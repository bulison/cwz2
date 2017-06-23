<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>销售单据信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
  .open{
	background:url(${ctx}/resources/images/open.png) no-repeat;
	padding-left:0px;
	width:40px;
	background-position:0px -1px;
}

#goodsChooser{
  display: none;
  text-align: center;
}

#add{
  vertical-align: middle;
  margin-top: 5px;
}
#search-form{
  display: inline-block;
  width:92%;
}
#search-form .textbox{
  margin:5px 5px 0px 0px
}
#search-form .btn-s{
  margin:5px 5px 0px 0px;
  vertical-align: middle;
}

#btnBox{
  text-align: center;
}

.form p.hideOut,.form1 p.hideOut{
    display: none;
  }
.form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{width:115px;}
  
#userChooser,#customerChooser,#goodsChooser,#goodsSpecChooser{
  display: none;

}

#userSearcher,#cusSearcher,#goodsSearcher,#goodsSpecSearcher{
  text-align: left;
  margin:10px
}
</style>
</head>
<body>
	<div id="addBox"></div>
	<div style="margin:5px 0px 10px 0px"> 
	<form action="" id="search-form"  style="display: inline-block; width: 93%;vertical-align:middle;">
	<fool:tagOpt optCode="xsddAdd">
	<a href="#" id="add" class="btn-ora-add" >新增</a> 
	</fool:tagOpt>
	<fool:tagOpt optCode="xsddSearch">
	  <input id="search-code"/><input id="search-startDay" /><input id="search-endDay"/><input id="search-status"/><input id="search-cusName"/><input id="search-cusId" type="hidden"/><input id="inMemberName" name="inMemberName" class="textBox"/><input type="hidden" name="inMemberId" id="inMemberId" /><input id="select-goods"><input id="select-goodsid" type="hidden"/><a id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">筛选</a><a style="vertical-align:middle;" id="clear-btn" class="btn-blue btn-s">清空</a>
	</fool:tagOpt>
	</form>
	</div>
	<table id="billList"></table>
<script type="text/javascript">
enterSearch("search-btn");
var texts = [
            {title:'原始单号',key:'voucherCode'}, 
             {title:'单号',key:'code'},
             /* {title:'单<span style="margin:0px 7px;"></span>据<span style="margin:0px 7px;"></span>日<span style="margin:0px 7px;"></span>期',key:'billDate'}, */
             {title:'单据日期',key:'billDate'},
             {title:'客户名称',key:'customerName'}, 
             {title:'客户编号',key:'customerCode'},
             {title:'客户电话',key:'customerPhone'},
             {title:'部门',key:'deptName'},
             {title:'业务员',key:'inMemberName'},
             {title:'计划完成日期',key:'endDate'},  
             /* {title:'合计金额',key:'totalAmount'},
             {title:'免单金额',key:'freeAmount'}, */
             {title:'备注',key:'describe',br:true}
             ];
var thead = [
             /* {title:'条码',key:'billCode'}, */
             {title:'编号',key:'goodsCode'},
             {title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
             /* {title:'规格',key:'goodsSpec'},
             {title:'属性',key:'goodsSpecName'}, */
             {title:'单位',key:'unitName'},
             /* {title:'换算关系',key:'scale'}, */
             {title:'数量',key:'quentity'},
            /*  {title:'单价',key:'unitPrice'},
             {title:'金额',key:'type'}, */
             {title:'备注',key:'describe'}
             ];
var tfoot = [
				/* {dtype:0,key:'quentity',text:'当页总数#'},
				{dtype:3,key:'unitPrice',text:'当页小计&nbsp;大写&nbsp;#'},
				{dtype:2,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#'},
				{dtype:5,key:'unitPrice',text:'合计&nbsp;大写&nbsp;#'},
				{dtype:4,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'}, */
			];

var goodsSpecChooserOpen=false;
var goodsChooserOpen=false;
$('#add').click(function(){
	$('#addBox').window({
		title:'新增销售单据',
		top:10,
		left:0,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/salebill/xsdd/edit',
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			
		}
	});
});

