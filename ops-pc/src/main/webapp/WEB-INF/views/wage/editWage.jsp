<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>新增/编辑工资</head>
<body>
	<!-- <div id="goodsSpecChooser" style="display: none;">
		<div id="goodsSpecSearcher">
			<input id="search-goodsSpecName" /><a href="javascript:;" id="search-goodsSpecBtn"
				class="btn-blue btn-s">筛选</a>
		</div>
		<table id="goodsSpecTable"></table>
	</div> -->
	<div id="goodsChooser"></div>
<c:set var="flagName" value="${empty wage.createTime?'新增':empty wage.auditorName?'编辑':'查看'}" scope="page"></c:set>
    <form action="" class="bill-form myform" id="form">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><span class="backBtn"><div id="triangle"></div></div><h1>${flagName}工资录入单</h1><a id="hide" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">基础信息</a>
		</div>
	</div>
	<div id="bill" class="shadow" style="margin-top:50px;">
		<div class="billTitle myTitle"><div id="square2"></div><h2>填写基础信息</h2></div>
		<div class="in-box" id="list2">
		  	<div class="inlist">
			<input id="fid" type="hidden" name="fid" value="${wage.fid}" /> <input
				name="updateTime" id="updateTime" value="${wage.updateTime}"
				type='hidden' />
			<p>
				<font><em>*</em>部门：</font><input id="deptName" name="deptId" class="textBox" />
				<font><em>*</em>月份：</font><input id="wageDate" name="wageDate" class="textBox" value="${wage.wageDate}" />
			</p>
			<p>
				<font>备注：</font><input id="remark" name="remark" class="textBox"
					validType='maxLength[200]' style="width: 519px;"
					value="${wage.remark}" />
			</p>
			<p>
			<span style="display: inline-block; margin-left: 44px;font-weight:700;">&emsp;其他信息：</span>
			<img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png"
				style="vertical-align: middle;"> <img id="closeBtn" alt="展开"
				src="${ctx}/resources/images/closeNode.png"
				style="vertical-align: middle; display: none">
			</p>
			<p class="hideOut">
				<font>创建人：</font><input id="creatorName" class="textBox" readonly="readonly" value="${wage.creatorName}" />
				<font>创建时间：</font><input id="createTime" class="textBox" readonly="readonly" value="${wage.createTime}" />
				<font>审核人：</font><input id="auditorName" class="textBox" readonly="readonly" value="${wage.auditorName}" />
			</p>
			<p class="hideOut">
				<font>审核时间：</font><input id="auditorTime" class="textBox" readonly="readonly" value="${wage.auditorTime}" />
			</p>
			 </div>
		</div>
	</div>
	<div id="dataBox" class="shadow">
		<div class="billTitle"><div id="square2"></div><h2>工资表详细信息</h2></div>
		<div class="in-box" id="list1">
			<div id="toolbar" style="margin-bottom:10px;">
		${(obj.recordStatus==null||obj.recordStatus==0)?'<a href="javascript:;" id="addGoods" class="btn-ora-add" >添加</a><a href="javascript:;" id="import" class="btn-ora-add">引入</a>':''}
		</div>
			<table id="goodsList"></table>
		</div>
	</div>
	

</form>
<div class="mybtn-footer" id="btnBox"></div>
 <div class="repertory"></div>
<div id="scroll1" class="scroll1">
	<div id="scroll2" class="scroll2"></div>
