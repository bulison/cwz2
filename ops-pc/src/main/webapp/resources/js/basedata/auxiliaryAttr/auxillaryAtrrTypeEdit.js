/**
 * 
 */
$("#code").validatebox({required:true,novalidate:true});
$("#name").validatebox({required:true,novalidate:true});
$("#save").click(function(){
	if(!$("#auxiliaryAttrTypeForm").fool('fromVali')){
		return false;
	}
	var fdata = $("#auxiliaryAttrTypeForm").serializeJson();
	$.post(getRootPath()+'/basedata/auxiliarytype/save',fdata,function(data){
		dataDispose(data);
		if(data.result =='0'){
			$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
				$('#dataTable').datagrid('reload');
				$("#my-window").window('close');
				return true;
			}});
		}else{
			$.fool.alert({msg:data.msg});
			return false;
		}
	});
});