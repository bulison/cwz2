<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<style>
#myForm p{
	margin:5px auto;
	text-align:center;
}
#myForm{
	text-align:center;
}
</style>
	<div style="margin:10px 10px;">
		<form id="myForm">
			<c:choose>
				<c:when test="${!empty param.fid}">
					<p><font>账号：</font><input id="userCode" name="userCode" class="textBox" /></p>
				</c:when>
				<c:otherwise>
				<br>
				</c:otherwise>
			</c:choose>
				<p><font>密码：</font><input id="password" name="password" class="textBox" /></p>
				<input id="submit" class="btn-blue btn-s" value="提交"/>
		</form>
	</div>
<script>
//控件初始化
if($('#userCode').length > 0){
	$('#userCode').textbox({
		required:true,
		width:160,
		height:30,
		novalidate:true
	});
	$('#userCode').textbox('textbox').keydown(function(e){
		if(e.keyCode == 13){
			$('#password').textbox('textbox').focus();
		}
	});
}
$('#password').textbox({
	required:true,
	width:160,
	height:30,
	type:'password',
	novalidate:true
});
$('#myForm').find('input.textbox-text').eq(0).focus();
$('#password').textbox('textbox').keydown(function(e){
	if(e.keyCode == 13){
		$('#submit').click();
	}
});
$('#submit').click(function(){
	$('#myForm').form('enableValidation');
	if($('#myForm').form('validate')){
		var mydata = $('#myForm').serializeJson();
		if('${param.fid}'!=''){
			eval(${param.callBack}(mydata,'${param.fid}'));
		}else if('${param.type}'!=''){
			if('${param.typeId}'!=''){
				eval(${param.callBack}(mydata.password,'${param.type}','${param.typeId}'));
			}else{
				eval(${param.callBack}(mydata.password,'${param.type}'));
			}
		}
	}
});
</script>
</body>
</html>