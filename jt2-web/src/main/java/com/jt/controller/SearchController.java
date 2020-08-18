package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jt.Service.ItemService;
import com.jt.pojo.Item;

@Controller
public class SearchController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/search")
	public String IndexPage(String searchKey, Model model) {
		System.out.println(searchKey);
		List<Item> itemList = itemService.findItemListBySearchKey(searchKey);
		if(!itemList.isEmpty()) {
			model.addAttribute("itemList", itemList);
		}else {
			model.addAttribute("searchKey", searchKey);
		}
		return "search";
	}
	
	

}
