package project.test.xface.service.impl;

import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Brand;
import project.test.xface.entity.pojo.Products;
import project.test.xface.entity.vo.BrandVO;
import project.test.xface.mapper.BrandMapper;
import project.test.xface.mapper.ProductsMapper;
import project.test.xface.service.BrandService;
import org.springframework.stereotype.Service;
import project.test.xface.utils.RedisUtils;
import project.test.xface.utils.RedisWorker;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static project.test.xface.common.RedisConstant.*;


@Service
public class BrandServiceImpl implements BrandService {


    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductsMapper productsMapper;
    @Resource
    private RedisWorker redisWorker;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    @Override
//    public Result checkBrands() {
//        //先查缓存(这个怎么查全部）
////        Map<Object, Object> brandMap = stringRedisTemplate.opsForHash().entries(BrandVO_KEY);
////        Brand brand = BeanUtil.fillBeanWithMap(brandMap, new Brand(), false);
//        //查数据库
//        List<Brand> brands = brandMapper.listBrands();
//        //放缓存
//        return Result.success(brands);
//    }

    @Override
    public Result checkById(Long id) {
        BrandVO brandVO = new BrandVO();
        //查数据库
        Brand brand = brandMapper.listBrandById(id);
        BeanUtils.copyProperties(brand, brandVO);
        List<Products> products = brandVO.getProducts();
        products = productsMapper.selectByBrand(id);
        return Result.success(brandVO);
    }

    @Override
    public Result addBrand(Brand brand) {
        if (StringUtils.isEmpty(brand.toString())) return Result.fail("不能为空");
        Long id = redisWorker.nextId("brandId");
        brand.setId(id);
        boolean isSuccess = brandMapper.addBrand(brand);
        if (isSuccess) {
            //加到缓存
            String setTypeKey = CACHE_BRAND_TYPE + brand.getTypeId();
            String serializedBrand = JSONUtil.toJsonStr(brand);
            stringRedisTemplate.opsForSet().add(setTypeKey,serializedBrand);
            return Result.success();
        }
        return Result.fail("添加失败");
    }

    @Transactional
    @Override
    public Result deleteBrand(Brand brand) {
        Long id=brand.getId();
        Long typeId = brand.getTypeId();
        boolean isSuccess = brandMapper.deleteById(id);
        if (!isSuccess) {
            return Result.fail("删除失败");
        }
        if (productsMapper.selectByBrand(id) != null) {
            boolean isYes = productsMapper.deleteByBrand(id);
            if (!isYes) {
                return Result.fail("删除失败");
            }
        }
          //操作缓存
        stringRedisTemplate.delete(CACHE_BRAND_TYPE+typeId);
        return Result.success();
    }

    @Override
    public Result updateBrand(Brand brand) {
        if (StringUtils.isEmpty(brand.toString())) return Result.fail("更新失败");
        boolean isSuccess = brandMapper.updateById(brand);
        if (!isSuccess) return Result.fail("更新失败");
        //清除缓存
        stringRedisTemplate.delete(CACHE_BRAND_TYPE+brand.getTypeId());
        return Result.success();
    }

    @Override
    public Result checkBrandsByCategory(BlogType blogType) {
        //solve 缓存穿透
        List<Brand> brands = redisUtils.queryWithPassThroughSet(CACHE_BRAND_TYPE, blogType.getId(), Brand.class,
                id-> brandMapper.selectByCategory(blogType.getId()), CACHE_SHOP_TTL, TimeUnit.DAYS);
        return Result.success(brands);
    }

    @Override
    public Result checkByName(String name) {
        List<Brand> brands=brandMapper.selectByName(name);
        return Result.success(brands);
    }
}




