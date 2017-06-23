<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>利润表</title>
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
                   <h1>利润表</h1>
                </div>             
             </div>
		<fool:tagOpt optCode="profitAdd">
			<a href="javascript:;" id="add" class="btn-ora-add">新增</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="profitCaculate">
			<a href="javascript:;" id="caculate" class="btn-ora-checkdate">计算</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="profitDefault">
			<a href="javascript:;" id="setDefault" class="btn-ora-import">写入默认公式</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="profitRecovery">
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
	<table id="profitList"></table>
</body>
<script>
	$('#add').wrap('<fool:tagOpt optCode="ADD1007"></fool:tagOpt>');
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
	var periodData=getComboData('${ctx}/report/profitSheet/queryPeriods');
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

	var listDate = "";
	
	$("#profitList").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/report/profitSheet/query',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
                        listDate = data.responseJSON;
                        listDate.totalpages=Math.ceil(listDate.total/postdata.rows);
		        		$("#profitList")[0].addJSONData(listDate);

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
                  <fool:tagOpt optCode="profitAction">
		          {name:'action',label:'操作',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                      var s = '<a title="保存" href="javascript:;" class="btn-save" onclick="save(\''+options.rowId+'\');"></a>';
                      var d = '<a title="删除" href="javascript:;" class="btn-del mydel" onclick="delit(\''+options.rowId+'\');"></a>';
                      var b = '<a title="撤销" href="javascript:;" class="btn-back" onclick="cancel(\''+options.rowId+'\');"></a>';
                      if(rowObject.editing){
							return s+b;
                      }else{
                          return d;
					  }
		          }},
                  </fool:tagOpt>
		          {name : 'item',label : '项目',align:'left',width:"100px",editable:true,edittype:"text"},
                  {name : 'number',label : '行号',align:'center',width:"100px",editable:true,edittype:"text"},
                  {name : 'lastPeriodAmount',label : '本期金额',align:'center',width:"100px"}, 
                  {name : 'currentPeriodAmount',label : '上期金额',align:'center',width:"100px"}, 
                  {name : 'formula',label : '公式',align:'center',width:"100px",editable:true,edittype:"text"},
	              ],
	    onLoadSuccess:function(){//列表权限控制
	    	<fool:tagOpt optCode="profitAction2">//</fool:tagOpt>$('.mydel').remove();
	    },
		onSelectRow:function(rowid,status){
			<fool:tagOpt optCode="profitAction1">edit(rowid);</fool:tagOpt>
		}
	});
	
	//按键控制
	/* keyHandler($('#profitList')); */
	
	function edit(index){
		var rowData=$("#profitList").jqGrid('getRowData',index);
        var rowEdit = $("table#profitList tr[editable = '1']");
		if(rowData.editing=="true"){
			return;
		}
		if (rowEdit.length > 0) {
/*            $.fool.alert({
                msg: '请先保存本行！', time: 1000
            });*/

            return false;
        }
		rowData.editing=true;
		rowData.action=null;
		$('#profitList').jqGrid('setRowData', index, rowData);
		$("#profitList").jqGrid('editRow',index);
		getEditor("profitList",index,"item").textbox({
			missingMessage:"该输入项为必输项",
			required:true,
			novalidate:true,
			validType:['isBank','maxLength[100]'],
			value:rowData.item
		});
		getEditor("profitList",index,"number").numberbox({
			required:true,
			novalidate:true,
			min:1,
            validType:'maxLength[9]',
			value:rowData.number
		});
		getEditor("profitList",index,"formula").textbox({
			validType:'maxLength[500]',
			value:rowData.formula
		});
		$("#profitList").form("enableValidation");
	}
	function save(index){
		var rowData=$("#profitList").jqGrid('getRowData',index);
		var number = getEditor("profitList",index,"number").numberbox('getValue');
		var formula = getEditor("profitList",index,"formula").textbox('getValue');
		formula = $.trim(formula);//去除公式前后空格
		var item = getEditor("profitList",index,"item").textbox('getValue');
        item = removeAllSpace(item);//去除所有空格
		var fid = rowData.fid;
		var updateTime = rowData.updateTime;
		var myform = $("#profitList").find("tr[id="+index+"]");
		myform.form("enableValidation");
		if(!myform.form("validate")){
			return false;
		}
		$.ajax({
			url:'${ctx}/report/profitSheet/save',
			method:'post',
			async:false,
			data:{periodId:periodId_,fid:fid,item:item,number:number,formula:formula,updateTime:updateTime/*,isNew:false*/},
			success:function(data){
				dataDispose(data);
				if(data.result == 0){
					$("#profitList").jqGrid('saveRow',index);
					$('#profitList').jqGrid('setRowData', index, {
						item:item,
						number:number,
						formula:formula,
						updateTime:data.msg,
						editing:false,
					    action:null,
						isNew:false
					});
					$('#profitList').trigger("reloadGrid");
					if(newtips[index] == true){
						newtips[index] = false;
					}
				}else if(data.result == 1){
					$.fool.alert({msg:data.msg});
					/* edit(index); */
				}else{
					$.fool.alert({msg:'服务器繁忙,请稍后再试'});
					/* edit(index); */
				}
			}
		});
	}
	function cancel(index,value) {
        var rowData = $("#profitList").jqGrid("getRowData", index);
        if (rowData.isNew == "true") {
            $('#profitList').jqGrid('delRowData',index);
        } else {
            $('#profitList').jqGrid('setRowData', index, {editing: false, action: null});
            $("#profitList").jqGrid('restoreRow', index);
        }
    }

    function removeAllSpace(str) {
        return str.replace(/\s+/g, "");
    }
