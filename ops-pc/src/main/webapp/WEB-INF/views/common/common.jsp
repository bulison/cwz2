<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="javax.servlet.http.Cookie"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fool" uri="/WEB-INF/tld/fool.tld" %>
<%@ taglib prefix="ss" uri="http://www.springframework.org/tags" %>

<%@ page trimDirectiveWhitespaces="true" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<c:set var="charset" value="UTF-8" scope="page"/>

<c:set var="company_name" value="佛山市蠢材科技有限公司" scope="page"/>
<c:set var="system_name" value="现金流管理系统" scope="page"/>
<c:set var="keywords" value="现金流管理系统,蠢材科技" scope="page"/>
<c:set var="description" value="现金流管理系统,蠢材科技" scope="page"/>
<c:set var="copyright" value="© 2016&nbsp;fooltech.cn,all rights reserved." scope="page"/>
<c:set var="icp_info" value="" scope="page"/>


<c:set var="version" value="beta 20170224_1.0_0071" scope="page"/>
<c:set var="css_v" value="20170224_1.0_0071" scope="page"/>
<c:set var="js_v" value="20170224_1.0_0071" scope="page"/>
 
<% response.setHeader("Cache-Control","no-cache");%> 
<% response.setHeader("Pragma","no-cache");%>
<% response.setDateHeader ("Expires",-1); %>