<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>曲线图界面</title>
</head>
<body>
<style>
#chart{
  width:100%;
  height:100%;
}
</style>
<div id="chart"></div>
<script src="/ops-pc/resources/js/echarts.min.js"></script>
<script type="text/javascript">
$.ajax({
	url:getRootPath()+'/api/purchasePrice/queryLineData',
	async:false,
	data:{goodsId:"${param.goodsId}",goodSpecId:"${param.goodSpecId}",startDay:"${param.startDay}",endDay:"${param.endDay}"},
	success:function(data){
		var series=data.lineDataVoList;
		for(var i=0;i<series.length;i++){
			series[i].type="line";
		}
		var option = {
				title: {
			        text: '价格趋势'
			    },
			    tooltip: {
			        trigger: 'axis'
			    },
			    legend: {
			        data:data.lineTitle
			    },
			    xAxis: {
			    	name: '日期',
			        data: data.xAxis,
			    },
			    yAxis: {
			    	name: '价格',
			    },
			    series: series
		};
		var myChart = echarts.init(document.getElementById('chart'));
		myChart.setOption(option);
	}
});
</script>
</body>  
</html>
