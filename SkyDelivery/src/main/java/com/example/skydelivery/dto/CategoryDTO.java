package com.example.skydelivery.dto;

import lombok.Data;

import java.io.Serializable;

/** 菜品表
 *
 * 在Java中，@Data是一个Lombok注解，它可以自动生成一些常见的代码，如getter和setter方法、equals和hashCode方法、toString方法等。使用@Data注解可以简化Java类的编写，减少样板代码的数量。
 *
 * 具体来说，@Data注解会自动为类中的所有非静态字段生成getter和setter方法。它还会自动生成equals和hashCode方法，这些方法会根据类中的字段来判断对象的相等性。此外，@Data注解还会生成一个toString方法，用于将对象转换为字符串表示。
 *
 * 使用@Data注解时，还可以通过其他注解来自定义生成的代码。例如，可以使用@NonNull注解来标记某个字段不能为空，生成的setter方法会进行非空校验。
 *
 * 需要注意的是，使用@Data注解会自动为类添加@ToString、@EqualsAndHashCode、@Getter和@Setter等注解，因此不需要再单独使用这些注解。
 */
@Data
public class CategoryDTO implements Serializable {
    //主键
    private Long id;
    //类型 1 菜品分类 2 套装分类
    private Integer type;

    //分类名
    private String name;

    //排序
    private Integer sort;
}
