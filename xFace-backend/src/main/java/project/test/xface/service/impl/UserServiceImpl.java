package project.test.xface.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import project.test.xface.common.PageResult;
import project.test.xface.mapper.UserMapper;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.dto.UserLoginDTO;
import project.test.xface.entity.pojo.Follow;
import project.test.xface.entity.pojo.User;
import project.test.xface.entity.pojo.UserInfo;
import project.test.xface.entity.vo.UserVO;
import project.test.xface.service.UserService;

import project.test.xface.utils.RedisWorker;
import project.test.xface.utils.RegexUtils;
import project.test.xface.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static project.test.xface.common.RedisConstant.*;


@Service
public class UserServiceImpl implements UserService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Resource
    private RedisWorker redisWorker;


    /**
     * 短信登陆
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public Result loginByMes(UserLoginDTO userLoginDTO) {
        String phonenum = userLoginDTO.getPhoneNum();
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phonenum);// 二次检验。防止改手机号登录；或者前端写死不能改
        if (phoneInvalid == true)
            return Result.fail("手机号不对");


        String code = stringRedisTemplate.opsForValue().get(Login_Code_Key + phonenum);

        if (code == null || !code.equals(userLoginDTO.getCode())) {
            return Result.fail("验证码不对，登陆失败");
        }
        //是否注册过
        User user = userMapper.getByPhone(phonenum);

        if (user == null) {
            user = createUser(userLoginDTO);
        }
        if(user.getStatus()==1) return Result.fail("账号被锁定");

        String token = UUID.randomUUID().toString();  //给前端发token
        String tokenKey = Login_Token_Key + token;       //后端以hashmap形式存储到缓存

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userVO, new HashMap<>()
                , CopyOptions.create().setIgnoreNullValue(true)
                        .setFieldValueEditor(
                                (name, value) -> {
                                    if (value != null) {
                                        return value.toString();
                                    } else {
                                        return null; // 或者你想要的默认值
                                    }
                                }
                        ));

        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, Login_User_TTL, TimeUnit.MINUTES);
        stringRedisTemplate.delete(Login_Code_Key + phonenum);
        //登陆后移除缓存里的code
        return Result.success(token);
    }

    /**
     * 第一次注册
     *
     * @param
     */
    @Transactional
    public User createUser(UserLoginDTO userLoginDTO) {
        User user = new User();
        BeanUtil.copyProperties(userLoginDTO, user);
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userLoginDTO.getPassword()).getBytes());
        user.setPassword(encryptPassword);
        String userName = UserName_PREFIX + RandomUtil.randomString(10);
        user.setUsername(userName);
        Long userId = redisWorker.nextId("userId");
        user.setId(userId);
        userMapper.save(user);
        //创建userinfo,传的id不对，居然是1
        //用全局id生成器
        userMapper.createProfile(userId, userName);
        return user;
    }


    /**
     * 发验证码，存储到session里
     *
     * @param
     * @return
     */
    @Override
    public Result sendCode(String phoneNum,Integer lou) {
        //校验手机号格式是否正确
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phoneNum);
        if (phoneInvalid) {
            return Result.fail("手机号格式不对");   //手机号格式不对返回错误信息
        }
        String code = RandomUtil.randomString(4);
        //TODO 发送验证码给手机
        System.out.println(code);
        if(lou==1)
            stringRedisTemplate.opsForValue().set(Login_Code_Key + phoneNum, code, Login_Code_TTL, TimeUnit.MINUTES);
        if(lou==2)
            stringRedisTemplate.opsForValue().set(Update_User_Phone_Key + phoneNum, code, Login_Code_TTL, TimeUnit.MINUTES);
        return Result.success("验证码已经发送");
    }

    @Override
    public Result loginByPsw(UserLoginDTO userLoginDTO) {
        String phonenum = userLoginDTO.getPhoneNum();
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phonenum);// 二次检验。防止改手机号登录；或者前端写死不能改
        if (phoneInvalid == true)
            return Result.fail("手机号不对");

        User user = userMapper.getByPhone(phonenum);
        //是否注册过
        if (user == null) {
            user = createUser(userLoginDTO);
            return Result.success("账号注册成功，密码为刚才输入的密码,重新登陆");
        }
        if(user.getStatus()==1) return Result.fail("账号被锁定");
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userLoginDTO.getPassword()).getBytes());
        if (userLoginDTO.getPassword() == null || !(user.getPassword()).equals(encryptPassword)) {
            return Result.fail("密码不对，登陆失败");
        }

        //session里存状态码可能会简单一点
        String token = UUID.randomUUID().toString();  //给前端发token
        String tokenKey = Login_Password_Key + token;       //后端以hashmap形式存储到缓存，过第二层拦截器
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userVO, new HashMap<>()
                , CopyOptions.create().setIgnoreNullValue(true)
                        .setFieldValueEditor(
                                (name, value) -> {
                                    if (value != null) {
                                        return value.toString();
                                    } else {
                                        return null; // 或者你想要的默认值
                                    }
                                }
                        ));

        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, Login_User_TTL, TimeUnit.MINUTES);
        return Result.success(token);

    }

    @Override
    public Result updateUserPSW(String password) {
        if (password.length() < 8) return Result.fail("密码太短，重新想");
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        User user=new User();
        user.setPassword(encryptPassword);
        user.setId(UserHolder.getUser().getId());
        userMapper.updateUser(user);
        //todo:前端重新登陆,缓存需要清除,调用exit
        return Result.success();
    }
    @Override
    public Result updateUserPhone(String code,String phoneNum) {
        //修改手机号,需要发验证码验证身份 TODO:前端输入手机号就写死
        if (RegexUtils.isPhoneInvalid(phoneNum)) return Result.fail("手机号位数不对");
        //前端验证码选项发送请求
        String s = stringRedisTemplate.opsForValue().get(Update_User_Phone_Key + phoneNum);
        if (!code.equals(s) || code.equals(null))
            return Result.fail("修改失败，验证码不对");
        User user=new User();
        Long id = UserHolder.getUser().getId();
        user.setId(id);
        user.setPhoneNum(phoneNum);
        userMapper.updateUser(user);
        stringRedisTemplate.delete(Update_User_Phone_Key + phoneNum);
        //todo:前端重新登陆,缓存需要清除,调用exit
        return Result.success();
    }

    @Override
    public Result deleteUser(Long id) {
        userMapper.deleteById(id);
        //todo:清除缓存
        return Result.success();
    }

    @Override
    public Result checkMyself() {
        UserDTO userDTO = UserHolder.getUser();
        Long id = userDTO.getId();
        UserInfo userInfo = userMapper.getUserInfo(id);
        return Result.success(userInfo);
    }

    @Override
    public Result exitUser(HttpServletRequest request, HttpServletResponse response) {
       String token=request.getHeader("token");
       String tokenKey=Login_Password_Key +token;
       if(stringRedisTemplate.opsForValue().get(tokenKey)==null) {
           tokenKey = Login_Token_Key + token;
       }
       stringRedisTemplate.delete(tokenKey);
       UserHolder.removeUser();
       //后端有没有必要清除cookie
//        response.setCookie(request, response, "utoken", "", COOKIE_DELETE);
//        setCookie(request, response, "uid", "", COOKIE_DELETE);
        return Result.success("成功退出");
    }

    @Override
    public Result updateMyself(UserInfo userInfo) {
        //信息校验
        if (StringUtils.isEmpty(userInfo.toString()))
            return Result.fail("信息不能为空");
        //详细信息校验
        userInfo.setFans(null);
        userInfo.setFollowee(null);
        if(RegexUtils.isEmailInvalid(userInfo.getMail()))  return Result.fail("邮箱不对");
        userMapper.updateMyself(userInfo);
        //todo：村缓存+清缓存

        return Result.success("ok");
    }

    @Override
    public Result follow(Long id,Boolean isFollow) {
        UserDTO userDTO = UserHolder.getUser();
        Long followerId = userDTO.getId();
        String key = Follow_User_Key + id;
        if (!isFollow) {               //没关注过
            Follow follow = new Follow();
            follow.setUserId(id);
            follow.setUserFollowId(followerId);
           boolean isSuccesss= userMapper.follow(follow);
           if(isSuccesss){
               stringRedisTemplate.opsForSet().add(key, followerId.toString());
           }
        } else {
           boolean isSuccess= userMapper.unFollow(id,followerId);
           if(isSuccess){

               stringRedisTemplate.opsForSet().remove(key, followerId.toString());
           }
           //todo：userInfo表的follow需要增加
        }
        return Result.success();
    }


    /**
     * 前端高亮显示当前博主是否被user点赞 判断
     * @param id
     * @return
     */
    public Result isFollow(Long id){
        UserDTO userDTO = UserHolder.getUser();
        if (userDTO == null) return Result.fail("用户获取失败");
        Long followerId=userDTO.getId();
       Long count=userMapper.selectFollow(id,followerId);
       return Result.success(count>0);
    }

    @Override
    public Result followCommon(Long userId, Integer pageNum, Integer pageSize) {
        UserDTO user = UserHolder.getUser();
        if(StringUtils.isEmpty(user.toString())) return Result.fail("用户不能为空");
        Long id = user.getId();
//        String key=FollowCommon_Key+userId;
//        String key2=FollowCommon_Key+id;
//        Set<String> intersect=stringRedisTemplate.opsForSet().intersect(key,key2);
//        if(intersect==null||intersect.isEmpty()) return Result.success(Collections.emptyList());  //为啥用这个
//        List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
//        List<User> users = listByIds(ids);
////        List<UserDTO> collect = users.stream()
////                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
////                .collect(Collectors.toList());
        PageHelper.startPage(pageNum,pageSize);
        List<Long> followCommonIds=userMapper.selectCommonFollow(id,userId);
        PageInfo<Long> pageInfo=new PageInfo<>(followCommonIds);
        long total=pageInfo.getTotal();
        List<UserVO> commonFollow=new ArrayList<>(followCommonIds.size()); //不加长度行不行
       for(Long followCommonId : followCommonIds){
           UserVO userVO = userMapper.getUserById(followCommonId);
           commonFollow.add(userVO);
       }
        return Result.success(new PageResult(total,commonFollow));

    }

    @Override
    public Result startOrStop(Long id, Integer status) {
        UserVO userVO = userMapper.getUserById(id);
        if (StringUtils.isEmpty(userVO.toString())) {
            return Result.fail("账号为空");
        }        //安全检验
        User user=new User();
        user.setId(id);
        user.setStatus(status);
        boolean isSuccess=userMapper.updateUser(user);
        if(isSuccess)
            return Result.success();
        else
            return Result.fail("状态修改失败");
        //todo：删缓存
    }

    @Override
    public Result updateRole(Long id, String role) {
        //验证当前操作用户是否为管理员
        String role1 = UserHolder.getUser().getRole();
        if(!role1.equals("sysAdmin")) return Result.fail("无权限修改");
        UserVO userVO = userMapper.getUserById(id);
        if (StringUtils.isEmpty(userVO.toString())) {
            return Result.fail("账号为空");
        }                              //todo?:安全检验or修改加锁
        User user=new User();
        user.setId(id);
        user.setRole(role);
        boolean isSuccess=userMapper.updateUser(user);
        if(isSuccess)
            return Result.success();
        else
            return Result.fail("角色修改失败");
        //todo：删缓存
    }

    @Override
    public Result checkUserInfo(Long id) {
        UserInfo userInfo = userMapper.getUserInfo(id);
        if(StringUtils.isEmpty(userInfo.toString())) return Result.fail("用户不存在");
        return Result.success(userInfo);
    }


}




