package ming.test.xface.controller.user;


import cn.dev33.satoken.util.SaResult;
import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.dto.UserDTO;
import ming.test.xface.enity.dto.UserLoginDTO;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.enity.pojo.UserInfo;
import ming.test.xface.service.UserService;
import ming.test.xface.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


/**
 * 设置：修改个人信息，修改用户个人信息
 */
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

    /**
     * 登录后用户主页,返回个人信息and推荐blog，暂定为推送热点（缓存穿透），关注的人的dairy and blog
     * @return
     */
    @GetMapping("/checkMyself")
    public Result mainPage(){
        return userService.checkMyself();
    }

    /**
     * 修改个人信息
     * @param
     * @return
     */
    @PostMapping("/updateMyself")
    public SaResult updateMyself(@RequestBody UserInfo userInfo){

        return userService.updateMyself(userInfo);
    }
    /**
     * 修改用户个人信息
     * @return
     */
    @PostMapping("/update")
    public Result updateUser(@RequestBody User user){
      Result result=  userService.updateUser(user);
      return result;
    }

    /**
     * TODO:管理员只能删本群的成员（踢人），系统管理员可以删任何人，普通用户谁也不能删，群管理表待定
     * @param id
     * @return
     */
    @DeleteMapping({"id"})
    public Result deleteUser(@PathVariable Integer id){
            return userService.deleteUser(id);
    }

    /**
     * 安全退出，前端直接返回登陆页面
     * @param
     * @return
     */
    @GetMapping("/exit")
    public Result exitById(){
        UserHolder.removeUser();
        //todo:redis remove
        userService.exitUser();
        return Result.success();
    }

    /**
     * 关注
     */
    @GetMapping("/follow/{Id}")
    public SaResult follow(@PathVariable("Id") Integer id){

        return null;
    }

    /**
     * 查看他人主页,根据对方设定隐私级别判断可不可以看
     */
    @GetMapping("/checkUserInfo/{id}")
    public SaResult checkUserInfo(@PathVariable Integer id){
        return null;
    }

}
