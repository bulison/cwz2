(function($){
	$.fn.extend($.fool.methods,{
			/**
			 * 初始化现金流动类型
			 * @param valTarget 选择值赋予的元素(jQ对象)
			 * @param category 现金流类型ID
			 */
			cashRecordType:function(setting){
				var $t=$.fool._get$(this);
				if(typeof setting == 'undefined')return;
				var _url = getRootPath()+'/cashRecordTypeController/getIncomeExpendTree?category='+setting.category;
				var opts = $.extend({'valTarget':'#recordTypeId',url:_url},setting);
				return $t.fool('combotree',opts);
			}
		}
	);
})(jQuery);

/**
 * 弹出账户dialog
 * @param id
 */
function openAccountByOrgId(id) {
	$(id).click(
			function() {
				return regDialog(
						getRootPath()+'/accountController/goAccountList', '账户列表', 500, 500);
			});
}
/**
 * 现金流修改保存ajax
 */
function saveCashflow(fid , message){
	var isValid = $('#form').form({novalidate:false}).form('validate');
	if(!isValid){
		$.fool.alert({msg:'数据验证不通过,不能保存！'});
	}
	else{
		$.ajax({
			type:'post',
			url:getRootPath()+'/cashRecordController/save?fid='+ fid,
			cache:false ,
			data:$('form').serialize(),
			dataType:'json',
			success:function(data){
				if(data == 1){
					$.fool.alert({
						msg:message,
						fn:function(){
							location.href = getRootPath()+"/cashRecordController/listCashRecord";
						}
					});
				}
			}
		});
	}
}