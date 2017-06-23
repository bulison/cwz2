<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>导入异常报告</title>
<style>
#excelForm a{
	margin-right:5px;
}
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>导入异常报告</h1>
                </div>             
             </div>
	<div id="excelForm" style="margin: 10px 0px;">
	<input id="search-code" value="${code}"/>
	<fool:tagOpt optCode="importSearch"><a href="javascript:void(0)" id="search-btn" class="search-btn btn-blue btn-s" >查询</a></fool:tagOpt>
	<fool:tagOpt optCode="importDownload"><a href="javascript:void(0)" id="down-btn" class="btn-blue btn-s" >下载</a></fool:tagOpt>
	<a href="javascript:void(0)" id="goback" onclick="goback()" class="btn-blue btn-s" style="display:none;">返回</a>
	</div>
   <table id="data"></table>
   <div id="pager"></div>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript">
$(function(){
	search();
});
function goback(){
	var tab = parent.$("#tabs").tabs("getSelected");
	var index = parent.$("#tabs").tabs('getTabIndex',tab);
	parent.$("#tabs").tabs('close',index);
}
if('${param.code}'!=''){
	$('#goback').show();
}
$("#search-code").textbox({
	prompt:'流水号',
	width:150,
	height:30
});
$('#data').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/ExcelExceptionController/query',
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
	          {name:'fid',label:'fid',hidden:true,width:100},
	          {name:'code',label:'流水号',align:"center",sortable:true,width:100},
	          {name:'rowNum',label:'行号',align:"center",sortable:true,width:100},
	          {name:'describe',label:'描述',align:"center",sortable:false,width:100},
	          {name:'createTime',label:'创建时间',align:"center",sortable:true,width:100},
	          <fool:tagOpt optCode="importDel">
	          {name:'action',label:'操作',align:"center",sortable:false,width:50,formatter:function(value,options,row){
	        	 var d='<a href="javascript:;" title="删除" class="btn-del" onclick="deleterow(\''+row.fid+'\')" ></a>';
	        	 return d;
	          }}
	          </fool:tagOpt>
	          ]
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

//setPager($('#data'));

$("#search-btn").click(function(){
	var code=$("#search-code").textbox('getValue');
	var options = {"code":code};
	$('#data').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
});

$("#down-btn").click(function(){
	var code=$("#search-code").textbox('getValue');
	if(code.length<1){
		$.fool.alert({msg:'请输入流水号！',fn:function(){}});
		return;
	}
	window.location.href='${ctx}/ExcelExceptionController/excelfile?code='+code;
});

function search(){
	var code=$("#search-code").textbox('getValue');
	var options = {"code":code};
	$('#data').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
}
function deleterow(fid){
		 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
			 if(r){
				 $.ajax({
						type : 'post',
						url : '${ctx}/ExcelExceptionController/delete',
						data : {id : fid},
						dataType : 'json',
						success : function(data) {	
							dataDispose(data);
							$.fool.alert({msg:'删除成功！',fn:function(){
									$('#data').trigger("reloadGrid");
							}});
			    		}
					});
			 }
		 }}); 
}
enterSearch("search-btn");
</script>
</body>
</html>
