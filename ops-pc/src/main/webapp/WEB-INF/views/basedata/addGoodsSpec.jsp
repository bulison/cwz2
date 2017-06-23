<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>1
<c:choose>
<c:when test="${empty goodsSpecVo.fid}">新增</c:when>
<c:otherwise>修改</c:otherwise>
</c:choose>
</title>
</head>
<body>
<form id="goodGoodsAttrForm" class="form readonlyForm">
<input type="hidden" id="fid" name="fid" value="${goodsSpecVo.fid}"/>
<input type="hidden" value="${goodsSpecVo.parentId}" name="parentId" id="parentId"/>

<input type="hidden" name="updateTime" value="${goodsSpecVo.updateTime}"/>
<p><font><label style="color: red">*</label>编号：</font><input class="textBox" type="text" name="code" value="${goodsSpecVo.code}" id="code"></p>
<p><font><label style="color: red">*</label>名称：</font><input class="textBox" type="text" name="name" value="${goodsSpecVo.name}" id="name"></p>
<p><font>描述：</font><textarea name="describe" class="easyui-validatebox textArea">${goodsSpecVo.describe}</textarea></p>
<p><font><label style="color: red">*</label>状态：</font>
<c:choose>
   <c:when test="${goodsSpecVo.recordStatus == 'SAC'}">
	   <input type="radio"  name="recordStatus" checked="checked" value="SAC"/>有效
	   <input type="radio" name="recordStatus" value="SNU"/>失效
   </c:when>
   <c:when test="${goodsSpecVo.recordStatus == 'SNU'}">
   <input type="radio"  name="recordStatus" value="SAC"/>有效
   <input type="radio" name="recordStatus" checked="checked" value="SNU"/>失效
   </c:when>
   <c:otherwise>
    <input type="radio" name="recordStatus" checked="checked" value="SAC"/>有效
    <input type="radio" name="recordStatus" value="SNU"/>失效
   </c:otherwise>
 </c:choose>
   
 </p>
<p><font></font><input class="btn-blue2 btn-xs" type="submit" value="保存" style=""/></p>
</form>
<script type="text/javascript">
$(function(){
	/* $.extend($.fn.validatebox.defaults.rules,{
		code:{//手机验证
				validator: function (value, param) {
					
					$.post("${ctx}/basedata/repeatByCategory?fid="+$("#fid").val()+"&cid="+$("#categoryId")+"&code="+value,function(data){
						alert(data);
					});
					
		            return false;
				},message:"编码已经存在"
			}
		});
	
	 */
	 
	 $("#code").validatebox({required:true,validType:'isBank'});
	 $("#name").validatebox({required:true,validType:'isBank'});
	 
	// $("#auxiliaryAttrForm").form({novalidate:true});
	$('#goodGoodsAttrForm').fool('form',{onSubmit:function(isValid){
		if(!isValid)return false;
		var _data = $('#goodGoodsAttrForm').serializeJson();
		$.ajax({
			url:'${ctx}/goodsspec/save',
			type:"post",
			'data':_data,
			cache:false,
			dataType:'json',
			success:function(data){
				if(data.returnCode=='0'){
					$('#goodsSpecTree').tree('reload');
					fromEnable("#goodGoodsAttrForm",false).form('clear').form({novalidate:true});
					$.fool.alert({time:1000,msg:'操作成功!'});
				}else{
					$.fool.alert({time:2000,msg:'操作失败!['+data.message+']'});
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