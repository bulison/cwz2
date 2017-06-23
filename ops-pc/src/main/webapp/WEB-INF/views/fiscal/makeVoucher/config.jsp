<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
<style>
.myform p{
	width:auto;
}	
#bill .in-box {
	text-align:left;
}
#actionbox a{
	display:inline-block;
}
#btn-box{
	background:#F0F0F0;
	position:absolute;
	bottom:0;
	text-align:center;
}
#btn-box .mybtn-blue{
	width:56px;
}
#warehousebillForm{
	overflow:auto;
}
#title h1{
	font:800 16px 宋体  !important;
	color:#50b3e7;
	margin-left:60px;
	height:18px;
}
#title{
	position: absolute;
	top:0px;
	background:#F0F0F0;
	width:100%; /*写给不兼容的浏览器*/
	width:calc(100% - 40px);
	width:-moz-calc(100% - 40px);
	width:-webkit-calc(100% - 40px);
	padding:0 20px;
	z-index:8999;
}
</style>
<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn"><div id="triangle"></div></div><h1>模板设置</h1>
		</div>
	</div>
<div class="configBox">
	<div id="actionbox" style="width:30%;display:inline-block;">
		<div style="margin:5px 10px"><a href="javascript:;" id="cadd" class="btn-ora-add" >新增</a>
		<a href="javascript:;" id="cdel" class="btn-ora-recovery" >删除</a></div>
		<table id="modelTable"></table>
		<div id="modelPager"></div>
	</div>
	<div id="detailBox" style="width:70%; display:inline-block;float:right;">
			<form action="" class="bill-form myform" id="warehousebillForm">
				<div id="bill" class="shadow" style="margin-top:10px;">
					<div class="billTitle"><div id="square2"></div><h2 id="mdt">模板信息</h2></div>
					<div class="in-box" id="list2">
						<input name="fid" id="fid" type='hidden'/>
						<input name="updateTime" id="updateTime" type='hidden'/>
						<input name="voucherWordId" id="myvoucherWordId" type="hidden"/>
						
						<p><font>单据类型：</font><input _imp="true" name="billType" id="mybillType" class="textBox" type="text"/></p>
						<p><font>模板名称：</font><input _imp="true" name="templateName" id="templateName" class="textBox" type="text"/></p>
						<p><font>模板编号：</font><input _imp="true" name="templateCode" id="templateCode" class="textBox" type="text"/></p>
						<p><font>凭证字：</font><input _imp="true" id="myvoucherWordName" name="voucherWordId" class="textBox" type="text"/></p>
						<p><font>备注：</font><input id="remark" name="remark" style="width:519px;" class="textBox" type="text"/></p>
					</div>
				</div>
				<div id="dataBox" class="shadow">
					<div class="billTitle"><div id="square2"></div><h2>添加明细</h2></div>
					<div class="in-box" id="list1">
						<table id="modelList"></table>
					</div>
				</div>
		</form>
		<div id="btn-box" style="padding:5px 0;border-top:1px #ccc solid;">
			<input href="javascript:;" id="modelSave" class="mybtn-blue mybtn-s" value="保存"/>
			<input href="javascript:;" id="modelCancel" class="mybtn-blue mybtn-s" value="撤销"/>
		</div>
	</div>	
