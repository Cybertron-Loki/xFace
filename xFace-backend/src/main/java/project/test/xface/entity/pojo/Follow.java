package project.test.xface.entity.pojo;


import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
       private Integer id;

       private Integer userId;

       private Integer userFollowId;

       private DateTime  createTime;



}
