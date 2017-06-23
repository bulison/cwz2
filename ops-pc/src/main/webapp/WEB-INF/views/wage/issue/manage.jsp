<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>工资发放</title>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
#search-form p{
	display: inline-block;
	padding: 0px;
	margin-top: 10px;
}
#dhxDiv_search-memberName{margin:0px 5px 7px 0px;}
#dhxDiv_deptId{margin: 0px 5px 7px 0px;}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>工资发放</h1>
       </div>             
    </div>
	<div class="nav-box">
		<ul>
			<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
			<li><a href="javascript:;">工资管理</a></li>
			<li><a href="javascript:;" class="curr">工资发放</a></li>
		</ul>
	</div>
		
		<form class="toolBox"  id="search-form">
		<div class="toolBox-button" style="margin-right:5px;">
			<fool:tagOpt optCode="issuePrint"><a href="javascript:;" id="print" class="btn-ora-printer" >打印</a></fool:tagOpt>
		</div>
		<div class="toolBox-pane">
			<input id="search-wageDate" />
			<input id="deptId" name="deptId" class="textBox" />
			<!-- <input id="search-deptName" class="textBox" /><input type="hidden" id="deptId" /> -->
			<!-- <input id="search-memberName" class="textBox" _class="member-combogrid" data-options="{required:false,novalidate:true,width:160,height:30,panelWidth:580}"/>  -->
			<input id="search-memberName" name="inMemberName" class="textBox"/><input type="hidden" name="inMemberId" id="select-inMemberId" />
			<fool:tagOpt optCode="issueSearch"><a href="javascript:;" id="search-btn" class="btn-blue btn-s">筛选</a></fool:tagOpt>
			<a href="javascript:;" id="clear-btn" class="btn-blue btn-s">清空</a>
		</div>
		</form>
	<table id="wageList"></table>
	<div id="pager"></div>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script>
var texts = [];
var thead = [
             {key:'date',title:'月份'},
             {key:'memberDept',title:'部门'},
             {key:'memberCode',title:'人员编号'},
             {key:'memberName',title:'人员名称'},
             <c:forEach items="${titles}" var="title">
             {key:'${title.fid}',title:'${title.columnName}'},
			  </c:forEach>
             ];
var tfoot = [];
$("#search-form").find("input[_class]").each(function(i,n){inputInit($(this));});
$('#search-btn').click(function(){
	//var deptId = $('#deptId').textbox("getValue");
	var deptId=deptName.getSelectedValue();//获取控件值
	var wageDate = $('#search-wageDate').datebox('getText');
	var memberId = $('#select-inMemberId').val();
	var options = {deptId:deptId,wageDate:wageDate,memberId:memberId};
	$('#search-form').form('enableValidation'); 
	if($('#search-form').form('validate')){	
		$('#wageList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	}
});
$('#clear-btn').click(function(){
	//$('#search-form').form('clear');
	cleanBoxInput($("#search-form"));
	deptName.setComboText("");
	deptName.setComboValue("");
	memCombo.setComboText("");
	memCombo.setComboValue("");
	
});
//控件初始化
$('#search-wageDate').datebox({
	prompt:'月份',
	width:160,
	height:32,
	editable:false,
	required:true,
	novalidate:true,
	onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
		span.trigger('click'); //触发click事件弹出月份层
		if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
			tds = p.find('div.calendar-menu-month-inner td');
	        tds.click(function (e) {
	        	e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
	            var year = /\d{4}/.exec(span.html())[0];//得到年份
	            var month = parseInt($(this).attr('abbr'), 10) + 1; //月份
	            $('#search-wageDate').datebox('hidePanel');//隐藏日期对象
	            $('#search-wageDate').datebox('setValue', year + '-' + month); //设置日期的值
	            $('input.calendar-menu-year').attr("disabled","disabled");//避免填写年份后会回到选择日期的面板的问题
	        });
		}, 0);
		$("#search-wageDate").datebox('calendar').find('.calendar-header').hide();
		$("#search-wageDate").datebox('calendar').parent().siblings('.datebox-button').hide();
	},
	parser: function (s) {//配置parser，返回选择的日期
		if (!s) return new Date();
        var arr = s.split('-');
        return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);	                
	},
	formatter: function (d) {
		if(d.getMonth()==0){
			return d.getFullYear()-1 + '-12'; 
		}else{
			return d.getFullYear() + '-' + d.getMonth();
		}
	}//配置formatter，只返回年月
});
var p = $('#search-wageDate').datebox('panel'), //日期选择对象
tds = false, //日期选择对象中月份
span = p.find('span.calendar-text'); //显示月份层的触发控件

/* $('#deptId').fool('deptComBoxTree',{
	 required:false,
	 prompt:'部门',
	 novalidate:true,
	 editable:false,
	 width:164,
	 height:31
 }); */
 
