package project.test.xface.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @TableName User
 */
@TableName(value = "User")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户唯一标识
     */
    private Long id;
    /**
     * 登录密码
     */
    private String password;
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
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;

    /**
     * 修改手机号需要的code
     */
    private String phoneUpdateCode;


}