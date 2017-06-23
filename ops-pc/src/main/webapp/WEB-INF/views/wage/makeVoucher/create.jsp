<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <title>计提工资</title>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <style>
    </style>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>计提工资</h1>
    </div>
</div>
<div class="nav-box">
    <ul>
        <li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
        <li><a href="javascript:;">工资</a></li>
        <li><a href="javascript:;" class="curr">计提工资</a></li>
    </ul>
</div>
<div class="form1">
    <form id="form">
        <!--  <input id="debitSubjectId" name="expenseSubjectId"/> -->
        <input id="creditSubjectId" name="wageSubjectId" type="hidden"/>
        <div id="formIn">
            <p>
                <font><em>*</em>科目：</font><input id="creditSubjectName" class="textBox"/>
            </p>
            <p>
                <font><em>*</em>科目方向：</font><input id="direction" name="direction" class=" easyui-validatebox textBox"/>
            </p>
            <p>
                <font><em>*</em>工资项目：</font><input id="wageFormulaId" name="wageFormulaId" class="textBox"/>
            </p></br>
            <p>
                <font><em>*</em>部门：</font><input id="deptId" name="deptId" class="textBox"/>
            </p>
            <p>
                <input type="button" id="addForm" class="btn-blue2 btn-xs" value="增加会计分录"/>
                <input type="button" id="delForm" class="btn-blue2 btn-xs" value="删除会计分录"/>
                <input type="button" id="delAll" class="btn-blue2 btn-xs" value="全部删除"/>
            </p>
        </div>
        </br>
        <div id="formIn2">
            <p>
                <font><em>*</em>核算工资年月：</font><input id="month" name="month" class="textBox"/>
            </p>
            <p>
                <font><em>*</em>凭证日期：</font><input id="voucherDate" name="voucherDate" class="textBox"/>
            </p>
            <p>
                <font><em>*</em>凭证字：</font><input id="voucherWordId" name="voucherWordId" class="textBox"/>
            </p></br>
            <p>
                <font>凭证摘要：</font><input id="voucherResume" name="resume" class="textBox"/>
            </p>
            <p>
                <fool:tagOpt optCode="svoucherMake"><input type="button" id="save" class="btn-blue2 btn-xs "
                                                           style="background:#FFC76C" value="生成凭证"/></fool:tagOpt>
            </p>
        </div>
        <br>
        <table id="wageList" style="width:0px"></table>
        <div id="pager"></div>
    </form>
</div>


