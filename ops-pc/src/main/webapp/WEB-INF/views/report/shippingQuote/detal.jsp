<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>详细界面</title>
</head>
<body>
<table id="resultList"></table>
<script type="text/javascript"> 
var _data='';
$.ajax({
  url:getRootPath()+'/transportPrice/queryByDetai1',
  async:false,
  data:{billId:"${param.fid}"},
  success:function(data){
	_data=data.rows;
	}
}); 
$("#resultList").jqGrid({
		data:_data,//加载本地数据,编辑
        gridComplete: completeMethod,
        footerrow:true,
		datatype:"local",
		autowidth:true,//自动填满宽度			 	
        height:$(window).outerHeight()-200+"px",
		forceFit:true,//调整列宽度，表格总宽度不会改变
		jsonReader:{
			records:"total",
			total: "totalpages",  
		}, 
	colModel:[ 
	    {name : 'fid',label :'fid',align:'center',width:"200px",hidden:true},            
        {name : 'deliveryPlaceName',label : '发货地',align:'center'}, 
        {name : 'deliveryPlaceId',label :'deliveryPlaceId',hidden:true},
        {name : 'receiptPlaceName',label : '收货地',align:'center'},
        {name : 'receiptPlaceId',label :'receiptPlaceId',hidden:true},
        {name : 'transportTypeName',label : '运输方式',align:'center'}, 
        {name : 'transportTypeId',label :'transportTypeId',hidden:true},
        {name : 'shipmentTypeName',label : '装运方式',align:'center'},
        {name : 'shipmentTypeId',label :'shipmentTypeId',hidden:true},
        {name : 'amount',label : '金额',align:'center'}, 
        {name : 'expectedDays',label : '预计时间',align:'center'},
        {name : 'describe',label : '备注',align:'center',},            
         ],
});

function completeMethod(){
    var sumAmount = $("#resultList").getCol("amount",false,'sum');
    $("#resultList").footerData('set',{
        'billDate':'合计',
        amount:sumAmount,
    });
}
</script>
</body>  
</html>
