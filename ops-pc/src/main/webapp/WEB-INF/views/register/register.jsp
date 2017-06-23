<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
    <%@ taglib prefix="fool" uri="/WEB-INF/tld/fool.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册页</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<style>
  label{
    font-size: 12px;
    color:#AAAAAA;
  } 
  #header{
    background-color: white; 
  }
  #content{
    width:1120px;
    height:600px;
    margin: 20px auto;
    background-color: white; 
  }
  #logo_link{
    margin-left: 240px
  }
  #getVCode{
    width:102px;
    height:41px;
    background-color:#8A8A8A;
    border:none;
    border-radius:5px;
    color: #FFFFFF;
    font-size: 15px;
    outline: none;
    margin-left: 18px      
  }
  #submit{
    width:300px; 
    height:41px;
    background-color:#1FA0F1;
    border:none;
    border-radius:5px;
    color: #FFFFFF;
    font-size: 15px;
    outline: none 
  }
  #info{
    text-align: center;
  }
  #registerInfo{
    padding: 60px;
  }
  #registerInfo p{
    margin-bottom: 24px
  }
  #registerInfo p font{
    display:inline-block;
    width:100px
  }
  #registerInfo .textbox{
    border-radius:5px;
    border:none; 
  }
  #registerInfo .textbox-text{
    background-color: #E8E8E8;
    font-size: 15px;
  }
  .validatebox-invalid{
    background-color: #fff3f3 !important;  
  }
</style>
</head>
<body>
<div id="header"><a id="logo_link" href="javascript:;" ><img id="logo" src="${ctx}/resources/images/login_logo.png"/></a></div>
<div id="content">
  <div id="info">
    <div id="registerInfo">
      <form id="form">
        <p><font>机构名称：</font><input id="orgName" name="orgName"/></p>
        <p><font>机构代码：</font><input id="orgAccountNum" name="orgCode"/></p>
        <p><font>手机号：</font><input id="phone" name="phoneOne"/>
        <p><font>用户账号：</font><input id="userCode" name="userCode"/></p>
        <p><font>密码：</font><input id="passWord" name="passWord"/></p>
        <p><font>确认密码：</font><input id="confirm" name="confirm"/></p>
        <p><font></font><input id="submit" type="button" value="注&emsp;册"/></p>
      </form>
    </div>
  </div>
</div>

<script type="text/javascript">
 $(function(){
	  initTextbox($("#phone"),301,42,"   输入手机号","phone","text");
	  initTextbox($("#orgName"),301,42,"   机构名称","isBank","text");
	  initTextbox($("#orgAccountNum"),301,42,"   机构代码","isBank","text");
	  initTextbox($("#userCode"),301,42,"   用户账号","myAccount","text");
	  initTextbox($("#passWord"),301,42,"   密码","","password");
	  initTextbox($("#confirm"),301,42," 确认密码","equals['#passWord']","password");
	  
	  $(".textbox-text").bind({focus:function(){
		  $(this).css("background-color","#FFFFFF");
		  $(this).parent().prev().textbox("enableValidation");
	  },blur:function(){
		  if(!$(this).next().val()){ 
			  $(this).css("background-color","#E8E8E8");
		  } 
	  }});
	  
	  $("#submit").click(function(){ 
		  var orgName=$("#orgName").textbox('getValue');
		  var orgCode=$("#orgAccountNum").textbox('getValue');
		  var userCode=$("#userCode").textbox('getValue');
		  var phone=$("#phone").textbox('getValue');
		  var passWord=$("#passWord").textbox('getValue');
		  $("#form").form('enableValidation');
		  if($("#form").form('validate')){
			  $("#submit").attr("disabled","disabled");
			  $.post("${ctx}/registerController/registerInfo",{passWord:passWord,orgName:orgName,orgCode:orgCode,userCode:userCode,phoneOne:phone},function(data){
				  if(data.returnCode=='0'){
					  $.fool.alert({
						  msg:orgCode+"注册成功！请重新登陆。",
						  fn:function(){
							  location.href="${ctx}";
						  }
					  });
				  }else if(data.returnCode=='1'){
			    		$.fool.alert({time:1000,msg:data.message});
			    		$("#submit").removeAttr("disabled");
				  }else{
					  $.fool.alert({time:1000,msg:'系统正忙，请稍后再试。'});
					  $("#submit").removeAttr("disabled");
				  }
			  });
		  }else{
			  return false;
		  }; 
	  });
	  
  });
 function initTextbox(target,width,height,prompt,validType,type){ 
	  target.textbox({
		  width:width,
		  height:height,
		  required:true,
		  novalidate:true,
		  missingMessage:"该项不能为空",
		  prompt:prompt,
		  validType:validType,
		  type:type
	  });
 }
 $.extend($.fn.validatebox.defaults.rules, {
	 equals: {
		 validator: function(value,param){
			 return value == $(param[0]).textbox("getValue");    
		 },
		 message: '两次输入的密码不一致'   
	 }    
 });
</script>
</body>  
</html>