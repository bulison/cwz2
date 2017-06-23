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


<div id="pricing-box"></div>
<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1>${billflagName}${billName}单据</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<span class="mystatus"></span>
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写单据信息</h2></div>
		<div class="in-box" id="list2">
			<div class="inlist">
			<input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
			<input id="localCache" value="${localCache}" type='hidden'/>
			<input name="updateTime" id="updateTime" value="${obj.updateTime}" type='hidden'/>
			<input name="recordStatus" id="recordStatus" value="${obj.recordStatus}" type='hidden'/>
			<input name="creatorName" value="${obj.creatorName}" class="textBox" type='hidden'/>
			<input name="createTime" value="${obj.createTime}" class="textBox" type='hidden'/>
			<input name="auditorName" value="${obj.auditorName}" class="textBox" type='hidden'/>
			<input name="auditTime" value="${obj.auditTime}" class="textBox" type='hidden'/>
			<input name="cancelorName" value="${obj.cancelorName}" class="textBox" type='hidden'/>
			<input name="cancelTime" value="${obj.cancelTime}" class="textBox" type='hidden'/>
			<input name="relationId" id="relationId" value="${obj.relationId}" type='hidden'/>
			<input name="supplierId" id="supplierId" value="${obj.supplierId}" type="hidden"/>
			<input name="deliveryPlaceId" id="deliveryPlaceId" value="${obj.deliveryPlaceId}" type="hidden"/>
			<input name="receiptPlaceId" id="receiptPlaceId" value="${obj.receiptPlaceId}" type="hidden"/>
			<input name="transportTypeId" id="transportTypeId" value="${obj.transportTypeId}" type="hidden"/>
			<input name="shipmentTypeId" id="shipmentTypeId" value="${obj.shipmentTypeId}" type="hidden"/>
			<input name="inMemberId" id="inMemberId" value="${obj.inMemberId}" type="hidden"/>
			<input name="inWareHouseId" id="inWareHouseId" value="${obj.inWareHouseId}" type='hidden'/>
			
			<p>
				<c:choose>
					<c:when test="${empty code}">
						<font>单号：</font><input _imp="true" name="code" id="code" value="${obj.code}" class="textBox" type="text"/>		
					</c:when>
					<c:otherwise>
						<font>单号：</font><input _imp="true" name="code" id="code" value="${code}" readonly="readonly" class="textBox" type="text"/>	
					</c:otherwise>
				</c:choose>	
				<font>发货单单号：</font><input _imp="true" _billCode='fhd' name="relationName" id="relation" value="${obj.relationName}" class="relationBox textBox" type="text" readonly="readonly"/><a href="javascript:;" title="清除" class="clearBill" style="margin-left:-20px;"><img style="vertical-align:middle;" src="${ctx}/resources/images/cancel.png"></a>
				<font>运输批号：</font><input _imp="true" name="transportNo" id="transportNo" value="${obj.transportNo}" class="textBox" type="text" readonly="readonly"/>
			</p>
			<p>
				<font>单据日期：</font><input name="billDate" _class="datebox_curr" id="billDate" value="${obj.billDate}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/>
				<font>运输公司名称：</font><input _imp="true" id="supplierName" name="supplierName" _class="supplier-combogrid" value="${obj.supplierName}" class="textBox" type="text"/>
				<font>发货地：</font><input  _imp="true" name="deliveryPlaceName" _class="freightAddress" id="deliveryPlaceName" value="${obj.deliveryPlaceName}" class="textBox" type="text" readonly="readonly"/>
				
			</p>
			<p>
			    <font>收货地：</font><input _imp="true" id="receiptPlaceName" name="receiptPlaceName"  id="receiptPlaceName" value="${obj.receiptPlaceName}" class="textBox" type="text"/>
				<font>运输方式：</font><input _imp="true" name="transportTypeName" _class="transportType" id="transportTypeName" value="${isDetail ? obj.transportTypeName : obj.transportTypeId}" class="textBox" type="text" readonly="readonly"/>
				<font>装运方式：</font><input _imp="true" name="shipmentTypeName" _class="shipmentType" id="shipmentTypeName" value="${isDetail ? obj.shipmentTypeName : obj.shipmentTypeId}" class="textBox" type="text" readonly="readonly"/> 
			</p>
			<p>
			    <font>收货仓：</font><input _imp="true" name="inWareHouseName" _class="warehouse" id="inWareHouseName" value="${isDetail ? obj.inWareHouseName : obj.inWareHouseId}" class="textBox" type="text"/>
				<font>车船号：</font><input name="carNo" id="carNo" value="${obj.carNo}" _class="carNo-combogrid" class="textBox" type="text" readonly="readonly"/>
				<font>司机姓名：</font><input id="driverName" name="driverName" value="${obj.driverName}" class="textBox" type="text" readonly="readonly"/>
			</p>
			<p>
			    <font>司机电话：</font><input id="driverPhone" name="driverPhone" value="${obj.driverPhone}" class="textBox" type="text" readonly="readonly"/>
				<font>到达日期：</font><input name="endDate" _class="datebox_curr" id="arriveDate" value="${obj.endDate}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/>
				<font>收货人：</font><input _imp="false" name="inMemberName" _class="member-combogrid" id="inMemberName" value="${obj.inMemberName}" class="textBox" type="text"/> 
			</p>
			<p>
				<font>运费金额：</font><input id="totalAmount" name="totalAmount" class="textBox" value="${obj.totalAmount}" type="text" readonly="readonly"/>
				<font>扣费金额：</font><input id="deductionAmount" name="deductionAmount" class="textBox" value="${obj.deductionAmount}" type="text" readonly="readonly"/>
				<font>优惠金额：</font><input id="freeAmount" name="freeAmount" class="textBox" value="${obj.freeAmount}" type="text" readonly="readonly"/>
			</p>
			<p>
				<font>付款金额：</font><input id="totalPayAmount" name="totalPayAmount" class="textBox" value="${obj.totalPayAmount}" type="text" readonly="readonly"/>
				<font>备注：</font><input id="describe" name="describe" class="textBox" value="${obj.describe}" type="text"/>
			</p>
			<p style="display: inline-block;margin-left:44px">&emsp;其他信息：<img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"></p><br/>
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
	</div>
	<div id="dataBox2" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>添加箱号</h2></div>
		<div class="in-box" id="list2">
			<table id="containerList"></table>
		</div>
	</div>
