package top.yawentan.springbootseckill.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yawentan.springbootseckill.pojo.User;
import top.yawentan.springbootseckill.service.LoginService;
import top.yawentan.springbootseckill.service.UserService;
import top.yawentan.springbootseckill.util.JWTUtils;
import top.yawentan.springbootseckill.util.StringUtils;
import top.yawentan.springbootseckill.vo.Result;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserService userService;
    String SALT = "miaosha";
    /**
     * @description 用户输入账号密码，登录,
     * @param name
     * @param password
     * @return
     */
    @Override
    public Result Login(String name, String password) {
        //1.为空判断
        if(StringUtils.isBlank(name)|| StringUtils.isBlank(password)){
            return Result.failed();
        }
        //2.查mysql看是否存在该用户
        String pwd = DigestUtils.md5Hex(password+SALT);
        User user = userService.getUserByNamePwd(name, pwd);
        if(user==null){
            System.out.println("用户名密码错误，登陆失败");
            return Result.failed();
        }
        String token = JWTUtils.createToken(user.getId());
        return Result.success(token);
    }
}
