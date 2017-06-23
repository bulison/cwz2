<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
<c:set var="flagName" value="${empty vo.recordStatus?'新增':subjectEdit=='false'?'查看':'编辑'}" scope="page"></c:set>
    <form action="" class="bill-form myform" id="form">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn"><div id="triangle"></div></div><h1>${flagName}待摊费用</h1><a id="hide" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">费用信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写费用信息</h2></div>
		<div class="in-box" id="list2">
		  	<div class="inlist">
                <input id="fid" name="fid" type="hidden" value="${vo.fid}"/>
                <input id="updateTime" name="updateTime" type="hidden" value="${vo.updateTime}"/>
                <input id="creatorId" type="hidden" value="${vo.creatorId}"/>
                <input id="auditorId" type="hidden" value="${vo.auditorId}"/>
                <p>
	                <font><em>*</em>费用编号：</font><input id="expensesCode" name="expensesCode" class="textBox" value="${vo.expensesCode}" />
					<font><em>*</em>费用名称：</font><input id="expensesName" name="expensesName" class="textBox" value="${vo.expensesName}" />
					<font><em>*</em>部门：</font><input id="deptName" name="deptName" class="textBox"/><input id="deptId" name="deptId" type="hidden" value="${vo.deptId}"/>
				</p>
				<p>
					<font><em>*</em>待摊金额：</font><input id="expensesAmount" name="expensesAmount" class="textBox" value="${vo.expensesAmount}"/>
					<font><em>*</em>预计待摊月份：</font><input id="discountPeriod" name="discountPeriod" class="textBox" value="${vo.discountPeriod}" />
					<font><em>*</em>发生日期：</font><input id="shareDate" name="shareDate" class="textBox" value="${vo.shareDate}"/>
				</p>
				<p>
					<font>借方科目：</font><input id="debitSubjectName" show="true" name="debitSubjectName" class="textBox" value="${vo.debitSubjectName}"/><input id="debitSubjectId" name="debitSubjectId" type="hidden" value="${vo.debitSubjectId}"/>
					<font>贷方科目：</font><input id="creditSubjectName" name="creditSubjectName" class="textBox" value="${vo.creditSubjectName}"/><input id="creditSubjectId" name="creditSubjectId" type="hidden" value="${vo.creditSubjectId}"/>
					<font>状态：</font><input id="recordStatus" class="textBox"  readonly="readonly" value="${vo.recordStatus}" disabled="disabled"/>
				</p>
				<p>
					<font>备注：</font><input id="remark" name="remark" class="textBox" value="${vo.remark}"/>
				</p>
				<p>
					<span style="display: inline-block;margin-left:44px;font-weight:700;">&emsp;其他信息：</span><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none">
				</p>
				<p class="hideOut">
					<font>创建人：</font><input id="creatorName" class="textBox" readonly="readonly" value="${vo.creatorName}"/>
					<font>创建日期：</font><input id="createTime" class="textBox" readonly="readonly" value="${vo.createTime}"/>
					<font>审核人：</font><input id="auditorName" class="textBox" readonly="readonly" value="${vo.auditorName}"/>
				</p>
				<p class="hideOut"><font>审核日期：</font><input id="auditTime" class="textBox" readonly="readonly" value="${vo.auditTime}"/></p>
			 </div>
		</div>
	</div>
	<div id="dataBox" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>待摊金额记录</h2></div>
		<div class="in-box" id="list1">
			<table id="expensesList"></table>
		</div>
	</div>
	

</form>
<div class="mybtn-footer" id="btnBox"></div>
 <div class="repertory"></div>
<div id="scroll1" class="scroll1">
	<div id="scroll2" class="scroll2"></div>
