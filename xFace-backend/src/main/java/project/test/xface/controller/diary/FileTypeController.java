package project.test.xface.controller.diary;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.DiaryType;
import project.test.xface.service.FileTypeService;

/**
 * 增，删，改，查 ,存入了缓存
 * todo:groupId只写了一个，没用数组表示，后续再改
 */

@RestController
@RequestMapping("/fileType")
public class FileTypeController {
    @Autowired
    private FileTypeService fileTypeService;

    /**
     * 用户自己查询自己的diaryFile
     * @return
     */
    @GetMapping("/select")
    public Result selectFileType(Long id,Integer pageNum, Integer pageSize){
        return   fileTypeService.selectType(id,pageNum,pageSize);
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
