<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>

<html>
<head>
</head>
<body>
<style>
</style>
<div>
    <table id="processList"></table>
    <div id="processPager"></div>
</div>
<div style="width:100%;text-align:center;">
    <a href="javascript:;" style="margin-right:5px;" class="btn-blue btn-s" id="yes">确认</a>
    <a href="javascript:;" style="margin-right:5px;display:none" class="btn-blue btn-s" id="create">生成</a>
    <a href="javascript:;" class="btn-blue btn-s" id="myclose">关闭</a>
</div>

<script type="text/javascript">
    if ("${param.flag}" == 1) {
        $("#create").show();
        $("#yes").hide();
    }
    $('#processList').jqGrid({
        footerrow: true,
        datatype: "local",
        data: makeRow,
        forceFit: true,
        height: 200,
        sortname:"num",
        width: $("#other").width() * 0.98,
        viewrecords: true,
        pager: '#processPager',
        rowList:[ 10, 20, 30 ],
        rowNum:10,
        jsonReader: {
            records: "total",
            total: "totalpages",
        },
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {
                name: 'costAnalyzeBillId',
                label: 'costAnalyzeBillId',
                hidden: true,
                formatter: function (value, options, row) {
                    return row.id;
                }
            },
            {name: 'num', label: 'num', sortable: true,hidden: true},
            {name: 'goodsId', label: 'goodsId', hidden: true},
            {name: 'goodsSpecId', label: 'goodsSpecId', hidden: true},
            {name: 'purchase', label: 'purchase', hidden: true},
            {name: 'goodsName', label: '货品', sortable: false, align: 'center', width: "100px"},
            {name: 'goodsSpecName', label: '货品属性', sortable: false, align: 'center', width: "100px"},
            {name: 'totalPrice', label: '总价', sortable: false, align: 'center', width: "100px"},
            {name: 'goodsQuentity', label: '货品数量', sortable: false, align: 'center', width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).numberbox({
                            width: "100%",
                            height: "80%",
                            precision: 2,
                            required: true,
                            min: 0,
                            validType: "numMaxLength[10]"
                        });
                        cacl($(cl));
                        $(cl).numberbox("textbox").focus(function () {
                            $(this).select();
                        });
                    }
                }
            },
            {
                name: 'saleAmount',
                label: '销售金额',
                sortable: false,
                align: 'center',
                formatter: priceFormat,
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).numberbox({
                            width: "100%",
                            height: "80%",
                            min: 0,
                            precision: 2,
                            required: true,
                            validType: "numMaxLength[10]"
                        });
                        cacl($(cl));
                        $(cl).numberbox("textbox").focus(function () {
                            $(this).select();
                        });
                    }
                }
            },
            {
                name: 'salePrice',
                label: '销售单价',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: priceFormat,
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).numberbox({
                            width: "100%",
                            height: "80%",
                            precision: 4,
                            required: true,
                            min: 0,
                            validType: "numMaxLength[10]"
                        });
                        cacl($(cl));
                        $(cl).numberbox("textbox").focus(function () {
                            $(this).select();
                        });
                    }
                }
            },
            {
                name: 'transportDate',
                label: '运输日期',
                sortable: false,
                align: 'center',
                width: "110px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).datebox({
                            width: "100%",
                            height: "80%",
                            required: true,
                            editable: false,
                            validType: "dateCompar['" + today + "',-1,'运输日期不能比今天早']"
                        });
                    }
                }
            },
        ],
        loadComplete: function () {
            var totalPrice=0;
            for (var i in makeRow) {
                processEdit(makeRow[i].id);
                totalPrice+=parseFloat(makeRow[i].totalPrice);
            }
            //合计栏初始化
            $("#processList").footerData('set',{
                goodsName:"合计",
                goodsSpecName:'',
                totalPrice:totalPrice.toFixed(2),
                goodsQuentity:"",
                saleAmount:"",
                salePrice:""
            });
        }
    }).navGrid('#processPager', {add: false, del: false, edit: false, search: false, view: false});

    function _getEditor(index, name) {
        return $('#processList #' + index + "_" + name);
    }
    function _getText(index, name) {
        return $('#processList #' + index + " td[aria-describedby=processList_" + name + "]");
    }

    //反计算
    function cacl(target) {
        var ind = target.closest('tr.jqgrow').attr("id");
        if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
            target.numberbox('textbox').bind('propertychange', function () {
                quentity(this, ind);
            });// IE专用
        } else {
            target.numberbox('textbox').bind('input', function () {
                quentity(this, ind);
            });
        }
    }
    function quentity(target, ind) {
        var tname = $(target).parent().prev().attr("textboxname");
        if (tname != "saleAmount") {
            var goodsQuentity = _getEditor(ind, 'goodsQuentity').numberbox("getText");
            var salePrice = _getEditor(ind, 'salePrice').numberbox("getText");
            var saleAmount = _getEditor(ind, 'saleAmount')
            var result = (goodsQuentity * salePrice).toFixed(4);
            saleAmount.numberbox('setValue', result);
        } else {
            var goodsQuentity = _getEditor(ind, 'goodsQuentity').numberbox("getText");
            var salePrice = _getEditor(ind, 'salePrice')
            var saleAmount = _getEditor(ind, 'saleAmount').numberbox("getText");
            var result = (saleAmount / goodsQuentity).toFixed(4);
            salePrice.numberbox('setValue', result);
        }
        //这是我写的
        var res=0,res1=0;
        if(tname=='goodsQuentity'){
            for (var i in makeRow) {
                var goodsnum = _getEditor(makeRow[i].id,'goodsQuentity').numberbox("getText");
                if(isNaN(Number(goodsnum)))
                    res += 0;
                else
                    res += Number(goodsnum);
            }
            getFootertar('goodsQuentity').text(parseFloat(res).toFixed(2));
        }
        for (var i in makeRow) {
            var saleamount = _getEditor(makeRow[i].id,'saleAmount').numberbox("getText");
            if(isNaN(Number(saleamount)))
                res1 += 0;
            else
                res1 += Number(saleamount);
        }
        getFootertar('saleAmount').text(parseFloat(res1).toFixed(2));
    }
    //还有这个也是我写的  明天我就给删了
    function getFootertar(name) {
        return $('.footrow [aria-describedby="processList_'+name+'"]')
    }
    function processEdit(index) {
        $('#processList').jqGrid("editRow", index);
        $('#processList').jqGrid("setRowData", index, {editing: true});
    }

    $('#yes').click(function () {
        $('#processList').form("enableValidation");
        if (!$('#processList').form("validate")) {
            return false;
        }
        var rows = $('#processList').jqGrid("getRowData");
        for (var i in rows) {
            var transportDate = $('#processList #' + rows[i].id + "_transportDate").datebox("getValue");
            $('#processList').jqGrid("saveRow", rows[i].id);
            $('#processList').jqGrid("setRowData", rows[i].id, {
                transportDate: transportDate
            });
        }
        rows = $('#processList').jqGrid("getRowData");
        var planGoods = JSON.stringify(rows);
        localStorage["planGoods"] = planGoods;
        var url = "/timeAxis/axis?lsread=1";
        parent.kk(url, "新增计划");
    });

    $("#create").click(function () {
        $('#processList').form("enableValidation");
        if (!$('#processList').form("validate")) {
            return false;
        }
        var rows = $('#processList').jqGrid("getRowData");
        for (var i in rows) {
            var transportDate = $('#processList #' + rows[i].id + "_transportDate").datebox("getValue");
            $('#processList').jqGrid("saveRow", rows[i].id);
            $('#processList').jqGrid("setRowData", rows[i].id, {
                transportDate: transportDate
            });
        }
        rows = $('#processList').jqGrid("getRowData");
        if ("${param.flag}" == 1) {
            $.ajax({
                url: "${ctx}/api/costAnalysisBill/genFlow?" + planVo,
                async: false,
                type: "put",
                data: {planGoodsJson: JSON.stringify(rows)},
                success: function (data) {
                    if (data.returnCode == 0) {
                        $.fool.alert({
                            time: 1000, msg: '生成成功！', fn: function () {
                                window.location.href = "${ctx}/flow/plan/axis?id=" + data.data;
                            }
                        });
                    } else {
                        $.fool.alert({time: 1000, msg: data.message});
                    }
                },
                error: function () {
                    $.fool.alert({msg: "服务器繁忙，请稍后再试！"});
                }
            });
        }
    });

    $('#myclose').click(function () {
        $('#other').window("close");
    });
</script>
</body>
</html>