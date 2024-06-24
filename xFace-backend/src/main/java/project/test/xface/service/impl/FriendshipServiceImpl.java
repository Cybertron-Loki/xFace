package project.test.xface.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import project.test.xface.common.PageResult;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Friendship;
import project.test.xface.mapper.FriendshipMapper;
import project.test.xface.service.FriendshipService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FriendshipServiceImpl implements FriendshipService {


    @Autowired
    private FriendshipMapper friendshipMapper;
    @Override
    public Result addFriend(Friendship friendship) {
        //发请求信息
        return null;
    }

    @Override
    public Result listFriends(Friendship friendship, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<Friendship> friendshipPage=friendshipMapper.selectById(friendship.getUserId());
        List<Friendship> results=friendshipPage.getResult();
        long total = friendshipPage.getTotal();
        return Result.success(new PageResult(total,results));
    }

    @Override
    public Result updateFriend(Friendship friendship) {
        if(friendship.getFriendNickName()==null){
            return Result.fail("昵称不能为空");
        }
        friendshipMapper.update(friendship);
        return Result.success();
    }

    @Override
    public Result deleteFriend(Friendship friendship) {
        //需不需要验证是否为好友关系?应该不用，应该会抛异常
        friendshipMapper.delete(friendship);
        return Result.success();
    }

    @Override
    public Result checkIfFriends(Friendship friendship) {
        boolean isFriend=friendshipMapper.checkIfFriend(friendship);
        return Result.success(isFriend);
    }
}




