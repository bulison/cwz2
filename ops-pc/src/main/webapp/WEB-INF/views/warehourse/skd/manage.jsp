<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>收款单信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
  .open{
	background:url(${ctx}/resources/images/open.png) no-repeat;
	padding-left:0px;
	width:40px;
	background-position:0px -1px;
}

#goodsChooser{
  display: none;
  text-align: center;
}

#btnBox{
  text-align: center;
}

.form p.hideOut,.form1 p.hideOut{
    display: none;
  }

  
#userChooser,#customerChooser,#goodsChooser,#goodsSpecChooser{
  display: none;

}

#userSearcher,#cusSearcher,#goodsSearcher,#goodsSpecSearcher{
  text-align: left;
  margin:10px
}

#add{
  vertical-align: top;
  /* margin-top: 5px; */
}
#search-form{
  display: inline-block;
  width:98%;
}
#search-form .textbox{
  margin:5px 5px 0px 0px
}
#search-form .dhxDiv{
	margin:5px 5px 0px 0px;
}
#search-form .btn-s{
  vertical-align: middle;
  margin:5px 5px 0px 0px
}
#Inquiry{margin-left: 5px;}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 25px;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:29px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:5px;}
.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:91px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px;width:240px;}
.button_clear{ border-top: 1px solid #D5DBEA;text-align: right; margin-top: 10px;}
#search-form #clear-btndiv{margin-left: -5px;}
.roed{
   -moz-transform:scaleY(-1);
   -webkit-transform:scaleY(-1);
   -o-transform:scaleY(-1);
   transform:scaleY(-1);
   filter:FlipV();
}
#bill p.hideOut{
  display: none ;
}
input,label { vertical-align:middle;}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>收款单</h1>
       </div>             
    </div>
	<div id="addBox"></div>
	<div style="margin:5px 0px 10px 0px">  
	  <fool:tagOpt optCode="skdAdd"><a href="javascript:;" id="add" class="btn-ora-add" style="margin-right:5px;" >新增</a></fool:tagOpt>
	   <input id="search-code"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	   <a href="javascript:;" id="print" class="btn-ora-printer" style="margin-left: 5px;">打印</a>
		<a href="javascript:;" id="tranToBankBill" class="btn-ora-trial" style="margin-left: 5px;">生成银行/现金日记帐单</a>
	   <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	   <div class="input_div">
		  <form id="search-form" >
		   <p>开始日期：<input id="search-startDay" name="startDay" /></p>
		   <p>结束日期：<input id="search-endDay" name="endDay"/></p>
		   <p style="margin: 5px 20px 5px 0px;">状态：
               <input type=checkbox value='0' name="recordStatus">未审核
	            <input type=checkbox value='1' name="recordStatus">已审核
               <input type=checkbox value='2' name="recordStatus">已作废
           </p>
			  <p style="height:36px"><span style="float: left;line-height: 28px;margin-left: 7px;">生成银行/现金日记帐单 ：</span>
				  <span style="float: left;line-height: 28px;margin-left: 7px;">
				<input style="margin-left: 5px;" type=checkbox value="1" name="saveStatus">已生成
				<input type=checkbox value="0" name="saveStatus">未生成
			</span>
			  </p>
		   <p>客户：<input id="search-cusName"/><input id="search-cusId" name="customerId" type="hidden"/></p>
		   <p>账户：<input id="search-bankAccount"/><input id="search-bankId" name="bankId" type="hidden"/></p>
		   <p>收款人：<input id="search-member"/><input id="search-memberId" name="memberId" type="hidden"/></p><!-- <input id="search-voucherCode"/> -->
		    <p>金额：<input id="search-amount" class="easyui-numberbox" name="amount" data-options="width:161,height:32,precision:2"/></p>
		    <div class="button_clear">
		    <fool:tagOpt optCode="skdSearch"><a id="search-btn" class="btn-blue btn-s">查询</a></fool:tagOpt>
		    <a href="javascript:;" id="clear-btn" class="btn-blue btn-s">清空</a>
		    <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
		    </div>
		  </form>
	  </div>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
