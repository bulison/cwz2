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
  .btnAdd{
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
        display: inline-block;
  }
  .btnAdd:hover{
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
        display: inline-block;
  }
</style>
</head>
<body>
    <div class="titleCustom">
        <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>场地费报价模板</h1>
        </div>             
    </div>
    <div id="bodyBox">
    <div class="groundBox">
        <div class="btnBox">
            <a id="update" class="btn updateBtn" href="javascript:;" title="修改报价模板">修改</a>
            <!-- <a id="delete" class="btn deleteBtn" href="javascript:;" title="删除报价模板">删除</a> -->
            <!-- <input type="text" id="searchBox" class="searchbox"/> -->
            <input type="text" id="searchBox" class="searchbox"/>
        </div>
        <table id="groundTree"></table>
    </div>
    <div class="detailBox"> 
        <div id="groundMsg"></div>
    	<table id="content"></table>
    	<div id="pager"></div>
    </div> 
    </div>
        
<script type="text/javascript" src="${ctx}/resources/js/easyui-tree-extend.js?v=${js_v}"></script>
<script type="text/javascript">
var templateId="";
$("#searchBox").searchbox({
	 prompt:'输入场地类型名称查询',
	 height:32,
	 width:"98%",
	 iconWidth:26,
	 searcher:function(value,name){
		 $('#groundTree').jqGrid('setGridParam',{url:"${ctx}/api/groundTemplate/list",postData:{searchKey:value}}).trigger("reloadGrid");
	 }
});
$("#groundTree").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/api/groundTemplate/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#groundTree")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	scrollOffset:0,
	autowidth:true,
	height:545, 
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	colModel:[
	          {name : 'id',label : 'id',hidden:true},
	          {name : 'groundId',label : 'groundId',hidden:true},
	          {name : 'describe',label : 'describe',hidden:true},
              {name : 'groundName',label : '场地类型',align:'center',width:"100px"},
              ],
    onSelectRow: function(rowid,status){
    	var rowdata=$("#groundTree").getRowData(rowid);
    	templateId=rowdata.id;
    	$("#groundMsg").html("<p>场地类型："+rowdata.groundName+"</p><p>描述："+rowdata.describe+"</p>");
    	$('#content').jqGrid('setGridParam',{url:"${ctx}/api/groundTemplateDetail/list",postData:{templateId:rowdata.id}}).trigger("reloadGrid"); 
    },
    gridComplete:function(){
    	var rowdata=$("#groundTree").find("tr[role='row']");
    	if(rowdata.length>1){
    		$("#groundTree").setSelection($(rowdata[1]).attr("id"),true);
    	}
    }
});
/* $('#groundTree').tree({
	 url:'${ctx}/api/groundTemplate/list',
	 onClick: function(node){
		 	var parentId = node.id; 
	    	if(!isFirst(node)){
	    		$('#content').jqGrid('setGridParam',{url:"",postData:{}}).trigger("reloadGrid"); 
	    	}
	 },
	 onLoadSuccess:function(node, data){
		 var noder = $("#goodsTree").tree('getRoot');
		 var nodes = $("#goodsTree").tree('getChildren',noder);
		 $("#goodsTree").tree('select', noder.target);
		 $(noder.target).click();
	 }
}); */
$("#content").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/api/groundTemplateDetail/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#content")[0].addJSONData(data.responseJSON);
	        		/* $("#content").addRowData("add",{"action":"new"},"last"); */
	        	}
	        }
		});
	},
	footerrow: true,
	pager:"#pager",
	autowidth:true,
	height:500, 
	rowNum:10,
	rowList:[10,20,30],
	postData:{templateId:""},
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[ 
	            {name:"action",label:"操作",align:'center',formatter:function(cellvalue, options, rowObject){
	            	var d='',c='',s='';
			    	d = "<a href='javascript:;' onClick='delRow(\""+options.rowId+"\",\""+rowObject.id+"\")' class='btn-del' title='删除'></a>";
			    	c = "<a href='javascript:;' onClick='cancelRow(\""+options.rowId+"\",\""+rowObject.isNew+"\")' class='btn-back' title='撤销'></a>";
			    	s = "<a href='javascript:;' onClick='saveRow(\""+options.rowId+"\",\""+rowObject.id+"\")' class='btn-save' title='保存'></a>";
			    	if(cellvalue == 'new'){
			    		return "<a href='javascript:;' class='btnAdd' onclick='addrow()' title=' 新增'>新增</a>";
			    	}else{
			    		if(rowObject.editing){
			    			return s+"  "+c;
			    		}else{
			    			//屏蔽编辑按钮
			    			return d;
			    		}
			    	}
	            }},
	            {name : 'isNew',label : 'isNew',hidden:true},
	            {name : 'editing',label : 'editing',hidden:true}, 
                {name : 'updateTime',label : 'updateTime',hidden:true}, 
                {name : 'id',label : 'id',hidden:true}, 
                {name : 'transportCostId',label : 'transportCostId',hidden:true}, 
                {name : 'transportUnitId',label : 'transportUnitId',hidden:true}, 
                {name : 'transportCostName',label : '运输费用',align:'center',width:"100px",editable:true,edittype:"text"},
                {name : 'transportUnitName',label : '运输单位',align:'center',width:"100px",editable:true,edittype:"text"},
                {name : 'costSign',label : '成本标识',align:'center',hidden:true,editable:true,edittype:"text",formatter:function(value){
                	var str="";
                	if(typeof value!="undefined"){
                		if(value == "1"){
                    		str='是';
                    	}else if(value == "0"){
                    		str='否';
                    	}else{
                    		str=value;
                    	}
                	}else{
                		str="";
                	}
                	return str;
                }}, 
                {name : 'describe',label : '描述',align:'center',width:"100px",editable:true,edittype:"text"}, 
              ],
    onSelectRow:function(rowid){
    	editRow(rowid);
    },
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});  
$("#content").jqGrid("footerData","set",{"action":"new"});

