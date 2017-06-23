<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="/WEB-INF/views/common/header.jsp"%>
	<title>银行存款对账</title>
	<%@ include file="/WEB-INF/views/common/js.jsp"%>
	<script type="text/javascript"
	src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
	<script type="text/javascript"
	src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
</head>
<style>
	#search-form{
		display: inline-block;
	}
	h5{
		margin:10px 0;
	}
	#Inquiry{margin-left: 5px;}
	#grabble{ float: right;}
	#bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 25px;}
	.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
		border-radius:3px;/* W3C syntax */float: right; right:24px; top:3px; position: absolute;}
		.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:5px;}
		.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:32px;z-index: 1;}
		.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px;width:255px;}
		.button_clear{ border-top: 1px solid #D5DBEA;margin-top: 10px; text-align: right; padding: 10px 10px 0px 0px;}
		.roed{
			-moz-transform:scaleY(-1);
			-webkit-transform:scaleY(-1);
			-o-transform:scaleY(-1);
			transform:scaleY(-1);
			filter:FlipV();
		}
		#dhxDiv_search-subjectName{
			margin:0px 0px 10px 0px
		}
	</style>
	<body>
		<div class="titleCustom">
			<div class="squareIcon">
				<span class='Icon'></span>
				<div class="trian"></div>
				<h1>银行存款对账</h1>
			</div>             
		</div>
		<div class="nav-box">
			<ul>
				<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
				<li><a href="javascript:;">出纳</a></li>
				<li><a href="javascript:;" class="curr">银行存款对账</a></li>
			</ul>
		</div>
		<div  class="toolBox" id="search-box">
			<font style="font-size:14px;">本次勾对日期：</font><input id="checkDate" />
			<input id="search-subjectId" name="subjectId" type="hidden"/>
			<input id="search-subjectName" class="textBox"/>
			<a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
			<fool:tagOpt optCode="acheckHandle"><span class="toolBox-button"><a href="javascript:;" id="handleCheck" class="btn-ora-checkdate">手动对账</a></span></fool:tagOpt>
			<fool:tagOpt optCode="acheckAuto"><span class="toolBox-button"><a href="javascript:;" id="autoCheck" class="btn-ora-trial">自动对账</a></span></fool:tagOpt>
			<fool:tagOpt optCode="acheckCancel"><span class="toolBox-button"><a href="javascript:;" id="cancleCheck" class="btn-ora-introduce">取消对账</a></span></fool:tagOpt>
			<div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
			<div class="input_div">
				<form id="search-form" class="toolBox-pane">
					<p>开始日期：<input id="search-startDay" /></p>
					<p>结束日期：<input id="search-endDay" /></p>			
					<p>是否勾对：<input id="search-checked" /></p>		
				</form>	
				<div class="button_clear">
					<fool:tagOpt optCode="acheckSearch"><a id="search-btn" class="btn-blue btn-s">查询</a></fool:tagOpt>
					<a href="javascript:;" id="clear-btn" class="btn-blue btn-s">清空</a>
					<a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
				</div>
			</div>		
		</div>
		<h5>银行日记账单</h5>
		<table id="dailyList"></table>
		<div id="dailyPager"></div>
		<h5>银行对账单</h5>
		<table id="balanceList"></table>
		<div id="balancePager"></div>

		<div id="winbox"></div>
		<script>
//点击下拉按钮
$('.button_a').click(function(){
	$('.input_div').slideToggle(200);
	$(this).addClass('roed');
});

//点击关闭隐藏
$('#clear-btndiv').click(function(){
	$('.input_div').hide(2);
	$('.button_a').removeClass('roed');
});
//全局查询
$('.Inquiry').click(function(){
	$('#search-box').form('enableValidation');
	if($('#search-box').form('validate')){
//		var subjectId=subjectCombo.getSelectedValue();
		var subjectId = $('#search-subjectId').val();
		var options={'subjectId':subjectId};
		$("#dailyList").jqGrid('setGridParam',{postData:options}).trigger("reloadGrid"); 
		$("#balanceList").jqGrid('setGridParam',{postData:options}).trigger("reloadGrid"); 
		$(".checker").attr("checked",false);
	  /* $("#dailyList").datagrid('load',options);
	  $("#balanceList").datagrid('load',options);
	  $("#dailyList").datagrid('clearChecked');
	  $("#balanceList").datagrid('clearChecked'); */
	}
});

