<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<body>
        <div id="ganttList">
          <div style="position:relative;" class="gantt" id="GanttChartDIV"></div>
          <div id="ganttDetail"></div>
          <div id="addMenu">
            <div id="option-si" data-options="name:'sibling'">同级事件</div>
            <div id="option-ch" data-options="name:'children'">下级事件</div>   
          </div>  
          
        </div> 
<script>
$("#addMenu").menu({
	onClick:function(item){
		var id=$("#addMenu").attr("rowId");
		if(item.name=="sibling"){
			openTaskDetail($("#"+id),"sibling");
		}else if(item.name=="children"){
			openTaskDetail($("#"+id),"children");
		}
	}
});

var taskList=[];
if(eval('${taskList}')){
	taskList=eval('${taskList}');
}
var g = new JSGantt.GanttChart('g',document.getElementById('GanttChartDIV'), 'day');
initialGantt();

var ganttList=g.getList();

function addTaskFn(target){
	var leftId=$(target).closest("tr").attr("id");
	var id=leftId.slice(leftId.indexOf("_")+1);
	var left=$(target).offset().left;
	var top=$(target).offset().top+20;
	var parentAssign=getById($(target).closest("tr").attr("parentid")).assignFlag;
	var assign=getById(id).assignFlag;
	var secureAdd=getById(id).secureAdd;
	var secureAddChild=getById(id).secureAddChild;
	$("#addMenu").attr("rowId",$(target).attr("id"));
	if(secureAdd==1){
		if(("${param.planStatus}"!="100"&&$(target).closest("tr").attr("class")=="group")||parentAssign==1){
			$("#option-si").hide();
		}else{
			$("#option-si").show();
		}
	}else{
		$("#option-si").hide();
	}
	
	if(secureAddChild==1){
		if(($(target).closest("tr").attr("status")!="0"&&$(target).closest("tr").attr("status")!="4")||assign==1){
			$("#option-ch").hide();
		}else{
			$("#option-ch").show();
		}
	}else{
		$("#option-ch").hide();
	}
	
	if($("#option-ch").css("display")!="none"||$("#option-si").css("display")!="none"){
		$("#addMenu").menu("show",{left:left,top:top,});
	}
};
	  
function showLinkFn(target){
	$("#ganttDetail").slideUp("",function(){
		$(".ganttBox_right").css("height","0px");
		$(".ganttBox_left").css("height","0px");
		$("#ganttDetail").removeAttr("flag");
	});
	var leftId=$(target).closest("tr").attr("id");
	var id=leftId.slice(leftId.indexOf("_")+1);
	if($("tr[id^='child_dep_']").length>0){
		var depId=$($("tr[id^='child_dep_']")[0]).attr("depid");
		g.setList(ganttList);
		g.Draw("${param.planStatus}");
		if(depId==id){
			setNode();
			if($("#status_plan").combobox("getValue")!="100"&&$("#status_plan").combobox("getValue")!="102"&&$("#status_plan").combobox("getValue")!="103"){
				$(".addTask").hide();
			}
			return false;
		}
	}
	$.ajax({
		url:"${ctx}/flow/task/getAllRelevanceTask",
		async:false,
		data:{fid:id},
		success:function(data){
			if(data){
				var depItems=[];
				var startTime='';
				var endTime='';
				for(var i=0;i<data.length;i++){
					startTime=getTime(data[i].antipateStartTime);
					endTime=getTime(data[i].antipateEndTime);
					acStart=getTime(data[i].actualStartTime);
					acEnd=getTime(data[i].actualEndTime);
                    //TaskItem(pID, pName, pStart, pEnd, pColor, pLink, pMile, pRes, pComp, pGroup, pParent, pOpen, pDepend, pCaption,pPrincipal,pCode,pStatus,pAcStart,pAcEnd)
					depItems.push(new JSGantt.TaskItem('dep_'+data[i].fid,data[i].name,startTime,endTime,'5DAEE5',data[i].parentId,0,'',0,0,'depItem', 0,id,'',data[i].undertakerName,data[i].code,data[i].status,acStart,acEnd));
				}
				var list=g.getList();
				var listSlice1="";
				var listSlice2="";
				var newList=[];
				for(var j=0;i<list.length;j++){
					if(list[j].getID()==id){
						listSlice1=list.slice(0,j+1).concat(depItems);
						listSlice2=list.slice(j+1);
						newList=listSlice1.concat(listSlice2);
						listSlice1="";
						listSlice2="";
						break;
					};
				}
				g.setList(newList);
				g.Draw("${param.planStatus}");
			}
			setNode();
		}
	});
	if($("#status_plan").combobox("getValue")!="100"&&$("#status_plan").combobox("getValue")!="102"&&$("#status_plan").combobox("getValue")!="103"){
		$(".addTask").hide();
	}
};
if($("td[bgcolor='#ccccff']").position()){
	if($("#status_plan").combobox("getValue")!="105"&&$("#status_plan").combobox("getValue")!="104"){
		$("#rightside").scrollLeft($("td[bgcolor='#ccccff']").position().left);
	}
}
function showDetailFn(target){
	openTaskDetail($(target),"detail");
};
function showTask(target){
	var id=$(target).attr("taskId");
	parent.kk('/flow/plan/planView?busType=1&rankFlag=0&id='+id,'查看事件');
}

