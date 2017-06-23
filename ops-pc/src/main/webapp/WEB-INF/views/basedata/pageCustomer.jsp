<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户管理</title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css" />
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/icon.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/demo.css"/>
<script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
  $(function(){
	   $('#customer').datagrid({
		   url : '${ctx}/customerController/list',
		   rownumbers:true,  
		   singleSelect:false,  
		   pagination: true,
		   fitColumns: true,
		   pageList :[5,10,15,20,25,30],
		   columns:[[ {
			   field:'fid',
		       title:'ID',
		       width:100,
		       align:'center'
		   },
		   {
			   field:'code',
		       title:'编码',
		       width:100,
		       align:'center'
		   },
		   {
			   field:'name',
		       title:'姓名',
		       width:100,
		       align:'center'
		   },
		   {
			   field:'category',
		       title:'类别',
		       width:100,
		       align:'center'
		   },
		   {
			   field:'phone',
		       title:'手机号码',
		       width:100,
		       align:'center'
		   },
		   {
			   field:'email',
		       title:'电子邮件',
		       width:100,
		       align:'center'
		   },
		   {
			   field:'address',
		       title:'地址',
		       width:100,
		       align:'center'
		   },
		   {
			   field:'orgFid',
		       title:'企业ID',
		       width:100,
		       align:'center'
		   }
		     ]],
		     toolbar:[
						{
							text:'删除用户' ,
							iconCls:'icon-no' , 
							handler:function(){
								var row = $('#customer').datagrid('getSelected');
								if (row){
									$.fool.alert({msg:'Item ID:'+row.fid+"\nPrice:"+row.listprice});
								}
							}								
						}
					] 
  
	   });
	   
		
	   //工具栏  
	 /*   $('#customer').datagrid({
		   toolbar:'#tb'
	   }); */
	   
	 
		
  });
   
   
</script>

</head>
<body>
<table id="customer"></table>
	<!-- <div id="tb">
		<a href="javascript:openAdd()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加客户</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改修改</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no',plain:true">删除客户</a>
	</div> -->
  
</body>

</html>
