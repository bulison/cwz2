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
<title>销售返利单</title>
<style>
#dataBox .in-box {
    text-align: center;
}
#list1 p {
    text-align: left;
}
#list1 p.payMsg span{
	margin-left:100px;
}
</style>

<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1>${billflagName}销售返利单单据</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写单据信息</h2></div>
		<div class="in-box" id="list2">
		  	<div class="inlist">
			<input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
			<input id="localCache" value="${localCache}" type='hidden'/>
			<input name="updateTime" id="updateTime" value="${obj.updateTime}" type='hidden'/>
			<input name="customerId" id="customerId" value="${obj.customerId}" type="hidden"/>
			<input name="memberId" id="memberId" value="${obj.memberId}" type='hidden'/>
			<%-- <input name="bankId" id="bankId" value="${obj.bankId}" type='hidden'/> --%>
			<%-- <input name="payeeId" id="payeeId" value="${obj.payeeId}" type='hidden'/> --%>
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
				<font>原始单号：</font><input name="vouchercode" id="voucherCode" value="${obj.vouchercode}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/>
				<font>付款日期：</font><input _imp="true" name="billDate" _class="datebox_curr" id="billDate" value="${obj.billDate}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/>
			</p>
			<p>
				<%-- <font>账号：</font><input _imp="true" id="bankName" name="bankName" _class="bank-combogrid" value="${obj.bankName}${empty obj.bankAccount?null:'('}${empty obj.bankAccount?null:obj.bankAccount}${empty obj.bankAccount?null:')'}" type="text" class="textBox" /> --%>
				<font>对公标识：</font><input _imp="true" id="toPublic" name="toPublic" _class="toPublic-combobox" value="${isDetail?(obj.toPublic==0?'对公':'对私'):obj.toPublic}" class="textBox" type="text" />
				<font>客户名称：</font><input _imp="true" id="customerName" name="customerName" _class="customer-combogrid" value="${obj.customerName}" class="textBox" type="text"/>
				<font class='paye'>受款人：</font><input _imp="true" id="payeeName" name="payeeName"  value="${obj.payeeName}" class="textBox" type="text"/>
			</p>
			<p>
			    <font>付款人：</font><input _imp="true" id="memberName" name="memberName" myrequired="1" _class="mymember-combogrid" value="${obj.memberName}" class="textBox" type="text"/>
				<font>返利金额：</font><input _imp="true" id="amount" name="amount" value="${obj.amount}"  type="text" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/>
				<font>本期销售开始日期：</font><input _imp="true" id="startDate" name="startDate" _class="datebox" value="${obj.startDate}" data-options='{required:true,novalidate:true}' class="textBox" type="text"/>				
			</p>
			<p>				
				<font>本期销售结束日期：</font><input _imp="true" id="endDate" name="endDate" _class="datebox" value="${obj.endDate}" data-options='{required:true,novalidate:true,validType:"dateCompar[\"#startDate\",-1,\"结束日期不能早于开始日期\"]"}' class="textBox" type="text"/>
				<font>本期销售额：</font><input id="sales" name="sales" value="${obj.sales=='0E-8'?'0.00':obj.sales}" class="textBox" type="text" readonly="readonly"/>
				<font>返利率%：</font><input id="rates" name="rates" value="${obj.rates}" class="textBox" type="text" readonly="readonly"/>
			</p>
			<p>				
				<font>备注：</font><input name="describe" class="textBox" value="${obj.describe}" type="text"/>
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
	<!-- <div id="dataBox" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>添加货品</h2></div>
		<div class="in-box" id="list1">
			<table id="goodsList"></table>
		</div>
	</div> -->
	<div id="dataBox" class="shadow" style="display:none;">
					<div class="billTitle"><div id="square2"></div><h2>收款操作</h2></div>
					<div class="in-box" id="list1">
						<input id="sbankId" type="hidden"/>
						<p class="payMsg"><span id="tpay">累计已收款金额：</span><span id="ntpay">累计未收款金额：</span></p>
						<p>
                          <font><em>*</em>收款单号：</font><input id="scode" class="textBox" readonly="readonly"/>
                          <font><em>*</em>收款人：</font><input id="smember" class="textBox"/>				
				          <font><em>*</em>现金/银行：</font><input id="sbankAccount" class="textBox" />
				        </p>
				        <p>
				          <font><em>*</em>收款金额：</font><input id="samount" class="textBox"/>
				        </p>
				        <div style="text-align:center;margin-top:30px;">
				        <input type="button" id="ssave" class="btn-blue2 btn-xs" value="确定" />
				        <input type="button" id="scancel" class="btn-blue2 btn-xs" value="取消" />
				        </div>
					</div>
				</div>

