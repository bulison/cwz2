<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>角色权限维护</title>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>角色权限维护</h1>
                </div>             
             </div>
	<%-- <div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="#">系统维护</a></li>
	        <li><a href="#" class="curr">角色权限维护</a></li>
	    </ul>
	</div> --%>
 	<div id="permissions"></div>
    <div id="content" style="height:270px;"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$('#content').load("${ctx}/roleController/onRoleRights");
</script>