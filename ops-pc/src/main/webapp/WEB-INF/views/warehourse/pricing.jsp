<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>核价</title>
</head>
<body>
<style>
*{margin: 0px; padding: 0px;}
.body{margin-top30px;}
.body #user_radio{margin: 20px 0px 20px 20px;}
.mybtn-blue{ color: none; padding:1px 10px; background: #50b3e7;}
.btton{text-align: center; width:200px; margin: auto; margin-top: 30px; margin-bottom:30px; }
#userWindow{ width:400px; height: 230px; background:#fff; border: 1px solid #50B3E7; margin:auto; display: none; position: absolute; top:65px; left: 300px;}
#userWindow .title{ background: #50b3e7; height: 50px;text-align: center; line-height: 50px; font-size: 16px; color: #fff; font-weight: bold;}
#userWindow .p{ margin: 10px auto; width:250px; text-align: right;}
input:-webkit-autofill{outline: none;-webkit-box-shadow: 0 0 0px 1000px #fff inset; }
font{font-size: 14px;}
#userCode,#password{height:30px; line-height: 30px;}
</style>
<div class="body">
			<form id="user_radio"><input type="radio" name="user" value="1" id="user_1" checked="checked"/><label for="enable_1">本用户操作</label>
			<input type="radio" name="user" value="0" id="user_0"/><label for="enable_0">其它用户操作</label></form>			
<table id="billList-p"></table>
<!-- <div id="mypager"></div> -->
<div class="btton">
  <a class="mybtn-blue mybtn-s" id="ensure">确定</a>
  <a class="mybtn-blue mybtn-s" id="shut">关闭</a>
</div>
<div id="userWindow" style="z-index:9000;">
     <p class="title">用户</p>
     <form action="">
         <p class="p"><font>用户名:</font><input type="text" id="userCode" value="" name=""></p>
         <p class="p"><font>密码:</font><input type="password" id="password" value="" name=""></p>
     </form>
     <div class="btton">
  <a class="mybtn-blue mybtn-s" id="user_ensure">确定</a>
  <a class="mybtn-blue mybtn-s" id="user_shut">关闭</a> 
</div>
</div>
</div>
<script type="text/javascript">
var id='${param.id}';//货品列行id
var userCode='';
var password='';
$('#billList-p').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/billdetail/getBillDetails?billId='+id,
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#billList-p")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,//自动填满宽度
	height:$('#pricing-box').height()-230,
	mtype:"post",
	/* pager:'#mypager',
	rowNum:10,
	rowList:[ 10, 20, 30 ], */
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	viewrecords:true,
	forceFit:true,//调整列宽度，表格总宽度不会改变
	//height:$(window).outerHeight()-200+"px",
	colModel:[
	  		{name:'fid',label:'fid',hidden:true,width:100},
	  		{name:'unitId',label:'unitId',hidden:true,width:100},
	  		{name:'billId',label:'billId',hidden:true,width:100,formatter:function(value){
	  			return id;
	  		}},
	  		{name:'unitGroupId',label:'unitGroupId',hidden:true,width:100},
	  		{name:'goodsId',label:'goodsId',hidden:true,width:100},
	  		{name:'goodsSpecId',label:'goodsSpecId',hidden:true,width:100},
	  		{name:'goodsCode',label:'编号',width:100,align:"center",sortable:false},
	  		{name:'goodsName',label:'名称',width:100,align:"center",sortable:false},
	  		{name:'goodsSpecName',label:'货品属性',width:100,align:"center",sortable:false},
	  		{name:'unitName',label:'单位',width:100,align:"center",sortable:false},
	  		{name:'quentity',label:'数量',width:100,align:"center",sortable:false},  		
	  		{name:'unitPrice',label:'单价',width:100,align:"center",sortable:false},
	  		{name:'type',label:'金额',width:100,align:"center",sortable:false},
	  		{name:'afterPrice',label:'现单价',width:100,align:"center",sortable:false,precision:4,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{min:0,precision:4,width:'100%',height:'100%'}} */,formatter: function(value){
	  			return value?value:"";
	  		}},
	  		{name:'afterAmount',label:'现金额',width:100,align:"center",sortable:false,precision:2,editable:true,edittype:"text"/* ,editor:{type:'numberbox',options:{min:0,precision:2,width:'100%',height:'100%'}} */,formatter:function(value,options,row){
	  			 if(row.quentity&&row.afterPrice){ 
	  				return (row.quentity*row.afterPrice).toFixed(2);
	  			 }else{
	  				return value?value:"";
	  			}}},
	  		{name:'action',label:'操作',width:100,align:"center",sortable:false,formatter:function(value,options,row){
	  		 	if (row.editing){
					var s = '<a class="btn-save saveConfirm" title="确认" href="javascript:;" onclick="saverow(this)"></a> ';
					var c = '<a class="btn-back save" title="撤销" href="javascript:;" onclick="cancelrow(this)"></a>';
					return s+c;
				 } else {
					var e= '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editer(this)"></a>'; 
		  			return e;
				} 
	  		}}
	      ]
	  /*     onBeforeEdit:function(index,row){
				row.editing = true;
				updateActions(index);
			},
			onAfterEdit:function(index,row){
				row.editing = false;
				updateActions(index);
			},
			onCancelEdit:function(index,row){
				row.editing = false;
				updateActions(index);
			}	  */
})/* .navGrid('#mypager',{add:false,del:false,edit:false,search:false,view:false}) */;

