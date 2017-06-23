<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
<style>
</style>
</head>
<body>
<style>
.validatebox-invalid {
    border-color: #ffa8a8 !important;
    background-color: #FFE1E1 !important;
    color: #000 !important;
}
.signtext{
	width:100px !important;
	height:18px !important;
}
</style>
<form action="" class="bill-form myform" id="form">
	<div class="title">
		<div class="title1 shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div class="square1"><span class="backBtn"></span><div class="triangle"></div></div><h1>填制凭证</h1>
		</div>
	</div>
	<div class="bill shadow" style="margin-top:50px;">
		<div class="in-box list2" id="list2">
			<div class="inlist">
                <input id="fid" name="fid" type="hidden" value="${obj.fid}"/>
                <input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}"/>
                <input id="creatorId" name="creatorId" type="hidden" value="${obj.creatorId}"/>
                <input id="auditorId" name="auditorId" type="hidden" value="${obj.auditorId}"/>
                <input id="cancelorId" name="cancelorId" type="hidden" value="${obj.cancelorId}"/>
                <input id="supervisorId" name="supervisorId" type="hidden" value="${obj.supervisorId}"/>
                <input id="postPeopleId" name="postPeopleId" type="hidden" value="${obj.postPeopleId}"/>
                <input id="voucherWordId" name="voucherWordId" type="hidden" value="${obj.voucherWordId}"/>
                <input id="fiscalAccountId" name="fiscalAccountId" type="hidden" value="${obj.fiscalAccountId}"/>
                <input id="getLastResume" type="hidden"/>
                <input id="editVoucherNumber" type="hidden"/>
                <p>
	                <font><em>*</em>凭证日期：</font><input id="voucherDate" name="voucherDate" class="textBox" value=""/>
	                <font><em>*</em>凭证字：</font><span style="display:inline-block;" id="voucherWordName" show="false" name="" ></span>
	               	<font><em>*</em>凭证号：</font><input id="voucherNumber" name="voucherNumber" class="textBox" value=""/>
	            </p>
                <p>
	                <font>附件数：</font><input id="accessoryNumber" name="accessoryNumber" class="textBox" value="${obj.accessoryNumber}"/>
	                <font>顺序号：</font><input id="number" name="number" class="textBox" value="${obj.number}"/>
	                <font>金额：</font><input id="totalAmount" name="totalAmount" class="textBox" readonly="readonly" value="${obj.totalAmount}"/>
	            </p>
	            <p>
	                <font>状态：</font><input id="recordStatus" name="recordStatus" class="textBox" value=""/>
	            </p>
                <br><h3 style="display: inline-block;margin-left:44px">&emsp;其他信息：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"><br/>
                <p class="hideOut">
	                <font>创建人：</font><input id="creatorName" class="textBox" value="${obj.creatorName}" disabled="disabled"/>
	                <font>创建日期：</font><input id="createTime" class="textBox" value="${obj.createTime}" disabled="disabled"/>
	                <font>审核人：</font><input id="auditorName" class="textBox" value="${obj.auditorName}" disabled="disabled"/>
	            </p>
                <p class="hideOut">
	                <font>审核日期：</font><input id="auditTime" class="textBox" value="${obj.auditDate}" disabled="disabled"/>
	                <font>作废人：</font><input id="cancelorName" class="textBox" value="${obj.cancelorName}" disabled="disabled"/>
	                <font>作废日期：</font><input id="cancelTime" class="textBox" value="${obj.cancelDate}" disabled="disabled"/>
	            </p>
                <p class="hideOut">
	                <font>凭证主管：</font><input id="supervisorName" class="textBox" value="${obj.supervisorName}" disabled="disabled"/>
	                <font>记账人：</font><input id="postPeopleName" class="textBox" value="${obj.postPeopleName}" disabled="disabled"/>
              	</p>
             </div>
        </div>
	</div>
	<div class="shadow dataBox">
		<div class="in-box list1" id="gridBox">
			<table id="detailList"></table>
            <div id="signBox">
                <p class="hideOut"><font>外币币别：</font><input type="text" id="currencyName" readonly="readonly"/></p>
                <p class="hideOut"><font>汇率：</font><input type="text" id="exchangeRate" class="textBox signtext" disabled="disabled"/></p>
                <p class="hideOut"><font>外币金额：</font><input type="text" id="currencyAmount" class="textBox signtext" disabled="disabled" /></p>
                <p class="hideOut"><font>单位：</font><input id="unitName" readonly="readonly" /></p>
                <p class="hideOut"><font>数量：</font><input type="text" id="quantity" class="textBox signtext" disabled="disabled" /></p>
                <p class="hideOut"><font>供应商：</font><span id="supplierName" ></span></p>
                <p class="hideOut"><font>客户：</font><span id="customerName" ></span></p>
                <p class="hideOut"><font>部门：</font><span id="departmentName" ></span></p>
                <p class="hideOut"><font>职员：</font><span id="memberName" ></span></p>
                <p class="hideOut"><font>仓库：</font><span id="warehouseName" ></span></p>
                <p class="hideOut"><font>项目：</font><span id="projectName" ></span></p>
                <p class="hideOut"><font>货品：</font><span id="goodsName" ></span></p>
            </div>
		</div>
	</div>
	

</form>

<div class="mybtn-footer" id="btnBox"></div>          
          
