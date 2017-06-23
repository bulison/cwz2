<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<html>
<head>
</head>
<body>
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>

<style>
body{overflow: hidden;}
.redRow{color:red;}
/* .mybtn-footer{ border: 1px solid red; } */
.mybtn-footer p{float: right;}
.mybtn-footer p input{ background: #50B3E7; color:#fff;border-radius:4px; font-size:16px; line-height: 30px;margin-right: 20px}
.myform .textBox{height: 30px;}
</style>
              <form id="form" class="bill-form myform">
              <div id="title" >
		         <div id="title1" class="shadow" style="margin:10px 0px 0 0;padding:11px 0; height:18px;">
			         <div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1 style="margin-top:2px;">${billflagName}付款单单据</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		         </div>
	          </div>
	          <div id="bill" class="shadow" style="margin-top:50px;">
		         <div class="billTitle myTitle"><div id="square2"></div><h2 style="margin-top: -2px;">填写单据信息</h2></div>
		         <div class="in-box" id="list2">
		         <div class="inlist">
                <input id="fid" type="hidden" value="${obj.fid}"/>
                <input id="updateTime" type="hidden" value="${obj.updateTime}"/>
                <input id="supplierId" type="hidden" value="${obj.supplierId}"/>
                <input id="memberId" type="hidden" value="${obj.memberId}"/>
                <input id="bankId" type="hidden" value="${obj.bankId}"/>
                <input id="recordStatus" type="hidden" value="${obj.recordStatus}" />
                
                <p>
                   <font><em>*</em>单号：</font><input id="code" class="textBox" value="${obj.code}"/>
                   <font>原始单号：</font><input id="vouchercode" class="textBox" value="${obj.vouchercode}"/>
				   <font><em>*</em>单据日期：</font><input id="billDate" class="textBox" value="${obj.billDate}"/>
				 </p>
				<p>
				   <font>供应商名称：</font><input id="supplierName" class="textBox" value="${obj.supplierName}"/>
				   <font><em>*</em>账号：</font><input id="bankAccount" class="textBox" value="${obj.bankName}${empty obj.bankAccount?null:'('}${empty obj.bankAccount?null:obj.bankAccount}${empty obj.bankAccount?null:')'}"/>
				   <font><em>*</em>付款人：</font><input id="memberName" class="textBox" value="${obj.memberName}"/>
				</p>
				<p>
				  <font><em>*</em>金额：</font><input id="amount" class="textBox" value="${obj.amount}"/>
				  <font>已对单金额：</font><input id="totalCheckAmount" class="textBox" readonly="readonly" value="${obj.totalCheckAmount}"/>
				  <font>未对单金额：</font><input id="mytotalUnCheckAmount" class="textBox" readonly="readonly" value="${obj.totalUnCheckAmount}"/>
				</p>
				<p><font>备注：</font><input id="describe" class="textBox" style="width:700px;" value="${obj.describe}"/></p><br/>
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
                  <div class="billTitle"><div id="square2"></div><h2>对单记录</h2></div>
		          <div class="in-box" id="list1">
		              <table id="goodsList"></table>
		          </div>
	            </div>
              </form>
              <div id="checkBox"></div>
              <div class="mybtn-footer" _id="${obj.fid}"></div>
<script type="text/javascript">
    if("${param.flag}"=='copy'){
        $('#vouchercode').val("");
    }
//打印参数配置
var texts = [
             {title:'原始单号',key:'vouchercode'},
             {title:'单号',key:'code'},
             {title:'单据日期',key:'billDate'},
             {title:'供应商名称',key:'supplierName'},
             {title:'账号',key:'bankAccount'},
             {title:'付款人',key:'memberName'},
             {title:'金额',key:'amount'},
             {title:'备注',key:'describe',br:true}
             ];
             
var thead = [
             ];

var allBillType=[
                 /* {value:"11",text:"采购入库单"},
                 {value:"12",text:"采购退货单"},
                 {value:"15",text:"采购发票"},
                 {value:"55",text:"采购返利单"},
                 {value:"53",text:"费用单"},
                 {value:"52",text:"付款单"}, */
                 {value:"0",text:"基础业务"},
                 {value:"91",text:"期初库存"},
      			 {value:"92",text:"期初应付"},
      			 {value:"93",text:"期初应收"},
      			 {value:"10",text:"采购订单"},
      			 {value:"11",text:"采购入库"},
      			 {value:"12",text:"采购退货"},
      			 {value:"13",text:"采购询价单"},
      			 {value:"14",text:"采购申请单"},
      			 {value:"15",text:"采购发票"},
      			 {value:"20",text:"盘点单"},
      			 {value:"21",text:"调仓单"},
      			 {value:"22",text:"报损单"},
      			 {value:"30",text:"生产领料"},
      			 {value:"32",text:"生产退料"},
      			 {value:"31",text:"成品入库"},
      			 {value:"33",text:"成品退库"},
      			 {value:"40",text:"销售订单"},
      			 {value:"41",text:"销售出货"},
      			 {value:"42",text:"销售退货"},
      			 {value:"43",text:"销售报价单"},
      			 {value:"44",text:"销售发票"},
      			 {value:"53",text:"费用单"},
      			 {value:"51",text:"收款单"},
      			 {value:"53",text:"费用单"},
      			 {value:"52",text:"付款单"},
      			 {value:"34",text:"生产计划单"},
      			 {value:"54",text:"费用申请单"},
      			 {value:"55",text:"采购返利单"},
      			 {value:"56",text:"销售返利单"},
      			 {value:"110",text:"工资单"},
      			 {value:"120",text:"固定资产"},
      			 {value:"130",text:"待摊费用"},
      			 {value:"24",text:"收货单"},
	]

var checker="";
var myStat = "${obj.recordStatus}";
$('.inlist').addClass('status'+myStat);//加上是否审核作废的状态图标
var vo1 = {};
var next = "<p><input id=\"nextSave\" type='button' onclick='nextsave()' style='width:auto !important;' class=\"btn-blue2 btn-xs\" value=\"下一张单\" /></p>";
var nextNew = "<p><input id=\"nextSave\" type='button' onclick='nextsave()' style='width:auto !important;' class=\"btn-blue2 btn-xs\" value=\"保存并新增\" /></p>";
if("${param.mark}" == "1"){
	$('#mytotalUnCheckAmount').val("");
}

var memData="",supData="",bankData="";
$.ajax({
	url:"${ctx}/basedata/query?num="+Math.random(),
	async:false,
	data:{param:"Member,Supplier"},
	success:function(data){
	    memData=formatData(data.Member,"fid");
	    supData=formatData(data.Supplier,"fid");
    }
	});
$.ajax({
	url:getRootPath()+'/bankController/list?num='+Math.random(),
	async:false,
	data:{},
	success:function(data){
		bankData=formatData(data.rows,"fid");
    }
	});

function checkStatus(){
	if($('#recordStatus').val()==0){
		$(".mybtn-footer").html('<fool:tagOpt optCode="fkdAction1"><p><input type="button" onclick="save()" id="save" class="btn-blue2 btn-xs" value="保存" /></p></fool:tagOpt> <fool:tagOpt optCode="fkdAction3"><p><input type="button" id="copy" onclick="copy()" class="btn-blue2 btn-xs" value="复制" /></p></fool:tagOpt> <p><input type="button" onclick="print()" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" onclick="refreshForm()" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p>'+next);
	}
}

//控件初始化
validateBox($("#vouchercode"));
validateBox($("#code"),true);
var today=new Date();
var todayStr=today.getFullYear()+"-"+(today.getMonth()+1)+"-"+today.getDate();
if("${obj.billDate}"){
	dateBox($("#billDate"),true,"${obj.billDate}");
}else{
	dateBox($("#billDate"),true,todayStr);
}
//validateBox($("#supplierName"),true);
/* comboBox($("#bankAccount"),"${ctx}/bankController/comboboxData",true,function(){
	$("#bankAccount").combobox("setValue","${obj.bankId}");
}); */
var bankCombo = $('#bankAccount').fool('dhxComboGrid',{
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
				{option:'#code#',header:'编号',width:80},
				{option:'#name#',header:'名称',width:80},
				{option:'#bank#',header:'开户行',width:80},
				{option:'#account#',header:'账号',width:200},
			    ]
	},
	toolsBar:{
	    name:"银行账号",
		addUrl:"/bankController/bankManager",
		refresh:true
	},
	onChange:function(value,text){
		value!=null?$("#bankId").val(value):null;
	},
	text:"${obj.bankName}${empty obj.bankAccount?'':'('}${empty obj.bankAccount?'':obj.bankAccount}${empty obj.bankAccount?'':')'}"
});
//validateBox($("#memberName"),true);
validateBox($("#describe"));
validateBox($("#amount"),true,['balanceAmount','numMaxLength[10]']);

