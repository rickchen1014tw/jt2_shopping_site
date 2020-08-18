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
public class IndexController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/")
	public String IndexPage(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		Integer rowCount = itemService.getItemTableRowCount();
		Integer pageTotal = rowCount/6 + 1;
		model.addAttribute("pageTotal", pageTotal);
		
		List<Item> itemList = itemService.findItemListByPage(page, 6);
		model.addAttribute("itemList", itemList); 
		
		Integer pageIndex = page;
		model.addAttribute("pageIndex", pageIndex);
		
		return "index";
	}
	
}
