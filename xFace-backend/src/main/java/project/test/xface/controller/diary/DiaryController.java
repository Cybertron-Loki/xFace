package project.test.xface.controller.diary;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.service.DairyService;


/**
 * 个人日记
 */
@RestController
@RequestMapping("/diary")
public class DiaryController {
  @Autowired
  private DairyService dairyService;
    /**
     * 新增日志
     * @param diary
     * @return
     */
    @PostMapping("/create")
    public Result createDiary(@RequestBody Diary diary){
        if(StringUtils.isEmpty(diary.toString()))  return Result.fail("不能为空");
        return dairyService.createDiary(diary);
    }

    @GetMapping("/checkDairy")
    public Result checkDairy(Integer dairyId){
        return dairyService.checkDairy(dairyId);
    }

    @GetMapping("/checkDairies")
    public Result checkDairies(Integer userId){
        return dairyService.checkDairies(userId);
    }
}
