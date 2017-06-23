<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
<div class="form">
<form id="form" action="" method="post"> 
 				<input type="hidden" id="my_fid" value="${obj.fid}"/>           
				<p><font>角色名称：</font><input type="text" id="roleName" name="roleName" value="${obj.roleName}" class="easyui-validatebox textBox ops-val" data-options="required:true,missingMessage:'该项不能为空',novalidate:true,validType:'normalChar'" /></p>
				<p><font>是否有效：</font><input id="validation" name="validation"/></p> 
				<p><font>描述：</font><textarea class="easyui-validatebox textArea" validType="length[2,200]" name="roleDesc" id="roleDesc"  style="width:180px !important;" >${obj.roleDesc}</textarea></p>
				<p style="margin-left:60px"><font><a href="javascript:;" id="save" class="btn-blue4 btn-s">提交</a></font></p>
</form>
</div>
<!--esayui验证扩展-->
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/easyui-validate-extend.js?v=${js_v}"></script>

  <script type="text/javascript">
  var valiCombo = $("#validation").fool("dhxCombo",{
	  width:162,
	  height:32,
	  novalidate:true,
	  required:true,
	  value:"${empty obj.validation?1:obj.validation}",
	  //missingMessage:'该项不能为空！',
	  editable:false,
	  data:[{
		  value:0,text:"否"
	  },{
		  value:1,text:"是"
	  }],
  	  focusShow:true
  });
  valiCombo.disable();
          $(function(){       	  
        		 $('#form').form({
        			 success:function(){        				
        				 location.reload(true);
        			 }
        		 });
        		 $(".ops-val").bind('blur', function(){
     				$(this).validatebox('enableValidation').validatebox('validate');
     				});
        	});
        	 $('#save').click(function(e) {
        		    $("#roleName").validatebox('enableValidation').validatebox('validate'); 
        			if($('#form').form('validate')){
        			      var jsonuserinfo = $('#form').serializeJson();
        				   $.post('${ctx}/roleController/saveOrUpdate?fid='+'${obj.fid}',jsonuserinfo,function(data){
        				    	if(data.returnCode == '0'){
        				    		$.fool.alert({msg:'保存成功！',fn:function(){
        				    			$('#addRole').window('close');
        				    			$('#datagrid').trigger('reloadGrid');
        				    		}});
        				    	}else{
        				    		$.fool.alert({msg:'保存失败'});
        			    		}
        				    }); 
        				}
        		});
          </script>          
</body>
