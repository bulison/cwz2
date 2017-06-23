<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String objId=request.getParameter("objId");
String objType=request.getParameter("objType");
String btnName=request.getParameter("btnName");
String dlgName=request.getParameter("dlgName");
String formName=request.getParameter("formName");

if("".equals(btnName)||btnName==null)btnName="评分";
%>

<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="$('#<%=dlgName %>').dialog('open')"><%=btnName %></a>
<div id="<%=dlgName %>" class="easyui-dialog" >
	<form id="<%=formName %>" method="post">
	
	<input type="hidden" id="objId" name="objId" value="<%=objId %>">
	<input type="hidden" id="objType" name="objType" value="<%=objType %>">
	
	<p style="display:none;">评分：<input type="text" id="scores" name="scores" value=""></p>
	<p style="display:none;">评论：<input type="text" id="comment" name="comment" value=""></p>
	<p>级别：
	<select id="scoreLevel" name="scoreLevel">
		<option value="5" >优秀</option>
		<option value="4" selected="selected">良好</option>
		<option value="3">中等</option>
		<option value="2">合格</option>
		<option value="1">不合格</option>
	</select>
	</p>
	<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="confirmScore('<%=formName %>')">确定</a>
	</form>
</div>
<script type="text/javascript">
 $('#<%=dlgName %>').dialog({
	  title : '<%=btnName %>',
		width : 300,
		height : 300,
		iconCls: 'icon-save',
		modal : true
 }).dialog('close');
 
 function confirmScore(formName){
	 
	 $.ajax({
		    type: 'post',
		    url: "${ctx}/scores/score" ,
		    dataType: 'json',
		    data: $('#'+formName).serialize(),
		    success: function(data){
		    	if (data.resultCode=='0'){
	            	$('#<%=dlgName %>').dialog('close');
	            	showScoreMessage('亲',data.resultMsg);
	            }else{
	            	$('#<%=dlgName %>').dialog('close');
	            	showScoreMessage('OH~',data.resultMsg);
	            }
		    }
	 });
 }
 
//展示信息
 function showScoreMessage(t,message){
 	$.messager.show({
         title: t,
         msg: message,
         showType:'fade',
         style:{
             right:'',
             bottom:''
         }
     });
 }
</script>