<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<body>
<style>
#billDetail-win #addBox{
	background:#F0F0F0;
}
#billDetail-win .myform{
	/* background:none; */background: #F0F0F0;
}
#billDetail-win .myform p {
    display: inline-block;
    padding: 0px;
    margin: 2px 0;
    font-size: 14px;
	font-family: 宋体;
	width:1050px;
}
#billDetail-win .myform p em{
    color:red;
}
#billDetail-win .myform p font {
    width: 150px;
    height: 27px;
    line-height: 30px;
    text-align: right;
    display: inline-block;
    margin: 0px;
    padding: 0px;
}
#billDetail-win .myform .textBox,#billDetail-win .myform .dateBox,#billDetail-win .myform .textBox-mid{
    width:180px;
    height:27px;
    border: 1px solid #d0d9e7;
    border-radius:2px;
}
#billDetail-win .bill-form{
	padding:10px 0;
}
#billDetail-win .shadow{
	background:#fff;
	padding:5px 0;
	margin:0px 20px 10px 20px;
	-moz-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	-o-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	-ms-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	box-shadow: 0 2px 7px rgba(0,0,0,0.3);
}
#billDetail-win .bill-form h1{
	font:800 16px 宋体  !important;
	color:#50b3e7;
	margin-left:60px;
	height:18px;
}
#billDetail-win .bill-form h2{
	font:800 14px 宋体  !important;
	color:#ffc96c;
	margin-left:10px;
	display: inline-block;
}
#billDetail-win .bill-form .billTitle{
	padding:5px 0;
	margin-left:20px;
}
#billDetail-win .myTitle{
	margin-bottom: -30px;
}
#billDetail-win .in-box{
	margin:5px 10px 10px 20px;
	overflow-y:auto;
	height:170px;
}
#billDetail-win .mybtn-footer{
	box-shadow: 0 -2px 7px rgba(0,0,0,0.3);
	-moz-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	-o-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	-ms-box-shadow: 0 2px 7px rgba(0,0,0,0.3);
	padding: 5px 0 20px 0;
	/* position: fixed; */position: absolute;
	bottom: 0px;
	width: 100%;
	text-align:right;
	height:40px;
	background:#F0F0F0;
}
#billDetail-win .mybtn-footer a{
	margin-right:20px;
}
#billDetail-win .mybtn-footer input{
	width:56px;
	margin-right:20px;
}
#billDetail-win .mybtn-blue {
    background: #50b3e7;
    border-color: #50b3e7;
}
#billDetail-win .mybtn-s {
	border-radius:3px;
    font-size: 16px;
    min-width: 56px;
    height: 33px;
    padding: 1px 10px;
    font-weight: bolder !important;
    line-height: 33px;
}
#billDetail-win .in-box p{
	text-align:left;
}
#billDetail-win .scroll1{width: 100%; height: 20px; /* position: fixed; */ position: absolute; bottom:0;  background: #CFCFCF;z-index:10000;}
#billDetail-win .scroll2{width: 20%; height: 20px; /* position: fixed; */position: absolute;background: #A1A1A1; margin-bottom: 25px;z-index:10000;}


#billDetail-win #dataBox .datagrid-body {
    margin: 0;
    padding: 0;
    overflow: hidden;
    zoom: 1;
}
#billDetail-win .fixed{
	position:fixed;
	border-width:1px 0;
	z-index:8999;
}
#billDetail-win #dataBox .in-box{
	overflow-y: inherit;
	overflow-x: hidden;
	height:auto;
}
#billDetail-win #dataBox{
	margin-bottom:50px;
}
#billDetail-win #bill .in-box{
	text-align:center;
	overflow-y: inherit;
	overflow-x: hidden;
	height:auto;
}
#billDetail-win #bill{
	margin-top: 24px !important;
}
#billDetail-win .billTitle #square2{
	background:#ffc96c;
	width:5px;
	height:24px;
	position: relative;
    margin-top: -5px;
    display: inline-block;
    vertical-align: middle;
}
#billDetail-win .bill-form #title{
	/* position:fixed; */
	position: absolute;
	top:0px;
	background:#F0F0F0;
	width:100%; /*写给不兼容的浏览器*/
	width:calc(100% - 40px);
	width:-moz-calc(100% - 40px);
	width:-webkit-calc(100% - 40px);
	padding:0 20px;
	z-index:8999;
}
#billDetail-win #title #square1{
	background:#50b3e7;
	width:30px;
	height:40px;
	position:absolute;
	margin-top:-11px;
}
#billDetail-win #square1 #triangle{
	width: 0;
	height: 0;
	border-top: 5px solid transparent;
	border-left: 10px solid #50b3e7;
	border-bottom: 5px solid transparent;
	margin-left:30px;
	margin-top:15px;
}
#billDetail-win .backBtn{
    width:100%;
    height:100%;
    position:absolute;
    background-image: url("/ops-pc/resources/images/back_img.png");
    background-repeat: no-repeat;
    background-position: center;
    cursor:pointer
}
#billDetail-win .repertory{
	height: 50px; 
	position:fixed;
	bottom: 0px; 
	left: 0px; 
	width:99%;
	word-wrap: break-word;
	font-size: 12px;
	line-height: 25px;
	 }
#billDetail-win .repertory ul {
	margin-left: 10px; 
    overflow: hidden; 
    display:inline-block;
}
#billDetail-win .repertory ul li{
	float: left;
}
#billDetail-win .number{ color: red;}
#billDetail-win .inlist{
    padding-top: 30px;
}
#billDetail-win .status{
	background: none !important;
}
/*未审核*/
#billDetail-win .status0{
	background: url("/ops-pc/resources/images/billstatus-0.png") no-repeat 390px 0%;
}
/*已审核*/
#billDetail-win .status1{
	background: url("/ops-pc/resources/images/billstatus-1.png") no-repeat 390px 0%;
}
/*已作废*/
#billDetail-win .status2{
	background: url("/ops-pc/resources/images/billstatus-2.png") no-repeat 390px 0%;
}
#gbox_relationList{
    display: inline-block;
    vertical-align: text-top;
}
.validatebox-invalid{
    border-color: #ffa8a8 !important;
    background-color: #FFE1E1 !important;
}
.form p textarea, .form1 p textarea {
    width:676px !important;
    height:60px !important;
}
</style>
    <script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/warehouseEdit2.js?v=${js_v}"></script>
        <div id="taskDetail_layout">
          <div id="taskCenter">
            <form class="form1" id="taskForm">
            <input class="textBox" id="name_task" name="name" value="${task.name}"/>
              <hr/>
              <input id="fid_task" name="fid" type="hidden" value="${task.fid}"/>
              <input id="level_task" name="level" type="hidden" value="${task.level}"/>
              <input id="hasChilds_task" name="hasChilds" type="hidden" value="${task.hasChilds}"/>
              <input id="assignFlag_task" name="assignFlag" type="hidden" value="${task.assignFlag}"/>
              <input id="billId_task" name="billId" type="hidden" value="${task.billId}"/>
              <input id="startMin_task" name="" type="hidden" value="${task.startMin}"/>
              <input id="startMax_task" name="" type="hidden" value="${task.startMax}"/>
              <input id="endMin_task" name="" type="hidden" value="${task.endMin}"/>
              <input id="endMax_task" name="" type="hidden" value="${task.endMax}"/>
              <input id="planId_task" name="planId" type="hidden" value=""/>
              <input id="parentId_task" name="parentId" type="hidden" value=""/>
              <input id="initiaterId_task" name="initiaterId" type="hidden" value="${task.initiaterId}"/>
              <input id="principalerId_task" name="principalerId" type="hidden" value="${task.principalerId}"/>
              <input id="principalerDeptId_task" name="deptId" type="hidden" value="${task.deptId}"/>
              <input id="auditerId_task" name="auditerId" type="hidden" value="${task.auditerId}"/>
              <input id="undertakerId_task" name="undertakerId" type="hidden" value="${task.undertakerId}"/>
              <p><font>状态：</font><input class="textBox" id="status_task" readonly="readonly"/></p>
              <p><font>事件类别：</font><input class="textBox" id="taskTypeId_task" name="taskTypeId" value="${task.taskTypeId}"/></p>
              <p class="hideP"><font>事件模板：</font><input class="textBox" id="taskTemp_task" name="taskTemp" value=""/></p>
              <p><font><em>*</em>事件编号：</font><input class="textBox" id="code_task" name="code" value="${task.code}"/></p>
              <p><font><em>*</em>预计开始时间：</font><input class="textBox" id="antipateStartTime_task" name="antipateStartTime"/></p>
              <p><font><em>*</em>预计结束时间：</font><input class="textBox" id="antipateEndTime_task" name="antipateEndTime"/></p>
              <p><font>实际开始时间：</font><input class="textBox" id="actualStartTime_task" readonly="readonly"/></p>
              <p><font>实际结束时间：</font><input class="textBox" id="actualEndTime_task" readonly="readonly"/></p>
              <p><font><em>*</em>事件级别：</font><input class="textBox" id="taskLevelId_task" name="taskLevelId" value="${task.taskLevelId}"/></p>
              <p><font><em>*</em>保密级别：</font><input class="textBox" id="securityLevelId_task" name="securityLevelId" value="${task.securityLevelId}"/></p>
              <p><font>预计金额：</font><input class="textBox" id="amount_task" name="amount" value="${task.amount}"/></p>
              <!-- <p><font>实际金额：</font><input class="textBox" id="realAmount_task" name="realAmount" readonly="readonly"/></p> -->
              <p><font><em>*</em>承办人：</font><input class="textBox" id="undertakerName_task" name="undertakerName" value="${task.undertakerName}"/></p>
              <p><font><em>*</em>责任人：</font><input class="textBox" id="principalerName_task" name="principalerName" value="${task.principalerName}"/></p>
              <p><font>责任部门：</font><input class="textBox" id="principalerDeptName_task" name=deptName value="${task.deptName}" readonly="readonly"/></p>
              <p><font>通知方式：</font><input type="checkbox" id="sendEmail_task" name="sendEmail" value="1">邮件 <input type="checkbox" id="sendPhoneMsg_task" name="sendPhoneMsg" value="1">短信</p>
              <br/><p><font style="vertical-align: top;">描述：</font><input class="textBox" id="describe_task" name="describe" value="${task.describe}"/></p> 
              <br/><p><font>附件：</font><div id="attachText_task"><span id="addAttach_task" title="添加附件,附件格式须为：pdf,jpeg,jpg,png,bmp,ppt,xls,xlsx,doc,docx,zip,rar,txt"></span></div></p>
              <br/><p><font>关联单据：</font><table id="relationList"></table></p>
              <hr/>
              <div id="taskBtn"> 
                <input id="cancelTask" type="button" class="btn-green4 btn-xs" style="display: none;" value="取消"/>
                <input id="saveTask" type="button" class="btn-orange3 btn-xs" style="display: none;" value="保存"/>
                <fool:flowTag planId="${task.planId}" taskId="${task.fid}" type="task"></fool:flowTag>
              </div>
            </form>
          </div>  
          <div id="taskSouth"></div>   
        </div>
        <div id="pop-win">
          <p style="margin:10px 20px"><input id="billType"/></p>
          <table id="billList"></table>
          <div id="billListPage"></div>
          <a style="float:right;margin:10px 20px" id="addRelations" class="btn-blue2 btn-s" onclick="addRelations()">添加</a>
        </div>
        <div id="billDetail-win"></div> 
