<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>采购退货单据信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
#search-form span{
  margin-right: 5px;
  margin-bottom: 10px
}
#search-form a{
  margin-right: 5px;
  margin-bottom: 10px
}
#Inquiry{
  margin-left: 5px;
}
#grabble{
  float: right;
}
#bolting{
  width: 160px;
  height: 27px;
  position:relative; 
  border: 1px solid #ccc; 
  background: #fff;
  margin-right: 25px;
}
.button_a{
  display:block; 
  width:25px; 
  height:25px;
  background:#76D5FC; 
  -moz-border-radius: 3px;
  /* Gecko browsers */-webkit-border-radius: 3px;
  /* Webkit browsers */border-radius:3px;
  /* W3C syntax */float: right; 
  right:29px; 
  top:63px; 
  position: absolute;
}
.button_span{
  width:0; 
  height:0; 
  border-left:8px solid transparent; 
  border-right:8px solid transparent;
  border-top:8px solid #fff;
  top:10px; 
  position: absolute;
  right:5px;
}
.input_div{
  display: none;
  background:#F5F5F5; 
  padding: 10px 0px 5px 0px; 
  border: 1px solid #D5DBEA;
  position: absolute;
  right: 23px; 
  top:93px;
  z-index: 1;
}
.input_div p{ 
  font-size: 12px; 
  color:#747474;
  vertical-align: middle;
  text-align: right;  
  margin: 0 20px 0 10px;
  width:240px;
}
.button_clear{ 
  border-top: 1px solid #D5DBEA;
  margin-top: 10px; 
  padding-top:8px;
  text-align: right;
}
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
                   <h1>采购退货单</h1>
                </div>             
             </div>
	<div id="addBox"></div> 
	<div style="margin:5px 0px 10px 0px;">
		<fool:tagOpt optCode="cgthAdd">
		<a href="javascript:;" id="addNew" class="btn-ora-add" style="vertical-align: top;">新增</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="cgthSearch">
		  <input id="search-code"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	      <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	      <div class="input_div">
	        <form id="search-form">
	          <p>开始日期：<input id="search-startDay" name="startDay" /></p>
	          <p>结束日期：<input id="search-endDay" name="endDay"/></p>
	          <p>仓库:<input id="search-warehouse" name="inWareHouseId" _class="warehouse"/></p><!-- <input id="search-status"/> -->
	          <p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	          <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
	          <p>采购员：<input id="select-inMemberName" _class="inmember-combogrid" name="inMemberName" class="textBox"/><input type="hidden" name="inMemberId" id="select-inMemberId" /></p>
	         <!--  <p>制单人：<input id="select-people" name="creatorName" class="textBox"/><input type="hidden" name="creatorId" id="select-peopleid" /></p> -->
	          <p>供应商：<input id="select-supplierName" _class="supplier-combogrid" name="supplierName" class="textBox"/><input type="hidden" name="supplierId" id="select-supplierId" /></p>
	          <p>货品：<input id="select-goods" _class="goods-combogrid" name="goodsName"><input id="select-goodsid" type="hidden" name="goodsId"/></p>
	          <div class="button_clear">
	            <a href="javascript:;" id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	            <a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	            <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	          </div>
	        </form>
	      </div>
		</fool:tagOpt>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
	<div id="mybox"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
initManage("cgth","采购退货");
//设置仓库
//$('#search-warehouse').fool('memberDeptComBoxTree',{width:160,'valTarget':$("#search-warehouse")}); 


var chechbox='';
var decide=true;
//新版领料员输入显示
$(":checkbox").click(function(){//点击复选框并且只让一个选中
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	 chechbox=$(this).val();
});

$("#search-btn").click(function(){
	refreshData();
});

enterSearch("Inquiry");//回车搜索


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

