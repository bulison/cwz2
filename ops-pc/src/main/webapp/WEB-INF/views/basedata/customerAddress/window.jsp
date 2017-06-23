<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>客户、供应商默认收发货地址设定</title>
</head>
<body>
<div class="mywin">
    <div class="mybtn" style="margin:5px;">
        <a href="javascript:;" style="margin-right:5px;" class="btn-ora-add" id="add-list" onclick="addNew()">新增</a>
        <a href="javascript:;" style="margin-right:5px;" class="btn-blue btn-s" id="select-ok">设为默认</a>
        <a href="javascript:;" class="btn-blue btn-s" id="myclose">关闭</a>
    </div>
    <table id="addressList"></table>
    <div id="addPager"></div>
</div>
<script>
    var csvId = '${param.csvId}';
    var csvName = $('#name').val();
    var csvType = '${param.csvType}';
    var listDate = '';
    $('#addressList').jqGrid({
        datatype: function (postdata) {
            $.ajax({
                url: "${ctx}/api/customerAddress/list?csvId=" + csvId,
                data: postdata,
                dataType: "json",
                complete: function (data, stat) {
                    if (stat == "success") {
                        listDate = data.responseJSON;
                        listDate.totalpages = Math.ceil(listDate.total / postdata.rows);
                        $("#addressList")[0].addJSONData(listDate);
                    }
                }

            });
        },
        forceFit: true,
        pager: '#addPager',
        //rowList:[ 10, 20, 30 ],
        pgbuttons: false,
        pginput: false,
        viewrecords: true,
        /* 	rowNum:10,
         jsonReader:{
         records:"total",
         total: "totalpages",
         },  */
        height: 200,
        width: $("#pop-win").width() * 0.98,
        ondblClickRow: function (rowid, iRow, iCol, e) {
            if (iCol != 0 && $("#addressList #" + rowid).find(".btn-save").length <= 0) {
                myedit(rowid);
            }
        },
        colModel: [
            {
                name: 'action',
                label: '操作',
                align: "center",
                sortable: false,
                width: 100,
                formatter: function (value, options, row ) {
                    var f = '<a class="btn-ok" title="此为默认地址"></a>';
                    var d = '<a class="btn-del" title="删除" href="javascript:;" onclick="mydel(\'' + row.id + '\',\'' + options.rowId + '\')"></a> ';
                    var c = '<input type="button" style="border:0px;" class="btn-cancel" title="撤销" onclick="mycancel(\'' + options.rowId + '\')" /> ';
                    var s = '<input type="button" style="border:0px;" class="btn-save" title="保存" onclick="mysave(\'' + options.rowId + '\')" /> ';
                    if (row.editing) {
                        return s + c;
                    } else if(row.fdefault == 1 || value == 1){
                        return f;
                    }else{
                        return d;
                    }
                }
            },
            {name: 'id', label: 'id', hidden: true, width: 100},
            {name: 'updateTime', label: 'updateTime', hidden: true, width: 100},
            {name: 'newRow', label: 'newRow', hidden: true, width: 100},
            {
                name: 'fdefault',
                label: '是否默认',
                align: "center",
                sortable: false,
                width: 100,
                formatter: function (value) {
                    if (value == 1) {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {name: 'customerId', label: 'customerId', hidden: true, width: 100},
            {name: 'addressName', label: 'addressName', hidden: true, width: 100, editable: true, edittype: "text"},
            {name: 'addressId', label: 'addressId', hidden: true, width: 100, editable: true, edittype: "text"},
            {
                name: 'customerName',
                align: "center",
                sortable: false,
                label: '${param.csvType == 1?"客户":"供应商"}',
                width: 100
            },
            {
                name: 'customerType',
                align: "center",
                sortable: false,
                label: '类型',
                width: 100,
                formatter: function (value) {
                    if (value) {
                        return value == 1 ? "客户" : "供应商";
                    } else {
                        return "";
                    }
                }
            },
            {
                name: 'address',
                label: '${param.csvType == 1?"收货":"发货"}地址',
                align: "center",
                sortable: false,
                width: 100,
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        var index = $(cl).closest("tr.jqgrow").attr("id");
                        $(cl).combotree({
                            width: "100%",
                            height: "80%",
                            method: "get",
                            required: true,
                            value: getEditor("addressId", index).val(),
                            url: getRootPath() + "/api/freightAddress/findAddressTree?enable=1",
                            editable: false,
                            onSelect: function (node) {
                                getEditor("addressName", index).val(node.name);
                                getEditor("addressId", index).val(node.fid);
                            },
                            onBeforeSelect: function (node) {
                                if (node.flag == 0) {
                                    $.fool.alert({msg: "请选择最详细的地址名"});
                                    return false;
                                }
                            }
                        });
                    }
                },
                formatter: function (value, options, row) {
                    return row.addressName;
                }
            },
            {
                name: 'describe',
                align: "center",
                sortable: false,
                label: '备注',
                width: 100,
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).textbox({
                            width: "100%",
                            height: "80%"
                        });
                    }
                }
            }
        ],
    }).navGrid('#addPager', {add: false, del: false, edit: false, search: false, view: false});

    $('#add-list').click(function () {
            var oldLength = listDate.length;
            var index = $('#addressList').jqGrid("getRowData").length + 1;
            var x = index - oldLength;
            if( x >= 2 ){
                $.fool.alert({
                    msg:"请先保存本行数据！",time:1000
                });
                return ;
            }
            $('#addressList').jqGrid("addRowData", index, {
                customerType: csvType,
                customerName: csvName,
                customerId: csvId,
                newRow: 1
            }, "first");
            myedit(index);
    });
    $('#myclose').click(function () {
        $('#pop-win').window("close");
    });

    function getEditor(name, index) {
        return $('#addressList #' + index + "_" + name);
    }
    function getText(name, index) {
        return $('#addressList #' + index + " td[aria-describedby=billList_" + name + "]");
    }
    function myedit(index) {
        var editRow = $("table#addressList tr[editable = '1']");
        var rowData = $("#addressList").jqGrid("getRowData",index);
        if(editRow.length >= 1){//第一次点击的时候，是0
            $.fool.alert({
                msg:"请先保存本行数据！",time:1000
            });
            return;
        }
        if(rowData.fdefault == "是"){
            $("#addressList").jqGrid("setColProp",'address',{editable:false});
        }
        $("#addressList").jqGrid("editRow", index);
        $("#addressList").jqGrid("setRowData", index, {editing: true, action: null});
    }
    function mydel(fid, index) {
        $.fool.confirm({
            title: "删除提示", msg: "确认删除该项记录？", fn: function (r) {
                if (r) {
                    $.ajax({
                        url: getRootPath() + "/api/customerAddress/delete?id=" + fid,
                        type: "delete",
                        dataType: 'json',
                        success: function (data) {
                            if (data.returnCode == 0) {
                                $.fool.alert({
                                    msg: "删除成功！", time: 1000, fn: function () {
                                        $('#addressList').jqGrid("delRowData", index);
                                    }
                                });
                                $("#addressList").trigger("reloadGrid");
                            } else {
                                $.fool.alert({
                                    msg: data.message, fn: function () {
                                    }
                                });
                            }
                        },
                        error: function () {
                            $.fool.alert({
                                msg: "服务器繁忙，请稍后再试！", fn: function () {
                                }
                            });
                        }
                    });
                } else {
                    return false;
                }
            }
        })

    }
    function mysave(index) {
        var row = $('#addressList').jqGrid("getRowData", index);
        if (!$("#addressList #" + index).form("validate")) {
            return false;
        }
        $("#addressList #" + index + " .btn-save").attr("disabled", "disabled");
        $("#addressList #" + index + " .btn-cancel").attr("disabled", "disabled");
        var id = row.id;
        var updateTime = row.updateTime;
        var describe = getEditor("describe", index).textbox("getValue");
        var addressId = "";
        if(row.fdefault == "是"){
            addressId = getEditor("addressId", index).val();
        }else {
            addressId = getEditor("address", index).combotree("getValue");
        }
        var data = {
            updateTime: updateTime,
            id: id,
            customerId: csvId,
            customerName: csvName,
            customerType: csvType,
            describe: describe,
            addressId: addressId
        };


        $.ajax({
            url: getRootPath() + "/api/customerAddress/save",
            type: "put",
            dataType: 'json',
            data: data,
            success: function (data) {
                if (data.returnCode == 0) {
                    $.fool.alert({
                        msg: "保存成功！", time: 1000, fn: function () {
                            $("#addressList #" + index + " .btn-save").removeAttr("disabled");
                            $("#addressList #" + index + " .btn-cancel").removeAttr("disabled");
                            $('#addressList').jqGrid("saveRow", index);
                            $('#addressList').jqGrid("setRowData", index, {
                                editing: false,
                                action: null,
                                fdefault: data.data.fdefault,
                                id: data.data.id,
                                updateTime: data.data.updateTime
                            });
                            $('#addressList').trigger("reloadGrid");
                            $("#addressList").jqGrid("setColProp",'address',{editable:true});
                        }
                    });
                } else {
                    $.fool.alert({
                        msg: data.message, fn: function () {
                            $("#addressList #" + index + " .btn-save").removeAttr("disabled");
                            $("#addressList #" + index + " .btn-cancel").removeAttr("disabled");
                        }
                    });
                }
            },
            error: function () {
                $.fool.alert({
                    msg: "服务器繁忙，请稍后再试！", fn: function () {
                        $("#addressList #" + index + " .btn-save").removeAttr("disabled");
                        $("#addressList #" + index + " .btn-cancel").removeAttr("disabled");
                    }
                });
            }
        });
    }
    function mycancel(index) {
        var row = $('#addressList').jqGrid("getRowData", index);
        if (row.newRow == 1) {
            $('#addressList').jqGrid("delRowData", index);
        } else {
            $('#addressList').jqGrid("restoreRow", index);
            if(row.fdefault == "是"){
                $('#addressList').jqGrid("setRowData", index, {editing: false, action: 1});
                $("#addressList").jqGrid("setColProp",'address',{editable:true});
            }else {
                $('#addressList').jqGrid("setRowData", index, {editing: false, action: null});
            }
        }
    }

    $('#select-ok').click(function () {
        var index = $('#addressList').jqGrid("getGridParam", "selrow");
        if (index == null) {
            $.fool.alert({
                msg: "请选择地址！", fn: function () {
                }
            });
            return false;
        }
        var row = $('#addressList').jqGrid("getRowData", index);
        $.ajax({
            url: getRootPath() + "/api/customerAddress/updateDefault?id=" + row.id,
            type: "put",
            dataType: 'json',
            success: function (data) {
                if (data.returnCode == 0) {
                    $.fool.alert({
                        msg: "设置默认成功！", time: 1000, fn: function () {
                            $('#addressList').trigger("reloadGrid");
                        }
                    });
                } else {
                    $.fool.alert({
                        msg: data.message, fn: function () {
                        }
                    });
                }
            },
            error: function () {
                $.fool.alert({
                    msg: "服务器繁忙，请稍后再试！", fn: function () {
                    }
                });
            }
        });
    });
</script>
</body>
</html>