/*	function del(index){
	    index--;
	    console.log(index)
		var rowAfter=$("#profitList").find("tr[id="+index+"]").nextAll();
		$("#profitList").jqGrid('delRowData',index);
		for(var i=0;i<rowAfter.length;i++){
			$(rowAfter[i]).attr("id",parseInt($(rowAfter[i]).attr("id"))-1);
		}
	}*/
	function delit(index){
		$.fool.confirm({title:'删除确认',msg:'此操作不可复原，确认删除？',fn:function(data){
			if(data){
				var rowData=$("#profitList").jqGrid('getRowData',index);
				var fid = rowData.fid;
				$.ajax({
					url:'${ctx}/report/profitSheet/delete',
					method:'post',
					async:false,
					data:{fid:fid},
					success:function(data){
						dataDispose(data);
						if(data.result == 0){
							var rowAfter=$("#profitList").find("tr[id="+index+"]").nextAll();
							$("#profitList").jqGrid('delRowData',index);
							for(var i=0;i<rowAfter.length;i++){
								$(rowAfter[i]).attr("id",parseInt($(rowAfter[i]).attr("id"))-1);
							}
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
				url:'${ctx}/report/profitSheet/computeFormula?periodId='+periodId_,
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
						$.fool.alert({msg:'计算利润表完成！',time:2000,fn:function(){
							$('#profitList').trigger("reloadGrid");
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
			$('#profitList').jqGrid('setGridParam',{postData:option}).trigger("reloadGrid");
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
			/*var tr = $('#profitList').find('tr[aria-selected=true]');*/
			/*var index = parseInt(tr.attr("id"));*/

/*			if(index >= 0){
				newtips[index+1] = true;
				$('#profitList').jqGrid('addRowData', index+1,{isNew:"true"},"after",index);
				var rowAfter=tr.next().nextAll();
				for(var i=0;i<rowAfter.length;i++){
					$(rowAfter[i]).attr("id",parseInt($(rowAfter[i]).attr("id"))+1);
				}
				edit(index+1);
				var pOffset = $('#profitList').find("tr[id='"+(index+1)+"']").offset().top-$(window).height()/2;
				$("html,body").animate({scrollTop:pOffset},500);
			}else{
				var indexL = $('#profitList').jqGrid('getRowData').length;
				newtips[indexL] = true;
				$('#profitList').jqGrid('addRowData', indexL,{isNew:"true"},"after",indexL);
				edit(indexL);
				var pOffset = $('#profitList').find("tr[id='"+(indexL)+"']").offset().top-$(window).height()/2;
				$("html,body").animate({scrollTop:pOffset},500);
			}*/
            var ids = $('#profitList').jqGrid("getDataIDs");
            var newIndex = Math.max.apply(null,ids)+1;
            var rowEdit = $("table#profitList tr[editable = '1']");
            var oldLength = listDate.length;
            var x = newIndex - oldLength;
            if (rowEdit.length > 0) {
                $.fool.alert({
                    msg: '请先保存本行！', time: 1000
                });
                return false;
            }
            if (x >= 2) {
                $.fool.alert({
                    msg: "请先保存本行数据！", time: 1000
                });
                return;
            }
            if (newIndex == "-Infinity"){
                newIndex = "1";
            }
			var dataRow = {
                isNew:true,
                item:"",
				number:"",
				formula:"",
				updateTime:"",
			}
            $("#profitList").jqGrid("addRowData", newIndex, dataRow, newIndex);
            edit(newIndex);
            var pOffset = $('#profitList').find("tr[id='"+(newIndex)+"']").offset().top-$(window).height()/2;
            $("html,body").animate({scrollTop:pOffset},500);
		}else{
			$.fool.alert({msg:'请选择会计期间查询数据后再新增'});
		}
	});
	
	//写入默认
	$('#setDefault').click(function(){
		if(periodId_!=''){
			$.fool.confirm({title:'设置默认提示',msg:'写入默认后会覆盖原来的默认设置，确认设置？',fn:function(data){
				if(data){
					$.post('${ctx}/report/profitSheet/saveSheetFormula?periodId='+periodId_,function(data){
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
					$.post('${ctx}/report/profitSheet/resumeFormula?periodId='+periodId_,function(data){
						dataDispose(data);
						if(data.result == 0){
							$.fool.alert({msg:'恢复默认成功！',time:2000,fn:function(){
								$('#profitList').trigger("reloadGrid");
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
	$(function(){
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
	 //获取jqGrid编辑框
	 function getEditor(gridId,rowId,name){
		  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
		  return editor;
	 }
	 function getComboData(url,mark){
			var result="";
			$.ajax({
				url:url,
				data:{num:Math.random()},
				async:false,
				success:function(data){
					if(data){
						result=data;
					}
				}
			});
			if(mark!="tree"){
				return formatData(result,"fid");
			}else{
				return formatTree(result[0].children,"text","id");
			}
	 }
</script>
</html>