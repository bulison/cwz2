<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>单位信息维护</title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/layout.css"/>
<link rel="stylesheet" href="${ctx}/resources/css/comm.css"/>
<script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<style>
h5{ font-size:14px; margin:20px 30px auto 50px; display:inline-block}

.foolTdTitle{
	text-align:right;
	font-size:12px;	
	color:#000000;
}
.foolTdEdit{
	width:238px;
}
.remark_r{color:#F00}
.foolText{
	width:100%;
	height:25px;
	font-size:12px;
	color:#717171;
	border:solid 1px;
}
.foolTextArea{
	height:72px;
	width:100%; 
	vertical-align:text-top;
	resize:none;
	font-size:12px;
	color:#717171;
}
.foolCommit{
	width:86px; 
	height:25px;
	background-color:rgb(27,185,250);
	border:1px solid #999; 
	border-radius:5px; 
	color:#FFF; 	
}
</style>
</head>

<body>

<form id="form" action="${ctx}/orgController/save" method="post">
	<table style="border-collapse:separate; border-spacing:12px;" border="0">
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>单位名称:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空',validType:'normalChar'" style=" border-width:1px" id="orgName" name="orgName" value='${orgName}'/>
			</td>
		</tr>
		
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>邮编:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" name="postCode" data-options="validType:['zip','normalChar']" value="${postCode}"/>				
			</td>
		</tr>
		
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>地址:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空',validType:'length[2,200]'" id="faddress" name="faddress" style=" border-width:1px" value="${address}"/>				
			</td>
		</tr>
		
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>传真:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="validType:['fax','normalChar']" name="fax" value="${fax}"/>				
			</td>
		</tr>
		
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>电子邮件:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空',validType:'email'" style=" border-width:1px" id="email" name="email" value="${email}"/>				
			</td>
		</tr>
			
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>联系电话:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空',validType:'phone'" style=" border-width:1px" id="phoneOne" name="phoneOne" value="${phoneOne}"/>				
			</td>
		</tr>	
		
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>主页:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" validType="url" name="homePage" value="${homePage}"/>				
			</td>
		</tr>	
		
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>描述:</td>
			<td class="foolTdEdit">				
				<textarea class="foolTextArea easyui-validatebox" validType="length[2,200]" name="orgDesc" />${orgDesc}</textarea>			
			</td>
		</tr>
		
		<tr>			
			<td colspan="2" style="text-align:center;">				
				<input type="submit" id="save" value="提交" class="foolCommit"/>			
			</td>
		</tr>					
	</table>     	  
</form>
 

<form id="form" action="${ctx}/orgController/save" method="post">
	<table style="border-collapse:separate; border-spacing:12px;" border="0">
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>单位名称:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空',validType:'normalChar'" style=" border-width:1px" id="orgName" name="orgName" value='${orgName}'/>
			</td>
			
			<td class="foolTdTitle"><span class="remark_r">*</span>邮编:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox"  data-options="validType:['zip','normalChar']" name="postCode" value="${postCode}"/>				
			</td>
		</tr>
						
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>地址:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="required:true,validType:'length[2,200]',novalidate:true,missingMessage:'该项不能为空'" id="faddress" name="faddress" style=" border-width:1px" value="${address}"/>				
			</td>
			
			<td class="foolTdTitle"><span class="remark_r">*</span>传真:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="validType:['fax','normalChar']" name="fax" value="${fax}"/>				
			</td>
		</tr>
						
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>电子邮件:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="required:true,validType:'email',novalidate:true,missingMessage:'该项不能为空'" style=" border-width:1px" id="email" name="email" value="${email}"/>				
			</td>
			<td class="foolTdTitle"><span class="remark_r">*</span>联系电话:</td>
			<td class="foolTdEdit">				
				<input type="text" class="foolText easyui-validatebox" data-options="required:true,validType:'phone',novalidate:true,missingMessage:'该项不能为空'" style=" border-width:1px" id="phoneOne" name="phoneOne" value="${phoneOne}"/>				
			</td>
		</tr>
								
		<tr>
			<td class="foolTdTitle"><span class="remark_r">*</span>主页:</td>
			<td class="foolTdEdit" colspan="3">				
				<input type="text" class="foolText easyui-validatebox" name="homePage" value="${homePage}"/>				
			</td>			
		</tr>					
				
		<tr>			
			<td class="foolTdTitle"><span class="remark_r">*</span>描述:</td>
			<td colspan="3">				
				<textarea class="foolTextArea easyui-validatebox" validType="length[2,200]" name="orgDesc" />${orgDesc}</textarea>			
			</td>
		</tr>	
		
		<tr>			
			<td colspan="4" style="text-align:center;">				
				<input type="submit" id="save" value="提交" class="foolCommit"/>			
			</td>
		</tr>					
	</table>   
	  	  
</form>

<!--esayui验证扩展-->
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/easyui-validate-extend.js?v=${js_v}"></script>
	

<script type="text/javascript">
$('#orgName').bind('blur', function(){
	$('#orgName').validatebox('enableValidation').validatebox('validate');
});
$('#faddress').bind('blur', function(){
	$('#faddress').validatebox('enableValidation').validatebox('validate');
});
$('#email').bind('blur', function(){
	$('#email').validatebox('enableValidation').validatebox('validate');
});
$('#phoneOne').bind('blur', function(){
	$('#phoneOne').validatebox('enableValidation').validatebox('validate');
});

$('#save').click(function(e) {
    $("#orgName").validatebox('enableValidation').validatebox('validate');
    $('#faddress').validatebox('enableValidation').validatebox('validate');
    $('#email').validatebox('enableValidation').validatebox('validate');
    $('#phoneOne').validatebox('enableValidation').validatebox('validate');
	 if($('#form').form('validate')){
			return true;
		}else{return false;}
});
</script>
</body>
</html>