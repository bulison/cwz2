<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>货品属性弹出窗口</title>
</head>
<body>

<div class="window-search-box" style="margin:5px;">
<input id="goodSpec-name" class="easyui-textbox" data-options="prompt:'货品属性名称',width:100,height:30"/>
<a href="javascript:;" class="btn-blue btn-s go-search">筛选</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>

<table class="easyui-datagrid" id="goodsSpec-table" data-options="idField:'fid',fitColumns:true,pagination:true,url:'${ctx}/goodsspec/getChilds?groupId=${param.groupId}'">
<thead>
<tr>
<th data-options="field:'fid',checkbox:true,width:10">fid</th>
<th data-options="field:'code',width:10,sortable:false">编号</th>
<th data-options="field:'name',width:10,sortable:false">名称</th>

<th data-options="field:'orgId',hidden:true,width:10">机构ID</th>
<th data-options="field:'flag',hidden:true,width:10">标识</th>
<th data-options="field:'recordStatus',hidden:true,width:10">记录状态</th>
<th data-options="field:'parentId',hidden:true,width:10">上级属性ID</th>
<th data-options="field:'parentName',hidden:true,width:10">上级属性名称</th>
<th data-options="field:'updateTime',hidden:true,width:10">更新时间</th>
<th data-options="field:'describe',hidden:true,width:10">描述</th>
<th data-options="field:'goodsSpecGroupId',hidden:true,width:10">货品属性组ID</th>
</tr>
</thead>
</table>

<script type="text/javascript">
$(function(){
	$('#goodsSpec-table').datagrid({
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		onDblClickRow:function(index,row){
			if('${param.onDblClick}'.length>0){
				eval(${param.onDblClick}(row,"${param.other}"));
			}
		}
	});
	setPager($('#goodsSpec-table'));
	
	$(".go-search").click(function(){
		var name=$("#goodSpec-name").textbox('getValue');
		var options = {"name":name};
		$('#goodsSpec-table').datagrid('load',options);
	});
	
	$(".select-ok").click(function(){
		var rows = $('#goodsSpec-table').datagrid('getSelections');
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