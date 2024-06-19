package project.test.xface.controller.diary;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Comment;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.service.DiaryService;


/**
 * 个人日记,隐私范围：仅个人看/仅群里人可见/公开,增 删 查(按分类）改（隐私范围)
 */
@RestController
@RequestMapping("/diary")
public class DiaryController {
  @Autowired
  private DiaryService diaryService;
    /**
     * 新增日志
     * @param diary
     * @return
     */
    @PostMapping("/create")
    public Result createDiary(@RequestBody Diary diary){
        if(StringUtils.isEmpty(diary.toString()))  return Result.fail("不能为空");
        return diaryService.createDiary(diary);
    }

    @GetMapping("/checkDairy")
    public Result checkDiary(Integer dairyId){
        return diaryService.checkDiary(dairyId);
    }


    //todo:根据文件夹分类返回(分页)
    @GetMapping("/checkDairies")
    public Result checkDiaries(Integer userId){
        return diaryService.checkDiaries(userId);
    }

    @PostMapping("/createComment")
    public Result createComment(Comment comment) {
        if (!StringUtils.isEmpty(comment.toString()))
            return Result.fail("不能为空");
        return diaryService.createComment(comment) ;
    }

    @DeleteMapping("/deleteDiary")
    public Result deleteDiary(Integer id){
        return diaryService.deleteDiary(id);
    }




}
