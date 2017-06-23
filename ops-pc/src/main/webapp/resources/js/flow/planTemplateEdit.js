/**
 * edit
 */
$('html').css('overflow', 'hidden');
var _days = 0;//控制主表预计完成天数的最小值
var IDindex = '';//保存编辑的行id
//var users = [];
var mainTable = $('#planform').serializeJson();//主表
var isEqul = true;
//屏蔽默认快捷键
$(document).keydown(function (e) {
    if (e.ctrlKey && e.keyCode == 83) {//ctrl+s
        if (e.preventDefault) {  // firefox
            e.preventDefault();
        } else { // other
            e.returnValue = false;
        }
    }
});
var plankd = '';//ctrl快捷键全局变量
//编辑页面保存按钮快捷键
$(document).keydown(plankd = function (e) {
    if (e.ctrlKey && e.keyCode == 83) {//ctrl+s
        $('#mysave').click();
    }
});
/*
 * 添加行
 */
$('#addRow').tooltip({
    position: 'top',
    content: '<span style="color:#fff"><strong>不选择节点时：</strong>新增最上级新行<br><strong>选择节点时：</strong>新增节点下的新行</span>',
    onShow: function () {
        $(this).tooltip('tip').css({backgroundColor: '#2CA7F6', borderColor: '#2CA7F6'});
    }
});

function hideBtn(bool) {
    var panel = $('#modelList').treegrid('getPanel');
    if (bool) {
        panel.find('.btn-edit').hide();
        panel.find('.btn-del').hide();
        $('#addRow').hide();
    } else {
        panel.find('.btn-edit').show();
        panel.find('.btn-del').show();
        $('#addRow').show();
    }
}
//任务级别
var taskLevelValue = '';
$.ajax({
    url: getRootPath() + "/flow/taskLevel/queryAll?num=" + Math.random(),
    async: false,
    type: "post",
    success: function (data) {
        taskLevelValue = formatData(data, "fid", 'name');
    }
});
var taskLevelId = $("#taskLevelId").fool("dhxCombo", {
    required: true,
    novalidate: true,
    width: 182,
    height: 32,
    data: taskLevelValue,
    clearOpt: false,
    setTemplate: {
        input: "#name#",
        option: "#name#"
    },
    toolsBar: {
        name: "事件级别",
        addUrl: "/flow/taskLevel/manage",
        refresh: true
    },
    editable: false,
    focusShow: true,
    onLoadSuccess: function (combo) {
        combo.setComboValue("${planTemplate.taskLevelId}");
    }
});
//保密级别
var securityValue = '';
$.ajax({
    url: getRootPath() + "/flow/security/queryAll",
    async: false,
    type: "post",
    success: function (data) {
        securityValue = formatData(data, "fid", 'name');
    }
});
var securityLevelId = $("#securityLevelId").fool("dhxCombo", {
    required: true,
    novalidate: true,
    width: 182,
    height: 32,
    data: securityValue,
    clearOpt: false,
    setTemplate: {
        input: "#name#",
        option: "#name#"
    },
    toolsBar: {
        name: "保密级别",
        addUrl: "/flow/security/manage",
        refresh: true
    },
    editable: false,
    focusShow: true,
    onLoadSuccess: function (combo) {
        combo.setComboValue("${planTemplate.securityLevelId}");
    }
});
//负责人  承办人
var principaler = '', undertaker = '';
$.ajax({
    url: getRootPath() + '/userController/vagueSearch?searchSize=8',
    async: false,
    data: {},
    success: function (data) {
        /*for (var i = 0; i < data.length; i++) {
            users.push({
                userName: data[i].userName,
                fid: data[i].fid,
                deptId: data[i].deptId,
                deptName: data[i].deptName
            });
        }
        ;*/
        principaler = formatData(data, "fid");
        undertaker = formatData(data, "fid");
    }
});
var principalerName = $('#principalerName').fool('dhxComboGrid', {
    required: true,
    novalidate: true,
    width: 182,
    height: 30,
    focusShow: true,
    data: principaler,
    hasDownArrow: false,
    filterUrl: getRootPath() + '/userController/vagueSearch?searchSize=8',
    searchKey: "searchKey",
    setTemplate: {
        input: "#userName#",
        columns: [
            {option: '#userCode#', header: '编号', width: 100},
            {option: '#jobNumber#', header: '工号', width: 100, searchKey: false},
            {option: '#userName#', header: '名称', width: 100},
            {option: '#phoneOne#', header: '电话', width: 100, searchKey: false},
        ],
    },
    toolsBar: {
        name: "责任人",
        addUrl: "/userController/userMessageUI",
        refresh: true
    },
    onLoadSuccess: function (combo) {
        mainTable.principalerName = $('#principalerName').val();
    },
    onChange: function (value, text) {
        var row = principalerName.getSelectedText();//获取选中行数据
        $("#principalerId").val(row.fid);
        $("#principalerName").val(row.userName).focus();
    },
});

