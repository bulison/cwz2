/**
 * 仓储应用详情页模板JS
 */
$(function(){});

//全局变量
var goodsIndex2 = '';
var actionC2 = [];   //标识是不是编辑状态
var actionCl2 = [];  //标识是否点击了操作单元格
var unitData2_ = [];
var mydetails2 = '';
var myobj2 = [];
var mynum2 = 0;
//var addkd2 = "";
var newIndex2 = 0;//新建行的行号全局变量
/*
 * 新模板详情页初始化
 * flag-编辑状态，billStat-单据状态,details-货品数据
 */
function initEdit2(flag,billStat,billCode,billCodeName,details){
	_billCode = billCode;
	_billCodeName = billCodeName;
	_flag = flag;
	_billStat = billStat;
	if(details){
		mydetails2 = details;
	} 
	/*initWareHouse(false);	
	initUnitData(false);*/
	if(flag=='detail'&&billStat!='0'){
		var tips = 1;
		/*$('#materialList').datagrid();*/
	}
	initMaterialListTableNew(details,tips);
}


/*
 * 新模板详情页初始化货品表格
 */
function initMaterialListTableNew(details,tips){
	$('#materialList').jqGrid({
		datatype:"local",
		data:[],
		footerrow: true,
		autowidth:true,//自动填满宽度
		height:"100%",
		forceFit:true,//改变列宽度，总宽度不变
		onCellSelect:function(rowid,iCol,cellcontent,e){
			if(iCol != 0 && _flag != "detail" && $("#materialList #"+rowid).find(".editing-on").length <= 0){
				editNew2(rowid);
			}
		},
		//pagination:true,
		colModel:[
			{name:'action',label:'操作',align:"center",sortable:false,width:80,formatter:materialListActionNew},
	        {name:'goodsId',label:'货品ID',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},    
	        {name:'goodsSpecId',label:'货品属性ID',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},  
	        {name:'unitId',label:'货品单位ID',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'updateTime',label:'更新时间',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'lowestPrice',label:'最低销售价',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'goodsSpecGroupId',label:'属性组ID',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'unitGroupId',label:'单位组ID',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'inWareHouseId',label:'仓库ID',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'_inWareHouseName',label:'仓库名称(临时)',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'barCode',label:'条码',align:"center",sortable:false,width:100,editable:true,edittype:"text"},
	        {name:'_goodsCode',label:'编号(临时)',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'_goodsSpec',label:'规格(临时)',align:"center",sortable:false,width:60,hidden:true,editable:true,edittype:"text"},
	        {name:'_goodsName',label:'名称(临时)',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'_unitName',label:'单位名称(临时)',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'_goodsSpecName',label:'属性(临时)',align:"center",sortable:false,width:60,hidden:true,editable:true,edittype:"text"},
	        {name:'_isNew',label:'是否新增',width:100,align:"center",sortable:false,hidden:true,editable:true,edittype:"text"},
	        {name:'goodsCode',label:'编号',width:80,align:"center",sortable:false,editable:true,edittype:"text",formatter:function(value,options,row){
	        	return row._goodsCode?row._goodsCode:value?value:"";
	        }},
	        {name:'goodsName',label:'名称',align:"center",sortable:false,width:240,formatter:function(value,options,row){
	        	return row._goodsName?row._goodsName:value?value:"";
	        }},
	        {name:'inWareHouseName',label:'仓库',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text",formatter:wareHouseAction},
	        {name:'goodsSpec',label:'规格',align:"center",sortable:false,width:60,formatter:function(value,options,row){
	        	return row._goodsSpec?row._goodsSpec:value?value:"";
	        }},
	        {name:'goodsSpecName',label:'属性',align:"center",sortable:false,width:60,editable:true,edittype:"text",formatter:function(value,options,row){
	        	return row._goodsSpecName?row._goodsSpecName:value?value:"";
	        }},
	        {name:'unitName',label:'单位',align:"center",sortable:false,width:100,editable:true,edittype:"text"/*,formatter:unitActionNew2*/},
	        {name:'scale',label:'换算关系',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'quentity',label:'数量',align:"center",sortable:false,width:60,editable:true,edittype:"text"/*editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}}*/},
	        {name:'unitPrice',label:'单价',align:"center",sortable:false,width:80,editable:true,edittype:"text",formatter:priceActionNew},
	        {name:'costPrice',label:'成本单价',align:"center",sortable:false,width:80,hidden:true,editable:true,edittype:"text",formatter:priceActionNew},
	        {name:'type',label:'金额',align:"center",sortable:false,width:80,formatter:typeActionNew},
	        {name:'describe',label:'备注',align:"center",sortable:false,width:80,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({validType:'maxLength[200]',width:'100%',height:'100%'});
	        }}},
	    ],
	    loadComplete:function(data){
	    	scrollDiy();
	    	$('#materialList').footerData('set',{action:'new',unitPrice:'new',costPrice:'new',type:'new'});
	    }
	});
	if(details!=undefined){
		$('#materialList')[0].addJSONData(details);
		/*if(tips != 1){
			$('#materialList').datagrid('insertRow',{
				index:details.length,
				row:{
					action:'new',
					unitPrice:'new',
					costPrice:'new',
					type:'new'
				}
			});
		}*/
		newIndex2 = $('#materialList').jqGrid('getRowData').length;
		if(tips == 1){
			$('#gbox_materialList .ui-jqgrid-sdiv').hide();
		}
	}/*else{
		$('#materialList').datagrid('insertRow',{
			index:0,
			row:{
				action:'new',
				unitPrice:'new',
				costPrice:'new',
				type:'new'
			}
		});
	}*/
	if(_flag == '' && localCache == "1"){
		var myCookies = localStorage[_billCode];
		if(myCookies!=null){
			var myobj = $.parseJSON(myCookies);
			var details2 = '';
			"undefined" != typeof myobj["details2"] && myobj["details2"]!='[]'?(details2 = $.parseJSON(myobj["details2"]),$('#materialList')[0].addJSONData(details2)):null;
			newIndex2 = $('#materialList').jqGrid('getRowData').length;
		}
	}
	
	//符合条件的单据界面，显示仓库列
	if(_billCode!='xsdd'&&_billCode!='cgxjd'&&_billCode!='cgdd'&&_billCode!='xsbjd'&&_billCode!='dcd'&&_billCode!='pdd'&&_billCode!='cgsqd'&&_billCode!='scjhd'){
		$('#materialList').jqGrid('showCol','inWareHouseName');
	}
	if(_billCode=='xsth'){
		$('#materialList').jqGrid('showCol','costPrice');
	}
	if(_billCode=='scjhd'){
		$('#materialList').jqGrid('hideCol',['type','unitPrice']);
	}
	$('#materialList').jqGrid("setGridWidth",$("#list1").width()); //让表格列变化后再一次自适应宽度
	if(_flag!='detail'){
		//keyHandler2();
	}
}

