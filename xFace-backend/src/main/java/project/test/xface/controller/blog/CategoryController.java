package project.test.xface.controller.blog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.service.CategoryService;

/**
 * blog分类，增（管理员），删（管理员批量删），改（管理员），查
 * 可以放缓存(改的不太频繁，可以放）
 * 这个类写差不多了可以不用看了
 */
@RestController
@RequestMapping("/Category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查
     * @return
     */
    @GetMapping("/checkCategory")
    private Result checkCategory(){
        return categoryService.checkCategory();
    }

    /**
     * 增
     * @param blogTypeName
     * @return
     */
    @GetMapping("/addCategory/{name}")
    private Result addCategory(@PathVariable("name") String blogTypeName){
        return categoryService.addCategory(blogTypeName);
    }
    /**
     * 删(批删除)
     */
    @DeleteMapping("/deleteCategory")
    private Result deleteCategory(Integer[] ids){
        return categoryService.deleteCategory(ids);
    }
    /**
     * 改
     */
    @PutMapping("/updateCategory")
    private Result updateCategory(BlogType blogType){
        return categoryService.updateCategory(blogType);
    }

}