//获取编辑行
function getRowIndex(target){
	var tr = $(target).closest('tr.jqgrow');
	return parseInt(tr.attr('id'));
}

function updateActions(index){
	$('#billList-p').datagrid('updateRow',{
		index: index,
		row:{}
	});
}

//获取表格里面某个编辑器方法
function _getTableEditor(index,name){
	return $("#billList-p").find('#'+index+"_"+name);
}

//列表编辑保存
function saverow(target){
	var index=getRowIndex(target);
	var q = $('#billList-p').find('tr#'+index+' td[aria-describedby=billList-p_quentity]').text();
	var afterPrice=_getTableEditor(index,"afterPrice").numberbox('getValue');
	var afterAmount=_getTableEditor(index,"afterAmount").numberbox('getValue');
	/*  var quentity=$(target).closest('[field="action"]').siblings('[field="quentity"]').find('.datagrid-cell').text(); */
    if(afterPrice==""){
		$.fool.alert({msg:'你没有修改现货品单价'});
	};
	$('#billList-p tr#'+index).form("enableValidation");
	if(!$('#billList-p tr#'+index).form("validate")){return false;}
	/* var afterAmount=(quentity*afterPrice).toFixed(2);
    $(target).closest('[field="action"]').siblings('[field="afterAmount"]').find('.numberbox-f').numberbox('setValue',afterAmount); */
	$('#billList-p').jqGrid('saveRow',index);
	$('#billList-p').jqGrid('setRowData',index, {
		editing:false,
		action:null,
		afterAmount:afterAmount
	});
}
//撤销行
function cancelrow(target){
		$('#billList-p').jqGrid('restoreRow', getRowIndex(target));
		$('#billList-p').jqGrid('setRowData', getRowIndex(target), {editing:false,action:null});
}
function editer(target){
	var index=getRowIndex(target);
	$('#billList-p').jqGrid('editRow',index);
	$('#billList-p').jqGrid('setRowData', index, {editing:true,action:null});//编辑状态转换，按钮变化
	/*var ed = $('#billList-p').datagrid('getEditor', {index:index,field:'afterAmount'});
	$(ed.target).numberbox('disable');*/  //金额框不可编辑
	var editor$ =_getTableEditor(index,'afterPrice');//现单价
	editor$.attr('_class','afterPrice');
	editor$.numberbox({
		validType:['intOrFloat',"myassetValue"],
		width:'100%',
		height:28,
		required:true,
		novalidate:true,
		precision:4,
		/* onChange:function(newVal,oldVal){
           alert(1);
		} */
	    });
	numeration(editor$,index);
	var editor$ =_getTableEditor(index,'afterAmount');//现金额
	editor$.attr('_class','afterAmount');
	editor$.numberbox({
		validType:['intOrFloat',"assetValue"],
		width:'100%',
		height:28,
		required:true,
		novalidate:true,
		precision:2,
		/* onChange:function(newVal,oldVal){
           alert(1);
		} */
	    });
	numeration(editor$,index);
}
$.extend($.fn.validatebox.defaults.rules, {
	myassetValue:{
  	  validator:function(value){
  		  if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
  			  if($(this).attr("class").indexOf("validatebox-text")!=-1){
  				  $(this).unbind("blur");
  				  $(this).blur(function(){//对数字进行去空格和自动保留2位小数
  					    var value = $(this).val();
          				var myvalue = "";
          				if(value!="" && !isNaN($.trim(value))){
	          					var nv = $.trim(value)+"";
	          					myvalue = parseFloat(nv).toFixed(4)+'';
          				}
	      					if($(this).attr("class").indexOf("textbox-text")!=-1){
	      						$(this).parent().prev().textbox('setText',myvalue);
	      						$(this).parent().prev().textbox('setValue',myvalue);
	      					}else{
	      						$(this).val(myvalue);
	      					}
          		});
          		}
        	  }
  		  return /^\d+(\.\d{1,4})*$/i.test(value);
  	  },
  	  message:'请输入大于等于0的数，且小数位最多4位'
     } 
});
//反计算
function numeration(editor$,index){
	if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
	editor$.numberbox('textbox').bind('propertychange',function(){			
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
	 var panel = $('#billList-p');
	if(_class$=="afterPrice"){//改变现单价	  
	   var _quentity=panel.find('tr#'+index+' td[aria-describedby=billList-p_quentity]').text();//数量
	   var _afterPrice=_getTableEditor(index,'afterPrice').numberbox('getText');
	   //var _afterPrice=$(target).parent().prev().numberbox('getText');
	   var _afterAmount=(_quentity*_afterPrice).toFixed(2);
	   _getTableEditor(index,'afterAmount').numberbox('setValue',_afterAmount);
	}
	if(_class$=="afterAmount"){//改变现金额
		var _quentity=panel.find('tr#'+index+' td[aria-describedby=billList-p_quentity]').text();//数量
		var _afterAmount=_getTableEditor(index,'afterAmount').numberbox('getText');
		var _afterPrice=(parseFloat(_afterAmount)/parseFloat(_quentity)).toFixed(4);
		_getTableEditor(index,'afterPrice').numberbox('setValue',_afterPrice);
	}
}

//点击关闭，关闭窗口
$('#shut').click(function(){
	$('#pricing-box').window('close'); 
});

//点击确定操作
$('#ensure').click(function(){
	var _dataPanel = $('#billList-p');
	var _editing = _dataPanel.find(".btn-save");	
	if(_editing.length>0){
		$.fool.alert({msg:'你还有没编辑完,请先确认！'});
		return false;
	};
	var options = $("#user_radio").serializeJson();
	//获取货品列表的所有数据
	var details = $("#billList-p").jqGrid('getRowData');	
	var details = JSON.stringify(details);	
	details = details.replace(new RegExp("fid","gm"),"billDetailId");
	details = details.replace(new RegExp("unitPrice","gm"),"beforePrice"); 
	details = details.replace(new RegExp("type","gm"), "beforeAmount");
		
	/* var deta =[];//定义一个json数组
	for(var i=0;i<details.length;i++){
		var billDetailId=details[i].billDetailId;//每条货品信息id
		var goodsId=details[i].goodsId;//货品id
	    var goodSpecId=details[i].goodSpecId;//货品属性id
	    var unitId=details[i].unitId;// 单位id
	    var beforePrice=details[i].unitPrice; //修改前价格,单价
	    var afterPrice=details[i].type; //修改后价格,金额
	    var beforeAmount=details[i].unitPrice; //修改前金额,现核价
	    var afterAmount=details[i].type; //修改后金额
	    var arr  =
	     {
	         "billDetailId" :billDetailId,
	         "goodsId" : goodsId,
	         "goodSpecId":goodSpecId,
	         "unitId":unitId,
	         "beforePrice":beforePrice,
	         "afterPrice":afterPrice,
	         "beforeAmount":beforeAmount,
	         "afterAmount":afterAmount, 
	     }
	    deta.push(arr);
	}
	deta = JSON.stringify(deta) */
	if(options.user==1){
		push('', '', id, details);
	}else if(options.user==0){
		$('#userWindow').css('display','block');
		$('#user_ensure').unbind('click').click(function(){
			userCode=$('#userCode').val();			
			password=$('#password').val();
			
			var isValid = true;			
			if($.trim(userCode).length === 0){
				isValid = false;
				$('#userCode').val('');
				$('#userCode').validatebox({required: true});
			}
			if($.trim(password).length === 0){
				isValid = false;
				$('#password').val('');
				$('#password').validatebox({required: true});
			}
			if(isValid){
				$('#userWindow').css('display','none');		
				push(userCode, password, id, details);			
			}
		});	
	}
});

function push(userCode, password, id,details){
	$.ajax({
		type : 'post',
		url : '${ctx}/pricing/checkPrice',
		data : {billId : id, userCode :userCode, password :password, details : details},  
		dataType : 'json',
		success : function(data) {
			if(data.returnCode==0){
				$.fool.alert({msg:'核价成功',fn:function(){
					 $('#pricing-box').window('close'); 
					 myrefreshForm();
	    		}});
			}else if(data.returnCode==1){
				$.fool.alert({msg:data.message,fn:function(){    			
	    		}});
			}
		},
		});
}

//关闭按钮
$('#user_shut').click(function(){
	$('#userWindow').css('display','none');
});


</script>
</body>
</html>
