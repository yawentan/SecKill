package top.yawentan.springbootseckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yawentan.springbootseckill.rabbitMQ.MQReceiver;
import top.yawentan.springbootseckill.rabbitMQ.MQSender;
import top.yawentan.springbootseckill.vo.Result;

@RestController
public class HelloController {
    @Autowired
    private MQSender mqSender;

    @GetMapping("/")
    public String helloController(){
        return "hello";
    }

    @RequestMapping("/mqSend")
    public Result mqSend(){
        mqSender.send("消息队列发消息");
        return Result.success("消息发送成功");
    }

}
