<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <title>数据分析界面</title>
    <style>
        #list {
            text-align: center;
        }

        #save {
            margin-top: 20px
        }

        #paction a {
            margin-right: 5px;
        }
    </style>
</head>

<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>结转损益</h1>
    </div>
</div>
<div id="inputer">
    <form id="form" class="form1">
        <input name="outSubjectId" id="outSubjectId" type="hidden" value="${syIds}"/>
        <input name="inSubjectId" id="inSubjectId" type="hidden" value="${dfInSubject.fid}"/>
        <p><font>转出科目：</font><input name="outSubjectName" subjectType="6" id="outSubjectName"/></p>
        <p><font>转入科目：</font><input name="inSubjectName" subjectType="6" id="inSubjectName"/></p>
        <p id="paction" style="margin-left:55px"><fool:tagOpt optCode="carryAdd"><a href="javascript:;" id="addSub"
                                                                                    class="btn-blue2 btn-s2">增加会计分录</a></fool:tagOpt>
            <fool:tagOpt optCode="carryDel"><a id="deleteSub" class="btn-blue2 btn-s2">删除会计分录</a></fool:tagOpt>
            <fool:tagOpt optCode="carryDelall"><a id="deleteAll" class="btn-blue2 btn-s2">全部删除</a></fool:tagOpt></p>
        <br/>
        <p><font>结转期间：</font><input name="fiscalPeriodId" id="fiscalPeriodId"/></p>
        <p><font>凭证日期：</font><input name="voucherDate" id="voucherDate"/></p>
        <p><font>凭证字：</font><input name="voucherWordId" id="voucherWordId"/></p>
        <p><font>凭证摘要：</font><input name="resume" id="voucherResume"/></p>
        <p style="margin-left:130px"><fool:tagOpt optCode="carrySave"><a href="javascript:;" id="save"
                                                                         class="btn-yellow2 btn-xs">结转</a></fool:tagOpt>
        </p>
    </form>
</div>
<div id="list">
    <table id="subjectList"></table>
