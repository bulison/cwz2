<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div class="window-search-box" style="margin:5px;">
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>

<table class="easyui-treegrid" id="customer-table" data-options="scrollbarSize:0,idField:'id',treeField:'text',fitColumns:true,url:'${ctx}/basedata/voucherWord'">
<thead>
<tr>
<th data-options="field:'id',checkbox:true"></th>
<th data-options="field:'text',width:10,sortable:false">凭证字</th>

</tr>
</thead>
</table>

<script type="text/javascript">
$(function(){	
	$('#customer-table').treegrid({
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		onDblClickRow:function(row){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		}
	});
	
	$("#clearSear").click(function(){
		$(".window-search-box").form('clear');
	});
	
	$(".select-ok").click(function(){
		var rows = $('#customer-table').treegrid('getSelections');
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
});

</script>
</body>
</html>
