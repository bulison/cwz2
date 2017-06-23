<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>资金计划</title>
</head>
<body>
<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
<!-- 从列表页传入参数    flag - edit,detail,copy   billCode - 单据编码 -->
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>


<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn"></span><div id="triangle"></div></div><h1>${billflagName}资金计划信息</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">计划信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写计划信息</h2></div>
		<div class="in-box" id="list2">
		  	<div class="inlist">
			<!-- 
				隐藏的input集中写在这里
				下面以采购入库单为例
			 -->
			<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>	 
			<input id="id" name="id" type="hidden" value="${obj.id}"/>
			<input id="localCache" value="${localCache}" type='hidden'/>
            <input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}"/>
            <input id="relationId" name="relationId" type="hidden" value="${obj.relationId}"/>
            <input id="relationSign" name="relationSign" type="hidden" value="${obj.relationSign==null?0:obj.relationSign}"/>
            <input id="calculation" name="calculation" type="hidden" value="${obj.calculation==null?1:obj.calculation}"/>
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
		        <font>单号：</font><input _imp="true" type="text" id="code" name="code" class="textBox" ${code==null&&obj.code==null?"":"readonly='readonly'"} value="${code==null?(obj.code==null?'':obj.code):code}"/>
			    <font>状态：</font><input type="text" id="_recordStatus" name="_recordStatus" class="textBox" disabled="disabled"/>
			    <font>预计收付款日期：</font><input _imp="true"  type="text" _class="datebox" id="paymentDate" name="paymentDate" class="textBox" value="${obj.paymentDate}" data-options="required:true,novalidate:true"/>
			  </p>
			 <p>
			    <font>计划收付金额：</font><input id="planAmount" name="planAmount" value="${obj.planAmount}" class="textBox" type="text" />
			    <font>单据金额：</font><input id="billAmount" name="billAmount" value="${obj.billAmount}" class="textBox" type="text" />
			    <font>收付款金额：</font><input id="paymentAmount" name="paymentAmount" value="${obj.paymentAmount}" class="textBox" type="text" />
			 </p>
			 <p>
			    <font>说明： </font><input data-options="validType:'maxLength[200]'" style="width:519px;" type="text" id="explain" name="explain" class="textBox easyui-validatebox" value="${obj.explain}"/>
			 </p> 
			 <p>
			 	<font>备注： </font><input data-options="validType:'maxLength[200]'" style="width:519px;" type="text" id="remark" name="remark" class="textBox easyui-validatebox" value="${obj.remark}"/>
			 </p> 
			 <br/><p style="display: inline-block;margin-left:44px">&emsp;其他信息：<img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"></p><br/>
			 <p class="hideOut">
			    <font>准备坏账操作人：</font><input id="badDebtName" class="textBox" disabled="disabled" value="${obj.badDebtName}"/>
			    <font>准备坏账日期：</font><input id="badDebtDate" class="textBox" disabled="disabled" value=""/>
			    <font>完成操作人：</font><input id="completeName" class="textBox" disabled="disabled" value="${obj.completeName}"/>
		     </p>
		     <p class="hideOut">
			    <font>完成日期：</font><input id="completeDate" class="textBox" disabled="disabled" value=""/>
			     <font>制单人：</font><input id="creatorName" class="textBox" disabled="disabled" value="${obj.creatorName}"/>
			    <font>制单时间：</font><input id="createTime" class="textBox" disabled="disabled" value=""/>
		     </p>
			 <p class="hideOut">
			    <font>审核人：</font><input id="auditorName" class="textBox" disabled="disabled" value="${obj.auditorName}"/>
			    <font>审核时间：</font><input id="auditTime" class="textBox" disabled="disabled" value=""/>
			    <font>取消人：</font><input id="cancelorName" class="textBox" disabled="disabled" value="${obj.cancelorName}"/>
		     </p>
			 <p class="hideOut">
			    <font>取消时间：</font><input id="cancelTime" class="textBox" disabled="disabled" value=""/>
			 </p>
			 </div>
		</div>
	</div>
	<div id="dataBox" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>添加明细</h2></div>
		<div class="in-box" id="list1">
			<table id="goodsList"></table>
		</div>
	</div>