<script type="text/javascript">
    var busScenevalue = [
        {id: '1', name: '借方'},
        {id: '-1', name: '贷方'},
    ];

    var chooserWindow = "";
    /*var newData = "";*/
    /*var optionCount = "";*/
    //工资项目
    var wageFormulaValue = "";
    $.ajax({
        url: "${ctx}/wageFormula/queryAll?num=" + Math.random(),
        async: false,
        data: {},
        success: function (data) {
            wageFormulaValue = formatData(data, 'fid');
        }
    });
    var wageFormulaName = $('#wageFormulaId').fool("dhxCombo", {
        height: "32px",
        width: "160px",
        editable: false,
        required: true,
        novalidate: true,
        setTemplate: {
            input: "#columnName#",
            option: "#columnName#"
        },
        toolsBar: {
            name: "工资项目",
            addUrl: "/wageFormula/listWageFormula",
            refresh: true
        },
        data: wageFormulaValue,
        focusShow: true,
    });

    var today = new Date().format("yyyy-MM-dd");
    $("#voucherDate").datebox({
        width: 160,
        height: 32,
        editable: false,
        required: true,
        novalidate: true,
        value: today
    });
    /*$.post('${ctx}/voucher/getDefaultMessage',function(data){
     $('#voucherDate').datebox('setValue',data.voucherDate);
     });*/

    //科目方向
    var directionName = $("#direction").fool("dhxCombo", {
        width: 160,
        height: 32,
        required: true,
        novalidate: true,
        editable: false,
        focusShow: true,
        data: [
            {
                value: '1',
                text: '借方'
            }, {
                value: '-1',
                text: '贷方'
            }
        ]
    });

    $("#month").datebox({
        width: 160,
        height: 32,
        editable: false,
        required: true,
        novalidate: true,
        onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
            setTimeout(function () {
                span.trigger('click');
            }, 0);  //触发click事件弹出月份层,秒延迟是为了防止系统面板上移的BUG
            if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                tds = p.find('div.calendar-menu-month-inner td');
                tds.click(function (e) {
                    e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                    var year = /\d{4}/.exec(span.html())[0];//得到年份
                    var month = parseInt($(this).attr('abbr'), 10) + 1; //月份
                    $('#month').datebox('hidePanel');//隐藏日期对象
                    $('#month').datebox('setValue', year + '-' + month); //设置日期的值
                });
            }, 0);
            $("#month").datebox('calendar').find('.calendar-header').hide();
            $("#month").datebox('calendar').parent().siblings('.datebox-button').hide();
            $('input.calendar-menu-year').attr("disabled", "disabled");//避免填写年份后会回到选择日期的面板的问题
        },
        parser: function (s) {//配置parser，返回选择的日期
            if (!s) return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
        },
        formatter: function (d) {
            if (d.getMonth() == 0) {
                return d.getFullYear() - 1 + '-12';
            } else {
                return d.getFullYear() + '-' + d.getMonth();
            }
        }//配置formatter，只返回年月
    });
    var p = $('#month').datebox('panel'), //日期选择对象
        tds = false, //日期选择对象中月份
        span = p.find('span.calendar-text'); //显示月份层的触发控件
    /*  $('#deptId').fool('deptComBoxTree',{
     required:true,
     novalidate:true,
     editable:false,
     width:164,
     height:31
     }); */

    //部门初始化
    var deptValue = '';
    $.ajax({
        url: "${ctx}/orgController/getAllTree?num=" + Math.random(),
        async: false,
        success: function (data) {
            deptValue = formatData(data[0].children, 'id', 'texr');
        }
    });
    var deptName = $("#deptId").fool("dhxCombo", {
        width: 160,
        height: 32,
        data: deptValue,
        required: true,
        novalidate: true,
        editable: false,
        focusShow: true,
        toolsBar: {
            name: "部门",
            addUrl: "/orgController/deptMessage",
            refresh: true
        },
    });


    /*  $("#creditSubjectName").fool('subjectCombobox',{
     validType:'isBank',
     required:true,
     missingMessage:'该项不能为空！',
     novalidate:true,
     width:164,
     height:31,
     url:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
     focusShow:true,
     onClickIcon:function(){
     $(this).combobox('hidePanel');
     $(this).combobox('textbox').blur();
     creditChooser(this);
     },
     onSelect:function(record){
     $("#creditSubjectId").val(record.fid);
     }
     }); */

    $('#creditSubjectName').fool('dhxComboGrid', {
        width: 164,
        height: 31,
        focusShow: true,
        data: getComboData(getRootPath() + "/fiscalSubject/getSubject?leafFlag=1&q=&num=" + Math.random()),
        filterUrl: getRootPath() + "/fiscalSubject/getSubject?leafFlag=1",
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
            $("#creditSubjectId").val(($('#creditSubjectName').next())[0].comboObj.getSelectedValue());
        }
    });
    $('#creditSubjectName').next().find(".dhxcombo_select_button").click(function () {
        ($('#creditSubjectName').next())[0].comboObj.closeAll();
        creditChooser();
    });

    //摘要
    var voucherResumeValue = "";
    $.ajax({
        url: "${ctx}/basedata/resume?num=" + Math.random(),
        async: false,
        data: {},
        success: function (data) {
            voucherResumeValue = formatTree(data[0].children, "text", "id")
        }
    });
    var voucherResumeName = $('#voucherResume').fool("dhxComboGrid", {
        height: "32px",
        width: "160px",
        //editable:false,
        data: voucherResumeValue,
        filterUrl: '${ctx}/basedata/fuzzyFindSubAuxiliaryAttrTree?code=014&num=' + Math.random(),
        setTemplate: {
            input: "#name#",
            columns: [
                {option: '#name#', header: '内容', width: 100},
            ],
        },
        toolsBar: {
            name: "摘要",
            addUrl: "/basedata/listAuxiliaryAttr",
            refresh: true
        },
        focusShow: true,
/*        onChange: function () {
            hasAbstract();
            newData = "";
        }*/
    });

    voucherResumeName.hdr = null;

