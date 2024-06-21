package project.test.xface.controller.diary;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Comment;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.service.DiaryService;


/**
 * 个人日记,隐私范围：仅个人看/仅好友可见()/公开,增 删 查(按分类）改（隐私范围)
 */
@RestController
@RequestMapping("/diary")
public class DiaryController {
  @Autowired
  private DiaryService diaryService;
    /**
     * 新增日志,只能自己修改自己的
     * @param diary
     * @return
     */
    @PostMapping("/create")
    public Result createDiary(@RequestBody Diary diary){
        if(StringUtils.isEmpty(diary.toString()))  return Result.fail("不能为空");
        return diaryService.createDiary(diary);
    }
     //TODO:(分页)

    /**
     * 自己查看日记直接返回，陌生人查看只返回public,好友返回可见范围内的
     * @param dairyId
     * @param userId
     * @param visitorId
     * @return
     */
    @GetMapping("/checkDairy")
    public Result checkDiary(Long dairyId,Long userId,Long visitorId){
        return diaryService.checkDiary(dairyId,userId,visitorId);
    }



    /**
     * 自己查看日记全部返回，陌生人查看只返回public,好友返回可见范围内的
     * @param userId
     * @param visitorId
     * @return
     */
    @GetMapping("/checkDairies")
    public Result checkDiaries(Long userId,Long visitorId){

        return diaryService.checkDiaries(userId,visitorId);
    }

    /**
     * 评论日记
     * @param comment
     * @return
     */
    @PostMapping("/createComment")
    public Result createComment(Comment comment) {
        if (!StringUtils.isEmpty(comment.toString()))
            return Result.fail("不能为空");
        return diaryService.createComment(comment) ;
    }

    /**
     * 只能自己删自己的
     * @param id
     * @return
     */
    @DeleteMapping("/deleteDiary")
    public Result deleteDiary(Long id){
        return diaryService.deleteDiary(id);
    }

    /**
     * 删除blog评论,只能自己删自己的
     */
    @DeleteMapping("/deleteComment")
    public Result deleteComment(Long id){
        return diaryService.deleteComment(id);
    }


}
