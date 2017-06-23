<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收益率方案模板</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/eventManagement.css" />
<link rel="stylesheet" href="${ctx}/resources/css/billModel.css" />
<style>
.dataBox .ui-state-default {
	background-color: #77b6e9 !important;
	border-color: #77b6e9 !important;
}
.dataBox .ui-state-default th.ui-state-hover {
	background-color: #77b6e9 !important;
}
.dataBox .ui-state-default th{
	border-color: #77b6e9;
	color:#fff;
}
/* .dataBox #detailsList .jqgrow td{
	/* border-left:none; */
	border-bottom: 1px dashed #DDDDDD !important;
} */
</style>
</head>
<body>
	<div class="titleCustom">
         <div class="squareIcon">
            <span class='Icon'></span>
            <div class="trian"></div>
            <h1>收益率方案</h1>
         </div>             
    </div>

    <div class="btn" style="margin-top:0px">
      <a href="javascript:;" id="addProg" class="btn-ora-add">新增</a> 
    </div>
    <div class="form_input">
      <form>
        <input class="easyui-textbox" id="search-code" data-options="{prompt:'名称',width:160,height:30}"/>
      </form>
      <a href="javascript:;" onclick="searchIt()" class="mysearch Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
    </div>
   <div id="addBox"></div>
   <table id="programList"></table>
   <div id="pager"></div>
<%-- <script type="text/javascript"  src="${ctx}/resources/js/flow/planTemplateManage.js?v=${js_v}" ></script> --%>
</body>
<script>
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
});

function windowOpen(url){
	$('#addBox').window({
		onOpen:function(){
			$('html').css('overflow','hidden');
		},
		onClose:function(){
			if("undefined" != typeof plankd){
				$(document).unbind('keydown',programkd);
			}
		}
	});
	$('#addBox').window("open");
	$('#addBox').window("setTitle","新增收益率方案");
	$('#addBox').window("refresh",url);
}

$('#addProg').click(function(){
	var url=getRootPath()+'/rate/program/edit';
	windowOpen(url);
});

//列表查询
function searchIt(){
	var searchKey = $('#search-code').textbox('getValue');
	$('#programList').jqGrid('setGridParam',{postData:{searchKey:searchKey}}).trigger("reloadGrid");
}

$(document).keydown(function (e) {
    if(e.keyCode==13)
        searchIt();
})
//主表数据
$('#programList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:getRootPath()+"/rate/program/query",
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#programList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	forceFit:true,
	pager:'#pager',
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	autowidth:true,//自动填满宽度
	height:$(window).outerHeight()-200+"px",
	colModel:[
            {name:"fid",label:"fid",align:"center",hidden:true},
            {name:"creatorId",label:'creatorId',align:"center",hidden:true},

            {name:"name",label:'方案名称',align:"center",width:10,formatter:function (val,opt,row) {
                return '<a href="javascript:;" onclick="editById(\''+row.fid+'\')">'+val+'</a>';
            }},
            {name:"profit",label:'利润',align:"center",width:10,formatter:function (value,option,row) {
                return parseFloat(value).toFixed(2);
            }},
            {name:"profitRate",label:'利润率(%)',align:"center",width:10,formatter:function (value,option,row) {
                return parseFloat(value).toFixed(2);
            }},
            {name:"cycleProfitRate",label:'周期收益率(%)',align:"center",width:10,formatter:function (value,option,row) {
                return parseFloat(value).toFixed(2);
            }},
            {name:"creatorName",label:'创建人',align:"center",width:10},
            {name:"action",label:'操作',align:"center",width:10,formatter:function(value,options,row){
//              var e = '<a href="javascript:;" class="btn-edit" onclick="editById(\''+row.fid+'\')" title="修改"></a>';
              var d = '<a href="javascript:;" class="btn-del" onclick="delById(\''+row.fid+'\')" title="删除"></a>';
              return d;
            }},
        ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function editById(id){
	var url=getRootPath()+'/rate/program/edit?mark=1&id='+id;
	windowOpen(url);
}
function delById(id){
	$.fool.confirm({title:"删除提示",msg:"确认删除该项收益率方案？",fn:function(bool){
		if(bool){
			$.post(getRootPath()+'/rate/program/delete?id='+id,{},function(data){
				if(data.returnCode == 0){
					$.fool.alert({time:1000,msg:"删除成功！",fn:function(){
						$('#programList').trigger('reloadGrid');
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
enterSearch('mysearch');
</script>
</html>
