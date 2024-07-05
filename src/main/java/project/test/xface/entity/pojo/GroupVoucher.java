package project.test.xface.entity.pojo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class GroupVoucher {
    private Long creatorId;

    private Long voucherId;

    private Long groupId;
    /**
     * 库存
     */
    private Integer stock;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 生效时间
     */
    private LocalDateTime beginTime;

    /**
     * 失效时间
     */
    private LocalDateTime endTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;



}
