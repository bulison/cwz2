<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<body>
<!--esayui验证扩展-->
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/easyui-validate-extend.js?v=${js_v}"></script>

<div class="form1"> 
		<form id="form">
			<p><font><em>*</em>姓名：</font><input type="text" id="userName" name="userName" value="${obj.userName}" class=" easyui-validatebox textBox ops-val"  data-options="required:true,missingMessage:'该项不能为空',novalidate:true,validType:['normalChar','maxLength[50]']" /></p>
			<p><font><em>*</em>邮箱：</font><input type="text" id="email" name="email" value="${obj.email}" class=" easyui-validatebox textBox ops-val"  data-options="required:true,missingMessage:'该项不能为空',novalidate:true,validType:['email','maxLength[50]']" /></p>
			<p><font><em>*</em>手机：</font><input type="text" id="phoneOne" name="phoneOne" value="${obj.phoneOne}" class=" easyui-validatebox textBox ops-val"   data-options="required:true,missingMessage:'该项不能为空',novalidate:true,validType:['number','maxLength[20]']" /></p>
			<p><font>传真：</font><input type="text" id="fax" name="fax" value="${obj.fax}" class=" easyui-validatebox textBox ops-val"  data-options="missingMessage:'该项不能为空',novalidate:true,validType:['fax','maxLength[50]']"/></p>
			<p><font>是否负责人：</font><input id="fisinterface" name="fisinterface" value="${!empty obj.fisinterface?obj.fisinterface:0}"/></p>
			<p><font>性别：</font><input id="sex" name="sex" value="${!empty obj.sex?obj.sex:0}"/></p>
			<p><font>允许移动登陆：</font><input id="isMobileLogin" name="isMobileLogin" value="${!empty obj.isMobileLogin?obj.isMobileLogin:0}"/></p>
			<p><font>有效报价：</font><input id="validPrice" name="validPrice" value="${!empty obj.validPrice?obj.validPrice:1}"/></p>
			<p><font>邮编：</font><input type="text" id="postCode" name="postCode" value="${obj.postCode}" class=" easyui-validatebox textBox" validType="zip" /></p>
			<p><font>地址：</font><input type="text" id="faddress" name="faddress" value="${obj.faddress}" class=" easyui-validatebox textBox" validType="length[2,50]" /></p>
			<p><font>身份证号码：</font><input type="text" id="idCard" name="idCard" value="${obj.idCard}" class="easyui-validatebox textBox" validType="idcard" /></p>
			<p><font>登录帐号：</font><input id="loginName" type="text" name="userCode" class=" easyui-validatebox textBox" value="${obj.userCode}" data-options="required:false,validType:'myAccount'"/></p>
			<p><font>保密级别：</font><input id="securityLevelId" type="text" name="securityLevelId" value="${obj.securityLevelId}" /></p>
			<p><font>描述：</font><textarea class="easyui-validatebox textArea" id="userDesc" name="userDesc"  validType="length[2,200]" style="width: 451px;height: 30px">${obj.userDesc}</textarea></p>
			<br/>
			<p style="margin-left:355px"><font><input type="button" id="save" class="btn-blue4 btn-s" value="保存" /></font></p>
		    <input type="hidden" id="my_deptId" value="${orgId}"  />
		    <input type="hidden" id="my_fid" value="${obj.fid}"/>		           
     </form>
