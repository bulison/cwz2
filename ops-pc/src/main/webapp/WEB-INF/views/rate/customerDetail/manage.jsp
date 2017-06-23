<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户明细收益率</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/rateModel.css" />
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/echarts.min.js?v=${js_v}"></script>
<style>
.form1 p font{
	width:100px;
}
#charts{
    width:auto;
    height:300px;
    display: none;
}
</style>
</head>
<body>
	<div class="titleCustom">
         <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>客户明细收益率</h1>
         </div>             
    </div>
    
    <div class="myform">
        <div id="buttonBox">
            <a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a>
	       	<a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a>
        </div>
	    <div class="searchBox">
	        <form class="form1 inSearch" id='inSearch' >
	       		<p><font>客户：</font><input id="customerId" name="customerId" /> </p>
	       		<p><font>地区：</font><input id="area" name="area" /> </p>
	       		<p><font>类别：</font><input id="category" name="category" /> </p>
	       		<p><font>开始日期：</font><input id="startDay" name="startDay" /> </p>
	       		<p><font>结束日期：</font><input id="endDay" name="endDay" /> </p>
	        </form>
	    </div>
	</div>
	<div class="dataBox">
	    <div id="detailsList"></div>
	</div>
</body>
<script>
$('#mySearch').click(function(){
	var data = $('#inSearch').serializeJson();
	$('#detailsList').datagrid('load',data);
});

$('#clearForm').click(function(){
	$('#inSearch').form('clear');
});

//搜索条件初始化
$('#customerId').fool('combogrid',{
	prompt:"输入编码、名称搜索",
	width:162,
	height:30,
	panelWidth:300,
	panelHeight:270,
	idField:'fid',
	fitColumns:true,
	textField:'name',
	focusShow:true,
	url:getRootPath()+'/customer/vagueSearch?searchSize=8',
	columns:[[
               {field:'fid',title:'fid',hidden:true},
               {field:'code',title:'编码',width:30,searchKey:false},
               {field:'name',title:'名称',width:100,searchKey:false},
               {field:'area',title:'区域',width:30,searchKey:false},
               {field:'searchKey',hidden:true,searchKey:true},
             ]]
});
$('#startDay').fool('datebox',{
	prompt:"日期可选择可输入",
	inputDate:true,
	width:162,
	height:30,
});
$('#startDay').datebox('textbox').focus(function(){
	$('#startDay').datebox('showPanel');
});
$('#endDay').fool('datebox',{
	prompt:"日期可选择可输入",
	inputDate:true,
	width:162,
	height:30,
});
$('#endDay').datebox('textbox').focus(function(){
	$('#endDay').datebox('showPanel');
});

//数据载入
$('#detailsList').datagrid({
	url:getRootPath()+"/rate/customer/list",
	fitColumns:true,
	singleSelect:true,
	showFooter:true,
	pagination:true,
	columns:[[
              {field:"code",title:'客户编号',width:10},
              {field:"name",title:'客户名称',width:10},
              {field:"area",title:'地区',width:10},
              {field:"category",title:'客户类别',width:10},
              {field:"saleAmount",title:'销售金额',width:10},
              {field:"cost",title:'费用',width:10},
              {field:"rate",title:'收益',width:10,formatter:rateOut},
              {field:"totalRate",title:'累计加权收益率',width:10,formatter:rateOut},
              {field:"action",title:'操作',width:10,formatter:function(value,row,index){
            	  return '<a href="javascript:;" class="btn-detail" onclick="detailById(\''+row.fid+'\',\''+row.code+'\',\''+row.name+'\')" title="查看"></a>';
              }},
	        ]],
});

function rateOut(value,row,index){
	if(value){
		return value+"%";
	}
}

//明细按钮点击
function detailById(customerId,customerCode,customerName){
	$("#detailWindow").window("setTitle",customerCode+" "+customerName);
	$("#detailWindow").window("open");
	$("#detailWindow").window("refresh","${ctx}/rate/customer/detailPage?customerId="+customerId+"&startDate="+$("#startDay").datebox("getValue")+"&endDate="+$("#endDay").datebox("getValue"));
}
</script>
</html>
