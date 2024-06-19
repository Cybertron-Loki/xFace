package project.test.xface.service;

import cn.dev33.satoken.util.SaResult;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserLoginDTO;
import project.test.xface.entity.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author XiaoMing
 * @description 针对表【User(用户表)】的数据库操作Service
 * @createDate 2024-06-05 20:48:03
 */
public interface UserService {

    Result loginByMes(UserLoginDTO userLoginDTO);

    Result sendCode(String phoneNum,Integer lou);

    Result loginByPsw(UserLoginDTO userLoginDTO);

    Result updateUserPSW(String password);

    Result updateUserPhone(String code,String phoneNum);

    Result deleteUser(Long id);

    Result checkMyself();

    Result exitUser(HttpServletRequest request, HttpServletResponse response);

    Result updateMyself(UserInfo userInfo);

    Result follow(Long id,Boolean isFollow);

    Result isFollow(Long id);

    Result followCommon(Long userId);

    Result startOrStop(Long id, Integer status);

    Result updateRole(Long id, String role);

    Result checkUserInfo(Long id);
}
