package project.test.xface.utils;


import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static project.test.xface.common.RedisConstant.Redis_Null_TLL;

@Component
public class RedisUtils {


    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }



    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }
    /**
     * 设置空值解决缓存击穿问题
     */
    public <R,ID> R queryWithPassThrough(String keyPrefix,
                                         ID id, Class<R> type,
                                         Function<ID,R> dbFallBack,
                                         Long time, TimeUnit unit){
        String key=keyPrefix+id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(json)){
            return JSONUtil.toBean(json,type);
        }
        if("".equals(json)){
            return null;
        }
       R r= dbFallBack.apply(id);
        if(r==null){
            this.set(key,"",Redis_Null_TLL,TimeUnit.SECONDS);
            return null;
        }
        this.set(key,r,time,unit);
        return r;
    }

    private void cleanCache(String keyPrefix){
        Set keys=stringRedisTemplate.keys(keyPrefix);
        stringRedisTemplate.delete(keys);
    }

}
