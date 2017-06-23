<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>货品库存</title>
	<%@ include file="/WEB-INF/views/common/header.jsp"%>
</head>
<body>

<div class="titleCustom">
	<div class="squareIcon">
		<span class='Icon'></span>
		<div class="trian"></div>
		<h1>货品库存</h1>
	</div>
</div>

<div class="tool-box">
	<div class="tool-box-pane">
		<fool:tagOpt optCode="goodsStockExport">
			<a href="javascript:;" id="export" class="btn-ora-export">导出</a>
		</fool:tagOpt>
		<!-- <a href="javascript:;" class="btn-ora-printer">打印</a> -->
	</div>
	<div class="tool-box-pane">
		<form action="" id="search-form" >
			<input id="periodId" name="periodId" />
			<input id="warehouseId" name="warehouseId" type="hidden"/>
			<input id="warehouseName" />
			<input id="goodsName" />
			<input id="goodsId" name="goodsId" type="hidden"/>
			<input id="specName" />
			<input id="goodsSpecId" name="goodsSpecId" type="hidden"/>
			<input id="calTotal" name="calTotal" type="hidden" value="0"/>
			<input id="calTotals" />
			<fool:tagOpt optCode="goodsStockSearch">
				<a href="javascript:;" class="btn-blue btn-s go-search">查询</a>
			</fool:tagOpt>
			<a href="javascript:;" class="btn-blue btn-s go-clear">清空</a>
		</form>
	</div>
</div>

<table id="dataTable" ></table>
<div id="pager"></div>

