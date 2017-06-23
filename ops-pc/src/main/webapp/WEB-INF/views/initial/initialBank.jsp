<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>现金银行期初信息管理</title>
</head>
<body>
	<div id="addBox"></div> 
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>现金银行期初</h1>
                </div>             
             </div>	
	<div class="toolBox">
		<div class="toolBox-button" style="margin-right:5px;">
			<fool:tagOpt optCode="ibankAdd"><a href="javascript:;" id="add" class="btn-ora-add">新增</a></fool:tagOpt>
			<fool:tagOpt optCode="ibankRefresh"><a href='javascript:;' id="refresh" class="btn-ora-refresh">刷新</a></fool:tagOpt>
		</div>
		<div class="toolBox-pane">
			<fool:tagOpt optCode="ibankSearch"><input id="search" class="easyui-searchbox" data-options="prompt:'根据银行编号、名称或账号查询',iconWidth:26,width:220,height:32,value:'',	searcher:function(value,name){refreshData();}"/></fool:tagOpt>
		</div>
	</div>
	<table id="bankList"></table>
	<div id="pager"></div>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript">
/* window.onload=function(){
	$.post('${ctx}/periodController/theFirstCheck',{},function(data){
		if(data.returnCode=='0'){//如果是0不能添加、修改、删除
			$(".edit").css('display','block');
			$('#add').click(function(){
				$.messager.alert('添加失败','会计期间已结账','info');
			});
		}else{
			$('#add').click(function(){
				add();
			});
		} 	
	});	
}; */
var iniKey = "${enableEditOrDelete}";
if(iniKey == "true"){
	$('#add').click(function(){
		add();
	});
}else{
	var text='';
	if('${state}'==-1){
		text='<span style="color:#fff">会计期间未启用</span>'
	}else if('${state}'==0){
		text='<span style="color:#fff">会计期间已启用</span>'
	}else if('${state}'==1){
		text='<span style="color:#fff">会计期间已结账</span>'
	}else if('${state}'==-2){
	    text='<span style="color:#fff">会计期间不存在</span>'
	}
	$('#add').css("background-color","grey");
	$('#add').tooltip({
		position: 'top',
		content:text,
		onShow: function(){
			$(this).tooltip('tip').css({
				backgroundColor: '#666',
				borderColor: '#666'
				});
		}});
}
function add(){
	$('#addBox').window({
		title:'新增现金银行期初',
		modal:true,
		top:($(window).height()-320)/2+$(document).scrollTop(),
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/initialBankController/addInitialBank',
		width:656,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});	
};
	

$('#refresh').click(function(){
	$('#bankList').trigger('reloadGrid');
});

/* $(function(){
$('#bankList').datagrid({
	url:'${ctx}/initialBankController/list',
	idField:'fid',
	remoteSort:false,
	pagination:true,
	singleSelect:true,
	fitColumns:true,
	onLoadSuccess:function(){
		warehouseAll();
	},
	columns:[[
	  		{field:'fid',title:'fid',hidden:true,width:100},
	  		{field:'checkoutStatus',title:'checkoutStatus',hidden:true,width:100},
	  		{field:'bankCode',title:'银行编号',sortable:true,width:100},
	  		{field:'bankName',title:'银行名称',sortable:true,width:100,<fool:tagOpt optCode="ibankAction1">formatter:function(value,row,index){
	  			return iniKey == "true"?'<a href="javascript:;" title="编辑" onclick="editById(\''+row.fid+'\')">'+value+'</a>':value; 
	  		}</fool:tagOpt>},
	  		{field:'bankAccount',title:'银行账号',sortable:true,width:100},
	  		{field:'amount',title:'结余金额',sortable:true,width:100},
	  		{field:'periodName',title:'会计期间',sortable:true,width:100},
			{field:'describe',title:'描述',width:100}, 
			{field:'createTime',title:'创建时间',width:100},
	  		{field:'updateTime',title:'修改时间戳',hidden:true,width:100},
	  		<fool:tagOpt optCode="ibankAction">
	  		{field:'action',title:'操作',width:30,formatter:function(value,row,index){
	  			var statusStr = '';
	  			if(iniKey == "true"){
	  				<fool:tagOpt optCode="ibankAction2">statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  			}
	  			if(row.checkoutStatus==0){
	  				return statusStr ;
	  			}else{
	  				return "";
	  			}
	  		}}
	  		</fool:tagOpt>
	      ]]
  });
}); */

$('#bankList').jqGrid({	
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/initialBankController/list',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#bankList")[0].addJSONData(data.responseJSON);
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
	loadcomplete:function(){
		//alert(JSON.stringify())
		 warehouseAll(); 
	}, 
	colModel:[       
		  		{name:'fid',label:'fid',hidden:true,width:100,align:'center'},
		  		{name:'checkoutStatus',label:'checkoutStatus',hidden:true,width:100,align:'center'},
		  		{name:'bankCode',label:'银行编号',sortable:true,width:100,align:'center'},	  			  		 	
		  		{name:'bankName',label:'银行名称',width:100,align:'center',formatter:function(value,options,row){	  			
		  			var status='';
		  			<fool:tagOpt optCode="ibankAction1">status+='<a href="javascript:;" onclick="editById(\''+row.fid+'\')">'+value+'</a>'</fool:tagOpt>; 
		  			return status ;
		  		}},
		  		{name:'bankAccount',label:'银行帐号',width:100,align:'center'},
		  		{name:'amount',label:'结余金额',width:100,align:'center'},
		  		{name:'periodName',label:'会计期间',width:100,align:'center'},
		  		{name:'describe',label:'描述',width:100,align:'center'},
		  		{name:'createTime',label:'创建时间',width:100,align:'center'},	
		  		{name:'updateTime',label:'修改时间',width:100,align:'center'},
		  		<fool:tagOpt optCode="ibankAction">
		  		{name:'action',label:'操作',width:30,width:60,align:'center',formatter:function(value,options,row){	  			
		  			var statusStr = '';
		  			<fool:tagOpt optCode="ibankAction2">statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a>';</fool:tagOpt>
		  			if(row.checkoutStatus==0){
		  				return statusStr ;
		  			}else{
		  				return "";
		  			}
		  		}}
		  		</fool:tagOpt>
		      ],
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

function refreshData(){
	var options = {"searchKey": $('#search').val()};
	$('#bankList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
}

//编辑 
function editById(fid){
	$('#addBox').window({
		title:'修改现金银行期初', 
		top:($(window).height()-320)/2+$(document).scrollTop(),
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/initialBankController/editInitialBank?fid="+fid,
		width:656,  
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});
} 

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/initialBankController/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data){
						dataDispose(data);
						if(data.result=='0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#bankList').trigger('reloadGrid');
							}});
						}else if(data.result=='1'){
							$.fool.alert({msg:data.msg,fn:function(){
								$('#bankList').trigger('reloadGrid');
							}});
						}
		    		}
				});
		 }
	 }});
}

$(function(){
	//分页条 
	//setPager($('#bankList'));
});
</script>
</body>
</html>
