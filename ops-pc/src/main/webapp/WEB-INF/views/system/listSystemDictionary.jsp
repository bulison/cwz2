<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>数据字典列表</title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/layout.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/icon.css"/>
<link rel="stylesheet" href="${ctx}/resources/css/comm.css"/>
<script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
</head>
  <body>
  <script type="text/javascript">
  $(function(){
		$('#dict-ok').click(function(){
			var systemDictionaryRows = $('#systemDictionaryDataTable').datagrid('getSelections');
			if(systemDictionaryRows.length != 1){
				$.fool.alert({msg:'请选择一条记录！'});
			}else{
				$('#operationTypeId').val(systemDictionaryRows[0].key);	
				$('#operationTypeName').val(systemDictionaryRows[0].name);
				$('.div-dialog-div').dialog('close');
			}
			
		});
	});
  </script>
  <div>
  		<a href="javascript:;" id="dict-ok" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">选择</a>  
  </div>
  <table class="easyui-datagrid" id="systemDictionaryDataTable" style="width: 100%;" pagination="true" pageList="[5,10,15,20,25,30]" 
	rownumbers="true" data-options="url:'${ctx}/systemDictionaryController/queryDictsByCode',fitColumns:true,singleSelect:false">
  	<thead>
			<tr>
				<th data-options="field:'fid', checkbox:'true' ,width:100"></th>
				<th data-options="field:'key',width:100,hidden:true"></th>
				<th data-options="field:'name',width:100"></th>
			</tr>
			
		</thead>
    </table>
 </body>
</html>
