<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<style type="text/css">
  /* #bodyBox{
      width:100%;
      overflow: auto;
  } */
  .groundBox{
      width:15%;
      display: inline-block;
      position: relative;
  }
  .detailBox{
      width:84%;
      display: inline-block;
      position: absolute;
      background: white;
      margin-left:10px
  }
  .btnBox{
      border:1px solid #CCC;
      width:100%;
  }
  .btn{
      display:inline-block;
      font-size:12px;
      text-decoration:none;
      color:rgb(92,174,232);
      background-repeat:no-repeat;
      height:21px;
      line-height:22px;
      background-position:15px 5px;
      margin:0px;
      padding:5px 6px 2px 30px;/*所有识别*/
      .padding:5px 10px 2px 35px\9;/*IE6、7、8识别*/
      +padding:5px 10px 2px 35px;/*IE6、7识别*/
      _padding:5px 10px 2px 35px;/*IE6识别*/
  }
  .deleteBtn{background-image:url(${ctx}/resources/images/delete-tree.png);padding-left:40px}
  .updateBtn{background-image:url(${ctx}/resources/images/edit-tree.png);padding-left:40px}
  #groundMsg p{display: inline-block;margin-right: 20px;font-size: 15px;margin:10px 20px 5px 0px}
  /* #gbox_groundTree .ui-jqgrid-hdiv{display:none}   */
  .btn-add{
    width:40%;
    height: 100%;
    background-color:#1BB9FA;
    background-image:url(${ctx}/resources/images/add.png);
    background-repeat:no-repeat;
    background-position:20px 5px;
    border:#1BB9FA;
    border-radius:3px;
    font-size: 14px;
    text-align: center;
    color: white !important;
    line-height: 30px;
    vertical-align: middle;
  }
  .btn-add:hover{
    width:40%;
    height: 100%;
    background-color:#1BB9FA;
    background-image:url(${ctx}/resources/images/add_on.png);
    background-repeat:no-repeat;
    background-position:20px 5px;
    border:#1BB9FA;
    border-radius:3px;
    font-size: 14px;
    vertical-align: middle;
    text-align: center;
    color: white;
    line-height: 30px;
    vertical-align: middle;
  }
</style>
</head>
<body>
    <div class="titleCustom">
        <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>运输费计价单位</h1>
        </div>             
    </div>
    <div id="bodyBox">
        <div class="listBox"> 
    	    <table id="unitList"></table>
    	    <div id="pager"></div>
        </div> 
    </div>
        
<script type="text/javascript">
var categoryId="";
$("#unitList").jqGrid({
	datatype:function(postdata){
		$("#unitList").jqGrid("footerData","set",{"action":"new"});
		$.ajax({
			url: '${ctx}/transportFeeUnit/query',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	/* var rows=data.responseJSON;
	        	$("#unitList")[0].addJSONData(rows); */
	        	data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
        		$("#unitList")[0].addJSONData(data.responseJSON);
	        }
		});
	},
	footerrow: true,
	pager:"#pager",
	autowidth:true,
	height:500, 
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
			    	if(cellvalue == 'new'){
			    		return "<a href='javascript:;' class='btn-add' onclick='addrow()' title='新增'>新增</a>";
			    	}else{
			    		if(rowObject.editing){
			    			return s+"  "+c;
			    		}else{
			    			//屏蔽编辑按钮
			    			return d;
			    		}
			    	}
	            }},
	            {name : 'categoryId',label : 'categoryId',hidden:true},
	            {name : 'parentId',label : 'parentId',hidden:true},
	            {name : 'updateTime',label : 'updateTime',hidden:true},
	            {name : 'isNew',label : 'isNew',hidden:true},
	            {name : 'first',label : 'first',hidden:true},
	            {name : 'editing',label : 'editing',hidden:true}, 
                /* {name : 'updateTime',label : 'updateTime',hidden:true}, */ 
                {name : 'fid',label : 'fid',hidden:true}, 
                {name : 'enable',label : 'enable',hidden:true,editable:true,edittype:"text"}, 
                {name : 'code',label : '编号',align:'center',width:"100px",editable:true,edittype:"text"},
                {name : 'name',label : '名称',align:'center',width:"100px",editable:true,edittype:"text"},
                {name : 'scale',label : '换算单位',align:'center',width:"100px",editable:true,edittype:"text"},
                {name : '_enable',label : '是否启用',align:'center',width:"100px",editable:true,edittype:"text",formatter:function(value,option,row){
                	if(row.enable=="1"){
                		return "是";
                	}else if(row.enable=="0"){
                		return "否";
                	}else{
                		return "";
                	}
                }},
                {name : 'describe',label : '描述',align:'center',width:"100px",editable:true,edittype:"text"},
              ],
    onSelectRow:function(rowid){
    	editRow(rowid);
    },
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});  
$("#unitList").jqGrid("footerData","set",{"action":"new"});

