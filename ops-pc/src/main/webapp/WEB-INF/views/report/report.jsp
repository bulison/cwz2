<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>数据分析界面</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
  #save{margin:10px 30px}
  #tb-select{
    text-align: right;
  }
  .resultBox{
    margin-top:20px;
  }
  .type2,.type3,.type4,.type5,.type6,.type7,.type12{
    width:165px;
    height:24px;
    border:1px solid rgb(220,220,220)
  }
  #tb{
    margin:10px 0px;
    height:32px
  }
  .form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{min-width:100px !important;width:auto !important;}
  .mybutton{
  	position:absolute;
  	top:10px;
  	left:330px;
  	width:200px !important;
  }
  .mybutton a{
	margin-right:5px;
  }
  .form1 p.hideP{
    display: none;
  }
</style>
</head>
<body>
	   <%-- <div class="nav-box">
	       <ul>
	           <li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	           <li><a href="#">统计报表</a></li>
	           <li><a href="#" class="curr">${reportName}</a></li>
	       </ul>
	   </div> --%>
	   <div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>${reportName}</h1>
                </div>             
       </div>
	   <div class="filterBox">
	       <div id="tb"> 
	           <input id="resultType"/> 
	           <div id="reportButton" style="background:#F0F0F0;display:inline-block;"></div>
	           <span style="vertical-align: middle;"><input id="flag" type="checkbox" checked="checked"/>分页</span>
               <!-- <a href="#" class="btn-blue4 btn-s" id="selectTemplate" >选择模板</a> 
               <a href="#" class="btn-blue4 btn-s" id="saveTemplate" >保存模板</a>  -->
           </div>
	       <div style="background: #fff;margin-right: 20px;border: 1px #ccc solid;">
	       		<div id="filterGrid" class="form1" style="display: none;background: url('/ops-pc/resources/images/notebook.jpg') repeat-y;padding: 10px 20px 10px 60px;background-position-x: 10px;"></div>
	       </div>
	       <div id="selectTempBox"></div>
	       <div id="saveTempBox"></div>
	   </div>
	   <div class="resultBox">
	       <div id="resultGrid"></div>
	   </div>
	   <form id="submitForm" action="${ctx}/report/exportExcel" method="post" style="display: none">
	     <input id="sysReportId" name="sysReportId" />
	     <input id="conditions" name="condition" />
	   </form>
	   
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
var missingMsg='该项为必填项';
var reportTypeId="${param.reportId}";
var reportTypeName="${reportName}";
var headers="";
var editor="";
var chooserWindow="";
var showPage="";

var planId = "${param.planId}";
var goodsSpecId ="";
var goodsId ="";
var deliveryPlace ="";
var receiptPlace ="";
function getParam() {
	goodsSpecId =($('.type7').next())[0].comboObj.getSelectedValue();
	goodsId =($('.type6').next())[0].comboObj.getSelectedValue();
    deliveryPlace =$('#FRECEIPT_PLACE[order="2"]').combotree('getValue');
    receiptPlace =$('#FRECEIPT_PLACE[order="3"]').combotree('getValue');
}

var billType=[
	{value:91,text:'期初库存'},
	{value:10,text:'采购订单'},
	{value:11,text:'采购入库'},
	{value:12,text:'采购退货'},
	//{value:13,text:'采购询价单'},
	{value:14,text:'采购申请单'},
	{value:20,text:'盘点单'},
	{value:21,text:'调仓单'},
	{value:22,text:'报损单'},
	{value:30,text:'生产领料（原材料）'},
	{value:32,text:'生产退料'},
	{value:31,text:'成品入库'},
	{value:33,text:'成品退库'},
	{value:34,text:'生产计划单'},
	{value:40,text:'销售订单'},
	{value:41,text:'销售出货'},
	{value:42,text:'销售退货'}//,
	//{value:43,text:'销售报价单'},
	/* {value:51,text:'收款单'},
	{value:52,text:'付款单'},
	{value:53,text:'费用单'},
	{value:55,text:'采购返利单'},
	{value:56,text:'销售返利单'}, */
];
var recordStatus=[
          	{value:'0',text:'未审核'},
          	{value:'1',text:'已审核'},
          	{value:'2',text:'已作废'},
];
var assetsStatus=[
            {value:'0',text:'未审核'},
            {value:'1',text:'已审核'},
            {value:'2',text:'计提中'},
            {value:'3',text:'计提完成'},
            {value:'4',text:'已清算'},
];
var voucherStatus=[
                   {value:'0',text:'未审核'},
                   {value:'1',text:'已审核'},
                   {value:'2',text:'已作废'},
                   {value:'3',text:'已过账'},
                   {value:'',text:'全部'},
];
var type=[
          {value:"1",text:'资产'},
          {value:"2",text:'负债'},
          {value:"3",text:'共同'},
          {value:"4",text:'所有者权益'},
          {value:"5",text:'成本'},
          {value:"6",text:'损益'}
];

