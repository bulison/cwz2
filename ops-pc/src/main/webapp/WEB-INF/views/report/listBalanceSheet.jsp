<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>资产负债表</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript"
	src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
body{
	padding-bottom:50px;
}
.fixed{
	position:fixed !important;
	border-width:1px 0 !important;
	z-index: 102;
}
.form1 p font{
	width:115px;
}
.form1 p{
	margin:5px 0;
}
.form1{
	padding:5px 0;
	margin-top:10px;
}
.box{
	margin:10px 0;
}
.toolBox-button{
	position:fixed;
	width:100%;
	background:rgb(240, 240, 240);
	top:0;
	z-index:1000;
	padding:5px 0;
}
</style>
</head>
<body>
<div class="toolBox-button">
		<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>资产负债表</h1>
                </div>             
             </div>
		<fool:tagOpt optCode="balsheetAdd">
			<a href="javascript:;" id="add" class="btn-ora-add">新增</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="balsheetCaculate">
			<a href="javascript:;" id="caculate" class="btn-ora-checkdate">计算</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="balsheetDefault">
			<a href="javascript:;" id="setDefault" class="btn-ora-import">写入默认公式</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="balsheetRecovery">
			<a href="javascript:;" id="recovery" class="btn-ora-recovery">恢复公式</a>
		</fool:tagOpt>
			<a href="javascript:;" id="tips" class="btn-ora-batch">公式格式帮助</a>
			<span id="hide" style="display:none;"><font>会计期间：</font><input id="search-periodId_" class="textBox"/> <a id="search_" href="javascript:;" onClick="$('body').animate({scrollTop:0},300);$('#search').click();" class="btn-blue4 btn-s" style="font-size:14px;">查询</a></span>
</div>
	<div class="box" style="margin-top:95px;">
		<form id="form" class="form1">
			<p><font>会计期间：</font><input id="search-periodId" class="textBox"/></p>
			<a id="search" href="javascript:;" class="btn-blue4 btn-s">查询</a>
			<!--  <a id="clear" href="#" class="btn-blue4 btn-s">清空</a>-->
		</form>
	</div>
	<table id="assetList"></table> 
