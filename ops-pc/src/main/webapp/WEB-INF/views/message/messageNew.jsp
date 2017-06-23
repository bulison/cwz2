<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<title>消息中心</title>
<%-- <link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" /> --%>
<style>
.bill-form{
	padding:10px 0;
}
.bill-form h1{
	font:800 16px 宋体  !important;
	color:#50b3e7;
	margin-left:60px;
	height:18px;
}
.bill-form #title{
	position:fixed;/* position: absolute; */
	top:0px;/* top:26px; */
	background:#F0F0F0;
	width:100%; /*写给不兼容的浏览器*/
	width:calc(100% - 40px);
	width:-moz-calc(100% - 40px);
	width:-webkit-calc(100% - 40px);
	padding:0 20px;
	z-index:8999;
}
#title #square1{
	background:#50b3e7;
	width:30px;
	height:40px;
	position:absolute;
	margin-top:-11px;
}
#square1 #triangle{
	width: 0;
	height: 0;
	border-top: 5px solid transparent;
	border-left: 10px solid #50b3e7;
	border-bottom: 5px solid transparent;
	margin-left:30px;
	margin-top:15px;
}
.shadow{
	background:#fff;
	padding:5px 0;
	margin:0px 20px 10px 20px;
	-moz-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	-o-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	-ms-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	box-shadow: 0 2px 7px rgba(0,0,0,0.3);
}
/**/
.msgList{
	margin: 10px 0;
    text-align: left;
    font-size: 13px;
}
#myMain{
	padding:5px 0;
	margin:50px 20px 10px 20px;
}
.msgTitle{
	background: #fff;
    padding: 15px 0px 15px 45px;
    border-radius: 5px;
    cursor: pointer;
}
.msgTitle .msgTime{
	margin-left:30px;
	color:#999999;
}
/* .msgFlag{
	color:#fff;
	font-weight:600;
} */
.msgTitle div,.msgTitle span{
	display:inline-block;
}
.msgFname{
	float:right;
}
.msgList .msgContent{
	border-bottom-left-radius:5px;
	border-bottom-right-radius:5px;
	background:#f7f7f7;
	padding:25px 40px;
}
/* #title1{
	padding:0px;
	margin-top:50px;
} */
.msgHead{
	padding:6px 20px;
	background:#f7f7f7;
	border:1px #ddd solid;
	height: 18px;
}
.msgHead h1{
	color:#fff;
	margin-left:10px;
	float:left;
}
.msgIcon{
	 right:0;
	 float:right;
}
.msgBtn{
	margin-top:10px;
}
.msgBtn a{
	margin-left:15px;
}
.btn-green{
	background: #a1e040;
    border-color: #a1e040;
}
.btn-grey{
	background: #c9c9c9;
    border-color: #c9c9c9;
}
.btn-type{
	margin:0 15px;
	padding:6px 14px;
	color:#666;
}
.btn-type:hover{
	text-decoration:none;
}
.msgHead .choose{
	background: #4fb4e7;
    width: 60px;
    height: 2px;
    position: absolute;
    left: 56px;
    top: 95px;
}
.Listbox{
	margin: 5px 0px;
    overflow-y: auto;
    /* height: 170px; */
}
.msgList .squareMy{
	background:#f5d379;
	width:5px;
	height:45px;
	position:relative;
	margin-bottom:-45px;
	border-top-left-radius:5px;
	border-bottom-left-radius:5px;
}
.search-box .triangle{
	width: 0;
    height: 0;
    border-left: 5px solid transparent;
    border-top: 10px solid #ccc;
    border-right: 5px solid transparent;
    margin: 5px;
}
.search-box{
	background:#fff;
	width:130px;
	height:20px;
	padding:0 0 0 15px;
	line-height:20px;
	font-size:14px;
	color:#999;
	margin-top:-2px;
	margin-right:30px;
	border:1px solid #ccc;
	float:right;
	display:inline-block;
	cursor:pointer;
}
.search-box:hover .triangle{
    border-top: 10px solid #4fb4e7;
}
.search-box:hover .searchIcon{
    border: 1px solid #4fb4e7;
}
.search-box:hover{
	background:#f7f7f7;
}
.search-box .searchIcon{
	right:0;
	float:right;
	width:18px;
	height:18px;
	line-height:18px;
	border:1px solid #ccc;
}
.search-main{
	display:none;
	width: 220px;
    background: #fff;
    position:absolute;
    border: 1px solid #ccc;
    font-size:14px;
	color:#999;
    padding: 10px 20px;
    z-index:9000;
    right: 20px;
    margin-top: 5px;
}
.search-main p{
	margin-top:5px;
}
#loadTips{
	width: 80%;
    height: 30px;
    border: 1px #FF7F50 solid;
    background: #FAFAD2;
    border-radius: 20px;
    text-align: center;
    line-height: 30px;
    margin: 0 auto 10px auto;
    color: #3B3B3B;
    cursor:pointer;
}
#loadTips:hover{
	background:#EEEED1;
}
#loading{
    position: fixed;
    width: 100%;
    text-align: center;
    top: 250px;
}
</style>
<link rel="stylesheet" href="${ctx}/resources/css/messageNew.css" />
</head>
<body>

