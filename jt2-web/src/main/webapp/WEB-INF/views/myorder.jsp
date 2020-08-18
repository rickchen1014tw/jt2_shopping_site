<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Cache-Control" content="max-age=300" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的訂單 - Demo購物商城</title>

<link rel="stylesheet" type="text/css" href="/css/base.css" media="all" />
<link rel="stylesheet" type="text/css" href="/css/myjd.common.css" media="all" />
<link rel="stylesheet" type="text/css" href="/css/myjd.order.css" media="all" />
<script type="text/javascript" src="/js/jquery-1.2.6.min.js"></script>
</head>
<body> 
<!--shortcut start-->
<jsp:include page="../commons/shortcut.jsp" />
<!--shortcut end-->
	<div id="logo"><a href="http://www.jt.com/"><img clstag="clickcart|keycount|xincart|logo" src="/images/jt-logo.png" title="返回Demo購物商城首頁" alt="返回Demo購物商城首頁"></a></div>
	</br>

<h2>我的訂單</h2><hr>
<c:forEach items="${orderList }" var="order" >
<h3>訂單編號: </h3>${order.orderId} </br>
<h3>訂單商品: </h3>

<c:forEach items="${order.orderItems }" var="item" >
	<a href="http://www.jt.com/items/${item.itemId}.html"><img src="${item.picPath}" width="100" height="100"></a>
	${item.title} &emsp; 價格: ${item.price} &emsp;  數量: ${item.num} &emsp; 
	</br>
</c:forEach>
<h3>總金額:</h3> ${order.payment }</br>
<h3>訂單寄送地址: </h3>
姓名${order.orderShipping.receiverName } </br>
地址:${order.orderShipping.receiverZip} ${order.orderShipping.receiverCity} ${order.orderShipping.receiverDistrict} ${order.orderShipping.receiverAddress}</br>
電話: ${order.orderShipping.receiverMobile }</br>
</br><hr>
</c:forEach>

<script type="text/javascript" src="/js/base-v1.js"></script>
<!-- footer start -->
<jsp:include page="../commons/footer.jsp" />
<!-- footer end -->

<!-- 购物车相关业务 -->
<script type="text/javascript" src="/js/cart.js"></script>
<script type="text/javascript" src="/js/jquery.price_format.2.0.min.js"></script>

</html>