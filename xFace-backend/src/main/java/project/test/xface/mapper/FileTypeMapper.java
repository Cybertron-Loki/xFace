package project.test.xface.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.DiaryType;
import project.test.xface.entity.vo.DiaryTypeVO;

import java.util.List;

@Mapper
public interface FileTypeMapper {
    @Select("select  * from DiaryType where userId=#{id}")
    Page<DiaryType> selectByUserId(Long id);

    @Delete("delete from DiaryType where id=#{id}")
    boolean deleteById(Long id);

    boolean addType(DiaryType diaryType);

    boolean updateFile(DiaryType diaryType);

    Page<DiaryType> checkPublic(Long userId, String visible);


//    List<Long> checkIfVisible(Long userId);
}
