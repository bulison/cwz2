<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>导入导出配置</title>
<style>
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>导入导出配置</h1>
                </div>             
             </div>
   <%-- <div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">系统维护</a></li>
	        <li><a href="javascript:;" class="curr">导入导出配置</a></li>
	    </ul>
	</div> --%>
	<div style="margin: 10px 0px;">
	<fool:tagOpt optCode="eximportAdd"><a href="javascript:;" id="add" class="btn-ora-add" >新增</a></fool:tagOpt>
	</div>
   <table id="data"></table>
   <div id="pager"></div>
   
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript">
var trueFalse = [
                  {value:'1',text:'是'},
                  {value:'0',text:'否'},
        		];

$('#data').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/ExcelMapController/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#data")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,//自动填满宽度
	height:"100%",
	mtype:"post",
	pager:'#pager',
	rowNum:10,
	rowList:[ 10, 20, 30 ],
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	viewrecords:true,
	forceFit:true,//调整列宽度，表格总宽度不会改变
	height:$(window).outerHeight()-200+"px",
	colModel:[
	          {name:'fid',label:'fid',hidden:true,width:100,editable:true,edittype:"text"},
	          {name:'clazz',label:'类名',align:"center",sortable:true,editable:true,edittype:"text",width:350},
	          {name:'isNew',label:'类名',align:"center",hidden:true,editable:true,edittype:"text",width:350},
	          {name:'field',label:'属性',align:"center",sortable:true,editable:true,edittype:"text",width:100},
	          {name:'cnName',label:'中文名称',align:"center",sortable:true,editable:true,edittype:"text",width:100},
	          {name:'sequence',label:'序号',align:"center",sortable:true,editable:true,edittype:"text",width:100},
	          {name:'type',label:'类型',align:"center",sortable:true,editable:true,edittype:"text",width:100},
	          {name:'fimport',label:'是否应用到导入',align:"center",sortable:false,editable:true,edittype:"text"/* editor:{type:"combobox",options:{required:true,data:trueFalse}} */,width:100,formatter:function(value){
					for(var i=0; i<trueFalse.length; i++){
						if (trueFalse[i].value == value) return trueFalse[i].text;
					}
					return value;
	          },unformat:function(value,options){
	        	  if(value == "是")
						return "1";
		        	  else if(value == "否")
		        		  return "0";
		          }},
	          {name:'fexport',label:'是否应用到导出',align:"center",sortable:false,editable:true,edittype:"text"/* editor:{type:"combobox",options:{required:true,data:trueFalse}} */,width:100,formatter:function(value){
					for(var i=0; i<trueFalse.length; i++){
						if (trueFalse[i].value == value) return trueFalse[i].text;
					}
					return value;
	          },unformat:function(value,options){
	        	  if(value == "是")
						return "1";
		        	  else if(value == "否")
		        		  return "0";
		          }},
	          {name:'processor',label:'处理器类全名',align:"center",sortable:true,editable:true,edittype:"text",width:100},
	          {name:'need',label:'导入时是否必填',align:"center",sortable:false,editable:true,edittype:"text"/* editor:{type:"combobox",options:{required:true,data:trueFalse}} */,width:100,formatter:function(value){
					for(var i=0; i<trueFalse.length; i++){
						if (trueFalse[i].value == value) return trueFalse[i].text;
					}
					return value;
	          },unformat:function(value,options){
	        	  if(value == "是")
					return "1";
	        	  else if(value == "否")
	        		  return "0";
	          }},
	          {name:'validation',label:'是否有效',align:"center",sortable:false,editable:true,edittype:"text"/* editor:{type:"combobox",options:{required:true,data:trueFalse}} */,width:100,formatter:function(value){
					for(var i=0; i<trueFalse.length; i++){
						if (trueFalse[i].value == value) return trueFalse[i].text;
					}
					return value;
	          },unformat:function(value,options){
	        	  if(value == "是")
						return "1";
		        	  else if(value == "否")
		        		  return "0";
		          }},
	          <fool:tagOpt optCode="eximportAction">
	          {name:'action',label:'操作',align:"center",sortable:false,width:70,formatter:function(value,options,row){
	        	  if (row.editing){
	        		  var s='<a href="javascript:;" title="保存" class="btn-save" onclick="saverow(this)"></a>';
	        		  var c='<a href="javascript:;" title="取消" class="btn-cancel" onclick="cancelrow(this)"></a>';
	        		  return s+" "+c;
	        		  }else{
	        			  var e='<a href="javascript:;" title="编辑" class="btn-edit" onclick="editrow(this)" ></a>';
	        			  var d='<a href="javascript:;" title="删除" class="btn-del" onclick="deleterow(this)" ></a>';
	        			  return e+" "+d;
	        			  }
	        	  }}
	          </fool:tagOpt>
	          ],
	          /* onBeforeEdit:function(index,row){
	      		row.editing = true;
	      		updateActions(index);
	          },
	          onAfterEdit:function(index,row){
	        	  row.editing = false;
	        	  updateActions(index);
	        	  $.post("${ctx}/ExcelMapController/save",{fid:row.fid,clazz:row.clazz,field:row.field,cnName:row.cnName,sequence:row.sequence,type:row.type,fimport:row.fimport,fexport:row.fexport,processor:row.processor,need:row.need,validation:row.validation},function(data){
	        		  if(data.result=="0"){
	        			  $.fool.alert({msg:'保存成功！',fn:function(){
	        				  $('#data').datagrid('reload');
	        			  }});
	        		  }else if(data.result=="1"){
	        				  $.fool.alert({msg:data.msg,fn:function(){
	        					  $('#data').datagrid('reload');
	        				  }});
	        				  }else{
	        					  $.fool.alert({msg:"系统正忙，请稍后再试。"});
	        				  }
	        	  });
	          },
	        	  onCancelEdit:function(index,row){
	        		  row.editing = false;
	        		  updateActions(index);
	        	  }, */
	        	  gridComplete:function(){//列表权限控制
	        			<fool:tagOpt optCode="eximportAction1">//</fool:tagOpt>$('.btn-edit').remove();
	        			<fool:tagOpt optCode="eximportAction2">//</fool:tagOpt>$('.btn-del').remove();
	        		}
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});


