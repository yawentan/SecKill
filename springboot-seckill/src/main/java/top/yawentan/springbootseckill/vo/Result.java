package top.yawentan.springbootseckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data){
        return new Result(200,"成功",data);
    }

    public static Result failed(){
        return new Result(400,"失败",null);
    }
}
