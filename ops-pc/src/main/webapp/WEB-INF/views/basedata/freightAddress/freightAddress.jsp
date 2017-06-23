<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>货运地址表</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
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
  margin-top: 5px;
}
#search-form{
  display: inline-block;
  width:92%;
}
#search-form .textbox{
  margin:5px 5px 0px 0px
}
.button_clear .btn-s{
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
.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{
	padding:0px !important;
}
.datagrid-view .datagrid-row-selected [mark="1"] .datagrid-cell{
	padding-left:0px;
}
.datagrid-view .datagrid-row-selected [mark="1"] .datagrid-cell-check{
	margin-left:0px;
}
.datagrid-view .datagrid-row-selected [mark="1"]{
	border-left:1px solid #ccc !important;
}
.datagrid-view .datagrid-row-selected [mark="1"] .datagrid-editable{
	padding-left:0px !important;
	margin:0;
}
#search-form .dhxDiv {
    margin: 5px 5px 0px 0px;
}
.disable{
	background-color:#ccc;
}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>货运地址表</h1>
       </div>             
    </div>
	<div style="margin:5px 0px 10px 0px">
	<div class="kk">
		<input type="button" id="add" class="btn-ora-add" style="display:inline;border: 0px;font-size: 16px;" value="新增" />
		<input name="searchKey" id="searchKey" class="textbox easyui-textbox" data-options="{prompt:'编号或名称',	width:160,height:30}"/><a href="javascript:;" class="btn-blue btn-s" id="searchBtn" style="margin-left: 5px;">查询</a>
		<div id="grabble">
			<input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/>
			<a href="javascript:;" class="button_a"><span class="button_span"></span></a>
		</div>
	</div>
	<div class="input_div">
		<form id="search-form">		 		 		 
		  <p>场地类型：<input id="search-ground" name="assetgroundId" class="textBox"/></p>
		  <p>状态：<input id="search-enable" name="enable" /></p>
		</form>
		<div class="button_clear">
		 <a id="search-btn" class="btn-blue btn-s">查询</a>
		 <a href="javascript:;" id="clear-btn" class="btn-blue btn-s">清空</a>
		<a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	    </div>
	</div>
	</div>
	<table id="addressList"></table>
	<!-- <div id="pager"></div> -->
	<div id="addBox"></div>
	<div id="pop-win"></div>
<script type="text/javascript"
	src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
var win = "";
var myIndex = 1; //记录最后的index
var _index = 0;
var editKey = true;
var skey = false;
var selectId = "";//标识保存后要选中的行ID
var groundData = getComboData("${ctx}/basedata/spaceType","tree");
//收货标识
var recipientData = [{value:0,text:"否"},{value:1,text:"是"}];
var statusData=[{value:"0",text:'停用'},{value:"1",text:'启用'}];
var swarehouse="";
var transLossData = "";
//加载控件数据
$.ajax({
	url:getRootPath()+"/basedata/query?num="+Math.random(),
	async:false,
    data:{param:"AuxiliaryAttr_Warehouse"},
    success:function(data){
    	swarehouse=formatTree(data.AuxiliaryAttr_Warehouse[0].children,"text","id");
    }
});
$.ajax({
	url:getRootPath()+"/basedata/findSubAuxiliaryAttrTree?code=023&num="+Math.random(),
	async:false,
    success:function(data){
    	transLossData=formatTree(data[0].children,"text","id");
    }
});

$('#addressList').height($(window).height()-150);


//全局查询
$('#searchBtn').click(function(){
    $('#clear-btn').click();
	var searchKey=$("#searchKey").textbox("getValue");
	var options={"searchKey":searchKey}
	$("#addressList").treegrid('reload',options);
    $('#add').removeAttr("disabled").removeClass("disable");

});


