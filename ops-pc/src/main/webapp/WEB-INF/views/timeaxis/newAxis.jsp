<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<html>
<head>
    <link rel="stylesheet" href="${ctx}/resources/css/newTimeaxis.css"/>   
    <link rel="stylesheet" href="${ctx}/resources/css/jsgantt.css"/> 
    <script type="text/javascript" src="${ctx}/resources/js/jsgantt.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/raty/jquery.raty.min.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>
    <meta charset="utf-8">
    <title>时间轴</title>
    <style>
    /* .form1{padding:0} */
    /* .form1 p{margin-left:20px} */
    /* .form1 p font{width:auto} */
    </style>
</head>
<body>
    <div id="time-line">
      <div id="plan-title">
          <span></span>
      </div>
      <div id="plan-start"></div>
      <div id="line">
        <div id="planDetail"></div>
        <div class="form1" id="completePlanMsg" style="display:none">
          <p id="completePlanDes"><font style="vertical-align: sub;">完成信息描述：</font><textarea style="width:850px !important; border: none;outline:none;" readonly="readonly"></textarea></p>
        </div>
        <div id="add-task">
          <fool:planSecure secureCodes="FQR,TZR" planId="${plan.fid}"><a id="addBtn" href="javascript:;" title="新增事件"></a></fool:planSecure>
          <div id="taskDetail"></div>
        </div>
        <div id="task-list"></div>
        <div id="plan-end"></div>
      </div>
      <div id="btnDiv">
          <input id="costAnalysis" type="button" value="成本分析表" style="display: none;"/>
          <input id="flowAnalysis" type="button" value="流程汇总分析" style=""/>
          <input id="zjjh" type="button" value="资金计划" style="display: none;" onclick="zjjh_plan('${plan.fid}')"/>
      </div>
      <div id="rateBox">
        <div id="nowRate">
          <p class="text">实际月收益率</p>
          <p class="rate"></p>
        </div>
        <div id="norRate">
          <p class="text">预计月收益率</p>
          <p class="rate"></p>
        </div>
      </div>
    </div>
    <div id="other" style="display: none;">
      <div>
        <form id="costBillSearchForm" class="form1" style="padding:0">
          <p><font style="width:60px">日期：</font><input id="costBillDate" /></p>
          <p><font style="width:60px">货品：</font><input id="goodsSearcher" /></p>
          <p><font style="width:60px">发货地：</font><input id="deliveryPlaceSearcher" /></p>
          <p><font style="width:60px">收货地：</font><input id="receiptPlaceSearcher" /></p>
          <a id="goSearch" class="btn-blue2 btn-s">筛选</a>
          <a id="goClear" class="btn-blue2 btn-s">清空</a>
        </form>
      </div>
      <table id="costBillList"></table>
      <div id="costBillPager"></div>
      <p style="margin:10px;float:right"><a id="goodsWin" class="btn-blue2 btn-s">下一步</a></p>
    </div>
    <div id="nextWin" style="display: none;">
    <div id="poper" style="display: none;">
</body>
<script>
if("${plan.status}"=="100"){
	var makeRow = [];
	var today=new Date().format("yyyy-MM-dd");
	var planVo="";
	$("#costAnalysis").show();
	$("#zjjh").show();
}
var lsread="${param.lsread}";
$("#planDetail").load("${ctx}/timeAxis/planDetail",{id:"${plan.fid}"},function(){
	$("#task-list").load("${ctx}/timeAxis/taskList",{planId:"${plan.fid}",planStatus:"${plan.status}"});
	$("#taskDetail").load("${ctx}/timeAxis/addTask",{planId:"${plan.fid}",planStatus:"${plan.status}"});
});
if("${plan.fid}"){
	$("#rateBox").show();
	var effectiveYieldrate="";
	var estimatedYieldrate="";
	if("${plan.effectiveYieldrate}"){
		effectiveYieldrate="${plan.effectiveYieldrate}%";
	}else{
		effectiveYieldrate="0%";
	}
	if("${plan.estimatedYieldrate}"){
		estimatedYieldrate="${plan.estimatedYieldrate}%";
	}else{
		estimatedYieldrate="0%";
	}
	$("#nowRate .rate").text(effectiveYieldrate);
	$("#norRate .rate").text(estimatedYieldrate);
	
	if("${plan.status}"!="100"){
		$("#addBtn").hide();
	}else{
		$("#addBtn").show();
	}
	$("#plan-title span").text("${plan.name}");
}else{
	$("#addBtn").hide();
	$("#rateBox").hide();
}

