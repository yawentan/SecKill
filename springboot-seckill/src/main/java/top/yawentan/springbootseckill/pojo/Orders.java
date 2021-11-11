package top.yawentan.springbootseckill.pojo;

import lombok.Data;

/**
 * mysql中order表的实体类
 * @author yawen
 */
@Data
public class Orders {
    Long id;
    Long userId;
    Long goodId;
    Long orderTime;
}
