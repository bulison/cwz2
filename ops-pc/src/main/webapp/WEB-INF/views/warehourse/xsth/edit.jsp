<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
           <div id="goodsSpecChooser" style="display: none;">
            <div id="goodsSpecSearcher">
              <input id="search-goodsSpecName"/> <a id="search-goodsSpecBtn" class="btn-blue btn-s">筛选</a>
            </div>
            <table id="goodsSpecTable"></table>
          </div>
          <div id="userChooser">
            <div id="userSearcher">
              <input id="search-userCode"/>+<input id="search-userName"/>+<input id="search-userPhone"/> <a id="search-userBtn" class="btn-blue btn-s">筛选</a>
            </div>
            <table id="userTable"></table>
          </div>
          <div id="customerChooser">
            <div id="cusSearcher">
              <input id="search-cusCode"/>+<input id="search-cusName"/>+<input id="search-cusPhone"/> <a id="search-cusBtn" class="btn-blue btn-s">筛选</a>
            </div>
            <table id="customerTable"></table>
          </div>
          <div id="goodsChooser">
            <div id="goodsSearcher">
              <input id="search-goodsCode"/>+<input id="search-goodsName"/>+<input id="search-goodsSpec"/> <a id="search-goodsBtn" class="btn-blue btn-s">筛选</a>
            </div>
            <table id="goodsTable"></table>
            <p style="margin-left:44px"><font><a id="ok" class="btn-blue btn-s">确认</a></font></p> 
          </div>
          <div class="form1" >
              <form id="form">
                <input id="fid" type="hidden" name="fid" value="${code!=null?'':obj.fid}"/>                <input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}"/>
                <input id="inMemberId" name="inMemberId" type="hidden" value="${obj.inMemberId}"/>
                <input id="supplierId" type="hidden" name="supplierId" value="${obj.supplierId}"/>
                <input id="customerId" type="hidden" name="customerId" value="${obj.customerId}"/>
                <input id="deptId" type="hidden" name="deptId" value="${obj.deptId}"/>
                <input id="relationId" type="hidden" name="relationId" value="${obj.relationId }"/>
                <p><font><label style="color: red">*</label>单号：</font><input id="code"  class="textBox" ${code==null&&obj.code==null?"":"readonly='readonly'"} value="${code==null?(obj.code==null?'':obj.code):code}" name="code" /></p>				
                <p><font>凭证号：</font><input data-options="validType:'maxLength[50]'" id="voucherCode" name="voucherCode" class="textBox" value="${obj.voucherCode}"/></p>
				<p><font><em>*</em>单据日期：</font><input id="billDate" name="billDate" class="textBox" value="${obj.billDate}"/></p>
				<p><font><em>*</em>销售商名称：</font><input id="customerName" name="customerName" value="${obj.customerName}" class="textBox" /></p>
				<p><font><em>*</em>销售商编号：</font><input id="customerCode" readonly="readonly" name="customerCode" value="${obj.customerCode}" class="textBox" /></p>
				<p><font>销售商电话：</font><input id="customerPhone" name="customerPhone" readonly="readonly" class="textBox"  value="${obj.customerPhone}"/></p>
				<p><font>仓库名称：</font><input id="inWareHouseId" class="textBox"/></p>
				<p><font><em>*</em>部门：</font><input id="deptName" class="textBox" value="${obj.deptName}"/></p>
				<p><font><em>*</em>业务员：</font><input id="inMemberName" value="${obj.inMemberName}" class="textBox"/></p>
				<p><font><em>*</em>计划完成日期：</font><input id="planEnd" name="endDate" class="textBox" value="${obj.endDate}"/></p>				
				<p><font>合计金额：</font><input id="totalAmount" name="totalAmount" class="textBox" readonly="readonly" value="${obj.totalAmount}"/></p>
				<p><font>销售出货单：</font><input id="relationName" value="${obj.relationName}" class="textBox"/><a href="#" title="清除" class="clearBill"><img style="vertical-align:middle;" src="${ctx}/resources/images/cancel.png"></a></p>
				<p><font>免单金额：</font><input id="freeAmount" name="freeAmount" class="textBox" readonly="readonly" value="${mark==1?0:obj.freeAmount}"/></p>
				
				<p style="width:65%;"><font>备注：</font><input data-options="validType:'maxLength[200]'" id="describe" name="describe" class="textBox" style="width:80%;" value="${obj.describe}"/></p><br/> 
				<h3 style="display: inline-block;margin-left:44px">&emsp;其他信息：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"><br/>
				<p class="hideOut"><font>制单人：</font><input id="creatorName" name="creatorName" class="textBox" readonly="readonly" value="${obj.creatorName}"/></p>
				<p class="hideOut"><font>制单时间：</font><input id="createTime" name="createTime" class="textBox" readonly="readonly" value="${obj.createTime}"/></p>
				<p class="hideOut"><font>审核人：</font><input id="auditorName" name="auditorName" class="textBox" readonly="readonly" value="${obj.auditorName}"/></p>
				<p class="hideOut"><font>审核时间：</font><input id="auditTime" name="auditTime" class="textBox" readonly="readonly" value="${obj.auditTime}"/></p>
				<p class="hideOut"><font>作废时间：</font><input id="cancelTime" name="cancelTime" class="textBox" readonly="readonly" value="${obj.cancelTime}"/></p>
				<p class="hideOut"><font>作废人：</font><input id="cancelorName" name="cancelorName" class="textBox" readonly="readonly" value="${obj.cancelorName}"/></p>
              </form>
          </div>
          <table id="goodsList"></table>
          <div id="toolbar">
           ${(obj.recordStatus==null||obj.recordStatus==0)?'<a href="#" id="addGoods" class="btn-ora-add">添加</a> <a href="#" id="okAll" class="btn-ora-add" onclick="allConfirm();" style="display:none">一键确认</a>':''} 
          </div>
          <div class="form1" id="btnBox">
          </div>
          <div id="pop-win"></div>