$("#plan-title").click(function(){
	if("${plan.fid}"){
		$("#taskDetail").slideUp();
		$("#planSouth").slideUp();
		$("#ganttDetail").empty();
		$("#ganttDetail").slideUp();
		$("#planDetail").slideToggle("",function(){
			setNode();
		});
		$(".ganttBox_left").css("height",0);
		$(".ganttBox_right").css("height",0);
		if($("#editPlan").css("display")=="none"){
			exitEditPlan();
	    	initialPlanBtns("${plan.status}");
		}
	}
});
$("#addBtn").click(function(){
	if(!$("#taskDetail").html()){
		$("#taskDetail").load("${ctx}/timeAxis/addTask",{planId:"${plan.fid}",planStatus:"${plan.status}"},function(){
			$("#taskDetail").slideToggle("",function(){
				setNode();
			});
		});
	}else{
		$('#taskForm').form('disableValidation');
		$("#taskDetail").slideToggle("",function(){
			var inputs=$("#taskForm").find(".dhxDiv");
			for(var i=0;i<inputs.length;i++){
				inputs[i].comboObj.setComboText("");
			}
			setNode();
		});
	}
	$("#planDetail").slideUp();
	$("#ganttDetail").empty();
	$("#ganttDetail").slideUp();
	$(".ganttBox_left").css("height",0);
	$(".ganttBox_right").css("height",0);
});

function jumpPlanLevel(){
	var src="/flow/taskLevel/manage";
	var text='事件级别';
	parent.kk(src,text);
}

function jumpSecLevel(){
	var src="/flow/security/manage";
	var text='保密级别';
	parent.kk(src,text);
}

function jumpTaskType(){
	var src="/flow/taskType/manage";
	var text='事件分类';
	parent.kk(src,text);
}

$.extend($.fn.validatebox.defaults.rules, {
	taskSecLevel:{
        validator: function (value, param) {
        	var secLevelId=$(this).parent().prev().combobox("getValue");
        	var secLevel="";
        	var parentSecLevel="";
        	if(secLevelId){
        		$.ajax({
	        		url:"${ctx}/flow/security/queryById",
	        		async:false,
	        		data:{fid:secLevelId},
	        		success:function(data){
	        			secLevel=data.level;
	        		}
	        	});
        	}
        	if(param[0]){
        		$.ajax({
	        		url:"${ctx}/flow/security/queryById",
	        		async:false,
	        		data:{fid:param[0]},
	        		success:function(data){
	        			parentSecLevel=data.level;
	        		}
	        	});
        	}else{
        		return true;
        	}
        	if(parentSecLevel!==""){
        		if(parentSecLevel<secLevel){
        			return false;
        		}else{
        			return true;
        		}
        	}else{
        		return true;
        	}
        },
        message:'当前事件的保密级别必须低于上级事件的保密级别'
	},
	checkSecurityLevel:{
        validator: function (value, param) {
        	var securityLevelId=$("#securityLevelId_task").combobox("getValue");
        	var userId=$("#undertakerId_task").val();
        	var undertakerLevelId="";
        	var securityLevel="";
        	var undertakerLevel="";
        	if(userId){
        		$.ajax({
	        		url:"${ctx}/userController/getById",
	        		async:false,
	        		data:{fid:userId},
	        		success:function(data){
	        			undertakerLevelId=data.securityLevelId;
	        		}
	        	});
        	}
        	if(undertakerLevelId){
        		$.ajax({
	        		url:"${ctx}/flow/security/queryById",
	        		async:false,
	        		data:{fid:undertakerLevelId},
	        		success:function(data){
	        			undertakerLevel=data.level;
	        		}
	        	});
        	}
        	if(securityLevelId){
        		$.ajax({
	        		url:"${ctx}/flow/security/queryById",
	        		async:false,
	        		data:{fid:securityLevelId},
	        		success:function(data){
	        			securityLevel=data.level;
	        		}
	        	});
        	}else{
        		return true;
        	}
        	if(securityLevel!==""&&undertakerLevel!==""){
        		if(securityLevel>undertakerLevel){
            		return false;
            	}else{
            		return true;
            	}
        	}else{
        		return true;
        	}
        },
        message:'承办人的保密级别必须高于事件的保密级别'
	},
	compareDate:{
        validator: function (value, param) {
        	var start=$(param[0]).datebox("getValue");
        	var startStr=start.split("-");
			var endStr=value.split("-");
			var startStrNum=parseInt(startStr[0]+startStr[1]+startStr[2]);
			var endStrNum=parseInt(endStr[0]+endStr[1]+endStr[2]);
			if(startStrNum>endStrNum){
				return false;
			}else{
				return true;
			}
        },
        message:'结束时间不能早于开始时间'
	},
	delayDate:{
        validator: function (value, param) {
    		var end=$("#antipateEndTime_task").datebox("getValue");
    		var endStr=end.split("-");
    		var date=value.split("-");
			var endStrNum=parseInt(endStr[0]+parseInt(endStr[1]).toString()+endStr[2]);
			var dateStrNum=parseInt(date[0]+parseInt(date[1]).toString()+date[2]);
	    		if(endStrNum>=dateStrNum){
					return false;
				}else{
					return true;
				}
        },
        message:'延迟日期必须大于本事件的预计结束日期'
	},
});

