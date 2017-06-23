var win,win2,_editor;

var sdeptData="";
var smemData="";
var ssupData="";
var scusData="";
var swarehouse="";
var sgoodsData = ""
//加载控件数据
$.ajax({
	url:getRootPath()+"/basedata/query?num="+Math.random(),
	async:false,
    data:{param:"Organization,Member,Supplier,Customer,Goods,AuxiliaryAttr_Warehouse"},
    success:function(data){
    	sdeptData=formatTree(data.Organization[0].children,"text","id");
    	smemData=formatData(data.Member,"fid");
    	ssupData=formatData(data.Supplier,"fid");
    	scusData=formatData(data.Customer,"fid");
    	swarehouse=formatTree(data.AuxiliaryAttr_Warehouse[0].children,"text","id");
    	sgoodsData = formatData(data.Goods,"fid");
    }
});
jQuery(function($){
	//列表。添加按钮
	$("#add").click(function(){
		/*$("#my-window").fool('window',{top:10,left:0,modal:true,title:$(this).attr('_title'),href:$(this).attr('href'),onClose:function(){
			$("#my-window").window('clear');
		},onBeforeClose:function(){
			closeWin();
		}});*/
		openWin($(this).attr('_title'),$(this).attr('href'));
		return false;
	});
	//新模板列表。添加按钮
	if(_billCode == 'xsfld'){
		$("#addNew").click(function(){
			var url=getRootPath()+'/salesRebateRebBill/add?billCode='+_billCode;
			/*$('#addBox').window("open");
			$('#addBox').window("setTitle","新增销售返利单");
			$('#addBox').window("refresh",url);*/
			warehouseWin("新增销售返利单",url)
		});
	}else if(_billCode == "bom"){
		$("#addNew").click(function(){
			var url=getRootPath()+"/bom/edit?billCode=bom";
			warehouseWin("新增"+_billCodeName,url)
		});
	}else if(_billCode == "zjjh"){
		$("#addNew").click(function(){
			var url=getRootPath()+"/capitalPlan/edit?billCode=zjjh";
			warehouseWin("新增"+_billCodeName,url)
		});
	}else{
		$("#addNew").click(function(){
			var url= getRootPath()+'/warehouse/'+_billCode+'/edit?billCode='+_billCode;
			warehouseWin("新增"+_billCodeName,url)
		});
	}
});

var _billCode,_billCodeName,_unitData=null,_flag,_wareHouse=null,_billStat;
var recordStatusOptions = [{id:'0',name:'未审核'},{id:'1',name:'已审核'},{id:'2',name:'已作废'}];

function initManage(billCode,billCodeName){
	_billCode = billCode;
	_billCodeName = billCodeName;
	//$('#dataTable').datagrid();
	//setPager($('#dataTable'));
	
	$("#search-form").find("input[_class]").each(function(i,n){inputInit($(this));});
	
	/*$('#addBox').window({
		top:10+$(window).scrollTop(),
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
	});*/
	
	//enterSearch('search-form-btn');
}

/*function init(flag,billStat,details){
	_flag = flag;
	_billStat = billStat;
	if(flag=='detail'&&billStat!='0'){
		$(".toolbar").hide();
		
		$('#goodsList').datagrid();
		
		$("#warehousebillForm").fool('fromEnable',{'enable':false});
		
		//$(".btn-footer").hide();
		
		$("input[_class^=datebox]").each(function(i,n){
			$(n).val(getDateStr($(n).val()));			
		});
		$("input[name*=Time]").each(function(i,n){
			$(n).val(getDateStr($(n).val()));
		});
	}else{
		if(flag=='copy'){
			$("#fid").val('');
			//$("#code").val('');
			$("#voucherCode").val('');
			$("#freeAmount").val('');
		}
		
		//初始化文本框
		$("#warehousebillForm").find("input[_class]").each(function(i,n){inputInit($(this));});		
		
		//业务员/采购员
		$(".memberBox").click(function(){
			_editor = $(this);
			win = $("#pop-win").fool('window',{modal:true,'title':"选择",height:480,width:780,
				href:getRootPath()+'/member/window?okCallBack=selectMember&onDblClick=selectMember&singleSelect=true'});
		}).validatebox({required:true,novalidate:true});
		//销售商
		$(".customerBox").click(function(){
			_editor = $(this);
			win = $("#pop-win").fool('window',{modal:true,'title':"选择",height:480,width:780,
				href:getRootPath()+'/customer/window?okCallBack=selectCus&onDblClick=selectCus&singleSelect=true'});
		}).validatebox({required:true,novalidate:true});
		//各种单
		$(".relationBox").click(function(){
			_editor = $(this);
			var _relationCode = $(this).attr('_billCode');
			win = $("#pop-win").fool('window',{modal:true,onClose:function(){
				if($('#salewin').html() != ''){
					$('#salewin').window('destroy');
				}
			},'title':"选择",height:480,width:780,
				href:getRootPath()+'/salebill/xsdd/window?okCallBack=selectRelation&onDblClick=selectRelation&singleSelect=true&billCode='+_relationCode});
		});//.validatebox({required:true,novalidate:true});
		
		$(".supplierBox").click(function(){
			_editor = $(this);
			win = $("#pop-win").fool('window',{modal:true,'title':"选择",height:480,width:780,
				href:getRootPath()+'/supplier/window?okCallBack=selectSupplier&onDblClick=selectSupplier&singleSelect=true'});
		}).validatebox({required:true,novalidate:true});
		
		$("#warehousebillForm").fool('fromBlurVali');
		
	}
	
	if(_unitData==null&&flag.length!=0){
		initUnitData(true,details);
	}else{
	}
	
	initGoodsListTable(details);
	
	btnFooterInit();
	
	hideSaveAllLink(true);
	
	if(flag!='detail')enterController("warehousebillForm");
}*/

/**
 * 预加载数据
 * Uninitialized 未初始化
 * Loading 载入
 * Loaded 载入完成
 * Interactive 交互
 * complete 完成
 *//*
document.onreadystatechange = preLoading; 
function preLoading(){
	if(document.readyState == "complete"){ //当页面加载状态
		if(_unitData==null){
					
		}
		initWareHouse(false);	
		initUnitData(false);
	}
}*/

/*//初始化单位数据
function initUnitData(b,d){
	$.getJSON(getRootPath()+'/unitController/getLeafUnit',function(data){
		_unitData = data;
		//if(b)initGoodsListTable(d);
	});
}

//初始化仓库数据
function initWareHouse(b){
	$.getJSON(getRootPath()+'/basedata/warehourseList',function(data){
		_wareHouse = data;
		//initUnitData(b);
		//if(b)initGoodsListTable(d);
	});
}*/

/**
 * 详情页 初始化货品表格
 */