//设置鼠标放进去自动出来下拉列表
$("#search-form").find("input[_class]").each(function(i,n){($(this));});
/* $("#select-people").fool('combogrid',{
	required:true,
	novalidate:true,
	width:boxWidth,
	height:boxHeight,
	idField:'fid',
	textField:'username',
	panelWidth:450,
	fitColumns:true,
	url:getRootPath()+"/userController/userList",
	columns:[[
		{field:'fid',title:'fid',hidden:true},
		{field:'userCode',title:'编号',width:100,searchKey:false},
		{field:'userName',title:'名称',width:100,searchKey:false},
		{field:'searchKey',hidden:true,searchKey:true},
	          ]],
	onSelect:function(index,row){
		$("#select-peopleid").val(row.fid);
		$("#select-people").val(row.username).focus();
	}	
});
$('#select-people').combogrid('textbox').focus(function(){
	$('#select-people').combogrid('showPanel');
}); */


$("#search-startDay").fool('datebox',{
	prompt:'开始日期',
	width:165,
	height:32,
	inputDate:true,
	editable : true
});

$("#search-endDay").fool('datebox',{
	prompt:'结束日期',
	width:165,
	height:32,
	inputDate:true,
	editable : true
});
$("#clear-btn").click(function(){
	cleanBoxInput($("#search-form"));
}); 