$("#clear-btn").click(function(){
	$("#search-form").form("clear");
});

$('#refresh').click(function(){
	$('#billList').datagrid('reload');
});

$("#search-code").textbox({
	prompt:'单号或原始单号',
	width:160,
	height:32
});

$("#search-warehouse").combotree({
	prompt:'仓库',
	width:160,
	height:32,
	editable:false,
	url:"${ctx}/basedata/warehourseList"
});

$("#search-status").combobox({
	valueField:"value",
	textField:"text",
	prompt:'状态',
	width:160,
	height:32,
	editable:false,
	data:[{value:0,text:"未审核"},{value:1,text:"已审核"},{value:2,text:"已作废"}],
	onLoadSuccess:function(){
		$(this).combobox('clear');
	}
});

$("#search-status").combobox({ 
	prompt:'状态',
	width:160,
	height:32,
	editable:false
});

$("#search-startDay").datebox({
	prompt:'开始日期',
	width:160,
	height:32,
	editable:false
});

$("#search-endDay").datebox({
	prompt:'结束日期',
	width:160,
	height:32,
	editable:false
});
$("#search-cusName").textbox({
	prompt:'客户',
	width:160,
	height:32,
	editable:false,
});
$('#inMemberName').textbox({
	novalidate:true,
	width:160,
	height:32,
	prompt:'业务员'
});

$("#select-goods").textbox({
	prompt:'货品',
	width:160,
	height:30,
	editable:false,
});

//货品
$("#select-goods").textbox('textbox').click(function(){
	chooser=$.fool.window({'title':"选择货品",href:'${ctx}/goods/window?okCallBack=selectgoodsSearch&onDblClick=selectgoodsSearchDBC&singleSelect=true',
		onClose:function(){
			$(this).window("destroy");
		}		
	});
});

function selectgoodsSearch(rowData){
	$("#select-goodsid").val(rowData[0].fid);
	$("#select-goods").textbox('setValue',rowData[0].name);
	chooser.window('close');
}
function selectgoodsSearchDBC(rowData){
	$("#select-goodsid").val(rowData.fid);
	$("#select-goods").textbox('setValue',rowData.name);
	chooser.window('close');
}

$('#inMemberName').textbox('textbox').click(function(){
	win = $.fool.window({width:780,height:480,href:"${ctx}/member/window?okCallBack=inMemberName&onDblClick=inMemberNameDBC&singleSelect=true",'title':'选择采购员'});
});
function inMemberName(data){
	$('#inMemberName').textbox('setValue',data[0].username);
	$('#inMemberId').val(data[0].fid);
	win.window('close');
}
function inMemberNameDBC(data){
	$('#inMemberName').textbox('setValue',data.username);
	$('#inMemberId').val(data.fid);
	win.window('close');
}
$("#search-cusName").textbox('textbox').click(function(){
	chooser=$.fool.window({width:780,height:480,'title':"选择客户",href:'${ctx}/customer/window?okCallBack=selectCusSearch&onDblClick=selectCusSearchDBC&singleSelect=true'}); 
});
function selectCusSearch(rowData){
	$("#search-cusId").val(rowData[0].fid);
	$("#search-cusName").textbox('setValue',rowData[0].name);
	chooser.window('close');
}
function selectCusSearchDBC(rowData){
	$("#search-cusId").val(rowData.fid);
	$("#search-cusName").textbox('setValue',rowData.name);
	chooser.window('close');
}

var options = [
    		    {id:'0',name:'未审核'},
    		    {id:'1',name:'已审核'},
    		    {id:'2',name:'已作废'}
    		];