</div>
<script>
$(function(){
	$('html').css('overflow','hidden');
	$('#warehousebillForm').height($('#addBox').height()-20);
	$('#btn-box').width($('#warehousebillForm').width());
	$('#list2').find("input[_imp=true]").prev().prepend('<em>*</em>');
	$('#templateName').validatebox({required:true,novalidate:true});
	$('#templateCode').validatebox({required:true,novalidate:true});
});
$('.backBtn').click(function(){
	$('#addBox').window('close');
});
$('#cadd').click(function(){
	if($('#templateName').attr('disabled')=="disabled"){
		disableFrom(false);
		loadData();
	}else{
		$.fool.confirm({title:"提示",msg:"你还有未编辑完成的模板，确认加载新的模板？",fn:function(data){
			if(data){
				disableFrom(false);
				loadData();
			}else{
				return false;
			}
		}});
	}
});
$('#cdel').click(function(){
	var selected=$('#modelTable').find('tr[aria-selected="true"]');
	if(selected.length>0){
		var row = $('#modelTable').jqGrid("getRowData",$(selected[0]).attr("id"));
		$.post('${ctx}/billsubject/delete',{fid:row.fid},function(data){
			if(data.returnCode == 0){
				$.fool.alert({time:1000,msg:"删除成功！",fn:function(){
					$('#modelTable').trigger("reloadGrid"); 
					/* $('#modelTable').datagrid('reload'); */
					if(row.fid == $('#fid').val()){
						$('#mdt').html('模板信息');
						disableFrom(true);
						loadData();
					}
				}});
			}else if(data.returnCode == 1){
				$.fool.alert({msg:data.message,fn:function(){
				}});
			}else{
				$.fool.alert({msg:"服务器繁忙，请稍后再试",fn:function(){
				}});
			}
		});
	}else{
		$.fool.alert({msg:"请选择模板"});
	}
});
/* $('#mybillType').combobox({
	required:true,
	novalidate:true,
	editable:false,
	width:180,
	height:30,
	data:[{value:11,text:"采购入库单"},
	      {value:12,text:"采购退货单"},
	      {value:15,text:"采购发票"},
	      {value:20,text:"盘点单"},
	      {value:21,text:"调仓单"},
	      {value:22,text:"报损单"},
	      {value:30,text:"生产领料单"},
	      {value:31,text:"成品入库单"},
	      {value:32,text:"生产退料单"},
	      {value:33,text:"成品退库单"},
	      {value:41,text:"销售出货单"},
	      {value:42,text:"销售退货单"},
	      {value:44,text:"销售发票"},
		  {value:51,text:"收款单"},
		  {value:52,text:"付款单"},
		  {value:53,text:"费用单"},
		  {value:55,text:"采购返利单"},
		  {value:56,text:"销售返利单"}
	]
}); */
$('#mybillType').fool("dhxCombo",{
	required:true,
	novalidate:true,
	editable:false,
	width:180,
	height:30,
	data:[{value:11,text:"采购入库单"},
	      {value:12,text:"采购退货单"},
	      {value:15,text:"采购发票"},
	      {value:20,text:"盘点单"},
	      {value:21,text:"调仓单"},
	      {value:22,text:"报损单"},
	      {value:30,text:"生产领料单"},
	      {value:31,text:"成品入库单"},
	      {value:32,text:"生产退料单"},
	      {value:33,text:"成品退库单"},
	      {value:41,text:"销售出货单"},
	      {value:42,text:"销售退货单"},
	      {value:44,text:"销售发票"},
		  {value:51,text:"收款单"},
		  {value:52,text:"付款单"},
		  {value:53,text:"费用单"},
		  {value:55,text:"采购返利单"},
		  {value:56,text:"销售返利单"}
	],
});

/* $('#myvoucherWordName').fool('voucherWordTree',{
	width:180,
	height:30,
	onBeforeSelect:function(node){
		if(node.text != '请选择'){
			$('#myvoucherWordId').val(node.id);
		}
	},
	onSelect:function(node){
		$('#myvoucherWordId').val(node.id);
	}
}); */

$('#myvoucherWordName').fool("dhxCombo",{
	width:180,
	height:30,
	editable:false,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	focusShow:true,
	data:getComboData(getRootPath()+'/basedata/voucherWord',"tree"),
});
$('#myvoucherWordName').next()[0].comboObj.disable();

