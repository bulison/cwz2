<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<c:set var="viewName" value="${param.rankFlag == 1?'评分' : param.busType == 0?'计划':'事件'}" scope="page"></c:set>
<c:set var="typeName" value="${param.busType == 0?'计划':'事件'}" scope="page"></c:set>
<title>查看${viewName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/billModel.css" />
<style>
.titleCustom{
	margin:10px 20px;
}
h2{
	font: 800 15px 宋体 !important;
    color: #ffc96c;
    margin-left: 10px;
}
h3{
	font: 800 13px 宋体 !important;
    color: #000;
}
#attachBox_task,#attachBox_plan{
    display: inline-block;
    vertical-align: text-top;
    width:862px;
    max-height:100px;
    overflow: auto;
}
div.mylist .fj,#attachBox_task .fj,#attachBox_plan .fj{
    width: 200px;
    border: 1px solid #ccc;
    height: 20px;
    padding: 0 0 10px 0;
    margin-right:20px;
    display:inline-block;
    border-radius:3px;
}
div.mylist .filename,#attachBox_task .filename,#attachBox_plan .filename{
    display: inline-block;
    font-size: 10px;
    position: absolute;
    width:90px;
    margin: 10px 10px;
    white-space:nowrap;
    text-overflow:ellipsis;
    overflow:hidden;
}
div.mylist a,#attachBox_task a,#attachBox_plan a{
	display: inline-block;
    margin-top: 5px;
    position: absolute;
    margin-left: 110px;
}
div.mylist .btn-s,#attachBox_task .btn-s,#attachBox_plan .btn-s{
	height:20px;
	line-height:20px;
}
div.mylist font,#attachBox_task font,#attachBox_plan font{
	width:auto;
}
div.mylist p,#attachBox_task p,#attachBox_plan p{
	margin-left:30px;
}
</style>
</head>
<body>
<form action="" class="bill-form myform" id="programform">
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>查看${viewName}信息</h1>
                </div>             
             </div>
	<div class="bill shadow" style="margin-top:50px;">
		<div class="in-box list2">
		<c:choose>
			<c:when test="${!empty rank}">
				<p><font>${typeName}名称:</font><input class="textBox" disabled="disabled" id="typeName_rank" name="typeName" value="${busName }"/></p>
				<p><font>评分:</font><div id="rank-task" style="display: inline-block;"></div></p>
				<p><font>评分时间:</font><input class="textBox" disabled="disabled" id="createTime_rank" name="createTime" value="${rank.createTime }"/></p>
				<p><font>评分人:</font><input class="textBox" disabled="disabled" id="creatorName_rank" name="creatorName" value="${rank.creatorName }"/></p>
				<br/>
				<p><font style="vertical-align: top;">评论:</font><textarea class="textBox" disabled="disabled" id="comment_rank" name="comment" style="resize: none;width:515px;height:90px;">${rank.comment }</textarea></p>
			</c:when>
			<c:otherwise>
			<c:choose>
			  <c:when test="${!empty plan.fid}">
			  <input id="fid_plan" name="fid" type="hidden" value="${plan.fid}"/>
              <input id="updateTime_plan" name="updateTime" type="hidden" value="${plan.updateTime}"/>
              <input id="initiaterId_plan" name="initiaterId" type="hidden" value="${plan.initiaterId}"/>
              <input id="principalerId_plan" name="principalerId" type="hidden" value="${plan.principalerId}"/>
              <input id="auditerId_plan" name="auditerId" type="hidden" value="${plan.auditerId}"/>
              <p><font>计划名称:</font><input class="textBox" disabled="disabled" id="name_plan" name="name" value="${plan.name}"/></p>
              <p><font>状态：</font><input class="textBox" id="status_plan" /></p>
              <%-- <p><font>计划类型：</font><input disabled="disabled" class="textBox" id="planType_plan" value="${plan.planType}"/></p> --%> 
              <p><font>计划编号：</font><input disabled="disabled" class="textBox" id="code_plan" name="code" value="${plan.code}"/></p>
              <p><font>计划开始时间：</font><input disabled="disabled" class="textBox" id="antipateStartTime_plan" name="antipateStartTime"/></p>
              <p><font>计划结束时间：</font><input disabled="disabled" class="textBox" id="antipateEndTime_plan" name="antipateEndTime"/></p>
              <p><font>计划级别：</font><input disabled="disabled" class="textBox" id="planLevelId_plan" name="planLevelId" value="${plan.planLevelName}"/></p>
              <p><font>保密级别：</font><input disabled="disabled" class="textBox" id="securityLevelId_plan" name="securityLevelId" value="${plan.securityLevelName}"/></p>
              <p><font>计划金额：</font><input class="textBox" disabled="disabled" id="estimatedAmount_plan" name="estimatedAmount" value="${plan.estimatedAmount}"/></p>
              <p><font>发起人：</font><input disabled="disabled" class="textBox" id="initiaterName_plan" name="initiaterName" value="${plan.initiaterName}"/></p>
              <p><font>责任人：</font><input disabled="disabled" class="textBox" id="principalerName_plan" name="principalerName" value="${plan.principalerName}"/></p>
              <p><font>审核人：</font><input disabled="disabled" class="textBox" id="auditerName_plan" name="auditerName" value="${plan.auditerName}"/></p>
              <br/><p><font>描述：</font><input disabled="disabled" class="textBox" id="describe_plan" style="width:520px;" name="describe" value="${plan.describe}"/></p>
              <br/><p><font>附件：</font><div id="attachBox_plan"></div></p>
              <br/><p><font></font><input id="jumpPlan" class="btn-green4 btn-xs" type="button" value="查看计划"/></p>
              <h2 style="margin-top:30px;">操作记录</h2> 
              <hr/>
              <c:forEach var="recordVo" items="${recordVoList}">
              <div class="mylist">
              	<h3>${recordVo.operateName}</h3>
				<p><font>创建时间：</font>${recordVo.createTime}</p>
				<p><font>描述：</font>${recordVo.describe}</p><br/>
				${!empty recordVo.fileMap?'<p><font style="vertical-align: bottom;">附件：</font></p>':''}
				<c:forEach var="file" items="${recordVo.fileMap }">
					<div class="fj">
						<span style="margin-left:5px;display: inline-block;"><img src="/ops-pc/resources/images/morefile.png" style="width:30px;height:30px;"></span>
						<span class="filename" title="${file.key}">${file.key}</span>
						<a href="${ctx}/flow/plan/download?fileId=${file.value}" class="btn-s btn-blue">下载</a>
					</div>
				</c:forEach>
			  </div>
              </c:forEach>		
			</c:when>
			<c:otherwise>
              <input id="fid_task" name="fid" type="hidden" value="${task.fid}"/>
              <input id="level_task" name="level" type="hidden" value="${task.level}"/>
              <input id="hasChilds_task" name="hasChilds" type="hidden" value="${task.hasChilds}"/>
              <input id="assignFlag_task" name="assignFlag" type="hidden" value="${task.assignFlag}"/>
              <input id="billId_task" name="billId" type="hidden" value="${task.billId}"/>
              <input id="planId_task" name="planId" type="hidden" value=""/>
              <input id="parentId_task" name="parentId" type="hidden" value=""/>
              <input id="initiaterId_task" name="initiaterId" type="hidden" value="${task.initiaterId}"/>
              <input id="principalerId_task" name="principalerId" type="hidden" value="${task.principalerId}"/>
              <input id="auditerId_task" name="auditerId" type="hidden" value="${task.auditerId}"/>
              <input id="undertakerId_task" name="undertakerId" type="hidden" value="${task.undertakerId}"/>
              <p><font>事件名称：</font><input class="textBox" disabled="disabled" id="name_task" name="name" value="${task.name}"/></p>
              <p><font>状态：</font><input class="textBox" id="status_task" disabled="disabled" readonly="readonly"/></p>
              <p><font>事件编号：</font><input class="textBox" id="code_task" disabled="disabled" name="code" value="${task.code}"/></p>
              <p><font>序号：</font><input class="textBox" id="number_task" disabled="disabled" name="number" value="${task.number}"/></p>
              <p><font>事件预计开始时间：</font><input class="textBox" disabled="disabled" id="antipateStartTime_task" name="antipateStartTime"/></p>
              <p><font>事件预计结束时间：</font><input class="textBox" disabled="disabled" id="antipateEndTime_task" name="antipateEndTime"/></p>
              <p><font>事件级别：</font><input class="textBox" disabled="disabled" id="taskLevelId_task" name="taskLevelId" value="${task.taskLevelName}"/></p>
              <p><font>保密级别：</font><input class="textBox" disabled="disabled" id="securityLevelId_task" name="securityLevelId" value="${task.securityLevelName}"/></p>
              <p><font>事件类别：</font><input class="textBox" disabled="disabled" id="taskTypeId_task" name="taskTypeId" value="${task.taskTypeName}"/></p>
              <p><font>事件金额：</font><input class="textBox" disabled="disabled" id="amount_task" name="amount" value="${task.amount}"/></p>
              <p><font>承办人：</font><input class="textBox" disabled="disabled" id="undertakerName_task" name="undertakerName" value="${task.undertakerName}"/></p>
              <p><font>责任人：</font><input class="textBox" disabled="disabled" id="principalerName_task" name="principalerName" value="${task.principalerName}"/></p>
              <p><font>单据类型：</font><input class="textBox" disabled="disabled" id="billType_task" name="billType"/></p>
              <p><font>单据：</font><input class="textBox" disabled="disabled" id="billCode_task" name="billCode" value="${task.billCode}"/></p>
              <br/><p><font>描述：</font><input class="textBox" disabled="disabled" id="describe_task" name="describe" style="width:520px;" value="${task.describe}"/></p>
              <br/><p><font>附件：</font><div id="attachBox_task"></div></p>
              <br/><p><font></font><input id="jumpPlan" class="btn-green4 btn-xs" type="button" value="查看计划"/></p>
              <h2 style="margin-top:30px;">操作记录</h2> 
              <hr/>
              <c:forEach var="recordVo" items="${recordVoList}">
              <div class="mylist">
              	<h3>${recordVo.operateName}</h3>
				<p><font>创建时间：</font>${recordVo.createTime}</p>
				<p><font>描述：</font>${recordVo.describe}</p><br/>
				${!empty recordVo.fileMap?'<p><font style="vertical-align: bottom;">附件：</font></p>':''}
				<c:forEach var="file" items="${recordVo.fileMap }">
					<div class="fj">
						<span style="margin-left:5px;display: inline-block;"><img src="/ops-pc/resources/images/morefile.png" style="width:30px;height:30px;"></span>
						<span class="filename" title="${file.key}">${file.key}</span>
						<a href="${ctx}/flow/plan/download?fileId=${file.value}" class="btn-s btn-blue">下载</a>
					</div>
				</c:forEach>
			  </div>
              </c:forEach>
			  </c:otherwise>
			</c:choose>
			</c:otherwise>
			
		</c:choose>	
		</div>
	</div>

