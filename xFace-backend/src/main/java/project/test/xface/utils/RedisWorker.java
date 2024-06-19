package project.test.xface.utils;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisWorker {
    private static final Long BEGIN_TIMESTAMP = 1640995200L;
    private static final Integer COUNT_BITS = 32;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Long nextId(String keyPrefix){
        LocalDateTime now=LocalDateTime.now();
        long nowScond=now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp=nowScond-BEGIN_TIMESTAMP;
        //序列号
        String today=now.format(DateTimeFormatter.ofPattern("yyyyyMMdd"));
        Long count=stringRedisTemplate.opsForValue().increment("icr"+keyPrefix+":"+today);
        return timeStamp<<COUNT_BITS|count;
    }
}
