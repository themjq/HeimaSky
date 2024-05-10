package com.example.skydelivery.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜品分页表
 */
@Data
public class DishPageQueryDTO implements Serializable {
    //页码
    private int page;

    //每页数量
    private int pageSize;
    //分类名称
    private String name;
    //分类id
    private Integer categoryId;
    //状态 0 禁用 1 启用
    private Integer status;
}
