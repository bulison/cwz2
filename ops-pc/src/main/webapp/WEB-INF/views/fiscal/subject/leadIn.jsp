<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
<div id="optionsBox">
  <form id="optionsForm">
  <input id="subjectId" name="subjectId" type="hidden" value="${subjectId}"/>
  <p><input id="cash" name="relationType" type="radio" value="9"/> 现金</p>
  <p><input id="bank" name="relationType" type="radio" checked="checked" value="1"/> 银行</p>
  <p><input id="supplier" name="relationType" type="radio" value="2"/> 供应商</p>
  <p><input id="customer" name="relationType" type="radio" value="3"/> 客户</p>
  <p><input id="department" name="relationType" type="radio" value="4"/> 部门</p>
  <p><input id="member" name="relationType" type="radio" value="5"/> 职员</p>
  <p><input id="warehouse" name="relationType" type="radio" value="6"/> 仓库</p>
  <p><input id="project" name="relationType" type="radio" value="7"/> 项目</p>
  <p><input id="goods" name="relationType" type="radio" value="8"/> 货品</p>
  <p><input id="saveSelected" class="btn-blue2 btn-xs" type="button" value="保存"/></p>
  <!-- <div id="bankGrid"></div>
  <div id="supGrid"></div>
  <div id="cusGrid"></div> -->
  </form> 
</div>
<script type="text/javascript">
$(function(){
	/* //初始化银行列表
	$("#bankGrid").datagrid({
		  fitColumns:true,
		  pagination:true,
		  singleSelect:false,
		  scrollbarSize:0,
		  columns:[[
		            
	                {field:'fid',title:'fid',checkbox:true},
	                {field:'code',title:'编号',width:100},
	                {field:'name',title:'名称',width:100},
	                {field:'bank',title:'开户行',width:100},
	                {field:'account',title:'账号',width:100},
		            ]]
	});

	//初始化客户列表
	$("#cusGrid").datagrid({
		  fitColumns:true,
		  pagination:true,
		  singleSelect:false,
		  scrollbarSize:0,
		  columns:[[
	                {field:'fid',title:'fid',checkbox:true},
	                {field:'code',title:'编号',width:100},
	                {field:'name',title:'名称',width:100},
	                {field:'bank',title:'开户行',width:100},
	                {field:'categoryName',title:'类别',width:100},
		            ]]
	});

	//初始化供应商列表
	$("#supGrid").datagrid({
		  fitColumns:true,
		  pagination:true,
		  singleSelect:false,
		  scrollbarSize:0,
		  columns:[[
	                {field:'fid',title:'fid',checkbox:true},
	                {field:'code',title:'编号',width:100},
	                {field:'name',title:'名称',width:100},
	                {field:'categoryName',title:'类别',width:100},
		            ]]
	});
	
	//隐藏列表
	$("#bankGrid").datagrid("getPanel").hide();
	$("#supGrid").datagrid("getPanel").hide();
	$("#cusGrid").datagrid("getPanel").hide(); */
});

//单选框选择事件
/* $(":radio").change(function(){
	if($(this).prop("checked")){
		if($(this).val()=="bank"){
			$("#bankGrid").datagrid({url:'${ctx}/bankController/list'});
			setPager($('#bankGrid'));
			$("#bankGrid").datagrid("getPanel").fadeIn();
			$("#supGrid").datagrid("getPanel").hide();
			$("#cusGrid").datagrid("getPanel").hide();
		}else if($(this).val()=="supplier"){
			$("#supGrid").datagrid({url:'${ctx}/supplier/list'});
			$("#bankGrid").datagrid("getPanel").hide();
			$("#supGrid").datagrid("getPanel").fadeIn();
			$("#cusGrid").datagrid("getPanel").hide();
			setPager($('#supGrid'));
		}else if($(this).val()=="customer"){
			$("#cusGrid").datagrid({url:'${ctx}/customer/list'});
			$("#bankGrid").datagrid("getPanel").hide();
			$("#supGrid").datagrid("getPanel").hide();
			$("#cusGrid").datagrid("getPanel").fadeIn();
			setPager($('#cusGrid'));
		}
	}
}); */


//保存按钮点击事件
/* $("#saveSelected").click(function(){
	var rows=""
	if($(":radio:checked").val()=="bank"){
		rows=$("#bankGrid").datagrid('getChecked');
	}else if($(":radio:checked").val()=="supplier"){
		rows=$("#supGrid").datagrid('getChecked');
	}else if($(":radio:checked").val()=="customer"){
		rows=$("#cusGrid").datagrid('getChecked');
	}
	if(!rows){$.fool.alert({msg:"未选择任何信息。"}); return false;}
	$('#saveSelected').attr("disabled","disabled");
	$.post('',rows,function(data){
		if(data.returnCode=='0'){
			$.fool.alert({msg:'引入成功！',fn:function(){
				$('#saveSelected').removeAttr("disabled");
				$('#leadInBox').window('close');
				$('#subjectTree').tree('reload');
				$('#subjectList').datagrid('reload');
			}});
		}else if(data.returnCode=='1'){
			$.fool.alert({msg:data.message,fn:function(){
				$('#saveSelected').removeAttr("disabled");
				$('#subjectTree').tree('reload');
			}});
		}else{
			$.fool.alert({msg:"系统繁忙，请稍后再试。"});
			$("#saveSelected").removeAttr("disabled");
		}
		return true;
	});
}); */

$("#saveSelected").click(function(){
		$('#saveSelected').attr("disabled","disabled");
		$.post('${ctx}/fiscalSubject/saveImport',$('#optionsForm').serialize(),function(data){
			dataDispose(data);	
			if(data.result==0){
				$.fool.alert({time:1000,msg:'引入成功！',fn:function(){
					$('#saveSelected').removeAttr("disabled");
					$('#leadInBox').window('close');
					$('#subjectList').trigger("reloadGrid"); 
					$('#subjectTree').tree('reload');
				}});
			}else if(data.result==1){
				$.fool.alert({msg:data.msg,fn:function(){
					$('#saveSelected').removeAttr("disabled");
					$('#subjectList').datagrid('reload');
					$('#subjectTree').tree('reload');
				}});
			}else{
				$.fool.alert({time:1000,msg:"系统繁忙，请稍后再试。"});
				$("#saveSelected").removeAttr("disabled");
			}
			return true;
		});
});

</script>
</body>
</html>