package project.test.xface.service;

import project.test.xface.entity.dto.GroupDTO;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Group;
import project.test.xface.entity.pojo.Groupmember;

/**
 * @author XiaoMing
 * @description 针对表【Group(群聊信息表)】的数据库操作Service
 * @createDate 2024-06-05 20:48:03
 */
public interface GroupService {

    Result createGroup(Group group);


    Result kickMember(GroupDTO groupDTO);

    Result updateHeader(GroupDTO groupDTO);

    Result updateName(GroupDTO groupDTO);

    Result updateStatus(Group group);

    Result updateType(GroupDTO groupDTO);

    Result listGroups(Long userId, Integer pageNum, Integer pageSize);

    Result exitGroup(GroupDTO groupDTO);

    Result groupMembers(Long id, Integer pageNum, Integer pageSize);

    Result selectByName(String name, Integer pageNum, Integer pageSize);

    Result updateAvatar(GroupDTO groupDTO);

    Result updateNickName(Groupmember groupmember);

    Result deleteGroup(GroupDTO groupDTO);
}
