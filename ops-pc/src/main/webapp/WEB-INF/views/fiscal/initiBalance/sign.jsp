<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
</head>
<body>
  <div>
    <table id="signList"></table>
  </div>
  <br/>
  <div id="btns">
    <input id="appendRows" class="btn-blue2 btn-xs" type="button" value="插入"/>&emsp;
    <input id="saveRows" class="btn-blue2 btn-xs" type="button" value="保存"/>
  </div>
  <br/>
  
<script type="text/javascript">
//弹出框变量
var chooserWindow="";
var editor="";

//列表数据初始化
var datas="";
$.ajax({
	url:"${ctx}/initiBalance/queryAccounting?num="+Math.random(),
	async:false,
	data:{subjectId:'${obj.fid}'},
	success:function(data){
		if(data!=""){
			datas=data;
		}else{
			datas=[{'isNew':1}];
		}
	}
});

//列表列
var column=[];
initColumn();

//核算列表
$('#signList').datagrid({
	data:datas,
	fitColumns:true,
	scrollbarSize:0,
	singleSelect:true,
	columns:[column],
	height:490,
	onLoadSuccess:function(data){
		if(!datas[0].amount){
			$('#signList').datagrid('beginEdit',0);
			//初始化列表编辑控件
			editorInit(0);
		}
	},
	onBeforeEdit:function(index,row){
		row.editing = true;
		updateActions(index);
	},
	onAfterEdit:function(index,row){
		row.editing = false;
		updateActions(index);
	},
	onCancelEdit:function(index,row){
		row.editing = false;
		updateActions(index);
	}
});
//按键控制
keyHandler();

