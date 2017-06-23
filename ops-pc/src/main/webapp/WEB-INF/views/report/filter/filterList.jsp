<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
   <div id="tb">
   <fool:tagOpt optCode="filterAdd">
       <a href="javascript:;" class="easyui-linkbutton btn-blue4 btn-s" id="addFilter" >添加条件</a> 
   </fool:tagOpt>
   </div>
   <table id="datagrid"></table>
   <div id="pager"></div>
<script type="text/javascript">
var inputType = [
   		    {value:'0',text:'用户自定义输入'},
   		    {value:'1',text:'从代码管理中选择输入'},
   		    {value:'2',text:'从供应商选择输入'},
   		    {value:'3',text:'从客户选择输入'},
   		    {value:'4',text:'从供应商、客户选择输入'},
   		    {value:'5',text:'从人员资料选择输入'},
   	   	    {value:'6',text:'从货品资料选择输入'},
   		    {value:'7',text:'从货品属性选择输入'},
   		    {value:'8',text:'从现金银行选择输入'},
   		    {value:'9',text:'从日期控制选择输入'},
   		    {value:'10',text:'数字输入框输入'},
		    {value:'11',text:'从单据类型选择输入'},
		    {value:'12',text:'从用户资料类型选择输入'},
		    {value:'13',text:'从部门选择输入'},
		    {value:'14',text:'从单据状态选择输入'},
		    {value:'15',text:'凭证号'},
		    {value:'16',text:'固定资产卡片状态'},
		    {value:'17',text:'从凭证状态选择'},
		    {value:'18',text:'从科目选择输入'},
		    {value:'19',text:'从科目类型选择输入'},
		    {value:'20',text:'从凭证字选择输入'},
		    {value:'21',text:'是否显示'},
		    {value:'22',text:'从财务会计期间选择输入'},
		    {value:'23',text:'从货运地址选择输入'},
		    {value:'24',text:'从收支类型选择输入'},
		    {value:'25',text:'从资金计划状态选择输入'},
   		];
   		
var compare = [
        		    {value:'0',text:'包含'},
        		    {value:'1',text:'排除'},
        		    {value:'2',text:'＝'},
        		    {value:'3',text:'≠'},
        		    {value:'4',text:'＞'},
        		    {value:'5',text:'≥'},
        	   	    {value:'6',text:'＜'},
        		    {value:'7',text:'≤'},
        		    {value:'8',text:'like'},
        		];
        		
var yesNo = [
   		    {value:'false',text:'否'},
   		    {value:'true',text:'是'},
   		];
   		
var trueFalse = [
    		    {value:'0',text:'否'},
    		    {value:'1',text:'是'},
    		];