</div>
<script type="text/javascript">
	var _data="";//明细数据
	var endDay2='';//年月
	var endDay='';//年月
	var deptName='${wage.deptId}';//部门
	var _id = '${wage.fid}';
	var addk='';//获取点击添加按钮的Text
	var _b = true;
	var deptText='';//部门控件对象
	$.ajax({
		type : 'post',
		url :'${ctx}/wage/detailList',
		data : {wageId : _id},
		dataType : 'json',
		async:false,
		success : function(data){
			_data=data;	
	    }			
	});
	$('.backBtn').click(function(){
		$('#addBox').window('close');
	});
	$('#hide').click(function(){
		var $myobj = $(this);
		$myobj.closest('.window-body').animate({scrollTop:0},500);
	});
	//点击图标展开隐藏
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
	//表单初始化 
	 $('#wageDate').datebox({
		 editable:false,
         novalidate:true,
		 width:182,height:31,
		 onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
			 setTimeout(function(){span.trigger('click');},0); //触发click事件弹出月份层,延迟是为了防止系统面板上移的BUG
			if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
				tds = p.find('div.calendar-menu-month-inner td');
		        tds.click(function (e) {
		        	e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
		            var year = /\d{4}/.exec(span.html())[0]//得到年份
		                        , month = parseInt($(this).attr('abbr'), 10) ; //月份
		                        $('#wageDate').datebox('hidePanel')//隐藏日期对象
		                        .datebox('setValue', year + '-' + month); //设置日期的值	                        
		                       $("#wageDate").datebox('calendar').find('.calendar-header').hide();
		                        if('${param.flag}'=='add'){	//新增                        	
		                           lead(deptName);
		                        }else if('${param.flag}'=='modified'){//修改		  
		                        	var rows = $("#goodsList").jqGrid('getRowData').length;
		                        	if(rows>0){	                        		
			                        	 $.fool.confirm({title:'确认',msg:'用户明细数据将会丢失，确定要继续吗？',fn:function(r){
			            					 if(r){	            					
			            						lead(deptName);
			            						dele(); 
			            					 }
			            				 }});
		                        	}else{
		                        		lead(deptName);
		                        	}
		                        } 	                        
		                    });
		                }, 0)
		                $("#wageDate").datebox('calendar').find('.calendar-header').hide();
			            $("#wageDate").datebox('calendar').parent().siblings('.datebox-button').hide();
			            $('input.calendar-menu-year').attr("disabled","disabled");//避免填写年份后会回到选择日期的面板的问题
		            },
		            parser: function (s) {//配置parser，返回选择的日期
		                if (!s) return new Date();
		                var arr = s.split('-');
		                return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) , 1);	
		            },
		            formatter: function (d) {
		            	if(d.getMonth()==0){
							return d.getFullYear()-1 + '-12'; 
						}else{
							return d.getFullYear() + '-' + d.getMonth();
						}
		            }//配置formatter，只返回年月   
		        });
		    var p = $('#wageDate').datebox('panel'), //日期选择对象	  
		    tds = false, //日期选择对象中月份
		    span = p.find('span.calendar-text'); //显示月份层的触发控件 

	//部门初始化
	var deptValue='';//部门数据
		$.ajax({
			url:"${ctx}/orgController/getAllTree?num="+Math.random(),
			async:false,		
			success:function(data){		  	
				deptValue=formatTree(data[0].children,'text','id');
		    }
			});
	deptText=$("#deptName").fool("dhxCombo",{
			 prompt:'部门',
			  width:160,
			  height:32,
			  required:true,
			  novalidate:true,
			  data:deptValue,							
			  editable:false,
			  clearOpt:false,
			  focusShow:true,
			toolsBar:{
				name:"部门",
				addUrl:"/orgController/deptMessage",
				refresh:true
			},
        onLoadSuccess:function(combo){
				combo.setComboValue("${wage.deptId}");
			},onChange:function(value,text){
				 if('${param.flag}'=='add'){//新增
					 deptName=value;
				     lead(deptName) 
				}	
				if('${param.flag}'=='modified'){//修改	
				var rows = $("#goodsList").jqGrid('getRowData').length;										   											
					if(rows>0){						
					$.fool.confirm({title:'确认',msg:'用户明细数据将会丢失，确定要继续吗？',fn:function(r){
					  if(r){
						 deptName=value;
						 lead(deptName);
						 dele(); 
							}
						}});
					}
				}	 
			},
		});	
	
	
	//点击引入事件
	$('#import').click(function(deptId,wageDate){
		var _endDay = $('#wageDate').datebox('getValue');
		 var deptName=$("[name='deptId']").val(); 
		 $.ajax({
				type : 'post',
				url :'${ctx}/wage/saveByImport',
				data : {deptId:deptName,wageDate:_endDay }, 
				dataType : 'json',
			    async:true, 
				success : function(data){
					dataDispose(data);
					var  Information=(JSON.stringify(data))//转化为json字符串
					if(data.result == "0"){	
						 $.fool.confirm({title:'操作提示',msg:data.msg,fn:function(r){
							 var id=data.obj;								
							 $('#addBox').window('refresh','${ctx}/wage/editWage?flag=yinru&&fid='+id);  
			                 $('#billList').trigger('reloadGrid');
						 }});	
					}
					else{							
						$.fool.alert({msg:data.msg});	
                	}
			    }				      
			});	
	});
	//工资录入明细列表
		$("#goodsList").jqGrid({
			    data:_data,//加载本地数据,编辑
				datatype:"local",
			 	autowidth:true,//自动填满宽度			 	
			 	height:$(window).outerHeight()-500+"px",	
			 	//height:'100%',
				forceFit:true,//调整列宽度，表格总宽度不会改变
				jsonReader:{
					records:"total",
					total: "totalpages",  
				}, 
				 onCellSelect:function(rowid,iCol,cellcontent,e){	
					if('${wage.auditorTime}'==null||'${wage.auditorTime}'==''){
						var edit='edit';
						editer(rowid);
	  				}
				}, 
			colModel:[
			    {name : 'editing',label : 'editing',hidden:true},
				{name:'memberId',label:'fid',hidden:true},
				{name:'memberCode',label:'人员编号',sortable:true,width:100,align:'center'},
				{name:'memberName',label:'人员名称',sortable:true,width:100,align:'center'}, 
				{name:'memberDept',label:'部门',sortable:true,width:100,align:'center'},		
				<c:forEach items="${titles}" var="title">
				{name:'${title.fid}',label:'${title.columnName}',sortable:true,width:100,align:'center',precision:2,editable:true,edittype:'text',editoptions:{dataInit:function(ed){
	        	$(ed).numberbox({validType:['maxLength[200]'],precision:2,width:'100%',height:'100%'})}},formatter:function(value,options,row){					 
					  var rows = $("#goodsList").jqGrid('getRowData');
					   //保存一条信息后需要刷新，在修改的时候多添一条数据才能保存	
					    if('${param.flag}'=='add'||('${param.flag}'=='modified'&&addk=='addbtn')||('${param.flag}'=='yinru'&&addk=='addbtn')){//新增和修改多加数据时执行					    	
					    	if(!row['${title.fid}']&&'${param.flag}'.length!=0){				    						    	
								//row['b_${title.fid}']="1";
										if("${title.columnType}"==0){//0可以手动修改1公式计算,判断如果是新增返回默认值0，查看和修改返回value
											row['${title.fid}']="${title.defaultValue}";											
											return "${title.defaultValue}";										
										}else if("${title.columnType}"==1){ 
											row['${title.fid}']='0';
											return 0;										
										}				
								    }else{								     						
									  row['${title.fid}']=value;					
									  return value;										
									   }	 							 
							    }else{
								    row['${title.fid}']=value;								   
								   if(value==undefined){
									   return '';
								   } 
									return value;									
						 		   }					   
                }},
				</c:forEach> 
				{name:'action',label:'操作',width:100,align:'center',formatter:function(value,options,row){
					if(value=='foot'||'${param.mark}'=='1')return '';
		  		 	if (row.editing){
						var s = '<a class="btn-save saveConfirm" title="确认" href="javascript:;" onclick="saverow(\''+options.rowId+'\')"></a> ';
						var c = '<a class="btn-back save" title="撤销" href="javascript:;" onclick="cancelrow(\''+options.rowId+'\')"></a>';
						return s+c;
					 } else {
		  				if('${obj.recordStatus}'!=0){
		  					return "";
		  				}					 
			  			var d= '<a class="btn-del" title="删除" href="javascript:;" onclick="deleter(\''+options.rowId+'\')"></a>';
			  			return d;
					} 
		  		}}					
		    ] , 
			onLoadSuccess:function(){ 
				scrollDiy();
				var soffset = $('#dataBox').offset();
	   			var $window = $('#dataBox').closest('.window-body');
	   			var offset = $('.in-box .datagrid').offset();
	   			$window.scroll(function () {
	   				var scrollTop = $(this).scrollTop();
	   				if (soffset.top <= (scrollTop+86)){
	   					$('#hide').fadeIn();
	   				}
	   				else{
	   					$('#hide').fadeOut();
	   				}
	   				if (offset.top <= (scrollTop+26)){
	   					$('#list1 .datagrid-body').css('padding-top',35);
	   					$('#list1 .datagrid-header').css('width',$('#dataBox').width()-25);
	   					$('#list1 .datagrid-header').css('top',$('#title').outerHeight(true)+11);
	   					$('#list1 .datagrid-header').addClass('fixed');
	   				}
	   				else{
	   					$('#list1 .datagrid-body').css('padding-top',0);
	   					$('#list1 .datagrid-header').css('top','');
	   					$('#list1 .datagrid-header').removeClass('fixed');
	   				} 
	   			});
			}
	  });
		//$("#goodsList").removeAttr("style");
		//按键控制
		keyHandler($("#goodsList"));
		//根据操作显示明细下面的按钮,//查看设置控件不可修改
		if('${wage.auditorTime}'==null||'${wage.auditorTime}'==''){
			 $("#btnBox").append('<input type="button" id="save" class="btn-blue2 btn-xs" value="保存" />');
		}else if('${wage.auditorTime}'!=null||'${wage.auditorTime}'!=''){	
			$('#addGoods').css('display','none');
		    deptText.disable();
		    $("#form").find("input").attr('disabled','disabled');
		    $("#wageDate").datebox({disabled:true});
			}

