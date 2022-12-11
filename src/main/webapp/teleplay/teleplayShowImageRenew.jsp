<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>美剧详情 - 更换图片</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>美剧详情 - 更换图片</h3>

	<c:if test="${ teleplay != null }">
	<table style="width:900px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>名称</td>
			<td class="left" style="font-weight:bold;">${ teleplay.teleplayName }</td>
		</tr>
		<tr>
			<td>集数</td>
			<td class="left">${ teleplay.episode }</td>
		</tr>
		<tr>
			<td>类型</td>
			<td class="left">${ teleplay.teleplayTypeName }</td>
		</tr>
		<tr>
			<td>地区</td>
			<td class="left">${ teleplay.loc }</td>
		</tr>
		<tr>
			<td>年份</td>
			<td class="left">${ teleplay.year }</td>
		</tr>
		<tr>
			<td>美剧图片</td>
			<td class="left" style="padding:0 5px;">		<!-- 内联框架 -->
				<iframe src="teleplayImageUpload?teleplayId=${ teleplay.teleplayId }&image=${ teleplay.image }" 
						style="height:155px; width:760px; border:0;"></iframe>
			</td>
		</tr>
		<tr>
			<td>剧情简介</td>
			<td class="left" style="width:650px;">${ teleplay.plot }</td>
		</tr>
		<tr>
			<td colspan="2">
				<a href="teleplayEdit?teleplayId=${ teleplay.teleplayId }">修改</a>&emsp;
				<a href="goodsDeleteDo?teleplayId=${ teleplay.teleplayId }"
						onclick="return confirm('确定要删除吗？');">删除</a>
			</td>
		</tr>
	</table>
  	</c:if>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<a href="teleplayList">返回美剧列表</a>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>
