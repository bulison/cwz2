<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>人员信息管理</title>
<style>
 .form p.hideOut,.form1 p.hideOut{
    display: none;
  }
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>企业人员</h1>
                </div>             
    </div>
	<div class="toolBox">
	<div class="toolBox-button" style="margin-right:5px;">
		<fool:tagOpt optCode="memberAdd"><a href="javascript:;" id="add" class="btn-ora-add" >新增</a></fool:tagOpt>
		<fool:tagOpt optCode="memberExport"><a href="javascript:;" id="getOut" class="btn-ora-export" >导出</a></fool:tagOpt>
		<fool:tagOpt optCode="memberImport"><a href="javascript:;" id="getIn" class="btn-ora-import" >导入</a></fool:tagOpt>
		<fool:tagOpt optCode="memberRefresh"><a href='javascript:;' id="refresh" class="btn-ora-refresh">刷新</a> </fool:tagOpt>
	</div>
	<div class="toolBox-pane">
		<fool:tagOpt optCode="memberSearch"><input id="search" class="easyui-searchbox" data-options="prompt:'请输入编号、名称、手机号码',iconWidth:26,width:220,height:32,value:'',	searcher:function(value,name){refreshData();}"/></fool:tagOpt>
	</div>
	</div>
	<table id="memberList"></table>
	  <div id="pager"></div>
	<div id="addBox"></div>
	<div id="importBox"></div> 
	
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
$('#add').click(function(){
	$('#addBox').window({
		title:'新增人员信息',
		//top:($(window).height()-650)/2+$(document).scrollTop(),
		top:10,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/member/addMember',
		width:656,
		onBeforeClose:function(){
			$(".hideOut").css("display","none");
			$('#openBtn').css("display","inline-block");
			$('#closeBtn').css("display","none");
		},
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			$("html").css("overflow","");
		}
	});
});

$("#getIn").click(function(){
	var s={
			target:$("#importBox"),
			boxTitle:"导入人员",
			downHref:"${ctx}/ExcelMapController/downTemplate?downFile=人员.xls",
			action:"${ctx}/member/import",
			fn:'okCallback()'
	};
	importBox(s);
});

function okCallback(){
	//$("#memberList").datagrid("reload");
	$('#memberList').trigger('reloadGrid');
	$("#importBox").window("close");
}

$('#refresh').click(function(){
	//$('#memberList').datagrid('reload');
	$('#memberList').trigger('reloadGrid');
});

var options = [
      		    {id:'0',name:'不允许'},
      		    {id:'1',name:'允许'}
      		];
var yesNo = [
     		    {id:'0',name:'不是'},
     		    {id:'1',name:'是'}
     		];
var sex = [
     		    {id:'1',name:'男'},
     		    {id:'2',name:'女'}
     		];

