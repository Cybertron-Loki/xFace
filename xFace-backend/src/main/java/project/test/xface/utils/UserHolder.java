package project.test.xface.utils;

import project.test.xface.entity.dto.UserDTO;

/**
 * 线程ThreadLocal工具
 */
public class UserHolder {

    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO userDTO) {
        tl.set(userDTO);
    }

    public static UserDTO getUser(){
            return tl.get();
    }

    public static void removeUser(){
            tl.remove();
    }

}
