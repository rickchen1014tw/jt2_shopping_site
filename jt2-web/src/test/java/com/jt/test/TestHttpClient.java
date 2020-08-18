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

public class TestHttpClient {

		/**
		 * 測試HttpClient
		 * 
		 * 1.實例化HttpClient對象
		 * 2.定義http請求路徑url/uri
		 * 3.定義請求方式 get/post
		 * 4.利用API發起http請求
		 * 5.獲取返回值以後判斷狀態信息 200
		 * 6.獲取響應數據
		 * @throws IOException 
		 * @throws ClientProtocolException 
		 */
		@Test
		public void testGet() throws ClientProtocolException, IOException {
			//1.獲取HttpClient對象
			CloseableHttpClient client = HttpClients.createDefault();
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
}
