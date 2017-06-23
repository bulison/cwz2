<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>过账</title>

<style>
#btnBox{
  text-align: center;
}
.form p.hideOut,.form1 p.hideOut{
  display: none;
}
#detailBox{
  overflow: auto;
}
</style>
</head>
<body>
<div id="sdd" class="easyui-dialog" title="凭证过账" style="width:400px;height:200px;"   
        data-options="iconCls:'icon-save',closable:false,resizable:true,modal:true,buttons:[{
				text:'前进',
				handler:showdd1
			},{
				text:'取消',
				handler:ddclose
			}]">   
	<p> 本指南将根据本期输入或生成的记账凭证，逐笔登记各总分类账户及明细分类账户，以便查询账薄和输出报表</p>
	 <br/>
	 <br/>
	 <p>
	  注意：凭证一旦过账，就不能再修改，如果发现错误，只能采用补充凭证或红字冲销的方法更正。因此，
	 在过账之前，你一定要仔细检查输入的凭证是否完全正确无误。如果你现在想改变主意，可以按取消按钮
	 </p>
    
</div>  
<div id="edd" class="easyui-dialog" title="凭证过账" style="width:400px;height:200px;"   
        data-options="iconCls:'icon-save',closable:false,resizable:true,modal:true,buttons:[{
				text:'后退',
				handler:showdd
			},{
				text:'前进',
				handler:possing
			},{
				text:'取消',
				handler:dd1close
			}]"> 
	 关于凭证号的连续性，你可以选择<br/>
	 <br/>
	 <form id="form" action="">
	 <input type="radio"  name="postAccountFlag" checked="checked" value="0"/>当发现凭证号不连续时终止过账<br/>
     <input type="radio" name="postAccountFlag"  value="1"/>当发现凭证号不连续时给予警告   <br/>
     <input type="radio" name="postAccountFlag"  value="2"/>当发现凭证号不连续时允许过账
     </form>  
</div>
<script type="text/javascript">
$("#sdd").dialog();
$("#edd").dialog();
$("#sdd").dialog('open');
$("#edd").dialog('close');
function showdd1(){
	  $("#sdd").dialog('close');
	  $("#edd").dialog('open');
}
function showdd(){
	  $("#edd").dialog('close');
	  $("#sdd").dialog('open');
}
function ddclose(){
	$("#sdd").dialog('close');
	$("#postBox").window("close");
}
function dd1close(){
	$("#edd").dialog('close');
	$("#postBox").window("close");
}
function possing(){
	$.post(getRootPath()+'/voucher/postAccount',$('#form').serializeJson(), function(result) {
		dataDispose(result);
		var msg = "";
		if(result.message==null){
	       msg ="已成功过账"+result.data.totalSuccess+"条"; 
		}else{
		   if(result.data==null){
		     msg = result.message;
		   }else{
			   msg = result.message+"<br/>已成功过账" + result.data.totalSuccess+"条";
		   }
		}
		$('#voucherList').trigger("reloadGrid"); 
		$.fool.alert({msg:msg,fn:function(){
			$('#voucherList').resetSelection(); 
		}});
		$("#edd").dialog('close');
		$("#postBox").window("close");
		return true;
	});
}
</script>
</body>
</html>