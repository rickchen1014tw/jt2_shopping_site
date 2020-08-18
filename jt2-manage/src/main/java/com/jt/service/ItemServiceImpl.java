package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemCat;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIData;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUIData findItemByPage(Integer page, Integer rows) {
		//1.獲取商品記錄總數
		int total = itemMapper.selectCount(null);
		
		//2.分頁之後的回傳數據
		/**
		 * sql: select * from tb_item limit 起始位置,查询行数
		 * 第1页:  20
		 * 	select * from tb_item limit 0,20
		 * 第2页:  
		 * 	select * from tb_item limit 20,20
		 * 第3页:
		 *  select * from tb_item limit 40,20
		 * 第N页:
		 * 	 select * from tb_item 
		 * 			limit (page-1)rows,rows
		 */
		//分頁也可以用mybatis-plus來完成，但我們這邊先不用，自己寫
		//計算起始位置
		int start = (page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		
		return new EasyUIData(total,itemList);
	}

	@Transactional//添加事務控制
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1)
			.setCreated(new Date())
			.setUpdated(item.getCreated());
		//mybatis中如果設定主鍵自增，則新增完成時id會自動回傳賦值給對象
		//insert into xxx values(xxx) 
		//sql:SELECT LAST_INSERT_ID(); 之後將數據交給對象
		itemMapper.insert(item);
		//同時入庫兩張表
		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}
	
	//根据主键更新
	/**
	 * propagation 事務傳播屬性
	 * 			       默認值為REQUIRED  必須添加事務 事務合併
	 * 			   A()有事務控制，B()也有事務控制，A()調用B()，則B()併入A()的事務
	 * 			   REQUIRES_NEW   必須新建一個事務 
	 *             A()有事務控制，B()也有事務控制，A()調用B()，則新建一個事務，少用
	 * 			   SUPPORTS		   表示事務支持的 查詢之前有事務時則合併事務
	 *             
	 * Spring中默認的事務控制策略:
	 * 	1.檢查異常/編譯異常 不負責事務控制
	 * 	2.運行時異常 /error 回滾事務
	  * 我們一般把檢查異常轉化成運行時異常再拋出
	 * 
	 * @Transactional(rollbackFor = "異常類型")
	  *  按照指定的異常回滾事務
	 * rollbackFor = "異常的類型" 按照指定的異常回滾事務
	 * noRollbackFor = "異常的類型" 指定不回滾的異常
	 * readOnly = true 不允許修改數據庫 只讀
	 * 
	 */
	@Transactional
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		//因為tb_item和tb_item_desc是一對一關聯關係，所以需要同時維護
		//同時修改2張表數據
		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Transactional
	@Override
	public void deleteItem(Long[] ids) {
		//1.手動寫刪除方法
		//itemMapper.deleteItem(ids);
		//2.利用Mybatis-plus 批量刪除
		List<Long> itemList = Arrays.asList(ids);
		//2張表一起刪除
		itemMapper.deleteBatchIds(itemList);
		itemDescMapper.deleteBatchIds(itemList);
	}
	
	/*
	 * sql: update tb_item 
	 * 		set status=#{status},
	 * 		updated = #{updated} 
	 * 		where id in (100,200,300....)
	 * */
	@Override
	@Transactional
	public void updateStatus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status)
			.setUpdated(new Date()); //要修改的數據
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>(); //where條件
		List<Long> longIds = Arrays.asList(ids);
		updateWrapper.in("id", longIds);
		itemMapper.update(item, updateWrapper);
	}

	@Override
	public Item findItemById(Long id) {
		
		return itemMapper.selectById(id);
	}
	
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public List<Item> findItemListByPage(Integer page, Integer rows) {
		int start = (page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);	
		return itemList;
	}

	@Override
	public Integer getItemTableRowCount() {
		Integer rowCount = itemMapper.selectCount(null);
		return rowCount;
	}

	@Override
	public List<Item> findItemListBySearchKey(String searchKey) {
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("title", searchKey);
		List<Item> itemList = itemMapper.selectList(queryWrapper);
		return itemList;
	}
	
}
