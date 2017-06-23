<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>客户信息详情</title>
<style>
</style>
</head>
<body> 
  <div class="form1">
	        <p><font>客户编码：</font><input type="text" id="code" name="code" class="easyui-validatebox textBox" required="true" missingMessage="客户编码必填!" value="${customer.code}" disabled="disabled"/></p>
	        <p><font>客户名称：</font><input type="text" class="easyui-validatebox textBox" id="name" name="name" required="true"  missingMessage="客户名称不能为空!" value="${customer.name}" disabled="disabled"/></p>
     	    <p><font>类别：</font><input type="text" class="easyui-validatebox textBox" id="category" name="category" required="true" missingMessage="类别必填!" value="${customer.category}" disabled="disabled"/></p>
	        <p><font>联系人：</font><input type="text" id="contacts" class="easyui-validatebox textBox" name="contacts" required="true" missingMessage="联系人不能为空!" value="${customer.contacts}" disabled="disabled"/></p>
	        <p><font>手机：</font><input type="text" id="phone" class="easyui-validatebox textBox" name="phone" required="true" validtype="mobile" value="${customer.phone}" disabled="disabled"/></p>
	        <p><font>固定电话：</font><input type="text" class="easyui-validatebox textBox" id="areaCode" style="width:40px" disabled="disabled"/> - <input type="text" id="tel" class="easyui-validatebox textBox" disabled="disabled" style="width:60px"/> - <input type="text" id="extension" class="easyui-validatebox textBox" style="width:40px" disabled="disabled"/></p>
	      	<p><font>传真：</font><input type="text" id="fax"  class="easyui-validatebox textBox" name="fax" value="${customer.fax}" disabled="disabled"/></p>
	      	<p><font>邮箱：</font><input type="text" id="email" name="email" class="easyui-validatebox textBox" validtype="email" required="true" missingMessage="邮箱不能为空" invalidMessage="邮箱格式不正确" value="${customer.email}" disabled="disabled"/></p>
	   		<p><font>地址：</font><input type="text" id="address" class="easyui-validatebox textBox"  name="address"  required="true" missingMessage="客户地址不能为空!" value="${customer.address}" disabled="disabled"/></p>
	      	<p><font>描述：</font><textarea name="describe" id="describe" disabled="disabled" class="textArea">${customer.describe}</textarea></p>
   </div>
   

   <script type="text/javascript">
   $(function(){
	   var str='${customer.tel}'.split("-");
	     $('#areaCode').val(str[0]);
	     $('#tel').val(str[1]);
	     $('#extension').val(str[2]);
   });
   </script>
</body>
</html>