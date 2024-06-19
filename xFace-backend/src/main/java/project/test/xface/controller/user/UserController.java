package project.test.xface.controller.user;


import cn.dev33.satoken.util.SaResult;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserLoginDTO;
import project.test.xface.entity.pojo.UserInfo;
import project.test.xface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
     * 发验证码,存到缓存里 ,验证是更改手机号还是登录(lou)_
     */
    @PostMapping("/sendCode")
    public Result sendCode( String phoneNum,Integer lou){
           return  userService.sendCode(phoneNum, lou);
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
    public Result updateMyself(@RequestBody UserInfo userInfo){

        return userService.updateMyself(userInfo);
    }
    /**
     * 修改密码
     * @return
     */
    @PostMapping("/updatepsw")
    public Result updateUserPSW(String password){
      Result result=  userService.updateUserPSW(password);
      return result;
    }

    /**
     * 修改手机号，需要前端先发验证码
     * @param code
     * @param phoneNum
     * @return
     */
    @PostMapping("/updatephone")
    public Result updateUserPhone(@RequestParam("code") String code,@RequestParam("phoneNum") String phoneNum){
        Result result=  userService.updateUserPhone(code,phoneNum);
        return result;
    }




    /**
     * TODO:管理员只能删本群的成员（踢人），系统管理员可以删任何人，普通用户谁也不能删，群管理表待定
     * @param id
     * @return
     */
    @DeleteMapping({"id"})
    public Result deleteUser(@PathVariable Long id){
            return userService.deleteUser(id);
    }

    /**
     * 安全退出，前端直接返回登陆页面
     * @param
     * @return
     */
    @GetMapping("/exit")
    public Result exitById(HttpServletRequest request, HttpServletResponse response){
        return Result.success(userService.exitUser(request,response));
    }

    /**
     * 关注
     */
    @GetMapping("/follow")
    public Result follow( Long id,Boolean isFollow){
        return userService.follow(id,isFollow);
    }

    /**
     * 查看他人主页,根据对方设定隐私级别判断可不可以看
     */
    @GetMapping("/checkUserInfo/{id}")
    public Result checkUserInfo(@PathVariable Long id){
        return userService.checkUserInfo(id);
    }

    @GetMapping("/isFollow/{id}")
    public Result isFollow(@PathVariable("id") Long id){

        return userService.isFollow(id);
    }

    /**
     * 共同关注
     */
    @GetMapping("/followCommon/{id}")
    public Result followCommon(@PathVariable("id") Long userId){
        return userService.followCommon(userId);
    }



    /**
     * 禁用用户与启用用户(管理员功能)
     */
    @GetMapping("/startOtStop")
    public Result startOrStopUser(Long id,Integer status){
        return userService.startOrStop(id,status);
    }

    /**
     * 禁用用户与启用用户(管理员功能)
     */
    @GetMapping("/updateRole")
    public Result updateRole(Long id,String role){
        return userService.updateRole(id,role);
    }

    //todo：清除缓存
    private void cleanCache(String key){
    }

}
