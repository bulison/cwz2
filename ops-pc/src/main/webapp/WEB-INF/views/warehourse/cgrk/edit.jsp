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

<input name="supplierId" id="supplierId" value="${obj.supplierId}" type="hidden"/>
<input name="deptId" id="deptId" value="${obj.deptId}" type='hidden'/>
<input name="inWareHouseId" id="inWareHouseId" value="${obj.inWareHouseId}" type='hidden'/>
<input name="inMemberId" id="inMemberId" value="${obj.inMemberId}" type='hidden'/>
<input name="relationId" id="relationId" value="${obj.relationId}" type='hidden'/>
<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>

<c:choose>
	<c:when test="${empty code}">
<p><font><em>*</em>单号：</font><input name="code" id="code" value="${obj.code}" class="textBox" type="text"/></p>		
	</c:when>
	<c:otherwise>
<p><font><em>*</em>单号：</font><input name="code" id="code" value="${code}" readonly="readonly" class="textBox" type="text"/></p>		
	</c:otherwise>
</c:choose>
<p><font>凭证号：</font><input name="voucherCode" id="voucherCode" value="${obj.voucherCode}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/></p>
<p><font><em>*</em>单据日期：</font><input name="billDate" _class="datebox_d_curr" id="billDate" value="${obj.billDate}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/></p>
<p><font>采购订单：</font><input _billCode='cgdd' name="relation" id="relation" value="${obj.relationName}" class="relationBox textBox" type="text" readonly="readonly"/></p><a href="#" title="清除" class="clearBill"><img style="vertical-align:middle;" src="${ctx}/resources/images/cancel.png"></a></p>
<p><font><em>*</em>供应商名称：</font><input id="supplierName" name="supplierName" value="${obj.supplierName}" class="supplierBox textBox" type="text" readonly="readonly"/></p>
<p><font>供应商编号：</font><input id="supplierCode" name="supplierCode" value="${obj.supplierCode}" class="textBox" type="text" readonly="readonly"/></p>
<p><font>供应商电话：</font><input id="supplierPhone" name="supplierPhone" validType="phone" value="${obj.supplierPhone}" class="textBox" type="text"/></p>
<p><font><em>*</em>部门：</font><input name="deptName" _class='deptComBoxTree_d' id="deptName" value="${isDetail ? obj.deptName : obj.deptId}" class="textBox" type="text"/></p>
<p><font>仓库：</font><input name="inWareHouseName" _class="warehouse_d" id="inWareHouseName" value="${isDetail ? obj.inWareHouseName : obj.inWareHouseId}" class="textBox" type="text"/></p>
<p><font><em>*</em>采购员：</font><input id="inMemberName" name="inMemberName" value="${obj.inMemberName}" class="memberBox textBox" type="text"/></p>
<p><font><em>*</em>计划完成日期：</font><input id="endDate" name="endDate" _class="datebox_d" value="${obj.endDate}" data-options='{required:true,novalidate:true,validType:"dateCompar[\"#billDate\",-1,\"计划完成日期不能早于单据日期\"]"}' class="textBox" type="text"/></p>
<p><font>合计金额：</font><input id="totalAmount" name="totalAmount" value="${obj.totalAmount}" class="textBox" type="text" readonly="readonly"/></p>
<p><font>免单金额：</font><input id="freeAmount" name="freeAmount" value="${obj.freeAmount}" class="textBox" type="text" readonly="readonly"/></p>
<br/>
<%-- <p><font>备注：</font><textarea name="describe" class="textArea">${obj.describe}</textarea></p> --%>
<p style="width: 70%"><font>备注：</font><input name="describe" validType='maxLength[200]' class="textBox" style="width:80%" value="${obj.describe}" type="text"/></p>
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

<div class="btn-footer" _id="${obj.fid}">
<!-- <a id="save" onclick="saveData()" class="btn-blue btn-s">保存</a>
<a id="verify" class="btn-blue btn-s">审核</a>
<a id="print" class="btn-blue btn-s">打印</a>
<a id="refreshForm" class="btn-blue btn-s" onclick="refreshForm()">刷新</a>
<a id="void" class="btn-blue btn-s">作废</a> -->
</div>

<div id="pop-win"></div>



<script type="text/javascript">
//var _d = ${obj.details};
<c:choose>
<c:when test="${empty obj.details}">
init('${param.flag}','${obj.recordStatus}');
</c:when>
<c:otherwise>
init('${param.flag}','${obj.recordStatus}',${obj.details});
</c:otherwise>
</c:choose>
<fool:tagOpt optCode="cgrkAction1">//</fool:tagOpt>$('#save').remove();
<fool:tagOpt optCode="cgrkAction5">//</fool:tagOpt>$('#void').remove();
<fool:tagOpt optCode="cgrkAction3">//</fool:tagOpt>$('#copyRow').remove();
$(".clearBill").click(function(){
	if($("#relation").val()){
		$("#relationId").val("");
		$("#relation").val("");
		$("#supplierId").val("");
		$("#supplierName").val("");
		$("#supplierCode").val("");
		$("#supplierPhone").val("");
		$("#goodsList").datagrid('loadData',[]);
	}
});
</script>

</body>
</html>