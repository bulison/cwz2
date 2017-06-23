<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>数量金额明细账</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript"
	src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
.form1 p font{
	width:115px;
}
.form1 p{
	margin:5px 0;
}
.form1{
	padding:5px 0;
	margin-top:10px;
}
.box{
	margin:10px 0;
}
</style>
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>数量金额明细账</h1>
                </div>             
             </div>
	<div class="box">
		<div class="toolBox-button">
		<fool:tagOpt optCode="queamLast">
			<a href="javascript:;" id="lastSj" class="btn-ora-recovery">上一科目</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="queamNext">
			<a href="javascript:;" id="nextSj" class="btn-ora-introduce">下一科目</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="queamExport">
			<a href="javascript:;" id="export" class="btn-ora-export">导出</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="queamPrint">
			<a href="javascript:;" id="print" class="btn-ora-printer">打印</a>
		</fool:tagOpt>
		</div>
		<form id="form" class="form1">
			<p><font>科目等级：</font><input id="search-level" class="textBox"/></p>
			<p><font>起始科目编码：</font><input id="search-startCode" class="textBox"/></p>
			<p><font>结束科目编码：</font><input id="search-endCode" class="textBox"/></p>
			<p><font>科目：</font><input id="search-code" class="textBox"/></p>
			<p><font>开始期间：</font><input id="search-startDay" class="textBox"/></p>
			<p><font>结束期间：</font><input id="search-endDay" class="textBox"/></p>
			<p><font>状态：</font><input id="search-voucherStatus" class="textBox"/></p>
		<fool:tagOpt optCode="queamSearch">
			<a href="javascript:;" id="search" href="javascript:;" class="btn-blue4 btn-s">查询</a>
		</fool:tagOpt>
			<a href="javascript:;" id="clear" href="javascript:;" class="btn-blue4 btn-s">清空</a>
		</form>
	</div>
	<table id="amountList"></table>
