<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>授权</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<style>
</style>
</head>

<body>
<div id="resourceList">   
    <div class="nomal-tree-box" style="width: 19% !important; border: none">
       <div id="resourceTree" class="easyui-tree">
       </div>
    </div> 
      
    <div class="contentBox">
    	<div id="list" style=" height:100%;"> 
        </div>
    </div>   
 
</div>
<script type="text/javascript">
var showKey = 0;//修复加载窗口后，自动点击根节点会出现加载2次导致载入选择项失效的问题
$(function(){
	 $('#resourceTree').tree({
		 url:'${ctx}/resourceController/getAllTree',
		 cascadeCheck:false,
		 onClick: function(node){
			 var node = $('#resourceTree').tree('getSelected');
			 var id = node.id;
			 $('#list').load("${ctx}/resourceController/onPermission?resId="+id+"&roleId=${fid}");
			 $('#list').css('border','1px solid #CCC'); 
		 },
		 onLoadSuccess:function(node, data){
			 var root=$('#resourceTree').tree("getRoot");
			 $('#resourceTree').tree("collapseAll");
			 $('#resourceTree').tree("expand",root.target);
			 $('#resourceTree').tree("select",root.target);
			 $(root.target).click();
		 }
	 });
}); 
</script>
</body>
</html>