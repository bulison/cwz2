<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>货品定价</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<style>
#afather a[class|=btn]{
	margin-right:5px;
}
.fixed {
    top: 0px;
}
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">系统设置</a></li>
	        <li><a href="javascript:;" class="curr">货品定价</a></li>
	    </ul>
	</div>
	<div id="addBox"></div>
	<div id="importBox"></div> 
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>货品定价</h1>
                </div>             
             </div>
	
	<div id="afather" style="margin: 10px 0px;"> 
	<fool:tagOpt optCode="gpriceAdd"><a href="javascript:;" id="add" class="btn-ora-add" >新增</a></fool:tagOpt>
	<fool:tagOpt optCode="gpriceExport"><a href="javascript:;" id="getOut" class="btn-ora-export" >导出</a></fool:tagOpt>
	<fool:tagOpt optCode="gpriceImport"><a href="javascript:;" id="getIn" class="btn-ora-import" >导入</a></fool:tagOpt>
	<fool:tagOpt optCode="gpriceRefresh"><a href='javascript:;' id="refresh" class="btn-ora-refresh">刷新</a></fool:tagOpt>
	<fool:tagOpt optCode="gpriceSearch"><input id="search" class="easyui-searchbox" data-options="prompt:'请输入编码或货品名称',iconWidth:26,width:220,height:32,value:'',	searcher:function(value,name){refreshData();}"/></fool:tagOpt>
	</div>
	<table id="goodsPriceList"></table>
	<div id="pager"></div>
