package com.jt.pojo;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_order")
@Data
@Accessors(chain=true)
public class Order extends BasePojo{
	//這兩個屬性是為了封裝數據方便而多引進來的，tb_order表中並沒有這兩個屬性，如果沒有做任何的處理，
	//將來入庫的時候，這兩個屬性也會參於入庫的操作，入庫tb_order表的時候就會報錯
	@TableField(exist=false)	//入库操作忽略该字段 tb_order表裡沒有這個字段，如果沒有設定入庫時會報錯
	private OrderShipping orderShipping;
								//封装订单商品信息  一对多
	@TableField(exist=false)	//入库操作忽略该字段
	private List<OrderItem> orderItems;
	
	//為了併發條件下，設主鍵自增可能會有問題，這裡主鍵不是設為自增而是設為user id+時間戳
	@TableId	//標示主鍵，這裡主鍵不是主鍵自增，是用userid+時間戳
    private String orderId;	//訂單id
    private String payment;	//實付金額
    private Integer paymentType; //支付類型 1.在線支付 2.貨到付款
    private String postFee;	//郵費
    private Integer status;	//狀態 1.未付款 2.已付款 3.未發貨 4.已發貨 5.交易成功 6.交易關閉
    private Date paymentTime;	//付款時間
    private Date consignTime;	//發貨時間
    private Date endTime;	//交易完成時間
    private Date closeTime;	//交易關閉時間
    private String shippingName;	//物流名稱
    private String shippingCode;	//物流單號
    private Long userId;	//用戶id
    private String buyerMessage;	//買家流言
    private String buyerNick;	//買家暱稱
    private Integer buyerRate;	//買家是否已經評價

}

/*
CREATE TABLE `tb_order` (
		  `order_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '订单id',
		  `payment` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
		  `payment_type` int(2) DEFAULT NULL COMMENT '支付类型，1、在线支付，2、货到付款',
		  `post_fee` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
		  `status` int(10) DEFAULT NULL COMMENT '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
		  `created` datetime DEFAULT NULL COMMENT '订单创建时间',
		  `updated` datetime DEFAULT NULL COMMENT '订单更新时间',
		  `payment_time` datetime DEFAULT NULL COMMENT '付款时间',
		  `consign_time` datetime DEFAULT NULL COMMENT '发货时间',
		  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
		  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
		  `shipping_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流名称',
		  `shipping_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流单号',
		  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
		  `buyer_message` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '买家留言',
		  `buyer_nick` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '买家昵称',
		  `buyer_rate` int(2) DEFAULT NULL COMMENT '买家是否已经评价',
		  UNIQUE KEY `order_id` (`order_id`) USING BTREE,
		  KEY `create_time` (`created`),
		  KEY `buyer_nick` (`buyer_nick`),
		  KEY `status` (`status`),
		  KEY `payment_type` (`payment_type`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
		
為了併發條件下，設主鍵自增可能會有問題，這裡主鍵不是設為自增而是設為user id+時間戳
訂單表跟訂單商品表是一對多關係
訂單表和訂單物流表是一對一關係
這三張表是透過order_id字段來進行關聯的
*/

/*
點擊提交訂單的post請求 
參數
Form Data
paymentType: 1
orderItems[0].itemId: 1474391969
orderItems[0].num: 1
orderItems[0].price: 8990000
orderItems[0].totalFee: 8990000
orderItems[0].title: MacBook Pro 16 銀色 8核心第九代 i9 / 1TB / 2.3GHz / 16GB
orderItems[0].picPath: http://image.jt.com/2020/03/31/a5211275117341d397cf0d6d32d534ac.jpg
orderItems[1].itemId: 562379
orderItems[1].num: 1
orderItems[1].price: 4299000
orderItems[1].totalFee: 4299000
orderItems[1].title: 三星 W999 黑色 电信3G手机 双卡双待双通
orderItems[1].picPath: http://image.taotao.com/jd/d2ac340e728d4c6181e763e772a9944a.jpg
orderItems[2].itemId: 1474391935
orderItems[2].num: 1
orderItems[2].price: 4680
orderItems[2].totalFee: 4680
orderItems[2].title: 990本色（精装图文版）真我非我
orderItems[2].picPath: http://image.jt.com/images/2015/05/20/2015052011005507304512.jpg
payment: 132936.80
orderShipping.receiverName: 陈晨
orderShipping.receiverMobile: 13800807944
orderShipping.receiverState: 北京
orderShipping.receiverCity: 北京
orderShipping.receiverDistrict: 海淀区
orderShipping.receiverAddress: 清华大学

*/