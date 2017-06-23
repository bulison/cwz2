<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>操作栏目</title>
</head>
<body>
<form action="" class="form1" id="detail-Form">
<input type="hidden" name="fid" value="${vo.fid}"/>
<input type="hidden" name="updateTime" value="${vo.updateTime}"/>
<input type="hidden" id="kjkm-code" name="subjectId" value="${vo.subjectId}"/>


<p><font>会计科目：</font><input name="subjectName" id="kjkm" value="${vo.subjectName}" type="text" data-options="{required:true,novalidate:true,missingMessage:'请选择科目'}" readonly="readonly"/></p>
<p><font>多栏账名称：</font><input name="name" _class="textbox" id="dlzmc" value="${vo.name}" data-options="{required:true,novalidate:true,missingMessage:'请输入栏目名称',validType:['maxLength[50]','isBank']}" class="textBox" type="text"/></p>
<p id="hsxmlb" style="display: none;"><font>核算项目类别：</font><input name="auxiliaryType" id="hsxmlb-select" data-options="{width:160,height:30,panelHeight:'auto',valueField:'id',textField:'text'}" class="textBox" type="text"/></p>

<div class="toolbar" style="margin-top: 20px;">
<a href="javascript:;" class="btn-orange btn-s" onclick="autoArrange(true)" >自动编排</a>
<a href="javascript:;" class="btn-orange btn-s" onclick="addRow()" >新增一行</a>
<a href="javascript:;" class="btn-orange btn-s" onclick="insertRow()" >插入行</a>
<a href="javascript:;" class="btn-orange btn-s" onclick="delRow()" >删除当前行</a>
<input type="checkbox" id="ajbszdlz" checked="checked"/><label for="ajbszdlz">按级别设置多栏账</label>
<span class="ajbszdlz-span" style="">
<input type="text" class="textBox easyui-numberspinner ajbszdlz-value" name="level" value='1' style="width:60px;"/>
</span>
</div>
<table id="column-table"></table>

</form>

<div class="btn-footer">
<a href="javascript:;" class="btn-blue btn-s save-form">保存</a>
<!-- <a href="javascript:;" class="btn-blue btn-s" onclick="function(){$('#column-win').window('close').window('clear')}">关闭</a> -->
</div>

<script>
var popWin;
function openWin(setting){
	popWin=$.fool.window(setting);
}

function closeWin(){
	if(popWin)popWin.window('close').window('clear');
}


function subjectChooser(){
	openWin({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubject&singleSelect=true'});
}

function initSubjectBox(){
	var $opt = $.fool._opts$($("#kjkm"));
	$opt = $.extend({width:160,height:30,editable:false,},$opt);
	$("#kjkm").textbox($opt).textbox('textbox').click(function(){
		subjectChooser();
	});
}

function selectSubject(rowData){
	var _fid = $("#kjkm-code").val();
	var _init = true;
	if(rowData){
		var _d = getDataTop1(rowData);
		$("#kjkm-code").val(_d.fid);
		$("#kjkm").textbox('setValue',_d.name);
		_fid = _d.fid;
		_init = false;
	}else{
		cleanTable(true);
	}
	 if(_fid){
		 $.ajax({
				type : 'post',
				url : getRootPath()+'/multiColumnSetting/getSubjectMsg',
				data : {subjectId :_fid},
				dataType : 'json',
				success : function(data) {
					if(data){
						var _lists = data.accountSignMsg;
						if(_lists){
							var _tmpComBoData =[];
							var _tmpJson = {};
							var i = 0;
							for(var _key in _lists){
								_tmpJson.value = _key;
								_tmpJson.text =  _lists[_key];
								_tmpComBoData[i] = _tmpJson;
								_tmpJson = {};
								i++;
							}
							
							($('#hsxmlb-select').next())[0].comboObj.clearAll(false);
							($('#hsxmlb-select').next())[0].comboObj.addOption(_tmpComBoData);
							if(${!empty vo.auxiliaryType}){
								($('#hsxmlb-select').next())[0].comboObj.setComboValue('${vo.auxiliaryType}');
							}
							showHsxmlb(true,_init);						
						}else{
							showHsxmlb(false,_init,"clear");
						}
					}else{
						showHsxmlb(false,_init);
					}
	    		},
	    		error:function(){
	    			alert("!!!");
	    		}
		 });
	 }
	 
	closeWin();
	
}

function showHsxmlb(b,init,clear){
	cleanTable(init);
	if(clear=="clear"){
		($("#hsxmlb-select").next())[0].comboObj.setComboValue("");
		($("#hsxmlb-select").next())[0].comboObj.setComboText("");
	}
	if($("#ajbszdlz").is(":checked")){
		$("#ajbszdlz").click();
	}
	
	if(b){
		($('#hsxmlb-select').next())[0].comboObj.enable();
		$("#hsxmlb").show();
	}else{
		($('#hsxmlb-select').next())[0].comboObj.disable();
		$("#hsxmlb").hide();
	}
}

function addRow(data){
	if(!validSubject())return;
	
	if(!validLastEditRow())return;
	
	if(!data){
		if(!editHelp(-1,true)){} 
	}else{	
		$("#column-table").datagrid('appendRow',{
			auxiliaryAttrId:data.auxiliaryAttrId,
			direction:data.direction,
			directionName:data.directionName,
			auxiliaryAttrCode: data.auxiliaryAttrCode,
			auxiliaryAttrName: data.auxiliaryAttrName,
			subjectId: data.subjectId,
			_directionName:data.directionName,
			_auxiliaryAttrCode:data.auxiliaryAttrCode,
			_auxiliaryAttrName:data.auxiliaryAttrName,
		});	
	}
	
	return false;
}

function insertRow(value){
    if(!validSubject())return;
    if(!validLastEditRow())return;
	var _data = $("#column-table").datagrid("getData");
	if(_data&&_data.length==0){
		editHelp();
		return;
	}
	
	var row = $("#column-table").datagrid('getSelected');
	if (!row) {
		$.fool.alert({msg:'请先选择要插入的位置'});
		return;
	}
	var rowIndex = $("#column-table").datagrid('getRowIndex', row);

	//$("#column-table").datagrid('beginEdit',rowIndex);
	editHelp(rowIndex);
}

function delRow(value){
	var row = $("#column-table").datagrid('getSelected');
	
	 if (row) {
		 $.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
				if(r){
					var rowIndex = $("#column-table").datagrid('getRowIndex', row);
			        $("#column-table").datagrid('deleteRow', rowIndex); 
				}
			},title:'确认'});
	 }else{
		 $.fool.alert({msg:'请先选择要删除的行'});
	 }
}


