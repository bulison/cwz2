<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
<div id="templateBox">
  <div id="templateGrid"></div>
  <br/><p><input id="savetemp" class="btn-blue2 btn-xs" type="button" value="确认"/></p><br/>
</div>
<script type="text/javascript">
$("#templateGrid").datagrid({
	url:"${ctx}/fiscalSubject/querytemplate",
	fitColumns:true,
	pagination:true,
	singleSelect:true,
	scrollbarSize:0,
	columns:[[
                    {field:'fid',title:'fid',checkbox:true},
                    {field:'code',title:'科目类别',width:100},
                    {field:'name',title:'科目类别',width:100},
	                ]]
});
setPager($('#templateGrid'));

function templateAction(pwd,typeId){
	$.ajax({
		url:'${ctx}/fiscalSubject/saveByTemplateType',
		data:{pwd:pwd,templateTypeId:typeId},
		method:'post',
		beforeSend:function(){
			$.messager.progress({
				text:'努力加载模板中，请稍后...'
			});
		},
		success:function(data){
			$.messager.progress('close');
			dataDispose(data);	
			if(data.result==0){
				$.fool.alert({time:1000,msg:'导入成功！',fn:function(){
					//$('#savetemp').removeProp("disabled");
					$('#tempBox').window('close');
					$('#subjectList').trigger('reloadGrid');
					$('#subjectTree').tree('reload');
				}});
			}else if(data.result==1){
				$.fool.alert({msg:data.msg,fn:function(){
					//$('#savetemp').removeProp("disabled");
					$('#subjectList').trigger('reloadGrid');
					$('#subjectTree').tree('reload');
				}});
			}else{
				$.fool.alert({time:1000,msg:"系统繁忙，请稍后再试。"});
				//$("#savetemp").removeProp("disabled");
			}
			return true;
		}
	});
}

$("#savetemp").click(function(){
	var row=$("#templateGrid").datagrid('getSelected');
	if(!row){
		$.fool.alert({time:2000,msg:"请先选择模板。"});
		return false;
	}else{
		var templateTypeId=row.fid;
	}
	$.fool.confirm({title:'确认',msg:'加载模板将删除现有所有科目，确定要加载模板吗？',fn:function(r){
		if(r){
			$('#mybox').fool('window',{
				top:300,
				left:$(window).width()/2-150,
				modal:true,
				width:300,
				height:150,
				title:"加载模板权限验证",
				href:"${ctx}/indexController/userCheck?callBack=userCheck&type=2&typeId="+templateTypeId
			});
		 }
	}});
});
</script>
</body>
</html>