$('#billList').datagrid({
	url:'${ctx}/salebill/xsdd/list',
	width:'100%',
	idField:'fid',
	pagination:true,
	fitColumns:true,
	remoteSort:false,
	columns:[[
	  		{field:'fid',title:'fid',hidden:true,width:100}, 
	  		{field:'code',title:'单号',sortable:true,width:100,<fool:tagOpt optCode="qckcAction1">formatter:function(value,row,index){
	  			return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')">'+value+'</a>';
	  		}</fool:tagOpt>},
	  		{field:'voucherCode',title:'原始单号',width:100,sortable:true},
	  		{field:'customerName',title:'客户名称',width:100},
	  		{field:'billDate',title:'单据日期',width:40,sortable:true,formatter:function(value){
	  			return value.substring(0,10);
	  		}},
	  		{field:'inMemberName',title:'业务员',width:100},
	  		{field:'totalAmount',title:'合计金额',width:40,sortable:true,formatter:function(value){
	  			if(value=="0E-8"){
	  				return 0;
	  			}else{
	  				return value;
	  			}
	  		}},
	  		{field:'recordStatus',title:'状态',width:40,sortable:true,formatter:function(value){
				for(var i=0; i<options.length; i++){
					if (options[i].id == value) return options[i].name;
				}
				return value;
			}},
			<fool:tagOpt optCode="xsddAction">
	  		{field:'action',title:'操作',formatter:function(value,row,index){
	  			if(row.recordStatus==0){
	  				var statusStr = '';
	  				<fool:tagOpt optCode="xsddAction2">
		  			 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
		  			</fool:tagOpt>
		  			<fool:tagOpt optCode="xsddAction3">
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			</fool:tagOpt>
		  			<fool:tagOpt optCode="xsddAction4">
		  			 statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+row.fid+'\')"></a> ';
		  			</fool:tagOpt>
		  			<fool:tagOpt optCode="xsddAction5">
		  			 statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
		  			</fool:tagOpt>
		  			 return statusStr ; 
	  			}else if(row.recordStatus==1){
	  				var statusStr = '';
	  				<fool:tagOpt optCode="xsddAction5">
		  			 statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
		  			</fool:tagOpt>
		  			<fool:tagOpt optCode="xsddAction3">
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			</fool:tagOpt>
		  			 return statusStr ; 
	  			}else if(row.recordStatus==2){
	  				var statusStr = '';
	  				<fool:tagOpt optCode="xsddAction3">
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			</fool:tagOpt>
		  			 return statusStr ; 
	  			}
	  		}}
	  		</fool:tagOpt>
	      ]]
});

$("#search-btn").click(function (){
	var code=$("#search-code").textbox('getValue');
	var recordStatus=$("#search-status").combobox('getValue');
	var startDay=$("#search-startDay").datebox('getValue');
	var endDay=$("#search-endDay").datebox('getValue');
	var customerId=$("#search-cusId").val();
	var inMemberId=$('#inMemberId').val();
	var goodsId=$("#select-goodsid").val();
	var options = {"codeOrVoucherCode":code,"inMemberId":inMemberId,"customerId":customerId,"recordStatus":recordStatus,"startDay":startDay,"endDay":endDay,"goodsId":goodsId};
	$('#billList').datagrid('load',options);
})

//编辑 
function editById(fid,mark,detail){
	var title = "修改销售单据";
	if(mark==1){
		title = "查看销售单据";
	}
	$('#addBox').window({
		title:title, 
		top:10,
		left:0,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/salebill/xsdd/edit?id="+fid,
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			
		}
	});
} 

//复制
function copyById(fid){
	$('#addBox').window({
		title:'新增销售单据', 
		top:10,
		left:0,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/salebill/xsdd/edit?id="+fid+"&mark=1",
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			
		}
	});
}

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/salebill/xsdd/delete',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
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
					url : '${ctx}/salebill/xsdd/passAudit',
					data : {id : fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
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

function cancelById(fid) {
	 $.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/salebill/xsdd/cancel',
					data : {id :fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});

};


$(function(){
	//分页条 
	//setPager($('#billList'));
});
</script>
</body>
</html>
