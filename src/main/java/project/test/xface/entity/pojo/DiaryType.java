package project.test.xface.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryType {
    private Long userId;
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
