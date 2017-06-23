<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/layout.css"/>
<link rel="stylesheet" href="${ctx}/resources/css/comm.css"/>
<script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/locale/easyui-lang-zh_CN.js?v=${js_v}"></script>

<style>
</style>
</head>

<body>
<div id="roleList"  style="text-align:center">        
    <div style=" width:100%; height:545px;border:1px solid;display:inline-block;margin-top:10px;text-align:center">
        <table id="role"></table>
        <input type="button"  id='yes' value="确定"  style="width:92px; height:25px;background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF;margin:20px 0px 0px 0px"/>     
    </div>   
</div>
<script type="text/javascript">
$('#role').datagrid({
	columns:[[
              {field:'checkbox',checkbox:true,width:100},
	          {field:'roleName',title:'角色名称',width:100},
	          {field:'validation',title:'是否有效',formatter:function(value){
					for(var i=0; i<options.length; i++){
						if (options[i].id == value) return options[i].name;
					}
					return value;
				},
				editor:{
					type:'combobox',
					options:{
						valueField:'id',
						textField:'name',
						data:options,
						required:true
					}
				},width:100},
	          {field:'roleDesc',title:'描述',width:300,editor:{
          		type:'validatebox',
        		options:{
        			validType:'length[2,200]'
        		}
          	}
				},
	          {field:'fid',title:'fid',hidden:true,width:100}
	]],
	idField:'fid',
	    url:'${ctx}/roleController/roleList',
        fitColumns:true,
        pagination:true,
	    pageNumber:1,
	    pageSize:10, 
});

var checkResID="";
$.post("${ctx}/userController/getUserRole",{userId:"${userId}"},function(data){
    $("#role").datagrid({
		onLoadSuccess:function(){
			 for(var i=0;i<data.length;i++){
				$("#role").datagrid('selectRecord',data[i]);
		     }
		},
	});
});

$("#yes").click(function(e) {
	var checkRes=$("#role").datagrid('getChecked');
	 for (var i=0;i<checkRes.length;i++){
		 checkResID+=checkRes[i].fid+',';
	 }
	 $.post("${ctx}/roleController/roleToUser",{userId:"${userId}",check:checkResID});
	 checkResID="";
	 $("#authorize").window('close');
});
</script>

</body>
</html>