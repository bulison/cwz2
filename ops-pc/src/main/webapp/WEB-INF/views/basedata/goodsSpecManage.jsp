<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>货品属性管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/easyui-tree-extend.js?v=${js_v}"></script>
<script type="text/javascript">

</script>
<style>
#goodsSpecTree{
    margin-top: 0px;
}
<fool:tagOpt optCode="gspecAdd|gspecEdit|gspecDel">
#goodsSpecTree{
	margin-top:29px;
}
</fool:tagOpt>
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">货品管理</a></li>
	        <li><a href="javascript:;" class="curr">货品属性</a></li>
	    </ul>
	</div>


       <div class="tree-box">
            <div class="btnBox">
         	<fool:tagOpt optCode="gspecAdd"><a id="add" class="btn addBtn" href="javascript:;">添加</a></fool:tagOpt>
            <fool:tagOpt optCode="gspecEdit"><a id="update" class="btn updateBtn" href="javascript:;" >修改</a></fool:tagOpt>
         	<fool:tagOpt optCode="gspecDel"><a id="delete" class="btn deleteBtn" href="javascript:;" >删除</a></fool:tagOpt>
            </div>
            <ul id="goodsSpecTree" class="easyui-tree"></ul>
       </div> 
       <div class="contentBox"> 
    	    <div id="content" style="height:100%;border: 1px solid #CCC"></div>
       </div>   

<script type="text/javascript">

 $(function(){
	/*  $('#searchBox').keyup(function(){
		 $("#auxiliaryTree").tree("search", $(this).val()); 
	}); */
	
	$('#goodsSpecTree').tree({//初始化树数据
		 url:'${ctx}/goodsspec/getAll',
		 onLoadSuccess : function(){//数据加载默认选中一级根节点
				var rootNode = $("#goodsSpecTree").tree("getRoot");//获取一级根节点
				if (rootNode) {
					$("#goodsSpecTree").tree("select", rootNode.target);
					$(rootNode.target).click();
				} 	
			},
		 onClick: function(node){
		    	var id = node.id;
		    	$('#id').val(id);
		    	if(!isFirst(node)){
		    		$('#content').load("${ctx}/goodsspec/get/"+node.id,function(){
		    			fromEnable("#goodGoodsAttrForm",false);
		    		});
		    	}
		  }   
	});
	 
	 $('#content').load("${ctx}/goodsspec/add",function(){
		 fromEnable("#goodGoodsAttrForm",false);
	 });
});

//是否为第一层节点，顶级父节点
function isFirst(node){
	return (typeof node.attributes != 'undefined'&&node.attributes.isFirst != undefined);
}

//获取选中节点
function getSelectNode(){
	var node = $('#goodsSpecTree').tree('getSelected');
	if(!node){
		$.fool.alert({time:1000,msg:'请选择一个节点'});
		return;
	}
	return node;
}
 
 //更新节点
 function updateNode(data){
	 var node = getSelectNode();
	 $('#goodsSpecTree').tree('update',{
         target: node.target,
         text:data.name,
         attributes:data
	 });
	 $(node.target).click();
 }
 
 //增加节点
 function addNode(fid,data){
	 var node = getSelectNode();
	 var nodes = [{
         "id":fid,
         "text":data.name,
         "attributes":data
     }];
     $('#goodsSpecTree').tree('append', {
         parent:node.target,
         data:nodes
     });
 }

 //添加 
$('#add').click(function() {
		var node = getSelectNode();//选中的节点
		var parent = $("#goodsSpecTree").tree('getParent',node.target);//获取父节点
	    var root = $("#goodsSpecTree").tree('getRoot',node.target);//顶部父节点
		 if(parent != null){
			 if(parent.id != root.id){
			    $.fool.alert({time:1000,msg:'此属性不能生成组'});
			    return;
			 }
		 }
		fromEnable("#goodGoodsAttrForm",true);
	    $("input[type=text]").attr("value","");
	    $("input[type=hidden]").attr("value","");
	    $("textarea").html("");
		$("input[type=radio]:eq(0)").attr("checked",'checked');
		$("#parentId").val(node.id);
		if(typeof node.attributes != 'undefined'){
			$("#categoryId").val(node.attributes.categoryId);
			$("#first").val(node.attributes.isFirst);
		}else{
			$("#categoryId").val('');
			$("#first").val('');
		}
 });
 
 //修改
$('#update').click(function(e) {
	var node = getSelectNode();
	if(isFirst(node)){
		$.fool.alert({time:1000,msg:'该节点不允许修改'});
		return;
	}
	var root = $("#goodsSpecTree").tree("getRoot");
    if(node.id==root.id){
		$.fool.alert({time:1000,msg:'根节点不允许修改'});
		return;
	}
	fromEnable("#goodGoodsAttrForm",true);
	if(typeof node.attributes != 'undefined'){
		$("#categoryId").val(node.attributes.categoryId);
		$("#first").val(node.attributes.isFirst);
	}else{
		$("#categoryId").val('');
		$("#first").val('');
	}
});
	
 //删除 
 $('#delete').click(function(){
	var node = getSelectNode();
    var root = $("#goodsSpecTree").tree("getRoot");
    if(node.id==root.id){
		$.fool.alert({time:1000,msg:'根节点不允许删除'});
		return;
	}
	if(isFirst(node)){
		$.fool.alert({time:1000,msg:'该节点不允许删除'});
		return;
	}
	if(!$('#goodsSpecTree').tree('isLeaf',node.target)){
		$.fool.alert({time:1500,msg:'存在下级属性不允许删除!'});
		return;
	}
	
	$.fool.confirm({msg:'确定要删除该记录吗？',title:'提示',fn:function(r){
		if(r){
			$.post('${ctx}/goodsspec/delete?id=' + node.id,function(data){
				if(data.returnCode == '0'){
				    $('#goodsSpecTree').tree('remove' , node.target); 
				    $.fool.alert({time:1000,msg:'删除成功！'});
				    fromEnable("#goodGoodsAttrForm",false).form('clear');
				}else{
					$.fool.alert({time:1000,msg:'删除失败！'});
				}
			},'json');
		}
	}}); 
});
</script>
</body>
</html>
