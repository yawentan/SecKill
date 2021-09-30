package top.yawentan.springbootseckill.pojo;

import lombok.Data;

@Data
public class Goods {
    private Long id;
    private String name;
    private Long number;
    private String startTime;
    private String endTime;
}
