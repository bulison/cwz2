<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="sjjb" scope="page"></c:set>
<c:set var="billCodeName" value="事件级别" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/eventManagement.css" />
<script type="text/javascript" src="${ctx}/resources/js/eventManagement.js?v=${js_v}"></script> 
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>事件级别</h1>
                </div>             
    </div>
    <div class="btn">
      <a href="javascript:;" id="addNew" class="btn-ora-add">新增</a> 
     <!--  <a href="javascript:;" id="getIn" class="btn-ora-import" >导入</a>
      <a href="javascript:;" id="getOut" class="btn-ora-export">导出</a>
      <a href="javascript:;" id="print" class="btn-ora-printer">打印</a> -->
    </div>
    <div class="form_input">
      <form>
           <input name="codeOrVoucherCode" class="easyui-textbox" id="search-code"/>
      </form>
      <a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
    </div>
   <div id="importBox"></div> <!--导入 -->
   <table id="datalist"></table>
   <div id="pager"></div>
  <script type="text/javascript">
  var dhxkey = "${param.dhxkey}";
  var dhxname = "${param.dhxname}";
  var dhxtab = "${param.dhxtab}";
	initManage('${billCode}','${billCodeName}');
	enterSearch("Inquiry");//回车搜索
</script> 
</body>
</html>