/*function initGoodsListTable(details){
	$('#goodsList').datagrid({
		idField:'goodsId',
		fitColumns:false,
		//pagination:true,
		columns:[[    
	        {field:'goodsId',title:'货品ID',width:100,hidden:true,editor:{type:'text'}},    
	        {field:'goodsSpecId',title:'货品属性ID',width:100,hidden:true,editor:{type:'text'}},  
	        {field:'unitId',title:'货品单位ID',width:100,hidden:true,editor:{type:'text'}},
	        {field:'scale',title:'换算关系',width:100,hidden:true,editor:{type:'text'}},
	        {field:'updateTime',title:'更新时间',width:100,hidden:true,editor:{type:'text'}},
	        {field:'lowestPrice',title:'最低销售价',width:100,hidden:true,editor:{type:'text'}},
	        {field:'goodsSpecGroupId',title:'属性组ID',width:100,hidden:true,editor:{type:'text'}},
	        {field:'unitGroupId',title:'单位组ID',width:100,hidden:true,editor:{type:'text'}},
	        {field:'inWareHouseId',title:'仓库ID',width:100,hidden:true,editor:{type:'text'}},
	        {field:'_inWareHouseName',title:'仓库名称(临时)',width:100,hidden:true,editor:{type:'text'}},
	        {field:'_isNew',title:'是否新增',width:100,hidden:true,editor:{type:'text'}},
	        
	        {field:'barCode',title:'条码',width:100,sortable:false},
	        {field:'goodsCode',title:'编号',width:100,sortable:false},
	        {field:'goodsName',title:'名称',width:100,sortable:false},
	        {field:'inWareHouseName',title:'仓库',width:100,sortable:false,hidden:true,editor:{type:'text',required:true},formatter:wareHouseAction},
	        {field:'goodsSpec',title:'规格',width:100,sortable:false},
	        {field:'goodsSpecName',title:'属性',width:100,sortable:false,editor:{type:'text'}},
	        {field:'unitName',title:'单位',width:100,sortable:false,editor:{type:'text'},formatter:unitAction},
	        {field:'quentity',title:'数量',width:100,sortable:false,editor:{type:'numberbox',options:{validType:'intOrFloat',required:true,precision:2}}},
	        {field:'unitPrice',title:'单价',width:100,sortable:false,editor:{type:'text'},formatter:priceAction},
	        {field:'type',title:'金额',width:100,hidden:true,sortable:false,formatter:typeAction},
	        {field:'describe',title:'备注',width:100,editor:{type:'textbox',options:{validType:'maxLength[200]'}}},
	        {field:'action',title:'操作',width:100,formatter:goodsListAction}
	    ]],
	    frozenColumns:[[
	        
	    ]]
	});
	
	if(_flag!='detail')$('#goodsList').datagrid({toolbar:'.toolbar'});
	
	//初始化货品表格
	//setPager($('#goodsList'));
	
	if(details!=undefined) $('#goodsList').datagrid('loadData',details);
	//符合条件的单据界面，显示仓库列
	if(_billCode=='cgrk'||_billCode=='cprk'||_billCode=='cptk')
		$('#goodsList').datagrid('showColumn','inWareHouseName');
	
	if(_flag!='detail')keyHandler();	
}*/

//详细页 货品列表。单价验证
$.extend($.fn.validatebox.defaults.rules, {
	goodPrice:{//验证货品单价
        validator: function (value, param) {
        	if(_billCode=='cgdd'||_billCode=='cgrk')return true;
        	if(value.length==0)return false;
        	
        	var editor$ = getTableEditor(param[0],'lowestPrice');
        	var _lowestPrice = editor$.val();
        	
        	param[1]=_lowestPrice;
        	
			if(parseFloat(value)<parseFloat(_lowestPrice)){
				return false;
			}else{
				return true;
			}
			return false;
         },
         message:'单价金额不能低于最低销售价{1}元'
     },
     positive:{//判断是否大于0
    	 validator:function(value,param){
    		 if(_billCode=='cprk')return true;
    		 //var reg = /^[1-9]\d*$/;
    		 return parseInt(value)>0;//reg.test(value);
    	 },
    	 message:'输入必须大于0'
     }
});

//状态翻译
function recordStatusAction(value){
	for(var i=0; i<recordStatusOptions.length; i++){
		if (recordStatusOptions[i].id == value) return recordStatusOptions[i].name;
	}
	return value;
}

/*//详细页。保存按钮
function saveData(){
	if(!$("#warehousebillForm").fool('fromVali'))
		return false;
	var details = $("#goodsList").datagrid('getRows');
	if(details.length<=0){
		$.fool.alert({msg:'你还没有添加任何货品'});
		return false;
	}
	var _dataPanel = $('#goodsList').datagrid('getPanel');
	var _editing = _dataPanel.find(".editing-on");
	if(_editing.length>0){
		$.fool.alert({msg:'你还有没编辑完成的货品,请先确认！'});
		return false;
	}
	
	var fdata = $("#warehousebillForm").serializeJson();
	fdata = $.extend(fdata,{'details':JSON.stringify(details)});
	$.post(getRootPath()+'/initialstock/'+_billCode+'/save',fdata,function(data){
		if(data.returnCode =='0'){
    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
    			refreshData();
    			$("#my-window").window('close');
    			return true;
    		}});
    	}else{
    		$.fool.alert({msg:data.message});
    		return false;
		}
    });
}*/

//详细页。作废按钮
function cancelVoid(value){
	var myurl = getRootPath()+'/warehouse/'+_billCode+'/cancel';
	if(_billCode == 'xsfld'){
		myurl = getRootPath()+'/salesRebateRebBill/cancel';
	}else if(_billCode == 'cgfld'){
		myurl = getRootPath()+'/purchaseRebBill/cancel';
	}
	$.fool.confirm({msg:'确定要作废该记录吗？',fn:function(r){
		if(r){
			 $.ajax({
					type : 'post',
					url : myurl,
					data : {id :value},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						var myresult = data.returnCode;
						var mymsg = data.message;
						if(_billCode == 'xsfld' || _billCode == 'cgfld'){
							myresult = data.returnCode;
							mymsg = data.message;
						}
						if(myresult == 0){
							$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
								refreshData();
								$("#void").hide();
							}});
						}else if(myresult == 1){
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:mymsg,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:'操作失败！'+mymsg,fn:function(){
					    		}});
				    		}
						}else{
							$.fool.alert({msg:'服务器繁忙，请稍后再试'});
						}
		    		}
				});
		}
	},title:'确认'});
}

