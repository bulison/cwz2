<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <title>费用单信息管理</title>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css"/>
    <link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css"/>
    <script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>

    <style>
        .open {
            background: url(${ctx}/resources/images/open.png) no-repeat;
            padding-left: 0px;
            width: 40px;
            background-position: 0px -1px;
        }

        #goodsChooser {
            display: none;
            text-align: center;
        }

        #btnBox {
            text-align: center;
        }

        .form p.hideOut, .form1 p.hideOut {
            display: none;
        }

        #userChooser, #customerChooser, #goodsChooser, #goodsSpecChooser {
            display: none;

        }

        #userSearcher, #cusSearcher, #goodsSearcher, #goodsSpecSearcher {
            text-align: left;
            margin: 10px
        }

        #add {
            vertical-align: top;
            margin-top: 5px;
        }

        #search-form {
            display: inline-block;
            width: 98%;
        }

        #search-form .textbox {
            margin: 5px 5px 0px 0px;
        }

        #search-form .dhxDiv {
            margin: 5px 5px 0px 0px;
        }

        #search-form .btn-s {
            vertical-align: middle;
            margin: 5px 5px 0px 0px
        }

        .dd {
            margin-left: 7px;
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
            text-align: right;
            margin-top: 10px;
        }

        #search-form #clear-btndiv {
            margin-left: -5px;
        }

        .roed {
            -moz-transform: scaleY(-1);
            -webkit-transform: scaleY(-1);
            -o-transform: scaleY(-1);
            transform: scaleY(-1);
            filter: FlipV();
        }

        #bill p.hideOut {
            display: none;
        }
    </style>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>费用单</h1>
    </div>
</div>
<div id="addBox"></div>
<div style="margin:5px 0px 10px 0px">
    <fool:tagOpt optCode="fydAdd"><a href="javascript:;" id="add" class="btn-ora-add"
                                     style="margin-right:5px;">新增</a></fool:tagOpt>
    <input id="search-code"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
    <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a
            href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
    <div class="input_div">
        <form id="search-form">
            <p>开始日期：<input id="search-startDay" name="startDay"/></p>
            <p>结束日期：<input id="search-endDay" name="endDay"/></p>
            <p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
                <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2'
                                                                             name="recordStatus">已作废</p>
            <p>核对：<input id="search-checkName"/><input id="search-checkId" name="checkerId" type="hidden"/></p>
            <p>收支单位：<input id="search-csvName"/><input id="search-csvId" name="csvId" type="hidden"/></p>
            <p>经手人：<input id="search-member"/><input id="search-memberId" type="hidden" name="memberId"/></p>
            <p>金额：<input id="search-amount" class="easyui-numberbox" name="freeAmount"
                         data-options="width:161,height:32,precision:2"/></p>
            <div class="button_clear">
                <fool:tagOpt optCode="fydSearch"><a href="javascript:;" id="search-btn"
                                                    class="btn-blue btn-s">查询</a></fool:tagOpt>
                <a id="clear-btn" class="btn-blue btn-s">清空</a>
                <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
            </div>
        </form>
    </div>
