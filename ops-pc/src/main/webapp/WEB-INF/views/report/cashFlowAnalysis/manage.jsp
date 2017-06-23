<%--
  Created by IntelliJ IDEA.
  User: yidie
  Date: 2017/3/6
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <title>现金流分析</title>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css"/>
    <link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css"/>
    <script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/echarts.min.js?v=${js_v}"></script>
</head>
<style type="text/css">
    .form1 p font {
        width: 115px;
    }

    .form1 p {
        margin: 5px 0;
    }

    .form1 {
        padding: 5px 0;
        margin-top: 10px;
    }

    .box {
        margin: 10px 0;
    }

    .mybtn-close {
        display: inline-block;
        width: 40px;
        height: 40px;
        background: url(/ops-pc/resources/images/collapse.png) no-repeat;
        opacity: 0.5;
        padding-left: 20px;
        cursor: pointer
    }

    .mybtn-close:hover {
        display: inline-block;
        width: 40px;
        height: 40px;
        background: url(/ops-pc/resources/images/collapse.png) no-repeat;
        opacity: 1;
        padding-left: 20px;
        cursor: pointer
    }

    #chart {
        z-index: 9999;
        background: #fff;
        margin-top: 15px;
        width: 100%;
        height: 79%;
        position: absolute;
        border: #ccc 1px solid;
    }

    #mychart {
        margin-left: 50px;
        height: 400px;
        position: relative;
        z-index: 10000;
    }

</style>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>现金流分析</h1>
    </div>
</div>
<div class="box">
    <form id="form" class="form1">
        <p><font>开始日期：</font><input id="startDay" name="startDay" class="textBox">
            <font>结束日期：</font><input id="endDay" name="endDay" class="textBox">
            <a href="javascript:;" id="search" class="btn-blue4 btn-s">查询</a>
            <a href="javascript:;" id="export" class="btn-ora-export">导出</a>
            <span style="vertical-align: middle">
                <input id="showChart" type="radio" name="show" checked="checked" value="0" onclick="rCheck()">
                显示图表
                <input id="showList" type="radio" name="show" value="1" onclick="rCheck()">
                 显示列表
            </span>
            <span id="showFlag" style="display: none;vertical-align: middle">
            <input id="flag" type="checkbox" name="flag" checked="checked">
            分页
        </span>
        </p>
        <%-- 折线图 --%>
        <div id="chart" class="chart">
            <div id="mychart"></div>
        </div>
    </form>
</div>
<table id="cashlist" class="cashlist"></table>
<div id="pager"></div>

