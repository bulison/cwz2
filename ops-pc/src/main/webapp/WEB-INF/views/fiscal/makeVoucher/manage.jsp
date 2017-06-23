<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>单据生成凭证</title>

<style>
#s p font,#search-form p font{
	width:auto;
}

.form1{
	padding:0px;
}
.form1 p{
	margin:10px 0px 0px;
}
#supplier,#customer,#bank,#deptId{
	margin:0px;
}
.form1 h4,h5{
	margin:5px 0px;
}
.form1 .easyui-validatebox{
	width:158px;
	height:28px;
}
.voucherMake{
	margin:-10px 0 5px 0;
}
#dhxDiv_customerName{
    margin:0 5px 10px 0 !important;
}
#dhxDiv_supplierName{
    margin:0 5px 10px 0 !important;
}
#dhxDiv_deptName{
    margin:0 5px 10px 0 !important;
}
.dhxDiv{
    margin:0 !important;
}
#modelPager_left{
    width:25px
}
#modelPager_center{
    width:0px !important;
}
</style>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
</head>
<body>
	<div class="titleCustom">
	  <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>单据生成凭证</h1>
      </div>             
    </div>
	<div class="form1">
		<form id="search-form">
			<h4 style="display: inline-block;margin-left:20px">模板</h4>
			<p><font><em style="color:red;">*</em>模板选择：</font><input id="modelName" class="textBox" /><input id="modelId" type="hidden" /> </p>
			<fool:tagOpt optCode="mvoucherConfig"><p><a id="config" href="javascript:;" class="btn-blue" style="margin-left:10px;">模板设置</a></p></fool:tagOpt>
			<p><input type="button" onclick="location.reload();" class="btn-blue" style="margin-left:10px;" value="刷新" /></p>	<br>
		    <div class="toolBox">
		      <div class="toolBox-pane" id="s">
			    <h4 style="font-size:16px;display: inline-block;margin:0 0 10px 20px;">单据筛选条件</h4>
			    <em style="color:red;font-size:12px;">*</em>
			    <select id="billType" name="billType" style="width:162px;height:32px;margin:0 5px 10px 0;" readonly="readonly" onchange="selectDo()">
        	      <option value="11">采购入库单</option>
        	      <option value="12">采购退货单</option>
        	      <option value="20">盘点单</option>
        	      <option value="21">调仓单</option>
        	      <option value="22">报损单</option>
        	      <option value="30">生产领料单</option>
        	      <option value="31">成品入库单</option>
        	      <option value="32">生产退料单</option>
        	      <option value="33">成品退库单</option>
        	      <option value="41">销售出货单</option>
        	      <option value="42">销售退货单</option>
        	      <option value="51">收款单</option>
        	      <option value="52">付款单</option>
        	      <option value="53">费用单</option>
        	      <option value="15">采购发票</option>
        	      <option value="44">销售发票</option>
        	      <option value="55">采购返利单</option>
        	      <option value="56">销售返利单</option>
    		    </select>
				<input name="startDay" id="startDay"/>
				<input name="endDay" id="endDay"/>
				<input name="code" _class="textbox" data-options="{prompt:'单据编号',width:160,height:30}"/>
				<span id="supplier" style="display:none;"><input id="supplierName" class="textBox" name="supplierId" _class="supplier-combogrid" type="text" /></span>
				<span id="customer" style="display:none;"><input id="customerName" name="customerId" class="textBox" _class="customer-combogrid"  type="text" /></span>
				<span id="bank" style="display:none;"><input id="bankId" name="bankId" class="textBox" /></span>
				<span id="deptId"><input id="deptName" name="deptName"  class="textBox"/><input id="deptId_s" name="deptId" type="hidden"  /></span>
				<fool:tagOpt optCode="mvoucherSearch"><a class="btn-blue btn-s go-search1" id="search-btn" onclick="refreshData()">查询</a></fool:tagOpt>
				<a class="btn-blue btn-s" onclick="cleanBoxInput('#s')">清空</a>
		      </div>
		    </div> 
		    <div class="voucherMake" style="margin-bottom:5px;">
		      <h4 style="display: inline-block;margin-left:20px">生成凭证</h4>
			  <p><font><em style="color:red;">*</em>凭证日期：</font><input id="voucherDate" name="voucherDate" class=" textbox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true"/></p>
			  <fool:tagOpt optCode="mvoucherMake"><p><input type="button" id="make" class="btn-yellow2 btn-xs" style="margin:0 10px;" value="生成凭证" /></p></fool:tagOpt>
			  <input name="voucherWordNumber" id="voucherCode" _class='textbox' data-options="{prompt:'凭证号',width:160,height:30}"/>
			  <input name="voucherTag" id="voucherTag" class="textBox"/>
			  <fool:tagOpt optCode="mvoucherSearch"><a class="btn-blue btn-s go-search1" id="search-btn2" onclick="refreshData()">查询</a></fool:tagOpt>
			  <a class="btn-blue btn-s" style="margin-left:5px;" onclick="cleanBoxInput('.voucherMake')">清空</a>
		    </div>
	        <table id="dataTable"></table>
	        <div id="dataPager"></div>
	    </form>
	</div>
	<h5>单据详情</h5>
	<table id="goodList" ></table>	
    <div id="addBox"></div>		
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript">
    var cbillType = [];
    cbillType[11]="采购入库单";
    cbillType[12]="采购退货单";
    cbillType[15]="采购发票";
    cbillType[20]="盘点单";
    cbillType[21]="调仓单";
    cbillType[22]="报损单";
    cbillType[30]="生产领料单";
    cbillType[31]="成品入库单";
    cbillType[32]="生产退料单";
    cbillType[33]="成品退库单";
    cbillType[41]="销售出货单";
    cbillType[42]="销售退货单";
    cbillType[44]="销售发票";
    cbillType[51]="收款单";
    cbillType[52]="付款单";
    cbillType[53]="费用单";
    cbillType[55]="采购返利单";
    cbillType[56]="销售返利单";
	$.post('${ctx}/voucher/getDefaultMessage',function(data){
		$('#voucherDate').datebox('setValue',data.voucherDate);
	});

    var $data = [];
	var rowNum = 10;

	$("#search-form").find("input[_class]").each(function(i,n){inputInit($(this));});

	comboBox($("#bankId"),"${ctx}/bankController/comboboxData",true,function(){//银行下拉框
		$("#bankId").combobox("setValue","${obj.bankId}");
	});
	var deptCombo=$('#deptName').fool("dhxCombo",{
		prompt:'部门',
		editable:false,
		focusShow:true,
		clearOpt:false,
		width:155,
		height:34,
		data:sdeptData,
		setTemplate:{
				input:"#text#",
			  	option:"#text#"
		},
		toolsBar:{
			name:"部门",
			refresh:true
		},
		onLoadSuccess:function(node,data){
			($('#deptName').next())[0].comboObj.setComboValue("${obj.deptId}");		 
			$("#deptId_s").val('${obj.deptId}');
		},
		onSelectionChange:function(){
			$('#deptId_s').val(($("#deptName").next())[0].comboObj.getSelectedValue());
		}
	});
	$("#voucherDate").datebox({
		width:155,
		height:30,
		editable:false,
	});
	//控件初始化
	/* $('#voucherTag').combobox({
		width:155,
		height:30,
		editable:false,
		data:[{
			value:"0",
			text:'未生成'
		},{
			value:"1",
			text:'已生成'
		}],
		prompt:'是否已生成',
		onSelect:function(record){
			if(record.value=='0'){
				$('#voucherCode').textbox('setValue','');
				$('#voucherCode').textbox('textbox').parent().hide();
			}else{
				$('#voucherCode').textbox('textbox').parent().show();
			}
		}
	}); */
	var voucherTagCombo=$('#voucherTag').fool("dhxCombo",{
		prompt:'是否已生成',
		editable:false,
		focusShow:true,
		clearOpt:false,
		width:160,
		height:34,
		data:[{
			value:"0",
			text:'未生成'
		},{
			value:"1",
			text:'已生成'
		}],
		setTemplate:{
			  input:"#name#",
			  option:"#name#"
		},
		onSelectionChange:function(){
			var value=voucherTagCombo.getSelectedText().value;
			if(value=='0'){
				$('#voucherCode').textbox('setValue','');
				$('#voucherCode').textbox('textbox').parent().hide();
			}else{
				$('#voucherCode').textbox('textbox').parent().show();
			}
		}
	});
	
	function comboBox(obj,url,required,onLSFn){
		obj.combobox({
			valueField:"fid",
			textField:"name",
			required:false,
			prompt:'银行账户',
			missingMessage:'该项不能为空！',
			novalidate:true,
			url:url,
			width:155,
			height:30,
			editable:false,
			onLoadSuccess:onLSFn
		});
	}
	$('#startDay').datebox({
		prompt:'开始日期',
		editable:false,
		width:155,
		height:30,
		required:false,
		novalidate:true
	});
	$('#endDay').datebox({
		prompt:'结束日期',
		editable:false,
		width:155,
		height:30,
		required:false,
		novalidate:true
	});
	$('#modelName').combogrid({
		url:"${ctx}/billsubject/query?page=1&rows=65535",
		editable:false,
		width:155,
		height:30,
		panelWidth:345,
		required:true,
		novalidate:true,
		idField:'fid',
		textField:'templateName',
		columns:[[
		          {field:"fid",title:'fid',hidden:true},
		          {field:"billType",title:"单据类型",width:100,formatter:function(value,row,index){
		        	  return value!=''?cbillType[value]:'';
		          }},
		          {field:"templateName",title:"模板名称",width:100},
		          {field:"templateCode",title:"模板编号",width:100}
		          ]],
	    onSelect:function(index,row){
	    	$('#modelId').val(row.fid);
	    }
	})
	/* $('#modelName').fool('dhxComboGrid',{
		required:true,
		novalidate:true,
		editable:false,
		width:160,
		height:32,
		focusShow:true,
	  	data:getComboData("${ctx}/billsubject/query?page=1&rows=65535","grid"),
	  	setTemplate:{
	  		input:"#templateName#",
			columns:[
						{option:'#billType#',header:'单据类型',width:100},
						{option:'#templateName#',header:'模板名称',width:100},
						{option:'#templateCode#',header:'模板编号',width:180},
					],
	  	},
	  	onSelectionChange:function(){
	  		$('#modelId').val(($('#modelName').next())[0].comboObj.getSelectedValue());
	  	}
	}); */
	
	//凭证字号超链接
	function clickDo(id){
		var url = "/voucher/manage?fid="+id;
		parent.kk(url,"填制凭证");
	}
	//单据类型的表格设置
	var option11=[ 
//                  {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this)'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
	              {name : 'code',label : '单号',align:'center',width:"100px"},
	              {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
	            	  return getDateStr(cellvalue);
	              }},
			  	  {name : 'contactUnitName',label : '供应商名称',align:'center',width:"100px"},
			  	  {name : 'deptName',label : '部门',align:'center',width:"100px"},
			  	  {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
			  	  {name : 'memberName',label : '采购员',align:'center',width:"100px"},
			  	  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
			  	  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
			  	  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
			  		  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
			  			  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
			  		  }else{
			  			  return '';
			  		  }
			  	  }},
			  	  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
	              ];
	var option12=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
		          {name : 'code',label : '单号',align:'center',width:"100px"},
		          {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
		        	  return getDateStr(cellvalue);
		          }},
		          {name : 'contactUnitName',label : '供应商名称',align:'center',width:"100px"},
				  {name : 'deptName',label : '部门',align:'center',width:"100px"},
				  {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
				  {name : 'memberName',label : '采购员',align:'center',width:"100px"},
				  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
						  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
					  }else{
						  return '';
					  }
				  }},
				  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
		          ];
	var option20=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
	              {name : 'fid',label : 'fid',hidden:true},
		          {name : 'code',label : '单号',align:'center',width:"100px"},
		          {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
		        	  return getDateStr(cellvalue);
		          }},
		          {name : 'deptName',label : '部门',align:'center',width:"100px"},
				  {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
				  {name : 'memberName',label : '经手人',align:'center',width:"100px"},
				  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
						  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
					  }else{
						  return '';
					  }
				  }},
				  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
				  ];
	var option21=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
	              {name : 'fid',label : 'fid',hidden:true},
		          {name : 'code',label : '单号',align:'center',width:"100px"},
		          {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
		        	  return getDateStr(cellvalue);
		          }},
		          {name : 'deptName',label : '部门',align:'center',width:"100px"},
				  {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
				  {name : 'memberName',label : '盘点员',align:'center',width:"100px"},
				  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
						  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
					  }else{
						  return '';
					  }
				  }},
				  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
				  ];
	var option30=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
		          {name : 'code',label : '单号',align:'center',width:"100px"},
		          {name : 'deptName',label : '部门',align:'center',width:"100px"},
				  {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
				  {name : 'memberName',label : '经手人',align:'center',width:"100px"},
				  {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					  return getDateStr(cellvalue);
				  }},
				  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
						  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
					  }else{
						  return '';
					  }
				  }},
				  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
				  ];
	var option31=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
		          {name : 'code',label : '单号',align:'center',width:"100px"},
		          {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
		        	  return getDateStr(cellvalue);
		          }},
		          {name : 'deptName',label : '部门',align:'center',width:"100px"},
				  {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
				  {name : 'memberName',label : '经手人',align:'center',width:"100px"},
				  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
						  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
					  }else{
						  return '';
					  }
				  }},
				  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
		          ];
	var option41=[ 
//                  {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
		          {name : 'code',label : '单号',align:'center',width:"100px"},
		          {name : 'contactUnitName',label : '客户名称',align:'center',width:"100px"},
		          {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
		        	  return getDateStr(cellvalue);
		          }},
		          {name : 'deptName',label : '部门',align:'center',width:"100px"},
				  {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
				  {name : 'memberName',label : '业务员',align:'center',width:"100px"},
				  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
						  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
					  }else{
						  return '';
					  }
				  }},
				  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
		          ];
	var option42=[
//                  {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
		          {name : 'code',label : '单号',align:'center',width:"100px"},
		          {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
		        	  return getDateStr(cellvalue);
		          }},
		          {name : 'contactUnitName',label : '客户名称',align:'center',width:"100px"},
		          {name : 'deptName',label : '部门',align:'center',width:"100px"},
				  {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
				  {name : 'memberName',label : '业务员',align:'center',width:"100px"},
				  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
				  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
					  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
						  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
					  }else{
						  return '';
					  }
				  }},
				  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
			      ]
	
	var option51=[
//                  {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
                  {name : 'code',label : '单号',align:'center',width:"100px"},
                  {name : 'contactUnitName',label : '客户名称',align:'center',width:"100px"},
                  {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  return getDateStr(cellvalue);
                  }},
                  {name : 'memberName',label : '收款人',align:'center',width:"100px"},
                  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
                		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
                	  }else{
                		  return '';
                	  }
                  }},
                  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
			      ]
	var option52=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
                  {name : 'code',label : '单号',align:'center',width:"100px"},
                  {name : 'contactUnitName',label : '供应商名称',align:'center',width:"100px"},
                  {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  return getDateStr(cellvalue);
                  }},
                  {name : 'memberName',label : '付款人',align:'center',width:"100px"},
                  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
                		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
                	  }else{
                		  return '';
                	  }
                  }},
                  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
			      ];
	var option53=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
	              {name : 'fid',label : 'fid',hidden:true},
	              {name : 'code',label : '单号',align:'center',width:"100px"},
                  {name : 'contactUnitName',label : '往来单位名称',align:'center',width:"100px"},
                  {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  return getDateStr(cellvalue);
                  }},
                  {name : 'memberName',label : '经手人',align:'center',width:"100px"},
                  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
                		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
                	  }else{
                		  return '';
                	  }
                  }},
                  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
			      ];
	var option55=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
                  {name : 'code',label : '单号',align:'center',width:"100px"},
                  {name : 'contactUnitName',label : '供应商',align:'center',width:"100px"},
                  {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  return getDateStr(cellvalue);
                  }},
                  {name : 'memberName',label : '收款人',align:'center',width:"100px"},
                  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
                		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
                	  }else{
                		  return '';
                	  }
                  }},
                  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
			      ];
	var option56=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
	              {name : 'fid',label : 'fid',hidden:true},
                  {name : 'code',label : '单号',align:'center',width:"100px"},
                  {name : 'contactUnitName',label : '客户',align:'center',width:"100px"},
                  {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  return getDateStr(cellvalue);
                  }},
                  {name : 'memberName',label : '收款人',align:'center',width:"100px"},
                  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
                		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
                	  }else{
                		  return '';
                	  }
                  }},
                  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
			      ];
	var option15=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
                  {name : 'code',label : '单号',align:'center',width:"100px"},
                  {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  return getDateStr(cellvalue);
                  }},
                  {name : 'contactUnitName',label : '供应商名称',align:'center',width:"100px"},
                  {name : 'deptName',label : '部门',align:'center',width:"100px"},
                  {name : 'memberName',label : '采购员',align:'center',width:"100px"},
                  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
                		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
                	  }else{
                		  return '';
                	  }
                  }},
                  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
	      	      ];
	var option44=[
//	              {name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
                  {name : 'fid',label : 'fid',hidden:true},
                  {name : 'code',label : '单号',align:'center',width:"100px"},
                  {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  return getDateStr(cellvalue);
                  }},
                  {name : 'contactUnitName',label : '客户名称',align:'center',width:"100px"},
                  {name : 'deptName',label : '部门',align:'center',width:"100px"},
                  {name : 'memberName',label : '业务员',align:'center',width:"100px"},
                  {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
                  {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                	  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
                		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
                	  }else{
                		  return '';
                	  }
                  }},
                  {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
	      	      ];
	//金额格式控制
	function priceAction(cellvalue, options, rowObject){
		if(!cellvalue){
			return "";
		}else if(cellvalue=="0E-8"){
			return 0;
		}else{
			return cellvalue;
		}
	}
	
	//刷新凭证制作列表
	function refreshData(value,name,sort){	
		var options = $("#search-form").serializeJson();
        $data=[];
		$('#dataTable').jqGrid("setGridParam",{postData:options}).trigger("reloadGrid");
		viewSee("");
		return false;
	}
	//清空除订单类型外的搜索选项
	function cleanBoxInput(obj){
		var a=$("#billType").val();
		var $t =$.fool._get$(obj);
		if(obj==".voucherMake"){
			($('#voucherTag').next())[0].comboObj.setComboValue('');
			($('#voucherTag').next())[0].comboObj.setComboText('');
			$('#voucherCode').textbox('textbox').parent().css('display','inline-block');
			$('#voucherCode').textbox('clear');
			return false;
		}
		var inputs=$(obj).find(".dhxDiv");
		for(var i=0;i<inputs.length;i++){
			inputs[i].comboObj.setComboText("");
		}
		$t.form('clear');
		$("#billType").val(a);
	}
	//选择单据类型后的初始化
	function selectDo(){
		var bvalue=$("#billType").val();
		if($data.length>0){
            if($data[0].billType!==bvalue)
                $data=[];
		}else {
            $data.push({billType:bvalue,$page:1,rowids:[],pgdata:[]});
		}
		cleanBoxInput('#s');
		var options={
				datatype:function(postdata){
					$.ajax({
						url: '${ctx}/vouchermake/queryBill',
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
				gridComplete:function(){
				    var pageRowNum = $('#dataTable').jqGrid("getGridParam").postData.rows;
				    if(pageRowNum!=rowNum){
                        rowNum=pageRowNum;
                        $data=[];
                    }
                    var currPage = $('#dataTable').jqGrid('getGridParam').postData.page;
				    if($data.length>0){
                        for(var i=0;i<$data.length;i++){
							if($data[i].$page == currPage){
							    for(var m=0;m<$data[i].rowids.length;m++){
                                    $('#jqg_dataTable_'+$data[i].rowids[m]).attr('checked',true);
                                }
                            }
                        }
                    }
			      },
				postData:$("#search-form").serializeJson(),
				autowidth:true,
				height:"100%",
				pager:"#dataPager",
            	multiselect:true,
				rowNum:10,
				rowList:[10,20,30],
				viewrecords:true,
				jsonReader:{
					records:"total",
					total: "totalpages",
				},
            onSelectRow:function(rowid,status){
                var rowdata = $("#dataTable").jqGrid("getRowData",rowid);
                var currPage = $('#dataTable').jqGrid('getGridParam').postData.page;
                var dataIsExist = false;
                var pageIsExist = false;
                if($data.length>=0){
                    if($data.length==0)
                        $data.push({billType:bvalue,$page:1,rowids:[],pgdata:[]});
                    for(var i=0;i<$data.length;i++){
                        if($data[i].$page==currPage){
                            pageIsExist=true;
                            if($data[i].pgdata.length>0){
                                for(var j = 0;j<$data[i].pgdata.length;j++){
                                    if($data[i].pgdata[j].code==rowdata.code){
                                        dataIsExist=true;
                                        break;
                                    }
                                    continue;
                                }
                                if(status&&!dataIsExist){
                                    $data[i].rowids[$data[i].rowids.length]=rowid;
                                    $data[i].pgdata[$data[i].pgdata.length]=rowdata;
                                }
                                else if(!status&&dataIsExist){
                                    $data[i].pgdata.splice(j,1);
                                    for(var m=0;m<$data[i].rowids.length;m++){
                                        if($data[i].rowids[m]==rowid)
                                            $data[i].rowids.splice(m,1)
                                    }
                                    if($data[i].pgdata.length<=0){
                                        $data.splice(i,1)
                                    }
                                }
                            }else{
                                if(status){
                                    $data[i].rowids.push(rowid);
                                    $data[i].pgdata.push(rowdata);
                                }
                            }
                            break;
                        }
                        continue;
                    }
                    if(!pageIsExist&&status)
                        $data.push({billType:bvalue,$page:currPage,rowids:[rowid],pgdata:[rowdata]});
                }else if($data.length<0&&status){
                    $data.push({billType:bvalue,$page:currPage,rowids:[rowid],pgdata:[rowdata]});
                }
                viewSee(rowdata.fid);
            },
            onSelectAll:function(aRowids,status){
                var currPage = $('#dataTable').jqGrid('getGridParam').postData.page;
                var pageIsExist = false;
                var rowdata;
                var datas = [];
                if(status){
                    for(var rowid in aRowids){
                        rowdata = jQuery('#dataTable').jqGrid('getRowData',aRowids[rowid]);
                        datas.push(rowdata)
                    }
                }
                for(var i=0;i<$data.length;i++){
                    if($data[i].$page == currPage){
                        pageIsExist = true;
                        if(status){
                            $data[i].rowids=aRowids;
                            $data[i].pgdata=datas;
                        }else{
                            $data.splice(i,1)
                        }
                        break;
                    }
                    continue;
                }
                if(!pageIsExist)
                    $data.push({billType:bvalue,$page:currPage,rowids:aRowids,pgdata:datas})
            },
		}
		switch(bvalue)
		{
		case '11':
			options.colModel=option11;
		  break;
		case '12':
			options.colModel=option12;
		  break;
		case '15':
			options.colModel=option15;
		  break;
		case '20':
			options.colModel=option21;
		  break;
		case '21':
			options.colModel=option20;
		  break;
		case '22':
			options.colModel=option20;
		  break;
		case '30':
			options.colModel=option30;
		  break;
		case '31':
			options.colModel=option31;
		  break;
		case '32':
			options.colModel=option30;
		  break;
		case '33':
			options.colModel=option31;
		  break;
		case '41':
			options.colModel=option41;
		  break;
		case '42':
			options.colModel=option42;
		  break;
		case '44':
			options.colModel=option44;
		  break;
		case '51':
			options.colModel=option51;
		  break;
		case '52':
			options.colModel=option52;
		  break;
		case '55':
			options.colModel=option55;
		  break;
		case '56':
			options.colModel=option56;
		  break;
		default:
			options.colModel=option53;
		}
		$.jgrid.gridUnload("dataTable");
		$("#dataTable").jqGrid(options);
		refreshData();
		//setPager($('#dataTable'));
		$("#customer").css({"display":"inline-block"});
		$("#supplier").css({"display":"inline-block"});
		$("#deptId").css({"display":"inline-block"});
		$("#bank").css({"display":"inline-block"});
		if(bvalue=='12'||bvalue=="11"||bvalue=="15"){
			$("#customer").css({"display":"none"});$("#bank").css({"display":"none"});
		}else if(bvalue=='41'||bvalue=="42"||bvalue=="44"){
			$("#supplier").css({"display":"none"});$("#bank").css({"display":"none"});
		}else if(bvalue=="53"){
			$("#deptId").css({"display":"none"});$("#bank").css({"display":"none"});
		}else if(bvalue=='51'||bvalue=="56"){
			$("#deptId").css({"display":"none"});$("#supplier").css({"display":"none"});$("#bank").css({"display":"none"});
		}else if(bvalue=='52'||bvalue=="55"){
			$("#deptId").css({"display":"none"});$("#customer").css({"display":"none"});$("#bank").css({"display":"none"});
		}else{
			$("#supplier").css({"display":"none"});$("#customer").css({"display":"none"});$("#bank").css({"display":"none"});
		}
	}
	//加载订单详情
	function viewSee(fid){
		var billType=$('#billType').val();
		$('#goodList').jqGrid("setGridParam",{postData:{billId:fid,billType:billType}}).trigger("reloadGrid");
	}
//制作凭证	
$('#make').click(function(e){
	var billType=$('#billType').val();
	var voucherDate=$("#voucherDate").datebox('getValue');
	var modelId = $('#modelId').val();
	var ids = [];
	var codes = [];
	var wordNum=[]
	for(var i=0;i<$data.length;i++){
	    for(var j=0;j<$data[i].pgdata.length;j++){
			ids.push(($data[i].pgdata[j].fid));
			codes.push(($data[i].pgdata[j].code))
            wordNum.push($data[i].pgdata[j].voucherWordNumber)
		}
	}

	var billIds=ids.join(',');
	var billcodes="";
	$('#modelName').combogrid('enableValidation');  
	if($('#modelName').combogrid('isValid')){
		if(!billIds){
			$.fool.alert({msg:"请选择单据！"});
			return false;
		}
		for(var i=0;i<wordNum.length;i++){
		    if(wordNum[i]!="")
                billcodes+=codes[i]+',';
		}
        if(billcodes){
			$.fool.alert({msg:'单据'+billcodes+'已经生成过凭证，不能再生成，请重新选择'});
			$(".checker").prop("checked",false);
			return false;
		}
		$.post('${ctx}/vouchermake/makeVoucher',{billType:billType,templateId:modelId,voucherDate:voucherDate,billIds:billIds},function(data){
    		if(data.returnCode=='0'){
    			$.fool.alert({msg:'制作完成！',fn:function(){
                    $data=[];
                    //selectDo();
                    $('#dataTable').trigger("reloadGrid");
    			}});
    		}else if(data.returnCode=='1'){
    			$.fool.alert({msg:data.message});
    		}else{
    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
    		}
    		return true;
    	});
	}else{
			return false;
		}
});

$('#config').click(function(){
	$('#addBox').window({
		title:'模板设置',
		top:10+$(window).scrollTop(),
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		modal:true,
		href:'${ctx}/vouchermake/config',
		onClose:function(){
			$('html').css({'overflow':''});
			//解决滑动条消失后再显示出现不能用滑轮滚动的问题
			parent.$("#tabs").tabs("unselect","制作凭证");
			setTimeout(function(){parent.$("#tabs").tabs("select","制作凭证");},100);
		},
		onOpen:function(){
			$(this).parent().prev().css("display","none");
			$(this).parent().css('padding-top',"24px");
		}
	});
});

$("#goodList").jqGrid({
	datatype:"json",
	url:"${ctx}/vouchermake/getBillDetail",
	postData:{billId:"",billType:$('#billType').val()},
	autowidth:true,
	height:"100%",
	pager:"#pager",
//    multiselect:true,
    viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[
	          {name : 'goodsId',label :"goodsId",hidden:true}, 
	          {name : 'unitId',label :"unitId",hidden:true}, 
	          {name : 'goodsSpecGroupId',label : 'goodsSpecGroupId',hidden:true},
	          {name : 'barCode',label : '货品条码',align:'center',width:"100px"},			          
	          {name : 'goodsCode',label : '货品编号',align:'center',width:"100px"},
	          {name : 'goodsName',label : '货品名称',align:'center',width:"100px"},
	          {name : 'inWareHouseName',label : '仓库',align:'center',width:"100px"},
	          {name : 'unitName',label : '单位',align:'center',width:"100px"},
	          {name : 'quentity',label : '数量',align:'center',width:"100px"},
	          {name : 'unitPrice',label : '单价',align:'center',width:"100px"},
	          {name : 'type',label : '金额',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
		  		  if(rowObject.quentity&&rowObject.unitPrice){
		  			  return (rowObject.quentity*rowObject.unitPrice).toFixed(2);
		  		  }else{
		  			  return 0;
		  		  }
		  	  }},
	          {name : 'describe',label : '备注',align:'center',width:"100px"},
	          {name : 'unitGroupId',label : '单位组ID',align:'center',width:"100px"},
			  ],
});

$("#dataTable").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/vouchermake/queryBill',
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
	postData:$("#search-form").serializeJson(),
	autowidth:true,
	height:"100%",
	pager:"#dataPager",
	rowNum:10,
	rowList:[10,20,30],
    multiselect:true,
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	colModel:[
	//	          {name : '',label :"",align:'center',width:"25px"/*,
	//				  formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>";
	//				}*/},
	          {name : 'fid',label : 'fid',hidden:true},
	          {name : 'code',label : '单号',align:'center',width:"100px"},
	          {name : 'billDate',label : '单据日期',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
	        	  return getDateStr(cellvalue);
	          }},
	          {name : 'contactUnitName',label : '供应商名称',align:'center',width:"100px"},
              {name : 'deptName',label : '部门',align:'center',width:"100px"},
              {name : 'warehouseStockName',label : '仓库',align:'center',width:"100px"},
              {name : 'memberName',label : '采购员',align:'center',width:"100px"},
              {name : 'amount',label : '合计金额',align:'center',width:"100px",formatter:priceAction},
              {name : 'specialAmount',label : '优惠金额',align:'center',width:"100px",formatter:priceAction},
              {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
            	  if(rowObject.voucherId && typeof cellvalue != 'undefined'){
            		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\');" >'+cellvalue+'</a>';
            	  }else{
            		  return '';
            	  }
              }},
              {name : 'voucherDate',label : '凭证日期',align:'center',width:"100px"},
		      ],
});
selectDo();
/* $('#customerName').combogrid('textbox').focus(function(){
	$('#customerName').combogrid('showPanel');
});
$('#supplierName').combogrid('textbox').focus(function(){
	$('#supplierName').combogrid('showPanel');
}); */
enterSearch('search-btn');
	</script>