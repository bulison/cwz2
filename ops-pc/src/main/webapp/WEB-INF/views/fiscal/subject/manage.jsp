<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>财务科目</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/easyui-tree-extend.js?v=${js_v}"></script>
<style type="text/css">
  .search-tree-box .searchbox{
    width:auto !important;
  }
  .btnBox{
    border-bottom: none !important;
  }
  .btnBox .btn{
  	display: block;
  	width: 39.5px;
  	text-align: center;
  	float: left;
  	border-bottom:1px solid #ccc; 
  	border-right:1px solid #ccc; 
  }
  .auxSign{
    border:1px solid #ccc;
    width:734px;
    margin:5px 0px 5px 115px;
    display: inline-block;
  }
  .billForm p input[type="checkbox"]{
    margin:0 5px 0 0px;
    vertical-align: middle;
  }
  .billForm .checkP font{
    height: auto
  }
  .billForm .checkP{
    width:280px;
    margin:10px 0px 10px 0px !important;
  }
  .billForm .auxSign font{
    width:20px !important;
  }
  .billForm .auxSign .checkP{
    width:auto;
    margin:10px 0 10px 0 !important;
  }
  #optionsBox{
    width:90%;
    border: 1px solid #ccc;
    margin:10px auto; 
  }
  #optionsBox p{
    display: inline-block;
    margin:10px 15px;
    font-size: 16px
  }
  #templateBox{
    text-align: center;
  }
  .search-tree-box{
  margin-top: 50px;}
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">基础资料</a></li>
	        <li><a href="javascript:;" class="curr">财务科目</a></li>
	    </ul>
	</div>


       <div class="search-tree-box">
            <div class="btnBox" style="border-bottom:none;">
         	<fool:tagOpt optCode="fsubjectAdd"><a id="add" class="btn addBtn" href="javascript:;">添加</a></fool:tagOpt>
         	<fool:tagOpt optCode="fsubjectEdit"><a id="edit" class="btn updateBtn" href="javascript:;">修改</a></fool:tagOpt>
         	<fool:tagOpt optCode="fsubjectExport"><a id="export" class="btn outBtn" href="${ctx}/fiscalSubject/export">导出</a></fool:tagOpt>
            <fool:tagOpt optCode="fsubjectImport"><a id="import" class="btn inBtn" href="javascript:;" >导入</a></fool:tagOpt>
         	<fool:tagOpt optCode="fsubjectInit"><a id="initial" class="btn srefreshBtn" href="javascript:;" >格式化</a></fool:tagOpt> 
         	<fool:tagOpt optCode="fsubjectModel"><a id="template" class="btn backupBtn" href="javascript:;" >模版</a></fool:tagOpt>
         	<fool:tagOpt optCode="fsubjectSearch"><div style="display:inline-block;"><input id="searchType"/><input id="search"/></div></fool:tagOpt>
            </div>
            
            <ul id="subjectTree" class="easyui-tree"></ul>
       </div> 
       <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>财务科目</h1>
                </div>             
             </div>
       
       <div class="contentBox"> 
    	    <div id="content"></div>
       </div>
       <div id="editBox"></div>   
       <div id="leadInBox"></div>
       <div id="tempBox"></div>
	   <div id='pop-window'></div>
	   <div id="mybox"></div>
<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
//科目树控件初始化
$('#subjectTree').tree({
	url:'${ctx}/fiscalSubject/getTree?showDisable=1',
	onClick: function(node){
		var id=node.id;
		$('#content').load("${ctx}/fiscalSubject/listPage",function(){
			$('#subjectList').jqGrid('setGridParam',{url:"${ctx}/fiscalSubject/getList",postData:{fid:id}}).trigger("reloadGrid");
			/* setPager($('#subjectList')); */
		});
	}
});

$("#searchType").combobox({
	data:[{value:"0",text:"编号、助记码"},{value:"1",text:"科目名称"},{value:"2",text:"科目类型"}],
	width:75,
	value:"0",
	editable:false,
	onSelect:function(record){
		if(record.value=="2"){
			$("#search").combobox({
				width:125,
				data:[{value:"1",text:'资产'},{value:"2",text:'负债'},{value:"3",text:'共同'},{value:"4",text:'所有者权益'},{value:"5",text:'成本'},{value:"6",text:'损益'}],
				editable:false,
				onSelect:function(record){
					$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:$("#searchType").combobox("getValue"),searchKey:record.value},function(data){
						$('#subjectTree').tree('loadData',data);
					});
				}
			});
		}else{
			$("#search").searchbox({
				width:125,
				searcher:function(value,name){
					$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:$("#searchType").combobox("getValue"),searchKey:value},function(data){
						$('#subjectTree').tree('loadData',data);
					});
				}
			});
		}
	}
});

