package project.test.xface.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Products;
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
        List<Products> products = productsMapper.selectByBrand(id);
        return Result.success(products);
    }

    @Override
    public Result selectByShop(Long id) {
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

}
