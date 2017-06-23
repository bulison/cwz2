<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>资金计划弹窗</title>
</head>
<body>
    <style>
      #creatByPlan{
        margin:20px 0px 0px 50px
      }
    </style>
	<table id="capitalPlanList"></table>
	<div id="listPager"></div>
	<a id="creatByPlan" class="btn-blue btn-s" onclick="creatByPlan()" style="display: none">提取事件计划金额</a>
	<a id="saveAllRow" class="btn-blue btn-s" onclick="saveAllRow()">一键确认</a>
	<a id="cancelAllRow" class="btn-blue btn-s" onclick="cancelAllRow()">一键撤销</a>
	<a id="creatCapitalPlan" class="btn-blue btn-s" onclick="creatCapitalPlan()">保存</a>
<script type="text/javascript">
$('#capitalPlanList').jqGrid({
	datatype:function(postdata){
		postdata.relationId="${param.relationId}";
		$("#capitalPlanList").footerData('set',{action:'new'});
		$.ajax({
			url:'${ctx}/capitalPlanDetail/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#capitalPlanList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
		if("${param.relationSign}"==71){
			$('#capitalPlanList').showCol("planAmount");
			if($("#status_plan").next().find(".textbox-value").val()!=100){
				$("#saveAllRow").hide();
				$("#creatCapitalPlan").hide();
			}else{
				$('#capitalPlanList').showCol("action");
			}
		}else if("${param.relationSign}"==92||"${param.relationSign}"==93){
			$('#capitalPlanList').showCol("billAmount");
			$('#capitalPlanList').showCol("action");
		}else{
			$('#capitalPlanList').showCol("billAmount");
			if($("#recordStatus").val()!=0){
				$("#saveAllRow").hide();
				$("#creatCapitalPlan").hide();
			}else{
				$('#capitalPlanList').showCol("action");
			}
		}
	},
	/* forceFit:true, */
	viewrecords:true,
	footerrow: true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	/* autowidth:true,//自动填满宽度   */
	width:670,
	height:300,
	colModel:[ 
		    {name:'_isNew',label:'_isNew',hidden:true,editable:true,edittype:"text"},
		    {name:'unSaved',label:'unSaved',hidden:true},
		    {name:'editing',label:'editing',hidden:true},
		    {name:'recordStatus',label:'状态',hidden:true},
		    {name:'relationId',label:'relationId',hidden:true},
		    {name:'orgPaymentDate',label:'orgPaymentDate',hidden:true},
			{name:'paymentAmount',label:'收付款金额',hidden:true},
		    {name:'relationSign',label:'关联类型',hidden:true,editable:true,edittype:"text"},
		    {name:'action',align:"center",label:'操作',hidden:true,width:30,formatter:function(value,options,row){
	        	var index = options.rowId;
	        	if((value == 'new'&&$("#recordStatus").val()==0)||(value == 'new'&&"${param.relationSign}"==93)||(value == 'new'&&"${param.relationSign}"==92)){
	        		return "<a href='javascript:;' id='addrow' class='btn-add' onclick='rowAdder()' title='新增'></a>";
	        	}else{
	        		if(row.editing){ 
	        			var s = "<a href='javascript:;' class='btn-save editing-on btn-index-save-"+index+"' onclick='rowSaver(\""+index+"\")' title='确认'></a>";
	        			var c = "<a href='javascript:;' class='btn-back btn-index-cancel-"+index+"' onclick='rowCancel(\""+index+"\")' title='撤销'></a>";
	        			return s+c;
	        		}else{
	        			var sc = "<a href='javascript:;' class='btn-del btn-index-scRow-"+index+"' onclick='rowDeleter(\""+index+"\",2)' title='删除'></a>";
	        			if($("#recordStatus").val()==0||"${param.relationSign}"==93||"${param.relationSign}"==92){
	        				return sc;
	        			}else{
	        				return "";
	        			}
	        		}
	        	}
	        }},
	  		{name:'paymentDate',align:"center",label:'预计收付款日期',width:60,editable:true,edittype:"text",formatter:dateFormatAction},
	        {name:'billAmount',align:"center",label:'单据金额',hidden:true,width:60,editable:true,edittype:"text"},
	        {name:'planAmount',align:"center",label:'预计收付金额',hidden:true,width:60},
	      ],
	onCellSelect:function(rowid,iCol,cellcontent,e){
		if(iCol != 0 && $("#recordStatus").val()==0 && $("#capitalPlanList #"+rowid).find(".editing-on").length <= 0){
			editRow(rowid);
		}else if("${param.relationSign}"==93||"${param.relationSign}"==92||"${param.relationSign}"==71){
			editRow(rowid);
		}
	},

})/* .navGrid('#listPager',{add:false,del:false,edit:false,search:false,view:false}) */;

if("${param.relationSign}"==71){
	$("#creatByPlan").show();
	$('#capitalPlanList').jqGrid("setGridParam",{
		datatype:function(postdata){
			postdata.relationId="${param.relationId}";
			$("#capitalPlanList").footerData('set',{action:'new'});
			$.ajax({
				url:'${ctx}/capitalPlanDetail/queryByPlantId',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#capitalPlanList")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
			$('#capitalPlanList').showCol("planAmount");
		},
	}).trigger("reloadGrid");
}else{
	$('#capitalPlanList').jqGrid("setGridParam",{
		datatype:function(postdata){
			postdata.relationId="${param.relationId}";
			$("#capitalPlanList").footerData('set',{action:'new'});
			$.ajax({
				url:'${ctx}/capitalPlanDetail/list',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#capitalPlanList")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
			$('#capitalPlanList').showCol("billAmount");
		},
	}).trigger("reloadGrid");
}

//添加数据
function rowAdder(){
	var ids=$('#capitalPlanList').getDataIDs();
	var newIndex="";
	if(ids.length>0){
		newIndex=ids[ids.length-1]+1;
	}else{
		newIndex=1;
	}
	$('#capitalPlanList').addRowData(newIndex,{unSaved:true,_isNew:true,relationSign:"${param.relationSign}",relationId:"${param.relationId}",recordStatus:0,billAmount:0,paymentAmount:0,planAmount:0},"last");
	editRow(newIndex);
}
//撤销数据
function rowCancel(index){
	var _isNew = getEditor(index,'_isNew').val();
	$('#capitalPlanList').jqGrid('restoreRow', index);
	if(_isNew=='true'||_isNew==true){
		$("#capitalPlanList").jqGrid('delRowData',index);
	}else{
		$('#capitalPlanList').jqGrid('setRowData', index, {
			editing:false,
			action:null
		});
	}
}
//保存数据
function rowSaver(index){
	$("#capitalPlanList").find('tr#'+index).form("enableValidation");
	var v = $("#capitalPlanList").find('tr#'+index).form("validate");
	if(v){
		getEditor(index,'_isNew').val(false);
		var paymentDate = getEditor(index,'paymentDate').datebox("getValue");
		$('#capitalPlanList').jqGrid('setRowData',index,{editing:false,action:null,paymentDate:paymentDate});
		$('#capitalPlanList').jqGrid('saveRow', index);
	} 
}

//删除数据
/* function rowDeleter(index,flag){
	var row = $("#capitalPlanList").jqGrid('getRowData',index);
	$("#capitalPlanList").delRowData(index);
	return false;
} */
function rowDeleter(index,flag){
	var row = $("#capitalPlanList").jqGrid('getRowData',index);
	if(row.relationSign==92||row.relationSign==93){
		$("#capitalPlanList").delRowData(index);
		return false;
	}
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r) {
			if(row.unSaved=="true"){
				$.fool.alert({time:1000,msg:'删除成功！'});
				$("#capitalPlanList").delRowData(index);
				return false;
			}
			$.ajax({
				url:getRootPath()+"/capitalPlanDetail/delete",
				type:"POST",
				async:false,
				data:{id:index},
				success:function(data){
					if(data.returnCode =='0'){
						$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
							$("#capitalPlanList").trigger("reloadGrid");
						}});
					}else if(data.returnCode == '1'){
						$.fool.alert({msg:data.message});
						return false;
					}else{
						$.fool.alert({msg:'服务器繁忙，请稍后再试！'});
						return false;
					}
				}
			});
		}
	}});
}
//编辑列表
function editRow(index){
	var sign=getSign("${param.billType}");
	$("#capitalPlanList").jqGrid('editRow',index);
	$('#capitalPlanList').jqGrid('setRowData', index, {editing:true,action:null});// 编辑状态转换，按钮变化
	var $e={
			_isNew:getEditor(index,'_isNew'),
			paymentDate:getEditor(index,'paymentDate'),
			billAmount:getEditor(index,'billAmount'),
	};
	$e.paymentDate.datebox({
		height:'100%',width:'100%',
		editable:false,required:true,
		novalidate:true,
	});
	if("${param.relationSign}"==71){
		$e.billAmount.attr("disabled","disabled");
		return false;
	}
	if(sign==1){
		$e.billAmount.textbox({
			height:'100%',width:'100%',
			required:true,novalidate:true,
			validType:['amount','numMaxLength[10]'],
			onChange:function(newValue,oldValue){
				if((sign==1&&newValue<0)||(sign==0&&newValue>0)){
					$(this).textbox("setValue",-newValue);
				}
			}
		});
	}else if(sign==0){
		$e.billAmount.textbox({
			height:'100%',width:'100%',
			required:true,novalidate:true,
			validType:['minus','numMaxLength[10]'],
			onChange:function(newValue,oldValue){
				if((sign==1&&newValue<0)||(sign==0&&newValue>0)){
					$(this).textbox("setValue",-newValue);
				}
			}
		});
	}else if(sign==2){
		if(freeAmount>=0){
			$e.billAmount.textbox({
				height:'100%',width:'100%',
				required:true,novalidate:true,
				validType:['minus','numMaxLength[10]'],
				onChange:function(newValue,oldValue){
					if(newValue>=0){
						$(this).textbox("setValue",-newValue);
					}
				}
			});
		}else{
			$e.billAmount.textbox({
				height:'100%',width:'100%',
				required:true,novalidate:true,
				validType:['amount','numMaxLength[10]'],
				onChange:function(newValue,oldValue){
					if(newValue<0){
						$(this).textbox("setValue",-newValue);
					}
				}
			});
		}
	}
}

