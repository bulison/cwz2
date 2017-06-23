<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>银行信息管理</title>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="javascript:;" href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">仓储初始化</a></li>
	        <li><a href="javascript:;" class="curr">银行信息管理</a></li>
	    </ul>
	</div>
	<div class="toolBox">
	<div id="addBox"></div> 
	<div id="importBox"></div> 
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>现金银行信息</h1>
                </div>             
             </div>
	<div class="toolBox-button" style="margin-right:5px;">
		<fool:tagOpt optCode="bankAdd"><a href="javascript:;" id="add" class="btn-ora-add">新增</a></fool:tagOpt>
		<fool:tagOpt optCode="bankExport"><a href="javascript:;" id="export" class="btn-ora-export">导出</a></fool:tagOpt>
		<fool:tagOpt optCode="bankImport"><a href="javascript:;" id="getIn" class="btn-ora-import" >导入</a></fool:tagOpt>
		<fool:tagOpt optCode="bankRefresh"><a href="javascript:;" id="refresh" class="btn-ora-refresh">刷新</a></fool:tagOpt>
	</div>
	<div class="toolBox-pane">
		<fool:tagOpt optCode="bankSearch"><input id="search" class="easyui-searchbox" data-options="prompt:'请输入编号、名称或账号',iconWidth:26,width:220,height:32,value:'',	searcher:function(value,name){refreshData();}"/></fool:tagOpt>
	</div>
	</div>
	<table id="bankList"></table>
	<div id="pager"></div>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
$('#add').click(function(){
	$('#addBox').window({
		title:'新增现金银行信息',
		top:($(window).height()-320)/2+$(document).scrollTop(),
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/bankController/addBank',
		width:656,
		height:320,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});
});

$('#refresh').click(function(){
	$('#bankList').trigger('reloadGrid');
});

/* $('#bankList').datagrid({
	url:'${ctx}/bankController/list',
	remoteSort:false,
	idField:'fid',
	pagination:true,
	singleSelect:true,
	fitColumns:true,
	onLoadSuccess:function(){
		warehouseAll();
	},
	columns:[[
	  		{field:'fid',title:'fid',hidden:true,width:100},
	  		{field:'code',title:'编号',sortable:true,width:100},
	  		{field:'name',title:'银行名称',sortable:true,width:100,formatter:function(value,row,index){
	  			<fool:tagOpt optCode="bankAction">
	  			<fool:tagOpt optCode="bankAction1">return '<a title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\',\''+row.updateTime+'\')">'+value+'</a>';</fool:tagOpt>
	  			</fool:tagOpt>
	  		}},
	  		{field:'type',title:'类型',width:100,formatter:function(value,row,index){
	  			return value == 1?
	  					"现金":value == 2?"银行":null;
	  		}},
	  		{field:'bank',title:'开户行',width:100},
			{field:'account',title:'账号',sortable:true,width:100}, 
			{field:'createTime',title:'创建时间',width:100},
	  		{field:'creatorName',title:'创建人',width:100},
	  		{field:'orgId',title:'机构ID',hidden:true,width:100},
	  		{field:'updateTime',title:'修改时间戳',hidden:true,width:100},
	  		<fool:tagOpt optCode="bankAction">
	  		{field:'action',title:'操作',width:30,formatter:function(value,row,index){
	  			var statusStr = '';
	  			//statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\',\''+row.updateTime+'\')"></a>';
	  			<fool:tagOpt optCode="bankAction2">statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  			 return statusStr ; 
	  		}}
	  		</fool:tagOpt>
	      ]]
}); */

$('#bankList').jqGrid({	
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/bankController/list',
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
		 warehouseAll(); 
	}, 
colModel:[       
	  		{name:'fid',label:'fid',hidden:true,width:100,align:'center'},
	  		{name:'code',label:'编号',sortable:true,width:100,align:'center'},
	  		{name:'name',label:'银行名称',sortable:true,width:100,align:'center',formatter:function(value,options,row){
	  			<fool:tagOpt optCode="bankAction">
	  			<fool:tagOpt optCode="bankAction1"> var s='<a title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\',\''+row.updateTime+'\')">'+value+'</a>';</fool:tagOpt>
	  			return s;
	  			</fool:tagOpt>
	  		}},	  			  		 	
	  		{name:'type',label:'类型',width:100,align:'center',formatter:function(value,options,row){
	  			return value == 1?
	  					"现金":value == 2?"银行":null;
	  		}},
	  		{name:'bank',label:'开户行',width:100,align:'center'},
	  		{name:'account',label:'帐号',width:100,align:'center'},
	  		{name:'createTime',label:'创建时间',width:100,align:'center'},
	  		{name:'creatorName',label:'创建人',width:100,align:'center'},
	  		{name:'orgId',label:'机构ID',width:100,align:'center',hidden:true,},
	  		{name:'updateTime',label:'修改时间戳',width:100,align:'center',hidden:true,},	
	  		<fool:tagOpt optCode="bankAction">
	  		{name:'action',label:'操作',width:30,width:60,align:'center',formatter:function(value,options,row){	  			
	  			var copy='',d='';
	  			//copy='<a class="btn-copy" href="javascript:;" title="编辑" onclick="editById(\''+row.fid+'\',true)"></a>';
	  			<fool:tagOpt optCode="bankAction2">d='<a class="btn-del" href="javascript:;" title="删除" onclick="removeById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  		    return copy+d;
	  		}}
	  		</fool:tagOpt>
	      ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 


function refreshData(){
	var options = {"keyWord": $('#search').val()}; 
	$('#bankList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
}

//编辑 
function editById(fid,updateTime){
	$('#addBox').window({
		title:'修改现金银行信息', 
		top:($(window).height()-320)/2+$(document).scrollTop(),
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/bankController/editBank?fid="+fid,
		width:656,  
		height:320,
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
					url : '${ctx}/bankController/delete', 
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#bankList').trigger('reloadGrid');
							}});
						}else if(data.returnCode=='1'){						
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#bankList').trigger('reloadGrid');
							}});
						}else{
							$.fool.alert({time:1000,msg:data.message,fn:function(){
								$('#bankList').trigger('reloadGrid');
							}});
						}
		    		}
				});
		 }
	 }});
}

  $.extend($.fn.validatebox.defaults.rules, {
	  integ:{
		    validator:function(value,param){
		      return /^[+]?[0-9]\d*$/.test(value)&&value.length>=16&&value.length<=19;
		    },
		    message: '银行帐号必须是大于16位小于19位的数字'
		  },
  });
  $(function(){	
	$("#getIn").click(function(){
		var s={
				target:$("#importBox"),
				boxTitle:"导入现金银行信息",
				downHref:"${ctx}/ExcelMapController/downTemplate?downFile=银行.xls",
				action:"${ctx}/bankController/import",
				fn:'okCallback()'
		};
		importBox(s);
	});
	
	$("#export").click(function(){
		exportFrom('${ctx}/bankController/export',{"keyWord": $('#search').val()});
	});
	 
	//分页条 
	//setPager($('#bankList'));
});

function okCallback(){
    $("#bankList").trigger("reloadGrid");
	$('#billList').trigger('reloadGrid');
	$("#importBox").window("close");
}

</script>
</body>
</html>