function updateActions(index){
	$('#goodsList').datagrid('updateRow',{
		index: index,
		row:{}
	});
}
function getRowIndex(target){
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}

//删除明细下面所有的行
function dele(){
	jQuery("#goodsList").jqGrid("clearGridData");
}

//点击添加人员操作
$("#addGoods").click(function(){
	addk='addbtn';
	memberChooserOpen = true;
	 var deptId=$("[name='deptId']").val()== undefined ? '' : $("[name='deptId']").val(); 
	win = $("#goodsChooser").fool('window',{'title':"选择",'height':"450",'width':"1074",
			href:getRootPath()+'/member/window?okCallBack=selectMember&onDblClick=selectMember2&singleSelect=false&deptId='+deptId});
});

//详细页
function selectMember(rowData){
    for(var i in rowData){
	selectrow(rowData[i]);
    }
    win.window('close');
}
 function selectMember2(rowData) {
	selectrow(rowData);
	win.window('close');
} 
//将选择的行插入到明细列表
function selectrow(rowData){
	var index = $("#goodsList").jqGrid('getRowData').length+1;
	$("#goodsList").jqGrid('addRowData',index,{flag:true,memberId:rowData.fid,memberCode:rowData.userCode,memberName:rowData.username,memberDept:rowData.deptName});
}

