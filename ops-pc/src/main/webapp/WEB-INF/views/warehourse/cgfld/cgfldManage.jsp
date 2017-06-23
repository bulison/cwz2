<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<c:set var="billCode" value="cgfld" scope="page"></c:set>
<c:set var="billCodeName" value="采购返利单" scope="page"></c:set>

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
#search-form .dhxDiv{
	margin:5px 5px 0px 0px;
}
#Inquiry{margin-left: 5px;}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; margin-right:19px; position:relative; border: 1px solid #ccc; background: #fff;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:23px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:4px;}
.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 19px; top:91px;z-index: 1;}
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
             <h1>采购返利单</h1>
       </div>             
    </div>
	<div id="addBox"></div>
	<div style="margin: 10px 0px 0px 0px;">	    
		<div style="margin-bottom:10px;">
			<fool:tagOpt optCode="cgfldAdd"> 
		    <a href="javascript:;" id="addNew" class="btn-ora-add" style="vertical-align: top;">新增</a>
			 </fool:tagOpt> 
			<fool:tagOpt optCode="cgfldSearch">
			<input name="codeOrVoucherCode" id="search-code" _class="textbox" data-options="{prompt:'单号或原始单号',width:160,height:30}"/><a class="Inquiry btn-blue btn-s" style="margin-left:5px;">查询</a>
			<div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
			</fool:tagOpt>
		</div>
		<div class="input_div">
		<form action="" id="search-form">
			<p>开始日期:<input name="startDay" id="startDay" data-options="{width:160,height:30,editable:false}"/></p>
			<p>结束日期:<input name="endDay" id="endDay" data-options="{width:160,height:30,editable:false}"/></p>
			<p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	<input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
			<p>供应商:<input id="mySupplierName" name="supplierName" class="textBox" data-options="{novalidate:true,width:160,height:30}"/>
			<input type="hidden" name="supplierId" id="mySupplierId" /></p>
			<!-- <p>收款人:<input id="myInMemberName" name="payeeName" class="textBox" data-options="{novalidate:true,width:160,height:30}"/>
			<input type="hidden" name="payeeId" id="myInMemberId" /></p>  -->
			<p>受款人:<input name="memberName" id="myOutMemberName" _class="memberBox"  data-options="{width:160,height:30,editable:false}"/>
			<input type="hidden" name="memberId" id="myOutMemberId"/></p>	
			<%-- <p>帐号:<input _imp="true" _class="bank-combogrid" id="relationName" name="relationName" value="${obj.relationName}" class="textBox" type="text"/></p>	 --%>
			<!-- <p>银行账户：<input id="mybankName" _class="mybankName-combogrid" class="textBox"/><input type="hidden" name="bankId" id="mybankId" /></p> -->
			<div class="button_clear">
			<fool:tagOpt optCode="cgfldSearch">
			<a href="javascript:;" class="btn-blue btn-s" id="search-form-btn" onclick="refreshData()" style="vertical-align:middle;">查询</a>
			</fool:tagOpt>
			<a href="javascript:;" class="btn-blue btn-s" onclick="cleanBoxInput('#search-form')" style="vertical-align:middle;">清空</a>
			<a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
			</div>
			</div>
		</form>
	</div>
	
	<table id="dataTable"></table>
	<div id="pager"></div>
<div id="my-window"></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
initManage('${billCode}','${billCodeName}');
$('#dataTable').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:"${ctx}/purchaseRebBill/list",
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
		<fool:tagOpt optCode="cgddAction2">//</fool:tagOpt>$('.btn-del').remove();
		<fool:tagOpt optCode="cgddAction3">//</fool:tagOpt>$('.btn-copy').remove();
		<fool:tagOpt optCode="cgddAction4">//</fool:tagOpt>$('.btn-approve').remove();
		<fool:tagOpt optCode="cgddAction5">//</fool:tagOpt>$('.btn-cancel').remove();
	  	  warehouseAll();
	},
	colModel:[
	  		{name:'fid',label:'fid',align:"center",hidden:true,width:100},
	  		{name:'amount',label:'amount',align:"center",hidden:true,width:100}, 
	  		{name:'code',label:'单号',align:"center",sortable:true,width:100<fool:tagOpt optCode="fkdAction1">,formatter:codeLinkNew</fool:tagOpt>},
	  		{name:"vouchercode",label:"原始单号",align:"center",width:100},
	  		{name:'supplierName',label:'供应商名称',align:"center",sortable:true,width:100},	  		
	  		{name:'memberName',label:'受款人',align:"center",sortable:true,width:100},
	  		{name:'billDate',label:'单据日期',align:"center",sortable:true,width:40,formatter:dateFormatAction},
	  		{name:'recordStatus',label:'状态',align:"center",sortable:true,width:40,formatter:recordStatusAction},
			<fool:tagOpt optCode="fkdAction">
	  		{name:'action',label:'操作',align:"center",width:100,formatter:actionFormatNew}
	  		</fool:tagOpt>
	      ]
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
enterSearch("Inquiry");//回车搜索
if("${param.billId}"){
	viewRowNew("${param.billId}");
}
</script>