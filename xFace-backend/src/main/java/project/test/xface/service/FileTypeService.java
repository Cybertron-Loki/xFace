package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.DiaryType;

public interface FileTypeService {
    Result selectType();

    Result deleteType(Long id);

    Result addFileType(DiaryType diaryType);

    Result updateFileType(DiaryType diaryType);
}
