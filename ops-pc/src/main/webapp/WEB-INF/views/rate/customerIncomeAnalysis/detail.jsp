<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <title>客户收益分析明细</title>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <script type="text/javascript"
            src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript"
            src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
</head>
<style>

</style>
<body>
<%-- 表 --%>
<div  id="form2" style="">
    <p><font>开始日期：</font><input id="_startDay" name="_startDay" class="textBox"></p>
    <p><font>结束日期：</font><input id="_endDay" name="_endDay" class="textBox"></p>
    <p><a href="javascript:;" id="_search" style="margin-left: 2px" class="btn-blue4 btn-s">查询</a></p>
    <%--<p><a href="javascript:;" id="clear" style="margin-left: 2px" class="btn-blue4 btn-s">清空</a></p>--%>
</div>
<div style="margin-top: 10px">
    <table id="detailIn"></table>
    <div id="pager1"></div>
</div>
<script type="text/javascript">

    var detailData = ${param.detailData}.rows;
    var customerId = ${param.customerId};
    formatter(detailData);

    //开始日期
    $("#_startDay").fool("datebox", {
        width: 100,
        height: 30,
        inputDate: true,
        focusShow: true,
        required: true,
        prompt: '开始日期',
        value:${param.startDate}
    });
    //结束日期
    $("#_endDay").fool("datebox", {
        width: 100,
        height: 30,
        inputDate: true,
        focusShow: true,
        required: true,
        prompt: '结束日期',
        value:${param.endDate}
    });

    $("#_search").click(function () {
        var startDate = $("#_startDay").textbox("getValue");
        var endDate = $("#_endDay").textbox("getValue");
        $.ajax({
            url: '${ctx}/api/rate/customerIncomeAnalysisDetail',
            type: 'get',
            data: {
                startDate: startDate,
                endDate: endDate,
                customerId: customerId
            },
            success: function (data) {
                detailData = data.rows;
                formatter(detailData);
                $("#detailIn")[0].addJSONData(detailData);

            }
        });

        /*$("#detailIn").jqGrid("setGridParam",{
         postData:{
         startDate:startDate,
         endDate:endDate
         }
         }).trigger("reloadGrid");*/
    });

    //明细表

    $("#detailIn").jqGrid({
        data: detailData,
        datatype: 'local',
        forceFit: true,
        pager: "#pager1",
        rowNum: 10,
        rowList: [10, 20, 30],
        viewrecords: true,
        jsonReader: {
            records: 'total',
            total: 'totalpages'
        },
        autowidth:true,
        height:478.5,
        footerrow: true,
        gridComplete: completeMethod,
        colModel: [{
            name: 'billDate',
            label: '单据日期',
            width: 100,
            align: 'center'
        }, {
            name: 'code',
            label: '单号',
            width: 160,
            align: 'center'
        }, {
            name: 'totalAmount',
            label: '销售金额',
            width: 100,
            align: 'center',
            formatter: 'currency',
            formatoptions: {
                decimalPlaces: 2
            }
        }, {
            name: 'costAmount',
            label: '销售成本',
            width: 100,
            align: 'center',
            formatter: 'currency',
            formatoptions: {
                decimalPlaces: 2
            }
        }, {
            name: 'cost',
            label: '费用',
            width: 100,
            align: 'center',
            formatter: 'currency',
            formatoptions: {
                decimalPlaces: 2
            }
        }, {
            name: 'profit',
            label: '利润',
            width: 100,
            align: 'center',
            formatter: 'currency',
            formatoptions: {
                decimalPlaces: 2
            }
        }, {
            name: 'totalPayAmount',
            label: '已收金额',
            width: 100,
            align: 'center',
            formatter: 'currency',
            formatoptions: {
                decimalPlaces: 2
            }
        }, {
            name: 'noPayAmount',
            label: '未收金额',
            width: 100,
            align: 'center',
            formatter: 'currency',
            formatoptions: {
                decimalPlaces: 2
            }
        }, {
            name: 'ty',
            label: '延迟收款天数',
            width: 100,
            align: 'center',
            formatter: 'number',
            formatoptions: {
                decimalPlaces: 2
            }
        }, {
            name: 'tn',
            label: '延迟收款次数',
            width: 100,
            align: 'center',
            formatter: 'number',
            formatoptions: {
                decimalPlaces: 2
            }
        }]

    });
    //格式化时间
    function formatter(detailData) {
        for (var i = 0; i < detailData.length; i++) {
            var date = detailData[i].billDate.split(" ");
            detailData[i].billDate = date[0];
        }
    }


    function completeMethod() {
        var sumTotalAmount = $("#detailIn").getCol("totalAmount", false, 'sum');
        var sumCostAmount = $("#detailIn").getCol("costAmount", false, 'sum');
        var sumCost = $("#detailIn").getCol("cost", false, 'sum');
        var sumProfit_ = $("#detailIn").getCol("profit", false, 'sum');
        var sumTotalPayAmount = $("#detailIn").getCol("totalPayAmount", false, 'sum');
        var sumNoPayAmount = $("#detailIn").getCol("noPayAmount", false, 'sum');
        $("#detailIn").footerData('set', {
            'code': '合计',
            totalAmount: sumTotalAmount,
            costAmount: sumCostAmount,
            freeAmount: sumCost,
            cost: sumCost,
            profit: sumProfit_,
            totalPayAmount: sumTotalPayAmount,
            noPayAmount: sumNoPayAmount
        })
    }
</script>
</body>
</html>