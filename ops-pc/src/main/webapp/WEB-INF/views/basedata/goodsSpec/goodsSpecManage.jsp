<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>货品属性管理</title>
  <%@ include file="/WEB-INF/views/common/header.jsp"%>
  <%@ include file="/WEB-INF/views/common/js.jsp"%>
  <link rel="stylesheet" href="${ctx}/resources/css/accordionManage.css?v=${css_v}"/>
  <script type="text/javascript" src="${ctx}/resources/js/easyui-tree-extend.js?v=${js_v}"></script>
</head>
<body>
  <div class="titleBar">
	<div id="titleContainer" class="shadow">
	  <div id="square"><p id="triangle"></p></div><h1>货品属性管理</h1>
	</div>
  </div>
  <div class="toolBar">
    <a id="addBtn" href="javascript:;" title="新增属性组" class="btn-ora-add" type="button" onClick="addType(this)">新增</a><!-- <input id="searcher" type="text" /><input id="searchBtn" class="btn-s btn-blue4" type="button" value="查询"/> -->
      <div class="editBox">
        <div class="close-btn" onClick="hideEdBox(this,true)"></div>
        <form class="edForm">
          <input class="parentId" name="parentId" style="display:none" value="${rootId}"/>
          <p><font><em>*</em>是否有效：</font><input type="radio" name="recordStatus" value="SAC" checked="checked"/><span>是</span>&emsp;&emsp;<input type="radio" name="recordStatus" value="SNU"/><span>否</span></p>
          <br/>
          <p><font><em>*</em>编号：</font><input class="code easyui-validatebox easyui-tooltip" name="code" data-options="validType:'isBank',required:true,novalidate:true,position:'right',content:'请输入属性组编号，如01。'"/></p>
          <p><font><em>*</em>属性组名称：</font><input class="name easyui-validatebox easyui-tooltip" name="name" data-options="validType:'isBank',required:true,novalidate:true,position:'right',content:'请输入属性组名称，如颜色。'"/></p>
          <p><font>描述：</font><input class="describe easyui-validatebox" name="describe" data-options=""/></p>
          <br/>
          <p>
            <font></font><input class="save-btn" type="button" value="保存" onClick="save(this,'0')"/>
          </p>
        </form>
      </div>
  </div>
  <div class="data-list">
    <div id="data-accordion">
    </div>
  </div>
