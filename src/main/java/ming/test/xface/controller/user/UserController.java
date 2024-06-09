package ming.test.xface.controller.user;


import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.dto.UserLoginDTO;
import ming.test.xface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
     private UserService userService;
    /**
     * 1:短信登陆
     * @return
     */
    @PostMapping("/login/mes")
    public Result LoginByMes(@RequestBody(required = false) UserLoginDTO userLoginDTO){
        return userService.loginByMes(userLoginDTO);
    }

    /**
     * 发验证码,存到缓存里
     */
    @PostMapping("/sendCode")
    public Result sendCode(@RequestParam("phone") String phoneNum){
           return  userService.sendCode(phoneNum);
    }

    /**
     * 密码登录,判断是否是新用户并注册，生成JWT令牌,存到缓存里
     */
    @PostMapping("/login/psw")
    public Result LoginByPsw(@RequestBody UserLoginDTO userLoginDTO){
        Result result = userService.loginByPsw(userLoginDTO);
        return result;
    }
}
