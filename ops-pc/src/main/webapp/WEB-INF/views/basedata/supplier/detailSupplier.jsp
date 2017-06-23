<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style>
</style>
</head>
  <body>
  <div class="form1">
  <form id="form" action="" method="post">
            <input type="hidden" id="fid" name="fid" value="${vo.fid}" />
  	
  	<p><font>编号：</font><input type="text" class="textBox" id="code" name="code" value="${vo.code}"/></p> 
  	<p><font>公司名称：</font><input type="text" class="textBox" id="name" name="name" value="${vo.name}"/></p>
  	<p><font>公司简称：</font><input type="text" class="textBox" maxLength="100" id="shortName" name="shortName" value="${vo.shortName}"/></p>
  	<p><font>地区：</font><input type="text" class="textBox" id="areaName" name="areaName" value="${vo.areaName}"/><input type="hidden" class="textBox" id="" name="areaId" value="${vo.areaId}"/></p>
  	<p><font>类别：</font><input type="text" class="textBox" id="categoryName" name="categoryName" value="${vo.categoryName}"/><input type="hidden" class="textBox" name="categoryId" value="${vo.categoryId}"/></p>
  	<p><font>用户配置信用额度：</font><input type="text" class="textBox" id="creditLineUser" name="creditLineUser" value="${vo.creditLineUser}"/></p>
  	<p><font>系统计算信用额度：</font><input type="text" class="textBox" id="creditLineSys" name="creditLineSys" value="${vo.creditLineSys}"/></p>
  	<p><font>业务联系人：</font><input type="text" class="textBox" id="businessContact" name="businessContact" value="${vo.businessContact}"/></p>
  	<p><font>业务联系人传真：</font><input type="text" class="textBox" id="fax" name="fax" value="${vo.fax}"/></p>
 	<p><font>业务联系人手机：</font><input type="text" class="textBox" id="phone" name="phone" value="${vo.phone}"/></p>
 	<p><font>邮箱：</font><input type="text" class="textBox" id="email" name="email" value="${vo.email}"/></p>
 	<%-- <p><font>公司电话：</font><input type="text" class="textBox" id="areaCode" style="width:40px"/> - <input type="text" class="textBox" id="tel" style="width:60px"/> - <input type="text" id="extension" class="textBox" style="width:40px"/><input type="hidden" name="tel" id="telNum" value="${vo.tel}"/></p> --%> 
 	
 	<p><font>公司电话：</font><input type="text" class="textBox" id="tel" name="tel" value="${vo.tel}"/></p>
 	
 	<p><font>采购员：</font><input type="text" class="textBox" id="memberName" name="memberName" value="${vo.memberName}"/></p>
 	<p><font>法人代表：</font><input type="text" class="textBox" id="principal" name="principal" value="${vo.principal}"/></p> 
 	<p><font>法人联系电话：</font><input type="text" class="textBox" id="principalPhone" name="principalPhone" value="${vo.principalPhone}"/></p>
 	<p><font>征信级别：</font><input type="text" class="textBox" id="creditRatingName" name="creditRatingName" value="${vo.creditRatingName}"/><input type="hidden" class="textBox" name="creditRatingId" value="${vo.creditRatingId}"/></p>
 	<p><font>邮编：</font><input type="text" class="textBox" id="postCode" name="postCode" value="${vo.postCode}"/></p>
 	<p><font>注册资金：</font><input type="text" class="textBox" id="registedCapital" name="registedCapital" value="${vo.registedCapital}"/></p>
 	<p><font>在业人数：</font><input type="text" class="textBox" id="staffNum" name="staffNum" value="${vo.staffNum}"/></p>
 	<p><font>开户银行：</font><input type="text" class="textBox" id="bank" name="bank" value="${vo.bank}"/></p>
 	<p><font>账户：</font><input type="text" class="textBox" id="account" name="account" value="${vo.account}"/></p>
 	<p><font>国税号：</font><input type="text" class="textBox" id="nationTax" name="nationTax" value="${vo.nationTax}"/></p>
 	<p><font>地税号：</font><input type="text" class="textBox" id="landTax" name="landTax" value="${vo.landTax}"/></p>
 	<p><font>成立日期：</font><input type="text" class="textBox" id="registerDate" name="registerDate" value="<fmt:formatDate value="${vo.registerDate}" pattern="yyyy-MM-dd"/>"/></p>
	<p><font>经营范围：</font><input type="text" class="textBox" id="bussinessRange"  name="bussinessRange" style="width:160px; height:25px;" value="${vo.bussinessRange}"/></p>
	<p><font>地址：</font><input type="text" class="textBox" id="address" name="address" style="width:160px; height:25px;" value="${vo.address}"/></p>
	<p><font>描述：</font><textarea  class="easyui-validatebox" id="describe" name="describe" style="width:276px;height:73px;resize:none; overflow:auto; vertical-align: text-top ;border-color:#CCC">${vo.describe}</textarea></p>		
  </form>
  </div>
  <script type="text/javascript">
    $("#form").fool('fromEnable',{'enable':false});;
    
    /* var str='${vo.tel}'.split("-");
    $('#areaCode').val(str[0]);
    $('#tel').val(str[1]);
    $('#extension').val(str[2]); */
  </script>
 </body>
</html>
