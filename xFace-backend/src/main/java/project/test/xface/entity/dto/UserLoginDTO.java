package project.test.xface.entity.dto;

import lombok.Data;

/**
 * 用户登录 DTO
 */
@Data
public class UserLoginDTO {
    /**
     * 登录手机号
     */
    private String phoneNum;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 登录状态码
     */
    private Integer status = 0;
    /**
     * code验证码，前端提交form表单验证
     */
    private String code;

}
