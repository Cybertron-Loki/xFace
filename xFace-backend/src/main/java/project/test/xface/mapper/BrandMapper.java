package project.test.xface.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Brand;

import java.util.List;


public interface BrandMapper {

    @Select("select * from Brand ")
    List<Brand> listBrands();

    @Select("select  * from Brand where id=#{id}")
    Brand listBrandById(Long id);

    boolean addBrand(Brand brand);

    @Delete("delete from Brand where id=#{id}")
    boolean deleteById(Long id);


    boolean updateById(Brand brand);

    @Select("select * from Brand where type_id=#{blogType}")
    Brand selectByCategory(BlogType blogType);
     @Select("select * from Brand where name like concat('%',#{name},'%') ")
    List<Brand> selectByName(String name);
}




