<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户管理</title>
</head>
<body>
<style>
.form,.form1{padding:10px 0px;}
.form1 p{margin:10px 0px 0px 0px;}
 .form1 .dateBox{ width:160px; height: 25px;}
</style>
<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
<c:set var="isEdit" value="${param.flag eq 'edit'}"></c:set>
<c:set var="isCopy" value="${param.flag eq 'copy'}"></c:set>
<form id="customer-form" class='form1' action="#">
	<c:if test="${isEdit}">
	<input type="hidden" value="${customer.fid}" id="fid" name="fid"/>
	<input type="hidden" value="${customer.updateTime}" name="updateTime"/>
	<input type="hidden" value="${customer.recordStatus}" name="recordStatus"/>
	</c:if>
	<input type="hidden" value="${customer.memberId}" name="memberId" id="memberId"/>
	<p><font><em>*</em>编号：</font><input type="text" class="textBox blurVali" id="code" name="code" value="${customer.code}"/></p>
	<p><font><em>*</em>名称：</font><input type="text" class="textBox blurVali" id="name" name="name" value="${customer.name}" /></p>
	<p id="p1" style="display: none"><font><em>*</em>状态：</font>
		<c:choose>
		  <c:when test="${customer.recordStatus == 'SNU'}">
			<input  type="radio" name="recordStatus" value="SAC"/>有效
			<input type="radio"  name="recordStatus" checked="checked" value="SNU"/>无效
		  </c:when>
		<%-- 复制页面也默认有效 --%>
		  <c:otherwise>
			<input type="radio" name="recordStatus" checked="checked" value="SAC"/>有效
			<input type="radio"  name="recordStatus" value="SNU"/>无效
		  </c:otherwise>
		</c:choose>
	</p>
	<p><font>简称：</font><input type="text" class="textBox" name="shortName" id="shortName" value="${customer.shortName}" /></p>
	<p><font>地区：</font><input id="areaId" type="text" class="textBox" name="areaId" value="${customer.areaId }"/></p>
	<p><font>分类：</font><input id="categoryId" type="text" class="textBox" name="categoryId" value="${customer.categoryId}" /></p>
	<p><font>用户配置信用额度：</font><input type="text" class="textBox" id="creditLineUser" name="creditLineUser" value="${customer.creditLineUser}" /></p>
	<p class="none"><font>系统计算信用额度：</font><input type="text" class="textBox" id="creditLineSys" name="creditLineSys" value="${customer.creditLineSys}" readonly="readonly"/></p>
	<p><font>业务联系人：</font><input type="text" class="textBox" name="businessContact" value="${customer.businessContact}" /></p>
	<p><font>业务联系人传真：</font><input type="text" class="textBox" id="fax" name="fax" value="${customer.fax}" /></p>
	<p><font>业务联系人手机：</font><input type="text" class="textBox" id="phone" name="phone" value="${customer.phone}" /></p>
	<p><font>Email：</font><input type="text" class="textBox" id="email" name="email" value="${customer.email}" /></p>
	<p><font>电话：</font><input type="text" class="textBox" id="tel" name="tel" value="${customer.tel}" /></p>
	<p><font>业务员：</font><input type="text" class="textBox" id="memberName" name="memberName" value="${customer.memberName}" /></p>
	<p><font>法人代表：</font><input type="text" class="textBox" name="principal" value="${customer.principal}" /></p>
	<p><font>法人联系手机：</font><input type="text" class="textBox" id="principalPhone" name="principalPhone" value="${customer.principalPhone}" /></p>
	<p><font>征信级别：</font><input id="creditRatingId" type="text" class="textBox" name="creditRatingId" value="${customer.creditRatingId}"/></p>
	<p><font>邮编：</font><input type="text" class="easyui-validatebox textBox" name="postCode" data-options="width:160,validType:['maxLength[16]'],height:31" value="${customer.postCode}" /></p>
	<p><font>注册资金：</font><input type="text" class="textBox" id="registedCapital" name="registedCapital" value="${customer.registedCapital}" /></p>
	<p><font>在业人数：</font><input type="text" class="easyui-numberbox textBox" data-options="width:160,validType:['accessoryNumber','maxLength[10]'],height:31" name="staffNum" value="${customer.staffNum}" /></p>
	<p><font>开户银行：</font><input type="text" class="textBox" name="bank" value="${customer.bank}" /></p>
	<p><font>帐号：</font><input type="text" class="easyui-textbox" name="account" data-options="width:160,validType:'maxLength[50]',height:31" value="${customer.account}" /></p>
	<p><font>国税号：</font><input type="text" class="textBox" name="nationTax" value="${customer.nationTax}" /></p>
	<p><font>地税号：</font><input type="text" class="textBox" name="landTax" value="${customer.landTax}" /></p>
	<p><font>成立日期：</font><input id="registerDate" type="text" class="dateBox" name="registerDate" value="${customer.registerDate}"/></p>
	<p><font>经营范围：</font><input type="text" class="textBox-mid" name="bussinessRange" value="${customer.bussinessRange}"  style='width:158px;'/></p>
	<p><font>地址：</font><input type="text" class="textBox-mid" name="address" value="${customer.address}"  style='width:158px;'/></p>
	<p><font>默认收货地址：</font><a href="javascript:;" id="customerAddress" class="btn-ora-introduce">设置地址</a></p>
	<p><font>备注：</font><textarea name="describe">${customer.describe}</textarea></p>
	<p class='btn-box' style="text-align: center;width: 100%;">
		<input class="btn-blue2 btn-xs" type="button" id="save" value="保存"/>
		<!-- <input type="reset" class="btn-gray btn-xs" value="重置"/> -->
	</p>
