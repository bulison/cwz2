<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="shd" scope="page"></c:set>
<c:set var="billCodeName" value="收货单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
#addNew{
  margin:0 0 10px 0;
  vertical-align: top;
}
.Inquiry{
  margin:0 0 10px 5px 
}
#Inquiry{margin-left: 5px;}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; margin-right:19px; position:relative; border: 1px solid #ccc; background: #fff;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:23px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:4px;}
.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:93px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 5px 20px 0 10px; width:235px;}
.button_clear{ border-top: 1px solid #D5DBEA;padding-top:8px; text-align: right;}
#search-form .dhxDiv{margin:0 !important;}
#bill p.hideOut{
  display: none ;
}
input[disabled]{background-color: rgb(209, 217, 224);color:#000!important;}
</style>
</head>
<body>
	<div class="titleCustom">
	  <div class="squareIcon">
	    <span class='Icon'></span>
	    <div class="trian"></div>
	    <h1>收货单</h1>
	  </div>             
    </div>
    <div>	    
	    <a href="javascript:;" id="addNew" class="btn-ora-add">新增</a>
	    <input name="codeOrVoucherCode" id="search-code"/><a class="Inquiry btn-blue btn-s">查询</a>
	    <div id="grabble"><input type="text" id="bolting" value="请选择筛选条件" readonly="readonly" /><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	    <div class="input_div">
	      <form action="" id="search-form">
	        <p>开始日期:&nbsp;<input name="startDay" id="startDay"/></p>
			<p>结束日期:&nbsp;<input name="endDay" id="endDay"/></p>
			<p>发货单号:&nbsp;<input name="relationCode" id="search-deliveryBillCode"/></p>
			<p>运输批号:&nbsp;<input name="transportNo" id="search-transportNo"/></p>
			<p>运输公司:&nbsp;<input name="supplierId" id="search-supplierId"/></p>
			<p>发货地:&nbsp;<input name="deliveryPlaceId" id="search-deliveryPlaceId"/></p>
			<p>车船号:&nbsp;<input name="carNo" id="search-carNo"/></p>
			<p>箱号:&nbsp;<input name="containerNumber" id="search-containerNumber"/></p>
			<p>封号:&nbsp;<input name="sealingNumber" id="search-sealingNumber"/></p>
			<p style="margin: 5px 20px 5px 0px;">状态:<input type=checkbox value='0' name="recordStatus">未审核<input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
			<div class="button_clear">
			  <a href="javascript:;" class="btn-blue btn-s" id="search-form-btn" onclick="refreshData()" style="vertical-align:middle;">查询</a>
			  <a href="javascript:;" class="btn-blue btn-s" onclick="cleanBoxInput('#search-form')" style="vertical-align:middle;">清空</a>
			  <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
			</div>
		  </form>
		</div>
	</div>
	<table id="dataTable"></table>
	<div id="pager"></div>
	<div id="addBox"></div>
    <div id="my-window"></div>
</body>
</html>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
enterSearch("Inquiry");//回车搜索
initManage('${billCode}','${billCodeName}');
$("#search-code").textbox({
	prompt:'单号',
	width:160,
	height:28
});
$("#search-deliveryBillCode").textbox({
	prompt:'发货单号',
	width:165,
	height:30
});
$("#search-transportNo").textbox({
	prompt:'运输批号',
	width:165,
	height:30
});
$('#search-supplierId').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	width:167,
	height:30,
	data:getComboData(getRootPath()+'/supplier/vagueSearch?searchSize=8&q='),
	prompt:"运输公司",
	toolsBar:{
		refresh:true
	},
	focusShow:true,
    onlySelect:true,
	filterUrl:getRootPath()+'/supplier/list?showDisable=0',
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
  	},
});
/* $("#search-deliveryPlaceId").fool("dhxCombo",{
	prompt:'发货地',
	width:167,
	height:30,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	focusShow:true,
	editable:false,
	data:getComboData("${ctx}/basedata/findSubAuxiliaryAttrTree?code=022","tree"),
}); */
$("#search-deliveryPlaceId").combotree({
    width:165,
    height:32,
	required:true,
	novalidate:true,
	url:getRootPath()+"/api/freightAddress/findAddressTree",
	method:"get",
	onSelect:function(record){

	}
});
$("#search-carNo").textbox({
	prompt:'车船号',
	width:165,
	height:30
});
$("#search-containerNumber").textbox({
	prompt:'箱号',
	width:165,
	height:30
});
$("#search-sealingNumber").textbox({
	prompt:'封号',
	width:165,
	height:28
});
$('#dataTable').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/warehouse/shd/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#dataTable")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	forceFit:true,
	pager:'#pager',
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	autowidth:true,//自动填满宽度
	height:$(window).outerHeight()-200+"px",
	colModel:[
	  		{name:'fid',label:'fid',align:"center",hidden:true,width:100},
	  		{name:'totalAmount',label:'totalAmount',align:"center",hidden:true,width:100},
	  		{name:'deductionAmount',label:'deductionAmount',align:"center",hidden:true,width:100},
	  		{name:'code',label:'单号',align:"center",sortable:true,width:150,formatter:codeLinkNew},
        	{name:'transportNo',align:"center",label:'运输批号',sortable:true,width:100},
			{name:'billDate',align:"center",label:'单据日期',width:100,sortable:true,formatter:dateFormatAction},
	  		{name:'supplierName',align:"center",label:'运输公司',sortable:true,width:100},
	  		{name:'deliveryPlaceName',align:"center",label:'发货地',sortable:true,width:100},
	  		{name:'receiptPlaceName',align:"center",label:'收货地',sortable:true,width:100},
	  		{name:'transportTypeName',align:"center",label:'运输方式',sortable:true,width:100},
	  		{name:'shipmentTypeName',align:"center",label:'装运方式',sortable:true,width:100},
	  		{name:'carNo',align:"center",label:'车船号',sortable:true,width:100},
	  		{name:'recordStatus',align:"center",label:'状态',width:40,sortable:true,formatter:recordStatusAction},
	  		{name:'action',align:"center",label:'操作',formatter:actionFormatNew}
	      ],
	      gridComplete:function(){
	    		warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
if("${param.billId}"){
	viewRowNew("${param.billId}");
}
</script>