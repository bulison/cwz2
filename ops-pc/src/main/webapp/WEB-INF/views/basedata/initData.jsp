<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>数据初始化</title>
</head>
<body>
     <div class="form1">
	     <form id="goodForm">
	         <p style="margin-left: 150px; margin-top: 50px;">
	         <input type="radio"  name="flag" checked="checked" value="1"/>单据初始化
	         <input type="radio" name="flag"  value="0"/>系统初始化
	         </p>
	         <br/>
	         <p style="margin-left:100px"><font><a href="javascript:;" id="save" class="btn-blue4 btn-s" >初始化</a></font><font><a id="save" class="btn-blue4 btn-s" >取消</a></font></p>  
	    </form>

    </div>	
	<!-- 分页信息 -->	
	<!--esayui验证扩展-->
	<script type="text/javascript">
	$(function(){
		
	});
	$('#save').click(function(){
		saveEventTpl();
	});
	function saveEventTpl(){
		var isValid = $('#goodForm').form({novalidate:false}).form('validate');
		if(!isValid){
			$.fool.alert({msg:'数据验证不通过,不能保存！'});
		}
		else{
			$.ajax({
				type:'post',
				url:getRootPath()+'/goods/save',
				cache:false ,
				data:$('#goodForm').serialize(),
				dataType:'json',
				success:function(data){
					var _data = $('#goodForm').serializeJson();
					if(data.returnCode=='0'){
						if(!$("#fid").val()){
							addNode(data.data.fid,_data);
						}else{	
							updateNode(_data);
						}
						fromEnable("#goodForm",false).form('clear').form({novalidate:true});
						$.fool.alert({msg:'操作成功!'});
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