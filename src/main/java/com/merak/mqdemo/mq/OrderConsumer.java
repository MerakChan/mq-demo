package com.merak.mqdemo.mq;

import com.merak.mqdemo.config.RabbitMQConfig;
import com.merak.mqdemo.entity.Order;
import com.merak.mqdemo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {

    @Autowired
    private OrderService orderService;

    /**
     * 监听订单处理队列，处理超时订单
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_PROCESS_QUEUE)
    public void processTimeoutOrder(String orderNo) {
        log.info("接收到订单超时消息，订单号：{}", orderNo);
        
        // 查询订单
        Order order = orderService.getOrderByOrderNo(orderNo);
        
        // 修改这里的比较方式
        if (order != null && order.getStatus() == Order.STATUS_UNPAID) {
            log.info("订单{}超时未支付，自动取消", orderNo);
            orderService.cancelOrder(orderNo);
        }
    }
} 