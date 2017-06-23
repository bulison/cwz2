<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>采购退货单据信息管理</title>
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

#userSearcher,#cusSearcher,#goodsSearcher{
  text-align: left;
  margin:10px;
}
#search-form span{
  margin-right: 5px;
  margin-bottom: 10px
}
#search-form a{
  margin-right: 5px;
  margin-bottom: 10px
}
  .form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{width:115px;}
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="#">仓库应用</a></li>
	        <li><a href="#" class="curr">采购退货单据</a></li>
	    </ul>
	</div>
	<div id="addBox"></div> 
	<!-- <a href="#" id="add" class="btn-ora-add" style="width:60px;margin:10px 0 10px 0;display: inline-block;vertical-align:middle;">新增</a>
	<a href="${ctx}/purchaseback/cgth/export" id="getOut" class="btn-ora-add" style="width:60px;margin:10px 0 10px 0;display: inline-block;vertical-align:middle;">导出</a>
	<a href="#" id="refresh" class="btn-ora-add" style="width:60px;margin:10px 0 10px 0;display: inline-block;vertical-align:middle;">刷新</a>
	<input id="search-code"/>+<input id="search-status"/>+<input id="search-startDay" />-<input id="search-endDay" class="easyui-datebox" data-options="prompt:'结束日期',width:100,height:32"/> <a id="search-btn" class="btn-blue btn-s">筛选</a>
	<table id="billList"></table>
	 -->
	
	
	<div style="margin:10px 0px 0px 0px;">
	    <fool:tagOpt optCode="cgthAdd">
		<a href="#" id="add" class="btn-ora-add" style="vertical-align: top;">新增</a>
		</fool:tagOpt>
		<form action="" id="search-form" style="display: inline-block; width: 90%">
			<input id="search-codeOrVoucherCode"/><!-- <input id="search-voucherCode"/> --><input id="search-status"/><input id="search-startDay" /><input id="search-endDay" class="easyui-datebox" data-options="prompt:'结束日期',width:160,height:32"/><input id="select-supplierName" name="select-supplierName" class="textbox"/><input type="hidden" name="select-supplierId" id="select-supplierId" />
			<input id="select-inMemberName" name="select-inMemberName" class="textBox"/><input type="hidden" name="select-inMemberId" id="select-inMemberId" /><input id="select-deptId" name="select-deptId" class="textBox"/><input id="select-goods"><input id="select-goodsid" type="hidden"/>
			<fool:tagOpt optCode="cgthSearch"><a class="btn-blue btn-s go-search" style="vertical-align:middle;" onclick="refreshData()">筛选</a></fool:tagOpt>
		    <a id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a> 
		</form>
	</div>
	<table id="billList"></table>
<script type="text/javascript">
var texts = [
             /* {title:'凭证号',key:'voucherCode'},
             {title:'单号',key:'code'},
             {title:'仓库名称',key:'inWareHouseName'},
             {title:'供应商名称',key:'supplierName'},
             {title:'供应商编号',key:'supplierCode'},
             {title:'供应商电话',key:'supplierPhone'},
             {title:'单据日期',key:'billDate'},
             {title:'备注',key:'describe',br:true}, */
             
             {title:'供应商',key:'supplierName'},
             {title:'电话',key:'supplierPhone'},
             {title:'NO',key:'code'},
             {title:' 地<span style="margin:0px 7px;"></span>址',key:'supplierAddress',colspan:2},
             {title:'单据日期',key:'billDate'},
             ];
