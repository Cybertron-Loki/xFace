package project.test.xface.entity.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {
      private Long    id ;//    '产品id',
      private Long   shopId ;   //'关联商家',
      private Long   brandId ;   //'关联品牌',
      private String   images ;// '照骗',
      private LocalDateTime createTime; //'创建时间',
      private LocalDateTime   updateTime; // '更新时间',
}