//初始化列表列
function initColumn(){
	  column.push({field:'isNew',hidden:true});
	  column.push({field:'editing',hidden:true});
	  if('${obj.supplierSign}'==1){
		  column.push({field:'supplierId',hidden:true});
		  column.push({field:'supplierName',title:'供应商',sortable:true,width:100,editor:{type:"text"}});
	  }
	  
	  if('${obj.customerSign}'==1){
		  column.push({field:'customerId',hidden:true});
		  column.push({field:'customerName',title:'客户',sortable:true,width:100,editor:{type:"text"}});
	  }
	  
	  if('${obj.departmentSign}'==1){
		  column.push({field:'departmentId',hidden:true});
		  column.push({field:'departmentName',title:'部门',sortable:true,width:100,editor:{type:"text"}});
	  }
	  
	  if('${obj.memberSign}'==1){
		  column.push({field:'memberId',hidden:true});
		  column.push({field:'memberName',title:'职员',sortable:true,width:100,editor:{type:"text"}});
	  }
	  
	  if('${obj.warehouseSign}'==1){
		  column.push({field:'warehouseId',hidden:true});
		  column.push({field:'warehouseName',title:'仓库',sortable:true,width:100,editor:{type:"text"}});
	  }
	  
	  if('${obj.projectSign}'==1){
		  column.push({field:'projectId',hidden:true});
		  column.push({field:'projectName',title:'项目',sortable:true,width:100,editor:{type:"text"}});
	  }
	  
	  if('${obj.goodsSign}'==1){
		  column.push({field:'goodsId',hidden:true});
		  column.push({field:'goodsName',title:'货品',sortable:true,width:100,editor:{type:"text"}});
	  }
	  
	  if('${obj.quantitySign}'==1){
		  column.push({field:'unitId',hidden:true,formatter:function(value,row,index){return "${obj.unitId}";}});
		  column.push({field:'unitName',title:'单位',sortable:true,width:100,formatter:function(value,row,index){
			  return "${obj.unitName}";
		  }});
		  column.push({field:'quantity',title:'数量',sortable:true,width:100,editor:{type:"numberbox",options:{validType:['isBank','quentity'],precision:2,required:true,missingMessage:"该输入项为必输项"}}});
	  }
	  
	  if('${obj.currencySign}'==1){
		  column.push({field:'currencyId',hidden:true,formatter:function(value,row,index){return '${obj.currencyId}';}});
		  column.push({field:'currencyName',title:'币别',sortable:true,width:100,formatter:function(value,row,index){
			  return '${obj.currencyName}';
		  }});
		  column.push({field:'currencyAmount',title:'外币金额',sortable:true,width:100,editor:{type:"textbox",options:{validType:['isBank','amount'],required:true,missingMessage:"该输入项为必输项",onChange:function(newValue, oldValue){
			  if(newValue&&oldValue!=newValue){
				  var exchangeRate=$(this).closest("[field='currencyAmount']").siblings("[field='exchangeRate']").find(".datagrid-editable-input").textbox('getValue');
				  if(exchangeRate){
					  var amount=(exchangeRate*newValue).toFixed(2);
					  $(this).closest("[field='currencyAmount']").siblings("[field='amount']").find(".datagrid-editable-input").textbox('setValue',amount);
				  }
			  }
		  }}}});
		  column.push({field:'exchangeRate',title:'汇率',sortable:true,width:100,formatter:function(value,row,index){
			  if(!value){
				  if(row.amount&&row.currencyAmount){
					  if(row.currencyAmount!=0){
						  row.exchangeRate=(row.amount)/(row.currencyAmount);
						  return ((row.amount)/(row.currencyAmount)).toFixed(4);
					  }else{
						  return 0;
					  }
				  }else{
					  return "";
				  }
			  }else{
				  return parseFloat(value).toFixed(4);
			  }
		  },editor:{type:"textbox",options:{validType:['isBank','exchangeRate'],required:true,missingMessage:"该输入项为必输项",onChange:function(newValue, oldValue){
			 if(newValue&&oldValue!=newValue){
				  var currencyAmount=$(this).closest("[field='exchangeRate']").siblings("[field='currencyAmount']").find(".datagrid-editable-input").textbox('getValue');
				  if(currencyAmount){
					  var amount=(currencyAmount*newValue).toFixed(2);
					  $(this).closest("[field='exchangeRate']").siblings("[field='amount']").find(".datagrid-editable-input").textbox('setValue',amount);
				  }
			  }
		  }}}});
	  }
	   
	  column.push({field:'amount',title:'期初余额（原币）',sortable:true,width:100,editor:{type:"textbox",options:{validType:['isBank','amount'],required:true,missingMessage:"该输入项为必输项",onChange:function(newValue, oldValue){
		  if('${obj.currencySign}'==1){
			  if(newValue&&oldValue!=newValue){
				  var exchangeRate=$(this).closest("[field='amount']").siblings("[field='exchangeRate']").find(".datagrid-editable-input").textbox('getValue');
				  if(exchangeRate){
					  var currencyAmount=(newValue/exchangeRate).toFixed(2);
					  $(this).closest("[field='amount']").siblings("[field='currencyAmount']").find(".datagrid-editable-input").textbox('setValue',currencyAmount);
				  }
			  }
		  }
	  }}}});
	  column.push({field:'action',title:'操作',width:100,formatter:function(value,row,index){
		  if (row.editing){
			  var s='<a title="保存" href="javascript:;" class="btn-save" onclick="saverow(this)"></a>';
			  var c='<a title="取消" href="javascript:;" class="btn-cancel" onclick="cancelrow(this)"></a>';
			  return s+" "+c;
		  }else{
			  var e='<a title="编辑" href="javascript:;" class="btn-edit" onclick="editrow(this)" ></a>';
			  var d='<a title="删除" href="javascript:;" class="btn-del" onclick="deleterow(this)" ></a>';
			  return e+" "+d;
		  }
	  }});
	  return column;
}

