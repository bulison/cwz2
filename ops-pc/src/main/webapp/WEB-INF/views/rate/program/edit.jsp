<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收益率方案明细</title>
<style>
	.dataBox{
		margin-bottom:10px !important;
	}
	.caclBox{
		margin-bottom:50px;
	}
	.caclBox h2{
		font:800 14px 宋体  !important;
		color:#ffc96c;
		margin-left:10px;
		display: inline-block;
	}
	.caclBox p{
		font-weight:600;
	}
	.mybtn-s{
		width:56px;
	}
</style>
<form action="" class="bill-form myform" id="programform">
	<div class="title">
		<div class="title1 shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div class="square1"><span class="backBtn"></span><div class="triangle"></div></div><h1>${param.mark == 1?'编辑':'新增' }收益率方案</h1>
		</div>
	</div>
	<div class="bill shadow" style="margin-top:50px;">
		<div class="in-box list2">
			<input name="fid" id="fid" value="${rateProgramme.fid}" type='hidden'/>
			<input id="creatorId" value="${rateProgramme.creatorId}" type="hidden"/>

			<p><font>方案名称：</font><input name="name" id="name" class="easyui-validatebox textBox" data-options="{required:true,novalidate:true}" value="${rateProgramme.name}"/></p>
			<p><font>创建人：</font><input id="creatorName" class="textBox" value="${rateProgramme.creatorName}" disabled="disabled"/></p>
			<p><font>创建时间：</font><input id="createTime" class="textBox" value="${rateProgramme.createTime}" disabled="disabled"/></p>

			<p><font>利润：</font><input name="profit"  id="profit" class="easyui-validatebox textBox" disabled value="${rateProgramme.profit}"/></p>
			<p><font>利润率：</font><input id="profitRate" name="profitRate" class="textBox" disabled value="${rateProgramme.profitRate}"/></p>
			<p><font>周期收益率：</font><input id="cycleProfitRate" name="cycleProfitRate" class="textBox" disabled value="${empty rateProgramme.cycleProfitRate?'0':rateProgramme.cycleProfitRate}"/></p>
		</div>
	</div>
</form>
<div class="shadow dataBox">
	<div class="in-box list1">
		<table id="detailsList"></table>
	</div>
</div>
<div class="shadow caclBox" style="display:none;">
	<h2>计算结果</h2><br/>
	<p><font>即时收益率：</font><font id="realRate"></font></p><br/>
	<!-- <p><font>预期收益率：</font><font id="expectedRate"></font></p><br/>
    <p><font>市场参考收益率：</font><font id="marketRate"></font></p><br/>
    <p><font>实际收益率：</font><font id="actualRate"></font></p><br/> -->
	<!-- <p><font>当前收益率：</font><font id="currentRate"></font></p><br/> -->
	<p><font>月收益率：</font><font id="cycleRate"></font></p>
</div>



<div class="mybtn-footer">
	<input class="mybtn-blue mybtn-s" value="保存" id="mysave" onclick="saveData()" />
	<input class="mybtn-blue mybtn-s" value="计算" id="mycacl" />
