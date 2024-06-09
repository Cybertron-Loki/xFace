package ming.test.xface.interceptor;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import ming.test.xface.common.JWTProperties;
import ming.test.xface.enity.dto.UserDTO;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.utils.JwtUtil;
import ming.test.xface.utils.UserHolder;
import org.elasticsearch.common.collect.HppcMaps;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static ming.test.xface.common.RedisConstant.*;

/**
 * 刷新token有效时间拦截器
 */
public class RefreshInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;


    public RefreshInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)){
                 return true;
        }
        Map<Object, Object> userMap1 = stringRedisTemplate.opsForHash().entries(Login_Token_Key+token);
        Map<Object, Object> userMap2  =stringRedisTemplate.opsForHash().entries(Login_Password_Key+token);
        if(!userMap1.isEmpty()) {
            UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap1, new UserDTO(), false);
            UserHolder.saveUser(userDTO);
            stringRedisTemplate.expire(Login_Token_Key+token,Login_User_TTL, TimeUnit.MINUTES);
        }
        if(!userMap2.isEmpty()) {
            UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap1, new UserDTO(), false);
            UserHolder.saveUser(userDTO);
            stringRedisTemplate.expire(Login_Password_Key + token, Login_User_TTL, TimeUnit.MINUTES);
        }

           return true;      //啥也没有给到下一个拦截器，反正也过不去


    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
