function initGoodsList(){
	$('#goodsList').jqGrid({
		datatype:function(postdata){
			postdata.capitalId=$("#id").val();
			$("#goodsList").footerData('set',{action:'new',planAmount:0,billAmount:0,paymentAmount:0});
			if(postdata.capitalId){
				$.ajax({
					url:getRootPath()+'/api/capitalPlanDetail/list',
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		$("#goodsList")[0].addJSONData(data.responseJSON);
			        		$("#goodsList").footerData('set',{action:'new',planAmount:0,billAmount:0,paymentAmount:0});
			    	    	getTotal();
			    	    	if($('#recordStatus').val()&&$('#recordStatus').val()!=0){
			    	    		$("#addrow").hide();
			    	    	}
			        	}
			        }
				});
			}
		},
		footerrow: true,
		autowidth:true,// 自动填满宽度
		height:"100%",
		forceFit:true,// 改变列宽度，总宽度不变
		multiselect:true,
		onCellSelect:function(rowid,iCol,cellcontent,e){
			var row=$('#goodsList').getRowData(rowid);
			if(iCol != 0 && flag != "detail"&&row.recordStatus==0 && $("#goodsList #"+rowid).find(".editing-on").length <= 0&&$("#relationSign").val()==0){
				editRow(rowid);
			}
		},
		colModel:[
			{name:'action',label:'操作',width:90,align:"center",sortable:false,formatter:actions},
			{name:'badDebtDate',label:'badDebtDate',hidden:true},
			{name:'completeDate',label:'completeDate',hidden:true},
			{name:'createTime',label:'createTime',hidden:true},
			{name:'auditTime',label:'auditTime',hidden:true},
			{name:'cancelTime',label:'cancelTime',hidden:true},
			{name:'badDebtName',label:'badDebtName',hidden:true},
			{name:'completeName',label:'completeName',hidden:true},
			{name:'creatorName',label:'creatorName',hidden:true},
			{name:'auditorName',label:'auditorName',hidden:true},
			{name:'cancelorName',label:'cancelorName',hidden:true},
			{name:'editing',label:'编辑状态',hidden:true,editable:true,edittype:"text"},
			{name:'recordStatus',label:'状态',hidden:true},
			{name:'updateTime',label:'updateTime',hidden:true},
			{name:'orgPaymentDate',label:'orgPaymentDate',hidden:true},
			{name:'relationSign',label:'关联类型',hidden:true,editable:true,edittype:"text"},
			{name:'relationId',label:'relationId',hidden:true},
			{name:'_isNew',label:'是否新增',hidden:true,editable:true,edittype:"text"},
			{name:'explain',label:'说明',align:"center",sortable:false,width:80,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
				$(ed).textbox({validType:'maxLength[200]',width:'100%',height:'100%'});
			}}},
			{name:'paymentDate',label:'预计收付款日期',align:"center",width:50,sortable:false,editable:true,edittype:"text"},
			{name:'planAmount',label:'计划收付金额',align:"center",width:80,sortable:false,precision:2,editable:true,edittype:"text",formatter:function(value,options,row){
				if(!value){
					return 0
				}else{
					return value;
				}
			}},
			{name:'billAmount',label:'单据金额',align:"center",width:80,sortable:false,precision:4,editable:true,edittype:"text",formatter:function(value,options,row){
				if(!value){
					return 0
				}else{
					return value;
				}
			}},
			{name:'paymentAmount',label:'收付款金额',align:"center",width:80,sortable:false,precision:4,editable:true,edittype:"text",formatter:function(value,options,row){
				if(!value){
					return 0
				}else{
					return value;
				}
			}},
			{name:'remark',label:'备注',align:"center",sortable:false,width:80,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
				$(ed).textbox({validType:'maxLength[200]',width:'100%',height:'100%'});
			}}},
			{name:'_recordStatus',label:'状态',align:"center",width:40,formatter:function(value,options,row){
				for(var i=0; i<recordStatusOptions.length; i++){
					if (recordStatusOptions[i].id == row.recordStatus) return recordStatusOptions[i].name;
				}
				return value;
			}},
	    ],
	});
}

