<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
   <div id="tb">
       <a href="javascript:;" class="easyui-linkbutton btn-blue4 btn-s" id="addFilter" style="display: none">添加SQL</a> 
   </div>
   <table id="datagrid"></table>
   <div id="pager"></div>
   <div id="textbox"><textarea id="text"></textarea><br/><input class="btn-blue4 btn-s" type="button" id="text-save" value="保存"/></div>
<script type="text/javascript">
var editIndex="";
$("#textbox").window({
	title:"输入SQL语句",
	collapsible:false,
	minimizable:false,
	maximizable:false,
	closed:true,
	top:120,
	width:650,
});
//初始化列表
$("#datagrid").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/report/sql/list?sysReportId=${reportId}',
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
	          {name : 'sql',label : 'SQL语句',align:'center',height:"30px",width:"100px",editable:true,edittype:"text"},
	          <fool:tagOpt optCode="sqlAction">
              {name:"action",label:"操作",align:'center',width:"5px",formatter:function(cellvalue, options, rowObject){
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
    onLoadSuccess:function(data){
    	if($("#datagrid").jqGrid('getRowData',rowid).length==0){
    		$("#addFilter").show();
    	}else{
    		$("#addFilter").hide();
    	}
    },
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

//添加查询语句
$("#addFilter").click(function(){
	var newIndex = $("#datagrid").jqGrid('getRowData').length+1;
	$("#datagrid").jqGrid('addRowData',newIndex,{sql:"",isNew:1},newIndex);
	editrow(newIndex);
});
$("#text-save").click(function(){
		var editor=getEditor("datagrid",editIndex,"sql");
		editor.combobox('setValue',$("#text").val());
		$("#textbox").window("close");
});

//列表操作方法
function editrow(rowid){
		var rowData=$("#datagrid").jqGrid('getRowData',rowid);
		var textVal = rowData.sql;
		if(rowData.editing=="true"){
			return;
		}
		rowData.editing=true;
		rowData.action=null;
		$('#datagrid').jqGrid('setRowData', rowid, rowData);
		$("#datagrid").jqGrid('editRow',rowid);
		getEditor("datagrid",rowid,"sql").combobox({
			hasDownArrow:false,
			editable:false,
			required:true,
			onShowPanel:function(){
				$(this).combobox('hidePanel');
				$("#text").val(textVal);
				editIndex=rowid;
				$("#textbox").window('open');
			}
		});
}
function deleterow(index,id){
	$.fool.confirm({
		msg:'确定删除这条记录吗?',
	    fn:function(r){
	    	if (r){
		        $.post("${ctx}/report/sql/delete",{id:id},function(data){
		        	if(data.returnCode=="0"){
						 $.fool.alert({msg:'删除成功！',fn:function(){
							 $("#datagrid").jqGrid('delRowData',index);
						  }});
					}else if(data.returnCode=="1"){
						$.fool.alert({msg:data.message,fn:function(){
						  }});
					}else{
						$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
						  }});
					}
		        });
	    	}
	    }
	});
}
function saverow(index,id){
	var rowData=$("#datagrid").jqGrid('getRowData',index);
	var row=$("#datagrid").find("tr[id="+index+"]");
	/* row.form('enableValidation'); */
	if(row.form('validate')){
		var sql=getEditor("datagrid",index,"sql").combobox("getValue");
		var reportId="${reportId}";
		$.post("${ctx}/report/sql/save",{fid:id,sql:sql,sysReportId:reportId},function(data){
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
