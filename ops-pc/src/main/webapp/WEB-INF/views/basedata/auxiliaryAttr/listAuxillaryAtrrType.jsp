<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>辅助属性类型</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>辅助属性类型</h1>
                </div>             
             </div>

    <div class="toolBox">
		<div class="toolBox-button" style="margin-right:5px;">
	        <fool:tagOpt optCode="atypeAdd"><a href="javascript:;" title="" id="auxillaryAtrrTypeEdit" class="btn-ora-add" onclick="addRow()">添加</a></fool:tagOpt>
	        <fool:tagOpt optCode="atypeAdd"><a href="javascript:;" id="refresh" onclick="javascript:refresh();" class="btn-ora-refresh">刷新</a></fool:tagOpt>
		</div>
	</div>
	<table id="dataTable"></table>
	 <div id="pager"></div>
	<div id="my-window"></div>
	<script type="text/javascript" src="${ctx}/resources/js/basedata/auxiliaryAttr/auxillaryAtrrType.js?v=${js_v}"></script>
	<script>
	var options=[{id:"1",name:"是"},{id:"0",name:"否"}];
	var newIndex = 0;
	/* $('#dataTable').datagrid({
		width:'100%',
		pagination:true,
		remoteSort:false,
		singleSelect:true,
		selectOnCheck:true,
		fitColumns:true,
		url:'${ctx}/basedata/auxiliarytype/list',
		columns:[[
		          {field:'fid',title:'fid',hidden:true},
		          {field:'code',title:'编号',width:100,editor:{type:"textbox",options:{}}},
		          {field:'name',title:'名称',width:100,editor:{type:"textbox",options:{}}},
		          {field:'describe',title:'描述',width:100,editor:{type:"textbox",options:{}}},
		          {field:'createName',title:'创建人',width:100},
		          {field:'isAccount',title:'设置财务账套',width:100,editor:{type:"combobox",options:{editable:false,data:[{value:"0",text:"否"},{value:"1",text:"是"}]}},formatter:function(value){
						for(var i=0; i<options.length; i++){
							if (options[i].id == value) return options[i].name;
						}
						return value;
					}},
		          {field:'enable',title:'是否有效',width:100,editor:{type:"combobox",options:{editable:false,data:[{value:"0",text:"否"},{value:"1",text:"是"}]}},formatter:function(value){
						for(var i=0; i<options.length; i++){
							if (options[i].id == value) return options[i].name;
						}
						return value;
		          }},
		          <fool:tagOpt optCode="atypeAction">
		          {field:'actions',title:'操作',width:100,formatter:function(value,row,index){return action(value,row,index);}},
				  </fool:tagOpt>
				    
		         ]],
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
		},
	}); */
	
	$('#dataTable').jqGrid({		
		datatype:function(postdata){
			$.ajax({
				url:'${ctx}/basedata/auxiliarytype/list',
				data:postdata,
			    dataType:"json",
			    complete: function(data,stat){
			    	if(stat=="success") {
			    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			    		$("#dataTable")[0].addJSONData(data.responseJSON);
			    		newIndex = $("#dataTable").jqGrid('getRowData').length;
			    	}
			    }
			});
		},
		forceFit:true,
		autowidth:true,
		viewrecords:true,
		height:$(window).outerHeight()-200+"px",
		pager:"#pager",
		rowNum:10,
		rowList:[10,20,30],
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},
		colModel:[
                  {name : 'isNew',label : 'isNew',hidden:true},
		          {name : 'editing',label : 'editing',hidden:true},
		          {name:'fid',label:'fid',hidden:true},
		          {name:'code',label:'编号',width:100,align:'center',editable:true,edittype:"text",editoptions:{dataInit:function(ed){
			        	$(ed).textbox({validType:'maxLength[50]',required:true,missingMessage:"该项必填",width:'100%',height:'100%'});
			        }}},
		          {name:'name',label:'名称',width:100,align:'center',editable:true,edittype:"text",editoptions:{dataInit:function(ed){
			        	$(ed).textbox({validType:'maxLength[50]',missingMessage:"该项必填",required:true,width:'100%',height:'100%'});
			        }}},
		          {name:'describe',label:'描述',width:100,align:'center',editable:true,edittype:"text",editoptions:{dataInit:function(ed){
			        	$(ed).textbox({validType:'maxLength[50]',width:'100%',height:'100%'});
			        }}},
		          {name:'createName',label:'创建人',width:100,align:'center'},
		          {name:'isAccount',label:'设置财务账套',width:100,align:'center',editable:true,edittype:"text",formatter:function(value){
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
		          {name:'enable',label:'是否有效',width:100,align:'center',editable:true,edittype:"text",formatter:function(value){
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
		          <fool:tagOpt optCode="atypeAction">
		          {name:'action',label:'操作',width:80,align:'center',formatter:action},
				  </fool:tagOpt>				    
		         ],onCellSelect:function(rowid,iCol,cellcontent,e){
		        	 editRow(rowid)
		         }
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});	
	/* $("#dataTable").removeAttr("style"); */
	</script>
</body>
</html>

