package com.merak.mqdemo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long productId;
    private String productName;
    private Integer productQuantity;
    private BigDecimal productPrice;
    private BigDecimal totalAmount;
    private Integer status; // 0-待支付，1-已支付，2-已取消，3-已完成
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime updateTime;
    
    // 订单状态常量
    public static final int STATUS_UNPAID = 0;
    public static final int STATUS_PAID = 1;
    public static final int STATUS_CANCELED = 2;
    public static final int STATUS_COMPLETED = 3;
} 