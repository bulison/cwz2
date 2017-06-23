<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>执行人效益分析明细</title>
<style>
</style>
</head>
<body>
	<div id="details"></div>
	<div id="charts"></div>
<script>
var codes =[];
var rates =[];

$('#details').datagrid({
	url:getRootPath()+"/rate/customerDetail/getDetail",
	queryParams: {
		userId:"${param.customerId}",startDate:"${param.startDate}",endDate:"${param.endDate}"
	},
	fitColumns:true,
	singleSelect:true,
	showFooter:true,
	pagination:true,
	columns:[[
             {field:"",title:'日期',width:10},
             {field:"code",title:'单据编号',width:10},
             {field:"saleAmount",title:'销售金额',width:10},
             {field:"expectReturn",title:'预计收益',width:10},
             {field:"expectRate",title:'预计收益率',width:10,formatter:rateOut},
             {field:"marketRate",title:'市场参考收益率',width:10,formatter:rateOut},
             {field:"realRate",title:'实际收益率',width:10,formatter:rateOut},
             {field:"currentRate",title:'当前收益率',width:10,formatter:rateOut},
             {field:"",title:'完成状况',width:10},
	        ]],
	onLoadSuccess:function(data){
		var rows=data.rows;
		codes=[];
		rates=[];
		if(rows){
			for(var i=0;i<rows.length;i++){
				codes.push(rows[i].code);
				rates.push(rows[i].rate);
			}
		}
	}
});

//图表属性
var option = {
		title: {
			text: '客户明细效率分析'
		},
		tooltip: {},
		xAxis: {
			name:'单号',
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
var dom=document.getElementById("charts");
var chart=echarts.init(dom);
chart.setOption(option);

</script>
</body>
</html>
