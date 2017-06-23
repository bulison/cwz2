<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="billCode" value="xsfld" scope="page"></c:set>
<c:set var="billCodeName" value="销售返利单" scope="page"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${billCodeName}</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
.toolBox a {
	margin: 0 5px 0 0;
}

.toolBox {
	margin-bottom: 10px;
}

.toolBox span.textbox {
	margin: 0 5px 0 0;
}

.toolBox-panel {
	display: inline-block;
}

.Inquiry {
	font-size: 14px !important;
}

#grabble {
	float: right;
	display: inline-block;
}

#bolting {
	width: 160px;
	height: 27px;
	position: relative;
	border: 1px solid #ccc;
	background: #fff;
	margin-right: 19px
}

.button_a {
	display: block;
	width: 25px;
	height: 25px;
	background: #76D5FC;
	-moz-border-radius: 3px; /* Gecko browsers */
	-webkit-border-radius: 3px; /* Webkit browsers */
	border-radius: 3px; /* W3C syntax */
	float: right;
	right: 18px;
	top: 3px;
	position: absolute;
}

.button_span {
	width: 0;
	height: 0;
	border-left: 8px solid transparent;
	border-right: 8px solid transparent;
	border-top: 8px solid #fff;
	top: 10px;
	position: absolute;
	right: 4px;
}

.input_div {
	display: none;
	background: #F5F5F5;
	padding: 10px 0px 5px 0px;
	border: 1px solid #D5DBEA;
	position: absolute;
	right: 19px;
	top: 31px;
	z-index: 1;
}

.input_div p {
	font-size: 12px;
	color: #747474;
	vertical-align: middle;
	text-align: right;
	margin: 10px 20px 0px 10px;
	width: 235px;
}

.button_clear {
	border-top: 1px solid #D5DBEA;
	padding-top: 10px;
	text-align: right;
	margin-top: 10px;
}

