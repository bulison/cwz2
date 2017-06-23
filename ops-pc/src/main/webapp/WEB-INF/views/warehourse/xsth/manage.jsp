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
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="#">仓库应用</a></li>
	        <li><a href="#" class="curr">销售退货单据</a></li>
	    </ul>
	</div>
	
	 <div id="addBox"></div> 
	<!--<a href="#" id="add" class="btn-ora-add" style="width:60px;margin:10px 0 10px 0;display: inline-block;vertical-align:middle;">新增</a>
	<a href="#" id="refresh" class="btn-ora-add" style="width:60px;margin:10px 0 10px 0;display: inline-block;vertical-align:middle;">刷新</a>
	<input id="search-code"/>+<input id="search-status"/>+<input id="search-warehouse"/>+<input id="search-startDay" />-<input id="search-endDay" class="easyui-datebox" data-options="prompt:'结束日期',width:100,height:32"/> <a id="search-btn" class="btn-blue btn-s">筛选</a>
	<table id="billList"></table>
	 -->
	
	<div style="margin: 10px 0px 0px 0px;">
	        <fool:tagOpt optCode="xsthAdd">
			   <a href="#" id="add" class="btn-ora-add" style="vertical-align: top;">新增</a>
			</fool:tagOpt>
			<%-- <a href="${ctx}/purchaseinquiry/xsth/export" id="export" class="btn-ora-export">导出</a> --%> 
		<form action="" id="search-form" style="display: inline-block; width: 93%">
			<input id="search-codeOrVoucherCode"/><!-- <input id="search-voucherCode"/> --><input id="search-status"/><input id="search-warehouse"/><input id="search-startDay" /><input id="search-endDay" class="easyui-datebox" data-options="prompt:'结束日期',width:100,height:32"/><input id="search-inMemberName" name="search-inMemberName" class="textBox"/><input type="hidden" name="search-inMemberId" id="search-inMemberId" /><input id="select-goods"><fool:tagOpt optCode="xsthSearch"><input id="select-goodsid" type="hidden"/><a id="search-btn" href="javascript:search();" class="btn-blue btn-s" style="vertical-align:middle;">筛选</a></fool:tagOpt><a id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
		</form>
	</div>	
	<table id="billList"></table>
	
<script type="text/javascript">
var texts = [
			{colspan:2,text:'<div style="margin-right:60px;font-size:20px;font-weight: bold;">[销售退货单]</div>',textAlign:'right'},
             {title:'NO',key:'code'},
             {title:'销售商',key:'customerName',br:true},
             {title:'电<span style="margin:0px 9px;"></span>话',key:'customerPhone'},
             {title:'业<span style="margin:0px 4px;"></span>务<span style="margin:0px 4px;"></span>员',key:'inMemberName'},
             {title:'地<span style="margin:0px 7px;"></span>址',key:'customerAddress',br:true},
             {title:'联系人',key:'supplierContact'},
             {title:'送货日期',key:'billDate'}
             /* {title:'单据日期',key:'billDate'},
             {title:'计划完成日期',key:'endDate'},
             {title:'备注',key:'describe',br:true} */
             ];
var thead = [
			{title:'仓库',key:'inWareHouseName',width:8},
             {title:'编号',key:'goodsCode'},
             {title:'产品名称',key:'goodsName,goodsSpecName,goodsSpec',width:30},
             {title:'单位',key:'unitName'},
             {title:'数量',key:'quentity'},
             {title:'单价',key:'unitPrice',textAlign:'right'},
             {title:'金额',key:'type',textAlign:'right'},
             {title:'备注',key:'describe',width:10}
             ];
