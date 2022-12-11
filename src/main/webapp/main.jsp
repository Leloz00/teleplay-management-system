<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户功能</title>
	<link rel="stylesheet" href="css/css.css">
  </head>
  
  <body>
	<%@ include file="header.jsp" %>
   	<h3>用户功能</h3> 
  		
	<table style="width:600px; margin:0 auto;" class="table_border table_border_bg table_hover">
		<tr height="50" class="tr_header">
			<th colspan="2">
				用户功能列表
			</th>
		</tr>
		<tr height="50">
			<td width="30%">
				登录相关
			</td>
			<td class="left">&emsp;
				<a href="userShow?userId=${ myUser.userId }">个人信息</a>&emsp;&emsp;
				<a href="logout">退出登录</a>&emsp;&emsp;
			</td>
		</tr>

		<c:if test="${ myUser.role == 'guest'}">
			<tr height="50">
				<td>
					订单管理
				</td>
				<td class="left">&emsp;
					<a href="teleplayListLayout">美剧列表</a>&emsp;&emsp;
					购物车&emsp;&emsp;
					订单列表&emsp;&emsp;
				</td>
			</tr>
		</c:if>

		<c:if test="${ myUser.role == 'admin' || myUser.role == 'user'}">
			<tr height="50">
				<td>
					美剧管理
				</td>
				<td class="left">&emsp;
					<a href="teleplayList">美剧列表</a>&emsp;&emsp;
					<a href="teleplayAdd">美剧新添</a>&emsp;&emsp;
					<a href="teleplayListLayout">美剧列表（平铺式）</a>&emsp;&emsp;
				</td>
			</tr>
			<tr height="50">
				<td>
					类型管理
				</td>
				<td class="left">&emsp;
					<a href="teleplayTypeList">类型列表</a>&emsp;&emsp;
					<a href="teleplayTypeAdd">类型新添</a>&emsp;&emsp;
				</td>
			</tr>
		</c:if>

		<c:if test="${ myUser.role == 'admin' }">
			<tr height="50">
				<td>
					用户管理
				</td>
				<td class="left">&emsp;
					<a href="userList">用户列表</a>&emsp;&emsp;
					<a href="userAdd">用户新添</a>&emsp;&emsp;
				</td>
			</tr>
		</c:if>	
	</table>
	
	<div id="msg" class="msg">${ msg }</div>
	
	<%@ include file="footer.jsp" %>
  </body>
</html>
