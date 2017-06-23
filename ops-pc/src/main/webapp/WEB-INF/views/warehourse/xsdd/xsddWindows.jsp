<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>销售订单窗口</title>
</head>

<body>
<div class="window-search-box" style="margin:5px;">
<c:if test="${param.billCode eq 'xsdd cgsqd'}">
<input id="type">
</c:if>

<form id="winForm">

<input id="saleBill-start" class="easyui-datebox" data-options="prompt:'开始日期',width:100,height:30"/>
<input id="saleBill-end" class="easyui-datebox" data-options="prompt:'结束日期',width:100,height:30"/>
<input id="saleBill-code" class="easyui-textbox" data-options="prompt:'单号',width:100,height:30"/>
<%-- <c:if test="${param.billCode ne 'scll'}"> --%>
<input id="saleBill-myName" /><input id="saleBill-myId" type="hidden"/>
<%-- </c:if> --%>
<input id="saleBill-voucherCode" />
<input id="saleBill-dept"/>
<input id="saleBill-member" />
<input id="saleBill-goods"/>
<a href="javascript:;" class="btn-blue btn-s go-search-bill">筛选</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
<a href="javascript:;" class="btn-blue btn-s" id="clearbtn" >清空</a>
</form>
</div> 
<table id="saleBill-table" <%-- data-options="idField:'fid',fitColumns:true,pagination:true,url:'${ctx}/salebill/${param.billCode}/list?recordStatus=1&supplierId=${param.supId}&customerId=${param.cusId}'" --%>>
<div id="salepager"></div>
<%-- <thead>
<tr>
<th data-options="field:'fid',checkbox:true,width:10">fid</th>
<th data-options="field:'code',width:10,sortable:false">单号</th>
<th data-options="field:'billDate',width:10,sortable:false,formatter:dateFormatAction">单据日期</th>
<c:if test="${param.billCode ne 'scll'}">
<th data-options="field:'customerName',width:10,sortable:false">客户名称</th>
<th data-options="field:'customerId',hidden:true,width:10,sortable:false">客户ID</th>
<th data-options="field:'customerCode',width:10,hidden:true,sortable:false">客户编号</th>
<th data-options="field:'customerPhone',width:10,hidden:true,sortable:false">客户电话</th>
</c:if>
<th data-options="field:'deptName',width:10,sortable:false,hidden:true">部门名称</th>
<th data-options="field:'deptId',width:10,hidden:true,sortable:false">部门ID</th>
<th data-options="field:'inMemberName',width:10,sortable:false,hidden:true">业务员</th>
<th data-options="field:'inMemberId',width:10,hidden:true,sortable:false">业务员ID</th>
<th data-options="field:'endDate',width:10,sortable:false,hidden:true">计划完成时间</th>
<th data-options="field:'totalAmount',width:10,sortable:false,hidden:true">总金额</th>
<th data-options="field:'freeAmount',width:10,hidden:true,sortable:false,hidden:true">免单金额</th>
<th data-options="field:'voucherCode',width:10,sortable:false">凭证号</th>
</tr>
</thead> --%>
</table>
<div id="salewin"></div>
<script type="text/javascript">
var wsupplier="";
var wcustomer="";
var wdept="";
var wmember="";
var wgoods="";
var wcsv = "";
var pgChos = [];
var rowChos = [];
	$.ajax({
		url:"${ctx}/basedata/query?num="+Math.random(),
		async:false,
		data:{param:"Goods,Supplier,Customer,Organization,Member,CSV"},
		success:function(data){
		    	wsupplier=formatData(data.Supplier,"fid");
		    	wcustomer=formatData(data.Customer,"fid");
		    	wdept=formatTree(data.Organization[0].children,"text","id");
		    	wmember=formatData(data.Member,"fid");
		    	wgoods=formatData(data.Goods,"fid");
		    	wcsv=formatData(data.CSV,"fid");
	    }
		});
