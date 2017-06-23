<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>销售订单分析</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/rateModel.css" />
<script type="text/javascript" src="${ctx}/resources/js/echarts.min.js?v=${js_v}"></script>
<%-- <script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script> --%>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
.form1 p font{
	width:100px;
}
</style>
</head>
<body>
	   <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>销售订单分析</h1>
                </div>             
       </div>
	
	   <div class="myform">
	       <div id="buttonBox" style="margin-bottom:10px;width:100%;"> 
	       		<a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch" class='mySearch'>查询</a>
	       		<a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a>
           </div>
	       <div class="searchBox" style="background: #fff;margin:0 20px 10px 0;">
	       		<form class="form1 inSearch" id='inSearch'>
	       			<p><font>单号：</font><input id="code" name="code"/></p>
	       			<p><font>业务员：</font><input id="businessName" name="business"/></p>
	       			<p><font>客户：</font><input id="customerName" name="customerId"/></p>
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
$('#chartbtn').click(function(){//显示图标
	$(this).attr('class','chart-on');
	$('.list-on').attr('class','list-off');
	$('#detailsList').datagrid('getPanel').hide();
	$('#detailsChart').show();
	var row = $('#detailsList').datagrid('getRows');
	var xValue = [],yValue = [],y1Value = [],y2Value = [],y3Value = [];
	for(var i = 0; i < row.length; i++){
		xValue.push(row[i].code);
		yValue.push(row[i].expectRate);
		y1Value.push(row[i].currentRate);
		y2Value.push(row[i].marketRate);
		y3Value.push(row[i].realRate);
	}
	var option = {
			title:{
				text:"销售订单分析（单位：%）",
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


$('#listbtn').click(function(){//列表显示
	$(this).attr('class','list-on');
	$('.chart-on').attr('class','chart-off');
	$('#detailsChart').hide();
	$('#detailsList').datagrid('getPanel').show();
});



$('#mySearch').click(function(){
	$('#detailsChart').hide();
	$('#detailsList').datagrid('getPanel').show();
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

$('#businessName').fool('combogrid',{
	prompt:"业务员",
	width:162,
	height:30,
	panelWidth:300,
	panelHeight:270,
	idField:'fid',
	fitColumns:true,
	textField:'username',
	focusShow:true,
	url:getRootPath()+'/member/list',
	columns:[[
		{field:'fid',title:'fid',hidden:true},
		{field:'userCode',title:'编号',width:100,searchKey:false},
		{field:'jobNumber',title:'工号',width:100,searchKey:false},
		{field:'username',title:'名称',width:100,searchKey:false},
		{field:'phoneOne',title:'电话',width:100,searchKey:false},
		{field:'deptName',title:'部门',width:100,searchKey:false},
		{field:'position',title:'职位',width:100,searchKey:false},
		{field:'searchKey',hidden:true,searchKey:true},
             ]]
});
$('#customerName').fool('combogrid',{
	prompt:"客户",
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
//数据载入
$('#detailsList').datagrid({
	url:getRootPath()+"/rate/ratesaleorder/query",
	fitColumns:false,//定义滚动条
	singleSelect:true,
	showFooter:true,
	pagination:true,
	columns:[[
	          {field:"fid",title:"fid",hidden:true},
	          {field:"code",title:'销售订单号',width:100},
	          {field:"business",title:'业务员',width:100},
	          {field:"customerCode",title:'客户编码',width:100},
	          {field:"customerId",title:'客户ID',width:100,hidden:true},
	          {field:"customerName",title:'客户名称',width:100},
	          {field:"orderAmount",title:'订货金额',width:100,formatter:numOut},
	          {field:"orderDate",title:'订货日期',width:100},
	          {field:"planDate",title:'计划完成日期',width:100},
	          {field:"deliveryAmount",title:'已发货金额',width:100,formatter:numOut},
	          {field:"notDeliveryAmount",title:'欠发货金额',width:100,formatter:numOut},
	          {field:"firstDeliveryDate",title:'首次发货日期',width:100},
	          {field:"lastDeliveryDate",title:'未次发货日期',width:100},
	          {field:"returnAmount",title:'退货金额',width:100,formatter:numOut},
	          {field:"returnDate",title:'退货日期',width:100},
	          {field:"lastReceiveDate",title:'未次日期',width:100},
	          {field:"cycle",title:'周期',width:100},
	          {field:"receivedAmount",title:'已收金额',width:100,formatter:numOut},
	          {field:"notReceiveAmount",title:'欠收金额',width:100,formatter:numOut},
	          {field:"goodCost",title:'货品成本',width:100,formatter:numOut},
	          {field:"lastGoodCost",title:'最近货品成本金额',width:100,formatter:numOut},
	          {field:"saleFee",title:'销售费用',width:100,formatter:numOut},
	          {field:"expectReturn",title:'预计收益',width:100,formatter:numOut},
	          {field:"expectRate",title:'预计收益率',width:100,formatter:rateOut},
	          {field:"marketRate",title:'市场参考收益率',width:100,formatter:rateOut},
	          {field:"realRate",title:'实际收益率',width:100,formatter:rateOut},
	          {field:"currentRate",title:'当前收益率',width:100,formatter:rateOut},
	        ]],
	        onLoadSuccess:function(data){
	        	var row = $('#detailsList').datagrid('getRows');
	        	var length = row.length;
	        	var myvalue = new Object;
	        	 myvalue.orderAmount = myvalue.deliveryAmount = myvalue.notDeliveryAmount = myvalue.returnAmount
	        	= myvalue.receivedAmount = myvalue.notReceiveAmount = myvalue.goodsCost = myvalue.lastGoodCost
	        	= myvalue.saleFee= 0; 
	        	for(var i=0; i<length; i++){
	        		myvalue.orderAmount = myvalue.orderAmount + row[i].orderAmount;//订货金额
	        		myvalue.deliveryAmount = myvalue.deliveryAmount + row[i].deliveryAmount;//已发货金额
	        		myvalue.notDeliveryAmount = myvalue.notDeliveryAmount + row[i].notDeliveryAmount;//欠发货金额
	        		myvalue.returnAmount = myvalue.returnAmount + row[i].returnAmount;//退货金额
	        		myvalue.receivedAmount = myvalue.receivedAmount + row[i].receivedAmount;//已收金额
	        		myvalue.notReceiveAmount = myvalue.notReceiveAmount + row[i].notReceiveAmount;//欠收金额
	        		myvalue.goodsCost = myvalue.goodsCost + row[i].goodsCost;//货品成本
	        		//myvalue.lastGoodCost = myvalue.lastGoodCost + row[i].lastGoodCost;//最近货品成本金额
	        		myvalue.saleFee = myvalue.saleFee + row[i].saleFee;//销售费用
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

function rateOut(value,row,index){
	var myvalue = new Number(value);
	if(row.code=="合计：")
		return '';
	else if(isNaN(myvalue))
		return '0%';
	else 
		return myvalue+"%";
}

//enterSearch("mySearch"); // 回车搜索
</script>
</body>  
</html>
