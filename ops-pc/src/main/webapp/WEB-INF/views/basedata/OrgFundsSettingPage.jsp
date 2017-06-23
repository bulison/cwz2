<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>企业管理</title>
<style>

</style>
</head>
<body>
 <div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">基础管理</a></li>
	        <li><a href="javascript:;" class="curr">企业管理</a></li>
	    </ul>
	</div>

<div id="body">
<div id="forms" class="form">
  <form id="form" action="${ctx }/OrgMsgSettingController/saveSetting " method="get">
  			<p><font>余额：</font><input class="easyui-validatebox textBox" data-options="validType:'intOrFloat'" id="balance" name="balance"  value="${orgFunds.balance}"/></p>
  			<p><font>净资金：</font><input class="easyui-validatebox textBox" data-options="validType:'intOrFloat'" id="usableBalance" name="usableBalance" value="${orgFunds.usableBalance}"/>	</p>
  			<p><font>借出金额：</font><input class="easyui-validatebox textBox" data-options="validType:'intOrFloat'" id="lendMoney" name="lendMoney" value="${orgFunds.lendMoney}"/>	</p>
  			<p><font>借入金额：</font><input class="easyui-validatebox textBox" data-options="validType:'intOrFloat'" id="borrowMoney" name="borrowMoney" value="${orgFunds.borrowMoney}"/></p>
  			<p><font>月固定收入：</font><input class="easyui-validatebox textBox" data-options="validType:'intOrFloat'" id="fixedIncome" name="fixedIncome" value="${orgFunds.fixedIncome}"/></p>
  			<p><font>月固定支出：</font><input class="easyui-validatebox textBox" data-options="validType:'intOrFloat'" id="fixedExpend" name="fixedExpend" value="${orgFunds.fixedExpend}"/></p>
  			<p class="btn">
  				<fool:tagOpt optCode="orgfundEdit"><a href="javascript:;" class="btn-blue4 btn-s" id="alter" >修改</a></fool:tagOpt>
  				
  				<a href="javascript:;" type="submit" class="btn-blue4 btn-s"  id="save" style="display:none">保存</a>
  				
  				<a href="javascript:;" type="button" class="btn-blue4 btn-s"  id="cancel" style="display:none">取消</a>
  			</p>
  			
  			
  			
  </form>
</div>

<!-- <div id="btn" style="text-align:center">
  <div id="alt">
  <form>
    <a class="easyui-linkbutton btn-blue btn-s" id="alter" >修改</a>
  </form>
  </div>
</div>
 -->
</div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$(document).ready(function(e) {
    $("#form :input").attr("readonly", "readonly");
    $(".easyui-validatebox").css("background-color","rgb(209,217,224)");
    $('#form').form({
    	success:function(){
    		location.reload();
    	}
    });
});
 
$("#alter").click(function(e){
	$("#form :input").removeAttr("readonly");
	$(this).hide();
	$("#save").fadeIn();
	$("#cancel").fadeIn();
	$(".easyui-validatebox").css("background-color","");
});

$("#save").click(function(e){
	 if($('#form').form('validate')){
		 $('#form').form('submit');
		}else{return false;}
});

$("#cancel").click(function(e){
	location.reload();
});




$("#balance").change(function(e){
	if(!$("#balance").validatebox("isValid")){
		$("#save").attr({"disabled":"disabled"});
		$("#save").css("background-color","#CCC");
	}else{
		$("#save").removeAttr("disabled");
		$("#save").css("background-color","rgb(74,177,235)");
	}
});

$("#usableBalance").change(function(e){
	if(!$("#usableBalance").validatebox("isValid")){
		$("#save").attr({"disabled":"disabled"});
		$("#save").css("background-color","#CCC");
	}else{
		$("#save").removeAttr("disabled");
		$("#save").css("background-color","rgb(74,177,235)");
	}
});

$("#lendMoney").change(function(e){
	if(!$("#lendMoney").validatebox("isValid")){
		$("#save").attr({"disabled":"disabled"});
		$("#save").css("background-color","#CCC");
	}else{
		$("#save").removeAttr("disabled");
		$("#save").css("background-color","rgb(74,177,235)");
	}
});

$("#borrowMoney").change(function(e){
	if(!$("#borrowMoney").validatebox("isValid")){
		$("#save").attr({"disabled":"disabled"});
		$("#save").css("background-color","#CCC");
	}else{
		$("#save").removeAttr("disabled");
		$("#save").css("background-color","rgb(74,177,235)");
	}
});

$("#fixedIncome").change(function(e){
	if(!$("#fixedIncome").validatebox("isValid")){
		$("#save").attr({"disabled":"disabled"});
		$("#save").css("background-color","#CCC");
	}else{
		$("#save").removeAttr("disabled");
		$("#save").css("background-color","rgb(74,177,235)");
	}
});

$("#fixedExpend").change(function(e){
	if(!$("#fixedExpend").validatebox("isValid")){
		$("#save").attr({"disabled":"disabled"});
		$("#save").css("background-color","#CCC");
	}else{
		$("#save").removeAttr("disabled");
		$("#save").css("background-color","rgb(74,177,235)");
	}
});

</script>
