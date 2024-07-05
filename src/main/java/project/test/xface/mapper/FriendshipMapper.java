package project.test.xface.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.Friendship;

/**
 * @author XiaoMing
 * @description 针对表【Friendship(友情状态存疑表)】的数据库操作Mapper
 * @createDate 2024-06-05 20:48:03
 * @Entity ming.test.xface.enity.pojo.Friendship
 */
public interface FriendshipMapper  {


    @Select("select * from Friendship where userId=#{userId}")
    Page<Friendship> selectById(Long userId);

    void update(Friendship friendship);


    @Delete("delete from Friendship where userId=#{userId} and friendId=#{friendId}")
    void delete(Friendship friendship);


    @Select("select * from FriendShip where userId=#{userId} and friendId=#{friendId}")
    boolean checkIfFriend(Friendship friendship);

    void addFriends(Friendship friendship);
}




