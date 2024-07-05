package project.test.xface.controller.group;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.test.xface.entity.dto.ApplicationDTO;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Message;
import project.test.xface.service.RabbitMQService;

@RestController
@RequestMapping("/Message")
public class CheatController {
    @Autowired
    private RabbitMQService rabbitMQService;

    /**
     * 加入群/好友申请,ps:不存database
     */
    @PostMapping("/joinGroup")
    public Result joinGroup(@RequestBody ApplicationDTO applicationDTO) {
        return rabbitMQService.sendApplication( applicationDTO);
    }
    /**
     * 申请同意,给进群人返回消息
     */
    @PostMapping("/joinGroup/AgreeOrNot")
    public Result joinGroupReply(@RequestBody ApplicationDTO applicationDTO){
        return rabbitMQService.applicationReply(applicationDTO);
    }

    /**
     * 组群内聊天
     */
    @PostMapping("/sendGroupMessage")
    public Result SendGroupMessage(@RequestBody Message message) {
        return rabbitMQService.groupChat(message);
    }

    /**
     * 私聊
     */
    @PostMapping("/sendPrivacyMessage")
    public Result SendPrivacyMessage(@RequestBody Message message) {
        return rabbitMQService.privacyChat(message);
    }

}
