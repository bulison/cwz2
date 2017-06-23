<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<style>
.validatebox-invalid{
    border-color: #ffa8a8 !important;
    background-color: #FFE1E1 !important;
}
.form p textarea, .form1 p textarea {
    width:865px !important;
    height:60px !important;
}
</style>
<body>
        <div id="planDetail_layout">   
          <div id="planCenter">
            <form class="form1" id="planForm">
              <input class="textBox" id="name_plan" name="name" value="${plan.name}"/>
              <p style="margin-left: 450px;"><font>状态：</font><input class="textBox" id="status_plan" readonly="readonly"/></p>
              <hr/>
              <input id="fid_plan" name="fid" type="hidden" value="${plan.fid}"/>
              <input id="updateTime_plan" name="updateTime" type="hidden" value="${plan.updateTime}"/>
              <input id="initiaterId_plan" name="initiaterId" type="hidden" value="${plan.initiaterId}"/>
              <input id="principalerId_plan" name="principalerId" type="hidden" value="${plan.principalerId}"/>
              <input id="auditerId_plan" name="auditerId" type="hidden" value="${plan.auditerId}"/>
              <p class="hideInEdit"><font>计划模板：</font><input class="textBox" id="planTemp_plan" name="planTemplateId" value=""/></p>
              <p><font><em>*</em>计划类型：</font><input class="textBox" id="planType_plan" name="planType" value="${plan.planType}"/></p>
              <p><font><em>*</em>计划编号：</font><input class="textBox" id="code_plan" name="code" value="${plan.code}"/></p>
              <p><font><em>*</em>预计开始时间：</font><input class="textBox" id="antipateStartTime_plan" name="antipateStartTime"/></p>
              <p><font><em>*</em>预计结束时间：</font><input class="textBox" id="antipateEndTime_plan" name="antipateEndTime"/></p>
              <p><font>实际开始时间：</font><input class="textBox" id="actualStartTime_plan" readonly="readonly"/></p>
              <p><font>实际结束时间：</font><input class="textBox" id="actualEndTime_plan" readonly="readonly"/></p>
              <p><font><em>*</em>计划级别：</font><input class="textBox" id="planLevelId_plan" name="planLevelId" value="${plan.planLevelId}"/></p>
              <p><font><em>*</em>保密级别：</font><input class="textBox" id="securityLevelId_plan" name="securityLevelId" value="${plan.securityLevelId}"/></p>
              <p><font>预计金额：</font><input class="textBox" id="estimatedAmount_plan" name="estimatedAmount" value="${plan.estimatedAmount}" readonly="readonly"/></p>
              <%-- <p><font>实际金额：</font><input class="textBox" id="realAmount_plan" name="realAmount" value="${plan.realAmount}" readonly="readonly"/></p> --%>
              <p><font><em>*</em>发起人：</font><input class="textBox" id="initiaterName_plan" name="initiaterName" value="${plan.initiaterName}"/></p>
              <p><font><em>*</em>责任人：</font><input class="textBox" id="principalerName_plan" name="principalerName" value="${plan.principalerName}"/></p>
              <p><font><em>*</em>审核人：</font><input class="textBox" id="auditerName_plan" name="auditerName" value="${plan.auditerName}"/></p>
              <p><font>通知方式：</font><input type="checkbox" id="sendEmail_plan" name="sendEmail" value="1">邮件 <input type="checkbox" id="sendPhoneMsg_plan" name="sendPhoneMsg" value="1">短信</p>
              <br/><p><font style="vertical-align: top;">描述：</font><input class="textBox" id="describe_plan" name="describe" value="${plan.describe}"/></p>
              <br/><p><font>附件：</font><div id="attachText_plan"><span id="addAttach_plan" title="添加附件,附件格式须为：pdf,jpeg,jpg,bmp,png,ppt,xls,xlsx,doc,docx,zip,rar,txt"></span></div></p>
              <hr/>
              <div id="planBtn">
                <!-- <input id="editPlan" type="button" class="btn-green4 btn-xs" value="修改"/> -->
                <input id="cancelPlan" type="button" class="btn-green4 btn-xs" style="display: none;" value="取消"/>
                <input id="savePlan" type="button" class="btn-orange3 btn-xs" style="display: none;" value="保存"/>
                <!-- <input id="deletePlan" type="button" class="btn-green4 btn-xs" value="删除"/>
                <input id="rankPlan" type="button" class="btn-green4 btn-xs" value="评分"/>
                <input id="handUpPlan" type="button" class="btn-green4 btn-xs" value="提交"/>
                <input id="auditPlan" type="button" class="btn-green4 btn-xs" value="审核"/>
                <input id="stopPlan" type="button" class="btn-orange3 btn-xs" value="终止"/>
                <input id="completePlan" type="button" class="btn-orange3 btn-xs" value="完成"/> -->
                <fool:flowTag planId="${plan.fid}" taskId="" type="plan"></fool:flowTag>
              </div>
            </form>
          </div>
          
          <div id="planSouth">
          </div>   
        </div> 
