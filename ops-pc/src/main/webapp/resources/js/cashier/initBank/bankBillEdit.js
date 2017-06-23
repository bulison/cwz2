/**
 * 
 */
function initManage(){
	if($("#view").val()=="1"){
	  $("#bankLessForm").fool('fromEnable',{'enable':false});
	  $(":input").attr("disabled",true);
	}
	dateBox($("#settlementDate"), false);
	dateBox($("#voucherDate"), false);
	comboTree($("#settlementTypeId"),getRootPath()+"/basedata/findSubAuxiliaryAttrTree?code=015",false,164,33,false,function(combo){
		var settlementTypeId = $("#settlementType").val();
		if(settlementTypeId != ''){
			combo.setComboValue(settlementTypeId);
		}
	},null,{
        name:"结算方式",
        addUrl:"/basedata/listAuxiliaryAttr",
        refresh:true
    });
	comboTree($("#resume"),getRootPath()+"/basedata/findSubAuxiliaryAttrTree?code=014",false,164,33,true,function(combo){
			if($("#resumeText").val() != ''){
				combo.setComboText($("#resumeText").val());
			}
	},function(node){

	},{
        name:"摘要",
        addUrl:"/basedata/listAuxiliaryAttr",
        refresh:true
    });
	$("#debit").validatebox({
		required:true,
		novalidate:true,
		validType:"amount"
	});
	$("#credit").validatebox({
		required:true,
		novalidate:true,
		validType:"amount"
	});
	$("#debit").bind("change",function(e){
		if(!isNaN($(this).val())){
			var nv = $(this).val()+"";
			value = parseFloat(nv).toFixed(2)+'';
			$(this).val(value);
		}
	});
	$("#credit").bind("change",function(e){
		if(!isNaN($(this).val())){
			var nv = $(this).val()+"";
			value = parseFloat(nv).toFixed(2)+'';
			$(this).val(value);
		}
	});
	//借贷方金额初始化
	var debit = $('#debit').val();
	var credit = $('#credit').val();
	if($("#fid").val()){
		if(debit==0||debit==""){
			$("#debit").val(0);
			$("#debit").attr("disabled",true);
		}
		if(credit==0||credit==""){
			$("#credit").val(0);
			$("#credit").attr("disabled",true);
		}
	};
	//借贷方金额操作
	$("#debit").keyup(function(){
		if($("#debit").val()&&$("#debit").val()!=0){
			$("#credit").val(0);
			$("#credit").attr("disabled",true);
			$("#credit").validatebox("validate");
		}else{
			$("#credit").val("");
			$("#credit").removeAttr("disabled",true);
			$("#credit").validatebox("validate");
		}
	});
	$("#credit").keyup(function(){
		if($("#credit").val()&&$("#credit").val()!=0){
			$("#debit").val(0);
			$("#debit").attr("disabled",true);
			$("#debit").validatebox("validate");
		}else{
			$("#debit").val("");
			$("#debit").removeAttr("disabled",true);
			$("#debit").validatebox("validate");
		}
	});
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
	
	$("#bankSave").click(function(){
		/*if(!rule()){
			return false;
		}*/
		if(!$("#bankLessForm").fool('fromVali')){
			return false;
		}
		var fdata = $("#bankLessForm").serializeJson();
		var  _bankInitId   = $("#bankInitId").val();
		var _type = $("#type").val();
		var _resume =  $("#resume").next()[0].comboObj.getComboText();
		fdata = $.extend(fdata, {
			bankInitId : _bankInitId,
			type : _type,
			resume:_resume
		});
		$.post(getRootPath()+'/cashierBankBillController/save',fdata,function(data){
			if(data.returnCode =='0'){
				$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
					refreshBankData();
					$("#bank-edit-window").window('close');
					return true;
				}});
			}else{
				$.fool.alert({msg:data.message});
				return false;
			}
		}); 
	});
	//经手人
	$("#memberName").click(function(){
		     editFlag=true;
			 win = $("#pop-win").fool('window',{'title':"选择",height:480,width:780,
				href:getRootPath()+'/member/window?okCallBack=selectMember&onDblClick=selectMember2&singleSelect=true'});
	}).validatebox({required:true,novalidate:true});
}
function selectMember(rowData){
	$("#memberName").focus();
	$("#memberId").val(rowData[0].fid);
	$("#memberName").val(rowData[0].username);
	win.window("close");
}
function selectMember2(rowData){
	$("#memberName").focus();
	$("#memberId").val(rowData.fid);
	$("#memberName").val(rowData.username);
	win.window("close");
}


function comboTree(obj, url, required,_width,_height,_editable, onLSFn, onSel, tools) {
	obj.fool("dhxCombo",{
		required : required,
		missingMessage : '该项不能为空！',
		novalidate : true,
		data : getComboData(url,"tree"),
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		toolsBar:tools,
		width : _width,
		height : _height,
		editable : _editable,
		onLoadSuccess : onLSFn,
		onChange : onSel
	});
}
function dateBox(obj,required,onLSFn){
	var _id = $("#fid").val();
	if(_id==''){
		obj.datebox({
			required:required,
			missingMessage:'该项不能为空！',
			novalidate:true,
			width:162,
			height:34,
			editable:false,
			value:' ',
			onLoadSuccess:onLSFn
		});
	}else{
		obj.datebox({
			required:required,
			missingMessage:'该项不能为空！',
			novalidate:true,
			width:162,
			height:34,
			editable:false,
			onLoadSuccess:onLSFn
		});
	}
}

/*function rule(){
	var debit = parseFloat($("#debit").val());
	var credit = parseFloat($("#credit").val());
	if(credit > 0 && debit > 0){
		$.fool.alert({msg:"如果借方金额不等于0，贷方金额为0，如果贷方金额不等于0，借方金额为0"});
		return false;
	}
	if(credit ==0 && debit==0){
		$.fool.alert({msg:"贷方和借方金额不能同时为0"});
		return false;
	}
	return true;
}*/
