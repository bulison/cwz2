<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div id="goodsSpecChooser">
            <div id="goodsSpecSearcher">
              <input id="search-goodsSpecName"/> <a id="search-goodsSpecBtn" class="btn-blue btn-s">筛选</a>
            </div>
            <table id="goodsSpecTable"></table>
          </div>
          <div class="form1" >
              <form id="form">
                <input id="fid" type="hidden" value="${obj.fid}"/>
                <input id="updateTime" type="hidden" value="${obj.updateTime}"/>
                <input id="customerId" type="hidden" value="${obj.customerId}"/>
                <input id="inUserId" type="hidden" value="${obj.inMemberId}"/>
                <p><font>原始单号：</font><input id="voucherCode" class="textBox" value="${obj.voucherCode}"/></p>
				<p><font><em>*</em>单号：</font><input id="code" class="textBox" value="${obj.code}"/></p>
				<p><font><em>*</em>单据日期：</font><input id="billDate" class="textBox" value="${obj.billDate}"/></p>
				<p><font><em>*</em>客户名称：</font><input id="customerName" class="textBox" readonly="readonly" value="${obj.customerName}"/></p>
				<p><font>客户编号：</font><input id="customerCode" class="textBox" readonly="readonly" value="${obj.customerCode}"/></p>
				<p><font><em></em>客户电话：</font><input id="customerPhone" class="textBox" value="${obj.customerPhone}"/></p>
				<p><font><em>*</em>部门：</font><input id="depName" class="textBox" /></p>
				<p><font><em>*</em>业务员：</font><input id="inUserName" class="textBox" value="${obj.inMemberName}"/></p>
				<p><font><em>*</em>计划完成日期：</font><input id="planEnd" class="textBox" value="${obj.endDate}"/></p>
				<p style="width:75.8%;"><font>备注：</font><input id="describe" class="textBox" style="width:730px;" value="${obj.describe}"/></p><br/>
				<p><font>合计金额：</font><input id="totalAmount" class="textBox" readonly="readonly" value="${obj.totalAmount}"/></p>
				<p><font>免单金额：</font><input id="freeAmount" class="textBox" readonly="readonly" value="${obj.freeAmount}"/></p><br/>
				<h3 style="display: inline-block;margin-left:44px">&emsp;其他信息：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"><br/>
				<p class="hideOut"><font>制单人：</font><input id="creatorName" class="textBox" readonly="readonly" value="${obj.creatorName}"/></p>
				<p class="hideOut"><font>制单时间：</font><input id="createTime" class="textBox" readonly="readonly" value="${obj.createTime}"/></p>
				<p class="hideOut"><font>审核人：</font><input id="auditorName" class="textBox" readonly="readonly" value="${obj.auditorName}"/></p>
				<p class="hideOut"><font>审核时间：</font><input id="auditTime" class="textBox" readonly="readonly" value="${obj.auditTime}"/></p>
				<p class="hideOut"><font>作废人：</font><input id="cancelorName" class="textBox" readonly="readonly" value="${obj.cancelorName}"/></p>
				<p class="hideOut"><font>作废时间：</font><input id="cancelTime" class="textBox" readonly="readonly" value="${obj.cancelTime}"/></p>
              </form>
          </div>
          <table id="goodsList"></table>
          <div id="toolbar">
              ${(obj.recordStatus==null||obj.recordStatus==0)?' <a href="#" id="addGoods" class="btn-ora-add">新增</a> <a href="#" id="okAll" class="btn-ora-add" style="display:none">一键确认</a>':''}
              
              <!-- <a href="#" class="easyui-linkbutton" id="addGoods" data-options="iconCls:'open',plain:true">添加</a>  -->
          </div>
          <div class="form1" id="btnBox">
          </div>

<script type="text/javascript">
$("input").attr('autocomplete','off');
var chooserWindow="";
var _data="";
if($("#fid").val()){
	 _data= ${obj.details}
	 _data;
}

