package ming.test.xface.dao;


import ming.test.xface.enity.pojo.LogHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogHistoryMapper  {
    @Insert("INSERT INTO Log_History (userName,id,operation_type) values (#{userName},#{id},#{operationType})")
    void insertLog(LogHistory logHistory);
}