</div>
<div id="pop-win"></div>
<script type="text/javascript">
    var _time = '${rateProgramme.createTime}';
    if(_time!=""){
        $('#createTime').val(_time.substring(0,_time.indexOf('.')));
    }
    var details = '${rateProgramme.details}';
    if("${param.mark}" != 1){
        $('#mycacl').hide();
    }else if("${param.mark}" == 1){
        $('#profit').val(parseFloat($('#profit').val()).toFixed(2));
        $('#profitRate').val(parseFloat($('#profitRate').val()).toFixed(2));
        $('#cycleProfitRate').val(parseFloat($('#cycleProfitRate').val()).toFixed(2));
    }

    $('.backBtn').click(function(){
        if($('#fid').val()!="")
            caculate($('#fid').val());
        $('#addBox').window('close');
        $('#programList').trigger('reloadGrid');
    });
    $('#mycacl').click(function(){
        caculate($('#fid').val());
    });
    //屏蔽默认快捷键
    $(document).keydown(function(e){
        if(e.ctrlKey && e.keyCode == 83){//ctrl+s
            if (e.preventDefault) {  // firefox
                e.preventDefault();
            } else { // other
                e.returnValue = false;
            }
        }
    });

    var programkd = '';//ctrl快捷键全局变量
    //编辑页面保存按钮快捷键
    $(document).keydown(plankd=function(e){
        if(e.ctrlKey && e.keyCode == 83){//ctrl+s
            $('#mysave').click();
        }
    });

    $('#detailsList').jqGrid({
        width:$(".dataBox").width()*0.95,
        height:"100%",
        footerrow: true,
        datatype:"local",
        forceFit:true,//改变列宽度，总宽度不变
        colModel:[
            {name:"action",label:'操作',align:"center",width:80,sortable:false,formatter:function(value,options,row){
                var index = options.rowId;
                if(value == "new"){
                    return '<a href="javascript:;" onclick="addRow()" class="btn-add" title="新增"></a>';
                }else{
                    var e = '<a href="javascript:;" class="btn-edit" onclick="editRow(\''+index+'\')" title="修改"></a>';
                    var d = '<a href="javascript:;" class="btn-del" onclick="delRow(\''+index+'\')" title="删除"></a>';
                    var c = '<a href="javascript:;" class="btn-cancel" onclick="cancelRow(\''+index+'\')" title="撤销"></a>';
                    var s = '<a href="javascript:;" class="btn-save" onclick="saveRow(\''+index+'\')" title="确认"></a>';
                    if(row.editing){
                        return s+c;
                    }else{
                        return e+d;
                    }
                }
            }},
            {name:"fid",label:'fid',hidden:true},
            {name:"rateProgrammeId",label:'rateProgrammeId',hidden:true},
            {name:"newRow",label:"newRow",hidden:true,editable:true,edittype:"text"},

            {name:"tradeDate",label:'交易日期',align:"center",sortable:false,width:100,editable:true,edittype:"text"},
			/* {field:"realDate",label:'实际交易日期',width:10,editor:{type:"text"}},
			 {field:"tradeStatus",title:'交易状态',width:10,editor:{type:"combobox",options:{height:"80%",required:true,novalidate:true,editable:false,valueField: 'value',textField: 'text',data:[{
			 value: '0',
			 text: '未开始'
			 },{
			 value: '1',
			 text: '进行中'
			 },{
			 value: '2',
			 text: '已完成'
			 }],onSelect:function(record){
			 var editor = $(this).closest('[field=tradeStatus]').siblings('[field=realDate]').find('.datagrid-editable-input');
			 record.value == "2"?editor.datebox({required:true}):editor.datebox({required:false});
			 editor.datebox('textbox').focus(function(){
			 editor.datebox('showPanel');
			 });
			 }}},formatter:function(value,row,index){
			 return value==0?"未开始":value==1?"进行中":value==2?"已完成":"";
			 }}, */
            {name:"type",label:'交易类型',align:"center",sortable:false,width:100,editable:true,edittype:"text",formatter:function(value,options,row){
                return value==0?"收入":value==1?"支出":"";
            },unformat:function(value,options){
                return value=="收入"?0:value=="支出"?1:"";
            }},
            {name:"amount",label:'交易金额',align:"center",sortable:false,width:100,editable:true,edittype:"text"/* editor:{type:"numberbox",options:{height:"80%",required:true,validType:["assetValue","numMaxLength[10]"],precision:2,novalidate:true}} */},
        ],
        loadComplete:function(){
            $("#detailsList").footerData('set',{action:"new"});
        }
    });
	/* function updateRow(index){
	 $('#detailsList').datagrid('updateRow',{
	 index:index,
	 row:{}
	 });
	 } */
    if(details!=''){
        var data = $.parseJSON(details);
        $('#detailsList')[0].addJSONData(data);
    }

    function addRow(){
        var index = $('#detailsList').jqGrid('getRowData').length+1;
        $('#detailsList').jqGrid('addRowData',index,{
            newRow:true
        });
        editRow(index);
    }

    function getTableTarget(rowId,name){
        var target = $('#detailsList').find('tr.jqgrow#'+rowId+' td[aria-describedby=detailsList_'+name+']');
        return target;
    }
    function getTableEditor(rowId,name){
        var target = $('#'+rowId+"_"+name);
        return target;
    }
    function getTableText(rowId,name){
        var target = $('#detailsList #'+rowId +' td[aria-describedby="detailsList_'+name+'"]');
        return target;
    }
    //列表操作
    function editRow(index){
        //var tradeDate = getTableTarget(index,"tradeDate").text();
        //var realDate = getTableTarget(index,"realDate").text();
        var type = $('#detailsList').jqGrid('getRowData',index).type;

        $('#detailsList').jqGrid('editRow', index);
        $('#detailsList').jqGrid('setRowData', index, {editing:true,action:null});//编辑状态转换，按钮变化
        getTableEditor(index,"amount").numberbox({
            height:"80%",width:"100%",required:true,validType:["assetValue","numMaxLength[10]"],precision:2,novalidate:true
        });
        getTableEditor(index,"amount").numberbox('textbox').focus(function(){
            $(this).select();
        });

        getTableEditor(index,"type").fool("dhxCombo",{
            height:"80%",
            width:"100%",
            clearOpt:false,
            required:true,
            novalidate:true,
            editable:false,
            focusShow:true,
            selectFirst:true,
            data:[{
                value: '0',
                text: '收入'
            },{
                value: '1',
                text: '支出'
            }],
            onLoadSuccess:function(combo){
                combo.setComboValue(type);
            }
        });
        getTableEditor(index,"tradeDate").fool("datebox",{
            width:"100%",
            height:"80%",
            required:true,
            novalidate:true,
            inputDate:true,
        });
        getTableEditor(index,"tradeDate").datebox('textbox').focus(function(){
            getTableEditor(index,"tradeDate").datebox('showPanel');
        });
    }
    function delRow(index){
        $.fool.confirm({title:'',msg:"是否确认删除该条数据",fn:function(e){
            if(e){
                if($('#fid').val() != ''){
                    var row = $('#detailsList').jqGrid('getRowData',index);
                    $.ajax({
                        url:getRootPath()+"/rate/program/deleteDetail",
                        method:"post",
                        data:{id:row.fid},
                        success:function(data){
                            dataDispose(data);
                            if(data.returnCode == 0){
                            }else if(data.returnCode == 1){
                                $.fool.alert({msg:data.message,fn:function(){
                                }});
                            }
                        },
                        error:function(data){
                            $.fool.alert({msg:"服务器繁忙，请稍后再试",fn:function(){
                            }});
                        }
                    });
                }
                $('#detailsList').jqGrid('delRowData',index);
            }

        }});
    }
    function cancelRow(index){
        var bool = getTableEditor(index,"newRow").val();
        if(bool == "true"){
            $('#detailsList').jqGrid('delRowData',index);
        }else{
            $('#detailsList').jqGrid('restoreRow',index);
            $('#detailsList').jqGrid('setRowData', index, {editing:false,action:null});
        }
    }
    function saveRow(index){
        var type = getTableEditor(index,"type").next()[0].comboObj.getSelectedValue();
        var tradeDate = getTableEditor(index,"tradeDate").datebox("getValue");
        //var realDate = getTableEditor(index,"realDate").datebox("getValue");
        //getTableEditor(index,"tradeStatus").combobox("getValue")=="2"?null:getTableEditor(index,"realDate").parent().form("disableValidation");
        $('#detailsList').find("tr#"+index).form('enableValidation');
        if(!$('#detailsList').find("tr#"+index).form('validate')){
            return false;
        }
        var rowData = {
            amount:getTableEditor(index,"amount").textbox('getValue'),
            tradeDate:tradeDate,
            type:type,
            rateProgrammeId:$('#fid').val(),
            fid:$.trim(getTableText(index,'fid').text())
        };
        if($('#fid').val() != ''&& '${param.mark}'==1){
            $.ajax({
                url:getRootPath()+"/rate/program/saveDetail",
                method:"post",
                data:rowData,
                success:function(data){
                    dataDispose(data);
                    if(data.returnCode == 0){
                        getTableEditor(index,"newRow").val(false);
                        $('#detailsList').jqGrid('saveRow',index);
                        $('#detailsList').jqGrid('setRowData',index,{
                            amount:data.data.amount,
                            tradeDate:data.data.tradeDate,
                            type:data.data.type,
                            rateProgrammeId:data.data.rateProgrammeId,
                            fid:data.data.fid,
                            editing:false,
                            action:null
                        });
                    }else if(data.returnCode == 1){
                        $.fool.alert({msg:data.message,fn:function(){}});
                    }
                },
                error:function(data){
                    $.fool.alert({msg:"服务器繁忙，请稍后再试",fn:function(){
                    }});
                }
            });
        }else{
            getTableEditor(index,"newRow").val(false);
            $('#detailsList').jqGrid('saveRow',index);
            $('#detailsList').jqGrid('setRowData',index,{
                tradeDate:tradeDate,
                editing:false,
                type:type,
                action:null
            });
        }
    }

    //保存数据
    function saveData(){
        $(document).unbind('keydown',programkd);
        $('#mysave').attr('disabled','disabled');
        $('#programform').form('enableValidation');
        if(!$('#programform').form('validate')){
            $('#mysave').removeAttr('disabled');
            $(document).unbind('keydown',programkd);
            $(document).bind('keydown',programkd);
            return false;
        }
        var _dataPanel = $('#detailsList');
        var _editing = _dataPanel.find(".btn-save");
        if(_editing.length>0){
            $.fool.alert({msg:'你还有没编辑完成的明细,请先确认！',fn:function(){
                $('#mysave').removeAttr('disabled');
                $(document).unbind('keydown',programkd);
                $(document).bind('keydown',programkd);
            }});
            return false;
        }
        var data = $('#programform').serializeJson();
        var row = $('#detailsList').jqGrid('getRowData');
        data = $.extend(data,{"details":JSON.stringify(row)});
        $.ajax({
            url:getRootPath()+"/rate/program/save",
            method:"post",
            data:data,
            success:function(data){
                dataDispose(data);
                if(data.returnCode == 0){
                    $.fool.alert({msg:"保存成功",time:1000,fn:function(){
                        $('#addBox').window('close');
                        if($('#fid').val() == ''){
                            $('#fid').val(data.data.fid);
                            editById(data.data.fid);
                        }else{
                            $('#programList').trigger('reloadGrid');
						}
                        caculate(data.data.fid);
                    }});
                }else if(data.returnCode == 1){
                    $.fool.alert({msg:data.message,fn:function(){
                        $('#mysave').removeAttr('disabled');
                        $(document).unbind('keydown',programkd);
                        $(document).bind('keydown',programkd);
                    }});
                }
            },
            error:function(data){
                $.fool.alert({msg:"服务器繁忙，请稍后再试",fn:function(){
                    $('#mysave').removeAttr('disabled');
                    $(document).unbind('keydown',programkd);
                    $(document).bind('keydown',programkd);
                }});
            }
        });

    }

    function caculate(fid){
        $('#mycacl').attr('disabled','disabled');
        //var id = $("#fid").val();
        //console.log($("#fid").val())
        $.ajax({
            url:getRootPath()+"/rate/program/calRate",
            method:"post",
            data:{id:fid},
            success:function(data){
                $('#mycacl').removeAttr('disabled');
                $('#realRate').html(parseFloat(data.realRate).toFixed(2)+"%");

                $('#profit').val(parseFloat(data.profit).toFixed(2));//利润
                $('#cycleProfitRate').val((parseFloat(data.cycleRate)/100).toFixed(2));//周期收益率
                $('#profitRate').val(parseFloat(data.profitRate).toFixed(2));//利润率

                $('#cycleRate').html(parseFloat(data.cycleRate).toFixed(2)+"%");
                $('.caclBox').show();
                var obj = $('.caclBox').closest('.window-body');
                var scrollValue = $('.caclBox').offset().top-obj.offset().top-24;
                obj.animate({scrollTop:scrollValue},500);
            },
            error:function(data){
                $.fool.alert({msg:"服务器繁忙，请稍后再试",fn:function(){
                    $('#mycacl').removeAttr('disabled');
                }});
            }
        });
    }
    enterController('programform');
</script>
<%-- <script type="text/javascript"  src="${ctx}/resources/js/flow/planTemplateEdit.js?v=${js_v}" ></script> --%>
</body>
</html>