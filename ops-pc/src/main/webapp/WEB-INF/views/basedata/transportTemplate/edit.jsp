<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>新增/编辑运输费用报价模版</head>
<body>
<style type="text/css">
h2{
   font: 800 14px 宋体 !important;
    color: #ffc96c;
    margin-left: 10px;
    display: inline-block;

   }
</style>		
<c:set var="flagName" value="${param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>
    <form action="" class="bill-form myform" id="form">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn"><div id="triangle"></div></div><h1>${flagName}运输费报价模版</h1><a id="hide" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">基础信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写基础信息</h2></div>
		<div class="in-box" id="list2">
		  <div class="inlist">	     
			<input id="id" name="id" type="hidden" value="${obj.id}"/>
			<input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}"/>
			<input id="createTime" name="createTime" type="hidden" value="${obj.createTime}"/>
			<input id="orgId" name="orgId" type="hidden" value="${obj.orgId}"/>
			<input id="fiscalAccountId" name="fiscalAccountId" type="hidden" value="${obj.fiscalAccountId}"/>
			<input id="creatorId" name="creatorId" type="hidden" value="${obj.creatorId}"/> 
		<p>		   
		    <font><em>*</em>编号：</font><input type="text" _trim='true' id="code" name="code" class="easyui-validatebox textBox" value="${obj.code}" data-options="required:true, novalidate:true,validType:'maxLength[50]'"/>	
			<font><em>*</em>名称：</font><input type="text" _trim='true' id="name" name="name" class="easyui-validatebox textBox" value="${obj.name}" data-options="required:true, novalidate:true,validType:'maxLength[50]'"/>
			<font><em>*</em>运输方式：</font><input id="transportTypeFid" type="text" class="textBox" name="transportTypeFid" value="${obj.transportTypeFid}"/>			
		</p>
		<p>
		  <font><em>*</em>装运方式：</font><input id="shipmentTypeFid" type="text" class="textBox" name="shipmentTypeFid" value="${obj.shipmentTypeFid}"/>
		  <font><em>*</em>预计天数：</font><input type="text" id="expectedDays" name="expectedDays" class="easyui-validatebox textBox" value="${obj.expectedDays}" data-options="required:true, novalidate:true,validType:['accessory','maxLength[50]']"/>
		  <font><em>*</em>发货地：</font><input id="deliveryPlaceFid" type="text" class="textBox" name="deliveryPlaceFid" value="${obj.deliveryPlaceFid}"/>		
		</p>
		<p>	
		  <font><em>*</em>收货地：</font><input id="receiptPlaceFid" type="text" class="textBox" name="receiptPlaceFid" value="${obj.receiptPlaceFid}"/>
		  <font><em>*</em>状态：</font><input id="enable" type="text" class="textBox" name="enable" value="${obj.enable}"/>
		  <font>描述：</font><input type="text" data-options="validType:'maxLength[200]'" id="describe" name="describe" class="easyui-validatebox textBox" value="${obj.describe}"/>		     
		</p>
		  </div>
		</div>
	</div>
	<div id="dataBox" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>模版信息</h2></div>
		<div class="in-box" id="list1">	 
	<table id="dataTable"></table>
	<div id="dataPager"></div>
	<h5><!-- >>单据详情 --></h5>
	<table id="goodList" ></table>	
    <div id="goodPager"></div>		
		</div>
	</div>
</form>
<div class="mybtn-footer" id="btnBox"></div>
 <div class="repertory"></div>
<script type="text/javascript">
var details2=[];
var datajson={};//存储表2数据数组
var list1number='';//表1序号
var list2number='';//表2序号  
var _data='';//数据
var tranUnName='';
var tranCoName='';
var receipName='';//收货地址
var deliveryName='';//发货地址
var sumDays=0;//预计天数和
//var detail1='';//从表1id
var fid='${obj.id}'; //主表fid
var enables=$("#enable").fool("dhxCombo",{
	  width:182,
	  height:32,
	  clearOpt:false,
	  editable:false,
	  required:true,
	  novalidate:true,
	  focusShow:true,
	  data:[
			{
			value: '0',
		    text: '停用'
		    },{
			value: '1',
		    text: '启用'
		    }
	        ]
});	
//运输方式
var transportValue='';	
$.ajax({
	url:"${ctx}/basedata/transitType?num="+Math.random(),
	async:false,		
	success:function(data){	
		transportValue=formatTree(data[0].children,'text','id');	
    }
	});