function getEditor(index,name){
	return $('#capitalPlanList').find("tr.jqgrow #"+index+"_"+name);
}

function creatCapitalPlan(){
	var details = $("#capitalPlanList").jqGrid('getRowData');
	if(details.length<1){
		$.fool.alert({msg:'你还没有添加任何明细'});
		return false;
	}
	var _dataPanel = $('#capitalPlanList');
	var _editing = _dataPanel.find(".editing-on");
	if(_editing.length>0){
		$.fool.alert({msg:'你还有没编辑完成的明细,请先确认！'});
		return false;
	}
	
	var relationId="${param.relationId}";
	var relationSign="${param.relationSign}";
	var fdata={};
	var totalBillAmount=0;
	var totalPlanAmount=0;
	for(var i=0;i<details.length;i++){
		if(details[i].billAmount){
			if(relationSign==71){
				totalPlanAmount=totalPlanAmount+parseFloat(details[i].planAmount);
			}else{
				totalBillAmount=totalBillAmount+parseFloat(details[i].billAmount);
			}
		}
	}
	if((relationSign==92||relationSign==93)&&Math.abs(totalBillAmount)!=Math.abs(_amount)){
		$.fool.alert({msg:'资金计划总金额必须等于应收/应付金额！'});
		return false;
	}
	$.ajax({
		type : 'post',
		async: false,
		url : getRootPath()+"/capitalPlan/list",
		data : {relationId :"${param.relationId}"},
		dataType : 'json',
		success : function(data) {
			if(data.rows.length>0){
				fdata=data.rows[0];
				if(relationSign==71){
					fdata.planAmount=totalPlanAmount;
					fdata.billAmount=0;
				}else{
					fdata.billAmount=totalBillAmount;
					fdata.planAmount=0;
				}
			}else{
				if(relationSign==71){
					fdata={relationId :relationId,relationSign:relationSign,calculation:1,code:$("#code_plan").val(),paymentAmount:0,paymentDate:$("#antipateEndTime_plan").datebox("getValue")};
				}else if(relationSign==92||relationSign==93){
					fdata={relationId :relationId,relationSign:relationSign,calculation:1,code:$("#code").val(),paymentAmount:0,paymentDate:$("#billDate").val()};
				}else if(relationSign==41){
					fdata={relationId :relationId,relationSign:relationSign,calculation:1,code:$("#code").val(),paymentAmount:0,paymentDate:$("#endDate").datebox("getValue")};
				}else if(relationSign==42){
					fdata={relationId :relationId,relationSign:relationSign,calculation:1,code:$("#code").val(),paymentAmount:0,paymentDate:$("#planEnd").datebox("getValue")};
				}else{
					fdata={relationId :relationId,relationSign:relationSign,calculation:1,code:$("#code").val(),paymentAmount:0,paymentDate:$("#billDate").datebox("getValue")};
				}
			}
			fdata=$.extend(fdata,{'details':JSON.stringify(details)});
		}
	});
	$.ajax({
		type : 'post',
		url : getRootPath()+"/capitalPlan/save",
		data :fdata,
		dataType : 'json',
		success : function(returnData) {
			if(returnData.returnCode==0){
				$.fool.alert({msg:'操作成功！',fn:function(){
					if(relationSign==71){
						$("#poper").window("close");
					}else{
						$("#pop-win").window("close");
					}
					if(relationSign==92||relationSign==93){
						$.ajax({
							url:getRootPath()+"/capitalPlan/passAudit",
							type:"POST",
							async:false,
							data:{id:returnData.data.id,recordStatus:1,updateTime:returnData.data.updateTime},
							success:function(data){
								if(data.returnCode =='0'){
						    	}else if(data.returnCode == '1'){
						    		$.fool.alert({msg:data.message});
								}else{
									$.fool.alert({msg:'服务器繁忙，请稍后再试！'});
						    		return false;
								}
						    }
						});
					}
				}});
			}else{
				$.fool.alert({msg:'操作失败！'+returnData.message});
			}
		}
	});
}

