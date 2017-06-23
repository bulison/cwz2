<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp"%>
    <title>经营收益率分析</title>
    <%@ include file="/WEB-INF/views/common/js.jsp"%>
    <script type="text/javascript"
            src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript"
            src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
    <style>
        body{
            padding-bottom:50px;
        }
        .fixed{
            position:fixed !important;
            border-width:1px 0 !important;
            z-index: 102;
        }
        .form1 p font{
            width:115px;
        }
        .form1 p{
            margin:5px 0;
        }
        .form1{
            padding:5px 0;
            margin-top:10px;
        }
        .box{
            margin:10px 0;
        }
        .toolBox-button{
            position:fixed;
            width:100%;
            background:rgb(240, 240, 240);
            top:0;
            z-index:1000;
            padding:5px 0;
        }
    </style>
</head>
<body>
<div class="toolBox-button">
    <div class="titleCustom">
        <div class="squareIcon">
            <span class="Icon"></span>
            <div class="trian"></div>
            <h1>经营收益率分析</h1>
        </div>
    </div>
    <form id="form" class="form1">
        <p><font>会计期间：</font><input id="search-periodId" class="textBox"></p>
        <a id="search" href="javascript:;" class="btn-blue4 btn-s">查询</a>
        <a href="javascript:;" id="caculate" class="btn-ora-checkdate">计算</a>
        <a href="javascript:;" id="default" class="btn-ora-import">写入默认公式</a>
        <a href="javascript:;" id="recovery" class="btn-ora-recovery">恢复公式</a>
        <a href="javascript:;" id="tips" class="btn-ora-batch">公式格式帮助</a>
    </form>
    <div style="margin-top: 10px">
    <table id="incomeRateList"></table>
    <div id="pager"></div>
    </div>
</div>

