<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>运输费用报价分析</title>
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
      <h1>运输费用报价分析</h1>
    </div>             
  </div>
  <div class="filterBox">
    <div id="tb">
      <div id="reportButton">
        <a href="javascript:;" id="search" class="btn-blue4 btn-s" onclick="query()">查询</a>
        <a href="javascript:;" id="clear" class="btn-blue4 btn-s" onclick="clearer()">清空</a>
        <a href="javascript:;" id="export" class="btn-blue4 btn-s" onclick="exporter()">导出</a>
       <!--  <a href="javascript:;" id="export" class="btn-blue4 btn-s" >打印</a> -->
      </div>
      <div id="filters">
        <form id="filterGrid" class="form1" action="${ctx}/transportPrice/export" method="post">
          <p><font>开始日期：</font><input id="strDate" name='startDay'/></p>
          <p><font>结束日期：</font><input id="endDate" name='endDay'/></p>
          <p><font>承运单位：</font><input id="search-carrName" name='supplierName'/></p>
          <p><font>发货地：</font><input id="deliveryPlaceFid"/><input id="deliveryPlaceId" name="deliveryPlaceName" type="hidden" /></p>
          <p><font>收货地：</font><input id="receiptPlaceFid"/><input id="receiptPlaceId" name="receiptPlaceName" type="hidden" /></p>
          <p><font>运输方式：</font><input id="transportTypeFid" name='transportTypeName'/></p>
          <p><font>装运方式：</font><input id="shipmentTypeFid" name='shipmentTypeName'/></p>
          <p><font>报价单位：</font><input id="search-offerName" name='transportUnitName'/></p>
        </form>
      </div>
    </div>
  </div>
  <div class="resultBox">
    <table id="resultGrid"></table>
    <div id="pager"></div>
  </div>
  <div id="chartWin"></div>
  <div id="detalWin"></div>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
var time='';
$("#strDate").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择开始日期",
	required:true,
	value:getCurrDate()
});
$("#endDate").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择结束日期",
	required:true,
	value:getCurrDate()
});
time=$('#strDate').datebox('getValue');
//承运单位
var ssupData='';
$.ajax({
	url:"${ctx}/basedata/query?num="+Math.random(),
	async:false,
	data:{param:"Supplier"},
	success:function(data){
		ssupData = formatData(data.Supplier,"fid");
    }
	});
//承运单位
$('#search-carrName').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	width:167,
	height:30,
	data:ssupData,
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
    toolsBar:{
        refresh:true
    },onChange:function(value,text){
        $('#search-carrName').val(text);
    }
});
//报价单位
$('#search-offerName').fool('dhxComboGrid',{
    required:true,
    novalidate:true,
    width:167,
    height:30,
    data:ssupData,
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
    toolsBar:{
        refresh:true
    },onChange:function(value,text){
        $('#search-offerName').val(text);
    }
});
//发货地址
var deliveryValue='';	
$.ajax({
	url:"${ctx}/freightAddress/findAddressTree?enable=1&num="+Math.random(),
	async:false,		
	success:function(data){	
		deliveryValue=data	
    }
	});
deliveryName=$("#deliveryPlaceFid").combotree({
	width:167,
	height:30,
	required:true, 
	novalidate:true,
	data:deliveryValue,
	onSelect:function(record){
		$('#deliveryPlaceId').val(record.name);
	  }
    });
receiptName=$("#receiptPlaceFid").combotree({
	width:167,
	height:30,
	required:true, 
	novalidate:true,
	data:deliveryValue,
	onSelect:function(record){	
		$('#receiptPlaceId').val(record.name);
	  }
    });
    
//运输方式
var transportValue='';	
$.ajax({
	url:"${ctx}/basedata/transitType?num="+Math.random(),
	async:false,		
	success:function(data){	
		transportValue=formatTree(data[0].children,'id','text');	
    }
	});
$("#transportTypeFid").fool("dhxCombo",{
	  width:167,
	  height:30,
	  data:transportValue,
	  clearOpt:false,
	  editable:false,
	  focusShow:true,
	  setTemplate:{
		input:"#name#",
		option:"#name#"
	  },
    toolsBar:{
    	name:"运输方式",
        refresh:true
    }
});	
    
//装运方式
var shipmentValue='';	
$.ajax({
	url:"${ctx}/basedata/shipmentType?num="+Math.random(),
	async:false,		
	success:function(data){		  	
		shipmentValue=formatTree(data[0].children,'id','text');	
    }
	});
