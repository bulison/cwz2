<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>计划管理信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<style>
#addBox{
  background-color: #EAEDF2
}
/* #search-form span{
  margin: 5px 5px 5px 0px
}
#search-form .dhxDiv{
  margin: 5px 5px 5px 0px
} */
#search-form a{
  margin-right: 5px;
  margin-bottom: 10px
}
#Inquiry{
  margin-left: 5px;
}
#grabble{
  float: right;
}
#bolting{
  width: 160px;
  height: 27px;
  position:relative; 
  border: 1px solid #ccc; 
  background: #fff;
  margin-right: 25px;
}
.button_a{
  display:block; 
  width:25px; 
  height:25px;
  background:#76D5FC; 
  -moz-border-radius: 3px;
  /* Gecko browsers */-webkit-border-radius: 3px;
  /* Webkit browsers */border-radius:3px;
  /* W3C syntax */float: right; 
  right:29px; 
  top: 63px;
  position: absolute;
}
.button_span{
  width:0; 
  height:0; 
  border-left:8px solid transparent; 
  border-right:8px solid transparent;
  border-top:8px solid #fff;
  top:10px; 
  position: absolute;
  right:5px;
}
.input_div{
  display: none;
  background:#F5F5F5; 
  padding: 10px 0px 5px 0px; 
  border: 1px solid #D5DBEA;
  position: absolute;
  right: 23px; 
  top:93px;
  z-index: 1;
}
.input_div p{ 
  font-size: 12px; 
  color:#747474;
  vertical-align: middle;
  text-align: right;  
  margin: 5px 20px 5px 10px;
  width:240px;
}
.button_clear{ 
  border-top: 1px solid #D5DBEA;
  margin-top: 10px; 
  padding-top:8px;
  text-align: right;
}
.roed{
   -moz-transform:scaleY(-1);
   -webkit-transform:scaleY(-1);
   -o-transform:scaleY(-1);
   transform:scaleY(-1);
   filter:FlipV();
}
#searchBar{padding:5px 0px}
#searchBar font{width:100px}
</style>
</head>
<body>
    <div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>计划管理</h1>
       </div>             
    </div>
	<div id="addBox"></div> 
	<div style="margin:10px 0px 10px 0px">  
	  <a href="javascript:;" id="add" class="btn-ora-add" >新增</a>
	  <a href="javascript:;" id="refresh" class="btn-ora-refresh" >刷新</a> 
	  <a href="javascript:;" id="executeSchedule" class="btn-ora-add" style="display:none;">调度测试</a>
	  <a href="javascript:;" id="executeSchedule2" class="btn-ora-add" style="display:none;" >调度测试2</a>
	  <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly" /><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div> 
	  <div class="input_div">
		  <form id="search-form">
		    <p>计划编号：<input id="search-code" name="code"/></p>
		    <p>计划名称：<input id="search-name" name="name"/></p>
		    <p class="dhx">部门：<input id="search-deptId" name="deptId"/></p>
		    <p class="dhx">计划状态：<input id="search-status" name="status"/></p>
		    <p>开始时间：<input id="search-antipateStartTime" name="antipateStartTime"/></p>
