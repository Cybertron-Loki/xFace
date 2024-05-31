package ming.test.xface.service.impl;

import lombok.extern.slf4j.Slf4j;
import ming.test.xface.dao.UserDao;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User getUserById(Integer id) {
        User userById = userDao.getUserById(id);
        log.info("userById:{}", userById);
        return userById;
    }

    @Override
    public Integer addUser(User user) {
        // TODO 参数校验
        return userDao.addUser(user);
    }
}