//详细页。货品列表删除按钮
function delNew2(index){
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r) {
			//var index_ = $(obj).fool('getRowIndex');
			$("#materialList").jqGrid('delRowData',index);
		}
	}});
}

function getTableText2(index,name){
	var td = $('#materialList').find("tr#"+index+" td[aria-describedby=materialList_"+name+"]");
	return td;
}
/*
 * 同行获取text编辑器
 * obj必须是action下的元素
 */
function getTextEditor2(obj,field){
	return $(obj).closest('td[field="action"]').siblings('td[field="'+field+'"]').find('.datagrid-editable-input');
}
//详细页。货品列表编辑按钮
function editNew2(ind){
	//var myEditnum=mynum2;
	var mygoodsCode='',mygoodsSpecName='';
	var unitName_ = getTableText2(ind,"unitName").text();
	var _unitName_ = getTableText2(ind,"_unitName").text();
	var goodsSpecName_ = getTableText2(ind,"goodsSpecName").text();
	var _goodsSpecName_ = getTableText2(ind,"_goodsSpecName").text();
	$("#materialList").jqGrid('editRow',ind);
	$('#materialList').jqGrid('setRowData', ind, {editing:true,action:null});//编辑状态转换，按钮变化
	mydetails2!='' && !getTableEditor2(ind,'_isNew').val()?(mygoodsCode = mydetails2[ind-1].goodsCode,mygoodsSpecName = mydetails2[ind-1].goodsSpecName):null;
	if(goodsSpecName_){
		getTableEditor2(ind,'_goodsSpecName').val(goodsSpecName_);
	}else{
		getTableEditor2(ind,'_goodsSpecName').val(_goodsSpecName_);
	}
	if(unitName_){
		getTableEditor2(ind,'_unitName').val(unitName_);
	}else{
		getTableEditor2(ind,'_unitName').val(_unitName_);
	}
	var barEdit = getTableEditor2(ind,'barCode');
	barEdit.textbox({
		width:"100%",
		height:"100%"
	});
	barEdit.textbox("textbox").bind("keydown",function(e){
		if(e.keyCode == 13){
			var barCode = $(this).val();
			$.post(getRootPath()+"/basedata/goodsBar/queryByBar",{barCode:barCode},function(data){
				 var myeditor$ = getTableEditor2(ind,'goodsCode');
		    	 var row = data;
		    	 barCodeKey2 = 1;//标识为通过barCode获取材料信息
		    	 myeditor$.next()[0].comboObj.setComboText(row.goodsCode);
		    	 getTableEditor2(ind,'goodsId').val(row.goodsId);
		    	 getTableEditor2(ind,'_goodsName').val(row.goodsName);
		    	 getTableEditor2(ind,'_goodsCode').val(row.goodsCode); 
		    	 getTableEditor2(ind,'_goodsSpec').val(row.goodsSpec);
		    	 getTableEditor2(ind,'quentity').numberbox('setValue',0);
		    	 getTableEditor2(ind,'describe').textbox('setValue','');
				 getTableText2(ind,'goodsSpec').text(row.goodsSpec);
				 getTableEditor2(ind,'goodsSpecGroupId').val(row.goodsSpecGroupId);
				 getTableEditor2(ind,'scale').val(row.unitScale);
				 getTableEditor2(ind,'updateTime').val(row.updateTime);
				 getTableText2(ind,'goodsName').text(row.goodsName);
		    	 var editor$_ = getTableEditor2(ind,'unitName');
		    	 if(row.unitGroupId){
		    		 getTableEditor2(ind,'unitGroupId').val(row.unitGroupId);
		    		 getTableEditor2(ind, '_unitName').val(row.unitName);
		    		 var _unitGroupId = row.unitGroupId;
		    		 var unitData = "";
		    		 $.ajax({
		    			 	url:getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,
		    				async:false,
		    		        data:{},
		    		        success:function(data){
		    		        	unitData=formatData(data,"fid");
		    		        }
		    			});
		    			editor$_.fool("dhxCombo",{
			    			 width:"100%",
			    			 height:"100%",
			    			 data:unitData,
			    			 required:true,
			    			 validType:['unitValidNew[\''+ind+'\']','nounitValidNew[\''+ind+'\']'],
			    			 setTemplate:{
			 					input:"#name#",
			 					option:'#name#'
			 				},
		    			    onLoadSuccess:function(combo){
		    			    	var _d = unitData;
		    			    	if(_d)unitData2_[ind] = _d;
		    			    	var _uid = row.unitId;
		    			    	combo.setComboValue(_uid);
		    			    	getTableEditor2(ind,'unitId').val(_uid);
		    			    	changeGoodPrice2(ind);
		    			    }
		    			});
		    			$(editor$_.next()[0].comboObj.getInput()).focus(function(){
		    				var $input = $(this);
		    				setTimeout(function(){$input.select();},100);
		    			});
		    	 }else{
		    		 editor$_.next()[0].comboObj.setComboValue('');
		    		 editor$_.next()[0].comboObj.setComboText('');
		    	 }
		    		var mygoodSpecId = row.goodsSpecGroupId;
		    		editor$spec = getTableEditor2(ind,'goodsSpecName');
		    		editor$spec.next()[0].comboObj.setComboText('');
		    		getTableEditor2(ind,'_goodsSpecName').val('');
		    		getTableEditor2(ind,'goodsSpecId').val('');
		    		if(mygoodSpecId!=undefined&&mygoodSpecId.trim().length!=0){
		    			//货品属性
		    			var myspecData = "";
		    			$.ajax({
		    			 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
		    				async:false,
		    		        data:{},
		    		        success:function(data){
		    		        	myspecData=formatData(data,"fid");
		    		        }
		    			});
		    			editor$spec.fool('dhxComboGrid',{
		    				width:"100%",
			    			height:"100%",
			    			hasDownArrow:false,
			    			validType:['specValidNew[\''+ind+'\']','nospecValidNew[\''+ind+'\']'],
			    			required:true,
		    				filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
		    				data:myspecData,
		    			});
		    			var specCombo = editor$spec.next()[0].comboObj;
		    			specCombo.setComboText("");
		    			"undefined"!=typeof $(specCombo.getInput()).attr('disabled')&&$(specCombo.getInput()).attr('disabled')=='disabled'?specCombo.enable():null;
		    		}else{
		    			$(editor$spec.next()[0].comboObj.getInput()).validatebox('disableValidation');
		    			editor$spec.next()[0].comboObj.disable();
		    			editor$spec.val("");
		    		}
			});
		}
	});
	
	//货品数量	
	var editor$ = getTableEditor2(ind,'quentity');
		editor$.numberbox({
			height:'100%',
			width:'100%',
			validType:['intOrFloat','numMaxLength[10]'],
			required:true,
			novalidate:true,
			precision:2
		});
	editor$.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	//货品单价	
	var editor$ = getTableEditor2(ind,'unitPrice');
	if(_billCode == 'xsbjd' || _billCode == 'xsdd' || _billCode == 'xsch'){
		editor$.numberbox({
			height:'100%',width:'100%',
			validType:['goodPrice[\''+ind+'\']','numMaxLength[10]'],required:true,precision:4,
			novalidate:true
		});
	}else{
		editor$.numberbox({
			height:'100%',width:'100%',
			required:true,precision:4,
			validType:'numMaxLength[10]',
			novalidate:true
		});
	}
	editor$.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	//单价快捷键操作
	/*editor$.numberbox('textbox').keydown(function(e){
		if(e.keyCode == 38){
			var obj = myobj2[myEditnum].find('a.btn-save');
			var index = obj.fool('getRowIndex') - 1;
			var v = mysave2(obj);
			if(v!=false && index != -1 && $('.2btn-index-save-'+index).length <= 0){
				var lastobj = $('#materialList').datagrid('getPanel').find('tr[datagrid-row-index="'+index+'"] td[field="action"] a.btn-del');
				editNew2(lastobj,index);
			}else if(v==false){
				return false;
			}else if(getTableEditor2(index,'goodsId').val() == ''){
				getTableEditor2(index,'goodsCode').combogrid('textbox').focus();
			}
		}else if(e.keyCode == 40){
			var obj = myobj2[myEditnum].find('a.btn-save');
	    	var index = obj.fool('getRowIndex') + 1;
			var max = $('#materialList').datagrid('getRows').length - 1;
			var v = mysave2(obj);
			if(v!=false && index < max && $('.2btn-index-save-'+index).length <= 0){
				var lastobj = $('#materialList').datagrid('getPanel').find('tr[datagrid-row-index="'+index+'"] td[field="action"] a.btn-del');
				editNew2(lastobj,index);
			}else if(index == max){
				$('#addrow').click();
				getTableEditor2(index,'goodsCode').combogrid('textbox').focus();
			}else if(v==false){
				return false;
			}else if(getTableEditor2(index,'goodsId').val() == ''){
				getTableEditor2(index,'goodsCode').combogrid('textbox').focus();
			}
		}else if(e.keyCode == 13){
			var obj = myobj2[myEditnum].find('a.btn-save');
			mysave2(obj);
		}
	});*/
	//备注快捷键操作
	/*var editor$ = getTextEditor2(obj,'describe');
	editor$.textbox('textbox').keydown(function(e){
		if(e.keyCode == 38){
			var obj = myobj2[myEditnum].find('a.btn-save');
			var index = obj.fool('getRowIndex') - 1;
			var v = mysave2(obj);
			if(v!=false && index != -1 && $('.2btn-index-save-'+index).length <= 0){
				var lastobj = $('#materialList').datagrid('getPanel').find('tr[datagrid-row-index="'+index+'"] td[field="action"] a.btn-del');
				editNew2('a.2btn-index-del-'+index,index);
			}else if(v==false){
				return false;
			}else if(getTableEditor2(index,'goodsId').val() == ''){
				getTableEditor2(index,'goodsCode').combogrid('textbox').focus();
			}
		}else if(e.keyCode == 40){
			var obj = myobj2[myEditnum].find('a.btn-save');
	    	var index = obj.fool('getRowIndex') + 1;
			var max = $('#materialList').datagrid('getRows').length - 1;
			var v = mysave2(obj);
			if(v!=false && index < max && $('.2btn-index-save-'+index).length <= 0){
				var lastobj = $('#materialList').datagrid('getPanel').find('tr[datagrid-row-index="'+index+'"] td[field="action"] a.btn-del');
				editNew2('a.2btn-index-del-'+index,index);
			}else if(index == max){
				$('#addrow2').click();
				getTableEditor2(index,'goodsCode').combogrid('textbox').focus();
			}else if(v==false){
				return false;
			}else if(getTableEditor2(index,'goodsId').val() == ''){
				getTableEditor2(index,'goodsCode').combogrid('textbox').focus();
			}
		}
	});*/
	
	//货品成本单价	
	var editor$ = getTableEditor2(ind,'costPrice');
	editor$.numberbox({
		height:'100%',width:'100%',
		required:true,precision:4,
		validType:'numMaxLength[10]',
		novalidate:true
	});
	editor$.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	
	//货品单位
	editor$ = getTableEditor2(ind,'unitGroupId');
		var _unitGroupId = editor$.val();
		var unitEditor$ = getTableEditor2(ind,'unitName');
		$.post(getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,{},function(data){
			var myunitData2 = formatData(data,"fid");
			unitEditor$.fool('dhxCombo',{  
				required:true,
				novalidate:true,
				validType:['unitValidNew[\''+ind+'\']','nounitValidNew[\''+ind+'\']'],
				data:myunitData2,
				setTemplate:{
					input:"#name#",
					option:'#name#'
				},
			    width:'100%',
			    height:"100%",
			    focusShow:true,
			    onLoadSuccess:function(combo){
			    	var _d = myunitData2;
			    	if(_d) unitData2_[ind] = _d;
			    	var _uid = getTableEditor2(ind,'unitId').val();
			    	combo.setComboValue(_uid);
			    },
			    onSelectionChange:function(){
			    	var myUnitName = getTableEditor2(ind,'unitName');
			    	//每次切换选项都选中
			    	var chol = $(myUnitName.next()[0].comboObj.getList()).find(".dhxcombo_option_selected");
			    	var mindex = chol.index();
			    	myUnitName.next()[0].comboObj.selectOption(mindex);
			    	myUnitName.next()[0].comboObj.openSelect();
			    },
			    onChange:function(value,text){
			    	var record = getTableEditor2(ind,'unitName').next()[0].comboObj.getOption(value).text;
			    	getTableEditor2(ind,'unitId').val(record.fid);
			    	getTableEditor2(ind, '_unitName').val(record.name); 
			    	changeGoodPrice2(ind);
			    	$(getTableEditor2(ind,'unitName').next()[0].comboObj.getInput()).validatebox("validate");
			    }
			});
			$(unitEditor$.next()[0].comboObj.getInput()).focus(function(){
				var $input = $(this);
				setTimeout(function(){$input.select();},100);
			});
			unitSearch(unitEditor$.next()[0].comboObj);
		});
		/*editor$.combobox('textbox').bind('blur',function(){
			var obj = myobj2[myEditnum].find('a.btn-save');
	    	var ind = obj.fool('getRowIndex');
			var _text = $(this).val();
			if(!_text)return;
			var _fid='' ,_name='';
			for(var i in unitData2_[ind]){
				if(_text==unitData2_[ind][i].name){
					_fid = unitData2_[ind][i].fid;
					_name = unitData2_[ind][i].name;
					break;
				}
			}
			getTableEditor2(ind,'unitId').val(_fid);
			getTableEditor2(ind,'unitName').val(_name);
			getTableEditor2(ind,'_unitName').val(_name);
			editor$.combobox('validate');
		});*/
	
	//货品属性
	editor$ = getTableEditor2(ind,'goodsSpecGroupId');//goodsSpecName
	var _goodSpecId = editor$.val();
	var specData = "";
	$.ajax({
	 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6',
		async:false,
        data:{},
        success:function(data){
        	specData=formatData(data,"fid");
        }
	});
	var editor$spec = getTableEditor2(ind,'goodsSpecName');
	editor$spec.fool('dhxComboGrid',{
		filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6',
		width:'100%',
		height:"100%",
		required:true,
		novalidate:true,
		hasDownArrow:false,
		focusShow:true,
		onlySelect:true,
		data:specData,
		validType:['specValidNew[\''+ind+'\']','nospecValidNew[\''+ind+'\']'],
		setTemplate:{
			input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
		},
	    onChange:function(value,text){
	    	if(value){
	    		var editor$1 = getTableEditor2(ind, 'goodsSpecId'); 
		    	var row = editor$spec.next()[0].comboObj.getOption(value).text;
		    	getTableEditor2(ind, '_goodsSpecName').val(row.name); 
		    	editor$1.val(row.fid);
		    	changeGoodPrice2(ind);
	    	}
	    }
	});
	mydetails2!='' && !getTableEditor2(ind,'_isNew').val()?editor$spec.next()[0].comboObj.setComboText(mygoodsSpecName):editor$spec.next()[0].comboObj.setComboText(getTableEditor2(ind,'_goodsSpecName').val());
	if(_goodSpecId!=undefined&&_goodSpecId.trim().length!=0){
		
	}else{
		editor$spec.next()[0].comboObj.setComboText("");
		editor$spec.next()[0].comboObj.disable();
	}
	
	//仓库
	var _inWareHouseId = getTableEditor2(ind,'inWareHouseId').val()?getTableEditor2(ind,'inWareHouseId').val():$("#inWareHouseId").val();
	editor$ = getTableEditor2(ind,'inWareHouseName');
	
	editor$.fool('dhxCombo',{
		data:warehouse,
		setTemplate:{
			input:"#name#",
			option:'#name#'
		},
	    width:'100%',
	    height:"100%",
	    value:_inWareHouseId,
	    clearOpt:false,
	    required:true,
	    editable:false,
	    onChange:function(value,text){
	    	var node = getTableEditor2(ind,'inWareHouseName').next()[0].comboObj.getSelectedText();
				getTableEditor2(ind,'inWareHouseId').val(node.id);
				getTableEditor2(ind,'_inWareHouseName').val(node.text);
	    }
	  /* onLoadSuccess:function(node,data){
		   if(undefined!=_inWareHouseId&&_inWareHouseId.length!=0){
			   getTableEditor2(ind,'inWareHouseName').combotree('setValue',_inWareHouseId);
			   getTableEditor2(ind,'inWareHouseId').val(_inWareHouseId);
			   getTableEditor2(ind,'_inWareHouseName').val(getTableEditor2(ind,'inWareHouseName').combotree('getText'));
		   }
	    }*/
	});
	//编号搜索货品
	editor$ = getTableEditor2(ind,'goodsCode');
	editor$.fool('dhxComboGrid',{
		filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=6',
		data:goods,
		width:'100%',
		height:"100%",
		required:true,
		novalidate:true,
		hasDownArrow:false,
		validType:'codeValid[\''+ind+'\']',
		focusShow:true,
		onlySelect:true,
		setTemplate:{
			input:"#code#",
			columns:[
						{option:'#name#',header:'名称',width:200},
						{option:'#code#',header:'编号',width:70},
						{option:"#barCode#",header:"条码",width:70}
					],
		},
	     onChange:function(value,text){
	    	 var myeditor$ = getTableEditor2(ind,'goodsCode');
	    	 var row = myeditor$.next()[0].comboObj.getSelectedText();
	    	 if(!row){return;}
	    	 barCodeKey2 = 0;//标识为通过goodsCode获取材料信息
	    	 getTableEditor2(ind,'goodsId').val(row.fid);
	    	 getTableEditor2(ind,'_goodsName').val(row.name);
	    	 getTableEditor2(ind,'_goodsCode').val(row.code); 
	    	 getTableEditor2(ind,'_goodsSpec').val(row.spec);
	    	 getTableEditor2(ind,'quentity').numberbox('setValue',0);
	    	 getTableEditor2(ind,'describe').textbox('setValue','');
	    	 getTableEditor2(ind,'barCode').textbox('setValue','');
			 getTableText2(ind,'goodsSpec').text(row.spec);
			 getTableEditor2(ind,'goodsSpecGroupId').val(row.goodsSpecGroupId);
			 getTableEditor2(ind,'scale').val(row.unitScale);
			 getTableEditor2(ind,'updateTime').val(row.updateTime);
			 getTableText2(ind,'goodsName').text(row.name);
	    	 var editor$_ = getTableEditor2(ind,'unitName');
	    	 if(row.unitGroupId){
	    		 getTableEditor2(ind,'unitGroupId').val(row.unitGroupId);
	    		 getTableEditor2(ind, '_unitName').val(row.unitName);
	    		 var _unitGroupId = row.unitGroupId;
	    		 var unitData = "";
	    		 $.ajax({
	    			 	url:getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,
	    				async:false,
	    		        data:{},
	    		        success:function(data){
	    		        	unitData=formatData(data,"fid");
	    		        }
	    			});
	    			editor$_.fool("dhxCombo",{
		    			 width:"100%",
		    			 height:"100%",
		    			 data:unitData,
		    			 required:true,
		    			 validType:['unitValidNew[\''+ind+'\']','nounitValidNew[\''+ind+'\']'],
		    			 setTemplate:{
		 					input:"#name#",
		 					option:'#name#'
		 				},
	    			    onLoadSuccess:function(combo){
	    			    	var _d = unitData;
	    			    	if(_d)unitData2_[ind] = _d;
	    			    	var _uid = row.unitId;
	    			    	combo.setComboValue(_uid);
	    			    	getTableEditor2(ind,'unitId').val(_uid);
	    			    	changeGoodPrice2(ind);
	    			    }
	    			});
	    			$(editor$_.next()[0].comboObj.getInput()).focus(function(){
	    				var $input = $(this);
	    				setTimeout(function(){$input.select();},100);
	    			});
	    	 }else{
	    		 editor$_.next()[0].comboObj.setComboValue('');
	    		 editor$_.next()[0].comboObj.setComboText('');
	    	 }
	    		var mygoodSpecId = row.goodsSpecGroupId;
	    		editor$spec = getTableEditor2(ind,'goodsSpecName');
	    		editor$spec.next()[0].comboObj.setComboText('');
	    		getTableEditor2(ind,'_goodsSpecName').val('');
	    		getTableEditor2(ind,'goodsSpecId').val('');
	    		if(mygoodSpecId!=undefined&&mygoodSpecId.trim().length!=0){
	    			//货品属性
	    			var myspecData = "";
	    			$.ajax({
	    			 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
	    				async:false,
	    		        data:{},
	    		        success:function(data){
	    		        	myspecData=formatData(data,"fid");
	    		        }
	    			});
	    			editor$spec.fool('dhxComboGrid',{
	    				width:"100%",
		    			height:"100%",
		    			hasDownArrow:false,
		    			required:true,
		    			validType:['specValidNew[\''+ind+'\']','nospecValidNew[\''+ind+'\']'],
	    				filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
	    				data:myspecData,
	    			});
	    			var specCombo = editor$spec.next()[0].comboObj;
	    			specCombo.setComboText("");
	    			"undefined"!=typeof $(specCombo.getInput()).attr('disabled')&&$(specCombo.getInput()).attr('disabled')=='disabled'?specCombo.enable():null;
	    		}else{
	    			$(editor$spec.next()[0].comboObj.getInput()).validatebox('disableValidation');
	    			editor$spec.next()[0].comboObj.disable();
	    			editor$spec.val("");
	    		}
	     },
	     onOpen:function(){
	    	 $(getTableEditor2(ind,'goodsCode').next()[0].comboObj.getList()).prev().find(".myAdd").css("margin-top",$(getTableEditor2(ind,'goodsCode').next()[0].comboObj.getList()).height()+37+'px');
	     }
	});
	/*editor$.combogrid('textbox').keydown(function(e){
		var selected = $(this); 
		if(e.keyCode == 9){
			var v = selected.closest('td[field=goodsCode]').siblings('td[field=inWareHouseName]').css('display');
			if(selected.parent().prev().combogrid('grid').datagrid('getRows').length==1 && v=="none"){//解决编码搜索剩下一个选项时tab切换到单位选择框时失效的问题
				e.preventDefault();
				selected.blur();
    			var mydata = selected.parent().prev().combogrid('grid').datagrid('getRows');
        		if("undefined" == typeof mydata[0].goodsSpecGroupId){
        			setTimeout(function(){
        				ed = selected.closest('td[field=goodsCode]').siblings('td[field=unitName]').find('.textbox-text');
        				ed.focus();
	            		ed.click();
        			},500);
        			return;
        		}else{
        			setTimeout(function(){
        				ed = selected.closest('td[field=goodsCode]').siblings('td[field=goodsSpecName]').find('.textbox-text');
        				ed.focus();
	            		ed.click();
        			},500);
        		}
    		}
		}
	});*/
	mydetails2!='' && !getTableEditor2(ind,'_isNew').val()?editor$.next()[0].comboObj.setComboText(mygoodsCode):editor$.next()[0].comboObj.setComboText(getTableEditor2(ind,'_goodsCode').val());
	$("#materialList #"+ind).find('input').bind('keydown',function (e){
		if(e.keyCode == 107){
			var $aa = $(this);
			$aa.blur();
			if($('#recordStatus').val() != 1 && $('#recordStatus').val() != 2){
				$('#addrow2').click();
			}
		}
	});
	
	//editor$.combogrid('panel').css('padding-bottom','50px');
	$(editor$.next()[0].comboObj.getList()).prev().append('<div class="myAdd" style="width:'+($(getTableEditor2(ind,'goodsCode').next()[0].comboObj.getList()).width()+17)+'px;margin-top:'+$(getTableEditor2(ind,'goodsCode').next()[0].comboObj.getList()).height()+37+'px;border:1px #ccc solid;margin-left:-1px; background:#fff;position:fixed; color:#646464; padding:5px 5px;">6条以后的商品请通过商品弹出框选择<br/><a href="javascript:;" onclick=addGoods2('+ind+'); class="btn-add" style="color:#646464;width:100px;padding-left: 20px;font-size: 14px;line-height: 20px;margin: 5px 0;">新增商品</a></div>');
	
	//替换按钮
	//$(obj).parent().html(mygetTBBtn2(true,ind));
	/*hideSaveAllLink2();*/
}
//详细页。添加货品按钮
function addGoods2(ind){
	getTableEditor2(ind,'goodsCode').next()[0].comboObj.closeAll();
	goodsIndex2 = ind;
	var boxWidth=1074;
	var boxHeight=500;
	var _billType=_billCode;
	var _customerId = $("#customerId").val();
	var _supplierId = $("#supplierId").val(); 
	win = $("#pop-win").fool('window',{top:$(window).scrollTop()+100,modal:true,'title':"选择货品",width:boxWidth,height:boxHeight,
		href:getRootPath()+'/goods/window?okCallBack=myselectGoods2&onDblClick=myselectGoods2&billType='+_billType+'&customerId='+_customerId+'&supplierId='+_supplierId});
}

