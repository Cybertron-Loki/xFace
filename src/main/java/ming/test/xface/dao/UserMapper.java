package ming.test.xface.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.enity.vo.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author XiaoMing
 * @description 针对表【User(用户表)】的数据库操作Mapper
 * @createDate 2024-06-05 20:48:03
 * @Entity ming.test.xface.enity.pojo.User
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过唯一标识获取用户信息
     *
     * @param id 用户唯一标识
     * @return
     */
    UserVO getUserById(Integer id);

    /**
     * 测试获取所有用户信息
     *
     * @return
     */
    @Select("SELECT * FROM User")
    List<UserVO> allUserInfo();

    /**
     * 通过手机号获取用户信息
     * @param phonenum
     * @return
     */
    @Select("select * from user where phoneNum=#{phoneNum}")
    User getByPhone(String phonenum);

    /**
     * 保存用户信息
     * @param user
     */

    void save(User user);
}