//修改
$('#update').click(function(){
	mywin=$.fool.window({
		title:'修改场地类型',
		width:400,
		height:180,
		href:"${ctx}/groundTemplate/edit?id="+templateId,
	});
});

//删除
$('#delete').click(function(){
	$.fool.confirm({title:'提示',msg:"确定删除该场地类型及其费用模板？",fn:function(data){
		if(data){
			$.ajax({
				type:'DELETE',
				url:'${ctx}/api/groundTemplate/delete?id='+templateId,
				cache:false ,
				data:{},
				dataType:'json',
				success:function(data){
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'删除成功！'});
						$("#groundTree").trigger("reloadGrid");
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
});

//列表新增
function addrow(){
	var length = $("#content").jqGrid('getRowData').length;
	$("#content").jqGrid('addRowData',length-1,{costSign:0,accountFlag:1,isNew:1},"last");
	editRow(length-1);
}
//列表编辑 参数改为index
function editRow(rowid){
	var rowData=$("#content").jqGrid('getRowData',rowid);
	if(rowData.editing=="true"||rowData.action=='<a href="javascript:;" class="btnAdd" onclick="addrow()" title="新增货品"></a>'){
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#content').jqGrid('setRowData', rowid, rowData);
	$("#content").jqGrid('editRow',rowid);
	getEditor("content",rowid,"transportCostName").fool("dhxCombo",{
		required:true,
		height:"24px",
		width:"90px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
        toolsBar:{
            name:'运输费用',
            addUrl:'/basedata/listAuxiliaryAttr',
            refresh:true,
        },
		editable:false,
		value:rowData.transportCostId,
		focusShow:true,
		data:getComboData("${ctx}/basedata/findSubAuxiliaryAttrTree?code=018","tree"),
	});
	getEditor("content",rowid,"transportUnitName").fool("dhxCombo",{
		required:true,
		height:"24px",
		width:"90px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
        toolsBar:{
            name:'运输费计价单位',
            addUrl:'/transportFeeUnit/manage',
            refresh:true,
        },
		editable:false,
		value:rowData.transportUnitId,
		focusShow:true,
		data:getComboData("${ctx}/basedata/findSubAuxiliaryAttrTree?code=021","tree"),
	});
	getEditor("content",rowid,"costSign").fool("dhxCombo",{
		required:true,
		height:"24px",
		width:"90px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		editable:false,
		focusShow:true,
		data:[
			  {
		    	  value: '0',
			      text: '否',
		      },{
		    	  value: '1',
			      text: '是'
		      }
		     ],
	});
	getEditor("content",rowid,"describe").validatebox({
		validType:"maxLength[50]"
	});
	if(rowData.costSign=='是'){
		(getEditor("content",rowid,"costSign").next())[0].comboObj.setComboValue('1');
	}else if(rowData.costSign=='否'){
		(getEditor("content",rowid,"costSign").next())[0].comboObj.setComboValue('0');
	}
}
//列表撤销
function cancelRow(index,value){
	var rowData=$('#content').jqGrid('getRowData',index);
	if(value == '1'){
		$("#content").jqGrid('delRowData',index);
	}else{
		$("#content").jqGrid('restoreRow', index);
		$('#content').jqGrid('setRowData', index, {editing:false,action:null,id:rowData.id});
	}
}
//列表删除
function delRow(index,id){
	$.fool.confirm({title:'提示',msg:"确认删除该货品记录？",fn:function(data){
		if(data){
			$.ajax({
				type:'DELETE',
				url:'${ctx}/api/groundTemplateDetail/delete?id='+id,
				cache:false ,
				data:{},
				dataType:'json',
				success:function(data){
					if(data.returnCode == '0'){
						$.fool.alert({time:1000,msg:'删除成功！'});
						$("#content").jqGrid('delRowData',index);
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
	var rowData=$('#content').jqGrid('getRowData',index);
	$('#content').find("#"+index).form("enableValidation");
	if($('#content').find("#"+index).form("validate")){
		var transportCostId=(getEditor("content",index,"transportCostName").next())[0].comboObj.getSelectedValue();
		var transportUnitId=(getEditor("content",index,"transportUnitName").next())[0].comboObj.getSelectedValue();
		var transportCostName=(getEditor("content",index,"transportCostName").next())[0].comboObj.getComboText();
		var transportUnitName=(getEditor("content",index,"transportUnitName").next())[0].comboObj.getComboText();
		var costSign=(getEditor("content",index,"costSign").next())[0].comboObj.getSelectedValue();
		var describe=getEditor("content",index,"describe").val();
		$.ajax({
			type:'put',
			url:'${ctx}/api/groundTemplateDetail/save',
			cache:false ,
			data:{updateTime:rowData.updateTime,templateId:templateId,id:id,transportCostId:transportCostId,transportUnitId:transportUnitId,costSign:0,describe:describe},
			dataType:'json',
			success:function(data){
				if(data.returnCode == '0'){
					$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
						/* $('#content').trigger("reloadGrid"); */
					    $("#content").jqGrid('saveRow',index);
						$('#content').jqGrid('setRowData', index, {
							editing:false,
							action:null,
							isNew:false,
							updateTime:data.data.updateTime,
							id:data.data.id,
							transportCostName:transportCostName,
							transportUnitName:transportUnitName,
							transportCostId:transportCostId,
							transportUnitId:transportUnitId,
							costSign:costSign,
							describe:describe
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
//是否为第一层节点
function isFirst(node){
	return (typeof node.attributes != 'undefined'&&node.attributes.isFirst != undefined);
}
//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}
</script>
</body>
</html>
