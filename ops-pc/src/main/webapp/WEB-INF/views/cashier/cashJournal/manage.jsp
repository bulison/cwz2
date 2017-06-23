<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <title>现金日记账管理</title>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <script type="text/javascript"
            src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript"
            src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
    <style>
        /* html{
            overflow:hidden;
        } */
        .fixed {
            top: 0px;
        }

        #add {
            /* vertical-align: top; */
            margin-top: 5px;
        }

        #search-form {
            display: inline-block;
            width: 92%;
        }

        #search-form .textbox {
            margin: 5px 5px 0px 0px
        }

        #search-form .btn-s {
            vertical-align: middle;
            margin: 5px 5px 0px 0px
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
            -moz-border-radius: 3px; /* Gecko browsers */
            -webkit-border-radius: 3px; /* Webkit browsers */
            border-radius: 3px; /* W3C syntax */
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

        .input_div {
            display: none;
            background: #F5F5F5;
            padding: 10px 0px 5px 0px;
            border: 1px solid #D5DBEA;
            position: absolute;
            right: 23px;
            top: 91px;
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

        .button_clear {
            border-top: 1px solid #D5DBEA;
            margin-top: 10px;
            text-align: right;
            padding: 10px 0px 0px 0px;
        }

        #subject {
            display: inline-block;
        }

        .roed {
            -moz-transform: scaleY(-1);
            -webkit-transform: scaleY(-1);
            -o-transform: scaleY(-1);
            transform: scaleY(-1);
            filter: FlipV();
        }

        .dhxDiv {
            margin: 5px 5px 0 0
        }
    </style>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>现金日记账</h1>
    </div>
</div>
<div style="margin:0px 0px 10px 0px">
    <a href="javascript:;" id="add" class="btn-ora-add" style="margin-right:5px;">新增</a>
    <a href="javascript:;" id="export" class="btn-ora-export" style="margin-right:5px;">导出</a>
    <a href="javascript:;" id="import" class="btn-ora-import" style="margin-right:5px;">导入</a>
    <form action="" id="subject"><input id="search-subjectId" name="search-subjectId" type="hidden"/><input
            id="search-subjectName" class="textBox"/></form>
    <a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
    <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a
            href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
    <div class="input_div">
        <form id="search-form">
            <!-- <p>科目：<input id="search-subjectId" type="hidden"/><input id="search-subjectName" class="textBox"/></p> -->
            <p>结算号：<input id="search-settlementNo" class="textBox"/></p>
            <p>开始日期：<input id="search-startDay"/></p>
            <p>结束日期：<input id="search-endDay"/></p>
            <p>结算方式：<input id="search-settlementTypeId" type="hidden"/><input id="search-settlementTypeName"/></p>
            <p>余额方向：<input id="search-direction"/></p><!-- <input id="search-checked" /> -->
            <div class="button_clear">
                <a id="search-btn" class="btn-blue btn-s">查询</a>
                <a href="javascript:;" id="clear-btn" class="btn-blue btn-s">清空</a>
                <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
            </div>
        </form>
    </div>
</div>
<table id="billList"></table>
<div id="pager"></div>
<div id="addBox"></div>
<div id="pop-win"></div>

