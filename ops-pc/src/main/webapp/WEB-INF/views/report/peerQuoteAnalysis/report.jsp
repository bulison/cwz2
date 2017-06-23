<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>同行报价分析界面</title>
<style>
#reportButton{
  background:#F0F0F0;
  display:inline-block;
  margin:0 0 10px 0
}
#paging{
  vertical-align: middle;
}
#filters{
  background: #fff;
  border: 1px #ccc solid;
}
#filterGrid{
  background: url('/ops-pc/resources/images/notebook.jpg') repeat-y;
  background-position-x: 10px; 
}
</style>
</head>
<body>
  <div class="titleCustom">
    <div class="squareIcon">
      <span class='Icon'></span>
      <div class="trian"></div>
      <h1>同行报价分析</h1>
    </div>             
  </div>
  <div class="filterBox">
    <div id="reportButton">
      <span id="paging"><input id="flag" type="checkbox" checked="checked"/>分页</span>
      <a href="javascript:;" id="search" class="btn-blue4 btn-s" onclick="query()">查询</a>
      <a href="javascript:;" id="clear" class="btn-blue4 btn-s" onclick="clearer()">清空</a>
      <a href="javascript:;" id="export" class="btn-blue4 btn-s" onclick="exporter()">导出</a>
    </div>
    <div id="filters">
      <form id="filterGrid" class="form1" action="${ctx}/peerQuoteReport/export" method="post">
        <p><font>开始日期：</font><input id="strDate" name="startDay"/></p>
        <p><font>结束日期：</font><input id="endDate" name="endDay"/></p>
        <p><font>供应商：</font><input id="supplier" name="supplier"/></p>
        <p><font>客户：</font><input id="customer" name="customerId"/></p>
        <p><font>货品编号：</font><input id="goodsCode" name="goodsId"/></p>
        <p><font>货品名称：</font><input id="goodsName" name="goodsName"/></p>
      </form>
    </div>
  </div>
  <div class="resultBox">
    <table id="resultGrid"></table>
    <div id="pager"></div>
  </div>
  <div id="chartWin"></div>
  <div id="picWin"></div>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
$("#strDate").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择日期",
	required:true,
	value:getCurrDate()  
});
$("#endDate").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择日期",
	required:true,
	value:getCurrDate()  
});
$("#supplier").textbox({
	prompt:'供应商',
	width:167,
	height:30,
});
$("#customer").fool("dhxComboGrid",{
	prompt:"选择客户",
	height:30,
	width:169,
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
	},
    toolsBar:{
        refresh:true
    },
    filterUrl:getRootPath()+'/customer/vagueSearch?searchSize=8',
	data:getComboData(getRootPath()+'/customer/vagueSearch?searchSize=8&q='),
	searchKey:"searchKey", 
	focusShow:true,
});
$("#goodsCode").fool("dhxComboGrid",{
	prompt:"选择货品",
	height:30,
	width:169,
	setTemplate:{
		input:"#code#",
		columns:[
		         {option:'#name#',header:'名称',width:100},
		         {option:'#code#',header:'编码',width:100},
		         {option:'#barCode#',header:'条码',width:100},
				],
	},
    toolsBar:{
        refresh:true
    },
    filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=6',
	data:getComboData(getRootPath()+'/goods/vagueSearch?searchSize=6'),
	searchKey:"searchKey", 
	focusShow:true,
});
$("#goodsName").textbox({
	prompt:'货品名称',
	width:167,
	height:30,
});
$("#chartWin").window({
	title:"价格趋势",
	width:900,
	height:500,
	closed:true,
	modal:true
});
$("#resultGrid").jqGrid({
	datatype:function(postdata){
		postdata.startDay=$("#strDate").datebox("getValue");
		postdata.endDay=$("#endDate").datebox("getValue");
		$.ajax({
			url: '${ctx}/api/peerQuoteReport/queryReport',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#resultGrid")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,
	height:390, 
	pager:"#pager",
	rowNum:30,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[ 
		        {name : 'goodsId',label :'goodsId',hidden:true},
		        {name : 'goodSpecId',label :'goodSpecId',hidden:true},
		        {name : 'receiptPlace',label :'receiptPlace',hidden:true},
                {name : 'code',label :'单号',align:'center',width:"200px"}, 
                {name : 'billDate',label :'单据日期',align:'center',width:"100px"}, 
                {name : 'supplier',label : '供应商',align:'center',width:"100px"}, 
                {name : 'customerName',label : '客户',align:'center',width:"100px"}, 
                {name : 'goodsCode',label : '货品编号',align:'center',width:"100px"}, 
                {name : 'goodsName',label : '货品名称',align:'center',width:"100px"}, 
                {name : 'specName',label : '货品属性',align:'center',width:"100px"}, 
                {name : 'unitName',label : '货品单位',align:'center',width:"100px"}, 
                {name : 'deliveryPrice',label : '交货价格',align:'center',width:"100px"}, 
                {name : 'receiptPlaceName',label : '交货地',align:'center',width:"100px"},
                {name : 'creatorName',label : '制单人',align:'center',width:"100px"},
                {name : 'describe',label : '备注',align:'center',width:"100px"},
                {name:"action",label:"操作",align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	var a='',t='';
			    	a = "<a href='javascript:;' onClick='showPic(\""+options.rowId+"\")' class='btn-detail' title='详细'></a>";
			    	t = "<a href='javascript:;' onClick='priceChart(\""+options.rowId+"\")' class='btn-back' title='价格趋势'></a>";
				    return a+" "+t;
                }},
              ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function query(){
	var startDay=$("#strDate").datebox("getValue");
	var endDay=$("#endDate").datebox("getValue");
	var supplier=$("#supplier").textbox("getValue");
	var customerId=($("#customer").next())[0].comboObj.getSelectedValue();
	var goodsId=($("#goodsCode").next())[0].comboObj.getSelectedValue();
	var goodsName=$("#goodsName").textbox("getValue");
	$("#filterGrid").form("enableValidation");
	if($("#filterGrid").form("validate")){
		$('#resultGrid').jqGrid('setGridParam',{url:"${ctx}/api/peerQuoteReport/queryReport",postData:{startDay:startDay,endDay:endDay,supplier:supplier,customerId:customerId,goodsId:goodsId,goodsName:goodsName}}).trigger("reloadGrid");
	};
}
function clearer(){
	$("#filterGrid").form("reset");
	var inputs=$("#filterGrid").find(".dhxDiv");
	for(var i=0;i<inputs.length;i++){ 
		inputs[i].comboObj.setComboText("");
	}
}
function exporter(){
	$("#filterGrid").submit(); 
};
function priceChart(id){
	var rowData=$("#resultGrid").getRowData(id);
	var goodsId=rowData.goodsId;
	var goodSpecId=rowData.goodSpecId;
	var receiptPlace=rowData.receiptPlace;
	var startDay=$("#strDate").datebox("getValue");
	var endDay=$("#endDate").datebox("getValue");
	$("#chartWin").window("open");
	$("#chartWin").window("refresh","${ctx}/peerQuoteReport/chart?goodsId="+goodsId+"&goodSpecId="+goodSpecId+"&receiptPlace="+receiptPlace+"&startDay="+startDay+"&endDay="+endDay);
}

$("#picWin").window({
	title:"",
	top:50,
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true,
	width:700,
	height:400
}); 
function showPic(busId){   
	var url="${ctx}/api/purchasePrice/queryAttach";
	$("#picWin").window("open");
	$("#picWin").window("refresh","${ctx}/purchasePriceReport/showPic?busId="+busId+"&url="+url);
}
</script>
</body>  
</html>
