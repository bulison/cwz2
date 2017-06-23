<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<c:set var="isDetail" value="${obj.recordStatus eq '1' || obj.recordStatus eq '2'}"></c:set>
<c:set var="action" value="${obj.recordStatus eq '0'?'编辑':obj.recordStatus eq '1' || obj.recordStatus eq '2'?'查看':empty param.id?'新增':'复制'}"></c:set>
<title></title>
<style>
</style>
</head>
<body>

<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1>${action}费用申请单</h1><a href="javascript:;" id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写单据信息</h2></div>
		<div class="in-box" id="list2">
		  	<div class="inlist">
		    <input name="fid" id="fid" value="${obj.fid}" type='hidden'/>
		    <input id="localCache" value="${localCache}" type='hidden'/>
			<input name="deptId" id="deptId" value="${obj.deptId}" type='hidden'/>
			<input name="memberId" id="inMemberId" value="${obj.memberId}" type='hidden'/>
			<input name="updateTime" id="updateTime" value="${obj.updateTime}" type='hidden'/>
			<input id="recordStatus" name="recordStatus" value="${obj.recordStatus}" type='hidden'/>
			<p>
				<c:choose>
					<c:when test="${empty code}">
						<font>单号：</font><input _imp="true" name="code" id="code" value="${obj.code}" class="textBox" type="text"/>		
					</c:when>
					<c:otherwise>
						<font>单号：</font><input _imp="true" name="code" id="code" value="${code}" readonly="readonly" class="textBox" type="text"/>	
					</c:otherwise>
				</c:choose>	
				<font>原始单号：</font><input name="voucherCode" id="voucherCode" value="${action == '复制'?'':obj.voucherCode}" class="textBox" type="text"/>
				<font>申请日期：</font><input _imp="true" name="date" _class="datebox_curr" id="billDate" value="${obj.date}" data-options="{required:true,novalidate:true}" class="textBox" type="text"/>
			</p>
			<p>
			    <font>申请部门：</font><input _imp="true" name="deptName" _class='deptComBoxTree' id="deptName" value="${isDetail ? obj.deptName : obj.deptId}" class="textBox" type="text"/>
				<font>申请人：</font><input _imp="true" id="inMemberName" name="memberName" _class="member-combogrid" value="${obj.memberName}" class="memberBox textBox" type="text"/>
				<font>金额：</font><input _imp="true" id="amount" name="amount" value="${obj.amount}" data-options="{required:true,novalidate:true,validType:['amount','numMaxLength[10]']}" class="easyui-validatebox textBox" type="text"/>
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
			                <font>作废人：</font><input id="cancelName" class="textBox" disabled="disabled" value="${obj.cancelName}"/>
			                <font>作废时间：</font><input id="cancelTime" class="textBox" disabled="disabled" value="${obj.cancelTime}"/>
			            </p>
			</div>
		</div>
	</div>
	<div class="mybtn-footer">
	</div>
