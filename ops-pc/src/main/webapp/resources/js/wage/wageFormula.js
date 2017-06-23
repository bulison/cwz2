/**
 * 工资公式计算
 */
var leftpos,toppos, divoffset = 0,eindex = 0; 
var typeData = [{ "value": "0", "text": "手动输入" },{ "value": "1", "text": "公式计算" }]
var viewData = [{ "value": "1", "text": "是" },{ "value": "0", "text": "否" }];
var textbox = {type:'textbox'};
var textEditor = { type: 'validatebox', options: {validType:'itemNum',required:true}};
var numberEditor = { type: 'validatebox', options: {validType:['accessoryNumber','maxLength[10]'],required:true}};
var floatEditor = { type: 'validatebox', options: {validType:['intOrFloat','maxLength[10]'],required:true}};//,novalidate:true
var typecombox = { type: 'combobox', options: { data: typeData, valueField: "value", textField: "text", editable:false, required:true,onSelect:selectType} };
var viewcombox = { type: 'combobox', options: { data: viewData, valueField: "value", textField: "text", readonly:true, editable:false, required:true} };
var formula='';//公式框

$(function() {
	$('#dataTable').jqGrid();
	$('#dataTable3').datagrid();
	$('#window').window({
		title:'',
	    width:150,    
	    modal:false   
	});
	$('#window').window('close');
});
function selectType(record){
	var ind = $(this).fool('getRowIndex');
	var isView = getTableEditor(ind,'isView');
	if(record.value==1){
		isView.combobox({readonly:false});
		isView.combobox("setValue",1);
	}else{
		isView.combobox({readonly:true});
		isView.combobox("setValue",1);
	}
}
function refresh(){
	$('#dataTable').trigger('reloadGrid');
	$("#window").window("close");
}
function cloumnType(value){
	if(value=='0'){
		return "手动输入";
	}else{
		return "公式计算";
	}
}
function isview(value){
	if(value=='0'){
		return "否";
	}else{
		return "是";
	}
}
function defaultValue(value){
	if(value==null||value==''){
		return 0;
	}else{
		return value;
	}
}
function biginEnable(row,index){
	row.editing=true;
}
function editEnable(row,index){
	row.editing=true;
}
function editDisable(row,index){
	row.editing=false;
}
function action(value,options,row){
	return getTBBtn(value,options,row);
}
function getTBBtn(value,options,row){
	if(row.editing){
		var s = "<a href='javascript:void(0);' class='btn-save editing-on ' onclick='save(this,\""+options.rowId+"\",\""+row.fid+"\")' title='确认'></a>";
		var c = "<a href='javascript:void(0);' class='btn-back' onclick='cancel(\""+options.rowId+"\",\""+row.isNew+"\")' title='撤销'></a>";
		return s+c;
	}else{
		var row = $('#dataTable').jqGrid("getRowData",options.rowId);
	    var r = "<a href='javascript:void(0);' class='btn-del' onclick='delRow(this,\""+options.rowId+"\",\""+row.fid+"\")' title='删除'></a>";
	    //var e = "<a href='javascript:void(0);' class='btn-edit' onclick='editrow("+options.rowId+")' title='编辑'></a>";
	    return r;
	}
}


/*//添加一行
function addRow(){
	var len = $("#dataTable").datagrid("getRows").length;
	$("#dataTable").datagrid("insertRow",{index:len,row:{flag:true,defaultValue:0.0,isView:1,orderNo:len+1,_isNew:true,action:1}});
	$("#dataTable").datagrid("beginEdit",len);
	var target = getTableEditor(len,'formula');
	bindEvent();
	bindEnter(target);
	eindex = len;
}*/
function addRow(){
	var rows = $("#dataTable").jqGrid('getRowData').length;	
	for(var i=1;i<=rows;i++){
		var _editing=$('#dataTable').find('.btn-save').length;		
	}
	if(_editing>0){
		$.fool.alert({msg:'你还有没编辑完,请先确认！'});
		return false;
	  }; 
	var len = $("#dataTable").jqGrid('getRowData').length+1;
	$("#dataTable").jqGrid('addRowData',len,{flag:true,defaultValue:0.0,isNew:1,orderNo:len,action:1,isView:1});
	editRow(len)
	var target = formula;
	/*bindEvent();
	bindEnter(target);*/	
}