var transportName= $("#transportTypeFid").fool("dhxCombo",{
	  width:182,
	  height:32,
	  data:transportValue,	
	  clearOpt:false,
	  editable:false,
	  focusShow:true,
	  required:true,
	  novalidate:true,
	  setTemplate:{
			input:"#name#",
			option:"#name#"
	  	},
	toolsBar:{
	      name:"运输方式",
		addUrl:"/basedata/listAuxiliaryAttr",
		refresh:true
	}
});	
//装运方式
var shipmentValue='';	
$.ajax({
	url:"${ctx}/basedata/shipmentType?num="+Math.random(),
	async:false,		
	success:function(data){		  	
		shipmentValue=formatTree(data[0].children,'text','id');	
    }
	});
var shipmentName= $("#shipmentTypeFid").fool("dhxCombo",{
	  width:182,
	  height:32,
	  data:shipmentValue,	
	  clearOpt:false,
	  editable:false,
	  focusShow:true,
	  required:true,
	  novalidate:true,
	  value:"${obj.shipmentTypeFid}",
	  setTemplate:{
			input:"#name#",
			option:"#name#"
	  	},
	toolsBar:{
	      name:"装运方式" ,
		addUrl:"/basedata/listAuxiliaryAttr",
		refresh:true
	},
});
//发货地址
var deliveryValue='';
$.ajax({
	url:"${ctx}/freightAddress/findAddressTree?enable=1&num="+Math.random(),
	async:false,		
	success:function(data){	
		deliveryValue=formatTree(data,"text","id");
    }
	});
var deliveryGroup='';//发货单组
deliveryName=$("#deliveryPlaceFid").fool("dhxCombo",{
	width:182,
	height:32,
	required:true, 
	novalidate:true,
	data:deliveryValue,
	clearOpt:false,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	toolsBar:{
	  name:'货运地址' ,
		addUrl:'/freightAddress/manage',
		refresh:true
	},
	focusShow:true,
	editable:false,
	onChange:function(value,text){
		var record = deliveryName.getSelectedText();
		if(record.fullParentId){
			deliveryGroup=(record.fullParentId).substring(0,32);
		}else{
			deliveryGroup=record.fid;
		}
		 if(receiptGroup==deliveryGroup){		
			 deliveryName.setComboText('');
	     	$.fool.alert({msg:'发货地址跟收货地址不能在同组内互发'});
	     }
	  },
	 /*  onLoadSuccess:function(){
		 console.log(deliveryValue); 
	  } */
    });

var receiptGroup='';
var receiptName=$("#receiptPlaceFid").fool("dhxCombo",{
	width:182,
	height:32,
	required:true, 
	novalidate:true,
	data:deliveryValue,
	clearOpt:false,
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	toolsBar:{
	  name:'货运地址' ,
		addUrl:'/freightAddress/manage',
		refresh:true
	},
	focusShow:true,
	editable:false,
	onChange:function(record){
		var record = receiptName.getSelectedText();
		if(record.fullParentId){
			receiptGroup=(record.fullParentId).substring(0,32);
		}else{
			receiptGroup=record.fid;
		}
        if(receiptGroup==deliveryGroup){
        	receiptName.setComboText('');
          	$.fool.alert({msg:'发货地址跟收货地址不能在同组内互发'});
        }
	  }
    });

	/* //设置_trim属性的输入框去除前后空格
	if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
		$('input[_trim=true]').bind('propertychange',function(){			
			var s=$(this).val();
			var id=$(this).attr('id');
			$("#"+id).val($.trim(s));  	
		});// IE专用
	}else{
		$('input[_trim=true]').bind('input',function(){
			var s=$(this).val();
			var id=$(this).attr('id');
			$("#"+id).val($.trim(s)); 
		});
	} */
//鼠标失去焦点事件，设置_trim属性的输入框去除前后空格
$('input[_trim=true]').blur(function(){
	var s=$(this).val();
	var id=$(this).attr('id');
	$("#"+id).val($.trim(s)); 	
})
	
