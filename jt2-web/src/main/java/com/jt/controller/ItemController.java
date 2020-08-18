package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.Service.ItemService;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	/**
	 * 根據商品id查詢後台服務器數據
	 * 從url中動態的取值
	 * 業務步驟:
	 * 1.在前台service層中實現httpClient調用
	 * 2.後台根據itemId查詢數據庫返回對象的json串
	 * 3.service層將返回的json轉化為item對象
	 * 4.將item對象保存到request域中
	 * 5.返回頁面邏輯名稱item
	 */
	//http://www.jt.com/items/562379.html
	@RequestMapping("/items/{itemId}")
	public String findItemById(@PathVariable Long itemId, Model model) {
		Item item = itemService.findItemById(itemId);
		model.addAttribute("item", item);	//加到域中
		//商品和商品詳情是同一個業務，寫在同一個業務層即可，不需要拆開
		//(X) ItemDesc itemDesc = itemDescService.findItemDescById(itemId);
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
