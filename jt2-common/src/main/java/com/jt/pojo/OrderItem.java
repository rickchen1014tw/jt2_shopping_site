package com.jt.pojo;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;
@TableName("tb_order_item")
@Data
@Accessors(chain=true)
public class OrderItem extends BasePojo{
	
	@TableId
    private String itemId;
	
	@TableId	
    private String orderId;

    private Integer num;

    private String title;

    private Long price;

    private Long totalFee;

    private String picPath;
}

/*
CREATE TABLE `tb_order_item` (
		  `item_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品id',
		  `order_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '订单id',
		  `num` int(10) DEFAULT NULL COMMENT '商品购买数量',
		  `title` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品标题',
		  `price` bigint(50) DEFAULT NULL COMMENT '商品单价',
		  `total_fee` bigint(50) DEFAULT NULL COMMENT '商品总金额',
		  `pic_path` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片地址',
		  `created` datetime DEFAULT NULL,
		  `updated` datetime DEFAULT NULL,
		  KEY `order_id` (`order_id`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

*/