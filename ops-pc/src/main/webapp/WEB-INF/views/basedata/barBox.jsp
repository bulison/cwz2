<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
       <a href="javascript:;" id="closeBar" class="btn-blue2 btn-s2" onClick='closeBar()' style="margin:5px 10px">返回</a><h2 class="title" style="display: inline-block;"></h2>
       <table id="barGrid"></table>
       <div id="barPager"></div>
       <a href="javascript:;" id="saveBar" class="btn-blue2 btn-s2" onClick='saveBar()' style="width:65px;margin:10px auto;display:block;">保存</a>
<script type="text/javascript">
$(function(){
	$(".title").text(decodeURI(decodeURI("${param.goodsName}")));
	
	$('#barGrid').jqGrid({
		datatype:function(postdata){
			postdata.goodsId='${param.goodsId}';
			postdata.num=Math.random();
			$.ajax({
				url: '${ctx}/basedata/goodsBar/query',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$('#barGrid')[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		footerrow: true,
		autowidth:true,
		height:380,
		pager:"#barPager",
		rowNum:10,
		rowList:[10,20,30],
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[ 
		          {name:"action",label:"操作",align:'center',formatter:function(cellvalue, options, rowObject){
		        	  var e='',d='',c='',s='';
		        	  //屏蔽编辑按钮
		        	  d = "<a href='javascript:;' onClick='delBarRow(this,\""+options.rowId+"\")' class='btn-del' title='删除'></a>";
	                  c = "<a href='javascript:;' onClick='cancelBarRow(this,\""+rowObject.isNew+"\",\""+options.rowId+"\")' class='btn-back' title='撤销'></a>";
	                  s = "<a href='javascript:;' onClick='saveBarRow(this,\""+options.rowId+"\")' class='btn-save' title='保存'></a>";
	                  if(cellvalue == 'new'){
	                	  return "<a href='javascript:;' class='btn-add' onclick='addBarRow()' title='新增'>新增</a>";
	                  }else{
	                	  if(rowObject.editing){
	                		  return s+c;
	                	  }else{
	                		  return d;
	                	  }
	                  }
		          }},
                  {name : 'fid',label : 'fid',hidden:true},
                  {name : 'isNew',label : 'isNew',hidden:true},
                  {name : 'unitId',label : 'unitId',hidden:true},
                  {name : 'editing',label : 'editing',hidden:true}, 
                  {name : 'updateTime',label : 'updateTime',hidden:true}, 
                  {name : 'goodsSpecId',label : 'goodsSpecId',hidden:true}, 
                  {name : 'barCode',label : '条码',align:'center',width:"200px",editable:true,edittype:"text"}, 
                  {name : 'goodsSpecName',label : '货品属性',align:'center',width:"100px",editable:true,edittype:"text"}, 
                  {name : 'unitName',label : '单位',align:'center',editable:true,edittype:"text"}, 
                  ],
        onSelectRow:function(rowid){
        	editBarRow(rowid);
        }
	}).navGrid('#barPager',{add:false,del:false,edit:false,search:false,view:false});
	$('#barGrid').jqGrid('footerData',"set",{action:'new'});
}); 
 
//新增一行
function addBarRow(){
	var ids=$('#barGrid').getDataIDs();
	console.log(ids);
	var newIndex =0;
	if(ids.length>0){
		newIndex=ids[ids.length-1]+1;
	}
	$('#barGrid').jqGrid('addRowData',newIndex,{isNew:1},"last");
	editBarRow(newIndex); 
}

//列表撤销
function cancelBarRow(obj,value,index){
	if(value == '1'){
		$('#barGrid').jqGrid('delRowData',index);
	}else{
		$('#barGrid').jqGrid('setRowData', index, {editing:false,action:null});
		$('#barGrid').jqGrid('restoreRow', index);
	}
}

//列表保存
function saveBarRow(obj,rowid){
	var row=$(obj).closest("tr");
	row.form("enableValidation");
	if(row.form('validate')){
		var barCode=getEditor("barGrid",rowid,"barCode").val();
		var unitName=(getEditor("barGrid",rowid,"unitName").next())[0].comboObj.getSelectedText().name;
		var goodsSpecName=(getEditor("barGrid",rowid,"goodsSpecName").next())[0].comboObj.getSelectedText().name;
		var unitId=(getEditor("barGrid",rowid,"unitName").next())[0].comboObj.getSelectedValue();
		var goodsSpecId=(getEditor("barGrid",rowid,"goodsSpecName").next())[0].comboObj.getSelectedValue();
		barCode=barCode.replace(/^\s+|\s+$/g,"");
		$("#barGrid").jqGrid('saveRow',rowid);
		$('#barGrid').jqGrid('setRowData', rowid, {
			editing:false,
			action:null,
    		action:'',
    		isNew:0,
    		unitName:unitName,
    		unitId:unitId,
    		goodsSpecName:goodsSpecName,
    		goodsSpecId:goodsSpecId
		});
	}else{
		return false;
	}
}

//列表编辑 
//进入编辑状态时设置单位和属性选择框的值，防止保存时ID获取了名称导致后台报错。
function editBarRow(rowid){
	var rowData=$('#barGrid').jqGrid('getRowData',rowid);
	if(rowData.editing=="true"){
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#barGrid').jqGrid('setRowData', rowid, rowData);
	$('#barGrid').jqGrid('editRow',rowid);
	getEditor("barGrid",rowid,"barCode").validatebox({
		required:true,
		novalidate:true,
	});
	
	var goodsSpecs="";
	$.ajax({
		url:"${ctx}/goodsspec/getChidlList?groupId=${param.specGroupId}",
		async:false,
		success:function(data){
			goodsSpecs=formatData(data,"fid")
		}
	});
	var goodsSpecCombo=getEditor("barGrid",rowid,"goodsSpecName").fool("dhxCombo",{
		height:"24px",
		width:"148px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		required:true,
		focusShow:true,
		data:goodsSpecs,
	});
	goodsSpecCombo.readonly(true);
	goodsSpecCombo.allowFreeText(false);
	var units="";
	$.ajax({
		url:"${ctx}/unitController/getChilds?unitGroupId=${param.unitGroupId}",
		async:false,
		success:function(data){
			units=formatData(data,"fid")
		}
	});
	var unitCombo=getEditor("barGrid",rowid,"unitName").fool("dhxCombo",{
		height:"24px",
		width:"220px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		required:true,
		focusShow:true,
		data:units,
	});
	unitCombo.readonly(true);
	unitCombo.setComboValue(rowData.unitId);
	//货品无属性组时禁选属性
	if(!"${param.specGroupId}"){
		goodsSpecCombo.disable();
	}else{
		goodsSpecCombo.setComboValue(rowData.goodsSpecId);
	}
	getEditor("barGrid",rowid,"barCode").focus();
	getEditor("barGrid",rowid,"barCode").select();
}

//列表删除
function delBarRow(obj,index){
	$("#barGrid").jqGrid('delRowData',index);
}

//关闭条码窗
function closeBar(){
	$(".barBox").window("close");
}

//保存条码
function saveBar(){
	var rowDate=$("#barGrid").jqGrid('getRowData');
	for(var i=0;i<rowDate.length;i++){
		if(rowDate[i].editing=="true"){
			$.fool.alert({msg:'您还有未确认的条码信息，请先确认。'});
			return;
		}
	}
	for(var i=0;i<rowDate.length;i++){
		rowDate[i].goodsId="${param.goodsId}";
	}
	$.ajax({
		url:"${ctx}/basedata/goodsBar/saveMulti",
		type:"post",
		async:false,
		data:{vos:JSON.stringify(rowDate),goodsId:"${param.goodsId}"},
		success:function(data){
			if(data.returnCode == '0'){ 
			    $.fool.alert({time:1000,msg:'保存成功！'});
			}else if(data.returnCode = '1'){
				$.fool.alert({msg:data.message});
			}else{
				$.fool.alert({msg:'服务器繁忙，请稍后再试'});
			}
		}
	});
}
</script>
</body>
</html>
