<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
</head>
<body>
	<fool:reportTagOpt reportId="${reportId}" optCode="reportSearch"><a href="javascript:;" id="search" class="btn-blue4 btn-s" >查询</a></fool:reportTagOpt><a href="javascript:;" id="clear" class="btn-blue4 btn-s" >清空</a><fool:reportTagOpt reportId="${reportId}" optCode="reportExport"><a href="javascript:;" id="export" class="btn-blue4 btn-s" >导出</a></fool:reportTagOpt>
<script>
	var planId = "${param.planId}";
    var goodsSpecId ="";
    var goodsId ="";
    var deliveryPlace ="";
    var receiptPlace ="";
    var code=($("#resultType").next())[0].comboObj.getSelectedValue();
    function getParam() {
		goodsSpecId =($('.type7').next())[0].comboObj.getSelectedValue();
		goodsId =($('.type6').next())[0].comboObj.getSelectedValue();
		deliveryPlace =$('#FRECEIPT_PLACE[order="3"]').combotree('getValue');
        receiptPlace =$('#FRECEIPT_PLACE[order="4"]').combotree('getValue');
	}
$("#search").click(function(){

	$('#filterGrid').form("enableValidation");
	if(!$('#filterGrid').form("validate")){
		return false;
	}
	var conditions=getConditions();
	if(code){
		if($("#flag").prop('checked')){
		    if(code=="402881285ada557f015adb7cbeb10000")
			{
                getParam();
                $("#resultGrid").load("${ctx}/flow/plan/fee/list",{planId:planId,deliveryPlace:deliveryPlace,goodsId:goodsId,receiptPlace:receiptPlace,goodsSpecId:goodsSpecId},function(){
                });
			}else{
                $("#resultGrid").load("${ctx}/report/query",{sysReportId:code,condition:JSON.stringify(conditions)},function(){
                });
            }
		}else{
            if(code=="402881285ada557f015adb7cbeb10000")
            {
                getParam();
                $("#resultGrid").load("${ctx}/flow/plan/fee/list",{planId:planId,deliveryPlace:deliveryPlace,goodsId:goodsId,receiptPlace:receiptPlace,goodsSpecId:goodsSpecId});
            }else{
                $("#resultGrid").load("${ctx}/report/query",{flag:0,sysReportId:code,condition:JSON.stringify(conditions)},function(){
                    $('#pageaction').hide();
                });
            }
		}
	}else{
		$.fool.alert({msg:'请选择汇总方式'});
		return false;
	}
});
$("#export").click(function(){
	var conditions=getConditions();
	var code=($("#resultType").next())[0].comboObj.getSelectedValue();
	$('#sysReportId').val(code);
	$('#conditions').val(JSON.stringify(conditions));
	if(code){
        if(code=="402881285ada557f015adb7cbeb10000")
        {
            getParam();
            $("#export").attr('href','${ctx}/flow/plan/fee/export?planId='+planId+'&deliveryPlace='+deliveryPlace+'&goodsId='+goodsId+'&receiptPlace='+receiptPlace+'&goodsSpecId='+goodsSpecId)
        }else{
            $("#submitForm").submit();
        }
	}else{
		$.fool.alert({msg:'请选择汇总方式'});
		return false;
	}
});
$("#clear").click(function(){
    $('#goodSpecId').css('display','inline-block');
    $('#goodSpecId').val("");
    $('#dhxDiv_goodSpecId').css('display','none');

    if(code=="402881285ada557f015adb7cbeb10000")
    {
        $("#filterGrid").form("clear");
        ($('#goodsId').next())[0].comboObj.setComboText("");
        return;
	}
	/* $('#filterGrid').html(''); */
	$("#filterGrid").form("clear");
	$(".type23").attr("value","");
	var inputs=$("#filterGrid").find(".dhxDiv");
	var periodData=getComboData("${ctx}/fiscalPeriod/getAll?defaultSelect=1");
	for(var i=0;i<inputs.length;i++){
		if($(inputs[i]).prev().attr("inputtype")==22){
			if(periodData.length>0){
				for(var j=0;j<periodData.length;j++){
					if(periodData[j].text.checkoutStatus==1){
						inputs[i].comboObj.setComboValue(periodData[j].text.fid);
						break;
					}
				}
			}
		}else{
			inputs[i].comboObj.setComboText("");
		}
	}
	var periodStr="";
	var periodEnd="";
	$.ajax({
		url:"${ctx}/fiscalPeriod/getFristUnCheckPeriod",
		async:false,
		success:function(data){
			periodStr=data.startDate;
			periodEnd=data.endDate;
		}
	});
	var dateboxs=$("#filterGrid").find(".type9");
	if(dateboxs.length>0){
		for(var i=0;i<dateboxs.length;i++){ 
			if($(dateboxs[i]).attr("mark")=="PERIODSTR"){
				$(dateboxs[i]).datebox("setValue",periodStr);
			}else if($(dateboxs[i]).attr("mark")=="PERIODEND"){
				$(dateboxs[i]).datebox("setValue",periodEnd);
			}
		}
	}
	$('#filterGrid').form("validate");
});

</script>
</body>
</html>