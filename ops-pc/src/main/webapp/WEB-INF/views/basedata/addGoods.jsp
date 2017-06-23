<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增货品</title>
</head>
<body>
     <div class="form1">
	     <form id="goodForm" style="text-align:left;">
	            <input id="fid" name="fid" type="hidden" value="${goods.fid}"/>
	            <input id="unitId" name="unitId" type="hidden" value="${goods.unitId}"/>
	     <c:choose>
				<c:when test="${empty goods}">
				<input id="parentId" name="parentId" type="hidden" value="${param.parentId}"/>
				</c:when>
				<c:otherwise>
				<input id="parentId" name="parentId" type="hidden" value="${goods.parentId}"/>
				</c:otherwise>
	     </c:choose>
	            <input id="goodsSpecId" name="goodsSpecId" type="hidden" value="${goods.goodsSpecId }">
	            <input id="useFlag" name="useFlag" value="${goods.useFlag}" type="hidden"/>
	            <input id="attrTypeId" name="categoryId" value="${goods.categoryId }" type="hidden"/>
	            <input id="goodFlag" value="${goods.flag}" type="hidden"/>
	            <input id="updateTime" name="updateTime" type="hidden" value="${goods.updateTime}" />
	           
		         <p><font>货品标识：</font>货品组</p>
	             <br/>
	                <p><font><label style="color: red">*</label>编号：</font><input type="text" id="code" name="code" class="easyui-validatebox textBox" validType="normalChar" required="true" missingMessage="货品组编码必填!" value="${goods.code}" /></p>
	                <p><font><label style="color: red">*</label>名称：</font><input type="text" class="easyui-validatebox textBox" id="name" name="name" validType="normalChar"  required="true"  missingMessage="货品名称不能为空!" value="${goods.name}" /></p>
	                <p><font><label style="color: red">*</label>是否有效：</font>
		            <c:choose>
	                  <c:when test="${goods.recordStatus == 'SAC'}">
		                <input type="radio"  name="recordStatus"  checked="checked"  value="SAC"/>有效
		                <input type="radio" name="recordStatus"  value="SNU"/>无效
		              </c:when>
		              <c:when test="${goods.recordStatus == 'SNU'}">
		                <input type="radio"  name="recordStatus" value="SAC"/>有效
		                <input type="radio" name="recordStatus" checked="checked" value="SNU"/>无效
		              </c:when>
		              <c:otherwise>
	                    <input type="radio"  name="recordStatus" checked="checked" value="SAC"/>有效
	                    <input type="radio"  name="recordStatus" value="SNU"/>无效
	                  </c:otherwise>
		            </c:choose>
	            </p>
	            <!-- <div id="goodsAttr" style="display: none;">
	                <p><font>规格：</font><input type="text" class="textBox"  name="spec"  value="${goods.spec}"/></p>
			        <p><font>条码：</font><input type="text" id="contacts" class="textBox" name="barCode"  value="${goods.barCode}"/></p>
			        <p><font>仓储时间（天）：</font><input id="storagePeriod" class="easyui-validatebox textBox" value="${goods.storagePeriod}" data-options="validType:'integer',missingMessage:'仓储时间不能其他类型'" name="storagePeriod" class="textBox" type="text"/></p>
		            <p><font>货品类别：</font><input id="goodsTypeTree"/></p>
		            <p><font>货品属性组：</font><input id="goodsSpcTree"/></p>
		            
		            
		            <p><font><label style="color: red">*</label>单位：</font><input id="unitTree"/></p>
		            <p><font>记账标识：</font>
		            <c:choose>
	                  <c:when test="${goods.accountFlag == '1'}">
		                <input type="radio"  name="accountFlag" checked="checked" value="1"/>计算库存
		                <input type="radio" name="accountFlag"  value="0"/>不计算库存
		              </c:when>
		              <c:otherwise>
	                    <input type="radio"  name="accountFlag" value="1"/>计算库存
	                    <input type="radio" name="accountFlag" checked="checked" value="0"/>不计算库存
	                  </c:otherwise>
		            </c:choose>
		            </p>
	            </div> -->
	         <!--    <br/> -->
	            <p><font>描述：</font><textarea name="describe" id="describe"  class="textArea">${goods.describe}</textarea></p>
	           	<div style="margin-top:10px;text-align:center;width:100%;">
	           		<a href="javascript:;" id="save" class="btn-blue2 btn-s2" style="margin-bottom:0px;">保存</a>
	           	</div>
	    </form>
       
    </div>	
	<!-- 分页信息 -->	
	<!--esayui验证扩展-->
	<script type="text/javascript">
	$(function(){
	   //$("#goodsAttr").append($("#attr").clone().html());
	   $("#code").validatebox({required:true});
	   $("#name").validatebox({required:true});
	   $("#category").validatebox({required:true});
	   /* if("${goods.flag}"=='1'){
		   $(":input[name='flag']").validatebox({required:true});
		   $(":input[name='accountFlag']").validatebox({required:true});
		   //$("#storagePeriod").validatebox({required:true});
		   //$("#goodsAttr").show();
		   $("#goodsSpcTree").combotree({disabled:false});
	 		$("#unitTree").combotree({disabled:false});
	 		$("#goodsTypeTree").combotree({disabled:false}); 
	   }else{ */
		   //$(":input[name='flag']").validatebox({required:false});
		   //$(":input[name='accountFlag']").validatebox({required:false});
		   //$("#storagePeriod").validatebox({required:false});
		   //$("#goodsAttr").hide();
	   //}
	   loadData();
		/* $('input[name="flag"]').click(function(){
			var flag = $('input[name="flag"]:checked').val();
			 if(flag == "1"){
				$(":input[name='flag']").validatebox({required:true});
				$(":input[name='accountFlag']").validatebox({required:true});
				$("#goodsSpcTree").combotree('clear');
		 		$("#unitTree").combotree('clear');
		 		$("#goodsTypeTree").combotree('clear'); 
				$("#goodsAttr").show();
			}else{ 
				$(":input[name='flag']").validatebox({required:false});
				$(":input[name='accountFlag']").validatebox({required:false});
				//$("#storagePeriod").validatebox({required:false});
				//$("#goodsAttr").hide();
			//}
		}); */
	});
	function loadData(){
		$('#unitTree').combotree({
			width:160,
			height:31,
			url:'${ctx}/unitController/getTree',
			valueField:'fid',textField:'fname',editable:true,required:false,
			onLoadSuccess:function(node,data){
					var roots=data[0].children;
					var node="";
					var removeNode=new Array();
					for(var i=0;i<roots.length;i++){
						if(roots[i].attributes.detail.flag==0 && roots[i].children==""){
							removeNode.push(roots[i].id);
						}
					}
					for(var i=0;i<removeNode.length;i++){
						node=$('#unitTree').combotree("tree").tree('find',removeNode[i]);
						$('#unitTree').combotree("tree").tree('remove',node.target);
					}
					if(data[0].id!=""){
						var node=$("#unitTree").combotree('tree').tree("find",data[0].id);
						$("#unitTree").combotree('tree').tree('update',{
							target: node.target,
							text: '请选择',
							id:""
						});
					}
				$('#unitTree').combotree("setText" , "${goods.unitName}");
			},
			onSelect:function(rec){
				    if(rec.children==''){
				    	$('#unitId').val(rec.id);
				    }else{
				    	$("#unitTree").combotree("clear");
				    	$.fool.alert({time:2000,msg:'你选择的是单位组，请选择单位组下的单位。'});
				    }
			} 
		}); 
	   $('#goodsTypeTree').fool('combotree',{
			url:'${ctx}/basedata/findSubAuxiliaryAttrTree?code=004',
			valueField:'fid',textField:'fname',editable:true,required:false,width:160,
			onLoadSuccess:function(node,data){
				$('#goodsTypeTree').combobox("setText" , "${goods.categoryName}");
			},
			onBeforeSelect:function(rec){
				if(rec.children == ''){
				    $("#attrTypeId").val(rec.id);
				}else{
					$.fool.alert({msg:'请选择类别!'});
					return false;
				}
			} 
		});
		$('#goodsSpcTree').fool('combotree',{
			url:'${ctx}/goodsspec/getSpecGroups',
			valueField:'fid',textField:'fname',editable:true,required:false,width:160,
			onLoadSuccess:function(node,data){
				$('#goodsSpcTree').combobox("setText" , "${goods.goodsSpecName}");
			},
			onBeforeSelect:function(rec){
				    if(rec.children == ''){
				    	$('#goodsSpecId').val(rec.id);
				    }else{
				    	$.fool.alert({msg:'请选择属性!'});
				    	return false;
				    }
			} 
		});
	}
	$('#save').click(function(){
		saveEventTpl();
	});
	function saveEventTpl(){
		var isValid = $('#goodForm').form().form('validate');
		var flag = 0;
		/* if(flag=="1"){
			var text = $('#unitTree').combotree("getText");
			if(text==""&&text=="请选择"){
				isValid=false;
			}
		} */
		if(!isValid){
			$.fool.alert({msg:'数据验证不通过,不能保存！'});
		}
		else{
			var datas;
			/* if(flag=="0"){ */
				var id = $("#fid").val();
				var code = $("#code").val();
				var updateTime = $("#updateTime").val();
				var describe = $("#describe").val();
				var recordStatus = $("input[name='recordStatus']:checked").val();
				var parentId = $("#parentId").val();
				var name = $("#name").val();
				datas = $.extend({parentId:parentId},{name:name},{flag:flag},{fid:id},{code:code},{updateTime:updateTime},{describe:describe},{recordStatus:recordStatus});
			/* }else{
				var data = $('#goodForm').serializeJson();
				datas = $.extend(data,{flag:flag});
			} */
			$.ajax({
				type:'post',
				url:getRootPath()+'/goods/save',
				cache:false ,
				data:datas,
				dataType:'json',
				success:function(data){
					var _data = $('#goodForm').serializeJson();
					if(data.returnCode=='0'){
						$('#goodsTree').tree('reload');
						fromEnable("#goodForm",false).form({novalidate:true});
						$("#goodsSpcTree").combotree({disabled:true});
				 		$("#unitTree").combotree({disabled:true});
				 		$("#goodsTypeTree").combotree({disabled:true});
						$.fool.alert({time:1000,msg:'操作成功!',fn:function(){
							$(".panel-tool-close").click();
							$('#goodsTree').tree('reload');
							$('#content').datagrid('reload');
						}});
						//$("#goodsTree").tree('select', node.target);
					}else{
						$.fool.alert({msg:'操作失败!['+data.message+']'});
					}
				}
			});
		}
    }
	</script>
</body>
</html>