var undertakerName = $('#undertakerName').fool('dhxComboGrid', {
    required: true,
    novalidate: true,
    width: 182,
    height: 30,
    focusShow: true,
    data: undertaker,
    hasDownArrow: false,
    filterUrl: getRootPath() + '/userController/vagueSearch?searchSize=8',
    searchKey: "searchKey",
    setTemplate: {
        input: "#userName#",
        columns: [
            {option: '#userCode#', header: '编号', width: 100},
            {option: '#jobNumber#', header: '工号', width: 100, searchKey: false},
            {option: '#userName#', header: '名称', width: 100},
            {option: '#phoneOne#', header: '电话', width: 100, searchKey: false},
        ],
    },
    toolsBar: {
        name: "承办人",
        addUrl: "/userController/userMessageUI",
        refresh: true
    },
    onLoadSuccess: function (combo) {
        mainTable.undertakerName = $('#undertakerName').val();
    },
    onChange: function (value, text) {
        var row = undertakerName.getSelectedText();//获取选中行数据
        $("#undertakerId").val(row.fid);
        $("#undertakerName").val(row.userName).focus();
    },
});
//计划编码
$('#code').validatebox({required: true, novalidate: true, validType: 'length[1,50]'});
//预计完成天数
$.extend($.fn.validatebox.defaults.rules, {
    daysValid: {
        validator: function (value, param) {
            return value >= param[0];
        },
        message: '预计完成天数必须大于或等于{0}天'
    },
    listDaysValid: {
        validator: function (value, param) {
            return parseInt(value) + parseInt(getTreeEditor(param[0], 'preDays').numberbox('getValue')) <= parseInt(param[1]);
        },
        message: '预计完成天数与前置天数的和必须小于或等于父级事件的预计完成天数{1}天'
    }
});
$('#modelList').treegrid({
    fitColumns: true,
    idField: 'fid',
    treeField: 'taskName',
    singleSelect: true,
    sortName: "number",
    sortOrder: "asc",
    columns: [[
        {field: "fid", title: 'fid', hidden: true, editor: {type: "text"}},
        {field: "deptId", title: 'deptId', hidden: true, editor: {type: "text"}},
        {field: "parentId", title: 'parentId', hidden: true, editor: {type: "text"}},
        {field: "billId", title: 'billId', hidden: true, editor: {type: "text"}},
        {field: "newRow", title: "newRow", hidden: true, editor: {type: "text"}},
        {field: "mytips", title: "mytips", hidden: true},
        {field: "principalId", title: 'principalId', hidden: true, editor: {type: "text"}},
        {field: "undertakerId", title: 'undertakerId', hidden: true, editor: {type: "text"}},
        {field: 'securityLevelId', title: 'securityLevelId', hidden: true, editor: {type: "text"}},
        {field: 'taskLevelId', title: 'taskLevelId', hidden: true, editor: {type: "textbox"}},
        {field: "planTemplateId", title: 'planTemplateId', hidden: true, editor: {type: "text"}},
        {
            field: "taskName", title: '事件名称', width: 20, editor: {
            type: "textbox",
            options: {
                height: '80%',
                required: true,
                novalidate: true,
                validType: 'length[1,100]',
                missingMessage: "该输入项为必输项"
            }
        }
        },
        {
            field: "number",
            title: '序号',
            width: 5,
            editor: {type: "numberbox", options: {height: '80%', required: true, novalidate: true}}
        },
        {
            field: "days",
            title: '预计完成天数',
            width: 12,
            editor: {
                type: "numberbox",
                options: {height: '80%', required: true, validType: "accessoryNumber", novalidate: true}
            }
        },
        {
            field: "preDays",
            title: '准备天数',
            width: 10,
            editor: {
                type: "numberbox",
                options: {height: '80%', required: true, validType: "accessoryNumber", novalidate: true}
            }
        },
        {
            field: "taskLevelName", title: '事件级别', width: 10, editor: {
            /*type:"combobox",
             options:{
             height:'80%',
             required:true,
             novalidate:true,
             url:getRootPath()+"/flow/taskLevel/queryAll",
             editable:false,
             valueField:'fid',
             textField: 'name',
             panelHeight:'auto',
             onSelect:function(record){
             var id = $(this).closest('.datagrid-row').attr('node-id');
             getTreeEditor(id,'taskLevelId').val(record.fid);
             },onLoadSuccess:function(){//事件级别提示
             $(".datagrid-cell-c3-taskLevelName").find('.easyui-fluid').mouseover(function(){
             tooltip(this);
             });
             }}*/
            type: "text"
        }
        },
        {
            field: "securityLevelName", title: '保密级别', width: 10, editor: {
            /*type:"combobox",
             options:{
             height:'80%',
             required:true,
             novalidate:true,
             url:getRootPath()+"/flow/security/queryAll",
             editable:false,
             valueField:'fid',
             textField: 'name',
             panelHeight:'auto',
             onSelect:function(record){
             var id = $(this).closest('.datagrid-row').attr('node-id');
             getTreeEditor(id,'securityLevelId').val(record.fid);
             },onLoadSuccess:function(){//保密级别提示
             $(".datagrid-cell-c3-securityLevelName").find('.easyui-fluid').mouseover(function(){
             tooltipbm(this);
             });
             }}*/
            type: "text"
        }
        },
        {field: "principalName", title: '责任人', width: 10, editor: {type: "text"}},
        {
            field: "deptName",
            title: '责任部门',
            width: 10
            /*editor: {type: "textbox", options: {height: '80%', disabled: 'disabled'}}*/
        },
        {field: "undertakerName", title: '承办人', width: 10, editor: {type: "text"}},
        {
            field: "amountType", title: '金额类型', width: 12, formatter: amounType, editor: {
            type: "combobox", options: {
                height: '80%', required: true, novalidate: true, editable: false, textField: 'text',
                data: [
                    /*{
                     value: '0',
                     text: '固定值'
                     },*/{
                        value: '1',
                        text: '比例值'
                    }/*,{
                     value: '2',
                     text: '余下金额'
                     }*/
                ],
                onSelect: function (record) {
                    if (record.value == 1) {//金额类型为1，金额限制值为负一百到一百之间
                        getTreeEditor(IDindex, 'amount').numberbox({
                            height: '80%',
                            required: true,
                            precision: 2,
                            novalidate: true,
                            min: -100,
                            max: 100,
                        });
                    } else if (record.value == 2) {//金额类型为2，金额设置为灰，不用填
                        getTreeEditor(IDindex, 'amount').numberbox('setValue', 0);
                        getTreeEditor(IDindex, 'amount').numberbox('disable');
                    } else {//金额类型为0，金额不做限制
                        getTreeEditor(IDindex, 'amount').numberbox('enable');
                        getTreeEditor(IDindex, 'amount').numberbox({
                            height: '80%',
                            required: true,
                            precision: 2,
                            novalidate: true,
                            min: null,
                            max: null
                        });
                    }
                },
                //目前只有一个“比例值”，所以先全部限制了
                onChange:function () {
                    getTreeEditor(IDindex, 'amount').numberbox({
                        height: '80%',
                        required: true,
                        precision: 2,
                        novalidate: true,
                        min: -100,
                        max: 100,
                    });
                }
            }
        }
        },
        {
            field: "amount",
            title: '金额(%)',
            width: 10,
            editor: {
                type: "numberbox",
                options: {height: '80%', required: true, novalidate: true, validType: ['scale[-100,100]','balanceAmount']},
            },
        },
        {
            field: "describe",
            title: '备注',
            width: 21,
            editor: {type: "textbox", options: {height: '80%', novalidate: true, validType: 'maxLength[200]'}},
            iconCls: "icon-sum"
        },
        {
            field: "action", title: '操作', width: 8, formatter: function (value, row) {
            return changeBtn(false, row.fid);
        }
        },
    ]],
    onBeforeSelect: function (row) {
        if ($(this).parent().find('[node-id=' + row.fid + ']').attr('class').search(/datagrid-row-selected/) != -1) {
            $('#modelList').treegrid('unselect', row.fid);
            return false;
        }
    },
    onBeforeLoad: function () {
        $('.datagrid-header-row .datagrid-cell').each(function () {
            $(this).find('span').removeClass('datagrid-sort-icon');
        });
    },
    onLoadSuccess: function () {
        if ($('#status').val() == '启用') {
            hideBtn(true);
            $('#planform').find('input').attr('disabled', 'disabled');
            taskLevelId.disable();
            securityLevelId.disable();
            $('#mysave').hide();
        }
        ;
        var dt = $('.datagrid-body [field="describe"]');
        for (var i = 0; i < dt.length; i++) {
            $(dt[i]).mouseenter(function () {
                if ($(this).text()) {
                    tooltipdt($(this), $(this).text())
                }
            })
        }
    }
});

