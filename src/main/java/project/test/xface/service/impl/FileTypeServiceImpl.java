package project.test.xface.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import project.test.xface.common.PageResult;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.entity.pojo.DiaryType;
import project.test.xface.mapper.DiaryMapper;
import project.test.xface.mapper.FileTypeMapper;
import project.test.xface.service.FileTypeService;
import project.test.xface.utils.RedisWorker;

import javax.annotation.Resource;
import java.util.List;

import static project.test.xface.common.RedisConstant.DiaryType_Key_ID;

@Service
public class FileTypeServiceImpl implements FileTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FileTypeMapper fileTypeMapper;
    @Autowired
    private DiaryMapper diaryMapper;
    @Resource
    private RedisWorker redisWorker;

    @Cacheable(cacheNames="fileType",key="#id")
    @Override
    public Result selectType(Long id, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
       Page<DiaryType> diaryType= fileTypeMapper.selectByUserId(id);
       List<DiaryType> results=diaryType.getResult();
       long total=diaryType.getTotal();
       if(StringUtils.isEmpty(diaryType.toString())) return Result.success(null);
        return Result.success(new PageResult(total,results));
    }


    @CacheEvict(cacheNames = "fileType",key="#id")
    @Override
    public Result deleteType(Long id) {
        //查看文件夹下是否有日记，有则不能删除
        List<Diary> diaries=diaryMapper.selectByType(id);
        if(!StringUtils.isEmpty(diaries.toString())) Result.fail("有日记不能删");
        boolean isSuccess=fileTypeMapper.deleteById(id);
        if(isSuccess) return Result.success();
        else return Result.fail("删除失败");
    }
    @CacheEvict(cacheNames = "fileType",key="#id")
    @Override
    public Result addFileType(DiaryType diaryType) {
        Long id = redisWorker.nextId(DiaryType_Key_ID);
        diaryType.setId(id);
        boolean isSuccess=fileTypeMapper.addType(diaryType);
        if(isSuccess) return Result.success();
        else return Result.fail("新增失败");
    }
    @CacheEvict(cacheNames = "fileType",key="#id")
    @Override
    public Result updateFileType(DiaryType diaryType) {
        boolean isSuccess=fileTypeMapper.updateFile(diaryType);
        if(isSuccess) return Result.success();
        else return Result.fail("更新失败");
    }

}
