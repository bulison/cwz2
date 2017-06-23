<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>生产计划单</title>
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
                   <h1>生产计划单</h1>
                </div>             
             </div>
	<div id="addBox">
	</div>
	<div id="myBox">
	  <p style="margin:20px">生产状况：<input id="productionStatus-setter" /><a id="setter" class="btn-blue btn-s" style="vertical-align: middle;margin-left:10px ">确定</a><input id="fid-setter" style="display: none;"/></p>
	</div> 
	<div style="margin:5px 0px 10px 0px;">
		<a href="javascript:;" id="addNew" class="btn-ora-add" style="vertical-align: top;">新增</a>
		  <input id="search-code"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	      <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly" /><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	      <div class="input_div">
	        <form id="search-form">
	          <p>开始日期：<input id="search-startDay" name="startDay" /></p>
	          <p>结束日期：<input id="search-endDay" name="endDay"/></p>
	          <p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	          <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
	          <p>生产状况：<input id="select-productionStatus" name="productionStatus" class="textBox"/>
	          <p>部门：<input name="deptId" _class="deptComBoxTree" id="select-deptId"/></p>
	          <p>订货客户：<input id="select-cusName" _class="customer-combogrid" name="customerName" class="textBox"/><input type="hidden" name="customerId" id="select-cusId" /></p>
	          <p>订单号：<input id="select-relationName" name="relationName" class="textBox"/><input type="hidden" name="relationId" id="select-relationId" /></p>
	          <!-- <p>制单人：<input id="select-people" _class="inmember-combogrid" name="creatorName" class="textBox"/><input type="hidden" name="createId" id="select-peopleid" /></p> -->
	          <div class="button_clear">
	            <a href="javascript:;" id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	            <a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	            <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	          </div>
	        </form>
	      </div>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
initManage("scjhd","生产计划单");
var chechbox='';
var decide=true;
var productionStatus=[
                      {value:"0",text:"未开工"},
                      {value:"1",text:"进行中"},
                      {value:"2",text:"已完成"},
                      {value:"3",text:"已暂停"},
                      {value:"4",text:"已中止"},
                      ]
$(":checkbox").click(function(){//点击复选框并且只让一个选中
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	 chechbox=$(this).val();
});

enterSearch("Inquiry");

$("#search-btn").click(function(){
	refreshData();
});
var texts = [
             {title:'供应商',key:'supplierName'},
             {title:'电话',key:'supplierPhone'},
             {title:'NO',key:'code'},
             {title:' 地<span style="margin:0px 7px;"></span>址',key:'supplierAddress',colspan:2},
             {title:'单据日期',key:'billDate'},
             ];
var thead = [
             {title:'编号',key:'goodsCode',width:8},
             {title:'名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
             {title:'单位',key:'unitName'},
             {title:'数量',key:'quentity'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe',width:10}
             ];
var tfoot = [
				{dtype:3,key:'type',text:'金额合计(大写)：#'},
				{dtype:2,key:'type',text:'小写金额：¥#元'},
			];

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

$('#myBox').window({
	top:$(window).height()/2-100,
	left:$(window).width()/2-150,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false, 
	width:350,
	closed:true,
	modal:true,
	onOpen:function(){
		$(this).parent().prev().css("display","none");
	}
});

$("#productionStatus-setter").fool("dhxCombo",{
	width:164,
	height:30,
	editable:false,
	clearOpt:false,
	focusShow:true,
	data:productionStatus
});

$("#select-productionStatus").fool("dhxCombo",{
    width:167,
    height:34,
    editable:false,
	focusShow:true,
    prompt:'生产状况',
    data:productionStatus
});

//设置鼠标放进去自动出来下拉列表
$("#search-form").find("input[_class]").each(function(i,n){($(this));});

var billData = "";
$.ajax({
	url:"${ctx}/salebill/xsdd/list?recordStatus=1",
	async:false,
    data:{},
    success:function(data){
    	if(data.rows){
    		billData=formatData(data.rows,"fid");
    	}
    }
});
//订单号
$('#select-relationName').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	width:167,
	height:34,
	focusShow:true,
    onlySelect:true,
    prompt:"订单号",
    searchKey:"codeOrVoucherCode",
	filterUrl:"${ctx}/salebill/xsdd/list?recordStatus=1",
	data:billData,
	setTemplate:{
		input:"#code#",
		columns:[
					{option:'#code#',header:'订单号',width:100},
					{option:'#customerName#',header:'客户名称',width:100},
				],
  	},
	onChange:function(value,text){
		$("#select-relationId").val(value);
	}
});

