package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Friendship;

/**
 * 
 * @description 针对表【Friendship(友情状态存疑表)】的数据库操作Service
 * @createDate 2024-06-05 20:48:03
 */
public interface FriendshipService {

    Result addFriend(Friendship friendship);

    Result listFriends(Friendship friendship, Integer pageNum, Integer pageSize);

    Result updateFriend(Friendship friendship);

    Result deleteFriend(Friendship friendship);

    Result checkIfFriends(Friendship friendship);
}