<script type="text/javascript">
var texts = [
             ];
             
var thead = [
             ];
var chechbox='';
var decide=true;
var rowNum = 10;
var pgChos = [];
var rowChos = [];
$(":checkbox").click(function(){//点击复选框并且只让一个选中
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	 chechbox=$(this).val();
});
var memberData = "",scusData = "",mybankData = "";
$.ajax({
	url:"${ctx}/basedata/query?num="+Math.random(),
	async:false,
	data:{param:"Member,Customer"},
	success:function(data){
		memberData=formatData(data.Member,"fid");
		scusData = formatData(data.Customer,"fid");
    }
	});
$.ajax({
	url:getRootPath()+'/bankController/list?num='+Math.random(),
	async:false,
	data:{},
	success:function(data){
		mybankData=formatData(data.rows,"fid");
    }
	});
//点击下拉按钮
$('.button_a').click(function(){
	$('.input_div').toggle(2);	
	var s=$('.button_a').attr('class'); 
	if(s=="button_a roed"){
		$('.button_a').removeClass('roed');	
	}else{
		$(this).addClass('roed');
	}
});

//点击关闭隐藏
$('#clear-btndiv').click(function(){
	$('.input_div').hide(2);
	$('.button_a').removeClass('roed');
});
//全局查询
$('.Inquiry').click(function(){
    $("#clear-btn").click();
	var searchKey=$("#search-code").textbox('getValue');
    var postdata={"searchKey":searchKey};
	$('#billList').jqGrid('setGridParam',{postData:postdata}).trigger("reloadGrid");
	pgChos=[];
	rowChos=[];
});
//鼠标获取焦点
$('#bolting').focus(function(){ 
	$('.input_div').show(2);
	$('.button_a').addClass('roed');
}); 

$('#add').click(function(){
	/* $('#addBox').window({
		title:'新增收款单',
		top:10,  
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		href:'${ctx}/receiveBill/add',
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			if($("#checkBox").html()){
				$("#checkBox").window("destroy");
			}
		},
		onOpen:function(){
			$(this).parent().prev().css("display","none");
		}
	}); */
	var url = '${ctx}/receiveBill/add';
	warehouseWin("新增收款单",url);
	/* window.location.href="${ctx}/receiveBill/add"; */
});

$('#refresh').click(function(){
	$('#billList').trigger("reloadGrid");
});

$("#search-code").textbox({
	prompt:'单号或原始单号',
	width:160,
	height:32,
	/* inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
		keyup:function(e){
			if(e.keyCode==13){
				search();
			}
		}
	}) */
});

/* $("#search-cusName").textbox({
	editable:false,
	prompt:'客户',
	width:160,
	height:32
});

$("#search-bankId").textbox({
	prompt:'银行账号',
	width:160,
	height:32
});

$("#search-member").textbox({
	editable:false,
	prompt:'收款人',
	width:160,
	height:32
}); */

/* $("#search-voucherCode").textbox({
	prompt:'原始单号',
	width:160,
	height:32
}); */

$("#search-startDay").fool('datebox',{
	prompt:'开始日期',
	width:160,
	height:32,
	inputDate:true,
	editable : true
});

$("#search-endDay").fool('datebox',{
	prompt:'结束日期',
	width:160,
	height:32,
	inputDate:true,
	editable : true
});