function autoArrange(){	
	if(!validSubject()){
		return;
	}
	
	getEditTableData(true,function(data){
		cleanTable();
		if(data.length>0){
			for(var i in data){
				addRow(data[i]);
			}
		}else{
			$.fool.alert({msg:'该科目无下级科目且无辅助核算项目'});
		}
	});
	
	return false;
}

function validSubject(){
	var subjectId = $("#kjkm-code").val();
	if(!subjectId||subjectId.trim().length==0){
		$("#kjkm").textbox('enableValidation');
		$.fool.alert({msg:'请先选择会计科目'});
		return false;
	}
	
	if(!$("#hsxmlb").is(':hidden')){
		var auxiliaryType = ($('#hsxmlb-select').next())[0].comboObj.getSelectedValue();
		if(!auxiliaryType||auxiliaryType.length==0){
			$(($('#hsxmlb-select').next())[0].comboObj.getInput()).validatebox('enableValidation');
			$.fool.alert({msg:'请选择核算项目类别'});
			return false;
		}
	}
	
	return true;
}

function getEditTableData(auto,fn){
	var autoVal = auto ? 1 : 0;
	var subjectId = $("#kjkm-code").val();
	var auxiliaryType = ($('#hsxmlb-select').next())[0].comboObj.getSelectedValue();
	var level = $(".ajbszdlz-value").numberbox('getValue');
	
	$.post(getRootPath()+'/multiColumnSetting/autoArrange',
			{'subjectId':subjectId,'auxiliaryType':auxiliaryType,'level':level,'flag':autoVal},
			function(data){
				if(!data || typeof data == 'object'){
					eval(fn(data));
				}else{
					$.fool.alert({msg:'数据获取异常，请稍后再试'});
				}
	});
}

function getTableEditor(index,field){
	return $("#column-table").fool('getEditor$',{'index':index,'field':field});
}

