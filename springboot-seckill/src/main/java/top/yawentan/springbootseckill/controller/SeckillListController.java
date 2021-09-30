package top.yawentan.springbootseckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yawentan.springbootseckill.service.GoodsService;
import top.yawentan.springbootseckill.vo.Result;

@RestController
@RequestMapping("/seckill_list")
public class SeckillListController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping
    public Result listController(){
        return goodsService.getAllGoods();
    }
    @PostMapping
    public Result doseckill(@RequestParam("id") Long id){
        boolean res = goodsService.doseckill(id);
        if(res){
            return Result.success("秒杀成功");
        }else{
            return Result.success("失败");
        }
    }
}