</form>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/raty/jquery.raty.min.js?v=${js_v}"></script>
<script type="text/javascript">
$.ajax({
	url:"${ctx}/flow/plan/showAttach?num="+Math.random(),
	async:false,
	data:{planId:"${plan.fid}"},
	success:function(data){
		for(var i=0;i<data.length;i++){
			$("#attachBox_plan").append('<div class="fj"><span style="margin-left:5px;display: inline-block;"><img src="/ops-pc/resources/images/morefile.png" style="width:30px;height:30px;"></span><span class="filename" title="'+data[i].name+'">'+data[i].name+'</span><a href="${ctx}/flow/plan/download?fileId='+data[i].id+'" class="btn-s btn-blue">下载</a></div>');
		}
	}
});
$.ajax({
	url:"${ctx}/flow/task/showAttach?num="+Math.random(),
	async:false,
	data:{taskId:"${task.fid}"},
	success:function(data){
		for(var i=0;i<data.length;i++){
			$("#attachBox_task").append('<div class="fj"><span style="margin-left:5px;display: inline-block;"><img src="/ops-pc/resources/images/morefile.png" style="width:30px;height:30px;"></span><span class="filename" title="'+data[i].name+'">'+data[i].name+'</span><a href="${ctx}/flow/plan/download?fileId='+data[i].id+'" class="btn-s btn-blue">下载</a></div>');
		}
	}
});
if($("#rank-task").length>0){
	$("#rank-task").raty({
		width:182,
		score:"${rank.rank}",
		readOnly:  true,
		scoreName: 'rank',
		path: '${ctx}/resources/js/raty/img', 
	});
}
<c:choose>
<c:when test="${!empty plan.fid}">
$("#planType_plan").combobox({
	width:180,
	height:32,
	url:"${ctx}/flow/plan/queryPlanTypes",
	valueField:"id",
	textField:"text",
});
$('#status_plan').combobox({
	width:180,
	height:30,
	disabled:true,
	value:"${plan.status}",
	data:[{value:'100',text:'草稿'},{value:'101',text:'已提交待审核'},{value:'102',text:'已审核办理中'},{value:'103',text:'已延迟'},{value:'104',text:'已终止'},{value:'105',text:'已完成'}]
});
var antipateStartTime = getDateStr("${plan.antipateStartTime}");
var antipateEndTime = getDateStr("${plan.antipateEndTime}");
$('#antipateStartTime_plan').val(antipateStartTime);
$('#antipateEndTime_plan').val(antipateEndTime);
</c:when>
<c:otherwise>
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
$('#billType_task').val(cbillType["${task.billType}"]);
$('#status_task').combobox({
	width:180,
	height:30,
	disabled:true,
	value:"${task.status}",
	data:[{value:"0",text:"草稿"},{value:"1",text:"办理中"},{value:"2",text:"已办理待审核"},{value:"3",text:"已完成"},{value:"4",text:"已延迟且未开始办理"},{value:"5",text:"已延迟且未结束办理"},{value:"6",text:"已终止"}]
});
var antipateStartTime = getDateStr("${task.antipateStartTime}");
var antipateEndTime = getDateStr("${task.antipateEndTime}");
$('#antipateStartTime_task').val(antipateStartTime);
$('#antipateEndTime_task').val(antipateEndTime);
</c:otherwise>
</c:choose>

$("#jumpPlan").click(function(){
	var src="";
	if("${plan.fid}"){
		src="/flow/plan/axis?id=${plan.fid}";
	}else{
		src="/flow/plan/axis?id=${task.planId}";
	}
	var text='浏览计划';
	parent.kk(src,text);
});

</script>
<%-- <script type="text/javascript"  src="${ctx}/resources/js/flow/planTemplateEdit.js?v=${js_v}" ></script> --%>
</body>
</html>