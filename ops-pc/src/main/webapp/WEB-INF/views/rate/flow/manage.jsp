<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程收益率分析</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/rateModel.css" />
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/echarts.min.js?v=${js_v}"></script>
<style>
.form1 p font{
	width:100px;
}
#charts{
    width:1100px;
    height:500px;
    display: none;
}
.btn-s{
    margin-left:20px
}
</style>
</head>
<body>
	<div class="titleCustom">
         <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>流程收益率分析</h1>
         </div>             
    </div>
    
    <div class="myform">
	    <div class="searchBox">
	        <form class="form1 inSearch" id='inSearch' >
	       		<p><font>开始日期：</font><input id="startDay" name="startDay" /> </p>
	       		<p><font>结束日期：</font><input id="endDay" name="endDay" /> </p>
	       		<p><font>流程编号：</font><input id="planCode" name="planCode" /> </p>
	       		<p><font>流程名称：</font><input id="planName" name="planName" /> </p>
	       		<p><font>发起人：</font><input id="initiate" name="initiate" /> </p>
	       		<p><font>责任人：</font><input id="principal" name="principal" /> </p>
	       		<p><font>流程状态：</font><input id="status" name="status" /> </p>
	       		<a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a><a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a>
	        </form>
	    </div>
	</div>
	<div class="dataBox">
	    <table id="detailsList"></table>
	    <div id="pager"></div>
	</div>
	<div id="detailWindow"></div>
</body>
<script>
$('#mySearch').click(function(){
	var options = $('#inSearch').serializeJson();
    /* $('#detailsList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid"); */ 
	$('#detailsList').jqGrid("setGridParam",{
		datatype:function(postdata){
			postdata.startDay=options.startDay;
			postdata.endDay=options.endDay;
			postdata.planCode=options.planCode;
			postdata.planName=options.planName;
			postdata.initiate=options.initiate;
			postdata.principal=options.principal;
			postdata.status=options.status;
			if(postdata.sord=="asc"){
				postdata.sord="0";
			}else if(postdata.sord=="desc"){
				postdata.sord="1";
			}
			if(postdata.sidx=="planCode"){
				postdata.sidx="1";
			}else if(postdata.sidx=="planName"){
				postdata.sidx="2";
			}else if(postdata.sidx=="outAmount"){
				postdata.sidx="3";
			}else if(postdata.sidx=="inAmount"){
				postdata.sidx="4";
			}else if(postdata.sidx=="profit"){
				postdata.sidx="5";
			}else if(postdata.sidx=="estimatedYieldrate"){
				postdata.sidx="6";
			}else if(postdata.sidx=="effectiveYieldrate"){
				postdata.sidx="7";
			}else if(postdata.sidx=="currentYieldRate"){
				postdata.sidx="8";
			}
			$.ajax({
				url:"${ctx}/rate/planYieldRate/list",
				data:postdata,
				dataType:"json",
				complete: function(data,stat){
					if(stat=="success") {
						data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
						$("#detailsList")[0].addJSONData(data.responseJSON);
					}
				}
			});
		},
	}).trigger("reloadGrid");
});

$('#clearForm').click(function(){
	var inputs=$("#inSearch").find(".dhxDiv");
	for(var i=0;i<inputs.length;i++){
		inputs[i].comboObj.setComboText("");
	}
	$('#inSearch').form('clear');
});

//搜索条件初始化
$("#startDay").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择日期",
});
$("#endDay").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择日期",
});
$("#planCode").textbox({
	height:30,
	width:167,
	validType:'isBank',
	prompt:"流程编号",
});
$("#planName").textbox({
	height:30,
	width:167,
	validType:'isBank',
	prompt:"流程名称",
});
$("#initiate").fool("dhxComboGrid",{
	prompt:"选择人员",
	height:32,
	width:169,
	setTemplate:{
		input:"#userName#",
		columns:[
		         {option:'#userCode#',header:'账号',width:100},
		         {option:'#userName#',header:'名称',width:100},
				],
	},
	filterUrl:getRootPath()+'/userController/vagueSearch?searchSize=6',
	data:getComboData(getRootPath()+'/userController/vagueSearch?searchSize=6'),
    toolsBar:{
        refresh:true
    },
    searchKey:"searchKey",
	focusShow:true,
});
$("#principal").fool("dhxComboGrid",{
	prompt:"选择人员",
	height:32,
	width:169,
	setTemplate:{
		input:"#userName#",
		columns:[
		         {option:'#userCode#',header:'账号',width:100},
		         {option:'#userName#',header:'名称',width:100},
				],
	},
	filterUrl:getRootPath()+'/userController/vagueSearch?searchSize=6',
	data:getComboData(getRootPath()+'/userController/vagueSearch?searchSize=6'),
    toolsBar:{
        refresh:true
    },
    searchKey:"searchKey",
	focusShow:true,
});
$("#status").fool("dhxCombo",{
	prompt:'选择单据状态',
	height:32,
	width:169,
	editable:false,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	focusShow:true,
	data:[
		  {value:"100",text:"草稿"},
		  {value:"101",text:"已提交待审核"},
		  {value:"102",text:"已审核办理中"},
		  {value:"103",text:"已延迟"},
		  {value:"104",text:"已终止"},
		  {value:"105",text:"已完成"},
		  ],
});
$("#detailWindow").window({
	top:100,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	modal:true,
	width:"90%",
	closed:true
});

