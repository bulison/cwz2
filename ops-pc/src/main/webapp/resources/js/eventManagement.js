/**
 * 事件管理通用
 */
var _billCode='';//页面标识
var _billCodeName='';//页面名称
//启用状态
var opTion = [
      		    {id:'0',name:'停用'},
      		    {id:'1',name:'启用'}
      		];
var events = [
            {id:'0',name:'触发事件'},
	   		{id:'1',name:'前关联事件'},
	   		{id:'2',name:'后关联事件'}
           ];

var send = [
     		    {id:'0',name:'否'},
     		    {id:'1',name:'是'}
     		];
var System = [
               {id:'0',name:'用户自定义配置'},
               {id:'1',name:'系统默认生产配置'}
            ];

var message =[
              {id:'0',name:'发起人'},
    		  {id:'1',name:'负责人'},
    		  {id:'2',name:'承办人'},
    		  {id:'3',name:'关注人'},
    		  {id:'4',name:'评价人'},
    		  {id:'5',name:'留言人'},
    		  {id:'6',name:'评分人'},
    		  {id:'7',name:'部门监督人'},
    		  {id:'8',name:'公司监督人'},
    		  {id:'9',name:'审核人'}
              ];
var trigger=[
              {id:'10',name:'提交'},
	   		  {id:'11',name:'修改'},
	   		  {id:'12',name:'删除'},
	   		  {id:'13',name:'终止'},
	   		  {id:'14',name:'变更承办人'},
	   		  {id:'15',name:'变更责任人'},
	   		  {id:'20',name:'申请延迟'},
	   		  {id:'30',name:'审核'},
	   		  {id:'31',name:'审核通过办理'},
	   		  {id:'32',name:'审核不通过办理'},
	   		  {id:'33',name:'审核通过申请延迟 '},
	   		  {id:'34',name:'审核不通过申请延迟'},
	   		  {id:'40',name:'新建,分派'},
	   		  {id:'41',name:'完成'},
	   		  {id:'42',name:'办理结束'},
	   		  {id:'43',name:'办理开始'},
	   		  {id:'50',name:'评价'},
	   		  {id:'51',name:'评分'},
	   		  {id:'52',name:'关注'},
	   		  {id:'53',name:'留言'},
	   		  {id:'54',name:'关联'},
	   		  {id:'60',name:'提醒'},
	   		  {id:'61',name:'延迟报警'},
	   		  {id:'62',name:'收益率报警'},
	   		  {id:'63',name:'库存报警'},
	   		  {id:'70',name:'库存不足触发采购/生产'}
             ];
var busSceneval=[
                  {id:'100',name:'计划草稿'},
     	   		  {id:'101',name:'计划已提交待审核'},
     	   		  {id:'102',name:'计划执行中'},
     	   		  {id:'103',name:'计划已延迟'},
     	   		  {id:'104',name:'计划终止'},
     	   		  {id:'105',name:'计划完成'},
     	   		  {id:'0',name:'事件草稿'},
     	   		  {id:'1',name:'事件办理中'},
     	   		  {id:'2',name:'事件已办理待审核'},
     	   		  {id:'3',name:'事件已完成'},
     	   		  {id:'4',name:'事件已延迟且未开始办理'},
     	   		  {id:'5',name:'事件已延迟且未结束办理'},
     	   		  {id:'6',name:'事件已延迟且未开始办理'},
                   ];
var taskTypeVal=[{value:'0',text:'触发事件'},{value: '1',text: '前关联事件'},{value: '2',text: '后关联事件'}];
var sendVal = [{value:'0',text:'否'},{value:'1',text:'是'}];
var sendTypeVal = [{value:'0',text:'发起人'},{value: '1',text: '负责人'},{value: '2',text: '承办人'},{value: '7',text: '部门监督人'},{value: '8',text: '公司监督人'} ,{value: '9',text: '审核人'}];

	function initManage(billCode,billCodeName){
		_billCode = billCode;
		_billCodeName = billCodeName;
	}