</div>
<script type="text/javascript">
var LevelData = "";
//加载控件数据
$.ajax({
	url:"${ctx}/flow/security/queryAll",
	async:false,
    data:{},
    success:function(data){
    	LevelData=formatData(data,"fid");
    }
});
  $(function(){
	  $('#form').form({
		  success:function(){
			  location.reload(true);
		  }
	  });
	  $(".ops-val").bind('blur', function(){
		  $(this).validatebox('enableValidation').validatebox('validate');
	  });
  });
  var securityCombo = $("#securityLevelId").fool("dhxCombo",{
	  width:162,
	  height:32,
      novalidate:true,
	  editable:false,
	  data:LevelData,
	  setTemplate:{
		  	input:"#name#",
		  	option:"#name#"
	  },
	  toolsBar:{
			name:"保密级别",
			addUrl:"/flow/security/manage",
			refresh:true,
	  },
  	  focusShow:true,
  	  onLoadSuccess:function(combo){
          combo.setComboValue('${obj.securityLevelId}');
		  <%--var option= ($("#securityLevelId").next())[0].comboObj.getOption("${obj.securityLevelId}");--%>
		  <%--if(!option){--%>
			  <%--($("#securityLevelId").next())[0].comboObj.addOption([["${obj.securityLevelId}","${obj.securityLevelName}"]]);--%>
		  <%--}--%>
	  }
  });
 var isMCombo = $("#isMobileLogin").fool("dhxCombo",{
	  width:162,
	  height:32,
      novalidate:true,
	  editable:false,
	  data:[{
		  value:0,text:"不允许"
	  },{
		  value:1,text:"允许"
	  }],
  	  focusShow:true,
  	  clearOpt:false
  });
 var vPriceCombo = $("#validPrice").fool("dhxCombo",{
	  width:162,
	  height:32,
      novalidate:true,
	  editable:false,
	  data:[{
		  value:0,text:"否"
	  },{
		  value:1,text:"是"
	  }],
 	  focusShow:true,
 	  clearOpt:false
 });
 
 var sexCombo = $("#sex").fool("dhxCombo",{
	  width:162,
	  height:32,
      novalidate:true,
	  editable:false,
	  data:[{
		  value:0,text:"男"
	  },{
		  value:1,text:"女"
	  }],
  	  focusShow:true,
  	  clearOpt:false
  });
 var fisCombo = $("#fisinterface").fool("dhxCombo",{
	  width:162,
	  height:32,
	  editable:false,
	  data:[{
		  value:0,text:"否"
	  },{
		  value:1,text:"是"
	  }],
  	  focusShow:true,
  	  clearOpt:false
  });
  $('#save').click(function(e) {
	  var userName=$('#userName').val();
	  var email=$('#email').val();
	  var postCode=$('#postCode').val();
	  var faddress=$('#faddress').val();
	  var fax=$('#fax').val();
	  var idCard=$('#idCard').val();
	  var sex=sexCombo.getSelectedValue();
      var fisinterface=fisCombo.getSelectedValue();
      var isMobileLogin=isMCombo.getSelectedValue();
      var validPrice = vPriceCombo.getSelectedValue();
      var phoneOne=$('#phoneOne').val();
      var userDesc=$('#userDesc').val();
      var userCode=$('#loginName').val();
      var securityLevelId=securityCombo.getSelectedValue();
      var fid = $('#my_fid').val();
      $('#save').attr('disabled','disabled'); 
      /* $("#userName").validatebox('enableValidation').validatebox('validate');
      $('#email').validatebox('enableValidation').validatebox('validate');
      $("#phoneOne").validatebox('enableValidation').validatebox('validate');
      $("#fax").validatebox('enableValidation').validatebox('validate'); 
      $('#loginName').validatebox({required:true}); */
      $('#form').form('enableValidation');
      if($('#form').form('validate')){
    	  $('#save').attr("disabled","disabled");
    	  var data_add = {orgId:id};
    	  var data_edit = {orgId:id,fid:fid};
    	  var mydata = {fid:fid,validPrice:validPrice,securityLevelId:securityLevelId,userCode:userCode,userName:userName,email:email,phoneOne:phoneOne,fax:fax,fisinterface:fisinterface,isMobileLogin:isMobileLogin,sex:sex,postCode:postCode,faddress:faddress,idCard:idCard,userDesc:userDesc};
    	  mydata = $.extend(mydata,data_add);
    	  if('${obj.fid}'!=""){
    		  mydata = $.extend(mydata,data_edit);
    	  }
    	  $.post('${ctx}/userController/saveOrUpdate',mydata,function(data){
    		  dataDispose(data);
    		  if(data.returnCode=='0'){
    			  $.fool.alert({msg:'保存成功！',fn:function(){
    				  if(dhxkey == 1){
    	                    selectTab(dhxname,dhxtab);
    	                }
    				  $('#save').removeAttr("disabled");
    				  $('#aaa').window('close');
        			  $('#datagrid').trigger('reloadGrid');
    			  }});
    		  }else if(data.returnCode=='1'){
    			  $.fool.alert({msg:data.message});
    			  $('#save').removeAttr("disabled");
    		  }else{
    			  $.fool.alert({msg:"系统正忙，请稍后再试。"});
    			  $('#save').removeAttr("disabled");
    		  }
    		  return true;
    	  });
      }else{
    	  $('#save').removeAttr("disabled");
    	  return false;
      }
  });
  <%--if('${obj.sex}'){--%>
	  <%--$("#sex").combobox('setValue','${obj.sex}');  --%>
  <%--}--%>
  <%--if('${obj.isMobileLogin}'){--%>
	  <%--$("#isMobileLogin").combobox('setValue','${obj.isMobileLogin}');--%>
  <%--}--%>
  <%--if('${obj.fisinterface}'){--%>
	  <%--$("#fisinterface").combobox('setValue','${obj.fisinterface}');--%>
  <%--}--%>
</script>

</body>
