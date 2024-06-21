package project.test.xface.controller.blog;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Products;
import project.test.xface.service.ProductsService;
import project.test.xface.service.ShopService;

/**
 * 与shop/brand关联的products
 * 增 ，删(管理员），改(管理员） ，查(根据shop/brand）
 * 增删批量没想好怎么加，有些麻烦，比较没必要
 * 判断brandId或者shopId比较冗余，想一下怎么减轻负担
 */
@RestController
@RequestMapping("/Products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;
    /**
     *根据品牌/商家返回products，先查缓存
     */
    @GetMapping("/selectByBrand")
    private Result selectByBrand(Long id){
        return productsService.selectByBrand(id);
    }
    @GetMapping("/selectByShop")
    private Result selectByShop(Long id){
        return productsService.selectByShop(id);
    }
    /**
     * 模糊查询（根据名字）
     */
    @GetMapping("/selectByName/{name}")
    private Result selectByName(@PathVariable("name") String name){
        return productsService.selectByName(name);
    }
    /**
     * 根据id查询相关的blog(\点击），只看群/全看(range)
     */
    @GetMapping("/selectBlog")
    private Result selectBlogs(Long id,String[] ranges)
    {
        return productsService.selectBlogs(id,ranges);
    }
    /**
     * 新增产品，注意清除缓存
     * @param products
     * @return
     */
    @PostMapping("/addProducts")
    private Result addProducts(Products products){
        return productsService.addProducts(products);
    }
    /**
     * 删除   (批）--->不行，太麻烦，数据库压力增大
     * 根据shopid或者brandid,注意缓存删除
     */
    @DeleteMapping("/deleteProducts")
    private Result deleteProducts(Long id,Long shopId,Long brandId){
        return productsService.deleteProducts(id,shopId,brandId);
    }
    /**
     * 更新，注意缓存删除
     */
    @PutMapping("/updateProducts")
    private Result updateProducts(Products products){
        return productsService.updateProducts(products);
    }


}