$('#print').click(function(){
	rows = [];
	for(var i  in rowChos){
		rows = rows.concat(rowChos[i]);
	}
	var q = rows.length;
	if(q == 0){
		$.fool.alert({msg:"请选择需要打印的项！",fn:function(){
			return false;
		}});
	}
	var pageRow = 5;
	$.ajax({
		url:"${ctx}/payBill/findTemp?code=skd&num="+Math.random(),
		async:false,
		data:{},
		success:function(data){
			if(data){
				pageRow = parseInt(data.message) > 14 ? 14 : parseInt(data.message);
			}else{
				$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
					return false;
				}});
			}
		}
	});
	var nullRow = q%pageRow != 0?pageRow - q%pageRow:0;
	var ids = [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].fid);
	}
	var billIds = ids.join(',');
	texts = [
	        {title:'单据日期',key:'billDate'},
	        ];
	             
	thead = [
			{title:'银行账号',key:'bankAccount'},
			{title:'客户名称',key:'customerName'},
			{title:'金额',key:'amount'},
	        ];
	printHandle(getRootPath()+"/payBill/printMulti/skd?nullRow="+nullRow+"&billIds="+billIds,0,pageRow);
});

$('#tranToBankBill').click(function(){
    rows = [];
    for (var i  in rowChos) {
        rows = rows.concat(rowChos[i]);
    }
    var q = rows.length;
    if (q == 0) {
        $.fool.alert({msg:"请选择需要生成的项！",fn:function(){
            return false;
        }});
    } else {
        var ids = [];
        for (var i=0; i<rows.length; i++) {
            ids.push(rows[i].fid);
        }
        var billIds = ids.join(',');
        $.post('${ctx}/payBill/save/as/bankBill',{"billIds": billIds},function(data){
            dataDispose(data);
            if(data.returnCode =='0'){
                var failTotal = 0;
                var failMsg = "";
                if (data.data) {
                    for (var key in data.data) {
                        failTotal += 1;
                        failMsg += '单号' + key + ':' + data.data[key] + '</br>';
                    }
                }
                if (failTotal == 0) {
                	$.fool.alert({time:3000,msg:"操作成功"});
				} else {
                	$.fool.confirm({title: failTotal + "条失败", msg:failMsg})
				}
                $('#billList').trigger('reloadGrid');
            } else if(data.returnCode == 1) {
                $.fool.alert({msg:data.message});
            } else {
                $.fool.alert({msg:"系统正忙，请稍后再试。"});
            }
        });
    }
});

var options = [
    		    {id:'0',name:'未审核'},
    		    {id:'1',name:'已审核'},
    		    {id:'2',name:'已作废'}
    		];