</div>
<script type="text/javascript">
    var chooserWindow = "";
    var allSubject = "";
    /*var newData = "";
    var optionCount = "";*/
    $(document).ready(function () {
        /* $("#outSubjectName").fool('subjectCombobox',{
         value:"
        ${syNames}", */
        /* multiple:true, */
        /* width:160,
         height:32,
         novalidate:true,
         required:true,
         missingMessage:"请先选取科目",
         focusShow:true,
         validType:"subject['#outSubjectId']",
         url:"
        ${ctx}/fiscalSubject/getSubject?leafFlag=1",
         onClickIcon:function(){
         $(this).combobox('hidePanel');
         outSubjectChooser();
         $("#outSubjectName").combobox('textbox').blur();
         },
         onSelect:function(record){
         $("#outSubjectId").val($("#outSubjectName").combobox("getValues"));
         $("#outSubjectName").attr("subjectType",record.type);
         }
         }); */

        var outSubjectCombo = $('#outSubjectName').fool('dhxComboGrid', {
            value: "${syNames}",
            text: "${syNames}",
            width: 160,
            height: 32,
            novalidate: true,
            required: true,
            missingMessage: "请先选取科目",
            focusShow: true,
            validType: "subject['#outSubjectId']",
            data: getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
            filterUrl: "${ctx}/fiscalSubject/getSubject?leafFlag=1",
            searchKey: "q",
            setTemplate: {
                input: "#name#",
                columns: [
                    {option: '#code#', header: '编号', width: 100},
                    {option: '#name#', header: '名称', width: 100},
                ]
            },
            toolsBar: {
                name: "科目",
                addUrl: "/fiscalSubject/manage",
                refresh: true
            },
            onSelectionChange: function () {
                $("#outSubjectId").val(outSubjectCombo.getSelectedValue());
                $("#outSubjectName").attr("subjectType", outSubjectCombo.getSelectedText().type);
            }
        });
        $('#outSubjectName').next().find(".dhxcombo_select_button").click(function () {
            outSubjectCombo.closeAll();
            outSubjectChooser();
        });

        var inSubjectCombo = $('#inSubjectName').fool('dhxComboGrid', {
            value: "${dfInSubject.name}",
            text: "${dfInSubject.name}",
            width: 160,
            height: 32,
            novalidate: true,
            required: true,
            missingMessage: "请先选取科目",
            focusShow: true,
            validType: "subject['#inSubjectId']",
            data: getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
            filterUrl: "${ctx}/fiscalSubject/getSubject?leafFlag=1",
            searchKey: "q",
            setTemplate: {
                input: "#name#",
                columns: [
                    {option: '#code#', header: '编号', width: 100},
                    {option: '#name#', header: '名称', width: 100},
                ],
            },
            toolsBar: {
                name: "科目",
                addUrl: "/fiscalSubject/manage",
                refresh: true
            },
            onSelectionChange: function () {
                $("#inSubjectId").val(inSubjectCombo.getSelectedValue());
                $("#inSubjectName").attr("subjectType", inSubjectCombo.getSelectedText().type);
            }
        });
        $('#inSubjectName').next().find(".dhxcombo_select_button").click(function () {
            inSubjectCombo.closeAll();
            inSubjectChooser();
        });
        var voucherWordData = getComboData(getRootPath() + '/basedata/voucherWord', "tree");
        var voucherWordCombo = $("#voucherWordId").fool('dhxCombo', {
            width: 160,
            height: 32,
            novalidate: true,
            required: true,
            missingMessage: "请先选取凭证字",
            focusShow: true,
            editable: false,
            data: voucherWordData,
            setTemplate: {
                input: "#name#",
                option: "#name#"
            },
            toolsBar: {
                name: "凭证字",
                addUrl: "/basedata/listAuxiliaryAttr",
                refresh: true
            },
            onLoadSuccess: function () {
                var childs = voucherWordData;
                for (var i = 0; i < childs.length; i++) {
                    if (childs[i].text.name == "记") {
                        ($("#voucherWordId").next())[0].comboObj.setComboValue(childs[i].value);
                        break;
                    }
                }
            },
        });

        /* $("#fiscalPeriodId").combobox({
         width:160,
         height:32,
         novalidate:true,
         required:true,
         missingMessage:"请选取结算期间",
         valueField:"fid",
         textField:"period",
         url:"
        ${ctx}/fiscalPeriod/getAll",
         mode:"remote",
         editable:false,
         onSelect:function(record){
         var year=record.period.slice(0,4);
         var month=record.period.slice(4,6);
         var day=new Date(year,month,0);
         var lastdate = year + '-' + month + '-' + day.getDate();
         $("#voucherDate").datebox("setValue",lastdate);
         }
         }); */

        var fiscalPeriodData = getComboData("${ctx}/fiscalPeriod/getAll");
        if (fiscalPeriodData[0]) {
            fiscalPeriodData[0].selected = true;
        }
        var fiscalPeriodCombo = $("#fiscalPeriodId").fool('dhxCombo', {
            novalidate: true,
            required: true,
            missingMessage: "请选取结算期间",
            width: 160,
            height: 32,
            focusShow: true,
            editable: false,
            data: fiscalPeriodData,
            setTemplate: {
                input: "#period#",
                option: "#period#"
            },
            onSelectionChange: function () {
                var record = ($("#fiscalPeriodId").next())[0].comboObj.getSelectedText();
                if (record) {
                    var year = record.period.slice(0, 4);
                    var month = record.period.slice(4, 6);
                    var day = new Date(year, month, 0);
                    var lastdate = year + '-' + month + '-' + day.getDate();
                    $("#voucherDate").datebox("setValue", lastdate);
                }
            }
        });

        $("#voucherDate").datebox({
            novalidate: true,
            required: true,
            width: 158,
            height: 30,
            editable: false,
            validType: "voucherDate",
            onSelect: function (date) {
                $("#voucherDate").datebox("enableValidation");
            }
        });
        $.post('${ctx}/voucher/getDefaultMessage', function (data) {
            $('#voucherDate').datebox('setValue', data.voucherDate);
        });
        /* $('#voucherResume').combotree({
         url:'
        ${ctx}/basedata/resume',
         width:158,
         height:30,
         editable:true,
         onBeforeSelect:function(node){
         if(node.text=='摘要'){
         return false;
         }
         },
         onShowPanel:function(){
         $('#voucherResume').combotree('panel').find('.tree').children().children('.tree-node').find('.tree-title').text('请选择');
         }
         }); */

        var voucherResumeValue = "";
        $.ajax({
            url: "${ctx}/basedata/resume?num=" + Math.random(),
            async: false,
            data: {},
            success: function (data) {
                voucherResumeValue = formatTree(data[0].children, "text", "id")
            }
        });
        var voucherResumeCombo = $("#voucherResume").fool('dhxComboGrid', {
            width: 160,
            height: 32,
            focusShow: true,
            editable: true,
            data: voucherResumeValue,
            filterUrl:'${ctx}/basedata/fuzzyFindSubAuxiliaryAttrTree?code=014&num='+Math.random(),
            setTemplate:{
                input:"#name#",
                columns:[
                    {option:'#name#',header:'内容',width:100},
                ],
            },
            toolsBar: {
                name: "摘要",
                addUrl: "/basedata/listAuxiliaryAttr",
                refresh: true
            },
            /*onChange: function () {
                hasAbstract();
                newData = "";
            }*/
        });

        voucherResumeCombo.hdr = null;

        /*function hasAbstract() {
            optionCount = voucherResumeCombo.getOptionsCount();
            if(optionCount <= 2){
                sInput.next().css('display', '');
            }else{
                sInput.next().css('display', 'none');
            }
        }*/

        var refresh = $(".mytoolsBar a.btn-refresh")[0];
        var sInput = $(voucherResumeCombo.getInput());
        //修改input宽度
        sInput.css("width", sInput.width() - 20);
        sInput.after('<a href="javascript:;" id="resumeSave" class="btn-save"></a>');
        sInput.next().css({'float': 'right', 'margin': '5px 23px', 'display': ''});
        $("#resumeSave").click(function () {
            var name = ($('#voucherResume').next())[0].comboObj.getComboText();
            if (name == "") {
                $.fool.alert({msg: '摘要不能为空！', time: 1000});
                return;
            }
            $.ajax({
                url: '${ctx}/basedata/saveFast',
                type: 'POST',
                data: {name: name, categoryCode: '014'},
                success: function (data) {
                    dataDispose(data);
                    if (data) {
                        if (data.returnCode == 0) {
                            $.fool.alert({
                                msg: '摘要保存成功！', time: 1000, fn: function () {
                                    newData = data.data;
                                    voucherResumeValue.push({value: "", text: name});
                                    //重新加载一下Combo
                                    ($('#voucherResume').next())[0].comboObj.load({options: voucherResumeValue});
                                    refresh.click();
                                }
                            });
                            /*sInput.next().css('display', 'none');*/
                        } else if (data.returnCode == 1) {
                            $.fool.alert({msg: data.msg});
                        } else {
                            $.fool.alert({msg: '系统繁忙，请稍后再试'});
                        }
                    }
                }

            })
        });

        $("#subjectList").jqGrid({
            datatype: function (postdata) {
                $.ajax({
                    url: '${ctx}/profitLoss/query',
                    data: postdata,
                    dataType: "json",
                    complete: function (data, stat) {
                        if (stat == "success") {
                            data.responseJSON.totalpages = Math.ceil(data.responseJSON.total / postdata.rows);
                            $("#subjectList")[0].addJSONData(data.responseJSON);
                        }
                    }
                });
            },
            autowidth: true,
            multiselect: true,
            height: $(window).height() - $("#inputer").height() - 120,
            viewrecords: true,
            jsonReader: {
                records: "total",
                total: "totalpages",
            },
            rowNum: 99999,
            colModel: [
                {name: 'fid', label: 'fid', hidden: true},
                {name: 'outSubjectId', label: 'outSubjectId', hidden: true},
                {name: 'inSubjectId', label: 'inSubjectId', hidden: true},
                {name: 'outSubjectCode', label: '编号', align: 'center', width: "100px"},
                {name: 'outSubjectName', label: '名称', align: 'center', width: "100px"},
                {name: 'inSubjectCode', label: '编号', align: 'center', width: "100px"},
                {name: 'inSubjectName', label: '名称', align: 'center', width: "100px"},
            ],
        });
        $("#subjectList").jqGrid('setGroupHeaders', {
            useColSpanStyle: false,
            groupHeaders: [
                {startColumnName: 'outSubjectCode', numberOfColumns: 2, titleText: '转出科目'},
                {startColumnName: 'inSubjectCode', numberOfColumns: 2, titleText: '转入科目'}
            ]
        });
    });

    //弹出框定义
    function outSubjectChooser(target) {
        chooserWindow = $.fool.window({
            'title': "选择转出科目",
            href: '${ctx}/fiscalSubject/window?okCallBack=selectOutSubject&singleSelect=false&onDblClick=selectOutSubjectDBC&onlyLeafCheck=true&subjectType=6',
            onLoad: function () {
                ($('#outSubjectName').next())[0].comboObj.closeAll();
            }
        });//解决IE点击弹窗下拉框不消失问题
    }
    function selectOutSubject(rowData) {
        var fids = "";
        var names = "";
        var type = true;
        for (var i = 0; i < rowData.length; i++) {
            fids += rowData[i].fid + ",";
            names += rowData[i].name + ",";
            if (rowData[i].type != 6) {
                type = false;
            }
        }
        if (!type) {
            $("#outSubjectName").attr("subjectType", 1);
        } else {
            $("#outSubjectName").attr("subjectType", 6);
        }
        ($('#outSubjectName').next())[0].comboObj.setComboText(names);
        $("#outSubjectId").val(fids);
        /* ($('#outSubjectName').next())[0].comboObj.setComboValue(fids); */
        /* $("#outSubjectName").combobox("setValue",fids);
         $("#outSubjectName").combobox("setText",names); */
        chooserWindow.window('close');
    }
    function selectOutSubjectDBC(rowData) {
        /* $("#inSubjectName").combobox("setValue",rowData.fid);
         $("#inSubjectName").combobox("setText",rowData.name); */
        /* ($('#outSubjectName').next())[0].comboObj.setComboValue(rowData.fid); */
        ($('#outSubjectName').next())[0].comboObj.setComboText(rowData.name);
        $("#outSubjectName").attr("subjectType", rowData.type);
        $("#outSubjectId").val(rowData.fid);
        chooserWindow.window('close');
    }
    /* function selectOutSubjectDBC(rowData){
     $("#outSubjectId").val(rowData.fid);
     $("#outSubjectName").combobox("setValue",rowData.fid);
     $("#outSubjectName").combobox("setText",rowData.name);
     $("#outSubjectName").attr("subjectType",rowData.type);
     chooserWindow.window('close');
     } */

    function inSubjectChooser(target) {
        chooserWindow = $.fool.window({
            'title': "选择转入科目",
            href: '${ctx}/fiscalSubject/window?okCallBack=selectInSubject&onDblClick=selectInSubjectDBC&singleSelect=true',
            onLoad: function () {
                ($('#inSubjectName').next())[0].comboObj.closeAll();
            }
        });//解决IE点击弹窗下拉框不消失问题
    }
    function selectInSubject(rowData) {
        $("#inSubjectId").val(rowData[0].fid);
        ($('#inSubjectName').next())[0].comboObj.setComboValue(rowData[0].fid);
        ($('#inSubjectName').next())[0].comboObj.setComboText(rowData[0].name);
        /* $("#inSubjectName").combobox("setValue",rowData[0].fid);
         $("#inSubjectName").combobox("setText",rowData[0].name); */
        $("#inSubjectName").attr("subjectType", rowData[0].type);
        chooserWindow.window('close');
    }
    function selectInSubjectDBC(rowData) {
        $("#inSubjectId").val(rowData.fid);
        ($('#inSubjectName').next())[0].comboObj.setComboValue(rowData.fid);
        ($('#inSubjectName').next())[0].comboObj.setComboText(rowData.name);
        /* $("#inSubjectName").combobox("setValue",rowData.fid);
         $("#inSubjectName").combobox("setText",rowData.name); */
        $("#inSubjectName").attr("subjectType", rowData.type);
        chooserWindow.window('close');
    }

    $("#addSub").click(function () {
        $("#outSubjectName").next().form("enableValidation");
        $("#inSubjectName").next().form("enableValidation");
        if ($("#outSubjectName").next().form("validate") && $("#inSubjectName").next().form("validate")) {
            var outSubjectIds = $("#outSubjectId").val();
            var inSubjectId = $("#inSubjectId").val();
            if ($("#outSubjectName").attr("subjectType") != 6 || $("#inSubjectName").attr("subjectType") != 6) {
                if ($("#outSubjectName").attr("subjectType") != 6) {
                    $.fool.alert({
                        time: 2000, msg: "转出科目包含非损益科目！", fn: function () {
                        }
                    });
                } else {
                    $.fool.alert({
                        time: 2000, msg: "转入科目不是损益科目！", fn: function () {
                        }
                    });
                }
            }
            var obj = {
                outSubjectIds: outSubjectIds,
                inSubjectId: inSubjectId,
            };
            $.ajax({
                type: 'post',
                url: '${ctx}/profitLoss/save',
                data: obj,
                async: false,
                dataType: 'json',
                success: function (data) {
                    if (data.returnCode == '0') {
                        $('#subjectList').trigger("reloadGrid");
                    } else {
                        $.fool.alert({
                            msg: data.message, fn: function () {
                                $('#subjectList').trigger("reloadGrid");
                            }
                        });
                    }
                },
                error: function () {
                    $.fool.alert({time: 1000, msg: "系统繁忙，稍后再试!"});
                }
            });
        }
    });
    function getFid(rowid) {
        return $('#' + rowid + ' [aria-describedby="subjectList_fid"]').text();
    }
    $("#deleteSub").click(function () {
        var ids = $('#subjectList').jqGrid('getGridParam', 'selarrrow');
        if (ids.length < 1) {
            $.fool.alert({msg: "请选择要删除的数据。"});
            return false;
        }
        for (var i = 0; i < ids.length; i++) {
            $.ajax({
                type: 'post',
                url: '${ctx}/profitLoss/delete',
                data: {fid: getFid(ids[i])},
                dataType: 'json',
                async: false,
                success: function (data) {
                    if (data.returnCode != '0') {
                        $.fool.alert({
                            time: 2000, msg: data.message, fn: function () {
                                $('#subjectList').trigger("reloadGrid");
                            }
                        });
                        return false;
                    }
                    $.fool.alert({
                        time: 1000, msg: data.message, fn: function () {
                            $('#subjectList').trigger("reloadGrid");
                        }
                    });
                },
                error: function () {
                    $.fool.alert({time: 1000, msg: "系统繁忙，稍后再试!"});
                    return false;
                }
            });
        }
        $('#subjectList').trigger("reloadGrid");
    });
    $("#deleteAll").click(function () {
        $.fool.confirm({
            title: '确认', msg: '确定要删除所有记录吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/profitLoss/deleteAll',
                        data: {},
                        dataType: 'json',
                        success: function (data) {
                            if (data.returnCode == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '删除成功！', fn: function () {
                                        $('#subjectList').trigger("reloadGrid");
                                    }
                                });
                            } else {
                                $.fool.alert({
                                    msg: data.message, fn: function () {
                                        $('#subjectList').trigger("reloadGrid");
                                    }
                                });
                            }
                        },
                        error: function () {
                            $.fool.alert({time: 1000, msg: "系统繁忙，稍后再试!"});
                        }
                    });
                }
            }
        });
    });
    $("#save").click(function () {
        $("#voucherWordId").next().form("enableValidation");
        $("#fiscalPeriodId").next().form("enableValidation");
        $("#voucherDate").datebox("enableValidation");
        /*var hasValue = ($('#voucherResume').next())[0].comboObj.getSelectedValue();
        if (hasValue == null) {
            hasValue = newData;
        }
        var hasName = ($('#voucherResume').next())[0].comboObj.getComboText();
        if (hasValue == null && hasName != "" && optionCount <= 2|| hasValue == "" && hasName != "" && optionCount <= 2) {
            $.fool.alert({time: 1000, msg: '请先保存凭证摘要！'});
            return;
        }else if(hasValue == null && hasName != "" && optionCount > 2|| hasValue == "" && hasName != "" && optionCount > 2){
            $.fool.alert({time: 1000, msg: '您没有凭证摘要！'});
            return;
        };*/
        if ($("#voucherWordId").next().form("validate") && $("#fiscalPeriodId").next().form("validate")) {
            var voucherWordId = ($("#voucherWordId").next())[0].comboObj.getSelectedValue();
            var fiscalPeriodId = ($("#fiscalPeriodId").next())[0].comboObj.getSelectedValue();
            var voucherDate = $("#voucherDate").datebox("getValue");
            var voucherResume = ($("#voucherResume").next())[0].comboObj.getComboText();
            $.ajax({
                type: 'post',
                url: '${ctx}/profitLoss/makeVoucher',
                data: {
                    voucherWordId: voucherWordId,
                    fiscalPeriodId: fiscalPeriodId,
                    voucherDate: voucherDate,
                    resume: voucherResume
                },
                dataType: 'json',
                success: function (data) {
                    if (data.returnCode == '0') {
                        if (data.data == " ") {
                            $.fool.alert({
                                time: 1000, msg: '操作成功！无凭证生成。', fn: function () {
                                    $('#subjectList').trigger("reloadGrid");
                                }
                            });
                        } else {
                            $.fool.alert({
                                msg: '操作成功！凭证字号为：' + data.data, fn: function () {
                                    $('#subjectList').trigger("reloadGrid");
                                }
                            });
                        }
                    } else {
                        if (data.errorCode == "101") {
                            $.fool.confirm({
                                title: '确认', msg: data.message, fn: function (r) {
                                    if (r) {
                                        $.ajax({
                                            type: 'post',
                                            url: '${ctx}/profitLoss/makeVoucher?flag=1',
                                            data: {
                                                voucherWordId: voucherWordId,
                                                fiscalPeriodId: fiscalPeriodId,
                                                voucherDate: voucherDate,
                                                resume: voucherResume
                                            },
                                            dataType: 'json',
                                            success: function (data) {
                                                if (data.returnCode == '0') {
                                                    if (data.data == " ") {
                                                        $.fool.alert({
                                                            time: 1000, msg: '操作成功！无凭证生成。', fn: function () {
                                                                $('#subjectList').trigger("reloadGrid");
                                                            }
                                                        });
                                                    } else {
                                                        $.fool.alert({
                                                            msg: '操作成功！凭证字号为：' + data.data, fn: function () {
                                                                $('#subjectList').trigger("reloadGrid");
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    $.fool.alert({
                                                        msg: data.message, fn: function () {
                                                            $('#subjectList').trigger("reloadGrid");
                                                        }
                                                    });
                                                }
                                            },
                                            error: function () {
                                                $.fool.alert({time: 1000, msg: "系统繁忙，稍后再试!"});
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            $.fool.alert({
                                msg: data.message, fn: function () {
                                    $('#subjectList').trigger("reloadGrid");
                                }
                            });
                        }
                    }
                },
                error: function () {
                    $.fool.alert({time: 1000, msg: "系统繁忙，稍后再试!"});
                }
            });
        }
    });

    $.extend($.fn.validatebox.defaults.rules, {
        voucherDate: {
            validator: function (value, param) {
                var result = "";
                $.ajax({
                    url: "${ctx}/voucher/getDefaultMessage?num=" + Math.random(),
                    async: false,
                    data: {voucherDate: value},
                    success: function (data) {
                        result = data.voucherDate;
                    }
                });
                if (result) {
                    return true;
                } else {
                    return false;
                }
            },
            message: '根据当前凭证日期找不到未结账的会计期间'
        }
        /* subject:{
         validator: function (value, param) {
         var dataName="";
         $.ajax({
         url:"${ctx}/fiscalSubject/getById",
         async:false,
         data:{fid:$(param[0]).val()},
         success:function(data){
         dataName=data.name;
         }
         });
         return (dataName!=""&&dataName==value);
         },
         message:'请先选择科目'
         },	 */
    });
    function getComboData(url, mark) {
        var result = "";
        $.ajax({
            url: url,
            data: {num: Math.random()},
            async: false,
            success: function (data) {
                if (data) {
                    result = data;
                }
            }
        });
        if (mark != "tree") {
            return formatData(result, "fid");
        } else {
            return formatTree(result[0].children, "text", "id");
        }
    }
</script>
</body>
</html>