//列表编辑控件初始化
function editorInit(ind,row){
	if(!row){
		row={};
	}
	var $e={
			supplierName:$('#signList').datagrid("getEditor",{index:ind,field:'supplierName'}),
	        customerName:$('#signList').datagrid("getEditor",{index:ind,field:'customerName'}),
	        memberName:$('#signList').datagrid("getEditor",{index:ind,field:'memberName'}),
	        goodsName:$('#signList').datagrid("getEditor",{index:ind,field:'goodsName'}),
	        departmentName:$('#signList').datagrid("getEditor",{index:ind,field:'departmentName'}),
	        warehouseName:$('#signList').datagrid("getEditor",{index:ind,field:'warehouseName'}),
	        projectName:$('#signList').datagrid("getEditor",{index:ind,field:'projectName'}),
	};
	
	if($e.supplierName){
		$($e.supplierName.target).attr("id",ind+"-supplierName");
		$($e.supplierName.target).fool("dhxComboGrid",{
			width:"auto",
			height:22,
			required:true,
			novalidate:false,
			focusShow:true,
			value:row.supplierId,
			text:row.supplierName,
			validType:"gridValid['supplierId']",
			filterUrl:getRootPath()+'/supplier/vagueSearch?searchSize=8',
			data:getComboData(getRootPath()+'/supplier/vagueSearch?searchSize=8'),
			searchKey:"searchKey",
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
			},
			onSelectionChange:function(){
				var id=$(this)[0].cont.comboObj.getSelectedValue();
		    	$($e.supplierName.target).closest('[field="supplierName"]').siblings('[field="supplierId"]').children().text(id);
		    },
		});
		$(($($e.supplierName.target).next())[0].comboObj.getInput()).validatebox("validate");
	}
	if($e.customerName){
		$($e.customerName.target).attr("id",ind+"-customerName");
		$($e.customerName.target).fool("dhxComboGrid",{
			width:"auto",
			height:22,
			required:true,
			novalidate:false,
			focusShow:true,
			value:row.customerId,
			text:row.customerName,
			validType:"gridValid['customerId']",
			filterUrl:getRootPath()+'/customer/vagueSearch?searchSize=8',
			data:getComboData(getRootPath()+'/customer/vagueSearch?searchSize=8'),
			searchKey:"searchKey",
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
			},
			onSelectionChange:function(){
		    	$($e.customerName.target).closest('[field="customerName"]').siblings('[field="customerId"]').children().text($(this)[0].cont.comboObj.getSelectedValue());
		    },
		});
		$(($($e.customerName.target).next())[0].comboObj.getInput()).validatebox("validate");
	}
	if($e.memberName){
		$($e.memberName.target).attr("id",ind+"-memberName");
		$($e.memberName.target).fool("dhxComboGrid",{
			width:"auto",
			height:22,
			required:true,
			novalidate:false,
			focusShow:true,
			value:row.memberId,
			text:row.memberName,
			validType:"gridValid['memberId']",
			filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8',
			data:getComboData(getRootPath()+'/member/vagueSearch?searchSize=8'),
			searchKey:"searchKey",
			setTemplate:{
				input:"#username#",
				columns:[
							{option:'#userCode#',header:'编号',width:100},
							{option:'#jobNumber#',header:'工号',width:100},
							{option:'#username#',header:'名称',width:100},
							{option:'#phoneOne#',header:'电话',width:100},
							{option:'#deptName#',header:'部门',width:100},
							{option:'#position#',header:'职位',width:100},
						],
			},
			onSelectionChange:function(){
		    	$($e.memberName.target).closest('[field="memberName"]').siblings('[field="memberId"]').children().text($(this)[0].cont.comboObj.getSelectedValue());
		    },
		});
		$(($($e.memberName.target).next())[0].comboObj.getInput()).validatebox("validate");
	}
	if($e.goodsName){
		$($e.goodsName.target).attr("id",ind+"-goodsName");
		$($e.goodsName.target).fool("dhxComboGrid",{
			width:"auto",
			height:22,
			required:true,
			novalidate:false,
			focusShow:true,
			value:row.goodsId,
			text:row.goodsName,
			validType:"gridValid['goodsId']",
			filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=6',
			data:getComboData(getRootPath()+'/goods/vagueSearch?searchSize=6'),
			searchKey:"searchKey",
			setTemplate:{
				input:"#name#",
				columns:[
				         {option:'#name#',header:'名称',width:100},
				         {option:'#code#',header:'编码',width:100},
				         {option:'#barCode#',header:'条码',width:100},
						],
			},
			onSelectionChange:function(){
		    	$($e.goodsName.target).closest('[field="goodsName"]').siblings('[field="goodsId"]').children().text($(this)[0].cont.comboObj.getSelectedValue());
		    },
		});
		$(($($e.goodsName.target).next())[0].comboObj.getInput()).validatebox("validate");
	}
	if($e.departmentName){
		$($e.departmentName.target).attr("id",ind+"-departmentName");
		$($e.departmentName.target).fool("dhxCombo",{
			width:"auto",
			height:22,
			required:true,
			novalidate:false,
			editable:false,
			value:row.departmentId,
			data:getComboData(getRootPath()+"/orgController/getAllTree","tree"),
			setTemplate:{
				input:"#orgName#",
				option:"#orgName#"
			}
		})
	}
	if($e.warehouseName){
		$($e.warehouseName.target).attr("id",ind+"-warehouseName");
		$($e.warehouseName.target).fool("dhxCombo",{
			width:"auto",
			height:22,
			required:true,
			novalidate:false,
			editable:false,
			value:row.warehouseId,
			data:getComboData(getRootPath()+"/basedata/warehourseList","tree"),
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
		})
	}
	if($e.projectName){
		$($e.projectName.target).attr("id",ind+"-projectName");
		$($e.projectName.target).fool("dhxCombo",{
			width:"auto",
			height:22,
			required:true,
			novalidate:false,
			editable:false,
			value:row.projectId,
			data:getComboData(getRootPath()+"/basedata/findSubAuxiliaryAttrTree?code=012","tree"),
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
		})
	}
	var tds=($("#signList").datagrid('getPanel').panel('panel').find("tr[datagrid-row-index="+ind+"]").children("td:visible"));
	setTimeout(function(){
		if($(tds[0]).attr("field")=="supplierName"){
			var ed=$('#signList').datagrid("getEditor",{index:ind,field:'supplierName'});
			$(($(ed.target).next())[0].comboObj.getInput()).focus();
		}else if($(tds[0]).attr("field")=="customerName"){
			var ed=$('#signList').datagrid("getEditor",{index:ind,field:'customerName'});
			$(($(ed.target).next())[0].comboObj.getInput()).focus();
		}else if($(tds[0]).attr("field")=="memberName"){
			var ed=$('#signList').datagrid("getEditor",{index:ind,field:'memberName'});
			$(($(ed.target).next())[0].comboObj.getInput()).focus();
		}else if($(tds[0]).attr("field")=="goodsName"){
			var ed=$('#signList').datagrid("getEditor",{index:ind,field:'goodsName'});
			$(($(ed.target).next())[0].comboObj.getInput()).focus();
		}else if($(tds[0]).attr("field")=="departmentName"){
			var ed=$('#signList').datagrid("getEditor",{index:ind,field:'departmentName'});
			$(($(ed.target).next())[0].comboObj.getInput()).focus();
		}else if($(tds[0]).attr("field")=="warehouseName"){
			var ed=$('#signList').datagrid("getEditor",{index:ind,field:'warehouseName'});
			$(($(ed.target).next())[0].comboObj.getInput()).focus();
		}else if($(tds[0]).attr("field")=="projectName"){
			var ed=$('#signList').datagrid("getEditor",{index:ind,field:'projectName'});
			$(($(ed.target).next())[0].comboObj.getInput()).focus();
		}
	}, 1);
}

