package ming.test.xface.enity.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Long SerialVersionUID = 1L;

    /**
     * 用户 id 唯一
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 权限
     */
    private String role;
    /**
     * 状态
     */
    private Integer isDelete;
}
