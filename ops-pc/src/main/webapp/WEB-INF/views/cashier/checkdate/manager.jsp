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
             <h1>出纳结账</h1>
       </div>             
    </div>
	<div class="nav-box">
		<ul>
			<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
			<li><a href="javascript:;">出纳</a></li>
			<li><a href="javascript:;" class="curr">出纳结账</a></li>
		</ul>
	</div>
	<div class="tool-box">
		<div class="tool-box-pane">
	        <fool:tagOpt optCode="checkdate"><a href="javascript:;" title="" id="checkdate" class="btn-ora-checkdate">结账</a></fool:tagOpt>
			<fool:tagOpt optCode="uncheckdate"><a href="javascript:;" id="cancel" class="btn-ora-uncheckdate">反结账</a></fool:tagOpt>
		</div>
	</div>
	
	<table id="dataTable"></table>
	<div id="pager"></div>
	<div id="sdd" class="easyui-dialog" title="结账" style="width:400px;height:200px;"   
        data-options="iconCls:'icon-save',closable:false,resizable:true,modal:true,buttons:[{
				text:'完成',
				handler:checkdate
			},{
				text:'关闭',
				handler:sddclose
			}]">  
	<form id="checkdateForm" action=""> 
	<p> 出纳结账之后，不能再输入结账日期之前的出纳业务资料，所有的出纳业务资料都无法再修改</p>
	 <br/>
	 <br/>
	 <p>
	  结账日期：<input id="voucherDate"data-options="{required:true,novalidate:true,editable:false}" value="" name="checkDate" class="easyui-datebox"/>
	 </p>
	 <br/>
	 <br/>
	 <p>按完成开始结账</p>
    </form>
    </div> 
<div id="my-window"></div>
<script type="text/javascript" src="${ctx}/resources/js/cashier/checkdate/checkdate.js?v=${js_v}"></script>
<script>
/* $('#dataTable').datagrid({
	onLoadSuccess:function(){
		warehouseAll();
	}
}); */
$("#dataTable").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/cashierBankCheckdateController/query',
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
	height:550,
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[ 
                {name : 'checkDate',label : '结账日期',align:'center',width:"100px",formatter:dateFormatAction},
	            {name : 'creatorName',label : '结账人',align:'center',width:"100px"},
              ],
    onLoadSuccess:function(){
    	warehouseAll();
    }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

</script>
</body>
</html>

