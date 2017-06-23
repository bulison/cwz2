<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>帮助文档</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<style>
.form1 p font{
	width:auto;
}
.form1{
	padding-left:100px;
}
.fj{
    width: 200px;
    border: 1px solid #ccc;
    height: 20px;
    padding: 0 0 10px 0;
    margin-right:20px;
    display:inline-block;
    border-radius:3px;
    vertical-align: middle;
}
.filename{
    display: inline-block;
    font-size: 10px;
    position: absolute;
    width:90px;
    margin: 10px 10px;
    white-space:nowrap;
    text-overflow:ellipsis;
    overflow:hidden;
}
.fj a{
	display: inline-block;
    margin-top: 5px;
    position: absolute;
    margin-left: 110px;
}
.fj .btn-s{
	height:20px;
	line-height:20px;
}
.fj font{
	width:auto;
}
.fj p{
	margin-left:30px;
}
</style>
</head>
<body>
		<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>帮助文档</h1>
                </div>             
             </div>
    <div class="form1">
    	<p><font>商业智能管理系统（1期）使用手册——仓储模块（2017-05）：</font>
	    	<div class="fj">
	    		<span style="margin-left:5px;display: inline-block;">
	    			<img src="${ctx}/resources/images/word.png" style="width:30px;height:30px;">
	    		</span>
	    		<span class="filename" title="商业智能管理系统（1期）使用手册——仓储模块（2017-05）.doc">
	    			商业智能管理系统（1期）使用手册——仓储模块（2017-05）.doc
	    		</span>
	    		<a onclick="myurl(1)" class="btn-s btn-blue">下载</a>
	    	</div>
    	</p><br/>
    	<p><font>商业智能管理系统（1期）使用手册——财务模块（2017-05）：</font>
    		<div class="fj">
    		<span style="margin-left:5px;display: inline-block;">
    			<img src="${ctx}/resources/images/word.png" style="width:30px;height:30px;">
    		</span>
    		<span class="filename" title="商业智能管理系统（1期）使用手册——财务模块（2017-05）.docx">
    			商业智能管理系统（1期）使用手册——财务模块（2017-05）.docx
    		</span>
    		<a onclick="myurl(2)" class="btn-s btn-blue">下载</a>
    	</div>
    	</p><br/>
    	<p><font>商业智能管理系统（1期）使用手册——系统设置：</font>
    		<div class="fj">
    		<span style="margin-left:5px;display: inline-block;">
    			<img src="${ctx}/resources/images/word.png" style="width:30px;height:30px;">
    		</span>
    		<span class="filename" title="商业智能管理系统（1期）使用手册——系统设置.docx">
    			商业智能管理系统（1期）使用手册——系统设置.docx
    		</span>
    		<a onclick="myurl(3)" class="btn-s btn-blue">下载</a>
    	</div>
    	</p><br />
    	<p><font>商业智能管理系统（1期）使用手册——报价系统移动端（APP）：</font>
    		<div class="fj">
    		<span style="margin-left:5px;display: inline-block;">
    			<img src="${ctx}/resources/images/word.png" style="width:30px;height:30px;">
    		</span>
    		<span class="filename" title="商业智能管理系统（1期）使用手册——报价系统移动端（APP）.docx">
    			商业智能管理系统（1期）使用手册——报价系统移动端（APP）.docx
    		</span>
    		<a onclick="myurl(4)" class="btn-s btn-blue">下载</a>
    	</div>
    	</p><br />
		<p><font>流程管理操作指南2017-05-10.docx：</font>
		<div class="fj">
    		<span style="margin-left:5px;display: inline-block;">
    			<img src="${ctx}/resources/images/word.png" style="width:30px;height:30px;">
    		</span>
			<span class="filename" title="商业智能管理系统（1期）使用手册——报价系统移动端（APP）.docx">
    			流程管理操作指南2017-05-10.docx
    		</span>
			<a onclick="myurl(5)" class="btn-s btn-blue">下载</a>
		</div>
		</p>
    </div>
<script>
//跳转下载
function myurl(type){
	var name = "";
	if(type == 1){
		name = encodeURI(encodeURI("商业智能管理系统（1期）使用手册——仓储模块（2017-05）.doc"));
	}else if(type == 2){
		name = encodeURI(encodeURI("商业智能管理系统（1期）使用手册——财务模块（2017-05）.docx"));
	}else if(type == 3){
		name = encodeURI(encodeURI("商业智能管理系统（1期）使用手册——（系统设置）.docx"));
	}else if(type == 4){
		name = encodeURI(encodeURI("商业智能管理系统（1期）使用手册——报价系统移动端（APP）.docx"));
	}else if(type == 5){
        name = encodeURI(encodeURI("流程管理操作指南2017-05-10.docx"));
    }
	var url = "${ctx}/doc/downDoc?downFile="+name;
	window.location.href = url;
}
</script>
</body>
</html>