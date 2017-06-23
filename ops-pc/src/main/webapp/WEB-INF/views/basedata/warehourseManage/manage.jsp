<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>仓库管理</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/easyui-tree-extend.js?v=${js_v}"></script>
<style type="text/css">
  #content{
    height:100%;
    border: 1px solid #CCC;
  }
  #describe{
    width:320px;
    height:90px;
    vertical-align: top;
  }
  .form p, .form1 p{
    font-size: 16px
  }
  .save-btn{
    display: none;
  }
  #refresh{
    margin: 0px 0px 0 10px;
  }
</style>
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>仓库管理</h1>
                </div>             
             </div>
       <div class="search-tree-box">
            <div class="btnBox" >
         	<fool:tagOpt optCode="storeAdd"><a href="javascript:;" id="add" class="btn addBtn" onClick="addNode()">添加</a></fool:tagOpt>
            <fool:tagOpt optCode="storeEdit"><a href="javascript:;" id="update" class="btn updateBtn" onClick="editNode()">修改</a> </fool:tagOpt>
         	<fool:tagOpt optCode="storeDel"><a href="javascript:;" id="delete" class="btn deleteBtn" onClick="deleteNode()">删除</a></fool:tagOpt>
         	<fool:tagOpt optCode="storeRefresh"><a href="javascript:;" id="refresh" class="btns refreshBtn hide-border" title="刷新" onclick="refresh()">&nbsp;</a></fool:tagOpt>
            </div>
            
            <ul id="warehouseTree" class="easyui-tree"></ul>
       </div> 
          
       <div class="contentBox"> 
    	    <div id="content">
    	      <form id="form" class="form">
    	        <input id="fid" name="fid" type="hidden"/>
    	        <input id="updateTime" name="updateTime" type="hidden"/>   
    	        <input id="parentId" name="parentId" type="hidden"/>
    	        <input id="flag" name="flag" type="hidden"/>
    	        <input id="systemSign" name="systemSign" type="hidden"/>
    	        <input id="level" name="level" type="hidden"/>
    	        <p><font><em>*</em>编号：</font><input id="code" name="code" class="textBox inputer" type="text" disabled="disabled"/></p>
    	        <p><font><em>*</em>名称：</font><input id="name" name="name" class="textBox inputer" type="text" disabled="disabled"/></p>
    	        <p><font><em>*</em>有效：</font><input id="enable1" name="enable" class="inputer" type="radio" checked="checked" disabled="disabled" value="1"/><span>是</span> <input id="enable0" name="enable" class="inputer" type="radio" disabled="disabled" value="0"/><span>否</span></p>
    	        <p><font>描述：</font><textarea id="describe" name="describe" class="inputer" disabled="disabled"></textarea></p>
    	        <p><font></font><input class="btn-blue2 btn-xs save-btn" type="button" value="保存" onclick="saver()"/></p>
    	      </form>
    	    </div>
       </div>   



