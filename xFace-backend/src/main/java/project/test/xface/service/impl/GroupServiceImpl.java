package project.test.xface.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import project.test.xface.common.PageResult;
import project.test.xface.entity.dto.GroupDTO;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Group;
import project.test.xface.entity.pojo.Groupmember;
import project.test.xface.entity.pojo.User;
import project.test.xface.entity.vo.GroupVO;
import project.test.xface.mapper.GroupMapper;
import project.test.xface.mapper.GroupmemberMapper;
import project.test.xface.mapper.UserMapper;
import project.test.xface.service.GroupService;
import org.springframework.stereotype.Service;
import project.test.xface.utils.RedisWorker;
import project.test.xface.utils.UserHolder;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;

import static project.test.xface.common.RedisConstant.Group_ID_Key;

@Service
public class GroupServiceImpl implements GroupService {


    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GroupmemberMapper groupmemberMapper;
    @Resource
    private RedisWorker redisWorker;

    @Transactional
    @Override
    public Result createGroup(Group group) {
        if (StringUtils.isEmpty(group.toString())) {
            return Result.fail("不能为空");
        }
        Long userId = UserHolder.getUser().getId();
        //每个人上限建30个群，多了不给
        Integer number = groupMapper.checkNumber(userId);
        if (number > 30) return Result.fail("max number is 30");
        Long id = redisWorker.nextId(Group_ID_Key);
        group.setId(id);
        group.setCreatorId(UserHolder.getUser().getId());
        group.setHeaderId(group.getCreatorId());    //默认创建人就是群主

        if (group.getStatus().toString().isEmpty()) group.setStatus(0);
        boolean isSuccess = groupMapper.addGroup(group);
        //:groupmember
        group.setRole("Header");
        groupmemberMapper.addUser(group);
        if (isSuccess) return Result.success();
        return Result.fail("新建失败");
    }
    @Transactional
    @Override
    public Result deleteGroup(GroupDTO groupDTO) {
        //是否是群主/super管理员
        String role = groupDTO.getRole();
        if(!role.equals("Header"))  return Result.fail("无权限");
        User user = userMapper.selectById(groupDTO.getId());

        if (!user.getRole().equals("SysAdmin")) return Result.fail("身份验证失败");
        //删除
        boolean isSuccess = groupMapper.deleteGroup(groupDTO.getId());
        groupmemberMapper.deleteByGroupId(groupDTO.getId());
        if (isSuccess) return Result.success();
        return Result.fail("删除失败");
    }

    @Override
    public Result update(GroupDTO groupDTO) {
        String role = groupDTO.getRole();
        if(!role.equals("Header"))  return Result.fail("无权限");
        Group group = new Group();
        BeanUtils.copyProperties(groupDTO,group);
        groupMapper.update(group);
        return Result.success();
    }

    @Transactional
    @Override
    public Result kickMember(GroupDTO groupDTO) {
        //todo:缓存查,如果踢的是群主不行
        //groupmember操作也行
        String userBeingRole = groupDTO.getUserBeingRole();//被操作用户
        if(userBeingRole=="Header") return Result.fail("群主不能kcik");


        if (!groupDTO.getRole().equals("Header") ) return Result.fail("no access");
        //如果是群主，才能kick others ass

        boolean isSuccess = groupmemberMapper.deleteById(groupDTO.getId(),groupDTO.getUserBeingId());
        if (isSuccess) return Result.success();

        return Result.fail("删除失败");
    }

    @Transactional
    @Override
    public Result updateHeader(GroupDTO groupDTO) {
        //操作人是否为群主
        String role = groupDTO.getRole();
        if(role!="Header") return Result.fail("无权限");
        //被操作人是否为群主
        if (groupDTO.getUserBeingRole().equals("Header") ) return Result.fail("已经是群主了");
        //todo：操作人是否被禁用

        Group group=new Group();
        group.setHeaderId(groupDTO.getUserId());
        group.setId(groupDTO.getId());
        groupMapper.update(group);

        Groupmember groupmember=new Groupmember();
        groupmember.setGroupId(groupDTO.getId());
        groupmember.setRole("Header");
        groupmember.setUserId(groupDTO.getUserBeingId());

        groupmemberMapper.update(groupmember);   //升级

        groupmember.setUserId(groupDTO.getUserId());
        groupmember.setRole("Member");
        groupmemberMapper.update(groupmember);   //降级
        return Result.success();
    }

//    @Override
//    public Result updateName(GroupDTO groupDTO) {
//        String role = groupDTO.getRole();
//        if(!role.equals("Header"))  return Result.fail("无权限");
//        Group group = new Group();
//        BeanUtils.copyProperties(groupDTO,group);
//        groupMapper.update(group);
//        return Result.success();
//    }

    @Override
    public Result updateStatus(Group group) {
        //管理员更改群状态，不需要穿dto
        User user = userMapper.selectById(UserHolder.getUser().getId());
        if (!user.getRole().equals("SysAdmin") )  return Result.fail("无权限");
        groupMapper.update(group);
        return Result.success();
    }


    @Override
    public Result listGroups(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<Group> groups=groupmemberMapper.listGroups(userId);
        List<Group> result = groups.getResult();
        long total = groups.getTotal();
        return Result.success(new PageResult(total,result));
    }

    @Override
    public Result exitGroup(GroupDTO groupDTO) {
        //判断是否为群主
        if (groupDTO.getRole().equals("Header")) {
            return Result.fail("群主不能退");
        }
        groupmemberMapper.deleteById(groupDTO.getId(),groupDTO.getUserId());
            return Result.success();
    }

    @Override
    public Result groupMembers(Long id, Integer pageNum, Integer pageSize) {
        Long userId = UserHolder.getUser().getId();
        PageHelper.startPage(pageNum,pageSize);
        List<Groupmember> results=groupmemberMapper.selectMembers(id,userId);
        PageInfo<Groupmember> groupmembers=new PageInfo<>(results);
        long total = groupmembers.getTotal();

        Group group=groupMapper.selectById(id);
        GroupVO groupVO=new GroupVO();
        BeanUtils.copyProperties(group,groupVO);

        groupVO.setGroupmemberList(groupmembers.getList());
        // 把 GroupVO 包装成 List
        List<GroupVO> groupVOList = Collections.singletonList(groupVO);
        return Result.success(new PageResult(total,groupVOList));
    }

    @Override
    public Result selectByName(String name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<Group> group=groupMapper.selectByName(name);
        List<Group> results=group.getResult();
        long total = group.getTotal();
        return Result.success(new PageResult(total,results));
    }


    @Override
    public Result updateNickName(Groupmember groupmember) {
        //只能自己修改自己的
        Long userId = groupmember.getUserId();
        if(!UserHolder.getUser().getId().equals(userId)){
            return Result.fail("不能修改其他人的");
        }
        groupmemberMapper.update(groupmember);
        return Result.success();
    }


}




