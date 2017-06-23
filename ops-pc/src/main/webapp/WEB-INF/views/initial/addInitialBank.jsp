<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div class="form1" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${entity.fid}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${entity.updateTime}"/>  
				<p><font><em>*</em>银行：</font><input id="bankName" type="text" class="textBox"/></p>
				<p><font><em>*</em>结余金额：</font><input id="amount" type="text" class=" easyui-validatebox textBox" value="${entity.amount}" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,validType:['amount','numMaxLength[10]']" /></p>
				<!--<p><font><em>*</em>会计期间：</font><input id="periodName" type="text" class="textBox" /></p>-->
				<p><font>描述：</font><input id=describe type="text" class=" easyui-validatebox textBox" value="${entity.describe}" data-options="novalidate:true,validType:'maxLength[200]'" /></p><br/>
				<p style="margin-left:65px"><font><input id="save" type="button" class="btn-blue2 btn-xs" value="保存"/></font></p> 
              </form>
          </div>

<script type="text/javascript">
var newPeriod=[{fid:'01',period:'<a href="${ctx}/periodController/periodManager">啥也没有，去新增一条吧！</a>'}];
var newBank=[{fid:'01',period:'<a href="${ctx}/bankController/bankManager">啥也没有，去新增一条吧！</a>'}];
$(function(){
	$("input").attr('autocomplete','off');
	/*$("#periodName").combobox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,width:167,
		height:31,
		editable:false,
		valueField: 'fid',
		textField: 'period',
		url:"${ctx}/periodController/comboboxData",
		onLoadSuccess:function(){
			var data=$("#periodName").combobox('getData');
			if(data==""){
				$("#periodName").combobox('loadData',newPeriod);
			}else{
				$("#periodName").combobox('select','${entity.periodId}');
			}
		}
	});*/
	$("#amount").bind("change",function(e){
		if(!isNaN($(this).val())){
			var nv = $(this).val()+"";
			value = parseFloat(nv).toFixed(2)+'';
			if(value == "NaN"){
			    return "";
            }
			$(this).val(value);
		}
	});
 	/* $("#bankName").combobox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:167,
		height:31,
		editable:false,
		valueField: 'fid',
		textField: 'name', 
		url:"${ctx}/bankController/comboboxData",
		onLoadSuccess:function(){
			var data=$("#bankName").combobox('getData');
			if(data==""){
				$("#bankName").combobox('loadData',newBank);
			}else{
				$("#bankName").combobox('select','${entity.bankId}');
			}
		}
	});  */
	//银行
 	var bankValue='';	
	$.ajax({
		url:"${ctx}/bankController/comboboxData?num="+Math.random(),
		async:false,		
		success:function(data){		  	
			bankValue=formatData(data,'fid');	
	    }
		});
	var bankName= $("#bankName").fool("dhxCombo",{
		  width:160,
		  height:32,
		  data:bankValue,
        clearOpt:false,
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
        toolsBar:{
            name:"现金银行",
            addUrl:"/bankController/bankManager",
            refresh:true
        },
		  required:true,
		  novalidate:true,
		  editable:false,
		  focusShow:true,
		  onLoadSuccess:function(combo){
				combo.setComboValue("${entity.bankId}");
			} 
	});	 
});
 
$('#save').click(function(e) {
	var fid=$("#fid").val();
	//var bankId=bankName.getSelectedValue();//获取控件值
	var bankId=$("[name='myCombo']").val();
	var amount=$("#amount").val();
	var describe=$("#describe").val();
	var updateTime=$("#updateTime").val();

	$('#form').form('enableValidation'); 
		if($('#form').form('validate')){
			$('#save').attr("disabled","disabled");
			    $.post('${ctx}/initialBankController/save',{fid:fid,bankId:bankId,amount:amount,describe:describe,updateTime:updateTime},function(data){
			    	dataDispose(data);
			    	if(data.result=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			$('#addBox').window('close');
			    			$('#bankList').trigger('reloadGrid');
			    			$('#save').removeAttr("disabled");
			    		}});
			    	}else if(data.result=='1'){
			    		if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
			    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.msg,fn:function(){
			    			}});
			    		}else{
				    		$.fool.alert({msg:data.msg,fn:function(){
				    		}});
			    		}
			    		$('#save').removeAttr("disabled");
			    	}else {
			    		$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
			    			$('#bankList').trigger('reloadGrid');
			    			$('#save').removeAttr("disabled");
			    		}});
			    	}
			    	return true;
			    });
			}else{
				return false;
				}
});

$.extend($.fn.validatebox.defaults.rules, {
	amount:{
          validator: function (value, param) {
           	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
           },
           message:'请输入正确的金额'

        },
});
 </script>
</body>
</html>