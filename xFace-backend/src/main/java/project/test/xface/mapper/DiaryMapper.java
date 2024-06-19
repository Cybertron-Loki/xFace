package project.test.xface.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Comment;
import project.test.xface.entity.pojo.Diary;

import java.util.List;


public interface DiaryMapper extends BaseMapper<Diary> {

    @Select("select * from Comment where  diaryId=#{id}")
    List<Comment> selectComment(Integer id);

    @Delete("delete from Diary where id=#{id}")
    boolean deleteById(Integer id);

    Result addDiary(Diary diary);


    @Select("select * from Diary where typeId=#{id}")
    Diary selectByType(Long id);
}