//对单记录表
if("${obj.recordStatus}"==1||"${obj.recordStatus}"==2){
	$("#goodsList").jqGrid({
		url:"${ctx}/paymentCheck/checkedList?paymentBillId=${obj.fid}",
		datatype:"json",
		forceFit:true,
		viewrecords:true,
		autowidth:true,//自动填满宽度
		height:"100%",
		loadComplete: function(data){   
			var row = data.rows;
			for(var i = 0 ; i<row.length; i++){
				if (row[i].detal=="-1"){
					$("#goodsList #"+(i+1)).find("td").addClass("redRow"); 
				}
			}
		},
		colModel:[
		          {name:'fid',label:'fid',align:"center",hidden:true,width:100}, 
		          {name:'checkDate',label:'对单日期',align:"center",width:100,formatter:function(value){
					  if(value){
						  return value.substring(0,10);
					  }else{
						  return value;
					  }
				  }},
				  {name:'csvName',label:'收支单位',align:"center",width:120},
				  {name:'billType',label:'单据类别',align:"center",width:100,formatter:function(value,options,row){
					  for(var i=0; i<allBillType.length; i++){
						  if (allBillType[i].value == value){
							  if(row.expenseType==1){
								  return allBillType[i].text+'(增加)';
							  }else if(row.expenseType==2){
								  return allBillType[i].text+'(减少)';
							  }else{
								  return allBillType[i].text;
							  }
						  } 
					  }
					  return value;
				  }},
				  {name:'billDate',label:'单据日期',align:"center",width:100,formatter:function(value){
					  if(value){
						  return value.substring(0,10);
					  }else{
						  return value;
					  }
				  }},
				  {name:'billCode',label:'单号',align:"center",width:120},
				  {name:'amount',label:'对单金额',align:"center",width:100},
				  {name:'freeAmount',label:'优惠金额',align:"center",width:100},
				  {name:'describe',label:'备注',align:"center",width:100},
				  ]
	});
}

