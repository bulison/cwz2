<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
  <body>
 <style>
.form,.form1{padding:10px 0px;}
.form1 p{margin:10px 0px 0px 0px;}
</style>
  <div class="form1">
  <form id="form" method="post">
  	<c:if test="${param.copy ne 'true'}">
     <input type="hidden"  id="fid" name="fid" value="${vo.fid}" />
     <input type="hidden" id="updateTime" name="updateTime" value="${vo.updateTime}"/>
     <input type="hidden" id="recordStatus" name="recordStatus" value="${vo.recordStatus}"/>
     <c:set var="_code" value="${vo.code}"/>
     <c:set var="_name" value="${vo.name}"/>
     <c:set var="_shortName" value="${vo.shortName}"/>
    </c:if>
    
    <p><font><em>*</em>编号：</font><input value="${_code}" type="text" class="easyui-validatebox textBox blurValidation" data-options="required:true,validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="code" name="code"/></p> 
  	<p><font><em>*</em>公司名称：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="required:true,validType:'norma',novalidate:true,missingMessage:'该项不能为空！'" maxLength="100" id="name" name="name" value="${_name}"/></p>
	  <p id="p1" style="display: none;"><font><em>*</em>状态：</font><c:choose>

		  <c:when test="${vo.recordStatus == 'SNU'}">
			  <input  type="radio" name="recordStatus" value="SAC"/>有效
			  <input type="radio"  name="recordStatus" checked="checked"  value="SNU"/>无效
		  </c:when>
		  <c:otherwise>
			  <input type="radio" name="recordStatus" checked="checked" value="SAC"/>有效
			  <input type="radio"  name="recordStatus" value="SNU"/>无效
		  </c:otherwise>
	  </c:choose>
	  </p>
	  <p><font>公司简称：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'norma',novalidate:true,missingMessage:'该项不能为空！'" maxLength="100" id="shortName" name="shortName" value="${_shortName}"/></p>
    
  	<p><font>地区：</font><input type="text" class=" blurValidation" id="areaId" name="areaId" value="${vo.areaId}"/></p>
  	<p><font>类别：</font><input type="text" class=" blurValidation" id="categoryId" name="categoryId" value="${vo.categoryId}"/></p>
  	<p><font>用户配置信用额度：</font><input type="text" class="easyui-numberbox textBox blurValidation" data-options="width:160, height:31,precision:2,novalidate:true,validType:['assetValue','numMaxLength[10]'],missingMessage:'该项不能为空！'" maxLength="10" id="creditLineUser" name="creditLineUser" value="${vo.creditLineUser}"/></p>
  	<p><font>系统计算信用额度：</font><input type="text" class="easyui-numberbox textBox blurValidation" data-options="width:160, height:31,precision:2,novalidate:true,validType:['assetValue','numMaxLength[10]'],missingMessage:'该项不能为空！'" maxLength="10" id="creditLineSys" name="creditLineSys" value="${vo.creditLineSys}" readonly="readonly"/></p>
  	<p><font>业务联系人：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="businessContact" name="businessContact" value="${vo.businessContact}"/></p>
  	<p><font>业务联系人传真：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'fax',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="fax" name="fax" value="${vo.fax}"/></p>
 	<p><font>业务联系人手机：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'phone',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="phone" name="phone" value="${vo.phone}"/></p>
 	<p><font>邮箱：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'email',novalidate:true,missingMessage:'该项不能为空！'" maxLength="128" id="email" name="email" value="${vo.email}"/></p>
 	<%-- <p><font>公司电话：</font><input type="text" class="easyui-validatebox textBox blurValidation" id="areaCode" style="width:40px"/> - <input type="text" class="easyui-validatebox textBox" data-options="validType:'tel',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="tel" style="width:60px"/> - <input type="text" id="extension" class="easyui-validatebox textBox" style="width:40px"/><input type="hidden" name="tel" id="telNum" value="${vo.tel}"/></p> --%> 
 	
 	<p><font>公司电话：</font><input type="text" class="easyui-validatebox textBox blurValidation" id="tel" name="tel" value="${vo.tel}" data-options="validType:'tel',novalidate:true"/></p>
 	
 	<p><font>采购员：</font><input type="text" class="textBox" id="memberName" name="memberName" value="${vo.memberName}"/><input type="hidden" class="" name="memberId" id="memberId" value="${vo.memberId}"/></p>
 	<p><font>法人代表：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="principal" name="principal" value="${vo.principal}"/></p> 
 	<p><font>法人联系电话：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="principalPhone" name="principalPhone" value="${vo.principalPhone}"/></p>
 	<p><font>征信级别：</font><input type="text" class=" blurValidation" id="creditRatingId" name="creditRatingId" value="${vo.creditRatingId}"/></p>
 	<p><font>邮编：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="16" id="postCode" name="postCode" value="${vo.postCode}"/></p>
 	<p><font>注册资金：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:['assetValue','numMaxLength[10]'],novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="registedCapital" name="registedCapital" value="${vo.registedCapital}"/></p>
 	<p><font>在业人数：</font><input type="text" class="easyui-numberbox textBox blurValidation" data-options="width:160, height:31,validType:['assetValue','numMaxLength[10]'],novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="staffNum" name="staffNum" value="${vo.staffNum}"/></p>
 	<p><font>开户银行：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="bank" name="bank" value="${vo.bank}"/></p>
 	<p><font>账户：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="account" name="account" value="${vo.account}"/></p>
 	<p><font>国税号：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="nationTax" name="nationTax" value="${vo.nationTax}"/></p>
 	<p><font>地税号：</font><input type="text" class="easyui-validatebox textBox blurValidation" data-options="validType:'normalChar',novalidate:true,missingMessage:'该项不能为空！'" maxLength="50" id="landTax" name="landTax" value="${vo.landTax}"/></p>
 	<p><font>成立日期：</font><input type="text" class="easyui-datebox blurValidation" data-options="validType:'date',novalidate:true,editable:false,missingMessage:'该项不能为空！'" id="registerDate" name="registerDate" style="width:160px; height:31px;" value="${vo.registerDate}"/></p>
	<p><font>经营范围：</font><input type="text" data-options="novalidate:true,missingMessage:'该项不能为空！'" id="bussinessRange"  class="easyui-validatebox textBox blurValidation"  name="bussinessRange" style="width:160px; height:25px;" value="${vo.bussinessRange}"/></p>
	<p><font>地址：</font><input type="text" data-options="novalidate:true,missingMessage:'该项不能为空！'" id="address"  class="easyui-validatebox textBox blurValidation"  name="address" style="width:160px; height:25px;" value="${vo.address}"/></p>
	<p><font>默认发货地址：</font><a href="javascript:;" id="customerAddress" class="btn-ora-introduce">设置地址</a></p>
	<p><font>描述：</font><textarea  class="easyui-validatebox blurValidation" data-options="novalidate:true,missingMessage:'该项不能为空！'" id="describe" name="describe" style="width:466px;height:73px; resize:none; overflow:auto; vertical-align:text-top; border-color:#CCC">${vo.describe}</textarea></p>
