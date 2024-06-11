package ming.test.xface.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.pojo.Blog;
import ming.test.xface.enity.pojo.BlogType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author XiaoMing
 * @description 针对表【blog(帖子表)】的数据库操作Mapper
 * @createDate 2024-06-05 20:48:03
 * @Entity ming.test.xface.enity.pojo.Blog
 */
public interface BlogMapper extends BaseMapper<Blog> {
   @Select("select * from type_blog ")
    List<BlogType> listType();

    void addBlog(Blog blog);


}




