package com.example.skydelivery.service;

import com.example.skydelivery.dto.ShoppingCartDTO;
import com.example.skydelivery.entity.ShoppingCart;
import com.example.skydelivery.result.Result;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 添加购物车
     */
    void addShopping(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> show();

    /**
     * 清空购物车
     * @return
     */
    void clean(Long userId);
}
