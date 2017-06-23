<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>供应商管理</title>
</head>
<body>

<div class="toolBox">
<div class="titleCustom">
     <div class="squareIcon">
         <span class='Icon'></span>
         <div class="trian"></div>
         <h1>供应商管理</h1>
     </div>             
</div>
<div class="toolBox-button" style="margin-right:5px;">
	<fool:tagOpt optCode="supplierAdd"><a href='javascript:;' id="add" class="btn-ora-add">新增</a></fool:tagOpt>
	<fool:tagOpt optCode="supplierExport"><a href='javascript:;' id="export" class="btn-ora-export">导出</a></fool:tagOpt>
	<fool:tagOpt optCode="supplierImport"><a href='javascript:;' id="getIn" class="btn-ora-import" >导入</a></fool:tagOpt>
	<fool:tagOpt optCode="supplierRefresh"><a href='javascript:;' id="refresh" class="btn-ora-refresh">刷新</a> </fool:tagOpt>
</div>

<div class="toolBox-pane">
	<fool:tagOpt optCode="supplierSearch"><input id="search" name="search-box" class="easyui-searchbox" data-options="prompt:'请输入搜索内容(编号或名称)',iconWidth:26,width:220,height:32,value:'',	searcher:function(value,name){search();}"/></fool:tagOpt>
</div>

<div class="tool-box-pane tool-box-where" style="margin-left:150px;display: none;">
	<div class="split-right" id="sort-box">
	<a href='javascript:;' _name="code">编号</a>
	<a href='javascript:;' _name="name">名称</a>
	<a href='javascript:;' _name="area.name">地区</a>
	<a href='javascript:;' _name="category.name">分类</a>
	</div>
</div>
</div>

 <table id="goodslist"></table> 
  <div id="pager"></div>
<%-- <table class="easyui-datagrid" id="goodslist"  data-options="url:'${ctx}/supplier/list?showDisable=1',pagination:true,fitColumns:true,remoteSort:false,singleSelect:true,">
  <thead>
   <th field="code" width="10" data-options="editor:'textbox',sortable:true">编号</th>
   <th field="name" width="10" data-options="sortable:true<fool:tagOpt optCode="supplierAction2">,formatter:open</fool:tagOpt>">名称</th>
   <th field="areaName" width="10">地区</th>
   <th field="categoryName" width="10">分类</th>
   <th field="principal" width="10">法人代表</th>
   <th field="principalPhone" width="10">法人联系电话</th>
   <th field="tel" width="10">电话</th>
   <th field="fax" width="10">业务联系人传真</th>
   <th field="registerDate" width="10" formatter=dateStr>成立日期</th>
   <th field="staffNum" width="10">在业人数</th>
   <th field="bussinessRange" width="10">经营范围</th>
   <fool:tagOpt optCode="supplierAction">
   <th field="fid" width="10" formatter=action>操作</th>
   </fool:tagOpt>
  </thead>
 </table> 
 --%>
<div id="addBox"></div>
<div id='import-window'></div>
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
$('#goodslist').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/supplier/list?showDisable=1',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#goodslist")[0].addJSONData(data.responseJSON);
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
	  		{name:'name',label:'名称',sortable:true,width:100,align:'center',formatter:function(value,options,row){
	  			var status='';
	  			<fool:tagOpt optCode="supplierAction2">status+='<a href="javascript:;" onclick="update(\''+row.fid+'\')">'+value+'</a>';</fool:tagOpt> 
	  			return status ;
	  		}},	  			  		 	
	  		{name:'areaName',label:'地区',width:100,align:'center'},	  		
	  		{name:'categoryName',label:'分类',width:100,align:'center'},
	  		{name:'principal',label:'法人代表',width:100,align:'center'},
	  		{name:'principalPhone',label:'法人联系电话',width:100,align:'center'},
	  		{name:'tel',label:'电话',width:100,align:'center'},
	  		{name:'fax',label:'业务联系人传真',width:100,align:'center'},	  			
	  		{name:'registerDate',label:'成立日期',width:100,align:'center'},
	  		{name:'staffNum',label:'在业人数',width:100,align:'center'},
	  		{name:'bussinessRange',label:'经营范围',width:100,align:'center'},  		
	  		{name:'action',label:'操作',width:30,width:60,align:'center',formatter:function(value,options,row){	  			
	  			var copy='',d='';
	  			<fool:tagOpt optCode="supplierAction3">copy='<a class="btn-copy" href="javascript:;" title="复制" onclick="update(\''+row.fid+'\',true)"></a>';</fool:tagOpt>
	  			<fool:tagOpt optCode="supplierAction4">d='<a class="btn-del" href="javascript:;" title="删除" onclick="deleted(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  		    return copy+d;
	  		}}
	      ],	  	
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

  $(document).ready(function(){	
	$("#sort-box a").click(function(){
		$(this).addClass("curr").siblings('a').removeClass('curr');
		search(null,null,$(this).attr('_name'));
	});
	
	$('#add').click(function(){
		windowHelp('新增供应商信息','${ctx}/supplier/add');
	});
	
	$("#export").click(function(){
		exportFrom('${ctx}/supplier/export',{"searchKey": $('#search').val()});
	});
	
	$("#refresh").click(function(){
		//$("#goodslist").datagrid('reload');
		$('#goodslist').trigger('reloadGrid');
	});
	
	 $("#getIn").click(function(){
		var s={
				target:$("#import-window"),
				boxTitle:"导入供应商",
				downHref:"${ctx}/ExcelMapController/downTemplate?downFile=供应商.xls",
				action:"${ctx}/supplier/import",
				fn:'okCallback()'
		};
		importBox(s);
	});
	 
	//setPager($('#goodslist'));
}); 
 function open(value,row,index){
	return '<a href="javascript:;" onclick="update(\''+row.fid+'\')">'+value+'</a>';
} 
function okCallback(){
	//$("#goodslist").datagrid("reload");
	$('#goodslist').trigger('reloadGrid');
	$("#import-window").window("close");
}
 