$('#billList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:"${ctx}/receiveBill/list",
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
	multiselect:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	autowidth:true,//自动填满宽度
	height:$(window).outerHeight()-200+"px",
	gridComplete:function(){//列表权限控制
		<fool:tagOpt optCode="skdAction1">//</fool:tagOpt>$('.btn-edit').remove();
		<fool:tagOpt optCode="skdAction2">//</fool:tagOpt>$('.btn-del').remove();
		<fool:tagOpt optCode="skdAction3">//</fool:tagOpt>$('.btn-copy').remove();
		<fool:tagOpt optCode="skdAction4">//</fool:tagOpt>$('.btn-approve').remove();
		<fool:tagOpt optCode="skdAction5">//</fool:tagOpt>$('.btn-cancel').remove();
		<fool:tagOpt optCode="skdAction6">//</fool:tagOpt>$('.btn-detail').remove();
	    	  warehouseAll();
	    	  if($('#billList').jqGrid("getGridParam").postData.rows != rowNum){
	  			pgChos= [];
	  			rowChos= [];
	  	   		rowNum = $('#billList').jqGrid("getGridParam").postData.rows;
	  	   	}else{
	  	   		var num = $('#billList').jqGrid('getGridParam').page;
	  			if(pgChos[num]){
	  				var rowids = pgChos[num];
	  				for(var i = 0; i < rowids.length; i++){
	  					$('#billList').jqGrid('setSelection',rowids[i],false);
	  				}
	  			}
	  	   	}
	},
	onSelectAll:function(aRowids,status){
		var num = $('#billList').jqGrid('getGridParam').page;
		if(status){
			var rowids = $('#billList').jqGrid('getDataIDs')
			pgChos[num] = rowids;
			var rows = $('#billList').jqGrid("getRowData");
			/* for(var i=0;i<rowids.length;i++){
				var row = $('#goods-table').jqGrid("getRowData",rowids[i]);
				rows.push(row);
			} */
			rowChos[num] = rows;
		}else{
			pgChos[num] = [];
			rowChos[num] = [];
		}
	},
	onSelectRow:function(rowid,status){
		var rowids = $('#billList').jqGrid('getGridParam', 'selarrrow');
		var num = $('#billList').jqGrid('getGridParam').page;
		pgChos[num] = rowids;
		var rows = [];
		for(var i=0;i<rowids.length;i++){
			var row = $('#billList').jqGrid("getRowData",rowids[i]);
			rows.push(row);
		}
		rowChos[num] = rows;
	},
	colModel:[
	  		{name:'fid',label:'fid',align:"center",hidden:true,width:100}, 
	  		{name:'code',label:'单号',align:"center",sortable:true,width:100<fool:tagOpt optCode="skdAction1">,formatter:function(value,options,row){
	  			return '<a title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\',\''+row.recordStatus+'\')">'+value+'</a>';
	  		}</fool:tagOpt>},
	  		{name:'customerName',sortable:true,label:'客户名称',align:"center",width:100},	  		
	  		{name:'memberName',label:'收款人',align:"center",sortable:true,width:100},
	  		{name:'amount',label:'金额',align:"center",width:60,sortable:true,formatter:function(value){
	  			if(value=="0E-8"){
	  				return 0;
	  			}else{
	  				return value;
	  			}
	  		}},
	  		{name:'totalUnCheckAmount',label:'未对单金额',align:"center",width:100,sortable:true,formatter:function(value){
	  			if(value=="0E-8"||!value){
	  				return 0;
	  			}else{
	  				return value;
	  			}
	  		}},
	  		{name:'billDate',sortable:true,label:'单据日期',align:"center",width:40,formatter:function(value){
	  			return value.substring(0,10);
	  		}},
	  		{name:'recordStatus',label:'状态',align:"center",width:40,sortable:true,formatter:function(value){
				for(var i=0; i<options.length; i++){
					if (options[i].id == value) return options[i].name;
				}
				return value;
			}},
			{name:'saveAsBankBillOperatorId',label:'是否生成银行单',align:"center",sortable:true,width:40,formatter:function(value){
				if (value) return "已生成";
				return "未生成";
			}},
			<fool:tagOpt optCode="skdAction">
	  		{name:'action',label:'操作',align:"center",width:100,formatter:function(value,options,row){
	  			if(row.recordStatus==0){
	  				var statusStr = '';
	  				/*  statusStr += '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+row.fid+'\')"></a> '; */
		  			 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			 statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+row.fid+'\')"></a> ';
		  			 statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
		  			 return statusStr ; 
	  			}else if(row.recordStatus==1){
	  				var statusStr = '';
		  			/*  statusStr += '<a class="btn-detail" title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\',\'1\')"></a> '; */
                    if (!row.saveAsBankBillOperatorId) {
                        statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
                    }
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			 return statusStr ; 
	  			}else if(row.recordStatus==2){
	  				var statusStr = '';
	  				/*  statusStr += '<a class="btn-detail" title="查看" href="javascript:;" onclick="editById(\''+row.fid+'\',\'1\')"></a> '; */
		  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  			 return statusStr ; 
	  			}
	  		}}
	  		</fool:tagOpt>
	      ]
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function resetPostdata() {
    var postdata = $('#billList').getGridParam('postData');
    var clearform = $('#search-form').serializeJson();
    for(var prop in clearform){
        if(prop in postdata)
            postdata[prop]="";
    }
}
$("#clear-btn").click(function(){
	$("#search-form").form("clear");
	var dhxDiv = $('#search-form .dhxDiv');
	for(var i=0;i<dhxDiv.length;i++){
        dhxDiv[i].comboObj.setComboText("");
        dhxDiv[i].comboObj.setComboValue("");
	}
    resetPostdata();
});

$("#search-btn").click(function (){
	 search();
})

function search(){
	/*var searchKey=$("#search-code").textbox('getValue');
	var startDay=$("#search-startDay").datebox('getValue');
	var endDay=$("#search-endDay").datebox('getValue');
	var customerId=$("#search-cusId").val();
	var memberId=$("#search-memberId").val();
    var voucherCode=$("#search-voucherCode").textbox('getValue'); 
	var options = {"searchKey":searchKey,"startDay":startDay,"endDay":endDay,"customerId":customerId,"memberId":memberId}; */
	$('#search-code').textbox('setValue','');
	var options=$("#search-form").serializeJson();
	options.searchKey="";
	typeof options.recordStatus == "undefined"?options.recordStatus = "":null;//由于取消选择状态后，options没有录入参数recordStatus，但jqGrid的GridParam依然存在，所以设置一下
	typeof options.saveStatus == "undefined"?options.saveStatus = "":null;
	$('#billList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	pgChos=[];
    rowChos=[];
}

//编辑 
/* function editById(fid,mark){
	var title = "修改收款单";
	if(mark==1){
		title = "查看收款单"
	}
	$('#addBox').window({
		title:title, 
		top:10,  
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/receiveBill/edit?fid="+fid,
		width:$(window).width()-200,
		//height:$(window).height()-300,
		onClose:function(){
			if($("#checkBox").html()){
				$("#checkBox").window("destroy");
			}
		}
	});
}  */

//编辑 、查看
function editById(fid,recordStatus,detail){
	var title="";
	var url="";
		if(recordStatus==1||recordStatus==2){//已审核已作废只能查看
			title = "查看收款单";
			url="${ctx}/receiveBill/edit?fid="+fid+"&flag=detail";		
		}else if(recordStatus==0){//状态为未审核可以修改
			title = "编辑收款单";
			url="${ctx}/receiveBill/edit?fid="+fid+"&flag=edit"; 
		}
		/* $('#addBox').window({
			title:title, 
			top:10,  
			modal:true,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false, 
			href:url,
			width:$(window).width()-10,
			height:$(window).height()-60,
			onClose:function(){
				if($("#checkBox").html()){
					$("#checkBox").window("destroy");
				}
			},
			onOpen:function(){
				$(this).parent().prev().css("display","none");
			}
		}); */
		warehouseWin(title,url);
}


//复制
function copyById(fid){
	/* $('#addBox').window({
		title:'复制收款单', 
		top:10,  
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		href:"${ctx}/receiveBill/edit?fid="+fid+"&mark=1&flag=copy",
		width:$(window).width()-10,
		height:$(window).height()-60,
		onClose:function(){
			if($("#checkBox").html()){
				$("#checkBox").window("destroy");
			}
		},
		onOpen:function(){
			$(this).parent().prev().css("display","none");
		}
	}); */
	var url = "${ctx}/receiveBill/edit?fid="+fid+"&mark=1&flag=copy";
	warehouseWin("复制收款单",url);
	/* window.location.href="${ctx}/receiveBill/edit?fid="+fid+"&mark=1&flag=copy"; */
}

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/receiveBill/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else if(data.result=='1'){
				    		$.fool.alert({msg:data.msg});
				    		$('#save').removeAttr("disabled");
			    		}else{
							$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!",fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:data.msg});
		    		}
				});
		 }
	 }});
}

