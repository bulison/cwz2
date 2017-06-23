<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>销售退货单据信息管理</title>
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

#btnBox{
  text-align: center;
}

.form p.hideOut,.form1 p.hideOut{
    display: none;
  }
  
#userChooser,#customerChooser,#goodsChooser{
  display: none;

}

#userSearcher,#cusSearcher,#goodsSearcher{
  text-align: left;
  margin:10px;  
}
  .form,.form1{padding:5px 0px;}
  .form1 p{margin:5px 0px}
  .form p font,.form1 p font{width:115px;}
  
  #search-form span{
  margin-right: 5px;
  margin-bottom: 10px
}
#search-form a{
  margin-right: 5px;
  margin-bottom: 10px
}
.nav{margin-bottom: 10px;}
 #search-form{display: inline-block; width: 98%}
#grabble{ float: right;}
#bolting{ width: 160px;height: 27px; position:relative; border: 1px solid #ccc; background: #fff;margin-right: 25px;}
.button_a{display:block; width:25px; height: 25px;background:#76D5FC; -moz-border-radius: 3px;/* Gecko browsers */-webkit-border-radius: 3px;/* Webkit browsers */
border-radius:3px;/* W3C syntax */float: right; right:29px; top:63px; position: absolute;}
.button_span{  width:0; height:0; border-left:8px solid transparent; border-right:8px solid transparent;border-top:8px solid #fff;top:10px; position: absolute;right:5px;}
.input_div{ display: none; background:#F5F5F5; padding: 10px 0px 5px 0px; border: 1px solid #D5DBEA;position: absolute;right: 23px; top:90px;z-index: 1;}
.input_div p{ font-size: 12px; color:#747474;vertical-align: middle;text-align: right;  margin: 0 20px 0 10px; width:235px;}
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
                   <h1>销售退货单</h1>
                </div>             
             </div>
	 <div id="addBox"></div> 
	<div style="margin: 10px 0px 0px 0px;">	
	<div class="nav">
	   <fool:tagOpt optCode="xsthAdd">
		 <a href="javascript:;" id="addNew" class="btn-ora-add" style="vertical-align: top;">新增</a>
	  </fool:tagOpt>
	  <input id="search-code" class="textbox"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	  <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly" /><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	</div>	
	<div class="input_div">       
		<form action="" id="search-form">
		  <p>开始日期：<input id="search-startDay" name="startDay" /></p>
		  <p>结束日期：<input id="search-endDay" name="endDay" class="easyui-datebox" data-options="width:100,height:32"/></p>
		  <!-- <p>状态：<input id="search-status"/></p> -->
		  <p>仓库：<input id="search-warehouse" _class="warehouse" name="inWareHouseId"/></p>
		  <p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	       <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
			<p>客户名称：<input id="myCustomerName" name="customerName" data-options="{editable:false,width:160,height:32}"/><input id="myCustomerId" name="customerId" type="hidden"/></p>
			<p>业务员：<input id="select-inMemberName" _class="inmember-combogrid" name="inMemberName" class="textBox"/><input type="hidden" name="inMemberId" id="select-inMemberId" /></p>
		  <fool:tagOpt optCode="xsthSearch">
		  <p>货品：<input id="select-goods" _class="goods-combogrid" name="goodsName"><input id="select-goodsId" name="goodsId" type="hidden"/></p>		 
		  </fool:tagOpt>
		   <div class="button_clear">
		   <a id="search-btn" href="javascript:search();" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
		  <a id="clear-btn" href="javascript:;" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
		  <a id="clear-btndiv" href="javascript:;" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
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
initManage("xsth","销售退货");
var chechbox='';
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

	var goodsSpecChooserOpen = false;
	var goodsChooserOpen = false;
	var memberChooserOpen = false;
	
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
	
	$("#clear-btn").click(function(){
		cleanBoxInput($("#search-form"));
	});
	
	$("#search-startDay").fool('datebox',{
		prompt : '开始日期',
		width : 160,
		height : 30,
		inputDate:true,
		editable : true
	});

	$("#search-endDay").fool('datebox',{
		prompt : '结束日期',
		width : 160,
		height : 30,
		inputDate:true,
		editable : true
	});

	$('#billList')
			.jqGrid(
					{
						datatype:function(postdata){
							$.ajax({
								url : '${ctx}/salesshipper/xsth/list',
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
						colModel : [
								{
									name : 'fid',
									label : 'fid',
									align:"center",
									hidden : true,
									width : 100
								},
								{
									name : 'totalAmount',
									label : 'totalAmount',
									align:"center",
									hidden : true,
									width : 100
								},
								{
									name : 'code',
									label : '单号',
									align:"center",
									sortable : true,
									width : 100
									<fool:tagOpt optCode="xsthAction1">,
									formatter:codeLinkNew
									</fool:tagOpt>
								},
								{
									name : 'voucherCode',
									label : '原始单号',
									align:"center",
									sortable : true,
									width : 100
								},
								{
									name : 'billDate',
									label : '单据日期',
									align:"center",
									sortable : true,
									width : 100,
									formatter : dateFormatAction
								},
								{
									name : 'customerName',
									label : '客户名称',
									align:"center",
									sortable : true,
									width : 100
								},
								{
									name : 'customerPhone',
									label : '客户电话',
									align:"center",
									width : 100
								},
								{
									name : 'inMemberName',
									label : '业务员',
									align:"center",
									width : 100
								},
								{
									name : 'inWareHouseName',
									label : '入仓仓库',
									align:"center",
									width : 100
								},
								{
									name : 'recordStatus',
									label : '状态',
									align:"center",
									sortable : true,
									width : 100,
									formatter : recordStatusAction
								},
								<fool:tagOpt optCode="xsthAction">
								{
									name : 'action',
									label : '操作',
									align:"center",
									width : 100,
									formatter:actionFormatNew
								}
								</fool:tagOpt>
								],
								gridComplete:function(){
							    	  	<fool:tagOpt optCode="xsthAction2">//</fool:tagOpt>$('.btn-del').remove();
										<fool:tagOpt optCode="xsthAction3">//</fool:tagOpt>$('.btn-copy').remove();
										<fool:tagOpt optCode="xsthAction4">//</fool:tagOpt>$('.btn-approve').remove();
										<fool:tagOpt optCode="xsthAction5">//</fool:tagOpt>$('.btn-cancel').remove();
							    	  warehouseAll();
							      }
					}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
	function search() {
		refreshData();
	};
	
	//设置鼠标放进去自动出来下拉列表
	$("#search-form").find("input[_class]").each(function(i,n){($(this));});
	if("${param.billId}"){
		viewRowNew("${param.billId}",1);
	}
</script>
</body>
</html>
