package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jt.enu.KEY_ENUM;

//定義一個緩存查詢的註解
@Target(ElementType.METHOD)	//註解的作用範圍
@Retention(RetentionPolicy.RUNTIME)	//程序運行時有效
public @interface Cache_Find {
	String key() default "";	//接收用戶自定義的key值，默認值空字串
	KEY_ENUM keyType() default KEY_ENUM.AUTO;	//定義key的類型，默認值自動，用key拼接第一個參數
	int seconds() default 0;	//0永不失效
}
