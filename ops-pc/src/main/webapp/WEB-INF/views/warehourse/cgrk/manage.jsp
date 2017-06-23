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
#search-form span{
  margin-right: 5px;
  margin-bottom: 10px
}
#search-form a{
  margin-right: 5px;
  margin-bottom: 10px
}
</style>
</head>
<body>
	
	<div style="margin: 10px 0px 0px 0px;">
	    <fool:tagOpt optCode="cgrkAdd">
	    <a href="${ctx}/warehouse/${billCode}/edit" _title="添加${billCodeName}" id="add" class="btn-ora-add" style="vertical-align: top;">新增</a>
		</fool:tagOpt>
			<%-- <a href="${ctx}/warehouse/${billCode}/export" class="btn-ora-export">导出</a> --%>
		<form action="" id="search-form" style="display: inline-block; width: 90%">
			<input name="codeOrVoucherCode" _class="textbox" data-options="{prompt:'单号或凭证号',	width:160,height:30}"/>
		<!-- 	<input name="voucherCode" _class="textbox" data-options="{prompt:'凭证号',width:160,height:30}"/> -->
			<input name="startDay" _class="datebox" data-options="{prompt:'单据开始日期',width:160,height:30,editable:false}"/>
			<input name="endDay" _class="datebox"  data-options="{prompt:'单据结束日期',width:160,height:30,editable:false}"/>
			<input id="mySupplierName" name="supplierName" class="textBox" data-options="{novalidate:true,width:160,height:30,prompt:'供应商'}"/>
			<input type="hidden" name="supplierId" id="mySupplierId" />
			<input id="myInMemberName" name="inMemberName" class="textBox" data-options="{novalidate:true,width:160,height:30,prompt:'采购员'}"/>
			<input type="hidden" name="inMemberId" id="myInMemberId" />
			<input name="deptId" _class="deptComBoxTree"/>
			<input name="inWareHouseId" _class="warehouse"/>
			<input name="recordStatus" _class="recordStatus"/>
			<input id="myGoodName" class="textBox" data-options="{novalidate:true,width:160,height:30,prompt:'货品'}"/>
			<input id="myGoodsId" name="goodsId" type="hidden"/>
			<fool:tagOpt optCode="cgrkSearch">
			<a class="btn-blue btn-s" id="search-form-btn" onclick="refreshData()" style="vertical-align:middle;">筛选</a>
			</fool:tagOpt>
			<a class="btn-blue btn-s" onclick="cleanBoxInput('#search-form')" style="vertical-align:middle;">清空</a>
		</form>
	</div>
	
	<table id="dataTable" data-options="width:'100%',remoteSort:false,pagination:true,onLoadSuccess:load,singleSelect:true,fitColumns:true,url:'${ctx}/warehouse/${billCode}/list'">
		<thead>
			<tr>
				<th data-options="field:'code',width:150,sortable:true<fool:tagOpt optCode="cgrkAction">,formatter:codeLink</fool:tagOpt>">单号</th>
				<th data-options="field:'voucherCode',width:100,sortable:true">凭证号</th>
				<th data-options="field:'billDate',width:100,formatter:dateFormatAction,sortable:true">单据日期</th>
				<!-- <th data-options="field:'supplierCode',width:100">供应商编号</th> -->
				<th data-options="field:'supplierName',width:100,sortable:true">供应商</th>
				<!-- <th data-options="field:'supplierPhone',width:100">供应商电话</th>
				<th data-options="field:'deptName',width:100">部门</th> -->
				<th data-options="field:'inWareHouseName',width:100">仓库</th>
				<th data-options="field:'inMemberName',width:100">采购员</th>
				<th data-options="field:'endDate',width:100,formatter:dateFormatAction,sortable:true">计划完成日期</th>
				<!-- <th data-options="field:'relationName',width:100">采购订单</th> -->
				<th data-options="field:'totalAmount',width:100,formatter:priceAction">合计金额</th>
				<th data-options="field:'freeAmount',width:100,formatter:priceAction">免单金额</th>
				<th data-options="field:'creatorName',width:100">制单人</th>
				<!-- <th data-options="field:'createTime',width:100">制单日期</th>
				<th data-options="field:'auditorName',width:100">审核人</th>
				<th data-options="field:'auditTime',width:100">审核日期</th>
				<th data-options="field:'cancelorName',width:100">作废人</th>
				<th data-options="field:'cancelTime',width:100">作废日期</th> -->
				
				<th data-options="field:'recordStatus',width:100,formatter:recordStatusAction,sortable:true">状态</th>
				<fool:tagOpt optCode="cgrkAction">
				<th data-options="field:'fid',formatter:actionFormat,width:140">操作</th>
				</fool:tagOpt>
			</tr>
		</thead>
	</table>

<div id="my-window"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
initManage('${billCode}','${billCodeName}');

var texts = [
			{title:'供应商',key:'supplierName'},
			{title:'电话',key:'supplierPhone'},
             {title:'NO',key:'code'},
             {title:'地<span style="margin:0px 7px;"></span>址',key:'supplierAddress',colspan:2},
             /* {title:'付款方式：',key:'voucherCode',align:'right'}, */
             {title:'日期',key:'billDate'},
            /*  {title:'采购订单',key:'relationName'},
             
             {title:'供应商编号',key:'supplierCode'},
             {title:'供应商电话',key:'supplierPhone'},
             {title:'部门',key:'deptName'},
             {title:'仓库',key:'inWareHouseName'},
             {title:'采购员',key:'inMemberName'},
             {title:'计划完成日',key:'endDate'},
             {title:'合计金额',key:'totalAmount'}, 
             {title:'免单金额',key:'freeAmount'},
             {title:'备注',key:'describe',br:true} */
             ];
var thead = [
            /*  {title:'条码',key:'billCode'}, */
            {title:'仓库',key:'inWareHouseName'},
             {title:'编号',key:'goodsCode',width:8},
             {title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
            /*  {title:'属性',key:'goodsSpecName'}, */
             {title:'单位',key:'unitName'},
             {title:'数量',key:'quentity'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe',width:10}
             ];
             
var tfoot = [
				/* {dtype:0,key:'quentity',text:'当页数量#'},
				{dtype:3,key:'type',text:'当页金额&nbsp;大写&nbsp;#'},
				{dtype:2,key:'type',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数量#'},
				{dtype:5,key:'type',text:'总金额&nbsp;大写&nbsp;#'},
				{dtype:4,key:'type',text:'¥&nbsp;#&nbsp;元'}, */
				{dtype:3,key:'type',text:'金额合计(大写)：#'},
				{dtype:2,key:'type',text:'小写金额&nbsp;¥#元'},			
			];
function load(){
	<fool:tagOpt optCode="cgrkAction2">//</fool:tagOpt>$('.btn-del').remove();
	<fool:tagOpt optCode="cgrkAction3">//</fool:tagOpt>$('.btn-copy').remove();
	<fool:tagOpt optCode="cgrkAction4">//</fool:tagOpt>$('.btn-approve').remove();
	<fool:tagOpt optCode="cgrkAction5">//</fool:tagOpt>$('.btn-cancel').remove();
}
</script>