</body>
<script>
	var periodId_ = '';
	var periodName = '';
	var newtips = [];
	$('#tips').tooltip({    
		position: 'bottom',
		width:300,
		content: '<span style="color:#fff">AA（科目,科目）：取科目的所有借方，贷方的发生额；<br>'
			+'AD（科目,科目）：取科目的借方发生额；<br>'
			+'AC（科目,科目）：取科目的贷方发生额；<br>'
			+'AS（科目,科目）：取科目设置中余额方向的发生额；<br>'
			+'DS（行号,行号）：根据行号，累加数据；<br>'
			+'AX（科目）：取科目借方余额；参照AA的计算方法，得出科目余额，如果余额小于0，则返回0，否则返回余额；<br>'
			+'AY（科目）：取科目贷方余额；参照AA的计算方法，得出科目余额，如果余额大于0，则返回0，否则返回余额的绝对值；'
			+'</span>',     
		onShow: function(){
			$(this).tooltip('tip').hide();
			$(this).tooltip('tip').slideDown(300);
			$(this).tooltip('tip').css({
				width: '500px',
				backgroundColor: '#666',            
				borderColor: '#666'        
				});    
			}
		});
	/* $('#search-periodId').combobox({
		width:160,
		height:30,
		editable:false,
		url:'${ctx}/report/balanceSheet/queryPeriods',
		required:true,
		novalidate:true,
		valueField:'fid',
		textField:'period',
		onSelect:function(record){
			$('#search-periodId_').combobox('select',record.fid);
		}
	});
	$('#search-periodId_').combobox({
		width:160,
		height:30,
		editable:false,
		url:'${ctx}/report/balanceSheet/queryPeriods',
		novalidate:true,
		valueField:'fid',
		textField:'period',
		onSelect:function(record){
			$('#search-periodId').combobox('select',record.fid);
		}
	}); */
	var periodData=getComboData('${ctx}/report/balanceSheet/queryPeriods');
	var periodCombo=$('#search-periodId').fool("dhxCombo",{
		width:160,
		height:30,
		editable:false,
		required:true,
		novalidate:true,
		setTemplate:{
		  input:"#period#",
		  option:"#period#"
		},
		data:periodData,
		focusShow:true,
		onLoadSuccess:function(){ 
			($("#search-periodId").next())[0].comboObj.deleteOption("")
		},
		onSelectionChange:function(){
			periodCombo_.setComboValue(periodCombo.getSelectedValue());
		}
	});
	var periodCombo_=$('#search-periodId_').fool("dhxCombo",{
		width:160,
		height:30,
		editable:false,
		required:true,
		novalidate:true,
		setTemplate:{
		  input:"#period#",
		  option:"#period#"
		},
		data:periodData,
		focusShow:true,
		onSelectionChange:function(){
			periodCombo.setComboValue(periodCombo_.getSelectedValue());
		}
	});
	
	$("#assetList").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/report/balanceSheet/query',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#assetList")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		autowidth:true,
		height:"100%",
		viewrecords:true,
		rowNum:9999,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[
		          {name:"fid",label:'fid',hidden:true},
		          {name : 'isNew',label : 'isNew',hidden:true},
		          {name : 'editing',label : 'editing',hidden:true}, 
		          {name:"updateTime",label:'updateTime',hidden:true},
		          <fool:tagOpt optCode="balsheetAction">
		          {name:'action',label:'操作',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                      var s = '<a title="保存" href="javascript:;" class="btn-save" onclick="save(\''+options.rowId+'\');"></a>';
					  var b = '<a title="删除" href="javascript:;" class="btn-del" onclick="delit(\''+options.rowId+'\');"></a>';
					  var c = '<a title="撤销" href="javascript:;" class="btn-back" onclick="cancel(\''+options.rowId+'\');"></a>';
		        	  if(rowObject.editing){
		        		  return s+c;
		        	  }else{
		        		  return b;
		        	  }
		          }},
                  </fool:tagOpt>
		          {name : 'assetItem',label : '资产项目',align:'center',width:"100px",editable:true,edittype:"text"},
                  {name : 'assetNumber',label : '资产行号',align:'center',width:"100px",editable:true,edittype:"text"},
                  {name : 'assetYearBegin',label : '年初余额',align:'center',width:"100px"}, 
                  {name : 'assetPeriodEnd',label : '期末余额',align:'center',width:"100px"}, 
                  {name : 'assetFormula',label : '资产公式',align:'center',width:"100px",editable:true,edittype:"text"},
                  
                  {name : 'debitItem',label : '负债项目',align:'center',width:"100px",editable:true,edittype:"text"},
                  {name : 'debitNumber',label : '负债行号',align:'center',width:"100px",editable:true,edittype:"text"},
                  {name : 'debitYearBegin',label : '年初余额',align:'center',width:"100px"}, 
                  {name : 'debitPeriodEnd',label : '期末余额',align:'center',width:"100px"}, 
                  {name : 'debitFormula',label : '负债公式',align:'center',width:"100px",editable:true,edittype:"text"},
	              ],
	    onLoadSuccess:function(){//列表权限控制
	    	<fool:tagOpt optCode="balsheetAction2">//</fool:tagOpt>$('.mydel').remove();
	    },
		onSelectRow:function(rowid,status){
			<fool:tagOpt optCode="balsheetAction1">edit(rowid);</fool:tagOpt>
		}
	});
	
	//按键控制
	/* keyHandler($("#assetList")); */
	function edit(index){
		var rowData=$("#assetList").jqGrid('getRowData',index);
		if(rowData.editing=="true"){
			return;
		}
		rowData.editing=true;
		rowData.action=null;
		$('#assetList').jqGrid('setRowData', index, rowData);
		$("#assetList").jqGrid('editRow',index);
		getEditor("assetList",index,"assetItem").validatebox({
			missingMessage:"该输入项为必输项",
			required:true,
			novalidate:true,
			validType:["isBank","maxLength[100]"],
			value:rowData.assetItem
		});
		getEditor("assetList",index,"assetNumber").numberbox({
			required:true,
			novalidate:true,
			min:1,
			validType:'maxLength[9]',
			value:rowData.assetNumber
		});
		getEditor("assetList",index,"assetFormula").validatebox({
			validType:'maxLength[500]',
			value:rowData.assetFormula
		});
        if(rowData.assetItem=="      非流动资产合计"&&rowData.assetNumber=="32"){
        	getEditor("assetList",index,"debitItem").validatebox({
    			missingMessage:"该输入项为必输项",
    			/* required:true,
    			novalidate:true, */
    			validType:["isBank","maxLength[100]"],
    			value:rowData.debitItem
    		});
    		getEditor("assetList",index,"debitNumber").numberbox({
    			/* required:true,
    			novalidate:true, */
    			min:1,
                validType:'maxLength[9]',
    			value:rowData.debitNumber
    		});
		}else{
			getEditor("assetList",index,"debitItem").validatebox({
				missingMessage:"该输入项为必输项",
				required:true,
				novalidate:true,
				validType:["isBank","maxLength[100]"],
				value:rowData.debitItem
			});
			getEditor("assetList",index,"debitNumber").numberbox({
				required:true,
				novalidate:true,
				min:1,
	            validType:'maxLength[9]',
				value:rowData.debitNumber
			});
		}
		
		getEditor("assetList",index,"debitFormula").validatebox({
			validType:'maxLength[500]',
			value:rowData.debitFormula
		});
		if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
			getEditor("assetList",index,"assetItem").bind('propertychange',function(){
				getEditor("assetList",index,"assetItem").validatebox({required:true});
				getEditor("assetList",index,"debitItem").validatebox({required:false});
				if($(this).val()==""){
					getEditor("assetList",index,"debitItem").validatebox({required:true});
				}
			});// IE专用
			getEditor("assetList",index,"debitItem").bind('propertychange',function(){
				getEditor("assetList",index,"debitItem").validatebox({required:true});
				getEditor("assetList",index,"assetItem").validatebox({required:false});
				if($(this).val()==""){
					getEditor("assetList",index,"assetItem").validatebox({required:true});
				}
			});// IE专用
		}else{
			getEditor("assetList",index,"assetItem").bind('input',function(){
				console.log(1)
				getEditor("assetList",index,"assetItem").validatebox({required:true});
				getEditor("assetList",index,"debitItem").validatebox({required:false});
				if($(this).val()==""){
					getEditor("assetList",index,"debitItem").validatebox({required:true});
				}
			});
			getEditor("assetList",index,"debitItem").bind('input',function(){
				getEditor("assetList",index,"debitItem").validatebox({required:true});
				getEditor("assetList",index,"assetItem").validatebox({required:false});
				if($(this).val()==""){
					getEditor("assetList",index,"assetItem").validatebox({required:true});
				}
			});
		}
	}
	function save(index){
		var myform = $("#assetList").find("tr[id="+index+"]");
		if(!myform.fool('fromVali')){
			return false;
		}
        var rowData=$("#assetList").jqGrid('getRowData',index);
        var assetNumber = getEditor("assetList",index,"assetNumber").numberbox('getValue');
        var assetFormula = getEditor("assetList",index,"assetFormula").val();
        var debitNumber = getEditor("assetList",index,"debitNumber").numberbox('getValue');
        var debitFormula = getEditor("assetList",index,"debitFormula").val();
        assetFormula = $.trim(assetFormula);//去除公式前后空格
        debitFormula = $.trim(debitFormula);//去除公式前后空格
        var assetItem = getEditor("assetList",index,"assetItem").val();
        var debitItem = getEditor("assetList",index,"debitItem").val();
        var fid = rowData.fid;
        var updateTime = rowData.updateTime;
		$.ajax({
			url:'${ctx}/report/balanceSheet/save',
			method:'post',
			async:false,
			data:{periodId:periodId_,fid:fid,assetItem:assetItem,debitItem:debitItem,assetNumber:assetNumber,assetFormula:assetFormula,debitNumber:debitNumber,debitFormula:debitFormula,updateTime:updateTime},
			success:function(data){
				dataDispose(data);
				var dataObj = data.data;
				if(data.result == 0){
					$("#assetList").jqGrid('saveRow',index);
					$('#assetList').jqGrid('setRowData', index, {
					    fid:dataObj.fid,
                        assetPeriodEnd:dataObj.assetPeriodEnd,
                        assetYearBegin:dataObj.assetYearBegin,
						debitPeriodEnd:dataObj.debitPeriodEnd,
						debitYearBegin:dataObj.debitYearBegin,
                        updateTime:dataObj.updateTime,
                        assetNumber:assetNumber,
						assetFormula:assetFormula,
						debitNumber:debitNumber,
						debitFormula:debitFormula,
						assetItem:assetItem,
						debitItem:debitItem,
						isNew:false,
						editing:false,
					    action:null,
					});
					if(newtips[index] == true){
						newtips[index] = false;
					}
				}else if(data.result == 1){
					$.fool.alert({msg:data.msg});
				}else{
					$.fool.alert({msg:'服务器繁忙,请稍后再试'});
				}
			}
		});
	}
	function cancel(index,value){
        var rowData=$("#assetList").jqGrid('getRowData',index);
        $("#assetList").jqGrid('restoreRow', index);
        if(rowData.isNew=="true")
            $("#assetList").jqGrid('delRowData',index);
        else{
            $('#assetList').jqGrid('setRowData', index, {editing:false,action:null});
        }
	}
