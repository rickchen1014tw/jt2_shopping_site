package com.jt.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private HttpClientService httpClient;

	@Override
	public Item findItemById(Long itemId) {
		//http://manage.jt.com/web/item/findItemById?id=1474391964
		String url = "http://manage.jt.com/web/item/findItemById";
		//String url = "http://productmanage.nctu.me/web/item/findItemById";
		//為了滿足get請求需求 定義id=xxx
		Map<String,String> params = new HashMap<>();
		params.put("id", itemId + "");
		String result = httpClient.doGet(url, params);
		Item item = ObjectMapperUtil.toObject(result, Item.class);
		return item;
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		//http://manage.jt.com/web/item/findItemDescById?id=1150413
		String url = "http://manage.jt.com/web/item/findItemDescById";
		//String url = "http://productmanage.nctu.me/web/item/findItemDescById";
		//為了滿足get請求需求 定義id=xxx
		Map<String,String> params = new HashMap<>();
		params.put("id", itemId + "");
		String result = httpClient.doGet(url, params);
		ItemDesc itemDesc = ObjectMapperUtil.toObject(result, ItemDesc.class);
		return itemDesc;
	}

	@Override
	public List<Item> findItemListByPage(int page, int rows) {
		String url = "http://manage.jt.com/web/item/findItemListByPage";
		//String url = "http://productmanage.nctu.me/web/item/findItemListByPage";
		Map<String,String> params = new HashMap<>();
		params.put("page", page + "");
		params.put("rows", rows + "");
		
		List<Item> itemList = new ArrayList<>();
		String result = httpClient.doGet(url, params);
		itemList = ObjectMapperUtil.toObject(result, itemList.getClass());
		
		return itemList;
	}
	
	@Override
	public List<Item> findItemListBySearchKey(String searchKey) {
		String url = "http://manage.jt.com/web/item/findItemListBySearchKey";
		//String url = "http://productmanage.nctu.me/web/item/findItemListBySearchKey";
		Map<String,String> params = new HashMap<>();
		params.put("searchKey", searchKey + "");
		
		List<Item> itemList = new ArrayList<>();
		String result = httpClient.doPost(url, params);
		itemList = ObjectMapperUtil.toObject(result, itemList.getClass());
		
		return itemList;
	}
	
	
	public Integer getItemTableRowCount() {
		String url = "http://manage.jt.com/web/item/getItemTableRowCount";
		//String url = "http://productmanage.nctu.me/web/item/getItemTableRowCount";
		
		String result = httpClient.doGet(url);
		Integer rowCount = ObjectMapperUtil.toObject(result, Integer.class);
		return rowCount;
	}
	
}