</form>
<%-- <script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script> --%>
<script type="text/javascript" src="${ctx}/resources/js/warehouseLoad.js?v=${js_v}"></script>
<script type="text/javascript">
var myStat = "${obj.recordStatus}";
$('.inlist').addClass('status'+myStat);//加上是否审核作废的状态图标
if(myStat != "0" && myStat != ""){
$("#warehousebillForm").fool('fromEnable',{'enable':false});
$("input[_class^=datebox]").each(function(i,n){
	$(n).val(getDateStr($(n).val()));			
});
$("input[name*=Time]").each(function(i,n){
	$(n).val(getDateStr($(n).val()));
});
}else{
$("#warehousebillForm").find("input[_class]").each(function(i,n){
	editInputInit($(this));
	if($("#recordStatus").val()==1||$("#recordStatus").val()==2){
		$("#billDate").datebox({hasDownArrow:false});
		$("#deptName").combotree({hasDownArrow:false});
	}
});
$('input[_imp=true]').prev().prepend('<em>*</em>');
for(var i=0; i<$('input[_imp=true]').length; i++){
	if(!$('input[_imp=true]').eq(i).attr('_class')){
		$('input[_imp=true]').eq(i).validatebox({required:true,novalidate:true});
	}
}
}
/* if($('#inMemberName').length > 0){
	$('#inMemberName').combogrid('textbox').focus(function(){
		$('#inMemberName').combogrid('showPanel');
	});
} */
mybtnFooterInit1();
//初始化详细页 按钮
function mybtnFooterInit1(){
	var $obj = $(".mybtn-footer");
	var _stat = $("#recordStatus").val();
	var _id = $("#fid").val();
	var _btn = "";
	var add = "<a id=\"save\" onclick=\"saver()\" class=\"mybtn-blue mybtn-s\">保存</a>";
	var can = "<a id=\"void\" class=\"mybtn-blue mybtn-s\" onclick=\"canceler('"+_id+"')\">作废</a>";	
	var appr = "<a id=\"verify\" class=\"mybtn-blue mybtn-s\" onclick=\"passer('"+_id+"')\">审核</a>";
	/* var pri = "<a id=\"print\" class=\"mybtn-blue mybtn-s\" onclick=\"printer('"+_id+"')\">打印</a>"; */
	var pri = "";
	var copyRow = "<a id=\"copyRow\" onclick=\"copier('"+_id+"')\" class=\"mybtn-blue mybtn-s\">复制</a>";
	if(!_stat){
		_btn=add;
	}else if(_stat==0){
		_btn=add+appr+can+pri;
	}else if(_stat==1){
		/* $(".textbox-addon").hide(); */
		//$("#warehousebillForm").find("input").attr("disabled","disabled");
		_btn=can+copyRow+pri;
	}else if(_stat==2){
		/* $(".textbox-addon").hide(); */
		//$("#warehousebillForm").find("input").attr("disabled","disabled");
		_btn=copyRow;
	}
	$obj.html(_btn);
}

//详情页保存按钮
function saver(){
	if(!$("#warehousebillForm").fool('fromVali')){
		$(document).unbind('keydown',editkd);
		setTimeout(function(){$(document).bind('keydown',editkd);},300);
		return false;
	}
	var url="${ctx}/expenApplyBill/save";
	var fdata = $("#warehousebillForm").serializeJson();
	$.post(url,fdata,function(data){
		if(data.returnCode =='0'){
    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
    			/* location.href = getRootPath()+"/expenApplyBill/manage"; */
    			$("#addBox").window("close");
    			$('#billList').trigger('reloadGrid'); 
    			return true;
    		}});
    	}else if(data.returnCode == '1'){
    		if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
    			}});
    		}else{
	    		$.fool.alert({msg:data.message,fn:function(){
	    		}});
    		}
		}else{
    		$.fool.alert({msg:'服务器繁忙，请稍后再试！'});
    		return false;
		}
    });
}

function canceler(fid){
	$.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/expenApplyBill/cancle',
					data : {fid :fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
								$("#addBox").window("refresh","${ctx}/expenApplyBill/edit?id="+fid);
								$('#billList').trigger('reloadGrid'); 
							}});
						}else{
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.message,fn:function(){
					    		}});
				    		}
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}
function passer(fid){
	$.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/expenApplyBill/passAudit',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
								$("#addBox").window("refresh","${ctx}/expenApplyBill/edit?id="+fid);
								$('#billList').trigger('reloadGrid'); 
							}});
						}else{
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.message,fn:function(){
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
}
function printer(fid){
	
}
function copier(fid){
	var url="${ctx}/expenApplyBill/edit?mark=1&id="+fid;
	$("#addBox").window("refresh",url);
}
enterController('bill');
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
/* if('${obj.recordStatus}'=="" || '${obj.recordStatus}'=="0"){
	$("#inMemberName").next()[0].comboObj.setComboText("${obj.memberName}");
} */
</script>
</body>
</html>