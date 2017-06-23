<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>执行人效益分析</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/rateModel.css" />
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/echarts.min.js?v=${js_v}"></script>
<style>
.form1 p font{
	width:100px;
}
#detailsChart{
    width:auto;
    height:300px;
    display: none;
}
#inSearch{
	position: relative;
}
#viewer{
  position:absolute;
	right:75px;
}
.list-on{
  display:inline-block;
  margin:0 10px;
  padding:20px;
  width:0px;
  height:0px;
  background: url("${ctx}/resources/images/list_on.png") no-repeat;
}
.list-off{
  display:inline-block;
  margin:0 10px;
  padding:20px;
  width:0px;
  height:0px;
  background: url("${ctx}/resources/images/list.png") no-repeat;
}
.chart-on{
  display:inline-block;
  margin:0 10px;
  padding:20px;
  width:0px;
  height:0px;
  background: url("${ctx}/resources/images/chart_on.png") no-repeat;
}
.chart-off{
  display:inline-block;
  margin:0 10px;
  padding:20px;
  width:0px;
  height:0px;
  background: url("${ctx}/resources/images/chart.png") no-repeat;
}
</style>
</head>
<body style="overflow: hidden">
	<div class="titleCustom">
         <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>执行人效益分析</h1>
         </div>             
    </div>
    
    <div class="myform">
	    <div class="searchBox" >
	        <form class="form1 inSearch" id='inSearch' >
	       		<input id="memberIds" name="memberIds" type="hidden"/>
	       		<p><font>执行人：</font><input id="memberNames" name="memberNames" /> </p>
	       		<a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a>
	       	    <a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a>
	       		<%--<p id="viewer">--%>
	       		  <%--<a id="viewer-list" class="list-on" title="列表显示" href="javascript:;"></a>--%>
	       		  <%--<a id="viewer-chart" class="chart-off" title="图表显示" href="javascript:;"></a>--%>
	       		<%--</p>--%>
	        </form>
	    </div>
	</div>
	<div class="dataBox">
	    <div id="detailsChart"></div>
	</div>
	<table id="detailsList"></table>
	<div id="pager"></div>
	<div id="detailWindow">
	</div>
