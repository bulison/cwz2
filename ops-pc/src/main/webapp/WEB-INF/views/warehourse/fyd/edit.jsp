<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
</head>
<body>
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}"
       scope="page"></c:set>

<style>
    #em2 {
        display: none;
    }

    body {
        overflow: hidden;
    }

    /* .mybtn-footer{ border: 1px solid red; } */
    .mybtn-footer p {
        float: right;
    }

    .mybtn-footer p input {
        background: #50B3E7;
        color: #fff;
        border-radius: 4px;
        font-size: 16px;
        line-height: 30px;
    }

    .myform .textBox {
        height: 30px;
    }

    #dataBox .in-box {
        text-align: center;
    }

    #list1 p {
        text-align: left;
    }

    #list1 p.payMsg span {
        margin-left: 100px;
    }
</style>

<form id="form" class="bill-form myform">
    <div id="title">
        <div id="title1" class="shadow" style="margin:10px 0px 0 0;padding:11px 0; height:18px;">
            <div id="square1"><span class="backBtn"></span>
                <div id="triangle"></div>
            </div>
            <h1 style="margin-top:2px;">${billflagName}费用单单据</h1><a id="hide" onClick="aniTo('bill');"
                                                                    style="display:none;position:absolute;right:40px;top:13px;"
                                                                    class="btn-ora-add">单据信息</a>
        </div>
    </div>

    <div id="bill" class="shadow" style="margin-top:50px;">
        <div class="billTitle myTitle">
            <div id="square2"></div>
            <h2 style="margin-top: -2px;">填写单据信息</h2></div>
        <div class="in-box" id="list2">
            <div class="inlist">
                <input id="fid" type="hidden" value="${obj.fid}"/>
                <input id="updateTime" type="hidden" value="${obj.updateTime}"/>
                <input id="csvId" type="hidden" value="${obj.csvId}"/>
                <input id="memberId" type="hidden" value="${obj.memberId}"/>
                <input id="bankId" type="hidden" value="${obj.bankId}"/>
                <input id="recordStatus" type="hidden" value="${obj.recordStatus}"/>
                <input id="payerId" type="hidden" value=""/>
                <p>
                    <font><em>*</em>单号：</font><input id="code" class="textBox" value="${obj.code}"/>
                    <font>原始单号：</font><input id="vouchercode" class="textBox" value="${param.flag=='copy'?'':obj.voucherCode}"/>
                    <font><em>*</em>单据日期：</font><input id="billDate" class="textBox"/>
                </p>
                <p>
                    <font><em>*</em>费用项目：</font><input id="feeName" class="textBox"/>
                    <%-- <font>收入金额：</font><input id="incomeAmount" class="textBox" value="${obj.incomeAmount}"/> --%>
                    <font><em>*</em>费用金额：</font><input id="freeAmount" class="textBox" value="${obj.freeAmount}"/>
                    <font><em>*</em>经手人：</font><input id="memberName" class="textBox" value="${obj.memberName}"/>
                </p>
                <p>
                    <%-- <font><em id="em">*</em>银行账号：</font><input id="bankAccount" class="textBox" value="${obj.bankName}${empty obj.bankAccount?null:'('}${empty obj.bankAccount?null:obj.bankAccount}${empty obj.bankAccount?null:')'}"/> --%>
                    <font><em>*</em>部门：</font><input id="deptName" class="textBox" value="${obj.deptId}"/>
                    <font>费用标识：</font><input name="expenseType" id="expenseType" class="textBox"
                                             value="${empty obj.expenseType?0:obj.expenseType}"/>
                    <font><em id="em" style="display:none;">*</em>收支单位：</font><input id="csvName" class="textBox"
                                                                                     value="${obj.csvName}"/>
                </p>
                <p>
                    <font>已对单金额：</font><input id="totalCheckAmount" class="textBox" readonly="readonly"
                                              value="${obj.totalCheckAmount}"/>
                    <font>未对单金额：</font><input id="mytotalUnCheckAmount" class="textBox" readonly="readonly"
                                              value="${obj.totalUnCheckAmount}"/>
                    <font>已收/付款金额：</font><input name="totalPayAmount" id="totalPayAmount" class="textBox"
                                                readonly="readonly"
                                                value="${obj.totalPayAmount == '0E-8'?0:obj.totalPayAmount}"/>
                </p>
                <p>
                    <font>未收/付款金额：</font><input id="notPayAmount" class="textBox" readonly="readonly"/>
                    <%--<font>付款人：</font><input id="payerName" class="textBox" value="${obj.payerName}"/>--%>
                    <font>备注：</font><input id="describe" class="textBox" value="${obj.describe}"/>
                </p>
                <%--<p>--%>
                    <%--<font>现金/银行：</font><input id="cashbName" class="textBox" value="${obj.bankId}"/>--%>
                    <%--<font>付款金额：</font><input id="payAmount" class="textBox" value="${obj.payAmount}">--%>
                <%--</p>--%>
                <p style="display: inline-block;margin-left:44px">&emsp;其他信息：<img id="openBtn" alt="展开"
                                                                                  src="${ctx}/resources/images/openNode.png"
                                                                                  style="vertical-align: middle;"><img
                        id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png"
                        style="vertical-align: middle;display: none"></p><br/>
                <p class="hideOut">
                    <font>制单人：</font><input id="creatorName" class="textBox" disabled="disabled"
                                            value="${obj.creatorName}"/>
                    <font>制单时间：</font><input id="createTime" class="textBox" disabled="disabled"
                                             value="${obj.createTime}"/>
                    <font>审核人：</font><input id="auditorName" class="textBox" disabled="disabled"
                                            value="${obj.auditorName}"/>
                </p>
                <p class="hideOut">
                    <font>审核时间：</font><input id="auditTime" class="textBox" disabled="disabled"
                                             value="${obj.auditTime}"/>
                    <font>作废人：</font><input id="cancelorName" class="textBox" disabled="disabled"
                                            value="${obj.cancelorName}"/>
                    <font>作废时间：</font><input id="cancelTime" class="textBox" disabled="disabled"
                                             value="${obj.cancelTime}"/>
                </p>
            </div>
        </div>
    </div>
    <div id="dataBox" class="shadow" style="display:none;">
        <div class="billTitle">
            <div id="square2"></div>
            <h2>操作</h2></div>
        <div class="in-box" id="list1">
            <input id="sbankId" type="hidden"/>
            <p class="payMsg"><span id="tpay">累计已收款金额：</span><span id="ntpay">累计未收款金额：</span></p>
            <p>
                <font><em>*</em>收款单号：</font><input id="scode" class="textBox" readonly="readonly"/>
                <font><em>*</em>收款人：</font><input id="smember" class="textBox" value="${obj.payerName}"/>
                <font><em>*</em>现金/银行：</font><input id="sbankAccount" class="textBox" value="${obj.bankId}"/>
            </p>
            <p>
                <font><em>*</em>收款金额：</font><input id="samount" class="textBox"/>
            </p>
            <div style="text-align:center;margin-top:30px;">
                <input type="button" id="ssave" class="btn-blue2 btn-xs" value="确定"/>
                <input type="button" id="scancel" class="btn-blue2 btn-xs" value="取消"/>
            </div>
        </div>
    </div>
