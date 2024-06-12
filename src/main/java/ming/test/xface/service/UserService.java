package ming.test.xface.service;

import cn.dev33.satoken.util.SaResult;
import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.dto.UserLoginDTO;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.enity.pojo.UserInfo;

/**
 * @author XiaoMing
 * @description 针对表【User(用户表)】的数据库操作Service
 * @createDate 2024-06-05 20:48:03
 */
public interface UserService {

    Result loginByMes(UserLoginDTO userLoginDTO);

    Result sendCode(String phonenum);

    Result loginByPsw(UserLoginDTO userLoginDTO);

    Result updateUser(User user);

    Result deleteUser(Integer id);

    Result checkMyself();

    void exitUser();

    SaResult updateMyself(UserInfo userInfo);
}
