package com.jt.service;

import java.util.List;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIData;

public interface ItemService {

	EasyUIData findItemByPage(Integer page, Integer rows);

	void saveItem(Item item, ItemDesc itemDesc);

	void updateItem(Item item, ItemDesc itemDesc);

	void deleteItem(Long[] ids);

	void updateStatus(Long[] ids, Integer status);

	ItemDesc findItemDescById(Long itemId);

	Item findItemById(Long id);

	List<Item> findItemListByPage(Integer page, Integer rows);
	
	Integer getItemTableRowCount();

	List<Item> findItemListBySearchKey(String searchKey);
	
}
