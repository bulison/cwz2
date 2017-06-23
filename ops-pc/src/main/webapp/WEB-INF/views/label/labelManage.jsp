<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>标签管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<style type="text/css">
.btnBox a{
	margin-right:5px;
}
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">基础管理</a></li>
	        <li><a href="javascript:;" class="curr">标签管理</a></li>
	    </ul>
	</div>


       <div class="tree-box">
            <div class="btnBox">
         	<fool:tagOpt optCode="labelAdd"><a id="add" class="btn addBtn" href="javascript:;">添加</a></fool:tagOpt>
            <fool:tagOpt optCode="labelEdit"><a id="update" class="btn updateBtn" href="javascript:;" >修改</a> </fool:tagOpt>
         	<fool:tagOpt optCode="labelDel"><a id="delete" class="btn deleteBtn hide-border" href="javascript:;" >删除</a></fool:tagOpt>
            </div>
            <ul id="labelTree" class="easyui-tree">
            </ul>
       </div> 
       <div class="contentBox" style="background-color:transparent;">  
    	    <div id="content" style="height:100%;"></div>
    	    <div id="addLabel"></div>
       </div>   




<script type="text/javascript">
 $(function(){
	 $('#labelTree').tree({
		 url:'${ctx}/labelController/getLabel',
		    onClick: function(node){
		    	if($("#labelTree").tree('getSelected').state=='closed'){
					 $(this).tree('expand',node.target);  
				}else if ($("#labelTree").tree('getSelected').state=='open'){
					 $(this).tree('collapse',node.target);
				}
				var node = $('#labelTree').tree('getSelected');
				$('#content').css("display","block");
				$('#content').load("${ctx}/labelController/labelDetail?labelId="+node.id);
				if(node.createId!='${userId}'){
					$("#delete").css("color","gray");
					$("#update").css("color","gray"); 
				}
			 }    
	});
	 
	$("#add").click(function(){
		$("#addLabel").window({
			width:410,  
			height:120,     
			collapsible:false,
			minimizable:false,
			maximizable:false,
			title:"添加标签",
			href:"${ctx}/labelController/addLabel"
		});
	});
	
	$("#delete").click(function(){
		var node = $('#labelTree').tree('getSelected');
		if(node){
			if(node.createId=='${userId}'){
				$.post("${ctx}/labelController/delete",{fid:node.id},function(data){
					if(data==1){
						$.fool.alert({
							msg:"删除成功！",
							fn:function(){
								$('#labelTree').tree('reload');
								$('#content').css("display","none");
							}
						});
					}else{
						$.fool.alert({
							msg:"删除失败！"
						});
					}
				});
			}else{
				$.fool.alert({
					msg:"只能删除自己创建的标签。"
				});
			}
		}else{
			$.fool.alert({
				msg:"请选择标签。"
			});
		}
		
	});
	
	$("#update").click(function(){
		var node = $('#labelTree').tree('getSelected');
		if(node){
			if(node.createId=='${userId}'){
				$("#addLabel").window({
					width:410,  
					height:120,     
					collapsible:false,
					minimizable:false,
					maximizable:false,
					title:"修改标签",
					href:"${ctx}/labelController/addLabel?labelId="+node.id+"&labelName="+node.text
				});
			}else{
				$.fool.alert({
					msg:"只能修改自己创建的标签。"
				});
			}
		}else{
			$.fool.alert({
				msg:"请选择标签。"
			});
		}
	});
});
 $('#labelTree').css('margin-top',$('.btnBox').outerHeight());
</script>
</body>
</html>
