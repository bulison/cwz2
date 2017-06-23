<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<title>代办管理</title>
</head>
<body>
<div class="titleCustom">
         <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>代办管理</h1>
         </div>             
    </div>
<table id="billList"></table>
<div id="pager"></div>
<script type="text/javascript">

$('#billList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:"${ctx}/message/queryTodo",
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#billList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	forceFit:true,
	pager:'#pager',
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	//multiselect:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	autowidth:true,//自动填满宽度
	height:$(window).outerHeight()-200+"px",
	colModel:[	  		 
	  		{name:'busTitle',label:'任务',align:"center",width:100},
	  		{name:'busType',label:'类型',align:"center",width:40,formatter:dateType},
	  		{name:'endTime',label:'截止日期',align:"center",width:40,formatter:dateFormatAction},	  		 		
	  		//{name:'recordStatus',label:'状态',align:"center",width:100},			
	      ]
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
function dateFormatAction(value,options,row){
	return getDateStr(value);
} 
function dateType(value,options,row){
	value=value.substr(value.length-4);
	if(value=='Task'){
		  return "事件";
	  }else if(value=='Plan'){
		  return "计划";
	  }
}
</script>
</body>
</html>