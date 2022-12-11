<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="zh">
<head>
<title>美剧列表</title>
<link rel="stylesheet" href="css/css.css" type="text/css" />
<style type="text/css">
.teleplayName {
	display: inline-block; /*行内块级元素*/
	width: 350px;
	vertical-align: middle; /*上下居中对齐*/
	white-space: nowrap; /*不换行*/
	overflow: hidden; /*隐藏超出部分*/
	text-overflow: ellipsis; /*不显示部分以三个点表示*/
}
</style>
</head>
<body>
	<%@ include file="../header.jsp"%>

	<form action="" method="post">
		<!-- 表单提交给对应的goodsList -->
		<div class="divGrid note right" style="margin-top: -25px;">
			&emsp;&emsp;按类型筛选： <select name="teleplayTypeName">
				<option value="">&nbsp;</option>
				<c:forEach var="type" items="${ teleplayTypeList }">
					<c:if test="${ type.teleplayTypeName != search }">
						<option value="${ type.teleplayTypeName }">${ type.teleplayTypeName }</option>
					</c:if>
					<c:if test="${ type.teleplayTypeName == search }">
						<option value="${ type.teleplayTypeName }" selected="selected">${ type.teleplayTypeName }</option>
					</c:if>
				</c:forEach>
			</select>&ensp; <input type="submit" name="buttonFilter" value="筛选">
			&emsp; &emsp;&emsp;搜索： <input type="text" name="search"
				value="${ search }" style="width: 80px;"> <input
				type="submit" name="buttonSearch" value="搜索"> <span
				class="note">（在集数、名称、类型中搜索）</span>
		</div>

		<h3>美剧列表</h3>

		<c:if test="${ teleplayList.size() > 0 }">
			<%-- 这里使用了jstl标签 --%>
			<div class="divGrid">
				<table style="width: 900px; margin: 0 auto;"
					class="table_border table_border_bg table_hover">
					<tr class="tr_header">
						<td style="width: 46px;">序号</td>
						<td>名称</td>
						<td>集数</td>
						<td>地区</td>
						<td style="width: 95px;">详情/修改</td>
						<td style="width: 46px;">选择</td>
					</tr>

					<c:forEach var="teleplay" items="${ teleplayList }"
						varStatus="status">
						<tr
							title="【年份】${ teleplay.year }，，&emsp;【类型】${ teleplay.teleplayTypeName }">
							<td class="note">${status.index + countShowed + 1 }</td>
							<td style="font-weight: bold; text-align: left;">
								<!-- 如果图片加载失败则不显示 --> <a
								href="teleplayShow?teleplayId=${ teleplay.teleplayId }"
								style="text-decoration: none;"> <img
									src="fileUpload/image/${ teleplay.image }" alt="无图"
									onerror="this.src='fileUpload/image/noImage2.png'"
									style="width: 100px; vertical-align: middle; margin-right: 5px;">
									<span title="${ teleplay.teleplayName }" class="teleplayName">${ teleplay.teleplayName }</span></a>
							</td>
							<td>${ teleplay.episode }</td>
							<td>${ teleplay.loc }</td>
							<td><a
								href="teleplayShow?teleplayId=${ teleplay.teleplayId }"
								title="详情"> <img src="image/icon_show.gif" border="0" /></a>&emsp;
								<a href="teleplayEdit?teleplayId=${ teleplay.teleplayId }"
								title="修改"> <img src="image/icon_edit.gif" border="0" /></a></td>
							<td><input type="checkbox" name="teleplayId"
								value="${ teleplay.teleplayId }"></td>
						</tr>
					</c:forEach>

					<tr>
						<td colspan="8" class="note"
							style="text-align: right; height: 50px;">（<a
							href="teleplayAdd">新添美剧</a>）
							&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
							<input type="submit" name="buttonDelete" value="删除"
							onclick="return confirm('确认删除所选记录？');"> &emsp; <label>全选:<input
								type="checkbox" name="checkboxAll" onchange="checkAll()"></label>&emsp;
						</td>
					</tr>
				</table>
			</div>

			<div class="divGrid note right">${ page }</div>
		</c:if>
	</form>

	<div id="msg" class="msg">${ msg }</div>

	<%@ include file="../footer.jsp"%>
</body>
<script type="text/javascript">
	function checkAll() {
		var box = document.getElementsByName("teleplayId"); // 获取复选框列表
		var all = document.getElementsByName("checkboxAll")[0]; // 全选复选框
		all.onclick = function() {
			for (var i = 0; i < box.length; i++) {
				// 全部变成all的状态
				box[i].checked = this.checked;
			}
		};
		//遍历checkbox，子复选框有一个未选中时，如果全选框设为false不选状态
		for (var i = 0; i < box.length; i++) {
			box[i].onclick = function() {
				// 如果有一个没选，则把all变成未选
				if (!this.checked) {
					all.checked = false;
				}
			};
		}
	}
</script>
</html>
