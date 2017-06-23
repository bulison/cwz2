<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>

<body>
<table id="user"></table>
<div id="pager1"></div>
<div style="text-align:center">
    <a id='yes' href="javascript:;" class="btn-blue4 btn-s" style="margin:10px">确定</a>
    <a id='close' href="javascript:;" class="btn-blue4 btn-s" style="margin:10px">关闭</a>
</div>
<script type="text/javascript">
    var checkUserID = "";
    var unCheckUserID = "";
    var option = [
        {id: '0', name: '否'},
        {id: '1', name: '是'},
        {id: '2', name: ''}
    ];
    $('#user').jqGrid({
        datatype: function (postdata) {
            $.ajax({
                url: '${ctx}/userController/userList?orgId=' + '${orgId}',
                data: postdata,
                dataType: "json",
                complete: function (data, stat) {
                    if (stat == "success") {
                        data.responseJSON.totalpages = Math.ceil(data.responseJSON.total / postdata.rows);
                        $("#user")[0].addJSONData(data.responseJSON);
                    }
                }
            });
        },
        onSelectRow: function (rowId, status) {
            var row = $('#user').jqGrid("getRowData", rowId);
            if (!status) {
                unCheckUserID = row.fid + ',' + unCheckUserID;
            }
        },
        autowidth: true,//自动填满宽度
        height: "100%",
        mtype: "post",
        pager: '#pager1',
        rowNum: 10,
        rowList: [10, 20, 30],
        jsonReader: {
            records: "total",
            total: "totalpages",
        },
        viewrecords: true,
        multiselect: true,
        forceFit: true,//调整列宽度，表格总宽度不会改变
        height: "100%",
        colModel: [
            {name: 'userName', label: '姓名', align: "center", sortable: false, editor: "text", width: 100},
            {name: 'email', label: '邮箱', align: "center", sortable: false, editor: "text", width: 100},
            {name: 'phoneOne', label: '电话', align: "center", sortable: false, editor: "text", width: 100},
            {name: 'fax', label: '传真', align: "center", sortable: false, editor: "text", width: 100},
            {
                name: 'isMobileLogin', label: '移动登陆', align: "center", sortable: false, formatter: function (value) {
                    for (var i = 0; i < option.length; i++) {
                        if(value==undefined){
                            value="";
                            break;
                        }
                        if (option[i].id == value){
                            value = option[i].name;
                            break;
                        }
                    }
                    return value;
            },
                width: 100
            },
            {name: 'fid', label: 'fid', hidden: true, width: 100}
        ],
        gridComplete: function () {
            if (showKey == 0) { //修复加载窗口后，自动点击根节点会出现加载2次导致载入选择项失效的问题
                showKey++;
                return;
            }
            $.post("${ctx}/resourceController/getRoleUser", {roleId: "${roleId}"}, function (data) {
                for (var i = 0; i < data.length; i++) {
                    var rowId = $('#user').find("tr.jqgrow td[title=" + data[i] + "]").parent().attr("id");
                    if (rowId) {
                        $("#user").jqGrid('setSelection', rowId);
                    }
                }
            });
        },
    }).navGrid('#pager1', {add: false, del: false, edit: false, search: false, view: false});


    //分页
    //setPager($('#user'));
    /* $.post("${ctx}/resourceController/getRoleUser",{roleId:"${roleId}"},function(data){
     $("#user").jqGrid({
     gridComplete:function(){
     for(var i=0;i<data.length;i++){
     var rowId = $('#user').find("tr.jqgrow td[title="+data[i]+"]").parent().attr("id");
     alert(rowId);
     $("#user").jqGrid('setSelection',rowId);
     }
     },
     });
     }); */


    $("#yes").click(function (e) {
        checkUserID = "";
        var rowIds = $("#user").jqGrid('getGridParam', 'selarrrow');
        //var checkUser=$("#user").jqGrid('getChecked');
        for (var i = 0; i < rowIds.length; i++) {
            var row = $("#user").jqGrid('getRowData', rowIds[i]);
            checkUserID += row.fid + ',';
        }
        $.post("${ctx}/userController/roleToUser", {roleId: "${roleId}", check: checkUserID, unCheck: unCheckUserID});
        //checkResID="";
        unCheckUserID = "";
        $.fool.alert({msg: "保存成功"});
    });
    $("#close").click(function (e) {
        $("#permissions").window('close');
    })
</script>
</body>
