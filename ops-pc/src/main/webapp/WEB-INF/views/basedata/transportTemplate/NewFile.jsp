<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>运输费报价模版</title>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style type="text/css">
</style>
</head>
<body>
	<div class="titleCustom">
	  <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>运输费报价模版</h1>
      </div>             
    </div>
	<div class="form1">		
		<!-- <form id="search-form">
		<p><font>编号、名称：</font><input id="search-code" type="text" calss="textBox" name="aaaa" value=""/></p>
		<p><font>运输方式：</font><input id="areaId" type="text" class="textBox" name="areaId" value=""/></p>
		<p><font>装运方式：</font><input id="areaId2" type="text" class="textBox" name="areaId2" value=""/></p>
		<a class="btn-blue btn-s go-search1" id="search-btn2" onclick="refreshData()">查询</a>
		<a class="btn-blue btn-s" style="margin-left:5px;" onclick="cleanBoxInput()">清空</a>
		</form> -->
		<form id="addTraffic">
			<input id="fid" name="fid" type="hidden" value="${obj.fid}"/>
		<p>		   
		    <font><em>*</em>编号：</font><input type="text" data-options="validType:'maxLength[50]'" id="code" name="code" class="textBox" value="${obj.code}"/>	
			<font><em>*</em>名称：</font><input type="text" data-options="validType:'maxLength[50]'" id="name" name="name" class="textBox" value="${obj.name}"/>
			<font><em>*</em>运输方式：</font><input id="areaId1" type="text" class="textBox" name="areaId" value=""/>
			<font><em>*</em>装运方式：</font><input id="areaId3" type="text" class="textBox" name="areaId2" value=""/>
		</p>
		<p>
		 <font><em>*</em>预计天数：</font><input type="text" data-options="validType:'maxLength[50]'" id="code" name="code" class="textBox" value="${obj.code}"/>
		<font><em>*</em>发货地：</font><input id="areaId" type="text" class="textBox" name="areaId" value=""/>
		<font><em>*</em>收货地：</font><input id="areaId2" type="text" class="textBox" name="areaId2" value=""/>
		<font><em>*</em>组织：</font><input id="areaId" type="text" class="textBox" name="areaId" value=""/>
		</p>
		<p>			
			<font><em>*</em>帐套：</font><input id="areaId2" type="text" class="textBox" name="areaId2" value=""/>
			<font><em>*</em>状态：</font><input id="enable" type="text" class="textBox" name="areaId2" value=""/>
			<font>描述：</font><input type="text" data-options="validType:'maxLength[200]'" id="code" name="code" class="textBox" value="${obj.code}"/>	
		</p>
		</form>
	</div>
	<table id="dataTable"></table>
	<div id="dataPager"></div>
	<h5>>>单据详情</h5>
	<table id="goodList" ></table>	
    <div id="addBox"></div>		
</body>
</html>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript">
/* //初始化查询框
$("#search-code").textbox({
	prompt:'编号或名称',
	width:160,
	height:32
});
//搜索框
function refreshData(){//查询
	var value = $("#search-form").serializeJson();
	var options = {"searchKey": value};
	$('#dataTable').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
} */
//状态
$('#enable').fool("dhxCombo",{
		height:"30px",
		width:"160px",		
		editable:false,
		clearOpt:false,
		valueField: 'value',
		textField: 'text',
		data:[
		      {
		    	  value: '0',
			      text: '停用'
		      },{
		    	  value: '1',
			      text: '启用'
		      }
		     ],
	onLoadSuccess:function(combo){		
		  combo.setComboValue();
		}	
	});
	
//运输方式
var areaIdValue='';	
$.ajax({
	url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=001&num="+Math.random(),
	async:false,		
	success:function(data){		  	
		areaIdValue=formatData(data[0].children,'id','texr');	
    }
	});
var areaIdName= $("#areaId1").fool("dhxCombo",{
	  width:160,
	  height:32,
	  data:areaIdValue,		
	  editable:false,
	  focusShow:true,
	  onLoadSuccess:function(combo){
			combo.setComboValue("${vo.areaId}");
		} 
});	
//装运方式
var areaIdName2= $("#areaId3").fool("dhxCombo",{
	  width:160,
	  height:32,
	  data:areaIdValue,		
	  editable:false,
	  focusShow:true,
	  onLoadSuccess:function(combo){
			combo.setComboValue("${vo.areaId}");
		} 
});

