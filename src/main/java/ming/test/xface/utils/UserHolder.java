package ming.test.xface.utils;

import ming.test.xface.enity.dto.UserDTO;
import ming.test.xface.enity.pojo.User;

import java.util.HashMap;

/**
 * 线程ThreadLocal工具
 */
public class UserHolder {
        private static final ThreadLocal<UserDTO> tl=new ThreadLocal<>() ;

        public static void saveUser(UserDTO userDTO){ tl.set(userDTO);}

    public static UserDTO getUser(){
            return tl.get();
    }

    public static void removeUser(){
            tl.remove();
    }

}
