<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>采购询价单据信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
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

.form p.hideOut,.form1 p.hideOut{
    display: none;
  }
  
#userChooser,#customerChooser,#goodsChooser{
  display: none;

}
#search-form span{
  margin-right: 5px;
  margin-bottom: 10px
}
#search-form a{
  margin-right: 5px;
  margin-bottom: 10px
}
#userSearcher,#cusSearcher,#goodsSearcher{
  text-align: left;
  margin:10px
}
  .form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{width:115px;}
</style>
</head>
<body>
	<div id="addBox"></div> 
	<div style="margin: 10px 0px 0px 0px;">
	    <fool:tagOpt optCode="cgxjAdd">
		<a href="#" id="add" class="btn-ora-add" style="vertical-align: top;">新增</a>
		</fool:tagOpt>
		<form id="search-form" style="display: inline-block; width: 90%">
			<input id="search-codeOrVoucherCode"/><!-- <input id="search-voucherCode"/> --><input id="search-status"/><input id="search-startDay" /><input id="search-endDay" class="easyui-datebox" data-options="prompt:'结束日期',width:100,height:32"/><input id="select-people"><input id="select-peopleid" type="hidden"/><input id="select-goods"><input id="select-goodsid" type="hidden"/><input id="select-supplierName"><input id="select-supplierNameid" type="hidden"/><input id="select-inMemberName" name="inMemberName" class="textBox"/><input type="hidden" name="inMemberId" id="select-inMemberId"/><input id="select-deptId" name="deptId" class="textBox"/>
			<fool:tagOpt optCode="cgxjSearch">
			<a id="search-btn" class="btn-blue btn-s" href="javascript:search();" style="vertical-align:middle;">筛选</a>
		    </fool:tagOpt>
		    <a id="clear-btn" class="btn-blue btn-s" style="vertical-align: middle;">清空</a>
		</form>
	</div>
	<table id="billList"></table>
<script type="text/javascript">
var texts = [
             {title:'凭证号',key:'voucherCode'},
             {title:'单号',key:'code'},
             {title:'仓库名称',key:'inWareHouseName'},
             {title:'供应商名称',key:'supplierName'},
             {title:'供应商编号',key:'supplierCode'},
             {title:'供应商电话',key:'supplierPhone'},
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
             {title:'数量',key:'quentity',textAlign:'right'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
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
var recordStatusOptions = [{id:'0',name:'未审核'},{id:'1',name:'已审核'},{id:'2',name:'已作废'}];
var goodsSpecChooserOpen=false;
var goodsChooserOpen=false;
var memberChooserOpen = false;
$('#add').click(function(){
	$('#addBox').window({
		title:'新增采购询价单据',
		top:10,
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		modal:true,
		href:'${ctx}/purchaseinquiry/cgxjd/edit',
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			if(memberChooserOpen){
				$("#pop-win").window("destroy");
				goodsSpecChooserOpen=false;
			}
		}
	});
});


$("#clear-btn").click(function(){
	$("#search-form").form("clear");
});

$('#refresh').click(function(){
	$('#billList').datagrid('reload');
});

$("#search-codeOrVoucherCode").textbox({
	prompt:'单号',
	width:160,
	height:30,
	inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
		keyup:function(e){
			if(e.keyCode==13){
				search();
			}
		}
	})
});
/* $("#search-voucherCode").textbox({
	prompt:'凭证号',
	width:160,
	height:30
}) */
$("#search-warehouse").combobox({
	prompt:'仓库',
	width:160,
	height:30,
	editable:false
});

$("#search-status").combobox({
	valueField:"value",
	textField:"text",
	prompt:'状态',
	width:160,
	height:30,
	editable:false,
	data:[{value:0,text:"未审核"},{value:1,text:"已审核"},{value:2,text:"已作废"}],
	onLoadSuccess:function(){
		$(this).combobox('clear');
	}
});
function search(){
	var code=$("#search-codeOrVoucherCode").textbox('getValue');
	//var voucherCode = $("#search-voucherCode").textbox('getValue');
	var recordStatus=$("#search-status").combobox('getValue');
	var startDay=$("#search-startDay").datebox('getValue');
	var endDay=$("#search-endDay").datebox('getValue');
	var creatorId=$("#select-peopleid").val();
	var goodsId=$("#select-goodsid").val();
	var supplierId=$("#select-supplierNameid").val();
	var deptId = $('#select-deptId').combotree('getValue');
	var inMemberId = $('#select-inMemberId').val();
	var options = {"codeOrVoucherCode":code,"recordStatus":recordStatus,"startDay":startDay,"endDay":endDay,"createId":creatorId,"goodsId":goodsId,"supplierId":supplierId,"inMemberId":inMemberId,"deptId":deptId};
	$('#billList').datagrid('load',options);
};
$("#search-status").combobox({
	prompt:'状态',
	width:160,
	height:30,
	editable:false
});