</form>
 <div class="mybtn-footer" _id="${obj.fid}"></div>
 <div class="repertory"></div>
 <div id="scroll1" class="scroll1">
   <div id="scroll2" class="scroll2"></div>
 </div>
 <div id="pop-win"></div>

<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<script type="text/javascript">
<c:choose>
<c:when test="${empty obj.details}">
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}');
</c:when>
<c:otherwise>
<c:choose>
<c:when test="${empty obj.transportDetails}">
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}',${obj.details});
</c:when>
<c:otherwise>
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}',${obj.details},${obj.transportDetails});
</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>
var freightAddressData="";
$.ajax({
    url:getRootPath()+"/api/freightAddress/findAddressTree?enable=1",
    async:false,
    data:{},
    success:function(data){
        freightAddressData=formatTree(data,"text","id");
    }
});
$("#receiptPlaceName").fool("dhxCombo",{
    required:true,
    novalidate:true,
	clearOpt:false,
    hasDownArrow:false,
	 setTemplate:{
	 input:"#name#",
	 option:"#name#"
 	},
	toolsBar:{
	  name:'货运地址' ,
		addUrl:'/freightAddress/manage',
		refresh:true
	},
	 focusShow:true,
	 editable:false,
 	 data:freightAddressData,
 });

$("#describe").validatebox({
    validType:'length[0,200]'
})
if(_billStat == "2"||_billStat == "1"){
    $("#receiptPlaceName").next()[0].comboObj.disable();
}
</script>

</body>
</html>