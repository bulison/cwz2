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
    <h2 style="margin: 10px 0;display: inline-block;">已对单单据：</h2><p id="totalUnCheckAmount"></p>
    <table id="checkedGrid"></table>
    <div id="cpager"></div>
  </div>
  <hr/><br/>
  <div id="unchecked">
    <h2 style="display: inline-block;">对单单据：</h2><input id="billType"/><input id="dateStart"/> <input id="dateEnd"/> <input id="billCode"> <a id="billSearch-btn" class="btn-blue btn-s">筛选</a> <a id="billClear-btn" class="btn-blue btn-s">清空</a>
    <br/><br/><table id="uncheckedGrid"></table>
    <div id="ucpager"></div>
  </div>
  <hr/><br/>
<script type="text/javascript">
$("#totalUnCheckAmount").text("累计未对单金额："+getTotalUnCheckAmount());
var option = [
   		    {id:'1',name:'客户'},
   		    {id:'2',name:'供应商'}
   		];
var date=new Date();
var time=date.getTime();
var lastWeekDate=new Date(time-7*24*60*60*1000);
var today=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
var lastWeek=lastWeekDate.getFullYear()+"-"+(lastWeekDate.getMonth()+1)+"-"+lastWeekDate.getDate();
var mytips = "${param.tips}";
var tdata = [];//单据类型的数据集

$.ajax({
	url:"${ctx}/costBill/getBillType",
	async:false,
	data:{expenseType:"${param.expenseType}",csvType:"${param.csvType}"},
	success:function(data){
		dataDispose(data);
		tdata = formatData(data.obj,"billType");
		tdata[0].selected=true;
	}
});

$("#billType").fool("dhxCombo",{
	prompt:"单据类型",
	required:true,
	editable:false,
	novalidate:true,
	width:100,
	height:30,
	data:tdata,
	clearOpt:false,
	setTemplate:{
		  input:"#billName#",
		  option:"#billName#"
	}
});
$("#dateStart").datebox({
	prompt:'开始日期',
	width:100,
	editable:false,
	height:30,
	value:lastWeek
});

$("#dateEnd").datebox({
	prompt:'结束日期',
	width:100,
	height:30,
	editable:false,
	value:today
});

$("#billCode").textbox({
	prompt:'单号',
	width:100,
	height:30
});

$("#billSearch-btn").click(function(){
	/* $('#unchecked').form("enableValidation");
	if(!$('#unchecked').form("validate")){
		return false;
	} */
	$('#uncheckedGrid').form("disableValidation");
	var billType = $('#billType').next()[0].comboObj.getSelectedValue();
	var code=$("#billCode").textbox('getValue');
	var startDay=$("#dateStart").datebox('getValue');
	var endDay=$("#dateEnd").datebox('getValue');
	var supplierId = "";
	var customerId = "";
	if("${param.csvType}"==1){
		customerId = "${param.csvId}";
	}else if("${param.csvType}"==2){
		supplierId = "${param.csvId}";
	}
	var options={startDay:startDay,endDay:endDay,billType:billType,code:code,supplierId:supplierId,customerId:customerId};
	$('#uncheckedGrid').jqGrid("setGridParam",{postData:options}).trigger("reloadGrid");
});

