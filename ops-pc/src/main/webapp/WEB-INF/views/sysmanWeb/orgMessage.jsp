<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>单位信息维护</title>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>单位信息维护</h1>
                </div>             
             </div>
	<%-- <div class="nav-box">
			<ul>
		    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
		        <li><a href="#">系统维护</a></li>
		        <li><a href="#" class="curr">单位信息维护</a></li>
		    </ul>
	</div> --%>
<!-- 
<div style="text-align:center; margin-top:20px">
<form id="form" action="${ctx}/orgController/save" method="post">
  <h5>*单位名称</h5><input type="text" class="easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空'" style=" border-width:1px" id="orgName" name="orgName" value='${orgName}'/>
  <br/>
  <h5 style=" margin-left:85px">邮编</h5><input type="text" class="easyui-validatebox" name="postCode" value="${postCode}"/>
  <br/>
  <h5 style=" margin-left:78px;">*地址</h5><input type="text" class="easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空'" id="faddress" name="faddress" style=" border-width:1px" value="${address}"/>
  <br/>
  <h5 style=" margin-left:85px">传真</h5><input type="text" class="easyui-validatebox" name="fax" value="${fax}"/>
  <br/>
  <h5>*电子邮件</h5><input type="text" class="easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空'" style=" border-width:1px" id="email" name="email" value="${email}"/>
  <br/>
  <h5>*联系电话</h5><input type="text" class="easyui-validatebox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空'" style=" border-width:1px" id="phoneOne" name="phoneOne" value="${phoneOne}"/>
  <br/>
  <h5 style=" margin-left:85px">主页</h5><input type="text" class="easyui-validatebox" name="homePage" value="${homePage}"/>
  <br/>
  <h5 style=" margin-left:85px">描述</h5><textarea  style="height:50px;width:173px; vertical-align:text-top;resize:none" name="orgDesc" />${orgDesc}</textarea>
  <br/>
  <input type="submit" id="save" value="提交" style="width:92px; height:25px;background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF; margin:20px 0px 0px 143px"/>
</form>
</div>
 -->
 <div class="form">
 <form id="form" action="${ctx}/orgController/save" method="post">
			<p><font>单位名称：</font><input type="text" class="easyui-validatebox textBox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空',validType:['normalChar','maxLength[50]']" id="orgName" name="orgName" value='${orgName}'/></p>
			<p><font>邮编：</font><input type="text" class="easyui-validatebox textBox" validType="zip" name="postCode" value="${postCode}"/></p>
			<p><font>地址：</font><input type="text" class="easyui-validatebox textBox" data-options="required:true,novalidate:true,missingMessage:'该项不能为空',validType:'length[2,200]'" id="faddress" name="faddress"  value="${address}"/></p>
			<p><font>传真：</font><input type="text" class="easyui-validatebox textBox" data-options="validType:['fax','maxLength[50]']" name="fax" value="${fax}"/></p>
			<p><font>电子邮件：</font><input type="text" class="easyui-validatebox textBox" data-options="validType:['email','maxLength[50]'],required:true,novalidate:true,missingMessage:'该项不能为空'"  id="email" name="email" value="${email}"/></p>
			<p><font>联系电话：</font><input type="text" class="easyui-validatebox textBox" data-options="validType:'tel',required:true,novalidate:true,missingMessage:'该项不能为空'"  id="phoneOne" name="phoneOne" value="${phoneOne}"/></p>
			<p><font>主页：</font><input type="text" class="easyui-validatebox textBox" validType="url" name="homePage" value="${homePage}"/></p>
			<p><font>描述：</font><textarea class="easyui-validatebox textArea" validType="length[0,200]" name="orgDesc" >${orgDesc}</textarea></p>
			<p style="margin-left:60px"><font>	
				<fool:tagOpt optCode="UnitInfoChange">
					<a href="javascript:;" id="save"  class="btn-blue4 btn-s">提交</a>
				</fool:tagOpt>			
			</font></p>
</form>
</div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$(function(){
	 $('#form').form({
		 success:function(){
			 $.fool.alert({msg:"修改成功！",fn:function(){
				 location.reload(true);
			 }});
			 
		 }
	 });
});
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
		    $('#form').form('submit');
			return true;
		}else{return false;}
});
</script>