//选取供应商、收款人
/* var chooserWindow="";
$("#supplierName").click(function(){
	chooserWindow=$.fool.window({width:780,height:480,'title':"选择供应商",href:'${ctx}/supplier/window?okCallBack=selectSupplier&onDblClick=selectSupplierDBC&singleSelect=true'}); 
});
$("#memberName").click(function(){
	chooserWindow=$.fool.window({width:780,height:480,'title':"选择付款人",href:'${ctx}/member/window?okCallBack=selectUser&onDblClick=selectUserDBC&singleSelect=true'});
}); */

//返回按钮指向链接
$('#square1 .backBtn').click(function(){
	$("#addBox").window("close");	
});

//保存
function save(){
	var fid=$("#fid").val();
	var updateTime=$("#updateTime").val();
	var vouchercode=$("#vouchercode").val();
	var code=$("#code").val();
	var billDate=$("#billDate").datebox("getValue");
	var supplierId=$("#supplierId").val();
	var bankId=$("#bankId").val();
	var memberId=$("#memberId").val();
	var amount=$("#amount").val();
	var describe=$("#describe").val();
	$('#form').form('enableValidation'); 
		if($('#form').form('validate')){
			$('#save').attr("disabled","disabled");
			    $.post('${ctx}/payBill/save',{billType:52,fid:fid,updateTime:updateTime,vouchercode:vouchercode,code:code,billDate:billDate,supplierId:supplierId,bankId:bankId,memberId:memberId,amount:amount,describe:describe},function(data){
			    	dataDispose(data);
			    	if(data.result=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			if($('#fid').val()==''){
			    				$('#fid').val(data.dataExt.fid);
			    				$('#updateTime').val(data.dataExt.updateTime);
			    				$('#recordStatus').val(0);
			    				checkStatus();
			    				vo1 = loadedVo();
			    			}else{
			    				$("#addBox").window("close");
			    			}
			    			//$('#addBox').window('close');
			    			/* window.location.href="${ctx}/payBill/manage"; */
			    			$('#save').removeAttr("disabled");
			    			$('#billList').trigger("reloadGrid");
			    		}});
			    	}else if(data.result=='1'){
			    		if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
			    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.msg,fn:function(){
			    			}});
			    		}else{
				    		$.fool.alert({msg:data.msg,fn:function(){
				    		}});
			    		}
			    		$('#save').removeAttr("disabled");
		    		}else{
		    			$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
			    		$('#save').removeAttr("disabled");
		    		}
			    	return true;
			    });
			}else{
				return false;
				}
};

