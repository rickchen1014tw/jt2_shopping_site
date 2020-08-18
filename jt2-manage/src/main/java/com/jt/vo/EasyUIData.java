package com.jt.vo;
//這個類用來展現表格數據

import java.io.Serializable;

import java.util.List;

import com.jt.pojo.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class EasyUIData implements Serializable{
	private Integer total;	//總記錄數
	private List<Item> rows;	//展示的數據集合
}

/*
頁面需要如下的數據:
{
	"total":2000,
	"rows":[
		{"code":"A","name":"果汁","price":"20"},
		{"code":"B","name":"汉堡","price":"30"},
		{"code":"C","name":"鸡柳","price":"40"},
		{"code":"D","name":"可乐","price":"50"},
		{"code":"E","name":"薯条","price":"10"},
		{"code":"F","name":"麦旋风","price":"20"},
		{"code":"G","name":"套餐","price":"100"}
	]
}
 */





