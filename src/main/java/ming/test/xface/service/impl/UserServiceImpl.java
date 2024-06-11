package ming.test.xface.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;

import ming.test.xface.dao.UserMapper;
import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.dto.UserDTO;
import ming.test.xface.enity.dto.UserLoginDTO;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.enity.vo.UserVO;
import ming.test.xface.service.UserService;

import ming.test.xface.utils.RegexUtils;
import ming.test.xface.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static ming.test.xface.common.RedisConstant.*;

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
        return Result.success(token);
    }

    /**
     * 第一次注册
     *
     * @param
     */
    private User createUser(UserLoginDTO userLoginDTO) {
        User user = new User();
        BeanUtil.copyProperties(userLoginDTO, user);
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userLoginDTO.getPassword()).getBytes());
        user.setPassword(encryptPassword);
        String userName = UserName_PREFIX + RandomUtil.randomString(10);
        user.setUsername(userName);
        userMapper.save(user);
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
    public Result me() {
        UserDTO userDTO = UserHolder.getUser();
        Integer id = userDTO.getId();
        UserVO userVO = userMapper.getUserById(id);
        return Result.success(userVO);
    }

    @Override
    public void exitUser() {

    }
}