$('#saleBill-voucherCode').textbox({
	width:100,
	height:30,
	prompt:'原始单号'
});
$('#saleBill-dept').fool('dhxCombo',{
	width:120,
	height:30,
	prompt:'部门',
	editable:false,
	focusShow:true,
	data:wdept
});
$('#saleBill-dept').next().hide();
$('#saleBill-member').fool('dhxComboGrid',{
	width:120,
	height:30,
	prompt:'申请人',
	data:wmember,
	focusShow:true,
    onlySelect:true,
	filterUrl:getRootPath()+'/member/vagueSearch',
	setTemplate:{
		input:"#username#",
		columns:[
					{option:'#userCode#',header:'编号',width:100},
					{option:'#jobNumber#',header:'工号',width:100},
					{option:'#username#',header:'名称',width:100},
					{option:'#phoneOne#',header:'电话',width:100},
					{option:'#deptName#',header:'部门',width:100},
					{option:'#position#',header:'职位',width:100},
				],
  	},
});
$('#saleBill-member').next().hide();

/* <c:if test="${param.billCode ne 'scll'}">
$('#saleBill-customerName').textbox({
	width:100,
	height:30,
	prompt:'客户名称'
});

$('#saleBill-customerName').textbox('textbox').click(function(){
	$('#salewin').window({
		"title":'选择客户名称',
		href:'${ctx}/customer/window?okCallBack=saleCustomerName&onDblClick=saleCustomerNameDBC&singleSelect=true',
		width:500,
		height:300,
		collapsible:false,
		minimizable:false,
		maximizable:false
	});
});
function saleCustomerName(data){
	$('#saleBill-customerId').val(data[0].fid);
	$('#saleBill-customerName').textbox('setValue',data[0].name);
	$('#salewin').window('close').window('clear');
}
function saleCustomerNameDBC(data){
	$('#saleBill-customerId').val(data.fid);
	$('#saleBill-customerName').textbox('setValue',data.name);
	$('#salewin').window('close').window('clear');
}
</c:if> */

