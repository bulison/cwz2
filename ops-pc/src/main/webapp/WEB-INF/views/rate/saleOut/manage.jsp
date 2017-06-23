<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>销售出货单分析</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/rateModel.css" />
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/echarts.min.js?v=${js_v}"></script>
<style>
.form1 p font{
	width:100px;
}
#detailsChart{
    width:auto;
    height:300px;
    display: none;
}
#viewer{
  float:right
}
.list-on{
  display:inline-block;
  margin:0 10px;
  padding:20px;
  width:0px;
  height:0px;
  background: url("${ctx}/resources/images/list_on.png") no-repeat;
}
.list-off{
  display:inline-block;
  margin:0 10px;
  padding:20px;
  width:0px;
  height:0px;
  background: url("${ctx}/resources/images/list.png") no-repeat;
}
.chart-on{
  display:inline-block;
  margin:0 10px;
  padding:20px;
  width:0px;
  height:0px;
  background: url("${ctx}/resources/images/chart_on.png") no-repeat;
}
.chart-off{
  display:inline-block;
  margin:0 10px;
  padding:20px;
  width:0px;
  height:0px;
  background: url("${ctx}/resources/images/chart.png") no-repeat;
}
</style>
</head>
<body>
	<div class="titleCustom">
         <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>销售出货单分析</h1>
         </div>             
    </div>
    
    <div class="myform">
	    <div class="searchBox" >
	        <form class="form1 inSearch" id='inSearch' >
	            <p><font>单号：</font><input id="code" name="code" /> </p>
	       		<p><font>货品：</font><input id="goodsName" name="goodsId" /> </p>
	       		<p><font>货品属性：</font><input id="goodsSpecName" name="goodsSpecId" /> </p>
	       		<p><font>开始日期：</font><input id="startDay" name="startDay" /> </p>
	       		<p><font>结束日期：</font><input id="endDay" name="endDay" /> </p>
	       		<p><font></font><a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a>
	       	    <a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a></p>
	       		<p id="viewer">
	       		  <a id="viewer-list" class="list-on" title="列表显示" href="javascript:;"></a>
	       		  <a id="viewer-chart" class="chart-off" title="图表显示" href="javascript:;"></a>
	       		</p>
	        </form>
	    </div>
	</div>
	<div class="dataBox">
	    <div id="detailsList"></div>
	    <div id="detailsChart"></div>
	</div>
</body>
<script>
var codes =[];
var expectRates =[];
var marketRates=[];
var realRates=[];
var currentRates=[];
$.ajax({
	url:getRootPath()+"/rate/saleOut/query",
	async:false,
	data:{},
	success:function(data){
		var rows=data.rows;
		names=[];
		rates=[];
		if(rows){
			for(var i=0;i<rows.length;i++){
				codes.push(rows[i].code);
				expectRates.push(rows[i].expectRate);
				marketRates.push(rows[i].marketRate);
				realRates.push(rows[i].realRate);
				currentRates.push(rows[i].currentRate);
			}
		}
	}
});

$('#mySearch').click(function(){
	var data = $('#inSearch').serializeJson();
	$('#detailsList').datagrid('load',data);
	/* $("#viewer-list").click(); */
	var newOption={
			xAxis: {
				name:'单号',
				data:codes
			},
			series: [{
				name: '预计收益率',
				type: 'bar',
				data: expectRates,
			},{
				name: '市场收益率',
				type: 'bar',
				data: marketRates,
			},{
				name: '实际收益率',
				type: 'bar',
				data: realRates,
			},{
				name: '当前收益率',
				type: 'bar',
				data: currentRates,
			}],
	};
	chart.setOption(newOption);
});

$('#clearForm').click(function(){
	$('#inSearch').form('clear');
});

//视图切换 列表
$("#viewer-list").click(function(){
	if($(".datagrid").css("display")=="none"){
		$(this).attr("class","list-on");
		$("#viewer-chart").attr("class","chart-off");
	}
	$(".datagrid").show();
	$("#detailsChart").hide();
	$("#detailsList").datagrid("reload");
	chart.resize();
});