/*    function hasAbstract() {
        optionCount = voucherResumeName.getOptionsCount();
        if (optionCount <= 2) {
            sInput.next().css('display', '');
        } else {
            sInput.next().css('display', 'none');
        }
    }*/

    var refresh = $(".mytoolsBar a.btn-refresh")[0];
    var sInput = $(voucherResumeName.getInput());
    //修改input宽度
    sInput.css("width", sInput.width() - 20);
    sInput.after('<a href="javascript:;" onclick="resumeSave()" class="btn-save"></a>');
    sInput.next().css({'float': 'right', 'margin': '5px 23px', 'display': ''});
    function resumeSave() {
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
    }


    //凭证字
    var voucherWordValue = "";
    $.ajax({
        url: "${ctx}/basedata/voucherWord?num=" + Math.random(),
        async: false,
        data: {},
        success: function (data) {
            voucherWordValue = formatTree(data[0].children, "text", "id")
        }
    });
    var voucherWordName = $('#voucherWordId').fool("dhxCombo", {
        height: "32px",
        width: "160px",
        editable: false,
        data: voucherWordValue,
        setTemplate: {
            input: "#name#",
            option: "#name#"
        },
        toolsBar: {
            name: "凭证字",
            addUrl: "/basedata/listAuxiliaryAttr",
            refresh: true
        },
        focusShow: true,
        required: true,
        novalidate: true,


    });


    /*   $('#wageList').datagrid({
     fitColumns:true,
     pagination:true,
     singleSelect:true,
     url:"${ctx}/wageVoucher/query",
     frozenColumns:[[
     {field:'fid',title:'fid',checkbox:true},
     ]],
     columns:[[
     {field:'wageFormulaId',title:'wageFormulaId',hidden:true},
     {field:'expenseSubjectId',title:'expenseSubjectId',hidden:true},
     {field:'wageSubjectId',title:'wageSubjectId',hidden:true},
     {field:'voucherWordId',title:'voucherWordId',hidden:true},
     {field:'deptId',title:'deptId',hidden:true},
     {field:'updateTime',title:'updateTime',hidden:true},

     //{field:'voucherWordName',title:'凭证号',width:20,sortable:false},
     {field:'deptName',title:'部门',width:30,sortable:false},
     {field:'wageFormulaName',title:'工资项目',width:50,sortable:false},
     {field:'wageSubjectName',title:'科目',width:50,sortable:false},
     {field:'direction',title:'科目方向',width:50,sortable:false,formatter:direc},
     // {field:'remark',title:'备注',width:60,sortable:false},
     ]]
     });  */

    $('#wageList').jqGrid({
        datatype: "json",
        url: "${ctx}/wageVoucher/query",
        footerrow: true,
        autowidth: true,
        height: $(window).outerHeight() - 450 + "px",
        pager: "#pager",
//     		rowNum:10,
//     		rowList:[10,20,30],
        viewrecords: true,
        jsonReader: {
            records: "total",
            total: "totalpages",
        },
        multiselect: true,
        colModel: [
            //{name:'',label:"",align:'center',width:"25px",formatter:function(cellvalue,options,row){ return "<input class='checker' fid='"+row.fid+"' type='checkbox' title='删除已勾选的凭证分录' onclick='checker(this,\""+row.fid+"\")'/>"; }},
//                     {name:'',label:"",align:'center',width:"25px",formatter:function(cellvalue,options,row){ return "<input class='checker' fid='"+row.fid+"' type='checkbox' title='删除已勾选的凭证分录'/>"; }},
            {name: 'fid', label: "fid", hidden: true},
            {name: 'wageFormulaId', label: 'wageFormulaId', hidden: true},
            {name: 'expenseSubjectId', label: 'expenseSubjectId', hidden: true},
            {name: 'wageSubjectId', label: 'wageSubjectId', hidden: true},
            {name: 'voucherWordId', label: 'voucherWordId', hidden: true},
            {name: 'deptId', label: 'deptId', hidden: true},
            {name: 'updateTime', label: 'updateTime', hidden: true},
            {name: 'deptName', label: '部门', width: 30,},
            {name: 'wageFormulaName', label: '工资项目', width: 50,},
            {name: 'wageSubjectName', label: '科目', width: 50,},
            {name: 'direction', label: '科目方向', width: 50, formatter: direc},
        ],
    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false});
    $("#wageList").removeAttr("style");
    //科目方向
    function direc(value, row, index) {
        if (value == 1) {
            return "借方";
        } else {
            return "贷方";
        }
        return value;
    }
    $('#addForm').click(function () {//增加凭证分录
        $('#formIn').form('enableValidation');
        if ($('#formIn').form('validate')) {
            var addData = $('#form').serializeJson();
            //alert(JSON.stringify(addData));
            /* var remark = $('#voucherResume').combotree('getText');
             addData = $.extend(addData,{remark:remark}); */
            $.post('${ctx}/wageVoucher/save', addData, function (data) {
                dataDispose(data);
                if (data.returnCode == 0) {
                    $.fool.alert({time: 1000, msg: '保存成功'});
                    $('#wageList').trigger('reloadGrid');
                } else if (data.returnCode == 1) {
                    $.fool.alert({msg: data.message});
                } else {
                    $.fool.alert({msg: '系统繁忙，请稍后再试'});
                }
            });
        }
    });

    /*  var delefid=''//删除行的fid
     function checker(target,fid){
     delefid=fid;
     $('.checker').not(target).removeAttr("checked");
     }  */

    $('#delForm').click(function () {//删除凭证分录
        var ids = '';
        var rowids = $('#wageList').jqGrid("getGridParam", "selarrrow");
//     		var rows=$(".checker:checkbox:checked");
        for (var i = 0; i < rowids.length; i++) {
            ids += $('#wageList').jqGrid("getRowData", rowids[i]).fid + ",";
        }
        if (ids == '' || ids == null) {
            $.fool.alert({msg: '请选择需要删除的会计分录'});
            return;
        }
        $.fool.confirm({
            title: '删除提示', msg: '确认删除已勾选的项目？', fn: function (data) {
                if (!data) {
                    return false;
                }
                $.post('${ctx}/wageVoucher/delete', {fid: ids}, function (data) {
                    dataDispose(data);
                    if (data.returnCode == 0) {
                        $.fool.alert({time: 1000, msg: '删除成功'});
                        $('#wageList').trigger('reloadGrid');
                    } else if (data.returnCode == 1) {
                        $.fool.alert({msg: data.message});
                    } else {
                        $.fool.alert({msg: '系统繁忙，请稍后再试'});
                    }
                });
            }
        });
    });

    $('#delAll').click(function () {//全部删除
        $.fool.confirm({
            title: '删除提示', msg: '确认删除全部项目？', fn: function (data) {
                if (data) {
                    $.post('${ctx}/wageVoucher/deleteAll', function (data) {
                        if (data.returnCode == 0) {
                            $.fool.alert({time: 1000, msg: '全部删除成功'});
                            $('#wageList').trigger('reloadGrid');
                        } else if (data.returnCode == 1) {
                            $.fool.alert({msg: data.message});
                        } else {
                            $.fool.alert({msg: '系统繁忙，请稍后再试'});
                        }
                    });
                } else {
                    return false;
                }
            }
        });

    });

    //彈出框
    /* function debitChooser(){
     chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectDebit&onDblClick=selectDebitDBC&singleSelect=true&flag=1'});
     } */
    function creditChooser() {
        chooserWindow = $.fool.window({
            'title': "选择科目",
            href: '${ctx}/fiscalSubject/window?okCallBack=selectCredit&onDblClick=selectCreditDBC&singleSelect=true&flag=1',
            //onLoad:function(){$('#creditSubjectName').combobox('hidePanel');}});//解决IE点击弹窗下拉框不消失问题
            onLoad: function () {
                $(':input').blur();
                ($('#creditSubjectName').next())[0].comboObj.closeAll();
            }
        });//解决IE点击弹窗下拉框不消失问题
    }
    /* function selectDebit(rowData){
     $("#debitSubjectName").combobox("setValue",rowData[0].name);
     $("#debitSubjectId").val(rowData[0].fid);
     chooserWindow.window('close');
     }
     function selectDebitDBC(rowData) {
     $("#debitSubjectName").combobox("setValue",rowData.name);
     $("#debitSubjectId").val(rowData.fid);
     chooserWindow.window('close');
     } */
    function selectCredit(rowData) {
        /*  $("#creditSubjectName").combobox("setText",rowData[0].name);
         $("#creditSubjectId").val(rowData[0].fid); */
        $("#creditSubjectId").val(rowData[0].fid);
        ($("#creditSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
        chooserWindow.window('close');
    }
    function selectCreditDBC(rowData) {
        $("#creditSubjectId").val(rowData.fid);
        ($("#creditSubjectName").next()).comboObj.setComboText(rowData.name);
        chooserWindow.window('close');
    }

    //生成按钮点击事件
    $("#save").click(function () {
        var month = $("#month").datebox("getValue") + "-01";
        var voucherDate = $("#voucherDate").datebox("getValue");
        var voucherWordId = voucherWordName.getSelectedValue();
        //var resume=voucherResumeName.getSelectedValue();
        var resume = voucherResumeName.getComboText();
/*        var hasValue = ($('#voucherResume').next())[0].comboObj.getSelectedValue();
        if (hasValue == null) {
            hasValue = newData;
        }
        var hasName = ($('#voucherResume').next())[0].comboObj.getComboText();
        if (hasValue == null && hasName != "" && optionCount <= 2 || hasValue == "" && hasName != "" && optionCount <= 2) {
            $.fool.alert({time: 1000, msg: '请先保存凭证摘要！'});
            return;
        } else if (hasValue == null && hasName != "" && optionCount > 2 || hasValue == "" && hasName != "" && optionCount > 2) {
            $.fool.alert({time: 1000, msg: '您没有选择凭证摘要！'});
            return;
        }*/
        $('#formIn2').form('enableValidation');
        if ($('#formIn2').form('validate')) {
            $('#save').attr("disabled", "disabled");
            $.post('${ctx}/vouchermake/makeVoucher?billType=110', {
                month: month,
                voucherDate: voucherDate,
                voucherWordId: voucherWordId,
                voucherResume: resume
            }, function (data) {
                dataDispose(data);
                if (data.returnCode == '0') {
                    $.fool.alert({
                        time: 1000, msg: "生成凭证完成！", fn: function () {
                            $('#save').removeAttr("disabled");
                            $('#formIn2').form('clear');
                            $('#formIn2').form('disableValidation');
                        }
                    });
                } else if (data.returnCode == '1') {
                    $.fool.alert({msg: data.message});
                    $('#save').removeAttr("disabled");
                } else {
                    $.fool.alert({time: 1000, msg: "系统正忙，请稍后再试。"});
                    $('#save').removeAttr("disabled");
                }
            });
        }
        ;
    });
</script>
</body>
</html>