<script>

	/* $('#search-startDay').combobox({
		valueField:'fid',
		textField:'period',
		width:160,
		height:30,
		required:true,
		novalidate:false,
		prompt:'选择期间',
		editable:false,
		url:'${ctx}/report/quentityAmount/queryPeriods',
		onLoadSuccess:function(data){
			for(var i=0; i<data.length; i++){
				if(data[i].isChecked == 1){
					$('#search-startDay').combobox('setText',data[i].period);
					$('#search-startDay').combobox('setValue',data[i].fid);
				}
			}
		}
	}); */
	$('#search-startDay').fool("dhxCombo",{
		width:160,
		height:30,
		editable:false,
		required:true,
		prompt:'选择期间',
		clearOpt:false,
		setTemplate:{
		  input:"#period#",
		  option:"#period#"
		},
		data:getComboData('${ctx}/fiscalPeriod/getAll?defaultSelect=2'),
		focusShow:true,
		onLoadSuccess:function(){
			var data = getComboData('${ctx}/fiscalPeriod/getAll?defaultSelect=2');
			for(var i=0; i<data.length; i++){
				if(data[i]['text'].isChecked == 1){
					$('#search-startDay').next()[0].comboObj.setComboValue(data[i]['text'].fid);
				}
			}
		}
	});
	/* $('#search-endDay').combobox({
		valueField:'fid',
		textField:'period',
		width:160,
		height:30,
		required:true,
		novalidate:false,
		prompt:'选择期间',
		editable:false,
		validType:'periodsCompare',
		url:'${ctx}/report/quentityAmount/queryPeriods',
		onLoadSuccess:function(data){
			for(var i=0; i<data.length; i++){
				if(data[i].isChecked == 1){
					$('#search-endDay').combobox('setText',data[i].period);
					$('#search-endDay').combobox('setValue',data[i].fid);
				}
			}
		}
	}); */
	$('#search-endDay').fool("dhxCombo",{
		width:160,
		height:30,
		editable:false,
		required:true,
		prompt:'选择期间',
		setTemplate:{
		  input:"#period#",
		  option:"#period#"
		},
		clearOpt:false,
		validType:'periodsCompare',
		data:getComboData('${ctx}/fiscalPeriod/getAll?defaultSelect=2'),
		focusShow:true,
		onLoadSuccess:function(){
			var data = getComboData('${ctx}/fiscalPeriod/getAll?defaultSelect=2');
			for(var i=0; i<data.length; i++){
				if(data[i]['text'].isChecked == 1){
					$('#search-endDay').next()[0].comboObj.setComboValue(data[i]['text'].fid);
				}
			}
		}
	});
	$('#search-startCode').textbox({
		prompt:'填写数字',
		width:160,
		height:30,
		validType:'subjectCode',
		novalidate:false,
		onChange:function(){
			if($('#search-startCode').textbox('isValid') && $('#search-endCode').textbox('isValid') && $('#search-level').textbox('isValid')){
				$('#search-code').combobox('reload','${ctx}/report/quentityAmount/querySubjects?startCode='+$('#search-startCode').textbox('getValue')+'&endCode='+$('#search-endCode').textbox('getValue')+'&level='+$('#search-level').textbox('getValue'));
			}
		}
	});
	$('#search-endCode').textbox({
		prompt:'填写数字',
		width:160,
		height:30,
		validType:Array("subjectCode","codeCompare"),
		novalidate:false,
		onChange:function(){
			if($('#search-startCode').textbox('isValid') && $('#search-endCode').textbox('isValid') && $('#search-level').textbox('isValid')){
				$('#search-code').combobox('reload','${ctx}/report/quentityAmount/querySubjects?startCode='+$('#search-startCode').textbox('getValue')+'&endCode='+$('#search-endCode').textbox('getValue')+'&level='+$('#search-level').textbox('getValue'));
			}
		}
	});
	/* $('#search-code').combobox({
		valueField:'code',
		textField:'name',
		width:160,
		height:30,
		prompt:'选择科目',
		url:'${ctx}/report/quentityAmount/querySubjects',
		onLoadSuccess:function(data){
			if(data == ''){
				$('#search-code').combobox('clear');
			}else{
				$('#search-code').combobox('showPanel');
				$('#search-code').combobox('panel').find('.combobox-item').eq(0).click();
			}
		}
	}); */
	$('#search-code').fool('dhxComboGrid',{
		prompt:'选择科目',
		width:160,
		height:30,
		novalidate:true,
		required:true,
		focusShow:true,
	  	data:getComboData("${ctx}/report/quentityAmount/querySubjects","","","code"),
	  	filterUrl:"${ctx}/report/quentityAmount/querySubjects",
	  	searchKey:"q", 
	  	setTemplate:{
	  		input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
	  	},
	  	toolsBar:{
	  		refresh:true,
	  	}
	  	/* onLoadSuccess:function(){
	  		($('#search-code').next())[0].comboObj.setComboValue(($('#search-code').next())[0].comboObj.getOptionByIndex(0).text.code);
			($('#search-code').next())[0].comboObj.setComboText(($('#search-code').next())[0].comboObj.getOptionByIndex(0).text.name);
		} */
	});
	$('#search-level').textbox({
		novalidate:false,
		validType:"accessoryNumber",
		prompt:'填写大于等于0的整数',
		width:160,
		height:30,
		onChange:function(){
			if($('#search-startCode').textbox('isValid') && $('#search-endCode').textbox('isValid') && $('#search-level').textbox('isValid')){
				var datas=getComboData('${ctx}/report/quentityAmount/querySubjects?startCode='+$('#search-startCode').textbox('getValue')+'&endCode='+$('#search-endCode').textbox('getValue')+'&level='+$('#search-level').textbox('getValue'));
				($('#search-code').next())[0].comboObj.setComboText("");
				($('#search-code').next())[0].comboObj.setComboValue("");
				($('#search-code').next())[0].comboObj.clearAll();
				($('#search-code').next())[0].comboObj.addOption(datas);
			}
		}	
	});
	/* $('#search-voucherStatus').combotree({
		width:160,
		height:30,
		prompt:'选择状态',
		multiple:true,
		data:[{
			text: '未审核',
			id: '0'
		},{
			text: '审核',
			id: '1'
		},{
			text: '过账',
			id: '3'
		}]
	}); */
	$("#search-voucherStatus").combobox({
		editable:false,
		prompt:'选择状态',
		width:160,
		height:30,
		multiple:true,
		data:[{
			text: '未审核',
			value: 0
		},{
			text: '审核',
			value: 1
		},{
			text: '过账',
			value: 3
		}],
	});
	$.extend($.fn.validatebox.defaults.rules, {
		periodsCompare:{
  	 		validator:function(value){
  				return value >= ($('#search-startDay').next())[0].comboObj.getSelectedText().period;
  	 	 	},
  	  		message:'结束期间不能比开始期间早！'
    	},
    	codeCompare:{
    		validator:function(value){
    			return value >= $('#search-startCode').textbox('getValue');
    		},
    		message:'结束科目编码不能小于起始科目编码！'
    	}
	});
	/* $('#amountList').datagrid({
		url:'${ctx}/report/quentityAmount/list',
		singleSelect:true,
		//pagination:true,
		fitColumns:true,
		columns:[[
		          {field:'voucherDate',title:'凭证日期',sortable:true,width:100},
		          {field:'voucherWordNum',title:'凭证字号',width:100},
		          {field:'voucherNum',title:'顺序号',sortable:true,width:100},
		          {field:'voucherResume',title:'摘要',width:100},
		          {field:'subjectCode',title:'科目编号',sortable:true,width:100},
		          {field:'subjectName',title:'科目名称',width:100},
		          {field:'debitUnit',title:'借方单位',width:100},
		          {field:'debitQuentity',title:'借方数量',sortable:true,width:100,formatter:priceFormat},
		          {field:'debitUnitPrice',title:'借方单价',sortable:true,width:100,formatter:priceFormat},
		          {field:'debitAmount',title:'借方金额',sortable:true,width:100,formatter:priceFormat},
		          {field:'creditUnit',title:'贷方单位',width:100},
		          {field:'creditQuentity',title:'贷方数量',sortable:true,width:100,formatter:priceFormat},
		          {field:'creditUnitPrice',title:'贷方单价',sortable:true,width:100,formatter:priceFormat},
		          {field:'creditAmount',title:'贷方金额',sortable:true,width:100,formatter:priceFormat},
		          {field:'endUnit',title:'余额单位',width:100},
		          {field:'endQuentity',title:'余额数量',sortable:true,width:100,formatter:priceFormat},
		          //{field:'endAmount',title:'余额金额',sortable:true,width:100,formatter:priceFormat},
		          {field:'endDirection',title:'方向',width:100},
		          {field:'endAbsAmount',title:'余额',sortable:true,width:100,formatter:priceFormat}
		          ]]
	}); */
	
	$("#amountList").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/report/quentityAmount/list',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#amountList")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		autowidth:true,
		height:"100%",
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[
	              {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"}, 
	              {name : 'voucherWordNum',label : '凭证字号',align:'center',width:"100px"}, 
	              {name : 'voucherNum',label : '顺序号',align:'center',width:"100px"}, 
	              {name : 'voucherResume',label : '摘要',align:'center',width:"100px"}, 
	              {name : 'subjectCode',label : '科目编号',align:'center',width:"100px"}, 
	              {name : 'subjectName',label : '科目名称',align:'center',width:"100px"}, 
	              {name : 'debitUnit',label : '借方单位',align:'center',width:"100px"}, 
	              {name : 'debitQuentity',label : '借方数量',align:'center',width:"100px",formatter:priceFormat}, 
	              {name : 'debitUnitPrice',label : '借方单价',align:'center',width:"100px",formatter:priceFormat}, 
	              {name : 'debitAmount',label : '借方金额',align:'center',width:"100px",formatter:priceFormat}, 
	              {name : 'creditUnit',label : '贷方单位',align:'center',width:"100px"}, 
	              {name : 'creditQuentity',label : '贷方数量',align:'center',width:"100px",formatter:priceFormat}, 
	              {name : 'creditUnitPrice',label : '贷方单价',align:'center',width:"100px",formatter:priceFormat},
	              {name : 'creditAmount',label : '贷方金额',align:'center',width:"100px",formatter:priceFormat}, 
	              {name : 'endUnit',label : '余额单位',align:'center',width:"100px"}, 
	              {name : 'endQuentity',label : '余额数量',align:'center',width:"100px",formatter:priceFormat}, 
	              {name : 'endDirection',label : '方向',align:'center',width:"100px"}, 
	              {name : 'endAbsAmount',label : '余额',align:'center',width:"100px",formatter:priceFormat}, 
	              ],
	});
	
	//设定分页栏
	//setPager($('#amountList'));
	function priceFormat(value){
		if(isNaN(value)){
			return 0;
		}else {
			return value;
		}
	}
	$('#search').click(function(){
		$('#form').form('enableValidation');
		if ($('#form').form('validate')) {
			var startPeriodId = ($('#search-startDay').next())[0].comboObj.getSelectedValue();
			var endPeriodId = ($('#search-endDay').next())[0].comboObj.getSelectedValue();
			var curSubjectCode = ($('#search-code').next())[0].comboObj.getSelectedValue();
			var a = [];
			a = $('#search-voucherStatus').combobox("getValues");
			var voucherStatus = a.join(',');
			var option = {"startPeriodId":startPeriodId,"endPeriodId":endPeriodId,"curSubjectCode":curSubjectCode,"voucherStatus":voucherStatus}; 
			/* $('#amountList').datagrid('load',option); */
			$('#amountList').jqGrid('setGridParam',{postData:option}).trigger("reloadGrid");
		}
	});
	$('#clear').click(function(){
		$('#form').form('clear');
		var inputs=$("#form").find(".dhxDiv");
		for(var i=0;i<inputs.length;i++){ 
			if($(inputs[i]).prev().attr("id")=="search-startDay"||$(inputs[i]).prev().attr("id")=="search-endDay"){
				inputs[i].comboObj.selectOption(0); 
			}else{
				inputs[i].comboObj.setComboText("");
			}
		}
		$('#form').form("validate");
	});
	$('#export').click(function(){
		var startPeriodId = ($('#search-startDay').next())[0].comboObj.getSelectedValue();
		var endPeriodId = ($('#search-endDay').next())[0].comboObj.getSelectedValue();
		var curSubjectCode = ($('#search-code').next())[0].comboObj.getSelectedValue();
		var a = [];
		a = $('#search-voucherStatus').combobox("getValues");
		var voucherStatus = a.join(',');
		var option = {"startPeriodId":startPeriodId,"endPeriodId":endPeriodId,"curSubjectCode":curSubjectCode,"voucherStatus":voucherStatus}; 
		exportFrom("${ctx}/report/quentityAmount/export",option);
	});
	$('#lastSj').click(function(){
		$('#form').form('enableValidation');
		if ($('#form').form('validate')) {
			if(($('#search-code').next())[0].comboObj.getSelectedValue() == ''){
				$.fool.alert({msg:'没有选择科目。'});
				return false;
			}
			var panel = $(($('#search-code').next())[0].comboObj.getList());
			if(panel.find('.dhxcombo_option_selected').prev(".dhxcombo_option").html()){
				var prevValue=$(panel.find('.dhxcombo_option_selected').prev(".dhxcombo_option").find(".dhxcombo_cell_text")[0]).text();
				if(prevValue){
					($('#search-code').next())[0].comboObj.setComboValue(prevValue);
				}
				/* panel.find('.dhxcombo_option_selected').prev().click(); */
			}else{
				$.fool.alert({msg:'没有前一个科目了。'});
				return false;
			}
			var startPeriodId = ($('#search-startDay').next())[0].comboObj.getSelectedValue()
			var endPeriodId = ($('#search-endDay').next())[0].comboObj.getSelectedValue()
			var curSubjectCode = ($('#search-code').next())[0].comboObj.getSelectedValue()
			var a = [];
			a = $('#search-voucherStatus').combobox("getValues");
			var voucherStatus = a.join(',');
			var option = {"startPeriodId":startPeriodId,"endPeriodId":endPeriodId,"curSubjectCode":curSubjectCode,"voucherStatus":voucherStatus}; 
			/* $('#amountList').datagrid('load',option); */
			$('#amountList').jqGrid('setGridParam',{postData:option}).trigger("reloadGrid");
		}
	});
	$('#nextSj').click(function(){
		$('#form').form('enableValidation');
		if ($('#form').form('validate')) {
			if(($('#search-code').next())[0].comboObj.getSelectedValue() == ''){
				$.fool.alert({msg:'没有选择科目。'});
				return false;
			}
			var panel = $(($('#search-code').next())[0].comboObj.getList());
			if(panel.find('.dhxcombo_option_selected').next(".dhxcombo_option").html()){
				var nextValue=$(panel.find('.dhxcombo_option_selected').next(".dhxcombo_option").find(".dhxcombo_cell_text")[0]).text();
				if(nextValue){
					($('#search-code').next())[0].comboObj.setComboValue(nextValue);
				}
			}else{
				$.fool.alert({msg:'已经最后一个科目了。'});
				return false;
			}
			var startPeriodId = ($('#search-startDay').next())[0].comboObj.getSelectedValue()
			var endPeriodId = ($('#search-endDay').next())[0].comboObj.getSelectedValue()
			var curSubjectCode = ($('#search-code').next())[0].comboObj.getSelectedValue();
			var a = [];
			a = $('#search-voucherStatus').combobox("getValues");
			var voucherStatus = a.join(',');
			var option = {"startPeriodId":startPeriodId,"endPeriodId":endPeriodId,"curSubjectCode":curSubjectCode,"voucherStatus":voucherStatus}; 
			/* $('#amountList').datagrid('load',option); */
			$('#amountList').jqGrid('setGridParam',{postData:option}).trigger("reloadGrid");
		}
	});
</script>
</body>
</html>