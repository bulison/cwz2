<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <title>运输损耗设置表</title>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
    <link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css"/>
    <style>
        #search-form span {
            margin-right: 5px;
            margin-bottom: 10px
        }

        #search-form a {
            margin-right: 5px;
            margin-bottom: 10px
        }

        #Inquiry {
            margin-left: 5px;
        }

        #grabble {
            float: right;
        }

        #bolting {
            width: 160px;
            height: 27px;
            position: relative;
            border: 1px solid #ccc;
            background: #fff;
            margin-right: 25px;
        }

        .button_a {
            display: block;
            width: 25px;
            height: 25px;
            background: #76D5FC;
            -moz-border-radius: 3px;
            /* Gecko browsers */
            -webkit-border-radius: 3px;
            /* Webkit browsers */
            border-radius: 3px;
            /* W3C syntax */
            float: right;
            right: 29px;
            top: 63px;
            position: absolute;
        }

        .button_span {
            width: 0;
            height: 0;
            border-left: 8px solid transparent;
            border-right: 8px solid transparent;
            border-top: 8px solid #fff;
            top: 10px;
            position: absolute;
            right: 5px;
        }

        /*设置下拉框*/
        .input_div {
            display: none;
            background: #F5F5F5;
            padding: 10px 0px 5px 0px;
            border: 1px solid #D5DBEA;
            position: absolute;
            right: 23px;
            top: 93px;
            z-index: 1;
        }

        .input_div p {
            font-size: 12px;
            color: #747474;
            vertical-align: middle;
            text-align: right;
            margin: 0 20px 0 10px;
            width: 240px;
        }

        #bill p.hideOut {
            display: none;
        }
    </style>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class="Icon"></span>
        <div class="trian"></div>
        <h1>运输损耗设置表</h1>
    </div>
</div>
<div style="margin:5px 0px 5px 0px">
    <a href="javascript:;" id="add" class="btn-ora-add"
       style="vertical-align:top ">新增 </a>
    <input id="search-code" type="text" calss="textBox" name="code" value=""/>
    <a href="javascript:;" id="search" class="Inquiry btn-blue btn-s " style="margin-left: 5px">查询</a>
</div>

