<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>报表汇总方式配置</title>
<style>
#reportTypeTree {
    margin-top: 0px;
}
<fool:tagOpt optCode="resultAdd|resultEdit|resultDel|resultRef">
#reportTypeTree{
	margin-top:29px;
}
</fool:tagOpt>
</style>
</head>

<body>
	   <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>汇总方式配置</h1>
                </div>             
             </div>
	
	   <div class="main">
	       <div class="tree-box">
	           <div class="btnBox">
	           <fool:tagOpt optCode="resultAdd">
	   	           <a id="add" class="btn addBtn" href="javascript:;">添加</a>
	   	       </fool:tagOpt>
	   	       <fool:tagOpt optCode="resultEdit">
                   <a id="update" class="btn updateBtn" href="javascript:;">修改</a>
               </fool:tagOpt>
               <fool:tagOpt optCode="resultDel"> 
         	       <a id="delete" class="btn deleteBtn" href="javascript:;">删除</a>
         	   </fool:tagOpt>
         	   <fool:tagOpt optCode="resultRef">
         	       <a id="refresh" class="btns refreshBtn hide-border" href="javascript:;" title="刷新" >&nbsp;</a>
               </fool:tagOpt>
               </div>
	           <ul id="reportTypeTree"></ul>
	       </div>
	
	       <div class="contentBox" style="background-color: transparent;"> 
    	       <div id="content" style="height:100%" ></div>
           </div>
    
           <div id="addBox"></div>  
	   </div>
	
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
var id;
$(function(){
	 //初始化树
	 $('#reportTypeTree').tree({
		    url:'${ctx}/report/SysRepor/getTree',
		    fitColumns:true,
		    singleSelect:true,
		    checkOnSelect:true,
		    selectOnCheck:true,
		    showHeader:false,
		    scrollbarSize:0,
		    onClick: function(node){
		    	id = node.id;
				$('#content').load("${ctx}/report/SysRepor/detail?fid="+id);
		    }  
	});
}); 

$("#refresh").click(function(){
	$("#reportTypeTree").tree('reload');
});

//新增
$('#add').click(function(){
	$("#form").form("clear");
	$("#parentId").val(id);
	$("#saveNCancel").fadeIn();
	$("#countInfo").removeAttr("disabled");
	$("#reportName").removeAttr("disabled");
	$("#code").numberbox("enable");
	$("#headers").removeAttr("disabled");
	($("#showPage").next())[0].comboObj.enable();
	$("#javaScript").removeAttr("disabled");
});

//编辑
$('#update').click(function(){
	$("#saveNCancel").fadeIn();
	$("#countInfo").removeAttr("disabled");
	$("#reportName").removeAttr("disabled");
	$("#code").numberbox("enable");
	$("#headers").removeAttr("disabled");
	($("#showPage").next())[0].comboObj.enable();
	$("#javaScript").removeAttr("disabled");
});

//删除
$('#delete').click(function(){
	$.fool.confirm({title:'确定',msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r){
			$.ajax({
				type : 'post',
				url : '${ctx}/report/SysRepor/deleteReport',
				data : {fid : id},
				dataType : 'json',
				success : function(data) {
					if(data.result == 0){
						$.fool.alert({msg:'删除成功！',fn:function(){
							$('#reportTypeTree').tree('reload');
						}});
					}else{
						$.fool.alert({msg:data.msg,fn:function(){
							$('#reportTypeTree').tree('reload');
						}});
					}
				},
				error:function(){
					$.fool.alert({msg:'系统正忙，请稍后再试。',fn:function(){
						$('#reportTypeTree').tree('reload');
					}});
				}
			});
		}
	}});
});

</script>
</body>  
</html>
