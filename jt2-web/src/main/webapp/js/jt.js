var TT = JT = {
	checkLogin : function(){
		var _ticket = $.cookie("JT_TICKET");
		//沒有cookie代表沒登錄過，直接返回
		if(!_ticket){
			return ;
		}
		//当dataType类型为jsonp时，jQuery就会自动在请求链接上增加一个callback的参数
		$.ajax({
			url : "http://sso.jt.com/user/query/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					//把json串转化为js对象
					var _data = JSON.parse(data.data);
					var html =_data.username+"，歡迎來到Demo購物商城!<a href=\"http://www.jt.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});