<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>客户管理</title>
<style>
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">基础管理</a></li>
	        <li><a href="javascript:;" class="curr">客户管理</a></li>
	    </ul>
	</div>
	
	
	<div class="tool-box">
		<div class="tool-box-pane">
			<a href="javascript:;" id="add" class="btn-ora-add">新增</a> 
		</div>
		<div class="tool-box-pane">
			<input class="easyui-searchbox easyui-validatebox" id="search" name="search-box" data-options="prompt:'请输入客户名称',validType:'normalChar',iconWidth:28,width:'220px',height:'39px'"/>
		</div>
		<div id="status" class="tool-box-pane tool-box-where" style="margin-left:300px;">
			<div class="split-right">
			 <a href="javascript:;" class="curr" val="0">类别</a>
	    	 <a href="javascript:;" val="1">地区</a>
	    	 </div>
		</div>
	</div>

	<table class="easyui-datagrid" id="dataTable" style="width: 100%;"  
	 data-options="url:'${ctx}/customerController/list',fitColumns:true,singleSelect:true,scrollbarSize:0,pagination:true" >
		<thead>
			<tr>
				<th data-options="field:'code',width:100">客户编码</th>
				<th data-options="field:'name',width:100">客户名称</th>
				<th data-options="field:'category',width:100">类别</th>
				<th data-options="field:'contacts',  width:100">联系人</th>
				<th data-options="field:'phone',width:100">手机号码</th>
				<th data-options="field:'tel',  width:100">电话号码</th>
				<th data-options="field:'fax',width:100">传真</th>
				<th data-options="field:'email',  width:100">电子邮件</th>
				<th data-options="field:'address',width:100">地址</th>
     			 <th data-options="field:'fid',formatter:operationFormatter">操作</th> 
			</tr>
		</thead>
	</table>
	<%-- <a href="${ctx}/studentController/listPage">测试附件上传</a> --%>
	<div id="addBox"></div>

</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript">
$('#add').click(function(){
	$('#addBox').window({
		title:'新增客户信息',
		top:80,  
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/customerController/addUI',
		width:1000,
		height:375
	});
});

//操作
 function operationFormatter(value, rec , index) {
    var statusStr = '';
     statusStr += '<a class="btn-detail" title="详情" href="javascript:;" onclick="detailedById(\''+value+'\')"></a>';
	 statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+value+'\')"></a>';
	 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+value+'\')"></a>';
	 return statusStr ; 
}  

//编辑 
 function editById(fid){
	 $('#addBox').window({
			title:'修改客户信息',
			top:80,  
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			href:"${ctx}/customerController/editCustomer?fid="+fid,
			width:1000,
			height:375
		});
	 //location.href = "${ctx}/customerController/editCustomer?fid="+fid ; 
} 
	
//明细
function detailedById(fid){
	$('#addBox').window({
		title:'客户信息详情',
		top:80,  
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:"${ctx}/customerController/detailsCustomer?fid="+fid,
		width:1000,
		height:375
	});
	//location.href = "${ctx}/customerController/detailsCustomer?fid="+fid ; 
}

//删除
function removeById(fid){
	 $.fool.confirm({title:'确定',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/customerController/deleteAll',
					data : {ids : fid},
					dataType : 'json',
					success : function(data) {	
						if(data == 1){
							$.fool.alert({msg:'删除成功！',fn:function(){
								$('#dataTable').datagrid('reload');
							}});
						}
		    		}
				});
		 }
	 }});
}
$(function(){
	//分页条 
	setPager($('#dataTable'));
	
	//搜索框
	 $("#search").searchbox({
		prompt:'请输入客户名称',
		iconWidth:26,
		width:220,
		height:35,
		searcher:function(value,name){
			refreshData();
		}
	}); 
	
	$('#status a').click(function(){
		$(this).addClass('curr').siblings('a').removeClass('curr');
		refreshData();
	});
});

//重新加载datagrid 
function refreshData(){
	var selectFlag = $('#status .curr').attr('val');
	var options = {"name":$('#search').val(), selectFlag:selectFlag};
	$('#dataTable').datagrid('load',options); 
}
</script>