$("#billClear-btn").click(function(){
	$("#billCode").textbox('clear');
	$("#dateStart").datebox('clear');
	$("#dateEnd").datebox('clear');
});

  $("#checkedGrid").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url:"${ctx}/costCheck/checkedList?costBillId=${costBillId}",
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
		colModel:[
		  		{name:'fid',label:'fid',hidden:true,width:100}, 
		  		{name:'billCode',label:'单号',align:"center",sortable:true,width:120},
		  		{name:'csvName',label:'收支单位',align:"center",sortable:true,width:120,formatter:function(value){
		  			return value?value:"";
		  		}},
		  		{name:'csvType',label:'类别',align:"center",sortable:true,width:100,formatter:function(value){
					for(var i=0; i<option.length; i++){
						if (option[i].id == value) return option[i].name;
					}
					return value?value:"";
				}},
		  		{name:'billDate',label:'单据日期',align:"center",sortable:true,width:100,formatter:function(value){
		  			if(value){
		  				return value.substring(0,10);
		  			}else{
		  				return "";
		  			}
		  		}},
		  		{name:'checkDate',label:'对单日期',align:"center",sortable:true,width:100,formatter:function(value){
		  			if(value){
		  				return value.substring(0,10);
		  			}else{
		  				return "";
		  			}
		  		}},
		  		{name:'billTotalAmount',label:'单据开单金额',align:"center",sortable:false,width:100},
		  		{name:'billTotalPayAmount',label:'单据累计对单金额',align:"center",sortable:false,width:100},
		  		{name:'amount',label:'对单金额',width:100,align:"center",sortable:false},
		  		{name:'describe',label:'备注',width:100,align:"center",sortable:false},
		  		{name:'action',label:'操作',width:100,align:"center",sortable:false,formatter:function(value,options,row){
		  			var statusStr = '';
			  		statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="deleteById(\''+row.fid+'\')"></a> ';
			  		return statusStr ; 
		  		}}
		      ]
  }).navGrid('#cpager',{add:false,del:false,edit:false,search:false,view:false});
  
  $("#uncheckedGrid").jqGrid({
	    datatype:function(postdata){
			$.ajax({
				url:"${ctx}/costCheck/uncheckedList",
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
		postData:{costBillId:"${costBillId}",startDay:lastWeek,endDay:today,billType:tdata[0].value},
		jsonReader:{
			records:"total",
			total: "totalpages",  
		}, 
		viewrecords:true,
		forceFit:true,//调整列宽度，表格总宽度不会改变
		//height:200,
		colModel:[
		  		{name:'billId',label:'billId',hidden:true,width:100}, 
		  		{name:'billCode',label:'单号',align:"center",sortable:true,width:120},
		  		{name:'billType',label:'单据类型',hidden:true,align:"center",sortable:true,width:120},
		  		{name:'csvName',label:'收支单位',align:"center",sortable:true,width:120,formatter:function(value){
		  			return value?value:"";
		  		}},
		  		{name:'csvType',label:'类别',align:"center",sortable:true,width:100,formatter:function(value){
					for(var i=0; i<option.length; i++){
						if (option[i].id == value) return option[i].name;
					}
					return value?value:"";
				}},
		  		{name:'billDate',label:'单据日期',align:"center",sortable:true,width:100,formatter:function(value){
		  			if(value){
		  				return value.substring(0,10);
		  			}else{
		  				return "";
		  			}
		  		}},
		  		/* {name:'payedAmount',label:'累计对单金额',align:"center",sortable:false,width:100,formatter:function(value){
		  			if(value=="0E-8"){
		  				return 0;
		  			}else{
		  				return value;
		  			}
		  		}}, */
		  		{name:'amount',label:'本次对单金额',align:"center",sortable:false,width:100,editable:true,edittype:"text",editoptions:{dataInit:function(cl){$(cl).textbox({required:true,novalidate:true,validType:["amount","numMaxLength[10]"],missingMessage:"该项为必填项"});}},formatter:function(value,options,row){
					if(value){
						return parseFloat(value).toFixed(2);
					}else{
						return 0;
					}
				}},
		  		{name:'describe',label:'备注',align:"center",sortable:false,width:100,editable:true,edittype:"text"},
		  		{name:'action',label:'操作',index:"action",align:"center",sortable:false,width:100,formatter:function(value,options,row){
		  			var statusStr = '';
		  			if(row.editing){
		  				statusStr = '<a class="btn-save" title="保存" href="javascript:;" onclick="saveRow(this)"></a> ';
				  		return statusStr ; 
		  			}else{
		  				statusStr = '<a class="btn-check" title="对单" href="javascript:;" onclick="checkById(\''+options.rowId+'\')"></a> '; 
				  		return statusStr ;
		  			}
		  		}}
		      ],
		onCellSelect:function(rowid,iCol,cellcontent,e){
			if(iCol!=8){
				$("#uncheckedGrid").jqGrid('editRow',rowid);
				$('#uncheckedGrid').jqGrid('setRowData', rowid, {editing:true,action:null});
				//var selected=$("#uncheckedGrid").jqGrid('getRowData',rowid);
				$('#uncheckedGrid').find("#"+rowid+'_amount').textbox("textbox").focus(function(){
					$(this).select();
				});
				$("#uncheckedGrid").find("#"+rowid+'_amount').textbox("textbox").focus();
			}
	    },
	    gridComplete:function(){
			//修复IE当页数大于1时，页内数据全部对完单后有几率不会自动跳转第一页的问题
			var gridOpt=$("#uncheckedGrid").jqGrid("getGridParam");
			setTimeout(function(){
				if($("#uncheckedGrid").jqGrid("getRowData").length == 0 && gridOpt.page != 1){
					$("#uncheckedGrid").jqGrid("setGridParam",{postData:{page:1}}).trigger("reloadGrid");
				}
			},400);
		}
/* 	    onBeforeEdit:function(index,row){
			row.editing = true;
			updateActions(index);
		},
		onAfterEdit:function(index,row){
			row.editing = false;
			updateActions(index);
		}, */
  }).navGrid('#ucpager',{add:false,del:false,edit:false,search:false,view:false});
  
 /*  function updateActions(index){
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
	  var index = getRowIndex(target);
	  $('#uncheckedGrid #'+index).form("enableValidation");
	  if(!$('#uncheckedGrid #'+index).form("validate")){
		  return false;
	  }
	    	$('#uncheckedGrid').jqGrid('saveRow', index);
	    	$('#uncheckedGrid').jqGrid('setRowData', index, {editing:false,action:null});
	}
  
  function deleteById(fid){
	  $.fool.confirm({title:'确认',msg:'确定要删除此记录吗？',fn:function(r){
			 if(r){
				  $.ajax({
						type : 'post',
						url : '${ctx}/costCheck/delete',
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
									$("#totalCheckAmount").val(($("#freeAmount").val()-getTotalUnCheckAmount()).toFixed(2));
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
	  $.fool.confirm({title:'确认',msg:'确定要对此单据进行对单吗？',fn:function(r){
			 if(r){
				  $.ajax({
						type : 'post',
						url : '${ctx}/costCheck/check',
						data : {billId:row.billId,billType:row.billType,amount:row.amount,describe:row.describe,costBillId:'${costBillId}'},
						dataType : 'json',
						success : function(data) { 
							dataDispose(data);
							if(data.result == '0'){
								    $("#totalUnCheckAmount").text("累计未对单金额："+getTotalUnCheckAmount());
								    $('#checkedGrid').trigger('reloadGrid');
									$('#uncheckedGrid').trigger('reloadGrid');
									$('#billList').trigger('reloadGrid');
									$("#mytotalUnCheckAmount").val(getTotalUnCheckAmount());
									$("#totalCheckAmount").val(($("#freeAmount").val()-getTotalUnCheckAmount()).toFixed(2));
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
		  url:"${ctx}/costBill/getById?num="+Math.random(),
		  async:false,
		  data:{id:"${param.costBillId}"},
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