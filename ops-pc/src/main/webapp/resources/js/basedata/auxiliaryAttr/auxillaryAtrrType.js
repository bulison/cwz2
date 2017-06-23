/**
 * 
 */
function refresh(){
	$('#dataTable').trigger('reloadGrid');
}

//操作按钮
function action(cellvalue,options,rowObject){ 
	if (rowObject.editing){
		var s="<a href='javascript:;' title='保存' class='btn-save' onclick='saverow(\""+options.rowId+"\",\""+rowObject.fid+"\")'></a>";
		var c="<a href='javascript:;' title='取消' class='btn-cancel' onclick='cancelrow(\""+options.rowId+"\",\""+rowObject.isNew+"\")'></a>";
		return s+c;
	}else{
		//var e='<a href="javascript:;" title="编辑" class="btn-edit" onclick="editrow('+options.rowId+')" ></a>';
		return '';
	}
}

//新增明细按钮点击事件添加新的一行并打开编辑器
function addRow(){
//		var newIndex = $("#dataTable").jqGrid('getRowData').length+1;
		newIndex++;
		$("#dataTable").jqGrid('addRowData',newIndex,{isAccount:'',enable:'',isNew:1},'first');
		editRow(newIndex);
}


function editRow(newIndex){
	var rowData=$("#dataTable").jqGrid('getRowData',newIndex);
	if(rowData.editing=="true"){//设置编辑状态
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#dataTable').jqGrid('setRowData', newIndex, rowData);
	jQuery("#dataTable").jqGrid('editRow',newIndex);//打开编辑行
	
	var isAccountValue=getEditor('dataTable',newIndex,"isAccount").fool("dhxCombo",{		
		value:rowData.isAccount,
		editable:false,
		valueField: 'value',
		textField: 'text',
		data:[
		      {
		    	  value: '1',
			      text: '是'
		      },{
		    	  value: '0',
			      text: '否'
		      }
		     ],onLoadSuccess:function(combo){		
				  combo.setComboValue(0);
				}	
	});
	isAccountValue.readonly(true);
	if(rowData.isAccount=='是'){
		isAccountValue.setComboValue("1");
	}else if(rowData.isAccount=='否'){
		isAccountValue.setComboValue("0");
	}
	var enableValue=getEditor('dataTable',newIndex,"enable").fool("dhxCombo",{
		value:rowData.enable,
		editable:false,
		valueField: 'value',
		textField: 'text',
		data:[
		      {
		    	  value: '1',
			      text: '是'
		      },{
		    	  value: '0',
			      text: '否'
		      }
		     ],
		     onLoadSuccess:function(combo){		
				  combo.setComboValue(0);
				}	
	});
	enableValue.readonly(true);
	if(rowData.enable=='是'){
		enableValue.setComboValue("1");
	}else if(rowData.enable=='否'){
		enableValue.setComboValue("0");
	}
}

//更新某行数据
function updateActions(index){
	$('#dataTable').datagrid('updateRow',{
		index: index,
		row:{}
	});
}

//获取某行序号
function getRowIndex(target){
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}

function editrow(newIndex){
	editRow(newIndex);
}

//保存某行
function saverow(index,id){
	$("#dataTable tr#"+index).form("enableValidation");
	if(!$("#dataTable tr#"+index).form("validate")){
		return false;
	}
	var rowData=$("#dataTable").jqGrid('getRowData',index);
	var fid=id;
	var name=getEditor("dataTable",index,"name").val();
	var code=getEditor("dataTable",index,"code").val();
	var describe=getEditor("dataTable",index,"describe").val();
	var isAccount=(getEditor("dataTable",index,"isAccount").next())[0].comboObj.getSelectedValue();
	if(isAccount=='是'){
		isAccount='1';
	}else if(isAccount=='否'){
		isAccount='0';
	}
	var enable=(getEditor("dataTable",index,"enable").next())[0].comboObj.getSelectedValue();
	if(enable=='是'){
		enable='1';
	}else if(enable=='否'){
		enable='0';
	}
	$.ajax({
		type:'post',
		url:getRootPath()+"/basedata/auxiliarytype/save",
		cache:false ,
		data:{fid:fid,code:code,name:name,describe:describe,isAccount:isAccount,enable:enable},
		dataType:'json',
		success:function(data){
			dataDispose(data);
			if(data.result == '0'){
				/*$.fool.alert({time:1000,msg:'保存成功！',fn:function(){*/
					$("#dataTable").jqGrid('saveRow',index);				
					$('#dataTable').jqGrid('setRowData',index,{fid:data.data.fid,code:code,name:name,describe:describe,isAccount:isAccount,enable:enable,editing:false,action:null});
				/*}});*/
			}else if(data.result = '1'){
				$.fool.alert({msg:data.msg});
			}else{
				$.fool.alert({msg:'服务器繁忙，请稍后再试'});
			}
		}
	});
}

//列表撤销
function cancelrow(index,value){
	if(value == '1'){
		$("#dataTable").jqGrid('delRowData',index);
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