<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>报表查询语句配置界面</title>
<style>
  #text{
    margin:10px 10px 10px 10px;
    width: 600px;
    height: 300px;
    resize:none
  }
  #text-save{
    margin: 0 0 10px 0;
  }
  #textbox{
    text-align: center;
  }
</style>
</head>

<body>
	   <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>报表查询语句配置</h1>
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
				id = node.id;
			   	if($('#reportTypeTree').tree("isLeaf",node.target)){
			   		$('#content').load("${ctx}/report/sql/listPage?reportId="+id);
			   	}else{
			   		$('#content').html("");
			   	}
			}   
	});
}); 
</script>
</body>  
</html>
