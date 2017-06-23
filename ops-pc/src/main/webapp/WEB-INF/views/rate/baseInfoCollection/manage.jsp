<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <title>基础信息采集</title>
    <style>
        #btn-box {
            margin: 0 0 10px 0
        }
    </style>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>基础信息采集</h1>
    </div>
</div>
<div style="margin-bottom: 10px">
<input id="lossRate" name="loss" type="text" class="textBox"/>
<a href="javascript:;" class="Inquiry btn-blue btn-s " style="margin-left: 5px" onclick="save()">保存</a>
</div>
<%--  <div id="btn-box">
	  <a href="javascript:;" id="add" class="btn-ora-add" onclick="addRow()">新增</a> 
  </div>--%>
<table id="infoGrid"></table>
<%--<div1 id="pager"></div>--%>
</body>
<script type="text/javascript">
    $("#lossRate").textbox({
        prompt: '输入资金日损率',
        width: 160,
        height:32
    });

    $(function () {
        $("#lossRate").textbox("textbox").bind("keydown",function (e) {
            if(e.keyCode == 13){
                save();
            }
        })
    });


    $('#infoGrid').jqGrid({
        datatype: function (postdata) {
            $.ajax({
                url: '${ctx}/api/rate/loanrate/queryLog',
                data: postdata,
                dataType: "json",
                complete: function (data, stat) {
                    if (stat == "success") {
                        //给后台rate加上“%”
                        data.responseJSON.totalpages = Math.ceil(data.responseJSON.total / postdata.rows);
                        for( var i = 0 ; i < data.responseJSON.rows.length ; i++ ){
                            /*var x = data.responseJSON.rows[i].rate;
                            if (x == 0){
                                x = parseFloat(x).toFixed(2);
                            }
                            data.responseJSON.rows[i].rate = parseFloat(data.responseJSON.rows[i].rate).toFixed(2)+"%";
                                if(data.responseJSON.rows[i].rate == "0.00%"){
                                    data.responseJSON.rows[i].rate = x+"%";
                                }*/
                            data.responseJSON.rows[i].rate = data.responseJSON.rows[i].rate + "%";
                        }
                        $("#infoGrid")[0].addJSONData(data.responseJSON);
                    }
                }
            });
        },
        forceFit: true,
//        pager: '#pager',
//        rowList: [10, 20, 30],
        viewrecords: true,
        rowNum: 10,
        jsonReader: {
            records: "total",
            total: "totalpages",
        },
        autowidth: true,//自动填满宽度
        height: $(window).outerHeight() - 200 + "px",
        colModel: [
            {
                name: 'fid',
                label: 'fid',
                align: "center",
                sortable: false,
                hidden: true,
                editable: true,
                edittype: "text"
            },
            {
                name: 'isNew',
                label: 'isNew',
                align: "center",
                sortable: false,
                hidden: true,
                editable: true,
                edittype: "text"
            },
            {
                name: 'increase',
                label: 'increase',
                align: "center",
                sortable: false,
                hidden: true,
                editable: true,
                edittype: "text"
            },
            {
                name: 'createTime',
                label: '创建日期',
                align: "center",
                sortable: false,
                width: 100,
                editable: true,
                edittype: "text"/* editor:{type:"datebox",options:{required:true,missingMessage:"该项必填",editable:false}} */
            },
            {
                name: 'rate',
                label: '资金日损率',
                align: "center",
                sortable: false,
                width: 100,
                editable: true,
                edittype: "text",
                /* editor:{type:"numberbox",options:{required:true,validType:"maxLength[10]",missingMessage:"该项必填",precision:4}} */
            },
            {name: 'creatorName', label: '录入人', align: 'center', sortable: false, width: 100, editable: true, edittype: 'text'}
            /*{name:'_increase',label:'增幅(%)',align:"center",sortable:false,width:100,editable:true,edittype:"text"/!* editor:{type:"numberbox",options:{required:true,validType:"maxLength[10]",missingMessage:"该项必填",precision:2}} *!/,formatter:function(value,options,row){return toHunder(row);}},
             {name:'action',label:'操作',align:"center",sortable:false,width:20,formatter:function(value,options,row){
             var rowStr= JSON.stringify(row);
             if (row.editing){
             var s="<a href='javascript:;' title='保存' class='btn-save' onclick='saverow(this)'></a>";
             var c="<a href='javascript:;' title='取消' class='btn-cancel' onclick='cancelrow(this)'></a>";
             return s+" "+c;
             }else{
             var e='<a href="javascript:;" title="编辑" class="btn-edit" onclick="editrow(this)" ></a>';
             var d='<a href="javascript:;" title="删除" class="btn-del" onclick="deleterow(this,\''+row.fid+'\')" ></a>';
             return e+" "+d;
             }
             }}*/
        ]/* ,
         onBeforeEdit:function(index,row){
         row.editing = true;
         updateActions(index);
         },
         onAfterEdit:function(index,row){
         row.editing = false;
         updateActions(index);
         },
         onCancelEdit:function(index,row){
         row.editing = false;
         updateActions(index);
         } */
    });

    //保存
    function save() {
        var ret = /^(\d|[1-9]\d)(\.\d{1,8})?$/;//小数点最多为8位
        var rate = $("#lossRate").textbox("getValue");
        if(rate>0 && rate<=0.2 && ret.test(rate)){
            $.ajax({
                url:'${ctx}/api/rate/loanrate/save',
                data:{rate:rate},
                type:'PUT',
                dataType:'json',
                success:function (data) {
                    dataDispose(data);//
                    if(data.returnCode == "0"){
                        $.fool.alert({
                            time:1000,msg:'保存成功',fn:function () {
                                $("#lossRate").textbox("setValue","");
                                $('#infoGrid').trigger("reloadGrid");
                            }
                        })
                    }else if(data.returnCode == '1'){
                        $.fool.alert({time:1000,msg:data.message});
                    }else{
                        $.fool.alert({msg:'系统繁忙，请稍后再试。'+data.returnCode});
                    }
                }
            })
        }else {
            $.fool.alert({
                time:2000,msg:"请输入0-0.2之间的数值，最多保留8位小数！",fn:function () {
                    $("#lossRate").textbox("setValue","");
                }
            });
        }
    }

