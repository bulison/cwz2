<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>单位管理页面</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<style>
#unitTree{
    margin-top: 0px;
}
<fool:tagOpt optCode="gunitAdd|gunitEdit|gunitDel">
#unitTree{
	margin-top:29px;
}
</fool:tagOpt>
</style>
</head>
<body>
	<!-- 导航栏 -->
	<div class="nav-box">
		<ul>
			<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
			<li><a href="#">基础资料</a></li>
			<li><a href="#" class="curr">货品单位</a></li>
		</ul>
	</div>

	<div class="tree-box">
		<div class="btnBox">
			<fool:tagOpt optCode="gunitAdd"><a id="add" class="btn addBtn" href="javascript:;">添加</a></fool:tagOpt>
			<fool:tagOpt optCode="gunitEdit"><a id="update" class="btn updateBtn" href="javascript:;">修改</a></fool:tagOpt>
			<fool:tagOpt optCode="gunitDel"><a id="del" class="btn deleteBtn" href="javascript:;">删除</a> </fool:tagOpt>
		</div>
		<ul id="unitTree" class="easyui-tree"></ul>
	</div>
	<div class="contentBox">
		<div id="content" style="height: 100%"></div>
	</div>

	<script type="text/javascript">
		$(function(){
			//$("#add").click();
		});
	
		//校验表单		
		$("form").form("enableValidation");
		//异步请求
		function ajaxRequest(_url, _data, successFn){
			$.ajax({ 
				url: _url,
				data: _data,
				dataType: 'json',
				type: "post", 
				cache : false, 
				success: successFn,
				error: function(){
					$.fool.confirm({"msg":"系统繁忙，请稍后再试!"});
				}
			}); 
		}
		//初始化树控件
		$("#unitTree").tree({
			url : "${ctx}/unitController/getAll",
			onSelect:function(node){},  
			onLoadSuccess : function(){//数据加载默认选中一级根节点
				var rootNode = $("#unitTree").tree("getRoot");
				if (rootNode) {
					$("#unitTree").tree("select", rootNode.target);
					$(rootNode.target).click();
				} 	
			},
			onLoadError : function() {
				$.fool.alert({time:2000,msg : "系统繁忙，请稍后再试!"});
			},
			onClick : function(){
				if(isRootSelected()){
					$("#content").text("");
				}
				else{
					$("#content").load("${ctx}/unitController/detail?id=" + getSelectedId());
					/* var node = getSelectedNode();
					if(!node)return;
					ajaxRequest("${ctx}/unitController/sameLevelCount?parentId="+node.id,null,function(d){
						var root = getNodeLevel(node)==1 ? true :false;
						$("#content").load("${ctx}/unitController/detail?count="+d+"&root="+root+"&id=" + node.id);	
					}); */
				}
			}
		});
		//重新加载树控件数据
		function reloadTree(){
			$("#unitTree").tree("reload");
		}
		//获取选中节点的ID
		function getSelectedId(){
			var node = getSelectedNode();
			return node ? node.id : null;
		}
		
		function getSelectedNode(){
			var node = $("#unitTree").tree("getSelected"); //获取选中的节点并反回
			return node ? node : null;			
		}
		
		function getNodeLevel(node){
			if(node){
				return $(node.target).parentsUntil("ul.tree", "ul").length;
			}
			return 0;
		}
		
		//判断是否选中了根节点
		function isRootSelected(){
			var rootNode = $("#unitTree").tree("getRoot");//获取根节点返回节点对象
			var selectedNode = getSelectedNode();
			if(selectedNode){
				return rootNode.id == selectedNode.id;
			}else{
				$.fool.alert({time:2000,msg:'请选择一个节点'});
			}
		}
		//添加
		$("#add").click(function() {
			var node = getSelectedNode();			
			if(!node){
				$.fool.alert({time:2000,msg:'请选择一个节点'});
				return;
			}
			var level = getNodeLevel(node);
			if(level>=2){//判断是第几层级的id
				$.fool.alert({time:2000,msg:'不允许在该节点下继续添加节点'});
				return;
			}
			ajaxRequest("${ctx}/unitController/sameLevelCount?parentId="+node.id,null,function(d){
				var root = (getNodeLevel(node)==0);
				$("#content").load("${ctx}/unitController/add?count="+d+"&root="+root+"&parentId="+node.id,function(){
					fromEnable("#unitAddForm", (node?true:false));
					
					//var parentNode = $("#unitTree").tree("getParent",node.target);
					//var childrenNode = $("#unitTree").tree("getChildren",node.target);
					
					//alert(childrenNode[0].attributes.detail.first);
					
					//for(var i in childrenNode){
						//alert(childrenNode[i].text);
					//}
					
				});
			});
		});
		//更新、修改
		$("#update").click(function() {
			if(isRootSelected()){
				$("#content").text("");
				$.fool.alert({time:2000,msg:'根节点不允许修改'});
			}
			else{
				var node = getSelectedNode();
				if(!node){
					$.fool.alert({time:2000,msg:'请选择一个节点'});
					return;
				}
				var group = (getNodeLevel(node)==1);
				$("#content").load("${ctx}/unitController/edit?group="+group+"&id=" + getSelectedId());
			}
		});
		$("#del").click(function(){
			var node = getSelectedNode();
			if(!node){
				$.fool.alert({time:2000,msg:'请选择一个节点'});
				return;
			}
			if(isRootSelected()){
				$.fool.alert({time:2000,msg:'根节点不能删除'});
				return;
			}
			if(!$("#unitTree").tree("isLeaf",node.target)){
				$.fool.alert({time:2000,msg:'请先删除子单位'});
				return;
			}
			
			ajaxRequest("${ctx}/unitController/delete?id="+node.id,null,function(d){
				if(d.returnCode==0){
					$.fool.alert({time:1000,msg:'删除成功！'});
					//reloadTree();
					var node = getSelectedNode();
					$('#unitTree').tree('remove' , node.target);
					var rootNode = $("#unitTree").tree("getRoot");
					if (rootNode) {
						$("#unitTree").tree("select", rootNode.target);
						$(rootNode.target).click();
					}
				}else{
					$.fool.alert({msg:d.message});
				}				
			});
		});
		
		 //增加节点
		 function addNode(fid,data){
			 var node = getSelectedNode();
			 var nodes = [{
		         "id":fid,
		         "text":data.code+" "+data.name,
		         "attributes":data
		     }];
		     $('#unitTree').tree('append', {
		         parent:node.target,
		         data:nodes
		     });
		     $(node.target).click();
		 }
		 
		//更新节点
		 function updateNode(data){
			 var node = getSelectedNode();
			 $('#unitTree').tree('update',{
		         target: node.target,
		         text:data.code+" "+data.name,
		         attributes:data
			 });
			 $(node.target).click();
		 }
	</script>
</body>
</html>
