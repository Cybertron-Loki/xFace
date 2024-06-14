package project.test.xface.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息表
 *
 * @TableName Message
 */
@TableName(value = "Message")
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 消息id
     */
    private Integer id;
    /**
     * 会话id 私聊/群聊
     */
    private String conversationid;
    /**
     * 发送者id
     */
    private Integer senderid;
    /**
     * 接收者id 空为群聊信息，非空为私聊信息
     */
    private Integer receiverid;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息类型 text/image/file/video/audio/etc
     */
    private String type;
    /**
     *
     */
    private Date createtime;

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
        Message other = (Message) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getConversationid() == null ? other.getConversationid() == null : this.getConversationid().equals(other.getConversationid()))
                && (this.getSenderid() == null ? other.getSenderid() == null : this.getSenderid().equals(other.getSenderid()))
                && (this.getReceiverid() == null ? other.getReceiverid() == null : this.getReceiverid().equals(other.getReceiverid()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getConversationid() == null) ? 0 : getConversationid().hashCode());
        result = prime * result + ((getSenderid() == null) ? 0 : getSenderid().hashCode());
        result = prime * result + ((getReceiverid() == null) ? 0 : getReceiverid().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", conversationid=" + conversationid +
                ", senderid=" + senderid +
                ", receiverid=" + receiverid +
                ", content=" + content +
                ", type=" + type +
                ", createtime=" + createtime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}