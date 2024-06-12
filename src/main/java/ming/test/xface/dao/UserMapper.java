package ming.test.xface.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import ming.test.xface.annotation.AutoFill;
import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.dto.UserDTO;
import ming.test.xface.enity.pojo.User;
import ming.test.xface.enity.pojo.UserInfo;
import ming.test.xface.enity.vo.UserVO;
import org.apache.ibatis.annotations.Select;

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
    Page<UserVO> allUserInfo();

    /**
     * 通过手机号获取用户信息
     * @param phonenum
     * @return
     */
    @Select("select * from User where phoneNum=#{phoneNum}")
    User getByPhone(String phonenum);

    /**
     * 保存用户信息
     * @param user
     */

    void save(User user);


    @AutoFill("updateUser")
    void updateUser(User user);

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @Select("select * from UserInfo where userId=#{id}")
    UserInfo getUserInfo(Integer id);

    void updateMyself(UserInfo userInfo);
}