//详细页。添加货品
function myselectGoods2(data){
	if(data==undefined)return;
	closeWin();
	$.messager.progress({
		text:'努力加载中，请稍后...'
	});
	setTimeout(function(){
		$(data).each(function(i,n){
			newIndex2++;
			var myind = goodsIndex2;
			if(i!=0){
				myind = newIndex2 - 1;
			}
			$("#materialList").jqGrid('addRowData',newIndex2,{
					goodsId: n.fid,
					barCode: n.barCode,
					goodsCode: n.code,
					goodsName: n.name,
					goodsSpec:n.spec,
					_goodsCode: n.code,
					_goodsName: n.name,
					_goodsSpec:n.spec,
					_unitName: n.unitName,
					goodsSpecGroupId:n.goodsSpecGroupId,
					unitName:n.unitName,
					quentity:1,
					scale:n.unitScale,
					unitId:n.unitId,
					type:0,
					unitPrice:n.referencePrice,
					costPrice:n.costPrice,
					lowestPrice:n.lowestPrice,
					unitGroupId:n.unitGroupId,
					_isNew:true
			},"after",myind);
			//var newobj = $('#materialList').datagrid('getPanel').find('tr[datagrid-row-index='+(goodsIndex+i)+'] td[field=action] a.btn-del');
			editNew2(newIndex2);
			changeGoodPrice2(newIndex2);
		});
		$("#materialList").jqGrid('delRowData',goodsIndex);
		$.messager.progress('close');
		hideSaveAllLink(false);
	},300);
}

