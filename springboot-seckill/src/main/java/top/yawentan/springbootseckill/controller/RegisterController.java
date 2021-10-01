package top.yawentan.springbootseckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.yawentan.springbootseckill.service.RegisterService;
import top.yawentan.springbootseckill.vo.Result;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping
    public Result register(@RequestParam("name") String name, @RequestParam("pwd") String pwd){
        return registerService.register(name,pwd);
    }
}
