package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_user")
public class User extends BasePojo {
	@TableId(type = IdType.AUTO)
	private Long id;
	private String username;
	private String password;
	private String phone;
	private String email;
}
/*
CREATE TABLE `tb_user` (
		  `id` bigint(20) NOT NULL AUTO_INCREMENT,
		  `username` varchar(50) DEFAULT NULL,
		  `password` varchar(32) DEFAULT NULL COMMENT 'MD5加密',
		  `phone` varchar(20) DEFAULT NULL,
		  `email` varchar(50) DEFAULT NULL,
		  `created` datetime DEFAULT NULL,
		  `updated` datetime DEFAULT NULL,
		  PRIMARY KEY (`id`),
		  UNIQUE KEY `username` (`username`),
		  UNIQUE KEY `phone` (`phone`),
		  UNIQUE KEY `email` (`email`)
		) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8

*/