<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>客户资金周转能力分析</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/rateModel.css" />
<%-- <script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script> --%>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
</style>
</head>
<body>
	   <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>客户资金周转能力分析</h1>
                </div>             
       </div>
	
	   <div class="myform">
	       <div id="buttonBox" style=""> 
	       		<a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a>
	       		<a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a>
           </div>
	       <div class="searchBox" style="">
	       		<form class="form1 inSearch" id='inSearch' style="">
	       			<p><font>客户编码：</font><input id="code" name="code" /> </p>
	       			<p><font>客户名称：</font><input id="goodsName" name="goodsId" /> </p>
	       		</form>
	       </div>
	   </div>
	   <div class="dataBox">
	       <div id="detailsList"></div>
	   </div>
	   
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$('#mySearch').click(function(){
	var data = $('#inSearch').serializeJson();
	$('#detailsList').datagrid('load',data);
});

$('#clearForm').click(function(){
	$('#inSearch').form('clear');
});

//搜索条件初始化
$('#goodsName').textbox({
	prompt:"输入单号",
	width:162,
	height:30
});

$('#code').textbox({
	prompt:"输入单号",
	width:162,
	height:30
});


//数据载入
$('#detailsList').datagrid({
	url:getRootPath()+"/rate/saleOutDetail/query",
	fitColumns:true,
	singleSelect:true,
	showFooter:true,
	pagination:true,
	columns:[[
	          {field:"customerName",title:'客户编码',width:100},
	          {field:"saleDate",title:'客户名称',width:100},
	          {field:"unitName",title:'资金周转能力',width:100},
	          {field:"unitPrice",title:'资金周转稳定性',width:100}
	        ]],
});

</script>
</body>  
</html>