//初始化列表
$("#datagrid").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/reportFilter/queryFilter?sysReportId=${reportId}',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#datagrid")[0].addJSONData(data.responseJSON);
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
		total: "totalpages",  
	},  
	colModel:[
	          {name : 'fid',label : 'fid',hidden:true},
	          {name : 'isNew',label : 'isNew',hidden:true},
	          {name : 'editing',label : 'editing',hidden:true}, 
	          {name : 'tableName',label : '表名',align:'center',width:"100px",editable:true,edittype:"text"},
	          {name : 'fieldName',label : '字段名',align:'center',width:"100px",editable:true,edittype:"text"},
	          {name : 'displayName',label : '显示名称',align:'center',width:"100px",editable:true,edittype:"text"},
	          {name : 'aliasName',label : '显示名称',align:'center',width:"100px",editable:true,edittype:"text"},
              {name : 'inputType',label : '输入方式',align:'center',width:"100px",editable:true,edittype:"text",formatter:function(cellvalue, options, rowObject){
            	  for(var i=0; i<inputType.length; i++){
            		  if (inputType[i].value == cellvalue) return inputType[i].text;
            	  }
            	  return cellvalue;
              }},
              {name : 'mark',label : '标识',align:'center',width:"100px",editable:true,edittype:"text"},
              //区分快捷搜索和高级搜索
              {name : 'show',label : '是否显示',align:'center',width:"100px",editable:true,edittype:"text",formatter:function(cellvalue, options, rowObject){
            	  var str="";
            	  if(cellvalue==true){
            		  str="是";
            	  }else if(cellvalue==false){
            		  str="否";
            	  }else{
            		  str=cellvalue;
            	  }
            	  return str;
              }},
              {name : 'need',label : '是否必填',align:'center',width:"100px",editable:true,edittype:"text",formatter:function(cellvalue, options, rowObject){
            	  var str="";
            	  if(cellvalue==true){
            		  str="是";
            	  }else if(cellvalue==false){
            		  str="否";
            	  }else{
            		  str=cellvalue;
            	  }
            	  return str
              }},
              {name : 'order',label : '顺序号',align:'center',width:"100px",editable:true,edittype:"text"},
              <fool:tagOpt optCode="filterAction">
              {name:"action",label:"操作",align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
            	  if (rowObject.editing){
            		  var s='<a href="javascript:;" title="保存" class="btn-save" onclick="saverow(\''+options.rowId+'\',\''+rowObject.fid+'\')"></a>';
            		  var c='<a href="javascript:;" title="取消" class="btn-cancel" onclick="cancelrow(\''+options.rowId+'\',\''+rowObject.isNew+'\')"></a>';
            		  return s+" "+c;
            	  }else{
            		  var e='<a href="javascript:;" title="编辑" class="btn-edit" onclick="editrow(\''+options.rowId+'\')" ></a>';
            		  var d='<a href="javascript:;" title="删除" class="btn-del" onclick="deleterow(\''+options.rowId+'\',\''+rowObject.fid+'\')" ></a>';
            		  return e+" "+d;
            	  }
              }},
              </fool:tagOpt>
              ],
    onLoadSuccess:function(){//列表权限控制
    	<fool:tagOpt optCode="filterAction1">//</fool:tagOpt>$('.btn-edit').remove();
    	<fool:tagOpt optCode="filterAction2">//</fool:tagOpt>$('.btn-del').remove();
    },
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});  


//添加查询条件
$("#addFilter").click(function(){
	var newIndex = $("#datagrid").jqGrid('getRowData').length+1;
	$("#datagrid").jqGrid('addRowData',newIndex,{inputType:"",show:"",need:"",isNew:1},newIndex);
	editrow(newIndex);
});