</div>
<script>
var subjectEdit = '${subjectEdit}';
$('.backBtn').click(function(){
	$('#addBox').window('close');
});
$('#hide').click(function(){
	var $myobj = $(this);
	$myobj.closest('.window-body').animate({scrollTop:0},500);
});
$("#expensesCode").validatebox({
	required:true,
	novalidate:true,
	missingMessage:'该项不能为空！',
	validType:'maxLength[50]'
});
$("#expensesName").validatebox({
	required:true,
	novalidate:true,
	missingMessage:'该项不能为空！',
	validType:'maxLength[50]'
});
$("#expensesAmount").validatebox({
	required:true,
	novalidate:true,
	missingMessage:'该项不能为空！',
	validType:["amount","numMaxLength[10]"]//添加数字位数限制
});
$("#discountPeriod").numberbox({
	width:182,
	height:31,
	required:true,
	novalidate:true,
	missingMessage:'该项不能为空！',
	min:1,
	validType:"numMaxLength[10]"//添加数字位数限制
});
$('#shareDate').datebox({
	required:true,
	novalidate:true,
	editable:false,
	missingMessage:'该项不能为空！',
	width:182,
	height:31,
	validType:""
});
/* $("#recordStatus").combobox({
	editable:false,
	hasDownArrow:false,
	width:182,
	height:31,
	data:[{value:0,text:"未审核"},{value:1,text:"已审核"}]
}); */
$('#recordStatus').fool("dhxCombo",{
	width:182,
	height:31,
	editable:false,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	focusShow:true,
	data:[{value:0,text:"未审核"},{value:1,text:"已审核"}]
});
($('#recordStatus').next())[0].comboObj.disable();
/* $("#debitSubjectName").fool("subjectCombobox",{
	width:182,
	height:31,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
	focusShow:true,
	onClickIcon:function(){
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
    	debitSubjectName();
	},
	onSelect:function(record){
		$("#debitSubjectId").val(record.fid);
	}
}); */
$('#debitSubjectName').fool('dhxComboGrid',{
	width:182,
	height:31,
	focusShow:true,
  	data:getComboData(getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&q=&num="+Math.random()),
  	filterUrl:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
  	searchKey:"q",
  	setTemplate:{
  		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
  	},
	toolsBar:{
	    name:'借方科目',
		addUrl:'/fiscalSubject/manage',
		refresh:true
	},
  	onSelectionChange:function(){
		$("#debitSubjectId").val(($('#debitSubjectName').next())[0].comboObj.getSelectedValue());
	}
  });
$('#debitSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  ($('#debitSubjectName').next())[0].comboObj.closeAll();
	  debitSubjectName();
});

/* $("#creditSubjectName").fool("subjectCombobox",{
	width:182,
	height:31,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
	focusShow:true,
	validType:"compareSub",
	onClickIcon:function(){
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
    	creditSubjectName();
	},
	onSelect:function(record){
		$("#creditSubjectId").val(record.fid);
	}
}); */
$('#creditSubjectName').fool('dhxComboGrid',{
	width:182,
	height:31,
	focusShow:true,
  	data:getComboData(getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&q=&num="+Math.random()),
  	filterUrl:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
  	searchKey:"q", 
  	setTemplate:{
  		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
  	},
	toolsBar:{
	    name:'贷方科目',
		addUrl:'/fiscalSubject/manage',
		refresh:true
	},
  	onSelectionChange:function(){
		$("#creditSubjectId").val(($('#creditSubjectName').next())[0].comboObj.getSelectedValue());
	}
  });
$('#creditSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  ($('#creditSubjectName').next())[0].comboObj.closeAll();
	  creditSubjectName();
});
/* $('#deptName').fool('deptComBoxTree',{
	required: true,
	novalidate:true,
	editable:false,
	width:182,
	height:31,
	valTarget:'#deptId',
	onLoadSuccess:function(node,data){
		$('#deptName').combotree("setValue","${vo.deptId}");
		$("#deptId").val('${vo.deptId}');
	}}); */
$('#deptName').fool("dhxCombo",{
	required: true,
	novalidate:true,
	editable:false,
	width:182,
	height:31,
	value:"${vo.deptId}",
	setTemplate:{
		input:"#text#",
		option:"#text#"
	},
	focusShow:true,
	data:getComboData(getRootPath()+'/orgController/getAllTree',"tree"),
	toolsBar:{
        name:"部门",
        addUrl:"/orgController/deptMessage",
        refresh:true
    },
	onSelectionChange:function(){
		$("#deptId").val(($('#deptName').next())[0].comboObj.getSelectedValue());
		$(($('#deptName').next())[0].comboObj.getInput()).validatebox("validate");
	}
});