//添加保存按钮
$("#btnBox").append('<input type="button" id="save" class="btn-blue2 btn-xs" value="保存" />');

$('.backBtn').click(function(){//返会链接
	$('#addBox').window('close');
});
     
$("#dataTable").jqGrid({//表1
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/transportTemplate/detail?templateId='+fid,
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){	        	
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#dataTable")[0].addJSONData(data.responseJSON);	 
// 	        		list1number=$('#dataTable').find('#'+1).find('[aria-describedby=dataTable_code]').text();
					list1number="A1";
	        	}
	        }
		});
	},
	footerrow: true,
	autowidth:true,
	height:"100%",
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	ondblClickRow:function(rowid,iCol,cellcontent,e){										
		 editRow(rowid);		
		}, 
	colModel:[
			 {name:"action",label:"操作",align:'center',width:'50px',formatter:function(cellvalue,options,rowObject){
				var d='',c='',s='';
				d = "<a href='javascript:;' onClick='delRow(\""+options.rowId+"\")' class='btn-del' title='删除'></a>";
				c = "<a href='javascript:;' onClick='cancelRow(\""+options.rowId+"\",\""+rowObject.isNew+"\")' class='btn-back' title='撤销'></a>";
				s = "<a href='javascript:;' onClick='saveRow(\""+options.rowId+"\")' class='btn-save' title='保存'></a>";
				if(cellvalue == 'new'){
					return "<a href='javascript:;' class='btn-add' onclick='addRow()' title='新增货品'></a>";
				}else{
					if(rowObject.editing){
						return s+"  "+c;
					}else{
						//屏蔽编辑按钮
						return d;
					}
				}
			   }},
			  {name : 'isNew',label : 'isNew',hidden:true},
			  {name:'code',label:'序列号',width:"30px"},
			  {name:'editing',label : 'editing',hidden:true},
	          {name : 'fid',label : 'fid',hidden:true},
	          {name : 'details1',label : '模版ID',hidden:true},
	          {name : 'deliveryPlaceFid',label : '发货地ID',hidden:true},
	          {name : 'deliveryPlaceName',label : '发货地址',align:'center',width:"90px",editable:true,edittype:"text"},
	          {name : 'receiptPlaceFid',label : '收货地ID',hidden:true},
	          {name : 'receiptPlaceName',label : '收货地址',align:'center',width:"100px",editable:true,edittype:"text"},
	          {name : 'expectedDays',label : '预计天数',align:'center',width:"100px",editable:true,edittype:"text",editoptions:{dataInit:function(ed){
		        	$(ed).numberbox({validType:['maxLength[200]'],height:'30'})}}},
	          {name : 'transportTypeFid',label : '运输方式ID',hidden:true},
	          {name : 'transportTypeName',label : '运输方式',align:'center',width:"100px",editable:true,edittype:"text"}, 
	          {name : 'shipmentTypeFid',label : '装运方式ID',hidden:true},
	          {name : 'shipmentTypeName',label : '装运方式',hidden:true,align:'center',width:"100px"},
              {name : 'describe',label : '描述',align:'center',width:"100px",editable:true,edittype:"text",editoptions:{dataInit:function(ed){
		        	$(ed).textbox({validType:['maxLength[200]'],height:'30'})}}},
		      ],
		      onSelectRow:function(rowid,status){
		    	_id=$('#dataTable').find('#'+rowid).find('[aria-describedby=dataTable_fid]').text(); 
		    	var row = $('#dataTable').jqGrid("getRowData",rowid);
		    	if(row.isNew == "1"){
		    		return false;
		    	}
		    	list1number=$('#dataTable').find('#'+rowid).find('[aria-describedby=dataTable_code]').text();			    
		    	addData();
		      }
});
$("#dataTable").footerData('set',{action:'new',isNew:'1'});

function addData(){
		jQuery("#goodList").jqGrid("clearGridData");//删除所有行	
	   _data=datajson[list1number]
		if(_data==''){return false;}
	for(var i=0;i<_data.length;i++){
		 $('#goodList').jqGrid('addRowData',i,{
			 code:list1number,
		     isNew:'0',
		     fid:_data[i].fid,
		     transportCostFid:_data[i].transportCostFid,
		     transportUnitFid:_data[i].transportUnitFid,
		     transportCostName:_data[i].transportCostName,
		     transportUnitName:_data[i].transportUnitName,
		     describe:_data[i].describe,
				  }); 
	}
}

