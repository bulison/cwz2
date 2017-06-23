<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>运输费报价模版</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript"src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript"src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
#search-form font{font-size: 14px;}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>运输费报价模版</h1>
       </div>             
    </div>
 <div id="myList" style="margin: 10px 0px;">
	 <a href="javascript:;" id="add" class="btn-ora-add">新增</a>
	<form id="search-form" style="display: inline-block;">
		<font>编号、名称：</font><input id="search-code" type="text" calss="textBox" name="code" value=""/>
		<font>运输方式：</font><input id="transportTypeText" type="text" class="textBox" name="transportTypeName" value=""/>
	    <font>装运方式：</font><input id="shipmentTypeText" type="text" class="textBox" name="shipmentTypeName" value=""/>
		<a class="btn-blue btn-s go-search1" id="search-btn2" onclick="refreshData()">查询</a>
		<a class="btn-blue btn-s" style="margin-left:5px;" onclick="cleanBox()">清空</a>
	</form>
</div>
<div id="addBox"></div> 
<table id="billList"></table>
<div id="pager"></div>
<script type="text/javascript">
//初始化查询框
$("#search-code").textbox({
	prompt:'编号或名称',
	width:160,
	height:32
});

//运输方式
var transportValue='';	
$.ajax({
	url:"${ctx}/basedata/transitType?num="+Math.random(),
	async:false,		
	success:function(data){	
		transportValue=formatTree(data[0].children,'id','text');	
    }
	});
var transportNameText= $("#transportTypeText").fool("dhxCombo",{
	  width:160,
	  height:32,
	  data:transportValue,
	  clearOpt:false,
	  editable:false,
	  focusShow:true,
	  setTemplate:{
			input:"#name#",
			option:"#name#"
	  	},
    toolsBar:{
    	name:"运输方式",
        refresh:true
    }
});	
//装运方式
var shipmentValue='';	
$.ajax({
	url:"${ctx}/basedata/shipmentType?num="+Math.random(),
	async:false,		
	success:function(data){		  	
		shipmentValue=formatTree(data[0].children,'id','text');	
    }
	});
var shipmentNameText= $("#shipmentTypeText").fool("dhxCombo",{
	  width:160,
	  height:32,
	  data:shipmentValue,	
	  clearOpt:false,
	  editable:false,
	  focusShow:true,
	  setTemplate:{
			input:"#name#",
			option:"#name#"
	  	},
    	toolsBar:{
    	name:"装运方式",
        refresh:true
    }
});
//清空除订单类型外的搜索选项
function cleanBox(){
	$('#search-code').textbox('setValue','');
	transportNameText.setComboText("");
	transportNameText.setComboValue(""); 
	shipmentNameText.setComboText("");
	shipmentNameText.setComboValue(""); 
}
function refreshData(){//查询
	var code=$("#search-code").textbox('getValue');
	var transportTypeFid=transportNameText.getComboText();
	var shipmentTypeFid=shipmentNameText.getComboText();
	var options={searchKey:code,transportTypeName:transportTypeFid,shipmentTypeName:shipmentTypeFid};
	$('#billList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
}
$('#billList').jqGrid({
    datatype:function(postdata){
		$.ajax({
			 url:'${ctx}/transportTemplate/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#billList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	forceFit:true,
	pager:'#pager',
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
	autowidth:true,//自动填满宽度
	height:$(window).outerHeight()-200+"px",
	colModel:[
        {name:'id',label:'id',hidden:true,width:100},
		{name:'code',label:'编号',width:100,align:'center',formatter:function(value,options,row){				 
		   return '<a title="查看" href="javascript:;" onclick="update(\''+row.id+'\')">'+value+'</a>';  											 
  		}},
		{name:'name',label:'名称',width:200,align:'center'},
		{name:'deliveryPlaceName',label:'发货地址',width:100,align:'center'},
		{name:'receiptPlaceName',label:'收货地址',width:100,align:'center'},		
    	{name:'transportTypeName',label:'运输方式',width:100,align:'center'}, 
    	{name:'shipmentTypeName',label:'装运方式',width:100,align:'center'}, 
    	{name:'expectedDays',label:'预计天数',width:50,align:'center'}, 
    	{name:'enable',label:'状态',width:60,align:'center',formatter:function(value){
    		var str="";
        	if(value == '0'){
        		str='停用';
        	}else if(value =='1'){
        		str='启用';
        	}else{
        		str='';
        	}
        	return str;
    	}}, 
    	{name:'describe',label:'描述',width:100,align:'center'}, 
		{name:'action',label:'操作',width:100,align:'center',formatter:function(value,options,row){
			var statusStr = '';
			 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.id+'\')"></a> ';
			if(row.enable=="0"){  					  			
	  			 statusStr += '<a class="btn-approve" title="启用" href="javascript:;" onclick="blockUp(\''+row.id+'\',\''+row.enable+'\')"></a> ';	
  			}else if(row.auditorTime!=""){
	  			 statusStr += '<a class="btn-cancel" title="停用" href="javascript:;" onclick="blockUp(\''+row.id+'\',\''+row.enable+'\')"></a> ';				 			 
  			}	
			return statusStr ; 
		}},
    ]
  }).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
  
	$('#add').click(function(){
		windowHelp('新增运输费报价模版','${ctx}/transportTemplate/add?flag=add');
	});
	function update(id){
		windowHelp('修改运输费报价模版',"${ctx}/transportTemplate/edit?&id="+id+"&flag=edit");
	}
	function windowHelp(title,url){
		$('#addBox').window({
			title:title,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			modal:true,
			href:url,
			width:$(window).width()-10,
			height:$(window).height()-60,
			top:10+$(window).scrollTop(),  
			left:0,
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
	function removeById(id){ 
		 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
			 if(r){
				 $.ajax({
						type : 'post',
						url :'${ctx}/transportTemplate/delete',
						data : {id:id},
						dataType : 'json',					
						success : function(data) {
							dataDispose(data);
							if(data.result == '0'){
								$.fool.alert({time:1000,msg:data.msg});
								 $('#billList').trigger('reloadGrid');
							}else{
								$.fool.alert({time:1000,msg:data.msg});
							}
			    		}
					});
			 }
		 }});
	}
	
	//停用用按钮点击事件
	function blockUp(id,enable){	
		if(enable==0){
			enable=1;
		}else{
			enable=0;	
		}
		$.ajax({
			type : 'post',
			url : getRootPath()+'/transportTemplate/changeEnable?id='+id+'&enable='+enable,			 
			dataType : 'json',
			success : function(data) {
				dataDispose(data);
			if(data.data == '操作成功!'){
				$.fool.alert({time:1000,msg:data.data,fn:function(){
				$('#billList').trigger('reloadGrid');
			}});
			}else{
				$.fool.alert({msg:data.message});
				}
			},
			error:function(){
			$.fool.alert({msg:"系统正忙，请稍后再试。"});
				}
			});
	}
</script>
</body>
</html>
