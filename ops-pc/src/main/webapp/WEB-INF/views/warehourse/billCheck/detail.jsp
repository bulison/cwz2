<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>货品库存</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
</head>
<body>
	
	<table id="dataTable" class="easyui-datagrid" 
	data-options="width:'100%',pagination:false,singleSelect:true,fitColumns:true,url:'${ctx}/paymentCheck/checkedBillList?billId=${billId}'">
		<thead>
			<tr>
				<th data-options="field:'paymentBillCode',width:100">收款单号</th>
				<th data-options="field:'checkDate',width:100">对单日期</th>
				<th data-options="field:'amount',width:100">勾对金额</th>
				<th data-options="field:'freeAmount',width:100">优惠金额</th>
				<th data-options="field:'describe',width:100">描述</th>
			</tr>
		</thead>
	</table>

<div id="my-window"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
</script>