var chooserWindow_search="";
  //控件初始化
  $("#search-ground").fool('dhxCombo',{
		width:162,
		height:32,
		prompt:"场地类型",
		focusShow:true,
		editable:false,
		data:groundData,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		toolsBar:{
			name:"场地类型",
			refresh:true
		}
  });
  $("#search-enable").fool("dhxCombo",{
	  width:162,
	  height:32,
	  prompt:"状态",
	  focusShow:true,
	  data:statusData,
	  editable:false
  });
  $('#add').tooltip({
		position: 'top',
		content: '<span style="color:#fff"><strong>不选择节点时：</strong>新增最上级新行<br><strong>选择节点时：</strong>新增节点下的新行</span>',
		onShow: function(){
			$(this).tooltip('tip').css({backgroundColor: '#2CA7F6',borderColor: '#2CA7F6'});
		}
	});
  //科目初始数据列表初始化
  $("#addressList").treegrid({
		url: '${ctx}/freightAddress/findAddressTree',
		method:"get",
		fitColumns:true,
		idField:'fid',
		treeField:'code',
		singleSelect:true,
		sortName:"number",
		sortOrder:"asc",
		columns:[[ 
					{field:'action',title:'操作',align:'center',width:100,formatter:function(cellvalue, rowObject, index){
						if(!rowObject.editing){
							var d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+rowObject.fid+'\')"></a> ';
							var e='<a class="btn-edit" title="编辑" href="javascript:;" onclick="myedit(\''+rowObject.fid+'\')"></a> ';
							if(rowObject.fchecked!=1){
								return e+d;  
							}else{
								return "";
							}
						}else{
							var c = '<input type="button" style="border:0px;" class="btn-cancel" title="撤销" onclick="mycancel(\''+rowObject.fid+'\')" /> ';
							var s = '<input type="button" style="border:0px;" class="btn-save" title="保存" onclick="mysave(\''+rowObject.fid+'\')" /> ';
							return s+c;
						}
						
					}},
					{field : 'updateTime',title : '更新时间',align:'center',hidden:true,width:100}, 
					{field : 'newRow',title : '新行标识',align:'center',hidden:true,width:100}, 
					/* {name : '_fchecked',label : '勾对',align:'center',width:70,formatter:function(value,options,row){
						if(typeof row.fchecked !="undefined"){
							return row.fchecked==1?"已勾对":"未勾对";
						}else{
							return "";
						}
					}}, */
	                {field:'fid',title:'fid',hidden:true},
	                /* {name:'level',label:'level',hidden:true},
	                {name:'isLeaf',label:'isLeaf',hidden:true},
	                {name:'expanded',label:'expanded',hidden:true}, */
	                {field:'assetgroundId',title:'场地类型ID',align:'center',hidden:true,width:100,editor:{type:"text"}},
	                {field:'flag',title:'节点标识',align:'center',hidden:true,width:100,editor:{type:"text"}},
					{field:'parentId',title:'父ID',align:'center',hidden:true,width:100,editor:{type:"text"}},
					{field:"transportLossId",title:"运输损耗Id",align:"center",hidden:true,width:80,editor:{type:"text"}},
					{field:'assetwarehouseId',title:'仓库ID',align:'center',hidden:true,width:100,editor:{type:"text"}}, 
					{field:'code',title:'编号',align:'center',width:100,editor:{type:"textbox",options:{validType:["isBank","maxLength[50]"],missingMessage:"该项不能为空",height:"80%",required:true,novalidate:true}}},
					{field:'name',title:'名称',align:'center',width:150,editor:{type:"textbox",options:{validType:["isBank","maxLength[100]"],missingMessage:"该项不能为空",height:"80%",required:true,novalidate:true}}},
	    	  		{field:'describe',title:'描述',align:'center',width:100,editor:{type:"textbox",options:{height:"80%",validType:"maxLength[200]"}}/* formatter:function(cellvalue, options, rowObject){
	    	  			return '<a title="编辑" href="javascript:;" onclick="editById(\''+rowObject.fid+'\')">'+cellvalue+'</a> ';
	                } */},
	                //{field:'fullParentId',title:'父地址',align:'center',width:100,editor:{type:"textbox",options:{height:"80%"}}},
	                {field:'assetgroundName',title:'场地类型',align:'center',width:100,editor:{type:"text"}
	                	/* var index = $(cl).attr("rowid");
	                	$(cl).fool("dhxCombo",{
							width:"100%",
							height:"80%",
							focusShow:true,
							editable:false,
							data:groundData,
							setTemplate:{
								input:"#name#",
								option:"#name#"
							},
							required:true,
							clearOpt:false,
							novalidate:true,
							onChange:function(value,text){
								getEditor("groundId",index).val(value);
							}
						}); */
					},
					{field:"transportLoss",title:"运输损耗",align:"center",width:80,editor:{type:"text"}},
	                {field:'assetwarehouseName',title:'仓库',align:'center',width:80,editor:{type:"text"}
	                	/* var index = $(cl).attr("rowid");
	                	$(cl).fool("dhxCombo",{
							width:"100%",
							height:"80%",
							focusShow:true,
							editable:false,
							data:swarehouse,
							setTemplate:{
								input:"#name#",
								option:"#name#"
							},
							onChange:function(value,text){
								getEditor("warehouseId",index).val(value);
							}
						}); */
					},
	                {field:'recipientSign',title:'收货标识',align:'center',width:60,editor:{type:"text"}
						/* $(cl).fool("dhxCombo",{
							width:"100%",
							height:"80%",
							focusShow:true,
							editable:false,
							clearOpt:false,
							data:recipientData,
							required:true,
							novalidate:true
						}); */
					,formatter:function(value){
						if(value == 1){
							return "是";
						}else{
							return "否";
						}
					}},
					{field:'transfer',title:'中转标识',align:'center',width:60,editor:{type:"text"},formatter:function(value){
						if(value == 1){
							return "是";
						}else{
							return "否";
						}
					}},
	                {field:'_flag',title:'节点标识',align:'center',hidden:true,width:100,formatter:function(value,row){
	                	if(typeof row.flag !="undefined"){
	                		return row.flag == 0?"父节点":"子节点";
	                	}else{
	                		return "";
	                	}
	                }},
	                {field:'enable',title:'状态',align:'center',width:100,editor:{type:"text"}
						/* $(cl).fool("dhxCombo",{
							width:"100%",
							height:"80%",
							required:true,
							clearOpt:false,
							novalidate:true,
							data:statusData,
							focusShow:true,
							editable:false
						}); */
					,formatter:function(value){
						if(value == 1){
							return "启用";
						}else{
							return "未用";
						}
					}}
	              ]],
 	    onLoadSuccess:function(){
	    	$('#addressList').treegrid("expandAll");
	    	if(selectId != ""){
	    		$('#addressList').treegrid("select",selectId);
	    	}
// 	    	myIndex = 1;
	    },
	              onBeforeEdit:function(row){
						row.editing = true;
						updateActions(row);
					},
					onAfterEdit:function(row){
						row.editing = false;
						updateActions(row);
					},
					onCancelEdit:function(row){
						row.editing = false;
						updateActions(row);
					},
					onBeforeSelect:function(row){
						if(skey){
							skey = false;
							return false;
						}
					},
					onClickCell:function(field,row){
						if(field != "action"){
							if($(this).parent().find('[node-id='+row.fid+']').attr('class').search(/datagrid-row-selected/)!=-1){
								$('#addressList').treegrid('unselect',row.fid);
								skey = true;
							}
						}
					},
	}); 
  
  function updateActions(row){
		$('#addressList').treegrid('update',{
			id: row.fid,
			row:{}
		});
	}
    /* console.log(mdata);
	$("#addressList")[0].addJSONData(mdata);
	myIndex = 3; */
  //清空按钮
  $("#clear-btn").click(function(){
	 $("#search-form").form('clear'); 
	 var inputs=$("#search-form").find(".dhxDiv");
	 for(var i=0;i<inputs.length;i++){
		 inputs[i].comboObj.setComboText(null);
	 } 
  });
  //筛选按钮
  $("#search-btn").click(function(){
	  var options=$('#search-form').serializeJson();
	  $('#searchKey').textbox('setValue',"");
	  $("#addressList").treegrid('reload',options); 
	  $('#add').removeAttr("disabled").removeClass("disable");
  });
  
