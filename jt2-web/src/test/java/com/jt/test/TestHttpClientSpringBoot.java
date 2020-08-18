package com.jt.test;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;

//想測試Spring容器裡面的對象，先要讓Spring容器啟動，加上@SpringBootTest跟@RunWith
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClientSpringBoot {

	//1.獲取HttpClient對象
	@Autowired
	private CloseableHttpClient client;
	
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		//2.定義訪問url
		String url = "https://www.baidu.com";
		//3.設定請求方式
		HttpGet httpGet = new HttpGet(url);
		//4.發起http請求獲取response對象
		CloseableHttpResponse response = client.execute(httpGet);
		//5.判斷返回值
		if(response.getStatusLine().getStatusCode() == 200) {
			System.out.println("請求成功");
			//6.獲取頁面信息
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			System.out.println(result);
		}else {
			System.out.println("請求失敗");
			throw new RuntimeException("請求失敗");
		}
	}
	
	@Autowired
	private HttpClientService httpClientService;
	
	//測試工具API
	@Test
	public void testUtil() {
		String url = "https://www.baidu.com";
		String result = httpClientService.doGet(url);
		System.out.println("獲取結果: " + result);
	}
}
