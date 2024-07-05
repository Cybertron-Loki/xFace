package project.test.xface.service;

import project.test.xface.entity.dto.ApplicationDTO;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Message;


public interface RabbitMQService {
    Result sendApplication(ApplicationDTO groupApplicationDTO);

    Result groupChat(Message message);

    Result privacyChat(Message message);

    Result applicationReply(ApplicationDTO applicationDTO);
}
