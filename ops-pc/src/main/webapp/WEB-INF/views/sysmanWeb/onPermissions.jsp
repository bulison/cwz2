<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
          <div style="text-align:center">
           <table id="resource"></table>
           <div id="pager1"></div>
           <a id='yes' href="javascript:;" class="btn-blue4 btn-s" style="margin:10px">选定</a>
           <a id='permi' href="javascript:;" class="btn-blue4 btn-s" style="margin:10px">授权</a>
           <a id='close' href="javascript:;" class="btn-blue4 btn-s" style="margin:10px">关闭</a>
          </div>

<script type="text/javascript">
var resKey = false;
var yesNo = [
  		    {id:'0',name:'菜单目录'},
  		    {id:'1',name:'菜单项'},
  		    {id:'2',name:'URL'},
  		    {id:'3',name:'操作'}
  		];
var dateFilter = [
   		    {value:'1',text:'查看我(创建人)自已的'},
   		    {value:'2',text:'查看本部门的（不包含下属部门）'},
   		    {value:'3',text:'查看本部门的（包含下属部门）'},
   		    {value:'4',text:'查看其它部门的(默认本部门及本部门的下属部门的也能查看)'},
   		    {value:'5',text:'查看本单位的'}
   		];
$('#resource').jqGrid({
	datatype:function(postdata){
		if(showKey == 0){ //修复加载窗口后，自动点击根节点会出现加载2次导致载入选择项失效的问题
		showKey++;
		return;
		}
  		$.ajax({
  			url:'${ctx}/resourceController/getResChilds?fid='+'${resId}'+"&roleId=${roleId}",
  			data:postdata,
  			dataType:"json",
  			complete: function(data,stat){
  				if(stat=="success") {
  					$("#resource")[0].addJSONData(data.responseJSON);
  				}
  			}
  		});
  	},
	onSelectRow:function(rowId,status){
		var row = $('#resource').jqGrid("getRowData",rowId);
		if(!status && !resKey){
			//unCheckUserID=row.fid+','+unCheckUserID;
			$("#resource").jqGrid("restoreRow",rowId);
		}else if(status && $('#resource tr#'+rowId).find("input").length == 1){
			editrow(rowId);
			var ed=$("#resource #"+rowId+"_dateFilter");
			if(!row.dateFilter){
				ed.next()[0].comboObj.setComboValue(5);
			}else{
				ed.next()[0].comboObj.setComboValue(row.dateFilter);
			}
		}else if(!status && resKey){
			$("#resource").jqGrid("setSelection",rowId);
		}
	},
	onCellSelect:function(rowid,iCol,cellcontent,e){
		if(iCol == 5){
			resKey = true;
		}else{
			resKey = false;
		}
	},
	autowidth:true,//自动填满宽度
	height:"100%",
	pager:'#pager1',
	rowNum:10000,
	pgbuttons:false,
	pginput:false,
	viewrecords:true,
	multiselect:true,
	forceFit:true,//调整列宽度，表格总宽度不会改变
	height:450,
	colModel:[
	          {name:'resName',label:'资源名称',align:"center",sortable:false,width:100},
	          {name:'resType',label:'资源类型',align:"center",sortable:false,width:100,formatter:function(value){
					for(var i=0; i<yesNo.length; i++){
						if (yesNo[i].id == value) return yesNo[i].name;
					}
					return value;
				}},
	          {name:'resString',label:'链接或JS函数',align:"center",sortable:false,width:300},
	          {name:'action',label:'功能描述',align:"center",sortable:false,width:100},
	          {name:'dateFilter',label:'数据过滤方式',align:"center",sortable:false,width:100,formatter:function(value){
	        	  for(var i=0; i<dateFilter.length; i++){
	        		  if (dateFilter[i].value == value) return dateFilter[i].text;
	        	  }
	        	  if(value)return value; else return "";
	          },unformat:function(value){
	        	  for(var i=0; i<dateFilter.length; i++){
	        		  if (dateFilter[i].text == value) return dateFilter[i].value;
	        	  }
	        	  return "";
	          },editable:true,edittype:"text"/* ,editor:{type:"combobox",options:{data:dateFilter,editable:false}} */},
	          {name:'fid',label:'fid',hidden:true,width:100}
	],
	gridComplete:function(){
		/* if(showKey == 0){ //修复加载窗口后，自动点击根节点会出现加载2次导致载入选择项失效的问题
			showKey++;
			return;
		} */
		$.post("${ctx}/resourceController/getResRole",{fid:"${roleId}"},function(data){
			for(var i=0;i<data.length;i++){
				var rowId = $('#resource').find("tr.jqgrow td[title="+data[i]+"]").parent().attr("id");
				if(rowId){
					$("#resource").jqGrid('setSelection',rowId);
				}
		     }
		});
	},
	/* onCheck:function(index, row){
		$("#resource").datagrid("beginEdit",index);
		if(!row.dateFilter){
			var ed=$("#resource").datagrid("getEditor",{index:index,field:"dateFilter"});
			$(ed.target).combobox("setValue",5);
		}
	},
	onUncheck:function(index, row){
		$("#resource").datagrid("cancelEdit",index);
	}, */
	/* onSelectAll:function(rowIds,status){
		var rows = $('#resource').jqGrid("getRowData");
		if(status){
			for(var i=0;i<rowIds.length;i++){
				editrow(i+1);
				var ed = $("#resource #"+(i+1)+"_dateFilter");
				if(!rows[i].dateFilter){
					ed.next()[0].comboObj.setComboValue(5);
				}else{
					ed.next()[0].comboObj.setComboValue(rows[i].dateFilter);
				}
			}
		}else{
			for(var i=0;i<rowIds.length;i++){
				$("#resource").jqGrid("restoreRow",i+1);
			}
		}
	} *//* ,
	onUncheckAll:function(rows){
		for(var i=0;i<rows.length;i++){
			$("#resource").datagrid("cancelEdit",i);
		}
	} */
	onSelectAll:function(aRowids,status){
		var rowId="";
		var row="";
		var ed=""; 
		for(var i=0;i<aRowids.length;i++){
			rowId=aRowids[i];
			row = $('#resource').jqGrid("getRowData",rowId);
			if(!status && !resKey){
				$("#resource").jqGrid("restoreRow",rowId);
			}else if(status && $('#resource tr#'+rowId).find("input").length == 1){
				editrow(rowId);
				ed=$("#resource #"+rowId+"_dateFilter");
				if(!row.dateFilter){
					ed.next()[0].comboObj.setComboValue(5);
				}else{
					ed.next()[0].comboObj.setComboValue(row.dateFilter);
				}
			}else if(!status && resKey){
				$("#resource").jqGrid("setSelection",rowId);
			}
			rowId="";
			row="";
			ed="";
		}
	},
	
}).navGrid('#pager1',{add:false,del:false,edit:false,search:false,view:false});

