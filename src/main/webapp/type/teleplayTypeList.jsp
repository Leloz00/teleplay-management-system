<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>类型列表</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
  </head>
  <body>
	<%@ include file="../header.jsp" %>

	<form action="" method="post">	
	<div class="divGrid note right" style="margin-top:-25px;">
		&emsp;&emsp;搜索：
		<input type="text" name="search" value="${ search }" style="width:80px;">
		<input type="submit" name="buttonSearch" value="搜索">
		<span class="note">（在类型名称、备注中搜索）</span>
	</div>	
	
	<h3>美剧类型列表</h3>
	
	<c:if test="${ teleplayTypeList.size() > 0 }">		<%-- 这里使用了jstl标签 --%>	
		<div class="divGrid">
		<table style="width:900px; margin:0 auto;" class="table_border table_border_bg table_hover">
			<tr class="tr_header">
				<td>序号</td>
				<td>类型名称</td>
				<td>备注</td>
				<td>更新时间</td>
				<td style="width:95px;">修改</td>
				<td style="width:46px;">选择</td>
			</tr>
			
			<c:forEach var="type" items="${ teleplayTypeList }" varStatus="status">
				<tr>
					<td class="note">${status.index + countShowed + 1 }</td>
					<td style="font-weight:bold;">${ type.teleplayTypeName }</td>
					<td>${ type.note }</td>
					<td class="note">${ type.timeRenew }</td>
					<td>
						<a href="teleplayTypeShow?teleplayTypeId=${ type.teleplayTypeId }" title="详情">
								<img src="image/icon_show.gif" border="0" /></a>&emsp;
						<a href="teleplayTypeEdit?teleplayTypeId=${ type.teleplayTypeId }" title="修改">
								<img src="image/icon_edit.gif" border="0" /></a>
					</td>
					<td>
						<input type="checkbox" name="teleplayTypeId" value="${ type.teleplayTypeId }">
					</td>
				</tr>
			</c:forEach>
			
			<tr>
				<td colspan="7" class="note" style="text-align:right; height:50px;">
					（<a href="teleplayTypeAdd">新添类型</a>）
					&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
					<input type="submit" name="buttonDelete" value="删除" 
							onclick="return confirm('确认删除所选记录？');">
					&emsp;
					<label>全选:<input type="checkbox" name="checkboxAll" onchange="checkAll()"></label>&emsp;
				</td>
			</tr>
		</table>
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
	var checkboxList = document.getElementsByName("teleplayTypeId");			//获取复选框列表
	var checkboxAll  = document.getElementsByName("checkboxAll")[0];	//全选复选框
	
	for (var i = 0; i < checkboxList.length; i++) {						//对于列表中的每一个复选框
		checkboxList[i].checked = checkboxAll.checked;					//此复选框的勾选情况与全选复选框一致
	}
}
</script>
</html>
