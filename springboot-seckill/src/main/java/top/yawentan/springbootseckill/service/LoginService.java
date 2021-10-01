package top.yawentan.springbootseckill.service;

import top.yawentan.springbootseckill.vo.Result;

public interface LoginService {
    Result Login(String name,String password);
}
