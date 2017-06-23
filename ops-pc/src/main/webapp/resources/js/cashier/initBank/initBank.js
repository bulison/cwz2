/**
 * 银行初始化
 */
function bindEnter(target,index){
	$('.datagrid-editable .textbox,.datagrid-editable .datagrid-editable-input,.datagrid-editable .textbox-text').bind('keyup', function(e){
		var code = e.keyCode || e.which;
	    if(code == 13){
	    	save(index);
	    }
	});
}
var _flag,rData, win,editFlag = false;
function status(value){
	return value=="1"?"启用":"未启用"
}
//银行单据窗口
function bankBill(bid,type){
	var _title = type==2?"银行未达单":"企业未达单";
	$("#my-window").fool('window',{modal:true,title:_title,width:$(window).width()-120,
	   top:100+$(document).scrollTop(),
	   height:$(window).height()-120,href:getRootPath()+"/cashierBankBillController/goBankBill?bid="+bid+"&type="+type,
	   onClose:function(){
		   $('html').css('overflow','');//控制弹出窗口无法滚动
		   if($("#bank-edit-window")){
			   $("#bank-edit-window").window("destroy");
		   }
		   win.window("destroy");
		   $(this).window("clear");
	   }
	});
	if($('#my-window').is(":visible")==true){//控制弹出窗口无法滚动
		$('html').css('overflow','hidden');
	}
	return false;
};
//获取表格里面某个编辑器方法
function getTableEditor(index,field){ 
	return getEditor("dataTable",index,field);
}
function editRow(ind){
	var rowData=$("#dataTable").jqGrid('getRowData',ind);
	if(rowData.editing=="true"){
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#dataTable').jqGrid('setRowData', ind, rowData);
	$("#dataTable").jqGrid('editRow',ind);
	getTableEditor(ind,'accountInit').numberbox({
		validType:'positive',required:true,precision:2
	});
	getTableEditor(ind,'statementInit').numberbox({
		validType:'positive',required:true,precision:2
	})
	var cashSubject = getTableEditor(ind,'cashSubject').val();
	if(cashSubject == 1){
		getTableEditor(ind,'statementInit').numberbox('disable');
	}
	var target = getTableEditor(ind,'accountInit');
	bindEnter(target,ind);
	/*$(obj).parent().html(getTBBtn(true,ind));*/
}
function save(ind,fid){
	var rowData=$("#dataTable").jqGrid('getRowData',ind);
	var fid=fid;
	var updateTime=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_updateTime]').text();
	var accountInit=getEditor("dataTable",ind,"accountInit").val();
	var statementInit=getEditor("dataTable",ind,"statementInit").val();
	var cashSubject=getEditor("dataTable",ind,"cashSubject").val();
	var bankSubject=getEditor("dataTable",ind,"bankSubject").val();
	var subjectCode=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_subjectCode]').text();
	var subjectName=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_subjectName]').text();
	var accountDebit=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_accountDebit]').text();
	var accountCredit=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_accountCredit]').text();
	var accountBalance=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_accountBalance]').text();
	var statementDebit=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_statementDebit]').text();
	var statementCredit=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_statementCredit]').text();
	var statementBalance=$('#dataTable').find('#'+ind).find('[aria-describedby=dataTable_statementBalance]').text();
	$("#dataTable").find("tr[id='"+ind+"']").form("enableValidation");
	var v = $("#dataTable").find("tr[id='"+ind+"']").form("validate");
	if(v){
		$.post(getRootPath()+'/cashierBankInitController/save',{fid:fid,accountInit:accountInit,statementInit:statementInit,subjectCode:subjectCode,subjectName:subjectName,cashSubject:cashSubject,bankSubject:bankSubject,
			accountDebit:accountDebit,accountCredit:accountCredit,accountBalance:accountBalance,statementInit:statementInit,statementDebit:statementDebit,statementCredit:statementCredit,statementBalance:statementBalance,updateTime:updateTime}, function(data) {
			dataDispose(data);
			if(data.returnCode=="0"){
				rowData.editing=false;
				rowData.action=null;
				$('#dataTable').jqGrid('setRowData', ind, rowData);
			   $("#dataTable").jqGrid('saveRow',ind);
			   $('#dataTable').trigger("reloadGrid");
			}else{
			   $.fool.alert({time:1000,msg:data.message});
			}
			return true;
		});
	}
}
//引入
$("#lead").click(function(){
	$.post(getRootPath()+'/cashierBankInitController/importBank',null, function(data) {
		dataDispose(data);
		if(data.returnCode=="0"){
			$('#dataTable').trigger("reloadGrid");
		   $.fool.alert({time:1000,msg:"引入成功"});
		}else{
			$.fool.alert({time:1000,msg:data.message});
		}
		return true;
	});
});
$("#refresh").click(function(){
	//$('#dataTable').datagrid("reload");
	window.location.reload();//刷新整个页面
});
//详细页。货品列表取消按钮
function cancel(ind){
	$("#dataTable").jqGrid('restoreRow', ind);
	var rowData=$("#dataTable").jqGrid('getRowData',ind); 
	console.log(rowData);
	rowData.editing=false;
	rowData.action=null;
	$('#dataTable').jqGrid('setRowData', ind, rowData);
}
//删除
function delRow(value){
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r) 
			$.post(getRootPath()+'/cashierBankInitController/delete',{fid:value}, function(data) {
				dataDispose(data);
				if(data.returnCode=="0"){
					 $.fool.alert({time:1000,msg:"删除成功"});
					$('#dataTable').trigger("reloadGrid");
				  return true;
				}else{
				  $.fool.alert({time:1000,msg:data.message});
				}
			});	
	}});
}
//启用
function updateUse(){
	$.fool.confirm({msg:'确定要启用吗？',fn:function(r){
		if(r){
			$.post(getRootPath()+'/cashierBankInitController/updateUse', function(data) {
				dataDispose(data);
				if(data.returnCode=="0"){
					$('#dataTable').trigger("reloadGrid");
				   $.fool.alert({time:1000,msg:"启用成功"});
				}else{
				   if(data.msg!=''){
				     $.fool.alert({time:1000,msg:data.message});
				   }
				}
				return true;
			});
		}
	}});
}
//反启用
function updateUnUse(){
	$.fool.confirm({msg:'确定要反启用吗？',fn:function(r){
		if(r){
			$.post(getRootPath()+'/cashierBankInitController/updateUnUse', function(data) {
				dataDispose(data);
				if(data.returnCode=="0"){
					$('#dataTable').trigger("reloadGrid");
				   $.fool.alert({time:1000,msg:"反启用成功"});
				}else{
				   $.fool.alert({time:1000,msg:data.message});
				}
				return true;
			});
		}
	}});
}
//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}
