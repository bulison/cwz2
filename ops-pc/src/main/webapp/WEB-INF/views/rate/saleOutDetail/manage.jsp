<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>销售出货单明细分析</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/rateModel.css" />
<%-- <script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script> --%>
<script type="text/javascript" src="${ctx}/resources/js/echarts.min.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
</style>
</head>
<body>
	   <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>销售出货单明细分析</h1>
                </div>             
       </div>
	
	   <div class="myform">
	       <div id="buttonBox" style=""> 
	       		<a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a>
	       		<a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a>
           </div>
	       <div class="searchBox" style="">
	       		<form class="form1 inSearch" id='inSearch'>
	       			<p><font>单号：</font><input id="code" name="code" /> </p>
	       			<p><font>货品：</font><input id="goodsName" name="goodsId" /> </p>
	       			<p><font>货品属性：</font><input id="goodsSpecName" name="goodsSpecId" /> </p>
	       			<p><font>客户：</font><input id="customerName" name="customerId" /> </p>
	       			<p><font>开始日期：</font><input id="startDay" name="startDay" /> </p>
	       			<p><font>结束日期：</font><input id="endDay" name="endDay" /> </p>
	       			<p class="listChart">
		       			<a id="listbtn" href="javascript:;" class="list-on" title="列表显示"></a>
		       			<a id="chartbtn" href="javascript:;" class="chart-off" title="图表显示"></a>
	       			</p>
	       		</form>
	       </div>
	   </div>
	   <div class="dataBox">
	       <div id="detailsList"></div>
	       <div id="detailsChart" style="width:100%;height:500px;display:none"></div>
	   </div>
	   
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$('#chartbtn').click(function(){
	$(this).attr('class','chart-on');
	$('.list-on').attr('class','list-off');
	$('#detailsList').datagrid('getPanel').hide();
	$('#detailsChart').show();
	var row = $('#detailsList').datagrid('getRows');
	var xValue = [],yValue = [],y1Value = [],y2Value = [],y3Value = [];
	for(var i = 0; i < row.length; i++){
		xValue.push(row[i].code+row[i].goodsName);
		yValue.push(row[i].expectRate);
		y1Value.push(row[i].currentRate);
		y2Value.push(row[i].marketRate);
		y3Value.push(row[i].realRate);
	}
	var option = {
			title:{
				text:"销售出货单收益明细分析",
				subtext:"单位：%"
			},
		    xAxis: {//列名
		        data: xValue
		    },
		    yAxis: {},
		    legend: {//可选择取消数据项
		        data: ['预计收益率', '当前收益率','市场收益率','实际收益率']
		    },
		    series: [{//多个数据项
		        name: '预计收益率',
		        type: 'bar',
		        data: yValue
		    },{
		        name: '当前收益率',
		        type: 'bar',
		        data: y1Value
		    },{
		        name: '市场收益率',
		        type: 'bar',
		        data: y2Value
		    },{
		        name: '实际收益率',
		        type: 'bar',
		        data: y3Value
		    }],
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            
		            type : 'shadow'
		        },
		   		formatter: function (params, ticket, callback) {
					var mystr = params[0].name;
					for(var i=0; i<params.length; i++){
						mystr = mystr + '<br/><span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:'+params[i].color+'"></span>'+params[i].seriesName+":"+params[i].value+"%";
					}
     				return mystr;
 				}
		    },
		};
	var myChart = echarts.init(document.getElementById('detailsChart'));
	myChart.setOption(option);
	
});
$('#listbtn').click(function(){
	$(this).attr('class','list-on');
	$('.chart-on').attr('class','chart-off');
	$('#detailsChart').hide();
	$('#detailsList').datagrid('getPanel').show();
});

$('#mySearch').click(function(){
	$('#listbtn').click();
	var data = $('#inSearch').serializeJson();
	$('#detailsList').datagrid('load',data);
});

$('#clearForm').click(function(){
	$('#inSearch').form('clear');
});

