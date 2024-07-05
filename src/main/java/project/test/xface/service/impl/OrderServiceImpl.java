package project.test.xface.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import project.test.xface.service.OrderService;

import javax.annotation.Resource;

public class OrderServiceImpl implements OrderService {

    @Resource
    private RabbitTemplate rabbitTemplate;

}
