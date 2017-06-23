<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div class="form1" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${entity.fid}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${entity.updateTime}"/>  
				<p><font><em>*</em>编号：</font><input id="code" type="text" value="${entity.code}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',validType:'normalChar',novalidate:true" /></p>
				<p><font><em>*</em>名称：</font><input id="name" type="text" value="${entity.name}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',validType:'normalChar',novalidate:true" /></p>
				<p><font>开户行：</font><input id="bank" type="text" value="${entity.bank}" class=" easyui-validatebox textBox"  /></p>
				<p><font id="id_"><em></em>账号：</font><input id="account" type="text" value="${entity.account}" class=" easyui-validatebox textBox"/></p><br/>
				<p style="margin-bottom: 30px; display: block;"><font>类型：</font><c:choose>
                  <c:when test="${entity.type =='1'}">
                    <input  type="radio" name="type" checked="checked" value="1"/>现金
                    <input type="radio"  name="type"  value="2"/>银行
	              </c:when>
	              <c:otherwise>
	                <input type="radio" name="type"  value="1"/>现金
                    <input type="radio"  name="type" checked="checked" value="2"/>银行
                  </c:otherwise>
	            </c:choose>
	           </p>
				<p style="margin-left:250px"><font><input id="save" type="button" class="btn-blue2 btn-xs" value="保存"/></font></p> 
              </form>
          </div>

<script type="text/javascript">
$(function(){
	$("input").attr('autocomplete','off');
    $('input[type="radio"]').attr('onclick','rCheck()');
    rCheck();//页面加载时，调用rCheck
});

//radio
function rCheck() {
    var rValue = $("input[type='radio']:checked").val();
    if(rValue == 2){
        $('font#id_').find("em").text("*");
    }else{
        $('font#id_ em').empty("<em>*</em>");
    }
}

 
$('#save').click(function(e) {
	var fid=$("#fid").val();
	var updateTime=$("#updateTime").val();
	var name=$("#name").val();
	var code=$("#code").val();
	var bank=$("#bank").val();
	var type=$("input[type='radio']:checked").val();
	var account=$("#account").val();
	if(account == ""){
	   account == null;
    }

	$('#form').form('enableValidation'); 
		if($('#form').form('validate')){
			$('#save').attr("disabled","disabled");
			    $.post('${ctx}/bankController/save',{fid:fid,updateTime:updateTime,name:name,code:code,bank:bank,account:account,type:type},function(data){
			    	dataDispose(data);
			    	if(data.returnCode=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			if(dhxkey == 1){
		                        selectTab(dhxname,dhxtab);
		                    }
			    			$('#addBox').window('close');
			    			$('#bankList').trigger('reloadGrid');
			    			$('#save').removeAttr("disabled");
			    		}});
			    	}else if(data.result=='1'){
			    		$.fool.alert({msg:data.msg});
			    		$('#save').removeAttr("disabled");
			    	}else{
			    		$.fool.alert({msg:'保存失败，'+data.message});
			    		$('#save').removeAttr("disabled");
			    	}
			    	return true;
			    });
			}else{
				return false;
			}
});
 </script>
</body>
</html>