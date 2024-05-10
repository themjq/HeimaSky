package com.example.skydelivery.controller.user;

import com.example.skydelivery.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺状态")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 查询店铺状态
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("店铺营业状态为:{}",status==1?"营业":"打烊");
        return Result.success(status);
    }
}