//添加查询条件
$("#add").click(function(){
	var index = $('#data').jqGrid('getRowData').length+1;
	$('#data').jqGrid("addRowData",index,{
		isNew:1
	},"first");
	var target = $('#data #'+index).find("td[aria-describedby=data_action] a.btn-edit");
	editrow(target);
});


/* //列表操作方法
function updateActions(index){
	$('#data').datagrid('updateRow',{
		index: index,
		row:{}
	});
} */
function getRowIndex(target){
		var tr = $(target).closest('tr.jqgrow');
		return parseInt(tr.attr('id'));
}
function editrow(target){
	var index = getRowIndex(target);
	var row = $('#data').jqGrid('getRowData',index);
	$('#data').jqGrid('editRow', index);
	$('#data').jqGrid('setRowData', index, {editing:true,action:null});//编辑状态转换，按钮变化
	getEditor("clazz",index).validatebox({width:"100%",height:"80%",required:true});
	getEditor("field",index).validatebox({width:"100%",height:"80%",required:true});
	getEditor("cnName",index).validatebox({width:"100%",height:"80%",required:true});
	getEditor("sequence",index).validatebox({width:"100%",height:"80%",required:true});
	getEditor("type",index).validatebox({width:"100%",height:"80%",required:true});
	getEditor("fimport",index).fool("dhxCombo",{
		required:true,
		data:trueFalse,
		editable:false,
		focusShow:true,
		width:"100%",
		height:"80%",
		onLoadSuccess:function(combo){
			combo.setComboValue(row.fimport);
		}
	});
	getEditor("fexport",index).fool("dhxCombo",{
		required:true,
		data:trueFalse,
		editable:false,
		focusShow:true,
		width:"100%",
		height:"80%",
		onLoadSuccess:function(combo){
			combo.setComboValue(row.fexport);
		}
	});
	getEditor("need",index).fool("dhxCombo",{
		required:true,
		data:trueFalse,
		editable:false,
		focusShow:true,
		width:"100%",
		height:"80%",
		onLoadSuccess:function(combo){
			combo.setComboValue(row.need);
		}
	});
	getEditor("validation",index).fool("dhxCombo",{
		required:true,
		data:trueFalse,
		editable:false,
		focusShow:true,
		width:"100%",
		height:"80%",
		onLoadSuccess:function(combo){
			combo.setComboValue(row.validation);
		}
	});
	
}
function deleterow(target){
	var index = getRowIndex(target);
	$.fool.confirm({
		msg:'确定删除这条记录吗?',
	    fn:function(r){
	    	if (r){
	    		var row=$("#data").jqGrid("getRowData",index);
		        $.post("${ctx}/ExcelMapController/delete",{fid:row.fid},function(data){
		        	dataDispose(data);
		        	if(data.returnCode=="0"){
			  			$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
			  				$('#data').jqGrid('delRowData', index);
			  			}});
			  		}else if(data.returnCode=="1"){
			  			$.fool.alert({msg:data.message});
			  		}else{
			  			$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
			  		}
		        });
	    	}
	    }
	});
}
function saverow(target){
	var index = getRowIndex(target);
	$('#data').find('#'+index).form("enableValidation");
	if(!$('#data').find('#'+index).form("validate")){
		return false;
	}
	var fid = getEditor("fid",index).val();
	var clazz = getEditor("clazz",index).val();
	var field = getEditor("field",index).val();
	var cnName = getEditor("cnName",index).val();
	var sequence = getEditor("sequence",index).val();
	var type = getEditor("type",index).val();
	var processor = getEditor("processor",index).val();
	var fimport = getEditor("fimport",index).next()[0].comboObj.getSelectedValue();
	var fexport = getEditor("fexport",index).next()[0].comboObj.getSelectedValue();
	var need = getEditor("need",index).next()[0].comboObj.getSelectedValue();
	var validation = getEditor("validation",index).next()[0].comboObj.getSelectedValue();
		$.post("${ctx}/ExcelMapController/save",{fid:fid,clazz:clazz,field:field,cnName:cnName,sequence:sequence,type:type,fimport:fimport,fexport:fexport,processor:processor,need:need,validation:validation},function(data){
			dataDispose(data);
			if(data.result=="0"){
  			    $('#data').jqGrid('saveRow', index);
  			    $('#data').jqGrid('setRowData',index,{
  			    	fid:data.data.fid,
  			    	isNew:0,
  			    	fimport:fimport,
  			    	fexport:fexport,
  			    	need:need,
				    validation:validation,
				    editing:false,
				    action:null
  			    });
  			    $.fool.alert({msg:'保存成功！'});
			}else if(data.result=="1"){
				$.fool.alert({msg:data.msg,fn:function(){
				}});
			}else{
				$.fool.alert({msg:"系统正忙，请稍后再试。"});
			}
  	  });
}
function cancelrow(target){
	var index = getRowIndex(target);
	var isNew = getEditor("isNew",index).val();
	    if(isNew != 1){
	    	$('#data').jqGrid('restoreRow', index);
	    	$('#data').jqGrid('setRowData', index, {editing:false,action:null});
	    }else{
	    	$('#data').jqGrid('delRowData', index);
	    }
}

function getEditor(name,rowId){
	return $('#data').find('#'+rowId+"_"+name);
}
</script>
</body>
</html>
