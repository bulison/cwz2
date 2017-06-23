<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title></title>
<style type="text/css">
  html,body{min-width:1024px;}
  a{color:black;text-decoration: none;}
  .box{padding:0px;margin:0px;display:inline-block; border-radius:10px}
  #left{display:inline-block;width:890px;padding:0px;margin:0px;float:left;}
  #right{display:inline-block;width:260px;padding:0px;margin:0 0 0 20px;float:left;}
  #menubar{width:885px;height:160px;background:url('${ctx}/resources/images/menu_bar.png') no-repeat}
  #schedule{margin-top:20px;width:287px;height:320px;background-color:white;}
  #message{margin-top:20px;margin-left:3px;vertical-align:top;background-color:#FFFFFF;width:585px;height:320px;} 
  #weather{margin-top:20px;width:260px;height:160px;background:url('${ctx}/resources/images/tianqi.png') no-repeat}
  #notepad{width:260px;height:320px;background:url('${ctx}/resources/images/notebook.png') no-repeat} 
  
  #title-message{margin:17px 0 0 25px;padding:3px 0 0 50px;font-family:"黑体";font-size:25px;color:#8BC068;background:url("${ctx}/resources/images/work_icon.png") no-repeat 0px 0px;}
  #message ul{padding:0 0 0 0}
  #message li{height:30px;margin:10px 0 0 0;padding:0 0 5px 0;background:url('${ctx}/resources/images/line_2.png') no-repeat 0 34px} 
  #message .text,#message .btn{display:inline-block;margin:0 0 0 0;padding:0 0 0 0}
  #message .text label{font-size:13px;letter-spacing:2px;}
  #message .text{margin:0 150px 0 25px;padding-left:30px;/*  width:230px; */height:15px;display:inline-block;overflow:hidden;background:url('${ctx}/resources/images/mark_1.png') no-repeat 0px 2px }
  #message .btn{vertical-align: 7px;}
  #message .btn .accept{margin-right:21px}
  #message .btn .deal,#message .btn .plan,#message .btn .answer{margin-left:80px}
  .nomessage{width:100%;text-align: center;font-size: 24px;color:#9B9B9B;font-weight: bold;margin-top: 100px;}
  
  #title-schedule{margin:17px 0 0 25px;padding:3px 0 0 50px;font-family:"黑体";font-size:25px;color:#68C3D8;background:url("${ctx}/resources/images/schedule_icon.png") no-repeat 0px 0px;}
  #title-schedule a{color:#68C3D8}
  #schedule ul{padding:0 0 0 0;}   
  #schedule li{height:30px;margin:10px 0 0 0;padding:0 0 5px 0;background:url('${ctx}/resources/images/line_1.png') no-repeat 0 20px}
  #schedule .text label{font-size:13px;letter-spacing:2px;}
  #schedule .text{margin:0 150px 0 25px;padding-left:30px;/*  width:230px; */height:15px;display:inline-block;overflow:hidden;background:url('${ctx}/resources/images/mark_1.png') no-repeat 0px 2px }
  
  .quickLink{border-radius:100px;padding:50px;margin-top:30px;display: inline-block;outline:none;}  
</style>
</head>
<body>
<div class="nav-box">
	<ul>
    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
    </ul>
</div>

<div id="left">
  <div class="box" id='menubar' >
  	  <a href="${ctx}/plan/addPlan?planType=TPL_PLAN_TYPE_ORDER" class="quickLink" style="margin-left:44px"></a> 
  	  <a href="${ctx}/plan/addPlan?planType=TPL_PLAN_TYPE_TASK" class="quickLink" style="margin-left:28px; margin-left:32px \9"></a>   
  	  <a href="${ctx}/cashRecordController/listCashRecord" class="quickLink" style="margin-left:34px;margin-left:38px \9"></a> 
  	  <a href="${ctx}/customerController/listPage" class="quickLink" style="margin-left:34px;margin-left:38px \9"></a>
  	  <a href="${ctx}/goodsController/manage" class="quickLink" style="margin-left:32px;margin-left:36px \9"></a>
  	  <a href="${ctx}/warehourse/listWarehourse" class="quickLink" style="margin-left:32px;margin-left:37px \9"></a>  
  </div> 
  
  <div class="box" id='schedule' >
    <p id="title-schedule"><a href="${ctx}/agendaController/agendaUI">我的日程</a></p>
    
    <ul>
      <!-- <li><p class="text"><label><a href="#">asdfasdfasfafd</a></label></p></li>
      <li><p class="text"><label><a href="#">asdfasdfasfafd</a></label></p></li>
      <li><p class="text"><label><a href="#">asdfasdfasfafd</a></label></p></li>
      <li><p class="text"><label><a href="#">asdfasdfasfafd</a></label></p></li>
      <li><p class="text"><label><a href="#">asdfasdfasfafd</a></label></p></li>
      <li><p class="text"><label><a href="#">asdfasdfasfafd</a></label></p></li> -->
    </ul>
  </div>
  
  <div class="box" id='message' >
    <p id="title-message">待办事务</p>
    <c:choose>
    	<c:when test="${ fn:length(todoList) > 0 }">
    		<ul> 
		      <!-- <li><p class="text"><label>张三分派[采购电脑]任务给你</label></p><p class="btn"><a href="#" class="btn-green btn-s accept">接受</a>  <a href="#" class="btn-yellow btn-s reject">拒绝</a></p></li>
		      <li><p class="text"><label>[落实海运]快到期，请马上处理</label></p><p class="btn"><a href="#" class="btn-green btn-s deal">处理</a></p></li>
		      <li><p class="text"><label>李四分派一个事件需要你排程</label></p><p class="btn"><a href="#" class="btn-green btn-s plan">排程</a></p></li>
		      <li><p class="text"><label>王五回复了你的留言</label></p><p class="btn"><a href="#" class="btn-green btn-s answer">回复</a></p></li> -->
		      <c:forEach items="${todoList}" var="todo" varStatus="index">
				    <li><p class="text"><label><a href="${ctx}${todo.actionVal}">${todo.title}</a></label></p></li>
			  </c:forEach>
			</ul>
    	</c:when>
    	<c:otherwise>
    		<div class="nomessage">您今天暂无待办事务</div>
    	</c:otherwise>
    </c:choose>
  </div>
</div>

<div id="right">
  <div class="box" id='notepad' ></div>
  <br/>
  <div class="box" id='weather' ></div>
</div> 
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$(document).ready(function(){
	$.post('${ctx}/agendaController/queryAgenda',function(data,status){
		if(data.length<=6){
			if(data!=null&&data!=''){
				for(var i=0;i<data.length;i++){
					  $('#schedule').find('ul').append('<li><p class="text"><label><a href="${ctx}/agendaController/agendaUI?start='+data[i].start+'">'+data[i].title+'</a></label></p></li>');
				  }
			}else{
				 $('#schedule').append('<div class="nomessage">暂无日程</div>');
			}
		}else{
			 $('#schedule').append('<div class="nomessage">暂无日程</div>');
		};
	  });
});
</script>
</body>
</html>