<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>计划模板</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/eventManagement.css" />
<link rel="stylesheet" href="${ctx}/resources/css/billModel.css" />
<style>
.dataBox .datagrid-header {
	background-color: #77b6e9 !important;
	border-color: #77b6e9;
}
.dataBox .datagrid-header td.datagrid-header-over {
	background-color: #77b6e9;
}
.dataBox .datagrid-header td{
	border-color: #77b6e9;
	color:#fff;
}
.dataBox .datagrid-body td{
	border-left:none;
	border-bottom: 1px dashed #DDDDDD !important;
}
.dataBox .datagrid-view2 .datagrid-row-selected [mark="1"] .datagrid-cell{
	padding-left:1px;
}
em{
	color:red;
}
.afterbtn{
	position:absolute;
	margin:0 0 0 200px;
	top:14px;
}
.beforebtn{
	margin:5px 0;
}
</style>
</head>
<body>
	<div class="titleCustom">
         <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>计划模板</h1>
         </div>
    </div>
    <div class="btn" style="margin-top:0px">
      	<a href="javascript:;" id="addPlan" class="btn-ora-add">新增</a> 
	      <!-- <a href="javascript:;" id="getIn" onclick="getIn()" class="btn-ora-import" >导入</a>
	      <a href="javascript:;" id="getOut" onclick="getOut()" class="btn-ora-export">导出</a>
	      <a href="javascript:;" id="print" onclick="printer()" class="btn-ora-printer">打印</a> -->
    </div>
    <div class="form_input">
      <form>
        <input class="easyui-textbox" id="search-code"/>
      </form>
      <a href="javascript:;" onclick="searchIt()" class="mysearch Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
    </div>
   <div id="importBox"></div> <!--导入 -->
   <div id="addBox"></div>
   <table id="planList"></table>
   <div id="pager"></div>
<script type="text/javascript">
var openAdder="${param.openAdder}";
var templateType="${param.templateType}";
var dataId="${param.dataId}";
var tempId="${param.tempId}";
var clearFlag="${param.clearFlag}";

var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
/**
 * manage
 */
$('#search-code').textbox({
	prompt:'编号或名称',
	width:160,
	height:30,
});
$('#addBox').window({
	top:10,
	left:0,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	width:$(window).width()-10,
	height:$(window).height()-60,
	closed:true,
	modal:true,
	onclose:function(){
		openAdder="";
		templateType="";
		dataId="";
		tempId="";
		clearFlag="";
	}
});
function windowOpen(url){
	$('#addBox').window({
		onOpen:function(){
			$('html').css('overflow','hidden');
		},
		onClose:function(){
			$('html').css('overflow','');
			if("undefined" != typeof plankd){
				$(document).unbind('keydown',plankd);
			}
			openAdder="";
			templateType="";
			dataId="";
			tempId="";
			clearFlag="";
		}
	});
	$('#addBox').window("open");
	var template=templateType == 1?'采购':templateType==2?'运输':templateType==3?'销售':'计划';
	$('#addBox').window("setTitle",template+'模板');
	$('#addBox').window("refresh",url);
}

$('#addPlan').click(function(){
	var url=getRootPath()+'/flow/planTemplate/edit?templateType='+templateType;
	windowOpen(url);
});

//查询
function searchIt(){
	var code = $('#search-code').textbox('getValue');
	var options = {"searchKey":code};
	$('#planList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
}

//主表数据
$('#planList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:getRootPath()+"/flow/planTemplate/query",
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#planList")[0].addJSONData(data.responseJSON);
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
	 loadcomplete:function(){
		warehouseAll();
	},
	colModel:[
        {name:"fid",label:"fid",hidden:true},
        {name:"taskLevelId",label:'taskLevelId',hidden:true},
        {name:"code",label:'计划编号',width:10,align:'center'},
        {name:"name",label:'计划名称',width:10,align:'center'},
        {name:"taskLevelName",label:'任务级别名称',width:10,align:'center'},
        {name:"securityLevelName",label:'保密级别名称',width:10,align:'center'},
        {name:"days",label:'预计完成天数',width:10,align:'center'},
        {name:"describe",label:'描述',width:10,align:'center'},
        {name:"creatorName",label:'创建人',width:10,align:'center'},
        {name:"status",label:'状态',width:10,align:'center',formatter:function(value,row,index){
              if(value == 0){
                  return "停用";
              }else if(value == 1){
                  return "启用";
              }
        }},
        {name:"action",label:'操作',width:10,align:'center',formatter:function(value,index,row){
              var e = '<a href="javascript:;" class="btn-edit" onclick="editById(\''+row.fid+'\')" title="修改"></a>';
              var v = '<a href="javascript:;" class="btn-detail" onclick="editById(\''+row.fid+'\')" title="查看"></a>';
              var d = '<a href="javascript:;" class="btn-del" onclick="delById(\''+row.fid+'\')" title="删除"></a>';
              var r = '<a href="javascript:;" class="btn-in" onclick="runById(\''+row.fid+'\')" title="启用"></a>';
              var u = '<a href="javascript:;" class="btn-cancel" onclick="unrunById(\''+row.fid+'\')" title="停用"></a>';
              if(row.status == 0){
                  return e+r+d;
              }else if(row.status == 1){
                  return v+u;
              }
        }},
    ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

//列表操作
function editById(id){
	var url=getRootPath()+'/flow/planTemplate/edit?id='+id+'&mark=1&templateType='+templateType;
	windowOpen(url);
}
function delById(id){
	$.fool.confirm({title:"删除提示",msg:"确认删除该项计划模板？",fn:function(bool){
		if(bool){
			$.post(getRootPath()+'/flow/planTemplate/delete?id='+id,{},function(data){
				dataDispose(data);
				if(data.returnCode == 0){
					$.fool.alert({time:1000,msg:"删除成功！",fn:function(){
						 $('#planList').trigger('reloadGrid');
					}});
				}else if(data.returnCode == 1){
					$.fool.alert({msg:data.message,fn:function(){
					}});
				}else{
					$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
					}});
				}
			});
		}else{
			return false;
		}
	}});
}
function unrunById(id){
	$.post(getRootPath()+'/flow/planTemplate/updateUnUse?id='+id,{},function(data){
		dataDispose(data);
		if(data.returnCode == 0){
			$.fool.alert({time:1000,msg:"停用成功！",fn:function(){
				 $('#planList').trigger('reloadGrid');
			}});
		}else if(data.returnCode == 1){
			$.fool.alert({msg:data.message,fn:function(){
			}});
		}else{
			$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
			}});
		}
	});
}
function runById(id){
	$.post(getRootPath()+'/flow/planTemplate/updateUse?id='+id,{},function(data){
		dataDispose(data);
		if(data.returnCode == 0){
			$.fool.alert({time:1000,msg:"启用成功！",fn:function(){
				 $('#planList').trigger('reloadGrid');
			}});
		}else if(data.returnCode == 1){
			$.fool.alert({msg:data.message,fn:function(){
			}});
		}else{
			$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
			}});
		}
	});
}
enterSearch('mysearch');

if(openAdder==1){
	if(tempId){
		editById(tempId);
	}else{
		$("#addPlan").click();
	}
}
</script>
</body>
</html>
