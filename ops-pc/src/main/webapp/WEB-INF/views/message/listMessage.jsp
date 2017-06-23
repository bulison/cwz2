<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>list message 4</title>
<style>
.warp-box{
	margin: 0px;
	padding:0px;
}
.fool .panel-title {
  margin:0px !important;
  margin-left:35px !important;
  text-align: left;
  line-height: 20px;
}
.fool .panel-title span{
	font-size: 12px;
	line-height: 35px !important;
}
.fool .accordion .accordion-body{
	background-color: #F5F5F5;
}
</style>
</head>

<body>
<div class="nav-box">
	<ul>
	   	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	       <li><a href="javascript:;" class="curr">消息中心</a></li>
	</ul>
</div>

<div class="tool-box">
	<div class="tool-box-pane">
		<input id="search" class="easyui-searchbox easyui-validatebox" name="search-box" data-options="validType:'normalChar',prompt:'输入关键字查询',iconWidth:28,width:'220px',height:'39px',searcher:search"/>
	</div>
		
	<div class="tool-box-pane tool-box-where" style="margin-left:230px;">
			<div class="split-right" id="typeTag">
				<a href="javascript:;" class="curr" val="-1">全部</a>
          		<a href="javascript:;" val="0">提醒</a>
          		<a href="javascript:;" val="5">待办</a>
          		<a href="javascript:;" val="6">告警</a>
			</div>
			<div class="tool-box-link" id="statusTag">
				<a href="javascript:;" class="curr" val="-1">全部</a>
	            <a href="javascript:;" val="1">已读</a>
	        	<a href="javascript:;" val="0">未读</a>
			</div>
		</div>
	</div>

<div class="warp-box fool">	
	<div id="msgPanel" class="easyui-accordion" data-options="multiple:false"></div>
	<div id="showPanel"></div>
	<div id="loadMore" style="text-align: center;">
		<a href="javascript:void(0)" class="btn-blue" style="width: 70%;" onclick="loadMore()" id="loadMoreBtn">更多</a>
		<span id="nomore" style="display:none;font-size:12px;">没有了..</span>
	</div>	
</div>

</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
$(function(){
	
	//选择类型条件后重新加载列表
	$("#typeTag a").click(function(){
		var typeTag = $('#typeTag .curr').attr('val');
		var curr = $(this).attr('val');
		if(typeTag==curr)return;
		$(this).addClass('curr').siblings('a').removeClass('curr');
		reset();
		refreshData(false);
	});
	
	//选择状态条件后重新加载列表
	$("#statusTag a").click(function(){
		var statusTag = $('#statusTag .curr').attr('val');
		var curr = $(this).attr('val');
		if(statusTag==curr)return;
		$(this).addClass('curr').siblings('a').removeClass('curr');
		reset();
		refreshData(false);
	});
	
	//页面加载完成后加载数据
	refreshData(false);
	
	//注册选择手风琴消息
	$('#msgPanel').accordion({
		onSelect:function(title){
			var curSel = $('#msgPanel').accordion('getSelected');
			var messageId = $(curSel).panel('body').find('.messageid').html();
			var status = $(curSel).panel('body').find('.messagestatus').html();
			if(status=="0"){
				readMessage(messageId,$(curSel));
			}
		}
	});
});

var pageNo=1;
var pageSize=8;

function reset(){
	pageNo=1;
	pageSize=8;
}

function loadMore(){
	pageNo++;
	refreshData(true);
}

//关键字搜索
function search(keyword){
	reset();
	refreshData(false,keyword);
}

//加载、刷新数据
function refreshData(append,keyword){
	var typeTag = $('#typeTag .curr').attr('val');
	var statusTag = $('#statusTag .curr').attr('val');
	var url = "${ctx}/message/queryMessage";
	if(keyword==undefined)keyword='';
	$.ajax({
	    type: 'post',
	    url: url ,
	    dataType: 'json',
	    data: {
	    	type:typeTag,
	    	status:statusTag,
	    	page:pageNo,
	    	rows:pageSize,
	    	title:keyword
	    },
	    success: function(data){
	    	if(!append||append==undefined){
	    		clearAccordion();
	    	}
	    	if(data.rows.length<pageSize){
	    		$('#nomore').show();
	    		$('#loadMoreBtn').hide();
	    	}else{
	    		$('#nomore').hide();
	    		$('#loadMoreBtn').show();
	    	}
	    	for(var i=0;i<data.rows.length;i++){
	    		addAccordion(data.rows[i]);
	    	}
	    }
	});
}

