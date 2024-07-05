package project.test.xface.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
          private  Long  id;
          private  String content;
          private  Long  sender;
          private  Long  receiver;
          private Integer type;
          private Integer reply;
          private Long groupId;
}
