package com.merak.mqdemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Logistics {
    private Long id;
    private Long orderId;
    private String logisticsNo;
    private Integer status; // 0-待发货，1-已发货，2-已签收
    private String address;
    private String receiver;
    private String phone;
    private LocalDateTime createTime;
    private LocalDateTime shipTime;
    private LocalDateTime receiveTime;
    private LocalDateTime updateTime;
    
    // 物流状态常量
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_SHIPPED = 1;
    public static final int STATUS_RECEIVED = 2;
} 