<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>货品维护</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/easyui-tree-extend.js?v=${js_v}"></script>
<script type="text/javascript">
</script>
<style type="text/css">
  .btnBox .btn{
  	display: block;
  	width: 39.5px;
  	text-align: center;
  	float: left;
  	border-bottom:1px solid #ccc;
  	border-right:1px solid #ccc;  
  }
  .search-tree-box {
  	border: 0px;
    border-left: 1px solid #CCC;
    border-right: 1px solid #CCC;
  }
  .ui-widget-content {
    background:transparent !important;
    background-color:transparent !important;
  }
  .validatebox-invalid {
    border-color: rgb(223,223,223) !important;
    background-color: #FFE1E1 !important;
    color: #000 !important;
  }
  td[role=gridcell] .btn-add{
    width:100%;
    height: 100%;
    background-color:#1BB9FA;
    background-image:url(${ctx}/resources/images/add.png);
    background-repeat:no-repeat;
    background-position:10px 5px;
    border:#1BB9FA;
    border-radius:3px;
    font-size: 14px;
    text-align: center;
    color: white !important;
    line-height: 30px;
    
  }
  td[role=gridcell] .btn-add:hover{
    width:100%;
    height: 100%;
    background-color:#1BB9FA;
    background-image:url(${ctx}/resources/images/add_on.png);
    background-repeat:no-repeat;
    background-position:10px 5px;
    border:#1BB9FA;
    border-radius:3px;
    font-size: 14px;
    vertical-align: middle;
    text-align: center;
    color: white;
    line-height: 30px
  }
</style>
</head>
<body>
 <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>货品维护</h1>
                </div>             
             </div>    
       <div class="search-tree-box" id="search-tree-curr">
            <div class="btnBox" style="border-bottom:none;">
         	<fool:tagOpt optCode="gmaintAdd"><a id="add" class="btn addBtn" href="javascript:;" title="添加货品组">添加</a></fool:tagOpt>
         	<fool:tagOpt optCode="gmaintEdit"><a id="update" class="btn updateBtn" href="javascript:;" title="修改货品组">修改</a></fool:tagOpt>
         	<fool:tagOpt optCode="gmaintDel"><a id="delete" class="btn deleteBtn" href="javascript:;" title="删除货品组">删除</a></fool:tagOpt>
         	<fool:tagOpt optCode="gmaintImport"><a id="in-group" class="btn" href="javascript:;"  title="导入">导入</a></fool:tagOpt>
         	<fool:tagOpt optCode="gmaintExport"><a id="out" class="btn" title="导出" href="javascript:;">导出</a></fool:tagOpt>
         	<fool:tagOpt optCode="gmaintSearch"><input type="text" id="searchBox" class="searchbox"/></fool:tagOpt>
            </div>
            <ul id="goodsTree" class="easyui-tree" style="height: 454px;"></ul>
       </div> 
        
       <div class="contentBox"> 
            <input type="text" id="searchBox2" class="searchbox2"/>
            <input type="radio" name="showChild" value="0" checked="checked" onclick="mysearch(0)" />显示当前货品组货品
            <input type="radio" name="showChild" value="1" onclick="mysearch(1)" />显示当前货品组及其子货品组货品
    	    <table id="content" style="width:inherit;border: 1px solid #CCC"></table>
    	    <div id="pager"></div>
       </div>
       
       <div class="barBox"></div>   