//从表按钮设置
function actions(value,options,row){
	var index = options.rowId;
	var relationSign=$("#relationSign").val();
	if(value == 'new'){
		if(relationSign!=0){
			return "";
		}
		return "<a href='javascript:;' id='addrow' class='btn-add' onclick='rowAdder()' title='新增'></a>";
	}else{
		if(row.editing){
			var s = "<a href='javascript:;' class='btn-save editing-on btn-index-save-"+index+"' onclick='rowSaver(\""+index+"\")' title='确认'></a>";
			var c = "<a href='javascript:;' class='btn-back btn-index-cancel-"+index+"' onclick='rowCancel(\""+index+"\")' title='撤销'></a>";
			return s+c;
		}else{
			var sc = "";
			var qx = "";
			var tzje = "";
			var wc="";
			if(relationSign==0||relationSign==71){
				sc = "<a href='javascript:;' class='btn-del btn-index-scRow-"+index+"' onclick='rowDeleter(\""+index+"\",2)' title='删除'></a>";
				qx = "<a href='javascript:;' class='btn-cancel btn-index-qxRow-"+index+"' onclick='cancelDetail(\""+row.id+"\",\""+row.updateTime+"\",2)' title='取消'></a>";
				tzje = "<a href='javascript:;' class='btn-money btn-index-tzjeRow-"+index+"' onclick='popWin(\""+row.id+"\",\""+row.updateTime+"\",3,2,\""+row.planAmount+"\")' title='调整金额'></a>";
				wc = "<a href='javascript:;' class='btn-ok btn-index-wcRow-"+index+"' onclick='finfishDetail(\""+row.id+"\",\""+row.updateTime+"\",2,1)' title='完成'></a>";
			}
			var cf="";
			if(relationSign==0||relationSign==71){
				cf = "<a href='javascript:;' class='btn-split btn-index-cfRow-"+index+"' onclick='popWin(\""+row.id+"\",\""+row.updateTime+"\",2,2,\""+row.planAmount+"\")' title='拆分'></a>";
			}else{
				cf = "<a href='javascript:;' class='btn-split btn-index-cfRow-"+index+"' onclick='popWin(\""+row.id+"\",\""+row.updateTime+"\",2,2,\""+row.billAmount+"\")' title='拆分'></a>";
			}
			var bddj = "";
			if(relationSign==71){
				bddj = "<a href='javascript:;' class='btn-binding btn-index-bddjRow-"+index+"' onclick='bindingBill(\""+row.id+"\")' title='绑定单据'></a>";
			}
			var zbhz = "<a href='javascript:;' class='btn-bad btn-index-zbhzRow-"+index+"' onclick='baddebtDetail(\""+row.id+"\",\""+row.updateTime+"\",2)' title='准备坏账'></a>";
			var yq = "<a href='javascript:;' class='btn-delay btn-index-yqRow-"+index+"' onclick='popWin(\""+row.id+"\",\""+row.updateTime+"\",1,2,\""+row.paymentDate+"\")' title='延期'></a>";
			
			var qxhz = "<a href='javascript:;' class='btn-cancelBad btn-index-qxRow-"+index+"' onclick='cancelBadDetail(\""+row.id+"\",\""+row.updateTime+"\",2)' title='取消坏账'></a>";
			var czxq = "<a href='javascript:;' class='btn-detail btn-index-bddjRow-"+index+"' onclick='operateDetail(\""+row.id+"\")' title='操作详情'></a>";
			if(row.recordStatus==0){
				return sc+czxq;
			}else if(row.recordStatus==1){
				return zbhz+yq+tzje+cf+wc+qx+bddj+czxq;
			}else if(row.recordStatus==2){
				return qxhz+czxq;
			}else if(row.recordStatus==3){
				return czxq;
			}else if(row.recordStatus==4){
				return czxq;
			}else{
				return sc+czxq;
			} 
		}
	}
}

