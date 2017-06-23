<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div class="form1" >
              <form id="form">
                <input name="type" id="type" type="hidden" value="4"/>
                <input name="fid" id="fid" type="hidden" value="${bankBill.fid}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${bankBill.updateTime}"/>
                <input name="subjectId" id="subjectId" type="hidden" value="${bankBill.subjectId}"/>
                <input name="memberId" id="memberId" type="hidden" value="${bankBill.memberId}"/>
				<p><font><em>*</em>科目：</font><input id="subjectName" name="subjectName" type="text" class="textBox" value="${bankBill.subjectName}"/></p>
				<p><font><em>*</em>借方金额：</font><input id="debit" name="debit" type="text" class="textBox" value="${bankBill.debit}" data-options="required:true,validType:['balanceAmount','numMaxLength[10]']"/></p>
				<p><font><em>*</em>贷方金额：</font><input id="credit" name="credit" type="text" class="textBox" value="${bankBill.credit}" data-options="required:true,validType:['balanceAmount','numMaxLength[10]']"/></p>
				<p><font><em>*</em>日期：</font><input id="voucherDate" name="voucherDate" type="text" class="textBox"/></p>
				<p><font>摘要：</font><input id="resume" type="text" class="textBox" value="${bankBill.resume}"/></p>
				<!-- <p><font><em>*</em>结算日期：</font><input id="settlementDate" name="settlementDate" type="text" class="textBox"/></p> -->
				<p><font>当天顺序号：</font><input id="orderno" name="orderno" type="text" class="textBox" value="${bankBill.orderno}" data-options="validType:'accessoryNumber'"/></p>
				<p><font>结算方式：</font><input id="settlementTypeId" name="settlementTypeId" type="text" class="textBox"/><a href="javascript:;" title="清除" class="clearBill" style="position: absolute;margin: 5px 0;"><img style="vertical-align:middle;" src="${ctx}/resources/images/cancel.png"></a></p>
				<p><font>结算号：</font><input id="settlementNo" name="settlementNo" type="text" class="textBox" value="${bankBill.settlementNo}" data-options="validType:'accessoryNumber'"/></p>
				<%-- <c:choose>
					<c:when test="${empty bankBill.fid}">
						<p><font>手续费：</font><input id="poundage" name="poundage" type="text" class="textBox" data-options="validType:'intOrFloat'"/></p>
					</c:when>
				</c:choose> --%>
				<%-- <p><font><em>*</em>经手人：</font><input id="memberName" name="memberName" type="text" class="textBox" value="${bankBill.memberName}"/></p> --%>
				<p><font>创建人：</font><input id="" name="" type="text" class="textBox" disabled="disabled" value="${bankBill.creatorName}"/></p><br/>
				<p><font></font><a href="javascript:;" id="save" class="btn-blue2 btn-xs">保存</a></p> 
              </form>
          </div>

<script type="text/javascript"
	src="${ctx}/resources/js/cashier/cashier.js?v=${js_v}"></script>
<script type="text/javascript">
var chooserWindow="";
initSubject(0);
//借贷方金额初始化
if($("#fid").val()){
	if('${bankBill.debit}'==0||'${bankBill.debit}'==""){
		$("#debit").val(0);
		$("#debit").attr("disabled",true);
	}
	if('${bankBill.credit}'==0||'${bankBill.credit}'==""){
		$("#credit").val(0);
		$("#credit").attr("disabled",true);
	}
};

//科目输入框赋值
/* if('${subject}'){
	$("#subjectId").val('${subject.fid}');
	$("#subjectName").combobox('setValue','${subject.fid}');
} */

