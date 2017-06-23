
/**
 * 仓储应用详情页模板JS
 */
var win,win2,_editor;
var natureId='';// 货品属性id
var goodsId='';// 货品id
/* var number="";//规格id */
var datalist="";// 货品材料明细
var editkd = '';// ctrl快捷键全局变量
// var addkd = '';// +快捷键全局变量
var mycookieSave = '';
var localCache = '';
var myunitData = [];
var newIndex = 0;// 新建行的行号全局变量
var newCIndex = 0;// 新建行的行号全局变量
//var s_goodsId = "";
// var selectTips = [];
var amount='';//发货单根据发货地ID、收货地ID、运输方式、装运方式、运输公司查询运输费报价
function rebatelv(){
	var rates = $('#amount').val()!=''&&$('#sales').val()!=''&&$('#sales').val()!='0'?$('#amount').val()/$('#sales').val():0;
	$('#rates').val((100*rates).toFixed(2));
}
// 全局变量
var goodsIndex = '';
var actionC = [];   // 标识是不是编辑状态
var actionCl = [];  // 标识是否点击了操作单元格
var unitData_ = [];
var mydetails = '';
var transDetails='';
var myobj = [];
var mynum = 0;
var saveAll_ = false; // 判断是否一键保存，决定是否弹出下拉框
/*
 * 新模板详情页初始化 flag-编辑状态，billStat-单据状态,details-货品数据
 */
var _billCode,_billCodeName,_unitData=null,_flag,_wareHouse=null,_billStat;
function initEdit(flag,billStat,billCode,billCodeName,details,transportDetails){ 
	//console.log(details);
	//console.log(transportDetails);
	_billCode = billCode;
	_billCodeName = billCodeName;
	_flag = flag;
	_billStat = billStat;
	if(details){
		mydetails = details;
	}
	if(transportDetails){
		transDetails=transportDetails;		
	}else{
		transDetails='';
	}
	/*
	 * initWareHouse(false); initUnitData(false);
	 */
	// editData();
	// 设置_imp属性后的相应操作
	$('input[_imp=true]').prev().prepend('<em>*</em>');
	for(var i=0; i<$('input[_imp=true]').length; i++){
		if(!$('input[_imp=true]').eq(i).attr('_class')){
			$('input[_imp=true]').eq(i).validatebox({required:true,novalidate:true});
		}	
	}
	/*//设置_trim属性的输入框去除前后空格
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
	}*/
	//鼠标失去焦点事件，设置_trim属性的输入框去除前后空格
	$('input[_trim=true]').blur(function(){
		var s=$(this).val();
		var id=$(this).attr('id');
		$("#"+id).val($.trim(s)); 	
	})
	
	$('.inlist').addClass('status'+billStat);// 加上是否审核作废的状态图标
	if(flag=='detail'&&billStat!='0'){
		var tips = billStat == 2?1:0;
		
		// $('#goodsList').datagrid();
		
		$("#warehousebillForm").fool('fromEnable',{'enable':false});
		// $('#memberName').combogrid('fromEnable',{'enable':false});
		
		$("input[_class^=datebox]").each(function(i,n){
			$(n).val(getDateStr($(n).val()));			
		});
		$("input[name*=Time]").each(function(i,n){
			$(n).val(getDateStr($(n).val()));
		});
		
	}else{
		if(flag=='copy'){
			$("#fid").val('');
			$("#voucherCode").val('');
			$("#freeAmount").val('');
			$('.hideOut input').val("");
		}		
		// 初始化文本框
		$("#warehousebillForm").find("input[_class]").each(function(i,n){editInputInit($(this));});
		/*
		 * if($('#supplierName').length > 0)
		 * $('#supplierName').combogrid('textbox').focus(function(){
		 * $('#supplierName').combogrid('showPanel'); });
		 * if($('#customerName').length > 0)
		 * $('#customerName').combogrid('textbox').focus(function(){
		 * $('#customerName').combogrid('showPanel'); });
		 * if($('#inMemberName').length > 0)
		 * $('#inMemberName').combogrid('textbox').focus(function(){
		 * $('#inMemberName').combogrid('showPanel'); });
		 * if($('#outMemberName').length > 0)
		 * $('#outMemberName').combogrid('textbox').focus(function(){
		 * $('#outMemberName').combogrid('showPanel'); });
		 * if($('#bankName').length > 0)
		 * $('#bankName').combogrid('textbox').focus(function(){
		 * $('#bankName').combogrid('showPanel'); }); if($('#goodsCode').length >
		 * 0) $('#goodsCode').combogrid('textbox').focus(function(){
		 * $('#goodsCode').combogrid('showPanel'); });
		 */
		/*
		 * if($('#specName').length > 0)
		 * $('#specName').combogrid('textbox').focus(function(){
		 * $('#specName').combogrid('showPanel'); });
		 */
		/*
		 * if($('#memberName').length > 0)
		 * $('#memberName').combogrid('textbox').focus(function(){
		 * $('#memberName').combogrid('showPanel'); });
		 */
		$('#customerPhone').attr('readonly','readonly');
		$('#customerCode').attr('readonly','readonly');
		$('#supplierPhone').attr('readonly','readonly');
		$('#supplierCode').attr('readonly','readonly');
		// 各种单
		$(".relationBox").css({'width':'160px','padding-right':'20px'});	
		$(".relationBox").click(function(){
			_editor = $(this);
			var _relationCode = $(this).attr('_billCode');
			if((_relationCode=="xsdd"||_relationCode=="cgdd"||_relationCode=="xsdd cgsqd") && _billCode != "cgrk" && _billCode != "xsch"){
				win = $("#pop-win").fool('window',{modal:true,onClose:function(){
					if($('#salewin').html() != ''){
						$('#salewin').window('destroy');
					}
				},'title':"选择",height:480,width:780,
					href:getRootPath()+'/salebill/xsdd/window?okCallBack=selectRelationNew&onDblClick=selectRelationNew&_billCode='+_billCode+'&billCode='+_relationCode});
				return;
			}
			win = $("#pop-win").fool('window',{modal:true,onClose:function(){
//                $("#pop-win").window('destroy');
				if($('#salewin').html() != ''){
                    $('#salewin').window('destroy');
                }
			},'title':"选择",height:480,width:780,
				href:getRootPath()+'/salebill/xsdd/window?okCallBack=selectRelationNew&onDblClick=selectRelationNew&singleSelect=true&_billCode='+_billCode+'&billCode='+_relationCode});
		});
		
		// 设置失去焦点后自动验证
		$("#warehousebillForm").fool('fromBlurVali');
		
		// 每5秒自动保存新增单据的内容
		if(_flag != 'edit' && localCache == "1"){mycookieSave = setInterval(function(){localSave()},5000);}
	}
	 
	_billCode !='zjjh' &&_billCode !='xsfld' && _billCode !='cgfld'?initGoodsListTableNew(details,tips):null;// 加载货品数据
	_billCode =='shd'||_billCode =='fhd' ?initContainerListTableNew(details,tips):null;// 加载货品数据
	
	_billCode !='zjjh'?mybtnFooterInit():null;// 最下方操作按钮的初始化
	
	hideSaveAllLink(true);// 根据状态显示 一键保存
	
	if(flag!='detail'){
		if(_billCode !='bom'){
			enterController("warehousebillForm");
		}else{
			enterController("warehousebillForm",1);
		}
	}
	
	// 详细信息折叠按钮
	$("#openBtn").click(
			function(e) {
				$(".hideOut").css("display","inline-block");
				$('#openBtn').css("display","none");
				$('#closeBtn').css("display","inline-block");
			});
	$("#closeBtn").click(
			function(e) {
				$(".hideOut").css("display","none");
				$('#openBtn').css("display","inline-block");
				$('#closeBtn').css("display","none");	
			});
}

// 将单据内容保存至localStorage里面
function localSave(){
	if(_billCode=="cgfld" || _billCode=="xsfld" || _billCode=="fysqd"){
		return false;
	}
	var _dataPanel = $('#goodsList');
	// 让表格的input排除在外，不被序列化
	_dataPanel.find("td[aria-describedby] input.editable").removeAttr("name");
	_dataPanel.find("input.dhxcombo_input").each(function(){
		$(this).next().removeAttr("name");
	});
	var fdata = $("#warehousebillForm").serializeJson();
	
	var _editing = _dataPanel.find(".editing-on");
	$('#supplierName').length>0?fdata.supplierName = $('#supplierName').next()[0].comboObj.getComboText():null;
	$('#customerName').length>0?fdata.customerName = $('#customerName').next()[0].comboObj.getComboText():null;
	$('#inMemberName').length>0?fdata.inMemberName = $('#inMemberName').next()[0].comboObj.getComboText():null;
	$('#outMemberName').length>0?fdata.outMemberName = $('#outMemberName').next()[0].comboObj.getComboText():null;
	$('#specName').length>0?fdata.specName = $('#specName').next()[0].comboObj.getComboText():null;
	$('#goodsCode').length>0?fdata.goodsCode = $('#goodsCode').next()[0].comboObj.getComboText():null;
	$('#accountUnitName').length>0?fdata.accountUnitName = $('#accountUnitName').next()[0].comboObj.getComboText():null;
	if(_editing.length<=0){
		details = $("#goodsList").jqGrid('getRowData');
		if(_billCode=="scjhd"){
			var _dataPanel2 = $('#materialList');
			var _editing2 = _dataPanel2.find(".editing-on");
			if(_editing2.length<=0){
				var myindex2 = $('#materialList').jqGrid('getRowData').length;
				var details2=$("#materialList").jqGrid('getRowData');
				for(var i=0;i<details2.length;i++){
					details2[i].detailType=1;
				}
				fdata = $.extend(fdata,{'details2':JSON.stringify(details2)});
			}
		}else if(_billCode=="bom"){
			var newDetails=eval(details);
			for(var i in newDetails){
				newDetails[i].specId=newDetails[i].goodsSpecId;
				newDetails[i].type="";
			}
			details=newDetails;
		}
		var details_ = JSON.stringify(details);
		fdata = $.extend(fdata,{"details":details_});
	}
	var _mysave = localStorage[_billCode];
	var mysave = _mysave !=null?$.parseJSON(_mysave):'';
	mysave = $.extend(mysave,fdata);
	var mysave_ = JSON.stringify(mysave);
	localStorage[_billCode]=mysave_;
}

// 改变供应商和客户输入框的状态
function disableRelation(bool){
	if(bool){
		$('#supplierName').length>0?$('#supplierName').next()[0].comboObj.disable():null;
		$('#customerName').length>0?$('#customerName').next()[0].comboObj.disable():null;
	}else{
		$('#supplierName').length>0?$('#supplierName').next()[0].comboObj.enable():null;
		$('#customerName').length>0?$('#customerName').next()[0].comboObj.enable():null;
	}
}

/*
 * 收货单从表2
 */
function initContainerListTableNew(details,atips){
	$('#containerList').jqGrid({
		  datatype:"local",
		  data:[],
		  footerrow: true,
		  autowidth:true,// 自动填满宽度
		  height:"100%",
		  forceFit:true,// 改变列宽度，总宽度不变
		onCellSelect:function(rowid,iCol,cellcontent,e){
			if(iCol != 0 && _flag != "detail" && $("#containerList #"+rowid).find(".editing-on").length <= 0){
				/* editNew(rowid); */
				editContainerNew(rowid);
			}
		},
		colModel:[
			{name:'_isNew',label:'是否新增',width:100,hidden:true,editable:true,edittype:"text"},
			{name:'updateTime',label:'更新时间',width:100,hidden:true,editable:true,edittype:"text"},
			{name:'action',label:'操作',width:80,align:"center",sortable:false,formatter:containerListActionNew},
	        {name:'containerNumber',label:'箱号',width:100,align:"center",sortable:false,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({required:true,validType:'maxLength[50]',sortable:false,missingMessage:"该项必填",width:'100%',height:'100%'});
	        }}},
	        {name:'sealingNumber1',label:'封号1',width:100,align:"center",sortable:false,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({required:true,validType:'maxLength[50]',missingMessage:"该项必填",width:'100%',height:'100%'});
	        }}},
	        {name:'sealingNumber2',label:'封号2',width:100,align:"center",sortable:false,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({required:true,validType:'maxLength[50]',sortable:false,missingMessage:"该项必填",width:'100%',height:'100%'});
	        }}},
	        {name:'describe',label:'备注',align:"center",sortable:false,width:80,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({validType:'maxLength[200]',width:'100%',height:'100%'});
	        }}},
	    ],
	    loadComplete:function(data){
	    	$("#containerList").footerData('set',{containerNumber:'',action:'new',sealingNumber1:'',sealingNumber2:'',describe:""});
	    }
	});
	// 初始化货品表格
	if(transDetails!=undefined){
		$('#containerList')[0].addJSONData(transDetails);
		if(atips == 1){
			$('#gbox_containerList .ui-jqgrid-sdiv').hide();
		}
		if(_billStat != 0){
			$('#cAddrow').hide();
		}
		newCIndex = $('#containerList').jqGrid('getRowData').length;
	}
}

/*
 * 新模板详情页初始化货品表格
 */
function initGoodsListTableNew(details,atips){
	$('#goodsList').jqGrid({
		  datatype:"local",
		  data:[],
		  footerrow: true,
		  autowidth:true,// 自动填满宽度
		  // width:$("#list1").width()*0.95,
		  height:"100%",
		  forceFit:true,// 改变列宽度，总宽度不变
		  /*
			 * onSelect:function(index,row){ //选择页面看不见的行时滚动条自动滑动到该行 var pOffset =
			 * $('#goodsList').datagrid('getPanel').find("tr[datagrid-row-index='"+index+"']");
			 * var poffset_ = pOffset.offset().top; var mypanelTop = $('#list1
			 * .datagrid').offset().top; var scrollTop_ =
			 * pOffset.closest('.window-body').scrollTop(); var windowHeight =
			 * pOffset.closest('.window-body').outerHeight(); var num =
			 * poffset_-windowHeight+100; if(index != 0 && num>0){
			 * pOffset.closest('.window-body').scrollTop(scrollTop_+num); }else
			 * if(index != 0 && poffset_<95){
			 * pOffset.closest('.window-body').scrollTop(scrollTop_-95+poffset_);
			 * }else if(index == 0 && mypanelTop<95){
			 * pOffset.closest('.window-body').scrollTop(scrollTop_-60+mypanelTop); }
			 * 
			 * var indexobj =
			 * $(this).parent().find('tr[datagrid-row-index="'+index+'"]
			 * td[field="action"] a.btn-del'); if(row.action != 'new' &&
			 * !actionCl[index] && indexobj.length>0 ){
			 * editNew(indexobj,index);// 选中一行打开编辑状态 } // 触发即时分仓 var
			 * goodsId=getTableEditor(index,'goodsId').val(); var
			 * natureId=getTableEditor(index,'goodsSpecId').val();
			 * if(goodsId!=''){ branchWarehouse(goodsId,natureId) } },
			 * onClickCell:function(index,field,value){ if(field == 'action' &&
			 * actionC[index]){ actionCl[index] = true; }else{ actionCl[index] =
			 * false; } },
			 */
		onSelectRow:function(rowid,status){
			// 选择页面看不见的行时滚动条自动滑动到该行
			var pOffset = $('#goodsList').find("tr#"+rowid);
			var poffset_ = pOffset.offset().top;
			var mypanelTop = $('#list1 #gbox_goodsList').offset().top;
			var scrollTop_ = pOffset.closest('.window-body').scrollTop();
			var windowHeight = pOffset.closest('.window-body').outerHeight();
			var num = poffset_-windowHeight+100;
			var index = pOffset.index();
			if(index != 1 && num>0){
				pOffset.closest('.window-body').scrollTop(scrollTop_+num);
			}else if(index != 1 && poffset_<95){
				pOffset.closest('.window-body').scrollTop(scrollTop_-95+poffset_);
			}else if(index == 1 && mypanelTop<95){
				pOffset.closest('.window-body').scrollTop(scrollTop_-60+mypanelTop);
			}
			if(status){
				// 触发即时分仓
				 var goodsId=getTableEditor(rowid,'goodsId').val();
				 var natureId=getTableEditor(rowid,'goodsSpecId').val();	
				 if(goodsId!=''){
					 branchWarehouse(goodsId,natureId)	 
				 }	
			}		
		},
		onCellSelect:function(rowid,iCol,cellcontent,e){
			if(iCol != 0 && _flag != "detail" && $("#goodsList #"+rowid).find(".editing-on").length <= 0){
				editNew(rowid);
			}
		},
		colModel:[
			{name:'action',label:'操作',width:80,align:"center",sortable:false,formatter:goodsListActionNew},
	        {name:'goodsId',label:'货品ID',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'regoodsId',label:'引用的单据货品ID',width:100,sortable:false,hidden:true,editable:true,edittype:"text"},
	        {name:'refDetailId',label:'引用的单据明细ID',width:100,sortable:false,hidden:true,editable:true,edittype:"text"},
	        {name:'goodsSpecId',label:'货品属性ID',width:100,sortable:false,hidden:true,editable:true,edittype:"text"},
	        {name:'unitId',label:'货品单位ID',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'transportUnitId',label:'运输单位ID',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'updateTime',label:'更新时间',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'lowestPrice',label:'最低销售价',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'goodsSpecGroupId',label:'属性组ID',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'unitGroupId',label:'单位组ID',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'inWareHouseId',label:'仓库ID',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'_inWareHouseName',label:'仓库名称(临时)',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'barCode',label:'条码',align:"center",sortable:false,width:100,editable:true,edittype:"text"},
	        {name:'_goodsCode',label:'编号(临时)',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'_goodsSpec',label:'规格(临时)',width:60,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'_goodsName',label:'名称(临时)',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'_unitName',label:'单位名称(临时)',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'_transportUnitName',label:'运输名称(临时)',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'_goodsSpecName',label:'属性(临时)',width:60,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'_isNew',label:'是否新增',width:100,hidden:true,sortable:false,editable:true,edittype:"text"},
	        {name:'goodsCode',label:'编号',align:"center",width:80,sortable:false,editable:true,edittype:"text",formatter:function(value,options,row){
	        	return row._goodsCode?row._goodsCode:value?value:"";
	        }},
	        {name:'goodsName',label:'名称',align:"center",width:240,sortable:false,formatter:function(value,options,row){
	        	return row._goodsName?row._goodsName:value?value:"";
	        }},
	        {name:'inWareHouseName',label:'仓库',align:"center",width:100,sortable:false,hidden:true,editable:true,edittype:"text",formatter:wareHouseAction},
	        {name:'goodsSpec',label:'规格',align:"center",width:60,sortable:false,formatter:function(value,options,row){
	        	return row._goodsSpec?row._goodsSpec:value?value:"";
	        }},
	        {name:'goodsSpecName',label:'属性',align:"center",width:60,sortable:false,editable:true,edittype:"text",formatter:function(value,options,row){
	        	return row._goodsSpecName?row._goodsSpecName:value?value:"";
	        }},
	        {name:'unitName',label:'单位',align:"center",width:100,sortable:false,editable:true,edittype:"text",formatter:unitActionNew},
	        {name:'scale',label:'单位换算关系',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'transportScale',label:'运输单位换算关系',align:"center",sortable:false,width:100,hidden:true,editable:true,edittype:"text"},
	        {name:'quentity',label:'数量',align:"center",width:60,sortable:false,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}} */},
	        {name:'receivedQuantity',label:'实收数量',align:"center",width:60,hidden:true,sortable:false,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}} */},
	        {name:'loseQuantity',label:'亏损数量',align:"center",width:60,hidden:true,sortable:false,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}} */},
	        {name:'transportUnitName',label:'运输单位',align:"center",width:100,hidden:true,sortable:false,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}} */},
	        {name:'transportQuentity',label:'运输数量',align:"center",width:60,hidden:true,sortable:false,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}} */},
	        {name:'transportPrice',label:'运输单价',align:"center",width:60,hidden:true,sortable:false,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}} */},
	        {name:'transportAmount',label:'运输金额',align:"center",width:60,hidden:true,sortable:false,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}} */},
	        {name:'deductionAmount',label:'扣费金额',align:"center",width:60,hidden:true,sortable:false,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{validType:'intOrFloat',width:'100%',height:28,required:true,novalidate:true,precision:2}} */},
	        {name:'unitPrice',label:'单价',align:"center",width:80,sortable:false,precision:4,editable:true,edittype:"text"/* editor:{type:'text',options:{min:0,precision:4,width:'100%',height:'100%'}} */,formatter:priceActionNew},
	        {name:'costPrice',label:'成本单价',align:"center",width:80,sortable:false,precision:4,hidden:true,editable:true,edittype:"text"/* editor:{type:'text',options:{min:0,precision:4,width:'100%',height:'100%'}} */,formatter:priceActionNew},	       
	        {name:'type',label:'金额',align:"center",width:80,sortable:false,formatter:typeActionNew,precision:2,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{precision:2,width:'100%',height:'100%'}} */},
	        {name:'taxRate',label:'税点%',align:"center",width:80,sortable:false,hidden:true,precision:4,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{min:0,precision:4,validType:'maxLength[200]',width:'100%',height:'100%'}} */},
	        {name:'taxAmount',label:'税金',align:"center",width:80,sortable:false,hidden:true,precision:2,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{min:0,precision:2,width:'100%',height:'100%'}} */},// 仅用于显示
	        {name:'totalAmount',label:'税后金额',align:"center",width:80,sortable:false,formatter:totalActionNew,hidden:true,precision:2,editable:true,edittype:"text"/* editor:{type:'numberbox',options:{min:0,precision:2,width:'100%',height:'100%'}} */},
	        
	        {name:'percentage',label:'提成点数',align:"center",width:80,sortable:false,formatter:totalActionNew,hidden:true,precision:2,editable:true,edittype:"text"},
	        {name:'percentageAmount',label:'提成金额',align:"center",width:80,sortable:false,formatter:totalActionNew,hidden:true,precision:2,editable:true,edittype:"text"},
	        
	        {name:'describe',label:'备注',align:"center",sortable:false,width:80,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({validType:'maxLength[200]',width:'100%',height:'100%'});
	        }}/* editor:{type:'textbox',options:{validType:'maxLength[200]',width:'100%',height:'100%'}} */},
	    ],
		/*
		 * onBeforeLoad:function(param){ var a = "" if(_billCode != "scjhd"){a =
		 * "合计："}
		 * $('#goodsList').datagrid('reloadFooter',[{goodsCode:a,action:'new',unitPrice:'new',costPrice:'new',type:0,totalAmount:0}]); },
		 */
	    loadComplete:function(data){
	    	scrollDiy();
	    	var a = ""
			if(_billCode != "scjhd"){a = "合计："}
	    	$("#goodsList").footerData('set',{goodsCode:a,action:'new',unitPrice:'new',costPrice:'new',quentity:0,receivedQuantity:0,loseQuantity:0,transportQuentity:0,transportAmount:0,deductionAmount:0});
	    	// 滚动条滑动到某位置元素固定
	   	 if(_billCode == 'scjhd'){
	   		 $('#hide').parent().append('<a href="javascript:;" id="hide2" onClick="aniTo(\'list1\');" style="display:none;position:absolute;right:150px;top:13px;" class="btn-ora-add">货品信息</a>')
	   		 var offset = $('#list1 #gbox_goodsList').offset();
	   		 var soffset = $('#dataBox').offset();
	   		 var $window = $('#dataBox').closest('.window-body');
	   		 $window.scroll(function () {
	   				var scrollTop = $(this).scrollTop();
	   				if (soffset.top <= (scrollTop+86)){
	   					$('#hide').fadeIn();
	   				}
	   				else{
	   					$('#hide').fadeOut();
	   				} 
	   				if (offset.top <= (scrollTop+56) && offset.top+$('#list1 #gbox_goodsList').outerHeight(true) > (scrollTop+26)){
	   					$('#list1 #gbox_goodsList .ui-jqgrid-bdiv').css('padding-top',31);
	   					$('#list1 #gbox_goodsList .ui-jqgrid-hdiv').css('margin-top',$('#title').outerHeight(true)+11);
	   					$('#list1 #gbox_goodsList .ui-jqgrid-hdiv').addClass('fixed');
	   				}else{
	   					$('#list1 #gbox_goodsList .ui-jqgrid-bdiv').css('padding-top',0);
	   					$('#list1 #gbox_goodsList .ui-jqgrid-hdiv').css('margin-top','0');
	   					$('#list1 #gbox_goodsList .ui-jqgrid-hdiv').removeClass('fixed');
	   				}
	   				if(offset.top+$('#list1 #gbox_goodsList').outerHeight(true) <= (scrollTop+20)){
   						$('#hide2').fadeIn();
   						$('#list3 #gbox_materialList .ui-jqgrid-bdiv').css('padding-top',31);
	   					$('#list3 #gbox_materialList .ui-jqgrid-hdiv').css('margin-top',$('#title').outerHeight(true)+11);
	   					$('#list3 #gbox_materialList .ui-jqgrid-hdiv').addClass('fixed');
   					}else{
   						$('#hide2').fadeOut();
   						$('#list3 #gbox_materialList .ui-jqgrid-bdiv').css('padding-top',0);
	   					$('#list3 #gbox_materialList .ui-jqgrid-hdiv').css('margin-top','0');
	   					$('#list3 #gbox_materialList .ui-jqgrid-hdiv').removeClass('fixed');
   					}
	   			});
	   	 }else{
	   			var soffset = $('#dataBox').offset();
	   			var $window = $('#dataBox').closest('.window-body');
	   			var offset = $('#list1 #gbox_goodsList').offset();
	   			$window.scroll(function () {
	   				var scrollTop = $(this).scrollTop();
	   				if (soffset.top <= (scrollTop+86)){
	   					$('#hide').fadeIn();
	   				}
	   				else{
	   					$('#hide').fadeOut();
	   				}
	   				if (offset.top <= (scrollTop+56)){
	   					$('#list1 #gbox_goodsList .ui-jqgrid-bdiv').css('padding-top',31);
	   					// $('#list1 #gbox_goodsList
						// .ui-jqgrid-hdiv').css('width',$('#dataBox').width()-25);
	   					$('#list1 #gbox_goodsList .ui-jqgrid-hdiv').css('margin-top',$('#title').outerHeight(true)+11);
	   					$('#list1 #gbox_goodsList .ui-jqgrid-hdiv').addClass('fixed');
	   				}
	   				else{
	   					$('#list1 #gbox_goodsList .ui-jqgrid-bdiv').css('padding-top',0);
	   					$('#list1 #gbox_goodsList .ui-jqgrid-hdiv').css('margin-top','0');
	   					$('#list1 #gbox_goodsList .ui-jqgrid-hdiv').removeClass('fixed');
	   				} 
	   			});
	   	 }
	    }
	});
	// 初始化货品表格
	if(details!=undefined){
		$('#goodsList')[0].addJSONData(details);
		getTotalNew();
		if(atips == 1){
			$('#gbox_goodsList .ui-jqgrid-sdiv').hide();
		}
		if(_billStat != 0){
			$('#addrow').hide();
		}
		newIndex = $('#goodsList').jqGrid('getRowData').length;
	}
	if(_flag == '' && localCache == "1"){
		var myCookies = localStorage[_billCode];
		if(myCookies!=null){
			var myobj = $.parseJSON(myCookies);
			var myform = $('#warehousebillForm');
			for(var i in myobj){
				if(myform.find('input[name='+i+']').parent().attr("class")=="dhxcombo_material" && myform.find('input[name='+i+']').prev().attr("class") && myform.find('input[name='+i+']').prev().attr("class").indexOf("div_grid")==-1){
					var index = myform.find('input[name='+i+']').closest(".dhxDiv")[0].comboObj.getIndexByValue(myobj[i]);// dhxCombo类型的下拉框特殊情况
					myform.find('input[name='+i+']').closest(".dhxDiv")[0].comboObj.selectOption(index);
				}else if(myform.find('input[name='+i+']').prev().attr("class") && myform.find('input[name='+i+']').prev().attr("class").indexOf("div_grid")!=-1){
					myform.find('input[name='+i+']').prev().val(myobj[i]);// dhxComboGrid类型的下拉框特殊情况
					myform.find('input[name='+i+']').prev()[0].lsKey = 1;
				}else{
					myform.find('input[name='+i+']').val(myobj[i]);
				}
				if(i.search(/Date/)!=-1 || i.search(/date/)!=-1 || i.search(/planStart/)!=-1){
					myform.find('input[comboname='+i+']').datebox("setText",myobj[i]);
				}
				/*
				 * if(_billCode == "bom" && i=="goodsCode"){
				 *  }
				 */
				if(_billCode == "bom" && i=="accountUnitName"){
					var _unitGroupId="";
					_unitGroupId=$("#unitGroupId").val();
					var mydata = "";
					if(_unitGroupId == ""){
						mydata=[];
					}else{
						$.ajax({
							url:getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,
							async:false,
							data:{},
							success:function(data){
								mydata = formatData(data,"fid");
							}
						});
					}
					myform.find('#accountUnitName').fool("dhxCombo",{
						data:mydata
					});
					myform.find('#accountUnitName').next()[0].comboObj.setComboValue(myobj["accountUnitId"]);
				}
				if(_billCode == "bom" && i=="specName"){
					var _goodSpecId="";
					var myspecData = [];
					_goodSpecId=$("#goodSpecGroupId").val();
					// var
					// url=getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6';
					if(!_goodSpecId){
						myform.find('#specName').fool('dhxComboGrid',{
		    				width:182,
			    			height:31,
			    			hasDownArrow:false,
		    				text:myobj[i],
		    			});
					}else{
		    			$.ajax({
		    			 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6',
		    				async:false,
		    		        data:{},
		    		        success:function(data){
		    		        	myspecData=formatData(data,"fid");
		    		        }
		    			});
		    			myform.find('#specName').fool('dhxComboGrid',{
		    				width:182,
			    			height:31,
			    			hasDownArrow:false,
		    				filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6',
		    				data:myspecData,
		    				text:myobj[i],
		    			});
					}
					myform.find('#specName').next()[0].comboObj.setComboText(myobj[i]);
				}
			}
			if("undefined" != typeof myobj["details"] && myobj["details"]!='[]'){
				var detailsObj = $.parseJSON(myobj.details);
				/*
				 * for(var j in detailsObj){ if(typeof detailsObj[j].unitName !=
				 * "undefined"){ var mydata = ''; var bool = false;
				 * $.post(getRootPath()+'/unitController/getChilds?unitGroupId='+detailsObj[j].unitGroupId,{},function(data){
				 * mydata = data; for(var k in mydata){//判断是否删除了单位
				 * if(detailsObj[j].unitName == mydata[k].name){ bool = true;
				 * continue; } } if(!bool){ //检测到单号已经被删除 detailsObj[j].unitName =
				 * ''; detailsObj[j].unitId = ''; detailsObj[j].scale = ''; }
				 * if(j == (detailsObj.length -1)){
				 * $('#goodsList').datagrid('loadData',detailsObj);
				 * getTotalNew(); } }); } }
				 */
				$('#goodsList')[0].addJSONData(detailsObj);
				getTotalNew();
			}
			newIndex = $('#goodsList').jqGrid('getRowData').length;
			if("undefined" != typeof myobj.relation && myobj.relation != '' && _billCode != "cgdd"){
				$('#supplierName').length>0?$('#supplierName').next()[0].comboObj.disable():null;
				$('#customerName').length>0?$('#customerName').next()[0].comboObj.disable():null;
			}
			/*
			 * var details = ''; "undefined" != typeof myobj["details"] &&
			 * myobj["details"]!='[]'?(details =
			 * $.parseJSON(myobj["details"]),$('#goodsList').datagrid('loadData',details),getTotalNew()):null;
			 */
		}
	}
	
	// 符合条件的单据界面，显示仓库列
	if(_billCode!='xsdd'&&_billCode!='cgxjd'&&_billCode!='cgdd'&&_billCode!='xsbjd'&&_billCode!='dcd'&&_billCode!='pdd'&&_billCode!='cgsqd'&&_billCode!='scjhd'&&_billCode!='bom'){
		$('#goodsList').jqGrid('showCol','inWareHouseName');
	}
	// 符合条件的单据界面显示，税前单价、税前金额、税点、税金、税后金额
	if(_billCode=='cgfp'||_billCode=='xsfp'){		
		$('#goodsList').jqGrid('showCol',['taxRate','taxAmount','totalAmount']);
		$('#goodsList').jqGrid('hideCol','inWareHouseName');
		// 采购发票和销售发票金额改为税前金额
		$('#goodsList').jqGrid("setLabel","type","税前金额");
	}
	if(_billCode=='xsth'){
		$('#goodsList').jqGrid('showCol','costPrice');
	}
	if(_billCode=='scjhd'){
		$('#goodsList').jqGrid('hideCol',['type','unitPrice']);
	}
	if(_billCode=='bom'){
		$('#goodsList').jqGrid('hideCol',['goodsSpec','unitPrice','type']);

	}
	if(_billCode == 'xsbjd' || _billCode == 'cgxjd'){
		$('#goodsList').jqGrid('hideCol',['quentity','type']);
	}
	if(_billCode == 'shd'|| _billCode=='fhd'){
		$('#goodsList').jqGrid('hideCol','inWareHouseName');
		$('#goodsList').jqGrid('hideCol','type');
		$('#goodsList').jqGrid('hideCol','unitPrice');
		$('#goodsList').jqGrid('showCol','receivedQuantity');
		$('#goodsList').jqGrid('showCol','loseQuantity');
		$('#goodsList').jqGrid('showCol','transportUnitName');
		$('#goodsList').jqGrid('showCol','transportQuentity');
		$('#goodsList').jqGrid('showCol','transportPrice');
		$('#goodsList').jqGrid('showCol','transportAmount');
		$('#goodsList').jqGrid('showCol','deductionAmount');
	}
	if( _billCode=='fhd'){
		$('#goodsList').jqGrid('hideCol','receivedQuantity');	
		$('#goodsList').jqGrid('hideCol','loseQuantity');
		$('#goodsList').jqGrid('hideCol','deductionAmount');
	}
	if( _billCode=='xsch'||_billCode=='xsth'){
		$('#goodsList').jqGrid('showCol','percentage');	
		$('#goodsList').jqGrid('showCol','percentageAmount');
	}
    if(_billCode=='bom')
        $('#goodsList').jqGrid("setGridWidth",$("#list1").width()*0.935);
    else
        $('#goodsList').jqGrid("setGridWidth",$("#list1").width()*0.99999999); // 让表格列变化后再一次自适应宽度 // 让表格列变化后再一次自适应宽度
	/*
	 * if(_billCode=='qckc'||_billCode=='xsdd'||_billCode=='xsch'||_billCode=='scll'||_billCode=='sctl'){
	 * $('#goodsList').datagrid('showColumn','scale'); }
	 */
	if(_flag!='detail')keyHandler();
}

