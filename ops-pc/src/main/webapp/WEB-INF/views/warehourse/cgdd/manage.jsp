<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="cgdd" scope="page"></c:set>
<c:set var="billCodeName" value="采购订单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<style>
.open {
	background: url(${ctx}/resources/images/open.png) no-repeat;
	padding-left: 0px;
	width: 40px;
	background-position: 0px -1px;
}

#goodsChooser {
	display: none;
	text-align: center;
}

#btnBox {
	text-align: center;
}

.form p.hideOut,.form1 p.hideOut {
	display: none;
}

.form,.form1 {
	padding: 5px 0px;
}

.form1 p {
	margin: 5px 0px
}

.form p font,.form1 p font {
	width: 115px;
}

#userChooser,#customerChooser,#goodsChooser,#goodsSpecChooser {
	display: none;
}

#userSearcher,#cusSearcher,#goodsSearcher,#goodsSpecSearcher {
	text-align: left;
	margin: 10px
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
	<div class="nav-box">
		<ul>
			<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
			<li><a href="#">仓库应用</a></li>
			<li><a href="#" class="curr">${billCodeName}</a></li>
		</ul>
	</div>
	
	<div style="margin: 10px 0px 0px 0px;">
			<fool:tagOpt optCode="cgddAdd">
			   <a href="${ctx}/warehouse/${billCode}/edit" _title="添加${billCodeName}" id="add" class="btn-ora-add" style="vertical-align: top;">新增</a>
			</fool:tagOpt>
			<%-- <a href="${ctx}/warehouse/${billCode}/export" class="btn-ora-export">导出</a> --%>
		<form action="" id="search-form" style="display: inline-block; width: 90%">
			<input name="codeOrVoucherCode" _class="textbox" data-options="{prompt:'单号或凭证号',	width:160,height:30}"/>
			<input name="startDay" _class="datebox" data-options="{prompt:'开始日期',editable:false}"/>
			<input name="endDay" _class="datebox"  data-options="{prompt:'结束日期',editable:false}"/>
			<!-- <input name="voucherCode" _class='textbox' data-options="{prompt:'凭证号',width:160,height:30}"/> -->
			<input id="mySupplierName" name="supplierName" class="textBox" data-options="{novalidate:true,width:160,height:30,prompt:'供应商'}"/>
			<input type="hidden" name="supplierId" id="mySupplierId" />
			<input id="myInMemberName" name="inMemberName" class="textBox" data-options="{novalidate:true,width:160,height:30,prompt:'采购员'}"/>
			<input type="hidden" name="inMemberId" id="myInMemberId" />
			<input name="deptId" _class="deptComBoxTree"/>			
			<input name="recordStatus" _class="recordStatus"/>
			<input id="myGoodName" class="textBox" data-options="{novalidate:true,width:160,height:30,prompt:'货品'}"/>
			<input id="myGoodsId" name="goodsId" type="hidden"/>
			<fool:tagOpt optCode="cgddSearch">
			   <a href="javascript:;" class="btn-blue btn-s search-form" id="search-form-btn" onclick="refreshData()" style="vertical-align:middle;">筛选</a>
			</fool:tagOpt>
			<a href="javascript:;" class="btn-blue btn-s" onclick="cleanBoxInput('#search-form')" style="vertical-align:middle;">清空</a>
		</form>
	</div>
	
	<table id="dataTable" data-options="width:'100%',onLoadSuccess:load,remoteSort:false,pagination:true,singleSelect:true,fitColumns:true,url:'${ctx}/warehouse/${billCode}/list'">
		<thead>
			<tr>
				<th data-options="field:'code',width:20,sortable:true<fool:tagOpt optCode="cgddAction1">,formatter:codeLink</fool:tagOpt>">单号</th>
				<th data-options="field:'voucherCode',width:10,sortable:true">凭证号</th>
				<th data-options="field:'billDate',width:10,formatter:dateFormatAction,sortable:true">单据日期</th>
				<!-- <th data-options="field:'supplierCode',width:10">供应商编号</th> -->
				<th data-options="field:'supplierName',width:10,sortable:true">供应商名称</th>
				<!-- <th data-options="field:'supplierPhone',width:10">销售商电话</th> -->
				<!-- <th data-options="field:'deptName',width:10">部门</th> -->
				<th data-options="field:'inMemberName',width:10">采购员</th>
				<th data-options="field:'endDate',width:10,formatter:dateFormatAction,sortable:true">计划完成日期</th>
				<th data-options="field:'relationName',width:20">销售订单</th>
				<th data-options="field:'totalAmount',width:10">合计金额</th>
				<!-- <th data-options="field:'freeAmount',width:10">免单金额</th> -->
				<th data-options="field:'creatorName',width:10">制单人</th>
				<!-- <th data-options="field:'createTime',width:10">制单日期</th>
				<th data-options="field:'auditorName',width:10">审核人</th>
				<th data-options="field:'auditTime',width:10">审核日期</th>
				<th data-options="field:'cancelorName',width:10">作废人</th>
				<th data-options="field:'cancelTime',width:10">作废日期</th> -->
				
				<th data-options="field:'recordStatus',width:10,formatter:recordStatusAction,sortable:true">状态</th>
				<fool:tagOpt optCode="cgddAction">
				<th data-options="field:'fid',formatter:actionFormat,width:10">操作</th>
				</fool:tagOpt>
			</tr>
		</thead>
	</table>

<div id="my-window"></div>
<a style="display:none;" href="javaScript:printBillDetail('402881ed50a6c0710150a6e2d8d60005','cgdd')">打印</a>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
initManage('${billCode}','${billCodeName}');

var texts = [
             {title:'单号',key:'code'},
             {title:'凭证号',key:'voucherCode'},
             {title:'单据日期',key:'billDate'},
             {title:'销售订单',key:'relationName'},
             {title:'供应商名称',key:'supplierName'},
             {title:'供应商编号',key:'supplierCode'},
             {title:'供应商电话',key:'supplierPhone'},
             {title:'部门',key:'deptName'},
             {title:'采购员',key:'inMemberName'},
             /*{title:'计划完成日期',key:'endDate'},
             {title:'合计金额',key:'totalAmount'},
             {title:'免单金额',key:'freeAmount'},*/
             {title:'备注',key:'describe',br:true}
             ];
var thead = [
            /* {title:'条码',key:'billCode',subsumStr:'当页小计'},*/
             {title:'编号',key:'goodsCode'},
             {title:'名称',key:'goodsName'},
             {title:'规格',key:'goodsSpec'},
             {title:'属性',key:'goodsSpecName'},
             {title:'单位',key:'unitName',textAlign:'center'},
             {title:'数量',key:'quentity',textAlign:'right'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe'}
             ];

var tfoot = [
				{dtype:0,key:'quentity',text:'当页总数#'},
				{dtype:3,key:'type',text:'当页小计&nbsp;大写&nbsp;#'},
				{dtype:2,key:'type',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#'},
				{dtype:5,key:'type',text:'合计&nbsp;大写金额&nbsp;#'},
				{dtype:4,key:'type',text:'¥&nbsp;#&nbsp;元'},
			];
function load(){
	<fool:tagOpt optCode="cgddAction2">//</fool:tagOpt>$('.btn-del').remove();
	<fool:tagOpt optCode="cgddAction3">//</fool:tagOpt>$('.btn-copy').remove();
	<fool:tagOpt optCode="cgddAction4">//</fool:tagOpt>$('.btn-approve').remove();
	<fool:tagOpt optCode="cgddAction5">//</fool:tagOpt>$('.btn-cancel').remove();
}
</script>