$("#modelTable").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/billsubject/query',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#modelTable")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,
	height:"100%",
	pager:"#modelPager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[
	          {name:"fid",label:'fid',hidden:true},
              {name:"billType",label:"单据类型",align:'center',width:"50px",formatter:function(cellvalue, options, rowObject){
            	  return cellvalue!=''?cbillType[cellvalue]:'';
              }},
              {name:"templateName",label:"模板名称",align:'center',width:"50px"},
              {name:"templateCode",label:"模板编号",align:'center',width:"100px"}
              ],
    ondblClickRow:function(rowid,iRow,iCol,e){
    	var row=$("#modelTable").jqGrid("getRowData",rowid);
    	if($('#fid').val()==row.fid){
			$('#mdt').html('修改模板信息');
			disableFrom(false);
		}else if($('#templateName').attr('disabled')=="disabled"){
			$('#mdt').html('修改模板信息');
			disableFrom(false);
			loadData(row.fid);
		}else{
			$.fool.confirm({title:"提示",msg:"你还有未编辑完成的模板，确认加载新的模板？",fn:function(data){
				if(data){
					$('#mdt').html('修改模板信息');
					disableFrom(false);
					loadData(row.fid);
				}else{
					return false;
				}
			}});
		}
    },
    onSelectRow:function(rowid,status){
    	var row=$("#modelTable").jqGrid("getRowData",rowid);
		if($('#templateName').attr('disabled')=="disabled"){
			$('#mdt').html('查看模板信息');
			loadData(row.fid);
		}
	}
}).navGrid('#modelPager',{add:false,del:false,edit:false,search:false,view:false});

function loadData(fid){
	if(fid){
		if($('#fid').val()==''){//解决新增模板明细编辑状态下转换修改其他单据时出错
			var length = $('#modelList').jqGrid('getRowData').length;
			if(length>0){
				for(i=0; i<length; i++){
					$('#modelList').jqGrid('delRowData',0);
				}
			}
		}
		$.post('${ctx}/billsubject/getById',{fid:fid},function(data){
			if(data){
				var myd = $.parseJSON(data.details);
				$('#fid').val(fid);
				$('#voucherWordId').val(data.voucherWordId);
				$('#updateTime').val(data.updateTime);
				($('#mybillType').next())[0].comboObj.setComboValue(data.billType);
				($('#myvoucherWordName').next())[0].comboObj.setComboValue(data.voucherWordId);
				$('#templateName').val(data.templateName);
				$('#templateCode').val(data.templateCode);
				$('#remark').val(data.remark);
				$('#modelList').datagrid('loadData',myd);
			}else{
				return false;
			}
		});
	}else{
		$('#mdt').html('新增模板信息');
		$('#warehousebillForm').form('clear');
		$('#modelList').datagrid('loadData',[]);
	}
}

function disableFrom(bool){
	if(bool){
		($('#mybillType').next())[0].comboObj.disable();
		$('#myvoucherWordName').next()[0].comboObj.disable();
		($('#myvoucherWordName').next())[0].comboObj.setComboValue();
		$('#templateName').attr('disabled','disabled');
		$('#templateCode').attr('disabled','disabled');
		$('#remark').attr('disabled','disabled');
		$('#modelList').datagrid('hideColumn','fid');
		$('#modelSave').hide();
		$('#modelCancel').hide();
	}else{
		$('#warehousebillForm').form('disableValidation');
		($('#mybillType').next())[0].comboObj.enable();
		($('#myvoucherWordName').next())[0].comboObj.enable();
		$('#templateName').removeAttr('disabled');
		$('#templateCode').removeAttr('disabled');
		$('#remark').removeAttr('disabled');
		$('#modelList').datagrid('showColumn','fid');
		$('#modelSave').show();
		$('#modelCancel').show();
		$('#modelSave').removeAttr('disabled');
	}
}

/* $("#modelList").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/voucher/query?startDay=${startVoucherDate}&endDay=${endVoucherDate}',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#modelList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,
	height:"100%",
	pager:"#modelPager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[ 
                
              ],
}).navGrid('#modelPager',{add:false,del:false,edit:false,search:false,view:false}); */

