<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>销售出货信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<script type="text/javascript" src="${ctx}/resources/js/lib/dhtmlx/codebase/dhtmlxcommon.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lib/dhtmlx/codebase/dhtmlxcombo.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/js/lib/dhtmlx/codebase/dhtmlxcombo.css" />
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

#add{
  vertical-align: top;
  /* margin-top: 5px; */
}
#search-form{
  display: inline-block;
  width:98%;
 /*  border: 1px solid red; */
}
#search-form .textbox{
  margin:5px 5px 0px 0px
}
#search-form .btn-s{
  margin:5px 5px 0px 0px;
  vertical-align: middle;
}

#btnBox{
  text-align: center;
}

.form p.hideOut,.form1 p.hideOut{
    display: none;
  }
.form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{width:115px;}
  
#userChooser,#customerChooser,#goodsChooser,#goodsSpecChooser{
  display: none;

}

#userSearcher,#cusSearcher,#goodsSearcher,#goodsSpecSearcher{
  text-align: left;
  margin:10px
}
#Inquiry{margin-left: 5px;}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 25px;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:29px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:5px;}
.input_div{display: none;background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:93px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px;width:235px;}
.button_clear{ border-top: 1px solid #D5DBEA;margin-top: 10px; text-align: right;}
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


</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>销售出货单</h1>
                </div>             
             </div>
	<div id="addBox"></div>
	<div style="margin:10px 0px 10px 0px">
	<div>
	<fool:tagOpt optCode="xschAdd">
	  <a href="javascript:;" id="addNew" class="btn-ora-add" >新增</a> 
	</fool:tagOpt>
	  <fool:tagOpt optCode="xschSearch">
	  <input id="search-code"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	  <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	 </div>	
	 <div class="input_div" style="z-index:9999;">   
	<form id="search-form">	  	 	  	  
	  <p>开始日期：<input id="search-startDay" name="startDay"/></p>
	  <p>结束日期：<input id="search-endDay" name="endDay"/></p>
	  <p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	  <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
	  <p>客户：<input id="search-cusName" _class="customer-combogrid" name="customerName"/><input id="search-cusId" name='customerId' type="hidden"/></p>
	  <p>货品：<input id="search-goods" _class="goods-combogrid" name="goodsName"/><input id="search-goodsId" type="hidden" name="goodsId"/></p>
	 <!--  <p>制单人：<input id="search-creater" name="creatorName"/><input id="search-createrId" type="hidden" name="creatorId" /></p> -->
	  <p>业务员：<input id="myInMemberName" name="inMemberName" class="textBox" /><input type="hidden" name="inMemberId" id="myInMemberId" /></p>
	  <div class="button_clear">
	  <a href="javascript:;" id="search-btn" class="btn-blue btn-s">查询</a>
	  <a href="javascript:;" id="clear-btn" class="btn-blue btn-s">清空</a>
	  <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	  </fool:tagOpt>
	  </div>	 
	</form>
	 </div>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
	<div id="mybox"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}">
</script><script type="text/javascript">
initManage("xsch","销售出货");
var chechbox='';
var decide=true;
$(":checkbox").click(function(){//点击复选框并且只让一个选中
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	 chechbox=$(this).val();
});


enterSearch("Inquiry");
var goodsSpecChooserOpen=false;
var goodsChooserOpen=false;