</form>
<div class="mybtn-footer" _id="${obj.fid}">


</div>
<div id="scroll1" class="scroll1">
	<div id="scroll2" class="scroll2"></div>
</div>
<div id="pop-win"></div>

<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<script type="text/javascript">
var texts = [
 			{title:'销售商',key:'customerName'},
 			{title:'电话',key:'customerPhone'},
            {title:'NO',key:'code'},
            {title:'地<span style="margin:0px 7px;"></span>址',key:'customerAddress',colspan:2},
            {title:'日期',key:'billDate'},
            /* {title:'付款方式：',key:'voucherCode',align:'right'}, */
            /*  {title:'采购订单',key:'relationName'},
              
              {title:'供应商编号',key:'supplierCode'},
              {title:'供应商电话',key:'supplierPhone'},
              {title:'部门',key:'deptName'},
              {title:'仓库',key:'inWareHouseName'},
              {title:'采购员',key:'inMemberName'},
              {title:'计划完成日',key:'endDate'},
              {title:'合计金额',key:'totalAmount'}, 
              {title:'免单金额',key:'freeAmount'},
              {title:'备注',key:'describe',br:true} */
              ];
 var thead = [
             /*  {title:'条码',key:'billCode'}, */
             //{title:'仓库',key:'inWareHouseName'},
              {title:'编号',key:'goodsCode',width:8},
              {title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
             /*  {title:'属性',key:'goodsSpecName'}, */
              {title:'单位',key:'unitName'},
              {title:'数量',key:'quentity'},
              {title:'单价',key:'unitPrice',textAlign:'right'},
              {title:'金额',key:'type',textAlign:'right'},
              {title:'备注',key:'describe',width:10}
              ];
              
 var tfoot = [
 				/* {dtype:0,key:'quentity',text:'当页数量#'},
 				{dtype:3,key:'type',text:'当页金额&nbsp;大写&nbsp;#'},
 				{dtype:2,key:'type',text:'¥&nbsp;#&nbsp;元'},
 				{dtype:1,key:'quentity',text:'总数量#'},
 				{dtype:5,key:'type',text:'总金额&nbsp;大写&nbsp;#'},
 				{dtype:4,key:'type',text:'¥&nbsp;#&nbsp;元'}, */
 				{dtype:3,key:'type',text:'金额合计(大写)：#'},
 				{dtype:2,key:'type',text:'小写金额&nbsp;¥#元'},			
 			];
initEdit('${param.flag}','${obj.recordStatus}','${param.billCode}','销售返利单');
var toPublic = "${obj.toPublic}";
var memData="",bankData="";
$.ajax({
	url:"${ctx}/basedata/query?num="+Math.random(),
	async:false,
	data:{param:"Member"},
	success:function(data){
	    memData=formatData(data.Member,"fid");
    }
	});
if('${obj.recordStatus}'==1){
		$.ajax({
			url:getRootPath()+'/bankController/list?num='+Math.random(),
			async:false,
			data:{},
			success:function(data){
				//console.log(data);
				bankData=formatData(data.rows,"fid");
		    }
			});
	}
if('${obj.toPublic}'=='0' && ('${obj.recordStatus}'=="" || '${obj.recordStatus}'=="0")){
	 $('#payeeName').siblings('.paye').children('em').css('display','none');
	 $('#payeeName').validatebox({required:false});
}
//收付款操作输入框初始化
$("#scode").validatebox({
	required:true,
	novalidate:true
});
var boxWidth=182,boxHeight=31;//统一设置搜索下拉输入框的大小
var sfmember = $('#smember').fool('dhxComboGrid',{//收/付款人
	required:true,
	novalidate:true,
	focusShow:true,
	width:boxWidth,
	height:boxHeight,
	data:memData,
	hasDownArrow:false,
	filterUrl:getRootPath()+'/member/list?num='+Math.random(),
	setTemplate:{
		  input:"#username#",
		  columns:[
				{option:'#userCode#',header:'编号',width:100},
				{option:'#jobNumber#',header:'工号',width:100},
				{option:'#username#',header:'名称',width:100},
				{option:'#phoneOne#',header:'电话',width:100},
				{option:'#deptName#',header:'部门',width:100},
				{option:'#position#',header:'职位',width:100},
			     ]
	  }
});
var bankCombo = $('#sbankAccount').fool('dhxComboGrid',{//可优化代码
	required:true, 
	novalidate:true,
	width:182,
	height:30,
	focusShow:true,
	data:bankData,
	hasDownArrow:false,
	filterUrl:getRootPath()+'/bankController/list?num='+Math.random(),
	searchKey:"keyWord",
	setTemplate:{
		input:"#name#(#account#)",
		columns:[
				{option:'#code#',header:'编号',width:40},
				{option:'#name#',header:'名称',width:80},
				{option:'#bank#',header:'银行',width:80},
				{option:'#account#',header:'账号',width:200},
			    ],
	},
	onChange:function(value,text){
		$("#sbankId").val(value);
	}
});


//收款按钮函数
function collection(){
	$("#dataBox").show();
	$('#dataBox #list1 #tpay').html("累计已收款金额：");
	$('#dataBox #list1 #ntpay').html("累计未收款金额：");
	var fid = $("#fid").val();
	$.post("${ctx}/purchaseRebBill/getCodeAndAmount",{fid:fid,type:51},function(data){
		console.log(data);
		if(data.returnCode == 0){
			var amount = data.data.amount;
			if(amount == 0){
				$('#dataBox #list1 p[class!=payMsg]').hide();
				$('#ssave').hide();
			}
			$('#tpay').append(data.data.totalPayAmount);
			$('#ntpay').append(Math.abs(amount));
			$('#scode').val(data.data.code);
			$('#samount').val(-Math.abs(amount));
			$("#samount").numberbox({width:182,height:32,precision:2,required:true,novalidate:true,validType:["samount["+Math.abs(amount)+"]","famount"]});
		}else if(data.returnCode == 1){
			$.fool.alert({msg:data.message});
		}else{
			$.fool.alert({msg:"服务器繁忙，请稍后再试！"});
		}
	});
}

$.extend($.fn.validatebox.defaults.rules, {
	samount:{//验证金额少于等于某个数
        validator: function (value, param) {
        		return Math.abs(parseFloat(value)) <= Math.abs(parseFloat(param[0]));
         },
         message:'金额的绝对值必须小于或等于{0}'
     },
     famount:{//验证金额少于等于某个数
         validator: function (value, param) {
          		return parseFloat(value) < 0;
          },
          message:'金额需小于0'
      }
});

//收款操作的确定和取消
$('#ssave').click(function(){
	$('#list1').form("enableValidation");
	if(!$('#list1').form("validate")){
		return false;
	}
	$('#ssave').attr("disabled","disabled");
	var billId = $('#fid').val();
	var code = $('#scode').val();
	var amount = $('#samount').val();
	var bankId = $('#sbankId').val();
	var memberId = sfmember.getSelectedValue();
	$.post("${ctx}/purchaseRebBill/savePaymentReceived",{code:code,toPublic:toPublic,amount:amount,bankId:bankId,memberId:memberId,billId:billId,billType:51,ckBillType:56},function(data){
		if(data.returnCode == 0){
			$.fool.alert({msg:"保存成功",time:1000,fn:function(){
				$('#ssave').removeAttr("disabled");
				$('#dataBox').hide();
				$('#dataBox').find("input[type!=button]").val("");
			}});
		}else if(data.returnCode == 1){
			if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
    				$('#ssave').removeAttr("disabled");
    			}});
    		}else{
	    		$.fool.alert({msg:data.message,fn:function(){
	    			$('#ssave').removeAttr("disabled");
	    		}});
    		}
		}else{
			$.fool.alert({msg:"服务器繁忙，请稍后再试！"});
			$('#ssave').removeAttr("disabled");
		}
	});
});
$('#scancel').click(function(){
	$('#dataBox').hide();
	$('#dataBox').find("input[type=text]").val("");
});
/* if('${obj.recordStatus}'=="" || '${obj.recordStatus}'=="0"){
	$("#memberName").next()[0].comboObj.setComboText("${obj.memberName}");
	$("#customerName").next()[0].comboObj.setComboText("${obj.customerName}");
} */
</script>

</body>
</html>