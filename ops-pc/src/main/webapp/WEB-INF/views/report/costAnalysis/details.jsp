<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>

<html>
<head>
</head>
<body>
<c:set var="isCost" value="${param.type == 1}"></c:set>
<style>
    .mybtn {
        margin-top: 10px;
        width: 100%;
        text-align: center;
    }

    #form2 {
        margin-bottom: 10px;
    }

    #form2 input[readonly=readonly] {
        background: rgb(209, 217, 224);
    }

    #form2 input[readonly=true] {
        background: rgb(209, 217, 224);
    }
</style>
<div class="form1">
    <form id="form2">
        <input name="id" id="id" type="hidden" value="${obj.id}"/>
        <input name="billDate" id="billDate" type="hidden" value="${obj.billDate}"/>
        <input name="goodsId" id="goodsId" type="hidden" value="${obj.goodsId}"/>
        <input name="goodsSpecId" id="goodsSpecId" type="hidden" value="${obj.goodsSpecId}"/>
        <input name="supplierId" id="supplierId" type="hidden" value="${obj.supplierId}"/>
        <input name="deliveryPlaceId" id="deliveryPlaceId" type="hidden" value="${obj.deliveryPlaceId}"/>
        <input name="receiptPlaceId" id="receiptPlaceId" type="hidden" value="${obj.receiptPlaceId}"/>
        <input name="defaultAddressId" id="defaultAddressId" type="hidden" value="${obj.defaultAddressId}"/>
        <input name="customerId" id="customerId" type="hidden" value="${obj.customerId}"/>
        <input name="updateTime" id="updateTime" type="hidden" value="${obj.updateTime}"/>
        <p><font>采购日期：</font><input id="purchaseDate" name="purchaseDate" readonly="readonly" type="text"
                                    class="textBox" value="${obj.purchaseDate}"/></p>
        <p><font>采购单位：</font><input id="supplierName" name="supplierName" readonly="readonly" type="text"
                                    class="textBox" value="${obj.supplierName}"/></p>
        <p><font>发货地：</font><input id="deliveryPlaceName" name="deliveryPlaceName" readonly="readonly" type="text"
                                   class="textBox" value="${obj.deliveryPlaceName}"/></p>
        <p><font>收货地：</font><input id="receiptPlaceName" name="receiptPlaceName" readonly="readonly" type="text"
                                   class="textBox" value="${obj.receiptPlaceName}"/></p>
        <p><font>货品单位：</font><input id="unitName" name="unitName" type="text" readonly="readonly" class="textBox"
                                    value="${obj.unitName}"/></p>
        <p><font>货品名称：</font><input id="goodsName" name="goodsName" type="text" readonly="readonly" class="textBox"
                                    value="${obj.goodsName}"/></p>
        <p><font>货品属性：</font><input id="goodsSpecName" name="goodsSpecName" readonly="readonly" type="text"
                                    class="textBox" value="${obj.goodsSpecName}"/></p>
        <p><font>出厂价：</font><input id="factoryPrice" name="factoryPrice" readonly="readonly" type="text" class="textBox"
                                   value="${obj.factoryPrice}"/></p>
        <c:if test="${isCost}">
            <p><font><em>*</em>对外出厂价：</font><input id="publishFactoryPrice" name="publishFactoryPrice" type="text"
                                                   class="textBox" value="${obj.publishFactoryPrice}"/></p>
        </c:if>
        <p><font>运费：</font><input id="freightPrice" name="freightPrice" readonly="readonly" type="text" class="textBox"
                                  value="${obj.freightPrice}"/></p>
        <c:if test="${isCost}">
            <p><font>对外运费：</font><input id="publishFreightPrice" name="publishFreightPrice" readonly="readonly"
                                        type="text" class="textBox" value="${obj.publishFreightPrice}"/></p>
        </c:if>
        <p><font>执行标识：</font><input id="executeSign" name="executeSign" readonly="readonly" type="text" class="textBox"
                                    value="${obj.executeSign}"/></p>
        <p><font>总价：</font><input id="totalPrice" name="totalPrice" readonly="readonly" type="text" class="textBox"
                                  value="${obj.totalPrice}"/></p>
        <p><font>损耗（元）：</font><input id="loss" name="loss" readonly="readonly" type="text" class="textBox"
                                  value="${obj.loss}"/></p>
        <c:if test="${isCost}">
            <p><font>对外总价：</font><input id="publishTotalPrice" name="publishTotalPrice" readonly="readonly" type="text"
                                        class="textBox" value="${obj.publishTotalPrice}"/></p>
        </c:if>
        <p><font>预计时间：</font><input id="expectedDays" name="expectedDays" type="text" readonly="readonly"
                                    class="textBox" value="${obj.expectedDays}"/></p>
        <input id="purchase" name="purchase" class="textBox" value="${obj.purchase}" type="hidden"/>
        <%-- <input id="route" name="route" class="textBox" value="${obj.route}" type="hidden"/> --%>
        <p><font>备注：</font><input id="remark" name="remark" type="text" class="textBox" value="${obj.remark}"/></p>
    </form>
    <table id="detailList"></table>
    <div class="mybtn">
        <input type="button" id="save" class="btn-blue2 btn-xs" value="${param.type==1?'保存/调价':'保存' }"/>
        <c:if test="${isCost}">
            <input type="button" id="myIssue" class="btn-blue2 btn-xs" value="发布"/>
            <input type="button" id="cgTemp" class="btn-blue2 btn-xs" value="采购模板"/>
        </c:if>
        <c:if test="${!empty obj.customerId}">
            <input type="button" id="xsTemp" class="btn-blue2 btn-xs" value="销售模板"/>
        </c:if>
        <input type="button" id="myMake" class="btn-blue2 btn-xs" value="生成流程"/>
        <input type="button" id="costChart" class="btn-blue2 btn-xs" value="成本分析图"/>
    </div>
    <br>
    <table id="costList"></table>
    <div id="pager"></div>