<!-- 		    <p style="text-align: left"><font style="margin-left:80px">-</font></p> -->
		    <p>结束时间：<input id="search-antipateEndTime" name="antipateEndTime"/></p>
		    <p>发起人：<input id="search-initiaterId" name="initiaterId"/></p>
		    <p>责任人：<input id="search-principalerId" name="principalerId"/></p>
		    <p>审核人：<input id="search-auditerId" name="auditerId"/></p>
		    <p>计划级别：<input id="search-planLevelId" name="planLevelId"/></p>
		    <p>保密级别：<input id="search-securityLevelId" name="securityLevelId"/></p>
		    <div class="button_clear">
	            <a href="javascript:;" id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	            <a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	            <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	        </div>
		  </form>
	  </div>
	</div>
	<table id="planList"></table>
	<div id="pager"></div>
	<div id="taskListWin">
	  <p style="margin:5px;font-size:16px;font-weight: bolder;">计划模板：<input id="planTempSelect" /></p>
	  <div id="taskList"></div>
	  <form id="searchBar" class="form1">
	    <p><font>预计开始时间：</font><input id="antipateStartTime-search" name="antipateStartTime"/>-<input id="antipateEndTime-search" name="antipateEndTime"/></p>
	    <p><font>计划编号：</font><input id="code-search" name="code"/></p>
	    <p><font>计划名称：</font><input id="name-search" name="name"/></p>
	    <a href="javascript:;" id="btn-search" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	    <a href="javascript:;" id="btn-clear" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	  </form>
	  <table id="mergerPlanList"></table>
	  <div id="mergerPlanPager"></div>
	  <div style="margin:5px 20px;float:right" id="winBtn">
	    <a id="saveTemp" class="btn-blue btn-s" onclick="saveTemp()">保存到模板</a>
	    <a id="nextStep" class="btn-blue btn-s" onclick="nextStep()">下一步</a>
	    <a id="lastStep" class="btn-blue btn-s" onclick="lastStep()">上一步</a>
	    <a id="mergePlan" class="btn-blue btn-s" onclick="mergePlan()">合并计划</a>
	  </div>
	</div>
</body>
</html>
<script type="text/javascript">
var recordStatusData=[{value:"100",text:"草稿"},{value:"101",text:"已提交待审核"},{value:"102",text:"已审核办理中"},{value:"103",text:"已延迟"},{value:"104",text:"已终止"},{value:"105",text:"已完成"}];
$("#planTempSelect").fool("dhxCombo",{
	  width:160,
	  height:32,
	  prompt:"选择计划模板",
	  data:getComboData("${ctx}/flow/planTemplate/queryAllTemp?rows=9999999"),
	  setTemplate:{
		  input:"#name#",
		  option:"#name#"
	  },
	  editable:false,
	  focusShow:true,
});
$('#taskListWin').window({
	top:50,
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true,
	width:1000,
	height:600
}); 
$('#taskList').treegrid({
	height:450,
	idField:'fid',
	treeField:'code',
	/* cascadeCheck:true, */
	fitColumns:true,
	remoteSort:false,
	singleSelect:false,
	columns:[[
                {field:'fid',title:'fid',checkbox:true,},
		  		{field:'code',title:'编号',width:200,align:"left"}, 
		  		{field:'name',title:'名称',width:100}, 
		  		{field:'antipateStartTime',title:'预计开始时间',width:100}, 
		  		{field:'antipateEndTime',title:'预计结束时间',width:100},
		  		{field:'taskLevelName',title:'事件级别',width:100},
		  		{field:'securityLevelName',title:'保密等级',width:100},
		  		{field:'amount',title:'预计金额',width:100},
		  		{field:'undertakerName',title:'承办人',width:100},
		  		{field:'principalerName',title:'责任人',width:100},
		  		{field:'auditerName',title:'审核人',width:100},
		      ]],
	onSelect:function(row){
		var childs=row.children;
		if(childs.length>0){
			for(var i=0;i<childs.length;i++){
				$('#taskList').treegrid("select",childs[i].fid);
			}
		}
	},
    onUnselect:function(row){
    	var childs=row.children;
		if(childs.length>0){
			for(var i=0;i<childs.length;i++){
				$('#taskList').treegrid("unselect",childs[i].fid);
			}
		}
    }
});
$("#mergerPlanList").jqGrid({	
	/* datatype:"local", */
	width:995,
	height:400,
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	multiselect:true,
	pager:"#mergerPlanPager",
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
	colModel:[
	  		{name:'fid',label:'fid',hidden:true,width:100,align:'center'},
	  		{name:'code',label:'计划编号',sortable:true,width:100,align:'center',},
	  		{name:'name',label:'计划名称',sortable:true,width:100,align:'center',},
	  		{name:'deptName',label:'部门',sortable:true,width:100,align:'center',}, 
	  		{name:'principalerName',label:'责任人',sortable:true,width:100,align:'center',}, 
	  		{name:'initiaterName',label:'发起人',sortable:true,width:100,align:'center',}, 
	  		{name:'auditerName',label:'审核人',sortable:true,width:100,align:'center',}, 
	  		{name:'status',label:'状态',sortable:true,width:100,align:'center',}, 
	  		{name:'planLevelName',label:'计划级别',sortable:true,width:100,align:'center',}, 
	  		{name:'securityLevelName',label:'保密级别',sortable:true,width:100,align:'center',}, 
	  		{name:'antipateStartTime',label:'预计开始时间',sortable:true,width:100,align:'center',},
	  		{name:'antipateEndTime',label:'预计结束时间',sortable:true,width:100,align:'center',},
	      ],
}).navGrid('#mergerPlanPager',{add:false,del:false,edit:false,search:false,view:false});
var boxWidth=162,boxHeight=30;//统一设置输入框大小
$("#search-code").textbox({
	width:160,
	height:30,
});
$("#search-name").textbox({
	width:160,
	height:30,
});
$("#code-search").textbox({
	width:100,
	height:25,
});
$("#name-search").textbox({
	width:100,
	height:25,
});

