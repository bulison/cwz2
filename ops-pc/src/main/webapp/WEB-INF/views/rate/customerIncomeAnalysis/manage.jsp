<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp"%>
    <title>客户收益分析</title>
    <%@ include file="/WEB-INF/views/common/js.jsp"%>
    <script type="text/javascript"
            src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript"
            src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
    <style type="text/css">
        .toolBox-button{
            position:fixed;
            width:100%;
            background:rgb(240, 240, 240);
            top:0;
            z-index:1000;
            padding:5px 0;
        }
        .form1 p font{
            width:90px;
        }
        .form1 p{
            margin:5px 0;
        }
        .form1{
            padding:5px 0;
            margin-top:10px;
        }
        #detail{
            margin-top: 10px;
            z-index: 9999
        }

        #form2 {
            background-color: rgb(255, 255, 255);
            padding-top: 10px;
            padding-bottom: 30px;
        }
        #form2 p{
            float: left;
            margin-left: 5px;
        }

    </style>
</head>
<body>
<div class="toolBox-button">
    <div class="titleCustom">
        <div class="squareIcon">
            <span class="Icon"></span>
            <div class="train"></div>
            <h1>客户收益分析</h1>
        </div>
    </div>
    <form id="form" class="form1">
        <p><font>开始日期：</font><input id="startDay" name="startDay" class="textBox"></p>
        <p><font>结束日期：</font><input id="endDay" name="endDay" class="textBox"></p>
        <p><font>客户类别：</font><input id="category" name="category" class="textBox"></p>
        <p><font style="width: 50px;">客户：</font><input id="customer" name="customer" class="textBox"></p>
        <p><font style="width: 50px;">地区：</font><input id="area" name="area" class="textBox"></p>
        <p><a href="javascript:;" id="search" style="margin-left: 2px" class="btn-blue4 btn-s">查询</a></p>
        <p><a href="javascript:;" id="clear" style="margin-left: 2px" class="btn-blue4 btn-s">清空</a></p>

    </form>
    <%-- 表格 --%>
    <div style="margin-top: 10px;">
        <table id="customerIn"></table>
        <div id="pager"></div>
    </div>
    <%-- 明细窗口 --%>
    <div id="detail"></div>
