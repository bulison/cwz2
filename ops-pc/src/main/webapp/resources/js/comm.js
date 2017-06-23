

//打印仓库单据
function printBillDetail(id,billCode,flag,pageRow){
	flag = 0;//开发时默认预览，投产请注释掉
	var myurl = getRootPath()+'/warehouse/'+billCode+'/print?billCode='+billCode+'&id='+id+'&code='+billCode;
	if(billCode == 'xsfld'){
		myurl = getRootPath()+'/salesRebateRebBill/xsfldPrint?billCode='+billCode+'&id='+id+'&code='+billCode;
	}else if(billCode == 'cgfld'){
		myurl = getRootPath()+'/purchaseRebBill/cgfldPrint?billCode='+billCode+'&id='+id+'&code='+billCode;
	}else if(billCode == 'fkd'){
		myurl=getRootPath()+"/payBill/fkdPrint?id="+id;
	}else if(billCode == 'skd'){
		myurl=getRootPath()+"/payBill/skdPrint?id="+id;
	}else if(billCode == 'fyd'){
		myurl=getRootPath()+"/costBill/print?id="+id;
	}
	if(!id)
		$.fool.alert({msg:'编号错误！'});
	else
		printHandle(myurl,flag,pageRow);
}

//打印凭证
function printVoucher(id,flag,pageRow){
	flag = 0;//开发时默认预览，投产请注释掉
	if(!id)
		$.fool.alert({msg:'编号错误！'});
	else
		printHandle(getRootPath()+'/voucher/print?&id='+id+'&code=dypz',flag,pageRow);
}

//工资打印条打印
function printWage(wageDate,deptId,memberId,pageRow){
	flag = 0;//开发时默认预览，投产请注释掉
	if(!wageDate){
		wageDate="";
	};
	if(!deptId){
		deptId="";
	};
	if(!memberId){
		memberId="";
	};
	printHandle(getRootPath()+'/wage/wagePrinter?wageDate='+wageDate+'&deptId='+deptId+'&memberId='+memberId+'&code=gzdyt',flag,pageRow);
}

//工资发放打印
function printWageIssue(wageDate,deptId,memberId,pageRow){
	flag = 0;//开发时默认预览，投产请注释掉
	if(!wageDate){
		wageDate="";
	};
	if(!deptId){
		deptId="";
	};
	if(!memberId){
		memberId="";
	};
	printHandle(getRootPath()+'/wage/wageIssuePrinter?wageDate='+wageDate+'&deptId='+deptId+'&memberId='+memberId+'&code=gzff',flag,pageRow);
}

//工资统计打印
function printWageStat(wageDate,deptId,pageRow){
	flag = 0;//开发时默认预览，投产请注释掉
	if(!wageDate){
		wageDate="";
	};
	if(!deptId){
		deptId="";
	};
	printHandle(getRootPath()+'/wage/wageStatPrinter?year='+wageDate+'&deptId='+deptId+'&code=gztj',flag,pageRow);
}

//工资表汇总打印
function printSum(wageDate,deptId,pageRow){
	flag = 0;//开发时默认预览，投产请注释掉
	if(!wageDate){
		wageDate="";
	};
	if(!deptId){
		deptId="";
	};
	printHandle(getRootPath()+'/wage/wageSumPrinter?wageDate='+wageDate+'&deptId='+deptId+'&code=gzbhz',flag,pageRow);
}

//应付应收分析打印
function printReport(details,type,pageRow,height){
	flag = 0;//开发时默认预览，投产请注释掉
	if(!details){
		details="";
	};
	var orgName=encodeURI(encodeURI(details.detail_1));
	if(type=="payment"){
		printHandle(getRootPath()+'/paymentReport/printAccountCheck?orgName='+orgName+'&details='+encodeURI(JSON.stringify(details))+'&code=yffx',flag,pageRow,height);	
	}else if(type=="receivable"){
		printHandle(getRootPath()+'/receivableReport/printAccountCheck?orgName='+orgName+'&details='+encodeURI(JSON.stringify(details))+'&code=ysfx',flag,pageRow,height);
	}
}

var LODOP = null;

