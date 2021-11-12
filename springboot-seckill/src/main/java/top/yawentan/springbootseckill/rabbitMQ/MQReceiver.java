package top.yawentan.springbootseckill.rabbitMQ;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yawentan.springbootseckill.dao.OrdersMapper;
import top.yawentan.springbootseckill.pojo.Orders;

/**
 * @author yawen
 */
@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private OrdersMapper orderMapper;

    @RabbitListener(queues="order")
    public void receive(String msg){
        Orders orders = JSON.parseObject(msg, Orders.class);
        orderMapper.insert(orders);
        log.info("接收到消息"+msg);
        log.info("下单成功");
    }
}