// 详细页。货品列表删除按钮
function delNew(index,flag){
	$.fool.confirm({msg:'确定要删除选中的记录吗？',fn:function(r){
		if(r) {
			// var index_ = $(obj).fool('getRowIndex');
			if(flag=="1"){
				var quentity=getTableText(index,"quentity","1").text();
				$("#containerList").jqGrid('delRowData',index);
			}else{
				var quentity=getTableText(index,"quentity").text();
				subsidiaryMaterial(index,quentity,1);
				$("#goodsList").jqGrid('delRowData',index);
				getTotalNew();
			}
		}
	}});
}
function getTableText(index,name){
	var td = $('#goodsList').find("tr#"+index+" td[aria-describedby=goodsList_"+name+"]");
	return td;
}
/*
 * 同行获取text编辑器 obj必须是action下的元素
 */
function getTextEditor(obj,field){
	var textObj = $(obj).closest('tr');
	var editor = textObj.find('td[field="'+field+'"] div table input.datagrid-editable-input');
	return editor;
}

// 列表编辑按钮
function editContainerNew(ind){
	/*if(_billCode=="shd"){
		return false;
	}*/
	$("#containerList").jqGrid('editRow',ind);
	$('#containerList').jqGrid('setRowData', ind, {editing:true,action:null});// 编辑状态转换，按钮变化
}

