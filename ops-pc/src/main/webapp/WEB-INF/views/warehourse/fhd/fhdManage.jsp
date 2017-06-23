<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>发货单信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
.open {
	background: url(${ctx}/resources/images/open.png) no-repeat;
	padding-left: 0px;
	width: 40px;
	background-position: 0px -1px;
}

#goodsChooser {
	display: none;
	text-align: center;
}

#btnBox {
	text-align: center;
}

.form p.hideOut,.form1 p.hideOut {
	display: none;
}

.form,.form1 {
	padding: 5px 0px;
}

.form1 p {
	margin: 5px 0px
}

.form p font,.form1 p font {
	width: 115px;
}

#userChooser,#customerChooser,#goodsChooser,#goodsSpecChooser {
	display: none;
}

#userSearcher,#cusSearcher,#goodsSearcher,#goodsSpecSearcher {
	text-align: left;
	margin: 10px
}
#search-form{
  display: inline-block;
  width:98%;
}
#search-form p{margin-top: 6px;}
#search-form .btn-s{
  margin:5px 5px 0px 0px;
  vertical-align: middle;
}
.dd{ margin-left: 7px;}

#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 25px;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:29px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:5px;}
.input_div{ display: none; background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:93px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px; width:235px;}
.button_clear{ border-top: 1px solid #D5DBEA;margin-top: 10px; text-align: right;}
.roed{
   -moz-transform:scaleY(-1);
   -webkit-transform:scaleY(-1);
   -o-transform:scaleY(-1);
   transform:scaleY(-1);
   filter:FlipV();
}
#bill p.hideOut{
  display: none ;
}
input[disabled]{
	background-color: rgb(209, 217, 224);
	color:#000!important;
}
</style>
</head>
<body>	
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>发货单</h1>
                </div>             
             </div>
	<div id="addBox"></div>
	<div style="margin: 10px 0px 10px 0px;">		
		<div>
		   <a href="javascript:;" id="addNew" class="btn-ora-add" >新增</a>
			<input id="search-code" /><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
			<div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
		</div>	
		<div class="input_div">
		<form action="" id="search-form">
				<p>开始日期：<input id="search-startDay"  name="startDay"/></p>
				<p>结束日期：<input id="search-endDay" name="endDay" class="easyui-datebox" data-options="width:160,height:32" /></p>
				<p>运输批号：<input id="search—transportNo" name="transportNo" class="textBox"/>
				<p>运输公司：<input name="supplierId" id='search-supplierId' class="textBox"/></p>
			    <p>发货地：<input name="deliveryPlaceId" id="search-deliveryPlaceId"/></p>
			    <p>船号：<input id="search—carNo" name="carNo" class="textBox"/>
			    <p>箱号：<input id="search—containerNumber" name="containerNumber" class="textBox"/>
			    <p>封号：<input id="search—sealingNumber" name="sealingNumber" class="textBox"/> 
				
				<p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	            <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
	            
			  <div class="button_clear">
				<a href="javascript:;" id="search-btn" class="btn-blue btn-s" onclick="refreshData()" style="vertical-align:middle;">查询</a>
				<a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
				<a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
			</div>			
		</form>
		</div>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<%-- <script type="text/javascript" src="${ctx}/resources/js/goodsBills.js?v=${js_v}"></script> --%>
<script type="text/javascript">
initManage("fhd","发货单");//传code
enterSearch("Inquiry");//回车搜索
$('#billList').jqGrid({
	datatype:function(postdata){//${ctx}/damaged/dcd/list
	$.ajax({
		url : '${ctx}/warehouse/fhd/list',
		data:postdata,
		dataType:"json",
		complete: function(data,stat){
		if(stat=="success") {
		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		$("#billList")[0].addJSONData(data.responseJSON);
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
	colModel : [
		{name:'fid',label:'fid',align:"center",hidden:true,width:100},
		{name:'code',label:'单号',align:"center",	width:100,sortable:true,formatter:codeLinkNew},
		{name:'transportNo',label:'运输批号',align:"center",width:100},
        {name:'billDate',label:'单据日期',align:"center",width:100,formatter:dateFormatAction},
        {name:'supplierName',label:'运输公司',align:"center",width:100},
		{name:'deliveryPlaceName',label:'发货地',align:"center",width:100},
		{name:'receiptPlaceName',label:'收货地',align:"center",width:100,sortable:true},
		{name:'transportTypeName',label:'运输方式',align:"center",width:100},
		{name:'shipmentTypeName',label:'装运方式',align:"center",width:100},
		{name:'carNo',label:'车船号',align:"center",width:100},
		{name:'recordStatus',label:'状态',align:"center",width:40,sortable:true,formatter:recordStatusAction},
		{name:'action',label : '操作',align:"center",width:100,formatter:actionFormatNew}],
		gridComplete:function(){
			warehouseAll();
		}}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
		
		$("#clear-btn").click(function() {
			cleanBoxInput($("#search-form"));
		});		 
		 $("#search-startDay").fool('datebox',{
			prompt : '开始日期',
			width : 165,
			height : 30,
			inputDate:true,
			editable : true
		});

		$("#search-endDay").fool('datebox',{
			prompt : '结束日期',
			width : 165,
			height : 30,
			inputDate:true,
			editable : true
		}); 
		$("#search—transportNo").textbox({
			prompt:'批号',
			width:160,
			height:32
		});
		$('#search—carNo').textbox({
			prompt:'船号',
			width:160,
			height:32
		});
		$('#search—containerNumber').textbox({
			prompt:'箱号',
			width:160,
			height:32
		});
		$('#search-supplierId').fool('dhxComboGrid',{
			required:true,
			novalidate:true,
			width:160,
			height:32,
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
		$('#search—sealingNumber').textbox({
			prompt:'封号',
			width:160,
			height:32
		});
		var freightAddressData="";
		$.ajax({
		 	url:getRootPath()+"/api/freightAddress/findAddressTree?enable=1",
			async:false,
	        data:{},
	        success:function(data){
	        	freightAddressData=data;
	        }
		});
		$("#search-deliveryPlaceId").combotree({
			    width:160,
			    height:32,
				required:true, 
				novalidate:true,
				data:freightAddressData,
				onSelect:function(record){
				 	
				}
		});
		//设置鼠标放进去自动出来下拉列表
		$("#search-form").find("input[_class]").each(function(i,n){($(this));});
		if("${param.billId}"){
			viewRowNew("${param.billId}");
		}
	</script>
</body>
</html>
