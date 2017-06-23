<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<body>
<style>
.dhxDiv{margin-right: 0px;}
</style>
<c:set var="flagName" value="${empty asset.recordStatus?'新增':subjectEdit=='false'?'查看':'编辑'}" scope="page"></c:set>
    <form action="" class="bill-form myform" id="form">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn"><div id="triangle"></div></div><h1>${flagName}固定资产卡片</h1><a id="hide" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">资产信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写资产信息</h2></div>
		<div class="in-box" id="list2">
		  	<div class="inlist">
                <input id="fid" type="hidden" value="${asset.fid}"/>
                <input id="updateTime" type="hidden" value="${asset.updateTime}"/>
                <input id="creatorId" type="hidden" value="${asset.creatorId}"/>
                <input id="auditorId" type="hidden" value="${asset.auditorId}"/>
                <p>
	                <font><em>*</em>资产编号：</font><input id="assetCode" class="textBox easyui-validatebox" value="${asset.assetCode}" data-options="required:true,novalidate:true,missingMessage:'该项不能为空！',validType:'maxLength[50]'"/>
					<font><em>*</em>资产名称：</font><input id="assetName" class="textBox easyui-validatebox" value="${asset.assetName}" data-options="required:true,novalidate:true,missingMessage:'该项不能为空！',validType:'maxLength[50]'"/>
					<font><em>*</em>资产类别：</font><input id="assetTypeId" class="textBox" value="${asset.assetTypeId}"/><%-- <input id="assetTypeId" type="hidden" value="${asset.assetTypeId}"/> --%>
				</p>
				<p>
					<font><em>*</em>部门：</font><input id="deptId" class="textBox" value="${asset.deptId}"/><%-- <input id="deptId" type="hidden" value="${asset.deptId}"/> --%>
					<font style="margin-left:6px;"><em>*</em>数量：</font><input id="quentity" class="textBox easyui-validatebox easyui-numberbox" value="${asset.quentity}" data-options="width:182,height:32,precision:2,required:true,novalidate:true,missingMessage:'该项不能为空！',validType:['numMaxLength[10]','assetValue[2]'],"/>
					<font><em>*</em>资产原值：</font><input id="initialValue" class="textBox" value="${asset.initialValue}"/>
				</p>
				<p>
					<font><em>*</em>折旧年限：</font><input id="discountYear" class="textBox easyui-validatebox" value="${asset.discountYear}" data-options="validType:'yearNumber',required:true,novalidate:true,missingMessage:'该项不能为空！'"/>
					<font><em>*</em>残值率：</font><input id="residualRate" class="textBox easyui-validatebox easyui-numberbox" value="${asset.residualRate}" data-options="width:182,height:32,precision:2,validType:'residualValid',required:true,novalidate:true,missingMessage:'该项不能为空！'"/>
					<font><em>*</em>购买日期：</font><input id="buyDate" class="textBox" value="${asset.buyDate}"/>
				</p>
				<p>
					<font><em>*</em>使用日期：</font><input id="useDate" class="textBox" value="${asset.useDate}"/>
					<font><em>*</em>计提日期：</font><input id="shareDate" class="textBox" value="${asset.shareDate}"/>
					<font>资产净值：</font><input id="residualValue" class="textBox" disabled="disabled" value="${asset.residualValue}"/>
				</p>
				<p>
					<font>累计已提折旧：</font><input id="sumAccruedValue" class="textBox" disabled="disabled"  value="${asset.sumAccruedValue}"/>
					<font>来源：</font><input id="supplier" class="textBox" value="${asset.supplier}"/>
					<font>等级：</font><input id="grade" class="textBox" value="${asset.grade}"/>
				</p>
				<p>
					<font>产地及厂家：</font><input id="manufactor" class="textBox" value="${asset.manufactor}"/>
					<font>规格型号：</font><input id="model" class="textBox" value="${asset.model}"/>
					<font>固定资产科目：</font><input id="assetSubjectName" name="assetSubjectName" class="textBox" value="${asset.assetSubjectName}"/><input id="assetSubjectId" name='assetSubjectId' type="hidden" value="${asset.assetSubjectId}"/>
				</p>
				<p>
					<font>折旧科目：</font><input id="depreciationSubjectName" name="depreciationSubjectName" class="textBox" value="${asset.depreciationSubjectName}"/><input id="depreciationSubjectId" type="hidden" name="depreciationSubjectId" value="${asset.depreciationSubjectId}"/>
					<font>支付科目：</font><input id="paymentSubjectName" name="paymentSubjectName" class="textBox" value="${asset.paymentSubjectName}"/><input id="paymentSubjectId" type="hidden" name="paymentSubjectId" value="${asset.paymentSubjectId}"/>
					<font>清算科目：</font><input id="clearSubjectName" name="clearSubjectName" class="textBox" value="${asset.clearSubjectName}"/><input id="clearSubjectId" type="hidden" name="clearSubjectId" value="${asset.clearSubjectId}"/>
				</p>
				<p>
					<font>费用科目：</font><input id="expenseSubjectName" name="expenseSubjectName" class="textBox" value="${asset.expenseSubjectName}"/><input id="expenseSubjectId" type="hidden" name="expenseSubjectId" value="${asset.expenseSubjectId}"/>
					<font>状态：</font><input id="statusName" class="textBox"  readonly="readonly" value="" disabled="disabled"/><input id="recordStatus" type="hidden" value="${asset.recordStatus}"/>
				</p>
				<p>
					<span style="display: inline-block;margin-left:44px;font-weight:700;">&emsp;其他信息：</span><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none">
				</p>
				<p class="hideOut">
					<font>创建人：</font><input id="creatorName" class="textBox" readonly="readonly" value="${asset.creatorName}"/>
					<font>创建日期：</font><input id="createTime" class="textBox" readonly="readonly" value="${asset.createTime}"/>
					<font>审核人：</font><input id="auditorName" class="textBox" readonly="readonly" value="${asset.auditorName}"/>
				</p>
				<p class="hideOut"><font>审核日期：</font><input id="auditTime" class="textBox" readonly="readonly" value="${asset.auditTime}"/></p>
			 </div>
		</div>
	</div>
	<div id="dataBox" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>资产计提记录</h2></div>
		<div class="in-box" id="list1">
			<table id="assetList"></table>
		</div>
	</div>
	

