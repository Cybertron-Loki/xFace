package project.test.xface.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 评论表
 *
 * @TableName comment
 */
@TableName(value = "comment")
@Data
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 评论id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 日记id
     */
    private Integer diaryId;
    /**
     * blog评论
     */
    private Integer blogId;
    /**
     * 父评论id
     */
    private Integer parentId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}