/* $('#addBox').window({
	top:10,
	left:0,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false, 
	width:$(window).width()-10,
	height:$(window).height()-60,
	closed:true,
	modal:true,
	onOpen:function(){
		$(this).parent().prev().css("display","none");
	}
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

    		
var pageNumber=1;
$('#billList').jqGrid({
	//url:'${ctx}/saleOutStock/xsch/list',
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/saleOutStock/xsch/list',
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
	autowidth:true,//自动填满宽度
	height:"100%",
	mtype:"post",
	pager:'#pager',
	rowNum:10,
	rowList:[ 10, 20, 30 ],
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	viewrecords:true,
	forceFit:true,//调整列宽度，表格总宽度不会改变
	height:$(window).outerHeight()-200+"px",
	colModel:[
		  		{name:'fid',label:'fid',hidden:true,width:100}, 
		  		{name:'code',label:'单号',align:"center",sortable:true,width:100,<fool:tagOpt optCode="xschAction1">formatter:codeLinkNew</fool:tagOpt>},
		  		{name:'voucherCode',align:"center",label:'原始单号',width:100},
        		{name:'billDate',align:"center",label:'单据日期',width:40,sortable:true,formatter:dateFormatAction},
		        {name:'customerName',align:"center",label:'客户名称',width:100},
		  		{name:'inMemberName',align:"center",label:'业务员',width:100},
		  		{name:'totalAmount',align:"center",label:'合计金额',width:40,sortable:true,formatter:function(value){
		  			if(value=="0E-8"){
		  				return 0;
		  			}else{
		  				return value;
		  			}
		  		}},
		  		{name:'recordStatus',align:"center",label:'状态',width:40,sortable:true,formatter:recordStatusAction},
				<fool:tagOpt optCode="xschAction">
		  		{name:'action',align:"center",label:'操作',formatter:function(value,options,row){
		  			if(row.recordStatus==0){
		  				var statusStr = '';
			  			<fool:tagOpt optCode="xschAction2">
		  				 statusStr += '<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
		  				</fool:tagOpt>
		  				<fool:tagOpt optCode="xschAction3">
		  				 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
		  				</fool:tagOpt>
		  				<fool:tagOpt optCode="xschAction4">
		  				 statusStr += '<a class="btn-approve" title="审核" href="javascript:;" onclick="verifyById(\''+row.fid+'\')"></a> ';
		  				</fool:tagOpt>
		  				<fool:tagOpt optCode="xschAction5">
		  				 statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
		  				</fool:tagOpt>
			  			 return statusStr ; 
		  			}else if(row.recordStatus==1){
		  				var statusStr = '';
		  				<fool:tagOpt optCode="xschAction5">
			  			 statusStr += '<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
			  			</fool:tagOpt>
			  			<fool:tagOpt optCode="xschAction3">
			  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
			  			</fool:tagOpt>
			  			 return statusStr ; 
		  			}else if(row.recordStatus==2){
		  				var statusStr = '';
		  				<fool:tagOpt optCode="xschAction3">
			  			 statusStr += '<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')" ></a> ';
			  			</fool:tagOpt>
			  			 return statusStr ; 
		  			}
		  		}}
		  		</fool:tagOpt>
		      ],
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

$("#clear-btn").click(function(){
	cleanBoxInput($("#search-form"));
});

$("#search-btn").click(function (){
	refreshData();
});

//编辑 
function editById(fid,recordStatus,fn,args){
	//编辑 
	var url="";
	if(recordStatus==1||recordStatus==2){//已审核已作废只能查看
		url="${ctx}/saleOutStock/xsch/edit?billCode=xsch&id="+fid+"&flag=detail";		
	}else if(recordStatus==0){//状态为未审核可以修改
		url="${ctx}/saleOutStock/xsch/edit?billCode=xsch&id="+fid+"&flag=edit"; 
	}
	if(fn){
		warehouseWin("编辑销售出货单",url,fn,args);
	}else{
		warehouseWin("编辑销售出货单",url);
	}
} 

//复制
function copyById(fid){
	var url="${ctx}/saleOutStock/xsch/edit?mark=1&billCode=xsch&id="+fid+"&flag=copy";
	warehouseWin("复制销售出货单",url);
}

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/saleOutStock/xsch/delete',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}
function userCheck(data,fid){
	verifyById(fid,true,data);
}

function myPass(mydata){
	$.ajax({
		type : 'post',
		url : '${ctx}/saleOutStock/xsch/passAudit',
		data : mydata,
		dataType : 'json',
		success : function(data) { 
			dataDispose(data);
			if(data.returnCode == "0"){
				$.fool.alert({time:1000,msg:"审核成功！",fn:function(){
					$('#billList').trigger("reloadGrid");
				}});
			}
			else if(data.returnCode == 1){
				if(data.errorCode == 201){
					if(data.dataExt.hasUnlockPermission == 1){
						$.fool.confirm({title:'库存提示',msg:"库存需解锁，是否解锁？",fn:function(bool){
							if(bool){
								verifyById(mydata.id,true);
							}else{
								return false;
							}
						}});
					}else if(data.dataExt.hasUnlockPermission == 0){
						$('#mybox').fool('window',{
							top:300,
							left:$(window).width()/2-150,
							modal:true,
							width:300,
							height:150,
							title:"库存锁定权限验证",
							href:"${ctx}/indexController/userCheck?callBack=userCheck&fid="+mydata.id
						});
					}
				}else if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
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
function verifyById(fid,bool,data){
	var mydata = {id : fid};
	if("undefined" != typeof bool){
		if(bool){
			"undefined" != typeof data?
			mydata=$.extend(mydata,data,{coerceOutStock:1}):mydata=$.extend(mydata,{coerceOutStock:1});
		}
	}
	if(queryByWarehouse(fid)){
		if(!checkPlanAmount(fid,$("td[title='"+fid+"']").siblings("[aria-describedby='billList_totalAmount']").text())){
			$.messager.defaults={ok:"编辑",cancel:"不编辑"};
			$.fool.confirm({msg:'资金计划的金额不等于单据金额，是否编辑资金计划？',fn:function(r){
				if(r){
					//todo
					editById(fid,0,zjjh,[fid]);
					/*zjjh(value);*/
				}
			}})
			$.messager.defaults={ok:"确定",cancel:"取消"};
			return false;
		}else{
			$.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
				 if(r){
					 myPass(mydata);
				 }
			}});
		}
	}else{
		$.messager.defaults={ok:"编辑",cancel:"不编辑"};
		$.fool.confirm({msg:'暂无资金计划记录，是否编辑资金计划？',fn:function(r){
			if(r) {
				editById(fid,0,zjjh,[fid]);
				/*zjjh(value);*/
			}else{
				capitalPassAudit(fid,bool,data);
			}
		}});
		$.messager.defaults={ok:"确定",cancel:"取消"};
	};
}

