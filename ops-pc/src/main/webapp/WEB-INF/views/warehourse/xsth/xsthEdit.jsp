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


<div id="pricing-box"></div>
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
			<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>	 
			<input id="fid" name="fid" type="hidden" value="${obj.fid}"/>
			<input id="localCache" value="${localCache}" type='hidden'/>
            <input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}"/>
            <input id="inMemberId" name="inMemberId" type="hidden" value="${obj.inMemberId}"/>
            <input id="inWareHouseId" name="inWareHouseId" type="hidden" value="${obj.inWareHouseId}"/>
            <input id="relationId" name="relationId" type="hidden" value="${obj.relationId}"/>	         
            <input id="customerId" name="customerId" type="hidden" value="${obj.customerId}"/>  
            <input id="supplierId" type="hidden" name="supplierId" value="${obj.supplierId}"/>        
            <input id="deptId" type="hidden" name="deptId" value="${obj.deptId}"/>
			<!-- 
				要输入input集中写在这里，一个<p>标签里放3个180宽的input
				input属性  
				_imp - 设置为true的时候，除easyui外的输入框将自动加上必填验证，并且在文字前加上红色*号。
				_class - 可以设置为easyui的常用输入框，封装在warhouseEdit.js中的editInputInit函数
				_billcode - 此属性是给关联订单输入框专用的，填了哪个订单的billcode就会弹出哪个订单的选择窗口,必须加上relationBox这个类名。关联弹出窗口的input必须加id="relation"
				name必须填写的，且必须与后台数据名字一样
				下面以采购入库单为例
				每一个输入类型的input都必须加上type="text"，这个关联到后面审核后禁用输入框的	
				每个非easyui的输入类型的input都要加上textBox			
			 -->
			  <p>
			     <font>单号：</font><input id="code" type="text"  class="textBox" ${code==null&&obj.code==null?"":"readonly='readonly'"} value="${code==null?(obj.code==null?'':obj.code):code}" name="code" />			
                 <font>原始单号：</font><input data-options="validType:'maxLength[50]'" type="text" id="voucherCode" name="voucherCode" class="textBox" value="${obj.voucherCode}"/>
				 <font>单据日期：</font><input id="billDate" _class="datebox_curr" name="billDate" type="text" class="textBox" value="${obj.billDate}"/>
			</p>
			 <p>
			 	<font>销售出货单：</font><input id="relation" _billcode="xsch" value="${obj.relationName}" class="relationBox textBox" type="text"/><a href="javascript:;" title="清除" class="clearBill" style="margin-left:-20px;"><img style="vertical-align:middle;" src="${ctx}/resources/images/cancel.png"></a>
			    <font>客户名称：</font><input _imp="true" id="customerName" _class="customer-combogrid" name="customerName" value="${obj.customerName}" class="textBox" type="text"/>
				<font>客户编号：</font><input  _imp="true" id="customerCode" readonly="readonly" name="customerCode" value="${obj.customerCode}" class="textBox" type="text"/>
			</p>
			 <p>
			 	<font>客户电话：</font><input id="customerPhone" name="customerPhone" readonly="readonly" class="textBox"  value="${obj.customerPhone}" type="text"/>
			    <font>部门：</font><input _imp="true" id="deptName" _class="deptComBoxTree" name="deptName" class="textBox" value="${isDetail ? obj.deptName : obj.deptId}" type="text"/>
				<font>业务员：</font><input _imp="true" id="inMemberName" _class='member-combogrid' name="inMemberName" value="${obj.inMemberName}" class="textBox" type="text"/>
			</p>
			<p>
				<font>入仓仓库：</font><input id="inWareHouseName" _class="warehouse" name="inWareHouseName" class="textBox" value="${isDetail ? obj.inWareHouseName : obj.inWareHouseId}" type="text"/>
				<font>计划付款日期：</font><input _imp="true" _class="datebox" id="planEnd" name="endDate" class="textBox" value="${obj.endDate}" type="text" data-options='{required:true,novalidate:true,validType:"dateCompar[\"#billDate\",-1,\"计划完成日期不能早于单据日期\"]"}'/>			
				<font>优惠金额：</font><input id="freeAmount" name="freeAmount" class="textBox" readonly="readonly" value="${mark==1?0:obj.freeAmount}" type="text"/>
			 </p>
			 <p>
			 	<font>合计金额：</font><input id="totalAmount" name="totalAmount" class="textBox" readonly="readonly" value="${obj.totalAmount}" type="text"/>
			 	<font>备注：</font><input data-options="validType:'maxLength[200]'" style="width:519px;" type="text" id="describe" name="describe" class="textBox" value="${obj.describe}" type="text"/>
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
 			{colspan:2,text:'<div style="margin-right:60px;font-size:20px;font-weight: bold;">[销售退货单]</div>',textAlign:'right'},
              {title:'NO',key:'code'},
              {title:'销售商',key:'customerName',br:true},
              {title:'电<span style="margin:0px 9px;"></span>话',key:'customerPhone'},
              {title:'业<span style="margin:0px 4px;"></span>务<span style="margin:0px 4px;"></span>员',key:'inMemberName'},
              {title:'地<span style="margin:0px 7px;"></span>址',key:'customerAddress',br:true},
              {title:'联系人',key:'supplierContact'},
              {title:'送货日期',key:'billDate'}
              /* {title:'单据日期',key:'billDate'},
              {title:'计划完成日期',key:'endDate'},
              {title:'备注',key:'describe',br:true} */
              ];
 var thead = [
 			{title:'仓库',key:'inWareHouseName',width:8},
              {title:'编号',key:'goodsCode'},
              {title:'产品名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
              {title:'单位',key:'unitName'},
              {title:'数量',key:'quentity'},
              {title:'单价',key:'unitPrice',textAlign:'right'},
              {title:'金额',key:'type',textAlign:'right'},
              {title:'备注',key:'describe',width:10}
              ];
 var tfoot = [
 				/* {dtype:0,key:'quentity',text:'当页总数#个'},
 				{dtype:3,key:'unitPrice',text:'当页总金额&nbsp;大写&nbsp;#'},
 				{dtype:2,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
 				{dtype:1,key:'quentity',text:'总数#个'},
 				{dtype:5,key:'unitPrice',text:'总金额&nbsp;大写&nbsp;#'},
 				{dtype:4,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'}, */
 				{dtype:3,key:'type',text:'金额合计(大写)：#'},
 				{dtype:2,key:'type',text:'小写金额&nbsp;¥#元'},			
 				/* {dtype:4,key:'type',text:'收款金额&nbsp;¥#元',br:true,preNull:true}, */
 			];
<c:choose>
<c:when test="${empty obj.details}">
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}');
</c:when>
<c:otherwise>
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','${billName}',${obj.details});
</c:otherwise>
</c:choose>


</script>

</body>
</html>