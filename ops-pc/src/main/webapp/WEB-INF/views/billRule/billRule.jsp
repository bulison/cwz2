<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>单号生成规则管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>单号生成规则</h1>
                </div>             
             </div>

	<div id="addBox"></div> 
	<div style="margin: 10px 0px;">
	<!-- <a href="#" id="add" class="btn-ora-add" >新增</a> -->
	</div>
	<table id="bankList"></table>
	<div id="pager"></div>
<script type="text/javascript">
var options = [
     		    {id:'0',name:'系统自动生成'},
     		    {id:'1',name:'用户手动输入'}
     		];
var billTypes=[{id:'91',name:'期初库存'},
               {id:'10',name:'采购订单'},
               {id:'11',name:'采购入库'},
               {id:'12',name:'采购退货'},
               {id:'13',name:'采购询价单'},
               {id:'20',name:'盘点单'},
               {id:'21',name:'调仓单'},
               {id:'22',name:'报损单'},
               {id:'30',name:'生产领料（原材料）'},
               {id:'31',name:'成品入库'},
               {id:'40',name:'销售订单'},
               {id:'41',name:'销售出货'},
               {id:'42',name:'销售退货'},
               {id:'43',name:'销售报价单'},
               {id:'51',name:'收款单'},
               {id:'52',name:'付款单'},
               {id:'53',name:'费用单'}];
$('#add').click(function(){
	$('#addBox').window({
		title:'新增单号规则',
		top:($(window).height()-320)/2+$(document).scrollTop(),
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/billrule/add', 
		width:650,
	});
});

$('#refresh').click(function(){
	//$('#bankList').datagrid('reload');
	$("#bankList").jqGrid("setGridParam", {//重新加载数据
	    postData: {}
	}).trigger("reloadGrid");
});

/* $('#bankList').datagrid({
	url:'${ctx}/billrule/list',
	idField:'fid',
	pagination:true,
	fitColumns:true,
	singleSelect:true,
	remoteSort:false,
	onLoadSuccess:function(){
		warehouseAll();
	},
	columns:[[
	  		{field:'fid',title:'fid',hidden:true,width:100},
	  		{field:'billName',title:'单据名称',sortable:true,width:100},
	  		// {field:'billType',title:'单据类型',width:100,formatter:function(value){
	  			//for(var i=0; i<billTypes.length; i++){
					//if (billTypes[i].id == value){
						//return billTypes[i].name;
					//}
				//}
				//return value;
			}}, 
			{field:'ruleType',title:'规则类型',sortable:true,width:100,formatter:function(value){
				for(var i=0; i<options.length; i++){
					if (options[i].id == value) return options[i].name;
				}
				return value;
			}},
			<fool:tagOpt optCode="bruleAction">
	  		{field:'action',title:'操作',width:30,formatter:function(value,row,index){
	  			var statusStr = '';
	  			<fool:tagOpt optCode="bruleAction1">statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  			 return statusStr ; 
	  		}}
	  		</fool:tagOpt>
	      ]]
}); */

$('#bankList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/billrule/list',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#bankList")[0].addJSONData(data.responseJSON);
		    	}
		    }
		});
	},
	autowidth:true,
	height:$(window).outerHeight()-200+"px",
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
	loadcomplete:function(){
		warehouseAll();
	},
	colModel:[
	  		{name:'fid',label:'fid',hidden:true,width:100},
	  		{name:'billName',label:'单据名称',sortable:true,width:100,align:'center'},
			{name:'ruleType',label:'规则类型',sortable:true,width:100,align:'center',formatter:function(value){
				for(var i=0; i<options.length; i++){
					if (options[i].id == value) return options[i].name;
				}
				return value;
			}},
			<fool:tagOpt optCode="bruleAction">
	  		{name:'action',label:'操作',width:30,align:'center',formatter:function(value,options,row){
	  			var statusStr = '';
	  			<fool:tagOpt optCode="bruleAction1">statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  			 return statusStr ; 
	  		}}
	  		</fool:tagOpt>
	      ]
});

//编辑 
function editById(fid){
	$('#addBox').window({
		title:'修改单号规则', 
		top:($(window).height()-320)/2+$(document).scrollTop(),
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:'${ctx}/billrule/edit?id='+fid, 
		width:650,  
	});
} 


$(function(){
	//分页条 
	//setPager($('#bankList'));
});
</script>
</body>
</html>
