<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>收付款弹出窗口</title>
</head>

<body>
<div id="billForm" class="form1">
  <input id="hid-memberId" type="hidden"/>
  <input id="hid-memberName" type="hidden"/>
  <input id="hid-bankId" type="hidden"/>
  <input id="hid-bankName" type="hidden"/>
  <p id="_totalPayAmount"></p>
  <p id="_unpayAmount"></p>
  <p id="_freeAmount"></p>
  <p><font><em>*</em></font><input id="_billCode" disabled="disabled"/></p>
  <p><font><em>*</em></font><input id="_memberName"/></p><br/>
  <p><font><em>*</em>现金银行：</font><input id="_bankName"/></p>
  <p><font><em>*</em></font><input id="_amount"/></p><br/>
  <p><font></font><input type="button" id="ssave" class="btn-blue2 btn-xs" value="确定" /> <input type="button" id="scancel" class="btn-blue2 btn-xs" value="取消" /></p>
</div>
<script type="text/javascript">
console.log("${param.billId}");
console.log("${param.billType}");
  var fid="${param.billId}";
  var type="";
  var caleType="";
  var billType="";
  var amountKey  = 0;
  var billTypeHelper=[
		  {value:11,text:"cgrk"},
		  {value:12,text:"cgth"},
		  {value:15,text:"cgfp"},
		  {value:41,text:"xsch"},
		  {value:42,text:"xsth"},
		  {value:44,text:"xsfp"},
		  {value:24,text:"shd"}
  ]
  for(var i=0;i<billTypeHelper.length;i++){
	  if(billTypeHelper[i].text=="${param.billType}"){
		  billType=billTypeHelper[i].value;
	  }
  }
  if("${param.billType}"=="xsch"||"${param.billType}"=="xsfp"){
	  $("#_billCode").prev().append("收款单号：");
	  $("#_memberName").prev().append("收款人：");
	  $("#_amount").prev().append("收款金额：");
	  $("#_amount").textbox({
		  required:true,
		  novalidate:true,
		  width:160,
		  height:30,
		  validType:"amount"
	  })
	  type=51;
	  caleType=1;
  }else if("${param.billType}"=="xsth"){
	  $("#_billCode").prev().append("收款单号：");
	  $("#_memberName").prev().append("收款人：");
	  $("#_amount").prev().append("收款金额：");
	  $("#_amount").textbox({
		  required:true,
		  novalidate:true,
		  width:160,
		  height:30,
		  validType:"minus"
	  })
	  type=51;
	  caleType=2;
	  amountKey  = 1;
  }else if("${param.billType}"=="cgrk"||"${param.billType}"=="cgfp"||"${param.billType}"=="shd"){
	  $("#_billCode").prev().append("付款单号：");
	  $("#_memberName").prev().append("付款人：");
	  $("#_amount").prev().append("付款金额：");
	  $("#_amount").textbox({
		  required:true,
		  novalidate:true,
		  width:160,
		  height:30,
		  validType:"amount"
	  });
	  type=52;
	  caleType=1;
  }else if("${param.billType}"=="cgth"){
	  $("#_billCode").prev().append("付款单号：");
	  $("#_memberName").prev().append("付款人：");
	  $("#_amount").prev().append("付款金额：");
	  $("#_amount").textbox({
		  required:true,
		  novalidate:true,
		  width:160,
		  height:30,
		  validType:"minus"
	  })
	  type=52;
	  caleType=2;
	  amountKey  = 1;
  }
  $("#_billCode").textbox({
	  width:160,
	  height:30,
	  readonly:true
  })
  $("#_memberName").fool('combogrid',{//收/付款人
		required:true,
		novalidate:true,
		focusShow:true,
		width:160,
		height:30,
		idField:'fid',
		textField:'username',
		panelWidth:450,
		fitColumns:true,
		validType:["combogridValid['hid-memberId']","nocobgValid['hid-memberName']"],
		url:getRootPath()+'/member/list',
		columns:[[
			{field:'fid',title:'fid',hidden:true},
			{field:'userCode',title:'编号',width:100,searchKey:false},
			{field:'jobNumber',title:'工号',width:100,searchKey:false},
			{field:'username',title:'名称',width:100,searchKey:false},
			{field:'phoneOne',title:'电话',width:100,searchKey:false},
			{field:'deptName',title:'部门',width:100,searchKey:false},
			{field:'position',title:'职位',width:100,searchKey:false},
			{field:'searchKey',hidden:true,searchKey:true},
		          ]],
		onSelect:function(index, row){
			$("#hid-memberId").val(row.fid);
			$("#hid-memberName").val(row.username);
			$("#_memberName").combogrid("validate");
		}
  });
  $("#_bankName").fool('combogrid',{//可优化代码
		required:true, 
		novalidate:true,
		width:160,
		height:30,
		idField:'fid',
		textField:'name',
		validType:["combogridValid['hid-bankId']","nocobgValid['hid-bankName']"],
		fitColumns:true,
		focusShow:true,
		panelWidth:350,
		panelHeight:283,
		url:getRootPath()+'/bankController/list',
		columns:[[
			{field:'fid',title:'fid',hidden:true},
			{field:'code',title:'编号',width:100,searchKey:false},
			{field:'name',title:'名称',width:100,searchKey:false},
			{field:'bank',title:'银行',width:100,searchKey:false},
			{field:'account',title:'账号',width:200,searchKey:false},
			{field:'keyWord',hidden:true,searchKey:true},
		          ]],
		onSelect:function(index, row){
			$("#hid-bankId").val(row.fid);
			$("#hid-bankName").val(row.name); 		
			$("#_bankName").combogrid("validate");
		}
  });
  $.ajax({
	  type : 'post',
	  url : getRootPath()+'/warehouse/${param.billType}/getCodeAndAmount',
	  data :{type:type,fid:fid,caleType:caleType},
	  dataType : 'json',
	  success : function(data) {
		  var amount = data.data.amount;
		  $("#_billCode").textbox("setValue",data.data.code);
		  if(amountKey == 1){
			  $("#_amount").textbox({
				  width:160,
				  height:30,
				  validType:["minus","samount["+Math.abs(amount)+"]"]
		  	  });
		  }else{
			  $("#_amount").textbox({
				  width:160,
				  height:30,
				  validType:["amount","samount["+Math.abs(amount)+"]"]
		  	  });
		  }
		  $("#_amount").textbox("setValue",amountKey == 1?-Math.abs(amount):Math.abs(amount));
		  $("#_totalPayAmount").append("<font>累计收付款金额：</font>"+data.data.totalPayAmount);
		  $("#_unpayAmount").append("<font>未收付款金额：</font>"+Math.abs(amount));
		  $('#freeAmount').length>0?$("#_freeAmount").append("<font>优惠金额：</font>"+data.data.freeAmount):null;
		  
	  },
  });
  
  $.extend($.fn.validatebox.defaults.rules, {
		samount:{//验证金额少于等于某个数
	        validator: function (value, param) {
	        		return Math.abs(parseFloat(value)) <= Math.abs(parseFloat(param[0]));
	         },
	         message:'金额必须小于或等于{0}'
	     }
	});
