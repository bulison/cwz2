<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>工资录入</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript"src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript"src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
*{margin: 0px; padding: 0px;}
  /* #search-form{
    display: inline-block;
  } */
  .fixed {
    top: 0px;
}
 .myform .hideOut{
 display:none;
 }
/*  #btnBox{text-align: center;}
 #btnBox p{ margin-left: 10px;} */
 #import{ margin-left: 20px;  display: none; }
/*  .form, .form1 { padding: 10px 0 20px;}
 .form p font,.form1 p font{ width:115px; outline:none;} */
 /* #myList a{
 	margin-right:5px;
 } */
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>工资录入</h1>
       </div>             
    </div>
<div class="nav-box">
	<ul>
		<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
		<li><a href="javascript:;">工资管理</a></li>
		<li><a href="javascript:;" class="curr">工资录入</a></li>
	</ul>
</div>
	<form class="toolBox" id="search-form">
		<div class="toolBox-button" style="margin-right:5px;">
		    <fool:tagOpt optCode="swageAdd"><a href="javascript:;" id="add" class="btn-ora-add">新增</a></fool:tagOpt>
		</div>
			
		<div class="toolBox-pane">
				<input id="search-endDay"class="easyui-datebox"data-options="prompt:'选择月份',width:160,height:32" /> 
				<fool:tagOpt optCode="swageSearch"><a href="javascript:;" id="search-btn" class="btn-blue btn-s">筛选</a></fool:tagOpt>
		</div>	
	</form>
	<div id="addBox"></div>
<table id="billList"></table>
<div id="pager"></div>
<script type="text/javascript">
var goodsSpecChooserOpen=false;
var goodsChooserOpen=false;
var _data="";//存储获取的数据
$(function(){
/* $('#billList').datagrid({
    url:'${ctx}/wage/list',
	idField : 'fid',
	pagination : true,
	fitColumns : true,
	singleSelect:true,
	remoteSort : false,
	onLoadSuccess:function(){//列表权限控制
		warehouseAll();
		<fool:tagOpt optCode="swageAction1">//</fool:tagOpt>$('.btn-edit').remove();
		<fool:tagOpt optCode="swageAction2">//</fool:tagOpt>$('.btn-del').remove();
		<fool:tagOpt optCode="swageAction3">//</fool:tagOpt>$('.btn-approve').remove();
		<fool:tagOpt optCode="swageAction4">//</fool:tagOpt>$('.btn-cancel').remove();
		<fool:tagOpt optCode="swageAction5">//</fool:tagOpt>$('.btn-detail').remove();
	},
    columns:[[
        {field:'fid',title:'fid',hidden:true,width:100},
		{field:'wageDate',title:'月份',width:100,formatter:function(value,row,index){	
			<fool:tagOpt optCode="swageAction">
			 if(row.auditorTime==null||row.auditorTime==''){
				return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')">'+value+'</a>';  
			}else{
				return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\',\''+1+'\')">'+value+'</a>'; 
			}  		 
  			</fool:tagOpt>
  		}},
		{field:'deptName',title:'部门',width:100},
		{field:'remark',title:'备注',width:100},
		{field:'creatorName',title:'创建人',width:100},
    	{field:'auditorTime',title:'审核时间',width:100,hidden:true}, 
    	<fool:tagOpt optCode="swageAction">
		{field:'action',title:'操作',width:100,formatter:function(value,row,index){
			var statusStr = '';
			if(row.auditorTime==""||row.auditorTime==null){
  				//statusStr += '<a class="btn-edit" title="修改" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> '; 
	  			 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
	  			 statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+row.fid+'\')"></a> ';	
  			}else if(row.auditorTime!=""){
	  			 statusStr += '<a class="btn-cancel" title="反审核" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
				 //statusStr += '<a class="btn-detail" title="详情" href="javascript:;" onclick="editById(\''+row.fid+'\',1)"></a>'; 			 
  			}	
			return statusStr ; 
		}},
		</fool:tagOpt>
    ]]
  }); */
  
  $('#billList').jqGrid({
	    url:'${ctx}/wage/list',
	    datatype:"json",
		autowidth:true,//自动填满宽度
		height:$(window).outerHeight()-200+"px",
		mtype:"post",
		pager:'#pager',
		rowNum:10, //每页显示记录数 
		rowList:[ 10, 20, 30 ],
		viewrecords:true,
		forceFit:true,//调整列宽度，表格总宽度不会改变
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},
		loadcomplete:function(){//列表权限控制
			warehouseAll();
			<fool:tagOpt optCode="swageAction1">//</fool:tagOpt>$('.btn-edit').remove();
			<fool:tagOpt optCode="swageAction2">//</fool:tagOpt>$('.btn-del').remove();
			<fool:tagOpt optCode="swageAction3">//</fool:tagOpt>$('.btn-approve').remove();
			<fool:tagOpt optCode="swageAction4">//</fool:tagOpt>$('.btn-cancel').remove();
			<fool:tagOpt optCode="swageAction5">//</fool:tagOpt>$('.btn-detail').remove();
		},
		colModel:[
	        {name:'fid',label:'fid',hidden:true,width:100},
			{name:'wageDate',label:'月份',width:100,align:'center',formatter:function(value,options,row){	
				<fool:tagOpt optCode="swageAction">
				 if(row.auditorTime==null||row.auditorTime==''){
					return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')">'+value+'</a>';  
				}else{
					return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\',\''+1+'\')">'+value+'</a>'; 
				}  		 
	  			</fool:tagOpt>
	  		}},
			{name:'deptName',label:'部门',width:100,align:'center'},
			{name:'remark',label:'备注',width:100,align:'center'},
			{name:'creatorName',label:'创建人',width:100,align:'center'},
	    	{name:'auditorTime',label:'审核时间',width:100,align:'center',hidden:true}, 
	    	<fool:tagOpt optCode="swageAction">
			{name:'action',label:'操作',width:100,align:'center',formatter:function(value,options,row){
				var statusStr = '';
				if(row.auditorTime==""||row.auditorTime==null){
	  				//statusStr += '<a class="btn-edit" title="修改" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> '; 
		  			 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
		  			 statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+row.fid+'\')"></a> ';	
	  			}else if(row.auditorTime!=""){
		  			 statusStr += '<a class="btn-cancel" title="反审核" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
					 //statusStr += '<a class="btn-detail" title="详情" href="javascript:;" onclick="editById(\''+row.fid+'\',1)"></a>'; 			 
	  			}	
				return statusStr ; 
			}},
			</fool:tagOpt>
	    ]
	  }).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
  //点击新增工资数据
$('#add').click(function(){
	$('#addBox').window({
		title:'新增工资录入单据',
		top:10+$(window).scrollTop(),  
		left:0,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		modal:true,
		href:'${ctx}/wage/editWage?flag=add&fid=',
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			$("html").css("overflow","");
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
			 location.reload();
		},
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		} 
	});
});
});

