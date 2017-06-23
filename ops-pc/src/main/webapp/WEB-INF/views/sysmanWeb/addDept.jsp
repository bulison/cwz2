<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>

</head>
<body>
          <div id='list-user' ></div>
          <div class="form" >
              <form id="form" action=""  method="post">
                <input name="parent" id="parent" type="hidden" value="${parent}"/>            
                <input name="fid" id="fid" type="hidden" value="${fid}"/>
                <p style="margin-left:40px"><font>部门编码：</font><input name="orgCode" id="orgCode" type="text" class=" easyui-validatebox textBox ops-val" data-options="required:true,validType:['normalChar','length[0,50]'],missingMessage:'该项不能为空！',novalidate:true" value="${orgCode}"/></p>
				<p style="margin-left:40px"><font>部门名称：</font><input name="orgName" id="orgName" type="text" class=" easyui-validatebox textBox ops-val" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,validType:['normalChar','length[0,50]']" value="${orgName}"/></p>
				<p style="margin-left:40px"><font>部门电话：</font><input name="phoneOne" id="phoneOne" type="text" class=" easyui-validatebox textBox ops-val" data-options="required:true,validType:'tel',invalidMessage:'电话格式不正确！',missingMessage:'该项不能为空！',novalidate:true" value="${telPhone}"/></p>
				<p style="margin-left:100px"><font><a href="javascript:;" id="save" class="btn-blue4 btn-s">提交</a></font></p>
          	<!--
            <p style=" display:inline-block; margin:20px 30px 0px 70px">部门名称：<input name="orgName" id="orgName" type="text" class="easyui-validatebox" data-options="required:true,missingMessage:'该项不能为空！'" style="border-width:1px" value="${orgName}"/></p>
            <p style=" display:inline-block;margin:20px 30px 0px 70px">部门电话：<input name="phoneOne" id="phoneOne" type="text" class="easyui-validatebox" data-options="required:true,missingMessage:'该项不能为空！'" style="border-width:1px" value="${phone}"/></p>
            <input name="parent" id="parent" type="hidden" value="${parent}"/>            
            <input name="fid" id="fid" type="hidden" value="${fid}"/>
            <input type="submit" value="提交" style="width:92px; height:25px;background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF;margin-top:20px"/>
            --> 
              </form>
          </div>
          	<!--esayui验证扩展-->
	<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/easyui-validate-extend.js?v=${js_v}"></script>
	
 <script type="text/javascript">
 $(function(){
	 $(".ops-val").bind('blur', function(){
			$(this).validatebox('enableValidation').validatebox('validate');
			});
});
 $('#save').click(function(e) {
	    $("#orgName").validatebox('enableValidation').validatebox('validate');
	    $('#phoneOne').validatebox('enableValidation').validatebox('validate');
	    $('#orgCode').validatebox('enableValidation').validatebox('validate');
	    
		if($('#form').form('validate')){
			$("#save").attr("disabled","disabled");
			$.post('${ctx}/orgController/addDept',$('#form').serialize(),function(data){
				dataDispose(data);
		    	if(data.result=='0'){
		    		$.fool.alert({msg:'保存成功！',fn:function(){
		    			if(dhxkey == 1){
	                        selectTab(dhxname,dhxtab);
	                    }
		    			$('#addDept').window('close');
		    			$("#save").removeAttr("disabled");
		    			$('#deptTree').tree('reload');
		    			//$('#deptTree').tree('reload');
		    		}});
		    	}else if(data.result=='1'){
		    		$.fool.alert({msg:data.msg});
		    		$("#save").removeAttr("disabled");
		    	}else {
		    		$.fool.alert({msg:"系统繁忙，请稍后再试。"});
		    		$("#save").removeAttr("disabled");
		    	}
		    	return true;
		    });
		}else{
			return false;
		}
 });
//更新节点
 function updateNode(data){
	 var node = getSelectNode();
	 $('#deptTree').tree('update',{
         target: node.target,
         text:data.orgName,
         attributes:data
	 });
	 $(node.target).click();
 }
 
 //增加节点
 function addNode(fid,data){
	 var node = getSelectNode();
	 var nodes = [{
         id:fid,
         text:data.orgName,
         attributes:data
     }];
     $('#deptTree').tree('append', {
         parent:node.target,
         data:nodes
     });
     $("#deptTree").tree('expand',node.target); 
 }
//获取选中节点
 function getSelectNode(){
 	var node = $('#deptTree').tree('getSelected');
 	if(!node){
 		$.fool.alert({msg:'请选择部门'});
 		return;
 	}
 	return node;
 }
 </script>
</body>
</html>