function ajaxUpload(btn, fileName, _data,className){
	return new AjaxUpload(btn, {
		action: '${ctx}/flow/task/upload',
		data:_data,
		name:"file",
		autoSubmit:true,
		responseType: "json",
        onChange: function(file, extension){
        	fileName.val(file);
        },
        onSubmit:function(file,ext,size){
        	if(size>10240000){
        		$.fool.alert({msg:'文件大小不能超过10M。'});
                fileName.val("");
        		return false;
        	}
        	if(file.length>100){
        		$.fool.alert({msg:'文件名称超长，请限制在100个字符以内。'});
                fileName.val("");
                return false;
        	}
        	if (!(ext && /^(jpeg|bmp|jpg|png|rar|txt|zip|doc|ppt|xls|pdf|docx|xlsx|JPEG|BMP|JPG|PNG|RAR|TXT|ZIP|DOC|PPT|XLS|PDF|DOCX|XLSX)$/.test(ext))) {  
                $.fool.alert({msg:'您上传的图片格式不对，仅能上<br/>传pdf,jpeg,jpg,png,bmp,ppt,xls,xlsx,doc,docx,zip,rar,txt'});
                fileName.val("");
                return false;  
            }
        	fileName.val("正在上传...");
        	fileName.attr('disabled',true);
        },
        onComplete:function(file,response){
        	if(response.returnCode==0){
        		fileName.val(file);
        		fileName.attr("id",response.data.attachId);
        	}else if(response.returnCode==1){
        		$.fool.alert({msg:response.message});
        		fileName.val("");
        		fileName.attr('disabled',false);
        	}else{
        		fileName.val("");
        		fileName.attr('disabled',false);
        	}
        }
	},className);
}

function clearSession(){
	$.ajax({
		url:"${ctx}/flow/task/cleanSession",
		data:{},
		success:function(data){}
	});
}

function priceFormat(value){
	if(isNaN(value)){
		return 0;
	}else {
		return value;
	}
}

