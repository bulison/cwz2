<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
	<table id="dataTable">
		<thead>
			<tr>
				<th data-options="field:'type',width:100">类型</th>
				<th data-options="field:'name',width:100,formatter:linkSty">名称</th>
			</tr>  
		</thead>
	</table>

<script type="text/javascript">
$(function(){
	//分页条 
	setPager($('#dataTable'));
		$("#dataTable").datagrid({
			url:'${ctx}/labelController/getLabelRelation?labelId='+'${labelId}',
			pagination:true,
			scrollbarSize:0,
			fitColumns:true,
			pageNumber:1,
		    pageSize:10,
		    pageList:[5, 10,20, 30]  
		}); 
});

function linkSty(val,row){
	if(row.eventId){
		return "<a href='${ctx}/axis/info?planFid="+row.planId+"&eventId="+row.eventId+"'>"+row.name+"</a>";
	}else{
		return "<a href='${ctx}/axis/info?planFid="+row.planId+"'>"+row.name+"</a>";    
	}
}
</script>
</body>
</html>