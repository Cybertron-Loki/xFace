package project.test.xface.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.pojo.Comment;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.mapper.CommentMapper;
import project.test.xface.mapper.DiaryMapper;
import project.test.xface.service.DiaryService;
import project.test.xface.utils.UserHolder;

import java.util.List;


@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public Result createDiary(Diary diary) {

        return diaryMapper.addDiary(diary);
    }

    @Override
    public Result checkDiaries(Integer userId) {
        return null;
    }


    //返回日记and评论
    @Override
    public Result checkDiary(Integer dairyId) {
        return null;
    }


    @Override
    public Result deleteDiary(Integer id) {
        //有没有评论
        List<Comment> comments = diaryMapper.selectComment(id);
        for(Comment comment:comments){
            Long commentId = comment.getId();
            boolean isSuccess= commentMapper.delete(commentId); //同时删除子评论
            if(isSuccess==false) return Result.fail("删除失败");
        }
       boolean isSuccess= diaryMapper.deleteById(id);
        return Result.success(isSuccess);
    }


    /**
     * 评论diary
     * @param comment
     * @return
     */
    @Override
    public Result createComment(Comment comment) {
        UserDTO user = UserHolder.getUser();
        if(StringUtils.isEmpty(comment.toString())) return Result.fail("不能为空");
        Long userId = user.getId();
        comment.setUserId(userId);
        if(comment.getDiaryId().equals(null)) return  Result.fail("不能为空");
        boolean isSuccess=commentMapper.addComment(comment);
        if(!isSuccess) return Result.fail("评论未成功");
        return Result.success();
    }

}