<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
	$('#form').find('em').hide();
  $("#code").validatebox({});
  $("#name").validatebox({});
  $("#enable0").validatebox({});
  $("#describe").validatebox({});
  $('#warehouseTree').css('margin-top',$('.btnBox').outerHeight());
  $('#warehouseTree').tree({
	  url:'${ctx}/storehouses/getTree',
	  onLoadSuccess : function(){//数据加载默认选中一级根节点
		  var rootNode = $("#warehouseTree").tree("getChildren");//获取一级根节点
		  if (rootNode[1]) {
			  $("#warehouseTree").tree("select", rootNode[1].target);
			  $(rootNode[1].target).click();
		  } 	
	  },
	  formatter:function(node){
			return node.attributes?node.attributes.detail.code+"&nbsp;&nbsp;&nbsp;"+node.text:node.text;
	  },
	  onClick: function(node){
		  $.ajax({
				url:"${ctx}/storehouses/getDetail?"+Math.random(),
				async:false,
				data:{fid:node.id},
				success:function(data){
					editing(1);
					$("#fid").val(data.fid);
					$("#updateTime").val(data.updateTime);   
					$("#parentId").val(data.parentId);
					$("#flag").val(data.flag);
					$("#systemSign").val(data.systemSign);
					$("#level").val(data.level);
					$("#code").val(data.code);
					$("#name").val(data.name);
					$("#enable"+data.enable).click();
					$("#describe").val(data.describe);
				},
				error:function(){
					$.fool.alert({msg:'系统正忙，请稍后再试！'});
				}
		  });
		  editing(0);
	  } 
  });
  
  function addNode(){
	  $("#form").form("clear");
	  var node= $("#warehouseTree").tree("getSelected");
	  var root= $("#warehouseTree").tree("getRoot");
	  if(node!=root){
		  $("#parentId").val(node.id);
	  }
	  editing(1);
	  $("#enable1").click();
  }
  
  function editNode(){
	  var node= $("#warehouseTree").tree("getSelected");
	  var root= $("#warehouseTree").tree("getRoot");
	  if(node==root){
		  $.fool.alert({time:1000,msg:'不能修改根节点'});
		  return;
	  }
	  editing(1);
  }
  
  function deleteNode(){
	  var node= $("#warehouseTree").tree("getSelected");
	  var root= $("#warehouseTree").tree("getRoot");
	  if(node==root){
		  $.fool.alert({time:1000,msg:'不能删除根节点'});
		  return;
	  }
	  if(node.children.length>0){
		  $.fool.alert({time:1000,msg:'请先删除所有子仓库'});
		  return;
	  }
	  $.fool.confirm({title:'确认',msg:'确定要删除此仓库信息吗？',fn:function(r){
		  if(r){
			  $.ajax({
					url:"${ctx}/storehouses/delete",
					async:false,
					data:{fid:node.id},
					success:function(data){
						if(data.returnCode==0){
							var node=$("#warehouseTree").tree("getSelected");
							var nodeParent=$("#warehouseTree").tree("getParent",node.target);
							$("#warehouseTree").tree("reload");
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								var newNode=$("#warehouseTree").tree("find",nodeParent.id);
								$(newNode.target).click();
							}});
						}else if(data.returnCode==1){
							$.fool.alert({msg:data.message});
						}
					},
					error:function(){
						$.fool.alert({msg:'系统正忙，请稍后再试！'});
					}
			  });
		  }
	  }});
  }
  
  function saver(){
	  $('#form').form('enableValidation');
	  if(!$('#form').form('validate')){
		  return false;
	  }
	  var data=$("#form").serializeJson();
	  $.ajax({
			url:"${ctx}/storehouses/save",
			type:"post",
			async:false,
			data:data,
			success:function(data){
				if(data.returnCode==0){
					var node=$("#warehouseTree").tree("getSelected");
					$("#warehouseTree").tree("reload");
					$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
						if(dhxkey == 1){
	                        selectTab(dhxname,dhxtab);
	                    }
						var newNode=$("#warehouseTree").tree("find",node.id);
						$(newNode.target).click();
					}});
				}else if(data.returnCode==1){
					$.fool.alert({msg:data.message});
				}
			},
			error:function(){
				$.fool.alert({msg:'系统正忙，请稍后再试！'});
			}
	  });
  }
  
  function editing(mark){
	  var inputers=$("#form").find(".inputer");
	  if(inputers){
		  if(mark==1){
			  for(var i=0;i<inputers.length;i++){
				  $(inputers[i]).removeAttr("disabled");
			  }
			  $("#code").validatebox({required:true,novalidate:true});
			  $("#name").validatebox({required:true,novalidate:true});
			  $('#form').find('em').show();
			  $(".save-btn").show();
		  }else if(mark==0){
			  for(var i=0;i<inputers.length;i++){
				  $(inputers[i]).attr("disabled","disabled");
			  }
			  $('#form').find('em').hide();
			  $(".save-btn").hide();
		  }
	  }
  }
  
  function refresh(){
	  $("#warehouseTree").tree('reload');
  };
</script>
</body>
</html>
