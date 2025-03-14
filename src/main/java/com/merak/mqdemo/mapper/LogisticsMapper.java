package com.merak.mqdemo.mapper;

import com.merak.mqdemo.entity.Logistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogisticsMapper {
    Logistics selectById(Long id);
    
    Logistics selectByOrderId(Long orderId);
    
    int insert(Logistics logistics);
    
    int updateStatus(@Param("id") Long id, 
                     @Param("status") Integer status);
} 