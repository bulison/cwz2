<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div class="form1" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${entity.fid}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${entity.updateTime}"/>
                <input name="checkoutStatus" id="checkoutStatus" type="hidden" value="${entity.checkoutStatus}"/>
				<p><font><em>*</em>会计期间：</font><input id="period" type="text" class="easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,validType:'period'"/></p>
				<p><font><em>*</em>开始日期：</font><input id="startDate" type="text" class="textBox"/></p>
				<p><font><em>*</em>结束日期：</font><input id="endDate" type="text" class="easyui-datebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,width:167,height:31,editable:false" value="${entity.endDate}"/></p>
				<p><font>描述：</font><input id="description" type="text" class="easyui-validatebox textBox" value="${entity.description}"/></p><br/>
				<p style="margin-left:130px"><a href="javascript:;" id="save" class="btn-blue2 btn-s2">保存</a></p> 
              </form>
          </div>

<script type="text/javascript">
$(document).ready(function(){
	$("input").attr('autocomplete','off');
});
if(!"${entity.fid}"){
	if(!'${lastVo.period}'){
		$("#startDate").datebox({
			required:true,
			missingMessage:'该项不能为空！',
			novalidate:true,
			width:160,
			height:31,
			editable:false,
		});
	}else{
		$("#startDate").datebox({
			required:true,
			missingMessage:'该项不能为空！',
			novalidate:true,
			width:160,
			height:31,
			editable:false,
			disabled:true,
			value:'${lastVo.endDate}'
		});
		$("#period").val('${lastVo.period}');
	}
}else{
	$("#startDate").datebox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:167,
		height:31,
		editable:false,
		disabled:true,
		value:'${entity.startDate}'
	});
	$("#period").val('${entity.period}');
}

$('#save').click(function(e) {
	var fid=$("#fid").val();
	var period=$("#period").val();
	var startDate=$("#startDate").datebox("getValue");
	var endDate=$("#endDate").datebox("getValue");
	var updateTime=$("#updateTime").val();
	var description=$("#description").val();
	$('#form').form('enableValidation');
		if($('#form').form('validate')){ 
			$('#save').attr("disabled","disabled");
			    $.post('${ctx}/periodController/save',{fid:fid,period:period,startDate:startDate,endDate:endDate,description:description,updateTime:updateTime},function(data){
			    	if(data.returnCode=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			$('#save').removeAttr("disabled");
			    			location.href="${ctx}/periodController/periodManager";
			    		}});
			    	}else{
			    		$.fool.alert({msg:data.message,fn:function(){
			    			$('#save').removeAttr("disabled");
			    			$('#periodList').datagrid('reload');
			    		}});
			    	}
			    	return true;
			    });
			}else{
				return false;
				}
});
 </script>
</body>
</html>