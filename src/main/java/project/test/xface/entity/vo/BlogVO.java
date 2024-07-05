package project.test.xface.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考虑到需要用户头像，但如果再次查询只为了一个头像不值得，所以头像干脆写里。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogVO {
    private Long id;
    private Long userId;

    private String type;

    private Integer like;

    private String title;
    private String images;
    private Long products_id;
    private String visible;
    private Long group_id;
    private String userName;
}
