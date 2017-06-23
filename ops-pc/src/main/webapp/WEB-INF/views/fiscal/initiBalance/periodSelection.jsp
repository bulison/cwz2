<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
<div id="optionsBox">
  <form id="periodForm">
  <p><input id="bank" name="bank" type="checkbox" value="1"/> 银行</p>
  <p><input id="supplier" name="supplier" type="checkbox" value="1"/> 供应商</p>
  <p><input id="customer" name="customer" type="checkbox" value="1"/> 客户</p>
  </form>
  <p><input id="saveSelected" class="btn-blue btn-s" type="button" value="保存"/></p> 
</div>
<script type="text/javascript">
//保存按钮点击事件
$("#saveSelected").click(function(){
	$('#saveSelected').attr("disabled","disabled");
	$.post('',$('#periodForm').serialize(),function(data){
		dataDispose(data);
		if(data.returnCode=='0'){
			$.fool.alert({time:1000,msg:'引入成功！',fn:function(){
				$('#saveSelected').removeAttr("disabled");
				$('#periodBox').window('close');
				$('#subjectList').datagrid('reload');
			}});
		}else if(data.returnCode=='1'){
			$.fool.alert({msg:data.message,fn:function(){
				$('#saveSelected').removeAttr("disabled");
				$('#subjectList').datagrid('reload');
			}});
		}else{
			$.fool.alert({time:1000,msg:"系统繁忙，请稍后再试。"});
			$("#saveSelected").removeAttr("disabled");
		}
		return true;
	});
});
</script>
</body>
</html>