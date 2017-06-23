<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="xsyj" scope="page"></c:set>
<c:set var="billCodeName" value="消息预警" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/eventManagement.css" />
<script type="text/javascript" src="${ctx}/resources/js/eventManagement.js?v=${js_v}"></script>
<style>
.fixed{ 

	position:fixed;
	border-width:1px 0;
	z-index:8999;
	top:0px;
}

#Inquiry{margin-left: 5px;}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 25px;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:27px; top:62px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:5px;}
.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:89px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 10px 10px;width:240px;}
.button_clear{ border-top: 1px solid #D5DBEA;margin-top: 10px; text-align: right; padding-top: 10px;}
.roed{
   -moz-transform:scaleY(-1);
   -webkit-transform:scaleY(-1);
   -o-transform:scaleY(-1);
   transform:scaleY(-1);
   filter:FlipV();
}
</style>
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>消息预警</h1>
                </div>             
    </div>
    <div class="btn">
      <a href="javascript:;" id="addlist" class="btn-ora-add">新增</a> 
    </div>
    <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
     <!-- <div class="form_input">
      <form>
        <input name="codeOrVoucherCode" class="easyui-textbox" id="search-code"/>
      </form>
      <a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
    </div> -->
    <div class="input_div">
	<form id="search-form">
	<p> <font>消息类型：</font><input id="sendType1" name="sendType" type="text" value="${msgWarnSetting.sendType}"/></p>
	<p>触发类型：<input name="triggerType"  id="triggerType1"/></p>
	<p><font>事件类型：</font><input id="taskType1" name="taskType"/></p>
	<div class="button_clear">
	<a href="javascript:;" id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	<a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	<a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	</div>
	</form>
	</div>
   <table id="Tablelist"></table>
   <div id="pager"></div>
   	<div id="addBox"></div>
  <script type="text/javascript">
	initManage('${billCode}','${billCodeName}');
    var combodata;
    $.post('${ctx}/userController/userList',{},function(data) {
        if (data) {
            combodata = data.rows;
            $('#Tablelist').jqGrid({
                datatype:function(postdata){
                    $.ajax({
                        url:getRootPath()+'/flow/msgSetting/query',
                        data:postdata,
                        dataType:"json",
                        complete: function(data,stat){
                            if(stat=="success") {
                                data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
                                $("#Tablelist")[0].addJSONData(data.responseJSON);
                            }
                        }
                    });
                },
                autowidth:true,
                height:$(window).outerHeight()-200+"px",
                pager:"#pager",
                rowNum:10,
                rowList:[10,20,30],
                viewrecords:true,
                jsonReader:{
                    records:"total",
                    total: "totalpages",
                },
                colModel:[
                    {name:'isNew',label : 'isNew',hidden:true},
                    {name:'editing',label : 'editing',hidden:true},
                    {name:'fid',label:'fid',hidden:true,width:100,align:'center'},
                    {name:'superviseIds',label:'superviseIds',hidden:true,width:100,align:'center'},
                    {name:'busScene',label:'发送场景',width:100,align:'center',editable:true,resizable:true,formatter:BusSceneValue},
                    {name:'sendType',label:'消息类型',width:100,align:'center',editable:true,resizable:true,formatter:MessageType},
                    {name:'triggerType',label:'触发类型',width:100,align:'center',editable:true,resizable:true,formatter:TriggerType},
                    {name:'taskLevel',label:'任务级别名称',width:60,align:'center',edittype:"text",editable:true,resizable:true,editoptions:{dataInit:function(ed){
                        $(ed).textbox({validType:'maxLength[50]',novalidate:true,missingMessage:"该项必填",required:true,width:'100%',height:'88%'});
                    }}},
                    {name:'days',label:'提前天数',width:60,align:'center',editable:true,resizable:true,editoptions:{dataInit:function(ed){
                        $(ed).textbox({validType:'accessory',novalidate:true,width:'100%',height:'88%'});
                    }}},
                    {name:'retryDays',label:'重发天数',width:60,align:'center',edittype:"text",editable:true,resizable:true,editoptions:{dataInit:function(ed){
                        $(ed).textbox({validType:'accessory',novalidate:true,width:'100%',height:'88%'});
                    }}},
                    {name:'range',label:'值域',width:60,align:'center',editable:true,resizable:true,editoptions:{dataInit:function(ed){
                        $(ed).textbox({validType:'assetValue',novalidate:true,width:'100%',height:'88%'});
                    }}},
                    {name:'taskType',label:'事件类型',width:100,align:'center',editable:true,resizable:true,formatter:EventType},
                    {name:'superviseId',label:'监督人',width:100,align:'center',resizable:true,editable:true,formatter:function(value,options,row){
                        var names="";
                        var fids=[];
                        if(value){
                            fids = value.split(',');
                        }else if(row.superviseIds)
                            fids = row.superviseIds.split(',');

                        for(var i=0;i<combodata.length;i++){
                            for(var j=0;j<fids.length;j++){
                                if(fids[j]=="")
                                    continue;
                                if(fids[j]==combodata[i].fid)
                                    names+=combodata[i].userName+',';
                            }
                        }
                        return names;
                    }},
                    {name:'toSuperior',label:'上级发送',width:60,align:'center',editable:true,resizable:true,formatter:sendType},
                    {name:'toSubordinate',label:'下级发送',width:60,align:'center',editable:true,resizable:true,formatter:sendType},
                    {name:'isSystem',label:'系统默认配置',width:100,align:'center',resizable:true,formatter:isSystemType},
                    {name:'action',label:'操作',width:100,align:'center',resizable:true,formatter:operate},
                ],
                onCellSelect:function(rowid,iCol,cellcontent,e){
                    editRow(rowid);
                },
                loadcomplete:function(){
                    warehouseAll();
                }
            }).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
        }
    });

	//新增
	$('#addlist').click(function(){
        var ids = jQuery("#Tablelist").jqGrid('getDataIDs');
        var newrowid = Math.max.apply(Math,ids) + 1;
        if(newrowid=="-Infinity"){
            newrowid = "1";
        }
        $("#Tablelist").jqGrid("addRowData", newrowid, {isNew:1,isSystem:0,toSuperior:0,toSubordinate:0}, "first");
        editRow(newrowid);
    });

    var triggerTypeValue='';
    $.ajax({
        url:"${ctx}/flow/msgSetting/getTriggerMap?num="+Math.random(),
        async:false,
        type:"post",
        success:function(data){
            var data=JSON.parse(data);
            triggerTypeValue=formatData(data,"id");
        }
    });
    var busSceneValue='';
    $.ajax({
        url:"${ctx}/flow/msgSetting/getSceneMap?num="+Math.random(),
        async:false,
        success:function(data){
            var data=JSON.parse(data);
            busSceneValue=formatData(data,"id");
        }
    });

    var sendTypeValue= $("#sendType1").fool("dhxCombo",{
        width:160,
        height:32,
        editable:false,
        focusShow:true,
        data:[
            {
                value:'0',
                text:'发起人',
            },
            {
                value: '1',
                text: '负责人'
            },{
                value: '2',
                text: '承办人'
            },{
                value: '7',
                text: '部门监督人'
            },{
                value: '8',
                text: '公司监督人'
            }
            ,{
                value: '9',
                text: '审核人'
            }
        ],
    });
    var taskTypeName= $("#taskType1").fool("dhxCombo",{
        width:160,
        height:32,
        editable:false,
        focusShow:true,
        data:[
            {
                value:'0',
                text:'触发事件',
            },
            {
                value: '1',
                text: '前关联事件'
            },{
                value: '2',
                text: '后关联事件'
            }
        ],
    });
    var triggerTypeName= $("#triggerType1").fool("dhxCombo",{
        width:160,
        height:32,
        data:triggerTypeValue,
        editable:false,
        focusShow:true,
    });
    function editInit(rowid,rowData) {
        getEditor("Tablelist",rowid,"superviseId").combobox({
            width:182,
            height:31,
            data:combodata,
            valueField: 'fid',
            textField: 'userName',
            novalidate:true,
            multiple:true,
            editable:false,
            onLoadSuccess:function(){
                var fids = rowData.superviseIds;
                getEditor("Tablelist",rowid,"superviseId").combobox('setValues',fids);
            }
        });
        var input= getEditor("Tablelist",rowid,"superviseId").next().find('.textbox-text').eq(0);
        if(rowData.isNew==1)
            input.prop('disabled',true);
        getEditor("Tablelist",rowid,"busScene").fool("dhxCombo",{
            required:true,
            novalidate:true,
            width:'100%',
            height:'88%',
            hasDownArrow:false,
            data:busSceneValue,
            editable:false,
            focusShow:true,
            clearOpt:false,
            onLoadSuccess:function(combo){
                for(var i=0; i<busSceneValue.length; i++){
                    if (busSceneValue[i].text.text == rowData.busScene)
                        combo.setComboValue(busSceneValue[i].value);
                }
            }
        });
        getEditor("Tablelist",rowid,"sendType").fool("dhxCombo",{
            width:'100%',
            height:'88%',
            required:true,
            novalidate:true,
            editable:false,
            focusShow:true,
            clearOpt:false,
            data:sendTypeVal,
            onChange:function(value,text){
                if(value=='7'||value=='8'){
                    input.prop('disabled',false);
                }else{
                    input.prop('disabled',true);
                    getEditor("Tablelist",rowid,"superviseId").combobox('setValues',"");
                }
            },
            onLoadSuccess:function(combo){
                for(var i=0; i<sendTypeVal.length; i++){
                    if (sendTypeVal[i].text == rowData.sendType){
                        combo.setComboValue(sendTypeVal[i].value);
                        if(sendTypeVal[i].value=='7'||sendTypeVal[i].value=='8'){
                            input.prop('disabled',false);
                        }else{
                            input.prop('disabled',true);
                        }
                    }
                }
            }
        });
        getEditor("Tablelist",rowid,"triggerType").fool("dhxCombo",{
            required:true,
            novalidate:true,
            width:'100%',
            height:'88%',
            data:triggerTypeValue,
            editable:false,
            focusShow:true,
            clearOpt:false,
            onLoadSuccess:function(combo){
                for(var i=0; i<triggerTypeValue.length; i++){
                    if (triggerTypeValue[i].text.text == rowData.triggerType)
                        combo.setComboValue(triggerTypeValue[i].value);
                }
            }
        });
        getEditor("Tablelist",rowid,"taskType").fool("dhxCombo",{
            width:'100%',
            height:'88%',
            editable:false,
            focusShow:true,
            clearOpt:false,
            data:taskTypeVal,
            onLoadSuccess:function(combo){
                for(var i=0; i<taskTypeVal.length; i++){
                    if (taskTypeVal[i].text == rowData.taskType)
                        combo.setComboValue(taskTypeVal[i].value);
                }
            }
        });

        getEditor("Tablelist",rowid,"isSystem").fool("dhxCombo",{
            required:true,
            novalidate:true,
            width:'100%',
            height:'88%',
            data:[
                {
                    value:'0',
                    text:'用户自定义配置',
                },
                {
                    value:'1',
                    text:'系统默认生产配置'
                }
            ],
            editable:false,
            focusShow:true,
            clearOpt:false,
            onLoadSuccess:function(combo){
                if('${msgWarnSetting.isSystem}'=='1')
                    combo.setComboValue('1')
                else
                    combo.setComboValue("0");
            }
        });
        getEditor("Tablelist",rowid,"toSuperior").fool("dhxCombo",{
            width:'100%',
            height:'88%',
            editable:false,
            focusShow:true,
            clearOpt:false,
            data:sendVal,
            onLoadSuccess:function(combo){
                for(var i=0; i<sendVal.length; i++){
                    if (sendVal[i].text == rowData.toSuperior)
                        combo.setComboValue(sendVal[i].value);
                }
        }});
        getEditor("Tablelist",rowid,"toSubordinate").fool("dhxCombo",{
            width:'100%',
            height:'88%',
            editable:false,
            focusShow:true,
            clearOpt:false,
            data:sendVal,
            onLoadSuccess:function(combo){
                for(var i=0; i<sendVal.length; i++){
                    if (sendVal[i].text == rowData.toSubordinate)
                        combo.setComboValue(sendVal[i].value);
                }
            }
        });
    }
    function editRow(rowid){
        var rowData=$("#Tablelist").jqGrid('getRowData',rowid);
        if(rowData.editing=="true"){//设置编辑状态
            return;
        }
        rowData.editing=true;
        rowData.action=null;
        $('#Tablelist').jqGrid('setRowData', rowid, rowData);
        $("#Tablelist").jqGrid('editRow',rowid);//打开编辑行
        editInit(rowid,rowData);
    }
    function getRowData(rowid) {
        var name=$("input[name='superviseId']");
        var fids='';
        for(var i=0;i<name.length;i++){
            if(name.eq(i).val()=="")
                continue;
            fids+=name.eq(i).val()+',';
        };
        var obj = {
            fid:getText("Tablelist",rowid,'fid').text().trim(),
            busScene:(getEditor("Tablelist",rowid,"busScene").next())[0].comboObj.getSelectedValue(),
            sendType:(getEditor("Tablelist",rowid,"sendType").next())[0].comboObj.getSelectedValue(),
            triggerType:(getEditor("Tablelist",rowid,"triggerType").next())[0].comboObj.getSelectedValue(),

            taskLevel:getEditor("Tablelist",rowid,"taskLevel").textbox('getValue'),
            days:getEditor("Tablelist",rowid,"days").textbox('getValue'),
            retryDays:getEditor("Tablelist",rowid,"retryDays").textbox('getValue'),
            range:getEditor("Tablelist",rowid,"range").textbox('getValue'),
            taskType:(getEditor("Tablelist",rowid,"taskType").next())[0].comboObj.getSelectedValue(),
            superviseIds:fids,
            toSuperior:(getEditor("Tablelist",rowid,"toSuperior").next())[0].comboObj.getSelectedValue(),
            toSubordinate:(getEditor("Tablelist",rowid,"toSubordinate").next())[0].comboObj.getSelectedValue(),
            isSystem:getText("Tablelist",rowid,'isSystem').text()=='用户自定义配置'?0:1
        };
        return obj;
    }
    function saverow(rowid,fid){
        var rowObj = getRowData(rowid);
        var _url=getRootPath()+'/flow/msgSetting/save';
        $('#Tablelist #'+rowid).form('enableValidation');

        if($('#Tablelist #'+rowid).form('validate')){
            $.post(_url,{fid:rowObj.fid,taskLevel:rowObj.taskLevel,days:rowObj.days,retryDays:rowObj.retryDays,range:rowObj.range,sendType:rowObj.sendType,triggerType:rowObj.triggerType,taskType:rowObj.taskType
                ,toSuperior:rowObj.toSuperior,toSubordinate:rowObj.toSubordinate,busScene:rowObj.busScene,isSystem:rowObj.isSystem,superviseIds:rowObj.superviseIds},function(data){
                dataDispose(data);
                if(data.returnCode==0){
                    $.fool.alert({time:1000,msg:'保存成功！',fn:function(){
                        rowObj.editing=false;
                        rowObj.action=null;
                        rowObj.isNew=0;
                        rowObj.fid=data.data.fid;
                        rowObj.superviseId=rowObj.superviseIds;
                        $("#Tablelist").jqGrid('restoreRow', rowid);
                        $('#Tablelist').jqGrid('setRowData', rowid,rowObj)
                    }});
                }else if(data.returnCode==1){
                    $.fool.alert({msg:data.message});
                };
            });
        }else{
            return false;
        }
    }
    //列表撤销
    function cancelrow(index,value,fid){
        var row = $('#Tablelist').jqGrid('getRowData',index);
        $('#Tablelist').jqGrid('restoreRow',index);
        if(value=='1'){
            $("#Tablelist").jqGrid('delRowData',index);
        }else{
            var data={fid:fid,editing:false,action:null,superviseId:row.superviseIds};
            $('#Tablelist').jqGrid('setRowData',index,data);
        }
    }
    //删除/delete
    function delRow(fid){
        var _url=getRootPath()+'/flow/msgSetting/delete?id='+fid;
        $.fool.confirm({title:'提示',msg:"确认删除记录？",fn:function(data){
            if(data){
                $.post(_url,function(data){
                    dataDispose(data);
                    if(data.returnCode == '0'){
                        $('#Tablelist').trigger('reloadGrid');
                        $.fool.alert({time:1000,msg:'删除成功'});
                    }else if(data.returnCode = '1'){
                        $.fool.alert({msg:data.message});
                    }else{
                        $.fool.alert({msg:'服务器繁忙，请稍后再试'});
                    }
                },'json');
            }else{
                return false;
            }
        }});
    }

	//点击下拉按钮
	$('.button_a').click(function(){
		$('.input_div').toggle(2);	
		var s=$('.button_a').attr('class'); 
		if(s=="button_a roed"){
			$('.button_a').removeClass('roed');	
		}else{
			$(this).addClass('roed');
		}
	});
	$("#clear-btn").click(function(){		
		$("#search-form").form("clear");
		taskTypeName.setComboValue("");
		taskTypeName.setComboText("");
		triggerTypeName.setComboValue("");
		triggerTypeName.setComboText("");
		sendTypeValue.setComboValue("");
		sendTypeValue.setComboText("");
	});

	//点击关闭隐藏
	$('#clear-btndiv').click(function(){
		$('.input_div').hide(2);
		$('.button_a').removeClass('roed');
	});
	//查询
	$("#search-btn").click(function(){
	var options = $("#search-form").serializeJson();
	$('#Tablelist').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
});
	//鼠标获取焦点
	$('#bolting').focus(function (){ 
		$('.input_div').show(2);
		$('.button_a').addClass('roed');
	}); 

	enterSearch("Inquiry");//回车搜索
</script> 
</body>
</html>
