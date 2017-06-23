/**
 * 出纳管理JS
 * balance,daily,cashJournal编辑页
 */
$('.clearBill').click(function(){
	settlementTypeCombo.setComboValue("");
	settlementTypeCombo.setComboText("");
});
if($('#memberName').length>0){
	$('#memberName').fool('combogrid',{
		required:true,
		novalidate:true,
		width:160,
		height:31,
		idField:'fid',
		textField:'username',
		validType:"combogridValid['memberId']",
		focusShow:true,
		panelWidth:450,
		panelHeight:283,
		fitColumns:true,
		focusShow:true,
		url:getRootPath()+'/member/vagueSearch?searchSize=8',
		columns:[[
			{field:'fid',title:'fid',hidden:true},
			{field:'userCode',title:'编号',width:100,searchKey:false},
			{field:'jobNumber',title:'工号',width:100,searchKey:false},
			{field:'username',title:'名称',width:100,searchKey:false},
			{field:'phoneOne',title:'电话',width:100,searchKey:false},
			{field:'deptName',title:'部门',width:100,searchKey:false},
			{field:'position',title:'职位',width:100,searchKey:false},
			{field:'searchKey',hidden:true,searchKey:true},
		          ]],
		onSelect:function(index,row){
			$("#memberId").val(row.fid);
			$("#memberName").val(row.username).focus();
			$('#memberName').combo('enableValidation').combo('validate');
		},
		onChange:function(nv,ov){
			$("#memberId").val('');
		}
	});
}
var cashSubject='',bankSubject='';
$('#type').val()==5?cashSubject=1:bankSubject=1;
/*$('#subjectName').fool("subjectCombobox",{
	width:160,
	height:32,
	url:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&cashSubject="+cashSubject+"&bankSubject="+bankSubject,
	focusShow:true,
	onClickIcon:function(){
		mysubjectName();
		$(this).combobox('hidePanel');
    	$(this).combobox('textbox').blur();
	},
	onSelect:function(record){
		$('#subjectId').val(record.id);
	}
});*/
var subjectComboObj=""; 
function initSubject(flag){
	var data="";
	var filterUrl="";
	if(flag==0){
		data=getComboData(getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&q=");
		filterUrl=getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&bankSubject=1";
	}else{
		data=getComboData(getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&cashSubject=1&q=");
		filterUrl=getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&cashSubject=1";
	}
	subjectComboObj = $('#subjectName').fool('dhxComboGrid',{
		  prompt:"科目",
		  width:160,
		  height:32,
		  focusShow:true,
		  // value:$('#subjectId').val(),
		  // text:$('#subjectName').val(),
		  data:data,
		  filterUrl:filterUrl,
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
		  onChange:function(value,text){
			  var row = $('#subjectName').next()[0].comboObj.getSelectedText();
			  if(row){
				  $('#subjectId').val(value);
			  }
		  }
	});
	$('#subjectName').next().find(".dhxcombo_select_button").click(function(){
		subjectComboObj.closeAll();
		mysubjectName();
	});
}

var chooserWindow = "";
//科目弹出框
function mysubjectName(){
	  chooserWindow=$.fool.window({width:780,height:480,'title':"选择科目",href:getRootPath()+'/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubjectDBC&cashSubject='+cashSubject+'&bankSubject='+bankSubject+'&flag=1&singleSelect=true',
		  onLoad:function(){($('#subjectName').next())[0].comboObj.closeAll();}});//解决IE点击弹窗下拉框不消失问题
}
function selectSubject(rowData){
	  if(rowData[0].flag!=1){
		  $.fool.alert({time:1000,msg:"请选择子节点"});
		  return false;
	  }
	  subjectComboObj.setComboValue(rowData[0].fid); 
	  subjectComboObj.setComboText(rowData[0].name);
	  /*$("#subjectName").combo("setText",rowData[0].name);*/
	  $("#subjectId").val(rowData[0].fid);
	  chooserWindow.window('close');
}
function selectSubjectDBC(rowData){
	  if(rowData.flag!=1){
		  $.fool.alert({time:1000,msg:"请选择子节点"});
		  return false;
	  }
	  //$("#subjectName").combo("setValue",rowData.fid);
	  subjectComboObj.setComboValue(rowData.fid);
	  subjectComboObj.setComboText(rowData.name);
	  /*$("#subjectName").combo("setText",rowData.name);*/
	  $("#subjectId").val(rowData.fid);
	  chooserWindow.window('close');
}
/*$('#subjectName').combobox('reload',getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&cashSubject="+cashSubject+"&bankSubject="+bankSubject);
$('#subjectName').combobox('textbox').focus(function(){
	$('#subjectName').combobox('showPanel');
});*/
$('#voucherDate').fool('datebox',{inputDate:true});
$("#settlementDate").fool('datebox',{inputDate:true});
$('#debit').focus(function(){
	$('#debit').select();
});
$('#debit').bind('keydown',function(e){
	if(e.keyCode == 39){
		e.preventDefault();
		var $t1 = $(this);
		var $t2 = $('#credit');
		$t2.attr('disabled')=='disabled'?($.fool.confirm({title:'提示',msg:'确认转换到贷方金额？',fn:function(data){
	    	if(data){
	    		$t2.removeAttr('disabled');
	    		$t2.val($t1.val());
	    		$t1.val('0');
	    		$t1.attr('disabled','disabled');
	    		$t2.focus();
	    	}else{
	    		return false;
	    	}
	    }})):($t2.val()==''||$t2.val()==0)&&($t1.val()==''||$t1.val()==0)?$t2.focus():($t2.focus(),$t1.focus());
	}
});
$('#credit').focus(function(){
	$('#credit').select();
});
$('#credit').bind('keydown',function(e){
	if(e.keyCode == 37){
		e.preventDefault();
		var $t1 = $(this);
		var $t2 = $('#debit');
		$t2.attr('disabled')=='disabled'?($.fool.confirm({title:'提示',msg:'确认转换到借方金额？',fn:function(data){
	    	if(data){
	    		$t2.removeAttr('disabled');
	    		$t2.val($t1.val());
	    		$t1.val('0');
	    		$t1.attr('disabled','disabled');
	    		$t2.focus();
	    	}else{
	    		return false;
	    	}
	    }})):($t2.val()==''||$t2.val()==0)&&($t1.val()==''||$t1.val()==0)?$t2.focus():($t2.focus(),$t1.focus());
	}
});
var cashkd = '';
//屏蔽默认快捷键
$(document).keydown(function(e){
	if(e.ctrlKey && e.keyCode == 83){//ctrl+s
		if (e.preventDefault) {  // firefox
            e.preventDefault();
        } else { // other
            e.returnValue = false;
        }
	}
});
$(document).bind('keydown',cashkd = function(e){
	if(e.ctrlKey && e.keyCode == 83){//ctrl+s
		setTimeout(function(){($('#subjectName').next())[0].comboObj.closeAll();},500);//防止下拉框不消失
		$('#save').click();
		$(document).unbind('keydown',cashkd);
	}
});

