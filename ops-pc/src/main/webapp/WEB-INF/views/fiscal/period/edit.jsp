<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div class="form1" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${obj.fid}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${obj.updateTime}"/>
                <input name="checkoutStatus" id="checkoutStatus" type="hidden" value="${obj.checkoutStatus}"/>
				<p><font><em>*</em>会计期间：</font><input id="period" name="period" type="text" class="textBox"/></p>
				<p><font><em>*</em>开始日期：</font><input id="startDate" name="startDate" type="text" class="textBox"/></p>
				<p><font><em>*</em>结束日期：</font><input id="endDate" name="endDate" type="text" class="textBox" value="${obj.endDate}"/></p>
				<p><font>描述：</font><input id="description" name="description" type="text" class="textBox" value="${obj.description}"/></p><br/>
				<p><font></font><a href="javascript:;" id="save" class="btn-blue2 btn-s2">保存</a></p> 
              </form>
          </div>

<script type="text/javascript">
$(document).ready(function(){
	//禁用文本框提示
	$("input").attr('autocomplete','off');
});

//表单控件初始化
$("#period").validatebox({
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	validType:["isBank","period"]
}); 
$("#endDate").datebox({
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	width:160,
	height:31,
	editable:false,
	validType:'endDate'
});
$("#description").validatebox({
	 validType:'maxLength[200]'
});

//判断新增、修改。判断是否为第一个，设置开始日期。
if(!"${obj.fid}"){
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
			value:'${lastVo.endDate}'
		});
		$("#period").val('${lastVo.period}');
	}
}else{
	$("#startDate").datebox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		width:160,
		height:31,
		editable:false,
		value:'${obj.startDate}'
	});
	$("#period").val('${obj.period}');
}

//保存按钮点击事件
$('#save').click(function(e) {
	$('#form').form('enableValidation');
	if($('#form').form('validate')){
		$('#save').attr("disabled","disabled");
		var startDate=$("#startDate").datebox('getValue');
		if(startDate!='${lastVo.endDate}'&&'${lastVo.endDate}'){
			var startDateStr=(startDate.split("-")).join("");
			var lastEndDateStr=('${lastVo.endDate}'.split("-")).join("");
			if(startDateStr<lastEndDateStr){
				$.fool.alert({msg:"会计期间时间重复",fn:function(){
					$('#save').removeAttr("disabled");
					$('#periodList').trigger("reloadGrid"); 
				}});
				return false;
			}
			$.fool.confirm({title:'确定',msg:'与上一个会计期间不连续，是否确认保存？',fn:function(r){
				if(r){
					$.post('${ctx}/fiscalPeriod/save',$('#form').serialize()/* +"&startDate="+startDate */,function(data){
						dataDispose(data);
						if(data.result=='0'){
							$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
								if(dhxkey == 1){
			                        selectTab(dhxname,dhxtab);
			                    }
								$('#save').removeAttr("disabled");
								$('#addBox').window('close');
								$('#periodList').trigger("reloadGrid"); 
							}});
						}else if(data.result=='1'){
							$.fool.alert({msg:data.msg,fn:function(){
								$('#save').removeAttr("disabled");
								$('#periodList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:"系统繁忙，请稍后再试。"});
							$("#save").removeAttr("disabled");
						}
						return true;
					});
				}else{
					$('#save').removeAttr("disabled");
					return false;
				}
			}});
		}else{
			$.post('${ctx}/fiscalPeriod/save',$('#form').serialize()/* +"&startDate="+startDate */,function(data){
				dataDispose(data);
				if(data.result=='0'){
					$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
						if(dhxkey == 1){
	                        selectTab(dhxname,dhxtab);
	                    }
						$('#save').removeAttr("disabled");
						$('#addBox').window('close');
						$('#periodList').trigger("reloadGrid"); 
					}});
				}else if(data.result=='1'){
					$.fool.alert({msg:data.msg,fn:function(){
						$('#save').removeAttr("disabled");
						$('#periodList').trigger("reloadGrid"); 
					}});
				}else{
					$.fool.alert({msg:"系统繁忙，请稍后再试。"});
					$("#save").removeAttr("disabled");
				}
				return true;
			});
		}
	}else{
		$("#save").removeAttr("disabled");
		return false;
	}
});

$.extend($.fn.validatebox.defaults.rules, {
	endDate:{
      validator: function (value, param) {
    	  var star=$("#startDate").datebox('getValue');
    	  var end=value;
    	  var startStr=star.slice(0,4)+star.slice(5,7)+star.slice(8,10);
    	  var endStr=end.slice(0,4)+end.slice(5,7)+end.slice(8,10);
       	return parseInt(startStr)<=parseInt(endStr);
       },
       message:'结束日期必须在开始日期后'

   },	
});
enterController('form');
 </script>
</body>
</html>