<div action="" class="bill-form myform" id="form">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><div id="triangle"></div></div><h1>消息中心</h1>
		</div>
	</div>
	<div id="myMain" >
		<div class="msgHead">
			<div style="display:inline-block;">
				<a href="javascript:;" val="-1" ondblclick="searchDbl(-1)" onclick="searchType(-1)" class="btn-type btn-all btn-select">全部</a>
				<a href="javascript:;" val="1" ondblclick="searchDbl(1)" onclick="searchType(1)" class="btn-type  btn-task">事务</a>
				<a href="javascript:;" val="0" ondblclick="searchDbl(0)" onclick="searchType(0)" class="btn-type  btn-msg">消息</a>
				<a href="javascript:;" val="2" ondblclick="searchDbl(2)" onclick="searchType(2)" class="btn-type  btn-msg">预警</a>
				<span class="choose"></span>
			</div>
			<div class="search-box" mouseover="javascript:;">
				请选择筛选内容
				<span class="searchIcon">
						<div class="triangle"></div>
				</span>
			</div>
			<form class="search-main">
					<p><font>状态：</font><input type="text" class="textBox" name="status" id="status" /></p>
					<p><font>标题：</font><input type="text" class="textBox" name="title" id="seTitle" /></p>
					<br/>
					<a href="javascript:;" class="btn-blue btn-s" id="search-form-btn" onclick="refreshData()" style="vertical-align:middle;">查询</a>
					<a href="javascript:;" class="btn-blue btn-s" onclick="cleanBox('.search-main')" style="vertical-align:middle;">清空</a>
			</form>
		</div>
		<div class="Listbox" id="list2">
		</div>
		<div id="loading"><img src="${ctx}/resources/images/loading.gif" style="width:50px;height:auto;"/></div>
		<div id="loadTips" style="display:none;">点击获取更多消息</div>
		<div id="pageSet"></div>
	</div>
</div>
<script type="text/javascript">
var myjson = '';
var loadtips = 0;//点击获取更多消息的标识
var myAccId = "${accId}"
$(function(){
	searchType(-1);
	$('#status').combobox({
		width:160,
		height:26,
		editable:false,
		data:[{
				value:0,
				text:"未读"
		  	  },
		      {
		    	value:1,
				text:'已读'
			  },
			  {
				value:2,
				text:"停用"
			  }]
	}).combobox('clear');
	$('#seTitle').textbox({
		width:160,
		height:26,
	});
});

//清空
function cleanBox(obj){
	$(obj).form('clear');
}
//查询
function refreshData(){
	var searchData = $('.search-main').serializeJson();
	searchList(searchData,false);
}