// 详细页。货品列表编辑按钮
function editNew(ind){
	// var myEditnum=mynum;
	var mygoodsCode='',mygoodsSpecName='';
	var editKey = 1;
	var teKey=1;
	var goodsCode_ = getTableText(ind,"goodsCode").text();
	var goodsSpecName_ = getTableText(ind,"goodsSpecName").text();
	var unitName_ = getTableText(ind,"unitName").text();
	var _unitName_ = getTableText(ind,"_unitName").text();
	$("#goodsList").jqGrid('editRow',ind);
	$('#goodsList').jqGrid('setRowData', ind, {editing:true,action:null});// 编辑状态转换，按钮变化
	var $e={
			barCode:getTableEditor(ind,'barCode'),
			_goodsCode:getTableEditor(ind,'_goodsCode'),
			_goodsName:getTableEditor(ind,'_goodsName'),
			_goodsSpec:getTableEditor(ind,'_goodsSpec'),
			_goodsSpecName:getTableEditor(ind,'_goodsSpecName'),
			_unitName:getTableEditor(ind,'_unitName'),
			_transportUnitName:getTableEditor(ind,'_transportUnitName'),
			_isNew:getTableEditor(ind,'_isNew'),
			transportUnitId:getTableEditor(ind,'transportUnitId'),
			quentity:getTableEditor(ind,'quentity'),
			taxRate:getTableEditor(ind,'taxRate'),
			unitPrice:getTableEditor(ind,'unitPrice'),
			type:getTableEditor(ind,'type'),
			taxAmount:getTableEditor(ind,'taxAmount'),
			totalAmount:getTableEditor(ind,'totalAmount'),
			describe:getTableEditor(ind,'describe'),
			costPrice:getTableEditor(ind,'costPrice'),
			unitGroupId:getTableEditor(ind,'unitGroupId'),
			unitName:getTableEditor(ind,'unitName'),
			unitId:getTableEditor(ind,"unitId"),
			goodsSpecGroupId:getTableEditor(ind,'goodsSpecGroupId'),
			goodsSpecName:getTableEditor(ind,'goodsSpecName'),
			goodsSpecId:getTableEditor(ind, 'goodsSpecId'),
			_goodsSpecName:getTableEditor(ind,'_goodsSpecName'),
			goodsId:getTableEditor(ind,'goodsId'),
			inWareHouseId:getTableEditor(ind,'inWareHouseId'),
			inWareHouseName:getTableEditor(ind,'inWareHouseName'),
			_inWareHouseName:getTableEditor(ind,'_inWareHouseName'),
			goodsCode:getTableEditor(ind,'goodsCode'),
			goodsName:getTableEditor(ind,'goodsName'),
			goodsSpec:getTableEditor(ind,'goodsSpec'),
			regoodsId:getTableEditor(ind,'regoodsId'),
			refDetailId:getTableEditor(ind,'refDetailId'),
			scale:getTableEditor(ind,'scale'),
			updateTime:getTableEditor(ind,'updateTime'),
			
			transportScale:getTableEditor(ind,'transportScale'),
			receivedQuantity:getTableEditor(ind,'receivedQuantity'),
			loseQuantity:getTableEditor(ind,'loseQuantity'),
			transportUnitName:getTableEditor(ind,'transportUnitName'),
			transportQuentity:getTableEditor(ind,'transportQuentity'),
			transportPrice:getTableEditor(ind,'transportPrice'),
			transportAmount:getTableEditor(ind,'transportAmount'),
			deductionAmount:getTableEditor(ind,'deductionAmount'),
			
			percentage:getTableEditor(ind,'percentage'),
			percentageAmount:getTableEditor(ind,'percentageAmount'),
	};
	
	$e._goodsCode.val(goodsCode_);
	$e._goodsSpecName.val(goodsSpecName_);
	if(unitName_){
		$e._unitName.val(unitName_);
	}else{
		$e._unitName.val(_unitName_);
	}
	mydetails!='' && !$e._isNew.val()?(mygoodsCode = mydetails[ind-1].goodsCode,mygoodsSpecName = mydetails[ind-1].goodsSpecName):null;
	var _billType=_billCode;
	var _customerId = $("#customerId").val();
	var _supplierId = $("#supplierId").val(); 
	$e.barCode.textbox({
		width:"100%",
		height:"100%"
	});
	$e.barCode.textbox("textbox").bind("keydown",function(e){
		if(e.keyCode == 13){
			var barCode = $(this).val();
			$.post(getRootPath()+"/basedata/goodsBar/queryByBar",{barCode:barCode},function(data){
				// bom表引用条码时控制物料不能与主货品相同。
				if((_billCode=="bom"&&data.goodsId==$("#goodsId").val())&&$("#goodsId").val()!=""){
					$e.barCode.textbox("clear");
					$.fool.alert({msg:"所选物料不能与主货品相同。",fn:function(){
						$e.barCode.textbox("textbox").focus();
						$e.barCode.textbox("textbox").click();
					}});
					return;
				}
				 var _$e = $e;
		    	 var editor$ = _$e.goodsCode;
		    	 // var obj = myobj[myEditnum].find('a.btn-save');
			     
			     // var selectTips=0;
		    	 barCodeKey = 1; // 标识为通过barCode获取商品信息
			     _$e.goodsId.val(data.goodsId);
			     _$e.quentity.numberbox('setValue',0);
		    	 getTableEditor(ind,"taxRate").parent().css('display')!="none"?
		    			 _$e.taxRate.numberbox('setValue',0):null;
		    			 _$e.describe.textbox('setValue','');
		    	 var regoodsId = _$e.regoodsId.val();
		    	 var refDetailId = _$e.refDetailId.val();
		    	 if(refDetailId != '' && data.fid != regoodsId){
		    		 _$e.refDetailId.val('');
		    	 }
		    	 _$e._goodsName.val(data.goodsName);
		    	 _$e._goodsCode.val(data.goodsCode);
		    	 _$e._goodsSpec.val(data.goodsSpec);
				 getTableText(ind,"goodsSpec").text(data.goodsSpec);
				 _$e.goodsSpecGroupId.val(data.goodsSpecGroupId);
				 _$e.scale.val(data.unitScale);
				 _$e.transportScale.val(data.transportScale);
				 _$e.updateTime.val(data.updateTime);
				 _$e.receivedQuantity.numberbox("setValue",0);
				 _$e.loseQuantity.numberbox("setValue",0);
				 _$e.transportQuentity.numberbox("setValue",0);
				 _$e.transportPrice.numberbox("setValue",0);
				 _$e.transportAmount.numberbox("setValue",0);
				 _$e.deductionAmount.numberbox("setValue",0);
				 getTableText(ind,"goodsName").text(data.goodsName);
		    	 // editor$.next()[0].comboObj.setComboValue(data.goodsId);
		    	 editor$.next()[0].comboObj.setComboText(data.goodsCode?data.goodsCode:"");
		    	 var goodsId=_$e.goodsId.val();
		    	 branchWarehouse(goodsId,natureId);
		    	 if(data.unitGroupId){
		    		 _$e.unitGroupId.val(data.unitGroupId);
		    		 _$e._unitName.val(data.unitName);
		    		 var _unitGroupId = data.unitGroupId;
		    		 $.ajax({
		    			 	url:getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,
		    				async:false,
		    		        data:{},
		    		        success:function(data){
		    		        	unitData=formatData(data,"fid");
		    		        }
		    			});
		    		 _$e.unitName.fool("dhxCombo",{
		    			 width:"100%",
		    			 height:"100%",
		    			 data:unitData,
		    			 validType:['unitValid['+ind+']','nounitValid['+ind+']'],
		    			 required:true,
		    			 novalidate:true,
		    			 onLoadSuccess:function(combo){
		    			    	var _d = unitData;
		    			    	if(_d)unitData_[ind] = _d;
		    			    	var _uid = data.unitId;
		    			    	combo.setComboValue(_uid);
		    			    	_$e.unitId.val(_uid);
		    			    	_$e.unitName.val(_uid);
		    			    	if(_billCode!="bom"){
		    			    		changeGoodPrice(ind);
		    			    	}
		    			    }});
		    			/*
						 * _$e.unitName.combobox('textbox').bind('blur',function(){
						 * var obj = myobj[myEditnum].find('a.btn-save'); var
						 * ind = obj.fool('getRowIndex'); var _text =
						 * $(this).val(); if(!_text)return; var _fid=''
						 * ,_name=''; for(var i in unitData_[ind]){
						 * if(_text==unitData_[ind][i].name){ _fid =
						 * unitData_[ind][i].fid; _name =
						 * unitData_[ind][i].name; break; } }
						 * _$e.unitId.val(_fid); _$e.unitName.val(_name);
						 * _$e._unitName.val(_name);
						 * _$e.unitName.combobox('validate'); });
						 */
		    		 /*
						 * $(_$e.unitName.next()[0].comboObj.getInput()).focus(function(){
						 * var $input = $(this);
						 * setTimeout(function(){$input.select();},100); });
						 */
		    			_$e.unitName.next()[0].comboObj.setComboValue(data.unitId);
		    			// _$e.unitName.next()[0].comboObj.setComboText(data.unitName);
		    	 }else{
		    		 _$e.unitName.next()[0].comboObj.setComboValue('');
		    		 _$e.unitName.next()[0].comboObj.setComboText('');
		    	 }
		    		var mygoodSpecId = data.goodsSpecGroupId;
		    		_$e.goodsSpecName.next()[0].comboObj.setComboText('');
		    		_$e._goodsSpecName.val('');
		    		_$e.goodsSpecId.val('');
		    		if(mygoodSpecId!=undefined&&mygoodSpecId.trim().length!=0){
		    			// 货品属性
		    			var myspecData = "";
		    			$.ajax({
		    			 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
		    				async:false,
		    		        data:{},
		    		        success:function(data){
		    		        	myspecData=formatData(data,"fid");
		    		        }
		    			});
		    			_$e.goodsSpecName.fool('dhxComboGrid',{
		    				width:"100%",
		    				height:"100%",
		    				hasDownArrow:false,
		    				validType:['specValid['+ind+']','nospecValid['+ind+']'],
		    				required:true,
		    				novalidate:true,
		    				filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
		    				data:myspecData,
		    				/*
							 * width:'100%', height:"90%", required:true,
							 * novalidate:true,
							 * validType:['specValid['+myEditnum+']','nospecValid['+myEditnum+']'],
							 * focusShow:true, onlySelect:true, columns:[[
							 * {field:'fid',title:'fid',hidden:true},
							 * {field:'name',title:'名称',width:100,searchKey:false},
							 * {field:'code',title:'编码',width:30,searchKey:false},
							 * {field:'searchKey',hidden:true,searchKey:true},
							 * ]],
							 */
		    			});
		    			var specCombo = _$e.goodsSpecName.next()[0].comboObj;
		    			specCombo.setComboText('');
		    			"undefined"!=typeof $(specCombo.getInput()).attr('disabled')&&$(specCombo.getInput()).attr('disabled')=='disabled'?specCombo.enable():null/* setTimeout(function(){_$e.goodsSpecName.combogrid('panel').css("display")=="block"?_$e.goodsSpecName.combogrid('hidePanel'):null;},500) */;
		    			/*
						 * _$e.goodsSpecName.combogrid('textbox').focus(function(){
						 * if(selectTips==0){//防止切换货品立即focus属性下拉框会消失的问题
						 * setTimeout(function(){_$e.goodsSpecName.combogrid('showPanel');selectTips=1;},500);
						 * 
						 * }else{ _$e.goodsSpecName.combogrid('showPanel'); }
						 * 
						 * });
						 */
		    			data.goodsSpecId?specCombo.setComboValue(data.goodsSpecId):null;
		    			data.goodsSpecId?specCombo.setComboText(data.goodsSpecName):null;
		    			getTableEditor(ind,'goodsSpecId').val(data.goodsSpecId);
		    			getTableEditor(ind,'_goodsSpecName').val(data.goodsSpecName);
		    			$(specCombo.getInput()).validatebox("validate");
		    		}else{
		    			$(_$e.goodsSpecName.next()[0].comboObj.getInput()).validatebox('disableValidation');
		    			_$e.goodsSpecName.next()[0].comboObj.disable();
		    			_$e.goodsSpecName.val("");
		    		}
		    		if(_$e.inWareHouseName.next()[0].comboObj.getComboText()!=""){
	    				_$e.quentity.numberbox("textbox").focus();
	    			}else{
	    				$(_$e.inWareHouseName.next()[0].comboObj.getInput()).focus();
	    			}		    		
			});
		}
	});
	// 货品数量
	var _stat = $("#recordStatus").val();// 状态
	
	$e.quentity.attr('_class','quentity');// siblings().children('.textbox-text').
	if(_billCode == 'pdd'){
		$e.quentity.numberbox({
			height:'100%',width:'100%',
			validType:null,
			required:true,
			validType:['numMaxLength[10]','notZero'],
			precision:2,
			novalidate:true,
			formatter:function(value){
				//如果数字过长会转为科学计数法，置为空
				var t = value.indexOf("e+");
				if(t!=-1){
					return "";
				}else{
					return value;
				}
			}
		});
	}else{
		$e.quentity.numberbox({
			validType:['intOrFloat','numMaxLength[10]'],
			width:'100%',
			height:'100%',
			required:true,
			novalidate:true,
			precision:2,
			formatter:function(value){
				//如果数字过长会转为科学计数法，置为空
				var t = value.indexOf("e+");
				if(t!=-1){
					return "";
				}else{
					return value;
				}
			}
		});
	}
	
	numeration($e.quentity);
	$e.quentity.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	$e.quentity.numberbox('textbox').keydown(function(e){
		if(e.keyCode == 18){
			$(this).blur();// 解决IE下按alt键导致内容清空的问题
		}
	});
	
	// 实收数量
	$e.receivedQuantity.numberbox({
		height:'100%',width:'100%',
		required:true, 
		novalidate:true,
		validType:['intOrFloat','numMaxLength[10]'],
		precision:2,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		},
		onChange:function(nv,ov){
			var value=$e.quentity.numberbox("getValue")-nv;
			$e.loseQuantity.numberbox("setValue",value);
		}
	});
	$e.receivedQuantity.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	$e.receivedQuantity.numberbox('textbox').keydown(function(e){
		if(e.keyCode == 18){
			$(this).blur();// 解决IE下按alt键导致内容清空的问题
		}
	});
	
	// 运输数量
	$e.transportQuentity.numberbox({
		height:'100%',width:'100%',
		required:true,
		validType:['intOrFloat','numMaxLength[10]'],
		precision:2,
		novalidate:true,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		},
		onChange:function(nv,ov){
			var value=$e.transportPrice.numberbox("getValue") * nv;
			$e.transportAmount.numberbox("setValue",value);
		}
	});
	$e.transportQuentity.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	$e.transportQuentity.numberbox('textbox').keydown(function(e){
		if(e.keyCode == 18){
			$(this).blur();// 解决IE下按alt键导致内容清空的问题
		}
	});
	
	// 亏损数量
	$e.loseQuantity.numberbox({
		height:'100%',width:'100%',
		required:true,
		validType:['numMaxLength[10]'],
		precision:2,
		novalidate:true,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		}
		/*onChange:function(nv,ov){
			var value=$e.quentity.numberbox("getValue")-nv;
			$e.receivedQuantity.numberbox("setValue",value);
		}*/
	});
	$e.loseQuantity.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	$e.loseQuantity.numberbox('textbox').keydown(function(e){
		if(e.keyCode == 18){
			$(this).blur();// 解决IE下按alt键导致内容清空的问题
		}
	});
	
	// 货品单价
	$e.unitPrice.attr('_class','unitPrice');
	if(_billCode == 'xsbjd' || _billCode == 'xsdd' || _billCode == 'xsch'){
		$e.unitPrice.numberbox({
			height:'100%',width:'100%',
			validType:['goodPrice[\''+ind+'\']','numMaxLength[10]'],required:true,precision:4,
			novalidate:true,
			formatter:function(value){
				//如果数字过长会转为科学计数法，置为空
				var t = value.indexOf("e+");
				if(t!=-1){
					return "";
				}else{
					return value;
				}
			}
		});
	}else{
		$e.unitPrice.numberbox({
			height:'100%',width:'100%',
			required:true,precision:4,
			validType:'numMaxLength[10]',
			novalidate:true,
			formatter:function(value){
				//如果数字过长会转为科学计数法，置为空
				var t = value.indexOf("e+");
				if(t!=-1){
					return "";
				}else{
					return value;
				}
			}
		});
	}
	numeration($e.unitPrice);
	$e.unitPrice.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	
	// 运输单价
	/*$e.transportPrice.attr('_class','unitPrice');*/
	$e.transportPrice.numberbox({
		height:'100%',width:'100%',
		required:true,precision:4,
		validType:'numMaxLength[10]',
		novalidate:true,
		onChange:function(nv,ov){
			var value=$e.transportQuentity.numberbox("getValue") * nv;
			$e.transportAmount.numberbox("setValue",value);
		}
	});
	/* numeration($e.unitPrice); */
	$e.transportPrice.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	
	// 金额
	$e.type.attr('_class','type');
	$e.type.numberbox({
		height:'100%',width:'100%',
		required:true,precision:2,
		validType:'numMaxLength[10]',
		novalidate:true,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		},
		onChange:function(newValue,oldValue){
			var percentage=$e.percentage.numberbox("getValue");
			if(percentage){
				$e.percentageAmount.numberbox("setValue",(newValue*parseFloat(percentage)/100).toFixed(2));
			}
		}
	});
	numeration($e.type);
	$e.type.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	
	// 运输金额
	/*$e.transportAmount.attr('_class','type');*/
	$e.transportAmount.numberbox({
		height:'100%',width:'100%',
		required:true,precision:2,
		validType:'numMaxLength[10]',
		novalidate:true,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		},
		onChange:function(nv,ov){
			var value=nv/$e.transportQuentity.numberbox("getValue");
			$e.transportPrice.numberbox("setValue",value);
		}
	});
	/* numeration($e.type); */
	$e.transportAmount.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	
	// 扣费金额
	/*$e.deductionAmount.attr('_class','type');*/
	$e.deductionAmount.numberbox({
		height:'100%',width:'100%',
		required:true,precision:2,
		validType:'numMaxLength[10]',
		novalidate:true,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		}
	});
	/* numeration($e.type); */
	$e.deductionAmount.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	
	// 税点，税金，税后金额
	if(_billCode=="cgfp"||_billCode=="xsfp"){
		$e.taxRate.attr('_class','taxRate');
		$e.taxRate.numberbox({
			height:'100%',width:'100%',
			required:true,precision:4,
			validType:'numMaxLength[10]',
			novalidate:true,
			formatter:function(value){
				//如果数字过长会转为科学计数法，置为空
				var t = value.indexOf("e+");
				if(t!=-1){
					return "";
				}else{
					return value;
				}
			}
		});
		numeration($e.taxRate);
		$e.taxRate.numberbox('textbox').focus(function(e){
			$(this).select();
		});
	// 税金值改变事件
		$e.taxAmount.attr('_class','taxAmount');
		$e.taxAmount.numberbox({
			height:'100%',width:'100%',
			required:true,precision:2,
			validType:'numMaxLength[10]',
			novalidate:true,
			formatter:function(value){
				//如果数字过长会转为科学计数法，置为空
				var t = value.indexOf("e+");
				if(t!=-1){
					return "";
				}else{
					return value;
				}
			}
		});
		numeration($e.taxAmount);
		$e.taxRate.numberbox('textbox').focus(function(e){
			$(this).select();
		});
		// 税后金额改变事件
		$e.totalAmount.attr('_class','totalAmount');
		$e.totalAmount.numberbox({
			height:'100%',width:'100%',
			required:true,precision:2,
			validType:'numMaxLength[10]',
			novalidate:true,
			formatter:function(value){
				//如果数字过长会转为科学计数法，置为空
				var t = value.indexOf("e+");
				if(t!=-1){
					return "";
				}else{
					return value;
				}
			}
		});
		numeration($e.totalAmount);
		$e.totalAmount.numberbox('textbox').focus(function(e){
			$(this).select();
		});
			$e.taxAmount.numberbox('disable');
	};
	
	
	// 单价快捷键操作
	$e.unitPrice.numberbox('textbox').keydown(function(e){
		/*
		 * if(e.keyCode == 38){ //var obj = myobj[myEditnum].find('a.btn-save');
		 * var eqInd =
		 * $e.unitPrice.closest("tr.jqgrow").find("td").index($e.unitPrice.parent())-1;
		 * //var index = obj.fool('getRowIndex') - 1; if(eqInd == 0){ return
		 * false; } var index =
		 * $e.unitPrice.closest("tr.jqgrow").eq(eqInd).attr("id");
		 * if($('.btn-index-save-'+index).length <= 0){ editNew(index); } }else
		 * if(e.keyCode == 40){ var eqInd =
		 * $e.unitPrice.closest("tr.jqgrow").find("td").index($e.unitPrice.parent())+1;
		 * var max = $('#goodsList').jqGrid('getRowData').length; if(eqInd >
		 * max){ return false; } var index =
		 * $e.unitPrice.closest("tr.jqgrow").eq(eqInd).attr("id");
		 * if($('.btn-index-save-'+index).length <= 0){ editNew(index); } }else
		 */if(e.keyCode == 18){
			$(this).blur();// 解决IE下按alt键导致内容清空的问题
		}
	});
	// 备注快捷键操作
	$e.describe.textbox('textbox').keydown(function(e){
		/*
		 * if(e.keyCode == 38){ var eqInd =
		 * $e.describe.closest("tr.jqgrow").find("td").index($e.describe.parent())-1;
		 * if(eqInd == -1){ return false; } var index =
		 * $e.describe.closest("tr.jqgrow").eq(eqInd).attr("id");
		 * if($('.btn-index-save-'+index).length <= 0){ editNew(index); } }else
		 * if(e.keyCode == 40){ var eqInd =
		 * $e.describe.closest("tr.jqgrow").find("td").index($e.describe.parent())+1;
		 * var max = $('#goodsList').jqGrid('getRowData').length; if(eqInd >
		 * max){ return false; } var index =
		 * $e.describe.closest("tr.jqgrow").eq(eqInd).attr("id");
		 * if($('.btn-index-save-'+index).length <= 0){ editNew(index); } }else
		 */ if(e.keyCode == 13){
			// var eqInd =
			// $e.describe.closest("tr.jqgrow").find("td").index($e.describe.parent());
			var nextl = $e.describe.closest("tr.jqgrow").next().length;
			var v = mysave(ind);
			var max = $('#goodsList').jqGrid('getRowData').length;
			(nextl==0) && v!=false && _billStat == ""?
					($('#addrow').click()):null;
		}else if(e.keyCode == 18){
			$(this).blur();// 解决IE下按alt键导致内容清空的问题
		}
	});
	
	// 货品成本单价
	$e.costPrice.numberbox({
		height:'100%',width:'100%',
		required:true,precision:4,
		validType:'numMaxLength[10]',
		novalidate:true,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		}
	});
	$e.costPrice.numberbox('textbox').focus(function(e){
		$(this).select();
	});
	$e.costPrice.numberbox('textbox').keydown(function(e){
		if(e.keyCode == 18){
			$(this).blur();// 解决IE下按alt键导致内容清空的问题
		}
	});
	
	// 货品单位
		var _unitGroupId = $e.unitGroupId.val();
		$.post(getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,{},function(data){
			myunitData[ind] = formatData(data,"fid");
			$e.unitName.fool('dhxCombo',{
				required:true,
				novalidate:true,
				validType:['unitValid['+ind+']','nounitValid['+ind+']'],
				data:myunitData[ind],
				setTemplate:{
					input:"#name#",
					option:'#name#'
				},
			    width:'100%',
			    height:"100%",
			    focusShow:true,
			    onLoadSuccess:function(combo){
			    	var _d = myunitData[ind];
			    	if(_d) unitData_[ind] = _d;
			    	var _uid = getTableEditor(ind,'unitId').val();
			    	if(_uid != ""){
			    		combo.setComboText("");// 修复单据未保存的时候，删除未被使用的单位时再编辑出现显示fid的问题
				    	for(var i in _d){
				    		if(_d[i].text.fid == _uid){
				    			combo.setComboValue(_uid);
				    		}
				    	}
			    	}
			    },
			    onSelectionChange:function(){
			    	var myUnitName = $e.unitName;
			    	// 每次切换选项都选中
			    	var chol = $(myUnitName.next()[0].comboObj.getList()).find(".dhxcombo_option_selected");
			    	var mindex = chol.index();
			    	myUnitName.next()[0].comboObj.selectOption(mindex);
			    	myUnitName.next()[0].comboObj.openSelect();
			    },
			    onChange:function(value,text){
			    	var myUnitName = $e.unitName;
			    	var myUnitId = $e.unitId;
			    	var _myUnitName = $e._unitName;
			    	var record = myUnitName.next()[0].comboObj.getSelectedText();
			    	myUnitId.val(record.fid);
			    	_myUnitName.val(record.name);
			    	if(_billCode!="bom" && editKey != 1){
			    		changeGoodPrice(ind);
			    	}			    	
			    	$(myUnitName.next()[0].comboObj.getInput()).validatebox("validate");
			    	editKey = 0;
			    	/*
					 * setTimeout(function(){ var chol =
					 * $(myUnitName.next()[0].comboObj.getList()).find(".dhxcombo_option_selected").length;
					 * chol!=0?$(myUnitName.next()[0].comboObj.getInput()).focus():null;
					 * },300);
					 */
			    	$.fool._formValidHelp($(myUnitName.next()[0].comboObj.getInput()));
	    			
			    }			
			});
			/*
			 * $e.unitName.combobox('textbox').bind('blur',function(){ var obj =
			 * myobj[myEditnum].find('a.btn-save'); var ind =
			 * obj.fool('getRowIndex'); var myUnitName = $e.unitName; var
			 * myUnitId = $e.unitId; var _myUnitName = $e._unitName; var _text =
			 * $(this).val(); if(!_text)return; var _fid='' ,_name=''; for(var i
			 * in unitData_[ind]){ if(_text==unitData_[ind][i].name){ _fid =
			 * unitData_[ind][i].fid; _name = unitData_[ind][i].name; break; } }
			 * myUnitId.val(_fid); myUnitName.val(_name);
			 * _myUnitName.val(_name); myUnitName.combobox('validate'); });
			 */
			$($e.unitName.next()[0].comboObj.getInput()).focus(function(){
				var $input = $(this);
				$input.select();
				$("#goodsList").jqGrid("setSelection",ind);
			});
			unitSearch($e.unitName.next()[0].comboObj);
		});
		
	// 运输单位
		var goodsId = $e.goodsId.val();
		var specId=$e.goodsSpecId.val();
		$.get(getRootPath()+'/api/goodsTransport/queryTransportUnit',{goodsId:goodsId,specId:specId,shipmentTypeId:$("#shipmentTypeId").val()},function(data){
			var transUnitData = formatData(data,"fid");
			$e.transportUnitName.fool('dhxCombo',{
				required:true,
				novalidate:true,
				validType:['transporValue['+ind+']'], 
				data:transUnitData,
				setTemplate:{
					input:"#name#",
					option:'#name#'
				},
			    width:'100%',
			    height:"100%",
			    focusShow:true,
			    onLoadSuccess:function(combo){
			    	var _uid = getTableEditor(ind,'transportUnitId').val();
			    	combo.setComboValue(_uid);
			    },
			    onSelectionChange:function(){
			    	var myUnitName = $e.transportUnitName;
			    	// 每次切换选项都选中
			    	var chol = $(myUnitName.next()[0].comboObj.getList()).find(".dhxcombo_option_selected");
			    	var mindex = chol.index();
			    	myUnitName.next()[0].comboObj.selectOption(mindex);
			    	myUnitName.next()[0].comboObj.openSelect();
			    },
			    onChange:function(value,text){ 
			    	var transUnitName = $e.transportUnitName;
			    	var transUnitId = $e.transportUnitId;
			    	var _transUnitName = $e._transportUnitName;
			    	var transportScale=$e.transportScale;
			    	var record = transUnitName.next()[0].comboObj.getSelectedText();
			    	transUnitId.val(record.fid);
			    	_transUnitName.val(record.name); 
			    	transportScale.val(record.scale);
			    	$(transUnitName.next()[0].comboObj.getInput()).validatebox("validate");
			    	if(_billCode=="fhd" && tekey != 1){//发货单
			    		demandAmount(value,ind);
			    	}
			    	tekey = 0;
			    }			
			});			
			$($e.transportUnitName.next()[0].comboObj.getInput()).focus(function(){
				var $input = $(this);
				$input.select();
				$("#goodsList").jqGrid("setSelection",ind);
			});
			var transportUnitId = $e.transportUnitId.val();
			/*if(transportUnitId==''||transportUnitId==null||transportUnitId==undefined){//查看运输单位不存在不允许编辑
				$e.transportUnitName.next()[0].comboObj.disable();	
			}*/
			/*if(goodsId!=undefined&&goodsId.trim().length!=0){//货品存在打开运输单位的编辑状态
				$e.transportUnitName.next()[0].comboObj.enable();	
			}else{
				$e.transportUnitName.next()[0].comboObj.disable();
			}*/
		});	
		
	// 货品属性
	var _goodSpecId = $e.goodsSpecGroupId.val();
	var specData = "";
	$.ajax({
	 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6',
		async:false,
        data:{},
        success:function(data){
        	specData=formatData(data,"fid");
        }
	});
		$e.goodsSpecName.fool('dhxComboGrid',{
			filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6',
			width:'100%',
			height:"100%",
			required:true,
			novalidate:true,
			hasDownArrow:false,
			focusShow:true,
			onlySelect:true,
			data:specData,
			validType:['specValid['+ind+']','nospecValid['+ind+']'],
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
			},
		    onChange:function(value,text){
		    	// var obj = myobj[myEditnum].find('a.btn-save');
		    	var mygoodsSpecId = $e.goodsSpecId; 
		    	var _mygoodsSpecName = $e._goodsSpecName;
		    	var mygoodsSpecName = $e.goodsSpecName;
		    	var transportUnitName=$e.transportUnitName;
		    	var row = mygoodsSpecName.next()[0].comboObj.getSelectedText();
		    	_mygoodsSpecName.val(row.name); 
		    	mygoodsSpecId.val(row.fid);
		    	if(_billCode!="bom"){
		    		changeGoodPrice(ind);
		    	}
		    	// mygoodsSpecName.combogrid('setText',row.name);
	            // natureId=row.fid;
		    	var goodsId=$e.goodsId.val();
		    	var natureId=$e.goodsSpecId.val();	
		    	if(goodsId!=''){
		    		branchWarehouse(goodsId,natureId);
		    	}
		    	$.fool._formValidHelp($(mygoodsSpecName.next()[0].comboObj.getInput()));
		    	$.get(getRootPath()+'/api/goodsTransport/queryTransportUnit',{goodsId:goodsId,specId:row.fid},function(data){
		    		 var transUnit = formatData(data,"fid");
	    			 transportUnitName.next()[0].comboObj.clearAll();
	    			 transportUnitName.next()[0].comboObj.addOption(transUnit);
	    			 if(data.length != 0){//选择货品属性存在运输单位就允许编辑
	    				 $e.transportUnitName.next()[0].comboObj.selectOption(0);
	    				 /*$e.transportUnitName.next()[0].comboObj.disable();	*/
	    			 }else{
	    				 $e.transportUnitName.next()[0].comboObj.setComboValue("");
	    				 $e.transportUnitName.next()[0].comboObj.setComboText("");
	    				 $e.transportUnitName.next()[0].comboObj.enable();
	    			 }
		    	});
		    }
		});
		mydetails!='' && !$e._isNew.val()?$e.goodsSpecName.next()[0].comboObj.setComboText(mygoodsSpecName):$e.goodsSpecName.next()[0].comboObj.setComboText($e._goodsSpecName.val());
		if(_goodSpecId!=undefined&&_goodSpecId.trim().length!=0){
			
		}else{
			$e.goodsSpecName.next()[0].comboObj.disable();
		}
	
	// 仓库
	var _inWareHouseId = $e.inWareHouseId.val()?$e.inWareHouseId.val():$("#inWareHouseId").val();
	
	$e.inWareHouseName.fool('dhxCombo',{
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
	    /*
		 * keyHandler:{ up: function(){ var ed=$(this); var
		 * tree=ed.combotree("tree"); var selected=tree.tree("getSelected");
		 * if(selected){ var parent=tree.tree("getParent",selected.target);
		 * if(!parent){ var childs=tree.tree("getChildren");
		 * getLast(childs,ed,tree); return; } var childs=parent.children;
		 * if(childs[0].id==selected.id){ ed.combotree("setValue",parent.id);
		 * tree.tree("scrollTo",parent.target); $(parent.target).click(); }else{
		 * for(var i=0;i<childs.length;i++){ if(childs[i].id==selected.id){
		 * if(childs[i-1].children.length>0){
		 * getLast(childs[i-1].children,ed,tree); }else{
		 * ed.combotree("setValue",childs[i-1].id);
		 * tree.tree("scrollTo",childs[i-1].target);
		 * $(childs[i-1].target).click(); } break; } } } }else{ var
		 * childs=tree.tree("getChildren"); getLast(childs,ed,tree); } }, down:
		 * function(){ var ed=$(this); var tree=ed.combotree("tree"); var
		 * selected=tree.tree("getSelected"); if(selected){
		 * if(selected.children.length>0){
		 * ed.combotree("setValue",(selected.children)[0].id);
		 * tree.tree("scrollTo",(selected.children)[0].target);
		 * $((selected.children)[0].target).click(); }else{
		 * nextNodeSearcher(selected,tree,ed); } }else{ var
		 * root=tree.tree("getRoot");
		 * ed.combotree("setValue",root.children[0].id);
		 * tree.tree("scrollTo",root.children[0].target);
		 * $(root.children[0].target).click(); } }, left: function(){
		 *  }, right: function(){
		 *  }, enter: function(){
		 *  }, },
		 */
	    onChange:function(value,text){
	    	var myinWareHouseName = $e.inWareHouseName;
	    	var myinWareHouseId = $e.inWareHouseId;
	    	var _myinWareHouseName = $e._inWareHouseName;
	    	var node = myinWareHouseName.next()[0].comboObj.getSelectedText();
				myinWareHouseId.val(node.fid);
				_myinWareHouseName.val(node.name);
	    }
/*
 * onLoadSuccess:function(combo){
 * if(undefined!=_inWareHouseId&&_inWareHouseId.length!=0){
 * combo.setComboValue(_inWareHouseId);
 * getTableEditor(ind,'inWareHouseId').val(_inWareHouseId); //
 * if(undefined!=node&&null!=node)
 * getTableEditor(ind,'_inWareHouseName').val(combo.getComboText()); } }
 */
	});
	
	$e.percentage.numberbox({
		height:'100%',width:'100%',
		required:true,precision:2,
		validType:'numMaxLength[10]',
		novalidate:true,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		}
	});
	$e.percentageAmount.numberbox({
		height:'100%',width:'100%',
		required:true,precision:2,
		validType:'numMaxLength[10]',
		novalidate:true,
		formatter:function(value){
			//如果数字过长会转为科学计数法，置为空
			var t = value.indexOf("e+");
			if(t!=-1){
				return "";
			}else{
				return value;
			}
		},
		onChange:function(newValue,oldValue){
			var percentage=$e.percentage.numberbox("getValue");
			if(percentage){
				$e.type.numberbox("setValue",(newValue/parseFloat(percentage)*100).toFixed(2));
			}
		}
	});
	
	// 编号搜索货品
	$e.goodsCode.fool('dhxComboGrid',{
		filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=10',
		data:goods,
		width:'100%',
		height:"100%",
		required:true,
		novalidate:true,
		hasDownArrow:false,
		validType:['codeValid['+ind+']','nocodeValid['+ind+']'],
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
	     /*
			 * onBeforeSelect:function(index, row){
			 * if(_billCode=="bom"&&row.fid==$("#goodsId").val()){ return false; } },
			 */
		onSelectionChange:function(){
			// 预选中状态下查询库存
			var editor$ = $e.goodsCode;
			var chol = $(editor$.next()[0].comboObj.getList()).find(".dhxcombo_option_selected");
	    	var mindex = chol.index();
	    	if(mindex == -1){
	    		return false;
	    	}
	    	var fid = editor$.next()[0].comboObj.getOptionByIndex(mindex).text.fid;
	    	branchWarehouse(fid,"");
		},
	     onChange:function(value,text){
	    	 var _$e = $e;
	    	 var editor$ = _$e.goodsCode;
	    	 // var obj = myobj[myEditnum].find('a.btn-save');
		     var selectTips=0;
		     var row = editor$.next()[0].comboObj.getSelectedText();
		     if(!row){return ;}
		     if(_billCode=="bom"&&row.fid==$("#goodsId").val()){
		    	 return false;
		     }
		     barCodeKey = 0; // 标识为通过goodsCode获取商品信息
		     _$e.goodsId.val(row.fid);
		     _$e.barCode.textbox("setText","");// 条码置空
		     _$e.quentity.numberbox('setValue',0);
		     
		     _$e.receivedQuantity.numberbox('setValue',0);
		     _$e.loseQuantity.numberbox('setValue',0);
		     _$e.transportQuentity.numberbox('setValue',0);
		     _$e.transportPrice.numberbox('setValue',0);
		     _$e.transportAmount.numberbox('setValue',0);
		     _$e.deductionAmount.numberbox('setValue',0); 
		     getTableEditor(ind,"taxRate").parent().css('display')!="none"?
	    			 _$e.taxRate.numberbox('setValue',0):null;
	    			 _$e.describe.textbox('setValue','');
	    	 var regoodsId = _$e.regoodsId.val();
	    	 var refDetailId = _$e.refDetailId.val();
	    	 if(refDetailId != '' && row.fid != regoodsId){
	    		 _$e.refDetailId.val('');
	    	 }
	    	 _$e._goodsName.val(row.name);
	    	 _$e._goodsCode.val(row.code);
	    	 _$e._goodsSpec.val(row.spec);
			 getTableText(ind,'goodsSpec').text(row.spec);
			 _$e.goodsSpecGroupId.val(row.goodsSpecGroupId);
			 _$e.scale.val(row.unitScale);
			 _$e.updateTime.val(row.updateTime);
			 getTableText(ind,'goodsName').text(row.name);
	    	 var goodsId=_$e.goodsId.val();	    	
	    	 branchWarehouse(goodsId,natureId);
	    	 if(_billCode=="fhd"){//选择货品清空运输单位
	    		 $e.transportUnitName.next()[0].comboObj.setComboValue('');
	    		 $e.transportUnitName.next()[0].comboObj.setComboText('');
	    		 _$e.transportUnitName.next()[0].comboObj.setComboValue('');
	    		 _$e.transportUnitName.next()[0].comboObj.setComboText('');
	    	 }
	    	 if(_billCode=="shd"||_billCode=='fhd'){
	    		 $.get(getRootPath()+'/api/goodsTransport/queryTransportUnit',{goodsId:goodsId,shipmentTypeId:$("#shipmentTypeId").val()},function(data){
	    			 var transUnit = formatData(data,"fid");
	    			 $e.transportUnitName.next()[0].comboObj.clearAll();
	    			 $e.transportUnitName.next()[0].comboObj.addOption(transUnit);
	    			 if(data==''){//选择货品查询运输单位，存在允许编辑
	    				 $e.transportUnitName.next()[0].comboObj.selectOption(0);
	    				 /*$e.transportUnitName.next()[0].comboObj.disable();*/
	    			 }else{
	    				 $e.transportUnitName.next()[0].comboObj.setComboValue("");
	    				 $e.transportUnitName.next()[0].comboObj.setComboText("");
	    				 $e.transportUnitName.next()[0].comboObj.enable();
	    			 }
	    		 });
	    	 }
	    	 $e.percentage.numberbox("setValue",row.percentage);
	    	 $e.percentageAmount.numberbox("setValue",0);
	    	 // var editor$_ = getTableEditor(ind,'unitName');
	    	 if(row.unitGroupId){
	    		 _$e.unitGroupId.val(row.unitGroupId);
	    		 _$e._unitName.val(row.unitName);
	    		 var _unitGroupId = row.unitGroupId;
	    		 var unitData = "";
	    		 var transUnit="";
	    		 $.ajax({
	    			 	url:getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,
	    				async:false,
	    		        data:{},
	    		        success:function(data){
	    		        	unitData=formatData(data,"fid");
	    		        }
	    			});
	    		 /*$.get(getRootPath()+'/api/goodsTransport/queryTransportUnit',{goodsId:row.fid,specId:specId},function(data){
	    			 transUnit = formatData(data,"fid");
	    		 });*/
	    		 _$e.unitName.fool("dhxCombo",{
	    			 width:"100%",
	    			 height:"100%",
	    			 data:unitData,
	    			 required:true,
	    			 novalidate:true,
	    			 validType:['unitValid['+ind+']','nounitValid['+ind+']'],
	    			 setTemplate:{
	 					input:"#name#",
	 					option:'#name#'
	 				},
	    			onLoadSuccess:function(combo){
	    			    	var _d = unitData;
	    			    	if(_d)unitData_[ind] = _d;
	    			    	var _uid = row.unitId;
	    			    	combo.setComboValue(_uid);
	    			    	_$e.unitId.val(_uid);
	    			    	if(_billCode!="bom"){
	    			    		changeGoodPrice(ind);
	    			    	}
	    			}
	    		 });
	    		/* _$e.transportUnitName.fool("dhxCombo",{
	    			 width:"100%",
	   	    		 height:"100%",
	   	    		 data:transUnit,
	   	    		 required:true,
	   	    		 novalidate:true,*/
	   	    		 /* validType:['unitValid['+ind+']','nounitValid['+ind+']'], */
	   	    		/* setTemplate:{
	   	 				input:"#name#",
	   	 				option:'#name#'
	   	    		 },
	   	    		 onLoadSuccess:function(combo){*/
	   	    			 /*var _d = unitData;
	   	    			 if(_d)unitData_[ind] = _d;
	   	    			 var _uid = row.unitId;
	   	    			 combo.setComboValue(_uid);
	   	    			 _$e.unitId.val(_uid);*/
	   	    		 /*}
	    		 });*/
	    		/*
				 * _$e.unitName.fool('combobox',{ required:true,
				 * novalidate:true,
				 * validType:['unitValid['+myEditnum+']','nounitValid['+myEditnum+']'],
				 * url:getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId,
				 * valueField:'fid',width:'100%',panelMaxHeight:200,
				 * textField:'name',editable:true, onLoadSuccess:function(){ var
				 * obj = myobj[myEditnum].find('a.btn-save'); var ind =
				 * obj.fool('getRowIndex'); var _d =
				 * $(this).combobox('getData'); if(_d)unitData_[ind] = _d; var
				 * _uid = row.unitId; $(this).combobox('setValue',_uid);
				 * _$e.unitId.val(_uid); _$e.unitName.val(_uid);
				 * if(_billCode!="bom"){ changeGoodPrice(ind); } },
				 * onSelect:function(record){ var obj =
				 * myobj[myEditnum].find('a.btn-save'); var ind =
				 * obj.fool('getRowIndex'); _$e.unitId.val(record.fid);
				 * _$e.unitName.val(record.fid); _$e._unitName.val(record.name);
				 * if(_billCode!="bom"){ changeGoodPrice(ind); } }, filter:
				 * function(q, row){ if(!q)return; var opts =
				 * $(this).combobox('options'); return
				 * row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >=
				 * 0; } });
				 */
	    			/*
					 * _$e.unitName.combobox('textbox').bind('blur',function(){
					 * var obj = myobj[myEditnum].find('a.btn-save'); var ind =
					 * obj.fool('getRowIndex'); var _text = $(this).val();
					 * if(!_text)return; var _fid='' ,_name=''; for(var i in
					 * unitData_[ind]){ if(_text==unitData_[ind][i].name){ _fid =
					 * unitData_[ind][i].fid; _name = unitData_[ind][i].name;
					 * break; } } _$e.unitId.val(_fid); _$e.unitName.val(_name);
					 * _$e._unitName.val(_name);
					 * _$e.unitName.combobox('validate'); });
					 */
	    		/*
				 * $(_$e.unitName.next()[0].comboObj.getInput()).focus(function(){
				 * var $input = $(this);
				 * //setTimeout(function(){$input.select();},100); });
				 */
	    	 }else{
	    		 _$e.unitName.next()[0].comboObj.setComboValue('');
	    		 _$e.unitName.next()[0].comboObj.setComboText('');
	    		 _$e.transportUnitName.next()[0].comboObj.setComboValue('');
	    		 _$e.transportUnitName.next()[0].comboObj.setComboText('');
	    	 }
	    		var mygoodSpecId = row.goodsSpecGroupId;
	    		// editor$spec = getTableEditor(ind,'goodsSpecName');
	    		_$e.goodsSpecName.next()[0].comboObj.setComboText('');
	    		_$e._goodsSpecName.val('');
	    		_$e.goodsSpecId.val('');
	    		if(mygoodSpecId!=undefined&&mygoodSpecId.trim().length!=0){
	    			// 货品属性
	    			var myspecData = "";
	    			$.ajax({
	    			 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
	    				async:false,
	    		        data:{},
	    		        success:function(data){
	    		        	myspecData=formatData(data,"fid");
	    		        }
	    			});
	    			_$e.goodsSpecName.fool('dhxComboGrid',{
	    				width:"100%",
		    			height:"100%",
		    			hasDownArrow:false,
		    			required:true,
		    			novalidate:true,
		    			validType:['specValid['+ind+']','nospecValid['+ind+']'],
	    				filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
	    				data:myspecData,
	    				/*
						 * width:'100%', height:"90%", required:true,
						 * novalidate:true,
						 * validType:['specValid['+myEditnum+']','nospecValid['+myEditnum+']'],
						 * focusShow:true, onlySelect:true, columns:[[
						 * {field:'fid',title:'fid',hidden:true},
						 * {field:'name',title:'名称',width:100,searchKey:false},
						 * {field:'code',title:'编码',width:30,searchKey:false},
						 * {field:'searchKey',hidden:true,searchKey:true}, ]],
						 */
	    				onChange:function(value,text){
	    				   if(_billCode=="fhd"){//选择货品属性清空运输单位
	    			    	$e.transportUnitName.next()[0].comboObj.setComboValue('');
	    			    	$e.transportUnitName.next()[0].comboObj.setComboText('');
	    			    	_$e.transportUnitName.next()[0].comboObj.setComboValue('');
	    			    	_$e.transportUnitName.next()[0].comboObj.setComboText('');
	    			    	}
	    				}
	    			});
	    			var specCombo = _$e.goodsSpecName.next()[0].comboObj;
	    			specCombo.setComboText('');
	    			"undefined"!=typeof $(specCombo.getInput()).attr('disabled')&&$(specCombo.getInput()).attr('disabled')=='disabled'?specCombo.enable():null/* setTimeout(function(){_$e.goodsSpecName.combogrid('panel').css("display")=="block"?_$e.goodsSpecName.combogrid('hidePanel'):null;},500) */;
	    			/*
					 * _$e.goodsSpecName.combogrid('textbox').focus(function(){
					 * if(selectTips==0){//防止切换货品立即focus属性下拉框会消失的问题
					 * setTimeout(function(){_$e.goodsSpecName.combogrid('showPanel');selectTips=1;},500);
					 * 
					 * }else{ _$e.goodsSpecName.combogrid('showPanel'); }
					 * 
					 * });
					 */
	    		}else{
	    			$(_$e.goodsSpecName.next()[0].comboObj.getInput()).validatebox('disableValidation');
	    			_$e.goodsSpecName.next()[0].comboObj.disable();
	    			_$e.goodsSpecName.val("");
	    		}	  
	    		$(_$e.goodsCode.next()[0].comboObj.getInput()).validatebox('enableValidation');
	    		$(_$e.goodsCode.next()[0].comboObj.getInput()).validatebox('validate');
	     },
	     onOpen:function(){
	    	 if(_billCode=="bom"&&$("#goodsId").val()){
	    		 // 定义editor$，解决下拉框不弹出问题
	    		 var _$e = $e;
		    	 var editor$ = _$e.goodsCode;
	    		 var goodsId=$("#goodsId").val();
	    		 var _data = ""
	    		 $.ajax({
	    			 	url:getRootPath()+'/goods/vagueSearch?searchSize=10',
	    				async:false,
	    		        data:{nids:goodsId},
	    		        success:function(data){
	    		        	_data=formatData(data,"fid");
	    		        }
	    			});
	    			editor$.fool('dhxComboGrid',{
	    				filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=10&nids='+goodsId,
	    				data:_data
	    			});
	    	 }
	    	 $($e.goodsCode.next()[0].comboObj.getList()).prev().find(".myAdd").css("margin-top",$($e.goodsCode.next()[0].comboObj.getList()).height()+37+'px');
	     }
	});
	/*
	 * $e.goodsCode.combogrid('textbox').keydown(function(e){ var selected =
	 * $(this); if(e.keyCode == 9){ var v =
	 * selected.closest('td[field=goodsCode]').siblings('td[field=inWareHouseName]').css('display');
	 * if(selected.parent().prev().combogrid('grid').datagrid('getRows').length==1 &&
	 * v=="none"){// 解决编码搜索剩下一个选项时tab切换到单位选择框时失效的问题 e.preventDefault();
	 * selected.blur(); var mydata =
	 * selected.parent().prev().combogrid('grid').datagrid('getRows');
	 * if("undefined" == typeof mydata[0].goodsSpecGroupId){
	 * setTimeout(function(){ ed =
	 * selected.closest('td[field=goodsCode]').siblings('td[field=unitName]').find('.textbox-text');
	 * ed.focus(); ed.click(); },500); return; }else{ setTimeout(function(){ ed =
	 * selected.closest('td[field=goodsCode]').siblings('td[field=goodsSpecName]').find('.textbox-text');
	 * ed.focus(); ed.click(); },500); } } } });
	 */
	
	setTimeout(function(){
		if(_billCode=="shd"){
			$e.barCode.textbox("disable");
			($e.goodsCode.next())[0].comboObj.disable();
			($e.unitName.next())[0].comboObj.disable();
			($e.transportUnitName.next())[0].comboObj.disable();
			($e.goodsSpecName.next())[0].comboObj.disable();
			$e.quentity.numberbox("disable");
		}
	}, 200);
	
	mydetails!='' && !$e._isNew.val()?$e.goodsCode.next()[0].comboObj.setComboText(mygoodsCode):$e.goodsCode.next()[0].comboObj.setComboText(getTableEditor(ind,'_goodsCode').val());
	
	// myobj[myEditnum] = $(obj).closest('td[field="action"]');// 全局变量储存对象
	// $e.goodsCode.combogrid('panel').css('padding-bottom','50px');
	if(_billCode=="bom"&&$("#goodsId").val()){
		var goodsId=$("#goodsId").val();
		$($e.goodsCode.next()[0].comboObj.getList()).prev().append('<div class="myAdd" style="width:'+($($e.goodsCode.next()[0].comboObj.getList()).width()+17)+'px;margin-top:'+$($e.goodsCode.next()[0].comboObj.getList()).height()+37+'px;border:1px #ccc solid;margin-left:-1px; background:#fff;position:fixed; color:#646464; padding:5px 5px;">10条以后的商品请通过商品弹出框选择<br/><a href="javascript:;" onclick=addGoods('+ind+',\''+goodsId+'\'); class="btn-add" style="color:#646464;width:100px;padding-left: 20px;font-size: 14px;line-height: 20px;margin: 5px 0;">新增商品</a></div>');
	}else{
		$($e.goodsCode.next()[0].comboObj.getList()).prev().append('<div class="myAdd" style="width:'+($($e.goodsCode.next()[0].comboObj.getList()).width()+17)+'px;margin-top:'+$($e.goodsCode.next()[0].comboObj.getList()).height()+37+'px;border:1px #ccc solid;margin-left:-1px; background:#fff;position:fixed; color:#646464; padding:5px 5px;">10条以后的商品请通过商品弹出框选择<br/><a href="javascript:;" onclick=addGoods('+ind+'); class="btn-add" style="color:#646464;width:100px;padding-left: 20px;font-size: 14px;line-height: 20px;margin: 5px 0;">新增商品</a></div>');
	}

	$("#goodsList").find('tr#'+ind).on('keydown','input',function(e){
		if(e.ctrlKey && e.keyCode == 90){
			if (e.preventDefault) {  // firefox
	            e.preventDefault();
	        } else { // other
	            e.returnValue = false;
	        }
			// var obj =
			// $(this).closest('td[field]').siblings('td[field="action"]').find('a.btn-save');
	    	
	    	var input = $(this);
	    	// input.parent().prev().attr('class').search(/combo-f/)!=-1?input.parent().prev().combo('hidePanel'):null;
	    	input.blur();// 解决撤销或者删除行后验证提示信息还在的问题tyc
	    	if(getTableEditor(ind,'_isNew').val()=="false" || getTableEditor(ind,'_isNew').val()==false){
	    		setTimeout(function(){
	    			$("#goodsList").find('tr#'+ind+' a.btn-back').trigger("click");
	    		},300);
	    	}else{
	    		setTimeout(function(){$('#goodsList').jqGrid('delRowData',ind);hideSaveAllLink();},300);
	    	}
	    	if($('#goodsList #'+ind).prev().length > 0){
    			var nInd = $('#goodsList #'+ind).prev().attr("id");
    			$("#goodsList").jqGrid("setSelection",nInd,false);
    			$("#goodsList #input_dhxDiv_"+nInd+"_goodsCode").focus();
    		}
		}
	});
	
	$('#goodsList tr#'+ind+' input').focus(function(e){
		$("#goodsList").jqGrid("setSelection",ind);
	});
	$('#goodsList tr#'+ind+' input').keydown(function(e){
		if(e.keyCode == 107){
			var $aa = $(this);
			$aa.blur();
			if($('#recordStatus').val() != 1 && $('#recordStatus').val() != 2){
				$('#addrow').click();
			}
		}
	});
	
	// 替换按钮
	// $(obj).parent().html(mygetTBBtn(true,ind));
	// mynum++;
	hideSaveAllLink();
}

