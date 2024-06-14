package project.test.xface.middleware;

import lombok.extern.slf4j.Slf4j;
import project.test.xface.entity.pojo.User;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis 测试
 */
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class RedisTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    void RedisTest() {
        RBucket<User> testBucket = redissonClient.getBucket("redis_test_user");
        log.info("testBucket: {}", testBucket.get());
        // 100s 后过期
        testBucket.set(new User(), 100000L, TimeUnit.MILLISECONDS);

        RList<User> userList = redissonClient.getList("redis_test_user_list");
        for (int i = 0; i < 5; i++) {
            User user = new User();
            userList.add(user);
        }
        // 100s 后过期 虽然方法过期了，但是问题不大
        userList.expire(100000L, TimeUnit.MILLISECONDS);
    }
}