function loadmore(){
	loadtips = 1;
	var npage = myjson.page + 1;
	/*myjson = $.extend(myjson,{page:npage});
	$.post(getRootPath()+'/message/query',myjson,function(data){
		if(data){
			loadMsg(data,false);
			$('#pageSet').find('.l-btn-selected').removeClass('l-btn-selected l-btn-plain-selected').next().addClass('l-btn-selected l-btn-plain-selected');
			data.total-10*myjson.page>0?$('#loadTips').attr('onclick','loadmore()').show():$('#loadTips').hide();
		}else{
			return false;
		}
	}); */
	$('#pageSet').pagination('select',npage);
}

//加载数据到页面上
function loadMsg(mdata,bool){
	if(bool){
		$('#list2').html('');
	}
	if(typeof mdata != "object"){
		return false;
	}
	var data = mdata.rows;
	var max = mdata.rows.length;
	for(var i=0; i<max; i++ ){
		/* if(myAccId != data[i].accId){
			continue;
		} */
		var style = data[i].status == 0 ?'font-weight:700;':null;
		var mymsg = '<div class="msgList" msg-index="'+i+'">'+
			'<div class="squareMy"></div>'+
			'<div class="msgTitle" onClick="msgClick(this)")>'+
				'<div class="msgId" style="display:none;">'+data[i].fid+'</div>'+
				'<div class="senderName" style="display:none;">'+data[i].senderName+'</div>'+
				'<div class="receiverName" style="display:none;">'+data[i].receiverName+'</div>'+
				'<div class="msgStatus" style="display:none;">'+data[i].status+'</div>'+
				'<div class="msgBus" style="display:none;">'+data[i].busParamObj+'</div>'+
				'<div class="msgAccId" style="display:none;">'+data[i].accId+'</div>'+
				'<div class="msgOpert" style="display:none;">'+data[i].operTime+'</div>'+
				'<div class="msgTriggerType" style="display:none;">'+data[i].triggerType+'</div>'+
				'<div class="msgType" style="display:none;">'+data[i].type+'</div>'+
				'<div class="msgMain" style="'+style+'">'+data[i].title+'</div>'+
				'<div class="msgTime">'+data[i].sendTime+'</div>'+
				'<span class="msgIcon">'+
					'<a class="btn-hide"></a>'+
				'</span>'+
			'</div>'+
			'<div class="msgContent" style="display:none;">'+
				'<div class="msgc">'+
				data[i].content+
				'</div>'+
				'<div class="msgBtn">'+
					'<a class="msgbtn1 btn-green btn-s" href="javascript:;" style="display:none;vertical-align:middle;"></a>'+
					'<a class="msgbtn2 btn-grey btn-s" href="javascript:;" style="display:none;vertical-align:middle;"></a>'+
					'<a class="msgbtn3 btn-grey btn-s" href="javascript:;" style="display:none;vertical-align:middle;"></a>'+
					'<a class="msgbtn4 btn-green btn-s" href="javascript:;" style="display:none;vertical-align:middle;"></a>'+
				'</div>'+
				/* '<div class="msgFname">'+
				'所属账套：'+data[i].accName+
				'</div>'+ */
			'</div>'+
		'</div>';
		$('#list2').append(mymsg);
	}
}

function setPager(jqTableObj,total,json){
	if(!jqTableObj)return;
	$(jqTableObj).pagination({
		total:total,
		pageSize:10,
		layout:['first','prev','links','next','last'],
		displayMsg:'{total}条记录&nbsp;&nbsp;{currPage}/{pages}页',
		onSelectPage:function(pageNumber,pageSize){
			var myjson = $.extend(json,{page:pageNumber});
			searchList(myjson,true);
		}
	});
}

//双击类型清空更多选项并搜索
function searchDbl(tips){
	$('.search-main').form('clear');
	searchType(tips);
}