$("#goodList").jqGrid({//从表二	
	footerrow: true,
	autowidth:true,
	height:"100%",
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	ondblClickRow:function(rowid,iCol,cellcontent,e){//onCellSelect										
		 editrow(rowid);
		}, 
	colModel:[
	          {name:"action",label:"操作",align:'center',width:'50px',formatter:function(cellvalue, options, rowObject){
					var d='',c='',s='';
					d = "<a href='javascript:;' onClick='delrow(\""+options.rowId+"\",\""+rowObject.id+"\")' class='btn-del' title='删除'></a>";
					c = "<a href='javascript:;' onClick='cancelrow(\""+options.rowId+"\",\""+rowObject.isNew+"\")' class='btn-back' title='撤销'></a>";
					s = "<a href='javascript:;' onClick='saverow(\""+options.rowId+"\",\""+rowObject.id+"\")' class='btn-save' title='保存'></a>";
					if(cellvalue == 'new'){
						return "<a href='javascript:;' class='btn-add' onclick='addrow()' title='新增货品'></a>";
					}else{
						if(rowObject.editing){
							return s+"  "+c;
						}else{
							//屏蔽编辑按钮
							return d;
						}
					}
				   }},
			  {name : 'isNew',label : 'isNew',hidden:true},
			  {name:'code',label:'序列号',width:"30px"},
			  {name:'editing',label : 'editing',hidden:true},
	          {name : 'fid',label :"fid",hidden:true}, 	         
		      {name : 'transportCostFid',label : 'transportCostId',hidden:true}, 
		      {name : 'transportUnitFid',label : 'transportUnitId',hidden:true}, 
	          {name : 'transportCostName',label : '运输费用',align:'center',width:"100px",editable:true,edittype:"text"},
	          {name : 'transportUnitName',label : '运输单位',hidden:true,align:'center',width:"100px"},
	          {name : 'describe',label : '描述',align:'center',width:"100px",editable:true,edittype:"text",editoptions:{dataInit:function(ed){
		        	$(ed).textbox({validType:['maxLength[200]'],height:'30'})}}}	         
			  ]
});
$("#goodList").footerData('set',{action:'new',isNew:'1'});

$.ajax({
	type : 'post',
	url :'${ctx}/transportTemplate/detail2?templateId='+fid,
	dataType : 'json',
	async:true,
	success : function(data){
		if('${param.flag}'=='edit'){	
		datajson=data;
	   _data=datajson[list1number];	 
	   for(var i=0;i<_data.length;i++){
			 $('#goodList').jqGrid('addRowData',i,{
				 code:list1number,
			     isNew:'0',
			     fid:_data[i].fid,
			     transportCostFid:_data[i].transportCostFid,
			     transportUnitFid:_data[i].transportUnitFid,
			     transportCostName:_data[i].transportCostName,
			     transportUnitName:_data[i].transportUnitName,
			     describe:_data[i].describe,
					  }); 
		}
	   $("#dataTable").jqGrid('setSelection',1);
    }
	}
});  

//列表新增
function addRow(){
	var receiptPlaceName='';
	 var receiptPlaceFid='';
	var length = $("#dataTable").jqGrid('getRowData').length;
	if(length>0){
	//判断货品是否编辑完，没有编辑完不能添加下一条
	//var rows = $("#dataTable").jqGrid('getRowData').length;
	for(var i=1;i<=length;i++){
		var _editing=$('#dataTable').find('.btn-save').length;		
	}
	if(_editing>0){
		$.fool.alert({msg:'你还有没编辑完,请先确认！'});
		return false;
	  };  
	}
	length=length+1;
	if(length==1){
	 receiptPlaceName=deliveryName.getComboText();
	 receiptPlaceFid=deliveryName.getSelectedValue();
	 $('#dataTable').jqGrid('addRowData',length,{
		code:'A'+length,
		isNew:'1',
		deliveryPlaceName:receiptPlaceName,
		deliveryPlaceFid:receiptPlaceFid,
	}); 
	}else{
	var rows=length-1;
	/* var code=$('#dataTable').find('#'+rows).find('[aria-describedby=dataTable_code]').text();
    code=parseInt(code.substring(1,2))+1; */	
	receiptPlaceName=$('#dataTable').find('#'+rows).find('[aria-describedby=dataTable_receiptPlaceName]').text();
    receiptPlaceFid = $('#dataTable').find('#'+rows).find('[aria-describedby=dataTable_receiptPlaceFid]').text();
	$('#dataTable').jqGrid('addRowData',length,{
	  code:'A'+length,
	  isNew:'1',
	  deliveryPlaceName:receiptPlaceName,
	  deliveryPlaceFid:receiptPlaceFid,
	   }); 
	}
	//list1number=$('#dataTable').find('#'+length).find('[aria-describedby=dataTable_code]').text();
	editRow(length);
}

