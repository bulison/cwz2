<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div class="window-search-box" style="margin:5px;">
<input id="subjectType"/>
<input id="searchKey"/><!-- 增加筛选框 -->
<a href="javascript:;" class="btn-blue btn-s search-ok">筛选</a>
<a href="javascript:;" class="btn-blue btn-s select-ok">确定</a>
</div>

<table id="customer-table" >
<thead>
<tr>
<th data-options="field:'fid',checkbox:true"></th>
<th data-options="field:'unitId',hidden:true"></th>
<th data-options="field:'unitName',hidden:true"></th>
<th data-options="field:'currencyId',hidden:true"></th>
<th data-options="field:'currencyName',hidden:true"></th>
<th data-options="field:'code',width:10,sortable:false">科目编号</th>
<th data-options="field:'name',width:10,sortable:false">科目名</th>
<th data-options="field:'direction',width:10,sortable:false,formatter:dir">余额方向</th>

</tr>
</thead>
</table>

<script type="text/javascript">
$(function(){
	$('#customer-table').treegrid({
		<c:choose>
		   <c:when test='${param.singleSelect}'>singleSelect:true,</c:when>
		   <c:otherwise>singleSelect:false,</c:otherwise>
		</c:choose>
		scrollbarSize:0,
		idField:'fid',
		treeField:'code',
		fitColumns:true,
		url:'${ctx}/fiscalSubject/getTree?direction=${param.direction}&cashSubject=${param.cashSubject}&bankSubject=${param.bankSubject}&showRoot=0&flag=${param.flag}&closeLevel=1',
		queryParams:{searchType:2,searchKey:"${param.subjectType}"},
		onDblClickRow:function(row){
			if(row.code=="ROOT"){
				$.fool.alert({time:2000,msg:'不能选择根科目！'});
				return false;
			}
			if('${param.onDblClick}'.length>0){
				if(!'${param.onlyLeafCheck}'||row.flag!=0){
					eval(${param.onDblClick}(row,"${param.other}"));
				}
			}
		},
		onCheck:function(node){
			if('${param.onlyLeafCheck}'&&node.flag==0){
				$('#customer-table').treegrid("unselect",node.id);
			}
		},
		onCheckAll:function(rows){
			if('${param.onlyLeafCheck}'){
				for(var i=0;i<rows.length;i++){
					if(rows[i].flag==0){
						$('#customer-table').treegrid("unselect",rows[i].id);
					}
				}
			}
		},
		onLoadSuccess:function(row, data){
			
		}
	});
	
	$("#searchKey").textbox({
		width:164,
		height:31,
		prompt:"输入名称或编号或助记码"
	});
	
	$("#subjectType").fool("dhxCombo",{
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		width:164,
		height:31,
		value:"${param.subjectType}", 
		data:[{value:"",text:'全部'},{value:"1",text:'资产'},{value:"2",text:'负债'},{value:"3",text:'共同'},{value:"4",text:'所有者权益'},{value:"5",text:'成本'},{value:"6",text:'损益'}],
		focusShow:true,
		onChange:function(value,text){
			var searchKey = $('#searchKey').textbox('getValue');
			$('#customer-table').treegrid('load',{searchType:3,subjectType:value,searchKey:searchKey});
		},
	});
	
	$("#clearSear").click(function(){
		$(".window-search-box").form('clear');
	});
	
	$(".select-ok").click(function(){
		var rows = $('#customer-table').treegrid('getSelections');
		if(rows.length==0){
			$.fool.alert({time:2000,msg:'你没有选择任何数据！'});
			return;
		}
		for(var i=0;i<rows.length;i++){
			if(rows[i].code=="ROOT"){
				$.fool.alert({time:1000,msg:'不能选择根科目！'});
				return false;
			}
		}
		if('${param.okCallBack}'.length>0){
		     eval(${param.okCallBack}(rows,"${param.other}"));
		}
	});
	
	//筛选按钮点击事件
	$(".search-ok").click(function(){
		var key=$("#searchKey").textbox("getValue");
		var subjectType=$("#subjectType").next()[0].comboObj.getSelectedValue();
		$('#customer-table').treegrid("unselectAll");
		$('#customer-table').treegrid('load',{searchType:3,searchKey:key,subjectType:subjectType});
	});
});

function dir(value){
	//余额方向
	var direction = [
	             {id:'1',name:"借方"},
	             {id:'-1',name:"贷方"}
	             ];
	for(var i=0; i<direction.length; i++){
		if (direction[i].id == value) return direction[i].name;
	}
	return value;
}

</script>
</body>
</html>
