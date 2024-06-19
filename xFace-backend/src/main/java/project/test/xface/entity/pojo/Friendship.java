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
    private Long userid;
    /**
     * 好友id
     */
    private Long friendid;
    /**
     * 好友名称
     */
    private String friendname;
    /**
     * 好友昵称
     */
    private String friendnickname;
    /**
     * 好友状态 0-正常 1-禁用等
     */
    private Integer friendstatus;
    /**
     * 好友关系状态 0-正常 1-禁用等
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;

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
        Friendship other = (Friendship) that;
        return (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
                && (this.getFriendid() == null ? other.getFriendid() == null : this.getFriendid().equals(other.getFriendid()))
                && (this.getFriendname() == null ? other.getFriendname() == null : this.getFriendname().equals(other.getFriendname()))
                && (this.getFriendnickname() == null ? other.getFriendnickname() == null : this.getFriendnickname().equals(other.getFriendnickname()))
                && (this.getFriendstatus() == null ? other.getFriendstatus() == null : this.getFriendstatus().equals(other.getFriendstatus()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
                && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getFriendid() == null) ? 0 : getFriendid().hashCode());
        result = prime * result + ((getFriendname() == null) ? 0 : getFriendname().hashCode());
        result = prime * result + ((getFriendnickname() == null) ? 0 : getFriendnickname().hashCode());
        result = prime * result + ((getFriendstatus() == null) ? 0 : getFriendstatus().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", userid=" + userid +
                ", friendid=" + friendid +
                ", friendname=" + friendname +
                ", friendnickname=" + friendnickname +
                ", friendstatus=" + friendstatus +
                ", status=" + status +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}