function addrow(){
	var row = $('#dataTable').jqGrid("getRowData",1);
	if(row.isNew && row.isNew == "1"){
		$.fool.alert({msg:"请先新增保存表1的第一条线路后再进行费用新增！"});
		return false;
	}
	var length = $("#goodList").jqGrid('getRowData').length;
	length=length+1;
		delet();
}

function delet(){//根据表1的数据添加删除表2数据
   list2number=$('#goodList').find('#1').find('[aria-describedby=goodList_code]').text();
	 if(list2number!="" && list1number!=list2number){//list2number表2中第1行序号，list1number表1中选中的序号 		 
		jQuery("#goodList").jqGrid("clearGridData");//删除所有行
	}
   var rows = $("#goodList").jqGrid('getRowData').length;
	    rows=rows+1;
	  $('#goodList').jqGrid('addRowData',rows,{
		code:list1number,
		   isNew:'1'
		  }); 
		editrow(rows);
}

function addup(list1number){//根据表1序号添加删除对应的表二数据
	var details2=$("#goodList").jqGrid('getRowData');
	var rows = $("#dataTable").jqGrid('getRowData').length;
	if(details2==''){
		 delete datajson[list1number];
	}/*if else(){
		 delete datajson[list1number];	
	}*/else{
	 datajson[details2[0].code]=details2; 
	}  
}

var mytranSportValue='';	
$.ajax({
	url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=018&num="+Math.random(),
	async:false,		
	success:function(data){	
		mytranSportValue=formatTree(data[0].children,'text','id');	
    }
	});
var transtUnValue='';	
$.ajax({
	url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=021&num="+Math.random(),
	async:false,		
	success:function(data){	
		transtUnValue=formatData(data[0].children,'id','texr');	
    }
	});