</form>
<div class="mybtn-footer" id="btnBox"></div>
 <div class="repertory"></div>
<div id="scroll1" class="scroll1">
	<div id="scroll2" class="scroll2"></div>
</div>
<script>
var _id = '${asset.fid}';
var subjectEdit = '${subjectEdit}';
//部门初始化	
var deptValue='';	
	$.ajax({
		url:"${ctx}/orgController/getAllTree?num="+Math.random(),
		async:false,		
		success:function(data){		  	
			deptValue=formatData(data[0].children,'id','texr');	
	    }
		});
	var deptNameValue= $("#deptId").fool("dhxCombo",{
		  width:182,
		  height:32,
		clearOpt:false,
		  data:deptValue,
        toolsBar:{
            name:"部门",
            addUrl:"/orgController/deptMessage",
            refresh:true
        },
        required:true,
		  novalidate:true,
		  editable:false,
		  focusShow:true,
		  onLoadSuccess:function(combo){
				combo.setComboValue("${entity.deptId}");
			}
	});	
$('.backBtn').click(function(){
	$('#addBox').window('close');
});
$('#hide').click(function(){
	var $myobj = $(this);
	$myobj.closest('.window-body').animate({scrollTop:0},500);
});

//资产类型下拉框	
var assetTypeValue='';	
	$.ajax({
		url: '${ctx}/basedata/assetType?num='+Math.random(),
		async:false,		
		success:function(data){		  	
			assetTypeValue=formatTree(data[0].children,'name','id');	
	    }
		});
	var assetTypeName= $("#assetTypeId").fool("dhxCombo",{
		  width:182,
		  height:32,
		clearOpt:false,
		  data:assetTypeValue,
        toolsBar:{
            name:"资产类型",
            addUrl:"/basedata/listAuxiliaryAttr",
            refresh:true
        },
        setTemplate:{
        	input:"#name#",
        	option:"#name#"
        },
        required:true,
		  novalidate:true,
		  editable:false,
		  focusShow:true,
		  onLoadSuccess:function(combo){
				combo.setComboValue("${entity.assetTypeId}");
			}
	});	
//输入框初始化
$('#initialValue').numberbox({
	required:true,
	novalidate:true,
	missingMessage:'该项不能为空！',
	precision:'2',
	width:182,
	height:31,
	validType:['numMaxLength[10]','assetValue[2]'],
	onChange:function(){
		$('#residualValue').val($('#initialValue').val()-$('#sumAccruedValue').val());
	}
});

/* $('#initialValue').numberbox({
	required:true,
	novalidate:true,
	missingMessage:'该项不能为空！',
	precision:'2',
	width:182,
	height:31,
	validType:['numMaxLength[10]','assetValue[2]'],
});
 */
