<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>期初库存信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
#add{
  vertical-align: top;
  /* margin-top: 5px; */
}
#search-form{
  display: inline-block;
  width:93%;
}
#search-form .textbox{
  margin:5px 5px 0px 0px
}
#search-form .btn-s{
  margin:5px 4px 0px 0px;
  vertical-align: middle;
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
                   <h1>期初库存</h1>
                </div>             
             </div>
	<div id="addBox"></div> 
	<div style="margin:10px 0px 10px 0px">  
	  <fool:tagOpt optCode="qckcAdd">
	  <a href="javascript:;" id="addNew" class="btn-ora-add" >新增</a> 
	  </fool:tagOpt>
	  <fool:tagOpt optCode="qckcSearch">
	  <input id="search-code"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	  <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	  </fool:tagOpt>
	  <div class="input_div">
	      <form id="search-form">
	        <p>开始日期：<input id="search-startDay" name="startDay" /></p>
	        <p>结束日期：<input id="search-endDay" name="endDay"/></p>
	        <p>仓库：<input id="search-warehouse" name="inWareHouseId" _class="warehouse"/></p><!-- <input id="search-status"/> -->
	        <p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	        <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
	        <p>货品：<input id="select-goods" name="goodsName" _class="goods-combogrid"><input id="select-goodsId" type="hidden" name="goodsId"/></p>
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
initManage("qckc","期初库存");
//设置仓库
/* $("#search-warehouse").fool('dhxCombo',{
	width:160,
	height:30,
	
});  */

var chechbox='';
var decide=true;
//新版领料员输入显示
$(":checkbox").click(function(){//点击复选框并且只让一个选中
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	 chechbox=$(this).val();
   /*  if(decide==true){
      chechbox=$(this).val();
      decide=false;
     }else{
    	chechbox='';
    	decide=true;
     }	 */
});



enterSearch("Inquiry");
var goodsSpecChooserOpen=false;
var goodsChooserOpen=false;


$('#billList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/initialstock/qckc/list',
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
	  		{name:'code',label:'单号',align:"center",sortable:true,width:100,<fool:tagOpt optCode="qckcAction1">formatter:codeLinkNew</fool:tagOpt>},
	  		{name:'voucherCode',align:"center",label:'原始单号',sortable:true,width:100,},
	  		{name:'inWareHouseName',align:"center",label:'仓库',sortable:true,width:100,}, 
	  		{name:'billDate',align:"center",label:'单据日期',width:40,sortable:true,formatter:dateFormatAction},
	  		{name:'recordStatus',align:"center",label:'状态',width:40,sortable:true,formatter:recordStatusAction},
			<fool:tagOpt optCode="qckcAction">
	  		{name:'action',align:"center",label:'操作',formatter:actionFormatNew}
	  		</fool:tagOpt>
	      ],
	      gridComplete:function(){
	    	  <fool:tagOpt optCode="qckcAction2">//</fool:tagOpt>$('.btn-del').remove();
				<fool:tagOpt optCode="qckcAction3">//</fool:tagOpt>$('.btn-copy').remove();
				<fool:tagOpt optCode="qckcAction4">//</fool:tagOpt>$('.btn-approve').remove();
				<fool:tagOpt optCode="qckcAction5">//</fool:tagOpt>$('.btn-cancel').remove();
	    	  warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

$("#clear-btn").click(function(){
	cleanBoxInput($("#search-form"));
});

$("#search-btn").click(function(){
	refreshData();
});

/* //编辑 
function editById(fid,mark){
	var url="${ctx}/warehouse/qckc/edit?billCode=qckc&id="+fid+"&flag=edit";
	if(mark==1){
		url="${ctx}/warehouse/qckc/edit?billCode=qckc&id="+fid+"&flag=detail";
	}
	warehouseWin("编辑期初库存",url);
} 

//复制
function copyById(fid){
	var url="${ctx}/warehouse/qckc/edit?billCode=qckc&id="+fid+"&mark=1&flag=copy";
	warehouseWin("复制期初库存",url);
}

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/initialstock/qckc/delete',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								$('#billList').datagrid('reload');
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').datagrid('reload');
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

function verifyById(fid){
	$.fool.confirm({title:'确认',msg:'确定要审核通过此单据吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '${ctx}/initialstock/qckc/passAudit',
					data : {id : fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'审核成功！',fn:function(){
								$('#billList').datagrid('reload');
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
					url : '${ctx}/initialstock/qckc/cancel',
					data : {id :fid},
					dataType : 'json',
					success : function(data) { 
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'已作废！',fn:function(){
								$('#billList').datagrid('reload');
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

}; */

/* $("#search-code").textbox({
	prompt:'单号或原始单号',
	width:165,
	height:32
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

//设置鼠标放进去自动出来下拉列表
$("#search-form").find("input[_class]").each(function(i,n){($(this));});
if("${param.billId}"){
	viewRowNew("${param.billId}");
}
</script>
</body>
</html>
