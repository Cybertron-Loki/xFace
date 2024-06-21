package project.test.xface.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.Comment;

import java.util.List;

/**
 * @author XiaoMing
 * @description 针对表【comment(评论表)】的数据库操作Mapper
 * @createDate 2024-06-05 20:48:03
 * @Entity ming.test.xface.enity.pojo.Comment
 */
@Mapper
public interface CommentMapper  {

    boolean addComment(Comment comment);


   @Delete("delete from Comment where id=#{commentId} ")
    Boolean delete(Long commentId);


   @Select("select * from Comment where diaryId=#{diaryId}")
    List<Comment> selectByDiaryId(Long diaryId);

  @Select("select * from Blog where commentId=#{commentId}")
    Comment selectById(Long commentId);
}