// 单位可进行本地模糊搜索
function unitSearch(target){
	if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
		  $(target.getInput()).bind('propertychange',function(){			
			  var mytext = $(this).val();
			  target.filter(function(text){
				    if (text.text.name.indexOf(mytext) != -1) return true;
				    return false;
			  });
			});// IE专用
		}else{
			$(target.getInput()).bind('input',function(){
				var mytext = $(this).val();
				target.filter(function(text){
					    if (text.text.name.indexOf(mytext) != -1) return true;
					    return false;
				  });
			});
		}
}

// 反计算
function numeration(target){
	var ind = target.closest('tr.jqgrow').attr("id");
	if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
	target.numberbox('textbox').bind('propertychange',function(){			
		quentity(this,ind);
	});// IE专用
}else{
	target.numberbox('textbox').bind('input',function(){
		quentity(this,ind);
	});
}
}

function quentity(target,ind){
	var taxAmount$=getTableEditor(ind,'taxAmount');// 税金编辑器
	var totalAmount$=getTableEditor(ind,'totalAmount');// 税后金额编辑器
	var _unitPrice$=getTableEditor(ind,'unitPrice');// 单价编辑器
	var _type$=getTableEditor(ind,'type');// 金额
	var _class$=$(target).parent().prev().attr('_class');
	if(_class$=="quentity"||_class$=="unitPrice"){// 改变单价和数量
	var _unitPrice=getTableEditor(ind,'unitPrice').numberbox('getText');// 单价
	var _quentity=getTableEditor(ind,'quentity').numberbox('getText');// 数量
	var _type=parseFloat(_unitPrice)*parseFloat(_quentity);// 税前金额
	_type$ .numberbox('setValue',_type);
	// 判断是修改状态
	if(_billCode=="cgfp"||_billCode=="xsfp"){	
	 var _taxRate=getTableEditor(ind,'taxRate').numberbox('getText');// 税点
	 var _taxAmount=parseFloat(_type)*parseFloat(_taxRate)/100;// 税金
	 var _totalAmount=parseFloat(_taxAmount)+parseFloat(_type);// 税后金额
	  taxAmount$.numberbox('setValue',_taxAmount);
	  totalAmount$.numberbox('setValue',_totalAmount);
		}
   }
	if(_class$=="type"){// 改变金额
		var _type=getTableEditor(ind,'type').numberbox('getText');// 金额
		var _quentity=getTableEditor(ind,'quentity').numberbox('getText');// 数量
        var _unitPrice=(parseFloat(_type)/parseFloat(_quentity));// 单价
        if(_quentity==0){
        	_unitPrice=0;
        }
          _unitPrice$.numberbox('setValue',_unitPrice);
          if(_billCode=="cgfp"||_billCode=="xsfp"){
        	 var _taxRate=getTableEditor(ind,'taxRate').numberbox('getText');// 税点
        	 var _taxAmount=parseFloat(_type)*parseFloat(_taxRate)/100;// 税金
        	 taxAmount$.numberbox('setValue',_taxAmount);
        	 var _totalAmount=parseFloat(_type)+parseFloat(_taxAmount);// 税后金额
             totalAmount$.numberbox('setValue',_totalAmount);
          }	
	}
	if(_class$=="taxRate"){// 改变税点
		 var _type=getTableEditor(ind,'type').numberbox('getText');// 税前金额
		 var _taxRate=getTableEditor(ind,'taxRate').numberbox('getText');// 税点
         var _taxAmount=parseFloat(_type)*parseFloat(_taxRate)/100;// 税金
         if(_type!=0){
         taxAmount$.numberbox('setValue',_taxAmount);
         var _totalAmount=parseFloat(_type)+parseFloat(_taxAmount);// 税后金额
         totalAmount$.numberbox('setValue',_totalAmount);	
         }
	}
	if(_class$=="taxAmount"){// 改变税金
		var _taxRate=getTableEditor(ind,'taxRate').numberbox('getText');// 税点
		_taxRate=_taxRate/100;
		var _taxAmount=getTableEditor(ind,'taxAmount').numberbox('getText');// 税金
		var _type=parseFloat(_taxAmount)/parseFloat(_taxRate);// 金额
		if(_taxRate==0){
			_type=0;
		}
		_type$.numberbox('setValue',_type);// 金额
		var _quentity=getTableEditor(ind,'quentity').numberbox('getText');// 数量
		var _unitPrice=parseFloat(_type)/parseFloat(_quentity);// 单价
		_unitPrice$.numberbox('setValue',_unitPrice);
		var _totalAmount=parseFloat(_type)+parseFloat(_taxAmount);// 税后金额
		if(_taxRate!=0){
         totalAmount$.numberbox('setValue',_totalAmount);
		}
	}
	if(_class$=="totalAmount"){// 改变税后金额
		var _taxRate=getTableEditor(ind,'taxRate').numberbox('getText');// 税点
		_taxRate=_taxRate/100;
		var _totalAmount=getTableEditor(ind,'totalAmount').numberbox('getText');// 税后金额
		var _type=parseFloat(_totalAmount)/(parseFloat(_taxRate)+parseFloat(1));// 金额
		var _quentity=getTableEditor(ind,'quentity').numberbox('getText');// 数量
		var _unitPrice=parseFloat(_type)/parseFloat(_quentity);
		if(_quentity!=0&&_unitPrice!=0){
		_type$.numberbox('setValue',_type);// 金额
		}
		if(_quentity==0){
			_unitPrice=0;
		}
		 _unitPrice$.numberbox('setValue',_unitPrice);
		var _taxAmount=parseFloat(parseFloat(_totalAmount)-parseFloat(_type));// 税金
		taxAmount$.numberbox('setValue',_taxAmount);
	}
}

// 当货品或属性改变时显示即时分仓方法
function branchWarehouse(goodsId,natureId){
	if(_billCode!="bom"&&_billCode!="shd"){
		/*if(s_goodsId == goodsId){
			return false;
		}	*/	
		$('.repertory ul').remove();
		 $.ajax({
				type : 'post',
				url : getRootPath()+'/stockStore/queryTreeFormat',
				data : {goodsId : goodsId, goodsSpecId : natureId},
				dataType : 'json',
				success : function(data) {
					//s_goodsId = goodsId;
					var dataLength=data.data.length;//children
					if(dataLength!=0){					
						var goodsName=data.data[0].goodsName;
						var warehousexier='';
						for(var i=0;i<data.data[0].children.length;i++){
							var goodsSpecName=data.data[0].children[i].goodsSpecName;// 属性
							if(goodsSpecName==undefined){
								goodsSpecName='';
							}
                            var warehouse="";
							var exist=false;
                            for(var k=0;k<data.data[0].children[i].children.length;k++){
                                var warehouseName=data.data[0].children[i].children[k].warehouseName;
                                var number = parseFloat(data.data[0].children[i].children[k].accountQuentity);
                                if(number>0) {
                                    exist=true;
                                    warehouse = warehouse + '<ul><li>' + warehouseName + '：</li><li class="number">' + number + '</li></ul>';
                                    $(".goodsSpec:gt(0)").css("margin-left","30px");// 从第二个开始添加外边距
                                }
                            }
                            if(exist)
                            	warehousexier='<ul class="goodsSpec"><li>'+goodsSpecName+'</li></ul>'+warehouse+warehousexier;
                        }
                        warehousexier='<ul><li>现有库存：'+goodsName+'</li></ul>'+warehousexier;
                        $('.repertory').html(warehousexier);
					 }else{
						 var dataName=data.goodsName;
						 var dataSpecname=data.specName;
						 if(dataSpecname==undefined){
							 dataSpecname='';
						 }
						 var warehousexier='<ul><li>'+dataName+dataSpecname+'&nbsp库存无货品</li></ul>';
						 $('.repertory').html(warehousexier);
						 $(".goodsSpec:gt(0)").css("margin-left","30px");// 从第二个开始添加外边距
					 }
	    		},
			});
	}
}

