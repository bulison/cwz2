<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>货品定价弹出窗口-暂时没用</title>
</head>
<body>

<div class="window-search-box" style="margin:5px;">
<input id="search-goodsName" class="easyui-textbox" data-options="prompt:'货品名',width:100,height:30"/>
<a href="javascript:;" class="btn-blue btn-s go-search">筛选</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>

<table id="goodsPric-table">
<thead>
<tr>

</tr>
</thead>
</table>

<script type="text/javascript">
$(function(){
	$('#goodsSpec-Table').datagrid({
		url:'${ctx}/goodsspec/getLeafSpec',
		idField:'fid',
		pagination:true,
		fitColumns:true,
		<c:if test='${param.singleSelect}'>
		singleSelect:true,
		</c:if>
		columns:[[
		          {field:'fid',title:'fid',checkbox:true,width:100},
		          {field:'code',title:'编号',sortable:true,width:100},
		          {field:'name',title:'名称',sortable:true,width:100},
		      ]],
		onClickRow:function(rowIndex, rowData){
			
		},
		onDblClickRow:function(index,row){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row));
			}
		}
	});

	setPager($('#goodsSpec-Table'));
});
function goodsSpec_search(){
	var name=$("#search-goodsSpecName").textbox('getValue');
	var options = {"name":name};
	$('#goodsSpec-Table').datagrid('load',options);
}

function goodsSpec_ok(){
	var rows = $('#goodsSpec-Table').datagrid('getSelections');
	if(rows.length==0){
		$.fool.alert({msg:'你没有选择任何数据！'});
		return;
	}
	if('${param.okCallBack}'.length>0){
	     eval(${param.okCallBack}(rows));
	}
}
</script>

</body>
</html>