<script type="text/javascript">
var chooserWindow="";
var _data="";
//if($("#fid").val()){
	 _data= ${obj.details}
	 _data;
//}
var _id = '${obj.fid}';
var win;
var scaleValue="";
var unitIdValue="";
var goodsSpecIdValue="";
var unitNameValue="";
var edits = new Array();
var goodsSpecData="";
$.ajax({
	url:"${ctx}/goodsspec/getChidlList",
	async:false,
	data:{q:""},
	success:function(data){
		goodsSpecData=data;
	}
});
$(function(){	
	var unitData="";
	validateBox($("#code"),true);
	validateBox($("#voucherCode"));
	validateBox($("#describe")); 
	dateBox($("#billDate"),true);
	dateBox($("#planEnd"),true);
	validateBox($("#supplierCode"));
	validateBox($("#supplierName"));
	comboTree($("#inWareHouseId"),"${ctx}/basedata/warehourseList",false,function(node, data){
		if(data[0].id!=""){
			var node=$(this).tree("find",data[0].id);
			$(this).tree('update',{
				target: node.target,
				text: '请选择',
				id:""
			});
		}
		$("#inWareHouseId").combotree("setValue","${obj.inWareHouseId}");
	});
	
	//业务员/采购员
	$("#inMemberName").click(function(){
		memberChooserOpen = true;
			 win = $("#pop-win").fool('window',{'title':"选择",height:480,width:780,
				href:getRootPath()+'/member/window?okCallBack=selectMember&onDblClick=selectMember2&singleSelect=true'});
    }).validatebox({required:true,novalidate:true});
	$("#customerName").click(function(){
		memberChooserOpen = true;
		win = $("#pop-win").fool('window',{
			'title':"选择销售商",height:480,width:780,
			href:'${ctx}/customer/window?okCallBack=selectCus&onDblClick=selectCusDBC&singleSelect=true'});
	}).validatebox({required:true,novalidate:true});
	$("#relationName").click(function(){
		memberChooserOpen = true;
		win = $("#pop-win").fool('window',{
			'title':"选择销售出货订单",height:480,width:780,
			href:'${ctx}/salebill/xsch/xschwindow?okCallBack=selectRelation&onDblClick=selectRelationDBC&billCode=xsch&singleSelect=true&cusId='+$("#customerId").val()});
	});
	$('#deptName').fool('deptComBoxTree',{'valTarget':'#deptId',editable:false,onLoadSuccess:function(node,data){
		if(_id != ''){
			$('#deptName').combotree("setValue","${obj.deptId}");		 
			$("#deptId").val('${obj.deptId}');
			 _id = '';
		}
	}});
	textBox($("#search-goodsCode"),"编号");
	textBox($("#search-goodsName"),"名称");
	textBox($("#search-goodsSpec"),"规格");
	textBox($("#search-goodsSpecName"),"属性名");
	$("#search-goodsCode").textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					goodSearch();
				}
			}
		})
	});
	$("#search-goodsName").textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					goodSearch();
				}
			}
		})
	});
	$("#search-goodsSpec").textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					goodSearch();
				}
			}
		})
	});
	if($("#fid").val()){
		$("#code").val('${obj.code}');
		$("#code").attr("readonly","readonly");
	}
	else if('${code}'){
		$("#code").val('${code}');
		$("#code").attr("readonly","readonly");
	}
	
	if('${obj.recordStatus}'==null||'${obj.recordStatus}'==''){
		$("#btnBox").append(' <fool:tagOpt optCode="xsthAdd"><p><input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /></p></fool:tagOpt>');
	}else if('${obj.recordStatus}'==0){
		$("#btnBox").append('<fool:tagOpt optCode="xsthAction1"><p><input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /></p></fool:tagOpt> <fool:tagOpt optCode="xsthAction3"><p><input type="button" id="copy" class="btn-blue2 btn-xs" value="复制" /></p></fool:tagOpt> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p> ');
	}else if('${obj.recordStatus}'==1){
		$("#inWareHouseId").combotree({hasDownArrow:false});
		$("#deptName").combotree({hasDownArrow:false});
		$("#form").find("input").attr('disabled','disabled');
		$("#billDate").datebox('disable'); 
		$("#planEnd").datebox('disable'); 
		$("#addGoods").attr('disabled','disabled');
		$("#btnBox").append('<fool:tagOpt optCode="xsthAction3"><p><input type="button" id="copy" class="btn-blue2 btn-xs" value="复制" /></p></fool:tagOpt> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p><fool:tagOpt optCode="xsthAction5"><p><input type="button" id="cancel" class="btn-blue2 btn-xs" value="作废"/></p></fool:tagOpt>');
	}else if('${obj.recordStatus}'==2){
		$("#inWareHouseId").combotree({hasDownArrow:false});
		$("#deptName").combotree({hasDownArrow:false});
		$("#form").find("input").attr('disabled','disabled');
		$("#billDate").datebox('disable');
		$("#planEnd").datebox('disable'); 
		$("#addGoods").attr('disabled','disabled');
		$("#btnBox").append('<fool:tagOpt optCode="xsthAction3"><p><input type="button" id="copy" class="btn-blue2 btn-xs" value="复制" /></p></fool:tagOpt><p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p>');
	}
	$.get("${ctx}/unitController/getLeafUnit",function(data){
		
		unitData=data;
		$("#goodsList").datagrid({
			data:_data,
			fitColumns:true,
			toolbar:"#toolbar",
			columns:[[
				  		{field:'goodsId',title:'fid',hidden:true,width:100},
				  		{field:'barCode',title:'货品条码',sortable:true,width:100},
						{field:'goodsCode',title:'货品编号',sortable:true,width:100}, 
						{field:'goodsName',title:'货品名称',sortable:true,width:100},
						{field:'inWareHouseId',title:'仓库ID',width:100,hidden:true,editor:{type:'textbox'}},//咱数存放id
						{field:'inWareHouseName',title:'仓库',width:'100',editor:{type:'combotree',options:{required:true,novalidate:true,url:"${ctx}/basedata/warehourseList",onLoadSuccess:function(node,data){
							if(data[0].id!=""){
								var node=$(this).tree("find",data[0].id);
								$(this).tree('update',{
									target: node.target,
									text: '请选择',
									id:null
								});
							}
						},onSelect:function(node){
							 var root=$(this).tree('getRoot');
								if(node.id==root.id){
									combotree('clear');
								}; 
						}}},formatter:function(value,row,index){
							if(value){
								var name='';
								$.ajax({
									url:"${ctx}/basedata/getById",
									async:false,
									data:{fid:value},
									success:function(data){
										if(value){
											name=data.name;
										}
									}
								});
								if(name){
									return name;
								}else{
									return value;
								}
							} 
						}},
						{field:'goodsSpec',title:'规格',sortable:true,width:100},
						{field:'goodsSpecId',hidden:true,sortable:true,width:100},
						{field:'goodsSpecName',title:'属性',formatter:function(value){
					  		for(var i=0; i<goodsSpecData.length; i++){
					  			if (goodsSpecData[i].fid == value) return goodsSpecData[i].name;
					  		}
					  		return value;
					  	},editor:{type:'combobox',options:{required:true,valueField:"fid",textField:"name",mode:"remote",novalidate:true,onBeforeLoad:function(param){
							if(!$(this).combobox('options').url||$(this).combobox('options').url==""){
								var index=$(this).parents("[field='goodsSpecName']").parent(".datagrid-row").attr("datagrid-row-index");
								$(this).combobox({ 
									url:"${ctx}/goodsspec/getChidlList?groupId="+$(this).parents("[field='goodsSpecName']").siblings("[field='goodsSpecGroupId']").children().text(),
									validType:"goodsSpec["+index+"]"
								});
								param.q="";
							}
						},onSelect:function(record){
							$(this).parents("[field='goodsSpecName']").prev().children().text(record.fid);
						}}},sortable:true,width:100},
					    {field:'unitId',hidden:true,sortable:true,width:100},
					    /*{field:'unitName',title:'单位',width:90,formatter:function(value){
					  		var unitData="";
					  		$.ajax({
					  			url:"${ctx}/unitController/getLeafUnit",
					  			async:false,
					  		    data:{},
					  		    success:function(data){
					  		    	unitData=data;
					  		    }
					  		});
					  		for(var i=0; i<unitData.length; i++){
					  			if (unitData[i].fid == value) return unitData[i].name;
					  		}
					  		return value;
					  	},editor:{type:'combobox',options:{
					  		valueField:"fid",
							textField:"name",
							required:true,
							readonly:false,
							mode:"remote",
							onBeforeLoad:function(param){
								if(!$(this).combobox('options').url||$(this).combobox('options').url==""){
									var index=$(this).parents("[field='unitName']").parent(".datagrid-row").attr("datagrid-row-index");
									$(this).combobox({
										url:"${ctx}/unitController/getChildsOfMatch?unitGroupId="+$(this).parents("[field='unitName']").siblings("[field='unitGroupId']").children().text(),
										validType:"unit["+index+"]"
												
									});
									param.q="";
								}
							},
							onShowPanel:function(){
								$(this).combobox('reload',"${ctx}/unitController/getChilds?unitGroupId="+$(this).parents("[field='unitName']").siblings("[field='unitGroupId']").children().text())
							},
							onSelect:function(record){
								var scaleField=$(this).parents("[field='unitName']").next().children();
								$(this).parents("[field='unitName']").prev().children().text(record.fid);
								$.post("${ctx}/unitController/get",{id:record.fid},function(data){scaleField.text(data.scale)});
								
								var customerId=$("#customerId").val();
								var supplierId=$("#supplierId").val();
								var goodid = $(this).parents("[field='unitName']").siblings("[field='goodsId']").children().text();
								var goodsSpecId = $(this).parents("[field='unitName']").siblings("[field='goodsSpecId']").children().text(); 
								var unitPriceField=$(this).parents("[field='unitName']").siblings("[field='unitPrice']").find(".numberbox-f");
								var costPriceField=$(this).parents("[field='unitName']").siblings("[field='costPrice']").find(".numberbox-f");
								$(this).parents("[field='unitName']").prev().children().text(record.fid);
								unitIdValue=record.fid;
								unitNameValue = record.name;
								$(this).combobox('setValue',record.name);
								$.post("${ctx}/goods/getOtherPrice",{billType:"xsth",fid:goodid,unitId:record.fid,customerId:customerId,supplierId:supplierId,goodsSpecId:goodsSpecId},function(data){
									unitPriceField.numberbox('setValue',data.referencePrice); 
									costPriceField.numberbox('setValue',data.costPrice);
								}); 
								$(this).combobox('validate');
								$(this).combobox('textbox').focus();
							}
					  	}},sortable:true,width:100},*/
					  	{field:'unitName',title:'单位',width:90,formatter:function(value){
					  		var unitData="";
					  		$.ajax({
					  			url:"${ctx}/unitController/getLeafUnit",
					  			async:false,
					  		    data:{},
					  		    success:function(data){
					  		    	unitData=data;
					  		    }
					  		});
					  		for(var i=0; i<unitData.length; i++){
					  			if (unitData[i].fid == value) return unitData[i].name;
					  		}
					  		return value;
					  	},editor:{type:'combobox',options:{
					  		valueField:"fid",
							textField:"name",
							required:true,
							readonly:false,
							mode:"remote",
							onBeforeLoad:function(param){
								if(!$(this).combobox('options').url||$(this).combobox('options').url==""){
									var index=$(this).parents("[field='unitName']").parent(".datagrid-row").attr("datagrid-row-index");
									$(this).combobox({
										url:"${ctx}/unitController/getChildsOfMatch?unitGroupId="+$(this).parents("[field='unitName']").siblings("[field='unitGroupId']").children().text(),
										validType:"unit["+index+"]"
												
									});
									param.q="";
								}
							},
							onSelect:function(record){
								var scaleField=$(this).parents("[field='unitName']").next().children();
								$(this).parents("[field='unitName']").prev().children().text(record.fid);
								$.post("${ctx}/unitController/get",{id:record.fid},function(data){scaleField.text(data.scale)});
								
								var customerId=$("#customerId").val();
								var supplierId=$("#supplierId").val();
								var goodid = $(this).parents("[field='unitName']").siblings("[field='goodsId']").children().text();
								var goodsSpecId = $(this).parents("[field='unitName']").siblings("[field='goodsSpecId']").children().text(); 
								var unitPriceField=$(this).parents("[field='unitName']").siblings("[field='unitPrice']").find(".numberbox-f");
								var costPriceField=$(this).parents("[field='unitName']").siblings("[field='costPrice']").find(".numberbox-f");
								$(this).parents("[field='unitName']").prev().children().text(record.fid);
								unitIdValue=record.fid;
								unitNameValue = record.name;
								 $.post("${ctx}/goods/getOtherPrice",{billType:"xsth",fid:goodid,unitId:record.fid,customerId:customerId,supplierId:supplierId,goodsSpecId:goodsSpecId},function(data){
									unitPriceField.numberbox('setValue',data.referencePrice); 
									costPriceField.numberbox('setValue',data.costPrice);
								});  
								$(this).combobox('validate');
								$(this).combobox('textbox').focus();
							}
					  	}},sortable:true,width:100},
						{field:'scale',title:'换算关系',hidden:true,sortable:true,width:100},
						{field:'quentity',title:'数量',editor:{type:'numberbox',options:{required:true,precision:2,min:0}},sortable:true,width:100},
				  		{field:'unitPrice',title:'单价',editor:{type:'numberbox',options:{required:true,precision:2,min:0}},sortable:true,width:100,formatter:function(value){
				  			if(value=="0E-8"){
				  				return 0;
				  			}else{
				  				return value;
				  			}
				  		}},
				  		{field:'type',title:'金额',sortable:true,width:50,formatter:function(value,row,index){
				  			if(row.quentity&&row.unitPrice){
				  				return (row.quentity*row.unitPrice).toFixed(2);
				  			}else{
				  				return 0;
				  			}
				  		}},
				  		{field:'costPrice',title:'成本单价',editor:{type:'numberbox',options:{required:true,novalidate:true,precision:2,min:0}},sortable:true,width:100},
						{field:'describe',title:'备注',editor:{type:'textbox',options:{validType:'maxLength[200]'}},sortable:true,width:100},
						{field:'unitGroupId',title:'单位组ID',hidden:true,width:100},
						{field:'goodsSpecGroupId',sortable:true,hidden:true},
						{field:'_isNew',hidden:true,title:"是否新增",editor:{type:'text'}},
						{field:'action',title:'操作',width:150,formatter:function(value,row,index){
				  			if (row.editing){
				  				btnDisabled();
				  				var s = '<a class="btn-save" title="确认" href="javascript:;" onclick="saverow(this)"></a>';
								var c = '<a class="btn-back" title="撤销" href="javascript:;" onclick="cancelrow(this)"></a>';
								return s+c;
							} else {
								btnEnabled();
				  				if('${obj.recordStatus}'!=0){
				  					return "";
				  				}
								var e= '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editer(this)"></a>'; 
					  			var d= '<a class="btn-del" title="删除" href="javascript:;" onclick="deleter(this)"></a>';
					  			return e+d;
							}
				  		}}
				      ]],
				      onBeforeEdit:function(index,row){
							row.editing = true;
							updateActions(index);
						},
						onAfterEdit:function(index,row){
							row.editing = false;
							updateActions(index);
							getTotal();
						},
						onCancelEdit:function(index,row){
							if(row.goodsSpecGroupId&&!row.goodsSpecName){
								//$.fool.alert({msg:'必须选取属性。'});
								$("#goodsList").datagrid('beginEdit',index);
							}else{
								row.editing = false;
								updateActions(index);
							}
						}
		});
		//键盘控制
		keyHandler();
	});
	
});
//回车键控制
enterController("form");		
$("#addGoods").click(function(){
	var urlStr="";
	if($("#relationId").val()){
		urlStr='${ctx}/goods/getGoods?billId='+$("#relationId").val();
	}else{
		urlStr='${ctx}/goods/getChilds';
	}
	if('${obj.recordStatus}'==0){
		 goodsChooserOpen=true;
		 $('#goodsChooser').css("display","inline-block");
		 $('#goodsChooser').window({
				title:'选择货品',
				top:100,  
				collapsible:false,
				minimizable:false,
				maximizable:false,
				resizable:false,
				width:1074,
				height:500,
				onBeforeOpen:function(){
					$('#goodsTable').datagrid({
						singleSelect:false,
						idField:'fid', 
						url:urlStr,
						pagination:true,
						fitColumns:true, 
						columns:[[  
	                                {field:'checkbox',title:'',checkbox:true},
	                                {field:'fid',title:'fid',hidden:true,width:100},
	            			  		{field:'barCode',title:'货品条码',sortable:true,width:100},
	            					{field:'code',title:'货品编号',sortable:true,width:100},
	            					{field:'name',title:'货品名称',sortable:true,width:100},
	            					{field:'spec',title:'规格',sortable:true,width:100},
	            					{field:'goodsSpecGroupId',title:'属性组Id',hidden:true,sortable:true,width:100},
	            					{field:'goodsSpecId',title:'属性Id',hidden:true,sortable:true,width:100},
	            					{field:'goodsSpecName',title:'属性',sortable:true,width:100},
	            					{field:'unitId',title:'单位Id',hidden:true,sortable:true,width:100},
	            			  		{field:'unitName',title:'单位',sortable:true,width:100},
	            			  		{field:'goodsSpecGroupId',title:'属性组Id',hidden:true,sortable:true,width:100},
	            			  		{field:'unitScale',title:'换算关系',hidden:true,sortable:true,width:100},
	            					{field:'describe',title:'备注',sortable:true,width:100},
							      ]],
					   onDblClickRow:function(rowIndex, rowData){
						   var customerId = $("#customerId").val();
						   var supplierId = $("#supplierId").val();
						   var referencePrice = 0;
						   var costPrice = 0;
						   $.post("${ctx}/goods/getOtherPrice",{billType:"xsth",fid:rowData.fid,unitId:rowData.unitId,customerId:customerId,supplierId:supplierId,goodsSpecId:rowData.goodsSpecId},function(data){
							   referencePrice = data.referencePrice;
							   costPrice = data.costPrice;
							});
								$("#goodsList").datagrid('appendRow',{
									goodsId:rowData.fid,
									barCode: rowData.barCode,
									goodsCode: rowData.code,
									goodsName: rowData.name,
									goodsSpec: rowData.spec,
									goodsSpecGroupId:rowData.goodsSpecGroupId,
									goodsSpecId:"",
									goodsSpecName: "",
									unitId:rowData.unitId,
									unitName: rowData.unitName,
									unitGroupId:rowData.unitGroupId,
									scale:rowData.unitScale,
									describe: rowData.describe,
									unitPrice:referencePrice,
									costPrice:costPrice,
									type:0,
									unitPrice:0,
									quentity:1,
									_isNew:true
								});
							getTotal();
							var rows=$("#goodsList").datagrid('getRows');
							var index=rows.length-1;
							$("#goodsList").datagrid('beginEdit',index);
							devalue(index);
							if(!rowData.goodsSpecId){
					    		  var editor=$("#goodsList").datagrid('getEditor',{index:index,field:'goodsSpecName'});
					    		  $(editor.target).combobox("destroy");
					    	}
							edits.push(index);
							hideOkAll();
							$('#goodsChooser').window('close');
							$('#goodsTable').datagrid('unselectAll');
							$('#goodsTable').datagrid('uncheckAll');
					   }
					});
				},
				onClose:function(){
					$('#goodsTable').datagrid('clearSelections');
					$('#goodsTable').datagrid('clearChecked');
				}
		 });
		 setPager($('#goodsTable'));
	}
});


