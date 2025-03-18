package com.merak.mqdemo.controller;

import com.merak.mqdemo.entity.Product;
import com.merak.mqdemo.service.ProductCacheService;
import com.merak.mqdemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductCacheService productCacheService;

    // 商品列表页面   ---展示所有商品
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productCacheService.getAllProducts();
        model.addAttribute("products", products);
        return "product/list";
    }
}