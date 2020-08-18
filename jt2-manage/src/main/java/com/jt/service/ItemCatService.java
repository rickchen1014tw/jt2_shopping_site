package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUITree;

public interface ItemCatService {

	String findItemCatNameById(Long itemCatId);

	List<EasyUITree> findItemCatByParentId(Long parentId);

	//改用AOP方式實現緩存，所以不需要這個方法了
	//List<EasyUITree> findItemCatByParentIdInCache(Long parentId);

}