var unitData="";
$.ajax({
	url:"${ctx}/unitController/getLeafUnit",
	async:false,
	data:{},
	success:function(data){
		unitData=data;
	}
});
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
	validateBox($("#code"),true);
	validateBox($("#voucherCode"));
	comboTree($("#depName"),"${ctx}/orgController/getAllTree",true,function(node, data){
		if(data[0].id!=""){
			var node=$(this).tree("find",data[0].id);
			$(this).tree('update',{
				target: node.target,
				text: '请选择',
				id:null
			});
		}
		$("#depName").combotree('setValue','${obj.deptId}');
	},function(node){
		var root=$(this).tree('getRoot');
		if(node.id==root.id){
			$("#depName").combotree('clear');
		};
	});
	validateBox($("#inUserName"),true); 
	validateBox($("#describe")); 
	validateBox($("#customerName"),true); 
	validateBox($("#customerCode")); 
	validateBox($("#customerPhone")); 
	dateBox($("#billDate"),true);
	dateBox($("#planEnd"),true);
	validateBox($("#totalAmount"));
	validateBox($("#freeAmount"));
	validateBox($("#creatorName"));
	validateBox($("#createTime"));
	validateBox($("#auditorName"));
	validateBox($("#auditTime"));
	validateBox($("#cancelorName"));
	validateBox($("#cancelTime")); 
	
	textBox($("#search-goodsSpecName"),"属性名");
	
	
	if('${obj.totalAmount}'=="0E-8"){
		$("#totalAmount").val(0);
	}else{
		$("#totalAmount").val('${obj.totalAmount}');
	}
	
	if($("#fid").val()){
		$("#code").val('${obj.code}');
		$("#code").attr("readonly","readonly");
		if('${code}'){
			$("#fid").val("");
			$("#code").val('${code}');
			$("#code").attr("readonly","readonly");
		}
	}
	else if('${code}'){
		$("#code").val('${code}');
		$("#code").attr("readonly","readonly");
	}
	
	if('${obj.recordStatus}'==null||'${obj.recordStatus}'==''){
		$("#btnBox").append('<p><input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /></p>');
	}else if('${obj.recordStatus}'==0){
		$("#btnBox").append('<p><input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /></p> <fool:tagOpt optCode="xsddAction3"><p><input type="button" id="copy" class="btn-blue2 btn-xs" value="复制" /></p> </fool:tagOpt> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p>');
	}else if('${obj.recordStatus}'==1){
		$("#depName").combotree({hasDownArrow:false});
		$("#billDate").datebox('disable');
		$("#planEnd").datebox('disable');
		$("#form").find("input").attr('disabled','disabled');
		$("#addGoods").attr('disabled','disabled');
		$("#btnBox").append('<fool:tagOpt optCode="xsddAction3"><p><input type="button" id="copy" class="btn-blue2 btn-xs" value="复制" /></p> </fool:tagOpt> <fool:tagOpt optCode="xsddAction5"><input type="button" id="cancel" class="btn-blue2 btn-xs" value="作废" /></p> </fool:tagOpt> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" id="refreshForm" class="btn-blue2 btn-xs" value="刷新"/></p>');
	}else if('${obj.recordStatus}'==2){
		$("#billDate").datebox('disable');
		$("#planEnd").datebox('disable');
		$("#depName").combotree({hasDownArrow:false});
		$("#form").find("input").attr('disabled','disabled');
		$("#addGoods").attr('disabled','disabled');
		$("#btnBox").append('<fool:tagOpt optCode="xsddAction3"><p><input type="button" id="copy" class="btn-blue2 btn-xs" value="复制" /></p> </fool:tagOpt> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p>');
	}
	
		$("#goodsList").datagrid({
			singleSelect:true,
			data:_data,
			//pagination:true,
			fitColumns:true,
			toolbar:"#toolbar",
			scrollbarSize:0,
			columns:[[
				  		{field:'goodsId',title:'fid',hidden:true,width:100},
				  		{field:'barCode',title:'货品条码',sortable:true,width:100},
						{field:'goodsCode',title:'货品编号',sortable:true,width:100}, 
						{field:'goodsName',title:'货品名称',sortable:true,width:100},
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
				  		{field:'unitName',title:'单位',formatter:function(value){
					  		for(var i=0; i<unitData.length; i++){
					  			if (unitData[i].fid == value) return unitData[i].name;
					  		}
					  		return value;
						},editor:{type:'combobox',options:{
							valueField:"fid",
							textField:"name",
							required:true,
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
							    var lowestPriceField=$(this).parents("[field='unitName']").siblings("[field='lowestPrice']").children();
							    var unitPriceField=$(this).parents("[field='unitName']").siblings("[field='unitPrice']").find(".numberbox-f");
							    var goodsId=$(this).parents("[field='unitName']").siblings("[field='goodsId']").children().text();
							    var goodsSpecId="";
							    goodsSpecId=$(this).parents("[field='unitName']").siblings("[field='goodsSpecId']").children().text();
							    $(this).parents("[field='unitName']").prev().children().text(record.fid);
							    $.post("${ctx}/unitController/get",{id:record.fid},function(data){scaleField.text(data.scale)});
							    $.post("${ctx}/goods/getOtherPrice",{billType:'xsdd',fid:goodsId,unitId:record.fid,goodsSpecId:goodsSpecId,customerId:$("#customerId").val()},function(data){lowestPriceField.text(data.lowestPrice);unitPriceField.numberbox('setValue',data.referencePrice);});
							    $(this).combobox('validate');
							}}},sortable:true,width:100},
						{field:'scale',title:'换算关系',sortable:true,width:100},
						{field:'quentity',title:'数量',editor:{precision:8,type:'numberbox',options:{required:true,precision:2,validType:'positive'}},sortable:true,width:100},
						{field:'lowestPrice',title:'最低价',hidden:true,width:100},
						{field:'unitPrice',title:'单价',editor:{type:'numberbox',options:{required:true,precision:2}},sortable:true,width:100,formatter:function(value){
				  			if(value=="0E-8"){
				  				return 0;
				  			}else{
				  				return value;
				  			}
				  		}},
				  		{field:'type',title:'金额',sortable:true,width:100,formatter:function(value,row,index){
				  			if(row.quentity&&row.unitPrice){
				  				return (row.quentity*row.unitPrice).toFixed(2);
				  			}else{
				  				return 0;
				  			}
				  		}},
						{field:'describe',title:'备注',editor:"text",sortable:true,width:100},
						{field:'goodsSpecGroupId',title:'属性组ID',hidden:true,width:100},
						{field:'unitGroupId',title:'单位组ID',hidden:true,width:100},
						{field:'_isNew',hidden:true,title:"是否新增",editor:{type:'text'}},
						{field:'action',title:'操作',width:50,formatter:function(value,row,index){
				  			if (row.editing){
				  				if(getEditNumber()<=0){
					  				btnEnabled();
					  			}else{
					  				btnDisabled();
					  			}
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
				      onLoadSuccess:function(){
					    	hideOkAll();
				      },
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
								$("#goodsList").datagrid('beginEdit',index);
								hideOkAll();
							}else{
								row.editing = false;
								updateActions(index);
							}
						}
		});
		//setPager($('#goodsList'));
		//键盘控制
		keyHandler();
		
});
enterController("form");
 