//详细页。审核按钮
function passAudit(value){
	if(_billCode == 'cgrk'||_billCode == 'cgth'||_billCode == 'cgfp'||_billCode == 'cgfld'||_billCode == 'xsfp'||_billCode == 'xsfld'||_billCode == 'shd'||_billCode == 'fyd'||_billCode == 'xsch'||_billCode == 'xsth'||_billCode == 'qcys'||_billCode == 'qcyf'){
		if(queryByWarehouse(value)){
			var amount="";
			if(_billCode == 'shd'){
				amount=$("td[title='"+value+"']").siblings("[aria-describedby='dataTable_totalAmount']").text()-$("td[title='"+value+"']").siblings("[aria-describedby='dataTable_deductionAmount']").text();
			}else{
				if($("td[title='"+value+"']").siblings("[aria-describedby='dataTable_totalAmount']").text()){
					amount=$("td[title='"+value+"']").siblings("[aria-describedby='dataTable_totalAmount']").text();
				}else if($("td[title='"+value+"']").siblings("[aria-describedby='dataTable_amount']").text()){
					amount=$("td[title='"+value+"']").siblings("[aria-describedby='dataTable_amount']").text();
				}else if($("td[title='"+value+"']").siblings("[aria-describedby='billList_totalAmount']").text()){
					amount=$("td[title='"+value+"']").siblings("[aria-describedby='billList_totalAmount']").text();
				}else{
					amount=$("td[title='"+value+"']").siblings("[aria-describedby='billList_amount']").text();
				}
			}
			if(!checkPlanAmount(value,amount)){
				$.messager.defaults={ok:"编辑",cancel:"不编辑"};
				$.fool.confirm({msg:'资金计划的金额不等于单据金额，是否编辑资金计划？',fn:function(r){
					if(r){
						//todo
						viewRowNew(value,0,zjjh,[value]);
						/*zjjh(value);*/
					}
				}})
				$.messager.defaults={ok:"确定",cancel:"取消"};
				return false;
			}else{
				auditNew(value);
			}
		}else{
			$.messager.defaults={ok:"编辑",cancel:"不编辑"};
			$.fool.confirm({msg:'暂无资金计划记录，是否编辑资金计划？',fn:function(r){
				if(r) {
					viewRowNew(value,0,zjjh,[value]);
					/*zjjh(value);*/
				}else{
					capitalPassAudit(value);
				}
			}});
			$.messager.defaults={ok:"确定",cancel:"取消"};
		};
	}else{
		auditNew(value);
	}
}
function auditNew(value){
	var myurl = getRootPath()+'/warehouse/'+_billCode+'/passAudit';
	if(_billCode == 'xsfld'){
		myurl = getRootPath()+'/salesRebateRebBill/passAudit';
	}else if(_billCode == 'cgfld'){
		myurl = getRootPath()+'/purchaseRebBill/passAudit';
	}
	$.fool.confirm({msg:'确定要审核该记录吗？',fn:function(r){
		if(r){
			if(_billCode == 'shd'){
				$.ajax({
					url:getRootPath()+'/warehouse/'+_billCode+'/checkShd',
					async:false,
					data:{id:value},
					success:function(re){
						if(re.returnCode!=0){
							$.fool.confirm({msg:re.message,title:"是否继续审核?",fn:function(result){
								if(result){
									$.ajax({
										type : 'post',
										url : myurl,
										data : {id :value},
										dataType : 'json',
										success : function(data) {
											dataDispose(data);
											var myresult = data.returnCode;
											var mymsg = data.message;
											if(_billCode == 'xsfld' || _billCode == 'cgfld'){
												myresult = data.returnCode;
												mymsg = data.message;
											}
											if(myresult == 0){
												$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
													refreshData();
													$("#verify").hide();
												}});
											}else if(myresult == 1){
												if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
									    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:mymsg,fn:function(){
									    			}});
									    		}else{
										    		$.fool.alert({msg:'操作失败！'+mymsg,fn:function(){
										    		}});
									    		}
											}else{
												$.fool.alert({msg:'服务器繁忙，请稍后再试'});
											}
										}
									});
								}
							}});
						}else{
							$.ajax({
								type : 'post',
								url : myurl,
								data : {id :value},
								dataType : 'json',
								success : function(data) {
									dataDispose(data);
									var myresult = data.returnCode;
									var mymsg = data.message;
									if(_billCode == 'xsfld' || _billCode == 'cgfld'){
										myresult = data.returnCode;
										mymsg = data.message;
									}
									if(myresult == 0){
										$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
											refreshData();
											$("#verify").hide();
										}});
									}else if(myresult == 1){
										if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
							    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:mymsg,fn:function(){
							    			}});
							    		}else{
								    		$.fool.alert({msg:'操作失败！'+mymsg,fn:function(){
								    		}});
							    		}
									}else{
										$.fool.alert({msg:'服务器繁忙，请稍后再试'});
									}
					    		}
							});
						}
					}
				});
			}else{
				$.ajax({
					type : 'post',
					url : myurl,
					data : {id :value},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						var myresult = data.returnCode;
						var mymsg = data.message;
						if(_billCode == 'xsfld' || _billCode == 'cgfld'){
							myresult = data.returnCode;
							mymsg = data.message;
						}
						if(myresult == 0){
							$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
								refreshData();
								$("#verify").hide();
							}});
						}else if(myresult == 1){
							if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
				    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:mymsg,fn:function(){
				    			}});
				    		}else{
					    		$.fool.alert({msg:'操作失败！'+mymsg,fn:function(){
					    		}});
				    		}
						}else{
							$.fool.alert({msg:'服务器繁忙，请稍后再试'});
						}
		    		}
				});
			}
		}
	},title:'确认'});
}
/*//详细页。刷新按钮
function refreshForm(){
	$("#my-window").window('refresh');
}

function inputFocusHelp(){
	if(_editor)_editor.focus();
}*/

/*//详细页。各种单
function selectRelation(data){
	var _d = getData(data);
	$("#relationId").val(_d.fid);
	$("#relation").val(_d.code);
	
	if(_billCode!='cgdd'){
		$("#supplierId").val(_d.supplierId);
		$("#supplierName").val(_d.supplierName);
		$("#supplierCode").val(_d.supplierCode);
		$("#supplierPhone").val(_d.supplierPhone);
	}	
	if(_billCode=='cgrk'||_billCode=='cgdd'){
		var details = eval(_d.details);
	    $("#goodsList").datagrid('loadData',details);
	    var rows=$("#goodsList").datagrid('getRows');
	    var editor="";
	    for(var i=0;i<rows.length;i++){
	    	$("#goodsList").datagrid('beginEdit',i);
		    editor=$("#goodsList").datagrid('getEditor',{index:i,field:'goodsSpecName'});
		    var obj=$(editor.target).closest('[field="goodsSpecName"]').siblings('[field="action"]').children();
		    edit(obj);
	    }
	}
	inputFocusHelp();
	//$("#relation").validatebox('validate');
	closeWin();
}*/

/*//详细页。业务员
function selectUser(data){
	var _d = getData(data);
	$("#userId").val(_d.fid);
	$("#userName").val(_d.username);
	$("#userName").validatebox('validate');
	closeWin();
}

//详细页。销售商文本框
function selectCus(data){
	var _d = getData(data);
	$("#customerId").val(_d.fid);
	$("#customerCode").val(_d.code);
	$("#customerName").val(_d.name);
	$("#customerPhone").val(_d.phone);
	
	$("#inMemberId").val(_d.memberId);
	$("#inMemberName").val(_d.memberName);
	$("#deptId").val(_d.deptId);
	$('#deptName').combotree('setValue', _d.deptId);
	
	$("#customerName").validatebox('validate');
	
	inputFocusHelp();
	closeWin();
}

//详细页。采购员文本框
function selectMember(data,other){
	var _d = getData(data);
	$("#inMemberId").val(_d.fid);
	$("#inMemberName").val(_d.username).focus();
	$("#inMemberName").validatebox('validate');
	inputFocusHelp();
	closeWin();
}

//详细页。货品列表 供应商文本框
function selectSupplier(data,other){
	var _d = getData(data);
	
	//清空单据号
	if(_d.fid!=$("#supplierId").val()&&_billCode!='cgdd'){
		$("#relation").val('');
		$("#relationId").val('');
	}
	
	$("#supplierId").val(_d.fid);
	$("#supplierCode").val(_d.code);
	$("#supplierName").val(_d.name);
	$("#supplierPhone").val(_d.phone);
	$("#inMemberId").val(_d.memberId);
	$("#inMemberName").val(_d.memberName);
	$("#deptId").val(_d.deptId);
	$('#deptName').combotree('setValue', _d.deptId);	
	$("#supplierName").validatebox('validate');
	inputFocusHelp();
	closeWin();
}

//详细页。货品列表 属性文本框
function selectGoodSpec(data,other){
	var _d = getData(data);
	if(!_d)return;
	var editor$ = getTableEditor(other, 'goodsSpecName'); 
	editor$.val(_d.name).validatebox('validate');
	
	editor$ = getTableEditor(other, 'goodsSpecId'); 
	editor$.val(_d.fid,other);
	
	changeGoodPrice(other);
	
	inputFocusHelp();
	
	closeWin();
}*/

