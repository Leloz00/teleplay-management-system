<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="zh">
<head>
<title>美剧详情</title>
<link rel="stylesheet" href="css/css.css" type="text/css" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/dplayer@1.25.0/dist/DPlayer.min.css">
	<style type="text/css">
	.left a:link { text-decoration: none;color:black;}
　　 .left a:active { text-decoration:blink}
	.left a:hover {
		color: #0785D4;
	    text-decoration: none;
	}
/* 	.left a:visited { text-decoration: none;} */
	</style>
</head>
<body>
	<%@ include file="../header.jsp"%>
	<h3>美剧详情</h3>

	<c:if test="${ teleplay != null }">
		<table style="width: 900px; margin: 0 auto; border: none"
			class="table_padding10">

			<tr>
				<td class="left" style="font-weight: bold;"><a
					href="teleplayListLayout">美剧列表</a> &ensp;>>&ensp; <a
					href="teleplayShow?teleplayId=${ teleplay.teleplayId }">${ teleplay.teleplayName }&ensp;${ teleplay.episode }</a>
				</td>
			</tr>
			<tr>
				<td class="left" style="width: 650px;">
					<div id="dplayer"></div>
				</td>
			</tr>


			<tr>
				<td colspan="2"><c:if
						test="${ myUser.role == 'admin' || myUser.role == 'user' }">
						<a href="teleplayEdit?teleplayId=${ teleplay.teleplayId }">修改</a>
						&emsp;
						<a href="goodsDeleteDo?teleplayId=${ teleplay.teleplayId }"
							onclick="return confirm('确定要删除吗？');">删除</a>
					</c:if> <c:if test="${ myUser.role != 'admin' && myUser.role != 'user' }">
						<a href="#" onclick="alert('已添加到购物车。')">立刻购买</a>
						&ensp;
					</c:if></td>
			</tr>
		</table>
	</c:if>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<c:if test="${ myUser.role == 'admin' || myUser.role == 'user' }">
		<a href="teleplayList">返回美剧列表</a>
	</c:if>
	<c:if test="${ myUser.role != 'admin' && myUser.role != 'user' }">
		<a href="teleplayListLayout">返回美剧列表</a>
	</c:if>

	<%@ include file="../footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/dplayer@1.25.0/dist/DPlayer.min.js"></script>
	<script>
		const dp = new DPlayer({
			container : document.getElementById('dplayer'),
			screenshot : true,
			video : {
				url : "fileUpload/video/${teleplay.video}",
				pic : "fileUpload/image/${teleplay.image}",
				thumbnails : "fileUpload/image/${teleplay.image}" // 缩略图
			},
// 			danmaku: {
// 		        // ...
// 		        addition: ['https://api.prprpr.me/dplayer/v3/bilibili?aid=51818204'],
// 		    },
		});
	</script>
</body>

</html>