//插入按钮点击事件
$("#appendRows").click(function(){
	$('#signList').datagrid('insertRow',{index:0,row:{
		isNew:1
	}});
	$('#signList').datagrid('beginEdit', 0);
	//初始化列表编辑控件
	editorInit(0);
});
//保存按钮点击事件
$("#saveRows").click(function(){
	var _dataPanel = $('#signList').datagrid('getPanel');
	var _editing = _dataPanel.find(".datagrid-editable");
	if(_editing.length>0){
		$.fool.alert({time:2000,msg:'你还有没编辑完成的数据,请先确认！'});
		return false;
	};
	var rows=$('#signList').datagrid('getRows');
	var vo='${jsonObj}';
	var jsonVo=JSON.parse(vo);
	rows.unshift(jsonVo);
	$('#saveRows').attr("disabled","disabled");
	$.post('${ctx}/initiBalance/save',{vos:JSON.stringify(rows)},function(data){
		dataDispose(data);
		if(data.result =='0'){
			$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
				$('#signBox').window('close');
				$('#saveRows').removeAttr("disabled");
				$('#subjectList').treegrid('reload');
			}});
		}else if(data.result =='1'){
			$.fool.alert({msg:data.msg,fn:function(){
				$('#saveRows').removeAttr("disabled");
			}});
		}else{
			$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
		    $('#saveRows').removeAttr("disabled");
		}
		rows.splice(0,1);
		return true;
	});
});

