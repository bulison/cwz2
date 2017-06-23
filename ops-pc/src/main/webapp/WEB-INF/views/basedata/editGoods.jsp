<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑货品</title>
</head>
<body>
	<form action="${ctx}/goodsController/save" method="post" enctype="multipart/form-data">
		<input type="hidden" name="fid" value="${requestScope.goods.fid}">
		
		<table>
			<tr>
				<td>货品编号：<input type="text" name="code" maxlength="50" class="easyui-validatebox textBox" data-options="required:true,validType:'normalChar',missingMessage:'货品编码不可为空!'" value="${requestScope.goods.code}"></td>
				<td>货品名称：<input type="text" name="name" maxlength="50" class="easyui-validatebox textBox" data-options="required:true,validType:'normalChar',missingMessage:'货品名称不可为空!'" value="${requestScope.goods.name}"></td>
				<td>类别：<input type="text" name="category" maxlength="50" class="easyui-validatebox textBox" data-options="required:true,validType:'normalChar',missingMessage:'货品类别不可为空!'" value="${requestScope.goods.category}"></td>
			</tr>
			<tr>
				<td>规格型号：<input type="text" name="spec" maxlength="50" class="easyui-validatebox textBox" data-options="validType:'normalChar'" value="${requestScope.goods.spec}"></td>
				<td>单位：<input type="text" name="unit" maxlength="20" class="easyui-validatebox textBox" data-options="required:true,validType:'normalChar',missingMessage:'单位不可为空!'" value="${requestScope.goods.unit}"></td>
				<td>单价：<input type="text" name="unitPrice" maxlength="20" class="easyui-validatebox textBox" data-options="required:true,validType:'intOrFloat',missingMessage:'单价不可为空!'" value="${requestScope.goods.unitPrice}"></td>
			</tr>
			<tr>
				<td colspan="2">描述：<input type="text" name="describe" class="easyui-validatebox" validType="length[6,200]" maxlength="200" value="${requestScope.goods.describe}"></td>
				<td>产品图片：<input type="file" name="files"></td>
			</tr>
			<tr>
				<td><input type="submit" value="保存"></td>
				<td><input type="reset" value="重新填写"></td>
			</tr>
		</table>
	</form>	
	<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/easyui-validate-extend.js?v=${js_v}"></script>
	
</body>
</html>