function initialGantt(){
	g.setShowRes(0); // Show/Hide Responsible (0/1)
	g.setShowDur(0); // Show/Hide Duration (0/1)
	g.setShowComp(0); // Show/Hide % Complete(0/1)
	g.setCaptionType('None');  // Set to Show Caption (None,Caption,Resource,Duration,Complete)
	g.setShowStartDate(0); // Show/Hide Start Date(0/1)
	g.setShowEndDate(0); // Show/Hide End Date(0/1)
	g.setDateInputFormat('mm/dd/yyyy');  // Set format of input dates ('mm/dd/yyyy', 'dd/mm/yyyy', 'yyyy-mm-dd')
	g.setDateDisplayFormat('mm/dd/yyyy'); // Set format to display dates ('mm/dd/yyyy', 'dd/mm/yyyy', 'yyyy-mm-dd')
	g.setFormatArr(""); // Set format options (up to 4 : "minute","hour","day","week","month","quarter")
	if(taskList.length>0){
		drawGantt(taskList);
		$("#task-list").slideDown();
		if($("#status_plan").combobox("getValue")!="100"&&$("#status_plan").combobox("getValue")!="102"&&$("#status_plan").combobox("getValue")!="103"){
			$(".addTask").hide();
		}
	}
	setNode();
}

function openTaskDetail(target,flag){
	if(exitEditPlan){
		exitEditPlan();
		$("#planDetail").slideUp();
	}
	if($("#taskDetail").html()){
		exitEditTask();
		$("#taskDetail").slideUp();
		$("#taskDetail").empty();
	}
	var leftId=target.closest("tr").attr("id");
	var id=leftId.slice(leftId.indexOf("_")+1);
	var taskId="";
	var parentId="";
	var planId="${param.planId}";
	if(flag=="detail"){
		taskId=id;
	}else if(flag=="sibling"){
		parentId=target.closest("tr").attr("parentId");
	}else if(flag=="children"){
		parentId=id;
	}
	if($("#bill-win").length>0 && $("#bill-win").html()!=''){
		$("#bill-win").window('destroy');
	}
	if($(".taskBox_"+id).css("height")=="0px"){
		$(".ganttBox_right").css("height","0px");
		$(".ganttBox_left").css("height","0px");
		var top=$(".taskBox_"+id).position().top+58;
		if($("#completePlanMsg").css("display")!="none"){
			top=top+$("#completePlanMsg").outerHeight();
		}
		var height="";
		$("#ganttDetail").css("top",top+"px");
		$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:taskId,planId:planId,parentId:parentId,planStatus:"${param.planStatus}"},function(){
			height=$("#ganttDetail").height();
			$(".taskBox_"+id).css("height",height+"px");
			$("#ganttDetail").slideDown("",function(){
				setNode();
			});
			$("#ganttDetail").attr("flag",flag);
		});
	}else{
		if($("#ganttDetail").attr("flag")!=flag){
			$("#ganttDetail").load("${ctx}/timeAxis/addTask",{id:taskId,planId:planId,parentId:parentId,planStatus:"${param.planStatus}"},function(){
				$("#ganttDetail").attr("flag",flag);
			});
			return false;
		}
		$("#ganttDetail").slideUp("",function(){
			$(".ganttBox_right").css("height","0px");
			$(".ganttBox_left").css("height","0px");
			$("#ganttDetail").removeAttr("flag");
			setNode();
		});
		if($("#bill-win").window()){
			$('#bill-win').window('destroy');
		}
	}
}

function hideTaskDetail(target,flag){
	$("#ganttDetail").slideUp("",function(){
		$(".ganttBox_right").css("height","0px");
		$(".ganttBox_left").css("height","0px");
		$("#ganttDetail").removeAttr("flag");
		setNode();
	});
}


