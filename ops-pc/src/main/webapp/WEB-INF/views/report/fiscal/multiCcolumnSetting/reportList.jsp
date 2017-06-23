<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>多栏明细账列表</title>
</head>
<body>

<table id="report-table"></table>
<div id="pager"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script>
/* function tableSearch(opt){
	//var url = "${ctx}/multiColumn/report/toList";
	//var options = $("#search-form").serializeJson();
	$('#report-table').datagrid('load',opt);
	return false;
} */
var _options = "";

$(function(){
	
	try{
		_options = window.parent.getSearchData();
		
		//alert(_options.status);
		
	}catch(e){
		//alert(e);
	}
	
	/* $('#report-table').datagrid(
			{
				url:'${ctx}/multiColumn/report/query',
				queryParams:_options, 
				pagination:true,
				//pageSize:'2',
		 frozenColumns: [[   
							<c:forEach items="${titles.fixTitle}" var="fixTitle">
							{ title: '${fixTitle.value }', field: '${fixTitle.key }', width: 80, sortable: true},
							</c:forEach>
                         ]],   
        columns: [
        [
	        <c:if test="${!empty titles.sizes.creditSize && titles.sizes.creditSize >0}">{"title":"贷方","colspan":${titles.sizes.creditSize}},</c:if>
	        <c:if test="${!empty titles.sizes.debitSize && titles.sizes.debitSize >0}">{"title":"借方","colspan":${titles.sizes.debitSize}},</c:if>         
        ],  
        [
			<c:if test="${!empty titles.sizes.creditSize}">
			<c:forEach items="${titles.creditTitle}" var="creditTitle">{"title":"${creditTitle.value }","field":"${creditTitle.key }","rowspan":1},</c:forEach>
			</c:if>
			<c:if test="${!empty titles.sizes.creditSize}">
			<c:forEach items="${titles.debitTitle}" var="debitTitle">{"title":"${debitTitle.value }","field":"${debitTitle.key }","rowspan":1},</c:forEach>
			</c:if>
        ]],
	});
	
	setPager($('#report-table')); */
	
	$("#report-table").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url:'${ctx}/multiColumn/report/query',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#report-table")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		/* footerrow: true, */
		autowidth:true,
		height:300,
		pager:"#pager",
		postData:_options, 
		rowNum:10,
		rowList:[10,20,30],
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[
		          <c:forEach items="${titles.fixTitle}" var="fixTitle">
		          { label: '${fixTitle.value }', name: '${fixTitle.key }', width: "80px", frozen:true},
                  </c:forEach>
		          
		          <c:if test="${!empty titles.sizes.creditSize}">
					<c:forEach items="${titles.creditTitle}" var="creditTitle">{"label":"${creditTitle.value }","name":"${creditTitle.key }", width: "100px",align:'center'},</c:forEach>
				  </c:if>
				  <c:if test="${!empty titles.sizes.debitSize}">
					<c:forEach items="${titles.debitTitle}" var="debitTitle">{"label":"${debitTitle.value }","name":"${debitTitle.key }", width: "100px",align:'center'},</c:forEach>
				  </c:if>
                  ],
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});  
	$("#report-table").jqGrid('setGroupHeaders', {
	    useColSpanStyle: true, 
	    groupHeaders:[
                      <c:if test="${!empty titles.sizes.creditSize && titles.sizes.creditSize >0}">{startColumnName: '${titles.creditTitle[0].key}', numberOfColumns: '${titles.sizes.creditSize}', titleText: '贷方'},</c:if>
                      <c:if test="${!empty titles.sizes.debitSize && titles.sizes.debitSize >0}">{startColumnName: '${titles.debitTitle[0].key}', numberOfColumns: '${titles.sizes.debitSize}', titleText: '借方'},</c:if>
	                  ]  
	});
	$("#report-table").jqGrid('setFrozenColumns');
});
</script>