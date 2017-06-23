/**
 * 消息模块，每隔10秒异步获取后台消息
 */
$(function(){
//新方法：websocket获取消息
if (!!window.WebSocket && window.WebSocket.prototype.send){
	queryUnreadMessage__(true);
	var ws = null;
	var a = getRootPath()+"/push"
	ws = new SockJS(a);
	ws.onopen = function () {
	};
	ws.onmessage = function (event) {
	    var json = JSON.parse(event.data);
	    queryUnreadMessage__();
	    setMsg(json);
	};
	ws.onclose = function () {
	};
}else{
	window.setTimeout("queryUnreadMessage__()",1000);
}
});
function setMsg(msg){
	var mymsg = msg;
	if(msg.message){
		mymsg = $.extend(msg.message,{unread:msg.unread});
	}
	var content = '<div id="my_msg" style="height:80px;overflow:hidden;">';
		content += '<a href="javascript:void(0);" style="text-decoration: none;cursor: pointer;color: #000000;" onclick="changeFrame__(\'/message/listMessage\')">';
		content += mymsg.title;
		content += '</a></div><div style="text-align:right;">';
		content += '<a href="javascript:void(0);" style="text-decoration: none;cursor: pointer;color: #000000;" onclick="changeFrame__(\'/message/listMessage\')">你共有';
		content += mymsg.unread;
		content += '条新信息</a></div>';
	$.messager.show({
		title:'温馨提示',
		msg:content,
		width:300,
		height:150,
		timeout:3000,
		showType:'slide'
	});
	var myobj = $('#my_msg').closest('.window');
	myobj.find(".window-body").css('background-color','#FFFFF0');
	myobj.css({'border-radius':'5px','border-color':'#53637a'});
	myobj.find(".panel-header").css({'background-color':'#53637a','background':'linear-gradient(to bottom,#53637a,#53637a 100%)'});
}

//请求未读消息
function queryUnreadMessage__(only){
	var root = getRootPath__();
	var url = root+"/message/checkMessage?r="+Math.random();
	$.ajax({
	    type: 'GET',
	    url: url ,
	    dataType: 'json',
	    success: function(data){
	    	if(data.unread>0){
	    		//$('.header-news').addClass('have-news');
	    		if(data.unread>99){
	    			data.unread="99+";
	    		}
	    		$('div.cathet .msg span.nav_num').html(data.unread).slideDown(500);
	    	}else{
	    		//$('.header-news').removeClass('have-news');
	    		$('div.cathet .msg span.nav_num').slideUp(500);
	    	}
	    	if(data.message){
	    		setMsg(data.message);
	    	}
	    	only?window.setTimeout("queryUnreadMessage__()",15000):null;
	    },
	    error:function(data){
	    	only?window.setTimeout("queryUnreadMessage__()",15000):null;
	    }
	});
}

//js获取项目根路径，如： http://localhost:8083/uimcardprj
function getRootPath__(){
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath=window.document.location.href;
	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName=window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8083
	var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return (localhostPaht+projectName);
}

function changeFrame__(url){
	parent.kk(url,"消息中心");
}