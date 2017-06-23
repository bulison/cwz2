<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <title>客户收益率分析</title>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
     <script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
    <style>
        .form1 p font {
            width: 100px;
        }
    </style>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>客户收益率分析</h1>
    </div>
</div>

<div class="myform">
    <div id="buttonBox" style="margin-bottom:10px;width:100%;">
        <a href="javascript:;" title="查询" class="btn-blue4 btn-s" id="mySearch">查询</a>
        <a href="javascript:;" title="清空" class="btn-blue4 btn-s" id="clearForm">清空</a>
    </div>
    <div class="searchBox" style="background: #fff;margin:0 20px 10px 0;/* border: 1px #ccc solid; */">
        <form class="form1 inSearch" id='inSearch'
              style="/* background: url('/ops/resources/images/notebook.jpg') repeat-y; */padding: 10px 20px 10px 60px;background-position-x: 10px;">
            <p><font>客户编码：</font><input id="code" name="code"/></p>
            <p><font>客户名称：</font><input id="goodsName" name="goodsId"/></p>
            <p><font>地区：</font><input id="goodsSpecName" name="goodsSpecId"/></p>
            <p><font>客户类别：</font><input id="customerName" name="customerId"/></p>
            <p><font>开始日期：</font><input id="startDay" name="startDay"/></p>
            <p><font>结束日期：</font><input id="endDay" name="endDay"/></p>
        </form>
    </div>
</div>
<table id="detailsList"></table>
<div id="pager"></div>