//收付款操作的确定和取消
  $('#ssave').click(function(){
	  $('#billForm').form("enableValidation");
  	  if(!$('#billForm').form("validate")){
  		  return false;
  	  }
  	  $('#ssave').attr("disabled","disabled");
  	  var code = $("#_billCode").textbox("getValue");
  	  var amount = $("#_amount").textbox("getValue");
  	  var bankId = $("#_bankName").combogrid("getValue");
  	  var memberId = $('#_memberName').combogrid("getValue");
  	  var _url=getRootPath()+'/warehouse/${param.billType}/savePaymentReceived';
  	  $.post(_url,{code:code,amount:amount,bankId:bankId,memberId:memberId,billId:fid,billType:type,ckBillType:billType,caleType:caleType},function(data){
  		dataDispose(data);
  		  if(data.result == 0){
  			  $.fool.alert({msg:"保存成功",time:1000,fn:function(){
  				  $("#pop-win").window("close");
  				  $('#ssave').removeAttr("disabled");
  			  }});
  		  }else if(data.result == 1){
  			  $.fool.alert({msg:data.msg});
  			  $('#ssave').removeAttr("disabled");
  		  }else{
  			  $.fool.alert({msg:"服务器繁忙，请稍后再试！"});
  			  $('#ssave').removeAttr("disabled");
  		  }
  	  });
  });
  $('#scancel').click(function(){
	  $("#pop-win").window("close");
  });
</script>
</body>
</html>
