package project.test.xface.controller.diary;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.DiaryType;
import project.test.xface.service.FileTypeService;

/**
 * 增，删，改，查
 */

@RestController
@RequestMapping("/fileType")
public class FileTypeController {
    @Autowired
    private FileTypeService fileTypeService;

    @GetMapping("/select")
    public Result selectFileType(){
        return   fileTypeService.selectType();
    }

    @DeleteMapping("/{id}")
    public Result deleteFileType(@PathVariable("id") Long id){
        return   fileTypeService.deleteType(id);
    }

    @PostMapping("/create")
    public Result addFileType(@RequestBody DiaryType diaryType){
        if(StringUtils.isEmpty(diaryType.toString())) return Result.fail("不能为空");
        return fileTypeService.addFileType(diaryType);
    }
    @PostMapping("/modify")
    public Result updateFileType(@RequestBody DiaryType diaryType){
        if(StringUtils.isEmpty(diaryType.toString())) return Result.fail("不能为空");
        return fileTypeService.updateFileType(diaryType);
    }
}
