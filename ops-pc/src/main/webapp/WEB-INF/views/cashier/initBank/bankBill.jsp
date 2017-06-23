<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="xsbjd" scope="page"></c:set>
<c:set var="billCodeName" value="销售报价单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
    <div class="tool-box">
		<div class="tool-box-pane">
	        ${bankInitVo.start==0?'<a href="javascript:;" title="" id="bankLessAdd" class="btn-ora-add">添加</a>':''}
		</div>
	</div>
	<input type="hidden" name="bankInitId" id="bankInitId" value="${bankInitVo.fid}"/>
	<input type="hidden" name="bankStatus" id="bankStatus" value="${bankInitVo.start}"/>
	<input type="hidden" name="type" id="type" value="${type}"/>
	<table id="bankLessTable"></table>
	<div id="bankLessPager"></div>
	<div id="bank-edit-window"></div>
<script type="text/javascript" src="${ctx}/resources/js/cashier/initBank/bankBill.js?v=${js_v}"></script>
<script type="text/javascript">
  $("#bankLessTable").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/cashierBankBillController/listBankBill?subjectId=${bankInitVo.subjectId}&type=${type}',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#bankLessTable")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,
	height:"100%",
	pager:"#bankLessPager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[ 
	            ${type==2?'{name : "settlementDate",label : "业务日期",align:"center",width:"100px",formatter:dateFormatAction},':''}
	            {name : 'fid',label : 'fid',hidden:true},
	            {name : 'orderno',label : '当天顺序号',align:'center',width:'100px'},
	            {name : 'settlementTypeName',label : '结算方式',align:'center',width:"100px"},
	            {name : 'settlementNo',label : '结算号',align:'center',width:"100px"},
	            {name : 'debit',label : '借方金额',align:'center',width:"100px"},
	            {name : 'credit',label : '贷方金额',align:'center',width:"100px"},
	            ${type==2?'{name : "memberName",label : "经手人",align:"center",width:"100px"},':''}
	            {name : 'voucherDate',label : '单据日期',align:'center',width:"100px",formatter:dateFormatAction},
	            {name : 'resume',label : '摘要',align:'center',width:"100px"},
	            {name : 'creatorName',label : '创建人',align:'center',width:"100px"},
	            {name:"action",label:"操作",align:'center',width:"80px",formatter:function (cellvalue, options, rowObject){
	            	var status = $("#bankStatus").val();
	            	var r = "<a href='javascript:;' class='btn-del' onclick='delBankLess(\""+rowObject.fid+"\")' title='删除'></a>";
	            	var e = "<a href='javascript:;' class='btn-edit' onclick='editBankLess(\""+rowObject.fid+"\")' title='编辑'></a>";
	            	var d = "<a href='javascript:;' class='btn-detail' onclick='viewBankLess(\""+rowObject.fid+"\")' title='详情'></a>";
	            	if(status=="1"){
	            		return d+r;
	            	}else{
	            		return e+r;
	            	}
	            }},
              ],
}).navGrid('#bankLessPager',{add:false,del:false,edit:false,search:false,view:false}); 
</script>
</body>
</html>