//保存操作
function getGoods(){
	return $("#goodsList").datagrid('getRows');
}

$('#save').click(function(e) {		
	var rows = $("#goodsList").jqGrid('getRowData').length;
	for(var i=1;i<=rows;i++){
		var _editing=$('#goodsList').find('.btn-save').length;		
	}
	 if(_editing>0){
		$.fool.alert({msg:'你还有没编辑完,请先确认！'});
		return false;
	};
	var details=$("#goodsList").jqGrid('getRowData');
	var jsonuserinfo = $('#form').serializeJson();
	if(details.length>0){
	var obj = $.extend(jsonuserinfo,{details:JSON.stringify(details)});
		obj["deptId"] = ($("#deptName").next())[0].comboObj.getSelectedText().fid;
	$('#form').form('enableValidation'); 
		 if($('#form').form('validate')){ 
			    $('#save').attr("disabled","disabled");
			    $.post('${ctx}/wage/save',obj,function(data){
			    	dataDispose(data);			    
			    	if(data.errorCode==202){
			    		$.fool.alert({btnName:'转到工资核算公式',btnAct:'mybtnAct2(this)',msg:data.msg,fn:function(){
		    			}});
			    	}else{
				    	if(data.returnCode =='0'){
				    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
				    			$('#addBox').window('close');
				    			$('#save').removeAttr("disabled");
				    			$('#billList').trigger('reloadGrid');
				    		}});
				    	}else{
				    		$.fool.alert({msg:data.msg});
				    		$('#save').removeAttr("disabled");
			    		}
				    	return true;
			    	}
			    });
		}else{
			return false;
		} 
	}else{
		$.fool.alert({msg:'必须选择员工明细'});
	} 
}); 

