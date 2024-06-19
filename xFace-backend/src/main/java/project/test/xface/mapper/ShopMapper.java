package project.test.xface.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Shop;

import java.util.List;

@Mapper
public interface ShopMapper {
    @Select("select * from Shop ")
    List<Shop> listShops();

    @Select("select  * from Shop where id=#{id}")
    Shop listShopById(Long id);

    boolean addShop(Shop shop);

    @Delete("delete from Shop where id=#{id}")
    boolean deleteById(Long id);

    boolean updateById(Shop shop);

    @Select("select * from Shop where type_id=#{blogType}")
    Shop selectByCategory(BlogType blogType);
    @Select("select * from Shop where name like concat('%',#{name},'%') ")
    List<Shop> selectByName(String name);
}
