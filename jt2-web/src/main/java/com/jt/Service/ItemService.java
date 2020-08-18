package com.jt.Service;

import java.util.List;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public interface ItemService {

	Item findItemById(Long itemId);

	ItemDesc findItemDescById(Long itemId);

	List<Item> findItemListByPage(int page, int rows);
	
	public List<Item> findItemListBySearchKey(String searchKey);
	
	public Integer getItemTableRowCount();

}
