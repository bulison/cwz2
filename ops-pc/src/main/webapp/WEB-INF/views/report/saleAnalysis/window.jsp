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
              <table id="myList"></table>
              <div id="myPager"></div>
          </div>

<script type="text/javascript">
var id= "${param.id}";
var cbillType = [];
cbillType[11]="采购入库单";
cbillType[12]="采购退货单";
cbillType[15]="采购发票";
cbillType[20]="盘点单";
cbillType[21]="调仓单";
cbillType[22]="报损单";
cbillType[30]="生产领料单";
cbillType[31]="成品入库单";
cbillType[32]="生产退料单";
cbillType[33]="成品退库单";
cbillType[41]="销售出货单";
cbillType[42]="销售退货单";
cbillType[44]="销售发票";
cbillType[51]="收款单";
cbillType[52]="付款单";
cbillType[53]="费用单";
cbillType[55]="采购返利单";
cbillType[56]="销售返利单";
$('#myList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:"${ctx}/api/rate/saleOutRelation?saleOutId="+id,
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#myList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	forceFit:true,
	height:250,
	width:$("#pop-win").width()*0.98,
	viewrecords:true,
	pager:'#myPager',
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	colModel:[
			  {name : 'billId',label : 'billId',hidden:true},
              {name : 'billDate',label : '日期',sortable:false,align:'center',width:"100px",formatter:function(value){
					if(value){
						return value.slice(0,10);
					}else{
						return "";
					}
              }}, 
              {name : 'billType',label : '单据类型',sortable:false,align:'center',width:"100px",formatter:function(value){
					return cbillType[value];
              }}, 
              {name : 'billCode',label : '单据编号',sortable:false,align:'center',width:"150px"},
              {name : 'goodsNameSpec',label : '货品+属性',sortable:false,align:'center',width:"100px"}, 
              {name : 'quentity',label : '数量',sortable:false,align:'center',width:"100px",formatter:function(value){
					if(value){
						return value.toFixed(2);
					}else{
						return 0;
					}
              }}, 
              {name : 'price',label : '价格',sortable:false,align:'center',width:"100px",formatter:function(value){
            	  if(value){
						return value.toFixed(2);
					}else{
						return 0;
					}
              }}, 
              {name : 'amount',label : '金额',sortable:false,align:'center',width:"100px",formatter:function(value){
            	  if(value){
						return value.toFixed(2);
					}else{
						return 0;
					}
              }},
              {name : 'costAmount',label : '成本金额',sortable:false,align:'center',width:"100px",formatter:function(value){
            	  if(value){
						return value.toFixed(2);
					}else{
						return 0;
					}
              }}
              ],
}).navGrid('#myPager',{add:false,del:false,edit:false,search:false,view:false});

</script>
</body>
</html>