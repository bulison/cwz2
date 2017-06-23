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
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<style>
.tool-box-pane a{
	margin-right:5px;
}
</style>
</head>
<body>
	<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>银行初始化</h1>
       </div>             
    </div>
	<div class="tool-box">
		<div class="tool-box-pane">
		<div class="tool-box-pane">
	        <fool:tagOpt optCode="bankinitAdd"><a href="javascript:;" title="" id="lead" class="btn-ora-add">引入</a></fool:tagOpt>
			<fool:tagOpt optCode="bankinitRef"><a href="javascript:;" id="refresh" class="btn-ora-refresh">刷新</a></fool:tagOpt>
			<fool:tagOpt optCode="bankinitEnb"><a href="javascript:void(0)" onclick="updateUse();" id="start" class="btn-ora-start">启用</a></fool:tagOpt>
			<fool:tagOpt optCode="bankinitUenb"><a href="javascript:void(0)" onclick="updateUnUse();" id="startback" class="btn-ora-back">反启用</a></fool:tagOpt>
		</div>
		</div>
	</div>
	
	<table id="dataTable"></table>
	<div id="pager"></div>
    <div id="my-window"></div>
<script type="text/javascript" src="${ctx}/resources/js/cashier/initBank/initBank.js?v=${js_v}"></script>
<script>
var lock = "${lock}";//出纳系统已启用或财务已结账lock=false；不能再修改或删除
jQuery(function($){
	$("#dataTable").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/cashierBankInitController/listBankInit',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#dataTable")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		autowidth:true,
		height:500 ,
		pager:"#pager",
		rowNum:10,
		rowList:[10,20,30],
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[ 
	                {name : 'fid',label : 'fid',hidden:true}, 
	                {name : 'subjectId',label : 'subjectId',hidden:true},
	                {name : 'start',label : 'start',hidden:true},
	                {name : 'editing',label : 'editing',hidden:true}, 
                    {name : 'updateTime',label : 'updateTime',hidden:true}, 
	                {name : 'subjectCode',label : '科目编号',align:'center',width:"100px"},
	                {name : 'subjectName',label : '科目名称',align:'center',width:"100px"},
	                {name : 'cashSubject',label : '现金科目',hidden:true,editable:true,edittype:"text"},
	                {name : 'bankSubject',label : '银行科目',hidden:true,editable:true,edittype:"text"},
	                {name : 'accountInit',label : '日记账期初余额',align:'center',width:"100px",editable:true,edittype:"text"},
	                {name : 'accountDebit',label : '日记账借方金额',align:'center',width:"100px"},
	                {name : 'accountCredit',label : '日记账贷方金额',align:'center',width:"100px"},
	                {name : 'accountBalance',label : '日记账调整后余额',align:'center',width:"100px"},
	                {name : 'statementInit',label : '对账单期初余额',align:'center',width:"100px",editable:true,edittype:"text"},
	                {name : 'statementDebit',label : '对账单借方金额',align:'center',width:"100px"},
	                {name : 'statementCredit',label : '对账单贷方金额',align:'center',width:"100px"},
	                {name : 'statementBalance',label : '对账单调整后余额',align:'center',width:"100px"},
	                {name : 'start',label : '状态',align:'center',width:"100px",formatter:status},
				    <fool:tagOpt optCode="bankinitAction">
	                {name : 'action',label : '操作',align:'center',width:"100px",formatter:function (cellvalue, options, rowObject){
	                	if(rowObject.editing){
	                		var s = "<a href='javascript:;' class='btn-save editing-on btn-index-save-"+options.rowid+"' onclick='save(\""+options.rowId+"\",\""+rowObject.fid+"\")' title='确认'></a>";
	                		var c = "<a href='javascript:;' class='btn-back btn-index-cancel-"+options.rowid+"' onclick='cancel(\""+options.rowId+"\")' title='撤销'></a>";
	                		return s+c;
	                	}else{
	                		var q = "<a href='javascript:;' class='btn-company' onclick='bankBill(\""+rowObject.fid+"\",1)' title='企业未达'></a>";
	                		var y = "<a href='javascript:;' class='btn-bank' onclick='bankBill(\""+rowObject.fid+"\",2)' title='银行未达'></a>";
	                		var r = "<a href='javascript:;' class='btn-del' onclick='delRow(\""+rowObject.fid+"\")' title='删除'></a>";
	                		var e = "<a href='javascript:;' class='btn-edit' onclick='editRow(\""+options.rowId+"\")' title='编辑'></a>";
	                		if(rowObject.start=="0"||rowObject.start=="未启用"){
	                			if(lock == "false"){//出纳系统已启用或财务已结账lock=false；不能再修改或删除
	                				r="",e="";
	                			}
	                			if(rowObject.bankSubject==1){
	                			   return r+e+q+y;
	                			}else{
	                			   return r+e;
	                			}
	                		}else{
	                			return q+y;
	                		}
	                	}
	                }},
				    </fool:tagOpt>
	              ],
	    onLoadSuccess:function(){//列表权限控制
	    	<fool:tagOpt optCode="bankinitAction1">//</fool:tagOpt>$('.btn-company').remove();
	        <fool:tagOpt optCode="bankinitAction2">//</fool:tagOpt>$('.btn-bank').remove();
	        <fool:tagOpt optCode="bankinitAction3">//</fool:tagOpt>$('.btn-edit').remove();
	        <fool:tagOpt optCode="bankinitAction4">//</fool:tagOpt>$('.btn-del').remove();
	        warehouseAll();
	    }
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
});
</script>
</body>
</html>

