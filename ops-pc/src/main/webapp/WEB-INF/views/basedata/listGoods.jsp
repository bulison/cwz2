<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>货品管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
</head>
<body>
       <div class="tree-box">
            <div class="btnBox">
         	<a id="add" class="btn addBtn" href="javascript:;">添加</a>
            <a id="update" class="btn updateBtn" href="javascript:;" >修改</a> 
         	<a id="delete" class="btn deleteBtn hide-border" href="javascript:;" >删除</a>
            </div>
            <ul id="goodsTree" class="easyui-tree">
            </ul>
       </div> 
       <div class="contentBox"> 
    	    <div id="content" style="height:100%">
            </div>
       </div>   




<script type="text/javascript">

 $(function(){
	 $('#goodsTree').tree({
		 url:'${ctx}/goodsController/getAllTree',
		 onLoadSuccess:expandFirst,
		    onClick: function(node){
		    	if($("#goodsTree").tree('getSelected').state=='closed'){
					 $(this).tree('expand',node.target);  
				}else if ($("#goodsTree").tree('getSelected').state=='open'){
					 $(this).tree('collapse',node.target);
				}
		    	var id = node.id;
		    	$('#id').val(id);
				var node = $('#goodsTree').tree('getSelected');
				$('#content').load("${ctx}/goodsController/List?id="+node.id);
				$('#content').css('border','1px solid #CCC');
			 }   
	});
}); 

	
 //删除 
 $('#delete').click(function(){
		var node = $('#goodsTree').tree('getSelected');
		var len = $(node.target).parentsUntil("ul.tree", "ul").length;
		if(len==0){
			$.fool.alert({msg:'不能删除根节点！'});
		}else if(!$('#goodsTree').tree('isLeaf',node.target)){
			$.fool.alert({msg:'请先清空所有子货品！'});
		}else{
			$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
				if(r){
					$.post('${ctx}/goodsController/delete?fid=' + node.id , function(data){
						if(data == 1){
						    $('#goodsTree').tree('remove' , node.target); 
						    $.fool.alert({msg:'删除成功！'});
						}else{
							$.fool.alert({msg:'删除失败！'});
						}
					});
				}
			},title:'确认'});
		};
    });
 
 //添加 
 	$('#add').click(function() {
        $('#form').form('clear');
        $('#save').fadeIn();
		$('#cancel').fadeIn();
		$("#parentId").val($('#id').val());
		$("#code").removeAttr('readonly');
		$("#name").removeAttr('readonly');
		$("#category").removeAttr('readonly');
		$("#spec").removeAttr('readonly');
		$("#unit").combobox('enable');    
		$("#unitPrice").removeAttr('readonly');
		$("#describe").removeAttr('readonly');
		$("#code").css('background-color','#FFF');
	    $("#name").css('background-color','#FFF');
	    $("#category").css('background-color','#FFF');
	    $("#spec").css('background-color','#FFF');
	    $(".textbox-text").css('background-color','#FFF');
	    $("#unitPrice").css('background-color','#FFF');
	    $("#describe").css('background-color','#FFF');
    });
 //  保存 
 
 $('#update').click(function(e) {
		var node = $('#goodsTree').tree('getSelected');
		var len = $(node.target).parentsUntil("ul.tree", "ul").length;
		if(len==0){
			$.fool.alert({msg:'不能修改根节点!'});
			return false;
		}
	        $('#save').fadeIn();
			$('#cancel').fadeIn();
			$("#fid").val('${goodsVos.fid}');
			$("#code").removeAttr('readonly');
			$("#name").removeAttr('readonly');
			$("#category").removeAttr('readonly');
			$("#spec").removeAttr('readonly');
			$("#unit").combobox('enable'); 
			$("#unitPrice").removeAttr('readonly');
			$("#describe").removeAttr('readonly');
			$("#code").css('background-color','#FFF');
	        $("#name").css('background-color','#FFF');
	        $("#category").css('background-color','#FFF');
	        $("#spec").css('background-color','#FFF');
	        $(".textbox-text").css('background-color','#FFF');
	        $("#unitPrice").css('background-color','#FFF');
	        $("#describe").css('background-color','#FFF');
    });

   
 
</script>
<input id="id" type="hidden" value=""/>
</body>
</html>
