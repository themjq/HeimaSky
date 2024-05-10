package com.example.skydelivery.dto;

import lombok.Data;

import java.io.Serializable;

/***
 * 拒绝此订单
 */
@Data
public class OrdersRejectionDTO implements Serializable {
    private Long id;

    //订单拒绝原因
    private String rejectionReason;
}
