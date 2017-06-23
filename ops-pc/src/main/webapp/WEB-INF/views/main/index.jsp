<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/views/common/common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<title>${system_name}</title>

<style>
*{ margin:0px; padding:0px; list-style:none;}
body{overflow: hidden;} 
.box{ width:100%; }
/*long 头部*/
.nav{padding:10px 0px 7px 58px; position:relative; background:#fff; min-width:800px; box-shadow:0px 2px 5px #888888; z-index: 2;}
.user{ width:190px;display:inline; right:84px; top:10px; position:absolute;}
.name{ font-size:24px; font-weight:bold; height:43px; overflow: hidden;}
/* #index_FiscalAccountName{ width:185px;height:30px; display: inline-block; overflow:hidden;line-height: 46px;} */

/*侧边导航*/
.cathet{ width:165px; height:100%;position:absolute;color:#fff; background:#53637a;}
.nav_bar{background: transparent url(${ctx}/resources/images/index/line.png) bottom no-repeat; 
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader (src='${ctx}/resources/images/index/line.png', sizingMethod='crop');  /* IE6+ 多边框属性代替样式*/}

.cathet ul li{ height:60px; line-height:59px; cursor:pointer;}
.nav_bar{padding: 0px 10px;}
.nav_b{background:red;}
.cathet ul .use{/* background:url(${ctx}/resources/images/index/line.png) no-repeat bottom; *//*正常浏览器多边框属性*/}
.nav_span{float:right;margin-top:22px; width:15px; height:15px; background:url(${ctx}/resources/images/index/jiant.png) no-repeat ;}
 
.cathet ul li span{margin-left: 10px;}
.content{max-height:76%; overflow: auto; } 
.cathet ol li{ height:35px; line-height:43px; padding:0px 10px 10px 10px; border-bottom:1px solid #d8d8d8; font-size:12px; cursor:pointer;overflow: hidden;}
.close{background:url(${ctx}/resources/images/index/delete_icon.png) no-repeat;float:right; width:15px;height:15px;margin-top: 15px;background-position:center center; }
.cathet ol li span{ margin:0px 8px 0px;float:left; width:90px; overflow: hidden;}
.cathet ol li .img{float: left;margin-top: 12px;}
a{ text-decoration:none;  width:100%;}
img{ vertical-align:middle;}

/*用户详细信息模块*/
.name{ position: relative;}
.information{ position: absolute;background:#fff; width:274px; height:202px; left:-100px; z-index: 1;
border:3px solid #f5f4f5;border-radius:15px;-moz-border-radius:15px; /* Old Firefox */ padding: 10px;
font-size:10px;
}
.user-mes{
    font-size:10px;position:absolute; display:none; background:url(${ctx}/resources/images/card.png) no-repeat;
	width:267px;height:180px;left:-105px;top:36px;color:#6D6D72;padding-top:30px;padding-left:10px;overflow:hidden;z-index: 1;
}
.user-mes h1{	
	color:#000;margin: 0px;
}
.user-mes-top{
	height:80px;width:240px;border-bottom:1px solid #E1E1E1;
}
.user-mes-left{
	float:left;margin-left:20px;
}
.user-mes-right{
	float:left;margin-left:20px;line-height:25px;width:128px; height:78px; 	
}
.account{ width:126px; height:24px; line-height:24px;font-size:12px; display: block; overflow: hidden;}
.user-mes-right a.setting{
	font-size:12px; color:#2DA0F0;font-weight:bold; width:126px;height:24px; line-height:24px; display: block; overflow: hidden;
}
.user-mes-bottom{
	line-height:25px;margin-left:10px;margin-top:5px; 
}
.user-mes-bottom label{ font-size: 14px; height:25px; line-height: 25px; display:inline-block; width:185px;}
.user-mes-bottom h1{
    font-size:12px;display:inline-table;
}
.user-mes-bottom a{ display: inline-block; height: 20px; line-height: 20px; width:60px; font-size: 12px;}
.change-btn{ color:#2DA0F1;}
/*easyui*/
.tabs li a.tabs-close{right: -15px;}
.tabs-header, .tabs-panels{border-color: #F2F2F2;}
/*easyui样式改版*/
/* .tabs,.tabs-header{border-color: none;} */
.tabs-header{display: none;}  
.headName{width:100px; display: inline-block;overflow: hidden; } 
/*导航页面消息数量样式*/
.nav_bar .nav_num{
	background: url("${ctx}/resources/images/warn.png");
    width: 35px;
    height: 16px;
    position: absolute;
    margin-top: 22px;
    line-height: 16px;
    text-align: center;
    font-weight: 900;
    padding-right: 3px;
}
.search{
    display: inline;
    right: 84px;
    top: 15px;
    position: absolute;
    right: 350px;
}
.search .searchbox-button {
    background:url(${ctx}/resources/images/indexseach.png) no-repeat center center;
    width:35px !important;
}
.search .searchbox{
	border-radius:5px;
}
</style>
<link rel="stylesheet" href="${ctx}/resources/css/jquery.mCustomScrollbar.css?v=${css_v}" type="text/css" />
</head>
<body>
 <div class="box">
 <input type="text" value="${userId}" id="UID" style="display: none"/>
   <div class="nav">
      <div class="long">
         <img src="${ctx}/resources/images/index/logo_0.png" width="273" height="47" >
      </div>
      <div class="search">
      	<input id="searchInput" class="text" />
      </div>
      <div class="user" >
          <div class='name'>
	          <span><img id="pic_round" src="${ctx}/userController/getHeadPortrait/${userId}.jpg" width="40" height="40"></span>
	          <span class="username headName" title="${username}">${username}</span>
          </div>
         <div class="user-mes-hover user-mes show-list">
        	<div class="user-mes-top">
            	<div class="user-mes-left">
                	<img id="pic_index" src="${ctx}/userController/getHeadPortrait/${userId}.jpg" width="70" height="70"/>
                </div>
                <div  class="user-mes-right">
                	<h1 style="height: 26px; overflow: hidden;" class="username yhName" title="${username}">${username}</h1>
                	<span class="account" title="${usercode}">账号：${usercode}</span>
                	<fool:tagOpt optCode="ops_sys_user">         	
         	          <a href="javascript:;" class="setting">个人信息</a>
                    </fool:tagOpt>  
                </div>
            </div>
            <div class="user-mes-bottom">
                <div title="${orgName}"><h1>企业名称：</h1><label id="orgName">${orgName}</label><br/></div>
                <div><div style="height:38px; overflow: hidden;" title="${sessionScope.FiscalAccountName}"><h1>账套名称：</h1><label id="index_FiscalAccountName">${sessionScope.FiscalAccountName}</label></div>&nbsp;&nbsp;<a id="change-btn" href="javascript:;">切换帐套</a>&nbsp;&nbsp;<a id="exit" href="javascript:;">退出系统</a></div>
            </div>
        </div>      
      </div>
   </div>
   <div class="con">
   <!--侧边栏导航-->
       <div class="cathet">
           <ul id="ul">
              <li class="msg"><div class="nav_bar"><img src="${ctx}/resources/images/index/news_icon.png" width="25" height="25"><span>消息</span><span class="nav_span"></span><span class="nav_num" style="display:none"></span></div></li>
              <li class="use nav_bar" listindex="0"><div><img src="${ctx}/resources/images/index/app_icon.png" width="25" height="25"><span>应用中心</span><span class="nav_span"></span></div></li>
           </ul>
           <div class="content mCustomScrollbar" data-mcs-theme="minimal">
	           <ol id="listTab">
	           </ol>
	       </div>
       </div>  
     <div data-options="region:'center',border:false" style="background:#F0F0F0;height:100%;width:88%;float: right; "> 
       <div id="tabs"  class="easyui-tabs" style="overflow: auto;">    
        <div title='首页' >        
	         <iframe id="iframe_index" scrolling="auto" frameborder="0" src="${ctx}/main/indexPage" style="width:88%;height:100%; position:absolute; left:0px; margin-left: 165px; "></iframe>
        </div>    
       </div>
    </div>  
   </div>
</div>
<div id="fiscalAccountWindows"></div>
<div id="rcmenu" class="easyui-menu" style="display: none;">
    <div data-options="name:1" id="closecur">关闭</div>
    <div data-options="name:3" id="closeother">关闭其他</div>
    <div data-options="name:2" id="closeall">关闭全部</div>
</div>
</body>
</html>
<%@include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript" src="${ctx}/resources/js/message.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.mCustomScrollbar.concat.min.js?v=${js_v}"></script>
<script type="text/javascript">
var billCodes=["base","qckc","cgdd","cgrk","cgth","cgxjd","cgsqd","cgfp","pdd","dcd","bsd","scll","sctl","cprk","cptk","xsdd","xsch","xsth","xsfp","xsbjd","scjhd","fysqd","cgfld","xsfld","skd","fkd","fyd","bom"];
function menuDatas(){
    var datas=[];
    if('${menuDatas}'!=''){
        datas =eval('${menuDatas}');
    }
	return datas;
}

	 (function($){//侧边滚动条插件调用
	        $(document).ready(function(){
	            $(".content").mCustomScrollbar();
	        });
	    })(jQuery);

function kk(src,text){//添加tabs	
	  var myheight = $(window).outerHeight()-$('.nav').outerHeight();
	  var content='<div style="width:88%;height:100%;"><iframe scrolling="auto" frameborder="0"  src="${ctx}'+src+'" style="width:88%;height:'+myheight+'px; margin-left: 175px;position:absolute; left:0px;"></iframe></div>';	
	  if ($('#tabs').tabs('exists',text)){//判断tabs存在那就打开当前tabs,然后return出去
		  $('#tabs').tabs('select',text);
		  var tabs=$('#tabs').tabs('getTab', text);
		  tabs.find('iframe').attr("src",'${ctx}'+src);
	  }else{ 		 
	  $('#tabs').tabs('add',{
		    plain:true,
			title:text,
			tabPosition:'left',
			content:content,
			closable:true,
		});	 
	  }
	  //让当前打开的tabs对应左侧的ol li对应变颜色
	  var tab = $('#tabs').tabs('getSelected');
  	  var index = $('#tabs').tabs('getTabIndex',tab);
  	  $('#listTab li').eq(index-1).css('background','#495970').siblings().css('background','#53637a');
  	  $('.cathet ul li').css('background','#53637a');
}
function searchInit(){
	var content = localStorage["myMean"];//获取本地储存的mean列表
	var oldValue = '';
	$('#searchInput').searchbox({
		width:250,
		height:35,
		prompt:"请输入搜索关键字(Ctrl+H)",
		searcher:function(value,name){
			value = $.trim(value);
			if(value == ''){//为空的时候重新加载首页功能
				$('.use[listindex=0]').click();
				var chunk = $('#iframe_index').contents().find('.chunk');
				var favorite = $('#iframe_index').contents().find('.favorite');
				var tips = $('#iframe_index').contents().find('.searchTips');
				favorite.show();
				chunk.html(content);
				tips.hide();
				chunkAction();
			}else{
				meanSearch(value);
			}
		}
	});
	//兼容各种浏览器即时输入执行搜索的函数
	$('#searchInput').searchbox('textbox').bind('propertychange',function(){
		var chunk = $('#iframe_index').contents().find('.chunk');
		var favorite = $('#iframe_index').contents().find('.favorite');
		var value = $.trim($('#searchInput').searchbox('getText'));
		var tips = $('#iframe_index').contents().find('.searchTips');
		if(value != oldValue){//屏蔽紧接着的两次相同值的搜索
			$('.use[listindex=0]').click();
			if(value == ''){//为空的时候重新加载首页功能
				var content = localStorage["myMean"];
				favorite.show();
				chunk.html(content);
				tips.hide();
				chunkAction();
			}else{
				meanSearch(value);
			}
			oldValue = value;
		}
	});// IE专用
	$('#searchInput').searchbox('textbox').bind('input',function(){
		var chunk = $('#iframe_index').contents().find('.chunk');
		var favorite = $('#iframe_index').contents().find('.favorite');
		var value = $.trim($('#searchInput').searchbox('getText'));
		var tips = $('#iframe_index').contents().find('.searchTips');
		if(value != oldValue){//屏蔽紧接着的两次相同值的搜索
			$('.use[listindex=0]').click();
			if(value == ''){//为空的时候重新加载首页功能
				var content = localStorage["myMean"];
				favorite.show();
				chunk.html(content);
				tips.hide();
				chunkAction();
			}else{
				meanSearch(value);
			}
			oldValue = value;
		}
	});
	//每个子页面的ctrl+p快捷键控制放在base.js
}
//首页搜索功能
function meanSearch(value){
	var chunk = $('#iframe_index').contents().find('.chunk');
	var favorite = $('#iframe_index').contents().find('.favorite');
	var tips = $('#iframe_index').contents().find('.searchTips');
	$.post(getRootPath()+"/main/queryMenu",{name:value},function(data){
		favorite.hide();
		chunk.html('');
		if(data == ''){
			tips.html('共搜索到0个功能').show();
			return false;
		}
		var num=0;
		for(var i=0; i<data.length;i++){//添加功能模块
			  var kindName=data[i].label; 
			  var myTag=$('<p>'+kindName+'<span class="tog"></span></p><ul></ul>');	  
		   	  for(var j=0;j<data[i].children.length;j++){    
		   	   num++;
			   var chilName=data[i].children[j].label;
			   var chilurl=data[i].children[j].url;
			   var smallIcoPath=data[i].children[j].smallIcoPath; 
			   var chilid=data[i].children[j].id;
			   var myTow=$('<li addurl='+chilurl+' text='+chilName+' title='+chilName+'><img src="${ctx}'+smallIcoPath+'" width="48" height="48"><br/><span>'+chilName+'</span></li>');
				myTag.eq(1).append(myTow);		  
		   	      }
		   		chunk.append(myTag);
		}
		tips.html('共搜索到'+num+'个功能').show();
		chunkAction();
	});
}
//让搜索出来的功能重新初始化按键功能
function chunkAction(){
	var chunk = $('#iframe_index').contents().find('.chunk');
	//点击显示隐藏
	chunk.find('p').click(function(){	
		$(this).next('ul').stop().slideToggle();
	    $(this).children().stop().toggleClass('bor'); 
	    $(this).toggleClass('border');
		});
	//点击ul下面的li添加tabs
	chunk.find('ul li').click(function(){
		 var src=$(this).attr("addurl");
		 var text=$(this).attr('text');
		 kk(src,text);		 
	  });
}
$(function(){
	searchInit();
	$('#tabs').tabs({//每当新添加一个tabs就往侧边  cathet下面的ol添加一个li
	    onAdd:function(title,index){  
	    	var index=index;
	        var title=title;
	        if(title == "消息中心") return false;
	        var list=$(' <li listindex='+index+' listTitle='+title+'><img class="img" src="${ctx}/resources/images/index/box_icon.png" width="20" height="20"><span class="tit">'+title+'</span><a class="close"></a></li>');
	        $('#listTab').append(list);	    	 
	    },
	    onSelect:function(title,index){
	    	var panel = $('#tabs').tabs('getTab',title);
	    	panel.parent().css({"visibility":""});//解决各浏览器iframe隐藏后显示置顶和滚动条消失的问题
	    	/* panel.find('iframe').contents().find('html').css("overflow-y","auto");
	    	var scrollTop = panel.find('iframe').contents().find('body').scrollTop();
	    	panel.find('iframe').contents().find('body').scrollTop(scrollTop-1); */
	    },
	    onUnselect:function(title,index){
	    	var panel = $('#tabs').tabs('getTab',title);
	    	panel.parent().css({'display':"block","visibility":"hidden"});//解决各浏览器iframe隐藏后显示置顶和滚动条消失的问题
	    	//panel.find('iframe').contents().find('html').css("overflow-y","");
	    }
	}); 
});
//控制帐套名超出部分用...代替
$(function(){
	var yhName="${username}";//用户名
	var yhCode="${usercode}"; //帐号
	var ztname='${sessionScope.FiscalAccountName}';//帐套名称
	var orgName="${orgName}"; //企业名
 	if(/.*[\u4e00-\u9fa5]+.*$/.test(yhName)){
		if(yhName.length>5){
			yhName=yhName.substring(0,10)+"...";
			$(".yhName").html(yhName);	
		}
	}else{
		if(yhName.length>8){
			yhName=yhName.substring(0,8)+"...";
			$(".yhName").html(yhName);	
		}
	}
	if(/.*[\u4e00-\u9fa5]+.*$/.test(yhCode)){
		if(yhCode.length>5){
			yhCode=yhCode.substring(0,10)+"...";
			$(".account").html(yhCode);	
		}
	}else{
		if(yhCode.length>8){
			yhCode=yhCode.substring(0,8)+"...";
			$(".account").html(yhCode);	
		}
	} 
	if(/.*[\u4e00-\u9fa5]+.*$/.test(orgName)){
	if(orgName.length>8){
		orgName=orgName.substring(0,8)+"...";
		$("#orgName").html(orgName);	
		}
	}else{
		if(orgName.length>12){
			orgName=orgName.substring(0,12)+"...";
			$("#orgName").html(orgName);
		}
	}
	if(/.*[\u4e00-\u9fa5]+.*$/.test(ztname)){
	if(ztname.length>8){//帐套名超过15个的用...代替
	ztname=ztname .substring(0,8)+"...";
	$("#index_FiscalAccountName").html(ztname);
		}
	}else{
		if(ztname.length>12){
			ztname=ztname.substring(0,12)+"...";
			$("#index_FiscalAccountName").html(ztname);
		}	
	}
	var name='${username}'; // 用户名为汉字时只能显示4个汉字
	if(/.*[\u4e00-\u9fa5]+.*$/.test(name)){
		if(name.length>4){
		name=name .substring(0,4)+"..."
		$(".username").html(name);
		}
	}else{
	if(name.length>7){
	   name=name .substring(0,7)+"..."
		$(".username").html(name);
		}
	}
});

/* $(function(){
	$('.username,#orgName,.account,#index_FiscalAccountName').mousemove(function(e){
		var mydiv=$('<div class=""></div>');
		var x=e.pageX;
		var y=e.pageY;
	});
});
 */

 
 $(function(){
	//点击.ul li div改变颜色
	   $('.ul li').click(function(){
	   e.preventDefault();//阻止该元素的其它事件执行
		$(this).addClass('nav_bar2').siblings('.ul li').removeClass('nav_bar2');  
	}); 
}); 
 
 //点击cathet ul li
 $('.cathet ul li').click(function(){
	$(this).css('background','#495970').siblings().css('background','#53637a');
	$('#listTab li').css('background','#53637a');
 });

//点击cathet ol li下面的x关闭对应的窗口
  $('#listTab').on("click",'.close',function(e){	
	var index=$(this).parents('li').attr('listindex');
	var text=$(this).siblings('span').html();
	$('#tabs').tabs('close',text);
	$(this).parents('li').remove();
});  

//点击左边的tabs ol li显示对应的页面
$('#listTab').on("click",'.tit',function(e){
	var text=$(this).text();//选中的标题文本
	var index=$(this).parents('li').attr('listindex');//选中的选项卡下标	
	$(this).parents('li').css('background','#495970').siblings().css('background','#53637a');
	$('.cathet ul li').css('background','#53637a');
	$('#tabs').tabs('select',text);	
});
//右击显示菜单
  $('#listTab').on("mousedown",'li',function(e){
	if(e.which==3){
	var index=$(this).attr('listindex');
	var text=$(this).children().text();
	$(this).addClass('keep').siblings().removeClass('keep');
    e.preventDefault();//阻止该元素的其它事件执行
    if(index>0){
        $('#rcmenu').menu('show', {
            left: e.pageX,
            top: e.pageY,
            duration:1500
          /*hideOnUnhover:false*/
        }).data("tabIndex",index);
        $('#rcmenu').on('contextmenu',function(){
            return false;
        });
        return false; 
      } 
	}
}); 

function closeTab(menu,type){
		switch(type){
			case 1 :
				var _index = $(menu).data("tabIndex");
				var _text=$('#listTab li.keep').children('span').text();
				$('#tabs').tabs('close',_text); 	
				$('#listTab li.keep').remove();
			break;
			case 2 :
				var tablist = $('#tabs').tabs('tabs');
				for(var i=tablist.length-1;i>0;i--){
		            $('#tabs').tabs('close',i);
		            $('#listTab li').remove();
		        }
			break;
			case 3 :
				var tablist = $('#tabs').tabs('tabs');
				var _index = $(menu).data("tabIndex");
				for(var i=tablist.length-1;i>0;i--){
					if(i==_index)continue;
		            $('#tabs').tabs('close',i);
		            $('#listTab li').not(".keep").remove();		            
		        }
				$("#tabs").tabs("select",_index);
			break;
			default:;
		}
	}
	
	$('#rcmenu').menu({
		onClick:function(item){
			closeTab(this,item.name);
		}
	});
	
//点击应用中心显示主页面
$('.use').click(function(){
	var index=0;//主页面的tabs索引
	$('#tabs').tabs('select',0);
});

$('.msg').click(function(){
	var myheight = $(window).outerHeight()-$('.nav').outerHeight();
	var src="/message/listMessage"
	var content='<div style="width:88%;height:100%;"><iframe scrolling="auto" frameborder="0"  src="${ctx}'+src+'" style="width:88%;height:'+myheight+'px; margin-left: 175px;position:absolute; left:0px;"></iframe></div>';	
	if($('#tabs').tabs('exists','消息中心')){
		$('#tabs').tabs('select','消息中心');
	}else{
		$('#tabs').tabs('add',{
		    plain:true,
			title:"消息中心",
			tabPosition:'left',
			content:content,
			closable:true,
		});	
	}
});

$("#exit").click(function(){//退出系统
	$.fool.confirm({
		msg:'确定退出系统吗?',
		fn:function(r){
			if (r){
				for(var i=0;i<billCodes.length;i++){
					if(localStorage[billCodes[i]]){
						localStorage.removeItem(billCodes[i]);
					}
					if(localStorage['myMean']){
						localStorage.removeItem('myMean');
					}
				}
				window.location.href="${pageContext.request.contextPath}/logout";
			} 
		}
	});
});

$("#change-btn").click(function(){//切换账套
	for(var i=0;i<billCodes.length;i++){
		if(localStorage[billCodes[i]]){
			localStorage.removeItem(billCodes[i]);
		}
		if(localStorage['myMean']){
			localStorage.removeItem('myMean');
		}
	}
	$("#fiscalAccountWindows").window({'title':"切换账套",top:120,
		width:"60%",
		height:"auto",
		href:'${ctx}/fiscalAccount/windows?okCallBack=selectAccount'				
	});
});

$('.setting').click(function(){//个人信息
	 var myheight = $(window).outerHeight()-$('.nav').outerHeight();
	 kk("/userController/userInfoUI","个人信息");
	/* if ($('#tabs').tabs('exists', '个人信息')){
		$('#tabs').tabs('select', '个人信息');
		var tabs=$('#tabs').tabs('getTab', '个人信息');
		tabs.find('iframe').attr("src",'${ctx}/userController/userInfoUI');
	} else {
		var content='<iframe title="个人信息" scrolling="auto" frameborder="0"  src="${ctx}/userController/userInfoUI" style="width:100%;height:'+myheight+'px; position:absolute; left:0px; margin-left: 165px;"></iframe>';
		$('#tabs').tabs('add',{
			title:'个人信息',
			content:content,
			closable:true
		});
	} */
});

//控制用户信息显示隐藏
$(document).ready(function(){
	$(".name").mouseover(function () {
	    $('.user-mes').show(2);
	   });
	 $(".user").mouseleave(function () {
	    $('.user-mes').hide(2); 
	 }); 
});

//javascript控制高度
window.onresize = function () {//窗口改变大小时触发
	var heightol = $(window).outerHeight()-$('.cathet ul').outerHeight()-$('.nav').outerHeight();
	 heightol=heightol+'px';
     $('.content').height(heightol);
}

//获取页面引导  First值  判断是否显示引导页，若用户点击了绿色图标下次登录进来将不再显示引导页
 function userID(){//用户id传入子页面标识用户
 	 var userID='${userId}';	
 	 return userID;
 } 
/*  $(function(){
	 var First=userID();
 	 var Firstlong=localStorage.getItem(First);
 	 var  Administrator=${isAdmin};
 	 console.log(Administrator);
 	 console.log(Firstlong);
 	 if(Administrator==false||Administrator==null){
 		 if(Firstlong!=1){ 
 			 $('#iframe_index').attr('src','${ctx}/main/guidance')
 		  }
 	 } 
 }); */

function changePic(){
    $("#pic_index").attr("src","${ctx}/userController/getHeadPortrait/${userId}.jpg");
    $("#pic_round").attr("src","${ctx}/userController/getHeadPortrait/${userId}.jpg");
};
</script>

