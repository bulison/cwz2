<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>货品库存</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
</head>
<body>
	<div class="tool-box-pane" style="float:none">
		<form action="" id="search-form" >
			<input type="hidden" id="periodId" name="periodId" value="${data.periodId}"/>
			<input type="hidden" id="goodsId" name="goodsId" value="${data.goodsId}"/>
			<input type="hidden" id="warehouseId" name="warehouseId" value="${data.warehouseId}"/>
			<input type="hidden" id="specId" name="specId" value="${data.specId}"/>
			<input type="hidden" id="lastAccountQuentity" name="lastAccountQuentity" value="${data.lastAccountQuentity}"/>
			<input type="hidden" id="lastAccountAmount" name="lastAccountAmount" value="${data.lastAccountAmount}"/>
			进出仓标识：<input id="intoutTag" name="inOutTag" />
			单据类型：<input id="billType" name="billType" />
			<a href="javascript:;" class="btn-blue btn-s go-search1">查询</a>
			<a href="javascript:;" class="btn-blue btn-s go-clear1">清空</a>
			<a href="javascript:;" class="btn-blue btn-s go-export">导出</a>
		</form>
	</div>
	<table id="detailTable"></table>
	<div id="pager1"></div>
<script type="text/javascript">
$(function(){
	$("#detailTable").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/report/goodsStock/listDetail?periodId=${data.periodId}&goodsId=${data.goodsId}&specId=${data.specId}&warehouseId=${data.warehouseId}&accountPrice=${data.accountPrice}&accountQuentity=${data.accountQuentity}&lastAccountQuentity=${data.lastAccountQuentity}',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#detailTable")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		pager:"#pager1",
		footerrow:true,
		autowidth:true,
        height:$(window).outerHeight()-200+"px",
		rowNum:10000,
		rowList:[10,20,30],
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},
		colModel:[
	              {name : 'date',label : '日期',align:'center',width:"100px"},
	              {name : 'code',label : '单号',align:'center',width:"100px"},
	              {name : 'billType',label : '单据类型',align:'center',width:"100px"},
	              {name : 'unitName',label : '记账单位',align:'center',width:"100px"},
	              {name : 'quentity',label : '记账数量',align:'center',width:"100px"},
	              {name : 'amount',label : '金额',align:'center',width:"100px"},
	              {name : 'costAmount',label : '成本金额',align:'center',width:"100px"},
	              {name : 'accountQuentity',label : '结余数量',align:'center',width:"100px"},
	              {name : 'accountCostAmount',label : '结余成本金额',align:'center',width:"100px"},
	              ],
		gridComplete:function () {
			var sumQuentity = $("#detailTable").getCol("quentity",false,"sum");
			var sumAmount = $("#detailTable").getCol("amount",false,"sum");
			var sumCostAmount = $("#detailTable").getCol("costAmount",false,"sum");
            var sumAccountQuentity = $("#detailTable").getCol("accountQuentity",false,"sum");
            var sumAccountCostAmount = $("#detailTable").getCol("accountCostAmount",false,"sum");

            $("#detailTable").footerData('set',{
                billType:'合计',
                quentity:sumQuentity,
                amount:sumAmount,
                costAmount:sumCostAmount,
                accountQuentity:sumAccountQuentity,
                accountCostAmount:sumAccountCostAmount
			})
        }
	}).navGrid('#pager1',{add:false,del:false,edit:false,search:false,view:false});

	$("#intoutTag").fool("dhxCombo",{
		width:150,
		height:31,
		prompt:'进仓出仓',
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data: [{
			text: '全部',
			value: ''
		},{
			text: '进仓',
			value: '1'
		},{
			text: '出仓',
			value: '-1'
		}],
		onLoadSuccess:function(){
			($("#intoutTag").next())[0].comboObj.deleteOption("")
		},
	});
	var billTypes=[
	          	{value:null,text:'全部'},
	          	{value:91,text:'期初库存'},
	          	{value:11,text:'采购入库'},
	          	{value:12,text:'采购退货'},
	          	{value:20,text:'盘点单'},
	          	{value:21,text:'调仓单'},
	          	{value:22,text:'报损单'},
	          	{value:30,text:'生产领料（原材料）'},
	          	{value:32,text:'生产退料'},
	          	{value:31,text:'成品入库'},
	          	{value:33,text:'成品退库'},
	          	{value:41,text:'销售出货'},
	          	{value:42,text:'销售退货'},
	          ];
	
	$("#billType").fool("dhxCombo",{
		data:billTypes,
		editable:false,
		prompt:'选择单据类型',
		width:167,
		height:30,
		setTemplate:{
			  input:"#text#",
			  option:"#text#"
			},
		focusShow:true,
		onLoadSuccess:function(){
			($("#billType").next())[0].comboObj.deleteOption("")
		},
	});
	
	$(".go-search1").click(function(){
		//$('#detailTable').datagrid('loadData',[]);
		$('#detailTable').jqGrid('setGridParam',{postData:{intoutTag:($("#intoutTag").next())[0].comboObj.getSelectedValue(),billType:($("#billType").next())[0].comboObj.getSelectedValue()}}).trigger("reloadGrid");
	});
	
	$(".go-clear1").click(function(){
		($("#intoutTag").next())[0].comboObj.setComboValue("");
		($('#billType').next())[0].comboObj.setComboValue("");
		($("#intoutTag").next())[0].comboObj.setComboText("");
		($('#billType').next())[0].comboObj.setComboText("");
	});
	
	$(".go-export").click(function(){
		var url = '${ctx}/report/goodsStock/exportDetail?periodId=${data.periodId}&goodsId=${data.goodsId}&specId=${data.specId}&warehouseId=${data.warehouseId}&accountPrice=${data.accountPrice}&accountQuentity=${data.accountQuentity}&lastAccountQuentity=${data.lastAccountQuentity}';
		var intoutTag = ($("#intoutTag").next())[0].comboObj.getSelectedValue()==null?"":($("#intoutTag").next())[0].comboObj.getSelectedValue();
		var billType = ($("#billType").next())[0].comboObj.getSelectedValue()==null?"":($("#billType").next())[0].comboObj.getSelectedValue();
		exportFrom(url,{intoutTag: intoutTag, billType:billType});
	});
});
</script>
</body>
</html>
