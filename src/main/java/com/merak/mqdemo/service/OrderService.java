package com.merak.mqdemo.service;

import com.merak.mqdemo.config.RabbitMQConfig;
import com.merak.mqdemo.entity.Order;
import com.merak.mqdemo.entity.Product;
import com.merak.mqdemo.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订单
 */
@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderMapper orderMapper;

    private final ProductCacheService productCacheService;

    private final LogisticsService logisticsService;

    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 创建订单--传入用户id，商品id和商品数量
     */
    @Transactional
    public Order createOrder(Long userId, Long productId, Integer quantity) {
        // 查询商品信息，优先从缓存获取
        Product product = productCacheService.getProductById(productId);
        if (product == null || product.getStock() < quantity) {
            return null;
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setProductId(productId);
        order.setProductName(product.getName());
        order.setProductQuantity(quantity);
        order.setProductPrice(product.getPrice());
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(quantity)));
        order.setStatus(Order.STATUS_UNPAID);

        // 保存订单
        orderMapper.insert(order);

        // 发送延迟消息，30分钟后检查订单状态
        sendDelayMessage(order.getOrderNo());

        return order;
    }

    /**
     * 支付订单
     */
    @Transactional
    public boolean payOrder(String orderNo, BigDecimal amount) {
        // 查询订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || order.getStatus() != Order.STATUS_UNPAID) {
            return false;
        }
        // 验证支付金额
        if (order.getTotalAmount().compareTo(amount) != 0) {
            return false;
        }

        // 扣减库存
        boolean decreaseResult = productService.decreaseStock(order.getProductId(), order.getProductQuantity());
        if (!decreaseResult) {
            return false;
        }
        
        // 更新商品缓存
        Product updatedProduct = productService.getProductById(order.getProductId());
        if (updatedProduct != null) {
            productCacheService.updateProductCache(updatedProduct);
        }

        // 更新订单状态
        orderMapper.updateStatus(order.getId(), Order.STATUS_PAID, LocalDateTime.now());

        // 创建物流订单
        logisticsService.createLogistics(order.getId());

        return true;
    }

    /**
     * 取消订单
     */
    @Transactional
    public void cancelOrder(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || order.getStatus() != Order.STATUS_UNPAID) {
            return;
        }

        // 更新订单状态
        orderMapper.updateStatus(order.getId(), Order.STATUS_CANCELED, null);
    }

    /**
     * 根据订单号查询订单
     */
    public Order getOrderByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }

    /**
     * 查询用户的所有订单
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return orderMapper.selectByUserId(userId);
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 发送延迟消息
     */
    private void sendDelayMessage(String orderNo) {
        // 设置消息过期时间为30分钟
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(String.valueOf(30 * 60 * 1000));
                return message;
            }
        };

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_DELAY_ROUTING_KEY,
                orderNo,
                messagePostProcessor
        );
    }
}