.roed {
	-moz-transform: scaleY(-1);
	-webkit-transform: scaleY(-1);
	-o-transform: scaleY(-1);
	transform: scaleY(-1);
	filter: FlipV();
}
#bill p.hideOut{
  display: none ;
}
#search-form .dhxDiv{
	margin:5px 5px 0px 0px;
}
</style>
</head>
<body>
	<div class="titleCustom">
		<div class="squareIcon">
			<span class='Icon'></span>
			<div class="trian"></div>
			<h1>销售返利单</h1>
		</div>
	</div>
	<div id="addBox"></div>
	<div class="toolBox">
		<div class="toolBox-button">
			<fool:tagOpt optCode="xsfldAdd">
				<a href="javascript:;" title="新增" id="addNew" class="btn-ora-add">新增</a>
			</fool:tagOpt>
		</div>
		<div class="toolBox-panel" style="margin-left: 5px;">
			<input name="codeOrVoucherCode" _class="textbox" id="search-code"
				data-options="{prompt:'单号或原始单号',width:160,height:30}" />
			<fool:tagOpt optCode="xsfldSearch">
				<a href="javascript:;" class="Inquiry btn-blue btn-s">查询</a>
			</fool:tagOpt>
		</div>
		<div id="grabble">
			<input type="text" id="bolting" value="请选择筛选条件" readonly="readonly" /><a
				href="javascript:;" class="button_a"><span class="button_span"></span></a>
		</div>
		<div class="input_div">
			<form action="" id="search-form">
				<!-- <input name="voucherCode" _class="textbox" data-options="{prompt:'原始单号',width:160,height:30}"/> -->
				<p>
					开始日期：<input name="startDay" id="startDay"
						data-options="{width:160,height:30,editable:false}" />
				</p>
				<p>
					结束日期：<input name="endDay" id="endDay"
						data-options="{width:160,height:30,editable:false}" />
				</p>
				<p style="margin: 5px 20px 5px 0px;">
					状态：<input type=checkbox value='0' name="recordStatus">未审核 <input
						type=checkbox value='1' name="recordStatus">已审核<input
						type=checkbox value='2' name="recordStatus">已作废
				</p>
				<p>
					客户：<input id="myCustomerName" name="customerName" class="textBox" /><input
						type="hidden" name="customerId" id="myCustomerId" />
				</p>
				<!-- <p>
					银行账户：<input id="mybankName" _class="mybankName-combogrid"
						class="textBox" /><input type="hidden" name="bankId" id="mybankId" />
				</p> -->
				<p>
					受款人：<input name="payeeName" id="mypayeeName" class="textBox"
						type="text" />
					<p>
						付款人：<input name="memberName" id="myOutMemberName"
							_class="memberBox"
							data-options="{width:160,height:30,editable:false}" />
						<!-- <input type="hidden" name="payeeId" id="myInMemberId"/></p>	 -->
						<input type="hidden" name="memberId" id="myOutMemberId" />
					</p>
					<!-- <input name="recordStatus" _class="recordStatus"/> -->
					<div class="button_clear">
						<fool:tagOpt optCode="xsfldSearch">
							<a href="javascript:;" class="btn-blue btn-s"
								id="search-form-btn" onclick="refreshData()">查询</a>
						</fool:tagOpt>
						<a href="javascript:;" class="btn-blue btn-s"
							onclick="cleanBoxInput('#search-form')">清空</a> <a
							href="javascript:;" id="clear-btndiv" class="btn-blue btn-s"
							style="vertical-align: middle;">关闭</a>
					</div>
			</form>
		</div>
	</div>
	<table id="dataTable"></table>
	<div id="pager"></div>

	<%@ include file="/WEB-INF/views/common/js.jsp"%>
	<script type="text/javascript"
		src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
	<script type="text/javascript"
		src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
	<script type="text/javascript"
		src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
	<script type="text/javascript"
		src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
	<script type="text/javascript">
		initManage('${billCode}', '${billCodeName}');
		$('#dataTable').jqGrid({
			datatype:function(postdata){
				$.ajax({
					url:"${ctx}/salesRebateRebBill/list",
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
			gridComplete:function(){//列表权限控制
				<fool:tagOpt optCode="xsfldAction2">//</fool:tagOpt>$('.btn-del').remove();
				<fool:tagOpt optCode="xsfldAction3">//</fool:tagOpt>$('.btn-copy').remove();
				<fool:tagOpt optCode="xsfldAction4">//</fool:tagOpt>$('.btn-approve').remove();
				<fool:tagOpt optCode="xsfldAction5">//</fool:tagOpt>$('.btn-cancel').remove();
				warehouseAll();
			},
			colModel:[
			  		{name:'fid',label:'fid',align:"center",hidden:true,width:100}, 
			  		{name:'amount',label:'amount',align:"center",hidden:true,width:100}, 
			  		{name:'code',label:'单号',align:"center",sortable:true,width:100<fool:tagOpt optCode="fkdAction1">,formatter:codeLinkNew</fool:tagOpt>},
			  		{name:'vouchercode',label:'原始单号',align:"center",sortable:true,width:100},
			  		{name:'customerName',label:'客户名称',align:"center",sortable:true,width:100},	  
			  		{name:'payeeName',label:'受款人',align:"center",sortable:true,width:100},
			  		{name:'memberName',label:'付款人',align:"center",sortable:true,width:100},
			  		{name:'billDate',label:'单据日期',align:"center",sortable:true,width:40,formatter:dateFormatAction},
			  		{name:'recordStatus',label:'状态',align:"center",sortable:true,width:40,formatter:recordStatusAction},
					<fool:tagOpt optCode="fkdAction">
			  		{name:'action',label:'操作',align:"center",width:100,formatter:actionFormatNew}
			  		</fool:tagOpt>
			      ]
		}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
		enterSearch("Inquiry");//回车搜索

		$("#mypayeeName").textbox({
			prompt : '受款人',
			width : 160,
			height : 32
		});
		if("${param.billId}"){
			viewRowNew("${param.billId}",1);
		}
	</script>
</body>
</html>