//列表操作方法
function updateActions(index){
	$('#signList').datagrid('updateRow',{
		index: index,
		row:{}
	});
}
function getRowIndex(target){
		var tr = $(target).closest('tr.datagrid-row');
		return parseInt(tr.attr('datagrid-row-index'));
}
function editrow(target){
	var index=getRowIndex(target);
	var rows=$('#signList').datagrid('getRows');
	$('#signList').datagrid('beginEdit', index);
	//初始化列表编辑控件
	editorInit(index,rows[index]);
}
function deleterow(target){
	$.fool.confirm({
		msg:'确定删除这条记录吗?',
	    fn:function(r){
	    	if (r){
				$('#signList').datagrid('deleteRow', getRowIndex(target));
	    	}
	    }
	});
}
function saverow(target){
	var index=getRowIndex(target);
    var supId="";
    var supName="";
    if('${obj.supplierSign}'==1){
    	supId=$(target).closest('[field="action"]').siblings('[field="supplierId"]').children().text();
    	supName=($($('#signList').datagrid("getEditor",{index:index,field:'supplierName'}).target).next())[0].comboObj.getComboText();
	}
    var cusId="";
	var cusName="";
    if('${obj.customerSign}'==1){
    	cusId=$(target).closest('[field="action"]').siblings('[field="customerId"]').children().text();
    	cusName=($($('#signList').datagrid("getEditor",{index:index,field:'customerName'}).target).next())[0].comboObj.getComboText();
	}
    var memberId="";
    var memberName="";
    if('${obj.memberSign}'==1){
    	memberId=$(target).closest('[field="action"]').siblings('[field="memberId"]').children().text();
    	memberName=($($('#signList').datagrid("getEditor",{index:index,field:'memberName'}).target).next())[0].comboObj.getComboText();
	}
	var goodsId="";
	var goodsName="";
	if('${obj.goodsSign}'==1){
		goodsId=$(target).closest('[field="action"]').siblings('[field="goodsId"]').children().text();
		goodsName=($($('#signList').datagrid("getEditor",{index:index,field:'goodsName'}).target).next())[0].comboObj.getComboText();
	}
	var departmentName="";
	var departmentId="";
	if('${obj.departmentSign}'==1){
		departmentName=($(target).closest('[field="action"]').siblings('[field="departmentName"]').find(".datagrid-editable-input").next())[0].comboObj.getComboText();
		departmentId=($(target).closest('[field="action"]').siblings('[field="departmentName"]').find(".datagrid-editable-input").next())[0].comboObj.getSelectedValue();
	}
	var warehouseName="";
	var warehouseId="";
    if('${obj.warehouseSign}'==1){
    	warehouseName=($(target).closest('[field="action"]').siblings('[field="warehouseName"]').find(".datagrid-editable-input").next())[0].comboObj.getComboText();
    	warehouseId=($(target).closest('[field="action"]').siblings('[field="warehouseName"]').find(".datagrid-editable-input").next())[0].comboObj.getSelectedValue();
    }
    var projectId="";
	var projectName="";
	if('${obj.projectSign}'==1){
		projectId=($(target).closest('[field="action"]').siblings('[field="projectName"]').find(".datagrid-editable-input").next())[0].comboObj.getSelectedValue();
		projectName=($(target).closest('[field="action"]').siblings('[field="projectName"]').find(".datagrid-editable-input").next())[0].comboObj.getComboText();
	}
	if($(target).closest('tr.datagrid-row').form('validate')){
		$('#signList').datagrid('endEdit', getRowIndex(target));
		$('#signList').datagrid('updateRow',{
			index: index,
			row:{
				'supplierId':supId,
				'customerId':cusId,
				'memberId':memberId,
				'goodsId':goodsId,
				'supplierName':supName,
				'customerName':cusName,
				'memberName':memberName,
				'goodsName':goodsName,
				'departmentName':departmentName,
				'departmentId':departmentId,
				'warehouseName':warehouseName,
				'warehouseId':warehouseId,
				'projectId':projectId,
				'projectName':projectName,
				'isNew':0
			}
		});
	}else{
		return false;
	}
}
function cancelrow(target){
	var isNew=$(target).closest('[field="action"]').siblings('[field="isNew"]').children().text();
	if(isNew!=1){
		$('#signList').datagrid('cancelEdit', getRowIndex(target));
	}else{
		$('#signList').datagrid('deleteRow', getRowIndex(target));
	}
}


