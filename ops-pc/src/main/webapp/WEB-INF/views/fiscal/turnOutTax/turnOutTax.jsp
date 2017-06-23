<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<title>转出未交增值税</title>
<style>
  .form1 p font{
    width:auto;
    min-width: 180px;
  }
  .first{
    margin-left:10% !important;
  }
</style>
</head>

<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>转出未交增值税</h1>
                </div>             
             </div>
  <div class="formBox" >
    <form class="form1">
      <input id="outSubjectId" name="outSubjectId" type="hidden" <%-- value="${entity.outSubjectId}" --%>/>
      <input id="inSubjectId" name="inSubjectId" type="hidden" <%-- value="${entity.inSubjectId}" --%>/>
      <input id="taxSubjectId" name="taxSubjectId" type="hidden" <%-- value="${entity.taxSubjectId}" --%>/>
      <input id="unpaidSubjectId" name="unpaidSubjectId" type="hidden" <%-- value="${entity.unpaidSubjectId}" --%>/>
      <input id="paySubjectId" name="paySubjectId" type="hidden" <%-- value="${entity.paySubjectId}" --%>/>
      <p class="first"><font><em>*</em>凭证日期：</font><input id="voucherDate" class="textBox" name="voucherDate" value=""/></p><br/>
      <p class="first"><font><em>*</em>凭证字：</font><input id="voucherWord" class="textBox" name="voucherWordId" value=""/></p><br/>
      <p class="first"><font>凭证摘要：</font><input id="voucherResume" class="textBox" name="" value=""/></p><br/>
      
      <p class="first"><font><em>*</em>销项税科目编号：</font><input id="outSubjectCode" class="textBox" name="outSubjectCode" readonly="readonly" <%-- value="${entity.outSubjectCode}" --%>/></p>
      <p><font><em>*</em>销项税科目名称：</font><input id="outSubjectName" class="textBox" name="outSubjectName" <%-- value="${entity.outSubjectName}" --%>/></p><br/>
      
      <p class="first"><font><em>*</em>进项税科目编号：</font><input id="inSubjectCode" class="textBox" name="inSubjectCode" readonly="readonly" <%-- value="${entity.inSubjectCode}" --%>/></p>
      <p><font><em>*</em>进项税科目名称：</font><input id="inSubjectName" class="textBox" name="inSubjectName" <%-- value="${entity.inSubjectName}" --%>/></p><br/>
      
      <p class="first"><font><em>*</em>转出未交增值税科目编号：</font><input id="taxSubjectCode" class="textBox" name="taxSubjectCode" readonly="readonly" <%-- value="${entity.taxSubjectCode}" --%>/></p>
      <p><font><em>*</em>转出未交增值税科目名称：</font><input id="taxSubjectName" class="textBox" name="taxSubjectName" <%-- value="${entity.taxSubjectName}" --%>/></p><br/>
      
      <p class="first"><font><em>*</em>未交增值税科目编号：</font><input id="unpaidSubjectCode" class="textBox" name="unpaidSubjectCode" readonly="readonly" <%-- value="${entity.unpaidSubjectCode}" --%>/></p>
      <p><font><em>*</em>未交增值税科目名称：</font><input id="unpaidSubjectName" class="textBox" name="unpaidSubjectName" <%-- value="${entity.unpaidSubjectName}" --%>/></p><br/>
      
      <p class="first"><font><em>*</em>支付税费科目编号：</font><input id="paySubjectCode" class="textBox" name="paySubjectCode" readonly="readonly" <%-- value="${entity.paySubjectCode}" --%>/></p>
      <p><font><em>*</em>支付税费科目名称：</font><input id="paySubjectName" class="textBox" name="paySubjectName" <%-- value="${entity.paySubjectName}" --%>/></p><br/>
      
      <p class="first"><font></font><input id="save-btn" class="btn-yellow2 btn-xs" type="button" onclick="saver()" value="结转转出未交增值税"/></p>
      <p><input id="save-btn2" class="btn-yellow2 btn-xs" type="button" onclick="saver2()" value="结转未交增值税"/></p>
      <p><input id="save-btn3" class="btn-yellow2 btn-xs" type="button" onclick="saver3()" value="支付未交增值税"/></p>
    </form>
  </div>
