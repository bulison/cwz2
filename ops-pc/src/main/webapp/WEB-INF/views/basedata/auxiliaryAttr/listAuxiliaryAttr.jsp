<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>辅助属性管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/easyui-tree-extend.js?v=${js_v}"></script>
<script type="text/javascript">

</script>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="javascript:;" href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">基础资料</a></li>
	        <li><a href="javascript:;" class="curr">辅助属性</a></li>
	    </ul>
	</div>
 <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>辅助属性</h1>
                </div>             
             </div>

       <div class="search-tree-box">
            <div class="btnBox" style="border-bottom:none; ">
         	<fool:tagOpt optCode="auxAdd"><a id="add" class="btn addBtn" href="javascript:;">添加</a></fool:tagOpt>
            <fool:tagOpt optCode="auxEdit"><a id="update" class="btn updateBtn" href="javascript:;">修改</a> </fool:tagOpt>
         	<fool:tagOpt optCode="auxDel"><a id="delete" class="btn deleteBtn" href="javascript:;">删除</a></fool:tagOpt>
         	<fool:tagOpt optCode="auxRefresh"><a id="refresh" class="btns refreshBtn hide-border" href="javascript:;" title="刷新" >&nbsp;</a></fool:tagOpt>
         	<fool:tagOpt optCode="auxSearch"><input type="text" id="searchBox" class="searchbox"/></fool:tagOpt>
            </div>
            
            <ul id="auxiliaryTree" class="easyui-tree"></ul>
       </div>     
       <div class="contentBox">             
    	    <div id="content" style="height:92%;border: 1px solid #CCC;"></div>
       </div>   



<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
var dhxtab_ = dhxtab.substring(2);
 $(function(){
	/*  $('#searchBox').keyup(function(){
		 $("#auxiliaryTree").tree("search", $(this).val()); 
	}); */
	
	$("#refresh").click(function(){
		$("ul#auxiliaryTree").tree('reload');
		//详细页面也刷新
		$('#content').load("${ctx}/basedata/viewAuxiliaryAttr/init",function(){
			 fromEnable("#auxiliaryAttrForm",false);
		 });
	});
	 
	$("#searchBox").searchbox({
		 prompt:'请输入搜索内容',
		 height:'38px',
		 iconWidth:26,
		 searcher:function(){
			 $("#auxiliaryTree").tree("search", {searchText:$(this).val(),compar:function(searchText,node){
				if(node.attributes==undefined||node.attributes.detail==undefined)return false;
				var detail = node.attributes.detail;
				return $.trim(detail.code)!="" && detail.code.indexOf(searchText)!=-1;
			 }});
		 }
	}); 
	 $('#auxiliaryTree').css('margin-top',$('.btnBox').outerHeight());
	 $('#auxiliaryTree').tree({
		 url:'${ctx}/basedata/getAuxiliaryTree',
		   onLoadSuccess : function(){//数据加载默认选中一级根节点
			var rootNode = $("#auxiliaryTree").tree("getRoot");//获取一级根节点
				if (rootNode) {
					$("#auxiliaryTree").tree("select", rootNode.target);
					$(rootNode.target).click();
				}
				if(dhxtab_ == ""){
					return false;
				}
				$('ul#auxiliaryTree .tree-node .tree-title').each(function(){
					if($(this).text()==dhxtab_){
						var target = $(this).parent();
						$('#auxiliaryTree').tree('select', target);
						target.click();
						var dis = target.position().top - $('.btnBox').outerHeight();
						$('ul#auxiliaryTree').animate({scrollTop:dis},300);  
					}
				});
			},  
		 onClick: function(node){
		    	var id = node.id;
		    	$('#id').val(id);
		    	
		    	if(!isFirst(node)){
		    		$('#content').load("${ctx}/basedata/viewAuxiliaryAttr/"+node.id,function(){
		    			fromEnable("#auxiliaryAttrForm",false);
		    		});
		    	}
		  }   
	});
	 
	 $('#content').load("${ctx}/basedata/viewAuxiliaryAttr/init",function(){
		 fromEnable("#auxiliaryAttrForm",false);
	 });
	 
});