//新增一行按钮
function addrow2(){
	var ids=$('#materialList').getDataIDs();
	var newIndex =0;
	if(ids.length>0){
		newIndex=ids[ids.length-1]+1;
	}
	$('#materialList').jqGrid('addRowData',newIndex,{_isNew:true},"last");
	editNew2(newIndex);
	setTimeout(function(){$('#materialList').jqGrid('setSelection',newIndex);$(getTableEditor2(newIndex,'goodsCode').next()[0].comboObj.getInput()).focus();},300);
	var pOffset = $('#materialList').find("tr#"+newIndex);
	pOffset.closest('.window-body').animate({scrollTop:pOffset.offset().top-$('#warehousebillForm').offset().top-76},500);
	/*newIndex2++;
	$('#materialList').jqGrid('addRowData',newIndex2,{
		_isNew:true
	});
	editNew2(newIndex2);*/
	/*setTimeout(function(){$('#materialList').jqGrid('setSelection',newIndex2);$(getTableEditor2(newIndex2,'goodsCode').next()[0].comboObj.getInput()).focus();},300);
	var pOffset = $('#materialList').find("tr#"+newIndex2);
	pOffset.closest('.window-body').animate({scrollTop:pOffset.offset().top-$('#warehousebillForm').offset().top-76},500);*/
}