$("#search-startDay").datebox({
	prompt:'开始日期',
	width:160,
	height:30,
	editable:false
});

$("#search-endDay").datebox({
	prompt:'结束日期',
	width:160,
	height:30,
	editable:false
});
$("#select-people").textbox({ //初始化制单人
	prompt:'制单人',
	width:160,
	height:30,
	editable:false,
});

$("#select-goods").textbox({
	prompt:'货品',
	width:160,
	height:30,
	editable:false,
});

$("#select-supplierName").textbox({
	prompt:'供应商名',
	width:160,
	height:30,
	editable:false,
});
$('#select-inMemberName').textbox({
	novalidate:true,
	width:160,
	height:30,
	prompt:'询价员'
});
$('#select-deptId').combotree({
	url:'${ctx}/orgController/getAllTree',
	novalidate:true,
	width:160,
	height:30,
	prompt:'部门'
});
$('#select-inMemberName').textbox('textbox').click(function(){
	win = $.fool.window({href:"${ctx}/member/window?okCallBack=inMemberName&onDblClick=inMemberNameDBC&singleSelect=true",'title':'选择询价员'});
});
function inMemberName(data){
	$('#select-inMemberName').textbox('setValue',data[0].username);
	$('#select-inMemberId').val(data[0].fid);
	win.window('close');
}
function inMemberNameDBC(data){
	$('#select-inMemberName').textbox('setValue',data.username);
	$('#select-inMemberId').val(data.fid);
	win.window('close');
}

var chooser="";
$("#select-people").textbox('textbox').click(function(){
	chooser=$.fool.window({'title':"选择制单人",href:'${ctx}/userController/window?okCallBack=selectUserSearch&onDblClick=selectUserSearchDBC&singleSelect=true',
		onClose:function(){
			$(this).window("destroy");
		}		
	});
});

$("#select-goods").textbox('textbox').click(function(){
	chooser=$.fool.window({'title':"选择货品",href:'${ctx}/goods/window?okCallBack=selectgoodsSearch&onDblClick=selectgoodsSearchDBC&singleSelect=true',
		onClose:function(){
			$(this).window("destroy");
		}		
	});
});

$("#select-supplierName").textbox('textbox').click(function(){
	chooser=$.fool.window({'title':"选择供应商名",href:'${ctx}/supplier/window?okCallBack=selectsupplierSearch&onDblClick=selectsupplierSearchDBC&singleSelect=true',
		onClose:function(){
			$(this).window("destroy");
		}
	});
});

function selectUserSearch(rowData){
	$("#select-peopleid").val(rowData[0].fid);
	$("#select-people").textbox('setValue',rowData[0].userName);
	chooser.window('close');
}

function selectUserSearchDBC(rowData){
	$("#select-peopleid").val(rowData.fid);
	$("#select-people").textbox('setValue',rowData.userName);
	chooser.window('close');
}

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

function selectsupplierSearch(rowData){
	$("#select-supplierNameid").val(rowData[0].fid);
	$("#select-supplierName").textbox('setValue',rowData[0].name);
	chooser.window('close');
}

function selectsupplierSearchDBC(rowData){
	$("#select-supplierNameid").val(rowData.fid);
	$("#select-supplierName").textbox('setValue',rowData.name);
	chooser.window('close');
}

