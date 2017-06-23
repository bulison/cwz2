<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
<!-- 从列表页传入参数    flag - edit,detail,copy   billCode - 单据编码 -->
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
			<!-- 
				隐藏的input集中写在这里
				下面以采购入库单为例
			 -->
			<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>	 
			<input id="fid" name="fid" type="hidden" value="${obj.fid}"/>
			<input id="localCache" value="${localCache}" type='hidden'/>
            <input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}"/>
            <input id="inMemberId" name="inMemberId" type="hidden" value="${obj.inMemberId}"/>
            <input id="relationId" name="relationId" type="hidden" value="${obj.relationId}"/>	
			<input name="deptId" id="deptId" value="${obj.deptId}" type='hidden'/>
			<input name="inWareHouseId" id="inWareHouseId" value="${obj.inWareHouseId}" type='hidden'/>
	
			<!-- 
				要输入input集中写在这里，一个<p>标签里放3个180宽的input
				input属性  
				_imp - 设置为true的时候，除easyui外的输入框将自动加上必填验证，并且在文字前加上红色*号。
				_class - 可以设置为easyui的常用输入框，封装在warhouseEdit.js中的editInputInit函数
				_billcode - 此属性是给关联订单输入框专用的，填了哪个订单的billcode就会弹出哪个订单的选择窗口,必须加上relationBox这个类名。关联弹出窗口的input必须加id="relation"
				name必须填写的，且必须与后台数据名字一样
				下面以采购入库单为例
				每一个输入类型的input都必须加上type="text"，这个关联到后面审核后禁用输入框的	
				每个非easyui的input都要加上class="textBox"	
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
				   <font>原始单号：</font><input name="voucherCode" id="voucherCode" value="${obj.voucherCode}" class="textBox" type="text"/>
				   <font>单据日期：</font><input _imp="true" name="billDate" _class="datebox_curr" id="billDate" value="${obj.billDate}" class="textBox" type="text"/>
			 </p>
			 <p>
			    <font>生产计划单：</font><input _billcode='scjhd' name="relation" id="relation" value="${obj.relationName}" class="textBox relationBox" type="text"/><a href="javascript:;" title="清除" class="clearBill" style="margin-left:-20px;"><img style="vertical-align:middle;" src="${ctx}/resources/images/cancel.png"></a>
                <font>部门：</font><input _imp="true" name="deptName" _class='deptComBoxTree' id="deptName" value="${isDetail ? obj.deptName : obj.deptId}" class="textBox" type="text"/>
				<font>出库人：</font><input _imp="true" id="inMemberName" _class='member-combogrid' name="inMemberName" value="${obj.inMemberName}" class="textBox" type="text"/>
			 </p>
			 <p>
			    <font>出仓仓库：</font><input  name="inWareHouseName" _class="warehouse" id="inWareHouseName" value="${isDetail ? obj.inWareHouseName : obj.inWareHouseId}" class="textBox" type="text"/>
			 	<font>备注：</font><input data-options="validType:'maxLength[200]'" style="width:519px;" type="text" id="describe" name="describe" class="textBox" value="${obj.describe}"/>
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
             /*  {title:'单号',key:'code'},
              {title:'原始单号',key:'voucherCode'},
              {title:'单据日期',key:'billDate'},
              {title:'生产领料单',key:'relationName'},
              {title:'部门',key:'deptName'},
              {title:'仓库',key:'inWareHouseName'},
              {title:'出库人',key:'inMemberName'},
              {title:'备注',key:'describe',br:true} */
             {title:'退库人',key:'inMemberName'},
             {title:'NO',key:'code'},
             {title:'日期',key:'billDate'},
              ];
 var thead = [
 				{title:'仓库',key:'inWareHouseName'},
 				{title:'编号',key:'goodsCode',width:8},
 				{title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
 				{title:'单位',key:'unitName'},
 				{title:'数量',key:'quentity'},
 				{title:'备注',key:'describe',width:10},
              ];
              
 var tfoot = [
 				/* {dtype:0,key:'quentity',text:'当页总数#'},
 				{dtype:3,key:'unitPrice',text:'当页小计&nbsp;大写&nbsp;#'},
 				{dtype:2,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
 				{dtype:1,key:'quentity',text:'总数#'},
 				{dtype:5,key:'unitPrice',text:'合计&nbsp;大写金额&nbsp;#'},
 				{dtype:4,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'}, */
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