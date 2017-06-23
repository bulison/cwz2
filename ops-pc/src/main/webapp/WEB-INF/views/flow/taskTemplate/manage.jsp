<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<c:set var="billCode" value="sjmb" scope="page"></c:set>
<c:set var="billCodeName" value="事件模版" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>${billCodeName}</title>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <link rel="stylesheet" href="${ctx}/resources/css/eventManagement.css"/>
    <script type="text/javascript" src="${ctx}/resources/js/eventManagement.js?v=${js_v}"></script>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>事件模版</h1>
    </div>
</div>
<div class="btn">
    <a href="javascript:;" id="add" class="btn-ora-add">新增</a>
    <!-- <a href="javascript:;" id="getIn" class="btn-ora-import" >导入</a>
    <a href="javascript:;" id="getOut" class="btn-ora-export">导出</a>
    <a href="javascript:;" id="print" class="btn-ora-printer">打印</a> -->
</div>
<div class="form_input">
    <form>
        <input name="codeOrVoucherCode" class="easyui-textbox" id="search-code"
               data-options="{prompt:'编号或名称',	width:160,height:30,searcher:function(value,name){refreshData()}}"/>
    </form>
    <a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
</div>
<table id="dataTable"></table>
<div id="pager"></div>
<%--  <table id="dataTable" class="easyui-datagrid" style="width:100%;" data-options="url:'${ctx}/flow/taskTemplate/query',fitColumns:true,pagination:true,singleSelect:true">
   <thead>
      <tr>
       <th data-options="field:'fid',width:100,resizable:true,hidden:true,">fid</th>
       <th data-options="field:'code',width:100,resizable:true,formatter:examine">编号</th>
       <th data-options="field:'name',width:100,resizable:true">名称</th>
       <th data-options="field:'taskTypeName',width:100,resizable:true">事件类别</th>
       <th data-options="field:'endDays',width:100,resizable:true">预计完成天数</th>
       <th data-options="field:'taskLevelName',width:100,resizable:true">事件级别</th>
       <th data-options="field:'creatorName',width:100,resizable:true">创建人</th>
       <th data-options="field:'createTime',width:100,resizable:true">创建时间</th>
       <th data-options="field:'describe',width:100,resizable:true">描述</th>
       <th data-options="field:'action',width:100,resizable:true,formatter:action">操作</th>
      </tr>
    </thead>
 </table> --%>
<div id="win"></div>
<div id="importBox"></div> <!--导入 -->
<!-- <table id="datalist2"></table> -->
<script type="text/javascript">
    var dhxkey = "${param.dhxkey}";
    var dhxname = "${param.dhxname}";
    var dhxtab = "${param.dhxtab}";
    initManage('${billCode}', '${billCodeName}');
    //初始化查询框
    $("#search-code").textbox({
        prompt: '编号或名称',
        width: 160,
        height: 32
    });
    var listData = "";
    $('#dataTable').jqGrid({
        datatype: function (postdata) {
            $.ajax({
                url: '${ctx}/flow/taskTemplate/query',
                data: postdata,
                dataType: "json",
                complete: function (data, stat) {
                    if (stat == "success") {
                        listData = data.responseJSON;
                        listData.totalpages = Math.ceil(listData.total / postdata.rows);
                        $("#dataTable")[0].addJSONData(listData);
                    }
                }
            });
        },
        autowidth: true,
        height: $(window).outerHeight() - 200 + "px",
        pager: "#pager",
        rowNum: 10,
        rowList: [10, 20, 30],
        viewrecords: true,
        jsonReader: {
            records: "total",
            total: "totalpages",
        },
        colModel: [
            {name: 'fid', label: 'fid', hidden: true, width: 100, align: 'center'},
            {name: 'isNew', label: 'isNew', hidden: true},
            {name: 'editing', label: 'editing', hidden: true},
            {name:'taskTypeId',label:'taskTypeId',hidden:true},
            {name:'taskLevelId',label:'taskLevelId',hidden:true},
            {name:'updateTime',label:'updateTime',hidden:true},
            {name: 'code', label: '编号', sortable: true, width: 100, align: 'center', editable:true,edittype:'text'},
            {name: 'name', label: '名称', sortable: true, width: 100, align: 'center',editable:true,edittype:'text'},
            {name: 'taskTypeName', label: '事件类别', width: 100, align: 'center',editable:true,edittype:'text'},
            {name: 'endDays', label: '预计完成天数', width: 100, align: 'center',editable:true,edittype:'text'},
            {name: 'taskLevelName', label: '事件级别', width: 100, align: 'center',editable:true,edittype:'text'},
            {name: 'creatorName', label: '创建人', width: 100, align: 'center'},
            {name: 'createTime', label: '创建时间', width: 100, align: 'center'},
            {name: 'describe', label: '描述', width: 100, align: 'center',editable:true,edittype:'text',},
            {name: 'action', label: '操作', width: 30, width: 60, align: 'center', formatter: action}
        ],
    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false});

