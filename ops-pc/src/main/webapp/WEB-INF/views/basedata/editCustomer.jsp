<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<style>
</style>
</head>

<body>
	<div class="form1">
	 	<form id="form" action="" method="post">
		        <p><font>客户编码：</font><input type="text" id="code" name="code" class="easyui-validatebox textBox" data-options="required:true,validType:'normalChar',missingMessage:'客户编码必填!'" maxlength="50" value="${customer.code}"/></p>
		        <p><font>客户名称：</font><input type="text" class="easyui-validatebox textBox" id="name" name="name" data-options="required:true,validType:'normalChar',missingMessage:'客户名称不能为空!'"  maxlength="50" value="${customer.name}"/></p>
	     	    <p><font>类别：</font><input type="text" class="easyui-validatebox textBox" id="category" name="category" data-options="required:true,validType:'normalChar',missingMessage:'类别必填!'"  maxlength="50" value="${customer.category}"/></p>
		        <p><font>联系人：</font><input type="text" id="contacts" class="easyui-validatebox textBox" name="contacts" data-options="required:true,validType:'normalChar',missingMessage:'联系人不能为空!'"  maxlength="50" value="${customer.contacts}"/></p>
		        <p><font>手机：</font><input type="text" id="phone" class="easyui-validatebox textBox" name="phone" data-options="required:true,validType:'phone',missingMessage:'手机号码不能为空!'" maxlength="50" value="${customer.phone}"/></p>
		        <p><font>固定电话：</font><input type="text" class="easyui-validatebox textBox" id="areaCode" style="width:40px"/> - <input type="text" id="tel" class="easyui-validatebox textBox" data-options="validType:'tel'"  maxlength="50" style="width:60px" /> - <input type="text" id="extension" class="easyui-validatebox textBox" style="width:40px"/></p>
		        <input type="hidden" name="tel" id="telNum"/>
		      	<p><font>传真：</font><input type="text" id="fax"  class="easyui-validatebox textBox" name="fax" data-options="validType:['fax','normalChar']"  maxlength="50" value="${customer.fax}"/></p>
		      	<p><font>邮箱：</font><input type="text" id="email" name="email" class="easyui-validatebox textBox" validtype="email" data-options="required:true,validType:'email',missingMessage:'邮箱不能为空'"   maxlength="50" value="${customer.email}"/></p>
		   		<p><font>地址：</font><input type="text" id="address" class="easyui-validatebox textBox"  name="address" data-options="required:true,validType:'length[6,200]',missingMessage:'客户地址不能为空!'"  maxlength="200" value="${customer.address}"/></p>
		      	<p><font>描述：</font><textarea name="describe" id="describe" data-options="validType:'length[6,200]'" maxlength="200" class="textArea easyui-validatebox">${customer.describe}</textarea></p><br/>
		    	<p style="margin-left:43px"><font><a href="javascript:;" id="save" class="btn-blue4 btn-s" >保存</a></font></p>
		       	<p><input type="hidden" name="fid"  value="${customer.fid}"/></p>
		 </form>
	 </div>
	 

<script type="text/javascript" src="${ctx}/resources/js/base.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/cashflow.js?v=${js_v}"></script>
<script type="text/javascript">
$(function(){
	
	var str='${customer.tel}'.split("-");
    $('#areaCode').val(str[0]);
    $('#tel').val(str[1]);
    $('#extension').val(str[2]);
	
	$('#areaCode').change(function(e) {
        $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
    });
	$('#tel').change(function(e) {
        $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
    });
	$('#extension').change(function(e) {
        $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
    });
	
	$.extend($.fn.validatebox.defaults.rules, {
	    //移动手机号码验证
	    mobile: {//value值为文本框中的值
	        validator: function (value) {
	            var reg = /^1[3|4|5|8|9]\d{9}$/;
	            return reg.test(value);
	        },
	        message: '输入手机号码格式不准确.'
	    },
	});
	//保存 
	$('#save').click(function(){
			var isValid = $('form').form('validate');
			if(!isValid){
				$.fool.alert({msg:'数据验证不通过,不能保存！'});
			}
			else{
				$.ajax({
					type:'post',
					url:'${ctx}/customerController/save',
					cache:false ,
					data:$('form').serialize(),
					dataType:'json',
					success:function(data){
						if(data == 1){
							$.fool.alert({msg:'修改成功!',fn:function(){
								location.href ='${ctx}/customerController/listPage';
							}});
						}
					}
					
				});
			}
	});
	$('#back').click(function(){
		location.href ='${ctx}/customerController/listPage';
	});
});
</script>
</body>
</html>