<br/>
 	<p style="text-align: center;width:100%;">
 	<input type="button" id="save" class="btn-blue2 btn-xs" value='保存' />
<!--   	<input type="button" id="reset" class="btn-gray btn-xs" value='重置' /> -->
	</p>
  </form>
  <div id="pop-win"></div>
  </div>
  
<script type="text/javascript">
var win = "";
var _Name = '${_name}';
$(function(){
	/* var str='${vo.tel}'.split("-");
    $('#areaCode').val(str[0]);
    $('#tel').val(str[1]);
    $('#extension').val(str[2]);
	
	$('#areaCode').change(function(e) {
	    $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
	});
	$('#tel').change(function(e) {
	    $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
	});
	$('#extension').change(function(e) {
	    $('#telNum').val($('#areaCode').val()+'-'+$('#tel').val()+'-'+$('#extension').val());
	}); */
	
	/* $("#areaId").fool('combotree',{width:160,valueField:'id',textField:'text',required:false,url:'${ctx}/basedata/findSubAuxiliaryAttrTree?code=001',onLoadSuccess:function(node,data){
		$("#areaId").combotree('setValue', '${vo.areaId}');
	}});
	$("#categoryId").fool('combotree',{width:160,valueField:'id',textField:'text',required:false,url:'${ctx}/basedata/findSubAuxiliaryAttrTree?code=002',onLoadSuccess:function(node,data){
		$("#categoryId").combotree('setValue', '${vo.categoryId}');
	}});
	$("#creditRatingId").fool('combotree',{width:160,valueField:'id',textField:'text',required:false,url:'${ctx}/basedata/findSubAuxiliaryAttrTree?code=003',onLoadSuccess:function(node,data){
		$("#creditRatingId").combotree('setValue', '${vo.creditRatingId}');
	}}); */
	<c:if test="${param.copy ne 'true'}">
    $("#p1").css("display","inline-block");
	$('#customerAddress').click(function(){
		win = $("#pop-win").fool('window',{modal:true,'title':"默认发货地址设定",height:380,width:780,href:getRootPath()+'/customerSupplier/cawindow?csvId='+$('#fid').val()+/*"&csvName="+_Name+*/"&csvType=2"});
	});
	</c:if>
	<c:if test="${param.copy eq 'true'}">
	$('#customerAddress').tooltip({
		position: 'top',
		content: '<span style="color:#fff">请完成新增后再进行设置</span>',
		onShow: function(){
			$(this).tooltip('tip').css({backgroundColor: '#2CA7F6',borderColor: '#2CA7F6'});
		}
	});
	</c:if>
	//地区控件初始化
	var areaIdValue='';	
		$.ajax({
			url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=001&num="+Math.random(),
			async:false,		
			success:function(data){		  	
				areaIdValue=formatTree(data[0].children,'text','id');	
		    }
			});
		var areaIdName= $("#areaId").fool("dhxCombo",{
			  width:160,
			  height:32,
			  data:areaIdValue,		
			  editable:false,
			  focusShow:true,
			  setTemplate:{
					input:"#name#",
					option:"#name#"
				  },
				  toolsBar:{
				      name:'地区',
					addUrl:'/basedata/listAuxiliaryAttr',
					refresh:true
				},
			  onLoadSuccess:function(combo){
					combo.setComboValue("${vo.areaId}");
				} 
		});	
		
		//类别控件初始化
		var categoryIdValue='';	
		$.ajax({
			url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=002&num="+Math.random(),
			async:false,		
			success:function(data){		  	
				categoryIdValue=formatTree(data[0].children,'text','id');	
		    }
			});
		var categoryIdName= $("#categoryId").fool("dhxCombo",{
			  width:160,
			  height:32,
			  data:categoryIdValue,		
			  editable:false,
			  focusShow:true,
			  setTemplate:{
					input:"#name#",
					option:"#name#"
				  },
				  toolsBar:{
				      name:'客户类别',
					addUrl:'/basedata/listAuxiliaryAttr',
					refresh:true
				},
			  onLoadSuccess:function(combo){
					combo.setComboValue("${vo.categoryId}");
				} 
		});	
		
		//征信级别初始化
		var creditRatingIdValue='';	
		$.ajax({
			url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=003&num="+Math.random(),
			async:false,		
			success:function(data){		  	
				creditRatingIdValue=formatTree(data[0].children,'text','id');	
		    }
			});
		var creditRatingIdName= $("#creditRatingId").fool("dhxCombo",{
			  width:160,
			  height:32,
			  data:creditRatingIdValue,		
			  editable:false,
			  focusShow:true,
			  setTemplate:{
					input:"#name#",
					option:"#name#"
				  },
				  toolsBar:{
				      name:'征信级别',
					addUrl:'/basedata/listAuxiliaryAttr',
					refresh:true
				},
			  onLoadSuccess:function(combo){
					combo.setComboValue("${vo.creditRatingId}");
				} 
		});	
		
		
	$("#registedCapital").bind("change",function(e){
		var nv = $(this).val()+"";
		value = parseFloat(nv).toFixed(2)+'';
		$(this).val(value);
	});
	
/* 	$('#memberName').fool('combogrid',{
		required:false,
		novalidate:true,
		idField:'fid',
		width:160,
		textField:'username',
		validType:"combogridValid['memberId']",
		panelWidth:450,
		panelHeight:283,
		focusShow:true,
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
		onSelect:function(index,row){
			$("#memberId").val(row.fid);
			$("#memberName").val(row.username).focus();
			$.fool._formValidHelp($('#memberName'));
		},
		onChange:function(nv,ov){
			$("#memberId").val('');
		}
		}); */
		
	var boxWidth=160,boxHeight=31;//统一设置搜索下拉输入框的大小	
	//数据初始化
	var memData="";
	$.ajax({
		url:"${ctx}/basedata/query?num="+Math.random(),
		async:false,
		data:{param:"Organization,Member,AuxiliaryAttr_CostType,CSV"},
		success:function(data){		
		    memData=formatData(data.Member,"fid");	   
	    }
		});

	//鼠标获取焦点出现下拉列表
	var memCombo = $('#memberName').fool('dhxComboGrid',{//采购员
		//required:true,
		novalidate:true,
		focusShow:true,
		width:boxWidth,
		height:boxHeight,
		data:memData,
		hasDownArrow:false,
		text:"${vo.memberName}",
		filterUrl:getRootPath()+'/member/list',
		setTemplate:{
			  input:"#username#",
			  columns:[
					{option:'#userCode#',header:'编号',width:100},
					{option:'#jobNumber#',header:'工号',width:100},
					{option:'#username#',header:'名称',width:100},
					{option:'#phoneOne#',header:'电话',width:100},
					{option:'#deptName#',header:'部门',width:100},
					{option:'#position#',header:'职位',width:100},
				     ]
		  },
		onChange:function(value,text){
			if(!value){
				memCombo.setComboText("");
				$("#memberId").val("");
			}else{
				$("#memberId").val(value);
			}
		}
	});

	//设置鼠标放进去自动出来下拉列表
	$("#form").find("input[_class]").each(function(i,n){($(this));});
	enterController('form');
	
});

  $("#save").click(function(e) {return saveHelp($('#form'));});
</script>
 </body>
 
</html>
