//package project.test.xface.controller.group;
//
//
//import cn.hutool.json.JSONException;
//import cn.hutool.json.JSONUtil;
//import io.github.classgraph.json.JSONUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestBody;
//import project.test.xface.entity.dto.ApplicationDTO;
//import project.test.xface.entity.dto.Result;
//import project.test.xface.entity.pojo.GroupMember;
//import project.test.xface.entity.pojo.Message;
//import project.test.xface.mapper.GroupMapper;
//import project.test.xface.mapper.GroupmemberMapper;
//
//import javax.annotation.Resource;
//
//import java.util.List;
//
//import static project.test.xface.rabbit.DirectRabbitConfig.*;
//
//@Component
//@Slf4j
//public class RabbitMQListener {
//
//    @Autowired
//    private GroupmemberMapper groupmemberMapper;
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @RabbitListener(queues = GROUP_APPLICATION_QUEQUE)
//    public void processGroupApplication(String applicationDTO1) {
//        // 处理群聊申请消息
//        ApplicationDTO applicationDTO = JSONUtil.toBean(applicationDTO1, ApplicationDTO.class);
//        log.info(applicationDTO1.toString());
//        messagingTemplate.convertAndSend("/topic/group_apply", applicationDTO);
//    }
//
//    @RabbitListener(queues = FRIEND_APPLICATION_QUEUE)
//    public void processFriendInvitation(String applicationDTO1) {
//        // 处理加好友申请消息
//        ApplicationDTO applicationDTO = JSONUtil.toBean(applicationDTO1, ApplicationDTO.class);
//        log.info(applicationDTO1);
//        messagingTemplate.convertAndSend("/topic/friend_invitation_apply", applicationDTO);
//    }
//
//    @RabbitListener(queues = GROUP_INVITE_APPLICATION_QUEUE)
//    public void processGroupInvitationApplication(String applicationDTO1) {
//        // 处理加好友申请消息
//        ApplicationDTO applicationDTO = JSONUtil.toBean(applicationDTO1, ApplicationDTO.class);
//        log.info(applicationDTO1);
//        messagingTemplate.convertAndSend("/topic/group_invitation_apply", applicationDTO);
//    }
//
//    @RabbitListener(queues = "#{autoDeleteQueue.name}")
//    public void processGroupMessage(String message1) {
//        // 处理加好友申请消息
//        try {
//            Message message = JSONUtil.toBean(message1, Message.class);
//            log.info(message1);
//            Long groupId = message.getGroupId();
//            List<GroupMember> groupMembers = groupmemberMapper.selectMembers(groupId);
//            for (GroupMember groupMember : groupMembers) {
//                Long userId = groupMember.getUserId();
//                messagingTemplate.convertAndSendToUser(userId.toString(), "/topic/group_message", message);
//            }
//        } catch (JSONException e) {
//            log.error("Failed to parse JSON message: " + message1, e);
//        } catch (Exception e) {
//            log.error("Failed to process group message: " + message1, e);
//        }
//    }
//
//}

