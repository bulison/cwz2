<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>保存模板界面</title>
</head>

<body>
  &ensp;<input id="templateName" /> <input id="save" type="button" class="btn-blue4 btn-s" value="保存"/>
  <hr/><br/>
  <div id="savedTempList"></div>

<script type="text/javascript">
$("#templateName").textbox({
	prompt:'模板名称',
	width:167,
	height:30,
	required:true,
	novalidate:true
});
$('#savedTempList').datagrid({
    url:'${ctx}/report/UserTemplate/getUserTemplate?reportId=${param.reportId}',
    fitColumns:true,
    scrollbarSize:0,
    pagination:true,
    singleSelect:true,
	columns:[[
              {field:'fid',title:'fid',hidden:true,width:10},
              {field:'reportName',title:'报表',width:10},
              {field:'templateName',title:'模板名称',width:10},
              {field:'creatorName',title:'创建人',width:10},
              {field:'createTime',title:'创建时间',width:10},
              
    ]],
    onClickRow:function(index,row){
    	$("#templateName").textbox('setValue',row.templateName);
    }
});
setPager($('#savedTempList'));

$("#save").click(function(){
	var fid="";
	var condition=getConditions();
	var _templateName = $("#templateName").textbox('getValue');
	if(_templateName==undefined||_templateName.length==0){
		$.fool.alert({msg:'模板名不能为空'});
		return false;
	}
	if(condition==""){
		$.fool.alert({msg:'不能保存空模板。'});
		return false;
	}
	$("#save").attr('disabled',true);
	var _tempList = $('#savedTempList').datagrid('getRows');
	var _cover = false;
	if(_tempList.length>0){
		for(var i in _tempList){
			if(_tempList[i].templateName==_templateName){
				fid=_tempList[i].fid;
				_cover = true;
				break;
			}
		}
	}
	if(_cover){
		$.fool.confirm({msg:'已经存在该模板,是否覆盖？',title:'确认操作？',fn:function(r){
			if(r){
				saver(_templateName,fid,condition);
			}else{
				return false;
			}
		}});
	}else{
		saver(_templateName,fid,condition);
	}
});

function saver(templateName,fid,condition){
	var conditions=[];
	if(condition){
		conditions=JSON.stringify(condition);
	}
	$.ajax({
		  type : 'post',
		  url : '${ctx}/report/UserTemplate/save',
		  data : {fid:fid,templateName:templateName,condition:conditions,reportId:'${param.reportId}'},
		  dataType : 'json',
		  success : function(data) {
			  if(data.returnCode == 0){
				  $.fool.alert({msg:'保存成功！',fn:function(){
					  $("#save").removeAttr('disabled');
					  $('#saveTempBox').window('close');
				  }});
			  }else{
				  $.fool.alert({msg:data.message,fn:function(){
					  $("#save").removeAttr('disabled');
				  }});
			  }
		  },
		  error:function(){
			  $.fool.alert({msg:'系统正忙，请稍后再试。',fn:function(){
				  $("#save").removeAttr('disabled');
			  }});
		  }
	  });
}
</script>
</body>  
</html>
