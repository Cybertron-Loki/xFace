package project.test.xface.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.test.xface.entity.pojo.Products;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandVO  implements Serializable {
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
     * 商家名/品牌位置
     */
    private String location;

    private String images;

    private Long typeId;

    private List<Products> products;
}
