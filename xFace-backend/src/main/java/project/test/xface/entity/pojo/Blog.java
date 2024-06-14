package project.test.xface.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
    private Integer id;
    /**
     * 用户id
     */
    private Integer userid;
    /**
     * 评论数量
     */
    private Integer commentCount;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 更新时间
     */
    private Date updatetime;
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
    private Integer brandId;
    /**
     * 所在位置
     */
    private String location;

    private Integer groupId;

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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Blog other = (Blog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
                && (this.getCommentCount() == null ? other.getCommentCount() == null : this.getCommentCount().equals(other.getCommentCount()))
                && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
                && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getVisible() == null ? other.getVisible() == null : this.getVisible().equals(other.getVisible()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getBrandId() == null ? other.getBrandId() == null : this.getBrandId().equals(other.getBrandId()))
                && (this.getLocation() == null ? other.getLocation() == null : this.getLocation().equals(other.getLocation()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getCommentCount() == null) ? 0 : getCommentCount().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getVisible() == null) ? 0 : getVisible().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getBrandId() == null) ? 0 : getBrandId().hashCode());
        result = prime * result + ((getLocation() == null) ? 0 : getLocation().hashCode());
        return result;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", userid=" + userid +
                ", commentid=" + commentCount +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", type=" + type +
                ", visible=" + visible +
                ", content=" + content +
                ", brandId=" + brandId +
                ", location=" + location +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}