//审核
$('#verify').click(function(e) {
	 $.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/payBill/passAudit',
					data : {fid : $("#fid").val()},
					dataType : 'json',
					success : function(data) { 
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
								$('#billList').trigger("reloadGrid");
								$('#addBox').window("refresh");
							}});
						}else{
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.msg,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.msg,fn:function(){
					    		}});
				    		}
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});

});

//打印
function print(){
	printHandle("${ctx}/payBill/fkdPrint?id="+$("#fid").val(),0);
};

//刷新
function refreshForm(){
	/* if(checker!=""){
		$("#checkBox").window("destroy");
	}
	$('#addBox').window("refresh"); */
	
	var url = '';
	var flag = '';
	if($('#title1 h1').text().search(/新增/)!=-1 || $('#title1 h1').text().search(/复制/)!=-1 || $('#title1 h1').text().search(/编辑/)!=-1){
		flag = 'edit';
	}else{
		flag = 'detail';
	}
	var value = $('#fid').val(); 
	url='${ctx}/payBill/edit?flag='+flag+'&fid='+value;
	/* $("#checkBox").window("destroy"); */
	if($("#checkBox").length>0 && $('#checkBox').html()){//修复了刷新单据再按对单时对单弹窗异常的问题
		$("#checkBox").window("destroy");
	}
	$('#addBox').window("refresh",url);
};

//作废
$('#cancel').click(function(e) {
	 $.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/payBill/cancel',
					data : {fid : $("#fid").val()},
					dataType : 'json',
					success : function(data) { 
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
								$('#billList').trigger("reloadGrid");
								$('#addBox').window("refresh");
							}});
						}else{
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.msg,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.msg,fn:function(){
					    		}});
				    		}
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
});

//复制
function copy(){
	if(checker!=""){
		$("#checkBox").window("destroy");
	}
	var fid=$("#fid").val();
	$('#addBox').window("refresh","${ctx}/payBill/edit?fid="+fid+"&mark=1&flag=copy");
	/* window.location.href="${ctx}/payBill/edit?fid="+fid+"&mark=1&flag=copy"; */
};

//对单
function checkr(){
	 checker=$("#checkBox").window({
		title:'对单单据',
		top:50,  
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/paymentCheck/manage?paymentBillId='+$("#fid").val()+"&mark=fkd",
		width:$(window).width()-10,
		height:$(window).height()-60,
	});
	/* window.location.href='${ctx}/paymentCheck/manage?paymentBillId='+$("#fid").val()+"&mark=fkd" */
};


/* //显示、隐藏详细
$("#openBtn").click(
		function(e) {
			$(".hideOut").css("display","inline-block");
			$('#openBtn').css("display","none");
			$('#closeBtn').css("display","inline-block");
		});
$("#closeBtn").click(
		function(e) {
			$(".hideOut").css("display","none");
			$('#openBtn').css("display","inline-block");
			$('#closeBtn').css("display","none");	
		});
		 */
//选取供应商、收款人的方法		
/* function selectSupplier(rowData){
	$("#supplierName").focus();
	$("#supplierId").val(rowData[0].fid);
	$("#supplierName").val(rowData[0].name);
	$("#memberId").val(rowData[0].memberId);
	$("#memberName").val(rowData[0].memberName);
	chooserWindow.window('close');
}
function selectSupplierDBC(rowData){
	$("#supplierName").focus();
	$("#supplierId").val(rowData.fid);
	$("#supplierName").val(rowData.name);
	$("#memberId").val(rowData.memberId);
	$("#memberName").val(rowData.memberName);
	chooserWindow.window('close');
}
function selectUser(rowData){
	$("#memberName").focus();
	$("#memberId").val(rowData[0].fid);
	$("#memberName").val(rowData[0].username);
	chooserWindow.window('close');
}
function selectUserDBC(rowData){
	$("#memberName").focus();
	$("#memberId").val(rowData.fid);
	$("#memberName").val(rowData.username);
	chooserWindow.window('close');
} */