/*//详细页 属性跟单位改变 修改 最低销售价、参考单价
function changeGoodPrice(index){
	var _billType=_billCode;
	var _customerId = $("#customerId").val();
	var _supplierId = $("#supplierId").val();
	
	var editor$ = getTableEditor(index, 'goodsId');
	var _goodsId = editor$.val();
	editor$ = getTableEditor(index, 'unitId');
	var _unitId = editor$.val();
	editor$ = getTableEditor(index,'goodsSpecId');
	var _goodsSpecId = editor$.val();
	
	$.post(getRootPath()+'/goods/getOtherPrice',{
		'unitId':_unitId,
		'fid':_goodsId,
		'goodsSpecId':_goodsSpecId,
		'billType':_billType,
		'customerId':_customerId,
		'supplierId':_supplierId
		},
		function(data){
			var $unitPrice = getTableEditor(index,'unitPrice');
			$unitPrice.numberbox('setValue',data.referencePrice).numberbox('validate');
			
			var $lowestPrice = getTableEditor(index,'lowestPrice');
			$lowestPrice.val(data.lowestPrice);
			
			var panel = $('#goodsList').datagrid('getPanel');//获取货品列表
			if(data.referencePrice='0E-8'||data.referencePrice==0||data.referencePrice=="0"){
			   var _unitPrice=0;//单价
			}		     	
			var _quentity=getTableEditor(index,'quentity').numberbox('getText');//数量
			var _type=parseFloat(_unitPrice)*parseFloat(_quentity).toFixed(2);//税前金额			
			 panel.find('tr[datagrid-row-index="'+index+'"]').children('[field="type"]').find('.numberbox-f').numberbox('setValue',_type);
			//判断是修改状态
			if(_billCode=="cgfp"||_billCode=="xsfp"){	
			 var taxAmount$=getTextEditor(index,'taxAmount');//税金编辑器
			 var totalAmount$=getTextEditor(index,'totalAmount');//税后金额编辑器
			 var _taxRate=getTableEditor(index,'taxRate').numberbox('getText');//税点
			 var _taxAmount=parseFloat(_type)*parseFloat(_taxRate)/100;//税金
			 var _totalAmount=parseFloat(_taxAmount)+parseFloat(_type);//税后金额
			  panel.find('tr[datagrid-row-index="'+index+'"]').children('[field="taxAmount"]').find('.numberbox-f').numberbox('setValue',_taxAmount);
			  panel.find('tr[datagrid-row-index="'+index+'"]').children('[field="totalAmount"]').find('.numberbox-f').numberbox('setValue',_totalAmount);
				}
		});
	
	$.post(getRootPath()+'/unitController/get',{'id':_unitId},function(data){
		var $scale =  getTableEditor(index,'scale');
		$scale.val(data.scale);
	});
}

function changeGoodPrice2(index){
	var _billType=_billCode;
	var _customerId = $("#customerId").val();
	var _supplierId = $("#supplierId").val();
	
	var editor$ = getTableEditor2(index, 'goodsId');
	var _goodsId = editor$.val();
	editor$ = getTableEditor2(index, 'unitId');
	var _unitId = editor$.val();
	editor$ = getTableEditor2(index,'goodsSpecId');
	var _goodsSpecId = editor$.val();
	
	$.post(getRootPath()+'/goods/getOtherPrice',{
		'unitId':_unitId,
		'fid':_goodsId,
		'goodsSpecId':_goodsSpecId,
		'billType':_billType,
		'customerId':_customerId,
		'supplierId':_supplierId
		},
		function(data){
			var $unitPrice = getTableEditor2(index,'unitPrice');
			$unitPrice.numberbox('setValue',data.referencePrice).numberbox('validate');
			
			var $lowestPrice = getTableEditor2(index,'lowestPrice');
			$lowestPrice.val(data.lowestPrice);
		});
	
	$.post(getRootPath()+'/unitController/get',{'id':_unitId},function(data){
		var $scale =  getTableEditor2(index,'scale');
		$scale.val(data.scale);
	});
}*/

/*//详细页。货品列表清空帮助方法
function clearHelp(ind){
	//仓库
	editor$ = getTableEditor(ind,'inWareHouseName');
	try{
		editor$.combotree('clear');
	}catch(e){
		
	}
	//属性
	editor$ = getTableEditor(ind,'goodsSpecName');
	try{
		editor$.val('');
	}catch(e){
		
	}
}*/

/*//详细页。货品列表验证帮助方法
function validDetailHelp(ind,billCode){
	//启动验证
	var editor$ = getTableEditor(ind,'goodsSpecName');
	if(!editor$.combogrid('options').disabled){
		try{
			editor$.combogrid('enableValidation');
		}catch(e){
			
		}
	}
	if(billCode!="bom"){
		editor$ = getTableEditor(ind,'unitPrice');
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
	
	editor$ = getTableEditor(ind,'costPrice');
	if(editor$.parents("td[field='costPrice']:hidden").size()<=0){
		try{
			editor$.combotree('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'inWareHouseName');
	if(editor$.parents("td[field='inWareHouseName']:hidden").size()<=0){
		try{
			editor$.combotree('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'goodsCode');
	if(editor$.parents("td[field='goodsCode']:hidden").size()<=0){
		try{
			editor$.combotree('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'quentity');
	if(editor$.parents("td[field='quentity']:hidden").size()<=0){
		try{
			editor$.combotree('enableValidation');
		}catch(e){
			
		}
	}
}

function validDetailHelp2(ind,billCode){
	//启动验证
	var editor$ = getTableEditor2(ind,'goodsSpecName');
	if(!editor$.combogrid('options').disabled){
		try{
			editor$.combogrid('enableValidation');
		}catch(e){
			
		}
	}
	if(billCode!="bom"){
		editor$ = getTableEditor2(ind,'unitPrice');
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
	
	editor$ = getTableEditor2(ind,'costPrice');
	if(editor$.parents("td[field='costPrice']:hidden").size()<=0){
		try{
			editor$.combotree('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor2(ind,'inWareHouseName');
	if(editor$.parents("td[field='inWareHouseName']:hidden").size()<=0){
		try{
			editor$.combotree('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor2(ind,'goodsCode');
	if(editor$.parents("td[field='goodsCode']:hidden").size()<=0){
		try{
			editor$.combotree('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor2(ind,'quentity');
	if(editor$.parents("td[field='quentity']:hidden").size()<=0){
		try{
			editor$.combotree('enableValidation');
		}catch(e){
			
		}
	}
}*/

/*//隐藏/显示保全全部按钮
function hideSaveAllLink(b){
	if(b||getEditingOnSize()<=0)
		$("a.savaAll").hide();
	else
		$("a.savaAll").show();
}
function hideSaveAllLink2(b){
	if(b||getEditingOnSize2()<=0)
		$("a.savaAll").hide();
	else
		$("a.savaAll").show();
}*/

/*function getEditingOnSize(){
	var _dataPanel = $('#goodsList').datagrid('getPanel');
	var _editing = _dataPanel.find(".editing-on");
	return _editing.length;
}
function getEditingOnSize2(){
	var _dataPanel = $('#materialList').datagrid('getPanel');
	var _editing = _dataPanel.find(".editing-on");
	return _editing.length;
}*/