$('#useDate').datebox({
	required:true,
	novalidate:true,
	editable:false,
	missingMessage:'该项不能为空！',
	width:182,
	height:31,
	validType:"usedateValid" 
});
$('#buyDate').datebox({
	required:true,
	novalidate:true,
	editable:false,
	missingMessage:'该项不能为空！',
	width:182,
	height:31,
});
$('#shareDate').datebox({
	required:true,
	novalidate:true,
	editable:false,
	missingMessage:'该项不能为空！',
	width:182,
	height:31,
	validType:"sharedateValid"
});
 /* $("#assetSubjectName").fool('subjectCombobox',{
	width:182,
	height:31,
	focusShow:true,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
	onSelect:function(record){
		$("#assetSubjectId").val(record.fid);
	},
	onClickIcon:function(){
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
    	assetSubjectWin();
	}
});  */
//数据初始化
var asValue="";
$.ajax({
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&q=&num="+Math.random(),
	async:false,
	data:{},
	success:function(data){	
		asValue=formatData(data,"fid");	   
    }
	});

var asName=$('#assetSubjectName').fool('dhxComboGrid',{
	width:182,
	height:31,
	focusShow:true,
	data:asValue,
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
        name:"固定资产科目",
        addUrl:"/fiscalSubject/manage",
        refresh:true
    },
    onSelectionChange:function(){
		$("#assetSubjectId").val(($('#assetSubjectName').next())[0].comboObj.getSelectedValue());
	}
  });
 $('#assetSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  ($('#assetSubjectName').next())[0].comboObj.closeAll();
	  assetSubjectWin();
});
 

/* $("#depreciationSubjectName").fool('subjectCombobox',{
	width:182,
	height:31,
	focusShow:true,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
	onSelect:function(record){
		$("#depreciationSubjectId").val(record.fid);
	},
	onClickIcon:function(){
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
    	depreciationSubjectWin();
	}
}); */
var depreciationName=$('#depreciationSubjectName').fool('dhxComboGrid',{
	width:182,
	height:31,
	focusShow:true,
	data:asValue,
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
        name:"折旧科目",
        addUrl:"/fiscalSubject/manage",
        refresh:true
    },
    onSelectionChange:function(){
		$("#depreciationSubjectId").val(($('#depreciationSubjectName').next())[0].comboObj.getSelectedValue());
	}
  });
$('#depreciationSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  ($('#depreciationSubjectName').next())[0].comboObj.closeAll();
	  depreciationSubjectWin();
});
/* $("#paymentSubjectName").fool('subjectCombobox',{
	width:182,
	height:31,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
	focusShow:true,
	onSelect:function(record){
		$("#paymentSubjectId").val(record.fid);
	},
	onClickIcon:function(){
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
    	paymentSubjectWin();
	}
}); */
var paymentName=$('#paymentSubjectName').fool('dhxComboGrid',{
	width:182,
	height:31,
	focusShow:true,
	data:asValue,
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
        name:"支付科目",
        addUrl:"/fiscalSubject/manage",
        refresh:true
    },
  	onSelectionChange:function(){		
		$("#paymentSubjectId").val(($('#paymentSubjectName').next())[0].comboObj.getSelectedValue());
	}
  });
$('#paymentSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  ($('#paymentSubjectName').next())[0].comboObj.closeAll();
	  paymentSubjectWin();
});

/* $("#clearSubjectName").fool('subjectCombobox',{
	width:182,
	height:31,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
	focusShow:true,
	onSelect:function(record){
		$("#clearSubjectId").val(record.fid);
	},
	onClickIcon:function(){
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
    	clearSubjectWin();
	}
}); */
var clearName=$('#clearSubjectName').fool('dhxComboGrid',{
	width:182,
	height:31,
	focusShow:true,
	data:asValue,
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
        name:"清算科目",
        addUrl:"/fiscalSubject/manage",
        refresh:true
    },
  	onSelectionChange:function(){		
		$("#clearSubjectId").val(($('#clearSubjectName').next())[0].comboObj.getSelectedValue());
	}
  });
$('#clearSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  ($('#clearSubjectName').next())[0].comboObj.closeAll();
	  clearSubjectWin();
});

