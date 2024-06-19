package project.test.xface.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.mapper.CategoryMapper;
import project.test.xface.service.CategoryService;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public Result checkCategory() {
       BlogType blogType=categoryMapper.selectAll();
        return Result.success(blogType);
    }
}