/*//详细页。货品保存。保存所有按钮
function saveAll(){
	saveAll_ = true;
	$("a.editing-on").click();
	//var _dataPanel = $('#goodsList').datagrid('getPanel');
	//var _editing = _dataPanel.find(".editing-on");
	if(getEditingOnSize()>0){
		$.fool.alert({msg:'还有未填完的货品信息，请检查',fn:function(){
			saveAll_ = false;
		}});
	}else{
		hideSaveAllLink(true);
	}
}*/

/*//详细页。货品列表保存按钮
function save(obj){
	var ind = $(obj).fool('getRowIndex');
	
	validDetailHelp(ind);
	
	//验证
	var v = $("#goodsList").datagrid('validateRow',ind);
	if(v){
		//更新新建标识
		getTableEditor(ind,'_isNew').val(false);
		$('#goodsList').datagrid('endEdit', ind);
		//替换按钮
		$(obj).parent().html(getTBBtn(false,ind));
		//更新总金额
		getTotal();
		
		//if(getEditingOnSize()<=0)
			hideSaveAllLink();
	}else{
	}
}*/
/*//详细页。货品列表取消按钮
function cancel(obj){
	var ind = $(obj).fool('getRowIndex');
	
	//edit(obj);
	
	var _isNew = getTableEditor(ind,'_isNew').val();
	if(_isNew=='true'||_isNew==true){
		$.fool.confirm({msg:'您确定要撤销该记录？',fn:function(r){
			if(r) {
				$("#goodsList").datagrid('deleteRow',$(obj).fool('getRowIndex'));
				//if(getEditingOnSize()<=0)
				hideSaveAllLink();
			}
		}});
	}else{
		//validDetailHelp(ind);
		
		//var v = $("#goodsList").datagrid('validateRow',ind);
		if(v) {
			unitData_[ind] = myunitData[ind];
			$('#goodsList').datagrid('cancelEdit', ind);
			//if(getEditingOnSize()<=0)
			hideSaveAllLink();
		}
	}
	
	
}*/

/*//详细页。货品列表编辑按钮
function edit(obj){
	var ind = $(obj).fool('getRowIndex');
	$("#goodsList").datagrid('beginEdit',ind);
	
	//获取最低售价
	//var editor$ = getTableEditor(ind,'lowestPrice');
	//var _lowestPrice = editor$.val();
	//货品单价	
	var editor$ = getTableEditor(ind,'unitPrice');
	editor$.numberbox({
		height:22,width:'100%',
		validType:'goodPrice[\''+ind+'\']',required:true,precision:2,
		novalidate:true
	});
	
	//货品单位
	editor$ = getTableEditor(ind,'unitGroupId');
	var _unitGroupId = editor$.val();
	
	editor$ = getTableEditor(ind,'unitName');
	editor$.fool('combobox',{    
	    url:getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,
		data:_unitData,
	    valueField:'fid',width:80,panelMaxHeight:200,
	    textField:'name',editable:true,
	    onLoadSuccess:function(){
	    	var _d = $(this).combobox('getData');
	    	if(_d)_unitData = _d;
	    	var _uid = getTableEditor(ind,'unitId').val();
	    	$(this).combobox('setValue',_uid);
	    },
	    onSelect:function(record){
	    	getTableEditor(ind,'unitId').val(record.fid);
	    	changeGoodPrice(ind);
	    },
	    filter: function(q, row){
	    	if(!q)return;
			var opts = $(this).combobox('options');
			return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;
		},
		onChange:function(newVal,oldVal){
			//if(newVal&&oldVal)
				//alert(newVal+","+oldVal);
			
			for(var i in _unitData){
				if(newVal==_unitData[i].name){
					getTableEditor(ind,'unitId').val(_unitData[i].fid);
					$(this).combobox('setValue',_unitData[i].fid);
					return;
				}
			}
			getTableEditor(ind,'unitId').val('');
		}
	});
	
	editor$.combobox('textbox').bind('blur',function(){
		var _text = $(this).val();
		if(!_text)return;
		var _fid='';//,_name='';
		for(var i in _unitData){
			if(_text==_unitData[i].name){
				_fid = _unitData[i].fid;
				//_name = _unitData[i].name;
				break;
			}
		}
		getTableEditor(ind,'unitId').val(_fid);
		getTableEditor(ind,'unitName').val(_fid);
	});  

	
	//货品属性
	editor$ = getTableEditor(ind,'goodsSpecGroupId');//goodsSpecName
	var _goodSpecId = editor$.val();
	editor$ = getTableEditor(ind,'goodsSpecName');
	if(_goodSpecId!=undefined&&_goodSpecId.trim().length!=0){
		//货品属性
		editor$.attr("readonly",true).click(function(){
			win = $("#pop-win").fool('window',{modal:true,
				title:"选择货品属性",height:480,width:780,
				href:getRootPath()+"/goodsspec/window?groupId="+_goodSpecId+"&okCallBack=selectGoodSpec&onDblClick=selectGoodSpec&singleSelect=true&other="+ind
			});
		});
		
		editor$.combobox({
			valueField:"fid",textField:"name",
			url:getRootPath()+"/goodsspec/getChidlList?q=&groupId="+_goodSpecId
		});
		editor$.validatebox({required:true}).validatebox('disableValidation');
		
	}else{
		editor$.hide();
	}
	
	//仓库
	var _inWareHouseId = getTableEditor(ind,'inWareHouseId').val()?getTableEditor(ind,'inWareHouseId').val():$("#inWareHouseId").val();
	editor$ = getTableEditor(ind,'inWareHouseName');
	
	editor$.fool('combotree',{
	   // url:getRootPath()+'/basedata/warehourseList',
		data:_wareHouse,  
	    width:80,
	    panelWidth:166,
	    required:true,
	     defaultVal:_inWareHouseId,	 
	    onSelect:function(node){
	    	if(!node||node.id==undefined||node.id.length==0||node.text==undefined||node.text=='请选择'){
	    		editor$.combotree('clear');
	    		getTableEditor(ind,'inWareHouseId').val('');
	    		getTableEditor(ind,'_inWareHouseName').val('');
			}else{
				getTableEditor(ind,'inWareHouseId').val(node.id);
				getTableEditor(ind,'_inWareHouseName').val(node.text);
			}
	    },
	   onLoadSuccess:function(node,data){
		   if(undefined!=_inWareHouseId&&_inWareHouseId.length!=0){
	    	getTableEditor(ind,'inWareHouseName').combotree('setValue',_inWareHouseId);
	    	getTableEditor(ind,'inWareHouseId').val(_inWareHouseId);
	    	//if(undefined!=node&&null!=node)
	    	getTableEditor(ind,'_inWareHouseName').val(getTableEditor(ind,'inWareHouseName').combotree('getText'));
		   }
	    }
	});
	
	//替换按钮
	$(obj).parent().html(getTBBtn(true,ind));
	
	//
	hideSaveAllLink();
}*/

/*//获取表格里面某个编辑器方法
function getTableEditor(index,field){
	return getTableEditorHelp($("#goodsList"),index,field);
}
//获取表格里面某个编辑器方法
function getTableEditor2(index,field){
	return getTableEditorHelp($("#materialList"),index,field);
}

function getTableEditorHelp(tbId,index,field){
	var $t =$.fool._get$(tbId);
	return $t.fool('getEditor$',{'index':index,'field':field});
}*/

/*//详细页。货品列表删除按钮
function del(obj){
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r) 
			$("#goodsList").datagrid('deleteRow',$(obj).fool('getRowIndex'));
	}});
}*/

