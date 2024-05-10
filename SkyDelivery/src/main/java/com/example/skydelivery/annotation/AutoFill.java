package com.example.skydelivery.annotation;

import com.example.skydelivery.enumeration.OperationType;
import org.apache.ibatis.annotations.Update;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，标识自动填充的部分
 */
@Target(ElementType.METHOD) //指定注解可以加在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