</div>
<table id="billList"></table>
<div id="pager"></div>
<script type="text/javascript">
    var chechbox = '';
    var decide = true;
    var memberData = "";
    var csvData = "";
    $.ajax({
        url: "${ctx}/basedata/query?num=" + Math.random(),
        async: false,
        data: {param: "Member"},
        success: function (data) {
            memberData = formatData(data.Member, "fid");
        }
    });
    $.ajax({
        url: getRootPath() + '/supplier/list?showDisable=0&num=' + Math.random(),
        async: false,
        data: {},
        success: function (data) {
            csvData = formatData(data.rows, "fid");
        }
    });
    $(":checkbox").click(function () {//点击复选框并且只让一个选中
        $(this).attr("checked", "checked").siblings().removeAttr("checked");
        chechbox = $(this).val();
        /*  if(decide==true){
         chechbox=$(this).val();
         decide=false;
         }else{
         chechbox='';
         decide=true;
         }	 */
    });

    //点击下拉按钮
    $('.button_a').click(function () {
        $('.input_div').toggle(2);
        var s = $('.button_a').attr('class');
        if (s == "button_a roed") {
            $('.button_a').removeClass('roed');
        } else {
            $(this).addClass('roed');
        }
    });

    //点击关闭隐藏
    $('#clear-btndiv').click(function () {
        $('.input_div').hide(2);
        $('.button_a').removeClass('roed');
    });
    //全局查询
    $('.Inquiry').click(function () {
        var searchKey = $("#search-code").textbox('getValue');
        var options = {"searchKey": searchKey}
        $('#billList').jqGrid('setGridParam', {postData: options}).trigger("reloadGrid");
    });

    //鼠标获取焦点
    $('#bolting').focus(function () {
        $('.input_div').show(2);
        $('.button_a').addClass('roed');
    });

    $('#add').click(function () {
        /* $('#addBox').window({
         title:'新增费用单',
         top:10,
         modal:true,
         collapsible:false,
         minimizable:false,
         maximizable:false,
         resizable:false,
         href:'
        ${ctx}/costBill/add',
         width:$(window).width()-10,
         height:$(window).height()-60,
         onClose:function(){
         if($("#checkBox").html()){
         $("#checkBox").window("destroy");
         }
         },
         onOpen:function(){
         $(this).parent().prev().css("display","none");
         }
         }); */
        var url = '${ctx}/costBill/add';
        warehouseWin("新增费用单", url);
        /* window.location.href="
        ${ctx}/costBill/add"; */
    });

    $('#refresh').click(function () {
        $('#billList').trigger("reloadGrid");
    });

    $("#search-code").textbox({
        prompt: '单号或原始单号',
        width: 160,
        height: 32,
        /* inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
         keyup:function(e){
         if(e.keyCode==13){
         var searchKey=$("#search-code").textbox('getValue');
         var options={"searchKey":searchKey}
         $('#billList').datagrid('load',options);
         }
         }
         }) */
    });

    /* $("#search-csvName").textbox({
     editable:false,
     prompt:'收支单位',
     width:160,
     height:32
     }); */

    /* $("#search-bankId").textbox({
     prompt:'银行账号',
     width:160,
     height:32
     });
     */
    /* $("#search-member").textbox({
     editable:false,
     prompt:'经手人',
     width:160,
     height:32
     }); */


    $("#search-startDay").fool('datebox', {
        prompt: '开始日期',
        width: 160,
        height: 32,
        inputDate: true,
        editable: true
    });

    $("#search-endDay").fool('datebox', {
        prompt: '结束日期',
        width: 160,
        height: 32,
        inputDate: true,
        editable: true
    });


    var options = [
        {id: '0', name: '未审核'},
        {id: '1', name: '已审核'},
        {id: '2', name: '已作废'}
    ];
    /* $('#billList').datagrid({
     url:"${ctx}/costBill/list",
     idField:'fid',
     pagination:true,
     fitColumns:true,
     singleSelect:true,
     remoteSort:false,
     onLoadSuccess:function(){//列表权限控制
    <fool:tagOpt optCode="fydAction1">//</fool:tagOpt>$('.btn-edit').remove();
    <fool:tagOpt optCode="fydAction2">//</fool:tagOpt>$('.btn-del').remove();
    <fool:tagOpt optCode="fydAction3">//</fool:tagOpt>$('.btn-copy').remove();
    <fool:tagOpt optCode="fydAction4">//</fool:tagOpt>$('.btn-approve').remove();
    <fool:tagOpt optCode="fydAction5">//</fool:tagOpt>$('.btn-cancel').remove();
    <fool:tagOpt optCode="fydAction6">//</fool:tagOpt>$('.btn-detail').remove();
     warehouseAll();
     },
     columns:[[
     {field:'fid',title:'fid',hidden:true,width:100},
     {field:'code',title:'单号',sortable:true,width:100<fool:tagOpt optCode="fydAction1">,formatter:function(value,row,index){
     return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\',\''+row.recordStatus+'\')">'+value+'</a>';
     }</fool:tagOpt>},
     {field:'csvName',title:'收支单位名称',sortable:true,width:100},
     {field:'memberName',title:'经手人',sortable:true,width:100},
     {field:'freeAmount',title:'费用金额',width:100,sortable:true,formatter:function(value){
     if(value=="0E-8"||!value){
     return 0;
     }else{
     return value;
     }
     }},
     {field:'incomeAmount',title:'收入金额',width:70,sortable:true,formatter:function(value){
     if(value=="0E-8"||!value){
     return 0;
     }else{
     return value;
     }
     }},
     {field:'billDate',title:'单据日期',sortable:true,width:50,formatter:function(value){
     return value.substring(0,10);
     }},
     {field:'recordStatus',title:'状态',width:50,sortable:true,formatter:function(value){
     for(var i=0; i<options.length; i++){
     if (options[i].id == value) return options[i].name;
     }
     return value;
     }},
    <fool:tagOpt optCode="fydAction">
     {field:'action',title:'操作',width:100,formatter:function(value,row,index){
     if(row.recordStatus==0){
     var statusStr = '';
     //  statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> ';
     statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
     statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
     statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+row.fid+'\')"></a> ';
     statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
     return statusStr ;
     }else if(row.recordStatus==1){
     var statusStr = '';
     //  statusStr += '<a class="btn-detail" title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> ';
     statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
     statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
     return statusStr ;
     }else if(row.recordStatus==2){
     var statusStr = '';
     //  statusStr += '<a class="btn-detail" title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> ';
     statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
     return statusStr ;
     }
     }}
    </fool:tagOpt>
     ]]
     }); */
    $('#billList').jqGrid({
        datatype: function (postdata) {
            $.ajax({
                url: "${ctx}/costBill/list",
                data: postdata,
                dataType: "json",
                complete: function (data, stat) {
                    if (stat == "success") {
                        data.responseJSON.totalpages = Math.ceil(data.responseJSON.total / postdata.rows);
                        $("#billList")[0].addJSONData(data.responseJSON);
                    }
                }
            });
        },
        forceFit: true,
        pager: '#pager',
        rowList: [10, 20, 30],
        viewrecords: true,
        rowNum: 10,
        jsonReader: {
            records: "total",
            total: "totalpages",
        },
        autowidth: true,//自动填满宽度
        height: $(window).outerHeight() - 200 + "px",
        gridComplete: function () {//列表权限控制
            <fool:tagOpt optCode="fydAction1">//</fool:tagOpt>$('.btn-edit').remove();
            <fool:tagOpt optCode="fydAction2">//</fool:tagOpt>$('.btn-del').remove();
            <fool:tagOpt optCode="fydAction3">//</fool:tagOpt>$('.btn-copy').remove();
            <fool:tagOpt optCode="fydAction4">//</fool:tagOpt>$('.btn-approve').remove();
            <fool:tagOpt optCode="fydAction5">//</fool:tagOpt>$('.btn-cancel').remove();
            <fool:tagOpt optCode="fydAction6">//</fool:tagOpt>$('.btn-detail').remove();
            warehouseAll();
        },
        colModel: [
            {name: 'fid', label: 'fid', align: "center", hidden: true, width: 100},
            {name: 'csvType', label: 'csvType', align: "center", hidden: true, width: 100},
            {
                name: 'code',
                label: '单号',
                align: "center",
                sortable: true,
                width: 100<fool:tagOpt optCode="fydAction1">,
                formatter: function (value, options, row) {
                    return '<a label="查看" href="javascript:;" onclick="editById(\'' + row.fid + '\',\'' + row.recordStatus + '\')">' + value + '</a>';
                }</fool:tagOpt>
            },
            {name: 'csvName', label: '收支单位名称', align: "center", sortable: true, width: 100},
            {name: 'memberName', label: '经手人', align: "center", sortable: true, width: 100},
            {
                name: 'freeAmount',
                label: '费用金额',
                align: "center",
                width: 100,
                sortable: true,
                formatter: function (value) {
                    if (value == "0E-8" || !value) {
                        return 0;
                    } else {
                        return value;
                    }
                }
            },
            {
                name: 'totalUnCheckAmount',
                label: '未对单金额',
                align: "center",
                width: 100,
                sortable: true,
                formatter: function (value) {
                    if (value == "0E-8" || !value) {
                        return 0;
                    } else {
                        return value;
                    }
                }
            },
            {
                name: 'expenseType',
                label: '费用标识',
                align: "center",
                width: 140,
                sortable: true,
                formatter: function (value) {
                    if (value == 0) {
                        return "不处理";
                    } else if (value == 1) {
                        return "增加往来单位应收/应付款项";
                    } else {
                        return "减少往来单位应收/应付款项";
                    }
                }
            },
            /* {name:'incomeAmount',label:'收入金额',width:70,sortable:true,formatter:function(value){
             if(value=="0E-8"||!value){
             return 0;
             }else{
             return value;
             }
             }}, */
            {
                name: 'billDate',
                label: '单据日期',
                align: "center",
                sortable: true,
                width: 50,
                formatter: function (value) {
                    return value.substring(0, 10);
                }
            },
            {
                name: 'recordStatus',
                label: '状态',
                align: "center",
                width: 50,
                sortable: true,
                formatter: function (value) {
                    for (var i = 0; i < options.length; i++) {
                        if (options[i].id == value) return options[i].name;
                    }
                    return value;
                }
            },
            {
                name: 'checked',
                label: '核对',
                align: "center",
                width: 50,
                sortable: true,
                formatter: function (value, options, row) {
                    return value == false?"未核对":"已核对";
                }
            },
            <fool:tagOpt optCode="fydAction">
            {
                name: 'action', label: '操作', width: 100, align: "center", formatter: function (value, options, row) {
                if (row.recordStatus == 0) {
                    var statusStr = '';
                    /*  statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> '; */
                    statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\'' + row.fid + '\')"></a> ';
                    statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\'' + row.fid + '\')" ></a> ';
                    statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\'' + row.fid + '\')"></a> ';
                    statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\'' + row.fid + '\')"></a> ';
                    return statusStr;
                } else if (row.recordStatus == 1) {
                    var statusStr = '';
                    /*  statusStr += '<a class="btn-detail" title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> '; */
                    if (row.checked == false) {
                        statusStr += '<a class="btn-check" title="核对" href="javascript:;" onclick="checkById(\'' + row.fid + '\')"></a>';
                    }
                    statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\'' + row.fid + '\')"></a> ';
                    statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\'' + row.fid + '\')" ></a> ';
                    return statusStr;
                } else if (row.recordStatus == 2) {
                    var statusStr = '';
                    /*  statusStr += '<a class="btn-detail" title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> '; */
                    if (row.checked == false) {
                        statusStr += '<a class="btn-check" title="核对" href="javascript:;" onclick="checkById(\'' + row.fid + '\')"></a>';
                    }
                    statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\'' + row.fid + '\')" ></a> ';
                    return statusStr;
                }
            }
            }
            </fool:tagOpt>
        ]
    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false});
    $("#clear-btn").click(function () {
        scsvCombo.setComboText("");
        smemCombo.setComboText("");
        checkCombo.setComboText("");
        $("#search-form").form("clear");
    });

    $("#search-btn").click(function () {
        search();
    })

    function search() {
        /* 	var searchKey=$("#search-code").textbox('getValue');
         var startDay=$("#search-startDay").datebox('getValue');
         var endDay=$("#search-endDay").datebox('getValue');
         var csvId=$("#search-csvId").val();
         var supplierId=$("#search-supplierId").val();
         var memberId=$("#search-memberId").val();
         var options = {"searchKey":searchKey,"startDay":startDay,"endDay":endDay,"csvId":csvId,"memberId":memberId}; */
        var options = $("#search-form").serializeJson();
        typeof options.recordStatus == "undefined" ? options.recordStatus = "" : null;//由于取消选择状态后，options没有录入参数recordStatus，但jqGrid的GridParam依然存在，所以设置一下
        $('#billList').jqGrid('setGridParam', {postData: options}).trigger("reloadGrid");
    }

    //编辑
    /* function editById(fid,mark){
     var title = "修改费用单";
     if(mark==1){
     title = "查看费用单";
     }
     $('#addBox').window({
     title:title,
     top:10,
     modal:true,
     collapsible:false,
     minimizable:false,
     maximizable:false,
     resizable:false,
     href:"${ctx}/costBill/edit?fid="+fid,
     width:$(window).width()-200,
     //height:$(window).height()-200,
     onClose:function(){
     if($("#checkBox").html()){
     $("#checkBox").window("destroy");
     }
     }
     });
     }  */

    //编辑 、查看
    function editById(fid, recordStatus, fnName,args) {
        var url = "";
        var title = "";
        if (recordStatus == 1 || recordStatus == 2) {//已审核已作废只能查看
            title = "查看费用单";
            url = "${ctx}/costBill/edit?fid=" + fid + "&flag=detail";
        } else if (recordStatus == 0) {//状态为未审核可以修改
            title = "修改费用单";
            url = "${ctx}/costBill/edit?fid=" + fid + "&flag=edit";
        }
        /* $('#addBox').window({
         title:title,
         top:10,
         modal:true,
         collapsible:false,
         minimizable:false,
         maximizable:false,
         resizable:false,
         href:url,
         width:$(window).width()-10,
         height:$(window).height()-60,
         onClose:function(){
         if($("#checkBox").html()){
         $("#checkBox").window("destroy");
         }
         },
         onOpen:function(){
         $(this).parent().prev().css("display","none");
         }
         }); */
         if(fnName){
        	 warehouseWin(title, url,fnName,args);
         }else{
        	 warehouseWin(title, url);
         }
    }

    //复制
    function copyById(fid) {
        var url = "${ctx}/costBill/edit?fid=" + fid + "&mark=1&flag=copy";
        /* $('#addBox').window({
         title:'复制费用单',
         top:10,
         modal:true,
         collapsible:false,
         minimizable:false,
         maximizable:false,
         resizable:false,
         href:"
        ${ctx}/costBill/edit?fid="+fid+"&mark=1&flag=copy",
         width:$(window).width()-10,
         height:$(window).height()-60,
         onClose:function(){
         if($("#checkBox").html()){
         $("#checkBox").window("destroy");
         }
         },
         onOpen:function(){
         $(this).parent().prev().css("display","none");
         }
         }); */
        warehouseWin("复制费用单", url);
        /* window.location.href="
        ${ctx}/costBill/edit?fid="+fid+"&mark=1&flag=copy"; */
    }


    function checkById(fid) {
        $.fool.confirm({
            title: '确认', msg: '确定要核对选中的记录吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/costBill/check',
                        data: {billId: fid},
                        dataType: 'json',
                        success: function (data) {
                            dataDispose(data);
                            if (data.result == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '核对完成！', fn: function () {
                                        $('#billList').trigger('reloadGrid');
                                    }
                                });
                            } else if (data.result == '1') {
                                $.fool.alert({msg: data.msg});
                                $('#save').removeAttr("disabled");
                            } else {
                                $.fool.alert({
                                    time: 1000, msg: "系统繁忙，稍后再试!", fn: function () {
                                        $('#billList').trigger('reloadGrid');
                                    }
                                });
                            }
                        },
                        error: function () {
                            $.fool.alert({time: 1000, msg: data.msg});
                        }
                    });
                }
            }
        });
    }

    //删除
    function removeById(fid) {
        $.fool.confirm({
            title: '确认', msg: '确定要删除选中的记录吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/costBill/delete',
                        data: {fid: fid},
                        dataType: 'json',
                        success: function (data) {
                            dataDispose(data);
                            if (data.result == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '删除成功！', fn: function () {
                                        $('#billList').trigger('reloadGrid');
                                    }
                                });
                            } else if (data.result == '1') {
                                $.fool.alert({msg: data.msg});
                                $('#save').removeAttr("disabled");
                            } else {
                                $.fool.alert({
                                    time: 1000, msg: "系统繁忙，稍后再试!", fn: function () {
                                        $('#billList').trigger('reloadGrid');
                                    }
                                });
                            }
                        },
                        error: function () {
                            $.fool.alert({time: 1000, msg: data.msg});
                        }
                    });
                }
            }
        });
    }

    function verifyById(fid) {
    	if(queryByWarehouse(fid)){
    		var expenseType= $("td[title='"+fid+"']").siblings("[aria-describedby='billList_expenseType']").text();
    		var csvType= $("td[title='"+fid+"']").siblings("[aria-describedby='billList_csvType']").text();
    		var amount= $("td[title='"+fid+"']").siblings("[aria-describedby='billList_freeAmount']").text();
    		if((expenseType=="不处理")||(expenseType=="增加往来单位应收/应付款项"&&csvType==2)||(expenseType=="减少往来单位应收/应付款项"&&csvType==1)){
    			amount=-amount;
    		}
			if(!checkPlanAmount(fid,amount)){
				$.messager.defaults={ok:"编辑",cancel:"不编辑"};
				$.fool.confirm({msg:'资金计划的金额不等于单据金额，是否编辑资金计划？',fn:function(r){
					if(r){
						//todo
						editById(fid,0,zjjh,[fid]);
						/*zjjh(value);*/
					}
				}})
				$.messager.defaults={ok:"确定",cancel:"取消"};
				return false;
			}else{
				auditNew(fid);
			}
		}else{
			$.messager.defaults={ok:"编辑",cancel:"不编辑"};
			$.fool.confirm({msg:'暂无资金计划记录，是否编辑资金计划？',fn:function(r){
				if(r) {
					editById(fid,0,zjjh,[fid]);
					/*zjjh(value);*/
				}else{
					capitalPassAudit(fid);
				}
			}});
			$.messager.defaults={ok:"确定",cancel:"取消"};
		};
    }

    function cancelById(fid) {
        $.fool.confirm({
            title: '确认', msg: '确定要作废此单据吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/costBill/cancel',
                        data: {fid: fid},
                        dataType: 'json',
                        success: function (data) {
                            dataDispose(data);
                            if (data.result == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '已作废！', fn: function () {
                                        $('#billList').trigger('reloadGrid');
                                    }
                                });
                            } else {
                                if (data.errorCode == '103' || data.errorCode == '104' || data.errorCode == '105') {
                                    $.fool.alert({
                                        btnName: '转到会计期间页面', btnAct: 'mybtnAct(this)', msg: data.msg, fn: function () {
                                        }
                                    });
                                } else {
                                    $.fool.alert({
                                        msg: data.msg, fn: function () {
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
            }
        });

    }
    ;

    //新版领料员输入显示
    var boxWidth = 162, boxHeight = 31;//统一设置输入框大小
    var smemCombo = $('#search-member').fool('dhxComboGrid', {
        required: true,
        novalidate: true,
        width: boxWidth,
        height: boxHeight,
        data: memberData,
        focusShow: true,
        onlySelect: true,
        filterUrl: getRootPath() + '/member/list',
        setTemplate: {
            input: "#username#",
            columns: [
                {option: '#userCode#', header: '编号', width: 100},
                {option: '#jobNumber#', header: '工号', width: 100},
                {option: '#username#', header: '名称', width: 100},
                {option: '#phoneOne#', header: '电话', width: 100},
                {option: '#deptName#', header: '部门', width: 100},
                {option: '#position#', header: '职位', width: 100},
            ]
        },
        toolsBar:{
            refresh:true
        },
        onChange: function (value, text) {
            $("#search-memberId").val(value);
        }
    });

    //核对
    var checkCombo = $('#search-checkName').fool('dhxComboGrid', {
        required: true,
        novalidate: true,
        data: [{
            text: '',
            value: '0'
        }, {
            text: '已核对',
            value: '1'
        }, {
            text: '未核对',
            value: '2'
        }],
        width: 162,
        height: 32,
        editable: true,
        edittype: 'select',
        onChange: function (value, text) {
            $("#search-checkId").val(value);
        }
    })

    //收支单位
    var scsvCombo = $('#search-csvName').fool('dhxComboGrid', {
        required: true,
        novalidate: true,
        width: boxWidth,
        height: boxHeight,
        data: csvData,
        focusShow: true,
        onlySelect: true,
        filterUrl: getRootPath() + '/customerSupplier/list?showDisable=0',
        setTemplate: {
            input: "#name#",
            columns: [
                {option: '#code#', header: '编号', width: 100},
                {option: '#name#', header: '名称', width: 100},
            ]
        },
        toolsBar:{
            refresh:true
        },
        onChange: function (value, text) {
            $("#search-csvId").val(value);
        }
    });

    //设置鼠标放进去自动出来下拉列表
    $("#search-form").find("input[_class]").each(function (i, n) {
        ($(this));
    });
    /* if($('#search-member').length > 0)//经手人
     $('#search-member').combogrid('textbox').focus(function(){
     $('#search-member').combogrid('showPanel');
     });
     if($('#search-csvName').length > 0)//收支单位
     $('#search-csvName').combogrid('textbox').focus(function(){
     $('#search-csvName').combogrid('showPanel');
     }); */


    /* var chooser="";
     $("#search-csvName").textbox('textbox').click(function(){
     chooser=$.fool.window({'title':"选择收支单位",width:780,height:480,href:'${ctx}/customerSupplier/window?okCallBack=selectCsvSearch&onDblClick=selectCsvSearchDBC&singleSelect=true'});
     });

     $("#search-member").textbox('textbox').click(function(){
     chooser=$.fool.window({'title':"选择经手人",width:780,height:480,href:'${ctx}/member/window?okCallBack=selectUserSearch&onDblClick=selectUserSearchDBC&singleSelect=true'});
     });


     function selectCsvSearch(rowData){
     $("#search-csvId").val(rowData[0].fid);
     $("#search-csvName").textbox('setValue',rowData[0].name);
     chooser.window('close');
     }
     function selectCsvSearchDBC(rowData){
     $("#search-csvId").val(rowData.fid);
     $("#search-csvName").textbox('setValue',rowData.name);
     chooser.window('close');
     }

     function selectUserSearch(rowData){
     $("#search-memberId").val(rowData[0].fid);
     $("#search-member").textbox('setValue',rowData[0].username);
     chooser.window('close');
     }
     function selectUserSearchDBC(rowData){
     $("#search-memberId").val(rowData.fid);
     $("#search-member").textbox('setValue',rowData.username);
     chooser.window('close');
     }

     $(function(){
     //分页条
     //setPager($('#billList'));
     }); */
    enterSearch("Inquiry");//回车搜索
    if ("${param.id}") {
        editById("${param.id}", "${param.recordStatus}");
    }
    
    function queryByWarehouse(relationId){
    	var result=false;
    	$.ajax({
    		type : 'post',
    		url : getRootPath()+"/capitalPlan/queryByWarehouse",
    		data : {relationId :relationId},
    		dataType : 'json',
    		async:false,
    		success : function(data) {
    			if(data){
    				result=true;
    			}
    		}
    	});
    	return result;
    }

    function checkPlanAmount(relationId,billAmount){
    	var result=false;
    	$.ajax({
    		type : 'post',
    		url : getRootPath()+"/capitalPlan/checkPlanAmount",
    		data : {relationId :relationId,billAmount:billAmount},
    		dataType : 'json',
    		async:false,
    		success : function(returnDate) {
    			if(returnDate.returnCode==0){
    				result=true;
    			}else{
    				result=false;
    			}
    		}
    	});
    	return result;
    }

    function auditNew(fid){
    	$.fool.confirm({
            title: '确认', msg: '确定要审核通过此单据吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/costBill/passAudit',
                        data: {fid: fid},
                        dataType: 'json',
                        success: function (data) {
                            dataDispose(data);
                            if (data.returnCode == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '审核成功！', fn: function () {
                                        $('#billList').trigger('reloadGrid');
                                    }
                                });
                            } else {
                                if (data.errorCode == '103' || data.errorCode == '104' || data.errorCode == '105') {
                                    $.fool.alert({
                                        btnName: '转到会计期间页面',
                                        btnAct: 'mybtnAct(this)',
                                        msg: data.message,
                                        fn: function () {
                                        }
                                    });
                                } else {
                                    $.fool.alert({
                                        msg: data.message, fn: function () {
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
            }
        });
    }
    
    function capitalPassAudit(value){
    	$.ajax({
    		type : 'post',
    		url : getRootPath()+"/capitalPlan/capitalPassAudit",
    		data : {relationId :value,relationSign:53},
    		dataType : 'json',
    		async:false,
    		success : function(returnDate) {
    			if(returnDate.returnCode==0){
    				auditNew(value);
    			}else{
    				$.fool.alert({msg:returnDate.message});
    			}
    		}
    	});
    }
    
  //资金计划
    function zjjh(id){
    	$('#scancel').length>0?$('#scancel').click():null;
    	win = $("#pop-win").fool('window',{modal:true,'title':"资金计划",height:480,width:780,href:getRootPath()+'/capitalPlanDetail/window?relationId='+id+'&relationSign=53&billType=fyd'});
    }
  
    if("${param.billId}"){
    	editById("${param.billId}");
    }
</script>
</body>
</html>
