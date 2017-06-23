<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div class="form1" >
              <form id="searchform">
                <input id="creatorId-search" name="creatorId" class="textBox" type="hidden"/>
                <input id="auditorId-search" name="auditorId" class="textBox" type="hidden"/>
                <input id="postPeopleId-search" name="postPeopleId" class="textBox" type="hidden"/>
                <input id="subjectId-search" name="subjectId" class="textBox" type="hidden"/>
                <input id="voucherWordId-search" name="voucherWordId" class="textBox" type="hidden"/>
				<p><font>凭证字：</font><input id="voucherWordName-search" name="voucherWordName" class="textBox"/></p>
				<p><font>状态：</font><input id="recordStatus-search" name="recordStatus" class="textBox"/></p><br/>
				<p><font>制单人：</font><input id="creatorName-search" name="creatorName" class="textBox"/></p>
				<p><font>审核人：</font><input id="auditorName-search" name="auditorName" class="textBox"/></p><br/>
				<p><font>过账人：</font><input id="postPeopleName-search" name="postPeopleName" class="textBox"/></p>
				<p><font>附单据：</font><input id="accessoryNumber-search" name="accessoryNumber" class="textBox"/></p><br/>
				<!-- <p><font>摘要：</font><input id="resume-search" name="resume" class="textBox"/></p> -->
				<!-- <p><font>科目名称：</font><input id="subjectName-search" name="subjectName" class="textBox"/></p><br/> -->
				<p><font>凭证号：</font><input id="voucherNumberStr-search" name="startVoucherNumber" class="textBox"/> 至 <input id="voucherNumberEnd-search" name="endVoucherNumber" class="textBox"/></p><br/>
				<p><font>制单日期：</font><input id="createTimeStr-search" name="startDay" class="textBox"/> 至 <input id="createTimeEnd-search" name="endDay" class="textBox"/></p><br/>
				<!-- <p><font>借方金额：</font><input id="debitAmountStr-search" name="debitAmountStr" class="textBox"/> 至 <input id="debitAmountEnd-search" name="debitAmountEnd" class="textBox"/></p><br/>
				<p><font>贷方金额：</font><input id="creidAmountStr-search" name="creidAmountStr" class="textBox"/> 至 <input id="creidAmountEnd-search" name="creidAmountEnd" class="textBox"/></p><br/> -->
				
				<p><font></font><a href="javascript:;" id="searchBtn" class="btn-blue btn-s">过滤</a></p> 
              </form>
          </div>

<script type="text/javascript">
var chooserWindow_search="";
$(document).ready(function(){
	//禁用文本框提示
	$("input").attr('autocomplete','off');
});

$("#voucherWordName-search").validatebox({});
$("#recordStatus-search").combobox({
	data:[{value:"0",text:'草稿单'},{value:"1",text:'已审核'},{value:"2",text:'已作废'}],
	width:164,
	height:31,
	editable:false,
});
$("#creatorName-search").validatebox({});
$("#auditorName-search").validatebox({});
$("#postPeopleName-search").validatebox({});
$("#accessoryNumber-search").validatebox({});
$("#resume-search").validatebox({});
$("#subjectName-search").validatebox({});
$("#voucherNumberStr-search").validatebox({});
$("#voucherNumberEnd-search").validatebox({});
$("#createTimeStr-search").datebox({
	width:164,
	height:31,
	editable:false,
});
$("#createTimeEnd-search").datebox({
	width:164,
	height:31,
	editable:false,
});
$("#debitAmountStr-search").validatebox({});
$("#debitAmountEnd-search").validatebox({});
$("#creidAmountStr-search").validatebox({});
$("#creidAmountEnd-search").validatebox({});