// 新增和修改生产计划单单据，货品在bom表有对应的记录，应该添加对应的材料明细
function subsidiaryMaterial(ind,newVal,del){
	var goodsIdnew="";
	var natureId="";
	var unitId="";
	var isNew="";
	var quentity="";
	var billId=$("#fid").val();
	var rows=$("#goodsList").jqGrid('getRowData');
	var row = $('#goodsList').jqGrid("getRowData",ind);
	var rowids = $('#goodsList').jqGrid("getDataIDs");
	var goods=[];
	if(del==1){
		goodsIdnew=row.goodsId;
		natureId=row.goodsSpecId;
		unitId=row.unitId;
		quentity=row.quentity;
		isNew=row._isNew;
		goods.push({goodsId:goodsIdnew,goodsSpecId:natureId,unitId:unitId,quentity:quentity});
	}else{
		for(var i=0;i<rows.length;i++){
			if($("#goodsList").find("tr#"+rowids[i]+" a.btn-save").length>0){
				if(getTableEditor(rowids[i],'_isNew').val()=='true'&&ind!=rowids[i]){
					continue;
				}else if(getTableEditor(rowids[i],'_isNew').val()!='true'&&ind!=rowids[i]){
					goodsIdnew=rows[i].goodsId;
					natureId=rows[i].goodsSpecId;
					unitId=rows[i].unitId;
					quentity=rows[i].quentity;
					isNew=rows[i]._isNew;
				}else{
					goodsIdnew=getTableEditor(rowids[i],'goodsId').val();
					natureId=getTableEditor(rowids[i],'goodsSpecId').val();
					unitId=getTableEditor(rowids[i],'unitId').val();
					quentity=getTableEditor(rowids[i],'quentity').val();
					isNew=getTableEditor(rowids[i],'_isNew').val();
				}
			}else{
				goodsIdnew=rows[i].goodsId;
				natureId=rows[i].goodsSpecId;
				unitId=rows[i].unitId;
				quentity=rows[i].quentity;
				isNew=rows[i]._isNew;
			}
			goods.push({goodsId:goodsIdnew,goodsSpecId:natureId,unitId:unitId,quentity:quentity,isNew:isNew});
			goodsIdnew="";
			natureId="";
			unitId="";
			quentity="";
			isNew="";
		}
	}
	 $.ajax({
			type : 'post',
			url : getRootPath()+'/warehouse/scjhd/calculation',
			data :{details:JSON.stringify(goods)},
			dataType : 'json',
			success : function(data) { 
				if(del==1){
					dellinst(newVal,data);
				}else{
					addlinst(newVal,data,isNew);
				}
			},
	 });
}

function addlinst(newVal,data,isNew){
	$('#materialList').jqGrid("clearGridData",false);
	var unitGroupId="";
	var goodsSpecGroupId="";
	var goodsCode="";
	var goodsId="";
    var goodsName="";
    var goodsSpec="";
    var unitId="";
    var unitName="";
    var quentity="";
    var describe="";
    var fid="";
    var goodsSpecName="";
    var goodsSpecId="";
    var counted=0;
	for(var j=0;j<data.length;j++){
		goodsCode=data[j].goodsCode;
		goodsId=data[j].goodsId;
	    goodsName=data[j].goodsName;
	    unitGroupId=data[j].unitGroupId;
	    goodsSpecGroupId=data[j].goodsSpecGroupId;
	    goodsSpec=data[j].goodsSpec;
	    unitId=data[j].unitId;
	    unitName=data[j].unitName;
	    quentity=data[j].quentity;
	    describe=data[j].describe;
	    unitPrice=data[j].unitPrice;
	    fid=data[j].fid;
	    goodsSpecName=data[j].goodsSpecName;
	    goodsSpecId=data[j].goodsSpecId;
	    counted=1;
	    ind=0;
	    $('#materialList').jqGrid('addRowData',j+1,{
	    	goodsCode:goodsCode,// 货品编号
	    	goodsId:goodsId,
	    	unitGroupId:unitGroupId,
	    	goodsSpecGroupId:goodsSpecGroupId,
	    	goodsName:goodsName,// 货品名
	    	_goodsCode:goodsCode,
	    	_goodsName:goodsName,
	    	goodsSpec:goodsSpec,// 规格
	    	_goodsSpec:goodsSpec,
	    	goodsSpecId:goodsSpecId,
	    	goodsSpecName:goodsSpecName,// 属性
	    	_goodsSpecName:goodsSpecName,
	    	unitId:unitId,
	    	unitName:unitName,// 单位
	    	_unitName:unitName,// 单位
	    	quentity:quentity,// 数量
	    	describe:describe,// 备注
	    	unitPrice:0,// 单价
			fid:fid,
			counted:counted,
	    });
	    goodsCode="";
		goodsId="";
	    goodsName="";
	    goodsSpec="";
	    unitId="";
	    unitName="";
	    quentity="";
	    describe="";
	    unitPrice="";
	    fid="";
	    goodsSpecName="";
	    goodsSpecId="";
	    counted=0;
	    ind=0;
	};
}

function dellinst(newVal,data){
	var rows=$("#materialList").jqGrid('getRowData');
	var rowids = $("#materialList").jqGrid('getDataIDs');
	// $("#materialList").datagrid('deleteRow',myIndex);
	var goodsId="";
	var unitId="";
	var goodsSpecId="";
    var quentity="";
    var sumQuentity="";
    var ind=0;
    var s="";
    var u="";
    var sp="";
    outer:
	for(var j=0;j<data.length;j++){
    	// 防止undefined
		if(data[j].goodsId){
			goodsId=data[j].goodsId;
		}else{
			goodsId="";
		}
		if(data[j].unitId){
			unitId=data[j].unitId;
		}else{
			unitId="";
		}
		if(data[j].goodsSpecId){
			goodsSpecId=data[j].goodsSpecId;
		}else{
			goodsSpecId="";
		}
		if(data[j].quentity){
			quentity=data[j].quentity;
		}else{
			quentity="";
		}
	    sumQuentity=parseFloat(quentity);
	    ind=0;
	    for(var i=0;i<rows.length;i++){
	    	// 防止undefined
	    	if(rows[i].goodsId){
				s=rows[i].goodsId;
			}else{
				s="";
			}
			if(rows[i].unitId){
				u=rows[i].unitId;
			}else{
				u="";
			}
			if(rows[i].goodsSpecId){
				sp=rows[i].goodsSpecId;
			}else{
				sp="";
			}
	    	if(ind<rows.length){
	    		ind=ind+1;
	    	}
	    	/*
			 * console.log(s); console.log(u); console.log(sp);
			 * console.log(goodsId); console.log(unitId);
			 * console.log(goodsSpecId+"////////////");
			 */
	    	if(goodsId==s&&unitId==u&&goodsSpecId==sp){
	    		if(rows[i].quentity>sumQuentity){
	    			$("#materialList").jqGrid('setRowData',rowids[i],{
	    					quentity:(rows[i].quentity-sumQuentity).toFixed(2),
	    			});
	    		}else{
	    			$("#materialList").jqGrid("delRowData",rowids[i]);
	    		}
	    		continue outer;
	    	}
	    	s="";
	    	u="";
	    	sp="";
	    }
		goodsId="";
	    quentity="";
	    sumQuentity="";
	    ind=0;
	};	
}

// 详细页。货品列表单位下拉
function unitActionNew(value,options,row){
	var index = options.rowId;
	if(unitData_[index] != null){
		for(var i in unitData_[index]){
			if(row.unitId==unitData_[index][i].text.fid){return unitData_[index][i].text.name;}
		}
	}else{
		return value;
	}
}
// 详细页。添加货品按钮
function addGoods(ind,goodsId){
	var boxWidth=1074;
	var boxHeight=500;
	if(ind){
		getTableEditor(ind,'goodsCode').next()[0].comboObj.closeAll();
		$(getTableEditor(ind,'goodsCode').next()[0].comboObj.getInput()).blur();
		goodsIndex = ind;
		
		var _billType=_billCode;
		if(_billType=="bom"){
			_billType="xsdd";
		}
		var _customerId = $("#customerId").val();
		var _supplierId = $("#supplierId").val(); 
		if(!_customerId){
			_customerId="";
		}
		if(!_supplierId){
			_supplierId="";
		}
		if(!goodsId){
			goodsId="";
		}
		win = $("#pop-win").fool('window',{top:$(window).scrollTop()+100,modal:true,'title':"选择货品",width:boxWidth,height:boxHeight,
			href:getRootPath()+'/goods/window?okCallBack=myselectGoods&onDblClick=myselectGoods&billType='+_billType+'&customerId='+_customerId+'&supplierId='+_supplierId+'&goodsId='+goodsId});
	}else{
		$("#goodsCode").next()[0].comboObj.closeAll();
		win = $("#pop-win").fool('window',{top:$(window).scrollTop()+100,modal:true,'title':"选择货品",width:boxWidth,height:boxHeight,
			href:getRootPath()+'/goods/window?okCallBack=bomSelectGoods&onDblClick=bomSelectGoods&singleSelect=true'});
	}
	
}

// 详细页。添加货品
function myselectGoods(data,notclose){
	if(data==undefined)return;
	typeof notclose !="undefined" && notclose == true?null:closeWin();
	$.messager.progress({
		text:'努力加载中，请稍后...'
	});
	setTimeout(function(){
		$(data).each(function(i,n){
			newIndex++;
			var myind = goodsIndex;
			if(i!=0 || myind==""){
				myind = newIndex - 1;
			}
			$("#goodsList").jqGrid('addRowData',newIndex,{
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
					receivedQuantity:0,
					loseQuantity:0,
					transportQuentity:0,
					transportPrice:0,
					transportAmount:0,
					deductionAmount:0,
					_isNew:true
			},"after",myind);
			// var _data = $("#goodsList").datagrid("getData");
			editNew(newIndex);
			if(_billCode!="bom"){
	    		changeGoodPrice(newIndex);
	    	}

		});
		goodsIndex!=""?$("#goodsList").jqGrid('delRowData',goodsIndex):null;
		if(typeof notclose !="undefined" && notclose == true){
			goodsIndex = "";
		}
		$.messager.progress('close');
		hideSaveAllLink(false);
	},300);
}

function bomSelectGoods(data){
	closeWin();
	var row="";
	if(data[0]){
		row=data[0];
	}else{
		row=data;
	}
	$("#goodsId").val(row.fid);
	$("#goodsName").next()[0].comboObj.setComboText(row.name);
	
	var myspecData = "";
	$.ajax({
	 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+row.goodsSpecId+'&searchSize=6',
		async:false,
        data:{},
        success:function(data){
        	myspecData=formatData(data,"fid");
        }
	});
	$("#specName").fool('dhxComboGrid',{
		width:182,
	    height:34,
		hasDownArrow:false,
		filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+row.goodsSpecId+'&searchSize=6',
		data:myspecData,
	});
	$.post(getRootPath()+'/unitController/getChilds?unitGroupId='+row.unitGroupId,{},function(data){
		var myunitData = formatData(data,"fid");
		$("#accountUnitName").fool('dhxCombo',{
			data:myunitData,
			setTemplate:{
				input:"#name#",
				option:'#name#'
			},
		    width:182,
		    height:34,
		});
	});
	$("#goodSpecGroupId").val(row.goodsSpecId);
	$("#unitGroupId").val(row.unitGroupId);
	$.ajax({
		url:getRootPath()+"/unitController/findAccountUnit",
		async:false,
		data:{parentId:row.unitGroupId},
		success:function(r){
			$("#accountUnitId").val(r.fid);
			$("#accountUnitName").next()[0].comboObj.setComboText(r.name); 
		}
	});
}

// 新增一行按钮
function addrow(flag){
	if(_billCode =='fhd'){//发货单
		var shipmentTypeId=$('#shipmentTypeId').val();
		if(shipmentTypeId==''||shipmentTypeId==null||shipmentTypeId=='undefine'){
			$.fool.alert({time:2000,msg:'请先填写单据信息装运方式'});
			return false;
		}
	}
	if(flag==1){
		newCIndex++;
		$('#containerList').jqGrid('addRowData',newCIndex,{
			_isNew:true
		});
		editContainerNew(newCIndex);
		setTimeout(function(){
			$('#containerList').jqGrid('setSelection',newCIndex);
			$(getTableEditor(newCIndex,'containerNumber',1).next()[0].comboObj.getInput()).focus(); 
		},300); 
		var pOffset = $('#containerList').find("tr#"+newCIndex);
		pOffset.closest('.window-body').animate({scrollTop:pOffset.offset().top-$('#warehousebillForm').offset().top-76},500);
	}else{
		newIndex++;
		$('#goodsList').jqGrid('addRowData',newIndex,{			
			transportPrice:0,
			_isNew:true,
		});
		// $('#goodsList').datagrid('beginEdit',(newIndex-1));
		// var newobj =
		// $('#goodsList').datagrid('getPanel').find('[datagrid-row-index="'+(newIndex)+'"]
		// [field="action"] .btn-del');
		editNew(newIndex);
		setTimeout(function(){$('#goodsList').jqGrid('setSelection',newIndex);$(getTableEditor(newIndex,'goodsCode').next()[0].comboObj.getInput()).focus();},300);
		var pOffset = $('#goodsList').find("tr#"+newIndex);
		pOffset.closest('.window-body').animate({scrollTop:pOffset.offset().top-$('#warehousebillForm').offset().top-76},500);
	}
}

// 新模板详细页。货品列表操作按钮
function goodsListActionNew(value,options,row){
	var index = options.rowId;
	if(value == 'new'){
		if(_billCode=="shd"){
			return "";
		}
		return "<a href='javascript:;' id='addrow' class='btn-add' onclick='addrow()' title='新增'></a>";
	}else{
		if(!row.editing){
			return mygetTBBtn(false,index);
		}else{
			return mygetTBBtn(true,index);
		}
	}
}
// 新模板详细页。箱号列表操作按钮
function containerListActionNew(value,options,row){
	var index = options.rowId;
	if(value == 'new'){
		/*if(_billCode=="shd"){
			return "";
		}*/
		return "<a href='javascript:;' id='cAddrow' class='btn-add' onclick='addrow(1)' title='新增'></a>";
	}else{
		if(!row.editing){
			return mygetCTBBtn(false,index);
		}else{
			return mygetCTBBtn(true,index);
		}
	}
}
$.extend($.fn.validatebox.defaults.rules, {    
	inMem: {    
        validator: function(value,param){
            return $('#outMemberName').length>0?($('#outMemberName').next()[0].comboObj.getComboText()!=value):true;    
        },    
        message: '入仓人和出仓人不能是同一个人'   
    },
    inWare: {    
        validator: function(value,param){
            return $('#outWareHouseName').length>0?($('#outWareHouseName').next()[0].comboObj.getComboText()!=value):true;    
        },    
        message: '调入仓库和调出仓库不能相同'   
    },
	codeValid: {    
        validator: function(value,param){
            return getTableEditor(param[0],'goodsId').val()==''?false:true;    
        },    
        message: '货品没有选中，请重新选择'   
    },
    nocodeValid: {    
        validator: function(value,param){
            return getTableEditor(param[0],'_goodsCode').val()!=getTableEditor(param[0],'goodsCode').next()[0].comboObj.getComboText()?false:true;    
        },    
        message: '货品编号与货品不符，请重新选择'   
    },
    specValid: {    
        validator: function(value,param){
            return getTableEditor(param[0],'goodsSpecId').val()==''?false:true;    
        },    
        message: '属性没有选中，请重新选择'   
    },
    nospecValid: {    
        validator: function(value,param){
            return getTableEditor(param[0],'_goodsSpecName').val()!=getTableEditor(param[0],'goodsSpecName').next()[0].comboObj.getComboText()?false:true;    
        },    
        message: '属性名称不符，请重新选择'   
    },
    unitValid: {    
        validator: function(value,param){
            return getTableEditor(param[0],'unitId').val()==''?false:true;    
        },    
        message: '单位没有选中，请重新选择'   
    },
    nounitValid: {    
        validator: function(value,param){
        	// console.log(getTableEditor(param[0],'_unitName').val()+"
			// "+getTableEditor(param[0],'unitName').combobox('getText'));
            return getTableEditor(param[0],'_unitName').val()!=getTableEditor(param[0],'unitName').next()[0].comboObj.getComboText()?false:true;    
        },    
        message: '单位名称不符，请重新选择'   
    },
    transporValue: {    
        validator: function(value,param){
            return getTableEditor(param[0],'_transportUnitName').val()!=getTableEditor(param[0],'transportUnitName').next()[0].comboObj.getComboText()?false:true;    
        },    
        message: '运输单位名称不符，请重新选择'   
    },
    warehouseValid: {    
        validator: function(value,param){
            return value!=$('#inWareHouseName').next()[0].comboObj.getComboText();    
        },    
        message: '调出仓库不能与调入仓库一样，请修改'   
    }
}); 
function mysave(index,flag){
	var ind = index;
	if(flag==1){ 
		var v = $("#containerList").find('tr#'+ind).form("validate");
		if(v){
			// 更新新建标识
			getTableEditor(ind,'_isNew',"1").val(false);
			$('#containerList').jqGrid('saveRow', ind);
			$('#containerList').jqGrid('setRowData',ind,{editing:false,action:null});
		}else{
			return false;
		}
	}else{
		var quentity=getTableEditor(ind,'quentity').val();// 数量
		validDetailHelp(ind,_billCode);
		if(!saveAll_){
			getTableEditor(ind,'goodsId').val()==''?$(getTableEditor(ind,'goodsCode').next()[0].comboObj.getInput()).validatebox('validate').select():null;
			getTableEditor(ind,'goodsSpecId').val()==''?$(getTableEditor(ind,'goodsSpecName').next()[0].comboObj.getInput()).validatebox('validate').select():null;
		}
		var inputCode = getTableEditor(ind,'goodsCode').next()[0].comboObj.getComboText();
		if(inputCode != getTableEditor(ind,'_goodsCode').val()){
			$(getTableEditor(ind,'goodsCode').next()[0].comboObj.getInput()).validatebox('validate').select();
		}
		var inputSpec = getTableEditor(ind,'goodsSpecName').next()[0].comboObj.getComboText();
		if(inputSpec != getTableEditor(ind,'_goodsSpecName').val()){
			$(getTableEditor(ind,'goodsSpecName').next()[0].comboObj.getInput()).validatebox('validate').select();
		}
		// 验证
		$("#goodsList").find('tr#'+ind).form("enableValidation");
		if($("#goodsList_inWareHouseName").css("display")=="none"){
			$(getTableEditor(ind,'inWareHouseName').next()[0].comboObj.getInput()).validatebox('disableValidation');
		}
		if($("#goodsList_transportUnitName").css("display")=="none"){
			$(getTableEditor(ind,'transportUnitName').next()[0].comboObj.getInput()).validatebox('disableValidation');
		}
		
		if($("#goodsList_quentity").css("display")=="none"){
			getTableEditor(ind,'quentity').numberbox("disableValidation");
		}
		if($("#goodsList_receivedQuantity").css("display")=="none"){
			getTableEditor(ind,'receivedQuantity').numberbox("disableValidation");
		}
		if($("#goodsList_loseQuantity").css("display")=="none"){
			getTableEditor(ind,'loseQuantity').numberbox("disableValidation");
		}
		if($("#goodsList_transportPrice").css("display")=="none"){
			getTableEditor(ind,'transportPrice').numberbox("disableValidation");
		}
		if($("#goodsList_transportAmount").css("display")=="none"){
			getTableEditor(ind,'transportAmount').numberbox("disableValidation");
		}  
		if($("#goodsList_deductionAmount").css("display")=="none"){
			getTableEditor(ind,'deductionAmount').numberbox("disableValidation");
		}
		if($("#goodsList_transportQuentity").css("display")=="none"){
			getTableEditor(ind,'transportQuentity').numberbox("disableValidation");
		}
		if($("#goodsList_costPrice").css("display")=="none"){
			getTableEditor(ind,'costPrice').numberbox("disableValidation");
		}
		if($("#goodsList_type").css("display")=="none"){
			getTableEditor(ind,'type').numberbox("disableValidation");
		}
		
		if($("#goodsList_percentage").css("display")=="none"){
			getTableEditor(ind,'costPrice').numberbox("disableValidation");
		}
		if($("#goodsList_percentageAmount").css("display")=="none"){
			getTableEditor(ind,'type').numberbox("disableValidation");
		}
		var v = $("#goodsList").find('tr#'+ind).form("validate");
		if(v){
			if(_billCode=="bom"){
				var rows=$("#goodsList").jqGrid("getRowData");
				var rowids = $('#goodsList').jqGrid("getDataIDs");
				for(var i=0;i<rows.length;i++){
					if(rowids[i]==ind){
						continue;
					}
					if(getTableEditor(ind,'goodsSpecId').val()==rows[i].goodsSpecId&&getTableEditor(ind,'goodsId').val()==rows[i].goodsId){
						$.fool.alert({msg:"物料+属性有重复"});
						return ;
					}
				}
			}
			if(_billCode=="scjhd"){// 生产计划单
				subsidiaryMaterial(ind,quentity);
			}
			if(_billCode=="shd"){
				var receivedQuantity=getTableEditor(ind,'receivedQuantity').numberbox("getValue");
				var loseQuantity=getTableEditor(ind,'loseQuantity').numberbox("getValue");
				var quentity=getTableEditor(ind,'quentity').numberbox("getValue");
				if(parseFloat(quentity)!=(parseFloat(receivedQuantity)+parseFloat(loseQuantity))){
					$.fool.alert({msg:"货品数量不等于实收数量+亏损数量"});
				}
			}
			// 更新新建标识
			getTableEditor(ind,'_isNew').val(false);
			var editor$ = getTableEditor(ind,'goodsCode');
			getTableEditor(ind,'_goodsSpec').val(getTableText(ind,'goodsSpec').text());
	   	 	getTableEditor(ind,'_goodsName').val(getTableText(ind,'goodsName').text());
	   	 	var editor$ = getTableEditor(ind,'goodsSpecName');
	   	    getTableEditor(ind,'_goodsSpecName').val(editor$.next()[0].comboObj.getComboText());
	   	    var unitName = getTableEditor(ind,'unitName').next()[0].comboObj.getComboText();
	   	    var transportUnitName = getTableEditor(ind,'transportUnitName').next()[0].comboObj.getComboText();
	   	    var inWareHouseName = getTableEditor(ind,"inWareHouseName").next()[0].comboObj.getComboText();
	   	    barCodeKey == 0?getTableEditor(ind,'barCode').val(""):null;
			$('#goodsList').jqGrid('saveRow', ind); 
			$('#goodsList').jqGrid('setRowData',ind,{unitName:unitName,transportUnitName:transportUnitName,inWareHouseName:inWareHouseName,quentity:quentity,editing:false,action:null});
			// 更新总金额
			getTotalNew();
			
			if(getEditingOnSize()<=0){
				hideSaveAllLink();
			}
		}else{
			return false;
		}
	}
}

// 计算货品列表总金额
function getTotalNew(){
	var rows=$('#goodsList').jqGrid('getRowData');
	var total=0;
	var _total=0;
	var taxTotal=0;
	var qtotal = 0;
	var tqtotal=0;
	var tTotal=0;
	var dTotal=0;
	var rqtotal=0;
	var lqtotal=0;
	for(var i=0;i<rows.length;i++){
		if(rows[i].action.search(/editing-on/) != -1){
			continue;
		}
		if("undefined" != typeof rows[i].quentity){
			qtotal += parseFloat(rows[i].quentity);
		}
		if("undefined" == typeof rows[i].quentity || "undefined" == typeof rows[i].unitPrice){
			rows[i].type = 0;
		}/*
			 * else{ rows[i].type=rows[i].quentity*rows[i].unitPrice; }
			 */
		if("undefined" != typeof rows[i].transportQuentity&&rows[i].transportQuentity!=""){
			tqtotal += parseFloat(rows[i].transportQuentity);
		}
		if("undefined" != typeof rows[i].transportAmount&&rows[i].transportAmount!=""){
			tTotal += parseFloat(rows[i].transportAmount);
		}
		if("undefined" != typeof rows[i].deductionAmount&&rows[i].deductionAmount!=""){
			dTotal += parseFloat(rows[i].deductionAmount);
		}
		if("undefined" != typeof rows[i].receivedQuantity&&rows[i].receivedQuantity!=""){
			rqtotal += parseFloat(rows[i].receivedQuantity);
		}
		if("undefined" != typeof rows[i].loseQuantity&&rows[i].loseQuantity!=""){
			lqtotal += parseFloat(rows[i].loseQuantity); 
		}
		if("undefined" == typeof rows[i].taxAmount){rows[i].taxAmount=0;}
		rows[i].totalAmount=parseFloat(rows[i].type)*(1+rows[i].taxRate/100);
		rows[i].taxAmount=parseFloat(rows[i].type)*(rows[i].taxRate/100);// 增加了税金的合计
		total+=parseFloat(rows[i].type);
		_total+=rows[i].totalAmount;
		taxTotal+=rows[i].taxAmount;
	}
	// var panel = $('#goodsList').datagrid('getPanel');
	$('#goodsList').footerData("set",{quentity:qtotal.toFixed(2),type:total.toFixed(2),totalAmount:_total.toFixed(2),receivedQuantity:rqtotal.toFixed(2),loseQuantity:lqtotal.toFixed(2),transportQuentity:tqtotal.toFixed(2),transportAmount:tTotal.toFixed(2),deductionAmount:dTotal.toFixed(2)},false);
	if(_billCode == "xsfp" || _billCode == "cgfp"){// 增加了发票编辑页税金的合计
		$('#goodsList').footerData("set",{taxAmount:taxTotal.toFixed(2)});
		$("#totalAmount").val(taxTotal.toFixed(2));
	}else if(_billCode == "shd"){
		$("#totalAmount").val(tTotal.toFixed(2)); 
		$("#deductionAmount").val(dTotal.toFixed(2));
	}else if(_billCode =='fhd'){
		$("#totalAmount").val(tTotal.toFixed(2)); 
	}else{
		$("#totalAmount").val(total.toFixed(2));
	}
}