var boxWidth=182,boxHeight=31;//统一设置输入框大小
var memCombo = $('#memberName').fool('dhxComboGrid',{//付款人
	required:true,
	novalidate:true,
	focusShow:true,
	width:boxWidth,
	height:boxHeight,
	data:memData,
	hasDownArrow:false,
	filterUrl:getRootPath()+'/member/list',
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
	  },
	toolsBar:{
	    name:"付款人",
		addUrl:"/member/memberManager",
		refresh:true
	},
	onChange:function(value,text){
		value!=null?$("#memberId").val(value):null;
	},
	text:"${obj.memberName}"
});

var supCombo = $('#supplierName').fool('dhxComboGrid',{//供应商
	//required:true, 
	novalidate:true,
	focusShow:true,
	width:boxWidth,
	height:boxHeight,
	hasDownArrow:false,
	data:supData,
	filterUrl:getRootPath()+'/supplier/vagueSearch?searchSize=8',
	setTemplate:{
		  input:"#name#",
		  columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				  ]
	},
	toolsBar:{
	  name:"供应商",
		addUrl:"/supplier/manage",
		refresh:true
	},
	onChange:function(value,text){
		value!=null?$("#supplierId").val(value):null;
	},
	text:"${obj.supplierName}"
});

//设置鼠标放进去自动出来下拉列表
$("#form").find("input[_class]").each(function(i,n){($(this));});
/* if($('#memberName').length > 0)//付款人
$('#memberName').combogrid('textbox').focus(function(){
	$('#memberName').combogrid('showPanel');
});
if($('#supplierName').length > 0)//供应商
 $('#supplierName').combogrid('textbox').focus(function(){
	$('#supplierName').combogrid('showPanel');
}); */

//控件初始化
function validateBox(obj,required,validType){
	obj.validatebox({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:180,
		height:32,
		validType:validType
	});
}
function comboBox(obj,url,required,onLSFn){
	obj.combobox({
		valueField:"fid",
		textField:"name",
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		url:url,
		width:180,
		height:32,
		editable:false,
		onLoadSuccess:onLSFn
	});
}

console.log()

function dateBox(obj,required,value){
    if ('${obj.recordStatus}' == '1' || '${obj.recordStatus}' == '2'){
        obj.datebox({
            disabled:true
        })
    };
	obj.datebox({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:180,
		height:32,
		editable:false,
		value:value
	});
}
enterController('form');
//详细信息折叠按钮
$("#openBtn").click(
		function(e) {
			$(".hideOut").css("display","inline-block");
			$('#openBtn').css("display","none");
			$('#closeBtn').css("display","inline-block");
		});
$("#closeBtn").click(
		function(e) {
			$(".hideOut").css("display","none");
			$('#openBtn').css("display","inline-block");
			$('#closeBtn').css("display","none");	
		});
