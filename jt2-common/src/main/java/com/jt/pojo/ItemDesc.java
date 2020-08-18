package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_item_desc")
@Data
@Accessors(chain = true)
public class ItemDesc extends BasePojo {
	@TableId
	private Long itemId;
	private String itemDesc;
}


/*
CREATE TABLE `tb_item_desc` (
  `item_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `item_desc` text DEFAULT NULL COMMENT '商品描述',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '更新时间',
  KEY `item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品描述表'

*/