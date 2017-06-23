<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户管理</title>
</head>
<body>
<div class="window-search-box" style="margin:5px;">
<input class="easyui-textbox" id="customer-code" data-options="prompt:'编号',width:100,height:30" />
<input class="easyui-textbox" id="customer-name" data-options="prompt:'名称',width:100,height:30" />
<input class="easyui-combobox" id="customer-categoryName" data-options="prompt:'类别',editable:false,width:100,height:30,data:[{value:'1',text:'客户'},{value:'2',text:'供应商'}]" />
<a href="javascript:;" class="btn-blue btn-s go-search">筛选</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>

<table class="easyui-datagrid" id="customer-table" data-options="idField:'fid',fitColumns:true,pagination:true,url:'${ctx}/customerSupplier/list'">
<thead>
<tr>
<th data-options="field:'fid',checkbox:true,width:10"></th>
<th data-options="field:'code',width:10,sortable:false">编号</th>
<th data-options="field:'name',width:10,sortable:false">名称</th>
<th data-options="field:'type',width:10,sortable:false,formatter:typeFun">类别</th>
</tr>
</thead>
</table>

<script type="text/javascript">
var type = [
		    {id:'1',name:'客户'},
		    {id:'2',name:'供应商'}
		];
$(function(){	
	$('#customer-table').datagrid({
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		onDblClickRow:function(index,row){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		}
	});
	
	$(".go-search").click(function(){
		var code=$("#customer-code").textbox('getValue');
		var name=$("#customer-name").textbox('getValue');
		var type=$("#customer-categoryName").combobox('getValue');
		var options = {"code":code,"name":name,"type":type};
		$('#customer-table').datagrid('load',options);
	});
	
	$(".select-ok").click(function(){
		var rows = $('#customer-table').datagrid('getSelections');
		if(rows.length==0){
			$.fool.alert({time:1000,msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
	
	setPager($('#customer-table'));
});

function typeFun(value){
	for(var i=0; i<type.length; i++){
		if (type[i].id == value) return type[i].name;
	}
	return value;
};
</script>
</body>
</html>