$("#search-goodsSpecBtn").click(function(){
	var name=$("#search-goodsSpecName").textbox('getValue');
	var options = {"name":name};
	$('#userTable').datagrid('load',options);
});

$("#search-goodsBtn").click(function(){
	var code=$("#search-goodsCode").textbox('getValue');
	var name=$("#search-goodsName").textbox('getValue');
	var spec=$("#search-goodsSpec").textbox('getValue');
	var options = {"code":code,"name":name,"spec":spec};
	$('#goodsTable').datagrid('load',options);
});

$("#ok").click(function(){
	var nowLen = $("#goodsList").datagrid('getRows').length;
	var nodes=$('#goodsTable').datagrid('getSelections');
	$(nodes).each(function(){
		$("#goodsList").datagrid('appendRow',{
			goodsId:this.fid,
			barCode: this.barCode,
			goodsCode: this.code,
			goodsName: this.name,
			goodsSpec: this.spec,
			goodsSpecId:this.goodsSpecId,
			goodsSpecName: "",
			goodsSpecGroupId: this.goodsSpecGroupId,
			unitId:this.unitId,
			unitName: this.unitName,
			unitGroupId:this.unitGroupId,
			scale:this.unitScale,
			describe: this.describe,
			type:0,
			unitPrice:0,
			quentity:1,
			_isNew:true
		});
	});
	getTotal();
	var rows=$("#goodsList").datagrid('getRows');
	var index="";
	for(var i=nowLen;i<rows.length;i++){
		index=$("#goodsList").datagrid('getRowIndex',rows[i]);
		$("#goodsList").datagrid('beginEdit',index);
		devalue(index);
		if(!rows[i].goodsSpecGroupId){
			  var editor=$("#goodsList").datagrid('getEditor',{index:index,field:'goodsSpecName'});
			  $(editor.target).combobox("destroy");
		}
		edits.push(index);
	}
	hideOkAll();
	$('#goodsChooser').window('close');
	$('#goodsTable').datagrid('unselectAll');
	$('#goodsTable').datagrid('uncheckAll');
});

