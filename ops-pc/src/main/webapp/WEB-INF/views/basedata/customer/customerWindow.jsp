<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户管理</title>
</head>
<body>
<div class="window-search-box" style="margin:5px;">
<input class="easyui-textbox" id="customer-code" data-options="prompt:'客户编号',width:100,height:30" />
<input class="easyui-textbox" id="customer-name" data-options="prompt:'客户名称',width:100,height:30" />
<input class="easyui-combotree" id="customer-categoryName" data-options="prompt:'类别',width:100,height:30,url:'${ctx}/basedata/costomerType'" />
<a href='javascript:;' class="btn-blue btn-s go-search">筛选</a>
<a href='javascript:;' id="clearSear" class="btn-blue btn-s">清空</a>
<a href='javascript:;' class="btn-blue btn-s select-ok">确定</a>
</div>

<table class="easyui-datagrid" id="customer-table" data-options="idField:'fid',fitColumns:true,pagination:true,url:'${ctx}/customer/list?showDisable=0'">
<thead>
<tr>
<th data-options="field:'fid',checkbox:true,width:10">编号</th>
<th data-options="field:'code',width:10,sortable:false">客户编号</th>
<th data-options="field:'name',width:10,sortable:false">客户名称</th>
<th data-options="field:'categoryName',width:10,sortable:false">类别</th>

<th data-options="field:'orgId',hidden:true,width:10">机构ID</th>
<th data-options="field:'shortName',hidden:true,width:10">简称</th>
<th data-options="field:'areaId',hidden:true,width:10">区域ID</th>
<th data-options="field:'areaName',hidden:true,width:10">区域名</th>
<th data-options="field:'categoryId',hidden:true,width:10">客户类别ID</th>
<th data-options="field:'categoryName',hidden:true,width:10">客户类别名称</th>
<th data-options="field:'creditLineSys',hidden:true,width:10">系统配置信用额度</th>
<th data-options="field:'creditLineUser',hidden:true,width:10">用户配置信用额度</th>
<th data-options="field:'businessContact',hidden:true,width:10">业务联系人</th>
<th data-options="field:'fax',hidden:true,width:10">业务联系人传真</th>
<th data-options="field:'phone',hidden:true,width:10">业务联系人手机</th>
<th data-options="field:'email',hidden:true,width:10">Email</th>
<th data-options="field:'address',hidden:true,width:10">地址</th>
<th data-options="field:'tel',hidden:true,width:10">电话</th>
<th data-options="field:'memberId',hidden:true,width:10">业务员ID</th>
<th data-options="field:'memberName',hidden:true,width:10">业务员</th>
<th data-options="field:'principal',hidden:true,width:10">法人代表</th>
<th data-options="field:'principalPhone',hidden:true,width:10">法人联系手机</th>
<th data-options="field:'creditRatingId',hidden:true,width:10">征信级别ID</th>
<th data-options="field:'creditRatingName',hidden:true,width:10">征信级别</th>
<th data-options="field:'postCode',hidden:true,width:10">邮编</th>
<th data-options="field:'registedCapital',hidden:true,width:10">注册资金</th>
<th data-options="field:'staffNum',hidden:true,width:10">在业人数</th>
<th data-options="field:'bank',hidden:true,width:10">开户银行</th>
<th data-options="field:'account',hidden:true,width:10">账户号</th>
<th data-options="field:'nationTax',hidden:true,width:10">国税号</th>
<th data-options="field:'landTax',hidden:true,width:10">地税号</th>
<th data-options="field:'registerDate',hidden:true,width:10">注册时间</th>
<th data-options="field:'creatorId',hidden:true,width:10">创建人ID</th>
<th data-options="field:'creatorName',hidden:true,width:10">创建人</th>
<th data-options="field:'createTime',hidden:true,width:10">创建时间</th>
<th data-options="field:'updateTime',hidden:true,width:10">更新时间</th>
<th data-options="field:'recordStatus',hidden:true,width:10">记录状态</th>
<th data-options="field:'describe',hidden:true,width:10">描述</th>
</tr>
</thead>
</table>

<script type="text/javascript">
$(function(){	
	$('#customer-table').datagrid({
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		onDblClickRow:function(index,row){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		}
	});
	
	$(".go-search").click(function(){
		var code=$("#customer-code").textbox('getValue');
		var name=$("#customer-name").textbox('getValue');
		var categoryId=$("#customer-categoryName").combotree('getValue');
		var options = {"code":code,"name":name,"categoryId":categoryId};
		$('#customer-table').datagrid('load',options);
	});
	
	$("#clearSear").click(function(){
		$(".window-search-box").form('clear');
	});
	
	$(".select-ok").click(function(){
		var rows = $('#customer-table').datagrid('getSelections');
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
	
	setPager($('#customer-table'));
});
</script>
</body>
</html>
