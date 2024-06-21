package project.test.xface.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.Blog;
import project.test.xface.entity.pojo.Products;
import project.test.xface.entity.vo.BlogVO;

import java.util.List;

@Mapper
public interface ProductsMapper {
    @Select("select * from Products where brandId=#{id}")
    List<Products> selectByBrand(Long id);

    @Delete("delete from Products where brandId=#{id}")
    boolean deleteByBrand(Long id);
    @Select("select * from Products where shopId=#{id}")
    List<Products> selectByShop(Long id);

    @Delete("delete from Products where shopId=#{id}")
    boolean deleteByShop(Long id);

    boolean addProducts(Products products);


    @Select("select * from Products where name like concat('%',{#name},'%')")
    List<Products> selectByName(String name);


    List<BlogVO> selectBlogs(Long id, String[] ranges);

    List<BlogVO> selectBlogsAll(Long id);

    boolean deleteProducts(Long id);

    boolean updateProducts(Products products);
}