function devalue(index){
	   var warehouse=$('#inWareHouseId').combotree('getValue');
	   var ez = $('#goodsList').datagrid('getEditor', {index:index,field:'inWareHouseId'});
	   $(ez.target).textbox({value:warehouse});
	   var ed = $('#goodsList').datagrid('getEditor', {index:index,field:'inWareHouseName'});
	   $(ed.target).combotree({onLoadSuccess:function(node, data){
		   $(ed.target).combotree('setValue',warehouse);
	   }});
	}
	
function selectCus(rowData){
	$("#customerId").val(rowData[0].fid);
	$("#customerName").val(rowData[0].name);
	$("#customerCode").val(rowData[0].code);
	$("#customerPhone").val(rowData[0].phone);
	$("#inWareHouseId").combotree("clear");
	$("#deptName").combotree("clear");
	$("#inMemberName").val("");
	$("#relationName").val("");
	$("#relationId").val("");
	$("#totalAmount").val("");
	$("#freeAmount").val("");
	$("#planEnd").datebox('clear');
	//$("#goodsList").datagrid('loadData',[]);
	win.window('close');
}
function selectCusDBC(rowData){
	$("#customerId").val(rowData.fid);
	$("#customerName").val(rowData.name);
	$("#customerCode").val(rowData.code);
	$("#customerPhone").val(rowData.phone);
	$("#inWareHouseId").combotree("clear");
	$("#deptName").combotree("clear");
	$("#inMemberName").val("");
	$("#relationName").val("");
	$("#relationId").val("");
	$("#totalAmount").val("");
	$("#freeAmount").val("");
	$("#planEnd").datebox('clear');
	//$("#goodsList").datagrid('loadData',[]);
	win.window('close');
}
$("#openBtn").click(
		function(e) {
			$(".hideOut").css("display","inline-block");
			$('#openBtn').css("display","none");
			$('#closeBtn').css("display","inline-block");
		});
