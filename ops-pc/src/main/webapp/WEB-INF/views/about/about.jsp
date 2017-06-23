<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>佛山蠢材科技有限公司</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<style>
ul{list-style-type: none;width:200px;}
li{ border:#CCC solid 1px; border-bottom:none; width:200px; height:45px; text-align:center; background-color: white;}
li a{font-size:15px; text-decoration:none; color:#000; line-height:50px;padding:10px 55px 10px 75px} 

.selected{background:url(${ctx}/resources/images/left_on.png) no-repeat;}
.curr{color:#5DAEE3; font-weight:bold;}
.about{background: url(${ctx}/resources/images/about.png) no-repeat 25px 6px} 
.about_on{background: url(${ctx}/resources/images/about_on.png) no-repeat 25px 6px;}

.contact{background: url(${ctx}/resources/images/contact.png) no-repeat 25px 6px}
.contact_on{background: url(${ctx}/resources/images/contact_on.png) no-repeat 25px 6px;}

.recruitment{background: url(${ctx}/resources/images/person.png) no-repeat 25px 6px}
.recruitment_on{background: url(${ctx}/resources/images/person_on.png) no-repeat 25px 6px;}

.report{background: url(${ctx}/resources/images/activity.png) no-repeat 25px 6px}
.report_on{background: url(${ctx}/resources/images/activity_on.png) no-repeat 25px 6px;}

.link{background: url(${ctx}/resources/images/link.png) no-repeat 25px 6px}
.link_on{background: url(${ctx}/resources/images/link_on.png) no-repeat 25px 6px;}

.body{margin:0px auto;width:960px;margin-top:16px}
.header{
	background: #FFF;
	height:80px;
	overflow:hidden;
	border:solid 1px #CCCCCC;
	margin:0px; 
}
.header-logo{
	width:350px;
	margin-left:250px;
}
.right{
    width: 730px;
    background: white;
    border: 1px solid #CCC;
}

.textArea{ border:none;font-size:14px;width:630px;resize:none;line-height:40px;margin-top:20px;background-color:white !important;overflow:hidden;}
.img{float:left;margin-left:35px;margin-top:55px;margin-bottom:10px;clear:both;} 
</style>
</head>
<body>
<div class="header">
<div class="header-logo"><a id="logo_link" href="#" ><img id="logo" src="${ctx}/resources/images/login_logo.png" style="border: none"/></a></div>
</div>
 
<div class="body">
<div class="left">
<ul>
<li><a href="javascript:;" class="about">关于我们</a></li>
<li><a href="javascript:;" class="contact">联系我们</a></li>
<li><a href="javascript:;" class="recruitment">人才招聘</a></li>
<li style="border-bottom:1px solid #CCC"><a href="javascript:;" class="report">活动报道</a></li>
<!-- <li style="border-bottom:1px solid #CCC"><a href="javascript:;" class="link">友情链接</a></li> --> 
</ul>
</div>

<div class="right">
</div>
</div>

<script>
$(function(){
	if('${site}'=='1'){
		$('.right').load("${ctx}/aboutController/aboutUs");
		$('.about').parent().attr('class','selected');
		$('.about').attr('class','about_on curr');
	}else if('${site}'=='2'){
		$('.right').load("${ctx}/aboutController/contractUs");
		$('.contact').parent().attr('class','selected');
		$('.contact').attr('class','contact_on curr');
	}else if('${site}'=='3'){
		$('.right').load("${ctx}/aboutController/recruitment");
		$('.recruitment').parent().attr('class','selected');
		$('.recruitment').attr('class','recruitment_on curr');
	}else if('${site}'=='4'){
		$('.right').load("${ctx}/aboutController/activityReport");
		$('.report').parent().attr('class','selected');
		$('.report').attr('class','report_on curr');
	}else if('${site}'=='5'){
		$('.right').load("${ctx}/aboutController/friendSite");
		$('.link').parent().attr('class','selected');
		$('.link').attr('class','link_on curr');
	};
	
});

  $("a").click(function(e) {
    if($(this).attr('class')=='about'){
		$(this).attr('class','about_on curr');
		$('.contact_on').attr('class','contact');
		$('.recruitment_on').attr('class','recruitment');
		$('.report_on').attr('class','report');
		$('.link_on').attr('class','link');
		$('li').removeAttr('class');
		$(this).parent().attr('class','selected');
		$('.right').load("${ctx}/aboutController/aboutUs");
	}else if($(this).attr('class')=='contact'){
		$(this).attr('class','contact_on curr');
		$('.about_on').attr('class','about');
		$('.recruitment_on').attr('class','recruitment');
		$('.report_on').attr('class','report');
		$('.link_on').attr('class','link');
		$('li').removeAttr('class');
		$(this).parent().attr('class','selected');
		$('.right').load("${ctx}/aboutController/contractUs");
	}else if($(this).attr('class')=='recruitment'){
		$(this).attr('class','recruitment_on curr');
		$('.contact_on').attr('class','contact');
		$('.about_on').attr('class','about');
		$('.report_on').attr('class','report');
		$('.link_on').attr('class','link');
		$('li').removeAttr('class');
		$(this).parent().attr('class','selected');
		$('.right').load("${ctx}/aboutController/recruitment");
	}else if($(this).attr('class')=='report'){
		$(this).attr('class','report_on curr');
		$('.contact_on').attr('class','contact');
		$('.about_on').attr('class','about');
		$('.recruitment_on').attr('class','recruitment');
		$('.link_on').attr('class','link');
		$('li').removeAttr('class');
		$(this).parent().attr('class','selected');
		$('.right').load("${ctx}/aboutController/activityReport");
	}else if($(this).attr('class')=='link'){
		$(this).attr('class','link_on curr');
		$('.contact_on').attr('class','contact');
		$('.about_on').attr('class','about');
		$('.recruitment_on').attr('class','recruitment');
		$('.report_on').attr('class','report');
		$('li').removeAttr('class');
		$(this).parent().attr('class','selected');
		$('.right').load("${ctx}/aboutController/friendSite");
	}
});
  
  $('#logo_link').click(function(){ 
	  location.href='${ctx}/login/login';
	});
</script>
</body>
</html>