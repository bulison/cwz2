<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>执行人效益分析明细</title>
<style>
</style>
</head>
<body>
	<form class="form1" style="padding:0px;">
		<p><font>开始日期：</font><input id="startDay" name="startDay" /> </p>
		<p><font>结束日期：</font><input id="endDay" name="endDay" /> </p>
		<a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="detailSearch">查询</a>
	</form>
	<table id="details"></table>
	<div id="_pager"></div>
<script>
    $('#startDay').fool('datebox',{
        prompt:"日期可选择可输入",
        inputDate:true,
        width:162,
        height:30,
        value:'${param.startDate}'
    });
    $('#startDay').datebox('textbox').focus(function(){
        $('#startDay').datebox('showPanel');
    });
    $('#endDay').datebox({
        prompt:"日期可选择可输入",
        inputDate:true,
        width:162,
        height:30,
        value:'${param.endDate}'
    });
    $('#endDay').datebox('textbox').focus(function(){
        $('#endDay').datebox('showPanel');
    });

    $('#detailSearch').click(function () {
        $('#details').trigger("reloadGrid");
    });
    $('#details').jqGrid({
        datatype:function(postdata){
            $.ajax({
                url:getRootPath()+"/api/rate/queryRateMemberDetailByMemberId?memberId="+'${param.memberId}'+"&startDay="+$("#startDay").datebox("getValue")+"&endDay="+$("#endDay").datebox("getValue"),
                data:postdata,
                dataType:"json",
                complete: function(data,stat){
                    if(stat=="success") {
                        data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
                        $("#details")[0].addJSONData(data.responseJSON);
                    }
                }
            });
        },
        autowidth:true,
        height:$(window).outerHeight()-200+"px",
        pager:"#_pager",
        rowNum:10,
        rowList:[10,20,30],
        viewrecords:true,
        jsonReader:{
            records:"total",
            total: "totalpages",
        },
        colModel:[
            {name:"taskName",label:'事件名称',width:5,align:'center'},
            {name:"planStartDate",label:'事件计划开始时间',width:5,align:'center',formatter:function (value,options,row) {
            	if(value)
            		return value.substring(0,10);
            }},
            {name:"planEndDate",label:'事件计划结束时间',width:5,align:'center',formatter:function (value,options,row) {
                if(value)
            		return value.substring(0,10);
            }},
            {name:"completeDate",label:'实际完成时间',width:8,align:'center',formatter:function (value,options,row) {
                if(value)
                    return value.substring(0,10);
                else{
                    return "";
				}
            }},
            {name:"delayTime",label:'延误天数',width:8,align:'center'},
            {name:"delayNumber",label:'延误次数',width:5,align:'center'},
        ],
    }).navGrid('#_pager',{add:false,del:false,edit:false,search:false,view:false});

</script>
</body>
</html>
