
/**
 * 仓储编辑页面Load
 * 原warehouseEdit.JS不再每次编辑都加载，放在管理页面加载
 * 必须编辑页面加载时的JS集中写在这里
 */
var win,win2,_editor;
var natureId='';// 货品属性id
var goodsId='';// 货品id
/* var number="";//规格id */
var datalist="";// 货品材料明细
var editkd = '';// ctrl快捷键全局变量
//var addkd = '';// +快捷键全局变量
var mycookieSave = '';
var localCache = $('#localCache').val();
var myunitData = [];
var barCodeKey = 0; //标识是否为barCode获取的商品
var barCodeKey2 = 0;//标识是否为barCode获取的材料
//var selectTips = 0;

//全局变量
var goodsIndex = '';
var actionC = [];   // 标识是不是编辑状态
var actionCl = [];  // 标识是否点击了操作单元格
var unitData_ = [];
var mydetails = '';
var myobj = [];
var mynum = 0;
var saveAll_ = false; // 判断是否一键保存，决定是否弹出下拉框

//控件数据变量
//AuxiliaryAttr_Warehouse仓库; Organization部门; Member人员 ; Supplier供应商 ; Customer客户 ; Goods货品 
var organization="";
var member="";
var supplier="";
var customer="";
var goods="";
var warehouse="",bankData="";
var carNoValue='';//船号获取司机信息
//加载控件数据
$.ajax({
	url:getRootPath()+"/api/vehicleInformation/vagueSearch?searchSize=10&num="+Math.random(),
	async:false,
	data:{},
	success:function(data){	
		carNoValue=formatData(data,"licenseNumber");	   
    }
	});

$.ajax({
	url:getRootPath()+"/basedata/query?num="+Math.random(),
	async:false,
    data:{param:"Organization,Member,Supplier,Customer,Goods,AuxiliaryAttr_Warehouse"},
    success:function(data){
    	organization=formatTree(data.Organization[0].children,"text","id");
    	member=formatData(data.Member,"fid");
    	supplier=formatData(data.Supplier,"fid");
    	customer=formatData(data.Customer,"fid");
    	goods=formatData(data.Goods,"fid");
    	warehouse=formatTree(data.AuxiliaryAttr_Warehouse[0].children,"text","id");
    }
});
$.ajax({
	url:getRootPath()+'/bankController/list?num='+Math.random(),
	async:false,
	data:{},
	success:function(data){
		bankData=formatData(data.rows,"fid");
    }
	});