//新模板详细页。材料列表操作按钮
function materialListActionNew(value,options,row){
	var index = options.rowId;
	if(value == 'new'){
		return "<a href='javascript:;' id='addrow2' class='btn-add' onclick='addrow2()' title='新增'></a>";
	}else{
		if(!row.editing){
			return mygetTBBtn2(false,index);
		}else{
			return mygetTBBtn2(true,index);
		}
	}
}
$.extend($.fn.validatebox.defaults.rules, {    
    codeValid: {    
        validator: function(value,param){
            return getTableEditor2(param[0],'goodsId').val()==''?false:true;    
        },    
        message: '货品没有选中，请重新选择'   
    },
    specValid: {    
        validator: function(value,param){
            return getTableEditor2(param[0],'goodsSpecId').val()==''?false:true;    
        },    
        message: '属性没有选中，请重新选择'   
    }
}); 
function mysave2(index){
	var ind = index;
	validDetailHelp2(ind);
	getTableEditor2(ind,'goodsId').val()==''?$(getTableEditor2(ind,'goodsCode').next()[0].comboObj.getInput()).validatebox('validate').select():null;
	getTableEditor2(ind,'goodsSpecId').val()==''?$(getTableEditor2(ind,'goodsSpecName').next()[0].comboObj.getInput()).validatebox('validate').select():null;
	//验证
	var v = $("#materialList").find('tr#'+ind).form('validate');
	if(v){
		//更新新建标识
		getTableEditor2(ind,'_isNew').val(false);
		var editor$ = getTableEditor2(ind,'goodsCode');
		getTableEditor2(ind,'_goodsSpec').val(getTableText2(ind,'goodsSpec').text());
   	 	getTableEditor2(ind,'_goodsCode').val(editor$.next()[0].comboObj.getComboText());
   	 	getTableEditor2(ind,'_goodsName').val(getTableText2(ind,'goodsName').text());
   	 	var editor$ = getTableEditor2(ind,'goodsSpecName');
   	    getTableEditor2(ind,'_goodsSpecName').val(editor$.next()[0].comboObj.getComboText());
   	    var unitName = getTableEditor2(ind,'unitName').next()[0].comboObj.getComboText();
   	    var inWareHouseName = getTableEditor2(ind,"inWareHouseName").next()[0].comboObj.getComboText();
   	 	barCodeKey2 == 0?getTableEditor2(ind,'barCode').val(""):null;
		$('#materialList').jqGrid('saveRow', ind);
		$('#materialList').jqGrid('setRowData',ind,{unitName:unitName,inWareHouseName:inWareHouseName,editing:false,action:null});
		//更新总金额
		getTotalNew2();
		
		/*if(getEditingOnSize2()<=0)*/
			/*hideSaveAllLink2();*/
	}else{
		return false;
	}
}

