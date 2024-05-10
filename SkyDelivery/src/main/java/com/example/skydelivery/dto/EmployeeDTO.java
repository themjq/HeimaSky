package com.example.skydelivery.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工表   //与数据库中的EmployeeDTO表不完全相同
 */
@Data
public class EmployeeDTO implements Serializable {
    //id
    private Long id;

    //用户名
    private String username;

    //昵称
    private String name;

    //手机号
    private String phone;

    //性别
    private String sex;

    //身份证号
    private String idNumber;
}