//部门初始化	
var deptValue='';	
	$.ajax({
		url:"${ctx}/orgController/getAllTree",
		async:false,		
		success:function(data){		  	
			deptValue=formatTree(data[0].children,'deptName','id');	
	    }
		});
	var deptNameValue= $("#search-deptId").fool("dhxCombo",{
		  width:162,
		  height:32,
		  data:deptValue,
		  toolsBar:{
			  name:"部门",
			  refresh:true
		  },
		  editable:false,
		  focusShow:true,		
	}); 

 var statusValue= $("#search-status").fool("dhxCombo",{
	  width:162,
	  height:32,
	  editable:false,
	  focusShow:true,
	  data:[
	        {
	        value:'100',
	        text:'草稿',
	        },
		      {
		    	  value: '101',
			      text: '已提交待审核'
		      },{
		    	  value: '102',
			      text: '已审核办理中'
		      },{
		    	  value: '103',
			      text: '已延迟'
		      },{
		    	  value: '104',
			      text: '已终止'
		      }
		      ,{
		    	  value: '105',
			      text: '已完成'
		      }
		     ],	
});	 

$("#search-antipateStartTime").datebox({
	editable:false,
	width:160,
	height:30,
});
$("#search-antipateEndTime").datebox({
	editable:false,
	width:160,
	height:30,
});
$("#antipateStartTime-search").datebox({
	editable:false,
	width:100,
	height:25,
});
$("#antipateEndTime-search").datebox({
	editable:false,
	width:100,
	height:25,
});
	
var initiaterIdValue='';
$.ajax({
	url:getRootPath()+"/userController/vagueSearch",
	async:false,		
	success:function(data){		  	
		initiaterIdValue=formatData(data,'fid');	
    }
	});
	
var initiaterId = $('#search-initiaterId').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	focusShow:true,
	width:boxWidth,
	height:boxHeight,
	data:initiaterIdValue,
	hasDownArrow:false,
	setTemplate:{
		  input:"#userName#",
		  columns:[
				{option:'#userCode#',header:'编号',width:100},
				{option:'#userName#',header:'名称',width:100},			
			     ]
	  },
	  toolsBar:{
		  refresh:true
	  },
	filterUrl:getRootPath()+"/userController/vagueSearch",
	onChange:function(value,text){
		$("#search-auditerId").val(value);
	}
});

var principalerIdValue='';	
$.ajax({
	url:getRootPath()+"/userController/vagueSearch",
	async:false,		
	success:function(data){		  	
		principalerIdValue=formatData(data,'fid');	
    }
	});
	
