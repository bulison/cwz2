<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>按功能授权</title>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>按功能授权</h1>
                </div>             
             </div>

    <div class="nomal-tree-box">
	    <ul id="deptTree" class="easyui-tree"></ul>
	</div>
	
	<div class="contentBox" style="background-color: transparent;"> 
    	<div id="content" style="height:100%" ></div>
    </div>

<div id="authorize"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$(function(){
	 $('#deptTree').tree({
		    url:'${ctx}/orgController/getAllTree',
		    onLoadSuccess:function(node, data){
		    	/* expandFirst(); */ 
		    	/* var node=$('#deptTree').tree('find',data[0].id);
		    	$('#deptTree').tree('select',node.target); */
		    	/* $('#content').load("${ctx}/userController/onAuthorizeByFunction?orgId="+node.id); */
		    	$('#content').load("${ctx}/userController/onAuthorizeByFunction");
		    },
		    onClick: function(node){
				var node = $('#deptTree').tree('getSelected');
    		    var id = node.id;
				$('#content').load("${ctx}/userController/onAuthorizeByFunction?orgId="+id);
			 }   
	});
}); 
</script>