var myid = 1;
$('#addRow').click(function () {
    var node = $('#modelList').treegrid('getSelected');
    if (node == null) {
        $('#modelList').treegrid('append', {
            data: [{
                fid: myid,
                newRow: true,
            }]
        });
    } else {
        var id = node.fid;
        $('#modelList').treegrid('append', {
            parent: id,
            data: [{
                fid: myid,
                newRow: true,
            }]
        });
    }
    $('#modelList').treegrid('getPanel').find('[node-id=' + myid + '] [field=action] .btn-edit').click();
    myid++;
});

var hasCheckedId = '';
var srcurl = getRootPath() + "/resources/js/lib/easyui/themes/icons/help.png";
var helpIcon = $('td[field="preDays"]').find('span').eq(1);
helpIcon.css({
    'background': 'url(' + srcurl + ') no-repeat',
    'display': 'inline-block',
    'width': '16px',
    'height': '16px',
    'cursor': 'pointer'
});
$('#taskTable').jqGrid({
    datatype: function (postdata) {
        $.ajax({
            url: getRootPath() + '/flow/taskTemplate/query',
            data: postdata,
            dataType: "json",
            complete: function (data, stat) {
                if (stat == "success") {
                    data.responseJSON.totalpages = Math.ceil(data.responseJSON.total / postdata.rows);
                    $("#taskTable")[0].addJSONData(data.responseJSON);
                }
            }
        });
    },
    height: $(window).outerHeight() - 200 + "px",
    pager: "#taskPager",
    rowNum: 10,
    rowList: [10, 20, 30],
    viewrecords: true,
    jsonReader: {
        records: "total",
        total: "totalpages",
    },
    colModel: [
        {
            name: 'fid',
            label: '选项',
            align: 'center',
            sortable: false,
            width: 80,
            formatter: function (cellvalue, options, rowObject) {
                return '<input type="checkbox" value="' + cellvalue + '" class="taskCB" id="cb' + options.rowId + '"/>';
            }
        },
        {name: 'taskLevelId', label: 'taskLevelId', hidden: true},
        {name: 'taskTypeId', label: 'taskTypeId', hidden: true},
        {name: 'code', label: '编号', sortable: true, align: 'center', width: 100},
        {name: 'name', label: '名称', sortable: true, align: 'center', width: 100},
        {name: 'taskTypeName', label: '事件类别', align: 'center', width: 100},
        {name: 'endDays', label: '预计完成天数', align: 'center', width: 100},
        {name: 'taskLevelName', label: '事件级别', align: 'center', width: 100},
        {name: 'creatorName', label: '创建人', align: 'center', width: 100},
        {name: 'createTime', label: '创建时间', align: 'center', width: 100},
        {name: 'describe', label: '描述', align: 'center', width: 100}
    ],
    onSelectRow: function (rowId, status, e) {
        if (hasCheckedId != '' && hasCheckedId != rowId) {
            $('#cb' + hasCheckedId).removeProp('checked');
        }
        $('#cb' + rowId).prop('checked', true);
        hasCheckedId = rowId;
    }
});
//通过checkbox的id获得rowId
function reRowId(cbId) {
    return cbId.substring(2);
}
$('#winPreDays').window({
    width: 700,
    height: 400,
    title: '准备天数',
    modal: true,
    closed: true
});
$('#winTaskName').window({
    width: $('#gbox_taskTable').width() + 22,
    height: 500,
    title: '事件模板',
    modal: true,
    closed: true
});