$("#closeBtn").click(
		function(e) {
			$(".hideOut").css("display","none");
			$('#openBtn').css("display","inline-block");
			$('#closeBtn').css("display","none");	
		});

$('#save').click(function(e) {
	var details=getGoods();
	if(details.length==0){
		$.fool.alert({msg:'货品明细不能为空'});
		return;
	}
	var rootId=$("#inWareHouseId").combotree('tree').tree("getRoot").id;
	var inWareHouseId=$("#inWareHouseId").combotree('getValue');
	/* if(rootId==inWareHouseId){
		$.fool.alert({msg:'仓库名称不能为空'});
		return;
	} */
	var jsonuserinfo = $('#form').serializeJson();
	var obj = $.extend(jsonuserinfo,{inWareHouseId:inWareHouseId},{details:JSON.stringify(details)});
	var billDate = $("#billDate").datebox("getValue");
	var endDate = $("#planEnd").datebox("getValue");
	var planEndStr=endDate.slice(0,4)+endDate.slice(5,7)+endDate.slice(8,10);
	var billdateStr=billDate.slice(0,4)+billDate.slice(5,7)+billDate.slice(8,10);
	$('#form').form('enableValidation'); 
		if($('#form').form('validate')){ 
				if(planEndStr < billdateStr){
					$.fool.alert({msg:'计划完成日期须设定在单据日期后。',fn:function(){ 
		    		}});
					return false;	
				} 
			    $('#save').attr("disabled","disabled");
			    $.post('${ctx}/salesshipper/xsth/save',obj,function(data){
			    	if(data.returnCode =='0'){
			    		$.fool.alert({msg:'保存成功！',fn:function(){
				    		$('#addBox').window('close');
			    			$('#save').removeAttr("disabled");
			    			$('#billList').datagrid('reload');
			    		}});
			    	}else{
			    		$.fool.alert({msg:data.message});
			    		$('#save').removeAttr("disabled");
		    		}
			    	return true;
			    });
			}else{
				return false;
		} 
});