<script type="text/javascript">
$('#add').click(function(){
	$('#addBox').window({
		title:'新增货品定价',
		top:40+$(window).scrollTop(),  
		left:($(window).outerWidth()-800)/2,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/goodsPriceController/addGoodsPrice',
		width:800,
		onOpen:function(){
			$("html").css("overflow","hidden");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});
	$('#addBox').window("resize");
});

$('#refresh').click(function(){
	/* $('#goodsPriceList').datagrid('reload'); */
	$("#goodsPriceList").trigger("reloadGrid"); 
});

/* $('#goodsPriceList').datagrid({
	url:'${ctx}/goodsPriceController/list',
	idField:'fid',
	pagination:true,
	fitColumns:true,
	remoteSort:false,
	singleSelect:true,
	onLoadSuccess:function(){
		warehouseAll();
	},
	columns:[[
			{field:'salesLowerLimit1',title:'销售一级价下限',hidden:true,width:100}, 
			{field:'salesLowerLimit2',title:'销售二级价下限',hidden:true,width:100},
			{field:'salesUpperLimit1',title:'销售一级价上限',hidden:true,width:100},
			{field:'salesUpperLimit2',title:'销售二级价上限',hidden:true,width:100},
			{field:'purchaseLowerLimit1',title:'采购一级价下限',hidden:true,width:100},
			{field:'purchaseLowerLimit2',title:'采购二级价下限',hidden:true,width:100},
			{field:'purchaseUpperLimit1',title:'采购一级价上限',hidden:true,width:100},
			{field:'purchaseUpperLimit2',title:'采购二级价上限',hidden:true,width:100},
			{field:'purchasingCycle',title:'增加采购周期（天）',hidden:true,width:100},
			{field:'capacity',title:'生产产能（计量数量/天）',hidden:true,width:100},
			
			{field:'fid',title:'fid',hidden:true,width:100},
			{field:'goodsCode',title:'编码',sortable:true,width:80},
	  		{field:'goodsName',title:'货品名称',sortable:true,width:100,formatter:function(value,row,index){
	  			return "<a href='javascript:;' title='编辑' onclick='editById(\""+row.fid+"\")'>"+value+"</a>";
	  		}},
	  		{field:'barCode',title:'条码',sortable:true,width:80},
	  		{field:'spec',title:'规格',sortable:true,width:70},
	  		{field:'goodsSpecName',title:'属性',sortable:true,width:80},
	  		{field:'unitName',title:'单位',sortable:true,width:100},
	  		{field:'salePrice',title:'销售价',sortable:true,sortable:true,width:100},
	  		{field:'lowestPrice',title:'最低售价',sortable:true,width:100},
			{field:'vipPrice',title:'会员价',sortable:true,width:100},
	  		{field:'lowestStock',title:'最低库存数',sortable:true,width:60},
	  		{field:'heightestStock',title:'最高库存数',width:60},
	  		{field:'createTime',title:'创建时间',width:120},
	  		{field:'updateTime',title:'修改时间戳',hidden:true,width:100},
	  		<fool:tagOpt optCode="gpriceAction">
	  		{field:'action',title:'操作',width:80,formatter:function(value,row,index){
	  			var statusStr = ''; */
	  			/* <fool:tagOpt optCode="gpriceAction1">statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a>';</fool:tagOpt> */
	  			/* <fool:tagOpt optCode="gpriceAction2">statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  			 return statusStr ; 
	  		}}
	  		</fool:tagOpt>
	      ]]
}); */

$("#goodsPriceList").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/goodsPriceController/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#goodsPriceList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	/* footerrow: true, */
	autowidth:true,
	height:$(window).height()-$(".titleCustom").height()-$("#afather").height()-120,
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[ 
                {name : 'fid',label : 'fid',hidden:true}, 
                {name : 'goodsCode',label : 'goodsCode',hidden:true}, 
                {name:'goodsCode',label:'货品编号',align:'center',width:"100px"},
                {name : 'goodsName',label : '货品名称',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
    	  			return "<a href='javascript:;' title='编辑' onclick='editById(\""+rowObject.fid+"\")'>"+cellvalue+"</a>";
    	  		}}, 
    	  		{name:'barCode',label:'条码',align:'center',width:"100px"},
    	  		{name:'spec',label:'规格',align:'center',width:"100px"},
    	  		{name:'goodsSpecName',label:'属性',align:'center',width:"100px"},
    	  		{name:'unitName',label:'单位',align:'center',width:"100px"},
    	  		{name:'salePrice',label:'销售价',align:'center',width:"100px"},
    	  		{name:'lowestPrice',label:'最低售价',align:'center',width:"100px"},
    			{name:'vipPrice',label:'会员价',align:'center',width:"100px"},
    	  		{name:'lowestStock',label:'最低库存数',align:'center',width:"100px"},
    	  		{name:'heightestStock',label:'最高库存数',align:'center',width:"100px"},
    	  		{name:'createTime',label:'创建时间',align:'center',width:"140px"},
                {name:"action",label:"操作",align:'center',width:"80px",formatter:function(cellvalue, options, rowObject){
                	var statusStr = '';
    	  			<fool:tagOpt optCode="gpriceAction2">statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+rowObject.fid+'\')"></a>';</fool:tagOpt>
    	  			return statusStr ;
	            }},
              ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

//查找
function refreshData(){
	var options = {"searchKey": $('#search').val()};
	$("#goodsPriceList").jqGrid('setGridParam',{postData:options}).trigger("reloadGrid"); 
}

//编辑 
function editById(fid){
	$('#addBox').window({
		title:'修改货品定价', 
		top:40,
		left:($(window).outerWidth()-800)/2,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/goodsPriceController/editGoodsPrice?fid="+fid,
		width:800,  
		onOpen:function(){
			$("html").css("overflow","hidden");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});
	$('#addBox').window("resize");
} 

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/goodsPriceController/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								/* $('#goodsPriceList').datagrid('reload'); */
								$("#goodsPriceList").trigger("reloadGrid"); 
							}});
						}else if(data.result == '1'){
							$.fool.alert({msg:data.msg});
						}
		    		}
				});
		 }
	 }});
}

$(function(){
	
	$("#getOut").click(function(){
		exportFrom('${ctx}/goodsPriceController/export',{"goodsName": $('#search').val()});
	});
	
	$("#getIn").click(function(){
		var s={
				target:$("#importBox"),
				boxTitle:"导入货品价格",
				downHref:"${ctx}/ExcelMapController/downTemplate?downFile=货品定价.xls",
				action:"${ctx}/goodsPriceController/import",
				fn:'okCallback()'
		};
		importBox(s);
	});
	 
	//分页条 
	//setPager($('#goodsPriceList'));
});

function okCallback(){
	$("#goodsPriceList").trigger("reloadGrid"); 
	/* $("#goodsPriceList").datagrid("reload"); */
	$("#importBox").window("close");
}
</script>
</body>
</html>