function mygetTBBtn(f,index){
	if(_flag=='detail'&&_billStat!='0')return "";
	if(f){
		// actionC[index] = false;
		var s = "<a href='javascript:;' class='btn-save editing-on btn-index-save-"+index+"' onclick='mysave(\""+index+"\")' title='确认'></a>";
		var c = "<a href='javascript:;' class='btn-back btn-index-cancel-"+index+"' onclick='cancel(\""+index+"\")' title='撤销'></a>";
		return s+c;
	}else{
		// actionC[index] = true;
		var r = "<a href='javascript:;' class='btn-del btn-index-del-"+index+"' onclick='delNew(\""+index+"\")' title='删除'></a>";
        if(_billCode=="shd"){
        	r="";
		}
		return r;
	}
}
function mygetCTBBtn(f,index){
	if(_flag=='detail'&&_billStat!='0')return "";
	if(f){
		// actionC[index] = false;
		var s = "<a href='javascript:;' class='btn-save editing-on btn-index-save-"+index+"' onclick='mysave(\""+index+"\",1)' title='确认'></a>";
		var c = "<a href='javascript:;' class='btn-back btn-index-cancel-"+index+"' onclick='cancel(\""+index+"\",1)' title='撤销'></a>";
		return s+c;
	}else{
		// actionC[index] = true;
		var r = "<a href='javascript:;' class='btn-del btn-index-del-"+index+"' onclick='delNew(\""+index+"\",1)' title='删除'></a>";
		return r;
	}
}
// 详细页货品列表，采购发票、销售发票根据税金计算
function calculation(value,options,row){
	if(value == 'new'){
		return '';
	}else{
		if(row.taxRate){			
			var type=(row.quentity*row.unitPrice).toFixed(2);
			return (type*row.taxRate).toFixed(2);			
		}else{
			return '';
		}
	}
}
// 详细页货品列表，采购发票、销售发票根据税金计算
function After_the_amount(value,options,row){
	if(value == 'new'){
		return '';
	}else{
		if(row.taxRate){			
			var type=(row.quentity*row.unitPrice);
			var taxAmount=(type*row.taxRate);
			return (parseInt(type)+parseInt(taxAmount)).toFixed(2);		
		}else{
			return '';
		}
	}
}

// 详细页。货品列表金额格式化
function typeActionNew(value,options,row){
	if(value == 'new'){
		return '';
	}else{
		if(row.quentity&&row.unitPrice){
			return (row.quentity*row.unitPrice).toFixed(2);
			// return parseInt(row.quentity)*parseInt(row.unitPrice);
		}else{
			return 0;
		}
	}
}
// 详细页。货品列表税后金额格式化
function totalActionNew(value,options,row){
		if(typeof row.taxRate != "undefined"){
			row.totalAmount=row.quentity*row.unitPrice*(1+row.taxRate/100);
			return (row.totalAmount).toFixed(2);
			// return parseInt(row.quentity)*parseInt(row.unitPrice);
		}else{
			return 0;
		}
}
// 详细页。货品列表单价
function priceActionNew(value,options,row){
	if(value == 'new'){
		return '';
	}else{
		var _n = new Number(value);
		if(isNaN(_n))
			return 0;
		else 
			return (_n).toFixed(4);
	}
}

function demandAmount(transportUnitId,ind){//单价根据发货地ID、收货地ID、运输方式、装运方式、运输公司、运输单位查询运输费报价
		   var supplierId=$('#supplierId').val();
		   var deliveryPlaceId=$('#deliveryPlaceId').val();
		   var receiptPlaceId=$('#receiptPlaceId').val();
		   var transportTypeId=$('#transportTypeId').val();
		   var shipmentTypeId=$('#shipmentTypeId').val();
		   var transportUnitId=transportUnitId;
		  if(supplierId!=''&&deliveryPlaceId!=''&&receiptPlaceId!=''&&transportTypeId!=''&&shipmentTypeId!=''&&transportUnitId!=undefined){			 
		   $.ajax({
		 	url:getRootPath()+'/transportPrice/findByCompany',
			async:false,
	        data:{transportUnitId:transportUnitId,deliveryPlaceId:deliveryPlaceId,receiptPlaceId:receiptPlaceId,transportTypeId:transportTypeId,shipmentTypeId:shipmentTypeId,supplierId:supplierId},
	        success:function(data){	
	        	var amount=data.amount;
	        	if(amount!=''||amount!=null||amount!=undefined){
	        	var panel = $('#goodsList');// 获取货品列表
				 panel.find('tr#'+ind+' input#'+ind+'_transportPrice').numberbox('setValue',amount);
	        	}
	        }
		});
	}
}


/*
 * 新模板详情页文本框初始化 文本框必须设置 _class 属性
 * 
 * 2016-9-8更改了控件的加载方式
 */
function editInputInit(obj){ 
	var requiredValue = null;
	_billCode == 'pdd' || _billCode == 'dcd'||_billCode == 'shd'||_billCode == 'fhd'?requiredValue = true:requiredValue = false;// 特定仓库验证
	var boxWidth=182,boxHeight=31;// 统一设置输入框大小
	var $t = $.fool.get$(obj);
	
	var _cla = $t.attr('_class');
	if(_cla=='datebox'){// 详情页
		$t.datebox({width:boxWidth,height:boxHeight,editable:false,onSelect:function(){
			var startTime = $('#startDate').length > 0?$('#startDate').datebox('getValue'):'';
			var endTime = $('#endDate').length > 0?$('#endDate').datebox('getValue'):'';
			var customerId = $('#customerId').length > 0?$('#customerId').val():'';
			var supplierId = $('#supplierId').length > 0?$('#supplierId').val():'';
			(_billCode == 'xsfld') && startTime!='' && endTime!='' && customerId!=''?
					$.post(getRootPath()+'/salesRebateRebBill/getSales',{startTime:startTime,endTime:endTime,customerId:customerId},function(data){
						data?$('#sales').val(data):null;
						rebatelv();
					}):null;
			( _billCode == 'cgfld') && startTime!='' && endTime!='' && supplierId!=''?
					$.post(getRootPath()+'/purchaseRebBill/getSales',{startTime:startTime,endTime:endTime,supplierId:supplierId},function(data){
						data?$('#sales').val(data):null;
						rebatelv();
					}):null;		
		}});
	}else if(_cla=='datebox_curr'){// 日期带当天默认值
		var _isNull = $t.val().length==0;
		$t.datebox({width:boxWidth,height:boxHeight,editable:false});
		if(_isNull)
			$t.datebox('setValue',getCurrDate());
	}else if(_cla=='datebox_end'){// 结束不能小于开始日期,data-options必须要设置startId,startId为开始日期控件的ID
		var $opt = $.fool._opts$($t);
		var _isNull = $t.val().length==0;
		if(!$opt||!$opt.startId){
			$.fool.alert({msg:"你没有设置开始日期文本框的值,请在该组件data-options设置startId的值为开始日期控件的id值"});
			return;
		}
		_isNull = $opt.currTime ? true : false;
		$t.datebox({width:boxWidth,height:boxHeight,editable:false,}).datebox('calendar').calendar({
			validator:function (date){
		    	var start = $("#"+$opt.startId).datebox("getValue");
		    	var d1 = $.fn.datebox.defaults.parser(start);
		    	return d1<=date;
			}
		});
		if(_isNull)$t.datebox('setValue',getCurrDate());
	}else if(_cla=='textbox'){
		$t.textbox({width:boxWidth,height:boxHeight});
	}else if(_cla=='validatebox'){
		$t.validatebox();
	}else if(_cla=='combobox'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({width:boxWidth,height:boxHeight},$opt);
		$t.combobox($opt);
	}else if(_cla=='customer-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true,
			novalidate:true,
			width:boxWidth,
			height:boxHeight,
			data:customer,
			validType:"combogridValid['customerId']",
			toolsBar:{
				name:"客户",
				addUrl:"/customer/manage",
				refresh:true
			},
			focusShow:true,
		    onlySelect:true,
		    hasDownArrow:false,
			filterUrl:getRootPath()+'/customer/vagueSearch?searchSize=8&num='+Math.random(),
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
		  	},
			onChange:function(value,text){
				var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;
					return false;
				}
				$("#customerId").val(row.fid);
				$("#customerCode").val(row.code);
				$("#customerName").val(row.name);
				$("#customerPhone").val(row.phone);
				$.fool._formValidHelp($($d[0].comboObj.getInput()));
				var startTime = $('#startDate').length > 0?$('#startDate').datebox('getValue'):'';
				var endTime = $('#endDate').length > 0?$('#endDate').datebox('getValue'):'';
				var customerId = row.fid;
				(_billCode == 'xsfld') && startTime!='' && endTime!='' && customerId!=''?
						$.post(getRootPath()+'/salesRebateRebBill/getSales',{startTime:startTime,endTime:endTime,customerId:customerId},function(data){
							data?$('#sales').val(data):null;
							rebatelv();
						}):null;
			}
			/*
			 * onChange:function(nv,ov){ $("#customerId").val('');
			 * if(!$("#customerName").combogrid("grid").datagrid("options").url&&$("#customerName").combogrid("getText")){
			 * $("#customerName").combogrid("grid").datagrid({
			 * url:getRootPath()+'/customer/vagueSearch?searchSize=8', });
			 * $("#customerName").combogrid("showPanel"); } }
			 */
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='supplier-combogrid'){
		var $opt = $.fool._opts$($t);
		var required=true;
		if($t.attr("_required")!=undefined){
			required=$t.attr("_required");
		}
		$opt = $.extend({
			required:required,
			novalidate:true,
			validType:"combogridValid['supplierId']",
			width:boxWidth,
			height:boxHeight,
			data:supplier,
			toolsBar:{
				name:"供应商",
				addUrl:"/supplier/manage",
				refresh:true
			},
			focusShow:true,
		    onlySelect:true,
		    hasDownArrow:false,
			filterUrl:getRootPath()+'/supplier/vagueSearch?searchSize=8&num='+Math.random(),
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
		  	},
			onChange:function(value,text){	
				var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();			
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;							
					return false;
				}
				/*
				 * if(row.fid!=$("#supplierId").val()&&_billCode!='cgdd'){
				 * $("#relation").val(''); $("#relationId").val(''); }
				 */
				$("#supplierId").val(row.fid);
				$("#supplierCode").val(row.code);
				// $("#supplierName").val(row.name);
				$("#supplierPhone").val(row.phone);
				$.fool._formValidHelp($($d[0].comboObj.getInput()));
				var startTime = $('#startDate').length > 0?$('#startDate').datebox('getValue'):'';
				var endTime = $('#endDate').length > 0?$('#endDate').datebox('getValue'):'';
				var supplierId = row.fid;
				( _billCode == 'cgfld') && startTime!='' && endTime!='' && supplierId!=''?
						$.post(getRootPath()+'/purchaseRebBill/getSales',{startTime:startTime,endTime:endTime,supplierId:supplierId},function(data){
							data?$('#sales').val(data):null;
							rebatelv();
						}):null;
			   if(_billCode=="fhd"){//发货单
					demandAmount();
				}					
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
		if(_billCode=="shd"){
			($("#supplierName").next())[0].comboObj.readonly(true);
			($("#supplierName").next())[0].comboObj.attachEvent("onOpen", function(){
				($("#supplierName").next())[0].comboObj.closeAll();
			});
		}
	}else if(_cla=='carNo-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			method:'get',
			width:boxWidth,
			height:boxHeight,
			data:carNoValue, 
			validType:["combogridValid['id']",'maxLength[50]'],
			focusShow:true,
		    /*onlySelect:true,*/
		    hasDownArrow:false,
			filterUrl:getRootPath()+'/api/vehicleInformation/vagueSearch?searchSize=10&num='+Math.random(),
			setTemplate:{
				input:"#licenseNumber#",
				columns:[
                         {option:'#licenseNumber#',header:'车牌号',width:100},
						 {option:'#ownerName#',header:'司机姓名',width:100},
						 {option:'#contactPhone#',header:'司机电话',width:100},					
						],
		  	},
			onChange:function(value,text){				
				var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				//console.log(row);
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;
					return false;
				}
				if(_billCode=='fhd'){
				$("#carNo").next()[0].comboObj.setComboValue(text);
				 //this.setComboValue(text);
				}
                $("#driverName").textbox('setValue',row.ownerName);
                $("#driverPhone").textbox('setValue',row.contactPhone);
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
		if(_billCode=="shd"){
			($("#carNo").next())[0].comboObj.readonly(true);
			($("#carNo").next())[0].comboObj.attachEvent("onOpen", function(){
				($("#carNo").next())[0].comboObj.closeAll();
			});
		}
	}else if(_cla=='member-combogrid'){
		var $opt = $.fool._opts$($t);
		var memRequried=true;
		if($t.attr("_imp")=="true"){
			memRequried=true;
		}else if($t.attr("_imp")=="false"){
			memRequried=false;
		}
		$opt = $.extend({
			required:memRequried,
			novalidate:true,
			width:boxWidth,
			height:boxHeight,
			data:member,
			validType:["combogridValid['inMemberId']","inMem"],
			focusShow:true,
		    onlySelect:true,
		    hasDownArrow:false,
			filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8&num='+Math.random(),
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
                name:"人员",
                addUrl:"/member/memberManager",
                refresh:true
            },
			onChange:function(value,text){
				var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;
					return false;
				}
				$("#inMemberId").val(value);
				$.fool._formValidHelp($($d[0].comboObj.getInput()));
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='outMember-combogrid'){
		var $opt = $.fool._opts$($t);
		var required=true;
		if($t.attr("_required")!=undefined){
			required=$t.attr("_required");
		}
		$opt = $.extend({
			required:required,
			novalidate:true,
			width:boxWidth,
			height:boxHeight,
			data:member,
			toolsBar:{
				name:"人员",
				addUrl:"/member/memberManager",
				refresh:true
			},
			validType:["combogridValid['outMemberId']"],
			focusShow:true,
		    onlySelect:true,
		    hasDownArrow:false,
			filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8&num='+Math.random(),
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
		  	onChange:function(value,text){
		  		var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;
					return false;
				}
				$("#outMemberId").val(value);
				$.fool._formValidHelp($($d[0].comboObj.getInput()));
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='payee-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true,
			novalidate:true,
			width:boxWidth,
			height:boxHeight,
			data:member,
			validType:"combogridValid['payeeId']",
			focusShow:true,
		    onlySelect:true,
		    hasDownArrow:false,
			filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8&num='+Math.random(),
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
                name:"人员",
                addUrl:"/member/memberManager",
                refresh:true
            },
		  	onChange:function(value,text){
		  		var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;
					return false;
				}
				$("#payeeId").val(value);
				$.fool._formValidHelp($($d[0].comboObj.getInput()));
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='mymember-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:$t.attr('myrequired')==1?true:false,
			novalidate:true,
			width:boxWidth,
			height:boxHeight,
			data:member,
			validType:"combogridValid['memberId']",
			toolsBar:{
				name:"人员",
				addUrl:"/member/memberManager",
				refresh:true
			},
			focusShow:true,
		    onlySelect:true,
		    hasDownArrow:false,
			filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8&num='+Math.random(),
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
		  	onChange:function(value,text){
		  		var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;
					return false;
				}
				$("#memberId").val(value);
				$.fool._formValidHelp($($d[0].comboObj.getInput()));	
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='bank-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true, 
			novalidate:true,
			width:182,
			height:30,
			focusShow:true,
			data:bankData,
			toolsBar:{
				name:"现金/银行",
				addUrl:"/bankController/bankManager",
				refresh:true
			},
			validType:"combogridValid['bankId']",
			hasDownArrow:false,
			filterUrl:getRootPath()+'/bankController/list&num='+Math.random(),
			searchKey:"keyWord",
			setTemplate:{
				input:"#name#(#account#)",
				columns:[
						{option:'#code#',header:'编号',width:40},
						{option:'#name#',header:'名称',width:80},
						{option:'#bank#',header:'银行',width:80},
						{option:'#account#',header:'账号',width:200},
					    ],
			},
			onChange:function(value,text){
				var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				if(!row){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					return false;
				}
				$("#bankId").val(Value);
				$.fool._formValidHelp($($d[0].comboObj.getInput()));
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='warehouse'){// 仓库详情页
		$t.fool('dhxCombo',{
			required:requiredValue,
			novalidate:true,
			setTemplate:{
				  input:"#name#",
				  option:"#name#"
			},
			toolsBar:{
				name:"仓库",
				addUrl:"/storehouses/manage",
				refresh:true
			},
			hasDownArrow:false,
			editable:false,
			data:warehouse,
			validType:"inWare",
			width:boxWidth,
			height:boxHeight,
			focusShow:true,
			onChange:function(value,text){
				$('#inWareHouseId').val(value);
			}
		});
		if(_billCode=="shd"){
			($("#inWareHouseName").next())[0].comboObj.readonly(true);
			($("#inWareHouseName").next())[0].comboObj.attachEvent("onOpen", function(){
				($("#inWareHouseName").next())[0].comboObj.closeAll();
			});
		}
	}else if(_cla=='outWarehouse'){// 仓库详情页
		$t.fool('dhxCombo',{
			required:requiredValue,
			validType:"warehouseValid",
			novalidate:true,
			setTemplate:{
				  input:"#name#",
				  option:"#name#"
			},
			toolsBar:{
				name:"仓库",
				addUrl:"/storehouses/manage",
				refresh:true
			},
			editable:false,
			data:warehouse,
			width:boxWidth,
			height:boxHeight,
			focusShow:true,
			onChange:function(value,text){
				$('#outWareHouseId').val(value);
			}
		});
	}else if(_cla=='outWarehousefhd'){// 发货单仓库详情页
		$t.fool('dhxCombo',{
			required:requiredValue,
			novalidate:true,
			setTemplate:{
				  input:"#name#",
				  option:"#name#"
			},
			toolsBar:{
				name:"仓库",
				addUrl:"/storehouses/manage",
				refresh:true
			},
			editable:false,
			data:warehouse,
			width:boxWidth,
			height:boxHeight,
			focusShow:true,
			onChange:function(value,text){
				$('#outWareHouseId').val(value);
			}
		});
	}else if(_cla=='freightAddress'){
		var freightAddressData="";
		$.ajax({
		 	url:getRootPath()+"/api/freightAddress/findAddressTree?enable=1",
			async:false,
	        data:{},
	        success:function(data){
	        	freightAddressData=data;
	        }
		});
		$t.fool('dhxCombo',{
			hasDownArrow:false,
			width:boxWidth,
			height:boxHeight,
			required:true,
            novalidate:true,
			data:freightAddressData,
			/*data:[{
				text: 'Item1',
				children: [{
					text: 'Item11'
				},{
					text: 'Item12'
				}]
			},{
				text: 'Item2',
				children: [ ],
				state:"closed"
			}],*/
			onSelect:function(record){
				if(_billCode=="shd"){
					if($t.attr("id")=="deliveryPlaceName"){
						$('#deliveryPlaceId').val(record.fid);
					}else if($t.attr("id")=="receiptPlaceName"){
						$('#receiptPlaceId').val(record.fid);
						$("#inWareHouseName").next()[0].comboObj.setComboValue(record.assetwarehouseId);
						$("#inWareHouseId").val(record.assetwarehouseId);
					}
				}else if(_billCode=="fhd"){
					if($t.attr("id")=="receiptPlaceName"){
						$('#receiptPlaceId').val(record.fid);
					}else if($t.attr("id")=="deliveryPlaceName"){
						   $('#deliveryPlaceId').val(record.fid);
						   $('#inWareHouseName').val(record.assetwarehouseName);
						   $("#inWareHouseId").val(record.assetwarehouseId);
		    		      /*  $("#inWareHouseName").next()[0].comboObj.setComboValue(record.assetwarehouseId);
		    		        $("#inWareHouseId").val(record.assetwarehouseId);*/
					}
				}
				if(_billCode=="fhd"){//发货单
					demandAmount();
				}
			},
			onBeforeSelect:function(node){
				if((_billCode=="fhd"&&$t.attr("id")=="deliveryPlaceName")||(_billCode=="shd"&&$t.attr("id")=="receiptPlaceName")){//发货单并且是发货地址
				  if(node.flag == 0){
					$.fool.alert({msg:"请选择最详细的地址名"});
					return false;
				   }
				}
			}
		})
		/*
		 * $t.fool('dhxCombo',{ required:requiredValue, novalidate:true,
		 * setTemplate:{ input:"#name#", option:"#name#" }, selectFirst:true,
		 * editable:false,
		 * data:getComboData(getRootPath()+"/api/freightAddress/getTree","tree"),
		 * width:boxWidth, height:boxHeight, focusShow:true,
		 * onChange:function(value,text){
		 * if($t.attr("id")=="deliveryPlaceName"){
		 * $('#deliveryPlaceId').val(value); }else
		 * if($t.attr("id")=="receiptPlaceName"){
		 * $('#receiptPlaceId').val(value); $.ajax({
		 * url:getRootPath()+'/api/freightAddress/getById?fid='+value,
		 * async:false, data:{}, success:function(data){
		 * $("#warehouseName").next()[0].comboObj.setComboValue(data); } }); } }
		 * });
		 */
	}else if(_cla=='transportType'){//运输方式情页
		$t.fool('dhxCombo',{
			required:requiredValue,
			hasDownArrow:false,
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
			},
			//selectFirst:true,
			editable:false,
			data:getComboData(getRootPath()+"/basedata/transitType?num="+Math.random(),'tree'),
			width:boxWidth,
			height:boxHeight,
			focusShow:true,
			onChange:function(value,text){
				$('#transportTypeId').val(value);
				if(_billCode=="fhd"){//发货单
					demandAmount();
				}		
			}
		});
		if(_billCode=="shd"){
			($("#transportTypeName").next())[0].comboObj.readonly(true);
			($("#transportTypeName").next())[0].comboObj.attachEvent("onOpen", function(){
			($("#transportTypeName").next())[0].comboObj.closeAll();
			});
		}
	}else if(_cla=='shipmentType'){//装运方式情页
		var ship=$t.fool('dhxCombo',{
			required:requiredValue,
			hasDownArrow:false,
			required:true, 
			novalidate:true,
			setTemplate:{
				  input:"#name#",
				  option:"#name#"
			},
			toolsBar:{
				name:"装运方式",
				addUrl:"/basedata/listAuxiliaryAttr",
				refresh:true
			},
			//selectFirst:true,
			editable:false,
			data:getComboData(getRootPath()+"/basedata/findSubAuxiliaryAttrTree?code=020","tree"),
			width:boxWidth,
			height:boxHeight,
			focusShow:true,
			onChange:function(value,text){
			  var shipmentTypeId=$('#shipmentTypeId').val();
			  $('#shipmentTypeId').val(value);
			  if(_billCode=="fhd"){//发货单
				demandAmount();
			 if(shipmentTypeId){
				if(shipmentTypeId!=value){//修改了装运方式
					 var details1=$("#goodsList").jqGrid('getRowData').length;
					if(details1>0){
				   $.fool.confirm({title:'确认',msg:'修改将会删除货品信息所以数据!',fn:function(r){
					 if(r){
						 jQuery("#goodsList").jqGrid("clearGridData");
					 }
				 }});
				}
			  }	
			  }
		     }		
			},
		});
		if(_billCode=="shd"){
			($("#shipmentTypeName").next())[0].comboObj.readonly(true);
			($("#shipmentTypeName").next())[0].comboObj.attachEvent("onOpen", function(){
				($("#shipmentTypeName").next())[0].comboObj.closeAll();
			});
		}
	}else if(_cla=='user-combogrid'){// 仓库详情页
		$t.fool("dhxComboGrid",{
			width:boxWidth,
			height:boxHeight,
			setTemplate:{
				input:"#userName#",
				columns:[
				         {option:'#userCode#',header:'编码',width:100},
				         {option:'#userName#',header:'名称',width:100},
						],
			},
			filterUrl:getRootPath()+'/userController/vagueSearch?searchSize=6',
			data:getComboData(getRootPath()+'/userController/vagueSearch?searchSize=6'),
			searchKey:"searchKey", 
			focusShow:true,
			onChange:function(value,text){
				$('#userId').val(value);
			}
		});
	}else if(_cla=='deptComBoxTree'){// 详情页
		$t.fool('dhxCombo',{
			  required: true,
			  novalidate:true,
			clearOpt:false,
			  setTemplate:{
				  input:"#text#",
				  option:"#text#"
			  },
			  toolsBar:{
					name:"部门",
					addUrl:"/orgController/deptMessage",
					refresh:true
			  },
			  editable:false,
			  data:organization,
			  width:boxWidth,
			  height:boxHeight,
			  focusShow:true,
			  onChange:function(value,text){
				  $('#deptId').val(value);
			  }
		});
	}else if(_cla=='productionStatus'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			width:boxWidth,
			height:boxHeight,
			hasDownArrow:false,
			editable:false,
			data:[
		          {value:"0",text:"未开工"},
		          {value:"1",text:"进行中"},
		          {value:"2",text:"已完成"},
		          {value:"3",text:"已暂停"},
		          {value:"4",text:"已中止"},
		          ],
		    focusShow:true,
		},$opt);
		$t.fool("dhxCombo",$opt);
	}else if(_cla=="toPublic-combobox"){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			width:boxWidth,
			height:boxHeight,
			editable:false,
			required:true,
			novalidate:true,
		    focusShow:true,
			data:[
		          {value:"0",text:"对公"},
		          {value:"1",text:"对私"},
		          ],
			onChange:function(value,text){
				var first = 1;
				if(_billCode == 'cgfld'){// 采购返利单对公标识为对公时收款人非必填
											// ,销售返利单对公标识为对公时受款人非必填
					if(value==0){
						$('#memberName').siblings('.memb').children('em').css('display','none');
						$('#memberName').attr('myrequired',"0");
						editInputInit($('#memberName'));
					}else if(value==1){
						$('#memberName').siblings('.memb').children('em').css('display','inline-block');
						$('#memberName').attr('myrequired',"1");
						editInputInit($('#memberName'));
					}
				}
				if(_billCode == 'xsfld'){
					if(value==0){
						$('#payeeName').siblings('.paye').children('em').css('display','none');
						$('#payeeName').validatebox({required:false});
					}else if(value==1){
						$('#payeeName').siblings('.paye').children('em').css('display','inline-block');
						$('#payeeName').validatebox({required:true});
					}  
				}
			}
		},$opt);
		$t.fool("dhxCombo",$opt); 
	}else if(_cla=='goods-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true,
			novalidate:true,
			width:boxWidth,
			height:boxHeight,
			focusShow:true,
			onlySelect:true,
			validType:"combogridValid['goodsId']",
			filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=8&num='+Math.random(),
			setTemplate:{
				input:"#code#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
							{option:'#barCode#',header:'条码',width:100},
							{option:'#spec#',header:'规格',width:100},
							{option:'#unitName#',header:'单位',width:100},
						],
			},
			data:goods,
			onChange:function(value,text){
				var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;
					return false;
				}
				var row = $d[0].comboObj.getSelectedText();
				$("#specId").val("");
				$("#specName").next()[0].comboObj.setComboText("");
				var mygoodSpecId = row.goodsSpecGroupId;
				var myspecData = "";
    			$.ajax({
    			 	url:getRootPath()+'/goodsspec/vagueSearch?parentId='+mygoodSpecId+'&searchSize=6',
    				async:false,
    		        data:{},
    		        success:function(data){
    		        	myspecData=formatData(data,"fid");
    		        }
    			});
				$("#specName").fool("dhxComboGrid",{data:myspecData,filterUrl:getRootPath()+'/goodsspec/vagueSearch?parentId='+row.goodsSpecId+'&searchSize=6'});
				$.ajax({
					url:getRootPath()+'/unitController/getChilds?unitGroupId='+row.unitGroupId,
					async:false,
					data:{},
					success:function(data){
						var mydata = formatData(data,"fid");
						$("#accountUnitName").fool("dhxCombo",{data:mydata,setTemplate:{
							input:"#name#",
							option:'#name#'
						},});
					}
				});
				// $("#accountUnitName").combobox({url:getRootPath()+'/unitController/getChilds?unitGroupId='+row.unitGroupId,width:boxWidth,height:boxHeight,});
				$("#goodSpecGroupId").val(row.goodsSpecId);
				$("#unitGroupId").val(row.unitGroupId);
				$.ajax({
					url:getRootPath()+"/unitController/findAccountUnit",
					async:false,
					data:{parentId:row.unitGroupId},
					success:function(data){
						$("#accountUnitId").val(data.fid);
						$("#accountUnitName").next()[0].comboObj.setComboValue(data.fid); 
					}
				});
				$("#goodsId").val(value);
				$("#goodsName").val(row.name);
				$.fool._formValidHelp($($d[0].comboObj.getInput()));
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
		// $t.combogrid('panel').css('padding-bottom','50px');
		// $t.combogrid('panel').children().append('<div
		// style="width:inherit;border:1px #ccc solid;margin-left:-1px;
		// background:#fff;position:absolute; color:#646464; padding:5px
		// 5px;">6条以后的商品请通过商品弹出框选择<br/><a href="javascript:;"
		// onclick=addGoods(); class="btn-add"
		// style="color:#646464;width:100px;padding-left: 20px;font-size:
		// 14px;line-height: 20px;margin: 5px 0;">新增商品</a></div>');
	}else if(_cla=='spec-combogrid'){
		var $opt = $.fool._opts$($t);
		var _goodSpecId="";
		_goodSpecId=$("#goodSpecGroupId").val();
		var url=getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6&num='+Math.random();
		var mydata = "";
		if(_goodSpecId == ""){
			mydata=[];
			url="";
		}else{
			$.ajax({
				url:getRootPath()+'/goodsspec/vagueSearch?parentId='+_goodSpecId+'&searchSize=6&num='+Math.random(),
				async:false,
				data:{},
				success:function(data){
					mydata = formatData(data,"fid");
				}
			});
		}
		$opt = $.extend({
			filterUrl:url,
			data:mydata,
			width:boxWidth,
			height:boxHeight,
			required:false,
			focusShow:true,
			hasDownArrow:false,
			onlySelect:true,
			validType:"combogridValid['specId']",
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#name#',header:'名称',width:100},
							{option:'#code#',header:'编码',width:100},
						],
			},
			toolsBar:{
				name:"货品属性",
				addUrl:"/goodsspec/manage",
				refresh:true
			},
			onChange:function(value,text){
				var $d = $t.next();
				var row = $d[0].comboObj.getSelectedText();
				if(!row && $d.find(".dhxcombo_input")[0].lsKey && $d.find(".dhxcombo_input")[0].lsKey == 1){ // 修复缓存后载入text触发后面的赋值导致其他输入框置空的问题
					$d.find(".dhxcombo_input")[0].lsKey = 0;
					return false;
				}
		    	$("#specId").val(value);
		 		$.fool._formValidHelp($($d[0].comboObj.getInput()));
		    },
			/*
			 * onChange:function(nv,ov){ $("#specId").val(''); }
			 */
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='unit-combogrid'){
		var $opt = $.fool._opts$($t);
		var _unitGroupId="";
		_unitGroupId=$("#unitGroupId").val();
		var mydata = "";
		if(_unitGroupId == ""){
			mydata=[];
		}else{
			$.ajax({
				url:getRootPath()+'/unitController/getChilds?unitGroupId='+_unitGroupId+'&num='+Math.random(),
				async:false,
				data:{},
				success:function(data){
					mydata = formatData(data,"fid");
				}
			});
		}
		$opt = $.extend({
			data:mydata,
		    width:boxWidth,
			height:boxHeight,
			focusShow:true,
			onlySelect:true,
			setTemplate:{
				input:"#name#",
				option:'#name#'
			},
		    onChange:function(value,text){
		    	$("#accountUnitId").val(value);
		    },
			/*
			 * onChange:function(nv,ov){ $("#accountUnitId").val(''); }
			 */
		},$opt);
		$t.fool("dhxCombo",$opt);
	}
}
// 期初库存导入操作
function getIn(){
	var s={
			target:$('#import-win'),
			boxTitle:"导入货品",
			downHref:getRootPath()+"/ExcelMapController/downTemplate?downFile=期初库存.xls",
			action:getRootPath()+"/initialstock/qckc/import",
			fn:'okCallback()',
			items:"goods",
	};
	importBox(s);
}
function okCallback(){
	$('#import-win').window("close");
}