//计算货品列表总金额
function getTotalNew2(){
	var rows=$('#materialList').jqGrid('getRowData');
	var total=0;
	for(var i=0;i<rows.length-1;i++){
		if(rows[i].action.search(/editing-on/) != -1){
			continue;
		}
		rows[i].type=rows[i].quentity*rows[i].unitPrice;
		total+=rows[i].type;
	}
	$("#totalAmount").val(total.toFixed(2));
}

function mygetTBBtn2(f,index){
	if(_flag=='detail'&&_billStat!='0')return "";
	if(f){
		var s = "<a href='javascript:;' class='btn-save editing-on 2btn-index-save-"+index+"' onclick='mysave2(\""+index+"\")' title='确认'></a>";
		var c = "<a href='javascript:;' class='btn-back 2btn-index-cancel-"+index+"' onclick='cancel2(\""+index+"\")' title='撤销'></a>";
		return s+c;
	}else{
		var r = "<a href='javascript:;' class='btn-del 2btn-index-del-"+index+"' onclick='delNew2(\""+index+"\")' title='删除'></a>";
		//var e = "<a href='#' class='btn-edit btn-index-edit-"+index+"' onclick='editNew(this)' title='编辑'></a>";
		return r;
	}
}

/*//详细页。货品列表删除按钮
function delNew(obj){
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r) {
			var index_ = $(obj).fool('getRowIndex');
			$("#materialList").datagrid('deleteRow',index_);
		}
	}});
}*/

function cancel2(index){
	var ind = index;
	var _isNew = getTableEditor2(ind,'_isNew').val();
	if(_isNew=='true'||_isNew==true){
		$.fool.confirm({msg:'您确定要撤销该记录？',fn:function(r){
			if(r) {
				$("#materialList").jqGrid('delRowData',ind);
				/*hideSaveAllLink2();*/
			}
		}});
	}else{
		$('#materialList').jqGrid('restoreRow', ind);
		$('#materialList').jqGrid('setRowData', ind, {
			editing:false,
			action:null
		});
		/*hideSaveAllLink2();*/
	}
}

