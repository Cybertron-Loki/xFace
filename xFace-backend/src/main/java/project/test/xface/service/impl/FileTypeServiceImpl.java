package project.test.xface.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.entity.pojo.DiaryType;
import project.test.xface.mapper.DiaryMapper;
import project.test.xface.mapper.FileTypeMapper;
import project.test.xface.service.FileTypeService;
import project.test.xface.utils.UserHolder;

import javax.annotation.Resource;

@Service
public class FileTypeServiceImpl implements FileTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FileTypeMapper fileTypeMapper;
    @Autowired
    private DiaryMapper diaryMapper;
    @Override
    public Result selectType() {
        UserDTO user = UserHolder.getUser();
        Long id = user.getId();
       DiaryType diaryType= fileTypeMapper.selectByUserId(id);
       if(StringUtils.isEmpty(diaryType.toString())) return Result.success(null);
        return Result.success(diaryType);
    }

    @Override
    public Result deleteType(Long id) {
        //查看文件夹下是否有日记，有则不能删除
        Diary diary=diaryMapper.selectByType(id);
        if(!StringUtils.isEmpty(diary.toString())) Result.fail("有日记不能删");
        fileTypeMapper.deleteById(id);
        return null;
    }

    @Override
    public Result addFileType(DiaryType diaryType) {
        return null;
    }

    @Override
    public Result updateFileType(DiaryType diaryType) {
        return null;
    }

}