//	function del(index){
//        var rowData=$("#assetList").jqGrid('getRowData',index);
//        if(rowData.isNew=="true")
//            $("#assetList").jqGrid('delRowData',index);
//		else{
//            delit(index);
//		}
////		var rowAfter=$("#assetList").find("tr[id="+index+"]").nextAll();
////		for(var i=0;i<rowAfter.length;i++){
////			$(rowAfter[i]).attr("id",parseInt($(rowAfter[i]).attr("id"))-1);
////		}
//	}
	function delit(index){
		$.fool.confirm({title:'删除确认',msg:'此操作不可复原，确认删除？',fn:function(data){
			if(data){
				var rowData=$("#assetList").jqGrid('getRowData',index);
				var fid = rowData.fid;
				$.ajax({
					url:'${ctx}/report/balanceSheet/delete',
					method:'post',
					async:false,
					data:{fid:fid},
					success:function(data){
						dataDispose(data);
						if(data.result == 0){
//							var rowAfter=$("#assetList").find("tr[id="+index+"]").nextAll();
							$("#assetList").jqGrid('delRowData',index);
//							for(var i=0;i<rowAfter.length;i++){
//								$(rowAfter[i]).attr("id",parseInt($(rowAfter[i]).attr("id"))-1);
//							}
							$.fool.alert({msg:'删除成功！',time:2000});
						}else if(data.result == 1){
							$.fool.alert({msg:data.msg});
						}else{
							$.fool.alert({msg:'服务器繁忙,请稍后再试'});
						}
					}
				});
			}
		}});
	}
	//计算操作
	$('#caculate').click(function(){
		if(periodId_!=''){
			$.ajax({
				url:'${ctx}/report/balanceSheet/computeFormula?periodId='+periodId_,
				method:'post',
				//async:false,
				beforeSend:function(){
					$.messager.progress({
						text:'努力计算中，请稍后...'
					});
				},
				success:function(data){
					dataDispose(data);
					$.messager.progress('close');
					if(data.result == 0){
						$.fool.alert({msg:'计算资产负债表完成！',time:2000,fn:function(){
							$('#assetList').trigger("reloadGrid");
						}});
					}else if(data.result == 1){
						$.fool.alert({msg:data.msg});
					}else{
						$.fool.alert({msg:'服务器繁忙,请稍后再试'});
					}
				}
			});
		}else{
			$.fool.alert({msg:'请选择会计期间查询数据后再计算'});
		}
		
	});
	
	//查询与清空
	$('#search').click(function(){
		$('#form').form('enableValidation');
		if($('#form').form('validate')){
			var periodId = ($('#search-periodId').next())[0].comboObj.getSelectedValue();
			var option = {periodId:periodId};
			$('#assetList').jqGrid('setGridParam',{postData:option}).trigger("reloadGrid");
			periodId_ = periodId;
			periodName = ($('#search-periodId').next())[0].comboObj.getComboText();
		}
	});
	/*$('#clear').click(function(){
		$('#form').form('clear');
	});*/
	
	
	//新增资产负债记录
	$('#add').click(function(){
		if(periodId_!=''){
			var tr = $('#assetList').find('.ui-state-highlight');
			var index = tr.attr("id");
			/* if(index >= 0){
				newtips[index+1] = true;
				$('#assetList').jqGrid('addRowData', index+1,{isNew:"true"},"after",index);
				var rowAfter=tr.next().nextAll();
				for(var i=0;i<rowAfter.length;i++){
					$(rowAfter[i]).attr("id",parseInt($(rowAfter[i]).attr("id"))+1);
				}
				edit(index+1);
				var pOffset = $('#assetList').find("tr[id='"+(index+1)+"']").offset().top-$(window).height()/2;
				$("html,body").animate({scrollTop:pOffset},500);
			}else{ */
				var indexL = $('#assetList').jqGrid('getRowData').length;
				newtips[indexL] = true;
            	var ids = jQuery("#assetList").jqGrid('getDataIDs');
            	var newrowid = Math.max.apply(Math,ids) + 1;
				if(newrowid=="-Infinity"){
					newrowid = "1";
				}
				$('#assetList').jqGrid('addRowData', newrowid,{isNew:"true"},"first",newrowid);
				edit(newrowid);
				//var pOffset = $('#assetList').find("tr[id='"+(indexL)+"']").offset().top-$(window).height()/2;
				//$("html,body").animate({scrollTop:pOffset},500);
			/* } */
		}else{
			$.fool.alert({msg:'请选择会计期间查询数据后再新增'});
		}
	});
	
	//写入默认
	$('#setDefault').click(function(){
		if(periodId_!=''){
			$.fool.confirm({title:'设置默认提示',msg:'写入默认后会覆盖原来的默认设置，确认设置？',fn:function(data){
				if(data){
					$.post('${ctx}/report/balanceSheet/saveSheetFormula?periodId='+periodId_,function(data){
						dataDispose(data);
						if(data.result == 0){
							$.fool.alert({msg:'写入默认成功！',time:2000});
						}else if(data.result == 1){
							$.fool.alert({msg:data.msg});
						}else{
							$.fool.alert({msg:'服务器繁忙，请稍后再试'});
						}
					});
				}
			}});
		}else{
			$.fool.alert({msg:'请选择会计期间查询数据后再写入默认'});
		}
	});
	
	//恢复默认
	$('#recovery').click(function(){
		if(periodId_!=''){
			$.fool.confirm({title:'恢复默认提示',msg:'恢复默认后会覆盖'+periodName+'会计期间当前页面数据，确认恢复？',fn:function(data){
				if(data){
					$.post('${ctx}/report/balanceSheet/resumeFormula?periodId='+periodId_,function(data){
						dataDispose(data);
						if(data.result == 0){
							$.fool.alert({msg:'恢复默认成功！',time:2000,fn:function(){
								$('#assetList').trigger("reloadGrid");
							}});
						}else if(data.result == 1){
							$.fool.alert({msg:data.msg});
						}else{
							$.fool.alert({msg:'服务器繁忙，请稍后再试'});
						}
					});
				}
			}});
		}else{
			$.fool.alert({msg:'请选择会计期间查询数据后再恢复默认'});
		}
	});
	$(function(){$(function() {
		var offset = $('.ui-jqgrid-hdiv').offset();
		$(window).scroll(function () {
			var scrollTop = $(window).scrollTop();
			if (offset.top-$('.toolBox-button').height() <= scrollTop){
				$("#hide").fadeIn();
				$('.ui-jqgrid-bdiv').css('padding-top',30);
				$('.ui-jqgrid-hdiv').css('top',$('.toolBox-button').outerHeight(true));
				$('.ui-jqgrid-hdiv').addClass('fixed');
			}else{
				$("#hide").fadeOut();
				$('.ui-jqgrid-bdiv').css('padding-top',0);
				$('.ui-jqgrid-hdiv').css('top',0);
				$('.ui-jqgrid-hdiv').removeClass('fixed');
			} 
		});
	});
	});
</script>
</html>