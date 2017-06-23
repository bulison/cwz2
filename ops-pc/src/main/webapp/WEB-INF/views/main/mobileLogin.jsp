<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/WEB-INF/views/common/header.jsp" %>
	<title>现金流管理系统-移动端登录测试A</title>
	<link rel="stylesheet" href="${ctx}/resources/css/login.css?v=0.1"></link>
	<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/icon.css"></link>
	
	 <script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/gever/gdp/js/encrypt/encrypt.js"></script>
	<style >
	  .site{color:black;} 
	  .site:hover{color:#FE9901;} 
	</style>
</head>

<body>

	<div class="container">
	    <!--nav top header begin-->
	    <nav class="header">
	        <div class="header-left">
	            <img src="${ctx}/resources/images/login_logo.png" />
	        </div>
	        <div class="header-right">
	        </div>
	    </nav>
	    <!--nav top header end-->
	
	    <!--context begin-->
	    <div class="context" >
	        <img src="${ctx}/resources/images/bg_pic.jpg" style="float: left;"/><!--context bgimg-->
	        <div class="context-left"></div><!--context left div-->
	        <!--context silder begin-->
	        <div class="context-sider">
	            <img src="${ctx}/resources/images/login_bg1.png"><!--bgimg-->
	            <!--login begin-->
	            <div class="context-sider-login">
	                <form action="${ctx}/login/login" method="post" onsubmit="return submitForm();">
	                    <table cellpadding="5">
	                        <thead>
	                            <tr>
	                                <th colspan="4" align="left"><h2 style="color: #666666">会员登录</h2></th>
	                            </tr>
	                        </thead>
	                        <tbody>
	                        	<c:if test="${not empty(errorMsg)}">
	                        	<tr class="msg">
							    	<td colspan="4" style="color:red;">${errorMsg}</td>
	                        	</tr>
							    </c:if>
		                        <tr>
		                            <td class="from-lable">用户名:</td>
		                            <td colspan="3"><input class="easyui-textbox easyui-validatebox" type="text" id="userCode" name="userCode" style="width: 222px;  height: 35px;" data-options="prompt:'账号、邮箱',iconCls:'icon-man',iconWidth:38,iconAlign:'left'"></td>
		                        </tr>
		                        <tr>
		                            <td class="from-lable">密码:</td>
		                            <td colspan="3"><input class="easyui-textbox easyui-validatebox" type="password" id="password" name="password" style="width: 222px;  height: 35px;" data-options="prompt:'请输入密码',iconCls:'icon-lock',iconWidth:38,iconAlign:'left'"></td>
		                        </tr>
		                        <tr>
		                            <td class="from-lable">验证码:</td>
		                            <td colspan="3">
		                                <input class="easyui-textbox easyui-validatebox" name="validateCode" data-options="prompt:'请输入验证码',iconWidth:38,iconAlign:'left'" style="width:45%; min-width: 120px; height: 35px;">
		                                <img src="${ctx}/ValidateCode" onclick="changeValidateCode(this);" style="height: 35px; width: 16%; min-width:88px; min-height:35px; padding-left: 10px;">
		                            </td>
		                        </tr>
		                        <tr>
		                            <td align="right">
		                            	<input type="checkbox" id="rememberme" name="rememberme" class="remember-user">
		                            </td>
	                                <td align="left">记住用户名</td>
		                            <td colspan="2">忘记密码&nbsp;|&nbsp;<a href="${ctx}/registerController/registerPage" ><font color="#fc9b00">免费注册</font></a> </td> 
		                        </tr>
		                        <tr>
		                            <td colspan="4">
		                                 <button type="submit" style="background:url('${ctx}/resources/images/login_btn.png') no-repeat center;border:none; width: 85%; height: 39px; min-width: 283px;min-height: 40px;  cursor:pointer;" ></button>
		                            </td>
		                        </tr>
							</tbody>
	                    </table>
	                </form>
	            </div><!--login end-->
	        </div><!--context silder end-->
	    </div><!--context end-->
	    
	    <div class="footer">
	        <div class="footer-center" align="center"> 
	           <p>
	               <span><a class="site" target="_blank" href="${ctx}/aboutController/about?site=1">关于我们</a></span> | <span><a class="site" target="_blank" href="${ctx}/aboutController/about?site=2">联系我们</a></span> | <span><a class="site" target="_blank" href="${ctx}/aboutController/about?site=3">人才招聘</a></span> | <span><a class="site" target="_blank" href="${ctx}/aboutController/about?site=4">活动报道</a></span>
	           </p> 
	           <p>
	               Copyright©2013-2015 fooltech.cn版权所有
	           </p>
	        </div>
	    </div>
	</div>

<script>
//非正常退出系统时，清除单据缓存
var billCodes=["base","qckc","cgdd","cgrk","cgth","cgxjd","cgsqd","cgfp","pdd","dcd","bsd","scll","sctl","cprk","cptk","xsdd","xsch","xsth","xsfp","xsbjd","scjhd","fysqd","cgfld","xsfld","skd","fkd","fyd","bom"];
for(var i=0;i<billCodes.length;i++){
	if(localStorage[billCodes[i]]){
		localStorage.removeItem(billCodes[i]);
	}
	if(localStorage['myMean']){
		localStorage.removeItem('myMean');
	}
}
if(localStorage.userCode){
	$("#userCode").val(localStorage.userCode);
}

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
	if(document.getElementById("rememberme").checked){
		localStorage.userCode=$("#userCode").val();		
	}	
	return true;
}

function changeValidateCode(target) {
	$(target).attr('src', '${ctx}/ValidateCode?rnd=' + new Date().getTime());
}
</script>
</body>
</html>