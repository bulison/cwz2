<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>
<title>${billName}</title>


<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1>${billflagName}${billName}单据</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写单据信息</h2></div>
		<div class="in-box" id="list2">
		  	<div class="inlist">
			<input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
			<input id="localCache" value="${localCache}" type='hidden'/>
			<input name="updateTime" id="updateTime" value="${obj.updateTime}" type='hidden'/>
			<input name="deptId" id="deptId" value="${obj.deptId}" type='hidden'/>
			<input name="customerId" id="customerId" value="${obj.customerId}" type='hidden'/>
			<input name="relationId" id="relationId" value="${obj.relationId}" type='hidden'/>
			<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>
			<input name="creatorName" value="${obj.creatorName}" class="textBox" type='hidden'/>
			<input name="createTime" value="${obj.createTime}" class="textBox" type='hidden'/>
			<input name="auditorName" value="${obj.auditorName}" class="textBox" type='hidden'/>
			<input name="auditTime" value="${obj.auditTime}" class="textBox" type='hidden'/>
			<input name="cancelorName" value="${obj.cancelorName}" class="textBox" type='hidden'/>
			<input name="cancelTime" value="${obj.cancelTime}" class="textBox" type='hidden'/>
			<p>
				<c:choose>
					<c:when test="${empty code}">
						<font>单号：</font><input _imp="true" name="code" id="code" value="${obj.code}" class="textBox" type="text"/>		
					</c:when>
					<c:otherwise>
						<font>单号：</font><input _imp="true" name="code" id="code" value="${code}" readonly="readonly" class="textBox" type="text"/>	
					</c:otherwise>
				</c:choose>
				<font>原始单号：</font><input name="voucherCode" id="voucherCode" value="${obj.voucherCode}" data-options="validType:'maxLength[50]'" class="textBox easyui-validatebox" type="text"/>	
				<font>单据日期：</font><input _imp="true" name="billDate" _class="datebox_curr" id="billDate" value="${obj.billDate}" data-options="{required:true,novalidate:true,validType:'dateCompar[\'#planStart\',1,\'单据日期必须早于计划生产开始日期\']'}" class="textBox" type="text"/>
			</p>
			<p>
				<font>销售订单：</font><input _billCode='xsdd' name="relation" id="relation" value="${obj.relationName}" class="relationBox textBox" type="text" readonly="readonly"/><a href="javascript:;" title="清除" class="clearBill" style="margin-left:-20px;"><img style="vertical-align:middle;" src="${ctx}/resources/images/cancel.png"></a>
			    <font>客户名称：</font><input _imp="true" id="customerName" name="customerName" _class="customer-combogrid" value="${obj.customerName}" class="textBox" type="text"/>
				<font>客户编号：</font><input _imp="true" id="customerCode" name="customerCode" value="${obj.customerCode}" class="textBox" type="text" readonly="readonly"/>
			</p>
			<p>
			    <font>部门：</font><input _imp="true" name="deptName" _class='deptComBoxTree' id="deptName" value="${isDetail ? obj.deptName : obj.deptId}" class="textBox" type="text"/>
			    <font>计划生产开始日期：</font><input _imp="true" id="planStart" name="planStart" _class="datebox" value="${obj.planStart}" data-options='{required:true,novalidate:true}' class="textBox" type="text"/>
			    <font>计划生产完成日期：</font><input _imp="true" id="endDate" name="endDate" _class="datebox" value="${obj.endDate}" data-options='{required:true,novalidate:true,validType:"dateCompar[\"#planStart\",-1,\"计划生产完成日期必须晚于计划生产开始日期\"]"}' class="textBox" type="text"/>
			</p>
			<p>
			    <font>生产状况：</font><input name="productionStatus" _class='productionStatus' id="productionStatus" value="${obj.productionStatus}" class="textBox" type="text"/>
			    <font>其他费用：</font><input name="otherCharges" class="otherCharges textBox" value="${obj.otherCharges}" type="text" readonly="readonly"/></br>
			</p>
			<p>
			    <font>备注：</font><input name="describe" class="textBox easyui-validatebox" style="width:519px;" value="${obj.describe}" type="text" data-options="validType:'maxLength[200]'"/>
			</p>
			<br/><p style="display: inline-block;margin-left:44px">&emsp;其他信息：<img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"></p><br/>
			 <p class="hideOut">
			    <font>制单人：</font><input id="creatorName" class="textBox" disabled="disabled" value="${obj.creatorName}"/>
			    <font>制单时间：</font><input id="createTime" class="textBox" disabled="disabled" value="${obj.createTime}"/>
			    <font>审核人：</font><input id="auditorName" class="textBox" disabled="disabled" value="${obj.auditorName}"/>
		     </p>
			 <p class="hideOut">
			    <font>审核时间：</font><input id="auditTime" class="textBox" disabled="disabled" value="${obj.auditTime}"/>
			    <font>作废人：</font><input id="cancelorName" class="textBox" disabled="disabled" value="${obj.cancelorName}"/>
			    <font>作废时间：</font><input id="cancelTime" class="textBox" disabled="disabled" value="${obj.cancelTime}"/>
			 </p>
			</div>
		</div>
	</div>
	<div id="dataBox" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>添加货品</h2></div>
		<div class="in-box" id="list1">
			<table id="goodsList"></table>
		</div>
		<div class="billTitle"><div id="square2"></div><h2>添加材料</h2></div>
		<div class="in-box" id="list3">
			<table id="materialList"></table>
		</div>
	</div>
	

</form>
<div class="mybtn-footer" _id="${obj.fid}">


</div>
<div id="scroll1" class="scroll1">
	<div id="scroll2" class="scroll2"></div>
</div>
<div id="pop-win"></div>

<%-- <script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script> --%>
<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit2.js?v=${js_v}"></script>
<script type="text/javascript">
 var details=${obj.details}
 details0=[];
 details1=[];
 if(details.length>0){
	 for(var i=0;i<details.length;i++){
		 if(details[i].detailType==1){
			 details1.push(details[i]);
		 }else{
			 details0.push(details[i]);
		 }
	 }
 }
<c:choose>
<c:when test="${empty obj.details}">
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}');
initEdit2('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}');
</c:when>
<c:otherwise>
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}',details0);
initEdit2('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}',details1);
</c:otherwise>
</c:choose>

if("${isDetail}"){
	for(var i=0; i<productionStatus.length; i++){
		if (productionStatus[i].value == "${obj.productionStatus}"){
			$("#productionStatus").val(productionStatus[i].text);
		} 
	}
}

function count(fid,billCode){
	$.ajax({
		type : 'post',
		url : '${ctx}/warehouse/scjhd/calculation',
		data : {id : fid},
		dataType : 'json',
		success : function(data) {
			for(var i in data){
				data[i]._goodsCode=data[i].goodsCode;
				data[i]._goodsSpecName=data[i].goodsSpecName;
			}
			$('#materialList').datagrid('loadData',[]);
			$('#materialList').datagrid('loadData',data);
		},
		error:function(){
			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		}
	});
};

$.extend($.fn.validatebox.defaults.rules, {
	compareDate:{
	    validator: function (value, param) {
	    	var start=$(param[0]).datebox("getValue");
	    	var startStr=start.split("-");
			var endStr=value.split("-");
			var startStrNum=parseInt(startStr[0]+startStr[1]+startStr[2]);
			var endStrNum=parseInt(endStr[0]+endStr[1]+endStr[2]);
			if(startStrNum>endStrNum){
				return false;
			}else{
				return true;
			}
	    },
	    message:'结束时间不能早于开始时间'
	},
});
</script>
</body>
</html>