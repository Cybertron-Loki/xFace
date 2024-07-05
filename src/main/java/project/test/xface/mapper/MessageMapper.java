package project.test.xface.mapper;

import project.test.xface.entity.pojo.Message;

/**
 * @author XiaoMing
 * @description 针对表【Message(消息表)】的数据库操作Mapper
 * @createDate 2024-06-05 20:48:03
 * @Entity ming.test.xface.enity.pojo.Message
 */
public interface MessageMapper {

    void save(Message message);
}




