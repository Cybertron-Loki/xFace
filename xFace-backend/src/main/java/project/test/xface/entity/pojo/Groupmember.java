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
    private Long groupid;
    /**
     * 用户id
     */
    private Long userid;
    /**
     * 群组状态 0-正常 1-禁用等
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
    /**
     * 群内昵称
     */
    private String nickname;

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
        Groupmember other = (Groupmember) that;
        return (this.getGroupid() == null ? other.getGroupid() == null : this.getGroupid().equals(other.getGroupid()))
                && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
                && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
                && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGroupid() == null) ? 0 : getGroupid().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        return result;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", groupid=" + groupid +
                ", userid=" + userid +
                ", status=" + status +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", nickname=" + nickname +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}