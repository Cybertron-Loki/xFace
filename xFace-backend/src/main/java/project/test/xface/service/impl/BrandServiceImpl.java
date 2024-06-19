package project.test.xface.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Brand;
import project.test.xface.entity.pojo.Products;
import project.test.xface.entity.vo.BrandVO;
import project.test.xface.entity.vo.UserVO;
import project.test.xface.mapper.BrandMapper;
import project.test.xface.mapper.ProductsMapper;
import project.test.xface.service.BrandService;
import org.springframework.stereotype.Service;
import project.test.xface.utils.RedisWorker;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result checkBrands() {
        //todo:先查缓存(这个怎么查全部）
//        Map<Object, Object> brandMap = stringRedisTemplate.opsForHash().entries(BrandVO_KEY);
//        Brand brand = BeanUtil.fillBeanWithMap(brandMap, new Brand(), false);
        //查数据库
        List<Brand> brands = brandMapper.listBrands();
        //放缓存
        return Result.success(brands);
    }

    @Override
    public Result checkById(Long id) {
        //先查缓存
        BrandVO brandVO = new BrandVO();
        Map<Object, Object> brandMap1 = stringRedisTemplate.opsForHash().entries(BrandVO_KEY + id);
        brandVO = BeanUtil.fillBeanWithMap(brandMap1, new BrandVO(), false);
        if (StringUtils.isEmpty(brandVO.toString()))
            return Result.success(brandVO);
        //查数据库
        Brand brand = brandMapper.listBrandById(id);
        BeanUtils.copyProperties(brand, brandVO);
        List<Products> products = brandVO.getProducts();
        products = productsMapper.selectByBrand(id);
        //        放缓存
        Map<String, Object> brandMap2 = BeanUtil.beanToMap(brandVO, new HashMap<>()
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
        stringRedisTemplate.opsForHash().putAll(BrandVO_KEY + id, brandMap2);
        stringRedisTemplate.expire(Brand_KEY + id, BrandVO_KEY_TTL, TimeUnit.DAYS);
        return Result.success(brandVO);
    }

    @Override
    public Result addBrand(Brand brand) {
        if (StringUtils.isEmpty(brand.toString())) return Result.fail("不能为空");
        Long id = redisWorker.nextId("brandId");
        brand.setId(id);
        boolean isSuccess = brandMapper.addBrand(brand);
        if (isSuccess == true) {
            //加到缓存  todo:暂时没考虑products一不一起加不加  no
            return Result.success();
        }
        return Result.fail("添加失败");
    }

    @Transactional
    @Override
    public Result deleteBrand(Long id) {
        boolean isSuccess = brandMapper.deleteById(id);
        if (isSuccess == false) {
            return Result.fail("删除失败");
        }
        if (productsMapper.selectByBrand(id) != null) {
            boolean isYes = productsMapper.deleteByBrand(id);
            if (isYes == false) {
                return Result.fail("删除失败");
            }
        }
        //清除缓存
//        stringRedisTemplate.delete(Brand_KEY+id);
        return Result.success();
    }

    @Override
    public Result updateBrand(Brand brand) {
        if (StringUtils.isEmpty(brand.toString())) return Result.fail("更新失败");
        boolean isSuccess = brandMapper.updateById(brand);
        if (isSuccess == false) return Result.fail("更新失败");
        //清除缓存
//        stringRedisTemplate.delete(Brand_KEY+brand.getId());
        return Result.success();
    }

    @Override
    public Result checkBrandsByCategory(BlogType blogType) {
        //先查缓存
        Brand brand = brandMapper.selectByCategory(blogType);
        return Result.success(brand);
    }

    @Override
    public Result checkByName(String name) {
        List<Brand> brands=brandMapper.selectByName(name);
        return Result.success(brands);
    }
}




