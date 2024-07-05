package project.test.xface.entity.pojo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 商家/品牌表
 *
 * @TableName brand
 */

@Data
public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商家id
     */
    private Long id;
    /**
     * 商家名/品牌名
     */
    private String name;
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

}