/*//详细页。添加货品按钮
function addGood(){
	var boxWidth=1074;
	var boxHeight=500;
	//if(target){
		//boxWidth=$(target).parents("#my-window").width();
		//boxHeight=$(target).parents("#my-window").height();
	//}
	
	//boxWidth=$("#my-window").width();
	//boxHeight=$("#my-window").height();
	
	var _billType=_billCode;
	var _customerId = $("#customerId").val();
	var _supplierId = $("#supplierId").val(); 
	win = $("#pop-win").fool('window',{modal:true,'title':"选择货品",width:boxWidth,height:boxHeight,
		href:getRootPath()+'/goods/window?okCallBack=selectGoods&onDblClick=selectGoods&billType='+_billType+'&customerId='+_customerId+'&supplierId='+_supplierId});
}*/

/*//详细页。添加货品
function selectGoods(data){
	if(data==undefined)return;	
	$(data).each(function(i,n){
		$("#goodsList").datagrid('appendRow',{
			goodsId: n.fid,
			barCode: n.barCode,
			goodsCode: n.code,
			goodsSpecId:n.goodsSpecId,
			goodsName: n.name,
			goodsSpec:n.spec,
			goodsSpecGroupId:n.goodsSpecGroupId,
			unitName:n.unitName,
			quentity:1,
			scale:n.unitScale,
			unitId:n.unitId,
			type:0,
			unitPrice:n.referencePrice,
			lowestPrice:n.lowestPrice,
			unitGroupId:n.unitGroupId,
			_isNew:true
		});
		
		var _data = $("#goodsList").datagrid("getData");
		var ind = _data.rows.length-1;
		$("a.btn-index-edit-"+ind).click();
	});
	closeWin();
	hideSaveAllLink(false);
}*/

/*//详细页。货品列表操作按钮
function goodsListAction(value,row,index){
	return getTBBtn(false,index);
}*/

//详细页。货品列表单价
function priceAction(value,row,index){
	var _n = new Number(value);
	if(isNaN(_n))
		return 0;
	else 
		return _n;
}

/*//详细页。货品列表仓库
function wareHouseAction(value,row,index){
	return row._inWareHouseName?row._inWareHouseName:value;
	//return getTableEditor(index,'_inWareHouseName').val();
//	value = row.inWareHouseId;
	for(var i in _wareHouse){
		if(row.inWareHouseId==_wareHouse[i].id)return _wareHouse[i].text;
		else{
			var _children = _wareHouse[i].children;
			if(_children!=undefined||_children.length!=0){
				for(var j in _children){
					return wareHouseAction(value,row,index,_children[j]);
				}
			}
			
		}
	}
}*/

/*//详细页。货品列表单位下拉
function unitAction(value,row,index){
	for(var i in _unitData){
		if(row.unitId==_unitData[i].fid)return _unitData[i].name;
	}
}
//详细页。货品列表金额格式化
function typeAction(value,row,index){
	if(row.quentity&&row.unitPrice){
		return (row.quentity*row.unitPrice).toFixed(2);
		//return parseInt(row.quentity)*parseInt(row.unitPrice);
	}else{
		return 0;
	}
}*/

/*//获取数据
function getData(data){
	var _d = data;
	if(typeof data[0]!='undefined'){
		_d = data[0];
	}
	return _d;
	
	return getDataTop1(data);
}*/

/*//详细页。关闭窗口方法
function closeWin(){
	if(win)	win.window('close').window('clear');
}
//打开窗口方法
function openWin(title,href){
	$("#my-window").fool('window',{top:10,left:0,modal:true,'title':title,'href':href,onBeforeOpen:function(){
		//closeWin();
		initUnitData(false);
		initWareHouse(false);
	},onBeforeClose:function(){
		//closeWin();
	},onClose:function(){
		initUnitData(false);
		initWareHouse(false);
	}});
}*/

/*function getTBBtn(f,index){
	if(_flag=='detail'&&_billStat!='0')return;
	if(f){
		var s = "<a href='javascript:;' class='btn-save editing-on btn-index-save-"+index+"' onclick='save(this)' title='确认'></a>";
		var c = "<a href='javascript:;' class='btn-back btn-index-cancel-"+index+"' onclick='cancel(this)' title='撤销'></a>";
		return s+c;
	}else{
		var r = "<a href='javascript:;' class='btn-del btn-index-del-"+index+"' onclick='del(this)' title='删除'></a>";
		var e = "<a href='javascript:;' class='btn-edit btn-index-edit-"+index+"' onclick='edit(this)' title='编辑'></a>";
		return e+r;
	}
}*/

/*//计算货品列表总金额
function getTotal(){
	var rows=$('#goodsList').datagrid('getRows');
	var total=0;
	for(var i=0;i<rows.length;i++){
		rows[i].type=rows[i].quentity*rows[i].unitPrice;
		total+=rows[i].type;
	}
	$("#totalAmount").val(total);
}*/

//详细页。更多按钮
function show_more(){
	$(".more-div").toggle();
	$(".more-div").fool('fromEnable',{'enable':false});
}

//初始化详细页 按钮
function btnFooterInit(){
	var $obj = $(".btn-footer");
	var _stat = $("#recordStatus").val();
	var _id = $("#fid").val();
	//var _flag = $("#page-flag").val();
	var _btn = "";
	
	var ref = "<a id=\"refreshForm\" class=\"btn-blue2 btn-s2\" onclick=\"refreshForm()\">刷新</a>&nbsp;&nbsp;";
	var add = "<a id=\"save\" onclick=\"saveData()\" class=\"btn-blue2 btn-s2\">保存</a>&nbsp;&nbsp;";
	var appr = "<a id=\"verify\" class=\"btn-blue2 btn-s2\" onclick=\"passAudit('"+_id+"')\">审核</a>&nbsp;&nbsp;";
	var can = "<a id=\"void\" class=\"btn-blue2 btn-s2\" onclick=\"cancelVoid('"+_id+"')\">作废</a>&nbsp;&nbsp;";	
	var pri = "<a id=\"print\" class=\"btn-blue2 btn-s2\" onclick=\"printBillDetail('"+_id+"','"+_billCode+"')\">打印</a>&nbsp;&nbsp;";
	var copyRow = "<a id=\"copyRow\" onclick=\"copyRow('"+_id+"')\" class=\"btn-blue2 btn-s2\">复制</a>&nbsp;&nbsp;";
	/*if(_stat==undefined||_stat.length==0){
		_btn = add;
	}else{
		if(_flag=='copy'){
			_btn = add;
		}else if(_flag=='edit'){
			_btn = add+copyRow+pri+ref;
		}else if(_flag=='detail'&&_stat!=1&&_stat!=2){
			_btn = ref+appr+can+pri;
		}else{
			if(_stat==0){
				_btn = add+appr+can+ref+pri;
			}else if(_stat==1){
				_btn = can+ref+pri;
			}else{
				_btn = ref+pri;
			}
		}
	}*/
	
	if(_stat==0){
		_btn = add+copyRow+pri+ref;
	}else if(_stat == 1){
		_btn = copyRow+pri+ref+can;
	}else{
		_btn = copyRow+pri;
	}
	
	
	$obj.html(_btn);
}