</form>
<div class="mybtn-footer" _id="${obj.id}"> </div>
 <div class="repertory"></div>
<div id="scroll1" class="scroll1">
	<div id="scroll2" class="scroll2"></div>
</div>
<div id="opWin" style="display: none">
    <form class="form1">
        <p>
	        <font>制单人：</font><input id="de_creatorName" class="textBox" disabled="disabled" value=""/>
		    <font>制单时间：</font><input id="de_createTime" class="textBox" disabled="disabled" value=""/>
	    </p>
	    <p>
    		<font>审核人：</font><input id="de_auditorName" class="textBox" disabled="disabled" value=""/>
		    <font>审核时间：</font><input id="de_auditTime" class="textBox" disabled="disabled" value=""/>
	    </p>
        <p>
	        <font>准备坏账操作人：</font><input id="de_badDebtName" class="textBox" disabled="disabled" value=""/>
		    <font>准备坏账日期：</font><input id="de_badDebtDate" class="textBox" disabled="disabled" value=""/>
	    </p>
	    <p>
	        <font>完成操作人：</font><input id="de_completeName" class="textBox" disabled="disabled" value=""/>
		    <font>完成日期：</font><input id="de_completeDate" class="textBox" disabled="disabled" value=""/>
	    </p>
	    <p>
	        <font>取消人：</font><input id="de_cancelorName" class="textBox" disabled="disabled" value=""/>
    		<font>取消时间：</font><input id="de_cancelTime" class="textBox" disabled="disabled" value=""/>
    	</p>
    </form>
</div>
<div id="relationWin"></div>

<script type="text/javascript">
var updateTime="${obj.updateTime}";
var flag="";
var recordStatus='${obj.recordStatus}';
var billCode='${param.billCode}';
var billName='${billName}';

$("#badDebtDate").val("${obj.badDebtDate}".slice(0,19));
$("#completeDate").val("${obj.completeDate}".slice(0,19));
$("#createTime").val("${obj.createTime}".slice(0,19));
$("#auditTime").val("${obj.auditTime}".slice(0,19));
$("#cancelTime").val("${obj.cancelTime}".slice(0,19));

if(recordStatus==0){
	flag="edit";
	$("#_recordStatus").val("草稿");
}else if(recordStatus==1){
	flag="detail";
	$("#_recordStatus").val("审核");
}else if(recordStatus==2){
	flag="detail";
	$("#_recordStatus").val("坏账");
}else if(recordStatus==3){
	flag="detail";
	$("#_recordStatus").val("完成");
}else if(recordStatus==4){
	flag="detail";
	$("#_recordStatus").val("取消");
}
$("#planAmount").numberbox({
	width:182,
	height:30,
	precision:2,
	required:true,
	novalidate:true,
	disabled:true,
	validType:'balanceAmount'
})
$("#billAmount").numberbox({
	width:182,
	height:30,
	precision:2,
	required:true,
	novalidate:true,
	disabled:true,
})
$("#paymentAmount").numberbox({
	width:182,
	height:30,
	precision:2,
	required:true,
	novalidate:true,
	disabled:true,
})
<c:choose>
<c:when test="${empty obj.details}">
initEdit(flag,'${obj.recordStatus}','${param.billCode}','资金计划');

</c:when>
<c:otherwise>
initEdit(flag,'${obj.recordStatus}','${param.billCode}','${billName}',${obj.details});
</c:otherwise>
</c:choose>
initGoodsList();
initFooterBtn();

$("#relationWin").window({
	title:"绑定单据",
	width:$(window).width()-10,
	height:$(window).height()-10,
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	href:"",
	closed:true
})

$("#opWin").window({
	title:"操作详情",
	top:50,
	width:600,
	height:300,
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	href:"",
	closed:true
})
</script>
</body>
</html>