$.extend($.fn.validatebox.defaults.rules, {
    amount:{
    	validator:function(value,param){
    		return ((/^(([+]?[0-9]\d*)|\d)(\.\d{1,2})?$/).test(value)||(/^(([-]?[0-9]\d*)|\d)(\.\d{1,2})?$/).test(value))&&parseFloat(value)<=9999999999.99&&parseFloat(value)>=-9999999999.99;
    	},
    	message:'最多能输入10位数，小数精确到2位，请输入正确的金额'
    },
    exchangeRate:{
    	validator:function(value,param){
    		return ((/^(([+]?[0-9]\d*)|\d)(\.\d{1,4})?$/).test(value)||(/^(([-]?[0-9]\d*)|\d)(\.\d{1,4})?$/).test(value))&&parseFloat(value)<=9999999999.99&&parseFloat(value)>=-9999999999.99;
    	},
    	message:'最多能输入10位数，小数精确到4位，请输入正确的汇率'
    },
    gridValid: {    
        validator: function(value,param){
        	if($(this).closest('tr.datagrid-row').find('[field="'+param[0]+'"]').children().text()==""||$(this).closest('tr.datagrid-row').find('[field="'+param[0]+'"]').children().text()=="null"){
        		return false;
        	}else{
        		return true;
        	}
        },    
        message: '没有选中，请重新选择'   
    },
});