function subjectChooser(target){
	editor=$(target);
	if(($("#resultType").next())[0].comboObj.getSelectedValue()!="402881ea545a4e9501545a516f8b0001"){
		chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubjectDBC&singleSelect=true&onlyLeafCheck=1',onLoad:function(){
			editor.combobox("hidePanel");
		}}); 
	}else{
		chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubjectDBC&singleSelect=true&onlyLeafCheck=1&cashSubject=1&bankSubject=1',onLoad:function(){
			(editor.next())[0].comboObj.closeAll();
			$((editor.next())[0].comboObj.getInput()).blur();
		}}); 
	}
};
$(document).ready(function(){
	$("#flag").change(function(){
		if(!$('#filterGrid').form("validate")){
			return false;
		}
		var conditions=getConditions();
		var code=($("#resultType").next())[0].comboObj.getSelectedValue();
		if(code){
			$.messager.progress({
				text:'努力计算中，请稍后...'
			});
			if($("#flag").prop('checked')){
                if(code=="402881285ada557f015adb7cbeb10000")
                {
                    getParam();
                    $("#resultGrid").load("${ctx}/flow/plan/fee/list",{planId:planId,deliveryPlace:deliveryPlace,goodsId:goodsId,receiptPlace:receiptPlace,goodsSpecId:goodsSpecId},function(){
                        $.messager.progress('close');
                    });
                }else{
                    $("#resultGrid").load("${ctx}/report/query",{flag:1,sysReportId:code,condition:JSON.stringify(conditions)},function(){
                        $.messager.progress('close');
                    });
                }

			}else{
                if(code=="402881285ada557f015adb7cbeb10000")
                {
                    getParam();
                    $("#resultGrid").load("${ctx}/flow/plan/fee/list",{planId:planId,deliveryPlace:deliveryPlace,goodsId:goodsId,receiptPlace:receiptPlace,goodsSpecId:goodsSpecId},function () {
                        $.messager.progress('close');
                    });
                }else{
                    $("#resultGrid").load("${ctx}/report/query",{flag:0,sysReportId:code,condition:JSON.stringify(conditions)},function(){
                        $('#pageaction').hide();
                        $.messager.progress('close');
                    });
                }

			}
		}else{
			$.fool.alert({msg:'请选择汇总方式'});
			return false;
		}
	});
	
	$("#selectTemplate").click(function(){
		var code=($("#resultType").next())[0].comboObj.getSelectedValue();
		if(code){
			$('#selectTempBox').window('refresh','${ctx}/report/UserTemplate/selectTemp?reportId='+($("#resultType").next())[0].comboObj.getSelectedValue());
			$('#selectTempBox').window('open');
		}else{
			$.fool.alert({msg:'请选择汇总方式'});
			return false;
		}
	});
	
	$("#saveTemplate").click(function(){
		var code=($("#resultType").next())[0].comboObj.getSelectedValue();
		if(code){
			$('#saveTempBox').window('refresh','${ctx}/report/UserTemplate/saveTemp?reportId='+($("#resultType").next())[0].comboObj.getSelectedValue());
			$('#saveTempBox').window('open');
		}else{
			$.fool.alert({msg:'请选择汇总方式'});
			return false;
		}
	});
	
	/* $("#clear").click(function(){
		$('#filterGrid').html('');
		$.post("/reportFilter/getWhereByReportId",{reportId:$("#resultType").combobox('getValue')},function(data){
			var rows=data.rows;
			if(rows.length!=0){
				$("#filterGrid").css('display','block');
				var row="";
				for(var i in rows){
					row=rows[i];
					$("#filterGrid").append("<p><font><nobr>"+row.displayName+"：</nobr></font><input id="+row.aliasName+" class='type"+row.inputType+"' order="+row.order+" tableName="+row.tableName+" fieldName="+row.fieldName+" inputType="+row.inputType+" mark="+row.mark+" data-options='required:"+row.need+",missingMessage:\""+missingMsg+"\"'></p>");
				}
				initial();
				$("#resultGrid").html("");
			}else{
				$("#filterGrid").css('display','none');
				$("#resultGrid").html("");
				return false;
			}
		});
	}); */
	
	/* $("#search").click(function(){
		alert(); */
		/* var conditions=getConditions();
		var code=$("#resultType").combobox('getValue');
		if(code){
			if($("#flag").prop('checked')){
				$("#resultGrid").load("${ctx}/report/query",{sysReportId:code,condition:JSON.stringify(conditions)});
			}else{
				$("#resultGrid").load("${ctx}/report/query",{flag:0,sysReportId:code,condition:JSON.stringify(conditions)},function(){
					$('#pageaction').hide();
				});
			}
		}else{
			$.fool.alert({msg:'请选择汇总方式'});
			return false;
		} */
	/* }); */
	
	$("#export").click(function(){
		var conditions=getConditions();
		var code=($("#resultType").next())[0].comboObj.getSelectedValue();
		$('#sysReportId').val(code);
		$('#conditions').val(JSON.stringify(conditions));
		if(code){
			$("#submitForm").submit(); 
			/* var url = "${ctx}/report/exportExcel?sysReportId="+code+"&condition="+JSON.stringify(conditions); */
			//window.location.href=encodeURI(encodeURI(url));
			/* $.post(encodeURI(encodeURI(url)),{},function(data){}); */
		}else{
			$.fool.alert({msg:'请选择汇总方式'});
			return false;
		}
	});
});

$('#selectTempBox').window({
    //href:'${ctx}/report/UserTemplate/selectTemp?reportId='+$("#resultType").combobox("getValue"),
    width:1000,
    top:80,
    title:'选择模板',
    minimizable:false,
    maximizable:false,
    closed:true
});

$('#saveTempBox').window({
    //href:'${ctx}/report/UserTemplate/saveTemp?reportId='+$("#resultType").combobox("getValue"),
    width:1000,
    top:80,
    title:'保存模板',
    minimizable:false,
    maximizable:false,
    closed:true
});

var resultTypeList=getComboData("${ctx}/report/SysRepor/list?parentId="+reportTypeId);
for(var i=0;i<resultTypeList.length;i++){
	if(resultTypeList[i].value=="4028818652a4afc40152a504e8480087"){
		resultTypeList.splice(i,1);
		break;
	}
}
/* resultTypeList[0].selected=true; */

