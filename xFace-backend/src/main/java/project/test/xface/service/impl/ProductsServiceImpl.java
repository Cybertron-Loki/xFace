package project.test.xface.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Products;
import project.test.xface.entity.vo.BlogVO;
import project.test.xface.mapper.ProductsMapper;
import project.test.xface.service.ProductsService;
import project.test.xface.utils.RedisWorker;

import javax.annotation.Resource;
import java.util.List;

import static project.test.xface.common.RedisConstant.BrandVO_KEY;
import static project.test.xface.common.RedisConstant.SHOPVO_KEY;

@Service
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private ProductsMapper productsMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisWorker redisWorker;
    @Override
    public Result selectByBrand(Long id) {
        //分页
        //查缓存
        List<Products> products = productsMapper.selectByBrand(id);
        return Result.success(products);
    }

    @Override
    public Result selectByShop(Long id) {
        //分页
        //查缓存
        List<Products> products = productsMapper.selectByShop(id);
        return Result.success(products);
    }

    @Override
    public Result addProducts(Products products) {
        Long shopId = products.getShopId();
        Long brandId = products.getBrandId();
        if(shopId.equals(null)&&products.getBrandId().equals(null))
            return Result.fail("必须有商铺");
        Long id = redisWorker.nextId("products");
        products.setId(id);
        boolean isSuccess=productsMapper.addProducts(products);

        //清除相关缓存

        if(shopId.equals(null)) {
            stringRedisTemplate.delete(BrandVO_KEY + brandId);
        }  else {
            stringRedisTemplate.delete(SHOPVO_KEY +shopId);
        }
        if(isSuccess)
        return Result.success();
        else return Result.fail("新增失败");
    }

    @Override
    public Result selectByName(String name) {
        List<Products> products=productsMapper.selectByName(name);
        return Result.success(products);
    }

    @Override
    public Result selectBlogs(Long id, String[] ranges) {
        //没选择某个群就是全部看
        if(ranges==null) {
          List<BlogVO> blogs=  productsMapper.selectBlogsAll(id);
           return Result.success(blogs);
           }
        //根据群范围返回某些群内人的blog
        List<BlogVO> blogs=productsMapper.selectBlogs(id,ranges);
        return Result.success(blogs);
    }

    @Override
    public Result deleteProducts(Long id, Long shopId, Long brandId) {
        boolean isSuccess=productsMapper.deleteProducts(id);
        if(isSuccess) {
         if(brandId!=null)
         {
             stringRedisTemplate.delete(BrandVO_KEY + brandId);
         }
         else{
             stringRedisTemplate.delete(SHOPVO_KEY +shopId);
         }
            return Result.success();
        }
        return Result.fail("删除失败");
    }

    @Override
    public Result updateProducts(Products products) {
        Long brandId = products.getBrandId();
        Long shopId = products.getShopId();
        boolean isSuccess= productsMapper.updateProducts(products);
        if(isSuccess) {
            if(brandId!=null){
                stringRedisTemplate.delete(BrandVO_KEY + brandId);
            }
            else{
                stringRedisTemplate.delete(SHOPVO_KEY +shopId);
            }
            return Result.success();
        }
        else{
            return Result.fail("更新失败");
        }
    }

}