//列表操作方法
function editrow(rowid){
	var rowData=$("#datagrid").jqGrid('getRowData',rowid);
	if(rowData.editing=="true"){
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#datagrid').jqGrid('setRowData', rowid, rowData);
	$("#datagrid").jqGrid('editRow',rowid);
	getEditor("datagrid",rowid,"tableName").textbox({
		required:true,
		missingMessage:"该项为必输项"
	});
	getEditor("datagrid",rowid,"fieldName").textbox({
		required:true,
		missingMessage:"该项为必输项"
	});
	getEditor("datagrid",rowid,"displayName").textbox({
		required:true,
		missingMessage:"该项为必输项"
	});
	getEditor("datagrid",rowid,"aliasName").textbox({
		required:true,
		missingMessage:"该项为必输项"
	});
	/* getEditor("datagrid",rowid,"inputType").combobox({
		required:true,
		editable:false,
		data:inputType,
		onSelect:function(record){
			if(record.value==1){
				getEditor("datagrid",rowid,"mark").textbox("enableValidation");
			}else{
				getEditor("datagrid",rowid,"mark").textbox("disableValidation");
			}
		}
	}); */
	getEditor("datagrid",rowid,"mark").textbox({
		required:true,
		novalidate:true,
		missingMessage:"该项为必输项"
	});
	getEditor("datagrid",rowid,"inputType").fool("dhxCombo",{
		width:91,
		height:22,
		required:true,
		editable:false,
		data:inputType,
		value:rowData.inputType,
		onSelectionChange:function(){
			var value=(getEditor("datagrid",rowid,"inputType").next())[0].comboObj.getSelectedValue();
			if(value==1){
				getEditor("datagrid",rowid,"mark").textbox("enableValidation");
			}else{
				getEditor("datagrid",rowid,"mark").textbox("disableValidation");
			}
		}
	});
	if(rowData.inputType){
		(getEditor("datagrid",rowid,"inputType").next())[0].comboObj.setComboValue(rowData.inputType);
	}
	/* getEditor("datagrid",rowid,"inputType").combobox() */
	for(var i=0;i<inputType.length;i++){
		if(rowData.inputType==inputType[i].text){
			(getEditor("datagrid",rowid,"inputType").next())[0].comboObj.setComboValue(inputType[i].value);
			break;
		}
	}
	/* getEditor("datagrid",rowid,"show").combobox({
		required:true,
		editable:false,
		data:trueFalse,
	}); */
	getEditor("datagrid",rowid,"show").fool("dhxCombo",{
		width:91,
		height:22,
		required:true,
		editable:false,
		data:trueFalse,
	});
	if(rowData.show){
		(getEditor("datagrid",rowid,"show").next())[0].comboObj.setComboValue(rowData.show);
	}
	for(var i=0;i<trueFalse.length;i++){
		if(rowData.show==trueFalse[i].text){
			(getEditor("datagrid",rowid,"show").next())[0].comboObj.setComboValue(trueFalse[i].value);
			break;
		}
	}
	/* getEditor("datagrid",rowid,"need").combobox({
		required:true,
		editable:false,
		data:yesNo,
	}); */
	getEditor("datagrid",rowid,"need").fool("dhxCombo",{
		width:91,
		height:22,
		required:true,
		editable:false,
		data:yesNo,
	});
	if(rowData.need){
		(getEditor("datagrid",rowid,"need").next())[0].comboObj.setComboValue(rowData.need);
	}
	for(var i=0;i<yesNo.length;i++){
		if(rowData.need==yesNo[i].text){
			(getEditor("datagrid",rowid,"need").next())[0].comboObj.setComboValue(yesNo[i].value);
			break;
		}
	}
	getEditor("datagrid",rowid,"order").numberbox({
		required:true
	});
}

function deleterow(index,id){
	$.fool.confirm({
		msg:'确定删除这条记录吗?',
	    fn:function(r){
	    	if (r){
	    		$("#datagrid").jqGrid('delRowData',index);
		        $.post("${ctx}/reportFilter/deleteFilter",{fid:id});
	    	}
	    }
	});
}
function saverow(index,id){
	var rowData=$("#datagrid").jqGrid('getRowData',index);
	var row=$("#datagrid").find("tr[id="+index+"]");
	/* row.form('enableValidation'); */
	if(row.form('validate')){
		var tableName=getEditor("datagrid",index,"tableName").textbox("getValue");
		var fieldName=getEditor("datagrid",index,"fieldName").textbox("getValue");
		var displayName=getEditor("datagrid",index,"displayName").textbox("getValue");
		var aliasName=getEditor("datagrid",index,"aliasName").textbox("getValue");
		var inputType=(getEditor("datagrid",index,"inputType").next())[0].comboObj.getSelectedValue();
		var mark=getEditor("datagrid",index,"mark").textbox("getValue");
		var show=(getEditor("datagrid",index,"show").next())[0].comboObj.getSelectedValue();
		var need=(getEditor("datagrid",index,"need").next())[0].comboObj.getSelectedValue();
		var order=getEditor("datagrid",index,"order").numberbox("getValue");
		$.post("${ctx}/reportFilter/saveFilter",{sysReportId:'${reportId}',fid:id,tableName:tableName,fieldName:fieldName,aliasName:aliasName,displayName:displayName,inputType:inputType,mark:mark,order:order,need:need,show:show},function(data){
			if(data.returnCode=="0"){
				 $.fool.alert({msg:'保存成功！',fn:function(){
					 $('#datagrid').trigger("reloadGrid");
				 }});
			}else if(data.returnCode=="1"){
				$.fool.alert({msg:data.message,fn:function(){
					$('#datagrid').trigger("reloadGrid");
				}});
			}else{
				$.fool.alert({msg:"系统正忙，请稍后再试。"});
			}
		});
	}
}
function cancelrow(index,value){
	if(value == '1'){
		$("#datagrid").jqGrid('delRowData',index);
	}else{
		$('#datagrid').jqGrid('setRowData', index, {editing:false,action:null});
		$("#datagrid").jqGrid('restoreRow', index);
	}
		
}
//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}
</script>
</body>
