package com.example.skydelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜品类别信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
    private static final long serialVersionUID=1L;
    private Long id;

    //类型: 1菜品 2套餐
    private Integer type;

    //分类名称
    private String name;

    //顺序
    private Integer sort;

    //分类状态 0禁用  1启用
    private Integer status;
    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;

    //创建人
    private Long createUser;

    //修改人
    private Long updateUser;
}