$("#customerName").click(function(){
	chooserWindow=$.fool.window({'title':"选择客户",width:780,height:480,href:'${ctx}/customer/window?okCallBack=selectCus&onDblClick=selectCusDBC&singleSelect=true'}); 
});

$("#inUserName").click(function(){
	chooserWindow=$.fool.window({'title':"选择业务员",width:780,height:480,href:'${ctx}/member/window?okCallBack=selectUser&onDblClick=selectUserDBC&singleSelect=true'});
});
 
$("#addGoods").click(function(){
	chooserWindow=$.fool.window({'title':"选择货品",width:1074,height:500,href:'${ctx}/goods/window?okCallBack=selectGoods&onDblClick=selectGoodsDBC&singleSelect=false&billType=xsdd&customerId='+$("#customerId").val()});
});

function selectGoods(rowData) {
	var nodes=rowData;
	var rows="";
	var index="";
	$(nodes).each(function(){
		$("#goodsList").datagrid('appendRow',{
			goodsId:this.fid,
			barCode: this.barCode,
			goodsCode: this.code,
			goodsName: this.name,
			goodsSpec: this.spec,
			goodsSpecId:"",
			goodsSpecName: "",
			unitId:this.unitId,
			unitName: this.unitName,
			scale:this.unitScale,
			describe: this.describe,
			type:0,
			lowestPrice:this.lowestPrice,
			unitPrice:this.referencePrice,
			quentity:1,
			goodsSpecGroupId:this.goodsSpecId,
			unitGroupId:this.unitGroupId,
			_isNew:true
		});
		rows=$("#goodsList").datagrid('getRows');
		index=rows.length-1;
		$("#goodsList").datagrid('beginEdit',index);
		hideOkAll();
		if(!this.goodsSpecId){
			  var editor=$("#goodsList").datagrid('getEditor',{index:index,field:'goodsSpecName'});
			  $(editor.target).combobox("destroy");
		};
		var unitPriceEd=$($("#goodsList").datagrid('getEditor',{index:index,field:'unitPrice'}).target);
		var str='$("#goodsList").datagrid("getPanel").panel("panel").find(".datagrid-view2").find(".datagrid-body").find(".datagrid-row[datagrid-row-index='+index+']").find("[field=\'lowestPrice\']")';
		unitPriceEd.numberbox({required:true,precision:2,validType:'goodPrice['+str+']'});
	});
	getTotal();
	chooserWindow.window('close');
}
function selectGoodsDBC(rowData) {
	$("#goodsList").datagrid('appendRow',{
		goodsId:rowData.fid,
		barCode: rowData.barCode,
		goodsCode: rowData.code,
		goodsName: rowData.name,
		goodsSpec: rowData.spec,
		goodsSpecId:"",
		goodsSpecName: "",
		unitId:rowData.unitId,
		unitName: rowData.unitName,
		scale:rowData.unitScale,
		describe: rowData.describe,
		type:0,
		lowestPrice:rowData.lowestPrice,
		unitPrice:rowData.referencePrice,
		quentity:1,
		goodsSpecGroupId:rowData.goodsSpecId,
		unitGroupId:rowData.unitGroupId,
		_isNew:true
	});
	getTotal();
    var rows=$("#goodsList").datagrid('getRows');
    var index=rows.length-1;
    $("#goodsList").datagrid('beginEdit',index);
    hideOkAll();
    if(!rowData.goodsSpecId){
    	var editor=$("#goodsList").datagrid('getEditor',{index:index,field:'goodsSpecName'});
	    $(editor.target).combobox("destroy");
    }
    var unitPriceEd=$($("#goodsList").datagrid('getEditor',{index:index,field:'unitPrice'}).target);
    var str='$("#goodsList").datagrid("getPanel").panel("panel").find(".datagrid-view2").find(".datagrid-body").find(".datagrid-row[datagrid-row-index='+index+']").find("[field=\'lowestPrice\']")';
	unitPriceEd.numberbox({required:true,precision:2,validType:'goodPrice['+str+']'});
    chooserWindow.window('close');
}

