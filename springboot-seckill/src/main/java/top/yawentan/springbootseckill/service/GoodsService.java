package top.yawentan.springbootseckill.service;

import top.yawentan.springbootseckill.vo.Result;

public interface GoodsService {
    Result getAllGoods();

    boolean doseckill(Long id);

    boolean order(Long userId,Long goodId,Long orderTime);
}
