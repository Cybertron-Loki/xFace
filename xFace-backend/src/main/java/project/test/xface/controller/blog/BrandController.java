package project.test.xface.controller.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Blog;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Brand;
import project.test.xface.service.BrandService;


/**
 * 增删改查
 */
@RestController
@RequestMapping("/Brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 查所有的牌子
     * @return
     */
    @GetMapping("/check")
    private Result checkBrands(){
        return brandService.checkBrands();
    }

    /**
     * 根据类型返回
     * @return
     */
    @GetMapping("/checkByType")
    private Result checkBrandsByCategory(BlogType blogType){
        return brandService.checkBrandsByCategory(blogType);
    }
    /**
     * 根据id查某一家,显示所有相关products
     */
    @GetMapping("/checkById/{id}")
    private Result checkBrand(@PathVariable("id") Long id){
        return brandService.checkById(id);
    }
    /**
     * 根据名字查询(模糊查询）type里面查shop and brand
     */
    @GetMapping("/checkByName")
    private Result checkByName(String name){
        if(name==null) return Result.fail("不能为空");
        else
        return brandService.checkByName(name);
    }

    /**
     * 增加(只brand）
     */
    @PostMapping("/addBrand")
    private Result addBrand(@RequestBody Brand brand){
        return brandService.addBrand(brand);
    }
    /**
     * 删除(只brand）
     */
    @DeleteMapping("/deleteBrand/{id}")
    private Result deleteBrand(@PathVariable("id") Long id){
        return brandService.deleteBrand(id);
    }
    /**
     * 改(只brand）
     */
    @PostMapping("/updateBrand")
    private Result updateBrand(@RequestBody  Brand brand)
    {
        return brandService.updateBrand(brand);
    }
}
