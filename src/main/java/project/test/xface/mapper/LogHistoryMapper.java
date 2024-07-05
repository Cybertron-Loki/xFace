package project.test.xface.mapper;


import project.test.xface.entity.pojo.LogHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogHistoryMapper  {
    @Insert("INSERT INTO Log_History (userName,id,operation_type) values (#{userName},#{id},#{operationType})")
    void insertLog(LogHistory logHistory);
}
