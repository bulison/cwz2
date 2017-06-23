<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
      <div class="form1" >
              <form id="passForm">
                <input id="voucherWordId-pass" name="voucherWordId" type="hidden"/>
				<p><font><em>*</em>会计期间：</font><input id="period-pass" name="fiscalPeriodId" class="textBox"/></p><br/>
				<p><font>凭证字：</font><input id="voucherWordName-pass" name="voucherWordName" class="textBox"/></p><br/>
				<p><font>凭证号：</font><input id="voucherNumberStr-pass" name="startVoucherNumber" class="textBox"/> 至 <input id="voucherNumberEnd-pass" name="endVoucherNumber" class="textBox"/></p><br/>
				<p><font></font><a href="javascript:;" id="ok" class="btn-blue btn-s">确定</a>
              </form>
      </div>    
<script type="text/javascript">
  var chooserWindow_pass="";
  //表单控件初始化
  /* $("#period-pass").combobox({
	  required:true,
	  missingMessage:"该项为必填项。",
	  novalidate:true,
	  valueField:"fid",
	  textField:"period",
	  editable:false,
	  width:164,
	  height:31,
	  url:"${ctx}/fiscalPeriod/getAll"
  }); */
  $("#period-pass").fool("dhxCombo",{
		prompt:'从财务会计期间选择',
		height:30,
		width:167,
		required:true,
		missingMessage:"该项为必填项。",
		novalidate:true,
		editable:false,
		setTemplate:{
			input:"#period#",
			option:"#period#"
		},
		focusShow:true,
		data:getComboData('${ctx}/fiscalPeriod/getAll'),
		onLoadSuccess:function(){
			($("#period-pass").next())[0].comboObj.deleteOption("");
		},
  });
  //$("#voucherWordName-pass").validatebox({});
  $("#voucherNumberStr-pass").validatebox({});
  $("#voucherNumberEnd-pass").validatebox({});
  
  //凭证字选择框操作方法
  /* $('#voucherWordName-pass').fool('voucherWordTree',{
		width:164,
		height:31,
		onBeforeSelect:function(node){
		if(node.text != '请选择'){
			$('#voucherWordId-pass').val(node.id);
		}
	}
  }); */
  $("#voucherWordName-pass").fool("dhxCombo",{
	  width:164,
	  height:31,
	  editable:false,
	  setTemplate:{
		  input:"#name#",
		  option:"#name#"
	  },
	  focusShow:true,
	  data:getComboData("${ctx}/basedata/voucherWord","tree"),
	  onSelectionChange:function(){
		  $('#voucherWordId-pass').val(($("#voucherWordName-pass").next())[0].comboObj.getSelectedValue());
	  },
	  onLoadSuccess:function(){
			($("#voucherWordName-pass").next())[0].comboObj.deleteOption("");
	  },
  });
  /*$("#voucherWordName-pass").click(function(){
	  chooserWindow_pass=$.fool.window({'title':"选择凭证字",href:'${ctx}/voucher/voucherWordWindow?okCallBack=selectVoucherWord&onDblClick=selectVoucherWordDBC&singleSelect=true'});
  });
  function selectVoucherWord(rowData) {
	  $("#voucherWordName-pass").val(rowData[0].text);
	  $("#voucherWordId-pass").val(rowData[0].id);
	  chooserWindow_pass.window('close');
  }
  function selectVoucherWordDBC(rowData) {
	  $("#voucherWordName-pass").val(rowData.text);
	  $("#voucherWordId-pass").val(rowData.id);
      chooserWindow_pass.window('close');
  }*/
  
  $("#voucherNumberStr-pass").numberbox({
	  width:164,
	  height:31,
	  min:0
  });
  $("#voucherNumberEnd-pass").numberbox({
	  width:164,
	  height:31,
	  min:0
  });
//确定按钮点击事件
$("#ok").click(function(){
	$('#passForm').form('enableValidation');
	if($('#passForm').form('validate')){
		$('#ok').attr("disabled","disabled");
		$.post('${ctx}/voucher/batchAudit',$('#passForm').serialize(),function(data){
			if(data.returnCode =='0'){
				$.fool.alert({time:2000,msg:"批审成功！成功数："+data.data.success+",失败数："+data.data.fail,fn:function(){
					$('#passBox').window('close');
	    			$('#ok').removeAttr("disabled");
	    			$('#voucherList').trigger("reloadGrid"); 
				}});
			}else if(data.returnCode =='1'){
	    		$.fool.alert({time:1000,msg:data.message});
	    		$('#ok').removeAttr("disabled");
	    	}else{
	    		$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
	    		$('#ok').removeAttr("disabled");
    		}
		});
	};
});

</script>
</body>
</html>