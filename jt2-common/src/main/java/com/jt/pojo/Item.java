package com.jt.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

//這個類的對象轉換成JSON會有一個屬性是Images(因為多添加了getImages()方法)，
//若把該JSON轉回Item對象，會因為找不到setImages()方法而出錯，所以要加下面這個註解
@JsonIgnoreProperties(ignoreUnknown=true) //表示JSON轉化時忽略未知屬性
@TableName("tb_item")
@Data
@Accessors(chain=true)
public class Item extends BasePojo{
	@TableId(type=IdType.AUTO)
	private Long id;				//商品id
	private String title;			//商品标题
	private String sellPoint;		//商品卖点信息
	private Long   price;			//商品价格  執行速度: Long > double  且double會有精度問題: 0.00000001+0.99999999=0.99999999999999999  把數據乘上100存在long中，頁面展現時再把數據除以100     
	private Integer num;			//商品数量
	private String barcode;			//条形码
	private String image;			//商品图片信息 在的是圖片的地址   url1, url2, url3
	private Long   cid;				//表示商品的分类id
	private Integer status;			//1正常，2下架
	
	//为了满足页面调用需求,添加get方法
	public String[] getImages(){
		
		return image.split(",");
	}
}