//借贷方金额操作
$("#debit").keyup(function(){
	if($("#debit").val()&&$("#debit").val()!=0){
		$("#credit").val(0);
		$("#credit").attr("disabled",true);
		$("#credit").validatebox("validate");
	}else{
		$("#credit").val("");
		$("#credit").removeAttr("disabled",true);
		$("#credit").validatebox("validate");
	}
});
$("#credit").keyup(function(){
	if($("#credit").val()&&$("#credit").val()!=0){
		$("#debit").val(0);
		$("#debit").attr("disabled",true);
		$("#debit").validatebox("validate");
	}else{
		$("#debit").val("");
		$("#debit").removeAttr("disabled",true);
		$("#debit").validatebox("validate");
	}
});
$("#orderno").validatebox({});

var settlementTypeCombo=$("#settlementTypeId").fool('dhxCombo',{
	width:160,
	height:32,
	focusShow:true,
	editable:false,
	value:"${bankBill.settlementTypeId}",
	data:getComboData("${ctx}/basedata/settlementType","tree"),
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
    toolsBar:{
        name:"结算方式",
        addUrl:"/basedata/listAuxiliaryAttr",
        refresh:true
    }
});
$("#settlementNo").validatebox({});
$("#debit").validatebox({
	required:true,
	novalidate:true,
});
$("#debit").bind("change",function(e){
	if(!isNaN($(this).val())){
		var nv = $(this).val()+"";
		value = parseFloat(nv).toFixed(2)+'';
		$(this).val(value);
	}
});
$("#credit").validatebox({
	required:true,
	novalidate:true,
});
$("#credit").bind("change",function(e){
	if(!isNaN($(this).val())){
		var nv = $(this).val()+"";
		value = parseFloat(nv).toFixed(2)+'';
		$(this).val(value);
	}
});
$("#voucherDate").datebox({
	value:"${bankBill.voucherDate}",
	required:true,
	novalidate:true,
	width:160,
	height:32,
});
$('#voucherDate').datebox('textbox').focus(function(){
	$('#voucherDate').datebox('showPanel');
});
var resumeCombo=$("#resume").fool('dhxCombo',{
	width:160,
	height:32,
	focusShow:true,
	/* editable:false, */
	value:"${bankBill.resume}",
	text:"${bankBill.resume}",
	data:getComboData("${ctx}/basedata/resume","tree"),
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
    toolsBar:{
        name:"摘要",
        addUrl:"/basedata/listAuxiliaryAttr",
        refresh:true
    }
});

if('${bankBill.fchecked}'==1){
	$("#form input").each(function(){
		var $input = $(this);
		if("undefined" != typeof $input.attr("class") && $input.attr("class").search(/combo-f/)!=-1){
			$input.combo("disable");
		}else{
			$input.attr("disabled","disabled");
		}
	});
	$('.clearBill').hide();
	$("#save").hide();
}

//保存按钮点击事件
$("#save").click(function(){
	$('#form').form('enableValidation');
	if($('#form').form('validate')){
		$('#save').attr("disabled","disabled");
		var debit = $.trim($("#debit").val());
		var credit = $.trim($("#credit").val());
		var resume=resumeCombo.getComboText();
		var mydata = $('#form').serializeJson();
		mydata = $.extend(mydata,{debit:debit,credit:credit,resume:resume});
		$.post('${ctx}/cashierBankBillController/save',mydata,function(data){
			dataDispose(data);
			if(data.returnCode =='0'){
						$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
							$(document).unbind('keydown',cashkd);
							$('#addBox').window('close');
			    			$('#save').removeAttr("disabled");
			    			$("#billList").trigger("reloadGrid"); 
						}});
			}else if(data.returnCode ==1){
	    		$.fool.alert({msg:data.message,fn:function(){
	    			$(document).unbind('keydown',cashkd);
	    			$(document).bind('keydown',cashkd);
	    		}});
	    		$('#save').removeAttr("disabled");
	    	}else{
	    		$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
	    			$(document).unbind('keydown',cashkd);
	    			$(document).bind('keydown',cashkd);
	    		}});
	    		$('#save').removeAttr("disabled");
    		}
		});
	}else{
		$(document).unbind('keydown',cashkd);
		setTimeout(function(){$(document).bind('keydown',cashkd);},300);
	}
});
enterController('form',true);
</script>
</body>
</html>