function verifyById(fid){
	$.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/receiveBill/passAudit',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {
						/* dataDispose(data); */
						/* if(typeof data.result != "undefined"){
							data.result = data.returnCode;
							data.msg = data.message;
						}else if(typeof data.returnCode != "undefined"){
							data.returnCode = data.result;
							data.message = data.msg;
						} */
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.message,fn:function(){
					    		}});
				    		}
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

function cancelById(fid) {
	 $.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/receiveBill/cancel',
					data : {fid :fid},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.msg,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:data.msg,fn:function(){
					    		}});
				    		}
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});

};

//新版领料员输入显示
var boxWidth=160,boxHeight=31;//统一设置输入框大小
var smemCombo = $('#search-member').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	width:boxWidth,
	height:boxHeight,
	data:memberData,
	focusShow:true,
//     onlySelect:true,
	filterUrl:getRootPath()+'/member/list',
	toolsBar:{
		refresh:true
	},
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
	onChange:function(value,text){
		$("#search-memberId").val(value);
	}
});
/* $('#search-member').combogrid('textbox').focus(function(){
	$('#search-member').combogrid('showPanel');
}); */

var mybankCombo = $('#search-bankAccount').fool('dhxComboGrid',{
	focusShow:true,
	width:boxWidth,
	height:boxHeight,
	data:mybankData,
	//hasDownArrow:false,
	filterUrl:getRootPath()+'/bankController/list?num='+Math.random(),
	searchKey:"keyWord",
	toolsBar:{
		refresh:true
	},
	setTemplate:{
		input:"#name#(#account#)",
		columns:[
				{option:'#code#',header:'编号',width:80},
				{option:'#name#',header:'名称',width:80},
				{option:'#bank#',header:'开户行',width:80},
				{option:'#account#',header:'账号',width:200},
			    ],
	},
	onChange:function(value,text){
		value!=null?$("#search-bankId").val(value):null;
	},
});
var scusCombo = $("#search-cusName").fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	width:boxWidth,
	height:boxHeight,
	data:scusData,
	focusShow:true,
