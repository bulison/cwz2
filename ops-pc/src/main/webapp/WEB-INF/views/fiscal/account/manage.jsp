<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>财务账套</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<style>
.gridBut{
	border:0px !important;
}
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">财务初始化</a></li>
	        <li><a href="javascript:;" class="curr">财务账套</a></li>
	    </ul>
	</div>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>财务账套</h1>
                </div>             
    </div>
	<div style="margin: 10px 0px;"> 
	<fool:tagOpt optCode="accountAdd"><a href="javascript:;" id="add" class="btn-ora-add" >新增</a></fool:tagOpt>
	</div>
	<table id="accountList"></table>
	<div id="pager"></div>
	<div id="addBox"></div> 
<script type="text/javascript">
//删除按钮点击事件
function removeById(index,fid){ 
	 $.fool.confirm({title:'确认',msg:'删除数据前请做好备份。确认删除此数据吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/fiscalAccount/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.msg,fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

//默认按钮点击事件
function defaultById(fid){
	$.fool.confirm({title:'确认',msg:'要设为默认登录吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '',
					data : {id : fid},
					dataType : 'json',
					success : function(data) { 
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'设置成功！',fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

//备份按钮点击事件
function backup(fid){
	$.fool.confirm({title:'确认',msg:'备份此数据？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '',
					data : {id : fid},
					dataType : 'json',
					success : function(data) { 
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'备份成功！',fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
};

//恢复按钮点击事件
function restore(fid){
	$.fool.confirm({title:'确认',msg:'恢复此数据？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '',
					data : {id : fid},
					dataType : 'json',
					success : function(data) { 
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'恢复成功！',fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
};

//恢复按钮点击事件
function initialize(fid){
	$.fool.confirm({title:'确认',msg:'初始化会删除账套下所有的凭证信息，请先进行数据备份。确认要初始化数据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '',
					data : {id : fid},
					dataType : 'json',
					success : function(data) { 
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'初始化成功！',fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}
					},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
			  });
		 }
	 }});
};

//设为默认按钮点击事件
function defaultById(fid){
	$.fool.confirm({title:'确认',msg:'确定要设为默认账套吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/fiscalAccount/setDefaultLogin',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) { 
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'设置成功！',fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.msg,fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}
					},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
			  });
		 }
	 }});
};

//复制账套
function copyById(fid){
	$.fool.confirm({title:'确定',msg:'确定要复制账套吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/fiscalAccount/copy',
					data : {accId:fid}, 
					dataType : 'json',
					beforeSend:function(){
						$.messager.progress({
							text:'处理中，请稍后...'
						});
					},
					success : function(data) {	
						dataDispose(data);
						$.messager.progress('close');
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'复制成功！',fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}else if(data.result == "1"){
							$.fool.alert({msg:data.msg});
						}else{
							$.fool.alert({msg:"服务器繁忙,请稍后再试"});
						}
					},
					error : function(data){
						$.messager.progress('close');
						$.fool.alert({msg:"服务器繁忙,请稍后再试"});
					}
		 });
		 }
	}});
}

//是否默认登录
var isDefault = [
             {value:0,text:"否"},
             {value:1,text:"是"}
             ];
             
var yesNo = [
             {value:0,text:"否"},
             {value:1,text:"是"}
    		];
    		
var enable = [
             {value:0,text:"无效"},
             {value:1,text:"有效"}
    		];

//新增、修改、详情窗口初始化
$('#addBox').window({
	top:50,  
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true,
	modal:true,
	width:400,
});

