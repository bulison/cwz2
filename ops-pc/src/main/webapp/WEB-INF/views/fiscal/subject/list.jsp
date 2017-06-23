<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
  <table id="subjectList"></table>
  <div id="pager"></div>
<script type="text/javascript">
var yesNo=[{value:0,text:"否"},{value:1,text:"是"}];
var direction = [{value:'1',text:"借方"},{value:'-1',text:"贷方"}];
var type=[{value:"1",text:'资产'},{value:"2",text:'负债'},{value:"3",text:'共同'},{value:"4",text:'所有者权益'},{value:"5",text:'成本'},{value:"6",text:'损益'}];

//引入窗口控件初始化
$("#leadInBox").window({
	  title:"引入信息",
	  collapsible:false,
	  minimizable:false,
	  maximizable:false,
	  closed:true,
	  modal:true,
	  top:65,
	  left:230,
	  width:$(content).width()-30,
});

//子科目列表控件初始化
  $("#subjectList").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: "${ctx}/fiscalSubject/getList",
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#subjectList")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		/* footerrow: true, */
		autowidth:true,
		shrinkToFit:false,
		height:"100%",
		pager:"#pager",
		rowNum:10,
		rowList:[10,20,30],
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[ 
                    {name : 'fid',label : 'fid',hidden:true}, 
                    {name : 'code',label : '科目编号',align:'center',width:"100px",frozen:true},
                    {name : 'name',label : '科目名称',align:'center',width:"100px",frozen:true,<fool:tagOpt optCode="fsubjectAction1">formatter:function(cellvalue, options, rowObject){
                    	  return '<a href="javascript:;" onclick="editrow(\''+rowObject.fid+'\')">'+cellvalue+'</a>';
                    }</fool:tagOpt>},
                    <fool:tagOpt optCode="fsubjectAction">
                    {name:"action",label:"操作",align:'center',frozen:true,formatter:function(cellvalue, options, rowObject){
                		var e='',d='',l='';
                		<fool:tagOpt optCode="fsubjectAction1">e='<a href="javascript:;" title="编辑" class="btn-edit" onclick="editrow(\''+rowObject.fid+'\')" ></a>';</fool:tagOpt>
                		<fool:tagOpt optCode="fsubjectAction2">d='<a href="javascript:;" title="删除" class="btn-del" onclick="deleterow(\''+rowObject.fid+'\',\''+rowObject.parentId+'\')" ></a>';</fool:tagOpt>
                		<fool:tagOpt optCode="fsubjectAction3">l='<a href="javascript:;" title="引入" class="btn-in" onclick="leadInrow(\''+rowObject.fid+'\')" ></a>';</fool:tagOpt>
                		return d+" "+l;
                    }},
		            </fool:tagOpt>
                    {name : 'type',label : '科目类型',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<type.length; i++){
                    		if (type[i].value == cellvalue) return type[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'subjectName',label : '科目类别',align:'center',width:"100px"}, 
                    {name : 'zjm',label : '助记码',align:'center',width:"100px"},
                    {name : 'direction',label : '余额方向',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<direction.length; i++){
                    		if (direction[i].value == cellvalue) return direction[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'cashSign',label : '核算现金流量',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'cussentAccountSign',label : '核算往来帐',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'supplierSign',label : '核算供应商',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'customerSign',label : '核算客户',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'departmentSign',label : '核算部门',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'memberSign',label : '核算职员',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'warehouseSign',label : '核算仓库',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'projectSign',label : '核算项目',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'goodsSign',label : '核算货品',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'quantitySign',label : '核算数量',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'unitName',label : '单位',align:'center',width:"100px"}, 
                    {name : 'currencySign',label : '核算外币',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
                    	for(var i=0; i<yesNo.length; i++){
                    		if (yesNo[i].value == cellvalue) return yesNo[i].text;
                    	}
                    	if(!cellvalue){
                    		return "";
                    	}
                    	return cellvalue;
                    }},
                    {name : 'currencyName',label : '币别',align:'center',width:"100px"}, 
                    {name : 'describe',label : '描述',align:'center',width:"100px"}, 
                  ],
	}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
  
  
  //编辑按钮点击事件
  function editrow(fid){
	  $("#editBox").window('setTitle',"修改科目");
	  $('#editBox').window('open');
	  $('#editBox').window('refresh',"${ctx}/fiscalSubject/edit?fid="+fid);
  };
  
  //删除按钮点击事件
  function deleterow(fid,parentId){
  	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
  		 if(r){
  			 $.ajax({
  					type : 'post',
  					url : '${ctx}/fiscalSubject/delete',
  					data : {fid : fid},
  					dataType : 'json',
  					success : function(data) {	
  						dataDispose(data);
  						if(data.result == 0){
  							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){									
  								$('#subjectList').trigger("reloadGrid");
  								var searchType=$("#searchType").combobox("getValue");
  								if(searchType!=2){
  									$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:$("#search").searchbox("getValue")},function(data){
  										$('#subjectTree').tree('loadData',data);
  										var node=$("#subjectTree").tree("find",parentId);
  										$("#subjectTree").tree("expandTo",node.target);
  										$(node.target).click();
  									});
  								}else{
  									$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:$("#search").combobox("getValue")},function(data){
  										$('#subjectTree').tree('loadData',data);
  										var node=$("#subjectTree").tree("find",parentId);
  										$("#subjectTree").tree("expandTo",node.target);
  										$(node.target).click();
  									});
  								}
  							}});
  						}else{
  							$.fool.alert({msg:data.msg});
  						}
  		    		},
  		    		error:function(){
  		    			$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
  		    		}
  				});
  		 }
  	 }});
  }
  
//引入按钮点击事件
function leadInrow(fid){
	$('#leadInBox').window('open');
	$('#leadInBox').window('refresh',"${ctx}/fiscalSubject/leadIn?fid="+fid);
};
</script>
</body>
</html>