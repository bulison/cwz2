<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>余额调节表</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
body{
	overflow-y: hidden;
}
#gridBox .panel{
  border: 1px solid gray;
  display: inline-block;
}
.height5{
 height: 5xp;
 line-height: 5xp;
 clear: both;
}
.tool-box-pane a{
	margin-right:5px;
}
#gbox_dataTable	.ui-jqgrid-bdiv{
	overflow-x: hidden;
}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>余额调节表</h1>
       </div>             
    </div>
	<div class="tool-box">
			<div class="tool-box-pane">
	    <input id="projectName"/> <fool:tagOpt optCode="baladjSearch"><a id="search-btn" class="btn-blue btn-s">确定</a></fool:tagOpt> <a href="javascript:;" id="clear-btn" class="btn-blue btn-s">清空</a>
		</div>
	</div>
	<div id="gridBox">
	<table id="dataTable">
	</table> 
	</div>
	<script type="text/javascript" src="${ctx}/resources/js/cashier/balanceAdjustment/balanceAdjustment.js?v=${js_v}"></script>
    <script type="text/javascript">
       initManage();
    </script>
</body>
</html>
