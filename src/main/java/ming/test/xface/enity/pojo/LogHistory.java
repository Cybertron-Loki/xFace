package ming.test.xface.enity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ming.test.xface.aspect.OperationType;

import java.time.LocalDateTime;

/**
 * 记载操作日志
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class LogHistory {
    private Integer id;

    private String userName;    //who

    private String operationType; //did

    private LocalDateTime operationTime;  //when

}
