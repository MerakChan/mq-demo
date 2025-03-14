package com.merak.mqdemo.service;

import com.merak.mqdemo.entity.Logistics;
import com.merak.mqdemo.mapper.LogisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogisticsService {

    @Autowired
    private LogisticsMapper logisticsMapper;

    /**
     * 创建物流订单
     */
    public Logistics createLogistics(Long orderId) {
        Logistics logistics = new Logistics();
        logistics.setOrderId(orderId);
        logistics.setLogisticsNo(generateLogisticsNo());
        logistics.setStatus(Logistics.STATUS_WAITING);
        logistics.setAddress("默认地址");
        logistics.setReceiver("默认收货人");
        logistics.setPhone("13800138000");

        logisticsMapper.insert(logistics);
        return logistics;
    }

    /**
     * 根据订单ID查询物流信息
     */
    public Logistics getLogisticsByOrderId(Long orderId) {
        return logisticsMapper.selectByOrderId(orderId);
    }

    /**
     * 生成物流单号
     */
    private String generateLogisticsNo() {
        return "LG" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);
    }
} 