</form>
     
<div id="pop-win"></div>
<script>
var win;
var win2 = "";
var _Name = '${customer.name}';
$(function($){
	<c:choose>
	<c:when test="${isDetail}">
		$("#customer-form").fool('fromEnable',{'enable':false});
		$("#areaId").val('${customer.areaName}');
		$("#categoryId").val('${customer.categoryName}');
		$('#memberName').val('${customer.memberName}');
		$("#creditRatingId").val('${customer.creditRatingName}');
	</c:when>
	<c:otherwise>
	<c:if test="${isCopy}">
		var newCode = $("#code").children('#code');
		 	newCode.prevObject[0].value = "";
		var newName = $("#name").children('#name');
			newName.prevObject[0].value = "";
		var newShortName = $("#shortName").children('#shortName');
			newShortName.prevObject[0].value = "";
	</c:if>
		
		//$("#areaId").fool('combotree',{width:160,required:false,url:'${ctx}/basedata/findSubAuxiliaryAttrTree?code=001'});
		//$("#categoryId").fool('combotree',{width:160,required:false,url:'${ctx}/basedata/findSubAuxiliaryAttrTree?code=002'});
		//$('#memberName').fool('memberDeptComBoxTree',{required:false,'valTarget':$("#memberId"),'value':'${customer.memberName}'});
		//$('#memberName').combotree('setValue','${customer.memberName}');		
		//$("#creditRatingId").fool('combotree',{width:160,required:false,url:'${ctx}/basedata/findSubAuxiliaryAttrTree?code=003'});
	<c:if test="${isEdit}">//修改状态下
	$("#p1").css("display","inline-block");
	$('#customerAddress').click(function(){
		win2 = $("#pop-win").fool('window',{modal:true,'title':"默认收货地址设定",height:380,width:780,href:getRootPath()+'/customerSupplier/cawindow?csvId='+$('#fid').val()+"&csvType=1"});
	});
	</c:if>
	<c:if test="${param.flag ne 'edit'}">//新增状态下
    //改变按钮颜色
    $(".btn-ora-introduce").css({
        "border-color":"#bcbaba",
        "background-color":"#bcbaba"
    });
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
		$("#registerDate").datebox({editable:false,width:'160px',height:'31px'});
		$("#code").validatebox({
		    width:160,
			required:true,
			novalidate:true,
			validType:'normalChar',
		});
		$("#name").validatebox({width:160,required:true,novalidate:true,validType:'isBank'});
		$("#creditLineUser").validatebox({width:160,validType:['assetValue','numMaxLength[10]'],novalidate:true});
		$("#creditLineSys").validatebox({width:160,validType:'intOrFloat',novalidate:true});
		$("#fax").validatebox({width:160,validType:'fax',novalidate:true});
		$("#phone").validatebox({width:160,validType:'phone',novalidate:true});
		$("#email").validatebox({width:160,validType:'email',novalidate:true});
		$("#tel").validatebox({width:160,validType:'tel',novalidate:true});
		$("#principalPhone").validatebox({width:160,validType:'phone',novalidate:true});
		$("#registedCapital").numberbox({width:160,precision:2,validType:['assetValue','numMaxLength[10]'], height:31});

		//业务员/采购员
		$("#memberName").click(function(){
			win = $("#pop-win").fool('window',{'title':"选择",height:450,width:500,
				href:getRootPath()+'/member/window?okCallBack=selectMember&onDblClick=selectMember&singleSelect=true'});
		});//.validatebox({required:true,novalidate:true});
		
		$("#customer-form").fool('fromBlurVali');
		
		$("#save").click(function(){
			if(!$("#customer-form").fool('fromVali')){
				return false;
			}
			var flag = $('input[name="recordStatus"]:checked').val();
			var data = $("#customer-form").serializeJson();
			datas = $.extend(data,{recordStatus:flag});
			$.ajax({
				url:'${ctx}/customer/save',
				type:"post",
				'data':datas,
				cache:false,
				dataType:'json',
				success:function(data){
					dataDispose(data);
					if(data.returnCode=='0'){
						$.fool.alert({time:1000,msg:'操作成功!',fn:function(){
							if(dhxkey == 1){
		                        selectTab(dhxname,dhxtab);
		                    }
							$('#pop-window').window('close');
							refreshData();
						}});
					}else{
						$.fool.alert({time:2000,msg:'操作失败!['+data.message+']'});
					}
				}
			});
			return false;
		});
	</c:otherwise>
	</c:choose>
});

function getData(data){
	var _d = data;
	if(typeof data[0]!='undefined'){
		_d = data[0];
	}
	return _d;
}

//隐藏系统计算额度
if(!'${customer.code}'){
	$('.none').css('display','none');
}

//详细页。采购员文本框
function selectMember(data,other){
	var _d = getData(data);
	$("#memberId").val(_d.fid);
	$("#memberName").val(_d.username);
	//$("#memberName").validatebox('validate');
	win.window('close').window('clear');
}

</script>
</body>
</html>
