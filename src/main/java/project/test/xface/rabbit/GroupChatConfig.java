package project.test.xface.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupChatConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    // 创建Direct类型的交换机
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("group_chat_exchange");
    }

    // 动态创建队列的方法
    public void createQueue(String queueName) {
        Queue queue = new Queue(queueName, false, false, true);  // durable, exclusive, auto-delete
        rabbitTemplate.execute(channel -> {
            channel.queueDeclare(queue.getName(), queue.isDurable(), queue.isExclusive(), queue.isAutoDelete(), null);
            channel.queueBind(queue.getName(), "group_chat_exchange", queueName);
            return queue.getName();
        });
//        return queue.getName();
    }




}