function createPrint(row,height){
	LODOP=getLodop();  
	LODOP.PRINT_INIT("开始打印");
	//alert(document.getElementById("warp-div").innerHTML);
	var pageSize = 140;
	var pageHeight=139.7;
	if(row != undefined && row!=''){
		 pageSize = parseInt(140/22)*(row+1);	
	}
	if(height!= undefined && height!=''){
		pageHeight=height;
	}
	LODOP.ADD_PRINT_TABLE(0, 0,"200mm",pageSize+"mm", document.getElementById("warp-div").innerHTML);
	
	LODOP.SET_PRINT_PAGESIZE(0,"241mm",pageHeight+"mm","");
	return;
	
	LODOP.ADD_PRINT_HTM('0%', '0%', 'RightMargin:0.1mm',120, document.getElementById("header").innerHTML);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"Horient",2);
	
	//LODOP.ADD_PRINT_HTM('20%', '5%', '90%', "69%", document.getElementById("print-area1").innerHTML);
	LODOP.ADD_PRINT_TABLE('10%', '0%', 'RightMargin:0.1mm', "BottomMargin:0.1mm", document.getElementById("print-area1").innerHTML);	
   //LODOP.SET_PRINT_STYLEA(0,"FontSize",15);
	//LODOP.SET_PRINT_STYLEA(0,"ItemType",4);
	//LODOP.SET_PRINT_STYLEA(0,"Horient",3);
	//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
	
	LODOP.ADD_PRINT_TEXT("0", "0", 'RightMargin:0.1mm',"BottomMargin:0.1mm","第#页/共&页");
	LODOP.SET_PRINT_STYLEA(0,"ItemType",3);
	//LODOP.SET_PRINT_STYLEA(0,"Horient",1);
	//LODOP.SET_PRINT_STYLEA(0,"Vorient",1); 
	
	
};

function $getPrintArea(){
	var _area = $("#the-print-area");
	if(_area[0]==undefined)
		_area = $("<div id='the-print-area' style='display:none;'></div>").appendTo("body");
	else
		_area.html('');
	_area.hide();
	return _area;
}

function printHandle(url,flag,pageRow,height){
	$getPrintArea().load(url,function(){
		if(pageRow==undefined){
			createPrint(height);
		}else{
			createPrint(pageRow,height);
		}
		
		//LODOP.SET_SHOW_MODE("PREVIEW_IN_BROWSE",true);
		if(0==flag)LODOP.PREVIEW();
		else LODOP.PRINT();
		$(this).html('');
	});	
}
//打印

//表格日期格式化
function dateFormatAction(value,options,row){
	return getDateStr(value);
}
function codeLink(value,row){
	return "<a href='#' onclick='viewRow(\""+row.fid+"\",\""+row.recordStatus+"\")'>"+value+"</a>";
}
//新模板单号按钮链接
function codeLinkNew(value,options,row){
	return "<a href='javascript:;' onclick='viewRowNew(\""+row.fid+"\",\""+row.recordStatus+"\")'>"+value+"</a>";
}
//获取当天
function getCurrDate() {
	return new Date().format('yyyy-MM-dd');
};