function getSign(billType){
	var sign=1;
	if(billType){
		if(billType=="cgrk"||billType=="cgfp"||billType=="xsth"||billType=="xsfld"||billType=="shd"||billType=="qckc"||billType=="qcyf"){
			sign=0;
		}else if(billType=="cgth"||billType=="cgfld"||billType=="xsch"||billType=="xsfp"||billType=="qcys"){
			sign=1;
		}else if(billType=="fyd"){
			if(expenseType==0){
				sign=2;
			}else if(expenseType==1){
				if(csvType==1){
					sign=1;
				}else if(csvType==2){
					sign=0;
				}
			}else if(expenseType==2){
                if(csvType==1){
                	sign=0;
				}else if(csvType==2){
					sign=1;
				}
			}
		}
	}
	return sign;
}

$.extend($.fn.validatebox.defaults.rules, {
	amount:{//验证金额，小数点后两位
        validator: function (value, param) {
        	if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
  			  if($(this).attr("class").indexOf("validatebox-text")!=-1){
  				  $(this).unbind("blur");
  				  $(this).blur(function(){//对数字进行去空格和自动保留2位小数
  					    var value = $(this).val();
          				var myvalue = "";
          				if(value!="" && !isNaN($.trim(value))){
	          					var nv = $.trim(value)+"";
	          					myvalue = parseFloat(nv).toFixed(2)+'';
          				}
	      					if($(this).attr("class").indexOf("textbox-text")!=-1){
	      						$(this).parent().prev().textbox('setText',myvalue);
	      						$(this).parent().prev().textbox('setValue',myvalue);
	      					}else{
	      						$(this).val(myvalue);
	      					}
          		});
          		}
        	  }
         	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
         },
         message:'金额必须大于等于0，并最多2位小数，请输入正确的金额'

     },	
     minus:{//验证金额，小数点后两位
         validator: function (value, param) {
         	if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
   			  if($(this).attr("class").indexOf("validatebox-text")!=-1){
   				  $(this).unbind("blur");
   				  $(this).blur(function(){//对数字进行去空格和自动保留2位小数
   					    var value = $(this).val();
           				var myvalue = "";
           				if(value!="" && !isNaN($.trim(value))){
 	          					var nv = $.trim(value)+"";
 	          					myvalue = parseFloat(nv).toFixed(2)+'';
           				}
 	      					if($(this).attr("class").indexOf("textbox-text")!=-1){
 	      						$(this).parent().prev().textbox('setText',myvalue);
 	      						$(this).parent().prev().textbox('setValue',myvalue);
 	      					}else{
 	      						$(this).val(myvalue);
 	      					}
           		});
           		}
         	  }
          	return (/^([-][0-9]\d*)(\.\d{1,2})?$/).test(value);
          },
          message:'金额必须小于0，并最多2位小数，请输入正确的金额'
     },	
})

