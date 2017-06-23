<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div class="form1" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${billRule.fid}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${billRule.updateTime}"/>  
				<%-- <p><font><em>*</em>单据名称：</font><input id="billName" class="textBox" value="${billRule.billName}"/></p> --%>
				<p><font><em>*</em>单据类型：</font><input id="billType" class="textBox" /></p>
				<p><font><em>*</em>规则类型：</font><input id="ruleType" class="textBox" /></p>
				<p class="hideOut"><font><em>*</em>前缀：</font><input id="prefix" class="textBox" value="${billRule.prefix}"/></p>
				<p class="hideOut"><font><em>*</em>年月日：</font><input id="date" class="textBox"/></p>
				<p class="hideOut"><font>序号长度：</font><input id="serial" class="textBox" value="${billRule.serial}"/></p>
				<p class="hideOut"><font><em>*</em>现时单号：</font><input id="lazyCode" class="textBox" value="${billRule.lazyCode}" readonly="readonly"/></p><br/>
				<p style="margin-left:65px"><font><input id="save" type="button" class="btn-blue2 btn-xs" value="保存"/></font></p>  
              </form>
          </div>

<script type="text/javascript">
$(function(){
	$("input").attr('autocomplete','off');
});
var billType=[{id:'91',text:'期初库存'},
               {id:'10',text:'采购订单'},
               {id:'11',text:'采购入库'},
               {id:'12',text:'采购退货'},
               {id:'13',text:'采购询价单'},
               {id:'20',text:'盘点单'},
               {id:'21',text:'调仓单'},
               {id:'22',text:'报损单'},
               {id:'30',text:'生产领料（原材料）'},
               {id:'31',text:'成品入库'},
               {id:'40',text:'销售订单'},
               {id:'41',text:'销售出货'},
               {id:'42',text:'销售退货'},
               {id:'43',text:'销售报价单'},
               {id:'51',text:'收款单'},
               {id:'52',text:'付款单'},
               {id:'53',text:'费用单'}];

var ruleType=[{id:'0',text:'系统自动生成'},
               {id:'1',text:'用户手动输入'},];
               
var date=[
          {id:'yyMM',text:'年月(yyMM)'},
          {id:'yyyyMM',text:'年月(yyyyMM)'},
          {id:'yyMMdd',text:'年月日(yyMMdd)'},
          {id:'yyyyMMdd',text:'年月日(yyyyMMdd)'},];
		
$("#billName").validatebox({
	required:true,
	validType:'normalChar',
	missingMessage:'该项不能为空！',
	novalidate:true,
});
$("#prefix").validatebox({
	required:true,
	missingMessage:'该项不能为空！',
	validType:'normalChar',
	novalidate:true,
});
$("#serial").numberbox({
	width:164,
	height:31,
	validType:'maxLength[10]'
});
$("#lazyCode").textbox({
	width:164,
	height:31,
	disabled:true,
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	validType:'normalChar'
});
$("#billType").combobox({
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	width:164,
	height:31,
	editable:false,
	valueField: 'id',
	textField: 'text',
	data:billType,
	hasDownArrow:false,
	disabled:true,
	onLoadSuccess:function(){
		$("#billType").combobox("setValue",'${billRule.billType}');
	}
});
$("#ruleType").combobox({
	disabled:true,
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	width:164,
	height:31,
	editable:false,
	valueField: 'id',
	textField: 'text',
	data:ruleType,
	onLoadSuccess:function(){
		$("#ruleType").combobox("setValue",'${billRule.ruleType}');
		if('${billRule.ruleType}'==1){
			$("#prefix").validatebox({required:false});
			$("#date").combobox({required:false});
			$("#lazyCode").validatebox({required:false});
			$(".hideOut").hide();
		}else{
			$("#prefix").validatebox({required:true});
			$("#date").combobox({required:true});
			$("#lazyCode").validatebox({required:true});
			$(".hideOut").show();
		}
	},
	onSelect:function(record){
		if(record.id==1){
			$("#prefix").validatebox({required:false});
			$("#date").combobox({required:false});
			$("#lazyCode").validatebox({required:false});
			$(".hideOut").hide();
		}else{
			$("#prefix").validatebox({required:true});
			$("#date").combobox({required:true});
			$("#lazyCode").validatebox({required:true});
			$(".hideOut").show();
		}
	}
});

$("#date").combobox({
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	width:164,
	height:31,
	editable:false,
	valueField: 'id',
	textField: 'text',
	data:date,
	onLoadSuccess:function(){
		$("#date").combobox("setValue",'${billRule.date}');
	}
});


$('#save').click(function(e) {
	var fid=$("#fid").val();
	var billName=$("#billName").val();
	var billType=$("#billType").combobox("getValue");;
	var ruleType=$("#ruleType").combobox("getValue");;
	var prefix=$("#prefix").val();
	var date=$("#date").combobox("getValue");
	var serial=$("#serial").val();
	var lazyCode=$("#lazyCode").textbox("getValue");
	var updateTime=$("#updateTime").val();
	$('#form').form('enableValidation'); 
		if($('#form').form('validate')){
			$("#save").attr("disabled","disabled");
			    $.post('${ctx}/billrule/save',{fid:fid,billName:billName,billType:billType,ruleType:ruleType,prefix:prefix,date:date,serial:serial,lazyCode:lazyCode,updateTime:updateTime},function(data){
			    	dataDispose(data);
			    	if(data.returnCode=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			$('#addBox').window('close');
			    			$("#save").removeAttr("disabled");
			    			//$('#bankList').datagrid('reload');
			    			$("#bankList").jqGrid("setGridParam", {//重新加载数据
			    			    postData: {}
			    			}).trigger("reloadGrid");
			    		}});
			    	}else if(data.returnCode=='1'){
			    		$.fool.alert({time:2000,msg:data.message});
			    		$("#save").removeAttr("disabled");
			    	}else {
			    		$.fool.alert({time:2000,msg:"系统繁忙，请稍后再试。"});
			    		$("#save").removeAttr("disabled");
			    	}
			    	return true;
			    });
			}else{
				return false;
				}
});
 </script>
</body>
</html>