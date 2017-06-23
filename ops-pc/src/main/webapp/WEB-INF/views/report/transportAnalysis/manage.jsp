<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <title>运输路线分析</title>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <script type="text/javascript"
            src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript"
            src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/echarts.min.js?v=${js_v}"></script>
    <style>
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
            top: 10px;
            left: 2%;
            width: 96%;
            display: none;
            position: absolute;
            border: #ccc 1px solid;
        }

        #mychart {
            margin-left: 50px;
            height: 400px;
            position: relative;
            z-index: 10000;
        }

        .chart-close {
            float: right;
            width: 40px;
            font-size: 14px;
            margin-right: 20px;
            margin-top: 10px;
        }

        .toolBox-button input {
            border: 0;
        }

        .grey {
            background-color: #DBDBDB;
            border-color: #DBDBDB;
        }
    </style>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>运输路线分析</h1>
    </div>
</div>
<div class="box">
    <form id="form" class="form1">
        <p><font>日期：</font><input id="search-billDate" name="billDate" class="textBox"/></p>
        <p><font>货品：</font><input id="search-goodsId" name="goodsId" class="textBox"/></p>
        <p><font>发货地：</font><input id="search-delivery" name="deliveryPlaceId" class="textBox"/></p>
        <p><font><em style="color:red;">*</em>收货地：</font><input id="search-receipt" name="receiptPlaceId"
                                                                class="textBox"/></p>
        <a href="javascript:;" id="search" href="javascript:;" class="btn-blue4 btn-s">查询</a>
        <a href="javascript:;" id="clear" href="javascript:;" class="btn-blue4 btn-s">清空</a>
    </form>
    <div class="toolBox-button" style="margin-top:10px;">
        <input type="button" id="export" class="btn-ora-export" style="float:left;margin-right:5px;" value="导出"/>
        <input type="button" id="make" class="btn-ora-introduce" style="float:left;" value="生成流程 "/>
    </div>
</div>
<table id="mylist"></table>
<div id="pager"></div>
<div id="pop-win"></div>
<div id="other"></div>
<div id="chart">
    <div id="closeChart" class="chart-close"><a class="mybtn-close" style="width:100%;">关闭</a></div>
    <form id="chartForm" class="form1" style="padding:0 10px;">
        <p>
            <input id="startDay" class="textBox" name="startDay"/>
        </p>
        -
        <p>
            <input id="endDay" class="textBox" name="endDay"/>
        </p>
        <a href="javascript:;" id="search2" href="javascript:;" class="btn-blue4 btn-s">查询</a>
        <a href="javascript:;" id="clear2" href="javascript:;" class="btn-blue4 btn-s">清空</a>
    </form>
    <div id="mychart"></div>
    <div id="closeChart" class="chart-close"><a class="mybtn-cancel" style="width:100%;"></a></div>
