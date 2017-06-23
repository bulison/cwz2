var type = new Array();
type[0]="字符";
type[1]="布尔";
type[2]="数字";
type[3]="日期";
type[4]="可选值";
/**
 * 财务参数设置
 */
var win;
jQuery(function($){
	//列表。添加按钮
	$("#add").click(function(){
		$("#my-window").fool('window',{modal:true,title:$(this).attr('_title'),href:$(this).attr('href'),onClose:function(){
			$("#my-window").window('clear');
		},onBeforeClose:function(){
			closeWin();
		}});
		return false;
	});
	$('#dataTable').datagrid();
	setPager($('#dataTable'));
});

function changeTypeAction(val){
	return type[val];
}

function recordStatusAction(val){
	return "";
}
function actionFormat(value,row,index){
	return "<a href='#' class='btn-edit' onclick='editRow(this)' title='编辑'></a>";;
}
function editRow(value){
	
}
//详细页。关闭窗口方法
function closeWin(){
	if(win)	win.window('close').window('clear');
} 