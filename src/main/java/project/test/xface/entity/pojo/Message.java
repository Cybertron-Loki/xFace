package project.test.xface.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 消息表
 *
 * @TableName Message
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 消息id
     */
    private Long id;
    /**
     * 会话id 私聊/群聊
     */
    private String conversationId;
    /**
     * 发送者id
     */
    private Long senderId;
    /**
     * 接收者id 空为群聊信息，非空为私聊信息
     */
    private Long receiverId;
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
    private LocalDateTime createTime;
    /**
     *
     */
    private Long groupId;
}