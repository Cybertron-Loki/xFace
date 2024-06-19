package project.test.xface.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.DiaryType;

@Mapper
public interface FileTypeMapper {
    @Select("select  * from DiaryType where userId=#{id}")
    DiaryType selectByUserId(Long id);

    @Delete("delete from DiaryType where id=#{id}")
    void deleteById(Long id);
}