function keyHandler(){
	var selected="";
	var row="";
	var index="";
	var field="";
	var panel=$("#signList").datagrid('getPanel').panel('panel');
	panel.on("focus","input",function(e){
		selected=$(this);
		row=$(this).closest(".datagrid-row");
		index=row.attr("datagrid-row-index");
		field=$(this).closest(".datagrid-editable").parent().attr("field");
	});
	panel.bind('keydown', function (e) {
		switch (e.keyCode) {
		    case 37: // left
		            if(selected){
		            	var ed=getLastField(row,field,index);
		            	if(ed){
		            		if($(ed.target).next().attr("class")=="dhxDiv"){
		            			($(ed.target).next())[0].comboObj.openSelect();
		            			$(ed.target).next().find(".dhxcombo_input").click();
		            			$(ed.target).next().find(".dhxcombo_input").focus();
		            		}else if($(ed.target).next().attr("class").search(/numberbox/)!=-1){
		            			$(ed.target).numberbox("textbox").focus();
		            			$(ed.target).numberbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed.target).numberbox("textbox").select();
		            			},1);
		            		}else if($(ed.target).next().attr("class").search(/textbox/)!=-1){
		            			$(ed.target).textbox("textbox").focus();
		            			$(ed.target).textbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed.target).textbox("textbox").select();
		            			},1);
		            		}else{
		            			$(ed.target).focus();
			            		$(ed.target).click();
			            		//获取焦点后全选内容
			            		setTimeout(function(){
			            			$(ed.target).select();
		            			},1);
		            		}
		            	}
		            }
	                break;
	                
	        case 38: // up
	        	    if(selected){
	        	    	var ed=getUpField(row,field,index);
	        	    	if(ed){
	        	    		if($(ed.target).next().attr("class")=="dhxDiv"){
		            			($(ed.target).next())[0].comboObj.openSelect();
		            			$(ed.target).next().find(".dhxcombo_input").click();
		            			$(ed.target).next().find(".dhxcombo_input").focus();
		            		}else if($(ed.target).next().attr("class").search(/numberbox/)!=-1){
		            			$(ed.target).numberbox("textbox").focus();
		            			$(ed.target).numberbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed.target).numberbox("textbox").select();
		            			},1);
		            		}else if($(ed.target).next().attr("class").search(/textbox/)!=-1){
		            			$(ed.target).textbox("textbox").focus();
		            			$(ed.target).textbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed.target).textbox("textbox").select();
		            			},1);
		            		}else{
		            			$(ed.target).focus();
			            		$(ed.target).click();
			            		//获取焦点后全选内容
			            		setTimeout(function(){
			            			$(ed.target).select();
		            			},1);
		            		}
		            	}
	        	    }
                    break; 
		
			case 39: // right
				    if(selected){
				    	var ed=getNextField(row,field,index);
				    	if(ed){
				    		if($(ed.target).next().attr("class")=="dhxDiv"){
		            			($(ed.target).next())[0].comboObj.openSelect();
		            			$(ed.target).next().find(".dhxcombo_input").click();
		            			$(ed.target).next().find(".dhxcombo_input").focus();
		            		}else if($(ed.target).next().attr("class").search(/numberbox/)!=-1){
		            			$(ed.target).numberbox("textbox").focus();
		            			$(ed.target).numberbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed.target).numberbox("textbox").select();
		            			},1);
		            		}else if($(ed.target).next().attr("class").search(/textbox/)!=-1){
		            			$(ed.target).textbox("textbox").focus();
		            			$(ed.target).textbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed.target).textbox("textbox").select();
		            			},1);
		            		}else{
		            			$(ed.target).focus();
			            		$(ed.target).click();
			            		//获取焦点后全选内容
			            		setTimeout(function(){
			            			$(ed.target).select();
		            			},1);
		            		}
				    	}
	                }
			        break;
			case 40: // down
				    if(selected){
				    	var ed=getDownField(row,field,index);
	        	    	if(ed){
	        	    		if($(ed.target).next().attr("class")=="dhxDiv"){
		            			($(ed.target).next())[0].comboObj.openSelect();
		            			$(ed.target).next().find(".dhxcombo_input").click();
		            			$(ed.target).next().find(".dhxcombo_input").focus();
		            		}else if($(ed.target).next().attr("class").search(/numberbox/)!=-1){
		            			$(ed.target).numberbox("textbox").focus();
		            			$(ed.target).numberbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed.target).numberbox("textbox").select();
		            			},1);
		            		}else if($(ed.target).next().attr("class").search(/textbox/)!=-1){
		            			$(ed.target).textbox("textbox").focus();
		            			$(ed.target).textbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed.target).textbox("textbox").select();
		            			},1);
		            		}else{
		            			$(ed.target).focus();
			            		$(ed.target).click();
			            		//获取焦点后全选内容
			            		setTimeout(function(){
			            			$(ed.target).select();
		            			},1);
		            		}
	        	    	}
				    }
		            break;
			case 13: // enter 需求变更
				if(selected){
			    	row.find("[field='action']").find(".btn-save").click();
			    	setTimeout(function(){
			    		console.log(row.find("[field='editing']").children().text());
			    		if(row.find("[field='editing']").children().text()=="false"){
				    		$("#appendRows").click();
				    	}
					}, 1);
                }
	            break;
		}
	});
}
//获取指定行的编辑列
function getEdFields(row){
	if(row){
		var tds=row.children();
		var edFields=[];
		for(var i=0;i<tds.length;i++){
			if ($(tds[i]).children().attr('class').search(/datagrid-editable/)!=-1&&$(tds[i]).css("display")!="none"){
				if(!row.children("[field='goodsSpecGroupId']").children().text()&&!row.children("[field='goodsSpecGroupId']").find(".datagrid-editable-input").val()&&$(tds[i]).attr("field")=="goodsSpecName"){
					continue;
				}
				edFields.push($(tds[i]));
			}
		}
		return edFields;
	}
}
//获取同行下一个编辑框，没有就保存
function nextOrSave(row,field,index){
	var edFields=getEdFields(row);
	for(var i=0;i<=edFields.length;i++){
		if($(edFields[i]).attr("field")==field&&parseInt(i)+1<edFields.length){
			return $("#signList").datagrid('getEditor',{index:index,field:$(edFields[i+1]).attr("field")});
		}
	}
	return false;
}
//获取下一个编辑框
function getNextField(row,field,index){
	var edFields=getEdFields(row);
	var result="";
	for(var i=0;i<=edFields.length;i++){
		if($(edFields[i]).attr("field")==field&&parseInt(i)+1<edFields.length){
			result=$("#signList").datagrid('getEditor',{index:index,field:$(edFields[i+1]).attr("field")});
			if($(result.target).attr("disabled")=="disabled"){
				result=getNextField(row,$(edFields[i+1]).attr("field"),index);
			}
			return result;
		}else if($(edFields[i]).attr("field")==field&&i+1>=edFields.length){
			var rows=$("#signList").datagrid('getRows');
			var num=rows.length;
			var nextRow="";
			var fields="";
			for (var j=parseInt(index)+1;j<num;j++){
				nextRow=row.siblings("[datagrid-row-index="+j+"]");
				fields=getEdFields($(nextRow[0]));
				if(fields.length>0){
					result=$("#signList").datagrid('getEditor',{index:j,field:$(fields[0]).attr('field')});
					if($(result).attr("disabled")=="disabled"){
						result=getNextField(nextRow,$(fields[0]).attr('field'),j);
					}
					return result;
				}
			}
		};
	}
}
//获取上一个编辑框
function getLastField(row,field,index){
	var edFields=getEdFields(row);
	var result="";
	for(var i=0;i<edFields.length;i++){
		if($(edFields[i]).attr("field")==field&&i-1>=0){
			result=$("#signList").datagrid('getEditor',{index:index,field:$(edFields[i-1]).attr("field")});
			if($(result.target).attr("disabled")=="disabled"){
				result=getLastField(row,$(edFields[i-1]).attr("field"),index);
			}
			return result;
		}else if($(edFields[i]).attr("field")==field&&i-1<0){
			var lastRow="";
			var fields="";
			for (var j=parseInt(index)-1;j>=0;j--){
				lastRow=row.siblings("[datagrid-row-index="+j+"]");
				fields=getEdFields($(lastRow[0]));
				if(fields.length>0){
					result=$("#signList").datagrid('getEditor',{index:j,field:$(fields[fields.length-1]).attr('field')});
					if($(result.target).attr("disabled")=="disabled"){
						result=getLastField(lastRow,$(fields[fields.length-1]).attr('field'),j);
					}
					return result;
				}
			}
		};
	}
}
//获取上一行编辑框
function getUpField(row,field,index){
	var lastRow="";
	var fields="";
	for (var j=parseInt(index)-1;j>=0;j--){
		lastRow=row.siblings("[datagrid-row-index="+j+"]");
		fields=getEdFields($(lastRow[0]));
		if(fields){
			return $("#signList").datagrid('getEditor',{index:j,field:field});
		}
	}
}
//获取下一行编辑框
function getDownField(row,field,index){
	var rows=$("#signList").datagrid('getRows');
	var num=rows.length;
	var nextRow="";
	var fields="";
	for (var j=parseInt(index)+1;j<num;j++){
		nextRow=row.siblings("[datagrid-row-index="+j+"]");
		fields=getEdFields($(nextRow[0]));
		if(fields){
			return $("#signList").datagrid('getEditor',{index:j,field:field});
		}
	}
}
$.extend($.fn.validatebox.defaults.rules, {
	quentity:{//验证金额，小数点后两位
        validator: function (value, param) {
        	if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
  			  if($(this).attr("class").indexOf("validatebox-text")!=-1){
  				  $(this).unbind("blur");
  				  $(this).blur(function(){//对数字进行去空格和自动保留2位小数
  					    var value = $(this).val();
          				var myvalue = "";
          				if(value!="" && !isNaN($.trim(value))){
	          					var nv = $.trim(value)+"";
	          					myvalue = parseFloat(nv).toFixed(2)+'';
          				}
	      					if($(this).attr("class").indexOf("textbox-text")!=-1){
	      						$(this).parent().prev().textbox('setText',myvalue);
	      						$(this).parent().prev().textbox('setValue',myvalue);
	      					}else{
	      						$(this).val(myvalue);
	      					}
          		});
          		}
        	  }
         	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
         },
         message:'数量必须大于等于0，并最多2位小数'
     },	
})
</script>
</body>
</html>