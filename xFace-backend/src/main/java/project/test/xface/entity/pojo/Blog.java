package project.test.xface.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 帖子表
 *
 * @TableName blog
 */
@TableName(value = "blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 个人发表的避雷/种草/旅游经验帖子
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userid;
    /**
     * 评论数量
     */
    private Integer commentCount;
    /**
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;
    /**
     * blog类型
     */
    private String type;
    /**
     * 隐私：先定为公开可见或者仅群内可见或者仅自己可见
     */
    private String visible;
    /**
     * blog内容
     */
    private String content;
    /**
     * 标题
     */
    private String title;
    /**
     * 关联商家id（可不填）
     */
    private Long brandId;
    /**
     * 所在位置
     */
    private String location;

    private Long groupId;

    private Integer like;

    /**
     * 当前用户是否点赞过
     */
    @TableField(exist = false)
    private boolean isLiked;

    /**
     * 探店的照片，最多9张，多张以","隔开
     */
    private String images;

    @TableField(exist = false)
    private String avatar;


}