</body>
<script>
var attachText="添加附件,附件格式须为：pdf,jpeg,jpg,png,ppt,bmp,xls,xlsx,doc,docx,zip,rar,txt";
/* if("${plan.fid}"){
	var minStart=getChildMinStart("${plan.fid}");
	var maxEnd=getChildMaxEnd("${plan.fid}");
} */
var planTemp2='';
var planTemp="";
if("${plan.status}"==105){
	$.ajax({
		url:"${ctx}/flow/plan/getRecordByPlan?num="+Math.random(),
		async:false,
		data:{planId:"${plan.fid}",triggerType:41},
		success:function(data){
			$("#completePlanDes").find("textarea").text(data.describe);
		}
	});
	$("#completePlanMsg").show();
}
$.ajax({
	url:"${ctx}/flow/planTemplate/queryAll?num="+Math.random(),
	async:false,
	data:{},
	success:function(data){
		planTemp2=data;
		planTemp=formatData(data,'fid');
	}
});  
$.ajax({
	url:"${ctx}/flow/plan/showAttach?num="+Math.random(),
	async:false,
	data:{planId:"${plan.fid}"},
	success:function(data){
		for(var i=0;i<data.length;i++){
			$("#addAttach_plan").before("<input class='attachName_plan' id='"+data[i].id+"' readonly='readonly' name='file' value='"+data[i].name+"' disabled='disabled' saved='true'></input><a class='delAttach_plan'>x</a>");
			ajaxUpload($("#addAttach_plan").prev().prev(),$("#addAttach_plan").prev().prev(),{triggerType:"plan40"});
		}
	}
});
if("${plan.sendEmail}"==1){
	$("#sendEmail_plan").attr("checked","checked");
}
if("${plan.sendPhoneMsg}"==1){
	$("#sendPhoneMsg_plan").attr("checked","checked");
}
var planTemplateId= $("#planTemp_plan").fool("dhxCombo",{
	width:130,
	height:32,
	data:planTemp,
    clearOpt:false,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	toolsBar:{
		name:"计划模板",
        addUrl:"/flow/planTemplate/manage",
        refresh:true
	},
	novalidate:true,
	editable:false,
	focusShow:true,
	onChange:function(value,text){
		var record = planTemplateId.getSelectedText();//获取选中行数据
		if(record.fid){
			$("#planTemp_plan").attr("days",record.days);
	        $("#describe_plan").textbox("setValue",record.describe);
	        planLevelName.setComboValue(record.taskLevelId);
		}else{
			planTemplateId.setComboText("");
			planTemplateId.setComboValue("");
		}
	},
});
if(lsread==1){
	planTemplateId.disable()
}else{
	planTemplateId.enable()
}
	
var planTypeValue='';
$.ajax({
	url:"${ctx}/flow/plan/queryPlanTypes",
	async:false,		
	success:function(data){
		data=JSON.parse(data);
		planTypeValue=formatData(data,'id');	
	}
});
var planTypeName= $("#planType_plan").fool("dhxCombo",{
	width:130,
	height:32,
	data:planTypeValue,
	setTemplate:{
		input:"#text#",
		option:"#text#"
	},
	editable:false,
	focusShow:true,		 
});
$("#planType_plan").parent().hide()

