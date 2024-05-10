package com.example.skydelivery.dto;

import com.example.skydelivery.result.PageResult;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工分页
 */
@Data
public class EmployeePageQueryDTO implements Serializable {
    //员工名
    private String name;
    //页码
    private int page;
    //每页数量
    private int pageSize;

}
