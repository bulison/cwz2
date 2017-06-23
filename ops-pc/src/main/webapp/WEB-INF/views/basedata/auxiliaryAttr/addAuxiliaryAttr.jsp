<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>1
<c:choose>
<c:when test="${empty auxiliaryAttr.fid}">新增</c:when>
<c:otherwise>修改</c:otherwise>
</c:choose>
</title>
</head>
<body>
<form id="auxiliaryAttrForm" class="form readonlyForm">
<input type="hidden" value="${auxiliaryAttr.categoryId}" name="categoryId" id="categoryId"/>
<input type="hidden" value="${auxiliaryAttr.updateTime}" name="updateTime" id="updateTime"/>
<input type="hidden" value="${auxiliaryAttr.parentId}" name="parentId" id="parentId"/>
<input type="hidden" value="${auxiliaryAttr.first}" name="first" id="first"/>
<input type="hidden" value="${auxiliaryAttr.fid}" name="fid" id="fid"/>
<p><font>编号：</font><input class="textBox" type="text" name="code" value="${auxiliaryAttr.code}" id="code"></p>
<p><font>名称：</font><input class="textBox" type="text" name="name" value="${auxiliaryAttr.name}" id="name"></p>
<!-- <p><font>有效：</font><input id="enable" name="enable"/></p> -->
<p><font>有效：</font>
	<c:choose>
		<c:when test="${auxiliaryAttr.enable eq '0'}">
			<input type="radio" name="enable" value="1" id="enable_1"/><label for="enable_1">是</label>
			<input type="radio" name="enable" value="0" id="enable_0" checked="checked"/><label for="enable_0">否</label>
		</c:when>
		<c:otherwise>
			<input type="radio" name="enable" value="1" id="enable_1" checked="checked"/><label for="enable_1">是</label>
			<input type="radio" name="enable" value="0" id="enable_0"/><label for="enable_0">否</label>			
		</c:otherwise>
	</c:choose>
	
</p>
<p><font>描述：</font><textarea name="describe" class="easyui-validatebox textArea">${auxiliaryAttr.describe}</textarea></p>
<p><font></font><input class="btn-blue2 btn-xs" type="submit" value="保存" style=""/></p>
</form>
<script type="text/javascript">
$(function(){
	 $("#code").validatebox({required:true});
	 $("#name").validatebox({required:true});
	 
	// $("#auxiliaryAttrForm").form({novalidate:true});
	$('#auxiliaryAttrForm').fool('form',{onSubmit:function(isValid){
		if(!isValid)return false;
		var _data = $('#auxiliaryAttrForm').serializeJson();
		var url = '${ctx}/basedata/saveAuxiliaryAttr'
		$.ajax({
			url:url,
			type:"post",
			'data':_data,
			cache:false,
			dataType:'json',
			async:false,
			success:function(data){
				//var str = JSON.stringify(_data);
				//alert(str);
				dataDispose(data);
				if(data.returnCode==0){
					if(dhxkey == 1){
                        selectTab(dhxname,dhxtab);
                    }
						if(!$("#fid").val()){
							addNode(data.data.fid,_data);
						}else{						
							updateNode(_data);
						} 
						var node=$("#auxiliaryTree").tree("find",$("#parentId").val());
					fromEnable("#auxiliaryAttrForm",false).form('clear').form({novalidate:true});
					/* $("#auxiliaryTree").tree("select",node.target); */
					$(node.target).click();
					$.fool.alert({time:1000,msg:'操作成功!'});
				}else{
					$.fool.alert({msg:'操作失败!['+data.message+']'});
				}
			}
		});	
		return false;
	}});
	
	/* $("#auxiliaryAttrForm").submit(function(){
		alert(3);

		$("#auxiliaryAttrForm").from({novalidate:false});
		
		var b = $("#auxiliaryAttrForm").form('validate');
		
		alert(b);
		
		if(!b)return false;
		
		var _data = $(this).serializeJson();
		$.ajax({
			url:'${ctx}/basedata/saveAuxiliaryAttr',
			type:"post",
			'data':_data,
			cache:false,
			dataType:'json',
			success:function(data){
				if(data.result=='ok'){
					if(!$("#fid").val()){
						addNode(data.fid,_data);
					}else{						
						updateNode(_data);
					}
					fromEnable("#auxiliaryAttrForm",false).form('clear');
					$.fool.alert({msg:'操作成功!'});
				}else{
					$.fool.alert({msg:'操作失败!['+data.errorMsg+']'});
				}
			}
		});	
		return false;
	}); */
});

</script>
</body>
</html>