package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_cart")
public class Cart extends BasePojo {
	@TableId(type = IdType.AUTO)
	private Long id;
	private Long userId;	//userID和itemId表示用戶信息
	private Long itemId;
	private String itemTitle;
	private String itemImage;	//保存商品的第一張圖片信息
	private Long itemPrice;
	private Integer num;
}

/*
CREATE TABLE `tb_cart` (
		  `id` bigint(20) NOT NULL AUTO_INCREMENT,
		  `user_id` bigint(20) DEFAULT NULL,
		  `item_id` bigint(20) DEFAULT NULL,
		  `item_title` varchar(100) DEFAULT NULL,
		  `item_image` varchar(200) DEFAULT NULL,
		  `item_price` bigint(20) DEFAULT NULL COMMENT '单位：分',
		  `num` int(10) DEFAULT NULL,
		  `created` datetime DEFAULT NULL,
		  `updated` datetime DEFAULT NULL,
		  PRIMARY KEY (`id`),
		  KEY `AK_user_itemId` (`user_id`,`item_id`)
		) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8

user_id和item_id是聯合主鍵 一個用戶對同一個商品的購物行為在數據庫裡只會有一條數據
一個用戶對同一個商品繼續購物，只會改變數量
聯合主鍵一般我們不會直接定義在數據庫的表當中，通常只會定義主鍵，不會定義其它如主外鍵之類的關係，
而是程序員自己透過業務的方式自己維護
*/
