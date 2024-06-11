package ming.test.xface.controller.user;

import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.pojo.Blog;
import ming.test.xface.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 三大modle之一，博客功能
 * 功能：发布新blog(分类，关联商家表)
 * 搜索相关blog（分页查询）；
 * 查相关blog在地图上（最后写）；
 * 点赞，
 * 关注，
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
     * 点赞
     * @return
     */
    @GetMapping("/like/{blogId}")
    public Result likeBlog(@PathVariable("blogId") Integer id){
        return null;
    }

    /**
     * 推荐blog
     */
    @GetMapping("/recommend")
    public Result recommendBlog(){
        return null;
    }
    /**
     * 关注
     */
    @GetMapping("/follow/{blogId}")
    public Result follow(@PathVariable("blogId") Integer id){
        return null;
    }

    /**
     * 评论
     */
    @PostMapping("/comment")
    public Result commentBlog(){
        return null;
    }
}
