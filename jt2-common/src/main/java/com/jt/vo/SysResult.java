package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//系統返回值對象
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
	private Integer status;	//200表示成功 201表示失敗
	private String msg;	//後台返回值數據提示信息
	private Object data;	//後台返回的任意數據
	
	public static SysResult ok() {
		return new SysResult(200, null, null);
	}
	
	public static SysResult ok(Object data) {
		return new SysResult(200, null, data);
	}
	
	/* 假如:用戶需要回傳一個data，data數據類型是String，會調到這個方法，而不是上面的方法
	 *    所以這裡不能定義這個方法，不然會產生岐義 
	public static SysResult OK(String msg) {
		return new SysResult(200, msg, null);
	}*/
	
	public static SysResult ok(String msg, Object data) {
		return new SysResult(200, msg, data);
	}
	
	public static SysResult fail() {
		return new SysResult(201, null, null);
	}
	
	public static SysResult fail(String msg) {
		return new SysResult(201, msg, null);
	}
}
