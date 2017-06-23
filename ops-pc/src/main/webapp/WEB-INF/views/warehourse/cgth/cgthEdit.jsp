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

<title>采购退货</title>
<style>
</style>
<div id="pricing-box"></div>
<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1>${billflagName}${billName}单据</h1><a href="javascript:;" id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
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
             
			<input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
			<input id="localCache" value="${localCache}" type='hidden'/>
			<input name="supplierId" id="supplierId" value="${obj.supplierId}" type="hidden"/>
			<input name="deptId" id="deptId" value="${obj.deptId}" type='hidden'/>
			<input name="inWareHouseId" id="inWareHouseId" value="${obj.inWareHouseId}" type='hidden'/>
			<input name="inMemberId" id="inMemberId" value="${obj.inMemberId}" type='hidden'/>
			<input name="relationId" id="relationId" value="${obj.relationId}" type='hidden'/>
			<input name="updateTime" id="updateTime" value="${obj.updateTime}" type='hidden'/>
			<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>
			
			<!-- 
				要输入input集中写在这里，一个<p>标签里放3个180宽的input
				input属性  
				_imp - 设置为true的时候，除easyui外的输入框将自动加上必填验证，并且在文字前加上红色*号。
				_class - 可以设置为easyui的常用输入框，封装在warhouseEdit.js中的editInputInit函数
				_billcode - 此属性是给关联订单输入框专用的，填了哪个订单的billcode就会弹出哪个订单的选择窗口
				name必须填写的，且必须与后台数据名字一样
				下面以采购入库单为例
				
			 -->
			<p>
				<c:choose>
					<c:when test="${empty code}">
						<font>单号：</font><input _imp="true" name="code" id="code" value="${obj.code}" class="textBox" type="text"/>		
					</c:when>
					<c:otherwise>
						<font>单号：</font><input _imp="true" name="code" id="code" value="${code}" readonly="readonly" class="textBox" type="text"/>	
					</c:otherwise>
				</c:choose>	
				<font>原始单号：</font><input name="voucherCode" id="voucherCode" value="${obj.voucherCode}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/>
				<font>单据日期：</font><input _imp="true" name="billDate" _class="datebox_curr" id="billDate" value="${obj.billDate}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/>
			</p>
			<p>
				<font>采购入库单：</font><input _billCode='cgrk' name="relation" id="relation" value="${obj.relationName}" class="relationBox textBox" type="text" readonly="readonly"/><a href="javascript:;" title="清除" class="clearBill" style="margin-left:-20px;"><img style="vertical-align:middle;" src="${ctx}/resources/images/cancel.png"></a>
			    <font>供应商名称：</font><input _imp="true" id="supplierName" name="supplierName" _class="supplier-combogrid" value="${obj.supplierName}" class="textBox" type="text"/>
				<font>供应商编号：</font><input _imp="true" id="supplierCode" name="supplierCode" value="${obj.supplierCode}" class="textBox" type="text" readonly="readonly"/>
			</p>
			<p>
				<font>供应商电话：</font><input id="supplierPhone" name="supplierPhone" value="${obj.supplierPhone}" class="textBox" type="text"/>
			    <font>部门：</font><input _imp="true" name="deptName" _class='deptComBoxTree' id="deptName" value="${isDetail ? obj.deptName : obj.deptId}" class="textBox" type="text"/>
				<font>采购员：</font><input _imp="true" id="inMemberName" name="inMemberName" _class="member-combogrid" value="${obj.inMemberName}" class="memberBox textBox" type="text"/>
			</p>
			<p>
			    <font>退货仓库：</font><input name="inWareHouseName" _class="warehouse" id="inWareHouseName" value="${isDetail ? obj.inWareHouseName : obj.inWareHouseId}" class="textBox" type="text"/>
				<font>计划退货完成日期：</font><input _imp="true" id="endDate" name="endDate" _class="datebox" value="${obj.endDate}" data-options='{required:true,novalidate:true,validType:"dateCompar[\"#billDate\",-1,\"计划完成日期不能早于单据日期\"]"}' class="textBox" type="text"/>
				<font>优惠金额：</font><input id="freeAmount" name="freeAmount" value="${obj.freeAmount}" class="textBox" type="text" readonly="readonly"/>
			</p>
			<p>
			    <font>合计金额：</font><input id="totalAmount" name="totalAmount" value="${obj.totalAmount}" class="textBox" type="text" readonly="readonly"/>
				<font>备注：</font><input name="describe" class="textBox" style="width:519px;" value="${obj.describe}" type="text"/>
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
	

</form>

<div class="mybtn-footer" _id="${obj.fid}"></div>
 <div class="repertory"></div>
  <div id="addBox"></div>
<div id="scroll1" class="scroll1">
    <div id="scroll2" class="scroll2"></div>
</div>

<div id="pop-win"></div>

<%-- <script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script> --%>
<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<script type="text/javascript">
var texts = [
             {title:'供应商',key:'supplierName'},
             {title:'电话',key:'supplierPhone'},
             {title:'NO',key:'code'},
             {title:' 地<span style="margin:0px 7px;"></span>址',key:'supplierAddress',colspan:2},
             {title:'单据日期',key:'billDate'},
             ];
var thead = [
             {title:'编号',key:'goodsCode',width:8},
             {title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
             {title:'单位',key:'unitName'},
             {title:'数量',key:'quentity'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe',width:10}
             ];
var tfoot = [
				{dtype:3,key:'type',text:'金额合计(大写)：#'},
				{dtype:2,key:'type',text:'小写金额：¥#元'},
			];
<c:choose>
<c:when test="${empty obj.details}">
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}');
</c:when>
<c:otherwise>
initEdit('${param.flag}','${obj.recordStatus}','cgth','采购退货',${obj.details});
</c:otherwise>
</c:choose>

 </script>
</body>
</html>