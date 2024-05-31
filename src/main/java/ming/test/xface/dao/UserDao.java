package ming.test.xface.dao;

import ming.test.xface.enity.pojo.User;

public interface UserDao {

    User getUserById(Integer id);

    Integer addUser(User user);

}
