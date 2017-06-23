<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增供应商</title>
</head>
<body>
<style>
.form,.form1{padding:10px 0px;}
.form1 p{margin:10px 0px 0px 0px;}
</style>
<div class="form1" style="margin-bottom:10px" id="detail">
 <form id="form" action="${ctx}/supplier/save" method="post">
     <input name="fid" id="fid" type="hidden" value="${taskTemplate.fid}"/>
     <%-- <input name="taskTypeId" id="taskTypeId" type="hidden" value="${entity.taskTypeId}"/>
     <input name="taskLevelId" id="taskLevelId" type="hidden" value="${entity.taskLevelId}"/> --%>
     <input name="updateTime" id="updateTime" type="hidden" value="${taskTemplate.updateTime}"/> <!-- 修改时间 --> 
     <input name="createTime" id="createTime" type="hidden" value="${taskTemplate.createTime}"/>  
	<p><font><em>*</em>编号：</font><input type="text" id="code" name="code" value="${taskTemplate.code}"/></p> 
  	<p><font><em>*</em>模版名称：</font><input type="text" id="name" name="name" value="${taskTemplate.name}"/></p>
  	<p><font><em>*</em>事件类别：</font><input type="text" id="taskTypeId" name="taskTypeId" /></p>
  	<p><font><em>*</em>事件级别：</font><input type="text"  id="taskLevelId" name="taskLevelId" style="width:160"/></p>
  	<p><font><em>*</em>预计完成天数：</font><input type="text" id="endDays" name="endDays" value="${taskTemplate.endDays}" /></p> 	
	<p><font>描述：</font><input type="text" id="describe" name="describe" value="${taskTemplate.describe}" /></p> 	
<br/>
 	<p style="text-align: center;width:100%;">
 	<input type="button" id="addsave" class="btn-blue2 btn-xs" value='保存' />
	</p>
</form>
</div>
<script type="text/javascript">
$('#code').textbox({
	 width:160,
	 height:32,
	 required:true,
	 novalidate:true,
	 validType:'length[1,50]',
	 missingMessage:'该项不能为空！',
});

$('#name').textbox({
	 width:160,
	 height:32,
	 required:true,
	 novalidate:true,
	 validType:'length[1,100]',
	 missingMessage:'该项不能为空！',
});
$('#endDays').numberbox({
	 width:160,
	 height:32,
	 required:true,
	 novalidate:true,
	 validType:'length[1,50]',
	 missingMessage:'该项不能为空！',
});

$('#describe').textbox({
	 width:160,
	 height:32,
	 novalidate:true,
	 validType:'length[1,200]',
});

//事件模版保存
$('#addsave').click(function(){
	$('#form').form('enableValidation');
	var options = $("#form").serializeJson();	
	if($('#form').form('validate')){
	$.ajax({
		   type: "post",
		   url: "${ctx}/flow/taskTemplate/save",
		   data:options,
		   success: function(data){
			   dataDispose(data);
		     if(data.returnCode == '0'){
		    	 if(dhxkey == 1){
                     selectTab(dhxname,dhxtab);
                 }
		    	 $.fool.alert({time:1000,msg:'保存成功'});
		    	 $('#dataTable').trigger('reloadGrid');
		    	 $('#win').window('close');  
		     }else if(data.returnCode == '1'){
		    	 $.fool.alert({msg:data.message}); 
		     }
		   },error:function(){
				$.fool.alert({msg:"系统正忙，请稍后再试。"});
			}
		});
	};
});

 /* $("#taskTypeId").combobox({//事件类型
	valueField: 'fid',    
	textField: 'name',
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	url:"${ctx}/flow/taskType/queryAll",
	width:160,
	height:31,
	editable:false,
	onLoadSuccess:function(){
		  $("#taskTypeId").combobox('setValue','${taskTemplate.taskTypeId}');  
	}
}); */

//事件类型
var etaskTypeIdValue='';	
	$.ajax({
		url:"${ctx}/flow/taskType/queryAll?num="+Math.random(),
		async:false,		
		success:function(data){		  	
			etaskTypeIdValue=formatData(data,'fid');		  	
	    }
		});
	var educationName= $("#taskTypeId").fool("dhxCombo",{
		  width:160,
		  height:32,
		  data:etaskTypeIdValue,
		  setTemplate:{
			  input:"#name#",
			  option:"#name#"
		  },
		toolsBar:{
            name:"事件类别",
            addUrl:"/flow/taskType/manage",
            refresh:true
        },
        focusShow:true,
		  editable:false,
		  clearOpt:false,
		  required:true,
		  novalidate:true,
		  onLoadSuccess:function(combo){
			combo.setComboValue('${taskTemplate.taskTypeId}');  
		}
	});

/* $("#taskLevelId").combobox({//事件级别
	valueField: 'fid',    
    textField: 'name',
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	url:"${ctx}/flow/taskLevel/queryAll",
	width:160,
	height:31,
	editable:false,
	onLoadSuccess:function(){
		 $("#taskLevelId").combobox('setValue','${taskTemplate.taskLevelId}'); 
	}
});  */

var taskLeveValue='';	//事件级别
$.ajax({
	url:"${ctx}/flow/taskLevel/queryAll?num="+Math.random(),
	async:false,		
	success:function(data){		  	
		taskLeveValue=formatData(data,'fid');		  	
    }
	});
var taskLeveName= $("#taskLevelId").fool("dhxCombo",{
	  width:160,
	  height:32,
	  data:taskLeveValue,
	  setTemplate:{
		  input:"#name#",
		  option:"#name#"
	  },
    toolsBar:{
        name:"事件级别",
        addUrl:"/flow/taskLevel/manage",
        refresh:true
    },
    focusShow:true,
	  editable:false,
	  clearOpt:false,
	  required:true,
	  novalidate:true,
	  onLoadSuccess:function(combo){
		  combo.setComboValue('${taskTemplate.taskLevelId}');  
	}			
});
 $('#dhxDiv_taskLevelId').mouseover(function(e){//鼠标已过去弹出提示框
	 tooltip(this)
});
 function tooltip(e){
		$(e).tooltip({
		    position: 'right',
		    content: '<span style="color:#fff">级别用数字表示，数字越大级别越高。<br/>计划和事件中的事件级别用来和消息预<br/>警的任务级别进行比较，任务级别大于<br/>事件级别时，向相关人发送相应消息。</span>',
		    onShow: function(){
				$(e).tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
		    }
		});
	}
</script>
</body>
</html>