<script>
var parentAssign="";
var billType=[
              {value:"91",text:"期初库存",billType:"qckc"},
              {value:"92",text:"期初应付",billType:"qcyf"},
              {value:"93",text:"期初应收",billType:"qcys"},
              {value:"10",text:"采购订单",billType:"cgdd"},
              {value:"11",text:"采购入库",billType:"cgrk"},
              {value:"12",text:"采购退货",billType:"cgth"},
              {value:"13",text:"采购询价单",billType:"cgxjd"},
              {value:"14",text:"采购申请单",billType:"cgsqd"},
              {value:"15",text:"采购发票",billType:"cgfp"},
              {value:"20",text:"盘点单",billType:"pdd"},
              {value:"21",text:"调仓单",billType:"dcd"},
              {value:"22",text:"报损单",billType:"bsd"},
              {value:"30",text:"生产领料",billType:"scll"},
              {value:"31",text:"成品入库",billType:"cprk"},
              {value:"32",text:"生产退料",billType:"sctl"},
              {value:"33",text:"成品退库",billType:"cptk"},
              {value:"40",text:"销售订单",billType:"xsdd"},
              {value:"41",text:"销售出货",billType:"xsch"},
              {value:"42",text:"销售退货",billType:"xsth"},
              {value:"43",text:"销售报价单",billType:"xsbjd"},
              {value:"44",text:"销售发票",billType:"xsfp"},
              {value:"34",text:"生产计划单",billType:"scjhd"},
              {value:"54",text:"费用申请单",billType:"fysqd"},
              {value:"55",text:"采购返利单",billType:"cgfld"},
              {value:"56",text:"销售返利单",billType:"xsfld"},
              {value:"53",text:"费用单",billType:"fyd"},
              {value:"51",text:"收款单",billType:"skd"},
              {value:"52",text:"付款单",billType:"fkd"},
              {value:"23",text:"发货单",billType:"fhd"},
              {value:"24",text:"收货单",billType:"shd"},
              ];
var _billCode="";
var minTaskStart=$("#startMax_task").val();
var maxTaskEnd=$("#endMin_task").val();
var gParentStart=$("#startMin_task").val();
var gParentEnd=$("#endMax_task").val();
var contentStart="";
var contentEnd="";
/* var parentSecLevelId=""; */

if("${task.planId}"){
	$("#planId_task").val("${task.planId}");
}else{
	$("#planId_task").val("${param.planId}");
}
if("${task.parentId}"){
	$("#parentId_task").val("${task.parentId}");
}else{
	$("#parentId_task").val("${param.parentId}");
}
if("${task.planId}"){
	$("#planId_task").val("${task.planId}");
}else{
	$("#planId_task").val("${param.planId}");
}
/* if($("#planId_task").val()){
	minTaskStart=getTaskMinStart($("#planId_task").val());
	maxTaskEnd=getTaskMaxEnd($("#planId_task").val());
} */
$.ajax({
	url:"${ctx}/flow/task/showAttach?num="+Math.random(),
	async:false,
	data:{taskId:"${task.fid}"},
	success:function(data){
		for(var i=0;i<data.length;i++){
			$("#addAttach_task").before("<input class='attachName_task' id='"+data[i].id+"' readonly='readonly' name='file' value='"+data[i].name+"' disabled='disabled' saved='true' /><a class='delAttach_task'>x</a>");
			ajaxUpload($("#addAttach_task").prev().prev(),$("#addAttach_task").prev().prev(),{triggerType:"plan40"});
		}
	}
});

if(!$("#fid_task").val()&&$("#parentId_task").val()){
	$.ajax({
		url:"${ctx}/flow/task/getById",
		async:false,
		data:{fid:$("#parentId_task").val()},
		success:function(data){
			if(data){
				if(data.antipateStartTime){
					gParentStart=(data.antipateStartTime).slice(0,10);
				}
				if(data.antipateEndTime){
					gParentEnd=(data.antipateEndTime).slice(0,10);
				}
				/* if(data.securityLevelId){
					parentSecLevelId=data.securityLevelId;
				} */
			}
		}
	});
}
if(minTaskStart){
	contentStart="开始日期必须早于"+minTaskStart;
	if(gParentStart){
		contentStart=contentStart+",晚于"+gParentStart;
	}
}else{
	if(gParentStart){
		contentStart="开始日期必须晚于"+gParentStart;
	}
}
if(maxTaskEnd){
	contentEnd="结束日期必须晚于"+maxTaskEnd;
	if(gParentEnd){
		contentEnd=contentEnd+",早于"+gParentEnd;
	}
}else{
	if(gParentEnd){
		contentEnd="结束日期必须早于"+gParentEnd;
	}
}
var taskTemp="";
$.ajax({
	url:"${ctx}/flow/taskTemplate/queryAll",
	async:false,
	data:{},
	success:function(data){
		data.unshift({fid:null,name:"请选择"});
		taskTemp=data;
	}
});
if("${task.fid}"){
	$.ajax({
		url:"${ctx}/flow/task/getById",
		async:false,
		data:{fid:$("#parentId_task").val()},
		success:function(data){
			if(data){
				parentAssign=data.assignFlag;
			}
		}
	});
	$("#relationList").jqGrid({	
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/flow/taskBill/queryByTaskId?queryByTaskId=${task.fid}',
				type:"get",
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		$("#relationList")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		width:"auto",
		height:200,
		viewrecords:true,
		/* multiselect:true, */
		footerrow: true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},
		colModel:[
			       {name : 'billId',label : 'billId',hidden:true,frozen:true},
			       {name : 'billSign',label : 'billSign',hidden:true,frozen:true},
			       {name : 'billName',label : '单据编号',sortable:false,align:'center',width:"200px",formatter:function(cellvalue, options, rowObject){
			    	   for(var i=0; i<billType.length; i++){
			    			if (billType[i].value == rowObject.billSign){
			    				return "<a href='javascript:;' onClick='openBill(\""+billType[i].billType+"\",\""+rowObject.billId+"\",\""+billType[i].text+"\")'>"+cellvalue+"</a>";
			    			}
			    		}
			    		return cellvalue; 
			       }},
			       {name : 'billType',label : '单据类型',sortable:false,align:'center',width:"200px",formatter:function(cellvalue, options, rowObject){
			    	   for(var i=0; i<billType.length; i++){
			    			if (billType[i].value == rowObject.billSign) return billType[i].text;
			    		}
			    		return rowObject.billSign; 
			       }},
			       {name : 'totalAmount',label : '金额',sortable:false,align:'center',width:"200px"},
			       {name : 'action',label : '操作',sortable:false,align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
			    	   if(!cellvalue){
			    		   return "<a href='javascript:;' onClick='delRelations(\""+options.rowId+"\",\""+rowObject.fid+"\")' class='btn-del' title='删除'></a>";
			    	   }else{
			    		   return cellvalue
			    	   }
			       }},
		      ],
	});
	$("#relationList").jqGrid("footerData","set",{"action":"<a href='javascript:;' id='openBillWin' onclick='openBillWin()'>关联单据</a>"});
}
if("${task.sendEmail}"==1){
	$("#sendEmail_task").attr("checked","checked");
}
if("${task.sendPhoneMsg}"==1){
	$("#sendPhoneMsg_task").attr("checked","checked");
}

