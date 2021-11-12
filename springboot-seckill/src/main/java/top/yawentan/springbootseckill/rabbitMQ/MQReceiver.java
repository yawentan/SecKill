package top.yawentan.springbootseckill.rabbitMQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @RabbitListener(queues="order")
    public void receive(Object msg){
        log.info("接收到消息"+msg);
    }
}
