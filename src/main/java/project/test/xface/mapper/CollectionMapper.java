package project.test.xface.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.Blog;
import project.test.xface.entity.pojo.Collection;

import java.util.List;

@Mapper
public interface CollectionMapper {

    void add(Collection collection);


    @Select("select count (*) from  Collection where userId=#{userId}")
    Integer count(Long userId);

    @Select("select * from Collection where userId=#{userId}")
    List<Collection> checkByUserId(Long userId);


    @Select("select * from Collection_Reflection where userId=#{userId} and collectionId=#{colletcionId}")
    List<Blog> selectBlogs(Collection collection);


    void deleteByIds(Long[] ids, Long userId);

    void deleteReflection(Long[] ids,Long userId);

    void modify(Collection collection);
}