//操作按钮
function operate(cellvalue,options,rowObject){
	var s="<a href='javascript:;' title='保存' class='btn-save' onclick='saverow(\""+options.rowId+"\",\""+rowObject.fid+"\",\""+rowObject.updateTime+"\")'></a>";
	var c="<a href='javascript:;' title='取消' class='btn-cancel' onclick='cancelrow(\""+options.rowId+"\",\""+rowObject.isNew+"\",\""+rowObject.fid+"\")'></a>";
	//var e="<a href='javascript:;' title='编辑' class='btn-edit' onclick='editer(\""+options.rowId+"\",\""+rowObject.checkoutStatus+"\")' ></a>";
    var r = "<a href='javascript:;' class='btn-del' onclick='delRow(\""+rowObject.fid+"\")' title='删除'></a>";	
	if (rowObject.editing){
		return s+c;
	}else{
		return r;
	}
}
//启用按钮点击事件
function Minitab(target,fid){
	if(_billCode=="bmjb"){
		var _url=getRootPath()+'/flow/security/use?id='+fid;
	}
		$.fool.confirm({title:'确定',msg:'启用后不能修改级别，是否启用？',fn:function(r){
			if(r){
				$.ajax({
					type : 'post',
					url : _url,
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'启用成功！',fn:function(){
								 $('#datalist').trigger('reloadGrid');
							}});
						}else{
							$.fool.alert({msg:data.message});
						}
					},
					error:function(){
						$.fool.alert({msg:"系统正忙，请稍后再试。"});
					}
				});
			}
		}})
}

//停用用按钮点击事件
function blockUp(target,fid){
	var _url='';
	if(_billCode=="bmjb"){
		_url=getRootPath()+'/flow/security/unuse?id='+fid;
	}
				$.ajax({
					type : 'post',
					url : _url,
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'停用成功！',fn:function(){
								 $('#datalist').trigger('reloadGrid');
							}});
						}else{
							$.fool.alert({msg:data.message});
						}
					},
					error:function(){
						$.fool.alert({msg:"系统正忙，请稍后再试。"});
					}
				});
}
function editer(newIndex,checkoutStatus){
	editRow(newIndex,checkoutStatus);
}
function editRow(newIndex,checkoutStatus){
	var rowData=$("#datalist").jqGrid('getRowData',newIndex);
	if(rowData.editing=="true"){//设置编辑状态
		return;
	}
	rowData.editing=true;
	rowData.action=null;
	$('#datalist').jqGrid('setRowData', newIndex, rowData);
	jQuery("#datalist").jqGrid('editRow',newIndex);//打开编辑行
	// var name=getEditor("datalist",newIndex,"name").validatebox({
	// 	required:true,
	// 	novalidate:true,
	// 	validType:['maxLength[50]'],
	// });
	// var code=getEditor("datalist",newIndex,"code").validatebox({
	// 	required:true,
	// 	novalidate:true,
	// 	validType:['maxLength[50]'],
	// });
	// var level=getEditor("datalist",newIndex,"level").validatebox({
	// 	required:true,
	// 	novalidate:true,
	// 	validType:['accessory','maxLength[50]'],
	// });
	var checkoutStatusCombo=getEditor('datalist',newIndex,"checkoutStatus").fool("dhxCombo",{
		editable:false,
		valueField: 'value',
		textField: 'text',
		clearOpt:false,
		data:[
		      {
		    	  value: '0',
			      text: '停用'
		      },{
		    	  value: '1',
			      text: '启用'
		      }
		     ],
		onLoadSuccess:function(combo){
            if(rowData.checkoutStatus=="启用")
                combo.setComboValue('1');
            else
                combo.setComboValue('0');
		}	
	});
    checkoutStatusCombo.readonly(true);

	var $name=$('#'+newIndex+'_name');//设置控件宽度
	var $code=$('#'+newIndex+'_code');
	var $level=$('#'+newIndex+'_level');
	$name.addClass('formula');
	$code.addClass('formula');
	$level.addClass('formula');
	
	var checkoutStatus=(getEditor("datalist",newIndex,"checkoutStatus").next())[0].comboObj.getSelectedValue();
	if(_billCode=="bmjb"||_billCode=="sjjb"){
		if(checkoutStatus==1 && rowData.isNew != 1){//保密级别事件级别启用后不能修改
			$('#'+newIndex+'_code').next('span').find('.textbox-text').attr('readonly',true).css('background','#EBEBE4');
			$('#'+newIndex+'_name').next('span').find('.textbox-text').attr('readonly',true).css('background','#EBEBE4');
			$('#'+newIndex+'_level').next('span').find('.textbox-text').attr('readonly',true).css('background','#EBEBE4');
			// $('#'+newIndex+'_level').next('span').find('.textbox-text').attr('readonly',true).css('background','#EBEBE4');
			$('#'+newIndex+'_describe').attr('readonly',true).css('background','#EBEBE4');
			$('#'+newIndex+'_price').attr('readonly',true).css('background','#EBEBE4');
		}
	}
}

