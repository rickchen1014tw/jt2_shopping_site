package com.jt.pojo;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;
@TableName("tb_order_shipping")
@Data
@Accessors(chain=true)
public class OrderShipping extends BasePojo{
	
	@TableId
    private String orderId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverState;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;
    
}

/*
CREATE TABLE `tb_order_shipping` (
		  `order_id` varchar(50) NOT NULL COMMENT '订单ID',
		  `receiver_name` varchar(20) DEFAULT NULL COMMENT '收货人全名',
		  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '固定电话',
		  `receiver_mobile` varchar(30) DEFAULT NULL COMMENT '移动电话',
		  `receiver_state` varchar(10) DEFAULT NULL COMMENT '省份',
		  `receiver_city` varchar(10) DEFAULT NULL COMMENT '城市',
		  `receiver_district` varchar(20) DEFAULT NULL COMMENT '区/县',
		  `receiver_address` varchar(200) DEFAULT NULL COMMENT '收货地址，如：xx路xx号',
		  `receiver_zip` varchar(6) DEFAULT NULL COMMENT '邮政编码,如：310001',
		  `created` datetime DEFAULT NULL,
		  `updated` datetime DEFAULT NULL,
		  PRIMARY KEY (`order_id`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8
*/