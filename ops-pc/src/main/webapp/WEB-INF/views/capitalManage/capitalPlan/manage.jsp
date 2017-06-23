<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>资金计划管理</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/capitalPlan.js?v=${js_v}"></script>
<style>
  .open{
	background:url(${ctx}/resources/images/open.png) no-repeat;
	padding-left:0px;
	width:40px;
	background-position:0px -1px;
}

#goodsChooser{
  display: none;
  text-align: center;
}

#add{
    vertical-align: top;
   /*  margin-top: 5px; */
}
#search-form{
  display: inline-block;
  width:98%;
}
#search-form .textbox{
  margin:5px 5px 0px 0px
}
#search-form .btn-s{
  margin:5px 5px 0px 0px;
  vertical-align: middle;
}

#btnBox{
  text-align: center;
}

.form p.hideOut,.form1 p.hideOut{
    display: none;
  }
.form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{width:115px;}
  
#userChooser,#customerChooser,#goodsChooser,#goodsSpecChooser{
  display: none;

}

#userSearcher,#cusSearcher,#goodsSearcher,#goodsSpecSearcher{
  text-align: left;
  margin:10px
}
#Inquiry{margin-left: 5px;}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 25px;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:29px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:5px;}
.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:93px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px;width:240px;}
.button_clear{ border-top: 1px solid #D5DBEA;margin-top: 10px; text-align: right;}
.roed{
   -moz-transform:scaleY(-1);
   -webkit-transform:scaleY(-1);
   -o-transform:scaleY(-1);
   transform:scaleY(-1);
   filter:FlipV();
}
#bill p.hideOut{
  display: none ;
}
.form p textarea, .form1 p #remarker{
  width:230px !important;
  height:60px !important
}
</style>
</head>
<body>
	<div class="titleCustom">
	    <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>资金计划管理</h1>
        </div>             
    </div>
	<div id="addBox"></div>
	<div id="poper-win">
	  <form class="form1">
	      <input id="capitalId" type="hidden"/>
	      <p class="flag1"><font>延期日期：</font><input id="delayDate"/></p>
	      <p class="flag2"><font>拆分金额：</font><input id="splitAmount"/></p>
	      <p class="flag3"><font>调整金额：</font><input id="adjustAmount"/></p>
	      <p><font>备注：</font><textarea id="remarker"></textarea></p>
	      <br/><p class="flag1"><font></font><a class="mybtn-blue mybtn-s flag1" onclick="delayDetial()">延期</a></p>
	      <p class="flag2"><font></font><a class="mybtn-blue mybtn-s flag2" onclick="splitDetail()">拆分</a></p>
	      <p class="flag3"><font></font><a class="mybtn-blue mybtn-s flag3" onclick="adjustDetail()">调整金额</a></p>
	  </form>
	</div>
	<div style="margin:10px 0px 10px 0px;">
	    <div>
	        <a href="javascript:;" id="addNew" class="btn-ora-add" >新增</a> 
	        <input class="search-startDay" name="startDay" />-<input class="search-endDay" name="endDay"/><a href="javascript:;" class="_Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	        <div id="grabble"><input type="text" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	    </div>
	    <div class="input_div">
	        <form id="search-form">
	            <p>单号：<input id="search-code" name="code"/></p>
	            <p>说明:<input id="search-explain" name="explain"/></p>
	            <p>状态：<input id="search-recordStatus" name="recordStatus"/></p>
	            <div class="button_clear">
	                <a href="javascript:;" id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	                <a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	                <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	            </div>
	        </form>
	    </div>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
