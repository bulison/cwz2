<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div style="margin:10px 10px;">
		<form id="autoForm">
			<p><font>截止日期：</font><input id="limitDate" /></p>
			<h5>对账条件</h5>
			<div>
				<p><input id="checkDay" name="checkDay" type="checkbox" checked="checked"/><font onclick="clickOn(this);">相隔日期：</font><input id="days" name="days" style="width:70px;" value="0" class="easyui-validatebox" data-options="required:true,missingMessage:'该项不能为空！',validType:'accessoryNumber',novalidate:true"/></p>
				<p><input id="settlementType" name="settlementType" type="checkbox" checked="checked"/><font onclick="clickOn(this);">结算方式相同</font></p>
				<p><input id="settlementNo" name="settlementNo" type="checkbox" checked="checked"/><font onclick="clickOn(this);">结算号相同</font></p>
				<!-- <p><input id="settlementDate" name="settlementDate" type="checkbox"  checked="checked"/><font onclick="clickOn(this);">业务日期相同</font></p> -->
				<p><input type="checkbox" checked="checked" disabled="disabled"/><font style="color:#999;">借贷方向相反，金额相等</font></p>
			</div>
			<br><br>
			<center><a href="javascript:;" id="checkAuto" class="btn-blue2 btn-xs">确认</a></center>
		</form>
	</div>
<script>
//控件初始化
$('#limitDate').datebox({
	required:true,
	editable:false,
	width:100,
	height:31,
	missingMessage:'该项不能为空！',
	novalidate:true
});
//选项选择效果
$('#checkDay').click(function(){
	if($('#checkDay').is(":checked")){
		$("#days").attr("disabled",false);
	}else{
		$("#days").attr("disabled",true);
	}
});
function clickOn(obj){
	if($(obj).siblings('input').is(":checked")){
		$(obj).siblings('input').attr('checked',false);
		if($(obj).siblings('input').attr('id')=="checkDay"){
			$("#days").attr("disabled",true);
		}
	}else{
		$(obj).siblings('input').prop("checked",true);
		if($(obj).siblings('input').attr('id')=="checkDay"){
			$("#days").attr("disabled",false);
		}
	}
}

//自动勾对
$('#checkAuto').click(function(){
	var limitDateStr=$('#limitDate').datebox('getValue');
	var checkDateStr = $('#checkDate').datebox('getValue');
	var subjectId = "${param.subjectId}";
	if($('#checkDay').is(":checked")){var checkDay = 1;var days = $('#days').val(); }else{ checkDay = 0;days = 0;}
	if($('#settlementType').is(":checked")){var settlementType = 1;}else{ settlementType = 0;}
	if($('#settlementNo').is(":checked")){var settlementNo = 1;}else{ settlementNo = 0;}
	/* if($('#settlementDate').is(":checked")){var settlementDate = 1;}else{ settlementDate = 0;} */
	$('#autoForm').form('enableValidation'); 
	if($('#autoForm').form('validate')){
		if($('#dailyList').jqGrid('getRowData').length==0 && $('#balanceList').jqGrid('getRowData').length==0){
			$.fool.alert({msg:'没有数据，请先筛选出账单！'});
			return false;
		}
		$.post('${ctx}/cashierBankBillController/saveByAutoCheck?checkDateStr='+checkDateStr+'&limitDateStr='+limitDateStr+'&checkDay='+checkDay+'&days='+days+'&settlementType='+settlementType+'&settlementNo='+settlementNo+"&subjectId="+subjectId/* +'&settlementDate='+settlementDate */,function(data){
			dataDispose(data);
			if(data.returnCode==0){
				$.fool.alert({time:1000,msg:'自动勾对完成！',fn:function(){
					$('#dailyList').trigger("reloadGrid"); 
					$('#balanceList').trigger("reloadGrid"); 
					$('#winbox').window('close');					
				}});
			}else if(data.returnCode==1){
				$.fool.alert({msg:data.message});
			}else{
				$.fool.alert({msg:'系统繁忙，请稍后再试。'});
			}
		});
	}
});
</script>
</body>
</html>