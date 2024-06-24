package project.test.xface.controller.blog;

import cn.dev33.satoken.util.SaResult;
import org.apache.commons.lang3.StringUtils;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Blog;
import project.test.xface.entity.pojo.Comment;
import project.test.xface.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 三大modle之一，博客功能
 * 功能：发布新blog(分类，关联商家表)
 * 搜索相关blog（分页查询）；
 * 查相关blog在地图上（最后写）；
 * 点赞，
 * 关注(user)
 * 刷blog。
 */

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;
    /**
     * 发布新blog
     * @return
     */
    @PostMapping("/create")
    public Result createBlog(@RequestBody Blog blog){
        return blogService.createBlog(blog);
    }

    /**
     * 前端下拉框，地点,第一个显示当前位置，之后按照省市来
     * @return
     */
    @GetMapping("/create/location")
    public Result locationList() throws Exception {
        return blogService.locationList();
    }

    /**
     * 前端下拉框，返回blog种类(必选)
     * @return
     */
    @GetMapping("/create/getType")
    public Result typeList(){
        return blogService.typeList();
    }


    /**
     * 查看blog todo:分页
     */
    @GetMapping("/listBlog/{id}")
    public Result listBlog(@PathVariable Integer id){

           return Result.success(blogService.listBlog(id));
    }
    /**
     * 点赞，取消点赞
     * @return
     */
    @GetMapping("/like/{blogId}")
    public Result likeBlog(@PathVariable("blogId") Integer id){
        return Result.success(blogService.likeBlog(id));
    }

    /**
     * todo:推荐blog，主推关注的人/群里人新发的，其次是热点key
     */
    @GetMapping("/recommend")
    public Result recommendBlog(@RequestParam("lastId")Long max,@RequestParam(value="offset",defaultValue = "0")Integer offset){
        blogService.queryFollowBlog(max,offset);
        return null;
    }


    /**
     * 查看评论
     */
    @PostMapping("/checkComment/{blogId}")
    public Result commentBlog(@PathVariable("blogId") Integer blogId){
       List<Comment> commentList= blogService.listComment(blogId);
        return Result.success(commentList);
    }

    /**
     * 评论blog
     */
    @PostMapping("/createComment")
    public Result createComment(Comment comment) {
        if (!StringUtils.isEmpty(comment.toString()))
            return Result.fail("不能为空");
        return blogService.createComment(comment) ;
    }
    /**
     * todo：数字字典，blog分类管理员添加
     */

    /**
     * 删除
     */
    @DeleteMapping("/delteBlog")
    public Result deleteBlog(Integer id){
      return  blogService.deleteBlog(id);
    }
    /**
     * 删除blog评论
     */
    @DeleteMapping("/deleteComment")
    public Result deleteComment(Long commentId,Long userId){
        return blogService.deleteComment(commentId,userId);
    }
    /**
     * 查shop/brand关键词搜相关blog
     */

}