// 初始化详细页 按钮
function mybtnFooterInit(){
	var $obj = $(".mybtn-footer");
	var _stat = $("#recordStatus").val();
	var _id = $("#fid").val();
	// var _flag = $("#page-flag").val();
	var _btn = "";
	var ref = "<a id=\"refreshForm\" class=\"mybtn-blue mybtn-s\" onclick=\"myrefreshForm()\">刷新</a>";
	var add = "<input type=\"button\" id=\"save\" onclick=\"mysaveData()\" class=\"mybtn-blue mybtn-s\" value=\"保存\" />";
	var fadd = "<input type=\"button\" id=\"save\" onclick=\"fldsaveData()\" class=\"mybtn-blue mybtn-s\" value=\"保存\" />";
	var sk = '<input onclick="collection()" id="mycollection" class="mybtn-blue mybtn-s" value="收款" />';
	var fk = '<input onclick="payBill()" id="mypay" class="mybtn-blue mybtn-s" value="付款" />';
	// var appr = "<a id=\"verify\" class=\"mybtn-blue mybtn-s\"
	// onclick=\"mypassAudit('"+_id+"')\">审核</a>";
	var can = "<a id=\"void\" class=\"mybtn-blue mybtn-s\" onclick=\"mycancelVoid('"+_id+"')\">作废</a>";	
	var pri = "<a id=\"print\" class=\"mybtn-blue mybtn-s\" onclick=\"printBillDetail('"+_id+"','"+_billCode+"')\">打印</a>";
	var gen = "<a id=\"generate\" class=\"mybtn-blue mybtn-s\" onclick=\"generateByIdEdit('"+_id+"','"+_billCode+"')\">生成</a>";
	var copyRow = "<a id=\"copyRow\" onclick=\"mycopyRow('"+_id+"','"+_billCode+"')\" class=\"mybtn-blue mybtn-s\">复制</a>";
	var sall = '<a href="javascript:;" class="btn-ora-add hide savaAll" onclick="saveAll()" >一键确定</a>';
	var imp = '<a href="javascript:;" onclick="getIn()" class="btn-ora-add">导入</a>';
	var receiver= '<a id="mysk" href="javascript:;" onclick="receiver(\''+_id+'\',\''+_billCode+'\')" class=\"mybtn-blue mybtn-s\">收款</a>';
	var payer= '<a id="myfk" href="javascript:;" onclick="payer(\''+_id+'\',\''+_billCode+'\')" class=\"mybtn-blue mybtn-s\">付款</a>';
	// 显示勾对单据
	var fkxq = '<a id="fkxq" href="javascript:;" onclick="fkxq(\''+_id+'\')" class=\"mybtn-blue mybtn-s\">付款详情</a>';
	var skxq = '<a id="skxq" href="javascript:;" onclick="skxq(\''+_id+'\')" class=\"mybtn-blue mybtn-s\">收款详情</a>';
	var fyxq = '<a id="fyxq" href="javascript:;" onclick="fyxq(\''+_id+'\')" class=\"mybtn-blue mybtn-s\">费用详情</a>';
	//显示资金计划
	var zjjh= '<a id="zjjh" href="javascript:;" onclick="zjjh(\''+_id+'\')" class=\"mybtn-blue mybtn-s\">资金计划</a>';
	/*
	 * var count = "<a id=\"count\" class=\"mybtn-blue mybtn-s\"
	 * onclick=\"count('"+_id+"','"+_billCode+"')\">计算</a>";
	 */
	//暂时屏蔽核价功能
	var pric = /*"<a id=\"pricing\" onclick=\"pricingway('"+_id+"','"+_billCode+"')\" class=\"mybtn-blue mybtn-s\">核价</a>"*/""; 

	_billCode == 'xsfld' || _billCode == 'cgfld'?add = fadd:null;
	/* _billCode != 'scjhd'?count = '':null; */
	if(_billCode=="bom"||_billCode=="fhd"||_billCode=="shd"){
		pri="";
	} 
	/*if(_billCode=="fhd"||_billCode=="shd"){
		fkxq = '';
		skxq = '';
		fyxq='';
	}*/
	if(_stat==0){
		_billCode != 'qckc'?imp='':null;
		if(_billCode=='cgfp'||_billCode=='xsfp'||_billCode=='cgsqd'||_billCode=='scjhd'||_billCode=='cgfld'||_billCode=='xsfld'){// 采购发票和销售发票打印功能暂未实现，暂时屏蔽打印按钮
			_id==''?_btn=sall+imp+add:_btn = sall+imp+add+copyRow+ref/* +count */;
		}else{
		_id==''?_btn=sall+imp+add:_btn = sall+imp+add+copyRow+pri+ref/* +count */;	
		}
		if(_billCode == 'cgrk'||_billCode == 'cgth'||_billCode == 'cgfp'||_billCode == 'cgfld'||_billCode == 'xsfp'||_billCode == 'xsfld'||_billCode == 'shd'||_billCode == 'fyd'||_billCode == 'xsch'||_billCode == 'xsth'||_billCode == 'qcys'||_billCode == 'qcyf'){
			_id!=''?_btn=_btn+zjjh:null;
		}
	}else if(_stat == 1){
		_billCode != 'cgsqd'?gen='':null;
		if(_billCode=='cgfp'||_billCode=='xsfp'||_billCode=='cgsqd'||_billCode=='scjhd'||_billCode=='cgfld'||_billCode=='xsfld'){// 采购发票和销售发票打印功能暂未实现，暂时屏蔽打印按钮
			_btn = copyRow+ref+can+gen/* +count */;
			if(_billCode=='xsfld'){
				_btn = _btn+sk;
			}
			if(_billCode=='cgfld'){
				_btn = _btn+fk;
			}
		}else{
			_btn = copyRow+pri+ref+can+gen/* +count */;	
		}
		// 采购入库单、销售出库单、采购退货单、销售退货单增加核价功能按钮
		if(_billCode=="cgrk"||_billCode=="cgth"||_billCode=="xsch"||_billCode=="xsth"){
			_btn=_btn+pric;
		}
		// 采购入库、采购退货增加付款详情按钮
		if(_billCode=="cgrk"||_billCode=="cgth"||_billCode=="cgfp"||_billCode=="cgfld"||_billCode=="shd"){
			_btn=_btn+fkxq;
		}
		// 销售出货、销售退货增加收款详情按钮
		if(_billCode=="xsch"||_billCode=="xsth"||_billCode=="xsfp"||_billCode=="xsfld"){
			_btn=_btn+skxq;
		}
		// 销售出货、销售发票增加收款按钮
		if(_billCode=="xsch"||_billCode=="xsfp"||_billCode=="xsth"){
			_btn=_btn+receiver;
		}
		// 采购入库、采购发票增加收款按钮
		if(_billCode=="cgrk"||_billCode=="cgfp"||_billCode=="cgth"||_billCode=="shd"){
			_btn=_btn+payer;
		}
		if(_billCode=="bom"){
				_btn=copyRow;
		}else{
			_btn=_btn+fyxq;// 增加费用详情按钮
		}
		if(_billCode == 'cgrk'||_billCode == 'cgth'||_billCode == 'cgfp'||_billCode == 'cgfld'||_billCode == 'xsfp'||_billCode == 'xsfld'||_billCode == 'shd'||_billCode == 'fyd'||_billCode == 'xsch'||_billCode == 'xsth'||_billCode == 'qcys'||_billCode == 'qcyf'){
			_btn=_btn+zjjh;
		}
	}else{
		if(_billCode=="bom"){
			if(_id){
				_btn=add+copyRow;
			}else{
				_btn=add;
			}
		}else{
			if(_billCode=='cgfp'||_billCode=='xsfp'||_billCode=='cgsqd'||_billCode=='scjhd'||_billCode=='cgfld'||_billCode=='xsfld'){// 采购发票和销售发票打印功能暂未实现，暂时屏蔽打印按钮
				_btn = copyRow;	
			}else{
				_btn = copyRow+pri;
			}			
		}
	}
	$obj.html(_btn);
}
			
// 付款详情
function fkxq(id){
	$('#scancel').length>0?$('#scancel').click():null;// 弹窗自动取消收付款操作
	win = $("#pop-win").fool('window',{modal:true,'title':"付款详情",height:480,width:780,href:getRootPath()+'/paymentCheck/checkingWin?billCode=fkd&billId='+id+'&_billCode='+_billCode});
}
// 收款详情
function skxq(id){
	$('#scancel').length>0?$('#scancel').click():null;
	win = $("#pop-win").fool('window',{modal:true,'title':"收款详情",height:480,width:780,href:getRootPath()+'/paymentCheck/checkingWin?billCode=skd&billId='+id+'&_billCode='+_billCode});
}
// 费用详情
function fyxq(id){
	$('#scancel').length>0?$('#scancel').click():null;
	win = $("#pop-win").fool('window',{modal:true,'title':"费用详情",height:480,width:780,href:getRootPath()+'/paymentCheck/checkingWin?billCode=fyd&billId='+id+'&_billCode='+_billCode});
}

//资金计划
function zjjh(id){
	$('#scancel').length>0?$('#scancel').click():null;
	win = $("#pop-win").fool('window',{modal:true,'title':"资金计划",height:480,width:780,href:getRootPath()+'/capitalPlanDetail/window?relationId='+id+'&relationSign='+getBillSign(_billCode)+"&billType="+_billCode});
}

// 采购入库单、销售出库单、采购退货单、销售退货单增加核价功能
function pricingway(_id,_billCode){
	$('#pricing-box').window({
	title:'核价',
	top:100+$(document).scrollTop(),
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	href:getRootPath()+'/pricing/pricingUI?id='+_id+'&billCode='+_billCode,
	width:$(window).width()-200,
	height:$(window).height()-200
}); 
}

function myrefreshForm(){
	/* location.reload(); */
	var url = '';
	var flag = '';
	if($('#title1 h1').text().search(/新增/)!=-1 || $('#title1 h1').text().search(/复制/)!=-1 || $('#title1 h1').text().search(/编辑/)!=-1){
		flag = 'edit';
	}else{
		flag = 'detail';
	}
	var value = $('#fid').val();
	if(_billCode == 'xsfld'){
		url = getRootPath()+'/salesRebateRebBill/edit?flag='+flag+'&id='+value+'&billCode='+_billCode;
	}else if(_billCode == 'cgfld'){
		url = getRootPath()+'/purchaseRebBill/edit?flag='+flag+'&id='+value+'&billCode='+_billCode;
	}else if(_billCode == 'bom'){
		url = getRootPath()+'/bom/edit?flag='+flag+'&id='+value+'&billCode='+_billCode;
	}else{
		url = getRootPath()+'/warehouse/'+_billCode+'/edit?flag='+flag+'&id='+value+'&billCode='+_billCode;
	}
	$('#pop-win').length>0 && $('#pop-win').html()!=''?$('#pop-win').window('destroy'):null;
	$('#import-win').length>0 && $('#import-win').html()!=''?$('#import-win').window('destroy'):null;
	$('#pricing-box').length>0 && $('#pricing-box').html()!=''?$('#pricing-box').window('destroy'):null;
	$('#addBox').window({
		onLoad:function(){
			return ;
		}
	});
	$('#addBox').window("refresh",url);
}
function mycopyRow(value,billCode){
	var url=getRootPath()+'/warehouse/'+billCode+'/edit?mark=1&flag=copy&id='+value+'&billCode='+billCode;
	if(_billCode=="bom"){
		url=getRootPath()+'/bom/edit?mark=1&flag=copy&id='+value+'&billCode=bom';
	}else if(_billCode == 'xsfld'){
		url = getRootPath()+'/salesRebateRebBill/edit?mark=1&flag=copy&id='+value+'&billCode='+billCode;
	}else if(_billCode == 'cgfld'){
		url = getRootPath()+'/purchaseRebBill/edit?mark=1&flag=copy&id='+value+'&billCode='+billCode;
	}
	/* location.href=url; */
	// 解决核价单窗口没完全销毁，内容隐藏的问题
	if($('#pricing-box').html()!=''){
		$('#pricing-box').window('destroy');
	}
	$('#import-win').length>0 && $('#import-win').html()!=''?$('#import-win').window('destroy'):null;
	$(document).unbind('keydown');
	$('#pop-win').length>0 && $('#pop-win').html()!=''?$('#pop-win').window('destroy'):null;
	$('#import-win').length>0 && $('#import-win').html()!=''?$('#import-win').window('destroy'):null;
	$('#addBox').window("setTitle","复制订单");
	$('#addBox').window({
		onLoad:function(){
			return ;
		}
	});
	$('#addBox').window("refresh",url);
	
	return false;
}
// 详细页作废按钮
function mycancelVoid(value){
	var myurl = getRootPath()+'/warehouse/'+_billCode+'/cancel';
	if(_billCode == 'xsfld'){
		myurl = getRootPath()+'/salesRebateRebBill/cancel';
	}else if(_billCode == 'cgfld'){
		myurl = getRootPath()+'/purchaseRebBill/cancel';
	}
	$.fool.confirm({msg:'确定要作废此单据吗？',fn:function(r){
		if(r){
			 $.ajax({
					type : 'post',
					url : myurl,
					data : {id :value},
					dataType : 'json',
					success : function(data) {
						var myresult = data.returnCode;
						var mymsg = data.message;
						if(myresult == 0){
							$.fool.alert({time:1000,msg:'操作成功！',fn:function(){
								$("#void").remove();
								$('#refreshForm').remove();
								$('#skxq').remove();
								$('#fkxq').remove();
								$('#fyxq').remove();
								$('#mycollection').remove();
								$('#mypay').remove();
								$('#mysk').remove();
								$('#myfk').remove();
								$('#pricing').remove();
								$('.inlist').removeClass('status0 status1');
								$('.inlist').addClass('status2');
								$("#generate").remove();
								$("#dataTable").trigger("reloadGrid");
								$("#billList").trigger("reloadGrid");
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
							$.fool.alert({msg:'系统繁忙，请稍后再试'});
						}
		    		}
				});
		}
	},title:'确认'});
}

// 详情页保存按钮
function mysaveData(){
	if(!$("#list2").fool('fromVali')){
		$(document).unbind('keydown',editkd);
		setTimeout(function(){$(document).bind('keydown',editkd);},300);
		return false;
	}
	$(document).unbind('keydown',editkd);
	var details = $("#goodsList").jqGrid('getRowData');
	if(details.length<1){
		$.fool.alert({msg:'你还没有添加任何货品',fn:function(){
			$(document).unbind('keydown',editkd);
			$(document).bind('keydown',editkd);
		}});
		return false;
	}
	var _dataPanel = $('#goodsList');
	var _editing = _dataPanel.find(".editing-on");
	if(_editing.length>0){
		$.fool.alert({msg:'你还有没编辑完成的货品,请先确认！',fn:function(){
			$(document).unbind('keydown',editkd);
			$(document).bind('keydown',editkd);
		}});
		return false;
	}
	if(_billCode=="scjhd"){
		var _dataPanel2 = $('#materialList');
		var _editing2 = _dataPanel2.find(".editing-on");
		if(_editing2.length>0){
			$.fool.alert({msg:'你还有没编辑完成的材料,请先确认！'});
			return false;
		}
	}
	if(_billCode=="shd"||_billCode=='fhd'){
		var _dataPanel2 = $('#containerList');
		var _editing2 = _dataPanel2.find(".editing-on");
        if($("#describe").val().length > 200){
            $.fool.alert({msg:'备注过长，请修改！'});
            return false;
        };
		if(_editing2.length>0){
			$.fool.alert({msg:'你还有没编辑完成的材料,请先确认！'});
			return false;
		}
	}
//	$('#save').attr("disabled","disabled");
	// $('#goodsList').datagrid('deleteRow',myindex-1);
	// details = $("#goodsList").datagrid('getRows');
	if(_billCode=="scjhd"){
		var details2=$("#materialList").jqGrid('getRowData');
		for(var i=0;i<details2.length;i++){
			details2[i].detailType=1;
		}
		details=details.concat(details2);
	}
	var transportDetails="";
	if(_billCode=="shd"||_billCode=='fhd'){
		transportDetails=$("#containerList").jqGrid('getRowData');
	}
	var fdata = $("#warehousebillForm").serializeJson();
	fdata = $.extend(fdata,{'details':JSON.stringify(details),'transportDetails':JSON.stringify(transportDetails)});
	var url=getRootPath()+'/warehouse/'+_billCode+'/save';
	if(_billCode=="bom"){
		url=getRootPath()+'/bom/save';
		var newDetails=eval(fdata.details);
		for(var i in newDetails){
			newDetails[i].specId=newDetails[i].goodsSpecId;
			newDetails[i].type="";
			newDetails[i].totalAmount="";
		}
		fdata.details=JSON.stringify(newDetails);
	}
	if(_billCode=="shd"||_billCode=='fhd'){
		for(var i=0;i<details.length;i++){
			details[i].inWareHouseId=$("#inWareHouseId").val();
		}
		fdata.details=JSON.stringify(details);
	}
	$.ajax({
		url:url,
		type:"post",
		data:fdata,
		async:false,
		success:function(data){
			if(data.returnCode =='0'){
	    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
	    			$("#dataTable").trigger("reloadGrid");
					$("#billList").trigger("reloadGrid");
					$(document).unbind('keydown',editkd);
	    			$(document).bind('keydown',editkd);
	    			if($('#fid').val()==''){
	    				$('#fid').val(data.data);
	    				$('#updateTime').val(data.dataExt.updateTime);
	    				$('#recordStatus').val(0);
	    				mybtnFooterInit();
	    				hideSaveAllLink(true);
	    				if(localCache == "1"){
	    					clearInterval(mycookieSave);
	    					localStorage.removeItem(_billCode);
	    				}
	    			}else{
	    				$("#addBox").window("close");
	    			}
	    			return true;
	    		}});
	    	}else if(data.returnCode == '1'){
	    		if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
	    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
	    				$(document).unbind('keydown',editkd);
		    			$(document).bind('keydown',editkd);
	    			}});
	    		}else{
		    		$.fool.alert({msg:data.message,fn:function(){
		    			$(document).unbind('keydown',editkd);
		    			$(document).bind('keydown',editkd);
		    		}});
	    		}
	    		$('#save').removeAttr("disabled");
			}else{
	    		$.fool.alert({msg:'服务器繁忙，请稍后再试！',fn:function(){
	    			$(document).unbind('keydown',editkd);
	    			$(document).bind('keydown',editkd);
	    		}});
	    		return false;
			}
		}
	});
}

