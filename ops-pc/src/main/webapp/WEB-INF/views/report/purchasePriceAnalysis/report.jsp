<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>货品采购价格汇总</title>
<style>
#reportButton{
  background:#F0F0F0;
  display:inline-block;
  margin-bottom: 10px;
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
      <h1>货品采购价格汇总</h1>
    </div>             
  </div>
  <div class="filterBox">
    <div id="tb">
      <span id="paging"><input id="flag" type="checkbox" checked="checked"/>分页</span>
      <div id="reportButton">
        <a href="javascript:;" id="search" class="btn-blue4 btn-s" onclick="query()">查询</a>
        <a href="javascript:;" id="clear" class="btn-blue4 btn-s" onclick="clearer()">清空</a>
        <a href="javascript:;" id="export" class="btn-blue4 btn-s" onclick="exporter()">导出</a>
       <!--  <a href="javascript:;" id="export" class="btn-blue4 btn-s" >打印</a> -->
      </div>
      <div id="filters">
        <form id="filterGrid" class="form1" action="${ctx}/purchasePriceReport/export" method="post">
          <p><font>开始日期：</font><input id="strDate" name='startDay'/></p>
          <p><font>结束日期：</font><input id="endDate" name='endDay'/></p>
          <p><font>供应商：</font><input id="supplier" name='supplierId'/></p>
         <!--  <input id='deliveryPlace' name='deliveryPlace' type="hidden"> -->
          <p><font>发货地：</font><input id="deliveryPlaceFid" name='deliveryPlace'/></p>
          <p><font>货品编号：</font><input id="goodsCode" name='goodsId'/></p>
          <p><font>货品名称：</font><input id="goodsName" name='goodsName'/></p>
        </form>
      </div>
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
var time='';
$("#strDate").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择开始日期",
	value:getCurrDate()
});
$("#endDate").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择结束日期",
	value:getCurrDate()
});
time=$('#strDate').datebox('getValue');
$("#supplier").fool("dhxComboGrid",{
	prompt:"选择供应商",
	height:30,
	width:167,
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
    filterUrl:getRootPath()+'/supplier/vagueSearch?searchSize=8',
	data:getComboData(getRootPath()+'/supplier/vagueSearch?searchSize=8&q='),
	searchKey:"searchKey", 
	focusShow:true,
});	
$("#goodsCode").fool("dhxComboGrid",{
	prompt:"选择货品",
	height:30,
	width:167,
	setTemplate:{
		input:"#code#",
		columns:[
		         {option:'#name#',header:'名称',width:100},
		         {option:'#code#',header:'编号',width:100},
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
$('#deliveryPlaceFid').combotree({
	width:167,
	height:30,
	required:true, 
	novalidate:true,
	url:getRootPath()+"/api/freightAddress/findAddressTree?enable=1",
	method:"get",
	onSelect:function(record){
		$('#deliveryPlace').val(record.fid);
	}
})
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
			url: '${ctx}/api/purchasePrice/queryReport',
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
	height:400, 
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[ 
                {name : 'id',label :'id',align:'center',width:"200px",hidden:true}, 
                {name : 'goodsId',label :'goodsId',hidden:true},
                {name : 'goodSpecId',label :'goodSpecId',hidden:true},
                {name : 'billDate',label :'单据日期',align:'center',width:"100px"}, 
                {name : 'supplierName',label : '供应商',align:'center'}, 
                {name : 'goodsCode',label : '货品编号',align:'center'}, 
                {name : 'goodsName',label : '货品名称',align:'center'}, 
                {name : 'specName',label : '货品属性',align:'center'}, 
                {name : 'unitName',label : '货品单位',align:'center'}, 
                {name : 'factoryPrice',label : '厂价',align:'center'}, 
                {name : 'taxPoint',label : '税点',align:'center'},
                {name : 'afterTaxPrice',label : '税后价',align:'center'},
                {name : 'pickUpCharge',label : '提货费',align:'center'},
                {name : 'deliveryPrice',label : '交货总价',align:'center'},
                {name : 'deliveryPlaceName',label : '发货地',align:'center'},
                {name : 'effectiveDate',label : '有效日期',align:'center'},
                {name : 'creatorName',label : '制单人',align:'center'},
                {name : 'describe',label : '备注',align:'center',},
                {name:"action",label:"操作",align:'center',formatter:function(cellvalue, options, rowObject){
                	var a='',t='',s='';
			    	/* a = "<a href='javascript:;' onClick='attach(\""+options.rowId+"\",\""+rowObject.fid+"\")' class='btn-detail' title='详细'></a>"; */
			    	t = "<a href='javascript:;' onClick='priceChart(\""+options.rowId+"\")' class='btn-detail' title='价格趋势'></a>";
			    	s = "<a href='javascript:;' onClick='showPic(\""+options.rowId+"\")' class='btn-detail' title='详细'></a>";
				    return /* a+"  "+ */t+" "+s;
                }}, 
              ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

function priceChart(id){
	var rowData=$("#resultGrid").getRowData(id);
	var goodsId=rowData.goodsId;
	var goodSpecId=rowData.goodSpecId;
	var startDay=$("#strDate").datebox("getValue");
	var endDay=$("#endDate").datebox("getValue");
	$("#chartWin").window("open");
	$("#chartWin").window("refresh","${ctx}/purchasePriceReport/chart?goodsId="+goodsId+"&goodSpecId="+goodSpecId+"&startDay="+startDay+"&endDay="+endDay);
}

function clearer(){//清除
	$('#filterGrid').form('clear');
	var inputs=$("#filterGrid").find(".dhxDiv");
	 for(var i=0;i<inputs.length;i++){
		 inputs[i].comboObj.setComboText("");
	 } 
	 $('#strDate').datebox('setValue',time);
	 $('#endDate').datebox('setValue',time);
}
function attach(){
	
}

function exporter(){//导出
	$("#filterGrid").submit(); 
};

function query(){//查询
	var options=$("#filterGrid").serializeJson();
	$('#resultGrid').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");	
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