<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
  /* $("#searcher").textbox({
	  prompt:"请输入搜索内容",
	  width:240,
	  height:40
  }); */
  
  //初始化控件-折叠框
  $("#data-accordion").accordion({
	  border:true,
	  multiple:true,
	  width:$(".attrList").width(),
	  onAdd:function(title,index){
		  var panel=$("#data-accordion").accordion("getPanel",index);
		  $(panel).siblings(".panel-header").append("<div class='panel-edit'><a href='javascript:;' class='editType' onClick='editType(event,this)'></a></div>");
	  }
  });
  
  //声明全局变量initContent
  var initContent='<a class="addItems" title="新增属性" href="javascript:void(0)" onClick="showDetails(this)"></a></div><div class="array"></div><div class="editBox"><div class="close-btn" onClick="hideEdBox(this)"></div><form class="edForm"><input class="flag" name="flag" style="display:none"/><input class="updateTime" name="updateTime" style="display:none"/><input class="fid" name="fid" style="display:none"/><input class="parentId" name="parentId" style="display:none"/><p><font><em>*</em>编号：</font><input class="code easyui-validatebox easyui-tooltip" name="code" data-options="validType:\'isBank\',required:true,novalidate:true,position:\'right\',content:\'请输入属性编号，如01。\'"/></p><p><font><em>*</em>属性名称：</font><input class="name easyui-validatebox easyui-tooltip" name="name" data-options="validType:\'isBank\',required:true,novalidate:true,position:\'right\',content:\'请输入属性名称，如红色。\'"/></p><p><font>描述：</font><input class="describe easyui-validatebox" name="describe" data-options=""/></p><p><font><em>*</em>是否有效：</font><input type="radio" name="recordStatus" value="SAC" checked="checked"/><span>是</span>&emsp;&emsp;<input type="radio" name="recordStatus" value="SNU"/><span>否</span></p><br/><p><font></font><input class="save-btn" type="button" value="保存" onClick="save(this,1)"/><input class="delete-btn" type="button" value="删除" onClick="delet(this)"/></p></form></div>';
  //声明全局变量unitData
  var unitData="";
  //调用initialData方法加载数据
  initialData("${ctx}/goodsspec/getAll");
  
  //清空并加载折叠框的数据
  function initialData(url){
	  var panels=$("#data-accordion").accordion("panels");
	  if(panels.length>0){
		  var title=[];
		  for(var i=0;i<panels.length;i++){
			  title.push($(panels[i]).panel("options").title);
		  }
		  for(var j=0;j<title.length;j++){
			  $("#data-accordion").accordion("remove",title[j]);
		  }
	  }
	  $.ajax({ 
			url: url,
			data: {},
			dataType: 'json',
			type: "post", 
			cache : false, 
			success: function(data){
				unitData=data[0].children;
				buildAccordion(unitData,initContent);
			},
			error: function(){
				$.fool.alert({"msg":"系统繁忙，请稍后再试!"});
			}
	  });
  }
  
  //组装数据，添加到折叠框上
  function buildAccordion(array,initContent){
	  var id="";
	  var children="";
	  var title="";
	  var content="";
	  for(var i=0;i<unitData.length;i++){
		  id=unitData[i].id;
		  title=unitData[i].attributes.detail.name;
		  children=unitData[i].children;
		  for(var j=0;j<children.length;j++){
			  content=content+'<a id="'+children[j].id+'" title="编号：'+children[j].attributes.detail.code+'" href="javascript:void(0)" class="easyui-tooltip items enable'+children[j].attributes.detail.recordStatus+'" data-options="showDelay:1,hideDelay:1" onClick="showDetails(this,\''+children[j].id+'\')"><span class="attr-name">'+children[j].attributes.detail.name+'</span></a>';
		  }
		  $("#data-accordion").accordion("add",{
			  id:id,
			  title: title,
			  content: '<div class=listBox>'+content+initContent,
			  selected: true,
		  });
		  id="";
		  children="";
		  title="";
		  content="";
	  }
  }
  
  //点击新增按钮，拉出新增框
  function addType(target){
	  var edbox=$(target).siblings(".editBox");
	  edbox.slideToggle("fast");
  };
  
  //拉出小类编辑框，并调整位置、加载数据
  function showDetails(target,id){
	  var edbox=$(target).parent().parent().find(".editBox");
	  var array=$(target).parent().parent().find(".array");
	  var listbox=$(target).parent();
	  if(edbox.css("display")=="none"){
		  edbox.slideDown("fast");
		  array.show();
		  var targetY=$(target).position().top;
		  var targetX=$(target).position().left;
		  var arrayY=array.position().top;
		  var arrayX=array.position().left;
	      var edboxY=edbox.position().top;
	      var edY=targetY-edboxY;
	      var arrY=targetY-arrayY;
	      var arrX=targetX-arrayX;
	      var edboxTop=edbox.css("top").slice(0,-2);
	      var arrayTop=array.css("top").slice(0,-2);
	      var arrayLeft=array.css("left").slice(0,-2);
	      array.css("top",parseInt(arrayTop)+arrY+30);
	      array.css("left",parseInt(arrayLeft)+arrX+(($(target).width()+26)/2)-10);
	      edbox.css("top",parseInt(edboxTop)+edY+40+6);
	      
	      var edboxBottomY=edbox.position().top+edbox.find("form").height()+2;
	      var listboxBottomY=listbox.position().top+listbox.height()+20;
	      if(edboxBottomY>=listboxBottomY){
	    	  $(target).parent().parent().animate({height:edboxBottomY},"fast");
	      }else{
	    	  $(target).parent().parent().animate({height:listboxBottomY},"fast");
	      }
	  }else{
		  if(id==edbox.find(".fid").val()){
			  edbox.slideUp("fast",function(){array.hide();edbox.find(".edForm").form("reset");edbox.find(".edForm").form("disableValidation");});
			  var listboxBottomY=listbox.position().top+listbox.height()+20;
			  $(target).parent().parent().animate({height:listboxBottomY},"fast");
			  return;
		  }else{
			  var targetY=$(target).position().top;
			  var targetX=$(target).position().left;
			  var arrayY=array.position().top;
			  var arrayX=array.position().left;
		      var edboxY=edbox.position().top;
		      var edY=targetY-edboxY;
		      var arrY=targetY-arrayY;
		      var arrX=targetX-arrayX;
		      var edboxTop=edbox.css("top").slice(0,-2);
		      var arrayTop=array.css("top").slice(0,-2);
		      var arrayLeft=array.css("left").slice(0,-2);
		      array.css("top",parseInt(arrayTop)+arrY+30);
		      array.css("left",parseInt(arrayLeft)+arrX+(($(target).width()+26)/2)-10);
		      edbox.css("top",parseInt(edboxTop)+edY+40+6);
		      
		      var edboxBottomY=edbox.position().top+edbox.find("form").height()+2;
		      var listboxBottomY=listbox.position().top+listbox.height()+20;
		      if(edboxBottomY>=listboxBottomY){
		    	  $(target).parent().parent().animate({height:edboxBottomY},"fast");
		      }else{
		    	  $(target).parent().parent().animate({height:listboxBottomY},"fast");
		      }
		  }
	  }
	  giveValue(id,edbox);
  }
  
  //给编辑框赋值
  function giveValue(id,edbox,group){
	  if(id){
		  $.ajax({ 
				url: "${ctx}/goodsspec/getById",
				data: {id:id},
				dataType: 'json',
				type: "post", 
				cache : false, 
				success: function(data){
					edbox.find(".fid").val(data.fid);
					edbox.find(".parentId").val(data.parentId);
					edbox.find(".updateTime").val(data.updateTime);
					edbox.find(".flag").val(data.flag);
					edbox.find(":radio[value="+data.recordStatus+"]").click();
					edbox.find(".code").val(data.code);
					edbox.find(".name").val(data.name);
					edbox.find(".describe").val(data.describe);
					if(group!="group"){
						edbox.find(".name").prev().html("<em>*</em>属性名称：");
						edbox.find(".name").tooltip({content:"请输入属性名称，如红色。"});
						edbox.find(".code").tooltip({content:"请输入属性编号，如01。"});
					}else{
						edbox.find(".name").prev().html("<em>*</em>属性组名称：");
						edbox.find(".name").tooltip({content:"请输入属性组名称，如颜色。"});
						edbox.find(".code").tooltip({content:"请输入属性组编号，如01。"});
					}
				},
				error: function(){
					$.fool.alert({"msg":"系统繁忙，请稍后再试!"});
				}
		  });
	  }else{
		  var parentId=edbox.parent().attr("id");
		  edbox.find(".parentId").val(parentId);
		  edbox.find(".fid").val("");
		  edbox.find(".updateTime").val("");
		  edbox.find(".flag").val("");
		  edbox.find(":radio[value='1']").click();
		  edbox.find(".code").val("");
		  edbox.find(".name").val("");
		  edbox.find(".describe").val("");
	  }
  }
  
  //隐藏编辑框
  function hideEdBox(target,stay){
	  var edbox=$(target).parent();
	  var array=$(target).parent().siblings(".array");
	  edbox.slideUp("fast",function(){array.hide();edbox.find(".edForm").form("reset");edbox.find(".edForm").form("disableValidation");});
	  if(!stay){
		  var listbox=$(target).parent().siblings(".listBox");
		  var listboxBottomY=listbox.position().top+listbox.height()+20;
		  $(target).parent().parent().animate({height:listboxBottomY},"fast");  
	  }
  }
  
  //保存
  function save(target,mark){
	  var form=$(target).parent().parent();
	  form.form("enableValidation");
	  if(!form.form("validate")){
		return false;  
	  }
	  $(".save-btn").attr("disabled","disabled");
	  var data=form.serializeJson();
	  var url = "${ctx}/goodsspec/save";
	  $.ajax({ 
			url: url,
			data: data,
			dataType: 'json',
			type: "post", 
			cache : false, 
			success: function(data){
				if(data.returnCode=="0"){
					$.fool.alert({time:1000,msg:"保存成功",fn:function(){
						if(dhxkey == 1){
	                        selectTab(dhxname,dhxtab);
	                    }
						var flag=form.find(".flag").val();
						var name=form.find(".name").val();
						var id=form.find(".fid").val();
						var code=form.find(".code").val();
						var recordStatus=form.find(":radio:checked").val();
						var returnId=data.data.fid;
						if(mark=="0"){
							var content="";
							$("#data-accordion").accordion("add",{
								  id:returnId,
								  title: name,
								  content: '<div class=listBox>'+content+initContent,
								  selected: true,
							});
							hideEdBox(form,true);
						}else if(mark=="1"){
							var listbox=$(target).closest(".editBox").siblings(".listBox");
							if(id==returnId){
								if(flag==1){
									var item=listbox.children("[id="+id+"]");
									item.children().text(name);
									item.attr("title","编号："+code);
									$("#"+id).tooltip({
										  content:'<span style="color:black">编号：'+code+'</span>',
										  showDelay:1,
									});
									hideEdBox(form,false);
								}else{
									$(target).closest(".panel-body").siblings(".panel-header").find(".panel-title").text(name);
									hideEdBox(form,false);
								}
							}else{
								var addBtn=listbox.find(".addItems");
								addBtn.before('<a id="'+returnId+'" href="javascript:void(0)" title="编号：'+code+'" class="easyui-tooltip items enable'+recordStatus+'" onClick="showDetails(this,\''+returnId+'\')"><span class="attr-name">'+name+'</span></a>');
								$("#"+returnId).tooltip({
									  content:'<span style="color:black">'+$("#"+returnId).attr("title")+'</span>',
									  showDelay:1,
								});
								hideEdBox(form,false);
							}
						}
						$(".save-btn").removeAttr("disabled");
					}});
				}if(data.returnCode == "1" ){
					$.fool.alert({time:2000,msg:data.message});
					$(".save-btn").removeAttr("disabled");
				}
			},
			error: function(){
				$.fool.alert({"msg":"系统繁忙，请稍后再试!"});
				$(".save-btn").removeAttr("disabled");
			}
	  });
  }
  
  //删除
  function delet(target){
	  var form=$(target).parent().parent();
	  var id=$(target).parent().siblings(".fid").val();
	  var flag=$(target).parent().siblings(".flag").val();
	  var url = "${ctx}/goodsspec/delete";
	  if(id){
		  $.ajax({ 
				url: url,
				data: {id:id},
				dataType: 'json',
				type: "post", 
				cache : false, 
				success: function(data){
					if(data.returnCode=="0"){
						$.fool.alert({time:1000,msg:"删除成功",fn:function(){
							var listbox=$(target).closest(".editBox").siblings(".listBox");
							if(flag==1){
								listbox.find("a").remove("[id="+id+"]");
								hideEdBox(form,false);
							}else{
								var title=listbox.parent().siblings(".panel-header").find(".panel-title").text();
								$("#data-accordion").accordion("remove",title);
								hideEdBox(form,false);
							}
						}});
					}if(data.returnCode == "1" ){
						$.fool.alert({time:2000,msg:data.message});
					}
				},
				error: function(){
					$.fool.alert({"msg":"系统繁忙，请稍后再试!"});
				}
		  }); 
	  }
  }
  
  //拉出大类编辑框
  function editType(e,target){
	  var panelHeader=$(target).closest(".panel-header");
	  var id=panelHeader.siblings(".panel-body").attr("id");
	  var edbox=panelHeader.siblings(".panel-body").find(".editBox");
	  var array=panelHeader.siblings(".panel-body").find(".array");
	  var listbox=panelHeader.siblings(".panel-body").find(".listBox");
	  if(panelHeader.attr("class")!="panel-header accordion-header accordion-header-selected"){
		  panelHeader.click();
		  if(edbox.css("display")=="none"){
			  edbox.slideDown("fast");
			  giveValue(id,edbox,"group");
			  array.show();
			  array.css("left",panelHeader.siblings(".panel-body").width()-35);
			  array.css("top",-listbox.height()-20);
			  edbox.css("top",-listbox.height()-23);
			  var edboxBottomY=edbox.position().top+edbox.find("form").height()+2;
		      var listboxBottomY=listbox.position().top+listbox.height()+20;
		      if(edboxBottomY>=listboxBottomY){
		    	  panelHeader.siblings(".panel-body").animate({height:edboxBottomY},"fast");
		      }else{
		    	  panelHeader.siblings(".panel-body").animate({height:listboxBottomY},"fast");
		      }
		  }else{
			  var edboxBottomY=edbox.find("form").height()+18;
		      var listboxBottomY=listbox.height()+20;
		      if(edboxBottomY>=listboxBottomY){
		    	  panelHeader.siblings(".panel-body").animate({height:edboxBottomY},"fast");
		      }else{
		    	  panelHeader.siblings(".panel-body").animate({height:listboxBottomY},"fast");
		      }
		  }
	  }else{
		  if(edbox.css("display")=="none"){
			  edbox.slideDown("fast");
			  giveValue(id,edbox,"group");
			  array.show();
			  array.css("left",panelHeader.siblings(".panel-body").width()-35);
			  array.css("top",-listbox.height()-20);
			  edbox.css("top",-listbox.height()-23);
			  var edboxBottomY=edbox.position().top+edbox.find("form").height()+2;
		      var listboxBottomY=listbox.position().top+listbox.height()+20;
		      if(edboxBottomY>=listboxBottomY){
		    	  panelHeader.siblings(".panel-body").animate({height:edboxBottomY},"fast");
		      }else{
		    	  panelHeader.siblings(".panel-body").animate({height:listboxBottomY},"fast");
		      }
		  }else{
			  hideEdBox(edbox.find("form"),false);
			  edbox.find("form").form("reset");
		  }
	  }
	  e.stopPropagation();
  }
  
</script>
</body>
</html>
