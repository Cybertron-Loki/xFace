package project.test.xface.controller.voucher;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import project.test.xface.entity.pojo.VoucherOrder;
import project.test.xface.service.VoucherService;



import static project.test.xface.rabbit.VoucherOrderConfig.VOUCHERORDER_DEAD_QUEUE;
import static project.test.xface.rabbit.VoucherOrderConfig.VOUCHERORDER_QUEUE;


@Component
public class OrderController {
    @Autowired
    private VoucherService voucherService;

    @RabbitListener(queues = VOUCHERORDER_QUEUE)
    public void createOrder(Message message, Channel channel){
        String s = new String(message.getBody());
        VoucherOrder bean = JSONUtil.toBean(s, VoucherOrder.class);
        voucherService.createVoucherOrder(bean);

    }

    @RabbitListener(queues = VOUCHERORDER_DEAD_QUEUE)
    public void createOrder1(Message message){
        String s = new String(message.getBody());
        VoucherOrder bean = JSONUtil.toBean(s, VoucherOrder.class);
        voucherService.createVoucherOrder(bean);

    }


}
