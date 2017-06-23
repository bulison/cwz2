<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>报损单</title>
<style>
</style>
</head>
<body>
<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
<!-- 从列表页传入参数    flag - edit,detail,copy   billCode - 单据编码 -->
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>
<%-- <c:set var="billName" value="${param.billCode=='bsd'?'报损单':param.billCode=='cgdd'?
							'采购订单':param.billCode=='cgrk'?'采购入库':param.billCode=='cgxjd'?
							'采购询价单':param.billCode=='cprk'?'成品入库':param.billCode=='cptk'?
							'成品退库':param.billCode=='dcd'?'调仓单':param.billCode=='pdd'?
							'盘点单':param.billCode=='qckc'?'期初库存':param.billCode=='scll'?
							'生产领料':param.billCode=='sctl'?'生产退料':param.billCode=='xsbjd'?
							'销售报价单':param.billCode=='xsch'?'销售出货':param.billCode=='xsdd'?
							'销售订单':param.billCode=='xsth'?'销售退货':''}" scope="page"></c:set> --%>
<%-- <%@ include file="/WEB-INF/views/common/header.jsp"%> --%>
<%-- <%@ include file="/WEB-INF/views/common/js.jsp"%> --%>



<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1>${billflagName}${billName}单据</h1><a href="javascript:;" id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写单据信息</h2></div>
		<div class="in-box" id="list2">
			<!-- 
				隐藏的input集中写在这里
				下面以采购入库单为例
			 -->
            <div class="inlist">
			<input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
			<input id="localCache" value="${localCache}" type='hidden'/>
			<input name="inMemberId" id="inMemberId" value="${obj.inMemberId}" type='hidden'/>
			<input name="inWareHouseId" id="inWareHouseId" value="${obj.inWareHouseId}" type='hidden'/>
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
			    <font>仓库：</font><input name="inWareHouseName" _class="warehouse" id="inWareHouseName" value="${isDetail ? obj.inWareHouseName : obj.inWareHouseId}" class="textBox" type="text"/>
			    <font>申报人：</font><input _imp="true" id="inMemberName" name="inMemberName" _class="member-combogrid" value="${obj.inMemberName}" class="memberBox textBox" type="text"/>
			</p>
			<p>
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
<div id="scroll1" class="scroll1">
    <div id="scroll2" class="scroll2"></div>
</div>

<div id="pop-win"></div>

<%-- <script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script> --%>
<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<%-- <script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script> --%>
<script type="text/javascript">
var texts = [
             {title:'原始单号',key:'voucherCode'},
             {title:'单号',key:'code'},
             {title:'仓库名称',key:'inWareHouseName'},
             {title:'申报人',key:'inMemberName'},
             {title:'单据日期',key:'billDate'},
             {title:'备注',key:'describe',br:true}
             ];
var thead = [
             {title:'编号',key:'goodsCode'},
             {title:'名称',key:'goodsName'},
             {title:'规格',key:'goodsSpec'},
             {title:'属性',key:'goodsSpecName'},
             {title:'单位',key:'unitName'},
             {title:'换算关系',key:'scale'},
             {title:'数量',key:'quentity',textAlign:'right'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe'}
             ];
var tfoot = [
				{dtype:0,key:'quentity',text:'当页总数#'},
				{dtype:3,key:'type',text:'当页小计&nbsp;大写&nbsp;#'},
				{dtype:2,key:'type',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#个'},
				{dtype:5,key:'type',text:'合计&nbsp;大写&nbsp;#'},
				{dtype:4,key:'type',text:'¥&nbsp;#&nbsp;元'},
			];
<c:choose>
<c:when test="${empty obj.details}">
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}');
</c:when>
<c:otherwise>
initEdit('${param.flag}','${obj.recordStatus}','bsd','报损单',${obj.details});
</c:otherwise>
</c:choose>
</script>
</body>
</html>