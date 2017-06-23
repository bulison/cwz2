<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<title>我的日程</title>
</head>
<body>
  <form id="form">
    <p><font>事项标题：</font><input id="title" class="easyui-validatebox textBox" type="text" data-options="required:true,novalidate:true" value="${vo.title}" /></p>
    <p><font>背景颜色：</font><input  id="color" class="textBox" readonly="readonly" data-options="width:167,height:31" /></p>
    <input type='hidden' id='color-id' value='#87CEFF'/>
      <div class="options" style="display:none;position: absolute;left:430px;z-index:10;background-color: white;">
           <label id='#000000' class="option" style="background-color:#000000" >&emsp;&emsp;</label>
           <label id='#0000FF' class="option" style="background-color:#0000FF" >&emsp;&emsp;</label>
           <label id='#87CEEB' class="option" style="background-color:#87CEEB" >&emsp;&emsp;</label>
           <label id='#3CB371' class="option" style="background-color:#3CB371" >&emsp;&emsp;</label>
           <label id='#8B0000' class="option" style="background-color:#8B0000" >&emsp;&emsp;</label>
           <label id='#FF69B4' class="option" style="background-color:#FF69B4" >&emsp;&emsp;</label>
      </div>
    <br/>
    <p><font>开始时间：</font><input id="start" class="easyui-datetimebox textBox" type="text" data-options="editable:false,width:167,height:31,required:true,novalidate:true" value="${vo.start}"/></p>
    <p><font>结束时间：</font><input id="end" class="easyui-datetimebox textBox" type="text" data-options="editable:false,width:167,height:31" value="${vo.end}"/></p>
    <p><font>提醒时间：</font><input id="warn" class="easyui-datetimebox textBox" type="text" data-options="editable:false,width:167,height:31" value="${vo.remindTime}"/></p>
    <p><font>详细内容：</font><textarea id="detail" class="easyui-validatebox textBox"  style="width:455px; height:60px;vertical-align:text-top;vertical-align:-45px \9;resize:none;overflow: auto">${vo.describe}</textarea></p>
    <input type="hidden" id="fid" value="${vo.fid}"/>
    <br/>
    <p style="margin-left: 130px"><input type="button" id="save" value="保存" class="btn-blue4 btn-s" style="display:none;"/></p>
    <p style="margin-left: 10px"><input type="button" id="cancel" value="取消" class="btn-blue4 btn-s" style="display:none;"/></p>
    <p style="margin-left: -15px"><input type="button" id="update" value="修改" class="btn-blue4 btn-s"/></p> 
    <p style="margin-left: 10px"><input type="button" id="delete" value="删除" class="btn-gray btn-s"/></p>
  </form>
    
    <script type="text/javascript">
    $(function() { 
    	$('.textBox').attr("readonly",true);
    	
    	if('${vo.backgroundColor}'!=null&&'${vo.backgroundColor}'!=""){ 
    		$('#color').css('background-color','${vo.backgroundColor}');       
    	}
    });
    
      $('#update').click(function(){
    	  $('#save').fadeIn();
    	  $('#cancel').fadeIn();
    	  $('#update').fadeOut();
    	  $("#title").attr("readonly",false);
      	  $("#start").datetimebox('readonly', false);
      	  $("#end").datetimebox('readonly', false);
      	  $("#warn").datetimebox('readonly', false);
      	  $("#detail").attr("readonly",false);
      });
      $('#cancel').click(function(){
    	  $('#save').fadeOut();
    	  $('#cancel').fadeOut();
    	  $('#update').fadeIn();
    	  $("#title").attr("readonly",true);
      	  $("#start").datetimebox('readonly', true);
      	  $("#end").datetimebox('readonly', true);
      	  $("#warn").datetimebox('readonly', true);
      	  $("#detail").attr("readonly",true);
      });
      $('#save').click(function(){
  		  var fid=$('#fid').val();
    	  var title=$('#title').val();
  		  var start=$('#start').datetimebox('getValue');
  		  var end=$('#end').datetimebox('getValue');
  		  var warn=$('#warn').datetimebox('getValue');
  		  var detail=$('#detail').val();
  		  var color=$('#color-id').val();
  		  var createTime='${vo.createTime}'; 
  		  var createTimeStr=createTime.substring(0,4)+createTime.substring(5, 7)+createTime.substring(8, 10)+createTime.substring(11, 13)+createTime.substring(14, 16)+createTime.substring(17, 19);  
		  var startStr=start.substring(0,4)+start.substring(5, 7)+start.substring(8, 10)+start.substring(11, 13)+start.substring(14, 16)+start.substring(17, 19);
		  var endStr=end.substring(0,4)+end.substring(5, 7)+end.substring(8, 10)+end.substring(11, 13)+end.substring(14, 16)+end.substring(17, 19);
  		 
		  $('#title').validatebox('enableValidation').validatebox('validate');
		  $('#start').datetimebox('enableValidation').datetimebox('validate');
		  if($('#form').form('validate')){
			  if(createTimeStr<=startStr){
				  if(endStr!=''){
					  if(startStr<=endStr){
							 $.post('${ctx}/agendaController/save',{fid:fid,title:title,start:start,end:end,remindTime:warn, describe:detail, backgroundColor:color,},function(){
								 $.fool.alert({
									  msg:'修改成功！'
								  }); 
					    		  $('#add').window('close');
					    		  $('#calendar').fullCalendar('refetchEvents');
					    	  });
							  return true;
						}else{
							$.fool.alert({
								  msg:'结束时间必须在开始时间之后哟！'
							  });
							return false;
						}
				  }else{
					  $.post('${ctx}/agendaController/save',{fid:fid,title:title,start:start,end:end,remindTime:warn, describe:detail, backgroundColor:color,},function(){
							 $.fool.alert({
								  msg:'修改成功！'
							  }); 
				    		  $('#add').window('close');
				    		  $('#calendar').fullCalendar('refetchEvents');
				    	  });
						  return true;
				  }
				}else{
					$.fool.alert({
						  msg:'开始事件必须在当前事件之前哟！'
					  });
					return false;
					}
		  }else{
			  $.fool.alert({
				  msg:'验证失败！'
			  });
			  $('#calendar').fullCalendar('unselect');
			  return false;
		  }
      });
      $('#delete').click(function(){
    	  var fid=$('#fid').val();
    	  eventData = {
  				title: '${vo.title}',
  				};
    	  $.fool.confirm({
    		  msg:'确定删除？',
    		  fn:function(r){
    			  if(r){
    				  $.post('${ctx}/agendaController/delete',{fid:fid},function(){
    					  $.fool.alert({
    						  msg:'删除成功！'
    					  });
    		    		  $('#add').window('close');
    		    		  $('#calendar').fullCalendar('refetchEvents');
    		    	  });
    			  }
    		  }
    	  });
    	  
      });
      
      $('#color').click(function(){
      	$('.options').fadeIn();
      });
      $('.option').click(function(){
      	var color=$(this).attr('id');
      	$('#color-id').val(color);
      	$('#color').css('background-color',color);
      	$('.options').fadeOut('fast');
      });
    </script>
</body>
</html>