//鼠标获取焦点
$('#bolting').focus(function (){ 
	$('.input_div').show(2);
	$('.button_a').addClass('roed');
}); 

var checkedData=[{value:"0",text:'未勾对'},{value:"1",text:'已勾对'}];
//手动勾对
$('#handleCheck').click(function(){
	var checkDateStr = $('#checkDate').datebox('getValue');
	$('#search-form').form('enableValidation'); 
	if($('#search-form').form('validate')){	
		var up = $('#dailyList').find(".checker:checkbox:checked");
		var down = $('#balanceList').find(".checker:checkbox:checked");
		if(!up[0]||up[0]==""){
			$.fool.alert({msg:"请选择银行日记账单！"});
			return false;
		}else if(!down[0]||down[0]==""){
			$.fool.alert({msg:"请选择银行对账单！"});
			return false;
		}
		var upId = $(up[0]).attr("fid");
		var downId = $(down[0]).attr("fid");
		$.post('${ctx}/cashierBankBillController/saveByCheck?upId='+upId+'&downId='+downId+'&checkDateStr='+checkDateStr,function(data){
			dataDispose(data);
			if(data.returnCode==0){
				$.fool.alert({time:1000,msg:'勾对完成！',fn:function(){
					/* $('#dailyList').datagrid('reload'); */
					$('#dailyList').trigger("reloadGrid"); 
					$('#balanceList').trigger("reloadGrid"); 
					/* $('#balanceList').datagrid('reload'); */
				}});
			}else if(data.returnCode==1){
				$.fool.alert({msg:data.message});
			}else{
				$.fool.alert({msg:'系统繁忙，请稍后再试。'});
			}
		});
	}
});
//取消勾对
$('#cancleCheck').click(function(){
	if($('#search-form').form('validate')){	
		var up = $('#dailyList').find(".checker:checkbox:checked");
		var down = $('#balanceList').find(".checker:checkbox:checked");
		var ids = [];
		if(!up[0] && !down[0]){
			$.fool.alert({msg:"请选择银行日记账单或对账单！"});
			return false;
		}else if(down[0]!="" && !up[0]){
			ids.push($(down[0]).attr("fid"));
		}else if(!down[0] && up[0]!=""){
			ids.push($(up[0]).attr("fid"));
		}else{
			ids.push($(down[0]).attr("fid"));
			ids.push($(up[0]).attr("fid"));
		}
		ids = ids.join(',');
		$.post('${ctx}/cashierBankBillController/deleteCheck?ids='+ids,function(data){
			dataDispose(data);
			if(data.result==0){
				$.fool.alert({msg:'取消勾对成功！',fn:function(){
					$('#dailyList').trigger("reloadGrid"); 
					$('#balanceList').trigger("reloadGrid"); 
				}});
			}else if(data.result==1){
				$.fool.alert({msg:data.msg});
			}else{
				$.fool.alert({msg:'系统繁忙，请稍后再试。'});
			}
		});
	}
});

$('#autoCheck').click(function(){
	$('#winbox').window({collapsible:false,minimizable:false,maximizable:false,'title':"自动对账",width:300,height:300,href:'${ctx}/cashierBankBillController/autoCheck?subjectId='+subjectCombo.getSelectedValue()});
});
//控件初始化
$("#search-startDay").datebox({
	editable:false,
	width:160,
	height:30,
	prompt:"开始日期"
});
$("#search-endDay").datebox({
	editable:false,
	width:160,
	height:30,
	prompt:"结束日期"
});
$("#search-checked").fool('dhxCombo',{
	width:162,
	height:32,
	clearOpt:false,
	prompt:"是否勾对",
	focusShow:true,
	editable:false,
	data:[{value:"0",text:'未勾对'},{value:"1",text:'已勾对'},{value:"2",text:'全部'}],
	setTemplate:{
		input:"#name#",
		option:"#name#"
	}

});
$('#checkDate').datebox({
	editable:false,
	required:true,
	missingMessage:'该项不能为空',
	novalidate:true,
	width:160,
	height:30
});
$("#clear-btn").click(function(){
	$("#search-form").form('clear'); 
	var inputs=$("#search-form").find(".dhxDiv");
	for(var i=0;i<inputs.length;i++){
		inputs[i].comboObj.setComboText(null);
	}
});
var curr_time = new Date();
var strDate = curr_time.getFullYear()+"-";
strDate += curr_time.getMonth()+1+"-";
strDate += curr_time.getDate();
$('#checkDate').datebox('setValue',strDate);

