package com.example.skydelivery.dto;

import lombok.Data;

/**
 * 取消订单
 */
@Data
public class OrdersCancelDTO {
    //id
    private Long id;
    //取消原因
    private String cancelReason;
}
