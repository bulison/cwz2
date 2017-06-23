<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>仓储会计期间</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<style>
  
</style>
</head>
<body>
	<div id="addBox"></div>
	<div id="createBox" class="form1" >   
	  <p><font>显示天数：</font><input id="showDays" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'输入显示天数',novalidate:true,validType:'ddPrice[1,28]'"/></p>
	  <p><font style="width:60px"><a href="javascript:;" id="createBtn" class="btn-blue btn-s">生成</a></font></p> 
	</div>
	<div class="toolBox">
		<div class="toolBox-button" style="margin-right:5px;">
	  		<fool:tagOpt optCode="speriodAdd"><a href="javascript:;" id="add" class="btn-ora-add">新增</a></fool:tagOpt>
	 		<fool:tagOpt optCode="speriodCreat"><a href="javascript:;" id="create" class="btn-ora-export">生成</a></fool:tagOpt>
	 	</div>
	</div>  
	<table id="periodList"></table>
<script type="text/javascript">
$('#createBox').window({
	title:'选择显示天数',
	top:80, 
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	width:430, 
	height:80,
	closed:true
});

$('#add').click(function(){
	$('#addBox').window({
		title:'新增会计期间',
		top:80,  
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/periodController/addPeriod',
		width:656,
		height:270 
	});
});

/* $('#refresh').click(function(){
	$('#periodList').datagrid('reload');
}); */

$('#create').click(function(){
	$('#createBox').window("open");  
});

$('#createBtn').click(function(){
	$('#showDays').validatebox("enableValidation");
	if($('#showDays').validatebox("isValid")){
		$.post("${ctx}/periodController/saveList",{number:$("#showDays").val()},function(data){
			if(data=="1"){
				$.fool.alert({
					msg:"生成成功！",
					fn:function(){
						$('#periodList').datagrid("reload");
						$('#createBox').window('close');
					}
				});
			}else{
				$.fool.alert({
					msg:"生成失败！"
				});
			}
		});
	}else{
		return false; 
	}
});

var options = [
   		    {id:'0',name:'未结账'},
   		    {id:'1',name:'已结账'}
   		];

$('#periodList').datagrid({
	url:'${ctx}/periodController/list',
	idField:'fid',
	pagination:true,
	fitColumns:true,
	remoteSort:false,
	pageNumber:1,
    pageSize:10,
    pageList:[10, 20, 30],
	columns:[[
	  		{field:'fid',title:'fid',hidden:true,width:100},
	  		{field:'isLast',title:'isLast',hidden:true,width:100},
	  		{field:'period',title:'会计期间',width:100,sortable:true},
	  		{field:'startDate',title:'开始日期',width:100,sortable:true},
	  		{field:'endDate',title:'结束日期',width:100,sortable:true},
	  		{field:'checkoutStatus',title:'结账状态',width:100,formatter:function(value){
				for(var i=0; i<options.length; i++){
					if (options[i].id == value) return options[i].name;
				}
				return value;
			}},
			{field:'description',title:'描述',width:100},
			{field:'updateTime',title:'修改时间',width:100,sortable:true},
			<fool:tagOpt optCode="speriodAction">
	  		{field:'action',title:'操作',width:40,formatter:function(value,row,index){
	  			var e='',c='',d='';
	  			<fool:tagOpt optCode="speriodAction1">e = '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a>'; </fool:tagOpt>
	  			<fool:tagOpt optCode="speriodAction2">c = '<a class="btn-detail" title="更改结账状态" href="javascript:;" onclick="checkById(\''+row.fid+'\',\''+row.checkoutStatus+'\')"></a>'; </fool:tagOpt>
	  			<fool:tagOpt optCode="speriodAction3">d = '<a class="btn-del" title="删除" href="javascript:;" onclick="deleteById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  			if(row.isLast==0){
	  				return c;
	  			}else if(row.isLast==1){
	  				return c+""+e+""+d;
	  			}
	  		}}
	  		</fool:tagOpt>
	      ]]
});

//编辑 
function editById(fid){
	$('#addBox').window({
		title:'修改会计期间', 
		top:80,  
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/periodController/editPeriod?fid="+fid,
		width:656,  
		height:270   
	});
} 

//结账
function checkById(fid){
	 $.fool.confirm({title:'确定',msg:'确定要更改结账状态吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/periodController/checkoutStatus',
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {	
						if(data.returnCode == '0'){
							$.fool.alert({msg:'更改结账状态成功！',fn:function(){
								$('#periodList').datagrid('reload');
							}});
						}else{
							$.fool.alert({msg:data.message});
						}
		    		}
				});
		 }
	 }});
}

function deleteById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/periodController/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {	
						if(data.returnCode == '0'){
							$.fool.alert({msg:'删除成功！',fn:function(){$('#periodList').datagrid('reload');}});
						}else{
							$.fool.alert({msg:data.message});
						}
		    		}
				});
		 }
	 }});
}

$.extend($.fn.validatebox.defaults.rules, {
	ddPrice:{
        validator:function(value,param){
          if(/^[1-9]\d*$/.test(value)){
            return value >= param[0] && value <= param[1];
          }else{
            return false;
          }
        },
        message:'请输入1到28之间正整数'
      },
      period:{
          validator:function(value,param){
            if(/^[1-9]\d*$/.test(value)){
              return value.substring(0,3)<= 9999 && value.substring(4)<= 12 && value.length==6; 
            }else{
              return false;
            }
          },
          message:'请输入年份+月份六位数字，如“201501”'
        }
});

$(function(){
	//分页条 
	//setPager($('#periodList'));
});
</script>
</body>
</html>
