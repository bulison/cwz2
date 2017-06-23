<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增、修改报表类型</title>
</head>
<body>
     <div class="form1">
	     <form id="form">
	         <input id="parentId" name="parentId" type="hidden" value="${vo.parentId}"/>
	         <p><font><em>*</em>fid：</font><input id="fid" name="fid" value="${vo.fid}" class="textBox" disabled="disabled"/></p>
	         <p><font>统计配置信息：</font><input id="countInfo" name="countInfo" value="${vo.countInfo}" class="textBox" disabled="disabled"/></p><br/>
             <p><font><em>*</em>报表名称：</font><input id="reportName" name="reportName" class="textBox" value="${vo.reportName}" disabled="disabled"/></p>
             <p><font><em>*</em>汇总方式编号：</font><input id="code" name="code" class="textBox" value="${vo.code}" /></p><br/>
             <p><font><em>*</em>是否分页：</font><input id="showPage" name="showPage" class="textBox" value="${vo.showPage}" disabled="disabled"/></p><br/>
             <p><font><em>*</em>列表标题：</font><textarea id="headers" name="headers" disabled="disabled">${vo.headers}</textarea></p>
             <p><font>javaScript：</font><textarea id="javaScript" name="javaScript" class="textBox" disabled="disabled">${vo.javaScript}</textarea></p><br/>
	         <p style="margin-left:95px;display: none" id="saveNCancel"><font><input id="save" type="button" class="btn-blue4 btn-s" value="保存" /> <input id="cancel" type="button" class="btn-blue4 btn-s" value="取消" /></font></p>
	     </form>

    </div>	
<script type="text/javascript">	
  $("#reportName").validatebox({
	  required:true,
	  novalidate:true,
	  missingMessage:"该项不能为空。"
  });
  $("#code").numberbox({
	  required:true,
	  novalidate:true,
	  missingMessage:"该项不能为空。",
	  width:164,
	  height:31,
	  disabled:true
  });
  $("#headers").validatebox({
	  required:true,
	  novalidate:true,
	  missingMessage:"该项不能为空。"
  });
  $("#showPage").fool("dhxCombo",{
	  required:true,
	  novalidate:true,
	  width:164,
	  height:31,
	  missingMessage:"该项不能为空。",
	  setTemplate:{
		  input:"#period#",
		  option:"#period#"
		},
	  data:[{value:"1",text:"分页"},{value:"0",text:"不分页"}]
  });
  ($("#showPage").next())[0].comboObj.disable();
  
  $("#fid").validatebox({
  });
  
  $("#cancel").click(function(){
		$('#content').load("${ctx}/report/SysRepor/detail?fid="+id);
  });
  
  $("#save").click(function(){
	  $("#form").form('enableValidation');
	  if($("#form").form('validate')){
		  $("#save").attr('disabled',true);
		  $.ajax({
			  type : 'post',
			  url : '${ctx}/report/SysRepor/saveReport',
			  data : {countInfo:$("#countInfo").val(),fid :$("#fid").val(),reportName:$("#reportName").val(),code:$("#code").val(),headers:$("#headers").val(),parentId:$("#parentId").val(),javaScript:$("#javaScript").val(),showPage:($("#showPage").next())[0].comboObj.getSelectedValue()},
			  dataType : 'json',
			  success : function(data) {
				  if(data.result == 0){
					  $.fool.alert({msg:'保存成功！',fn:function(){
						  $("#saveNCancel").fadeOut();
						  $("#save").removeAttr('disabled');
						  $('#reportTypeTree').tree('reload');
						  $('#content').load("${ctx}/report/SysRepor/detail?fid="+id);
					  }});
				  }else{ 
					  $.fool.alert({msg:data.msg,fn:function(){
						  $("#save").removeAttr('disabled');
					  }});
				  }
			  },
			  error:function(){
				  $.fool.alert({msg:'系统正忙，请稍后再试。',fn:function(){
					  $("#save").removeAttr('disabled');
				  }});
			  }
		  });
	  }
  });
</script>
</body>
</html>