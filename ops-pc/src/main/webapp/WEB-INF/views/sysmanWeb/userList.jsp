<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
          <div style="text-align:center; width:auto; height:auto">
             <table id="principal-list" ></table>
             <input id="select" value="选择" type="button" style="width:92px; height:25px;background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF;margin-top:20px"/>
          </div>
<script type="text/javascript">
$('#principal-list').datagrid({
	columns:[[
              {field:'checkbox',checkbox:true,width:100},
              {field:'userCode',title:'用户编号',width:100},
              {field:'userName',title:'姓名',width:100},
              {field:'fid',title:'fid',hidden:'true',width:100},
              ]],
    singleSelect:true,
    selectOnCheck:true,
    fitColumns:true,
    url:"${ctx}/userController/userList",
});

$("#select").click(function(e) {
	 var selected=$("#principal-list").datagrid("getSelected");
	 if(selected){
		 $("#principal-name").val(selected.userName);
		 $("#principal").val(selected.fid);
	     $('#list-user').window("close");
	     
	 }else{$('#list-user').window("close");}
     
});
</script>    
      
</body>
