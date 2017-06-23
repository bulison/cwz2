<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消息预警设置</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/css/eventManagement.css" />
    <script type="text/javascript" src="${ctx}/resources/js/eventManagement.js?v=${js_v}"></script>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
       <span class='Icon'></span>
       <div class="trian"></div>
       <h1>消息预警设置</h1>
    </div>
</div>

<div class="form_input" style="padding-bottom: 10px">
    <form>
        <input name="codeOrVoucherCode" class="easyui-textbox" id="search-code" data-options="{prompt:'预警场景',	width:160,height:30,searcher:function(value,name){refreshData()}}"/>
    </form>
    <a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
</div>
<table id="dataTable"></table>
<div id="pager"></div>
<div id="win">
    <div class="window-search-box" style="margin:5px;">
        <input id="txtSearch" placeholder="编号或名称" style="padding:5px;width:120px;" />
        <a href="javascript:;" class="btn-blue btn-s" id="go-search">筛选</a>
        <a href="javascript:;" class="btn-blue btn-s" id="select-ok">确定</a>
        <a href="javascript:;" id="go-clear" class="btn-orange btn-s">清空</a>
    </div>
    <table id="winTable"></table>
    <div id="winPager"></div>
</div>
<script>
    var _ind;
    var options = [
        {value:'SAC',text:'有效'},
        {value:'SNU',text:'无效'}
    ];
    $('#win').window({
       width:1000,
        height:500,
        title:'配置的数值',
        modal:true,
        closed:true
    });
    $("#dataTable").jqGrid({
        datatype:function(postdata){
            $.ajax({
                url: '${ctx}/smgOrgAttr/query',
                data:postdata,
                dataType:"json",
                complete: function(data,stat){
                    if(stat=="success") {
                        data.responseJSON.totalPages=Math.ceil(data.responseJSON.totalElements/postdata.rows);
                        $("#dataTable")[0].addJSONData(data.responseJSON);
                    }
                }
            });
        },
        autowidth:true,
        height:"100%",
        pager:"#pager",
        rowNum:10,
        rowList:[10,20,30],
        viewrecords:true,
        jsonReader:{
            root:"content",
            page:"page",
            total:"totalPages",
            records:"totalElements",
            repeatitems:false,
        },
        colModel:[
            {name : 'editing',label : 'editing',hidden:true},
            {name : 'fid',label : 'fid',hidden:true},
            {name : 'type',label : 'type',hidden:true},
            {name : 'ids',label : 'ids',hidden:true},
            {name : 'orgId',label : 'orgId',hidden:true},
            {name : 'orgName',label : 'orgName',hidden:true},
            {name : 'name',label : 'name',hidden:true},
            {name : 'key',label : 'key',hidden:true},
            {name : 'describe',label : '预警场景',align:'center',width:"100px",edittype:"text"},
            {name : 'value',label : '配置的数值',editable:true,align:'center',width:"100px",formatter:function (cellvalue, options, rowObject) {
                var str="";
                if(rowObject.name=='分仓核算'){
                    if(cellvalue == '0'){
                        str='0-不分仓核算';
                    }else if(cellvalue == '1'){
                        str='1-分仓核算';
                    }
                }else if(rowObject.name=='成本计算方式'){
                    if(cellvalue == '1'){
                        str='1-先进先出';
                    }else if(cellvalue == '2'){
                        str='2-月加权平均';
                    }else if(cellvalue == '3'){
                        str='3-移动平均';
                    }
                }else
                    str = cellvalue;
                return str;
            }},
            {name : 'createTime',label : '创建时间',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                return new Date(cellvalue).format('yyyy-MM-dd');
            }},
            {name : 'recordState',label : '数据状态',align:'center',width:"80px",editable:true,edittype:"text",formatter:function(cellvalue, options, rowObject){
                var str="";
                if(cellvalue == 'SAC'){
                    str='有效';
                }else if(cellvalue == 'SNU'){
                    str='无效';
                }else{
                    str='无效';
                }
                return str;
            }},
            <fool:tagOpt optCode="fconfigAction">
                {name:'action',label:'操作',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    if (rowObject.editing){
                        var s='<a href="javascript:;" title="保存" class="btn-save" onclick="saveRow(\''+options.rowId+'\',\''+rowObject.fid+'\')"></a>';
                        var c='<a href="javascript:;" title="取消" class="btn-cancel" onclick="cancelRow(\''+options.rowId+'\')"></a>';
                        return s+" "+c;
                    }else{
                        return "<a href='javascript:;' class='btn-edit' onclick='editRow(\""+options.rowId+"\")' title='编辑'></a>";;
                    }
                }}
            </fool:tagOpt>
        ],
        onLoadSuccess:function(){//列表权限控制
            <fool:tagOpt optCode="fconfigAction1">//</fool:tagOpt>$('.btn-edit').remove();
        },
        onCellSelect:function(rowid,iCol,cellcontent,e){
            editRow(rowid);
        }
    }).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
    $('#winTable').jqGrid({
        datatype:function(postdata){
            $.ajax({
                url:getRootPath()+'/userController/vagueSearch?searchSize=8',
                data:postdata,
                dataType:"json",
                complete: function(data,stat){
                    if(stat=="success") {
                        data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
                        $("#winTable")[0].addJSONData(data.responseJSON);
                    }
                }
            });
        },
        width:$('#win').width(),
        height:$('#win').height(),
        pager:"#winPager",
        rowNum:10,
        rowList:[10,20,30],
        viewrecords:true,
        jsonReader:{
            records:"total",
            total: "totalpages",
        },
        colModel:[
            {name:'fid',label:'选项',align:'center',sortable:false,width:80,formatter:function(cellvalue, options, rowObject){
            return '<input type="checkbox" value="'+cellvalue+'" id="cb'+options.rowId+'"/>';
            }},
            {name:'userName',label:'名称',align:'center'},
            {name:'deptName',label:'部门',align:'center'},
            {name:'email',label:'邮箱',align:'center'},
            {name:'phoneOne',label:'电话',align:'center'},
        ],
        onSelectRow: function (rowId, status, e) {
            if($('#cb'+rowId).prop('checked')){
                $('#cb'+rowId).removeProp('checked');
            }else{
                $('#cb'+rowId).prop('checked',true);
            }
        }
    });
    function getRowId(id) {
        return id.substring(2);
    }
    $('#select-ok').click(function () {
        var names="",ids="";
        $('#gview_winTable :checkbox:checked').each(function () {
            var rowdata = $("#winTable").jqGrid('getRowData',getRowId($(this).attr('id')));
            names+=rowdata.userName+',';
            ids+=$(this).val()+',';
        });
        hideObj(_ind,'name').text(names);
        $('#'+_ind+'_value').val(names.substring(0,names.length-1));
        hideObj(_ind,'ids').text(ids);
        $('#win').window('close');
    })

    $('#go-search').click(function() {
        var para =$('#txtSearch').val();
        var options = {"searchKey":para};
        $('#winTable').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
    })
    $('#go-clear').click(function () {
        $('#txtSearch').val("");
        var options = {"searchKey":""};
        $('#winTable').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
    })
    function editRow(rowid){
        var rowData=$("#dataTable").jqGrid('getRowData',rowid);
        if(rowData.editing=='true'){
            return;
        }
        rowData.editing=true;
        rowData.action=null;
        $('#dataTable').jqGrid('setRowData', rowid, rowData);
        $("#dataTable").jqGrid('editRow',rowid);
        if(!rowData.type||rowData.type==0){
            if(rowData.name=="成本计算方式"){
                $('#'+rowid+'_value').fool("dhxCombo",{
                    clearOpt:false,
                    editable:false,
                    valueField: 'value',
                    textField: 'text',
                    required:true,
                    focusShow:true,
                    data:[{value: '1',text: '先进先出'},{value: '2',text: '月加权平均'},{value: '3',text: '移动平均'}],
                    onLoadSuccess:function (combo) {
                        $('#'+rowid+'_value').val(rowData.value);
                        if(rowData.value=="1-先进先出")
                            combo.setComboValue('1');
                        else if(rowData.value=="2-月加权平均")
                            combo.setComboValue('2');
                        else
                            combo.setComboValue('3');
                    }
                })
            }else if(rowData.name=="分仓核算"){
                $('#'+rowid+'_value').fool("dhxCombo",{
                    clearOpt:false,
                    editable:false,
                    valueField: 'value',
                    textField: 'text',
                    required:true,
                    focusShow:true,
                    data:[{value: '0',text: '0-不分仓核算'},{value: '1',text: '1-分仓核算'}],
                    onLoadSuccess:function (combo) {
                        $('#'+rowid+'_value').val(rowData.value);
                        if(rowData.value=="0-不分仓核算")
                            combo.setComboValue('0');
                        else
                            combo.setComboValue('1');
                    }
                })
            }else {
                $('#'+rowid+'_value').numberbox({
                    width:"100%",
                    height:"80%",
                    //novalidate:true,
                    validType:['accessory','numMaxLength[10]']
                })
            }
            rowData.type=0;
        }
        if(rowData.type==1){
            _ind = rowid;
            $('#'+rowid+'_value').attr('readonly',true);
            $('#'+rowid+'_value').click(function () {
                var ids = strToArray(hideObj(rowid,'ids').text(),[]);
                for(var i=0;i<ids.length;i++){
                    $('#gview_winTable :checkbox').each(function () {
                        if($(this).val()==ids[i])
                            $(this).prop('checked','checked');
                    });
                }
                $('#win').window('open');
            })
        }
        getEditor("dataTable",rowid,"recordState").fool("dhxCombo",{
            editable:false,
            valueField: 'value',
            textField: 'text',
            required:true,
            focusShow:true,
            data:[
                {
                    value: 'SAC',
                    text: '有效'
                },{
                    value: 'SNU',
                    text: '无效'
                }
            ],
            onLoadSuccess:function(combo){
                (getEditor("dataTable",rowid,"recordState").next())[0].comboObj.deleteOption("");
                if(rowData.recordState=="有效")
                    combo.setComboValue('SAC');
                else
                    combo.setComboValue('SNU');
            }
        });
    }

    function hideObj(rowid,obj) {
        return $('#'+rowid+' td[aria-describedby="dataTable_'+obj+'"]');
    }

    function strToArray(str,arr) {
        if(str.indexOf(',')!=-1){
           arr.push(str.substring(0,str.indexOf(',')))
           return arguments.callee(str.substring(str.indexOf(',')+1),arr )
        }
        return arr;
    }
    function saveRow(index,fid){
        var myform = $("#dataTable").find("tr[id="+index+"]");
        if(!myform.fool('fromVali')){
            return false;
        }
        var type = hideObj(index,"type").text();
        var ids = hideObj(index,"ids").text();
        var orgId = hideObj(index,"orgId").text();
        var orgName = hideObj(index,"orgName").text();
        var name = hideObj(index,"name").text();
        var value = $('#'+index+'_value').val();
        var txt = value;
        if(name=="分仓核算"||name=="成本计算方式"){
            value=(getEditor("dataTable",index,"value").next())[0].comboObj.getSelectedValue();
            txt=(getEditor("dataTable",index,"value").next())[0].comboObj.getComboText();
        }

        var key = hideObj(index,"key").text();
        var describe = hideObj(index,"describe").text();
        var recordState = (getEditor("dataTable",index,"recordState").next())[0].comboObj.getSelectedValue();

        $.post("${ctx}/smgOrgAttr/save",{fid:fid,type:type,ids:ids,orgId:orgId,orgName:orgName,name:name,key:key,value:value,describe:describe,recordState:recordState},function(data){
            dataDispose(data);
            if(data.result=="0"){
                var data = data.data;
                $("#dataTable").jqGrid("restoreRow",index);
                $('#dataTable').jqGrid('setRowData',index,{
                    fid:data.fid,
                    key:data.key,
                    value:txt,
                    describe:data.describe,
                    recordState:data.recordState,
                    editing:false,
                    action:null
                });
            }else if(data.result=="1"){
                $.fool.alert({msg:data.msg,fn:function(){
                }});
            }else{
                $.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
                }});
            }
        });
    }
    function cancelRow(rowid){
        var rowData = $("#dataTable").jqGrid('getRowData',rowid);
        var comboTxt,flag=false;
        if(rowData.type==0&&(rowData.name=='分仓核算'||rowData.name=='成本计算方式')){
            flag=true;
            comboTxt = $('#'+rowid+'_value').val();
        }
        $("#dataTable").jqGrid('restoreRow',rowid);
        if(flag)
            $('#dataTable').jqGrid('setRowData', rowid,{editing:false,action:null,value:comboTxt});
        else
            $('#dataTable').jqGrid('setRowData', rowid,{editing:false,action:null});
    }
    //获取jqGrid编辑框
    function getEditor(gridId,rowId,name){
        var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
        return editor;
    }
    //查询
    $('.Inquiry').click(function(){	//事件分类
        var describe=$("#search-code").textbox('getValue');
        var options = {"describe":describe};
        $('#dataTable').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
    });
</script>
</body>
</html>
