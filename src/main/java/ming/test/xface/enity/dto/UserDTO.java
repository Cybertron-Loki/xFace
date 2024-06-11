package ming.test.xface.enity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户唯一标识
     */
    private Integer id;
    /**
     * 手机号码
     */
    private String phoneNum;
    /**
     * 用户角色 user-普通用户 admin-管理员等
     */
    private String role;
    /**
     * 用户状态 0-正常 1-禁用等
     */
    private Integer status;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String nickName;
}
