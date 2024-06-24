package project.test.xface.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.Group;
import project.test.xface.entity.pojo.Groupmember;

import java.util.List;


public interface GroupmemberMapper {


    void update(Groupmember groupmember);

    Page<Group> listGroups(Long userId);

    void addUser(Group group);


    @Select("select  *  from  GroupMember where groupId=#{id} and userId=#{userId}")
    List<Groupmember> selectMembers(Long id, Long userId);

    @Delete("delete from GroupMember where groupId=#{id} and userId=#{userBeingId}")
    boolean deleteById(Long id, Long userBeingId);


    @Delete("delete from GroupMember where groupId=#{id}")
    void deleteByGroupId(Long id);
}




