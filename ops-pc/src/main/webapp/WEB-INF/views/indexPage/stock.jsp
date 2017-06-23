<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Insert title here</title>

</head>
<body>
<div class="stock-conter">
	<div class="nav">
	   <a href="javascript:;" class='shrink'></a>
	   <a href="javascript:;" class='listope'></a>
	   <a href="javascript:;" class='shut'></a>
	</div>
	<div class="stock-con" style="display:block;">
		<div id="tt" style="width:550px;height:280px; overflow: hidden;">   
	      
	    </div>  
	</div>
	<div class="stock-cons" style="display: none;">
	    <div id="dd" style="width:550px;height:280px; overflow: hidden;">   
	      
	   </div>
	</div>
	<div class="save">确定</div>
</div>
<script type="text/javascript">
$('#tt,#dd').tabs({//将滚动动画禁用
	scrollDuration:0
}); //初始化tabs 
for(var i=0; i<menuDatas.length;i++){
	var kindName=menuDatas[i].label; 
	var myTabs=$('<div class="play"><ul></ul></div>');
	var mylistTabs=$('<div class="list"><ol></ol></div>')
	var number=0;//编号
   	for(var j=0;j<menuDatas[i].children.length;j++){  
   		var chilName=menuDatas[i].children[j].label;
   		var smallIcoPath=menuDatas[i].children[j].smallIcoPath; 
   		var chilurl=menuDatas[i].children[j].url;
   		var Id=menuDatas[i].children[j].id;
   		 if(number<menuDatas[i].children.length){
   			number=number+1;
   		}  		
   		var myplay=$('<li liurl='+chilurl+'><div class="check"><input class='+Id+' name="Fruit" type="checkbox" value=""/></div><div class="stock-inf"><img src="${ctx}'+smallIcoPath+'" width="48" height="48"><br/><span>'+chilName+'</span></div></li>');
   		var mylistli=$('<li lisurl='+chilurl+'><span><input class='+Id+' name="Fruit" type="checkbox" value=""/></span><span class="number">'+number+'</span><div>'+chilName+'</div></li>');                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
   		var mylist=myTabs.eq(0).children('ul').append(myplay); 
   		var mylistbox=mylistTabs.eq(0).children('ol').append(mylistli)
 
	}
	$('#tt').tabs('add',{//缩列图模式
	    plain:true,
		title:kindName,
		tabPosition:'left',
		content:mylist,
		closable:true,
	});
	
	$('#dd').tabs('add',{//列表模式
	    plain:true,
		title:kindName,
		tabPosition:'left',
		content:mylistbox,
		closable:true,
	});	
}
$('#tt,#dd').tabs({//恢复滚动动画，默认选择第一个tab 
	scrollDuration:400
});
$(function(){//关闭窗体
	$('.shut').click(function(){
		$('#addStock').window('close');  		
	}); 
	$(".shut").mouseover(function () {
		$(this).css('background','url("${ctx}/resources/images/index/delete_on.png") no-repeat 50% 50%');
	});
	$(".shut").mouseleave(function () {
	   $(this).css('background','url("${ctx}/resources/images/index/delete.png") no-repeat 50% 50%');
	}); 
})

$(function(){//列表模式
	$('.shrink').click(function(){
		$('.stock-cons').css('display','block'); 
		$('.stock-con').css('display','none');
		$(this).css('background','url("${ctx}/resources/images/index/form_on.png") no-repeat 50% 50%');
		 $('.listope').css('background','url("${ctx}/resources/images/index/block.png") no-repeat 50% 50%');
	});
});

$(function(){//缩列图模式
	$('.listope').click(function(){
		$('.stock-cons').css('display','none'); 
		$('.stock-con').css('display','block');
		$(this).css('background','url("${ctx}/resources/images/index/block_on.png") no-repeat 50% 50%');
		$('.shrink').css('background','url("${ctx}/resources/images/index/form.png") no-repeat 50% 50%');
	});
});

$(document).ready(function(){//窗体加载完成执行在ol li最前面添加一个li
	var mylistli=$('<li><span><input name="Fruit" type="checkbox" value=""/></span><span class="number">编号</span><div>功能模块名称</div></li>');
	$('.list ol').prepend(mylistli);
	$('.tabs-scroller-left,.tabs-scroller-right').css('height','28');
	$('.tabs-panels').css('height','247');
});

//获取选择的id
var map={};//保存选中添加常用模块的id
$(function(){
	var boxes = document.getElementsByName("Fruit");//获取所有的复选狂
	var arr='';
	$.ajax({//窗体加载获取常用模块数据
        type: "post",
        url: "${ctx}/commonUseModule/query",
        dataType: "json",
        async:true,
        success: function(data){//将已添加为常用模块的数据对应的复选框默认选中
        	var id="";
       /*  console.log('data'+JSON.stringify(data)); */
        	 for(var s=0;s<=boxes.length;s++){
        		var arr=$(boxes[s]).attr('class');
        		if(data[arr]){ //如果常用模块的数据在列表中有,就默认选中    		
        		   $('.'+arr).attr("checked","checked");	 
        		}
        	map = data;
           }
        }
    });	
	$(boxes).click(function(){
		var ID = $(this).attr('class');//获取选中的ID	
		// map[ID] = ID;			
		if($(this).attr("checked")){//删除取消选中状态id   取消选中
			 delete map[ID]; 						 	
			 for(var c=0;c<=boxes.length;c++){//两边同步 取消
				 $('.'+ID).removeAttr("checked"); 
			 }
		}else{	//选中状态的id  选中
			if(!map.hasOwnProperty(ID)){
				map[ID] = ID;	
			}
			 for(var n=0;n<=boxes.length;n++){//两边同步 选中
				 $('.'+ID).attr("checked","checked");
			 }
		}
	});	
	$('.save').click(function(){//保存方法
	/* 	console.log("save: " + joinData(map));  */
		$.ajax({
            type: "get",
            url: "${ctx}/commonUseModule/save",
            data: {resourceIds:joinData(map)},
            dataType: "json",
            success: function(data){
                if(data.returnCode==0){
                	$.fool.alert({time:1000,msg:'保存成功'});	
                	   //保存成功后先删除indexpageNew常用模块的所有li
            		   $('li.shell').remove();
                 	for(var i=0; i<menuDatas.length;i++){
               		 for(var j=0;j<menuDatas[i].children.length;j++){ 
               			 var chilName=menuDatas[i].children[j].label;
               			 var chilurl=menuDatas[i].children[j].url;
               			 var smallIcoPath=menuDatas[i].children[j].smallIcoPath;
               			 var chilid=menuDatas[i].children[j].id;              		 
                    	  //重新向indexpageNew里面添加常用模块
               			 if(map[chilid]){//如果常用模块的id等于页面子id,就添加常用模块  
               				 var stock=$('<li class="shell" addurl='+chilurl+' text='+chilName+' title='+chilName+' ><img src="${ctx}'+smallIcoPath+'" width="48" height="48"><br/><span>'+chilName+'</span></li>');
               				 $('.favorite ul').append(stock);
               		     }; 
               		 };
               	};                	
               	$('#addStock').window('close');
                 }  
             }
        });
   });
});

function joinData(map){//选中的map数据
	var temp = "";		
	for(var n in map){
		temp = temp + map[n] + ",";
    }
	return temp;
}

</script>
</body>
</html>
