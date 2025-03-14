package com.merak.mqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // 订单延迟队列
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";
    // 订单处理队列
    public static final String ORDER_PROCESS_QUEUE = "order.process.queue";
    // 订单交换机
    public static final String ORDER_EXCHANGE = "order.exchange";
    // 订单路由键
    public static final String ORDER_ROUTING_KEY = "order.routing.key";
    // 订单延迟路由键
    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay.routing.key";

    // 声明交换机
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // 声明延迟队列
    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", ORDER_EXCHANGE);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", ORDER_ROUTING_KEY);
        return new Queue(ORDER_DELAY_QUEUE, true, false, false, args);
    }

    // 声明处理队列
    @Bean
    public Queue orderProcessQueue() {
        return new Queue(ORDER_PROCESS_QUEUE, true);
    }

    // 绑定延迟队列到交换机
    @Bean
    public Binding orderDelayBinding() {
        return BindingBuilder.bind(orderDelayQueue())
                .to(orderExchange())
                .with(ORDER_DELAY_ROUTING_KEY);
    }

    // 绑定处理队列到交换机
    @Bean
    public Binding orderProcessBinding() {
        return BindingBuilder.bind(orderProcessQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }

    // 配置消息转换器
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
} 