package com.jt.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

//後台接收用戶請求獲取商品信息
//url: manage.jt.com/web/item/findItemById?id=562379  我們自己訂的url
@RestController
@RequestMapping("/web/item")
public class WebItemController {

	@Autowired
	private ItemService itemService;
	
	//manage.jt.com/web/item/findItemById?id=562379
	@RequestMapping("/findItemById")
	@Cache_Find(key = "ITEM", keyType = KEY_ENUM.AUTO, seconds = 60*60*12)
	public Item findItemById(Long id) {
		
		return itemService.findItemById(id);
	}
	
	@RequestMapping("/findItemDescById")
	@Cache_Find(key = "ITEM_DESC", keyType = KEY_ENUM.AUTO, seconds = 60*60*12)
	public ItemDesc findItemDescById(Long id) {
		
		return itemService.findItemDescById(id);
	}
	
	@RequestMapping("/findItemListByPage")
	public List<Item> findItemListByPage(Integer page, Integer rows) {
	
		return itemService.findItemListByPage(page, rows);
	}
	
	@RequestMapping("/findItemListBySearchKey")
	public List<Item> findItemListBySearchKey(String searchKey) {
	
		//System.out.println("findItemListBySearchKey(" + searchKey + ")");
		return itemService.findItemListBySearchKey(searchKey);
	}
	
	@RequestMapping("/getItemTableRowCount")
	public Integer getItemTableRowCount() {
		
		return itemService.getItemTableRowCount();
	}
}
