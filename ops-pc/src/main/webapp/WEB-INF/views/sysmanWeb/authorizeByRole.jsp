<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>按角色授权</title>
<style type="text/css">
 #subDept0-p{
    display: inline-block;
  }
  #subDept1-p{
    display: inline-block;
  }
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>按角色授权</h1>
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
var subDept=0;
$(function(){
	 $('#deptTree').tree({
		    url:'${ctx}/orgController/getAllTree',
		    onLoadSuccess:expandFirst,
		    onClick: function(node){
		    	if($("#deptTree").tree('getSelected').state=='closed'){
					 $(this).tree('expand',node.target);  
				}else if ($("#deptTree").tree('getSelected').state=='open'){
					 $(this).tree('collapse',node.target);
				}
				var node = $('#deptTree').tree('getSelected');
   		        var id = node.id;
				$('#content').load("${ctx}/roleController/onAuthorizeByRole?orgId="+id+"&subDept="+subDept);
			 },onLoadSuccess:function(node, data){ //默认选中第一个节点
				 var noder = $("#deptTree").tree('getRoot');
				 var id=noder.id;
				 $("#deptTree").tree('select', noder.target);
				 $('#content').load("${ctx}/roleController/onAuthorizeByRole?orgId="+id+"&subDept="+subDept);
			 } 
	});
}); 
</script>