<script type="text/javascript">
	var voucherWord="";
	var voucherResume="";
	var supplier="";
	var customer="";
	var organization="";
	var member="";
	var warehouse="";
	var costType="";
	var goods="";
	var subject = "";
	
		$.ajax({
			url:"${ctx}/basedata/query?num="+Math.random(),
			async:false,
			data:{param:"Goods,Supplier,Customer,Organization,Member,AuxiliaryAttr_Warehouse,AuxiliaryAttr_Project,AuxiliaryAttr_VoucherWord,AuxiliaryAttr_Abstract"},
			success:function(data){
		    	voucherWord=formatTree(data.AuxiliaryAttr_VoucherWord[0].children,"text","id");
		    	if('${obj.recordStatus}'!=1&&'${obj.recordStatus}'!=2&&'${obj.recordStatus}'!=3){
			    	voucherResume=formatTree(data.AuxiliaryAttr_Abstract[0].children,"text","id");
			    	supplier=formatData(data.Supplier,"fid");
			    	customer=formatData(data.Customer,"fid");
			    	organization=formatTree(data.Organization[0].children,"text","id");
			    	member=formatData(data.Member,"fid");
			    	warehouse=formatTree(data.AuxiliaryAttr_Warehouse[0].children,"text","id");
			    	costType=formatTree(data.AuxiliaryAttr_Project[0].children,"text","id");
			    	goods=formatData(data.Goods,"fid");
		    	}
		    }
			});
	if('${obj.recordStatus}'!=1&&'${obj.recordStatus}'!=2&&'${obj.recordStatus}'!=3){
		$.ajax({
			url:"${ctx}/fiscalSubject/getSubject?q=&num="+Math.random(),
			async:false,
			success:function(data){
		    	subject=formatData(data,"fid");
		    }
			});
	}
	var treefkd='',boxfkd='',tbox1kd='',tbox2kd='';//快捷键事件变量
	var treef='',boxf='',tbox1='',tbox2='';//快捷键编辑框变量
	var vkd='';
	//弹出框变量
	var chooserWindow="";
	var edIndex="";
	var $grid="";
	var initialVo="";
	var initialDetail="";
	var rowData=[];
	$('html').css('overflow','hidden');//解决combo下拉框闪移问题
	if('${obj.fid}'||"${fid}"){
		var fid="";
		if('${obj.fid}'){
			fid='${obj.fid}';
		}else{
			fid="${fid}";
		}
		$.ajax({
			url:"${ctx}/voucherdetail/getDetails?r="+Math.random(),
			async:false,
	        data:{voucherId:fid},
	        success:function(data){
	        	rowData=data;
	        }
		});
	}

	$('.backBtn').click(function(){
		$('#addBox').window('close');
	});
	 
	  //表单控件初始化
	  $("#recordStatus").combobox({
		  value:'${obj.recordStatus}',
		  disabled:true,
		  hasDownArrow:false,
		  editable:false,
		  width:182,
		  height:32,
		  data:[{value:"0",text:'草稿单'},{value:"1",text:'已审核'},{value:"2",text:'已作废'},{value:"3",text:'已过账'}]
	  });
	  
	  $('.inlist').addClass('status'+$("#recordStatus").combobox('getValue'));//加上是否审核作废的状态图标
	  
	  $("#voucherNumber").validatebox({
		  validType:['isBank','positiveInt'],
		  width:182,
		  height:32,
		  required:true,
		  missingMessage:'该项不能为空！',
		  novalidate:true, 
	  });
	  var vouCombo = $("#voucherWordName").fool("dhxCombo",{
		  width:182,
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
		  required:true,
		  selectFirst:true,
		  data:voucherWord,
		  focusShow:true,
		  onChange:function(value,text){
			  var rowdata = vouCombo.getOption(value);
			  var node = rowdata.text;
			  if("${obj.voucherWordId}"&&node.fid=="${obj.voucherWordId}"&&$("#voucherDate").datebox("getValue")=="${obj.voucherDate}"){
					$("#voucherNumber").val("${obj.voucherNumber}");
					  $("#voucherWordId").val("${obj.voucherWordId}");
					  //$("#voucherWordName").combotree("setValue","${obj.voucherWordId}");
					  vouCombo.setComboValue("${obj.voucherWordId}");
					  $("#getLastResume").val("${getLastResume}");
					  $("#editVoucherNumber").val("${editVoucherNumber}");
					  if($("#editVoucherNumber").val()==1){
							$("#voucherNumber").attr("readonly","readonly");
					  }else{
							$("#voucherNumber").removeAttr("readonly");
					  }
					  return;
				}
				$("#voucherWordId").val(node.fid);
				$.post("${ctx}/voucher/getDefaultMessage",{voucherDate:$("#voucherDate").datebox('getValue'),voucherWordId:node.fid},function(data){
					$("#voucherNumber").val(data.voucherNumber);
					$("#getLastResume").val(data.getLastResume);
					$("#editVoucherNumber").val(data.editVoucherNumber);
					if($("#editVoucherNumber").val()==1){
						$("#voucherNumber").attr("readonly","readonly");
					}else{
						$("#voucherNumber").removeAttr("readonly");
					}
				});
		  }
	  });
	  vouCombo.readonly(true);
	  /* $("#voucherWordName").find("input.dhxcombo_input").validatebox({
		  required:true
	  }); */
	  /* $("#voucherWordName").combotree({
		    data:voucherWord,
		    width:182,
		    height:32,
			onBeforeSelect:function(node){
				if(node.text=="请选择"){
					return false;
				}
			},
			onSelect:function(node){
				if("${obj.voucherWordId}"&&node.id=="${obj.voucherWordId}"&&$("#voucherDate").datebox("getValue")=="${obj.voucherDate}"){
					$("#voucherNumber").val("${obj.voucherNumber}");
					  $("#voucherWordId").val("${obj.voucherWordId}");
					  $("#voucherWordName").combotree("setValue","${obj.voucherWordId}");
					  $("#getLastResume").val("${getLastResume}");
					  $("#editVoucherNumber").val("${editVoucherNumber}");
					  if($("#editVoucherNumber").val()==1){
							$("#voucherNumber").attr("readonly","readonly");
					  }else{
							$("#voucherNumber").removeAttr("readonly");
					  }
					  return;
				}
				$("#voucherWordId").val(node.id);
				$.post("${ctx}/voucher/getDefaultMessage",{voucherDate:$("#voucherDate").datebox('getValue'),voucherWordId:node.id},function(data){
					$("#voucherNumber").val(data.voucherNumber);
					$("#getLastResume").val(data.getLastResume);
					$("#editVoucherNumber").val(data.editVoucherNumber);
					if($("#editVoucherNumber").val()==1){
						$("#voucherNumber").attr("readonly","readonly");
					}else{
						$("#voucherNumber").removeAttr("readonly");
					}
				});
			},
			onLoadSuccess:function(){
				if(!'${obj.fid}'){
					  $("#voucherWordName").combotree("setValue","${voucherWordId}");
				  }else{
					  $("#voucherWordName").combotree("setValue","${obj.voucherWordId}");
				  }
			}
	  }); */
	  $("#voucherDate").fool('datebox',{
		  required:true,
		  missingMessage:'该项不能为空！',
		  novalidate:false,
		  inputDate:true,
		  validType:"voucherDate",
		  width:182,
		  height:32,
		  //editable:false,
		  onSelect:function(date){
			  var y = date.getFullYear();
			  var m = date.getMonth()+1;
			  var d = date.getDate();
			  if(m<9){
				  m="0"+m;
			  }
			  if(d<9){
				  d="0"+d;
			  }
			  var value=y+"-"+m+"-"+d;
			  if("${obj.voucherDate}"&&value=="${obj.voucherDate}"){
				  $("#voucherNumber").val("${obj.voucherNumber}");
				  $("#voucherWordId").val("${obj.voucherWordId}");
				  //$("#voucherWordName").combotree("setValue","${obj.voucherWordId}");
				  vouCombo.setComboValue("${obj.voucherWordId}");
				  $("#getLastResume").val("${getLastResume}");
				  $("#editVoucherNumber").val("${editVoucherNumber}");
				  if($("#editVoucherNumber").val()==1){
						$("#voucherNumber").attr("readonly","readonly");
				  }else{
						$("#voucherNumber").removeAttr("readonly");
				  }
				  return ;
			  }
			  $.post('${ctx}/voucher/getDefaultMessage',{voucherDate:$("#voucherDate").datebox('getValue')},function(data){
				  if(!$.isEmptyObject(data) && typeof data.voucherNumber == "undefined"){
					  $("#voucherNumber").val(1);
				  }else{
					  $("#voucherNumber").val(data.voucherNumber);
				  }
				  $("#voucherWordId").val(data.voucherWordId);
				  if(data.voucherWordId){
					  //$("#voucherWordName").combotree("setValue",data.voucherWordId);
					  vouCombo.setComboValue(data.voucherWordId);
				  }else{
					  //$("#voucherWordName").combotree("clear");
					  vouCombo.setComboValue("");
				  }
				  $("#getLastResume").val(data.getLastResume);
				  $("#editVoucherNumber").val(data.editVoucherNumber);
				  if($("#editVoucherNumber").val()==1){
						$("#voucherNumber").attr("readonly","readonly");
				  }else{
						$("#voucherNumber").removeAttr("readonly");
				  }
			  });
			  $("#voucherDate").datebox("enableValidation");
			  $("#voucherDate").datebox("validate");
		  },
		  onHidePanel:function(){
			  var value=$('#voucherDate').datebox('getValue');
			  if("${obj.voucherDate}"&&value=="${obj.voucherDate}"){
				  $("#voucherNumber").val("${obj.voucherNumber}");
				  $("#voucherWordId").val("${obj.voucherWordId}");
				  //$("#voucherWordName").combotree("setValue","${obj.voucherWordId}");
				  vouCombo.setComboValue("${obj.voucherWordId}");
				  $("#getLastResume").val("${getLastResume}");
				  $("#editVoucherNumber").val("${editVoucherNumber}");
				  if($("#editVoucherNumber").val()==1){
						$("#voucherNumber").attr("readonly","readonly");
				  }else{
						$("#voucherNumber").removeAttr("readonly");
				  }
				  return ;
			  }
			  $.post('${ctx}/voucher/getDefaultMessage',{voucherDate:$("#voucherDate").datebox('getValue')},function(data){
				  if(!$.isEmptyObject(data) && typeof data.voucherNumber == "undefined"){
					  $("#voucherNumber").val(1);
				  }else{
					  $("#voucherNumber").val(data.voucherNumber);
				  }
				  $("#voucherWordId").val(data.voucherWordId);
				  if(data.voucherWordId){
					  //$("#voucherWordName").combotree("setValue",data.voucherWordId);
					  vouCombo.setComboValue(data.voucherWordId);
				  }else{
					  //$("#voucherWordName").combotree("clear");
					  vouCombo.setComboValue("");
				  }
				  $("#getLastResume").val(data.getLastResume);
				  $("#editVoucherNumber").val(data.editVoucherNumber);
				  if($("#editVoucherNumber").val()==1){
						$("#voucherNumber").attr("readonly","readonly");
				  }else{
						$("#voucherNumber").removeAttr("readonly");
				  }
			  });
			  $("#voucherDate").datebox("enableValidation");
			  $("#voucherDate").datebox("validate");
		  }
	  });
	  var t = $("#voucherDate").datebox('textbox');
	  var date=t.parent().prev().datebox('panel');
	  t.unbind('keydown');
	  var datekd = '';
	  t.bind('keydown',datekd = function (e) {
		    var sIndex = date.find("td").index($('.calendar-nav-hover'));
			switch (e.keyCode) {
			case 37: // 左
			if(sIndex%7!=0){
			date.find('td').eq(sIndex).removeClass('calendar-nav-hover');
			date.find('td').eq(sIndex-1).addClass('calendar-nav-hover');
			}
			break;
			case 38: // 上
			if(sIndex>6){
				date.find('td').eq(sIndex).removeClass('calendar-nav-hover');
				date.find('td').eq(sIndex-7).addClass('calendar-nav-hover');
			}
			break;
			case 39: // 右
			if(sIndex%7!=6){
				date.find('td').eq(sIndex).removeClass('calendar-nav-hover');
				date.find('td').eq(sIndex+1).addClass('calendar-nav-hover');
			}
			break;
			case 40: // 下
			if(sIndex<date.find("td").length-1){
				date.find('td').eq(sIndex).removeClass('calendar-nav-hover');
				date.find('td').eq(sIndex+7).addClass('calendar-nav-hover');
			}
			break;
			case 219: // [键切换月份减1
			date.find(".calendar-prevmonth").click();
			date.find("td").eq(sIndex).addClass('calendar-nav-hover');
			break;
			case 221: // ]键切换月份加1
			date.find(".calendar-nextmonth").click();
			date.find("td").eq(sIndex).addClass('calendar-nav-hover');
			break;
			case 32: //空格键
			date.find('td').eq(sIndex).click();
			break;
			case 13: //回车
			if(sIndex!='-1'){
				date.find('td').eq(sIndex).click();
			}else{
				$("#voucherDate").datebox('hidePanel');
				$("#voucherDate").datebox('showPanel');
			}
			break;
			}
		}); 
	  $("#accessoryNumber").validatebox({
		  width:180,
		  height:32,
		  validType:'positiveInt',
	  });
	  $("#number").validatebox({
		  width:180,
		  height:32,
		  validType:'positiveInt',
	  });
	 
	  $("#currencyName").textbox({
		  validType:'isBank',
		  width:100,
		  disabled:true,
		  required:true,
		  missingMessage:'该项不能为空！',
		  novalidate:true,
	  });
	  $("#exchangeRate").validatebox({
		  validType:['isBank','exchangeRate'],
		  required:true,
		  missingMessage:'该项不能为空！',
		  novalidate:true,
	  });
	  $("#exchangeRate").change(function(){
		  var newValue = $(this).val();
		  valueGrid("exchangeRate",newValue);
		  if(newValue&&!editComplete()){
			  var debitAmount=getEditor(edIndex,"debitAmount").textbox("getValue");
			  var creditAmount=getEditor(edIndex,"creditAmount").textbox("getValue");
			  var currencyAmount="";
			  if(debitAmount&&debitAmount!=0){
				  currencyAmount=(debitAmount/newValue).toFixed(2);
			  }else if(creditAmount&&creditAmount!=0){
				  currencyAmount=(creditAmount/newValue).toFixed(2);
			  }
			  $("#currencyAmount").val(currencyAmount);
		  }
	  });
	  $("#currencyAmount").validatebox({
		  validType:['isBank','amount'],
		  required:true,
		  missingMessage:'该项不能为空！',
		  novalidate:true
	  });
	  $("#currencyAmount").change(function(){
		  var newValue = $(this).val();
		  valueGrid("currencyAmount",newValue);
	  });
	  $("#currencyAmount").keyup(function(){
		  var exchangeRate=$("#exchangeRate").val();
		  var newValue=$("#currencyAmount").val();
		  if(exchangeRate){
			  var amount=(newValue*exchangeRate).toFixed(2);
			  var debitAmount=getEditor(edIndex,"debitAmount").textbox("getValue");
			  var creditAmount=getEditor(edIndex,"creditAmount").textbox("getValue");
			  if((!debitAmount||debitAmount==0)&&(!creditAmount||creditAmount==0)){
				  getEditor(edIndex,"debitAmount").textbox("setValue",amount);
			  }else if(debitAmount&&debitAmount!=0){
				  getEditor(edIndex,"debitAmount").textbox("setValue",amount);
			  }else if(creditAmount&&creditAmount!=0){
				  getEditor(edIndex,"creditAmount").textbox("setValue",amount);
			  }
		  }
	  });
	  $("#unitName").textbox({
		  validType:'isBank',
		  width:100,
		  disabled:true,
		  required:true,
		  missingMessage:'该项不能为空！',
		  novalidate:true,
	  });
	  $("#quantity").validatebox({
		  validType:['isBank','intOrFloat'],
		  required:true,
		  missingMessage:'该项不能为空！',
		  novalidate:true
	  });
	  $("#quantity").change(function(){
		  var newValue = $(this).val();
		  valueGrid("quantity",newValue);
	  });
	  var deptCombo = $("#departmentName").fool("dhxCombo",{
		  required: true,
		  novalidate:true,
		  setTemplate:{
			  input:"#orgName#",
			  option:"#orgName#"
		  },
		  selectFirst:true,
		  data:organization,
		  validType:"valueValid['#departmentName']",
		  width:100,
		  height:22,
		  focusShow:true,
		  hasDownArrow:false,
		  onChange:function(value,text){
			  valueGrid("departmentName",text);
			  valueGrid("departmentId",value);
		  }
	  });
	  /* $("#departmentName").combotree({
		  width:100,
		  panelWidth:200,
		  disabled:true,
		  // url:"${ctx}/orgController/getAllTree",
		  data:organization,
		  required:true,
		  novalidate:true,
		  editable:false,
		  onLoadSuccess:function(node, data){
			  if(data[0].id!=""){
				  var node=$("#departmentName").combotree('tree').tree("find",data[0].id);
				  $("#departmentName").combotree('tree').tree('update',{
					  target: node.target,
					  text: '请选择',
					  id:"",
				  });
			  }
		  },
		  onBeforeSelect:function(node){
			  if(!node.id){
				  return false;
			  }
		  },
		  onSelect:function(record){
			  valueGrid("departmentName",record.text);
			  valueGrid("departmentId",record.id);
		  }
	  }); */
	  var wareCombo = $("#warehouseName").fool("dhxCombo",{
		  required:true,
		  novalidate:true,
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
		  selectFirst:true,
		  data:warehouse,
		  validType:"valueValid['#warehouseName']",
		  width:100,
		  height:22,
		  focusShow:true,
		  hasDownArrow:false,
		  onChange:function(value,text){
			  valueGrid("warehouseName",text);
			  valueGrid("warehouseId",value);
		  }
	  });
	  /* $("#warehouseName").combotree({
		  width:100,
		  panelWidth:200,
		  disabled:true,
		  // url:"${ctx}/basedata/warehourseList",
		  data:warehouse,
		  required:true,
		  novalidate:true,
		  editable:false,
		  onLoadSuccess:function(node, data){
			  if(data[0].id!=""){
				  var node=$("#warehouseName").combotree('tree').tree("find",data[0].id);
				  $("#warehouseName").combotree('tree').tree('update',{
					  target: node.target,
					  text: '请选择',
					  id:"",
				  });
			  }
		  },
		  onBeforeSelect:function(node){
			  if(!node.id){
				  return false;
			  }
		  },
		  onSelect:function(record){
			  valueGrid("warehouseName",record.text);
			  valueGrid("warehouseId",record.id);
		  }
	  }); */
	  var proCombo = $("#projectName").fool("dhxCombo",{
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
		  //selectFirst:true,
		  data:costType,
		  validType:"valueValid['#projectName']",
		  width:100,
		  height:22,
		  required:true,
	      novalidate:true,
		  focusShow:true,
		  hasDownArrow:false,
		  onChange:function(value,text){
			  var record = proCombo.getSelectedText();
				if(record != "" && !record.myflag){
					$.fool.alert({msg:"请选择子节点"});
					proCombo.setComboText("");
					return false;
				}
			  valueGrid("projectName",text);
			  valueGrid("projectId",value);
		  }
	  });
	  /* $("#projectName").combotree({
		  width:100,
		  panelWidth:200,
		  disabled:true,
		  // url:"${ctx}/costBill/fee",
		  data:costType,
		  required:true,
		  novalidate:true,
		  editable:false,
		  onLoadSuccess:function(node, data){
			  if(data[0].id!=""){
				  var node=$("#projectName").combotree('tree').tree("find",data[0].id);
				  $("#projectName").combotree('tree').tree('update',{
					  target: node.target,
					  text: '请选择',
					  id:"",
				  });
			  }
		  },
		  onBeforeSelect:function(node){
			  if(!node.id){
				  return false;
			  }
		  },
		  onSelect:function(record){
			  valueGrid("projectName",record.text);
			  valueGrid("projectId",record.id);
		  }
	  }); */
	  var cusCombo = $('#customerName').fool('dhxComboGrid',{
	    	width:100,
	    	height:22,
	    	required:true,
	    	novalidate:true,
	    	focusShow:true,
	    	validType:"noValueValid['customerId','customerName']",
	    	selectFirst:true,
	    	hasDownArrow:false,
	    	filterUrl:getRootPath()+'/customer/vagueSearch?searchSize=8',
	    	setTemplate:{
	    		input:"#name#",
	    		columns:[
	    					{option:'#code#',header:'编号',width:100},
	    					{option:'#name#',header:'名称',width:200},
	    				],
	    	},
	    	data:customer,
	    	onChange:function(value,text){
	    		var row = cusCombo.getSelectedText();
	    		if(!row){
	    			return false;
	    		}
				valueGrid('customerId',value);
				valueGrid('customerName',text);
	    	}
	    });
	  /* $('#customerName').fool('combogrid',{
		    width:100,
			height:20,
			required:true,
			novalidate:true,
			disabled:true,
			idField:'fid',
			textField:'name',
			panelWidth:300,
			panelHeight:283,
			fitColumns:true,
			missingMessage:'该项不能为空！',
			// url:getRootPath()+'/customer/list',
			data:customer,
			columns:[[
				{field:'fid',title:'fid',hidden:true},
				{field:'code',title:'编号',width:100,searchKey:false},
				{field:'name',title:'名称',width:100,searchKey:false},
				{field:'categoryName',title:'分类',width:100,searchKey:false},
				{field:'searchKey',hidden:true,searchKey:true},
			          ]],
			onSelect:function(index,row){
				$("#customerName").val(row.name);
				valueGrid('customerId',row.fid);
				valueGrid('customerName',row.name);
			},
			onChange:function(newValue, oldValue){
				if(!$("#customerName").combogrid("grid").datagrid("options").url&&$("#customerName").combogrid("getText")){
					$("#customerName").combogrid("grid").datagrid({
						url:getRootPath()+'/customer/list',
					});
				}
			}
		}); */
	    var supCombo = $('#supplierName').fool('dhxComboGrid',{
	    	width:100,
	    	height:22,
	    	required:true,
	    	novalidate:true,
	    	focusShow:true,
	    	selectFirst:true,
	    	validType:"noValueValid['supplierId','supplierName']",
	    	hasDownArrow:false,
	    	filterUrl:getRootPath()+'/supplier/vagueSearch?searchSize=8',
	    	setTemplate:{
	    		input:"#name#",
	    		columns:[
	    					{option:'#code#',header:'编号',width:100},
	    					{option:'#name#',header:'名称',width:200},
	    				],
	    	},
	    	data:supplier,
	    	onChange:function(value,text){
	    		var row = supCombo.getSelectedText();
	    		if(!row){
	    			return false;
	    		}
				valueGrid('supplierId',value);
				valueGrid('supplierName',text);
	    	}
	    });
		/* $("#supplierName").fool('combogrid',{
			width:100,
			height:20,
			required:true,
			novalidate:true,
			disabled:true,
			idField:'fid',
			textField:'name',
			panelWidth:300,
			panelHeight:283,
			fitColumns:true,
			missingMessage:'该项不能为空！',
			// url:getRootPath()+'/supplier/list',
			data:supplier,
			columns:[[
				{field:'fid',title:'fid',hidden:true},
				{field:'code',title:'编号',width:100,searchKey:false},
				{field:'name',title:'名称',width:100,searchKey:false},
				{field:'searchKey',hidden:true,searchKey:true},
			          ]],
			onSelect:function(index,row){
				$("#supplierName").val(row.name);
				valueGrid('supplierId',row.fid);
				valueGrid('supplierName',row.name);
			},
			onChange:function(newValue, oldValue){
				if(!$("#supplierName").combogrid("grid").datagrid("options").url&&$("#supplierName").combogrid("getText")){
					$("#supplierName").combogrid("grid").datagrid({
						url:getRootPath()+'/supplier/list',
					});
				}
			}
		}); */
		var memCombo = $('#memberName').fool('dhxComboGrid',{
	    	width:100,
	    	height:22,
	    	required:true,
	    	novalidate:true,
	    	focusShow:true,
	    	selectFirst:true,
	    	validType:"noValueValid['memberId','memberName']",
	    	hasDownArrow:false,
	    	filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8',
	    	setTemplate:{
	    		input:"#username#",
	    		columns:[
	    					{option:'#userCode#',header:'编号',width:100},
	    					{option:'#username#',header:'名称',width:200},
	    					{option:'#jobNumber#',header:'工号',width:100},
	    					{option:'#phoneOne#',header:'电话',width:100},
	    					{option:'#deptName#',header:'部门',width:100},
	    					{option:'#position#',header:'职位',width:100},
	    				],
	    	},
	    	data:member,
	    	onChange:function(value,text){
	    		var row = memCombo.getSelectedText();
	    		if(!row){
	    			return false;
	    		}
				valueGrid('memberId',value);
				valueGrid('memberName',text);
	    	}
	    });
		/* $("#memberName").fool('combogrid',{
			width:100,
			height:20,
			required:true,
			novalidate:true,
			disabled:true,
			idField:'fid',
			textField:'username',
			panelWidth:450,
			panelHeight:283,
			missingMessage:'该项不能为空！',
			fitColumns:true,
			// url:getRootPath()+'/member/list',
			data:member,
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
			onSelect:function(index,row){
				$("#memberName").val(row.username);
				valueGrid('memberId',row.fid);
				valueGrid('memberName',row.username);
			},
			onChange:function(newValue, oldValue){
				if(!$("#memberName").combogrid("grid").datagrid("options").url&&$("#memberName").combogrid("getText")){
					$("#memberName").combogrid("grid").datagrid({
						url:getRootPath()+'/member/list',
					});
				}
			}
		}); */
		var goodsCombo = $('#goodsName').fool('dhxComboGrid',{
	    	width:100,
	    	height:22,
	    	focusShow:true,
	    	selectFirst:true,
	    	required:true,
	    	novalidate:true,
	    	validType:"noValueValid['goodsId','goodsName']",
	    	hasDownArrow:false,
	    	filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=8',
	    	setTemplate:{
	    		input:"#name#",
	    		columns:[
	    					{option:'#code#',header:'编号',width:100},
	    					{option:'#name#',header:'名称',width:200},
	    					{option:'#barCode#',header:'条码',width:100},
	    					{option:'#spec#',header:'规格',width:100},
	    					{option:'#unitName#',header:'单位',width:100},
	    				],
	    	},
	    	data:goods,
	    	onChange:function(value,text){
	    		var row = goodsCombo.getSelectedText();
	    		if(!row){
	    			return false;
	    		}
				valueGrid('goodsId',value);
				valueGrid('goodsName',text);
	    	}
	    });
		/* $("#goodsName").fool('combogrid',{
			width:100,
			height:20,
			required:true,
			novalidate:true,
			disabled:true,
			idField:'fid',
			textField:'name',
			panelWidth:450,
			missingMessage:'该项不能为空！',
			fitColumns:true,
			// url:getRootPath()+'/goods/vagueSearch',
			data:goods,
			columns:[[
				{field:'fid',title:'fid',hidden:true},
				{field:'barCode',title:'条码',width:100,searchKey:false},
				{field:'name',title:'名称',width:100,searchKey:false},
				{field:'code',title:'编号',width:100,searchKey:false},
				{field:'spec',title:'规格',width:100,searchKey:false},
				{field:'unitName',title:'单位',width:100,searchKey:false},
				{field:'searchKey',hidden:true,searchKey:true},
			          ]],
			onSelect:function(index,row){
				$("#goodsName").val(row.name);
				valueGrid('goodsId',row.fid);
				valueGrid('goodsName',row.name);
			},
			onChange:function(newValue, oldValue){
				if(!$("#goodsName").combogrid("grid").datagrid("options").url&&$("#goodsName").combogrid("getText")){
					$("#goodsName").combogrid("grid").datagrid({
						url:getRootPath()+'/goods/vagueSearch',
					});
				}
			}
		}); */
		
		/* $('#customerName').combogrid('textbox').focus(function(){
			  $('#customerName').combogrid('showPanel');
		  });
		$('#supplierName').combogrid('textbox').focus(function(){
			  $('#supplierName').combogrid('showPanel');
		  });
		$('#memberName').combogrid('textbox').focus(function(){
			  $('#memberName').combogrid('showPanel');
		  });
		$('#goodsName').combogrid('textbox').focus(function(){
			  $('#goodsName').combogrid('showPanel');
		  });
		 */
	  //初始化凭证字号
	  if(!'${obj.fid}'){
		  $("#voucherDate").datebox('setValue',"${voucherDate}");
		  vouCombo.setComboValue("${voucherWordId}");
		  $("#voucherNumber").val("${voucherNumber}");
		  $("#voucherWordId").val("${voucherWordId}");
		  $("#getLastResume").val("${getLastResume}");
		  $("#editVoucherNumber").val("${editVoucherNumber}");
		  if($("#editVoucherNumber").val()==1){
				$("#voucherNumber").attr("readonly","readonly");
			}else{
				$("#voucherNumber").removeAttr("readonly");
			}
	  }else{
		  $("#voucherDate").datebox('setValue','${obj.voucherDate}');
		  vouCombo.setComboValue("${obj.voucherWordId}");
		  $("#voucherNumber").val("${obj.voucherNumber}");
		  $("#voucherWordId").val("${obj.voucherWordId}");
		  $("#getLastResume").val("${getLastResume}");
		  $("#editVoucherNumber").val("${editVoucherNumber}");
		  if($("#editVoucherNumber").val()==1){
				$("#voucherNumber").attr("readonly","readonly");
			}else{
				$("#voucherNumber").removeAttr("readonly");
			}
	  }
	  
	  function clearBut(target){
		  var gridSub = ($(target).closest(".dhxcombo_material").parent())[0].comboObj;
		  gridSub.detachEvent(gridSub.fevId);//删除原来的onDynXLS事件
		  gridSub.fevId = gridSub.attachEvent("onDynXLS",function(text){
				gridSub.clearAll();
				$.post('${ctx}/fiscalSubject/getSubject',{q:text},function(data){
				    gridSub.addOption(formatData(data,"fid"));
				    gridSub.openSelect();
				});
		  });
		  gridSub.clearAll();
			$.post('${ctx}/fiscalSubject/getSubject?q=',{},function(data){
			    gridSub.addOption(formatData(data,"fid"));
			    gridSub.openSelect();
			});
		  $(target).prev().html('');
		  $(target).parent().hide();
		  $(gridSub.getInput()).css({'width':$(gridSub.getInput()).parent().width()-27,"margin-left":"0px"});
	  }
	  
	  //明细列标初始化
	  var mylist = $("#detailList");
	  mylist.jqGrid({
		  datatype:"local",
		  data:rowData,
		  footerrow: true,
		  loadComplete:function(data){
			  /* if(rowData.length != 0){//载入数据
				  for( i in rowData){
					  mylist.jqGrid('addRowData', parseInt(i) + 1, rowData[i]);
				  }
			  } */
			  $grid=$("#gridBox").find(".ui-jqgrid-bdiv .ui-jqgrid-btable tbody");
			  loadFooter();
			  if('${param.mark}'=="1"){
				  var rows=rowData;
				  var debitAmount="";
				  var creditAmount="";
				  for(var i=0;i<rows.length;i++){
					  if(rows[i].debitAmount){
						  debitAmount=-(rows[i].debitAmount);
					  }
					  if(rows[i].creditAmount){
						  creditAmount=-(rows[i].creditAmount);
					  }
					  $("#detailList").jqGrid('setRowData',i+1,{
							  debitAmount: debitAmount,
							  creditAmount: creditAmount
					  });
					  debitAmount="";
					  creditAmount="";
				  }
				  //var number=parseInt($("#number").val())+1;
				  //$("#number").val(number);
				  loadFooter();
			  }
			  initialVo=$('#form').serialize();
			  initialDetail=mylist.jqGrid("getRowData");
		  },
		  //autowidth:true,//自动填满宽度
		  width:$("#gridBox").width()*0.95,
		  height:"100%",
		  forceFit:true,//改变列宽度，总宽度不变
		  onSelectRow:function(rowid,status){
			  var row=$('#detailList').jqGrid("getRowData",rowid);
			  if(status&&row.editing!=true&&editComplete()){
				  //console.log(row);
				  setInputer(row);
				  signInputer(row);
			  }
		  },
		  colModel : [ 
		                {name:'newRow',label:'newRow',hidden:true},
		                {name:'quantity',label:'quantity',hidden:true,editable:true,edittype:"text"},
		                {name:'exchangeRate',label:'exchangeRate',hidden:true,editable:true,edittype:"text"},
		                {name:'currencyAmount',label:'currencyAmount',hidden:true,editable:true,edittype:"text"},
		                {name:'unitName',label:'unitName',hidden:true,editable:true,edittype:"text"},
		                {name:'currencyName',label:'currencyName',hidden:true,editable:true,edittype:"text"},
		                {name:'supplierName',label:'supplierName',hidden:true,editable:true,edittype:"text"},
		                {name:'customerName',label:'customerName',hidden:true,editable:true,edittype:"text"},
		                {name:'departmentName',label:'departmentName',hidden:true,editable:true,edittype:"text"},
		                {name:'memberName',label:'memberName',hidden:true,editable:true,edittype:"text"},
		                {name:'warehouseName',label:'warehouseName',hidden:true,editable:true,edittype:"text"},
		                {name:'projectName',label:'projectName',hidden:true,editable:true,edittype:"text"},
		                {name:'goodsName',label:'goodsName',hidden:true,editable:true,edittype:"text"},
		                {name:'subjectName_',label:'subjectName_',hidden:true,editable:true,edittype:"text",formatter:function(value,options,row){
		                	return typeof row.subjectName!="undefined"?row.subjectName:value;
		                }},
		                {name:'unitId',label:'unitId',hidden:true,editable:true,edittype:"text"},
		                {name:'currencyId',label:'currencyId',hidden:true,editable:true,edittype:"text"},
		                {name:'supplierId',label:'supplierId',hidden:true,editable:true,edittype:"text"},
		                {name:'customerId',label:'customerId',hidden:true,editable:true,edittype:"text"},
		                {name:'departmentId',label:'departmentId',hidden:true,editable:true,edittype:"text"},
		                {name:'memberId',label:'memberId',hidden:true,editable:true,edittype:"text"},
		                {name:'warehouseId',label:'warehouseId',hidden:true,editable:true,edittype:"text"},
		                {name:'projectId',label:'projectId',hidden:true,editable:true,edittype:"text"},
		                {name:'goodsId',label:'goodsId',hidden:true,editable:true,edittype:"text"},
		                {name:'subjectId',label:'subjectId',hidden:true,editable:true,edittype:"text"},
		                {name:'subjectCode',label:'subjectCode',hidden:true,editable:true,edittype:"text"},
		                {name:'parentSubjectCode',label:'parentSubjectCode',hidden:true,editable:true,edittype:"text"},
		                {name:'parentSubjectName',label:'parentSubjectName',hidden:true,editable:true,edittype:"text"},

		                {name:'currencySign',label:'核算外币',hidden:true,editable:true,edittype:"text"},
		                {name:'supplierSign',label:'核算供应商',hidden:true,editable:true,edittype:"text"},
		                {name:'customerSign',label:'核算客户',hidden:true,editable:true,edittype:"text"},
		                {name:'departmentSign',label:'核算部门',hidden:true,editable:true,edittype:"text"},
		                {name:'memberSign',label:'核算职员',hidden:true,editable:true,edittype:"text"},
		                {name:'warehouseSign',label:'核算仓库',hidden:true,editable:true,edittype:"text"},
		                {name:'projectSign',label:'核算项目',hidden:true,editable:true,edittype:"text"},
		                {name:'goodsSign',label:'核算货品',hidden:true,editable:true,edittype:"text"},
		                {name:'quantitySign',label:'核算数量',hidden:true,editable:true,edittype:"text"},
		                {name:'resume',label:'摘要',align:'left',sortable:false,width:70,editable:true,edittype:"text"/* ,editor:{type:'combotree',options:{validType:'isBank',editable:true,data:voucherResume,onLoadSuccess:function(node, data){
		                	var row=mylist.datagrid("getSelected");
		                	var index=mylist.datagrid("getRowIndex",row);
		                	if(row.resume){
		                		$($("div#detailList").datagrid('getEditor',{index:index,field:'resume'}).target).combotree("setText",row.resume);
		                	}else if($("#getLastResume").val()==1&&row.newRow==1){
		                		var lastIndex=parseInt(index-1);
		                		if(index!=0){
		                			var lastResume=$("div#detailList").datagrid("getPanel").find("[datagrid-row-index="+lastIndex+"]").find("[field='resume']").children().text();
		                			$($("div#detailList").datagrid('getEditor',{index:index,field:'resume'}).target).combotree("setText",lastResume);
		                		}
		                	}
		                	$(this).find("div").eq(1).addClass('tree-node-hover');
		                	if(data[0].id!=""){
		                		var node=$(this).tree("find",data[0].id);
		                		$(this).tree('update',{
		        			    	target: node.target,
		        				    text: '请选择',
		        				    id:"",
		        			    });
		                	}
		                },onBeforeSelect:function(node){
		                	if(!node.id){
		                		return false;
		                	}
		                }}} */},
		                {name:'subjectName',label:'科目',align:'left',sortable:false,width:200,editable:true,edittype:"text"/* ,editor:{type:"combobox",options:{required:true,validType:'subjValid',valueField:"id",textField:"text",url:"${ctx}/fiscalSubject/getSubject",mode:"remote",onClickIcon:function(){
		                	$(this).combobox('hidePanel');
		                	$(this).combobox('textbox').blur();
		                	subjectChooser(this);
		                },onLoadSuccess:function(){
		                	$(this).next().find('.subj').attr('mytips',0);
		                	var items=$(this).combobox("panel").find(".combobox-item");
		                	if(items[0]){
		                		$(items[0]).attr("class","combobox-item combobox-item-hover");
		                	}
		                },onShowPanel:function(){
		                	var items=$(this).combobox("getData");
		                	if($(this).combobox("getText")==""&&items.length==0){
		                		$(this).combobox('reload','${ctx}/fiscalSubject/getSubject?q=');
		                		$(this).combobox('reload','${ctx}/fiscalSubject/getSubject');
		                	}
		                },
		                onSelect:function(record){
		                	var editor=$(this);
		                	if(getValue("subjectId")==record.fid){
		                		return false;
		                	}else{
		                		vkd!=''?editor.combobox('textbox').unbind('keydown',vkd):null;
		                		if(record.level >= 1){
		                			editor.combobox('textbox').bind('keydown',vkd=function(e){
		                				if(record.flag == 0 && e.keyCode == 39){
		                        			editor.next().find('.subj1').html(record.text);
		                        			editor.combobox('textbox').css({'width':(editor.parent().width()-editor.next().find('.subjn1').width()-40),'float':'right'});
		        	                		editor.next().find('.subjn1').show();
		        	                		editor.combobox('reload','${ctx}/fiscalSubject/getSubject?q=&parentId='+record.fid);//传一次q为空的参数去服务器显示下级科目数据
		        	                		editor.combobox('reload','${ctx}/fiscalSubject/getSubject?parentId='+record.fid);
		        	                		editor.combobox('clear');
		        	                		editor.combobox('textbox').focus();
		                				}else if(editor.combobox('getText')=='' && e.keyCode == 8){
		                					editor.next().find('.subjn1 a').click();
		                					editor.combobox('textbox').unbind('keydown',vkd);
		                				}
		                			});
			                		editor.combobox('textbox').focus();
		                		}
		                		if(record.flag==1){
		                			editor.next().find('.subj').attr('mytips',0);
			                		valueGrid('subjectName_',record.name);
			                		valueGrid('subjectId',record.fid);
			                		valueGrid('subjectCode',record.code);
			                		if(record.parentCode){
			                			valueGrid('parentSubjectCode',record.parentCode);
			                		}
			                		if(record.parentName){
			                			valueGrid('parentSubjectName',record.parentName);
			                		}
			                    	initialSign(record);
			                    	signInputer(record);
			                    	clearInputer();
			                    	setInputer(record);
		                		}else if(record.flag==0){
		                			valueGrid('subjectId','');
		                			editor.next().find('.subj').attr('mytips',1);
		                		}
		                	} 
		                	disableInputer(record,"enable");
		                }}} */,formatter:function(value,options,row){
		                	var parentNames="";
		                	if(row.subjectId){
		                		$.ajax({
		                    		url:"${ctx}/fiscalSubject/getParentNamesById",
		                    		async:false,
		                    		data:{id:row.subjectId},
		                    		success:function(data){
		                    			parentNames=data;
		                    		}
		                    	});
		                	}
		                	if(value=="合计"){
		                		return value;
		                	}
		                	var str="";
		                	if(row.subjectCode){
		                		str+=row.subjectCode+" ";
		                	};
		                	if(row.parentSubjectName&&row.parentSubjectName!="科目"){
		                		str+=parentNames;
		                	};
		                	if(value){
		                		str+=value;
		                	};
		                	if(row.supplierName){
		                		str+="|"+row.supplierName;
		                	};
		                	if(row.customerName){
		                		str+="|"+row.customerName;
		                	};
		                	if(row.departmentName){
		                		str+="|"+row.departmentName;
		                	};
		                	if(row.memberName){
		                		str+="|"+row.memberName;
		                	};
		                	if(row.warehouseName){
		                		str+="|"+row.warehouseName;
		                	};
		                	if(row.projectName){
		                		str+="|"+row.projectName;
		                	};
		                	if(row.goodsName){
		                		str+="|"+row.goodsName;
		                	};
		                	
		                	return str;
		                }},
		                {name:'debitAmount',label:'借方金额',sortable:false,align:'right',width:40,editable:true,edittype:"text"/* ,editor:{type:'textbox',options:{required:true,missingMessage:'该项不能为空！',validType:'amount',onChange:function(newValue, oldValue){
		                	if(newValue&&newValue!=0){
		                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("setText",'0');
		                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("disable");
		                	}else{
		                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("enable");
		                	}
		                }}} */,formatter:"number",formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, defaultValue: '0.00'}},
		                {name:'creditAmount',label:'贷方金额',sortable:false,align:'right',width:40,editable:true,edittype:"text"/* ,editor:{type:'textbox',options:{required:true,missingMessage:'该项不能为空！',validType:'amount',onChange:function(newValue, oldValue){
		                	if(newValue&&newValue!=0){
		                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("setText",'0');
		                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("disable");
		                	}else{
		                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("enable");
		                	}
		                }}} */,formatter:"number",formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, defaultValue: '0.00'}},
		                {name:'action',label:'操作',sortable:false,width:50,formatter:function(value,options,row){
		                	var index = options.rowId - 1 ;
		                	if(value||'${obj.recordStatus}'==1||'${obj.recordStatus}'==2||'${obj.recordStatus}'==3){
		                		return "";
		                	}
		                	if(row.editing){
		                		var s = '<a class="btn-save" id="btn-'+index+'-save" title="确认" href="javascript:;" onclick="saverow('+options.rowId+')"></a>';
		              		    var c = '<a class="btn-back" id="btn-'+index+'-back" title="撤销" href="javascript:;" onclick="cancelrow('+options.rowId+')"></a>';
		                        return s+" "+c;
		                	}else{
		                		var e= '<a class="btn-edit" id="btn-'+index+'-edit" title="编辑" href="javascript:;" onclick="editrow('+options.rowId+')"></a>';
		              		    var d= '<a class="btn-del" id="btn-'+index+'-del" title="删除" href="javascript:;" onclick="deleterow('+options.rowId+')"></a>';
			                    return e+" "+d;
		                	}
		                }}
		             ],
	  });
	  /* mylist.datagrid({
		  width:$("#gridBox").width()*0.95,
		  data:rowData,
		  autoRowHeight:true,
		  nowrap:false,
		  singleSelect:true,
		  scrollbarSize:0,
		  fitColumns:true,
		  showFooter:true,
		  columns:[[
	                {field:'newRow',title:'newRow',hidden:true},
	                {field:'quantity',title:'quantity',hidden:true},
	                {field:'exchangeRate',title:'exchangeRate',hidden:true},
	                {field:'currencyAmount',title:'currencyAmount',hidden:true},
	                {field:'unitName',title:'unitId',hidden:true},
	                {field:'currencyName',title:'currencyId',hidden:true},
	                {field:'supplierName',title:'supplierId',hidden:true},
	                {field:'customerName',title:'customerId',hidden:true},
	                {field:'departmentName',title:'departmentId',hidden:true},
	                {field:'memberName',title:'memberId',hidden:true},
	                {field:'warehouseName',title:'warehouseId',hidden:true},
	                {field:'projectName',title:'projectId',hidden:true},
	                {field:'goodsName',title:'goodsId',hidden:true},
	                {field:'subjectName_',title:'subjectName_',hidden:true,formatter:function(value,row,index){
	                	return row.subjectName;
	                }},
	                
	                {field:'unitId',title:'unitId',hidden:true},
	                {field:'currencyId',title:'currencyId',hidden:true},
	                {field:'supplierId',title:'supplierId',hidden:true},
	                {field:'customerId',title:'customerId',hidden:true},
	                {field:'departmentId',title:'departmentId',hidden:true},
	                {field:'memberId',title:'memberId',hidden:true},
	                {field:'warehouseId',title:'warehouseId',hidden:true},
	                {field:'projectId',title:'projectId',hidden:true},
	                {field:'goodsId',title:'goodsId',hidden:true},
	                {field:'subjectId',title:'subjectId',hidden:true},
	                {field:'subjectCode',title:'subjectCode',hidden:true},
	                {field:'parentSubjectCode',title:'parentSubjectCode',hidden:true},
	                {field:'parentSubjectName',title:'parentSubjectName',hidden:true},

	                {field:'currencySign',title:'核算外币',hidden:true},
	                {field:'supplierSign',title:'核算供应商',hidden:true},
	                {field:'customerSign',title:'核算客户',hidden:true},
	                {field:'departmentSign',title:'核算部门',hidden:true},
	                {field:'memberSign',title:'核算职员',hidden:true},
	                {field:'warehouseSign',title:'核算仓库',hidden:true},
	                {field:'projectSign',title:'核算项目',hidden:true},
	                {field:'goodsSign',title:'核算货品',hidden:true},
	                {field:'quantitySign',title:'核算数量',hidden:true},

	                {field:'resume',title:'摘要',align:'left',halign:'center',width:70,editor:{type:'combotree',options:{validType:'isBank',editable:true,data:voucherResume,onLoadSuccess:function(node, data){
	                	var row=mylist.datagrid("getSelected");
	                	var index=mylist.datagrid("getRowIndex",row);
	                	if(row.resume){
	                		$($("div#detailList").datagrid('getEditor',{index:index,field:'resume'}).target).combotree("setText",row.resume);
	                	}else if($("#getLastResume").val()==1&&row.newRow==1){
	                		var lastIndex=parseInt(index-1);
	                		if(index!=0){
	                			var lastResume=$("div#detailList").datagrid("getPanel").find("[datagrid-row-index="+lastIndex+"]").find("[field='resume']").children().text();
	                			$($("div#detailList").datagrid('getEditor',{index:index,field:'resume'}).target).combotree("setText",lastResume);
	                		}
	                	}
	                	$(this).find("div").eq(1).addClass('tree-node-hover');
	                	if(data[0].id!=""){
	                		var node=$(this).tree("find",data[0].id);
	                		$(this).tree('update',{
	        			    	target: node.target,
	        				    text: '请选择',
	        				    id:"",
	        			    });
	                	}
	                },onBeforeSelect:function(node){
	                	if(!node.id){
	                		return false;
	                	}
	                }}}},
	                {field:'subjectName',title:'科目',align:'left',halign:'center',width:200,editor:{type:"combobox",options:{required:true,validType:'subjValid',valueField:"id",textField:"text",url:"${ctx}/fiscalSubject/getSubject",mode:"remote",onClickIcon:function(){
	                	$(this).combobox('hidePanel');
	                	$(this).combobox('textbox').blur();
	                	subjectChooser(this);
	                },onLoadSuccess:function(){
	                	$(this).next().find('.subj').attr('mytips',0);
	                	var items=$(this).combobox("panel").find(".combobox-item");
	                	if(items[0]){
	                		$(items[0]).attr("class","combobox-item combobox-item-hover");
	                	}
	                },onShowPanel:function(){
	                	var items=$(this).combobox("getData");
	                	if($(this).combobox("getText")==""&&items.length==0){
	                		$(this).combobox('reload','${ctx}/fiscalSubject/getSubject?q=');
	                		$(this).combobox('reload','${ctx}/fiscalSubject/getSubject');
	                	}
	                },
	                onSelect:function(record){
	                	var editor=$(this);
	                	if(getValue("subjectId")==record.fid){
	                		return false;
	                	}else{
	                		vkd!=''?editor.combobox('textbox').unbind('keydown',vkd):null;
	                		if(record.level >= 1){
	                			editor.combobox('textbox').bind('keydown',vkd=function(e){
	                				if(record.flag == 0 && e.keyCode == 39){
	                        			editor.next().find('.subj1').html(record.text);
	                        			editor.combobox('textbox').css({'width':(editor.parent().width()-editor.next().find('.subjn1').width()-40),'float':'right'});
	        	                		editor.next().find('.subjn1').show();
	        	                		editor.combobox('reload','${ctx}/fiscalSubject/getSubject?q=&parentId='+record.fid);//传一次q为空的参数去服务器显示下级科目数据
	        	                		editor.combobox('reload','${ctx}/fiscalSubject/getSubject?parentId='+record.fid);
	        	                		editor.combobox('clear');
	        	                		editor.combobox('textbox').focus();
	                				}else if(editor.combobox('getText')=='' && e.keyCode == 8){
	                					editor.next().find('.subjn1 a').click();
	                					editor.combobox('textbox').unbind('keydown',vkd);
	                				}
	                			});
		                		editor.combobox('textbox').focus();
	                		}
	                		if(record.flag==1){
	                			editor.next().find('.subj').attr('mytips',0);
		                		valueGrid('subjectName_',record.name);
		                		valueGrid('subjectId',record.fid);
		                		valueGrid('subjectCode',record.code);
		                		if(record.parentCode){
		                			valueGrid('parentSubjectCode',record.parentCode);
		                		}
		                		if(record.parentName){
		                			valueGrid('parentSubjectName',record.parentName);
		                		}
		                    	initialSign(record);
		                    	signInputer(record);
		                    	clearInputer();
		                    	setInputer(record);
	                		}else if(record.flag==0){
	                			valueGrid('subjectId','');
	                			editor.next().find('.subj').attr('mytips',1);
	                		}
	                	} 
	                	disableInputer(record,"enable");
	                }}},formatter:function(value,row,index){
	                	var parentNames="";
	                	if(row.subjectId){
	                		$.ajax({
	                    		url:"${ctx}/fiscalSubject/getParentNamesById",
	                    		async:false,
	                    		data:{id:row.subjectId},
	                    		success:function(data){
	                    			parentNames=data;
	                    		}
	                    	});
	                	}
	                	if(value=="合计"){
	                		return value;
	                	}
	                	var str="";
	                	if(row.subjectCode){
	                		str+=row.subjectCode+" ";
	                	};
	                	if(row.parentSubjectName&&row.parentSubjectName!="科目"){
	                		str+=parentNames;
	                	};
	                	if(value){
	                		str+=value;
	                	};
	                	if(row.supplierName){
	                		str+="|"+row.supplierName;
	                	};
	                	if(row.customerName){
	                		str+="|"+row.customerName;
	                	};
	                	if(row.departmentName){
	                		str+="|"+row.departmentName;
	                	};
	                	if(row.memberName){
	                		str+="|"+row.memberName;
	                	};
	                	if(row.warehouseName){
	                		str+="|"+row.warehouseName;
	                	};
	                	if(row.projectName){
	                		str+="|"+row.projectName;
	                	};
	                	if(row.goodsName){
	                		str+="|"+row.goodsName;
	                	};
	                	
	                	return str;
	                }},
	                {field:'debitAmount',title:'借方金额',align:'right',halign:'center',width:40,editor:{type:'textbox',options:{required:true,missingMessage:'该项不能为空！',validType:'amount',onChange:function(newValue, oldValue){
	                	if(newValue&&newValue!=0){
	                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("setText",'0');
	                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("disable");
	                	}else{
	                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("enable");
	                	}
	                }}},formatter:function(value){
	                	if(value){
	                		value = (parseFloat(value)).toFixed(2) +"";
	    		  			var re=/(-?\d+)(\d{3})/ ;
	    		  			while(re.test(value)){
	    		  				value=value.replace(re,"$1,$2"); 
	    		  			}
	    		  			return value;
	                	}else{
	                		return 0.00;
	                	}
					}},
	                {field:'creditAmount',title:'贷方金额',align:'right',halign:'center',width:40,editor:{type:'textbox',options:{required:true,missingMessage:'该项不能为空！',validType:'amount',onChange:function(newValue, oldValue){
	                	if(newValue&&newValue!=0){
	                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("setText",'0');
	                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("disable");
	                	}else{
	                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("enable");
	                	}
	                }}},formatter:function(value){
	                	if(value){
	                		value = (parseFloat(value)).toFixed(2) +"";
	    		  			var re=/(-?\d+)(\d{3})/ ;
	    		  			while(re.test(value)){
	    		  				value=value.replace(re,"$1,$2"); 
	    		  			}
	    		  			return value;
	                	}else{
	                		return 0.00;
	                	}
					}},
	                {field:'action',title:'操作',width:50,formatter:function(value,row,index){
	                	if(value||'${obj.recordStatus}'==1||'${obj.recordStatus}'==2||'${obj.recordStatus}'==3){
	                		return "";
	                	}
	                	if(row.editing){
	                		var s = '<a class="btn-save" id="btn-'+index+'-save" title="确认" href="javascript:;" onclick="saverow(this)"></a>';
	              		    var c = '<a class="btn-back" id="btn-'+index+'-back" title="撤销" href="javascript:;" onclick="cancelrow(this)"></a>';
	                        return s+" "+c;
	                	}else{
	                		var e= '<a class="btn-edit" id="btn-'+index+'-edit" title="编辑" href="javascript:;" onclick="editrow(this)"></a>';
	              		    var d= '<a class="btn-del" id="btn-'+index+'-del" title="删除" href="javascript:;" onclick="deleterow(this)"></a>';
		                    return e+" "+d;
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
		  },
		  onCancelEdit:function(index,row){
			  row.editing = false;
			  updateActions(index);
			  treef.unbind('keydown',treefkd);
			  boxf.unbind('keydown',boxfkd);
			  tbox1.unbind('keydown',tbox1kd);
			  tbox2.unbind('keydown',tbox2kd);
		  },
		  onEndEdit:function(index,row,changes){
			  treef.unbind('keydown',treefkd);
			  boxf.unbind('keydown',boxfkd);
			  tbox1.unbind('keydown',tbox1kd);
			  tbox2.unbind('keydown',tbox2kd);  
		  },
		  onLoadSuccess:function(data){
			  $grid=$("#gridBox").find(".datagrid").find(".datagrid-view2").find(".datagrid-body");
			  loadFooter();
			  if('${param.mark}'=="1"){
				  var rows=data.rows;
				  var debitAmount="";
				  var creditAmount="";
				  for(var i=0;i<rows.length;i++){
					  if(rows[i].debitAmount){
						  debitAmount=-(rows[i].debitAmount);
					  }
					  if(rows[i].creditAmount){
						  creditAmount=-(rows[i].creditAmount);
					  }
					  $("#detailList").datagrid('updateRow',{
						  index: i,
						  row: {
							  debitAmount: debitAmount,
							  creditAmount: creditAmount
						  }
					  });
					  debitAmount="";
					  creditAmount="";
				  }
				  var number=parseInt($("#number").val())+1;
				  $("#number").val(number);
				  loadFooter();
			  }
			  initialVo=$('#form').serialize();
		  },
		  onSelect:function(index,row){
			  if(row.editing!=true&&editComplete()){
				  signInputer(row);
				  setInputer(row);
			  }
		  }
	  }); */
	  
	  /* $grid.on("keyup","input.textbox-text",function(e){
		  var newValue="";
		  if($(this).closest(".datagrid-cell").parent().attr("field")=="debitAmount"){
			  newValue=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("getText");  
		  }else if($(this).closest(".datagrid-cell").parent().attr("field")=="creditAmount"){
			  newValue=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'creditAmount'}).target).textbox("getText");
		  }else{
			  return false;
		  }
		  if(getValue("currencySign")==1){
			  var exchangeRate=$("#exchangeRate").textbox("getValue");
			  if(exchangeRate){
				  var currencyAmount=(newValue/exchangeRate).toFixed(2);
				  $("#currencyAmount").textbox("setValue",currencyAmount);
			  }
		  }
	  }); */
	  
	  //判断状态，初始化表单及按钮
	  if('${obj.recordStatus}'==null||'${obj.recordStatus}'==''){
		  $("#btnBox").append('<input type="button" id="save" class="mybtn-blue mybtn-s" value="保存" /> <input type="button" id="nextVoucher" class="mybtn-blue mybtn-s" value="保存并新增" />');
	  }else if('${obj.recordStatus}'==0){
		  $("#btnBox").append('<input type="button" id="save" class="mybtn-blue mybtn-s" value="保存" /> <input type="button" id="prevVoucher" class="mybtn-blue mybtn-s" value="上一张凭证" /> <input type="button" id="nextVoucher" class="mybtn-blue mybtn-s" value="下一张凭证" /> <input type="button" id="print" class="mybtn-blue mybtn-s" value="打印" /> <input type="button" id="verify" class="mybtn-blue mybtn-s" value="审核"/> <input type="button" id="cancel" class="mybtn-blue mybtn-s" value="作废"/> <input type="button" id="writeOff" class="mybtn-blue mybtn-s" value="冲销"/> ');
	  }else if('${obj.recordStatus}'==1){
		  $("#form").find("input").attr('disabled','disabled');
		  vouCombo.disable();
		  $("#addDetail").hide();
		  $("#btnBox").append('<input type="button" id="prevVoucher" class="mybtn-blue mybtn-s" value="上一张凭证" /> <input type="button" id="nextVoucher" class="mybtn-blue mybtn-s" value="下一张凭证" /> <input type="button" id="print" class="mybtn-blue mybtn-s" value="打印" /> <input type="button" id="unVerify" class="mybtn-blue mybtn-s" value="反审核"/> <input type="button" id="cancel" class="mybtn-blue mybtn-s" value="作废"/> <input type="button" id="writeOff" class="mybtn-blue mybtn-s" value="冲销"/> <input type="button" id="sign" class="mybtn-blue mybtn-s" value="签字"/>');
	  }else if('${obj.recordStatus}'==2){
		  $("#form").find("input").attr('disabled','disabled');
		  vouCombo.disable();
		  $("#addDetail").hide();
		  $("#btnBox").append('<input type="button" id="prevVoucher" class="mybtn-blue mybtn-s" value="上一张凭证" /> <input type="button" id="nextVoucher" class="mybtn-blue mybtn-s" value="下一张凭证" /> <input type="button" id="print" class="mybtn-blue mybtn-s" value="打印" /> <input type="button" id="unCancel" class="mybtn-blue mybtn-s" value="反作废"/> <input type="button" id="writeOff" class="mybtn-blue mybtn-s" value="冲销"/>');
	  }else if('${obj.recordStatus}'==3){
		  $("#form").find("input").attr('disabled','disabled');
		  vouCombo.disable();
		  $("#addDetail").hide();
		  $("#btnBox").append('<input type="button" id="prevVoucher" class="mybtn-blue mybtn-s" value="上一张凭证" /> <input type="button" id="nextVoucher" class="mybtn-blue mybtn-s" value="下一张凭证" /> <input type="button" id="print" class="mybtn-blue mybtn-s" value="打印" />');
	  }
	  
	  //展开、收起其他信息
	  $("#openBtn").click(function(e) {
				  $(".list2 .hideOut").css("display","inline-block");
				  $('#openBtn').css("display","none");
				  $('#closeBtn').css("display","inline-block");
	  });
	  $("#closeBtn").click(function(e) {
				  $(".list2 .hideOut").css("display","none");
				  $('#openBtn').css("display","inline-block");
				  $('#closeBtn').css("display","none");	
	  });
	  
	  //列表方法
	  /* function updateActions(index){
		  $('#detailList').datagrid('updateRow',{
			  index: index,
			  row:{}
		  });
	  } */
	  /* function getRowIndex(target){
		  var tr = $(target).closest('tr.datagrid-row');
		  return parseInt(tr.attr('datagrid-row-index'));
	  } */
	  function deleterow(index){
		  $('#detailList').jqGrid('delRowData', index);
		  loadFooter();
	  }
	  function saverow(index){
		  setTimeout(function(){
			  //var index=getRowIndex(target);
			  var row=$grid.find("tr.jqgrow[id='"+edIndex+"']");
			  var subjectId=getValue("subjectId");
			  var subEditor = (getEditor(index,"subjectName").next())[0].comboObj;
			  var resEditor = (getEditor(index,"resume").next())[0].comboObj;
			  var subjectName=subEditor.getComboText();
			  var resume=resEditor.getComboText();
			  var debitAmount=parseFloat(getEditor(index,"debitAmount").textbox('getText')).toFixed(2);
			  getEditor(index,"debitAmount").textbox("setValue",debitAmount);
			  var creditAmount=parseFloat(getEditor(index,"creditAmount").textbox('getText')).toFixed(2);
			  getEditor(index,"creditAmount").textbox("setValue",creditAmount);
			  if(debitAmount==0&&creditAmount==0){
				  $.fool.alert({msg:"借方与贷方金额不能同时为0！",fn:function(){
					  getEditor(index,"debitAmount").textbox('textbox').focus().select();
				  }});
				  return false;
			  }
			  var unitId=getValue("unitId");
			  var currencyId=getValue("currencyId");
			  var supplierId=getValue("supplierId");
			  var customerId=getValue("customerId");
			  var departmentId=getValue("departmentId");
			  var memberId=getValue("memberId");
			  var warehouseId=getValue("warehouseId");
			  var projectId=getValue("projectId");
			  var goodsId=getValue("goodsId");
			  var unitName=$('#unitName').textbox("getValue");
			  var currencyName=$("#currencyName").textbox("getValue");
			  var supplierName=supCombo.getComboText();
			  var customerName=cusCombo.getComboText();
			  var departmentName=deptCombo.getComboText();
			  var memberName=memCombo.getComboText();
			  var warehouseName=wareCombo.getComboText();
			  var projectName=proCombo.getComboText();
			  var goodsName=goodsCombo.getComboText();
			  var quantity=$("#quantity").val();
			  var exchangeRate=$("#exchangeRate").val();;
			  var currencyAmount=$("#currencyAmount").val();;
			  var currencySign=getValue("currencySign");
			  var supplierSign=getValue("supplierSign");
			  var customerSign=getValue("customerSign");
			  var departmentSign=getValue("departmentSign");
			  var memberSign=getValue("memberSign");
			  var warehouseSign=getValue("warehouseSign");
			  var projectSign=getValue("projectSign");
			  var goodsSign=getValue("goodsSign");
			  var quantitySign=getValue("quantitySign");
			  var parentSubjectCode=getValue("parentSubjectCode");
			  var parentSubjectName=getValue("parentSubjectName");
			  var subjectCode=getValue("subjectCode");
			  var subjectName_=getValue("subjectName_");
			  var bool = true;
			  if(subjectId){
				  $.ajax({
					  url:"${ctx}/fiscalSubject/getById",
					  async:false,
		              data:{fid:subjectId},
		              success:function(data){
		            	  if(data){
		    				  if(data.name!=subjectName_){
		    					  subEditor.setComboValue('');$(subEditor.getInput()).focus();
		    					  bool = false;
		    				  }
		    			  }else{
		    				  subEditor.setComboValue('');$(subEditor.getInput()).focus();
		    				  bool = false;
		    			  }
		              }
				  });
			  }else{
				  subEditor.setComboValue('');
				  $(subEditor.getInput()).focus();
				  return false;
			  }
			  if(!bool){return false;}
			  $("#signBox").form("enableValidation");
			  if(row.form('validate') && $("#signBox").form("validate")){
				  $('#detailList').jqGrid('saveRow', index);
				  $('#detailList').jqGrid('setRowData',index,{
						  'subjectId':subjectId,
						  'subjectName':subjectName_,
						  'resume':resume,
						  'debitAmount':debitAmount,
						  'creditAmount':creditAmount,
						  'unitId':unitId,
						  'currencyId':currencyId,
						  'supplierId':supplierId,
						  'customerId':customerId,
						  'departmentId':departmentId,
						  'memberId':memberId,
						  'warehouseId':warehouseId,
						  'projectId':projectId,
						  'goodsId':goodsId,
						  'unitName':unitName,
						  'currencyName':currencyName,
						  'supplierName':supplierName,
						  'customerName':customerName,
						  'departmentName':departmentName,
						  'memberName':memberName,
						  'warehouseName':warehouseName,
						  'projectName':projectName,
						  'goodsName':goodsName,
						  'quantity':quantity,
						  'exchangeRate':exchangeRate,
						  'currencyAmount':currencyAmount,
						  'quantitySign':quantitySign,
						  'currencySign':currencySign,
						  'supplierSign':supplierSign,
						  'customerSign':customerSign,
						  'departmentSign':departmentSign,
						  'memberSign':memberSign,
						  'warehouseSign':warehouseSign,
						  'projectSign':projectSign,
						  'goodsSign':goodsSign,
						  'parentSubjectCode':parentSubjectCode,
						  'parentSubjectName':parentSubjectName,
						  'newRow':0,
						  'subjectCode':subjectCode
					  }
				  );
				  disableInputer("","disable");
				  loadFooter();
				  hideBtn();
				  $('#detailList').jqGrid('setRowData', index, {editing:false,action:null});//编辑状态转换，按钮变化
				  edIndex="";
				  return false;
			  }else{
				  return false;
			  }
		  }, 100);
	  }
	  function cancelrow(index){
		  hideBtn();
		  //$('#detailList').jqGrid('setSelection', edIndex);
		  var row=$('#detailList').jqGrid('getRowData', edIndex);
		  if(row.newRow!=1){
			  $('#detailList').jqGrid('restoreRow', index);
			  $('#detailList').jqGrid('setRowData', index, {editing:false,action:null});//编辑状态转换，按钮变化
			  row=$('#detailList').jqGrid('getRowData', edIndex);
			  signInputer(row);
			  setInputer(row);
			  disableInputer("","disable");
		  }else{
			  $('#detailList').jqGrid('delRowData', index);
			  $('#detailList').jqGrid('setRowData', index, {editing:false,action:null});//编辑状态转换，按钮变化
			  signInputer("");
			  disableInputer("","disable");
		  }
	  }
	  function getEditor(rowId,name){
		  var editor = $('#'+rowId+"_"+name);
		  return editor;
	  }
	  function editrow(index){
		  var rows=$('#detailList').jqGrid('getRowData');
		  var rowData=mylist.jqGrid("getRowData",index);
		  var editKey = rowData.newRow == 1?0:1;
		  if(!editComplete()&&rows.length>0){
			  $.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！'});
			  return false;
		  }
		  //var index=getRowIndex(target);
		  edIndex=index;
		  var resume=getValue("resume");
		  var debitStr = getValue("debitAmount");
		  var creditStr = getValue("creditAmount");
		  $('#detailList').jqGrid('setSelection', index);
		  hideBtn(1);
		  var q = getValue("subjectCode");
		  var subjid = getValue("subjectId");
		  $('#detailList').jqGrid('setRowData', index, {subjectName:rowData.subjectName_});
		  $('#detailList').jqGrid('editRow', index);
		  $('#detailList').jqGrid('setRowData', index, {editing:true,action:null});//编辑状态转换，按钮变化
		  var editor=getEditor(index,"subjectName");
		  var gridSub = editor.fool("dhxComboGrid",{
			  height:"80%",
			  width:"100%",
			  filterUrl:"${ctx}/fiscalSubject/getSubject?num="+Math.random(),
			  searchKey:"q",
			  data:subject,
			  required:true,
			  validType:['subjValid','mysubjValid['+index+']'],
			  novalidate:false,
		      focusShow:true,
		      onlySelect:true,
		      //selectFirst:true,
		      setTemplate:{
		    		input:"#code# #name#",
		    		columns:[
		    					{option:'#code#',header:'科目编码',width:100},
		    					{option:'#name#',header:'名称',width:300},
		    				],
		      },
		      onSelectionChange:function(){
		    		$(gridSub.getInput()).blur();
		    		//gridSub.openSelect();//让onchange生效
		      },
		      onChange:function(value,text){
		    	  	var rowdata = gridSub.getOption(value);
		    	  	if(rowdata == null || editKey == 1){
				  		return false;
				  	}
				  	var record = rowdata.text;
		    	  	var editor=$(this);
		        		vkd!=''?$(gridSub.getInput()).unbind('keydown',vkd):null;
		        		if(record.level >= 1){
		        			$(gridSub.getInput()).bind('keydown',vkd=function(e){
		        				var subjn1 = $(gridSub.getInput()).parent().find(".subjn1");
		        				if(record.flag == 0 && e.keyCode == 39){
		        					subjn1.find(".subj1").html(record.text);
		        					subjn1.css("font-size","12px");
		        					$(gridSub.getInput()).css({'width':$(gridSub.getInput()).closest("td[aria-describedby]").width()-subjn1.width(),'margin-left':subjn1.width()});
		        					subjn1.show();
		        					gridSub.detachEvent(gridSub.fevId);//删除原来的onDynXLS事件
		        					gridSub.fevId = gridSub.attachEvent("onDynXLS",function(text){
		    							  gridSub.clearAll();
		    						      $.post('${ctx}/fiscalSubject/getSubject?parentId='+record.fid,{q:text},function(data){
		    						    	  gridSub.addOption(formatData(data,"fid"));
		    						    	  $(gridSub.getInput()).focus();
		    						      });
		    						});
		        					gridSub.setComboText("");
			                		// editor.combobox('reload','${ctx}/fiscalSubject/getSubject?q=&parentId='+record.fid);//传一次q为空的参数去服务器显示下级科目数据
			                		//editor.combobox('reload','${ctx}/fiscalSubject/getSubject?parentId='+record.fid);
			                		//editor.combobox('clear');
			                		//editor.combobox('textbox').focus();
		        				}else if(gridSub.getComboText()=='' && e.keyCode == 8){
		        					subjn1.find('a').click();
		        					$(gridSub.getInput()).unbind('keydown',vkd);
		        				}
		        			});
		        			$(gridSub.getInput()).focus();
		        		}
		        		if(record.flag==1){
		        			$(gridSub.getInput()).parent().find(".subjn1").attr('mytips',0);
		            		valueGrid('subjectName_',record.name);
		            		valueGrid('subjectId',record.fid);
		            		valueGrid('subjectCode',record.code);
		            		if(record.parentCode){
		            			valueGrid('parentSubjectCode',record.parentCode);
		            		}
		            		if(record.parentName){
		            			valueGrid('parentSubjectName',record.parentName);
		            		}
		            		disableInputer(record,"enable");
		            		clearInputer();
		                	initialSign(record);
		                	setInputer(record);
		                	signInputer(record);
		                	enterController('signBox',true);
			      			var signIndex = 0 , myindex = '';
			      			var tbox1 = getEditor(index,"debitAmount").textbox("textbox"),tbox2=getEditor(index,"creditAmount").textbox("textbox");
			      			var max = $('#signBox p').length;
			      			$('#signBox p').each(function(){myindex = max - ($(this).css('display')=='inline-block'?signIndex = 0:++signIndex) - 1;});
			      			$('#signBox p').eq(myindex).find('input[type=text]').bind('keydown',function(e){
			      				if(e.keyCode == 13){
			      					tbox1.attr('disabled')=='disabled'?tbox2.focus():tbox1.focus();;
			      				}
			      			});
		        		}else if(record.flag==0){
		        			clearInputer();
		        			valueGrid('subjectId','');
		        			$(gridSub.getInput()).parent().find(".subjn1").attr('mytips',1);
		        			disableInputer(record,"disable");
		        		}
		        		getEditor(index,"subjectName").val(record.name);//修复撤销后科目出现undefined的问题
		        	return true;
		      }
		  });
		  $(gridSub.getInput()).bind('keydown',function(e){
			  if(!gridSub.getSelectedText() && e.keyCode == 13){
					gridSub.selectOption(0,true,true);
				}
		  });
		  gridSub.disableAutocomplete();//为了让IE下拉上下键选择时直接选中
		  $(gridSub.getButton()).click(function(){//定义下拉按钮点击后弹窗
			  subjectChooser(index);
		  });
		  if(rowData.subjectId != ''){
			  gridSub.setComboValue(rowData.subjectId);
			  gridSub.closeAll();//防止赋值后自动弹出下拉框
		  }
		  $(gridSub.getInput()).validatebox("validate");//修复点击原有数据编辑后，输入框有值却出现验证为空的情况
		  $(gridSub.getInput()).parent().append('<div class="subjn1 subj" style="display:none;"><div class="subj1" style="line-height:'+$(gridSub.getInput()).height()+'px"></div><a href="javascript:;" title="清除" onclick="clearBut(this)"><img src="/ops-pc/resources/images/cancel.png" style="margin-top:'+($(gridSub.getInput()).height()-15)/2+'px"></a></div>');
		  $(gridSub.getInput()).closest('.subjn1').attr('mytips',0);
		  /* editor.combobox({required:true,validType:'subjValid',valueField:"id",textField:"text",url:"${ctx}/fiscalSubject/getSubject",mode:"remote",onClickIcon:function(){
          	$(this).combobox('hidePanel');
        	$(this).combobox('textbox').blur();
        	subjectChooser(this);
        },onLoadSuccess:function(){
        	$(this).next().find('.subj').attr('mytips',0);
        	var items=$(this).combobox("panel").find(".combobox-item");
        	if(items[0]){
        		$(items[0]).attr("class","combobox-item combobox-item-hover");
        	}
        },onShowPanel:function(){
        	var items=$(this).combobox("getData");
        	if($(this).combobox("getText")==""&&items.length==0){
        		$(this).combobox('reload','${ctx}/fiscalSubject/getSubject?q=');
        		$(this).combobox('reload','${ctx}/fiscalSubject/getSubject');                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
        	}
        },
        onSelect:function(record){
        	var editor=$(this);
        	if(getValue("subjectId")==record.fid){
        		return false;
        	}else{
        		vkd!=''?editor.combobox('textbox').unbind('keydown',vkd):null;
        		if(record.level >= 1){
        			editor.combobox('textbox').bind('keydown',vkd=function(e){
        				if(record.flag == 0 && e.keyCode == 39){
                			editor.next().find('.subj1').html(record.text);
                			editor.combobox('textbox').css({'width':(editor.parent().width()-editor.next().find('.subjn1').width()-40),'float':'right'});
	                		editor.next().find('.subjn1').show();
	                		editor.combobox('reload','${ctx}/fiscalSubject/getSubject?q=&parentId='+record.fid);//传一次q为空的参数去服务器显示下级科目数据
	                		editor.combobox('reload','${ctx}/fiscalSubject/getSubject?parentId='+record.fid);
	                		editor.combobox('clear');
	                		editor.combobox('textbox').focus();
        				}else if(editor.combobox('getText')=='' && e.keyCode == 8){
        					editor.next().find('.subjn1 a').click();
        					editor.combobox('textbox').unbind('keydown',vkd);
        				}
        			});
            		editor.combobox('textbox').focus();
        		}
        		if(record.flag==1){
        			editor.next().find('.subj').attr('mytips',0);
            		valueGrid('subjectName_',record.name);
            		valueGrid('subjectId',record.fid);
            		valueGrid('subjectCode',record.code);
            		if(record.parentCode){
            			valueGrid('parentSubjectCode',record.parentCode);
            		}
            		if(record.parentName){
            			valueGrid('parentSubjectName',record.parentName);
            		}
                	initialSign(record);
                	signInputer(record);
                	clearInputer();
                	setInputer(record);
        		}else if(record.flag==0){
        			valueGrid('subjectId','');
        			editor.next().find('.subj').attr('mytips',1);
        		}
        	} 
        	disableInputer(record,"enable");
        }}); */
	  	  /* editor.next().append('<div class="subjn1 subj" style="display:none;"><div class="subj1"></div><a href="javascript:;" title="清除" onclick="clearBut(this)"><img src="/ops/resources/images/cancel.png"></a></div>');
	  	  editor.combobox('reload','${ctx}/fiscalSubject/getSubject?q='+q);
	  	  editor.combobox('textbox').next().val(subjid);
	  	  editor.combobox('reload','${ctx}/fiscalSubject/getSubject'); */
	  	  var resumeEditor = getEditor(index,"resume");
	  	  var gridRes = resumeEditor.fool("dhxComboGrid",{
	  		  height:"80%",
			  width:"100%",
			  data:voucherResume,
		      focusShow:true,
			  filterUrl:'${ctx}/basedata/fuzzyFindSubAuxiliaryAttrTree?code=014&num='+Math.random(),
		      //selectFirst:true,
              setTemplate:{
                  input:"#name#",
                  columns:[
                      {option:'#name#',header:'内容',width:100},
                  ],
              },
              toolsBar: {
                  name: "摘要",
                  addUrl: "/basedata/listAuxiliaryAttr",
                  refresh: true
              },
	  	});
          gridRes.hdr = null;
	  	gridRes.setComboText(rowData.resume);
	  	var row=rowData;
    	if(row.resume){
    		gridRes.setComboText(row.resume);
    	}else if($("#getLastResume").val()==1&&row.newRow==1){
    		var lastIndex=parseInt(index-1);
    		if(index!=1){
    			var lastResume=$grid.find(".ui-row-ltr[id='"+lastIndex+"']").find('td[aria-describedby=detailList_resume]').text();
    			gridRes.setComboText(lastResume);
    		}
    	}
	  	  /* resumeEditor.combotree({validType:'isBank',editable:true,data:voucherResume,onLoadSuccess:function(node, data){
        	var row=rowData;
        	if(row.resume){
        		getEditor(index,"resume").combotree("setText",row.resume);
        	}else if($("#getLastResume").val()==1&&row.newRow==1){
        		var lastIndex=parseInt(index-1);
        		if(index!=1){
        			var lastResume=$grid.find(".jqgrow ui-row-ltr[id='"+lastIndex+"']").find('td[aria-describedby="detailList_resume"]').text();
        			getEditor(index,"resume").combotree("setText",lastResume);
        		}
        	}
        	$(this).find("div").eq(1).addClass('tree-node-hover');
        	if(data[0].id!=""){
        		var node=$(this).tree("find",data[0].id);
        		$(this).tree('update',{
			    	target: node.target,
				    text: '请选择',
				    id:"",
			    });
        	}
        },onBeforeSelect:function(node){
        	if(!node.id){
        		return false;
        	}
        }}); */
		  var myinput = $(gridRes.getInput());
		  myinput.css('width',myinput.width()-20);
		  //myinput.css('margin-right',42);
		  myinput.after('<a href="javascript:;" onclick="addRs('+index+');" class="btn-save" style="float:left;"></a>');;
		  myinput.next().css({'float':'right',"margin":"3px 22px"});
		  var creditEditor = getEditor(index,"creditAmount");
		  creditEditor.textbox({height:"80%",required:true,missingMessage:'该项不能为空！',validType:'amount',onChange:function(newValue, oldValue){
			  if(newValue&&newValue!=0){
          		$grid.find("tr#"+edIndex).find('td[aria-describedby="detailList_debitAmount"] input.editable').textbox("setValue",'0');
          		$grid.find("tr#"+edIndex).find('td[aria-describedby="detailList_debitAmount"] input.editable').textbox("disable");
        	}else{
        		$grid.find("tr#"+edIndex).find('td[aria-describedby="detailList_debitAmount"] input.editable').textbox("enable");
        	}
        }});
		  var debitEditor = getEditor(index,"debitAmount");
		  debitEditor.textbox({height:"80%",required:true,missingMessage:'该项不能为空！',validType:'amount',onChange:function(newValue, oldValue){
          	if(newValue&&newValue!=0){
          		$grid.find("tr#"+edIndex).find('td[aria-describedby="detailList_creditAmount"] input.editable').textbox("setValue",'0');
          		$grid.find("tr#"+edIndex).find('td[aria-describedby="detailList_creditAmount"] input.editable').textbox("disable");
        	}else{
        		$grid.find("tr#"+edIndex).find('td[aria-describedby="detailList_creditAmount"] input.editable').textbox("enable");
        	}
        }});
		  if(rowData.newRow == 1){
			  if(index>=2){
				  var defaultResume=rows[index-2].resume;
				  defaultResume;
				  /* if($("#getLastResume").val()==1){
					  setTimeout(function(){$($('#detailList').datagrid('getEditor',{index:index,field:'resume'}).target).combotree("setText",defaultResume);},101);
					  setTimeout(function(){$($('#detailList').datagrid('getEditor',{index:index,field:'resume'}).target).combotree('panel').find("div").eq(1).removeClass('tree-node-hover');},101);
				  } */
				  var defaultDebit=0;
				  var defaultCredit=0;
				  var credit=0;
				  var debit=0;
				  for(var i=0;i<rows.length;i++){
					  debit=parseFloat((rows[i].debitAmount).replace(",",""))+parseFloat(debit);
					  credit=parseFloat((rows[i].creditAmount).replace(",",""))+parseFloat(credit);
				  }
				  var balance=debit-credit;
				  if(balance>0){
					  defaultCredit=balance;
				  }else if(balance<0){
					  defaultDebit=-balance;
				  }
				  getEditor(index,'debitAmount').textbox("setValue",defaultDebit.toFixed(2));
				  getEditor(index,'creditAmount').textbox("setValue",defaultCredit.toFixed(2));
				  getEditor(index,'debitAmount').textbox("setText",defaultDebit.toFixed(2).toString());
				  getEditor(index,'creditAmount').textbox("setText",defaultCredit.toFixed(2).toString());
			  }
		  }else{
			  /* var reg =/\,/ig;
			  var darr=debitStr.replace(reg,"");//从千位数开始格式会加上逗号为分隔符，在这里将分隔符去掉，再进行赋值
			  debitEditor.textbox("setValue",darr);
			  var carr=creditStr.replace(reg,"");
			  creditEditor.textbox("setValue",carr); */
			  disableInputer({quantitySign:getValue("quantitySign"),currencySign:getValue("currencySign"),supplierSign:getValue("supplierSign"),customerSign:getValue("customerSign"),departmentSign:getValue("departmentSign"),memberSign:getValue("memberSign"),warehouseSign:getValue("warehouseSign"),projectSign:getValue("projectSign"),goodsSign:getValue("goodsSign")},'enable');
			  setInputer(rowData);
			  editKey = 0;
		  }
		  voucherEnter(index);
		}
	  //列表键盘控制
	  function voucherEnter(index){
		  var rows=$('#detailList').jqGrid('getRowData');
		  treef = $('#gridBox tr#'+index+' td[aria-describedby="detailList_resume"] .dhxcombo_input');
		  boxf = $('#gridBox tr#'+index+' td[aria-describedby="detailList_subjectName"] .dhxcombo_input');
		  tbox1 = getEditor(index,"debitAmount").textbox("textbox");
		  tbox2 = getEditor(index,"creditAmount").textbox("textbox");
		  tbox1.focus(function(){
			  tbox1.select(); 
		  });
		  tbox1.keyup(function(){
				if(tbox1.val()&&tbox1.val()!=0){
					tbox2.val(0);
					tbox2.next().val(0);
					tbox2.parent().prev().textbox('disable');
					tbox2.validatebox("validate");
				}else{
					tbox2.val("");
					tbox2.next().val("");
					tbox2.parent().prev().textbox('enable');
					tbox2.validatebox("validate");
				}
			});
		  tbox2.focus(function(){
			  tbox2.select(); 
		  });
		  tbox2.keyup(function(){
				if(tbox2.val()&&tbox2.val()!=0){
					tbox1.val(0);
					tbox1.next().val(0);
					tbox1.parent().prev().textbox('disable');
					tbox1.validatebox("validate");
				}else{
					tbox1.val("");
					tbox1.next().val("");
					tbox1.parent().prev().textbox('enable');
					tbox1.validatebox("validate");
				}
			});
	 	  treef.focus();
		  treef.bind('keydown',treefkd = function(e){
			  switch (e.keyCode) {
				case 13: //回车
					boxf.focus();
			    break;
				case 39:
					e.preventDefault();
					boxf.focus();
				break;
				}
			  if(e.ctrlKey && e.keyCode == 90){
				  e.preventDefault();
					addRs(index);
			  }
		  });
		  boxf.bind('keydown',boxfkd = function(e){
			  if(e.keyCode == 13){
				    if(!boxf.validatebox('isValid')) return false;
				    var sign = true;
				    $('#signBox p').each(function(){sign = sign && ($(this).css('display')=='inline-block'?false:true);});
		      		if(sign){
		      			tbox1.attr('disabled')=='disabled'?tbox2.focus():tbox1.focus();;
		      		}else{
		      			//$('#signBox p').each(function(){return ($(this).css('display')=='inline-block'?($(this).find('.validatebox-text').click(),false):true);});
		      			enterController('signBox');
		      			/* var signIndex = 0 , myindex = '';
		      			var max = $('#signBox p').length;
		      			$('#signBox p').each(function(){myindex = max - ($(this).css('display')=='inline-block'?signIndex = 0:++signIndex) - 1;});
		      			$('#signBox p').eq(myindex).find('input[type=text]').bind('keydown',function(e){
		      				if(e.keyCode == 13){
		      					tbox1.attr('disabled')=='disabled'?tbox2.focus():tbox1.focus();;
		      				}
		      			}); */
		      		}
			  }else if(e.keyCode == 37){
				  e.preventDefault();
				  //boxf.parent().prev().combobox('hidePanel');
				  //boxf.closest('[field=subjectName]').siblings('[field=resume]').find('.textbox-text').focus();
			  	  treef.focus();
			  }else if(e.keyCode == 39){
				  e.preventDefault();
				  //boxf.parent().prev().combobox('hidePanel');
				  tbox1.attr('disabled')=='disabled'?tbox2.focus():tbox1.focus();;
			  }
		  });
		  tbox1.bind('keydown',tbox1kd = function(e){
			  if(e.keyCode == 39){
				  		e.preventDefault();
				  		var n1 = 0;
				    	tbox2.attr('disabled')=='disabled'?($.fool.confirm({title:'提示',msg:'确认转换到贷方金额？',fn:function(data){
					    	if(data){
					    		tbox2.parent().prev().textbox('enable');
					    		tbox2.parent().prev().textbox('setText',tbox1.val());
					    		tbox1.val('0');
					    		tbox1.next().val('0');
					    		tbox1.parent().prev().textbox('disable');
					    		tbox2.focus().select();
					    	}else{
					    		return false;
					    	}
					    }})):tbox2.val()==0&&tbox1.val()==0?tbox2.focus():(tbox2.focus(),tbox1.focus());
			  }else if(e.keyCode == 37){
				  e.preventDefault();
				  boxf.focus();
			  }else if(e.keyCode == 13){
					$('#btn-'+(index-1)+'-save').click();
					setTimeout(function(){
						if(editComplete()&&rows.length == index){
							$('#addDetail').click();
						}
					}, 101);
			  }
		  });
		  tbox2.bind('keydown',tbox2kd = function(e){
			  if(e.keyCode == 37){
				  e.preventDefault();
				  tbox1.attr('disabled')=='disabled'?($.fool.confirm({title:'提示',msg:'确认转换到借方金额？',fn:function(data){
				    	if(data){
				    		tbox1.parent().prev().textbox('enable');
				    		tbox1.parent().prev().textbox('setText',tbox2.val());
				    		tbox2.val('0');
				    		tbox2.next().val('0');
				    		tbox2.parent().prev().textbox('disable');
				    		tbox1.focus().select();
				    	}else{
				    		return false;
				    	}
				    }})):tbox2.val()==0&&tbox1.val()==0?tbox1.focus():(tbox1.focus(),tbox2.focus());
			  }else if(e.keyCode == 13){
					$('#btn-'+(index-1)+'-save').click();
					setTimeout(function(){
						if(editComplete()&&rows.length == index){
							$('#addDetail').click();
						}
					}, 101);
					
			  }
		  });
	  }
	  
	  $.extend($.fn.validatebox.defaults.rules, {    
		    subjValid: {    
		        validator: function(value,param){
		            var mytips = $(this).siblings(".subjn1").attr('mytips');
		        	return mytips==1?false:true;    
		        },    
		        message: '请按右箭头选择下级科目'   
		    },
		    mysubjValid: {    
		        validator: function(value,param){
		        	return getEditor(param[0],"subjectId").val()==""?false:true;    
		        },    
		        message: '请从下拉框中选择科目'   
		    },
		    valueValid: {    
		        validator: function(value,param){
		        	var row = $(param[0])[0].comboObj.getSelectedText();
		            return !row?false:true;    
		        },    
		        message: '选项没有选中，请重新选择'   
		    },
		    noValueValid: {    
		        validator: function(value,param){
		        	var id = getEditor(edIndex,param[0]).val();
		        	var name = getEditor(edIndex,param[1]).val();
		            return id != "" && name == value;    
		        },    
		        message: '该项没有选中，请重新选择'   
		    },
	  });
	  //新增明细按钮点击事件
	  function insert(target){
		  var rows=$('#detailList').jqGrid('getDataIDs');
		  if(!editComplete()&&rows.length>0){
			  $.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！'});
			  return false;
		  }
		  hideBtn(1);
		  var index=rows.length;
		  $('#detailList').jqGrid('addRowData',index+1,{
				newRow:1
		  });
		  $('#addBox').scrollTop($('#btn-'+index+'-edit').offset().top - $('#form').offset().top);
		  $('#btn-'+index+'-edit').click();
	  }
	//保存按钮点击事件
	$("#save").click(function(){
		var details=$("#detailList").jqGrid('getRowData');
		var footer=$("#detailList").jqGrid('footerData','get');
		if(details.length<2){
			$.fool.alert({msg:'必须添加至少两条明细。',fn:function(){
				$(document).unbind('keydown',mykd);
				$(document).bind('keydown',mykd);
			}});
			return;
		}
		if(footer.debitAmount!=footer.creditAmount){
	    	$.fool.alert({msg:'借方与贷方金额不平衡。',fn:function(){
	    		$(document).unbind('keydown',mykd);
				$(document).bind('keydown',mykd);
			}});
			return;
		}
		if(!editComplete(1)){ 
			$.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！',fn:function(){
				$(document).unbind('keydown',mykd);
				$(document).bind('keydown',mykd);
			}});
			return;
		};
		details=$("#detailList").jqGrid('getRowData');
		var vo=$('#form').serialize(); 
		//vo = $.extend(vo,{'details':JSON.stringify(details)});
		$('#form').form('enableValidation');
		if($('#form').form('validate')){
			$('#save').attr("disabled","disabled");
			$.ajax({
				type:'post',
				url:'${ctx}/voucher/save',
				data:vo+"&details="+JSON.stringify(details),
				success:function(data){
					if(data.returnCode =='0'){
						$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
							$('#save').removeAttr("disabled");
							$('#addBox').window('close');
			    			$('#voucherList').trigger("reloadGrid"); 
						}});
					}else if(data.returnCode =='1'){
			    		$.fool.alert({msg:data.message,fn:function(){
			    			$(document).unbind('keydown',mykd);
			    			$(document).bind('keydown',mykd);
			    		}});
			    		$('#save').removeAttr("disabled");
			    	}else{
			    		$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。",fn:function(){
			    			$(document).unbind('keydown',mykd);
			    			$(document).bind('keydown',mykd);
			    		}});
			    		$('#save').removeAttr("disabled");
		    		}
				}
			});
		}
	});

	//上一页凭证
	$("#prevVoucher").click(function(){
		if('${obj.recordStatus}'==null||'${obj.recordStatus}'==''){//新增状态
			var details=$("#detailList").jqGrid('getRowData');
			var footer=$("#detailList").jqGrid('footerData','get');
			if(details.length<2){
				$.fool.alert({msg:'必须添加至少两条明细。',fn:function(){
					$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			}
			if(!editComplete(1)){
				$.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！',fn:function(){
					$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			};
		    if(footer.debitAmount!=footer.creditAmount){
		    	$.fool.alert({msg:'借方与贷方金额不平衡。',fn:function(){
		    		$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			}
		    details=$("#detailList").jqGrid('getRowData');
			var vo=$('#form').serialize();
			$('#form').form('enableValidation');
			if($('#form').form('validate')){
				$('#save').attr("disabled","disabled");
					$.post('${ctx}/voucher/save',vo+"&details="+JSON.stringify(details),function(data){
						if(data.returnCode =='0'){
							$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
								$(document).unbind('keydown',mykd);
								$('#voucherList').trigger("reloadGrid"); 
								$('#addBox').window('setTitle',"新增凭证");
								$('#addBox').window('refresh',"${ctx}/voucher/add");
							}});
						}else if(data.returnCode =='1'){
				    		$.fool.alert({msg:data.message,fn:function(){
				    			$(document).unbind('keydown',mykd);
				    			$(document).bind('keydown',mykd);
				    		}});
				    		$('#save').removeAttr("disabled");
				    	}else{
				    		$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
				    			$(document).unbind('keydown',mykd);
				    			$(document).bind('keydown',mykd);
				    		}});
				    		$('#save').removeAttr("disabled");
			    		}
					});
			}
		}else if('${obj.recordStatus}'==0){//草稿状态
			var details=$("#detailList").jqGrid('getRowData');
			var footer=$("#detailList").jqGrid('footerData','get');
			if(details.length<2){
				$.fool.alert({msg:'必须添加至少两条明细。',fn:function(){
					$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			}
			if(!editComplete(1)){
				$.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！',fn:function(){
					$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			};
		    if(footer.debitAmount!=footer.creditAmount){
		    	$.fool.alert({msg:'借方与贷方金额不平衡。',fn:function(){
		    		$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			}
		    details=$("#detailList").jqGrid('getRowData');
			var vo=$('#form').serialize();
			$('#form').form('enableValidation');
			if($('#form').form('validate')){
				$('#save').attr("disabled","disabled");
				if(vo!=initialVo||JSON.stringify(details)!=JSON.stringify(initialDetail)){
					$.post('${ctx}/voucher/save',vo+"&details="+JSON.stringify(details),function(data){
						if(data.returnCode =='0'){
							$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
								$(document).unbind('keydown',mykd);
								var index=$('#voucherList tr.jqgrow td[title=${obj.fid}]').parent().attr("id")-1;
								var rows=$('#voucherList').jqGrid('getRowData');
								var fid="";
								var status="";
								if(rows[index+1]){
									fid=rows[index+1].fid;
									status=rows[index+1].recordStatus;
								}
								if(fid==""){
									var next=getPrev();
									if(next){
										if(next[0].recordStatus==1||next[0].recordStatus==2||next[0].recordStatus==3){
											$('#addBox').window('setTitle',"查看凭证");
										}else{
											$('#addBox').window('setTitle',"修改凭证");
										}
										$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+next[0].fid);
									}else{
										$('#addBox').window('setTitle',"新增凭证");
										$('#addBox').window('refresh',"${ctx}/voucher/add");
									}
								}else{
									if(status==1||status==2||status==3){
										$('#addBox').window('setTitle',"查看凭证");
									}else{
										$('#addBox').window('setTitle',"修改凭证");
									}
									$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
								}
				    			$('#save').removeAttr("disabled");
				    			$('#voucherList').trigger("reloadGrid"); 
							}});
						}else if(data.returnCode =='1'){
				    		$.fool.alert({msg:data.message,fn:function(){
				    			$(document).unbind('keydown',mykd);
				    			$(document).bind('keydown',mykd);
				    		}});
				    		$('#save').removeAttr("disabled");
				    	}else{
				    		$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
				    			$(document).unbind('keydown',mykd);
				    			$(document).bind('keydown',mykd);
				    		}});
				    		$('#save').removeAttr("disabled");
			    		}
					});
				}else{
					$(document).unbind('keydown',mykd);
					var index=$('#voucherList tr.jqgrow td[title=${obj.fid}]').parent().attr("id")-1;
					var rows=$('#voucherList').jqGrid('getRowData');
					var fid="";
					var status="";
					if(rows[index+1]){
						fid=rows[index+1].fid;
						status=rows[index+1].recordStatus;
					}
					if(fid==""){
						var next=getPrev();
						if(next){
							if(next[0].recordStatus==1||next[0].recordStatus==2||next[0].recordStatus==3){
								$('#addBox').window('setTitle',"查看凭证");
							}else{
								$('#addBox').window('setTitle',"修改凭证");
							}
							$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+next[0].fid);
						}else{
							$('#addBox').window('setTitle',"新增凭证");
							$('#addBox').window('refresh',"${ctx}/voucher/add");
						}
					}else{
						if(status==1||status==2||status==3){
							$('#addBox').window('setTitle',"查看凭证");
						}else{
							$('#addBox').window('setTitle',"修改凭证");
						}
						$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
					}
	    			$('#save').removeAttr("disabled");
	    			$('#voucherList').trigger("reloadGrid"); 
				}
			};
		}else if('${obj.recordStatus}'==1||'${obj.recordStatus}'==2||'${obj.recordStatus}'==3){
			$(document).unbind('keydown',mykd);
			var index=$('#voucherList tr.jqgrow td[title=${obj.fid}]').parent().attr("id")-1;
			var rows=$('#voucherList').jqGrid('getRowData');
			var fid="";
			var status="";
			if(rows[index+1]){
				fid=rows[index+1].fid;
				status=rows[index+1].recordStatus;
			}
			if(fid==""){
				var next=getPrev();
				if(next){
					if(next[0].recordStatus==1||next[0].recordStatus==2||next[0].recordStatus==3){
						$('#addBox').window('setTitle',"查看凭证");
					}else{
						$('#addBox').window('setTitle',"修改凭证");
					}
					$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+next[0].fid);
				}else{
					$('#addBox').window('setTitle',"新增凭证");
					$('#addBox').window('refresh',"${ctx}/voucher/add");
				}
			}else{
				if(status==1||status==2||status==3){
					$('#addBox').window('setTitle',"查看凭证");
				}else{
					$('#addBox').window('setTitle',"修改凭证");
				}
				$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
			}
		}
	});
	
	//下一页凭证
	$("#nextVoucher").click(function(){
		if('${obj.recordStatus}'==null||'${obj.recordStatus}'==''){
			var details=$("#detailList").jqGrid('getRowData');
			var footer=$("#detailList").jqGrid('footerData','get');
			if(details.length<2){
				$.fool.alert({msg:'必须添加至少两条明细。',fn:function(){
					$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			}
			if(!editComplete(1)){
				$.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！',fn:function(){
					$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			};
		    if(footer.debitAmount!=footer.creditAmount){
		    	$.fool.alert({msg:'借方与贷方金额不平衡。',fn:function(){
		    		$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			}
		    details=$("#detailList").jqGrid('getRowData');
			var vo=$('#form').serialize();
			$('#form').form('enableValidation');
			if($('#form').form('validate')){
				$('#save').attr("disabled","disabled");
					$.post('${ctx}/voucher/save',vo+"&details="+JSON.stringify(details),function(data){
						if(data.returnCode =='0'){
							$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
								$(document).unbind('keydown',mykd);
								$('#voucherList').trigger("reloadGrid"); 
								$('#addBox').window('setTitle',"新增凭证");
								$('#addBox').window('refresh',"${ctx}/voucher/add");
							}});
						}else if(data.returnCode =='1'){
				    		$.fool.alert({msg:data.message,fn:function(){
				    			$(document).unbind('keydown',mykd);
				    			$(document).bind('keydown',mykd);
				    		}});
				    		$('#save').removeAttr("disabled");
				    	}else{
				    		$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
				    			$(document).unbind('keydown',mykd);
				    			$(document).bind('keydown',mykd);
				    		}});
				    		$('#save').removeAttr("disabled");
			    		}
					});
			}
		}else if('${obj.recordStatus}'==0){
			var details=$("#detailList").jqGrid('getRowData');
			var footer=$("#detailList").jqGrid('footerData','get');
			if(details.length<2){
				$.fool.alert({msg:'必须添加至少两条明细。',fn:function(){
					$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			}
			if(!editComplete(1)){
				$.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！',fn:function(){
					$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			};
		    if(footer.debitAmount!=footer.creditAmount){
		    	$.fool.alert({msg:'借方与贷方金额不平衡。',fn:function(){
		    		$(document).unbind('keydown',mykd);
					$(document).bind('keydown',mykd);
				}});
				return;
			}
		    details=$("#detailList").jqGrid('getRowData');
			var vo=$('#form').serialize();
			$('#form').form('enableValidation');
			if($('#form').form('validate')){
				$('#save').attr("disabled","disabled");
				if(vo!=initialVo||JSON.stringify(details)!=JSON.stringify(initialDetail)){
					$.post('${ctx}/voucher/save',vo+"&details="+JSON.stringify(details),function(data){
						if(data.returnCode =='0'){
							$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
								$(document).unbind('keydown',mykd);
								var index=$('#voucherList tr.jqgrow td[title=${obj.fid}]').parent().attr("id")-1;
								var rows=$('#voucherList').jqGrid('getRowData');
								var fid="";
								var status="";
								if(rows[index-1]){
									fid=rows[index-1].fid;
									status=rows[index-1].recordStatus;
								}
								if(fid==""){
									var next=getNext();
									if(next){
										if(next[next.length-1].recordStatus==1||next[next.length-1].recordStatus==2||next[next.length-1].recordStatus==3){
											$('#addBox').window('setTitle',"查看凭证");
										}else{
											$('#addBox').window('setTitle',"修改凭证");
										}
										$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+next[next.length-1].fid);
									}else{
										$('#addBox').window('setTitle',"新增凭证");
										$('#addBox').window('refresh',"${ctx}/voucher/add");
									}
								}else{
									if(status==1||status==2||status==3){
										$('#addBox').window('setTitle',"查看凭证");
									}else{
										$('#addBox').window('setTitle',"修改凭证");
									}
									$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
								}
				    			$('#save').removeAttr("disabled");
				    			$('#voucherList').trigger("reloadGrid"); 
							}});
						}else if(data.returnCode =='1'){
				    		$.fool.alert({msg:data.message,fn:function(){
				    			$(document).unbind('keydown',mykd);
				    			$(document).bind('keydown',mykd);
				    		}});
				    		$('#save').removeAttr("disabled");
				    	}else{
				    		$.fool.alert({msg:"系统正忙，请稍后再试。",fn:function(){
				    			$(document).unbind('keydown',mykd);
				    			$(document).bind('keydown',mykd);
				    		}});
				    		$('#save').removeAttr("disabled");
			    		}
					});
				}else{
					$(document).unbind('keydown',mykd);
					var index=$('#voucherList tr.jqgrow td[title=${obj.fid}]').parent().attr("id")-1;
					var rows=$('#voucherList').jqGrid('getRowData');
					var fid="";
					var status="";
					if(rows[index-1]){
						fid=rows[index-1].fid;
						status=rows[index-1].recordStatus;
					}
					if(fid==""){
						var next=getNext();
						if(next){
							if(next[next.length-1].recordStatus==1||next[next.length-1].recordStatus==2||next[next.length-1].recordStatus==3){
								$('#addBox').window('setTitle',"查看凭证");
							}else{
								$('#addBox').window('setTitle',"修改凭证");
							}
							$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+next[next.length-1].fid);
						}else{
							$('#addBox').window('setTitle',"新增凭证");
							$('#addBox').window('refresh',"${ctx}/voucher/add");
						}
					}else{
						if(status==1||status==2||status==3){
							$('#addBox').window('setTitle',"查看凭证");
						}else{
							$('#addBox').window('setTitle',"修改凭证");
						}
						$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
					}
	    			$('#save').removeAttr("disabled");
	    			$('#voucherList').trigger("reloadGrid"); 
				}
			};
		}else if('${obj.recordStatus}'==1||'${obj.recordStatus}'==2||'${obj.recordStatus}'==3){
			$(document).unbind('keydown',mykd);
			var index=$('#voucherList tr.jqgrow td[title=${obj.fid}]').parent().attr("id")-1;
			var rows=$('#voucherList').jqGrid('getRowData');
			var fid="";
			var status="";
			if(rows[index-1]){
				fid=rows[index-1].fid;
				status=rows[index-1].recordStatus;
			}
			if(fid==""){
				var next=getNext();
				if(next){
					if(next[next.length-1].recordStatus==1||next[next.length-1].recordStatus==2||next[next.length-1].recordStatus==3){
						$('#addBox').window('setTitle',"查看凭证");
					}else{
						$('#addBox').window('setTitle',"修改凭证");
					}
					$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+next[next.length-1].fid);
				}else{
					$('#addBox').window('setTitle',"新增凭证");
					$('#addBox').window('refresh',"${ctx}/voucher/add");
				}
			}else{
				if(status==1||status==2||status==3){
					$('#addBox').window('setTitle',"查看凭证");
				}else{
					$('#addBox').window('setTitle',"修改凭证");
				}
				$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
			}
		}
	});

	//打印按钮
	$("#print").click(function(){
		printVoucher($("#fid").val(),0);
	});

	//审核按钮
	$("#verify").click(function(){
		verifyById($("#fid").val());
	});

	//反审核按钮
	$("#unVerify").click(function(){
		unVerifyById($("#fid").val());
	});

	//作废按钮
	$("#cancel").click(function(){
		cancelById($("#fid").val());
	});

	//反作废按钮
	$("#unCancel").click(function(){
		unCancelById($("#fid").val());
	});

	//冲销按钮
	$("#writeOff").click(function(){
		$('#addBox').window('setTitle',"新增凭证");
		$('#addBox').window('refresh',"${ctx}/voucher/writeOff?mark=1&fid="+fid);
	});

	//导出按钮
	$("#export").click(function(){});

	//签字按钮
	$("#sign").click(function(){
		signById($("#fid").val());
	});

	//弹出框定义
	function subjectChooser(index){
		  editor = (getEditor(index,"subjectName").next())[0].comboObj;
		  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubjectDBC&singleSelect=true',onLoad:function(){
			  $(editor.getInput()).blur();
		  }});
	}

	function selectSubject(rowData){
		  if(rowData[0].children.length>0){
			  $.fool.alert({msg:"凭证填制必须是科目明细。"});
			  return false;
		  }
		  $(editor.getInput()).parent().find('.subjn1 a').click();
		  editor.setComboText(rowData[0].text);
		  editor.setComboValue(rowData[0].fid);
		  if(getValue("subjectId")==rowData[0].fid){
			  chooserWindow.window('close');
			  editor.setComboValue(rowData[0].fid);
			  $(editor.getInput()).focus();
			  //setTimeout(function(){
				  editor.closeAll();
			  //}, 100);
			  return false;
		  }else{
			  valueGrid('subjectId',rowData[0].fid);
			  valueGrid('subjectCode',rowData[0].code);
			  valueGrid('subjectName_',rowData[0].name);
			  if(rowData[0].parentCode){
				  valueGrid('parentSubjectCode',rowData[0].parentCode);
			  }
			  if(rowData[0].parentName){
				  valueGrid('parentSubjectName',rowData[0].parentName);
			  }
			  initialSign(rowData[0]);
			  signInputer(rowData[0]);
	      	  clearInputer();
	      	  setInputer(rowData[0]);
		  }
		  disableInputer(rowData[0],"enable");
		  chooserWindow.window('close');
		  editor.setComboValue(rowData[0].fid);
		  $(editor.getInput()).focus();
		  //setTimeout(function(){
			  editor.closeAll();
		  //}, 100);
	}
	function selectSubjectDBC(rowData) {
		  if(rowData.children.length>0){
			  $.fool.alert({msg:"凭证填制必须是科目明细。"});
			  return false;
		  }
		  $(editor.getInput()).parent().find('.subjn1 a').click();
		  editor.setComboText(rowData.text);
		  editor.setComboValue(rowData.fid);
		  if(getValue("subjectId")==rowData.fid){
			  chooserWindow.window('close');
			  editor.setComboValue(rowData.fid);
			  $(editor.getInput()).focus();
			  //setTimeout(function(){
				  editor.closeAll();
			  //}, 100);
			  return false;
		  }else{
			  valueGrid('subjectId',rowData.fid);
			  valueGrid('subjectCode',rowData.code);
			  valueGrid('subjectName_',rowData.name);
			  if(rowData.parentCode){
				  valueGrid('parentSubjectCode',rowData.parentCode);
			  }
			  if(rowData.parentName){
				  valueGrid('parentSubjectName',rowData.parentName);
			  }
			  initialSign(rowData);
			  signInputer(rowData);
	      	  clearInputer();
	      	  setInputer(rowData);
		  }
		  disableInputer(rowData,"enable");
		  chooserWindow.window('close');
		  editor.setComboValue(rowData.fid);
		  $(editor.getInput()).focus();
		  //setTimeout(function(){
			  editor.closeAll();
		  //}, 100);
		  
	}

	//判断是否编辑完成
	function editComplete(mark){
		var _dataPanel = $('#detailList');
		var rows=$('#detailList').jqGrid("getRowData");
		var lastIndex=rows.length-1;
		//确保最后一行是编辑状态并且除摘要外其他值没有填写的情况下才撤销该行
		if(mark==1&&rows&&rows[lastIndex].newRow == "1"&&rows[lastIndex].action.search(/btn-save/)!=-1&&getEditor(lastIndex+1,"subjectId").val()==""&&getEditor(lastIndex+1,"debitAmount").textbox("getValue")==0&&getEditor(lastIndex+1,"creditAmount").textbox("getValue")==0){
			var back=_dataPanel.find(".btn-back");
			$(back[back.length-1]).click();
		}
		var _editing =_dataPanel.find(".editable");
		$("#signBox").form('enableValidation');
		if(_editing.length>0||!$("#signBox").form("validate")){
			return false;
		}else{
			return true;
		}
	}

	//为编辑状态单元格赋值
	function valueGrid(name,value){
		$grid.find("#"+edIndex+"_"+name).val(value);
		/* var option = {};
		option[name] = value;
		$('#detailList').jqGrid('setRowData', edIndex, option); */
	}

	//获取编辑状态单元格值
	function getValue(name){
		var val = $grid.find("#"+edIndex+"_"+name).val();;
		return val;
		//return $grid.find(".jqgrow ui-row-ltr[id='"+edIndex+"']").find('td[aria-describedby="detailList_'+name+'"]').text();
	}

	//初始化列表的核算属性
	function initialSign(data){
		valueGrid("currencySign",data.currencySign);
		valueGrid("supplierSign",data.supplierSign);
		valueGrid("customerSign",data.customerSign);
		valueGrid("departmentSign",data.departmentSign);
		valueGrid("memberSign",data.memberSign);
		valueGrid("warehouseSign",data.warehouseSign);
		valueGrid("projectSign",data.projectSign);
		valueGrid("goodsSign",data.goodsSign);
		valueGrid("quantitySign",data.quantitySign);
		
		valueGrid("unitName","");
		valueGrid("currencyName","");
		valueGrid("supplierName","");
		valueGrid("customerName","");
		valueGrid("departmentName","");
		valueGrid("memberName","");
		valueGrid("warehouseName","");
		valueGrid("projectName","");
		valueGrid("goodsName","");
		valueGrid("unitId","");
		valueGrid("currencyId","");
		valueGrid("supplierId","");
		valueGrid("customerId","");
		valueGrid("departmentId","");
		valueGrid("memberId","");
		valueGrid("warehouseId","");
		valueGrid("projectId","");
		valueGrid("goodsId","");
		valueGrid("quantity","");
		valueGrid("exchangeRate","");
		valueGrid("currencyAmount","");
	}

	//初始化核算输入框
	function signInputer(data,mark){
		if(data.currencySign!=1){
	        $("#currencyName").parent().hide();
	        $("#exchangeRate").parent().hide();
	        $("#currencyAmount").parent().hide();
		}else{
	        $("#currencyName").parent().show();
	        $("#exchangeRate").parent().show();
	        $("#currencyAmount").parent().show();
		}
		  
		if(data.supplierSign!=1){
			$("#supplierName").parent().hide();
		}else{
			$("#supplierName").parent().show();
		}
		  
		if(data.customerSign!=1){
			$("#customerName").parent().hide();
		}else{
			$("#customerName").parent().show();
		}
		  
		if(data.departmentSign!=1){
			$("#departmentName").parent().hide();
		}else{
			$("#departmentName").parent().show();
		}
		  
		if(data.memberSign!=1){
			$("#memberName").parent().hide();
		}else{
			$("#memberName").parent().show();
		}
		  
		if(data.warehouseSign!=1){
			$("#warehouseName").parent().hide();
		}else{
			$("#warehouseName").parent().show();
		}
		  
		if(data.projectSign!=1){
			$("#projectName").parent().hide();
		}else{
			$("#projectName").parent().show();
		}
		 
		if(data.goodsSign!=1){
			$("#goodsName").parent().hide();
		}else{
			$("#goodsName").parent().show();
		}
		  
		if(data.quantitySign!=1){
	        $("#unitName").parent().hide();
	        $("#quantity").parent().hide();
		}else{
			valueGrid("unitId",data.unitId);
			valueGrid("unitName",data.unitName);
	        $("#unitName").parent().show();
	        $("#quantity").parent().show();
		}
		var p=$("#signBox p");
		for(var i=0;i<p.length;i++){
			if($(p[i]).css('display')=="block"){
				$(p[i]).css('display','inline-block');
			}
		}
	}

	//清空核算输入框
	function clearInputer(){
		$("#signBox").form('clear');
			supCombo.setComboText("");
			cusCombo.setComboText("");
			deptCombo.setComboText("");
			memCombo.setComboText("");
			wareCombo.setComboText("");
			proCombo.setComboText("");
			goodsCombo.setComboText("");
	}

	//核算输入框赋值
	function setInputer(data){
		if(data.currencySign==1){
			valueGrid("currencyName",data.currencyName);
			valueGrid("currencyId",data.currencyId);
			valueGrid("currencyAmount",data.currencyAmount);
			valueGrid("exchangeRate",data.exchangeRate);
			$("#currencyName").textbox('setValue',data.currencyName);
			$("#exchangeRate").val(data.exchangeRate);
			$("#currencyAmount").val(data.currencyAmount);
		}
		  
		if(data.supplierSign==1){
			supCombo.setComboText(data.supplierName?data.supplierName:"");
		}
		  
		if(data.customerSign==1){
			cusCombo.setComboText(data.customerName?data.customerName:"");
		}
		  
		if(data.departmentSign==1){
			deptCombo.setComboValue(data.departmentId);
			//$("#departmentName").combotree('setValue',data.departmentId);
		}
		  
		if(data.memberSign==1){
			memCombo.setComboValue(data.memberId);
			/* memCombo.setComboText(data.memberName?data.memberName:""); */
		}
		  
		if(data.warehouseSign==1){
			wareCombo.setComboValue(data.warehouseId);
			/* wareCombo.setComboText(data.warehouseName?data.warehouseName:""); */
			//$("#warehouseName").combotree('setValue',data.warehouseId);
		}
		  
		if(data.projectSign==1){
			proCombo.setComboValue(data.projectId);
			/* proCombo.setComboText(data.projectName?data.projectName:""); */
			//$("#projectName").combotree('setValue',data.projectId);
		}
		 
		if(data.goodsSign==1){
			goodsCombo.setComboText(data.goodsName?data.goodsName:"");
		}
		
		if(data.quantitySign==1){
			$("#unitName").textbox('setValue',data.unitName);
	        $("#quantity").val(data.quantity);
	        valueGrid("quantity",data.quantity);
		}
	}

	//显示、隐藏核算输入框
	function disableInputer(data,action){
		if(action=="disable"){
			$("#currencyName").textbox('disable');
	        $("#exchangeRate").attr('disabled','disabled');;
	        $("#currencyAmount").attr('disabled','disabled');;
	        supCombo.disable();
	        //$("#supplierName").combogrid('disable');
	        cusCombo.disable();
	        //$("#customerName").combogrid('disable');
	        //$("#departmentName").combotree('disable');
	        deptCombo.disable();
	        //$("#memberName").combogrid('disable');
	        memCombo.disable();
	        //$("#warehouseName").combotree('disable');
	        wareCombo.disable();
	        //$("#projectName").combotree('disable');
	        proCombo.disable();
	        //$("#goodsName").combogrid('disable');
	        goodsCombo.disable();
	        $("#unitName").textbox('disable');
	        $("#quantity").attr('disabled','disabled');
	        $("#currencyName").textbox('disableValidation');
	        $("#exchangeRate").validatebox('disableValidation');
	        $("#currencyAmount").validatebox('disableValidation');
	        //$("#supplierName").combogrid('disableValidation');
	        $(supCombo.getInput()).validatebox('disableValidation');
	        $(cusCombo.getInput()).validatebox('disableValidation');
	        $(deptCombo.getInput()).validatebox('disableValidation');
	        $(memCombo.getInput()).validatebox('disableValidation');
	        $(wareCombo.getInput()).validatebox('disableValidation');
	        $(proCombo.getInput()).validatebox('disableValidation');
	        $(goodsCombo.getInput()).validatebox('disableValidation');
	        //$("#customerName").combogrid('disableValidation');
	        //$("#departmentName").combotree('disableValidation');
	        //$("#memberName").combogrid('disableValidation');
	        //$("#warehouseName").combotree('disableValidation');
	        //$("#projectName").combotree('disableValidation');
	        //$("#goodsName").combogrid('disableValidation');
	        $("#unitName").textbox('disableValidation');
	        $("#quantity").validatebox('disableValidation');
	        $("#signBox").form("disableValidation");
		}else if(action=="enable"){
			if(data.currencySign==1){
				$("#currencyName").textbox('enable');
				$("#exchangeRate").removeAttr('disabled');
			    $("#currencyAmount").removeAttr('disabled');
			}else{
				$("#currencyName").textbox('disable');
				$("#exchangeRate").attr('disabled','disabled');
		        $("#currencyAmount").attr('disabled','disabled');
		        $("#currencyName").textbox('disableValidation');
				$("#exchangeRate").validatebox('disableValidation');
			    $("#currencyAmount").validatebox('disableValidation');
			}
			if(data.supplierSign==1){
				//$("#supplierName").combogrid('enable');
				supCombo.enable();
			}else{
				supCombo.disable();
				$(supCombo.getInput()).validatebox('disableValidation');
				//$("#supplierName").combogrid('disable');
				//$("#supplierName").combogrid('disableValidation');
			}
			if(data.customerSign==1){
				cusCombo.enable();
				//$("#customerName").combogrid('enable');
			}else{
				cusCombo.disable();
				$(cusCombo.getInput()).validatebox('disableValidation');
				//$("#customerName").combogrid('disable');
				//$("#customerName").combogrid('disableValidation');
			}
			if(data.departmentSign==1){
				//$("#departmentName").combotree('enable');
				deptCombo.enable();
			}else{
				deptCombo.disable();
				$(deptCombo.getInput()).validatebox('disableValidation');
				//$("#departmentName").combotree('disable');
				//$("#departmentName").combotree('disableValidation');
			}
			if(data.memberSign==1){
				memCombo.enable();
				//$("#memberName").combogrid('enable');
			}else{
				memCombo.disable();
				$(memCombo.getInput()).validatebox('disableValidation');
				//$("#memberName").combogrid('disable');
				//$("#memberName").combogrid('disableValidation');
			}
		    if(data.warehouseSign==1){
		    	//$("#warehouseName").combotree('enable');
		    	wareCombo.enable();
			}else{
				wareCombo.disable();
				$(wareCombo.getInput()).validatebox('disableValidation');
				//$("#warehouseName").combotree('disable');
				//$("#warehouseName").combotree('disableValidation');
			}
			if(data.projectSign==1){
				//$("#projectName").combotree('enable');
				proCombo.enable();
			}else{
				proCombo.disable();
				$(proCombo.getInput()).validatebox('disableValidation');
				//$("#projectName").combotree('disable');
				//$("#projectName").combotree('disableValidation');
			}
			if(data.goodsSign==1){
				goodsCombo.enable();
				//$("#goodsName").combogrid('enable');
			}else{
				goodsCombo.disable();
				$(goodsCombo.getInput()).validatebox('disableValidation');
				//$("#goodsName").combogrid('disable');
				//$("#goodsName").combogrid('disableValidation');
			}
			if(data.quantitySign==1){
				$("#unitName").textbox('enable');
				$("#quantity").removeAttr('disabled');
				
			}else{
				$("#unitName").textbox('disable');
		        $("#quantity").attr('disabled','disabled');
		        $("#unitName").textbox('disableValidation');
		        $("#quantity").validatebox('disableValidation');
			}
		}
	}
	disableInputer("","disable");
	//加载页脚
	function loadFooter(){
		var debit=0;
		var credit=0;
		var rows=$('#detailList').jqGrid("getRowData");
		/* var edrow = $('#detailList').jqGrid("getRowData",rows.length+1);
		edIndex != ""?rows[rows.length] = edrow:null; */
		
		for(var i=0;i<rows.length;i++){
			if(!rows[i].debitAmount){
				debit=parseFloat(debit)+0;
			}else{
				debit=parseFloat(debit)+parseFloat(rows[i].debitAmount.replace(",",""));
			}
			if(!rows[i].creditAmount){
				credit=parseFloat(credit)+0;
			}else{
				credit=parseFloat(credit)+parseFloat(rows[i].creditAmount.replace(",",""));
			}
		}
		$('#totalAmount').val(debit.toFixed(2));
		$("#detailList").footerData('set',{resume:'<a class="btn-add" id="addDetail" title="插入" href="javascript:;" onclick="insert(this)"></a>', subjectName:'合计', debitAmount:debit.toFixed(2),creditAmount:credit.toFixed(2),signDetail:"0",action:"0"});
		//$('#detailList').datagrid('reloadFooter',[{resume:'<a class="btn-add" id="addDetail" title="插入" href="javascript:;" onclick="insert(this)"></a>', subjectName:'合计', debitAmount:debit.toFixed(2),creditAmount:credit.toFixed(2),signDetail:"0",action:"0"}]);
	}

	function hideBtn(hide,target){
		if(hide=="1"){
			$grid.find("tr.ui-row-ltr").find("td[aria-describedby=detailList_action]").find("a").hide();
		}else{
			$grid.find("tr.ui-row-ltr").find("td[aria-describedby=detailList_action]").find("a").show();
		}
	}

	$.extend($.fn.validatebox.defaults.rules, {
		intOrFloat:{
	        validator: function (value) {
	            var b = /^\d+(\.\d+)?$/i.test(value);
	            if(!b)return false;
	            else 
	           	 return value>0&&parseFloat(value)<=9999999999.9999999999&&parseFloat(value)>=-9999999999.9999999999;
	        },
	        message: '请输入数字，并确保大于0'
	    },
	    amount:{
	    	validator:function(value,param){
	    		return ((/^(([+]?[0-9]\d*)|\d)(\.\d*)?$/).test(value)||(/^(([-]?[0-9]\d*)|\d)(\.\d*)?$/).test(value))&&parseFloat(value)<=9999999999.99&&parseFloat(value)>=-9999999999.99;
	    	},
	    	message:'正数最多能输入10位数，请输入正确的金额'
	    },
	    exchangeRate:{
	    	validator:function(value,param){
	    		return ((/^(([+]?[0-9]\d*)|\d)(\.\d{1,4})?$/).test(value)||(/^(([-]?[0-9]\d*)|\d)(\.\d{1,4})?$/).test(value))&&parseFloat(value)<=9999999999.99&&parseFloat(value)>=-9999999999.99;
	    	},
	    	message:'最多能输入10位数，小数精确到4位，请输入正确的汇率'
	    },
	    voucherDate:{
	    	validator:function(value,param){
	    		var result="";
	    		$.ajax({
	    			url:"${ctx}/voucher/getDefaultMessage?num="+Math.random(),
	    			async:false,
	    			data:{voucherDate:value},
	    			success:function(data){
	    				result=data.voucherDate;
	    			}
	    		});
	    		if(result){
	    			return true;
				}else{
					return false;
				}
	    	},
	    	message:'根据当前凭证日期找不到未结账的会计期间' 
	    },
	    positiveInt:{
	    	  validator:function(value){
	    		  return /^([+]?[0-9])+\d*$/i.test(value);
	    	  },
	    	  message: '请输入大于等于0的整数'
	      },
	});
	enterController('list2');
	t.blur();
	t.focus(function(){
		  $(this).parent().prev().datebox('showPanel');
	});
	$(vouCombo.getInput()).blur();
	//顺序号输入框回车焦点跳转列表
	$('#number').bind('keydown',function(e){
		  if(e.keyCode == 13){
			  if($('#detailList').jqGrid('getDataIDs').length < 1){//列表没有内容则新增一行
				  $('#addDetail').click();
			  }
		  }
	});
	var mykd='';
	//屏蔽默认快捷键
	$(document).keydown(function(e){
		if(e.ctrlKey && e.keyCode == 83){//ctrl+s屏蔽默认
			if (e.preventDefault) {  // firefox
	            e.preventDefault();
	        } else { // other
	            e.returnValue = false;
	        }
		}else if(e.ctrlKey && e.keyCode == 69){
			if (e.preventDefault) {  // firefox
	            e.preventDefault();
	        } else { // other
	            e.returnValue = false;
	        }
		}else if(e.ctrlKey && e.keyCode == 8){
			if (e.preventDefault) {  // firefox
	            e.preventDefault();
	        } else { // other
	            e.returnValue = false;
	        }
		}
	});
	//编辑页面保存/下一页按钮,列表撤销按钮快捷键
	$(document).keydown(mykd=function(e){
		if($('#addBox').closest('.window').css('display') != 'none'){
			if(e.ctrlKey && e.keyCode == 83){//ctrl+s
				$('#save').click();
				$(document).unbind('keydown',mykd);
			}else if(e.ctrlKey && e.keyCode == 69){//ctrl+e
				$('#nextVoucher').click();
				$(document).unbind('keydown',mykd);
			}else if(e.ctrlKey && e.keyCode == 8){//ctrl+backspace
				var row = $('#detailList').datagrid('getRows');
				var index = row.length-1;
				if(index!=-1){
					$(document).unbind('keydown',mykd);
					row[index].newRow==1?setTimeout(function(){$('#btn-'+index+'-back').click();$(document).bind('keydown',mykd);},400):null;
				}
			}
		}
	});

	//保存摘要
	function addRs(index){
		var name = (getEditor(index,"resume").next())[0].comboObj.getComboText();
		if(name == ''){
			$.fool.alert({msg:'摘要为空，不能保存'});
			return false;
		}
		$.ajax({
			url:'${ctx}/basedata/saveFast',
			type:'POST',
			data:{name:name,categoryCode:"014"},
			success:function(data){
				dataDispose(data);
				if(data){
					if(data.result == '0'){
						$.fool.alert({msg:'摘要保存成功！',time:1000,fn:function(){
							voucherResume.push({value:"",text:name});
							(getEditor(index,"resume").next())[0].comboObj.load({options:voucherResume});
                            var refresh = $(".mytoolsBar a.btn-refresh")[0];
                            refresh.click();
							/* var $t =$.fool._get$($('#detailList'));
							var editor = $t.fool('getEditor$',{field:"resume",index:index});
							editor.combotree({
								onLoadSuccess:function(data){
									editor.combotree('setText',name);
								}
							});
							editor.combotree('reload');
							editor.combotree('textbox').bind('keydown',treefkd);
							editor.combotree('textbox').focus(function(){
								editor.combotree('showPanel');
							  }); */
							/* var myinput = editor.combotree('textbox');
							  myinput.css('width',myinput.width()-20);
							  myinput.css('margin-right',42);
							  myinput.prev().append('<a href="javascript:;" onclick="addRs('+index+');" class="btn-save" style="float:left;"></a>');
							  editor.combotree('textbox').focus(); */
						}});
					}else if(data.result == '1'){
						$.fool.alert({msg:data.msg});
					}else{
						$.fool.alert({msg:'系统繁忙，请稍后再试'});
					}
				}else{
					return false;
				}
			}
		});
	}

	function getNext(){
		var gridParam = $("#voucherList").jqGrid("getGridParam");
		//var pagination=$("#voucherList").datagrid("getPager");
		//var pageSize=gridParam.rowNum;
		var pageNumber=gridParam.page;
		//var url=$("#voucherList").datagrid("options").url;
		//var page=(pageNumber-1)*pageSize-(times-1);
		var page = pageNumber - 1;
		if(pageNumber<=1){
			return false;
		}
		//times=times+1;
		var result = "";
		var postdata = gridParam.postData;
		postdata.page=page;
		postdata.startDay=$('#createTimeStr-search').datebox("getValue");
		postdata.endDay=$('#createTimeEnd-search').datebox("getValue");
		var url = '${ctx}/voucher/query';
		 $.ajax({
			url:url,
			async:false,
			data:postdata,
			success:function(data){
				if(data){
					result=data.rows;
				}
			}
		});
		 $('#voucherList').jqGrid("setGridParam",{page:page}).trigger("reloadGrid");
			if(result){
				return result;
			}else{
				return false;
			}
	}
	function getPrev(){
		var gridParam = $("#voucherList").jqGrid("getGridParam");
		//var pagination=$("#voucherList").datagrid("getPager");
		//var pageSize=gridParam.rowNum;
		/* var pageNumber=gridParam.page; */
		//var url=$("#voucherList").datagrid("options").url;
		//var page=(pageNumber-1)*pageSize-(times-1);
		var page = gridParam.lastpage;
		//times=times+1;
		var result = "";
		var postdata = gridParam.postData;
		postdata.page=page;
		postdata.startDay=$('#createTimeStr-search').datebox("getValue");
		postdata.endDay=$('#createTimeEnd-search').datebox("getValue");
		var url = '${ctx}/voucher/query';
		 $.ajax({
			url:url,
			async:false,
			data:postdata,
			success:function(data){
				if(data){
					result=data.rows;
				}
			}
		});
		 $('#voucherList').jqGrid("setGridParam",{page:page}).trigger("reloadGrid");
			if(result){
				return result;
			}else{
				return false;
			}
	}
</script>
</body>
</html>