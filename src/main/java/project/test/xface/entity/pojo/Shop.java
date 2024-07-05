package project.test.xface.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    /**
     * 商家id
     */
    private Long id;
    /**
     * 商家名/品牌名
     */
    private String shopName;
    /**
     * 创建时间
     */
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;
    /**
     * 商家名/品牌位置
     */
    private String location;

    private String images;

    private Long typeId;


    private Double distance;

    private Double x;
    private Double y;
}