//键盘操作绑定事件
function keyHandler2(){
	var selected="";
	var row="";
	var index="";
	var field="";
	var panel=$("#materialList").datagrid('getPanel').panel('panel');
	panel.on("focus","input",function(e){
		selected=$(this);
		row=$(this).closest(".datagrid-row");
		index=row.attr("datagrid-row-index");
		field=$(this).closest(".datagrid-editable").parent().attr("field");
		$("#materialList").datagrid('selectRow',index);
		$(this).parent().prev().attr('class').search(/numberbox-f/)!=-1?setTimeout(function(){selected.select();},50):null;//数字input聚焦后选中
	});
	panel.bind('keydown', function (e) {
		switch (e.keyCode) {
		    case 37: // left
		            if(selected){
		            	var ed=getLastField2(row,field,index);
		            	if(selected.parent().attr("class").search(/combo/)!=-1){
				    		if(selected.parent().prev().combo("panel").parent().css("display")!="none"){
				    			selected.parent().prev().combo("hidePanel");
				    		};
				    	};
		            	if(ed){
		            		//tyc
		            		if(selected.attr("class")!="datagrid-editable-input"&&$(ed.target).attr("class")!="datagrid-editable-input validatebox-text"){
		            			selected.parent().prev().attr('class').search(/combogrid-f/)!=-1?selected.parent().prev().combogrid('hidePanel'):null;
		            		}
		            		//tyc
		            		if($(ed.target).attr("class")=="datagrid-editable-input"||$(ed.target).attr("class")=="datagrid-editable-input validatebox-text"){
			            		$(ed.target).focus();
			            		$(ed.target).click();
			            	}else{
			            		$(ed.target).next().children(".textbox-text").focus();
			            		$(ed.target).next().children(".textbox-text").click();
			            	};
		            	}
		            }
	                break;
	                
	        case 38: // up
	        	    if(selected){
	        	    	var ed=getUpField2(row,field,index);
	        	    	//tyc 明细列表的备注报错解决方法
	        	    	var v = -1;
	        	    	selected.parent().length>0?v = selected.parent().attr('class').search(/combo/):null;
	        	    	//tyc
	        	    	if(selected.attr('class').search(/textbox-text/)!=-1 && v!=-1 && selected.parent().prev().combo('options').hasDownArrow==true){
	        	    		if(selected.parent().prev().attr('class').search(/combotree-f/)!=-1){
	        	    			var options=selected.parent().prev().combo("options");
				    			options.keyHandler.enter=function(){
				    				var selectedNode=selected.parent().prev().combotree("tree").children().children("ul").find('.tree-node-hover');
				    				if(selectedNode){
				    					selectedNode[0].click();
				    				}
				    			};
        	    				selected.parent().prev().combo("showPanel");
        	    				/*var trees=selected.parent().prev().combotree("tree").children().children("ul");
        	    				var hover=trees.find('.tree-node-hover');
        	    				if(hover[0]&&hover[0]!='undefined'){
        	    					$(hover[0]).attr("class","tree-node");
        	    					$(hover[0]).parent().prev().children().attr("class","tree-node tree-node-hover");
        	    				}else{
        	    					trees.children('li:last').children().attr("class","tree-node tree-node-hover");
        	    				}
        	    				return;*/
        	    			}
	        	    		selected.parent().prev().combo("showPanel");	
        	    			return;
        	    		}
	        	    	if(ed){
	        	    		if($(ed.target).attr("class")=="datagrid-editable-input"||$(ed.target).attr("class")=="datagrid-editable-input validatebox-text"){
	        	    			$(ed.target).focus();
	        	    			$(ed.target).click();
	        	    		}else{
	        	    			$(ed.target).next().children(".textbox-text").focus();
	        	    			$(ed.target).next().children(".textbox-text").click();
	        	    		};
		            	}
	        	    }
                    break; 
		
			case 39: // right
				    if(selected){
				    	var ed=getNextField2(row,field,index);
				    	if(selected.parent().attr("class").search(/combo/)!=-1){
				    		if(selected.parent().prev().combo("panel").parent().css("display")!="none"){
				    			selected.parent().prev().combo("hidePanel");
				    		};
				    	};
				    	if(ed){
				    		if($(ed.target).attr("class")=="datagrid-editable-input"||$(ed.target).attr("class")=="datagrid-editable-input validatebox-text"){
				    			$(ed.target).focus();
			            		$(ed.target).click();
			            	}else{
			            		if(selected.closest('td[field]').attr('field')=="goodsCode" && selected.parent().prev().combogrid('grid').datagrid('getRows').length==1){//解决编码搜索剩下一个选项时右键切换到单位选择框时失效的问题
			            			selected.blur();
			            			var mydata = selected.parent().prev().combogrid('grid').datagrid('getRows');
				            		if(($(ed.target).closest('td[field]').attr('field')=="goodsSpecName" || $(ed.target).closest('td[field]').attr('field')=="unitName") && "undefined" == typeof mydata[0].goodsSpecGroupId){
				            			setTimeout(function(){
				            				ed = selected.closest('[field]').siblings('[field=unitName]').find('.datagrid-editable-input');
				            				ed.next().children(".textbox-text").focus();
						            		ed.next().children(".textbox-text").click();
				            			},500);
				            			return;
				            		}else{
				            			setTimeout(function(){
				            				ed=getNextField2(row,field,index);
				            				$(ed.target).next().children(".textbox-text").focus();
						            		$(ed.target).next().children(".textbox-text").click();
				            			},500);
				            			return;
				            		}
			            		}
			            		$(ed.target).next().children(".textbox-text").focus();
			            		$(ed.target).next().children(".textbox-text").click();
			            	};
				    	}
	                }
			        break;
			case 40: // down
				    if(selected){
				    	var ed=getDownField2(row,field,index);
				    	//tyc 明细列表的备注报错解决方法
				    	var v = -1;
	        	    	selected.parent().length>0?v = selected.parent().attr('class').search(/combo/):null;
				    	//tyc
	        	    	if(selected.attr('class').search(/textbox-text/)!=-1 && v!=-1 && selected.parent().prev().combo('options').hasDownArrow==true){
				    		if(selected.parent().prev().attr('class').search(/combotree-f/)!=-1){
				    			var options=selected.parent().prev().combo("options");
				    			options.keyHandler.enter=function(){
				    				var selectedNode=selected.parent().prev().combotree("tree").children().children("ul").find('.tree-node-hover');
				    				if(selectedNode){
				    					selectedNode[0].click();
				    				}
				    			};
				    			selected.parent().prev().combo("showPanel");
        	    				/*var trees=selected.parent().prev().combotree("tree").children().children("ul");
        	    				var hover=trees.find('.tree-node-hover');
        	    				if(hover[0]&&hover[0]!='undefined'){
        	    					$(hover[0]).attr("class","tree-node");
        	    					$(hover[0]).parent().next().children().attr("class","tree-node tree-node-hover");
        	    				}else{
        	    					trees.children('li:first').children().attr("class","tree-node tree-node-hover");
        	    				}
        	    				return;*/
        	    			}
				    		selected.parent().prev().combo("showPanel");
        	    			return;
        	    		}
	        	    	if(ed){
	        	    		if($(ed.target).attr("class")=="datagrid-editable-input"||$(ed.target).attr("class")=="datagrid-editable-input validatebox-text"){
	        	    			$(ed.target).focus();
	        	    			$(ed.target).click();
	        	    		}else{
	        	    			$(ed.target).next().children(".textbox-text").focus();
	        	    			$(ed.target).next().children(".textbox-text").click();
	        	    		};
	        	    	}
				    }
		            break;
			case 13: // enter 需求变更
			    /*if(selected){
			    	var ed=nextOrSave2(row,field,index);
        	    	if(!ed){
        	    		nextRow=getNextField2(row,field,index);
        	    		$(row).children("[field=action]").find(".btn-save").click();
        	    		if(nextRow){
	        	    		if($(nextRow.target).attr("class")=="datagrid-editable-input"||$(nextRow.target).attr("class")=="datagrid-editable-input validatebox-text"){
	        	    			$(nextRow.target).focus();
	        	    			$(nextRow.target).click();
	        	    		}else{
	        	    			$(nextRow.target).next().children(".textbox-text").focus();
	        	    			$(nextRow.target).next().children(".textbox-text").click();
	        	    		};
	        	    	}
	            	}
			    }*/
	            break;
		}
	});
}