//管理页面列表操作按钮设置
function actionFormat(value,options,row){
	var sc = "";
	if(row.relationSign==0||row.relationSign==71){
		sc = "<a href='javascript:;' class='btn-del' onclick='rowDeleter(\""+row.id+"\",1)' title='删除'></a>";
	}
	var zbhz = "<a href='javascript:;' class='btn-bad' onclick='baddebtDetail(\""+row.id+"\",\""+row.updateTime+"\",1)' title='准备坏账'></a> ";
	var yq = "<a href='javascript:;' class='btn-delay' onclick='popWin(\""+row.id+"\",\""+row.updateTime+"\",1,1,\""+row.paymentDate+"\")' title='延期'></a> ";
	var cf=""
	if(row.relationSign==0||row.relationSign==72){
		cf = "<a href='javascript:;' class='btn-split' onclick='popWin(\""+row.id+"\",\""+row.updateTime+"\",2,1,\""+row.planAmount+"\")' title='拆分'></a>";
	}else{
		cf = "<a href='javascript:;' class='btn-split' onclick='popWin(\""+row.id+"\",\""+row.updateTime+"\",2,1,\""+row.billAmount+"\")' title='拆分'></a>";
	}
	var wc="";
	if(row.relationSign==72||row.relationSign==0){
		wc = "<a href='javascript:;' class='btn-ok' onclick='finfishDetail(\""+row.id+"\",\""+row.updateTime+"\",1,1)' title='完成'></a> ";
	}
	var qxhz = "<a href='javascript:;' class='btn-cancelBad' onclick='cancelBadDetail(\""+row.id+"\",\""+row.updateTime+"\",1)' title='取消坏账'></a> ";
	
	if(row.recordStatus==0){
		return sc;
	}else if(row.recordStatus==1){
		return zbhz+""+yq+""+cf+""+wc;
	}else if(row.recordStatus==2){
		return qxhz;
	}else if(row.recordStatus==3){
		return "";
	}else if(row.recordStatus==4){
		return "";
	}
}

//添加从表数据
function rowAdder(){
	var ids=$('#goodsList').getDataIDs();
	var newIndex="";
	if(ids.length>0){
		newIndex=ids[ids.length-1]+1;
	}else{
		newIndex=1;
	}
	$('#goodsList').addRowData(newIndex,{_isNew:true,planAmount:0,billAmount:0,paymentAmount:0,recordStatus:0,relationSign:0,relationId:$("#relationId").val()},"last");
	editRow(newIndex);
}
//撤销从表数据
function rowCancel(index){
	var _isNew = getTableEditor(index,'_isNew').val();
	if(_isNew=='true'||_isNew==true){
		$.fool.confirm({msg:'您确定要撤销该记录？',fn:function(r){
			if(r) {
				$("#goodsList").jqGrid('delRowData',index);
			}
		}});
	}else{
		$('#goodsList').jqGrid('restoreRow', index);
		$('#goodsList').jqGrid('setRowData', index, {
			editing:false,
			action:null
		});
	}
}
//保存从表数据
function rowSaver(index){
	$("#goodsList").find('tr#'+index).form("enableValidation");
	var v = $("#goodsList").find('tr#'+index).form("validate");
	if(v){
		getTableEditor(index,'_isNew').val(false);
		var paymentDate = getTableEditor(index,'paymentDate').datebox("getValue");
		$('#goodsList').jqGrid('setRowData',index,{paymentDate:paymentDate,editing:false,action:null});
		$('#goodsList').jqGrid('saveRow', index);
		getTotal();
	}
}
//删除从表数据
function rowDeleter(index,flag){
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r) {
			if(flag==1){
				$.ajax({
					url:getRootPath()+"/capitalPlanDetail/delete",
					type:"POST",
					async:false,
					data:{id:index},
					success:function(data){
						if(data.returnCode =='0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$("#billList").trigger("reloadGrid");
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
			}else if(flag==2){
				$("#goodsList").jqGrid('delRowData',index);
				getTotal();
			}
		}
	}});
}
//编辑列表
function editRow(index){
	$("#goodsList").jqGrid('editRow',index);
	$('#goodsList').jqGrid('setRowData', index, {editing:true,action:null});// 编辑状态转换，按钮变化
	var $e={
			_isNew:getTableEditor(index,'_isNew'),
			relationSign:getTableEditor(index,'relationSign'),
			explain:getTableEditor(index,'explain'),
			paymentDate:getTableEditor(index,'paymentDate'),
			planAmount:getTableEditor(index,'planAmount'),
			billAmount:getTableEditor(index,'billAmount'),
			paymentAmount:getTableEditor(index,'paymentAmount'),
			remark:getTableEditor(index,'remark'),
	};
	$e.paymentDate.datebox({
		height:'100%',width:'100%',
		editable:false,
		required:true
	});
	if($("#relationSign").val()!=71&&$("#relationSign").val()!=0&&$e.relationSign.val()!=0){
		$e.planAmount.numberbox({
			height:'100%',width:'100%',
			precision:2,validType:'numMaxLength[10]',
			required:true,disabled:true,
			max:9999999999.99
		});
	}else{
		$e.planAmount.numberbox({
			height:'100%',width:'100%',
			precision:2,validType:'numMaxLength[10]',
			required:true,max:9999999999.99
		});
	}
	$e.billAmount.numberbox({
		height:'100%',width:'100%',
		precision:4,validType:'numMaxLength[10]',
        disabled:true,max:9999999999.99
	});
	$e.paymentAmount.numberbox({
		height:'100%',width:'100%',
		precision:4,validType:'numMaxLength[10]',
		disabled:true,max:9999999999.99
	});
	if(!$e.relationSign.val()){
		$e.relationSign.val(0);
	}
}

