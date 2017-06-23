<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>数据分析界面</title>
<style>
</style>
</head>

<body>
    <table id="result"></table>
    <div id="pageaction" > </div> 
<script type="text/javascript">
var resultObj=eval(${result});
var rowNum=10;
var str=0;
var end=0;
if("${param.rowNum}"!=""){
    rowNum="${param.rowNum}";
}
$("#result").jqGrid({
	datatype:function(postdata){
		var list=resultObj.list;
		var result={};
		result.rows=resultObj.list;
		result.total=resultObj.pageBean.totalRows;
		result.totalpages=resultObj.pageBean.totalPages;
		result.page=resultObj.pageBean.currentPage;
		$("#result")[0].addJSONData(result);
	},
	rowNum:rowNum,
	width:$("#toolBox").width()-10,
	height:"300px",
	pager:"#pageaction",
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[
	          {name : '0',label : '凭证日期',align:'center',width:"100px",formatter:formate},
	          {name : '1',label : '凭证字号',align:'center',width:"100px",formatter:formate},
              {name : '2',label : '顺序号',align:'center',width:"100px",formatter:formate},
              {name : '3',label : '摘要',align:'center',width:"100px"},
              {name : '4',label : '科目编号',align:'center',width:"100px"},
              {name : '5',label : '科目名称',align:'center',width:"100px"},
              {name : '6',label : '借方金额',align:'center',width:"100px"},
              {name : '7',label : '贷方金额',align:'center',width:"100px"},
              {name : '8',label : '方向',align:'center',width:"100px"},
              {name : '9',label : '余额',align:'center',width:"100px"},
              {name : '10',label : '供应商',align:'center',width:"100px"},
              {name : '11',label : '客户',align:'center',width:"100px"},
              {name : '12',label : '部门',align:'center',width:"100px"},
              {name : '13',label : '人员',align:'center',width:"100px"},
              {name : '14',label : '项目',align:'center',width:"100px"},
              {name : '15',label : '仓库',align:'center',width:"100px"},
              {name : '16',label : '货品',align:'center',width:"100px"},
              ],
    gridComplete:function(){
    	$("#curSubjectId").val(resultObj.data.subject.fid);
    	$("#curSubjectCode").val(resultObj.data.subject.code);
        $("#curSubjectName").val(resultObj.data.subject.name);
    },
    onPaging:function(pgButton){//first,last,prev,next
    	var data = {};
    	var conditions=getConditions();
    	data.condition =JSON.stringify(conditions);
    	data.subjectLevel=$("#subjectLevel").val();
    	data.curSubjectId=$("#curSubjectId").val();
    	data.subjectStartCode=$("#startCode").textbox("getValue");
    	data.subjectEndCode=$("#endCode").textbox("getValue");
    	data.sysReportId="4028818652a4afc40152a504e8480087";
    	if(pgButton=="first"){
    		data.page=1;
        	data.rows=resultObj.pageBean.pageSize;
    	}else if(pgButton=="last"){
    		data.page=resultObj.pageBean.totalRows;
        	data.rows=resultObj.pageBean.pageSize;
    	}else if(pgButton=="prev"){
        	data.page=parseInt(resultObj.pageBean.currentPage)-1;
        	data.rows=resultObj.pageBean.pageSize;
    	}else if(pgButton=="next"){
    		data.page=parseInt(resultObj.pageBean.currentPage)+1;
        	data.rows=resultObj.pageBean.pageSize;
    	}
    	setTimeout(function(){
            var rows=$("#result").getGridParam().postData.rows;
            data.rows=rows;
            $("#resultBox").load("${ctx}/detailCategoryAccount/report/query?rowNum="+data.rows,data);
        }, 0.1);
    	/* $("#resultBox").load("${ctx}/detailCategoryAccount/report/query",data); */
    }
}).navGrid('#pageaction',{add:false,del:false,edit:false,search:false,view:false});

function formate(cellvalue, options, rowObject){
	if(cellvalue){
		if (rowObject.color){
			return '<p style:"color='+rowObject.color+'">'+cellvalue+'</p>';
		}else{
			return cellvalue;
		}
	}else{
		return "";
	}
}
</script>
</body>  
</html>