/*//操作按钮
function actionFormat(value,row,index){
	var d = "";//"<a href='#' class='btn-detail' onclick='viewRow(\""+value+"\")' title='详情'></a>";
	var e = "<a href='javascript:;' class='btn-edit' onclick='editRow(\""+value+"\")' title='编辑'></a>";
	var c = "<a href='javascript:;' class='btn-copy' onclick='copyRow(\""+value+"\")' title='复制'></a>";
	var r = "<a href='javascript:;' class='btn-del' onclick='delRow(\""+value+"\")' title='删除'></a>";
	var a = "<a href='javascript:;' class='btn-approve' onclick='passAudit(\""+value+"\")' title='审核'></a>";
	var zf = "<a href='javascript:;' class='btn-cancel' onclick='cancelVoid(\""+value+"\")' title='作废'></a>";
	//var dy = "<a href='#' class='btn-printer' onclick='printerRow(\""+value+"\")' title='打印'></a>";
	
	if(row.recordStatus==0){
		return d+a+zf+c+r;//e+d+c+a+zf+r;
	}else if(row.recordStatus==1){
		return zf+c;//d+zf+c;
	}else{
		return c;//d+c;
	}
}*/
//新模板操作按钮
function actionFormatNew(value,options,row){
	var d = "";//"<a href='#' class='btn-detail' onclick='viewRow(\""+value+"\")' title='详情'></a>";
	var e = "<a href='javascript:;' class='btn-edit' onclick='editRowNew(\""+row.fid+"\")' title='编辑'></a> ";
	var c = "<a href='javascript:;' class='btn-copy' onclick='copyRowNew(\""+row.fid+"\")' title='复制'></a> ";
	var r = "<a href='javascript:;' class='btn-del' onclick='delRow(\""+row.fid+"\")' title='删除'></a> ";
	var a = "<a href='javascript:;' class='btn-approve' onclick='passAudit(\""+row.fid+"\")' title='审核'></a> ";
	var zf = "<a href='javascript:;' class='btn-cancel' onclick='cancelVoid(\""+row.fid+"\")' title='作废'></a> ";
	//var dy = "<a href='#' class='btn-printer' onclick='printerRow(\""+value+"\")' title='打印'></a>";
	
	if(row.recordStatus==0){
		return r+""+c+""+a+""+zf;//e+d+c+a+zf+r;
	}else if(row.recordStatus==1){
		return zf+""+c;//d+zf+c;
	}else{
		return c;//d+c;
	}
}

//列表页。编辑详情
function editRow(value){
	//$("#my-window").fool('window',{top:36,left:0,modal:true,title:'编辑单据',href:getRootPath()+'/warehouse/'+_billCode+'/edit?flag=edit&id='+value});
	
	openWin('编辑单据',getRootPath()+'/warehouse/'+_billCode+'/edit?flag=edit&id='+value);
	
	return false;
}

//列表页。查看详情
function viewRow(value,stat){
	//$("#my-window").fool('window',{top:36,left:0,modal:true,title:'查看详情',href:getRootPath()+'/warehouse/'+_billCode+'/edit?flag=detail&id='+value});
	var theflag = stat!=0 ? "detail" : "edit";
	
	openWin('查看详情',getRootPath()+'/warehouse/'+_billCode+'/edit?flag='+theflag+'&id='+value);
	
	return false;
}
//新模板列表页。编辑详情
function editRowNew(value){
	//$("#my-window").fool('window',{top:36,left:0,modal:true,title:'编辑单据',href:getRootPath()+'/warehouse/'+_billCode+'/edit?flag=edit&id='+value});
	var url="";
	if(_billCode == 'xsfld'){
		url = getRootPath()+'/salesRebateRebBill/edit?flag=edit&id='+value+'&billCode='+_billCode;
	}else{
		url = getRootPath()+'/warehouse/'+_billCode+'/edit?flag=edit&id='+value+'&billCode='+_billCode;
	}
	warehouseWin("编辑"+_billCodeName,url)
	return false;
}
//新模板列表页。查看详情
function viewRowNew(value,stat,fn,args){
	//$("#my-window").fool('window',{top:36,left:0,modal:true,title:'查看详情',href:getRootPath()+'/warehouse/'+_billCode+'/edit?flag=detail&id='+value});
	var theflag = stat!=0 ? "detail" : "edit";
	var url="";
	if(_billCode == 'xsfld'){
		url = getRootPath()+'/salesRebateRebBill/edit?flag='+theflag+'&id='+value+'&billCode='+_billCode;
	}else if(_billCode == 'cgfld'){
		url = getRootPath()+'/purchaseRebBill/edit?flag='+theflag+'&id='+value+'&billCode='+_billCode;
	}else if(_billCode == 'zjjh'){
		url = getRootPath()+'/capitalPlan/edit?flag='+theflag+'&id='+value+'&billCode='+_billCode;
	}else{
		url = getRootPath()+'/warehouse/'+_billCode+'/edit?flag='+theflag+'&id='+value+'&billCode='+_billCode;
	}
	if(fn){
		warehouseWin("查看"+_billCodeName,url,fn,args)
	}else{
		warehouseWin("查看"+_billCodeName,url)
	}
	
	
	return false;
}
//新模板列表页。复制
function copyRowNew(value){
	//$("#my-window").fool('window',{top:36,left:0,modal:true,title:'复制单据',href:getRootPath()+'/warehouse/'+_billCode+'/edit?mark=1&flag=copy&id='+value});
	var url="";
	if(_billCode == 'xsfld'){
		url = getRootPath()+'/salesRebateRebBill/edit?mark=1&flag=copy&id='+value+'&billCode='+_billCode;
	}else if(_billCode == 'cgfld'){
		url = getRootPath()+'/purchaseRebBill/edit?mark=1&flag=copy&id='+value+'&billCode='+_billCode;
	}else{
		url = getRootPath()+'/warehouse/'+_billCode+'/edit?mark=1&flag=copy&id='+value+'&billCode='+_billCode;
	}
	
	warehouseWin("复制"+_billCodeName,url)
	return false;
}
//列表页。复制
function copyRow(value){
	//$("#my-window").fool('window',{top:36,left:0,modal:true,title:'复制单据',href:getRootPath()+'/warehouse/'+_billCode+'/edit?mark=1&flag=copy&id='+value});
	
	openWin('复制单据',getRootPath()+'/warehouse/'+_billCode+'/edit?mark=1&flag=copy&id='+value);
	
	return false;
}

//列表页。删除
function delRow(value){
	var myurl = getRootPath()+'/warehouse/'+_billCode+'/delete';
	if(_billCode == 'xsfld'){
		myurl = getRootPath()+'/salesRebateRebBill/delete';
	}else if(_billCode == 'cgfld'){
		myurl = getRootPath()+'/purchaseRebBill/delete';
	}
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r){
			 $.ajax({
					type : 'post',
					url : myurl,
					data : {id :value},
					dataType : 'json',
					success : function(data) {
						var myresult = data.returnCode;
						var mymsg = data.message;
						if(_billCode == 'xsfld' || _billCode == 'cgfld'){
							myresult = data.returnCode;
							mymsg = data.message;
						}
						if(myresult == 0){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
								refreshData();
							}});
						}else{
							$.fool.alert({msg:'删除失败！'+mymsg});
						}
		    		}
				});
		}
	},title:'确认'});
}

//新版初始化列表 搜索栏输入框
/*$("#mySupplierName,#myGoodName,#myCustomerName").textbox({
	width:165,
	height:30,
	editable:false,
});*/
$("#payeeName").textbox({
	width:160,
	height:30,
});
$("#startDay").fool('datebox',{
	prompt:'开始日期',
	width:165,
	height:32,
	inputDate:true,
	editable : true
});

