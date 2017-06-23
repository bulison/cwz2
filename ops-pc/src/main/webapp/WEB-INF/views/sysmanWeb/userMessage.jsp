<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>用户信息维护</title>
<style>
.btnBox{
    /* position: fixed !important; */
    left:1;
    top:50;
  }
  #subDept0-p{
    display: inline-block;
  }
  #subDept1-p{
    display: inline-block;
  }
</style>
</head>

<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>用户信息维护</h1>
                </div>             
             </div>
 <div class="main">       
	<div class="tree-box">
	    <div class="btnBox">
    	 <fool:tagOpt optCode="UserAdd">
	   	 <a href="javascript:;" id="addUser" class="btn addBtn hide-border" >添加用户</a>
	     </fool:tagOpt>
         </div>
	    <ul id="deptTree" class="easyui-tree" style=" position:absolute;"></ul>
	</div>
    <div id="aaa"></div>
	<div class="contentBox" style="background-color: transparent;"> 
    	<div id="content" style="height:100%" ></div>
    </div>  
 </div>
	
  
</body>  
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
var id;
var subDept=0;
		$(function(){
			 $('#deptTree').tree({
				    url:'${ctx}/orgController/getAllTree',
				    onLoadSuccess:expandFirst,
				    onClick: function(node){
				    	if($("#deptTree").tree('getSelected').state=='closed'){
							 $(this).tree('expand',node.target);  
						}else if ($("#deptTree").tree('getSelected').state=='open'){
							 $(this).tree('collapse',node.target);
						}
						var node = $('#deptTree').tree('getSelected');
		    		    id = node.id;
						$('#content').load("${ctx}/userController/onUserList?orgId="+id+"&subDept="+subDept);

					 }   
			});
			 
		}); 
		
		$('#addUser').click(function(){
			if(id==null||id==""){
				$.fool.alert({msg:'请选择部门！'});
			}else{
			$('#aaa').window({
			 	href:'${ctx}/userController/addUserUI?orgId=' + id,
				top : 100,
				left : 90,
				width : 1000,
				title : '新增用户',
				modal : true,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				resizable : false,
			});
		}
	});
		$('#deptTree').css('margin-top',$('.btnBox').outerHeight());	
</script>