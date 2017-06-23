<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
</head>
<body>
    <div class="nomal-tree-box">
	    <ul id="deptTree" class="easyui-tree"></ul>
	</div>
	
	<div class="contentBox" style="background-color: transparent;"> 
    	<div id="list" style="height:100%" ></div>
    </div>


<script type="text/javascript">
var showKey = 0;//修复加载窗口后，自动点击根节点会出现加载2次导致载入选择项失效的问题
$(function(){
	function expandFirst(){
		var roots=$('#deptTree').tree('getRoots');
		roots[0].target.click(); 
	}
	 $('#deptTree').tree({
		    url:'${ctx}/orgController/getAllTree',
		    onLoadSuccess:expandFirst,
		    onClick: function(node){
		    	if($("#deptTree").tree('getSelected').state=='closed'){
					 $(this).tree('expand',node.target);  
				}/* else if ($("#deptTree").tree('getSelected').state=='open'){
					 $(this).tree('collapse',node.target);
				} */
				var node = $('#deptTree').tree('getSelected');
    		    var id = node.id;
				$('#list').load("${ctx}/resourceController/onPermissionsToUser?roleId=${roleId}&orgId="+id);
			 }   
	});
}); 
</script>
</body>
</html>