</form>
<div id="checkBox"></div>
<div class="mybtn-footer" _id="${obj.fid}">
    <p><input type="button" onclick="payBill()" id="mypay" class="btn-blue2 btn-xs" value="付款" style="display:none;"/>
    </p>
    <p><input type="button" onclick="collection()" id="mycollection" class="btn-blue2 btn-xs" value="收款"
              style="display:none;"/></p>
</div>
<div id="pop-win"></div>
<script type="text/javascript">
    //打印参数配置
    var texts = [
        {title: '原始单号', key: 'voucherCode'},
        {title: '单号', key: 'code'},
        {title: '单据日期', key: 'billDate'},
        {title: '费用项目', key: 'feeName'},
        {title: '收入金额', key: 'incomeAmount'},
        {title: '支出金额', key: 'freeAmount'},
        {title: '银行账号', key: 'bankAccount'},
        {title: '经办人', key: 'memberName'},
        {title: '付款人', key: 'payerName'},
        {title: '部门', key: 'deptName'},
        {title: '收支单位', key: 'csvName'},
        {title: '现金/银行', key: 'cashbName'},
        {title: '备注', key: 'describe', br: true}
    ];

    var thead = [];
    var boxWidth = 182, boxHeight = 31;//统一设置搜索下拉输入框的大小
    var billType = "";//记录审核状态时可以进行的操作是收款还是付款
    var checker = "";
    var samountKey = 0;
    var _id = $('#fid').val();
    var vo1 = {};
    var myStat = "${obj.recordStatus}";
    var expenseType="${obj.expenseType}";
    var freeAmount="${obj.freeAmount}";
    var csvType="${obj.csvType}";
    var csvId="${obj.csvId}";
    $('.inlist').addClass('status' + myStat);//加上是否审核作废的状态图标

    if ("${param.mark}" == "1") {
        $('#mytotalUnCheckAmount').val("");
        $('#totalPayAmount').val("");
    }

    var next = "<p><input id=\"nextSave\" type='button' style='width:auto !important;' class=\"btn-blue2 btn-xs\" value=\"下一张单\" /></p>";
    var nextNew = "<p><input id=\"nextSave\" type='button' style='width:auto !important;' class=\"btn-blue2 btn-xs\" value=\"保存并新增\" /></p>";

    var orgData = "", memData = "", costData = "", csvData = "", bankData = "";
    $.ajax({
        url: "${ctx}/basedata/query?num=" + Math.random(),
        async: false,
        data: {param: "Organization,Member,AuxiliaryAttr_CostType,CSV"},
        success: function (data) {
            orgData = formatTree(data.Organization[0].children, "text", "id");
            memData = formatData(data.Member, "fid");
            costData = formatTree(data.AuxiliaryAttr_CostType[0].children, "text", "id");
            csvData = formatData(data.CSV, "fid");
        }
    });

    // if ('${obj.recordStatus}' == 1) {//已审核
    $.ajax({
        url: getRootPath() + '/bankController/list',
        async: false,
        data: {},
        success: function (data) {
            //console.log(data);
            bankData = formatData(data.rows, "fid");
        }
    });
    // }


    function checkStatus() {
        if ($('#recordStatus').val() == 0) {
        	var zjjh= '<a id="zjjh" href="javascript:;" onclick="zjjh(\''+$("#fid").val()+'\')" class=\"mybtn-blue mybtn-s\">资金计划</a>';
            $(".mybtn-footer").html('<p><input type="button" onclick="saver()" id="save" class="btn-blue2 btn-xs" value="保存" /></p> <p><input type="button" onclick="copyr()" id="copy" class="btn-blue2 btn-xs" value="复制" /></p> <p><input type="button" onclick="printr()" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" onclick="refreshFormr()" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p>' +zjjh+ next);
        }
    }

    //返回按钮指向链接
    $('#square1 .backBtn').click(function () {
        $("#addBox").window("close");
    });

    //控件初始化
    var feeCombo = $("#feeName").fool("dhxCombo", {
        required: true,
        //missingMessage : '该项不能为空！',
        novalidate: true,
        data: costData,
        setTemplate: {
            input: "#name#",
            option: "#name#"
        },
        toolsBar:{
            name:"费用项目",
            addUrl:"/basedata/listAuxiliaryAttr",
            refresh:true
        },
        focusShow: true,
        width: 182,
        height: 32,
        editable: false,
        value: "${obj.feeId}",
        onChange: function (value, text) {
            var record = $("#feeName").next()[0].comboObj.getSelectedText();
            if (record != "" && !record.myflag) {
                $.fool.alert({msg: "请选择子节点"});
                $("#feeName").next()[0].comboObj.setComboText("");
                return false;
            }
        }
    });

    $('#payAmount').blur(function(t){
        var payAmount = $('#payAmount').val();
        var feeAmount = $('#freeAmount').val();

        if (payAmount > feeAmount) {
            var msg = '付款金额不能大于费用金额';
            alert(msg);
            return false;
        }
    });


    var expCombo = $('#expenseType').fool("dhxCombo", {
        required: true,
        novalidate: true,
        data: [{
            text: '不处理',
            value: '0'
        }, {
            text: '增加往来单位应收/应付款项',
            value: '1'
        }, {
            text: '减少往来单位应收/应付款项',
            value: '2'
        }],
        width: 180,
        height: 32,
        editable: false,
        focusShow: true,
        onChange: function (value, text) {
            bank();
        }
    });
    //控制收支单位的必填标识
    if (expCombo.getSelectedValue() != 0) {
        $('#em').show();
    } else {
        $('#em').hide();
    }

    //bank
    var csvCombo = "";
    function bank() {
        var expenseType = expCombo.getSelectedValue();
        if (expenseType != 0) {
            $('#em').show();
            csvCombo = $('#csvName').fool('dhxComboGrid', {//供应商
                required: true,
                novalidate: true,
                focusShow: true,
                width: boxWidth,
                height: boxHeight,
                hasDownArrow: false,
                data: csvData,
                filterUrl: '${ctx}/customerSupplier/list',
                setTemplate: {
                    input: "#name#",
                    columns: [
                        {option: '#code#', header: '编号', width: 100},
                        {option: '#name#', header: '名称', width: 100},
                        {option: "#type#", header: '类别', css: "type", width: 100},
                    ]
                },
                toolsBar: {
                    refresh:true
                },
                onChange: function (value, text) {
                    value != null ? $("#csvId").val(value) : null;
                },
                onLoadSuccess: function (combo) {
                    $('.type .dhxcombo_cell_text').each(function () {
                        $(this).text($(this).text() == 1 ? "客户" : "供应商");
                    });
                },
                text: "${obj.csvName}"
            });
            validateBox($("#freeAmount"), true, ['amount', 'numMaxLength[10]']);
            /* $('#bankAccount').fool('combogrid',{//可优化代码
             required:false,
             novalidate:true,
             width:182,
             height:30,
             idField:'fid',
             textField:'name',
             validType:"combogridValid['bankId']",
             fitColumns:true,
             focusShow:true,
             panelWidth:350,
             panelHeight:283,
             url:getRootPath()+'/bankController/list',
             columns:[[
             {field:'fid',title:'fid',hidden:true},
             {field:'code',title:'编号',width:40,searchKey:false},
             {field:'name',title:'名称',width:80,searchKey:false},
             {field:'bank',title:'银行',width:80,searchKey:false},
             {field:'account',title:'账号',width:200,searchKey:false},
             {field:'keyWord',hidden:true,searchKey:true},
             ]],
             onSelect:function(index,row){
             $("#bankId").val(row.fid);
             setTimeout(function(){$("#bankAccount").combogrid('setText',row.name+'('+row.account+')');},100);
             $('#bankAccount').combo('enableValidation').combo('validate');
             },
             onChange:function(nv,ov){
             $("#bankId").val('');
             }
             }); */
        } else {
            $('#em').hide();
            csvCombo = $('#csvName').fool('dhxComboGrid', {//供应商
                required: false,
                novalidate: true,
                focusShow: true,
                width: boxWidth,
                height: boxHeight,
                data: csvData,
                hasDownArrow: false,
                filterUrl: '${ctx}/customerSupplier/list',
                setTemplate: {
                    input: "#name#",
                    columns: [
                        {option: '#code#', header: '编号', width: 100},
                        {option: '#name#', header: '名称', width: 100},
                        {option: "#type#", header: '类别', css: "type", width: 100},
                    ]
                },
                toolsBar: {
                    addUrl:'/supplier/manage' ,
                    name:'供应商管理',
                    refresh:true
                },
                onChange: function (value, text) {
                    value != null ? $("#csvId").val(value) : null;
                },
                onLoadSuccess: function (combo) {
                    $('.type .dhxcombo_cell_text').each(function () {
                        $(this).text($(this).text() == 1 ? "客户" : "供应商");
                    });
                },
                text: "${obj.csvName}"
            });
            validateBox($("#freeAmount"), true, ['balanceAmount', 'numMaxLength[10]']);
            /* $('#bankAccount').fool('combogrid',{//可优化代码
             required:true,
             novalidate:true,
             width:182,
             height:30,
             idField:'fid',
             textField:'name',
             validType:"combogridValid['bankId']",
             fitColumns:true,
             focusShow:true,
             panelWidth:350,
             panelHeight:283,
             url:getRootPath()+'/bankController/list',
             columns:[[
             {field:'fid',title:'fid',hidden:true},
             {field:'code',title:'编号',width:40,searchKey:false},
             {field:'name',title:'名称',width:80,searchKey:false},
             {field:'bank',title:'银行',width:80,searchKey:false},
             {field:'account',title:'账号',width:200,searchKey:false},
             {field:'keyWord',hidden:true,searchKey:true},
             ]],
             onSelect:function(index,row){
             $("#bankId").val(row.fid);
             setTimeout(function(){$("#bankAccount").combogrid('setText',row.name+'('+row.account+')');},100);
             $('#bankAccount').combo('enableValidation').combo('validate');
             },
             onChange:function(nv,ov){
             $("#bankId").val('');
             }
             }); */
        }
        if ('${obj.recordStatus}' != "" && '${obj.recordStatus}' != 0) {
            csvCombo.disable();
        }
    }
    //现金、银行
    var cashbCombo = $('#cashbName').fool('dhxComboGrid', {
//        required: true,
        novalidate: true,
        width: 182,
        height: 30,
        focusShow: true,
        data: bankData,
        hasDownArrow: false,
        filterUrl: getRootPath() + '/bankController/list',
        setTemplate: {
            input: "#name#(#account#)",
            columns: [
                {option: '#code#', header: '编号', width: 40},
                {option: '#name#', header: '名称', width: 80},
                {option: '#bank#', header: '银行', width: 80},
                {option: '#account#', header: '账号', width: 200},
            ]
        },
        toolsBar: {
            name: "银行",
            addUrl: "/bankController/bankManager",
            refresh: true,
        },
        onChange: function (value, text) {
            value != null ? $("#bankId").val(value) : null;
        },
        text: "${obj.bankName}${empty obj.bankAccount?'':'('}${empty obj.bankAccount?'':obj.bankAccount}${empty obj.bankAccount?'':')'}"
    });


    //付款人
    var memCombo = $('#payerName').fool('dhxComboGrid', {
//        required: true,
        novalidate: true,
        focusShow: true,
        width: boxWidth,
        height: boxHeight,
        data: memData,
        hasDownArrow: false,
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
        toolsBar: {
            name: "付款人",
            addUrl: "/member/memberManager",
            refresh: true
        },
        onChange: function (value, text) {
            value != null ? $("#payerId").val(value) : null;
        },
        text: "${obj.payerName}"
    });


    //收付款操作输入框初始化
    validateBox($("#scode"), true);
    var sfmember = $('#smember').fool('dhxComboGrid', {//收/付款人
        required: true,
        novalidate: true,
        focusShow: true,
        width: boxWidth,
        height: boxHeight,
        data: memData,
        hasDownArrow: false,
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
        toolsBar: {
            name: "收/付款人",
            addUrl: "/fiscalSubject/manage",
            refresh: true
        },
    });
    var bankCombo = $('#sbankAccount').fool('dhxComboGrid', {//可优化代码
        required: true,
        novalidate: true,
        width: 182,
        height: 30,
        focusShow: true,
        data: bankData,
        hasDownArrow: false,
        filterUrl: getRootPath() + '/bankController/list',
        searchKey: "keyWord",
        setTemplate: {
            input: "#name#(#account#)",
            columns: [
                {option: '#code#', header: '编号', width: 40},
                {option: '#name#', header: '名称', width: 80},
                {option: '#bank#', header: '银行', width: 80},
                {option: '#account#', header: '账号', width: 200},
            ]
        },
        toolsBar: {
            name: "银行",
            addUrl: "/bankController/bankManager",
            refresh: true,
        },
        onChange: function (value, text) {
            value != null ? $("#sbankId").val(value) : null;
        }
    });


    //付款按钮函数
    function payBill() {
        billShow("付款");
        billType = 52;
        var fid = $("#fid").val();
        $.post("${ctx}/costBill/getCodeAndAmount", {fid: fid, type: billType}, function (data) {
            dataDispose(data);
            if (data.returnCode == 0) {
                var amount = data.data.amount;
                var payAmount = data.data.payAmount;
                var totalPayAmount = data.data.totalPayAmount;
                if (amount == 0) {
                    $('#dataBox #list1 p[class!=payMsg]').hide();
                    $('#ssave').hide();
                }
                $('#tpay').append(data.data.totalPayAmount);
                $('#ntpay').append(amount);
                $('#scode').val(data.data.code);
                samountKey == 1 ?
                    validateBox($("#samount"), true, ["samount[" + Math.abs(amount) + "]", "balanceAmount", "ffamount"]) :
                    validateBox($("#samount"), true, ["samount[" + Math.abs(amount) + "]", "balanceAmount", "famount"]);
                $("#samount").validatebox("enableValidation");
                if (totalPayAmount != 0) {
                    $('#samount').val(samountKey == 1 ? -Math.abs(amount) : Math.abs(amount));
                } else {
                    $('#samount').val(samountKey == 1 ? -Math.abs(amount) : Math.abs(amount));
//                    $('#samount').val(samountKey == 1 ? -Math.abs(payAmount) : Math.abs(payAmount));
                }
                $("#samount").validatebox("validate");
            } else if (data.returnCode == 1) {
                $.fool.alert({msg: data.message});
            } else {
                $.fool.alert({msg: "服务器繁忙，请稍后再试！"});
            }
        });
    }

    $.extend($.fn.validatebox.defaults.rules, {
        samount: {//验证金额少于等于某个数
            validator: function (value, param) {
                return Math.abs(parseFloat(value)) <= Math.abs(parseFloat(param[0]));
            },
            message: '金额必须小于或等于{0}'
        },
        famount: {
            validator: function (value, param) {
                return parseFloat(value) > 0;
            },
            message: '金额需为正数'
        },
        ffamount: {
            validator: function (value, param) {
                return parseFloat(value) < 0;
            },
            message: '金额需为负数'
        }
    });
    //收款按钮函数
    function collection() {
        billShow("收款");
        billType = 51;
        var fid = $("#fid").val();
        $.post("${ctx}/costBill/getCodeAndAmount", {fid: fid, type: billType}, function (data) {
            dataDispose(data);
            if (data.returnCode == 0) {
                var amount = data.data.amount;
                if (amount == 0) {
                    $('#dataBox #list1 p[class!=payMsg]').hide();
                    $('#ssave').hide();
                }
                $('#tpay').append(data.data.totalPayAmount);
                $('#ntpay').append(Math.abs(amount));
                $('#scode').val(data.data.code);
                if (samountKey == 1) {
                    validateBox($("#samount"), true, ["samount[" + Math.abs(amount) + "]", "balanceAmount", "ffamount"]);
                } else {
                    validateBox($("#samount"), true, ["samount[" + Math.abs(amount) + "]", "balanceAmount", "famount"]);
                }
                $("#samount").validatebox("enableValidation");
                $('#samount').val(samountKey == 1 ? -Math.abs(amount) : Math.abs(amount));
                $("#samount").validatebox("validate");
            } else if (data.returnCode == 1) {
                $.fool.alert({msg: data.message});
            } else {
                $.fool.alert({msg: "服务器繁忙，请稍后再试！"});
            }
        });
    }
    //收付款操作的确定和取消
    $('#ssave').click(function () {
        $('#list1').form("enableValidation");
        if (!$('#list1').form("validate")) {
            return false;
        }
        $('#ssave').attr("disabled", "disabled");
        var costBillId = $('#fid').val();
        var expenseType = expCombo.getSelectedValue();
        var code = $('#scode').val();
        var amount = $('#samount').val();
        var bankId = $('#sbankId').val();
        var memberId = sfmember.getSelectedValue();
        $.post("${ctx}/costBill/savePaymentReceived", {
            code: code,
            amount: amount,
            bankId: bankId,
            memberId: memberId,
            costBillId: costBillId,
            expenseType: expenseType,
            billType: billType
        }, function (data) {
            dataDispose(data);
            if (data.returnCode == 0) {
                $.fool.alert({
                    msg: "保存成功", time: 1000, fn: function () {
                        $('#ssave').removeAttr("disabled");
                        if(!"${obj.fid}"){
                        	$("#fid").val(data.dataExt.fid);
                        }
                        $('#dataBox').hide();
                        $('#dataBox').find("input[type!=button]").val("");
                        $('#smember').next()[0].comboObj.setComboText("");
                        $('#sbankAccount').next()[0].comboObj.setComboText("");
                    }
                });
                validateBox($("#samount"), true, ["samount[" + data.data.amount + "]", "amount"]);
            } else if (data.returnCode == 1) {
                $.fool.alert({msg: data.message});
                $('#ssave').removeAttr("disabled");
            } else {
                $.fool.alert({msg: "服务器繁忙，请稍后再试！"});
                $('#ssave').removeAttr("disabled");
            }
        });
    });
    $('#scancel').click(function () {
        $('#dataBox').hide();
        $('#dataBox').find("input[type=text]").val("");
    });

    //根据收款、付款类型显示操作框
    function billShow(name) {
        $('#dataBox').show();
        $('#dataBox .billTitle h2').html(name + "操作");
        $('#dataBox #list1 #tpay').html("累计已" + name + "金额：");
        $('#dataBox #list1 #ntpay').html("累计未" + name + "金额：");
        $('#dataBox #list1 #scode').prev().html("<em>*</em>" + name + "单号：");
        $('#dataBox #list1 #smember').prev().html("<em>*</em>" + name + "人：");
        $('#dataBox #list1 #samount').prev().html("<em>*</em>" + name + "金额：");
    }

    var deptCombo = $("#deptName").fool("dhxCombo", {
        required: true,
        missingMessage: '该项不能为空！',
        novalidate: true,
        data: orgData,
        setTemplate: {
            input: "#text#",
            option: "#text#"
        },
        toolsBar: {
            name: "部门",
            addUrl: "/orgController/deptMessage",
            refresh: true
        },
        focusShow: true,
        width: 182,
        height: 32,
        editable: false,
        value: "${obj.deptId}"
    });
    validateBox($("#vouchercode"));
    validateBox($("#code"), true);
    var today = new Date();
    var todayStr = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate();
    if ("${obj.billDate}") {
        dateBox($("#billDate"), true, "${obj.billDate}");
    } else {
        dateBox($("#billDate"), true, todayStr);
    }
    validateBox($("#incomeAmount"), false, ['amount', 'numMaxLength[10]']);
    if (expCombo.getSelectedValue() != 0) {//根据费用标识是否为0，判断费用金额是否可以输入负数
        validateBox($("#freeAmount"), false, ['amount', 'numMaxLength[10]']);
    } else {
        validateBox($("#freeAmount"), false, ['balanceAmount', 'numMaxLength[10]']);
    }
    /* comboBox($("#bankAccount"),"${ctx}/bankController/comboboxData",true,function(){
     $("#bankAccount").combobox("setValue","${obj.bankId}");
     }); */


    /* $('#bankAccount').fool('combogrid',{
     required:true,
     novalidate:true,
     width:182,
     height:30,
     idField:'fid',
     textField:'name',
     validType:"combogridValid['bankId']",
     fitColumns:true,
     focusShow:true,
     panelWidth:350,
     panelHeight:283,
     url:getRootPath()+'/bankController/list',
     columns:[[
     {field:'fid',title:'fid',hidden:true},
     {field:'code',title:'编号',width:40,searchKey:false},
     {field:'name',title:'名称',width:80,searchKey:false},
     {field:'bank',title:'银行',width:80,searchKey:false},
     {field:'account',title:'账号',width:200,searchKey:false},
     {field:'keyWord',hidden:true,searchKey:true},
     ]],
     onSelect:function(index,row){
     $("#bankId").val(row.fid);
     setTimeout(function(){$("#bankAccount").combogrid('setText',row.name+'('+row.account+')');},100);
     $('#bankAccount').combo('enableValidation').combo('validate');
     },
     onChange:function(nv,ov){
     $("#bankId").val('');
     }
     }); */
    //validateBox($("#memberName"),true);
    //validateBox($("#csvName"));
    validateBox($("#describe"));

    //选取收支单位、经手人
    /* var chooserWindow="";
     $("#csvName").click(function(){
     chooserWindow=$.fool.window({width:780,height:480,'title':"选择收支单位",href:'${ctx}/customerSupplier/window?okCallBack=selectCsv&onDblClick=selectCsvDBC&singleSelect=true'});
     });
     $("#memberName").click(function(){
     chooserWindow=$.fool.window({width:780,height:480,'title':"选择经手人",href:'${ctx}/member/window?okCallBack=selectUser&onDblClick=selectUserDBC&singleSelect=true'});
     }) */
    ;


    //保存
    function saver() {
        var fid = $("#fid").val();
        var updateTime = $("#updateTime").val();
        var vouchercode = $("#vouchercode").val();
        var code = $("#code").val();
        var billDate = $("#billDate").datebox("getValue");
        var feeId = feeCombo.getSelectedValue();
        var incomeAmount = $("#incomeAmount").val();
        var freeAmount = $("#freeAmount").val();
        var deptId = deptCombo.getSelectedValue();
        var csvId = $("#csvId").val();
        var bankId = $("#bankId").val();
        var memberId = $("#memberId").val();
        var describe = $("#describe").val();
        var expenseType = expCombo.getSelectedValue();
        var payerId = $("#payerId").val();
        var payAmount = $("#payAmount").val();
        $('#list2').form('enableValidation');
        if ($('#list2').form('validate')) {
            $('#save').attr("disabled", "disabled");
            $.post('${ctx}/costBill/save', {
                fid: fid,
                updateTime: updateTime,
                expenseType: expenseType,
                voucherCode: vouchercode,
                code: code,
                billDate: billDate,
                csvId: csvId,
                bankId: bankId,
                memberId: memberId,
                describe: describe,
                feeId: feeId,
                incomeAmount: incomeAmount,
                freeAmount: freeAmount,
                deptId: deptId,
                payerId: payerId,
                payAmount: payAmount,
            }, function (data) {
                dataDispose(data);
                if (data.returnCode == '0') {
                    $.fool.alert({
                        time: 1000, msg: '保存成功！', fn: function () {
                            if ($('#fid').val() == '') {
                                $('#fid').val(data.dataExt.fid);
                                $('#updateTime').val(data.dataExt.updateTime);
                                $('#recordStatus').val(0);
                                checkStatus();
                            } else {
                                $("#addBox").window("close");
                            }
                            /* window.location.href="
                            ${ctx}/costBill/manage"; */
                            $('#save').removeAttr("disabled");
                            $('#billList').trigger('reloadGrid');

                        }
                    });
                } else if (data.returnCode == '1') {
                    if (data.errorCode == '103' || data.errorCode == '104' || data.errorCode == '105') {
                        $.fool.alert({
                            btnName: '转到会计期间页面', btnAct: 'mybtnAct(this)', msg: data.message, fn: function () {
                            }
                        });
                    } else {
                        $.fool.alert({
                            msg: data.message, fn: function () {
                            }
                        });
                    }
                    $('#save').removeAttr("disabled");
                } else {
                    $.fool.alert({msg: "系统正忙，请稍后再试。"});
                    $('#save').removeAttr("disabled");
                }
                return true;
            });
        } else {
            return false;
        }
        ;
    }
    ;

    //审核
    $('#verify').click(function (e) {
        $.fool.confirm({
            title: '确认', msg: '确定要审核通过此单据吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/costBill/passAudit',
                        data: {fid: $("#fid").val()},
                        dataType: 'json',
                        success: function (data) {
                            dataDispose(data);
                            if (data.returnCode == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '审核成功！', fn: function () {
                                        $('#billList').trigger('reloadGrid');
                                        $('#addBox').window("refresh");
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

    });

    //打印
    function printr() {
        printHandle("${ctx}/costBill/print?id=" + $("#fid").val(), 0);
    }
    ;

    //作废
    $('#cancel').click(function (e) {
        $.fool.confirm({
            title: '确认', msg: '确定要作废此单据吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/costBill/cancel',
                        data: {fid: $("#fid").val()},
                        dataType: 'json',
                        success: function (data) {
                            dataDispose(data);
                            if (data.returnCode == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '已作废！', fn: function () {
                                        $('#billList').trigger('reloadGrid');
                                        $('#addBox').window("refresh");
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
    });

    //复制
    function copyr() {
        if (checker != "") {
            $("#checkBox").window("destroy");
        }
        var fid = $("#fid").val();
        $('#addBox').window({
    		onLoad:function(){
    			return null;
    		}
    	});
        $('#pop-win').length>0 && $('#pop-win').html()!=''?$('#pop-win').window('destroy'):null;
        $('#addBox').window("open");
        $('#addBox').window("refresh", "${ctx}/costBill/edit?fid=" + fid + "&mark=1&flag=copy");
        /* window.location.href="
        ${ctx}/costBill/edit?fid="+fid+"&mark=1&flag=copy"; */
    }
    ;

    //刷新
    function refreshFormr() {
        /* if(checker!=""){
         $("#checkBox").window("destroy");
         }
         $('#addBox').window("refresh"); */

        var url = '';
        var flag = '';
        if ($('#title1 h1').text().search(/新增/) != -1 || $('#title1 h1').text().search(/复制/) != -1 || $('#title1 h1').text().search(/编辑/) != -1) {
            flag = 'edit';
        } else {
            flag = 'detail';
        }
        var value = $('#fid').val();
        url = '${ctx}/costBill/edit?flag=' + flag + '&fid=' + value;
        /* $("#checkBox").window("destroy"); */
        if ($("#checkBox").length > 0 && $('#checkBox').html()) {//修复了刷新单据再按对单时对单弹窗异常的问题
            $("#checkBox").window("destroy");
        }
        if ($("#pop-win").length > 0 && $('#pop-win').html()) {//修复了刷新单据再按费用付款收款详情弹窗异常的问题
            $("#pop-win").window("destroy");
        }
        $('#addBox').window({
    		onLoad:function(){
    			return null;
    		}
    	});
        $('#addBox').window("open");
        $('#addBox').window("refresh", url);
    };

    //对单
    function checkr() {
        $('#scancel').click();//弹窗自动取消收付款操作
        checker = $("#checkBox").window({
            title: '对单单据',
            top: 50,
            collapsible: false,
            minimizable: false,
            maximizable: false,
            resizable: false,
            href: '${ctx}/costCheck/manage?costBillId=' + $("#fid").val() + "&csvId=" + $("#csvId").val() + "&csvType=${obj.csvType}&expenseType=${obj.expenseType}",
            width: $(window).width() - 10,
            height: $(window).height() - 60,
        });
        /* window.location.href='
        ${ctx}/costCheck/manage?costBillId='+$("#fid").val()+"&csvId="+$("#csvId").val(); */
    }
    ;

    //显示、隐藏详细
    /* $("#openBtn").click(
     function(e) {
     $(".hideOut").css("display","inline-block");
     $('#openBtn').css("display","none");
     $('#closeBtn').css("display","inline-block");
     });
     $("#closeBtn").click(
     function(e) {
     $(".hideOut").css("display","none");
     $('#openBtn').css("display","inline-block");
     $('#closeBtn').css("display","none");
     }); */

    //选取收支单位、经手人的方法
    /* function selectCsv(rowData){
     $("#csvName").focus();
     $("#csvId").val(rowData[0].fid);
     $("#csvName").val(rowData[0].name);
     chooserWindow.window('close');
     }
     function selectCsvDBC(rowData){
     $("#csvName").focus();
     $("#csvId").val(rowData.fid);
     $("#csvName").val(rowData.name);
     chooserWindow.window('close');
     }
     function selectUser(rowData){
     $("#memberName").focus();
     $("#memberId").val(rowData[0].fid);
     $("#memberName").val(rowData[0].username);
     chooserWindow.window('close');
     }
     function selectUserDBC(rowData){
     $("#memberName").focus();
     $("#memberId").val(rowData.fid);
     $("#memberName").val(rowData.username);
     chooserWindow.window('close');
     }
     */
    //控件初始化
    function validateBox(obj, required, validType) {
        obj.validatebox({
            required: required,
            missingMessage: '该项不能为空！',
            novalidate: true,
            width: 180,
            height: 32,
            validType: validType
        });
    }
    function comboBox(obj, url, required, onLSFn) {
        obj.combobox({
            valueField: "fid",
            textField: "name",
            required: required,
            missingMessage: '该项不能为空！',
            novalidate: true,
            url: url,
            width: 180,
            height: 32,
            editable: false,
            onLoadSuccess: onLSFn
        });
    }
    function comboTree(obj, url, required, onLSFn) {
        obj.combotree({
            valueField: "fid",
            textField: "name",
            required: required,
            missingMessage: '该项不能为空！',
            novalidate: true,
            url: url,
            width: 180,
            height: 32,
            editable: false,
            onLoadSuccess: onLSFn
        });
    }
    function dateBox(obj, required, value) {
        if ('${obj.recordStatus}' == '1' || '${obj.recordStatus}' == '2'){
            obj.datebox({
                disabled:true
            })
        };
        obj.datebox({
            required: required,
            missingMessage: '该项不能为空！',
            novalidate: true,
            width: 180,
            height: 32,
            editable: false,
            value: value
        });
    }


    function typeFun(value) {
        for (var i = 0; i < type.length; i++) {
            if (type[i].id == value) return type[i].name;
        }
        return value;
    }
    ;

    //

    //鼠标获取焦点出现下拉列表
    var memCombo = $('#memberName').fool('dhxComboGrid', {
        required: true,
        novalidate: true,
        focusShow: true,
        width: boxWidth,
        height: boxHeight,
        data: memData,
        hasDownArrow: false,
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
        toolsBar: {
            name: "经手人",
            addUrl: "/member/memberManager",
            refresh: true
        },
        onChange: function (value, text) {
            value != null ? $("#memberId").val(value) : null;
        },
        text: "${obj.memberName}"
    });
    /* $('#csvName').fool('combogrid',{//供应商
     required:false,
     novalidate:true,
     focusShow:true,
     width:boxWidth,
     height:boxHeight,
     idField:'fid',
     textField:'name',
     panelWidth:350,
     url:'${ctx}/customerSupplier/list',
     columns:[[
     {field:'fid',title:'fid',hidden:true},
     {field:'code',title:'编号',width:100,searchKey:false},
     {field:'name',title:'名称',width:100,searchKey:false},
     {field:'type',title:'类别',width:100,formatter:typeFun,searchKey:false},
     {field:'searchKey',hidden:true,searchKey:true},
     ]],
     onSelect:function(index,row){
     $("#csvId").val(row.fid);
     $("#csvName").val(row.name);
     }
     }); */


    //设置鼠标放进去自动出来下拉列表
    $("#form").find("input[_class]").each(function (i, n) {
        ($(this));
    });
    enterController('form');
    //详细信息折叠按钮
    $("#openBtn").click(
        function (e) {
            $(".hideOut").css("display", "inline-block");
            $('#openBtn').css("display", "none");
            $('#closeBtn').css("display", "inline-block");
        });
    $("#closeBtn").click(
        function (e) {
            $(".hideOut").css("display", "none");
            $('#openBtn').css("display", "inline-block");
            $('#closeBtn').css("display", "none");
        });

    $(function () {
        $("input").attr('autocomplete', 'off');
        //判断有无订单生成规则
        if ($("#fid").val()) {
            $("#code").val('${obj.code}');
            $("#code").attr("readonly", "readonly");
            if ('${code}') {
                $("#fid").val("");
                $("#code").val('${code}');
                $("#code").attr("readonly", "readonly");
            }
            if ('${obj.freeAmount}' != 0 || '${obj.incomeAmount}' == "") {
                $("#incomeAmount").val(0);
                $("#incomeAmount").attr("disabled", true);
            }
            if ('${obj.incomeAmount}' != 0 || '${obj.freeAmount}' == "") {
                $("#freeAmount").val(0);
                $("#freeAmount").attr("disabled", true);
            }
        } else if ('${code}') {
            $("#code").val('${code}');
            $("#code").attr("readonly", "readonly");
        }
        //判断订单状态
        var zjjh= '<a id="zjjh" href="javascript:;" onclick="zjjh(\''+$("#fid").val()+'\')" class=\"mybtn-blue mybtn-s\">资金计划</a>';
        if ('${obj.recordStatus}' == null || '${obj.recordStatus}' == '') {
            $(".mybtn-footer").append('<p><input type="button" onclick="saver()" id="save" class="btn-blue2 btn-xs" value="保存" /></p>' + nextNew);
        } else if ('${obj.recordStatus}' == 0) {
            $(".mybtn-footer").append('<p><input type="button" onclick="saver()" id="save" class="btn-blue2 btn-xs" value="保存" /></p><p><input type="button" onclick="copyr()" id="copy" class="btn-blue2 btn-xs" value="复制" /></p><p><input type="button" onclick="printr()" id="print" class="btn-blue2 btn-xs" value="打印" /></p><p><input type="button" onclick="refreshFormr()" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p>'+zjjh+ next);
        } else if ('${obj.recordStatus}' == 1) {
            $('#totalPayAmount').val(${empty obj.totalPayAmount?0.00:obj.totalPayAmount}.toFixed(2));
            $('#notPayAmount').val($('#freeAmount').val() - $('#totalPayAmount').val());
            var fkxq = "", skxq = "";
            if ('${obj.expenseType}' != 0) {
                //在费用标识不为0的情况下，按条件分别显示付款、收款按钮
                if ("${obj.csvType}" == 2) {//付款
                    $('#mycollection').hide();
                    $('#mypay').show();
                    fkxq = '<p><input id="fkxq" type="button" onclick="fkxq(\'' + _id + '\')" class=\"btn-blue2 btn-xs\" style="width:auto !important;" value="付款详情"/></p>';
                } else if ("${obj.csvType}" == 1) {//收款
                    $('#mycollection').show();
                    $('#mypay').hide();
                    skxq = '<p><input id="skxq" type="button" onclick="skxq(\'' + _id + '\')" class=\"btn-blue2 btn-xs\" style="width:auto !important;" value="收款详情" /></p>';
                }
                '${obj.expenseType}' == 2 && $('#freeAmount').val() > 0 ? samountKey = 1 : null;
            } else {
                //在费用标识为0的情况下，按条件分别显示付款、收款按钮
                var myvalue = parseFloat($("#freeAmount").val());
                if (myvalue > 0) {//付款
                    $('#mycollection').hide();
                    $('#mypay').show();
                    fkxq = '<p><input id="fkxq" type="button" onclick="fkxq(\'' + _id + '\')" class=\"btn-blue2 btn-xs\" style="width:auto !important;" value="付款详情"/></p>';
                } else if (myvalue < 0) {//收款
                    $('#mycollection').show();
                    $('#mypay').hide();
                    skxq = '<p><input id="skxq" type="button" onclick="skxq(\'' + _id + '\')" class=\"btn-blue2 btn-xs\" style="width:auto !important;" value="收款详情" /></p>';
                }
            }
            var fyxq = '<p><input id="fyxq" type="button" onclick="fyxq(\'' + _id + '\')" class=\"btn-blue2 btn-xs\" style="width:auto !important;" value="费用详情" /></p>';
            $("#list2").find("input").attr('disabled', 'disabled').css('background', '#EBEBE4');
            $(".mybtn-footer").append(fkxq + skxq + fyxq + '<p><input type="button" onclick="refreshFormr()" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p><p><input type="button" onclick="printr()" id="print" class="btn-blue2 btn-xs" value="打印" /></p><p><input type="button"  onclick="copyr()" id="copy" class="btn-blue2 btn-xs" value="复制" /></p><p><input type="button" onclick="checkr()" id="check" class="btn-blue2 btn-xs dd" value="对单"/></p>'+zjjh + next);
            deptCombo.disable();
            expCombo.disable();
            memCombo.disable();
            feeCombo.disable();
        } else if ('${obj.recordStatus}' == 2) {
            $('#totalPayAmount').val(${empty obj.totalPayAmount?0.00:obj.totalPayAmount}.toFixed(2)
        )
            ;
            $('#notPayAmount').val($('#freeAmount').val() - $('#totalPayAmount').val());
            $("#list2").find("input").attr('disabled', 'disabled').css('background', '#EBEBE4');
            $(".mybtn-footer").append('<p><input type="button" onclick="copyr()" id="copy" class="btn-blue2 btn-xs" value="复制" /></p><p><input type="button" onclick="printr()" id="print" class="btn-blue2 btn-xs" value="打印" /></p><p><input type="button" onclick="refreshFormr()" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p>' + next);
            deptCombo.disable();
            expCombo.disable();
            feeCombo.disable();
            memCombo.disable();
        }

        /* $("#incomeAmount").keyup(function(){
         if($("#incomeAmount").val()&&$("#incomeAmount").val()!=0){
         $("#freeAmount").val(0);
         $("#freeAmount").attr("disabled",true);
         $("#freeAmount").validatebox("disableValidation");
         }else{
         $("#freeAmount").val("");
         $("#freeAmount").removeAttr("disabled",true);
         }
         }); */

        /* $("#freeAmount").keyup(function(){
         if($("#freeAmount").val()&&$("#freeAmount").val()!=0){
         $("#incomeAmount").val(0);
         $("#incomeAmount").attr("disabled",true);
         $("#incomeAmount").validatebox("disableValidation");
         }else{
         $("#incomeAmount").val("");
         $("#incomeAmount").removeAttr("disabled",true);
         }
         }); */
    });
    //付款详情
    function fkxq(id) {
        $('#scancel').click();//弹窗自动取消收付款操作
        win = $("#pop-win").fool('window', {
            modal: true,
            'title': "付款详情",
            height: 480,
            width: 780,
            href: getRootPath() + '/paymentCheck/checkingWin?billCode=fkd&billId=' + _id + '&_billCode=fyd'

        });
    }
    //收款详情
    function skxq(id) {
        $('#scancel').click();
        win = $("#pop-win").fool('window', {
            modal: true,
            'title': "收款详情",
            height: 480,
            width: 780,
            href: getRootPath() + '/paymentCheck/checkingWin?billCode=skd&billId=' + _id + '&_billCode=fyd'
        });
    }
    //费用详情
    function fyxq(id) {
        $('#scancel').click();
        win = $("#pop-win").fool('window', {
            modal: true,
            'title': "费用详情",
            height: 480,
            width: 780,
            href: getRootPath() + '/paymentCheck/checkingWin?billCode=fyd&billId=' + _id + '&_billCode=fyd'
        });
    }

    $("#nextSave").click(function () {
        if ('${obj.recordStatus}' == null || '${obj.recordStatus}' == '') {
            var fid = $("#fid").val();
            var updateTime = $("#updateTime").val();
            var vouchercode = $("#vouchercode").val();
            var code = $("#code").val();
            var billDate = $("#billDate").datebox("getValue");
            var feeId = feeCombo.getSelectedValue();
            var incomeAmount = $("#incomeAmount").val();
            var freeAmount = $("#freeAmount").val();
            var deptId = deptCombo.getSelectedValue();
            var csvId = $("#csvId").val();
            var bankId = $("#bankId").val();
            var memberId = $("#memberId").val();
            var describe = $("#describe").val();
            var expenseType = expCombo.getSelectedValue();
            var payerId = $("#payerId").val();
            var payAmount = $("#payAmount").val();
            $('#list2').form('enableValidation');
            if ($('#list2').form('validate')) {
                $('#nextSave').attr("disabled", "disabled");
                $.post('${ctx}/costBill/save', {
                    fid: fid,
                    updateTime: updateTime,
                    expenseType: expenseType,
                    voucherCode: vouchercode,
                    code: code,
                    billDate: billDate,
                    csvId: csvId,
                    bankId: bankId,
                    memberId: memberId,
                    describe: describe,
                    feeId: feeId,
                    incomeAmount: incomeAmount,
                    freeAmount: freeAmount,
                    deptId: deptId,
                    payerId: payerId,
                    payAmount: payAmount
                }, function (data) {
                    dataDispose(data);
                    if (data.returnCode == '0') {
                        $.fool.alert({
                            time: 1000, msg: '保存成功！', fn: function () {
                                $('#addBox').window('setTitle', "新增凭证");
                                $('#addBox').window('refresh', "${ctx}/costBill/add");
                                /* window.location.href="
                                ${ctx}/costBill/manage"; */
                                $('#nextSave').removeAttr("disabled");
                                $('#billList').trigger('reloadGrid');

                            }
                        });
                    } else if (data.returnCode == '1') {
                        if (data.errorCode == '103' || data.errorCode == '104' || data.errorCode == '105') {
                            $.fool.alert({
                                btnName: '转到会计期间页面', btnAct: 'mybtnAct(this)', msg: data.message, fn: function () {
                                }
                            });
                        } else {
                            $.fool.alert({
                                msg: data.message, fn: function () {
                                }
                            });
                        }
                        $('#nextSave').removeAttr("disabled");
                    } else {
                        $.fool.alert({msg: "系统正忙，请稍后再试。"});
                        $('#nextSave').removeAttr("disabled");
                    }
                    return true;
                });
            } else {
                return false;
            }
            ;
        } else if ('${obj.recordStatus}' == 0) {
            var fid = $("#fid").val();
            var updateTime = $("#updateTime").val();
            var vouchercode = $("#vouchercode").val();
            var code = $("#code").val();
            var billDate = $("#billDate").datebox("getValue");
            var feeId = feeCombo.getSelectedValue();
            var incomeAmount = $("#incomeAmount").val();
            var freeAmount = $("#freeAmount").val();
            var deptId = deptCombo.getSelectedValue();
            var csvId = $("#csvId").val();
            var bankId = $("#bankId").val();
            var memberId = $("#memberId").val();
            var describe = $("#describe").val();
            var expenseType = expCombo.getSelectedValue();
            var payerId = $("#payerId").val();
            var payAmount = $("#payAmount").val();
            var vo = {
                fid: fid,
                updateTime: updateTime,
                expenseType: expenseType,
                voucherCode: vouchercode,
                code: code,
                billDate: billDate,
                csvId: csvId,
                bankId: bankId,
                memberId: memberId,
                describe: describe,
                feeId: feeId,
                incomeAmount: incomeAmount,
                freeAmount: freeAmount,
                deptId: deptId,
                payerId: payerId,
                payAmount: payAmount
            };
            $('#list2').form('enableValidation');
            if ($('#list2').form('validate')) {
                $('#nextSave').attr("disabled", "disabled");
                if (!equals(vo, vo1)) {
                    $.post('${ctx}/costBill/save', vo, function (data) {
                        dataDispose(data);
                        if (data.returnCode == '0') {
                            $.fool.alert({
                                time: 1000, msg: '保存成功！', fn: function () {
                                    var index = $('#billList tr.jqgrow td[title=${obj.fid}]').parent().attr("id") - 1;
                                    var rows = $('#billList').jqGrid('getRowData');
                                    var fid = "";
                                    var status = "";
                                    if (rows[index - 1]) {
                                        fid = rows[index - 1].fid;
                                        status = rows[index - 1].recordStatus;
                                    }
                                    if (fid == "") {
                                        var next = getNext("#billList", "${ctx}/costBill/list?");
                                        if (next) {
                                            if (next[0].recordStatus == 1 || next[0].recordStatus == 2) {
                                                $('#addBox').window('setTitle', "查看费用单");
                                                $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + next[next.length - 1].fid + "&flag=detail");
                                            } else {
                                                $('#addBox').window('setTitle', "编辑费用单");
                                                $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + next[next.length - 1].fid + "&flag=edit");
                                            }
                                        } else {
                                            $('#addBox').window('setTitle', "新增费用单");
                                            $('#addBox').window('refresh', "${ctx}/costBill/add");
                                        }
                                    } else {
                                        if (status == "已审核" || status == "已作废") {
                                            $('#addBox').window('setTitle', "查看费用单");
                                            $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + fid + "&flag=detail");
                                        } else {
                                            $('#addBox').window('setTitle', "编辑费用单");
                                            $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + fid + "&flag=edit");
                                        }
                                    }
                                    /* window.location.href="
                                    ${ctx}/costBill/manage"; */
                                    $('#nextSave').removeAttr("disabled");
                                    $('#billList').trigger('reloadGrid');

                                }
                            });
                        } else if (data.returnCode == '1') {
                            if (data.errorCode == '103' || data.errorCode == '104' || data.errorCode == '105') {
                                $.fool.alert({
                                    btnName: '转到会计期间页面', btnAct: 'mybtnAct(this)', msg: data.message, fn: function () {
                                    }
                                });
                            } else {
                                $.fool.alert({
                                    msg: data.message, fn: function () {
                                    }
                                });
                            }
                            $('#nextSave').removeAttr("disabled");
                        } else {
                            $.fool.alert({msg: "系统正忙，请稍后再试。"});
                            $('#nextSave').removeAttr("disabled");
                        }
                        return true;
                    });
                } else {
                    var index = $('#billList tr.jqgrow td[title=${obj.fid}]').parent().attr("id") - 1;
                    var rows = $('#billList').jqGrid('getRowData');
                    var fid = "";
                    var status = "";
                    if (rows[index - 1]) {
                        fid = rows[index - 1].fid;
                        status = rows[index - 1].recordStatus;
                    }
                    if (fid == "") {
                        var next = getNext("#billList", "${ctx}/costBill/list?");
                        if (next) {
                            if (next[0].recordStatus == 1 || next[0].recordStatus == 2) {
                                $('#addBox').window('setTitle', "查看费用单");
                                $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + next[next.length - 1].fid + "&flag=detail");
                            } else {
                                $('#addBox').window('setTitle', "编辑费用单");
                                $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + next[next.length - 1].fid + "&flag=edit");
                            }
                        } else {
                            $('#addBox').window('setTitle', "新增费用单");
                            $('#addBox').window('refresh', "${ctx}/costBill/add");
                        }
                    } else {
                        if (status == "已审核" || status == "已作废") {
                            $('#addBox').window('setTitle', "查看费用单");
                            $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + fid + "&flag=detail");
                        } else {
                            $('#addBox').window('setTitle', "编辑费用单");
                            $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + fid + "&flag=edit");
                        }
                    }
                    $('#nextSave').removeAttr("disabled");
                    $('#billList').trigger("reloadGrid");
                }
            } else {
                return false;
            }
            ;
        } else if ('${obj.recordStatus}' == 1 || '${obj.recordStatus}' == 2) {
            var index = $('#billList tr.jqgrow td[title=${obj.fid}]').parent().attr("id") - 1;
            var rows = $('#billList').jqGrid('getRowData');
            var fid = "";
            var status = "";
            if (rows[index - 1]) {
                fid = rows[index - 1].fid;
                status = rows[index - 1].recordStatus;
            }
            if (fid == "") {
                var next = getNext("#billList", "${ctx}/costBill/list?");
                if (next) {
                    if (next[0].recordStatus == 1 || next[0].recordStatus == 2) {
                        $('#addBox').window('setTitle', "查看费用单");
                        $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + next[next.length - 1].fid + "&flag=detail");
                    } else {
                        $('#addBox').window('setTitle', "编辑费用单");
                        $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + next[next.length - 1].fid + "&flag=edit");
                    }
                } else {
                    $('#addBox').window('setTitle', "新增费用单");
                    $('#addBox').window('refresh', "${ctx}/costBill/add");
                }
            } else {
                if (status == "已审核" || status == "已作废") {
                    $('#addBox').window('setTitle', "查看费用单");
                    $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + fid + "&flag=detail");
                } else {
                    $('#addBox').window('setTitle', "编辑费用单");
                    $('#addBox').window('refresh', "${ctx}/costBill/edit?fid=" + fid + "&flag=edit");
                }
            }
        }
    });

    vo1 = loadedVo();
    function loadedVo() {
        var fid = $("#fid").val();
        var updateTime = $("#updateTime").val();
        var vouchercode = $("#vouchercode").val();
        var code = $("#code").val();
        var billDate = $("#billDate").datebox("getValue");
        var feeId = feeCombo.getSelectedValue();
        var incomeAmount = $("#incomeAmount").val();
        var freeAmount = $("#freeAmount").val();
        var deptId = deptCombo.getSelectedValue();
        var csvId = $("#csvId").val();
        var bankId = $("#bankId").val();
        var memberId = $("#memberId").val();
        var describe = $("#describe").val();
        var expenseType = "${obj.expenseType}";
        return {
            fid: fid,
            updateTime: updateTime,
            expenseType: expenseType,
            voucherCode: vouchercode,
            code: code,
            billDate: billDate,
            csvId: csvId,
            bankId: bankId,
            memberId: memberId,
            describe: describe,
            feeId: feeId,
            incomeAmount: incomeAmount,
            freeAmount: freeAmount,
            deptId: deptId
        };
    }
</script>
</body>
</html>