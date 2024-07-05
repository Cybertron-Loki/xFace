//package project.test.xface.interceptor;
//
//
//import project.test.xface.entity.dto.UserDTO;
//import project.test.xface.utils.UserHolder;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class LoginInterceptor implements HandlerInterceptor {
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        UserDTO userDTO = UserHolder.getUser();
//        if(userDTO==null) {
//            response.setStatus(40100);
//        }
//          return true;
//    }
//
//
//}