var subjectCombo = $('#search-subjectName').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	prompt:"银行存款科目",
	width:165,
	height:32,
	focusShow:true,
	data:getComboData(getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&q="),
	filterUrl:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&bankSubject=1",
	searchKey:"q", 
	setTemplate:{
		input:"#name#",
		columns:[
		{option:'#code#',header:'编号',width:100},
		{option:'#name#',header:'名称',width:100},
		],
	},
	toolsBar:{
		refresh:true
	},
	onChange:function (value,text) {
        $('#search-subjectId').val(value);
        $('#search-subjectName').val(text);
    },
    onLoadSuccess:function () {
        var check = $("#search-subjectName").val();
        if(check !=""){
            $("#input_dhxDiv_search-subjectId").css("background-color","#fff");
        }
    }
});
$('#search-subjectName').next().find(".dhxcombo_select_button").click(function(){
	subjectCombo.closeAll();
	mysubjectName();
});

 //筛选按钮
 $("#search-btn").click(function(){
 	$('#search-form').form('enableValidation')
 	if($('#search-form').form('validate')){
// 		var subjectId=subjectCombo.getSelectedValue();
 		var subjectId=$('#search-subjectId').val();
 		var startDate=$("#search-startDay").datebox('getValue');
 		var endDate=$("#search-endDay").datebox('getValue');		
 		var fchecked=($("#search-checked").next())[0].comboObj.getSelectedValue();
 		var options={startDate:startDate,endDate:endDate,fchecked:fchecked,subjectId:subjectId};
 		$("#dailyList").jqGrid('setGridParam',{postData:options}).trigger("reloadGrid"); 
 		$("#balanceList").jqGrid('setGridParam',{postData:options}).trigger("reloadGrid"); 
 		$(".checker").attr("checked",false);
 	}
 });
 
 //科目初始数据列表初始化
 $("#dailyList").jqGrid({
 	datatype:function(postdata){
 		if(!postdata.subjectId){
 			return false;
 		}
 		$.ajax({
 			url: '${ctx}/cashierBankBillController/listBankBill?showDefault=0&types=2,3',
 			data:postdata,
 			dataType:"json",
 			complete: function(data,stat){
 				if(stat=="success") {
 					data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
 					$("#dailyList")[0].addJSONData(data.responseJSON);
 				}
 			}
 		});
 	},
 	autowidth:true,
 	height:180,
 	pager:"#dailyPager",
 	rowNum:10,
 	rowList:[10,20,30],
 	viewrecords:true,
 	jsonReader:{
 		records:"total",
 		total: "totalpages",  
 	},  
 	colModel:[
 	{name : 'fid',label : 'fid',hidden:true},
 	{name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\"dailyList\")' />"; }},
 	{name : 'fchecked',label : '勾对',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
 		for(var i=0; i<checkedData.length; i++){
 			if (checkedData[i].value == cellvalue) return checkedData[i].text;
 		}
 		return cellvalue;
 	}}, 
 	{name:'voucherDate',label:'单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
 		return cellvalue.substring(0,10);
 	}},
 	{name:'settlementDate',label:'业务日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
 		return cellvalue.substring(0,10);
 	}},
 	{name:'orderno',label:'当天顺序号',align:'center',width:"100px"},
 	{name:'settlementTypeName',label:'结算方式',align:'center',width:"100px"},
 	{name:'settlementNo',label:'结算号',align:'center',width:"100px"},
 	{name:'resume',label:'摘要',align:'center',width:"100px"},
 	{name:'debit',label:'借方金额',align:'center',width:"100px"},
 	{name:'credit',label:'贷方金额',align:'center',width:"100px"},
 	{name:'memberName',label:'经手人',align:'center',width:"100px"},
 	{name:'checkDate',label:'勾对日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
 		return cellvalue.substring(0,10);
 	}}
 	],
 }).navGrid('#dailyPager',{add:false,del:false,edit:false,search:false,view:false}); 
 
 $("#balanceList").jqGrid({
 	datatype:function(postdata){
 		if(!postdata.subjectId){
 			return false;
 		}
 		$.ajax({
 			url: '${ctx}/cashierBankBillController/listBankBill?showDefault=0&types=1,4',
 			data:postdata,
 			dataType:"json",
 			complete: function(data,stat){
 				if(stat=="success") {
 					data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
 					$("#balanceList")[0].addJSONData(data.responseJSON);
 				}
 			}
 		});
 	},
 	autowidth:true,
 	height:180,
 	pager:"#balancePager",
 	rowNum:10,
 	rowList:[10,20,30],
 	viewrecords:true,
 	jsonReader:{
 		records:"total",
 		total: "totalpages",  
 	},  
 	colModel:[
 	{name : 'fid',label : 'fid',hidden:true},
 	{name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\"balanceList\")'/>"; }},
 	{name : 'fchecked',label : '勾对',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
 		for(var i=0; i<checkedData.length; i++){
 			if (checkedData[i].value == cellvalue) return checkedData[i].text;
 		}
 		return cellvalue;
 	}}, 
 	{name:'voucherDate',label:'单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
 		return cellvalue.substring(0,10);
 	}},
 	{name:'orderno',label:'当天顺序号',align:'center',width:"100px"},
 	{name:'settlementTypeName',label:'结算方式',align:'center',width:"100px"},
 	{name:'settlementNo',label:'结算号',align:'center',width:"100px"},
 	{name:'resume',label:'摘要',align:'center',width:"100px"},
 	{name:'debit',label:'借方金额',align:'center',width:"100px"},
 	{name:'credit',label:'贷方金额',align:'center',width:"100px"},
 	{name:'checkDate',label:'勾对日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
 		return cellvalue.substring(0,10);
 	}}
 	],
 }).navGrid('#balancePager',{add:false,del:false,edit:false,search:false,view:false}); 

 function mysubjectName(){
 	chooserWindow_search=$.fool.window({width:780,height:480,'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubjectSearch&onDblClick=selectSubjectDBCSearch&singleSelect=true&bankSubject=1&flag=1',
		  onLoad:function(){subjectCombo.closeAll();}});//解决IE点击弹窗下拉框不消失问题
 }
 function selectSubjectSearch(rowData){
 	if(rowData[0].name == "科目"){
 		$.fool.alert({msg:"请选择科目子节点！"});
 		return false;
 	}
 	$('#search-subjectId').val(rowData[0].fid);
 	subjectCombo.setComboValue(rowData[0].fid);
 	chooserWindow_search.window('close');
 }
 function selectSubjectDBCSearch(rowData){
 	if(rowData.name == "科目"){
 		$.fool.alert({msg:"请选择科目子节点！"});
 		return false;
 	}
     $('#search-subjectId').val(rowData.fid);
     subjectCombo.setComboValue(rowData.fid);
 	chooserWindow_search.window('close');
 }
 enterSearch('search-btn');

 function checker(target,billId){
 	$($("#"+billId)[0]).find(".checker").prop("checked",false);
 	$(target).prop('checked',true);
 }
 function getComboData(url,mark){
 	var result="";
 	$.ajax({
 		url:url,
 		data:{num:Math.random()},
 		async:false,
 		success:function(data){
 			if(data){
 				result=data;
 			}
 		}
 	});
 	if(mark!="tree"){
 		return formatData(result,"fid");
 	}else{
 		return formatTree(result[0].children,"text","id");
 	}
 }
</script>
</body>
</html>