</div>
<script type="text/javascript">
    var endDate = new Date().format('yyyy-MM-dd');
    var y = new Date().getFullYear();
    var m = new Date().getMonth();
    var d = new Date().getDate();

    var startDate = y + "-" + m + "-" + d;
    //开始日期
    $('#startDay').fool("datebox", {
        width: 120,
        height: 30,
        inputDate: true,
        focusShow: true,
        required: true,
        prompt: '开始日期',
        value:startDate
    });
    //结束日期
    $("#endDay").fool("datebox",{
        width:120,
        height:30,
        inputDate:true,
        focusShow:true,
        required:true,
        prompt:'结束日期',
        value:endDate
    });

    //地区
    var regionValue = "";
    $.ajax({
        url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=001&num="+Math.random(),
        async:false,
        success:function (data) {
            regionValue = formatTree(data[0].children,'text','id')
        }
    });
    $("#area").fool("dhxCombo",{
        width:90,
        height:30,
        data:regionValue,
        editable:false,
        focusShow:true,
        setTemplate:{
            input:'#name#',
            option:'#name#'
        },
        toolsBar:{
            name:'地区',
            addUrl:'/basedata/listAuxiliaryAttr',
            refresh:true
        },
        onLoadSuccess:function (combo) {
            combo.deleteOption("");
        }
    });


    var customerTypeData = "";
    $.ajax({
        url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=002&num="+Math.random(),
        async:false,
        success:function (data) {
            customerTypeData = formatTree(data[0].children,'text','id');
        }
    });

    //客户类别
    $("#category").fool("dhxCombo",{
        width:100,
        height:30,
        data:customerTypeData,
        editable:false,
        focusShow:true,
        setTemplate:{
            input:"#name#",
            option:"#name#"
        },
        toolsBar:{
            name:'客户类别',
            addUrl:'/basedata/listAuxiliaryAttr',
            refresh:true
        },
        onLoadSuccess:function (combo) {
            combo.deleteOption("");
        }
    });

    //客户
    var customerValue = "";
    $.ajax({
        url:'${ctx}/customer/list?rows=99999',
        async:false,
        success:function (data) {
            customerValue = formatData(data.rows,'fid','name');
        }
    });

    $("#customer").fool("dhxComboGrid",{
        width:100,
        height:30,
        data:customerValue,
        filterUrl:'${ctx}/customer/list?rows=99999',
        editable:false,
        focusShow:true,
        setTemplate:{
            input:"#name#",
            columns:[
                {option:'#code#',header:'编号',width:100},
                {option:'#name#',header:'名称',width:100}
            ]
        },
        toolsBar:{
            name:'客户',
            addUrl:'/customer/manage',
            refresh:true
        }
    });

    //查询
    $("#search").click(function () {
        var startDate = $("#startDay").textbox("getValue");
        var endDate = $("#endDay").textbox("getValue");
        var area = ($("#area").next())[0].comboObj.getSelectedValue();
        var customerId = ($("#customer").next())[0].comboObj.getSelectedValue();
        var category = ($("#category").next())[0].comboObj.getSelectedValue();
        $("#customerIn").jqGrid("setGridParam",{
            postData: {
                "startDate": startDate,
                "endDate": endDate,
                "area":area,
                "customerId":customerId,
                "category":category
            }
        }).trigger("reloadGrid");
    });

    //清空
    $("#clear").click(function () {
        ($("#category").next())[0].comboObj.setComboValue("");
        ($("#category").next())[0].comboObj.setComboText("");
        ($("#customer").next())[0].comboObj.setComboValue("");
        ($("#customer").next())[0].comboObj.setComboText("");
        ($("#area").next())[0].comboObj.setComboValue("");
        ($("#area").next())[0].comboObj.setComboText("");
    });

    //建表
    $("#customerIn").jqGrid({
        datatype:function (postdata) {
            if(!postdata.endDate||!postdata.startDate) return;
            $.ajax({
                url:'${ctx}/api/rate/customerIncomeAnalysisProcessing',
                data:postdata,
                complete:function (data,stat) {
                    if(stat == "success"){
                        data.responseJSON.totalpages = Math.ceil(data.responseJSON.total/postdata.row);
                        $("#customerIn")[0].addJSONData(data.responseJSON);
                    }
                }
            })
        },
        forceFit:true,
        pager:'#pager',
        rowNum:10,
        rowList:[10,20,30],
        footerrow:true,//添加底部汇总
        gridComplete: completeMethod,//加载完成后的调用completeMethod
        viewrecords:true,
        jsonReader:{
            records:'total',
            total:'totalpages'
        },
        width:$(window).width()-20,
        height:$(window).outerHeight()-260+"px",
        colModel:[{
            name:'customerId',
            label:'客户ID',
            hidden:true
        },{
            name:'code',
            label:'客户编号',
            align:'center',
            width:100
        },{
            name:'name',
            label:'客户名称',
            align:'center',
            width:100
        },{
            name:'areaName',
            label:'地区',
            align:'center',
            width:100,
        },{
            name:'area',
            label:'area',
            align:'center',
            width:100,
            hidden:true
        },{
            name: 'category',
            label: 'category',
            align: 'center',
            width: 100,
            hidden:true
        },{
            name:'categoryName',
            label:'客户类别',
            align: 'center',
            width: 100,
        },{
            name:'salesAmount',
            label:'销售金额',
            align:'center',
            width:100,
            formatter:'currency',
            formatoptions:{
                decimalPlaces:2
            }
    },{
            name:'goodsCost',
            label:'货品成本',
            align:'center',
            width:100,
            formatter:'currency',
            formatoptions:{
                decimalPlaces:2
            }
        },{
            name:'cost',
            label:'费用',
            align:'center',
            width:100,
            formatter:'currency',
            formatoptions:{
                decimalPlaces:2
            }
        },{
            name:'profit',
            label:'利润',
            align:'center',
            width:100,
            formatter:'currency',
            formatoptions:{
                decimalPlaces:2
            }

        },{
            name:'currentDebt',
            label:'当前欠收',
            align:'center',
            width:100,
            formatter:'currency',
            formatoptions:{
                decimalPlaces:2
            }
        },{
            name:'credit',
            label:'信用度(%)',
            align:'center',
            width:100,
            formatter:'number',
            formatoptions:{
                decimalPlaces:2
            }
        },{
            name:'percentageAmount',
            label:'销售提成',
            align:'center',
            width:100,
        },{
            name:'action',
            label:'明细',
            align:'center',
            width:100,
            formatter:function (cellValue,options,rowObject) {
                var c = '<a class="btn-detail" href="javascript:;" onclick=showDetail(\''+rowObject.customerId+'\')></a>';
                return c;
            }
        }]
    }).navGrid('#pager', {add: false, del: false, edit: false, search: false, view: false});

    function windowHelp(title,url){
        $('#detail').window({
            title:title,
            collapsible:false,
            minimizable:false,
            maximizable:false,
            resizable:false,
            href:url,
            width:1005,
            height:650,
            top:($(window).height()-730)/2+$(document).scrollTop(),
            modal:true,
            onOpen:function(){
                /*$("html").css("overflow","hidden");//当内容溢出时，隐藏溢出部分*/
                /*$(this).parent().prev().css("display","none");*/
            },
            onClose:function(){
                if($('#pop-win').html()!=""){
                    $('#pop-win').window("destroy");//销毁
                }
                $("html").css("overflow","");
            }
        });
    }

    var detailData = '';
    var customerId = '';
    var startDay = '';
    var endDay = '';
    //查看明细
    function showDetail(id) {
        startDay = $("#startDay").textbox("getValue");
        endDay = $("#endDay").textbox("getValue");
        customerId = id;
        $.ajax({
            url:'${ctx}/api/rate/customerIncomeAnalysisDetail',
            type:'get',
            data:{
                startDate:startDay,
                endDate:endDay,
                customerId:customerId
            },
            success:function (data) {
                detailData = data;
                windowHelp('查看明细','${ctx}/customer/customerIncomeDetail?detailData=detailData&customerId=customerId&startDate=startDay&endDate=endDay');
            }
        })

    }

    //底部添加合计
    function completeMethod() {
        var sumSalesAmount = $("#customerIn").getCol("salesAmount",false,'sum');
        var sumGoodsCost = $("#customerIn").getCol("goodsCost",false,'sum');
        var sumCost = $("#customerIn").getCol("cost",false,'sum');
        var sumProfit = $("#customerIn").getCol("profit",false,'sum');
        var sumCurrentDebt = parseFloat($("#customerIn").getCol("currentDebt",false,'sum')).toFixed(4);//保留两位小数
        $("#customerIn").footerData('set',{
            'areaName':'合计',
            salesAmount:sumSalesAmount,
            goodsCost:sumGoodsCost,
            cost:sumCost,
            profit:sumProfit,
            currentDebt:sumCurrentDebt
        });
    }
</script>
</body>
</html>