$("#search-goodsSpecBtn").click(function(){
	var name=$("#search-goodsSpecName").textbox('getValue');
	var options = {"name":name};
	$('#goodsSpecTable').datagrid('load',options);
});

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
	var _dataPanel = $('#goodsList').datagrid('getPanel');
	var _editing = _dataPanel.find(".datagrid-editable");
	if(_editing.length>0){
		$.fool.alert({time:1000,msg:'你还有没编辑完成的货品,请先确认！'});
		return false;
	};
	var rows=$('#goodsList').datagrid("getRows");
	if(rows.length<1){
		$.fool.alert({time:1000,msg:'请至少添加一条商品信息！'});
		return false;
	}
	var details=getGoods();
	var id=$("#fid").val();
	var updateTime=$("#updateTime").val();
	var voucherCode=$("#voucherCode").val();
	var code=$("#code").val();
	var billDate=$("#billDate").datebox('getValue');
	var customerId=$("#customerId").val();
	var deptId=$("#depName").combotree('getValue');
	var inMemberId=$("#inUserId").val();
	var endDate=$("#planEnd").datebox('getValue');
	var describe=$("#describe").val();
	var totalAmount=$("#totalAmount").val();
	var customerPhone=$("#customerPhone").val();
	
	var planEndStr=endDate.slice(0,4)+endDate.slice(5,7)+endDate.slice(8,10);
	var billdateStr=billDate.slice(0,4)+billDate.slice(5,7)+billDate.slice(8,10);;
	$('#form').form('enableValidation'); 
		if($('#form').form('validate')){
			if(planEndStr<billdateStr){
				$.fool.alert({time:1000,msg:'计划完成日期须设定在单据日期后。',fn:function(){ 
	    		}});
				return false;
			}
			$('#save').attr("disabled","disabled");
			    $.post('${ctx}/salebill/xsdd/save',{details:JSON.stringify(details),fid:id,updateTime:updateTime,voucherCode:voucherCode,code:code,billDate:billDate,customerId:customerId,deptId:deptId,inMemberId:inMemberId,endDate:endDate,describe:describe,totalAmount:totalAmount,customerPhone:customerPhone},function(data){
			    	if(data.returnCode=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			$('#addBox').window('close');
			    			$('#save').removeAttr("disabled");
			    			$('#billList').datagrid('reload');
			    		}});
			    	}else{
			    		$.fool.alert({time:1000,msg:data.message});
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
	$('#addBox').window("refresh","${ctx}/salebill/xsdd/edit?id="+fid+"&mark=1");

});

