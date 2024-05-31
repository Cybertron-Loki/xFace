package ming.test.xface.middleware;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

/**
 * MySQL 测试
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
public class MySQLTest {
    @Resource
    private UserService userService;

    @Test
    void UserDaoTest() {
        User user = new User();
        user.setUsername("xiaoming");
        user.setNickname("小明");
        user.setAccount("XiaoMing");
        String password = DigestUtil.md5Hex("xiaoming");
        user.setPassword(password);

        Integer res = userService.addUser(user);
        Assertions.assertEquals(1, res);
        log.info("添加用户成功{}", res);

        User tmpUser = userService.getUserById(res);
        Assertions.assertNotNull(tmpUser);
        log.info("获取用户成功 {}", tmpUser);


    }
}
