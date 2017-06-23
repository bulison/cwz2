<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>时间轴</title>
   <link rel="stylesheet" href="${ctx}/resources/css/timeaxis.css"/>   
   <%@ include file="/WEB-INF/views/common/header.jsp"%>
   <%@ include file="/WEB-INF/views/common/js.jsp"%> 
   <script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script> 
</head>
<%
String eventId=request.getParameter("eventId");
%>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="#">应用中心</a></li>
	        <li><a href="${ctx}/plan/listPlan">计划管理</a></li>
	        <li><a href="#" class="curr">时间轴</a></li>
	    </ul>
	</div>

    <div class="tl"></div>
    <script>
    var eventId="<%=eventId%>";
    
    data=${axisDatas}
	root = data.rootEvent;//获得数据
	rootId=root.fid;		//根事件ID
	moreLevelNo1=root.more;        //一级事件个数
	pid = data.fid;
	canChange=data.canChange;		
	var pt=data.planType;
	userid=data.curUserId;		//当前用户ID
	arrs=new Array();
    arrs.length=0;
    planStatus=data.planStatus;	//计划状态
    canShenHePlan=data.canShenHePlan;  //是否能审核计划
    oneLevelEventIds=data.oneLevelEventIds;//所有一级事件的ID
    curUserId=data.curUserId;    //当前用户ID
    creatorId=data.creatorId;
    var isCommit=false;
    /*
    $(function(){    		    	
		BuildTL($('.tl'),data);		
		if(eventId!=null&&eventId!="null"){	
			getParentIds(root.sonTevents,eventId);
			openEvent(eventId);
		}else{
			getMyEventFids(root.sonTevents);
			openCurUserEvent(arrs);
		}				
    })
	*/
	
	init();
    
    function init(){
    	BuildTL($('.tl'),data);		
		if(eventId!=null&&eventId!="null"){	
			getParentIds(root.sonTevents,eventId);
			openEvent(eventId);
		}else{
			getMyEventFids(root.sonTevents);
			openCurUserEvent(arrs);
		}
    }
    
    	function BuildTL(el,data){			
			/*构建dom*/			
			var str ='<font face="黑体">'+data.planName+'</font>';	
			var planInfo='计划开始时间：'+data.planStart+'\n'+'计划结束时间：'+data.planEnd+'\n'+'部门名称：'+data.deptName+'\n'+'责任人：'+data.creatorName+'\n'+'描述：'+data.fdescribe;
			str += '<div title="'+planInfo+'"><a href="javascript:;" class="addplan"><i class="tl_head"></i></a>';
			if(pid==null){
				str += '<div class="tb_addplan">';
				str += '<i class="tb_tools_topPlan"></i>';			
				str += '<div class="tb_tools_main"><a href="javascript:;" onClick="Add_ddPlan()">订单计划</a><div class="tb_tools_line"></div><a onClick="Add_rwPlan()">任务计划</a></div>';			
				str += '<i class="tb_tools_bottom"></i>';
				str += '</div>';	
			}else{							
				if(canChange=="1"){				//能修改计划				
					if(pt=="TPL_PLAN_TYPE_ORDER"){						
						if(canShenHePlan==true){
							if(moreLevelNo1>0){								
								str += '<div class="tb_addplan">';
								str += '<i class="tb_tools_topPlan"></i>';
								str+='<div class="tb_tools_main"><a href="javascript:;" onClick="shenhePlan(\'' + data.fid + '\')">计划审核</a>';
								str+='</div>';
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';
							}																				
						}else{							
							if(moreLevelNo1>0&&"SOD"==planStatus){								
								str += '<div class="tb_addplan">';
								str += '<i class="tb_tools_topPlan"></i>';
								str+='<div class="tb_tools_main"><a href="javascript:;" onClick="CommitPlan(\'' + data.fid + '\',this)">提交计划</a>';
								str+='</div>';
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';
							}						
						}												
					}else{						//任务型 									
						if(canShenHePlan==true){
							if(moreLevelNo1>0){								
								str += '<div class="tb_addplan">';
								str += '<i class="tb_tools_topPlan"></i>';
								str+='<div class="tb_tools_main"><a href="javascript:;" onClick="shenhePlan(\'' + data.fid + '\')">计划审核</a>';
								str+='</div>';
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';
							}
						}else{							
							if(moreLevelNo1>0&&"SOD"==planStatus){								
								str += '<div class="tb_addplan">';
								str += '<i class="tb_tools_topPlan"></i>';								
								str+='<div class="tb_tools_main"><a href="javascript:;" onClick="CommitPlan(\'' + data.fid + '\',this)">提交计划</a>';								
								str+='</div>';
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';
							}							
						}												
					}	
				}else{
					if(pt=="TPL_PLAN_TYPE_ORDER"){
						str += '<div class="tb_addplan">';
						str += '<i class="tb_tools_topPlan"></i>';
						if(canShenHePlan==true){
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="selPlan(\'' + data.fid + '\')">查看计划</a>';
							if(moreLevelNo1>0){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="shenhePlan(\'' + data.fid + '\')">计划审核</a>';								
							}
							str+='</div>';
						}else{							
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="selPlan(\'' + data.fid + '\')">查看计划</a>';
							if(planStatus=="STE"  && creatorId==curUserId){
								
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="PlanPinJia(\'' + data.fid + '\')">计划评价</a>';
							}
							str+='</div>';
						}									
						str += '<i class="tb_tools_bottom"></i>';
						str += '</div>';
					}else{
						str += '<div class="tb_addplan">';
						str += '<i class="tb_tools_topPlan"></i>';	 
						if(canShenHePlan==true){
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="selPlan(\'' + data.fid + '\')">查看计划</a>';
							if(moreLevelNo1>0){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="shenhePlan(\'' + data.fid + '\')">计划审核</a>';								
							}
							str+='</div>';
						}else{
							str += '<div class="tb_tools_main"><a  href="javascript:;"onClick="selPlan(\'' + data.fid + '\')">查看计划</a>';	
							if(planStatus=="STE" && creatorId==curUserId ){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="PlanPinJia(\'' + data.fid + '\')">计划评价</a>';
							}
							str+='</div>';							
						}									
						str += '<i class="tb_tools_bottom"></i>';
						str += '</div>';
					}
				}								
			}									
			str+='</div>';
			str += '<div class="tl_body">';
			if(canChange=="1"){							//1表示可以添加一级事件
				str += '<div class="tl_item"><a href="javascript:;" class="tb_dot_add"></a></div>';	
			}	
			
			var ll=root.sonTevents.length;	
			$.each(root.sonTevents,function(){
				var canRelation=this.canRelation;
				str += '<div class="tl_item" ffinishTime="'+this.ffinishTime+'" fstartDate="' + this.fstartDate + '" fendDate="' + this.fendDate + '">';
				str += '<div class="tl_progressR"></div><div class="tl_progressG"></div>';
				if(this.lineValue=="-"){
					str += '<b class="tl_time">' + '<p>'+'</p></b>';
				}else{
					str += '<b class="tl_time">' + this.fendDate + '<p>' + this.lineValue + '</p></b>';	
				}								
				str += '<a href="javascript:;" class="tb_dot';
				if(this.fwarningLevel && this.fwarningLevel > 0){
					str += ' tb_dot_red';
				} else if(this.fwarningLevel == 0){
					str += ' tb_dot_green';
				} else {					
					str += ' tb_dot_green';
				}
				str += '"></a>';
				var fstatus=this.fstatus;	//事件状态- 待确定								
				var fName=this.fname;	
				var info="计划开始日期："+this.fstartDate+"\n"+"预计完成日期："+this.fendDate+"\n"+"收支类型："+this.ftypeName+"\n金额："+this.famount+"\n责任人："+this.executorName;
				if(this.reason){
					info+="\n延迟原因："+this.reason;
				}else{
					info+="\n描述："+this.fdescribe;
				}
				str += '<div title="'+info+'" class="tb_label"  id="' + this.fid + '"><b>' + fName + '</b><p>' + this.dept + '</p>';
				if(this.showTools=="011"){	
					if("SAP"==planStatus||"SPG"==planStatus){		//计划状态- 待审核:一级排程提交后
						if(fstatus=="SWU"){
							str += '<a href="javascript:;" class="btn_onelevel">+</a>';
						}
					}else if(fstatus=="SMA"&&"SMA"==planStatus){
						str += '<a href="javascript:;" class="btn_onelevel">+</a>';
					}else{
						str += '<a href="javascript:;" class="btn_onelevel">+</a>';	
					}					
				}else if(this.showTools=="111"){
					if("SCO"==fstatus&&"SAP"==planStatus){		//事件状态- 待确定
						str += '<a href="javascript:;" class="btn_onelevel">+</a>';
					}else{
						if(fstatus=="SMA"&&"SMA"==planStatus){
							str += '<a href="javascript:;" class="btn_onelevel">+</a>';
						}else if("SCO"==fstatus){		
							str += '<a href="javascript:;" class="btn_onelevel">+</a>';
						}else if(fstatus=="SWU"){
							str += '<a href="javascript:;" class="btn_onelevel">+</a>';
						}						
					}
				}else if("SCO"==fstatus&&this.showTools.charAt(0)=="1"){
					str += '<a href="javascript:;" class="btn_onelevel">+</a>';
				}else{					
						var i=0;						
						$.each(this.eventButs || [],function(){								
							i=i+1;
						});
						if(i>0){
							str += '<a href="javascript:;" class="btn_onelevel">+</a>';
						}																				
				}				
				str+='</div>';
				//alert(this.showTools+";"+planStatus+";"+fstatus);relationEvent
				if(this.showTools=="011"){
					if("SAP"==planStatus||"SPG"==planStatus){		//计划状态- 待审核:一级排程提交后;计划状态 - 进行中:
						if(fstatus=="SWU"){
							str += '<div class="tb_onelevelevent">';
							str += '<i class="tb_tools_topPlan"></i>';			
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="EditItem(\'' + this.fid +'\',\''+this.fstatus + '\')">修改</a><div class="tb_tools_line"></div><a onClick="DelItem(\'' + this.fid+'\',\''+this.fname + '\')">删除</a>';
							if(canRelation==true&&ll>1){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
							}
							str+='</div>';
							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';
						}
					}else if(fstatus=="SMA"&&"SMA"==planStatus){   //审核 计划不通过
						str += '<div class="tb_onelevelevent">';
						str += '<i class="tb_tools_topPlan"></i>';			
						str += '<div class="tb_tools_main"><a href="javascript:;" onClick="EditItem(\'' + this.fid +'\',\''+this.fstatus + '\')">修改</a><div class="tb_tools_line"></div><a onClick="DelItem(\'' + this.fid+'\',\''+this.fname + '\')">删除</a>';
						if(canRelation==true&&ll>1){
							str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
						}
						str+='</div>';
						
						str += '<i class="tb_tools_bottom"></i>';
						str += '</div>';
					}else{						
						str += '<div class="tb_onelevelevent">';
						str += '<i class="tb_tools_topPlan"></i>';			
						str += '<div class="tb_tools_main"><a href="javascript:;" onClick="EditItem(\'' + this.fid +'\',\''+this.fstatus + '\')">修改</a><div class="tb_tools_line"></div><a onClick="DelItem(\'' + this.fid+'\',\''+this.fname + '\')">删除</a>';
						if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
							str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
						}
						str+='</div>';
						
						str += '<i class="tb_tools_bottom"></i>';
						str += '</div>';
					}										
				}else if(this.showTools=="111"){					
					if("SCO"==fstatus&&"SAP"==planStatus){ //事件状态- 待确定 
						str += '<div class="tb_onelevelevent">';
						str += '<i class="tb_tools_topPlan"></i>';			
						str += '<div class="tb_tools_main"><a href="javascript:;" onClick="ConfirmEvent(\'' + this.fid+'\',\''+this.fname + '\')">确认</a><div class="tb_tools_line"></div><a onClick="refuseEvent(\'' + this.fid+'\',\''+this.fname + '\')">拒绝</a>';
						if(canRelation==true&&ll>1){
							str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
						}
						str+='</div>';
						
						str += '<i class="tb_tools_bottom"></i>';
						str += '</div>';
					}else{
						if(fstatus=="SMA"&&"SMA"==planStatus){   //审核 计划不通过
							str += '<div class="tb_onelevelevent">';
							str += '<i class="tb_tools_topPlan"></i>';			
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="EditItem(\'' + this.fid +'\',\''+this.fstatus + '\')">修改</a><div class="tb_tools_line"></div><a onClick="DelItem(\'' + this.fid+'\',\''+this.fname + '\')">删除</a>';
							if(canRelation==true&&ll>1){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
							}
							str+='</div>';
							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';
						}else if("SCO"==fstatus){
							str += '<div class="tb_onelevelevent">';
							str += '<i class="tb_tools_topPlan"></i>';			
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="EditItem(\'' + this.fid +'\',\''+this.fstatus  + '\')">修改</a><div class="tb_tools_line"></div><a onClick="DelItem(\'' + this.fid+'\',\''+this.fname + '\')">删除</a>';
							if(canRelation==true&&ll>1){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
							}
							str+='</div>';
							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';
						}else if(fstatus=="SWU"){
							str += '<div class="tb_onelevelevent">';
							str += '<i class="tb_tools_topPlan"></i>';			
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="EditItem(\'' + this.fid +'\',\''+this.fstatus  + '\')">修改</a><div class="tb_tools_line"></div><a onClick="DelItem(\'' + this.fid+'\',\''+this.fname + '\')">删除</a>';
							if(canRelation==true&&ll>1){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
							}
							str+='</div>';
							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';
						}						
					}
				}else if("SCO"==fstatus&&this.showTools.charAt(0)=="1"){
					str += '<div class="tb_onelevelevent">';
					str += '<i class="tb_tools_topPlan"></i>';			
					str += '<div class="tb_tools_main"><a href="javascript:;" onClick="ConfirmEvent(\'' + this.fid+'\',\''+this.fname + '\')">确认</a><div class="tb_tools_line"></div><a onClick="refuseEvent(\'' + this.fid+'\',\''+this.fname + '\')">拒绝</a>';
					if(canRelation==true&&ll>1){
						str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
					}
					str+='</div>';
					
					str += '<i class="tb_tools_bottom"></i>';
					str += '</div>';
				}else{
					str += '<div class="tb_onelevelevent">';										
						var i=0;
						var fid=this.fid;
						var fname=this.fname;
						$.each(this.eventButs || [],function(){	
							if(i==0){
								str += '<i class="tb_tools_topPlan"></i>';
								str += '<div class="tb_tools_main"><a href="javascript:;" onClick="'+this.clickEvent+'(\'' + fid+'\',\''+fname + '\')">'+this.value+'</a>';								
							}else{
								str += '<div class="tb_tools_line"></div><a href="javascript:;" onClick="'+this.clickEvent+'(\'' + fid+'\',\''+fname + '\')">'+this.value+'</a>';
							}
							i=i+1;
						});
						if(i>0){
							if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
							}														
							str+='</div>';
							str += '<i class="tb_tools_bottom"></i>';	
						}else{
							if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
								str += '<i class="tb_tools_topPlan"></i>';								
								str+='<div class="tb_tools_main"><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
								str+='</div>';
								str += '<i class="tb_tools_bottom"></i>';
							}																					
						}															
					str += '</div>';
				}
				
				
				str += '<div class="tb_list">';
				str += BuildItems(this.sonTevents || [],this);
				//alert_f(this.showTools+";"+this.fname+";"+planStatus+";"+fstatus); 
				if(this.showTools.charAt(0)=="1"&&planStatus=="SPG"&&fstatus!="SCO"&&fstatus!="SHS"&&fstatus!="SDE"){			//1表示可以新增二级事件(大加号)
					str += '<div class="tb_listItem tb_listItemAdd" fid="' + this.fid + '"><i></i></div>';
				}								
				str += '<div class="clean"></div>';
				str += '</div>';
				str += '</div>';
			});
			str += '<div class="tl_foot"><i class="tl_foot_dot';
			if(root.fwarningLevel && root.fwarningLevel > 0){ str += ' tl_foot_red';}
			str += '"></i></div>';
			str += '</div>';
			el.html(str);
			
			/*注册事件*/
			el.find('.addplan').click(function(){				
				
				if(pid==null){
					$(this).next().toggle();
				}else{
					if(canChange=="1"){
						if(pt=="TPL_PLAN_TYPE_ORDER"){
							$(this).next().toggle();
						}else{
							//EditPlan(pid);
							$(this).next().toggle();
						}
					}else{
						if(pt=="TPL_PLAN_TYPE_ORDER"){							
							$(this).next().toggle();
						}else{
							//selPlan(pid);
							$(this).next().toggle();
						}						
					}
				}
				
			});
									
			el.find('.tb_dot').click(function(){
				//$(this).next().toggle();				
			});
						
			el.find('.tb_dot_add').click(function(){
				//alert_f('添加一级事件'+"\n"+"pid="+pid+"\n"+"rootid="+rootId);				
				var $win=$('#timeaxis').window({
					href:'${ctx}/eventController/listEvent?okCallBack=timeaxisok&cancelCallBack=cancelWin&level=1&planId='+pid,
				    width:700,    
				    height:600, 
				    title:'添加一级事件',
				    minimizable:false,
					maximizable:false,
				    modal:true,
				    onClose:function(){
				    	if(isCommit){
			    			setTimeout(function () { window.location.reload(); }, 200);	
			    			isCommit=false;
			    		} 				    	
				    }
				});		
				//$win.window('open');
				$win.window('move',{    					    
					  top:$(document).scrollTop()+($(window).height() - 600) * 0.5
					});
			});
			el.find('.tb_label').click(function(){				
				$progressG.height(0);
				$progressR.height(0);
				//$(this).next().children(':not(.sonTevents)').toggle();
				$(this).next().next().find('.sonTevents').hide();
				$(this).next().next().find('.tb_listItem').toggle();

				setProgressHeight();
			});
			
			el.find('.tb_listItemAdd').click(function(){
				//alert_f('添加一级事件的子事件，parentid=' + $(this).attr("fid")+"\n"+"计划ID="+pid);
				var parentid=$(this).attr("fid");
				var $win=$('#timeaxis').window({
					href:'${ctx}/eventController/listEvent?okCallBack=timeaxisok&cancelCallBack=cancelWin&planId='+pid+'&parentId='+parentid,
				    width:700,    
				    height:600, 
				    title:'添加一级事件的子事件',
				    minimizable:false,
					maximizable:false,
				    modal:true,
				    onClose:function(){
				    	if(isCommit){
			    			setTimeout(function () { window.location.reload(); }, 200);	
			    			isCommit=false;
			    		} 				    	
				    }
				});		
				//$win.window('open');
				$win.window('move',{    					    
					  top:$(document).scrollTop()+($(window).height() - 600) * 0.5
					});
			});
			
			el.find('.btn_listItem_add').click(function(){				
				$(this).next().toggle();
			});
			el.find('.tl_more').click(function(){
				$progressG.height(0);
				$progressR.height(0);
				$(this).parent().next().toggle();
				setProgressHeight();
			});
			var $progressG = el.find('.tl_progressG');//温度计绿
			var $progressR = el.find('.tl_progressR');//温度计红
			setProgressHeight();
			function setProgressHeight(){
				$.each($progressG || [],function(){
					var $dot = $(this).parent();
					var fstartDate = new Date($dot.attr('fstartDate').replace(/\-/g, "\/"));//计划开始时间
					var fendDate   = new Date($dot.attr('fendDate').replace(/\-/g, "\/"));//计划结束时间					
					var step = (fendDate-fstartDate)/86400000;					
					var finishTime=$dot.attr('ffinishTime');
					if(finishTime!="undefined"){
						/*
						var date = new Date($dot.attr('ffinishTime').replace(/\-/g, "\/"));//实际结束时间
						var d = parseInt((date-fstartDate)/86400000);							
						var d2 = parseInt((fendDate-date)/86400000);							
						var h = parseInt($dot.height() / step * d);		
						if(d2<0){								//已过了结束时间
							h=$dot.height();
						}
						if(h > $dot.height()){
							h=$dot.height();
						}
						*/
						h=$dot.height();
						if($dot.find('>a').hasClass('tb_dot_red')){
							$(this).prev().height(h);
						} else {
							$(this).height(h);
						}
					}else{
						var date       = new Date();//当前日期
						var d = parseInt((date-fstartDate)/86400000);							
						var d2 = parseInt((fendDate-date)/86400000);							
						var h = parseInt($dot.height() / step * d);		
						if(d2<0){								//已过了结束时间
							h=$dot.height();
						}
						if(h > $dot.height()){
							h=$dot.height();
						}												
						if($dot.find('>a').hasClass('tb_dot_red')){
							$(this).prev().height(h);
						} else {
							$(this).height(h);
						}						
					}									
				});
			}	
			
			
		}
    	
    	
    /*当eventId参数有值时，展开
    */
    /*
    function openEvent(eventId){    	
    	var top1=document.body.scrollHeight;     	
    	var len=arrs.length;
		for(var i=0;i<len;i++){
			var objs=arrs[i];
			var l=objs.length;					
			if(eventId==objs[l-1]){					
				for(var j=0;j<l-1;j++){
						var fid=objs[j];
						$('#'+fid).click();						
				}				
			}
		}    	
		var top2=document.body.scrollHeight;
		top3=top2-top1-70;
		$("html,body").animate({scrollTop:top3},10);
    }
    */
    
    function openEvent(eventId){    	
    	var top1=document.body.scrollHeight;     	
    	var len=arrs.length;
		for(var i=0;i<len;i++){
			var objs=arrs[i];
			var l=objs.length;					
			if(eventId==objs[l-1].fid){				
				var pid=objs[l-1].fparentId;
				var fids=new Array();
				fids.length=0;
				fids[fids.length]=pid;
				getpids(objs,pid,fids);
				for(var j=fids.length-2;j>=0;j--){					
					$('#'+fids[j]).click();	 
				}
				
			}
		}    	
		var top2=document.body.scrollHeight;
		top3=top2-top1-70;
		$("html,body").animate({scrollTop:top3},10);
    }
    
    function getpids(objs,pid,fids){
    	var l=objs.length;	
    	for(var i=0;i<l;i++){
    		var fparentId=objs[i].fparentId;
    		var fid=objs[i].fid;    		
    		if(fid==pid){
    			if(fparentId==null||fparentId==""){
        			break;
        		}
    			fids[fids.length]=fparentId;
    			getpids(objs,fparentId,fids);
    		}
    		
    	}
    }
    
    function getParentIds(sonTevents,eventId){    	
    	for(var i=0;i<sonTevents.length;i++){
    		var myArray=new Array();
    		myArray.length=0;    		
    		var fid=sonTevents[i].fid;
    		myArray[myArray.length]=sonTevents[i];
    		if(eventId==fid){    			
    			break;
    		}
    		getParentFid(sonTevents[i].sonTevents,myArray,eventId);
    		arrs[arrs.length]=myArray;
    	} 
    }
    
    function getParentFid(sonTevents,myArray,eventId){
		for(var i=0;i<sonTevents.length;i++){			
    		var fid=sonTevents[i].fid;
    		if(myArray[myArray.length-1].fid==eventId){
    			break;
    		}
    		myArray[myArray.length]=sonTevents[i];    		    		
    		if(eventId==fid){    			
    			break;
    		}
    		getParentFid(sonTevents[i].sonTevents,myArray,eventId);
		}
    }
    
    /*
    算法不优
    function getParentIds(sonTevents,eventId){    	
    	for(var i=0;i<sonTevents.length;i++){
    		var myArray=new Array();
    		myArray.length=0;    		
    		var fid=sonTevents[i].fid;
    		myArray[myArray.length]=fid;
    		if(eventId==fid){    			
    			break;
    		}
    		getParentFid(sonTevents[i].sonTevents,myArray,eventId);
    		arrs[arrs.length]=myArray;
    	} 
    }
    
    function getParentFid(sonTevents,myArray,eventId){
		for(var i=0;i<sonTevents.length;i++){			
    		var fid=sonTevents[i].fid;
    		if(myArray[myArray.length-1]==eventId){
    			break;
    		}
    		myArray[myArray.length]=fid;    		    		
    		if(eventId==fid){    			
    			break;
    		}
    		getParentFid(sonTevents[i].sonTevents,myArray,eventId);
		}
    }
    */
    function getMyEventFids(sonTevents){
    	for(var i=0;i<sonTevents.length;i++){
    		var myArray=new Array();
    		myArray.length=0;
    		var exeid=sonTevents[i].fexecutorId; 
    		var fid=sonTevents[i].fid;
    		myArray[myArray.length]=fid;
    		if(userid==exeid){    			
    			myArray[myArray.length]="userid="+userid;
    		}
    		getEventFids(sonTevents[i].sonTevents,myArray);
    		arrs[arrs.length]=myArray;
    	}   	
    }
    
	function getEventFids(sonTevents,myArray){
		for(var i=0;i<sonTevents.length;i++){
			var exeid=sonTevents[i].fexecutorId; 
    		var fid=sonTevents[i].fid;
    		myArray[myArray.length]=fid;
    		if(userid==exeid){
    			myArray[myArray.length]="userid="+userid;
    		}
    		getEventFids(sonTevents[i].sonTevents,myArray);
		}
    }
    	
    function openCurUserEvent(arrs){
    	var top1=document.body.scrollHeight;  
		var len=arrs.length;		
		for(var i=0;i<len;i++){
			var objs=arrs[i];
			var l=objs.length;
			var b=false;
			for(var j=0;j<l;j++){				
				if(objs[j]=="userid="+userid){
					b=true;
					break;
				}
			}
			if(b){				
				for(var j=0;j<l-1;j++){
					var fid=objs[j];
					if(fid!="userid="+userid){
						$('#'+fid).click();			
					}						
				}
			}			
		}
		var top2=document.body.scrollHeight;
		top3=top2-top1-70;
		$("html,body").animate({scrollTop:top3},10);
    }
    
    
	function BuildItems(data,pEvent){//建二级以上目录
		var str = '';
		var ll=data.length;
		$.each(data,function(){
			var canRelation=this.canRelation;
			//alert(canRelation);
			var fid = this.fid;
			var fname=this.fname;
			var fstatus=this.fstatus;	//事件状态- 待确定
			var pStatus=pEvent.fstatus;	//parent事件状态			
			str += '<div class="tb_listItem';
			if(this.fexecutorType == 2){ str += ' tb_blueLine';}//蓝框
			if(this.fexecutorType == 3){ str += ' tb_redLine';}//红框
			str += '">';	
			//alert(this.showTools+";"+fname+";"+fstatus+";"+pStatus);
			if(this.showTools){				
					if(this.showTools=="111"){
						if("SHS"==pStatus){		//parent事件已拆分完成
							if(fstatus=="SCO"){									
								str += '<a href="javascript:;" class="btn_listItem_add"></a>';
								str += '<div class="tb_tools">';
								str += '<i class="tb_tools_top"></i>';
								str += '<div class="tb_tools_main"><a href="javascript:;" onClick="ConfirmEvent2(\'' + this.fid+'\',\''+this.fname + '\')">确认</a><div class="tb_tools_line"></div><a href="javascript:;" onClick="EditItem2(\'' + this.fid+'\',\''+this.fname + '\')">修改</a>';
								if(canRelation==true&&ll>1){
									str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
									
								}
								str+='</div>';								
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';							
							}else if(fstatus=="SPG"){
								str += '<a href="javascript:;" class="btn_listItem_add"></a>';
								str += '<div class="tb_tools">';
								str += '<i class="tb_tools_top"></i>';														
								str += '<div class="tb_tools_main"><a href="javascript:;" onClick="AddItem(\'' + this.fid + '\')">新增</a>';
								if(canRelation==true&&ll>1){
									str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';									
								}
								str+='</div>';								
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';
							}else if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
								str += '<a href="javascript:;" class="btn_listItem_add"></a>';
								str += '<div class="tb_tools">';
								str += '<i class="tb_tools_top"></i>';																					
								str+='<div class="tb_tools_main"><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';																	
								str+='</div>';								
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';
							}
						}else{
							str += '<a href="javascript:;" class="btn_listItem_add"></a>';
							str += '<div class="tb_tools">';
							str += '<i class="tb_tools_top"></i>';
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="EditItem(\'' + this.fid + '\')">修改</a><div class="tb_tools_line"></div><a href="javascript:;" onClick="DelItem(\'' + this.fid+'\',\''+this.fname + '\')">删除</a>';
							if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
								
							}
							str+='</div>';							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';
						}
					}else if(this.showTools=="100"){
						if("SHS"==pStatus||"SDE"==pStatus){		//parent事件已拆分完成
							if(fstatus=="SCO"){		//待确定,在用户新建并提交事件后
								str += '<a href="javascript:;" class="btn_listItem_add"></a>';
								str += '<div class="tb_tools">';
								str += '<i class="tb_tools_top"></i>';
								str += '<div class="tb_tools_main"><a href="javascript:;" onClick="ConfirmEvent2(\'' + this.fid+'\',\''+this.fname + '\')">确认</a><div class="tb_tools_line"></div><a href="javascript:;" onClick="EditItem2(\'' + this.fid+'\',\''+this.fname + '\')">修改</a>';
								if(canRelation==true&&ll>1){
									str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';									
								}
								str+='</div>';								
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';
							}else if(fstatus=="SHA"){
								var fexecutorId=pEvent.fexecutorId;
								if(fexecutorId==curUserId){
									str += '<a href="javascript:;" class="btn_listItem_add"></a>';
									str += '<div class="tb_tools">';
									str += '<i class="tb_tools_top"></i>';
									str += '<div class="tb_tools_main"><a href="javascript:;" onClick="shenhe(\'' + this.fid + '\')">通过</a><div class="tb_tools_line"></div><a href="javascript:;" onClick="shenheNo(\'' + this.fid + '\')">不通过</a>';
									if(canRelation==true&&ll>1){
										str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';										
									}
									str+='</div>';									
									str += '<i class="tb_tools_bottom"></i>';
									str += '</div>';	
								}
							}else if("SHS"!=fstatus){  //事件已拆分完成
								str += '<a href="javascript:;" class="btn_listItem_add"></a>';
								str += '<div class="tb_tools">';
								str += '<i class="tb_tools_top"></i>';
								str += '<div class="tb_tools_main"><a href="javascript:;" onClick="AddItem(\'' + this.fid + '\')">新增</a>';
								if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
									str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
									
								}
								str+='</div>';								
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';	
							}else if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
								str += '<a href="javascript:;" class="btn_listItem_add"></a>';
								str += '<div class="tb_tools">';
								str += '<i class="tb_tools_top"></i>';																					
								str+='<div class="tb_tools_main"><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';																	
								str+='</div>';								
								str += '<i class="tb_tools_bottom"></i>';
								str += '</div>';
							}	
						}else if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
							str += '<a href="javascript:;" class="btn_listItem_add"></a>';
							str += '<div class="tb_tools">';
							str += '<i class="tb_tools_top"></i>';							
							str+='<div class="tb_tools_main"><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';															
							str+='</div>';							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';	
						}
					}else if(this.showTools=="011"){						
						if("SHS"==pStatus){
							 
						}else{
							str += '<a href="javascript:;" class="btn_listItem_add"></a>';
							str += '<div class="tb_tools">';
							str += '<i class="tb_tools_top"></i>';
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="EditItem(\'' + this.fid + '\')">修改</a><div class="tb_tools_line"></div><a href="javascript:;" onClick="DelItem(\'' + this.fid+'\',\''+this.fname + '\')">删除</a>';
							if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
								
							}
							str+='</div>';
							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';	
						}						
					}else if(fstatus=="SHA"){  //审核  上级对下级的任务完成情况的审核
						var fexecutorId=pEvent.fexecutorId;
						if(fexecutorId==curUserId){
							str += '<a href="javascript:;" class="btn_listItem_add"></a>';
							str += '<div class="tb_tools">';
							str += '<i class="tb_tools_top"></i>';
							str += '<div class="tb_tools_main"><a href="javascript:;" onClick="shenhe(\'' + this.fid + '\')">通过</a><div class="tb_tools_line"></div><a href="javascript:;" onClick="shenheNo(\'' + this.fid + '\')">不通过</a>';
							if(canRelation==true&&ll>1){
								str+='<div class="tb_tools_line"></div><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';
								
							}
							str+='</div>';
							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';	
						}						
					}else{
						if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
							str += '<a href="javascript:;" class="btn_listItem_add"></a>';
							str += '<div class="tb_tools">';
							str += '<i class="tb_tools_top"></i>';							
							str+='<div class="tb_tools_main"><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';															
							str+='</div>';							
							str += '<i class="tb_tools_bottom"></i>';
							str += '</div>';	
						}
					}				
			}else if(canRelation==true&&ll>1&&fstatus!="SOK"&&fstatus!="SOX"){
				str += '<a href="javascript:;" class="btn_listItem_add"></a>';
				str += '<div class="tb_tools">';
				str += '<i class="tb_tools_top"></i>';							
				str+='<div class="tb_tools_main"><a href="javascript:;" onClick="relationEvent(\'' + this.fid+'\',\''+this.fname + '\')">关联事件</a>';															
				str+='</div>';							
				str += '<i class="tb_tools_bottom"></i>';
				str += '</div>';
			}
			
			if(this.seal == 1){ str += '<div class="icon_handle"></div>';}//已办理
			if(this.seal == 2){ str += '<div class="icon_warning"></div>';}//已预警
			str += '<table border="0" cellpadding="0" cellspacing="0"';
			if(this.end){ str += ' class="tb_readonly"';}//事件结束
			str += '>';
			str += '<tr><td align="right">事件名称：</td><td><div class="single" title="' + this.fname + '">' + this.fname + '</div></td></tr>';
			str += '<tr><td style="white-space: nowrap;" align="right">开始日期：</td><td><div class="single" title="' + this.fstartDate + '">' + this.fstartDate + '</div></td></tr>';
			str += '<tr><td align="right">完成日期：</td><td><div class="single" title="' + this.fendDate + '">' + this.fendDate + '</div></td></tr>';
			str += '<tr><td align="right">收支类型：</td><td><div class="single" title="' + this.ftypeName + '">' + this.ftypeName + '</div></td></tr>';
			str += '<tr><td align="right">金额：</td><td><div class="single" title="' + this.famount + '">' + this.famount + '元</div></td></tr>';
			str += '<tr><td align="right">责任人：</td><td><div class="single" title="' + this.executorName + '">' + this.executorName + '</div></td></tr>';
			if(this.reason){
				str += '<tr><td align="right" class="fontRed">延迟原因：</td><td><div class="single fontRed" title="' + this.reason + '">' + this.reason + '</div></td></tr>';
			} else {
				str += '<tr><td align="right">描述：</td><td><div class="single" title="' + this.fdescribe + '">' + this.fdescribe + '</div></td></tr>';
			}
			str += '</table>';
			str += '<div class="tl_tools">';
			if(this.end){
				str += '<div class="tb_end"><i></i><b>事件已终止</b></div>';
			} else {				
				$.each(this.eventButs || [],function(){					
					str += '<a href="javascript:;" class="tl_btn btn_' + this.color + '" onClick="' + this.clickEvent + '(\'' + fid+'\',\''+fname + '\')">' + this.value + '</a>';					
				});
			}
			str += '</div>';
			if(!this.end){
				str += '<div class="tl_line';
				if(this.line){ str += ' line_' + this.line;}
				str += '">';
				if(this.lineValue){ str += '<b>' + this.lineValue + '</b>';}
				str += '</div>';
			}
			if(this.sonTevents && this.sonTevents.length > 0){				
				str += '<div class="tl_more" id="' + this.fid + '" fid="' + this.fid + '">' + this.sonTevents.length + '</div>';
			}
			str += '</div>';
			if(this.sonTevents){//有子级
				str += '<div class="sonTevents"><div class="clean"></div>' + BuildItems(this.sonTevents,this) + '<div class="clean"></div></div>';
			}
		});
		return str;
	}
	
	/*按钮事件集*/
	function Handle(fid){
		//alert("你点击了办理按钮，fid：" + fid);		
		var $win=$('#timeaxis').window({
			href:'${ctx}//uploadController/uploadUI?okCallBack=timeaxisok&cancelCallBack=cancelWin&paramId='+fid,
		    width:350, 
		    height:150,
		    title:'办理',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	//if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		//} 				    	
		    }    
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 150) * 0.5
			});
		/*
		var aj = $.ajax({    
		    url:'${ctx}/otherLevelEventReview/executeEvent?paramId='+fid,		       
		    type:'post',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {    
		        if(data.result =="0" ){    		           		           
		            window.location.reload();    
		        }else{    
		        	$.fool.alert({msg:data.msg});     
		        }    
		     },    
		     error : function() {        
		    	 $.fool.alert({msg:"异常！"});    
		     }    
		});
		*/
	}
	function Delay(fid){
		//alert_f("你点击了延迟按钮，fid：" + fid);
		var $win=$('#timeaxis').window({
			href:'${ctx}/otherLevelEventReview/applyEventDelayUI?okCallBack=timeaxisok&cancelCallBack=cancelWin&paramId='+fid,
		    width:600,    
		    height:620, 
		    title:'延迟',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);
	    			isCommit=false;
	    		} 				    	
		    }    
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 620) * 0.5
			});
	}
	function Stop(fid,fname){
		//alert_f("你点击了终止按钮，fid：" + fid);
		$.fool.confirm({
			msg:'你确定终止【'+fname+'】事件吗?',
			fn:function(r){
				if (r){
					var aj = $.ajax({    
					    url:'${ctx}/otherLevelEventReview/cancelEvent?paramId='+fid,		       
					    type:'post',    
					    cache:false,    
					    dataType:'json',    
					    success:function(data) {    
					        if(data.result =="0" ){    		           		           
					            window.location.reload();    
					        }else{    
					        	$.fool.alert({msg:data.msg});     
					        }    
					     },    
					     error : function() {        
					    	 $.fool.alert({msg:"异常！"});    
					     }    
					});
				}
			}
		});
	}
	function Score(fid){
		//alert_f("你点击了评分按钮，fid：" + fid);
		var $win=$('#timeaxis').window({
			href:'${ctx}/otherLevelEventReview/valuateEventUI?okCallBack=timeaxisok&cancelCallBack=cancelWin&paramId='+fid,
		    width:600,    
		    height:600, 
		    title:'评分',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
		    }       
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 600) * 0.5
			});
	}
	function Comment(fid){
		//$.fool.alert({msg:"你点击了留言按钮，功能还没实现，等等。fid：" + fid}); 
		//http://localhost:8080/ops/eventComment/commentlist?eventId=402881104d8f7d91014d8f81021a0007&currentPage=1&pageSize=10
		var $win=$('#timeaxis').window({
			href:'${ctx}/eventComment/commentlist?okCallBack=timeaxisok&cancelCallBack=cancelWin&eventFid='+fid,
		    width:600,    
		    height:500, 
		    title:'留言',
		    minimizable:false,
			maximizable:false,
		    modal:true,
	    	onClose:function(){	    		
	    		if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		}    							    	
    		}   
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 500) * 0.5
			});				
	}
	/*
	计划增删改Add_ddPlan
	*/
	function Add_ddPlan(){
		$.fool.alert({msg:"新增订单计划:这里暂不实现，只提供了接口"});	
	}
	function Add_rwPlan(){
		$.fool.alert({msg:"新增任务计划:这里暂不实现，只提供了接口"});	
	}
	function EditPlan(pid){
		$.fool.alert({msg:"修改计划:这里暂不实现，只提供了接口。"+pid});	
	}
	/*提交计划 
	*/
	function CommitPlan(pid,srcObj){
		//alert("提交计划:"+pid); 
		//alert(srcObj.tagName);
		var evt =srcObj;// event.srcElement!=null ? event.srcElement : event.target;		
		evt.onclick=function(){
			//$.fool.alert({msg:"提交了就不要再提交了!"});
			alert("提交了就不要再提交了!");
		}						
		var aj = $.ajax( {    
		    url:'${ctx}/oneLevelEventReview/submitPlan?planId='+pid,		       
		    type:'post',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {    
		        if(data.returnCode == "0" ){
		        	$.fool.alert({msg:"提交成功，请耐心等待审核！", fn:function(){
		            	window.location.reload();    
		        	}});
		        }else{    
		        	$.fool.alert({msg:data.message, fn:function(){
		        		//window.location.reload();		        		
		        	}});
		        }    
		     },    
		     error : function(XMLHttpRequest, textStatus, errorThrown) {        
		    	 $.fool.alert({msg:"异常！"+textStatus+","+errorThrown});
		    	// window.location.reload();
		     }       
		}); 
		
	}
	
	function Add_dd(pid){
		$.fool.alert({msg:"订单维护:这里暂不实现，只提供了接口"+pid});
	}
	
	function sel_dd(pid){
		$.fool.alert({msg:"查看订单:这里暂不实现，只提供了接口"+pid});
	}
	/*
	*计划评价
	*/
	function PlanPinJia(pid){
		var $win=$('#timeaxis').window({
			href:'${ctx}/oneLevelEventReview/valuatePlanUI?okCallBack=timeaxisok&cancelCallBack=cancelWin&planId='+pid,
		    width:600,    
		    height:500, 
		    title:'计划评价',
		    minimizable:false,
			maximizable:false,
		    modal:true,
	    	onClose:function(){
	    		if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
    		}   
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 500) * 0.5
			});
	}
	function selPlan(pid){
		//$.fool.alert({msg:"查看计划:这里暂不实现，只提供了接口"+pid});			
		var $win=$('#timeaxis').window({
			href:'${ctx}/plan/viewPlan?planFid='+pid,
		    width:600,    
		    height:500, 
		    title:'查看计划',
		    minimizable:false,
			maximizable:false,
		    modal:true,
	    	onClose:function(){
	    		if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
    		}   
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 500) * 0.5
			});
		
	}
	/*事件增删改*/
	function AddItem(fid){
		//alert_f("新增子事件，parentId=" + fid+"\n"+"计划ID="+pid);
		var $win=$('#timeaxis').window({
			href:'${ctx}/eventController/listEvent?okCallBack=timeaxisok&cancelCallBack=cancelWin&planId='+pid+'&parentId='+fid,
		    width:700,    
		    height:600, 
		    title:'新增子事件',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
		    }
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 600) * 0.5
			});
	}
	/*修改一级事件
	*/
	function EditItem(fid,fstatus){
		//alert_f("修改事件，当前事件id=" + fid);
		var saveok='';
		if("SWU"==fstatus||"SMA"==fstatus){		//一级事件拒绝后的修改
			saveok='/oneLevelEventReview/editEvent';
		}else{
			saveok='/eventController/save';
		}		
		var $win=$('#timeaxis').window({
			href:'${ctx}/eventController/editTimeaxisEvent?okCallBack=timeaxisok&cancelCallBack=cancelWin&saveok='+saveok+'&eventId=' + fid,
		    width:700,    
		    height:600, 
		    title:'修改事件',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
		    }
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 600) * 0.5
			});
	}
	/*修改二级以上事件
	*/
	function EditItem2(fid){
		//alert_f("修改事件，当前事件id=" + fid);		
		var $win=$('#timeaxis').window({
			href:'${ctx}/eventController/editTimeaxisEvent?okCallBack=timeaxisok&cancelCallBack=cancelWin&saveok=/otherLevelEventReview/editEvent&eventId=' + fid,
		    width:700,    
		    height:600, 
		    title:'修改事件',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
		    }
		});				
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 600) * 0.5
			});
	}
	
	/*
	事件关联
	*/
	function relationEvent(eventId){
		var $win=$('#timeaxis').window({
			href:'${ctx}/eventRelation/addRelation?okCallBack=timeaxisok&cancelCallBack=cancelWin&eventId=' + eventId,
		    width:800,    
		    height:650, 
		    title:'关联事件',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
		    }
		});				
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 650) * 0.5
			});
	}
	
	function DelItem(fid,fname){
		//alert_f("删除事件，当前事件id=" + fid);
		$.fool.confirm({
			msg:'你确定删除【'+fname+'】事件吗?',
			fn:function(r){
				if (r){
					var aj = $.ajax( {    
					    url:'${ctx}/eventController/delete?fid='+fid,		      
					    type:'get',    
					    cache:false,    
					    dataType:'json',    
					    success:function(data) {
					    	if(data.returnCode=='0'){
					    		 window.location.reload();
					    	}
					    	else{
					    		$.fool.alert({msg:data.message});
					    	}
					     },    
					     error : function() {    		              
					    	 $.fool.alert({msg:"异常！"});    
					     }    
					});
				}
			}
		});
	}
	
	function ConfirmEvent(fid,fname){
		//alert_f("确认一级事件"+fid);
		$.fool.confirm({
			msg:'你确定接收【'+fname+'】任务吗?',
			fn:function(r){
				if (r){
					var aj = $.ajax( {    
					    url:'${ctx}/oneLevelEventReview/affirmEvent?paramId='+fid,		      
					    type:'get',    
					    cache:false,    
					    dataType:'json',    
					    success:function(data) {
					    	window.location.reload(); 		    	
					     },    
					     error : function() {    		              
					    	 $.fool.alert({msg:"异常！"});    
					     }    
					});
				}
			}
		});
	}
	
	function ConfirmEvent2(fid,fname){
		//alert_f("确认二级及以上事件"+fid);
		$.fool.confirm({
			msg:'你确定接收【'+fname+'】任务吗?',
			fn:function(r){
				if (r){
					var aj = $.ajax( {    
					    url:'${ctx}/otherLevelEventReview/affirmEvent?paramId='+fid,		      
					    type:'get',    
					    cache:false,    
					    dataType:'json',    
					    success:function(data) {
					    	window.location.reload(); 		    	
					     },    
					     error : function() {    		              
					    	 $.fool.alert({msg:"异常！"});    
					     }    
					});
				}
			}
		});
	}
	
	function refuseEvent(fid,fname){
		//alert_f("拒绝一级事件"+fid);
		$.fool.confirm({
			msg:'你确定拒绝【'+fname+'】任务吗?',
			fn:function(r){
				if (r){
					var aj = $.ajax( {    
					    url:'${ctx}/oneLevelEventReview/refuseEvent?paramId='+fid,		      
					    type:'get',    
					    cache:false,    
					    dataType:'json',    
					    success:function(data) {
					    	window.location.reload(); 		    	
					     },    
					     error : function() {    		              
					    	 $.fool.alert({msg:"异常！"});    
					     }    
					});	
				}
			}
		});
	}
	
	function refuseEvent2(fid,fname){
		//alert_f("拒绝二级及以上事件"+fid);
		$.fool.confirm({
			msg:'你确定拒绝【'+fname+'】任务吗?',
			fn:function(r){
				if (r){
					var aj = $.ajax( {    
					    url:'${ctx}/otherLevelEventReview/vetoReviewEventEdited?paramId='+fid,		      
					    type:'get',    
					    cache:false,    
					    dataType:'json',    
					    success:function(data) {
					    	window.location.reload(); 		    	
					     },    
					     error : function() {    		              
					    	 $.fool.alert({msg:"异常！"});    
					     }    
					});	
				}
			}
		});
	}
	
	function splitEventOk(fid){
		//alert_f("二级事件拆分确认"+fid);
		var aj = $.ajax( {    
		    url:'${ctx}/otherLevelEventReview/finishSplit?paramId='+fid,		      
		    type:'get',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {
		    	window.location.reload(); 		    	
		     },    
		     error : function() {    		              
		    	 $.fool.alert({msg:"异常！"});    
		     }    
		});
	}
	/*上级对下级任务完成情况的审核通过
	*/
	function shenhe(fid){
		var aj = $.ajax( {    
		    url:'${ctx}/otherLevelEventReview/passReviewEventExecuted?paramId='+fid,		      
		    type:'get',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {
		    	window.location.reload(); 		    	
		     },    
		     error : function() {    		              
		    	 $.fool.alert({msg:"异常！"});    
		     }    
		});
	}
	
	/*上级对下级任务完成情况的审核不通过
	*/
	function shenheNo(fid){
		var aj = $.ajax( {    
		    url:'${ctx}/otherLevelEventReview/vetoReviewEventExecuted?paramId='+fid,		      
		    type:'get',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {
		    	window.location.reload(); 		    	
		     },    
		     error : function() {    		              
		    	 $.fool.alert({msg:"异常！"});    
		     }    
		});
	}
	
	/*
	计划审核
	*/
	function shenhePlan(pid){
		var evt = event.srcElement!=null ? event.srcElement : event.target;				
		var $win=$('#timeaxis').window({
			href:'${ctx}/oneLevelEventReview/reviewPlanUI?okCallBack=timeaxisok&cancelCallBack=cancelWin&paramId=' + pid,
		    width:700,    
		    height:600, 
		    title:'计划审核',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
		    }
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 600) * 0.5
			});
	}
	/*
	计划审核通过
	*/
	/*
	function shenhePlan(pid){
		//alert_f("计划审核");		
		var checkedIds=JSON.stringify(oneLevelEventIds);//oneLevelEventIds;
		var uncheckedIds=JSON.stringify([]);
		var aj = $.ajax( {    
			url:'${ctx}/oneLevelEventReview/review?planId='+pid+'&checkedIds='+checkedIds+'&uncheckedIds='+uncheckedIds,		      
		    type:'post',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {
		    	window.location.reload(); 		    	
		     },    
		     error : function() {    		              
		    	 $.fool.alert({msg:"异常！"});    
		     }    
		});		
	}
	*/
	/*
	计划审核不通过
	*/
	/*
	function shenhePlanNo(pid){
		//alert_f("计划审核不通过");		
		var uncheckedIds =JSON.stringify(oneLevelEventIds);//oneLevelEventIds;
		var checkedIds=JSON.stringify([]);
		var aj = $.ajax( {    
			url:'${ctx}/oneLevelEventReview/review?planId='+pid+'&checkedIds='+checkedIds+'&uncheckedIds='+uncheckedIds,		      
		    type:'post',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {
		    	window.location.reload(); 		    	
		     },    
		     error : function() {    		              
		    	 $.fool.alert({msg:"异常！"});    
		     }    
		});		
	}
	*/
	/*
	延迟审批
	*/
	function yanchisp(fid){
		//alert_f("延迟审批界面待完成！");
		var $win=$('#timeaxis').window({
			href:'${ctx}/otherLevelEventReview/reviewEventDelayUI?okCallBack=timeaxisok&cancelCallBack=cancelWin&paramId=' + fid,
		    width:700,    
		    height:600, 
		    title:'延迟审批',
		    minimizable:false,
			maximizable:false,
		    modal:true,
		    onClose:function(){
		    	if(isCommit){
	    			setTimeout(function () { window.location.reload(); }, 200);	
	    			isCommit=false;
	    		} 				    	
		    }
		});		
		//$win.window('open');
		$win.window('move',{    					    
			  top:$(document).scrollTop()+($(window).height() - 600) * 0.5
			});
	}
	/*
	  二级以上事件修改通过
	*/
	function changeok(fid){
		var aj = $.ajax( {    
		    url:'${ctx}/otherLevelEventReview/passReviewEventEdited?paramId='+fid,		      
		    type:'get',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {
		    	window.location.reload(); 		    	
		     },    
		     error : function() {    		              
		    	 $.fool.alert({msg:"异常！"});    
		     }    
		});
	}
	
	/*
	  二级以上事件修改否决
	*/
	function changeno(fid){
		var aj = $.ajax( {    
		    url:'${ctx}/otherLevelEventReview/vetoReviewEventEdited?paramId='+fid,		      
		    type:'get',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {
		    	window.location.reload(); 		    	
		     },    
		     error : function() {    		              
		    	 $.fool.alert({msg:"异常！"});    
		     }    
		});
	}
	
	
	function cancelWin(){
		isCancel=true;
		$('#timeaxis').window('close');
	}
	function timeaxisok(){
		isCommit=true;
		$('#timeaxis').window('close');
	}
		
	
	
    </script>

    <div  id="timeaxis"  title="Login"  style="width: 0px; height: 0px;">
	    
	</div>	
	<!--input type=button name=commit value="flush" onclick="init();"  -->
</body>
</html>