<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>
<c:choose>
<c:when test="${empty vo.fid}">新增</c:when>
<c:otherwise>修改</c:otherwise>
</c:choose>
</title>
</head>
<body>
<form id="auxiliaryAttrTypeForm" class="form readonlyForm">
<div align="center">
<input type="hidden" value="${vo.fid}" name="fid" id="fid"/>
<p><font>编号：</font><input class="textBox" type="text" name="code" value="${vo.code}" id="code"/></p>
<p><font>名称：</font><input class="textBox" type="text" name="name" value="${vo.name}" id="name"/></p>
<p><font>有效：</font>
	<c:choose>
		<c:when test="${vo.enable eq '0'}">
			<input type="radio" name="enable" value="1" id="enable_1"/><label for="enable_1">是</label>
			<input type="radio" name="enable" value="0" id="enable_0" checked="checked"/><label for="enable_0">否</label>
		</c:when>
		<c:otherwise>
			<input type="radio" name="enable" value="1" id="enable_1" checked="checked"/><label for="enable_1">是</label>
			<input type="radio" name="enable" value="0" id="enable_0"/><label for="enable_0">否</label>			
		</c:otherwise>
	</c:choose>
	
</p>
<p><font>是否设置财务账套：</font>
	<c:choose>
		<c:when test="${vo.isAccount eq '0'}">
			<input type="radio" name="isAccount" value="1" /><label for="enable_1">是</label>
			<input type="radio" name="isAccount" value="0"  checked="checked"/><label for="enable_0">否</label>
		</c:when>
		<c:otherwise>
			<input type="radio" name="isAccount" value="1"  checked="checked"/><label for="enable_1">是</label>
			<input type="radio" name="isAccount" value="0" /><label for="enable_0">否</label>			
		</c:otherwise>
	</c:choose>
</p>
<p><font>描述：</font><input class="textBox" type="text" name="describe" value="${vo.describe}" id="describe"/></p>
<p><font></font><input class="btn-blue2 btn-xs" type="button" id="save" value="保存" style=""/></p>
</div>
</form>
<script type="text/javascript" src="${ctx}/resources/js/basedata/auxiliaryAttr/auxillaryAtrrTypeEdit.js?v=${js_v}"></script>

</body>
</html>