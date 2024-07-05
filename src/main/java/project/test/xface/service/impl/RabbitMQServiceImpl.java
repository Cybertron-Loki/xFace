package project.test.xface.service.impl;

import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.test.xface.entity.dto.ApplicationDTO;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Friendship;
import project.test.xface.entity.pojo.GroupMember;
import project.test.xface.entity.pojo.Message;
import project.test.xface.mapper.FriendshipMapper;
import project.test.xface.mapper.GroupMapper;
import project.test.xface.mapper.MessageMapper;
import project.test.xface.rabbit.GroupChatConfig;
import project.test.xface.service.RabbitMQService;
import project.test.xface.utils.RedisWorker;

import javax.annotation.Resource;

import static project.test.xface.rabbit.DirectRabbitConfig.*;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    @Resource
    private RedisWorker redisWorker;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    private GroupChatConfig chatConfig;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private FriendshipMapper friendshipMapper;

    @Override
    public Result sendApplication(ApplicationDTO applicationDTO) {
        Long id = redisWorker.nextId("applications:");
        applicationDTO.setId(id);
        if (applicationDTO.getType() == 0) //群聊申请
        {
            rabbitTemplate.convertAndSend(GROUP_APPLICATION_EXCHANGE, "group apply", JSONUtil.toJsonStr(applicationDTO));

        }else if(applicationDTO.getType() == 1){  //拉人进群申请
            rabbitTemplate.convertAndSend(GROUP_APPLICATION_EXCHANGE, "group apply", JSONUtil.toJsonStr(applicationDTO));

        }
        else if(applicationDTO.getType() == 2) { //加好友申请
            rabbitTemplate.convertAndSend(GROUP_INVITE_APPLICATION_EXCHANGE, "invitation apply", JSONUtil.toJsonStr(applicationDTO));
        }
        return Result.success("申请发送成功");
    }

    @Override
    public Result groupChat(Message message) {
        Long id = redisWorker.nextId("privacy message:");
        message.setId(id);
        messageMapper.save(message);

        chatConfig.createQueue("group_chat" + message.getGroupId());
        rabbitTemplate.convertAndSend("group_chat_exchange", "group_chat" + message.getGroupId(), JSONUtil.toJsonStr(message));
        return Result.success();
    }

    @Override
    public Result privacyChat(Message message) {
        Long id = redisWorker.nextId("privacy message:");
        message.setId(id);
        messageMapper.save(message);

        chatConfig.createQueue("privacy_chat" + message.getReceiverId());
        rabbitTemplate.convertAndSend("privacy_chat_exchange", "privacy_chat" + message.getReceiverId(), JSONUtil.toJsonStr(message));
        return Result.success();
    }

    @Transactional
    @Override
    public Result applicationReply(ApplicationDTO applicationDTO) {
        Integer type = applicationDTO.getType();
        /**
         * 加群申请 拉群申请
         */
        if (type == 0 || type == 2) {
            //disagree
            if (applicationDTO.getReply() == 0) {
                return Result.success("refuse u");
            } else {
                GroupMember groupMember = new GroupMember();
                groupMember.setGroupId(applicationDTO.getGroupId());
                groupMember.setUserId(applicationDTO.getSender());
                groupMapper.addMembers(groupMember);
            }
        }
        /**
         * 加好友申请
         */
        else if (type == 1) {
            //disagree
            if (applicationDTO.getReply() == 0) {
                return Result.success("refuse u");
            } else {
                //注意好友要双向存储数据
                Friendship friendship = new Friendship();
                friendship.setUserId(applicationDTO.getReceiver());
                friendship.setFriendId(applicationDTO.getSender());
                friendshipMapper.addFriends(friendship);
                friendship.setUserId(applicationDTO.getSender());
                friendship.setFriendId(applicationDTO.getReceiver());
                friendshipMapper.addFriends(friendship);
            }
        }
        return Result.success();
    }


}