$(function(){
	$("input").attr('autocomplete','off');
	//判断有无订单生成规则
	if($("#fid").val()){
		$("#code").val('${obj.code}');
		$("#code").attr("readonly","readonly");
		if('${code}'){
			$("#fid").val("");
			$("#code").val('${code}');
			$("#code").attr("readonly","readonly");
		}
	}else if('${code}'){
		$("#code").val('${code}');
		$("#code").attr("readonly","readonly");
	}
	//判断订单状态
	if('${obj.recordStatus}'==null||'${obj.recordStatus}'==''){
		$(".mybtn-footer").append('<p><input type="button" onclick="save()" id="save" class="btn-blue2 btn-xs" value="保存" /></p>'+nextNew);
	}else if('${obj.recordStatus}'==0){
		$(".mybtn-footer").append('<p><input type="button" onclick="save()" id="save" class="btn-blue2 btn-xs" value="保存" /></p><p><input type="button" onclick="copy()" id="copy" class="btn-blue2 btn-xs" value="复制" /></p><p><input type="button" onclick="print()" id="print" class="btn-blue2 btn-xs" value="打印" /></p><p><input type="button" onclick="refreshForm()" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p>'+next);
	}else if('${obj.recordStatus}'==1){
		$("#form").find("input").attr('disabled','disabled').css('background','#EBEBE4');
		$(".mybtn-footer").append('<p><input type="button" onclick="refreshForm()" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p><p><input type="button" onclick="print()" id="print" class="btn-blue2 btn-xs" value="打印" /></p><p><input type="button"  onclick="copy()" id="copy" class="btn-blue2 btn-xs" value="复制" /></p><p><input type="button" onclick="checkr()" id="check" class="btn-blue2 btn-xs" value="对单"/></p>'+next);
		supCombo.disable();
		bankCombo.disable();
		memCombo.disable();
	}else if('${obj.recordStatus}'==2){
		$("#form").find("input").attr('disabled','disabled').css('background','#EBEBE4');
		$(".mybtn-footer").append('<p><input type="button" onclick="copy()" id="copy" class="btn-blue2 btn-xs" value="复制" /></p><p><input type="button" onclick="print()" id="print" class="btn-blue2 btn-xs" value="打印" /></p><p><input type="button" onclick="refreshForm()" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p>'+next);
		supCombo.disable();
		bankCombo.disable();
		memCombo.disable();
	}
});
function nextsave(){
	if($("#recordStatus").val() == ''){
		var fid=$("#fid").val();
		var updateTime=$("#updateTime").val();
		var vouchercode=$("#vouchercode").val();
		var code=$("#code").val();
		var billDate=$("#billDate").datebox("getValue");
		var supplierId=$("#supplierId").val();
		var bankId=$("#bankId").val();
		var memberId=$("#memberId").val();
		var amount=$("#amount").val();
		var describe=$("#describe").val();
		$('#form').form('enableValidation'); 
			if($('#form').form('validate')){
				$('#nextSave').attr("disabled","disabled");
				    $.post('${ctx}/payBill/save',{billType:52,fid:fid,updateTime:updateTime,vouchercode:vouchercode,code:code,billDate:billDate,supplierId:supplierId,bankId:bankId,memberId:memberId,amount:amount,describe:describe},function(data){
				    	dataDispose(data);
				    	if(data.returnCode=='0'){
				    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
				    			$('#addBox').window('setTitle',"新增付款单");
								$('#addBox').window('refresh',"${ctx}/payBill/add");
				    			/* window.location.href="${ctx}/costBill/manage"; */
				    			$('#nextSave').removeAttr("disabled");
				    			$('#billList').trigger('reloadGrid');
				    			
				    		}});
				    	}else if(data.returnCode=='1'){
				    		if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.message,fn:function(){
					    		}});
				    		}
				    		$('#nextSave').removeAttr("disabled");
			    		}else{
			    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
				    		$('#nextSave').removeAttr("disabled");
			    		}
				    	return true;
				    });
				}else{
					return false;
				};
	}else if($("#recordStatus").val()==0){
		var fid=$("#fid").val();
		var updateTime=$("#updateTime").val();
		var vouchercode=$("#vouchercode").val();
		var code=$("#code").val();
		var billDate=$("#billDate").datebox("getValue");
		var supplierId=$("#supplierId").val();
		var bankId=$("#bankId").val();
		var memberId=$("#memberId").val();
		var amount=$("#amount").val();
		var describe=$("#describe").val();
		var vo = {billType:52,fid:fid,updateTime:updateTime,vouchercode:vouchercode,code:code,billDate:billDate,supplierId:supplierId,bankId:bankId,memberId:memberId,amount:amount,describe:describe};
		$('#form').form('enableValidation'); 
			if($('#form').form('validate')){
				$('#nextSave').attr("disabled","disabled");
				if(!equals(vo,vo1)){
				    $.post('${ctx}/payBill/save',vo,function(data){
				    	dataDispose(data);
				    	if(data.returnCode=='0'){
				    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
				    			var index=$('#billList tr.jqgrow td[title=${obj.fid}]').parent().attr("id")-1;
								var rows=$('#billList').jqGrid('getRowData');
								var fid="";
								var status="";
								if(rows[index-1]){
									fid=rows[index-1].fid;
									status=rows[index-1].recordStatus;
								}
								if(fid==""){
									var next=getNext("#billList","${ctx}/payBill/list?");
									if(next){
										if(next[0].recordStatus==1||next[0].recordStatus==2){
											$('#addBox').window('setTitle',"查看付款单");
											$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+next[next.length-1].fid+"&flag=detail");
										}else{
											$('#addBox').window('setTitle',"编辑付款单");
											$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+next[next.length-1].fid+"&flag=edit");
										}
									}else{
										$('#addBox').window('setTitle',"新增付款单");
										$('#addBox').window('refresh',"${ctx}/payBill/add");
									}
								}else{
									if(status=="已审核"||status=="已作废"){
										$('#addBox').window('setTitle',"查看付款单");
										$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+fid+"&flag=detail");
									}else{
										$('#addBox').window('setTitle',"编辑付款单");
										$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+fid+"&flag=edit");
									}
								}				    			/* window.location.href="${ctx}/costBill/manage"; */
				    			$('#nextSave').removeAttr("disabled");
				    			$('#billList').trigger('reloadGrid');
				    			
				    		}});
				    	}else if(data.returnCode=='1'){
				    		if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.message,fn:function(){
					    		}});
				    		}
				    		$('#nextSave').removeAttr("disabled");
			    		}else{
			    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
				    		$('#nextSave').removeAttr("disabled");
			    		}
				    	return true;
				    });
				}else{
					var ofid = $('#fid').val();
					var index=$('#billList tr.jqgrow td[title='+ofid+']').parent().attr("id")-1;
					var rows=$('#billList').jqGrid('getRowData');
					var fid="";
					var status="";
					if(rows[index-1]){
						fid=rows[index-1].fid;
						status=rows[index-1].recordStatus;
					}
					if(fid==""){
						var next=getNext("#billList","${ctx}/payBill/list?");
						if(next){
							if(next[0].recordStatus==1||next[0].recordStatus==2){
								$('#addBox').window('setTitle',"查看付款单");
								$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+next[next.length-1].fid+"&flag=detail");
							}else{
								$('#addBox').window('setTitle',"编辑付款单");
								$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+next[next.length-1].fid+"&flag=edit");
							}
						}else{
							$('#addBox').window('setTitle',"新增付款单");
							$('#addBox').window('refresh',"${ctx}/payBill/add");
						}
					}else{
						if(status=="已审核"||status=="已作废"){
							$('#addBox').window('setTitle',"查看付款单");
							$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+fid+"&flag=detail");
						}else{
							$('#addBox').window('setTitle',"编辑付款单");
							$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+fid+"&flag=edit");
						}
					}
	    			$('#nextSave').removeAttr("disabled");
	    			$('#billList').trigger("reloadGrid"); 
				 }
				}else{
					return false;
				};
	}else if($("#recordStatus").val()==1||$("#recordStatus").val()==2){
		var index=$('#billList tr.jqgrow td[title=${obj.fid}]').parent().attr("id")-1;
		var rows=$('#billList').jqGrid('getRowData');
		var fid="";
		var status="";
		if(rows[index-1]){
			fid=rows[index-1].fid;
			status=rows[index-1].recordStatus;
		}
		if(fid==""){
			var next=getNext("#billList","${ctx}/payBill/list?");
			if(next){
				if(next[0].recordStatus==1||next[0].recordStatus==2){
					$('#addBox').window('setTitle',"查看付款单");
					$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+next[next.length-1].fid+"&flag=detail");
				}else{
					$('#addBox').window('setTitle',"编辑付款单");
					$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+next[next.length-1].fid+"&flag=edit");
				}
			}else{
				$('#addBox').window('setTitle',"新增付款单");
				$('#addBox').window('refresh',"${ctx}/payBill/add");
			}
		}else{
			if(status=="已审核"||status=="已作废"){
				$('#addBox').window('setTitle',"查看付款单");
				$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+fid+"&flag=detail");
			}else{
				$('#addBox').window('setTitle',"编辑付款单");
				$('#addBox').window('refresh',"${ctx}/payBill/edit?fid="+fid+"&flag=edit");
			}
		}
	}
};

vo1 = loadedVo();
function loadedVo(){
	var fid=$("#fid").val();
	var updateTime=$("#updateTime").val();
	var vouchercode=$("#vouchercode").val();
	var code=$("#code").val();
	var billDate=$("#billDate").datebox("getValue");
	var supplierId=$("#supplierId").val();
	var bankId=$("#bankId").val();
	var memberId=$("#memberId").val();
	var amount=$("#amount").val();
	var describe=$("#describe").val();
	return {billType:52,fid:fid,updateTime:updateTime,vouchercode:vouchercode,code:code,billDate:billDate,supplierId:supplierId,bankId:bankId,memberId:memberId,amount:amount,describe:describe};
}
</script>
</body>
</html>