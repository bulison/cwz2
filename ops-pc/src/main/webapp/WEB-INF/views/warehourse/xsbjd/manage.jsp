<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="xsbjd" scope="page"></c:set>
<c:set var="billCodeName" value="销售报价单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<style>
  .open{
	background:url(/ops/resources/images/open.png) no-repeat;
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
  
#userChooser,#customerChooser,#goodsChooser{
  display: none;

}

#userSearcher,#cusSearcher,#goodsSearcher{
  text-align: left;
  margin:10px;
}
#search-form span{
  margin-right: 5px;
  margin-bottom: 10px
}
#search-form a{
  margin-right: 5px;
  margin-bottom: 10px
}
  .form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{width:115px;}
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
	
	<div style="margin:10px 0px 0px 0px;">
		<a href="${ctx}/warehouse/${billCode}/edit" _title="添加${billCodeName}" id="add" class="btn-ora-add"  style="vertical-align: top;">新增</a>
		<form action="" id="search-form" style="display: inline-block; width: 90%">
			<input name="codeOrVoucherCode" _class="textbox" data-options="{prompt:'单号',width:160,height:30}"/>
			<input name="startDay" _class="datebox" data-options="{prompt:'单据开始日期',width:100,height:30,editable:false}"/>
			<input name="endDay" _class="datebox"  data-options="{prompt:'单据结束日期',width:100,height:30,editable:false}"/>
			<input id="myCustomerName" name="customerName" data-options="{editable:false,prompt:'销售商',	width:160,height:32}"/><input id="myCustomerId" name="customerId" type="hidden"/>
			<input id="myInMemberName" name="inMemberName" class="textBox" data-options="{editable:false,prompt:'业务员',	width:160,height:32}"/><input type="hidden" name="inMemberId" id="memberVal" />
			<input name="recordStatus" _class="recordStatus"/>
			<input id="myGoodName" class="textBox" data-options="{novalidate:true,width:160,height:30,prompt:'货品'}"/>
			<input id="myGoodsId" name="goodsId" type="hidden"/>
			<a class="btn-blue btn-s" id="search-form-btn" style="vertical-align:middle;" onclick="refreshData()">筛选</a>
			<a class="btn-blue btn-s" style="vertical-align:middle;" onclick="cleanBoxInput('#search-form')">清空</a> 
		</form>
	</div>
	
	
	
	<table id="dataTable" data-options="width:'100%',remoteSort:false,pagination:true,singleSelect:true,fitColumns:true,url:'${ctx}/warehouse/${billCode}/list'">
		<thead>
			<tr>
				<th data-options="field:'code',width:10,sortable:true,formatter:codeLink">单号</th>
				<th data-options="field:'billDate',width:10,formatter:dateFormatAction,sortable:true,">单据日期</th>
				<!-- <th data-options="field:'customerCode',width:10,sortable:true,">销售商编号</th> -->
				<th data-options="field:'customerName',width:10,sortable:true,">销售商</th>
				<!-- <th data-options="field:'customerPhone',width:10">销售商电话</th>
				<th data-options="field:'deptName',width:10">部门</th> -->
				<th data-options="field:'inMemberName',width:10">业务员</th>
				<th data-options="field:'creatorName',width:10">制单人</th>
				<!-- <th data-options="field:'createTime',width:10">制单日期</th>
				<th data-options="field:'auditorName',width:10">审核人</th>
				<th data-options="field:'auditTime',width:10">审核日期</th>
				<th data-options="field:'cancelorName',width:10">作废人</th>
				<th data-options="field:'cancelTime',width:10">作废日期</th> -->
				<th data-options="field:'recordStatus',width:10,formatter:recordStatusAction,sortable:true,">状态</th>
				<th data-options="field:'fid',formatter:actionFormat,width:10">操作</th>
			</tr>
		</thead>
	</table>

<div id="my-window"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript">
initManage('${billCode}','${billCodeName}');

var texts = [
              {title:'单号',key:'code'},
              {title:'单据日期',key:'billDate'},
              {title:'客户名称',key:'customerName'},
              {title:'客户编号',key:'customerCode'},
              {title:'客户电话',key:'customerPhone'},
              {title:'部门',key:'deptName'},
              {title:'业务员',key:'inMemberName'},
              {title:'单据日期',key:'billDate'},
              {title:'备注',key:'describe',br:true}
              ];
var thead = [
              /* {title:'条码',key:'billCode',subsumStr:'当页小计'}, */
              {title:'编号',key:'goodsCode'},
              {title:'名称',key:'goodsName'},
              {title:'规格',key:'goodsSpec'},
              {title:'属性',key:'goodsSpecName'},
              {title:'单位',key:'unitName'},
              {title:'数量',key:'quentity',textAlign:'right'},
              {title:'单价',key:'unitPrice',textAlign:'right'},
              {title:'金额',key:'type',textAlign:'right'},
              {title:'备注',key:'describe'}
              ];
              
var tfoot = [
				{dtype:0,key:'quentity',text:'当页总数#'},
				{dtype:3,key:'unitPrice',text:'当页小计&nbsp;大写&nbsp;#'},
				{dtype:2,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#'},
				{dtype:5,key:'unitPrice',text:'合计&nbsp;大写&nbsp;#'},
				{dtype:4,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
			];

</script>