function getTime(time){
	if(time){
		time=time.slice(0,10);
		var timeArry=time.split("-");
		var result=timeArry[1]+"/"+timeArry[2]+"/"+timeArry[0];
		return result;
	}
};

function drawGantt(taskList){
	var hasChild=0;
	var startTime='';
	var endTime='';
	var parentId='';
	var barColor='5DAEE5';
	for(var i=1;i<taskList.length;i++){
		for(var j=0;j<taskList.length;j++){
			if(taskList[j].parentId==taskList[i].fid){
				hasChild=1;
				break;
			}
		}
		startTime=getTime(taskList[i].antipateStartTime);
		endTime=getTime(taskList[i].antipateEndTime);
		acStart=getTime(taskList[i].actualStartTime);
		acEnd=getTime(taskList[i].actualEndTime);
		if(taskList[i].level!=1){
			parentId=taskList[i].parentId;
		}
		if(taskList[i].status==6){
			barColor="9A9A9A";
		}else{
			barColor='5DAEE5';
		}
		                        //TaskItem(pID, pName, pStart, pEnd, pColor, pLink, pMile, pRes, pComp, pGroup, pParent, pOpen, pDepend, pCaption,pPrincipal,pCode,pStatus,pAcStart,pAcEnd,pSecureShow)
		g.AddTaskItem(new JSGantt.TaskItem(taskList[i].fid,taskList[i].name,startTime,endTime,barColor,taskList[i].parentId,0,'',0,hasChild,parentId, 0,'',taskList[i].relevanceQuantity,taskList[i].undertakerName,taskList[i].code,taskList[i].status,acStart,acEnd,taskList[i].secureShow));
		startTime='';
		endTime='';
		parentId='';
		hasChild=0;
	}
	g.Draw("${param.planStatus}");
};

function setNode(){
	var groups=$(".group");
	var height="";
	var trId="";
	var id="";
	$(".list-node").hide();
	for(var i=0;i<groups.length;i++){
		trId=$(groups[i]).attr("id");
		id=trId.slice(trId.indexOf("_")+1);
		height=parseInt($(groups[i]).offset().top)-parseInt(101);
		$("#task-list").append("<span class='list-node node_"+trId+"' style='top:"+height+"px'>"+$(groups[i]).attr("start")+"</span>");
	}
}

function getById(fid){
	var result="";
	$.ajax({
		url:"${ctx}/flow/task/getById",
		async:false,
		data:{fid:fid},
		success:function(data){
			result=data;
		}
	});
	return result;
}
/* if($("#left-head").offset()){
	var defultHeadTop=$("#left-head").offset().top;
	$(document).scroll(function(){
		var scrollTop=$(document).scrollTop();
		if(scrollTop>defultHeadTop){
			$("#left-head").css("position","absolute");
			$("#right-head").css("position","absolute");
			$("#left-head").css("width",$("#left-head").next().width()+"px");
			$("#statusH").css("width",$(".gdatehead").width()+"px");
			$("#codeH").css("width",$(".gcode").width()+"px");
			$("#memberH").css("width",$(".gprincipal").width()+"px");
			$("#nameH").css("width",$(".gname").width()+"px");
			
			$("#statusH").css("height",($(".gdatehead").height()+20)+"px");
			$("#codeH").css("height",($(".gcode").height()+20)+"px");
			$("#memberH").css("height",($(".gprincipal").height()+20)+"px");
			$("#nameH").css("height",($(".gname").height()+20)+"px");
			
			$("#left-head").css("top",(scrollTop-168)+"px");
			$("#right-head").css("top",(scrollTop-168)+"px");
		}else{
			$("#left-head").css("position","static");
			$("#right-head").css("position","static");
			
			$("#left-head").next().css("padding-top","0px");
			$("#right-head").next().css("padding-top","0px");
		}
		setNode();
	});
} */
$(document).scroll(function(){
	if(!$("#left-head").offset()){
		return false;
	}
	var scrollTop=$(document).scrollTop();
	var leftTop=$("#left-head").offset().top;
	var rightTop=$("#right-head").offset().top;
	var top=$('#time-line').outerHeight()-$("#task-list").outerHeight()-$("#plan-end").outerHeight()-11;
	if(scrollTop>leftTop){
		$("#left-head div").css("position","relative");
		$("#left-head div").css("top",(scrollTop-top)+"px");
		$("#right-head").css("position","relative");
		$("#right-head").css("top",(scrollTop-top)+"px");
	}else{
		$("#left-head div").css("position","static");
		$("#left-head div").css("top",leftTop+"px");
		$("#right-head").css("position","static");
		$("#right-head").css("top",rightTop+"px");
	}
});
</script>
</body>
</html>