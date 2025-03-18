package com.merak.mqdemo.service;

import com.merak.mqdemo.entity.Product;
import com.merak.mqdemo.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductMapper productMapper;

    /**
     * 获取单个商品信息（直接从数据库获取被缓存服务调用）
     */
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }


    /**
     * 获取所有商品
     */
    public List<Product> getAllProducts() {
        return productMapper.selectAll();
    }


    /**
     * 扣减库存，使用乐观锁控制并发
     */
    public boolean decreaseStock(Long productId, Integer quantity) {
        // 查询商品
        Product product = productMapper.selectById(productId);
        if (product == null) {
            return false;
        }
        
        // 检查库存是否足够
        if (product.getStock() < quantity) {
            return false;
        }
        
        // 更新库存，使用乐观锁
        int result = productMapper.updateStock(productId, product.getStock() - quantity, product.getVersion());
        return result > 0;
    }
}