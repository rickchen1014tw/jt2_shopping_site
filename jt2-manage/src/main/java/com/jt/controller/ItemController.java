package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUIData;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	//http://localhost:8091/item/query?page=1&rows=20
	//查詢商品列表信息 分頁查詢
	@RequestMapping("/query")
	public EasyUIData findItemByPage(Integer page,Integer rows) {
		
		return itemService.findItemByPage(page,rows);
	}
	
	//itemDesc=<div>頁面信息...</div>
	//只要pojo對象的屬性不重複，就可以用不同的對象來接收參數
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		try {
			//實現數據新增
			itemService.saveItem(item,itemDesc);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	//修改商品
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		try {
			itemService.updateItem(item,itemDesc);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail("商品修改失败");
		}
	}
	
	/**
	 * 1.刪除商品信息
	 * url地址:/item/delete
	 *    參數: {ids:ids}   
	 * ids:1,2,3,4,5
	 * public SysResult deleteItem(String ids) { 用String來接ids
	 * 	String[] strIds = ids.split(",");
	 *  String再轉成Long
	 *   上面的步驟SpringMVC幫我們做了，可以直接用Long[]來接
	 */
	@RequestMapping("/delete")
	public SysResult deleteItem(Long[] ids) {
		try {
			itemService.deleteItem(ids);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	//實現商品下架
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		try {
			int status = 2;	//下架
			itemService.updateStatus(ids,status);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		try {
			int status = 1;	//上架
			itemService.updateStatus(ids,status);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	//根據itemId查詢商品詳情信息
	
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		try {
			ItemDesc itemDesc = itemService.findItemDescById(itemId);
			return SysResult.ok(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}	
	
}