var taskTypeIdValue='';
$.ajax({
  url:"${ctx}/flow/taskType/queryAll",
  async:false,		
  success:function(data){
	  taskTypeIdValue=formatData(data,'fid');	
  }
});
var taskTempValue='';
$.ajax({
  url:"${ctx}/flow/taskTemplate/queryAll",
  async:false,		
  success:function(data){
	  taskTempValue=formatData(data,'fid');	
  }
});
var taskTypeIdName= $("#taskTypeId_task").fool("dhxCombo",{
  width:130,
  height:32,
  data:taskTypeIdValue,
  clearOpt:false,
  setTemplate:{
	  input:"#name#",
	  option:"#name#"
  },
  toolsBar:{
      name:"事件类别",
      addUrl:"/flow/taskType/manage",
      refresh:true
  },
  editable:false,
  focusShow:true,
  onChange:function(value,text){
	  var record = taskTypeIdName.getSelectedText();//获取选中行数据
	  if(record.fid===null){
		  taskTypeIdName.setComboText("");
		  taskTypeIdName.setComboValue("");
		}
	  $.ajax({
			url:"${ctx}/flow/taskTemplate/queryAll?taskTypeId="+record.fid,
			async:false,		
			success:function(data){	
				taskTempValue=formatData(data,'fid');
				($("#taskTemp_task").next())[0].comboObj.clearAll(true);
				($("#taskTemp_task").next())[0].comboObj.addOption(taskTempValue);
		    }
	  });
  },
  onLoadSuccess:function(value,text){
		if(taskTypeIdValue.length<=0){
			/* ($("#taskTypeId_task").next())[0].comboObj.setComboText("<a onclick='jumpTaskType()'>暂无数据，请先添加数据</a>");
			($("#taskTypeId_task").next())[0].comboObj.setComboValue(""); */
		} 
  }
});	 
	var taskTempName= $("#taskTemp_task").fool("dhxCombo",{
		  width:130,
		  height:32,
		  data:taskTempValue,
        clearOpt:false,
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
        toolsBar:{
            name:"事件模板",
            addUrl:"/flow/taskTemplate/manage",
            refresh:true
        },
		  editable:false,
		  focusShow:true,
		  onChange:function(record){
			  var record = taskTempName.getSelectedText();//获取选中行数据
	    	  if(record.fid){
	    		  $("#taskTemp_task").attr("endDays",record.endDays);
	        	  /* $("#name_task").textbox("setValue",record.name); */
	        	 /*  $("#code_task").textbox("setValue",record.code); */
	        	 ($("#taskLevelId_task").next())[0].comboObj.setComboValue(record.taskLevelId);
	        	 ($("#taskTypeId_task").next())[0].comboObj.setComboValue(record.taskTypeId);
				  /* taskLeveName.setComboValue(record.taskLevelId);	        		
				  taskTypeIdName.setComboValue(record.taskTypeId); */
	    	  }
		  }
	});

	$("#name_task").textbox({
		required:true,
    	novalidate:true,
    	missingMessage:"该项必填",
    	width:160,
    	height:30,
    	prompt:"请输入事件名称",
    	validType:['maxLength[50]','isBank']
	});
	$("#name_task").textbox("textbox").css("cssText","color:#00A8FF;margin-left:20px;font-size: 16px;font-weight: bold");
	$("#code_task").textbox({
		required:true,
    	novalidate:true,
    	missingMessage:"该项必填",
    	width:130,
    	height:30,
    	validType:['maxLength[50]','isBank']
	});
	$("#antipateStartTime_task").datebox({
		required:true,
    	novalidate:true,
    	missingMessage:"该项必填",
		editable:false,
    	width:130,
    	height:30,
    	value:"${task.antipateStartTime}",
    	onSelect:function(){
    		var result="";
    		if($("#parentId_task").val()){
    			result=checkTime($("#parentId_task").val(),$("#antipateStartTime_task").datebox("getValue"),"","task");
    		}else{
    			result=checkTime($("#planId_task").val(),$("#antipateStartTime_task").datebox("getValue"),"","plan");
    		}
    		
    		var date=$("#antipateStartTime_task").datebox("getValue");
    		var dateStr=date.split("-");
    		var dateNum=dateStr[0]+dateStr[1]+dateStr[2];
    		var minStartStr="";
    		var minStartNum="";
    		if(minTaskStart){
    			minStartStr=minTaskStart.split("-");
    			minStartNum=minStartStr[0]+minStartStr[1]+minStartStr[2];
    		}
    		
    		if(minStartNum&&dateNum>minStartNum){
    			result=false;
    		}
    		
    		if(!result){
    			$("#antipateStartTime_task").datebox("showPanel");
    			$("#antipateStartTime_task").datebox("clear");
    			return;
    		}
    		if($("#taskTemp_task").attr("endDays")){
    			var endDays=$("#taskTemp_task").attr("endDays")-1;
    			var nowdate=$("#antipateStartTime_task").datebox("getText");
    			var datearr=nowdate.split("-");
    			var a=new Date(datearr[0],datearr[1]-1,datearr[2]);
    			a=a.valueOf();
    			a=a+endDays*24*60*60*1000;
    			a=new Date(a);
    			$("#antipateEndTime_task").datebox("setValue",a.getFullYear() + "-" + (a.getMonth() + 1) + "-" + a.getDate());
    		}
    		if(contentStart!=""){
    			$("#antipateStartTime_task").datebox("textbox").tooltip({
    		    	showDelay:200,
    		    	hideDelay:0,
    		    	position: 'right',
    		    	content: '<span style="color:#fff">'+contentStart+'</span>',
    		    	onShow: function(){
    		    		if($(".hideP").css("display")=="none"){
    		    			$("#antipateStartTime_task").datebox("textbox").tooltip("hide");
    		    		}
    		    		$(this).tooltip('tip').css({
    		    			backgroundColor: '#666',
    		    			borderColor: '#666'
    		    		});
    		    	}
    		    });
    		}
    	}
	});
	if(contentStart!=""){
		$("#antipateStartTime_task").datebox("textbox").tooltip({
	    	showDelay:200,
	    	hideDelay:0,
	    	position: 'right',
	    	content: '<span style="color:#fff">'+contentStart+'</span>',
	    	onShow: function(){
	    		if($(".hideP").css("display")=="none"){
	    			$("#antipateStartTime_task").datebox("textbox").tooltip("hide");
	    		}
	    		$(this).tooltip('tip').css({
	    			backgroundColor: '#666',
	    			borderColor: '#666'
	    		});
	    	}
	    });
	}
	$("#antipateEndTime_task").datebox({
		required:true,
    	novalidate:true,
    	missingMessage:"该项必填",
		editable:false,
    	width:130,
    	height:30,
    	value:"${task.antipateEndTime}",
    	onSelect:function(date){
    		var result="";
    		if($("#parentId_task").val()){
    			result=checkTime($("#parentId_task").val(),"",$("#antipateEndTime_task").datebox("getValue"),"task");
    		}else{
    			result=checkTime($("#planId_task").val(),"",$("#antipateEndTime_task").datebox("getValue"),"plan");
    		}
    		
    		var date=$("#antipateEndTime_task").datebox("getValue");
    		var dateStr=date.split("-");
    		var dateNum=dateStr[0]+dateStr[1]+dateStr[2];
    		var maxEndStr="";
    		var maxEndNum="";
    		if(maxTaskEnd){
    			maxEndStr=maxTaskEnd.split("-");
        		maxEndNum=maxEndStr[0]+maxEndStr[1]+maxEndStr[2];
    		}
    		if(maxEndNum&&dateNum<maxEndNum){
    			result=false;
    		}
    		
    		if(!result){
    			$("#antipateEndTime_task").datebox("showPanel");
    			$("#antipateEndTime_task").datebox("clear");
    		}
    		if(contentEnd!=""){
    			$("#antipateEndTime_task").datebox("textbox").tooltip({
    		    	showDelay:200,
    		    	hideDelay:0,
    		    	position: 'right',
    		    	content: '<span style="color:#fff">'+contentEnd+'</span>',
    		    	onShow: function(){
    		    		if($(".hideP").css("display")=="none"){
    		    			$("#antipateEndTime_task").datebox("textbox").tooltip("hide");
    		    		}
    		    		$(this).tooltip('tip').css({
    		    			backgroundColor: '#666',
    		    			borderColor: '#666'
    		    		});
    		    	}
    		    });
    		}
    	},
    	validType:'compareDate["#antipateStartTime_task"]'
	});
	if(contentEnd!=""){
		$("#antipateEndTime_task").datebox("textbox").tooltip({
	    	showDelay:200,
	    	hideDelay:0,
	    	position: 'right',
	    	content: '<span style="color:#fff">'+contentEnd+'</span>',
	    	onShow: function(){
	    		if($(".hideP").css("display")=="none"){
	    			$("#antipateEndTime_task").datebox("textbox").tooltip("hide");
	    		}
	    		$(this).tooltip('tip').css({
	    			backgroundColor: '#666',
	    			borderColor: '#666'
	    		});
	    	}
	    });
	}
	$("#actualStartTime_task").datebox({
		editable:false,
    	width:130,
    	height:30,
    	value:"${task.actualStartTime}",
    	readonly:true,
    	hasDownArrow:false,
	});
	$("#actualEndTime_task").datebox({
		editable:false,
    	width:130,
    	height:30,
    	value:"${task.actualEndTime}",
    	readonly:true,
    	hasDownArrow:false,
	});
	if("${task.status}"!="0"&&"${task.status}"!=""){
		$("#actualStartTime_task").parent().show();
    	$("#actualEndTime_task").parent().show();
    }else{
    	$("#actualStartTime_task").parent().hide();
    	$("#actualEndTime_task").parent().hide();
    }
	
	 var taskLeveValue='';	
	$.ajax({
		url:"${ctx}/flow/taskLevel/queryAll",
		async:false,
		success:function(data){
			taskLeveValue=formatData(data,'fid');	
	    }
		});
	var taskLeveName= $("#taskLevelId_task").fool("dhxCombo",{
		  width:130,
		  height:32,
		  data:taskLeveValue,
        clearOpt:false,
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
        toolsBar:{
            name:"事件级别",
            addUrl:"/flow/taskLevel/manage",
            refresh:true
        },

        required:true,
		  novalidate:true,
		  editable:false,
		  focusShow:true,
		  onChange:function(value,text){
			  var record = taskLeveName.getSelectedText();//获取选中行数据
			  if(record.fid===null){
				  taskLeveName.setComboText("");
				  taskLeveName.setComboValue("");
	    		}
		  },onLoadSuccess:function(value,text){
	    		/* if(taskLeveValue.length<=0&&($("#taskLevelId_task").next())[0].comboObj){	    			
	    			($("#taskLevelId_task").next())[0].comboObj.setComboText("<a onclick='jumpPlanLevel()'>暂无数据，请先添加数据</a>");
	    			($("#taskLevelId_task").next())[0].comboObj.setComboValue("");
	    		}  */
	    		var option= ($("#taskLevelId_task").next())[0].comboObj.getOption("${task.taskLevelId}");
	    		if(!option){
	    			($("#taskLevelId_task").next())[0].comboObj.addOption([["${task.taskLevelId}","${task.taskLevelName}"]]);
	    		}
		  }
	});	 
	
	 var securityValue='';	
	$.ajax({
		url:"${ctx}/flow/security/queryAll",
		async:false,		
		success:function(data){	
			securityValue=formatData(data,'fid');	
	    }
		});
	var securityName= $("#securityLevelId_task").fool("dhxCombo",{
		  width:130,
		  height:32,
		  data:securityValue,
        clearOpt:false,
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
        toolsBar:{
            name:"保密级别",
            addUrl:"/flow/security/manage",
            refresh:true
        },
        required:true,
		  novalidate:true,
		  editable:false,
		  focusShow:true,
		  onChange:function(value,text){
			  var record = securityName.getSelectedText();//获取选中行数据
			  if(record.fid===null){
				  securityName.setComboText("");
				  securityName.setComboValue("");
	    		}
		  },onLoadSuccess:function(value,text){
	    		/* if(securityValue.length<=0&&($("#securityLevelId_task").next())[0].comboObj){	    		
	    			($("#securityLevelId_task").next())[0].comboObj.setComboText("<a onclick='jumpSecLevel()'>暂无数据，请先添加数据</a>");
	    			($("#securityLevelId_task").next())[0].comboObj.setComboValue("");
	    		} */
	    		var option= ($("#securityLevelId_task").next())[0].comboObj.getOption("${task.securityLevelId}");
	    		if(!option){
	    			($("#securityLevelId_task").next())[0].comboObj.addOption([["${task.securityLevelId}","${task.securityLevelName}"]]);
	    		}
		  }
	});	 
	
	$("#amount_task").textbox({
		width:130,
    	height:30,
    	validType:['balanceAmount','numMaxLength[10]']
	});
    var boxWidth=130,boxHeight=31;//统一设置输入框大小
    var initiaterValue='';	
    $.ajax({
    	url:getRootPath()+'/userController/vagueSearch',
    	async:false,		
    	success:function(data){		  	
    		initiaterValue=formatData(data,'fid');	
        }
    	});
    var undertakerName = $('#undertakerName_task').fool('dhxComboGrid',{
    	required:true,
    	novalidate:true,
    	focusShow:true,
    	width:boxWidth,
    	height:boxHeight,  	
    	data:initiaterValue,
    	filterUrl:getRootPath()+'/userController/vagueSearch',
    	hasDownArrow:false,
    	setTemplate:{
    	 input:"#userName#",
    	 columns:[
    		{option:'#userCode#',header:'编号',width:100},
    		{option:'#userName#',header:'名称',width:100},
    		{option:'#phoneOne#',header:'电话',width:100},    
    		  ]
    	  },
    	  toolsBar:{
              name:"承办人",
              addUrl:"/userController/userMessageUI",
              refresh:true
          },
    	 onChange:function(value,text){
    		 if(!value){
    			 ($('#undertakerName_task').next())[0].comboObj.setComboText("");
    		 }else{
    			 var row=undertakerName.getSelectedText();//获取选中行数据
    	    		$("#undertakerId_task").val(row.fid);
    				$("#undertakerName_task").val(row.userName).focus();
    		 }
    		
    	},
    });
    
    $("#principalerDeptName_task").textbox({
    	width:130,
    	height:30,
    	editable:false
    });
    var principalerName = $('#principalerName_task').fool('dhxComboGrid',{
    	required:true,
    	novalidate:true,
    	focusShow:true,
    	width:boxWidth,
    	height:boxHeight,  	
    	data:initiaterValue,
    	filterUrl:getRootPath()+'/userController/vagueSearch',
    	hasDownArrow:false,
    	setTemplate:{
    	 input:"#userName#",
    	 columns:[
    		{option:'#userCode#',header:'编号',width:100},
    		{option:'#userName#',header:'名称',width:100},
    		{option:'#phoneOne#',header:'电话',width:100},    
    		  ]
    	  },
    	  toolsBar:{
              name:"责任人",
              addUrl:"/userController/userMessageUI",
              refresh:true
          },
    	 onChange:function(value,text){
    		 if(!value){
    			 ($('#principalerName_task').next())[0].comboObj.setComboText("");
    		 }else{
    			 var row=principalerName.getSelectedText();//获取选中行数据
    			 $("#principalerDeptId_task").val(row.deptId);
    	    	 $("#principalerDeptName_task").textbox("setValue",row.deptName);
    	    	 $("#principalerId_task").val(row.fid);
    			 $("#principalerName_task").val(row.userName).focus();
    		 }
    	},
    });
    
	$("#status_task").combobox({
		editable:false,
    	width:130,
    	height:30,
    	disabled:true,
    	hasDownArrow:false,
    	data:[{value:"0",text:"草稿"},{value:"1",text:"办理中"},{value:"2",text:"已办理待审核"},{value:"3",text:"已完成"},{value:"4",text:"已延迟且未开始办理"},{value:"5",text:"已延迟且未结束办理"},{value:"6",text:"已终止"}],
        value:"${task.status}",
	});
    /* $("#realAmount_task").textbox({
    	editable:false,
    	width:130,
    	height:30,
    	disabled:true,
        value:"${task.realAmount}",
	}); */
	$("#describe_task").textbox({
		width:686,
    	height:60,
    	multiline:true,
    	validType:'maxLength[200]'
	});
	initialTask();