/* $("#expenseSubjectName").fool('subjectCombobox',{
	width:182,
	height:31,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1",
	focusShow:true,
	onSelect:function(record){
		$("#expenseSubjectId").val(record.fid);
	},
	onClickIcon:function(){
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
    	expenseSubjectWin();
	}
}); */
var SubjectName=$('#expenseSubjectName').fool('dhxComboGrid',{
	width:182,
	height:31,
	focusShow:true,
	data:asValue,
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
        name:"费用科目",
        addUrl:"/fiscalSubject/manage",
        refresh:true
    },
  	onSelectionChange:function(){		
		$("#expenseSubjectId").val(($('#expenseSubjectName').next())[0].comboObj.getSelectedValue());
	}
  });
$('#expenseSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  ($('#expenseSubjectName').next())[0].comboObj.closeAll();
	  expenseSubjectWin();
});
//定义日期对比验证
$.extend($.fn.validatebox.defaults.rules, {    
    usedateValid: {    
        validator: function(value, param){
        	var date = $('#buyDate').datebox('getValue');
        	var sdate = new Date(date.replace("-", "/").replace("-", "/")); 
        	var fdate = new Date(value.replace("-", "/").replace("-", "/"));
            return fdate >= sdate;    
        },    
        message: '使用日期不能前于购买日期'   
    },
	sharedateValid: {    
    validator: function(value, param){
    	var date = $('#buyDate').datebox('getValue');
    	var sdate = new Date(date.replace("-", "/").replace("-", "/")); 
    	var fdate = new Date(value.replace("-", "/").replace("-", "/"));
        return fdate >= sdate;    
    	},    
    	message: '计提日期不能前于购买日期'   
	},
	residualValid:{
	validator: function(value, param){
		return (/^([0]+\.[0-9]{1,2})$/).test(value);
	},
	message: '必须是小于1且小数点最多2位的数'
	},
	yearNumber:{
  	  validator:function(value){
  		  var n = /^([+]?[0-9])+\d*$/i.test(value);
  		  if(!n)return false;
          else 
       	  return value>0;
  	  },
  	  message: '请输入大于0的整数'
    }
});  