//搜索条件初始化
$('#code').textbox({
	prompt:"输入单号",
	width:162,
	height:30
});
$('#goodsName').fool('combogrid',{
	prompt:"输入编码、名称搜索",
	width:162,
	height:30,
	panelWidth:300,
	panelHeight:270,
	idField:'fid',
	fitColumns:true,
	textField:'name',
	focusShow:true,
	url:getRootPath()+'/goods/vagueSearch?searchSize=8',
	columns:[[
               {field:'fid',title:'fid',hidden:true},
               {field:'name',title:'名称',width:100,searchKey:false},
               {field:'code',title:'编码',width:30,searchKey:false},
               {field:'barCode',title:'条码',width:30,searchKey:false},
               {field:'searchKey',hidden:true,searchKey:true},
             ]]
});
$('#goodsSpecName').fool('combogrid',{
	prompt:"输入编码、名称搜索",
	width:162,
	height:30,
	panelWidth:300,
	panelHeight:270,
	idField:'fid',
	fitColumns:true,
	textField:'name',
	focusShow:true,
	url:getRootPath()+'/goodsspec/vagueSearch?searchSize=8',
	columns:[[
				{field:'fid',title:'fid',hidden:true},
				{field:'name',title:'名称',width:100,searchKey:false},
				{field:'code',title:'编码',width:30,searchKey:false},
				{field:'searchKey',hidden:true,searchKey:true},
             ]]
});
$('#customerName').fool('combogrid',{
	prompt:"输入编码、名称搜索",
	width:162,
	height:30,
	idField:'fid',
	textField:'name',
	fitColumns:true,
	focusShow:true,
	panelWidth:300,
	panelHeight:283,
	url:getRootPath()+'/customer/vagueSearch?searchSize=8',
	columns:[[
		{field:'fid',title:'fid',hidden:true},
		{field:'code',title:'编号',width:100,searchKey:false},
		{field:'name',title:'名称',width:100,searchKey:false},
		{field:'categoryName',title:'分类',width:100,searchKey:false},
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
	url:getRootPath()+"/rate/saleOutDetail/query",
	//fitColumns:true,
	singleSelect:true,
	showFooter:true,
	pagination:true,
	frozenColumns:[[
					{field:"code",title:'单据编号',width:100},
					{field:"goodsName",title:'货品名称',width:100},
	                ]],
	columns:[[
	          {field:"billId",title:'billId',hidden:true},
	          
	          {field:"goodsCode",title:'货品编码',width:100},
	          {field:"goodsSpecName",title:'货品属性',width:100},
	          {field:"customerName",title:'客户名称',width:100},
	          {field:"saleDate",title:'销售日期',width:100,formatter:dateFormatOut},
	          {field:"unitName",title:'单位名称',width:100},
	          {field:"unitPrice",title:'价格',width:80,formatter:numOut},
	          {field:"saleAmount",title:'销售金额',width:80,formatter:numOut},
	          {field:"planDate",title:'计划完成日期',width:100,formatter:dateFormatOut},
	          {field:"returnQuentity",title:'退货数量',width:50,formatter:numOut},
	          {field:"returnAmount",title:'退货金额',width:80,formatter:numOut},
	          {field:"returnDate",title:'退货日期',width:100,formatter:dateFormatOut},
	          {field:"lastReceiveDate",title:'末次收款日期',width:100,formatter:dateFormatOut},
	          {field:"cycle",title:'周期',width:50},
	          {field:"receivedAmount",title:'已收金额',width:80,formatter:numOut},
	          {field:"notReceiveAmount",title:'未收金额',width:80,formatter:numOut},
	          {field:"goodsCost",title:'货品成本',width:80,formatter:numOut},
	          {field:"saleCharge",title:'销售费用',width:80,formatter:numOut},
	          {field:"expectReturn",title:'预计收益',width:80,formatter:numOut},
	          {field:"expectRate",title:'预计收益率',width:80,formatter:rateOut},
	          {field:"marketRate",title:'市场收益率',width:80,formatter:rateOut},
	          {field:"realRate",title:'实际收益率',width:80,formatter:rateOut},
	          {field:"currentRate",title:'当前收益率',width:80,formatter:rateOut},
	          {field:"scale",title:'货品金额占总金额的比例',width:80,formatter:rateOut},
	        ]],
	        onLoadSuccess:function(data){
	        	var row = $('#detailsList').datagrid('getRows');
	        	var length = row.length;
	        	var myvalue = new Object;
	        	myvalue.saleAmount = myvalue.returnQuentity = myvalue.returnAmount = myvalue.receivedAmount
	        	= myvalue.notReceiveAmount = myvalue.saleCharge = myvalue.goodsCost = 0;
	        	for(var i=0; i<length; i++){
	        		myvalue.saleAmount = myvalue.saleAmount + row[i].saleAmount;
	        		myvalue.returnQuentity = myvalue.returnQuentity + row[i].returnQuentity;
	        		myvalue.returnAmount = myvalue.returnAmount + row[i].returnAmount;
	        		myvalue.receivedAmount = myvalue.receivedAmount + row[i].receivedAmount;
	        		myvalue.notReceiveAmount = myvalue.notReceiveAmount + row[i].notReceiveAmount;
	        		myvalue.saleCharge = myvalue.saleCharge + row[i].saleCharge;
	        		myvalue.goodsCost = myvalue.goodsCost + row[i].goodsCost;
	        	}
	        	var data = $.extend({code:"合计："},myvalue);
	        	$('#detailsList').datagrid('reloadFooter',[data]);
	        }
});

function numOut(value,row,index){
	var myvalue = new Number(value);
	if(isNaN(myvalue))
		return 0;
	else 
		return myvalue;
}

function dateFormatOut(value,row,index){
	return getDateStr(value);
}

function rateOut(value,row,index){
	var myvalue = new Number(value);
	if(row.code=="合计：")
		return '';
	else if(isNaN(myvalue))
		return '0%';
	else 
		return myvalue+"%";
}
</script>
</body>  
</html>
