<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
	<title>美剧新添</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>美剧新添</h3>

	<table style="width:900px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>名称</td>
			<td class="left">
				<input type="text" name="teleplayName" maxlength="45" style="width:400px;">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>集数</td>
			<td class="left">
				<input type="text" name="episode" maxlength="45">&ensp;
				<span class="msg">**</span>
			</td>
		</tr>
		<tr>
			<td>类型</td>
			<td class="left">
				<select name="teleplayTypeId">
					<option value="">&lt;请选择&gt;</option>
					<c:forEach var="type" items="${ teleplayTypeList }">
						<option value="${ type.teleplayTypeId }">${ type.teleplayTypeName }</option>
					</c:forEach>
				</select>&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>地区</td>
			<td class="left">
				<input type="text" name="loc" maxlength="10">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>年份</td>
			<td class="left">
				<input type="text" name="year" maxlength="5">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>美剧图片</td>
			<td class="left" style="padding:0 3px;">		<!-- 内联框架。对象goods在Servlet类TeleplayAdd中生成 -->
				<iframe src="teleplayImageUpload?teleplayId=${ teleplay.teleplayId }&image=${ teleplay.image }" 
						style="height:155px; width:760px; border:0;"></iframe>
				<input type="hidden" name="goodsUUID" value="${ teleplay.teleplayId }">		<!-- 临时的ID -->
			</td>
		</tr>
		<tr>
			<td>剧情简介</td>
			<td class="left" style="width:750px;">
				<script type="text/plain" id="plot"></script>		<!-- 加载编辑器的容器 -->
			</td>
		</tr>
		<tr height="40">
			<td colspan="2" style="padding-left:30px; font-size:small;">			
				<input type="submit" value="提交" onclick="ajax()">	
			</td>
		</tr>
	</table>

	<div id="msg" class="msg" style="line-height:25px;">${ msg }</div>
	<br>
	<a href="teleplayList">返回美剧列表</a>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">	

function ajax() {
	
	var episode 	= $("[name='episode']").val();					//根据控件的name属性值获取输入的值
	var teleplayName 	= $("[name='teleplayName']").val();
	var teleplayTypeId 		= $("[name='teleplayTypeId']").val();
	var loc 		= $("[name='loc']").val();
	var year 		= $("[name='year']").val();
	
	var goodsUUID 	= $("[name='goodsUUID']").val();				//临时的ID
	var plot 		= ue.getContent();								//获取富文本编辑器ue中的输入内容
	
	var url = "teleplayAddDo";											//****目标地址
	var arg = {	"episode" : episode, "teleplayName" : teleplayName, 
			   	"teleplayTypeId"  : teleplayTypeId,  "loc"     : loc, 
			   	"year"   : year,
			   	"plot"  : plot,  "goodsUUID" : goodsUUID };		//JSON格式数据： {key:value, k2:v2}

	$.post(url, arg, function(data) {
		setMsg(data);
	});
}
  
function setMsg(msg) {

	var prefix = "@Redirect:";							//前缀
	
	if (msg.indexOf(prefix) == 0) {						//如果msg的前面部分是“@Redirect:”
		var url = msg.substring(prefix.length); 		//截取前缀后面的路径名
		window.location.href = url;						//网页转向
		return;
	}
	
	$("#msg").html(msg);								//显示消息
}
</script>

<script type="text/javascript" src="UEditor/ueditor.config.js"></script>	<!-- 配置文件。可自行修改 -->
<script type="text/javascript" src="UEditor/ueditor.all.min.js"></script>	<!-- 编辑器源码文件 -->
<script type="text/javascript">
	var ue = UE.getEditor("plot", {					//通过id实例化编辑器，得到的对象为ue
		autoHeightEnabled: false,						//高度不自动适应
		initialFrameHeight:300,							//初始化编辑器高度,默认320
		//initialFrameWidth:500,						//初始化编辑器宽度,默认500
	});
</script>
</html>
