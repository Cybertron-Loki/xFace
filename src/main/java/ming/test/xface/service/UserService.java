package ming.test.xface.service;

import ming.test.xface.enity.pojo.User;

/**
 * 用户服务
 */
public interface UserService {

    User getUserById(Integer id);

    Integer addUser(User user);

}