//设置路径模板
function checkTemp(ids){
	var a = false;
	$.ajax({
		url:getRootPath()+'/flow/planTemplateRelation/settleAllTemplate',
		data:{ids:ids},
		type:"post",
		async:false,
		success:function(data){
			if(data.returnCode == 1){
				if(data.errorCode=="301"){
					var mydata = data.data;
					var type="";
					for(var i in mydata){
						if(mydata[i].type==1){
							type="采购";
						}else if(mydata[i].type==2){
							type="运输";
						}else if(mydata[i].type==3){
							type="销售";
						}
						$.fool.alert({msg:mydata[i].deliveryPlace+"-"+mydata[i].receiptPlace+mydata[i].goodsName+mydata[i].specName+"找不到"+type+"模板，请先新增。",fn:function(){
							var url = "/flow/planTemplate/manage?openAdder=1&templateType="+mydata[i].type+"&dataId="+mydata[i].dataId;
							parent.kk(url,"计划模板");
						}});
					}
					
				}else{
					$.fool.alert({msg:data.message});
				}
			}else{
				a = true;
			}
		},
		error:function(){
			$.fool.alert({msg:"服务器繁忙，请稍后再试！"});
		}
	});
	return a;
}

$("#costAnalysis").click(function(){
	if($('#goodsSearcher').next().length<1){
		$('#costBillDate').fool("datebox",{
			width:120,
			height:30,
			inputDate:true,
			focusShow:true,
			required:false,
			prompt:'选择日期',
			disabled:true,
			value:new Date().format("yyyy-MM-dd")
		});
		$('#deliveryPlaceSearcher').combotree({
			prompt:'选择地址',
			width:120,
			height:30,
			method:"get",
			url:getRootPath()+"/api/freightAddress/findAddressTree?enable=1&num="+Math.random(),
			editable:false,
		});
		$('#receiptPlaceSearcher').combotree({
			prompt:'选择地址',
	        novalidate:true,
			width:120,
			height:30,
			method:"get",
			url:getRootPath()+"/api/freightAddress/findAddressTree?enable=1&num="+Math.random(),
			editable:false,
			required:true
		});
		$('#goodsSearcher').fool('dhxComboGrid',{
	    	width:120,
	    	height:30,
	    	prompt:"搜索选择货品",
	    	focusShow:true,
	    	filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=8',
	    	setTemplate:{
	    		input:"#name#",
	    		columns:[
	    					{option:'#code#',header:'编号',width:100},
	    					{option:'#name#',header:'名称',width:100},
	    					{option:'#barCode#',header:'条码',width:100},
	    					{option:'#spec#',header:'规格',width:100},
	    					{option:'#unitName#',header:'单位',width:100},
	    				],
	    	},
	        toolsBar:{
	            name:"货品",
	            addUrl:"/goods/manage",
	            refresh:true
	        },
	        data:getComboData(getRootPath()+'/goods/vagueSearch?searchSize=6'),
	    });
		$('#other').window({
			top:50,
			modal:true,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			closed:true,
			width:1000,
			height:600,
			title:"成本分析表"
		}); 
		$("#other").show();
		$("#costBillList").jqGrid({	
			width:$('#other').width()-2,
			height:380,
			pager:'#costBillPager',
			rowNum:10,
			rowList:[10,20,30],
			viewrecords:true,
			multiselect:true,
			jsonReader:{
				records:"total",
				total: "totalpages",  
			},
			colModel:[
				       {name : 'goodsName',label : '货品',sortable:false,align:'center',width:"100px",frozen:true},
				       {name : 'goodsSpecName',label : '货品属性',sortable:false,align:'center',width:"100px",frozen:true},
				       {name : 'deliveryPlaceName',label : '发货地',sortable:false,align:'center',width:"100px",frozen:true},
				       {name : 'receiptPlaceName',label : '收货地',sortable:false,align:'center',width:"100px",frozen:true},
				       {name : 'id',label : 'id',hidden:true,frozen:true},
				       {name : 'unitName',label : '货品单位',sortable:false,align:'center',width:"100px"},
				       {name : 'factoryPrice',label : '出厂价',sortable:false,align:'center',width:"100px",formatter:priceFormat},
				       {name : 'freightPrice',label : '运输费用',sortable:false,align:'center',width:"100px",formatter:priceFormat},
				       {name : 'totalPrice',label : '总价',sortable:false,align:'center',width:"100px",formatter:priceFormat},
				       {name : 'expectedDays',label : '预计时间',sortable:false,align:'center',width:"100px"},
				       {name : 'publishFactoryPrice',label : '对外出厂价',sortable:false,align:'center',width:"100px",formatter:priceFormat},
				       {name : 'publishFreightPrice',label : '对外运费',sortable:false,align:'center',width:"100px",formatter:priceFormat},
				       {name : 'publishTotalPrice',label : '对外总价',sortable:false,align:'center',width:"100px",formatter:priceFormat},
				       {name : 'remark',label : '备注',sortable:false,align:'center',width:"100px"},
			      ],
		}).navGrid('#costBillPager',{add:false,del:false,edit:false,search:false,view:false})/* .jqGrid('setFrozenColumns') */;
	}
	$('#other').window("open");
})
$("#goodsWin").click(function(){
	var selections=$("#costBillList").jqGrid("getGridParam", "selarrrow");
	if(selections.length==0){
		$.fool.alert({msg:"请先选择记录！"});
		return false;
	}
	makeRow=[];
	var ids = "";
	for(var i=0;i<selections.length;i++){
		var row = $('#costBillList').jqGrid("getRowData",selections[i]);
		ids = row.id + ",";
		makeRow.push(row);
	}
	var a = checkTemp(ids);
	if(!a){
		return false;
	}
	exitEditPlan();
	planVo=$('#planForm').serialize();
	$('#other').window("close");
	$("#nextWin").show();
	$('#nextWin').window({
		href:getRootPath()+'/costAnalysisBill/process?type=1&flag=1',
		top:50,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		closed:true,
		width:$("#other").width()*0.984,
		height:380,
		title:"生成流程"
	}); 
	$('#nextWin').window("open");
})

