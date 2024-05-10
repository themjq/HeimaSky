package com.example.skydelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 *
 * @Builder注解：@Builder注解可以自动生成一个建造者模式的构建器方法，用
 * 于创建对象。通过在类上添加@Builder注解，Lombok会自动生成一个静态内部类作为构建器，并为类中的所有非静态字段生成对应的setter方法。使用构建器可以方便地链式调用设置对象的属性，从而创建对象。
 *
 * @NoArgsConstructor注解：@NoArgsConstructor注解可以自动生成一个无参的构造方法
 * 。通过在类上添加@NoArgsConstructor注解，Lombok会自动为类生成一个无参的构造方法，用于创建对象时不需要传递任何参数。
 *
 * @AllArgsConstructor注解：@AllArgsConstructor注解可以自动生成一个包含所有字段的构造方法
 * 。通过在类上添加@AllArgsConstructor注解，Lombok会自动为类生成一个构造方法，该构造方法包含类中所有非静态字段作为参数，用于创建对象时传递所有字段的值。
 */
public class DataOverViewQueryDTO implements Serializable {
    //得到当前时间
    private LocalDateTime begin;
    private LocalDateTime end;
}
