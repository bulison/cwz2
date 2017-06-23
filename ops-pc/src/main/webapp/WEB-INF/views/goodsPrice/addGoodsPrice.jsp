<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
<style>
#form font{
	width:200px;
}
.form1 p.hideOut{
    display: none; 
}
</style>
          <div class="form1" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${entity.fid}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${entity.updateTime}"/>
                <input name="goodsId" id="goodsId" type="hidden" value="${entity.goodsId}"/> 
                <input name="_goodsName" id="_goodsName" type="hidden" value="${entity.goodsName}"/> 
				<input name="unitGroupId" id="unitGroupId" type="hidden" value="${unitGroupId}"/>
				<input name="goodsSpecGroupId" id="goodsSpecGroupId" type="hidden" value="${goodsSpecGroupId}"/>
				<p><font><em>*</em>货品：</font><input id="goodsName" type="text" class="textBox" /></p>
				<p><font>编码：</font><input id="goodsCode" type="text" class="textBox" disabled="disabled" value="${entity.goodsCode}"/></p>
				<p><font>条码：</font><input id="barCode" type="text" class="textBox" disabled="disabled" value="${entity.barCode}"/></p>
				<p><font>规格：</font><input id="spec" type="text" class="textBox" disabled="disabled" value="${entity.spec}"/></p>
				<p><font><em>*</em>属性：</font><input id=goodsSpecName type="text" class="textBox" /></p>
				<p><font>单位：</font><input id="unitName" type="text" class="textBox"/></p>
				<p class="price"><font><em>*</em>销售单价：</font><input id="salePrice" type="text" value="${entity.salePrice}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,validType:['amount','numMaxLength[10]']" /></p>
				<p class="price"><font><em>*</em>最低售价：</font><input id="lowestPrice" type="text" value="${entity.lowestPrice}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,validType:['amount','numMaxLength[10]','lowestPrice']" /></p>
				<p class="price"><font>会员价：</font><input id="vipPrice" type="text" value="${entity.vipPrice}" class=" easyui-validatebox textBox" data-options="validType:['amount','vipPrice','numnumMaxLength[10]']"/></p>
				<br/>
				<h3 style="display: inline-block;margin-left:20px">&emsp;其他定价：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none">
				<br/>
				<p class="price sale hideOut"><font>销售一级价下限：</font><input id="salesLowerLimit1" type="text" value="${entity.salesLowerLimit1}" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/></p>
				<p class="price sale hideOut"><font>销售二级价下限：</font><input id="salesLowerLimit2" type="text" value="${entity.salesLowerLimit2}" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/></p>
				<p class="price sale hideOut"><font>销售一级价上限：</font><input id="salesUpperLimit1" type="text" value="${entity.salesUpperLimit1}" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/></p>
				<p class="price sale hideOut"><font>销售二级价上限：</font><input id="salesUpperLimit2" type="text" value="${entity.salesUpperLimit2}" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/></p>
				<p class="price buy hideOut"><font>采购一级价下限：</font><input id="purchaseLowerLimit1" type="text" value="${entity.purchaseLowerLimit1}" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/></p>
				<p class="price buy hideOut"><font>采购二级价下限：</font><input id="purchaseLowerLimit2" type="text" value="${entity.purchaseLowerLimit2}" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/></p>
				<p class="price buy hideOut"><font>采购一级价上限：</font><input id="purchaseUpperLimit1" type="text" value="${entity.purchaseUpperLimit1}" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/></p>
				<p class="price buy hideOut"><font>采购二级价上限：</font><input id="purchaseUpperLimit2" type="text" value="${entity.purchaseUpperLimit2}" class=" easyui-validatebox textBox" data-options="validType:['amount','numMaxLength[10]']"/></p>
				<p class="hideOut"><font>最低库存数：</font><input id="lowestStock" type="text" value="${entity.lowestStock}" class=" easyui-validatebox textBox" data-options="validType:['myamount','numMaxLength[10]']"/></p>
				<p class="hideOut"><font>最高库存数：</font><input id="heightestStock" type="text" value="${entity.heightestStock}" class=" easyui-validatebox textBox" data-options="validType:['myamount','maxStock','numMaxLength[10]']"/></p>
				<p class="hideOut"><font>采购周期（天）：</font><input id="purchasingCycle" type="text" value="${entity.purchasingCycle}" class=" easyui-validatebox textBox" data-options="validType:['myamount','numMaxLength[10]']"/></p>
				<p class="hideOut"><font>生产产能（计量数量/天）：</font><input id="capacity" type="text" value="${entity.capacity}" class=" easyui-validatebox textBox" data-options="validType:['myamount','numMaxLength[10]']"/></p>
				<br><p style="margin-left:450px"><font><input id="save" type="button" class="btn-blue2 btn-xs" value="保存"/></font></p>
              </form>
          </div>

