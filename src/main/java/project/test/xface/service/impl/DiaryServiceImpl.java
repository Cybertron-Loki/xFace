package project.test.xface.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.test.xface.common.PageResult;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.pojo.Comment;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.entity.pojo.DiaryType;
import project.test.xface.entity.vo.DiaryVO;
import project.test.xface.entity.vo.DiaryTypeVO;
import project.test.xface.mapper.CommentMapper;
import project.test.xface.mapper.DiaryMapper;
import project.test.xface.mapper.FileTypeMapper;
import project.test.xface.service.DiaryService;
import project.test.xface.utils.RedisWorker;
import project.test.xface.utils.UserHolder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static project.test.xface.common.RedisConstant.Diary_ID;


@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private FileTypeMapper fileTypeMapper;
    @Resource
    private RedisWorker redisWorker;
    @Override
    public Result createDiary(Diary diary) {
        Long id = redisWorker.nextId(Diary_ID);
        diary.setId(id);
        return diaryMapper.addDiary(diary);
    }

    /**
     * 需要判断查看者身份
     * 先写一个自己查看的
     *
     * @param userId
     * @param visitorId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result checkDiaries(Long userId, Long visitorId, Integer pageNum, Integer pageSize) {
//        if(1==1) {
//            PageHelper.startPage(pageNum,pageSize);
//            List<DiaryTypeVO> diaries=new ArrayList<>();
//            Page<DiaryType> diaryTypes=fileTypeMapper.selectByUserId(userId);
//            long total=diaryTypes.getTotal();
//            List<DiaryType> result = diaryTypes.getResult();
//            for(DiaryTypeVO diaryTypeVO:diaries) {
//                for (DiaryType diaryType : diaryTypes) {
//                    List<Diary> diaryList = diaryMapper.selectByType(diaryType.getId());
//                    diaryTypeVO.setDiaries(diaryList);
//                    diaryTypeVO.setDiaryType(diaryType);
//                }
//            }
//
//            return Result.success(new PageResult(total,result));
//        }
        // ToDo: 访客信息，是否为好友
        // 如果是好友，直接返回可见日记（privacy不可以）
        String visible = null;

        // 自己，全部返回 (因为是按照分类返回的，所以不能两个表联查)
        if (userId.equals(visitorId)) {
            // 开始分页
            PageHelper.startPage(pageNum, pageSize);

            // 查询用户的日记分类
            Page<DiaryType> diaryTypesPage = fileTypeMapper.selectByUserId(userId);
            long total = diaryTypesPage.getTotal();
            List<DiaryType> diaryTypes = diaryTypesPage.getResult();

            // 准备存储结果的列表
            List<DiaryTypeVO> diaries = new ArrayList<>();

            for (DiaryType diaryType : diaryTypes) {
                DiaryTypeVO diaryTypeVO = new DiaryTypeVO();
                List<Diary> diaryList = diaryMapper.selectByType(diaryType.getId());
                diaryTypeVO.setDiaries(diaryList);
                diaryTypeVO.setDiaryType(diaryType);
                diaries.add(diaryTypeVO);
            }

            // 包装分页结果并返回
            return Result.success(new PageResult(total, diaries));
    }
        else if(1==2){
            visible="friends";
        }
        //如果不是好友，只返回public diary
          else  if(1==3){
              visible="public";
            }
            /*
            直接返回type diary(title) 查看详细内容需要再请求不然数据量大
             */
//        List<DiaryTypeVO> diaries=new ArrayList<>();
//        List<DiaryType> diaryTypes=fileTypeMapper.checkPublic(userId,visible);
//        for(DiaryTypeVO diaryTypeVO:diaries){
//        for (DiaryType diaryType : diaryTypes) {
//            List<Diary> diaryList = diaryMapper.selectByType(diaryType.getId());
//                diaryTypeVO.setDiaries(diaryList);
//                diaryTypeVO.setDiaryType(diaryType);
//        }
//        }
        // 开始分页
        PageHelper.startPage(pageNum, pageSize);

        // 查询用户的日记分类
        Page<DiaryType> diaryTypes=fileTypeMapper.checkPublic(userId,visible);
        long total = diaryTypes.getTotal();
        List<DiaryType> result = diaryTypes.getResult();

        // 准备存储结果的列表
        List<DiaryTypeVO> diaries = new ArrayList<>();

        for (DiaryType diaryType : diaryTypes) {
            DiaryTypeVO diaryTypeVO = new DiaryTypeVO();
            List<Diary> diaryList = diaryMapper.selectByType(diaryType.getId());
            diaryTypeVO.setDiaries(diaryList);
            diaryTypeVO.setDiaryType(diaryType);
            diaries.add(diaryTypeVO);
        }

        // 包装分页结果并返回
        return Result.success(new PageResult(total, diaries));
    }



    //返回某篇日记and评论
    @Override
    public Result checkDiary(Long dairyId, Long userId, Long visitorId) {
        Diary diary = diaryMapper.selectById(dairyId);
        List<Comment>  comments=commentMapper.selectByDiaryId(dairyId);
        DiaryVO diaryVO=new DiaryVO();
        BeanUtils.copyProperties(diary,diaryVO);
        diaryVO.setCommentList(comments);
        return Result.success(diaryVO);
    }


    @Override
    public Result deleteDiary(Long id) {
        //todo:验证是不是本人
        //有没有评论
        List<Comment> comments = diaryMapper.selectComment(id);
        for(Comment comment:comments){
            Long commentId = comment.getId();
            boolean isSuccess= commentMapper.delete(commentId);
            if(isSuccess==false) return Result.fail("删除失败");
        }
       boolean isSuccess= diaryMapper.deleteById(id);
        if(!isSuccess) return Result.fail("删除未成功");
        return Result.success();
    }

    @Override
    public Result deleteComment(Long id) {
        //todo:验证是不是本人
        boolean isSuccess=commentMapper.delete(id);
        if(!isSuccess) return Result.fail("评论未成功");
        return Result.success();
    }


    /**
     * 评论diary
     * @param comment
     * @return
     */
    @Override
    public Result createComment(Comment comment) {
        //todo:不是朋友不能评论
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