$("#name_plan").textbox({
	required:true,
    novalidate:true,
    missingMessage:"该项必填",
    width:221,
    height:30,
    prompt:"请输入计划名称",
    validType:'maxLength[20]'
});
$("#name_plan").textbox("textbox").css("cssText","color:#00A8FF;margin-left:20px;font-size: 16px;font-weight: bold");
$("#code_plan").textbox({
	required:true,
    novalidate:true,
    missingMessage:"该项必填",
    width:130,
    height:30,
});
$("#antipateStartTime_plan").datebox({
	required:true,
    novalidate:true,
    missingMessage:"该项必填",
    editable:false,
    width:130,
    height:30,
    value:"${plan.antipateStartTime}",
    onSelect:function(){
    	/* var date=$("#antipateStartTime_plan").datebox("getValue");
    	var dateStr=date.split("-");
    	var dateNum=dateStr[0]+dateStr[1]+dateStr[2];
    	var minStartStr="";
    	var minStartNum="";
    	if(minStart){
    		minStartStr=minStart.split("-");
        	minStartNum=minStartStr[0]+minStartStr[1]+minStartStr[2];
    	}
    	if(minStartNum&&dateNum>minStartNum){
    		$("#antipateStartTime_plan").datebox("showPanel");
    		$("#antipateStartTime_plan").datebox("clear");
    		return ;
    	} */
    	if($("#planTemp_plan").attr("days")){
    		var endDays=$("#planTemp_plan").attr("days")-1;
    		var nowdate=$("#antipateStartTime_plan").datebox("getText");
    		var datearr=nowdate.split("-");
    		var a=new Date(datearr[0],datearr[1]-1,datearr[2]);
    		a=a.valueOf();
    		a=a+endDays*24*60*60*1000;
    		a=new Date(a);
    		$("#antipateEndTime_plan").datebox("setValue",a.getFullYear() + "-" + (a.getMonth() + 1) + "-" + a.getDate());
    	}
    }
});
    if(lsread==1){
    	var ls=JSON.parse(localStorage.getItem("planGoods"));
    	var lastestDate=ls[0].transportDate;
    	var lastestStr=ls[0].transportDate.split("-").join("");
    	for(var i=1;i<ls.length;i++){
    		if(ls[i].transportDate.split("-").join("")<=lastestDate){
    			lastestStr=ls[0].transportDate.split("-").join("");
    			lastestDate=ls[0].transportDate;
    		}
    	}
    	$("#antipateStartTime_plan").datebox({
        	onSelect:function(){
        		var date=$("#antipateStartTime_plan").datebox("getValue");
        		var dateStr=date.split("-").join("");
        		if(lastestStr&&dateStr>lastestStr){
        			$("#antipateStartTime_plan").datebox("showPanel");
        			$("#antipateStartTime_plan").datebox("clear");
        			return ;
        		}
        	}
        });
    	$("#antipateStartTime_plan").datebox("textbox").tooltip({
        	showDelay:200,
        	hideDelay:0,
        	position: 'right',
        	content: '<span style="color:#fff">开始日期必须早于'+lastestDate+'</span>',
        	onShow: function(){
        		$(this).tooltip('tip').css({
        			backgroundColor: '#666',
        			borderColor: '#666'
        		});
        	}
        });
    }/* else{
    	if("${plan.fid}"){
    		$("#antipateStartTime_plan").datebox("textbox").tooltip({
            	showDelay:200,
            	hideDelay:0,
            	position: 'right',
            	content: '<span style="color:#fff">开始日期必须早于'+getChildMinStart("${plan.fid}")+'</span>',
            	onShow: function(){
            		if($("#actualStartTime_plan").parent().css("display")!="none"){
            			$("#antipateStartTime_plan").datebox("textbox").tooltip("hide");
            		}
            		$(this).tooltip('tip').css({
            			backgroundColor: '#666',
            			borderColor: '#666'
            		});
            	}
            });
    	}
    } */
    
    $("#antipateEndTime_plan").datebox({
    	required:true,
    	novalidate:true,
    	missingMessage:"该项必填",
    	editable:false,
    	width:130,
    	height:30,
    	value:"${plan.antipateEndTime}",
    	validType:'compareDate["#antipateStartTime_plan"]',
    	onSelect:function(){
    		/* var date=$("#antipateEndTime_plan").datebox("getValue");
    		var dateStr=date.split("-");
    		var dateNum=dateStr[0]+dateStr[1]+dateStr[2];
    		var maxEndStr="";
    		var maxEndNum="";
    		if(maxEnd){
    			maxEndStr=maxEnd.split("-");
        		maxEndNum=maxEndStr[0]+maxEndStr[1]+maxEndStr[2];
    		}
    		
    		if(maxEndNum&&dateNum<maxEndNum){
    			$("#antipateEndTime_plan").datebox("showPanel");
    			$("#antipateEndTime_plan").datebox("clear");
    			return ;
    		} */
    	}
    });
    /* if("${plan.fid}"){
    	 $("#antipateEndTime_plan").datebox("textbox").tooltip({
    	    	showDelay:200,
    	    	hideDelay:0,
    	    	position: 'right',
    	    	content: '<span style="color:#fff">结束日期必须晚于'+getChildMaxEnd("${plan.fid}")+'</span>',
    	    	onShow: function(){
    	    		if($("#actualEndTime_plan").parent().css("display")!="none"){
    	    			$("#antipateEndTime_plan").datebox("textbox").tooltip("hide");
    	    		}
    	    		$(this).tooltip('tip').css({
    	    			backgroundColor: '#666',
    	    			borderColor: '#666'
    	    		});
    	    	}
    	    });
    } */
    $("#actualStartTime_plan").datebox({
		editable:false,
    	width:130,
    	height:30,
    	value:"${plan.actualStartTime}",
    	readonly:true,
    	hasDownArrow:false,
	});
	$("#actualEndTime_plan").datebox({
		editable:false,
    	width:130,
    	height:30,
    	value:"${plan.actualEndTime}",
    	readonly:true,
    	hasDownArrow:false,
	});
	if("${plan.status}"!="100"&&"${plan.status}"!=""){
		$("#actualStartTime_plan").parent().show();
    	$("#actualEndTime_plan").parent().show();
    }else{
    	$("#actualStartTime_plan").parent().hide();
    	$("#actualEndTime_plan").parent().hide();
    }
	
     var planTypeValue='';	
	$.ajax({
		url:"${ctx}/flow/taskLevel/queryAll",
		async:false,		
		success:function(data){
			planTypeValue=formatData(data,'fid');	
	    }
		});
	var planLevelName= $("#planLevelId_plan").fool("dhxCombo",{
		  width:130,
		  height:32,
        clearOpt:false,
		  data:planTypeValue,
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
        toolsBar:{
            name:"计划级别",
            addUrl:"/flow/taskLevel/manage",
            refresh:true
        },
        required:true,
		  novalidate:true,
		  editable:false,
		  focusShow:true,
		  onChange:function(value,text){
			  var record = planLevelName.getSelectedText();//获取选中行数据
			  if(record.fid===null){
				  planLevelName.setComboText("");
				  planLevelName.setComboValue("");
	    		}
		  },
		  onLoadSuccess:function(){
			  /* if(planTypeValue.length<=0&&($("#planLevelId_plan").next())[0].comboObj){
				  ($("#planLevelId_plan").next())[0].comboObj.setComboText("<a onclick='jumpPlanLevel()'>暂无数据，请先添加数据</a>");
				  ($("#planLevelId_plan").next())[0].comboObj.setComboValue('');
			  } */
			  var option= ($("#planLevelId_plan").next())[0].comboObj.getOption("${plan.planLevelId}");
			  if(!option){
				  ($("#planLevelId_plan").next())[0].comboObj.addOption([["${plan.planLevelId}","${plan.planLevelName}"]]);
			  }
		  }
	});	 
    
    
    var securityLevelId='';	
	$.ajax({
		url:"${ctx}/flow/security/queryAll",
		async:false,		
		success:function(data){	
			securityLevelId=formatData(data,'fid');	
	    }
		});
    var securityLevelIdName= $("#securityLevelId_plan").fool("dhxCombo",{
		  width:130,
		  height:32,
        clearOpt:false,
		  data:securityLevelId,
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
			  var record = securityLevelIdName.getSelectedText();//获取选中行数据
			  if(record.fid===null){
				  securityLevelIdName.setComboText("");
				  securityLevelIdName.setComboValue("");
	    		}
		  },
		  onLoadSuccess:function(obj){
			  /* if(securityLevelId.length<=0&&($("#securityLevelId_plan").next())[0].comboObj){
				  ($("#securityLevelId_plan").next())[0].comboObj.setComboText("<a onclick='jumpSecLevel()'>暂无数据，请先添加数据</a>");
				  ($("#securityLevelId_plan").next())[0].comboObj.setComboValue("");
			  } */
			  $('#dhxDiv_securityLevelId_plan').mouseover(function(e){//保密级别提示
				  tooltipbm(this);
			  });
			  var option= ($("#securityLevelId_plan").next())[0].comboObj.getOption("${plan.securityLevelId}");
			  if(!option){
				  ($("#securityLevelId_plan").next())[0].comboObj.addOption([["${plan.securityLevelId}","${plan.securityLevelName}"]]);
			  }
		  }
	});	 
    function tooltipbm(e){//保密级别提示
    	$(e).tooltip({
    	    position: 'right',
    	    content: '<span style="color:#fff">级别用数字表示，数字越大级别越高。<br/>计划和事件中的保密级别用来和人员的<br/>保密级别（系统管理员进行配置）进行<br/>比较，人员的保密级别大于计划和事件<br/>的保密级别时，可以操作本计划和事件。</span>',
    	    onShow: function(){
    			$(e).tooltip('tip').css({
    				backgroundColor: '#666',
    				borderColor: '#666'
    			});
    	    }
    	});
    }  
    $("#estimatedAmount_plan").textbox({
    	width:130,
    	height:30,
    	disabled:true,
    	validType:['balanceAmount','numMaxLength[10]']
    });
    /* $("#realAmount_plan").textbox({
    	editable:false,
    	width:130,
    	height:30,
    	disabled:true,
    	validType:['balanceAmount','numMaxLength[10]']
    }); */
    var boxWidth=130,boxHeight=31;//统一设置输入框大小
    var initiaterValue='';	
    $.ajax({
    	url:getRootPath()+'/userController/vagueSearch',
    	async:false,		
    	success:function(data){		  	
    		initiaterValue=formatData(data,'fid');
        }
    });
    var initiaterName = $('#initiaterName_plan').fool('dhxComboGrid',{
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
              name:"发起人",
              addUrl:"/userController/userMessageUI",
              refresh:true
          },
        onChange:function(value,text){
        	if(!value){
        		($('#initiaterName_plan').next())[0].comboObj.setComboText("");
        	}else{
        		var row=initiaterName.getSelectedText();//获取选中行数据   	
        		$("#initiaterId_plan").val(row.fid);
        		$("#initiaterName_plan").val(row.userName);
        	}
    	},
    });
    var princiName = $('#principalerName_plan').fool('dhxComboGrid',{
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
        		($('#principalerName_plan').next())[0].comboObj.setComboText("");
        	}else{
        		var row=princiName.getSelectedText();//获取选中行数据 
        		$("#principalerId_plan").val(row.fid);
        		$("#principalerName_plan").val(row.userName).focus();
        	}
    	},
    });
    var auditerName = $('#auditerName_plan').fool('dhxComboGrid',{
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
            name:"审核人",
            addUrl:"/userController/userMessageUI",
            refresh:true
        },
        onChange:function(value,text){
        	if(!value){
        		($('#auditerName_plan').next())[0].comboObj.setComboText("");
        	}else{
        		var row=auditerName.getSelectedText();//获取选中行数据
      		    $("#auditerId_plan").val(row.fid);
    			$("#auditerName_plan").val(row.userName).focus();
        	}
    	},
    });
    
    $("#attachId_plan").textbox({
    	width:130,
    	height:30
    });
    $("#status_plan").combobox({
    	editable:false,
    	width:130,
    	height:30,
    	disabled:true,
    	hasDownArrow:false,
    	data:[{value:"100",text:"草稿"},{value:"101",text:"已提交待审核"},{value:"102",text:"已审核办理中"},{value:"103",text:"已延迟"},{value:"104",text:"已终止"},{value:"105",text:"已完成"}],
        value:"${plan.status}",
    });
    $("#describe_plan").textbox({
    	width:874,
    	height:60,
    	multiline:true,
    	validType:'maxLength[200]'
    });
    initialPlan();
    
    function initialPlan(){
    	if(!"${plan.fid}"){//新增计划
    		$("#planDetail").slideDown();
    		$("#planDetailTitle").text("新增计划");
    		//按钮初始化
    		$("#savePlan").css("display","inline-block");
    	}else{
    		$("#planDetail").slideUp();
    		exitEditPlan();
    	}
    }

    $("#addAttach_plan").click(function(){
    	if($(".attachName_plan").length>=5){
    		$.fool.alert({msg:"最多上传5个附件"});
    		return;
    	}
    	$(this).before("<input class='attachName_plan' id='' readonly='readonly' name='file' value='请选取附件'></input><a class='delAttach_plan'>x</a>");
		var inputer=ajaxUpload($(this).prev().prev(),$(this).prev().prev(),{triggerType:"plan40"});
    });
  
    $("#editPlan").click(function(){//注释修改
    	editPlan();
    });
    
    //取消按钮
    $("#cancelPlan").click(function(){
    	$('#planForm').form("reset");
    	($("#initiaterName_plan").next())[0].comboObj.setComboValue("${plan.initiaterId}");
    	($("#principalerName_plan").next())[0].comboObj.setComboValue("${plan.principalerId}");
    	($("#auditerName_plan").next())[0].comboObj.setComboValue("${plan.auditerId}");
    	($("#planLevelId_plan").next())[0].comboObj.setComboValue("${plan.planLevelId}");
    	($("#securityLevelId_plan").next())[0].comboObj.setComboValue("${plan.securityLevelId}");
    	$('#planForm').form('validate');
    	exitEditPlan();
    });
    //保存按钮
    $("#savePlan").click(function(){
    	/* if(!initiaterName.getSelectedText()){
    		initiaterName.setComboText("");
    	}
        if(!auditerName.getSelectedText()){
        	auditerName.setComboText("");
    	}
        if(!princiName.getSelectedText()){
        	princiName.setComboText("");
        } */
    	if(auditerName.getOption($("#auditerId_plan").val())){
			var auditerNameLevel=auditerName.getOption($("#auditerId_plan").val()).text.securityLevel;
		}else{
			auditerName.setComboText("");
		}
		if(princiName.getOption($("#principalerId_plan").val())){
			var principalerLevel=princiName.getOption($("#principalerId_plan").val()).text.securityLevel;
		}else{
			princiName.setComboText("");
		}
    	$('#planForm').form('enableValidation');
    	if($('#planForm').form('validate')){
    		$("#savePlan").attr("disabled","disabled");
    		var sLevel="";
    		if(securityLevelIdName.getSelectedValue()){
    			sLevel=securityLevelIdName.getOption(securityLevelIdName.getSelectedValue());
    		}
    		if(sLevel){
    			if(auditerNameLevel&&auditerNameLevel<sLevel.text.level){
    				$.fool.confirm({title:'确认',msg:'所选审核人的保密级别比所选保密级别低，确定保存？',fn:function(r){
    					if(!r){
    						return false;
    					}else{
    						planSaver();
    					}
    				}})
    			}else if(principalerLevel&&principalerLevel<sLevel.text.level){
    				$.fool.confirm({title:'确认',msg:'所选负责人的保密级别比所选保密级别低，确定保存？',fn:function(r){
    					if(!r){
    						return false;
    					}else{
    						planSaver();
    					}
    				}})
    			}else{
    				planSaver();
    			}
    		}else{
    			planSaver();
    		}
    	}
    });
    //删除按钮
    $("#deletePlan").click(function(){//注释删除
    	$.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
   		 if(r){
   			 $.ajax({
   					type : 'post',
   					url : '${ctx}/flow/plan/delete',
   					data : {fid :"${plan.fid}"},
   					dataType : 'json',
   					success : function(data) {	
   						if(data.returnCode == '0'){
   							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
   								//返回列表页面
   								var src="/flow/plan/listPage";
   								var text='计划管理';
   								parent.kk(src,text);
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
    //提交按钮
    $("#handUpPlan").click(function(){//注释提交
    	if($("#handUpForm").length<=0){
    		$("#planSouth").html('<form class="form1" id="handUpForm"><div class="closeBar"><span class="closeBtn" onclick="closePlanSouth()"></span></div><input name="id" type="hidden" value="${plan.fid}"/><p><font><em>*</em>提交信息描述：</font><textarea class="textBox easyui-validatebox" name="describe" data-options="validType:\'maxLength[200]\'" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="handUpPlanFn(this)" value="提交"/></p></form>');
    		$("#handUpForm textarea").validatebox({
    			required:true,
    			novalidate:true
    		});
    	}
    	$("#planSouth").slideDown("",function(){
    		setNode();
    	});
    });
    $("#auditPlan").click(function(){//注释审核
    	if($("#auditPlanForm").length<=0){
    		$("#planSouth").html('<form class="form1" id="auditPlanForm"><div class="closeBar"><span class="closeBtn" onclick="closePlanSouth()"></span></div><input name="id" type="hidden" value="${plan.fid}"/><p><font><em>*</em>审核意见：</font><input class="textBox" id="auditResult" name="auditResult" /></p><br/><p><font>备注：</font><textarea class="textBox easyui-validatebox" name="describe" data-options="validType:\'maxLength[200]\'" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="auditPlanFn(this)" value="审核"/></p></form>');
    		$("#auditPlanForm textarea").validatebox({
    			
    		});
    		$("#auditResult").combobox({
    			required:true,
    			novalidate:true,
    			editable:false,
    	    	width:130,
    	    	height:30,
    	    	data:[{value:"0",text:"审核通过"},{value:"1",text:"拒绝，返回发起人"},{value:"3",text:"终止"}]
    		});
    	}
    	$("#planSouth").slideDown("",function(){
    		setNode();
    	});
    });
    $("#stopPlan").click(function(){//注释终止
    	if($("#stopForm").length<=0){
    		$("#planSouth").html('<form class="form1" id="stopForm"><div class="closeBar"><span class="closeBtn" onclick="closePlanSouth()"></span></div><input name="id" type="hidden" value="${plan.fid}"/><p><font><em>*</em>提交信息描述：</font><textarea class="textBox easyui-validatebox" name="describe" data-options="validType:\'maxLength[200]\'" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="stopPlanFn(this)" value="终止"/></p></form>');
            $("#stopForm textarea").validatebox({
    			
    		});
    	}
    	$("#planSouth").slideDown("",function(){
    		setNode();
    	});
    });
    $("#rankPlan").click(function(){//注释评分
    	if($("#rankPlanForm").length<=0){
    		$("#planSouth").html('<form class="form1" id="rankPlanForm" style="background-color: #F2F2F2"><div class="closeBar"><span class="closeBtn" onclick="closePlanSouth()"></span></div><input name="businessId" type="hidden" value="${plan.fid}"/><p><font><em>*</em>评分：</font><div id="rank-plan"></div></p><p><font><em>*</em>评论：</font><textarea class="textBox easyui-validatebox" name="comment" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="rankPlanFn(this)" value="提交"/></p></form>');
    		$("#rankPlanForm textarea").validatebox({
    			required:true,
    			novalidate:true,
    			validType:'maxLength[200]'
    		});
    		$("#rank-plan").raty({
    			score: 3, 
    			scoreName: 'rank',
    			path: '${ctx}/resources/js/raty/img', 
    		});
    	}
    	$("#planSouth").slideDown("",function(){
    		setNode();
    	});
    });
    $("#completePlan").click(function(){//注释完成
    	if($("#completeForm").length<=0){
    		$("#planSouth").html('<form class="form1" id="completeForm"><div class="closeBar"><span class="closeBtn" onclick="closePlanSouth()"></span></div><input name="id" type="hidden" value="${plan.fid}"/><p><em>*</em><font>完成信息描述：</font><textarea class="textBox easyui-validatebox" name="describe" data-options="validType:\'maxLength[200]\'" style="width:700px !important"></textarea></p><br/><p style="position:relative;left:642px"><font></font><input type="button" class="btn-orange3 btn-xs" onclick="completePlanFn(this)" value="完成"/></p></form>');
    		$("#completeForm textarea").validatebox({
    			required:true,
    			novalidate:true
    		});
    	}
    	$("#planSouth").slideDown("",function(){
    		setNode();
    	});
    	/* $.ajax({
				type : 'post',
				url : '${ctx}/flow/plan/complete',
				data : {id :"${plan.fid}"},
				dataType : 'json',
				success : function(data) {	
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
							window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
						}});
					}else{
						$.fool.alert({msg:data.message});
					}
	    		},
	    		error:function(){
	    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
	    		}
			}); */
    });
    
    function closePlanSouth(){
    	$("#planSouth").slideUp("",function(){
    		setNode();
    	});
    };
    //进入编辑状态
    function editPlan(){
    	$("#planDetailTitle").text("修改计划");
    	if($("#fid_plan").val()){
    		$(".hideInEdit").hide();
    	}else{
    		$(".hideInEdit").show();
    	}
    	$("#actualStartTime_plan").parent().hide();
    	$("#actualEndTime_plan").parent().hide();
    	$("#addAttach_plan").show();
    	$(".delAttach_plan").show();
    	$("#planForm").find(":checkbox").removeAttr("disabled");
    	$("#planBtn input").css("display","none");
    	$("#cancelPlan").css("display","inline-block");
    	$("#savePlan").css("display","inline-block");
    	$("#planForm").find("input").removeAttr("readonly");
		$("#planForm span.textbox").css("cssText","border-width:1px !important");
		$("#planForm .textbox-addon-right").css("display","inline-block");
		$("#planForm .combo-f").combo("readonly",false);
		$("#planForm .textbox-f").textbox("readonly",false);
		if(!auditerName.getOption($("#auditerId_plan").val())){
			auditerName.setComboText("");
		}
		if(!princiName.getOption($("#principalerId_plan").val())){
			princiName.setComboText("");
		}
		$("#planSouth").slideUp("",function(){
    		setNode();
    	});
		if(lsread==1){
			planTemplateId.disable()
		}else{
			planTemplateId.enable()
		}
		 planTypeName.enable(); 
		 planLevelName.enable(); 
		 securityLevelIdName.enable();
		 initiaterName.enable();
		 princiName.enable();
		 auditerName.enable();  
		 $('#planForm .dhxcombo_select_button').css("display","block");	    
		 $('#planForm div.dhxcombo_material').css('border','1px solid #dfdfdf');
		 $('#planForm .dhxcombo_input').css('background','none'); 
		 
    }
    //退出编辑状态
    function exitEditPlan(){
    	if(!auditerName.getOption($("#auditerId_plan").val())){
			auditerName.setComboText("");
			$('#planForm').form('enableValidation');
			return false;
		}
		if(!princiName.getOption($("#principalerId_plan").val())){
			princiName.setComboText("");
			$('#planForm').form('enableValidation');
			return false;
		}
    	$("#planDetailTitle").text("计划详情");
    	$(".hideInEdit").hide();
    	if("${plan.status}"!="100"&&"${plan.status}"!=""){
    		$("#actualStartTime_plan").parent().show();
        	$("#actualEndTime_plan").parent().show();
        }else{
        	$("#actualStartTime_plan").parent().hide();
        	$("#actualEndTime_plan").parent().hide();
        }
    	var attachs=$(".attachName_plan");
    	for(var i=0;i<attachs.length;i++){
    		if($(attachs[i]).attr("saved")!="true"){
    			$(attachs[i]).next().remove();
    			$(attachs[i]).remove();
    		}
    	}
    	$("#addAttach_plan").hide();
    	$(".delAttach_plan").hide();
		$("#planForm").find("input").attr("readonly","readonly");
		$("#planForm").find(":checkbox").attr("disabled","disabled"); 
		$("#planBtn input").css("display","inline-block");
		$("#cancelPlan").css("display","none");
    	$("#savePlan").css("display","none");
		$("#planForm span.textbox").css("cssText","border-width:0 !important");
		$("#planForm .textbox-addon-right").css("display","none");
		$("#planForm .combo-f").combo("readonly",true);
		$("#planForm .textbox-f").textbox("readonly",true); 
	    //设置dhxComboGrid和dhxCombo不可用
		planTypeName.disable(); 
		planLevelName.disable();
		securityLevelIdName.disable();
		initiaterName.disable();
		princiName.disable();
		auditerName.disable(); 		
	    $('#planForm .dhxcombo_select_button').css("display","none");	    
	    $('#planForm div.dhxcombo_material').css('border','none');
	    $('#planForm .dhxcombo_input').css('background','none');
	    
    }
    function handUpPlanFn(e){
    	$('#handUpForm').form('enableValidation');
    	if($('#handUpForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		$.ajax({
    			type : 'post',
				url : '${ctx}/flow/plan/submit?type=0',
				data : $('#handUpForm').serialize(),
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'提交成功！',fn:function(){
							//刷新页面
							window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
						}});
					}else if(data.returnCode == '1'){
						if(data.errorCode==1){
							$.fool.confirm({title:'确认',msg:"事件数与资金计划明细数不一致，是否需要修改？",fn:function(r){
		    					if(r){
		    						coverAll();
		    					}
		    				}})
						}else if(data.errorCode==2){
							$.messager.defaults={ok:"修改",cancel:"不修改"};
							$.fool.confirm({title:'确认',msg:data.message+"是否需要修改？",fn:function(r){
		    					if(r){
		    						/* $.ajax({
		    			    			type : 'post',
		    							url : '${ctx}/flow/plan/submit?type=1',
		    							data : $('#handUpForm').serialize(),
		    							dataType : 'json',
		    							success : function(data) {
		    								if(data.returnCode == '0'){
		    									$.fool.alert({time:1000,msg:'提交成功！',fn:function(){
		    										//刷新页面
		    										window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
		    									}});
		    								}else if(data.returnCode == '1'){
		    									if(data.errorCode==1){
		    										$.fool.confirm({title:'确认',msg:"事件数与资金计划明细数不一致，是否需要修改？",fn:function(r){
		    					    					if(r){
		    					    						coverAll();
		    					    					}
		    					    				}})
		    									}else if(data.errorCode==3){
		    										$.fool.confirm({title:'确认',msg:data.message,fn:function(r){
		    					    					if(r){
		    					    						coverAll();
		    					    					}
		    					    				}})
		    									}else{
		    										$.fool.alert({msg:data.message});
		    									}
		    								}
		    							},
		    				    		error:function(){
		    				    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
		    				    		}
		    						}); */
		    						/* coverAll(); */
		    						coverAll(2);
		    					}else{
		    						$.ajax({
		    			    			type : 'post',
		    							url : '${ctx}/flow/plan/submit?type=1',
		    							data : $('#handUpForm').serialize(),
		    							dataType : 'json',
		    							success : function(data) {
		    								if(data.returnCode == '0'){
		    									$.fool.alert({time:1000,msg:'提交成功！',fn:function(){
		    										//刷新页面
		    										window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
		    									}});
		    								}else if(data.returnCode == '1'){
		    									if(data.errorCode==1){
		    										$.fool.confirm({title:'确认',msg:"事件数与资金计划明细数不一致，是否需要修改？",fn:function(r){
		    					    					if(r){
		    					    						coverAll(1);
		    					    					}
		    					    				}})
		    									}else if(data.errorCode==3){
		    										$.fool.confirm({title:'确认',msg:data.message,fn:function(r){
		    					    					if(r){
		    					    						coverAll(1);
		    					    					}
		    					    				}})
		    									}else{
		    										$.fool.alert({msg:data.message});
		    									}
		    								}
		    							},
		    				    		error:function(){
		    				    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
		    				    		}
		    						});
		    					}
							}})
							$.messager.defaults={ok:"确认",cancel:"取消"};
						}else if(data.errorCode==3){
							$.fool.confirm({title:'确认',msg:data.message,fn:function(r){
		    					if(r){
		    						coverAll();
		    					}
		    				}})
						}else{
							$.fool.alert({msg:data.message});
						}
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
    function completePlanFn(e){
    	$('#completeForm').form('enableValidation');
    	if($('#completeForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		/* $.ajax({
    			type : 'post',
				url : '${ctx}/flow/plan/audit',
				data : $('#auditPlanForm').serialize(),
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
							//刷新页面
							window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
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
    		}); */
    		/* {id :"${plan.fid}"} */
    		$.ajax({
				type : 'post',
				url : '${ctx}/flow/plan/complete',
				data : $('#completeForm').serialize(),
				dataType : 'json',
				success : function(data) {	
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
							window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
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
    }
    function auditPlanFn(e){
    	$('#auditPlanForm').form('enableValidation');
    	if($('#auditPlanForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		$.ajax({
    			type : 'post',
				url : '${ctx}/flow/plan/audit',
				data : $('#auditPlanForm').serialize(),
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
							//刷新页面
							window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
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
    function stopPlanFn(e){
    	$('#stopForm').form('enableValidation');
    	if($('#stopForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		$.ajax({
    			type : 'post',
				url : '${ctx}/flow/plan/terminate',
				data : $('#stopForm').serialize(),
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'终止成功！',fn:function(){
							//刷新页面
							window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
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
    
    function rankPlanFn(e){
    	$('#rankPlanForm').form('enableValidation');
    	if($('#rankPlanForm').form('validate')){
    		$(e).attr("disabled","disabled");
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/flow/task/addRank?type=0',
    			data : $('#rankPlanForm').serialize(),
    			dataType : 'json',
    			success : function(data) {	
    				if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
							//刷新页面
							window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
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
    
    /* function getChildMinStart(id){
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
    	});
    	return minStart;
    }
    function getChildMaxEnd(id){
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
    	});
    	return maxEnd;
    } */
    
    $("#attachText_plan").on("mouseup",".attachName_plan",function(){
    	var target=$(this);
    	if(target.attr("disabled")=="disabled"){
    		if(target.attr("id")){
    			window.location.href=encodeURI(encodeURI("${ctx}/flow/plan/download?fileId="+target.attr("id")));
    		}
    	}
    });
    $("#attachText_plan").on("mouseup",".delAttach_plan",function(){
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
    
    function planSaver(){
    	var attachs=$(".attachName_plan");
		var param=$('#planForm').serialize();
		var attachIds="";
		for(var i=0;i<attachs.length;i++){
			if(i==0){
				attachIds=$(attachs[0]).attr("id");
			}else{
				attachIds=attachIds+","+$(attachs[i]).attr("id");
			}
		}
		param=param+"&attachIds="+attachIds;
		var planGoods=localStorage.getItem("planGoods");
		if(lsread==1&&planGoods){
			$.ajax({
				url:"${ctx}/api/costAnalysisBill/genFlow?"+param,
				async:false,
				type:"put",
				data:{planGoodsJson:localStorage.getItem("planGoods")},
				success:function(data){
					if(data.returnCode==0){
						$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
							 localStorage.removeItem("planGoods");
							 window.location.href="${ctx}/flow/plan/axis?id="+data.data;
							 $("#savePlan").removeAttr("disabled");
						}});
					}else{
						$.fool.alert({time:1000,msg:data.message});
						$("#savePlan").removeAttr("disabled");
					}
			    }
			});
		}else{
			$.ajax({
    			type : 'post',
				url : '${ctx}/flow/plan/save',
				data : param,
				dataType : 'json',
				success : function(data) {
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
							//刷新页面
							if(planTemplateId.getSelectedValue()){
								window.location.href="${ctx}/timeAxis/addTaskByPlanTemplate?planId="+data.data+"&planTemplateId="+planTemplateId.getSelectedValue();
							}else{
								window.location.href="${ctx}/flow/plan/axis?id="+data.data;
							}
							$("#savePlan").removeAttr("disabled");
						}});
					}else{
						$.fool.alert({msg:data.message});
						$("#savePlan").removeAttr("disabled");
					}
				},
	    		error:function(){
	    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
	    			$("#savePlan").removeAttr("disabled");
	    		}
    		});
		}
    }
    
    function coverAll(flag){
    	var details=[];
    	var fdata={};
    	$.ajax({
    		url:'${ctx}/flow/task/queryAllByPlan',
    		data:{planId:"${plan.fid}"},
            dataType:"json",
            async: false,
            complete: function(data,stat){
            	var result=data.responseJSON;
            	for(var i=1;i<result.length;i++){
            		if(result[i].amount!=0){
            			if(result[i].amount>0){
            				details.push({paymentDate:result[i].antipateEndTime,planAmount:result[i].amount,relationSign:72,relationId:result[i].fid,recordStatus:0,paymentAmount:0,billAmount:0})
            			}else{
            				details.push({paymentDate:result[i].antipateStartTime,planAmount:result[i].amount,relationSign:72,relationId:result[i].fid,recordStatus:0,paymentAmount:0,billAmount:0})
            			}		
            		}
            	}
            }
    	});
    	if(flag){
    		$.ajax({
        		url:'${ctx}/capitalPlanDetail/queryByPlantId',
        		data:{relationId:"${plan.fid}"},
                dataType:"json",
                async: false,
                complete: function(data,stat){
                	var rows=data.responseJSON;
                	if(flag==1){
                		for(var i=0;i<rows.length;i++){
                    		details[i].paymentDate=rows[i].paymentDate;
                    	}
                	}else{
                		outer:for(var j=0;j<details.length;j++){
            				for(var i=0;i<rows.length;i++){
            					if(details[j].fid==rows[i].relationId){
            						details[j].planAmount=rows[i].planAmount;
                					continue outer;
                				}
            				}
            			}
                		/* for(var i=0;i<rows.length;i++){
                    		details[i].planAmount=rows[i].planAmount;
                    	} */
                	}
                }
        	});
    	}
    	var totalPlanAmount=0;
    	for(var i=0;i<details.length;i++){
    		if(details[i].billAmount){
    			totalPlanAmount=totalPlanAmount+parseFloat(details[i].planAmount);
    		}
    	}
    	$.ajax({
    		type : 'post',
    		async: false,
    		url : getRootPath()+"/capitalPlan/list",
    		data : {relationId :"${plan.fid}"},
    		dataType : 'json',
    		success : function(data) {
    			if(data.rows.length>0){
    				fdata=data.rows[0];
    				fdata.planAmount=totalPlanAmount;
    				fdata.billAmount=0;
    			}else{
    				fdata={relationId :"${plan.fid}",relationSign:71,calculation:1,code:$("#code_plan").val(),paymentAmount:0,paymentDate:$("#antipateEndTime_plan").datebox("getValue")};
    			}
    			fdata=$.extend(fdata,{'details':JSON.stringify(details)});
    		}
    	});
    	$.ajax({
    		type : 'post',
    		async: false,
    		url : getRootPath()+"/capitalPlan/save",
    		data :fdata,
    		dataType : 'json',
    		success : function(returnData) {
    			if(returnData.returnCode==0){
    				if(flag==2){
    					$.ajax({
			    			type : 'post',
							url : '${ctx}/flow/plan/submit?type=1',
							data : $('#handUpForm').serialize(),
							dataType : 'json',
							success : function(data) {
								if(data.returnCode == '0'){
									$.fool.alert({time:1000,msg:'提交成功！',fn:function(){
										//刷新页面
										window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
									}});
								}else if(data.returnCode == '1'){
									if(data.errorCode==1){
										$.fool.confirm({title:'确认',msg:"事件数与资金计划明细数不一致，是否需要修改？",fn:function(r){
					    					if(r){
					    						coverAll();
					    					}
					    				}})
									}else if(data.errorCode==3){
										$.fool.confirm({title:'确认',msg:data.message,fn:function(r){
					    					if(r){
					    						coverAll();
					    					}
					    				}})
									}else{
										$.fool.alert({msg:data.message});
									}
								}
							},
				    		error:function(){
				    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
				    		}
						});
    				}else{
    					$.ajax({
    		    			type : 'post',
    						url : '${ctx}/flow/plan/submit?type=1',
    						data : $('#handUpForm').serialize(),
    						dataType : 'json',
    						success : function(data) {
    							if(data.returnCode == '0'){
    								$.fool.alert({time:1000,msg:'提交成功！',fn:function(){
    									//刷新页面
    									window.location.href="${ctx}/flow/plan/axis?id=${plan.fid}";
    								}});
    							}else if(data.returnCode == '1'){
    								$.fool.alert({msg:data.message});
    							}
    						},
    			    		error:function(){
    			    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
    			    		}
        				});
    				}
    			}else{
    				$.fool.alert({msg:'操作失败！'+returnData.message});
    			}
    		}
    	});
    }
</script>
</html>