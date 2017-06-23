<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>销售商弹出窗口</title>
</head>
<body>
<div class="window-search-box" style="margin:5px;">
<input id="supplier-code" />
<a href="javascript:;" class="btn-blue btn-s go-search">筛选</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>

<table class="easyui-datagrid" id="supplier-table"  data-options="url:'${ctx}/supplier/list?showDisable=0',pagination:true,fitColumns:true">
  <thead>
  	<th data-options="field:'fid',checkbox:true,width:10">fid</th>
   <th field="code" width="10" data-options="editor:'textbox'">编号</th>
   <th field="name" width="10">名称</th>
   <th field="areaName" width="10" data-options="hidden:true">地区</th>
   <th field="categoryName" width="10" data-options="hidden:true">分类</th>
   <th field="principal" width="10" data-options="hidden:true">法人代表</th>
   <th field="principalPhone" width="10" data-options="hidden:true">法人联系电话</th>
   <th field="tel" width="10" data-options="hidden:true">电话</th>
   <th field="fax" width="10" data-options="hidden:true">业务联系人传真</th>
   <th field="registerDate" width="10" data-options="hidden:true">成立日期</th>
   <th field="staffNum" width="10" data-options="hidden:true">在业人数</th>
   <th field="bussinessRange" width="10" data-options="hidden:true">经营范围</th>
   <th field="memberId" width="10" data-options="hidden:true">业务员ID</th>
   <th field="memberName" width="10" data-options="hidden:true">业务员</th>
   <th field="deptId" width="10" data-options="hidden:true">部门ID</th>
   <th field="deptName" width="10" data-options="hidden:true">部门名称</th>
  </thead>
 </table>

<script type="text/javascript">
$(function(){
	$("#supplier-code").textbox({
		prompt:'输入供应商名或者编号',
		width:150,
		height:30,
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					search();
				}
			}
		})
	});
	$('#supplier-table').datagrid({
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		onDblClickRow:function(index,row){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		}
	});
	
	$(".go-search").click(function(){
		var searchKey=$("#supplier-code").textbox('getValue');
		var options = {"searchKey":searchKey};
		$('#supplier-table').datagrid('load',options);
	});
	
	$(".select-ok").click(function(){
		var rows = $('#supplier-table').datagrid('getSelections');
		if(rows.length==0){
			$.fool.alert({time:2000,msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
	function search(){
		var searchKey=$("#supplier-code").textbox('getValue');
		var options = {"searchKey":searchKey};
		$('#supplier-table').datagrid('load',options);
	}
	setPager($('#supplier-table'));
});

</script>
</body>
</html>
