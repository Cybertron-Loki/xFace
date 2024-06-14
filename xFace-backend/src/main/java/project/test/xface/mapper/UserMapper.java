package project.test.xface.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import project.test.xface.annotation.AutoFill;
import project.test.xface.entity.pojo.Follow;
import project.test.xface.entity.pojo.User;
import project.test.xface.entity.pojo.UserInfo;
import project.test.xface.entity.vo.UserVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author XiaoMing
 * @description 针对表【User(用户表)】的数据库操作Mapper
 * @createDate 2024-06-05 20:48:03
 * @Entity ming.test.xface.enity.pojo.User
 */
@Mapper
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

    Integer save(User user);


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

    void createProfile(Integer id,String userName);


    Boolean follow(Follow follow);


    @Delete("delete from  Follow where userId=#{id} and userFollowId=#{followerId}")
    boolean unFollow(Integer id, Integer followerId);


    @Select("SELECT COUNT(*) FROM Follow WHERE userFollowId = #{id} AND userId = #{followerId}")
    Long selectFollow(Integer id, Integer followerId);

    @Select("select  userId from Follow where userFollowId=#{userId}")
    List<Integer> selectFollowers(Integer userId);
}