function editrow(rowId){
	$("#resource").jqGrid("editRow",rowId);
	$('#resource #'+rowId+"_dateFilter").fool("dhxCombo",{
		data:dateFilter,
		editable:false,
		clearOpt:false,
		width:"100%",
		height:"80%"
	});
}

 $("#yes").click(function(e) {
	 $('#yes').attr("disabled","disabled");
	 var checked=[];
	 var index="";
	 var fid="";
	 var dateFilter="";
	 var rowIds = $("#resource").jqGrid('getGridParam', 'selarrrow'); 
	 for (var i=0;i<rowIds.length;i++){
		 //index=$("#resource").datagrid("getRowIndex",checkRes[i]);
		 var row=$("#resource").jqGrid('getRowData',rowIds[i]);
		 var _dateFilter = $('#resource #'+rowIds[i]+"_dateFilter").next()[0].comboObj.getSelectedValue();
		 //$("#resource").jqGrid("saveRow",rowIds[i]);不需要确定行再保存
		 fid=row.fid;
		 dateFilter=_dateFilter;
		 checked.push({fid:fid,dateFilter:dateFilter});
	 }
	 $.post("${ctx}/userController/functionRole",{resId:"${resId}",roleId:"${roleId}",check:JSON.stringify(checked)},function(data){
		 dataDispose(data);
		 if(data.result =='0'){
			 $.fool.alert({msg:'保存成功！',fn:function(){
				 $('#yes').removeAttr("disabled");
				 $('#resource').trigger('reloadGrid');
			 }});
		 }else if(data.result =='1'){
			 $.fool.alert({msg:data.msg});
			 $('#yes').removeAttr("disabled");
			 $('#resource').datagrid('reload');
		 }else{
			 $.fool.alert({msg:"系统正忙，请稍后再试。"});
			 $('#yes').removeAttr("disabled");
		 }
	 });
	 /* $.fool.alert({msg:"保存成功"}); */
});
 
 $('#permi').click(function(){
	 $.messager.progress({
			text:'努力运行中，请稍后...'
	 });
	 $('#permi').attr("disabled","disabled");
	 var checked=[];
	 var index="";
	 var fid="";
	 var dateFilter="";
	 var rowIds = $("#resource").jqGrid('getGridParam', 'selarrrow'); 
	 for (var i=0;i<rowIds.length;i++){
		// index=$("#resource").datagrid("getRowIndex",checkRes[i]);
		 var row=$("#resource").jqGrid('getRowData',rowIds[i]);
		 var _dateFilter = $('#resource #'+rowIds[i]+"_dateFilter").next()[0].comboObj.getSelectedValue();
		 fid=row.fid;
		 dateFilter=_dateFilter;
		 checked.push({fid:fid,dateFilter:dateFilter});
	 }
	 
	 $.post("${ctx}/userController/functionRole",{resId:"${resId}",roleId:"${roleId}",subRes:1,check:JSON.stringify(checked)},function(data){
		 dataDispose(data);
		 if(data.result =='0'){
			 $.fool.alert({msg:'保存成功！',fn:function(){
				 $.messager.progress('close');
				 $('#permi').removeAttr("disabled");
				 $('#resource').trigger('reloadGrid');
			 }});
		 }else if(data.result =='1'){
			 $.messager.progress('close');
			 $.fool.alert({msg:data.msg});
			 $('#permi').removeAttr("disabled");
			 $('#resource').trigger('reloadGrid');
		 }else{
			 $.messager.progress('close');
			 $.fool.alert({msg:"系统正忙，请稍后再试。"});
			 $('#permi').removeAttr("disabled");
		 }
	 });
 });
 
 $("#close").click(function(e){
	 $("#permissions").window('close');
 });
</script>
</body>
