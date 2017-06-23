<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>工资统计</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
#form a{
	margin-right:5px;
}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>工资统计</h1>
       </div>             
    </div>
    <form  id="form">
    <a href="javascript:;" id="print" class="btn-ora-printer" >打印</a>
    <input id="orgcombo" name="orgcombo"/>
	期间：<input id="selectYear" class="easyui-combobox" name="year"   
    data-options="width:160,height:32,valueField:'year',textField:'text',data:yearData" /> 
    <!-- <input class="easyui-combobox" id="selectYear" type="number" max="3000" min="0" step="2" name="dept"   
    data-options="valueField:'year',textField:'text',data:yearData" style="width: 300xp"/>
           部门：<input id="org" class="orgcombo easyui-combobox"/> -->
     <fool:tagOpt optCode="wagestatSearch"><a href="javascript:;" id="search"  class="btn-blue btn-s" >查询</a></fool:tagOpt> <a id="clear" class="btn-blue btn-s" >清空</a>
    </form>
    <br/>
    <table id="wageList"></table>
</body>
<script type="text/javascript" src="${ctx}/resources/js/wage/stat/manage.js"></script>
<script type="text/javascript">

//打印按钮
$("#print").click(function(){
	var year=$("#selectYear").combobox('getValue'); 
	//var deptId=$("#orgcombo").combobox('getValue');
	var deptId=deptName.getSelectedValue();//获取控件值
	printWageStat(year,deptId);
});
</script>
</html>
