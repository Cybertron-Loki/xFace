package project.test.xface.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;

import project.test.xface.common.ResultUtils;
import project.test.xface.mapper.UserMapper;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.dto.UserLoginDTO;
import project.test.xface.entity.pojo.Follow;
import project.test.xface.entity.pojo.User;
import project.test.xface.entity.pojo.UserInfo;
import project.test.xface.entity.vo.UserVO;
import project.test.xface.service.UserService;

import project.test.xface.utils.RegexUtils;
import project.test.xface.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static project.test.xface.common.RedisConstant.*;

/**
 * @author XiaoMing
 * @description 针对表【User(用户表)】的数据库操作Service实现
 * @createDate 2024-06-05 20:48:03
 */
@Service
public class UserServiceImpl implements UserService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;


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
        //登陆后移除缓存里的code
        // stringRedisTemplate.delete(Login_Code_Key + phonenum);
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
        Integer id = userMapper.save(user);
        //todo：创建userinfo,传的id不对，居然是1
        userMapper.createProfile(id, userName);
        return user;
    }


    /**
     * 发验证码，存储到session里
     *
     * @param phonenum
     * @return
     */
    @Override
    public Result sendCode(String phonenum) {
        //校验手机号格式是否正确
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phonenum);
        if (phoneInvalid) {
            return Result.fail("手机号格式不对");   //手机号格式不对返回错误信息
        }
        String code = RandomUtil.randomString(4);
        //TODO 发送验证码给手机
        System.out.println(code);
        stringRedisTemplate.opsForValue().set(Login_Code_Key + phonenum, code, Login_Code_TTL, TimeUnit.MINUTES);
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
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userLoginDTO.getPassword()).getBytes());
        if (userLoginDTO.getPassword() == null || !(user.getPassword()).equals(encryptPassword)) {
            return Result.fail("密码不对，登陆失败");
        }


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
    public Result updateUser(User user) {
        //权限分级管理？前端自己判断，传参
        String password = user.getPassword();
        if (password.length() < 8) return Result.fail("密码太短，重新想");
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getPassword()).getBytes());
        user.setPassword(encryptPassword);
        //todo:blog可见范围
        userMapper.updateUser(user);
        return Result.success();
    }


    @Override
    public Result deleteUser(Integer id) {
        userMapper.deleteById(id);
        return null;
    }

    @Override
    public Result checkMyself() {
        UserDTO userDTO = UserHolder.getUser();
        Integer id = userDTO.getId();
        UserInfo userInfo = userMapper.getUserInfo(id);
        return Result.success(userInfo);
    }

    @Override
    public void exitUser() {

    }

    @Override
    public SaResult updateMyself(UserInfo userInfo) {
        //信息校验
        if (StringUtils.isEmpty(userInfo.toString()))
            return ResultUtils.error(500, "信息不能为空");
        //todo:详细信息校验
        userMapper.updateMyself(userInfo);
        return ResultUtils.success("ok");
    }

    @Override
    public Result follow(Integer id,Boolean isFollow) {
        UserDTO userDTO = UserHolder.getUser();
        Integer followerId = userDTO.getId();
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

        }
        return Result.success();
    }


    /**
     * 前端高亮显示当前博主是否被user点赞
     * @param id
     * @return
     */
    public Result isFollow(Integer id){
        UserDTO userDTO = UserHolder.getUser();
        if (userDTO == null) return Result.fail("用户获取失败");
       Integer followerId=userDTO.getId();
       Long count=userMapper.selectFollow(id,followerId);
       return Result.success(count>0);
    }
}




