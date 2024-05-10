package com.example.skydelivery.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单付款方式信息
 */
@Data
public class OrdersPaymentDTO implements Serializable {
    //订单号
    private String orderNumber;

    //付款方式
    private Integer payMethod;
}
