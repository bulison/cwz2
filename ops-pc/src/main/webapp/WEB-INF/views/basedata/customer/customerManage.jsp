<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户管理</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>客户管理</h1>
                </div>             
             </div>
<div class="toolBox">
		<div class="toolBox-button" style="margin-right:5px;">
			<fool:tagOpt optCode="customerAdd"><a href="javascript:;" id="add" class="btn-ora-add">新增</a></fool:tagOpt>
			<fool:tagOpt optCode="customerExport"><a href="javascript:;" id="export" class="btn-ora-export">导出</a></fool:tagOpt>
			<fool:tagOpt optCode="customerImport"><a href="javascript:;" id="getIn" class="btn-ora-import" >导入</a></fool:tagOpt>
			<fool:tagOpt optCode="customerRefresh"><a href="javascript:;" id="refresh" class="btn-ora-refresh">刷新</a>  </fool:tagOpt>
		</div>
		<div class="toolBox-pane">
			<fool:tagOpt optCode="customerSearch"><input id="search"/></fool:tagOpt>
		</div>
		<div id="status" class="tool-box-pane tool-box-where" style="margin-left:170px;display: none;">
			<div class="split-right" id="sort-box">
			  <a href="javascript:;" val="code">编号</a>
	  		  <a href="javascript:;" val="name">名称</a>
	    	  <a href="javascript:;" val="area.name" >地区</a>
	    	  <a href="javascript:;" val="category.name" >分类</a>
			</div>
		</div>
	</div>

 <table id="dataTable"></table> 
 <div id="pager"></div>
<%-- <table class="easyui-datagrid" id="dataTable" pagination="true" fitColumns="true" data-options="remoteSort:false,singleSelect:true,fitColumns:true,url:'${ctx}/customer/list?showDisable=1'">
	    <thead>
	    	<tr>
	        	<th data-options="field:'code',width:10,sortable:true">编号</th>
	            <th data-options="field:'name',width:10,sortable:true<fool:tagOpt optCode="customerAction2">,formatter:open</fool:tagOpt>">名称</th>
	            <th data-options="field:'shortName',width:10">简称</th>
	            <th data-options="field:'areaName',width:10,sortable:true">地区</th>
	            <th data-options="field:'categoryName',width:10">类别</th>
	            <th data-options="field:'creditLineUser',width:10">用户配置信用额度</th>
	            <th data-options="field:'creditLineSys',width:10">系统计算信用额度</th>
	            <th data-options="field:'recordStatus',width:10,formatter:statFormatter">状态</th>
	            <fool:tagOpt optCode="customerAction">
	            <th data-options="field:'fid',formatter:operationFormatter,width:10">操作</th>
	            </fool:tagOpt>
	        </tr>
	    </thead>
</table>
 --%>
