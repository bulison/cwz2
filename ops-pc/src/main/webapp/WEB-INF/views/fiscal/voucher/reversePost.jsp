<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="xsbjd" scope="page"></c:set>
<c:set var="billCodeName" value="销售报价单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
	<div class="nav-box">
		<ul>
			<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
			<li><a href="javascript:;">凭证</a></li>
			<li><a href="javascript:;" class="curr">凭证反过账</a></li>
		</ul>
	</div>
	
	<div class="tool-box">
		
		<div class="tool-box-pane">
		<form id="search-form">
			<input name="voucherWordId" class="easyui-textbox" data-options="prompt:'凭证字',width:100,height:32"/>+
			<input name="startVoucherNumber" class="easyui-textbox" data-options="prompt:'开始凭证号',width:100,height:32"/>+
			<input name="endVoucherNumber" class="easyui-textbox" data-options="prompt:'结束凭证号',width:100,height:32"/>+
			<input name="startDay" class="easyui-datebox" data-options="prompt:'开始日期',width:100,height:32"/>-
			<input name="endDay" class="easyui-datebox" data-options="prompt:'结束日期',width:100,height:32"/>
			<a href="javascript:;" class="btn-blue btn-s go-search" onclick="refreshData()">筛选</a>
		    <a href="javascript:;" class="btn-blue btn-s" onclick="clearForm()">清空</a>
		    <a href="javascript:;" class="btn-blue btn-s" onclick="possing()">反过账</a>
		</form>
		</div>
	</div>
	
	<table id="dataTable" data-options="width:'100%',pagination:true,singleSelect:false,selectOnCheck:true,checkOnSelect:true,fitColumns:true,url:'${ctx}/voucher/query'">
		<thead>
			<tr>
			    <th data-options="field:'ck',width:10,checkbox:true"></th>
				<th data-options="field:'fid',width:10">单号</th>
				<th data-options="field:'startVoucherNumber',width:10">凭证字号</th>
				<th data-options="field:'voucherDate',width:10">凭证日期</th>
				<th data-options="field:'accessoryNumber',width:10">附单据</th>
				<th data-options="field:'creatorName',width:10">制单人</th>
				<th data-options="field:'auditorName',width:10">审核人</th>
				<th data-options="field:'postPeopleName',width:10">过账人</th>
			</tr>
		</thead>
	</table>
<div id="my-window"></div>
<script type="text/javascript" src="${ctx}/resources/js/reversPost.js?v=${js_v}"></script>
</body>
</html>

