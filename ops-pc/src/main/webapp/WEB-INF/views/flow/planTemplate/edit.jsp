l<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>计划模板明细</title>
</head>
<body>
    <form action="" class="bill-form myform" id="planform">
        <div class="title">
            <div class="title1 shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
                <div class="square1"><span class="backBtn"></span><div class="triangle"></div></div><h1>${param.mark == 1?(planTemplate.status==1?'查看':'编辑'):'新增' }${param.templateType == 1?'采购':param.templateType==2?'运输':param.templateType==3?'销售':'计划'}模板</h1>
            </div>
        </div>
        <div class="bill shadow" style="margin-top:50px;">
            <div class="in-box list2">
                <input name="fid" id="fid" value="${planTemplate.fid}" type='hidden'/>
                <%-- <input id="taskLevelId" name="taskLevelId" value="${planTemplate.taskLevelId}" type="hidden"/>
                <input id="securityLevelId" name="securityLevelId" value="${planTemplate.securityLevelId}" type="hidden"/> --%>
                <input name="updateTime" id="updateTime" type="hidden" value="${planTemplate.updateTime}"/>
                <input name="principalerId" id="principalerId" value="${planTemplate.principalerId}" type='hidden'/>
                <input name="undertakerId" id="undertakerId" value="${planTemplate.undertakerId}" type='hidden'/>
                <%--<input name="updateTime" id="updateTime" value="${planTemplate.updateTime}" type='hidden'/>--%>
                <p><font><em>*</em>计划编号：</font><input name="code" id="code" class="textBox" value="${planTemplate.code}"/></p>
                <p><font><em>*</em>计划名称：</font><input name="name" id="name" class="easyui-validatebox textBox" data-options="{required:true,novalidate:true,validType:'length[1,50]'}" value="${planTemplate.name}"/></p>
                <p><font><em>*</em>任务级别：</font><input name="taskLevelId" id="taskLevelId" class="textBox" value="${planTemplate.taskLevelId}"/></p>
                <p><font><em>*</em>保密级别：</font><input name="securityLevelId" id="securityLevelId" class="textBox" value="${planTemplate.securityLevelId}"/></p>

                <p style="position:relative"><font><em>*</em>负责人：</font><input name="principalerName" id="principalerName" class="textBox" value="${planTemplate.principalerName}"/></p>
                <p><font><em>*</em>承办人：</font><input name="undertakerName" id="undertakerName" class="textBox" value="${planTemplate.undertakerName}"/></p>

                <p><font><em>*</em>预计完成天数：</font><input name="days" id="days" class="textBox" value="${planTemplate.days}"/></p>
                <p><font>描述：</font><input name="describe" id="describe" class="easyui-validatebox textBox" data-options="{validType:'maxLength[200]'}" value="${planTemplate.describe}"/></p>
                <p><font>状态：</font><input name="status" id="status" class="textBox" value="${planTemplate.status==0?'停用':planTemplate.status==1?'启用':null}" disabled="disabled"/></p>
                <script type="">

                    $('#describe').textbox({
                        height:29,
                        icons: [{
                            iconCls:'',
                            handler: function(e){
                                if($('#mysave').css('display')=='none')
                                    return;
                                $(e.data.target).textbox('clear');
                                span.find('.textbox-text').focus();
                            }
                        }]
                    })
                    var span = $('#describe').next().find('.textbox-icon ').addClass('icon-clear');
                </script>
            </div>
        </div>
        <div class="shadow dataBox">
            <div class="in-box list1">
                <div class="actionBtn" style="height:42px;">
                    <a href="javascript:;" id="checkTask" class="btn-ora-add beforebtn">选择事件</a>
                    <a href="javascript:;" id="addRow" class="btn-ora-add beforebtn">新增</a>
                </div>
                <table id="modelList"></table>
            </div>
        </div>
        <div id="winPreDays">
            <div style="padding:10px 20px 20px;font-size:14px">
                如下图，需要建立电影院放映月计划模板，首先，某月需要放映3部电影，模板的一级排程可以这样排，见图中红色部分<br>
                诸神之怒（3D）从第1天开始放映，持续时间为7天：设置模板时准备天数为0，预计完成天数为7<br>
                道亦有道从第5天开始放映，持续时间为10天：设置模板时准备天数为4，预计完成天数为10<br>
                车在囧途从第17天开始放映，持续时间为6天：设置模板时准备天数为16，预计完成天数为6<br>
                其中道亦有道电影在10天时间内需要安排2个厅播放，模板的二级排程可以这样排，见图中紫色部分<br>
                1号厅从第1天开始放映，持续时间为3天：设置模板时准备天数为0，预计完成天数为3<br>
                2号厅从第2天开始放映，持续时间为4天：设置模板时准备天数为1，预计完成天数为4<br>
                <img src="" alt="" id="predayImg">
            </div>
        </div>
        <div id="winTaskName">
            <div class="window-search-box" style="margin:5px;">
                <input id="taskSeach" placeholder="编号或名称" style="padding:5px;width:120px;" />
                <a href="javascript:;" class="btn-blue btn-s" id="go-search">筛选</a>
                <a href="javascript:;" class="btn-blue btn-s" id="select-ok">确定</a>
                <a href="javascript:;" id="go-clear" class="btn-orange btn-s">清空</a>
            </div>
            <table id="taskTable"></table>
            <div id="taskPager"></div>
        </div>
    </form>
<div class="mybtn-footer">
    <input class="mybtn-blue mybtn-s" style="width:56px;" value="保存" id="mysave" onclick="saveData()" />
</div>
<div id="pop-win"></div>
<script type="text/javascript">
    if(clearFlag==1){
    	$(".bill").form("clear");
    	$("#taskLevelId").attr("value","");
    	$("#securityLevelId").attr("value","");
    	$("#principalerName").attr("value","");
    	$("#undertakerName").attr("value","");
    	$("#fid").val("${planTemplate.fid}");
    	$("#days").val("${planTemplate.days}");
    	$("#updateTime").val("${planTemplate.updateTime}");
    }
    var _details = '${planTemplate.details}';
if(${planTemplate.status==1})
     $('#checkTask').hide();
else
    $('#checkTask').show();
</script>
<script type="text/javascript"  src="${ctx}/resources/js/flow/planTemplateEdit.js?v=${js_v}" ></script>

</body>
</html>