/* else if (data.returnCode == "1") {
                $.fool.alert({time: 1000, msg: data.message});
            } else {
                $.fool.alert({msg: "系统正忙，请稍后再试。" + data.returnCode + "haha"});
            }
        }
    });*/

/*    function addRow() {
        var index = $('#detailsList').jqGrid('getRowData').length + 1;
        $('#infoGrid').jqGrid("addRowData", index, {
            isNew: 1
        }, "first");
        editrow("", index);
    }*/

    /* //更新某行数据
     function updateActions(index){
     $('#infoGrid').datagrid('updateRow',{
     index: index,
     row:{}
     });
     } */

    //获取某行序号
    function getRowIndex(target) {
        var tr = $(target).closest('tr.jqgrow');
        return parseInt(tr.attr('id'));
    }

    //编辑某行
    function editrow(target, index) {
        if (index != null) {
            $('#infoGrid').jqGrid('editRow', index);
        } else {
            index = getRowIndex(target);
            $('#infoGrid').jqGrid('editRow', index);
        }
        $('#infoGrid').jqGrid('setRowData', index, {editing: true, action: null});//编辑状态转换，按钮变化
        getEditor("rate", index).numberbox({
            width: "100%",
            height: "80%",
            required: true,
            validType: "maxLength[10]",
            missingMessage: "该项必填",
            precision: 4
        });
        getEditor("date", index).datebox({
            width: "100%", height: "80%", required: true, missingMessage: "该项必填", editable: false
        });
        getEditor("date", index).datebox('textbox').focus(function () {
            getEditor("date", index).datebox('showPanel');
        });
        getEditor("_increase", index).numberbox({
            width: "100%",
            height: "80%",
            required: true,
            validType: "maxLength[10]",
            missingMessage: "该项必填",
            precision: 2
        });
    }

    //删除某行
    function deleterow(target, fid) {
        $.fool.confirm({
            msg: '确定删除这条记录吗?',
            fn: function (r) {
                if (r) {
                    /*$.post("
                    ${ctx}/rate/loanrate/delete",{fid:fid},function(data){
                     dataDispose(data);
                     if(data.returnCode=="0"){
                     $.fool.alert({time:1000,msg:'删除成功！',fn:function(){
                     $('#infoGrid').jqGrid('delRowData', getRowIndex(target));
                     }});
                     }else if(data.returnCode=="1"){
                     $.fool.alert({msg:data.message});
                     }else{
                     $.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
                     }
                     });*/
                    $.ajax({
                        url: '${ctx}/api/rate/loanrate/delete?fid=' + fid,
                        type: 'DELETE',
                        dataType: "json",
                        success: function (data) {
                            dataDispose(data);
                            if (data.returnCode == "0") {
                                $.fool.alert({
                                    time: 1000, msg: '删除成功！', fn: function () {
                                        $('#infoGrid').jqGrid('delRowData', getRowIndex(target));
                                    }
                                });
                            } else if (data.returnCode == "1") {
                                $.fool.alert({msg: data.message});
                            } else {
                                $.fool.alert({time: 1000, msg: "系统正忙，请稍后再试。"});
                            }
                        }
                    });
                }
            }
        });
    }

    //保存某行
    function saverow(target) {
        var index = getRowIndex(target);
        var row = $("#infoGrid").jqGrid("getRowData", index);
        var fid = getEditor("fid", index).val();
        var rate = getEditor("rate", index).numberbox("getValue");
        var date = getEditor("date", index).datebox("getValue");
        var increase = (getEditor("_increase", index).numberbox("getValue")) / 100;
        getEditor("increase", index).val(increase);
        /*$.post("
        ${ctx}/api/rate/loanrate/save",{fid:fid,rate:rate,date:date,increase:increase},function(data){
         dataDispose(data);
         if(data.returnCode=="0"){
         $.fool.alert({time:1000,msg:'保存成功！',fn:function(){
         $('#infoGrid').jqGrid('saveRow', index);
         $('#infoGrid').jqGrid('setRowData',index,{
         fid:data.data,
         isNew:0,
         date:date,
         rate:rate,
         editing:false,
         action:null
         });
         /!* $('#infoGrid').datagrid('reload'); *!/
         }});
         }else if(data.returnCode=="1"){
         $.fool.alert({time:1000,msg:data.message});
         }else{
         $.fool.alert({msg:"系统正忙，请稍后再试。"});
         }
         });*/
        $.ajax({
            url: '${ctx}/api/rate/loanrate/save',
            data: {fid: fid, rate: rate, date: date, increase: increase},
            type: "PUT",
            dataType: "json",
            success: function (data) {
                dataDispose(data);
                if (data.returnCode == "0") {
                    $.fool.alert({
                        time: 1000, msg: '保存成功！', fn: function () {
                            $('#infoGrid').jqGrid('saveRow', index);
                            $('#infoGrid').jqGrid('setRowData', index, {
                                fid: data.data,
                                isNew: 0,
                                date: date,
                                rate: rate,
                                editing: false,
                                action: null
                            });
                            /* $('#infoGrid').datagrid('reload'); */
                        }
                    });
                } else if (data.returnCode == "1") {
                    $.fool.alert({time: 1000, msg: data.message});
                } else {
                    $.fool.alert({msg: "系统正忙，请稍后再试。" + data.returnCode + "haha"});
                }
            }
        });
    }

    //撤销修改
    function cancelrow(target) {
        var index = getRowIndex(target);
        var isNew = getEditor("isNew", index).val();
        if (isNew != 1) {
            $('#infoGrid').jqGrid('restoreRow', index);
            $('#infoGrid').jqGrid('setRowData', index, {editing: false, action: null});
        } else {
            $('#infoGrid').jqGrid('delRowData', index);
        }

    }

    //增幅转换成百分比
    function toHunder(row) {
        if (row.increase) {
            row._increase = row.increase * 100;
            return row._increase;
        } else {
            return 0;
        }
    }

    function getEditor(name, rowId) {
        return $('#infoGrid').find('#' + rowId + "_" + name);
    }
</script>
</html>