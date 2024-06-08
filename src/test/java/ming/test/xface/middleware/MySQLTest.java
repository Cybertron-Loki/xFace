package ming.test.xface.middleware;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ming.test.xface.constant.RegexConstant;
import ming.test.xface.dao.UserMapper;
import ming.test.xface.enity.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MySQL 测试
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
public class MySQLTest {
    @Resource
    private UserMapper userMapper;

    @Test
    void UserDaoTest() {
        UserVO tmpUser = userMapper.getUserById(1);
        Assertions.assertNotNull(tmpUser);
        log.info("获取用户成功 {}", tmpUser);

        Matcher matcher = Pattern.compile(RegexConstant.PHONE_NUM).matcher(tmpUser.getPhoneNum());
        Assertions.assertTrue(matcher.matches());
    }

    @Test
    void UserPageTest() {
        PageHelper.startPage(2, 1);
        Page<UserVO> userVOPage = userMapper.allUserInfo();
        System.out.println(userVOPage);
    }
}