//列表新增
function addrow(){
	/* var length = $("#unitList").jqGrid('getRowData').length;
	$("#unitList").jqGrid('addRowData',length-1,{costSign:0,accountFlag:1,isNew:1,enable:0,categoryId:categoryId,first:"isFirst",parentId:categoryId},"last"); */
	var ids=$('#unitList').getDataIDs();
	var newIndex =0;
	if(ids.length>0){
		newIndex=ids[ids.length-1]+1;
	}
	$('#unitList').jqGrid('addRowData',newIndex,{isNew:1},"last");
	editRow(newIndex);
}
//列表编辑 参数改为index
function editRow(rowid){
	var rowData=$("#unitList").jqGrid('getRowData',rowid);
	if(rowData.editing=="true"||rowData.action=='<a href="javascript:;" class="btn-add" onclick="addrow()" title="新增货品"></a>'){ 
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#unitList').jqGrid('setRowData', rowid, rowData);
	$("#unitList").jqGrid('editRow',rowid);
	getEditor("unitList",rowid,"code").textbox({
		missingMessage:"该项为必填项",
		height:"98%",
		required:true,
		validType:['maxLength[50]',"isBank"],
	});
	getEditor("unitList",rowid,"name").textbox({
		missingMessage:"该项为必填项",
		height:"98%",
		required:true,
		validType:['maxLength[50]',"isBank"],
	});
	getEditor("unitList",rowid,"scale").numberbox({
		missingMessage:"该项为必填项",
		height:"98%",
		required:true,
		precision:2,
		validType:'numMaxLength[10]',
		max:9999999999.99
	});
	getEditor("unitList",rowid,"_enable").fool("dhxCombo",{
		height:"98%",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		data:[{value:"0",text:"否"},{value:"1",text:"是"}],
		value:rowData.enable,
		editable:false,
		focusShow:true,
		required:true,
		onChange:function(value,text){
			getEditor("unitList",rowid,"enable").val(value);
		}
	});
	getEditor("unitList",rowid,"describe").textbox({
		height:"98%",
		validType:"maxLength[50]"
	});
}
//列表撤销
function cancelRow(index,value){
	if(value == '1'){
		$("#unitList").jqGrid('delRowData',index);
	}else{
		$("#unitList").jqGrid('restoreRow', index);
		var rowData=$("#unitList").getRowData(index);
		rowData.editing=false;
		rowData.action=null;
		$('#unitList').jqGrid('setRowData', index, rowData);
	}
}
//列表删除
function delRow(index,id){
	$.fool.confirm({title:'提示',msg:"确认删除该货品记录？",fn:function(data){
		if(data){
			$.ajax({
				type:'DELETE',
				url:'${ctx}/transportFeeUnit/deleteTransportFeeUnit?fid='+id,
				cache:false ,
				data:{},
				dataType:'json',
				success:function(data){
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'删除成功！'});
						$('#unitList').trigger("reloadGrid");
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
	}});
}
//列表保存
function saveRow(index,id){
	$('#unitList').find("#"+index).form("enableValidation");
    if($('#unitList').find("#"+index).form("validate")){
    	$('#unitList').saveRow(index);
    	var rowData=$('#unitList').jqGrid('getRowData',index);
		$.ajax({
			type:'post',
			url:'${ctx}/transportFeeUnit/saveTransportFeeUnit',
			cache:false ,
			data:rowData,
			dataType:'json',
			success:function(data){
				if(data.returnCode == '0'){
					$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
						/* $('#unitList').trigger("reloadGrid"); */
						$('#unitList').jqGrid('setRowData', index, {editing:false,action:null,fid:data.data.fid,updateTime:data.data.updateTime,isNew:""});
					    if('${param.dhxkey}'==1){
                            selectTab('${param.dhxname}','${param.dhxtab}')
                        }
					}});
				}else if(data.returnCode = '1'){
					$('#unitList').jqGrid('setRowData', index, {editing:false,action:null});
					editRow(index);
					$.fool.alert({msg:data.message});
				}
			},
			error:function(){
				$('#unitList').jqGrid('setRowData', index, {editing:false,action:null});
				editRow(index);
				$.fool.alert({msg:'服务器繁忙，请稍后再试'});
			}
		});
	}
}
//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}
</script>
</body>
</html>