<script type="text/javascript">
var unit_ = '';
	//保留2位小数
	$('.price input').each(function(){
		var a = $(this).val();
		a!=''?$(this).val(parseFloat(a).toFixed(4)):null;
		$(this).blur(function(){
			var val = $(this).val();
			!isNaN($.trim(val))&&$.trim(val)!=''?
			$(this).val(parseFloat(val).toFixed(4)):null;
		});
	});
	$("input").attr('autocomplete','off');
	
	var unitCombo=$("#unitName").fool('dhxCombo',{
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:160,
		height:31,
		editable:false,
		setTemplate:{
    		input:"#name#",
			option:"#name#"
    	},
    	data:getComboData("${ctx}/unitController/getPartTree?unitGroupId="+$("#unitGroupId").val(),"tree"),
		onLoadSuccess:function(){
			if('${entity.unitId}'){
				var value='${entity.unitId}';
				($("#unitName").next())[0].comboObj.setComboValue(value);
			}
			if(unit_ != ''){
				($("#unitName").next())[0].comboObj.setComboValue(unit_);
				unit = '';
			}
		},
	});
	unitCombo.disable();
	
	var goodsCombo = $('#goodsName').fool('dhxComboGrid',{
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
    	width:160,
    	height:31,
    	focusShow:true,
    	text:'${entity.goodsName}',
    	/* value:'${entity.goodsId}', */
    	data:getComboData(getRootPath()+'/goods/vagueSearch?searchSize=8'),
    	validType:["combogridValid['goodsId']","nocobgValid['_goodsName']"],
    	filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=8',
    	setTemplate:{
    		input:"#name#",
    		columns:[
    					{option:'#code#',header:'编号',width:100},
    					{option:'#name#',header:'名称',width:100},
    				],
    	},
    	toolsBar:{
			name:"货品",
			addUrl:"/goods/manage",
			refresh:true
		},
    	onSelectionChange:function(){
    		var row=($('#goodsName').next())[0].comboObj.getSelectedText();
    		if(!row){
    			return false;
    		}
			var id=row.fid;
			$('#goodsId').val(id);
			$('#_goodsName').val(row.name);
			$('#goodsCode').val(row.code);
			$('#barCode').val(row.barCode);
			$('#spec').val(row.spec);
			$("#unitGroupId").val(row.unitGroupId);
			unit_ = row.unitId;
			($("#unitName").next())[0].comboObj.clearAll(false);
			($("#unitName").next())[0].comboObj.addOption(getComboData("${ctx}/unitController/getPartTree?unitGroupId="+$("#unitGroupId").val(),"tree"));
			($("#unitName").next())[0].comboObj.setComboValue(unit_);
			if(($("#goodsSpecName").next())[0]){
				if(row.goodsSpecGroupId){
					$("#goodsSpecName").prev().find('em').html('*');
					goodsSpecCombo.enable();
					$(goodsSpecCombo.getInput()).validatebox({required:true});
					$("#goodsSpecGroupId").val(row.goodsSpecGroupId);
					goodsSpecCombo.setComboText(""); 
					goodsSpecCombo.clearAll(false);
					goodsSpecCombo=$("#goodsSpecName").fool('dhxComboGrid',{
						required:true,
						missingMessage:'该项不能为空！',
						novalidate:true,
						width:160,
				    	height:31,
				    	data:getComboData("${ctx}/goodsspec/vagueSearch?parentId="+$("#goodsSpecGroupId").val()),
				    	filterUrl:"${ctx}/goodsspec/vagueSearch?parentId="+$("#goodsSpecGroupId").val(),
				    	setTemplate:{
				    		input:"#name#",
				    		columns:[
				    					{option:'#code#',header:'编号',width:100},
				    					{option:'#name#',header:'名称',width:100},
				    				],
				    	},
				    	toolsBar:{
							name:"货品属性",
							addUrl:"/goodsspec/manage",
							refresh:true
						},
					});
				}else{
					$("#goodsSpecName").prev().find('em').html('');
					goodsSpecCombo.setComboValue("");  
					goodsSpecCombo.setComboText("");  
					goodsSpecCombo.disable();
					$(goodsSpecCombo.getInput()).validatebox({required:false});
					$("#goodsSpecGroupId").val('');
				}
			}
    	}
    });
	var goodsSpecData="";
	if($("#goodsSpecGroupId").val()){
		goodsSpecData=getComboData("${ctx}/goodsspec/vagueSearch?parentId="+$("#goodsSpecGroupId").val());
	}/* else{
		goodsSpecData=getComboData("${ctx}/goodsspec/getTree","tree");
	} */
	var goodsSpecCombo=$("#goodsSpecName").fool('dhxComboGrid',{
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
    	width:160,
    	height:31,
    	focusShow:true,
    	data:goodsSpecData,
    	filterUrl:"${ctx}/goodsspec/vagueSearch?parentId="+$("#goodsSpecGroupId").val(),
    	setTemplate:{
    		input:"#name#",
    		columns:[
    					{option:'#code#',header:'编号',width:100},
    					{option:'#name#',header:'名称',width:100},
    				],
    	},
    	toolsBar:{
			name:"货品属性",
			addUrl:"/goodsspec/manage",
			refresh:true
		},
    	/* text:"${entity.goodsSpecName}", */
    	value:"${entity.goodsSpecId}",
		onLoadSuccess:function(){
			/* ($("#goodsSpecName").next())[0].comboObj.setComboValue("${entity.goodsSpecId}");
			($("#goodsSpecName").next())[0].comboObj.setComboText("${entity.goodsSpecName}"); */
		},
	});
	if(!'${entity.goodsSpecId}'){
		$("#goodsSpecName").prev().find('em').html('');
		goodsSpecCombo.disable();
	}
