package com.jt.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO implements Serializable{
	private Integer error;  //0.表示成功  1.表示失败	
	private String  url;
	private Integer width;
	private Integer height;
	
}

/*
富文本編輯器圖片回顯的json格式要求:
{"error":0,"url":"图片的保存路径","width":图片的宽度,"height":图片的高度}
参数说明：
1.error 表示文件上传是否正确     0表示正常  1表示失败
2.url   该路径为虚拟的url路径.(不是真實的磁盤路徑)(用來回顯圖片的路徑)
3.width/height  宽和高

*/