function editHelp(index,isAppend){
	//alert(index);
	var _comboOPT = get$Opt({width:'100%',height:'100%'});//$.extend({},comboOPT,{width:'100%',height:'100%'});
	getEditTableData(false,function(data){
		if(data==null){ 
			$("#column-table").datagrid('deleteRow',index);
			return;
		}
		
		if(data.length<=0){
			$.fool.alert({msg:'该科目无下级科目且无辅助核算项目'});
			//$("#column-table").datagrid('deleteRow',index);
			return false;
		}
		
		if(isAppend=='edit'){
			
		}else if(isAppend==true){
			$("#column-table").datagrid('appendRow',{});
			var _data = $("#column-table").datagrid("getData");
			index = _data.rows.length-1;
		}else{
			$("#column-table").datagrid('insertRow',{index: index,row:{}});
		}
		
		$("#column-table").datagrid('beginEdit',index);
		
		var fs = [{id:-1,text:'贷'},{id:1,text:'借'}];
		var editor$ = getTableEditor(index,"directionName");
		var $opt = $.extend(_comboOPT,{valueField:'id',textField:'text',editable:false,onChange:function(newVal,oldVal){
			getTableEditor(index,"direction").val(newVal);
			getTableEditor(index,"_directionName").val(newVal==1?"借":"贷");
		}});
		editor$.combobox($opt).combobox('loadData',fs).combobox('setValue',getTableEditor(index,"direction").val());
		
		var _combD = [];
		var _tmpJson = {};
		for(var i=0;i<data.length;i++){
			_tmpJson.id = data[i].auxiliaryAttrCode;
			_tmpJson.text =  data[i].auxiliaryAttrName;
			_tmpJson.subjectId =  data[i].subjectId;
			_tmpJson.auxiliaryAttrId = data[i].auxiliaryAttrId;
			_combD[i] = _tmpJson;
			_tmpJson = {};
		}
		editor$ = $("#column-table").fool('getEditor$',{'index':index,'field':"auxiliaryAttrCode"});
		$opt = $.extend(_comboOPT,{valueField:'text',textField:'id',editable:false,onChange:function(newVal,oldVal){
			//newVal = newVal.trim().length==0 ? oldVal : newVal;
			var  combd = $(this).data("combd");
			var _auxiliaryAttrName = "";
			for(var _i in combd){
				if(newVal==combd[_i].text){
					getTableEditor(index,"subjectId").val(combd[_i].subjectId);
					getTableEditor(index,"_auxiliaryAttrCode").val(combd[_i].id);
					getTableEditor(index,"auxiliaryAttrId").val(combd[_i].auxiliaryAttrId);
					getTableEditor(index,"_auxiliaryAttrName").val(combd[_i].text);
					_auxiliaryAttrName = combd[_i].text;
					break;
				}
			}
			
			//if($(this).parents('tr').find("td[field='auxiliaryAttrName'] div").length>=0)
			$(this).parents('tr').find("td[field='auxiliaryAttrName'] div").text(_auxiliaryAttrName);
			
		}});
		editor$.combobox($opt).combobox('loadData',_combD).data("combd", _combD);
		var _attName = getTableEditor(index,"_auxiliaryAttrName").val();
		if(_attName)editor$.combobox('setValue',_attName);
		
		$("#column-table").datagrid('selectRow',index);
		$("#column-table").datagrid("options")._currIndex=index;
		
	});
}

function validLastEditRow(index, row){
	if(-1==index)return true;
	var _ind = $("#column-table").datagrid("options")._currIndex;
	var v = $("#column-table").datagrid('validateRow',_ind);
	if(v){
		$("#column-table").datagrid("endEdit",_ind);
		$("#column-table").datagrid("options")._currIndex = -1;
	}else
		$("#column-table").datagrid("unselectRow",index);
	return v;
}

function directionNameAction(value,row,index){
	return row._directionName?row._directionName:value?value:row.direction==1?"借":"贷";
}

function auxiliaryAttrCodeAction(value,row,index){
	return row._auxiliaryAttrCode?row._auxiliaryAttrCode:value;
}

function auxiliaryAttrNameAction(value,row,index){
	return row._auxiliaryAttrName?row._auxiliaryAttrName:value;
}

