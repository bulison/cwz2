
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
	//setPager($('#dataTable'));
});
function actionFormat(value,row,index){
	return "<a href='javascript:;' class='btn-edit' onclick='edit(\""+value+"\")' title='编辑'></a>";;
}
function edit(value){
	$("#edit-window").fool('window',{modal:true,title:"编辑财务参数设置",width:$(window).width()-320,
		height:$(window).height()-120,href:getRootPath()+"/fiscalConfig/edit?fid="+value});
}
//详细页。关闭窗口方法
function closeWin(){
	if(win)	win.window('close').window('clear');
} 
var array = new Array();
