<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
 
<link href='${ctx}/resources/js/fullcalendar/fullcalendar.css' rel='stylesheet' />
<link href='${ctx}/resources/js/fullcalendar/fullcalendar.print.css' rel='stylesheet' media='print' />
<script src='${ctx}/resources/js/fullcalendar/lib/moment.min.js'></script>
<script src='${ctx}/resources/js/fullcalendar/fullcalendar.min.js'></script>
<script src='${ctx}/resources/js/fullcalendar/lang/zh-cn.js'></script>
<title>我的日程</title>
<style type="text/css">
body {
		margin:0;
		padding: 0;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
		font-size: 12px;
	}

#continer{
        background-color: white;
}

#calendar {
        padding:20px 0;
		margin: 0 35px; 
	}
	
span{font-size:12px } 

.fc-state-default {
	background-color: #FFFFFF;
	background-image: none;
	border-color: #6EC9FF;
	color: #6EC9FF;
	text-shadow: none;
	box-shadow: none;
}

.fc-state-down,
.fc-state-active{
	color: #FFFFFF;
	background-color: #6EC9FF;
}

.fc-state-disabled {
    color: #333333;
	background-color: #e6e6e6;
}

.fc-state-hover {
	color: #FFFFFF;
	background-color: #6EC9FF;
	-webkit-transition: none; 
	   -moz-transition: none;
	     -o-transition: none;
	        transition: none; 
}

.fc-day-header{
    background-color: #CFEDFF;
    color:#7F8385;
    font-weight: normal;
}

.fc-today-button{
    background-color:#FFCF7A;
    color:#FFFFFF;
    border:none;
}

.option{
    border:none;
    margin-right:1px;
}
</style>
</head>
<body>
<div class="nav-box">
		<ul>
	    	<li class="index"><a href="javascript:;" href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">应用中心</a></li>
	        <li><a href="javascript:;" class="curr">我的日程</a></li>
	    </ul>
</div>

<div id="continer">
  <div id='calendar'></div>
  <div id="add" class="form1"></div>  
</div>

<script>
$(document).ready(function() {
	    var date = new Date();
	    var defaultDate='';	
	    if('${startTime}'!=null&&'${startTime}'!=''){
	    	defaultDate='${startTime}';
	    }else{
	    	var currMon=Number(date.getMonth()+1);
	    	if(currMon>=9){
	    		defaultDate=date.getFullYear()+"-"+currMon+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+".0";
	    	}else{
	    		defaultDate=date.getFullYear()+"-0"+currMon+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+".0";
	    	}
	    	 
	    } 
		$('#calendar').fullCalendar({
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			eventSources:'${ctx}/agendaController/queryAll',
			defaultDate: defaultDate,   
			contentHeight:576,
			timeFormat: 'H:mm',
			editable: true,
			eventLimit: true, 
			selectable: true,
			selectHelper: true,
			select: function(start, end) {
				$('#add').window({
					title:'添加日程',
					width:650,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					href:'${ctx}/agendaController/addUI'
				});
			},
			eventClick:function(event, jsEvent, view){
				$('#add').window({
					title:'日程详情',
					width:650,
					collapsible:false,
					minimizable:false, 
					maximizable:false,
					href:'${ctx}/agendaController/detailUI?fid='+event.fid
				});
				return false;
			}
		});
		
		$('.fc-icon-left-single-arrow').css('display','none');
		$('.fc-icon-right-single-arrow').css('display','none');
		$('.fc-prev-button').css('width','40px');
		$('.fc-next-button').css('width','40px'); 
		$('.fc-prev-button').css('font-weight','bolder');
		$('.fc-next-button').css('font-weight','bolder');
		$('.fc-prev-button').append('<'); 
		$('.fc-next-button').append('>');
});
</script>
</body>
</html>