$('td[field="preDays"]').click(function () {
    $('#winPreDays').window('open');
    $('#predayImg').attr('src', getRootPath() + "/resources/images/index/preDays.png");
});
$('#checkTask').click(function () {
    $('#winTaskName').window('open');
    return $('.taskCB').each(function () {
        $(this).click(function (e) {
            var rowId = reRowId($(this).attr('id'));

            if (hasCheckedId != '' && hasCheckedId != rowId) {
                $('#cb' + hasCheckedId).removeProp('checked');
            }
            $('#cb' + rowId).prop('checked', true);
            hasCheckedId = rowId;
        })
    })
});
$('#select-ok').bind('click', selectOK);
$('#go-search').click(function () {
    var para = $('#taskSeach').val();
    var options = {"searchKey": para};
    $('#taskTable').jqGrid('setGridParam', {postData: options}).trigger("reloadGrid");
})
$('#go-clear').click(function () {
    $('#taskSeach').val("");
    var options = {"searchKey": ""};
    $('#taskTable').jqGrid('setGridParam', {postData: options}).trigger("reloadGrid");
})

$('#winTaskName').keydown(function (e) {
    if (e.keyCode == 13) {
        $('#go-search').click();
    }
})
function selectOK() {
    var rowData = $('#taskTable').getRowData(hasCheckedId);
    $('#winTaskName').window('close');
    if ($('#addRow').css('display') != 'none') {
        $('#addRow').click();
    }
    var id = $('td[field="fid"] input').val();
    getTreeEditor(id, 'taskName').textbox('setText', rowData.name);
    getTreeEditor(id, 'days').numberbox('setValue', rowData.endDays);
    (getTreeEditor(id, 'taskLevelName').next())[0].comboObj.setComboText(rowData.taskLevelName);
    getTreeEditor(id, 'taskLevelId').textbox('setValue',rowData.taskLevelId);
}