$("#resultType").fool("dhxCombo",{
	prompt:'汇总方式',
	height:30,
	width:250,
	editable:false,
/*	required:true,*/
	novalidate:true, 
	clearOpt:false,
	/* value:resultTypeList[0].value,
	text:resultTypeList[0].text, */
	setTemplate:{
		input:"#reportName#",
		option:"#reportName#"
	},
	focusShow:true,
	data:resultTypeList,
    onLoadSuccess:function(){
		($("#resultType").next())[0].comboObj.selectOption(0);
	},
	onSelectionChange:function(){
		var record=($("#resultType").next())[0].comboObj.getSelectedText();
		if(!record){
			return false;
		}
		$('#reportButton').panel({
			noheader:true,
			width:200,
			top:60,
			fit:true,
			cls:'mybutton',
			href:"${ctx}/report/SysRepor/button?reportId="+record.fid+"&planId="+"${param.planId}",
		});
		headers=record.headers;
		$('#filterGrid').html('');
		$.ajax({
			url:"${ctx}/reportFilter/getWhereByReportId",
			async:false,
			data:{reportId:record.fid},
			success:function(data){
				var rows=data.rows;
				if(rows.length!=0){
					$("#filterGrid").css('display','block');
					var row="";
					for(var i in rows){
						row=rows[i];
						//区分快捷筛选和详细筛选
						if(row.show==1){
							if(row.need==true){
								$("#filterGrid").append("<p class='showP'><font><em>*</em><nobr>"+row.displayName+"：</nobr></font><input id="+row.aliasName+" class='type"+row.inputType+"' order="+row.order+" tableName="+row.tableName+" fieldName="+row.fieldName+" inputType="+row.inputType+" _required="+row.need+" data-options='required:"+row.need+",novalidate:true,missingMessage:\""+missingMsg+"\"' mark="+row.mark+" ></p>");
							}else{
								$("#filterGrid").append("<p class='showP'><font><nobr>"+row.displayName+"：</nobr></font><input id="+row.aliasName+" class='type"+row.inputType+"' order="+row.order+" tableName="+row.tableName+" fieldName="+row.fieldName+" inputType="+row.inputType+" _required="+row.need+" data-options='required:"+row.need+",novalidate:true,missingMessage:\""+missingMsg+"\"' mark="+row.mark+" ></p>");
							}
						}else{
							if($("#openBtn").length<1){
								$("#filterGrid").append('<br><h3 style="display: inline-block;margin-left:10px;font-size:17px">&emsp;详细筛选：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"><br/>');
							}
							$("#filterGrid").append("<p class='hideP'><font><nobr>"+row.displayName+"：</nobr></font><input id="+row.aliasName+" class='type"+row.inputType+"' order="+row.order+" tableName="+row.tableName+" fieldName="+row.fieldName+" inputType="+row.inputType+" data-options='required:"+row.need+",novalidate:true,missingMessage:\""+missingMsg+"\"' mark="+row.mark+" ></p>");
						}
					}
					//展开、收起其他信息
					$("#openBtn").click(function(e) {
								  $("#filterGrid .hideP").css("display","inline-block");
								  $('#openBtn').css("display","none");
								  $('#closeBtn').css("display","inline-block");
					});
					$("#closeBtn").click(function(e) {
								  $("#filterGrid .hideP").css("display","none");
								  $('#openBtn').css("display","inline-block");
								  $('#closeBtn').css("display","none");	
					});
					initial();
					$("#resultGrid").html("");
				}else{
					$("#filterGrid").css('display','none');
					$("#resultGrid").html("");
					return false;
				}
				showPage=data.other.showPage;
				if(showPage==0){
					$("#flag").removeAttr("checked");
					$("#flag").parent().hide();
				}else{
					$("#flag").attr("checked","checked");
					$("#flag").parent().show();
				}
			}
		});
		if(!$('#filterGrid').form("validate")){
			return false;
		}
		var conditions=getConditions();
		var code=($("#resultType").next())[0].comboObj.getSelectedValue();
		if(code){
			/* $.messager.progress({
				text:'努力计算中，请稍后...'
			}); */
			if(!showPage){
				showPage=0;
			}
			/* $("#resultGrid").load("${ctx}/report/query",{flag:showPage,sysReportId:code,condition:JSON.stringify(conditions)},function(){
				if(showPage==0){
					$('#pageaction').hide();
				}
			}); */
		}else{
			$.fool.alert({msg:'请选择汇总方式'});
			return false;
		}
	}
});


/* $("#resultType").combobox({
	url:"${ctx}/report/SysRepor/list?parentId="+reportTypeId,
	editable:false,
	prompt:'汇总方式',
	height:30,
	width:250,
	required:true,
	novalidate:true,
	textField:'reportName',
	valueField:'fid',
	onLoadSuccess:function(){
		var data=$("#resultType").combobox('getData');
		for(var i=0;i<data.length;i++){
			if(data[i].fid=="4028818652a4afc40152a504e8480087"){
				data.splice(i,1);
				$("#resultType").combobox('loadData',data);
			}
		}
		if(data[0]){
			$("#resultType").combobox("select",data[0].fid);
		}
	},
	onSelect:function(record){
		$('#reportButton').panel({
			noheader:true,
			width:200,
			top:60,
			fit:true,
			cls:'mybutton',
			href:"${ctx}/report/SysRepor/button?reportId="+record.fid,
		});
		headers=record.headers;
		$('#filterGrid').html('');
		$.ajax({
			url:"${ctx}/reportFilter/getWhereByReportId",
			async:false,
			data:{reportId:record.fid},
			success:function(data){
				var rows=data.rows;
				if(rows.length!=0){
					$("#filterGrid").css('display','block');
					var row="";
					for(var i in rows){
						row=rows[i];
						//区分快捷筛选和详细筛选
						if(row.show==1){
							$("#filterGrid").append("<p class='showP'><font><nobr>"+row.displayName+"：</nobr></font><input id="+row.aliasName+" class='type"+row.inputType+"' order="+row.order+" tableName="+row.tableName+" fieldName="+row.fieldName+" inputType="+row.inputType+" data-options='required:"+row.need+",missingMessage:\""+missingMsg+"\"' mark="+row.mark+" ></p>");
						}else{
							if($("#openBtn").length<1){
								$("#filterGrid").append('<br><h3 style="display: inline-block;margin-left:10px;font-size:17px">&emsp;详细筛选：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"><br/>');
							}
							$("#filterGrid").append("<p class='hideP'><font><nobr>"+row.displayName+"：</nobr></font><input id="+row.aliasName+" class='type"+row.inputType+"' order="+row.order+" tableName="+row.tableName+" fieldName="+row.fieldName+" inputType="+row.inputType+" data-options='required:"+row.need+",missingMessage:\""+missingMsg+"\"' mark="+row.mark+" ></p>");
						}
					}
					//展开、收起其他信息
					$("#openBtn").click(function(e) {
								  $("#filterGrid .hideP").css("display","inline-block");
								  $('#openBtn').css("display","none");
								  $('#closeBtn').css("display","inline-block");
					});
					$("#closeBtn").click(function(e) {
								  $("#filterGrid .hideP").css("display","none");
								  $('#openBtn').css("display","inline-block");
								  $('#closeBtn').css("display","none");	
					});
					initial();
					$("#resultGrid").html("");
				}else{
					$("#filterGrid").css('display','none');
					$("#resultGrid").html("");
					return false;
				}
				showPage=data.other.showPage;
				if(showPage==0){
					$("#flag").removeAttr("checked");
					$("#flag").parent().hide();
				}else{
					$("#flag").attr("checked","checked");
					$("#flag").parent().show();
				}
			}
		});
		if(!$('#filterGrid').form("validate")){
			return false;
		}
		var conditions=getConditions();
		var code=$("#resultType").combobox('getValue');
		if(code){
			$.messager.progress({
				text:'努力计算中，请稍后...'
			});
			$("#resultGrid").load("${ctx}/report/query",{flag:showPage,sysReportId:code,condition:JSON.stringify(conditions)},function(){
				$.messager.progress('close');
				if(showPage==0){
					$('#pageaction').hide();
				}
			});
		}else{
			$.fool.alert({msg:'请选择汇总方式'});
			return false;
		}
	}
}); */