<script type="text/javascript">
    var showChart = "";
    //日期
    var today = new Date().format("yyyy-MM-dd");
    var y = new Date().getFullYear();
    var m = new Date().getMonth() + 2;
    var d = new Date().getDate();
    var enday = y + "-" + m + "-" + d;

    //判断radio选中状态，获取选中值
    function rCheck() {
        var show = $('input:radio[name="show"]:checked').val();
        if (show == '0') {
            $(".cashlist").css("display", "none");
            $(".chart").css("display", "");
            $("#showFlag").css("display", "none");
        } else if (show == '1') {
            $(".chart").css("display", "none");
            $(".cashlist").css("display", "");
            $("#showFlag").css("display", "inline-block");
        } else {
            $(".cashlist").css("display", "none");
            $(".chart").css("display", "");
        }
    }

    /*查询条件日期*/
    $('#startDay').fool("datebox", {
        width: 160,
        height: 30,
        inputDate: true,
        focusShow: true,
        required: false,
        prompt: '开始日期',
        disabled: true,
        value: today
    });

    $('#endDay').fool("datebox", {
        width: 160,
        height: 30,
        inputDate: true,
        focusShow: true,
        required: false,
        prompt: '结束日期',
        value: enday
    });

    //查询
    $("#search").click(function () {
        var endDay = $("#endDay").textbox("getValue");
        $("#cashlist").jqGrid("setGridParam", {
            postData: {
                "startDay": today,
                "endDay": endDay
            }
        }).trigger("reloadGrid");
        /*        /report/cashFlowAnalysis/list?startDay=&endDay=*/
        $.ajax({
            url: '${ctx}/report/cashFlowAnalysis/list',
            type: 'get',
            data: {
                "startDay": today,
                "endDay": endDay
            },
            success: function (data) {
                showChart = true;
                var paymentDate = [];//横坐标
                var income = [];//预计收入
                var expenditure = [];//预计支出
                var amount = [];//结余金额
                var warningQuota = "";//预警线
                var charData = data.rows;//取到rows
                if(showChart) {
                    for (var i = 0; i < charData.length; i++) {
                        var warning = charData[0].warningQuota;
                        warningQuota = parseInt(warning);//转化为数字类型 toFixed函数只能针对数字类型函数使用
                        paymentDate.push(charData[i].paymentDate);
                        income.push(charData[i].income);
                        amount.push(charData[i].amount);
                        expenditure.push(charData[i].expenditure);
                    }
                }
                var option = charOpt(amount, income, expenditure, paymentDate, warningQuota);
                chart.getOption(option);//setOption也可以,但是日志会报错
                showChart = false;
            }
        })
    });


    //导出
    $('#export').click(function () {
        var endDay = $("#endDay").textbox("getValue");
        exportFrom("${ctx}/report/cashFlowAnalysis/export", {"startDay": today, "endDay": endDay});
    });

    //分页
    $("#flag").click(function () {
        if ($("#flag").prop("checked")) {//选中为true
            $("#pager").show();
            $("#cashlist").jqGrid("setGridParam", {
                postData: {
                    flag: 1
                }
            }).trigger("reloadGrid");
        } else {
            $("#pager").hide();
            $("#cashlist").jqGrid("setGridParam", {
                postData: {
                    flag: 0
                }
            }).trigger("reloadGrid");
        }
    });

    $('#cashlist').jqGrid({
        datatype: function (postdata) {
            if ($("#flag").prop("checked")) {
                postdata['rows'] = 10;
            } else {
                postdata['rows'] = 99999;
            }
            $.ajax({
                url: "${ctx}/report/cashFlowAnalysis/list",
                data: postdata,
                dataType: "json",
                complete: function (data, stat) {
                    if (stat == "success") {
                        data.responseJSON.totalpages = Math.ceil(data.responseJSON.total / postdata.rows);
                        $("#cashlist")[0].addJSONData(data.responseJSON);
                    }

                }
            });
        },
        forceFit: true,
        pager: '#pager',
        rowList: [10, 20, 30],
        viewrecords: true,
        footerrow:true,
        jsonReader: {
            records: "total",
            total: "totalpages"
        },
        autowidth: true,//自动填满宽度
        height: $(window).outerHeight() - 215 + "px",
        colModel: [
            {
                name: "fid",
                label: "fid",
                align: "center",
                hidden: true
            }, {
                name: 'colour',
                label: 'colour',
                align: 'center',
                width: 100,
                hidden: true

            }, {
                name: "paymentDate",
                label: '预计收付日期',
                align: "center",
                width: 100
            }, {
                name: "income",
                label: '预计收入',
                align: "center",
                width: 100,
                formatter: 'currency',
                formatoptions: {
                    decimalPlaces: 2
                }
            }, {
                name: "expenditure",
                label: '预计支出',
                align: "center",
                formatter: 'currency',
                formatoptions: {
                    decimalPlaces: 2
                },
                width: 100
            }, {
                name: "amount",
                label: '结余金额',
                align: "center",
                formatter: 'currency',
                formatoptions: {
                    decimalPlaces: 2
                },
                width: 100
            }
        ],
        gridComplete: function () {//获取colour不为空的行，并设置此行为红色
            var rowData = $("#cashlist").jqGrid("getRowData");//获取表格所有数据
            for (var i = 0; i < rowData.length; i++) {
                var colour = rowData[i].colour;
                if (colour != "") {
                    $(this).jqGrid("setRowData", i + 1, false, {background: '#ffe1e1'});
                }
            }

            //底部添加合计
            var sumIncome = $("#cashlist").getCol("income",false,'sum');
            var sumExpenditure= $("#cashlist").getCol("expenditure",false,'sum');
            var sumAmount = $("#cashlist").getCol("amount",false,'sum');
            $("#cashlist").footerData("set",{
                'paymentDate':'合计',
                income:sumIncome,
                expenditure:sumExpenditure,
                amount:sumAmount
            })
        }

    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false});

    //折线图
    //设置折线图宽度
    $("#mychart").width($(window).width() - 100);
    var dom = document.getElementById("mychart");
    var chart = echarts.init(dom);
    var paymentDate = [];//横坐标
    var income = [];//预计收入
    var expenditure = [];//预计支出
    var amount = [];//结余金额
    var warningQuota = "";//预警线
    $.ajax({
        url: "${ctx}/report/cashFlowAnalysis/list",
        ataType: "json",
        success: function (data) {
            var charData = data.rows;//取到rows
            var warning = charData[0].warningQuota;
            warningQuota = parseFloat(warningQuota);//要点击查询后才显示数据，开始预警额度显示空
            if(showChart) {
                for (var i = 0; i < charData.length; i++) {
                    paymentDate.push(charData[i].paymentDate);
                    income.push(charData[i].income);
                    amount.push(charData[i].amount);
                    expenditure.push(charData[i].expenditure);
                }
                warningQuota = parseFloat(warning);//转化为数字类型 toFixed函数只能针对数字类型函数使用
            }
            charOpt(amount, income, expenditure, paymentDate, warningQuota);
        }
    });
    // 基于准备好的dom，初始化echarts图表
    function charOpt(amount, income, expenditure, paymentDate, warningQuota) {
        var option = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['预警额度', '预计支出', '结余金额', '预计收入']
            },
            grid: {
                left: '3%',
                right: '5%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {
                        name:'现金流汇总分析'
                    }
                }
            },
            xAxis: {//横坐标
                type: 'category',
                boundaryGap: false,
                data: paymentDate
            },
            yAxis: {
                type: 'value',
            },
            series: [
                {
                    name: '预警额度',
                    type: 'line',
                    markLine: {
                        data: [{
                            name: "预警",
                            yAxis: warningQuota
                        }]
                    }
                },
                {
                    name: '预计收入',
                    type: 'line',
                    data: income
                },
                {
                    name: '预计支出',
                    type: 'line',
                    data: expenditure
                    /*                    markLine:{
                     data:[{
                     name:"预警",
                     yAxis:warningQuota
                     }]
                     }*/
                },
                {
                    name: '结余金额',
                    type: 'line',
                    stack: '总量',
                    data: amount
                }
            ]

        };
        // 为echarts对象加载数据
        chart.setOption(option);
        paymentDate = "";//横坐标
        income = "";//预计收入
        expenditure = "";//预计支出
        amount = "";//结余金额
        warningQuota = "";//预警线
    }
</script>
</body>
</html>
