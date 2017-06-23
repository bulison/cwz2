<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人员弹出窗口</title>
</head>
<body>

<div class="window-search-box" style="margin:5px;">
<input id="member-code" class="easyui-textbox" data-options="prompt:'人员编号',width:100,height:30"/>
<input id="member-name" class="easyui-textbox" data-options="prompt:'人员名称',width:100,height:30"/>
<input id="member-phone" class="easyui-textbox" data-options="prompt:'人员电话',width:100,height:30"/>
<a href="javascript:;" class="btn-blue btn-s go-search">筛选</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>

<%-- <table class="easyui-datagrid" id="member-table" data-options="idField:'fid',fitColumns:true,pagination:true,url:'${ctx}/member/list?deptId=${param.deptId}'">
<thead>
<tr>
<th data-options="field:'fid',checkbox:true,width:10">fid</th>
<th data-options="field:'userCode',width:10,sortable:false">人员编号</th>
<th data-options="field:'jobNumber',width:10,sortable:false">人员工号</th>
<th data-options="field:'username',width:10,sortable:false">人员名称</th>
<th data-options="field:'phoneOne',width:10,sortable:false">人员电话</th>
<th data-options="field:'deptName',width:10,sortable:false">部门</th>
<th data-options="field:'position',width:10,sortable:false">职位</th>

<th data-options="field:'isInterface',width:10,hidden:true">部门负责人</th>
<th data-options="field:'sex',width:10,hidden:true">性别</th>
<th data-options="field:'email',hidden:true,width:10">Email</th>
<th data-options="field:'postcode',hidden:true,width:10">邮政编码</th>
<th data-options="field:'address',hidden:true,width:10">地址</th>
<th data-options="field:'fax',hidden:true,width:10">传真</th>
<th data-options="field:'idCard',hidden:true,width:10"></th>
<th data-options="field:'isWebLogin',hidden:true,width:10">允许WEB端登陆</th>
<th data-options="field:'isMobileLogin',hidden:true,width:10">允许移动端登陆</th>
<th data-options="field:'phoneTwo',hidden:true,width:10">人员电话2</th>
<th data-options="field:'userDesc',hidden:true,width:10"></th>
<th data-options="field:'departureReason',hidden:true,width:10"></th>
<th data-options="field:'createTime',hidden:true,width:10">创建时间</th>
<th data-options="field:'updateTime',hidden:true,width:10">更新时间</th>
<th data-options="field:'enable',hidden:true,width:10"></th>
<th data-options="field:'deptId',hidden:true,width:10">部门ID</th>
<th data-options="field:'orgId',hidden:true,width:10">机构ID</th>
<th data-options="field:'orgName',hidden:true,width:10">机构名</th>
</tr>
</thead>
</table> --%>

 <table id="member-table"></table>
<div id="pager2"></div> 
<script type="text/javascript">
    var multiselect;
 if('${param.singleSelect}'=='true')
     multiselect=false;
 else
     multiselect=true;

 $('#member-table').jqGrid({	
	 datatype:function(postdata){
		$.ajax({
			url:'${ctx}/member/list?deptId=${param.deptId}',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){		    	
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#member-table")[0].addJSONData(data.responseJSON);
		    	}
		    }
		});
	}, 
	height:380,
	width:1052,
	pager:"#pager2",
	multiselect:multiselect,
	rowNum:10,
	rowList:[10,20,30], 
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	colModel:[       
	  		{name:'fid',label:'fid',hidden:true,width:100,align:'center'},	 
	  		{name:'userCode',label:'人员编号',sortable:false,width:100,align:'center'},
	  		{name:'jobNumber',label:'人员工号',sortable:false,width:100,align:'center'},	  			  		 	
	  		{name:'username',label:'人员名称',sortable:false,width:100,align:'center'},
	  		{name:'phoneOne',label:'人员电话',width:100,sortable:false,align:'center'},
	  		{name:'deptName',label:'部门',width:100,sortable:false,align:'center'},
	  		{name:'position',label:'职位',width:100,align:'center'},
	  		{name:'isInterface',label:'部门负责人',hidden:true,width:100,align:'center'},
	  		{name:'sex',label:'性别',hidden:true,width:100,align:'center'},
	  		{name:'email',label:'Email',hidden:true,width:100,align:'center'},
	  		{name:'postcode',label:'邮政编码',hidden:true,width:100,align:'center'},
	  		{name:'address',label:'地址',hidden:true,width:100,align:'center'},
	  		{name:'fax',label:'传真',width:100,hidden:true,sortable:false,align:'center'},
	  		{name:'idCard',label:'idCard',hidden:true,width:100,align:'center'},
	  		{name:'isWebLogin',label:'允许WEB端登陆',hidden:true,width:100,align:'center'},
	  		{name:'isMobileLogin',label:'允许移动端登陆',hidden:true,sortable:false,width:100,align:'center'},
	  		{name:'phoneTwo',label:'人员电话2',hidden:true,sortable:false,width:100,align:'center'},
	  		{name:'userDesc',label:'userDesc',hidden:true,width:100,align:'center'},
	  		{name:'departureReason',label:'departureReason',hidden:true,width:100,sortable:false,align:'center'},
	  		{name:'createTime',label:'创建时间',hidden:true,width:100,align:'center'},
	  		{name:'updateTime',label:'更新时间',hidden:true,width:100,align:'center'},
	  		{name:'enable',label:'enable',hidden:true,width:100,align:'center'},
	  		{name:'orgId',label:'机构ID',hidden:true,width:100,align:'center'},
	  		{name:'orgName',label:'机构名',hidden:true,width:100,align:'center'},	  			  		
	      ],ondblClickRow:function(rowid,iRow,iCol,e){
				var row = $('#member-table').jqGrid("getRowData",rowid);
				if('${param.onDblClick}'.length>0){
				     eval(${param.onDblClick}(row,"${param.other}"));
				}
			},
}).navGrid('#pager2',{add:false,del:false,edit:false,search:false,view:false});

  $(function(){
 	/* $('#member-table').jqGrid({
		<c:if test='${param.singleSelect}'>singleSelect:true,</c:if>
		ondblClickRow:function(rowid,iRow,iCol,e){
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(e,"${param.other}"));
			}
		}
	});  */
	
	$("#member-code").textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					search();
				}
			}
		})
	})
	 $("#member-phone").textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					search();
				}
			}
		})
	})
	$("#member-name").textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					search();
				}
			}
		})
	})
//setPager($('#member-table'));
	
	$(".go-search").click(function(){
		search();
	}); 
    function search(){
    	var code=$("#member-code").textbox('getValue');
		var name=$("#member-name").textbox('getValue');
		var phone=$("#member-phone").textbox('getValue');
		var deptId= "${param.deptId}".length == 0 ? null : "${param.deptId}";
		var options = {"userCode":code,"username":name,"phoneOne":phone};
		 $('#member-table').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
    }
	/*  $(".select-ok").click(function(){
		var rows = $('#member-table').jqGrid('',1);
		alert(JSON.stringify(rows));
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		 if('${param.okCallBack}'.length>0){			
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});  */
    $(".select-ok").click(function(){
		var rowids = $('#member-table').jqGrid('getGridParam', 'selrow');
		<c:if test='${!param.singleSelect}'>rowids = $('#member-table').jqGrid('getGridParam','selarrrow');</c:if>
		var rows = [];
		for(var i=0;i<rowids.length;i++){
			var row = $('#member-table').jqGrid("getRowData",rowids[i]);
			rows.push(row);
		}
		
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
}); 
</script>

</body>
</html>
