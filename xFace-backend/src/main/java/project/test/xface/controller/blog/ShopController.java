package project.test.xface.controller.blog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Shop;
import project.test.xface.service.ShopService;

/**
 * 增删改查
 */
@RestController
@RequestMapping("/Shop")
public class ShopController {

     @Autowired
     private ShopService shopService;

    /**
     * 查所有的
     * @return
     */
     @GetMapping("/check")
    private Result checkShops(){
          return shopService.checkShops();
     }
    /**
     * 根据id查某一家,显示所有相关products
     */
    @GetMapping("/checkById/{id}")
    private Result checkShop(@PathVariable("id") Long id){
            return shopService.checkById(id);
    }
    /**
     * 根据名字查询(模糊查询）type里面查shop and brand
     */
    @GetMapping("/checkByName")
    private Result checkByName(String name){
        if(name==null) return Result.fail("不能为空");
        else
            return shopService.checkByName(name);
    }

    /**
     * 增加
     */
    @PostMapping("/addShop")
    private Result addShop(@RequestBody  Shop shop){
        return shopService.addShop(shop);
    }
    /**
     * 删除
     */
    @DeleteMapping("/deleteShop/{id}")
    private Result deleteShop(@PathVariable("id") Long id){
        return shopService.deleteShop(id);
    }
    /**
     * 改
     */
    @PostMapping("/updateShop")
    private Result updateShop(@RequestBody  Shop shop)
    {
        return shopService.updateShop(shop);
    }
    /**
     * 根据类型返回
     * @return
     */
    @GetMapping("/checkByType")
    private Result checkShopsByCategory(BlogType blogType){
        return shopService.checkShopsByCategory(blogType);
    }

}