var principalerId = $('#search-principalerId').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	focusShow:true,
	width:boxWidth,
	height:boxHeight,
	data:principalerIdValue,
	hasDownArrow:false,
	filterUrl:getRootPath()+"/userController/vagueSearch",
	setTemplate:{
		  input:"#userName#",
		  columns:[
				{option:'#userCode#',header:'编号',width:100},
				{option:'#userName#',header:'名称',width:100},			
			     ]
	  },
	  toolsBar:{
		  refresh:true
	  },
	onChange:function(value,text){
		$("#search-auditerId").val(value);
	}
});

var auditerIdValue='';	
$.ajax({
	url:getRootPath()+'/userController/vagueSearch',
	async:false,		
	success:function(data){		  	
		auditerIdValue=formatData(data,'fid');	
    }
	});
	
var auditerId = $('#search-auditerId').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	focusShow:true,
	width:boxWidth,
	height:boxHeight,
	data:auditerIdValue,
	hasDownArrow:false,
	filterUrl:getRootPath()+'/userController/vagueSearch',
	setTemplate:{
		  input:"#userName#",
		  columns:[
				{option:'#userCode#',header:'编号',width:100},
				{option:'#userName#',header:'名称',width:100},			
			     ]
	  },
	  toolsBar:{
		  refresh:true
	  },
	onChange:function(value,text){
		$("#search-auditerId").val(value);
	}
});

var planLevelId='';	
$.ajax({
	url:"${ctx}/flow/taskLevel/queryAll",
	async:false,		
	success:function(data){		  	
		planLevelId=formatData(data,'fid');	
    }
	});
var planLevelIdName= $("#search-planLevelId").fool("dhxCombo",{
	  width:162,
	  height:32,
	  data:planLevelId,
	  setTemplate:{
		  input:"#name#",
		  option:"#name#"
	  },
	  toolsBar:{
		  name:"计划级别",
		  refresh:true
	  },
	  editable:false,
	  focusShow:true,		
}); 

 var securityLevelId='';	
 $.ajax({
 	url:"${ctx}/flow/security/queryAll",
 	async:false,		
 	success:function(data){		  	
 		securityLevelId=formatData(data,'fid');	
     }
 	});
 var securityLevelIdName= $("#search-securityLevelId").fool("dhxCombo",{
 	  width:162,
 	  height:32,
 	  data:securityLevelId,
 	  setTemplate:{
 		  input:"#name#",
 		  option:"#name#"
 	  },
 	 toolsBar:{
		  name:"保密级别",
		  refresh:true
	  },
 	  editable:false,
 	  focusShow:true,		
 }); 
 
//点击下拉按钮
$('.button_a').click(function(){
	$('.input_div').toggle(2);	
	var s=$('.button_a').attr('class'); 
	if(s=="button_a roed"){
		$('.button_a').removeClass('roed');	
	}else{
		$(this).addClass('roed');
	}
});

//点击关闭隐藏
$('#clear-btndiv').click(function(){
	$('.input_div').hide(2);
	$('.button_a').removeClass('roed');
});