$('#modelList').datagrid({
	fitColumns:true,
	singleSelect:true,
	showFooter:true,
	onClickRow:function(index,row){
		var myobj = $(this).parent().find('tr[datagrid-row-index='+index+'] td[field=fid] a.btn-del');
		if(!row.editing&&$('#mdt').html()=="修改模板信息"){
			mEdit(myobj);
		}
	},
	columns:[[
			  {field:"fid",title:"操作",width:80,fixed:true,hidden:true,formatter:function(value,row,index){
				    if(value == "new"){
				    	return '<a href="javascript:;" onclick="addRow()" class="btn-add" title="新增"></a>';
				    }else{
				    	var s='<a href="javascript:;" onclick="mSave(this)" class="btn-save" title="保存"></a>';
				    	//var e='<a href="javascript:;" onclick="mEdit(this)" class="btn-edit" title="编辑"></a>';
				    	var c='<a href="javascript:;" onclick="mCancel(this)" class="btn-cancel" title="撤销"></a>';
				    	var d='<a href="javascript:;" onclick="mDel(this)" class="btn-del" title="删除"></a>';
						if(row.editing){
				    		return s+c;
				    	}else{
				    		return d;
				    	}
				    }
			  }},
	          {field:"billSubjectId",title:"billSubjectId",editor:{type:"text"},hidden:true},
	          {field:"subjectId",title:"subjectId",editor:{type:"text"},hidden:true},
	          {field:"subjectCode",title:"subjectCode",editor:{type:"text"},hidden:true},
	          {field:"newRow",title:"newRow",hidden:true,editor:{type:"text"}},
	          
	          {field:"subjectName",title:"科目",editor:{type:"text" /* ,options:{required:true,panelWidth:200,novalidate:true,validType:'subjValid',valueField:"id",textField:"text",url:"${ctx}/fiscalSubject/getSubject?leafFlag=1",mode:"remote"} */ },width:10},
	          {field:"direction",title:"方向",editor:{type:"text"},width:10,formatter:function(value,row,index){
					return value=='1'?'借方':value=='-1'?'贷方':'';
				}},
	          {field:"subjectSource",title:"科目来源",editor:{type:"text"},width:10,formatter:function(value,row,index){
					return value=='1'?'模板':value=='2'?'银行账号关联科目':'';
				}},
	          {field:"amountSource",title:"金额来源",editor:{type:"text"},width:10,formatter:function(value,row,index){
					return value=='1'?'单据金额（不含税金额）':value=='2'?'成本金额':value=='3'?"税额":value=="4"?"含税金额":value=="5"?"利润":value=="6"?"提成金额":"";
				}},
			  {field:"hedge",title:"红蓝对冲",editor:{type:"text"},width:10,formatter:function(value){
				  return value == -1?"红字":value == 1?"蓝字":'';
			  }},
	          {field:"resume",title:"摘要",editor:{type:"text"},width:10},
	      ]],
	onBeforeEdit:function(index,row){
		row.editing = true;
		updateRow(index);
	},
	onAfterEdit:function(index,row){
		row.editing = false;
		updateRow(index);
	},
	onCancelEdit:function(index,row){
		row.editing = false;
		updateRow(index);
	},
	onBeforeLoad:function(param){
		$('#modelList').datagrid('reloadFooter',[{fid:'new'}]);
	}
}); 
//按键控制
keyHandler($("#modelList"));
disableFrom(true);
function updateRow(index){
	$('#modelList').datagrid('updateRow',{
		index:index,
		row:{}
	});
}

function getTableEditor(index,field){
	var myobj = $("#modelList");
	return getTableEditorHelp(myobj,index,field);
}
function getTableEditorHelp(tbId,index,field){
	var $t =$.fool._get$(tbId);
	return $t.fool('getEditor$',{'index':index,'field':field});
}

