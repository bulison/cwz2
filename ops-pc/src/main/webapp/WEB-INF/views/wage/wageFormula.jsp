<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>工资公式</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<style type="text/css">
#window .datagrid-header {
    position: absolute; visibility: hidden;
}
.tool-box-pane a{
 	margin-right:5px;
}
.ui-jqgrid tr.jqgrow td {white-space: normal;}
/**/
#gbox_dataTable	.ui-jqgrid-bdiv{
		overflow-x: hidden;
	}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>工资核算公式</h1>
       </div>             
   </div>
    <div class="tool-box">
		<div class="tool-box-pane">
		<div class="tool-box-pane">
	        <fool:tagOpt optCode="swagefAdd"><a href="javascript:;" title="" id="add" onclick="javascript:addRow();" class="btn-ora-add">添加</a></fool:tagOpt>
			<fool:tagOpt optCode="swagefRef"><a href="javascript:;" id="refresh" onclick="javascript:refresh();" class="btn-ora-refresh">刷新</a></fool:tagOpt>
		</div>
		</div>
		<input id="pageStatus" type="hidden" value="true"/>
		<input id="pageRs" type="hidden" value="3"/>
		<input id="isNew" type="hidden" value=""/>
	</div>
    <table id="dataTable"></table> 
	 <div id="window">
	  <table id="dataTable3" data-options="width:'100%',height:'200px',pagination:false,onSelect:selectData,remoteSort:false,singleSelect:true,selectOnCheck:true,checkOnSelect:true,fitColumns:true,url:'${ctx}/wageFormula/queryAll'">
		<thead>
			<tr>
			   <th data-options="field:'fid',hidden:true,sortable:true,width:10,editor:{type:'textbox'}"></th>
			   <th data-options="field:'columnName',sortable:true,width:10,editor:{type:'textbox'}">工资项目名称</th>
			</tr>
		</thead>
	   </table>  
	</div>
	<script type="text/javascript" src="${ctx}/resources/js/wage/wageFormula.js?v=${js_v}"></script>
	<script>	
        $('#dataTable').jqGrid({						
			datatype:function(postdata){
				$.ajax({
				   url:'${ctx}/wageFormula/queryAll',
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
			   viewrecords:true,
			   autowidth:true,//自动填满宽度
			   height:$(window).outerHeight()-200+"px",
		colModel:[
	              {name : 'isNew',label : 'isNew',hidden:true},
		          {name : 'editing',label : 'editing',hidden:true},
		          {name:'fid',label:'fid',hidden:true},
		          {name:'columnName',label:'工资项目名称',width:10,align:'center',novalidate:true,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
			        	$(ed).textbox({validType:['itemNum','maxLength[200]'],missingMessage:"该项必填",required:true,
		        		 });
		          }}},
		          {name:'columnType',label:'项目类型',width:13,align:'center',editable:true,edittype:"text",formatter:function(value){
		        	    var Type="";
	                	if(value == 1){
	                		Type='公式计算';
	                	}else if(value == 0){
	                		Type='手动输入';
	                	}else{
	                		Type=value;
	                	}
	                	return Type;
		          }},
		          {name:'formula',label:'公式',width:21,align:'center',editable:true,edittype:"text"},
		          {name:'defaultValue',label:'默认值',width:10,align:'center',editable:true,edittype:"text",editoptions:{dataInit:function(ed){
			        	$(ed).numberbox({precision:2,validType:['assetValue','maxLength[10]'],missingMessage:"该项必填"});
			        }}},
		          {name:'orderNo',label:'顺序号',width:10,align:'center',editable:true,edittype:"text"/* ,editoptions:{dataInit:function(ed){
			        	$(ed).textbox({validType:['accessoryNumber','maxLength[200]'],missingMessage:"该项必填",width:'100%',height:'100%',required:true,
			        		 });
			        }} */},
		          {name:'isView',label:'是否显示',width:10,align:'center',editable:true,edittype:"text",formatter:function(value){
                  	var str="";
                	if(value==1){
                		str='是';
                	}else if(value==0){
                		str='否';
                	}else{
                		str=value;
                	}
                	return str;
                }},        
		          {name:'remark',label:'备注',width:10,align:'center',editable:true,edittype:"text",editoptions:{dataInit:function(ed){
			        	$(ed).textbox({validType:'maxLength[200]'});
			        }}},	
		          {name:'action',label:'操作',width:10,align:'center',formatter:action},
		         ],onCellSelect:function(rowid,iCol,cellcontent,e){
		        	 editRow(rowid)
		         }
	});
        //$("#dataTable").removeAttr("style");  
        
	 $('#dataTable').jqGrid({
		 onClickRow:function(index,row){//点击该行就进入编辑状态			
			var myobj = $(this).parent().find('tr[datagrid-row-index='+index+'] td[field=action] a.btn-del');
			if(myobj.length>0){
				<fool:tagOpt optCode="swagefAction1">editRow(myobj);</fool:tagOpt>
			}
		}, 
		onLoadSuccess:function(){//列表权限控制
			<fool:tagOpt optCode="swagefAction2">//</fool:tagOpt>$('.btn-del').remove();
		},
		onCancelEdit:function(index,row){
			<fool:tagOpt optCode="swagefAction2">//</fool:tagOpt>$('.btn-del').remove();
		},
		onAfterEdit:function(index,row){
			var str = $(".datagrid-body").find("tr").attr("id");
			var arr = str.split('-');
			$("#datagrid-row-"+arr[2]+"-2-"+index+" td[field='action'] .datagrid-cell").html(getTBBtn(false));
			<fool:tagOpt optCode="swagefAction2">//</fool:tagOpt>$('.btn-del').remove();
		},
	});
	 $('#gbox_dataTable	.ui-jqgrid-bdiv').scroll(function () {
	     $('#gbox_dataTable input').blur();//tooltip-f导致横向滚动条问题
     })
	</script>
</body>
</html>
