<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="cprk" scope="page"></c:set>
<c:set var="billCodeName" value="成品入库单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
</head>
<body>
	<div class="nav-box">
		<ul>
			<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
			<li><a href="#">仓库应用</a></li>
			<li><a href="#" class="curr">${billCodeName}</a></li>
		</ul>
	</div>
	
	<div class="toolBox">
		<div class="toolBox-button" style="margin-right:5px;">
		<fool:tagOpt optCode="cprkAdd">
			<a href="${ctx}/warehouse/${billCode}/edit" _title="添加${billCodeName}" id="add" class="btn-ora-add">新增</a>
		</fool:tagOpt>
			<%-- <a href="${ctx}/warehouse/${billCode}/export" class="btn-ora-export">导出</a> --%>
		</div>
		<form class="toolBox-pane" action="" id="search-form">
			<input type="hidden" name="inMemberId" id="myInMemberId"/>
			<input name="codeOrVoucherCode" _class="textbox" data-options="{prompt:'单号或凭证号',	width:160,height:30}"/>
			<!-- <input name="voucherCode" _class="textbox" data-options="{prompt:'凭证号',width:160,height:30}"/> -->
			<input name="startDay" _class="datebox" data-options="{prompt:'开始日期',width:160,height:30,editable:false}"/>
			<input name="endDay" _class="datebox"  data-options="{prompt:'结束日期',width:160,height:30,editable:false}"/>
			<input name="deptId" _class="deptComBoxTree"/>
			<input name="inMemberName" id="myInMemberName" _class="memberBox"  data-options="{prompt:'入库人',width:160,height:30,editable:false}"/>
			<input name="inWareHouseId" _class="warehouse"/>
			<input name="recordStatus" _class="recordStatus"/>
			<input id="myGoodName" class="textBox" data-options="{novalidate:true,width:160,height:30,prompt:'货品'}"/>
			<input id="myGoodsId" name="goodsId" type="hidden"/>
			<fool:tagOpt optCode="cprkSearch">
			<a class="btn-blue btn-s" id="search-form-btn" onclick="refreshData()">筛选</a>
			</fool:tagOpt>
			<a class="btn-blue btn-s" onclick="cleanBoxInput('#search-form')">清空</a>
		</form>
	</div>
	
	<table id="dataTable" data-options="width:'100%',remoteSort:false,pagination:true,onLoadSuccess:load,singleSelect:true,fitColumns:true,url:'${ctx}/warehouse/${billCode}/list'">
		<thead>
			<tr>
				<th data-options="field:'code',width:10,sortable:true<fool:tagOpt optCode="cprkAction">,formatter:codeLink</fool:tagOpt>">单号</th>
				<th data-options="field:'voucherCode',width:10,sortable:true,">凭证号</th>
				<th data-options="field:'billDate',width:10,formatter:dateFormatAction,sortable:true,">单据日期</th>
				<!-- <th data-options="field:'deptName',width:10">部门</th> -->
				<th data-options="field:'inWareHouseName',width:10">仓库</th>
				<th data-options="field:'inMemberName',width:10">入库人</th>
				<th data-options="field:'relationName',width:10">生产领料单</th>
				<th data-options="field:'recordStatus',width:10,formatter:recordStatusAction,sortable:true,">状态</th>
				<fool:tagOpt optCode="cprkAction">
				<th data-options="field:'fid',formatter:actionFormat,width:10">操作</th>
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
             /* {title:'凭证号',key:'voucherCode'},
             {title:'生产领料单',key:'relationName'},
             {title:'部门',key:'deptName'},
             {title:'仓库',key:'inWareHouseName'},
             {title:'备注',key:'describe',br:true}, */
             
             {title:'入库人',key:'inMemberName'},
             {title:'NO',key:'code'},
             {title:'日期',key:'billDate'},
             ];
var thead = [
			{title:'仓库',key:'inWareHouseName'},
             {title:'编号',key:'goodsCode',width:8},
             {title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
             /* {title:'规格',key:'goodsSpec'},
             {title:'属性',key:'goodsSpecName'}, */
             {title:'单位',key:'unitName'},
             {title:'数量',key:'quentity'},
             {title:'单价',key:'unitPrice'},
             {title:'金额',key:'type'},
             {title:'备注',key:'describe',width:10}
             ];
             
var tfoot = [
				/* {dtype:0,key:'quentity',text:'当页总数#'},
				{dtype:3,key:'unitPrice',text:'当页小计&nbsp;大写&nbsp;#'},
				{dtype:2,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#'},
				{dtype:5,key:'unitPrice',text:'合计&nbsp;大写金额&nbsp;#'},
				{dtype:4,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'}, */
				
				{dtype:3,key:'type',text:'金额合计(大写)：#'},
				{dtype:2,key:'type',text:'小写金额&nbsp;¥#元'},
			];
function load(){
	<fool:tagOpt optCode="cprkAction2">//</fool:tagOpt>$('.btn-del').remove();
	<fool:tagOpt optCode="cprkAction3">//</fool:tagOpt>$('.btn-copy').remove();
	<fool:tagOpt optCode="cprkAction4">//</fool:tagOpt>$('.btn-approve').remove();
	<fool:tagOpt optCode="cprkAction5">//</fool:tagOpt>$('.btn-cancel').remove();
}
</script>