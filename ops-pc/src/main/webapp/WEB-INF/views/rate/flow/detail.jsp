<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程收益率分析明细</title>
</head>
<body>
	<table id="details"></table>
	<div id = "pager1"></div>
	<div id="charts"></div>
<script>
if("${param.flag}"==0){
	$("#charts").hide();
	$('#details').jqGrid({
		datatype:function(postdata){
			$.ajax({
				url:"${ctx}/flow/task/queryByPlan?planId=${param.planId}",
				data:postdata,
				dataType:"json",
				complete: function(data,stat){
					if(stat=="success") {
						data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
						$("#details")[0].addJSONData(data.responseJSON);
					}
				}
			});
		},
		autowidth:true,
		footerrow:true,
        height:$(window).outerHeight()-200+"px",
		rowNum:10,
		rowList:[10,20,30],
		pager:"#pager1",
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",
		},
		colModel:[
			{name:"name",label:'事件名称',width:5,align:'center'},
			{name:"antipateStartTime",label:'计划开始日期',width:5,align:'center'},
			{name:"originalEndTime",label:'原计划完成日期',width:5,align:'center'},
			{name:"actualStartTime",label:'实际开始日期',width:5,align:'center'},
			{name:"actualEndTime",label:'实际完成日期',width:5,align:'center'},
			{name:"status",label:'状态',width:6,align:'center',formatter:function(value){
				if(value==0){
					return "草稿";
				}else if(value==1){
					return "办理中";
				}else if(value==2){
					return "已办理待审核";
				}else if(value==3){
					return "已完成";
				}else if(value==4){
					return "已延迟且未开始办理";
				}else if(value==5){
					return "已延迟且未结束办理";
				}else if(value==6){
					return "已终止";
				}
			}},
			{name:"fextensionDays",label:'延迟天数',width:5,align:'center'},
			{name:"delayedTime",label:'延迟次数',width:5,align:'center'},
			{name:"contractorsEfficiency",label:'承办效率(%)',width:5,align:'center'/*,formatter:function(value){
				if(value!=""){
					return value+"%";
				}else{
					return "0%";
				}
			}*/},
			{name:"principalerName",label:'责任人',width:5,align:'center'},
			{name:"undertakerName",label:'承办人',width:5,align:'center'},
		],
	}).navGrid("#pager1",{add:false,del:false,edit:false,search:false,view:false});
}else if("${param.flag}"==1){
	$("#charts").show();
	var effectiveYieldrate=[];
	var currentYieldRate=[];
	var referenceYieldrate=[];
	var estimatedYieldrate=[];
	var dates=[];
	$.ajax({
		type : 'post',
		url : getRootPath()+'/flow/yieldRate/queryByPlan',
		data :{planId:"${param.planId}"},
		dataType : 'json',
		async:false,
		success : function(data) { 
			if(data.length>0){
				for(var i=0;i<data.length;i++){
					effectiveYieldrate.push(data[i].effectiveYieldrate);
					currentYieldRate.push(data[i].currentYieldRate);
					referenceYieldrate.push(data[i].referenceYieldrate);
					estimatedYieldrate.push(data[i].estimatedYieldrate);
					dates.push(data[i].date.slice(0,10));
				}
			}
		},
	});
	//图表属性
	var option = {
			title: {
				text: '流程收益率分析明细'
			},
			tooltip: {
				trigger: 'axis',
			}, 
			legend: {
		        data:['参考收益率','计划预计收益率','当前预计收益率','实际收益率']
		    },
			xAxis: {
				name:'日期',
				data:dates,
				nameLocation:'middle',
				nameGap:20
			},
			yAxis: {
				name:'收益率'
			},
			series: [{
				name: '参考收益率',
				type: 'line',
				data: referenceYieldrate,
				label: {
					normal: {
						show: true,
						position: 'top'
					}
				},
				lineStyle: {
					normal: {
						color: "rgb(79,129,189)"
					}
				},
				itemStyle: {
					normal: {
						color: "rgb(79,129,189)"
					}
				}
			},{
				name: '计划预计收益率',
				type: 'line',
				data: estimatedYieldrate,
				label: {
					normal: {
						show: true,
						position: 'top'
					}
				},
				lineStyle: {
					normal: {
						color: "rgb(255,0,0)"
					}
				},
				itemStyle: {
					normal: {
						color: "rgb(255,0,0)"
					}
				}
			},{
				name: '当前预计收益率',
				type: 'line',
				data: currentYieldRate,
				label: {
					normal: {
						show: true,
						position: 'top'
					}
				},
				lineStyle: {
					normal: {
						color: "rgb(247,150,70)"
					}
				},
				itemStyle: {
					normal: {
						color: "rgb(247,150,70)"
					}
				}
			},{
				name: '实际收益率',
				type: 'line',
				data: effectiveYieldrate,
				label: {
					normal: {
						show: true,
						position: 'top'
					}
				},
				lineStyle: {
					normal: {
						color: "rgb(0,176,80)"
					}
				},
				itemStyle: {
					normal: {
						color: "rgb(0,176,80)"
					}
				}
			}],
	};

	//图表初始化
	var dom=document.getElementById("charts");
	var chart=echarts.init(dom);
	chart.setOption(option);
}
</script>
</body>
</html>
