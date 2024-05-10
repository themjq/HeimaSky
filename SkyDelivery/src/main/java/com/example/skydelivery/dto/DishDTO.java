package com.example.skydelivery.dto;

import com.example.skydelivery.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *菜品表
 */
@Data
public class DishDTO implements Serializable {
    //菜品id
    private Long id;
    //菜品名
    private String name;
    //菜品分类id
    private Long categoryId;
    //菜品价格
    private BigDecimal price;
    //菜品图片地址
    private String image;
    //描述
    private String description;
    //状态 1 起售 0 停售
    private Integer status;
    //口味
    private List<DishFlavor> flavors=new ArrayList<>();

}