var billtype=[];
var url='${ctx}/salebill/${param.billCode}/list?recordStatus=1&supplierId=${param.supId}&customerId=${param.cusId}';
if("${param.billCode}"=="xsdd"){
	billtype=[{value:"xsdd",text:"销售订单"}];
}else if("${param.billCode}"=="xsdd cgsqd"){
	url='${ctx}/salebill/xsdd/list?recordStatus=1&supplierId=${param.supId}&customerId=${param.cusId}';
	billtype=[{value:"xsdd",text:"销售订单"},{value:"cgsqd",text:"采购申请单"}];
}else if("${param.billCode}"=="fkd"||"${param.billCode}"=="skd"){
	if("${param.billCode}"=="fkd"){
		url='${ctx}/payBill/list?recordStatus=1';
	}else{
		url='${ctx}/receiveBill/list?recordStatus=1';
	}
}else if("${param.billCode}"=="fyd"){
	url='${ctx}/costBill/list?recordStatus=1';
}
function dateFormat(value){
	var date = getDateStr(value);
	return date;
}
$(function(){
	$('#saleBill-table').jqGrid({		
		<c:if test='${!param.singleSelect}'>multiselect:true,</c:if>
		datatype:function(postdata){
			$.ajax({
				url:url,
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#saleBill-table")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		forceFit:true,
		pager:'#salepager',
		rowList:[ 10, 20, 30 ],
		viewrecords:true,
		rowNum:10,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		}, 
		width:$("#pop-win").width()-10,
		height:290,
		colModel:[
                   {name:'fid',label:'fid',align:"center",hidden:true,width:100},
                   {name:'code',label:'单号',align:"center",width:100},
                   {name:'billDate',label:'单据日期',align:"center",width:100,formatter:dateFormat},
                   {name:'customerName',label:'客户名称',align:"center",width:100},
                   {name:'supplierName',label:'供应商名称',align:"center",hidden:true,width:100},
                   {name:'csvName',label:'收支单位名称',hidden:true,width:100},
                   {name:'csvId',label:'收支单位ID',hidden:true,width:100},
                   {name:'supplierId',label:'供应商ID',hidden:true,width:100},
                   {name:'supplierCode',label:'供应商编号',hidden:true,width:100},
                   {name:'customerCode',label:'客户编号',hidden:true,width:100},
                   {name:'customerId',label:'客户ID',hidden:true,width:100},
                   {name:'supplierPhone',label:'供应商电话',hidden:true,width:100},
                   {name:'customerPhone',label:'客户电话',hidden:true,width:100},
                   {name:'voucherCode',label:'原始单号',align:"center",width:100},
                   
                   {name:'transportNo',label:'transportNo',align:"center",hidden:true,width:100},
                   {name:'deliveryPlaceName',label:'deliveryPlaceName',align:"center",hidden:true,width:100},
                   {name:'deliveryPlaceId',label:'deliveryPlaceId',align:"center",hidden:true,width:100},
                   {name:'receiptPlaceName',label:'receiptPlaceName',align:"center",hidden:true,width:100},
                   {name:'receiptPlaceId',label:'receiptPlaceId',align:"center",hidden:true,width:100},
                   {name:'transportTypeName',label:'transportTypeName',align:"center",hidden:true,width:100},
                   {name:'transportTypeId',label:'transportTypeId',align:"center",hidden:true,width:100},
                   {name:'shipmentTypeName',label:'shipmentTypeName',align:"center",hidden:true,width:100},
                   {name:'shipmentTypeId',label:'shipmentTypeId',align:"center",hidden:true,width:100},
                   {name:'carNo',label:'carNo',align:"center",hidden:true,width:100},
                   {name:'driverName',label:'driverName',align:"center",hidden:true,width:100},
                   {name:'driverPhone',label:'driverPhone',align:"center",hidden:true,width:100},
                   
                   {name:'deptId',label:'deptId',align:"center",hidden:true,width:100},
                   {name:'inMemberId',label:'inMemberId',align:"center",hidden:true,width:100},
                   {name:'deptName',align:"center",label:'申请部门',hidden:true,width:100},
			       {name:'inMemberName',align:"center",label:'申请人',hidden:true,width:100},
		          ],
		ondblClickRow:function(rowid,iRow,iCol,e){
			var row = $('#saleBill-table').jqGrid("getRowData",rowid);
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		},
		onSelectRow:function(rowid,status){
			<c:if test='${!param.singleSelect}'>
			var rowids = $('#saleBill-table').jqGrid('getGridParam', 'selarrrow');
			var num = $('#saleBill-table').jqGrid('getGridParam').page;
			pgChos[num] = rowids;
			var rows = [];
			for(var i=0;i<rowids.length;i++){
				var myrow = $('#saleBill-table').jqGrid("getRowData",rowids[i]);
				rows.push(myrow);
			}
			rowChos[num] = rows;
			</c:if>
			var row = $('#saleBill-table').jqGrid("getRowData");
			if($("#type").length<=0){
				return false;
			}
			if(status){
				$("#type").next()[0].comboObj.disable();
			}else{
				var bool = true;
				for(var i = 1 ; i <= row.length; i++){
					bool = bool && !$('#jqg_saleBill-table_'+i)[0].checked;
				}
				if(bool){
					$("#type").next()[0].comboObj.enable();
				}
			}
			
		},
		onSelectAll:function(aRowids,status){
			if($("#type").length>0){
				if(!status && rowChos.length){
					$("#type").next()[0].comboObj.enable();
				}else{
					$("#type").next()[0].comboObj.disable();
				}
			}
			<c:if test='${!param.singleSelect}'>
				var num = $('#saleBill-table').jqGrid('getGridParam').page;
				if(status){
					var rowids = $('#saleBill-table').jqGrid('getDataIDs')
					pgChos[num] = rowids;
					var rows = $('#saleBill-table').jqGrid("getRowData");
					/* for(var i=0;i<rowids.length;i++){
						var row = $('#goods-table').jqGrid("getRowData",rowids[i]);
						rows.push(row);
					} */
					rowChos[num] = rows;
				}else{
					pgChos[num] = [];
					rowChos[num] = [];
				}
			</c:if>
		},
		gridComplete:function(){
			<c:if test='${!param.singleSelect}'>
			var num = $('#saleBill-table').jqGrid('getGridParam').page;
			if(pgChos[num]){
				var rowids = pgChos[num];
				for(var i = 0; i < rowids.length; i++){
					$('#saleBill-table').jqGrid('setSelection',rowids[i],false);
				}
			}
			</c:if>
		}
	});
	$("#type").fool("dhxCombo",{
		width:160,
		height:30,
		data:billtype,
		prompt:"订单类型",
		editable:false,
		clearOpt:false,
		focusShow:true,
		value:"xsdd",
		onChange:function(value,text){
			$('#clearbtn').click();
			if(value=="xsdd"){
				$('#saleBill-dept').next().hide();
				$('#saleBill-member').next().hide();
				$('#saleBill-myName').next().show();
				$('#saleBill-voucherCode').next().show();
				$('#saleBill-table').jqGrid("hideCol",["deptName","inMemberName"]);
				$('#saleBill-table').jqGrid("showCol",["customerName","voucherCode"]);
				$('#saleBill-table').jqGrid("setGridParam",{
					datatype:function(postdata){
						$.ajax({
							url:'${ctx}/salebill/xsdd/list?recordStatus=1',
							data:postdata,
					        dataType:"json",
					        complete: function(data,stat){
					        	if(stat=="success") {
					        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
					        		$("#saleBill-table")[0].addJSONData(data.responseJSON);
					        	}
					        }
						});
					}}).trigger("reloadGrid");
				pgChos = [];
				rowChos = [];
				$('#saleBill-table').jqGrid("setGridWidth",$("#pop-win").width()-10); //让表格列变化后再一次自适应宽度
				//$('#saleBill-myName').combogrid('textbox').parent().show();
			}else if(value=="cgsqd"){
				$('#saleBill-dept').next().show();
				$('#saleBill-member').next().show();
				$('#saleBill-myName').next().hide();
				$('#saleBill-voucherCode').next().hide();
				$('#saleBill-table').jqGrid("hideCol",["supplierName","customerName","csvName","voucherCode"]);
				$('#saleBill-table').jqGrid("showCol",["deptName","inMemberName"]);
				$('#saleBill-table').jqGrid("setGridParam",{
					datatype:function(postdata){
						$.ajax({
							url:'${ctx}/salebill/cgsqd/list?recordStatus=1',
							data:postdata,
					        dataType:"json",
					        complete: function(data,stat){
					        	if(stat=="success") {
					        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
					        		$("#saleBill-table")[0].addJSONData(data.responseJSON);
					        	}
					        }
						});
					}}).trigger("reloadGrid");
				pgChos = [];
				rowChos = [];
				$('#saleBill-table').jqGrid("setGridWidth",$("#pop-win").width()-10); //让表格列变化后再一次自适应宽度
				//$('#saleBill-myName').combogrid('textbox').parent().hide();
			}
			
		}
	});
	//setPager($('#saleBill-table'));
	
	$("#clearbtn").click(function(){
		$(".window-search-box form").form('clear');
		$('#saleBill-member').next()[0].comboObj.setComboText("");
		$('#saleBill-dept').next()[0].comboObj.setComboText("");
		$('#saleBill-myName').next()[0].comboObj.setComboText("");
		$('#saleBill-goods').next()[0].comboObj.setComboText("");
	});
	
	$(".select-ok").click(function(){
		var rowids = $('#saleBill-table').jqGrid('getGridParam', 'selrow');
		var rows = [];
		if(rowids!=null){
			for(var i=0;i<rowids.length;i++){
				var row = $('#saleBill-table').jqGrid("getRowData",rowids[i]);
				rows.push(row);
			}
		}
		<c:if test='${!param.singleSelect}'>
		rows = [];
		for(var i  in rowChos){
			rows = rows.concat(rowChos[i]);
		}
		</c:if>
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
	
var searchValue = 'customer',myname = '客户名称',_data = wcustomer;
if(_billCode=="cgrk"||_billCode=="cgth"||_billCode=="cgfp"||"${param.billCode}"=="fkd"){
	searchValue = 'supplier';
    myname = '供应商名称';
	_data = wsupplier;
	$('#saleBill-table').jqGrid("hideCol",["customerName","csvName"]);
	$('#saleBill-table').jqGrid("showCol",["supplierName"]);
	$('#saleBill-table').jqGrid("setGridWidth",$("#pop-win").width()-10); //让表格列变化后再一次自适应宽度
}else if("${param.billCode}"=="fyd"){
	searchValue = 'csv';
	myname = '收支单位名称';
	_data = wcsv;
	$('#saleBill-table').jqGrid("hideCol",["customerName","supplierName"]);
	$('#saleBill-table').jqGrid("showCol",["csvName"]);
	$('#saleBill-table').jqGrid("setGridWidth",$("#pop-win").width()-10); //让表格列变化后再一次自适应宽度
}else if(_billCode == "shd"){
    searchValue = "supplier";
    myname = '运输公司';
    _data = wsupplier;
    $('#saleBill-voucherCode').next().hide();
    $('#saleBill-table').jqGrid("hideCol",["voucherCode","customerName"]);
    $('#saleBill-table').jqGrid("setLabel","supplierName","运输公司");
    $('#saleBill-table').jqGrid("showCol","supplierName");
    $('#saleBill-table').jqGrid("setGridWidth",$("#pop-win").width()-10);
}

	$('#saleBill-myName').fool('dhxComboGrid',{
		prompt:myname,
		width:120,
		height:30,
		data:_data,
		focusShow:true,
	    onlySelect:true,
		setTemplate:{
			input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					]
	  	},
		filterUrl:getRootPath()+'/'+searchValue+'/vagueSearch?searchSize=8',
		onChange:function(value,text){
			$("#saleBill-myId").val(value);
		}
	});
	$('#saleBill-goods').fool('dhxComboGrid',{
		url:getRootPath()+'/goods/vagueSearch?searchSize=8',
		width:120,
		height:30,
		prompt:"货品",
		focusShow:true,
		onlySelect:true,
		filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=8',
		setTemplate:{
			input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
						{option:'#barCode#',header:'条码',width:100},
						{option:'#spec#',header:'规格',width:100},
						{option:'#unitName#',header:'单位',width:100},
					],
		},
		data:wgoods,
	});
	if("${param.billCode}" != "scjhd"){
		$('#saleBill-goods').next().hide();
	}else{
		$('#saleBill-myName').next().hide();
		$('#saleBill-voucherCode').next().hide();
	}
	/* $('#saleBill-table').jqGrid("setGridParam",{
		colModel:[
                  {name:'code',label:'单号',align:"center",width:100},
                  {name:'billDate',label:'单据日期',align:"center",width:100,formatter:dateFormat},
                  {name:searchValue+'Name',align:"center",label:myname,width:100},
                  {name:searchValue+'Id',align:"center",label:myname+'Id',hidden:true,width:100},
                  {name:'voucherCode',align:"center",label:'原始单号',width:100},
		          ],
	}); */
	
	$(".go-search-bill").click(function(){
		//${ctx}/salebill/${param.billCode}/list?recordStatus=1&supplierId=${param.supId}&customerId=${param.cusId}		
		var saleBillStart=$("#saleBill-start").datebox('getValue');
		var saleBillEnd=$("#saleBill-end").datebox('getValue');
		var saleBillCode=$("#saleBill-code").textbox('getValue');
		var saleBillDept=$("#saleBill-dept").next()[0].comboObj.getSelectedValue();
		var saleBillMember=$("#saleBill-member").next()[0].comboObj.getSelectedValue();
		var saleBillGoods=$("#saleBill-goods").next()[0].comboObj.getSelectedValue();
		var myId = $('#saleBill-myId').val();
		myId==''?$('#saleBill-myName').next()[0].comboObj.setComboText(""):null;
		var voucherCode = $('#saleBill-voucherCode').textbox('getValue');
		var options = {"startDay":saleBillStart,"endDay":saleBillEnd,"code":saleBillCode,"voucherCode":voucherCode,"deptId":saleBillDept,"inMemberId":saleBillMember,"goodsId":saleBillGoods};
		var myjson = {"customerId":myId};
		if(_billCode=="cgrk"||_billCode=="cgth"||_billCode=="cgfp"||_billCode=="shd"){
			myjson = {"supplierId":myId};
		}
		pgChos = [];
		rowChos = [];
		options = $.extend(options,myjson);
		if("${param.billCode}"=="xsdd cgsqd"){
			var type=$("#type").next()[0].comboObj.getSelectedValue();
			$('#saleBill-table').jqGrid("setGridParam",{
				datatype:function(postdata){
					$.ajax({
						url:"${ctx}/salebill/"+type+"/list?recordStatus=1",
						data:postdata,
				        dataType:"json",
				        complete: function(data,stat){
				        	if(stat=="success") {
				        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
				        		$("#saleBill-table")[0].addJSONData(data.responseJSON);
				        	}
				        }
					});
				}
			});
		}else{
			$('#saleBill-table').jqGrid("setGridParam",{
				datatype:function(postdata){
					$.ajax({
						url:"${ctx}/salebill/${param.billCode}/list?recordStatus=1",
						data:postdata,
				        dataType:"json",
				        complete: function(data,stat){
				        	if(stat=="success") {
				        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
				        		$("#saleBill-table")[0].addJSONData(data.responseJSON);
				        	}
				        }
					});
				}
			});
		}
		$('#saleBill-table').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	});
});
$('#winForm').bind("keydown",function(e){
	if(e.keyCode == 13){
		$("#winForm .go-search-bill").click();
	}
});
</script>

</body>
</html>