$('#verify').click(function(e) {
	 $.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/salebill/xsdd/passAudit',
					data : {id : $("#fid").val()},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
								$('#addBox').window("refresh");
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
});

$('#print').click(function(e) {
	printBillDetail($("#fid").val(),'xsdd');
});
$('#refreshForm').click(function(e) {
	$('#addBox').window("refresh");
});
$('#cancel').click(function(e) {
	$.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/salebill/xsdd/cancel',
					data : {id : $("#fid").val()},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
								$('#billList').datagrid('reload');
								$('#addBox').window("refresh");
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
								$('#addBox').window("refresh");
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
});

$.extend($.fn.validatebox.defaults.rules, {
    positive:{//判断是否大于0
    	validator:function(value,param){
    		return parseFloat(value)>0;
    	},
    	message:'输入必须大于0'
    },
    goodPrice:{//验证货品单价
        validator: function (value,param) {
        	if(value.length==0)return false;
        	var lowestPrice=$(param[0]).children().text();
        	param[1]=lowestPrice;
			if(parseFloat(value)<parseFloat(lowestPrice)){
				return false;
			}else{
				return true;
			}
			return false;
         },
         message:'单价金额不能低于最低销售价{1}元',
     },
     unit:{//判断有没有选单位
     	validator:function(value,param){
     		var ed=$('#goodsList').datagrid('getEditor', {index:param,field:'unitName'});
     		var text=$(ed.target).combobox("getText");
     		var unitId=$(ed.target).parents("[field='unitName']").siblings("[field='unitId']").children().text();
 			var name="";
 			for(var i=0; i<unitData.length; i++){
 	  			if (unitData[i].fid == unitId){
 	  				name=unitData[i].name;
 	  			}
 	  		}
     		return name==text;
     	},
     	message:'请选择货品单位'
     },
     goodsSpec:{
     	validator:function(value,param){
     		var ed=$('#goodsList').datagrid('getEditor', {index:param,field:'goodsSpecName'});
     		var text=$(ed.target).combobox("getText");
     		var unitId=$(ed.target).parents("[field='goodsSpecName']").siblings("[field='goodsSpecId']").children().text();
 			var name="";
 			for(var i=0; i<goodsSpecData.length; i++){
 	  			if (goodsSpecData[i].fid == unitId){
 	  				name=goodsSpecData[i].name;
 	  			}
 	  		}
     		return name==text;
     	},
     	message:'请选择货品属性'
     }
});

function deleter(target){
	$("#goodsList").datagrid('deleteRow', getRowIndex(target));
	getTotal(); 
}
function editer(target){
	var index=getRowIndex(target);
	$("#goodsList").datagrid('beginEdit',getRowIndex(target));
	hideOkAll();
	$("#goodsList").datagrid('selectRow',index);
	var row=$("#goodsList").datagrid('getSelected');
	if(!row.goodsSpecGroupId){
		  var editor=$("#goodsList").datagrid('getEditor',{index:index,field:'goodsSpecName'});
		  $(editor.target).combobox("destroy");
	}
	var str='$("#goodsList").datagrid("getPanel").panel("panel").find(".datagrid-view2").find(".datagrid-body").find(".datagrid-row[datagrid-row-index='+index+']").find("[field=\'lowestPrice\']")';
	$($("#goodsList").datagrid('getEditor',{index:index,field:'unitPrice'}).target).numberbox({required:true,precision:2,validType:'goodPrice['+str+']'});
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
	var unitPrice=$(target).parents("[field='action']").siblings("[field='unitPrice']").find("input.textbox-value").val();
	if(!unitPrice){
		$('#goodsList').datagrid('selectRow', getRowIndex(target));
		var row=$('#goodsList').datagrid('getSelected');
		unitPrice=row.unitPrice;
	}
		var tarIndex=getRowIndex(target);
		$(target).closest('tr.datagrid-row').form('enableValidation');
		if($(target).closest('tr.datagrid-row').form('validate')){
			var scale=$(target).parents("[field='action']").siblings("[field='scale']").children().text();
			var unitId=$(target).parents("[field='action']").siblings("[field='unitId']").children().text();
			var goodsSpecId=$(target).parents("[field='action']").siblings("[field='goodsSpecId']").children().text();
			getTableEditor(tarIndex,'_isNew').val('false');
			$('#goodsList').datagrid('endEdit', getRowIndex(target));
			hideOkAll();
			$('#goodsList').datagrid('updateRow',{index:tarIndex,row:{scale:scale,unitId:unitId,goodsSpecId:goodsSpecId}});
			if(getEditNumber()>0){
				btnDisabled();
			}else{
				btnEnabled();
			}
		}
}
function cancelrow(target){
	var ind = $(target).fool('getRowIndex');
	var _isNew = getTableEditor(ind,'_isNew').val();
	if(_isNew=='true'||_isNew==true){
		$.fool.confirm({msg:'您确定要撤销该记录？',fn:function(r){
			if(r){
				$("#goodsList").datagrid('deleteRow',ind);
			    hideOkAll();
			}
		}});
	}else{
		$('#goodsList').datagrid('cancelEdit', getRowIndex(target));
		hideOkAll();
	}
}

