<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择地址</title>
 <link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/default/layout.css"/>
<link rel="stylesheet" href="${ctx}/resources/js/lib/easyui/themes/icon.css"/>
 <script type="text/javascript" src="${ctx}/resources/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/locale/easyui-lang-zh_CN.js"></script>

<style>
#province,#city,#district{ width:120px; height:20px}
</style>
</head>
<body>
  <form action="" method="get">
    <input class="easyui-combobox" name="province" id="province" data-options="prompt:'请选择省'"/>
    
   
    <input class="easyui-combobox" name="city" id="city" data-options="prompt:'请选择市'"/>
      
    
    <input class="easyui-combobox" name="district" id="district" data-options="prompt:'请选择区'"/>
      
    
  </form>
  
<script type="text/javascript">

$(function(){
	$('#province').combobox({
		url:'${ctx}/regionsController/selectProvince',
	    valueField:"id",
	    textField:"name",
	    onSelect:function(record){
	    	/* alert(record.id); */
	    	$('#city').combobox({
	    		url:'${ctx}/regionsController/selectCity?parentId='+record.id,
	    	    valueField:"id",
	    	    textField:"name",
	    	    onSelect:function(record){
	    	    	/* alert(record.id); */
	    	    	$('#district').combobox({
	    	    		url:'${ctx}/regionsController/selectDistrict?parentId='+record.id,
	    	    		valueField:"id",
	    	    	    textField:"name",
	    	    	    onSelect:function(record){
	    	    	    	/* alert(record.id); */
	    	    	    }
	    	    	    	    	    	
	    	    	});
	    	    },
				
	    	     onChange:function(){
	    			$('#district').combobox('clear');
	    			
		        } 
	    	});
	    },
	     onChange:function(){
			$('#city').combobox('clear');
			$('#district').combobox('clear');
		} 
	});
	
});




</script>

</body>
</html>