var _billCode,_billCodeName,_unitData=null,_flag,_wareHouse=null,_billStat;
$(function(){$(function() {
	// 解决窗口未完全销毁出现的多个快捷键操作叠加问题
	if($('#addBox').length>0){
		var options = $('#addBox').window('options');
		options = $.extend(options,{onClose:function(){
			if($("#pop-win").length>0 && $("#pop-win").html()){
				$("#pop-win").window("destroy");
			}
			if(typeof(editkd) != "undefined"){
				$(document).unbind('keydown',editkd);
			}
			/*if(typeof(addkd) != "undefined"){
				$(document).unbind('keydown',addkd);
			}*/
			if(_billCode == 'cgdd' && "undefined" != typeof idsTips){
				if(idsTips){
					window.location.href=getRootPath()+'/warehouse/cgdd/manage';
					/*var tab = parent.$('#tabs').tabs('getTab','采购订单');
					tab.find('iframe').attr('src',getRootPath()+'/warehouse/cgdd/manage');*/
					idsTips=false;
				}
			}
			// 解决核价单窗口没完全销毁，内容隐藏的问题
			if($('#pricing-box').html()!=''){
				$('#pricing-box').window('destroy');
			}
			$('html').css('overflow','');
			//解决滑动条消失后再显示出现不能用滑轮滚动的问题
			/*parent.$("#tabs").tabs("unselect",_billCodeName);
			setTimeout(function(){parent.$("#tabs").tabs("select",_billCodeName);},100);*/
			if(localCache == "1"){
				clearInterval(mycookieSave);
			}
		}});
		if(('#relation').length>0){$('#relation').attr('readonly','readonly');}//统一设置关联单据输入框只读
		$('#addBox').window(options);
		$('html').css('overflow','hidden');// 解决combo下拉框闪移问题
		$('.window-mask').css("width",$('.window-mask').width()-17);
		$('#code').attr('readonly','readonly');// 编辑状态单号不能编辑
		$('#hide').attr('href','javascript:;');// 单据信息回顶部按钮加点击提示
		$('.clearBill').click(function(){
			if($("#relation").val()){
				hideSaveAllLink(true);
				$("#relationId").val("");
				$("#relation").val('');
				if(_billCode!='cgdd'){
					$("#supplierId").val('');
					$("#supplierName").length>0?$("#supplierName").next()[0].comboObj.setComboText(""):null;
					$("#supplierId").val('');
					$("#supplierCode").val('');
					$("#supplierPhone").val('');
					$("#customerId").val('');
					$("#customerName").length>0?$("#customerName").next()[0].comboObj.setComboText(""):null;
					$("#customerId").val('');
					$("#customerCode").val('');
					$("#customerPhone").val('');
					if(_billCode == 'cgrk'){
						$("#deptName").length>0?$('#deptName').next()[0].comboObj.setComboText(""):null;
						$('#deptId').val('');
						$("#inMemberName").length>0?$('#inMemberName').next()[0].comboObj.setComboText(""):null;
						$('#inMemberId').val('');
					}
				}
				if(_billCode != 'cgsqd'){
					if(_billCode == 'scjhd'){//清空生产计划单明细
							$('#materialList').jqGrid('clearGridData',false);
					}
					clearAllData();
				}
			}
			disableRelation(false);
		});
		// 返回按钮指向链接
		$('#square1 .backBtn').click(function(){
			$("#addBox").window("close");
			if($("#checkBox").length>0 && $('#checkBox').html()){
				$("#checkBox").window("destroy");
			}
			if($("#pop-win").length>0 && $("#pop-win").html()){
				$("#pop-win").window("destroy");
			}
			if($("#import-win").length>0 && $("#import-win").html()){
				$('#import-win').window("destroy");
			}
		});
		
		// 屏蔽默认快捷键
		$(document).keydown(function(e){
			if(e.ctrlKey && e.keyCode == 83){// ctrl+s
				if (e.preventDefault) {  // firefox
		            e.preventDefault();
		        } else { // other
		        	e.keyCode = 0;
		            e.returnValue = false;
		        }
			}
		});
		
		// 编辑页面保存按钮,列表撤销按钮快捷键
		$(document).keydown(editkd=function(e){
				if(e.ctrlKey && e.keyCode == 83){// ctrl+s
					$('#save').click();
				}
		});
		// +快捷键新增一行
		/*$(document).keydown(addkd=function(e){
			if(e.keyCode == 107){
				if($('#recordStatus').val() != 1 && $('#recordStatus').val() != 2){
					$(document).unbind('keydown',addkd);
					$('#addrow').click();
				}
			}
		});*/
		_billCode == 'cgfld' || _billCode == 'xsfld'?scrollDiy():null;	
		// 采购返利单和销售返利单填写金额自动同步计算返利率
		if(_billCode == 'cgfld' || _billCode == 'xsfld'){
			$('#sales').val()!=0?$('#rates').val((100*($('#amount').val()/$('#sales').val())).toFixed(2)):null;
		    var testinput = document.createElement('input');      
		    if('oninput' in testinput){  // 对支持oninput的浏览器用oninput，其他浏览器（IE6/7/8）使用onpropertychange：
		        $('#amount').bind("input",function(){
		        	rebatelv();
		        });  
		    }else{  
		    	$('#amount').bind('propertychange',function(){
		    		rebatelv();
		    	});  
		    }  
		}	
	}
});
});
//页面加载后初始化
document.onreadystatechange = preLoading; 

