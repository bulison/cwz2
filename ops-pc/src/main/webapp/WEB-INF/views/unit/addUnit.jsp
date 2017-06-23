<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<html>
<head>
	<title>单位添加页面</title>
</head>
<body>
<style>
	p span{
		color: red;
	}
</style>
	<div class="form">
		<form id="unitAddForm">
			<input type="hidden" name="parentId" value="${parentId}"/>
			<input type="hidden" name="root" value="${param.root}" id="root"/>
			<p><font><span>*</span>编号：</font><input id="code" class="textBox" type="text" validType="isBank" name="code"/></p>
			<p><font><span>*</span>名称：</font><input id="name" class="textBox" type="text" validType="isBank" name="name"/></p>
			<c:choose>
				<c:when test="${param.root}"><input type="hidden" name="scale" value="1"/></c:when>
				<c:when test="${param.count eq '0'}">
					<p><font><span>*</span>换算关系：</font><input class="textBox not-operate" style="background-color:#D1D9E0" type="text" name="scale" value="1" readonly="true"/></p>
				</c:when>
				<c:otherwise>
					<p><font><span>*</span>换算关系：</font><input id="scale" class="textBox" type="text" name="scale" value="0"/>
					<div class="scale-display" style="margin-left: 130px;"></div>
					</p>
				</c:otherwise>
			</c:choose>
			<p><font>有效：</font>
				<input type="radio" name="enable" value="1" id="enable_1" checked="checked"/><label for="enable_1">是</label>
				<input type="radio" name="enable" value="0" id="enable_0"/><label for="enable_0">否</label>
			</p>
			<p><font>描述：</font><input id="describe" class="textBox" type="text" name="describe"/></p>
			<p><font></font><a href="javascript:;" id="addUnit" class="btn-blue2 btn-s2">保存</a></p>
			
			
		</form>
	</div>
	
	<script type="text/javascript">
		//校验表单
		function validateForm(){
			$("#code").validatebox({required:true});
			$("#name").validatebox({required:true});
			$("#scale").validatebox({required:true, validType:'intOrFloat'});
			return $("#unitAddForm").form("validate");
		}
		
		//添加单位
		$("#addUnit").click(function(){
			if(validateForm()){
				var url = "${ctx}/unitController/save";
				var data = $("#unitAddForm").serializeJson();
				ajaxRequest(url, data, function(result){
					if(result.returnCode == "1" ){
						$.fool.alert({time:2000,msg:result.message});
					}
					else{
						//reloadTree();
						addNode(result.data,data);
						$('#unitAddForm')[0].reset();					 
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