//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}
function getText(gridId,rowId,name){
    var editor = $('#'+gridId+' #'+rowId).find('td[aria-describedby="Tablelist_'+name+'"]');
    return editor;
}
//删除/delete
function delRow(fid){
    var _url="";
	if(_billCode=="bmjb"){
		_url=getRootPath()+'/flow/security/delete?fid='+fid;
	}else if(_billCode=="sjfl"){
		_url=getRootPath()+'/flow/taskType/delete?fid='+fid;
	}else if(_billCode=="sjjb"){
		_url=getRootPath()+'/flow/taskLevel/delete?fid='+fid;
	}
	$.fool.confirm({title:'提示',msg:"确认删除记录？",fn:function(data){
		if(data){	
			$.post(_url,function(data){	
				dataDispose(data);
				if(data.returnCode == '0'){
				    $('#datalist').trigger('reloadGrid');
				    $.fool.alert({time:1000,msg:'删除成功'});
				}else if(data.returnCode = '1'){
					$.fool.alert({msg:data.message});
				}else{
					$.fool.alert({msg:'服务器繁忙，请稍后再试'});
					
				}
			},'json');
		}else{
			return false;
		}
	}});
}

//列表编辑保存
function saverow(index,fid,updateTime){
	var a =getEditor("datalist",index,"name");
    var b =getEditor("datalist",index,"code");
    var c =getEditor("datalist",index,"level");
	// var b =getEditor("datalist",index,"code")[0];
	// var c =getEditor("datalist",index,"level")[0];
	a.textbox("enableValidation")
	b.textbox("enableValidation")
	c.textbox("enableValidation")
	if(_billCode=="sjfl"){
		if(!a.textbox('isValid')||!b.textbox('isValid')){
			return false;
		}
	}

	if(_billCode=="bmjb"||_billCode=="sjjb"){
		if(!a.textbox('isValid')||!b.textbox('isValid')||!c.textbox('isValid')){
			return false;
		}
	}
	var fid=fid;
	var updateTime=updateTime;
	var name=getEditor("datalist",index,"name").val();
	var code=getEditor("datalist",index,"code").val();
	var level=getEditor("datalist",index,"level").val();
	var checkoutStatus=(getEditor("datalist",index,"checkoutStatus").next())[0].comboObj.getSelectedValue();
	var price=getEditor("datalist",index,"price").val();
	var describe=getEditor("datalist",index,"describe").val();
	$('#datalist').jqGrid('setRowData', index, {editing:false,action:null,isNew:0});//编辑状态转换，按钮变化
	$("#datalist").jqGrid('restoreRow', index);
	var _url='';
	if(_billCode=="bmjb"){
		_url=getRootPath()+'/flow/security/save';
	}else if(_billCode=="sjfl"){
		_url=getRootPath()+'/flow/taskType/save';
	}else if(_billCode=="sjjb"){
		_url=getRootPath()+'/flow/taskLevel/save';
	}
	$.post(_url,{fid:fid,updateTime:updateTime,code:code,name:name,level:level,checkoutStatus:checkoutStatus,price:price,describe:describe},function(data){
		dataDispose(data);
		if(data.returnCode==0){
			if(dhxkey == 1){
                selectTab(dhxname,dhxtab);
            }
			$("#datalist").jqGrid('saveRow',index);
			$('#datalist').jqGrid('setRowData',index,{fid:data.data.fid,updateTime:data.data.updateTime,code:code,name:name,level:level,checkoutStatus:checkoutStatus,price:price,describe:describe,editing:false,action:null});
			}else if(data.returnCode==1){
				$('#datalist').trigger('reloadGrid');
				$.fool.alert({msg:"保存失败,"+data.message});				
			}
		});
    //}
}

