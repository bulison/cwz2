<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>list</title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/layout.css"/>
<link rel="stylesheet" href="${ctx}/resources/css/comm.css"/>
<script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<title>信息详情</title>
<style>
  #context{ margin:20px auto auto 50px}
  #context h1{color:#000; margin:10px auto 20px auto;}
  #context h5{ font-size:16px; color:#000; display:inline-block; margin:10px auto 20px auto;}
  #context P{display:inline-block;color:#000; font-size:16px;}
</style>
</head>
<body>
<div id="context">
  <h1>信息详情</h1>
  <h5>订单号：</h5><a href="${ctx}/eventController/list?planId=${order.fid}&orgFid=${order.orgFid}&fid=${event.fid}">${order.code}</a><br/>
  <h5>描述：</h5><p>${entity.title}</p><br/>
  <h5>内容：</h5><p>${entity.content}</p><br/>
  <h5>发送人：</h5><p>${entity.senderName}</p><br/>
  <h5>接收人：</h5><p>${entity.receiverName}</p><br/>
  <h5>发送时间：</h5><p>${entity.sendTime}</p><br/>
  <input type="button" id="return" value="返回" style=" width:87px; height:26px; background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF"/>
  <input type="button" id="delete" value="删除" style=" width:87px; height:26px; background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF"/>
</div>
<script type="text/javascript">
$("#return").click(function(e) {
    location.href = "${ctx}/messageController/manage";
});
$("#delete").click(function(e) {
	$.fool.confirm({
		msg:'确定删除？',
		fn:function(b){
			if(b){
				$.post("${ctx}/messageController/delete",{fid:'${entity.fid}'});
	       	    location.href = "${ctx}/messageController/manage";
			}
		}
	});
});
</script>
</body>
</html>