//列表编辑 参数改为index从表2
function editrow(rowid){
	var rowData=$("#goodList").jqGrid('getRowData',rowid);
	if(rowData.editing=="true"){ 
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#goodList').jqGrid('setRowData', rowid, rowData);
	$("#goodList").jqGrid('editRow',rowid);
	 tranCoName=getEditor("goodList",rowid,"transportCostName").fool("dhxCombo",{		
		height:"30px",
		width:"250px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		 toolsBar:{
		    name:'运输费用',
			 addUrl:'/basedata/listAuxiliaryAttr',
		   refresh:true,
		 },
		value:rowData.transportCostFid,
		required:true,
		novalidate:true,
		focusShow:true,
		data:mytranSportValue,
	});
	/*  tranUnName=getEditor("goodList",rowid,"transportUnitName").fool("dhxCombo",{	
		height:"30px",
		width:"250px",
		setTemplate:{
			input:"#text#",
			option:"#text#"
		},
		value:rowData.transportUnitFid,			
		required:true,
		novalidate:true,
		focusShow:true,
		data:transtUnValue,
	}); */
}
//从表1
function editRow(rowid){
	var rowData=$("#dataTable").jqGrid('getRowData',rowid);
	if(rowData.editing=="true"){ 
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#dataTable').jqGrid('setRowData', rowid, rowData);
	$("#dataTable").jqGrid('editRow',rowid);
	getEditor("dataTable",rowid,"deliveryPlaceName").fool("dhxCombo",{
		height:"30px",
		width:"150px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		toolsBar:{
		  name:'货运地址' ,
			addUrl:'/freightAddress/manage',
			refresh:true
		},
		clearOpt:false,
		focusShow:true,
		editable:false,
		value:rowData.deliveryPlaceFid,
		required:true,
		novalidate:true,
		data:deliveryValue,
	});
	 receipName=getEditor("dataTable",rowid,"receiptPlaceName").fool("dhxCombo",{
		height:"30px",
		width:"150px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		toolsBar:{
		  name:'货运地址' ,
			addUrl:'/freightAddress/manage',
			refresh:true
		},
		clearOpt:false,
		focusShow:true,
		editable:false,
		value:rowData.receiptPlaceFid,
		required:true,
		novalidate:true,
		data:deliveryValue,
	});
	getEditor("dataTable",rowid,"transportTypeName").fool("dhxCombo",{
		height:"30px",
		width:"150px",
		setTemplate:{
			input:"#name#",
			option:"#name#"
	  	},
	  	toolsBar:{
		      name:"运输方式",
			addUrl:"/basedata/listAuxiliaryAttr",
			refresh:true
		},
		value:rowData.transportTypeFid,
		focusShow:true,
		editable:false,
		clearOpt:false,
		required:true,
		novalidate:true,		
		data:transportValue,
	  });
	/* getEditor("dataTable",rowid,"shipmentTypeName").fool("dhxCombo",{	
		height:"30px",
		width:"150px",
		setTemplate:{
			input:"#text#",
			option:"#text#"
		},
		value:rowData.shipmentTypeFid,
		required:true,
		novalidate:true,
		focusShow:true,
		data:shipmentValue,
	  }); */
     getEditor("dataTable",rowid,"expectedDays").numberbox({
		required:true,
		novalidate:true,
		validType:['accessory','maxLength[50]'],
	});
	};
//撤销
function cancelRow(index,value){
	if(value == '1'){
		$("#dataTable").jqGrid('delRowData',index);
		$("#dataTable").jqGrid('setSelection',1);
	}else{
		$('#dataTable').jqGrid('setRowData', index, {editing:false,action:null});
		$("#dataTable").jqGrid('restoreRow', index);
	}
}
//撤销
function cancelrow(index,value){
	if(value == '1'){
		$("#goodList").jqGrid('delRowData',index);
	}else{
		$('#goodList').jqGrid('setRowData', index, {editing:false,action:null});
		$("#goodList").jqGrid('restoreRow', index);
	}
}
//列表编辑保存
function saveRow(index){
	sumDays=0;
	var a =getEditor("dataTable",index,"receiptPlaceName");
	var b =getEditor("dataTable",index,"deliveryPlaceName");
	var c =getEditor("dataTable",index,"transportTypeName");
	var d =getEditor("dataTable",index,"expectedDays");
	$(a[0]).closest("tr").form("enableValidation");
	$(b[0]).closest("tr").form("enableValidation");
	$(c[0]).closest("tr").form("enableValidation");
	$(d[0]).closest("tr").form("enableValidation");
	if(!$(a[0]).closest("tr").form('validate')||!$(b[0]).closest("tr").form('validate')||!$(c[0]).closest("tr").form('validate')||!$(d[0]).closest("tr").form('validate')){
		return false;//验证不通过跳出方法不保存
	}
	var rowData=$("#dataTable").jqGrid('getRowData',index);
	if(rowData.editing=="false"){//设置编辑状态
		return;
	}
	rowData.editing=false;
	rowData.action=null;
	 var receiptPlaceName = getTableEditor(index,'receiptPlaceName').next()[0].comboObj.getComboText();
	 var receiptPlaceFid = getTableEditor(index,'receiptPlaceName').next()[0].comboObj.getSelectedValue();
	 var deliveryPlaceName = getTableEditor(index,'deliveryPlaceName').next()[0].comboObj.getComboText();
	 var deliveryPlaceFid = getTableEditor(index,'deliveryPlaceName').next()[0].comboObj.getSelectedValue();
	 var transportTypeName = getTableEditor(index,'transportTypeName').next()[0].comboObj.getComboText();
	 var transportTypeFid = getTableEditor(index,'transportTypeName').next()[0].comboObj.getActualValue();
	 //var shipmentTypeName = getTableEditor(index,'shipmentTypeName').next()[0].comboObj.getComboText();
	 //var shipmentTypeFid = getTableEditor(index,'shipmentTypeName').next()[0].comboObj.getActualValue(); 
	$('#dataTable').jqGrid('setRowData', index, {receiptPlaceName:receiptPlaceName,receiptPlaceFid:receiptPlaceFid,deliveryPlaceName:deliveryPlaceName,deliveryPlaceFid:deliveryPlaceFid,transportTypeName:transportTypeName,transportTypeFid:transportTypeFid,isNew:"0",editing:false,action:null});//编辑状态转换，按钮变化
	$("#dataTable").jqGrid('saveRow',index,false);	
	sum();
	$("#dataTable").jqGrid('setSelection',index);	
}
function sum(){
	//计算合计天数
	var rows=$('#dataTable').jqGrid('getRowData');
	for(var i=0;i<rows.length;i++){
		if("undefined" != typeof rows[i].expectedDays&&rows[i].expectedDays!=""){
			sumDays += parseFloat(rows[i].expectedDays);
		}
	}
	$('#expectedDays').val(sumDays);
}
function saverow(index){
	var a =getEditor("goodList",index,"transportCostName");
	//var b =getEditor("goodList",index,"transportUnitName");
	$(a[0]).closest("tr").form("enableValidation")
	//$(b[0]).closest("tr").form("enableValidation")
	if(!$(a[0]).closest("tr").form('validate')){
		return false;
	}
	var rowData=$("#goodList").jqGrid('getRowData',index);
	if(rowData.editing=="false"){//设置编辑状态
		return;
	}
	rowData.editing=false;
	rowData.action=null;
	 var transportCostName = getTableEditor2(index,'transportCostName').next()[0].comboObj.getComboText();
	 var transportCostFid = getTableEditor2(index,'transportCostName').next()[0].comboObj.getActualValue();
	 //var transportUnitName = getTableEditor2(index,'transportUnitName').next()[0].comboObj.getComboText();
	 //var transportUnitFid = getTableEditor2(index,'transportUnitName').next()[0].comboObj.getActualValue();	 
	$('#goodList').jqGrid('setRowData', index, {transportCostName:transportCostName,transportCostFid:transportCostFid,editing:false,action:null});//编辑状态转换，按钮变化
	$("#goodList").jqGrid('saveRow',index,false);
	addup();//将数据保存
}

