package top.yawentan.springbootseckill.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("top.yawentan.springbootseckill.dao")
public class MyBatisPlusConfig {
}
