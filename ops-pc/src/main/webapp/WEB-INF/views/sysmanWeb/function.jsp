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

<div id="functionTree" class="easyui-tree" data-options="url:'${ctx}/resourceController/getAllTree',checkbox:true" >
</div>

<input type="button"  id='yes' value="确定"  style="width:92px; height:25px;background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF;margin:20px 0px 0px 0px"/>

<script type="text/javascript">
$.post("${ctx}/resourceController/getResUser",{fid:"${fid}"},function(data){
 	$("#functionTree").tree({
 		cascadeCheck:false,	
		onLoadSuccess:function(){
			for(var i=0;i<data.length;i++){
			    var node = $('#functionTree').tree('find',data[i]);
			    if(node){
			    	$('#functionTree').tree('check', node.target);
			    };
		     };
		     $("#functionTree").find('span.tree-checkbox').click(function(e) {
			    	var target=$(this).parent();
					var node=$('#functionTree').tree('getNode',target);
					var len = $(node.target).parentsUntil("ul.tree", "ul").length;
					var parents=$("#functionTree").tree('getParent',target);
	      		    var childs=$("#functionTree").tree('getChildren',target);
	      		    if(node.checked==true){
	      		      for(var i=0;i<childs.length;i++){
	      		    	  $('#functionTree').tree('uncheck', childs[i].target);
	      		      };
	      		    }else{
	          		    $('#functionTree').tree('check', parents.target);
	          		    for(var i=0;i<childs.length;i++){
	          		    	$('#functionTree').tree('check', childs[i].target);
	          		    };
	      		    };
	      		  });
		},
	 });
});

function haveParent(parents,target){
	if($("#functionTree").tree('getParent',target)){
		var parNode=$("#functionTree").tree('getParent',target);
		parents.unshift(parNode);
		haveParent(parents,parNode[0].target);
		return parents;
	}
}

	
		


var checkNodesID="";
var parentNodeID="";

$("#yes").click(function(e) {
	var parentNodes=$("#functionTree").tree('getChecked','indeterminate');
	var checkNodes=$("#functionTree").tree('getChecked');	
	for (var i=0;i<checkNodes.length;i++){
		checkNodesID+=checkNodes[i].id+',';
	}
	for (var i=0;i<parentNodes.length;i++){
		parentNodeID+=parentNodes[i].id+',';
	}
    $.post("${ctx}/userController/functionUser",{userID:"${fid}",check:checkNodesID,parent:parentNodeID});
    checkNodesID="";
    parentNodeID="";
    $("#authorize").window('close');
});
</script>
</body>
</html>