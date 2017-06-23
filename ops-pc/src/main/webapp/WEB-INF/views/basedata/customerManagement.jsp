<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户管理</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<style>
html,body{ min-width:1024px}
#detail p{ display:inline-block; margin-right:45px; font-size:15px;padding:24px 0px 0px 60px}
#detail p input{ width:151px; border-width:1px;}
#detail #save{ width:87px; height:26px; background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF}
#detail #reset{ width:87px; height:26px; background-color:#CCC;border:1px solid #999; border-radius:5px}
#detail form div{ margin:12px 0px 10px 25px}
.pagination a{text-decoration:none; border:solid 1px #999999; color:#999; margin:5px}
.pagination p{ color:#999; font-size:14px}
</style>
</head>
<body>
<div id="detail" style=" width:1000px; height:270px; border:1px solid #CCC; margin:20px auto">
 <form id="form" action="${ctx}/customerController/save" method="post">
 
  <p>客户编号：<input type="text" class="easyui-validatebox" id="code" name="code" data-options="required:true,novalidate:true,validType:'normalChar',missingMessage:'该项不能为空！'"/></p>
  <p>客户名称：<input type="text" class="easyui-validatebox" id="name" name="name" data-options="required:true,novalidate:true,validType:'normalChar',missingMessage:'该项不能为空！'"/></p>
  <p>类别：<input type="text" class="easyui-validatebox" id="category" name="category" data-options="required:true,novalidate:true,validType:'normalChar',missingMessage:'该项不能为空！'"/></p>
  <br/>
  <p style=" margin-left:15px">联系人：<input type="text" id="contacts" class="easyui-validatebox" name="contacts" data-options="required:true,novalidate:true,validType:'normalChar',missingMessage:'该项不能为空！'"/></p>
  <p style="margin-left:30px">手机：<input type="text" id="phone" class="easyui-validatebox" name="phone" data-options="required:true,novalidate:true,validType:'phone',missingMessage:'该项不能为空！'"/></p>
  <p style="margin-left:-30px">固定电话：<input type="text" id="tel" class="easyui-validatebox" name="tel" data-options="required:true,validType:'tel',novalidate:true,missingMessage:'该项不能为空！'"/></p>
  <br/>
  <p style="margin-left:30px">传真：<input type="text" id="fax"  class="easyui-validatebox" name="fax" data-options="required:true,novalidate:true,validType:['normalChar','fax'],missingMessage:'该项不能为空！'"/></p>
  <p style="margin-left:30px">邮箱：<input type="text" id="email"  class="easyui-validatebox" name="email" data-options="required:true,novalidate:true,validType:'email',missingMessage:'该项不能为空！'"/></p>
  <p style="margin-left:0px; ">地址：<input type="text" id="address" class="easyui-validatebox"  name="address" data-options="required:true,novalidate:true,validType:['minLength[6]','normalChar'],missingMessage:'该项不能为空！'"/></p>
  <br/>
  <p style="margin-left:30px; margin-right:0px">描述：<input name="describe" id="describe" class="easyui-validatebox" data-options="required:true,novalidate:true,validType:['minLength[6]','normalChar'],missingMessage:'该项不能为空！'" style=" width:444px; height:60px; vertical-align: text-top;"/></p>
  <br/>
  <input id="save" type="submit" value="保存"   style=" margin:15px 0px 0px 145px"/>
  <input id="reset" type="reset" value="重新填写" style=" margin:15px 0px 0px 0px" />
 </form>
</div>

<div id="toolbar" style="width:1000px; height:32px; border:1px solid #CCC;background-color:rgb(247,247,247);margin:20px auto 10px auto">
 <input type="button" id="byCategory" value="类别" style=" background-color:rgb(247,247,247); height:32px; width:77px; border:1px #CCCCCC; border-style:none solid none none;"/>
 <input type="button" id="byAddress" value="地区" style=" background-color:rgb(247,247,247); height:32px;width:77px;border:1px #CCCCCC;border-style:none solid none none;"/>
 
 <input id="search" type="text" class="easyui-textbox" data-options="prompt:'输入客户名或编号查询',validType:'normalChar'" style="color:#CCC; border:1px  solid; margin:auto 10px auto 30px"/>
 <input type="button" id="searchBtn"  value="查询" style="background-color:rgb(239,214,147);border:1px solid #CCC; border-radius:5px; color:#FFF; margin-right:370px;width:50px;height:20px"/>

</div>

<div style="width:1000px; height:311px;margin:10px auto; position: relative">
<div id="list" style="width:1000px; height:320px; border:1px solid #CCC;margin:10px auto">
 <table class="easyui-datagrid" id="goodslist" style="width:1000px; height:320px" data-options="url:'${ctx}/customerController/list'">
  <thead>
   <th field="category" width="100" editor="text">类别</th>
   <th field="code" width="100" formatter="linkstyle" editor="text">客户编号</th>
   <th field="name" width="90" editor="text">客户名称</th>
   <th field="contacts" width="90" editor="text">联系人</th>
   <th field="phone" width="100" editor="text">手机</th>
   <th field="tel" width="100" editor="text">固定电话</th>
   <th field="fax" width="100" editor="text">传真</th>
   <th field="email" width="100" editor="text">邮箱</th>
   <th field="address" width="120" editor="text">地址</th>
   <th field="act" width="80" formatter="action">操作</th>
  </thead>
 </table>
</div>

</div>

<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">

 $(document).ready(function(){
	 $("#byCategory").click(function(e){
		 keyword=$('#search').val();
		 $('#goodslist').datagrid({
	        	url:"${ctx}/customerController/list?selectFlag=0&keyword="+keyword,
	        });
	 });
	 
	 $("#byAddress").click(function(e){
		 keyword=$('#search').val();
		 $('#goodslist').datagrid({
	        	url:"${ctx}/customerController/list?selectFlag=1&keyword="+keyword,
	        });
	 });
	 
	 
	 $("#searchBtn").click(function(e) {
			keyword=$('#search').val();
	        $('#goodslist').datagrid({
	        	url:"${ctx}/customerController/list?keyword="+keyword,
	        });
			$('#goodslist').datagrid('reload');
	    });

});

function action(value,row,index){
	if (row.editing){
		var s='<a href="javascript:;"  onclick="saverow(this)"><img title="保存" src="${ctx}/resources/js/lib/easyui/themes/default/images/save.png" style="border:0px" /></a>';
		var c='<a href="javascript:;"  onclick="cancelrow(this)"><img title="取消" src="${ctx}/resources/js/lib/easyui/themes/default/images/cancel.png" style="border:0px" /></a>';
		return s+"&nbsp;"+c;
		}
		else{
			var e='<a href="javascript:;" onclick="editrow(this)"><img title="修改" src="${ctx}/resources/js/lib/easyui/themes/default/images/edit.png" style="border:0px" /></a>';
			var d='<a href="javascript:;" onclick="deleterow(this)"><img title="删除" src="${ctx}/resources/js/lib/easyui/themes/default/images/delete.png" style="border:0px" /></a>';
			return e+"&nbsp;"+d;
			}
	}
	
$('#goodslist').datagrid({
	        onBeforeEdit:function(index,row){
				row.editing = true;
				updateActions(index);
			},
			onAfterEdit:function(index,row){
				row.editing = false;
				updateActions(index);
				$.post("${ctx}/customerController/save",{fid:row.fid,orgFid:row.orgFid,code:row.code,name:row.name,describe:row.describe,category:row.category,contacts:row.contacts,phone:row.phone,tel:row.tel,fax:row.fax,email:row.email,address:row.address});
			},
			onCancelEdit:function(index,row){
				row.editing = false;
				updateActions(index);
			}
	})
	
function updateActions(index){
		$('#goodslist').datagrid('updateRow',{
			index: index,
			row:{}
		});
	}
	
function getRowIndex(target){
			var tr = $(target).closest('tr.datagrid-row');
			return parseInt(tr.attr('datagrid-row-index'));
		}
function editrow(target){
			$('#goodslist').datagrid('beginEdit', getRowIndex(target));
		}
function deleterow(target){
	$.fool.confirm({
		msg:'确定删除',
		fn:function(b){
			if (b){
				var row=$('#goodslist').datagrid('getSelected');
				$('#goodslist').datagrid('deleteRow', getRowIndex(target));
				$.post("${ctx}/customerController/delete",{fid:row.fid});
			}
		}
	});
}
function saverow(target){
			$('#goodslist').datagrid('endEdit', getRowIndex(target));
		}
function cancelrow(target){
			$('#goodslist').datagrid('cancelEdit', getRowIndex(target));
		}

function linkstyle(val,row){
	return '<a href="javascript:;" style="text-decoration:none;color:rgb(53,159,247)">'+val+'</a>';
}

$('#code').bind('blur', function(){
				$('#code').validatebox('enableValidation').validatebox('validate');
});
$('#name').bind('blur', function(){
				$('#name').validatebox('enableValidation').validatebox('validate');
});
$('#category').bind('blur', function(){
				$('#category').validatebox('enableValidation').validatebox('validate');
});
$('#contacts').bind('blur', function(){
				$('#contacts').validatebox('enableValidation').validatebox('validate');
});
$('#phone').bind('blur', function(){
				$('#phone').validatebox('enableValidation').validatebox('validate');
});
$('#tel').bind('blur', function(){
				$('#tel').validatebox('enableValidation').validatebox('validate');
});
$('#fax').bind('blur', function(){
				$('#fax').validatebox('enableValidation').validatebox('validate');
});
$('#email').bind('blur', function(){
				$('#email').validatebox('enableValidation').validatebox('validate');
});
$('#address').bind('blur', function(){
				$('#address').validatebox('enableValidation').validatebox('validate');
});
$('#describe').bind('blur', function(){
				$('#describe').validatebox('enableValidation').validatebox('validate');
});
			
$('#save').click(function(e) {
     $("#code").validatebox('enableValidation').validatebox('validate');
	 $("#name").validatebox('enableValidation').validatebox('validate');
	 $("#category").validatebox('enableValidation').validatebox('validate');
	 $("#contacts").validatebox('enableValidation').validatebox('validate');
	 $("#phone").validatebox('enableValidation').validatebox('validate');
	 $("#tel").validatebox('enableValidation').validatebox('validate');
	 $("#fax").validatebox('enableValidation').validatebox('validate');
	 $("#email").validatebox('enableValidation').validatebox('validate');
	 $("#address").validatebox('enableValidation').validatebox('validate');
	 $("#describe").validatebox('enableValidation').validatebox('validate');
	 
	 if($('#form').form('validate')){
			return true;
		}else{return false;}
});

</script>

</body>
</html>