<script type="text/javascript">
    var periodId = "";
    var periodName = "";
    //查询
    $("#search").click(function () {
       $("#form").form("enableValidation");
       if($("#form").form("validate")){
           periodId = ($("#search-periodId").next())[0].comboObj.getSelectedValue();
           periodName = ($("#search-periodId").next())[0].comboObj.getComboText();
           var option = {periodId:periodId};
           $("#incomeRateList").jqGrid("setGridParam",{postData:option}).trigger("reloadGrid");
       }
    });

    //建表
    $("#incomeRateList").jqGrid({
        datatype:function (postdata) {
            $.ajax({
                url:'${ctx}/rate/FiscalBusiness/query',
                dataType:'json',
                data:postdata,
                complete:function (data,stat) {
                    if(stat == 'success'){
                        data.responseJSON.totalpages = Math.ceil(data.responseJSON.total/postdata.row);
                        $("#incomeRateList")[0].addJSONData(data.responseJSON);
                    }
                }
            })
        },
        forceFit:true,
        pager:'#pager',
        /*rowList:[10,20,30],*/
        viewrecords:true,
        rowNum:99999,//不用分页，设置一个最大的
        jsonReader:{
            records:'total',
            total:'totalpages'
        },
        width:$(window).outerWidth() - 18 ,
        height:$(window).outerHeight() - 200 +'px',
        colModel:[{
            name:'fid',
            label:'fid',
            hidden:true
        },{
            name:'isNew',//自己设置的
            label:'isNew',
            hidden:true
        },{
            name:'editing',//自己设置的
            label:'editing',
            hidden:true
        },{
            name:'updateTime',
            label:'updateTime',
            hidden:true
        },{
            name:'action',
            label:'操作',
            align:'center',
            width:100,
            formatter:function (cellvalue,options,rowObject) {
                if(rowObject.editing){
                    var s = '<a title="保存" href="javascript:;" class="btn-save" onclick="save(\''+options.rowId+'\',\''+rowObject.fid+'\');"></a>';
                if(rowObject.isNew=="true"){
                    var d ='<a title="删除" href="javascript:;" class="btn-del" onclick="del(\''+rowObject.fid+'\');"></a>';
                }else{
                    var d = '<a title="取消" href="javascript:;" class="btn-back" onclick="cancel(\''+options.rowId+'\');"></a>';
                }
                return s+d;
                }else{
                    var d_ = '<a title="删除" href="javascript:;" class="btn-del" onclick="del(\''+rowObject.fid+'\');"></a>';
                    return d_;
                }
            }
        },{
            name : 'item',
            label : '项目',
            align:'left',
            width:100,
            editable:true,
            edittype:"text"
        },{
            name : 'number',
            label : '行号',
            align:'center',
            width:100,
            editable:true,
            edittype:"text"
        },{
            name : 'value',
            label : '本期金额',
            align:'center',
            width:100,
            formatter:'currency',
            formatoptions:{
                decimalPlaces:2
            }
        },{
            name : 'formula',
            label : '公式',
            align:'center',
            width:100,
            editable:true,
            edittype:"text"
        }],
        onSelectRow:function (rowid) {
            editRow(rowid);
        },
        gridComplete:function () {
            $("#incomeRateList").parents(".ui-jqgrid-bdiv").css("overflow-x","hidden");//隐藏底部滚动条
        }
    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false});
    //隐藏工具栏
    $("#pager").hide();
            //会计期间
    var  periodData = getComboData('${ctx}/report/profitSheet/queryPeriods');
        $("#search-periodId").fool("dhxCombo",{
        width:160,
        height:30,
        editable:false,
        required:true,
        novalidate:true,
        setTemplate:{
            input:'#period#',
            option:'#period#'
        },
        data:periodData,
        focusShow:true,
        onLoadSuccess:function () {
            ($("#search-periodId").next())[0].comboObj.deleteOption("");
        }
    });

        //编辑
    function editRow(rowid) {//传入行rowid也是一样的

        var editRow_ = $("table#incomeRateList tr[editable = '1']");
        if(editRow_.length > 0){
/*            $.fool.alert({
                msg:'请先保存本行数据！',time:2000
            })*/
            return false;
        }

        var rowData = $("#incomeRateList").jqGrid("getRowData",rowid);
        if(rowData.editing == "true"){//如果editing本身是true的话，return中断
            return;
        }
        rowData.editing = "true";
        rowData.action = null;
        $("#incomeRateList").jqGrid("setRowData",rowid,rowData);//将修改后的rowData传到行中
        $("#incomeRateList").jqGrid("editRow",rowid);//鼠标单击第几行就进入编辑状态

        //项目
        getEditor("incomeRateList",rowid,"item").textbox({
            missingMessage:'该项为必输项目',
            required:true,
            novalidate:true,
            validType:'maxLength[100]',
            value:rowData.item
        });
        //行号
        getEditor("incomeRateList",rowid,"number").numberbox({
            required:true,
            novalidate:true,
            min:1,
            value:rowData.number
        });
        //公式
        getEditor("incomeRateList",rowid,"formula").textbox({
            validType:'maxLength[500]',
            value:rowData.formula
        })
    }

        //保存
    function save(rowid,id) {
        var rowData = $("#incomeRateList").jqGrid("getRowData",rowid);
        var updateTime = rowData.updateTime;
        var item = getEditor('incomeRateList',rowid,'item').textbox("getValue");
        var number = getEditor("incomeRateList",rowid,'number').numberbox("getValue");
        var formula = getEditor("incomeRateList",rowid,'formula').textbox("getValue");
        $.ajax({
            url:'${ctx}/rate/FiscalBusiness/save',
            async:false,
            method:'post',
            data:{fid:id,item:item,number:number,updateTime:updateTime,formula:formula,periodId:periodId,isNew:false},
            success:function (data) {
                dataDispose(data);
                if(data.returnCode == 0){
                    $.fool.alert({msg:"保存成功！",time:2000})
                    $("#incomeRateList").jqGrid("saveRow",rowid);
                    $("#incomeRateList").jqGrid("setRowData",rowid,{
                        item:item,
                        number:number,
                        formula:formula
                    })
                    $("#incomeRateList").trigger("reloadGrid");
                }else if(data.returnCode == 1){
                    $.fool.alert({msg:data.msg,time:1000})
                }else{
                    $.fool.alert({msg:'服务器繁忙，请稍后再试！'})
                }
            }
        })
    };

    //取消
    function cancel(rowid) {
        $("#incomeRateList").jqGrid("setRowData",rowid,{
            editing:false,
            action:null
        });
        $("#incomeRateList").jqGrid("restoreRow",rowid);
    }

    //删除
    function del(id) {
        $.fool.confirm({title:"确认",msg:'删除后将无法恢复，确认删除？',fn:function (data) {
            if(data) {
                $.ajax({
                    url: '${ctx}/rate/FiscalBusiness/delete',
                    async: false,
                    data: {fid:id},
                    success: function (data) {
                        dataDispose(data);
                        if(data.result == 0){
                            $.fool.alert({
                                msg:"删除成功！",time:1000
                            });
                            $("#incomeRateList").trigger("reloadGrid");
                        }else if(data.result == 1){
                            $.fool.alert({msg:data.msg})
                        }else{
                            $.fool.alert({msg:'服务器繁忙，请稍后再试！'})
                        }
                    }

                })
            }
        }})
    };

    //写入默认公式
    $("#default").click(function(){
        if(periodId != ""){
            $.fool.confirm({title:'设置默认提示',msg:'写入默认后会覆盖原来的默认设置，确认设置？',fn:function (data) {
                if(data){
                    $.post("${ctx}/rate/FiscalBusiness/saveBusinessFormula?periodId="+periodId,function (data) {
                        dataDispose(data);
                        if(data.returnCode == 0){
                            $.fool.alert({msg:'写入默认成功!',time:2000})
                        }else if(data.returnCode == 1){
                            $.fool.alert({msg:data.msg})
                        }else{
                            $.fool.alert({msg:'服务器繁忙，请稍后再试'});
                        }
                    })
                }
            }})
        }else{
            $.fool.alert({msg:'请选择会计期间查询数据后再写入默认'});
        }
    });

    //恢复默认公式
    $("#recovery").click(function () {
        if(periodId != ""){
            $.fool.confirm({title:'恢复公式提示',msg:'恢复公式后会覆盖'+periodName+'当前会计期间页面数据，确认恢复？',fn:function (data) {
                if(data){
                    $.post('${ctx}/rate/FiscalBusiness/resumeFormula?periodId='+periodId,function (data) {
                        dataDispose(data);
                        if(data.returnCode == 0){
                            $.fool.alert({msg:'恢复默认成功！',time:2000})
                            $("#incomeRateList").trigger("reloadGrid");
                        }else if(data.returnCode == 1){
                            $.fool.alert({msg:data.msg})
                        }else{
                            $.fool.alert({msg:'服务器繁忙，请稍后再试'});
                        }
                    });
                }
            }
            });
        }else{
            $.fool.alert({msg:'请选择会计期间查询数据后再写入默认'})
        }
    });

    //计算
    $("#caculate").click(function () {
        if(periodId != ''){
            $.ajax({
                url:'${ctx}/rate/FiscalBusiness/computeFormula',
                dataType:'json',
                method:'post',
                data:{periodId:periodId},
                beforeSend:function () {
                    $.messager.progress({
                        text:'努力计算中，请稍后...'
                    })
                },
                success:function (data) {
                    dataDispose(data);//base.js中的兼容处理
                    $.messager.progress("close");
                    if(data.returnCode == 0){
                        $.fool.alert({msg:'计算经营收益分析表完成！',time:2000,fn:function(){
                            $('#incomeRateList').trigger("reloadGrid");
                        }});
                    }else if(data.returnCode == 0){
                        $.fool.alert({msg:data.msg});
                    }else{
                        $.fool.alert({msg:'服务器繁忙,请稍后再试'});
                    }
                }
            })
        }else{
            $.fool.alert({msg:'请选择会计期间查询数据后再计算'});
        }
    });

    $("#tips").tooltip({
        position:'bottom',
        width:300,
        content:'<span style="color: #fff">AA(科目、科目)：取科目的所有借方，贷方的发生额；<br>' +
        'AD(科目、科目)：取科目的借方发生额；<br>' +
        'AC（科目,科目）：取科目的贷方发生额；<br>' +
        'AS（科目,科目）：取科目设置中余额方向的发生额；<br>' +
        'DS（行号,行号）：根据行号，累加数据；<br>' +
        'AX（科目）：取科目借方余额；参照AA的计算方法，得出科目余额，如果余额小于0，则返回0，否则返回余额；<br>' +
        'AY（科目）：取科目贷方余额；参照AA的计算方法，得出科目余额，如果余额大于0，则返回0，否则返回余额的绝对值；' +
        '</span>',
        onShow:function () {
            $(this).tooltip('tip').hide();
            $(this).tooltip('tip').slideDown(300);
            $(this).tooltip('tip').css({
                width:'500px',
                background:'#666',
                boderColor:'#666'
            })
        }

    });
</script>
</body>
</html>