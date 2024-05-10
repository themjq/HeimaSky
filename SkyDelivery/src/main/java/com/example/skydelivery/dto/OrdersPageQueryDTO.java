package com.example.skydelivery.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 *
 * 这个分页是怎么回事，phone和name的意思是？
 *
 */

/**
 * 订单分页
 */
@Data
public class OrdersPageQueryDTO implements Serializable {
    //页码
    private int page;
    //每页数量
    private int pageSize;
    //
    private String number;
    //手机号
    private String phone;
    //状态
    private Integer status;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private Long userId;
}
