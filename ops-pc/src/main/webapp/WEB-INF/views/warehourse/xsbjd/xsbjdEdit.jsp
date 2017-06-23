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
			<div id="square1"><span class="backBtn"></span><div id="triangle"></div></div><h1>${billflagName}${billName}单据</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写单据信息</h2></div>
		<div class="in-box" id="list2">
		    <div class="inlist">
			<input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
			<input id="localCache" value="${localCache}" type='hidden'/>
			<input name="updateTime" id="updateTime" value="${obj.updateTime}" type='hidden'/>

			<input id="customerId" name="customerId" value="${obj.customerId}" type="hidden"/>
			<input name="deptId" id="deptId" value="${obj.deptId}" type='hidden'/>
			<input id="inMemberId" name="inMemberId" value="${obj.inMemberId}" type='hidden'/>
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
				<font>单据日期：</font><input _imp="true" name="billDate" _class="datebox_curr" id="billDate" value="${obj.billDate}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/>
			</p>
			<p>
				<font>客户名称：</font><input _imp="true" id="customerName" name="customerName" _class="customer-combogrid" value="${obj.customerName}" class="textBox" type="text"/>
				<font>客户编号：</font><input _imp="true" id="customerCode" name="customerCode"value="${obj.customerCode}" class="textBox" type="text" readonly="readonly"/>
				<font>客户电话：</font><input id="customerPhone" name="customerPhone" value="${obj.customerPhone}" class="textBox" type="text"/>
			</p>
			<p>
				<font>部门：</font><input _imp="true" name="deptName" _class='deptComBoxTree' id="deptName" value="${isDetail ? obj.deptName : obj.deptId}" class="textBox" type="text"/>
				<font>业务员：</font><input _imp="true" id="inMemberName" name="inMemberName" _class="member-combogrid" value="${obj.inMemberName}" class="textBox" type="text"/>
			</p>
			<p>
				<font>备注：</font><input name="describe" class="easyui-validatebox textBox" style="width:519px;" value="${obj.describe}" data-options="{validType:'maxLength[200]'}" type="text"/>
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

<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<script type="text/javascript">
var texts = [
             {title:'单号',key:'code'},
             {title:'单据日期',key:'billDate'},
             {title:'客户名称',key:'customerName'},
             {title:'客户编号',key:'customerCode'},
             {title:'客户电话',key:'customerPhone'},
             {title:'部门',key:'deptName'},
             {title:'业务员',key:'inMemberName'},
             {title:'单据日期',key:'billDate'},
             {title:'备注',key:'describe',br:true}
             ];
var thead = [
             /* {title:'条码',key:'billCode',subsumStr:'当页小计'}, */
             {title:'编号',key:'goodsCode'},
             {title:'名称',key:'goodsName'},
             {title:'规格',key:'goodsSpec'},
             {title:'属性',key:'goodsSpecName'},
             {title:'单位',key:'unitName'},
             //{title:'数量',key:'quentity',textAlign:'right'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             //{title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe'}
             ];
             
var tfoot = [
				{dtype:0,key:'quentity',text:'当页总数#'},
				{dtype:3,key:'type',text:'当页小计&nbsp;大写&nbsp;#'},
				{dtype:2,key:'type',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#'},
				{dtype:5,key:'type',text:'合计&nbsp;大写&nbsp;#'},
				{dtype:4,key:'type',text:'¥&nbsp;#&nbsp;元'},
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