var events = [//金额类型
    {id: '0', text: '固定值'},
    {id: '1', text: '比例值'},
    {id: '2', text: '余下金额'}
];
function amounType(value, options, row) {
    for (var i = 0; i < events.length; i++) {
        if (events[i].id == value) return events[i].text;
        if (value == '' || value == null || value == "undefined ") {
            return '';
        }
    }
    return value;
}
if (_details != '') {
    var data = $.parseJSON(_details);
    data = data[0].children;
    $('#modelList').treegrid('loadData', data);
    _days = getMaxdayValue();
}
//预计完成天数
$('#days').validatebox({required: true, novalidate: true, validType: ['accessoryNumber', 'daysValid[' + _days + ']']});

$('.backBtn').click(function () {
    var m = $('#planform').serializeJson();
    delete m.principalerName_new_value;
    delete m.undertakerName_new_value;
    delete m.securityLevelId_new_value;
    delete m.taskLevelId_new_value;
    if ($('#mysave').css('display') == 'none' || (equals(mainTable, m) && isEqul))
        return $('#addBox').window('close');
    var showmsg = "您的计划模板还未保存，是否进行保存?";
    $.fool.confirm({
        title: "保存提示", msg: showmsg, fn: function (data) {
            dataDispose(data);
            if (data) {
                $('#mysave').click();
            } else {
                if (clearFlag == 1) {
                    $.post(getRootPath() + '/flow/planTemplate/delete?id=' + $("#fid").val(), {}, function (data) {
                        $('#planList').trigger('reloadGrid');
                    });
                }
                openAdder = "";
                templateType = "";
                dataId = "";
                tempId = "";
                clearFlag = "";
                $('#addBox').window('close');
            }
        }
    });
});

function equals(x, y) {
    var in1 = x instanceof Object;
    var in2 = y instanceof Object;
    if (!in1 || !in2) {
        return x === y;
    }
    if (Object.keys(x).length !== Object.keys(y).length) {
        return false;
    }
    for (var p in x) {
        var a = x[p] instanceof Object;
        var b = y[p] instanceof Object;
        if (a && b) {
            return equals(x[p], y[p]);
        }
        else if (x[p] !== y[p]) {
            return false;
        }
    }
    return true;
}
//新增、编辑保存
function saveData() {
    $(document).unbind('keydown', plankd);
    $('#mysave').attr('disabled', 'disabled');
    $('#planform').form('enableValidation');
    if (!$('#planform').form('validate')) {
        $('#mysave').removeAttr('disabled');
        $(document).unbind('keydown', plankd);
        $(document).bind('keydown', plankd);
        return false;
    }
    var formdata = $('#modelList').treegrid('getData');
    if (formdata.length == 0) {
        $.fool.alert({
            msg: '事件明细不能为空，请添加事件明细！', fn: function () {
                $('#mysave').removeAttr('disabled');
                $(document).unbind('keydown', plankd);
                $(document).bind('keydown', plankd);
            }
        });
        return false;
    }
    var _dataPanel = $('#modelList').treegrid('getPanel');
    var _editing = _dataPanel.find(".btn-save");
    if (_editing.length > 0) {
        $.fool.alert({
            msg: '你还有没编辑完成的明细,请先确认！', fn: function () {
                $('#mysave').removeAttr('disabled');
                $(document).unbind('keydown', plankd);
                $(document).bind('keydown', plankd);
            }
        });
        return false;
    }
    var mydata = $('#planform').serializeJson();
    var details = $('#modelList').treegrid('getData');
    mydata.templateType = templateType;
    mydata.dataId = dataId;
    mydata = $.extend(mydata, {"details": JSON.stringify(details)});
    var url = getRootPath() + '/flow/planTemplate/save';
    $.post(url, mydata, function (data) {
        // dataDispose(data);
        if (data.returnCode == 0) {
            $.fool.alert({
                time: 1000, msg: "保存成功！", fn: function () {
                    if (dhxkey == 1) {
                        selectTab(dhxname, dhxtab);
                    }
                    if (openAdder == 1) {
                        parent.$('#tabs').tabs('select', "成本分析");
                        var tab = parent.$('#tabs').tabs('getSelected');
                        var index = parent.$('#tabs').tabs('getTabIndex', tab);
                        parent.$('#listTab li').eq(index - 1).css('background', '#495970').siblings().css('background', '#53637a');
                        parent.$('.cathet ul li').css('background', '#53637a');
                    }
                    openAdder = "";
                    templateType = "";
                    dataId = "";
                    tempId = "";
                    clearFlag = "";
                    $('#select-ok').unbind('click', selectOK)
                    $('#addBox').window('close');
                    $('#planList').trigger('reloadGrid');
                }
            });
        } else if (data.returnCode == 1) {
            $.fool.alert({
                msg: data.message, fn: function () {
                    $('#mysave').removeAttr('disabled');
                    $(document).unbind('keydown', plankd);
                    $(document).bind('keydown', plankd);
                    return false;
                }
            });
        } else {
            $.fool.alert({
                msg: "服务器繁忙，请稍后再试", fn: function () {
                    $('#mysave').removeAttr('disabled');
                    $(document).unbind('keydown', plankd);
                    $(document).bind('keydown', plankd);
                    return false;
                }
            });
        }
    });
};
function changeBtn(bool, id) {
    var e = '<a href="javascript:;" class="btn-edit" onclick="editRow(\'' + id + '\')" title="修改"></a>';
    var d = '<a href="javascript:;" class="btn-del" onclick="delRow(\'' + id + '\')" title="删除"></a>';
    var c = '<a href="javascript:;" class="btn-cancel" onclick="cancelRow(\'' + id + '\')" title="撤销"></a>';
    var s = '<a href="javascript:;" class="btn-save" onclick="saveRow(\'' + id + '\')" title="保存"></a>';
    if (bool) {
        return s + c;
    } else {
        return e + d;
    }
}