function getConditions(){
	var p=$("#filterGrid").children();
	var input;
	var conditions=[];
	for(var i=0;i<p.length;i++){
		input=new Object;
		input.displayName=$(p[i]).children('font').text().slice(0,-1);
		input.mark=$(p[i]).children('input').attr("mark");
		input.order=$(p[i]).children('input').attr("order");
		input.tableName=$(p[i]).children('input').attr("tableName");
		input.fieldName=$(p[i]).children('input').attr("fieldName");
		input.aliasName=$(p[i]).children('input').attr("id");
		input.inputType=$(p[i]).children('input').attr("inputType");
		if($(p[i]).children('span')){
			input.value=$(p[i]).children('input').attr("fid");
		}
		if($(p[i]).children('span').attr("class")=="textbox"){
			input.value=$(p[i]).children('input').textbox('getValue');
		}else if($(p[i]).children('span').attr("class")=="textbox numberbox"){
			input.value=$(p[i]).children('input').numberbox('getValue');
		}else if($(p[i]).children('span').attr("class")=="textbox combo"){
			if($(p[i]).children('input').attr("inputType")=="23"){
				if($(p[i]).children('input').combotree('getValue')){
					input.value=$(p[i]).children('input').combotree('getValue');
				}else{
					input.value="";
				}
			}else{
				if($(p[i]).children('input').combobox('getValues').length>1){
					input.value=$(p[i]).children('input').combobox('getValues').toString();
				}else{
					input.value=$(p[i]).children('input').combobox('getValue');
				}
			}
		}else if($(p[i]).children('span').attr("class")=="textbox combo datebox"){//遍历p下的类为textbox combo datebox的所有span
			input.value=$(p[i]).children('input').datebox('getValue');
		}else if($(p[i]).children('span').attr("class")=="checker"){
			input.value=$(p[i]).children('input').val();
		}else if($(p[i]).find(".dhxDiv").length>0){
			var value=($(p[i]).children('input').next())[0].comboObj.getSelectedValue();
			if(value==null||value=="null"){
				value="";
			}
			input.value=value;
		}
		conditions.push(input);
 	}
	if("${param.planId}"){
		conditions.push({value:"${param.planId}",aliasName:"PLAN_ID"});
	}
	return conditions;
}

