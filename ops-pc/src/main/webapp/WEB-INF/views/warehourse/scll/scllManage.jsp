<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>生产领料信息管理</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<link rel="stylesheet" href="${ctx}/resources/css/warehouseManage.css" />
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
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
   /*  margin-top: 5px; */
}
#search-form{
  display: inline-block;
  width:98%;
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
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px;width:240px;}
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
                   <h1>生产领料单</h1>
                </div>             
             </div>
	<div id="addBox"></div>
	<div style="margin:10px 0px 10px 0px;">
	<div>
	<fool:tagOpt optCode="scllAdd">
	<a href="javascript:;" id="addNew" class="btn-ora-add" >新增</a> 
	</fool:tagOpt>
	<fool:tagOpt optCode="scllSearch">
	<input id="search-code"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	<div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	</div>
	
	<div class="input_div">
	<form id="search-form">
	<p>开始日期：<input id="search-startDay" name="startDay" /></p>
	<p>结束日期：<input id="search-endDay" name="endDay"/></p>
	<p>仓库:<input id="search-warehouse" _class="warehouse" name="inWareHouseId"/></p><!-- <input id="search-status"/> -->
	<p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	<input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
	<p>部门：<input name="deptId" _class="deptComBoxTree"  id="select-deptId"/></p>
	<p>领料员：<input id="select-inMemberName" _class="inmember-combogrid" name="inMemberName" class="textBox"/><input type="hidden" name="inMemberId" id="select-inMemberId" /></p>	
	<p>货品：<input id="select-goods" _class="goods-combogrid" name="goodsName"><input id="select-goodsid" type="hidden" name="goodsId"/></p>
	<div class="button_clear">
	<a href="javascript:;" id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	<a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	<a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	</div>
	<!-- </div> -->
	</fool:tagOpt>
	</form>
	</div>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
initManage("scll","生产领料");
var chechbox='';
var decide=true;
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

enterSearch("Inquiry");//回车搜索

$('#billList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/productionMaterials/scll/list',
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
	  		{name:'code',label:'单号',align:"center",width:100,sortable:true,<fool:tagOpt optCode="scllAction1">formatter:codeLinkNew</fool:tagOpt>},
	  		{name:'voucherCode',align:"center",label:'原始单号',width:100},
        	{name:'billDate',align:"center",label:'单据日期',width:40,sortable:true,formatter:dateFormatAction},
        	{name:'relationName',align:"center",label:'生产计划单',width:100},
	        {name:'inMemberName',align:"center",label:'领料员',width:100},
	        {name:'inWareHouseName',align:"center",label:'仓库',width:100},
	  		{name:'recordStatus',align:"center",label:'状态',width:40,sortable:true,formatter:recordStatusAction},
			<fool:tagOpt optCode="scllAction">
	  		{name:'action',align:"center",label:'操作',formatter:actionFormatNew}
	  		</fool:tagOpt>
	      ],
	      gridComplete:function(){
	    	  	<fool:tagOpt optCode="scllAction2">//</fool:tagOpt>$('.btn-del').remove();
				<fool:tagOpt optCode="scllAction3">//</fool:tagOpt>$('.btn-copy').remove();
				<fool:tagOpt optCode="scllAction4">//</fool:tagOpt>$('.btn-approve').remove();
				<fool:tagOpt optCode="scllAction5">//</fool:tagOpt>$('.btn-cancel').remove();
	    	  warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

$("#clear-btn").click(function(){
	chechbox='';
	cleanBoxInput($("#search-form"));
});

$("#search-btn").click(function(){
	refreshData();
});

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