$(function(){
	 /* $("#assetList").datagrid({
		url:'${ctx}/assetdetail/queryDetail?assetId='+'${asset.fid}',
		fitColumns:true,
		sortName:'date',
		singleSelect:true,
		sortOrder:'asc',
		columns:[[
			  		{field:'fid',title:'fid',hidden:true,width:100},
			  		{field:'type',title:'类型',width:100,formatter:function(value){
			  			switch(value){
			  			case 1: return '资产购入';
			  			case 2: return '资产折旧';
			  			case 3: return '资产清算';
			  			}
			  		}},
					{field:'date',title:'日期',sortable:true,width:100}, 
					{field:'amount',title:'金额',sortable:true,width:100},
					{field:'remark',title:'备注',width:100},
					{field:'action',title:'操作',width:100,formatter:function(value,row,index){
						var d= '<a class="btn-del" title="删除" href="javascript:;" onclick="deleter(\''+row.fid+'\')"></a>';
			  			return d;
					}}
			      ]],
		onLoadSuccess:function(){
			scrollDiy();
			var soffset = $('#dataBox').offset();
   			var $window = $('#dataBox').closest('.window-body');
   			var offset = $('.in-box .datagrid').offset();
   			$window.scroll(function () {
   				var scrollTop = $(this).scrollTop();
   				if (soffset.top <= (scrollTop+86)){
   					$('#hide').fadeIn();
   				}
   				else{
   					$('#hide').fadeOut();
   				}
   				if (offset.top <= (scrollTop+26)){
   					$('#list1 .datagrid-body').css('padding-top',35);
   					$('#list1 .datagrid-header').css('width',$('#dataBox').width()-25);
   					$('#list1 .datagrid-header').css('top',$('#title').outerHeight(true)+11);
   					$('#list1 .datagrid-header').addClass('fixed');
   				}
   				else{
   					$('#list1 .datagrid-body').css('padding-top',0);
   					$('#list1 .datagrid-header').css('top','');
   					$('#list1 .datagrid-header').removeClass('fixed');
   				} 
   			});
		}
	}); 
	 */
	$("#assetList").jqGrid({
		url:'${ctx}/assetdetail/queryDetail?assetId='+'${asset.fid}',
		datatype:"json",
		autowidth:true,//自动填满宽度
		height:"100%",
		mtype:"post",		
		viewrecords:true,
		forceFit:true,//调整列宽度，表格总宽度不会改变
		colModel:[
			  		{name:'fid',label:'fid',hidden:true,width:100},
			  		{name:'type',label:'类型',align:'center',width:100,formatter:function(value){
			  			switch(value){
			  			case 1: return '资产购入';
			  			case 2: return '资产折旧';
			  			case 3: return '资产清算';
			  			}
			  		}},
					{name:'date',label:'日期',align:'center',sortable:true,width:100}, 
					{name:'amount',label:'金额',align:'center',sortable:true,width:100},
					{name:'remark',label:'备注',align:'center',width:100},
					{name:'action',label:'操作',align:'center',width:100,formatter:function(value,options,row){
						var d= '<a class="btn-del" title="删除" href="javascript:;" onclick="deleter(\''+row.fid+'\')"></a>';
			  			return d;
					}}
			      ],
			loadcomplete:function(){
			scrollDiy();
			var soffset = $('#dataBox').offset();
   			var $window = $('#dataBox').closest('.window-body');
   			var offset = $('.in-box .datagrid').offset();
   			$window.scroll(function () {
   				var scrollTop = $(this).scrollTop();
   				if (soffset.top <= (scrollTop+86)){
   					$('#hide').fadeIn();
   				}
   				else{
   					$('#hide').fadeOut();
   				}
   				if (offset.top <= (scrollTop+26)){
   					$('#list1 .datagrid-body').css('padding-top',35);
   					$('#list1 .datagrid-header').css('width',$('#dataBox').width()-25);
   					$('#list1 .datagrid-header').css('top',$('#title').outerHeight(true)+11);
   					$('#list1 .datagrid-header').addClass('fixed');
   				}
   				else{
   					$('#list1 .datagrid-body').css('padding-top',0);
   					$('#list1 .datagrid-header').css('top','');
   					$('#list1 .datagrid-header').removeClass('fixed');
   				} 
   			});
		}
	});
	
	if(_id){
		if('${asset.sumAccruedValue}'=="0E-8"){
			$("#sumAccruedValue").val(0);
		}else{
			$("#sumAccruedValue").val('${asset.sumAccruedValue}');
		}
		if('${asset.residualValue}'=="0E-8"){
			$("#residualValue").val(0);
		}else{
			$("#residualValue").val('${asset.residualValue}');
		}
	}
	var mysave = "";
	if(subjectEdit == "true"){
		mysave = '<input type="button" id="save" class="btn-blue2 btn-xs" value="保存" />';
	}
	if('${asset.recordStatus}'==null||'${asset.recordStatus}'==''){
		$('#sumAccruedValue').val(0);
		$('#quentity').val(1);
		$('#discountYear').val(5);
		$('#residualRate').val(0.05);
		$("#btnBox").append('<input type="button" id="save" class="btn-blue2 btn-xs" value="保存" />');
		$('#statusName').val('未审核');
	}else if('${asset.recordStatus}'==0){
		$("#btnBox").append('<input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /><input type="button" id="checker" class="btn-blue2 btn-xs" value="审核" /><input type="button" id="delete" class="btn-blue2 btn-xs" value="删除" />');
		$('#statusName').val('未审核');
	}else if('${asset.recordStatus}'==1){
		disabledAll();
		$("#btnBox").append(mysave+'<input type="button" id="jt" class="btn-blue2 btn-xs" value="计提" /><input type="button" id="clear" class="btn-blue2 btn-xs" value="清算" /><input type="button" id="unchecker" class="btn-blue2 btn-xs" value="反审核" />');
		$('#statusName').val('审核');
	}else if('${asset.recordStatus}'==2){
		disabledAll();
		$("#btnBox").append(mysave+'<input type="button" id="jt" class="btn-blue2 btn-xs" value="计提" /><input type="button" id="clear" class="btn-blue2 btn-xs" value="清算" />');
		$('#statusName').val('计提中');
	}else if('${asset.recordStatus}'==3){
		disabledAll();
		$("#btnBox").append(mysave+'<input type="button" id="clear" class="btn-blue2 btn-xs" value="清算" />');
		$('#statusName').val('计提完成');
	}else if('${asset.recordStatus}'==4){
		disabledAll();
		$("#btnBox").append(mysave+'<input type="button" id="cancle" class="btn-blue2 btn-xs" value="关闭" />');
		$('#statusName').val('已清算');
	}
});
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
function disabledAll(){
	$('#form').find('input').each(function(e){
		$(this).attr('disabled','disabled');
	});
	asName.disable();
	depreciationName.disable();
	paymentName.disable();
	clearName.disable();
	SubjectName.disable();
	$('.dhxcombo_select_button').css({"display":"none"});	 
	/* $("#assetSubjectName").combobox('disable');
	$("#depreciationSubjectName").combobox('disable');
	$("#paymentSubjectName").combobox('disable');
	$("#clearSubjectName").combobox('disable');
	$("#expenseSubjectName").combobox('disable'); */
	if(subjectEdit == "true"){
	     asName.enable(); 
		depreciationName.enable(); 
		paymentName.enable();
		clearName.enable();
		SubjectName.enable(); 
		$('.dhxcombo_select_button').css("display","block");	 
	}
	/* $('#deptName').combotree('readonly',true);
	$('#assetTypeName').combotree('readonly',true); */
	deptNameValue.disable();
	assetTypeName.disable();
	$('#buyDate').datebox('readonly',true);
	$('#useDate').datebox('readonly',true);
	$('#shareDate').datebox('readonly',true);
}
function deleter(id) {
	$.ajax({
	    url: "${ctx}/assetdetail/deleteAssetDetail?fid="+id,
	    type: "POST",
	}).done(function(data) {
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:'删除成功！',fn:function(){
				$('#addBox').window('refresh');
				   $('#cardList').trigger('reloadGrid');
			},time:2000});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg});
		}else{
			$.fool.alert({msg:'系统繁忙，请稍后再试。'});
		}
	});
}
$('#unchecker').click(function(){
	var fid = $('#fid').val();
	$.ajax({
	    url: "${ctx}/asset/saveCancleAudit?fid="+fid,
	    type: "POST",
	}).done(function(data) {
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:'反审核完成！',fn:function(){
				$('#addBox').window('close');
				   $('#cardList').trigger('reloadGrid');
			},time:2000});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg});
		}else{
			$.fool.alert({msg:'系统繁忙，请稍后再试。'});
		}
	});
});
$('#clear').click(function(){
	var fid = $('#fid').val();
	$.ajax({
	    url: "${ctx}/asset/saveClear?fid="+fid,
	    type: "POST",
	}).done(function(data) {
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:'清算完成！',fn:function(){
				$('#addBox').window('refresh');
				   $('#cardList').trigger('reloadGrid');
			},time:2000});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg});
		}else{
			$.fool.alert({msg:'系统繁忙，请稍后再试。'});
		}
	});
});
$('#delete').click(function(){
	var fid = $('#fid').val();
	$.fool.confirm({
		title:'删除提示',
		msg:'确认删除资产卡片？',
		fn:function(data){
			if(data){
				$.ajax({
				    url: "${ctx}/asset/deleteAsset?fid="+fid,
				    type: "POST",
				}).done(function(data) {
					dataDispose(data);
					if(data.result==0){
						$.fool.alert({msg:'删除成功！',fn:function(){
							$('#addBox').window('close');
							$('#cardList').trigger('reloadGrid');
						},time:2000});
					}else if(data.result==1){
						$.fool.alert({msg:data.msg});
					}else{
						$.fool.alert({msg:'系统繁忙，请稍后再试。'});
					}
				});
			}else{
				return false;
			}
		}
	});
});
$('#jt').click(function(){
	var fid = $('#fid').val();
	$.ajax({
	    url: "${ctx}/asset/saveAccrued?fid="+fid,
	    type: "POST",
	}).done(function(data) {
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:'当次计提完成！',fn:function(){
				$('#addBox').window('refresh');
				   $('#cardList').trigger('reloadGrid');
			},time:2000});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg});
		}else{
			$.fool.alert({msg:'系统繁忙，请稍后再试。'});
		}
	});
});
$('#checker').click(function(){
	var fid = $('#fid').val();
		$.ajax({
		    url: "${ctx}/asset/savePassAudit?fid="+fid,
		    type: "POST",
		}).done(function(data) {
			dataDispose(data);
			if(data.result==0){
				$.fool.alert({msg:'审核完成！',fn:function(){
					$('#addBox').window('close');
					$('#cardList').trigger('reloadGrid');
				},time:2000});
			}else if(data.result==1){
				$.fool.alert({msg:data.msg});
			}else{
				$.fool.alert({msg:'系统繁忙，请稍后再试。'});
			}
		});
});
$('#cancle').click(function(){
	$('#addBox').window('close');
});
$('#save').click(function(){
	var fid = $('#fid').val();
	if(fid!=''){var rmsg='修改成功！';}else{var rmsg='新增成功！';}
	var updateTime = $('#updateTime').val();
	var assetCode = $('#assetCode').val();
	var assetName = $('#assetName').val();
	var quentity = $('#quentity').val();
	var initialValue = $('#initialValue').numberbox('getValue');
	var discountYear = $('#discountYear').val();
	var residualRate = $('#residualRate').val();
	var residualValue = $('#residualValue').val();
	var supplier = $('#supplier').val();
	var grade = $('#grade').val();
	var manufactor = $('#manufactor').val();
	var model = $('#model').val();
	var recordStatus = $('#recordStatus').val();
	var assetTypeId=assetTypeName.getSelectedValue();	
	var assetSubjectId=$('#assetSubjectId').val();
	var depreciationSubjectId=$('#depreciationSubjectId').val();
	var paymentSubjectId=$('#paymentSubjectId').val();
	var clearSubjectId=$('#clearSubjectId').val();
	var expenseSubjectId=$('#expenseSubjectId').val();	
	var deptId=deptNameValue.getSelectedValue();
	var useDate = $('#useDate').datebox('getValue');
	var buyDate = $('#buyDate').datebox('getValue');
	var shareDate = $('#shareDate').datebox('getValue');
	$('#form').form('enableValidation');
	if ($('#form').form('validate')) {
		$.ajax({
		    url: "${ctx}/asset/save",
		    type: "POST",
		    data: {fid:fid,updateTime:updateTime,assetCode:assetCode,assetName:assetName,quentity:quentity,initialValue:initialValue,discountYear:discountYear,
				residualRate:residualRate,residualValue:residualValue,supplier:supplier,grade:grade,manufactor:manufactor,model:model,recordStatus:recordStatus,
				assetTypeId:assetTypeId,assetSubjectId:assetSubjectId,depreciationSubjectId:depreciationSubjectId,paymentSubjectId:paymentSubjectId,
				clearSubjectId:clearSubjectId,expenseSubjectId:expenseSubjectId,deptId:deptId,useDate:useDate,buyDate:buyDate,shareDate:shareDate}
		}).done(function(data) {
			dataDispose(data);
			if(data.result==0){
				$.fool.alert({msg:rmsg,fn:function(){
					$('#addBox').window('close');
					 $('#cardList').trigger('reloadGrid');
				},time:2000});
			}else if(data.result==1){
				$.fool.alert({msg:data.msg});
			}else{
				$.fool.alert({msg:'系统繁忙，请稍后再试。'});
			}
		});
	}
});

