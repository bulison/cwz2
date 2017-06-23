/**
 * 工资统计
 */
var columns="";
var texts = [];
var thead = [];
var tfoot = [];
var total=[];
var col={};
//年份
var yearData = new Array();
var nowd = new Date();  
var yf = nowd.getFullYear(); 
for(var i = 5;i>=1;i--){
	yearData.push({
		"year": yf-i,
	    "text": yf-i});
}
yearData.push({
	"year": yf,
    "text": yf,
    "selected":true});
for(var i = 1;i<=5;i++){
	yearData.push({
		"year": yf+i,
	    "text": yf+i});
}
//获取今年的统计列表
$.post(getRootPath()+'/wage/wageMonthsJson',{year:yf}, function(months) {
	dataGrid(months,yf);
});
/*//部门下拉列表
$("#orgcombo").combotree({
	url:getRootPath()+"/orgController/getAllTree",
	required:true,
	novalidate:true,
	height:30,
	width:167,
	prompt:"选择部门",
	onLoadSuccess:function(node, data){
		if(data[0].id!=""){
			var node=$(this).tree("find",data[0].id);
			$(this).tree('update',{
				target: node.target,
				text: '请选择',
				id:""
			});
		}
	},
});*/

//部门初始化	
var deptValue='';	
	$.ajax({
		url:getRootPath()+"/orgController/getAllTree",
		async:false,		
		success:function(data){		  	
			deptValue=formatData(data[0].children,'id','texr');	
	    }
		});
	var deptName= $("#orgcombo").fool("dhxCombo",{
		  width:160,
		  height:32,
		clearOpt:false,
		  prompt:'部门',
		  required:true,
		  novalidate:true,
		  data:deptValue,
        toolsBar:{
            refresh:true
        },
        editable:false,
		  focusShow:true,
	});	
	
//表单
function dataGrid(months,year,deptId){
	var dataClumns = [
	             {field:'columnName',title:'项目',sortable:true,width:100}
	         ];
	$.each(months,function(key,month){
		dataClumns.push({field:month,title:month,width:100});
	});
	dataClumns.push({field:"total",title:"合计",sortable:true,width:100});
	var queryParams = {year:year,deptId:deptId};
	$.post(getRootPath()+'/wage/wageStat',queryParams, function(data) {
		var jsons = eval(data);
		var datas = new Array();
		$.each(jsons,function(index,value){
			var total=0;
			for(var x in value){
				if(x!='columnName'){
				   total += value[x];
				}
			}
			var obj = $.extend(value,{total:total});
			datas.push(obj);
		});
		for(var i=0;i<datas.length;i++){
			total.push(datas[i].total);
		}
		$('#wageList').datagrid({
			data:datas,
			pagination:false,
			fitColumns:true,
			singleSelect:true,
			remoteSort:false,
			columns:[dataClumns],
		});
	});
	columns=dataClumns;
	if(columns){
		for(var i=0;i<columns.length;i++){
			col.key=columns[i].field;
			col.title=columns[i].title;
			thead.push(col);
			col={};
		}
	}
}
$('#clear').click(function(e){
	//$("#form").form("clear");
	cleanBoxInput($("#form"));
	deptName.setComboText("");
	deptName.setComboValue("");
});
$('#search').click(function(e){
	var year=$("#selectYear").combobox('getValue'); 
	//var deptId=$("#orgcombo").combobox('getValue');
	var deptId=deptName.getSelectedValue();//获取控件值
	$.post(getRootPath()+'/wage/wageMonthsJson',{year:year,deptId:deptId}, function(months) {
		dataGrid(months,year,deptId);
	});
});
