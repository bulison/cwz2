<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<html>
<head>
	<title>单位编辑页面</title>
</head>
<body>
	<div class="form">
		<form id="unitUpdateForm">
			<input class="textBox" type="hidden" name="fid" value="${unit.fid}"/>
			<input class="textBox" type="hidden" name="parentId" value="${unit.parentId}"/>
			<input class="textBox" type="hidden" name="flag" value="${unit.flag}"/>
			<input class="textBox" type="hidden" name="updateTime" value="${unit.updateTime}"/>
			<p><font><em>*</em>编号：</font><input id="code" class="textBox" type="text" name="code" value="${unit.code}"/></p>
			<p><font><em>*</em>名称：</font><input id="name" class="textBox" type="text" name="name" value="${unit.name}"/></p>
			<c:choose>
				<c:when test="${unit.flag==0}"><input type="hidden" name="scale" value="1"/></c:when>
				<c:when test="${unit.first}">
					<p><font><em>*</em>换算关系：</font><input class="textBox not-operate" style="background-color:#D1D9E0" type="text" name="scale" value="${unit.scale}" readonly="true"/></p>
				</c:when>
				<c:otherwise>
					<p><font><em>*</em>换算关系：</font><input id="scale" class="textBox" type="text" name="scale" value="${unit.scale}"/>
					<div class="scale-display" style="margin-left: 130px;"></div>
					</p>	
				</c:otherwise>
			</c:choose>
			<%-- <p><font>换算关系：</font><input id="scale" class="textBox" type="text" name="scale" value="${unit.scale}"/></p> --%>
			<p><font>有效：</font>
				<c:choose>
					<c:when test="${unit.enable eq '0'}">
						<input type="radio" name="enable" value="1" id="enable_0"/><label for="enable_0">是</label>
						<input type="radio" name="enable" value="0" id="enable_1" checked="checked"/><label for="enable_1">否</label>
					</c:when>
					<c:otherwise>
						<input type="radio" name="enable" value="1" id="enable_0" checked="checked"/><label for="enable_0">是</label>
						<input type="radio" name="enable" value="0" id="enable_1"/><label for="enable_1">否</label>						
					</c:otherwise>
				</c:choose>
			</p>
			<p><font>描述：</font><input id="describe" class="textBox" type="text" name="describe" value="${unit.describe}"/></p>
			<p><font></font><a href="javascript:;" id="updateUnit" class="btn-blue2 btn-s2">保存</a></p>
		</form>
	</div>
	
	<script type="text/javascript">
		//校验表单
		function validateForm(){
			$("#code").validatebox({required:true,validType:'isBank'});
			$("#name").validatebox({required:true,validType:'isBank'});
			$("#scale").validatebox({required:true, validType:'intOrFloat'});
			return $("#unitUpdateForm").form("validate");
		}
		
		//编辑单位
		$("#updateUnit").click(function(){
			if(validateForm()){
				var url = "${ctx}/unitController/save";
				var data = $("#unitUpdateForm").serializeJson();
				ajaxRequest(url, data, function(result){
					if(result.returnCode == "1" ){
						$.fool.alert({time:2000,msg:result.message});
					}
					else{
						//window.location.reload();
						updateNode(data);
					}
				});
			}
		});	
	
		<c:if test="${!empty scaleOne}">
		$("#name").change(changeScale);
		$("#scale").change(changeScale);
		
		function changeScale(){
			var _unit = $("#name").val();
			var _scale = $("#scale").val();
			$(".scale-display").css('color','red').html("一"+_unit+"="+_scale+"${scaleOne.name}");
		}
		</c:if>		
	</script> 
</body>
</html>