/* focusShow('assetSubjectName');
focusShow('depreciationSubjectName');
focusShow('paymentSubjectName');
focusShow('clearSubjectName');
focusShow('expenseSubjectName'); */
function assetSubjectWin(){
	  chooserWindow=$.fool.window({width:780,height:480,'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubjectDBC&singleSelect=true&flag=1',
  		onLoad:function(){
  		  $(':input').blur();
  		  ($('#assetSubjectName').next())[0].comboObj.closeAll(); 		  
  		}});//解决IE点击弹窗下拉框不消失问题
}
function depreciationSubjectWin(){
	  chooserWindow=$.fool.window({width:780,height:480,'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubjectD&onDblClick=selectSubjectDBCD&singleSelect=true&flag=1',
	  	onLoad:function(){ $(':input').blur();($('#depreciationSubjectName').next())[0].comboObj.closeAll();}});//解决IE点击弹窗下拉框不消失问题
}
function paymentSubjectWin(){
	  chooserWindow=$.fool.window({width:780,height:480,'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubjectP&onDblClick=selectSubjectDBCP&singleSelect=true&flag=1',
	  	//onLoad:function(){$('#paymentSubjectName').combobox('hidePanel');}});//解决IE点击弹窗下拉框不消失问题
	  	onLoad:function(){ $(':input').blur();($('#paymentSubjectName').next())[0].comboObj.closeAll();}});//解决IE点击弹窗下拉框不消失问题
	  		
}
function clearSubjectWin(){
	  chooserWindow=$.fool.window({width:780,height:480,'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubjectC&onDblClick=selectSubjectDBCC&singleSelect=true&flag=1',
	  	 onLoad:function(){ $(':input').blur();($('#clearSubjectName').next())[0].comboObj.closeAll();}});//解决IE点击弹窗下拉框不消失问题
}
function expenseSubjectWin(){
	  chooserWindow=$.fool.window({width:780,height:480,'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubjectE&onDblClick=selectSubjectDBCE&singleSelect=true&flag=1',
	  	 onLoad:function(){ $(':input').blur();($('#expenseSubjectName').next())[0].comboObj.closeAll();}});//解决IE点击弹窗下拉框不消失问题
}
function selectSubject(rowData){
	$("#assetSubjectName").focus();
	if(rowData[0].name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	  /* $("#assetSubjectName").combobox('textbox').next().find('a').click();
	  $("#assetSubjectName").combobox('setValue',rowData[0].name);
	  $("#assetSubjectId").val(rowData[0].fid); */	  
	  $("#assetSubjectId").val(rowData[0].fid);
	  ($("#assetSubjectName").next())[0].comboObj.setComboText(rowData[0].name);	
	  chooserWindow.window('close');
}
function selectSubjectDBC(rowData){
	$("#assetSubjectName").focus();
	if(rowData.name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	 /*  $("#assetSubjectName").combobox('textbox').next().find('a').click();
	  $("#assetSubjectName").combobox('setValue',rowData.name);
	  $("#assetSubjectId").val(rowData.fid); */
	  $("#assetSubjectId").val(rowData.fid);
	  ($("#assetSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window('close');
	  
}
function selectSubjectD(rowData){
	$("#depreciationSubjectName").focus();
	if(rowData[0].name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	  /* $("#depreciationSubjectName").combobox('textbox').next().find('a').click();
	  $("#depreciationSubjectName").combobox('setValue',rowData[0].name);
	  $("#depreciationSubjectId").val(rowData[0].fid); */
	  $("#depreciationSubjectId").val(rowData[0].fid);
	  ($("#depreciationSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window('close');
}
function selectSubjectDBCD(rowData){
	$("#depreciationSubjectName").focus();
	if(rowData.name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	/* $("#depreciationSubjectName").combobox('textbox').next().find('a').click();
	  $("#depreciationSubjectName").combobox('setValue',rowData.name);
	  $("#depreciationSubjectId").val(rowData.fid); */
	  $("#depreciationSubjectId").val(rowData.fid);
	  ($("#depreciationSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window('close');
}
function selectSubjectP(rowData){
	$("#paymentSubjectName").focus();
	if(rowData[0].name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	/* $("#paymentSubjectName").combobox('textbox').next().find('a').click();
	  $("#paymentSubjectName").combobox('setValue',rowData[0].name);
	  $("#paymentSubjectId").val(rowData[0].fid); */
	  $("#paymentSubjectId").val(rowData[0].fid);
	  ($("#paymentSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window('close');
}
function selectSubjectDBCP(rowData){
	$("#paymentSubjectName").focus();
	if(rowData.name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	/* $("#paymentSubjectName").combobox('textbox').next().find('a').click();
	  $("#paymentSubjectName").combobox('setValue',rowData.name);
	  $("#paymentSubjectId").val(rowData.fid); */
	  $("#paymentSubjectId").val(rowData.fid);
	  ($("#paymentSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window('close');
}
function selectSubjectC(rowData){
	$("#clearSubjectName").focus();
	if(rowData[0].name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	/* $("#clearSubjectName").combobox('textbox').next().find('a').click();
	  $("#clearSubjectName").combobox('setValue',rowData[0].name);
	  $("#clearSubjectId").val(rowData[0].fid); */
	  $("#clearSubjectId").val(rowData[0].fid);
	  ($("#clearSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window('close');
}
function selectSubjectDBCC(rowData){
	$("#clearSubjectName").focus();
	if(rowData.name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	/* $("#clearSubjectName").combobox('textbox').next().find('a').click();
	  $("#clearSubjectName").combobox('setValue',rowData.name);
	  $("#clearSubjectId").val(rowData.fid); */
	  $("#clearSubjectId").val(rowData.fid);
	  ($("#clearSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window('close');
}
function selectSubjectE(rowData){
	$("#expenseSubjectName").focus();
	if(rowData[0].name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	/*$("#expenseSubjectName").combobox('textbox').next().find('a').click();
	  $("#expenseSubjectName").combobox('setValue',rowData[0].name);
	  $("#expenseSubjectId").val(rowData[0].fid);*/
	  $("#expenseSubjectId").val(rowData[0].fid);
	  ($("#expenseSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window('close');
}
function selectSubjectDBCE(rowData){
	$("#expenseSubjectName").focus();
	if(rowData.name=="科目"){
		$.fool.alert({msg:'请选择科目子节点！'});
		return false;
	}
	/*$("#expenseSubjectName").combobox('textbox').next().find('a').click();
	  $("#expenseSubjectName").combobox('setValue',rowData.name);
	  $("#expenseSubjectId").val(rowData.fid);*/
	  $("#expenseSubjectId").val(rowData.fid);
	  ($("#expenseSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window('close');
}
if('${asset.recordStatus}'==null || '${asset.recordStatus}'=='' || '${asset.recordStatus}'== 0){
	enterController('form');
}else{
	enterController('form',true);
}
</script>
</body>
</html>