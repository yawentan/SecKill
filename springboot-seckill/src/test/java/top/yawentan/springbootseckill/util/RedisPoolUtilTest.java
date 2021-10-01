package top.yawentan.springbootseckill.util;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.junit.jupiter.api.Assertions.*;

class RedisPoolUtilTest {
    @Test
    public void testRedisPool(){
        JedisPool jedisPool = RedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = jedisPool.getResource();
        String s = jedis.ping();
        System.out.println(s);
        String number = jedis.hget("1", "number");
        if(number!=null){
            int num = Integer.valueOf(number)-1;
            jedis.hset("1","number",String.valueOf(num));
            System.out.println(jedis.hget("1", "number"));
        }

        jedis.close();
    }
}