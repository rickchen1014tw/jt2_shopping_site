package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

//因為需要跳轉頁面，所以不能使用@RestController
//如果需要返回json串，則在方法上添加@ResponseBody註解
@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Reference(timeout = 3000, check = false)
	private DubboCartService cartService;

	/**
	 * http://www.jt.com/cart/show.html
	 * 1.實現商品列表的信息展現
	 * 2.頁面取值: ${cartList}
	 */
	@RequestMapping("/show")
	public String findCardList(Model model, HttpServletRequest request) {
		//用戶發起請求到購物車頁面，可以用透過用戶的請求得到cookie(JT_TICKET)，拿到cookie就可以拿到token
		//拿到token，就可以到redis server中拿到用戶信息的json串，拿到json串就可以拿到用戶的id
		//但把取得用戶id的代碼寫在這邊合適嗎? 不合適，因為如果有其它的方法也需要獲取用戶id，就得把同樣的代碼寫在多個地方
		//把取得用戶id的代碼寫在aop裡? 不行，因為aop裡沒辦法拿到HttpServletRequest對象
		//拿不到HttpServletRequest對象就沒辦法拿到cookie，aop適合拿來業務模塊的處理，而cookie屬於用戶交互的環節
		/*
		//把在攔載器裡面取得後儲存在request域中的User對象取出來
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();
		*/
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = cartService.findCardListByUserId(userId);
		model.addAttribute("cartList", cartList); //利用model把cartList放到域對象中
		return "cart"; //返回頁面邏輯名稱
	}
	
	/**
	 * 實現購物車數量的修改
	 * url地址:http://www.jt.com/cart/update/num/562379/8
	 * 規定: 如果url參數中使用Restful風格獲取數據時
	 * 接收的參數和對象的屬性匹配，則可以直接使用對象接收
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
	//public SysResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num) { 這裡我們不用這個方式來接
		try {
			Long userId =  UserThreadLocal.get().getId();
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	/**
	 * 實現購物車刪除操作
	 * url:http://www.jt.com/cart/delete/562379.html
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId =  UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		//重定向到購物車列表頁面
		//return cart;	直接跳過去會沒有carList數據
		return "redirect:/cart/show.html"; //重定向到/cart/show.html 再查詢一次購物車內容
	}
	
	/**
	 * 新增購物車
	 * 頁面表單提交 發起post請求
	 * 攜帶購物車參數 itemTitle itemImage itemPrice
	 * url:http://www.jt.com/cart/add/562379.html
	 * 頁面用的是form表單提交，有多個參數，頁面js把參數名跟Cart屬性名設為相同，方便用Cart對象來接收
	 * itemId也順便賦值到Cart對象裡
	 * @param cart
	 * @return
	 */
	@RequestMapping("/add/{itemId}")
	public String insertCart(Cart cart, HttpServletRequest request) {
		/*
		//把在攔載器裡面取得後儲存在request域中的User對象取出來
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();
		*/
		
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.insertCart(cart);
		//新增數據之後，重定向展現購物車列表信息
		return "redirect:/cart/show.html";
	}
/*
item.jsp

<form id="cartForm" method="post">
	<input class="text" id="buy-num" name="num" value="1" onkeyup="setAmount.modify('#buy-num');"/>
	<input type="hidden" class="text"  name="itemTitle" value="${item.title }"/>
	<input type="hidden" class="text" name="itemImage" value="${item.images[0]}"/>
	<input type="hidden" class="text" name="itemPrice" value="${item.price}"/>
</form>

//利用post传值
function addCart(){
	var url = "http://www.jt.com/cart/add/${item.id}.html";
	document.forms[0].action = url;		//js设置提交链接
	document.forms[0].submit();			//js表单提交
}

<div id="choose-btn-append"  class="btn">
	<a class="btn-append " id="InitCartUrl" onclick="addCart();" clstag="shangpin|keycount|product|initcarturl">加入购物车<b></b></a>
</div>
	
	
	
	
	
 */
	
	
	
}
