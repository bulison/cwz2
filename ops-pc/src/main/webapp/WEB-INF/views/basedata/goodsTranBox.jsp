<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
       <a href="javascript:;" id="closeBar" class="btn-blue2 btn-s2" onClick='closeBar()' style="margin:5px 10px">返回</a><h2 class="title" style="display: inline-block;"></h2>
       <table id="barGrid"></table>
       <div id="barPager"></div> 
      <!--  <a href="javascript:;" id="saveBar" class="btn-blue2 btn-s2" onClick='saveBar()' style="width:65px;margin:10px auto;display:block;">保存</a> -->
<script type="text/javascript">
$(function(){
	$(".title").text(decodeURI(decodeURI("${param.goodsName}")));
	
	$('#barGrid').jqGrid({
		datatype:function(postdata){
			postdata.goodsId='${param.goodsId}';
			postdata.num=Math.random();
			$.ajax({
				url: '${ctx}/goodsTransport/list',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$('#barGrid')[0].addJSONData(data.responseJSON);
		        		var rows=$('#barGrid').find(".jqgrow");
		        		for(var i=0;i<rows.length;i++){
		        			var scale=$(rows[i]).find("td[aria-describedby='barGrid_conversionRate']").text();
		        			var tUnit=$(rows[i]).find("td[aria-describedby='barGrid_transportUnitName']").text();
		        			var gUnit=$(rows[i]).find("td[aria-describedby='barGrid_unitName']").text();
		        			$(rows[i]).find("td[aria-describedby='barGrid_conversionRate']").attr("title","1"+tUnit+"="+scale+gUnit);
		        		}
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
		        	  d = "<a href='javascript:;' onClick='delBarRow(\""+options.rowId+"\",\""+rowObject.fid+"\")' class='btn-del' title='删除'></a>";
	                  c = "<a href='javascript:;' onClick='cancelBarRow(this,\""+rowObject.isNew+"\",\""+options.rowId+"\",\""+rowObject.fid+"\")' class='btn-back' title='撤销'></a>";
	                  s = "<a href='javascript:;' onClick='saveBarRow(this,\""+options.rowId+"\",\""+rowObject.fid+"\")' class='btn-save' title='保存'></a>";
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
                  {name : 'sysSign',label : 'sysSign',hidden:true},
                  {name : 'shipmentTypeId',label : 'shipmentTypeId',hidden:true},
                  {name : 'transportUnitId',label : 'transportUnitId',hidden:true},
                  {name : 'unitId',label : 'unitId',hidden:true},
                  {name : 'editing',label : 'editing',hidden:true}, 
                  {name : 'updateTime',label : 'updateTime',hidden:true}, 
                  {name : 'goodSpecId',label : 'goodSpecId',hidden:true}, 
                  {name : 'shipmentTypeName',label : '装运方式',align:'center',width:"100px",editable:true,edittype:"text"},
                  {name : 'transportUnitName',label : '运输单位',align:'center',width:"100px",editable:true,edittype:"text"},
                  {name : 'unitName',label : '货品单位',align:'center',editable:true,edittype:"text"},
                  {name : 'goodSpecName',label : '货品属性',align:'center',width:"100px",editable:true,edittype:"text"}, 
                  {name : 'conversionRate',label : '换算关系',align:'center',width:"80px",editable:true,edittype:"text"},
                  {name : '_sysSign',label : '来源',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
			    	  if(rowObject.sysSign == '1'){
	                	  return "系统录入";
	                  }else{
	                	  return "手动录入";
	                  }
		          }},
                  {name : 'describe',label : '描述',align:'center',width:"100px",editable:true,edittype:"text"}, 
                  ],
        onSelectRow:function(rowid){
        	editBarRow(rowid);
        },
	}).navGrid('#barPager',{add:false,del:false,edit:false,search:false,view:false});
	$('#barGrid').jqGrid('footerData',"set",{action:'new'});
}); 
 
//新增一行
function addBarRow(){
	var ids=$('#barGrid').getDataIDs();
	var newIndex =0;
	if(ids.length>0){
		newIndex=ids[ids.length-1]+1;
	}
	$('#barGrid').jqGrid('addRowData',newIndex,{isNew:1,unitId:"${param.unitId}",unitName:"${param.unitName}"},"last");
	
	/* var newIndex = $('#barGrid').jqGrid('getRowData').length+1;
	$('#barGrid').jqGrid('addRowData',newIndex,{isNew:1,unitId:"${param.unitId}",unitName:"${param.unitName}"},newIndex); */
	editBarRow(newIndex);
}

//列表撤销
function cancelBarRow(obj,value,index,fid){
	if(value == '1'){
		$('#barGrid').jqGrid('delRowData',index);
	}else{
		$('#barGrid').jqGrid('setRowData', index, {editing:false,action:null,fid:fid});
		$('#barGrid').jqGrid('restoreRow', index);
	}
}

//列表保存
function saveBarRow(obj,rowid,fid){
	var row=$(obj).closest("tr");
	var rowData=$('#barGrid').jqGrid('getRowData',rowid);
	row.form("enableValidation");
	if(row.form('validate')){
		var conversionRate=getEditor("barGrid",rowid,"conversionRate").numberbox("getValue");
		var unitId=(getEditor("barGrid",rowid,"unitName").next())[0].comboObj.getSelectedValue();
		var unitName=(getEditor("barGrid",rowid,"unitName").next())[0].comboObj.getSelectedText();
		var goodSpecId=(getEditor("barGrid",rowid,"goodSpecName").next())[0].comboObj.getSelectedValue();
		var goodSpecName=(getEditor("barGrid",rowid,"goodSpecName").next())[0].comboObj.getSelectedText();
		var shipmentTypeId=(getEditor("barGrid",rowid,"shipmentTypeName").next())[0].comboObj.getSelectedValue();
		var shipmentTypeName=(getEditor("barGrid",rowid,"shipmentTypeName").next())[0].comboObj.getSelectedText();
		var transportUnitId=(getEditor("barGrid",rowid,"transportUnitName").next())[0].comboObj.getSelectedValue();
		var transportUnitName=(getEditor("barGrid",rowid,"transportUnitName").next())[0].comboObj.getSelectedText();
		var describe=getEditor("barGrid",rowid,"describe").val();
		$.ajax({
			url:"${ctx}/goodsTransport/save",
			type:"post",
			async:false,
			data:{sysSign:0,fid:fid,updateTime:rowData.updateTime,conversionRate:conversionRate,unitId:unitId,goodSpecId:goodSpecId,shipmentTypeId:shipmentTypeId,transportUnitId:transportUnitId,describe:describe,goodsId:"${param.goodsId}"},
			success:function(data){
				if(data.returnCode == '0'){ 
				    $.fool.alert({time:1000,msg:'保存成功！'});
				    $('#barGrid').setRowData(rowid,{isNew:false,updateTime:data.data.updateTime,fid:data.data.fid,editing:false,action:null,unitName:unitName.name,goodSpecName:goodSpecName.name,shipmentTypeName:shipmentTypeName.name,transportUnitName:transportUnitName.name,unitId:unitName.fid,goodSpecId:goodSpecName.fid,shipmentTypeId:shipmentTypeName.fid,transportUnitId:transportUnitName.fid});
				    $('#barGrid').saveRow(rowid);
				    /* $('#barGrid').trigger("reloadGrid"); */
				}else if(data.returnCode = '1'){
					$.fool.alert({msg:data.message});
				}else{
					$.fool.alert({msg:'服务器繁忙，请稍后再试'});
				}
			}
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
	var shipmentTypeCombo=getEditor("barGrid",rowid,"shipmentTypeName").fool("dhxCombo",{
		height:"24px",
		width:"148px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		editable:false,
		required:true,
		focusShow:true,
		data:getComboData("${ctx}/basedata/findSubAuxiliaryAttrTree?code=020","tree"),
	});
	shipmentTypeCombo.setComboValue(rowData.shipmentTypeId);
	var transportUnitCombo=getEditor("barGrid",rowid,"transportUnitName").fool("dhxCombo",{
		height:"24px",
		width:"148px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		editable:false,
		required:true,
		focusShow:true,
		data:getComboData("${ctx}/basedata/findSubAuxiliaryAttrTree?code=021","tree"),
	});
	transportUnitCombo.setComboValue(rowData.transportUnitId); 
	var goodsSpecs="";
	$.ajax({
		url:"${ctx}/goodsspec/getChidlList?groupId=${param.specGroupId}",
		async:false,
		success:function(data){
			goodsSpecs=formatData(data,"fid")
		}
	});
	var goodsSpecCombo=getEditor("barGrid",rowid,"goodSpecName").fool("dhxCombo",{
		height:"24px",
		width:"148px",
		required:true,
		novalidate:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
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
	unitCombo.disable(true);
	if(rowData.unitId){
		unitCombo.setComboValue(rowData.unitId);
	}else{
		$.ajax({
			url:"${ctx}/unitController/findAccountUnit?parentId=${param.unitGroupId}",
			async:false,
			success:function(data){
				unitCombo.setComboValue(data.fid);
			}
		});
	}
	getEditor("barGrid",rowid,"conversionRate").numberbox({
		required:true,
		height:"24px",
		width:"220px",
		precision:2,
		validType:"intOrFloat",
	});
	getEditor("barGrid",rowid,"conversionRate").numberbox("textbox").hover(
			function(){
				var scale=getEditor("barGrid",rowid,"conversionRate").numberbox("getText");
				var tUnit=(getEditor("barGrid",rowid,"transportUnitName").next())[0].comboObj.getComboText();
				var gUnit=(getEditor("barGrid",rowid,"unitName").next())[0].comboObj.getComboText();
				$(getEditor("barGrid",rowid,"conversionRate").numberbox("textbox")).tooltip({
				    position: 'left',
				    content: '<span style="color:black">1'+tUnit+'='+scale+gUnit+'</span>',
				    onShow: function(){
				    	$(this).tooltip('tip').css({
							  backgroundColor: '#FFFFCC',            
							  borderColor: '#CC9933'        
				    	});  
				    }
				});
				$(getEditor("barGrid",rowid,"conversionRate").numberbox("textbox")).tooltip("show");
			},
			function(){
				$(getEditor("barGrid",rowid,"conversionRate").numberbox("textbox")).tooltip("hide");
			}
		);
	getEditor("barGrid",rowid,"conversionRate").numberbox("textbox").focus(function(){
		$(this).select();
	});
	getEditor("barGrid",rowid,"conversionRate").numberbox("textbox").blur(function(){
		$(getEditor("barGrid",rowid,"conversionRate").numberbox("textbox")).tooltip("hide");
	});
	
	getEditor("barGrid",rowid,"describe").validatebox({
		validType:"maxLength[50]",
	});
	//货品无属性组时禁选属性
	if(!"${param.specGroupId}"){
		goodsSpecCombo.disable();
		$(goodsSpecCombo.getInput()).validatebox("disableValidation");
	}else{
		goodsSpecCombo.setComboValue(rowData.goodSpecId);
	}
	getEditor("barGrid",rowid,"barCode").focus();
	getEditor("barGrid",rowid,"barCode").select();
}

//列表删除
function delBarRow(index,fid){
	$.fool.confirm({msg:'确定要删除该记录吗？',title:'提示',fn:function(r){
		if(r){
			var rowData=$('#barGrid').jqGrid('getRowData',index);
			$.post('${ctx}/goodsTransport/delete?fid=' + fid,function(data){
				if(data.returnCode == '0'){
					/* $("#barGrid").jqGrid('delRowData',index); */
					$('#barGrid').trigger("reloadGrid");
				}else if(data.returnCode = '1'){
					$.fool.alert({msg:data.message});
				}else{
					$.fool.alert({msg:'服务器繁忙，请稍后再试'});
				}
			},'json');
		}
	}});
}

//关闭条码窗
function closeBar(){
	$(".barBox").window("close");
}

/* function getTip(){
	var scale=$("#barGrid").find("tr#"+rowid).find("td[aria-describedby='barGrid_conversionRate']").text();
	var tUnit=$("#barGrid").find("tr#"+rowid).find("td[aria-describedby='barGrid_transportUnitName']").text();
	var gUnit=$("#barGrid").find("tr#"+rowid).find("td[aria-describedby='barGrid_unitName']").text();
	return 
} */
</script>
</body>
</html>
