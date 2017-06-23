<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>固定资产卡片</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript"
	src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
.myform p.hideOut{
    display: none;
}
.fixed {
    top: 0px;
}
.dhxDiv{margin-right:6px;}
/* .form1{
	padding:5px 0 0 0;
}
.form1 p{
	margin:5px 0px;
}
.form1 p font{
	width:115px;
} */
/* .form1 .easyui-validatebox{
	width:158px;
	height:26px;
}
#btnBox{
  text-align: center;
} */
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>固定资产卡片</h1>
                </div>             
             </div>
	<div class="toolBox">
		<div class="toolBox-button" style="margin-right:5px;">
		<fool:tagOpt optCode="ascardAdd">
			<a href="javascript:;" id="add" class="btn-ora-add">新增</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="ascardAll">
			<a href="javascript:;" id="accruedAll" class="btn-ora-introduce">全计</a>
		</fool:tagOpt>
		</div>
	</div>
	<table id="cardList"></table>
	<div id="pager"></div>
	<div id="addBox"></div>
<script>
/* $('#cardList').datagrid({
	url:'${ctx}/asset/queryAsset',
 	pagination:true,
 	fitColumns:true,
 	remoteSort:true,
 	singleSelect:true,
 	scrollbarSize:0,
 	onLoadSuccess:function(){//列表权限控制
 		warehouseAll();
		<fool:tagOpt optCode="ascardAction1">//</fool:tagOpt>$('.btn-edit').remove();
		<fool:tagOpt optCode="ascardAction2">//</fool:tagOpt>$('.btn-del').remove();
		<fool:tagOpt optCode="ascardAction3">//</fool:tagOpt>$('.btn-approve').remove();
		<fool:tagOpt optCode="ascardAction4">//</fool:tagOpt>$('.btn-cancel').remove();
		<fool:tagOpt optCode="ascardAction5">//</fool:tagOpt>$('.btn-check').remove();
		<fool:tagOpt optCode="ascardAction6">//</fool:tagOpt>$('.btn-bank').remove();
	},
 	columns:[[
             {field:'fid',title:'fid',hidden:true},
             {field:'assetTypeId',title:'assetTypeId',hidden:true},
             {field:'assetCode',title:'资产编号',sortable:true,width:100},
             {field:'assetName',title:'资产名称',sortable:true,width:100},
             {field:'assetTypeName',title:'类型名称',sortable:true,width:100},
             {field:'quentity',title:'购买数量',sortable:true,width:100},
             {field:'discountYear',title:'折旧年限',sortable:true,width:100},
             {field:'residualRate',title:'残值率',sortable:true,width:100},
             {field:'initialValue',title:'资产原值',sortable:true,width:100},
             {field:'sumAccruedValue',title:'累计已提折扣',sortable:true,width:100},
             {field:'residualValue',title:'资产净值',sortable:true,width:100},
             {field:'recordStatus',title:'状态',sortable:true,width:100,formatter:function(value){
             		switch(value){
             		case 0: return '未审核';break;
             		case 1: return '审核';break;
             		case 2: return '计提中';break;
             		case 3: return '计提完成';break;
             		case 4: return '已清算';break;
             		}
             }},
             <fool:tagOpt optCode="ascardAction">
             {field:'action',title:'操作',width:100,formatter:function(value,row,index){
            	 var e = '<a class="btn-edit" title="修改" href="javascript:;" onclick="editer(\''+row.fid+'\')"></a> ';
				 var d = '<a class="btn-del" title="删除" href="javascript:;" onclick="deleterList(\''+row.fid+'\')"></a>';
			  	 var c = '<a class="btn-approve" title="审核" href="javascript:;" onclick="checker(\''+row.fid+'\')"></a>';
			  	 var uc = '<a class="btn-cancel" title="反审核" href="javascript:;" onclick="unchecker(\''+row.fid+'\')"></a>';
				 var cs = '<a class="btn-check" title="清算" href="javascript:;" onclick="clearList(\''+row.fid+'\')"></a>';
			  	 var j = '<a class="btn-bank" title="计提" href="javascript:;" onclick="jt(\''+row.fid+'\')"></a>';
			  	 switch(row.recordStatus){
			  	 	case 0: return e+c+d;break;
			  	 	case 1: return e+j+cs+uc;break;
			  	 	case 2: return e+j+cs;break;
			  	 	case 3: return e+cs;break;
			  	 	case 4: return e;break;
			  	 }
		  		}}
             </fool:tagOpt>
  	          ]]
}); */

 $('#cardList').jqGrid({
   datatype:function(postdata){
		$.ajax({
			 url:'${ctx}/asset/queryAsset',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#cardList")[0].addJSONData(data.responseJSON);
		    	}
		    }
		});
	},
	autowidth:true,
	height:$(window).outerHeight()-200+"px",
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
	loadcomplete:function(){//列表权限控制
		warehouseAll();
	<fool:tagOpt optCode="ascardAction1">//</fool:tagOpt>$('.btn-edit').remove();
	<fool:tagOpt optCode="ascardAction2">//</fool:tagOpt>$('.btn-del').remove();
	<fool:tagOpt optCode="ascardAction3">//</fool:tagOpt>$('.btn-approve').remove();
	<fool:tagOpt optCode="ascardAction4">//</fool:tagOpt>$('.btn-cancel').remove();
	<fool:tagOpt optCode="ascardAction5">//</fool:tagOpt>$('.btn-check').remove();
	<fool:tagOpt optCode="ascardAction6">//</fool:tagOpt>$('.btn-bank').remove();
},
colModel:[
         {name:'fid',label:'fid',hidden:true},
         {name:'assetTypeId',label:'assetTypeId',hidden:true},
         {name:'assetCode',label:'资产编号',sortable:true,width:100,align:'center',formatter:function(value,index,row){  	 
	  		return '<a title="修改" href="javascript:;" onclick="editer(\''+row.fid+'\')">'+value+'</a>';	  		
	  		}},
         {name:'assetName',label:'资产名称',sortable:true,width:100,align:'center'},
         {name:'assetTypeName',label:'类型名称',sortable:true,width:100,align:'center'},
         {name:'quentity',label:'购买数量',sortable:true,width:100,align:'center'},
         {name:'discountYear',label:'折旧年限',sortable:true,width:100,align:'center'},
         {name:'residualRate',label:'残值率',sortable:true,width:100,align:'center'},
         {name:'initialValue',label:'资产原值',sortable:true,width:100,align:'center'},
         {name:'sumAccruedValue',label:'累计已提折旧',sortable:true,width:100,align:'center'},
         {name:'residualValue',label:'资产净值',sortable:true,width:100,align:'center'},
         {name:'recordStatus',label:'状态',sortable:true,width:100,align:'center',formatter:function(value){
         		switch(value){
         		case 0: return '未审核';break;
         		case 1: return '审核';break;
         		case 2: return '计提中';break;
         		case 3: return '计提完成';break;
         		case 4: return '已清算';break;
         		}
         }},
         <fool:tagOpt optCode="ascardAction">
         {name:'action',label:'操作',width:100,align:'center',formatter:function(value,options,row){
        	// var e = '<a class="btn-edit" title="修改" href="javascript:;" onclick="editer(\''+row.fid+'\')"></a> ';
			 var d = '<a class="btn-del" title="删除" href="javascript:;" onclick="deleterList(\''+row.fid+'\')"></a>';
		  	 var c = '<a class="btn-approve" title="审核" href="javascript:;" onclick="checker(\''+row.fid+'\')"></a>';
		  	 var uc = '<a class="btn-cancel" title="反审核" href="javascript:;" onclick="unchecker(\''+row.fid+'\')"></a>';
			 var cs = '<a class="btn-check" title="清算" href="javascript:;" onclick="clearList(\''+row.fid+'\')"></a>';
		  	 var j = '<a class="btn-bank" title="计提" href="javascript:;" onclick="jt(\''+row.fid+'\')"></a>';
		  	 switch(row.recordStatus){
		  	 	case 0: return c+d;break;
		  	 	case 1: return j+cs+uc;break;
		  	 	case 2: return j+cs;break;
		  	 	case 3: return cs;break;
		  	 	case 4: return '';break;
		  	 }
	  		}},
         </fool:tagOpt>
	          ]
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});  
//设定分页栏
$('#accruedAll').click(function(){
	$.ajax({
	    url: "${ctx}/asset/saveAccruedAll",
	    type: "POST",
	}).done(function(data) {
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:'全计完成！',fn:function(){
				$('#cardList').trigger('reloadGrid');
			},time:1000});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg});
		}else{
			$.fool.alert({msg:'系统繁忙，请稍后再试。'});
		}
	});
});
$('#add').click(function(){
	$('#addBox').window({
		title:'新增固定资产卡片',
		top:10+$(window).scrollTop(), 
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/asset/editAsset',
		modal:true,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});
});
function deleterList(id){
	$.fool.confirm({
		title:'删除提示',
		msg:'确认删除资产卡片？',
		fn:function(data){
			if(data){
				$.ajax({
				    url: "${ctx}/asset/deleteAsset?fid="+id,
				    type: "POST",
				}).done(function(data) {
					dataDispose(data);
					if(data.result==0){
						$.fool.alert({msg:'删除成功！',fn:function(){
							$('#cardList').trigger('reloadGrid');
						},time:2000});
					}else if(data.result==1){
						$.fool.alert({msg:data.msg});
					}else{
						$.fool.alert({msg:'系统繁忙，请稍后再试。'});
					}
				});
			}else{
				return false;
			}
		}
	});
	
}
function editer(id){
	$('#addBox').window({
		title:'编辑固定资产卡片',
		top:10+$(window).scrollTop(), 
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/asset/editAsset?fid='+id,
		modal:true,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});
}
function jt(id){
	$('#addBox').window({
		title:'固定资产卡片计提中',
		top:10+$(window).scrollTop(), 
		left:0,
		width:$(window).width()-10,
		height:$(window).height()-60,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/asset/editAsset?fid='+id,
		modal:true,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});
}
function checker(id){
	$.ajax({
	    url: "${ctx}/asset/savePassAudit?fid="+id,
	    type: "POST",
	}).done(function(data) {
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:'审核完成！',fn:function(){
				$('#cardList').trigger('reloadGrid');
			},time:2000});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg});
		}else{
			$.fool.alert({msg:'系统繁忙，请稍后再试。'});
		}
	});
}
function unchecker(id){
	$.ajax({
	    url: "${ctx}/asset/saveCancleAudit?fid="+id,
	    type: "POST",
	}).done(function(data) {
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:'反审核完成！',fn:function(){
				$('#cardList').trigger('reloadGrid');
			},time:2000});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg});
		}else{
			$.fool.alert({msg:'系统繁忙，请稍后再试。'});
		}
	});
}
function clearList(id){
	$.ajax({
	    url: "${ctx}/asset/saveClear?fid="+id,
	    type: "POST",
	}).done(function(data) {
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:'清算完成！',fn:function(){
			  $('#cardList').trigger('reloadGrid');
			},time:2000});
		}else if(data.result==1){
			$.fool.alert({msg:data.msg});
		}else{
			$.fool.alert({msg:'系统繁忙，请稍后再试。'});
		}
	});
}
</script>
</body>
</html>