$("#endDay").fool('datebox',{
	prompt:'结束日期',
	width:165,
	height:32,
	inputDate:true,
	editable : true
});

//新版列表页 搜索栏
var boxWidth=165,boxHeight=31;//统一设置输入框大小
$('#myInMemberName').fool('dhxComboGrid',{//人员
	required:true,
	novalidate:true,
	width:boxWidth,
	prompt:"人员",
	height:boxHeight,
	data:smemData,
	focusShow:true,
    onlySelect:true,
	filterUrl:getRootPath()+'/member/vagueSearch',
	setTemplate:{
		input:"#username#",
		columns:[
					{option:'#userCode#',header:'编号',width:100},
					{option:'#jobNumber#',header:'工号',width:100},
					{option:'#username#',header:'名称',width:100},
					{option:'#phoneOne#',header:'电话',width:100},
					{option:'#deptName#',header:'部门',width:100},
					{option:'#position#',header:'职位',width:100},
				],
  	},
    toolsBar:{
        refresh:true
    },
	onChange:function(value,text){
		$("#myInMemberId").val(value);
	}	
});
$('#myOutMemberName').fool('dhxComboGrid',{//人员
	required:true,
	novalidate:true,
	width:boxWidth,
	prompt:"人员",
	height:boxHeight,
	data:smemData,
	focusShow:true,
    onlySelect:true,
	filterUrl:getRootPath()+'/member/vagueSearch',
	setTemplate:{
		input:"#username#",
		columns:[
					{option:'#userCode#',header:'编号',width:100},
					{option:'#jobNumber#',header:'工号',width:100},
					{option:'#username#',header:'名称',width:100},
					{option:'#phoneOne#',header:'电话',width:100},
					{option:'#deptName#',header:'部门',width:100},
					{option:'#position#',header:'职位',width:100},
				],
  	},
    toolsBar:{
        refresh:true
    },
	onChange:function(value,text){
		$("#myOutMemberId").val(value);
	}	
});


//新版货品查询列表
$('#myGoodName').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	width:boxWidth,
	height:boxHeight,
	prompt:"货品",
	focusShow:true,
	onlySelect:true,
	filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=8',
	toolsBar:{
        refresh:true
    },
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
					{option:'#barCode#',header:'条码',width:100},
					{option:'#spec#',header:'规格',width:100},
					{option:'#unitName#',header:'单位',width:100},
				],
	},
	data:sgoodsData,
	onChange:function(value,text){
		$('#myGoodsId').val(value);
	}
});

//供应商
$('#mySupplierName').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	width:boxWidth,
	height:boxHeight,
	data:ssupData,
	prompt:"供应商",
	focusShow:true,
    onlySelect:true,
	filterUrl:getRootPath()+'/supplier/list?showDisable=0',
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
  	},
    toolsBar:{
        refresh:true
    },
  	onChange:function(value,text){
	$("#mySupplierId").val(value);
   }
});

//客户
$('#myCustomerName').fool('dhxComboGrid',{
	required:true,
	novalidate:true,
	width:boxWidth,
	height:boxHeight,
	data:scusData,
	prompt:"客户",
	focusShow:true,
    onlySelect:true,
	filterUrl:getRootPath()+'/customer/list?showDisable=0',
	setTemplate:{
		input:"#name#",
		columns:[
					{option:'#code#',header:'编号',width:100},
					{option:'#name#',header:'名称',width:100},
				],
  	},
    toolsBar:{
        refresh:true
    },
	onChange:function(value,text){
		$("#myCustomerId").val(value);
	}
})


//设置鼠标放进去自动出来下拉列表
$("#search-form").find("input[_class]").each(function(i,n){($(this));});
/*if($('#myInMemberName').length > 0)//人员
$('#myInMemberName').combogrid('textbox').focus(function(){
	$('#myInMemberName').combogrid('showPanel');
});
if($('#myOutMemberName').length > 0)//人员
	$('#myOutMemberName').combogrid('textbox').focus(function(){
		$('#myOutMemberName').combogrid('showPanel');
	});
if($('#myGoodName').length > 0)//货品
 $('#myGoodName').combogrid('textbox').focus(function(){
	$('#myGoodName').combogrid('showPanel');
});*/
/*if($('#mySupplierName').length > 0)//供应商
	 $('#mySupplierName').combogrid('textbox').focus(function(){
		$('#mySupplierName').combogrid('showPanel');
	});*/
/*if($('#myCustomerName').length > 0)//客户
	 $('#myCustomerName').combogrid('textbox').focus(function(){
		$('#myCustomerName').combogrid('showPanel');
    });*/

function getBillSign(billCode){
	var billSign=[
		          {text:"qckc", value: 91},
		          {text:"qcyf", value: 92},
		          {text:"qcys", value: 93},
		          {text:"cgdd", value: 10},
		          {text:"cgrk", value: 11},
		          {text:"cgth", value: 12},
		          {text:"cgxjd", value: 13},
		          {text:"cgsqd", value: 14},
		          {text:"cgfp", value: 15},
		          {text:"pdd", value: 20},
		          {text:"dcd", value: 21},
		          {text:"bsd", value: 22},
		          {text:"scll", value: 30},
		          {text:"sctl", value: 32},
		          {text:"cprk", value: 31},
		          {text:"cptk", value: 33},
		          {text:"xsdd", value: 40},
		          {text:"xsch", value: 41},
		          {text:"xsth", value: 42},
		          {text:"xsbjd", value: 43},
		          {text:"xsfp", value: 44},
		          {text:"fyd", value: 53},
		          {text:"skd", value: 51},
		          {text:"fkd", value: 52},
		          {text:"scjhd", value: 34},
		          {text:"fysqd", value: 54},
		          {text:"cgfld", value: 55},
		          {text:"xsfld", value: 56},
		          {text:"gzd", value: 110},
		          {text:"gdzc", value: 120},
		          {text:"dtfy", value: 130},
		          {text:"fhd", value: 23},
		          {text:"shd", value: 24},
		          {text:"thbjd", value: 61},
		          {text:"cdfbjd", value: 62},
		          {text:"ysfbjd", value: 63},
		          {text:"zjjh", value: 100},
		          ]
	for(var i=0;i<billSign.length;i++){
		if(billSign[i].text==billCode){
			return billSign[i].value;
		}
	}
	return "";
}

function queryByWarehouse(relationId){
	var result=false;
	$.ajax({
		type : 'post',
		url : getRootPath()+"/capitalPlan/queryByWarehouse",
		data : {relationId :relationId},
		dataType : 'json',
		async:false,
		success : function(data) {
			if(data){
				result=true;
			}
		}
	});
	return result;
}

function checkPlanAmount(relationId,billAmount){
	var result=false;
	$.ajax({
		type : 'post',
		url : getRootPath()+"/capitalPlan/checkPlanAmount",
		data : {relationId :relationId,billAmount:billAmount},
		dataType : 'json',
		async:false,
		success : function(returnDate) {
			if(returnDate.returnCode==0){
				result=true;
			}else{
				result=false;
			}
		}
	});
	return result;
}

function capitalPassAudit(value){
	$.ajax({
		type : 'post',
		url : getRootPath()+"/capitalPlan/capitalPassAudit",
		data : {relationId :value,relationSign:getBillSign(_billCode)},
		dataType : 'json',
		async:false,
		success : function(returnDate) {
			if(returnDate.returnCode==0){
				auditNew(value);
			}else{
				$.fool.alert({msg:returnDate.message});
			}
		}
	});
}