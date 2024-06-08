package ming.test.xface.service;

import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.dto.UserLoginDTO;

import javax.servlet.http.HttpSession;

/**
 * @author XiaoMing
 * @description 针对表【User(用户表)】的数据库操作Service
 * @createDate 2024-06-05 20:48:03
 */
public interface UserService {

    Result loginByMes(UserLoginDTO userLoginDTO);

    Result sendCode(String phonenum);

    Result loginByPsw(UserLoginDTO userLoginDTO);
}
