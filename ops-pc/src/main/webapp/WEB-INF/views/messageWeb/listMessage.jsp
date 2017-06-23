<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>list</title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/layout.css"/>
<link rel="stylesheet" href="${ctx}/resources/css/comm.css"/>
<script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<script>
function getRowIndex(target){
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}

function deleterow(target){
	$.fool.confirm({
		msg:'确定删除？',
		fn:function(b){
			var row=$('#datagrid').datagrid('getSelected');
			$('#datagrid').datagrid('deleteRow', getRowIndex(target));
			$.post("${ctx}/messageController/delete",{fid:row.fid});
		}
	});
}

function check(target){
			var row=$('#datagrid').datagrid('getSelected');
			if(row!=null){
				location.href = "${ctx}/messageController/check?fid="+row.fid;
			}	
}

function action(value,row,index){
	    var c='<a href="javascript:;" onclick="check(this)"><img title="查看" src="${ctx}/resources/js/lib/easyui/themes/default/images/check.png" style="border:0px;height:16px;width:16px" /></a>';
		var d='&ensp;&ensp;<a href="javascript:;" onclick="deleterow(this)"><img title="删除" src="${ctx}/resources/js/lib/easyui/themes/default/images/delete.png" style="border:0px" /></a>';
		return c+d;
	}
	
$(function(){
	$("#datagrid").datagrid({
		url:'${ctx}/messageController/list?type='+$("#scope").val()+'&status='+$("#status").val()
	});
	$(".tool-bar a").click(function(){
		$(this).addClass('curr').siblings('a').removeClass('curr');
		submitForm();
	});
	
	$("a.tb-del").click(function(){
		var _b = false; 
		$.fool.confirm({
			msg:'确定要删除该条记录吗?',
			fn:function(b){
				_b=b;
			}
		});
		return _b;
	});
});

function setPage(currPage){
	$("#search-order-page").val(currPage);
	submitForm();
}

function submitForm(){
	//订单范围
	$("#scope").val($(".order-scope a[class='curr']").attr('val'));
	//订单状态
	$("#status").val($(".order-stat a[class='curr']").attr('val'));
	$("#datagrid").datagrid({
		url:'${ctx}/messageController/list?type='+$("#scope").val()+'&status='+$("#status").val()
	});

}
</script>
<style>
.fool-page{
	text-align: center;
	margin-top: 10px;
}
.page-info,.page-list,.page-list li{
	display:inline-block;
	font-size:12px;
	color:#9A9A9A;
	text-align:center;
}
.page-info{
}
.page-list li{
	display:inline-block;	
	margin:auto 2px;
}
.page-list li.curr{
	color:#000;
	margin:auto 5px;
	font-weight:bold;
}
.page-list li a{
	color:#9A9A9A;
	border:1px solid #CCCCCC;
	text-decoration:none;
	padding:4px 10px;
}
.myClazz{
	
}
</style>
</head>

<body>

<div class="warp-box">
	<input type="hidden" id="scope" name="type" value="3"/>
	<input type="hidden" id="status" name="status" value="-1"/>
	<div class="tool-left">
	<%-- 	<a href="${ctx}/orderController/addUI" class="add-order">新增订单</a>    
	    <input id="search" name="search-box"/> --%>
	      
	</div>
	    <div class="tool-bar">
	    	<div class="tool-btn order-scope" >
	    		<c:choose>
	    			<c:when test="${type eq '1'}">
	    				<a href="javascript:;" class="curr" val=1>待办消息</a>
	            		<a href="javascript:;" val=2>已办消息</a>
	            		<a href="javascript:;" val=3>提醒消息</a>
	            	</c:when>
	            	
	            	<c:when test="${type eq '2'}">
	    				<a href="javascript:;"  val=1>待办消息</a>
	            		<a href="javascript:;" class="curr" val=2>已办消息</a>
	            		<a href="javascript:;"  val=3>提醒消息</a>
	            	</c:when>
	    			 <c:otherwise>
	    				<a href="javascript:;" val=1>待办消息</a>
	            		<a href="javascript:;"  val=2>已办消息</a> 
	            		<a href="javascript:;"  class="curr" val=3>提醒消息</a> 
	            	</c:otherwise> 
	    		</c:choose>
	        </div>
	        <div class="tool-link order-stat" style="float:right; margin-right:10px;">
		        <c:choose>
		        	<c:when test="${status eq '0'}">
		        		<a href="javascript:;" val="-1">全部</a>
			        	<a href="javascript:;" val="0" class="curr">未读</a>
			            <a href="javascript:;" val="1">已读</a>
			            <a href="javascript:;" val="2">停用</a>
		        	</c:when>
		        	<c:when test="${orderStatus eq '1'}">
		        		<a href="javascript:;" val="-1">全部</a>
			        	<a href="javascript:;" val="0">未读</a>
			            <a href="javascript:;" class="curr" val="1">已读</a>
			            <a href="javascript:;" val="2">停用</a>
		        	</c:when>
		        	<c:when test="${orderStatus eq '2'}">
		        		<a href="javascript:;" val="-1">全部</a>
		        		<a href="javascript:;" val="0">未读</a>
			            <a href="javascript:;" val="1">已读</a>
			            <a href="javascript:;" class="curr" val="2">停用</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="javascript:;" val="-1" class="curr">全部</a>
		        		<a href="javascript:;" val="0">未读</a>
			            <a href="javascript:;" val="1">已读</a>
			            <a href="javascript:;" val="2">停用</a>
		        	</c:otherwise>
		        </c:choose>
	        </div>
	    </div>
    
    <div style="margin-top:20px;" class="tabel-warp">
    <table class="easyui-datagrid" id="datagrid" style="width:100%;height:600px" data-options="singleSelect:true">
    <thead>
    	<tr>
    	    <th field="orderCode" width="20%">订单号</th>
        	<th field="title" width="75%">描述</th>
            <th field="act" width="5%" formatter="action">操作</th>
        </tr>
    </thead>
        
    </table>
    </div>
     <fool:Page pageKey="pageBean.currentPage" jsMethod="setPage" pageBean="${result.pageBean}" /> 
     
     
</div>

</body>
</html>