//鼠标获取焦点
$('#bolting').focus(function (){ 
	$('.input_div').show(2);
	$('.button_a').addClass('roed');
}); 
$("#btn-search").click(function(){
	var options = $("#searchBar").serializeJson();
	$('#mergerPlanList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
});
$("#btn-clear").click(function(){
	$("#searchBar").form("clear");
	var inputs=$("#searchBar").find(".dhxDiv");
	for(var i=0;i<inputs.length;i++){
		inputs[i].comboObj.setComboText("");
	}
}); 
$("#search-btn").click(function(){
	var options = $("#search-form").serializeJson();
	$('#planList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
});
$("#clear-btn").click(function(){
	$("#search-form").form("clear");
	var inputs=$("#search-form").find(".dhxDiv");
	for(var i=0;i<inputs.length;i++){
		inputs[i].comboObj.setComboText("");
	}
}); 

//设置鼠标放进去自动出来下拉列表
$("#search-form").find("input[_class]").each(function(i,n){($(this));});


$('#addBox').window({
	top:10,
	left:0,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false, 
	width:$(window).width()-30,
	height:$(window).height()-60,
	closed:true,
	modal:true,
});
$("#add").click(function(){
	var src="/flow/plan/axis";
	var text='新增计划';
	parent.kk(src,text);
});
$('#planList').jqGrid({	
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/flow/plan/query',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#planList")[0].addJSONData(data.responseJSON);
		    	}
		    }
		});
	},
	autowidth:true,
	height:$(window).outerHeight()-200+"px",
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
	colModel:[
		    {name:'_code',label:'_code',hidden:true,width:100,align:'center',formatter:function(value,options,row){
	  				return row.code; 
	  		}},
	  		{name:'fid',label:'fid',hidden:true,width:100,align:'center'},
	  		{name:'code',label:'计划编号',sortable:true,width:100,align:'center',formatter:function(value,options,row){
	  				return '<a href="javascript:;" onclick="editById(\''+row.fid+'\')">'+value+'</a>'; 
	  		}},
	  		{name:'name',label:'计划名称',sortable:true,width:100,align:'center',},
	  		{name:'principalerName',label:'责任人',sortable:true,width:100,align:'center',}, 
	  		{name:'initiaterName',label:'发起人',sortable:true,width:100,align:'center',}, 
	  		{name:'auditerName',label:'审核人',sortable:true,width:100,align:'center',}, 
	  		{name:'status',label:'状态',sortable:true,width:100,align:'center',formatter:function(value,options,row){
	  			for(var i=0; i<recordStatusData.length; i++){
	    			if (recordStatusData[i].value == row.status) return recordStatusData[i].text;
	    		}
	    		return row.status; 
	  		}}, 
	  		{name:'planLevelName',label:'计划级别',sortable:true,width:100,align:'center',}, 
	  		{name:'securityLevelName',label:'保密级别',sortable:true,width:100,align:'center',}, 
	  		{name:'antipateStartTime',label:'预计开始时间',sortable:true,width:100,align:'center',},
	  		{name:'antipateEndTime',label:'预计结束时间',sortable:true,width:100,align:'center',},
	  		{name:'actualStartTime',label:'实际开始时间',sortable:true,width:100,align:'center',},
	  		{name:'actualEndTime',label:'实际结束时间',sortable:true,width:100,align:'center',},
	  		{name:'effectiveYieldrate',label:'收益率',sortable:true,width:100,align:'center',},
	  		{name:'action',label:'操作',width:100,align:'center',formatter:function(value,options,row){
	  			var s="";
	  			s += '<a class="btn-save" title="保存为模板" href="javascript:;" onclick="saveTempWin(\''+row.fid+'\',\''+options.rowId+'\')"></a> ';
	  			if(row.status=="100"||row.status=="102"){
	  				s += ' <a class="btn-backup" title="计划合并" href="javascript:;" onclick="mergePlanWin(\''+row.fid+'\',\''+row.status+'\',\''+options.rowId+'\')"></a> ';
	  			}
	  			return s; 
	  		}}
	      ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function editById(fid){
	var src="/flow/plan/axis?id="+fid;
	var text='浏览计划';
	parent.kk(src,text);
}

$("#executeSchedule").click(function(){
	$.ajax({
		url:"${ctx}/flow/plan/executeSchedule",
		async:false,
		data:{},
		success:function(data){
			if(data.returnCode == '0'){
				$.fool.alert({msg:"调度成功"});
			}else{
				$.fool.alert({msg:"调度失败"});
			}
		},
		error:function(){
			$.fool.alert({msg:"调度失败"});
		}
	});
});

$("#executeSchedule2").click(function(){
	$.ajax({
		url:"${ctx}/flow/plan/executeSchedule2",
		async:false,
		data:{},
		success:function(data){
			if(data.returnCode == '0'){
				$.fool.alert({msg:"调度成功"});
			}else{
				$.fool.alert({msg:"调度失败"});
			}
		},
		error:function(){
			$.fool.alert({msg:"调度失败"});
		}
	});
});

$("#refresh").click(function(){
	$('#planList').trigger('reloadGrid');
});

//保存模板窗口
function saveTempWin(fid,index){
	$($("#taskList").datagrid("getPanel")).show();
	$('#taskListWin').window("open");
	$('#taskListWin').window("setTitle","保存到模板");
	$('#taskList').treegrid({
		singleSelect:false,
		onBeforeSelect:function(row){
			var selected=$('#taskList').treegrid("getSelections").concat();
			if(row.level==0){
				$('#taskList').treegrid("selectAll");
				return false;
			}
			outer:for(var i=0;i<selected.length;i++){
				if((row.level!=selected[i].level&&selected[i].fid!=row._parentId)||(row.level==selected[i].level&&selected[i]._parentId!=row._parentId)){
					for(var j=0;j<selected.length;j++){
						if(selected[i]._parentId==selected[j].fid||selected[j].fid==row._parentId){
							continue outer;
						}
					}
					$('#taskList').treegrid("unselect",selected[i].fid);
				}
			}
		},
		onSelect:function(row){
			var childs=row.children;
			if(childs.length>0){
				for(var i=0;i<childs.length;i++){
					$('#taskList').treegrid("select",childs[i].fid);
				}
			}
		},
	    onUnselect:function(row){
	    	var childs=row.children;
			if(childs.length>0){
				for(var i=0;i<childs.length;i++){
					$('#taskList').treegrid("unselect",childs[i].fid);
				}
			}
	    }
	});
	$('#taskList').treegrid("uncheckAll");
	$.ajax({
		url:"${ctx}/flow/task/tree",
		async:false,
		data:{planId:fid},
		success:function(data){
			var planRow=$('#planList').getRowData(index);
			if(data[0]){
				data[0].fid=data[0].fid;
				data[0].code=planRow._code;
				data[0].name=planRow.name;
				data[0].taskLevelName=planRow.planLevelName;
				data[0].securityLevelName=planRow.securityLevelName;
				data[0].principalerName=planRow.principalerName;
				data[0].auditerName=planRow.auditerName;
				data[0].undertakerName=planRow.initiaterName;
				data[0].type=1;
			}
			$('#taskList').treegrid("loadData",data);
		}
	});
	$("#planTempSelect").parent().show();
	$("#planTempSelect").attr("planId",fid);
	$("#searchBar").hide();
	$("#gbox_mergerPlanList").hide();
	$("#mergePlan").hide();
	$("#nextStep").hide();
	$("#lastStep").hide();
	$("#saveTemp").show();
}
//合并窗口
function mergePlanWin(fid,status,index){
	$($("#taskList").datagrid("getPanel")).show();
	$('#taskListWin').window("open");
	$('#taskListWin').window("setTitle","需要合并到计划或事件");
	$('#taskList').treegrid({
		singleSelect:true,
		onSelect:function(row){
			return;
		},
	    onUnselect:function(row){
	    	return;
	    }
	});
	$('#taskList').treegrid("uncheckAll");
	$.ajax({
		url:"${ctx}/flow/task/tree",
		async:false,
		data:{planId:fid},
		success:function(data){
			var planRow=$('#planList').getRowData(index);
			if(data[0]){
				data[0].fid=data[0].fid;
				data[0].code=planRow._code;
				data[0].name=planRow.name;
				data[0].taskLevelName=planRow.planLevelName;
				data[0].securityLevelName=planRow.securityLevelName;
				data[0].principalerName=planRow.principalerName;
				data[0].auditerName=planRow.auditerName;
				data[0].undertakerName=planRow.initiaterName;
				data[0].type=1;
			}
			$('#taskList').treegrid("loadData",data);
			$('#taskList').attr("planId",fid);
		}
	});
	$("#planTempSelect").parent().hide();
	$("#planTempSelect").attr("planId",fid);
	$("#planTempSelect").attr("planStatus",status);
	$("#searchBar").hide();
	$("#gbox_mergerPlanList").hide();
	$("#mergePlan").hide();
	$("#nextStep").show();
	$("#lastStep").hide();
	$("#saveTemp").hide();
}
//保存到模板
function saveTemp(){
	var planId=$("#planTempSelect").attr("planId");
	var planTplId=($("#planTempSelect").next())[0].comboObj.getSelectedValue();
	var selections=$('#taskList').treegrid("getSelections");
	if(selections.length<1){
		$.fool.alert({msg:"请先选取事件"});
		return false;
	}
	var taskIds="";
	for(var i=0;i<selections.length;i++){
		taskIds=selections[i].fid+","+taskIds;
	}
	$.ajax({
		url:"${ctx}/flow/plan/savePlanToTemplate",
		async:false,
		data:{planId:planId,planTplId:planTplId,taskIds:taskIds},
		success:function(data){
			if(data.returnCode == '0'){
				if(!planTplId){
					var url = "/flow/planTemplate/manage?clearFlag=1&openAdder=1&tempId="+data.data;
					parent.kk(url,"计划模板");
					$('#taskListWin').window("close");
					taskIds="";
				}else{
					$.fool.alert({msg:"保存成功"});
					$('#taskListWin').window("close");
					taskIds="";
				}
			}else{
				$.fool.alert({msg:data.message});
			}
		}
	});
	
}

//下一步
function nextStep(){
	var selected=$('#taskList').treegrid("getSelected");
	if(!selected){
		$.fool.alert({msg:"请先选取事件"});
		return false;
	}
	$('#taskListWin').window("setTitle","选取需要被合并的计划");
	$("#planTempSelect").attr("mergeId",selected.fid);
	if(selected.type==1){
		$("#planTempSelect").attr("type",1);
	}else{
		$("#planTempSelect").attr("type",2);
	}
	if(selected!=null){
		$('#mergerPlanList').jqGrid("setGridParam",{
			datatype:function(postdata){
				postdata.status=$("#planTempSelect").attr("planStatus");
				$.ajax({
					url: "${ctx}/flow/plan/query?excludeId="+$('#taskList').attr("planId"),
					type:"get",
					data:postdata,
				    dataType:"json",
				    complete: function(data,stat){
				    	if(stat=="success") {
				    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
				    		$("#mergerPlanList")[0].addJSONData(data.responseJSON);
				    	}
				    }
				});
			},
		}).trigger("reloadGrid");
		$("#searchBar").show();
		$("#gbox_mergerPlanList").show();
		$($("#taskList").datagrid("getPanel")).hide();
		$("#mergePlan").show();
		$("#nextStep").hide();
		$("#lastStep").show();
		$("#saveTemp").hide();
	}else{
		$.fool.alert({msg:"请先选取事件"});
	}
};
//上一步
function lastStep(){
	$('#taskListWin').window("setTitle","需要合并到计划或事件");
	$("#searchBar").hide();
	$("#gbox_mergerPlanList").hide();
	$($("#taskList").datagrid("getPanel")).show();
	$("#mergePlan").hide();
	$("#nextStep").show();
	$("#lastStep").hide();
	$("#saveTemp").hide();
};
//合并计划
function mergePlan(){
	var selections=$("#mergerPlanList").jqGrid("getGridParam", "selarrrow");
	var ids="";
	for(var i=0;i<selections.length;i++){
		ids=$("#mergerPlanList").jqGrid("getRowData",selections[i]).fid+","+ids;
	}
	$.ajax({ 
		url:"${ctx}/flow/task/mergePlan",
		async:false,
		data:{planId:$("#planTempSelect").attr("planId"),fid:$("#planTempSelect").attr("mergeId"),ids:ids,type:$("#planTempSelect").attr("type")},
		success:function(data){
			if(data.returnCode == '0'){
				$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
					$('#planList').trigger("reloadGrid");
					$('#taskListWin').window("close");
				}});
			}else{
				$.fool.alert({msg:data.message});
			}
		}
	});
}
</script>
