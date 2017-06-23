<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录超时</title>
<spring:url value="/resources" var="resources_path"/>
</head>

<body>
<div style="margin:auto; padding:5px; margin-top:100px;width:712px; ">
<div style="background:#fff;">
  <div style=" width:712px; min-height:380px;background:url(${resources_path}/images/time.png) no-repeat!important; background:#fff; ">
    <div style=" padding-top:300px; text-align:center;">
      	请<a href="#" onclick="relogin()">重新登录 </a>
    </div>
  </div>
</div>
</div>
<script type="text/javascript">
	function relogin(){			
		window.top.window.location.href = "<%=contextPath%>/login/login";
	}
</script>
</body>
</html>