//刷新列表页
function refreshData(value,name,sort){
	var options = $("#search-form").serializeJson();
	typeof options.recordStatus == "undefined"?options.recordStatus = "":null;//由于取消选择状态后，options没有录入参数recordStatus，但jqGrid的GridParam依然存在，所以设置一下
	typeof options.enable == "undefined"?options.enable = "":null;
	$("#search-code").textbox("clear");
	options.codeOrVoucherCode = "";
	$('#dataTable').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	$('#billList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	return false;
}
/*
 * 新版搜索下拉面板功能控制
 */
//新版搜索功能代码
var chechbox='';
var decide=true;
$(":checkbox").click(function(){//点击复选框并且只让一个选中
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	 chechbox=$(this).val();
    /*if(decide==true){
      chechbox=$(this).val();
      decide=false;
     }else{
    	chechbox='';
    	decide=true;
     }	*/
});

//点击下拉按钮
$('.button_a').click(function(){
	$('.input_div').toggle(2);	
	var s=$('.button_a').attr('class'); 
	if(s=="button_a roed"){
		$('.button_a').removeClass('roed');	
	}else{
		$(this).addClass('roed');
	}
});

//点击关闭隐藏
$('#clear-btndiv').click(function(){
	$('.input_div').hide(2);
	$('.button_a').removeClass('roed');
});
function cleanBoxInput(obj){
	$(obj).form('clear');
	var inputs=$("#search-form").find(".dhxDiv");
	 for(var i=0;i<inputs.length;i++){
		 inputs[i].comboObj.setComboText(null);
	 } 
}
//全局查询
$('.Inquiry').click(function(){
	var code=$("#search-code").textbox('getValue');
	cleanBoxInput($("#search-form"));
	var options=$.extend($("#search-form").serializeJson(),{"codeOrVoucherCode":code});
	typeof options.recordStatus == "undefined"?options.recordStatus = "":null;//由于取消选择状态后，options没有录入参数recordStatus，但jqGrid的GridParam依然存在，所以设置一下
	typeof options.enable == "undefined"?options.enable = "":null;
	$('#dataTable').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	$('#billList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
});

//鼠标获取焦点
$('#bolting').focus(function (){ 
	$('.input_div').show(2);
	$('.button_a').addClass('roed');
});

$("#search-code").textbox({
	prompt:$('.titleCustom h1').text() == "采购申请单"||'发货单'||'收货单'?'单号':'单号或原始单号',
	width:160,
	height:32
});
/*
 * 新版下拉搜索功能控制结束
 */

//获取DataGrid返回JSON对象集合的第一条数据
function getDataTop1(data){
	var _d = data;
	if(typeof data[0]!='undefined'){
		_d = data[0];
	}
	return _d;
}

//文本框初始化 文本框必须设置 _class 属性
function inputInit(obj){
	var $t = $.fool.get$(obj);
	var boxWidth=167,boxHeight=34;// 统一设置输入框大小
	var _cla = $t.attr('_class');
	if(_cla=='datebox'){//搜索页
		$t.datebox({width:160,height:30,editable:false});
	}else if(_cla=='datebox_d'){//详情页
		$t.datebox({width:160,height:30,editable:false});
	}else if(_cla=='datebox_curr'){//日期带当天默认值
		var _isNull = $t.val().length==0;
		$t.datebox({width:160,height:30,editable:false});
		if(_isNull)
			$t.datebox('setValue',getCurrDate());
	}else if(_cla=='datebox_end'){//结束不能小于开始日期,data-options必须要设置startId,startId为开始日期控件的ID
		var $opt = $.fool._opts$($t);
		var _isNull = $t.val().length==0;
		if(!$opt||!$opt.startId){
			$.fool.alert({msg:"你没有设置开始日期文本框的值,请在该组件data-options设置startId的值为开始日期控件的id值"});
			return;
		}
		_isNull = $opt.currTime ? true : false;
		$t.datebox({width:160,height:30,editable:false,}).datebox('calendar').calendar({
			validator:function (date){
				/* var now = new Date();
		    	var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
		    	var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate()+10);
		    	return d1<=date && date<=d2; */
		    	/* startTime2 = $(param[0]).datetimebox('getValue'); 
		    	var d1 = $.fn.datebox.defaults.parser(startTime2); 
		    	var d2 = $.fn.datebox.defaults.parser(value); 
		    	varify=d2>d1; 
		    	return varify;  */
		    	
		    	var start = $("#"+$opt.startId).datebox("getValue");
		    	var d1 = $.fn.datebox.defaults.parser(start);
		    	return d1<=date;
			}
		});
		if(_isNull)$t.datebox('setValue',getCurrDate());
	}else if(_cla=='datebox_d_curr'){//详情页,带当天默认值
		var _isNull = $t.val().length==0;
		$t.datebox({width:160,height:31,editable:false});
		if(_isNull)
			$t.datebox('setValue',getCurrDate());
	}else if(_cla=='textbox'){
		$t.textbox({width:160,height:30});
	}else if(_cla=='validatebox'){
		$t.validatebox();
	}else if(_cla=='combobox'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({width:160,height:30},$opt);
		$t.combobox($opt);
	}else if(_cla=='customer-combogrid'){
		var $opt = $.fool._opts$($t);
		//scusData.unshift({value:"",text:{name:""}});
		$opt = $.extend({
			required:true,
			novalidate:true,
			prompt:"客户",
			toolsBar:{
				refresh:true
			},
			width:boxWidth,
			height:boxHeight,
			data:scusData,
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
            onChange:function(value,text){
				$("#search-cusId").val(value);
				$("#select-cusId").val(value);
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='inmember-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true,
			novalidate:true,
			width:boxWidth,
			height:boxHeight,
			data:smemData,
			prompt:"人员",
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
				$('#select-peopleid').val(value);
				$('#select-inMemberId').val(value);
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='supplier-combogrid'){
		var $opt = $.fool._opts$($t);
		//ssupData.unshift({value:"",text:{name:""}});
		$opt = $.extend({
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
			    $("#select-supplierId").val(value);
		   }
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='outmember-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true,
			prompt:"人员",
			novalidate:true,
			width:boxWidth,
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
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='warehouse'){//仓库搜索页
		$t.fool("dhxCombo",{
			  prompt:'仓库',
			  width:boxWidth,
			  height:boxHeight,
			  editable:false,
			  setTemplate:{
				  input:"#name#",
				  option:"#name#"
			  },
			  toolsBar:{
				  name:"仓库",
				  refresh:true
				},
			  data:swarehouse,
			  focusShow:true,
		});
	}else if(_cla=='warehouse_d'){//仓库详情页
		$t.fool('combotree',{width:166,height:33,valTarget:'#inWareHouseId',editable:false,required:false,url:getRootPath()+'/basedata/warehourseList',onSelect:function(node){
			if(!node||node.id==undefined||node.id.length==0||node.text==undefined||node.text=='请选择'){
				$t.combotree('clear');
				$("#inMemberId").val('');
			}
		}});
	}else if(_cla=='recordStatus'){
		$t.fool("dhxCombo",{
			prompt:'状态',editable:false,
			focusShow:true,
            width:boxWidth,
			height:boxHeight,
			data:(typeof recordStatusOptions == undefined?recordStatusOptions:[{value:'0',text:'未审核'},{value:'1',text:'已审核'},{value:'2',text:'已作废'}]),
		});
	}else if(_cla=='deptComBoxTree'){//列表页
		$t.fool("dhxCombo",{
			prompt:'部门',
			editable:false,
			focusShow:true,
			width:boxWidth,
			height:boxHeight,
			data:sdeptData,
			toolsBar:{
				name:"部门",
				refresh:true
			},
			setTemplate:{
				  input:"#text#",
				  option:"#text#"
			}
		});
	}else if(_cla=='deptComBoxTree_d'){//详情页
		$t.fool('deptComBoxTree',{required: true,valTarget:'#deptId',width:160,height:30,isLeaf:false,editable:false});
	}else if(_cla=='bankName-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true,
			novalidate:true,
			width:160,
			height:30,
			prompt:"银行账号",
			toolsBar:{
				refresh:true
			},
			data:getComboData(getRootPath()+'/bankController/comboboxData'),
			hasDownArrow:false,
			focusShow:true,
			filterUrl:getRootPath()+'/bankController/comboboxData',
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
							{option:'#bank#',header:'银行',width:100},
							{option:'#account#',header:'账号',width:100},
						],
			},
			onChange:function(value,text){
				$("#bankId").val(value);
				var row = $t.next()[0].comboObj.getOption(value).text;
				setTimeout(function(){$("#bankName").combogrid('setText',row.name+'('+row.account+')');},100);
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='mybankName-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true,
			novalidate:true,
			width:160,
			height:30,
			toolsBar:{
				refresh:true
			},
			data:getComboData(getRootPath()+'/bankController/comboboxData'),
			focusShow:true,
			hasDownArrow:false,
			filterUrl:getRootPath()+'/bankController/comboboxData',
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
							{option:'#bank#',header:'银行',width:100},
							{option:'#account#',header:'账号',width:100},
						],
			},
			onChange:function(value,text){
				$("#mybankId").val(value);
				var row = $t.next()[0].comboObj.getOption(value).text;
				setTimeout(function(){$("#mybankName").combogrid('setText',row.name+'('+row.account+')');},100);
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}else if(_cla=='goods-combogrid'){
		var $opt = $.fool._opts$($t);
		$opt = $.extend({
			required:true,
			novalidate:true,
			width:boxWidth,
			height:boxHeight,
			prompt:"货品",
			focusShow:true,
			onlySelect:true,
			filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=8',
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
            toolsBar:{
                refresh:true
            },
			data:sgoodsData,
			onChange:function(value,text){
				$('#select-goodsId').val(value);
				$('#search-goodsId').val(value);
				$('#select-goodsid').val(value);
			}
		},$opt);
		$t.fool('dhxComboGrid',$opt);
	}
}

function getTotal(){
	var sum=0.00;
	var rows=$('#goodsList').datagrid('getRows');
	$.each(rows,function(index,row){
		sum  += parseFloat((row.quentity*row.unitPrice).toFixed(2));
	});
	$("#totalAmount").val(sum.toFixed(2));
}

function getPage(target){
	var pager=$(target).datagrid("getPager");
	var options=pager.pagination("options");
	var pageNumber=options.pageNumber;
	return pageNumber;
}