$(document).keydown(function(event){ 
    if (event.keyCode == "13") {//keyCode=13是回车键
    	var searchCode=$('#search-code').val();
         if(searchCode!=''){
        	var code=$("#search-code").textbox('getValue');
     		var options = {"searchKey":code};
     		$('#datalist').datagrid('load',options);
         }else{      	
         document.getElementById("save").click();//保存按钮点击事件
    } 	
 }
});

//列表撤销
function cancelrow(index,value,fid){
    $('#datalist').jqGrid('restoreRow',index);
    if(value=='1'){
        $("#datalist").jqGrid('delRowData',index);
    }else{
    	var data={fid:fid,editing:false,action:null};
        $('#datalist').jqGrid('setRowData',index,data);
    }
}

$(function(){
	//创建列表
	var _url='';
	if(_billCode=="bmjb"){
		 _url=getRootPath()+'/flow/security/query';
	}else if(_billCode=="sjfl"){
		_url=getRootPath()+'/flow/taskType/query';
	}else if(_billCode=="sjjb"){
		_url=getRootPath()+'/flow/taskLevel/query';
	}		
	$('#datalist').jqGrid({ 
		datatype:function(postdata){
			$.ajax({
				url:_url,
				data:postdata,
			    dataType:"json",
			    complete: function(data,stat){
			    	if(stat=="success") {
			    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			    		$("#datalist")[0].addJSONData(data.responseJSON);
			    	}
			    }
			});
		},
		autowidth:true,
		height:$(window).outerHeight()-200+"px",
		pager:"#pager",
		rowNum:10,
		rowList:[10,20,30],
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},
		colModel:[
		    {name:'isNew',label : 'isNew',hidden:true},
		    {name:'editing',label : 'editing',hidden:true},
	        {name:'fid',label:'fid',hidden:true,width:100},
	        {name:'updateTime',label:'updateTime',hidden:true,width:100},	       
	        {name:'code',label:'编号',width:100,align:'center',editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({validType:'maxLength[50]',novalidate:true,missingMessage:"该项必填",required:true,width:'100%',height:'100%'});
	        }}},
	        {name:'name',label:'名称',width:100,align:'center',editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({validType:'maxLength[50]',missingMessage:"该项必填",novalidate:true,required:true,width:'100%',height:'100%'});
	        }}},
	        {name:'level',label:'级别',width:100,align:'center',editable:true,edittype:"text",hidden:true,editoptions:{dataInit:function(ed){
                $(ed).textbox({validType:['accessory','maxLength[50]'],novalidate:true,missingMessage:"该项必填",required:true,width:'100%',height:'100%'});
            }}},
	        {name:'checkoutStatus',label:'状态',width:100,align:'center',editable:true,edittype:"text",hidden:true,formatter:function(value){
              	var str="";
            	if(value == '0'){
            		str='停用';
            	}else if(value == '1'){
            		str='启用';
            	}else{
            		str=value;
            	}
            	return str;
            }},
	        {name:'price',label:'描述',width:100,align:'center',hidden:true,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({validType:'maxLength[200]',width:'100%',height:'100%'});
	        }}},  
	        {name:'describe',label:'描述',width:100,align:'center',hidden:true,editable:true,edittype:"text",editoptions:{dataInit:function(ed){
	        	$(ed).textbox({validType:'maxLength[200]',width:'100%',height:'100%'});
	        }}},
	        {name:'action',label:'操作',width:100,align:'center',formatter:operate},
	    ],onCellSelect:function(rowid,iCol,cellcontent,e){
	    	editRow(rowid,e.checkoutStatus);
        }
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

	//设置列显示
	if(_billCode=="sjfl"){
		jQuery("#datalist").setGridParam().showCol("describe").trigger("reloadGrid");
	}else if(_billCode=="bmjb"||_billCode=="sjjb"){
		jQuery("#datalist").setGridParam().showCol("level").trigger("reloadGrid");
		jQuery("#datalist").setGridParam().showCol("checkoutStatus").trigger("reloadGrid");
		jQuery("#datalist").setGridParam().showCol("describe").trigger("reloadGrid");
	}
	//设置后面显示列宽度自适应
	$("#datalist").setGridWidth($(window).width()*0.99);　　
	$("#datalist").setGridWidth(document.body.clientWidth*0.99);　	

	function reLength() {
		var temp=0;
		var trs= $('#datalist tr[role="row"]');
		for(var i=0;i<trs.length;i++){
			if(Number(trs[i].id)>temp){
				temp = Number(trs[i].id);
			}else{
				continue;
			}
		}
		return temp;
    }
	//新增事件
	$('#addNew').click(function(){
		$("#datalist").jqGrid('addRowData',Number(reLength()+1),{checkoutStatus:1,isNew:1},'first');
		editRow(Number(reLength()))

		
		$('#addNew').keydown(function(event) {
			if (event.keyCode == 13) {
	        return false;
	   }
	});		
});
		
	//当前状态是启用还是停用
	function state(value,row,index){
		for(var i=0; i<opTion.length; i++){
			if (opTion[i].id == value) return opTion[i].name;
		}
		return value;
	}
		
		
	//初始化查询框
	$("#search-code").textbox({
		prompt:'编号或名称',
		width:160,
		height:32
	});
	
	//查询
	$('.Inquiry').click(function(){	//事件分类	
		var code=$("#search-code").textbox('getValue');
		var options = {"searchKey":code};
		$('#datalist').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	});
		
	//导入
	$("#getIn").click(function(){
		var s={
				target:$("#importBox"),
				boxTitle:"导入保密级别",
				downHref:"${ctx}/ExcelMapController/downTemplate?downFile=保密级别.xls",
				action:"${ctx}/member/import",
				fn:'okCallback()'
		};
		importBox(s);
	});
	
	function okCallback(){
		$('#datalist').trigger('reloadGrid');
	}
	
	//导出
	$("#getOut").click(function(){
		exportFrom('${ctx}/member/export',{"searchKey": $('#search-code').val()});
	});
	
	//打印按钮
	$("#print").click(function(){
		var search_code= $('#search-code').val();
		printWage(search_code);
	});	
		
});