$('#billList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/purchaseback/cgth/list',
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
	  		{name:'fid',label:'fid',align:"center",hidden:true,width:100},
	  		{name:'totalAmount',label:'totalAmount',align:"center",hidden:true,width:100},
	  		{name:'code',label:'单号',align:"center",sortable:true,width:100
	  		<fool:tagOpt optCode="cgthAction1">,
	  		formatter:codeLinkNew
	  		</fool:tagOpt>
	  		},
	  		{name:'voucherCode',align:"center",label:'原始单号',sortable:true,width:100},
	  		{name:'billDate',label:'单据日期',align:"center",sortable:true,width:100,formatter:dateFormatAction},
	  		{name:'supplierName',label:'供应商名称',align:"center",sortable:true,width:100},
        	{name:'supplierCode',label:'供应商编号',align:"center",sortable:true,width:100},
	  		{name:'supplierPhone',label:'供应商电话',align:"center",width:100},
	  		{name:'deptName',label:'部门',align:"center",width:100},
	  		{name:'inWareHouseName',label:'退货仓库',align:"center",width:100},
	  		{name:'recordStatus',label:'状态',align:"center",sortable:true,width:100,formatter:recordStatusAction},	  	
	  		<fool:tagOpt optCode="cgthAction">
	  		{name:'action',label:'操作',align:"center",width:100,formatter:function(value,options,row){
	  			var d='',c='',s='',b='';
	  			<fool:tagOpt optCode="cgthAction2">
	  			   d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
	  			 </fool:tagOpt>
	  			<fool:tagOpt optCode="cgthAction3">
	  			   c='<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
	  			</fool:tagOpt>
	  			 <fool:tagOpt optCode="cgthAction4">
	  			   s='<a class="btn-approve" title="审核" href="javascript:;" onclick="passAuditById(\''+row.fid+'\')"></a> ';
	  			 </fool:tagOpt>
	  			<fool:tagOpt optCode="cgthAction5">
	  			   b='<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
	  			 </fool:tagOpt>
	  			 switch(row.recordStatus){
	  			   case 0:
	  				   return d+c+s+b;
	  				   break;
	  			   case 1:
	  			       return c+b;
	  			       break;
	  			   case 2:
	  				   return c;
	  				   break;
	  			 }
	  		}}
	  		</fool:tagOpt>
	      ],
	      gridComplete:function(){
	    	  warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

//编辑 
function editById(fid,mark){
	var url="${ctx}/warehouse/cgth/edit?billCode=cgth&id="+fid+"&flag=edit";
	if(mark==1){
		url="${ctx}/warehouse/cgth/edit?billCode=cgth&id="+fid+"&flag=detail";
	}
	warehouseWin("编辑采购退货单",url)
} 
//复制
function copyById(fid){
	var url="${ctx}/warehouse/cgth/edit?billCode=cgth&id="+fid+"&mark=1&flag=copy";
	warehouseWin("复制采购申请单",url)
}
//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/purchaseback/cgth/delete',
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
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

function userCheck(data,fid){
	passAuditById(fid,true,data);
}

function myPass(mydata){
	$.fool.confirm({msg:'是否审核该单据？',fn:function(r){
		if(r){
			$.ajax({
				type : 'post',
				url : '${ctx}/purchaseback/cgth/passAudit',
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
										passAuditById(mydata.id,true);
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
					$.fool.alert({msg:"系统繁忙，稍后再试!"});
				}
			});
		}
	}})
}
//审核
function passAuditById(fid,bool,data){ 
	var mydata = {id : fid};
	if("undefined" != typeof bool){
		if(bool){
			"undefined" != typeof data?
			mydata=$.extend(mydata,data,{coerceOutStock:1}):mydata=$.extend(mydata,{coerceOutStock:1});
		}
		if(queryByWarehouse(fid)){
			if(!checkPlanAmount(fid,$("td[title='"+fid+"']").siblings("[aria-describedby='billList_totalAmount']").text())){
				$.messager.defaults={ok:"编辑",cancel:"不编辑"};
				$.fool.confirm({msg:'资金计划的金额不等于单据金额，是否编辑资金计划？',fn:function(r){
					if(r){
						//todo
						viewRowNew(fid,0,zjjh,[fid]);
						/*zjjh(value);*/
					}
				}})
				$.messager.defaults={ok:"确定",cancel:"取消"};
				return false;
			}else{
				myPass(mydata);
			}
		}else{
			$.messager.defaults={ok:"编辑",cancel:"不编辑"};
			$.fool.confirm({msg:'暂无资金计划记录，是否编辑资金计划？',fn:function(r){
				if(r) {
					viewRowNew(fid,0,zjjh,[fid]);
					/*zjjh(value);*/
				}else{
					capitalPassAudit(fid,bool,data);
				}
			}});
			$.messager.defaults={ok:"确定",cancel:"取消"};
		};
	}else{
		if(queryByWarehouse(fid)){
			if(!checkPlanAmount(fid,$("td[title='"+fid+"']").siblings("[aria-describedby='billList_totalAmount']").text())){
				$.messager.defaults={ok:"编辑",cancel:"不编辑"};
				$.fool.confirm({msg:'资金计划的金额不等于单据金额，是否编辑资金计划？',fn:function(r){
					if(r){
						//todo
						viewRowNew(fid,0,zjjh,[fid]);
						/*zjjh(value);*/
					}
				}})
				$.messager.defaults={ok:"确定",cancel:"取消"};
				return false;
			}else{
				myPass(mydata);
			}
		}else{
			$.messager.defaults={ok:"编辑",cancel:"不编辑"};
			$.fool.confirm({msg:'暂无资金计划记录，是否编辑资金计划？',fn:function(r){
				if(r) {
					viewRowNew(fid,0,zjjh,[fid]);
					/*zjjh(value);*/
				}else{
					capitalPassAudit(fid,bool,data);
				}
			}});
			$.messager.defaults={ok:"确定",cancel:"取消"};
		};
	}
}
//废除
function cancelById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要废除该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/purchaseback/cgth/cancel',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.returnCode == "0"){
							$.fool.alert({time:1000,msg:"作废成功！",fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}
						else{
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
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

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
		data : {relationId :value,relationSign:getBillSign(_billCode)},
		dataType : 'json',
		async:false,
		success : function(returnDate) {
			if(returnDate.returnCode==0){
				myPass(mydata);
			}else{
				$.fool.alert({msg:returnDate.message});
			}
		}
	});
}

if("${param.billId}"){
	viewRowNew("${param.billId}");
}
</script>
</body>
</html>
