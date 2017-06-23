<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>现金银行账</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
</head>
<body>

	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>现金银行账</h1>
                </div>             
             </div>
	
	<div class="tool-box">
		<div class="tool-box-pane">
		<fool:tagOpt optCode="cashBankExport">
			<a href="javascript:;" id="export" class="btn-ora-export">导出</a>
		</fool:tagOpt>
			<!-- <a href="#" class="btn-ora-printer">打印</a> -->
		</div>
		<div class="tool-box-pane">
		<form action="" id="search-form">
			<input _class="bankName-combogrid" id="bankId" name="bankId" />
			<input name="startTime" id="startTime" _class="datebox_curr" data-options="{prompt:'开始日期',width:100,height:30,editable:false}"/>
			<input name="endTime" id="endTime" _class="datebox_curr"  data-options="{prompt:'结束日期',width:100,height:30,editable:false}"/>
		<fool:tagOpt optCode="cashBankSearch">
			<a href="javascript:;" class="btn-blue btn-s go-search">筛选</a>
		</fool:tagOpt>
			<a href="javascript:;" class="btn-blue btn-s" onclick="myclear()">清空</a>
		</form>
		</div>
	</div>
	
	<table id="dataTable"></table>
	<div id="pager"></div>

<div id="my-window"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
var today = new Date().format("yyyy-MM-dd");
var y = new Date().getFullYear();
var m = new Date().getMonth()+1;
var d = new Date().getDate();
var startDate=today,endDate=today;
$.ajax({
    url: '${ctx}/fiscalPeriod/list',
    dataType:"json",
    complete: function(data,stat){
        if(stat=="success") {
            var $data = data.responseJSON.rows;
            var _startDate,_y,_m,_d;
			for(var i=0;i<$data.length;i++){
                _startDate=new Date($data[i].startDate)
                _y=_startDate.getFullYear();
                _m=_startDate.getMonth()+1;
                _d=_startDate.getDate();
                if(_y==y&&_m==m&&_d<=d&&$data[i].checkoutStatus==0){
                    startDate=_y+"-"+_m+"-"+_d;
					endDate=$data[i].endDate;
                    $("#startTime").datebox("setValue",startDate);
                    $("#endTime").datebox("setValue",endDate);
                }
            }
        }
    }
});
function myclear(){
	cleanBoxInput('#search-form');
	$("#startTime").datebox("setValue",startDate);
	$("#endTime").datebox("setValue",endDate);
}
$(function(){
	$(".go-search").click(function(){
		refreshData();
		return false;
	});
	
	
	$("#search-form").find("input[_class]").each(function(i,n){inputInit($(this));});
	
	$("#export").click(function(){
		var bid = $("#bankId").next()[0].comboObj.getSelectedValue();
		if(bid==undefined || bid.length==0){
			$.fool.alert({msg:'账户不能为空'});
		}else{
			var st = $("#startTime").datebox("getValue");
			var et = $("#endTime").datebox("getValue");
			location.href="${ctx}/report/cashbankaccount/export?bankId="+bid+"&startTime="+st+"&endTime="+et;
		}
	});
	
	$("#dataTable").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/report/cashbankaccount/list',
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
		height:$(window).height()-$(".titleCustom").height()-$(".tool-box").height()-120,
		pager:"#pager",
		rowNum:10,
		rowList:[10,20,30],
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[
	              {name : 'time',label : '日期',align:'center',width:"100px",formatter:dateFormatAction}, 
	              {name : 'code',label : '编号',align:'center',width:"100px"}, 
	              {name : 'voucherCode',label : '原始单号',align:'center',width:"100px"}, 
	              {name : 'billType',label : '单据类型编号',align:'center',width:"100px",hidden:true}, 
	              {name : 'billTypeName',label : '单据类型',align:'center',width:"100px"}, 
	              {name : 'income',label : '收入',align:'center',width:"100px"}, 
	              {name : 'expend',label : '支出',align:'center',width:"100px"}, 
	              {name : 'balanceAmount',label : '结余金额',align:'center',width:"100px"}, 
	              {name : 'remark',label : '备注',align:'center',width:"100px"}, 
	              {name : 'abst',label : '摘要号',align:'center',width:"100px",hidden:true}, 
	              {name : 'abstName',label : '摘要',align:'center',width:"100px"}, 
	              {name : 'contactUnit',label : '往来单位',align:'center',width:"100px"}, 
	              ],
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
});

/* function comboboxHandler(value){
	$.ajax({
		type : 'post',
		url : '${ctx}/bankController/keyhandler',
		data : {'account' :value},
		dataType : 'json',
		success : function(data) {
			//alert(data);
			if(data.total>0){
				$("#bankId").combobox('loadData',"");
			}
				//return data.rows;
			//alert(data.rows.length);
		}
	});
} */

</script>