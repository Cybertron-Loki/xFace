package ming.test.xface.config;

import ming.test.xface.common.JWTProperties;
import ming.test.xface.interceptor.LoginInterceptor;
import ming.test.xface.interceptor.RefreshInterceptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Component
public class MvcConfig implements WebMvcConfigurer {

   @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(new LoginInterceptor())
                 .excludePathPatterns("/user/login/*","/demo/*","/user/sendCode")
                 .order(1);
         registry.addInterceptor(new RefreshInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);

    }
}
