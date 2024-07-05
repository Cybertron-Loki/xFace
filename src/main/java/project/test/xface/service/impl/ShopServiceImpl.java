package project.test.xface.service.impl;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Products;
import project.test.xface.entity.pojo.Shop;
import project.test.xface.entity.vo.ShopVO;
import project.test.xface.mapper.ProductsMapper;
import project.test.xface.mapper.ShopMapper;
import project.test.xface.service.ShopService;
import project.test.xface.utils.RedisUtils;
import project.test.xface.utils.RedisWorker;


import javax.annotation.Resource;
import java.util.*;
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
    private RedisUtils redisUtils;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
//    @Override
//    public Result checkShops() {
//
//        //查type ids 再查shops
//        List<Shop> shops=shopMapper.listShops();
//        return Result.success(shops);
//    }

    @Override
    public Result checkById(Long id) {
        //查数据库
        Shop shop=shopMapper.listShopById(id);
        ShopVO shopVO =new ShopVO();
        BeanUtils.copyProperties(shop,shopVO);
        List<Products> products = shopVO.getProducts();
        products=productsMapper.selectByShop(id);
        return Result.success(shopVO);
    }



    @Override
    public Result addShop(Shop shop) {
        if(shop==null) return Result.fail("不能为空");
        Long id = redisWorker.nextId("brandId");
        shop.setId(id);
        boolean isSuccess=shopMapper.addShop(shop);
        if(!isSuccess) return Result.fail("添加失败");
        /**
         * 缓存
         */
        // Add serialized shop to set
        // 根据类型放到redis里
        String setTypeKey = CACHE_SHOPS_TYPE + shop.getTypeId();
        String serializedShop = JSONUtil.toJsonStr(shop);
        stringRedisTemplate.opsForSet().add(setTypeKey, serializedShop);

        //存储到GEO
        String key = SHOP_GEO_KEY + shop.getTypeId();
        RedisGeoCommands.GeoLocation<String> stringGeoLocation =
                new RedisGeoCommands.GeoLocation<>(shop.getId().toString(), new Point(shop.getX(), shop.getY()));
        stringRedisTemplate.opsForGeo().add(key,stringGeoLocation);

        return Result.success();

    }



    @Override
    public Result deleteShop(Shop shop) {
        Long id=shop.getId();
        Long typeId=shop.getTypeId();
        boolean isSuccess=shopMapper.deleteById(id);
        if(!isSuccess) {return Result.fail("删除失败");}
        if(productsMapper.selectByShop(id)!=null) {
            boolean isYes = productsMapper.deleteByShop(id);
            if(!isYes){ return Result.fail("删除失败");}
        }
        //操作缓存
        stringRedisTemplate.delete(CACHE_SHOPS_TYPE+typeId);
        stringRedisTemplate.delete(SHOP_GEO_KEY+typeId);

        return Result.success();
    }


    @Override
    public Result updateShop(Shop shop) {
        if(shop==null) return Result.fail("更新失败");
        Long typeId=shop.getTypeId();
        boolean isSuccess=shopMapper.updateById(shop);
        if(!isSuccess) return Result.fail("更新失败");
        //操作缓存
        stringRedisTemplate.delete(CACHE_SHOPS_TYPE+typeId);
        stringRedisTemplate.delete(SHOP_GEO_KEY+typeId);
        return Result.success();
    }

    @Override
    public Result checkShopsByCategory(Integer blogTypeId, Integer current, Double x, Double y) {
        //是否需要坐标查询
        //普通查询（下拉框）,不分页
        if(x==null||y==null) {
            List<Shop> shops = redisUtils.queryWithPassThroughSet(CACHE_SHOPS_TYPE, blogTypeId, Shop.class,
                    id -> shopMapper.selectByCategory(blogTypeId), CACHE_SHOP_TTL, TimeUnit.DAYS);
            return Result.success(shops);
            }
        //计算分页参数
        int from = (current - 1) * MAX_PAGE_SIZE;
        int end = current * MAX_PAGE_SIZE;
        //查询redis 距离排序 分页
        String key= SHOP_GEO_KEY+ blogTypeId;
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo()
                .search(key
                        , GeoReference.fromCoordinate(x, y)
                        , new Distance(5000)
                        , RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(end)
                );
        //解析出id
        //如果为空，有可能是redis更新删除了，这样需要重新写入一次redis根据type
        if (results==null){
            //查询店铺信息
            List<Shop> list =shopMapper.selectByCategory(blogTypeId);
            //写入
            List<RedisGeoCommands.GeoLocation<String>> locations=new ArrayList<>(list.size());
                //写入redis
                for (Shop shop : list) {
                    locations.add(new RedisGeoCommands.GeoLocation<>(shop.getId().toString(),new Point(shop.getX(),shop.getY())));
                }
                stringRedisTemplate.opsForGeo().add(key,locations);
                return Result.fail("请重新尝试");
            }

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = results.getContent();
        if (content.size()<from){
            //没有下一页
            return Result.success();
        }
        //截取
        List<Long> ids=new ArrayList<>(content.size());  //16
        Map<String,Distance> distanceMap=new HashMap<>();
        content.stream().skip(from).forEach(result->{
            //店铺id
            String shopId = result.getContent().getName();
            ids.add(Long.valueOf(shopId));
            //距离
            Distance distance = result.getDistance();
            distanceMap.put(shopId,distance);
        });
        //根据id查询shop
        List<Shop> shopList = shopMapper.selectBatch(ids);
        for (Shop shop : shopList) {
            shop.setDistance(distanceMap.get(shop.getId().toString()).getValue());
        }
        return Result.success(shopList);
    }

    @Override
    public Result checkByName(String name) {
        List<Shop> shops=shopMapper.selectByName(name);
        return Result.success(shops);
    }
}


