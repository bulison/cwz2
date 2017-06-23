<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单据维护</title>
<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
</head>
<body>
<form action="" class="billForm form1" id="warehousebillForm">

<input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
<input name="updateTime" id="updateTime" value="${obj.updateTime}" type='hidden'/>

<input id="customerId" name="customerId" value="${obj.customerId}" type="hidden"/>
<input name="deptId" id="deptId" value="${obj.deptId}" type='hidden'/>
<input id="inMemberId" name="inMemberId" value="${obj.inMemberId}" type='hidden'/>
<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>

<c:choose>
	<c:when test="${empty code}">
<p><font><em>*</em>单号：</font><input name="code" id="code" value="${obj.code}" class="textBox" type="text"/></p>		
	</c:when>
	<c:otherwise>
<p><font><em>*</em>单号：</font><input name="code" id="code" value="${code}" readonly="readonly" class="textBox" type="text"/></p>		
	</c:otherwise>
</c:choose>
<p><font><em>*</em>单据日期：</font><input name="billDate" _class="datebox_d_curr" id="billDate" value="${obj.billDate}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/></p>
<p><font><em>*</em>客户名称：</font><input id="customerName" name="customerName" value="${obj.customerName}" class="customerBox textBox" type="text" readonly="readonly"/></p>
<p><font>客户编号：</font><input id="customerCode" name="customerCode"value="${obj.customerCode}" class="textBox" type="text" readonly="readonly"/></p>
<p><font>客户电话：</font><input id="customerPhone" name="customerPhone" value="${obj.customerPhone}" class="textBox" type="text"/></p>
<p><font><em>*</em>部门：</font><input name="deptName" _class='deptComBoxTree_d' id="deptName" value="${isDetail ? obj.deptName : obj.deptId}" class="textBox" type="text"/></p>
<p><font><em>*</em>业务员：</font><input id="inMemberName" name="inMemberName" value="${obj.inMemberName}" class="memberBox textBox" type="text"/></p>

<%-- <p><font><em>*</em>业务员：</font><input id="inMemberName1" name="inMemberName" value="${obj.inMemberName}" class="textBox" type="text"/></p> --%>
<br/>
<%-- <p><font>备注：</font><input name="describe" class="textBox-mid" value="${obj.describe}"></input></p> --%>
<p style="width:70%"><font>备注：</font><input name="describe" class="textBox" style="width:80%" value="${obj.describe}" type="text"/></p>
<p class="more"><font><a href="#" onclick="show_more()" class="show-more"></a>其他信息</font></p>
<div class="more-div">
<p><font>制单人：</font><input name="creatorName" value="${obj.creatorName}" class="textBox" type="text"/></p>
<p><font>制单日期：</font><input name="createTime" value="${obj.createTime}" class="textBox" type="text"/></p>
<p><font>审核人：</font><input name="auditorName" value="${obj.auditorName}" class="textBox" type="text"/></p>
<p><font>审核日期：</font><input name="auditTime" value="${obj.auditTime}" class="textBox" type="text"/></p>
<p><font>作废人：</font><input name="cancelorName" value="${obj.cancelorName}" class="textBox" type="text"/></p>
<p><font>作废日期：</font><input name="cancelTime" value="${obj.cancelTime}" class="textBox" type="text"/></p>
</div>

<div class="toolbar">
${(obj.recordStatus==null||obj.recordStatus==0)?'<a href="#" class="btn-ora-add" onclick="addGood()" >添加</a>&nbsp;<a href="#" class="btn-ora-add hide savaAll" onclick="saveAll()" >一键确定</a>':''}

</div>
<table id="goodsList"></table>

</form>
<div class="btn-footer"></div>
<div id="pop-win"></div>
<script type="text/javascript">
<c:choose>
<c:when test="${empty obj.details}">
init('${param.flag}','${obj.recordStatus}');
</c:when>
<c:otherwise>
init('${param.flag}','${obj.recordStatus}',${obj.details});
</c:otherwise>
</c:choose>

function searchHelp(q,rows,searchFields){
	if(!q||!row||!searchFields)return false;
	var _sarr = searchFields.split(',');
	q = q.toLowerCase();
	var _tmpD = {};
	for(var _i in _sarr){
		if(row[_sarr[_i]].toLowerCase().indexOf(q) >= 0){
			return true;
		}
	}
	return false;
}


$(function(){
	$("#inMemberName1").fool('combogrid',{
		panelHeight:'auto',
		panelWidth:'240',
		idField:'fid',    
		textField:'username',
		url:'${ctx}/member/list',
		columns:[[
		          {field:'userCode',title:'编号',width:100,searchKey:false},
		          {field:'username',title:'名称',width:120,searchKey:true},
		          {field:'dept.orgName',title:'部门',width:100,hidden:true,searchKey:false},
		          {field:'deptName',title:'部门',width:100,hidden:true}
		]],on
	});
	
});

</script>
</body>
</html>