<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>货品管理</title>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/layout.css"/>
<link rel="stylesheet" href="${ctx}/resources/css/comm.css"/>
<script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<style>
html,body{ min-width:1024px}
#detail p{ display:inline-block; font-size:15px; padding:35px 0px 0px 60px}
#detail p input{ width:162px; border:1px solid #999;}
#detail p select{ width:162px; height:27px; border:1px solid #999}
#detail #save{ width:87px; height:26px; background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF}
#detail #reset{ width:87px; height:26px; background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px}
#detail form div{ margin:20px 0px 10px 30px}

#toolbar a{ margin:0px}
#img img{ width:115px; height:116px; }
#img div{display:inline-block; margin:5px auto}
#img p{ font-size:14px; margin:5px 0px 0px 0px}
.pagination a{text-decoration:none; border:solid 1px #999999; color:#999; margin:5px}
.pagination p{ color:#999; font-size:14px}
</style>
</head>
<body>
<div id="detail" style=" width:1000px; height:270px; border:1px solid #CCC; margin:20px auto">
 <form action="${ctx}/goodsController/save">

  <p>货品编号：</p><input type="text" class="easyui-validatebox textBox" data-options="required:true,validType:'normalChar',missingMessage:'货品编号不可为空!'" maxLength="50" name="code" />
  <p>货品名称：</p><input type="text" class="easyui-validatebox textBox" data-options="required:true,validType:'normalChar',missingMessage:'货品名称不可为空!'" maxLength="50" name="name"/>
  <p>类别：</p><input type="text" class="easyui-validatebox  textBox" data-options="required:true,validType:'normalChar',missingMessage:'货品类别不可为空!'" maxLength="50" name="category"/>
  <br/> 
  <p style="">规格型号：<input type="text" class="easyui-validatebox textBox" name="spec" data-options="validType:'normalChar'" maxLength="50" /></p>
  <p style=" margin-left:30px">单位：<input type="text" class="easyui-validatebox textBox" name="unit" data-options="required:true,validType:'normalChar',missingMessage:'货品单位不可为空!'" maxLength="20"/></p>
  <p style="margin-left:-4px">单价：<input type="text" class="easyui-validatebox textBox" name="unitPrice" data-options="required:true,validType:'intOrFloat',missingMessage:'货品单价不可为空!'" maxLength="20"/></p>
  <br/> 
  <p style="margin-left:30px">描述：<textarea name="describe" class="easyui-validatebox" data-options="validType:['minLength[6]','normalChar']" style="width:444px; height:70px; vertical-align: text-top; resize:none;border:1px solid #999;"></textarea></p>
  <br/>
  <input id="save" type="submit" value="保存"  style=" margin:15px 0px 0px 145px"/>
  <input id="reset" type="reset" value="重新填写"  style=" margin:15px 0px 0px 0px"/>

 </form>
</div>

<div id="toolbar" style="width:1000px; height:32px; border:1px solid #CCC;background-color:rgb(247,247,247);margin:20px auto 10px auto">
 <input type="button" id="byCategory" value="类别" style=" background-color:rgb(247,247,247); height:32px; width:77px; border:1px #CCCCCC; border-style:none solid none none;"/>
 <input type="button" id="byUnitPrice" value="价格" style=" background-color:rgb(247,247,247); height:32px;width:77px;border:1px #CCCCCC;border-style:none solid none none;"/>
 <input type="button" id="byInTime" value="录入时间" style=" background-color:rgb(247,247,247); height:32px;width:112px;border:1px #CCCCCC;border-style:none solid none none;"/>
 <input id="search" type="text" class="easyui-textbox" data-options="prompt:'请输入商品名或编号查询'"  name="keyword" style="color:#CCC; border:1px  solid; margin:auto 10px auto 30px"/>
 <input type="submit" id="searchBtn"  value="查询" style="background-color:rgb(239,214,147);border:1px solid #CCC; border-radius:5px; color:#FFF; margin-right:390px"/>
 <a href="javascript:;" id="showtable"><img src="" style="border:0px" /></a>
 <a href="javascript:;" id="showimg"><img src="" style="border:0px" /></a>
</div>

<div style="width:1000px; height:311px;margin:10px auto; position: relative">
 <div id="list" style="width:1000px; height:320px; border:1px solid #CCC;margin:10px auto">
 <table class="easyui-datagrid" id="goodslist" style="width:1000px; height:320px" data-options="url:'${ctx}/goodsController/list'">
  <thead>
   <th field="category" width="95" editor="text">类别</th>
   <th field="code" width="151" formatter="linkstyle" editor="text">货品编号</th>
   <th field="name" width="380" editor="text">货品名称</th>
   <th field="spec" width="115" editor="text">规格型号</th>
   <th field="unit" width="67" editor="text">单位</th>
   <th field="unitPrice" width="95" editor="text">单价</th>
   <th field="act" width="75" formatter="action">操作</th>
  </thead>
  
 </table>
</div>
 
 <div id="img" style="width:1000px; height:320px; border:1px solid #CCC;margin:10px auto; text-align:center; position:absolute; top:-10px; left:0px; display:none">
 </div>

</div>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/easyui-validate-extend.js?v=${js_v}"></script>

<script type="text/javascript">
 $(document).ready(function(){
	 $("#byCategory").click(function(e){
		 keyword=$('#search').val();
		 $('#goodslist').datagrid({
	        	url:"${ctx}/goodsController/list?selectFlag=0&keyword="+keyword,
	        });
	 });
	 
	 $("#byUnitPrice").click(function(e){
		 keyword=$('#search').val();
		 $('#goodslist').datagrid({
	        	url:"${ctx}/goodsController/list?selectFlag=1&keyword="+keyword,
	        });
	 });
	 
	 $("#byInTime").click(function(e){
		 keyword=$('#search').val();
		 $('#goodslist').datagrid({
	        	/* url:"${ctx}/goodsController/list?selectFlag=2&keyword="+keyword, */
	        });
	 });
	 
	 $("#searchBtn").click(function(e) {
		keyword=$('#search').val();
        $('#goodslist').datagrid({
        	url:"${ctx}/goodsController/list?keyword="+keyword,
        });
		$('#goodslist').datagrid('reload');
    });
	
	
	  /* $("#search").click(function(){
	    $("#search").val("");
		$("#search").css("color","#333");
	  });*/
	}); 
	 
	  $(document).ready(function(){
	  $("#showtable").click(function(){
	    $("#img").fadeOut();
		$("#list").fadeIn();
	  });
	});

	  $(document).ready(function(){
	  $("#showimg").click(function(){
	    $("#list").fadeOut();
		$("#img").fadeIn();
	  });
	});

	function action(value,row,index){
		if (row.editing){
			var s='<a href="javascript:;" onclick="saverow(this)">Save</a>';
			var c='<a href="javascript:;" onclick="cancelrow(this)">Cancel</a>';
			return s+"&nbsp;"+c;
			}
			else{
				var e='<a href="javascript:;" onclick="editrow(this)"><img src="${ctx}/resources/js/lib/easyui/themes/default/images/edit.png" style="border:0px;" /></a>';
				var d='<a href="javascript:;" onclick="deleterow(this)"><img src="${ctx}/resources/js/lib/easyui/themes/default/images/delete.png" style="border:0px" /></a>';
				return e+"&nbsp;&nbsp;"+d;
				}
		}
 	function writeObj(obj){ 
	    var description = ""; 
	    for(var i in obj){   
	        var property=obj[i];   
	        description+=i+" = "+property+"\n";  
	    }   
	    $.fool.alert({msg:description});
	};
	$('#goodslist').datagrid({
		        onBeforeEdit:function(index,row){
					row.editing = true;
					updateActions(index);
				},
				onAfterEdit:function(index,row){
					row.editing = false;
					updateActions(index);
					$.post("${ctx}/goodsController/save",{fid:row.fid,orgFid:row.orgFid,code:row.code,name:row.name,describe:row.describe,category:row.category,spec:row.spec,unit:row.unit,unitPrice:row.unitPrice});
				},
				onCancelEdit:function(index,row){
					row.editing = false;
					updateActions(index);
				}
		});
	 
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
			fn:function(b){
				if(b){
					var row=$('#goodslist').datagrid('getSelected');
					$('#goodslist').datagrid('deleteRow', getRowIndex(target));
					$.post("${ctx}/goodsController/delete",{fid:row.fid});
				}
			},
			msg:'确定删除?'
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
</script>




</body>
</html>