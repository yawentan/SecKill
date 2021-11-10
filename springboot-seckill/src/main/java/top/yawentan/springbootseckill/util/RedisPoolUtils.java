package top.yawentan.springbootseckill.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import top.yawentan.springbootseckill.dao.RedisMapper;

/**
 * @author yawen
 */
public class RedisPoolUtils {
    //单例模式
    private static volatile JedisPool jedisPool = null;

    /**
     * @author:yawen
     * @function:单例模式获得数据库连接池，采用双重判断的形式
     * @return:JedisPool
     */
    public static JedisPool getJedisPoolInstance(){
        if(jedisPool==null){
            synchronized (RedisMapper.class){
                //双指针判断保证单例模式
                if(jedisPool==null){
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(200);
                    poolConfig.setMaxIdle(32);
                    poolConfig.setMaxWaitMillis(100*1000);
                    poolConfig.setBlockWhenExhausted(true);

                    jedisPool = new JedisPool(poolConfig,"localhost",6379,60000);
                }
            }
        }
        return jedisPool;
    }

    /**
     * @function:从连接池中获取连接
     * @return:Jedis
     */
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
}