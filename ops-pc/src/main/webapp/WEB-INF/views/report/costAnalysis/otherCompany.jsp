<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
<style>
</style>
          <div>
          	  <div style="margin:5px;">
				<a href="javascript:;" style="margin-right:5px;" class="btn-blue btn-s" id="yes">选择</a>
				<a href="javascript:;" class="btn-blue btn-s" id="myclose">关闭</a>
			  </div>
              <table id="otherList"></table>
              <div id="otherPager"></div>
          </div>

<script type="text/javascript">
var _supplierId = "${param.supplierId}";
var _deliveryPlaceId = "${param.deliveryPlaceId}";
var _receiptPlaceId = "${param.receiptPlaceId}";
var _transportTypeId = "${param.transportTypeId}";
var _shipmentTypeId = "${param.shipmentTypeId}";
$('#otherList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:"${ctx}/api/costAnalysisBillDetail/findOtherCompany?supplierId="+_supplierId+"&deliveryPlaceId="+_deliveryPlaceId+"&receiptPlaceId="+_receiptPlaceId+"&transportTypeId="+_transportTypeId+"&shipmentTypeId="+_shipmentTypeId,
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#otherList")[0].addJSONData(data.responseJSON.content);
	        	}
	        }
		});
	},
	forceFit:true,
	height:200,
	width:$("#other").width()*0.98,
	viewrecords:true,
	pager:'#otherPager',
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	colModel:[
			  {name : 'fid',label : 'fid',hidden:true}, 
			  {name : 'deliveryPlaceName',label : '发货地',hidden:true}, 
			  {name : 'executeSign',label : 'executeSign',hidden:true},
              {name : 'receiptPlaceName',label : '收货地',hidden:true}, 
              {name : 'transportTypeName',label : '运输方式',hidden:true}, 
              {name : 'shipmentTypeName',label : '装运方式',hidden:true},
              {name : 'deliveryPlaceId',label : 'deliveryPlaceId',hidden:true}, 
              {name : 'receiptPlaceId',label : 'receiptPlaceId',hidden:true}, 
              {name : 'transportTypeId',label : 'transportTypeId',hidden:true}, 
              {name : 'transportUnitId',label : 'transportUnitId',hidden:true}, 
              {name : 'shipmentTypeId',label : 'shipmentTypeId',hidden:true},
              {name : 'supplierId',label : 'supplierId',hidden:true}, 
              {name : 'billDate',label : '日期',align:'center',width:"100px"}, 
              {name : 'supplierName',label : '运输公司',align:'center',width:"100px"}, 
              {name : 'transportUnitName',label : '运输单位',align:'center',width:"100px"},
              {name : 'amount',label : 'amount',hidden:true,formatter:priceFormat}, 
              {name : 'freightPrice',label : '运费',align:'center',width:"100px",formatter:function(value,options,row){
            	  return row.amount;
              }}, 
              //{name : 'conversionRate',label : '换算率',align:'center',width:"100px",formatter:priceFormat}, 
              //{name : 'basePrice',label : '基本运费',align:'center',width:"100px",formatter:priceFormat}, 
              {name : 'expectedDays',label : '预计时间',align:'center',width:"100px"}, 
              {name : '_executeSign',label : '执行标识',align:'center',width:"100px",formatter:function(value,options,row){
            	  return row.executeSign == 1?"可执行":row.executeSign == 2?"难执行":row.executeSign == 3?"无法执行":"";
              }}, 
              {name : 'remark',label : '备注',align:'center',width:"100px"},
              ],
              ondblClickRow:function(rowid,iRow,iCol,e){
      			var row = $('#otherList').jqGrid("getRowData",rowid);
      			var goodsId = $('#goodsId').val(),
      			goodsSpecId = $('#goodsSpecId').val(),
      			transportUnitId = row.transportUnitId,
      			shipmentTypeId = row.shipmentTypeId,
      			billId = row.fid;
      			$.ajax({
      				url:getRootPath()+"/goodsTransport/findTopBySpec?goodsId="+goodsId+"&goodSpecId="+goodsSpecId+"&transportUnitId="+transportUnitId+"&shipmentTypeId="+shipmentTypeId,
      				async:false,
      				success:function(data){
      					row.conversionRate = data.conversionRate;
      				}
      			});
      		$.ajax({
      			url:getRootPath()+"/transportPrice/queryByAmount?billId="+billId+"&goodsId="+goodsId+"&goodSpecId="+goodsSpecId+"&transportUnitId="+transportUnitId+"&shipmentTypeId="+shipmentTypeId,
      			async:false,
      			success:function(data){
      				row.basePrice = data;
      			}
      		});
      		row.transportBillId = row.fid;
      		delete row["fid"];
      			if('${param.onDblClick}'.length>0){
      			     eval(${param.onDblClick}(row));
      			}
      		  }
}).navGrid('#otherPager',{add:false,del:false,edit:false,search:false,view:false});

$('#yes').click(function(){
	var id = $('#otherList').jqGrid('getGridParam', 'selrow');
	var row = $('#otherList').jqGrid("getRowData",id);
	var goodsId = $('#goodsId').val(),
		goodsSpecId = $('#goodsSpecId').val(),
		transportUnitId = row.transportUnitId,
		shipmentTypeId = row.shipmentTypeId,
		billId = row.fid;
	$.ajax({
		url:getRootPath()+"/goodsTransport/findTopBySpec?goodsId="+goodsId+"&goodSpecId="+goodsSpecId+"&transportUnitId="+transportUnitId+"&shipmentTypeId="+shipmentTypeId,
		async:false,
		success:function(data){
			row.conversionRate = data.conversionRate;
		}
	});
	$.ajax({
		url:getRootPath()+"/transportPrice/queryByAmount?billId="+billId+"&goodsId="+goodsId+"&goodSpecId="+goodsSpecId+"&transportUnitId="+transportUnitId+"&shipmentTypeId="+shipmentTypeId,
		async:false,
		success:function(data){
			row.basePrice = data;
		}
	});
	row.transportBillId = row.fid;
	delete row["fid"];
	if('${param.okCallBack}'.length>0){
		eval(${param.okCallBack}(row));
	}
});

$('#myclose').click(function(){
	$('#other').window("close");
});
</script>
</body>
</html>