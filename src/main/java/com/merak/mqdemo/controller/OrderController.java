package com.merak.mqdemo.controller;

import com.merak.mqdemo.entity.Logistics;
import com.merak.mqdemo.entity.Order;
import com.merak.mqdemo.entity.Product;
import com.merak.mqdemo.service.LogisticsService;
import com.merak.mqdemo.service.OrderService;
import com.merak.mqdemo.service.ProductCacheService;
import com.merak.mqdemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

//订单页面
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductCacheService productCacheService;
    private final LogisticsService logisticsService;

    /**
     * 创建订单页面
     */
    @GetMapping("/create")
    public String createOrderPage(@RequestParam Long productId, Model model) {
        Product product = productCacheService.getProductById(productId);
        model.addAttribute("product", product);
        return "order/create";
    }

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public String createOrder(@RequestParam Long productId,
                            @RequestParam Integer quantity,
                            Model model) {
        // 模拟用户ID
        Long userId = 1L; //实际应用中，用户ID通常从当前登录用户的会话中获取。
        Order order = orderService.createOrder(userId, productId, quantity);
        
        if (order != null) {
            return "redirect:/orders/" + order.getOrderNo();
        } else {
            model.addAttribute("error", "创建订单失败，库存不足");
            return "redirect:/products";
        }
    }

    /**
     * 支付订单
     */
    @PostMapping("/{orderNo}/pay")
    @ResponseBody
    public String payOrder(@PathVariable String orderNo,
                         @RequestParam BigDecimal amount) {
        boolean success = orderService.payOrder(orderNo, amount);
        return success ? "支付成功" : "支付失败";
    }


    /**
     * 订单列表
     */
    @GetMapping("/my")
    public String myOrders(Model model) {
        // 模拟用户ID
        Long userId = 1L;
        model.addAttribute("orders", orderService.getOrdersByUserId(userId));
        return "order/list";
    }

    /**
     * 订单详情页面
     */
    @GetMapping("/{orderNo}")
    public String orderDetail(@PathVariable String orderNo, Model model) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        if (order == null) {
            return "redirect:/products";
        }

        Product product = productCacheService.getProductById(order.getProductId());
        Logistics logistics = null;
        if (order.getStatus() == Order.STATUS_PAID) {
            logistics = logisticsService.getLogisticsByOrderId(order.getId());
        }

        model.addAttribute("order", order);
        model.addAttribute("product", product);
        model.addAttribute("logistics", logistics);
        return "order/detail";
    }

}