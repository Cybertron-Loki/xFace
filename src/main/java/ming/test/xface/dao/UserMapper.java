package ming.test.xface.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.enity.vo.UserVO;

/**
 * @author XiaoMing
 * @description 针对表【User(用户表)】的数据库操作Mapper
 * @createDate 2024-06-05 20:48:03
 * @Entity ming.test.xface.enity.pojo.User
 */
public interface UserMapper extends BaseMapper<User> {
    UserVO getUserById(Integer id);
}