function cancelById(fid) {
	 $.fool.confirm({title:'确认',msg:'确定要作废此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/saleOutStock/xsch/cancel',
					data : {id :fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							dataDispose(data);
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
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

};



//设置鼠标放进去自动出来下拉列表
$("#search-form").find("input[_class]").each(function(i,n){($(this));});

function queryByWarehouse(relationId){
	var result=false;
	$.ajax({
		type : 'post',
		url : getRootPath()+"/capitalPlan/queryByWarehouse",
		data : {relationId :relationId},
		dataType : 'json',
		async:false,
		success : function(data) {
			if(data){
				result=true;
			}
		}
	});
	return result;
}

function checkPlanAmount(relationId,billAmount){
	var result=false;
	$.ajax({
		type : 'post',
		url : getRootPath()+"/capitalPlan/checkPlanAmount",
		data : {relationId :relationId,billAmount:billAmount},
		dataType : 'json',
		async:false,
		success : function(returnDate) {
			if(returnDate.returnCode==0){
				result=true;
			}else{
				result=false;
			}
		}
	});
	return result;
}

function capitalPassAudit(value,bool,data){
	var mydata = {id : value};
	if("undefined" != typeof bool){
		if(bool){
			"undefined" != typeof data?
			mydata=$.extend(mydata,data,{coerceOutStock:1}):mydata=$.extend(mydata,{coerceOutStock:1});
		}
	}
	$.ajax({
		type : 'post',
		url : getRootPath()+"/capitalPlan/capitalPassAudit",
		data : {relationId :value,relationSign:41},
		dataType : 'json',
		async:false,
		success : function(returnDate) {
			if(returnDate.returnCode==0){
				$.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
					 if(r){
						 myPass(mydata);
					 }
				}});
			}else{
				$.fool.alert({msg:returnDate.message});
			}
		}
	});
}

//资金计划
function zjjh(id){
	$('#scancel').length>0?$('#scancel').click():null;
	win = $("#pop-win").fool('window',{modal:true,'title':"资金计划",height:480,width:780,href:getRootPath()+'/capitalPlanDetail/window?relationId='+id+'&relationSign=41&billType=xsch'});
}
if("${param.billId}"){
	editById("${param.billId}",1);
}
</script>
</body>
</html>
