package com.merak.mqdemo.mapper;

import com.merak.mqdemo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    Product selectById(Long id);
    
    List<Product> selectAll();
    
    int insert(Product product);
    
    int updateStock(@Param("id") Long id, 
                    @Param("stock") Integer stock, 
                    @Param("version") Integer version);
} 