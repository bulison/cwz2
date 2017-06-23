<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>货品弹出窗口</title>
</head>
<body>
<div id="winForm" class="window-search-box" style="margin:5px;">
<input id="good-name" class="easyui-textbox" data-options="prompt:'货品名称',width:100,height:30"/>
<input id="good-code" class="easyui-textbox" data-options="prompt:'货品编号',width:100,height:30"/>
<a href="javascript:;" class="btn-blue btn-s go-search">筛选</a>
<a href="javascript:;" onclick="gwinClear()" class="btn-blue btn-s">清空</a>
<a href="javascript:;" id="wAdd" class="btn-blue btn-s" style="display:none;">添加</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>

<table id="goods-table">
</table>
<div id="goodspager"></div>
<script type="text/javascript">
function gwinClear(){
	$('#good-name').textbox("clear");
	$('#good-code').textbox("clear");
}
$(function(){
	var pgChos = [];
	var rowChos = [];
	var gridWidth=940; 
	if($("#pop-win").length>0){
		gridWidth=$("#pop-win").width()-10;
	}
	$('#goods-table').jqGrid({
		<c:if test='${!param.singleSelect}'>multiselect:true,</c:if>
		datatype:function(postdata){
			$.ajax({
				url:'${ctx}/goods/getChilds?billType=${param.billType}&customerId=${param.customerId}&supplierId=${param.supplierId}&nids=${param.goodsId}',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#goods-table")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		forceFit:true,
		pager:'#goodspager',
		//rowList:[ 10, 20, 30 ],
		viewrecords:true,
		rowNum:10,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		}, 
		width:gridWidth, 
		height:340,
		colModel:[
				{name:'fid',label:'fid',align:"center",hidden:true,width:100},
				{name:'barCode',label:'货品条码',align:"center",sortable:false,width:100},
				{name:'code',label:'货品编号',align:"center",sortable:false,width:100},
				{name:'name',label:'货品名称',align:"center",sortable:false,width:100},
				{name:'spec',label:'规格',sortable:false,align:"center",width:100},
				{name:'goodsSpecName',label:'属性组',sortable:false,hidden:true,width:100},
				{name:'unitName',label:'单位',sortable:false,width:100},
				{name:'unitScale',label:'换算关系',sortable:false,width:100},
				{name:'describe',label:'备注',sortable:false,width:100},
				{name:'orgId',label:'机构ID',sortable:false,hidden:true,width:100},
				{name:'goodsSpecGroupId',label:'货品属性组ID',sortable:false,hidden:true,width:100},
				{name:'goodsSpecId',label:'货品属性ID',sortable:false,hidden:true,width:100},
				{name:'unitGroupId',label:'单位组ID',sortable:false,hidden:true,width:100},
				{name:'unitId',label:'单位ID',sortable:false,hidden:true,width:100},
				{name:'flag',label:'标识',sortable:false,hidden:true,width:100},
				{name:'accountFlag',label:'账户标识',sortable:false,hidden:true,width:100},
				{name:'useFlag',label:'用户标识',sortable:false,hidden:true,align:"center",width:100},
				{name:'parentId',label:'上级货品ID',align:"center",sortable:false,hidden:true,width:100},
				{name:'parentName',label:'上级货品',sortable:false,align:"center",hidden:true,width:100},
				{name:'creatorId',label:'创建人ID',align:"center",sortable:false,hidden:true,width:100},
				{name:'creatorName',label:'创建人',align:"center",sortable:false,hidden:true,width:100},
				{name:'createTime',label:'创建时间',align:"center",sortable:false,hidden:true,width:100},
				{name:'updateTime',label:'更新时间',align:"center",sortable:false,hidden:true,width:100},
				{name:'recordStatus',label:'记录状态',align:"center",sortable:false,hidden:true,width:100},
				{name:'lowestPrice',label:'货品的最低销售价',align:"center",sortable:false,hidden:true,width:100},
				{name:'referencePrice',label:'货品的参考价格',align:"center",sortable:false,hidden:true,width:100},
		          ],
		ondblClickRow:function(rowid,iRow,iCol,e){
			var row = $('#goods-table').jqGrid("getRowData",rowid);
			if('${param.onDblClick}'.length>0){
			     eval(${param.onDblClick}(row,"${param.other}"));
			}
		},
		<c:if test='${!param.singleSelect}'>
		onSelectAll:function(aRowids,status){
			var num = $('#goods-table').jqGrid('getGridParam').page;
			if(status){
				var rowids = $('#goods-table').jqGrid('getDataIDs')
				pgChos[num] = rowids;
				var rows = $('#goods-table').jqGrid("getRowData");
				/* for(var i=0;i<rowids.length;i++){
					var row = $('#goods-table').jqGrid("getRowData",rowids[i]);
					rows.push(row);
				} */
				rowChos[num] = rows;
			}else{
				pgChos[num] = [];
				rowChos[num] = [];
			}
		},
		</c:if>
		onSelectRow:function(rowid,status){
			<c:if test='${!param.singleSelect}'>
			var rowids = $('#goods-table').jqGrid('getGridParam', 'selarrrow');
			var num = $('#goods-table').jqGrid('getGridParam').page;
			pgChos[num] = rowids;
			var rows = [];
			for(var i=0;i<rowids.length;i++){
				var row = $('#goods-table').jqGrid("getRowData",rowids[i]);
				rows.push(row);
			}
			rowChos[num] = rows;
			</c:if>
		},
		gridComplete:function(){
			<c:if test='${!param.singleSelect}'>
			var num = $('#goods-table').jqGrid('getGridParam').page;
			if(pgChos[num]){
				var rowids = pgChos[num];
				for(var i = 0; i < rowids.length; i++){
					$('#goods-table').jqGrid('setSelection',rowids[i],false);
				}
			}
			</c:if>
		}
	});

	$("#good-name").textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					search();
				}
			}
		})
	})
	$("#good-code").textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode==13){
					search();
				}
			}
		})
	})
	$(".go-search").click(function(){
		var name=$("#good-name").textbox('getValue');
		var code=$("#good-code").textbox('getValue');
		var options = {"name":name,"code":code};
		pgChos = [];
		rowChos = [];
		$('#goods-table').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	});
	function search(){
		var name=$("#good-name").textbox('getValue');
		var code=$("#good-code").textbox('getValue');
		var options = {"name":name,"code":code};
		pgChos = [];
		rowChos = [];
		$('#goods-table').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	}
	if(typeof _billCode != "undefined" || "${param.addKey}" == "1"){
		$('#wAdd').click(function(){
			var rowids = $('#goods-table').jqGrid('getGridParam',"selrow");
			var rows = [];
			if(rowids!=null){
				for(var i=0;i<rowids.length;i++){
					var row = $('#goods-table').jqGrid("getRowData",rowids[i]);
					rows.push(row);
				}
			}
			<c:if test='${!param.singleSelect}'>
			rows = [];
			for(var i  in rowChos){
				rows = rows.concat(rowChos[i]);
			}
			</c:if>
			if(rows.length==0){
				$.fool.alert({msg:'你没有选择任何数据！'});
				return;
			}
			if('${param.okCallBack}'.length>0){
			     eval(${param.okCallBack}(rows,true));
			     pgChos = [];
				 rowChos = [];
				 $('#goods-table').trigger("reloadGrid");
			}
		}).show();
	}
	$(".select-ok").click(function(){
		var rowids = $('#goods-table').jqGrid('getGridParam',"selrow");
		var rows = [];
		if(rowids!=null){
			for(var i=0;i<rowids.length;i++){
				var row = $('#goods-table').jqGrid("getRowData",rowids[i]);
				rows.push(row);
			}
		}
		<c:if test='${!param.singleSelect}'>
		rows = [];
		for(var i  in rowChos){
			rows = rows.concat(rowChos[i]);
		}
		</c:if>
		if(rows.length==0){
			$.fool.alert({msg:'你没有选择任何数据！'});
			return;
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
});
$('#winForm').bind("keydown",function(e){
	if(e.keyCode == 13){
		$("#winForm .go-search").click();
	}
});
</script>

</body>
</html>
