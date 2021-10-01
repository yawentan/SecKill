package top.yawentan.springbootseckill.service;

import top.yawentan.springbootseckill.pojo.User;

public interface UserService {
    User getUserByNamePwd(String name,String password);
}
