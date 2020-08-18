package com.jt.util;

import com.jt.pojo.User;

//ThreadLocal是線程安全的
public class UserThreadLocal {
	/**
	 * 如何存多個數據在thread-local variable?? 利用Map集合
	 * 定義ThreadLocal<Map<K,V>>
	 */

	private static ThreadLocal<User> threadLocal = new ThreadLocal<>();
	
	//利用ThreadLocal工具類新增數據到這個線程的thread-local variable
	public static void set(User user) {
		threadLocal.set(user);
	}
	
	//利用ThreadLocal工具類從這個線程的thread-local variable獲取數據
	public static User get() {
		return threadLocal.get();
	}
	
	//使用ThreadLocal切記關閉 防止內存泄漏 gc回收慢
	public static void remove() {
		threadLocal.remove();
	}
}
