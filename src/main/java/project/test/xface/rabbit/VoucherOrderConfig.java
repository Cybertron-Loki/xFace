package project.test.xface.rabbit;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Objects;

@Configuration
public class VoucherOrderConfig {
    public static final String VOUCHERORDER_EXCHANGE = "voucher order exchange";
    public static final String VOUCHERORDER_DEAD_EXCHANGE = "voucher order exchange";
    public static final String VOUCHERORDER_QUEUE = "order_handler_queue";
    public static final String VOUCHERORDER_DEAD_QUEUE= "orderDEAD_handler_queue";



    // 创建Direct类型的交换机
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(VOUCHERORDER_EXCHANGE);
    }
    @Bean
    public DirectExchange directDeadExchange() {
        return new DirectExchange(VOUCHERORDER_DEAD_EXCHANGE);
    }
    //
    @Bean
    public Queue orderQueue() {
        final HashMap<String , Object> arguments=new HashMap<>();
        //设置死信交换机
        arguments.put("dead_order_exchange",VOUCHERORDER_DEAD_EXCHANGE);
        //设置死信routingkey
        arguments.put("dead_order_key","dead_order_routingkey");
        //设置TTL设置10秒过期
        arguments.put("dead_ttl",10000);
        return QueueBuilder.durable(VOUCHERORDER_QUEUE).withArguments(arguments).build();

    }
    @Bean
    public Queue orderDeadQueue() {
        return QueueBuilder.durable(VOUCHERORDER_DEAD_QUEUE).build();
    }


    @Bean
    public Binding bindingA(Queue orderQueue , DirectExchange directExchange) {
        return BindingBuilder.bind(orderQueue).to(directExchange).with("order");
    }
    @Bean
    public Binding bindingD(Queue orderDeadQueue , DirectExchange directDeadExchange) {
        return BindingBuilder.bind(orderDeadQueue).to(directDeadExchange).with("dead_order");
    }

}