//获取指定行的编辑列
function getEdFields(row){
	if(row){
		var tds=row.children();
		var edFields=[];
		for(var i=0;i<tds.length;i++){
			if ($(tds[i]).children().attr('class').search(/datagrid-editable/)!=-1&&$(tds[i]).css("display")!="none"){
				if(!row.children("[field='goodsSpecGroupId']").children().text()&&!row.children("[field='goodsSpecGroupId']").find(".datagrid-editable-input").val()&&$(tds[i]).attr("field")=="goodsSpecName"){
					continue;
				}
				edFields.push($(tds[i]));
			}
		}
		return edFields;
	}
}

//获取同行下一个编辑框，没有就保存
function nextOrSave2(row,field,index){
	var edFields=getEdFields(row);
	for(var i=0;i<=edFields.length;i++){
		if($(edFields[i]).attr("field")==field&&parseInt(i)+1<edFields.length){
			return $("#materialList").datagrid('getEditor',{index:index,field:$(edFields[i+1]).attr("field")});
		}
	}
	return false;
}
//获取下一个编辑框
function getNextField2(row,field,index){
	var edFields=getEdFields(row);
	for(var i=0;i<=edFields.length;i++){
		if($(edFields[i]).attr("field")==field&&parseInt(i)+1<edFields.length){
			return $("#materialList").datagrid('getEditor',{index:index,field:$(edFields[i+1]).attr("field")});
		}else if($(edFields[i]).attr("field")==field&&i+1>=edFields.length){
			var rows=$("#materialList").datagrid('getRows');
			var num=rows.length;
			var nextRow="";
			var fields="";
			for (var j=parseInt(index)+1;j<num;j++){
				nextRow=row.siblings("[datagrid-row-index="+j+"]");
				fields=getEdFields($(nextRow[0]));
				if(fields.length>0){
					return $("#materialList").datagrid('getEditor',{index:j,field:$(fields[0]).attr('field')});
				}
			}
		};
	}
}
//获取上一个编辑框
function getLastField2(row,field,index){
	var edFields=getEdFields(row);
	for(var i=0;i<edFields.length;i++){
		if($(edFields[i]).attr("field")==field&&i-1>=0){
			return $("#materialList").datagrid('getEditor',{index:index,field:$(edFields[i-1]).attr("field")});
		}else if($(edFields[i]).attr("field")==field&&i-1<0){
			var lastRow="";
			var fields="";
			for (var j=parseInt(index)-1;j>=0;j--){
				lastRow=row.siblings("[datagrid-row-index="+j+"]");
				fields=getEdFields($(lastRow[0]));
				if(fields.length>0){
					return $("#materialList").datagrid('getEditor',{index:j,field:$(fields[fields.length-1]).attr('field')});
				}
			}
		};
	}
}
//获取上一行编辑框
function getUpField2(row,field,index){
	var lastRow="";
	var fields="";
	for (var j=parseInt(index)-1;j>=0;j--){
		lastRow=row.siblings("[datagrid-row-index="+j+"]");
		fields=getEdFields($(lastRow[0]));
		if(fields){
			return $("#materialList").datagrid('getEditor',{index:j,field:field});
		}
	}
}
//获取下一行编辑框
function getDownField2(row,field,index){
	var rows=$("#materialList").datagrid('getRows');
	var num=rows.length;
	var nextRow="";
	var fields="";
	for (var j=parseInt(index)+1;j<num;j++){
		nextRow=row.siblings("[datagrid-row-index="+j+"]");
		fields=getEdFields($(nextRow[0]));
		if(fields){
			return $("#materialList").datagrid('getEditor',{index:j,field:field});
		}
	}
}

//详细页。货品列表单位下拉
function unitActionNew2(value,options,row){
	var index = options.rowId;
	if(unitData2_[index] != null){
		for(var i in unitData2_[index]){
			if(row.unitId==unitData2_[index][i].text.fid){
				return unitData2_[index][i].text.name;
			}
		}
	}else{
		return value;
	}
}

$.extend($.fn.validatebox.defaults.rules, {    
    unitValidNew: {    
        validator: function(value,param){
            return getTableEditor2(param[0],'unitId').val()==''?false:true;    
        },    
        message: '单位没有选中，请重新选择'   
    },
    nounitValidNew: {    
        validator: function(value,param){
            return getTableEditor2(param[0],'_unitName').val()!=getTableEditor2(param[0],'unitName').next()[0].comboObj.getComboText()?false:true;    
        },    
        message: '单位名称不符，请重新选择'   
    },
    specValidNew: {    
        validator: function(value,param){
            return getTableEditor2(param[0],'goodsSpecId').val()==''?false:true;    
        },    
        message: '属性没有选中，请重新选择'   
    },
    nospecValidNew: {    
        validator: function(value,param){
            return getTableEditor2(param[0],'_goodsSpecName').val()!=getTableEditor2(param[0],'goodsSpecName').next()[0].comboObj.getComboText()?false:true;    
        },    
        message: '属性名称不符，请重新选择'   
    },
}); 