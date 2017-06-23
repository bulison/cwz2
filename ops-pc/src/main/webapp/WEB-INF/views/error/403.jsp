<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>403</title>
<spring:url value="/resources" var="resources_path"/>
<script type="text/javascript" src="${resources_path}/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${resources_path}/js/lib/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${resources_path}/js/lib/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${resources_path}/js/base.js?v=${js_v}"></script>
<link rel="stylesheet" href="${resources_path}/js/lib/easyui/themes/default/easyui.css" />
<link rel="stylesheet" href="${resources_path}/css/base.css?v=${css_v}"/>
</head>
<body>
<div style="margin:auto; padding:5px; margin-top:60px;width:712px;">
<!-- <div style="background:#fff;"> -->
  <div style=" width:712px; min-height:380px;background:url(${resources_path}/images/403.png) no-repeat!important; background:#fff; ">
   <!--  <div style=" padding-top:300px; text-align:center;">
     	      
    </div> -->    
  </div>
<!-- </div> -->
</div>
</body>
</html>