$("#billDetail-win").window({
	left:0,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false, 
	width:$(window).width()-10,
	height:$(window).height()-60,
	closed:true,
	modal:true,
	onLoad:function(){
		$(".backBtn").click(function(){
			$("#billDetail-win").window("close");
		});
		$(".mybtn-footer").hide();
	},
	onClose:function(){
		if($("#checkBox").length>0 && $('#checkBox').html()){
			$("#checkBox").window("destroy");
		}
		if($("#pop-win").length>0 && $("#pop-win").html()){
			$("#pop-win").window("destroy");
		}
	},
});

$("#pop-win").window({
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false, 
	width:700,
	height:500,
	closed:true,
	modal:true,
	title:"选取单据",
});


function initialTask(){
	if(!"${task.fid}"){//新增事件
		$("#saveTask").css("display","inline-block");
	}else{
		exitEditTask();
	}
}

function closeTaskSouth(){
	$("#taskSouth").slideUp("",function(){
		setHeight("${task.fid}");
		setNode();
	});
	
};
function editTask(){
	$('#taskForm').form('disableValidation');
	$(".hideP").show();
	$("#actualStartTime_task").parent().hide();
	$("#actualEndTime_task").parent().hide();
	$("#addAttach_task").show();
	$(".delAttach_task").show();
	$(".clearBill").show();
	$("#taskForm").find(":checkbox").removeAttr("disabled");
	$("#taskBtn input").css("display","none");
	$("#cancelTask").css("display","inline-block");
	$("#saveTask").css("display","inline-block");
	$("#taskForm").find("input").removeAttr("readonly");
	$("#taskForm span.textbox").css("cssText","border-width:1px !important");
	$("#taskForm .textbox-addon-right").css("display","inline-block");
	$("#taskForm .combo-f").combo("readonly",false);
	$("#taskForm .textbox-f").textbox("readonly",false);
	/* $("#openBillWin").show(); */
	if(!undertakerName.getOption($("#undertakerId_task").val())){
		undertakerName.setComboText("");
	}
	if(!principalerName.getOption($("#principalerId_task").val())){
		principalerName.setComboText("");
	}
	$("#taskSouth").slideUp("",function(){
		setNode();
	});
		taskLeveName.enable(); 
		securityName.enable();
		taskTypeIdName.enable();
		undertakerName.enable();
		principalerName.enable();
		 $('#taskForm .dhxcombo_select_button').css("display","block");	    
		 $('#taskForm div.dhxcombo_material').css('border','1px solid #dfdfdf');
	
}
function exitEditTask(){
	if(!undertakerName.getOption($("#undertakerId_task").val())){
		undertakerName.setComboText("");
		$('#taskForm').form('enableValidation');
		return false;
	}
	if(!principalerName.getOption($("#principalerId_task").val())){
		principalerName.setComboText("");
		$('#taskForm').form('enableValidation');
		return false;
	}
	$(".hideP").hide();
	$("#actualStartTime_task").parent().show();
	$("#actualEndTime_task").parent().show();
	$(".clearBill").hide();
	if("${task.status}"!="0"&&"${task.status}"!=""){
		$("#actualStartTime_task").parent().show();
    	$("#actualEndTime_task").parent().show();
    }else{
    	$("#actualStartTime_task").parent().hide();
    	$("#actualEndTime_task").parent().hide();
    }
	/* $("#realAmount_task").textbox("textbox").css("background-color","white"); */
	$("#addAttach_task").hide();
	$(".delAttach_task").hide();
	/* $("#openBillWin").hide(); */
	var attachs=$(".attachName_task");
	for(var i=0;i<attachs.length;i++){
		if($(attachs[i]).attr("saved")!="true"){
			$(attachs[i]).next().remove();
			$(attachs[i]).remove();
		}
	}
	$("#taskForm").find("input").attr("readonly","readonly");
	$("#taskForm").find(":checkbox").attr("disabled","disabled");
	$("#taskBtn input").css("display","inline-block");
	$("#cancelTask").css("display","none");
	$("#saveTask").css("display","none");
	$("#taskForm span.textbox").css("cssText","border-width:0 !important");
	$("#taskForm .textbox-addon-right").css("display","none");
	$("#taskForm .combo-f").combo("readonly",true);
	$("#taskForm .textbox-f").textbox("readonly",true);
	 //设置dhxComboGrid和dhxCombo不可用
	taskLeveName.disable(); 
	securityName.disable();
	taskTypeIdName.disable();
	undertakerName.disable();
	principalerName.disable();
    $('#taskForm .dhxcombo_select_button').css("display","none");	    
    $('#taskForm div.dhxcombo_material').css('border','none');
    $('#taskForm .dhxcombo_input').css('background','none');
}

    $("#addAttach_task").click(function(){
    	if($(".attachName_task").length>=5){
    		$.fool.alert({msg:"最多上传5个附件"});
    		return;
    	}
    	$(this).before("<input class='attachName_task' id='' readonly='readonly' name='file' value='请选取附件'></input><a class='delAttach_task'>x</a>");
    	ajaxUpload($(this).prev().prev(),$(this).prev().prev(),{triggerType:"task40"});
    });

    $("#cancelTask").click(function(){
    	$('#taskForm').form("reset");
    	if("${task.taskTypeId}"){
    		($("#taskTypeId_task").next())[0].comboObj.setComboValue("${task.taskTypeId}");
    	}
    	if("${task.taskLevelId}"){
    		($("#taskLevelId_task").next())[0].comboObj.setComboValue("${task.taskLevelId}");
    	}
    	if("${task.securityLevelId}"){
    		($("#securityLevelId_task").next())[0].comboObj.setComboValue("${task.securityLevelId}");
    	}
    	if("${task.undertakerId}"){
    		($("#undertakerName_task").next())[0].comboObj.setComboValue("${task.undertakerId}");
    	}
    	if("${task.principalerId}"){
    		($("#principalerName_task").next())[0].comboObj.setComboValue("${task.principalerId}");
    	}
    	$('#taskForm').form('validate');
    	exitEditTask();
    });
    $("#editTask").click(function(){
    	editTask();
    });
    //保存按钮
    $("#saveTask").click(function(){
    	/* if(!undertakerName.getSelectedText()){
    		undertakerName.setComboText("");
    	}
        if(!principalerName.getSelectedText()){
        	principalerName.setComboText("");
    	} */
    	/* undertakerName.getSelectedText();
    	principalerName.getSelectedText(); */
    	
    	if(undertakerName.getOption($("#undertakerId_task").val())){
    		var undertakerLevel=undertakerName.getOption($("#undertakerId_task").val()).text.securityLevel;
		}else{
			undertakerName.setComboText("");
		}
		if(principalerName.getOption($("#principalerId_task").val())){
			var principalerLevel=principalerName.getOption($("#principalerId_task").val()).text.securityLevel;
		}else{
			principalerName.setComboText("");
		}
    	$('#taskForm').form('enableValidation');
    	if($('#taskForm').form('validate')){
    		$("#saveTask").attr("disabled","disabled");
    		/* var undertakerLevel=undertakerName.getOption($("#undertakerId_task").val()).text.securityLevel;
    		var principalerLevel=principalerName.getOption($("#principalerId_task").val()).text.securityLevel; */
    		var sLevel=securityName.getOption(securityName.getSelectedValue());
    		if(sLevel){
    			if(undertakerLevel&&undertakerLevel<sLevel.text.level){
    				$.fool.confirm({title:'确认',msg:'所选承办人的保密级别比所选保密级别低，确定保存？',fn:function(r){
    					if(!r){
    						return false;
    					}else{
    						saver();
    					}
    				}})
    			}else if(principalerLevel&&principalerLevel<sLevel.text.level){
    				$.fool.confirm({title:'确认',msg:'所选负责人的保密级别比所选保密级别低，确定保存？',fn:function(r){
    					if(!r){
    						return false;
    					}else{
    						saver();
    					}
    				}})
    			}else{
    				saver();
    			}
    		}else{
    			saver();
    		}
    	}
    });
    //删除按钮
    $("#deleteTask").click(function(){//注释删除
    	$.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
   		 if(r){
   			 $.ajax({
   					type : 'post',
   					url : '${ctx}/flow/task/delete',
   					data : {fid :"${task.fid}"},
   					dataType : 'json',
   					success : function(data) {	
   						if(data.returnCode == '0'){
   							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
   								$("#task-list").load("${ctx}/timeAxis/taskList",{planId:"${task.planId}",planStatus:"${param.planStatus}"});
   								$("#planDetail").load("${ctx}/timeAxis/planDetail",{id:$("#planId_task").val()},function(){});
   							}});
   						}else{
   							$.fool.alert({msg:data.message});
   						}
   		    		},
   		    		error:function(){
   		    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
   		    		}
   				});
   		 }
    	}});
    });
    $("#auditExecuteTask").click(function(){//注释审核办理
    	if($("#auditExecuteForm").length<=0){
    		$("#taskSouth").html('<form class="form1" id="auditExecuteForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closeTaskSouth()"></span></div><input name="fid" type="hidden" value="${task.fid}"/><p><font><em>*</em>审核意见：</font><input class="textBox" id="checkResult" name="checkResult" /></p><br/><p><font>备注：</font><textarea class="textBox easyui-validatebox" name="describe" data-options="validType:\'maxLength[200]\'" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="auditExecuteFn(this)" value="审核"/></p></form>');
    		$("#auditExecuteForm textarea").validatebox({
    		});
    		$.ajax({
    			url:"${ctx}/flow/task/showLastAttach?num="+Math.random(),
    			async:false,
    			data:{taskId:"${task.fid}"},
    			success:function(data){
    				if(data){
    					var attachs="";
    					for(var i=0;i<data.length;i++){
    						attachs=attachs+"<input class='attachName' id='"+data[i].id+"' title='点击下载附件' readonly='readonly' name='file' value='"+data[i].name+"' />";
    					}
    					$("#checkResult").parent().after("<br/><p><font>附件：</font>"+attachs+"</p>");
    				}
    			}
    		});
    		$(".attachName").click(function(){
    			var target=$(this);
    			if(target.attr("id")){
    				window.location.href=encodeURI(encodeURI("${ctx}/flow/plan/download?fileId="+target.attr("id")));
    			}
    		});
    		$("#checkResult").combobox({
    			required:true,
    			novalidate:true,
    			editable:false,
    	    	width:160,
    	    	height:30,
    	    	data:[{value:"0",text:"审核通过"},{value:"1",text:"拒绝，返回发起人"}]
    		});
    	}
    	$("#taskSouth").slideDown("",function(){
    		setHeight("${task.fid}");
            setNode();
    	});
    });
    $("#auditDelayTask").click(function(){//注释审核延迟
    	var delayReson="";
    	$.ajax({
    		url:"${ctx}/flow/task/getApplyDelayMsg",
    		async:false,
    		data:{fid:"${task.fid}"},
    		success:function(data){
    			delayReson=data.deplayReason;
    		}
    	});
    	if($("#auditDelayForm").length<=0){
    		$("#taskSouth").html('<form class="form1" id="auditDelayForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closeTaskSouth()"></span></div><p><font>申请延迟日期：</font><span>'+("${task.delayedEndTime}").slice(0,10)+'</span></p><br/><p><font>申请延迟理由：</font><span>'+delayReson+'</span></p><br/><input name="fid" type="hidden" value="${task.fid}"/><p><font><em>*</em>审核意见：</font><input class="textBox" id="checkResult" name="checkResult" /></p><br/><p><font>备注：</font><textarea class="textBox easyui-validatebox" name="describe" data-options="validType:\'maxLength[200]\'" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="auditDelayFn(this)" value="审核"/></p></form>');
    		$("#auditDelayForm textarea").validatebox({
    			validType:"maxLength[200]"
    		});
    		$.ajax({
    			url:"${ctx}/flow/task/showLastAttach?num="+Math.random(),
    			async:false,
    			data:{taskId:"${task.fid}"},
    			success:function(data){
    				if(data){
    					var attachs="";
    					for(var i=0;i<data.length;i++){
    						attachs=attachs+"<input class='attachName' id='"+data[i].id+"' title='点击下载附件' readonly='readonly' name='file' value='"+data[i].name+"' />";
    					}
    					$("#checkResult").parent().after("<br/><p><font>附件：</font>"+attachs+"</p>");
    				}
    			}
    		});
    		$(".attachName").click(function(){
    			var target=$(this);
    			if(target.attr("id")){
    				window.location.href=encodeURI(encodeURI("${ctx}/flow/plan/download?fileId="+target.attr("id")));
    			}
    		});
    		$("#checkResult").combobox({
    			required:true,
    			novalidate:true,
    			editable:false,
    	    	width:160,
    	    	height:30,
    	    	data:[{value:"0",text:"审核通过"},{value:"1",text:"拒绝，返回发起人"}]
    		});
    	}
    	$("#taskSouth").slideDown("",function(){
    		setHeight("${task.fid}");
    		setNode();
    	});
    });
    $("#delayTask").click(function(){//注释延迟申请
    	if($("#delayTaskForm").length<=0){
    		$("#taskSouth").html('<form class="form1" id="delayTaskForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closeTaskSouth()"></span></div><input name="fid" type="hidden" value="${task.fid}"/><p><font><em>*</em>延迟日期：</font><input class="textBox" id="delayedEndTime" name="delayedEndTime" /></p><br/><p><font>备注：</font><textarea class="textBox" name="deplayReason" style="width:700px !important"></textarea></p><br/><p><font>附件：</font><div id="attachText"><span id="addAttach" title="添加附件,附件格式须为：pdf,jpeg,jpg,png,bmp,ppt,xls,xlsx,doc,docx,zip,rar,txt"></span></div></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="delayTaskFn(this)" value="提交"/></p></form>');
    		$("#delayedEndTime").datebox({
    			required:true,
    			novalidate:true,
    			editable:false,
    	    	width:160,
    	    	height:30,
    	    	validType:"delayDate",
    	    	onSelect:function(date){
    	    		$("#delayedEndTime").datebox("enableValidation");
    	    	},
    		});
    		$("#delayTaskForm textarea").validatebox({
    			validType:"maxLength[200]"
    		});
    	}
    	$("#addAttach").click(function(){
    		if($(".attachName").length>=5){
    			$.fool.alert({msg:"最多上传5个附件"});
        		return;
    		}
    		$(this).before("<input class='attachName' readonly='readonly' name='file' value='请选取附件'></input><a class='delAttach'>x</a>");
    		ajaxUpload($(this).prev().prev(),$(this).prev().prev(),{busId:"${task.fid}",triggerType:"task20"});
    		
    		$(".delAttach").click(function(){
    			$(this).prev().remove();
            	$(this).remove();
    		});
    	});
    	$("#taskSouth").slideDown("",function(){
    		setHeight("${task.fid}");
    		setNode();
    	});
    });
    $("#stopTask").click(function(){//注释终止
    	if($("#stopTaskForm").length<=0){
    		$("#taskSouth").html('<form class="form1" id="stopTaskForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closeTaskSouth()"></span></div><input name="fid" type="hidden" value="${task.fid}"/><p><font><em>*</em>终止描述：</font><textarea class="textBox validatebox" name="describe" data-options="validType:\'maxLength[200]\'" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="stopTaskFn(this)" value="提交"/></p></form>');
    		$("#stopTaskForm textarea").validatebox({
    			required:true,
    			novalidate:true
    		});
    	}
    	$("#taskSouth").slideDown("",function(){
    		setHeight("${task.fid}");
    		setNode();
    	});
    });
    $("#rankTask").click(function(){//注释评分
    	if($("#rankTaskForm").length<=0){
    		$("#taskSouth").html('<form class="form1" id="rankTaskForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closeTaskSouth()"></span></div><input name="businessId" type="hidden" value="${task.fid}"/><p><font><em>*</em>评分：</font><div id="rank-task"></div></p><p><font><em>*</em>评论：</font><textarea class="textBox" name="comment" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="rankTaskFn(this)" value="提交"/></p></form>');
    		$("#rankTaskForm textarea").validatebox({
    			required:true,
    			novalidate:true,
    			validType:'maxLength[200]'
    		});
    		$("#rank-task").raty({
    			score: 3, 
    			scoreName: 'rank',
    			path: '${ctx}/resources/js/raty/img', 
    		});
    	}
    	$("#taskSouth").slideDown("",function(){
    		setHeight("${task.fid}");
    		setNode();
    	});
    });
    $("#changeTask").click(function(){//注释变更
    	if($("#changeTaskForm").length<=0){
    		$("#taskSouth").html('<form class="form1" id="changeTaskForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closeTaskSouth()"></span></div><input name="fid" type="hidden" value="${task.fid}"/><p><font>责任人：</font><input class="textBox" name="principalerId" /></p><p><font>承办人：</font><input class="textBox" name="undertakerId" /></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="changeTaskFn(this)" value="提交"/></p></form>');
    		$("#changeTaskForm input[name='principalerId']").fool('combogrid',{
    	    	missingMessage:"该项必填",
    	    	width:160,
    	    	height:30,
    	    	idField:'fid',
    	    	textField:'userName',
    	    	panelWidth:450,
    	    	fitColumns:true,
    	    	url:getRootPath()+'/userController/vagueSearch',
    	    	columns:[[
    	    		{field:'fid',title:'fid',hidden:true},
    	    		{field:'userCode',title:'编号',width:100,searchKey:false},
    	    		{field:'userName',title:'名称',width:100,searchKey:false},
    	    		{field:'phoneOne',title:'电话',width:100,searchKey:false},
    	    		{field:'searchKey',hidden:true,searchKey:true},
    	    	          ]],
    	    });
    		$("#changeTaskForm input[name='undertakerId']").fool('combogrid',{
    	    	missingMessage:"该项必填",
    	    	width:160,
    	    	height:30,
    	    	idField:'fid',
    	    	textField:'userName',
    	    	panelWidth:450,
    	    	fitColumns:true,
    	    	url:getRootPath()+'/userController/vagueSearch',
    	    	columns:[[
    	    		{field:'fid',title:'fid',hidden:true},
    	    		{field:'userCode',title:'编号',width:100,searchKey:false},
    	    		{field:'userName',title:'名称',width:100,searchKey:false},
    	    		{field:'phoneOne',title:'电话',width:100,searchKey:false},
    	    		{field:'searchKey',hidden:true,searchKey:true},
    	    	          ]],
    	    });
    		$("#changeTaskForm input[textboxname='principalerId']").combogrid('textbox').focus(function(){
    			if($("#changeTaskForm input[textboxname='principalerId']").combogrid("textbox").attr("readonly")=="readonly"){
    				return false;
    			}
    			$("#changeTaskForm input[textboxname='principalerId']").combogrid('showPanel');
    		});
    		$("#changeTaskForm input[textboxname='undertakerId']").combogrid('textbox').focus(function(){
    			if($("#changeTaskForm input[textboxname='undertakerId']").combogrid("textbox").attr("readonly")=="readonly"){
    				return false;
    			}
    			$("#changeTaskForm input[textboxname='undertakerId']").combogrid('showPanel');
    		});
    	}
    	$("#taskSouth").slideDown("",function(){
    		setHeight("${task.fid}");
    		setNode();
    	});
    });
    $("#relateTask").click(function(){//注释关联
    	if($("#relateTaskForm").length<=0){
    		$("#taskSouth").html('<form class="form1" id="relateTaskForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closeTaskSouth()"></span></div><div style="position:relative"><div id="gridBox" style="width:60%;display:inline-block"><h4 style="margin:5px;color:#00A8FF">前置关联：</h4><div id="pre"></div><br/><h4 style="margin:5px;color:#00A8FF">后置关联：</h4><div id="post"></div></div><div id="detailBox" style="display:inline-block;width:auto;margin-left:20px;position: absolute;top:20px"></div></div><br/><p style="position: relative;left: 660px;"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="relateTaskFn(this)" value="提交"/></p></form>');
    		$("#pre").datagrid({
    			url:"${ctx}/flow/task/getFrontRelevanceTask?fid=${task.fid}",
    			idField:"fid",
    			height:150,
    			fitColumns:true,
    			columns:[[
    			          {field:'fid',title:'fid',checkbox:true},    
    			          {field:'code',title:'编号',width:100},
    			          {field:'name',title:'名称',width:100},
    			             ]],
    			onClickRow:function(index, row){
    				$("#detailBox").html("<p>事件编号："+row.code+"</p><br/><p>事件名称："+row.name+"</p><br/><p>开始时间："+row.antipateStartTime.slice(0,10)+"</p><br/><p>结束时间："+row.antipateEndTime.slice(0,10)+"</p><br/><p>金额："+row.amount+"</p><br/><p>发起人："+row.initiaterName+"</p><br/><p>责任人："+row.principalerName+"</p><br/><p>承办人："+row.undertakerName+"</p>");
    			},
    			onLoadSuccess:function(data){
    				var rows=data.rows;
    				for(var i in rows){
    					if(rows[i].checked===true){
    						$("#pre").datagrid("checkRow",i);
    					}
    				}
    			}
    		});
    		$("#post").datagrid({
    			url:"${ctx}/flow/task/getBehindRelevanceTask?fid=${task.fid}",
    			idField:"fid",
    			height:150,
    			fitColumns:true,
    			singleSelect:true,
    			columns:[[
    			          {field:'fid',title:'fid',checkbox:true},    
    			          {field:'code',title:'编号',width:100},
    			          {field:'name',title:'名称',width:100},
    			             ]],
    			onClickRow:function(index, row){
    				$("#detailBox").html("<p>事件编号："+row.code+"</p><br/><p>事件名称："+row.name+"</p><br/><p>开始时间："+row.antipateStartTime.slice(0,10)+"</p><br/><p>结束时间："+row.antipateEndTime.slice(0,10)+"</p><br/><p>金额："+row.amount+"</p><br/><p>发起人："+row.initiaterName+"</p><br/><p>责任人："+row.principalerName+"</p><br/><p>承办人："+row.undertakerName+"</p>");
    			},
    			onLoadSuccess:function(data){
    				var rows=data.rows;
    				for(var i in rows){
    					if(rows[i].checked===true){
    						$("#post").datagrid("checkRow",i);
    					}
    				}
    			},
    			onBeforeCheck:function(index, row){
    				return false;
    			},
    			onBeforeUncheck:function(index, row){
    				return false;
    			},
    			onCheckAll:function(rows){
    				return false;
    			},
    			onUnselectAll:function(rows){
    				return false;
    			}
    		});
    	}
    	$("#taskSouth").slideDown("",function(){
    		setHeight("${task.fid}");
    		setNode();
    	});
    });
    $("#executeBeginTask").click(function(){//注释开始办理
    	$.ajax({
				type : 'post',
				url : '${ctx}/flow/task/startExcute',
				data : {fid :"${task.fid}"},
				dataType : 'json',
				success : function(data) {	
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
							$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
							$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){});
						}});
					}else{
						$.fool.alert({msg:data.message});
					}
	    		},
	    		error:function(){
	    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
	    		}
    	});
    });
    
    $("#assignTask").click(function(){//注释确认分派
    	$.ajax({
			type : 'post',
			url : '${ctx}/flow/task/assignTask',
			data : {fid :"${task.fid}"},
			dataType : 'json',
			success : function(data) {	
				if(data.returnCode == '0'){
					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){});
					}});
				}else{
					$.fool.alert({msg:data.message});
				}
    		},
    		error:function(){
    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
    		}
    	});
    });
    
    $("#executeDoneTask").click(function(){//注释结束办理
    	if($("#executeDoneForm").length<=0){
    		$("#taskSouth").html('<form class="form1" id="executeDoneForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closeTaskSouth()"></span></div><input name="fid" type="hidden" value="${task.fid}"/><p><font><em>*</em>办理结束描述：</font><textarea class="textBox validatebox" name="endExcuteDescribe" data-options="validType:\'maxLength[200]\'" style="width:700px !important"></textarea></p><br/><p><font>附件：</font><div id="attachText"><span id="addAttach" title="添加附件,附件格式须为：pdf,jpeg,jpg,png,bmp,ppt,xls,xlsx,doc,docx,zip,rar,txt"></span></div></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="executeDoneFn(this)" value="提交"/></p></form>');
    		$("#executeDoneForm textarea").validatebox({
    			required:true,
    			novalidate:true
    		});
    		/* $("#executeDoneForm #realAmount_ex").validatebox({
    			required:true,
    			novalidate:true,
    			validType:"balanceAmount"
    		}); */
    	}
    	$("#addAttach").click(function(){
    		if($(".attachName").length>=5){
    			$.fool.alert({msg:"最多上传5个附件"});
        		return;
    		}
    		$(this).before("<input class='attachName' readonly='readonly' value='请选取附件'></input><a class='delAttach'>x</a>");
    		ajaxUpload($(this).prev().prev(),$(this).prev().prev(),{busId:"${task.fid}",triggerType:"task42"});
    		$(".delAttach").click(function(){
    			$(this).prev().remove();
            	$(this).remove();
    		});
    		/* inputer.clickBtn(); */
    	});
    	$("#taskSouth").slideDown("",function(){
    		setHeight("${task.fid}");
    		setNode();
    	});
    });
    
    function executeDoneFn(e){
    	$('#executeDoneForm').form('enableValidation');
    	if($('#executeDoneForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		var attachs=$(".attachName");
    		var param=$('#executeDoneForm').serialize();
    		var attachIds="";
    		for(var i=0;i<attachs.length;i++){
    			if(i==0){
    				attachIds=$(attachs[0]).attr("id");
    			}else{
    				attachIds=attachIds+","+$(attachs[i]).attr("id");
    			}
    		}
    		param=param+"&attachIds="+attachIds;
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/flow/task/endExecute?triggerType=42',
    			data : param,
    			dataType : 'json',
    			success : function(data) {	
    				if(data.returnCode == '0'){
    					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
    						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
    						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){
    							setHeight("${task.fid}");
    						});
    					}});
    				}else{
    					$.fool.alert({msg:data.message});
    					$(e).removeAttr("disabled");
    				}
        		},
        		error:function(){
        			$.fool.alert({msg:"系统正忙，请稍后再试。"});
        			$(e).removeAttr("disabled");
        		}
    		});
    	}
    }
    
    function auditExecuteFn(e){
    	$('#auditExecuteForm').form('enableValidation');
    	if($('#auditExecuteForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/flow/task/checkExcute',
    			data : $('#auditExecuteForm').serialize(),
    			dataType : 'json',
    			success : function(data) {	
    				if(data.returnCode == '0'){
    					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
    						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
    						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){
    							setHeight("${task.fid}");
    						});
    						$("#planDetail").load("${ctx}/timeAxis/planDetail",{id:$("#planId_task").val()},function(){});
    					}});
    				}else{
    					$.fool.alert({msg:data.message});
    					$(e).removeAttr("disabled");
    				}
        		},
        		error:function(){
        			$.fool.alert({msg:"系统正忙，请稍后再试。"});
        			$(e).removeAttr("disabled");
        		}
    		});
    	}
    }
    
    function auditDelayFn(e){
    	$('#auditDelayForm').form('enableValidation');
    	if($('#auditDelayForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/flow/task/checkApplyDelay',
    			data : $('#auditDelayForm').serialize(),
    			dataType : 'json',
    			success : function(data) {	
    				if(data.returnCode == '0'){
    					var msg="";
    					var time="";
    					if(data.message){
    						msg=data.message;
    						time=undefined;
    					}else{
    						msg="审核成功！";
    						time=1000;
    					}
    					$.fool.alert({time:time,msg:msg,fn:function(){
    						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
    						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){
    							setHeight("${task.fid}");
    						});
    					}});
    				}else{
    					$.fool.alert({msg:data.message});
    					$(e).removeAttr("disabled");
    				}
        		},
        		error:function(){
        			$.fool.alert({msg:"系统正忙，请稍后再试。"});
        			$(e).removeAttr("disabled");
        		}
    		});
    	}
    }
    
    function delayTaskFn(e){
    	$('#delayTaskForm').form('enableValidation');
    	if($('#delayTaskForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		var attachs=$(".attachName");
    		var param=$('#delayTaskForm').serialize();
    		var attachIds="";
    		for(var i=0;i<attachs.length;i++){
    			if(i==0){
    				attachIds=$(attachs[0]).attr("id");
    			}else{
    				attachIds=attachIds+","+$(attachs[i]).attr("id");
    			}
    		}
    		param=param+"&attachIds="+attachIds;
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/flow/task/applyDelay?triggerType=20',
    			data : param,
    			dataType : 'json',
    			success : function(data) {	
    				if(data.returnCode == '0'){
    					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
    						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
    						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){
    							setHeight("${task.fid}");
    						});
    					}});
    				}else{
    					$.fool.alert({msg:data.message});
    					$(e).removeAttr("disabled");
    				}
        		},
        		error:function(){
        			$.fool.alert({msg:"系统正忙，请稍后再试。"});
        			$(e).removeAttr("disabled");
        		}
    		});
    	}
    }
    
    function stopTaskFn(e){
    	$('#stopTaskForm').form('enableValidation');
    	if($('#stopTaskForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/flow/task/stop',
    			data : $('#stopTaskForm').serialize(),
    			dataType : 'json',
    			success : function(data) {	
    				if(data.returnCode == '0'){
    					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
    						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
    						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){
    							setHeight("${task.fid}");
    						});
    						$("#planDetail").load("${ctx}/timeAxis/planDetail",{id:$("#planId_task").val()},function(){});
    					}});
    				}else{
    					$.fool.alert({msg:data.message});
    					$(e).removeAttr("disabled");
    				}
        		},
        		error:function(){
        			$.fool.alert({msg:"系统正忙，请稍后再试。"});
        			$(e).removeAttr("disabled");
        		}
    		});
    	}
    }
    
    function changeTaskFn(e){
    	var pri=$("#changeTaskForm input[textboxname='principalerId']").combogrid("getValue");
		var cha=$("#changeTaskForm input[textboxname='undertakerId']").combogrid("getValue");
    	if(!pri&&!cha){
    		$.fool.alert({msg:"请选择人员。"});
    		return;
    	}
    	$(e).attr("disabled","disabled");
    	$.ajax({
			type : 'post',
			url : '${ctx}/flow/task/change',
			data : $('#changeTaskForm').serialize(),
			dataType : 'json',
			success : function(data) {	
				if(data.returnCode == '0'){
					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){
							setHeight("${task.fid}");
						});
					}});
				}else{
					$.fool.alert({msg:data.message});
					$(e).removeAttr("disabled");
				}
    		},
    		error:function(){
    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
    			$(e).removeAttr("disabled");
    		}
		});
    }
    
    function relateTaskFn(e){
    	var preChecked=$("#pre").datagrid("getChecked");
		var frontRelevanceIds="";
		for(var i in preChecked){
			frontRelevanceIds=preChecked[i].fid+","+frontRelevanceIds;
		}
		$(e).attr("disabled","disabled");
		$.ajax({
			type : 'post',
			url : '${ctx}/flow/task/relevance',
			data : {fid:"${task.fid}",frontRelevanceIds:frontRelevanceIds/* ,behindRelevanceIds:behindRelevanceIds */},
			dataType : 'json',
			success : function(data) {	
				if(data.returnCode == '0'){
					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){
							setHeight("${task.fid}");
						});
					}});
				}else{
					$.fool.alert({msg:data.message});
					$(e).removeAttr("disabled");
				}
    		},
    		error:function(){
    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
    			$(e).removeAttr("disabled");
    		}
		});
    }
    
    function rankTaskFn(e){
    	$('#rankTaskForm').form('enableValidation');
    	if($('#rankTaskForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/flow/task/addRank?type=1',
    			data : $('#rankTaskForm').serialize(),
    			dataType : 'json',
    			success : function(data) {	
    				if(data.returnCode == '0'){
    					$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
    						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
    						$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:"${task.fid}",planStatus:"${param.planStatus}"},function(){
    							setHeight("${task.fid}");
    						});
    						$("#planDetail").load("${ctx}/timeAxis/planDetail",{id:$("#planId_task").val()},function(){});
    					}});
    				}else{
    					$.fool.alert({msg:data.message});
    					$(e).removeAttr("disabled");
    				}
        		},
        		error:function(){
        			$.fool.alert({msg:"系统正忙，请稍后再试。"});
        			$(e).removeAttr("disabled");
        		}
    		});
    	}
    }
    
    function setHeight(id){
    	height=$("#ganttDetail").height();
		$(".taskBox_"+id).css("height",height+"px");
    }
    
    function checkTime(parentId,start,end,flag){
    	var result=true;
    	var parentStart="";
        var parentEnd="";
    	if(start){
			parentStart=(gParentStart).slice(0,10);
			var parentStartStr=parentStart.split("-");
			var startStr=start.split("-");
			var parentStartNum=parseInt(parentStartStr[0]+parentStartStr[1]+parentStartStr[2]);
			var startStrNum=parseInt(startStr[0]+startStr[1]+startStr[2]);
			if(parentStartNum>startStrNum){
				result=false;
			}
		}
		
		if(end){
			parentEnd=(gParentEnd).slice(0,10);
			var parentEndStr=parentEnd.split("-");
			var endStr=end.split("-");
			var parentEndNum=parseInt(parentEndStr[0]+parentEndStr[1]+parentEndStr[2]);
			var endStrNum=parseInt(endStr[0]+endStr[1]+endStr[2]);
			if(parentEndNum<endStrNum){
				result=false;
			}
		}
    	return result;
    }
    
    function closeWin(){
    	if(win)	win.window('close').window('clear');
    }
    
    /* function getTaskMinStart(id){
    	var taskId=$("#fid_task").val();
    	var minStart="";
    	var minStartStr="";
		var minStartNum="";
    	$.ajax({
    		url:"${ctx}/flow/task/queryAllByPlan",
    		async:false,
    		data:{planId:id,num:Math.random()},
    		success:function(data){
    			if(data.length>0&&data[0]){
    			  var start="";
    			  var startStr="";
    			  var startNum="";
    			  for(var i=1;i<data.length;i++){
    				  if(data[i].parentId==taskId){
    					  start=(data[i].antipateStartTime).slice(0,10);
        				  startStr=start.split("-");
        				  startNum=parseInt(startStr[0]+startStr[1]+startStr[2]);
              			  if(minStart){
              				minStartStr=minStart.split("-");
              				minStartNum=parseInt(minStartStr[0]+minStartStr[1]+minStartStr[2]);
              				if(minStartNum>startNum){
              					minStart=start;
              				}
              			  }else{
              				minStart=start;
              			  }
    				  }
    			  }
    			}
    		}
    	});
    	return minStart;
    }
    function getTaskMaxEnd(id){
    	var taskId=$("#fid_task").val();
    	var maxEnd="";
    	var maxEndStr="";
		var maxEndNum="";
    	$.ajax({
    		url:"${ctx}/flow/task/queryAllByPlan",
    		async:false,
    		data:{planId:id,num:Math.random()},
    		success:function(data){
    			if(data.length>0&&data[0]){
    			  var end="";
    			  var endStr="";
    			  var endNum="";
    			  for(var i=1;i<data.length;i++){
    				  if(data[i].parentId==taskId){
    					  end=(data[i].antipateEndTime).slice(0,10);
        				  endStr=end.split("-");
        				  endNum=parseInt(endStr[0]+endStr[1]+endStr[2]);
              			  if(maxEnd){
              				maxEndStr=maxEnd.split("-");
              				maxEndNum=parseInt(maxEndStr[0]+maxEndStr[1]+maxEndStr[2]);
              				if(maxEndNum<endNum){
              					maxEnd=end;
              				}
              			  }else{
              				maxEnd=end;
              			  }
    				  }
    			  }
    			}
    		}
    	});
    	return maxEnd;
    } */
    
    $("#attachText_task").on("mouseup",".attachName_task",function(){
    	var target=$(this);
    	if(target.attr("disabled")=="disabled"){
    		if(target.attr("id")){
    			window.location.href=encodeURI(encodeURI("${ctx}/flow/plan/download?fileId="+target.attr("id")));
    		}
    	}
    });
    $("#attachText_task").on("mouseup",".delAttach_task",function(){
    	var target=$(this);
    	var attachId=target.prev().attr("id");
    	if(attachId){
    		$.ajax({
        		url:"${ctx}/flow/task/deleteAttach?num="+Math.random(),
        		async:false,
        		data:{attachId:attachId},
        		success:function(data){
        			if(data.returnCode==0){
        				target.prev().remove();
        				target.remove();
        			}else{
        				$.fool.alert({msg:"系统正忙，请稍后再试。"});
        			}
        		}
        	});
    	}else{
    		target.prev().remove();
			target.remove();
    	}
    });
    
    function openBillWin(){
    	$("#pop-win").window("open");
    	$('#pop-win').window('move',{top:$(document).scrollTop() + 80});
    	$("#billList").jqGrid({	
			pager:"#billListPage",
			autowidth:true,
			height:300,
			rowNum:10,
			rowList:[10,20,30],
			viewrecords:true,
			multiselect:true,
			jsonReader:{
				records:"total",
				total: "totalpages",  
			},
			colModel:[
				       {name : 'fid',label : 'fid',hidden:true},
				       {name : 'billType',label : '单据类型',hidden:true},
				       {name : 'totalAmount',label : '金额',hidden:true},
				       {name : 'code',label : '单据编号',sortable:false,align:'center',width:"200px"},
				       {name : 'billDate',label : '单据日期',sortable:false,align:'center',width:"200px",formatter:function(cellvalue, options, rowObject){
				    	   if(cellvalue){
				    		   return cellvalue.slice(0,10)
				    	   }
				       }},
				       {name : 'billSign',label : '单据类型',sortable:false,align:'center',width:"200px",formatter:function(cellvalue, options, rowObject){
				    	   for(var i=0; i<billType.length; i++){
				    			if (billType[i].value == rowObject.billType) return billType[i].text;
				    	   }
				    		return rowObject.billType; 
				       }},
			      ],
		}).navGrid('#billListPage',{add:false,del:false,edit:false,search:false,view:false});
    	$('#billList').trigger("reloadGrid");
    	var billTypeName= $("#billType").fool("dhxCombo",{
    		width:130,
  		    height:32,
  		    data:billType,
  		    setTemplate:{
  		    	input:"#text#",
  			    option:"#text#"
  		    },
  		    editable:false,
  		    focusShow:true,
  		    prompt:"单据类型",
  		    onChange:function(value,text){
  		    	var url="";
  		    	var type="";
  		    	if(value==53){
  		    		url='${ctx}/costBill/list?recordStatus=1&taskFilter=1';
  		    	}else if(value==51){
  		    		url='${ctx}/receiveBill/list?recordStatus=1&taskFilter=1';
  		    	}else if(value==52){
  		    		url='${ctx}/payBill/list?recordStatus=1&taskFilter=1';
  		    	}else if(value==55){
  		    		url='${ctx}/purchaseRebBill/list?recordStatus=1&taskFilter=1';
  		    	}else if(value==56){
  		    		url='${ctx}/salesRebateRebBill/list?recordStatus=1&taskFilter=1';
  		    	}else{
  		    		for(var i=0;i<billType.length;i++){
  		    			if(billType[i].value==value){
  		    				type=billType[i].billType;
  		    				break;
  		    			}
  		    		}
  		    		url='${ctx}/salebill/'+type+'/list?recordStatus=1&taskFilter=1';
  		    	}
  		    	$('#billList').jqGrid("setGridParam",{
  		    		datatype:function(postdata){
  						$.ajax({
  							url: url,
  							type:"get",
  							data:postdata,
  					        dataType:"json",
  					        complete: function(data,stat){
  					        	if(value==53){
  					        		var rows=data.responseJSON.rows;
  					        		for(var i=0;i<rows.length;i++){
  					        			rows[i].billType=53;
  					        		}
  					        	}
  					        	$("#billList")[0].addJSONData(data.responseJSON);
  					        }
  						});
  					},
  		    	}).trigger("reloadGrid");
  		    }
    	});	 
    }
    
   function addRelations(){
	   var billRows=$("#relationList").getRowData();
       var rows=$('#billList').getGridParam('selarrrow');
       var row="";
       outer:for(var i=0;i<rows.length;i++){
    	   row=$("#billList").jqGrid("getRowData",rows[i]);
    	   for(var j=0;j<billRows.length;j++){
    		   if(billRows[j].billId==row.fid){
    			   continue outer;
    		   }
    	   }
    	   row.billId=row.fid;
    	   row.billSign=row.billType;
    	   row.billName=row.code;
    	   console.log(row);
    	   $("#relationList").addRowData(row.fid,row,"last");
       }
       var billRows=$("#relationList").getRowData();
       var vos=[];
	   var vo={planId:"${task.planId}",planName:$("#name_plan").textbox("getValue"),taskId:"${task.fid}",taskName:$("#name_task").textbox("getValue")};
	   if(billRows.length>0){
		   for(var i=0;i<billRows.length;i++){
			   vo.billId=billRows[i].billId;
   			   vo.billName=billRows[i].billName;
   			   vo.billSign=billRows[i].billSign;
   			   vo.totalAmount=billRows[i].totalAmount;
   			   vos.push(vo);
   			   vo={planId:"${task.planId}",planName:$("#name_plan").textbox("getValue"),taskId:"${task.fid}",taskName:$("#name_task").textbox("getValue")};
		   }
	   }
       $.ajax({
			type : 'post',
			url : '${ctx}/flow/taskBill/relateTaskBill',
			data : {vos:JSON.stringify(vos)},
			dataType : 'json',
			success : function(data) {
				if(data.returnCode == '0'){
					$.fool.alert({msg:"关联成功！"});
					$("#relationList").trigger("reloadGrid");
				}else{
					$.fool.alert({msg:data.message});
					$("#relationList").trigger("reloadGrid");
				}
			},
   		error:function(){
   			$.fool.alert({msg:"单据关联失败，请稍后再试。"});
   		}
       });
       $("#pop-win").window("close");
   };
    function delRelations(rowId,fid){
    	$.ajax({
    		type : 'post',
			url : '${ctx}/flow/taskBill/delete',
			data : {id:rowId},
			dataType : 'json',
			success : function(data) {
				if(data.returnCode == '0'){
					$.fool.alert({msg:"删除成功！"});
					$("#relationList").trigger("reloadGrid");
				}else{
					$.fool.alert({msg:data.message});
					
				}
			},
			error:function(){
				$.fool.alert({msg:"删除关联单据失败，请稍后再试。"});
			}
    	});
    	/* $("#relationList").delRowData(rowId); */
    }
    
    function saver(){
    	var attachs=$(".attachName_task");
		var param=$('#taskForm').serialize();
		var attachIds="";
		for(var i=0;i<attachs.length;i++){
			if(i==0){
				attachIds=$(attachs[0]).attr("id");
			}else{
				attachIds=attachIds+","+$(attachs[i]).attr("id");
			}
		}
		param=param+"&attachIds="+attachIds;
		if($("#level_task").val()==1){
			$("#parentId_task").removeAttr("name");
		}
		$.ajax({
			type : 'post',
			url : '${ctx}/flow/task/save',
			data : param,
			dataType : 'json',
			success : function(data) {
				if(data.returnCode == '0'){
					$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
						$("#task-list").load("${ctx}/timeAxis/taskList",{planId:$("#planId_task").val(),planStatus:"${param.planStatus}"});
						$("#taskDetail").slideUp("",function(){
							var inputs=$("#taskDetail").find(".dhxDiv");
							$("#taskDetail").form("clear");
							for(var i=0;i<inputs.length;i++){ 
								inputs[i].comboObj.setComboText("");
							}
							$("#planId_task").val("${param.planId}");
							setNode();
						}); 
						$("#planDetail").load("${ctx}/timeAxis/planDetail",{id:$("#planId_task").val()},function(){});
						$("#saveTask").removeAttr("disabled");
					}});
				}else{
					$.fool.alert({msg:data.message});
					$("#saveTask").removeAttr("disabled");
				}
			},
    		error:function(){
    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
    			$("#saveTask").removeAttr("disabled");
    		}
		});
    }
    
    if($("#status_task").combobox("getValue")!=3&&$("#status_task").combobox("getValue")!=6){
    	$("#openBillWin").show();
    }else{
    	$("#openBillWin").hide();
    }
    
    function openBill(billType,billId,text){
    	var url='/warehouse/'+billType+'/manage?billId='+billId;
    	if(billType=="fyd"){
    		url="/costBill/manage?billId="+billId;
    	}else if(billType=="skd"){
    		url="/receiveBill/manage?billId="+billId;
    	}else if(billType=="fkd"){
    		url="/payBill/manage?billId="+billId;
    	}else if(billType=="fysqd"){
    		url="/expenApplyBill/manage?billId="+billId;
    	}else if(billType=="cgfld"){
    		url="/purchaseRebBill/manage?billId="+billId;
    	}else if(billType=="xsfld"){
    		url="/salesRebateRebBill/manage?billId="+billId;
    	}
    	parent.kk(url,text);
    }
</script>
</body>
</html>