var thead = [
             /* {title:'条码',key:'billCode'}, 
             {title:'编号',key:'goodsCode'},
             {title:'名称',key:'goodsName'},
             {title:'规格',key:'goodsSpec'},
             {title:'属性',key:'goodsSpecName'},
             {title:'单位',key:'unitName'},
             {title:'换算关系',key:'scale'},
             {title:'数量',key:'quentity',textAlign:'right'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe'}*/
             
             {title:'编号',key:'goodsCode',width:8},
             {title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
             {title:'单位',key:'unitName'},
             {title:'数量',key:'quentity'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe',width:10}
             ];
var tfoot = [
				/* {dtype:0,key:'quentity',text:'当页总数#'},
				{dtype:3,key:'unitPrice',text:'当页小计&nbsp;大写&nbsp;#'},
				{dtype:2,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#'},
				{dtype:5,key:'unitPrice',text:'合计&nbsp;大写&nbsp;#'},
				{dtype:4,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'}, */
				{dtype:3,key:'type',text:'金额合计(大写)：#'},
				{dtype:2,key:'type',text:'小写金额：¥#元'},
			];
var recordStatusOptions = [{id:'0',name:'未审核'},{id:'1',name:'已审核'},{id:'2',name:'已作废'}];
var goodsChooserOpen=false;
var goodsSpecChooserOpen=false;
var memberChooserOpen = false;
$('#add').click(function(){
	$('#addBox').window({
		title:'新增采购退货单',
		top:10, 
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/purchaseback/cgth/edit',
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
			if (memberChooserOpen) {
				$("#pop-win").window("destroy");
				memberChooserOpen = false;
			}
		}
	});
});
$('#select-inMemberName').textbox({
	novalidate:true,
	width:158,
	height:30,
	prompt:'采购员'
});
$('#select-deptId').combotree({
	url:'${ctx}/orgController/getAllTree',
	novalidate:true,
	width:160,
	height:30,
	prompt:'部门'
});
$('#select-supplierName').textbox({
	novalidate:true,
	width:160,
	height:30,
	prompt:'供应商'
});
$("#select-goods").textbox({
	prompt:'货品',
	width:160,
	height:30,
	editable:false,
});
$("#select-goods").textbox('textbox').click(function(){
	chooser=$.fool.window({'title':"选择货品",href:'${ctx}/goods/window?okCallBack=selectgoodsSearch&onDblClick=selectgoodsSearchDBC&singleSelect=true',
		onClose:function(){
			$(this).window("destroy");
		}		
	});
});
$('#select-inMemberName').textbox('textbox').click(function(){
	win = $.fool.window({href:"${ctx}/member/window?okCallBack=inMemberName&onDblClick=inMemberNameDBC&singleSelect=true",'title':'选择采购员',
			onClose:function(){
				$(this).window("destroy");
			}});
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
$('#select-supplierName').textbox('textbox').click(function(){
	win = $.fool.window({href:"${ctx}/supplier/window?okCallBack=supplierName&onDblClick=supplierNameDBC&singleSelect=true",'title':'选择供应商',
		onClose:function(){
			$(this).window("destroy");
		}});
});
function supplierName(data){
	$('#select-supplierName').textbox('setValue',data[0].name);
	$('#select-supplierId').val(data[0].fid);
	win.window('close');
}
function supplierNameDBC(data){
	$('#select-supplierName').textbox('setValue',data.name);
	$('#select-supplierId').val(data.fid);
	win.window('close');
}

 $('#refresh').click(function(){
	$('#billList').datagrid('reload');
});
/* $("#search-voucherCode").textbox({
	prompt:'凭证号',
	width:160,
	height:30
}) */
$("#search-codeOrVoucherCode").textbox({
	prompt:'单号或凭证号',
	width:160,
	height:30,
	inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
		keyup:function(e){
			if(e.keyCode==13){
				refreshData();
			}
		}
	})
}); 
//comboTree($("#search-warehouse"),"${ctx}/basedata/warehourseList",true);
$("#search-warehouse").combotree({
	prompt:'仓库',
	width:100,
	height:30,
	editable:false,
	url:"${ctx}/basedata/warehourseList"
}); 
$("#search-warehouse").combotree({
	prompt:'仓库',
	width:160,
	height:30,
	editable:false,
	url:"${ctx}/basedata/warehourseList"
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
$("#clear-btn").click(function(){
	$("#search-form").form("clear");
}); 
$('#billList').datagrid({
	url:'${ctx}/purchaseback/cgth/list',
	width:'100%',
	idField:'fid',
	pagination:true,
	fitColumns:true,
	remoteSort:false,
	columns:[[
	  		{field:'fid',title:'fid',hidden:true,width:100},
	  		{field:'code',title:'单号',sortable:true,width:100
	  		<fool:tagOpt optCode="cgthAction1">,
	  			formatter:function(value,row,index){
	  			return '<a href="javascript:;" onclick="editById(\''+row.fid+'\',\'1\')">'+value+'</a>';
	  		}
	  		</fool:tagOpt>
	  		},
	  		{field:'voucherCode',title:'凭证号',sortable:true,width:100},
	  		{field:'billDate',title:'单据日期',sortable:true,width:100,formatter:dateFormatAction},
	  		{field:'supplierCode',title:'供应商编号',sortable:true,width:100},
	  		{field:'supplierName',title:'供应商名称',sortable:true,width:100},
	  		{field:'supplierPhone',title:'供应商电话',width:100},
	  		{field:'deptName',title:'部门',width:100},
	  		{field:'inWareHouseName',title:'仓库',width:100},
	  		{field:'recordStatus',title:'状态',sortable:true,width:100,formatter:recordStatusAction},	  	
	  		<fool:tagOpt optCode="cgthAction">
	  		{field:'action',title:'操作',width:100,formatter:function(value,row,index){
	  			var d='',c='',s='',b='';
	  			<fool:tagOpt optCode="cgthAction2">
	  			   d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
	  			 </fool:tagOpt>
	  			<fool:tagOpt optCode="cgthAction3">
	  			   c='<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
	  			</fool:tagOpt>
	  			 <fool:tagOpt optCode="cgthAction4">
	  			   s='<a class="btn-approve" title="审核" href="javascript:;" onclick="passAuditById(\''+row.fid+'\')"></a> ';
	  			 </fool:tagOpt>
	  			<fool:tagOpt optCode="cgthAction5">
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
//状态翻译
function recordStatusAction(value){
	for(var i=0; i<recordStatusOptions.length; i++){
		if (recordStatusOptions[i].id == value) return recordStatusOptions[i].name;
	}
	return value;
}

function refreshData(){
	var codeOrVoucherCode=$("#search-codeOrVoucherCode").textbox('getValue');
	//var voucherCode = $("#search-voucherCode").textbox('getValue');
	var status=$("#search-status").combobox('getValue');
	var startDay=$("#search-startDay").datebox('getValue');
	var endDay=$("#search-endDay").datebox('getValue');
	var supplierId=$('#select-supplierId').val();
	var deptId=$('#select-deptId').combotree('getValue');
	var inMemberId=$('#select-inMemberId').val();
	var goodsId=$("#select-goodsid").val();
	var options = {"goodsId":goodsId,"codeOrVoucherCode":codeOrVoucherCode,"deptId":deptId,"inMemberId":inMemberId,"supplierId":supplierId,"recordStatus":status,"startDay":startDay,"endDay":endDay};
	$('#billList').datagrid('load',options);
} 

//编辑 
function editById(fid,mark){
	var title='修改采购退货单';
	if(mark==1){
		title='查看采购退货单'
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
		href:'${ctx}/purchaseback/cgth/edit?id='+fid,
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
				memberChooserOpen=false;
			}
		}
	});
} 
//复制
function copyById(fid){
	$('#addBox').window({
		title:'新增盘点单', 
		top:10,
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		modal:true,
		href:"${ctx}/purchaseback/cgth/edit?id="+fid+"&mark=1",
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			if (memberChooserOpen) {
				$("#pop-win").window("destroy");
				memberChooserOpen = false;
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
					url : '${ctx}/purchaseback/cgth/delete',
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
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
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
					url : '${ctx}/purchaseback/cgth/passAudit',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						if(data.returnCode == "0"){
							$('#billList').datagrid('reload');
						}
						else{
							$.fool.alert({msg:data.message});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
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
					url : '${ctx}/purchaseback/cgth/cancel',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						if(data.returnCode == "0"){
							$('#billList').datagrid('reload');
						}
						else{
							$.fool.alert({msg:data.message});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
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