var tfoot = [
				/* {dtype:0,key:'quentity',text:'当页总数#个'},
				{dtype:3,key:'unitPrice',text:'当页总金额&nbsp;大写&nbsp;#'},
				{dtype:2,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'},
				{dtype:1,key:'quentity',text:'总数#个'},
				{dtype:5,key:'unitPrice',text:'总金额&nbsp;大写&nbsp;#'},
				{dtype:4,key:'unitPrice',text:'¥&nbsp;#&nbsp;元'}, */
				{dtype:3,key:'type',text:'金额合计(大写)：#'},
				{dtype:2,key:'type',text:'小写金额&nbsp;¥#元'},			
				/* {dtype:4,key:'type',text:'收款金额&nbsp;¥#元',br:true,preNull:true}, */
			];

	var goodsSpecChooserOpen = false;
	var goodsChooserOpen = false;
	var memberChooserOpen = false;
	var recordStatusOptions = [{id:'',name:'全部'},{id:'0',name:'未审核'},{id:'1',name:'已审核'},{id:'2',name:'已作废'}];
	$('#add').click(function() {
		$('#addBox').window({
			title : '新增销售退货单',
			top:10,
			left:0,
			width:$(window).width()-10,
			height:$(window).height()-60,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			resizable : false,
			modal:true,
			href : '${ctx}/salesshipper/xsth/edit',
			onClose : function() {
				if (goodsChooserOpen) {
					$('#goodsChooser').window("destroy");
					goodsChooserOpen = false;
				}
				if (goodsSpecChooserOpen) {
					$("#goodsSpecChooser").window("destroy");
					goodsSpecChooserOpen = false;
				}
				if (memberChooserOpen) {
					$("#pop-win").window("destroy");
					memberChooserOpen = false;
				}
			}
		});
	});
	
	$("#clear-btn").click(function(){
		$("#search-form").form("clear");
	});

	$('#refresh').click(function() {
		$('#billList').datagrid('reload');
	});

	$("#search-codeOrVoucherCode").textbox({
		prompt : '单号或凭证号',
		width : 160,
		height : 30,
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					search();
				}
			}
		})
	});
	/* $("#search-voucherCode").textbox({
		prompt:'凭证号',
		width:160,
		height:30
	}); */
	$('#search-inMemberName').textbox({
		novalidate:true,
		width:160,
		height:32,
		prompt:'业务员'
	});
	$("#select-goods").textbox({
		prompt:'货品',
		width:160,
		height:30,
		editable:false,
	});
	$("#select-goods").textbox('textbox').click(function(){
		chooser=$.fool.window({'title':"选择货品",href:'${ctx}/goods/window?okCallBack=selectgoodsSearch&onDblClick=selectgoodsSearchDBC&singleSelect=true',
			onClose:function(){
				$(this).window("destroy");
			}		
		});
	});
	$('#search-inMemberName').textbox('textbox').click(function(){
		win = $.fool.window({href:"${ctx}/member/window?okCallBack=inMemberName&onDblClick=inMemberNameDBC&singleSelect=true",'title':'选择采购员',
			onClose:function(){
				$(this).window("destroy");
			}});
	});
	function inMemberName(data){
		$('#search-inMemberName').textbox('setValue',data[0].username);
		$('#search-inMemberId').val(data[0].fid);
		win.window('close');
	}
	function inMemberNameDBC(data){
		$('#search-inMemberName').textbox('setValue',data.username);
		$('#search-inMemberId').val(data.fid);
		win.window('close');
	}
	function selectgoodsSearch(rowData){
		$("#select-goodsid").val(rowData[0].fid);
		$("#select-goods").textbox('setValue',rowData[0].name);
		chooser.window('close');
	}
	function selectgoodsSearchDBC(rowData){
		$("#select-goodsid").val(rowData.fid);
		$("#select-goods").textbox('setValue',rowData.name);
		chooser.window('close');
	}
	//comboTree($("#search-warehouse"),"${ctx}/basedata/warehourseList",true);
	$("#search-warehouse").combotree({
		prompt : '仓库',
		width : 160,
		height : 30,
		editable : false,
		url : "${ctx}/basedata/warehourseList"
	});

	$("#search-status").combobox({
		valueField:"value",
		textField:"text",
		prompt:'状态',
		width:160,
		height:30,
		editable:false,
		data:[{value:null,text:"全部"},{value:0,text:"未审核"},{value:1,text:"已审核"},{value:2,text:"已作废"}],
		onLoadSuccess:function(){
			$(this).combobox('clear');
		}
	});

	$("#search-startDay").datebox({
		prompt : '开始日期',
		width : 160,
		height : 30,
		editable : false
	});

	$("#search-endDay").datebox({
		prompt : '结束日期',
		width : 160,
		height : 30,
		editable : false
	});

	$('#billList')
			.datagrid(
					{
						url : '${ctx}/salesshipper/xsth/list',
						width:'100%',
						idField : 'fid',
						pagination : true,
						fitColumns : true,
						remoteSort:false,
						columns : [ [
								{
									field : 'fid',
									title : 'fid',
									hidden : true,
									width : 100
								},
								{
									field : 'code',
									title : '单号',
									sortable : true,
									width : 100
									<fool:tagOpt optCode="xsthAction1">,
									formatter:function(value,row,index){
							  			return '<a href="javascript:;" onclick="editById(\''+row.fid+'\',\'1\')">'+value+'</a>';
							  		}
									</fool:tagOpt>
								},
								{
									field : 'voucherCode',
									title : '凭证号',
									sortable : true,
									width : 100
								},
								{
									field : 'billDate',
									title : '单据日期',
									sortable : true,
									width : 100,
									formatter : dateFormatAction
								},
								{
									field : 'customerName',
									title : '销售商名称',
									sortable : true,
									width : 100
								},
								{
									field : 'customerPhone',
									title : '销售商电话',
									width : 100
								},
								{
									field : 'inMemberName',
									title : '业务员',
									width : 100
								},
								{
									field : 'inWareHouseName',
									title : '仓库',
									width : 100
								},
								{
									field : 'recordStatus',
									title : '状态',
									sortable : true,
									width : 100,
									formatter : recordStatusAction
								},
								<fool:tagOpt optCode="xsthAction">
								{
									field : 'action',
									title : '操作',
									width : 100,
									formatter : function(value, row, index) {
										var d='',c='',s='',b='';
							  			<fool:tagOpt optCode="xsthAction2">
							  			   d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
							  			 </fool:tagOpt>
							  			<fool:tagOpt optCode="xsthAction3">
							  			   c='<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
							  			</fool:tagOpt>
							  			 <fool:tagOpt optCode="xsthAction4">
							  			   s='<a class="btn-approve" title="审核" href="javascript:;" onclick="passAuditById(\''+row.fid+'\')"></a> ';
							  			 </fool:tagOpt>
							  			<fool:tagOpt optCode="xsthAction5">
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
									}
								}
								</fool:tagOpt>
								] ]
					});
	function search() {
		var code = $("#search-codeOrVoucherCode").textbox('getValue');
		var warehouseId = $("#search-warehouse").combobox('getValue');
		var recordStatus = $("#search-status").combobox('getValue');
		var startDay = $("#search-startDay").datebox('getValue');
		var endDay = $("#search-endDay").datebox('getValue');
		//var voucherCode = $("#search-voucherCode").textbox('getValue');
		var goodsId=$("#select-goodsid").val();
		var inMemberId=$('#search-inMemberId').val();
		var options = {
			"goodsId":goodsId,
			"codeOrVoucherCode" : code,
			"inWareHouseId" : warehouseId,
			"recordStatus" : recordStatus,
			"startDay" : startDay,
			"endDay" : endDay,
			"inMemberId":inMemberId,
		};
		$('#billList').datagrid('load', options);
	};
	

	//编辑 
	function editById(fid,mark) {
		var title = "修改销售退货单";
		if(mark==1){
			title = "查看销售退货单"
		}
		$('#addBox').window({
			title : title,
			top:10,
			left:0,
			width:$(window).width()-10,
			height:$(window).height()-60,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			resizable : false,
			modal:true,
			href : '${ctx}/salesshipper/xsth/edit?id=' + fid,
			onClose : function() {
				if (goodsChooserOpen) {
					$('#goodsChooser').window("destroy");
					goodsChooserOpen = false;
				}
				if (goodsSpecChooserOpen) {
					$("#goodsSpecChooser").window("destroy");
					goodsSpecChooserOpen = false;
				}
				if (memberChooserOpen) {
					$("#pop-win").window("destroy");
					memberChooserOpen = false;
				}
			}
		});
	}
	//复制
	function copyById(fid) {
		$('#addBox').window({
			title : '新增期初库存',
			top:10,
			left:0,
			width:$(window).width()-10,
			height:$(window).height()-60,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			resizable : false,
			modal:true,
			href : "${ctx}/salesshipper/xsth/edit?id=" + fid + "&mark=1",
			onClose : function() {
				if (goodsChooserOpen) {
					$('#goodsChooser').window("destroy");
					goodsChooserOpen = false;
				}
				if (goodsSpecChooserOpen) {
					$("#goodsSpecChooser").window("destroy");
					goodsSpecChooserOpen = false;
				}
				if (memberChooserOpen) {
					$("#pop-win").window("destroy");
					memberChooserOpen = false;
				}
			}
		});
	}
	//状态翻译
	function recordStatusAction(value) {
		for (var i = 0; i < recordStatusOptions.length; i++) {
			if (recordStatusOptions[i].id == value)
				return recordStatusOptions[i].name;
		}
		return value;
	}
	//删除
	function removeById(fid) {
		$.fool.confirm({
			title : '确认',
			msg : '确定要删除选中的记录吗？',
			fn : function(r) {
				if (r) {
					$.ajax({
						type : 'post',
						url : '${ctx}/salesshipper/xsth/delete',
						data : {
							id : fid
						},
						dataType : 'json',
						success : function(data) {
							if (data.returnCode == '0') {
								$.fool.alert({
									msg : '删除成功！',
									fn : function() {
										$('#billList').datagrid('reload');
									}
								});
							} else {
								$.fool.alert({
									msg : data.message,
									fn : function() {
										$('#billList').datagrid('reload');
									}
								});
							}
						}
					});
				}
			}
		});
	}
	//审核
	function passAuditById(fid) {
		$.fool.confirm({
			title : '确认',
			msg : '确定要审核该记录吗？',
			fn : function(r) {
				if (r) {
					$.ajax({
						type : 'post',
						url : '${ctx}/purchaseinquiry/xsth/passAudit',
						data : {
							id : fid
						},
						dataType : 'json',
						success : function(data) {
							$('#billList').datagrid('reload');
						}
					});
				}
			}
		});
	}
	//废除
	function cancelById(fid) {
		$.fool.confirm({
			title : '确认',
			msg : '确定要废除该记录吗？',
			fn : function(r) {
				if (r) {
					$.ajax({
						type : 'post',
						url : '${ctx}/purchaseinquiry/xsth/cancel',
						data : {
							id : fid
						},
						dataType : 'json',
						success : function(data) {
							$('#billList').datagrid('reload');
						}
					});
				}
			}
		});
	}
	$(function() {
		//分页条 
		//setPager($('#billList'));
	});
</script>
</body>
</html>