</div>

<script type="text/javascript">
	if(mydisableEdit){
		$('.mybtn input[id!=costChart]').addClass("grey").attr("disabled","disabled");
		$('#publishFactoryPrice').attr("readonly","readonly");
		$('#remark').attr("readonly","readonly");
	}
    var chooserWindow = "";
    var details = ${obj.details};
    var _index = 0;
    //var key = [];
    var _type = "${param.type}";
    var execute = [{value: 1, text: "可执行"}, {value: 2, text: "难执行"}, {value: 3, text: "无法执行"}];
    $('#publishFactoryPrice').numberbox({
        width: 160,
        height: 30,
        precision: 2,
        validType: "numMaxLength[10]"
    });
    $('#remark').textbox({
        width: 440,
        height: 30,
        validType: "maxLength[100]",
    });
    $("#executeSign").fool("dhxCombo", {
        width: 162,
        height: 31,
        data: execute,
        required: true,
        clearOpt: false,
        editable: false
    });
    $("#executeSign").next()[0].comboObj.disable();
    $('#detailList').jqGrid({
        datatype: 'local',
        width: $('#pop-win').width() * 0.98,
        height: "100%",
        viewrecords: true,
        onCellSelect: function (rowid, iCol, cellcontent, e) {
        	if(mydisableEdit){return false};
        	var col = 21;
            <c:if test="${isCost}">
            col = 22;
            </c:if>
            if (iCol != col && $("#detailList #" + rowid).find(".btn-save").length <= 0) {
                myedit(rowid);
            }
        },
        onSelectRow: function () {//鼠标点击某一行时触发
            var rowId = $("#detailList").jqGrid('getGridParam', 'selrow');//获取鼠标点中行的id
            var _details = $('#detailList').jqGrid('getRowData', rowId);//通过id获取行中的对象
            var _detailsNo = _details.no;//获取对象中的 "no"的值
            var _id = details[_detailsNo - 1].transportBillId;
            costShow(_id);
           // console.log(_id);
            _id = "";
        },
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'conversionRate', label: 'conversionRate', hidden: true},
            {name: 'transportBillId', label: 'transportBillId', hidden: true},
            {name: 'deliveryPlaceId', label: '发货地ID', hidden: true},
            {name: 'receiptPlaceId', label: '收货地ID', hidden: true},
            {name: 'transportTypeId', label: '运输方式ID', hidden: true},
            {name: 'shipmentTypeId', label: '装运方式ID', hidden: true},
            {name: 'supplierId', label: '运输公司ID', hidden: true},
            {name: 'transportUnitId', label: '运输单位ID', hidden: true},
            {name: 'no', label: '序号', sortable: false, align: 'center', width: "100px"},
            {
                name: 'billDate', label: '日期', align: 'center', width: "100px", formatter: function (value) {
                return getDateStr(value);
            }
            },
            {name: 'updateTime', label: 'updateTime', hidden: true},
            {name: 'deliveryPlaceName', label: '发货地', sortable: false, align: 'center', width: "100px"},
            {name: 'receiptPlaceName', label: '收货地', sortable: false, align: 'center', width: "100px"},
            {name: 'supplierName', label: '运输公司', sortable: false, align: 'center', width: "100px"},
            {name: 'transportTypeName', label: '运输方式', sortable: false, align: 'center', width: "100px"},
            {name: 'shipmentTypeName', label: '装运方式', sortable: false, align: 'center', width: "100px"},
            /* {name: 'transportUnitName', label: '运输单位', sortable: false, align: 'center', width: "100px"},
            {
                name: 'freightPrice',
                label: '运费',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: priceFormat
            },
            <c:if test="${isCost}">
            {
                name: 'publishFreightPrice',
                label: '对外运费',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: priceFormat,
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        var id = $(cl).closest("tr.jqgrow").attr("id");
                        var row = $('#detailList').jqGrid("getRowData", id);
                        $(cl).numberbox({
                            width: "100%",
                            height: "80%",
                            precision: 2,
                            required: true,
                            validType: "numMaxLength[10]"
                        });
                        if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
                            $(cl).numberbox("textbox").bind('propertychange', function () {
                                var total = $(this).val() / parseFloat(row.conversionRate);
                                getEditor("publishBasePrice", id).numberbox("setValue", total);
                            });// IE专用
                        } else {
                            $(cl).numberbox("textbox").bind('input', function () {
                                var total = $(this).val() / parseFloat(row.conversionRate);
                                getEditor("publishBasePrice", id).numberbox("setValue", total);
                            });
                        }
                    }
                }
            },
            </c:if>
            {
                name: 'conversionRate',
                label: '换算率',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: priceFormat
            }, */
            {
                name: 'basePrice',
                label: '基本运费',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: priceFormat
            },
            <c:if test="${isCost}">
            {
                name: 'publishBasePrice',
                label: '对外基本运费',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: priceFormat,
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        var id = $(cl).closest("tr.jqgrow").attr("id");
                        var row = $('#detailList').jqGrid("getRowData", id);
                        $(cl).numberbox({
                            width: "100%",
                            height: "80%",
                            precision: 2,
                            required: true,
                            validType: "numMaxLength[10]"
                        });
                        /* if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
                            $(cl).numberbox("textbox").bind('propertychange', function () {
                                var total = $(this).val() * parseFloat(row.conversionRate);
                                getEditor("publishFreightPrice", id).numberbox("setValue", total);
                            });// IE专用
                        } else {
                            $(cl).numberbox("textbox").bind('input', function () {
                                var total = $(this).val() * parseFloat(row.conversionRate);
                                getEditor("publishFreightPrice", id).numberbox("setValue", total);
                            });
                        } */
                    }
                }
            },
            </c:if>
            /* {name : 'publishFactoryPrice',label : '对外出厂价',align:'center',width:"100px",formatter:priceFormat,editable:true,edittype:"text",editoptions:{dataInit:function(cl){
             $(cl).numberbox({
             width:"100%",
             height:"80%",
             precision:2,
             required:true,
             validType:"numMaxLength[10]"
             });
             }}},  */
            {
                name: 'expectedDays',
                label: '预计时间',
                sortable: false,
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).numberbox({
                            width: "100%",
                            height: "80%",
                            precision: 0,
                            required: true,
                            validType: "numMaxLength[10]"
                        });
                    }
                }
            },
            /*  {name : 'groundCostPrice',label : '场地费用单价',align:'center',width:"100px",editable:true,edittype:"text",editoptions:{dataInit:function(cl){
             $(cl).numberbox({
             width:"100%",
             height:"80%",
             precision:2,
             required:true,
             validType:"numMaxLength[10]"
             });
             }}}, */
            {
                name: 'executeSign',
                label: '执行标识',
                sortable: false,
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        var id = $(cl).closest("tr.jqgrow").attr("id");
                        var row = $('#detailList').jqGrid("getRowData", id);
                        var index = parseInt(row.no);
                        $(cl).fool("dhxCombo", {
                            width: "100%",
                            height: "80%",
                            data: execute,
                            required: true,
                            clearOpt: false,
                            editable: false,
                            /* onChange:function(value,text){
                             key[index-1] = value;
                             var t = 0;
                             for(var i in key){
                             t = key[i]>t?key[i]:t;
                             }
                             $('#executeSign').next()[0].comboObj.setComboValue(t);
                             } */
                        });
                    }
                },
                formatter: function (value) {
                    return value == 1 ? "可执行" : value == 2 ? "难执行" : value == 3 ? "无法执行" : "";
                },
                unformat: function (value) {
                    return value == "可执行" ? 1 : value == "难执行" ? 2 : value == "无法执行" ? 3 : "";
                }
            },
            {
                name: 'remark',
                label: '备注',
                sortable: false,
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).textbox({
                            width: "100%",
                            height: "80%",
                            validType: "maxLength[100]",
                        });
                    }
                }
            },
            {
                name: 'action',
                label: '操作',
                sortable: false,
                align: 'center',
                width: "100px",
                formatter: function (value, options, row) {
                    var other = "<a href='javascript:;' class='btn-company' title='其他公司' onclick='otherCompany(\"" + options.rowId + "\")'></a> ";
                    var c = '<input type="button" style="border:0px;" class="btn-cancel" title="撤销" onclick="mycancel(\'' + options.rowId + '\')" /> ';
                    var s = '<input type="button" style="border:0px;" class="btn-save" title="保存" onclick="mysave(\'' + options.rowId + '\')" /> ';
                    var setTransTemp = "<a href='javascript:;' class='btn-initialize' title='设置路径模板按钮' onclick='setTemp(\"" + row.id + "\")'></a> ";
                    if(mydisableEdit){
                    	return "";
                    }
                    if (row.editing) {
                        return s + c;
                    } else {
                        return other + setTransTemp;
                    }
                }
            },
        ],

    });

    var fydata = "";

    function costShow(_id) {
        $.ajax({
                url: getRootPath() + '/api/costAnalysisBillDetail/queryTransportPrice?transportBillId=' + _id,
                dataType:"json",
                success: function(data){
                    fydata = data;
                    $("#costList")[0].addJSONData(fydata);//加载数据
                }
            });

        $('#costList').jqGrid({//从2表
            datatype:'local',
            data:fydata,
            viewrecords: true,
            rownumbers: false,
            autowidth: true,
            required: true,
            colModel: [{
                name: 'id',
                label: '从2ID',
                width: 100,
                hidden: true,
                align: 'center'
            }, {
                name: 'detail1Id',
                label: '从1ID',
                width: 100,
                hidden: true,
                align: 'center'
            }, {
                name: 'transportCostId',
                label: 'transportCostId',
                hidden: true,
                width: 100,
                align: 'center'
            }, {
                name: 'transportUnitId',
                label: 'transportUnitId',
                width: 100,
                hidden: true,
                align: 'center'
            }, {
                name: 'billId',
                label: 'billId',
                width: 100,
                hidden: true,
                align: 'center'
            }, {
                name: 'transportCostCode',
                label: '费用编号',
                width: 100,
                align: 'center',
                hidden:true
            },{
                name:'deliveryPlace',
                label:'发货地',
                width:100,
                align:'center'
            },{
                name:'receiptPlace',
                label:'收货地',
                width:100,
                align:'center'
            }, {
                name: 'transportCostName',
                label: '费用名称',
                width: 100,
                align: 'center'
            }, {
                name: 'amount',
                label: '费用金额',
                width: 100,
                align: 'center'
            }, {
                name: 'transportUnitName',
                label: '单位',
                width: 100,
                align: 'center'
            }, {
                name: 'describe',
                label: '备注',
                width: 100,
                align: 'center'
            }],

        });
    }




    if (details != undefined) {
        $('#detailList')[0].addJSONData(details);
        /* for(var i in details){
         key[i] = details[i].executeSign;
         } */
        getTotal();
    }
    function otherCompany(index) {
        _index = index;
        var row = $('#detailList').jqGrid("getRowData", index);
        chooserWindow = $('#other').fool('window', {
            modal: true,
            'title': "选择其他公司运输报价",
            height: 380,
            width: 780,
            href: getRootPath() + '/costAnalysisBill/otherCompany?transportUnitId=' + row.transportUnitId + '&supplierId=' + row.supplierId + "&deliveryPlaceId=" + row.deliveryPlaceId + "&receiptPlaceId=" + row.receiptPlaceId + "&transportTypeId=" + row.transportTypeId + "&shipmentTypeId=" + row.shipmentTypeId + '&okCallBack=myselect&onDblClick=myselect'
        });
    }
    function myselect(data) {
        var index = _index;
        var _d = getDataFirst(data);
        mycancel(index);
        $('#detailList').jqGrid("setRowData", index, _d);
        $('#other').window("close");
        myedit(index);
        mysave(index);
    }

    function getEditor(name, index) {
        return $('#detailList #' + index + "_" + name);
    }
    function getText(name, index) {
        return $('#detailList #' + index + " td[aria-describedby=detailList_" + name + "]");
    }

    function getTotal() {
        /* 	var rows=$("#detailList").jqGrid('getRowData');
         var ftotal=0;
         var etotal=0;
         //var pTotal=0;
         for(var i in rows){
         if(rows[i].action.search(/btn-save/) != -1){
         continue;
         }
         ftotal += parseFloat(rows[i].publishFreightPrice);
         etotal += parseFloat(rows[i].expectedDays);
         }
         $('#publishFreightPrice').val(ftotal);
         $('#expectedDays').val(etotal); */
    }
    function myedit(index) {
        var row = $("#detailList").jqGrid("getRowData", index);
        $("#detailList").jqGrid("editRow", index);
        $("#detailList").jqGrid("setRowData", index, {editing: true, action: null});
        getEditor("executeSign", index).next()[0].comboObj.setComboValue(row.executeSign);
    }
    function mysave(index) {
        var row = $('#detailList').jqGrid("getRowData", index);
        if (!$("#detailList #" + index).form("validate")) {
            return false;
        }
        $("#detailList #" + index + " .btn-save").attr("disabled", "disabled");
        $("#detailList #" + index + " .btn-cancel").attr("disabled", "disabled");
        var remark = getEditor("remark", index).textbox("getValue");
        row.basePrice = getText("basePrice",index).text();
        row.conversionRate = getText("conversionRate",index).text();
        <c:if test="${isCost}">
        //var publishFreightPrice = getEditor("publishFreightPrice", index).numberbox("getValue");
        var publishBasePrice = getEditor("publishBasePrice", index).numberbox("getValue");
        </c:if>
        var expectedDays = getEditor("expectedDays", index).numberbox("getValue");
        var executeSign = getEditor("executeSign", index).next()[0].comboObj.getSelectedValue();
        //var groundCostPrice = getEditor("groundCostPrice",index).numberbox("getValue");
        //var publishFactoryPrice = getEditor("publishFactoryPrice",index).numberbox("getValue");
        <c:choose>
        <c:when test="${isCost}">
        var data = $.extend(row, {
            /* publishFactoryPrice:publishFactoryPrice,groundCostPrice:groundCostPrice, */
            billId: $('#id').val(),
            executeSign: executeSign,
            remark: remark,
            //publishFreightPrice: publishFreightPrice,
            publishBasePrice: publishBasePrice,
            expectedDays: expectedDays
        });
        </c:when>
        <c:otherwise>
        var data = $.extend(row, {
            /* publishFactoryPrice:publishFactoryPrice,groundCostPrice:groundCostPrice, */
            billId: $('#id').val(),
            executeSign: executeSign,
            remark: remark, /* publishBasePrice:publishBasePrice, */
            expectedDays: expectedDays
            
        });
        </c:otherwise>
        </c:choose>
        $.ajax({
            url: getRootPath() + "/costAnalysisBillDetail/save",
            type: "put",
            dataType: 'json',
            data: data,
            success: function (data) {
                if (data.returnCode == 0) {
                    $.fool.alert({
                        msg: "保存成功！", time: 1000, fn: function () {
                            /* $("#detailList #"+index+" .btn-save").removeAttr("disabled");
                             $("#detailList #"+index+" .btn-cancel").removeAttr("disabled");
                             $('#detailList').jqGrid("saveRow",index);
                             $('#detailList').jqGrid("setRowData",index,{
                             editing:false,
                             executeSign:executeSign,
                             action:null
                             });
                             getTotal(); */
                            $("#pop-win").window("refresh");
                        }
                    });
                } else {
                    $.fool.alert({
                        msg: data.message, fn: function () {
                            $("#detailList #" + index + " .btn-save").removeAttr("disabled");
                            $("#detailList #" + index + " .btn-cancel").removeAttr("disabled");
                        }
                    });
                }
            },
            error: function () {
                $.fool.alert({
                    msg: "服务器繁忙，请稍后再试！", fn: function () {
                        $("#detailList #" + index + " .btn-save").removeAttr("disabled");
                        $("#detailList #" + index + " .btn-cancel").removeAttr("disabled");
                    }
                });
            }
        });
    }
    function mycancel(index) {
        $('#detailList').jqGrid("restoreRow", index);
        $('#detailList').jqGrid("setRowData", index, {editing: false, action: null});
    }

    $('#save').click(function () {
        $('#form2').form("enableValidation");
        if (!$('#form2').form("validate")) {
            return false;
        }
        $("#save").attr("disabled", "disabled");
        var data = $('#form2').serialize();
        $.ajax({
            url: getRootPath() + "/costAnalysisBill/save",
            type: "put",
            //dataType:'application/json;charset=utf-8',
            data: data,
            success: function (data) {
                if (data.returnCode == 0) {
                    $.fool.alert({
                        msg: "保存成功！", time: 1000, fn: function () {
                            $("#save").removeAttr("disabled");
                            $('#mylist').trigger("reloadGrid");
                            $('#pop-win').window("close");
                        }
                    });
                } else {
                    $.fool.alert({
                        msg: data.message, fn: function () {
                            $("#save").removeAttr("disabled");
                        }
                    });
                }
            },
            error: function () {
                $.fool.alert({
                    msg: "服务器繁忙，请稍后再试！", fn: function () {
                        $("#save").removeAttr("disabled");
                    }
                });
            }
        });
    });
    $('#myIssue').click(function () {
        var id = $('#id').val();
        $.ajax({
            url: getRootPath() + '/costAnalysisBill/issue?fid=' + id,
            type: "post",
            success: function (data) {
                if (data.returnCode == 0) {
                    $.fool.alert({
                        msg: "发布成功！", time: 1000, fn: function () {
                            $('#mylist').trigger("reloadGrid");
                        }
                    });
                } else {
                    $.fool.alert({msg: data.message});
                }
            },
            error: function () {
                $.fool.alert({msg: "服务器繁忙，请稍后再试！"});
            }
        });
    });
    $('#myMake').click(function () {
        $.fool.confirm({
            title: "提示", msg: "确定生成流程？", fn: function (r) {
                if (r) {
                    makeRow = [];
                    makeRow[0] = $('#form2').serializeJson();
                    var id = "${obj.id}";
                    var a = checkTemp(id);
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
                } else {
                    return false;
                }
            }
        });
    });

    $('#costChart').click(function () {
        var totalPrice = [];
        var date = [];
        var _date = new Date().getTime();
        var today = getDate(_date);
        var start = getDate(_date - 30 * 24 * 3600 * 1000);
        $('#startDay').datebox("setValue", start);
        $('#endDay').datebox("setValue", today);
        var goodsSpecId = $('#goodsSpecId').val();
        var goodsId = $('#goodsId').val();
        var deliveryPlaceId = $('#deliveryPlaceId').val();
        var receiptPlaceId = $('#receiptPlaceId').val();
        var supplierId = $('#supplierId').val();
        var purchase = "${param.type == 1?1:0}"
        var options = {
            startDay: start,
            endDay: today,
            goodsSpecId: goodsSpecId,
            supplierId: supplierId,
            route: myrow.route,
            goodsId: goodsId,
            deliveryPlaceId: deliveryPlaceId,
            receiptPlaceId: receiptPlaceId
        };
        $.ajax({
            url: '${ctx}/costAnalysisBill/chart?purchase=' + purchase,
            type: "get",
            async: false,
            data: options,
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

    //设置路径模板
    function setTemp(fid) {
        $.ajax({
            url: getRootPath() + '/flow/planTemplateRelation/settleRouteTemplate',
            data: {id: fid},
            type: "post",
            success: function (data) {
                if (data.returnCode == 0) {
                    var url = "/flow/planTemplate/manage?openAdder=1&templateType=2&dataId=" + fid + "&tempId=" + data.data.planTemplateId;
                    parent.kk(url, "计划模板");
                } else {
                    if (data.errorCode == "301") {
                        $.fool.alert({
                            msg: "找不到运输模板，请先新增。", fn: function () {
                                var url = "/flow/planTemplate/manage?openAdder=1&templateType=2&dataId=" + fid;
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

    //设置采购模板
    $("#cgTemp").click(function () {
        $.ajax({
            url: getRootPath() + '/flow/planTemplateRelation/settlePurchaseTemplate',
            data: {id: "${obj.id}"},
            type: "post",
            success: function (data) {
                if (data.returnCode == 0) {
                    var url = "/flow/planTemplate/manage?openAdder=1&templateType=1&dataId=${obj.id}&tempId=" + data.data.planTemplateId;
                    parent.kk(url, "计划模板");
                } else {
                    if (data.errorCode == "301") {
                        $.fool.alert({
                            msg: "找不到采购模板，请先新增。", fn: function () {
                                var url = "/flow/planTemplate/manage?openAdder=1&templateType=1&dataId=${obj.id}";
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
    })

    //设置销售模板
    $("#xsTemp").click(function () {
    	xstemp("${obj.id}");
    });
</script>
</body>
</html>