</div>
<script>
    //生成流程选中row存储变量
    var makeRow = [];
    var goods = "";
    var win = "";
    var win2 = "";
    var myoption = {};
    var today = new Date().format("yyyy-MM-dd");
    var myrow = [];
    var mydisableEdit = false;
    $('#mychart').width($(window).width() - 100);
    var dom = document.getElementById("mychart");
    var chart = echarts.init(dom);
    $('#startDay').fool("datebox", {
        width: 160,
        height: 30,
        inputDate: true,
        focusShow: true,
        validType: "dateCompare",
        prompt: '开始日期'
    });
    $('#endDay').fool("datebox", {
        width: 160,
        height: 30,
        inputDate: true,
        focusShow: true,
        required: false,
        //value:mydate,
        prompt: '结束日期'
    });
    $('#search2').click(function () {
        $('#chartForm').form("enableValidation");
        if (!$('#chartForm').form("validate")) {
            return false
        }
        ;
        var totalPrice = [];
        var date = [];
        var data = $('#chartForm').serializeJson();
        myoption = $.extend(myoption, data);
        $.ajax({
            url: '${ctx}/costAnalysisBill/chart?purchase=0',
            type: "get",
            async: false,
            data: myoption,
            success: function (data) {
                for (var i in data) {
                    date.push(getDateStr(data[i].billDate));
                    totalPrice.push(data[i].totalPrice);
                }
            }
        });
        var option = chartOpt(date, totalPrice, myrow);
        chart.setOption(option);
        $('#chart').slideDown(500);
    });
    $('#clear2').click(function () {
        $('#chartForm').form("clear");
        $('#startDay').datebox("setValue", start);
        $('#endDay').datebox("setValue", today);
    });
    //导出
    $('#export').click(function () {
        $('#form').form('enableValidation');
        if ($('#form').form('validate')) {
            var option = $('#form').serializeJson();
            option['billDate'] = $('#search-billDate').datebox("getValue");;
            option['purchase'] = 0;
            exportFrom("${ctx}/costAnalysisBill/export", option);
        }
    });
    function exportFrom(url, jsonObj) {
        if (url.indexOf('?') < 0) url += '?';
        for (var item in jsonObj) {
            var jValue = jsonObj[item];//key所对应的value
            url += '&';
            url += item;
            url += '=';
            url += jValue;
        }
        url = encodeURI(encodeURI(url));

        $.ajax({
            type: "GET",
            url: url,
            success: function (data) {
                window.location.href = url;
            },
            error: function (e) {
                $.fool.alert({msg: e.responseJSON.error});
            }
        });
    }

    $('#make').click(function () {
        var rowids = $('#mylist').jqGrid('getGridParam', 'selarrrow');
        if (rowids.length == 0) {
            $.fool.alert({msg: "请先选择记录！"});
            return false;
        }
        makeRow = [];
        var ids = "";
        for (var i = 0; i < rowids.length; i++) {
            var row = $('#mylist').jqGrid("getRowData", rowids[i]);
            ids = row.id + "," + ids;
            makeRow.push(row);
        }
        var a = checkTemp(ids);
        if (!a) {
            return false;
        }
        win = $("#other").fool('window', {
            modal: true,
            'title': "生成流程",
            width: 780,
            height: 380,
            href: getRootPath() + '/costAnalysisBill/process?type=1'
        });
    });
    function checkTemp(ids) {
        var a = false;
        $.ajax({
            url: getRootPath() + '/flow/planTemplateRelation/settleAllTemplate',
            data: {ids: ids},
            type: "post",
            async: false,
            success: function (data) {
                if (data.returnCode == 1) {
                    if (data.errorCode == "301") {
                        var mydata = data.data;
                        for (var i in mydata) {
                            var str = "采购";
                            if (mydata[i].type == 2) {
                                str = "运输";
                            } else if (mydata[i].type == 3) {
                                str = "销售";
                            }
                            /*  $.fool.alert({msg:mydata[i].deliveryPlace+"-"+mydata[i].receiptPlace+mydata[i].goodsName+mydata[i].specName+"找不到"+str+"模板，请先新增。",fn:function(){
                             var url = "/flow/planTemplate/manage?openAdder=1&templateType="+mydata[i].type+"&dataId="+mydata[i].dataId;
                             parent.kk(url,"计划模板");
                             }}); */
                            $.fool.alert({
                                btnName: '新增模板',
                                yesbtnName: "取消",
                                btnAct: 'ptbtn(this,\'' + mydata[i].type + '\',\'' + mydata[i].dataId + '\')',
                                msg: mydata[i].deliveryPlace + "-" + mydata[i].receiptPlace + mydata[i].goodsName + mydata[i].specName + "找不到" + str + "模板，请先新增。",
                                fn: function () {
                                }
                            });
                        }

                    } else {
                        $.fool.alert({msg: data.message});
                    }
                } else {
                    a = true;
                }
            },
            error: function () {
                $.fool.alert({msg: "服务器繁忙，请稍后再试！"});
            }
        });
        return a;
    }
    function ptbtn(target, type, id) {
        $(target).next().click();
        var url = "/flow/planTemplate/manage?openAdder=1&templateType=" + type + "&dataId=" + id;
        parent.kk(url, "计划模板");
    }
    $.ajax({
        url: "${ctx}/basedata/query?num=" + Math.random(),
        async: false,
        data: {param: "Goods"},
        success: function (data) {
            goods = formatData(data.Goods, "fid");
        }
    });
    $('#search-billDate').fool("datebox", {
        width: 160,
        height: 30,
        inputDate: true,
        focusShow: true,
        required: false,
        //disabled:true,
        prompt: '选择日期',
        value: today,
        onSelect: function (date) {
            var mydate = $('#search-billDate').datebox("getValue");
            var a = parseInt(mydate.replace("-", "").replace("-", ""));
            var b = parseInt(today.replace("-", "").replace("-", ""));
            if (a - b < 0) {
                mydisableEdit = true;
            } else {
                mydisableEdit = false;
            }
        }
    });
    var goodsCombo = $('#search-goodsId').fool('dhxComboGrid', {
        width: 160,
        height: 30,
        prompt: "搜索选择货品",
        focusShow: true,
        filterUrl: getRootPath() + '/goods/vagueSearch?searchSize=8',
        setTemplate: {
            input: "#name#",
            columns: [
                {option: '#code#', header: '编号', width: 100},
                {option: '#name#', header: '名称', width: 100},
                {option: '#barCode#', header: '条码', width: 100},
                {option: '#spec#', header: '规格', width: 100},
                {option: '#unitName#', header: '单位', width: 100},
            ],
        },
        toolsBar: {
            refresh: true
        },
        data: goods
    });
    $('#search-delivery').combotree({
        prompt: '选择地址',
        width: 160,
        height: 30,
        method: "get",
        url: getRootPath() + "/api/freightAddress/findAddressTree?enable=1",
        editable: false
    });
    $('#search-receipt').combotree({
        prompt: '选择地址',
        required: true,
        novalidate: true,
        width: 160,
        height: 30,
        method: "get",
        url: getRootPath() + "/api/freightAddress/findAddressTree?enable=1",
        editable: false
    });
    $.extend($.fn.validatebox.defaults.rules, {
        dateCompare: {
            validator: function (value) {
                var start = new Date(value.replace("-", "/").replace("-", "/"));
                var end = new Date($('#endDay').datebox("getValue").replace("-", "/").replace("-", "/"));
                return end - start >= 0;
            },
            message: '结束日期不能比开始日期早！'
        }
    });

    $("#mylist").jqGrid({
        datatype: function (postdata) {
            if (!postdata.receiptPlaceId) {
                return false;
            }
            if (!postdata.billDate) {
                postdata = $.extend(postdata, {billDate: today});
            }
            $.ajax({
                url: '${ctx}/api/costAnalysisBill/query?purchase=0',
                type: "get",
                data: postdata,
                dataType: "json",
                complete: function (data, stat) {
                    if (stat == "success") {
                        data.responseJSON.content.total = data.responseJSON.totalElements;
                        data.responseJSON.content.totalpages = Math.ceil(data.responseJSON.totalElements / postdata.rows);
                        $("#mylist")[0].addJSONData(data.responseJSON.content);
                    }
                }
            });
        },
        width: $(window).width(),
        shrinkToFit: false,
        autoScroll: false,
        height: "100%",
        viewrecords: true,
        multiselect: true,
        //footerrow:true,
        pager: '#pager',
        rowList: [10, 20, 30],
        rowNum: 10,
        jsonReader: {
            records: "total",
            total: "totalpages",
        },
        colModel: [
            {
                name: 'action',
                label: '操作',
                sortable: false,
                align: 'center',
                frozen: true,
                width: "100px",
                formatter: function (value, options, row) {
                    var cost = "<a href='javascript:;' class='btn-initialize' title='成本分析图' onclick='costMap(\"" + options.rowId + "\")'></a> ";
                    var detail = "<a href='javascript:;' class='btn-edit' title='明细' onclick='detail(\"" + row.id + "\")'></a> ";
                    var xstemp = "<a href='javascript:;' class='btn-bills' title='销售模板' onclick='xstemp(\"" + row.id + "\")'></a> ";
                    if (row.customerId) {
                        xstemp = mydisableEdit ? "" : xstemp;
                        return detail + cost + xstemp;
                    }
                    return detail + cost;
                }
            },
            {name: 'goodsName', label: '货品', sortable: false, align: 'center', width: "100px", frozen: true},
            {name: 'goodsSpecName', label: '货品属性', sortable: false, align: 'center', width: "100px", frozen: true},
            {name: 'deliveryPlaceName', label: '发货地', sortable: false, align: 'center', width: "100px", frozen: true},
            {name: 'receiptPlaceName', label: '收货地', sortable: false, align: 'center', width: "100px", frozen: true},
            {name: 'id', label: 'id', hidden: true, frozen: true},
            {name: 'goodsId', label: '货品Id', hidden: true},
            {name: 'supplierId', label: 'supplierId', hidden: true},
            {name: 'goodsSpecId', label: 'goodsSpecId', hidden: true},
            {name: 'route', label: 'route', hidden: true},
            {name: 'deliveryPlaceId', label: '发货地Id', hidden: true},
            {name: 'receiptPlaceId', label: '收货地Id', hidden: true},
            {name: 'unitName', label: '货品单位', sortable: false, align: 'center', width: "100px"},
            {
                name: 'factoryPrice',
                label: '出厂价',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: priceFormat
            },
            {
                name: 'freightPrice',
                label: '运输费用',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: priceFormat
            },
            {name: 'loss', label: '损耗（元）', sortable: false, align: 'center', width: "100px", formatter: priceFormat},
            {name: 'totalPrice', label: '总价', sortable: false, align: 'center', width: "100px", formatter: priceFormat},
            {name: 'expectedDays', label: '预计时间', sortable: false, align: 'center', width: "100px"},
            {name: 'remark', label: '备注', sortable: false, align: 'center', width: "100px"},
        ],
    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false}).jqGrid('setFrozenColumns');
    function getDate(time) {
        time = parseInt(time);
        var _date = new Date(time);
        var today = _date.getFullYear() + "-" + (_date.getMonth() + 1) + "-" + _date.getDate();
        return today;
    }
    function xstemp(fid) {
        $.ajax({
            url: getRootPath() + '/flow/planTemplateRelation/settleSaleTemplate',
            data: {id: fid},
            type: "post",
            success: function (data) {
                if (data.returnCode == 0) {
                    var url = "/flow/planTemplate/manage?openAdder=1&templateType=3&dataId=${obj.id}&tempId=" + data.data.planTemplateId;
                    parent.kk(url, "计划模板");
                } else {
                    if (data.errorCode == "301") {
                        $.fool.alert({
                            msg: "找不到销售模板，请先新增。", fn: function () {
                                var url = "/flow/planTemplate/manage?openAdder=1&templateType=3&dataId=${obj.id}";
                                parent.kk(url, "计划模板");
                            }
                        });
                    } else {
                        $.fool.alert({msg: data.message});
                    }
                }
            },
            error: function () {
                $.fool.alert({msg: "服务器繁忙，请稍后再试！"});
            }
        });
    }
    function costMap(index) {
        var _date = new Date().getTime();
        var today = getDate(_date);
        var start = getDate(_date - 30 * 24 * 3600 * 1000);
        $('#startDay').datebox("setValue", start);
        $('#endDay').datebox("setValue", today);
        var row = $('#mylist').jqGrid("getRowData", index);
        myrow = row;
        var totalPrice = [];
        var date = [];
        myoption = {
            startDay: start,
            endDay: today,
            goodsSpecId: row.goodsSpecId,
            supplierId: row.supplierId,
            route: row.route,
            goodsId: row.goodsId,
            deliveryPlaceId: row.deliveryPlaceId,
            receiptPlaceId: row.receiptPlaceId
        };
        $.ajax({
            url: '${ctx}/costAnalysisBill/chart?purchase=0',
            type: "get",
            async: false,
            data: myoption,
            success: function (data) {
                for (var i in data) {
                    date.push(getDateStr(data[i].billDate));
                    totalPrice.push(data[i].totalPrice);
                }
            }
        });
        var option = chartOpt(date, totalPrice, myrow);
        chart.setOption(option);
        $('#chart').slideDown(500);
    }
    function chartOpt(data1, data2, row) {
        var option = {
            title: {
                text: row.deliveryPlaceName + "--" + row.receiptPlaceName + " " + row.goodsName + '历史价格折线图'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['线路价格']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: data1
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    name: '线路价格',
                    type: 'line',
                    data: data2
                }
            ]
        };
        return option;
    }
    function detail(fid) {
        var row = $('#mylist').jqGrid("getRowData", fid);
        myrow = row;
        win = $("#pop-win").fool('window', {
            modal: true,
            'title': "查看明细",
            top: 10 + $(window).scrollTop(),
            left: 0,
            width: $(window).width() - 10,
            height: $(window).height() - 60,
            href: getRootPath() + '/costAnalysisBill/edit?type=2&id=' + fid
        });
    }

    function priceFormat(value) {
        if (isNaN(value)) {
            return 0;
        } else {
            return value;
        }
    }
    $('#search').click(function () {
        $('#form').form('enableValidation');
        if ($('#form').form('validate')) {
            var option = $('#form').serializeJson();
            $('#mylist').jqGrid('setGridParam', {postData: option}).trigger("reloadGrid");
            if (mydisableEdit) {
                $('.toolBox-button input[id!=export]').addClass("grey").attr("disabled", "disabled");
            } else {
                $('.toolBox-button input[id!=export]').removeClass("grey").removeAttr("disabled");
            }
        }
    });
    $('#clear').click(function () {
        $('#form').form('clear');
        goodsCombo.setComboText("");
        $('#search-billDate').datebox("setValue", today);
        $('#form').form("validate");

    });
    $('#closeChart').click(function () {
        $('#chart').slideUp(500);
        chart.clear();
    });
</script>
</body>
</html>