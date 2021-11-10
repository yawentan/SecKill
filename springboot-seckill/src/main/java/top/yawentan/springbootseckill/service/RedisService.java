package top.yawentan.springbootseckill.service;

import top.yawentan.springbootseckill.pojo.Goods;

import java.util.List;

public interface RedisService {
    /**
     * 从redis中查询是否存在对应key，并返回对应内容
     */
    String findKey(String key);

    String findKeyMap(String key,String field);

    String saveString(String seckill_list, List<Goods> goods);

    void saveKeyMap(String goods_list, String valueOf,String data);
}
