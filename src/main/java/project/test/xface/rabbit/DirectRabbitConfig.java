package project.test.xface.rabbit;


import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 群/好友申请，一共两个交换机，自己处理自己的类型
 */

@Configuration
public class DirectRabbitConfig {


    public static final String GROUP_APPLICATION_EXCHANGE = "group application exchange";
    public static final String FRIEND_APPLICATION_EXCHANGE = "friend application exchange";
    public static final String GROUP_APPLICATION_QUEQUE="group application queue";
    public static final String FRIEND_APPLICATION_QUEUE="group application queue";
    public static final String GROUP_INVITE_APPLICATION_EXCHANGE = "group invite application exchange";
    public static final String GROUP_INVITE_APPLICATION_QUEUE="group invite application queue";


    @Bean
    public DirectExchange directExchangeA() {
        return new DirectExchange(GROUP_APPLICATION_EXCHANGE);
    }

    @Bean
    public DirectExchange directExchangeB() {
        return new DirectExchange(FRIEND_APPLICATION_EXCHANGE);
    }
    @Bean
    public DirectExchange directExchangeC() {
        return new DirectExchange(GROUP_INVITE_APPLICATION_EXCHANGE);
    }


    @Bean
    public Queue groupApplicationQueue() {
        return new Queue(GROUP_APPLICATION_QUEQUE);
    }

    @Bean
    public Queue friendApplicationQueue() {
        return new Queue(FRIEND_APPLICATION_QUEUE);
    }

    @Bean
    public Queue groupInviteApplicationQueue() {
        return new Queue(GROUP_INVITE_APPLICATION_QUEUE);
    }


    @Bean
    public Binding bindingGroup(Queue groupApplicationQueue, DirectExchange directExchangeA) {
        return BindingBuilder.bind(groupApplicationQueue).to(directExchangeA).with("group apply");
    }
    @Bean
    public Binding bindingFriend(Queue friendApplicationQueue, DirectExchange directExchangeB) {
        return BindingBuilder.bind(friendApplicationQueue).to(directExchangeB).with("friend apply");
    }
    @Bean
    public Binding bindingInvite(Queue groupInviteApplicationQueue, DirectExchange directExchangeC) {
        return BindingBuilder.bind(groupInviteApplicationQueue).to(directExchangeC).with("invitation apply");
    }


}