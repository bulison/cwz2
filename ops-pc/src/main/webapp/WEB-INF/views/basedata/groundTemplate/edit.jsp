<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增场地类型</title>
</head>
<body>
    <div class="form1">
        <form id="groundForm">
            <input id="updateTime" name="updateTime" type="hidden" value='<fmt:formatDate value="${vo.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
            <input id="id" name="id" type="hidden" value="${vo.id}"/>
            <p><font>描述：</font><input id="describe" class="textBox" name="describe" value="${vo.describe}"/></p>
            <div>
                <p><font></font><a href="javascript:;" id="save" class="btn-blue2 btn-s2">保存</a></p>
	        </div>
        </form>
    </div>	
<script type="text/javascript">
$("#describe").validatebox({
	validType:"maxLength[50]"
});
$('#save').click(function(){
	if($('#groundForm').form('validate')){
		$.ajax({
			type:'put',
			url:"${ctx}/api/groundTemplate/save",
			cache:false ,
			data:$("#groundForm").serializeJson(),
			dataType:'json',
			success:function(data){
				if(data.returnCode=='0'){
					$.fool.alert({time:1000,msg:'操作成功!',fn:function(){
						$('#groundTree').trigger("reloadGrid");
						mywin.window("close");
					}});
				}else{
					$.fool.alert({msg:'操作失败!['+data.message+']'});
				}
			}
		});
	}
});
</script>
</body>
</html>