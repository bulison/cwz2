<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>银行信息管理</title>
</head>
<body>
<div class="window-search-box" style="margin:5px;">
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>
<table class="easyui-datagrid" id="bank-table" data-options="idField:'fid',fitColumns:true,pagination:true,url:'${ctx}/bankController/list'">
<thead>
<tr>
<th data-options="field:'fid',checkbox:true,width:10">fid</th>
<th data-options="field:'code',width:10,sortable:false">编号</th>
<th data-options="field:'name',width:10,sortable:false">名称</th>
<th data-options="field:'bank',width:10,sortable:false">开户行</th>
<th data-options="field:'account',width:10,sortable:false">账号</th>
</tr>
</thead>
</table>

<script type="text/javascript">
$(function(){	
	$('#bank-table').datagrid({
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		onDblClickRow:function(index,row){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		}
	});
	
	$(".select-ok").click(function(){
		var rows = $('#bank-table').datagrid('getSelections');
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
	
	setPager($('#bank-table'));
});
</script>
</body>
</html>