//
function getTreeEditor(id, field) {
    var target = $($('#modelList').treegrid('getEditor', {id: id, field: field}).target);
    return target;
}
function getTreeTarget(id, field) {
    var target = $('#modelList').treegrid('getPanel').find('[node-id=' + id + '] [field=' + field + ']').children();
    return target;
}
var principalCombo, undertakerCombo, securityLevelCombo, taskLevelCombo;
//明细列表操作
function editRow(id, amountType) {
    IDindex = id;
    isEqul = false;
    // subTable=$('#modelList').treegrid('find',id);
    var rowData = $('#modelList').treegrid('getData',id)[0];
    var _principalName = getTreeTarget(id, 'principalName').text();
    var _undertakerName = getTreeTarget(id, 'undertakerName').text();
    var btn = changeBtn(true, id);
    $('#modelList').treegrid('beginEdit', id);

    hideBtn(true);
    if ($('#modelList').treegrid('getParent', id) != null) {
        var days = $('#modelList').treegrid('getParent', id).days;
        getTreeEditor(id, 'days').numberbox({validType: ["accessoryNumber", "listDaysValid['" + id + "','" + days + "']"]});
    }
    var obj = $('#modelList').treegrid('getPanel').find('[node-id=' + id + '] [field=action] .btn-edit');
    obj.parent().html(btn);

    getTreeTarget(id, 'principalName').find('.datagrid-editable-input').attr('id', 'ppN' + id);
    getTreeTarget(id, 'undertakerName').find('.datagrid-editable-input').attr('id', 'utN' + id);
    getTreeTarget(id, 'securityLevelName').find('.datagrid-editable-input').attr('id', 'slN' + id);
    getTreeTarget(id, 'taskLevelName').find('.datagrid-editable-input').attr('id', 'tlN' + id);

    principalCombo = getTreeEditor(id, 'principalName').fool('dhxComboGrid', {
        required: true,
        novalidate: true,
        width: 182,
        height: 30,
        focusShow: true,
        data: principaler,
        hasDownArrow: false,
        filterUrl: getRootPath() + '/userController/vagueSearch?searchSize=8',
        searchKey: "searchKey",
        setTemplate: {
            input: "#userName#",
            columns: [
                {option: '#userCode#', header: '编号', width: 100},
                {option: '#jobNumber#', header: '工号', width: 100, searchKey: false},
                {option: '#userName#', header: '名称', width: 100},
                {option: '#phoneOne#', header: '电话', width: 100, searchKey: false},
            ],
        },
        toolsBar: {
            name: "责任人",
            addUrl: "/userController/userMessageUI",
            refresh: true
        },
        onChange:function(value,text){
        	var row = principalCombo.getSelectedText();
        	if(row == ""){
        		return false;
        	}
        	getTreeEditor(id, 'deptId').val(row.deptId);
        	getTreeTarget(id, 'deptName').text(row.deptName);
            getTreeEditor(id, 'principalId').val(row.fid);
            getTreeEditor(id, 'principalName').val(row.userName);
        }
    });
    undertakerCombo = getTreeEditor(id, 'undertakerName').fool('dhxComboGrid', {
        required: true,
        novalidate: true,
        width: 182,
        height: 30,
        focusShow: true,
        data: undertaker,
        hasDownArrow: false,
        filterUrl: getRootPath() + '/userController/vagueSearch?searchSize=8',
        searchKey: "searchKey",
        setTemplate: {
            input: "#userName#",
            columns: [
                {option: '#userCode#', header: '编号', width: 100},
                {option: '#jobNumber#', header: '工号', width: 100, searchKey: false},
                {option: '#userName#', header: '名称', width: 100},
                {option: '#phoneOne#', header: '电话', width: 100, searchKey: false},
            ],
        },
        toolsBar: {
            name: "承办人",
            addUrl: "/userController/userMessageUI",
            refresh: true
        },
        onChange:function(value,text){
        	var row = undertakerCombo.getSelectedText();
        	if(row == ""){
        		return false;
        	}
        	getTreeEditor(id, 'undertakerId').val(row.fid);
            getTreeEditor(id, 'undertakerName').val(row.userName);
        }
    });

    //保密级别
    securityLevelCombo = getTreeEditor(id, 'securityLevelName').fool("dhxCombo", {
        required: true,
        novalidate: true,
        width: 70,
        height: 32,
        data: securityValue,
        clearOpt: false,
        setTemplate: {
            input: "#name#",
            option: "#name#"
        },
        toolsBar: {
            name: "保密级别",
            addUrl: "/flow/security/manage",
            refresh: true
        },
        editable: false,
        focusShow: true,
        onLoadSuccess: function (combo) {//保密级别提示
            $(".datagrid-cell-c3-securityLevelName").find('.easyui-fluid').mouseover(function () {
                tooltipbm(this);
            });
            combo.setComboText(rowData.securityLevelName);
        },
    });

    //事件级别
    taskLevelCombo = getTreeEditor(id, 'taskLevelName').fool("dhxCombo", {
        required: true,
        novalidate: true,
        width: 70,
        height: 32,
        data: taskLevelValue,
        clearOpt: false,
        setTemplate: {
            input: "#name#",
            option: "#name#"
        },
        toolsBar: {
            name: "事件级别",
            addUrl: "/flow/taskLevel/manage",
            refresh: true
        },
        editable: false,
        focusShow: true,
        onLoadSuccess: function (combo) {//事件级别提示
            $(".datagrid-cell-c3-taskLevelName").find('.easyui-fluid').mouseover(function () {
                tooltip(this);
            });
            combo.setComboText(rowData.taskLevelName);
        }
    });

    if (!getTreeEditor(id, 'newRow').val() || getTreeEditor(id, 'newRow').val() == "false") {
        $('#input_dhxDiv_ppN' + id).val(_principalName);
        $('#input_dhxDiv_utN' + id).val(_undertakerName);
    }else{
        var row = $('#modelList').treegrid('getPanel').find('[node-id=' + id + ']');
        //事件级别
        (getTreeEditor(id, 'taskLevelName').next())[0].comboObj.setComboText("");
        //保密级别
        (getTreeEditor(id, 'securityLevelName').next())[0].comboObj.setComboText("");
        row.form('disableValidation');
    }
    if (getTreeEditor(id, 'amountType').combobox('getValue') == 2) {
        getTreeEditor(id, 'amount').numberbox('disable');
    }
}
function cancelRow(id) {
    var bool = getTreeEditor(id, 'newRow').val();
    if (bool == 'true') {
        $('#modelList').treegrid('remove', id);
    } else {
        $('#modelList').treegrid('cancelEdit', id);
    }
    hideBtn(false);
}
function delRow(id) {
    var showmsg = "";
    var length = $('#modelList').treegrid('getChildren', id).length;
    if (length > 0) {
        // 下面有子节点
        showmsg = "事件下面存在的明细也会删除，确认删除？";
    } else {
        showmsg = "是否删除此事件 ？";
    }
    $.fool.confirm({
        title: "删除提示", msg: showmsg, fn: function (data) {
            dataDispose(data);
            if (data) {
                $('#modelList').treegrid('remove', id);
                $('#days').val(getMaxdayValue());
                if ($('#modelList').treegrid('getData').length == 0) {
                    $('#days').validatebox({
                        required: true,
                        novalidate: true,
                        validType: ['accessoryNumber', 'daysValid[' + 0 + ']']
                    });
                }
            } else {
                return false;
            }
        }
    });

}

