package project.test.xface.entity.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 群聊信息表
 *
 * @TableName Group
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 群组id
     */
    private Long id;
    /**
     * 群组名称
     */
    private String name;
    /**
     * 创建者id
     */
    private Long creatorId;
    /**
     * 群组状态 0-正常 1-禁用等
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 族群类别，默认0-朋友群，1-家庭群，2-同事群 ...
     */
    private Integer type;

    private Long headerId;


    private String role;

}