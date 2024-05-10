package com.example.skydelivery.controller.admin;

import com.example.skydelivery.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺状态")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 设置店铺状态
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result setStatus(@PathVariable Integer status){
        log.info("店铺营业状态设置为:{}",status==1?"营业":"打烊");
        redisTemplate.opsForValue().set("SHOP_STATUS",status);
        return Result.success();
    }
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
