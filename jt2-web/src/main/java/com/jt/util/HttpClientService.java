package com.jt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HttpClientService {

	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private RequestConfig requestConfig;

	/**
	 * 構建get請求工具類
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * 目的: 利用用戶傳遞url，幫助用戶發起http get請求
	 * 是否有參數: 沒有參數: http://manage.jt.com/xxx
	 *            有參數        http://manage.jt.com?key1=value1&key2=value2&....
	 *最後利用httpClient發起請求返回結果即可        
	 */
	public String doGet(String url, Map<String,String> params, String charset) {
		//校驗url 這邊省略
		//1.校驗字符集編碼格式 這裡可以用正則表達式去校驗指定哪個字符集
		if(StringUtils.isEmpty(charset)) {
			//表式用戶沒有指定字符類型，指定默認值
			charset = "UTF-8";
		}
		//2.校驗Map集合是否為null
		if(params != null) {
			url = url + "?";
			for(Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				url = url + key + "=" + value + "&";
			}
			//經過循環結束後把多出的&符拿掉
			url = url.substring(0, url.length()-1);	
		}
		
		/*
		if(params != null){
			try {
				URIBuilder uriBuilder = new URIBuilder(url);
				for (Map.Entry<String,String> entry : params.entrySet()) {

					uriBuilder.addParameter(entry.getKey(), entry.getValue());
				}
				//url?id=1&name=tom
				url = uriBuilder.build().toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/

		//3.定義請求對象
		HttpGet httpGet = new HttpGet(url);
		//設定超時時間
		httpGet.setConfig(requestConfig);
		String result = null;
		try {	//用try/catch而不直接拋出，不然之後程式碼使用這個工具類都必須去處理異常，不方便使用
			//4.發起http請求獲取response對象
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			//5.判斷返回值
			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity(), charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);	//轉成運行時異常
		}

		return result;
	}

	public String doGet(String url) {
		return doGet(url, null, null);
	}

	public String doGet(String url, Map<String,String> params) {
		return doGet(url, params, null);
	}


	//实现httpClient POST提交
	public String doPost(String url,Map<String,String> params,String charset){
		String result = null;
		//1.定义请求类型
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);  	//定义超时时间
		//2.判断字符集是否为null
		if(StringUtils.isEmpty(charset)){
			charset = "UTF-8";
		}
		//3.判断用户是否传递参数
		if(params !=null){
			//3.2准备List集合信息
			List<NameValuePair> parameters = 
					new ArrayList<>();
			//3.3将数据封装到List集合中
			for (Map.Entry<String,String> entry : params.entrySet()) {
				//NameValuePair是一個接口，ctrl+T可以找到有一個實現類BasicNameValuePair
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			//3.1模拟表单提交
			try {
				//form表單用post請求，提交的時候將參數key/value封裝成一個form的結構再進行提交
				//這裡也把參數封裝到一個form對象裡
				UrlEncodedFormEntity formEntity = 
						new UrlEncodedFormEntity(parameters,charset); //采用u8编码
				//3.4将实体对象封装到请求对象中
				post.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		//4.发送请求
		try {
			CloseableHttpResponse response = httpClient.execute(post);
			//4.1判断返回值状态
			if(response.getStatusLine().getStatusCode() == 200) {
				//4.2表示请求成功
				result = EntityUtils.toString(response.getEntity(),charset);
			}else{
				System.out.println("获取状态码信息:"+response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);	//轉成運行時異常
		}

		return result;
	}

	public String doPost(String url){
		return doPost(url, null, null);
	}

	public String doPost(String url,Map<String,String> params){
		return doPost(url, params, null);
	}

	public String doPost(String url,String charset){
		return doPost(url, null, charset);
	}
}
