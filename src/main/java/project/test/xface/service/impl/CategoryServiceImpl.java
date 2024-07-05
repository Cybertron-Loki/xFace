package project.test.xface.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.mapper.CategoryMapper;
import project.test.xface.service.CategoryService;
import project.test.xface.utils.UserHolder;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;




    @Cacheable("AllCategories")
    @Override
    public Result checkCategory() {
        //不分页也行，没多少
        BlogType blogType = categoryMapper.selectAll();
        return Result.success(blogType);
    }

    @CacheEvict("AllCategories")
    @Override
    public Result addCategory(String blogTypeName) {
        //管理员才可以新增
        String role = UserHolder.getUser().getRole();
        if (role != "SysAdmin") return Result.fail("非管理员无法新增");
        boolean isSuccess = categoryMapper.addCategory(blogTypeName);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail("新增失败");
        }
    }


    @CacheEvict("AllCategories")
    @Override
    public Result deleteCategory(Integer[] ids) {
        //管理员才可以删除
        String role = UserHolder.getUser().getRole();
        if (role != "SysAdmin") return Result.fail("非管理员无法删除");
        boolean isSuccess = categoryMapper.deleteCategories(ids);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail("新增失败");
        }
    }

    @CacheEvict("AllCategories")
    @Override
    public Result updateCategory(BlogType blogType) {
        //管理员才可以更新
        String role = UserHolder.getUser().getRole();
        if (role != "SysAdmin") return Result.fail("非管理员无法更新");
        if (blogType.getName() == "") return Result.fail("不能为空");
        boolean isSuccess = categoryMapper.updateCategory(blogType);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail("新增失败");
        }

    }
}