function getMaxdayValue() {
    var tree = $('#modelList').treegrid('getChildren');
    var length = tree.length;
    var mydays = 0;
    for (var i = 0; i < length; i++) {
        var treeday = parseInt(tree[i].days) + parseInt(tree[i].preDays);
        if (mydays < treeday) {
            mydays = treeday;
        }
    }
    return mydays;
}
$('#dhxDiv_securityLevelId').mouseover(function (e) {//主表保密级别
    tooltipbm(this);
});
function tooltip(e) {//事件级别
    $(e).tooltip({
        position: 'right',
        content: '<span style="color:#fff">级别用数字表示，数字越大级别越高。<br/>计划和事件中的事件级别用来和消息预<br/>警的任务级别进行比较，任务级别大于<br/>事件级别时，向相关人发送相应消息。</span>',
        onShow: function () {
            $(e).tooltip('tip').css({
                backgroundColor: '#666',
                borderColor: '#666'
            });
        }
    });
}
function tooltipbm(e) {//保密级别
    $(e).tooltip({
        position: 'right',
        content: '<span style="color:#fff">级别用数字表示，数字越大级别越高。<br/>计划和事件中的保密级别用来和人员的<br/>保密级别（系统管理员进行配置）进行<br/>比较，人员的保密级别大于计划和事件<br/>的保密级别时，可以操作本计划和事件。</span>',
        onShow: function () {
            $(e).tooltip('tip').css({
                backgroundColor: '#666',
                borderColor: '#666'
            });
        }
    });
}
function tooltipdt(e, content) {//描述
    $(e).tooltip({
        position: 'top',
        content: '<span style="color:#fff">' + content + '</span>',
        onShow: function () {
            $(e).tooltip('tip').css({
                backgroundColor: '#666',
                borderColor: '#666'
            });
        }
    });
}
function saveRow(id) {
    var btn = changeBtn(false, id);
    var row = $('#modelList').treegrid('getPanel').find('[node-id=' + id + ']');
    row.form('enableValidation');
    /*var flag = false;//判断责任人和承办人是否有效
    for (var i = 0; i < users.length; i++) {
        if ($('#input_dhxDiv_ppN' + id).val() != users[i].userName) {
            continue;
        } else {
            getTreeEditor(id, 'deptId').val(users[i].deptId);
            getTreeEditor(id, 'deptName').textbox('setText', users[i].deptName);
            getTreeEditor(id, 'principalId').val(users[i].fid);
            getTreeEditor(id, 'principalName').val(users[i].userName);
            flag = true;
            break;
        }
    }

    if (!flag) {
        $('#input_dhxDiv_ppN' + id).val('');
        getTreeEditor(id, 'deptName').textbox('setText', '');
        $('#input_dhxDiv_ppN' + id).validatebox('validate');
        return;
    }
    for (var i = 0; i < users.length; i++) {
        if ($('#input_dhxDiv_utN' + id).val() != users[i].userName) {
            flag = false;
            continue;
        } else {
            getTreeEditor(id, 'undertakerId').val(users[i].fid);
            getTreeEditor(id, 'undertakerName').val(users[i].userName);
            flag = true;
            break;
        }
    }
    if (!flag) {
        $('#input_dhxDiv_utN' + id).val('');
        $('#input_dhxDiv_utN' + id).validatebox('validate');
        return;
    }*/
    if (!row.form('validate')) {
        return false;
    }

    var deptName = getTreeTarget(id, 'deptName').text();
    var principalName = getTreeEditor(id, 'principalName').next()[0].comboObj.getComboText();
    var undertakerName = getTreeEditor(id, 'undertakerName').next()[0].comboObj.getComboText();
    var taskLevelName = (getTreeEditor(id, 'taskLevelName').next())[0].comboObj.getComboText();
    var taskLevelId = getTreeEditor(id, 'taskLevelId').val();
    if(!taskLevelId){
        taskLevelId = (getTreeEditor(id, 'taskLevelName').next())[0].comboObj.getSelectedValue();
    }
    var securityLevelName = (getTreeEditor(id, 'securityLevelName').next())[0].comboObj.getComboText();
    var securityLevelId = getTreeEditor(id, 'securityLevelId').val();
    if(!securityLevelId){
        securityLevelId = (getTreeEditor(id, 'securityLevelName').next())[0].comboObj.getSelectedValue();
    }
    var amountType = getTreeEditor(id, 'amountType').combobox('getValue');
    $('#modelList').treegrid('endEdit', id);
    _days = getMaxdayValue();
    $('#days').val(_days);
    $('#days').validatebox({
        required: true,
        novalidate: true,
        validType: ['accessoryNumber', 'daysValid[' + _days + ']']
    });
    $('#modelList').treegrid('update', {
        id: id,
        row: {
            "newRow": false,
            "securityLevelId": securityLevelId,
            "taskLevelId": taskLevelId,
            "taskLevelName": taskLevelName,
            "deptName": deptName,
            "principalName": principalName,
            "undertakerName": undertakerName,
            "securityLevelName": securityLevelName,
            "amountType": amountType,
        }
    });
    var obj = $('#modelList').treegrid('getPanel').find('[node-id=' + id + '] [field=action] .btn-save');
    obj.parent().html(btn);
    hideBtn(false);
    var dt = $('.datagrid-body [field="describe"]');
    for (var i = 0; i < dt.length; i++) {
        $(dt[i]).mouseenter(function () {
            if ($(this).text()) {
                tooltipdt($(this), $(this).text())
            }
        })
    }
}
//表格表头跟随滚动
var offset = $('.list1 .datagrid-view2 .datagrid-header').offset();
$window = $('.list1').closest('.window-body');
$window.scroll(function () {
    var scroll = $(this).scrollTop();
    if (offset.top <= (scroll + 54)) {
        $('.list1 .datagrid-view2 .datagrid-header').addClass('fixed');
        $('.list1 .datagrid-view2 .datagrid-body').css('padding-top', "35px");
        $('#addRow').insertAfter('.title .title1').removeClass('beforebtn').addClass('afterbtn');
    } else {
        $('.list1 .datagrid-view2 .datagrid-header').removeClass('fixed');
        $('.list1 .datagrid-view2 .datagrid-body').css('padding-top', 0);
        $('#addRow').appendTo('.actionBtn').removeClass('afterbtn').addClass('beforebtn');
        ;
    }
});
enterController('planform');