$.extend($.fn.validatebox.defaults.rules, {
	myamount:{//验证金额，小数点后两位
        validator: function (value, param) {
        	if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
        		if($(this).attr("class").indexOf("textbox-f")!=-1){
          			$(this).textbox("textbox").blur(function(){//对数字进行去空格和自动保留2位小数
          				if(!isNaN($.trim(value)) && $.trim(value)!=""){
        					var nv = $.trim(value)+"";
        					var myvalue = parseFloat(nv).toFixed(2)+'';
        					$(this).textbox("setText",myvalue);
        				}else{
        					$(this).textbox("setText","");
        				}
          			});
          		}else if($(this).attr("class").indexOf("validatebox-text")!=-1){
          			$(this).blur(function(){//对数字进行去空格和自动保留2位小数
          				if(!isNaN($.trim(value)) && $.trim(value)!=""){
        					var nv = $.trim(value)+"";
        					var myvalue = parseFloat(nv).toFixed(2)+'';
        					$(this).val(myvalue);
        				}else{
        					$(this).val("");
        				}
          			});
          		}
        	}
         	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
         },
         message:'请输入正数，最多2位小数'

     },
	amount:{
          validator: function (value, param) {
           	return (/^(([1-9]\d*)|\d)(\.\d{1,8})?$/).test(value);
           },
           message:'请输入正确的金额,精确到小数点后八位。'

	},
    integ:{
        	validator: function (value, param) {
               	return (/^(([1-9]\d*)|\d)(\.\d{1,8})?$/).test(value);
               },
               message:'请输入正确的库存数,精确到小数点后八位。'
    },
    integ1:{
    	validator: function (value, param) {
           	return (/^(([1-9]\d*)|\d)(\.\d{1,8})?$/).test(value);
           },
           message:'请输入正数,精确到小数点后八位。'
    },
    maxStock:{
    	validator: function (value, param) {
           	return parseFloat(value) >= parseFloat($('#lowestStock').val());
           },
        message:'最低库存数不可大于最高库存数。'
    },
    lowestPrice:{
    	validator: function (value, param) {
           	return parseFloat(value) <= parseFloat($('#salePrice').val());
           },
        message:'最低售价必须低于销售价。'
    },
    vipPrice:{
    	validator: function (value, param) {
           	return parseFloat(value) >= parseFloat($('#lowestPrice').val())&&parseFloat(value) <= parseFloat($('#salePrice').val());
           },
        message:'会员售价必须高于最低售价，低于售价。'
    },
    nocobgValid: {    
        validator: function(value,param){
            return $('#'+param).val()==$(this).val();    
        },    
        message: '该项已经被修改，但并未选中，请重新选择'   
    },
});
 