function mybtnAct2(target){
	$(target).prev().click();
	parent.kk('/wageFormula/listWageFormula','工资核算公式')
}

//删除
function deleter(index){
	$("#goodsList").jqGrid('delRowData',index);
}

//编辑方法
function editer(index){
	var rowData=$("#goodsList").jqGrid('getRowData',index);
	if(rowData.editing=="true"){//设置编辑状态
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#goodsList').jqGrid('setRowData', index, rowData);
	jQuery("#goodsList").jqGrid('editRow',index);//打开编辑行
	<c:forEach items="${titles}" var="title">
		if("${title.columnType}"==1){			
			$('#'+index+'_${title.fid}').numberbox('disable',true);
		}			
		$('#'+index+'_${title.fid}').numberbox('textbox').focus(function(e){//获取焦点的时候全选数据
			$(this).select();
		});			
	</c:forEach> 
}
<c:forEach items="${titles}" var="title">//设置isView等于0不显示
if("${title.isView}"==0){
	jQuery("#goodsList").setGridParam().hideCol("${title.fid}").trigger("reloadGrid");
}
</c:forEach> 
$("#goodsList").setGridWidth($(window).width()*0.94);　　
$("#goodsList").setGridWidth(document.body.clientWidth*0.94);　
//列表编辑保存
function saverow(index){
	var rowData=$("#goodsList").jqGrid('getRowData',index);
	if(rowData.editing=="false"){//设置编辑状态
		return;
	}
	rowData.editing=false;
	rowData.action=null;
	$('#goodsList').jqGrid('setRowData', index, {editing:false,action:null});//编辑状态转换，按钮变化
	$("#goodsList").jqGrid('saveRow',index,false);	
	
}
//撤销行
function cancelrow(index){
	$('#goodsList').jqGrid('setRowData', index, {editing:false,action:null});//编辑状态转换，按钮变化
	$("#goodsList").jqGrid('restoreRow', index);
}
	   
function lead(deptName){//判断时间和部门不为空引入按钮显示 
	var _endDay = $('#wageDate').datebox('getValue');
     var deptName=$("[name='deptId']").val(); 
    if(_endDay!=''&&_endDay!=null&&_endDay!=undefined&&deptName!=''&&deptName!=null&&deptName!=undefined){
		$('#import').css('display','inline-block');
    } 
}
 
$('#wageDate').datebox({
	height:30,
    required:true
});

function textBox(obj,prompt){
	obj.textbox({
		'prompt':prompt,
		width:100,
		height:30
	});
}
textBox($("#search-goodsCode"),"编号");
textBox($("#search-goodsName"),"名称");
textBox($("#search-goodsSpec"),"规格");

	</script>
</body>
</html>