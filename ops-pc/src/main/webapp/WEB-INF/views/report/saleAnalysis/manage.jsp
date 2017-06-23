<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>销售分析</title>
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
            <h1>销售分析</h1>
         </div>             
    </div>
    <div class="reportType" style="margin-bottom:10px;">
    	<input id="reportType" />
    </div>
    <div class="myform">
	    <div class="searchBox" >
	        <form class="form1 inSearch" id='inSearch' >
	            <p><font>单号：</font><input id="code" name="saleCode" /> </p>
	       		<p><font>开始日期：</font><input id="startDay" name="startDate" /> </p>
	       		<p><font>结束日期：</font><input id="endDay" name="endDate" /> </p>
	       		<p><font>客户：</font><input id="customerName" name="customerId" /> </p>
	       		<p><font>业务员：</font><input id="businessName" name="saleId"/></p>
	       		<span class="details-search" style="display:none;">
		       		<p><font>货品：</font><input id="goodsName" name="goodsId" /> </p>
		       		<p><font>货品属性：</font><input id="goodsSpecName" name="goodsSpecId" /> </p>
	       		</span>
	       		<p><font></font><a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a>
	       	    <a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a></p>
	       		<!-- <p id="viewer">
	       		  <a id="viewer-list" class="list-on" title="列表显示" href="javascript:;"></a>
	       		  <a id="viewer-chart" class="chart-off" title="图表显示" href="javascript:;"></a>
	       		</p> -->
	        </form>
	    </div>
	</div>
	<div class="dataBox">
	    <table id="detailsList"></table>
	    <div id="pager"></div>
	    <!-- <div id="detailsChart"></div> -->
	</div>
	<div id="pop-win"></div>
</body>
<script>
var codes =[];
var expectRates =[];
var marketRates=[];
var realRates=[];
var currentRates=[];
var win = ""
var myselect = 1;
var member="",customer="",goods="";
$.ajax({
	url:getRootPath()+"/basedata/query?num="+Math.random(),
	async:false,
    data:{param:"Member,Customer,Goods"},
    success:function(data){
    	member=formatData(data.Member,"fid");
    	customer=formatData(data.Customer,"fid");
    	goods=formatData(data.Goods,"fid");
    }
});
/* $.ajax({
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
}); */

$('#reportType').fool("dhxCombo",{
	width:200,
	height:30,
	focusShow:true,
	editable:false,
	clearOpt:false,
	value:1,
	data:[{value:1,text:"销售订单分析"},{value:2,text:"销售订单明细分析"},{value:3,text:"销售出库单分析"},{value:4,text:"销售出库单明细分析"}],
	onChange:function(value,text){
		toggleSelect(value);
		myselect = value;
	}
});