<table id="lossList"></table>
<div id="pager"></div>
<div id="addBox"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">

    //查询
    $('#search-code').textbox({
        prompt: '输入货品名称查询',
        width: 160,
        height: 32
    });

    $("#search").click(function () {
        var code = $("#search-code").textbox('getValue');
        var options = {searchKey: code};
        $('#lossList').jqGrid('setGridParam', {postData: options}).trigger("reloadGrid");
    });

    //当光标选中文本框，监听回车，执行查询
    $(function () {
        $("#search-code").textbox("textbox").bind("keydown", function (e) {
            if (e.keyCode == 13) {
                $("#search").click();
            }
        })
    });

    //新增、修改、详情窗口初始化
    $('#addBox').window({
        top: 50,
        collapsible: false,
        minimizable: false,
        resizable: false,
        closed: true,
        modal: true,
        width: 400
    });

    var listDate = "";

    $("#lossList").jqGrid({
        datatype: function (postdata) {
            $.ajax({
                url: '${ctx}/transportLoss/list',
                data: postdata,
                dataType: 'json',
                complete: function (data, stat) {
                    if (stat == "success") {
                        listDate = data.responseJSON;
                        listDate.totalpages = Math.ceil(listDate.total / postdata.rows);
                        $('#lossList')[0].addJSONData(listDate);
                    }
                }
            })
        },
        forceFit: true,
        pager: '#pager',
        rowList: [10, 20, 30],
        viewrecords: true,
        rowNum: 10,
        jsonReader: {
            records: "total",
            total: "totalpages"
        },
        autowidth: true,//自动填满宽度
        height: $(window).outerHeight() - 200 + "px",
        colModel: [/*{
            name: 'isNew',
            label: 'isNew',
            width: 100,
            align: 'center',
            hidden: true
        }, */{
            name: 'editing',
            label: 'editing',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'orgId',
            label: 'orgId',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'deliveryId',
            label: 'deliveryId',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'receiptId',
            label: 'receiptId',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'shipmentId',
            label: 'shipmentId',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'createId',
            label: 'createId',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'fid',
            label: 'fid',
            width: 100,
            align: 'center',
            hidden: true
        },{
            name: 'accountId',
            label: 'accountId',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'goodsId',
            label: 'goodsId',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'goodsSpecId',
            label: 'goodsSpecId',
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'updateTime',
            label: "更新日期",
            width: 100,
            align: 'center',
            hidden: true
        }, {
            name: 'action',
            label: '操作',
            width: 100,
            align: 'center',
            formatter: function (cellvalue, options, rowObject) {
                var e = '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editRow(\'' + options.rowId + '\')"></a>';
                var r = '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\'' + options.rowId + '\',\'' + rowObject.fid + '\')"></a>';
                if (rowObject.editing) {
                    var s = '<a  href="javascript:;" title="保存" class="btn-save" onclick="saveRow(\'' + options.rowId + '\',\'' + rowObject.fid + '\')" ></a>';
                    var c = '<a  href="javascript:;" title="取消" class="btn-cancel " onclick="cancelRow(\'' + options.rowId + '\'/*,\'' + rowObject.isNew + '\'*/)" ></a>';
                    return s + "" + c;
                } else {
                    return e + "" + r;
                }
            }
        }, {
            name: 'goods',
            label: '货品',
            width: 100,
            align: 'center',
            editable: true,
            edittype: "text"
        }, {
            name: 'goodsSpec',
            label: '货品属性',
            width: 100,
            align: 'center',
            editable: true,
            edittype: "text"
        }, {
            name: 'delivery',
            label: '发货地',
            width: 100,
            align: 'center',
            editable: true,
            edittype: "text"
        }, {
            name: 'receipt',
            label: '收货地',
            width: 100,
            align: 'center',
            editable: true,
            edittype: "text"
        }, {
            name: 'shipment',
            label: '装运方式',
            width: 100,
            align: 'center',
            editable: true,
            edittype: "text"
        }, {
            name: 'paymentAmonut',
            label: '损耗（%）',
            width: 50,
            align: 'center',
            editable: true,
            edittype: "text",
            formatter: 'currency',
            editoptions: {
                decimalPlaces: 2,
            }
        }],
        gridComplete:function () {
            $("#lossList").parents(".ui-jqgrid-bdiv").css("overflow-x","hidden");
        }

    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false});

    //设置鼠标放进去自动出来下拉列表
    $("#search-form").find("input[_class]").each(function (i, n) {
        ($(this))
    });

    //编辑行
    function editRow(rowid) {
        var rowEdit = $("table#lossList tr[editable = '1']");
        if (rowEdit.length > 0) {
            $.fool.alert({
                msg: '请先保存本行！', time: 1000
            });
            return false;
        }
        var rowData = $("#lossList").jqGrid('getRowData', rowid);
        if (rowData.editing == "true") {
            return;
        }
        rowData.editing = true;
        rowData.action = null;
        $('#lossList').jqGrid('setRowData', rowid, rowData);
        $("#lossList").jqGrid("editRow", rowid);

        //损耗
        getEditor("lossList", rowid, "paymentAmonut").numberbox({
            min:-99.99,
            max: 99.99,
            precision: 2
        });

        //货品属性
        function goodsSpecC(goodsSpecGroupId) {
            var goodsSpecCombo = getEditor("lossList", rowid, "goodsSpec").fool('dhxComboGrid', {
                width: "100%",
                height: "80%",
                value: rowData.goodsSpecId,
                focusShow: true,
                editable: true,
                novalidate: true,
                data: getComboData("${ctx}/goodsspec/vagueSearch?parentId=" + goodsSpecGroupId),
                filterUrl: "${ctx}/goodsspec/vagueSearch?parentId=" + goodsSpecGroupId,
                setTemplate: {
                    input: "#name#",
                    columns: [
                        {option: '#code#', header: '编号', width: 100},
                        {option: '#name#', header: '名称', width: 100}
                    ]
                },
                toolsBar: {
                    name: '货品属性',
                    addUrl: '/goodsspec/manage',
                    refresh: true
                },
                onLoadSuccess: function (combo) {
                    combo.deleteOption("");
                }
            });
            if (goodsSpecGroupId == undefined) {
                goodsSpecCombo.disable();
            } else {
                goodsSpecCombo.enable();
            }

        }

        //货品
        var goodsCombo = getEditor("lossList", rowid, "goods").fool('dhxComboGrid', {
            novalidate: true,
            width: 160,
            height: 31,
            focusShow: true,
            value: rowData.goodsId,
            data: getComboData(getRootPath() + '/goods/vagueSearch?searchSize=8'),
            /*validType:["combogridValid['goodsId']","nocobgValid['_goodsName']"],*///验证有没有goodsId，_goodsName
            filterUrl: getRootPath() + '/goods/vagueSearch?searchSize=8',//模糊查询
            setTemplate: {
                input: "#name#",
                columns: [
                    {option: '#code#', header: '编号', width: 100},
                    {option: '#name#', header: '名称', width: 100}
                ]
            },
            toolsBar: {
                name: '新增货品',
                addUrl: '/goods/manage',
                refresh: true
            },
            onLoadSuccess: function (combo) {
                combo.deleteOption("");
            },
            onSelectionChange: function () {
                goodsSpecC();
                var _goodsId = (getEditor("lossList", rowid, "goods").next())[0].comboObj.getSelectedValue();
                toGoosId(_goodsId);
                if (_goodsId) {
                    (getEditor("lossList", rowid, "goodsSpec").next())[0].comboObj.setComboValue("");
                    (getEditor("lossList", rowid, "goodsSpec").next())[0].comboObj.setComboText("");
                }
            }

        });

        //发货地
        var deliveryValue = "";
        $.ajax({
            url: "${ctx}/basedata/transportLoss",
            async: false,
            success: function (data) {
                deliveryValue = formatTree(data[0].children, "text", "id")//数据需要格式化
            }
        });

        var deliveryName = getEditor("lossList", rowid, "delivery").fool("dhxCombo", {
            width: "100%",
            height: "80%",
            data: deliveryValue,
            focusShow: true,
            editable: false,
            novalidate: true,
            setTemplate: {
                input: "#name#",
                option: "#name#"
            },
            toolsBar: {
                name: '损耗地址',
                addUrl: '/basedata/listAuxiliaryAttr',
                refresh: true
            },
            onLoadSuccess: function (combo) {
                combo.deleteOption("");
                combo.setComboValue(rowData.deliveryId);
            }
        });

        //收货地
        var receiptName = getEditor("lossList", rowid, "receipt").fool("dhxCombo", {
            width: "100%",
            height: "80%",
            data: deliveryValue,
            focusShow: true,
            editable: false,
            novalidate: true,
            setTemplate: {
                input: "#name#",
                option: "#name#"
            },
            toolsBar: {
                name: '损耗地址',
                addUrl: '/basedata/listAuxiliaryAttr',
                refresh: true
            },
            onLoadSuccess: function (combo) {
                combo.deleteOption("");
                combo.setComboValue(rowData.receiptId);
            }
        });

        //运装方式
        var shipmentValue = '';
        $.ajax({
            url: "${ctx}/basedata/findSubAuxiliaryAttrTree?code=020&num=" + Math.random(),
            async: false,
            success: function (data) {
                shipmentValue = formatTree(data[0].children, 'text', 'id');
            }
        });
        var shipmentName = getEditor("lossList", rowid, "shipment").fool("dhxCombo", {
            width: 182,
            height: 32,
            data: shipmentValue,
            clearOpt: false,
            editable: false,
            focusShow: true,
            novalidate: true,
            value: rowData.shipmentId,
            setTemplate: {
                input: "#name#",
                option: "#name#"
            },
            toolsBar: {
                name: "装运方式",
                addUrl: "/basedata/listAuxiliaryAttr",
                refresh: true
            }
        });

        function toGoosId(_goodsId) {
            $.ajax({
                url: getRootPath() + '/goods/getGoodsById?fid=' + _goodsId,
                datatype: 'json',
                success: function (data) {
                    var goodsSpecGroupId = data.goodsSpecGroupId;
                    goodsSpecC(goodsSpecGroupId);
                }
            })
        }

        return goodsSpecC();
    }


    //新增行
    $("#add").click(function () {
        var rowEdit = $("table#lossList tr[editable = '1']");
        var oldLength = listDate.rows.length;
        var newIndex = $("#lossList").jqGrid("getRowData").length + 1;
        var x = newIndex - oldLength;
        if (rowEdit.length > 0) {
            $.fool.alert({
                msg: '请先保存本行！', time: 1000
            });
            return false;
        }
        if (x >= 2) {
            $.fool.alert({
                msg: "请先保存本行数据！", time: 1000
            });
            return;
        }
        //将新添加的行插入到第一列
        var dataRow = {
            deliveryId: "",
            receiptId: "",
            shipmentId: "",
            createId: "",
            fid: "",
            orgId: "",
            accountId: "",
            goodsId: "",
            goodsSpecId: "",
            updateTime: "",
            goods: "",
            goodsSpec: "",
            delivery: "",
            receipt: "",
            shipment: "",
            paymentAmonut: ""
        };
        $("#lossList").jqGrid("addRowData", newIndex, dataRow, newIndex);
        editRow(newIndex);
    });

    //删除行
    function removeById(index, fid) {
        $.fool.confirm({
            title: '确认', msg: '确认删除此数据吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/transportLoss/delete',
                        data: {id: fid},
                        dataType: 'json',
                        success: function (data) {
                            if (data.result == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '删除成功', fn: function () {
                                        $('#lossList').trigger("reloadGrid");
                                    }
                                });
                            } else {
                                $.fool.alert({
                                    time: 1000, msg: '删除成功', fn: function () {
                                        $('#lossList').trigger("reloadGrid");
                                    }
                                });
                            }
                        },
                        error: function () {
                            $.fool.alert({time: 1000, msg: "系统繁忙，稍后再试！"});
                        }
                    })
                }
            }
        })
    }


    //取消编辑
    function cancelRow(index) {
        var rowData = $('#lossList').jqGrid('getRowData', index);
        rowData.action = null;
        rowData.edting = false;
        $("#lossList").jqGrid("setRowData", index, rowData);
        $("#lossList").jqGrid("restoreRow", index);
        $("#lossList").trigger("reloadGrid");

    }


    //保存行
    function saveRow(index, id) {
        var rowData = $('#lossList').jqGrid("getRowData", index);
        var b = getEditor("lossList", index, "goods");
        var c = getEditor("lossList", index, "goodsSpec");
        var d = getEditor("lossList", index, "delivery");
        var e = getEditor("lossList", index, "receipt");
        var f = getEditor("lossList", index, "shipment");
        var g = getEditor("lossList", index, "paymentAmonut");
        $(b[0]).closest("tr").form("enableValidation");
        $(c[0]).closest("tr").form("enableValidation");
        $(d[0]).closest("tr").form("enableValidation");
        $(e[0]).closest("tr").form("enableValidation");
        $(f[0]).closest("tr").form("enableValidation");
        $("#lossList#" + index + ".btn-save").attr("disabled", "disabled");
        //获取货品
        var paymentAmonut = $(g[0]).val();
        var goods = ($(b[0]).next())[0].comboObj.getSelectedValue();
        var shipment = ($(f[0]).next())[0].comboObj.getSelectedValue();
        var delivery = ($(d[0]).next())[0].comboObj.getSelectedValue();
        var receipt = ($(e[0]).next())[0].comboObj.getSelectedValue();
        var goodsSpec = ($(c[0]).next())[0].comboObj.getSelectedValue();
        if(goods == null){
            $.fool.alert({
                msg:"没有选择货品，请重新选择！",time:1000
            });
            return ;
        }
        $.post("${ctx}/transportLoss/save", {
            fid: rowData.fid,
            goodsId: goods,
            goodsSpecId: goodsSpec,
            deliveryId: delivery,
            receiptId: receipt,
            shipmentId: shipment,
            paymentAmonut: paymentAmonut
        }, function (data) {
            dataDispose(data);
            if (data.result == '0') {
                $.fool.alert({
                    time: 1000, msg: '保存成功！', fn: function () {
                        $("#lossList").trigger("reloadGrid");
                    }
                });
            } else if (data.result == '1') {
                $.fool.alert({
                    msg: data.msg, fn: function () {
                        $("#lossList #" + index + ".btn-save").removeAttr("disabled");
                    }
                });
            } else {
                $.fool.alert({
                    msg: "系统繁忙，请稍后再试。", fn: function () {
                        $("#lossList #" + index + ".btn-save").removeAttr("disabled");
                    }
                })
            }
        });


    }
</script>
</body>
</html>