$("#goSearch").click(function(){
	$('#costBillSearchForm').form('enableValidation');
	if ($('#costBillSearchForm').form('validate')) {
		var billDate=$('#costBillDate').datebox("getValue");
		var deliveryPlaceId=$('#deliveryPlaceSearcher').combotree("getValue");
		var receiptPlaceId=$('#receiptPlaceSearcher').combotree("getValue");
		var goodsId=($('#goodsSearcher').next())[0].comboObj.getSelectedValue();
		/* $("#costBillList").jqGrid('setGridParam',{postData:{billDate:billDate,deliveryPlaceId:deliveryPlaceId,receiptPlaceId:receiptPlaceId,goodsId:goodsId}}).trigger("reloadGrid"); */
		$('#costBillList').jqGrid("setGridParam",{
			datatype:function(postdata){
				postdata.billDate=billDate;
				postdata.deliveryPlaceId=deliveryPlaceId;
				postdata.receiptPlaceId=receiptPlaceId;
				postdata.goodsId=goodsId;
				$.ajax({
					url: '${ctx}/api/costAnalysisBill/query',
					type:"get",
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
			        		data.responseJSON.content.total = data.responseJSON.totalElements;
			        		data.responseJSON.content.totalpages=Math.ceil(data.responseJSON.totalElements/postdata.rows);
			        		$("#costBillList")[0].addJSONData(data.responseJSON.content);
			        	}
			        }
				});
			},
		}).trigger("reloadGrid");
	}
})
$("#goClear").click(function(){
	$("#costBillSearchForm").form("clear");
	var inputs=$("#costBillSearchForm").find(".dhxDiv");
	for(var i=0;i<inputs.length;i++){
		inputs[i].comboObj.setComboText("");
	}
})

$("#flowAnalysis").click(function(){
	var src="/report/SysRepor/manage?reportId=402881f85aa1c3d3015aa2546f310001&planId=${plan.fid}";
	var text='流程汇总';
	parent.kk(src,text);
});

function zjjh_plan(id){
	$("#poper").show();
	$("#poper").fool('window',{modal:true,'title':"资金计划",height:480,width:780,href:getRootPath()+'/capitalPlanDetail/window?relationId='+id+'&relationSign=71'});
}
</script>
</html>