$("#expensesList").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/prepaidExpenses/queryDetails?fid=${vo.fid}',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#expensesList")[0].addJSONData(data.responseJSON);
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
	          {name : 'fid',label : 'fid',hidden:true}, 
              {name : 'date',label : '日期',align:'center',width:"100px"}, 
              {name : 'amount',label : '金额',align:'center',width:"100px"}, 
              {name : 'remark',label : '备注',align:'center',width:"100px"}, 
              {name : 'creatorName',label : '创建人',align:'center',width:"100px"}, 
              ],
    onLoadSuccess:function(){
    	scrollDiy();
    	var soffset = $('#dataBox').offset();
  	   	var $window = $('#dataBox').closest('.window-body');
  	   	var offset = $('.in-box #expensesList').offset();
  	   	$window.scroll(function () {
  	   		var scrollTop = $(this).scrollTop();
  	   		if (soffset.top <= (scrollTop+86)){
  	   			$('#hide').fadeIn();
  	   		}else{
  	   			$('#hide').fadeOut();
  	   		}if (offset.top <= (scrollTop+26)){
  	   			$('#list1 .ui-jqgrid-bdiv').css('padding-top',35);
  	   			$('#list1 .ui-jqgrid-hdiv').css('width',$('#dataBox').width()-25);
  	   			$('#list1 .ui-jqgrid-hdiv').css('top',$('#title').outerHeight(true)+11);
  	   			$('#list1 .ui-jqgrid-hdiv').addClass('fixed');
  	   		}else{
  	   			$('#list1 .ui-jqgrid-bdiv').css('padding-top',0);
  	   			$('#list1 .ui-jqgrid-hdiv').css('top','');
  	   			$('#list1 .ui-jqgrid-hdiv').removeClass('fixed');
  	   		} 
  	   	});
    }
});

function debitSubjectName(){
	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectDebitSubject&onDblClick=selectDebitSubjectDBC&singleSelect=true&flag=1',
		  onLoad:function(){($('#debitSubjectName').next())[0].comboObj.closeAll();}});//解决IE点击弹窗下拉框不消失问题
}
function creditSubjectName(){
	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectCreditSubject&onDblClick=selectCreditSubjectDBC&singleSelect=true&flag=1',
		  onLoad:function(){($('#creditSubjectName').next())[0].comboObj.closeAll();}});//解决IE点击弹窗下拉框不消失问题
}

