<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>销售订单窗口</title>
</head>

<body>
<div class="window-search-box" style="margin:5px;">
<form>
<input id="saleBill-start" class="easyui-datebox" data-options="prompt:'开始日期',width:100,height:30"/>
<input id="saleBill-end" class="easyui-datebox" data-options="prompt:'结束日期',width:100,height:30"/>
<input id="saleBill-code" class="easyui-textbox" data-options="prompt:'单号',width:100,height:30"/>
<input id="saleBill-inMemberName" /><input id="saleBill-inMemberId" type="hidden"/>
<input id="saleBill-voucherCode" />
<a href="javascript:;" class="btn-blue btn-s go-search">筛选</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
<a href="javascript:;" class="btn-blue btn-s" id="clearbtn" >清空</a>
</form>
</div> 

<table class="easyui-datagrid" id="saleBill-table" data-options="idField:'fid',fitColumns:true,pagination:true,url:'${ctx}/salebill/${param.billCode}/list?recordStatus=1&supplierId=${param.supId}&customerId=${param.cusId}'">
<thead>
<tr>
<th data-options="field:'fid',checkbox:true,width:10">fid</th>
<th data-options="field:'code',width:10,sortable:false">单号</th>
<th data-options="field:'billDate',width:10,sortable:false">单据日期</th>
<th data-options="field:'customerName',width:10,sortable:false,hidden:true">客户名称</th>
<th data-options="field:'customerId',hidden:true,width:10,sortable:false">客户ID</th>
<th data-options="field:'customerCode',width:10,hidden:true,sortable:false">客户编号</th>
<th data-options="field:'customerPhone',width:10,hidden:true,sortable:false">客户电话</th>
<th data-options="field:'deptName',width:10,sortable:false,hidden:true">部门名称</th>
<th data-options="field:'deptId',width:10,hidden:true,sortable:false">部门ID</th>
<th data-options="field:'inMemberName',width:10,sortable:false">采购员</th>
<th data-options="field:'inMemberId',width:10,hidden:true,sortable:false">业务员ID</th>
<th data-options="field:'endDate',width:10,sortable:false,hidden:true">计划完成时间</th>
<th data-options="field:'totalAmount',width:10,sortable:false,hidden:true">总金额</th>
<th data-options="field:'freeAmount',width:10,hidden:true,sortable:false,hidden:true">免单金额</th>
<th data-options="field:'voucherCode',width:10,sortable:false">凭证号</th>
</tr>
</thead>
</table>
<div id="salewin"></div>
<script type="text/javascript">
$('#saleBill-voucherCode').textbox({
	width:100,
	height:30,
	prompt:'凭证号',
	inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
		keyup:function(e){
			if(e.keyCode==13){
				search();
			}
		}
	})
});
$('#saleBill-inMemberName').textbox({
	width:100,
	height:30,
	prompt:'采购员',
	inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
		keyup:function(e){
			if(e.keyCode==13){
				search();
			}
		}
	})
});
$("#saleBill-start").datebox({
	inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
		keyup:function(e){
			if(e.keyCode==13){
				search();
			}
		}
	})
})
$("#saleBill-end").datebox({
	inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
		keyup:function(e){
			if(e.keyCode==13){
				search();
			}
		}
	})
})
$('#saleBill-inMemberName').textbox('textbox').click(function(){
	$('#salewin').window({
		"title":'选择采购员',
		href:'${ctx}/member/window?okCallBack=saleCustomerName&onDblClick=saleCustomerNameDBC&singleSelect=true',
		width:500,
		height:300,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		onClose:function(){
			$(this).window("destroy");
		}
	});
});
function saleCustomerName(data){
    $('#saleBill-inMemberId').val(data[0].fid);
	$('#saleBill-inMemberName').textbox('setValue',data[0].username);
	$('#salewin').window('close');
}
function saleCustomerNameDBC(data){
    $('#saleBill-inMemberId').val(data.fid);
	$('#saleBill-inMemberName').textbox('setValue',data.username);
	$('#salewin').window('close');
}
$(function(){
	$('#saleBill-table').datagrid({		
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		onDblClickRow:function(index,row){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		}
	});

	setPager($('#saleBill-table'));
	
	$("#clearbtn").click(function(){
		$(".window-search-box form").form('clear');
	});
	
	$(".select-ok").click(function(){
		var rows = $('#saleBill-table').datagrid('getSelections');
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
	
	$(".go-search").click(function(){
		var saleBillStart=$("#saleBill-start").datebox('getValue');
		var saleBillEnd=$("#saleBill-end").datebox('getValue');
		var saleBillCode=$("#saleBill-code").textbox('getValue');
		var inMemberId = $('#saleBill-inMemberId').val();
		var voucherCode = $('#saleBill-voucherCode').textbox('getValue');
		var options = {"startDay":saleBillStart,"endDay":saleBillEnd,"code":saleBillCode,"inMemberId":inMemberId,"voucherCode":voucherCode};
		$('#saleBill-table').datagrid('load',options);
	});
	
});
function search(){
	var saleBillStart=$("#saleBill-start").datebox('getValue');
	var saleBillEnd=$("#saleBill-end").datebox('getValue');
	var saleBillCode=$("#saleBill-code").textbox('getValue');
	var inMemberId = $('#saleBill-inMemberId').val();
	var voucherCode = $('#saleBill-voucherCode').textbox('getValue');
	var options = {"startDay":saleBillStart,"endDay":saleBillEnd,"code":saleBillCode,"inMemberId":inMemberId,"voucherCode":voucherCode};
	$('#saleBill-table').datagrid('load',options);
}
</script>

</body>
</html>
