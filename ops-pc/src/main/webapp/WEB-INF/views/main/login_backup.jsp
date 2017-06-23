<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>现金流管理系统-登录</title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"></link>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/icon.css"></link>
<link rel="stylesheet" href="${ctx}/resources/css/base.css"></link>
 <script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<script language="javascript" src="${ctx}/resources/gever/gdp/js/encrypt/encrypt.js"></script>
<style>
html,body{
	background:#F5F6FA;
}
.login-box{
	/*border:1px solid #D8DCE5;
	background-color:#ffffff;*/
	background:url(${ctx}/resources/images/login_bg.png) no-repeat;
	width:727px;
	height:398px;
	margin:70px auto 0px auto;	
}
.login-box .left{
	float:left;
	width:350px;
	text-align:center;
	padding-top:60px;
}
.login-box .left h1{
	font-size:16px;
	font-weight:bold;
	color:#4F2719;
	text-align:center;
	margin-bottom:20px;
}
.login-box .right{
	float:left;
	padding-top:70px;
	margin-left:50px;
}
.login-box .right h1{
	font-size:14px;
	color:#5B5B5B;	
}
.login-box .right .textbox{
	border:1px solid #D7D7D7;
	border-radius:0px;
}
.login-btn{
	background:url(${ctx}/resources/images/sprites_btn.png) no-repeat;
	width:100px;
	height:38px;
	border:none;
	cursor:pointer;
	color:#ffffff;
	font-weight:bold;
}
.copyright{
	text-align:center;
	margin-top:40px;
}
</style>
</head>

<body>

<div class="header">
<div class="left">
<div class="logo">
<a href="#"><img src="${ctx}/resources/images/logo.png" alt="${system_name}" title="${system_name}"/></a>
</div>
</div>
<div class="right">
<ul class="header-nav">
	<li><a href="#">人工服务</a></li>
	<li><a href="#">帮助中心</a></li>    
</ul>
<ul class="header-news">
	<li><a href="#">IPAD移动客户端0.9版本正式上线</a></li>
    <li><a href="#">IPAD移动客户端0.9版本正式上线</a></li>
</ul>
</div>
</div>

<div class="login-box">
	<div class="left">
    	<h1>下载&nbsp;ipad&nbsp;现金流管理系统</h1>
        <img src="${ctx}/resources/images/qcode.png" width="200" height="200"/>
    </div>
    <div class="right">
    	<form action="${ctx}/login/login" method="post" onsubmit="return submitForm();">
    	<h1>账户登录</h1><br/>
    	<c:if test="${not empty(errorMsg)}">
    	&nbsp;&nbsp;&nbsp;<div style="color:red;">${errorMsg}</div><br/>
    	</c:if>
        <input class="easyui-textbox easyui-validatebox" name="userCode" id="userCode" value="xjh" style="width:280px;height:35px;" data-options="prompt:'账号、邮箱',iconCls:'icon-man',iconWidth:38,iconAlign:'left',required:true,missingMessage:'请输入账户、邮箱'"><br/><br/>
		 <input class="easyui-textbox" type="password" name="password" id="password" value="123456" style="width:100%;height:35px;" data-options="prompt:'请输入密码',iconCls:'icon-lock',iconWidth:38,iconAlign:'left',required:true,missingMessage:'请输入密码'"><br/><br/>       
		 <input class="easyui-textbox" name="validateCode" id="validateCode" style="width:60%;height:35px;vertical-align:middle;" data-options="prompt:'请输入验证码',iconCls:'icon-lock',iconWidth:38,iconAlign:'left',required:true,missingMessage:'请输入验证码',tipPosition:'left'">
		 <img border="0" style="width:30%;height:35px;vertical-align:middle;" id="validateCodeIMG" hspace="5" src="${pageContext.request.contextPath}/ValidateCode" alt="看不清？点击更换" onclick="changeValidateCode();" style="cursor: pointer; float: right;"></img>
		 <br/><br/>       
          <input type="checkbox" id="rememberme" name="rememberme" style="vertical-align:text-bottom;">
          <label class="" for="rememberme">记住密码</label>
          <a href="#" style="float:right;">忘记密码？</a>
          <br/><br/>
          <input type="submit" value="登录" class="login-btn"/>
          </form>
    </div>
</div>

<div class="copyright">
	${company_name}&nbsp;版权所有<br/>
    ${copyright}
</div>
</body>
</html>
<script>
function submitForm() {
	var $usercode = $("#userCode"),
	$password = $("#password");
	
	if(!$usercode.textbox('isValid')){
		$usercode.textbox('textbox').focus();
		return false;
	}
	if(!$password.textbox('isValid')){
		$password.textbox('textbox').focus();
		return false;
	}else{
		$password.textbox('setValue',Base64.encode($password.textbox('getValue')));
	}
	
	return true;
}

function changeValidateCode() {
	$(this).attr('src', '${pageContext.request.contextPath}/ValidateCode?rnd=' + new Date().getTime());
}
</script>