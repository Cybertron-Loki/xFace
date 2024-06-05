package ming.test.xface.enity.dto;

import lombok.Data;

/**
 * 用户登录 DTO
 */
@Data
public class UserLoginDTO {
    /**
     * 登录手机号
     */
    private String phonenum;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 登录状态码
     */
    private Integer status = 0;
}
