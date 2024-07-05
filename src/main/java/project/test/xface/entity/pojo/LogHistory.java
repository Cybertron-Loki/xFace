package project.test.xface.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 记载操作日志
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class LogHistory {
    private Long id;

    private String userName;    //who

    private String operationType; //did

    private LocalDateTime operationTime;  //when

}
