package project.test.xface.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import project.test.xface.entity.pojo.Group;
import project.test.xface.entity.pojo.GroupMember;


public interface GroupMapper  {

    @Select("select count(*) from Group where creatorId=#{userId} ")
    Integer checkNumber(Long userId);

    boolean addGroup(Group group);

    @Select("select headerId from Group where groupId=#{groupId}")
    Long checkGroupHeader(Long groupId);


    @Delete("delete from Group where id=#{groupId}")
    boolean deleteGroup(Long groupId);

    boolean update(Group group);

    @Select("select * from Group where id=#{id}")
    Group selectById(Long id);


    @Select("select * from Group where name like concat('%',name,'%')")
    Page<Group> selectByName(String name);

    void addMembers(GroupMember groupMember);
}