//弹出框操作方法
//凭证字选择框操作方法
$("#voucherWordName-search").click(function(){
	chooserWindow_search=$.fool.window({'title':"选择凭证字",href:'${ctx}/voucher/voucherWordWindow?okCallBack=selectVoucherWord&onDblClick=selectVoucherWordDBC&singleSelect=true'});
});
function selectVoucherWord(rowData) {
	$("#voucherWordName-search").val(rowData[0].text);
	$("#voucherWordId-search").val(rowData[0].id);
	chooserWindow_search.window('close');
}
function selectVoucherWordDBC(rowData) {
	$("#voucherWordName-search").val(rowData.text);
	$("#voucherWordId-search").val(rowData.id);
	chooserWindow_search.window('close');
}
//制单人选择框操作方法
$("#creatorName-search").click(function(){
	chooserWindow_search=$.fool.window({'title':"选择制单人",href:'${ctx}/member/window?okCallBack=selectCreator&onDblClick=selectCreatorDBC&singleSelect=true'});
});
function selectCreator(rowData) {
	$("#creatorName-search").val(rowData[0].username);
	$("#creatorId-search").val(rowData[0].fid);
	chooserWindow_search.window('close');
}
function selectCreatorDBC(rowData) {
	$("#creatorName-search").val(rowData.username);
	$("#creatorId-search").val(rowData.fid);
	chooserWindow_search.window('close');
}
//审核人选择框操作方法
$("#auditorName-search").click(function(){
	chooserWindow_search=$.fool.window({'title':"选择审核人",href:'${ctx}/member/window?okCallBack=selectAuditor&onDblClick=selectAuditorDBC&singleSelect=true'});
});
function selectAuditor(rowData) {
	$("#auditorName-search").val(rowData[0].username);
	$("#auditorId-search").val(rowData[0].fid);
	chooserWindow_search.window('close');
}
function selectAuditorDBC(rowData) {
	$("#auditorName-search").val(rowData.username);
	$("#auditorId-search").val(rowData.fid);
	chooserWindow_search.window('close');
}
//过账人选择框操作方法
$("#postPeopleName-search").click(function(){
	chooserWindow_search=$.fool.window({'title':"选择过账人",href:'${ctx}/member/window?okCallBack=selectPostPeople&onDblClick=selectPostPeopleDBC&singleSelect=true'});
});
function selectPostPeople(rowData) {
	$("#postPeopleName-search").val(rowData[0].username);
	$("#postPeopleId-search").val(rowData[0].fid);
	chooserWindow_search.window('close');
}
function selectPostPeopleDBC(rowData) {
	$("#postPeopleName-search").val(rowData.username);
	$("#postPeopleId-search").val(rowData.fid);
	chooserWindow_search.window('close');
}
//科目选择框操作方法
$("#subjectName-search").click(function(){
	chooserWindow_search=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubjectDBC&singleSelect=true'});
});
function selectSubject(rowData) {
	$("#subjectName-search").val(rowData[0].name);
	$("#subjectId-search").val(rowData[0].fid);
	chooserWindow_search.window('close');
}
function selectSubjectDBC(rowData) {
	$("#subjectName-search").val(rowData.name);
	$("#subjectId-search").val(rowData.fid);
	chooserWindow_search.window('close');
}

//保存按钮点击事件
$('#searchBtn').click(function(e) {
	var voucherWordId=$("#voucherWordId-search").val();
	var recordStatus=$("#recordStatus-search").combotree('getValue');
	var creatorId=$("#creatorId-search").val();
	var auditorId=$("#auditorId-search").val();
	var postPeopleId=$("#postPeopleId-search").val();
	var accessoryNumber=$("#accessoryNumber-search").val();
	var startVoucherNumber=$("#voucherNumberStr-search").val();
	var endVoucherNumber=$("#voucherNumberEnd-search").val();
	var createTimeStr=$("#createTimeStr-search").datebox('getValue');
	var createTimeEnd=$("#createTimeEnd-search").datebox('getValue');
	if($('#searchform').form('validate')){
		var options = {voucherWordId:voucherWordId,recordStatus:recordStatus,creatorId:creatorId,auditorId:auditorId,postPeopleId:postPeopleId,accessoryNumber:accessoryNumber,startVoucherNumber:startVoucherNumber,endVoucherNumber:endVoucherNumber,startDay:createTimeStr,endDay:createTimeEnd};
		$('#voucherList').datagrid('load',options);
		//$("#searchBox").window('close');
	}else{
		return false;
	}
});
 </script>
</body>
</html>