package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.DiaryType;

public interface FileTypeService {
    Result selectType(Long id, Integer pageNum, Integer pageSize);

    Result deleteType(Long id);

    Result addFileType(DiaryType diaryType);

    Result updateFileType(DiaryType diaryType);
}