//点击类型的搜索方法
function searchType(tips){
	var mydata = $('.search-main').serializeJson();
	mydata = $.extend(mydata,{type:tips,page:1});
		$.ajax({
			url:getRootPath()+'/message/query?accId='+myAccId,
			type:'post', 
			data:mydata,
			beforeSend:function(){
				$('#loading').show();
			},
			success:function(data){
				//var msgBus = $.parseJSON(data.rows[0].busParamObj)[0];
				//parent.kk('/flow/plan/axis?mark=1&id='+msgBus.busData,'浏览计划');
				
				loadMsg(data,true);
				myjson = mydata;
				if(typeof data != "object"){
					setPager('#pageSet',0,mydata);
					return false;
				}
				setPager('#pageSet',data.total,mydata);
				$('#pageSet').pagination('select',1);
				data.total>10?$('#loadTips').attr('onclick','loadmore()').show():$('#loadTips').hide();
			},
			complete:function(data){
	            $("#loading").hide();
	        }, 
			error:function(data){
				$("#loading").hide();
			}
		});
}

//点击更多选项查询数据搜索方法
function searchList(json,bool){
	var type = $('.btn-select').attr('val');
	var mydata = $.extend(json,{type:type});
	if(!bool){
		mydata = $.extend(mydata,{page:1});
	}
	$.ajax({
		url:getRootPath()+'/message/query?accId='+myAccId,
		data:mydata,
		beforeSend:function(){
			$('#loading').show();
		},
		success:function(data){
			loadtips == 1?loadMsg(data,false):loadMsg(data,true);
			loadtips = 0;
			myjson = json;
			if(!bool){
				setPager('#pageSet',data.total,json);
				data.total>10?$('#loadTips').attr('onclick','loadmore()').show():$('#loadTips').hide();
				$('#pageSet').pagination('select',1);
			}else{
				data.total-10*json.page>0?$('#loadTips').attr('onclick','loadmore()').show():$('#loadTips').hide();
			}
		},
		complete:function(data){
            $("#loading").hide();
        }, 
		error:function(data){
			$("#loading").hide();
		}
	});
}

$('.btn-type').click(function(){
	$('.btn-type').removeClass('btn-select');
	$('.choose').animate({'left':$(this).offset().left},300);
	$(this).addClass('btn-select');
});

$('.search-box').click(function(){
	$('.search-main').slideToggle(300);
});

//设置搜索框和下拉框阻止冒泡，然后设置整个界面的点击事件
$(".search-main").click(function(event){
	event.stopPropagation();
}); 
$(".search-box").click(function(event){
	event.stopPropagation();
});
$(document).click(function(){
	$('.search-main').slideUp(300);
});

function getBtnobj(index,num){
	return $('.msgList[msg-index='+index+']').find('.msgbtn'+num);
}

function oprbyUrl(type,url,index,id,tips,aCode,rank){
	var mydata = '';
	if(type == "101"){
		mydata = {"id":id};
		mydata = $.extend(mydata,{auditResult:aCode});
	}else if(type == "102"||type == "103"||type == "104"||type == "105"){
		mydata = {"id":id};
	}else if(type == "42" || type == "20"){
		mydata = {"fid":id};
		mydata = $.extend(mydata,{checkResult:aCode});
	}else{
		mydata = {"fid":id};
	}
	var mybtn = $('.msgList[msg-index='+index+']').find('.msgBtn');
	if(tips == 0){
		$.post(url,mydata,function(data){
			if(data.returnCode == 0){
				mybtn.hide();
			}else if(data.returnCode == 1){
				$.fool.alert({msg:data.message,fn:function(){
					
				}});
			}else{
				$.fool.alert({msg:'服务器繁忙，请稍后再试',fn:function(){
					
				}});
			}
		});
	}else if(tips == 1){//查看计划
		if(rank == 1) {
			parent.kk('/flow/plan/planView?busType='+aCode+'&rankFlag=1&id='+id,'查看评分');
		}else {
			if(aCode == 0)
				parent.kk('/flow/plan/planView?busType='+aCode+'&rankFlag=0&id='+id,'查看计划');
			else if(aCode == 1)
				parent.kk('/flow/plan/planView?busType='+aCode+'&rankFlag=0&id='+id,'查看事件');
		}
	}
}

