<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>提醒设置</title>
<style>
.panel-title {
  text-align: left;
  font-size: 14px;
  line-height: 25px;
}
.accordion .panel-body {
  background-color:transparent;
}
.boxs{padding:0;margin:10px 20px 10px 0px;width:170px;height:130px;border:2px solid #5DAEE5;border-radius:5px;display: inline-block;vertical-align: text-bottom;color: black;text-align:center;}
.boxs label{display:inline-block;margin-top:20px}
.boxs .num{width:30px}
.boxs .text{width:80px;}
.addboxs{border:2px solid #CCCCCC;background-position:-1px -1px;background-image:url('${ctx}/resources/images/addBox.png')}
.checkbox{vertical-align:-8px;margin-left:25px}
.save, .delete {padding:0;margin:10px;width:45px;height:18px;} 
</style>
</head>
<body>
<div class="nav-box">
		<ul style="">
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">基础管理</a></li>
	        <li><a href="javascript:;" class="curr">提醒/预警设置</a></li>
	    </ul>
</div>

<div class="warp-box">	
	<div id="settingPanel" class="easyui-accordion" data-options="multiple:true,selected:-1">
	  <fool:tagOpt optCode="notifOpen">
	  <div data-options="collapsible:false" title='<span>开启延迟通知<img class="checkbox" onclick="checkbox(this)" src="${ctx}/resources/images/check_false.png"></span>'>
	    <div class="boxContainer" id="boxContainer1">
	      <div class="boxs warning_even">
	        <label>延迟：</label><input class="easyui-validatebox text days" data-options="required:true,novalidate:true" /><label>天</label>
	        <br/>
	        <label class="notic">通知：</label><input type="text" id="default1" class="text"/>
	        <br/>
	        <a href="javascript:;" class="btn-green3 save">保存</a>
	        <a href="javascript:;" class="btn-green3 delete">删除</a>
	        <input class="defaultUID1 userFid" type="hidden"/>
	        <input class="fid" type="hidden"/>
	      </div>
	      <div class="boxs addboxs" id="addBox1"></div>
	    </div>
	  </div>
	  </fool:tagOpt>
	  <fool:tagOpt optCode="notifAlarm">
	  <div data-options="collapsible:false" title='<span>收益率警报<img class="checkbox" onclick="checkbox(this)" src="${ctx}/resources/images/check_false.png" ></span>'>
	    <div class="boxContainer" id="boxContainer2">
	      <div class="boxs warning_rate">
	        <label>介于：</label><input class="easyui-validatebox num sPoint" data-options="required:true,novalidate:true" type="text" /><label>% - </label>
	        <input class="easyui-validatebox num ePoint" data-options="required:true,novalidate:true" type="text" /><label>%</label>
	        <br/>
	        <label class="notic">通知：</label><input type="text" id="default2" class="text"/>
	        <br/>
	        <a href="javascript:;" class="btn-green3 save">保存</a>
	        <a href="javascript:;" class="btn-green3 delete">删除</a>
	        <input class="defaultUID2 userFid" type="hidden"/>
	        <input class="fid" type="hidden"/>
	      </div>
	      <div class="boxs addboxs" id="addBox2" >
	      </div>
	    </div>
	  </div>   
	  </fool:tagOpt>   
	</div>
</div>


<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
var box1='<div class="boxs warning_even"><label>延迟：</label><input class="text days" /><label>天</label><br/><label class="notic">通知：</label><br/><a href="javascript:;" class="btn-green3 save">保存</a><a href="javascript:;" class="btn-green3 delete">删除</a><input class="text userFid" type="hidden"/><input class="text fid" type="hidden"/></div>';
var box2='<div class="boxs warning_rate"><label>介于：</label><input class="num sPoint" type="text" /><label>% - </label><input class="num ePoint" type="text" /><label>%</label><br/><label class="notic">通知：</label><br/><a href="javascript:;" class="btn-green3 save">保存</a><a href="javascript:;" class="btn-green3 delete">删除</a><input class="text userFid" type="hidden"/><input class="text fid" type="hidden"/></div>';
var input='<input type="text" class="text" />';
var warningDatas=null;
var warningStatus=0;
 $(function(){
	 $('#default1').combotree({
		 required:true,
		 novalidate:true,
		 height:22,
		 width:100,
		 url:'${ctx}/orgController/getAllTree?asynLeaf=true',
		 onBeforeExpand:function(node,param){
			expend($('#default1'),node,param);
		 },
		 onClick:function(node){
			 $('.defaultUID1').val(node.id);
		 }
	 });
	
	 $('#default2').combotree({
		 required:true,
		 novalidate:true,
		 height:22,
		 width:100,
		 url:'${ctx}/orgController/getAllTree?asynLeaf=true',
		 onBeforeExpand:function(node,param){
			expend($('#default2'),node,param);
		 },
		 onClick:function(node){
			 $('.defaultUID2').val(node.id);
		 }
	 });
	 
	$.post('${ctx}/orgAttributeController/warningEvent',function(data){
		loadData(data);
		if(warningStatus==1){
			$('#default1').parent().hide();
			$('#settingPanel').accordion('getPanel',0).panel('expand');
	    	$('#settingPanel').accordion('getPanel',0).panel({
	    		title:'<span>开启延迟通知<img class="checkbox" onclick="checkbox(this)" src="${ctx}/resources/images/check_true.png"></span>'
	    	});
		}
	});
	
	$.post('${ctx}/orgAttributeController/warningRate',function(data){
		loadData(data);
		if(warningStatus==1){
			$('#default2').parent().hide();
			$('#settingPanel').accordion('getPanel',1).panel('expand');
	    	$('#settingPanel').accordion('getPanel',1).panel({
	    		title:'<span>收益率警报<img class="checkbox" onclick="checkbox(this)" src="${ctx}/resources/images/check_true.png"></span>'
	    	});
		}
	});
});
 
function loadData(data){
	warningDatas=data;
	warningStatus=0;
	if(warningDatas!=null&&warningDatas!=''){
		warningStatus=1;
	}
	if(warningDatas!=null && warningDatas!=""){
		var datas=null;
		var sPoint='';
		var ePoint='';
		var name='';
		var userFid='';
		var fid='';
		var content='';
		var combo='';
		var noticDay='';
		var noticRate='';
		var value='';
		var content='';
		var type='';
		var days='';
		for(var i=0;i<warningDatas.length;i++){
			datas=warningDatas[i].split(',');
			type=datas[0];
			if(type=='WARNING_EVENT_DELAY'){
				name=datas[1];
				userFid=datas[2];
				fid=datas[3];
				days=datas[4];
				combo='<label class="notic">通知：</label><input  type="text" id="comb1-'+i+'" class="text" /><br/>';
				noticDay='<label>延迟：</label><input class="text days" value='+days+' /><label>天</label><br/>';
				value='<input class="text userFid" type="hidden" value='+userFid+' /><input class="text fid" type="hidden" value='+fid+' />';
				
				content='<div class="boxs warning_even">'+noticDay+combo+value+'<a href="#" class="btn-green3 save">保存</a><a href="#" class="btn-green3 delete">删除</a></div>';
				$('#boxContainer1').prepend(content);
				var target=$('#comb1-'+i+'');
				target.combotree({
					 novalidate:true,
					 required:true,
					 height:22,
					 width:100,
					 url:'${ctx}/orgController/getAllTree?asynLeaf=true',
					 onBeforeExpand:function(node,param){
						expend(target,node,param);
					 },
					 onClick:function(node){
						 $('#comb1-'+i+'').parent().find('input.userFid').val(node.id);
					 }
				 });
				target.combotree('textbox').val(name);
				$('#comb1-'+i+'').parent().find('input.days').validatebox({required:true,novalidate:true});
			}else if(type=='WARNING_RATE'){
				name=datas[1];
				sPoint=datas[1];
				ePoint=datas[2];
				name=datas[3];
				userFid=datas[4];
				fid=datas[5];
				combo='<label class="notic">通知：</label><input  type="text" id="comb2-'+i+'" class="text" /><br/>';
				noticRate='<label>介于：</label><input class="num sPoint" type="text" value='+sPoint+' /><label>% - </label><input class="num ePoint" type="text" value='+ePoint+' /><label>%</label><br/>';
				value='<input class="text userFid" type="hidden" value='+userFid+' /><input class="text fid" type="hidden" value='+fid+' />';
				
				content='<div class="boxs warning_rate">'+noticRate+combo+value+'<a href="#" class="btn-green3 save">保存</a><a href="#" class="btn-green3 delete">删除</a></div>';
				$('#boxContainer2').prepend(content);
				var target=$('#comb2-'+i+'');
				target.combotree({
					 required:true,
					 novalidate:true,
					 height:22,
					 width:100,
					 url:'${ctx}/orgController/getAllTree?asynLeaf=true',
					 onBeforeExpand:function(node,param){
						expend(target,node,param);
					 },
					 onClick:function(node){
						 $('#comb2-'+i+'').parent().find('input.userFid').val(node.id);
					 }
				 });
				$('#comb2-'+i+'').parent().find('input.sPoint').validatebox({required:true,novalidate:true});
				$('#comb2-'+i+'').parent().find('input.ePoint').validatebox({required:true,novalidate:true});
				$('#comb2-'+i+'').combotree('textbox').val(name);
			}
		}
	}
};
 
function checkbox(target){
	var key1='WARNING_EVENT_DELAY';
	var key2='WARNING_RATE';
	if($(target).attr('src')=='${ctx}/resources/images/check_false.png'){
		$(target).attr('src','${ctx}/resources/images/check_true.png');
		$(target).parent().parent().parent().next().fadeIn();
		if($(target).parent().text()=='开启延迟通知'){
			$.post('${ctx}/orgAttributeController/active',{key:key1},function(){
				$.post('${ctx}/orgAttributeController/warningEvent',function(data){
					loadData(data);
				});
			});
		}else if($(target).parent().text()=='收益率警报'){
			$.post('${ctx}/orgAttributeController/active',{key:key2},function(){
				$.post('${ctx}/orgAttributeController/warningRate',function(data){
					loadData(data);
				});
			});
		};
	}else{
		$(target).attr('src','${ctx}/resources/images/check_false.png');
		$(target).parent().parent().parent().next().fadeOut();
		if($(target).parent().text()=='开启延迟通知'){
			$.post('${ctx}/orgAttributeController/inactive',{key:key1},function(){
				location.reload(); 
			});
		}else if($(target).parent().text()=='收益率警报'){
			$.post('${ctx}/orgAttributeController/inactive',{key:key2},function(){
				location.reload(); 
			});
		};
	}
}

function expend(target,node,param){
		var t=target.combotree('tree');
		t.tree('options').url ='${ctx}/userController/usersTreeByDep?orgId='+node.id;
		var loadSucFun = t.tree('options').onLoadSuccess;
		if(t.tree('getChildren',node.target).length==0){
			t.tree('options').onLoadSuccess = function(){};
		}else{
			t.tree('options').onLoadSuccess = loadSucFun;
		}
};

$(".addboxs").click(function(){
	var boxContainer=$(this).parent();
	if(boxContainer.attr('id')=='boxContainer1'){
		boxContainer.prepend(box1);
		$(input).appendTo(boxContainer.children("div:first-child").find("label.notic")).combotree({
			 novalidate:true,
			 required:true,
			 height:22,
			 width:100,
			 url:'${ctx}/orgController/getAllTree?asynLeaf=true',
			 onBeforeExpand:function(node,param){
				expend(boxContainer.children("div:first-child").find("input.combotree-f"),node,param);
			 },
			 onClick:function(node){
				boxContainer.children("div:first-child").find("input.userFid").val(node.id);
			 }
		 });
		boxContainer.children("div:first-child").find("input.days").validatebox({required:true,novalidate:true});
	}else if(boxContainer.attr('id')=='boxContainer2'){
		boxContainer.prepend(box2);
		$(input).appendTo(boxContainer.children("div:first-child").find("label.notic")).combotree({
			 novalidate:true, 
			 required:true,
			 height:22,
			 width:100,
			 url:'${ctx}/orgController/getAllTree?asynLeaf=true',
			 onBeforeExpand:function(node,param){
				expend(boxContainer.children("div:first-child").find("input.combotree-f"),node,param);
			 },
			 onClick:function(node){
				boxContainer.children("div:first-child").find("input.userFid").val(node.id);
			 }
		 });
		boxContainer.children("div:first-child").find("input.sPoint").validatebox({required:true,novalidate:true});
		boxContainer.children("div:first-child").find("input.ePoint").validatebox({required:true,novalidate:true});
	}
});

$("div").on('click','.save',function(){
	if($(this).parent().attr('class')=="boxs warning_even"){
		var key='WARNING_EVENT_DELAY';
		var fid=$(this).parent().find("input.fid").val();
		var userFid=$(this).parent().find("input.userFid").val();
		var days=$(this).parent().find("input.days").val();
		$(this).parent().find("input.days").validatebox({novalidate:false});
		//$(this).parent().find("input.combotree-f").combotree({novalidate:false});
		var valid1=$(this).parent().find("input.days").validatebox('isValid');
		var valid2=$(this).parent().find("input.userFid").val();
		if(valid1&&valid2!=null&&valid2!=""){
			$.post('${ctx}/orgAttributeController/saveOrUpdate',{key:key, fid:fid, userFid:userFid, sPoint:'', ePoint:'', days:days},function(status){
				$.fool.alert({msg:"保存成功！"});
			});
			return false;
		}else{
			$.fool.alert({msg:"验证失败！"});
			return false;
		}
	}else if($(this).parent().attr('class')=="boxs warning_rate"){
		var key='WARNING_RATE';
		var fid=$(this).parent().find("input.fid").val();
		var userFid=$(this).parent().find("input.userFid").val();
		var sPoint=$(this).parent().find("input.sPoint").val();
		var ePoint=$(this).parent().find("input.ePoint").val();
		$(this).parent().find("input.sPoint").validatebox({novalidate:false});
		$(this).parent().find("input.ePoint").validatebox({novalidate:false});
		//$(this).parent().find("input.combotree-f").combotree({novalidate:false});
		var valid1=$(this).parent().find("input.sPoint").validatebox('isValid');
		var valid2=$(this).parent().find("input.ePoint").validatebox('isValid');
		var valid3=$(this).parent().find("input.userFid").val();
		if(valid1&&valid2&&valid3!=null&&valid3!=""){
			$.post('${ctx}/orgAttributeController/saveOrUpdate',{key:key, fid:fid, userFid:userFid, sPoint:sPoint, ePoint:ePoint, days:''},function(status){
				$.fool.alert({msg:"保存成功！"});
			});
			return false;
		}else{
			$.fool.alert({msg:"验证失败！"});
			return false;
		}
	};
});

$("div").on('click','.delete',function(){
	var fid=$(this).parent().find("input.fid").val();
	var target=$(this).parent();
	if(fid!=null&&fid!=''){
		$.fool.confirm({msg:'你确定要删除吗？',fn:function(r){
			if(r){
				$.post('${ctx}/orgAttributeController/delete',{fid:fid},function(status){
					$.fool.alert({msg:"删除成功！"});
				});
				target.fadeOut(); 
		        return false;
			}else{
		        return false;
			}
		},title:'确认'});
		return false; 
	}else{
		$(this).parent().fadeOut();
	}
});
</script>
</body>
</html>