//页脚按钮设置
function initFooterBtn(){
	var $obj = $(".mybtn-footer");
	var _stat = $('#recordStatus').val();
	var relationSign=$("#relationSign").val();
	var _id = $("#id").val();
	var _btn = "";
	var s = "<a id=\"saveForm\" class=\"mybtn-blue mybtn-s\" onclick=\"saveForm()\">保存</a>";
	var d = "<a id=\"delForm\" class=\"mybtn-blue mybtn-s\" onclick=\"delForm()\">删除</a>";
	var a = "<a id=\"auditForm\" class=\"mybtn-blue mybtn-s\" onclick=\"auditForm()\">审核</a>";
	var una = "<a id=\"unAuditForm\" class=\"mybtn-blue mybtn-s\" onclick=\"unAuditForm()\">反审核</a>";
	var b = "<a id=\"baddebtForm\" class=\"mybtn-blue mybtn-s\" onclick=\"baddebtForm()\">准备坏账</a>";
	var f = "<a id=\"finfishForm\" class=\"mybtn-blue mybtn-s\" onclick=\"finfishForm("+relationSign+")\">完成</a>";
	var c = "<a id=\"cancelForm\" class=\"mybtn-blue mybtn-s\" onclick=\"cancelForm()\">取消</a>";
	var m = "<a id=\"mergeForm\" class=\"mybtn-blue mybtn-s\" onclick=\"mergeForm()\">合并</a>";
	var cb = "<a id=\"cancelBadForm\" class=\"mybtn-blue mybtn-s\" onclick=\"cancelBadForm()\">取消坏账</a>";
	if(_stat!=""){
		if(_stat==0){
			_btn=s;
			if(relationSign==0||relationSign==71){
				_btn=_btn+d;
			}
			if(relationSign==0){
				_btn=_btn+a;
			}
		}else if(_stat == 1){
			_btn=b+m;
			if(relationSign==0){
				_btn=_btn+una;
			}
			if(relationSign==0||relationSign==71){
				_btn=_btn+f+c;
			}
		}else if(_stat == 2){
			_btn=cb;
		}else if(_stat == 3){
			_btn="";
		}else if(_stat == 4){
			_btn="";
		}
	}else{
		_btn=s;
	}
	$obj.html(_btn);
}

function getTotal(){
	var rows=$('#goodsList').jqGrid('getRowData');
	var planAmount=0;
	var billAmount=0;
	var paymentAmount=0;
	for(var i=0;i<rows.length;i++){
		if(rows[i].action.search(/editing-on/) != -1){
			continue;
		}
		if("undefined" != typeof rows[i].planAmount&&rows[i].planAmount!=""){
			planAmount += parseFloat(rows[i].planAmount);
		}
		if("undefined" != typeof rows[i].billAmount&&rows[i].billAmount!=""){
			billAmount += parseFloat(rows[i].billAmount);
		}
		if("undefined" != typeof rows[i].paymentAmount&&rows[i].paymentAmount!=""){
			paymentAmount += parseFloat(rows[i].paymentAmount);
		}
	}
	$('#goodsList').footerData("set",{planAmount:planAmount.toFixed(2),billAmount:billAmount.toFixed(2),paymentAmount:paymentAmount.toFixed(2)},false);
	$("#planAmount").numberbox("setValue",planAmount);
	$("#billAmount").numberbox("setValue",billAmount);
	$("#paymentAmount").numberbox("setValue",paymentAmount);
}

