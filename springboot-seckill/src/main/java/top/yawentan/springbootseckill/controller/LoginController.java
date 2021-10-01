package top.yawentan.springbootseckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.yawentan.springbootseckill.service.LoginService;
import top.yawentan.springbootseckill.vo.Result;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result Login(@RequestParam("name") String name, @RequestParam("pwd") String pwd) {
        return loginService.Login(name,pwd);
    }

}