$("#search").searchbox({
	width:115,
	searcher:function(value,name){
		$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:$("#searchType").combobox("getValue"),searchKey:value},function(data){
			$('#subjectTree').tree('loadData',data);
		});
	}
});

//编辑窗口控件初始化
$("#editBox").window({
	  collapsible:false,
	  minimizable:false,
	  maximizable:false,
	  closed:true,
	  modal:true,
	  top:65,
	  left:230,
	  width:$(content).width()-20
});


//模板选择窗口控件初始化
$("#tempBox").window({
	  title:"选择模板",
	  href:"${ctx}/fiscalSubject/template",
	  collapsible:false,
	  minimizable:false,
	  maximizable:false,
	  closed:true,
	  modal:true,
	  top:65,
	  left:230,
	  width:$(content).width()-20,
	  onClose:function(){
			$("html").css("overflow","");
		}
});

//新增按钮点击事件
$("#add").click(function(){
	var node=$("#subjectTree").tree('getSelected');
	if(node){
		$("#editBox").window('setTitle',"新增科目");
		$('#editBox').window('open');
		$('#editBox').window('refresh',"${ctx}/fiscalSubject/add?fid="+node.id);
	}else{
		$.fool.alert({time:1000,msg:"请先选择节点。"});
	}
});

//新增按钮点击事件
$("#edit").click(function(){
	var node=$("#subjectTree").tree('getSelected');
	if(!node||node.text==="ROOT 科目"){
		$.fool.alert({time:1000,msg:"请先选择节点。"});
		return;
	}
	$("#editBox").window('setTitle',"修改科目");
	$('#editBox').window('open');
	$('#editBox').window('refresh',"${ctx}/fiscalSubject/edit?fid="+node.id);
});

//导出按钮点击事件
/* $("#export").click(function(){
}); */

//导入按钮点击事件
$("#import").click(function(){
	var s={
			target:$("#pop-window"),
			boxTitle:"导入科目",
			downHref:"${ctx}/ExcelMapController/downTemplate?downFile=科目.xls",
			action:"${ctx}/fiscalSubject/import",
			fn:'okCallback()'
	};
	importBox(s);
});

//导入回调
function okCallback(){
	$("#subjectTree").tree("reload");
	$("#pop-window").window("close");
}

//引入按钮点击事件
$("#template").click(function(){
	$("#tempBox").window('open');
});

function userCheck(pwd,type,typeId){
	if(type == 1){
		initAction(pwd)
	}else if(type ==2){
		templateAction(pwd,typeId);
	}
	$("#mybox").window("close");
}

function initAction(pwd){
	$.post('${ctx}/fiscalSubject/saveInitialize',{pwd:pwd},function(data){
		dataDispose(data);		
		if(data.result==0){
			$.fool.alert({time:1000,msg:'格式化成功！',fn:function(){
				$('#subjectList').trigger("reloadGrid");
				$('#subjectTree').tree('reload');
			}});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg,fn:function(){
				$('#subjectList').trigger("reloadGrid");
				$('#subjectTree').tree('reload');
			}});
		}else{
			$.fool.alert({time:1000,msg:"系统繁忙，请稍后再试。"});
		}
		return true;
	});
}

//初始化按钮点击事件
$("#initial").click(function(){
	$.fool.confirm({title:'确认',msg:'格式化将删除所有科目，确定要格式化吗？',fn:function(r){
		if(r){
			$('#mybox').fool('window',{
				top:300,
				left:$(window).width()/2-150,
				modal:true,
				width:300,
				height:150,
				title:"格式化权限验证",
				href:"${ctx}/indexController/userCheck?callBack=userCheck&type=1"
			});
		 }
	}});
});
$('#subjectTree').css('margin-top',$('.btnBox').outerHeight());
</script>
</body>
</html>