//修改、编辑//查看
//列表页,查看详情
function editById(fid,mark){
	var title="修改工资单";
	if(mark==1){
		title="查看工资单";
		$('.btn-save').css('display','none');
	}
	$('#addBox').window({
		title:title,
		top:10+$(window).scrollTop(),  
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		modal:true,
		href:'${ctx}/wage/editWage?flag=modified&mark='+mark+'&fid='+fid,
		 onClose:function(){
			 $("html").css("overflow","");
			if(goodsChooserOpen){
				$('#goodsChooser').window("destroy");
				goodsChooserOpen=false;
			}
			if(goodsSpecChooserOpen){
				$("#goodsSpecChooser").window("destroy");
				goodsSpecChooserOpen=false;
			}
		  location.reload(); //窗口一关闭重新刷新当前页面 ,否则会导致列表页面日期控件不正确
		},
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		}  	
	});	
} 

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url :'${ctx}/wage/delete',
					data : {fid : fid},
					dataType : 'json',					
					success : function(data) {
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:"操作成功！"});
							 $('#billList').trigger('reloadGrid');
						}else{
							$.fool.alert({msg:data.msg});
						}
		    		}
				});
		 }
	 }});
}

//审核
function verifyById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要审核该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/wage/passAudit',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.result == "0"){	
							$.fool.alert({time:1000,msg:"操作成功！"});
							 $('#billList').trigger('reloadGrid');
						}else{							
							$.fool.alert({msg:data.msg});
						}
		    		}
			});
		 }
	 }});
}

//反审核
function cancelById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要反审核该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/wage/cancleAudit',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.result == "0"){
							$.fool.alert({time:1000,msg:"操作成功！"});						
							 $('#billList').trigger('reloadGrid');
						}
						else{
							$.fool.alert({msg:data.msg});
						}
		    		}
				});
		 }
	 }});
}
//月份数据加载
$('#search-endDay').datebox({
	editable:false,
	onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
		setTimeout(function(){span.trigger('click');},0); //触发click事件弹出月份层,秒延迟是为了防止系统面板上移的BUG		
		if (!td) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
			td = ps.find('div.calendar-menu-month-inner td');
	        td.click(function (e) {
	        	e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
	            var years = /\d{4}/.exec(span.html())[0]//得到年份
	                        , month = parseInt($(this).attr('abbr'), 10) + 1; //月份
	                        $('#search-endDay').datebox('hidePanel')//隐藏日期对象
	                        .datebox('setValue', years + '-' + month); //设置日期的值
	                    });
	                }, 0)
		            $("#search-endDay").datebox('calendar').find('.calendar-header').hide();
		            $("#search-endDay").datebox('calendar').parent().siblings('.datebox-button').hide();
		            $('input.calendar-menu-year').attr("disabled","disabled");//避免填写年份后会回到选择日期的面板的问题
	            },
	            parser: function (s) {//配置parser，返回选择的日期
	                if (!s) return new Date();
	                var arrs = s.split('-');
	                return new Date(parseInt(arrs[0], 10), parseInt(arrs[1], 10) - 1, 1);	                
	            },
	            formatter: function (d) {
	            	if(d.getMonth()==0){
						return d.getFullYear()-1 + '-12'; 
					}else{
						return d.getFullYear() + '-' + d.getMonth();
					}
	            }//配置formatter，只返回年月            
	        });
	    var ps = $('#search-endDay').datebox('panel'), //日期选择对象
	    td = false, //日期选择对象中月份
	    span = ps.find('span.calendar-text'); //显示月份层的触发控件
  
//筛选
$("#search-btn").click(function(){
	var wageDate=$("#search-endDay").datebox('getText');
	var options = {"wageDate":wageDate};
	$('#billList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
});
</script>
</body>
</html>
