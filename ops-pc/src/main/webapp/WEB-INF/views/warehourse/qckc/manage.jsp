<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>期初库存信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>
<style>
  .open{
	background:url(${ctx}/resources/images/open.png) no-repeat;
	padding-left:0px;
	width:40px;
	background-position:0px -1px;
}

#goodsChooser{
  display: none;
  text-align: center;
}

#btnBox{
  text-align: center;
}

#add{
  vertical-align: middle;
  margin-top: 5px;
}
#search-form{
  display: inline-block;
  width:93%;
}
#search-form .textbox{
  margin:5px 5px 0px 0px
}
#search-form .btn-s{
  margin:5px 4px 0px 0px;
  vertical-align: middle;
}

.form p.hideOut,.form1 p.hideOut{
    display: none;
  }
.form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{width:115px;}
  
#userChooser,#customerChooser,#goodsChooser,#goodsSpecChooser{
  display: none;
}

#userSearcher,#cusSearcher,#goodsSearcher,#goodsSpecSearcher{
  text-align: left;
  margin:10px
}

</style>
</head>
<body>
	<div id="addBox"></div> 
	<div style="margin:5px 0px 10px 0px">  
	<form id="search-form">
	  <fool:tagOpt optCode="qckcAdd">
	  <a href="#" id="add" class="btn-ora-add" >新增</a> 
	  </fool:tagOpt>
	  <fool:tagOpt optCode="qckcSearch">
	  <input id="search-code"/><input id="search-startDay" /><input id="search-endDay"/><input id="search-warehouse"/><input id="search-status"/><input id="select-goods"><input id="select-goodsid" type="hidden"/><a id="search-btn" class="btn-blue btn-s">筛选</a><a id="clear-btn" class="btn-blue btn-s">清空</a>
	  </fool:tagOpt>
	</form>
	</div>
	<table id="billList"></table>
<script type="text/javascript">
enterSearch("search-btn");
var texts = [
             {title:'原始单号',key:'voucherCode'},
             {title:'单号',key:'code'},
             {title:'仓库名称',key:'inWareHouseName'},
             {title:'单据日期',key:'billDate'},
             {title:'备注',key:'describe',br:true}
             ];
var thead = [
             /* {title:'条码',key:'billCode'}, */
             {title:'编号',key:'goodsCode'},
             {title:'名称',key:'goodsName'},
             {title:'规格',key:'goodsSpec'},
             {title:'属性',key:'goodsSpecName'},
             {title:'单位',key:'unitName'},
             {title:'换算关系',key:'scale'},
             {title:'数量',key:'quentity'},
             {title:'单价',key:'unitPrice'},
             {title:'金额',key:'type'},
             {title:'备注',key:'describe'}
             ];