<div id='pop-window'></div>
<div id='import-window'></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script>
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
$('#dataTable').jqGrid({	
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/customer/list?showDisable=1',
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
	height:$(window).outerHeight()-200+"px",
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
	loadcomplete:function(){
		//alert(JSON.stringify())
		 warehouseAll(); 
	}, 
	colModel:[       
	  		{name:'fid',label:'fid',hidden:true,width:100,align:'center'},
	  		{name:'code',label:'编号',sortable:true,width:100,align:'center'},
	  		{name:'name',label:'名称',sortable:true,width:100,align:'center',formatter:function(value,options,row){	  			
	  			var status='';
	  			<fool:tagOpt optCode="customerAction2">status+='<a href="javascript:;" onclick="edit(\''+row.fid+'\')">'+value+'</a>'</fool:tagOpt> ; 
	  			return status ;
	  		}},	  			  		 	
	  		{name:'shortName',label:'简称',width:100,align:'center'},	  		
	  		{name:'areaName',label:'地区',width:100,align:'center'},
	  		{name:'categoryName',label:'类别',width:100,align:'center'},
	  		{name:'creditLineUser',label:'用户配置信用额度',width:100,align:'center'},
	  		{name:'creditLineSys',label:'系统计算信用额度',width:100,align:'center'},
	  		{name:'recordStatus',label:'状态',width:100,align:'center',formatter:statFormatter},	  				  		  		
	  		{name:'action',label:'操作',width:30,width:60,align:'center',formatter:function(value,options,row){	  			
	  			var copy='',d='';
	  			<fool:tagOpt optCode="customerAction3">copy='<a class="btn-copy" href="javascript:;" title="复制" onclick="copy(\''+row.fid+'\',true)"></a>';</fool:tagOpt>
	  			<fool:tagOpt optCode="customerAction4">d='<a class="btn-del" href="javascript:;" title="删除" onclick="del(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  		    return copy+d;
	  		}}
	      ],	  
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

$(function($){
	$("#add").click(function(){
		windowHelp('新增客户','${ctx}/customer/add');
		return false;
	});
	//搜索框
	$("#search").fool('initSearch',{
		prompt:'请输入编号或名称',
		width:220,
		height:32,
		searcher:function(value,name){
			refreshData();
		}
	});
	
	$("#refresh").click(function(){
		$('#dataTable').trigger('reloadGrid');
	});
	
	$("#sort-box a").click(function(){
		$(this).addClass('curr').siblings().removeClass('curr');
		refreshData(null,null,$(this).attr('val'));
	});
	
	$("#export").click(function(){
		exportFrom('${ctx}/customer/export',{"searchKey": $('#search').val()});
	});
	
	$("#getIn").click(function(){
		var s={
				target:$("#import-window"),
				boxTitle:"导入客户",
				downHref:"${ctx}/ExcelMapController/downTemplate?downFile=客户.xls",
				action:"${ctx}/customer/import",
				fn:'okCallback()'
		};
		importBox(s);
	});
});

function okCallback(){
	$('#dataTable').trigger('reloadGrid');
	$("#import-window").window("close");
}
function open(value,row,index){
	return '<a href="javascript:;" onclick="edit(\''+row.fid+'\')">'+value+'</a>';
}
 function refreshData(value,name,sort){//查询
    var order = sort != undefined ? sort : $('#statusTag .curr').attr('val');
	var options = {"searchKey": $('#search').val(),'order':order};
	$('#dataTable').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
} 

function del(value){
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r){
			 $.ajax({
					type : 'post',
					url : '/ops-pc/customer/delete',
					data : {id :value},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.returnCode == 0){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#dataTable').trigger('reloadGrid');
							}});
						}else{
							$.fool.alert({time:2000,msg:'删除失败！'+data.message});
						}
		    		}
				});
		}
	},title:'确认'});
}

function copy(value){
	windowHelp('客户资料复制','${ctx}/customer/edit?flag=copy&id='+value);
	return false;
}

function edit(value){
	windowHelp('修改客户资料','${ctx}/customer/edit?flag=edit&id='+value);
	return false;
}

function viewDetail(value){
	windowHelp('客户详情','${ctx}/customer/detail?flag=detail&id='+value);
	return false;
}
function windowHelp(title,url){
	$('#pop-window').window({
		title:title,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		modal:true,
		top:($(window).height()-580)/2+$(document).scrollTop(),
		href:url,
		width:950,
		height:580,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			if($('#pop-win').html()!=""){
				$('#pop-win').window("destroy");
			}
			$("html").css("overflow","");
		}
	});
}
function operationFormatter(value,row,index){
	var d='',e='',c='',r='';
	/* <fool:tagOpt optCode="customerAction1">d = "<a href='javascript:;' class='btn-detail' onclick='viewDetail(\""+value+"\")' title='详情'></a>";</fool:tagOpt>
	<fool:tagOpt optCode="customerAction2">e = "<a href='javascript:;' class='btn-edit' onclick='edit(\""+value+"\")' title='编辑'></a>";</fool:tagOpt> */
	<fool:tagOpt optCode="customerAction3">c = "<a href='javascript:;' class='btn-copy' onclick='copy(\""+value+"\")' title='复制'></a>";</fool:tagOpt>
	<fool:tagOpt optCode="customerAction4">r = "<a href='javascript:;' class='btn-del' onclick='del(\""+value+"\")' title='删除'></a>";</fool:tagOpt>
	return c+r;
}

function statFormatter(value,row,index){
	if(value=='SAC')return '有效';
	else return '无效';
}
</script>