//获取表格里面某个编辑器方法
function getTableEditor(index,field){
	return getTableEditorHelp($("#goodsList"),index,field);
}

function getTableEditorHelp(tbId,index,field){
	var $t =$.fool._get$(tbId);
	return $t.fool('getEditor$',{'index':index,'field':field});
}

function getEditNumber(){
	var _dataPanel = $('#goodsList').datagrid('getPanel');
	var _editing = _dataPanel.find(".datagrid-editable");
	return _editing.length;
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

function validateBox(obj,required,onLSFn){
	obj.validatebox({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:160,
		height:32,
		onLoadSuccess:onLSFn
	});
}

function comboBox(obj,url,required,onLSFn){
	obj.combobox({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		url:url,
		width:160,
		height:32,
		editable:false,
		onLoadSuccess:onLSFn
	});
}

function comboTree(obj,url,required,onLSFn,onStFn){
	obj.combotree({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		url:url,
		width:160,
		height:32,
		editable:false,
		onLoadSuccess:onLSFn,
		onSelect:onStFn
	});
}

function dateBox(obj,required,valid,onLSFn){
	if(!valid){valid="";};
	obj.datebox({
		required:required,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:160,
		height:32,
		editable:false,
		validType:valid,
		onLoadSuccess:onLSFn
	});
}

function textBox(obj,prompt){
	obj.textbox({
		'prompt':prompt,
		width:160,
		height:32,
	});
}

function getGoods(){
	return $("#goodsList").datagrid('getRows');
}

function selectCus(rowData){
	$("#customerName").focus();
	$("#customerId").val(rowData[0].fid);
	$("#customerName").val(rowData[0].name);
	$("#customerCode").val(rowData[0].code);
	$("#customerPhone").val(rowData[0].phone);
	chooserWindow.window('close');
}
function selectCusDBC(rowData){
	$("#customerName").focus();
	$("#customerId").val(rowData.fid);
	$("#customerName").val(rowData.name);
	$("#customerCode").val(rowData.code);
	$("#customerPhone").val(rowData.phone);
	chooserWindow.window('close');
}

function selectUser(rowData){
	$("#inUserName").focus();
	$("#inUserId").val(rowData[0].fid);
	$("#inUserName").val(rowData[0].username);
	chooserWindow.window('close');
}
function selectUserDBC(rowData){
	$("#inUserName").focus();
	$("#inUserId").val(rowData.fid);
	$("#inUserName").val(rowData.username);
	chooserWindow.window('close');
}
function hideOkAll(){
	var _dataPanel = $('#goodsList').datagrid('getPanel');
	var _editing = _dataPanel.find(".datagrid-editable");
	if(_editing.length>0){
		$("#okAll").show();
	}else{
		$("#okAll").hide();
	}
}

$("#okAll").click(function(){
	var _dataPanel = $('#goodsList').datagrid('getPanel');
	var editingRows=_dataPanel.find(".datagrid-view").find(".datagrid-view2").find(".datagrid-body").find(".datagrid-row-editing");
	var index="";
	for(var i=0;i<editingRows.length;i++){
		$(editingRows[i]).form('enableValidation');
		if($(editingRows[i]).form('validate')){
			index=$(editingRows[i]).attr("datagrid-row-index");
			$('#goodsList').datagrid('endEdit', index);
			hideOkAll();
		}else{
			$.fool.alert({time:1000,msg:"还有未填完的货品信息，请检查。"});
			return false;
		}
	}
});
 </script>
</body>
</html>