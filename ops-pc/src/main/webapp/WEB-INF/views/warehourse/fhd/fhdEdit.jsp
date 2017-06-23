<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${billName}</title>
</head>
<body>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
<!-- 从列表页传入参数    flag - edit,detail,copy   billCode - 单据编码 -->
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>


<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn"><div id="triangle"></div></div><h1>${billflagName}${billName}单据</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写单据信息</h2></div>
		<div class="in-box" id="list2">
		  	<div class="inlist">
			<!-- 
				隐藏的input集中写在这里
				下面以采购入库单为例
			 -->
			<input id="localCache" value="${localCache}" type='hidden'/>
			<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>
			<input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}"/>	 
			<input id="fid" name="fid" type="hidden" value="${obj.fid}"/>
			<input id="supplierId" name="supplierId" type="hidden" value="${obj.supplierId}"/>
			<input id="deliveryPlaceId" name="deliveryPlaceId" type="hidden" value="${obj.deliveryPlaceId}"/>
			<input id="receiptPlaceId" name="receiptPlaceId" type="hidden" value="${obj.receiptPlaceId}"/>
			<input id="transportTypeId" name="transportTypeId" type="hidden" value="${obj.transportTypeId}"/>
			<input id="shipmentTypeId" name="shipmentTypeId" type="hidden" value="${obj.shipmentTypeId}"/>
			<input name="inWareHouseId" id="inWareHouseId" value="${obj.inWareHouseId}" type='hidden'/>
			<input name="outMemberId" id="outMemberId" value="${obj.outMemberId}" type="hidden"/>
			<!-- 
				要输入input集中写在这里，一个<p>标签里放3个180宽的input
				input属性  
				_imp - 设置为true的时候，除easyui外的输入框将自动加上必填验证，并且在文字前加上红色*号。
				_class - 可以设置为easyui的常用输入框，封装在warhouseEdit.js中的editInputInit函数
				_billcode - 此属性是给关联订单输入框专用的，填了哪个订单的billcode就会弹出哪个订单的选择窗口,必须加上relationBox这个类名。关联弹出窗口的input必须加id="relation"
				name必须填写的，且必须与后台数据名字一样
				下面以采购入库单为例
				每一个输入类型的input都必须加上type="text"，这个关联到后面审核后禁用输入框的				
			 -->
		     <p>
		        <font>单号：</font><input _imp="true" type="text" id="code" name="code" class="textBox" ${code==null&&obj.code==null?"":"readonly='readonly'"} value="${code==null?(obj.code==null?'':obj.code):code}"/>
			    <font>运输批号：</font><input _imp="true" _trim='true' type="text" data-options="validType:'maxLength[50]'" id="transportNo" name="transportNo" class="textBox" value="${obj.transportNo}"/>			   
			    <font>单据日期：</font><input _imp="true" _class="datebox_curr" type="text" id="billDate" name="billDate" class="textBox billDate" value="${obj.billDate}" />
			  </p>
			 <p>
			   <font>运输公司名称：</font><input _imp="true" type="text" class="textBox" _class="supplier-combogrid" id="supplierName" name="supplierName"  value="${obj.supplierName}"/><!-- 就是供应商 -->
			   <font>发货地：</font><input _imp="true" type="text" id="deliveryPlaceName" id="deliveryPlaceName" name="deliveryPlaceName" class="textBox" value="${obj.deliveryPlaceName}"/>
			   <font>收货地：</font><input _imp="true" type="text" id="receiptPlaceName" id="receiptPlaceName" name="receiptPlaceName" class="textBox" value="${obj.receiptPlaceName}"/>
			 </p>
			 <p>
			    <font>运输方式：</font><input _imp="true" id="transportTypeName" type="text" _class="transportType" name="transportTypeName" class="textBox" value="${isDetail ? obj.transportTypeName : obj.transportTypeId}"/>
			    <font>装运方式：</font><input _imp="true" _class="shipmentType" type="text" id="shipmentTypeName" name="shipmentTypeName" class="textBox"  value="${isDetail ? obj.shipmentTypeName : obj.shipmentTypeId}"/>
			    <font>发货仓：</font><input  _imp="true"  id="inWareHouseName" type="text" name="inWareHouseName" class="textBox"  value="${obj.inWareHouseName}" readonly="readonly"/>
			 </p>
			  <p>
		        <font>车船号：</font><input name="carNo" id="carNo" value="${obj.carNo}" _class="carNo-combogrid" class="textBox" type="text"/>
			    <font>司机姓名：</font><input type="text" _trim='true' id="driverName"  name="driverName" value="${obj.driverName}"/>
			    <font>司机电话：</font><input type="text" _trim='true' id="driverPhone" name="driverPhone" value="${obj.driverPhone}" />
			  </p>
			  <p>
		        <font>预计到达日期：</font><input _imp="true" _class="datebox" type="text" id="endDate" name="endDate" class="textBox" data-options='{required:true,novalidate:true,validType:"dateCompar[\"#billDate\",-1,\"预计到达日期不能早于单据日期\"]"}' value="${obj.endDate}"/>
			    <font>发货人：</font><input name="outMemberName" _required="false" _class="outMember-combogrid" id="outMemberName" value="${obj.outMemberName}" class="textBox" type="text"/> 
			    <font>运输金额：</font><input type="text" data-options="validType:'maxLength[50]'" id="totalAmount" name="totalAmount" class="textBox" value="${obj.totalAmount}" readonly="readonly"/>	
			  </p>
			 <p>
			 	<font>备注：</font><input style="width:519px;" data-options="validType:'maxLength[200]'" type="text" id="describe" name="describe" class="easyui-validatebox textBox" value="${obj.describe}"/>
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
var texts = [ 
     		{title:'退料人',key:'inMemberName'},
     		{title : 'NO',key : 'code'},
     		{title : '日期',key : 'billDate'},
     		];
     		var thead = [
     		{title:'仓库',key:'inWareHouseName'},
             {title:'编号',key:'goodsCode',width:8},
             {title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
             {title:'单位',key:'unitName'},
             {title:'数量',key:'quentity'},
             {title:'单价',key:'unitPrice'},
             {title:'金额',key:'type'},
             {title:'备注',key:'describe',width:10}
     		
     		];
     		var tfoot = [ 
     			{dtype:3,key:'type',text:'金额合计(大写)：#'},
     			{dtype:2,key:'type',text:'小写金额&nbsp;¥#元'},
     		           ];         		
/* <c:choose>
<c:when test="${empty obj.details}">
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}');
</c:when>
<c:otherwise>
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}',${obj.details});
</c:otherwise>
</c:choose>  */

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
$('#driverName').textbox({
    validType:'maxLength[50]',
	width:182,
	height:31,
})
$('#driverPhone').textbox({
    validType:'maxLength[50]',
    width:182,
    height:31,
})
var AddressData="";
$.ajax({
    url:getRootPath()+"/api/freightAddress/findAddressTree?enable=1",
    async:false,
    data:{},
    success:function(data){
        AddressData=formatTree(data,"text","id");
    }
});
var deliveryCombo = $("#deliveryPlaceName").fool("dhxCombo",{
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
    data:AddressData,
    onChange:function(value,text){
        var obj = deliveryCombo.getOption(value).text;
        $('#deliveryPlaceName').val(text);
        $('#deliveryPlaceId').val(value);
        $('#inWareHouseName').val(obj.assetwarehouseName);
        $("#inWareHouseId").val(obj.assetwarehouseId);
        demandAmount();
    },
});
var receiptCombo = $("#receiptPlaceName").fool("dhxCombo",{
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
    data:AddressData,
    onChange:function(value,text){
        $('#receiptPlaceName').val(text);
        $('#receiptPlaceId').val(value);
        demandAmount();
    },
});
if('${obj.recordStatus}'==1||'${obj.recordStatus}'==2){
    deliveryCombo.disable();
    receiptCombo.disable();
    $('#driverName').textbox('disable');
    $('#driverPhone').textbox('disable');
}
</script>

</body>
</html>