function setMsgJson(msgBus){
	if(msgBus.busClass&&msgBus.busClass.search(/Plan/)>0){
		var url=getRootPath()+'/flow/plan/';
		switch(msgBus.busScene){
		case "101":
			url = url + 'audit';
			msgBus = $.extend(msgBus,{url:url});
		break;
		case "102":
			if(msgBus.type == "41"){
				url = url + 'complete';
			}else{
				url = url + 'listPage';
			}
			msgBus = $.extend(msgBus,{url:url});
		break;
		case "103":
			url = url + 'axis';
			msgBus = $.extend(msgBus,{url:url});
		break;
		case "104":
			url = url + 'axis';
			msgBus = $.extend(msgBus,{url:url});
		break;
		case "105":
			url = url + 'axis';
			msgBus = $.extend(msgBus,{url:url});
		break;
		}
	}else if(msgBus.busClass&&msgBus.busClass.search(/Task/)>0){
		url=getRootPath()+'/flow/task/';
		switch(msgBus.type){
		case "40":
			url = "undefined" != typeof msgBus.childSize && msgBus.childSize>0?url + 'assignTask':url + 'startExcute';
			msgBus = $.extend(msgBus,{url:url});
		break;
		case "42":
			url = url + 'checkExcute';
			msgBus = $.extend(msgBus,{url:url});
		break;
		case "20":
			url = url + 'checkApplyDelay';
			msgBus = $.extend(msgBus,{url:url});
		break;
		}
	}
	return msgBus;
}

