package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private ItemCatMapper itemCatMapper;
	/*
	//改用aop的方式實現緩存，所以這邊不需要這個對象
	@Autowired
	private Jedis jedis;
	*/
	
	@Override
	public String findItemCatNameById(Long itemCatId) {
		ItemCat itemCat = itemCatMapper.selectById(itemCatId);
		return itemCat.getName();
	}

	/*
	 * 1.根據parentId查詢數據庫記錄返回itemCatList集合
	 * 2.將itemCatList集合中的數據按照指定的格式封裝為List<EasyUITree>
	 */
	@Override
	public List<EasyUITree> findItemCatByParentId(Long parentId) {
		List<ItemCat> catList = findItemCatList(parentId);
		List<EasyUITree> treeList = new ArrayList<>();
		//遍歷集合數據，實現數據的轉化
		for(ItemCat itemCat : catList) {
			EasyUITree uiTree = new EasyUITree();
			uiTree.setId(itemCat.getId());
			uiTree.setText(itemCat.getName());
			//如果是父級菜單則closed，不是則open
			/*
			 * if(itemCat.getIsParent()) { 
			 *    uiTree.setState("closed");
			 * }else {
			 *    uiTree.setState("open"); 
			 * }
			 */
			String state = itemCat.getIsParent()? "closed" : "open";
			uiTree.setState(state);
			treeList.add(uiTree);
		}
		return treeList;
	}
	
	
	/* 改用AOP方式實現緩存，所以不需要這個方法了
	@Override
	public List<EasyUITree> findItemCatByParentIdInCache(Long parentId) {
		//數據查詢要先查緩存，需要一個獨一無二的key
		String key = "ITEM_CAT_" + parentId;
		String result = jedis.get(key);	
		//在這裡提前實例化treeList是因為else裡要透過treeList去得到class物件
		List<EasyUITree> treeList = new ArrayList<>();
		if(StringUtils.isEmpty(result)) {
			//如果為null，查詢數據庫
			treeList = findItemCatByParentId(parentId);
			//將數據轉化為JSON
			String json = ObjectMapperUtil.toJSON(treeList);
			jedis.setex(key, 7*24*3600, json);	//設定失效時間 7天
			System.out.println("業務查詢數據庫!");
		}else {
			//緩存中有數據
			treeList = ObjectMapperUtil.toObject(result, treeList.getClass());
			System.out.println("業務查詢Redis緩存!");
		}
		
		return treeList;
	}
	*/
	
	//根據父級id查詢子集集合
	public List<ItemCat> findItemCatList(Long parentId){
		
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}


}