$("#accountList").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/fiscalAccount/list',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#accountList")[0].addJSONData(data.responseJSON);
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
		            {name : 'isNew',label : 'isNew',hidden:true},
		            {name : 'editing',label : 'editing',hidden:true}, 
                    {name : 'updateTime',label : 'updateTime',hidden:true}, 
                    {name : 'fid',label : 'fid',hidden:true}, 
                    {name : 'name',label : '账套名',align:'center',editable:true,edittype:"text"}, 
                    {name : 'defaultFlag',label : '是否默认登录',align:'center',width:"100px",formatter:function(value){
                    	var str="";
                    	if(value == '1'){
                    		str='是';
                    	}else if(value == '0'){
                    		str='否';
                    	}else{
                    		str=value;
                    	}
                    	return str;
                    }}, 
                    {name : 'enable',label : '可修改数据',hidden:true,align:'center',editable:true,edittype:"text",formatter:function(value){
                    	var str="";
                    	if(value == 1){
                    		str='是';
                    	}else if(value == 0){
                    		str='否';
                    	}else{
                    		str=value;
                    	}
                    	return str;
                    }}, 
                    {name : 'description',label : '描述',align:'center',editable:true,edittype:"text"},
                    <fool:tagOpt optCode="accountAction">
                    {name:"action",label:"操作",align:'center',formatter:function(cellvalue, options, rowObject){
                    	var e='<a class="btn-edit" title="编辑" href="javascript:;" onclick="editRow(\''+options.rowId+'\')"></a> ';
        	  			var m='<a class="btn-initialize" title="设为默认" href="javascript:;" onclick="defaultById(\''+rowObject.fid+'\')"></a> ';
        	  			var d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+options.rowId+'\',\''+rowObject.fid+'\')"></a> ';
        	  			var b='<a class="btn-backup" title="备份" href="javascript:;" onclick="backup(\''+rowObject.fid+'\')"></a> ';
        	  			var r='<a class="btn-recovery" title="恢复" href="javascript:;" onclick="restore(\''+rowObject.fid+'\')"></a> ';
        	  			var i='<a class="btn-refresh" title="初始化" href="javascript:;" onclick="initialize(\''+rowObject.fid+'\')"></a> ';
        	  			var copy = '' /* '<a class="btn-copy" title="复制账套" href="javascript:;" onclick="copyById(\''+rowObject.fid+'\')"></a>'; */
        	  			if (rowObject.editing){
                			var s='<input type="button" href="javascript:;" title="保存" class="btn-save gridBut" onclick="saveRow(\''+options.rowId+'\',\''+rowObject.fid+'\')" />';
                			var c='<input type="button" href="javascript:;" title="取消" class="btn-cancel gridBut" onclick="cancelRow(\''+options.rowId+'\',\''+rowObject.isNew+'\')" />';
                			return s+" "+c;
        	  			}else{
        	  				return e+""+m+""+copy;
        	  			}
		            }},
		            </fool:tagOpt>
                  ],
        /* onSelectRow:function(rowid){
        	editRow(rowid);
        }, */ 
        onLoadSuccess:function(){//列表权限控制
	  		<fool:tagOpt optCode="accountAction1">//</fool:tagOpt>$('.btn-edit').remove();
	  		<fool:tagOpt optCode="accountAction2">//</fool:tagOpt>$('.btn-del').remove();
	  		<fool:tagOpt optCode="accountAction3">//</fool:tagOpt>$('.btn-backup').remove();
	  		<fool:tagOpt optCode="accountAction4">//</fool:tagOpt>$('.btn-recovery').remove();
	  		<fool:tagOpt optCode="accountAction5">//</fool:tagOpt>$('.btn-refresh').remove();
	  		<fool:tagOpt optCode="accountAction6">//</fool:tagOpt>$('.btn-initialize').remove();
	  	},
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});  

//添加查询条件
$("#add").click(function(){
	var newIndex = $("#accountList").jqGrid('getRowData').length+1;
	$("#accountList").jqGrid('addRowData',newIndex,{isNew:1,defaultFlag:0,enable:0},newIndex);
	editRow(newIndex);
});

function editRow(rowid){
	var rowData=$("#accountList").jqGrid('getRowData',rowid);
	if(rowData.editing=="true"){
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#accountList').jqGrid('setRowData', rowid, rowData);
	$("#accountList").jqGrid('editRow',rowid);
	getEditor("accountList",rowid,"name").validatebox({
		required:true,
		novalidate:true,
	});
	getEditor("accountList",rowid,"description").validatebox({
	});
	var enableCombo=getEditor("accountList",rowid,"enable").fool("dhxCombo",{
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
			      text: '是'
		      },{
		    	  value: '0',
		    	  text: '否'
		      }
		     ],
		onLoadSuccess:function(){
		 	(getEditor("accountList",rowid,"enable").next())[0].comboObj.deleteOption("")
		},
	});
	if(rowData.enable=='是'){
		enableCombo.setComboValue('1');
	}else if(rowData.enable=='否'){
		enableCombo.setComboValue('0');
	}
}
function saveRow(index,id){
	var rowData=$("#accountList").jqGrid('getRowData',index);
	var a =getEditor("accountList",index,"name");
	var b =getEditor("accountList",index,"enable");
	var c =getEditor("accountList",index,"description");
	$(a[0]).closest("tr").form("enableValidation")
	if($(a[0]).closest("tr").form("validate")){
		$("#accountList #"+index+" .btn-save").attr("disabled","disabled");
		var name=$(a[0]).val();
		var enable=($(b[0]).next())[0].comboObj.getSelectedValue();
		var description=$(c[0]).val();
		$.post("${ctx}/fiscalAccount/save",{fid:rowData.fid,name:name,enable:enable,description:description},function(data){
			dataDispose(data);
			if(data.result=="0"){
  				 $.fool.alert({time:1000,msg:'保存成功！',fn:function(){
                     $("#accountList").jqGrid('restoreRow', index);
                     $('#accountList').jqGrid('setRowData', index,{
                         isNew:0,
						 action:null,
                         editing:false,
                         name:name,
                         description:description
					 })
 						$("#accountList").trigger("reloadGrid");
  				 }});
  			}else if(data.result=="1"){
  				$.fool.alert({msg:data.msg,fn:function(){
  					$("#accountList #"+index+" .btn-save").removeAttr("disabled");
  				  }});
  			}else{
  				$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
  					$("#accountList #"+index+" .btn-save").removeAttr("disabled");
  				}});
  			}
  		});
	}
}
function cancelRow(index,isNew){
	if(isNew == '1'){
		$("#accountList").jqGrid('delRowData',index);
	}else{
		var rowData=$("#accountList").jqGrid('getRowData',index);
		rowData.action=null;
		rowData.editing=false;
		$('#accountList').jqGrid('setRowData', index, rowData);
		$("#accountList").jqGrid('restoreRow', index);
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
