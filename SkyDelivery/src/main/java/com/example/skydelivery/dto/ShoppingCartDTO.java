package com.example.skydelivery.dto;

import lombok.Data;

/**
 * 购物车
 */
@Data
public class ShoppingCartDTO {
    //菜品id
    private Long dishId;
    //套餐id
    private Long setmealId;
    //口味
    private String dishFlavor;
}