/*    function examine(value, options, row) {
        var e = '<a href="javascript:;" onclick="editById(this,\'' + options.fid + '\',1)">' + value + '</a>';
        return e;
    }*/

    function action(value, options, row) {
        var e = '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\'' + options.rowId + '\')"></a>';
        var r = "<a href='javascript:;' class='btn-del' onclick='removeById(\"" + options.rowId + "\")' title='删除'></a>";
        var s = "<a href='javascript:;' class='btn-save' onclick='save(\""+ options.rowId + "\")' title='保存'></a>";
        var c = "<a href = 'javascript:;' class='btn-cancel' onclick='cancel(\""+ options.rowId + "\")' title = '取消'></a>"
         if(row.editing){
            return s + c;
        }else{
            return e + r;
        };
    }

    //编辑
    function editById(rowid) {
        /*		$('#win').window({
         title:'修改事件模版',
         top:80,
         collapsible:false,
         minimizable:false,
         maximizable:false,
         resizable:false,
         href:"
        ${ctx}/flow/taskTemplate/edit?mark=1&id="+fid,
         width:656,
         });*/

        var rowData = $("#dataTable").jqGrid("getRowData",rowid);
/*        var rowEdit = $("table#dataTable tr[editable='1']");
        if(rowEdit.length > 0 ){
            $.fool.alert({
                msg: '请先保存本行！', time: 1000
            });
            return false;
        }
        if(rowData.editing == "true"){
            return;
        }*/

        rowData.editing = true;
        rowData.action = null;
        $("#dataTable").jqGrid("setRowData",rowid,rowData);
        $("#dataTable").jqGrid("editRow",rowid);


        //编号
        getEditor("dataTable",rowid,"code").textbox({//获取编辑状态下的下拉框
            width:160,
            height:32,
            required:true,
            novalidate:true,
            validType:'length[1,50]',
            missingMessage:'该项不能为空！',
        });

        //模版名称
        getEditor("dataTable",rowid,"name").textbox({
            width:160,
            height:32,
            required:true,
            novalidate:true,
            validType:'length[1,100]',
            missingMessage:'该项不能为空！',
        });

        //事件类别
        var etaskTypeIdValue='';
        $.ajax({
            url:"${ctx}/flow/taskType/queryAll?num="+Math.random(),
            async:false,
            success:function (data) {
                etaskTypeIdValue = formatData(data,'fid');
            }
        });

        getEditor("dataTable",rowid,"taskTypeName").fool("dhxCombo",{
            width:135,
            height:32,
            data:etaskTypeIdValue,
            setTemplate:{
                input:'#name#',
                option:'#name#'
            },
            toolsBar:{
                name:'事件类别',
                addUrl:'/flow/taskType/manage',
                refresh:true
            },
            required:true,
            focusShow:true,
            clearOpt:false,
            editable:false,
            novalidate:true,
            onLoadSuccess:function (combo) {
                combo.setComboValue(rowData.taskTypeId);
            }
        });

        var taskLeveValue='';	//事件级别
        $.ajax({
            url:"${ctx}/flow/taskLevel/queryAll?num="+Math.random(),
            async:false,
            success:function(data){
                taskLeveValue=formatData(data,'fid');
            }
        });
        getEditor("dataTable",rowid,"taskLevelName").fool("dhxCombo",{
            width:135,
            height:32,
            data:taskLeveValue,
            setTemplate:{
                input:"#name#",
                option:"#name#"
            },
            toolsBar:{
                name:"事件级别",
                addUrl:"/flow/taskLevel/manage",
                refresh:true
            },
            focusShow:true,
            editable:false,
            clearOpt:false,
            required:true,
            novalidate:true,
            onLoadSuccess:function(combo){
                combo.setComboValue(rowData.taskLevelId);
            }
        });

        //预计完成天数
        getEditor("dataTable",rowid,"endDays").numberbox({
            width:160,
            height:32,
            novalidate:true,
            required:true,
            validType:'length[1,50]',
            missingMessage:'该项不能为空！'
        });

        //描述
        getEditor("dataTable",rowid,"describe").textbox({
            width:160,
            height:32,
            novalidate:true,
            validType:'length[1,200]',
        })

    }

    //新增
    $('#add').click(function () {
        /*$('#win').window({
            title: '新增事件模版',
            top: 55,
            modal: true,
            collapsible: false,
            minimizable: false,
            maximizable: false,
            resizable: false,
            href: '${ctx}/flow/taskTemplate/edit',
            width: 656,
            onBeforeClose: function () {
            },
            onClose: function () {
            }
        });*/

/*        var rowEdit = $("table#dataTable tr[editable = '1']");
        var oldLength = listData.rows.length;
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
        }*/

        var dataRow = {
            isNew:true,
            code:"",
            name:"",
            taskTypeName:"",
            taskLevelName:"",
            endDays:"",
            describe:""
        };
        var ids = $("#dataTable").jqGrid("getDataIDs");
        var index = Math.max.apply(null,ids)+1;
        if (index == "-Infinity"){
            index = "1";
        }
        $("#dataTable").jqGrid("addRowData",index,dataRow,"first");
        editById(index);

    });

    //取消
    function  cancel(index) {
        var rowData = $("#dataTable").jqGrid("getRowData",index);
        $("#dataTable").jqGrid("restoreRow",index);
        if(rowData.isNew){
            $("#dataTable").jqGrid("delRowData",index);
        }else{
            $("#dataTable").jqGrid("setRowData",index,{editing:false,action:null});
        }
    }

    //删除
    function removeById(rowid) {
        var rowData = $("#dataTable").jqGrid("getRowData",rowid);
        $.fool.confirm({
            title: '确认', msg: '确定要删除选中的记录吗？', fn: function (r) {
                if (r) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/flow/taskTemplate/delete?id=' + rowData.fid,
                        data: {fid: rowData.fid},
                        dataType: 'json',
                        success: function (data) {
                            dataDispose(data);
                            if (data.returnCode == '0') {
                                $.fool.alert({
                                    time: 1000, msg: '删除成功！', fn: function () {
                                    $("#dataTable").trigger("reloadGrid");
                                    }
                                });
                            } else if (data.returnCode == '1') {
                                $.fool.alert({msg: data.message});
                            } else {
                                $.fool.alert({time: 1000, msg: "系统正忙，请稍后再试。"});
                                $('#save').removeAttr("disabled");
                            }
                        }
                    });
                }
            }
        });
    }
    //查询
    $('.Inquiry').click(function () {	//事件分类
        var code = $("#search-code").textbox('getValue');
        var options = {"searchKey": code};
        $('#dataTable').jqGrid('setGridParam', {postData: options}).trigger("reloadGrid");
    });
    enterSearch("Inquiry");//回车搜索

    //保存
    function save(index) {
        var rowData = $("#dataTable").jqGrid("getRowData",index);
        var c = getEditor("dataTable",index,"code");
        var n = getEditor("dataTable",index,"name");
        var t = getEditor("dataTable",index,"taskTypeName");
        var l = getEditor("dataTable",index,"taskLevelName");
        var e = getEditor("dataTable",index,"endDays");
        var d = getEditor("dataTable",index,"describe");
        $("#dataTable #"+index).form("enableValidation");//index这一行开启验证
        if(!$("#dataTable #"+index).form("validate")){
            return false;
        }
            if(rowData.isNew){
                var taskTypeId_new_value = true;
                var taskLevelId_new_value = true;
            }else{
                var taskTypeId_new_value = false;
                var taskLevelId_new_value = false;
            }
            var code = $(c[0]).val();
            var name = $(n[0]).val();
            var taskTypeId = ($(t[0]).next())[0].comboObj.getSelectedValue();
            var taskLevelId = ($(l[0]).next())[0].comboObj.getSelectedValue();
            var endDays = $(e[0]).val();
            var describe = $(d[0]).val();
            $.ajax({
                type: "post",
                url: "${ctx}/flow/taskTemplate/save",
                data:{fid:rowData.fid,updateTime:rowData.updateTime,createTime:rowData.createTime,code:code,name:name,taskTypeId:taskTypeId,taskLevelId:taskLevelId,endDays:endDays,describe:describe,taskTypeId_new_value:taskTypeId_new_value,taskLevelId_new_value:taskLevelId_new_value},
                success: function(data){
                    dataDispose(data);
                    if(data.returnCode == '0'){
                        if(dhxkey == 1){
                            selectTab(dhxname,dhxtab);
                        }
                        $.fool.alert({time:1000,msg:'保存成功'});
                        $('#dataTable').jqGrid('saveRow',index);
                        $('#dataTable').jqGrid('setRowData',index,{isNew:"",fid:data.data.fid,updateTime:data.data.updateTime,createTime:data.data.createTime,creatorName:data.data.creatorName,code:code,name:name,taskTypeName:data.data.taskTypeName,taskLevelName:data.data.taskLevelName,endDays:endDays,describe:describe,editing:false,action:null})
                    }else if(data.returnCode == '1'){
                        $.fool.alert({msg:data.message});
                    }
                },error:function(){
                    $.fool.alert({msg:"系统正忙，请稍后再试。"});
                }
            });
    }
</script>
</body>
</html>