<div id="my-window"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
    var groupId = "";
    $(function(){
        $("#goodsName").textbox({
            prompt:'选择货品',
            width:100,
            height:30
        });
		/* $("#goodsName").fool("dhxComboGrid",{
		 prompt:"选择货品",
		 height:30,
		 width:100,
		 setTemplate:{
		 input:"#name#",
		 columns:[
		 {option:'#name#',header:'名称',width:100},
		 {option:'#code#',header:'编码',width:100},
		 {option:'#barCode#',header:'条码',width:100},
		 ],
		 },
		 filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=6',
		 data:getComboData(getRootPath()+'/goods/vagueSearch?searchSize=6'),
		 searchKey:"searchKey",
		 focusShow:true,
		 onSelectionChange:function(){
		 $("#goodsId").val(($("#goodsName").next())[0].comboObj.getSelectedValue());
		 }
		 }); */

        $("#specName").fool("dhxComboGrid",{
            prompt:"选择货品属性",
            height:30,
            width:167,
            setTemplate:{
                input:"#name#",
                columns:[
                    {option:'#name#',header:'名称',width:100},
                    {option:'#code#',header:'编码',width:100},
                ],
            },
            toolsBar:{
                refresh:true
            },
            filterUrl:getRootPath()+'/goodsspec/vagueSearch?searchSize=6',
            data:getComboData(getRootPath()+'/goodsspec/vagueSearch?searchSize=6'),
            searchKey:"searchKey",
            focusShow:true,
            onSelectionChange:function(){
                $("#goodsSpecId").val(($("#specName").next())[0].comboObj.getSelectedValue());
            }
        });
		/* $("#specName").textbox({
		 prompt:'选择属性',
		 width:100,
		 height:30
		 }); */

        $("#warehouseName").fool("dhxCombo",{
            prompt:'仓库',
            width:150,
            height:31,
            editable:false,
			/* required:true,
			 novalidate:true, */
            setTemplate:{
                input:"#name#",
                option:"#name#"
            },
            toolsBar:{
                name:"仓库",
                refresh:true
            },
            focusShow:true,
            data:getComboData('${ctx}/basedata/warehourseList',"tree"),
            onSelectionChange:function(){
                var warehouseId = '';
                var node=($("#warehouseName").next())[0].comboObj.getSelectedText();
                warehouseId = node.id;
                $('#warehouseId').val(warehouseId);
            },
			/* onLoadSuccess:function(){
			 ($("#warehouseName").next())[0].comboObj.deleteOption("")
			 }, */
        });

        $("#calTotals").fool("dhxCombo",{
            prompt:'计算总仓/分仓',
            width:150,
            height:31,
            editable:false,
            setTemplate:{
                input:"#name#",
                option:"#name#"
            },
            focusShow:true,
            data: [{
                text: '计算分仓',
                value: '0',
                selected:true
            },{
                text: '计算总仓',
                value: '1'
            }],
            clearOpt:false,
            onChange:function(value,text){
                $("#calTotal").val(($("#calTotals").next())[0].comboObj.getSelectedValue());
                var options = $("#search-form").serializeJson();
                typeof options.recordStatus == "undefined"?options.recordStatus = "":null;//由于取消选择状态后，options没有录入参数recordStatus，但jqGrid的GridParam依然存在，所以设置一下
                typeof options.enable == "undefined"?options.enable = "":null;
                if(value == 1){
                    $('#dataTable').jqGrid('setGridParam',{
                        datatype:function(postdata){
                            $.ajax({
                                url: '${ctx}/periodAmount/query',
                                data:postdata,
                                dataType:"json",
                                complete: function(data,stat){
                                    if(stat=="success") {
                                        data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
                                        $("#dataTable")[0].addJSONData(data.responseJSON);
                                    }
                                }
                            });
                        },
                        postData:options}).trigger("reloadGrid");
                }else{
                    $('#dataTable').jqGrid('setGridParam',{
                        datatype:function(postdata){
                            $.ajax({
                                url: '${ctx}/periodStockAmount/query',
                                data:postdata,
                                dataType:"json",
                                complete: function(data,stat){
                                    if(stat=="success") {
                                        data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
                                        $("#dataTable")[0].addJSONData(data.responseJSON);
                                    }
                                }
                            });
                        },
                        postData:options}).trigger("reloadGrid");
                }
            }
        });
        $("#goodsName").textbox('textbox').click(function(){
            chooserWindow=$.fool.window({'title':"选择货品",href:'${ctx}/goods/window?okCallBack=selectGoods&onDblClick=selectGoodsDBC&addKey=1'});
        });

		/* $("#specName").textbox('textbox').click(function(){
		 chooserWindow=$.fool.window({'title':"选择属性",href:'${ctx}/goodsspec/window?okCallBack=selectSpec&onDblClick=selectSpecDBC&singleSelect=true&groupId='+groupId});
		 }); */

        $(".go-search").click(function(){
            var bid = ($("#periodId").next())[0].comboObj.getSelectedValue();
            if(bid==undefined || bid.length==0){
                $.fool.alert({msg:'会计期间不能为空'});
            }else{
                refreshData();
            }
            return false;
        });

        $(".go-clear").click(function(){
			/* $("#periodId").next()[0].comboObj.setComboText(""); */
            ($('#warehouseName').next())[0].comboObj.setComboValue("");
            ($('#warehouseName').next())[0].comboObj.setComboText("");
            $('#warehouseId').val("");
            $('#goodsName').textbox("clear");
            $('#goodsId').val("");
            ($('#specName').next())[0].comboObj.setComboText("");
            ($('#specName').next())[0].comboObj.setComboValue("");
            ($('#specName').next())[0].comboObj.enable();
            $('#goodsSpecId').val("");
        });

        var periodCombo=$('#periodId').fool("dhxCombo",{
            prompt:'会计期间',
            width:150,
            height:31,
            editable:false,
            required:true,
            missingMessage:'该项不能为空！',
            novalidate:true,
            setTemplate:{
                input:"#period#",
                option:"#period#"
            },
            toolsBar:{
                name:"会计期间",
                refresh:true
            },
            data:getComboData('${ctx}/report/profitSheet/queryPeriods'),
            focusShow:true,
            onLoadSuccess:function(){
				/*($("#periodId").next())[0].comboObj.selectOption(1);*/
                ($("#periodId").next())[0].comboObj.deleteOption("")
            },
        });

        //comboTree($("#warehouseId"),"${ctx}/basedata/warehourseList",true,function(){},'仓库');

        $("#export").click(function(){
            var periodId = $("#periodId").next()[0].comboObj.getSelectedValue();
            if(periodId==undefined || periodId.length==0){
                $.fool.alert({msg:'会计期间不能为空'});
            }else{
                var goodsId = $('#goodsId').val();
                var node = ($("#warehouseName").next())[0].comboObj.getSelectedText(); // 得到选择的节点
                var calTotal=$("#calTotal").val();
                var warehouseId = '';
                if(node){
                    warehouseId = node.fid;
                }
                var specId = $('#goodsSpecId').val();
                url="${ctx}/periodStockAmount/export?goodsId="+goodsId+"&warehouseId="+warehouseId+"&goodsSpecId="+specId+"&periodId="+periodId;
                if(calTotal==1){
                    url = "${ctx}/periodAmount/export?goodsId="+goodsId+"&warehouseId="+warehouseId+"&goodsSpecId="+specId+"&periodId="+periodId;
                }
                window.location.href=url;
            }
        });

        showGoodsStock();

		/* setPager($("#dataTable"));	 */
    });

    function showGoodsStock(){
        $("#dataTable").jqGrid({
            datatype:function(postdata){
                $.ajax({
                    url: '${ctx}/periodStockAmount/query',
                    data:postdata,
                    dataType:"json",
                    complete: function(data,stat){
                        if(stat=="success") {
                            data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
                            $("#dataTable")[0].addJSONData(data.responseJSON);
                        }
                    }
                });
            },
// 		postData:{periodId:($('#periodId').next())[0].comboObj.getSelectedValue()},
            autowidth:true,
            height:$(window).height()-180,
            shrinkToFit :false,
            pager:"#pager",
            rowNum:10,
            rowList:[10,20,30],
            viewrecords:true,
            jsonReader:{
                records:"total",
                total: "totalpages",
            },
            colModel:[
                {name : 'warehouseName',label : '仓库',align:'center',width:"100px",frozen:true},
                {name : 'goodsName',label : '货品名称',align:'center',width:"100px",frozen:true},
                  {name : 'goodsCode',label : '货品编号',align:'center',width:"100px",frozen:true},
// 		          {name : 'goodsSpec',label : '货品型号',align:'center',width:"100px"},
                {name : 'goodsSpecName',label : '货品属性',align:'center',width:"100px"},
//                   {name : 'unitPrice',label : '货品平均单价',align:'center',width:"100px"},
                {name : 'accountUnitName',label : '计量单位',align:'center',width:"100px"},
                {name : 'preQuentity',label : '上期结余数',align:'center',width:"100px"},
                {name : 'preAmount',label : '上期结余金额',align:'center',width:"100px"},
                {name : 'inQuentity',label : '本期入库数',align:'center',width:"100px"},
                {name : 'inAmount',label : '本期入库金额',align:'center',width:"100px"},
                {name : 'outQuentity',label : '本期出库数',align:'center',width:"100px"},
                {name : 'outAmount',label : '本期出库金额',align:'center',width:"100px"},
                {name : 'moveInQuentity',label : '调入数',align:'center',width:"100px"},
                {name : 'moveOutQuentity',label : '调出数',align:'center',width:"100px"},
                {name : 'movePrice',label : '调仓单价',align:'center',width:"100px"},
                {name : 'profitQuentity',label : '本期盘点盈亏数',align:'center',width:"100px"},
                {name : 'profitAmount',label : '本期盘点盈亏金额',align:'center',width:"100px"},
                {name : 'accountQuentity',label : '本期结存数',align:'center',width:"100px"},
                {name : 'accountAmount',label : '本期结存金额',align:'center',width:"100px"},
                {name : 'accountPrice',label : '本期结存单价',align:'center',width:"100px"},
                {name : 'purchaseQuantity',label : '采购入库数量',align:'center',width:"100px"},
                {name : 'purchaseAmount',label : '采购入库金额',align:'center',width:"100px"},
                {name : 'purchaseReturnQuantity',label : '采购退货数量',align:'center',width:"100px"},
                {name : 'purchaseReturnAmount',label : '采购退货金额',align:'center',width:"100px"},
                {name : 'materialsQuantity',label : '生产领料数量',align:'center',width:"100px"},
                {name : 'materialsAmount',label : '生产领料金额',align:'center',width:"100px"},
                {name : 'materialsReturnQuantity',label : '生产退料数量',align:'center',width:"100px"},
                {name : 'materialsReturnAmount',label : '生产退料金额',align:'center',width:"100px"},
                {name : 'productQuantity',label : '成品入库数量',align:'center',width:"100px"},
                {name : 'productAmount',label : '成品入库金额',align:'center',width:"100px"},
                {name : 'productReturnQuantity',label : '成品退库数量',align:'center',width:"100px"},
                {name : 'productReturnAmount',label : '成品退库金额',align:'center',width:"100px"},
                {name : 'saleQuantity',label : '销售出货数量',align:'center',width:"100px"},
                {name : 'saleAmount',label : '销售出货金额',align:'center',width:"100px"},
                {name : 'saleReturnQuantity',label : '销售退货数量',align:'center',width:"100px"},
                {name : 'saleReturnAmount',label : '销售退货金额',align:'center',width:"100px"},
                {name : 'lossQuantity',label : '报损数量',align:'center',width:"100px"},
                {name : 'lossAmount',label : '报损金额',align:'center',width:"100px"},
                {name : 'transportOutQuantity',label : '发货单数量',align:'center',width:"100px"},
                {name : 'transportOutAmount',label : '发货单金额',align:'center',width:"100px"},
                {name : 'transportInQuantity',label : '收货单数量',align:'center',width:"100px"},
                {name : 'transportInAmount',label : '收货单金额',align:'center',width:"100px"},
                {name : 'moveOutAmount',label : '调出金额',align:'center',width:"100px"},
                {name : 'moveInAmount',label : '调入金额',align:'center',width:"100px"},
                {name : 'prePrice',label : '上期结存单价',align:'center',width:"100px"},
                {name : 'periodId',label : 'periodId',hidden: true},
                {name : 'goodsId',label : 'goodsId',hidden: true},
                {name : 'goodsSpecId',label : 'goodsSpecId',hidden: true},
                {name : 'warehouseId',label : 'warehouseId',hidden: true},
            ],
            ondblClickRow:function(rowid,iRow,iCol,e){
                var rowData=$("#dataTable").jqGrid("getRowData",rowid);
                var goodsName = rowData.goodsName;
                var goodsSpec = rowData.spec;
                var showTitle = goodsName;
                var periodId = $('#periodId').next()[0].comboObj.getSelectedValue();
                if(goodsSpec!=undefined && goodsSpec!=''){
                    showTitle = showTitle + ' + ' + goodsSpec;
                }
                showDetail(showTitle,periodId,rowData.goodsId,rowData.goodsSpecId,rowData.warehouseId,rowData.accountPrice,rowData.accountQuentity,rowData.preQuentity);
            },
            gridComplete:function(){
                if(($("#calTotals").next())[0].comboObj.getSelectedValue()==0){
                    $("#dataTable").jqGrid('destroyFrozenColumns');
                    $('#dataTable').showCol('warehouseName');
                    $('#dataTable').showCol('moveIn');
                    $('#dataTable').showCol('moveOut');
                    ($('#warehouseName').next())[0].comboObj.enable();
                    var node = ($('#warehouseName').next())[0].comboObj.getSelectedText();
                    var warehouseId = '';
                    if(node){
                        warehouseId = node.fid;
                    }
                    $('#warehouseId').val(warehouseId);
                }else{
                    $("#dataTable").jqGrid('destroyFrozenColumns');
                    $('#dataTable').hideCol('warehouseName');
                    $('#dataTable').hideCol('moveIn');
                    $('#dataTable').hideCol('moveOut');
                    ($('#warehouseName').next())[0].comboObj.disable();
                    $('#warehouseId').val("");
                }
                $("#dataTable").jqGrid('setFrozenColumns');
            }
        }).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
    }

    var chooserWindow="";
    function selectGoods(rowData,notclose){//修改货品弹窗为多选，多选的时候禁用属性输入
        var ids = [];
        var names = [];
        for(var i in rowData){
            ids.push(rowData[i].fid);
            names.push(rowData[i].name);
        }
        var gid = $('#goodsId').val();
        var gname = $('#goodsName').textbox("getValue");
        gid!=""?$('#goodsId').val(gid+","+ids):$('#goodsId').val(ids);
        gname!=""?$('#goodsName').textbox('setValue',gname+","+names):$('#goodsName').textbox('setValue',names);
        if(rowData.length>1){
            ($('#specName').next())[0].comboObj.setComboText("");
            ($('#specName').next())[0].comboObj.setComboValue("");
            ($('#specName').next())[0].comboObj.disable();
			/* $('#specName').textbox("clear").textbox("disable"); */
        }else{
            groupId = rowData[0].goodsSpecGroupId;
            ($('#specName').next())[0].comboObj.enable();
            ($('#specName').next())[0].comboObj.clearAll();
            if(groupId){
                //($('#specName').next())[0].comboObj.addOption();
                $("#specName").fool("dhxComboGrid",{
                    prompt:"选择货品属性",
                    height:30,
                    width:167,
                    setTemplate:{
                        input:"#name#",
                        columns:[
                            {option:'#name#',header:'名称',width:100},
                            {option:'#code#',header:'编码',width:100},
                        ],
                    },
                    toolsBar:{
                        refresh:true
                    },
                    filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+groupId+'&searchSize=6',
                    data:getComboData("${ctx}/goodsspec/vagueSearch?parentId="+groupId),
                    searchKey:"searchKey",
                    focusShow:true,
                    onSelectionChange:function(){
                        $("#goodsSpecId").val(($("#specName").next())[0].comboObj.getSelectedValue());
                    }
                });
            }else{
                ($('#specName').next())[0].comboObj.disable();
            }
        }
        typeof notclose !="undefined" && notclose == true?null:chooserWindow.window('close');
    }
    function selectGoodsDBC(rowData){
        $('#goodsId').val(rowData.fid);
        $('#goodsName').textbox('setValue',rowData.name);
        groupId = rowData.goodsSpecGroupId;
        ($('#specName').next())[0].comboObj.enable();
        ($('#specName').next())[0].comboObj.clearAll();
        if(groupId){
            $("#specName").fool("dhxComboGrid",{
                prompt:"选择货品属性",
                height:30,
                width:167,
                setTemplate:{
                    input:"#name#",
                    columns:[
                        {option:'#name#',header:'名称',width:100},
                        {option:'#code#',header:'编码',width:100},
                    ],
                },
                toolsBar:{
                    refresh:true
                },
                filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+groupId+'&searchSize=6',
                data:getComboData("${ctx}/goodsspec/vagueSearch?parentId="+groupId),
                searchKey:"searchKey",
                focusShow:true,
                onSelectionChange:function(){
                    $("#goodsSpecId").val(($("#specName").next())[0].comboObj.getSelectedValue());
                }
            });
        }else{
            ($('#specName').next())[0].comboObj.disable();
        }
        chooserWindow.window('close');
    }

	/* function selectSpec(rowData){
	 $('#specId').val(rowData[0].fid);
	 $('#specName').textbox('setValue',rowData[0].name);
	 chooserWindow.window('close');
	 }
	 function selectSpecDBC(rowData){
	 $('#specId').val(rowData.fid);
	 $('#specName').textbox('setValue',rowData.name);
	 chooserWindow.window('close');
	 } */

    function showDetail(showTitle,periodId,goodsId,goodsSpecId,warehouseId,accountPrice,accountQuentity,preQuentity){
        if(goodsSpecId==undefined)goodsSpecId='';
        $("#my-window").window({
                top:80,
                modal:true,
                collapsible:false,
                minimizable:false,
                maximizable:false,
                resizable:false,
                title:showTitle,
                height:450,
                width:930,
				//这个接口需要用的属性name为specId其他的为goodsSpecId
                href:getRootPath()+'/report/goodsStock/window?periodId='+periodId+'&goodsId='+goodsId+'&specId='
                +goodsSpecId+'&warehouseId='+warehouseId+'&accountPrice='+accountPrice+'&accountQuentity='+accountQuentity+'&lastAccountQuentity='+preQuentity
            }
        );
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
        if(mark!="tree"){
            return formatData(result,"fid");
        }else{
            return formatTree(result[0].children,"text","id");
        }
    }
</script>