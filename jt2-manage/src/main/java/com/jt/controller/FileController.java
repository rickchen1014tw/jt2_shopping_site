package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@Controller
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 當用戶上傳完成時重定向到上傳頁面
	 * 思路:
	 * 	1.獲取用戶文件信息 包含文件名稱
	 *  2.指定文件上傳的路徑  
	 *  3.實現文件上傳
	 * @param fileImage
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//1.獲取input標簽中的name屬性 這裡會取得fileImage 不是我們需要的參數
		String inputName = fileImage.getName();
		System.out.println("1:"+inputName);
		//2.獲取文件名稱
		String fileName = fileImage.getOriginalFilename();
		//3.定義文件夾路徑
		//把\\用/來取代，因為之後程序要在linux上執行而linux不認得\
		//File fileDir = new File("C:\\Users\\chenminghong\\Java-Code\\Workspace-SpringToolSuite\\1902JT\\jt\\image");
		File fileDir = new File("C:/Users/chenminghong/Java-Code/Workspace-SpringToolSuite/1902JT/jt/image");
		if(!fileDir.exists()) {
			//創建文件夾
			fileDir.mkdirs();
		}
		//4.實現文件上傳
		fileImage.transferTo(new File("C:/Users/chenminghong/Java-Code/Workspace-SpringToolSuite/1902JT/jt/image/"+fileName));
		//重定向到file.jsp 這樣寫不會走視圖解析器，寫什麼就跳轉到什麼
		return "redirect:/file.jsp";
	}
	
	//實現圖片上傳
	@RequestMapping("/pic/upload")
	@ResponseBody
	public ImageVO uploadFile(MultipartFile uploadFile) {
		
		return fileService.updateFile(uploadFile);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
