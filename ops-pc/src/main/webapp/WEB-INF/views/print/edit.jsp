<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<html>
<head>
</head>
<body>
  <div id="detailBox">
    <form id="form" class="form" action="${ctx}/printTempController/save" method="post" enctype="multipart/form-data">
      <input id="fid" name="fid" type="hidden" value="${vo.fid}"/>
      <input id="updateTime" name="updateTime" type="hidden" value='<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"  value="${vo.updateTime}"/>'/>
      <p><font>机构：</font><input id="orgName" name="orgId"/></p>
      <p><font>类型：</font><input id="type" name="type"/></p>
      <p><font>行数：</font><input id="pageRow" name="pageRow" class="textBox" value="${vo.pageRow}"/></p>
      <p><font>模板：</font><input id="file" name="file" type="file"/></p>
      <p><font></font><input id="save" class="btn-blue2 btn-xs" type="submit" value="保存" /></p>
    </form>
  </div>
  <script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>
<script type="text/javascript">
  /* $("#orgName").combobox({
	  editable:false,
	  url:"${ctx}/orgController/getAll",
	  value:"${vo.orgId}",
	  valueField:"fid",
	  textField:"orgName",
	  width:160,
	  height:30
  }); */
  var printData = "",orgData="";
	//加载控件数据
	$.ajax({
		url:getRootPath()+"/basedata/query?num="+Math.random(),
		async:false,
	    data:{param:"AuxiliaryAttr_Print"},
	    success:function(data){
	    	printData=formatTree(data.AuxiliaryAttr_Print[0].children,"text","id");
	    }
	});
  $.ajax({
		url:"${ctx}/orgController/getAll",
		async:false,
	    data:{},
	    success:function(data){
	    	orgData=formatData(data,"fid");
	    }
	});
  
  $("#orgName").fool('dhxCombo',{
	  required:true,
	  novalidate:true,
	  value:"${vo.orgId}",
	  width:162,
	  height:32,
	  data:orgData,
	  focusShow:true,
	  onlySelect:true,
	  searchKey:"orgName",
	  filterUrl:"${ctx}/orgController/getAll",
	  setTemplate:{
		  	input:"#orgName#",
          option:"#orgName#"
	  }
  });
  //重写验证
  $.extend($.fn.validatebox.defaults.rules, {
      int: {// 验证整数
          validator: function (value) {
              return /^\d+?$/i.test(value);
          },
          message: '请输入整数'
      },
  });

  $("#pageRow").validatebox({
     validType: ['int','length[0,10]']
  });

  $("#type").fool("dhxCombo",{
	  data:printData,
	  required:true,
	  //editable:false,
	  value:"${vo.type}",
	  width:162,
	  height:32,
	  toolsBar:{
			name:"打印类型",
			addUrl:"/basedata/listAuxiliaryAttr",
			refresh:true,
		},
	  focusShow:true,
	  setTemplate:{
		  	input:"#name#",
		  	option:"#name#"
	  }
  });
  $("#form").form({
	  onSubmit:function(param){
		  if(!$("#file").val()&&!$("#fid").val()){
			  $("#file").css("background-color","#FFE1E1");
			  return false;
		  }
		  $('#form').form('enableValidation');
		  if(!$('#form').form('validate')){
			  return false;
		  }
	  },
	  success:function(dataStr){
		 var data=eval("("+dataStr+")");
		 dataDispose(data);
		 if(data.result==0){
			 $.fool.alert({time:1000,msg:'保存成功！',fn:function(){
				 $("#edBox").window("close");
				 $("#list").trigger("reloadGrid");
			 }});
		 }else if(data.result==1){
			 $.fool.alert({time:1000,msg:data.msg});
		 }else{
			 $.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
		 }
	  }
  });
</script>
</body>
</html>