//消息预警配置	
	//操作按钮
	function handle(value,options,row){
		var e= '<a class="btn-edit" title="编辑" href="javascript:;" onclick="compile(this,\''+row.fid+'\',1)"></a>';
		var r = "<a href='javascript:;' class='btn-del' onclick='deleteRow(this,\""+row.fid+"\")' title='删除'></a>";
		if(row.isSystem=='1'){
			return e;
		}else{
			return e+r;
		}
	}

function sendType(value,options,row){
	for(var i=0; i<send.length; i++){
		if (send[i].id == value) return send[i].name;
	}
	return value;	
}
//是否系统配置
function isSystemType(value,options,row){
	for(var i=0; i<System.length; i++){
		if (System[i].id == value)
			return System[i].name;
	}
	return value;
}
//消息类型
function MessageType(value,options,row){
	for(var i=0; i<message.length; i++){
		if (message[i].id == value) return message[i].name;
	}
	return value;	
}

//触发类型 
function TriggerType(value,options,row){
	for(var i=0; i<trigger.length; i++){
		if (trigger[i].id == value) return trigger[i].name;
	}
	return value;	
}

//触发场景
function BusSceneValue(value,options,row){
	for(var i=0;i<busSceneval.length;i++){
		if(busSceneval[i].id==value) return busSceneval[i].name;
	}
	return value;
}

