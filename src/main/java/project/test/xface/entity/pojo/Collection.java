package project.test.xface.entity.pojo;


import lombok.Data;

import java.util.List;

@Data
public class Collection {
      private String name;
      private Long userId;
      private Long id;
      private List<Blog> blogList;

}