$('#copy').click(function(e) {
	var fid=$("#fid").val();
	$('#addBox').window("refresh","${ctx}/warehouse/xsth/edit?id="+fid+"&mark=1");
});
$('#verify').click(function(e) {
	var fid=$("#fid").val();
	$.fool.confirm({title:'确认',msg:'确定要审核该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/purchaseinquiry/xsth/passAudit',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {	
						if(data.returnCode == '0'){
							$.fool.alert({msg:'审核成功！',fn:function(){
								$('#addBox').window("refresh");
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
});
$('#print').click(function(e) {
		printBillDetail($("#fid").val(),'xsth');
});
$('#refreshForm').click(function(e) {
	$('#addBox').window("refresh");
});
$('#cancel').click(function(e) {
	 $.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/warehouse/xsth/cancel',
					data : {id : $("#fid").val()},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({msg:'已作废！',fn:function(){
								$("#addBox").window('close');
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
});

$.extend($.fn.validatebox.defaults.rules, {
	amount:{
          validator: function (value, param) {
           	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
           },
           message:'请输入正确的金额'
     },
});

function deleter(target){
	$("#goodsList").datagrid('deleteRow', getRowIndex(target));
	getTotal(); 
}
function editer(target){
	var inWareHouseId=$(target).closest('[field="action"]').siblings('[field="inWareHouseId"]').children().text();
	var index=getRowIndex(target);
	$("#goodsList").datagrid('beginEdit',getRowIndex(target));
	var ed = $('#goodsList').datagrid('getEditor', {index:index,field:'inWareHouseName'});
	 $(ed.target).combotree({
		onLoadSuccess:function(none){
			$($("#goodsList").datagrid('getEditor',{index:index,field:'inWareHouseName'}).target).combotree('setValue',inWareHouseId);
		}
	});
	$("#goodsList").datagrid('unselectAll');
	$("#goodsList").datagrid('selectRow',index);
	var row=$("#goodsList").datagrid('getSelected');
	if(!row.goodsSpecGroupId){
		  var editor=$("#goodsList").datagrid('getEditor',{index:index,field:'goodsSpecName'});
		  $(editor.target).combobox("destroy");
	}
	edits.push(index);
	hideOkAll();
}

function updateActions(index){
	$('#goodsList').datagrid('updateRow',{
		index: index,
		row:{}
	});
}

function getRowIndex(target){
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}

function saverow(target){
	var tarIndex=getRowIndex(target);
	$(target).closest('tr.datagrid-row').form('enableValidation');
	if($(target).closest('tr.datagrid-row').form('validate')){
		var scale=$(target).parents("[field='action']").siblings("[field='scale']").children().text();
		var unitId=$(target).parents("[field='action']").siblings("[field='unitId']").children().text();
		var goodsSpecId=$(target).parents("[field='action']").siblings("[field='goodsSpecId']").children().text();
		var inWareHouseId=$(target).parents("[field='action']").siblings("[field='inWareHouseName']").find("input.datagrid-editable-input").combotree('getValue');		
		var inWareHouseName=$(target).parents("[field='action']").siblings("[field='inWareHouseName']").find("input.datagrid-editable-input").combotree('getText');
		//更新新建标识
		getTableEditor(tarIndex,'_isNew').val('false');
		$('#goodsList').datagrid('endEdit', getRowIndex(target));
		$('#goodsList').datagrid('updateRow',{index:tarIndex,row:{scale:scale,unitId:unitId,goodsSpecId:goodsSpecId,inWareHouseId:inWareHouseId,inWareHouseId:inWareHouseId,inWareHouseName:inWareHouseName}});	
		if(getEditNumber()>0){
			btnDisabled();
		}else{
			btnEnabled();
		}
		removeEl(tarIndex);
		return true;
	}
	return false;
}
function cancelrow(target){
	var ind = $(target).fool('getRowIndex');
	var _isNew = getTableEditor(ind,'_isNew').val();
	if(_isNew=='true'||_isNew==true){
		$.fool.confirm({msg:'您确定要撤销该记录？',fn:function(r){
			if(r){ 
				$("#goodsList").datagrid('deleteRow',ind);
				removeEl(ind);
			}
		}});
	}else{
			$('#goodsList').datagrid('cancelEdit', getRowIndex(target));
			removeEl(ind);
	}
}

function comboTree(obj,url,required,onLSFn){
	obj.combotree({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		url:url,
		width:164,
		height:31,
		editable:false,
		onLoadSuccess:onLSFn
	});
}
function validateBox(obj,required,onLSFn){
	obj.validatebox({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:164,
		height:31,
		onLoadSuccess:onLSFn
	});
}

function comboBox(obj,url,required,onLSFn){
	obj.combobox({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		url:url,
		width:164,
		height:31,
		editable:false,
		onLoadSuccess:onLSFn
	});
}


function dateBox(obj,required,onLSFn){
	if(_id==''){
		obj.datebox({
			required:required,
			missingMessage:'该项不能为空！',
			novalidate:true,
			width:164,
			height:31,
			editable:false,
			value:' ',
			onLoadSuccess:onLSFn
		});
	}else{
		obj.datebox({
			required:required,
			missingMessage:'该项不能为空！',
			novalidate:true,
			width:164,
			height:31,
			editable:false,
			onLoadSuccess:onLSFn
		});
	}
}

function textBox(obj,prompt){
	obj.textbox({
		'prompt':prompt,
		width:100,
		height:30
	});
}
//获取表格里面某个编辑器方法
function getTableEditor(index,field){
	return getTableEditorHelp($("#goodsList"),index,field);
}

function getTableEditorHelp(tbId,index,field){
	var $t =$.fool._get$(tbId);
	return $t.fool('getEditor$',{'index':index,'field':field});
}
//详细页。采购员文本框
function selectMember(rowData){
	$("#inMemberName").focus();
	$("#inMemberId").val(rowData[0].fid);
	$("#inMemberName").val(rowData[0].username);
	win.window('close');
}

//详细页。货品列表 供应商文本框
function selectSupplier(rowData){
	$("#supplierName").focus();
	$("#supplierId").val(rowData[0].fid);
	$("#supplierName").val(rowData[0].name);
	$("#supplierCode").val(rowData[0].code);
	$("#supplierPhone").val(rowData[0].phone);
	win.window('close');
}
//详细页。采购员文本框
function selectMember2(rowData){
	$("#inMemberName").focus();
	$("#inMemberId").val(rowData.fid);
	$("#inMemberName").val(rowData.username);
	win.window('close');
}

//详细页。货品列表 供应商文本框
function selectSupplier2(rowData){
	$("#supplierName").focus();
	$("#supplierId").val(rowData.fid);
	$("#supplierName").val(rowData.name);
	$("#supplierCode").val(rowData.code);
	$("#supplierPhone").val(rowData.phone);
	win.window('close');
}
//点击选取销售订单方法
function selectRelation(rowData){
	$("#relationName").focus();
	$("#relationId").val(rowData[0].fid);
	$("#relationName").val(rowData[0].code);
	$("#customerName").val(rowData[0].customerName);
	$("#customerId").val(rowData[0].customerId);
	$("#customerCode").val(rowData[0].customerCode);
	$("#customerPhone").val(rowData[0].customerPhone);
	$("#deptName").combotree('setValue',rowData[0].deptId);
	$("#deptId").val(rowData[0].deptId);
	$("#inMemberName").val(rowData[0].inMemberName);
	$("#inMemberId").val(rowData[0].inMemberId);
	$("#planEnd").datebox('setValue',rowData[0].endDate);
	$("#totalAmount").val(rowData[0].totalAmount);
	$("#freeAmount").val(rowData[0].freeAmount);
	var details = eval(rowData[0].details);
	$("#goodsList").datagrid('loadData',details);
	var rows=$("#goodsList").datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		$("#goodsList").datagrid('beginEdit',i);
		if(!rows[i].goodsSpecGroupId){
			  var editor=$("#goodsList").datagrid('getEditor',{index:i,field:'goodsSpecName'});
			  $(editor.target).combobox("destroy");
		}
		edits.push(i);
	}
	win.window('close');
}
function selectRelationDBC(rowData){
	$("#relationName").focus();
	$("#relationId").val(rowData.fid);
	$("#relationName").val(rowData.code);
	$("#customerName").val(rowData.customerName);
    $("#customerId").val(rowData.customerId);
	$("#customerCode").val(rowData.customerCode);
	$("#customerPhone").val(rowData.customerPhone);
	$("#deptName").combotree('setValue',rowData.deptId);
	$("#deptId").val(rowData.deptId);
	$("#inMemberName").val(rowData.inMemberName);
	$("#inMemberId").val(rowData.inMemberId);
	$("#planEnd").datebox('setValue',rowData.endDate);
	$("#totalAmount").val(rowData.totalAmount);
	$("#freeAmount").val(rowData.freeAmount); 
	var details = eval(rowData.details);
	$("#goodsList").datagrid('loadData',details);
	var rows=$("#goodsList").datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		$("#goodsList").datagrid('beginEdit',i);
		if(!rows[i].goodsSpecGroupId){
			  var editor=$("#goodsList").datagrid('getEditor',{index:i,field:'goodsSpecName'});
			  $(editor.target).combobox("destroy");
		}
		edits.push(i);
	}
	hideOkAll();
	win.window('close');
	
}
$(".clearBill").click(function(){
	if($("#relationName").val()){
		$("#relationId").val("");
		$("#relationName").val("");
		$("#customerName").val("");
		$("#customerCode").val("");
		$("#customerPhone").val("");
		$('#deptName').combotree("clear");		 
		$("#deptId").val("");
		$("#inWareHouseId").combotree("clear");
		$("#planEnd").datebox('setValue',"");
		$("#inMemberName").val("");
		$("#inMemberId").val("");
		$("#totalAmount").val("");
		$("#freeAmount").val(""); 
		$("#goodsList").datagrid('loadData',[]);
		edits=new Array();
		hideOkAll();
	}
});
function getGoods(){
	return $("#goodsList").datagrid('getRows');
}
function btnEnabled(){
	$("#save").removeAttr('disabled');       
	$("#copy").removeAttr('disabled');
	$("#print").removeAttr('disabled');
	$("#refreshForm").removeAttr('disabled');
	$("#cancel").removeAttr('disabled');
	$("#verify").removeAttr('disabled','disabled');
	$("#save").css('background-color','#85C0EA');       
	$("#copy").css('background-color','#85C0EA');
	$("#print").css('background-color','#85C0EA');
	$("#refreshForm").css('background-color','#85C0EA');
	$("#cancel").css('background-color','#85C0EA');
	$("#verify").css('background-color','#85C0EA');
}
function btnDisabled(){
    $("#save").attr('disabled','disabled');       
	$("#copy").attr('disabled','disabled');
	$("#print").attr('disabled','disabled');
	$("#refreshForm").attr('disabled','disabled');
	$("#cancel").attr('disabled','disabled');
	$("#verify").attr('disabled','disabled');
	$("#save").css('background-color','#D4D0C8');       
	$("#copy").css('background-color','#D4D0C8');
	$("#print").css('background-color','#D4D0C8');
	$("#refreshForm").css('background-color','#D4D0C8');
	$("#cancel").css('background-color','#D4D0C8');
	$("#verify").css('background-color','#D4D0C8');
}
function getEditNumber(){
	var _dataPanel = $('#goodsList').datagrid('getPanel');
	var _editing = _dataPanel.find(".datagrid-editable");
	return _editing.length;
}
function removeEl(val){
	for(var i =0; i < edits.length;i++){
		if(edits[i]==val){
			var temp = edits[i];
			edits[i] = edits[edits.length-1];
			edits[edits.length-1] = temp;
		}
	}
	edits.pop();
	hideOkAll();
}
function allConfirm(){
	var obj = $(".datagrid-view2 .datagrid-body:eq(1) tr");
	var str = $(obj[0]).attr("id");
	var arrayStr = str.split("-");
	var datas = new Array(); 
	
	for(var i = 0;i<edits.length;i++){
	   datas.push(edits[i]); 
	}
	for(var i=0;i<datas.length;i++){
		var str = "#datagrid-row-"+arrayStr[2]+"-2-"+datas[i]+" td[field='action'] .datagrid-cell";
		if(!saverow(str)){
			   $.fool.alert({msg:"还有未填完的货品信息，请检查。"});
			   break;
		}
	}
	hideOkAll();
}
function hideOkAll(){
	if(edits.length>0){
		$("#okAll").show();
	}else{
		$("#okAll").hide();
	}
}
function goodSearch(){
	var code=$("#search-goodsCode").textbox('getValue');
	var name=$("#search-goodsName").textbox('getValue');
	var spec=$("#search-goodsSpec").textbox('getValue');
	var options = {"code":code,"name":name,"spec":spec};
	$('#goodsTable').datagrid('load',options);
}
 </script>
</body>
</html>