function initial(){
	//设置文本输入框的提示信息
	var textboxs=$(".type0");
	var prompt="";
	for(var i=0;i<textboxs.length;i++){
	    if($(textboxs[i]).attr('id')=='UNITID'){//收支单位
            var csvData="";
            $.ajax({
                url: "${ctx}/basedata/query?num=" + Math.random(),
                async: false,
                data: {param: "Organization,Member,AuxiliaryAttr_CostType,CSV"},
                success: function (data) {
                    csvData = formatData(data.CSV, "fid");
                }
            });
            $('#UNITID').fool('dhxComboGrid', {//收支单位
                prompt:"选择收支单位",
                novalidate: true,
                focusShow: true,
                height:30,
                width:167,
                hasDownArrow: false,
                data: csvData,
                filterUrl: '${ctx}/customerSupplier/list',
                setTemplate: {
                    input: "#name#",
                    columns: [
                        {option: '#code#', header: '编号', width: 100},
                        {option: '#name#', header: '名称', width: 100},
                        {option: "#type#", header: '类别', css: "type", width: 100},
                    ]
                },
                toolsBar: {
                    refresh:true
                },
            })
		}
		else{
            prompt="输入"+$(textboxs[i]).prev().find("nobr").text();
            $(textboxs[i]).textbox({
                height:30,
                width:167,
                validType:'isBank',
                prompt:prompt.slice(0,-1)
            });
		}

	}
	
	//设置辅助属性选择框的提示信息
	var attrCombo=$(".type1");
	var attrPrompt="";
	for(var i=0;i<attrCombo.length;i++){
		if($(attrCombo[i]).attr("mark")=="008"){
			$(attrCombo[i]).combotree({
				url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=008",
				height:30,
				width:167,
				prompt:"选择费用项目",
                onLoadSuccess:function(node, data){
					if(data[0].id!=""){
						var node=$(this).tree("find",data[0].id);
						$(this).tree('update',{
							target: node.target,
							text: '请选择',
							id:""
						});
					}
				},
				onBeforeSelect:function(node){
					  if(!node.id){
						  return false;
					  }
				},
			}); 
		}else{
			attrPrompt="选择"+$(attrCombo[i]).prev().find("nobr").text();
			var myname = $(attrCombo[i]).prev().find("nobr").text();
			var code=$(attrCombo[i]).attr("mark");
// 			console.log(getComboData("${ctx}/basedata/findSubAuxiliaryAttrTree?code="+code,"tree"));
			$(attrCombo[i]).fool("dhxCombo",{
				prompt:attrPrompt.slice(0,-1),
				height:30,
				width:167,
				setTemplate:{
					input:"#name#",
					option:"#name#"
				},
				clearOpt:false,
				data:getComboData("${ctx}/basedata/findSubAuxiliaryAttrTree?code="+code,"tree"),
				toolsBar:{
					name:dhxGetName(code),
					refresh:true
				},
                focusShow:true,
				editable:false,
				onOpen:function(){
					/* var code=$(attrCombo[i]).attr("mark"); */
					/* $(attrCombo[i]).next()[0].comboObj.clearAll("false");
					$(attrCombo[i]).next()[0].comboObj.addOption(getComboData("${ctx}/basedata/findSubAuxiliaryAttrTree?code="+code,"tree")); */
				},
			});
		}
		/* 		$(attrCombo[i]).combotree({
			height:30,
			width:167,
			prompt:attrPrompt.slice(0,-1),
			onLoadSuccess:function(node, data){
				if(data[0].id!=""){
					var node=$(this).tree("find",data[0].id);
					$(this).tree('update',{
						target: node.target,
						text: '请选择',
						id:""
					});
				}
			},
			onShowPanel:function(){
				var code=$(this).combotree("textbox").parent().prev().attr("mark");
				$(this).combotree("reload","${ctx}/basedata/findSubAuxiliaryAttrTree?code="+code);
			},
			onBeforeSelect:function(node){
				  if(!node.id){
					  return false;
				  }
			},
		}); */
	}
	
	var supCombogrid=$(".type2");
	var goodsMark="";
	for(var i=0;i<supCombogrid.length;i++) {
		if ($(supCombogrid[i]).attr("mark") == "FID" || !$(supCombogrid[i]).attr("mark")) {
	        goodsMark = "fid";
	    } else if ($(supCombogrid[i]).attr("mark") == "CODE") {
	        goodsMark = "code";
	    }
		//供应商弹出框换成下拉框
		$(supCombogrid[i]).fool("dhxComboGrid",{
			prompt:"选择供应商",
			height:30,
			width:167,
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
			},
			filterUrl:getRootPath()+'/supplier/vagueSearch?searchSize=8',
			data:getComboData(getRootPath()+'/supplier/vagueSearch?searchSize=8&q=', "", "", goodsMark),
	        toolsBar:{
	            refresh:true
	        },
	        searchKey:"searchKey",
			focusShow:true,
		});
	};
	
	$(".type3").fool("dhxComboGrid",{
		prompt:"选择客户",
		height:30,
		width:167,
		setTemplate:{
			input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
		},
		filterUrl:getRootPath()+'/customer/vagueSearch?searchSize=8',
		data:getComboData(getRootPath()+'/customer/vagueSearch?searchSize=8&q='),
        toolsBar:{
            refresh:true
        },
        searchKey:"searchKey",
		/* focusShow:true, */
	});
	$(".type4").fool("dhxComboGrid",{
		prompt:"选择客户、供应商",
		height:30,
		width:167,
		setTemplate:{
			input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
						{option:'#type#',header:'类别',width:100},
					],
		},
		toolsBar:{
			refresh:true
		},
		filterUrl:getRootPath()+'/customerSupplier/list',
		data:getComboData(getRootPath()+'/customerSupplier/list',"grid"),
        searchKey:"searchKey",
		focusShow:true,
	});
	//人员弹出框改为下拉框
	$(".type5").fool("dhxComboGrid",{
		prompt:"选择人员",
		height:30,
		width:167,
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
		filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8',
		data:getComboData(getRootPath()+'/member/vagueSearch?searchSize=8'),
        toolsBar:{
            refresh:true
        },
        searchKey:"searchKey",
		focusShow:true,
	});
	/* $(".type5").fool("combogrid",{
		required:false,
		height:30,
		width:167,
		prompt:"选择人员",
		idField:'fid',
		textField:'username',
		panelWidth:450,
		panelHeight:283,
		fitColumns:true,
		url:getRootPath()+'/member/vagueSearch?searchSize=8',
		columns:[[
			{field:'fid',title:'fid',hidden:true},
			{field:'userCode',title:'编号',width:100,searchKey:false},
			{field:'jobNumber',title:'工号',width:100,searchKey:false},
			{field:'username',title:'名称',width:100,searchKey:false},
			{field:'phoneOne',title:'电话',width:100,searchKey:false},
			{field:'deptName',title:'部门',width:100,searchKey:false},
			{field:'position',title:'职位',width:100,searchKey:false},
			{field:'searchKey',hidden:true,searchKey:true},
		          ]],
	}); */
	/* if($('.type5').length>0){
		$(($('.type5').next())[0].comboObj.getInput()).focus(function(){
			($('.type5').next())[0].comboObj.openSelect();
		});
	} */
	//货品弹出框改为下拉框
	var goodsCombogrid=$(".type6");
	var goodsMark="";
	for(var i=0;i<goodsCombogrid.length;i++) {
        if ($(goodsCombogrid[i]).attr("mark") == "FID" || !$(goodsCombogrid[i]).attr("mark")) {
            goodsMark = "fid";
        } else if ($(goodsCombogrid[i]).attr("mark") == "CODE") {
            goodsMark = "code";
        }
        var goodsCombo = $(".type6").fool("dhxComboGrid", {
            prompt: "选择货品",
            height: 30,
            width: 167,
            setTemplate: {
                input: "#name#",
                columns: [
                    {option: '#name#', header: '名称', width: 100},
                    {option: '#code#', header: '编码', width: 100},
                    {option: '#barCode#', header: '条码', width: 100},
                ],
            },
            toolsBar: {
                refresh: true
            },
            filterUrl: getRootPath() + '/goods/vagueSearch?searchSize=6',
            data: getComboData(getRootPath() + '/goods/vagueSearch?searchSize=6', "", "", goodsMark),
            searchKey: "searchKey",
            focusShow: true,
            idField:goodsMark,
            onChange: function (value, text) {
                if(value){
                    var opt = (goodsCombo.getOption(value)).text;
                    if(opt.goodsSpecGroupId){
                    	if($('.type7').length>0){
                    		var specUrl=""; //属性链接地址
                    		($('.type7').next())[0].comboObj.enable();
                    		specUrl = getRootPath() + "/goodsspec/getPartTree?groupId=" + opt.goodsSpecGroupId;
                            getSpec(specUrl);
                    	}
                    }
                    else{
                    	($('.type7').next())[0].comboObj.disable();
                    	($('.type7').next())[0].comboObj.setComboText("");
                    }
				}
            }
        });
    }

	$(".type7").fool("dhxComboGrid",{
		prompt:"选择货品属性",
		height:30,
		width:167,
		setTemplate:{
			input:"#name#",
			columns:[
						{option:'#name#',header:'名称',width:100},
						{option:'#code#',header:'编码',width:100},
						],
		},
		toolsBar:{
			refresh:true
		},
		editable:false,
		data:getComboData(getRootPath()+'/goodsspec/vagueSearch?searchSize=6'),
		searchKey:"searchKey",
		focusShow:true,
	});
	if($('.type7').length>0){
		($('.type7').next())[0].comboObj.disable(true);
	}
	//货品属性弹出框改为下拉框
	function getSpec(specUrl) {
		$(".type7").fool("dhxComboGrid",{
			prompt:"选择货品属性",
			height:30,
			width:167,
			disabled:true,
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#name#',header:'名称',width:100},
							{option:'#code#',header:'编码',width:100},
							],
			},
			toolsBar:{
				refresh:true
			},
			editable:false,
			data:getComboData(specUrl,"tree"),
			searchKey:"searchKey",
			focusShow:true,
			onLoadSuccess:function () {
                ($(".type7").next())[0].comboObj.setComboText("");
            },
		});
    }

	$(".type8").fool("dhxCombo",{
		prompt:"选择现金银行",
		height:30,
		width:167,
		editable:false,
		clearOpt:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:getComboData("${ctx}/bankController/comboboxData"),
        toolsBar:{
        	name:"现金银行",
            refresh:true
        }

    });
	$(".type9").datebox({
		editable:false,
		height:30,
		width:167,
		prompt:"选择日期",
	});

	var dateboxs=$(".type9");
	var periodStr="";
	var periodEnd="";
	var today = new Date().format("yyyy-MM-dd");
	var y = new Date().getFullYear();
	var m = new Date().getMonth()+2;
    var d = new Date().getDate();
	var enday = y+"-"+m+"-"+d;
	$.ajax({
		url:"${ctx}/fiscalPeriod/getFristUnCheckPeriod",
		async:false,
		success:function(data){
			periodStr=data.startDate;
			periodEnd=data.endDate;
		}
	});
	for(var i=0;i<dateboxs.length;i++){
		if($(dateboxs[i]).attr("mark")=="PERIODSTR"){
			$(dateboxs[i]).datebox("setValue",periodStr);
            continue;
		}else if($(dateboxs[i]).attr("mark")=="PERIODEND"){
			$(dateboxs[i]).datebox("setValue",periodEnd);
			continue;
		}else if($(dateboxs[i]).attr("mark")=="TODAY"){
		    $(dateboxs[i]).datebox("setValue",today);
            continue;
		}else if($(dateboxs[i]).attr("mark")=="NEXTMONTH"){
		 	$(dateboxs[i]).datebox("setValue",enday);
		 	continue;
        }
	}
	$(".type10").numberbox({
		height:30,
		width:167,
		prompt:"输入数字",
		validType:'numMaxLength[10]'
	});
	/* $(".type11").fool("dhxCombo",{
		prompt:'选择单据类型',
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:billType,
		onLoadSuccess:function(){
			($(".type11").next())[0].comboObj.deleteOption("");
		},
	}); */
	$(".type11").combobox({
		data:billType,
		editable:false,
		prompt:'选择单据类型',
		width:167,
		height:30,
		multiple:true
	});
	//用户弹出框改为下拉框
	$(".type12").fool("dhxComboGrid",{
		prompt:"选择用户",
		height:30,
		width:167,
		setTemplate:{
			input:"#userName#",
			columns:[
			         {option:'#userCode#',header:'账号',width:100},
			         {option:'#userName#',header:'名称',width:100},
					],
		},
		filterUrl:getRootPath()+'/userController/vagueSearch?searchSize=6',
		data:getComboData(getRootPath()+'/userController/vagueSearch?searchSize=6'),
        toolsBar:{
            refresh:true
        },
        searchKey:"searchKey",
		focusShow:true,
	});
	$(".type13").fool("dhxCombo",{
		prompt:"选择部门",
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#orgName#",
			option:"#orgName#"
		},
        toolsBar:{
        	name:"部门",
            refresh:true
        },
		focusShow:true,
		data:getComboData("${ctx}/orgController/getAllTree","tree")

    });

	$(".type14").fool("dhxCombo",{
		prompt:'选择单据状态',
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:recordStatus,
	});
	/* $(".type14").combobox({
		data:recordStatus,
		editable:false,
		prompt:'选择单据状态',
		width:167,
		height:30,
	}); */
	$(".type15").textbox({
		prompt:'凭证号',
		width:167,
		height:30,
	});
	$(".type16").fool("dhxCombo",{
		prompt:'固定资产卡片状态',
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:assetsStatus,
	});
	/* $(".type16").combobox({
		data:assetsStatus,
		editable:false,
		prompt:'固定资产卡片状态',
		width:167,
		height:30,
	}); */
	/* $(".type17").fool("dhxCombo",{
		prompt:'从凭证状态选择',
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:voucherStatus,
		onLoadSuccess:function(){
			($(".type17").next())[0].comboObj.deleteOption("");
		},
	}); */
	$(".type17").combobox({
		data:voucherStatus,
		editable:false,
		prompt:'从凭证状态选择',
		width:167,
		height:30,
		multiple:true
	});
	/* var subjectData="";
	var filterUrl="";
	if(($("#resultType").next())[0].comboObj.getSelectedValue()!="402881ea545a4e9501545a516f8b0001"){
		subjectData=getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q=");
		filterUrl="${ctx}/fiscalSubject/getSubject?leafFlag=1";
	}else{
		subjectData=getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&cashSubject=1&q=");
		filterUrl="${ctx}/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&cashSubject=1";
	}
	$('.type18').fool('dhxComboGrid',{
		prompt:'从科目选择输入',
		width:160,
		height:32,
		focusShow:true,
	  	data:subjectData,
	  	filterUrl:filterUrl,
	  	searchKey:"q", 
	  	setTemplate:{
	  		input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
	  	},
	  	onLoadSuccess:function(){
			if($($(this)[0].cont).prev().attr("mark")=="FID"){
				$(this).combobox('options').valueField="fid";
				$($(this)[0].cont)[0].comboObj.clearAll("false");
				$($(this)[0].cont)[0].comboObj.addOption(getComboData("${ctx}/fiscalSubject/getSubject"),"","","fid");
			}
		},
	  });
	  $('.type18').next().find(".dhxcombo_select_button").click(function(){
		  ($('.type18').next())[0].comboObj.closeAll();
		  subjectChooser($(this).closest(".dhxDiv").prev());
	  }); */
	var subjectData="";
	var filterUrl="";
	if(($("#resultType").next())[0].comboObj.getSelectedValue()!="402881ea545a4e9501545a516f8b0001"){
		subjectData=getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="); 
		filterUrl="${ctx}/fiscalSubject/getSubject?leafFlag=1";
		$(".type18").fool('subjectCombobox',{
			prompt:'从科目选择输入',
			width:167,
			height:30,
			valueField:"code",
			textField:"name",
			searchHelp:false,//不填默认是false，为true的时候选中有子节点的节点时按右键会自动重新搜索父节点下的子节点
			focusShow:true, //设置聚焦就显示下拉框
			url:filterUrl, 
			mode:"remote",
			toolsBar:{
	              refresh:true
	          },
			onLoadSuccess:function(){
				if($(this).attr("mark")=="FID"){
					$(this).combobox('options').valueField="fid";
				}
			},
			onClickIcon:function(){
				$(this).combobox('textbox').blur();
	        	$(this).combobox('hidePanel');
	        	subjectChooser(this);
	        },
		});
	}else{
		subjectData=getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&cashSubject=1&q=");
		filterUrl="${ctx}/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&cashSubject=1";
		$('.type18').fool('dhxComboGrid',{
			prompt:'从科目选择输入',
			width:160,
			height:32,
			focusShow:true,
		  	data:subjectData,
		  	filterUrl:filterUrl,
		  	searchKey:"q", 
		  	toolsBar:{
	              refresh:true
	          },
		  	setTemplate:{
		  		input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
		  	},
		  	onLoadSuccess:function(){
				if($($(this)[0].cont).prev().attr("mark")=="FID"){
					$(this).combobox('options').valueField="fid";
					$($(this)[0].cont)[0].comboObj.clearAll("false");
					$($(this)[0].cont)[0].comboObj.addOption(getComboData("${ctx}/fiscalSubject/getSubject"),"","","fid");
				}
			},
		  });
		  $('.type18').next().find(".dhxcombo_select_button").click(function(){
			  ($('.type18').next())[0].comboObj.closeAll();
			  subjectChooser($(this).closest(".dhxDiv").prev());
		  }); 
	}
	$(".type19").fool("dhxCombo",{
		prompt:'从科目类型选择输入',
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:type,
	});
	/* $(".type19").combobox({
		data:type,
		editable:false,
		prompt:'从科目类型选择输入',
		width:167,
		height:30,
	}); */
	/* $(".type20").fool("dhxCombo",{
		prompt:"选择凭证字",
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:getComboData("${ctx}/basedata/voucherWord","tree"),
	}); */
	$(".type20").combotree({
		url:"${ctx}/basedata/voucherWord",
		multiple:true,
		onlyLeafCheck:true,
		height:30,
		width:167,
		prompt:"选择凭证字",
		onLoadSuccess:function(node, data){
			if(data[0].id!=""){
				var node=$(this).tree("find",data[0].id);
				$(this).tree('update',{
					target: node.target,
					text: '请选择',
					id:""
				});
			}
		},
		onBeforeSelect:function(node){
			  if(!node.id){
				  return false;
			  }
		},
	}); 
	
	//是否的combobox改为checkbox
	/* $(".type21").combobox({
		data:[{value:1,text:"是"},{value:0,text:'否'}	],
		editable:false,
		prompt:'是否显示',
		width:167,
		height:30, */
		/* required:true,
		novalidate:true, */
	/* 	multiple:false
	}); */
	$(".type21").hide();
	var targets=$(".type21");
	var fieldname="";
	for(var i=0;i<targets.length;i++){
		fieldname=$(targets[i]).attr("fieldname");
		if(i==0){
			$(targets[i]).parent().before("<br/>");
		}
		$(targets[i]).after("<span class='checker' id='check_"+fieldname+"' style='width:30px;display:inline-block'><input type='checkbox' style='width:14px;height:14px;vertical-align: middle;' /></span>");
		if($(targets[i]).attr("mark")=="CHECKED"){
			$(targets[i]).next().find("input").prop("checked",true);
			$(targets[i]).val(1);
		}
		//调整checkbox的宽度
		$(".checker").closest("p").find("font").css("min-width","100px");
		$(".checker").closest("p").find("font").css("width","auto");
		$(".checker").click(function(){
			if($(this).find("input").prop('checked')){
				$(this).prev().val(1);
			}else{
				$(this).prev().val(0);
			}
		});
	}
	
	var periodCombo=$(".type22");
	var periodData=getComboData("${ctx}/fiscalPeriod/getAll?defaultSelect=2");
	var defultPeriod="";
	for(var i=0;i<periodData.length;i++){
		if(periodData[i].text&&periodData[i].text.isChecked==1){
			defultPeriod=periodData[i].value;
			break;
		};
	}
	for(var i=0;i<periodCombo.length;i++){
		$(periodCombo[i]).fool("dhxCombo",{
			prompt:'从财务会计期间选择',
			height:30,
			width:167,
			editable:false,
			setTemplate:{
				input:"#period#",
				option:"#period#"
			},
			focusShow:true,
			data:periodData,
			onOpen:function(){
				/* var code=$($(this)[0].cont).prev().attr("mark");
				$($(this)[0].cont)[0].comboObj.clearAll("false");
				if(code==""){
					$($(this)[0].cont)[0].comboObj.addOption(getComboData("${ctx}"+code));
				}else{
					$($(this)[0].cont)[0].comboObj.addOption(getComboData("${ctx}/fiscalPeriod/getAll"));
				} */
			},
		});
		if(!($(periodCombo[i]).next())[0].comboObj.getSelectedValue()||defultPeriod!=""){
			($(periodCombo[i]).next())[0].comboObj.setComboValue(defultPeriod)
		}
	}
	/* $(".type22").combobox({
		valueField:"fid",
		textField:"period",
		editable:false,
		prompt:'从财务会计期间选择',
		width:167,
		height:30,
		multiple:false,
		onShowPanel:function(){
			var code=$(this).combobox("textbox").parent().prev().attr("mark");
			if(code){
				$(this).combobox("reload","${ctx}"+code);
			}else{
				$(this).combobox("reload","${ctx}/fiscalPeriod/getAll");
			}
			
		},
		onLoadSuccess:function(data){
			if(!$(this).combobox("getValue")){
				for(var i=0;i<data.length;i++){
					if(data[i].isChecked==1){
						$(this).combobox("select",data[i].fid);
						return;
					}
				}
			}
		}
	}); */
	/* $(".type22").combobox("showPanel");
	$(".type22").combobox("hidePanel"); */
	var freightAddressData="";
	$.ajax({
	 	url:getRootPath()+"/api/freightAddress/getTree?enable=1",
		async:false,
        data:{},
        success:function(data){
        	freightAddressData=data;
        }
	});
	
	$(".type23").combotree({
		prompt:'从货运地址选择',
        novalidate:true,
        width:167,
        height:30,
        method:"get",
        url:getRootPath()+"/api/freightAddress/findAddressTree?enable=1",
        editable:false
	})
	
	$(".type24").fool("dhxCombo",{
		prompt:'从收支类型选择输入',
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:[{value:1,text:"收款"},{value:-1,text:"付款"}],
	});
	
	$(".type25").fool("dhxCombo",{
		prompt:'从资金计划状态选择输入',
		height:30,
		width:167,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:[{value:0,text:"草稿"},{value:1,text:"审核"},{value:2,text:"坏账"},{value:3,text:"完成"},{value:4,text:"取消"}],
	});
	
	$('#MAXVOUCHERNUM').textbox({
		validType:Array("numCompare","accessoryNumber"),
	});
	$('#MINVOUCHERNUM').textbox({
		onChange:function(){
			if(!$('#MAXVOUCHERNUM').textbox('isValid')){
				return false;
			}
		}
	});
	$('#FSUBJECTSTARTCODE').textbox({
		validType:'subjectCode',
		onChange:function(){
			if(!$('#FSUBJECTENDCODE').textbox('isValid')){
				return false;
			}
		}
	});
	$('#FSUBJECTENDCODE').textbox({
		validType:Array("codeCompare","subjectCode")
	});
	$('#fsubjectStartCode').textbox({
		validType:'subjectCode',
		onChange:function(){
			if(!$('#fsubjectEndCode').textbox('isValid')){
				return false;
			}
		}
	});
	$('#fsubjectEndCode').textbox({
		validType:Array("codeCompares","subjectCode")
	});
	$.extend($.fn.validatebox.defaults.rules, {
		numCompare:{
			validator:function(value){
				return value>=$('#MINVOUCHERNUM').textbox('getValue');
			},
			message:'最大凭证号不能小于最小凭证号。'
		},
		codeCompare:{
			validator:function(value){
				return value>=$('#FSUBJECTSTARTCODE').textbox('getValue');
			},
			message:'结束编码不能小于开始编码。'
		},
		codeCompares:{
			validator:function(value){
				return value>=$('#fsubjectStartCode').textbox('getValue');
			},
			message:'结束编码不能小于开始编码。'
		},
	});
}
function selectSubject(rowData){
	if(($("#resultType").next())[0].comboObj.getSelectedValue()!="402881ea545a4e9501545a516f8b0001"){
		if(editor.attr("mark")=="FID"){
			editor.combobox("setValue",rowData[0].fid);
		}else{
			editor.combobox("setValue",rowData[0].code);
		}
	}else{
		/* (editor.next())[0].comboObj.setComboText(rowData[0].name); */
		if(editor.attr("mark")=="FID"){
			(editor.next())[0].comboObj.setComboValue(rowData[0].fid);
		}else{
			(editor.next())[0].comboObj.setComboValue(rowData[0].code);
		}
	}
	chooserWindow.window('close');
}
function selectSubjectDBC(rowData) { 
	if(($("#resultType").next())[0].comboObj.getSelectedValue()!="402881ea545a4e9501545a516f8b0001"){
		if(editor.attr("mark")=="FID"){
			editor.combobox("setValue",rowData.fid);
		}else{ 
			editor.combobox("setValue",rowData.code);
		}
	}else{
		/* (editor.next())[0].comboObj.setComboText(rowData.name); */
		if(editor.attr("mark")=="FID"){
			(editor.next())[0].comboObj.setComboValue(rowData.fid);
		}else{
			(editor.next())[0].comboObj.setComboValue(rowData.code);
		}
	}
	chooserWindow.window('close');
}

//辅助属性对应编号
function dhxGetName(code){
	var name = "";
	switch(code){
	case "022": 
		name = '场地类型';break;
	case "008": 
		name = '费用项目';break;
	case "018": 
		name = '运输费用';break;
	case "014": 
		name = '摘要';break;
	case "004": 
		name = '货品类别';break;
	case "017": 
		name = '打印类型';break;
	case "021": 
		name = '运输费计价单位';break;
	case "002": 
		name = '客户类别';break;
	case "006": 
		name = '学历';break;
	case "005":
		name = '在职状况';break;
	case "019": 
		name = '运输方式';break;
	case "023":
		name = '损耗地址';break;
	case "012": 
		name = '财务项目';break;
	case "011": 
		name = '币别';break;
	case "003": 
		name = '征信级别';break;
	case "007": 
		name = '仓库';break;
	case "001": 
		name = '地区';break;
	case "009": 
		name = '科目类别';break;
	case "010": 
		name = '凭证字';break;
	case "015": 
		name = '结算方式';break;
	case "020": 
		name = '装运方式';break;
	case "016": 
		name = '资产类型';break;
	}
	return name;
}
</script>
</body>  
</html>