//事件类型
function EventType(value,options,row){
	for(var i=0; i<events.length; i++){
		if (events[i].id == value) return events[i].name;		
		if(value==''||value==null||value=="undefined "){
			return '';
		}
	}	
	return value;
}
//编辑
function compile(target,fid,mark){
	var url=getRootPath()+'/flow/msgSetting/edit?mark=1&id='+fid+'&flag=edit';
	warehouseWin("编辑消息预警",url);
	if($('#addBox').is(":visible")==true){
		$('html').css('overflow','hidden');
	}
}
//删除
function deleteRow(target,fid){
	var _url=getRootPath()+'/flow/msgSetting/delete?id='+fid;
	$.fool.confirm({title:'提示',msg:"确认删除记录？",fn:function(data){
		if(data){
			$.post(_url,function(data){	
				dataDispose(data);
				if(data.returnCode == '0'){
				    $('#Tablelist').trigger('reloadGrid');
				    $.fool.alert({time:1000,msg:'删除成功'});
				}else if(data.returnCode = '1'){
					$.fool.alert({msg:data.message});
				}else{
					$.fool.alert({msg:'服务器繁忙，请稍后再试'});
				}
			},'json');
		}else{
			return false;
		}
	}});
}

//保存
// function saver(){
	//var data=$("#form").serialize();
	// var fid=$('#fid').val();
    // var taskLevel=$('#taskLevel').val();
    // var days=$('#days').val();
    // var retryDays=$('#retryDays').val();
    // var range=$('#range').val();
    // var sendType=sendTypeValue.getSelectedValue();
    // var triggerType=triggerTypeName.getSelectedValue();
    // var taskType=taskTypeName.getSelectedValue();
    // var toSuperior=toSuperiorName.getSelectedValue();
    // var toSubordinate=toSubordinateName.getSelectedValue();
    // var busScene=busSceneName.getSelectedValue();
    // var isSystem=isSystemName.getSelectedValue();
    // //var superviseId=superviseIdName.getSelectedValue();
    // var superviseIds=$('#superviseIds').val();
    // var _url=getRootPath()+'/flow/msgSetting/save';
    //
	// $('#form').form('enableValidation');
    //
    // if($('#form').form('validate')){
     //    $('#save').removeAttr("disabled","disabled");
	// 	$.post(_url,{fid:fid,taskLevel:taskLevel,days:days,retryDays:retryDays,range:range,sendType:sendType,triggerType:triggerType,taskType:taskType
	// 	 ,toSuperior:toSuperior,toSubordinate:toSubordinate,busScene:busScene,isSystem:isSystem,superviseIds:superviseIds},function(data){
	// 		 dataDispose(data);
	// 		if(data.returnCode==0){
	// 			$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
	// 				$("#addBox").window("close");
	// 				 $('#Tablelist').trigger('reloadGrid');
	// 				$('html').css('overflow','');
	// 			}});
	// 			}else if(data.returnCode==1){
	// 				$.fool.alert({msg:data.message});
	// 			};
	// 		});
	// }else{
     //    $('#save').removeAttr("disabled");
     //    return false;
	// }
// };

















	