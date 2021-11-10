package top.yawentan.springbootseckill.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import top.yawentan.springbootseckill.pojo.Goods;
import top.yawentan.springbootseckill.service.RedisService;
import top.yawentan.springbootseckill.util.RedisPoolUtils;

import java.util.List;

@Service
public class RedisServiceImpl implements RedisService {
    JedisPool jedisPoolInstance = RedisPoolUtils.getJedisPoolInstance();
    @Override
    public String findKey(String key) {
        Jedis jedis = jedisPoolInstance.getResource();
        String s = jedis.get(key);
        jedis.close();
        return s;
    }

    @Override
    public String findKeyMap(String key, String field) {
        Jedis jedis = jedisPoolInstance.getResource();
        String s = jedis.hget(key,field);
        jedis.close();
        return s;
    }

    @Override
    public String saveString(String key, List<Goods> goods) {
        Jedis jedis = jedisPoolInstance.getResource();
        String secKillList = jedis.set(key, JSONArray.toJSONString(goods));
        jedis.close();
        return secKillList;
    }

    @Override
    public void saveKeyMap(String key, String field,String data) {
        Jedis jedis = jedisPoolInstance.getResource();
        jedis.hset(key,field,data);
        jedis.close();
    }
}
