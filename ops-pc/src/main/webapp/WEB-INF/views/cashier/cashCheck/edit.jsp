<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<html>
<head>
</head>
<body>
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
.mybtn-footer{ padding-right: 20px; width:98%;}
.mybtn-footer p{ width:73px; display: inline-block;}
.mybtn-footer p input{ background: #50B3E7; color:#fff;border-radius:4px; font-size:16px; line-height: 30px;}
.mybtn-footer #dd,.mybtn-footer #dd input{ width:135px;}
.mybtn-footer #dd{margin-right: 10px;}
.ui-jqgrid{margin: auto;} 
</style>
<form id="form" class="bill-form myform">
     <div id="title" >
		 <div id="title1" class="shadow" style="margin:10px 0px 0 0;padding:11px 0; height:18px;">
			  <div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1 style="margin-top:2px;">${billflagName}现金盘点单</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		 </div>
	 </div>
	          
	 <div id="bill" class="shadow">
		  <div class="billTitle"><div id="square2"></div><h2 style="margin-top: -2px;">填写单据信息</h2></div>
		     <div class="in-box" id="list2">
             <input id="fid" type="hidden"  name="fid" value="${cash.fid}"/>
             <input id="updateTime" name="updateTime" type="hidden" value="${cash.updateTime}"/>
             <input id="subjectId" name="subjectId" type="hidden" value="${cash.subjectId}"/>
             <input id="amount" name="amount" type="hidden" value="${cash.amount}"/>
             <p>
               <font><em>*</em>日期：</font><input id="date" name="date" type="text" value="${cash.date}" data-options="novalidate:true,required:true,missingMessage:'该项不能为空！'"/>
               <font><em>*</em>现金科目：</font><input id="subjectName"  type="text" name="subjectName" value="${cash.subjectName}" data-options="novalidate:true,required:true,missingMessage:'该项不能为空！'" />				
			   <font>备注：</font><input id="resume" name="resume" type="text" class="easyui-validatebox textBox" value="${cash.resume}" />
		    </p>										
		   </div>
		 <table id="amountlist"></table>
	</div>
</form>
  <div class="mybtn-footer" _id="${cash.fid}"></div>
<script type="text/javascript">
//var Edit='';//标记上一次盘点单
var curMoney='';//标记币值大小
$(".mybtn-footer").append('<p id="dd"><input type="button" onclick="lastSaved()" id="save" class="btn-blue2 btn-xs" value="读取上次盘点数"/></p><p><input type="button" onclick="saver()" id="repetition" class="btn-blue2 btn-xs" value="保存" /></p>');
$("#date").datebox({
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	prompt:'日期',
	width:160,
	height:32,
	editable:false
});

formatterDate = function(date) {
var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
+ (date.getMonth() + 1);
return date.getFullYear() + '-' + month + '-' + day;
};
//新增时默认选中当前日期
if(!'${cash.fid}'){
	$('#date').datebox('setValue', formatterDate(new Date()));	
}


/* $("#subjectName").fool("subjectCombobox",{
	prompt:"科目",
	width:160,
	height:32,
	required:true,
	missingMessage:'该项不能为空！',
	novalidate:true,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&cashSubject=1",
	focusShow:true,
	onClickIcon:function(){
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
		searchSubjectName();
	},
	onSelect:function(record){
		$('#subjectId').val(record.id);
	}
}); */

var subjectCombo = $('#subjectName').fool('dhxComboGrid',{
	  prompt:"科目",
	  width:160,
	  height:32,
	  focusShow:true,
	  data:getComboData(getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&cashSubject=1&q="),
	  filterUrl:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&cashSubject=1",
	  /* value:"${cash.subjectId}",
	  text:"${cash.subjectName}", */
	  searchKey:"q", 
	  setTemplate:{
		input:"#name#",

		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
	  },
	toolsBar:{
        name:"科目",
        addUrl:"/fiscalSubject/manage",
		refresh:true
    },

    onSelectionChange:function(){
    	$("#subjectId").val(($('#subjectName').next())[0].comboObj.getSelectedValue());
    }
});
$('#subjectName').next().find(".dhxcombo_select_button").click(function(){
	  subjectCombo.closeAll();
	  searchSubjectName();
});

 if('${cash.stauts}'=='2'){//不能修改的情况。${cash.stauts}返回1可以修改返回2不能修改
	$("#form").find("input").attr('disabled','disabled').css('background','#EBEBE4');	
    $('#repetition,#save,.textbox-addon').css('display','none');
    $('.textbox').css('background','#EBEBE4');
}

if('${cash.fid}'){//不能修改日期	
	$("#date").datebox({'readonly':'false'});
	$('#date').next('.datebox').css('background','#EBEBE4');
	$('#date').next('.datebox').find('input').css('background','#EBEBE4');
} 
//科目弹出框
function searchSubjectName(){
	  chooserWindow_search=$.fool.window({width:780,height:480,'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubjectSearch&onDblClick=selectSubjectDBCSearch&cashSubject=1&flag=1&singleSelect=true',
		  onLoad:function(){subjectCombo.closeAll();}});//解决IE点击弹窗下拉框不消失问题
}

function selectSubjectSearch(rowData){
	  if(rowData[0].flag!=1){
		  $.fool.alert({time:1000,msg:"请选择子节点"});
		  return false;
	  }
	  var fids="";
	  var names="";
	  if(rowData.length>1){
	  	  for(var i=0;i<rowData.length;i++){
	  		  fids+=rowData[i].fid+",";
	  		  names+=rowData[i].name+",";
	  	  }
	  }else{
		  fids=rowData[0].fid;
		  names=rowData[0].name;
	  }
	  subjectCombo.setComboText(names);
	  subjectCombo.setComboValue(fids);
	  $("#subjectId").val(fids);
	  chooserWindow_search.window('close');
}
function selectSubjectDBCSearch(rowData){
	  if(rowData.flag!=1){
		  $.fool.alert({time:1000,msg:"请选择子节点"});
		  return false;
	  }
	  subjectCombo.setComboText(rowData.name);
	  subjectCombo.setComboValue(rowData.fid);
	  /* $("#subjectName").combobox("setValue",rowData.name); */
	  $("#subjectId").val(rowData.fid);
	  chooserWindow_search.window('close');
}
//返回导向链接
$('#square1 .backBtn').click(function(){
	$("#addBox").window("close");
	$('html').css('overflow',''); 
});
	   
 //获取表格里面某个编辑器方法
   function _getTableEditor(index,field){
   	 return getTableEditorHelp($("#amountlist"),index,field);       
   }  
   
   function getTableEditorHelp(tbId,index,field){
		var $t =$.fool._get$(tbId);
		return $t.fool('getEditor$',{'index':index,'field':field});
	}
   
//操作按钮，计算合计
function action(){	
	 var _url=getRootPath()+'/cashCheck/totalFamount';
	 var f100=getEditor("amountlist",1,"quantity").numberbox("getText");
	 var f050=getEditor("amountlist",2,"quantity").numberbox("getText");
	 var f020=getEditor("amountlist",3,"quantity").numberbox("getText");
	 var f010=getEditor("amountlist",4,"quantity").numberbox("getText");
	 var f005=getEditor("amountlist",5,"quantity").numberbox("getText");
	 var f002=getEditor("amountlist",6,"quantity").numberbox("getText");
	 var f001=getEditor("amountlist",7,"quantity").numberbox("getText");
	 var f_50=getEditor("amountlist",8,"quantity").numberbox("getText");
	 var f_20=getEditor("amountlist",9,"quantity").numberbox("getText");
	 var f_10=getEditor("amountlist",10,"quantity").numberbox("getText");
	 var f_05=getEditor("amountlist",11,"quantity").numberbox("getText");
	 var f_02=getEditor("amountlist",12,"quantity").numberbox("getText");
	 var f_01=getEditor("amountlist",13,"quantity").numberbox("getText");
	 $.post(_url,{f100:f100,f050:f050,f020:f020,f010:f010,f005:f005,f002:f002,f001:f001,f_50:f_50,f_20:f_20,f_10:f_10,f_05:f_05,f_02:f_02,f_01:f_01},function(data){
		 /* panel.find('tr[datagrid-row-index="13"]').children('[field="money"]').find('.datagrid-cell').text(data); */
		 $('#amountlist').jqGrid("setRowData",14,{money:data}); 
		 $('#amount').val(data);
	 }); 
}
   
$("#amountlist").jqGrid({
	datatype:"local",
	width:800,
	height:430, 
	viewrecords:true,
	jsonReader:{
		records:"total",
	},  
	colModel:[
                {name:'currency',label:'币值',align:'center',width:"100px"},
                {name:'quantity',label:'数量',align:'center',width:"100px",editable:true,edittype:"text"},
                {name:'money',label:'金额',align:'center',width:"100px"},
              ],
    data:[
          {
        	  currency:"100元",
		      quantity:'${cash.f100}',
		      money:'',
          },{
        	  currency:"50元",
              quantity:'${cash.f050}',
		      money:'',
	      },{
		      currency:"20元",
	          quantity:'${cash.f020}',
	          money:'',
	      },{
		      currency:"10元",
	          quantity:'${cash.f010}',
	          money:'',
	      },{
		      currency:"5元",
	          quantity:'${cash.f005}',
	          money:'',
	      },{
		      currency:"2元",
	          quantity:'${cash.f002}',
	          money:'',
	      },{
		      currency:"1元",
	          quantity:'${cash.f001}',
	          money:'',
	      },{
		      currency:"5角",
	          quantity:'${cash.f_50}',
	          money:'',
	      },{
		      currency:"2角",
	          quantity:'${cash.f_20}',
	          money:'',
	      },{
		      currency:"1角",
	          quantity:'${cash.f_10}',
	          money:'',
	      },{
		      currency:"5分",
	          quantity:'${cash.f_05}',
	          money:'',
	      },{ 
		      currency:"2分",
	          quantity:'${cash.f_02}',
	          money:'',
	      },{
		      currency:"1分",
	          quantity:'${cash.f_01}',
	          money:'',
	      },{
		      currency:"合计",
	          quantity:'',
	          money:'${cash.amount}',
	      }
          ],
    onSelectRow:function(rowid,status){//开启编辑状态
    	if('${cash.stauts}'=='2'){//返回2会计期间已结账不能编辑
    		return;
    	}
        for(var i=1;i<=13;i++){
        	$("#amountlist").jqGrid('editRow',i);
        }	
        
        var editor$ =getEditor("amountlist",1,"quantity");//100元
        editor$.attr('_class',1);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
        	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,1);	
          		
        var editor$ =getEditor("amountlist",2,"quantity");//50元
        editor$.attr('_class',2);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,2);	
          		
        var editor$ =getEditor("amountlist",3,"quantity");//20元
        editor$.attr('_class',3);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,3);	
          		
        var editor$ =getEditor("amountlist",4,"quantity");//10元
        editor$.attr('_class',4);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,4);
          		
        var editor$ =getEditor("amountlist",5,"quantity");//5元
        editor$.attr('_class',5);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,5);
          		
        var editor$ =getEditor("amountlist",6,"quantity");//2元
        editor$.attr('_class',6);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,6);	
          		
        var editor$ =getEditor("amountlist",7,"quantity");//1元
        editor$.attr('_class',7);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,7);	
          		
        var editor$ =getEditor("amountlist",8,"quantity");//5角
        editor$.attr('_class',8);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,8);	
          		
        var editor$ =getEditor("amountlist",9,"quantity");//2角
        editor$.attr('_class',9);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,9);
          		
        var editor$ =getEditor("amountlist",10,"quantity");//1角
        editor$.attr('_class',10);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,10);
          		
        var editor$ =getEditor("amountlist",11,"quantity");//5分
        editor$.attr('_class',11);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,11);	
          		
        var editor$ =getEditor("amountlist",12,"quantity");//2分
        editor$.attr('_class',12);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,12);	
          		
        var editor$ =getEditor("amountlist",13,"quantity");//1分
        editor$.attr('_class',13);
        editor$.numberbox({
        	validType:['accessoryNumber','numMaxLength[8]'],
          	width:'100%',
          	height:'100%',		
        });
        numeration(editor$,13);
        setTimeout(function (){
        	getEditor("amountlist",rowid,"quantity").numberbox("textbox").click();
		}, 10);
    }	
})   
 
 //编辑的时候页面显示金额
   if('${cash.fid}'){
	  var _f100=('${cash.f100}'*100).toFixed(2);
	  $('#amountlist').jqGrid("setRowData",1,{money:_f100});
	  var _f050= ('${cash.f050}'*50).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",2,{money:_f050});
	  var _f020= ('${cash.f020}'*20).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",3,{money:_f020}); 
	  var _f010= ('${cash.f010}'*10).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",4,{money:_f010}); 
	  var _f005= ('${cash.f005}'*5).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",5,{money:_f005}); 
	  var _f002= ('${cash.f002}'*2).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",6,{money:_f002}); 
	  var _f001= ('${cash.f001}'*1).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",7,{money:_f001}); 
	  var _f_50= ('${cash.f_50}'*0.5).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",8,{money:_f_50}); 
	  var _f_20= ('${cash.f_20}'*0.2).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",9,{money:_f_20}); 
	  var _f_10= ('${cash.f_10}'*0.1).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",10,{money:_f_10}); 
	  var _f_05= ('${cash.f_05}'*0.05).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",11,{money:_f_05}); 
	  var _f_02= ('${cash.f_02}'*0.02).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",12,{money:_f_02}); 
	  var _f_01= ('${cash.f_01}'*0.01).toFixed(2); 
	  $('#amountlist').jqGrid("setRowData",13,{money:_f_01}); 
   }
   
    function saver(){ //保存	
    	$('#form').form('enableValidation'); 
    	if($('#form').form('validate')){
    		for(var i=1;i<=13;i++){
        		$("#amountlist").jqGrid('saveRow',i);
        	}
        	/* var panel = $('#amountlist').datagrid('getPanel'); */
        	var dataform=$("#form").serializeJson(); 
        	/* var details=$('#amountlist').datagrid('getData').rows[0];  */
        	var rowData=$("#amountlist").jqGrid('getRowData');
        	var f100=rowData[0].quantity;
        	var f050=rowData[1].quantity;
        	var f020=rowData[2].quantity; 
        	var f010=rowData[3].quantity; 
        	var f005=rowData[4].quantity; 
        	var f002=rowData[5].quantity; 
        	var f001=rowData[6].quantity; 
        	var f_50=rowData[7].quantity; 
        	var f_20=rowData[8].quantity; 
        	var f_10=rowData[9].quantity; 
        	var f_05=rowData[10].quantity;
        	var f_02=rowData[11].quantity;
        	var f_01=rowData[12].quantity;  
        	var _url=getRootPath()+'/cashCheck/save';
    		$('#save').attr("disabled","disabled"); 
    		var s={f100:f100,f050:f050,f020:f020,f010:f010,f005:f005,f002:f002,
   				 f001:f001,f_50:f_50,f_20:f_20,f_10:f_10,f_05:f_05,f_02:f_02,f_01:f_01};
    		var obj = $.extend(dataform,s);
    		/*  alert(JSON.stringify(obj))  */ 
    		$.post(_url,obj,function(data){
    			dataDispose(data);
    			if(data.returnCode==0){
    				$.fool.alert({time:1000,msg:data.message,fn:function(){	   						
    					$("#addBox").window("close");
    					$("#Tablelist").trigger("reloadGrid"); 
    					$('html').css('overflow','');  
    				}});
    			}else if(data.returnCode==1){
    				$.fool.alert({msg:data.message});
    			}else{
    				$.fool.alert({msg:"系统繁忙，请稍后再试。"});
    			};
    		});			 
    	 }else{
    		return false;
    	 }
    };
    
    //上次盘点金额
    function lastSaved(){
    	var _url=getRootPath()+'/cashCheck/queryLastNumber';
    	var fid=$('#fid').val();
    	var date=$('#date').datebox('getValue');
    	var subjectId=$('#subjectId').val(); 
    	if(subjectId==''||subjectId==null){
    		$.fool.alert({time:1000,msg:'请选择科目'});
    		return;
    	}
    	$.post(_url,{fid:fid,subjectId:subjectId,date:date},function(data){  
    		if(!data.subjectId){
    			return;
    		}
    		/* $('#fid').val(data.fid);
    		subjectCombo.setComboText(data.subjectName);
    		subjectCombo.setComboValue(data.subjectId); 
    		$("#subjectId").val(data.subjectId);
    		$('#date').datebox('setValue',data.date); 
    		$('#updateTime').val(data.updateTime)    		
    		$('#resume').val(data.resume);  */
    		$('#amount').val(data.amount);
    		if(getEditor("amountlist",1,"quantity")){
    			getEditor("amountlist",1,"quantity").numberbox("setValue",data.f100);
    			getEditor("amountlist",2,"quantity").numberbox("setValue",data.f050);
    			getEditor("amountlist",3,"quantity").numberbox("setValue",data.f020);
    			getEditor("amountlist",4,"quantity").numberbox("setValue",data.f010);
    			getEditor("amountlist",5,"quantity").numberbox("setValue",data.f005);
    			getEditor("amountlist",6,"quantity").numberbox("setValue",data.f002);
    			getEditor("amountlist",7,"quantity").numberbox("setValue",data.f001);
    			getEditor("amountlist",8,"quantity").numberbox("setValue",data.f_50);
    			getEditor("amountlist",9,"quantity").numberbox("setValue",data.f_20);
    			getEditor("amountlist",10,"quantity").numberbox("setValue",data.f_10);
    			getEditor("amountlist",11,"quantity").numberbox("setValue",data.f_05);
    			getEditor("amountlist",12,"quantity").numberbox("setValue",data.f_02);
    			getEditor("amountlist",13,"quantity").numberbox("setValue",data.f_01);
    		}
    		$('#amountlist').jqGrid("setRowData",1,{quantity:data.f100,money:(data.f100*100).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",2,{quantity:data.f050,money:(data.f050*50).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",3,{quantity:data.f020,money:(data.f020*20).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",4,{quantity:data.f010,money:(data.f010*10).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",5,{quantity:data.f005,money:(data.f005*5).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",6,{quantity:data.f002,money:(data.f002*2).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",7,{quantity:data.f001,money:(data.f001*1).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",8,{quantity:data.f_50,money:(data.f_50*0.5).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",9,{quantity:data.f_20,money:(data.f_20*0.2).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",10,{quantity:data.f_10,money:(data.f_10*0.1).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",11,{quantity:data.f_05,money:(data.f_05*0.05).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",12,{quantity:data.f_02,money:(data.f_02*0.02).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",13,{quantity:data.f_01,money:(data.f_01*0.01).toFixed(2)});
    		$('#amountlist').jqGrid("setRowData",14,{money:(data.amount).toFixed(2)});
    	})
    }
      	
	//反计算
	function numeration(editor$,index){
		if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
			editor$.numberbox('textbox').bind('keyup',function(){
				quentity(this,index);
			});//IE专用
		}else{
			editor$.numberbox('textbox').bind('input',function(){
				quentity(this,index);
			});
		}
	}
	
	function quentity(target,index){
		var _class$=$(target).parent().prev().attr('_class');
		/* var panel = $('#amountlist').datagrid('getPanel'); */
	    numb(index);
		if(_class$==index){//100元
			/* var number=panel.find('tr[datagrid-row-index="'+index+'"] [field="quantity"] .numberbox-f').numberbox('getText'); */
			var number=getEditor('amountlist',index,'quantity').numberbox("textbox").val();
			var _mount=(curMoney*number).toFixed(2);
		    $('#amountlist').jqGrid("setRowData",index,{money:_mount});
		    /*  panel.find('tr[datagrid-row-index="'+index+'"] [field="money"] .numberbox-f').numberbox('setValue',_mount); */
		    /* getEditor('amountlist',index,'quantity').numberbox("textbox").blur();
		    getEditor('amountlist',index,'quantity').numberbox("textbox").focus(); */ 
		    action();
		}	   
	}
	
function numb(index){//根据不同的index给curMoney付不同的币值	 
	   if(index==1){
		   curMoney=100;
	   }else if(index==2){
		   curMoney=50;
	   }else if(index==3){
		   curMoney=20;
	   }else if(index==4){
		   curMoney=10;
	   }else if(index==5){
		   curMoney=5;
	   }else if(index==6){
		   curMoney=2;
	   }else if(index==7){
		   curMoney=1;
	   }else if(index==8){
		   curMoney=0.5;
	   }else if(index==9){
		   curMoney=0.2;
	   }else if(index==10){
		   curMoney=0.1;
	   }else if(index==11){
		   curMoney=0.05;
	   }else if(index==12){
		   curMoney=0.02;
	   }else if(index==13){
		   curMoney=0.01;
	   }else{
		   curMoney=0; 
	   }
}	

//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}

function getComboData(url,mark){
	var result="";
	$.ajax({
		url:url,
		data:{num:Math.random()},
		async:false,
		success:function(data){
			if(data){
				result=data;
			}
		}
	});
	if(mark!="tree"){
		return formatData(result,"fid");
	}else{
		return formatTree(result[0].children,"text","id");
	}
}
keyHandler($("#amountlist"));
</script>
</body>
</html>