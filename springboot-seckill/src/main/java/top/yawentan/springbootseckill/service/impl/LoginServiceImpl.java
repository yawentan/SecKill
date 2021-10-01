package top.yawentan.springbootseckill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yawentan.springbootseckill.dao.UserMapper;
import top.yawentan.springbootseckill.pojo.User;
import top.yawentan.springbootseckill.service.LoginService;
import top.yawentan.springbootseckill.service.UserService;
import top.yawentan.springbootseckill.vo.Result;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserService userService;

    @Override
    public Result Login(String name, String password) {
        //为空判断
        if(name==null||name.length()==0||password==null||password.length()==0){
            return Result.failed();
        }
        User user = userService.getUserByNamePwd(name, password);
        if(user==null){
            System.out.println("没有该用户，登陆失败");
            return Result.failed();
        }
        System.out.println("登录成功");
        return Result.success("登录成功");
    }
}
