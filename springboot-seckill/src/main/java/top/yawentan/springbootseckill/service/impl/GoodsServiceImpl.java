package top.yawentan.springbootseckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import top.yawentan.springbootseckill.dao.GoodsMapper;
import top.yawentan.springbootseckill.pojo.Goods;
import top.yawentan.springbootseckill.service.GoodsService;
import top.yawentan.springbootseckill.util.RedisPoolUtil;
import top.yawentan.springbootseckill.vo.Result;

import java.util.List;

//注入IoC
@Service
public class GoodsServiceImpl implements GoodsService {
    //不推荐的字段注入但是确实最方便
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Result getAllGoods() {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Goods::getId,Goods::getName,Goods::getNumber,Goods::getStartTime,Goods::getEndTime);
        List<Goods> goods = goodsMapper.selectList(queryWrapper);
        return Result.success(goods);
    }

    /**
     * 通过事务直接操作mysql数据库，在十万个请求一万个并发下用时10128ms
     * 改写service层先从redis中读数据没用的话再从mysql中读
     * 尝试直接访问redis，在同样情况下的处理效率
     * @param id
     * @return
     */
//    @Transactional
    @Override
    public boolean doseckill(Long id) {
        String key = String.valueOf(id);
        //先尝试从redis中读取数据
        JedisPool jedisPool = RedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = jedisPool.getResource();
        //从redis中读取剩余数量
        String s = jedis.hget(key,"number");
        if(s!=null){
            int num = Integer.valueOf(s);
            //秒杀
            if(num>0){
                jedis.hset(key,"number",String.valueOf(num-1));
                jedis.close();
                System.out.println("秒杀成功");
                return true;
            }else{
                jedis.close();
                System.out.println("秒杀失败");
                return false;
            }
        }else{
            //redis中没有时，从数据库中读取并缓存到redis
            Goods goods = goodsMapper.selectById(id);
            Long number = goods.getNumber();
            //判断过程没加锁，因此是线程不安全的
            //当线程商品数量大于0时才能秒杀
            if(number>0){
                LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(Goods::getNumber,number-1);
                updateWrapper.eq(Goods::getId,id);
                goodsMapper.update(null,updateWrapper);
                System.out.println("秒杀成功");
                return true;
            }
            System.out.println("秒杀失败");
            return false;
        }
    }
}
