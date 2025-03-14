package com.merak.mqdemo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer version; // 乐观锁版本号
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 