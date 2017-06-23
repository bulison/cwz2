<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>货品利润分析</title>
    <%@ include file="/WEB-INF/views/common/header.jsp"%>
    <%@ include file="/WEB-INF/views/common/js.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/css/rateModel.css" />
</head>
<body>
	<div class="titleCustom">
         <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>货品利润分析</h1>
         </div>             
    </div>
    
    <div class="myform">
	    <div class="searchBox" >
	        <form class="form1 inSearch" id='inSearch' >
	       		<p><font>开始日期：</font><input id="startDate" name="startDate" /> </p>
	       		<p><font>结束日期：</font><input id="endDate" name="endDate" /> </p>
	       		<p><font>货品：</font><input id="goodsId" name="goodsId" /> </p>
	       		<p><font>货品属性：</font><input id="goodsSpecId" name="specId" /> </p>
	       		<a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a>
	       	    <a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a>
	        </form>
	    </div>
	</div>
	<table id="detailsList"></table>
	<div id="pager"></div>
	<div id="detailWindow"></div>
</body>
<script>
var specUrl=""; //属性链接地址
$("#startDate").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择日期",
});
$("#endDate").datebox({
	editable:false,
	height:30,
	width:167,
	prompt:"选择日期",
});
var goodsCombo=$("#goodsId").fool("dhxComboGrid", {
    prompt: "选择货品",
    height: 30,
    width: 167,
    setTemplate: {
        input: "#name#",
        columns: [
            {option: '#name#', header: '名称', width: 100},
            {option: '#code#', header: '编码', width: 100},
            {option: '#barCode#', header: '条码', width: 100},
        ],
    },
    toolsBar: {
        refresh: true
    },
    filterUrl: getRootPath() + '/goods/vagueSearch?searchSize=6',
    data: getComboData(getRootPath() + '/goods/vagueSearch?searchSize=6'),
    searchKey: "searchKey",
    focusShow: true,
    onChange: function (value, text) {
        if(value){
            var opt = (goodsCombo.getOption(value)).text;
            if(opt.goodsSpecGroupId){
            	($("#goodsSpecId").next())[0].comboObj.enable();
                specUrl = getRootPath() + "/goodsspec/getPartTree?groupId=" + opt.goodsSpecGroupId;
                getSpec(specUrl);
            }else{
            	($("#goodsSpecId").next())[0].comboObj.disable();
            	($("#goodsSpecId").next())[0].comboObj.setComboText("");
            }
		}
    }
});
$("#goodsSpecId").fool("dhxComboGrid",{
	prompt:"选择货品属性",
	height:30,
	width:167,
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#name#',header:'名称',width:100},
					{option:'#code#',header:'编码',width:100},
					],
	},
	toolsBar:{
		refresh:true
	},
	editable:false,
	searchKey:"searchKey",
	focusShow:true,
});
($("#goodsSpecId").next())[0].comboObj.disable();

$("#detailWindow").window({
	top:10,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	modal:true,
	width:"90%",
	closed:true
});

$('#detailsList').jqGrid({
	/* datatype:function(postdata){
		$.ajax({
			url:"${ctx}/api/rate/goodsProfitAnalysis",
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
	height:$(window).outerHeight()-260+"px",
	pager:"#pager",
	/* rowNum:10,
	rowList:[10,20,30], */
	footerrow:true,
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",
	},
    gridComplete:completeMethod,
	colModel:[
		{name:"goodsId",label:'货品编号',hidden:true},
		{name:"specId",label:'货品名称',hidden:true},
		{name:"goodsCode",label:'货品编号',width:5,align:'center'},
		{name:"goodsName",label:'货品名称',width:5,align:'center'},
		{name:"specName",label:'货品属性',width:5,align:'center'},
		{name:"accountUnitName",label:'单位',width:5,align:'center'},
		{name:"accountQuentity",label:'销售数量',width:5,align:'center',formatter:function(value){
			if(value){
				return value.toFixed(2);
			}else{
				return "0";
			}
		}},
		{name:"accountAmount",label:'销售金额',width:5,align:'center',formatter:function(value){
			if(value){
				return value.toFixed(2);
			}else{
				return "0";
			}
		}},
		{name:"costAmount",label:'货品成本',width:5,align:'center',formatter:function(value){
			if(value){
				return value.toFixed(2);
			}else{
				return "0";
			}
		}},
		{name:"profit",label:'利润',width:5,align:'center',formatter:function(value){
			if(value){
				return value.toFixed(2);
			}else{
				return "0";
			}
		}},
		{name:"profitRate",label:'利润率(%)',width:5,align:'center',formatter:function(value){
			if(value){
				return value.toFixed(2);
			}else{
				return "0";
			}
		}},
		{name:"action",width:5,label:'操作',align:'center',formatter:function(value,options,row){
			return '<a href="javascript:;" class="btn-detail" onclick="detailById(\''+row.goodsName+'\',\''+row.goodsId+'\',\''+row.specId+'\',\''+row.specName+'\',\''+row.accountUnitName+'\')" title="明细"></a>';
		}},
	],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function completeMethod() {
	var sumAccountQuentity = $("#detailsList").getCol("accountQuentity",false,"sum");
	var sumAccountAmount = $("#detailsList").getCol("accountAmount",false,"sum");
	var sumCostAmount =  $("#detailsList").getCol("costAmount",false,"sum");
	var sumProfit = $("#detailsList").getCol("profit",false,"sum");
    $("#detailsList").footerData("set",{
        specName:'合计',
        accountQuentity:sumAccountQuentity,
        accountAmount:sumAccountAmount,
        costAmount:sumCostAmount,
        profit:sumProfit
	})
}

//搜索按钮点击
$('#mySearch').click(function(){
	var options = $('#inSearch').serializeJson();
	$('#detailsList').jqGrid("setGridParam",{
		datatype:function(postdata){
			var request=$.extend(postdata,options);
			$.ajax({
				url:"${ctx}/api/rate/goodsProfitAnalysis",
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
    /* $('#detailsList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid"); */
});

//清除按钮点击
$('#clearForm').click(function(){
	($("#goodsSpecId").next())[0].comboObj.disable();
	($("#goodsSpecId").next())[0].comboObj.setComboText("");
	($("#goodsId").next())[0].comboObj.setComboText("");
	$('#inSearch').form('clear');
});

//明细按钮点击
function detailById(goodsName,goodsId,specId,specName,accountUnitName){
	if(!goodsId||goodsId=="undefined"){
		goodsId="";
	}
	if(!specId||specId=="undefined"){
		specId="";
		specName="";
	}
	$("#detailWindow").window("setTitle",goodsName+" "+specName+" "+accountUnitName);
	$("#detailWindow").window("open");
	var url = encodeURI("${ctx}/rate/detailPage?goodsName="+goodsName+"&goodsId="+goodsId+"&specId="+specId);
	$("#detailWindow").window("refresh",url);
}

//货品属性弹出框改为下拉框
function getSpec(specUrl) {
	$("#goodsSpecId").fool("dhxComboGrid",{
		prompt:"选择货品属性",
		height:30,
		width:167,
		setTemplate:{
			input:"#name#",
			columns:[
				{option:'#name#',header:'名称',width:100},
				{option:'#code#',header:'编码',width:100},
			],
		},
		toolsBar:{
			refresh:true
		},
		editable:false,
		data:getComboData(specUrl,"tree"),
		searchKey:"searchKey",
		focusShow:true,
		onLoadSuccess:function () {
			($('#goodsSpecId').next())[0].comboObj.setComboText("");
		},
	});
}
</script>
</html>
