<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = request.getContextPath();
request.setAttribute("basePath",basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${basePath }/gever/gdp/js/keyboard/css/keyboard.css" />
<script language="javascript">var basePath = "${basePath }";</script>
<script language="javascript" src="${basePath }/gever/gdp/js/jdevelop/JPattern.js"></script>
<script language="javascript" src="${basePath }/gever/gdp/js/jdevelop/JUtil.js"></script>
<script language="javascript" src="${basePath }/gever/gdp/js/jdevelop/JEvent.js"></script>
<script language="javascript" src="${basePath }/gever/gdp/js/jdevelop/JComponent.js"></script>
<script language="javascript" src="${basePath }/gever/gdp/js/keyboard/GKeyboard.js"></script>
</head>
<body>
<input id="key" ype="text" />
<script language="javascript">
	var key = new GKeyboard();
	key.attach( "key" );
</script>
</body>
</html>