//表格日期格式化
function dateFormatAction(value,options,row){
	return getDateStr(value);
}

function creatByPlan(){
	$.ajax({
		url:'${ctx}/flow/task/queryAllByPlan',
		data:{planId:"${param.relationId}"},
        dataType:"json",
        complete: function(data,stat){
        	var result=data.responseJSON;
        	var rows=[];
        	for(var i=1;i<result.length;i++){
        		if(result[i].amount!=0){
        			if(result[i].amount>0){
        				rows.push({paymentDate:result[i].antipateEndTime,planAmount:result[i].amount,relationSign:72,relationId:result[i].fid,recordStatus:0,paymentAmount:0,billAmount:0})
        			}else{
        				rows.push({paymentDate:result[i].antipateStartTime,planAmount:result[i].amount,relationSign:72,relationId:result[i].fid,recordStatus:0,paymentAmount:0,billAmount:0})
        			}
        			
        		}
        	}
        	$("#capitalPlanList")[0].addJSONData(rows);
        	for(var i=1;i<result.length;i++){
        		editRow(i);
        	}
        }
	});
}

function saveAllRow(){
	$("#gview_capitalPlanList").find(".btn-save").click();
}
function cancelAllRow(){
	$("#gview_capitalPlanList").find(".btn-back").click();
}
</script>
</body>
</html>
