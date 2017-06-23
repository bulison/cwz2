<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
<div class="form1">
<form id="form" action="" method="post">
			<p><font>资源编号：</font><input id="code" type="text" name="code" value="${code}" class="easyui-validatebox textBox ops-val" data-options="required:true,missingMessage:'该项不能为空',novalidate:true,validType:['maxLength[50]','isBank']"  /></p>
			
			<p><font>资源名称：</font><input id="resName" type="text" name="resName" value="${resName}" class="easyui-validatebox textBox ops-val" data-options="required:true,missingMessage:'该项不能为空',novalidate:true,validType:['maxLength[50]','isBank']" /></p>
			
			<p><font>资源应用环境:</font><select id="resApp"  name="resApp" class="easyui-combobox"  data-options="required:true,missingMessage:'该项不能为空',width:164,height:31,editable:false">                                                                               
                                                                               <option value="1">web</option>
                                                                               <option value="2">android</option>
                                                                               <option value="3">iphone</option>
                                                                               </select>
			</p>
			
			<p><font>资源类型：</font><select id="resType"  name="resType" class="easyui-combobox"  data-options="required:true,missingMessage:'该项不能为空',width:164,height:31,novalidate:true,value:'${resType}',editable:false">
                                                                               <option value="0">菜单目录</option>
                                                                               <option value="1">菜单项</option>
                                                                               <option value="2">URl</option>
                                                                               <option value="3">操作</option>
                                                                               </select>
			</p>
			<p><font>资源级别：</font><select id="permType"  name="permType" class="easyui-combobox"  data-options="required:true,missingMessage:'该项不能为空',width:164,height:31,novalidate:true,value:'${permType}',editable:false">
                                                                               <option value="0">普通资源</option>
                                                                               <option value="1">系统资源</option>
                                                                               </select>
			</p>
			<p><font>链接或JS函数：</font><input id="resString" type="text" name="resString" value="${resString}" class="easyui-validatebox textBox ops-val" data-options="validType:'maxLength[100]'" /></p>
			<p><font>资源图标：</font><input id="smallIcoPath" type="text" name="smallIcoPath" value="${smallIcoPath}" class="easyui-validatebox textBox ops-val" data-options="validType:'maxLength[50]'"  /></p>
			<p><font>排列序号：</font><input id="rankOrder" type="text" name="rankOrder" value="${rankOrder}" class="easyui-validatebox textBox ops-val" data-options="required:true,missingMessage:'该项不能为空',novalidate:true,validType:['maxLength[50]','isBank']" /></p>
			<p><font>是否显示：</font><select id="show"  name="show" class="easyui-combobox"  data-options="required:true,missingMessage:'该项不能为空',width:164,height:31,novalidate:true,value:'${show}',editable:false">
				<option value="0">否</option>
				<option value="1">是</option>
			</select>
			</p>
			<p><font>功能描述：</font><textarea class="easyui-validatebox textArea" name="resDesc" style="width:265px !important" data-options="validType:'maxLength[200]'">${resDesc}</textarea></p>
			<br/>
			<p style="margin-left:45px"><font><a href="javascript:;" id="save"  class="btn-blue4 btn-s">提交</a></font></p>
<input type="hidden" name="parent" value="${parent}" />
<input type="hidden" name="fid" value="${fid}" />
</form>
</div>
<!-- 
          <div style="text-align:center;">
          <form action="${ctx}/resourceController/saveOrUpdate" method="post">
            <p style=" display:inline-block; margin:20px 30px 0px 70px">资源编号：<input type="text" name="code" value="${code}" class="easyui-validatebox" data-options="required:true,missingMessage:'该项不能为空'" style="border-width:1px" /></p>
            <p style=" display:inline-block; margin:20px 30px 0px 70px">资源名称：<input type="text" name="resName" value="${resName}" class="easyui-validatebox" data-options="required:true,missingMessage:'该项不能为空'" style="border-width:1px" /></p>
            <p style=" display:inline-block; margin:20px 30px 0px 70px">资源类型：<select id="resType"  name="resType" class="easyui-combobox" style="border-width:1px" data-options="required:true,missingMessage:'该项不能为空'"  >
                                                                               <option value="0">菜单目录</option>
                                                                               <option value="1">菜单项</option>
                                                                               <option value="2">URl</option>
                                                                               <option value="3">操作</option>
                                                                               </select></p>
            <p style=" display:inline-block;margin:20px 30px 0px 46px">链接或JS函数：<input type="text" name="resString" value="${resString}" class="easyui-validatebox" data-options="required:true,missingMessage:'该项不能为空'" style="border-width:1px" /></p>
            <p style=" display:inline-block;margin:20px 30px 0px 70px">功能描述：<input type="text" name="resDesc" value="${resDesc}" class="easyui-validatebox" data-options="required:true,missingMessage:'该项不能为空'" style="border-width:1px" /></p>
            <input type="hidden" name="parent" value="${parent}" />
            <input type="hidden" name="fid" value="${fid}" />
            <input type="submit" value="提交" style="width:92px; height:25px;background-color:rgb(27,185,250);border:1px solid #999; border-radius:5px; color:#FFF;margin:20px 0px 10px 0px"/>
          </form>
          </div>
 -->
           
<script type="text/javascript">
  $(document).ready(function(e) {
	  $("#resType").val('${resType}');
  });
  $(function(){
     $(".ops-val").bind('blur', function(){
    	 $(this).validatebox('enableValidation').validatebox('validate');
     });
  });

  $('#save').click(function(e) {
	  $('#form').form('enableValidation');
	   if($('#form').form('validate')){
			$("#save").attr("disabled","disabled");
			$.post('${ctx}/resourceController/saveOrUpdate',$('#form').serialize(),function(data){
		    	if(data.returnCode=='0'){
		    		$.fool.alert({msg:'保存成功！',fn:function(){
		    			$('#addResource').window('close');
		    			$("#save").removeAttr("disabled");
		    			$('#resourceTree').tree('reload');
		    		}});
		    	}else if(data.returnCode=='1'){
		    		$.fool.alert({msg:data.message});
		    		$("#save").removeAttr("disabled");
		    	}else{
		    		$.fool.alert({msg:"系统正忙，请稍后再试"});
		    		$("#save").removeAttr("disabled");
		    	}
		    	return true;
		    });
		}else{
			return false;
		}
});
</script>
</body>