//数据载入
$('#detailsList').jqGrid({
	/* datatype:function(postdata){
		if(postdata.sord=="asc"){
			postdata.sord="0";
		}else if(postdata.sord=="desc"){
			postdata.sord="1";
		}
		if(postdata.sidx=="planCode"){
			postdata.sidx="1";
		}else if(postdata.sidx=="planName"){
			postdata.sidx="2";
		}else if(postdata.sidx=="outAmount"){
			postdata.sidx="3";
		}else if(postdata.sidx=="inAmount"){
			postdata.sidx="4";
		}else if(postdata.sidx=="profit"){
			postdata.sidx="5";
		}else if(postdata.sidx=="estimatedYieldrate"){
			postdata.sidx="6";
		}else if(postdata.sidx=="effectiveYieldrate"){
			postdata.sidx="7";
		}else if(postdata.sidx=="currentYieldRate"){
			postdata.sidx="8";
		}
		$.ajax({
			url:"${ctx}/rate/planYieldRate/list",
			data:postdata,
			dataType:"json",
			complete: function(data,stat){
				if(stat=="success") {
					data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
					$("#detailsList")[0].addJSONData(data.responseJSON);
				}
			}
		});
	}, */
	autowidth:true,
	footerrow: true,
	height:$(window).height()-350,
	viewrecords:true,
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	jsonReader:{
		records:"total",
		total: "totalpages",
	},
	colModel:[
		{name:"planCode",label:'流程编号',width:7,align:'center'},
		{name:"planName",label:'流程名称',width:8,align:'center'},
		{name:"outAmount",label:'支出金额',width:5,align:'center'},
		{name:"inAmount",label:'收入金额',width:5,align:'center'},
		{name:"profit",label:'利润',width:5,align:'center'},
		{name:"estimatedYieldrate",label:'预计收益率(%)',width:8,align:'center'/*,formatter:function(value){
			if(value!=""){
				return value+"%";
			}else{
				return "0%";
			}
		}*/},
		{name:"effectiveYieldrate",label:'实际收益率(%)',width:8,align:'center'/*,formatter:function(value){
			if(value!=""){
				return value+"%";
			}else{
				return "0%";
			}
		}*/},
		{name:"referenceYieldrate",label:'参考收益率(%)',width:8,align:'center',sortable:false/*,formatter:function(value){
			if(value!=""){
				return value+"%";
			}else{
				return "0%";
			}
		}*/},
		{name:"currentYieldRate",label:'当前预计收益率(%)',width:10,align:'center'/*,formatter:function(value){
			if(value!=""){
				return value+"%";
			}else{
				return "0%";
			}
		}*/},
		{name:"plantStartDate",label:'计划开始日期',width:8,align:'center',sortable:false},
		{name:"fantipateEndTime",label:'原计划完成日期',width:8,align:'center',sortable:false},
		{name:"factualEndTime",label:'实际完成日期',width:8,align:'center',sortable:false},
		{name:"fextensionDays",label:'延迟天数',width:5,align:'center',sortable:false},
		{name:"fextensionCount",label:'延迟次数',width:5,align:'center',sortable:false},
		{name:"contractorsEfficiency",label:'承办效率(%)',width:8,align:'center',sortable:false/*,formatter:function(value){
			if(value!=""){
				return value+"%";
			}else{
				return "0%";
			}
		}*/},
		{name:"action",width:5,label:'操作',align:'center',sortable:false,formatter:function(value,options,row){
			var detail='<a href="javascript:;" class="btn-detail" onclick="detailById(\''+row.fid+'\',\''+row.planName+'\',0)" title="明细"></a>';
			var chart='<a href="javascript:;" class="btn-chart" onclick="detailById(\''+row.fid+'\',\''+row.planName+'\',1)" title="收益率分析图"></a>';
			return detail+chart;
		}},
	],
	gridComplete:function(){
		setFooter();
	}
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function rateOut(value,row,index){
	if(value){
		return value+"%";
	}
}

//明细按钮点击
function detailById(planId,planName,flag){
	$("#detailWindow").window("setTitle",planName);
	$("#detailWindow").window("open");
	$("#detailWindow").window("refresh","${ctx}/rate/flowDetail?planId="+planId+"&planName="+planName+"&flag="+flag);
}

function setFooter(){
	var rowData=$('#detailsList').getRowData();
	var inAmount=0;
	var outAmount=0;
	var profit=0;
	for(var i=0;i<rowData.length;i++){
		if(rowData[0].inAmount){
			inAmount=parseFloat(rowData[0].inAmount)+inAmount;
		}
		if(rowData[0].outAmount){
			outAmount=parseFloat(rowData[0].outAmount)+outAmount;
		}
		if(rowData[0].profit){
			profit=parseFloat(rowData[0].profit)+profit;
		}
	}
	$('#detailsList').footerData('set',{
		planName:"合计:",
		inAmount:inAmount.toFixed(2),
		outAmount:outAmount.toFixed(2),
		profit:profit.toFixed(2),
	});
}
</script>
</html>
