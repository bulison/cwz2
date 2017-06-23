<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<title>bom表</title>
<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1>${billflagName}bom表</h1><a href="javascript:;" id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写单据信息</h2></div>
		<div class="in-box" id="list2">
		  <div class="inlist">
		  <input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
		  <input id="localCache" value="${localCache}" type='hidden'/>
		  <input  id="recordStatus" value="${obj.enable}" type='hidden'/>
		  <input name="updateTime" id="updateTime" value="${obj.updateTime}" type='hidden'/>
	      <input name="accountUnitId" id="accountUnitId" value="${obj.accountUnitId}" type='hidden'/>
		  <input name="goodsId" id="goodsId" value="${obj.goodsId}" type='hidden'/>
		  <input name="specId" id="specId" value="${obj.specId}" type='hidden'/>
		  <input name="goodSpecGroupId" id="goodSpecGroupId" value="${obj.specGroupId}" type='hidden'/>
		  <input name="unitGroupId" id="unitGroupId" value="${obj.unitGroupId}" type='hidden'/>
		  <p>
		    <font>货品编号：</font><input name="goodsCode" _imp="true" _class="goods-combogrid" id="goodsCode" value="${obj.goodsCode}" class="textBox" type="text"/>
		    <font>货品名称：</font><input name="goodsName" _imp="true" id="goodsName" value="${obj.goodsName}" class="textBox" type="text" readonly="readonly"/>
		    <font>记账单位：</font><input name="accountUnitName" _class="unit-combogrid" id="accountUnitName" value="${isDetail ? obj.accountUnitName : obj.accountUnitId}" data-options="{readonly:true,hasDownArrow:false}" class="textBox" type="text"/>
		  </p>
		  <p>
		    <font>记账数量：</font><input name="accountQuentity" id="accountQuentity" value="1" data-options="{required:true,novalidate:true}" readonly="readonly" class="textBox" type="text"/>
		    <font>货品属性：</font><input name="specName"  _class="spec-combogrid" id="specName" value="${obj.specName}" class="textBox" type="text"/>
		  </p>
		  <p>
		    <font>描述：</font><input name="describe" class="textBox" style="width:519px;" value="${obj.describe}" type="text"/>
		  </p>
		  </div>
		</div>
	</div>
	<div id="dataBox" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>添加物料</h2></div>
		<div class="in-box" id="list1">
			<table id="goodsList"></table>
		</div>
	</div>
	

</form>

<div class="mybtn-footer" _id="${obj.fid}"></div>

<div id="scroll1" class="scroll1">
    <div id="scroll2" class="scroll2"></div>
</div>

<div id="pop-win"></div>

<%-- <script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script> --%>
<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<script type="text/javascript">
<c:choose>
<c:when test="${empty obj.details}">
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}');
</c:when>
<c:otherwise>
var details= ${obj.details};
	for(var i in details){
			details[i].goodsSpecName=details[i].specName;
			details[i].goodsSpecId=details[i].specId;
	}
initEdit('${param.flag}','${obj.enable}','bom','bom',details);
</c:otherwise>
</c:choose>
($("#accountUnitName").next())[0].comboObj.disable(); 
</script>
</body>
</html>