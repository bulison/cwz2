<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>绑定单据</title>
</head>
<body>
    <style>
        #leftSide{
            width:49%;
            display: inline-block;
            padding:5px;
            border:1px solid rgb(197,197,197);
            float:left
        }
        #rightSide{
            width:49%;
            display: inline-block;
            padding:5px;
            border:1px solid rgb(197,197,197);
            float:right
        }
        .tableTitle{
            margin:5px
        }
    </style>
    <div id="leftSide">
        <p class="tableTitle">已绑定单据：</p>
        <table id="checkedListL"></table>
        <p class="tableTitle">准备绑定单据：<input id="billTypeL"/></p>
        <table id="uncheckedListL"></table>
    </div>
    <div id="rightSide">
        <p class="tableTitle">已绑定收付款单据：</p>
        <table id="checkedListR"></table>
        <p class="tableTitle">准备绑定收付款单据：<input id="billTypeR"/></p>
        <table id="uncheckedListR"></table>
    </div>
    <script type="text/javascript">
        $("#billTypeL").fool("dhxCombo",{
        	width:100,
        	height:25,
        	prompt:"单据类型",
        	editable:false,
        	data:[
        		  {value:11,text:"采购入库"},
        		  {value:15,text:"采购发票"},
        		  {value:42,text:"销售退货"},
        		  {value:56,text:"销售返利"},
        		  {value:92,text:"期初应付"},
        		  {value:12,text:"采购退货"},
        		  {value:55,text:"采购返利"},
        		  {value:41,text:"销售出货"},
        		  {value:44,text:"销售发票"},
        		  {value:93,text:"期初应收"},
        		  {value:24,text:"收货单"},
        		  {value:53,text:"费用单"},
        	],
        	onChange:function(value,text){
        		$('#uncheckedListL').jqGrid("setGridParam",{
        			datatype:function(postdata){
        				postdata.billType=value;
        				postdata.planId=$("#relationId").val();
        				$.ajax({
            				url:getRootPath()+'/api/capitalPlanDetail/uncheckedList',
        				    data:postdata,
        		            dataType:"json",
        		            complete: function(data,stat){
        		            	if(stat=="success") {
        		            		$("#uncheckedListL")[0].addJSONData(data.responseJSON.rows);
        		            	}
        		            }
            			});
        			},
        		}).trigger("reloadGrid");
        	}
        })
        $("#billTypeR").fool("dhxCombo",{
        	width:100,
        	height:25,
        	prompt:"单据类型",
        	editable:false,
        	data:[
      		      {value:51,text:"收款单"},
      		      {value:52,text:"付款单"},
      		],
      		onChange:function(value,text){
      			$('#uncheckedListR').jqGrid("setGridParam",{
        			datatype:function(postdata){
        				postdata.billType=value;
        				postdata.planId=$("#relationId").val();
        				$.ajax({
            				url:getRootPath()+'/api/capitalPlanDetail/uncheckedList',
        				    data:postdata,
        		            dataType:"json",
        		            complete: function(data,stat){
        		            	if(stat=="success") {
        		            		$("#uncheckedListR")[0].addJSONData(data.responseJSON.rows);
        		            	}
        		            }
            			});
        			},
        		}).trigger("reloadGrid");
        	}
        })
        $('#checkedListL').jqGrid({
        	datatype:function(postdata){
        		postdata.detailId="${param.detailId}",
        		postdata.bindType=1,
        		$.ajax({
    				url:getRootPath()+'/api/capitalPlanDetail/checkedList',
				    data:postdata,
		            dataType:"json",
		            complete: function(data,stat){
		            	if(stat=="success") {
		            		$("#checkedListL")[0].addJSONData(data.responseJSON.content);
		            	}
		            }
    			});
        	},
		    autowidth:true,// 自动填满宽度
		    height:$("#relationWin").height()/2-70,
		    forceFit:true,// 改变列宽度，总宽度不变
		    colModel:[
		    	{name:'id',label:'id',align:"center",hidden:true},
			    {name:'billDate',label:'日期',align:"center",width:60,formatter:dateFormatAction},
			    {name:'code',label:'单号',align:"center",width:80},
			    {name:'csvName',label:'往来单位',align:"center",width:80},
			    {name:'billAmount',label:'单据金额',align:"center",width:70},
			    {name:'bindAmount',label:'已绑定金额',align:"center",width:70},
			    {name:'action',label:'操作',width:30,align:"center",formatter:function(value,options,row){
			    	var index = options.rowId;
			    	return "<a href='javascript:;' class='btn-del editing-on btn-index-del-"+index+"' onclick='deleteBinding(\""+index+"\",1)' title='取消绑定'></a>";
			    }},
			],
        });
        $('#uncheckedListL').jqGrid({
		    autowidth:true,// 自动填满宽度
		    height:$("#relationWin").height()/2-70,
		    forceFit:true,// 改变列宽度，总宽度不变
		    colModel:[
		    	{name:'id',label:'id',hidden:true},
			    {name:'editing',label:'编辑状态',hidden:true},
			    {name:'unitId',label:'unitId',hidden:true},
			    {name:'checkAmount',label:'checkAmount',hidden:true},
			    {name:'billDate',label:'日期',align:"center",width:60,formatter:dateFormatAction},
			    {name:'code',label:'单号',align:"center",width:100},
			    {name:'unitName',label:'往来单位',align:"center",width:60},
			    {name:'amount',label:'单据金额',align:"center",width:50},
			    {name:'inputAmount',label:'绑定金额',align:"center",width:50,precision:4,editable:true,edittype:"text",formatter:function(value,options,row){
			    	if(value){
			    		return value;
			    	}else{
			    		return (parseFloat(row.amount)-parseFloat(row.checkAmount)).toFixed(2);
			    	}
			    }},
			    {name:'checkAmount',label:'已绑定金额',align:"center",width:50},
			    {name:'action',label:'操作',width:50,align:"center",formatter:function(value,options,row){
			    	var index = options.rowId;
			    	if(row.editing){
		    			var s = "<a href='javascript:;' class='btn-save editing-on btn-index-save-"+index+"' onclick='save(\""+index+"\",1)' title='确认'></a>";
		    			var c = "<a href='javascript:;' class='btn-back btn-index-cancel-"+index+"' onclick='cancel(\""+index+"\",1)' title='撤销'></a>";
		    			return s+c;
		    		}else{
		    			return "<a href='javascript:;' class='btn-check btn-index-check-"+index+"' onclick='binding(\""+index+"\",1)' title='绑定'></a>";
		    		}
			    }},
			],
			onCellSelect:function(rowid,iCol,cellcontent,e){
				if(iCol != 0 && $("#uncheckedListL #"+rowid).find(".editing-on").length <= 0){
					editBill(rowid,1);
				}
			},
        });
        $('#checkedListR').jqGrid({
        	datatype:function(postdata){
        		postdata.detailId="${param.detailId}",
        		postdata.bindType=2,
        		$.ajax({
    				url:getRootPath()+'/api/capitalPlanDetail/checkedList',
				    data:postdata,
		            dataType:"json",
		            complete: function(data,stat){
		            	if(stat=="success") {
		            		$("#checkedListR")[0].addJSONData(data.responseJSON.content);
		            	}
		            }
    			});
        	},
		    autowidth:true,// 自动填满宽度
		    height:$("#relationWin").height()/2-70,
		    forceFit:true,// 改变列宽度，总宽度不变
		    colModel:[
		    	{name:'id',label:'id',align:"center",hidden:true},
		    	{name:'billDate',label:'日期',align:"center",width:60,formatter:dateFormatAction},
			    {name:'code',label:'单号',align:"center",width:80},
			    {name:'csvName',label:'往来单位',align:"center",width:80},
			    {name:'billAmount',label:'单据金额',align:"center",width:70},
			    {name:'bindAmount',label:'已绑定金额',align:"center",width:70},
			    {name:'action',label:'操作',width:30,align:"center",formatter:function(value,options,row){
			    	var index = options.rowId;
			    	return "<a href='javascript:;' class='btn-del editing-on btn-index-del-"+index+"' onclick='deleteBinding(\""+index+"\",2)' title='取消绑定'></a>";
			    }},
			],
        });
        $('#uncheckedListR').jqGrid({
		    autowidth:true,// 自动填满宽度
		    height:$("#relationWin").height()/2-70,
		    forceFit:true,// 改变列宽度，总宽度不变
		    colModel:[
		    	{name:'id',label:'id',hidden:true},
			    {name:'editing',label:'编辑状态',hidden:true},
			    {name:'unitId',label:'unitId',hidden:true},
			    {name:'checkAmount',label:'checkAmount',hidden:true},
			    {name:'billDate',label:'日期',align:"center",width:60,formatter:dateFormatAction},
			    {name:'code',label:'单号',align:"center",width:100},
			    {name:'unitName',label:'往来单位',align:"center",width:60},
			    {name:'amount',label:'单据金额',align:"center",width:50},
			    {name:'inputAmount',label:'绑定金额',align:"center",width:50,precision:4,editable:true,edittype:"text",,formatter:function(value,options,row){
			    	if(value){
			    		return value;
			    	}else{
			    		return (parseFloat(row.amount)-parseFloat(row.checkAmount)).toFixed(2);
			    	}
			    }},
			    {name:'checkAmount',label:'已绑定金额',align:"center",width:50},
			    {name:'action',label:'操作',width:50,align:"center",formatter:function(value,options,row){
			    	var index = options.rowId;
			    	if(row.editing){
		    			var s = "<a href='javascript:;' class='btn-save editing-on btn-index-save-"+index+"' onclick='save(\""+index+"\",2)' title='确认'></a>";
		    			var c = "<a href='javascript:;' class='btn-back btn-index-cancel-"+index+"' onclick='cancel(\""+index+"\",2)' title='撤销'></a>";
		    			return s+c;
		    		}else{
		    			return "<a href='javascript:;' class='btn-check btn-index-check-"+index+"' onclick='binding(\""+index+"\",2)' title='绑定'></a>"; 
		    		}
			    }},
			],
			onCellSelect:function(rowid,iCol,cellcontent,e){
				if(iCol != 0 && $("#uncheckedListL #"+rowid).find(".editing-on").length <= 0){
					editBill(rowid,2);
				}
			},
        });
        
        //编辑列表
        function editBill(index,flag){
        	var row="";
        	var target="";
        	if(flag==1){
        		target=$('#uncheckedListL');
        	}else if(flag==2){
        		target=$('#uncheckedListR');
        	}
        	row=target.getRowData(index);
        	target.jqGrid('editRow',index);
        	target.jqGrid('setRowData', index, {editing:true,action:null});// 编辑状态转换，按钮变化
        	var $e={
        			inputAmount:getEditor(index,'inputAmount',flag),
        	};
        	if(row.amount>=0){
        		$e.inputAmount.numberbox({
            		height:'100%',width:'100%',
            		required:true,precision:2,
            		validType:'amount',
            		novalidate:true,
            	});
        	}else{
        		$e.inputAmount.numberbox({
            		height:'100%',width:'100%',
            		required:true,precision:2,
            		validType:'minus',
            		novalidate:true,
            	});
        	}
        	if(row.inputAmount){
        		$e.inputAmount.numberbox("setValue",row.inputAmount);
        	}else{
        		$e.inputAmount.numberbox("setValue",row.amount);
        	}
        }
        //撤销列表
        function cancel(index,flag){
        	var target="";
        	if(flag==1){
        		target=$('#uncheckedListL');
        	}else if(flag==2){
        		target=$('#uncheckedListR');
        	}
        	target.jqGrid('restoreRow', index);
        	target.jqGrid('setRowData', index, {
    			editing:false,
    			action:null
    		});
        	$(".tooltip").hide();
        }
        
        //保存
        function save(index,flag){
        	var target="";
        	if(flag==1){
        		target=$('#uncheckedListL');
        	}else if(flag==2){
        		target=$('#uncheckedListR');
        	}
        	target.find('tr#'+index).form("enableValidation");
        	var v = target.find('tr#'+index).form("validate");
        	if(!v){
        		return false;
        	}
        	target.jqGrid('setRowData', index, {
    			editing:false,
    			action:null
    		});
        	target.jqGrid('saveRow', index);
        }
        
        //绑定单据
        function binding(index,flag){
        	var target="";
        	var relationSign="";
        	if(flag==1){
        		target=$('#uncheckedListL');
        		target2=$('#checkedListL');
        		relationSign=($("#billTypeL").next())[0].comboObj.getSelectedValue();
        	}else if(flag==2){
        		target=$('#uncheckedListR');
        		target2=$('#checkedListR');
        		relationSign=($("#billTypeR").next())[0].comboObj.getSelectedValue();
        	}
        	
        	/* target.jqGrid('setRowData',index,{editing:false,action:null});
        	target.jqGrid('saveRow', index); */
        	var rowData=target.getRowData(index);
        	$.ajax({
				url:getRootPath()+"/capitalPlanDetail/bindingBill",
				type:"POST",
				async:false,
				data:{
					detailId:"${param.detailId}",
					bindType:flag,
					relationSign:relationSign,
					relationId:rowData.id,
					inputAmount:rowData.inputAmount,
					checkAmount:rowData.checkAmount,
					billAmount:rowData.amount,
					code:rowData.code,
					csvId:rowData.unitId,
					csvName:rowData.unitName,
					billDate:rowData.billDate,
				},
				
				success:function(data){
					if(data.returnCode =='0'){
						$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
							target.trigger("reloadGrid");
							target2.trigger("reloadGrid");
							$("#goodsList").trigger("reloadGrid");
							$("#billList").trigger("reloadGrid");
							$(".tooltip").hide();
			    		}});
			    	}else if(data.returnCode == '1'){
			    		$.fool.alert({msg:data.message});
			    		editBill(index,flag);
			    		$(".tooltip").hide();
			    		return false;
					}else{
						$.fool.alert({msg:'服务器繁忙，请稍后再试！'});
						$(".tooltip").hide();
			    		return false;
					}
			    }
			});
        }
        
        //解绑单据
        function deleteBinding(index,flag){
        	if(flag==1){
        		target=$('#checkedListL');
        		target2=$('#uncheckedListL');
        	}else if(flag==2){
        		target=$('#checkedListR');
        		target2=$('#uncheckedListR');
        	}
        	$.ajax({
				url:getRootPath()+"/capitalPlanBill/delete", 
				type:"POST",
				async:false,
				data:{id:index,bindType:flag},
				success:function(data){
					if(data.returnCode =='0'){
						$.fool.alert({time:1000,msg:'解绑成功！',fn:function(){
							target.trigger("reloadGrid");
							target2.trigger("reloadGrid");
							$("#goodsList").trigger("reloadGrid");
							$("#billList").trigger("reloadGrid");
			    		}});
			    	}else if(data.returnCode == '1'){
			    		$.fool.alert({msg:data.message});
			    		editBill(index,flag);
			    		return false;
					}else{
						$.fool.alert({msg:'服务器繁忙，请稍后再试！'});
			    		return false;
					}
			    }
			});
        }
        
        function getEditor(index,name,flag){
        	if(flag==1){
        		return $('#uncheckedListL').find("tr.jqgrow #"+index+"_"+name);
        	}else if(flag==2){
        		return $('#uncheckedListR').find("tr.jqgrow #"+index+"_"+name);
        	}
        }
    </script>
</body>
</html>