<script type="text/javascript">
recordStatusOptions = [{id:'0',name:'草稿'},{id:'1',name:'审核'},{id:'2',name:'坏账'},{id:'3',name:'完成'},{id:'4',name:'取消'}];
initManage("zjjh","资金计划");
var date=new Date();
var nextMonth=date.getFullYear()+"-"+(date.getMonth()+2)+"-"+date.getDate();
$("#search-code").textbox({
	prompt:'单号',
	width:160,
	height:32,
});
$(".search-startDay").fool('datebox',{
	prompt:'预计收付款结束日期',
	width:160,
	height:32,
	editable : false,
	value:getCurrDate()
});
$(".search-endDay").fool('datebox',{
	prompt:'预计收付款结束日期',
	width:160,
	height:32,
	editable : false,
	value:nextMonth
});
$("#search-explain").textbox({
	prompt:'说明',
	width:160,
	height:32,
}); 
$("#search-recordStatus").fool('dhxCombo',{
	prompt:"状态",
	width:162,
	height:32,
	data:[{value:'0',text:'草稿'},{value:'1',text:'审核'},{value:'2',text:'坏账'},{value:'3',text:'完成'},{value:'4',text:'取消'}],
	focusShow:true,
	editable : false,
}); 
$("#poper-win").window({
	top:100+$(document).scrollTop(),
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	href:"",
	width:400,
	height:230,
	closed:true,
	onClose:function(){
		$("#poper-win").form("clear");
	}
}); 

$("#delayDate").datebox({
	required:true,
	prompt:'延期日期',
	width:160,
	height:32,
	editable : false,
});
$("#splitAmount").textbox({
	required:true,
	prompt:'拆分金额',
	width:160,
	height:32,
	precision:2,
	missingMessage:"该项不能为空"
});
$("#adjustAmount").textbox({
	required:true,
	prompt:'调整金额',
	width:160,
	height:32,
	precision:2,
	missingMessage:"该项不能为空"
});
$("#remarker").validatebox({
	validType:"maxLength[200]"
});

$('#billList').jqGrid({
	datatype:function(postdata){
		postdata.startDay=$(".search-startDay").datebox("getValue");
		postdata.endDay=$(".search-endDay").datebox("getValue");
		postdata.calculation=1;
		$.ajax({
			url:'${ctx}/capitalPlanDetail/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#billList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	forceFit:true,
	pager:'#pager',
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	autowidth:true,//自动填满宽度
	height:$(window).outerHeight()-200+"px",
	colModel:[
	  		{name:'id',label:'id',align:"center",hidden:true,width:100},
	  		{name:'capitalId',label:'capitalId',align:"center",hidden:true,width:100},
	  		{name:'capitalCode',label:'单号',align:"center",width:100,sortable:true,formatter:function(value,options,row){
	  			return "<a href='javascript:;' onclick='viewRowNew(\""+row.capitalId+"\",\""+row.recordStatus+"\")'>"+value+"</a>";
	  		}},
	  		{name:'explain',align:"center",label:'说明',width:100},
	  		{name:'paymentDate',align:"center",label:'预计收付款日期',width:60,sortable:true,formatter:dateFormatAction},
	        {name:'planAmount',align:"center",label:'计划收付金额',width:100},
	        {name:'billAmount',align:"center",label:'单据金额',width:100},
	        {name:'paymentAmount',align:"center",label:'收付金额',width:100},
	  		{name:'recordStatus',align:"center",label:'状态',width:40,sortable:true,formatter:recordStatusAction},
	  		{name:'creatorName',align:"center",label:'最后修改人',width:100},
	  		{name:'action',align:"center",label:'操作',formatter:actionFormat}
	      ],

}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

$('#bolting').focus(function (){ 
	$('.input_div').show();
	$('.button_a').addClass('roed');
});

$("#clear-btn").click(function(){
	/* $(".search-startDay").datebox('setValue',"");
	$(".search-endDay").datebox('setValue',""); */
	cleanBoxInput($("#search-form"));
});

$("#search-btn").click(function(){
	/* refreshData(); */
	//刷新列表页
	var options = $("#search-form").serializeJson();
	$('#billList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	return false;
});
//全局查询
$('._Inquiry').click(function(){
	var startDay=$(".search-startDay").datebox('getValue');
	var endDay=$(".search-endDay").datebox('getValue');
	var options=$.extend($("#search-form").serializeJson(),{"startDay":startDay,"endDay":endDay});
	options.calculation=1;
	$('#billList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
});
//点击关闭隐藏
$('#clear-btndiv').click(function(){
	$('.input_div').hide();
	$('.button_a').removeClass('roed');
});

</script>
</body>
</html>
