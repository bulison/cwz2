<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>部门信息维护</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<style>
  .btnBox{
    /* position: fixed !important; */
    left:1;
    top:50;
  }
</style>
<script type="text/javascript">
$(function(){
	 $('#deptTree').tree({
		    url:'${ctx}/orgController/getAllTree',
		    onLoadSuccess:function(){
		    	expandFirst();
		    },
		    onClick: function(node){
		    	if($("#deptTree").tree('getSelected').state=='closed'){
					 $(this).tree('expand',node.target);  
				}else if ($("#deptTree").tree('getSelected').state=='open'){
					 $(this).tree('collapse',node.target);
				}
				var node = $('#deptTree').tree('getSelected');
    		    var id = node.id;
				$('#content').load("${ctx}/orgController/List?id="+id);
			 }   
	});
	 
}); 
</script>
</head>

<body >  
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>部门信息维护</h1>
                </div>             
             </div>
	<%-- <div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="#">系统维护</a></li>
	        <li><a href="#" class="curr">部门信息维护</a></li>
	    </ul>
	</div> --%>

		 <div class="tree-box">
		 <div class="btnBox">
    	 <fool:tagOpt optCode="DeptAdd">
         	<a id="add" class="btn addBtn" href="javascript:;" >新增</a>
         </fool:tagOpt>
         <fool:tagOpt optCode="DeptChange">
         	<a id="update" class="btn updateBtn"  href="javascript:;">编辑</a>
         </fool:tagOpt>
         <fool:tagOpt optCode="DeptDel">
         	<a id="delete" class="btn deleteBtn hide-border" href="javascript:;">删除</a>
         </fool:tagOpt>
         </div>
         <ul id="deptTree" class="easyui-tree" style=" position:absolute;"></ul>
    	</div>   
    	
    	<div class="contentBox"> 
    	<div  id="content" style="height:100%" ></div>
    	</div>  
	    <div id="addDept"></div> 
	    
<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
$('#add').click(function(){
	var node = $('#deptTree').tree('getSelected');
	var root = $('#deptTree').tree('getRoot');
	if(node==null){
		/* if(root==null){ */
			$.fool.alert({msg:"请选择单位节点！"});
		/* }else{
			var id = '${orgId}';
			$('#addDept').window({
				href:'${ctx}/orgController/addDeptUI?id='+id,
				top:100,
				width:425,
				title:'新增部门',
				minimizable:false,
				maximizable:false
			});
		} */
	}else{
		var id = node.id;
		$('#addDept').window({
			href:'${ctx}/orgController/addDeptUI?id='+id,
			top:100,
			width:425,
			title:'新增部门',
			minimizable:false,
			maximizable:false,
			onClose:function(){
			}
		});
	}
	
});

$('#update').click(function(){
	var node = $('#deptTree').tree('getSelected');
	if(node==null){
		$.fool.alert({msg:"请选择单位节点！"});
	}else{
		var id = node.id;
		$('#addDept').window({
			href:'${ctx}/orgController/updateDeptUI?id='+id,
			top:100,
			width:425,
			title:'修改部门',
			minimizable:false,
			maximizable:false
		});
	}
});

$('#delete').click(function(){
	var node = $('#deptTree').tree('getSelected');
	var id = node.id;
	var len = $(node.target).parentsUntil("ul.tree", "ul").length;
	 if(len==0){
		$.fool.alert({msg:'不能删除根节点！'});
	}else{
		$.fool.confirm({msg:'确定要删除该条记录吗?',fn:function(r){
			if(r=='1'){
				$.post('${ctx}/orgController/deleteDept',{fid:id},function(data){
					if(data.returnCode=='0'){
		    			  $.fool.alert({msg:'删除成功！',fn:function(){
		    				  $('#deptTree').tree('reload');
		    			  }});
		    		  }else if(data.returnCode=='1'){
		    			  $.fool.alert({msg:data.message});
		    		  }else{
		    			  $.fool.alert({msg:"系统正忙，请稍后再试。"});
		    		  }
				});
			}
		},title:'确认'});
	};
});

$('#deptTree').css('margin-top',$('.btnBox').outerHeight());

</script> 
 </body>
</html>
 