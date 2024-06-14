package project.test.xface.config;

import project.test.xface.interceptor.*;
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
                .excludePathPatterns("/user/login/*", "/demo/*", "/user/sendCode", "/doc.html", "/v2/api-docs", "/webjars/**")
                 .order(1);
        registry.addInterceptor(new RefreshInterceptor(stringRedisTemplate)).addPathPatterns("/**")
                .excludePathPatterns("/doc.html", "/v2/api-docs", "/webjars/**").order(0);

    }
}