var tfoot = [
				{dtype:0,key:'quentity',text:'当页总数#'},
				{dtype:3,key:'unitPrice',text:'当页小计&nbsp;大写&nbsp;#'},
				{dtype:2,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#'},
				{dtype:5,key:'unitPrice',text:'合计&nbsp;大写&nbsp;#'},
				{dtype:4,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
			];
var goodsSpecChooserOpen=false;
var goodsChooserOpen=false;
$('#add').click(function(){
	$('#addBox').window({
		title:'新增期初库存',
		top:10,
		left:0,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		modal:true,
		href:'${ctx}/initialstock/qckc/edit',
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			if($("#importer").html() != ''){
			$("#importer").window("destroy");
			}
		}
	});
});

$('#refresh').click(function(){
	$('#billList').datagrid('reload');
});

var options = [
     		    {id:'0',name:'未审核'},
     		    {id:'1',name:'已审核'},
     		    {id:'2',name:'已作废'}
     		];

$('#billList').datagrid({
	url:'${ctx}/initialstock/qckc/list',
	idField:'fid',
	pagination:true,
	fitColumns:true,
	remoteSort:false,
	columns:[[
	  		{field:'fid',title:'fid',hidden:true,width:100},
	  		{field:'code',title:'单号',sortable:true,width:100,<fool:tagOpt optCode="qckcAction1">formatter:function(value,row,index){
	  			return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')">'+value+'</a>';
	  		}</fool:tagOpt>},
	  		{field:'voucherCode',title:'原始单号',sortable:true,width:100},
	  		{field:'inWareHouseName',title:'仓库',sortable:true,width:100}, 
	  		{field:'billDate',title:'单据日期',width:40,sortable:true,formatter:function(value){
	  			return value.substring(0,10);
	  		}},
	  		{field:'recordStatus',title:'状态',width:40,sortable:true,formatter:function(value){
				for(var i=0; i<options.length; i++){
					if (options[i].id == value) return options[i].name;
				}
				return value;
			}},
			<fool:tagOpt optCode="qckcAction">
	  		{field:'action',title:'操作',formatter:function(value,row,index){
	  			if(row.recordStatus==0){
	  				 var statusStr = '';
	  				<fool:tagOpt optCode="qckcAction2">
		  			 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
		  			</fool:tagOpt>
		  			<fool:tagOpt optCode="qckcAction3">
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			</fool:tagOpt>
		  			<fool:tagOpt optCode="qckcAction4">
		  			 statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+row.fid+'\')"></a> ';
		  			</fool:tagOpt>
		  			<fool:tagOpt optCode="qckcAction5">
		  			 statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
		  			</fool:tagOpt>
		  			 return statusStr ; 
	  			}else if(row.recordStatus==1){
	  				var statusStr = '';
	  				<fool:tagOpt optCode="qckcAction5">
		  			 statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
		  			</fool:tagOpt>
		  			<fool:tagOpt optCode="qckcAction3">
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			</fool:tagOpt>
		  			 return statusStr ; 
	  			}else if(row.recordStatus==2){
	  				var statusStr = '';
	  				<fool:tagOpt optCode="qckcAction3">
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			</fool:tagOpt>
		  			 return statusStr ; 
	  			}
	  		}}
	  		</fool:tagOpt>
	      ]]
});

$("#clear-btn").click(function(){
	$("#search-form").form("clear");
});

$("#search-btn").click(function(){
	var code=$("#search-code").textbox('getValue');
	var warehouseId=$("#search-warehouse").combobox('getValue');
	var recordStatus=$("#search-status").combobox('getValue');
	var startDay=$("#search-startDay").datebox('getValue');
	var endDay=$("#search-endDay").datebox('getValue');
	var goodsId=$("#select-goodsid").val();
	var options = {"codeOrVoucherCode":code,"inWareHouseId":warehouseId,"recordStatus":recordStatus,"startDay":startDay,"endDay":endDay,"goodsId":goodsId};
	$('#billList').datagrid('load',options);
});

//编辑 
function editById(fid,mark){
	var title = "修改期初库存";
	if(mark==1){
		title = "查看期初库存";
	}
	$('#addBox').window({
		title:title, 
		top:10,
		left:0,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/initialstock/qckc/edit?id="+fid,
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			if($("#importer").html() != ''){
				$("#importer").window("destroy");
			}
		}
	});
} 

//复制
function copyById(fid){
	$('#addBox').window({
		title:'新增期初库存', 
		top:10, 
		left:0,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/initialstock/qckc/edit?id="+fid+"&mark=1",
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			
		}
	});
}

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/initialstock/qckc/delete',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

function verifyById(fid){
	$.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/initialstock/qckc/passAudit',
					data : {id : fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

function cancelById(fid) {
	 $.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/initialstock/qckc/cancel',
					data : {id :fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});

};

$("#select-goods").textbox({
	prompt:'货品',
	width:160,
	height:30,
	editable:false,
});

//选择货品
$("#select-goods").textbox('textbox').click(function(){
	chooser=$.fool.window({'title':"选择货品",href:'${ctx}/goods/window?okCallBack=selectgoodsSearch&onDblClick=selectgoodsSearchDBC&singleSelect=true',
		onClose:function(){
			$(this).window("destroy");
		}		
	});
});

function selectgoodsSearch(rowData){
	$("#select-goodsid").val(rowData[0].fid);
	$("#select-goods").textbox('setValue',rowData[0].name);
	chooser.window('close');
}
function selectgoodsSearchDBC(rowData){
	$("#select-goodsid").val(rowData.fid);
	$("#select-goods").textbox('setValue',rowData.name);
	chooser.window('close');
}

$("#search-code").textbox({
	prompt:'单号或原始单号',
	width:160,
	height:32
});
$("#search-warehouse").combotree({
	prompt:'仓库',
	width:160,
	height:32,
	editable:false,
	url:"${ctx}/basedata/warehourseList",
	onLoadSuccess:function(node, data){
		if(data[0].id!=""){
			var node=$(this).tree("find",data[0].id);
			$(this).tree('update',{
				target: node.target,
				text: '请选择',
				id:""
			});
		}
	}
});

$("#search-status").combobox({
	valueField:"value",
	textField:"text",
	prompt:'状态',
	width:160,
	height:32,
	editable:false,
	data:[{value:0,text:"未审核"},{value:1,text:"已审核"},{value:2,text:"已作废"}],
	onLoadSuccess:function(){
		$(this).combobox('clear');
	}
});

$("#search-status").combobox({ 
	prompt:'状态',
	width:160,
	height:32,
	editable:false
});

$("#search-startDay").datebox({
	prompt:'开始日期',
	width:160,
	height:32,
	editable:false
});

$("#search-endDay").datebox({
	prompt:'结束日期',
	width:162,
	height:32,
	editable:false
});
$(function(){
	//分页条 
	//setPager($('#billList'));
});

</script>
</body>
</html>
