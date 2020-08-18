package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITree {
	private Long id;		//節點id值
	private String text;	//名稱
	private String state;	//closed,open
}

/* tree.json:
[
	{
		"id":"1",
		"text":"英雄联盟",
		"iconCls":"icon-save",
		"children":[
			{
				"id":"4",
				"text":"沙漠死神"
			},{
				"id":"5",
				"text":"德玛西亚"
			},{
				"id":"6",
				"text":"诺克萨斯之手"
			},
			{
				"id":"7",
				"text":"蛮族之王"
			},
			{
				"id":"8",
				"text":"孙悟空"
			}
		],
		"state":"open"
	},{
		"id":"2",
		"text":"王者荣耀",
		"children":[
			{
				"id":"10",
				"text":"阿科"
			},{
				"id":"11",
				"text":"吕布"
			},{
				"id":"12",
				"text":"陈咬金"
			},{
				"id":"13",
				"text":"典韦"
			}
		],
		"state":"closed"
	},
	{
		"id":"3",
		"text":"吃鸡游戏",
		"iconCls":"icon-save"
	}
]
 */