function toggleSelect(value){
	$.messager.progress({
		text:'加载中，请稍后...'
	});
	$('#clearForm').click();
	var data = $('#inSearch').serializeJson();
	$('#detailsList').jqGrid('setGridParam',{postData:data});
	if(value == 1){
		$('.details-search').hide();
		$('#detailsList').jqGrid('hideCol',[
			"salesMan",
			"customerName",
			"planFinishDate",
			"goodsCode",
			"goodCode",
			"goodsName",
			"goodName",
			"goodsSpecName",
			"specName",
			"goodsUnit",
			"goodUnit",
			"bookTotal",
			"bookPrice",
			"saleQuantity",
			"salePrice",
			"saleAmount",
			"deliveredTotal",
			"undeliveredTotal",
			"unDeliveredAmount",
			"backTotal",
			"backQuantity",
			"backCost",
			"goodCost",
			"profit",
			"totalSaleExp",
			"estimatedYieldRate",
			"referenceYieldRate",
			"effectiveYieldRate",
			"currentYieldRate",
		]);
		$('#detailsList').jqGrid('showCol',["saleCode","sale","supplierName","saleDate","finishDate","amount","deliveryAmount","hasIncome","notIncome","backAmount","saleExp","goodsFee","lastDate","lastIncomeDate","lastBackDate"]);
		$('td[aria-describedby=detailsList_action] a.btn-detail').hide();
		$('#detailsList').jqGrid("setGridParam",{
			datatype:function(postdata){
				$.ajax({
					url:getRootPath()+"/api/rate/saleOrder/orderAnalyze",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#detailsList")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			}});
	}else if(value == 2){
		$('.details-search').show();
		$('#detailsList').jqGrid('hideCol',[
			"salesMan",
			"customerName",
			"planFinishDate",
			"goodCode",
			"goodName",
			"specName",
			"goodUnit",
			"saleQuantity",
			"salePrice",
			"saleAmount",
		    "hasIncome",
		    "notIncome",
		    "saleExp",
		    "lastIncomeDate",
			"backQuantity",
			"backCost",
			"goodCost",
			"profit",
			"totalSaleExp",
			"estimatedYieldRate",
			"referenceYieldRate",
			"effectiveYieldRate",
			"currentYieldRate",
		]);
		$('#detailsList').jqGrid('showCol',["saleCode","sale","supplierName","saleDate","finishDate","goodsCode","goodsName","goodsSpecName","goodsUnit","bookTotal","bookPrice","amount","deliveredTotal","deliveryAmount","undeliveredTotal","unDeliveredAmount","backTotal","backAmount","goodsFee","lastDate","lastBackDate"]);
		$('td[aria-describedby=detailsList_action] a.btn-detail').hide();
		$('#detailsList').jqGrid("setGridParam",{
			datatype:function(postdata){
				$.ajax({
					url:getRootPath()+"/api/rate/saleOrder/detailAnalyze",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#detailsList")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			}});
	}else if(value == 3){
		$('.details-search').hide();
		$('#detailsList').jqGrid('hideCol',[
			"sale",	     
			"supplierName",
			"finishDate",	    
			"goodsCode",	     
			"goodCode",
			"goodsName",
			"goodName",
			"goodsSpecName",
			"specName",	    
			"goodsUnit",
			"goodUnit",
			"bookTotal",
			"bookPrice",
			"amount",
			"saleQuantity",
			"salePrice",
			"deliveredTotal",
			"deliveryAmount",
			"undeliveredTotal",
			"unDeliveredAmount",
			"backTotal",
			"backQuantity",
			"goodsFee",
			"totalSaleExp",
			"lastDate",
		]);
		$('#detailsList').jqGrid('showCol',["saleCode","salesMan","customerName","saleDate","planFinishDate","saleAmount","hasIncome","notIncome","backAmount","saleExp","goodCost","backCost","profit","estimatedYieldRate","referenceYieldRate","effectiveYieldRate","currentYieldRate","lastIncomeDate","lastBackDate",]);
		$('td[aria-describedby=detailsList_action] a.btn-detail').show();
		$('#detailsList').jqGrid("setGridParam",{
			datatype:function(postdata){
				$.ajax({
					url:getRootPath()+"/api/rate/saleOutAnalyze",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#detailsList")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			}});
	}else{
		$('.details-search').show();
		$('#detailsList').jqGrid('hideCol',[
			"sale",	     
			"supplierName",
			"finishDate",	    
			"goodsCode",	     
			"goodsName",
			"goodsSpecName",
			"goodsUnit",
			"bookTotal",
			"bookPrice",
			"amount",
			"deliveredTotal",
			"deliveryAmount",
			"undeliveredTotal",
			"unDeliveredAmount",
			"backTotal",
			"goodsFee",
			"totalSaleExp",
			"lastDate",
            "hasIncome",
            "notIncome",
            "saleExp",
            "profit",
            "estimatedYieldRate",
            "referenceYieldRate",
            "effectiveYieldRate",
            "currentYieldRate",
            "lastIncomeDate",
		]);
		$('#detailsList').jqGrid('showCol',["saleCode","salesMan","customerName","saleDate","planFinishDate","goodCode","goodName","specName","goodUnit","saleQuantity","salePrice","saleAmount","backQuantity","backAmount","goodCost","backCost","lastBackDate",]);
		$('td[aria-describedby=detailsList_action] a.btn-detail').hide();
		$('#detailsList').jqGrid("setGridParam",{
			datatype:function(postdata){
				$.ajax({
					url:getRootPath()+"/api/rate/saleOutDetailAnalyze",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#detailsList")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			}});
	}
	$('#detailsList').jqGrid("setGridWidth",$(window).width()-20);
	$.messager.progress('close');
}

$('#mySearch').click(function(){
	$('#inSearch').form("enableValidation");
	if(!$('#inSearch').form("validate")){
		return false;
	}
	var data = $('#inSearch').serializeJson();
	if(myselect == 4){//出库单明细的货品搜索字段不一样
		data.goodId = data.goodsId;
		data.goodSpecId = data.goodsSpecId;
	}
	$('#detailsList').jqGrid('setGridParam',{postData:data}).trigger("reloadGrid");
});

$('#clearForm').click(function(){
	$('#inSearch').form('clear');
	$('#startDay').datebox('setValue',new Date().format("yyyy-MM-dd"));
	$('#endDay').datebox('setValue',new Date().format("yyyy-MM-dd"));
	$('#inSearch').find("input.dhxCombo-f").each(function(){
		$(this).next()[0].comboObj.setComboText("");
	});
});

//视图切换 列表
/* $("#viewer-list").click(function(){
	if($(".datagrid").css("display")=="none"){
		$(this).attr("class","list-on");
		$("#viewer-chart").attr("class","chart-off");
	}
	$(".datagrid").show();
	$("#detailsChart").hide();
	$("#detailsList").datagrid("reload");
	chart.resize();
}); */

//视图切换 图表
/* $("#viewer-chart").click(function(){
	if($("#detailsChart").css("display")=="none"){
		$(this).attr("class","chart-on");
		$("#viewer-list").attr("class","list-off");
	}
	$(".datagrid").hide();
	$("#detailsChart").show();
	chart.resize();
}); */

//搜索条件初始化
$('#code').textbox({
	prompt:"输入单号",
	width:162,
	height:30
});
$('#customerName').fool('dhxComboGrid',{
	prompt:"输入编码、名称搜索",
	width:162,
	height:30,
	focusShow:true,
    hasDownArrow:false,
    data:customer,
    toolsBar:{
		refresh:true
	},
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
  	},
	filterUrl:getRootPath()+'/customer/vagueSearch?searchSize=8'
});
$('#businessName').fool('dhxComboGrid',{
	prompt:"选择业务员",
	width:162,
	height:30,
    hasDownArrow:false,
	focusShow:true,
	toolsBar:{
		refresh:true
	},
	data:member,
	filterUrl:getRootPath()+'/member/vagueSearch',
	setTemplate:{
		input:"#username#",
		columns:[
					{option:'#userCode#',header:'编号',width:100},
					{option:'#jobNumber#',header:'工号',width:100},
					{option:'#username#',header:'名称',width:100},
					{option:'#phoneOne#',header:'电话',width:100},
					{option:'#deptName#',header:'部门',width:100},
				]
  	}
});
var goodsCombo = $('#goodsName').fool('dhxComboGrid',{
	prompt:"输入编码、名称搜索",
	width:162,
	height:30,
    hasDownArrow:false,
	focusShow:true,
	data:goods,
	filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=8',
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#name#',header:'名称',width:100},
					{option:'#code#',header:'编码',width:100},
					{option:'#barCode#',header:'条形码',width:100}
				]
  	},
  	onChange:function(value,text){
  		var record = goodsCombo.getSelectedText();
  		var specCombo = $('#goodsSpecName').next()[0].comboObj;
  		specCombo.disable();
  		if(!record.goodsSpecGroupId){
  			return false;
  		}
  		var myspecData = "";
		$.ajax({
		 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+record.goodsSpecGroupId,
			async:false,
	        data:{},
	        success:function(data){
	        	myspecData=formatData(data,"fid");
	        }
		});
		$('#goodsSpecName').fool('dhxComboGrid',{
			prompt:"输入编码、名称搜索",
			width:162,
			height:30,
		    hasDownArrow:false,
			focusShow:true,
			toolsBar:{
				refresh:true
			},
			filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+record.goodsSpecGroupId,
			data:myspecData
		});
		specCombo.setComboText('');
		"undefined"!=typeof $(specCombo.getInput()).attr('disabled')&&$(specCombo.getInput()).attr('disabled')=='disabled'?specCombo.enable():null
  	}
});
var goodsSpecCombo = $('#goodsSpecName').fool('dhxComboGrid',{
	prompt:"输入编码、名称搜索",
	width:162,
	height:30,
	toolsBar:{
		refresh:true
	},
    hasDownArrow:false,
	focusShow:true,
	filterUrl:getRootPath()+'/goodsspec/vagueSearch?searchSize=8',
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#name#',header:'名称',width:100},
					{option:'#code#',header:'编码',width:100}
				]
  	}
});
goodsSpecCombo.disable();
$('#startDay').fool('datebox',{
	prompt:"日期可选择可输入",
	inputDate:true,
	required:false,
	value:new Date().format("yyyy-MM-dd"),
	width:162,
	height:30,
});
$('#startDay').datebox('textbox').focus(function(){
	$('#startDay').datebox('showPanel');
});
$('#endDay').fool('datebox',{
	prompt:"日期可选择可输入",
	inputDate:true,
	required:false,
	value:new Date().format("yyyy-MM-dd"),
	width:162,
	height:30,
	validType:"dateCompare",
});
$('#endDay').datebox('textbox').focus(function(){
	$('#endDay').datebox('showPanel');
});
$.extend($.fn.validatebox.defaults.rules, {
	dateCompare:{
	 		validator:function(value){
	 			var end=new Date(value.replace("-", "/").replace("-", "/"));
		    	var start=new Date($('#startDay').datebox("getValue").replace("-", "/").replace("-", "/"));
		    	if($('#startDay').datebox("getValue")){
					return end - start >= 0;
				}else{
					return true;
				}
		    	
	 	 	},
	  		message:'结束日期不能比开始日期早！'
	}
});

//数据载入
$('#detailsList').jqGrid({
	datatype:"local",
	forceFit:true,
	pager:'#pager',
	footerrow: true,
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	shrinkToFit:false,
	autoScroll: false,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
// 	autowidth:true,//自动填满宽度
	width:$(window).width()-20,
	height:$(window).outerHeight()-350+"px",
	colModel:[
	          {name:"billId",label:'billId',hidden:true,frozen:true},
	          {name:"saleId",label:'saleId',hidden:true,frozen:true},
	          {name:"detailId",label:'saleId',hidden:true,frozen:true},
	          {name:"supplierCode",label:'supplierCode',hidden:true,frozen:true},
	          {name:"action",label:'操作',frozen:true,sortable:false,align:'center',width:100,formatter:function(value,options,row){
				  var detail = "<a href=\"javascript:;\" class=\"btn-detail\" title=\"明细\" onclick=\"viewDetails('"+row.saleId+"')\" ></a>";
				  return detail;
			  }},
	          {name:"saleCode",label:'单号',sortable:false,align:'center',width:150,frozen:true,},
	          {name:"sale",label:'业务员',sortable:false,align:'center',width:100},
	          {name:"salesMan",label:'业务员',sortable:false,align:'center',width:100,hidden:true},
	          {name:"supplierName",label:'供应商名称',sortable:false,align:'center',width:100,},
	          {name:"customerName",label:'客户名称',sortable:false,align:'center',width:100,hidden:true},
	          {name:"saleDate",label:'订单日期',sortable:false,align:'center',width:100,formatter:dateFormatOut,},
	          {name:"finishDate",label:'计划完成日期',sortable:false,align:'center',width:100,formatter:dateFormatOut},
	          {name:"planFinishDate",label:'计划完成日期',sortable:false,align:'center',width:100,formatter:dateFormatOut,hidden:true},
	          {name:"goodsCode",label:"货品编码",sortable:false,align:'center',width:100,hidden:true},
	          {name:"goodCode",label:"货品编码",sortable:false,align:'center',width:100,hidden:true},
	          {name:"goodsName",label:"货品名称",sortable:false,align:'center',width:100,hidden:true},
	          {name:"goodName",label:"货品名称",sortable:false,align:'center',width:100,hidden:true},
	          {name:"goodsSpecName",label:"货品属性",sortable:false,align:'center',width:100,hidden:true},
	          {name:"specName",label:"货品属性",sortable:false,align:'center',width:100,hidden:true},
	          {name:"goodsUnit",label:"货品单位",sortable:false,align:'center',width:100,hidden:true},
	          {name:"goodUnit",label:"货品单位",sortable:false,align:'center',width:100,hidden:true},
	          {name:"bookTotal",label:"订货数量",sortable:false,align:'center',width:100,hidden:true},
	          {name:"bookPrice",label:"订货价格",sortable:false,align:'center',width:100,hidden:true},
	          {name:"amount",label:'订货金额',sortable:false,align:'center',width:100},
	          {name:"saleQuantity",label:'销售数量',sortable:false,align:'center',width:100,hidden:true},
	          {name:"salePrice",label:'销售单价',sortable:false,align:'center',width:100,hidden:true},
	          {name:"saleAmount",label:'销售金额',sortable:false,align:'center',width:100,hidden:true},
	          {name:"deliveredTotal",label:"已发货数量",sortable:false,align:'center',width:100,hidden:true},
	          {name:"deliveryAmount",label:'已发货金额',sortable:false,align:'center',width:100}, 
	          {name:"undeliveredTotal",label:"欠发货数量",sortable:false,align:'center',width:100,hidden:true},
	          {name:"unDeliveredAmount",label:"欠发货金额",sortable:false,align:'center',width:100,hidden:true},
	          {name:"hasIncome",label:'已收金额',sortable:false,align:'center',width:100},
	          {name:"notIncome",label:'欠收金额',sortable:false,align:'center',width:100},
	          {name:"backTotal",label:"退货数量",sortable:false,align:'center',width:100,hidden:true},
	          {name:"backQuantity",label:"退货数量",sortable:false,align:'center',width:100,hidden:true},
	          {name:"backAmount",label:'退货金额',sortable:false,align:'center',width:50},
	          {name:"backCost",label:'退货成本',sortable:false,align:'center',width:50,hidden:true},
	          {name:"goodsFee",label:'货品成本',sortable:false,align:'center',width:100},
	          {name:"goodCost",label:'货品成本',sortable:false,align:'center',width:100,hidden:true},
	          {name:"percentageAmount",label:'提成金额',sortable:false,align:'center',width:100},
	          {name:"saleExp",label:'销售费用',sortable:false,align:'center',width:100},
	          {name:"saleExp",label:'销售费用',sortable:false,align:'center',width:100},
	          {name:"profit",label:'利润',sortable:false,align:'center',width:100,hidden:true},
	          {name:"lastDate",label:'末次发货日期',sortable:false,align:'center',width:100,formatter:dateFormatOut},
	          {name:"lastIncomeDate",label:'末次收款日期',sortable:false,align:'center',width:100,formatter:dateFormatOut},
	          {name:"lastBackDate",label:'末次退货日期',sortable:false,align:'center',width:100,formatter:dateFormatOut},
	          {name:"estimatedYieldRate",label:'预计收益率',sortable:false,align:'center',width:100,hidden:true},
	          {name:"referenceYieldRate",label:'参考收益率',sortable:false,align:'center',width:100,hidden:true},
	          {name:"effectiveYieldRate",label:'实际收益率',sortable:false,align:'center',width:100,hidden:true},
	          {name:"currentYieldRate",label:'当前预计收益率',sortable:false,align:'center',width:100,hidden:true},
	        ],
	gridComplete:function(){
		if($('#detailsList').getGridParam("datatype")=="local"){
			$('#detailsList').jqGrid("setGridParam",{
				datatype:function(postdata){
					$.ajax({
						url:getRootPath()+"/api/rate/saleOrder/orderAnalyze",
						data:postdata,
				        dataType:"json",
				        complete: function(data,stat){
				        	if(stat=="success") {
				        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
				        		$("#detailsList")[0].addJSONData(data.responseJSON);
				        	}
				        }
					});
				}
			});
		};
		if($('#reportType').next()[0].comboObj.getSelectedValue()==3){
			$('td[aria-describedby=detailsList_action] a.btn-detail').show();
		}else{
			$('td[aria-describedby=detailsList_action] a.btn-detail').hide();
		}
		var rows=$('#detailsList').jqGrid("getRowData");
		var amount=0;
		var bookTotal=0,bookPrice=0,deliveredTotal=0,undeliveredTotal=0,unDeliveredAmount=0,backTotal=0;
		var backAmount=0;
		var deliveryAmount=0;
		var hasIncome=0;
		var notIncome=0;
		var goodsFee=0;
		var saleExp=0;
		var goodCost=0;
		var backCost=0;
		var profit=0;
		var saleAmount=0;
		var backQuantity=0;
		var saleQuantity=0;
		for(var i=0;i<rows.length;i++){
			if(rows[i].bookTotal){
				bookTotal=parseFloat(rows[i].bookTotal)+bookTotal;
			}
			if(rows[i].bookPrice){
				bookPrice=parseFloat(rows[i].bookPrice)+bookPrice;
			}
			if(rows[i].deliveredTotal){
				deliveredTotal=parseFloat(rows[i].deliveredTotal)+deliveredTotal;
			}
			if(rows[i].undeliveredTotal){
				undeliveredTotal=parseFloat(rows[i].undeliveredTotal)+undeliveredTotal;
			}
			if(rows[i].unDeliveredAmount){
				unDeliveredAmount=parseFloat(rows[i].unDeliveredAmount)+unDeliveredAmount;
			}
			if(rows[i].backTotal){
				backTotal=parseFloat(rows[i].backTotal)+backTotal;
			}
			if(rows[i].amount){
				amount=parseFloat(rows[i].amount)+amount;
			}
			if(rows[i].backAmount){
				backAmount=parseFloat(rows[i].backAmount)+backAmount;
			}
			if(rows[i].deliveryAmount){
				deliveryAmount=parseFloat(rows[i].deliveryAmount)+deliveryAmount;
			}
			if(rows[i].hasIncome){
				hasIncome=parseFloat(rows[i].hasIncome)+hasIncome;
			}
			if(rows[i].notIncome){
				notIncome=parseFloat(rows[i].notIncome)+notIncome;
			}
			if(rows[i].goodsFee){
				goodsFee=parseFloat(rows[i].goodsFee)+goodsFee;
			}
			if(rows[i].saleExp){
				saleExp=parseFloat(rows[i].saleExp)+saleExp;
			}
			if(rows[i].goodCost){
				goodCost=parseFloat(rows[i].goodCost)+goodCost;
			}
			if(rows[i].backCost){
				backCost=parseFloat(rows[i].backCost)+backCost;
			}
			if(rows[i].profit){
				profit=parseFloat(rows[i].profit)+profit;
			}
			if(rows[i].saleAmount){
				saleAmount=parseFloat(rows[i].saleAmount)+saleAmount;
			}
			if(rows[i].backQuantity){
				backQuantity=parseFloat(rows[i].backQuantity)+backQuantity;
			}
			if(rows[i].saleQuantity){
				saleQuantity=parseFloat(rows[i].saleQuantity)+saleQuantity;
			}
		}
		$('#detailsList').footerData('set',{
			saleCode:"合计:",
    		amount:amount.toFixed(2),
    		backTotal:backTotal.toFixed(2),
    		unDeliveredAmount:unDeliveredAmount.toFixed(2),
    		undeliveredTotal:undeliveredTotal.toFixed(2),
    		deliveredTotal:deliveredTotal.toFixed(2),
    		deliveryAmount:deliveryAmount.toFixed(2),
    		bookPrice:bookPrice.toFixed(2),
    		bookTotal:bookTotal.toFixed(2),
    		backAmount:backAmount.toFixed(2),
    		hasIncome:hasIncome.toFixed(2),
    		notIncome:notIncome.toFixed(2),
    		goodsFee:goodsFee.toFixed(2),
    		saleExp:saleExp.toFixed(2),
    		goodCost:goodCost.toFixed(2),
    		backCost:backCost.toFixed(2),
    		profit:profit.toFixed(2),
    		saleAmount:saleAmount.toFixed(2),
    		backQuantity:backQuantity.toFixed(2),
    		saleQuantity:saleQuantity.toFixed(2)
		});
		$('.dataBox .frozen-sdiv td[aria-describedby=detailsList_saleCode]').text("合计：");
	}
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}).jqGrid('setFrozenColumns');

function viewDetails(fid){
	var top = $(window).height()/2-200;
	var left = $(window).width()/2-400;
	win = $("#pop-win").fool('window',{modal:true,'title':"查看销售出库单明细",top:top,left:left,width:800,height:400,href:getRootPath()+'/rate/xsckad?id='+fid});
}
//图表属性
/* var option = {
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
chart.setOption(option); */

function dateFormatOut(value){
	return getDateStr(value);
}

function rateOut(value){
	if(value){
		return value+"%";
	}else{
		return "0%";
	}
}
</script>
</html>
