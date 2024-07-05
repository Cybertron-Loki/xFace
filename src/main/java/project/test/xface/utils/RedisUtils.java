package project.test.xface.utils;


import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import project.test.xface.entity.dto.Result;

import java.util.*;
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
    public <R,ID> List<R> queryWithPassThroughSet(String keyPrefix,
                                         ID id, Class<R> type,
                                         Function<ID,List<R>> dbFallBack,
                                         Long time, TimeUnit unit){
        String key=keyPrefix+id;
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if(members != null && !members.isEmpty()) {
//            String json = stringRedisTemplate.opsForValue().get(key);
//            if (!StringUtils.isEmpty(json)) {
//                return JSONUtil.toBean(json, type);
//            }
            List<R> list=new ArrayList<>();
            for (String member : members) {
                R bean = JSONUtil.toBean(member, type);
                list.add(bean);
            }
            return list;
        }
        // If not found or found empty placeholder, fetch from DB
        List<R> fetchedList = dbFallBack.apply(id);

        if (fetchedList == null || fetchedList.isEmpty()) {
            // Set empty placeholder in the set
            stringRedisTemplate.opsForSet().add(key,"{}");
            stringRedisTemplate.expire(key, 5, TimeUnit.MINUTES);
            return Collections.emptyList();
        }
        // Update cache with fetched value
        for(R r:fetchedList) {
            String jsonStr = JSONUtil.toJsonStr(r);
            stringRedisTemplate.opsForSet().add(key, jsonStr);
        }
              return fetchedList;
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



}
