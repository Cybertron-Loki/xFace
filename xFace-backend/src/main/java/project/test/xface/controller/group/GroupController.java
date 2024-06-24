package project.test.xface.controller.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.GroupDTO;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Group;
import project.test.xface.entity.pojo.Groupmember;
import project.test.xface.service.GroupService;

/**
 * 增 删 改 查
 * todo：聊天and秒杀
 */
@RestController
@RequestMapping("/group")
public class GroupController {


    @Autowired
    private GroupService groupService;

    /**
     * 创建群（默认设置群主）
     */

    @PostMapping("/group")
    public Result createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }
/**
 * todo:加入群,申请，验证
 */
//@PostMapping("/joinGroup")
//    public Result joinGroup(@RequestBody GroupApplicationDTO groupApplicationDTO){
//
//}

    /**
     * 群主删群
     */
    @DeleteMapping("/group")
    public Result deleteGroup(@RequestBody GroupDTO groupDTO) {
        return groupService.deleteGroup(groupDTO);
    }
/**
 * todo:拉人进群,发邀请，同意再进
 */
//@PostMapping("/joinGroup")
//    public Result joinGroup(@RequestBody GroupApplicationDTO groupApplicationDTO){
//
//}

    /**
     * 成员退群
     */
    @DeleteMapping("/groupMember")
    public Result exitGroup(@RequestBody GroupDTO groupDTO) {
        return groupService.exitGroup(groupDTO);
    }

    /**
     * 改群主
     */
    @PutMapping("/creator")
    public Result updateHeader(@RequestBody GroupDTO groupDTO) {
        return groupService.updateHeader(groupDTO);
    }

    /**
     * 改群名,类型,群头像
     */
    @PutMapping("/group")
    public Result update(@RequestBody GroupDTO groupDTO) {
        return groupService.update(groupDTO);
    }


    /**
     * 改状态(管理员）
     */
    @PutMapping("/status")
    public Result updateStatus(@RequestBody Group group) {
        return groupService.updateStatus(group);
    }




    /**
     * 根据userId显示群列表,分页,todo:可以存缓存
     **/
    @GetMapping("/groups")
    public Result listGroups(Long userId, Integer pageNum, Integer pageSize) {
        return groupService.listGroups(userId, pageNum, pageSize);
    }

    /**
     * 查看群信息&&成员列表,群内成员才能查看(?需要判断吗—）
     */
    @GetMapping("/groupMembers/{id}")
    public Result groupMembers(@PathVariable("id") Long id, Integer pageNum, Integer pageSize) {
        return groupService.groupMembers(id, pageNum, pageSize);
    }

    /**
     * 群主踢人
     */
    @DeleteMapping("/groupMember")
    public Result kickMember(@RequestBody GroupDTO groupDTO) {
        return groupService.kickMember(groupDTO);
    }


    /**
     * 根据群名搜
     */
    @GetMapping("/groupName")
    public Result selectByName(@PathVariable("groupName") String name, Integer pageNum, Integer pageSize) {
        return groupService.selectByName(name, pageNum, pageSize);
    }

    /**
     * 修改个人群内昵称
     */
    @PutMapping("/nickName")
    public Result updateNickName(@RequestBody Groupmember groupmember) {
        return groupService.updateNickName(groupmember);
    }
}
