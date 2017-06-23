<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>


<script type="text/javascript">
$(function(){
	$.extend($.fn.validatebox.defaults.rules, {
		intOrFloat: {// 验证整数或小数
	        validator: function (value) {
	            return /^\d+(\.\d+)?$/i.test(value);
	        },
	        message: '请输入数字，并确保格式正确'
	    }
	});
	$('#unit').fool('combobox',{url:'${ctx}/unitController/query',valueField:'fid',textField:'name'});
	
	$('#code').validatebox({
		required:true,
		novalidate:true,
		missingMessage:'该项不能为空！'
	});
	$('#name').validatebox({
		required:true,
		novalidate:true,
		missingMessage:'该项不能为空！'
	});
	$('#category').validatebox({
		required:true,
		novalidate:true,
		missingMessage:'该项不能为空！'
	});
	$('#spec').validatebox({
		required:true,
		novalidate:true,
		missingMessage:'该项不能为空！'
	});
	$('#unit').combobox({
		required:true,
		novalidate:true,
		missingMessage:'该项不能为空！', 
		onSelect:function(record){
			$('#unitId').val(record.fid);
		},
		onLoadSuccess:function(){
			$('#unit').combobox('setValue',$('#unitId').val());
			$('.combo-panel').append('<fool:tagOpt optCode="ops_sys_unit"><div class="combobox-item"><a href="javascript:;" href="${ctx}/unitController/unitManage">管理类型单位</a></div></fool:tagOpt>');
			
		}
	});
	$('#unitPrice').validatebox({
		required:true,
		validType:'intOrFloat',
		novalidate:true,
		missingMessage:'该项不能为空！'
	});
	
	$("#code").attr('readonly','readonly');
	$("#name").attr('readonly','readonly');
	$("#category").attr('readonly','readonly');
	$("#spec").attr('readonly','readonly');
	$("#unit").combobox('disable'); 
	$("#unitPrice").attr('readonly','readonly');
	$("#describe").attr('readonly','readonly');
	$("#code").css('background-color','rgb(209,217,224)');
	$("#name").css('background-color','rgb(209,217,224)');
	$("#category").css('background-color','rgb(209,217,224)');
	$("#spec").css('background-color','rgb(209,217,224)');
	$(".textbox-text").css('background-color','rgb(209,217,224)');
	$("#unitPrice").css('background-color','rgb(209,217,224)');
	$("#describe").css('background-color','rgb(209,217,224)');
	$('#save').click(function(e) {
     $("#code").validatebox('enableValidation').validatebox('validate');
	 $("#name").validatebox('enableValidation').validatebox('validate');
	 $("#category").validatebox('enableValidation').validatebox('validate');
	 $("#spec").validatebox('enableValidation').validatebox('validate');
	 $("#unit").combobox('enableValidation').combobox('validate');
	 $("#unitPrice").validatebox('enableValidation').validatebox('validate');
	 $('#form').form('submit');
	 if($('#form').form('validate')){
		 location.reload(true);
			return true;
		}else{return false;}
    });
	
	
	$("#cancel").click(function(e) {
        $('#save').fadeOut();
		$('#cancel').fadeOut();
		$("#code").attr('readonly','readonly');
		$("#name").attr('readonly','readonly');
		$("#category").attr('readonly','readonly');
		$("#spec").attr('readonly','readonly');
		$("#unit").combobox('enable');
		$("#unitPrice").attr('readonly','readonly');
		$("#describe").attr('readonly','readonly');
		$("#code").css('background-color','rgb(209,217,224)');
	    $("#name").css('background-color','rgb(209,217,224)');
	    $("#category").css('background-color','rgb(209,217,224)');
	    $("#spec").css('background-color','rgb(209,217,224)');
	    $(".textbox-text").css('background-color','rgb(209,217,224)');
	    $("#unitPrice").css('background-color','rgb(209,217,224)');
	    $("#describe").css('background-color','rgb(209,217,224)');
    });
});
</script>

<div class="form">
          <form action="${ctx}/goodsController/save" id='form'>
            <input type="hidden" class="easyui-validatebox" name="fid" id="fid" />
            <p><font>货品编号：</font><input type="text" class="easyui-validatebox textBox" name="code" id="code" validType="normalChar" value='${goodsVos.code}' /></p>
            <p><font>货品名称：</font><input type="text" class="easyui-validatebox textBox" name="name" id="name" validType="normalChar"  value='${goodsVos.name}'/></p>
            <p><font>类别：</font><input type="text" class="easyui-validatebox textBox" name="category" id="category" validType="normalChar"  value='${goodsVos.category}'/></p>
            <p><font>规格型号：</font><input type="text" class="easyui-validatebox textBox" name="spec" id="spec" validType="normalChar"  value='${goodsVos.spec}'/></p>
            <p><font>单位：</font><input type="text" class="easyui-combobox textBox" id="unit" validType="normalChar"  data-options=""/></p>
            <p><font>单价：</font><input type="text" class="easyui-validatebox textBox" name="unitPrice" id="unitPrice" data-options="required:true,validType:'intOrFloat',missingMessage:'价格不能为空!'" value='${goodsVos.unitPrice}'/></p>
            <p><font>描述：</font><textarea name="describe" class="textArea easyui-validatebox"  style="width:444px; height:70px;" data-options="validType:['normalChar','minLength[6]']" id="describe">${goodsVos.describe}</textarea></p>
            <input type="hidden" class="easyui-validatebox" name="parentId" id="parentId"  style=" width:1000px;"/>
            <input type="hidden" name="unitId" id="unitId"  style=" width:1000px;" value="${goodsVos.unitId}"/>
            <p style="margin-left:108px"><font>  
            <a id="save"  href="javascript:;" class="btn-blue4 btn-s" style="display:none;">保存</a>
            <a id="cancel" href="javascript:;" class="btn-gray btn-s" style="display:none;">取消</a>
            </font></p>
          </form>
</div>
</body>
