package top.yawentan.springbootseckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yawentan.springbootseckill.dao.GoodsMapper;
import top.yawentan.springbootseckill.pojo.Goods;
import top.yawentan.springbootseckill.service.GoodsService;
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

    @Override
    public boolean doseckill(Long id) {
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
