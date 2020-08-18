package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;

@Service
@PropertySource
("classpath:/properties/image.properties") //用註解的方法賦值
//@ConfigurationProperties(prefix = "image")  依賴get/set方法來賦值
public class FileServiceImpl implements FileService {
	//定義本地磁盤路徑
	@Value("${image.localDirPath}")
	private String localDirPath;
	//定義虛擬路徑名稱
	@Value("${image.urlPath}")
	private String urlPath;
	
	/**
	 * 1.獲取圖片名稱
	 * 2.校驗是否為圖片類型 jpg|png|gif
	 * 3.校驗是否為惡意程序 木馬.exe.jpg
	 * 4.分文件保存 按照時間存儲 yyyy/MM/dd
	 * 5.防止文件重名 UUID 32位16進制數+毫秒數
	 * 
	  * 正則常用字符:
	 * 	1.^   標識開始
	 *  2.$   標識結束
	 *  3. 點.    任意單個字符
	 *  4.*	    表示任意個0~~~無窮
	 *  5.+	    表示1~~~無窮
	 *  6.\  標識特殊字符 \. 標識.
	 *  7.(xxx|xx|xxx)  代表分組 滿足其中一個條件即可
	 */

	@Override
	public ImageVO updateFile(MultipartFile uploadFile) {
		ImageVO imageVO = new ImageVO();
		//1.獲取圖片名稱   a.jpg  A.JPG windows不區分大小寫但linux區分
		String fileName = uploadFile.getOriginalFilename();
		//將字符統一轉化為小寫
		fileName = fileName.toLowerCase();
		
		//2.校驗圖片類型 使用正則表達式判斷字符串 如果遇到字符串的校驗，要用正則表達式來判斷，不要用if
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			imageVO.setError(1); //表示上傳有誤
			return imageVO;
		}

		//3.判斷是否為惡意程序
		try {
			//準備一個圖片的模板，把用戶上傳的圖片數據塞到模版裡，如果塞不進去，就會報錯
			//如果塞得進去，判斷有沒有寬和高的屬性
			BufferedImage bufferedImage = 
				ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			if(width==0 || height ==0) {
				imageVO.setError(1);
				return imageVO;
			}
			
			//4.時間轉化為字符串 2019/5/31
			String dateDir = 
					new SimpleDateFormat("yyyy/MM/dd")
					.format(new Date());
			
			//5.準備文件夾
			String localDir = localDirPath + dateDir;
			File dirFile = new File(localDir);
			if(!dirFile.exists()) {
				//如果文件不存在，則創建文件夾
				dirFile.mkdirs();
			}
			//b8a7ff05-8356-11e9-9997-e0d55e0fcfd8
			//6.使用UUID定義文件名稱 uuid.jpg
			String uuid = 
			UUID.randomUUID().toString().replace("-","");
			//動態獲取副檔名 jpg 
			String fileType = 
			fileName.substring(fileName.lastIndexOf("."));
			
			//拼接新的文件名稱
			//C:/Users/chenminghong/Java-Code/Workspace-SpringToolSuite/1902JT/jt/image/文件名稱.副檔名
			String realLocalPath = localDir+"/"+uuid+fileType;
			
			//7.完成文件上傳
			uploadFile.transferTo(new File(realLocalPath));
			
			//8.拼接url路徑   http://image.jt.com/yyyy/MM/dd/uuid.jpg
			String realUrlPath = 
				   urlPath+dateDir+"/"+uuid+fileType;
			
			//將圖片信息回傳給頁面
			imageVO.setError(0)
				   .setHeight(height)
				   .setWidth(width)
				   .setUrl(realUrlPath);
		} catch (Exception e) {
			e.printStackTrace();
			imageVO.setError(1);
			return imageVO;
		}

		return imageVO;
	}

}
