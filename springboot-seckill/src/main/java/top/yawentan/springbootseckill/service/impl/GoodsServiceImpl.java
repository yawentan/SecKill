package top.yawentan.springbootseckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import top.yawentan.springbootseckill.dao.GoodsMapper;
import top.yawentan.springbootseckill.dao.OrdersMapper;
import top.yawentan.springbootseckill.pojo.Goods;
import top.yawentan.springbootseckill.pojo.Orders;
import top.yawentan.springbootseckill.rabbitMQ.MQSender;
import top.yawentan.springbootseckill.service.GoodsService;
import top.yawentan.springbootseckill.service.RedisService;
import top.yawentan.springbootseckill.util.StringUtils;
import top.yawentan.springbootseckill.util.UserThreadLocal;
import top.yawentan.springbootseckill.vo.Result;

import java.util.List;

/**
 * 注入IoC
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    /**
     * 不推荐的字段注入但是确实最方便
     */
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrdersMapper orderMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender mqSender;

    /**
     * 查询秒杀列表：
     * 一开始是直接查询mysql，通过jmeter测试，在1000个线程，吞吐量为412.7/sec-510.7/sec
     * 现在打算将秒杀列表放入redis进行缓存，先查询redis，不存在的话就查redis并将列表拉入redis中
     * 修改后吞吐量变为了1184.8-1398.6/sec
     * @return Result
     */
    @Override
    public Result getAllGoods() {
        //1.先尝试从redis中查询秒杀列表
        String secKillList = redisService.findKey("seckill_list");
        //2.判断是否为空
        if(StringUtils.isBlank(secKillList)){
            //3.为空查询mysql
            LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Goods::getId,Goods::getName,Goods::getNumber,Goods::getStartTime,Goods::getEndTime);
            List<Goods> goods = goodsMapper.selectList(queryWrapper);
            //4.将秒杀列表缓存到redis
            redisService.saveString("seckill_list",goods);
            return Result.success(goods);
        }
        //5.缓存中能查到,从redis中取出来的Goods
        List<Goods> redisGoods = JSONArray.parseArray(secKillList, Goods.class);
        return Result.success(redisGoods);
    }

    /**
     * 通过事务直接操作mysql数据库，在十万个请求一万个并发下用时10128ms
     * 改写service层先从redis中读数据没用的话再从mysql中读
     * 尝试直接访问redis，在同样情况下的处理效率
     * @param id 商品id
     * @return boolean 秒杀是否成功
     */
    @Override
    public boolean doseckill(Long id) {
        String key = String.valueOf(id);
        Goods good;
        synchronized(Object.class) {
            //1.先尝试从redis中读取数据
            String seckillList = redisService.findKeyMap("goods_list", String.valueOf(key));
            //2.判断redis中是否有数据
            if (!StringUtils.isBlank(seckillList)) {
                //3.从redis中读取剩余数量
                good = JSON.parseObject(seckillList, Goods.class);
            } else {
                //4. redis中没有时，从数据库中读取并缓存到redis
                good = goodsMapper.selectById(id);
                redisService.saveKeyMap("goods_list", String.valueOf(key), JSON.toJSONString(good));
            }
            Long num = good.getNumber();
            //5. 采用同步机制保证了不超卖
            //当线程商品数量大于0时才能秒杀
            if (num > 0) {
                good.setNumber(num - 1);
                redisService.saveKeyMap("goods_list", String.valueOf(id), JSON.toJSONString(good));
                //6.新建订单对象
                Orders order = new Orders();
                order.setUserId(UserThreadLocal.get());
                order.setGoodId(id);
                order.setOrderTime(System.currentTimeMillis());
                //7.采用rabbitMQ进行异步下单
                mqSender.send(JSON.toJSONString(order));
                System.out.println("秒杀成功");
                return true;
            } else {
                System.out.println("秒杀失败");
                return false;
            }
        }
    }

    /**
     * 新建一条订单信息，将订单信息插入orders表中。
     * @param userId 用户id
     * @param goodId 商品id
     * @param orderTime 下单时间
     * @return boolean 下单是否成功
     */
    @Override
    public boolean order(Long userId, Long goodId, Long orderTime) {
        Orders order = new Orders();
        order.setUserId(userId);
        order.setGoodId(goodId);
        order.setOrderTime(orderTime);
        return orderMapper.insert(order)==1;
    }
}
