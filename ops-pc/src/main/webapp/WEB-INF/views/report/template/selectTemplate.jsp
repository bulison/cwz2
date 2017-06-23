<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>保存模板界面</title>
<style>
</style>
</head>

<body>
 <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>保存模板</h1>
                </div>             
             </div>
    <div id="selectTempList"></div>
    <div id="tb-select">
    <input id="select" type="button" class="btn-blue4 btn-s" value="选择"/>
    </div>
    <hr/><br/>
    <div id="details"></div> 
  

<script type="text/javascript">
$('#selectTempList').datagrid({
    url:'${ctx}/report/UserTemplate/list?reportId=${param.reportId}',
    fitColumns:true,
    scrollbarSize:0,
    pagination:false,
    singleSelect:true,
    selectOnCheck:true,
    checkOnSelect:true,
    toolbar:"#tb-select",
	columns:[[
              {field:'fid',checkbox:true},
              {field:'reportName',title:'报表',width:10},
              {field:'templateName',title:'模板名称',width:10},
              {field:'creatorName',title:'创建人',width:10},
              {field:'createTime',title:'创建时间',width:10},
              
    ]],
    onClickRow:function(index,row){
    	var _url = "${ctx}/report/UserTemplateDetail/getByUserTemplateId";
    	$.post(_url,{templateId:row.fid},function(data){
    		if(data.rows){
    			$('#details').datagrid("loadData",data);
    		}else{
    			$('#details').datagrid("loadData",[]);
    		}
    	});
    }
});
//setPager($('#selectTempList'));

$('#details').datagrid({
    fitColumns:true,
    scrollbarSize:0,
    pagination:false,
    singleSelect:true,
	columns:[[
              {field:'fid',hidden:true},
              {field:'displayName',title:'显示名称',width:10},
              {field:'value',title:'录入内容',width:10},
              {field:'inputType',hidden:true,width:10},
              {field:'aliasName',hidden:true,width:10},
              {field:'tableName',hidden:true,width:10},
              {field:'fieldName',hidden:true,width:10},
              {field:'order',hidden:true,width:10},
              {field:'mark',hidden:true,width:10},
              {field:'compare',hidden:true,width:10},
              
    ]],
});

$("#select").click(function(){
	var rows=$('#details').datagrid('getRows');
	$('#filterGrid').html('');
	for(var i=0;i<rows.length;i++){
		row=rows[i];
		if(row.inputType==1||row.inputType==8||row.inputType==9||row.inputType==10||row.inputType==11){
			$("#filterGrid").append("<p><font>"+row.displayName+"：</font><input id="+row.aliasName+" class='type"+row.inputType+"' tableName="+row.tableName+" fieldName="+row.fieldName+" data-options='value: \""+row.value+"\"'/></p>");
		}else{
			$("#filterGrid").append("<p><font>"+row.displayName+"：</font><input id="+row.aliasName+" class='type"+row.inputType+"' tableName="+row.tableName+" fieldName="+row.fieldName+" value="+row.value+" ></p>");
		}
		
	}
	$.parser.parse("#filterGrid");
	initial();
	$('#selectTempBox').window("close");
});
</script>
</body>  
</html>