$("#dataTable").jqGrid({//表1
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/vouchermake/queryBill',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#dataTable")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	//postData:$("#search-form").serializeJson(),
	autowidth:true,
	height:"100%",
	pager:"#dataPager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[
	          //{name : '',label :"",align:'center',width:"25px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' rowIndex='"+options.rowId+"' fid='"+rowObject.fid+"' type='checkbox' onclick='checker(this,\""+rowObject.recordStatus+"\")'/>"; }},
	          {name : 'fid',label : 'fid',hidden:true},
	          {name : 'fid',label : '模版ID',hidden:true},
	          {name : 'fid',label : '发货地ID',hidden:true},
	          {name : 'fid',label : '收货地ID',hidden:true},
	          //{name : 'code',label : '单号',align:'center',width:"100px"},
	          {name : 'billDate',label : '预计天数',align:'center',width:"100px"},
	          {name : 'contactUnitName',label : '运输方式',align:'center',width:"100px"},
              {name : 'deptName',label : '组织',align:'center',width:"100px"},
              {name : 'warehouseStockName',label : '帐套',align:'center',width:"100px"},
              {name : 'memberName',label : '创建时间',align:'center',width:"100px"},
              {name : 'amount',label : '创建人',align:'center',width:"100px"},
              {name : 'specialAmount',label : '描述',align:'center',width:"100px"},
		      ],
});

$("#goodList").jqGrid({//从表二
	datatype:"json",
	url:"${ctx}/vouchermake/getBillDetail",
	//postData:{billId:"",billType:$('#billType').val()},
	autowidth:true,
	height:"100%",
	pager:"#pager",
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	colModel:[
	          {name : 'goodsId',label :"goodsId",hidden:true}, 
	          {name : 'unitId',label :"unitId",hidden:true},
	          {name : 'unitId2',label :"unitId2",hidden:true},
	          {name : 'barCode',label : '模版',align:'center',width:"100px"},			          
	          {name : 'goodsCode',label : '运输费用',align:'center',width:"100px"},
	          {name : 'goodsName',label : '运输单位',align:'center',width:"100px"},
	          {name : 'inWareHouseName',label : '组织',align:'center',width:"100px"},
	          {name : 'unitName',label : '帐套',align:'center',width:"100px"},
	          {name : 'quentity',label : '创建时间',align:'center',width:"100px"},
	          {name : 'unitPrice',label : '创建人',align:'center',width:"100px"},
	          {name : 'type',label : '描述',align:'center',width:"100px"}	         
			  ]
});
//启用事件
function defaultById(fid){
	$.fool.confirm({title:'确认',msg:'要启用吗？',fn:function(r){
		 if(r){
			  $.ajax({
					type : 'post',
					url : '',
					data : {id : fid},
					dataType : 'json',
					success : function(data) { 
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'启用成功！',fn:function(){
								$('#accountList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#accountList').trigger("reloadGrid"); 
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

//停用用按钮点击事件
function blockUp(target,fid){
	var _url='';
	if(_billCode=="bmjb"){
		_url=getRootPath()+'/flow/security/unuse?id='+fid;
	}
				$.ajax({
					type : 'post',
					url : _url,
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'停用成功！',fn:function(){
								 $('#datalist').trigger('reloadGrid');
							}});
						}else{
							$.fool.alert({msg:data.message});
						}
					},
					error:function(){
						$.fool.alert({msg:"系统正忙，请稍后再试。"});
					}
				});
}

//删除/delete
function delRow(fid){
	var _url=getRootPath()+'/flow/taskLevel/delete?fid='+fid;
	$.fool.confirm({title:'提示',msg:"确认删除记录？",fn:function(data){
		if(data){	
			$.post(_url,function(data){	
				dataDispose(data);
				if(data.returnCode == '0'){
				    $('#dataTable').trigger('reloadGrid');
				    $.fool.alert({time:1000,msg:'删除成功'});
				}else if(data.returnCode = '1'){
					$.fool.alert({msg:data.message});
				}else{
					$.fool.alert({msg:'服务器繁忙，请稍后再试'});
					
				}
			},'json');
		}else{
			return false;
		}
	}});
}
</script>