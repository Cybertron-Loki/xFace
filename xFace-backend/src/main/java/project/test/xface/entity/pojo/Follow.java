package project.test.xface.entity.pojo;


import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
       private Long id;

       private Long userId;

       private Long userFollowId;

       private LocalDateTime createTime;



}
