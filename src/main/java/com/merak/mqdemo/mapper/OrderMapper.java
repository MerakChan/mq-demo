package com.merak.mqdemo.mapper;

import com.merak.mqdemo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    Order selectById(Long id);
    
    Order selectByOrderNo(String orderNo);
    
    List<Order> selectByUserId(Long userId);
    
    int insert(Order order);
    
    int updateStatus(@Param("id") Long id, 
                     @Param("status") Integer status, 
                     @Param("payTime") LocalDateTime payTime);
} 