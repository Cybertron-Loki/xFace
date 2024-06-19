package project.test.xface.controller.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Products;
import project.test.xface.service.ProductsService;
import project.test.xface.service.ShopService;

@RestController
@RequestMapping("/Products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;
    /**
     *根据品牌/商家返回products
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
     * 模糊查询
     */
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
     * 删除
     */
    /**
     * 更新
     */


}
