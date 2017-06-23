<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人员弹出窗口</title>
</head>
<body>

<!-- <div class="window-search-box" style="margin:5px;">
<input id="member-code" class="easyui-textbox" data-options="prompt:'人员编号',width:100,height:30"/>
<input id="member-name" class="easyui-textbox" data-options="prompt:'人员名称',width:100,height:30"/>
<input id="member-phone" class="easyui-textbox" data-options="prompt:'人员电话',width:100,height:30"/>
<a class="btn-blue btn-s go-search">筛选</a> 
</div>-->

<table class="easyui-datagrid" id="member-table" data-options="idField:'fid',fitColumns:true,pagination:true,url:'${ctx}/userController/userList'">
<thead>
<tr>
 <th data-options="field:'fid',checkbox:true,width:10">fid</th>
 <th data-options="field:'userCode',width:10,sortable:false">人员编号</th>
 <th data-options="field:'userName',width:10,sortable:false">人员名称</th>
 <th data-options="field:'phoneOne',width:10,sortable:false">人员电话</th>

 <th data-options="field:'fisinterface',width:10,hidden:true">部门负责人</th>
 <th data-options="field:'sex',width:10,hidden:true">性别</th>
 <th data-options="field:'email',hidden:true,width:10">Email</th>
 <th data-options="field:'postcode',hidden:true,width:10">邮政编码</th>
 <th data-options="field:'faddress',hidden:true,width:10">地址</th>
 <th data-options="field:'fax',hidden:true,width:10">传真</th>
 <th data-options="field:'idCard',hidden:true,width:10"></th>
 <th data-options="field:'isMobileLogin',hidden:true,width:10">允许移动端登陆</th>
 <th data-options="field:'userDesc',hidden:true,width:10"></th>
 <th data-options="field:'orgId',hidden:true,width:10">部门ID</th>
</tr>
</thead>
</table>
 <div class="window-search-box" style=" margin:15px 450px;">
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>
<script type="text/javascript">
$(function(){
	$('#member-table').datagrid({
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		onDblClickRow:function(index,row){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		}
	});

	setPager($('#member-table'));
	
	/* $(".go-search").click(function(){
		var code=$("#member-code").textbox('getValue');
		var name=$("#member-name").textbox('getValue');
		var phone=$("#member-phone").textbox('getValue');
		var options = {"userCode":code,"username":name,"phoneOne":phone};
		$('#member-table').datagrid('load',options);
	}); */

	$(".select-ok").click(function(){
		var rows = $('#member-table').datagrid('getSelections');
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
});
</script>

</body>
</html>
