/**
 * 
 */
/*$('#bankLessTable').datagrid();
setPager($('#bankLessTable'));*/ 

/*function myedit(value,row,index){
	var status = $("#bankStatus").val();
	var e = "<a href='javascript:;' onclick='editBankLess(\""+row.fid+"\")' title='编辑'>"+value+"</a>";
	var d = "<a href='javascript:;' onclick='viewBankLess(\""+row.fid+"\")' title='详情'>"+value+"</a>";
	if(status=="1"){
		return d;
	}
	return e;
}*/
$("#bankLessAdd").click(function(){
	addBankLess();
});
function addBankLess(){
    var type = $("#type").val();
    var _title = type==2?"添加银行未达单":"添加企业未达单";
	$("#bank-edit-window").fool('window',{modal:true,title:_title,width:$(window).width()-300,
		height:$(window).height()-340,href:getRootPath()+"/cashierBankBillController/edit?type="+type,
		onClose:function(){
			if(editFlag){
			   win.window("destroy");
			   $("#settlementTypeId").combotree("destroy");
			   $("#resume").combotree("destroy");
			}
			//$(this).window("destroy");
		}});
}
function viewBankLess(value){
	 var type = $("#type").val();
	 var _title = type==2?"银行未达单详情":"企业未达单详情";
	 $("#bank-edit-window").fool('window',{modal:true,title:_title,width:$(window).width()-300,
			height:$(window).height()-340,href:getRootPath()+"/cashierBankBillController/edit?view=1&type="+type+"&fid="+value,onClose:function(){
				if(editFlag){
					   win.window("destroy");
					   $("#settlementTypeId").combotree("destroy");
					   $("#resume").combotree("destroy");
				}
				//$(this).window("destroy");
	 }});
	 
}
function delBankLess(fid){
	var _bankInitId = $("#bankInitId").val();
	$.fool.confirm({msg:'确定要删除该记录吗？',fn:function(r){
		if(r){
			$.post(getRootPath()+'/cashierBankBillController/delete',{fid:fid,bankInitId:_bankInitId},function(data){
				if(data.returnCode =='0'){
		    		$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
		    			refreshBankData();
		    			return true;
		    		}});
		    	}else{
		    		$.fool.alert({msg:data.message});
		    		return false;
				}
		    });
		}
	}});
}
function editBankLess(value){
	var type = $("#type").val();
	var _title = type==2?"编辑银行未达单":"编辑企业未达单"
	$("#bank-edit-window").fool('window',{modal:true,title:_title,width:$(window).width()-300,
		height:$(window).height()-340,href:getRootPath()+"/cashierBankBillController/edit?type="+type+"&fid="+value,
		onClose:function(){
			if(editFlag){
			   win.window("destroy");
			   $("#settlementTypeId").combotree("destroy");
			   $("#resume").combotree("destroy");
			}
			//$(this).window("destroy");
			
		}});
}
function refreshBankData(){
	$("#bankLessTable").trigger("reloadGrid"); 
}
function bankWindowsClose(){
	$("#bank-edit-window").window("close");
}