function windowHelp(title,url){
	$('#addBox').window({
		title:title,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:url,
		width:950,
		height:580,
		top:($(window).height()-580)/2+$(document).scrollTop(),
		modal:true,
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			if($('#pop-win').html()!=""){
				$('#pop-win').window("destroy");
			}
			$("html").css("overflow","");
		}
	});
}
/* $('#goodslist').datagrid({onLoadSuccess:function(){
	warehouseAll();
}});*/
function operate(value,row,index){
	var c='',e='',copy='',d='';
	//c='<a class="btn-detail" href="javascript:;" title="详情" onclick="detail(\''+value+'\')"></a>';
	//e='<a class="btn-edit" href="javascript:;" title="编辑" onclick="update(\''+value+'\')"></a>';
	<fool:tagOpt optCode="supplierAction3">copy='<a class="btn-copy" href="javascript:;" title="复制" onclick="update(\''+value+'\',true)"></a>';</fool:tagOpt>
	<fool:tagOpt optCode="supplierAction4">d='<a class="btn-del" href="javascript:;" title="删除" onclick="deleted(\''+value+'\')"></a>';</fool:tagOpt>
    return c+e+copy+d;
} 

function update(fid,copy){
	windowHelp(copy ? '复制供应商信息' : '修改供应商信息',"${ctx}/supplier/edit?&copy="+copy+"&id="+fid);
}

function detail(fid){
	windowHelp('供应商信息详情',"${ctx}/supplier/detail?id=" +  fid);
}

function deleted(fid){
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r){
			$.ajax({
				type : 'post',
				url : '${ctx}/supplier/delete',
				data : {id :fid},
				dataType : 'json',
				success : function(data) {	
					dataDispose(data);
					if(data.returnCode == 0){
						$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
							//$('#goodslist').datagrid('reload');
							$('#goodslist').trigger('reloadGrid');
						}});
					}else{
						$.fool.alert({msg:'删除失败！'+data.message});
					}
		    	}
			});
		}
	},title:'确认'});
}
	
 
function search(value,name,sort){
	var order=sort==undefined?$("#sort-box a[class='curr']").attr("_name"):sort;
	var keyword = $('#search').val();
	var options = {'searchKey':keyword,'order':order};
	//$('#goodslist').datagrid('load',options);
	$('#goodslist').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
}

function saveHelp(targetForm){
	if(targetForm.fool('fromVali')){
		submitForm(targetForm);
	}else{
		return false;
	}
}

function submitForm(targetForm){
	targetForm = $.fool.get$(targetForm);
    var flag = $('input[name="recordStatus"]:checked').val();
    var data = targetForm.serializeJson();
    data.recordStatus=flag;
	$.ajax({
		url:'${ctx}/supplier/save',
		type:"post",
		data:data,
		cache:false,
		dataType:'json',
		success:function(data){
			dataDispose(data);
			if(data.returnCode=='0'){
				$.fool.alert({time:1000,msg:'操作成功!',fn:function(){
					if(dhxkey == 1){
                        selectTab(dhxname,dhxtab);
                    }
					$('#addBox').window('close');
					//$('#goodslist').datagrid('reload');
					$('#goodslist').trigger('reloadGrid');
				}});
			}else{
				$.fool.alert({msg:'操作失败!['+data.message+']'});
			} 
		}
	});
	return false;
}

function dateStr(value,row,index){
	if(value){
		return value.substring(0,10);
	}else{
		return value;
	}
	
}; 

</script>