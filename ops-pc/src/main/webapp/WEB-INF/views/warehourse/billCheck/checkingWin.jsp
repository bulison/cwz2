<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>被勾对单据列表窗口</title>
</head>

<body>
<table id="checkbill_List"></table>
<div id="checkPager"></div>
<script type="text/javascript">
  var options = [
   		    {id:'0',name:'未审核'},
   		    {id:'1',name:'已审核'},
   		    {id:'2',name:'已作废'}
   		];
  var mybillType = [
      		{name:"cgrk",code:11},              
      		{name:"cgth",code:12}, 
      		{name:"cgfp",code:15}, 
      		{name:"pdd",code:20}, 
      		{name:"dcd",code:21}, 
      		{name:"bsd",code:22}, 
      		{name:"scll",code:30}, 
      		{name:"cprk",code:31}, 
      		{name:"sctl",code:32}, 
      		{name:"cptk",code:33}, 
      		{name:"xsch",code:41}, 
      		{name:"xsth",code:42}, 
      		{name:"xsfp",code:44}, 
      		{name:"skd",code:51}, 
      		{name:"fkd",code:52}, 
      		{name:"fyd",code:53}, 
      		{name:"cgfld",code:55}, 
      		{name:"xsfld",code:56}, 
      		{name:"shd",code:24}, 
      ];
  var billType = "";
  for(var i=0; i<mybillType.length; i++){
	  if (mybillType[i].name == "${param._billCode}") billType = mybillType[i].code;
  }
  if("${param.billCode}"=="skd"){
	  $("#checkbill_List").jqGrid({
		  datatype:function(postdata){
				$.ajax({
					url:"${ctx}/payBill/getByWarehouseId?billId=${param.billId}&billType=51",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#checkbill_List")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			},
			forceFit:true,
			pager:'#checkPager',
			rowList:[ 10, 20, 30 ],
			viewrecords:true,
			rowNum:10,
			jsonReader:{
				records:"total",
				total: "totalpages",  
			}, 
			autowidth:true,//自动填满宽度
			height:350,
			colModel:[
		            {name:'fid',label:'fid',hidden:true,width:100},
		            {name:'code',label:'单号',align:"center",sortable:false,width:100<fool:tagOpt optCode="skdAction1">,formatter:function(value,options,row){
		            	return '<a title="查看" href="javascript:;" onclick="jump(\''+row.fid+'\',\''+row.recordStatus+'\',\'skd\')">'+value+'</a>';
		            }</fool:tagOpt>},
			  		{name:'customerName',align:"center",sortable:false,label:'客户名称',width:100},	  		
			  		{name:'memberName',align:"center",sortable:false,label:'收款人',width:100},
		            {name:'amount',label:'金额',align:"center",sortable:false,width:100,formatter:amountFormat},
		            {name:'totalCheckAmount',label:'累计勾对金额',align:"center",sortable:false,width:100,formatter:amountFormat},
		            {name:'billDate',label:'单据日期',align:"center",sortable:false,width:100,formatter:function(value){
		            	return value.substring(0,10);
		            }},
		            {name:'recordStatus',label:'状态',align:"center",sortable:false,width:100,formatter:statFormat},
		            ]
	  }).navGrid('#checkPager',{add:false,del:false,edit:false,search:false,view:false});
  }else if("${param.billCode}"=="fkd"){
	  $("#checkbill_List").jqGrid({
		  datatype:function(postdata){
				$.ajax({
					url:"${ctx}/payBill/getByWarehouseId?billId=${param.billId}&billType=52",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#checkbill_List")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			},
			forceFit:true,
			pager:'#checkPager',
			rowList:[ 10, 20, 30 ],
			viewrecords:true,
			rowNum:10,
			jsonReader:{
				records:"total",
				total: "totalpages",  
			}, 
			autowidth:true,//自动填满宽度
			height:350,
			colModel:[
		            {name:'fid',label:'fid',hidden:true,width:100},
		            {name:'code',label:'单号',align:"center",sortable:false,width:100<fool:tagOpt optCode="fkdAction1">,formatter:function(value,options,row){
		            	return '<a title="查看" href="javascript:;" onclick="jump(\''+row.fid+'\',\''+row.recordStatus+'\',\'fkd\')">'+value+'</a>';
		            }</fool:tagOpt>},
		            {name:'supplierName',label:'供应商名称',align:"center",sortable:false,width:100},
		            {name:'memberName',label:'付款人',align:"center",sortable:false,width:100},
		            {name:'amount',label:'金额',align:"center",sortable:false,width:100,formatter:amountFormat},
		            {name:'totalCheckAmount',label:'累计勾对金额',align:"center",sortable:false,width:100,formatter:amountFormat},
		            {name:'billDate',label:'单据日期',align:"center",sortable:false,width:100,formatter:function(value){
		            	return value.substring(0,10);
		            }},
		            {name:'recordStatus',label:'状态',align:"center",sortable:false,width:100,formatter:statFormat},
		            ]
	  }).navGrid('#checkPager',{add:false,del:false,edit:false,search:false,view:false});
  }else if("${param.billCode}"=="fyd"){
	  $("#checkbill_List").jqGrid({
		  datatype:function(postdata){
				$.ajax({
					url:"${ctx}/costBill/getByWarehouseId?billId=${param.billId}&billType=53",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#checkbill_List")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			},
			forceFit:true,
			pager:'#checkPager',
			rowList:[ 10, 20, 30 ],
			viewrecords:true,
			rowNum:10,
			jsonReader:{
				records:"total",
				total: "totalpages",  
			}, 
			autowidth:true,//自动填满宽度
			height:350,
			colModel:[
		            {name:'fid',label:'fid',hidden:true,width:100},
		            {name:'billCode',label:'单号',align:"center",sortable:false,width:100<fool:tagOpt optCode="fydAction1">,formatter:function(value,options,row){
		            	return '<a title="查看" href="javascript:;" onclick="jump(\''+row.fid+'\',\''+row.recordStatus+'\',\'fyd\')">'+value+'</a>';
		            }</fool:tagOpt>},
		            {name:'csvName',label:'收支单位名称',align:"center",sortable:false,width:100},	  		
			  		{name:'memberName',label:'经手人',align:"center",sortable:false,width:100},
			  		{name:'freeAmount',label:'支出金额',align:"center",sortable:false,width:100,formatter:amountFormat},
			  		{name:'incomeAmount',label:'收入金额',align:"center",sortable:false,width:100,formatter:amountFormat},
			  		{name:'billTotalPayAmount',label:'累计勾对金额',align:"center",sortable:false,width:100,formatter:amountFormat},
		            {name:'billDate',label:'单据日期',align:"center",sortable:false,width:100,formatter:function(value){
		            	return value.substring(0,10);
		            }},
		            {name:'recordStatus',label:'状态',align:"center",sortable:false,width:100,formatter:statFormat},
		            ]
	  }).navGrid('#checkPager',{add:false,del:false,edit:false,search:false,view:false});
  }
  
  function amountFormat(value){
	  if(value=="0E-8"){
		  return 0;
	  }else{
		  return value;
	  }
  }
  function statFormat(value){
	  if(value){
		  for(var i=0; i<options.length; i++){
			  if (options[i].id == value) return options[i].name;
		  }
	  }else{
		  return "";
	  }
  }
  
  function jump(id,recordStatus,billCode){
	  if(billCode=="fyd"){
		  var src='/costBill/manage?id='+id+"&recordStatus="+recordStatus+"&num="+Math.random();
		  var text='费用单';
		  parent.kk(src,text);
	  }else if(billCode=="fkd"){
		  var src='/payBill/manage?id='+id+"&recordStatus="+recordStatus+"&num="+Math.random();
		  var text='付款单';
		  parent.kk(src,text);
	  }else if(billCode=="skd"){
		  var src='/receiveBill/manage?id='+id+"&recordStatus="+recordStatus+"&num="+Math.random();
		  var text='收款单';
		  parent.kk(src,text);
	  }
  }
</script>

</body>
</html>
