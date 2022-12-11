<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>美剧列表</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<link rel="stylesheet" href="css/cssTeleplayListLayout.css" type="text/css" />
  </head>
  <body>
	<%@ include file="../header.jsp" %>

	<form action="" method="post">		<!-- 表单提交给对应的goodsListLayout -->
	<div class="divGrid note right" style="margin-top:-25px;">
		&emsp;&emsp;按类型筛选：
		<select name="teleplayTypeName">
			<option value="">&nbsp;</option>
			<c:forEach var="type" items="${ teleplayTypeList }">
				<c:if test="${ type.teleplayTypeName != search }">
					<option value="${ type.teleplayTypeName }">${ type.teleplayTypeName }</option>
				</c:if>
				<c:if test="${ type.teleplayTypeName == search }">
					<option value="${ type.teleplayTypeName }" selected="selected">${ type.teleplayTypeName }</option>
				</c:if>
			</c:forEach>
		</select>&ensp;
		<input type="submit" name="buttonFilter" value="筛选">
		&emsp;
		&emsp;&emsp;搜索：
		<input type="text" name="search" value="${ search }" style="width:80px;">
		<input type="submit" name="buttonSearch" value="搜索">
		<span class="note">（在集数、名称、类型中搜索）</span>
	</div>	
	
	<h3>美剧列表</h3>
	
  	<c:if test="${ teleplayList.size() > 0 }">		<%-- 这里使用了jstl标签 --%>
		<div class="divGrid">		
			<div class="list">
				<ul>
							
	<c:forEach var="teleplay" items="${ teleplayList }" varStatus="status">
		<li>
			 <div class="imgDiv">
				 <a href="teleplayShow?teleplayId=${ teleplay.teleplayId }" title="${teleplay.teleplayName }">
				 	<img src="fileUpload/image/${ teleplay.image }" alt="（无图片）" class="img"
				 		onerror="this.src='fileUpload/image/noImage2.png'"></a>		<!-- 图片不存在时以默认的图片代替 -->
			 </div>
			 <div class="teleplayName">
			 	<a href="teleplayShow?teleplayId=${ teleplay.teleplayId }" title="${teleplay.teleplayName }">${teleplay.teleplayName }</a>
			 </div>
			 
			 <%-- <div class="locDiv">
				 &emsp;&emsp;&emsp;&emsp;
				 <span class="locCode">￥</span>
				 <span class="loc"> ${teleplay.loc }</span>&emsp;				 
				 <a href="#" onclick="alert('已添加到购物车。')"> <span class="cart">立刻购买</span></a>
			 </div> --%>
		</li>
	</c:forEach>		
				
				</ul>	
			</div>
		</div>
		
		<div class="divGrid note right">
			${ page }
		</div>
  	</c:if>
	</form>
		
	<div id="msg" class="msg">${ msg }</div>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">
function checkAll() {
    var box = document.getElementsByName("teleplayId"); // 获取复选框列表
    var all = document.getElementsByName("checkboxAll")[0]; // 全选复选框
    all.onclick = function () {
        for (var i = 0; i < box.length; i++) {
            // 全部变成all的状态
            box[i].checked = this.checked;
        }
    };
    //遍历checkbox，子复选框有一个未选中时，如果全选框设为false不选状态
    for (var i = 0; i < box.length; i++) {
        box[i].onclick = function () {
            // 如果有一个没选，则把all变成未选
            if (!this.checked) {
                all.checked = false;
            }
        };
    }
}
</script>
</html>
