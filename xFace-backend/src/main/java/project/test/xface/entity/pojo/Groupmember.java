package project.test.xface.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 群成员信息表
 *
 * @TableName GroupMember
 */
@TableName(value = "GroupMember")
@Data
public class Groupmember implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 群组状态 0-正常 1-禁用等
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 群内昵称
     */
    private String nickName;

    private String role;


}