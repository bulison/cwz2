<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head> 
<body>
<div class="form1">
      <input type="hidden" id="fid" value="${labelId}"/>
      <p><font>标签名称：</font><input type="text" class="easyui-validatebox textBox" data-options="required:true,novalidate:true,missingMessage:'请输入标签名称'" id="labelName" value="${labelName}"/></p>
      <p><input class="btn-s2 btn-blue" id="save" type="button" value="保存" /></p> 
</div>


<script type="text/javascript">
$(function(){
	$("#save").click(function(){
		$("#labelName").validatebox('enableValidation');
		if($("#labelName").validatebox('isValid')){
			$.post("${ctx}/labelController/saveOrUpdate",{name:$("#labelName").val(),fid:$("#fid").val()},function(data){
				if(data==0){
					$.fool.alert({
						msg:"保存失败！标签名已存在。"
					});
				}else if(data==1){
					$.fool.alert({
						msg:"保存成功！",
						fn:function(){
							$('#labelTree').tree('reload');
							$("#addLabel").window('close');  
						}
					});
				}
			});
		}else{
			$.fool.alert({
				msg:"请输入标签名。",
				fn:function(){
					return false;
				}
			});
		}
	});
});
</script>
</body>
</html>