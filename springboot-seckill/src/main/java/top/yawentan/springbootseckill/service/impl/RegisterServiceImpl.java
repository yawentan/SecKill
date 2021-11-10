package top.yawentan.springbootseckill.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yawentan.springbootseckill.pojo.User;
import top.yawentan.springbootseckill.service.RegisterService;
import top.yawentan.springbootseckill.service.UserService;
import top.yawentan.springbootseckill.vo.Result;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserService userService;
    String SALT = "miaosha";

    @Override
    public Result register(String name, String pwd) {
        //用户名密码不能为空
        if(name==null||name.length()==0||pwd==null||pwd.length()==0){
            System.out.println("用户名密码不能为空");
            return Result.failed();
        }
        //查询用户是否存在
        User user = userService.getUserByNamePwd(name, pwd);
        if(user!=null){
            System.out.println("用户已注册！");
            return Result.failed();
        }
        //插入用户
        user = new User();
        user.setName(name);
        String pwdMd5 = DigestUtils.md5Hex(pwd + SALT);
        user.setPassword(pwdMd5);
        userService.save(user);
        return Result.success("注册成功");
    }
}
