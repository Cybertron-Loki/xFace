package project.test.xface.service;

import cn.dev33.satoken.util.SaResult;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Blog;
import project.test.xface.entity.pojo.Comment;

import java.util.List;

/**
 *
 * @description 针对表【blog(帖子表)】的数据库操作Service
 * @createDate 2024-06-05 20:48:03
 */
public interface BlogService {

    Result createBlog(Blog blog);

    Result locationList() throws Exception;

    Result typeList();

    Result likeBlog(Integer id);

    Blog listBlog(Integer id);

    List<Comment> listComment(Integer blogId);

    Result queryFollowBlog(Long max, Integer offset);

    Result createComment(Comment comment);

    Result deleteBlog(Integer id);

    Result deleteComment(Long commentId, Long userId);
}
