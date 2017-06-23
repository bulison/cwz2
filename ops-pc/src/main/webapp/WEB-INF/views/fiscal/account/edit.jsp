<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
</head>
<body>
  <div class="form" >
  <form id="form">
    <input id="fid" name="fid" type="hidden" value="${obj.fid}"/>
    <p><font><em>*</em>账套名：</font><input id="name" name="name" class="textBox"/></p>
    <p><font><em>*</em>状态：</font><input id="enable" name="enable" class="textBox"/></p>
    <p><font>描述：</font><input id="description" name="description" class="textBox"/></p>
    <p><font></font><input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /></p>
  </form>
  </div>

<script type="text/javascript">
  //表单控件初始化
  $("#name").textbox({
	  required:true,
	  novalidate:true,
	  validType:'isBank',
	  missingMessage:'该项不能为空！',
	  width:160,
	  height:31,
	  validType:['isBank','maxLength[50]'],
	  value:"${obj.name}"
  });
  $("#enable").combobox({
	  required:true,
	  novalidate:true,
	  missingMessage:'该项不能为空！',
	  width:160,
	  height:31,
	  data:[{value:0,text:"不能修改数据"},{value:1,text:"可以修改数据"}],
	  editable:false,
	  validType:'isBank',
	  onLoadSuccess:function(data){
		  if("${obj.enable}"){
			  $("#enable").combobox('setValue',"${obj.enable}");
		  }else{
			  $("#enable").combobox('setValue',0);
		  }
	  }
  });
  $("#description").textbox({
	  width:160,
	  height:31,
	  value:"${obj.description}",
	  validType:'maxLength[200]'
  });
  
  //保存按钮点击事件
  $('#save').click(function(e) {
	  $('#form').form('enableValidation');
	  if($('#form').form('validate')){
		  $("#save").attr("disabled","disabled");
		  $.post('${ctx}/fiscalAccount/save',$('#form').serialize(),function(data){
			  dataDispose(data);
			  if(data.result=='0'){
				  $.fool.alert({time:1000,msg:'保存成功！',fn:function(){
					  $('#addBox').window('close');
		    		  $("#save").removeAttr("disabled");
		    		  $('#accountList').trigger("reloadGrid"); 
				  }});
			  }else if(data.result=='1'){
				  $.fool.alert({msg:data.msg});
		    	  $("#save").removeAttr("disabled");
			  }else{
				  $.fool.alert({time:1000,msg:"系统繁忙，请稍后再试。"});
		    	  $("#save").removeAttr("disabled");
			  }
		    	return true;
		  });
	  }else{
			return false;
	  }
  });
  enterController('form');
</script>
</body>
</html>