function fldsaveData(){
	$('#list2').form("enableValidation");
	if(!$("#list2").form('validate')){
		$(document).unbind('keydown',editkd);
		setTimeout(function(){$(document).bind('keydown',editkd);},300);
		return false;
	}
	$(document).unbind('keydown',editkd);
	var fdata = $("#warehousebillForm").serializeJson();
	if(_billCode == 'xsfld'){
		var url=getRootPath()+'/salesRebateRebBill/save';
		var _url=getRootPath()+'/salesRebateRebBill/checkExist'; 
	}else if(_billCode == 'cgfld'){
		var url=getRootPath()+'/purchaseRebBill/save';
		var _url=getRootPath()+'/purchaseRebBill/checkExist';
	}	
	 $.ajax({
			type : 'post',
			url : _url,
			data :fdata,
			async:false,
			dataType : 'json',
			success : function(data) {
			 if(data.returnCode==1){
					$.fool.confirm({msg:data.message,title:'提示',fn:function(r){
						if(r){
						 invoking(url,fdata);
						}
					}});
			 }else{
				 invoking(url,fdata); 
			 }			
 		},
		});	
}

function invoking(url,fdata){
	$.post(url,fdata,function(data){
		if(data.returnCode =='0'){
    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
    			$("#billList").trigger("reloadGrid");
				$("#dataTable").trigger("reloadGrid");
				$(document).unbind('keydown',editkd);
    			$(document).bind('keydown',editkd);
    			if($('#fid').val()==''){
    				$('#fid').val(data.dataExt.fid);
    				$('#updateTime').val(data.dataExt.updateTime);
    				$('#recordStatus').val(0);
    				mybtnFooterInit();
    				hideSaveAllLink(true);
    				if(localCache == "1"){
    					clearInterval(mycookieSave);
    					localStorage.removeItem(_billCode);
    				}
    			}else{
    				$("#addBox").window("close");
    			}
    			return true;
    		}});
    	}else if(data.returnCode == '1'){
    		if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.message,fn:function(){
    				$(document).unbind('keydown',editkd);
	    			$(document).bind('keydown',editkd);
    			}});
    		}else{
	    		$.fool.alert({msg:data.message,fn:function(){
	    			$(document).unbind('keydown',editkd);
	    			$(document).bind('keydown',editkd);
	    		}});
    		}
    		$('#save').removeAttr("disabled");
		}else{
    		$.fool.alert({msg:'服务器繁忙，请稍后再试！',fn:function(){
    			$(document).unbind('keydown',editkd);
    			$(document).bind('keydown',editkd);
    		}});
    		return false;
		}
    });
}

// 动画滑动到相应位置
function aniTo(id){
	$('#'+id).closest('.window-body').animate({scrollTop:$('#'+id).offset().top-$('#warehousebillForm').offset().top-76},500);
}

// 删除所有明细数据行
function clearAllData(){
		$('#goodsList').jqGrid('clearGridData',false);
}

function selectRelationNew(data){
	var ids="";
	var codes="";
	var _d = getData(data);
	var isXsdd = _d.code.search(/XSDD/)!=-1?true:false;
	$("#relationId").val("");
	$("#relation").val("");
	if(_billCode != 'cgdd'){
		$("#supplierId").val("");
		$("#supplierName").length>0?$("#supplierName").next()[0].comboObj.setComboText(""):null;
		$("#supplierCode").val("");
		$("#supplierPhone").val("");
		$("#customerId").val("");
		$("#customerName").length>0?$("#customerName").next()[0].comboObj.setComboText(""):null;
		$("#customerCode").val("");
		$("#customerPhone").val("");
		if(_billCode == 'cgrk'){
			$("#deptName").length>0?$('#deptName').next()[0].comboObj.setComboText(""):null;
			$('#deptId').val('');
			$("#inMemberName").length>0?$('#inMemberName').next()[0].comboObj.setComboText(""):null;
			$('#inMemberId').val('');
		}
	}
	if( _billCode != 'cgsqd'){
		clearAllData();
	}
	if(data.length>1){
		for(var i=0;i<data.length;i++){
			ids=data[i].fid+","+ids;
			codes=data[i].code+","+codes;
		};
		$("#relationId").val(ids);
		$("#relation").val(codes);
	}else{
		$("#relationId").val(_d.fid);
		$("#relation").val(_d.code);
	}
	
	if(_billCode != 'cgdd'){
		$("#supplierId").val(_d.supplierId); 
		$("#supplierName").length>0?$("#supplierName").next()[0].comboObj.setComboText(_d.supplierName):null;
		$("#supplierId").val(_d.supplierId);
		$("#supplierCode").val(_d.supplierCode);
		$("#supplierPhone").val(_d.supplierPhone);
		$("#customerId").val(_d.customerId);
		$("#customerName").length>0?$("#customerName").next()[0].comboObj.setComboText(_d.customerName):null;
		$("#customerId").val(_d.customerId);
		$("#customerCode").val(_d.customerCode);
		$("#customerPhone").val(_d.customerPhone);
		if(_billCode == 'cgrk'){
			$("#deptName").length>0?$('#deptName').next()[0].comboObj.setComboValue(_d.deptId):null;
			$('#deptId').val(_d.deptId);
			$("#inMemberName").length>0?$('#inMemberName').next()[0].comboObj.setComboText(_d.inMemberName):null;
			$('#inMemberId').val(_d.inMemberId);
		}
		if(_billCode == 'shd'){
			$('#transportNo').val(_d.transportNo);
			$("#deliveryPlaceName").length>0?$('#deliveryPlaceName').next()[0].comboObj.setComboText(_d.deliveryPlaceName):null;
			$('#deliveryPlaceId').val(_d.deliveryPlaceId);
			$.ajax({
			 	url:getRootPath()+'/freightAddress/queryByParentId?fid='+_d.receiptPlaceId,
				async:false,
			    data:{},
			    success:function(data){
			    	if(data){
			    		$("#receiptPlaceName").length>0?$('#receiptPlaceName').next()[0].comboObj.setComboText(data.name):null;
						$('#receiptPlaceId').val(data.fid);
			    	}else{
			    		$("#receiptPlaceName").length>0?$('#receiptPlaceName').next()[0].comboObj.setComboText(_d.receiptPlaceName):null;
						$('#receiptPlaceId').val(_d.receiptPlaceId);
			    	}
			    }
			});
			$.ajax({
				url:getRootPath()+'/api/freightAddress/getById?fid='+_d.receiptPlaceId,
				async:false,
				data:{},
				success:function(data){
					($("#inWareHouseName").next())[0].comboObj.setComboValue(data.assetwarehouseId);
					$("#inWareHouseId").val(data.assetwarehouseId);
				}
			});
			$("#transportTypeName").length>0?$('#transportTypeName').next()[0].comboObj.setComboText(_d.transportTypeName):null;
			$('#transportTypeId').val(_d.transportTypeId);
			$("#shipmentTypeName").length>0?$('#shipmentTypeName').next()[0].comboObj.setComboText(_d.shipmentTypeName):null;
			$('#shipmentTypeId').val(_d.shipmentTypeId);
			$("#carNo").next()[0].comboObj.setComboText(_d.carNo);
			/*$("#carNo").next()[0].comboObj.setComboValue(_d.carNo);*/
			$('#driverName').val(_d.driverName);
			$('#driverPhone').val(_d.driverPhone);
		}
	}
		
	if(data.length>1){
		if((_billCode != "scjhd" && _billCode != "cgdd" && _billCode != "cgsqd") || (_billCode == "cgdd" && !isXsdd)){
			$.ajax({
			url:getRootPath()+"/warehouse/xsdd/mergeBillDetails",
			type:"post",
			async:false,
			data:{billIds:ids},
			success:function(data){
				$("#goodsList").jqGrid('clearGridData',false);
				$("#goodsList")[0].addJSONData(data);
			    var rows=$("#goodsList").jqGrid('getRowData');
			    newIndex = rows.length;
			    mydetails = "";
			    for(var i=0;i<rows.length;i++){
			    	editNew(i+1);
			    	getTableEditor(i+1,'_isNew').val(true);
			    	if(_billCode=="xsth"||_billCode=="xsch")getTableEditor(i+1,'regoodsId').val(data[i].goodsId);
			    	// getTableEditor(i,'goodsCode').combogrid('setValue',data[i].goodsCode);//编号栏获取数据
			    }
			}
			});
		}else if(_billCode=="scjhd"){
			$.ajax({
				url:getRootPath()+"/warehouse/xsdd/mergeBillDetails",
				type:"post",
				async:false,
				data:{billIds:ids},
				success:function(data){
					for(var k=0;k<data.length;k++){
						if(data[k].totalAmount==0){
							data[k].totalAmount=data[k].type;
						}
						data[k]._unitName=data[k].unitName;// 校验单位需要
					}
					$("#goodsList").jqGrid('clearGridData',false);
					$("#goodsList")[0].addJSONData(data);
					$("#materialList").jqGrid('clearGridData',false);
					var rows=$("#goodsList").jqGrid('getRowData');
					mydetails = "";
					newIndex = rows.length;
				    for(var i=0;i<rows.length;i++){
				    	editNew(i+1);
				    	getTableEditor(i+1,'_isNew').val(true);
				    }
				}
			});
		}
	}else{
		if(_billCode=="scjhd"||_billCode=="cprk"||_billCode=="scll"||_billCode=='cgfp'||_billCode=='xsfp'||_billCode=='cgrk'||_billCode=='cgth'||_billCode=='xsch'||_billCode=='xsth'||_billCode=='sctl'||_billCode=='cptk'||_billCode=='xsdd'||(_billCode=='cgdd'&&!isXsdd)){
			var id = _d.fid;
			var onlyMaterial = _billCode=="scll"||_billCode=="sctl"?1:_billCode=="cprk"||_billCode=="cptk"?2:"";
			$.ajax({
				url:getRootPath()+"/warehouse/"+_billCode+"/mergeBillDetails?onlyMaterial="+onlyMaterial,// onlyMaterial生产计划单1:材料
																											// 2:货品
				type:"post",
				async:false,
				data:{billIds:id},
				success:function(data){
					for(var k=0;k<data.length;k++){
						if(data[k].totalAmount==0){
							data[k].totalAmount=data[k].type;
						}
						data[k]._unitName=data[k].unitName;// 校验单位需要
					}
					$("#goodsList").jqGrid('clearGridData',false);
					$("#goodsList")[0].addJSONData(data);
					$("#materialList").jqGrid('clearGridData',false);
				    var rows=$("#goodsList").jqGrid('getRowData');
				    // var editor="";
				    mydetails = "";
				    newIndex = rows.length;
				    for(var i=0;i<rows.length;i++){
				    	// $("#goodsList").datagrid('beginEdit',i);
				    	editNew(i+1);
				    	getTableEditor(i+1,'_isNew').val(true);
				    	if(_billCode=="xsth"||_billCode=="xsch")getTableEditor(i+1,'regoodsId').val(data[i].goodsId);
				    	// getTableEditor(i,'goodsCode').combogrid('setValue',data[i].goodsCode);//编号栏获取数据
				    	// getTableEditor(i,'goodsSpecName').combogrid('setValue',data[i].goodsSpecName);
				    }
				}});
		    
		}
		if(_billCode=="shd"){
			var id = _d.fid;
			$.ajax({
				url:getRootPath()+"/warehouse/"+_billCode+"/billDetails",
				type:"post",
				async:false,
				data:{billId:id},
				success:function(data){
					for(var k=0;k<data.length;k++){
						if(data[k].totalAmount==0){
							data[k].totalAmount=data[k].type;
						}
						data[k].refDetailId=data[k].fid;
						data[k]._unitName=data[k].unitName;// 校验单位需要
						data[k].receivedQuantity=data[k].quentity;
					}
					$("#goodsList").jqGrid('clearGridData',false);
					$("#goodsList")[0].addJSONData(data);
					$("#materialList").jqGrid('clearGridData',false);
				    var rows=$("#goodsList").jqGrid('getRowData');
				    mydetails = "";
				    newIndex = rows.length;
				    for(var i=0;i<rows.length;i++){
				    	editNew(i+1);
				    	getTableEditor(i+1,'_isNew').val(true);
				    }
				}
			});
			$.ajax({
				url:getRootPath()+"/warehouse/"+_billCode+"/transportBillDetails",
				type:"post",
				async:false,
				data:{billId:id}, 
				success:function(data){
					$("#containerList").jqGrid('clearGridData',false);
					$("#containerList")[0].addJSONData(data);
					var rows=$("#containerList").jqGrid('getRowData');
				    for(var i=0;i<rows.length;i++){
				    	editContainerNew(i+1);
				    	/*getTableEditor(i+1,'_isNew',1).val(true);*/
				    }
				}
			});
		}
	}
	if(_billCode != 'cgdd'){
		disableRelation(true);
	}
	inputFocusHelp();
	// $("#relation").validatebox('validate');
	closeWin();
	$("#list2").fool('fromVali');
}

function nextNodeSearcher(node,$tree,$ed){
	var parent=$tree.tree("getParent",node.target);
	if(!parent){
		var root=$tree.tree("getRoot");
		$ed.combotree("setValue",root.children[0].id);
		$tree.tree("scrollTo",root.children[0].target);
		$(root.children[0].target).click();
		return false;
	}
	var childs=parent.children;
	for(var i=0;i<childs.length;i++){
		if(childs[i].id==node.id){
			if(i+1==childs.length){
				nextNodeSearcher(parent,$tree,$ed);
			}else{
				$ed.combotree("setValue",parent.children[i+1].id);
				$tree.tree("scrollTo",parent.children[i+1].target);
				$(parent.children[i+1].target).click();
			}
		}
	}
}
function getLast(childs,$ed,$tree){
	var length=childs.length;
	if(childs[length-1].children.length>0){
		getLast(childs[length-1].children,$ed,$tree);
	}else{
		$ed.combotree("setValue",childs[length-1].id);
		$tree.tree("scrollTo",childs[length-1].target);
		$(childs[length-1].target).click();
		return;
	}
}

// 详细页。关闭窗口方法
function closeWin(){
	if(win)	win.window('close').window('clear');
}

// 获取数据
function getData(data){
	return getDataTop1(data);
}

// 详细页。货品列表仓库
function wareHouseAction(value,options,row){
	return row._inWareHouseName?row._inWareHouseName:value;
}
// 获取表格里面某个编辑器方法
function getTableEditor(index,name,flag){
	// return getTableEditorHelp($("#goodsList"),index,field);
	if(flag==1){
		return $('#containerList').find("tr.jqgrow #"+index+"_"+name);
	}else{
		return $('#goodsList').find("tr.jqgrow #"+index+"_"+name);
	}
}
// 获取表格里面某个编辑器方法
function getTableEditor2(index,name){
	// return getTableEditorHelp($("#materialList"),index,field);
	return $('#materialList').find("tr.jqgrow #"+index+"_"+name);
}

/*
 * function getTableEditorHelp(tbId,index,field){ var $t =$.fool._get$(tbId);
 * return $t.fool('getEditor$',{'index':index,'field':field}); }
 */
// 详细页。货品列表取消按钮
function cancel(index,flag){
	var ind = index;
	if(flag=="1"){
		var _isNew = getTableEditor(ind,'_isNew',"1").val();
		if(_isNew=='true'||_isNew==true){
			$.fool.confirm({msg:'您确定要撤销该记录？',fn:function(r){
				if(r) {
					$("#containerList").jqGrid('delRowData',ind);
				}
			}});
		}else{
				unitData_[ind] = myunitData[ind];
				$('#containerList').jqGrid('restoreRow', ind);
				$('#containerList').jqGrid('setRowData', ind, {
					editing:false,
					action:null
				});
		}
	}else{
		var _isNew = getTableEditor(ind,'_isNew').val();
		if(_isNew=='true'||_isNew==true){
			$.fool.confirm({msg:'您确定要撤销该记录？',fn:function(r){
				if(r) {
					$("#goodsList").jqGrid('delRowData',ind);
					hideSaveAllLink();
					getTotalNew();
				}
			}});
		}else{
				unitData_[ind] = myunitData[ind];
				$('#goodsList').jqGrid('restoreRow', ind);
				$('#goodsList').jqGrid('setRowData', ind, {
					editing:false,
					action:null
				});
				hideSaveAllLink();
		}
	}
}
// 详细页。货品保存。保存所有按钮
function saveAll(){
	saveAll_ = true;
	$("a.editing-on").click();
	// var _dataPanel = $('#goodsList').datagrid('getPanel');
	// var _editing = _dataPanel.find(".editing-on");
	if(getEditingOnSize()>0){
		$.fool.alert({msg:'还有未填完的货品信息，请检查',fn:function(){
			saveAll_ = false;
		}});
	}else{
		hideSaveAllLink(true);
	}
}
function getEditingOnSize(){
	var _dataPanel = $('#goodsList');
	var _editing = _dataPanel.find(".editing-on");
	return _editing.length;
}
function getEditingOnSize2(){
	var _dataPanel = $('#materialList');
	var _editing = _dataPanel.find(".editing-on");
	return _editing.length;
}
// 隐藏/显示保全全部按钮
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
}
// 详细页。货品列表验证帮助方法
function validDetailHelp(ind,billCode){
	// 启动验证
	var editor$ = getTableEditor(ind,'goodsSpecName');
	if(!$(editor$.next()[0].comboObj.getInput()).attr('disabled')){
		try{
			$(editor$.next()[0].comboObj.getInput()).validatebox('enableValidation');
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
	
		editor$ = getTableEditor(ind,'unitName');
		try{
			$(editor$.next()[0].comboObj.getInput()).validatebox('enableValidation');
		}catch(e){
			
		}
	
	editor$ = getTableEditor(ind,'costPrice');
	if(editor$.parents().css("display")!="none"){
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'type');
	if(editor$.parents().css("display")!="none"){
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'taxRate');
	if(editor$.parents().css("display")!="none"){
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'totalAmount');
	if(editor$.parents().css("display")!="none"){
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'inWareHouseName');
	if(editor$.parents().css("display")!="none"){
		try{
			$(editor$.next()[0].comboObj.getInput()).validatebox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'goodsCode');
	if(editor$.parents().css("display")!="none"){
		try{
			$(editor$.next()[0].comboObj.getInput()).validatebox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor(ind,'quentity');
	if(editor$.parents().css("display")!="none"){
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
}

function validDetailHelp2(ind,billCode){
	// 启动验证
	var editor$ = getTableEditor2(ind,'goodsSpecName');
	if(!$(editor$.next()[0].comboObj.getInput()).attr('disabled')){
		try{
			$(editor$.next()[0].comboObj.getInput()).validatebox('enableValidation');
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
	
	editor$ = getTableEditor2(ind,'unitName');
	try{
		$(editor$.next()[0].comboObj.getInput()).validatebox('enableValidation');
	}catch(e){
		
	}
	
	editor$ = getTableEditor2(ind,'costPrice');
	if(editor$.parents().css("display")!="none"){
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor2(ind,'inWareHouseName');
	if(editor$.parents().css("display")!="none"){
		try{
			$(editor$.next()[0].comboObj.getInput()).validatebox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor2(ind,'goodsCode');
	if(editor$.parents().css("display")!="none"){
		try{
			$(editor$.next()[0].comboObj.getInput()).validatebox('enableValidation');
		}catch(e){
			
		}
	}
	editor$ = getTableEditor2(ind,'quentity');
	if(editor$.parents().css("display")!="none"){
		try{
			editor$.numberbox('enableValidation');
		}catch(e){
			
		}
	}
}
// 详细页 属性跟单位改变 修改 最低销售价、参考单价
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
			
			var panel = $('#goodsList');// 获取货品列表
			var _unitPrice=data.referencePrice;// 货品单价
			if(data.referencePrice=='0E-8'||data.referencePrice==0||data.referencePrice=="0"){
			    _unitPrice=0;// 单价
			}		     	
			var _quentity=getTableEditor(index,'quentity').numberbox('getText');// 数量
			var _type=parseFloat(_unitPrice)*parseFloat(_quentity).toFixed(2);// 税前金额
			 panel.find('tr#'+index+' input#'+index+'_type').numberbox('setValue',_type);
			// 判断是修改状态
			if(_billCode=="cgfp"||_billCode=="xsfp"){	
			 var taxAmount$=getTextEditor(index,'taxAmount');// 税金编辑器
			 var totalAmount$=getTextEditor(index,'totalAmount');// 税后金额编辑器
			 var _taxRate=getTableEditor(index,'taxRate').numberbox('getText');// 税点
			 var _taxAmount=parseFloat(_type)*parseFloat(_taxRate)/100;// 税金
			 var _totalAmount=parseFloat(_taxAmount)+parseFloat(_type);// 税后金额
			  panel.find('tr#'+index+' input#'+index+'_taxAmount').numberbox('setValue',_taxAmount);
			  panel.find('tr#'+index+' input#'+index+'_totalAmount').numberbox('setValue',_totalAmount);
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
}
function inputFocusHelp(){
	if(_editor)_editor.focus();
}
// 初始化单位数据
/*
 * function initUnitData(b,d){
 * $.getJSON(getRootPath()+'/unitController/getLeafUnit',function(data){
 * _unitData = data; //if(b)initGoodsListTable(d); }); }
 */

// 初始化仓库数据
/*
 * function initWareHouse(b){
 * $.getJSON(getRootPath()+'/basedata/warehourseList?'+Math.random(),function(data){
 * _wareHouse = data; //initUnitData(b); //if(b)initGoodsListTable(d); }); }
 */
/*
 * function editData(){
 * $.post(getRootPath()+"/basedata/query",{param:"AuxiliaryAttr_Warehouse"},function(data){
 * warehouseData = data.AuxiliaryAttr_Warehouse; }) }
 */
/**
 * 预加载数据 Uninitialized 未初始化 Loading 载入 Loaded 载入完成 Interactive 交互 complete 完成
 */
function preLoading(){
	if(document.readyState == "complete"){ // 当页面加载状态
		/*
		 * if(_unitData==null){
		 *  }
		 */
		/*
		 * initWareHouse(false); initUnitData(false);
		 */
		// editData();
		
	}
}

// 收款按钮
function receiver(billId,billType){
	win = $("#pop-win").fool('window',{modal:true,'title':"收款",height:450,width:350,href:getRootPath()+'/warehouse/'+billType+'/billCreater?billId='+billId+'&billType='+billType});
}
// 付款按钮
function payer(billId,billType){
	win = $("#pop-win").fool('window',{modal:true,'title':"付款",height:450,width:350,href:getRootPath()+'/warehouse/'+billType+'/billCreater?billId='+billId+'&billType='+billType});
}