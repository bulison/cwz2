<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
          <div class="form" id="show" style="text-align:center;"> 
            <p><font>部门编号：</font><input type="text" class="easyui-validatebox textBox"   value='${orgCode}'  disabled="disabled"/></p>
            <p><font>部门名称：</font><input type="text" class="easyui-validatebox textBox"   value='${orgName}'  disabled="disabled"/></p>
            <p><font>部门电话：</font><input type="text" class="easyui-validatebox textBox"   value='${phoneOne}' disabled="disabled"/></p>
          </div>  
</body>