//添加一个Accordion
function addAccordion(data){
	var paramId = data.busId;
	var btnHtml = '';
	if(data.operTime == undefined || data.operTime == ''){
		btnHtml += '<p id="btn'+data.fid+'">';
		var optData = operationer(data);
		if(optData != undefined){
			var btns = optData.btn;
			for(var i=0;i<btns.length;i++){
				
				var busParamObj = JSON.parse(data.busParamObj);
				var params = generateParam(btns[i].paramMap, busParamObj, paramId);
				
				btnHtml+=genBtn(btns[i],paramId,data.fid,params);
				btnHtml+='&nbsp;';
			}	
		}
		btnHtml += '</p>';
	}
	var content = '';
	if(data.content != undefined) content = data.content;
	
	var message = '<span class="messageid" style="display:none;">'+data.fid+'</span>';//消息主键
		message += '<span class="messagestatus" style="display:none;">'+data.status+'</span>';//消息状态
	var isbold = '';
	if(data.status==1){
		isbold='font-weight:normal;';//消息是否加粗显示标题
	}else if(data.status==0){
		isbold='font-weight:bold;';
	}
	var title = '<span class="boldtitle" style="'+isbold+'">'+data.title+'</span><span style="font-size:12px;color:#979797;padding-left:15px;">'+data.sendTime+'</span>';
	
	$('#msgPanel').accordion('add',{
		title:title,
		cls:'accd',
		selected: false,
		content: '<div>'+content+btnHtml+message+'</div>'
	});
}

//生成url参数
function generateParam(paramMap, busParamObj,busId){
	var param = '';
	var paramMapJson = JSON.parse(paramMap);
	for(var name in paramMapJson) {
		var val = paramMapJson[name];
		param += '&';
		param += name;
		param += '=';
		if(val=='BUSID'){
			param += busId;
		}else{
			param += busParamObj[name];
		}
	}
	
	return param;
}

//读消息，把消息设置为已读
function readMessage(messageId,curSel){
	$.ajax({
	    type: 'POST',
	    url: '${ctx}/message/read',
	    dataType: 'json',
	    data: {
	    	messageId:messageId
	    },
	    success: function(data){
	    	//修改显示样式
	    	if(curSel){
	    		curSel.panel('body').find('.messagestatus').html('1');//设置已读
	    		curSel.panel('header').find('.boldtitle').css('font-weight','normal');//去除标题加粗
	    	}
	    }
	});
}

//操作消息，把消息设置为已操作
function operMessage(messageId){
	$.ajax({
	    type: 'POST',
	    url: '${ctx}/message/oper',
	    dataType: 'json',
	    data: {
	    	messageId:messageId
	    },
	    success: function(data){
	    	//隐藏按钮
	    	$('#btn'+messageId).hide(100);
	    }
	});
}

//生成按钮
function genBtn(btn,paramId,msgid,params){
	var html = '';
	if(params==undefined)params='';
	if(btn.type=='ajax'){
		html+='<a href="javascript:void(0)" class="btn-green2 btn-s" onclick="ajaxRequest(\''+btn.url+'\',\''+paramId+'\',\''+msgid+'\',\''+params+'\')">'+btn.name+'</a>';
	}else if(btn.type=='dialog'){
		html+='<a href="javascript:void(0)" class="btn-gray btn-s" onclick="showDialog(\''+btn.url+'\',\''+paramId+'\',\''+msgid+'\',\''+params+'\')">'+btn.name+'</a>';
	}else if(btn.type=='link'){
		html+='<a href="'+btn.url+'?msgid=' + msgid + params + '" class="easyui-linkbutton">'+btn.name+'</a>';
	}
	
	return html;
}

//展示对话框
function showDialog(url,paramId,msgid,params){
	$('#showPanel').dialog({
			title : '请填写具体信息',
			width : 700,
			height : 600,
			cache : false, 
			href : url+'?paramId='+paramId+'&messageId='+msgid+params,
			modal : true,
			resizable : true
	}).dialog('open');
}

//异步请求
function ajaxRequest(url,paramId,msgid,params){
	$.ajax({
	    type: 'GET',
	    url: url ,
	    dataType: 'json',
	    data: {
	    	paramId:paramId,
			messageId:msgid
	    },
	    success: function(data){
	    	if(data.result=='0'){
	    		showMessage('温馨提示','亲~操作成功');
	    		//隐藏按钮
		    	$('#btn'+msgid).hide(100);
	    	}
	    },
	    error: function(){
	    	showMessage('温馨提示','亲~系统繁忙，请稍后再试');
	    }
	});
}

//展示信息
function showMessage(t,message){
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

//清除所有的Accordion
function clearAccordion(){
	var allPanels = $('#msgPanel').accordion('panels');
	$(allPanels).each(function(index,ele){
		var title = $(this).panel('options').title;
		$('#msgPanel').accordion('remove',title);
	});
}

//获得按钮集合
function operationer(data){
	if(data.busParamObj == undefined || $.trim(data.busParamObj) == ''){
		return undefined;
	}
	
	var busClass = data.busClass;
	var busParamObj = JSON.parse(data.busParamObj);
	var busStatus = busParamObj.status;
	var level = busParamObj.level;
	var key = '';
	if(level != undefined && level == '1'){
		//一级事件
		key = busClass + '_' + busStatus + "_" + level;
	}else{
		//计划、二级以上事件
		key = busClass + '_' + busStatus;
	}
	var btns = classStatus[key];
	return btns;
}

var classStatus={};
${operationMap}

//按钮对象定义
function Btn(name,url,type,paramMap) {
	var obj = new Object();
	obj.name=name;
	obj.url='${ctx}/'+url;
	obj.type=type;//type有ajax、dialog、link三种
	obj.paramMap=paramMap;
	return obj;
}
</script>