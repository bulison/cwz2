<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
<style>
  #totalUnCheckAmount{display: inline-block;margin:10px 10px 10px 0px !important;font-weight: bold;font-size:16px}
</style>
  <div id="checked">
    <h2 style="margin: 10px 0;display: inline-block;">已对单单据：</h2><p style="margin: 10px 0;float:right;display: inline-block;" id="totalUnCheckAmount"></p>
    <table id="checkedGrid"></table>
    <div id="cpager"></div>
  </div>
  <hr/><br/>
  <div id="unchecked">
    <h2 style="display: inline-block;">对单单据：</h2><input id="dateStart"/> <input id="dateEnd"/> <input id="billCode"> <input id="billType"> <a href="javascript:;" id="billSearch-btn" class="btn-blue btn-s">筛选</a> <a href="javascript:;" id="billClear-btn" class="btn-blue btn-s">清空</a>
    <br/>
    <table id="uncheckedGrid"></table>
    <div id="ucpager"></div>
  </div>
  <hr/><br/>
<script type="text/javascript">
$("#totalUnCheckAmount").text("累计未对单金额："+getTotalUnCheckAmount());
var option = [
      		    {id:'1',name:'客户'},
      		    {id:'2',name:'供应商'}
      		];
var columns="";
var billkey = [];
//增加单据类型
var billType="";
if('${param.mark}'=='skd'){
	if($("#customerId").val()&&$("#amount").val()>=0){
		billType=[
				{value:"41",text:"销售出货单",selected:true},
				{value:"42",text:"销售退货单"},
				{value:"44",text:"销售发票"},
				{value:"56",text:"销售返利单"},
				{value:"53",text:"费用单"},
				{value:"51",text:"收款单"},
				{value:"93",text:"期初应收"},
				{value:"24",text:"收货单"},
		]
	}else if($("#customerId").val()&&$("#amount").val()<0){
		billType=[
					{value:"42",text:"销售退货单",selected:true},
					{value:"56",text:"销售返利单"},
					{value:"53",text:"费用单"},
					{value:"51",text:"收款单"},
					{value:"24",text:"收货单"},
		]
	}else if(!$("#customerId").val()&&$("#amount").val()>0){
		billType=[
	                {value:"53",text:"费用单",selected:true},
	                {value:"51",text:"收款单"},
	                {value:"24",text:"收货单"},
		]
	}else if(!$("#customerId").val()&&$("#amount").val()<0){
		billType=[
					{value:"56",text:"销售返利单",selected:true},
					{value:"51",text:"收款单"},
					{value:"24",text:"收货单"},
		]
	}
}else{
	if($("#supplierId").val()&&$("#amount").val()>=0){
		billType=[
				{value:"11",text:"采购入库单",selected:true},
				{value:"12",text:"采购退货单"},
				{value:"15",text:"采购发票"},
				{value:"55",text:"采购返利单"},
				{value:"53",text:"费用单"},
				{value:"52",text:"付款单"},
				{value:"92",text:"期初应付"},
				{value:"24",text:"收货单"},
		]
	}else if($("#supplierId").val()&&$("#amount").val()<0){
		billType=[
					{value:"12",text:"采购退货单",selected:true},
					{value:"55",text:"采购返利单"},
					{value:"53",text:"费用单"},
					{value:"52",text:"付款单"},
					{value:"24",text:"收货单"},
		]
	}else if(!$("#supplierId").val()&&$("#amount").val()>0){
		billType=[
	                {value:"53",text:"费用单",selected:true},
	                {value:"52",text:"付款单"},
	                {value:"24",text:"收货单"},
		]
	}else if(!$("#supplierId").val()&&$("#amount").val()<0){
		billType=[
					{value:"52",text:"付款单",selected:true},
					{value:"24",text:"收货单"},
		]
	}
}
var checkedGridColumns="";
	if('${param.mark}'=='skd'){
		columns=[
			{name:'billId',label:'billId',hidden:true,width:100},
			{name:'detal',label:'delta',hidden:true,width:100}, 
			{name:'expenseAmount',align:"center",label:'expenseAmount',hidden:true,width:100},
			{name:'billCode',label:'单号',align:"center",sortable:true,width:120,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'csvName',label:'收支单位',align:"center",sortable:true,width:120,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'billType',label:'单据类别',align:"center",sortable:true,width:100,formatter:function(value,options,row){
				if(value){
					for(var i=0; i<allBillType.length; i++){
						if (allBillType[i].value == value){
							billkey[options.rowId] = value;
							if(row.detal=='-1'){
								if(row.expenseType==1){
									return '<span style="color:red;">'+allBillType[i].text+'(增加)</span>';
								}else if(row.expenseType==2){
									return '<span style="color:red;">'+allBillType[i].text+'(减少)</span>';
								}else{
									return '<span style="color:red;">'+allBillType[i].text+'</span>';
								}
				  			}else{
				  				if(row.expenseType==1){
									return allBillType[i].text+'(增加)';
								}else if(row.expenseType==2){
									return allBillType[i].text+'(减少)';
								}else{
									return allBillType[i].text;
								}
				  			}
						}
					}
				}else{
					return "";
				}
			}},
			{name:'billDate',label:'单据日期',align:"center",sortable:true,width:100,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value.substring(0,10)+'</span>';
		  			}else{
		  				return value.substring(0,10);
		  			}
				}else{
					return "";
				}
			}},
			{name:'billAmount',label:'应收金额',align:"center",width:100,formatter:function(value,options,row){
				if(value){
					var total=parseFloat(value)+parseFloat(row.expenseAmount);
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+total.toFixed(2)+'</span>';
		  			}else{
		  				return total.toFixed(2);
		  			}
				}else{
					return "";
				}
			}},
			{name:'payedAmount',label:'累计已收金额',align:"center",width:100,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'unpayAmount',label:'未收金额',align:"center",width:100,formatter:function(value,options,row){
				if(row.billAmount&&row.payedAmount&&row.totalFreeAmount){
					var number=(parseFloat(row.expenseAmount)+parseFloat(row.billAmount)-parseFloat(row.payedAmount)-parseFloat(row.totalFreeAmount)).toFixed(2);
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+number+'</span>';
		  			}else{
		  				return number;
		  			}
				}else{
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+0+'</span>';
		  			}else{
		  				return 0;
		  			}
				}
			}},
			{name:'amount',label:'本次对单金额',align:"center",width:100,editable:true,edittype:"text",editoptions:{dataInit:function(cl){$(cl).textbox({novalidate:true,required:true,validType:"amount",missingMessage:"该项为必填项"});}},formatter:function(value,options,row){
				if(value){
					/* var expenseAmount = 0;
					var totalAmount = 0;
					var totalPayAmount = 0;
					var freeAmount = 0;
					if(row.expenseAmount){
						expenseAmount = parseFloat(row.expenseAmount);
					}
					if(row.totalAmount){
						totalAmount = parseFloat(row.totalAmount);
					}
					if(row.totalPayAmount){
						totalPayAmount = parseFloat(row.totalPayAmount);
					}
					if(row.freeAmount){
						freeAmount = parseFloat(row.freeAmount);
					}
					var number=(expenseAmount+totalAmount-totalPayAmount-freeAmount).toFixed(2);
					row.amount=number;
					return number; */
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+parseFloat(value).toFixed(2)+'</span>';
		  			}else{
		  				return parseFloat(value).toFixed(2);
		  			}
				}else{
					var rowid = options.rowId;
					if(row.unpayAmount){
						var unpayAmountTd = row.unpayAmount;
						var unpayAmount =$(unpayAmountTd)[0]?$(unpayAmountTd).text():unpayAmountTd;
						if(unpayAmount<0){
							unpayAmount=-unpayAmount;
						}
						if(row.billType==11||row.billType==12||row.billType==41||row.billType==42||row.billType==92||row.billType==93){
							var totalFreeAmountTd = row.totalFreeAmount;
							var totalFreeAmount =$(totalFreeAmountTd)[0]?$(totalFreeAmountTd).text():totalFreeAmountTd;
							return (unpayAmount - totalFreeAmount).toFixed(2);
						}else{
							return parseFloat(unpayAmount).toFixed(2);
						}
					}else{
						return "";
					}
				}
			}},
			{name:'freeAmountNow',label:'本次优惠金额',align:"center",width:100,editable:true,edittype:"text",editoptions:{dataInit:function(cl){$(cl).numberbox({precision:2});}},formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'totalFreeAmount',label:'累计优惠金额',align:"center",width:100,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'describe',label:'备注',align:"center",width:100,editable:true,edittype:"text",formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'action',label:'操作',align:"center",width:100,formatter:function(value,options,row){
				var statusStr = '';
				if(row.editing){
					statusStr = '<a class="btn-save" title="保存" href="javascript:;" onclick="saveRow(this)"></a> ';
			  		return statusStr ; 
				}else{
					statusStr = '<a class="btn-check" title="对单" href="javascript:;" onclick="checkById(\''+options.rowId+'\')"></a> '; 
			  		return statusStr ;
				}
			}}
	   ]
		
		checkedGridColumns=[
		                    {name:'fid',label:'fid',hidden:true,width:100}, 
			    			{name:'billCode',align:"center",label:'单号',sortable:true,width:120},
			    			{name:'csvName',align:"center",label:'收支单位',sortable:true,width:120},
			    			{name:'csvType',align:"center",label:'类别',sortable:true,width:100,formatter:function(value){
			    				for(var i=0; i<option.length; i++){
			    					if (option[i].id == value) return option[i].name;
			    				}
			    				return value?value:"";
			    			}},
			    			{name:'billDate',align:"center",label:'单据日期',sortable:true,width:100,formatter:function(value){
			    				if(value){
			    					return value.substring(0,10);
			    				}else{
			    					return "";
			    				}
			    			}},
			    			{name:'checkDate',align:"center",label:'对单日期',sortable:true,width:100,formatter:function(value){
			    				if(value){
			    					return value.substring(0,10);
			    				}else{
			    					return "";
			    				}
			    			}},
			    			{name:'billTotalAmount',align:"center",sortable:false,label:'单据应收金额',width:100},
			    			{name:'billTotalPayAmount',align:"center",sortable:false,label:'单据累计对单金额',width:100},
			    			{name:'amount',align:"center",label:'对单金额',sortable:false,width:100},
			    			{name:'freeAmount',align:"center",label:'优惠金额',sortable:false,width:100},
			    			{name:'describe',align:"center",label:'备注',sortable:false,width:100},
			    			{name:'action',align:"center",label:'操作',sortable:false,width:100,formatter:function(value,options,row){
			    				var statusStr = ''; 
			    				statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="deleteById(\''+row.fid+'\')"></a> ';
			    				return statusStr ; 
			    			}}
			    			]
	}else if('${param.mark}'=='fkd'){
		columns=[
			{name:'billId',align:"center",label:'fid',hidden:true,width:100},
			{name:'detal',align:"center",label:'delta',hidden:true,width:100}, 
			{name:'expenseAmount',align:"center",label:'expenseAmount',hidden:true,width:100}, 
			{name:'billCode',align:"center",label:'单号',sortable:true,width:120,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}
			}},
			{name:'csvName',align:"center",label:'收支单位',sortable:true,width:120,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'billType',align:"center",label:'单据类别',sortable:true,width:100,formatter:function(value,options,row){
				if(value){
					for(var i=0; i<allBillType.length; i++){
						if (allBillType[i].value == value){
							billkey[options.rowId] = value;
							if(row.detal=='-1'){
								if(row.expenseType==1){
									return '<span style="color:red;">'+allBillType[i].text+'(增加)</span>';
								}else if(row.expenseType==2){
									return '<span style="color:red;">'+allBillType[i].text+'(减少)</span>';
								}else{
									return '<span style="color:red;">'+allBillType[i].text+'</span>';
								}
				  			}else{
				  				if(row.expenseType==1){
									return allBillType[i].text+'(增加)';
								}else if(row.expenseType==2){
									return allBillType[i].text+'(减少)';
								}else{
									return allBillType[i].text;
								}
				  			}
						}
					}
					return value;
				}
			}},
			{name:'billDate',align:"center",label:'单据日期',sortable:true,width:100,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value.substring(0,10)+'</span>';
		  			}else{
		  				return value.substring(0,10);
		  			}
				}else{
					return "";
				}
			}},
			{name:'billAmount',align:"center",label:'应付金额',sortable:false,width:100,formatter:function(value,options,row){
				if(value){
					var total=parseFloat(value)+parseFloat(row.expenseAmount);
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+total.toFixed(2)+'</span>';
		  			}else{
		  				return total.toFixed(2);
		  			}
				}else{
					return "";
				}
			}},
			{name:'payedAmount',align:"center",label:'累计对单金额',sortable:false,width:100,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'unpayAmount',align:"center",label:'未付金额',sortable:false,width:100,formatter:function(value,options,row){
				if(row.billAmount&&row.payedAmount&&row.totalFreeAmount){
					var number=(parseFloat(row.billAmount)+parseFloat(row.expenseAmount)-parseFloat(row.payedAmount)-parseFloat(row.totalFreeAmount)).toFixed(2);
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+number+'</span>';
		  			}else{
		  				return number;
		  			}
				}else{
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+0+'</span>';
		  			}else{
		  				return 0;
		  			}
				}
			}},
			{name:'amount',align:"center",label:'本次对单金额',sortable:false,width:100,editable:true,edittype:"text",editoptions:{dataInit:function(cl){$(cl).textbox({novalidate:true,required:true,validType:"amount",missingMessage:"该项为必填项"});}},formatter:function(value,options,row){
				if(value){
					/* var expenseAmount = 0;
					var totalAmount = 0;
					var totalPayAmount = 0;
					var freeAmount = 0;
					if(row.expenseAmount){
						expenseAmount = parseFloat(row.expenseAmount);
					}
					if(row.totalAmount){
						totalAmount = parseFloat(row.totalAmount);
					}
					if(row.totalPayAmount){
						totalPayAmount = parseFloat(row.totalPayAmount);
					}
					if(row.freeAmount){
						freeAmount = parseFloat(row.freeAmount);
					}
					var number=(expenseAmount+totalAmount-totalPayAmount-freeAmount).toFixed(2);
					row.amount=number;
					return number; */
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+parseFloat(value).toFixed(2)+'</span>';
		  			}else{
		  				return parseFloat(value).toFixed(2);
		  			}
				}else{
					var rowid = options.rowId;
					if(row.unpayAmount){
						var unpayAmountTd = row.unpayAmount;
						var unpayAmount =$(unpayAmountTd)[0]?$(unpayAmountTd).text():unpayAmountTd;
						if(unpayAmount<0){
							unpayAmount=-unpayAmount;
						}
						if(row.billType==11||row.billType==12||row.billType==41||row.billType==42||row.billType==92||row.billType==93){
							var totalFreeAmountTd = row.totalFreeAmount;
							var totalFreeAmount =$(totalFreeAmountTd)[0]?$(totalFreeAmountTd).text():totalFreeAmountTd;
							return (unpayAmount - totalFreeAmount).toFixed(2);
						}else{
							return parseFloat(unpayAmount).toFixed(2);
						}
					}else{
						return "";
					}
				}
			}},
			{name:'freeAmountNow',align:"center",label:'本次优惠金额',sortable:false,width:100,editable:true,edittype:"text",editoptions:{dataInit:function(cl){$(cl).numberbox({precision:2});}},formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'totalFreeAmount',align:"center",label:'累计优惠金额',sortable:false,width:100,formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'describe',align:"center",label:'备注',width:100,sortable:false,editable:true,edittype:"text",formatter:function(value,options,row){
				if(value){
					if(row.detal=='-1'){
		  				return '<span style="color:red;">'+value+'</span>';
		  			}else{
		  				return value;
		  			}
				}else{
					return "";
				}
			}},
			{name:'action',align:"center",label:'操作',width:100,sortable:false,formatter:function(value,options,row){
				var statusStr = '';
				if(row.editing){
					statusStr = '<a class="btn-save" title="保存" href="javascript:;" onclick="saveRow(this)"></a> ';
			  		return statusStr ; 
				}else{
					statusStr = '<a class="btn-check" title="对单" href="javascript:;" onclick="checkById(\''+options.rowId+'\')"></a> '; 
			  		return statusStr ;
				}
			}}
	   ]
		checkedGridColumns=[
		                    {name:'fid',label:'fid',hidden:true,width:100}, 
			    			{name:'billCode',align:"center",label:'单号',sortable:true,width:120},
			    			{name:'csvName',align:"center",label:'收支单位',sortable:true,width:120},
			    			{name:'csvType',align:"center",label:'类别',sortable:true,width:100,formatter:function(value){
			    				for(var i=0; i<option.length; i++){
			    					if (option[i].id == value) return option[i].name;
			    				}
			    				return value?value:"";
			    			}},
			    			{name:'billDate',align:"center",label:'单据日期',sortable:true,width:100,formatter:function(value){
			    				if(value){
			    					return value.substring(0,10);
			    				}else{
			    					return "";
			    				}
			    			}},
			    			{name:'checkDate',align:"center",label:'对单日期',sortable:true,width:100,formatter:function(value){
			    				if(value){
			    					return value.substring(0,10);
			    				}else{
			    					return "";
			    				}
			    			}},
			    			{name:'billTotalAmount',align:"center",sortable:false,label:'单据对单金额',width:100},
			    			{name:'billTotalPayAmount',align:"center",sortable:false,label:'单据累计对单金额',width:100},
			    			{name:'amount',align:"center",sortable:false,label:'对单金额',width:100},
			    			{name:'freeAmount',align:"center",sortable:false,label:'优惠金额',width:100},
			    			{name:'describe',align:"center",sortable:false,label:'备注',width:100},
			    			{name:'action',align:"center",sortable:false,label:'操作',width:100,formatter:function(value,options,row){
			    				var statusStr = '';
			    				statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="deleteById(\''+row.fid+'\')"></a> ';
			    				return statusStr ; 
			    			}}
			    			]
	}
	$("#dateStart").datebox({
		prompt:'开始日期',
		width:100,
		height:30
	});

	$("#dateEnd").datebox({
		prompt:'结束日期',
		width:100,
		height:30
	});

	$("#billCode").textbox({
		prompt:'单号',
		width:100,
		height:30
	});
	
	$("#billType").fool("dhxCombo",{
		prompt:"单据类型",
		required:true,
		editable:false,
		novalidate:true,
		clearOpt:false,
		width:100,
		height:30,
		data:billType,
	});

	$("#billSearch-btn").click(function(){
		if($($("#billType").next()[0].comboObj.getInput()).validatebox("isValid")){
			$('#uncheckedGrid').form("disableValidation");
			var code=$("#billCode").textbox('getValue');
			var startDay=$("#dateStart").datebox('getValue');
			var endDay=$("#dateEnd").datebox('getValue');
			var billType=$("#billType").next()[0].comboObj.getSelectedValue();
			var options = {"billId":"${paymentBillId}","checkBillCode":code,"checkStartDay":startDay,"checkEndDay":endDay,"checkBillType":billType};
			$('#uncheckedGrid').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
		}
	});

	$("#billClear-btn").click(function(){
		$("#billCode").textbox('clear');
		$("#dateStart").datebox('clear');
		$("#dateEnd").datebox('clear');
	});

	  $("#checkedGrid").jqGrid({
			datatype:function(postdata){
				$.ajax({
					url:"${ctx}/paymentCheck/checkedList?paymentBillId=${paymentBillId}",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#checkedGrid")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			},
			autowidth:true,//自动填满宽度
			mtype:"post",
			pager:'#cpager',
			rowNum:10,
			rowList:[ 10, 20, 30 ],
			jsonReader:{
				records:"total",
				total: "totalpages",  
			}, 
			viewrecords:true,
			forceFit:true,//调整列宽度，表格总宽度不会改变
			//height:200,
			colModel:checkedGridColumns,
			/* rowStyler:function(value,row,index){
				console.log(row.detal);
				if (row.detal==-1){
					return 'color:red;';
				}
			}, */
			loadComplete: function(data){   
				var row = data.rows;
				for(var i = 0 ; i<row.length; i++){
					if (row[i].detal==-1){
						$("#checkedGrid #"+(i+1)).find("td").css("color","red"); 
					}
				}
			},
	  }).navGrid('#cpager',{add:false,del:false,edit:false,search:false,view:false});
	  
	  $("#uncheckedGrid").jqGrid({
		    //paymentBillId换成billId。
			postData:{
				billId:"${paymentBillId}",
				checkBillType:billType[0].value,
			},
			datatype:function(postdata){
				$.ajax({
					url:"${ctx}/paymentCheck/uncheckedList",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#uncheckedGrid")[0].addJSONData(data.responseJSON);
			        	}
			        }
				});
			},
			autowidth:true,//自动填满宽度
			mtype:"post",
			pager:'#ucpager',
			rowNum:10,
			rowList:[ 10, 20, 30 ],
			jsonReader:{
				records:"total",
				total: "totalpages",  
			}, 
			viewrecords:true,
			forceFit:true,//调整列宽度，表格总宽度不会改变
			//height:200,
			colModel:columns,
			loadComplete:function(data){
				var rows=data.rows;
				for(i in rows){
					var num=(parseFloat(rows[i].expenseAmount)+parseFloat(rows[i].billAmount)-parseFloat(rows[i].payedAmount)-parseFloat(rows[i].totalFreeAmount)).toFixed(2);
					$("#uncheckedGrid").jqGrid('setRowData',i+1,{amount:Math.abs(num)});
				}
			},
			gridComplete:function(){
				//修复IE当页数大于1时，页内数据全部对完单后有几率不会自动跳转第一页的问题
				/* setTimeout(function(){
					if($("#uncheckedGrid").jqGrid("getRowData").length == 0){
						$("#uncheckedGrid").jqGrid("setGridParam",{postData:{page:1}}).trigger("reloadGrid");
					}
				},400); */
			},
			onCellSelect:function(rowid,iCol,cellcontent,e){
				if(iCol!=14){
					$("#uncheckedGrid").jqGrid('editRow',rowid);
					$('#uncheckedGrid').jqGrid('setRowData', rowid, {editing:true,action:null});
					//单据类型为收付款、费用单时，禁用本次免单金额编辑框
					var selected=$("#uncheckedGrid").jqGrid('getRowData',rowid);
					if((selected.detal==1&&selected.unpayAmount<0)||selected.unpayAmount.search("-")!=-1){
						if($("#supplierId").val()&&$("#amount").val()>0){
							var ed=$("#uncheckedGrid").find("#"+rowid+'_freeAmountNow');
							ed.numberbox("disable");
						}
					}
					if(selected.billType.indexOf("销售发票")!=-1||selected.billType.indexOf("采购发票")!=-1||selected.billType.indexOf("费用单")!=-1||selected.billType.indexOf("采购返利")!=-1||selected.billType.indexOf("销售返利")!=-1){
						var ed=$("#uncheckedGrid").find("#"+rowid+'_freeAmountNow');
						ed.numberbox("disable");
					}
					$('#uncheckedGrid').find("#"+rowid+'_amount').textbox("textbox").focus(function(){
						$(this).select();
					});
					/* var unpayAmountTd = $("#uncheckedGrid #"+rowid+" td[aria-describedby=uncheckedGrid_unpayAmount]");
					var unpayAmount = unpayAmountTd.find("span").length>0?unpayAmountTd.children().text():unpayAmountTd.text();
					$("#uncheckedGrid").find("#"+rowid+'_amount').textbox("setValue",unpayAmount);//设定默认值 */
					$("#uncheckedGrid").find("#"+rowid+'_amount').textbox("textbox").focus();
				}
		    },
		   /*  onBeforeEdit:function(index,row){
				row.editing = true;
				updateActions(index);
			},
			onAfterEdit:function(index,row){
				row.editing = false;
				updateActions(index);
			}, */
	  }).navGrid('#ucpager',{add:false,del:false,edit:false,search:false,view:false});
	  
/*   function updateActions(index){
		$('#uncheckedGrid').datagrid('updateRow',{
			index: index,
			row:{}
		});
	} */
  
  function getRowIndex(target){
		var tr = $(target).closest('tr.jqgrow');
		return parseInt(tr.attr('id'));
	}
  
  function saveRow(target){
	  //$('#uncheckedGrid').datagrid('selectRow',getRowIndex(target));
	  var index = getRowIndex(target);
	  $('#uncheckedGrid #'+index).form("enableValidation");
	  if(!$('#uncheckedGrid #'+index).form("validate")){
		  return false;
	  }
	  var row=$('#uncheckedGrid').jqGrid('getRowData',index);
	  var uncollected=0;
	  if(row.detal!=-1){
		  uncollected=parseFloat(row.expenseAmount)+parseFloat(row.billAmount)-parseFloat(row.payedAmount)-parseFloat(row.totalFreeAmount);
	  }else{
		  uncollected=parseFloat(row.expenseAmount)+parseFloat($(row.billAmount).text())-parseFloat($(row.payedAmount).text())-parseFloat($(row.totalFreeAmount).text());
	  }
	  var amountEd=$(uncheckedGrid).find("#"+index+'_amount');
	  var amount=amountEd.numberbox('getValue');
	  /* if((row.billCode).indexOf("TH")==-1&&amount<0){
		  $.fool.alert({msg:"非退货类单据对单金额不能小于0！"});
		  return false; 
	  } */
	  var freeAmountNowEd=$(uncheckedGrid).find("#"+index+'_freeAmountNow');
	  var freeAmountNow=freeAmountNowEd.numberbox('getValue');
	  if(!amount){
		  amount=0;
	  }
	  if(!freeAmountNow){
		  freeAmountNow=0;
	  }
	  //去除校验："对单金额与免单金额之和不能大于未付金额"
	  /* if(parseFloat(amount)+parseFloat(freeAmountNow)<=parseFloat(uncollected).toFixed(2)){ */
		  $('#uncheckedGrid #'+index).form("disableValidation");
		  $('#uncheckedGrid').jqGrid('saveRow', index);
		  $('#uncheckedGrid').jqGrid('setRowData', index, {editing:false,action:null});
	  /* }else{
		  if('${param.mark}'=='fkd'){
			  $.fool.alert({msg:"对单金额与优惠金额之和不能大于未付金额！",fn:function(){
					return false; 
			  }}); 
		  }else{
			  $.fool.alert({msg:"对单金额与优惠金额之和不能大于未收金额！",fn:function(){
					return false; 
			  }}); 
		  }
		  
	  } */
	}
  
  function deleteById(fid){
	  $.fool.confirm({title:'确认',msg:'确定要删除此记录吗？',fn:function(r){
			 if(r){
				  $.ajax({
						type : 'post',
						url : '${ctx}/paymentCheck/delete',
						data : {fid:fid},
						dataType : 'json',
						success : function(data) {
							dataDispose(data);
							if(data.result == '0'){
								$.fool.alert({msg:"已删除！",fn:function(){
									$("#totalUnCheckAmount").text("累计未对单金额："+getTotalUnCheckAmount());
									$('#checkedGrid').trigger('reloadGrid');
									$('#uncheckedGrid').trigger('reloadGrid');
									$('#billList').trigger('reloadGrid');
									$("#mytotalUnCheckAmount").val(getTotalUnCheckAmount());
									$("#totalCheckAmount").val(($("#amount").val()-getTotalUnCheckAmount()).toFixed(2));
								}});
							}else{
								$.fool.alert({msg:data.msg});
							}
			    		},
			    		error:function(){
			    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
			    		}
					});
			 }
		 }});
  }
  
  function checkById(index){
	  var row=$('#uncheckedGrid').jqGrid('getRowData',index);
	  if(!row.amount){
		  $.fool.alert({msg:"请先点击填写本次对单金额。"});
		  return false;
	  }
	  $.fool.confirm({title:'确认',msg:'确定要勾对此单据吗？',fn:function(r){
			 if(r){
				  $.ajax({
						type : 'post',
						url : '${ctx}/paymentCheck/check',
						data : {billId:row.billId,billType:billkey[index],amount:row.amount,freeAmount:row.freeAmountNow,describe:row.describe,paymentBillId:'${paymentBillId}'},
						dataType : 'json',
						success : function(data) { 
							dataDispose(data);
							if(data.result == '0'){
								    $("#totalUnCheckAmount").text("累计未对单金额："+getTotalUnCheckAmount());
								    $('#checkedGrid').trigger('reloadGrid');
									$('#uncheckedGrid').trigger('reloadGrid');
									$('#billList').trigger('reloadGrid');
									$("#mytotalUnCheckAmount").val(getTotalUnCheckAmount());
									$("#totalCheckAmount").val(($("#amount").val()-getTotalUnCheckAmount()).toFixed(2));
							}else{
								if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
					    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.msg,fn:function(){
					    			}});
					    		}else{
						    		$.fool.alert({msg:data.msg,fn:function(){
						    		}});
					    		}
							}
			    		},
			    		error:function(){
			    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
			    		}
					});
			 }
		 }});
  }
  
  function getTotalUnCheckAmount(){
	  var result="";
	  $.ajax({
		  url:"${ctx}/payBill/getById?num="+Math.random(),
		  async:false,
		  data:{id:"${param.paymentBillId}"},
		  success:function(data){
			  if(data){
				  result=data.totalUnCheckAmount;
			  }
		  }
	  });
	  return result;
  }
</script>
</body>
</html>