package com.example.skydelivery.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜品分页查询
 */

/**
 *
 * 在Java中，Serializable是一个标记接口（marker interface），用于表示一个类的对象可以被序列化和反序列化。
 *
 * 序列化是指将对象转换为字节流的过程，以便可以将其存储在文件中、通过网络传输或在不同的Java虚拟机之间进行通信。反序列化则是将字节流转换回对象的过程。
 *
 * Serializable接口的存在使得Java对象可以被序列化和反序列化。当一个类实现了Serializable接口时，它的对象可以被序列化为字节流，并且可以在需要时重新创建为原始对象。
 */
@Data
public class CategoryPageQueryDTO implements Serializable {
    //页码
    private int page;       //页码
    private int pageSize;   //每页数目
    private String name;    //分类的名称
    private Integer type;//菜品分类，1为菜品，2为套餐
}