</body>
<script>
var today=new Date();
var todayStr=today.getFullYear()+"-"+(today.getMonth()+1)+"-"+today.getDate();
var lastMonth=new Date(today.setDate(today.getDate()-30));
var lastMonthStr=lastMonth.getFullYear()+"-"+(lastMonth.getMonth()+1)+"-"+lastMonth.getDate();
/* var names =[];
var rates =[]; */
var paramdata = $('#inSearch').serializeJson();
/* $.ajax({
	url:getRootPath()+"/api/rate/queryRateMemberSum",
	async:false,
	data:{startDay:lastMonthStr,endDay:todayStr},
	success:function(data){
		var rows=data.rows;
		names=[];
		rates=[];
		if(rows){
			for(var i=0;i<rows.length;i++){
				names.push(rows[i].userName);
				rates.push(rows[i].rate);
			}
		}
	}
}); */
var toLoad=false;
function getData(postdata) {
    if(toLoad){
        $.ajax({
            url:getRootPath()+"/api/rate/queryRateMemberSum?memberIds="+paramdata.memberIds,
            data:postdata,
            dataType:"json",
            complete: function(data,stat){
                if(stat=="success") {
                    data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
                    var rows = data.responseJSON.rows;
                    if(rows.length>0){
                        $.ajax({//用户列表
                            url:'${ctx}/userController/userList',
                            dataType:"json",
                            complete: function(_data,_stat){
                                if(_stat=="success") {
                                    var memberData=_data.responseJSON.rows;
                                    for(var i=0;i<rows.length;i++){
                                        for(var j=0;j<memberData.length;j++){
                                            if(rows[i].memberId==memberData[j].fid){
                                                rows[i].memberName=memberData[j].userName;
                                            }
                                        }
                                    }
                                    $("#detailsList")[0].addJSONData(data.responseJSON);
                                }
                            }
                        });
					}
					else{
                        $("#detailsList")[0].addJSONData(data.responseJSON);
                    }
                }
            }
        });
	}
}
$('#detailsList').jqGrid({
	datatype:function(postdata){
        getData(postdata,toLoad);
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
		{name:"memberId",label:'memberId',hidden:true},
		{name:"memberName",label:'人员',width:5,align:'center'},
		{name:"eventsNumber",label:'办理事件数',width:5,align:'center'},
		{name:"planTotalTime",label:'预计总时间',width:5,align:'center'},
		{name:"totalTime",label:'实际完成总时间',width:8,align:'center'},
		{name:"calcDelayNumber",label:'延误次数',width:5,align:'center'},
		{name:"delayTime",label:'延误天数',width:5,align:'center'},
		{name:"efficiency",label:'效益(%)',width:5,formatter:rateOut,align:'center'},
		{name:"action",width:5,label:'操作',align:'center',formatter:function(value,options,row){
			return '<a href="javascript:;" class="btn-detail" onclick="detailById(\''+row.memberId+'\',\''+row.memberName+'\')" title="明细"></a>';
		}},
	]/* ,onLoadSuccess:function(data){
		var rows=data.rows;
		names=[];
		rates=[];
		if(rows){
			for(var i=0;i<rows.length;i++){
				names.push(rows[i].userName);
				rates.push(rows[i].rate);
			}
		}
	} */
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

//搜索按钮点击
$('#mySearch').click(function(){
	var data = $('#inSearch').serializeJson();
	paramdata.memberIds=data.memberIds;
    toLoad=true;
	$('#detailsList').trigger("reloadGrid");
    /* var newOption={
			xAxis: {
				name:'姓名',
				data:names
			},
			series: [{
				data: rates,
			}],
	};
	chart.setOption(newOption); */
});

//清除按钮点击
$('#clearForm').click(function(){
	$('#inSearch').form('clear');
});
//
////视图切换 列表
//$("#viewer-list").click(function(){
//	if($("#gbox_detailsList").css("display")=="none"){
//		$(this).attr("class","list-on");
//		$("#viewer-chart").attr("class","chart-off");
//	}
//    $("#gbox_detailsList").show();
//	$("#detailsChart").hide();
//	$("#detailsList").trigger("reloadGrid");
//	chart.resize();
//});
//
////视图切换 图表
//$("#viewer-chart").click(function(){
//	if($("#detailsChart").css("display")=="none"){
//		$(this).attr("class","chart-on");
//		$("#viewer-list").attr("class","list-off");
//	}
//	$(".datagrid").hide();
//	$("#detailsChart").show();
//	chart.resize();
//});

//表单控件初始化-----start-----
$('#memberNames').textbox({
	novalidate:true,
	width:160,
	height:30,
	editable:false,
	prompt:'人员'
});
$('#memberNames').textbox('textbox').click(function(){
	win = $.fool.window({
		href:"${ctx}/userController/window?okCallBack=memberSelect&onDblClick=memberSelect",
		title:'选择人员',
		onClose:function(){
			$(this).window("destroy");
		}
	});
});
$("#detailWindow").window({
	top:10,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	modal:true,
	width:"90%",
	//height:700,
	closed:true
});
//表单控件初始化-----end-----

//图表属性
/* var option = {
		title: {
			text: '执行人效率'
		},
		tooltip: {},
		xAxis: {
			name:'姓名',
			data:names
		},
		yAxis: {
			name:'(%)'
		},
		series: [{
			name: 'bar',
			type: 'bar',
			data: rates,
			label: {
				normal: {
					show: true,
					position: 'top'
				}
			}
		}],
		animationEasing: 'elasticOut',
		animationDelayUpdate: function (idx) {
			return idx * 5;
		}
};

//图表初始化
var dom=document.getElementById("detailsChart");
var chart=echarts.init(dom);
chart.setOption(option); */

//加 "%"
function rateOut(value,row,index){
	if(value){
	    //去掉"%"
		/*return parseFloat(value).toFixed(2)+"%";*/
        return parseFloat(value).toFixed(2)
	}else{
		return "0";
	}
}

//选取执行人
function memberSelect(data){
	if(data[0]){
		var ids="";
		var names="";
		for(var i=0;i<data.length;i++){
			ids=data[i].fid+","+ids;
			names=data[i].userName+","+names;
		}
		$("#memberIds").val(ids);
		$("#memberNames").textbox("setValue",names);
	}else{
		$("#memberIds").val(data.fid);
		$("#memberNames").textbox("setValue",data.userName);
	}
	win.window("close");
}

//明细按钮点击
function detailById(memberId,memberName){
    var name =""
    if(memberName!='undefined')
        name=memberName;
    $("#detailWindow").window("setTitle",name);
	$("#detailWindow").window("open");
	$("#detailWindow").window("refresh","${ctx}/rate/rateMember/detailPage?memberId="+memberId+"&startDate="+lastMonthStr+"&endDate="+todayStr);
}
</script>
</html>
