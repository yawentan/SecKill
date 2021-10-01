package top.yawentan.springbootseckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yawentan.springbootseckill.dao.UserMapper;
import top.yawentan.springbootseckill.pojo.User;
import top.yawentan.springbootseckill.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByNamePwd(String name, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId,User::getName,User::getPassword);
        queryWrapper.eq(User::getName,name);
        queryWrapper.eq(User::getPassword,password);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }
}