/*function editrow(len){
	editRow(len);
}*/

//编辑
/*function editRow(obj){
	var ind = $(obj).fool('getRowIndex');
	$("#dataTable").datagrid('beginEdit',ind);
	$(obj).parent().html(getTBBtn(true));
	var target = getTableEditor(ind,'formula');
	var columnType = getTableEditor(ind,'columnType').textbox('getValue');
	var isView = getTableEditor(ind,'isView');
	var v = isView.combobox('getValue')
	if(columnType==1){
		isView.combobox({readonly:false});
		isView.combobox("setValue",v);
	}else{
		isView.combobox({readonly:true});
		isView.combobox("setValue",v);
	}
	bindEvent();
	bindEnter(target);
	$('td[field=formula] .textbox-text').focus(function(e){
		tooltip(this);
	});
	eindex = ind;
	$("#window").window('close');
}*/
function editRow(len){
	//判断货品是否编辑完，没有编辑完不能添加下一条
	var rowData=$("#dataTable").jqGrid('getRowData',len);
	if(rowData.editing=="true"){//设置编辑状态
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	var rows = $("#dataTable").jqGrid('getRowData').length;	
	for(var i=1;i<=rows;i++){
		var _editing=$('#dataTable').find('.btn-save').length;		
	}
	if(_editing>0){
		$.fool.alert({msg:'你还有没编辑完,请先确认！'});
		return false;
	  }; 
	$('#dataTable').jqGrid('setRowData', len, rowData);
	jQuery("#dataTable").jqGrid('editRow',len);//打开编辑行

	var columnName=getEditor("dataTable",len,"columnName").validatebox({
		required:true,
		validType:['itemNum','maxLength[50]'],
		width:'100%',height:'100%'
	});
	
	var defaultValue=getEditor("dataTable",len,"defaultValue").validatebox({
		required:true,
		validType:['amount','maxLength[50]'],
		width:'100%',height:'100%'
	});
	
	var orderNo=getEditor("dataTable",len,"orderNo").validatebox({
		required:true,
		validType:['accessoryNumber','maxLength[50]'],
		width:'100%',height:'100%'
	});
		
	formula=getEditor("dataTable",len,"formula").validatebox({
		validType:'maxLength[50]',
		width:'100%',height:'100%'
	});
	var $formula=$('#'+len+'_formula');//设置控件宽度
	var $orderNo=$('#'+len+'_orderNo');
	var $columnName=$('#'+len+'_columnName');
	var $defaultValue=$('#'+len+'_defaultValue');
	$formula.addClass('formula');
	$orderNo.addClass('formula');
	$columnName.addClass('formula');
	$defaultValue.addClass('formula');
	
	var columnType=getEditor('dataTable',len,"columnType").fool("dhxCombo",{		
		value:rowData.columnType,
		clearOpt:false,
        width:'100%',
        height:'80%',
		/*required:true,
		novalidate:true,*/
		editable:false,
		valueField: 'value',
		textField: 'text',
		data:[
		      {
		    	  value: '0',
			      text: '手动输入'
		      },{
		    	  value: '1',
			      text: '公式计算'
		      }
		     ],
		 onLoadSuccess:function(combo){		
			combo.setComboValue(0);
			}	
	});
	columnType.readonly(true);
	if(rowData.columnType=='手动输入'){
		columnType.setComboValue("0");
	}else if(rowData.columnType=='公式计算'){
		columnType.setComboValue("1");
	}
	
	var isView=getEditor('dataTable',len,"isView").fool("dhxCombo",{		
		value:rowData.isView,
		editable:false,
		clearOpt:false,
		valueField: 'value',
		textField: 'text',
        width:'100%',
		height:'80%',
		data:[
		      {
		    	  value: '0',
			      text: '否'
		      },{
		    	  value: '1',
			      text: '是'
		      }
		     ]
	});
	isView.readonly(true);
	if(rowData.isView=='否'){
		isView.setComboValue("0");
	}else if(rowData.isView=='是'){
		isView.setComboValue("1");
	}
	//$('#'+len+'_columnName').style.height="60"+"px";
	
	bindEvent();
	bindEnter(formula);
	formula.focus(function(e){
		tooltip(this);
	});
	$("#window").window('close');
}

//enter键保存
function bindEnter(target){
	$('.datagrid-editable .textbox,.datagrid-editable .datagrid-editable-input,.datagrid-editable .textbox-text').bind('keyup', function(e){
		var code = e.keyCode || e.which;
	    if(code == 13){	    	
	    	save(target);
	    }
	});
}
/*function bindEvent(){
	$('td[field=formula] .textbox-text').bind('keyup', function(e){
		var code = e.keyCode || e.which;
		mouses(this,code);
	});
	$('td[field=formula] .textbox-text').bind('mouseover', function(e){
		tooltip(this);
	});
	$('td[field=formula] .textbox-text').bind('focus', function(e){
		tooltip(this);
	});
}*/
function bindEvent(){
	formula.bind('keyup', function(e){
		var code = e.keyCode || e.which;
		mouses(this,code);
	});
	formula.bind('mouseover', function(e){
		tooltip(this);
	});
	formula.bind('focus', function(e){
		tooltip(this);
	});
}

function tooltip(e){
	$(e).tooltip({
	    position: 'right',
	    content: '<span style="color:#fff">当录入“（”时，自动补充“）”；<br/>当录入“@”时，弹出字段的选择框</span>',
	    onShow: function(){
			$(e).tooltip('tip').css({
				backgroundColor: '#666',
				borderColor: '#666'
			});
	    }
	});
}
//鼠标事件
function mouses(e,code){
	var key = $(e).val();
	var len = key.split('@').length;
	var size = $("#dataTable3").jqGrid('getRowData').length;
	size=2;
	if(size>1){
		if(len > 1){			
			$("#window").window({left:$(e).offset().left, top:$(e).offset().top+20,});
			$('#dataTable3').datagrid('reload');    
			//getTableEditor2(1,'fid').focus();
		}else{
			$("#window").window("close");
		}
	}
	if(key.indexOf("（")>=0){
		$.fool.alert({msg:"请输入英文小括号("});
		return;
	}
	if(code==57){
		//var $formula =  getTableEditor(eindex,'formula');
		if(key.indexOf("(")>=0){
			formula.val(key+')');	
		}		
	}
}
//选择
function selectData(index, row){
	/*var text = getTableEditor(eindex,'formula').val();
	var name = row.columnName;
	var result = text.replace("@",name);
	var $formula =  getTableEditor(eindex,'formula');
	$formula.textbox('setValue',result);
	$formula.focus();
	$("#window").window('close');*/
	var text=formula.val();
	var name = row.columnName;
	var result = text.replace("@",name);
	formula.val(result);
	formula.focus();
	$("#window").window('close');
}
/*//保存
function save(obj){
	$("#window").window('close');
	var ind = $(obj).fool('getRowIndex');
	//验证
	var v = $("#dataTable").datagrid('validateRow',ind);
	if(v){		    
		    var rowData = $("#dataTable").datagrid("getRows");
		    $("#dataTable").datagrid('endEdit',ind);
			$.post(getRootPath()+'/wageFormula/save',rowData[ind], function(data) {
				dataDispose(data);
				if(data.result=="0"){
						$("#dataTable").datagrid('beginEdit',ind);
						var eFid = getTableEditor(ind,'fid');
						var eNews = getTableEditor(ind,'_isNew');
						eFid.textbox("setValue",data.obj.fid);
						eNews.textbox("setValue",false);
						$("#dataTable").datagrid('endEdit',ind);
						var str = $(".datagrid-body").find("tr").attr("id");
						var arr = str.split('-');
						$('#dataTable3').datagrid('reload');
				}else{
				  var str = $(".datagrid-body").find("tr").attr("id");
				  var arr = str.split('-');
				  $("#dataTable").datagrid('beginEdit',ind);
				  bindEvent();
				  var target = getTableEditor(ind,'formula');
				  bindEnter(target);
				  $("#datagrid-row-"+arr[2]+"-2-"+ind+" td[field='action'] .datagrid-cell").html(getTBBtn(true));
				  $.fool.alert({msg:data.msg});
				}
			});
	}
}*/

//保存
function save(obj,index,fid){
	$("#window").window('close');
//	var a =getEditor("dataTable",index,"columnName");
//	var b =getEditor("dataTable",index,"orderNo");
//	var c =getEditor("dataTable",index,"defaultValue");
//	var d =getEditor("dataTable",index,"columnType");
//	$(a[0]).validatebox('enableValidation');
//	$(b[0]).validatebox('enableValidation');
//	$(c[0]).numberbox('enableValidation');
	//$(d[0]).validatebox('enableValidation');
	var row = $('#dataTable #'+index);
	row.form("enableValidation");
	if(!row.form("validate")/*!d.validatebox('isValid')||*//*!a.validatebox('isValid')||!b.validatebox('isValid')||!c.numberbox('isValid')*/){
		return false;
	}
		var fid=fid;
		var columnName=getEditor("dataTable",index,"columnName").val();		
		var columnType=(getEditor("dataTable",index,"columnType").next())[0].comboObj.getSelectedValue();
		if(columnType=='公式计算'){
			columnType='1';
		}else if(columnType=='手动输入'){
			columnType='0';
		}			
		var formula=getEditor("dataTable",index,"formula").val();		
		var defaultValue=getEditor("dataTable",index,"defaultValue").val();
		var orderNo=getEditor("dataTable",index,"orderNo").val();
		var remark=getEditor("dataTable",index,"remark").val();
		var isView=(getEditor("dataTable",index,"isView").next())[0].comboObj.getSelectedValue();
		if(isView=='是'){
			isView='1';
		}else if(isView=='否'){
			isView='0';
		}
	    $.post(getRootPath()+'/wageFormula/save',{fid:fid,columnName:columnName,columnType:columnType,
				formula:formula,defaultValue:defaultValue,orderNo:orderNo,remark:remark,isView:isView}, function(data) {
	    		//console.log(data);
		dataDispose(data);
		if(data.result=="0"){
			/*$.fool.alert({time:1000,msg:'保存成功！',fn:function(){		*/		
				$('#dataTable').jqGrid('saveRow',index);
				$('#dataTable').jqGrid('setRowData',index,{fid:data.obj.fid,columnName:columnName,columnType:columnType,formula:formula,defaultValue:defaultValue,orderNo:orderNo,remark:remark,isView:isView,editing:false,action:null});				
				//}});
				$("#dataTable").trigger("reloadGrid");
		}else{
			$.fool.alert({msg:data.msg});			
			}});
}

function delRow(obj,index,fid){
	$("#window").window('close');
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r) 
			$.post(getRootPath()+'/wageFormula/deleteWageFormula',{fid:fid}, function(data) {
				dataDispose(data);
				if(data.result=="0"){
					  $.fool.alert({time:1000,msg:'删除成功'});
					 $('#dataTable').trigger('reloadGrid');
					 $('#dataTable3').trigger('reloadGrid');
				  return true;
				}else{
				  $.fool.alert({msg:data.msg});
				}
			});	
	}});
}

function cancel(index,value){
	$("#window").window('close');
	if(value==1){		
		$.fool.confirm({msg:'确定要撤销这一项吗？',fn:function(r){
		  if(r){
			$("#dataTable").jqGrid('delRowData',index);
		  }}});
	}else{	
		$('#dataTable').jqGrid('setRowData', index, {editing:false,action:null});//编辑状态转换，按钮变化
		$("#dataTable").jqGrid('restoreRow', index);
	}
}

//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}
