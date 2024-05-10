package com.example.skydelivery.controller.user;

import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.ShoppingCartDTO;
import com.example.skydelivery.entity.ShoppingCart;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    /**
     * 购物车添加
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("购物车新增商品:{}",shoppingCartDTO);
        shoppingCartService.addShopping(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> show(){
        log.info("{}查看购物车",BaseContext.getCurrentId());
        return Result.success(shoppingCartService.show());
    }
    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    @ApiOperation("查看购物车")
    public Result clean(){
        Long userId=BaseContext.getCurrentId();
        log.info("{}清空购物车",userId);
        shoppingCartService.clean(userId);
        return Result.success();
    }
}
