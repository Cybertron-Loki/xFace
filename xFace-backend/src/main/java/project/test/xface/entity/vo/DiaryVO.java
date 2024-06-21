package project.test.xface.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.test.xface.entity.pojo.Comment;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记and评论
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryVO {
    private Long userId;
    private Long typeId;
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String visible;
    private List<Comment> commentList;
}
