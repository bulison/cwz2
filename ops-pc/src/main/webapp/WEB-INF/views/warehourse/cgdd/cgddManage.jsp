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
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
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
#search-form{
display: inline-block; width: 98%;
}
#Inquiry{margin-left: 5px;}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 19px;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:23px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:4px;}
.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:93px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px; width:235px}
.button_clear{ border-top: 1px solid #D5DBEA;padding-top:8px; text-align: right;}
#search-code{ width:160px; height: 25px; border: 1px solid #ccc;}
.nav{margin-bottom: 10px;}
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
                   <h1>采购订单</h1>
                </div>             
             </div>
    <div id="addBox"></div> 
	<div style="margin: 10px 0px 0px 0px;">			
			<%-- <a href="${ctx}/warehouse/${billCode}/export" class="btn-ora-export">导出</a> --%>
		<div class="nav">
			<fool:tagOpt optCode="cgddAdd">
			<a href="javascript:;" id="addNew" class="btn-ora-add" style="vertical-align: top;">新增</a>
		    </fool:tagOpt>
			<input name="codeOrVoucherCode" _class="textbox" id="search-code" placeholder='单号或原始单号' /><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
			<div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" /><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
		</div>			  
		<div class="input_div">
		  <form action="" id="search-form">	
			<p>开始日期:<input name="startDay" id="startDay"  data-options="{editable:false}"/></p>
			<p>结束日期:<input name="endDay" id="endDay" data-options="{editable:false}"/></p>
			<p>部门：<input id="search-deptName" name="deptId" _class="deptComBoxTree"/></p>
			<p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	        <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>	
			<!-- <input name="voucherCode" _class='textbox' data-options="{prompt:'原始单号',width:160,height:30}"/> -->
			<p>供应商:<input id="mySupplierName" name="supplierName" class="textBox" data-options="{novalidate:true,width:160,height:30}"/>
			<input type="hidden" name="supplierId" id="mySupplierId" /></p>
			<p>采购员:<input id="myInMemberName" name="inMemberName" class="textBox" data-options="{novalidate:true,width:160,height:30}"/>
			<input type="hidden" name="inMemberId" id="myInMemberId" /></p>					
		<!-- 	<input name="recordStatus" _class="recordStatus"/> -->
			<p>货品:<input id="myGoodName" name="myGoodName" class="textBox" data-options="{novalidate:true,width:160,height:30}"/>
			<input id="myGoodsId" name="goodsId" type="hidden"/></p>
			<div class="button_clear">
			<fool:tagOpt optCode="cgddSearch">
			   <a href="javascript:;" class="btn-blue btn-s search-form" id="search-form-btn" onclick="refreshData()" style="vertical-align:middle;">查询</a>
			</fool:tagOpt>
			<a href="javascript:;" class="btn-blue btn-s" onclick="cleanBoxInput('#search-form')" style="vertical-align:middle;">清空</a>
			<a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
			</div>			
		  </form>
		</div>
	</div>
	
	<table id="dataTable">
	</table>
	<div id="pager"></div>

<div id="my-window"></div>
<a style="display:none;" href="javaScript:printBillDetail('402881ed50a6c0710150a6e2d8d60005','cgdd')">打印</a>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
initManage('${billCode}','${billCodeName}');
enterSearch("Inquiry");//回车搜索
var texts = [
             {title:'单号',key:'code'},
             {title:'原始单号',key:'voucherCode'},
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
$('#dataTable').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/warehouse/cgdd/list',
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
			{name:'billDate',align:"center",label:'单据日期',width:80,sortable:true,formatter:dateFormatAction},
			{name:'relationName',align:"center",label:'关联单据',sortable:true,width:120},
			{name:'supplierName',align:"center",label:'供应商',sortable:true,width:100},
	  		{name:'inMemberName',align:"center",label:'采购员',sortable:true,width:100},
	  		{name:'endDate',align:"center",label:'计划完成日期',sortable:true,width:100,formatter:dateFormatAction}, 
	  		{name:'totalAmount',align:"center",label:'合计金额',sortable:true,width:100}, 
	  		{name:'creatorName',align:"center",label:'制单人',sortable:true,width:100},
	  		{name:'recordStatus',align:"center",label:'状态',width:40,sortable:true,formatter:recordStatusAction},
			<fool:tagOpt optCode="qckcAction">
	  		{name:'action',align:"center",label:'操作',formatter:actionFormatNew}
	  		</fool:tagOpt>
	      ],
	      gridComplete:function(){
	    	  	<fool:tagOpt optCode="cgddAction2">//</fool:tagOpt>$('.btn-del').remove();
	    		<fool:tagOpt optCode="cgddAction3">//</fool:tagOpt>$('.btn-copy').remove();
	    		<fool:tagOpt optCode="cgddAction4">//</fool:tagOpt>$('.btn-approve').remove();
	    		<fool:tagOpt optCode="cgddAction5">//</fool:tagOpt>$('.btn-cancel').remove();
	    	  	warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
var mybillids = '${param.billIds}';
var idsTips = false;
if(mybillids != ''){
	idsTips=true;
	var wsrc='${ctx}/warehouse/cgdd/generats?billIds='+mybillids+'&billCode=cgdd';
	$('#addBox').window({
		top:10+$(window).scrollTop(),
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
			//$('html').css('overflow','hidden');
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			if($("#checkBox").length>0 && $('#checkBox').html()){
				$("#checkBox").window("destroy");
			}
			if($("#pop-win").length>0 && $("#pop-win").html()){
				$("#pop-win").window("destroy");
			}
			if($("#import-win").length>0 && $("#import-win").html()){
				$('#import-win').window("destroy");
			}
			//$('html').css('overflow',"");
		},
		onLoad:function(){
		if(mybillids != ''){
			$.ajax({
				url:getRootPath()+"/warehouse/xsdd/mergeBillDetails?merge=1",
				type:"post",
				async:false,
				data:{billIds:mybillids},
				success:function(data){
					$("#goodsList")[0].addJSONData(data);
				    var rows=$("#goodsList").jqGrid('getRowData');
				    newIndex = rows.length;
				    for(var i=0;i<rows.length;i++){
				    	editNew(i+1);
				    	getTableEditor(i+1,'_isNew').val(true);
				    }
				}
			});
			mybillids='';
		}
	}});
	$('#addBox').window("setTitle","新增采购订单");
	$('#addBox').window("open");
	$('#addBox').window("refresh",wsrc);
}
if("${param.billId}"){
	viewRowNew("${param.billId}");
}
</script>