$(function(){
	//初始化
	$("#detail-Form").find("input[_class]").each(function(i,n){inputInit($(this));});
	
	//初始化表格
	$('#column-table').datagrid({
		toolbar:'.toolbar',
		singleSelect:true,
		_currIndex:-1,
		_data:undefined,
		_init:false,
		/* data:data, */
        columns: [[ 
	         {"field":"auxiliaryAttrId","title":"id",hidden:true,editor:{type:'text'}},
	         {"field":"_directionName","title":"方向名值",hidden:true,editor:{type:'text'}},
	         {"field":"_auxiliaryAttrName","title":"栏目名值",hidden:true,editor:{type:'text'}},
	         {"field":"_auxiliaryAttrCode","title":"栏目CODE值",hidden:true,editor:{type:'text'}},
	         
	         {"field":"_auxiliaryAttrId","title":"全选",checkbox:true},  
	         {"field":"direction","title":"方向值",hidden:true,width:100,editor:{type:'text'}},
	         {"field":"directionName","title":"方向",width:100,editor:{type:'text'},formatter:directionNameAction},  
	         {"field":"auxiliaryAttrCode","title":"核算项目编号",width:100,editor:{type:'text'},formatter:auxiliaryAttrCodeAction},  
	         {"field":"auxiliaryAttrName","title":"栏目名称",width:100,formatter:auxiliaryAttrNameAction},//,editor:{type:'text'}
	         {"field":"subjectId","title":"科目ID",hidden:true,width:100,editor:{type:'text'}},
        ]],
        onClickRow:validLastEditRow,
        /* onClickCell:function(index, field, value){
        	var _ind = $("#column-table").datagrid("options")._currIndex;
        	var v = $("#column-table").datagrid('validateRow',_ind);
        	if(v){
        		$("#column-table").datagrid("endEdit",_ind);
        		$("#column-table").datagrid("options")._currIndex = -1;
        	}else
        		$("#column-table").datagrid("unselectRow",index);
        }, */
        onDblClickRow:function(index, row){
        	var _opt = $("#column-table").datagrid("options");
        	var _ind = _opt._currIndex;
        	if(_ind==-1){
        		//_opt._currIndex = index;
        		//$("#column-table").datagrid("beginEdit",index);
	        	editHelp(index,"edit");
        	}
        },
	});
	
	//保存按钮
	$(".save-form").click(function(){
		if(!validSubject())return;
		
		$("#dlzmc").textbox('enableValidation');
		
		if(!validLastEditRow(-1))return;
		
		if(!$("#detail-Form").fool('fromVali'))
			return false;		
		
		var details = $("#column-table").datagrid('getRows');
		if(details.length<=0){
			$.fool.alert({msg:'你还没有添加任何核算项目'});
			return false;
		}
		var _ind = $("#column-table").datagrid("options")._currIndex;
		validLastEditRow(_ind);
		if($("#column-table").datagrid("options")._currIndex!=-1){
			$.fool.alert({msg:'你还有没编辑完成的核算项目,请先保存！'});
			return false;
		}
		var fdata = $("#detail-Form").serializeJson();
		fdata = $.extend(fdata,{'details':JSON.stringify(details)});
		$.post(getRootPath()+'/multiColumnSetting/save',fdata,function(data){
			if(data.returnCode =='0'){
	    		$.fool.alert({msg:'保存成功！',fn:function(){
	    			$("#column-win").window('close').window('clear');
                    BindColCombo();
	    			return true;
	    		}});
	    	}else{
	    		$.fool.alert({msg:data.message});
	    		return false;
			}
	    });
	});
	
	initSubjectBox();

    function BindColCombo() {
    	var selected=($("#column").next())[0].comboObj.getSelectedValue();
        $('#column').fool("dhxCombo",get$Opt({
            data:getComboData('${ctx}/multiColumnSetting/list'),
            required:true,
            novalidate:true,
            editable:false,
            focusShow:true,
            value:selected,
            setTemplate:{
                input:"#name#",
                option:"#name#"
            },
            onLoadSuccess:function(){
                ($("#column").next())[0].comboObj.deleteOption("")
            },
        }));
    }
	//复选框
	$("#ajbszdlz").click(function(){
		if($(this).is(":checked")){
			$(".ajbszdlz-span").show();
		}else{
			$(".ajbszdlz-span").hide();
			$(".ajbszdlz-value").numberbox('setValue',1);
		}
	});
	//下拉框
	/* $('#hsxmlb-select').combobox({
		required:true,
		novalidate:true,
		missingMessage:'请选择核算项目类别',
		onChange:function(newVal,oldVal){
			cleanTable();
		}
	}); */
	$('#hsxmlb-select').fool("dhxCombo",get$Opt({
		required:true,
		novalidate:true,
		missingMessage:'请选择核算项目类别',
	    focusShow:true,
	    /* value:"${vo.auxiliaryType}", */
	    setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		onChange:function(newVal,oldVal){
			cleanTable();
		}
	}));
	if("${vo.auxiliaryType}"){
		$('#hsxmlb-select').next()[0].comboObj.setComboValue("${vo.auxiliaryType}");
	}
	
	/* $('#hsxmlb-select').fool("dhxCombo",{
		required:true,
		novalidate:true,
		missingMessage:'请选择核算项目类别',
	    setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		onChange:function(newVal,oldVal){
			cleanTable();
		}
	}); */
	var data=eval('${vo.details}');
	if(data){
		for(var i=0;i<data.length;i++){
			if(data[i].direction==0){
				data[i]._directionName="贷";
			}else if(data[i].direction==1){
				data[i]._directionName="借";
			}
			data[i]._auxiliaryAttrName=data[i].auxiliaryAttrName;
			data[i]._auxiliaryAttrCode=data[i].auxiliaryAttrCode;
			data[i]._auxiliaryAttrId=data[i].auxiliaryAttrId;
		}
		$('#column-table').datagrid("loadData",data);
	}
	selectSubject();
});

function cleanTable(init){
	var _td = init ? ${empty vo.details ? '[]' : vo.details} : [];
	$("#column-table").datagrid('loadData',_td);
	$("#column-table").datagrid("options")._currIndex = -1;
}
</script>
</body>
</html>