<div id='pop-window'></div>
<script type="text/javascript">
var parentId='';
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
 $(function(){
	 $('#goodsTree').tree({
		 url:'${ctx}/goods/getAll',
		 onClick: function(node){
			 	parentId = node.id;
		    	if(!isFirst(node)){
		    		var showChild=$("input[name='showChild']:checked").val();
		    		$('#content').jqGrid('setGridParam',{url:"${ctx}/goods/getChilds",postData:{parentId:node.id,showChild:showChild,showDisable:1}}).trigger("reloadGrid"); 
		    	}
		    	if(node.attributes.detail.recordStatus=="SNU"){
		    		$("#add").attr("disabled","disabled");
		    		$("#add").attr("title","货品组无效，不能新增");
		    		$("#add").css("background-color","rgb(204, 204, 204)");
		    	}else if(node.attributes.detail.recordStatus=="SAC"){
		    		$("#add").removeAttr("disabled");
		    		$("#add").attr("title","添加货品组");
		    		$("#add").css("background-color","#ffffff");
		    	}
		 },
		 onLoadSuccess:function(node, data){
			 var noder = $("#goodsTree").tree('getRoot');
			 var nodes = $("#goodsTree").tree('getChildren',noder);
			 var max = nodes.length;
			 for(var i = 0; i < max; i++){
				 $.ajax({
					url:"${ctx}/goods/getChilds?showDisable=1&parentId="+nodes[i].id,
					async:false,
					success:function(data){
				 	data.total != 0?$(nodes[i].target).append('<span class="btn-bills"></span>'):null;;
				 }});
			 }
			 setTimeout(function(){
				 $("#goodsTree").tree('select', noder.target);
				 $(noder.target).click();
			 }, 1);
		 }
	});
	
	$(".barBox").window({
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closed:true,
		modal:true,
		shadow:false,
		top:100,
		width:900,
	}); 
	
	$("#content").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/goods/getChilds?showDisable=1',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		/* data.responseJSON.rows.push({"id":"new","action":"new","adder":"new"}); */
		        		$("#content")[0].addJSONData(data.responseJSON);
		            	/* $("#content").addRowData("new",{"action":"new","adder":"new"},"last"); */
		        	}
		        }
			});
		},
		footerrow: true,
		autowidth:true,
		height:500, 
		pager:"#pager",
		rowNum:10,
		rowList:[10,20,30],
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},
		colModel:[ 
		            {name:"action",label:"操作",align:'center',formatter:function(cellvalue, options, rowObject){
		            	var d='',c='',s='';
				    	d = "<a href='javascript:;' onClick='delRow(\""+options.rowId+"\",\""+rowObject.fid+"\")' class='btn-del' title='删除'></a>";
				    	c = "<a href='javascript:;' onClick='cancelRow(\""+options.rowId+"\",\""+rowObject.isNew+"\")' class='btn-back' title='撤销'></a>";
				    	s = "<a href='javascript:;' onClick='saveRow(\""+options.rowId+"\",\""+rowObject.fid+"\")' class='btn-save' title='保存'></a>";
				    	b = "<a href='javascript:;' onClick='getBar("+options.rowId+")' class='btn-barCode' title='条码'></a>";
				    	t = "<a href='javascript:;' onClick='getTrans("+options.rowId+")' class='btn-scale' title='运输计价换算关系'></a>";
				    	  if(cellvalue == 'new'){
				    		  return '<a class="btn-add" onclick="addrow()" title="新增" href="javascript:;"> 新增</a>';
				    	  }else{
					    	  if(rowObject.editing){
					    		  return s+"  "+c;
					    	  }else{
					    		  //屏蔽编辑按钮
					    	      return d+"  "+b+" "+t;
					    	  }
				    	  }
		            }},
		            {name : 'isNew',label : 'isNew',hidden:true},
		            {name : 'adder',label : 'isNew',hidden:true},
		            {name : 'parentId',label : 'parentId',hidden:true},
		            {name : 'editing',label : 'editing',hidden:true}, 
                    {name : 'updateTime',label : 'updateTime',hidden:true}, 
                    {name : 'fid',label : 'fid',hidden:true}, 
                    {name : 'categoryId',label : 'categoryId',hidden:true}, 
                    {name : 'goodsSpecId',label : 'goodsSpecId',hidden:true}, 
                    {name : 'unitId',label : 'unitId',hidden:true}, 
                    {name : 'useFlag',label : 'useFlag',hidden:true}, 
                    {name : 'accountFlag_',index : 'accountFlag_',hidden:true},
                    {name : 'barCode',label : '条码',align:'center',width:"200px",editable:true,edittype:"text"}, 
                    {name : 'code',label : '编号',align:'center',width:"100px",editable:true,edittype:"text"}, 
                    {name : 'name',label : '名称',align:'center',editable:true,edittype:"text"}, 
                    {name : 'spec',label : '规格',align:'center',editable:true,edittype:"text"}, 
                    {name : 'recordStatus',label : '是否有效',align:'center',width:"100px",editable:true,edittype:"text",formatter:function(value){
                    	var str="";
                    	if(value){
                    		if(value == 'SAC'){
                        		str='有效';
                        	}else if(value == 'SNU'){
                        		str='无效';
                        	}else{
                        		str=value;
                        	}
                    	}else{
                    		str="";
                    	}
                    	return str;
                    }}, 
                    {name : 'accountFlag',label : '记账标识',align:'center',editable:true,edittype:"text",formatter:function(value){
                    	var str="";
                    	if(value == "1"){
                    		str='计算库存';
                    	}else if(value == "0"){
                    		str='不计算库存';
                    	}else{
                    		str=value;
                    	}	
                    	return str;
                    }}, 
                    {name : 'categoryName',label : '货品类别',align:'center',editable:true,edittype:"text"},
                    {name : 'goodsSpecName',label : '货品属性组',align:'center',editable:true,edittype:"text"}, 
                    {name : 'unitName',label : '单位',align:'center',editable:true,edittype:"text"},
                    {name : 'percentage',label : '提成点数',align:'center',editable:true,edittype:"text"},
                    {name : 'describe',label : '描述',align:'center',editable:true,edittype:"text"}
                  ],
        onSelectRow:function(rowid){
        	editRow(rowid);
        },
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});  
	$("#content").jqGrid("footerData","set",{"action":"new","adder":"new"});
	
	$("#in-group").click(function(){
		var s={
				target:$("#pop-window"),
				boxTitle:"导入货品或货品组",
				downHref:"${ctx}/ExcelMapController/downTemplate?downFile=货品.xls",
				action:"${ctx}/goods/import",
				fn:'okCallback()'
		};
		importBox(s);
	});
});
 $('#out').click(function(){
	 var showChild = $('input[name=showChild]:checked').val();
	 var searchKey = $('#searchBox2').searchbox('getValue');
	 location.href="${ctx}/goods/export?parentId="+parentId+"&searchKey="+searchKey+"&showChild="+showChild;
 });
 