<script type="text/javascript">

  /*var newData = "";
  var optionCount = "";*/

  var voucherWordCombo=$("#voucherWord").fool('dhxCombo',{
	  width:160,
	  height:32,
	  focusShow:true,
	  editable:false,
	  data:getComboData(getRootPath()+'/basedata/voucherWord',"tree"),
	  setTemplate:{
		  input:"#name#",
		  option:"#name#"
	  },
      toolsBar:{
          name:"凭证字",
          addUrl:"/basedata/listAuxiliaryAttr",
          refresh:true
      },
	  onLoadSuccess:function(){
			  ($("#voucherWord").next())[0].comboObj.selectOption(1);
	  },
  });
  var outSubjectCombo = $('#outSubjectName').fool('dhxComboGrid',{
	  width:320,
	  height:32,
	  required:true,
	  novalidate:true,
	  required:true,
	  missingMessage:"请先选取科目",
	  focusShow:true,
	  data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  searchKey:"q", 
	  setTemplate:{
		  input:"#name#",
		  columns:[
		           {option:'#code#',header:'编号',width:100},
				   {option:'#name#',header:'名称',width:100},
					],
	  },
      toolsBar:{
          name:"科目",
          addUrl:"/fiscalSubject/manage",
          refresh:true
      },
	  onSelectionChange:function(){
		  $("#outSubjectId").val(($('#outSubjectName').next())[0].comboObj.getSelectedValue());
		  $("#outSubjectCode").val(($('#outSubjectName').next())[0].comboObj.getSelectedText().code);
	  }
  });
  $('#outSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  outSubjectCombo.closeAll();
	  outSubjectChooser();
  });
  $.ajax({
	  url:"${ctx}/fiscalSubject/getByCode?getChild=1",
	  /* async:false, */
	  data:{code:"2221.01.06"},
	  success:function(data){
		  if(data){
			  outSubjectCombo.setComboValue(data.fid);
			  $("#outSubjectId").val(data.fid);
			  $("#outSubjectCode").val(data.code);
			  
		  }
	  }
  });
  var inSubjectCombo = $('#inSubjectName').fool('dhxComboGrid',{
		width:320,
		height:32,
		required:true,
		focusShow:true,
	  	data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  	filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  	searchKey:"q", 
	  	setTemplate:{
	  		input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
	  	},
	  	toolsBar:{
	          name:"科目",
	          addUrl:"/fiscalSubject/manage",
	          refresh:true
	      },
	  	onSelectionChange:function(){
	  		$("#inSubjectId").val(($('#inSubjectName').next())[0].comboObj.getSelectedValue());
			$("#inSubjectCode").val(($('#inSubjectName').next())[0].comboObj.getSelectedText().code);
	  	}
  });
  $('#inSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  inSubjectCombo.closeAll();
	  inSubjectChooser();
  });
  $.ajax({
	  url:"${ctx}/fiscalSubject/getByCode?getChild=1",
	  /* async:false, */
	  data:{code:"2221.01.01"},
	  success:function(data){
		  if(data){
			  inSubjectCombo.setComboValue(data.fid);
			  $("#inSubjectId").val(data.fid);
			  $("#inSubjectCode").val(data.code);
		  }
	  }
  });
  var taxSubjectCombo = $('#taxSubjectName').fool('dhxComboGrid',{
	  width:320,
	  height:32,
	  required:true,
	  focusShow:true,
	  data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  searchKey:"q", 
	  setTemplate:{
		  input:"#name#",
		  columns:[
		           {option:'#code#',header:'编号',width:100},
				   {option:'#name#',header:'名称',width:100},
					],
	  },
	  toolsBar:{
          name:"科目",
          addUrl:"/fiscalSubject/manage",
          refresh:true
      },
	  onSelectionChange:function(){
		  $("#taxSubjectId").val(($('#taxSubjectName').next())[0].comboObj.getSelectedValue());
		  $("#taxSubjectCode").val(($('#taxSubjectName').next())[0].comboObj.getSelectedText().code);
	  }
  });
  $('#taxSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  taxSubjectCombo.closeAll();
	  taxSubjectChooser();
  });
  $.ajax({
	  url:"${ctx}/fiscalSubject/getByCode?getChild=1",
	  /* async:false, */
	  data:{code:"2221.01.05"},
	  success:function(data){
		  if(data){
			  taxSubjectCombo.setComboValue(data.fid);
			  $("#taxSubjectId").val(data.fid);
			  $("#taxSubjectCode").val(data.code);
		  }
	  }
  });
  var unpaidSubjectCombo = $('#unpaidSubjectName').fool('dhxComboGrid',{
	  width:320,
	  height:32,
	  required:true,
	  focusShow:true,
	  data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  searchKey:"q", 
	  setTemplate:{
		  input:"#name#",
		  columns:[
		           {option:'#code#',header:'编号',width:100},
				   {option:'#name#',header:'名称',width:100},
					],
	  },
	  toolsBar:{
          name:"科目",
          addUrl:"/fiscalSubject/manage",
          refresh:true
      },
	  onSelectionChange:function(){
		  $("#unpaidSubjectId").val(($('#unpaidSubjectName').next())[0].comboObj.getSelectedValue());
		  $("#unpaidSubjectCode").val(($('#unpaidSubjectName').next())[0].comboObj.getSelectedText().code);
	  }
  });
  $('#unpaidSubjectName').next().find(".dhxcombo_select_button").click(function(){
	  unpaidSubjectCombo.closeAll();
	  unpaidSubjectChooser();
  });
  $.ajax({
	  url:"${ctx}/fiscalSubject/getByCode?getChild=1",
	  /* async:false, */
	  data:{code:"2221.02"},
	  success:function(data){
		  if(data){
			  unpaidSubjectCombo.setComboValue(data.fid);
			  $("#unpaidSubjectId").val(data.fid);
			  $("#unpaidSubjectCode").val(data.code);
		  }
	  }
  });
  var paySubjectCombo = $('#paySubjectName').fool('dhxComboGrid',{
	  width:320,
	  height:32,
	  required:true,
	  focusShow:true,
	  data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  searchKey:"q", 
	  setTemplate:{
		  input:"#name#",
		  columns:[
		           {option:'#code#',header:'编号',width:100},
				   {option:'#name#',header:'名称',width:100},
					],
	  },
	  toolsBar:{
          name:"科目",
          addUrl:"/fiscalSubject/manage",
          refresh:true
      },
	  onSelectionChange:function(){
		  $("#paySubjectId").val(($('#paySubjectName').next())[0].comboObj.getSelectedValue());
		  $("#paySubjectCode").val(($('#paySubjectName').next())[0].comboObj.getSelectedText().code);
	  }
  });
  $('#paySubjectName').next().find(".dhxcombo_select_button").click(function(){
	  paySubjectCombo.closeAll();
	  paySubjectChooser();
  });
  $.ajax({
	  url:"${ctx}/fiscalSubject/getByCode?getChild=1",
	  /* async:false, */
	  data:{code:"1002"},
	  success:function(data){
		  if(data){
			  paySubjectCombo.setComboValue(data.fid);
			  $("#paySubjectId").val(data.fid);
			  $("#paySubjectCode").val(data.code);
		  }
	  }
  });
  
  $("#voucherDate").datebox({
		novalidate:true,
		required:true,
		width:158,
		height:30,
		editable:false,
  });
  $.post('${ctx}/voucher/getDefaultMessage',function(data){
		$('#voucherDate').datebox('setValue',data.voucherDate);
	});
 /*  $('#voucherResume').combotree({ 
		url:'${ctx}/basedata/resume',
		width:158,
		height:30,
		editable:true,
		onBeforeSelect:function(node){
			if(node.text=='摘要'){
				return false;
			}
		},
		onShowPanel:function(){
			$('#voucherResume').combotree('panel').find('.tree').children().children('.tree-node').find('.tree-title').text('请选择');
		}
	}); */
  var voucherResumeValue = "";
  $.ajax({
      url: "${ctx}/basedata/resume?num=" + Math.random(),
      async: false,
      data: {},
      success: function (data) {
          voucherResumeValue = formatTree(data[0].children, "text", "id")
      }
  });
	var resumeCombo=$("#voucherResume").fool('dhxComboGrid',{
		width:160,
		height:32,
		focusShow:true,
		/* editable:false, */
		data:voucherResumeValue,
        filterUrl:'${ctx}/basedata/fuzzyFindSubAuxiliaryAttrTree?code=014&num='+Math.random(),
        setTemplate: {
            input: "#name#",
            columns: [
                {option: '#name#', header: '内容', width: 100},
            ],
        },
        toolsBar:{
            name:"摘要",
            addUrl:"/basedata/listAuxiliaryAttr",
            refresh:true
        },
        /*onChange:function () {
            hasAbstract();
            newData = "";
        }*/
	});

  resumeCombo.hdr = null;

  var refresh = $(".mytoolsBar a.btn-refresh")[0];
  var sInput = $(resumeCombo.getInput());
  //修改input宽度
  sInput.css("width", sInput.width() - 20);
  sInput.after('<a href="javascript:;" id="resumeSave" class="btn-save"></a>');
  sInput.next().css({'float': 'right', 'margin': '5px 23px', 'display': ''});
  $("#resumeSave").click(function () {
      var name = ($('#voucherResume').next())[0].comboObj.getComboText();
      if (name == "") {
          $.fool.alert({msg: '摘要不能为空！', time: 1000});
          return;
      }
      $.ajax({
          url: '${ctx}/basedata/saveFast',
          type: 'POST',
          data: {name: name, categoryCode: '014'},
          success: function (data) {
              dataDispose(data);
              if (data) {
                  if (data.returnCode == 0) {
                      $.fool.alert({
                          msg: '摘要保存成功！', time: 1000, fn: function () {
                              newData = data.data;
                              voucherResumeValue.push({value: "", text: name});
                              //重新加载一下Combo
                              ($('#voucherResume').next())[0].comboObj.load({options: voucherResumeValue});
                              refresh.click();
                          }
                      });
                      /*sInput.next().css('display', 'none');*/
                  } else if (data.returnCode == 1) {
                      $.fool.alert({msg: data.msg});
                  } else {
                      $.fool.alert({msg: '系统繁忙，请稍后再试'});
                  }
              }
          }

      })
  });

  function hasAbstract() {
      optionCount = resumeCombo.getOptionsCount();
      if(optionCount <= 2){
          sInput.next().css('display', '');
      }else{
          sInput.next().css('display', 'none');
      }
  }

  function outSubjectChooser(){
	  ($("#outSubjectName").next())[0].comboObj.closeAll();
	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectOutSubject&onDblClick=selectOutSubjectDBC&singleSelect=true&flag=1'});
  };
  function inSubjectChooser(){
	  ($("#inSubjectName").next())[0].comboObj.closeAll();
	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectInSubject&onDblClick=selectInSubjectDBC&singleSelect=true&flag=1'});
  };
  function taxSubjectChooser(){
	  ($("#taxSubjectName").next())[0].comboObj.closeAll();
	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectTaxSubject&onDblClick=selectTaxSubjectDBC&singleSelect=true&flag=1'});
  };
  function unpaidSubjectChooser(){
	  ($("#unpaidSubjectName").next())[0].comboObj.closeAll();
	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectUnpaidSubject&onDblClick=selectUnpaidSubjectDBC&singleSelect=true&flag=1'});
  };
  function paySubjectChooser(){
	  ($("#paySubjectName").next())[0].comboObj.closeAll();
	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectPaySubject&onDblClick=selectPaySubjectDBC&singleSelect=true&flag=1'});
  };
  
  function selectOutSubject(rowData){
	  $("#outSubjectId").val(rowData[0].fid);
	  $("#outSubjectCode").val(rowData[0].code);
	  ($("#outSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window("close");
  }
  function selectOutSubjectDBC(rowData){
	  $("#outSubjectId").val(rowData.fid);
	  $("#outSubjectCode").val(rowData.code);
	  ($("#outSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window("close");
  }
  function selectInSubject(rowData){
	  $("#inSubjectId").val(rowData[0].fid);
	  $("#inSubjectCode").val(rowData[0].code);
	  ($("#inSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window("close");
  }
  function selectInSubjectDBC(rowData){
	  $("#inSubjectId").val(rowData.fid);
	  $("#inSubjectCode").val(rowData.code);
	  ($("#inSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window("close");
  }
  function selectTaxSubject(rowData){
	  $("#taxSubjectId").val(rowData[0].fid);
	  $("#taxSubjectCode").val(rowData[0].code);
	  ($("#taxSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window("close");
  }
  function selectTaxSubjectDBC(rowData){
	  $("#taxSubjectId").val(rowData.fid);
	  $("#taxSubjectCode").val(rowData.code);
	  ($("#taxSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window("close");
  }
  function selectUnpaidSubject(rowData){
	  $("#unpaidSubjectId").val(rowData[0].fid);
	  $("#unpaidSubjectCode").val(rowData[0].code);
	  ($("#unpaidSubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window("close");
  }
  function selectUnpaidSubjectDBC(rowData){
	  $("#unpaidSubjectId").val(rowData.fid);
	  $("#unpaidSubjectCode").val(rowData.code);
	  ($("#unpaidSubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window("close");
  }
  function selectPaySubject(rowData){
	  $("#paySubjectId").val(rowData[0].fid);
	  $("#paySubjectCode").val(rowData[0].code);
	  ($("#paySubjectName").next())[0].comboObj.setComboText(rowData[0].name);
	  chooserWindow.window("close");
  }
  function selectPaySubjectDBC(rowData){
	  $("#paySubjectId").val(rowData.fid);
	  $("#paySubjectCode").val(rowData.code);
	  ($("#paySubjectName").next())[0].comboObj.setComboText(rowData.name);
	  chooserWindow.window("close");
  }
  
  function saver(){
	  var vo=$('.form1').serialize()+"&resume="+resumeCombo.getComboText();
      /*var hasValue = ($('#voucherResume').next())[0].comboObj.getSelectedValue();
      if (hasValue == null) {
          hasValue = newData;
      }
      var hasName = ($('#voucherResume').next())[0].comboObj.getComboText();
      if (hasValue == null && hasName != "" && optionCount <= 2|| hasValue == "" && hasName != "" && optionCount <= 2) {
          $.fool.alert({time: 1000, msg: '请先保存凭证摘要！'});
          return;
      }else if(hasValue == null && hasName != "" && optionCount > 2|| hasValue == "" && hasName != "" && optionCount > 2){
          $.fool.alert({time: 1000, msg: '您没有选择凭证摘要！'});
          return;
      };*/
	  $('.form1').form('enableValidation');
	  if($('.form1').form('validate')){
		  $('#save-btn').attr("disabled","disabled");
		  $.post('${ctx}/turnOutTax/saveOutUnpaidTax',vo,function(data){
			  if(data.returnCode =='0'){
				  $.fool.alert({time:1000,msg:"操作成功！",fn:function(){
		    		  $('#save-btn').removeAttr("disabled");
		    		  var src='/voucher/manage?id='+data.data;
		    		  var text='填制凭证';
		    		  parent.kk(src,text);
				  }});
			  }else if(data.returnCode =='1'){
				  $.fool.alert({msg:data.message});
		    	  $('#save-btn').removeAttr("disabled");
			  }else{
				  $.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
		    	  $('#save-btn').removeAttr("disabled");
			  }
		  });
	  };
  }
  function saver2(){
	  var vo=$('.form1').serialize()+"&resume="+resumeCombo.getComboText();
/*      var hasValue = ($('#voucherResume').next())[0].comboObj.getSelectedValue();
      if (hasValue == null) {
          hasValue = newData;
      }
      var hasName = ($('#voucherResume').next())[0].comboObj.getComboText();
      if (hasValue == null && hasName != "" && optionCount <= 2|| hasValue == "" && hasName != "" && optionCount <= 2) {
          $.fool.alert({time: 1000, msg: '请先保存凭证摘要！'});
          return;
      }else if(hasValue == null && hasName != "" && optionCount > 2|| hasValue == "" && hasName != "" && optionCount > 2){
          $.fool.alert({time: 1000, msg: '您没有选择凭证摘要！'});
          return;
      };*/
	  $('.form1').form('enableValidation');
	  if($('.form1').form('validate')){
		  $('#save-btn').attr("disabled","disabled");
		  $.post('${ctx}/turnOutTax/saveChangeUnpaidTax',vo,function(data){
			  if(data.returnCode =='0'){
				  $.fool.alert({time:1000,msg:"操作成功！",fn:function(){
		    		  $('#save-btn').removeAttr("disabled");
		    		  var src='/voucher/manage?id='+data.data;
		    		  var text='填制凭证';
		    		  parent.kk(src,text);
				  }});
			  }else if(data.returnCode =='1'){
				  $.fool.alert({msg:data.message});
		    	  $('#save-btn').removeAttr("disabled");
			  }else{
				  $.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
		    	  $('#save-btn').removeAttr("disabled");
			  }
		  });
	  };
  }
  function saver3(){
	  var vo=$('.form1').serialize()+"&resume="+resumeCombo.getComboText();
     /* var hasValue = ($('#voucherResume').next())[0].comboObj.getSelectedValue();
      if (hasValue == null) {
          hasValue = newData;
      }
      var hasName = ($('#voucherResume').next())[0].comboObj.getComboText();
      if (hasValue == null && hasName != "" && optionCount <= 2|| hasValue == "" && hasName != "" && optionCount <= 2) {
          $.fool.alert({time: 1000, msg: '请先保存凭证摘要！'});
          return;
      }else if(hasValue == null && hasName != "" && optionCount > 2|| hasValue == "" && hasName != "" && optionCount > 2){
          $.fool.alert({time: 1000, msg: '您没有选择凭证摘要！'});
          return;
      };*/
	  $('.form1').form('enableValidation');
	  if($('.form1').form('validate')){
		  $('#save-btn').attr("disabled","disabled");
		  $.post('${ctx}/turnOutTax/savePayUnpaidTax',vo,function(data){
			  if(data.returnCode =='0'){
				  $.fool.alert({time:1000,msg:"操作成功！",fn:function(){
		    		  $('#save-btn').removeAttr("disabled");
		    		  var src='/voucher/manage?id='+data.data;
		    		  var text='填制凭证';
		    		  parent.kk(src,text);
				  }});
			  }else if(data.returnCode =='1'){
				  $.fool.alert({msg:data.message});
		    	  $('#save-btn').removeAttr("disabled");
			  }else{
				  $.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
		    	  $('#save-btn').removeAttr("disabled");
			  }
		  });
	  };
  }
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
