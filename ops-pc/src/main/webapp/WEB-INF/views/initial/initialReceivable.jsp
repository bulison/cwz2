<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>期初应收</title>
<style type="text/css">
	#bolting{ width: 160px;height: 27px; border: 1px solid #ccc; background: #fff;margin-right: 19px;}
	.button_a{display:inline-block;margin-bottom: 0; position:relative;right: 48px; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px}
	.btn_bot{border-top:8px solid #fff;top:10px;}
	.btn_top{border-bottom:8px solid #fff;top:8px;}
	.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent; position: absolute;right:4px;}
	#search-form{position: absolute;z-index: 999;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;right: 23px;display: none}
	#search-form p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 5px 20px 5px 10px; width:235px}
	.button_clear{ border-top: 1px solid #D5DBEA;padding-top:8px; text-align: right;}
	.button_clear a{ vertical-align:middle;}
	#search-form span{margin-right: 5px;}
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">仓储初始化</a></li>
	        <li><a href="javascript:;" class="curr">期初应收</a></li>
	    </ul>
	</div>
	<div id="addBox"></div> 
	<div id="importBox"></div> 
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>期初应收</h1>
                </div>             
             </div>
	
	<div class="toolBox">
		<div class="toolBox-button" style="margin-right:5px;">
			<fool:tagOpt optCode="irecAdd"><a href="javascript:;" id="add" class="btn-ora-add">新增</a></fool:tagOpt>
			<fool:tagOpt optCode="irecExport"><a href="javascript:;" id="getOut" class="btn-ora-export" >导出</a></fool:tagOpt>
			<fool:tagOpt optCode="irecImport"><a href="javascript:;" id="getIn" class="btn-ora-import" >导入</a></fool:tagOpt>
			<fool:tagOpt optCode="irecRefresh"><a href='javascript:;' id="refresh" class="btn-ora-refresh">刷新</a> </fool:tagOpt>
		</div>
		<div class="toolBox-pane">
			<fool:tagOpt optCode="irecSearch">
				<input class="easyui-textbox" id="search-code" data-options="{prompt:'单号',width:162,height:32}" /><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
				<div style="float:right;position: relative">
					<input type="text" name="inMemberId" id="bolting" placeholder="请选择筛选条件" />
					<a href="javascript:;" class="button_a"><span class="button_span btn_top"></span></a>
					<form action="" id="search-form">
						<p><span>客户:</span><input id="customer" /></p>
						<p><span>负责人:</span><input id="InMember"/></p>
						<div class="button_clear">
							<a href="javascript:;" class="btn-blue btn-s search-form" id="search-form-btn">查询</a>
							<a href="javascript:;" class="btn-blue btn-s" onclick="clearInput()">清空</a>
							<a href="javascript:;" class="btn-blue btn-s" onclick="javascript:$('#search-form').hide()">关闭</a>
						</div>
					</form>
				</div>
			</fool:tagOpt>
			<%--<fool:tagOpt optCode="irecSearch"><input id="search" class="easyui-searchbox" data-options="prompt:'请输入客户或负责人',iconWidth:26,width:220,height:32,value:'',searcher:function(value,name){refreshData();}"/></fool:tagOpt>--%>
		</div>
	</div>
	<table id="iniRecList"></table>
	<div id="pager"></div>
	
<%@ include file="/WEB-INF/views/common/js.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"	></script>
<script type="text/javascript">
var enableEditOrDelete = "${enableEditOrDelete}";
var memberChooserOpen=false;
var customerChooserOpen=false;
if(enableEditOrDelete == "true"){
	$('#add').click(function(){
		$('#addBox').window({
			title:'新增期初应收',
			top:($(window).height()-320)/2+$(document).scrollTop(),
			modal:true,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			shadow:false,
			href:'${ctx}/initialReceivableController/addInitialReceivable',
			width:656,
			onClose:function(){
				if(memberChooserOpen){
					$('#memberChooser').window('destroy');
					memberChooserOpen=false;
				}
				if(customerChooserOpen){
					$('#customerChooser').window('destroy');
					customerChooserOpen=false;
				}
				if($('#gbox_capitalPlanList').length>0){
					$('#pop-win').window('destroy');
				}
				$("html").css("overflow","");
			},
			onOpen:function(){
				$("html").css("overflow","hidden");
				$(this).parent().prev().css("display","none");
			},
		});
	});
	$("#getIn").click(function(){
		var s={
				target:$("#importBox"),
				boxTitle:"导入期初应收",
				downHref:"${ctx}/ExcelMapController/downTemplate?downFile=期初应收.xls",
				action:"${ctx}/initialReceivableController/import",
				fn:'okCallback()'
		};
		importBox(s);
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
	$('#add,#getIn').css("background-color","grey");
	$('#add,#getIn').tooltip({
		position: 'top',
	    content:text,		
		onShow: function(){
			$(this).tooltip('tip').css({
				backgroundColor: '#666',
				borderColor: '#666'
				});
	}});
}

$('#refresh').click(function(){
	$('#iniRecList').trigger('reloadGrid');
});

$('#iniRecList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/initialReceivableController/list',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#iniRecList")[0].addJSONData(data.responseJSON);
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
	  		{name:'fid',label:'fid',hidden:true,width:100},
	  		{name:'code',label:'单号',align:'center',sortable:true,width:100,formatter:function(value,index,row){
	  			if(enableEditOrDelete == "true"){
	  				return '<fool:tagOpt optCode="irecAction1"><a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\')">'+value+'</a></fool:tagOpt>';
	  			}else{
	  				return value;
	  			}
	  			
	  			}},
	  		{name:'customerName',label:'客户',align:'center',sortable:true,width:100},
	  		{name:'amount',label:'应收金额',align:'center',sortable:true,width:100},
	  		{name:'freeAmount',label:'优惠金额',align:'center',sortable:true,width:100},
	  		{name:'billDate',label:'日期',align:'center',sortable:true,width:100},
	  		{name:'memberName',label:'负责人',align:'center',sortable:true,width:100},
	  		{name:'describe',label:'描述',align:'center',width:100},
			{name:'createTime',label:'创建时间',align:'center',width:100},
	  		{name:'updateTime',label:'修改时间戳',align:'center',hidden:true,width:100},
	  		<fool:tagOpt optCode="irecAction">
	  		{name:'action',label:'操作',width:30,align:'center',formatter:function(value,index,row){
	  			var statusStr = '';
	  			/* <fool:tagOpt optCode="irecAction1">statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a>';</fool:tagOpt> */
	  			<fool:tagOpt optCode="irecAction2">statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a>';</fool:tagOpt>
	  			if(enableEditOrDelete == "true"){ 
	  				return statusStr ; 
	  			}else{
	  				return '';
	  			}
	  		}}
	  		</fool:tagOpt>
	      ]
});
$('#customer').fool('dhxComboGrid',{
    clearOpt:false,
    novalidate:true,
    data:scusData,
    prompt:"客户",
    focusShow:true,
    onlySelect:true,
    filterUrl:getRootPath()+'/customer/list?showDisable=0',
    setTemplate:{
        input:"#name#",
        columns:[
            {option:'#code#',header:'编号',width:100},
            {option:'#name#',header:'名称',width:100},
        ],
    },
    toolsBar:{
        refresh:true
    },
    onChange:function(value,text){
        // $("#mySupplierId").val(value);
    }
})
//负责人
$('#InMember').fool('dhxComboGrid',{
    clearOpt:false,
    prompt:"人员",
    novalidate:true,
    data:smemData,
    focusShow:true,
    onlySelect:true,
    filterUrl:getRootPath()+'/member/vagueSearch',
    setTemplate:{
        input:"#username#",
        columns:[
            {option:'#userCode#',header:'编号',width:100},
            {option:'#jobNumber#',header:'工号',width:100},
            {option:'#username#',header:'名称',width:100},
            {option:'#phoneOne#',header:'电话',width:100},
            {option:'#deptName#',header:'部门',width:100},
            {option:'#position#',header:'职位',width:100},
        ],
    },
    toolsBar:{
        refresh:true
    }
})
function clearInput() {
    ($('#customer').next())[0].comboObj.setComboText("");
    ($('#InMember').next())[0].comboObj.setComboText("");
}
$('.Inquiry').click(function(){
    clearInput();
    $('#search-form-btn').click();
});
$('#search-form-btn').click(function () {
    var code=$("#search-code").textbox('getValue');
    var customerId = ($('#customer').next())[0].comboObj.getSelectedValue();
    var memberId = ($('#InMember').next())[0].comboObj.getSelectedValue();
    $('#iniRecList').jqGrid('setGridParam',{postData:{customerId:customerId,memberId:memberId,searchKey:code}}).trigger("reloadGrid");
})
$('#bolting').attr('readonly',true);
$('#bolting,.button_a').click(function () {
    $('#search-form').toggle();
    if($('.button_span').hasClass('btn_top')){
        $('.button_span').removeClass('btn_top');
        $('.button_span').addClass('btn_bot');
    }else{
        $('.button_span').removeClass('btn_bot');
        $('.button_span').addClass('btn_top');
    }
})
//编辑 
function editById(fid){
	$('#addBox').window({
		title:'修改期初应收', 
		top:($(window).height()-320)/2+$(document).scrollTop(),
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		shadow:false,
		href:"${ctx}/initialReceivableController/editInitialReceivable?fid="+fid,
		width:656,  
		onClose:function(){
			if(memberChooserOpen){
				$('#memberChooser').window('destroy');
				memberChooserOpen=false;
			}
			if(customerChooserOpen){
				$('#customerChooser').window('destroy');
				customerChooserOpen=false;
			}
			if($('#gbox_capitalPlanList').length>0){
				$('#pop-win').window('destroy');
			}
			$("html").css("overflow","");
		},
		onOpen:function(){
			$("html").css("overflow","hidden");
			$(this).parent().prev().css("display","none");
		},
	});
} 

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/initialReceivableController/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#iniRecList').trigger('reloadGrid');
							}});
						}else if(data.result == '1'){
							$.fool.alert({msg:data.msg});
						}
		    		}
				});
		 }
	 }});
}

$(function(){
	
	$("#getOut").click(function(){
		exportFrom('${ctx}/initialReceivableController/export',{"searchKey": $('#search-code').val()});
	});
	
	//分页条 
	//setPager($('#iniRecList'));
});

function okCallback(){
	$("#importBox").window("close");
	$('#iniRecList').trigger('reloadGrid');
}
</script>
</body>
</html>