//视图切换 图表
$("#viewer-chart").click(function(){
	if($("#detailsChart").css("display")=="none"){
		$(this).attr("class","chart-on");
		$("#viewer-list").attr("class","list-off");
	}
	$(".datagrid").hide();
	$("#detailsChart").show();
	chart.resize();
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
	url:getRootPath()+"/rate/saleOut/query",
	singleSelect:true,
	showFooter:true,
	pagination:true,
	columns:[[
	          {field:"billId",title:'billId',hidden:true},
	          {field:"code",title:'单据编号',width:100},
	          {field:"business",title:'业务员',width:100},
	          {field:"customerName",title:'客户名称',width:100},
	          {field:"saleAmount",title:'销售金额',width:100},
	          {field:"saleDate",title:'销售日期',width:100,formatter:dateFormatOut},
	          {field:"planDate",title:'计划完成日期',width:100,formatter:dateFormatOut},
	          {field:"returnQuentity",title:'退货数量',width:50},
	          {field:"returnAmount",title:'退货金额',width:100},
	          {field:"returnDate",title:'退货日期',width:100,formatter:dateFormatOut},
	          {field:"lastReceiveDate",title:'末次收款日期',width:100,formatter:dateFormatOut},
	          {field:"cycle",title:'周期',width:50},
	          {field:"receivedAmount",title:'已收金额',width:100},
	          {field:"notReceiveAmount",title:'未收金额',width:100},
	          {field:"goodCost",title:'货品成本',width:100},
	          {field:"saleFee",title:'销售费用',width:100},
	          {field:"expectReturn",title:'预计收益',width:100},
	          {field:"expectRate",title:'预计收益率',width:100,formatter:rateOut},
	          {field:"marketRate",title:'市场收益率',width:100,formatter:rateOut},
	          {field:"realRate",title:'实际收益率',width:100,formatter:rateOut},
	          {field:"currentRate",title:'当前收益率',width:100,formatter:rateOut},
	        ]],
	onLoadSuccess:function(data){
		var rows=data.rows;
		codes=[];
		expectRates =[];
		marketRates=[];
		realRates=[];
		currentRates=[];
		if(rows){
			for(var i=0;i<rows.length;i++){
				codes.push(rows[i].code);
				expectRates.push(rows[i].expectRate);
				marketRates.push(rows[i].marketRate);
				realRates.push(rows[i].realRate);
				currentRates.push(rows[i].currentRate);
			}
		}
		var saleAmount=0;
		var returnQuentity=0;
		var returnAmount=0;
		var receivedAmount=0;
		var notReceiveAmount=0;
		var goodsCost=0;
		var saleFee=0;
		var expectReturn=0;
		var expectRate=0;
		var marketRate=0;
		var realRate=0;
		var currentRate=0;
		for(var i=0;i<rows.length;i++){
			if(rows[i].saleAmount){
				saleAmount=rows[i].saleAmount+saleAmount;
			}
			if(rows[i].returnQuentity){
				returnQuentity=rows[i].returnQuentity+returnQuentity;
			}
			if(rows[i].returnAmount){
				returnAmount=rows[i].returnAmount+returnAmount;
			}
			if(rows[i].receivedAmount){
				receivedAmount=rows[i].receivedAmount+receivedAmount;
			}
			if(rows[i].notReceiveAmount){
				notReceiveAmount=rows[i].notReceiveAmount+notReceiveAmount;
			}
			if(rows[i].goodsCost){
				goodsCost=rows[i].goodsCost+goodsCost;
			}
			if(rows[i].saleFee){
				saleFee=rows[i].saleFee+saleFee;
			}
			if(rows[i].expectReturn){
				expectReturn=rows[i].expectReturn+expectReturn;
			}
			if(rows[i].expectRate){
				expectRate=rows[i].expectRate+expectRate;
			}
			if(rows[i].marketRate){
				marketRate=rows[i].marketRate+marketRate;
			}
			if(rows[i].realRate){
				realRate=rows[i].realRate+realRate;
			}
			if(rows[i].currentRate){
				currentRate=rows[i].currentRate+currentRate;
			}
		}
		$('#detailsList').datagrid('reloadFooter',[{
    		code:"合计:",
    		saleAmount:saleAmount,
    		returnQuentity:returnQuentity,
    		returnAmount:returnAmount,
    		receivedAmount:receivedAmount,
    		notReceiveAmount:notReceiveAmount,
    		goodsCost:goodsCost,
    		saleFee:saleFee,
    		expectReturn:expectReturn,
    		expectRate:expectRate,
    		marketRate:marketRate,
    		realRate:realRate,
    		currentRate:currentRate
		}]);
	}
});

//图表属性
var option = {
		title: {
			text: '销售出货单分析'
		},
		legend: {
	        data: ['预计收益率', '市场收益率','实际收益率','当前收益率'],
	        align: 'left'
	    },
		tooltip: {},
		xAxis: {
			name:'单号',
			data:codes
		},
		yAxis: {
			name:'(%)'
		},
		series: [{
			name: '预计收益率',
			type: 'bar',
			data: expectRates,
			label: {
				normal: {
					show: true,
					position: 'top'
				}
			}
		},{
			name: '市场收益率',
			type: 'bar',
			data: marketRates,
			label: {
				normal: {
					show: true,
					position: 'top'
				}
			}
		},{
			name: '实际收益率',
			type: 'bar',
			data: realRates,
			label: {
				normal: {
					show: true,
					position: 'top'
				}
			}
		},{
			name: '当前收益率',
			type: 'bar',
			data: currentRates,
			label: {
				normal: {
					show: true,
					position: 'top'
				}
			}
		}],
		animationEasing: 'elasticOut',
		animationDelayUpdate: function (idx) {
			return idx * 5;
		}
};

//图表初始化
var dom=document.getElementById("detailsChart");
var chart=echarts.init(dom);
chart.setOption(option);

function dateFormatOut(value,row,index){
	return getDateStr(value);
}

function rateOut(value,row,index){
	if(value){
		return value+"%";
	}else{
		return "0%";
	}
}
</script>
</html>