<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
    $('#mySearch').click(function () {
        var data = $('#inSearch').serializeJson();
        $('#detailsList').datagrid('load', data);
    });

    $('#clearForm').click(function () {
        $('#inSearch').form('clear');
    });

    //搜索条件初始化
    $('#code').textbox({
        prompt: "输入单号",
        width: 162,
        height: 30
    });
    $('#goodsName').fool('combogrid', {
        prompt: "输入编码、名称搜索",
        width: 162,
        height: 30,
        panelWidth: 300,
        panelHeight: 270,
        idField: 'fid',
        fitColumns: true,
        textField: 'name',
        focusShow: true,
        url: getRootPath() + '/goods/vagueSearch?searchSize=8',
        columns: [[
            {field: 'fid', title: 'fid', hidden: true},
            {field: 'name', title: '名称', width: 100, searchKey: false},
            {field: 'code', title: '编码', width: 30, searchKey: false},
            {field: 'barCode', title: '条码', width: 30, searchKey: false},
            {field: 'searchKey', hidden: true, searchKey: true},
        ]]
    });
    $('#goodsSpecName').fool('combogrid', {
        prompt: "输入编码、名称搜索",
        width: 162,
        height: 30,
        panelWidth: 300,
        panelHeight: 270,
        idField: 'fid',
        fitColumns: true,
        textField: 'name',
        focusShow: true,
        url: getRootPath() + '/goodsspec/vagueSearch?searchSize=8',
        columns: [[
            {field: 'fid', title: 'fid', hidden: true},
            {field: 'name', title: '名称', width: 100, searchKey: false},
            {field: 'code', title: '编码', width: 30, searchKey: false},
            {field: 'searchKey', hidden: true, searchKey: true},
        ]]
    });
    $('#customerName').fool('combogrid', {
        prompt: "输入编码、名称搜索",
        width: 162,
        height: 30,
        idField: 'fid',
        textField: 'name',
        fitColumns: true,
        focusShow: true,
        panelWidth: 300,
        panelHeight: 283,
        url: getRootPath() + '/customer/vagueSearch?searchSize=8',
        columns: [[
            {field: 'fid', title: 'fid', hidden: true},
            {field: 'code', title: '编号', width: 100, searchKey: false},
            {field: 'name', title: '名称', width: 100, searchKey: false},
            {field: 'categoryName', title: '分类', width: 100, searchKey: false},
            {field: 'searchKey', hidden: true, searchKey: true},
        ]]
    });
    $('#startDay').fool('datebox', {
        prompt: "日期可选择可输入",
        inputDate: true,
        width: 162,
        height: 30,
    });
    $('#startDay').datebox('textbox').focus(function () {
        $('#startDay').datebox('showPanel');
    });
    $('#endDay').fool('datebox', {
        prompt: "日期可选择可输入",
        inputDate: true,
        width: 162,
        height: 30,
    });
    $('#endDay').datebox('textbox').focus(function () {
        $('#endDay').datebox('showPanel');
    });


    //数据载入
   /* $('#detailsList').datagrid({
        url: getRootPath() + "/rate/saleOutDetail/query",
        fitColumns: true,
        singleSelect: true,
        showFooter: true,
        pagination: true,
        columns: [[
            /!*   {field:"detailId",title:"detailId",hidden:true},
             {field:"billId",title:'billId',hidden:true},	   *!/
            {field: "code", title: '客户编码', width: 100},
            {field: "goodsName", title: '客户名称', width: 100},
            {field: "goodsSpecName", title: '地区', width: 100},
            {field: "customerName", title: '客户类别', width: 100},
            {field: "saleDate", title: '销售金额', width: 100},
            {field: 'hpcb', title: '货品成本', width: 100},
            {field: "unitName", title: '费用', width: 100},
            {field: 'lirun', title: '利润', width: 100},
            {field: 'dqqs', title: '当前欠收', width: 100},
            /!*	          {field:"unitPrice",title:'收益',width:100},
             {field:"saleAmount",title:'加权收益率',width:100},*!/
            {field: 'xyd', title: '信用度', width: 100},
            {field: 'mx', title: '明细', width: 100}
        ]],
        onLoadSuccess: function (data) {
            $('#detailsList').datagrid('reloadFooter', [{
                code: "合计:",
                saleAmount: 0,
                returnQuentity: 0,
                returnAmount: 0,
                receivedAmount: 0,
                notReceiveAmount: 0,
                expectReturn: 0,
                expectRate: 0,
                marketRate: 0,
                realRate: 0,
                currentRate: 0
            }]);
        }
    });*/


    var detailDate = "";
    $.ajax({
        url:getRootPath() + "/rate/saleOutDetail/query",
        dataType:'json',
        success:function(data){
            detailDate=data;
        }

    });

    $("#detailsList").jqGrid({
/*        datatype:'local',*/
        footerrow:true,
        data:detailDate,
        forceFit:true,
        height:200,
        width:$(window).width()*0.985,
        pager:"#pager",
        rowList:[10,20,30],
        rowNum:10,
        jsonReader:{
            records:'total',
            total:'totalpages'
        },
        colModel:[{
            name:'code',
            label:'客户编码',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'khname',
            label:'客户名称',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'diqu',
            label:'地区',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'kutype',
            label:'客户类别',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'saleDate',
            label:'销售金额',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'hpcb',
            label:'货品成本',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'fy',
            label:'费用',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'lirun',
            label:'利润',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'dqqs',
            label:'当前欠收',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'xyd',
            label:'信用度',
            align:'center',
            sortable:true,
            width:100
        },{
            name:'mx',
            label:'明细',
            align:'center',
            sortable:true,
            width:100
        }] ,
        loadComplete:function(){
            var sumSale = $("#detailsList").getCol("saleDate",false,"sum");
            var sumHpcb = $("#detailsList").getCol("hpcb",false,"sum");
            var sumLirun = $("#detailsList").getCol("lirun",false,"sum");
            var sumDqqs = $("#detailsList").getCol("dqqs",false,"sum");
            $("#detailsList").footerDate('set',{
                code:'合计',
                saleDate:sumSale,
                hpcb:sumHpcb,
                lirun:sumLirun,
                dqqs:sumDqqs
            });
        }
    }).navGrid('#detailsList', {add: false, del: false, edit: false, search: false, view: false});

    function rateOut(value, row, index) {
        return value + "%";
    }
</script>
</body>
</html>
