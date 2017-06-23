<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>
  
<div class="form1">
	<form id="form_Customer" action="" method="post">        
	        <p><font>客户编码：</font><input type="text" id="code" name="code" class="easyui-validatebox textBox ops-val" required="true" validType="normalChar" maxlength="50" missingMessage="客户编码必填!" /></p>
	        <p><font>客户名称：</font><input type="text" class="easyui-validatebox textBox ops-val" id="name" name="name" required="true" validType="normalChar" maxlength="50" missingMessage="客户名称不能为空!"/></p>
     	    <p><font>类别：</font><input type="text" class="easyui-validatebox textBox ops-val" id="category" name="category" required="true" validType="normalChar" maxlength="50" missingMessage="类别必填!"/></p>
	  
	        <p><font>联系人：</font><input type="text" id="contacts" class="easyui-validatebox textBox ops-val" name="contacts" required="true" validType="normalChar" maxlength="50" missingMessage="联系人不能为空!"/></p>
	        <p><font>手机：</font><input type="text" id="phone" class="easyui-validatebox textBox ops-val" name="phone" required="true" validType="phone" maxlength="50" missingMessage="手机不能为空!" /></p>
	        <p><font>固定电话：</font><input type="text" class="easyui-validatebox textBox" id="areaCode" style="width:40px"/> - <input type="text" id="tel" class="easyui-validatebox textBox" validType="tel" maxlength="50" style="width:60px"/> - <input type="text" id="extension" class="easyui-validatebox textBox" style="width:40px"/></p>
	        <input type="hidden" name="tel" id="telNum"/>
	    
	      	<p><font>传真：</font><input type="text" id="fax"  class="easyui-validatebox textBox" name="fax"  validType="fax" maxlength="50" /></p>
	      	<p><font>邮箱：</font><input type="text" id="email" name="email" class="easyui-validatebox textBox ops-val"  required="true" validType="email" maxlength="50"  maxmissingMessage="邮箱不能为空" /></p>
	   		<p><font>地址：</font><input type="text" id="address" class="easyui-validatebox textBox ops-val"  name="address" validType="length[6,200]" required="true" maxlength="200" missingMessage="客户地址不能为空!"/></p>
	
	      	<p><font>描述：</font><textarea name="describe" id="describe" class="textArea easyui-validatebox" validType="length[6,200]" maxlength="200" ></textarea></p><br/>
	    	<p style="margin-left:43px"><font><a href="javascript:;" id="save" class="btn-blue4 btn-s">保存</a></font></p>
	</form>
</div>


<script type="text/javascript" src="${ctx}/resources/js/cashflow.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/easyui-validate-extend.js?v=${js_v}"></script>
<script type="text/javascript">
$(function(){
	//保存 
	$('#save').click(function(){
		    $('#form_Customer').form('enableValidation');
			var isValid = $('#form_Customer').form('validate');
			if(!isValid){
				$.fool.alert({msg:'数据验证不通过,不能保存！'});
			}
			else{
				$.ajax({
					type:'post',
					url:'${ctx}/customerController/save',
					cache:false ,
					data:$('#form_Customer').serialize(),
					dataType:'json',
					success:function(data){
						if(data == 1){
							$.fool.alert({
								msg:"保存成功！",
								fn:function(data){
									 if("${param.okCallBack}".length>0){
										    eval(${param.okCallBack}(data));  
									    }
									 if("${param.reloadCallBack}".length>0){
										    eval(${param.reloadCallBack}(data));   
									    }
								}
							});
						}
					}
				});
			}
	});
	$('#back').click(function(){
		location.href ='${ctx}/customerController/listPage';
	});
	
	$('#areaCode').change(function(e) {
        $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
    });
	$('#tel').change(function(e) {
        $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
    });
	$('#extension').change(function(e) {
        $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
    });
});
</script>
</body>
</html>