//     onlySelect:true,
	filterUrl:getRootPath()+'/customer/list?showDisable=0',
	toolsBar:{
		refresh:true
	},
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
  	},
	onChange:function(value,text){
		$("#search-cusId").val(value);
	}
});
/* $('#search-cusName').combogrid('textbox').focus(function(){
	$('#search-cusName').combogrid('showPanel');
}); */

/* if($('#search-supplierName').length > 0)//供应商
 $('#search-supplierName').combogrid('textbox').focus(function(){
	$('#search-supplierName').combogrid('showPanel');
}); */

/* var chooser="";
$("#search-cusName").textbox('textbox').click(function(){
	chooser=$.fool.window({'title':"选择客户",width:780,height:480,href:'${ctx}/customer/window?okCallBack=selectCusSearch&onDblClick=selectCusSearchDBC&singleSelect=true'}); 
});

$("#search-member").textbox('textbox').click(function(){
	chooser=$.fool.window({'title':"选择收款人",width:780,height:480,href:'${ctx}/member/window?okCallBack=selectUserSearch&onDblClick=selectUserSearchDBC&singleSelect=true'});
});


function selectCusSearch(rowData){
	$("#search-cusId").val(rowData[0].fid);
	$("#search-cusName").textbox('setValue',rowData[0].name);
	chooser.window('close');
}
function selectCusSearchDBC(rowData){
	$("#search-cusId").val(rowData.fid);
	$("#search-cusName").textbox('setValue',rowData.name);
	chooser.window('close');
}

function selectUserSearch(rowData){
	$("#search-memberId").val(rowData[0].fid);
	$("#search-member").textbox('setValue',rowData[0].username);
	chooser.window('close');
}
function selectUserSearchDBC(rowData){
	$("#search-memberId").val(rowData.fid);
	$("#search-member").textbox('setValue',rowData.username);
	chooser.window('close');
}

$(function(){
	//分页条 
	//setPager($('#billList'));
}); */
enterSearch("Inquiry"); //回车搜索
if("${param.id}"){
	editById("${param.id}","${param.recordStatus}");
}
if("${param.billId}"){
	editById("${param.billId}",1);
}
</script>
</body>
</html>