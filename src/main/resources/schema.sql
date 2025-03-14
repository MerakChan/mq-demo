-- 商品表
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL COMMENT '商品库存',
  `version` int NOT NULL DEFAULT 0 COMMENT '版本号，用于乐观锁',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 订单表
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称',
  `product_quantity` int NOT NULL COMMENT '商品数量',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` tinyint NOT NULL COMMENT '订单状态：0-待支付，1-已支付，2-已取消，3-已完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 物流表
CREATE TABLE `logistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `logistics_no` varchar(32) NOT NULL COMMENT '物流单号',
  `status` tinyint NOT NULL COMMENT '物流状态：0-待发货，1-已发货，2-已签收',
  `address` varchar(255) NOT NULL COMMENT '收货地址',
  `receiver` varchar(50) NOT NULL COMMENT '收货人',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ship_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '签收时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_logistics_no` (`logistics_no`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流表';

-- 插入商品数据（id自增，create_time和update_time自动填充）
INSERT INTO product (name, price, stock) VALUES
                                             ('智能手机 X30 Pro', 5999.00, 1000),
                                             ('无线蓝牙耳机 AirSound', 299.00, 200),
                                             ('纯棉T恤 夏季款-白色 L', 89.90, 500),
                                             ('不锈钢保温杯 500ml-磨砂黑', 129.00, 300),
                                             ('便携式充电宝 20000mAh', 199.00, 150),
                                             ('4K超清智能电视 65英寸', 8999.00, 80),
                                             ('家用全自动咖啡机', 1599.00, 120),
                                             ('运动跑鞋 透气款-42码', 459.00, 250);