function addRow(){
	$('#modelList').datagrid("appendRow",{
		newRow:true
	});
	var index = $('#modelList').datagrid("getRows").length-1;
	var myobj = $('#modelList').datagrid('getPanel').find('.datagrid-body [datagrid-row-index='+index+'] [field=fid] a.btn-del');
	mEdit(myobj);
}

/* function getIndex(target){
	return $(target).closest('tr[datagrid-row-index]').attr('datagrid-row-index');
} */
//列表操作函数
function mEdit(target){
	var index = $(target).fool('getRowIndex');
	var rowData=$('#modelList').datagrid("getData").rows;
	var myresume = $('#modelList').datagrid('getPanel').find('.datagrid-body [datagrid-row-index='+index+'] [field=resume]').children().text();
	var subjectName = $('#modelList').datagrid('getPanel').find('.datagrid-body [datagrid-row-index='+index+'] [field=subjectName]').children().text();
	$('#modelList').datagrid('beginEdit',index);
	var sIdEditor = getTableEditor(index,"subjectId");
	var sEditor = getTableEditor(index,"subjectName");
	var dEditor = getTableEditor(index,"direction");
	var ssEditor = getTableEditor(index,"subjectSource");
	var asEditor=getTableEditor(index,"amountSource");
	var hEditor=getTableEditor(index,"hedge");
	var rEditor=getTableEditor(index,"resume");
	
	sEditor.attr("id",index+"-subjectName");
	dEditor.attr("id",index+"-direction");
	ssEditor.attr("id",index+"-subjectSource");
	asEditor.attr("id",index+"-amountSource");
	hEditor.attr("id",index+"-hedge");
	rEditor.attr("id",index+"-resume");
	
	var edindex = index;
	sIdEditor.val(rowData[index].subjectId);
	sEditor.fool('dhxComboGrid',{
		data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  	filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
		focusShow:true,
		required:true,
		novalidate:true,
		/* value:rowData[index].subjectId, */
		validType:'subjValid',
		searchKey:"q", 
	  	setTemplate:{
	  		input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
	  	},
		onOpen:function(){
			edindex = $(this).fool('getRowIndex');
		},
		onSelectionChange:function(){
			var ind = $($(this)[0].cont).closest(".datagrid-row").attr("datagrid-row-index");
			var node=($("#"+index+"-subjectName").next())[0].comboObj.getSelectedText();
			var sEditor = getTableEditor(ind,"subjectId");
			var cEditor = getTableEditor(ind,"subjectCode");
			sEditor.val(node.id);
			cEditor.val(node.code);
		}
	});
	sEditor.next().find(".dhxcombo_select_button").click(function(){
		var ind = $(this).fool('getRowIndex');
		mysubjectName(ind);
	});
	dEditor.fool("dhxCombo",{
		required:true,
		novalidate:true,
		editable:false,
		focusShow:true,
		value:rowData[index].direction,
		setTemplate:{
			  input:"#name#",
			  option:"#name#"
		},
		data:[{
			value: '1',
		    text: '借方'
		},{
			value: '-1',
		    text: '贷方'
		}]
	})
	ssEditor.fool("dhxCombo",{
		required:true,
		novalidate:true,
		editable:false,
		focusShow:true,
		value:rowData[index].subjectSource,
		setTemplate:{
			  input:"#name#",
			  option:"#name#"
		},
		data:[{
			value: '1',
		    text: '模板'
		},{
			value: '2',
		    text: '银行账号关联科目'
		}],
		onSelect:function(record){
			var subjn = $(this).closest('[field=subjectSource]').siblings('[field=subjectName]').find('.datagrid-editable-input');
		    var subjc = $(this).closest('[field=subjectSource]').siblings('[field=subjectCode]').find('.datagrid-editable-input');
		    var subji = $(this).closest('[field=subjectSource]').siblings('[field=subjectId]').find('.datagrid-editable-input');
		    if(record.value == '2'){
		    	(subjn.next())[0].comboObj.setComboValue('');
		    	(subjn.next())[0].comboObj.setComboText('');
		    	(subjn.next())[0].comboObj.disable();
		    	subjc.val('');subji.val('');
		    }else{
		    	(subjn.next())[0].comboObj.enable();
		    }
		}
	})
	var source = (getTableEditor(index,"subjectSource").next())[0].comboObj.getSelectedValue();
	
	asEditor.fool("dhxCombo",{
		required:true,
		novalidate:true,
		editable:false,
		focusShow:true,
		value:rowData[index].amountSource,
		setTemplate:{
			  input:"#name#",
			  option:"#name#"
		},
		data:[{
			value: '1',
		    text: '单据金额（不含税金额）'
		},{
			value: '2',
			text: '成本金额'
		},{
			value: '3',
		    text: '税额'
		},{
		    value: '4',
		    text: '含税金额'
		},{
		    value: '5',
		    text: '利润'
		},{
		    value:'6',
			text:'提成金额'
		}]
	});
	hEditor.fool("dhxCombo",{
		editable:false,
		focusShow:true,
		required:true,
		novalidate:true,
		value:rowData[index].hedge,
		setTemplate:{
			  input:"#name#",
			  option:"#name#"
		},
		data:[{
			value:-1,
		    text:"红字"
		},{
		    value:1,
		    text:"蓝字"
		}]
	});
	rEditor.fool("dhxCombo",{
		focusShow:true,
		validType:'isBank',
		text:rowData[index].resume,
		setTemplate:{
			  input:"#name#",
			  option:"#name#"
		},
		editable:true,
		data:getComboData("${ctx}/basedata/resume","tree"),
		onLoadSuccess:function(){
			(rEditor.next())[0].comboObj.setComboText(myresume);
		}
	})
	/* sEditor.combobox('textbox').focus(function(){
		sEditor.combobox('showPanel');
	}); */
	var newRow = getTableEditor(index,"newRow").val()
	newRow || newRow == "true"?//默认蓝字
			(getTableEditor(index,"hedge").next())[0].comboObj.setComboValue(1):null;
	var q = getTableEditor(index,"subjectCode").val();
	var subnId = getTableEditor(index,"subjectId").val();
	(sEditor.next())[0].comboObj.clearAll(false);
	(sEditor.next())[0].comboObj.addOption(getComboData('${ctx}/fiscalSubject/getSubject?leafFlag=1&q='+q));
	(sEditor.next())[0].comboObj.setComboText(subjectName);
	(sEditor.next())[0].comboObj.clearAll(false);
	(sEditor.next())[0].comboObj.addOption(getComboData('${ctx}/fiscalSubject/getSubject?leafFlag=1&q='));
	if(source == 2){
		(sEditor.next())[0].comboObj.setComboValue('');
		(sEditor.next())[0].comboObj.disable();
		getTableEditor(index,"subjectId").val('');
		getTableEditor(index,"subjectCode").val('');
	}
	var rEditor = getTableEditor(index,"resume");
	// var resume = _resume==''?myresume:_resume;
}
//科目弹出框
var chooserWindow = '';
var myindex;
function mysubjectName(ind){
	  myindex = ind;
	  chooserWindow=$.fool.window({width:780,height:480,'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubject&singleSelect=true&flag=1',
			  onLoad:function(){(getTableEditor(ind,"subjectName").next())[0].comboObj.closeAll();}});
}
function selectSubject(rowData){
	  if("undefined" != typeof rowData[0]){
		  rowData = rowData[0]
	  }
	  if(rowData.flag!=1){
		  $.fool.alert({time:1000,msg:"请选择子节点"});
		  return false;
	  }
	  (getTableEditor(myindex,"subjectName").next())[0].comboObj.setComboValue(rowData.fid);
	  getTableEditor(myindex,"subjectId").val(rowData.fid);
	  getTableEditor(myindex,"subjectCode").val(rowData.code);
	  chooserWindow.window('close');
}
function mDel(target){
	var index = $(target).fool('getRowIndex');
	$('#modelList').datagrid('deleteRow',index);
}
function mSave(target){
	var index = $(target).fool('getRowIndex');
	$('#modelList').datagrid('getPanel').find('.datagrid-body [datagrid-row-index='+index+']').form('enableValidation');
	if($('#modelList').datagrid('validateRow',index)){
		var sIdEditor = getTableEditor(index,"subjectId");
		var sEditor = getTableEditor(index,"subjectName");
		var rEditor = getTableEditor(index,"resume");
		var dEditor = getTableEditor(index,"direction");
		var ssEditor = getTableEditor(index,"subjectSource");
		var asEditor = getTableEditor(index,"amountSource");
		var hEditor = getTableEditor(index,"hedge");
		
		
		var resume=(rEditor.next())[0].comboObj.getComboText();
		var subjectId=sIdEditor.val();
		var subjectName=(sEditor.next())[0].comboObj.getComboText();
		var direction=(dEditor.next())[0].comboObj.getSelectedValue();
		var subjectSource=(ssEditor.next())[0].comboObj.getSelectedValue();
		var amountSource=(asEditor.next())[0].comboObj.getSelectedValue();
		var hedge=(hEditor.next())[0].comboObj.getSelectedValue();
		getTableEditor(index,"newRow").val(false);
		$('#modelList').datagrid('endEdit',index);
		$('#modelList').datagrid('updateRow',{
			index:index,
			row:{
				"resume":resume,
				"subjectName":subjectName,
				"subjectId":subjectId,
				"direction":direction,
				"subjectSource":subjectSource,
				"amountSource":amountSource,
				"hedge":hedge
			}
		});
	}else{
		return false;
	}
}
function mCancel(target){
	var index = $(target).fool('getRowIndex');
	if(getTableEditor(index,'newRow').val()==true || getTableEditor(index,'newRow').val()=="true"){
		$('#modelList').datagrid('deleteRow',index);
	}else{
		$('#modelList').datagrid('cancelEdit',index);
	}
}
$('#modelCancel').click(function(){
	$.fool.confirm({title:"提示",msg:"确认撤销？",fn:function(r){
		if(r){
			var id = $('#fid').val();
			$('#mdt').html('查看模板信息');
			loadData(id);
			disableFrom(true);
		}
	}});
});
$('#modelSave').click(function(){
	var details = $("#modelList").datagrid('getRows');
	$('#modelSave').attr('disabled','disabled');
	$('#warehousebillForm').form('enableValidation');
	if(!$('#warehousebillForm').form('validate')){
		$('#modelSave').removeAttr('disabled');
		return false;
	}
	if(details.length<1){
		$.fool.alert({msg:'你还没有添加任何明细',fn:function(){
			$('#modelSave').removeAttr('disabled');
		}});
		return false;
	}
	var _dataPanel = $('#modelList').datagrid('getPanel');
	var _editing = _dataPanel.find(".btn-save");
	if(_editing.length>0){
		$.fool.alert({msg:'你还有没编辑完成的明细,请先确认！',fn:function(){
			$('#modelSave').removeAttr('disabled');
		}});
		return false;
	}
	var mydata = $('#warehousebillForm').serializeJson();
	mydata = $.extend(mydata,{'details':JSON.stringify(details)});
	$.post('${ctx}/billsubject/save',mydata,function(data){
		if(data.returnCode == 0){
			$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
				$('#modelTable').trigger("reloadGrid"); 
				/* $('#modelTable').datagrid('reload'); */
				$('#mdt').html('查看模板信息');
				disableFrom(true);
			}});
		}else if(data.returnCode == 1){
			$.fool.alert({msg:data.message,fn:function(){
				$('#modelSave').removeAttr('disabled');
			}});
		}else{
			$.fool.alert({msg:"服务器繁忙，请稍后再试",fn:function(){
				$('#modelSave').removeAttr('disabled');
			}});
		}
	});
});
</script>
</body>
</html>
