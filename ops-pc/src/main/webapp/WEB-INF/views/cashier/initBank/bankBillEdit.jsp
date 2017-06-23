<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
</head>
<body>
<style>
.form1 .textBox,.form .easyui-validatebox{
	width:160px;
	height:30px;
}
</style>
  <form class="form1" id="bankLessForm">
    <input id="fid" name="fid" type="hidden" value="${bankBillVo.fid}"/>
    <input id="bankInitId" type="hidden" value="${bankInit.fid}"/>
    <input id="memberId" name="memberId" type="hidden" value="${bankBillVo.memberId}"/>
    <input id="settlementType" name="settlementType" type="hidden" value="${bankBillVo.settlementTypeId}"/>
    <input id="resumeText" type="hidden" value="${bankBillVo.resume}"/>
    <input id="view" value="${view}" type="hidden" name="view"/>
    <p><font><em>*</em>银行借方：</font><input id="debit" value="${empty bankBillVo.debit?'0':bankBillVo.debit}" name="debit" class="textBox"/></p>
    <p><font><em>*</em>银行贷方：</font><input id="credit" value="${empty bankBillVo.credit?'0':bankBillVo.credit}" name="credit" class="textBox"/></p>
    <c:choose>
    <c:when test="${bankBillVo.type == 2}">
    <p><font><em>*</em>业务日期：</font><input id="settlementDate" data-options="required:true" value="${bankBillVo.settlementDate}" name="settlementDate"/></p>
    </c:when>
    </c:choose>
    <c:choose>
    <c:when test="${bankBillVo.type == 1}">
    <p><font><em>*</em>单据日期：</font><input id="voucherDate" data-options="required:true" value="${bankBillVo.voucherDate}" name="voucherDate"/></p>
    </c:when>
    </c:choose>
    <p><font>摘要：</font><input id="resume"  name="resume" class="textBox"/></p>
    <p><font>当天顺序号：</font><input id="orderno" name="orderno" value="${bankBillVo.orderno}" data-options="validType:'accessoryNumber'" class="easyui-validatebox"/></p>
    <p><font>结算方式：</font><input id="settlementTypeId" name="settlementTypeId" class="textBox"/></p>
    <p><font>结算号：</font><input id="settlementNo" name="settlementNo" value="${bankBillVo.settlementNo}" data-options="validType:'normalChar'" class="easyui-validatebox"/></p>
    <c:choose>
    <c:when test="${bankBillVo.type == 2}">
    <p><font><em>*</em>单据日期：</font><input id="voucherDate" data-options="required:true" value="${bankBillVo.voucherDate}" name="voucherDate"/></p>
    <p><font><em>*</em>经手人：</font><input id="memberName" data-options="required:true" value="${bankBillVo.memberName}" name="memberName" class="easyui-validatebox"/></p>
    </c:when>
    </c:choose>
    <p><font>制单人：</font><input id="creatorName" value="${bankBillVo.creatorName}" name="creatorName" readonly="readonly" class="textBox"/></p>
    <br/>
    <p><font></font><input type="button" id="bankSave" class="btn-blue2 btn-xs" value="保存" /></p>
  </form>
  <div id="pop-win"></div>
<script type="text/javascript" src="${ctx}/resources/js/cashier/initBank/bankBillEdit.js?v=${js_v}"></script>
<script type="text/javascript">
  initManage();
  enterController("bankLessForm");
</script>
</body>
</html>