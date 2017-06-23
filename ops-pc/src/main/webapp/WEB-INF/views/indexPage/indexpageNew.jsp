<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title></title>
<style type="text/css">
 *{ margin:0px; padding:0px; list-style:none;}
/* .post{  width:90%;  float:left; height: 92%;} */
.post{margin-bottom: 60px;}
a{ text-decoration:none; color:#fff; width:100%; height:40px; }
img{ vertical-align:middle;}
/*内容区*/
/*添加常用模块*/
.favorite p{ height:35px; line-height:35px; background:#f2f2f2;padding-left:14px;}
.favorite ul{ overflow:hidden; background:#fff; padding:36px 0px 0px 20px;}
.favorite ul li{margin:0px 18px 36px 0px; text-align:center; width:87px; display:inline-block;cursor:pointer;}
.favorite ul li span{  padding-top:18px; font-size:12px; display: inline-block;}
/*业务模块*/
.chunk p{ height:35px; line-height:35px; background:#f2f2f2;padding-left:14px;}
.tog{ display:inline-block; float:right;width:15px; height:15px; margin:10px 20px 0px 0px; background:url(${ctx}/resources/images/index/icons1.jpg) no-repeat 95% 50%;}
.chunk ul{ overflow:hidden; background:#fff; padding:36px 0px 0px 20px;}
.chunk ul li{margin:0px 18px 36px 0px; text-align:center; width:88px; display:inline-block;cursor:pointer;}
.chunk ul li span{ padding-top:10px; font-size:12px; display: block;height: 15px;overflow: hidden; }
.bor{ background:url(${ctx}/resources/images/index/icons.jpg) no-repeat 95% 50%;}
.border{border-top: 1px solid #A4D6FB;margin-bottom: 2px;}

/*easyui重构css*/
.panel-title{height: 50px;}
.window, .window .window-body{ border: 3px solid #CCCCCC;}
.panel-title{ text-align:left;line-height:50px; padding:0px 20px; color:#4D4D4D;}
.window .window-header .panel-icon, .window .window-header .panel-tool{top:25%;position: relative;}
.window .window-header{background: #F3F3F3}
.panel-tool{display: none;}
.tabs li a.tabs-close{right:-15px;}
.tabs-header{width:580px;}
.tabs-header{border: 1px solid #CCC;}
.tabs-scroller-left,.tabs-panels{border: 0.5px solid #ccc;}
.tabs{border: none;background: #fff;}
.tabs-scroller-right{border:none;}
.tabs li a.tabs-inner,.panel-body{color: #797979;}
.tabs li.tabs-selected a.tabs-inner{border-bottom: none;}
.tabs-selected{border: none;border-bottom: 0.5px solid #CCC;}
.tabs-inner{border-color: none;}
.tabs-scroller-left,.tabs-scroller-right{height: 28px;!important}
.tabs li a.tabs-inner{border: none;}
.tabs li a.tabs-inner:hover{color: #797979; background: #fff;}
.tabs li a.tabs-inner{background: #fff;}
.tabs-selected{background: #ccc;}
.tabs li.tabs-selected a.tabs-inner{color: #4D4D4D;font-weight:normal;background: #ccc;}
.tabs li {margin: 0px;}
.tabs-header{border-bottom: none;background: none;}
.tabs-closable{margin-right: 16px;}

/* .panel{border: 1px solid red;} */

/*添加常用模块窗体样式*/
.nav{position: absolute; top:20px;right: 20px; width: 100px;height:25px;}
.nav a{ width:25px;height:25px;display: inline-block;cursor:pointer;margin-left: 2px;}
.shrink{background: url("${ctx}/resources/images/index/form.png") no-repeat 50% 50%;}
.listope{background: url("${ctx}/resources/images/index/block_on.png") no-repeat 50% 50%;}
.shut{background: url("${ctx}/resources/images/index/delete.png") no-repeat 50% 50%;}
.stock-con,.stock-cons{padding: 5px 20px;}
.Tabs{display: none;}
.play{width:100px; height:100px;}
.panel ul,li{overflow: hidden;}
.panel ul li{border-right: 0.5px solid #ccc;border-bottom: 0.5px solid #ccc;  float: left; padding: 5px; width:79px;height:75px;}
.panel ul li div{float: left; text-align: center;}
.check{width:14px; height:5px; padding-top: 50px;}
.stock-inf{width:60px;overflow: hidden;float: right;}
.stock-inf span{padding-left: 3px;}
.save{background: #FF8C3C;width:80px;height: 30px;cursor:pointer;font-size: 16px;color: #fff;text-align: center;line-height:30px;position: absolute; top:350px;right: 20px;}
.stock-cons ol li{ height: 30px;line-height:30px;border-bottom: 0.5px solid #ccc;padding-left: 5px;}
.stock-cons ol li div{float:right; width:450px;text-align: center;}
.number{margin: 0px 15px;}
.tt{border:none;}
.searchTips{    
	color: #777;
    padding: 10px 30px;
}
</style>
</head>
<body>
 <!--内容-->
<div class="post">
 	<div class="searchTips" style="display:none;">
   </div>
    <div class="favorite">
        <p>常用模块</p>
        <ul>
          <li class="favli"><img src="${ctx}/resources/images/index/nadd.jpg" width="48" height="48"><br/><span>添加模块</span></li>
        </ul>
    </div>
    <div class="chunk">
   </div>
   <div id='addStock'></div>       
</div>
<%@include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
var menuDatas=parent.menuDatas();
/*  var  Information=(JSON.stringify(menuDatas))//转化为json字符串
alert(Information);  */
$(function(){
    if(menuDatas){
        $.ajax({//窗体加载获取常用模块数据
            type: "post",
            url: "${ctx}/commonUseModule/query",
            dataType: "json",
            async:true,
            success: function(data){
                for(var i=0; i<menuDatas.length;i++){
                    for(var j=0;j<menuDatas[i].children.length;j++){
                        var chilName=menuDatas[i].children[j].label;
                        var chilurl=menuDatas[i].children[j].url;
                        var smallIcoPath=menuDatas[i].children[j].smallIcoPath;
                        var chilid=menuDatas[i].children[j].id;
                        if(data[chilid]){//如果常用模块的id等于页面子id,就添加常用模块
                            var stock=$('<li class="shell" _class='+chilid+' name="Common" addurl='+chilurl+' text='+chilName+' title='+chilName+' ><img src="${ctx}'+smallIcoPath+'" width="48" height="48"><br/><span>'+chilName+'</span></li>');
                            $('.favorite ul').append(stock);
                        };
                    };
                };
            }
        });

        for(var i=0; i<menuDatas.length;i++){//添加功能模块
            var kindID=menuDatas[i].id;
            var index=menuDatas[i].index;
            var kindName=menuDatas[i].label;
            var myTag=$('<p>'+kindName+'<span class="tog"></span></p><ul></ul>');	//id='+index+'
            for(var j=0;j<menuDatas[i].children.length;j++){
                var chilName=menuDatas[i].children[j].label;
                var chilurl=menuDatas[i].children[j].url;
                var smallIcoPath=menuDatas[i].children[j].smallIcoPath;
                var chilid=menuDatas[i].children[j].id;
                var myTow=$('<li addurl='+chilurl+' text='+chilName+' title='+chilName+'><img src="${ctx}'+smallIcoPath+'" width="48" height="48"><br/><span>'+chilName+'</span></li>');
                myTag.eq(1).append(myTow);
            };
            $('.chunk').append(myTag);
        };
        var myMean = $('.chunk').html();
        localStorage['myMean']=myMean;//本地储存储存mean列表，减轻搜索量
    }
});

$(function(){
		//点击显示隐藏
	$('.chunk p').click(function(){	
		$(this).next('ul').stop().slideToggle();
	    $(this).children().stop().toggleClass('bor'); 
	    $(this).toggleClass('border');
		});
		 //点击ul下面的li添加tabs
	$('.chunk ul li').click(function(){
		 var src=$(this).attr("addurl");
		 var text=$(this).attr('text');
		 parent.kk(src,text);		 
	  });
	
	 $('.favorite').on("click",'.shell',function(e){//常用模块的点击事件	
		 var src=$(this).attr("addurl");
		 var text=$(this).attr('text');
		 parent.kk(src,text);		 
	 });
});

//添加常用模块窗口
 $('.favli').click(function(){
	$('#addStock').window({
		title:'请选择常用功能',
		top:10,
		left:300,
		width:600,
		height:400,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		modal:true,
		href:'${ctx}/commonUseModule/manage',
	});
}); 

</script>
</body>
</html>