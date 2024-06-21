package project.test.xface.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Brand;
import project.test.xface.entity.pojo.Products;
import project.test.xface.entity.pojo.Shop;
import project.test.xface.entity.vo.BrandVO;
import project.test.xface.entity.vo.ShopVO;
import project.test.xface.mapper.BrandMapper;
import project.test.xface.mapper.ProductsMapper;
import project.test.xface.mapper.ShopMapper;
import project.test.xface.service.ShopService;
import project.test.xface.utils.RedisWorker;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static project.test.xface.common.RedisConstant.*;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private ProductsMapper productsMapper;
    @Resource
    private RedisWorker redisWorker;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result checkShops() {
        //查缓存
        List<Shop> shops=shopMapper.listShops();
        return Result.success(shops);
    }

    @Override
    public Result checkById(Long id) {
        //先查缓存
        Map<Object, Object> brandMap1 = stringRedisTemplate.opsForHash().entries(SHOPVO_KEY + id);
        ShopVO shopVO = BeanUtil.fillBeanWithMap(brandMap1, new ShopVO(), false);
        if (StringUtils.isEmpty(shopVO.toString()))
            return Result.success(shopVO);
        //查数据库
        Shop shop=shopMapper.listShopById(id);
        BeanUtils.copyProperties(shop,shopVO);
        List<Products> products = shopVO.getProducts();
        products=productsMapper.selectByShop(id);
        //     放缓存
        Map<String, Object> brandMap2 = BeanUtil.beanToMap(shopVO, new HashMap<>()
                , CopyOptions.create().setIgnoreNullValue(true)
                        .setFieldValueEditor(
                                (name, value) -> {
                                    if (value != null) {
                                        return value.toString();
                                    } else {
                                        return null; // 或者你想要的默认值
                                    }
                                }
                        ));
        //就这么存吧我懒得删了
        stringRedisTemplate.opsForHash().putAll(SHOPVO_KEY + id, brandMap2);
        stringRedisTemplate.expire(SHOPVO_KEY + id, SHOPVO_KEY_TTL, TimeUnit.DAYS);
        return Result.success(shopVO);
    }



    @Override
    public Result addShop(Shop shop) {
        if(StringUtils.isEmpty(shop.toString())) return Result.fail("不能为空");
        Long id = redisWorker.nextId("brandId");
        shop.setId(id);
        boolean isSuccess=shopMapper.addShop(shop);
        if(isSuccess==true) return Result.success();
        return Result.fail("添加失败");
    }



    @Override
    public Result deleteShop(Long id) {
        boolean isSuccess=shopMapper.deleteById(id);
        if(isSuccess==false) {return Result.fail("删除失败");}
        if(productsMapper.selectByShop(id)!=null) {
            boolean isYes = productsMapper.deleteByShop(id);
            if(isYes==false){ return Result.fail("删除失败");}
        }
        return Result.success();
    }


    @Override
    public Result updateShop(Shop shop) {
        if(StringUtils.isEmpty(shop.toString())) return Result.fail("更新失败");
        boolean isSuccess=shopMapper.updateById(shop);
        if(isSuccess==false) return Result.fail("更新失败");
        return Result.success();
    }
    //这里可以考虑存缓存，可能比较值得
    @Override
    public Result checkShopsByCategory(BlogType blogType) {
        //先查缓存
        Shop shops = shopMapper.selectByCategory(blogType);
        return Result.success(shops);
    }

    @Override
    public Result checkByName(String name) {
        List<Shop> shops=shopMapper.selectByName(name);
        return Result.success(shops);
    }


}
