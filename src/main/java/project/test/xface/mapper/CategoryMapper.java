package project.test.xface.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.BlogType;

@Mapper
public interface CategoryMapper {


    @Select("select * from Category")
    BlogType selectAll();

    boolean addCategory(String blogTypeName);



    boolean deleteCategories(Integer[] ids);

    boolean updateCategory(BlogType blogType);
}
