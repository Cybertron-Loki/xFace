package ming.test.xface.interceptor;


import ming.test.xface.enity.dto.UserDTO;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ming.test.xface.common.RedisConstant.Login_Code_Key;

public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO userDTO = UserHolder.getUser();
        if(userDTO==null) {
            response.setStatus(40100);
        }
          return true;
    }


}
