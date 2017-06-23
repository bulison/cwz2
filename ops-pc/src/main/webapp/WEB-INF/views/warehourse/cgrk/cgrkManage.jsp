<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<c:set var="billCode" value="cgrk" scope="page"></c:set>
<c:set var="billCodeName" value="采购入库单" scope="page"></c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
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
#search-form{
display: inline-block; width: 98%;
}
#search-form span{
  margin-right: 5px;
  margin-bottom: 10px
}
/* #search-form a{
  margin-right: 5px;
  margin-bottom: 10px
} */
#Inquiry{margin-left: 5px;}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; margin-right:19px; position:relative; border: 1px solid #ccc; background: #fff;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:23px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:4px;}
.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:93px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px; width:235px;}
.button_clear{ border-top: 1px solid #D5DBEA;padding-top:8px; text-align: right;}
.roed{
   -moz-transform:scaleY(-1);
   -webkit-transform:scaleY(-1);
   -o-transform:scaleY(-1);
   transform:scaleY(-1);
   filter:FlipV();
}
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
                   <h1>采购入库单</h1>
                </div>             
             </div>
	<div id="addBox"></div>
	<div style="margin: 10px 0px 0px 0px;">	    
			<%-- <a href="${ctx}/warehouse/${billCode}/export" class="btn-ora-export">导出</a> --%>
		<div style="margin-bottom:10px;">
			<fool:tagOpt optCode="cgrkAdd">
		    <a href="javascript:;" id="addNew" class="btn-ora-add" style="vertical-align: top;">新增</a>
			</fool:tagOpt>
			<fool:tagOpt optCode="cgrkSearch">
			<input name="codeOrVoucherCode" id="search-code" _class="textbox" data-options="{prompt:'单号或原始单号',width:160,height:30}"/><a class="Inquiry btn-blue btn-s" style="margin-left:5px;">查询</a>
			<div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly" /><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
			</fool:tagOpt>
		</div>
		<!--<input name="voucherCode" _class="textbox" data-options="{prompt:'原始单号',width:160,height:30}"/> -->
		<div class="input_div">
		<form action="" id="search-form">
			<p>开始日期:<input name="startDay" id="startDay" data-options="{width:160,height:30,editable:false}"/></p>
			<p>结束日期:<input name="endDay" id="endDay" data-options="{width:160,height:30,editable:false}"/></p>
			<p>部门：<input id="search-deptName" name="deptId" _class="deptComBoxTree"/></p>
			<p>仓库：<input id="search-warehouse" name="inWareHouseId" _class="warehouse"/></p>
			<p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	<input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
			<p>供应商:<input id="mySupplierName" name="supplierName" class="textBox" data-options="{novalidate:true,width:160,height:30}"/>
			<input type="hidden" name="supplierId" id="mySupplierId" /></p>
			<p>采购员:<input id="myInMemberName" name="inMemberName" class="textBox" data-options="{novalidate:true,width:160,height:30}"/>
			<input type="hidden" name="inMemberId" id="myInMemberId" /></p>			
			<!-- <input name="recordStatus" _class="recordStatus"/> -->
			<p>货品:<input id="myGoodName" class="textBox" data-options="{novalidate:true,width:160,height:30}"/>
			<input id="myGoodsId" name="goodsId" type="hidden"/></p>
			<div class="button_clear">
			<fool:tagOpt optCode="cgrkSearch">
			<a href="javascript:;" class="btn-blue btn-s" id="search-form-btn" onclick="refreshData()" style="vertical-align:middle;">查询</a>
			</fool:tagOpt>
			<a href="javascript:;" class="btn-blue btn-s" onclick="cleanBoxInput('#search-form')" style="vertical-align:middle;">清空</a>
			<a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
			</div>
			</div>
		</form>
	</div>
	
	<table id="dataTable">
	</table>
	<div id="pager"></div>
<div id="my-window"></div>

<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
enterSearch("Inquiry");//回车搜索
initManage('${billCode}','${billCodeName}');
$('#dataTable').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/warehouse/cgrk/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#dataTable")[0].addJSONData(data.responseJSON);
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
	  		{name:'fid',label:'fid',align:"center",hidden:true,width:100},
	  		{name:'code',label:'单号',align:"center",sortable:true,width:150,<fool:tagOpt optCode="qckcAction1">formatter:codeLinkNew</fool:tagOpt>},
	  		{name:'voucherCode',align:"center",label:'原始单号',sortable:true,width:100},
        	{name:'billDate',align:"center",label:'单据日期',width:100,sortable:true,formatter:dateFormatAction},
        	{name:'supplierName',align:"center",label:'供应商',sortable:true,width:100},
	  		{name:'inMemberName',align:"center",label:'采购员',sortable:true,width:100},
        	{name:'inWareHouseName',align:"center",label:'入仓仓库',sortable:true,width:80},
        	{name:'endDate',align:"center",label:'计划完成日期',sortable:true,width:100,formatter:dateFormatAction},
	  		{name:'freeAmount',align:"center",label:'优惠金额',sortable:true,width:100}, 
	  		{name:'totalAmount',align:"center",label:'合计金额',sortable:true,width:100},
	  		{name:'creatorName',align:"center",label:'制单人',sortable:true,width:100}, 
	  		{name:'recordStatus',align:"center",label:'状态',width:40,sortable:true,formatter:recordStatusAction},
			<fool:tagOpt optCode="qckcAction">
	  		{name:'action',align:"center",label:'操作',formatter:actionFormatNew}
	  		</fool:tagOpt>
	      ],
	      gridComplete:function(){
	    	  	<fool:tagOpt optCode="cgrkAction2">//</fool:tagOpt>$('.btn-del').remove();
	    		<fool:tagOpt optCode="cgrkAction3">//</fool:tagOpt>$('.btn-copy').remove();
	    		<fool:tagOpt optCode="cgrkAction4">//</fool:tagOpt>$('.btn-approve').remove();
	    		<fool:tagOpt optCode="cgrkAction5">//</fool:tagOpt>$('.btn-cancel').remove();
	    		warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

if("${param.billId}"){
	viewRowNew("${param.billId}");
}
</script>
</body>
</html>