//列表删除
function delRow(index){
	var length = $("#dataTable").jqGrid('getRowData').length;
	list1number=$('#dataTable').find('#'+index).find('[aria-describedby=dataTable_code]').text();	
	if(length==index){
    if(list1number!=''){//删除从表1对应也要删除从表二的数据
	delete datajson[list1number];
	} 	
	$("#dataTable").jqGrid('delRowData',index);
	sumDays=0;
	sum();
	$("#dataTable").jqGrid('setSelection',index-1);	
	}else{
		$.fool.alert({msg:'请先删除最后一行！'});
	}
}
function delrow(index){	
	list1number=$('#goodList').find('#'+index).find('[aria-describedby=goodList_code]').text();
	$("#goodList").jqGrid('delRowData',index);
	addup(list1number)//删除数据也要重新保存
}
//获取表格里面某个编辑器方法
function getTableEditor(index,name){
	return $('#dataTable').find("tr.jqgrow #"+index+"_"+name);
}
function getTableEditor2(index,name){
	return $('#goodList').find("tr.jqgrow #"+index+"_"+name);
}

function pdEdit(){
	var rows = $("#dataTable").jqGrid('getRowData').length;
	for(var i=1;i<=rows;i++){
		var _editing=$('#dataTable').find('.btn-save').length;		
	}
	 if(_editing>0){
		$.fool.alert({msg:'你还有没编辑完,请先确认！'});
		return false;
	};  
}
$('#save').click(function(e) {	
    //判断货品是否编辑完成
    var deliveryPlaceFidMain=deliveryName.getSelectedValue();//主表发货地址
    var receiptPlaceFidMain=receiptName.getSelectedValue();
	var rows = $("#dataTable").jqGrid('getRowData').length;
	var rowsb = $("#goodList").jqGrid('getRowData').length;
    var details1=$("#dataTable").jqGrid('getRowData');
     details2=[];//保存前先将对象清空   
     details2.push(datajson); //重新给对象付值 
     datajson.total=rows;	
	var jsonuserinfo = $('#form').serializeJson();	
	//if(details1.length>0){
	var obj = $.extend(jsonuserinfo,{details1:JSON.stringify(details1),details2:JSON.stringify(details2)});
	$('#form').form('enableValidation'); 
		 if($('#form').form('validate')){ 
			//从表1的发货地址、收货地址默认出主表的发货地址、收货地址，提交保存时判断第一条记录的发货地址等于主表的发货地址，
			    //最后一条记录的收货地址=主表的收货地址，除第一条记录外，每条记录的发货地址等于上一条记录的收货地址
				 if(deliveryPlaceFidMain==receiptPlaceFidMain){
				    	$.fool.alert({msg:'基础信息的发货地址不能和收货地址一样！'});	
				    	return false;
				    };
				    if(rows<=0){ //判断明细数据至少存在一条
				    	$.fool.alert({msg:'请添加明细数据！'});	
				    	return false;	
				    }
				    for(var i=1;i<=rows;i++){
						var _editing=$('#dataTable').find('.btn-save').length;		
					}
				    for(var i=1;i<=rowsb;i++){
						var _editingb=$('#goodList').find('.btn-save').length;		
					}
					if(_editing>0 || _editingb>0){
						$.fool.alert({msg:'你还有没编辑完,请先确认！'});
						return false;
					  };  
			    	if(rows==1){   		
			    	 if(details1[0].deliveryPlaceFid!=deliveryPlaceFidMain||details1[0].receiptPlaceFid!=receiptPlaceFidMain){
			    		 $.fool.alert({msg:'模版信息的发/收地址应与基础信息的发/收货地址对应！'});
			    		 return false;
			    		 };
			    	}else if(rows>1){
			    		 if(details1[0].deliveryPlaceFid!=deliveryPlaceFidMain){//第一行数据的发货地址等于主表的发货地址
			    			 $.fool.alert({msg:'模版信息的发/收地址应与基础信息的发/收货地址对应'});
			    			 return false;
			     		};
			    	  for(var i=1;i<details1.length;i++){//循环判断
			    		 var irows=i+1;//第i+1行
			    		 var receiptPlaceFid = $('#dataTable').find('#'+i).find('[aria-describedby=dataTable_receiptPlaceFid]').text();//收货地址
			    		 var deliveryPlaceFid=$('#dataTable').find('#'+irows).find('[aria-describedby=dataTable_deliveryPlaceFid]').text();//发货地址
			    		if(receiptPlaceFid!=deliveryPlaceFid){
			    		  $.fool.alert({msg:'模版信息上一条的收货地址应是下一条的发货地址'});
			    		  return false;
			    		};
			    		var j=i-1;
			    		if(details1[j].deliveryPlaceFid==details1[j].receiptPlaceFid){
			    			$.fool.alert({msg:'模版信息发货地址不能和收货地址一样'});
			    			 return false;
			    		};			    		 		
			    	  };  
			    	  var receiptPlaceFidlast= $('#dataTable').find('#'+rows).find('[aria-describedby=dataTable_receiptPlaceFid]').text();
			    		if(receiptPlaceFidlast!=receiptPlaceFidMain){//最后一条数据的收货地址等于主表的收货地址
			       		 $.fool.alert({msg:'模版信息最后收货地址必须等于基础信息的收货地址'});
			       		 return false;
			       	     };     
			    	};
			    $('#save').attr("disabled","disabled");
			    $.post('${ctx}/transportTemplate/save',obj,function(data){
			    	dataDispose(data);	
				    	if(data.result =='0'){
				    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
				    			$('#addBox').window('close');
				    			$('#save').removeAttr("disabled");
				    			$('#billList').trigger('reloadGrid');
				    		}});
				    	}else{
				    		$.fool.alert({msg:data.message});
				    		$('#save').removeAttr("disabled");
			    		}
				    	return true;
			    });
		}else{
			return false;
		} 
	/*  }else{
		$.fool.alert({msg:'必须填写模版信息'});
	}   */
}); 
</script>
</body>
</html>