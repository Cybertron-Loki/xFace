package project.test.xface.controller.group;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Friendship;
import project.test.xface.service.FriendshipService;

/**
 * 增 删 改 查
 */
@RequestMapping("/friend")
@RestController
public class FriendController {
    @Autowired
    private FriendshipService friendshipService;

    /**
     * 新增好友
     */
    @PostMapping("/friend")
    public Result addFriendRequest(@RequestBody Friendship friendship){
        return friendshipService.addFriend(friendship);
    }
//    /**
//     * todo:新增好友
//     */
//    @PostMapping("/friend")
//    public Result addFriendResponse(@RequestBody Friendship friendship){
//        return friendshipService.addFriend(friendship);
//    }
    /**
     * 删除好友
     */
    @DeleteMapping("/friend")
    public Result deleteFriend(@RequestBody Friendship friendship){
        return friendshipService.deleteFriend(friendship);
    }
    /**
     * 改备注昵称
     */
    @PutMapping("/friend")
    public Result updateFriend(@RequestBody Friendship friendship){
        return friendshipService.updateFriend(friendship);
    }
    /**
     * 查看好友列表.分页
     */
    @GetMapping("/friends")
    public Result listFriends(@RequestBody Friendship friendship,Integer pageNum, Integer pageSize){
        return friendshipService.listFriends(friendship,pageNum,pageSize);
    }
    /**
     * 判断是否是好友
     */
    @GetMapping("/ifFriends")
    public Result checkIfFriend(@RequestBody Friendship friendship){
        return friendshipService.checkIfFriends(friendship);
    }


}
