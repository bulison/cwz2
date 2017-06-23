<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>采购申请单</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
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
                   <h1>采购申请单</h1>
                </div>             
             </div>
	<div id="addBox"></div> 
	<div style="margin:5px 0px 10px 0px;">
		<a href="javascript:;" id="addNew" class="btn-ora-add" style="vertical-align: top;">新增</a>
		<a href="javascript:;" id="generate" class="btn-ora-add" style="vertical-align: top;" onclick="generateById()">生成</a>
		  <input id="search-code"/><a href="javascript:;" class="Inquiry btn-blue btn-s" style="margin-left: 5px;">查询</a>
	      <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly"/><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	      <div class="input_div">
	        <form id="search-form">
	          <p>开始日期：<input id="search-startDay" name="startDay" /></p>
	          <p>结束日期：<input id="search-endDay" name="endDay"/></p>
	          <p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="recordStatus">未审核
	          <input type=checkbox value='1' name="recordStatus">已审核<input type=checkbox value='2' name="recordStatus">已作废</p>
	          <p>申请人：<input id="select-people" name="inMemberName" class="textBox" _class="inmember-combogrid"/><input type="hidden" name="inMemberId" id="select-peopleid" /></p>
	          <!-- <p>供应商：<input id="select-supplierName" name="supplierName" class="textBox"/><input type="hidden" name="supplierId" id="select-supplierId" /></p> -->
	          <p>货品：<input id="select-goods" name="goodsName" _class="goods-combogrid"><input id="select-goodsid" type="hidden" name="goodsId"/></p>
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
initManage("cgsqd","采购申请单");
var chechbox='';
var decide=true;
//新版领料员输入显示
$(":checkbox").click(function(){//点击复选框并且只让一个选中
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	 chechbox=$(this).val();
});
enterSearch("Inquiry");//回车搜索

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


//设置鼠标放进去自动出来下拉列表
$("#search-form").find("input[_class]").each(function(i,n){($(this));});

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
			url:'${ctx}/warehouse/cgsqd/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#billList")[0].addJSONData(data.responseJSON);
                    $("#cb_billList_")[0].checked = false;
                    $("#cb_billList").css("display","inline-block");
                    $("#cb_billList_").css("display","none");
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
	multiselect:true,
	autowidth:true,//自动填满宽度
	height:$(window).outerHeight()-200+"px",
	gridComplete:function(){
  	  /* $('#voucherList').datagrid("getPanel").panel("body").find(".datagrid-view").css("height","350px"); */
  	  var checkbox = $('#billList').find('tr.jqgrow td[aria-describedby=billList_cb] input');
  	  checkbox.tooltip({
  		  position: 'right',
  		  content: '<span style="color:#fff">选择已审核的单据生成采购订单</span>',
  		  onShow: function(){
  			  $(this).tooltip('tip').css({
  				  backgroundColor: '#666',
  				  borderColor: '#666'        
  			  });    
  		  }
  	  });
  	  warehouseAll();
    },
	/* onClickRow:function(index,row){
		$('#billList').datagrid('getPanel').find('div.datagrid-body tr[datagrid-row-index] td[mark=1]').attr('mark',2);
		$('#billList').datagrid('getPanel').find('div.datagrid-body tr[datagrid-row-index]').css('background-color','white');
		$('#billList').datagrid('getPanel').find('div.datagrid-body tr[datagrid-row-index='+index+']').css('background-color','#eaf2ff');
		$('#billList').datagrid('getPanel').find('div.datagrid-body tr[datagrid-row-index='+index+'] td[mark=2]').attr('mark',1);
	}, */
	colModel:[
			{name:'fid',label:'fid',align:"center",hidden:true,width:100},
	  		{name:'code',label:'单号',align:"center",sortable:true,width:100,
	  			formatter:codeLinkNew
	  		},
        	{name:'billDate',label:'单据日期',align:"center",width:100,formatter:dateFormatAction},
        	{name:'deptName',label:'申请部门',align:"center",width:100},
	  		{name:'inMemberName',label:'申请人',align:"center",width:100},
	  		{name:'recordStatus',label:'状态',align:"center",sortable:true,width:100,formatter:recordStatusAction},
	  		{name:'action',label:'操作',align:"center",width:100,formatter:actionFormatNew}
	      ],
	      onSelectRow:function(rowId, status){
	    	  var row = $('#billList').jqGrid('getRowData',rowId);
	    	  if(row.recordStatus!="已审核"){
	    		  $('#billList').jqGrid('setSelection',rowId,false);
	    	  }
	    	  if(row.recordStatus=="已审核") {
                  var audited = $("table#billList td[title='已审核']");
                  var checked = $('#billList').find('tr[aria-selected=true]');
                  if (1 <= audited.length < checked.length) {
                      $("#cb_billList").css("display", "inline-block");
                      $("#cb_billList_").css("display", "none");
                  }
              }
		  },
		gridComplete:function () {
            $("#jqgh_billList_cb").append('<input role="checkbox" id="cb_billList_" style="display: none" onclick="unCheckAll()" class="cbox" type="checkbox" checked="true">');
        },
	      onSelectAll:function(aRowids,status){
	    	  var rows = $('#billList').jqGrid('getRowData');
	    	  for(var i=0;i<rows.length;i++){
	    		  if(rows[i].recordStatus!="已审核"){
	    			  $('#billList').jqGrid('setSelection',i+1,false);
	    		  }
	    		  if(rows[i].recordStatus=="已审核"){
                      $("#cb_billList").css("display","none");
                      $("#cb_billList_").css("display","inline-block");
                      $("#cb_billList_")[0].checked = true;
                  }
	    	  }
	      },
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function unCheckAll() {
	var audited = $("table#billList td[title='已审核']");
    var checked = $('#billList').find('tr[aria-selected=true]');
    if(audited.length===checked.length){
        checked.removeClass("ui-state-highlight");
        $("#billList :checkbox").prop("checked", false);
        $("#billList").jqGrid("resetSelection");
        $("#cb_billList").css("display","inline-block");
        $("#cb_billList_").css("display","none");
    }
}

function generateById(){
	var ids="";
	var rowids = $('#billList').jqGrid('getGridParam', 'selarrrow');
	//var checkeds=$('#billList').datagrid("getChecked");
	for(var i=0;i<rowids.length;i++){
		var row = $('#billList').jqGrid("getRowData",rowids[i]);
		ids=row.fid+","+ids;
	}
	if(ids!=""){
		var src='/warehouse/cgdd/manage?billIds='+ids;
		var text='采购订单';
		parent.kk(src,text);
	}else{
		$.fool.alert({msg:"请先勾选单据"});
	}
};

if("${param.billId}"){
	viewRowNew("${param.billId}");
}
</script>
</body>
</html>