/*   function selectParent(data){
		var _d = data;
		if(typeof data[0]!='undefined'){
			_d = data[0];
		}
		getEditor("fullParentId",_index).textbox("setValue",_d.path);
  } */
  //新增按钮点击事件
  $('#add').click(function(){
	  $('#addressList').treegrid("expandAll");
      $('#add').tooltip("hide");
	  var selectId = $("#addressList").treegrid('getSelected');
		var index = myIndex;
		if(selectId != null){
			$("#addressList").treegrid('expand',selectId.fid);
			var row = {parent:selectId.fid,data:[{
				fid:index,transportLoss:selectId.transportLoss,
				transportLossId:selectId.transportLossId,
				assetwarehouseId:selectId.assetwarehouseId,
				assetgroundId:selectId.assetgroundId,
				flag:1,parentId:selectId.fid,newRow:1}]};
		}else{
			var row = {data:[{fid:index,newRow:1,flag:1}]};
		}
		$("#addressList").treegrid("append",row);
		myedit(index);
		myIndex++;
  });
  function getEditor(field,id){
		var target = $($('#addressList').treegrid('getEditor',{id:id,field:field}).target);
		return target;
  }
  function getText(field,id){
	var target = $('#addressList').treegrid('getPanel').find('[node-id='+id+'] [field='+field+']').children();
	return target;
  }
  function hideBtn(bool){
		var panel = $('#addressList').treegrid('getPanel');
		if(bool){
			editKey = false;
			panel.find('.btn-del').hide();
			panel.find('.btn-edit').hide();
			$('#add').attr("disabled","disabled").addClass("disable");
		}else{
			editKey = true;
			panel.find('.btn-del').show();
			panel.find('.btn-edit').show();
			$('#add').removeAttr("disabled").removeClass("disable");
		}
	}
  function mycancel(id){
		var bool = getText('newRow',id).text();
		if(bool == 1){
			$("#addressList").treegrid("remove",id);
		}else{
			$("#addressList").treegrid("cancelEdit",id);
			//$("#addressList").treegrid("update",{id:id,row:{editing:false,action:null}});
		}
		hideBtn(false);
	}
	function mysave(id){
		var tr = $("#addressList").treegrid("getPanel").find(".datagrid-view2 .datagrid-btable tr[node-id="+id+"]");
		tr.form("enableValidation");
		if(!tr.form("validate")){
			return false;
		}
		$("#addressList").treegrid("select",id);
		var row = $("#addressList").treegrid("getSelected");
		var newRow = getText("newRow",id).text();
		var code = getEditor("code",id).textbox("getValue");
		var name = getEditor("name",id).textbox("getValue");
		var parentId = getEditor("parentId",id).val();
		var describe = getEditor("describe",id).textbox("getValue");
		var assetgroundId = getEditor("assetgroundId",id).val();
		var assetwarehouseId = getEditor("assetwarehouseId",id).val();
		var recipientSign = getEditor("recipientSign",id).next()[0].comboObj.getSelectedValue();
		//var assetwarehouseName = getEditor("assetwarehouseName",id).next()[0].comboObj.getComboText();
		//var assetgroundName = getEditor("assetgroundName",id).next()[0].comboObj.getComboText();
		//var transportLossName = getEditor("transportLossName",id).next()[0].comboObj.getComboText();
		var transportLossId = getEditor("transportLossId",id).val();
		var flag = getEditor("flag",id).val();
		var transfer = getEditor("transfer",id).next()[0].comboObj.getSelectedValue();
		var fid = newRow == 1?"":row.fid;
		var enable = getEditor("enable",id).next()[0].comboObj.getSelectedValue();
		var updateTime = getText("updateTime",id).text();
		$('#save').attr("disabled","disabled");
		
		var mydata = {transfer:transfer,transportLossId:transportLossId,parentId:parentId,enable:enable,updateTime:updateTime,fid:fid,code:code,name:name,describe:describe,assetgroundId:assetgroundId,assetwarehouseId:assetwarehouseId,recipientSign:recipientSign};
		$.ajax({
			url:'${ctx}/freightAddress/save',
			type:"PUT",
			data:mydata,
			success:function(data){
				if(data.returnCode =='0'){
							$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
								//$(document).unbind('keydown',cashkd);
				    			$('#save').removeAttr("disabled");
				    			$("#addressList").treegrid("endEdit",id);
				    			/* $("#addressList").treegrid("update",{id:id,row:{
				    				newRow:0,
				    				editing:false,
				    				action:null,
				    				fid:data.data.fid,
				    				updateTime:data.data.updateTime,
				    				recipientSign:recipientSign,
				    				enable:enable,
				    				assetwarehouseName:assetwarehouseName,
				    				assetgroundName:assetgroundName
				    			}}); */
				    			$("#addressList").treegrid("reload");
				    			selectId = data.data.fid;
				    			hideBtn(false);
							}});
				}else{
		    		$.fool.alert({msg:data.message,fn:function(){
		    			$('#save').removeAttr("disabled");
		    		}});
		    	}
			},
			error:function(){
				$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
	    			$('#save').removeAttr("disabled");
	    		}});
			}
		});
	}
	function myedit(id){
		$("#addressList").treegrid("select",id);
		$("#addressList").treegrid("select",id);
		var row = $("#addressList").treegrid("getSelected");
		$("#addressList").treegrid("beginEdit",id);
		hideBtn(true);
		var newRow = getText("newRow",id).text();
		getEditor("code",id).textbox("textbox").blur(function(){
		    var value = $(this).val();
		    $(this).parent().prev().textbox("setValue",$.trim(value));
		});
		getEditor("name",id).textbox("textbox").blur(function(){
		    var value = $(this).val();
		    $(this).parent().prev().textbox("setValue",$.trim(value));
		});
		getEditor("transportLoss",id).attr("id","transportLoss-"+id);
		getEditor("transportLoss",id).fool("dhxCombo",{
			width:"100%",
			height:"80%",
			data:transLossData,
			focusShow:true,
			editable:false,
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
			toolsBar:{
				name:"损耗地址",
				addUrl:"/basedata/listAuxiliaryAttr",
				refresh:true
			},
			required:true,
			novalidate:true,
			onChange:function(value,text){
				getEditor("transportLossId",id).val(value);
			}
		});
		getEditor("assetgroundName",id).attr("id","assetgroundName-"+id);
		getEditor("assetgroundName",id).fool("dhxCombo",{
			width:"100%",
			height:"80%",
			focusShow:true,
			editable:false,
			data:groundData,
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
			toolsBar:{
				name:"场地类型",
				addUrl:"/basedata/listAuxiliaryAttr",
				refresh:true
			},
			required:true,
			clearOpt:false,
			novalidate:true,
			onChange:function(value,text){
				getEditor("assetgroundId",id).val(value);
			}
		});
		getEditor("assetwarehouseName",id).attr("id","assetwarehouseName-"+id);
		getEditor("assetwarehouseName",id).fool("dhxCombo",{
			width:"100%",
			height:"80%",
			focusShow:true,
			editable:false,
			required:true,
			noValidate:true,
			data:swarehouse,
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
			toolsBar:{
				name:"仓库",
				addUrl:"/storehouses/manage",
				refresh:true
			},
			onChange:function(value,text){
				getEditor("assetwarehouseId",id).val(value);
			}
		});
		getEditor("recipientSign",id).attr("id","recipientSign-"+id);
		getEditor("recipientSign",id).fool("dhxCombo",{
			width:"100%",
			height:"80%",
			focusShow:true,
			editable:false,
			clearOpt:false,
			data:recipientData,
			required:true,
			novalidate:true,
			value:0
		});
		getEditor("enable",id).attr("id","enable-"+id);
		getEditor("enable",id).fool("dhxCombo",{
			width:"100%",
			height:"80%",
			required:true,
			clearOpt:false,
			novalidate:true,
			data:statusData,
			focusShow:true,
			editable:false,
			value:1
		});
		getEditor("transfer",id).attr("id","transfer-"+id);
		getEditor("transfer",id).fool("dhxCombo",{
			width:"100%",
			height:"80%",
			focusShow:true,
			editable:false,
			clearOpt:false,
			data:recipientData,
			required:true,
			novalidate:true,
			value:0
		});
		getEditor("transportLoss",id).next()[0].comboObj.setComboValue(row.transportLossId);
		getEditor("assetwarehouseName",id).next()[0].comboObj.setComboValue(row.assetwarehouseId);
		getEditor("assetgroundName",id).next()[0].comboObj.setComboValue(row.assetgroundId);
		if(newRow!=1){
			getEditor("recipientSign",id).next()[0].comboObj.setComboValue(row.recipientSign);
			getEditor("enable",id).next()[0].comboObj.setComboValue(row.enable);
			getEditor("transfer",id).next()[0].comboObj.setComboValue(row.transfer);
		}
		/* if(row.flag == 0){
			getEditor("recipientSign",id).next()[0].comboObj.disable();
		} */
	}
  
  //删除按钮点击事件
  function removeById(id){ 
	  var length = $('#addressList').treegrid('getChildren',id).length;
	  var msg = '确定要删除此数据吗？'
	  if(length != 0){
		  msg = '此节点下有子节点，删除该节点会一并删除其子节点，确定删除此数据？'
	  }
  	 $.fool.confirm({title:'删除提示',msg:msg,fn:function(r){
  		 if(r){
  			 $.ajax({
  					type : 'delete',
  					url : '${ctx}/api/freightAddress/delete?fid='+id,
  					dataType : 'json',
  					success : function(data) {
  						if(data.returnCode == '0'){
  							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
  								$("#addressList").treegrid("remove",id);
  								selectId = "";
  							}});
  						}else{
  							$.fool.alert({msg:data.message,fn:function(){
//   								$("#addressList").treegrid("reload");
  							}});
  						}
  		    		},
  		    		error:function(){
  		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
  		    		}
  				});
  		 }
  	 }});
  }
  $(function(){
	  $("#searchKey").textbox("textbox").bind("keydown",function(e){
			if(e.keyCode == 13){
				$('#searchBtn').click();
			}
		});
  })
 
</script>
</body>
</html>
