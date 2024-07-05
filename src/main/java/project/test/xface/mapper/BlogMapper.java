package project.test.xface.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import project.test.xface.entity.pojo.Blog;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlogMapper  {
   @Select("select * from Type_Blog ")
    List<BlogType> listType();

    boolean addBlog(Blog blog);


    boolean likeBlog(Integer id);

    boolean dislikeBlog(Integer id);


    @Select("select * from Blog where id=#{id}")
    Blog getByBlogId(Integer id);

    @Select("select * from Comment where  blogId=#{blogId}")
    List<Comment> selectComment(Integer blogId);


    @Delete("delete from Diary where id=#{id}")
    boolean deleteById(Integer id);

}




