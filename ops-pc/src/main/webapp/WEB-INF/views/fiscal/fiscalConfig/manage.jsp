<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="xsbjd" scope="page"></c:set>
<c:set var="billCodeName" value="销售报价单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>财务参数设置</h1>
                </div>             
    </div>
	
	<table id="dataTable"></table>
	<div id="pager"></div>

<div id="edit-window"></div>
<script>
var options = [
               {value:'1',text:'启用'},
               {value:'0',text:'禁止'},
    		];
$("#dataTable").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/fiscalConfig/list',
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
	height:"100%",
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages"
	},  
	colModel:[ 
	            {name : 'isNew',label : 'isNew',hidden:true},
	            {name : 'editing',label : 'editing',hidden:true}, 
                {name : 'updateTime',label : 'updateTime',hidden:true}, 
                {name : 'fid',label : 'fid',hidden:true}, 
                {name : 'code',label : '编号',hidden:true},
                {name : 'valueType',label : 'valueType',hidden:true},
                {name : 'name',label : '项目',align:'center',width:"100px"}, 
                {name : 'describe',label : '描述',align:'center',width:"100px",editable:true,edittype:"text"},
                {name : 'value',label : '启用、禁止',align:'center',width:"100px",editable:true,edittype:"text",formatter:function(cellvalue, options, rowObject){
                	var str="";
                	if(cellvalue == '1'){
                		str='启用';
                	}else if(cellvalue == '0'){
                		str='禁用';
                	}else{
                		str=cellvalue;
                	}
                	return str;
                }}, 
                <fool:tagOpt optCode="fconfigAction">
				{name:'action',label:'操作',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					if (rowObject.editing){
						var s='<a href="javascript:;" title="保存" class="btn-save" onclick="saveRow(\''+options.rowId+'\',\''+rowObject.fid+'\')"></a>';
						var c='<a href="javascript:;" title="取消" class="btn-cancel" onclick="cancelRow(\''+options.rowId+'\',\''+rowObject.isNew+'\')"></a>';
						return s+" "+c;
					}else{
						return "<a href='javascript:;' class='btn-edit' onclick='editRow(\""+options.rowId+"\")' title='编辑'></a>";;
					}
				}}
				</fool:tagOpt>
              ],
    onLoadSuccess:function(){//列表权限控制
    	<fool:tagOpt optCode="fconfigAction1">//</fool:tagOpt>$('.btn-edit').remove();
    },
    /* onSelectRow:function(rowid){
    	editRow(rowid);
    } */
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function editRow(rowid){
	var rowData=$("#dataTable").jqGrid('getRowData',rowid);
	if(rowData.editing=="true"){
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#dataTable').jqGrid('setRowData', rowid, rowData);
	$("#dataTable").jqGrid('editRow',rowid);
	getEditor("dataTable",rowid,"describe").validatebox({
		required:true,
		novalidate:true,
	});
	var valueCombo=getEditor("dataTable",rowid,"value").fool("dhxCombo",{
		height:"24px",
		width:"264px",
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		required:true,
		focusShow:true,
		data:[
		      {
		    	  value: '1',
			      text: '启用'
		      },{
		    	  value: '0',
		    	  text: '禁用'
		      }
		     ],
		onLoadSuccess:function(){
		 	(getEditor("dataTable",rowid,"value").next())[0].comboObj.deleteOption("")
		},
	});
	if(rowData.value=='启用'){
		valueCombo.setComboValue('1');
	}else if(rowData.value=='禁用'){
		valueCombo.setComboValue('0');
	}
}
function saveRow(index,fid){
	var rowData=$("#dataTable").jqGrid('getRowData',index);
	var describe=getEditor("dataTable",index,"describe").val();
	var value = (getEditor("dataTable",index,"value").next())[0].comboObj.getSelectedValue();
	var myform = $("#dataTable").find("tr[id="+index+"]");
	myform.form("enableValidation");
	if(myform.form('validate')){
		$.post("${ctx}/fiscalConfig/save",{fid:fid,describe:describe,value:value,code:rowData.code,name:rowData.name,valueType:rowData.valueType,updateTime:rowData.updateTime},function(data){
			dataDispose(data);
			if(data.result=="0"){
				 /* var myDate = (new Date()).format('yyyy-MM-dd hh:mm:ss');
				 $.fool.alert({msg:'保存成功！',fn:function(){
					 $('#dataTable').jqGrid('setRowData', index, {
	  						action:null,
	  	  					editing:false,
	  	  					updateTime:myDate,
	  	  				    describe:describe,
	  	  				    value:value
					 });
					 $("#dataTable").jqGrid('saveRow',index);
				  }}); */
				$("#dataTable").trigger("reloadGrid");
			}else if(data.result=="1"){
				$.fool.alert({msg:data.msg,fn:function(){
				}});
			}else{
				$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
				}});
			}
		});
	}
}
function cancelRow(rowid,isNew){
	    if(isNew==1){
	    	$("#dataTable").jqGrid('delRowData',rowid);
	    }else{
	    	var rowData=$("#dataTable").jqGrid('getRowData',rowid);
			rowData.action=null;
			rowData.editing=false;
			$('#dataTable').jqGrid('setRowData', rowid, rowData);
			$("#dataTable").jqGrid('restoreRow', rowid);
	    }
}
//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}
</script>
</body>
</html>