function btnShowUrl(msgBus){
	var index = msgBus.index;
	if(msgBus.busClass&&msgBus.busClass.search(/Plan/)>0){
		if(msgBus.busScene == "101"){
			getBtnobj(index,1).attr('onclick','oprbyUrl('+msgBus.busScene+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",0,0)').html('通过').show();
			getBtnobj(index,2).attr('onclick','oprbyUrl('+msgBus.busScene+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",0,1)').html('不通过').show();
			getBtnobj(index,3).attr('onclick','oprbyUrl('+msgBus.busScene+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",0,3)').html('终止').show();
			//getBtnobj(index,4).attr('onclick','oprbyUrl('+msgBus.busScene+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",1)').html('查看计划').show();
		}else if(msgBus.busScene == "102"){
			if(msgBus.type == "41"){
				getBtnobj(index,1).attr('onclick','oprbyUrl('+msgBus.busScene+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",0)').html('完成').show();
			}
			//getBtnobj(index,4).attr('onclick','oprbyUrl('+msgBus.busScene+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",1)').html('查看计划').show();
		}else{
			//getBtnobj(index,4).attr('onclick','oprbyUrl('+msgBus.busScene+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",1)').html('查看计划').show();
		}
	}else if(msgBus.type == "40"){
		var name = "undefined" != typeof msgBus.childSize && msgBus.childSize>0?'确认分派':'开始办理';
		getBtnobj(index,1).attr('onclick','oprbyUrl('+msgBus.type+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",0)').html(name).show();
	}else if(msgBus.type == "42" || msgBus.type == "20"){
		getBtnobj(index,1).attr('onclick','oprbyUrl('+msgBus.type+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",0,0)').html('通过').show();
		getBtnobj(index,2).attr('onclick','oprbyUrl('+msgBus.type+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",0,1)').html('不通过').show();
	}
}

//点击消息下拉动画显示内容
function msgClick(obj){
	var messageId = $(obj).find('.msgId').text();
	var index = $(obj).parent().attr('msg-index');
	var status = $(obj).find('.msgStatus').text();
	var operTime = $(obj).find('.msgOpert').text();
	var mymsgBus = $(obj).find('.msgBus').text();
	var type = $(obj).find('.msgTriggerType').text();
	var msgc=$(obj).siblings('.msgContent').find('.msgc').text();
	if(type == "90") {
		var strArry=msgc.split(";");
		var newMsgc="";
		for(var i=0;i<strArry.length;i++){
			if(strArry[i]!=""){
				newMsgc=newMsgc+strArry[i]+";<br/>";
			}
		}
		$(obj).siblings('.msgContent').find('.msgc').html(newMsgc)
	}
	//var accId = $(obj).find('.msgAccId').text();
	$(obj).siblings('.msgContent').css('display')=='none'?
			($(obj).css('border-bottom','2px #ccc solid'),$(obj).find('.btn-hide').removeAttr('class').attr('class','btn-show')):
				(setTimeout(function(){$(obj).css('border-bottom','none');},300),$(obj).find('.btn-show').removeAttr('class').attr('class','btn-hide'));
	
	$(obj).siblings('.msgContent').slideToggle(300);
	$(obj).find('.msgMain').css('font-weight','500');
	//$(obj).siblings('.msgContent').find('.msgc').html(data.data.content);
	var msgBus = $.parseJSON(mymsgBus)[0];
	msgBus = $.extend(msgBus,{index:index});
	msgBus = $.extend(msgBus,{type:type});
	var myjson = setMsgJson(msgBus);
	operTime == "undefined"?btnShowUrl(myjson):null;
	var mycode='';
	var viewName = "计划";
	var rankFlag = '';
	if(msgBus.busClass&&msgBus.busClass.search(/Plan/)>0){
		mycode=0;
	}else if(msgBus.busClass&&msgBus.busClass.search(/Task/)>0){
		mycode=1;
		viewName = "事件";
	}
	if(type == "51") {
		rankFlag = 1;
		viewName = "评论";
	}else {
		rankFlag = 0;
	}
	//if(myAccId == accId){
		getBtnobj(index,4).attr('onclick','oprbyUrl('+msgBus.busScene+',"'+msgBus.url+'",'+msgBus.index+',"'+msgBus.busData+'",1,'+mycode+','+rankFlag+')').html('查看'+viewName).show();
		if(type=="100"){
			getBtnobj(index,4).hide();
		}
	/* }else{
		getBtnobj(index,4).removeClass('btn-green').addClass('btn-grey').html('查看计划').show();
		getBtnobj(index,4).tooltip({    
			position: 'right',    
			content: '<span style="color:#fff">由于账套不符，不能查看</span>',    
			onShow: function(){        
				$(this).tooltip('tip').css({            
					backgroundColor: '#666',            
					borderColor: '#666'        
					});    
			}});
	} */
	
	if(status == 1 || $(obj).siblings('.msgContent').css('display') == 'none'){
		return false;
	}
	$.post(getRootPath()+'/message/read',{messageId:messageId},function(data){
		if(data){
			$(obj).find('.msgStatus').text(data.data.status);
			/* $(obj).find('.msgMain').css('font-weight','500');
			//$(obj).siblings('.msgContent').find('.msgc').html(data.data.content);
			var msgBus = $.parseJSON(data.data.busParamObj)[0];
			var type = data.data.triggerType;
			msgBus = $.extend(msgBus,{index:index});
			msgBus = $.extend(msgBus,{type:type});
			var myjson = setMsgJson(msgBus);
			btnShowUrl(myjson); */
			var msgNum = parseInt($('div.cathet .msg span.nav_num',window.parent.document).text())-1;
			$('div.cathet .msg span.nav_num',window.parent.document).text(msgNum);
			if(msgNum == 0){
				$('div.cathet .msg span.nav_num',window.parent.document).slideUp(500);
			}
		}else{
			return false;
		}
	});
}
</script>

</body>
</html>