<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>报表查询条件配置</title>
<style>
</style>
</head>

<body>
	   <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>报表查询条件配置</h1>
                </div>             
             </div>
	
	   <div class="main">
	       <div class="nomal-tree-box">
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
		    columns:[[
		              {field:'fid',title:'fid',hidden:true,width:10},
		              {field:'reportName',title:'报表名',width:10},
		    ]],
		    onClick: function(node){
		    	if($('#reportTypeTree').tree("isLeaf",node.target)){
		    		id = node.id;
					$('#content').load("${ctx}/reportFilter/list?reportId="+id);
		    	}else{
		    		$('#content').html("");
		    	}
		    }   
	});
}); 
</script>
</body>  
</html>