function selectDebitSubject(rowData){
	  $("#debitSubjectId").val(rowData[0].fid);
	  ($("#debitSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window("close");
}
function selectDebitSubjectDBC(rowData){
	  $("#debitSubjectId").val(rowData.fid);
	  ($("#debitSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window("close");
}
function selectCreditSubject(rowData){
	  $("#creditSubjectId").val(rowData[0].fid);
	  ($("#creditSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window("close");
}
function selectCreditSubjectDBC(rowData){
	  $("#creditSubjectId").val(rowData.fid);
	  ($("#creditSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window("close");
}
var mysave = "";
if(subjectEdit == "true"){
	mysave = '<input type="button" id="save" class="btn-blue2 btn-xs" value="保存" />';
}
if('${vo.recordStatus}'==null||'${vo.recordStatus}'==''){
	$("#btnBox").append('<input type="button" id="save" class="btn-blue2 btn-xs" value="保存" />');
}else if('${vo.recordStatus}'==0){
	$("#btnBox").append(mysave+'<input type="button" id="checker" class="btn-blue2 btn-xs" value="审核" /><input type="button" id="delete" class="btn-blue2 btn-xs" value="删除" />');
}else if('${vo.recordStatus}'==1){
	disabledAll();
	$("#btnBox").append(mysave+'<input type="button" id="unchecker" class="btn-blue2 btn-xs" value="反审核" />');
}

function disabledAll(){
	//$('#form').find('input').attr('disabled','disabled');
	$('#form').find('input').each(function(e){
		$(this).attr('disabled','disabled');
	});
	($('#deptName').next())[0].comboObj.readonly(true);
	$('#buyDate').datebox('readonly',true);
	$('#shareDate').datebox('readonly',true);
	($("#debitSubjectName").next())[0].comboObj.disable();
	($("#creditSubjectName").next())[0].comboObj.disable();
	if(subjectEdit == "true"){
		($("#debitSubjectName").next())[0].comboObj.enable();
		$("#debitSubjectId").removeAttr('disabled');
		($("#creditSubjectName").next())[0].comboObj.enable();
		$("#creditSubjectId").removeAttr('disabled');
	}
}

$("#openBtn").click(function(e) {
	$(".hideOut").css("display", "inline-block");
	$('#openBtn').css("display", "none");
	$('#closeBtn').css("display", "inline-block");
});
$("#closeBtn").click(function(e) {
	$(".hideOut").css("display", "none");
	$('#openBtn').css("display", "inline-block");
	$('#closeBtn').css("display", "none");
});

$('#save').click(function(){
	$('#form').form('enableValidation');
	if ($('#form').form('validate')) {
		var vo=$('#form').serializeJson();
		if('${vo.recordStatus}'!=''){
			var fid = $('#fid').val();
			var updateTime = $('#updateTime').val();
			var expensesCode = $('#expensesCode').val();
			var expensesName = $('#expensesName').val();
			var deptId = ($('#deptName').next())[0].comboObj.getSelectedValue();
			var expensesAmount = $('#expensesAmount').val();
			var discountPeriod = $('#discountPeriod').numberbox('getValue');
			var shareDate = $('#shareDate').datebox('getValue');
			var remark = $('#remark').val();
			vo = $.extend(vo,{fid:fid,updateTime:updateTime,expensesCode:expensesCode,expensesName:expensesName,deptId:deptId,expensesAmount:expensesAmount,discountPeriod:discountPeriod,shareDate:shareDate,remark:remark});
		}
		$.ajax({
		    url: "${ctx}/prepaidExpenses/save",
		    type: "POST",
		    async:false,
		    data: vo,
		    success:function(data){
		    	if(data.returnCode==0){
		    		$.fool.alert({time:2000,msg:"保存成功！",fn:function(){
						$('#addBox').window('close');
						$('#cardList').trigger("reloadGrid"); 
					}});
		    	}else if(data.returnCode==1){
		    		$.fool.alert({msg:data.message});
		    	}else{
					$.fool.alert({msg:'系统繁忙，请稍后再试。'});
				}
			}
		});
	}
});

$("#delete").click(function(){
	$.fool.confirm({
		title:'删除提示',
		msg:'确认删除此数据？',
		fn:function(data){
			if(data){
				$.ajax({
				    url: "${ctx}/prepaidExpenses/delete?fid="+$("#fid").val(),
				    type: "POST",
				    async:false,
				    success:function(result) {
				    	if(result.returnCode==0){
				    		$.fool.alert({msg:'删除成功！',fn:function(){
				    			$('#cardList').trigger("reloadGrid"); 
				    			$('#addBox').window('close');
				    		},time:2000});
						}else if(result.returnCode==1){
							$.fool.alert({msg:data.message});
						}else{
							$.fool.alert({msg:'系统繁忙，请稍后再试。'});
						}
					}
				});
			}
		}
	});
});

$("#checker").click(function(){
	var fid=$("#fid").val();
	if(checker(fid)){
		$('#addBox').window('close');
	};
});

$("#unchecker").click(function(){
	var fid=$("#fid").val();
	if(unchecker(fid)){
		$('#addBox').window('close');
	};
});

$.extend($.fn.validatebox.defaults.rules, {
	compareSub: {    
        validator: function(value, param){
        	var debitSubjectId=$("#debitSubjectId").val();
        	var creditSubjectId=$("#creditSubjectId").val();
        	if(debitSubjectId==creditSubjectId){
        		return false;
        	}else{
        		return true;
        	}
        },    
        message: '借方科目不能与贷方科目相同'   
    },
}); 
if('${vo.recordStatus}'==1){
	enterController('form',true);
}else{
	enterController('form');
}
</script>
</body>
</html>