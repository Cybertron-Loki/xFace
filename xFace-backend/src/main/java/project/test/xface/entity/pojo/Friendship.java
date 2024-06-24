package project.test.xface.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 友情状态存疑表
 *
 * @TableName Friendship
 */
@TableName(value = "Friendship")
@Data
public class Friendship implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 好友id
     */
    private Long friendId;
    /**
     * 好友名称
     */
    private String friendName;
    /**
     * 好友昵称
     */
    private String friendNickName;
    /**
     * 好友状态 0-正常 1-禁用等
     */
    private Integer friendStatus;
    /**
     * 好友关系状态 0-正常 1-禁用等
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


}