//保存表单
function saveForm(){
	if(!$("#list2").fool('fromVali')){
		return false;
	}
	var details = $("#goodsList").jqGrid('getRowData');
	if(details.length<1){
		$.fool.alert({msg:'你还没有添加任何明细'});
		return false;
	}
	var _dataPanel = $('#goodsList');
	var _editing = _dataPanel.find(".editing-on");
	if(_editing.length>0){
		$.fool.alert({msg:'你还有没编辑完成的明细,请先确认！'});
		return false;
	}
	$('#save').attr("disabled","disabled");
	if(updateTime){
		$("#updateTime").val(updateTime.slice(0,19))
	}
	var fdata = $("#warehousebillForm").serializeJson();
	fdata = $.extend(fdata,{'details':JSON.stringify(details)});
	var url=getRootPath()+'/capitalPlan/save';
	$.ajax({
		url:url,
		type:"PUT",
		async:false,
		data:fdata,
		success:function(data){
			if(data.returnCode =='0'){
				$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
					$("#billList").trigger("reloadGrid");
					viewRowNew(data.data.id,recordStatus); 
	    			return true;
	    		}});
	    	}else if(data.returnCode == '1'){
	    		$.fool.alert({msg:data.message,fn:function(){
	    			$('#save').removeAttr("disabled"); 
	    		}});
	    		return false;
			}else{
	    		$.fool.alert({msg:'服务器繁忙，请稍后再试！'});
	    		return false;
			}
	    }
	});
}
//表单删除
function delForm(fid){
	$('#delForm').attr("disabled","disabled");
	$.fool.confirm({msg:'确定要删除该记录吗？',fn:function(r){
		if(r){
			$.ajax({
				url:getRootPath()+"/capitalPlan/delete",
				type:"POST",
				async:false,
				data:{id:$("#id").val()},
				success:function(data){
					if(data.returnCode =='0'){
						$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
							$("#billList").trigger("reloadGrid");
							$('#addBox').window("close");
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

//表单审核
function auditForm(){
	formChangeStatus($('#auditForm'),1);
}
//表单反审核
function unAuditForm(){
	formChangeStatus($('#unAuditForm'),0);
}
//表单准备坏账
function baddebtForm(){
	formChangeStatus($('#baddebtForm'),2);
}
//表单取消坏账
function cancelBadForm(){
	formChangeStatus($('#cancelBadForm'),1);
}
//表单完成
function finfishForm(relationSign){
	var editType=0;
	if(relationSign==71){
		editType=1
	}
	formChangeStatus($('#finfishForm'),3,editType);
}
//表单取消
function cancelForm(){
	formChangeStatus($('#cancelForm'),4);
}
//表单合并
function mergeForm(){
	if($('#goodsList').getGridParam('selarrrow').length<2){
		$.fool.alert({msg:"请先选取至少两个明细。"});
		return;
	}
	var selections=$('#goodsList').getGridParam('selarrrow').join(",");
	$.ajax({
		url:getRootPath()+"/capitalPlanDetail/mergeDetail",
		type:"POST",
		async:false,
		data:{ids:selections,planId:$("#id").val(),relationId:$("#relationId").val()},
		success:function(data){
			if(data.returnCode =='0'){
				$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
					$("#goodsList").trigger("reloadGrid");
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

//明细准备坏账
function baddebtDetail(fid,updateTime,flag){
	detailChangeStatus(fid,updateTime,2,flag);
}
//明细完成
function finfishDetail(fid,updateTime,flag,editType){
	detailChangeStatus(fid,updateTime,3,flag,editType);
	
}
//明细取消坏账
function cancelBadDetail(fid,updateTime,flag){
	detailChangeStatus(fid,updateTime,1,flag);
}
//明细取消
function cancelDetail(fid,updateTime,flag){
	detailChangeStatus(fid,updateTime,4,flag);
}

//明细绑定单据
function bindingBill(fid){
	$("#relationWin").window("open");
	$("#relationWin").window("refresh",getRootPath()+"/capitalPlanDetail/bingingPage?detailId="+fid);
}

//明细调整金额
function adjustDetail(){
	if($("#adjustAmount").textbox("isValid")&&$("#remarker").validatebox("isValid")){
		if(parseFloat($("#capitalId").attr("data"))==parseFloat($("#adjustAmount").textbox("getValue"))){
			$.fool.alert({msg:'调整金额不能等于计划收付金额！'});
			return false;
		}
		$.ajax({
			url:getRootPath()+"/capitalPlanDetail/adjustedAmount",
			type:"POST",
			async:false,
			data:{id:$("#capitalId").val(),amount:$("#adjustAmount").textbox("getValue"),remark:$("#remarker").val(),updateTime:$("#capitalId").attr("updateTime")},
			success:function(data){
				if(data.returnCode =='0'){
					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
						if($("#capitalId").attr("listFlag")==1){
							$("#billList").trigger("reloadGrid");
							$("#poper-win").window("close");
						}else if($("#capitalId").attr("listFlag")==2){
							$("#goodsList").trigger("reloadGrid");
							$("#poper-win").window("close");
						}
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
}
//明细延期
function delayDetial(){
	if($("#delayDate").datebox("isValid")&&$("#remarker").validatebox("isValid")){
		var data=$("#capitalId").attr("data");
		var delayDate=$("#delayDate").datebox("getValue");
		var delayDateStr=delayDate.slice(0,4)+delayDate.slice(5,7)+delayDate.slice(8,10);
		var dataStr=data.slice(0,4)+data.slice(5,7)+data.slice(8,10);
		if(dataStr>=delayDateStr){
			$.fool.alert({msg:'延期日期必须晚于预计收付款日期！'});
			return false;
		}
		$.ajax({
			url:getRootPath()+"/capitalPlanDetail/delayDate",
			type:"POST",
			async:false,
			data:{id:$("#capitalId").val(),date:$("#delayDate").datebox("getValue"),remark:$("#remarker").val(),updateTime:$("#capitalId").attr("updateTime")},
			success:function(data){
				if(data.returnCode =='0'){
					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
						if($("#capitalId").attr("listFlag")==1){
							$("#billList").trigger("reloadGrid");
							$("#poper-win").window("close");
						}else if($("#capitalId").attr("listFlag")==2){
							$("#goodsList").trigger("reloadGrid");
							$("#poper-win").window("close");
						}
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
}
//明细拆分
function splitDetail(){
	if($("#splitAmount").textbox("isValid")&&$("#remarker").validatebox("isValid")){
		$.ajax({
			url:getRootPath()+"/capitalPlanDetail/splitDetail",
			type:"POST",
			async:false,
			data:{id:$("#capitalId").val(),amount:$("#splitAmount").textbox("getValue"),remark:$("#remarker").val(),updateTime:$("#capitalId").attr("updateTime")},
			success:function(data){
				if(data.returnCode =='0'){
					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
						if($("#capitalId").attr("listFlag")==1){
							$("#billList").trigger("reloadGrid");
							$("#poper-win").window("close");
						}else if($("#capitalId").attr("listFlag")==2){
							$("#goodsList").trigger("reloadGrid");
							$("#poper-win").window("close");
						}
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
}

//明细操作详情
function operateDetail(id){
	if(id){
		var row=$('#goodsList').getRowData(id);
		$("#de_badDebtDate").val(row.badDebtDate);
		$("#de_completeDate").val(row.completeDate);
		$("#de_createTime").val(row.createTime);
		$("#de_auditTime").val(row.auditTime);
		$("#de_cancelTime").val(row.cancelTime); 
		$("#de_badDebtName").val(row.badDebtName);
		$("#de_completeName").val(row.completeName);
		$("#de_creatorName").val(row.creatorName);
		$("#de_auditorName").val(row.auditorName);
		$("#de_cancelorName").val(row.cancelorName);
		$("#opWin").show();
		$("#opWin").window("open")
	}
}

function formChangeStatus(target,recordStatus,editType){
	if(!editType){
		editType=0;
	}
	target.attr("disabled","disabled");
	$.ajax({
		url:getRootPath()+"/capitalPlan/changeByCapitalId",
		type:"POST",
		async:false,
		data:{id:$("#id").val(),recordStatus:recordStatus,updateTime:updateTime.slice(0,19),editType:editType},
		success:function(data){
			if(data.returnCode =='0'){
				$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
					$("#billList").trigger("reloadGrid");
					viewRowNew($("#id").val(),recordStatus);
	    		}});
	    	}else if(data.returnCode == '1'){
	    		if(editType==1){
	    			$.fool.confirm({title:"确认",msg:data.message,fn:function(r){
	    				if(r){
	    					$.ajax({
			    				url:getRootPath()+"/capitalPlan/changeByCapitalId",
			    				type:"POST",
			    				async:false,
			    				data:{id:$("#id").val(),recordStatus:recordStatus,updateTime:updateTime.slice(0,19),editType:0},
			    				success:function(result){
			    					if(result.returnCode =='0'){
			    						$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
			    							$("#billList").trigger("reloadGrid");
			    							viewRowNew($("#id").val(),recordStatus);
			    			    		}});
			    			    	}else{
			    			    		$.fool.alert({msg:data.message});
			    			    	}
			    				}
			    			})
	    				}else{
	    					return false;
	    				}
	    			}});
	    		}else{
	    			$.fool.alert({msg:data.message});
	    		}
	    		return false;
			}else{
				$.fool.alert({msg:'服务器繁忙，请稍后再试！'});
	    		return false;
			}
	    }
	});
}

function detailChangeStatus(id,updateTime,recordStatus,flag,editType){
	if(!editType){
		editType=0;
	}
	$.ajax({
		url:getRootPath()+"/capitalPlanDetail/changeByCapitalId",
		type:"POST",
		async:false,
		data:{id:id,recordStatus:recordStatus,updateTime:updateTime,editType:editType},
		success:function(data){
			if(data.returnCode =='0'){
				$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
					if(flag==1){
						$("#billList").trigger("reloadGrid");
					}else if(flag==2){
						$("#goodsList").trigger("reloadGrid");
					}
	    		}});
	    	}else if(data.returnCode == '1'){
	    		if(editType==1){
	    			$.fool.confirm({title:"确认",msg:data.message,fn:function(r){
	    				if(r){
	    					$.ajax({
			    				url:getRootPath()+"/capitalPlanDetail/changeByCapitalId",
			    				type:"POST",
			    				async:false,
			    				data:{id:id,recordStatus:recordStatus,updateTime:updateTime,editType:0},
			    				success:function(result){
			    					if(result.returnCode =='0'){
			    						$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
			    							if(flag==1){
			    								$("#billList").trigger("reloadGrid");
			    							}else if(flag==2){
			    								$("#goodsList").trigger("reloadGrid");
			    							}
			    			    		}});
			    			    	}else{
			    			    		$.fool.alert({msg:data.message});
			    			    	}
			    				}
			    			})
	    				}else{
	    					return false;
	    				}
	    			}});
	    		}else{
	    			$.fool.alert({msg:data.message});
	    		}
	    		return false;
			}else{
				$.fool.alert({msg:'服务器繁忙，请稍后再试！'});
	    		return false;
			}
	    }
	});
}

function popWin(id,updateTime,showFlag,listFlag,data){
	$("#capitalId").val(id);
	$("#capitalId").attr("data",data);
	$("#capitalId").attr("listFlag",listFlag);
	$("#capitalId").attr("updateTime",updateTime);
	if(showFlag==1){
		$("#poper-win").window("setTitle","延期");
		$(".flag1").show();
		$(".flag2").hide();
		$(".flag3").hide();
	}else if(showFlag==2){
		$("#poper-win").window("setTitle","拆分");
		$(".flag1").hide();
		$(".flag2").show();
		$(".flag3").hide();
		if($("#capitalId").attr("data")>=0){
			$("#splitAmount").textbox({
				validType:'amount'
			});
		}else{
			$("#splitAmount").textbox({
				validType:'minus'
			});
		}
		$("#splitAmount").textbox("setValue",data);
	}else if(showFlag==3){
		$("#poper-win").window("setTitle","调整金额");
		$(".flag1").hide();
		$(".flag2").hide();
		$(".flag3").show();
	}
	$("#poper-win").window("open");
}