package project.test.xface.service.impl;

import cn.dev33.satoken.util.SaResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;
import project.test.xface.common.ResultUtils;
import project.test.xface.entity.dto.ScrollResult;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.mapper.BlogMapper;
import project.test.xface.mapper.CommentMapper;
import project.test.xface.mapper.DiaryMapper;
import project.test.xface.mapper.UserMapper;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.pojo.Blog;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Comment;
import project.test.xface.entity.vo.UserVO;
import project.test.xface.service.BlogService;
import project.test.xface.utils.BaiduMapUtils;
import project.test.xface.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static project.test.xface.common.RedisConstant.FollowerUser_Feed_Key;
import static project.test.xface.common.RedisConstant.Like_Blog_Key;
import static project.test.xface.utils.BaiduMapUtils.AK;
import static project.test.xface.utils.BaiduMapUtils.URL;

/**
 * @author XiaoMing
 * @description 针对表【blog(帖子表)】的数据库操作Service实现
 * @createDate 2024-06-05 20:48:03
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private DiaryMapper diaryMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result createBlog(Blog blog) {
        UserDTO userDTO = UserHolder.getUser();
        Long userid = userDTO.getId();
        blog.setUserid(userid);
        boolean isSuccess=blogMapper.addBlog(blog);
        if(!isSuccess) return Result.fail("新增失败");
        //feed流给粉丝发
        List<Long> followersId = userMapper.selectFollowers(userid);
        for(Long followerId:followersId) {
            String key = FollowerUser_Feed_Key + followerId;
           stringRedisTemplate.opsForZSet().add(key, String.valueOf(blog.getId()),System.currentTimeMillis());
        }
        return Result.success(blog.getId());
    }

    @Override
    public Result locationList() throws Exception {

//        blogMapper.locationList();
        /**
         * 选择了ak或使用IP白名单校验：
         */
            BaiduMapUtils snCal = new BaiduMapUtils();
            Map params = new LinkedHashMap<String, String>();
            params.put("keyword", "全国");
            params.put("sub_admin", "2");
            params.put("ak", AK);
            snCal.requestGetAK(URL, params);
        return Result.success(URL);
    }

    @Override
    public Result typeList() {
        List<BlogType> blogTypes = blogMapper.listType();
        return Result.success(blogTypes);
    }

    /**
     * 点赞的人和被点赞的blog存在redis里，减轻数据库压力
     * @param id
     * @return
     */
    @Override
    public Result likeBlog(Integer id) {
        UserDTO userDTO = UserHolder.getUser();
        long userId = userDTO.getId();
        String key = Like_Blog_Key + id;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId);
        if (score == null) {
            boolean isSuccess = blogMapper.likeBlog(id); //like加一
            if (isSuccess)
                stringRedisTemplate.opsForZSet().add(key, String.valueOf(userId), System.currentTimeMillis());
        }   //没点过赞
        else {
             boolean isSuccess = blogMapper.dislikeBlog(id);   //点赞取消
            if (isSuccess)
                stringRedisTemplate.opsForZSet().remove(key, userId);
        }
        return Result.success("ok");
    }

    /**
     * 查询list，返回（是否点赞，高亮）
     */
    @Override
    public Blog listBlog(Integer id) {
           Blog blog=blogMapper.getByBlogId(id);
           if(blog==null) return null;
           isLiked(blog);
           queryUser(blog);
           return blog;
    }

    @Override
    public List<Comment> listComment(Integer blogId) {
        //todo:实现朋友的评论在前
        return blogMapper.selectComment(blogId);
    }


    @Override
    public Result queryFollowBlog(Long max, Integer offset) {
        UserDTO user = UserHolder.getUser();
        String key=FollowerUser_Feed_Key+ user.getId();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().
                reverseRangeByScoreWithScores(key, 0, max, offset, 2);
    if(typedTuples==null||typedTuples.isEmpty()){
        return Result.success();
    }
    List<Integer> ids=new ArrayList<>(typedTuples.size());
    long minTime=0;
    int os=1;
    for(ZSetOperations.TypedTuple<String> typedTuple:typedTuples) {
        String blogId = typedTuple.getValue();
        ids.add(Integer.valueOf(blogId));
        long time = typedTuple.getScore().longValue();
        if (time == minTime) {
            os++;
        } else {
            minTime = time;
            os = 1;
        }
    }
        List<Blog> blogs=new ArrayList<>(ids.size());
        for(Integer id:ids){
           Blog blog=listBlog(id);
           blogs.add(blog);
        }
        blogs.forEach(this::isLiked);
        ScrollResult scrollResult = new ScrollResult();
        scrollResult.setList(blogs);
        scrollResult.setOffset(os);
        scrollResult.setMinTime(minTime);
        return Result.success(scrollResult);
    }

    /**
     * 评论blog
     * @param comment
     * @return
     */
    @Override
    public Result createComment(Comment comment) {
        UserDTO user = UserHolder.getUser();
        if(StringUtils.isEmpty(comment.toString())) return Result.fail("不能为空");
        Long userId = user.getId();
        comment.setUserId(userId);
        if(comment.getBlogId()==null) return  Result.fail("不能为空");
        boolean isSuccess=commentMapper.addComment(comment);
       if(!isSuccess){ return Result.fail("评论未成功");}
        return Result.success();
    }

    @Override
    public Result deleteBlog(Integer id) {
        //有没有评论
        List<Comment> comments = blogMapper.selectComment(id);
        for(Comment comment:comments){
            Long commentId = comment.getId();
        boolean isSuccess= commentMapper.delete(commentId); //同时删除子评论
        if(isSuccess) return Result.fail("删除失败");
        }
        boolean isSuccess = blogMapper.deleteById(id);
        return Result.success(isSuccess);
    }

    @Override
    public Result deleteComment(Long commentId, Long userId) {
        //todo:有点麻烦:验证身份是否是blog主/评论主
       Comment comment= commentMapper.selectById(commentId);
        Diary diary = diaryMapper.selectById(comment.getDiaryId());
        if(comment.getUserId().equals(userId)||diary.getUserid().equals(userId)){
            Boolean delete = commentMapper.delete(commentId);
            if(delete) return Result.success();
        }
        return Result.fail("删除失败");
    }

    /**
     * blog查询用户头像等信息
     * @param
     */
    private void queryUser(Blog blog) {
        UserDTO userDTO = UserHolder.getUser();
        UserVO userVO = userMapper.getUserById(userDTO.getId());
        blog.setAvatar(userVO.getAvatar());
    }

    private void isLiked(Blog blog){
        UserDTO userDTO = UserHolder.getUser();
        if(userDTO==null) return ;
        Long id = userDTO.getId();
        String key=Like_Blog_Key+blog.getId();
        Double score = stringRedisTemplate.opsForZSet().score(key,id.toString());
        blog.setLiked(score!=null);
    }



}