//是否为第一层节点
function isFirst(node){
	return (typeof node.attributes != 'undefined'&&node.attributes.isFirst != undefined);
}

//获取选中节点
function getSelectNode(){
	var node = $('#auxiliaryTree').tree('getSelected');
	if(!node){
		$.fool.alert({time:1000,msg:'请选择一个节点'});
		return null;
	}
	return node;
}
 
 //更新节点
 function updateNode(data){
	 var node = getSelectNode();
	 $('#auxiliaryTree').tree('update',{
         target: node.target,
         text:data.code+" "+data.name,
         attributes:data
	 });
	 $(node.target).click();
 }
 
 //增加节点
 function addNode(fid,data){
	 var node = getSelectNode();
	 var nodes = [{
         "id":fid,
         "text":data.code+" "+data.name,
         "attributes":data
     }];
     $('#auxiliaryTree').tree('append', {
         parent:node.target,
         data:nodes
     });
 }

 function getNodeRoot(node){
	 var parent=$('#auxiliaryTree').tree('getParent',node.target);
	 if(parent){
		 return getNodeRoot(parent);
	 }else{
		 return node;
	 }
 }
 
 //添加 
$('#add').click(function() {
		var node = getSelectNode();
		if(null==node)return;
		//如果是辅助属性的dhxCombo链接过来，只能添加该辅助属性
		var cgName = node.attributes.categoryName?node.attributes.categoryName:node.text;
		if(dhxkey == 1 && cgName != dhxtab_){
			$.fool.alert({msg:'请选择添加或修改'+dhxtab_});
			return;
		}
		var root=getNodeRoot(node);
		if(!isFirst(node)){
            //设置学历和在职情况不允许添加子节点
            if(root.text == "学历" || root.text == "在职状况"){
                root.attributes.treeFlag = "0";
            }
			/* var str = JSON.stringify(node);
			alert(str); */
			if(root.attributes.treeFlag!=1){
				$.fool.alert({time:1000,msg:'该节点不允许添加子节点'});
				return;
			}
		}
		fromEnable("#auxiliaryAttrForm",true).form('clear');
		$("#parentId").val(node.id);
		if(typeof node.attributes != 'undefined'){
			$("#categoryId").val(node.attributes.categoryId);
			$("#first").val(node.attributes.isFirst);
		}else{
			$("#categoryId").val('');
			$("#first").val('');
		}
		$("input[name='enable']").first().click();
 });
 
 //修改
$('#update').click(function(e) {
	var node = getSelectNode();
	if(isFirst(node)||node.attributes.systemSign==1){
		$.fool.alert({time:1000,msg:'该节点不允许修改'});
		return;
	}
	//如果是辅助属性的dhxCombo链接过来，只能添加该辅助属性
	var cgName = node.attributes.categoryName?node.attributes.categoryName:node.text;
	if(dhxkey == 1 && cgName != dhxtab_){
		$.fool.alert({msg:'请选择添加或修改'+dhxtab_});
		return;
	}
	fromEnable("#auxiliaryAttrForm",true);
	$("#parentId").val(node.id);
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
	if(isFirst(node)||node.attributes.systemSign==1){
		$.fool.alert({time:1000,msg:'该节点不允许删除'});
		return;
	}
	if(!$('#auxiliaryTree').tree('isLeaf',node.target)){
		$.fool.alert({time:1000,msg:'存在下级属性不允许删除!'});
		return;
	}
	
	$.fool.confirm({msg:'确定要删除该记录吗？',title:'提示',fn:function(r){
		if(r){
			$.ajax({
				url:'${ctx}/basedata/deleteAuxiliaryAttr?fid=' + node.id,
				data:[],
				type:"post",
				async:false,
				success:function(data){
					dataDispose(data);
					if(data.returnCode == '0'){
					    $('#auxiliaryTree').tree('remove' , node.target); 
					    $.fool.alert({time:1000,msg:'删除成功！'});
					    fromEnable("#auxiliaryAttrForm",false).form('clear');
					}else{
						$.fool.alert({msg:'删除失败！['+data.message+']'});
					}
				}
				});
		}
	}});
});

</script>
</body>
</html>