<script type="text/javascript">
    /*var myIndex = "";*/
    //点击下拉按钮
    $('.button_a').click(function () {
        $('.input_div').slideToggle(200);
        $(this).addClass('roed');
    });

    //点击关闭隐藏
    $('#clear-btndiv').click(function () {
        $('.input_div').hide(2);
        $('.button_a').removeClass('roed');
    });
    //全局查询
    $('.Inquiry').click(function () {
//         var subjectId = $("#search-subjectId").val();
//         var options = {"subjectIds": subjectId}
//         $("#billList").jqGrid('setGridParam', {postData: options}).trigger("reloadGrid");
		$('#clear-btndiv').click();
		$("#search-btn").click();
    });

    //鼠标获取焦点
    $('#bolting').focus(function () {
        $('.input_div').show(2);
        $('.button_a').addClass('roed');
    });

    var chooserWindow_search = "";
    var checkedData = [{value: "0", text: '未勾对'}, {value: "1", text: '已勾对'}, {value: null, text: '全部'}];
    var startDate = "";
    var endDate = "";
    $.ajax({
    	url:"${ctx}/fiscalPeriod/getFristUnCheckPeriod",
    	type:"post",
    	async:false,
    	data:{},
    	success:function(data){
    		if(data.startDate){
	    		startDate = data.startDate;
	    		endDate = data.endDate;
    		}
    	}
    });
    //控件初始化
    $("#search-startDay").datebox({
        editable: false,
        width: 160,
        height: 32,
        prompt: "开始日期",
        value:startDate
    });
    $("#search-endDay").datebox({
        editable: false,
        width: 160,
        height: 32,
        prompt: "结束日期",
        value:endDate
    });
    /* $("#search-subjectName").fool("subjectCombobox",{
     prompt:"科目",
     width:160,
     height:32,
     url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&cashSubject=1",
     focusShow:true,
     onClickIcon:function(){
     $(this).combobox('hidePanel');
     $(this).combobox('textbox').blur();
     searchSubjectName();
     },
     onSelect:function(record){
     $('#search-subjectId').val(record.id);
     }
     }); */
    var subjectCombo = $('#search-subjectName').fool('dhxComboGrid', {
        prompt: "科目",
        width: 160,
        height: 32,
        focusShow: true,
        required: true,
        novalidate: true,
        data: getComboData(getRootPath() + "/fiscalSubject/getSubject?leafFlag=1&cashSubject=1&q="),
        filterUrl: getRootPath() + "/fiscalSubject/getSubject?leafFlag=1&cashSubject=1",
        searchKey: "q",
        setTemplate: {
            input: "#name#",
            columns: [
                {option: '#code#', header: '编号', width: 100},
                {option: '#name#', header: '名称', width: 100},
            ],
        },
        toolsBar: {
            refresh: true
        },
        onSelectionChange: function (value, text) {
            $('#search-subjectId').val(($('#search-subjectName').next())[0].comboObj.getSelectedValue());
            $('.Inquiry').click();
        },
    });
    $('#search-subjectName').next().find(".dhxcombo_select_button").click(function () {
        subjectCombo.closeAll();
        searchSubjectName();
    });
    /* $("#search-settlementTypeName").combotree({
     url:"${ctx}/basedata/settlementType",
     editable:false,
     width:160,
     height:32,
     prompt:"结算方式"
     }); */
    $("#search-settlementTypeName").fool('dhxCombo', {
        width: 162,
        height: 32,
        clearOpt: false,
        prompt: "结算方式",
        focusShow: true,
        editable: false,
        data: getComboData("${ctx}/basedata/settlementType", "tree"),
        setTemplate: {
            input: "#name#",
            option: "#name#"
        },
        toolsBar: {
            name: "结算方式",
            refresh: true
        }
    });
    $("#search-settlementNo").textbox({
        width: 160,
        height: 32,
        prompt: "结算号"
    });
    /* $("#search-direction").combobox({
     editable:false,
     width:160,
     height:32,
     data:[{value:"1",text:'借方'},{value:"-1",text:'贷方'},{value:"",text:'全部'}],
     prompt:"金额方向"
     }); */
    $("#search-direction").fool('dhxCombo', {
        width: 162,
        height: 32,
        clearOpt: false,
        prompt: "余额方向",
        focusShow: true,
        editable: false,
        data: [{value: "1", text: '借方'}, {value: "-1", text: '贷方'}, {value: "", text: '全部'}],
        setTemplate: {
            input: "#name#",
            option: "#name#"
        }
    });
    //新增、修改、详情窗口初始化
    $('#addBox').window({
        top: 150,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        resizable: false,
        closed: true,
        modal: true,
        width: $(window).width() - 200,
    });
    //导入
    $('#import').click(function () {
        var s = {
            target: $('#pop-win'),
            boxTitle: "导入现金日记账单",
            downHref: getRootPath() + "/ExcelMapController/downTemplate?downFile=现金日记账.xls",
            action: getRootPath() + "/cashierBankBillController/import?type=5",
            fn: 'okCallback()',
        };
        importBox(s);
    });
    $('#export').click(function () {
        var startDay = $("#search-startDay").datebox('getValue');
        var endDay = $("#search-endDay").datebox('getValue');
        var subjectId = $("#search-subjectId").val();
        var settlementTypeId = ($("#search-settlementTypeName").next())[0].comboObj.getSelectedValue();
        var settlementNo = $("#search-settlementNo").textbox('getText');
        var direction = ($("#search-direction").next())[0].comboObj.getSelectedValue();
        if(settlementTypeId==null)
            settlementTypeId="";
        if(direction==null)
            direction="";
        /* var fchecked=$("#search-checked").combobox('getValue'); */
        var options = {
            subjectIds: subjectId,
            startDate: startDay,
            endDate: endDay,
            settlementTypeId: settlementTypeId,
            settlementNo: settlementNo,
            direction: direction
        };
        exportFrom('${ctx}/cashierBankBillController/export?type=5',options)
    });

    function okCallback() {
        $('#pop-win').window("close");
        $("#billList").trigger("reloadGrid");
    }
    //科目初始数据列表初始化
    $("#billList").jqGrid({
        datatype: function (postdata) {
        	if(!postdata.subjectIds){
    			return false;
    		}
            $.ajax({
                url: '${ctx}/cashierBankBillController/listBankBill?type=5',
                data: postdata,
                dataType: "json",
                complete: function (data, stat) {
                    if (stat == "success") {
                        data.responseJSON.totalpages = Math.ceil(data.responseJSON.total / postdata.rows);
                        $("#billList")[0].addJSONData(data.responseJSON);
                        getTotal();
                    }
                }
            });
        },
        footerrow:true,
        autowidth: true,
        height: 500,
        pager: "#pager",
        rowNum: 10,
        rowList: [10, 20, 30],
        viewrecords: true,
        jsonReader: {
            records: "total",
            total: "totalpages",
        },
        colModel: [
            {name: 'fid', label: 'fid', hidden: true},
            {name: 'newRow', label: '新行标识', align: 'center', hidden: true, width: 100},
            {
                name: 'settlementTypeId',
                label: '结算方式ID',
                align: 'center',
                hidden: true,
                width: 100,
                editable: true,
                edittype: "text"
            },
            {
                name: 'subjectId',
                label: '科目ID',
                align: 'center',
                hidden: true,
                width: 100,
                editable: true,
                edittype: "text"
            },
            {
                name: 'subjectName',
                label: '科目',
                align: 'center',
                width: "100px"/* ,formatter:function(cellvalue, options, rowObject){
             return '<a title="编辑" href="javascript:;" onclick="editById(\''+rowObject.fid+'\')">'+cellvalue+'</a> ';
             } */
            },
            {
                name: 'voucherDate',
                label: '日期',
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).datebox({
                            width: "100%",
                            height: "80%",
                            editable: false,
                            required: true,
                            novalidate: true,
                        });
                        $(cl).datebox('textbox').focus(function () {
                            $(cl).datebox('showPanel');
                        });
                    }
                },
                formatter: function (cellvalue, options, rowObject) {
                    return cellvalue ? cellvalue.substring(0, 10) : "";
                }
            },
            {
                name: 'orderno',
                label: '当天顺序号',
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).textbox({
                            width: "100%",
                            height: "80%",
                            validType: 'accessoryNumber'
                        });
                    }
                }
            },
            {
                name: 'settlementTypeName',
                label: '结算方式',
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).fool("dhxCombo", {
                            width: "100%",
                            height: "80%",
                            clearOpt: false,
                            focusShow: true,
                            editable: false,
                            data: getComboData("${ctx}/basedata/settlementType", "tree"),
                            setTemplate: {
                                input: "#name#",
                                option: "#name#"
                            },
                            toolsBar: {
                                name: "结算方式",
                                addUrl: "/basedata/listAuxiliaryAttr",
                                refresh: true
                            }
                        });
                    }
                }
            },
            {
                name: 'settlementNo',
                label: '结算号',
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).textbox({
                            width: "100%",
                            height: "80%",
                            validType: 'accessoryNumber'
                        });
                    }
                }
            },
            {
                name: 'resume', label: '摘要', align: 'center', width: "100px", editable: true, editoptions: {
                dataInit: function (cl) {
                    $(cl).fool("dhxCombo", {
                        width: "100%",
                        height: "80%",
                        focusShow: true,
                        data: getComboData("${ctx}/basedata/resume", "tree"),
                        setTemplate: {
                            input: "#name#",
                            option: "#name#"
                        },
                        toolsBar: {
                            name: "摘要",
                            addUrl: "/basedata/listAuxiliaryAttr",
                            refresh: true
                        }
                    });
                }
            }, edittype: "text"
            },
            {
                name: 'debit',
                label: '借方金额',
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).numberbox({
                            width: "100%",
                            height: "80%",
                            required: true,
                            novalidate: true,
                            validType: ['balanceAmount', 'numMaxLength[10]']
                        });
                        $(cl).bind("change", function (e) {
                            if (!isNaN($(this).val())) {
                                var nv = $(this).val() + "";
                                value = nv != "" ? parseFloat(nv).toFixed(2) + '' : "";
                                $(this).val(value);
                            }
                        });
                    }
                }
            },
            {
                name: 'credit',
                label: '贷方金额',
                align: 'center',
                width: "100px",
                editable: true,
                edittype: "text",
                editoptions: {
                    dataInit: function (cl) {
                        $(cl).numberbox({
                            width: "100%",
                            height: "80%",
                            required: true,
                            novalidate: true,
                            validType: ['balanceAmount', 'numMaxLength[10]']
                        });
                        $(cl).bind("change", function (e) {
                            if (!isNaN($(this).val())) {
                                var nv = $(this).val() + "";
                                value = nv != "" ? parseFloat(nv).toFixed(2) + '' : "";
                                $(this).val(value);
                            }
                        });
                    }
                }
            },
            {name:'balance',label:'余额',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
            	if (cellvalue) {
            		return parseFloat(cellvalue).toFixed(2);
            	} else {
            		return "";
            	}
            }},
            {
                name: 'action',
                label: '操作',
                align: 'center',
                width: "100px",
                formatter: function (cellvalue, options, rowObject) {
                    if (!rowObject.editing) {
                        var d = '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\'' + options.rowId + '\')"></a> ';
// 	    					if(rowObject.fchecked!=1){
                        return d;
// 	    					}else{
// 	    						return "";
// 	    					}
                    } else {
                        var c = '<input type="button" style="border:0px;" class="btn-cancel" title="撤销" onclick="mycancel(\'' + options.rowId + '\')" /> ';
                        var s = '<input type="button" style="border:0px;" class="btn-save" title="保存" onclick="mysave(\'' + options.rowId + '\')" /> ';
                        return s + c;
                    }
                }
            }
        ],
        loadComplete: function () {//列表权限控制
            warehouseAll();
        },
        onCellSelect: function (rowid, iCol, cellcontent, e) {
            var row = $("#billList").jqGrid("getRowData", rowid);
            if (iCol != 0 && $("#billList #" + rowid).find(".btn-save").length <= 0) {
                editById(rowid);
            }
        },
    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false});

    //清空按钮
    $("#clear-btn").click(function () {
        $("#search-form").form('clear');
        var inputs = $("#search-form").find(".dhxDiv");
        for (var i = 0; i < inputs.length; i++) {
            inputs[i].comboObj.setComboText(null);
        }
    });
    //筛选按钮
    $("#search-btn").click(function () {
        var startDay = $("#search-startDay").datebox('getValue');
        var endDay = $("#search-endDay").datebox('getValue');
        var subjectId = $("#search-subjectId").val();
        var settlementTypeId = ($("#search-settlementTypeName").next())[0].comboObj.getSelectedValue();
        var settlementNo = $("#search-settlementNo").textbox('getText');
        var direction = ($("#search-direction").next())[0].comboObj.getSelectedValue();
        /* var fchecked=$("#search-checked").combobox('getValue'); */
        var options = {
            subjectIds: subjectId,
            startDate: startDay,
            endDate: endDay,
            settlementTypeId: settlementTypeId,
            settlementNo: settlementNo,
            direction: direction
        };
        $("#billList").jqGrid('setGridParam', {postData: options}).trigger("reloadGrid");
    });

    //新增按钮点击事件
    $('#add').click(function () {
// 	  $('#addBox').window({
// 		    top:150+$(window).scrollTop(),  
// 		    left:100,
// 			onClose:function(){
// 				$("html").css("overflow","");
// 				if(typeof(cashkd) != "undefined"){
// 					$(document).unbind('keydown',cashkd);
// 				}
// 			},
// 			onOpen:function(){
// 				$("html").css("overflow","hidden");
// 			}
// 		});
//   	$('#addBox').window('setTitle',"新增现金日记账单");
//   	$('#addBox').window('open');
//   	if($("#search-subjectId").val()!='' && $("#search-subjectId").val().search(/\,/)==-1){
// 	  	$('#addBox').window('refresh',"${ctx}/cashierBankBillController/add?type=6&subjectId="+$("#search-subjectId").val());
// 	}else{
// 		$('#addBox').window('refresh',"${ctx}/cashierBankBillController/add?type=6");
// 	}
        if ($('#search-subjectId').val().match(",")) {
            subjectCombo.setComboText("");
            subjectCombo.setComboValue("");
        }
        $(subjectCombo.getInput()).validatebox("enableValidation");
        if (!$(subjectCombo.getInput()).validatebox("isValid")) {
            return false;
        }
        var ids = $("#billList").jqGrid('getDataIDs');
        var index = Math.max.apply(null,ids) + 1;
        if (index == "-Infinity"){
            index = "1";
        }
        $("#billList").jqGrid("addRowData", index, {newRow: 1}, "first");
        editById(index);
        getEditor("subjectId", index).val($('#search-subjectId').val());
        getText("subjectName", index).text(subjectCombo.getComboText());
    });

    function getEditor(name, index) {
        return $('#billList #' + index + "_" + name);
    }
    function getText(name, index) {
        return $('#billList #' + index + " td[aria-describedby=billList_" + name + "]");
    }
    //编辑按钮点击事件
    function editById(index) {
// 	  $('#addBox').window({
// 		  	top:150+$(window).scrollTop(),  
// 		    left:100,
// 			onClose:function(){
// 				$("html").css("overflow","");
// 				if(typeof(cashkd) != "undefined"){
// 					$(document).unbind('keydown',cashkd);
// 				}
// 			},
// 			onOpen:function(){
// 				$("html").css("overflow","hidden");
// 			}
// 		});
//   	  $('#addBox').window('open');
//   	  $('#addBox').window('refresh',"${ctx}/cashierBankBillController/editBankBill?type=6&fid="+fid);
        var row = $("#billList").jqGrid("getRowData", index);
        $("#billList").jqGrid("editRow", index);
        $("#billList").jqGrid("setRowData", index, {editing: true, action: null});
        //借贷方金额操作
        var $debit = getEditor("debit", index);
        var $credit = getEditor("credit", index);
        $debit.numberbox("textbox").keyup(function () {
            if ($(this).val() != 0) {
                $credit.numberbox("textbox").val(0);
                $credit.numberbox("textbox").attr("disabled", true);
                $credit.numberbox("validate");
            } else {
                $credit.numberbox("textbox").val("");
                $credit.numberbox("textbox").removeAttr("disabled");
                $credit.numberbox("validate");
            }
        });
        $credit.numberbox("textbox").keyup(function () {
            if ($(this).val() != 0) {
                $debit.numberbox("textbox").val(0);
                $debit.numberbox("textbox").attr("disabled", true);
                $debit.numberbox("validate");
            } else {
                $debit.numberbox("textbox").val("");
                $debit.numberbox("textbox").removeAttr("disabled");
                $debit.numberbox("validate");
            }
        });
        if (row.newRow != 1) {
            getEditor("voucherDate", index).datebox("setValue", row.voucherDate);
            if (row.settlementTypeId) {
                getEditor("settlementTypeName", index).next()[0].comboObj.setComboValue(row.settlementTypeId);
            }
            ;
            getEditor("resume", index).next()[0].comboObj.setComboText(row.resume);
            if (row.debit == 0 || row.debit == "") {
                $debit.numberbox("textbox").val(0);
                $debit.numberbox("textbox").attr("disabled", true);
            }
            if (row.credit == 0 || row.credit == "") {
                $credit.numberbox("textbox").val(0);
                $credit.numberbox("textbox").attr("disabled", true);
            }
        }
    }
    function mycancel(index) {
        var row = $("#billList").jqGrid("getRowData", index);
        $("#billList").jqGrid("restoreRow", index);
        if (row.newRow == 1) {
            $("#billList").jqGrid("delRowData", index);
        } else {
            $("#billList").jqGrid("setRowData", index, {fid:row.fid,editing: false, action: null});
        }
    }
    function mysave(index) {
        $("#billList #" + index).form("enableValidation");
        if (!$("#billList #" + index).form("validate")) {
            return false;
        }
        var row = $("#billList").jqGrid("getRowData", index);
        var resume = getEditor("resume", index).next()[0].comboObj.getComboText();
        var subjectName = getText("subjectName", index).text();
        var settlementTypeName = getEditor("settlementTypeName", index).next()[0].comboObj.getComboText();
        var voucherDate = getEditor("voucherDate", index).datebox("getValue");
        getEditor("settlementTypeId", index).val(getEditor("settlementTypeName", index).next()[0].comboObj.getSelectedValue());
        var subjectId = getEditor("subjectId", index).val();
        var settlementTypeId = getEditor("settlementTypeId", index).val();
        var fid = row.fid ? row.fid : "";
        var orderno = getEditor("orderno", index).textbox("getValue");
        var settlementNo = getEditor("settlementNo", index).textbox("getValue");
        var debit = $.trim(getEditor("debit", index).val());
        var credit = $.trim(getEditor("credit", index).val());
        var updateTime = getText("updateTime", index).text();
        var type = 5;
        $('#save').attr("disabled", "disabled");
        var mydata = {
            type: type,
            updateTime: updateTime,
            fid: fid,
            subjectId: subjectId,
            settlementTypeId: settlementTypeId,
            resume: resume,
            voucherDate: voucherDate,
            orderno: orderno,
            settlementNo: settlementNo,
            debit: debit,
            credit: credit
        };
        $.post('${ctx}/cashierBankBillController/save', mydata, function (data) {
            dataDispose(data);
            if (data.returnCode == '0') {
                $.fool.alert({
                    time: 1000, msg: "保存成功！", fn: function () {
                        //$(document).unbind('keydown',cashkd);
                        var obj = data.data[0];
                        $('#save').removeAttr("disabled");
                        $("#billList").jqGrid("saveRow", index);
                        $("#billList").jqGrid("setRowData", index, {
                            newRow:0,
                            fid: obj.fid,
                            voucherDate: obj.voucherDate,
                            settlementTypeName: obj.settlementTypeName,
                            resume: obj.resume,
                            credit: obj.credit,
                            debit:obj.debit,
                            editing: false,
                            action: null
                        })
                    }
                });
            } else if (data.returnCode == 1) {
                $.fool.alert({
                    msg: data.message, fn: function () {
                        /* $(document).unbind('keydown',cashkd);
                         $(document).bind('keydown',cashkd); */
                        $('#save').removeAttr("disabled");
                    }
                });
            } else {
                $.fool.alert({
                    msg: "系统正忙，请稍后再试。", fn: function () {
                        /* $(document).unbind('keydown',cashkd);
                         $(document).bind('keydown',cashkd); */
                        $('#save').removeAttr("disabled");
                    }
                });
            }
        });
    }
    //删除按钮点击事件
    function removeById(rowid) {
        var rowData = $("#billList").jqGrid("getRowData",rowid);
        $.fool.confirm({
            title: '确认', msg: '确定要删除此数据吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/cashierBankBillController/delete',
                        data: {fid: rowData.fid},
                        dataType: 'json',
                        success: function (data) {
                            dataDispose(data);
                            if (data.returnCode == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '删除成功！', fn: function () {
                                        $("#billList").jqGrid("delRowData",rowid);
                                        var ids = $("#billList").jqGrid("getDataIDs").length;
                                        if(ids == 0){
                                            $("#billList").trigger("reloadGrid");
                                        }
                                    }
                                });
                            } else {
                                $.fool.alert({
                                    time: 1000, msg: data.message, fn: function () {
                                        $("#billList").trigger("reloadGrid");
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
    }

    //科目弹出框
    function searchSubjectName() {
        chooserWindow_search = $.fool.window({
            width: 780,
            height: 480,
            'title': "选择科目",
            href: '${ctx}/fiscalSubject/window?okCallBack=selectSubjectSearch&onDblClick=selectSubjectDBCSearch&cashSubject=1&flag=1',
            onLoad: function () {
                subjectCombo.closeAll();
            }
        });//解决IE点击弹窗下拉框不消失问题
    }
    function selectSubjectSearch(rowData) {
        if (rowData[0].flag != 1) {
            $.fool.alert({time: 1000, msg: "请选择子节点"});
            return false;
        }
        var fids = "";
        var names = "";
        if (rowData.length > 1) {
            for (var i = 0; i < rowData.length; i++) {
                fids += rowData[i].fid + ",";
                names += rowData[i].name + ",";
            }
        } else {
            fids = rowData[0].fid;
            names = rowData[0].name;
        }
        subjectCombo.setComboText(names);
        subjectCombo.setComboValue(fids);
        $("#search-subjectId").val(fids);
        chooserWindow_search.window('close');
    }
    function selectSubjectDBCSearch(rowData) {
        if (rowData.flag != 1) {
            $.fool.alert({time: 1000, msg: "请选择子节点"});
            return false;
        }
        subjectCombo.setComboText(rowData.names);
        subjectCombo.setComboValue(rowData.fid);
        $("#search-subjectId").val(rowData.fid);
        chooserWindow_search.window('close');
    }
    function getComboData(url, mark) {
        var result = "";
        $.ajax({
            url: url,
            async: false,
            data: {num: Math.random()},
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
    
    function getTotal(){
  	  var rows=$('#billList').jqGrid('getRowData');
  	  var debit=0;
  	  var credit=0;
  	  var balance=0;
  	  for(var i=0;i<rows.length;i++){
  		  if(rows[i].action.search(/editing-on/) != -1){
  			  continue;
  		  }
  		  if("undefined" != typeof rows[i].debit&&rows[i].debit!=""){
  			  debit += parseFloat(rows[i].debit);
  		  }
  		  if("undefined" != typeof rows[i].credit&&rows[i].credit!=""){
  			  credit += parseFloat(rows[i].credit);
  		  }
  		  if("undefined" != typeof rows[i].balance&&rows[i].balance!=""){
  			  balance += parseFloat(rows[i].balance);
  		  }
  	  }
  	  $('#billList').footerData("set",{fchecked:"合计",debit:debit.toFixed(2),credit:credit.toFixed(2),balance:balance.toFixed(2)},false);
    }
</script>
</body>
</html>