$("#search-code").textbox({
	prompt:'单号',
	width:160,
	height:30,
}); 

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
			url:'${ctx}/warehouse/scjhd/list',
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
	  		{name:'code',label:'单号',align:"center",sortable:true,width:150,formatter:function(value,options,row){
	  			if(row.recordStatus&&row.recordStatus!=0){
	  				return '<a href="javascript:;" onclick="editById(\''+row.fid+'\',\'1\')">'+value+'</a>'; 
	  			}else{
	  				return '<a href="javascript:;" onclick="editById(\''+row.fid+'\',\'\')">'+value+'</a>';
	  			}
	  		}
	  		},
        	{name:'billDate',align:"center",label:'单据日期',width:100,formatter:dateFormatAction},
	  		{name:'relationName',align:"center",label:'销售订单',width:100},
        	{name:'customerName',align:"center",label:'客户名称',width:100},
	        {name:'planStart',align:"center",label:'计划开工日期',width:100,formatter:dateFormatAction},
	  		{name:'endDate',align:"center",label:'计划完工日期',width:100,formatter:dateFormatAction},
	  		{name:'productionStatus',align:"center",label:'生产状况',width:100,formatter:productionStatusAction},
	  		{name:'recordStatus',align:"center",label:'状态',sortable:true,width:100,formatter:recordStatusAction},
	  		{name:'creatorName',align:"center",label:'制单人',width:100},
	  		{name:'action',align:"center",label:'操作',width:100,formatter:function(value,options,row){
	  			var d='',c='',s='',b='',g='';
	  			   d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
	  			   c='<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
	  			   s='<a class="btn-approve" title="审核" href="javascript:;" onclick="passAuditById(\''+row.fid+'\')"></a> ';
	  			   b='<a class="btn-cancel" title="作废" href="javascript:;" onclick="cancelById(\''+row.fid+'\')"></a> ';
	  			   p='<a class="btn-initialize" title="生产状态" href="javascript:;" onclick="productStatus(\''+row.fid+'\',\''+row.productionStatus+'\')"></a> ';
	  			 switch(row.recordStatus){
	  			   case 0:
	  				   return d+c+s+b;
	  				   break;
	  			   case 1:
	  			       return c+b+p;
	  			       break;
	  			   case 2:
	  				   return c;
	  				   break;
	  			 }
	  		}}
	      ],
	      gridComplete:function(){
	    	  warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
//状态翻译

function productionStatusAction(value){
	for(var i=0; i<productionStatus.length; i++){
		if (productionStatus[i].value == value) return productionStatus[i].text;
	}
	return value;
}

//编辑 
function editById(fid,mark){
	var url="${ctx}/warehouse/scjhd/edit?billCode=scjhd&id="+fid+"&flag=edit";
	if(mark==1){
		url="${ctx}/warehouse/scjhd/edit?billCode=scjhd&id="+fid+"&flag=detail";
	}
	warehouseWin("编辑生产计划单",url);
} 
//复制
function copyById(fid){
	var url="${ctx}/warehouse/scjhd/edit?billCode=scjhd&id="+fid+"&mark=1&flag=copy";
	warehouseWin("复制生产计划单",url);
}
//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/warehouse/scjhd/delete',
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
//审核
function passAuditById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要审核该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/warehouse/scjhd/passAudit',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.returnCode == "0"){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
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
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}
//废除
function cancelById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要废除该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/warehouse/scjhd/cancel',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.returnCode == "0"){
							$.fool.alert({time:1000,msg:"废除成功!"});
							$('#billList').trigger("reloadGrid");
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
		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

//生產狀況
function productStatus(fid,productionStatus){
	$('#myBox').window("open");
	$('#myBox').window("setTitle",'生产状况');
	$("#fid-setter").val(fid);
	if(!productionStatus||productionStatus=="undefined"){
		$("#productionStatus-setter").next()[0].comboObj.setComboValue(0);
	}else{
		$("#productionStatus-setter").next()[0].comboObj.setComboValue(productionStatus);
	}
}

$("#setter").click(function(){
	$.ajax({
		type : 'post',
		url : '${ctx}/warehouse/scjhd/proStatus',
		data : {id : $("#fid-setter").val(),productionStatus:$("#productionStatus-setter").next()[0].comboObj.getSelectedValue()},
		dataType : 'json',
		success : function(data) {	
			dataDispose(data);
			if(data.returnCode == '0'){
				$.fool.alert({msg:'操作成功！',fn:function(){
					$('#myBox').window("close");
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
});
if("${param.billId}"){
	editById("${param.billId}");
}
</script>
</body>
</html>