//部门初始化	
	var deptValue='';	
		$.ajax({
			url:"${ctx}/orgController/getAllTree?num="+Math.random(),
			async:false,		
			success:function(data){		  	
				deptValue=formatData(data[0].children,'id','texr');	
		    }
			});
		var deptName= $("#deptId").fool("dhxCombo",{
			 prompt:'部门',
			  width:160,
			  height:32,
			  clearOpt:false,
			  data:deptValue,
            toolsBar:{
            	name:"部门",
                refresh:true
            },
            editable:false,
			  focusShow:true,
		});	

 var boxWidth=160,boxHeight=31;//统一设置输入框大小
/* $('#search-memberName').fool('combogrid',{
	required:false,
	novalidate:true,
	width:boxWidth,
	height:boxHeight,
	prompt:'人员',
	idField:'fid',
	textField:'username',
	panelWidth:450,
	fitColumns:true,
	url:getRootPath()+'/member/vagueSearch',
	columns:[[
		{field:'fid',title:'fid',hidden:true},
		{field:'userCode',title:'编号',width:100,searchKey:false},
		{field:'jobNumber',title:'工号',width:100,searchKey:false},
		{field:'username',title:'名称',width:100,searchKey:false},
		{field:'phoneOne',title:'电话',width:100,searchKey:false},
		{field:'deptName',title:'部门',width:100,searchKey:false},
		{field:'position',title:'职位',width:100,searchKey:false},
		{field:'searchKey',hidden:true,searchKey:true},
	          ]],
	onSelect:function(index,row){
		$("#select-inMemberId").val(row.fid);
		$("#search-memberName").val(row.username).focus();
	}	
});
$('#search-memberName').combogrid('textbox').focus(function(){
	$('#search-memberName').combogrid('showPanel');
}); */ 

//鼠标获取焦点出现下拉列表
var memData='';	
$.ajax({
	url:getRootPath()+'/member/vagueSearch?num='+Math.random(),
	async:false,		
	success:function(data){		  	
		memData=formatData(data,'fid');	
    }
	});
var memCombo = $('#search-memberName').fool('dhxComboGrid',{
	focusShow:true,
	width:boxWidth,
	height:boxHeight,
	 prompt:'人员',
	data:memData,
	filterUrl:getRootPath()+'/member/vagueSearch?num='+Math.random(),
	toolsBar:{
        refresh:true
    },
	hasDownArrow:false,
	setTemplate:{
		  input:"#username#",
		  columns:[
				{option:'#userCode#',header:'编号',width:100},
				{option:'#jobNumber#',header:'工号',width:100},
				{option:'#username#',header:'名称',width:100},
				{option:'#phoneOne#',header:'电话',width:100},
				{option:'#deptName#',header:'部门',width:100},
				{option:'#position#',header:'职位',width:100},
			     ]
	  },
	onChange:function(value,text){
		$("#select-inMemberId").val(value);
	},
});

/* $('#wageList').datagrid({
	url:'${ctx}/wage/wagePublish',
	pagination:false,
	fitColumns:true,
	singleSelect:true,
	remoteSort:false,
	columns:[[
	             {field:'fid',title:'fid',hidden:true},
	             {field:'date',title:'月份',width:100},
	             {field:'memberDept',title:'部门',width:100},
	             {field:'memberCode',title:'人员编号',width:100},
	             {field:'memberName',title:'人员名称',width:100},
	             <c:forEach items="${titles}" var="title">
	             	{field:'${title.fid}',title:'${title.columnName}',width:100},
	             </c:forEach>
	  	          ]]
}); */

$('#wageList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/wage/wagePublish',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		var json=JSON.parse(data.responseJSON);
	        		$("#wageList")[0].addJSONData(json);
	        	}
	        }
		});
	},
	autowidth:true,//自动填满宽度
	height:'100%',
	rowNum:'',
	viewrecords:true,
	forceFit:true,//调整列宽度，表格总宽度不会改变
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	colModel:[
	             {name:'fid',label:'fid',align:'center',hidden:true},
	             {name:'date',label:'月份',align:'center',width:100},
	             {name:'memberDept',label:'部门',align:'center',width:100},
	             {name:'memberCode',label:'人员编号',align:'center',width:100},
	             {name:'memberName',label:'人员名称',align:'center',width:100},
	             <c:forEach items="${titles}" var="title">
	             	{name:'${title.fid}',label:'${title.columnName}',align:'center',width:100},
	             </c:forEach>
	  	          ]
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 
enterSearch('search-btn');

//打印按钮
$("#print").click(function(){
	var wageDate=$("#search-wageDate").datebox("getValue");
	var deptId=deptName.getSelectedValue();//获取控件值
	var memberId=$("#search-memberName").next()[0].comboObj.getSelectedValue();
	printWageIssue(wageDate,deptId,memberId);
});
</script>
</body>
</html>