package com.example.skydelivery.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 设置套餐内的分页
 */
@Data
public class SetmealPageQueryDTO implements Serializable {
    private int page;

    private int pageSize;

    private String name;

    //分类id
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    private Integer status;
}
