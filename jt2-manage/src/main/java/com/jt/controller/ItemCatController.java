package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	//實現根據id查詢商品分類信息
	/* common.js
    	$.ajax({
    		type:"post",
    		url:"/item/cat/queryItemName",
    		data:{itemCatId:val},	//代表參數
    		cache:true,    //缓存
    		async:false,    //表示同步  默認是true 異步
    		dataType:"text",//表示返回值参数类型
    		success:function(data){
        		name = data;
        	}
    	});
	 */
	/**
	 * 1.用戶發起post請求攜帶了itemCatId=560
	 * 2.Spring-MVC底層是servlet，servlet request response
	 * 	 底層會用request.getParameter("itemCatId");來取值
	 *   public String findItemCatNameById(HttpServletRequest request) {
	 *   	request.getParameter("itemCatId");
	 * 	 所以參數的名字要跟請求的名字一樣為itemCatId
	 * @return
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		
		return itemCatService.findItemCatNameById(itemCatId);
	}
	
	//查詢全部數據的商品分類信息
	//http://localhost:8091/item/cat/list   請求參數id=56
	//需要獲取任意名稱的參數，為指定的參數賦值
	//@RequestParam	value/name 接收參數名稱	defaultValue="默認值"	required=true/false 是否必須傳值
	@RequestMapping("/list")
	@Cache_Find(key = "ITEM_CAT", keyType = KEY_ENUM.AUTO)
	public List<EasyUITree> findItemCatByParentId(@RequestParam(value = "id", defaultValue = "0") Long parentId){
		
		//if(parentId == null)
		//	parentId = 0;
		return itemCatService.findItemCatByParentId(parentId);
		
		//從Redis的緩存取
		//return itemCatService.findItemCatByParentIdInCache(parentId);
	}
}