var shipmentName= $("#shipmentTypeFid").fool("dhxCombo",{
	  width:167,
	  height:30,
	  data:shipmentValue,	
	  clearOpt:false,
	  editable:false,
	  focusShow:true,
	  setTemplate:{
			input:"#name#",
			option:"#name#"
		  },
    toolsBar:{
    	name:"装运方式",
        refresh:true
    }
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
			url: '${ctx}/transportPrice/list',
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
    gridComplete: completeMethod,
    footerrow:true,
	autowidth:true,
	height:400, 
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
    shrinkToFit:false,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[ 
	          {name : 'fid',label :'fid',align:'center',hidden:true},
              {name : 'code',label :'单号',align:'center',width:138},
              {name : 'billDate',label :'单据日期',align:'center',width:78},
              {name : 'supplierName',label : '承运单位',align:'center',width:78},
              {name : 'priceUnitName',label : '报价单位',align:'center',width:78},
              {name : 'deliveryPlaceName',label : '发货地',align:'center',width:78},
              {name : 'deliveryPlaceId',label :'deliveryPlaceId',hidden:true},
              {name : 'receiptPlaceName',label : '收货地',align:'center',width:78},
              {name : 'receiptPlaceId',label :'receiptPlaceId',hidden:true},
              {name : 'transportTypeName',label : '运输方式',align:'center',width:70},
              {name : 'transportTypeId',label :'transportTypeId',hidden:true},
              {name : 'shipmentTypeName',label : '装运方式',align:'center',width:56},
              {name : 'shipmentTypeId',label :'shipmentTypeId',hidden:true},
              {name : 'amount',label : '金额',align:'center',width:64},
              {name : 'expectedDays',label : '预计时间',align:'center',width:54},
              {name : 'effectiveDate',label : '有效日期',align:'center',width:78},
              {name : 'executeSign',label : '可执行标志',align:'center',width:70,formatter:EventType},
              {name : 'creatorName',label : '制单人',align:'center',width:44},
              {name : 'describe',label : '备注',width:84,align:'center',},
              {name:"action",label:"操作",align:'center',width:70,formatter:function(cellvalue, options, rowObject){
              	var a='',t='';
			    	a = "<a href='javascript:;' onClick='attach(\""+options.rowId+"\",\""+rowObject.fid+"\")' class='btn-detail' title='详细'></a>";
			    	t = "<a href='javascript:;' onClick='priceChart(\""+options.rowId+"\")' class='btn-detail' title='价格趋势'></a>";
				    return a+"  "+t;
              }},
              ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function completeMethod(){
    var sumAmount = $("#resultGrid").getCol("amount",false,'sum');
    $("#resultGrid").footerData('set',{
        'billDate':'合计',
        amount:sumAmount,
    });
}

var events = [
            {id:'1',name:'可执行'},
  	   		{id:'2',name:'难执行'},
  	   		{id:'3',name:'不可执行'}
             ];
function EventType(value,options,row){	
	for(var i=0; i<events.length; i++){
		if (events[i].id == value) return events[i].name;		
		if(value==''||value==null||value=="undefined "){
			return '';
		}
	}	
	return value;
}             

function priceChart(id){
	var rowData=$("#resultGrid").getRowData(id);
	var deliveryPlaceName=encodeURI(rowData.deliveryPlaceName);
	var receiptPlaceName=encodeURI(rowData.receiptPlaceName);
	var transportTypeName=encodeURI(rowData.transportTypeName);
	var shipmentTypeName=encodeURI(rowData.shipmentTypeName);
	var startDay=$("#strDate").datebox("getValue");
	var endDay=$("#endDate").datebox("getValue");
	$("#chartWin").window("open");
	$("#chartWin").window("refresh","${ctx}/transportPrice/chart?shipmentTypeName="+shipmentTypeName+"&transportTypeName="+transportTypeName+"&receiptPlaceName="+receiptPlaceName+"&deliveryPlaceName="+deliveryPlaceName+"&startDay="+startDay+"&endDay="+endDay);
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

$("#detalWin").window({
	title:"详细",
	width:900,
	height:500,
	closed:true,
	modal:true
});

function attach(id,fid){	
	$("#detalWin").window("open");
	$("#detalWin").window("refresh","${ctx}/transportPrice/detal?fid="+fid);
}

function exporter(){//导出
	$("#filterGrid").submit(); 
};

function query(){//查询
	var options=$("#filterGrid").serializeJson();
	options.transportUnitName=$('#search-offerName').val();//报价单位
    options.supplierName=$('#search-carrName').val();//承运单位
	$('#resultGrid').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");	
}
</script>
</body>  
</html>