$('#memberList').jqGrid({	
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/member/list',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#memberList")[0].addJSONData(data.responseJSON);
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
	  		{name:'userCode',label:'编号',sortable:true,width:100,align:'center'},
	  		{name:'username',label:'名称',sortable:true,width:100,align:'center',formatter:function(value,options,row){
	  			var status='';
	  			<fool:tagOpt optCode="memberAction1">status+='<a href="javascript:;" onclick="editById(\''+row.fid+'\')">'+value+'</a>';</fool:tagOpt> 
	  			return status ;
	  		}},	  			  		 	
	  		{name:'jobNumber',label:'工号',sortable:true,width:100,align:'center'},
	  		{name:'sex',label:'性别',width:100,align:'center',formatter:function(value){
				for(var i=0; i<sex.length; i++){
					if (sex[i].id == value){
						return sex[i].name;
					}
					if(value=='undefined'||value==''||value==null){
						return '';
					}
				}				
				return value;
			}},
	  		{name:'email',label:'邮箱',width:100,align:'center'},
	  		{name:'postCode',label:'邮编',hidden:true,width:100,align:'center'},
	  		{name:'address',label:'地址',hidden:true,width:100,align:'center'},
	  		{name:'fax',label:'传真',hidden:true,width:100,align:'center'},
	  		{name:'idCard',label:'身份证',hidden:true,width:100,align:'center'},
	  		{name:'isWebLogin',label:'允许WEB端登陆',hidden:true,width:100,align:'center',formatter:function(value){	  			
				for(var i=0; i<options.length; i++){
					if(options[i].id == value){
						return options[i].name;
					}
					if(value=='undefined'||value==''||value==null){
						return '';
					}
				}
				return value;
			}},
	  		{name:'isMobileLogin',label:'允许移动端登陆',hidden:true,width:100,align:'center',formatter:function(value){
				for(var i=0; i<options.length; i++){
					if (options[i].id == value){
						return options[i].name;
					}
					if(value=='undefined'||value==''||value==null){
						return '';
					}
				}
				return value;
			}},
	  		{name:'phoneOne',label:'手机号码',width:100,align:'center'},
	  		{name:'phoneTwo',label:'电话',hidden:true,width:100,align:'center'},
	  		{name:'userDesc',label:'描述',hidden:true,width:100,align:'center'},
	  		{name:'deptName',label:'部门',width:100,align:'center'},
	  		{name:'isInterface',label:'是否部门负责人',width:100,align:'center',formatter:function(value){
				for(var i=0; i<yesNo.length; i++){
					if (yesNo[i].id == value) {
						return yesNo[i].name;
					}
					if(value=='undefined'||value==''||value==null){
						return '';
					}
				}
				return value;
			}},
	  		{name:'entryDay',label:'入职日期',hidden:true,width:100,align:'center'},
	  		{name:'position',label:'职位',width:100,align:'center'},
	  		{name:'jobStatusName',label:'状况',hidden:true,width:100,align:'center'},
	  		{name:'educationName',label:'学历',hidden:true,width:100,align:'center'},
	  		{name:'departureDate',label:'离职日期',hidden:true,width:100,align:'center'},
	  		{name:'departureReasons',label:'离职原因',hidden:true,width:100,align:'center'},
	  		{name:'createTime',label:'创建时间',hidden:true,width:100,align:'center'},
	  		{name:'creator',label:'创建人',hidden:true,width:100,align:'center'},
	  		{name:'orgId',label:'机构ID',hidden:true,width:100,align:'center'},
	  		{name:'updateTime',label:'修改时间戳',hidden:true,width:100,align:'center'},
	  		<fool:tagOpt optCode="memberAction">
	  		{name:'action',label:'操作',width:30,width:60,align:'center',formatter:function(value,options,row){	  			
	  			var statusStr = '';
	  			//statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a>';
	  			<fool:tagOpt optCode="memberAction2">statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  			 return statusStr ; 
	  		} }
	  	   </fool:tagOpt>
	      ],	 
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function refreshData(){
	var options = {"searchKey": $('#search').val()};
	$('#memberList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
}

//编辑 
function editById(fid){
	$('#addBox').window({
		title:'修改人员信息',
		top:10,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/member/editMember?fid="+fid,
		width:656,
		onBeforeClose:function(){
			$(".hideOut").css("display","none");
			$('#openBtn').css("display","inline-block");
			$('#closeBtn').css("display","none");
		},
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
					url : '${ctx}/member/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.result=='0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
							//$('#memberList').datagrid('reload');
								$('#memberList').trigger('reloadGrid');
							}});
						}else if(data.result=='1'){
							$.fool.alert({msg:data.msg});
						}else{
				    		$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
				    		$('#save').removeAttr("disabled");
				    	}
					}
				});
		 }
	 }});
}
$(function(){
	//分页条 
	//setPager($('#memberList'));
	
	$("#getOut").click(function(){
		exportFrom('${ctx}/member/export',{"searchKey": $('#search').val()});
	});
});


</script>
</body>
</html>