$('#billList').datagrid({
	width:'100%',
	url:'${ctx}/purchaseinquiry/cgxjd/list',
	idField:'fid',
	pagination:true,
	fitColumns:true,
	remoteSort:false,
	columns:[[
	  		{field:'fid',title:'fid',hidden:true,width:100},
	  		{field:'code',title:'单号',sortable:true,width:100
	  		<fool:tagOpt optCode="cgxjAction1">,
	  			formatter:function(value,row,index){
	  			return '<a href="javascript:;" onclick="editById(\''+row.fid+'\',\'1\')">'+value+'</a>';
	  		}
	  		</fool:tagOpt>
	  		},
	  		{field:'billDate',title:'单据日期',sortable:true,width:100,formatter:dateFormatAction},
	  		{field:'supplierCode',title:'供应商编号',sortable:true,width:100},
	  		{field:'supplierName',title:'供应商名称',sortable:true,width:100},
	  		{field:'supplierPhone',title:'供应商电话',width:100},
	  		{field:'deptName',title:'部门',width:100},
	  		{field:'inMemberName',title:'询价人',width:100},	
	  		{field:'recordStatus',title:'状态',sortable:true,width:100,formatter:recordStatusAction},
	  		<fool:tagOpt optCode="cgxjAction">
	  		{field:'action',title:'操作',width:100,formatter:function(value,row,index){
	  			var d='',c='',s='',b='';
	  			<fool:tagOpt optCode="cgxjAction2">
	  			   d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
	  			 </fool:tagOpt>
	  			<fool:tagOpt optCode="cgxjAction3">
	  			   c='<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
	  			</fool:tagOpt>
	  			 <fool:tagOpt optCode="cgxjAction4">
	  			   s='<a class="btn-approve" title="审核" href="javascript:;" onclick="passAuditById(\''+row.fid+'\')"></a> ';
	  			 </fool:tagOpt>
	  			<fool:tagOpt optCode="cgxjAction5">
	  			   b='<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
	  			 </fool:tagOpt>
	  			 switch(row.recordStatus){
	  			   case 0:
	  				   return d+c+s+b;
	  				   break;
	  			   case 1:
	  			       return c+b;
	  			       break;
	  			   case 2:
	  				   return c;
	  				   break;
	  			 }
	  		}}
	  		</fool:tagOpt>
	      ]]
});

function refreshData(){
	var code=$("#search-code").textbox('getValue');
	var warehouse=$("#search-warehouse").combobox('getValue');
	var status=$("#search-status").combobox('getValue');
	var startDay=$("#search-startDay").datebox('getValue');
	var endDay=$("#search-endDay").datebox('getValue');
	var peopleId=$("#select-peopleid").textbox('getValue');
	var goodsId=$("#select-goodsid").textbox('getValue');
	var supplierid=$("#select-supplierNameid").textbox('getValue');
	var options = {"code":code,"warehouse":warehouse,"status":status,"startDay":startDay,"endDay":endDay,"peopleId":peopleId,"goodsId":goodsId,"supplierid":supplierid};
	$('#billList').datagrid('load',options);
}
//状态翻译
function recordStatusAction(value){
	for(var i=0; i<recordStatusOptions.length; i++){
		if (recordStatusOptions[i].id == value) return recordStatusOptions[i].name;
	}
	return value;
}
//编辑 
function editById(fid,mark){
	var title = "修改采购询价单据";
	if(mark==1){
		title = "查看采购询价单据";
	}
	$('#addBox').window({
		title:title, 
		top:10,
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		modal:true,
		href:'${ctx}/purchaseinquiry/cgxjd/edit?id='+fid,
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			if(memberChooserOpen){
				$("#pop-win").window("destroy");
				goodsSpecChooserOpen=false;
			}
		} 
	});
} 
//复制
function copyById(fid){
	$('#addBox').window({
		title:'新增采购询价单据', 
		top:10,
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/purchaseinquiry/cgxjd/edit?id="+fid+"&mark=1",
		modal:true,
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			if(memberChooserOpen){
				$("#pop-win").window("destroy");
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
					url : '${ctx}/purchaseinquiry/cgxjd/delete',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {	
						if(data.returnCode == '0'){
							$.fool.alert({msg:'删除成功！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
		    		}
				});
		 }
	 }});
}
//审核
function passAuditById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要审核该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/purchaseinquiry/cgxjd/passAudit',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {	
						if(data.returnCode == "0"){
							$.fool.alert({msg:'审核已通过！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
						else{
							$.fool.alert({msg:data.message});
						}
		    		}
				});
		 }
	 }});
}
//废除
function cancelById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要废除该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/purchaseinquiry/cgxjd/cancel',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {	
						if(data.returnCode == "0"){
							$('#billList').datagrid('reload');
						}
						else{
							$.fool.alert({msg:data.message});
						}
		    		}
				});
		 }
	 }});
}

$(function(){
	//分页条 
	//setPager($('#billList'));
});
</script>
</body>
</html>
