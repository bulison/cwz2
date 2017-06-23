<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>send record list</title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/layout.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/icon.css"/>
<link rel="stylesheet" href="${ctx}/resources/css/comm.css"/>
<script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<script>
	
</script>
</head>

<body>
    <div style="margin-top:20px;" class="tabel-warp">
    <table class="easyui-datagrid" id="dataTable" style="width:100%;" pagination="true"  
    	pageList="[10,20,50]",rownumbers="true" fitColumns="true" 
    	data-options="singleSelect:true,fitColumns:true,url:'${ctx}/sendrecord/querySendRecord'">
	    <thead>
	    	<tr>
	        	<th data-options="field:'receiverName',sortable:false" style="width:40px;">接收人</th>
	            <th data-options="field:'messageContent',sortable:false" style="width:100px;">信息内容</th>
	            <th data-options="field:'sendType',sortable:false" style="width:40px;">发送类型</th>
	            <th data-options="field:'result',sortable:false" style="width:40px;">发送结果</th>
	            <th data-options="field:'responseCode',sortable:false" style="width:40px;">返回码</th>
	            <th data-options="field:'responseDesc',sortable:false" style="width:40px;">返回描述</th>
	        </tr>
	    </thead>
    </table>
    </div>
</body>
</html>