//列表新增
function addrow(){
	var ids=$('#content').getDataIDs();
	var newIndex =0;
	if(ids.length>0){
		newIndex=ids[ids.length-1]+1;
	}
	$('#content').jqGrid('addRowData',newIndex,{isNew:1,recordStatus:"SAC",accountFlag:"1"},"last");
	/* var index = $("#content").jqGrid('getRowData').length;
	$("#content").jqGrid('addRowData',index,{recordStatus:"SAC",accountFlag:1,isNew:1},"last"); */
	editRow(newIndex);
}
//列表编辑 参数改为index
function editRow(rowid){
	var rowData=$("#content").jqGrid('getRowData',rowid);
	if(rowData.editing=="true"||rowData.adder=='new'){ 
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#content').jqGrid('setRowData', rowid, rowData);
	$("#content").jqGrid('editRow',rowid);
	getEditor("content",rowid,"code").validatebox({
		required:true,
		novalidate:true,
	});
	getEditor("content",rowid,"name").validatebox({
		required:true,
		novalidate:true,
	});
	getEditor("content",rowid,"unitName").validatebox({
	    required:true,
        novalidate:true
    })
    getEditor("content",rowid,"percentage").validatebox({
	    required:true,
        novalidate:true,
        validType:"percentage"
    })
	
	var statusCombo=getEditor("content",rowid,"recordStatus").fool("dhxCombo",{
		height:"24px",
		width:"62px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
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
	});
	statusCombo.readonly(true);
	if(rowData.recordStatus=='有效'){
		statusCombo.setComboValue("SAC");
	}else if(rowData.recordStatus=='无效'){
		statusCombo.setComboValue("SNU");
	}
	
	var flagCombo=getEditor("content",rowid,"accountFlag").fool("dhxCombo",{
		height:"24px",
		width:"90px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		required:true,
		focusShow:true,
		data:[
		      {
		    	  value: '1',
			      text: '计算库存'
		      },{
		    	  value: '0',
			      text: '不计算库存'
		      }
		],
	});
	flagCombo.readonly(true);
	if(rowData.accountFlag=='计算库存'){
		flagCombo.setComboValue('1');
	}else if(rowData.accountFlag=='不计算库存'){
		flagCombo.setComboValue('0');
	}
	(getEditor("content",rowid,"accountFlag").next())[0].comboObj.attachEvent("onSelectionChange",function(){
		var text=(getEditor("content",rowid,"accountFlag").next())[0].comboObj.getSelectedText();
		var noSelect = rowData.useFlag;
		if(noSelect==1&&text!=rowData.accountFlag){
			$.fool.alert({msg:'此货品已被使用，不能更改货品记账标识'});
			if(rowData.accountFlag=='计算库存'){
				(getEditor("content",rowid,"accountFlag").next())[0].comboObj.setComboValue('1');
			}else if(rowData.accountFlag=='不计算库存'){
				(getEditor("content",rowid,"accountFlag").next())[0].comboObj.setComboValue('0');
			}
		}
	})
	
	var categorys="";
	$.ajax({
		url:'${ctx}/basedata/findSubAuxiliaryAttrTree',
		async:false,
		data:{code:"004"},
		success:function(data){
			categorys=formatTree(data[0].children,"text","id")
		}
	});
	var categoryCombo=getEditor("content",rowid,"categoryName").fool("dhxCombo",{
		height:"24px",
		width:"90px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		toolsBar:{
			name:"货品类别",
			addUrl:"/basedata/listAuxiliaryAttr",
			refresh:true
		},
		value:rowData.categoryId,
		data:categorys,
		/* required:true, */
		focusShow:true,
		editable:false,
	});

	var unitData = "";
	$.ajax({
	    url:'${ctx}/unitController/getTree',
        async:false,
        success:function (data) {
            unitData=formatTree(data[0].children,'text','id');
        }
    });

    getEditor("content",rowid,"unitName").fool("dhxCombo",{
        height:"24px",
        width:"90px",
        setTemplate:{
            input:"#name#",
            option:"#name#"
        },
        toolsBar:{
            name:"货品单位",
            addUrl:'/unitController/unitManage',
            refresh:true
        },
        data:unitData,
        value:rowData.unitId,
        focusShow:true,
        editable:false,
/*        onLoadSuccess:function (combo) {
            combo.deleteOption("");//删除空选项
        },*/
    });

    (getEditor("content",rowid,"unitName").next())[0].comboObj.attachEvent("onSelectionChange",function () {
        var value = (getEditor("content",rowid,"unitName").next())[0].comboObj.getSelectedValue();
        var noSelect = rowData.useFlag;
        //判断是否选择父节点
        if((getEditor("content",rowid,"unitName").next())[0].comboObj.getSelectedText().flag == 0){
            $.fool.alert({msg:'你选择的是单位组，请选择下拉单位。'});
            (getEditor("content",rowid,"unitName").next())[0].comboObj.setComboText("");
            return false;
        }
        if(noSelect == 1 && value != rowData.unitId){
            $.fool.alert({msg:'此货品已被使用，不能更改货品属性组'});
            (getEditor("content",rowid,"unitName").next())[0].comboObj.setComboValue(rowData.unitId);
        };
    });

	var goodsSpecs="";
	$.ajax({
		url:'${ctx}/goodsspec/getSpecGroups',
		async:false,
		success:function(data){
			goodsSpecs=formatTree(data[0].children,"text","id")
		}
	});
	var goodsSpecCombo=getEditor("content",rowid,"goodsSpecName").fool("dhxCombo",{
		height:"24px",
		width:"90px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		toolsBar:{
			name:"货品属性组",
			addUrl:"/goodsspec/manage",
			refresh:true
		},
		data:goodsSpecs,
		/* required:true, */
		focusShow:true,
		editable:false,
		value:rowData.goodsSpecId,
		/* onSelectionChange:function(rec){
			var noSelect = rowData.useFlag;
			if(noSelect==1){
				$.fool.alert({msg:'此货品已被使用，不能更改货品属性组'});
				(getEditor("content",rowid,"goodsSpecName").next())[0].comboObj.setComboValue(rowData.goodsSpecId);
			}
		} */
	});
	(getEditor("content",rowid,"goodsSpecName").next())[0].comboObj.attachEvent("onSelectionChange",function(){
		var value=(getEditor("content",rowid,"goodsSpecName").next())[0].comboObj.getSelectedValue();
		var noSelect = rowData.useFlag;
		if(noSelect==1&&value!=rowData.goodsSpecId){
			$.fool.alert({msg:'此货品已被使用，不能更改货品属性组'});
			(getEditor("content",rowid,"goodsSpecName").next())[0].comboObj.setComboValue(rowData.goodsSpecId);
		}
	});

	/* var unitCombo=getEditor("content",rowid,"unitName").fool("dhxCombo",{
		height:"24px",
		width:"90px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		data:getComboData('${ctx}/unitController/getTree',"tree"),
		editable:false,
		required:true,
		novalidate:true,
		missingMessage:'单位必须填写',
		focusShow:true,
		text:rowData.unitName,
		value:rowData.unitId, */
		/* onSelectionChange:function(rec){
			var noSelect = rowData.useFlag;
			if(noSelect==1){
				$.fool.alert({msg:'此货品已被使用，不能更改货品单位'});
				(getEditor("content",rowid,"unitName").next())[0].comboObj.setComboValue(rowData.unitId);
			}
		} */
	/* });
	(getEditor("content",rowid,"unitName").next())[0].comboObj.attachEvent("onSelectionChange",function(){
		var value=(getEditor("content",rowid,"unitName").next())[0].comboObj.getSelectedValue();
		var noSelect = rowData.useFlag;
		if(noSelect==1&&value!=rowData.unitId){
			(getEditor("content",rowid,"unitName").next())[0].comboObj.setComboValue(rowData.unitId);
		}
	}) */
}
//列表撤销
function cancelRow(index,value){
	if(value == '1'){
		$("#content").jqGrid('delRowData',index);
	}else{
		$('#content').jqGrid('setRowData', index, {editing:false,action:null});
		$("#content").jqGrid('restoreRow', index);
	}
}
//列表删除
function delRow(index,id){
	$.fool.confirm({title:'提示',msg:"确认删除该货品记录？",fn:function(data){
		if(data){
			$.post('${ctx}/goods/delete?id=' + id,function(data){
				if(data.returnCode == '0'){
					$("#content").jqGrid('delRowData',index);
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

//条码按钮点击
function getBar(index){
	var rowData=$("#content").jqGrid('getRowData',index);
	var goodsId=rowData.fid;
	var goodsName=rowData.name;
	var specGroupId=rowData.goodsSpecId;
	var unitGroupId="";
	$.ajax({
		url:"${ctx}/unitController/getByGoodsId",
		async:false,
		data:{goodsId:goodsId},
		success:function(data){
			unitGroupId=data;
		}
	});
	var url=encodeURI(encodeURI("${ctx}/goods/barGrid?goodsId="+goodsId+"&goodsName="+goodsName+"&unitGroupId="+unitGroupId+"&specGroupId="+specGroupId));
	$(".barBox").window("setTitle","条码");
	$(".barBox").window("open");
	$(".barBox").window("refresh",url);
}

function getTrans(index){
	var rowData=$("#content").jqGrid('getRowData',index);
	var goodsId=rowData.fid;
	var goodsName=rowData.name;
	var specGroupId=rowData.goodsSpecId;
	var unitGroupId="";
	var unitId=rowData.unitId;
	var unitName=rowData.unitName;
	$.ajax({
		url:"${ctx}/unitController/getByGoodsId",
		async:false,
		data:{goodsId:goodsId},
		success:function(data){
			unitGroupId=data;
		} 
	});
	var url=encodeURI(encodeURI("${ctx}/goodsTransport/manage?goodsId="+goodsId+"&goodsName="+goodsName+"&unitGroupId="+unitGroupId+"&specGroupId="+specGroupId+"&unitId="+unitId+"&unitName="+unitName));
	$(".barBox").window("setTitle","货品运输计价换算关系");
	$(".barBox").window("open");
	$(".barBox").window("refresh",url);
}

function mysearch(value){
	var key = $('#searchBox2').searchbox('getValue');
	var node=$('#goodsTree').tree("getSelected");
	$('#content').jqGrid('setGridParam',{url:"${ctx}/goods/getChilds",postData:{parentId:node.id,showChild:value,showDisable:1}}).trigger("reloadGrid");
}
//列表保存
function saveRow(index,id){
	var rowData=$("#content").jqGrid('getRowData',index);
	var a =getEditor("content",index,"unitName");
	var b =getEditor("content",index,"code");
	var c =getEditor("content",index,"name");
	var d =getEditor("content",index,"percentage");
	$(a[0]).validatebox('enableValidation');
	$(b[0]).validatebox('enableValidation');
	$(c[0]).validatebox('enableValidation');
	$(d[0]).validatebox('enableValidation');
	if(a.validatebox('isValid') && b.validatebox('isValid') && c.validatebox('isValid')&& d.validatebox('isValid')){
		var categoryName="";
		var categoryId="";
		if((getEditor("content",index,"categoryName").next())[0].comboObj.getSelectedValue()!=""){
			categoryName=(getEditor("content",index,"categoryName").next())[0].comboObj.getSelectedText().name;
			categoryId=(getEditor("content",index,"categoryName").next())[0].comboObj.getSelectedValue();
		}
		var goodsSpecName="";
		var goodsSpecId="";
		if((getEditor("content",index,"goodsSpecName").next())[0].comboObj.getSelectedValue()!=""){
			goodsSpecName=(getEditor("content",index,"goodsSpecName").next())[0].comboObj.getSelectedText().name;
			goodsSpecId=(getEditor("content",index,"goodsSpecName").next())[0].comboObj.getSelectedValue();
		}
		var unitName=(getEditor("content",index,"unitName").next())[0].comboObj.getSelectedText().name;
		var unitId=(getEditor("content",index,"unitName").next())[0].comboObj.getSelectedValue();
		var name=$(c[0]).val();
		var code=$(b[0]).val();
		var describe=getEditor("content",index,"describe").val();
		var spec=getEditor("content",index,"spec").val();
		var accountFlag=(getEditor("content",index,"accountFlag").next())[0].comboObj.getSelectedValue();
		var recordStatus=(getEditor("content",index,"recordStatus").next())[0].comboObj.getSelectedValue();
		var barCode=getEditor("content",index,"barCode").val();
		var percentage=getEditor("content",index,"percentage").val();
		//去掉前后空格
		barCode=barCode.replace(/^\s+|\s+$/g,"");
		var myDate = new Date();
		if(id == 'undefined'){
			id = '';
			var updateTime=myDate.format('yyyy-MM-dd hh:mm:ss');
		}else{
			var updateTime=rowData.updateTime;
		}
		$.ajax({
			type:'post',
			url:'${ctx}/goods/checkGoodsName',
			cache:false ,
			data:{fid:id,goodsName:name},
			dataType:'json',
			success:function(re){
				if(re.returnCode==1){
					$.fool.confirm({msg:'货品名称已存在，确定保存？',title:'提示',fn:function(r){
						if(r){
							$.ajax({
								type:'post',
								url:'${ctx}/goods/save',
								cache:false ,
								data:{flag:1,accountFlag:accountFlag,spec:spec,recordStatus:recordStatus,updateTime:updateTime,fid:id,parentId:parentId,name:name,code:code,describe:describe
										,categoryId:categoryId,goodsSpecId:goodsSpecId,unitId:unitId,barCode:barCode,percentage:percentage},
								dataType:'json',
								success:function(data){
									if(data.returnCode == '0'){
										$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
											if(dhxkey == 1){
						                        selectTab(dhxname,dhxtab);
						                    }
											$("#content").jqGrid('saveRow',index);
											$('#content').jqGrid('setRowData', index, {
												editing:false,
												action:null,
												isNew:"",
												fid:data.data.fid,
									    		action:'',
									    		categoryName:categoryName,
									    		goodsSpecName:goodsSpecName,
									    		unitName:unitName,
									    		updateTime:data.data.updateTime,
									    		unitId:unitId,
									    		flag:1,
									    		accountFlag:accountFlag,
									    		recordStatus:recordStatus,
									    		parentId:parentId,
									    		name:name,
									    		spec:spec,
									    		code:code,
									    		barCode:barCode,
									    		describe:describe,
									    		categoryId:categoryId,
												goodsSpecId:goodsSpecId,
											});
										}});
									}else if(data.returnCode = '1'){
										$.fool.alert({msg:data.message});
									}else{
										$.fool.alert({msg:'服务器繁忙，请稍后再试'});
									}
								}
							});
						}
					}});
				}else{
					$.ajax({
						type:'post',
						url:'${ctx}/goods/save',
						cache:false ,
						data:{flag:1,accountFlag:accountFlag,spec:spec,recordStatus:recordStatus,updateTime:updateTime,fid:id,parentId:parentId,name:name,code:code,describe:describe
								,categoryId:categoryId,goodsSpecId:goodsSpecId,unitId:unitId,barCode:barCode,percentage:percentage},
						dataType:'json',
						success:function(data){
							if(data.returnCode == '0'){
								$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
									if(dhxkey == 1){
				                        selectTab(dhxname,dhxtab);
				                    }
									$("#content").jqGrid('saveRow',index);
									$('#content').jqGrid('setRowData', index, {
										editing:false,
										action:null,
										isNew:"",
										fid:data.data.fid,
							    		action:'',
							    		categoryName:categoryName,
							    		goodsSpecName:goodsSpecName,
							    		unitName:unitName,
							    		updateTime:data.data.updateTime,
							    		unitId:unitId,
							    		flag:1,
							    		accountFlag:accountFlag,
							    		recordStatus:recordStatus,
							    		parentId:parentId,
							    		name:name,
							    		spec:spec,
							    		code:code,
							    		barCode:barCode,
							    		describe:describe,
							    		categoryId:categoryId,
										goodsSpecId:goodsSpecId,
									});
								}});
							}else if(data.returnCode = '1'){
								$.fool.alert({msg:data.message});
							}else{
								$.fool.alert({msg:'服务器繁忙，请稍后再试'});
							}
						}
					});
				}
			}
		});
		return;
	}
}

//导入回调
function okCallback(){
	$("#goodsTree").tree("reload");
	$("#content").trigger("reloadGrid"); 
	$("#pop-window").window("close");
}
 
//是否为第一层节点
 function isFirst(node){
 	return (typeof node.attributes != 'undefined'&&node.attributes.isFirst != undefined);
 }
//添加 
$('#add').click(function(){
	if($('#add').attr("disabled")=="disabled"){
		return false;
	}
	var node = getSelectNode();
		if(node.attributes.detail.flag==1){
			$.fool.alert({msg:'货品不能添加下一级'});
			return;
		}
	mywin=$.fool.window({
		title:'新增货品组',
		width:700,
		height:400,
		href:'${ctx}/goods/add?parentId='+node.id,
		});
});
//获取选中节点
 function getSelectNode(){
 	var node = $('#goodsTree').tree('getSelected');
 	if(!node){
 		$.fool.alert({msg:'请选择选择货品或货品组'});
 		return;
 	}
 	return node;
 }
 $("#searchBox").searchbox({
	 prompt:'请输入搜索内容',
	 height:'38px',
	 iconWidth:26,
	 searcher:function(){
		 $("#goodsTree").tree("search", {searchText:$(this).val(),compar:function(searchText,node){
			if(node.attributes==undefined||node.attributes.detail==undefined)return false;
			var detail = node.attributes.detail;
			return $.trim(detail.code)!="" && detail.code.indexOf(searchText)!=-1;
		 }});
	 }
 });
 $("#searchBox2").searchbox({
	 prompt:'编号或名称',
	 height:'30px',
	 iconWidth:26,
	 searcher:function(value,name){
		 var showChild = $("input[name='showChild']:checked").val();
		 var node=$('#goodsTree').tree("getSelected");
		 var rows=$('#content').getGridParam().rowNum;
			$.ajax({
				url:"${ctx}/goods/getChilds",
				async:false,
				data:{parentId:node.id,showChild: showChild,searchKey: value,rows:rows,showDisable:1},
				success:function(data){
					var mygrid = jQuery('#content')[0]; 
					if(node.attributes.detail.flag == 0){
						mygrid.addJSONData(data);
						$('#content').jqGrid('footerData',"set",{action:'new'});
					} 
				}
			});
	 }
 });
 
 function getSelectAddNode(){
	 	var node = $('#goodsTree').tree('getSelected');
	 	return node;
}
 
//更新节点
 function updateNode(data){
	 var node = getSelectNode();
	 $('#goodsTree').tree('update',{
         target: node.target,
         text:data.name,
         attributes:data
	 });
	 $(node.target).click();
 }
 
 //增加节点
 function addNode(fid,data){
	 var node = getSelectNode();
	 var nodes = [{
         "id":fid,
         "text":data.name,
         "attributes":data
     }];
	 
     $('#goodsTree').tree('append', {
         parent:node.target,
         data:nodes
     });
 }
 //修改
 $('#update').click(function(){
	 var node = getSelectNode();
	 var root = $("#goodsTree").tree("getRoot");
	 if(node.text==root.text){
	 	$.fool.alert({msg:'该货品组不能修改!'});
	 	return;
	 }
	 if(isFirst(node)){
	 	$.fool.alert({msg:'该货品组不允许修改'});
	 	return;
	 }
	 mywin = $.fool.window({
			title:'编辑货品',
			width:700,
			height:400,
			href:'${ctx}/goods/edit?id='+node.id,
			});
 });
//删除 
 $('#delete').click(function(){
	var node = getSelectNode();
	if(isFirst(node)){
		$.fool.alert({msg:'该货品不允许删除'});
		return;
	}
	var root = $("#goodsTree").tree("getRoot");
	if(node.id==root.id){
		$.fool.alert({msg:'该货品组不允许删除'});
		return;
	} 
	
	
	$.fool.confirm({msg:'确定要删除该记录吗？',title:'提示',fn:function(r){
		if(r){
			$.post('${ctx}/goods/delete?id=' + node.id,function(data){
				if(data.returnCode == '0'){
				    $('#goodsTree').tree('remove' , node.target); 
				    $.fool.alert({time:1000,msg:'删除成功！'});
				    fromEnable("#goodForm",false).form('clear');
				}else if(data.returnCode = '1'){
					$.fool.alert({msg:data.message});
				}else{
					$.fool.alert({msg:'服务器繁忙，请稍后再试'});
				}
			},'json');
		}
	}});
});
 $('#goodsTree').css('margin-top',$('.btnBox').outerHeight());
 
 //获取jqGrid编辑框
 function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
 }
 //封装jqGrid列表数据
 function setData(data){
 }
 
 $.extend($.fn.validatebox.defaults.rules, {
	 percentage:{
		 validator: function (value) {
			 var b = (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
			 if(!b){
				 return false;
			 }else{
				 return (value>=0&&value<=100);
			 }
		 },
		 message: '请输入0-100之间的数字,最多两位小数。'
	 },
 })
</script>
</body>
</html>
