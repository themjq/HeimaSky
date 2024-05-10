package com.example.skydelivery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提交订单
 */

@Data
public class OrdersSubmitDTO implements Serializable {
    //地址簿id
    private Long addressBookId;
    //付款方式
    private int payMethod;
    //备注
    private String remark;
    //预计送达时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;
    //配送状态  1立即送出  0选择具体时间
    private Integer deliveryStatus;
    //餐具数量
    private Integer tablewareNumber;
    //餐具数量状态  1按餐量提供  0选择具体数量
    private Integer tablewareStatus;
    //打包费
    private Integer packAmount;
    //总金额
    private BigDecimal amount;
}

/**
 *@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")是Jackson库中的注解，用于指定在序列化和反序列化过程中，日期时间类型字段的格式化方式。
 *
 * 具体作用如下：
 *
 * @JsonFormat是一个用于字段的注解，它可以应用于Java类的字段上。
 *
 * shape = JsonFormat.Shape.STRING表示将日期时间字段序列化为字符串形式。
 *
 * pattern = "yyyy-MM-dd HH:mm:ss"表示指定日期时间的格式化模式，即将日期时间格式化为指定的字符串形式。
 *
 * 通过使用@JsonFormat注解，可以在序列化和反序列化过程中，将日期时间字段按照指定的格式进行格式化和解析。当将Java对象序列化为JSON字符串时，日期时间字段会以字符串形式输出，并按照指定的格式进行格式化。当从JSON字符串反序列化为Java对象时，日期时间字段会按照指定的格式进行解析，并转换为对应的日期时间类型。
 *
 * 需要注意的是，@JsonFormat注解只是用于格式化和解析日期时间字段，它并不会对字段进行校验。如果需要对日期时间字段进行校验，可以使用其他的校验注解，如@Past、@Future等。
 *
 */