$('#save').click(function(e) {
	$('#form').form('enableValidation'); 
	if($('#form').form('validate')){
		if($('#purchaseLowerLimit1').val()!=''||$('#purchaseLowerLimit2').val()!=''||$('#purchaseUpperLimit2').val()!='')
			$('#purchaseUpperLimit1').validatebox({
				required:true,
				missingMessage:'若设置了采购限制价，采购一级上限为必填'
			});
		var fid=$("#fid").val();
		var goodsId=$("#goodsId").val();
		var goodsSpecId="";
		if($("#goodsSpecGroupId").val()){
			goodsSpecId=($("#goodsSpecName").next())[0].comboObj.getSelectedValue();/* $("#goodsSpecName").combobox('getValue'); */
		}
		var unitId=($("#unitName").next())[0].comboObj.getSelectedValue();/* $("#unitName").combobox('getValue'); */
		var salePrice=$("#salePrice").val();
		var lowestPrice=$("#lowestPrice").val();
		var lowestStock=$("#lowestStock").val();
		var heightestStock=$("#heightestStock").val();
		var updateTime=$("#updateTime").val();
		
		$("#vipPrice").val()==''?$("#vipPrice").val(salePrice):null;
		var vipPrice=$("#vipPrice").val();
		$('#salesLowerLimit1').val()==''?$('#salesLowerLimit1').val(vipPrice):null;
		var salesLowerLimit1=$('#salesLowerLimit1').val();
		$('#salesLowerLimit2').val()==''?$('#salesLowerLimit2').val(salesLowerLimit1):null;
		var salesLowerLimit2=$('#salesLowerLimit2').val();
		$('#salesUpperLimit1').val()==''?$('#salesUpperLimit1').val(salePrice):null;
		var salesUpperLimit1=$('#salesUpperLimit1').val();
		$('#salesUpperLimit2').val()==''?$('#salesUpperLimit2').val(salesUpperLimit1):null;
		var salesUpperLimit2=$('#salesUpperLimit2').val();
		var purchaseUpperLimit1=$('#purchaseUpperLimit1').val();
		$('#purchaseLowerLimit1').val()==''?$('#purchaseLowerLimit1').val(purchaseUpperLimit1):null;
		var purchaseLowerLimit1=$('#purchaseLowerLimit1').val();
		$('#purchaseLowerLimit2').val()==''?$('#purchaseLowerLimit2').val(purchaseLowerLimit1):null;
		var purchaseLowerLimit2=$('#purchaseLowerLimit2').val();
		$('#purchaseUpperLimit2').val()==''?$('#purchaseUpperLimit2').val(purchaseUpperLimit1):null;
		var purchaseUpperLimit2=$('#purchaseUpperLimit2').val();
		var purchasingCycle=$('#purchasingCycle').val();
		var capacity=$('#capacity').val();
		
			$('#save').attr("disabled","disabled");
			    $.post('${ctx}/goodsPriceController/save',{purchasingCycle:purchasingCycle,capacity:capacity,
			    	purchaseUpperLimit1:purchaseUpperLimit1,purchaseUpperLimit2:purchaseUpperLimit2,purchaseLowerLimit1:purchaseLowerLimit1,purchaseLowerLimit2:purchaseLowerLimit2,
			    	salesUpperLimit1:salesUpperLimit1,salesUpperLimit2:salesUpperLimit2,salesLowerLimit1:salesLowerLimit1,salesLowerLimit2:salesLowerLimit2,
			    	fid:fid,goodsId:goodsId,goodsSpecId:goodsSpecId,unitId:unitId,salePrice:salePrice,lowestPrice:lowestPrice,vipPrice:vipPrice,lowestStock:lowestStock,heightestStock:heightestStock,updateTime:updateTime},function(data){
			    		dataDispose(data);
			    		if(data.result=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			$('#addBox').window('close');
			    			$("#goodsPriceList").trigger("reloadGrid"); 
			    			/* $('#goodsPriceList').datagrid('reload'); */
			    			$('#save').removeAttr("disabled");
			    		}}); 
			    	}else if(data.result=='1'){
			    		$.fool.alert({msg:data.msg});
			    		$('#save').removeAttr("disabled");
			    	}
			    	return true;
			    });
			}else{
				return false;
				}
});
function setRequired(target,clazz){
	if($(target).val()){
		$(clazz).find("input").validatebox({
			required:true,
			missingMessage:"该项为必输项",
		});
	}else{
		var inputs=$(clazz).find("input");
		for(var i=0;i<inputs.length;i++){
			if($(inputs[i]).val()){
				return false;
			}
		}
		$(clazz).find("input").validatebox({
			required:false,
			missingMessage:"该项为必输项",
		});
	}
}
function initRequired(clazz){
	var inputs=$(clazz).find("input");
	for(var i=0;i<inputs.length;i++){
		if($(inputs[i]).val()){
			return false;
		}
	}
	$(clazz).find("input").validatebox({
		required:false,
		missingMessage:"该项为必输项",
	});
}
$("#openBtn").click(function(e) {
	$(".hideOut").css("display","inline-block");
	$('#openBtn').css("display","none");
	$('#closeBtn').css("display","inline-block");
});
$("#closeBtn").click(function(e) {
	$(".hideOut").css("display","none");
	$('#openBtn').css("display","inline-block");
	$('#closeBtn').css("display","none");	
});

function getComboData(url,mark){
	var result="";
	$.ajax({
		url:url,
		data:{num:Math.random()},
		async:false,
		success:function(data){
			if(data){
				result=data;
			}
		}
	});
	if(mark!="tree"){
		return formatData(result,"fid");
	}else{
		return formatTree(result[0].children,"text","id");
	}
}
 </script>
</body>
</html>