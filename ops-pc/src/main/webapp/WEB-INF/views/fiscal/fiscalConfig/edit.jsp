<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
</head>
<body>
  <form class="form" id="form" >
   <div style="padding-left: 140px;">
    <input id="fid" name="fid" type="hidden" value="${obj.fid}"/>
    <input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}">
    <p style="display: none;"><font><em>*</em>编号：</font><input id="code" name="code" readonly="readonly" data-options="required:true" class="textBox" value="${obj.code }"/></p>
    <p><font><em>*</em>名称：</font><input id="name" name="name" readonly="readonly" data-options="required:true" class="textBox" value="${obj.name }"/></p>
    <p><font>值：</font>
    
    <c:choose>
                  <c:when test="${obj.value == '0'}">
                    <input type="radio" name="value"  value="1"/>启用
                    <input type="radio"  name="value" checked="checked" value="0"/>禁止
	              </c:when>
	              <c:otherwise>
	                <input type="radio" name="value" checked="checked" value="1"/>启用
                    <input type="radio"  name="value" value="0"/>禁止
                  </c:otherwise>
	            </c:choose>
	</p>
    <input id="type" hidden="true"  name="valueType" class="textBox" value="${obj.valueType}"/>
    <input id="selectValue" hidden="true" name="selectValue"  class="textBox" value="${obj.selectValue}"/>
    <p style="width:85%;"><font>备注：</font>
    <textarea rows="5" cols="5" name="describe" >${obj.describe}</textarea>
    
    <%-- <input id="describe" name="describe" class="textBox" style="width:80%;" value="${obj.describe}"/></p> --%>
    <p><font></font><input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /></p>
    </div>
  </form>

<script type="text/javascript">
  //表单控件初始化
  $("#name").textbox({
	  required:true,
	  novalidate:true,
	  missingMessage:'该项不能为空！',
	  width:160,
	  height:31,
	  value:"${obj.name}"
  });
  
  //保存按钮点击事件
  $('#save').click(function(e) {
	  $('#form').form('enableValidation');
	  if($('#form').form('validate')){
		  $("#save").attr("disabled","disabled");
		  $.post('${ctx}/fiscalConfig/save',$("#form").serializeJson(),function(data){
			  dataDispose(data);
			  if(data.result=='0'){
				  $.fool.alert({time:1000,msg:'保存成功！',fn:function(){
					  $('#edit-window').window('close');
		    		  $("#save").removeAttr("disabled");
		    		  $('#dataTable').datagrid('reload');
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
		  $.fool.alert({time:1000,msg:"系统繁忙，请稍后再试。"});
			return false;
	  }
  });
  enterController('form');
</script>
</body>
</html>