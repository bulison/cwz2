/**
 * 轧帐管理
 */
jQuery(function($){
	$("#sdd").dialog();
	sddclose();
	/*$('#dataTable').datagrid();*/
	//setPager($('#dataTable'));
});

function openCheckdate(){
   $("#sdd").dialog('open');
}
function sddclose(){
   $("#sdd").dialog('close');
}
$("#checkdate").click(function(){
	openCheckdate();
});
//轧帐
function checkdate(){
	if(!$("#checkdateForm").fool('fromVali')){
		return false;
	}	
	$.post(getRootPath()+'/cashierBankCheckdateController/save',$('#checkdateForm').serializeJson(), function(result) {
		dataDispose(result);
		if(result.result==0){
			$.fool.alert({time:1000,msg:"成功结账"});
			sddclose();
			$("#dataTable").trigger("reloadGrid"); 
		}else{
			$.fool.alert({time:1000,msg:result.message});
		}
		return true;
	});		
}
//反轧帐
$("#cancel").click(function(){
	$.fool.confirm({msg:'确定反结账吗？',fn:function(r){
		if(r){			
			$.post(getRootPath()+'/cashierBankCheckdateController/cancel',null, function(result) {
				dataDispose(result);
				if(result.result==0){
					$.fool.alert({time:1000,msg:"反结账成功"});
					$("#dataTable").trigger("reloadGrid");
				}else{
					$.fool.alert({time:1000,msg:result.message});
				}
				return true;
			});	
		}
  }});
})