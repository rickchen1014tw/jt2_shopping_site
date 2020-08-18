package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

//工具類實現對象與json轉化
public class ObjectMapperUtil {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static String toJSON(Object target) {
		String json = null;
		try {
			json = MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			//將檢查異常轉化為運行時異常
			//(這裡捕獲後caller會看不到異常，所以把checked exception轉成runtime exception再拋出)
			/* Spring中默認的事務控制策略:
			   1.檢查異常/編譯異常 不負責事務控制
			   2.運行時異常 /error 回滾事務
			      我們一般把檢查異常轉化成運行時異常再拋出
			      這裡拋出runtime exception後，若上層有事務控制就會進行回滾
			*/
			throw new RuntimeException();
		}
		return json;
	}
	
	public static <T> T toObject(String json, Class<T> targetClass) {
		T target = null;
		try {
			target = MAPPER.readValue(json, targetClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return target;
	}
}
