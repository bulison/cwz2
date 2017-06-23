<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>资源信息维护</title>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>资源信息维护</h1>
                </div>             
             </div>
	 <%-- <div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="#">系统维护</a></li>
	        <li><a href="#" class="curr">资源信息维护</a></li>
	    </ul>
	</div> --%>
       <div class="tree-box">
            <div class="btnBox">
         	<fool:tagOpt optCode="ResourceAdd"><a id="add" class="btn addBtn" href="javascript:;">添加</a></fool:tagOpt>
            <fool:tagOpt optCode="ResourceUpt"><a id="update" class="btn updateBtn" href="javascript:;" >修改</a></fool:tagOpt>
         	<fool:tagOpt optCode="ResourceDel"><a id="delete" class="btn deleteBtn hide-border" href="javascript:;" >删除</a></fool:tagOpt>
            </div>
            <ul id="resourceTree" class="easyui-tree">
            </ul>
       </div> 
       <div class="contentBox"> 
    	    <div id="content" style="height:100%">
            </div>
       </div>
       
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$('#add').click(function(){
	var node = $('#resourceTree').tree('getSelected');
    var id = "";
    if(node==null){
    	$.fool.alert({msg:"需要选择一个节点!"});
    	return;
    }else{
    	id = node.id;
    }
	$('#addResource').window({
		href:'${ctx}/resourceController/addResourceUI?parent='+id,
		top:100,
		width:700,
		title:'新增资源',
		minimizable:false,
		maximizable:false
	});
});

$('#update').click(function(){
	var node = $('#resourceTree').tree('getSelected');
	var len = $(node.target).parentsUntil("ul.tree", "ul").length;
    var id = node.id;
    if(len==0){
    	$.fool.alert({msg:"不能修改根节点！"});
    	return;
    }
	$('#addResource').window({
		href:'${ctx}/resourceController/updateResourceUI?fid='+id,
		top:100,
		width:700,
		title:'修改资源',
		minimizable:false,
		maximizable:false
	});
});

$('#delete').click(function(){
	var node = $('#resourceTree').tree('getSelected');
	var id = node.id;
	var len = $(node.target).parentsUntil("ul.tree", "ul").length;
	 if(len==0){
		$.fool.alert({msg:'不能删除根节点！'});
	}else{
		$.fool.confirm({msg:'确定要删除该条记录吗?',fn:function(r){
			if(r){
				$.post('${ctx}/resourceController/delete',{fid:id},function(data){
					if(data=="1"){
						$.fool.alert({msg:'删除成功！',fn:function(){
			    			$('#resourceTree').tree('reload');
			    		}});
					}else{
						$.fool.alert({msg:'系统正忙，请稍后再试。',fn:function(){
			    			$('#resourceTree').tree('reload');
			    		}});
					}
				});
			}
		},title:'确认'});
	};
});

$(function(){
	 $('#resourceTree').tree({
		    url:'${ctx}/resourceController/getAllTree',
		    onLoadSuccess:expandFirst,
		    onClick: function(node){
		    	if($('#addResource').attr('class')!=null){
		    		$('#addResource').window('destroy');
		    	}
		    	if($("#resourceTree").tree('getSelected').state=='closed'){
					 $(this).tree('expand',node.target);  
				}else if ($("#resourceTree").tree('getSelected').state=='open'){
					 $(this).tree('collapse',node.target);
				}
				var node = $('#resourceTree').tree('getSelected');
    		    var len = $(node.target).parentsUntil("ul.tree", "ul").length;
    		    var id = node.id;
			    $('#content').load("${ctx}/resourceController/getRes?fid="+id);
			 }   
	});
});
$('#resourceTree').css('margin-top',$('.btnBox').outerHeight());
</script>   
</body> 
</html>
