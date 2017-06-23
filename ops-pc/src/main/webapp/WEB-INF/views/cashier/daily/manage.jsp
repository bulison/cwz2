<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/common/header.jsp"%> 
    <title>银行日记账单管理</title>
    <%@ include file="/WEB-INF/views/common/js.jsp"%>
    <script type="text/javascript"
    src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
    <script type="text/javascript"
    src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
    <style>
        /* html{
            overflow:hidden;
        } */
        .fixed {
            top: 0px;
        }
        #add,#import{
            vertical-align: top;
            margin-top: 5px;
        }
        #search-form{
            display: inline-block;
            width:92%;
        }
        #search-form .textbox{
            margin:5px 5px 0px 0px
        }
        #search-form .btn-s{
            vertical-align: middle;
            margin:5px 5px 0px 0px
        }
        #Inquiry{margin-left: 5px;}
        #grabble{ float: right;}
        #bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 25px;}
        .button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
            border-radius:3px;/* W3C syntax */float: right; right:29px; top:63px; position: absolute;}
        .button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:5px;}
        .input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:91px;z-index: 1;}
        .input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px;width:240px;}
        .button_clear{ border-top: 1px solid #D5DBEA;margin-top: 10px; text-align: right; padding: 10px 10px 0px 0px;}
        #subject{display: inline-block;margin-top:5px;}
        .roed{
            -moz-transform:scaleY(-1);
            -webkit-transform:scaleY(-1);
            -o-transform:scaleY(-1);
            transform:scaleY(-1);
            filter:FlipV();
        }
        .dhxDiv{
            margin:5px 5px 0 0
        }
    </style>
</head>
<body>
    <div class="titleCustom">
        <div class="squareIcon"><span class='Icon'></span><div class="trian"></div><h1>银行日记账单</h1></div>
    </div>
    <div style="margin:5px 0px 10px 0px">
      <fool:tagOpt optCode="dabillAdd"><a href="javascript:;" id="add" class="btn-ora-add" style="margin-right:5px;">新增</a></fool:tagOpt>
        <a href="javascript:;" id="export" class="btn-ora-export" style="margin-right:5px;">导出</a>
        <a href="javascript:;" id="import" class="btn-ora-import" style="margin-right:5px;">导入</a>
      <form action="" id="subject">
        <input id="search-subjectId" name="subjectIds" type="hidden"/>
        <input id="search-subjectName" class="textBox"/>
    </form>
    <a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
    <a href="javascript:;" id="print" class="btn-ora-printer" style="margin-left: 5px;">打印</a>
    <div id="grabble">
        <input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly" />
        <a href="javascript:;" class="button_a"><span class="button_span"></span></a>
    </div>
    <div class="input_div">
        <form id="search-form">
            <p>结算号：<input id="search-settlementNo" class="textBox"/></p>
            <p>开始日期：<input id="search-startDay" /></p>
            <p>结束日期：<input id="search-endDay" /></p>
            <p>结算方式：<input id="search-settlementTypeId" type="hidden"/><input id="search-settlementTypeName" /></p>
            <p>余额方向：<input id="search-direction" /></p>
            <p>是否勾对：<input id="search-checked" /></p>
        </form>
        <div class="button_clear">
            <fool:tagOpt optCode="dabillSearch"><a id="search-btn" class="btn-blue btn-s">查询</a></fool:tagOpt>
           <a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="margin-left: 5px">清空</a>
           <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
       </div>
   </div>
</div>
<table id="billList"></table>
<div id="pager"></div>
<div id="addBox"></div>
<div id="pop-win"></div>

<script type="text/javascript">
/*    var myIndex = 0; //记录最后的index*/
    var rowChos = []; //选中的行

    var texts = [];
    var thead = [];
    
    //点击下拉按钮
    $('.button_a').click(function(){
        $('.input_div').slideToggle(200);
        $(this).addClass('roed');
    });

    //点击关闭隐藏
    $('#clear-btndiv').click(function(){
        $('.input_div').hide(2);
        $('.button_a').removeClass('roed');
    });

    //全局查询
    $('.Inquiry').click(function(){
    	$('#clear-btndiv').click();
		$("#search-btn").click();
//         $('#clear-btn').click();

    });

    //鼠标获取焦点
    $('#bolting').focus(function (){
        $('.input_div').show(2);
        $('.button_a').addClass('roed');
    });


    var chooserWindow_search="";
    var checkedData=[{value:"0",text:'未勾对'},{value:"1",text:'已勾对'},{value:null,text:'全部'}];

    var startDate = "";
    var endDate = "";
    $.ajax({
    	url:"${ctx}/fiscalPeriod/getFristUnCheckPeriod",
    	type:"post",
    	async:false,
    	data:{},
    	success:function(data){
    		if(data.startDate){
	    		startDate = data.startDate;
	    		endDate = data.endDate;
    		}
    	}
    });
    //控件初始化
    $("#search-startDay").datebox({
      editable:false,
      width:160,
      height:32,
      prompt:"开始日期",
      value:startDate
  });
    $("#search-endDay").datebox({
      editable:false,
      width:160,
      height:32,
      prompt:"结束日期",
      value:endDate
  });

    var subjectCombo = $('#search-subjectName').fool('dhxComboGrid',{
        prompt:"科目",
        width:160,
        height:32,
        focusShow:true,
        required:true,
        novalidate:true,
        data:getComboData(getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&q="),
        filterUrl:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&bankSubject=1",
        searchKey:"q",
        setTemplate:{
            input:"#name#",
            columns:[
            {option:'#code#',header:'编号',width:100},
            {option:'#name#',header:'名称',width:100},
            ],
        },
        toolsBar:{
            refresh:true
        },
        onChange:function(value,text){
            $('#search-subjectId').val(value);
            $('#search-subjectName').val(text);
            $('.Inquiry').click();
        }
  });

    $('#search-subjectName').next().find(".dhxcombo_select_button").click(function(){
        subjectCombo.closeAll();
        searchSubjectName();
    });

    $("#search-settlementTypeName").fool('dhxCombo',{
        width:162,
        height:32,
        clearOpt:false,
        prompt:"结算方式",
        focusShow:true,
        editable:false,
        data:getComboData("${ctx}/basedata/settlementType","tree"),
        setTemplate:{
            input:"#name#",
            option:"#name#"
        },
        toolsBar:{
        	name:"结算方式",
            refresh:true
        }
    });

    $("#search-settlementNo").textbox({
        width:160,
        height:32,
        prompt:"结算号"
    });

    $("#search-direction").fool('dhxCombo',{
      width:162,
      height:32,
      prompt:"余额方向",
      focusShow:true,
      editable:false,
      data:[{value:"1",text:'借方'},{value:"-1",text:'贷方'},{value:"",text:'全部'}],
      setTemplate:{
         input:"#name#",
         option:"#name#"
     }
});

    $("#search-checked").fool('dhxCombo',{
      width:162,
      height:32,
      prompt:"是否勾对",
      focusShow:true,
      editable:false,
      data:[{value:"0",text:'未勾对'},{value:"1",text:'已勾对'},{value:"2",text:'全部'}],
      setTemplate:{
         input:"#name#",
         option:"#name#"
     }
});

    //新增、修改、详情窗口初始化
    $('#addBox').window({
        top:150,
        collapsible:false,
        minimizable:false,
        maximizable:false,
        resizable:false,
        closed:true,
        modal:true,
        width:$(window).width()-200,
    });

    //导入
    $('#import').click(function(){
        var s={
            target:$('#pop-win'),
            boxTitle:"导入银行日记账单",
            downHref:getRootPath()+"/ExcelMapController/downTemplate?downFile=银行日记账单.xls",
            action:getRootPath()+"/cashierBankBillController/import?type=3",
            fn:'okCallback()',
        };
        importBox(s);
    });
    //导出
    $('#export').click(function(){
        var startDay=$("#search-startDay").datebox('getValue');
        var endDay=$("#search-endDay").datebox('getValue');
        var subjectId=$("#search-subjectId").val();
        var settlementTypeId=($("#search-settlementTypeName").next())[0].comboObj.getSelectedValue();
        var settlementNo=$("#search-settlementNo").textbox('getText');
        var direction=($("#search-direction").next())[0].comboObj.getSelectedValue();
        var fchecked=($("#search-checked").next())[0].comboObj.getSelectedValue();
        if(settlementTypeId==null)
            settlementTypeId="";
        if(direction==null)
            direction="";
        if(fchecked==null)
            fchecked="";
        var options={startDate:startDay,endDate:endDay,subjectIds:subjectId,settlementTypeId:settlementTypeId,settlementNo:settlementNo,direction:direction,fchecked:fchecked};
        exportFrom('${ctx}/cashierBankBillController/export?type=3',options);
    });
    function okCallback(){
        $('#pop-win').window("close");
        $("#billList").trigger("reloadGrid");
    }

    var cellattr = function(rowId, tv, rawObject, cm, rdata) {
        if(rawObject.source != "") return ' class="not-editable-cell"';
    }

    //科目初始数据列表初始化
    $("#billList").jqGrid({
    	datatype:function(postdata){
    		if(!postdata.subjectIds){
    			return false;
    		}
    		$.ajax({
    			url: '${ctx}/cashierBankBillController/listBankBill?type=3',
    			data:postdata,
    			dataType:"json",
    			complete: function(data,stat){
    			if(stat=="success") {
    				data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
    				$("#billList")[0].addJSONData(data.responseJSON);
                    /*myIndex = $('#billList').jqGrid("getRowData").length+1;*/
    				getTotal();
    			}
    			}
    		});
    	},
    	footerrow:true,
    	autowidth:true,
    	height:450,
    	pager:"#pager",
    	rowNum:10,
    	rowList:[10,20,30],
    	viewrecords:true,
    	multiselect:true,
    	jsonReader:{
    		records:"total",
    		total: "totalpages",  
    	},
    	onCellSelect:function(rowid,iCol,cellcontent,e){
    		var row = $("#billList").jqGrid("getRowData",rowid);
    		var fchecked = row.fchecked?row.fchecked:"";
            if (row.source != undefined && row.source != "") {//设置单个单元格
            }
            if(iCol != 0 && $("#billList #"+rowid).find(".btn-save").length <= 0 &&fchecked!=1){
            	myedit(rowid);
            }
    	},
    	onSelectAll:function(aRowids,status){
      		if(status){
      			var rows = $('#billList').jqGrid("getRowData");
      			rowChos = rows;
      		}else{
      			rowChos = [];
      		}
      	},
      	onSelectRow:function(rowid,status){
      		var rowids = $('#billList').jqGrid('getGridParam', 'selarrrow');
      		var rows = [];
      		for(var i=0;i<rowids.length;i++){
      			var row = $('#billList').jqGrid("getRowData",rowids[i]);
      			rows.push(row);
      		}
      		rowChos = rows;
      	},
    	colModel:[
    		{name : 'newRow',label : '新行标识',align:'center',hidden:true},
    		{name : 'updateTime',label : '更新时间',align:'center',hidden:true},
    		{name : 'memberId',label : '经手人',align:'center',hidden:true,editable:true,edittype:"text"},
    		{name : 'source',label : '来源',align:'center',hidden:true},
    		{name : 'settlementTypeId',label : '结算方式ID',align:'center',hidden:true,editable:true,edittype:"text"},
    		{name : 'subjectId',label : '科目ID',align:'center',hidden:true,editable:true,edittype:"text"},
    		{name : 'fid',label : 'fid',hidden:true},
    		{name : 'fchecked',label : '勾对',align:'center',width:"60px",formatter:function(cellvalue, options, rowObject){
    			if (typeof rowObject.fchecked != "undefined") {
    				return rowObject.fchecked==1?"已勾对":"未勾对";
    			} else {
    				return "";
    			}
    		}},
    		{name:'subjectName',label:'科目',align:'center',width:"100px"},
            {name:'voucherDate',label:'单据日期',align:'center',width:"120px",cellattr:function(rowId, val, rowObject, cm, rdata) {
            	if(rowObject.source != undefined && rowObject.source != "") {}
            },
            editable:true,
            edittype:"text",
            editoptions:{
            	dataInit:function (cl) {
            		$(cl).datebox({
            			width:"100%",
            			height:"80%",
            			editable:false,
            			required:true,
            			novalidate:true,
            		});
            		$(cl).datebox('textbox').focus(function(){
            			$(cl).datebox('showPanel');
            		});
            	}},
            	formatter:function(cellvalue, options, rowObject){
            		return cellvalue?cellvalue.substring(0,10):"";
            	}
            },
            {name:'settlementDate',label:'业务日期',align:'center',width:"120px",cellattr:function(rowId, val, rowObject, cm, rdata) {
            	if(rowObject.source != undefined && rowObject.source != "") {}
            },
            editable: true,
            edittype:"text",
            editoptions:{
            	dataInit:function (cl) {
            		$(cl).datebox({
            			width:"100%",
                        height:"80%",
                        editable:false,
                        required:true,
                        novalidate:true,
            		});
                    $(cl).datebox('textbox').focus(function(){
                        $(cl).datebox('showPanel');
                    });
            	}},
                formatter:function(cellvalue, options, rowObject){
                	return cellvalue?cellvalue.substring(0,10):"";
                }
            },
            {name:'orderno',label:'当天顺序号',align:'center',width:"65px",editable:true,edittype:"text",cellattr:function(rowId, val, rowObject, cm, rdata) {
            	if(rowObject.source != undefined && rowObject.source != "") {}
            },
            editoptions:{
            	dataInit:function (cl) {
            		$(cl).textbox({
                        width:"100%",
                        height:"80%",
                        validType:'accessoryNumber'
                    });
            	}
            }},
            {name:'settlementTypeName',label:'结算方式',align:'center',width:"100px",clearOpt:false,cellattr:function(rowId, val, rowObject, cm, rdata) {
            	if(rowObject.source != undefined && rowObject.source != "") {}
            },
            editable:true,edittype:"text",
            editoptions:{dataInit:function(cl){
            	$(cl).fool("dhxCombo",{
            		width:"100%",
                    height:"80%",
                    focusShow:true,
                    editable:false,
                    data:getComboData("${ctx}/basedata/settlementType","tree"),
                    setTemplate:{
                    	input:"#name#",
                        option:"#name#"
                    },
                    toolsBar:{
                    	name:"结算方式",
                        addUrl:"/basedata/listAuxiliaryAttr",
                        refresh:true
                    }
            	});
            }}},
            {name:'settlementNo',label:'结算号',align:'center',width:"100px",cellattr:function(rowId, val, rowObject, cm, rdata) {
            	if(rowObject.source != undefined && rowObject.source != "") {}
            },
                editable:true,edittype:"text",editoptions:{dataInit:function(cl){
                    $(cl).textbox({
                        width:"100%",
                        height:"80%",
                        validType:'accessoryNumber'
                    });
                }}},
                {name:'resume',label:'摘要',align:'center',width:"100px",edittype:"text",editable:true,
                editoptions:{dataInit:function(cl){
                    $(cl).fool("dhxCombo",{
                        width:"100%",
                        height:"80%",
                        focusShow:true,
                        data:getComboData("${ctx}/basedata/resume","tree"),
                        setTemplate:{
                            input:"#name#",
                            option:"#name#"
                        },
                        toolsBar:{
                            name:"摘要",
                            addUrl:"/basedata/listAuxiliaryAttr",
                            refresh:true
                        }
                    });
                }}},
                {name:'debit',label:'借方金额',align:'center',width:"100px",
                cellattr:function(rowId, val, rowObject, cm, rdata) {
                    if(rowObject.source != undefined && rowObject.source != "") {
//                        // cm.editable = false;
//                        $("#billList").jqGrid('setCell', rowId, 'debit', 0, 'not-editable-cell');
var a = '1';
}
},
editable:true,edittype:"text",editoptions:{dataInit:function(cl){
    $(cl).validatebox({
        width:"100%",
        height:"80%",
        required:true,
        novalidate:true,
        validType:['balanceAmount','numMaxLength[10]']
    });
    $(cl).bind("change",function(e){
        if(!isNaN($(this).val())){
            var nv = $(this).val()+"";
            value = nv!=""?parseFloat(nv).toFixed(2)+'':"";
            $(this).val(value);
        }
    });
}}},
{name:'credit',label:'贷方金额',align:'center',width:"100px",
cellattr:function(rowId, val, rowObject, cm, rdata) {
    if(rowObject.source != undefined && rowObject.source != "") {
//                        // cm.editable = false;
//                        $("#billList").jqGrid('setCell', rowId, 'debit', 0, 'not-editable-cell');
}
},
editable:true,edittype:"text",editoptions:{dataInit:function(cl){
    $(cl).validatebox({
        width:"100%",
        height:"80%",
        required:true,
        novalidate:true,
        validType:['balanceAmount','numMaxLength[10]']
    });
    $(cl).bind("change",function(e){
        if(!isNaN($(this).val())){
            var nv = $(this).val()+"";
            value = nv!=""?parseFloat(nv).toFixed(2)+'':"";
            $(this).val(value);
        }
    });
}}},
{name:'balance',label:'余额',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
	if (cellvalue) {
		return parseFloat(cellvalue).toFixed(2);
	} else {
		return "";
	}
}},
{name:'memberName',label:'经手人',align:'center',width:"100px",
editable:true,
edittype:"text",
editoptions:{
    dataInit:function (cl) {
        var thisRowId = $(cl).closest("tr.jqgrow").attr("id");
//                        var row = $('#billList').jqGrid("getRowData",idx);

$(cl).fool('combogrid',{
    required:true,
    novalidate:true,
    width:160,
    height:31,
    idField:'fid',
    textField:'username',
    validType:"combogridValid['memberId']",
    panelWidth:450,
    panelHeight:283,
    fitColumns:true,
    focusShow:true,
    url:getRootPath()+'/member/vagueSearch?searchSize=8',
    columns:[[
    {field:'fid',title:'fid',hidden:true},
    {field:'userCode',title:'编号',width:100,searchKey:false},
    {field:'jobNumber',title:'工号',width:100,searchKey:false},
    {field:'username',title:'名称',width:100,searchKey:false},
    {field:'phoneOne',title:'电话',width:100,searchKey:false},
    {field:'deptName',title:'部门',width:100,searchKey:false},
    {field:'position',title:'职位',width:100,searchKey:false},
    {field:'searchKey',hidden:true,searchKey:true},
    ]],
    onSelect:function(idx,row){
//                                getEditor("memberId",index).val(row.fid);
getEditor("memberId",thisRowId).val(row.fid);
var tt = "";
//                                $("#memberId").val(row.fid);
//                                $("#memberName").val(row.username).focus();
},
onChange:function(nv,ov){
//                                getEditor("memberId",index).val(row.fid);
//                                $("#memberId").val('');
}
});
}
},
formatter:function(cellvalue, options, rowObject){
    return cellvalue?cellvalue.substring(0,10):"";
}},
<fool:tagOpt optCode="dabillAction">
{name:'action',label:'操作',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    if (!rowObject.editing) {
                    //var detail='<a class="btn-detail" title="修改" href="javascript:;" onclick="editById(\''+rowObject.fid+'\',1)"></a> ';
                    var d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+options.rowId+'\')"></a> ';
                    if(rowObject.fchecked!=1){
                        return d;
                    }else{
                        return "";
                    }
                } else {
                    var c = '<input type="button" style="border:0px;" class="btn-cancel" title="撤销" onclick="mycancel(\''+options.rowId+'\')" /> ';
                    var s = '<input type="button" style="border:0px;" class="btn-save" title="保存" onclick="mysave(\''+options.rowId+'\')" /> ';
                    return s+c;
                }
            }}
        </fool:tagOpt>
        ],
        onLoadSuccess:function(){//列表权限控制
          warehouseAll();
          <fool:tagOpt optCode="dabillAction1">//</fool:tagOpt>$('.btn-edit').remove();
			<fool:tagOpt optCode="dabillAction2">//</fool:tagOpt>$('.btn-detail').remove();
			<fool:tagOpt optCode="dabillAction3">//</fool:tagOpt>$('.btn-del').remove();
      	},
/*        gridComplete:function(){
            myIndex = $('#billList').jqGrid("getRowData").length+1;
        }*/
  }).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

    //清空按钮
    $("#clear-btn").click(function(){
        $("#search-form").form('clear');
        var inputs=$("#search-form").find(".dhxDiv");
        for(var i=0;i<inputs.length;i++){
            inputs[i].comboObj.setComboText(null);
        }
        $("#search-btn").click();
    });
    //筛选按钮
    $("#search-btn").click(function(){
        $('#subject').form('enableValidation');
        if($('#subject').form('validate')){
            var startDay=$("#search-startDay").datebox('getValue');
            var endDay=$("#search-endDay").datebox('getValue');
            var subjectId=$("#search-subjectId").val();
            var settlementTypeId=($("#search-settlementTypeName").next())[0].comboObj.getSelectedValue();
            var settlementNo=$("#search-settlementNo").textbox('getText');
            var direction=($("#search-direction").next())[0].comboObj.getSelectedValue();
            var fchecked=($("#search-checked").next())[0].comboObj.getSelectedValue();
            var options={startDate:startDay,endDate:endDay,subjectIds:subjectId,settlementTypeId:settlementTypeId,settlementNo:settlementNo,direction:direction,fchecked:fchecked};
            $("#billList").jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
        }
    });

    //新增按钮点击事件
    $('#add').click(function(){
        if($('#search-subjectId').val().match(",")){
            subjectCombo.setComboText("");
            subjectCombo.setComboValue("");
        }
        $(subjectCombo.getInput()).validatebox("enableValidation");
        if(!$(subjectCombo.getInput()).validatebox("isValid")){
            return false;
        }
        var ids = $("#billList").jqGrid('getDataIDs');
        var index = Math.max.apply(null,ids) + 1;
        if (index == "-Infinity"){
            index = "1";
        }
        $("#billList").jqGrid("addRowData",index,{newRow:1},"first");
        myedit(index);
        getEditor("subjectId",index).val($('#search-subjectId').val());
        getText("subjectName",index).text(subjectCombo.getComboText());
    });

    function getEditor(name,index){
      return $('#billList #'+index+"_"+name);
  }
  function getText(name,index){
      return $('#billList #'+index+" td[aria-describedby=billList_"+name+"]");
  }

  function myedit(index){
    var row = $("#billList").jqGrid("getRowData",index);
    var source = row.source;
      if(row.fchecked == "已勾对"){
            $.fool.alert({
                msg:'已勾对的数据不能修改！',time:1000
            });
            return ;
      }

    $("#billList").jqGrid("editRow",index);
    $("#billList").jqGrid("setRowData",index,{editing:true,action:null});
        //借贷方金额操作
        var $debit = getEditor("debit",index);
        var $credit = getEditor("credit",index);
        $debit.keyup(function(){
            if($debit.val()!=0){
                $credit.val(0);
                $credit.attr("disabled",true);
                $credit.validatebox("validate");
            }else{
                $credit.val("");
                $credit.removeAttr("disabled");
                $credit.validatebox("validate");
            }
        });
        $credit.keyup(function(){
            if($credit.val()!=0){
                $debit.val(0);
                $debit.attr("disabled",true);
                $debit.validatebox("validate");
            }else{
                $debit.val("");
                $debit.removeAttr("disabled");
                $debit.validatebox("validate");
            }
        });
        if(row.newRow!=1){
            getEditor("voucherDate",index).datebox("setValue",row.voucherDate);
            getEditor("settlementTypeName",index).next()[0].comboObj.setComboValue(row.settlementTypeId);
            getEditor("resume",index).next()[0].comboObj.setComboText(row.resume);
            if(row.debit==0||row.debit==""){
                $debit.val(0);
                $debit.attr("disabled",true);
            }
            if(row.credit==0||row.credit==""){
                $credit.val(0);
                $credit.attr("disabled",true);
            }
        }
        if(row.source){
        	var tr = $debit.closest("tr.jqgrow");
        	var inputs=tr.find(".dhxDiv");
          for(var i=0;i<inputs.length;i++){
             if($(inputs[i]).parent().attr("aria-describedby")!="billList_resume"){
                 $(inputs[i])[0].comboObj.disable(true);
             }
         } 
         tr.find("input.textbox-f").textbox("disable");
         tr.find("td[aria-describedby!=billList_resume] input[type!=button]").css("background","#ededed");
         tr.find("td[aria-describedby!=billList_resume] input[type!=button]").attr("disabled","disabled");
     }
 }

 function mycancel(index){
     $("#billList #"+index).form("disableValidation");
     var row = $("#billList").jqGrid("getRowData",index);
     $("#billList").jqGrid("restoreRow",index);
    if(row.newRow == 1){
        $("#billList").jqGrid("delRowData",index);
    }else{
        $("#billList").jqGrid("setRowData",index,{fid:row.fid,editing:false,action:null});
    }
}
function mysave(index){
    $("#billList #"+index).form("enableValidation");
    if(!$("#billList #"+index).form("validate")){
        return false;
    }
    var row = $("#billList").jqGrid("getRowData",index);
    /*var fid = row.fid?row.fid:"";*/
    var fid = row.fid;//看下这里
    var subjectId = getEditor("subjectId",index).val();
    var subjectName = getText("subjectName",index).text();
    var settlementDate = getEditor("settlementDate",index).datebox("getValue");
    var voucherDate = getEditor("voucherDate",index).datebox("getValue");
    var orderno = getEditor("orderno",index).textbox("getValue");

    getEditor("settlementTypeId",index).val(getEditor("settlementTypeName",index).next()[0].comboObj.getSelectedValue());
    var settlementTypeId = getEditor("settlementTypeId",index).val();
    var settlementNo = getEditor("settlementNo",index).textbox("getValue");
    var settlementTypeName = getEditor("settlementTypeName",index).next()[0].comboObj.getComboText();
        var memberId = getEditor("memberId",index).val();//getEditor("memberName",index).val();
        var memberName = getEditor("memberName",index).val();

        var resume = getEditor("resume",index).next()[0].comboObj.getComboText();
        var debit = $.trim(getEditor("debit",index).val());
        var credit = $.trim(getEditor("credit",index).val());
        var updateTime = getText("updateTime",index).text();
        var type = 3;

        $('#save').attr("disabled","disabled");
        var mydata = {type:type,updateTime:updateTime,fid:fid,subjectId:subjectId,
            subjectName:subjectName,settlementDate:settlementDate,settlementTypeName:settlementTypeName,
            settlementTypeId:settlementTypeId,resume:resume,voucherDate:voucherDate,
            orderno:orderno,settlementNo:settlementNo,debit:debit,credit:credit,
            memberName:memberName,memberId:memberId};
            $.post('${ctx}/cashierBankBillController/save',mydata,function(data){
                dataDispose(data);
                if(data.returnCode =='0'){
                    $.fool.alert({time:1000,msg:"保存成功！",fn:function(){
                        var obj = data.data[0];
                    //$(document).unbind('keydown',cashkd);
                    $('#save').removeAttr("disabled");
                    $("#billList").jqGrid("saveRow",index);
                    $("#billList").jqGrid("setRowData",index,{
                        newRow:0,
                        editing:false,
                        action:null,
                        fid:obj.fid,
                        updateTime:obj.updateTime,
                        settlementTypeName:obj.settlementTypeName,
                        settlementDate:obj.settlementDate,
                        resume:obj.resume,
                        subjectName:obj.subjectName,
                        debit:obj.debit,
                        voucherDate:obj.voucherDate,
                        memberName:obj.memberName,
                        fchecked:obj.fchecked,
                        balance:obj.balance
                    });
                }});
                }else if(data.returnCode ==1){
                    $.fool.alert({msg:data.message,fn:function(){
                        $('#save').removeAttr("disabled");
                    }});
                }else{
                    $.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
                        $('#save').removeAttr("disabled");
                    }});
                }
            });
        }

    //编辑按钮点击事件
    /* function editById(fid,mark){
      $('#addBox').window({
            top:150+$(window).scrollTop(),
            left:100,
            onClose:function(){
                $("html").css("overflow","");
                if(typeof(cashkd) != "undefined"){
                    $(document).unbind('keydown',cashkd);
                }
            },
            onOpen:function(){
                $("html").css("overflow","hidden");
            }
        });
      if(mark==1){
          $('#addBox').window('setTitle',"查看银行日记账单");
      }else{
          $('#addBox').window('setTitle',"修改银行日记账单");
      }
      $('#addBox').window('open');
      $('#addBox').window('refresh',"${ctx}/cashierBankBillController/editBankBill?type=3&fid="+fid);
  } */
  
  //删除按钮点击事件
  function removeById(rowid){
      var rowData = $("#billList").jqGrid("getRowData",rowid);
   $.fool.confirm({title:'确认',msg:'确定要删除此数据吗？',fn:function(r){
       if(r){
           $.ajax({
            type : 'post',
            url : '${ctx}/cashierBankBillController/delete',
            data : {fid : rowData.fid},
            dataType : 'json',
            success : function(data) {
                dataDispose(data);
                if(data.returnCode == '0'){
                    $.fool.alert({time:1000,msg:'删除成功！',fn:function(){
                        $("#billList").jqGrid("delRowData",rowid);
                        var ids = $("#billList").jqGrid("getDataIDs").length;
                        if(ids == 0){
                            $("#billList").trigger("reloadGrid");
                        }
                    }});
                }else{
                    $.fool.alert({time:1000,msg:data.message,fn:function(){
                        $("#billList").trigger("reloadGrid");
                    }});
                }
            },
            error:function(){
                $.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
            }
        });
       }
   }});
}

    //科目弹出框
    function searchSubjectName(){
        chooserWindow_search = $.fool.window({
            width: 780,
            height: 480,
            'title': "选择科目",
            href:' ${ctx}/fiscalSubject/window?okCallBack=selectSubjectSearch&onDblClick=selectSubjectDBCSearch&bankSubject=1&flag=1',
            onLoad:function(){(
                $('#search-subjectName').next())[0].comboObj.closeAll();}
        });
        //解决IE点击弹窗下拉框不消失问题
    }

    function selectSubjectSearch(rowData){
        if(rowData[0].flag!=1){
            $.fool.alert({time:1000,msg:"请选择子节点"});
            return false;
        }
        var fids="";
        var names="";
        if(rowData.length>1){
            for(var i=0;i<rowData.length;i++){
                fids+=rowData[i].fid+",";
                names+=rowData[i].name+",";
            }
        } else {
            fids=rowData[0].fid;
            names=rowData[0].name;
        }
        ($('#search-subjectName').next())[0].comboObj.setComboValue(fids);
        ($('#search-subjectName').next())[0].comboObj.setComboText(names);
        $("#search-subjectId").val(fids);
        chooserWindow_search.window('close');
        $($('#search-subjectName').next()[0].comboObj.getInput()).focus().blur();
    }

    function selectSubjectDBCSearch(rowData){
       if(rowData.flag!=1){
          $.fool.alert({time:1000,msg:"请选择子节点"});
          return false;
      }
        ($('#search-subjectName').next())[0].comboObj.setComboValue(rowData.fid);
        ($('#search-subjectName').next())[0].comboObj.setComboText(rowData.name);
      /* $("#search-subjectName").combobox("setValue",rowData.name); */
      $("#search-subjectId").val(rowData.fid);
      chooserWindow_search.window('close');
      $($('#search-subjectName').next()[0].comboObj.getInput()).focus().blur();
  }

  function getComboData(url,mark){
      var result="";
      $.ajax({
         url:url,
         data:{num:Math.random()},
         async:false,
         success:function(data){
            if(data){
               result=data;
           }
       }
   });
      if (mark != "tree") {
         return formatData(result,"fid");
     } else {
         return formatTree(result[0].children,"text","id");
     }
 }
  
  function getTotal(){
	  var rows=$('#billList').jqGrid('getRowData');
	  var debit=0;
	  var credit=0;
	  var balance=0;
	  for(var i=0;i<rows.length;i++){
		  if(rows[i].action.search(/editing-on/) != -1){
			  continue;
		  }
		  if("undefined" != typeof rows[i].debit&&rows[i].debit!=""){
			  debit += parseFloat(rows[i].debit);
		  }
		  if("undefined" != typeof rows[i].credit&&rows[i].credit!=""){
			  credit += parseFloat(rows[i].credit);
		  }
		  if("undefined" != typeof rows[i].balance&&rows[i].balance!=""){
			  balance += parseFloat(rows[i].balance);
		  }
	  }
	  $('#billList').footerData("set",{fchecked:"合计",debit:debit.toFixed(2),credit:credit.toFixed(2),balance:balance.toFixed(2)},false);
  }
  
  
  $('#print').click(function(){
	  	var q = rowChos.length;
	  	if(q == 0){
	  		alert("请选择需要打印的项！");
	  		return false;

	  	}
	  	var arrDebit = [];
	  	var arrCredit = [];
	  	for(var i=0; i<rowChos.length; i++){
	  		if (rowChos[i].debit != 0) {
	  			arrDebit.push(rowChos[i].debit)
	  		}
	  		if (rowChos[i].credit != 0) {
	  			arrCredit.push(rowChos[i].credit)
	  		}
	  	}
	  	if(arrDebit.length > 0 && arrCredit.length > 0) {
	  		alert("不能同时选择余额方向不同的项！");
	  		return false;
	  	}
	  	var pageRow = 6;
	  	texts = [
	  	{title:'单据日期',key:'voucherDate'},
	  	];

	  	thead = [
	  	{title:'银行账户',key:'subjectName'},
	  	{title:'摘要',key:'resume'},
	  	{title:'金额',key:'amount'},
	  	];
	  	$.ajax({
	  		url:"${ctx}/payBill/findTemp?code=skd&num="+Math.random(),
	  		async:false,
	  		data:{},
	  		success:function(data){
	  			if(data){
	  				pageRow = parseInt(data.message) > 14 ? 14 : parseInt(data.message);
	  				var nullRow = q%pageRow != 0?pageRow - q%pageRow:0;
	  				var ids = [];
	  				for(var i=0; i<rowChos.length; i++){
	  					ids.push(rowChos[i].fid);
	  				}

	  				var billIds = ids.join(',');
	  				printHandle(getRootPath()+"/cashierBankBillController/print/batch/rjzd?mergeTotal=0&mergeSize=5&nullRow=0&billIds="+billIds,0,7);
	  			}else{
	  				$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
	  					return false;
	  				}});
	  			}
	  		}
	  	});
  });
</script>
</body>
</html>
