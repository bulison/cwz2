<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>费用申请单</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
#bill p.hideOut{
  display: none ;
}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>费用申请单</h1>
       </div>             
    </div>
	<div id="addBox"></div> 
	<div style="margin:10px 0px 10px 0px">  
	  <form id="searchForm">
	    <a href="javascript:;" id="add" class="btn-ora-add" >新增</a> 
	    <input id="search_startDate" name="startDate"/>
	    <input id="search_endDate" name="endDate"/>
	    <input id="search_deptId" name="deptId"/>
	    <input id="search_memberId" name="memberId"/>
	    <a href="javascript:;" id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	    <a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	  </form>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript">
var smemData="",sdeptData="";
$.ajax({
	url:"${ctx}/basedata/query?num="+Math.random(),
	async:false,
	data:{param:"Member,Organization"},
	success:function(data){
	    smemData=formatData(data.Member,"fid");
	    sdeptData=formatTree(data.Organization[0].children,"text","id");
    }
	});
$("#search_startDate").fool('datebox',{
	prompt:'开始日期',
	width:160,
	height:32,
	editable : false
});

$("#search_endDate").fool('datebox',{
	prompt:'结束日期',
	width:160,
	height:32,
	editable : false
});
var sdeptCombo = $('#search_deptId').fool("dhxCombo",{
	setTemplate:{
		input:"#orgName#",
		option:"#orgName#"
	},
	toolsBar:{
		refresh:true
	},
	editable:false,
	data:sdeptData,
	width:160,
	height:34,
	focusShow:true,
	prompt:'部门'
});
var smemCombo = $('#search_memberId').fool('dhxComboGrid',{
	prompt:'申请人',
	required:true,
	novalidate:true,
	toolsBar:{
		refresh:true
	},
	width:160,
	height:34,
	data:smemData,
	focusShow:true,
    onlySelect:true,
	filterUrl:getRootPath()+'/member/vagueSearch',
	setTemplate:{
		input:"#username#",
		columns:[
					{option:'#userCode#',header:'编号',width:100},
					{option:'#jobNumber#',header:'工号',width:100},
					{option:'#username#',header:'名称',width:100},
					{option:'#phoneOne#',header:'电话',width:100},
					{option:'#deptName#',header:'部门',width:100},
					{option:'#position#',header:'职位',width:100},
				],
  	}
});
/* $('#search_memberId').combogrid('textbox').focus(function(){
	$('#search_memberId').combogrid('showPanel');
}); */

$("#search-btn").click(function(){
	var options = $("#searchForm").serializeJson();
	$('#billList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
});
$("#clear-btn").click(function(){
	$("#searchForm").form("clear");
	sdeptCombo.setComboText("");
	smemCombo.setComboText("");
});

var options = [
    		    {id:'0',name:'未审核'},
    		    {id:'1',name:'已审核'},
    		    {id:'2',name:'已作废'}
    		];
$("#billList").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:"${ctx}/expenApplyBill/query",
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#billList")[0].addJSONData(data.responseJSON);
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
	  		{name:'fid',label:'fid',hidden:true,width:90},
	  		{name:'code',label:'单号',align:"center",width:90,sortable:true,formatter:function(value,options,row){
	  			return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\',\''+row.recordStatus+'\')">'+value+'</a>';
	  		}},
	  		{name:'voucherCode',label:'原始单号',align:"center",width:90,sortable:true},
	  		{name:'date',label:'申请日期',align:"center",width:90,sortable:true},
	  		{name:'memberName',label:'申请人',align:"center",width:90},
	  		{name:'amount',label:'金额',align:"center",width:90},
	  		{name:'describe',label:'备注',align:"center",width:90},
	  		{name:'recordStatus',label:'状态',align:"center",width:50,sortable:true,formatter:function(value){
				for(var i=0; i<options.length; i++){
					if (options[i].id == value) return options[i].name;
				}
				return value;
			}},
	  		{name:'action',label:'操作',align:"center",width:50,formatter:function(value,options,row){
	  			if(row.recordStatus==0){
	  				var statusStr = '';
		  			 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
		  			 statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+row.fid+'\')"></a> ';
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
		  			 statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
		  			 return statusStr ; 
	  			}else if(row.recordStatus==1){
	  				var statusStr = '';
	  				statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
		  			statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
		  			return statusStr ; 
	  			}else if(row.recordStatus==2){
	  				return '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
	  			}
	  			
	  		}}
	      ],
	      onLoadSuccess:function(){
	    	  warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
/* $('#addBox').window({
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
	onOpen:function(){
		$(this).parent().prev().css("display","none");
	}
}); */

$('#add').click(function(){//点击新增
	var url="${ctx}/expenApplyBill/edit?mark=1"; 
	warehouseWin("新增费用申请单",url)
});

//编辑 
function editById(fid){
		var url="${ctx}/expenApplyBill/edit?id="+fid;
		warehouseWin("编辑费用申请单",url)
}

//复制
function copyById(fid){
	var url = "${ctx}/expenApplyBill/edit?mark=1&id="+fid;
	warehouseWin("复制费用申请单",url);
}

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/expenApplyBill/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

function verifyById(fid){
	$.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/expenApplyBill/passAudit',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.message,fn:function(){
					    		}});
				    		}
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

function cancelById(fid) {
	 $.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/expenApplyBill/cancle',
					data : {fid :fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.message,fn:function(){
					    		}});
				    		}
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
};
if("${param.billId}"){
	editById("${param.billId}");
}
</script>
</body>
</html>
