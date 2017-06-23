<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<html>
<head>
	<title>单位详情页面</title>
	<style type="text/css">
		.unable{
			background-color:rgb(209, 217, 224)
		}
	</style>
</head>
<body>
	<div class="form">
		<form id="unitUpdateForm">
			<p><font>编号：</font><input class="textBox unable" type="text" name="code" value="${unit.code}" readonly="readonly"/></p>
			<p><font>名称：</font><input class="textBox unable" type="text" name="name" value="${unit.name}" readonly="readonly"/></p>
			<c:choose>
				<c:when test="${unit.root or unit.flag==0}"><input type="hidden" name="scale" value="1"/></c:when>
				<c:otherwise>
					<p><font>换算关系：</font><input class="textBox unable" type="text" name="scale" value="${unit.scale}" readonly="readonly"/></p>	
				</c:otherwise>
			</c:choose>
			<p><font>有效：</font>
				<c:choose>
					<c:when test="${unit.enable eq '0'}">
						<input type="radio" name="enable" value="1" id="enable_0" disabled="disabled"/><label for="enable_0">是</label>
						<input type="radio" name="enable" value="0" id="enable_1" disabled="disabled" checked="checked"/><label for="enable_1">否</label>
					</c:when>
					<c:otherwise>
						<input type="radio" name="enable" value="1" id="enable_0" disabled="disabled" checked="checked"/><label for="enable_0">是</label>
						<input type="radio" name="enable" value="0" id="enable_1" disabled="disabled"/><label for="enable_1">否</label>						
					</c:otherwise>
				</c:choose>
			
				
			</p>			
			<p><font>描述：</font><input class="textBox unable" type="text" name="describe" value="${unit.describe}" readonly="readonly"/></p>
		</form>
	</div>
</body>
</html>